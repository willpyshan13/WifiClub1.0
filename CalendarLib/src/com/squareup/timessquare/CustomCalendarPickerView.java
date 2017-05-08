package com.squareup.timessquare;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.timessquare.CustomMonthCellDescriptor.RangeState;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;

/**
 * Android component to allow picking a date from a calendar view (a list of months).  Must be
 * initialized after inflation with {@link #init(java.util.Date, java.util.Date, java.util.List<com.squareup.timessquare.JsonTeeTimeCalendar.DataList.DateStatus>)} and can be customized with any of the
 * {@link com.squareup.timessquare.CustomCalendarPickerView.FluentInitializer} methods returned.  The currently selected date can be retrieved with
 * {@link #getSelectedDate()}.
 */
@SuppressWarnings("UnusedDeclaration")
public class CustomCalendarPickerView extends ListView {

    private final static String FLAG_YES = "1";

    final CustomMonthView.Listener listener = new CellClickedListener();
    final List<CustomMonthDescriptor> months = new ArrayList<CustomMonthDescriptor>();
    final List<CustomMonthCellDescriptor> selectedCells = new ArrayList<>();
    final List<CustomMonthCellDescriptor> highlightedCells = new ArrayList<>();
    final List<Calendar> selectedCals = new ArrayList<Calendar>();
    final List<Calendar> highlightedCals = new ArrayList<Calendar>();
    private final MonthAdapter adapter;
    private final List<List<List<CustomMonthCellDescriptor>>> cells =
            new ArrayList<List<List<CustomMonthCellDescriptor>>>();
    private final CustomJsonAdvancedSetting yearDateStatus = new CustomJsonAdvancedSetting();
    private final List<List<List<CustomMonthCellDescriptor>>> colors =
            new ArrayList<List<List<CustomMonthCellDescriptor>>>();
    SelectionMode selectionMode;
    Calendar today;
    private JsonTeeTimeCalendar.DataList.DateStatus dateStatus = new JsonTeeTimeCalendar.DataList.DateStatus();
    private Locale locale;
    private DateFormat monthNameFormat;
    private DateFormat weekdayNameFormat;
    private DateFormat fullDateFormat;
    private Calendar minCal;
    private Calendar maxCal;
    private Calendar monthCounter;
    private boolean displayOnly;
    private int dividerColor;
    private int dayBackgroundResId;
    private int dayTextColorResId;
    private int titleTextColor;
    private boolean displayHeader;
    private int headerTextColor;
    private Typeface titleTypeface;
    private Typeface dateTypeface;
    private int firstDay;
    private BeforeSelectDateListener dateBeforeSelectListener;
    private OnDateSelectedListener dateListener;
    private DateSelectableFilter dateConfiguredListener;
    OnClickListener monthClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView tv = (TextView) view;
            int month = (Integer) view.getTag();
            clearSelectionsWithMonth(month);
            validateAndUpdate();
            if (!tv.isSelected()) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, minCal.get(Calendar.YEAR));
                calendar.set(Calendar.MONTH, month);
                for (int i = 1; i <= 31; i++) {
                    calendar.set(Calendar.DAY_OF_MONTH, i);
                    Date date = calendar.getTime();
                    if (calendar.get(MONTH) == month) {
                        selectDate(date, false);
                    }
                }
            }
            tv.setSelected(!tv.isSelected());
        }
    };
    private OnInvalidDateSelectedListener invalidDateListener =
            new DefaultOnInvalidDateSelectedListener();
    private CellClickInterceptor cellClickInterceptor;

    public CustomCalendarPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Resources res = context.getResources();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarPickerView);
        final int bg = a.getColor(R.styleable.CalendarPickerView_android_background,
                res.getColor(R.color.calendar_bg));
        dividerColor = a.getColor(R.styleable.CalendarPickerView_dividerColor,
                res.getColor(R.color.calendar_divider));
        dayBackgroundResId = a.getResourceId(R.styleable.CalendarPickerView_dayBackground,
                R.drawable.calendar_bg_selector);
        dayTextColorResId = a.getResourceId(R.styleable.CalendarPickerView_dayTextColor,
                R.color.calendar_text_selector);
        titleTextColor = a.getColor(R.styleable.CalendarPickerView_titleTextColor,
                res.getColor(R.color.calendar_text_active));
        displayHeader = a.getBoolean(R.styleable.CalendarPickerView_displayHeader, true);
        headerTextColor = a.getColor(R.styleable.CalendarPickerView_headerTextColor,
                res.getColor(R.color.calendar_text_week));
        a.recycle();

        adapter = new MonthAdapter();
        setDivider(null);
        setDividerHeight(0);
        setBackgroundColor(bg);
        setCacheColorHint(bg);
        locale = Locale.getDefault();
        today = Calendar.getInstance(locale);
        minCal = Calendar.getInstance(locale);
        maxCal = Calendar.getInstance(locale);
        monthCounter = Calendar.getInstance(locale);
        monthNameFormat = new SimpleDateFormat(context.getString(R.string.month_name_format), locale);
        weekdayNameFormat = new SimpleDateFormat(context.getString(R.string.day_name_format), locale);
        fullDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);

        if (isInEditMode()) {
            Calendar nextYear = Calendar.getInstance(locale);
            nextYear.add(Calendar.YEAR, 1);

            init(new Date(), nextYear.getTime(), yearDateStatus) //
                    .withSelectedDate(new Date());
        }
    }

    /**
     * Returns a string summarizing what the client sent us for init() params.
     */
    private static String dbg(Date minDate, Date maxDate) {
        return "minDate: " + minDate + "\nmaxDate: " + maxDate;
    }

    /**
     * Clears out the hours/minutes/seconds/millis of a Calendar.
     */
    static void setMidnight(Calendar cal) {
        cal.set(HOUR_OF_DAY, 0);
        cal.set(MINUTE, 0);
        cal.set(SECOND, 0);
        cal.set(MILLISECOND, 0);
    }

    private static boolean containsDate(List<Calendar> selectedCals, Calendar cal) {
        for (Calendar selectedCal : selectedCals) {
            if (sameDate(cal, selectedCal)) {
                return true;
            }
        }
        return false;
    }

    private static Calendar minDate(List<Calendar> selectedCals) {
        if (selectedCals == null || selectedCals.size() == 0) {
            return null;
        }
        Collections.sort(selectedCals);
        return selectedCals.get(0);
    }

    private static Calendar maxDate(List<Calendar> selectedCals) {
        if (selectedCals == null || selectedCals.size() == 0) {
            return null;
        }
        Collections.sort(selectedCals);
        return selectedCals.get(selectedCals.size() - 1);
    }

    private static boolean sameDate(Calendar cal, Calendar selectedDate) {
        return cal.get(MONTH) == selectedDate.get(MONTH)
                && cal.get(YEAR) == selectedDate.get(YEAR)
                && cal.get(DAY_OF_MONTH) == selectedDate.get(DAY_OF_MONTH);
    }

    private static boolean betweenDates(Calendar cal, Calendar minCal, Calendar maxCal) {
        final Date date = cal.getTime();
        return betweenDates(date, minCal, maxCal);
    }

    static boolean betweenDates(Date date, Calendar minCal, Calendar maxCal) {
        final Date min = minCal.getTime();
        return (date.equals(min) || date.after(min)) // >= minCal
                && date.before(maxCal.getTime()); // && < maxCal
    }

    private static boolean sameMonth(Calendar cal, CustomMonthDescriptor month) {
        return (cal.get(MONTH) == month.getMonth() && cal.get(YEAR) == month.getYear());
    }

    /**
     * Both date parameters must be non-null and their {@link java.util.Date#getTime()} must not return 0. Time
     * of day will be ignored.  For instance, if you pass in {@code minDate} as 11/16/2012 5:15pm and
     * {@code maxDate} as 11/16/2013 4:30am, 11/16/2012 will be the first selectable date and
     * 11/15/2013 will be the last selectable date ({@code maxDate} is exclusive).
     * <p/>
     * This will implicitly set the {@link com.squareup.timessquare.CustomCalendarPickerView.SelectionMode} to {@link com.squareup.timessquare.CalendarPickerView.SelectionMode#SINGLE}.  If you
     * want a different selection mode, use {@link com.squareup.timessquare.CustomCalendarPickerView.FluentInitializer#inMode(com.squareup.timessquare.CalendarPickerView.SelectionMode)} on the
     * {@link com.squareup.timessquare.CustomCalendarPickerView.FluentInitializer} this method returns.
     * <p/>
     * The calendar will be constructed using the given locale. This means that all names
     * (months, days) will be in the language of the locale and the weeks start with the day
     * specified by the locale.
     *
     * @param minDate Earliest selectable date, inclusive.  Must be earlier than {@code maxDate}.
     * @param maxDate Latest selectable date, exclusive.  Must be later than {@code minDate}.
     */
    public FluentInitializer init(Date minDate, Date maxDate, Locale locale, CustomJsonAdvancedSetting dateStatuses, int firstDayOfWeek) {
        if (minDate == null || maxDate == null) {
            throw new IllegalArgumentException(
                    "minDate and maxDate must be non-null.  " + dbg(minDate, maxDate));
        }
        if (minDate.after(maxDate)) {
            throw new IllegalArgumentException(
                    "minDate must be before maxDate.  " + dbg(minDate, maxDate));
        }
        if (minDate.getTime() == 0 || maxDate.getTime() == 0) {
            throw new IllegalArgumentException(
                    "minDate and maxDate must be non-zero.  " + dbg(minDate, maxDate));
        }
        if (locale == null) {
            throw new IllegalArgumentException("Locale is null.");
        }

        // Make sure that all calendar instances use the same locale.
        this.locale = locale;
        firstDay = firstDayOfWeek;
        today = Calendar.getInstance(locale);
        minCal = Calendar.getInstance(locale);
        maxCal = Calendar.getInstance(locale);
        monthCounter = Calendar.getInstance(locale);
        monthNameFormat =
                new SimpleDateFormat(getContext().getString(R.string.month_name_format), locale);
        for (CustomMonthDescriptor month : months) {
            month.setLabel(monthNameFormat.format(month.getDate()));
        }
        weekdayNameFormat =
                new SimpleDateFormat(getContext().getString(R.string.day_name_format), locale);
        fullDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);

        this.selectionMode = SelectionMode.SINGLE;
        // Clear out any previously-selected dates/cells.
        selectedCals.clear();
        selectedCells.clear();
        highlightedCals.clear();
        highlightedCells.clear();

        // Clear previous state.
        cells.clear();
        months.clear();
        minCal.setTime(minDate);
        maxCal.setTime(maxDate);
        setMidnight(minCal);
        setMidnight(maxCal);
        displayOnly = false;

        // maxDate is exclusive: bump back to the previous day so if maxDate is the first of a month,
        // we don't accidentally include that month in the view.
        maxCal.add(MINUTE, -1);

        // Now iterate between minCal and maxCal and build up our list of months to show.
        monthCounter.setTime(minCal.getTime());
        final int maxMonth = maxCal.get(MONTH);
        final int maxYear = maxCal.get(YEAR);
        while ((monthCounter.get(MONTH) <= maxMonth // Up to, including the month.
                || monthCounter.get(YEAR) < maxYear) // Up to the year.
                && monthCounter.get(YEAR) < maxYear + 1) { // But not > next yr.
            Date date = monthCounter.getTime();
            CustomMonthDescriptor month =
                    new CustomMonthDescriptor(monthCounter.get(MONTH), monthCounter.get(YEAR), date,
                            monthNameFormat.format(date));


            cells.add(getMonthCells(month, monthCounter, dateStatuses, firstDayOfWeek));
            months.add(month);
            monthCounter.add(MONTH, 1);
        }

        validateAndUpdate();
        return new FluentInitializer();
    }

    /**
     * Both date parameters must be non-null and their {@link java.util.Date#getTime()} must not return 0. Time
     * of day will be ignored.  For instance, if you pass in {@code minDate} as 11/16/2012 5:15pm and
     * {@code maxDate} as 11/16/2013 4:30am, 11/16/2012 will be the first selectable date and
     * 11/15/2013 will be the last selectable date ({@code maxDate} is exclusive).
     * <p/>
     * This will implicitly set the {@link com.squareup.timessquare.CustomCalendarPickerView.SelectionMode} to {@link com.squareup.timessquare.CalendarPickerView.SelectionMode#SINGLE}.  If you
     * want a different selection mode, use {@link com.squareup.timessquare.CustomCalendarPickerView.FluentInitializer#inMode(com.squareup.timessquare.CalendarPickerView.SelectionMode)} on the
     * {@link com.squareup.timessquare.CustomCalendarPickerView.FluentInitializer} this method returns.
     * <p/>
     * The calendar will be constructed using the default locale as returned by
     * {@link java.util.Locale#getDefault()}. If you wish the calendar to be constructed using a
     *
     * @param minDate Earliest selectable date, inclusive.  Must be earlier than {@code maxDate}.
     * @param maxDate Latest selectable date, exclusive.  Must be later than {@code minDate}.
     */
    public FluentInitializer init(Date minDate, Date maxDate, CustomJsonAdvancedSetting dateStatus) {
        return init(minDate, maxDate, Locale.getDefault(), dateStatus, 0);
    }

    public void validateAndUpdate() {
        if (getAdapter() == null) {
            setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
    }

    private void scrollToSelectedMonth(final int selectedIndex) {
        scrollToSelectedMonth(selectedIndex, false);
    }

    private void scrollToSelectedMonth(final int selectedIndex, final boolean smoothScroll) {
        post(new Runnable() {
            @Override
            public void run() {
                Logr.d("Scrolling to position %d", selectedIndex);

                if (smoothScroll) {
                    smoothScrollToPosition(selectedIndex);
                } else {
                    setSelection(selectedIndex);
                }
            }
        });
    }

    private void scrollToSelectedDates() {
        Integer selectedIndex = null;
        Integer todayIndex = null;
        Calendar today = Calendar.getInstance(locale);
        for (int c = 0; c < months.size(); c++) {
            CustomMonthDescriptor month = months.get(c);
            if (selectedIndex == null) {
                for (Calendar selectedCal : selectedCals) {
                    if (sameMonth(selectedCal, month)) {
                        selectedIndex = c;
                        break;
                    }
                }
                if (selectedIndex == null && todayIndex == null && sameMonth(today, month)) {
                    todayIndex = c;
                }
            }
        }
        if (selectedIndex != null) {
            scrollToSelectedMonth(selectedIndex);
        } else if (todayIndex != null) {
            scrollToSelectedMonth(todayIndex);
        }
    }

    public boolean scrollToDate(Date date) {
        Integer selectedIndex = null;

        Calendar cal = Calendar.getInstance(locale);
        cal.setTime(date);
        for (int c = 0; c < months.size(); c++) {
            CustomMonthDescriptor month = months.get(c);
            if (sameMonth(cal, month)) {
                selectedIndex = c;
                break;
            }
        }
        if (selectedIndex != null) {
            scrollToSelectedMonth(selectedIndex);
            return true;
        }
        return false;
    }

    /**
     * This method should only be called if the calendar is contained in a dialog, and it should only
     * be called once, right after the dialog is shown (using
     * {@link android.content.DialogInterface.OnShowListener} or
     * {@link android.app.DialogFragment#onStart()}).
     */
    public void fixDialogDimens() {
        Logr.d("Fixing dimensions to h = %d / w = %d", getMeasuredHeight(), getMeasuredWidth());
        // Fix the layout height/width after the dialog has been shown.
        getLayoutParams().height = getMeasuredHeight();
        getLayoutParams().width = getMeasuredWidth();
        // Post this runnable so it runs _after_ the dimen changes have been applied/re-measured.
        post(new Runnable() {
            @Override
            public void run() {
                Logr.d("Dimens are fixed: now scroll to the selected date");
                scrollToSelectedDates();
            }
        });
    }

    /**
     * Set the typeface to be used for month titles.
     */
    public void setTitleTypeface(Typeface titleTypeface) {
        this.titleTypeface = titleTypeface;
        validateAndUpdate();
    }

    /**
     * Sets the typeface to be used within the date grid.
     */
    public void setDateTypeface(Typeface dateTypeface) {
        this.dateTypeface = dateTypeface;
        validateAndUpdate();
    }

    /**
     * Sets the typeface to be used for all text within this calendar.
     */
    public void setTypeface(Typeface typeface) {
        setTitleTypeface(typeface);
        setDateTypeface(typeface);
    }

    /**
     * This method should only be called if the calendar is contained in a dialog, and it should only
     * be called when the screen has been rotated and the dialog should be re-measured.
     */
    public void unfixDialogDimens() {
        Logr.d("Reset the fixed dimensions to allow for re-measurement");
        // Fix the layout height/width after the dialog has been shown.
        getLayoutParams().height = LayoutParams.MATCH_PARENT;
        getLayoutParams().width = LayoutParams.MATCH_PARENT;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (months.isEmpty()) {
            throw new IllegalStateException(
                    "Must have at least one month to display.  Did you forget to call init()?");
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public Date getSelectedDate() {
        return (selectedCals.size() > 0 ? selectedCals.get(0).getTime() : null);
    }

    public List<Date> getSelectedDates() {
        List<Date> selectedDates = new ArrayList<>();
        for (CustomMonthCellDescriptor cal : selectedCells) {
            selectedDates.add(cal.getDate());
        }
        Collections.sort(selectedDates);
        return selectedDates;
    }

    public void setSelectedDates(List<Date> selectedDates) {
        selectDate(new Date(), false);
    }

    /**
     * Select a new date.  Respects the {@link com.squareup.timessquare.CustomCalendarPickerView.SelectionMode} this CalendarPickerView is configured
     * with: if you are in {@link com.squareup.timessquare.CustomCalendarPickerView.SelectionMode#SINGLE}, the previously selected date will be
     * un-selected.  In {@link com.squareup.timessquare.CustomCalendarPickerView.SelectionMode#MULTIPLE}, the new date will be added to the list of
     * selected dates.
     * <p/>
     * If the selection was made (selectable date, in range), the view will scroll to the newly
     * selected date if it's not already visible.
     *
     * @return - whether we were able to set the date
     */
    public boolean selectDate(Date date) {
        return selectDate(date, false);
    }

    /**
     * Select a new date.  Respects the {@link com.squareup.timessquare.CustomCalendarPickerView.SelectionMode} this CalendarPickerView is configured
     * with: if you are in {@link com.squareup.timessquare.CustomCalendarPickerView.SelectionMode#SINGLE}, the previously selected date will be
     * un-selected.  In {@link com.squareup.timessquare.CustomCalendarPickerView.SelectionMode#MULTIPLE}, the new date will be added to the list of
     * selected dates.
     * <p/>
     * If the selection was made (selectable date, in range), the view will scroll to the newly
     * selected date if it's not already visible.
     *
     * @return - whether we were able to set the date
     */
    public boolean selectDate(Date date, boolean smoothScroll) {
        validateDate(date);

        MonthCellWithMonthIndex monthCellWithMonthIndex = getMonthCellWithIndexByDate(date);
        if (monthCellWithMonthIndex == null || !isDateSelectable(date)) {
            return false;
        }
        boolean wasSelected = doSelectDate(date, monthCellWithMonthIndex.cell);
        if (wasSelected) {
            scrollToSelectedMonth(monthCellWithMonthIndex.monthIndex, smoothScroll);
        }
        return wasSelected;
    }

    private void validateDate(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Selected date must be non-null.");
        }
        if (date.getTime() == 0) {
            throw new IllegalArgumentException("Selected date must be non-zero.  " + date);
        }
        if (date.before(minCal.getTime()) || date.after(maxCal.getTime())) {
            throw new IllegalArgumentException(String.format(
                    "SelectedDate must be between minDate and maxDate."
                            + "%nminDate: %s%nmaxDate: %s%nselectedDate: %s", minCal.getTime(), maxCal.getTime(),
                    date));
        }
    }

    private boolean doSelectDate(Date date, CustomMonthCellDescriptor cell) {
        Calendar newlySelectedCal = Calendar.getInstance(locale);
        newlySelectedCal.setTime(date);
        // Sanitize input: clear out the hours/minutes/seconds/millis.
        setMidnight(newlySelectedCal);

        // Clear any remaining range state.
        for (CustomMonthCellDescriptor selectedCell : selectedCells) {
            selectedCell.setRangeState(RangeState.NONE);
        }

        switch (selectionMode) {
            case RANGE:
                if (selectedCals.size() > 1) {
                    // We've already got a range selected: clear the old one.
                    clearOldSelections();
                } else if (selectedCals.size() == 1 && newlySelectedCal.before(selectedCals.get(0))) {
                    // We're moving the start of the range back in time: clear the old start date.
                    clearOldSelections();
                }
                break;

            case MULTIPLE:
                date = applyMultiSelect(date, newlySelectedCal);
                break;

            case SINGLE:
                clearOldSelections();
                break;
            default:
                throw new IllegalStateException("Unknown selectionMode " + selectionMode);
        }

        if (date != null) {
            // Select a new cell.
            if (selectedCells.size() == 0 || !selectedCells.get(0).equals(cell)) {
                selectedCells.add(cell);
                cell.setSelected(true);
            }
            selectedCals.add(newlySelectedCal);

            if (selectionMode == SelectionMode.RANGE && selectedCells.size() > 1) {
                // Select all days in between start and end.
                Date start = selectedCells.get(0).getDate();
                Date end = selectedCells.get(1).getDate();
                selectedCells.get(0).setRangeState(CustomMonthCellDescriptor.RangeState.FIRST);
                selectedCells.get(1).setRangeState(CustomMonthCellDescriptor.RangeState.LAST);

                for (List<List<CustomMonthCellDescriptor>> month : cells) {
                    for (List<CustomMonthCellDescriptor> week : month) {
                        for (CustomMonthCellDescriptor singleCell : week) {
                            if (singleCell.getDate().after(start)
                                    && singleCell.getDate().before(end)
                                    && singleCell.isSelectable()) {
                                singleCell.setSelected(true);
                                singleCell.setRangeState(CustomMonthCellDescriptor.RangeState.MIDDLE);
                                selectedCells.add(singleCell);
                            }
                        }
                    }
                }
            }
        }

        // Update the adapter.
        validateAndUpdate();
        return date != null;
    }

    public void clearOldSelections() {
        for (CustomMonthCellDescriptor selectedCell : selectedCells) {
            // De-select the currently-selected cell.
            selectedCell.setSelected(false);
        }
        selectedCells.clear();
        selectedCals.clear();
    }

    private Date applyMultiSelect(Date date, Calendar selectedCal) {
        for (CustomMonthCellDescriptor selectedCell : selectedCells) {
            if (selectedCell.getDate().equals(date)) {
                // De-select the currently-selected cell.
                selectedCell.setSelected(false);
                selectedCells.remove(selectedCell);
                date = null;
                break;
            }
        }
        for (Calendar cal : selectedCals) {
            if (sameDate(cal, selectedCal)) {
                selectedCals.remove(cal);
                break;
            }
        }
        return date;
    }

    public void highlightDates(Collection<Date> dates) {
        for (Date date : dates) {
            validateDate(date);

            MonthCellWithMonthIndex monthCellWithMonthIndex = getMonthCellWithIndexByDate(date);
            if (monthCellWithMonthIndex != null) {
                Calendar newlyHighlightedCal = Calendar.getInstance();
                newlyHighlightedCal.setTime(date);
                CustomMonthCellDescriptor cell = monthCellWithMonthIndex.cell;

                highlightedCells.add(cell);
                highlightedCals.add(newlyHighlightedCal);
                cell.setHighlighted(true);
            }
        }

        validateAndUpdate();
    }

    public void clearHighlightedDates() {
        for (CustomMonthCellDescriptor cal : highlightedCells) {
            cal.setHighlighted(false);
        }
        highlightedCells.clear();
        highlightedCals.clear();

        validateAndUpdate();
    }

    /**
     * Return cell and month-index (for scrolling) for a given Date.
     */
    private MonthCellWithMonthIndex getMonthCellWithIndexByDate(Date date) {
        int index = 0;
        Calendar searchCal = Calendar.getInstance(locale);
        searchCal.setTime(date);
        Calendar actCal = Calendar.getInstance(locale);

        for (List<List<CustomMonthCellDescriptor>> monthCells : cells) {
            for (List<CustomMonthCellDescriptor> weekCells : monthCells) {
                for (CustomMonthCellDescriptor actCell : weekCells) {
                    actCal.setTime(actCell.getDate());
                    if (sameDate(actCal, searchCal) && actCell.isSelectable()) {
                        return new MonthCellWithMonthIndex(actCell, index);
                    }
                }
            }
            index++;
        }
        return null;
    }

    private void clearSelectionsWithMonth(int month) {
        Calendar calendar = Calendar.getInstance(locale);
        List<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < selectedCells.size(); i++) {
            CustomMonthCellDescriptor selectedCell = selectedCells.get(i);
            Date date = selectedCell.getDate();
            calendar.setTime(date);
            if (calendar.get(Calendar.MONTH) == month) {
                indexList.add(i);
                selectedCell.setSelected(false);
            }
        }
        for (int i = indexList.size() - 1; i >= 0; i--) {
            int index = indexList.get(i);
            selectedCells.remove(index);
        }
        indexList.clear();
        for (int i = 0; i < selectedCals.size(); i++) {
            Calendar c = selectedCals.get(i);
            if (c.get(Calendar.MONTH) == month) {
                indexList.add(i);
            }
        }
        for (int i = indexList.size() - 1; i >= 0; i--) {
            int index = indexList.get(i);
            selectedCals.remove(index);
        }
    }

    private void clearSelectionsWithMonth(int month, int weekDay) {
        Calendar calendar = Calendar.getInstance(locale);
        List<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < selectedCells.size(); i++) {
            CustomMonthCellDescriptor selectedCell = selectedCells.get(i);
            Date date = selectedCell.getDate();
            calendar.setTime(date);
            if (calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.DAY_OF_WEEK) == weekDay) {
                indexList.add(i);
                selectedCell.setSelected(false);
            }
        }
        for (int i = indexList.size() - 1; i >= 0; i--) {
            int index = indexList.get(i);
            selectedCells.remove(index);
        }
        indexList.clear();
        for (int i = 0; i < selectedCals.size(); i++) {
            Calendar c = selectedCals.get(i);
            if (c.get(Calendar.MONTH) == month && calendar.get(Calendar.DAY_OF_WEEK) == weekDay) {
                indexList.add(i);
            }
        }
        for (int i = indexList.size() - 1; i >= 0; i--) {
            int index = indexList.get(i);
            selectedCals.remove(index);
        }
    }

    List<List<CustomMonthCellDescriptor>> getMonthCells(CustomMonthDescriptor month, Calendar startCal, CustomJsonAdvancedSetting dateStatuses, int firstDay) {
        int flag = 0;
        Calendar cal = Calendar.getInstance(locale);
        cal.setTime(startCal.getTime());


        HashMap<String, CustomJsonAdvancedSetting.DaySetting> statusList = null;
        CustomJsonAdvancedSetting.DaySetting daySetting = null;
        if (dateStatuses.getDataList() != null && dateStatuses.getDataList().size() > 0) {
            statusList = dateStatuses.getDataList().get(month.getMonth());
        }

        if (firstDay == 0) {
            cal.setFirstDayOfWeek(Calendar.SUNDAY);
        } else {
            cal.setFirstDayOfWeek(Calendar.MONDAY);
        }

        List<List<CustomMonthCellDescriptor>> cells = new ArrayList<>();
        cal.set(DAY_OF_MONTH, 1);
        int firstDayOfWeek = cal.get(DAY_OF_WEEK);
        int offset = cal.getFirstDayOfWeek() - firstDayOfWeek;
        if (offset > 0) {
            offset -= 7;
        }

        cal.add(Calendar.DATE, offset);

        Calendar minSelectedCal = minDate(selectedCals);
        Calendar maxSelectedCal = maxDate(selectedCals);

        String lastSetting = "";
        int teeTimeColor = 0;
        int lastColor = 0;
        int currentColor = 0;

        while ((cal.get(MONTH) < month.getMonth() + 1 || cal.get(YEAR) < month.getYear()) //
                && cal.get(YEAR) <= month.getYear()) {
            List<CustomMonthCellDescriptor> weekCells = new ArrayList<>();
            cells.add(weekCells);
            for (int c = 0; c < 7; c++) {

                Date date = cal.getTime();
                boolean isCurrentMonth = cal.get(MONTH) == month.getMonth();
                boolean isSelected = isCurrentMonth && containsDate(selectedCals, cal);
                boolean isSelectable =
                        isCurrentMonth && betweenDates(cal, minCal, maxCal) && isDateSelectable(date);
                boolean isToday = sameDate(cal, today);
                boolean isHighlighted = containsDate(highlightedCals, cal);
                int value = cal.get(DAY_OF_MONTH);

                CustomMonthCellDescriptor.RangeState rangeState = CustomMonthCellDescriptor.RangeState.NONE;
                if (selectedCals.size() > 1) {
                    if (sameDate(minSelectedCal, cal)) {
                        rangeState = CustomMonthCellDescriptor.RangeState.FIRST;
                    } else if (sameDate(maxDate(selectedCals), cal)) {
                        rangeState = CustomMonthCellDescriptor.RangeState.LAST;
                    } else if (betweenDates(cal, minSelectedCal, maxSelectedCal)) {
                        rangeState = CustomMonthCellDescriptor.RangeState.MIDDLE;
                    }
                }

                boolean isBookingHere = false;
                boolean isNineHoles = false;
                boolean isThreeTeeStart = false;
                boolean isBlockTimes = false;
                boolean isTwoTeeStart = false;
                boolean isMemberOnly = false;
                boolean isPrimeTime = false;


                if (isCurrentMonth) {
                    if (statusList != null) {


                        String dayString = null;
                        if (value < 10) {
                            dayString = "0" + value;
                        } else {
                            dayString = String.valueOf(value);
                        }

                        CustomJsonAdvancedSetting.DaySetting daySettingTemp = statusList.get(dayString);
                        if (daySettingTemp != null) {
                            String dateData = daySettingTemp.getCdDate();
                            if (dateData != null && !"".equals(dateData)) {
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date1 = sdf.parse(dateData);
                                    if (date.equals(date1)) {
                                        daySetting = daySettingTemp;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

//                        isBookingHere = (FLAG_YES.equals(teeTimeStatusList.getBooking()));
//                        isNineHoles = (FLAG_YES.equals(teeTimeStatusList.getNineHoles()));
//                        isThreeTeeStart = (FLAG_YES.equals(teeTimeStatusList.getThreeTeeStart()));
//                        isBlockTimes = (FLAG_YES.equals(teeTimeStatusList.getBlock()));
//                        isTwoTeeStart = (FLAG_YES.equals(teeTimeStatusList.getTwoTeeStart()));
//                        isMemberOnly = (FLAG_YES.equals(teeTimeStatusList.getMemberOnly()));
//                        isPrimeTime = (FLAG_YES.equals(teeTimeStatusList.getPrimeTime()));
//                        String currentSetting = teeTimeStatusList.getTeeTimeSetting();

//                        if (currentSetting != null) {
//                            //上一个没设置并且当前有设置tee time
//                            if (("").equals(lastSetting) && !("").equals(currentSetting)) {
//                                currentColor = 1;
//                                teeTimeColor = currentColor;
//                                lastColor = currentColor;
//                            }
//                            //上一个和当前都有设置tee time,但设置不一样
//                            else if (!lastSetting.equals(currentSetting) && !("").equals(lastSetting) && !("").equals(currentSetting)) {
//                                if (lastColor == 1) {
//                                    currentColor = 2;
//                                } else {
//                                    currentColor = 1;
//                                }
//
//                                teeTimeColor = currentColor;
//                                lastColor = currentColor;
//                            }
//                            //上一个和当前都有设置tee time,并且设置一样
//                            else if (lastSetting.equals(currentSetting) && !("").equals(lastSetting) && !("").equals(currentSetting)) {
//                                currentColor = lastColor;
//                                teeTimeColor = currentColor;
//                                lastColor = currentColor;
//                            }
//                            //上一个和当前都没有设置tee time
//                            else {
//                                currentColor = 0;
//                                teeTimeColor = currentColor;
//                                lastColor = currentColor;
//                            }
//
//                        } else {
//                            currentColor = 0;
//                            teeTimeColor = currentColor;
//                            lastColor = currentColor;
//                            currentSetting = "";
//                        }
//
//                        lastSetting = currentSetting;
                    }
                    flag++;
                } else {
                    isBookingHere = false;
                    isNineHoles = false;
                    isThreeTeeStart = false;
                    isBlockTimes = false;
                    isTwoTeeStart = false;
                    isMemberOnly = false;
                    isPrimeTime = false;
                    teeTimeColor = 0;
                    daySetting = null;
                }

                /*weekCells.add(
                        new CustomMonthCellDescriptor(date, isCurrentMonth, isSelectable, isSelected, isToday,
                                isHighlighted, value, rangeState));*/

                weekCells.add(new CustomMonthCellDescriptor(daySetting, date, value, isCurrentMonth, isToday, isSelectable, isSelected, isHighlighted, rangeState, isBookingHere, isNineHoles, isThreeTeeStart, isBlockTimes, isTwoTeeStart, isMemberOnly, isPrimeTime, teeTimeColor));
                cal.add(DATE, 1);

            }
        }
        return cells;
    }

    private boolean containsDate(List<Calendar> selectedCals, Date date) {
        Calendar cal = Calendar.getInstance(locale);
        cal.setTime(date);
        return containsDate(selectedCals, cal);
    }

    private boolean isDateSelectable(Date date) {
        return dateConfiguredListener == null || dateConfiguredListener.isDateSelectable(date);
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        dateListener = listener;
    }

    /**
     * Set a listener to react to user selection of a disabled date.
     *
     * @param listener the listener to set, or null for no reaction
     */
    public void setOnInvalidDateSelectedListener(OnInvalidDateSelectedListener listener) {
        invalidDateListener = listener;
    }

    /**
     * Set a listener used to discriminate between selectable and unselectable dates. Set this to
     * disable arbitrary dates as they are rendered.
     * <p/>
     * Important: set this before you call {@link #init(java.util.Date, java.util.Date, java.util.List<com.squareup.timessquare.JsonTeeTimeCalendar.DataList.DateStatus>)} methods.  If called afterwards,
     * it will not be consistently applied.
     */
    public void setDateSelectableFilter(DateSelectableFilter listener) {
        dateConfiguredListener = listener;
    }

    /**
     * Set a listener to intercept clicks on calendar cells.
     */
    public void setCellClickInterceptor(CellClickInterceptor listener) {
        cellClickInterceptor = listener;
    }

    public void setDateBeforeSelectListener(BeforeSelectDateListener dateBeforeSelectListener) {
        this.dateBeforeSelectListener = dateBeforeSelectListener;
    }

    public enum SelectionMode {
        /**
         * Only one date will be selectable.  If there is already a selected date and you select a new
         * one, the old date will be unselected.
         */
        SINGLE,
        /**
         * Multiple dates will be selectable.  Selecting an already-selected date will un-select it.
         */
        MULTIPLE,
        /**
         * Allows you to select a date range.  Previous selections are cleared when you either:
         * <ul>
         * <li>Have a range selected and select another date (even if it's in the current range).</li>
         * <li>Have one date selected and then select an earlier date.</li>
         * </ul>
         */
        RANGE
    }

    /**
     * Interface to be notified when a new date is selected or unselected. This will only be called
     * when the user initiates the date selection.  If you call {@link #selectDate(java.util.Date)} this
     * listener will not be notified.
     *
     * @see #setOnDateSelectedListener(com.squareup.timessquare.CustomCalendarPickerView.OnDateSelectedListener)
     */
    public interface OnDateSelectedListener {
        void onDateSelected(Date date);

        void onDateUnselected(Date date);
    }

    /**
     * Interface to be notified when an invalid date is selected by the user. This will only be
     * called when the user initiates the date selection. If you call {@link #selectDate(java.util.Date)} this
     * listener will not be notified.
     *
     * @see #setOnInvalidDateSelectedListener(com.squareup.timessquare.CustomCalendarPickerView.OnInvalidDateSelectedListener)
     */
    public interface OnInvalidDateSelectedListener {
        void onInvalidDateSelected(Date date);
    }

    /**
     * Interface used for determining the selectability of a date cell when it is configured for
     * display on the calendar.
     *
     * @see #setDateSelectableFilter(com.squareup.timessquare.CustomCalendarPickerView.DateSelectableFilter)
     */
    public interface DateSelectableFilter {
        boolean isDateSelectable(Date date);
    }

    /**
     * Interface to be notified when a cell is clicked and possibly intercept the click.  Return true
     * to intercept the click and prevent any selections from changing.
     *
     * @see #setCellClickInterceptor(com.squareup.timessquare.CustomCalendarPickerView.CellClickInterceptor)
     */
    public interface CellClickInterceptor {
        boolean onCellClicked(Date date);
    }

    public interface BeforeSelectDateListener {
        boolean doBeforeSelect(Date date);
    }

    /**
     * Hold a cell with a month-index.
     */
    private static class MonthCellWithMonthIndex {
        public CustomMonthCellDescriptor cell;
        public int monthIndex;

        public MonthCellWithMonthIndex(CustomMonthCellDescriptor cell, int monthIndex) {
            this.cell = cell;
            this.monthIndex = monthIndex;
        }
    }

    public class FluentInitializer {
        /**
         * Override the {@link com.squareup.timessquare.CustomCalendarPickerView.SelectionMode} from the default ({@link com.squareup.timessquare.CalendarPickerView.SelectionMode#SINGLE}).
         */
        public FluentInitializer inMode(SelectionMode mode) {
            selectionMode = mode;
            validateAndUpdate();
            return this;
        }

        /**
         * Set an initially-selected date.  The calendar will scroll to that date if it's not already
         * visible.
         */
        public FluentInitializer withSelectedDate(Date selectedDates) {
            return withSelectedDates(Arrays.asList(selectedDates));
        }

        /**
         * Set multiple selected dates.  This will throw an {@link IllegalArgumentException} if you
         * pass in multiple dates and haven't already called {@link #inMode(com.squareup.timessquare.CustomCalendarPickerView.SelectionMode)}.
         */
        public FluentInitializer withSelectedDates(Collection<Date> selectedDates) {
            if (selectionMode == SelectionMode.SINGLE && selectedDates.size() > 1) {
                throw new IllegalArgumentException("SINGLE mode can't be used with multiple selectedDates");
            }
            if (selectionMode == SelectionMode.RANGE && selectedDates.size() > 2) {
                throw new IllegalArgumentException(
                        "RANGE mode only allows two selectedDates.  You tried to pass " + selectedDates.size());
            }
            if (selectedDates != null) {
                for (Date date : selectedDates) {
                    selectDate(date);
                }
            }
            scrollToSelectedDates();

            validateAndUpdate();
            return this;
        }

        public FluentInitializer withHighlightedDates(Collection<Date> dates) {
            highlightDates(dates);
            return this;
        }

        public FluentInitializer withHighlightedDate(Date date) {
            return withHighlightedDates(Arrays.asList(date));
        }

        public FluentInitializer setShortWeekdays(String[] newShortWeekdays) {
            DateFormatSymbols symbols = new DateFormatSymbols(locale);
            symbols.setShortWeekdays(newShortWeekdays);
            weekdayNameFormat =
                    new SimpleDateFormat(getContext().getString(R.string.day_name_format), symbols);
            return this;
        }

        public FluentInitializer displayOnly() {
            displayOnly = true;
            return this;
        }
    }

    private class CellClickedListener implements CustomMonthView.Listener {
        @Override
        public void handleClick(CustomMonthCellDescriptor cell) {
            Date clickedDate = cell.getDate();

            if (cellClickInterceptor != null && cellClickInterceptor.onCellClicked(clickedDate)) {
                return;
            }
            if (!betweenDates(clickedDate, minCal, maxCal) || !isDateSelectable(clickedDate)) {
                if (invalidDateListener != null) {
                    invalidDateListener.onInvalidDateSelected(clickedDate);
                }
            } else {

                if (dateBeforeSelectListener != null) {
                    boolean isContinue = dateBeforeSelectListener.doBeforeSelect(clickedDate);
                    if (!isContinue) {
                        return;
                    }
                }

                boolean wasSelected = doSelectDate(clickedDate, cell);

                if (dateListener != null) {
                    if (wasSelected) {
                        dateListener.onDateSelected(clickedDate);
                    } else {
                        dateListener.onDateUnselected(clickedDate);
                    }
                }
            }
        }
    }

    private class MonthAdapter extends BaseAdapter {
        private final LayoutInflater inflater;


        private MonthAdapter() {
            inflater = LayoutInflater.from(getContext());
        }


        @Override
        public boolean isEnabled(int position) {
            // Disable selectability: each cell will handle that itself.
            return false;
        }

        @Override
        public int getCount() {
            return months.size();
        }

        @Override
        public Object getItem(int position) {
            return months.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CustomMonthView monthView = (CustomMonthView) convertView;
            final CustomMonthDescriptor CustomMonthDescriptor = months.get(position);
            if (monthView == null) {
                monthView =
                        CustomMonthView.create(parent, inflater, weekdayNameFormat, listener, today, dividerColor,
                                dayBackgroundResId, dayTextColorResId, titleTextColor, displayHeader,
                                headerTextColor, firstDay);
            }

            OnClickListener weekSelectListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int month = CustomMonthDescriptor.getMonth();
                    int weekDay = (Integer) v.getTag();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, minCal.get(Calendar.YEAR));
                    calendar.set(Calendar.MONTH, month);
                    clearSelectionsWithMonth(month, weekDay);
                    validateAndUpdate();
                    if (!v.isSelected()) {
                        for (int i = 1; i <= 31; i++) {
                            calendar.set(Calendar.DAY_OF_MONTH, i);
                            Date date = calendar.getTime();
                            if (calendar.get(MONTH) == month && calendar.get(DAY_OF_WEEK) == weekDay) {
                                selectDate(date, false);
                            }
                        }
                    }
                    v.setSelected(!v.isSelected());
                }
            };

            monthView.init(monthClickListener, CustomMonthDescriptor, cells.get(position), displayOnly, titleTypeface,
                    dateTypeface, weekSelectListener);
            return monthView;
        }
    }

    private class DefaultOnInvalidDateSelectedListener implements OnInvalidDateSelectedListener {
        @Override
        public void onInvalidDateSelected(Date date) {
            String errMessage =
                    getResources().getString(R.string.invalid_date, fullDateFormat.format(minCal.getTime()),
                            fullDateFormat.format(maxCal.getTime()));
            Toast.makeText(getContext(), errMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
