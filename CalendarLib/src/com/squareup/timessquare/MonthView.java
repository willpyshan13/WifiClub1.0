package com.squareup.timessquare;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthView extends LinearLayout {
    TextView title;
    CalendarGridView grid;
    private Listener listener;

    private ArrayList<TextView> weekTitleContainer;

    public static MonthView create(ViewGroup parent, LayoutInflater inflater,
                                   DateFormat weekdayNameFormat, Listener listener, Calendar today, int dividerColor,
                                   int dayBackgroundResId, int dayTextColorResId, int titleTextColor, boolean displayHeader,
                                   int headerTextColor, int firstDay) {

        final MonthView view = (MonthView) inflater.inflate(R.layout.month, parent, false);
        view.setDividerColor(dividerColor);
        view.setDayTextColor(dayTextColorResId);
        view.setTitleTextColor(titleTextColor);
        view.setDisplayHeader(displayHeader);
        view.setHeaderTextColor(headerTextColor);
        view.weekTitleContainer = new ArrayList<TextView>();

        if (dayBackgroundResId != 0) {
            view.setDayBackground(dayBackgroundResId);
        }

        if (firstDay == 0) {
            today.setFirstDayOfWeek(Calendar.SUNDAY);
        } else {
            today.setFirstDayOfWeek(Calendar.MONDAY);
        }

        final int originalDayOfWeek = today.get(Calendar.DAY_OF_WEEK);

        int firstDayOfWeek = today.getFirstDayOfWeek();
        final CalendarRowView headerRow = (CalendarRowView) view.grid.getChildAt(0);
        for (int offset = 0; offset < 7; offset++) {
            today.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + offset);
            final TextView textView = (TextView) headerRow.getChildAt(offset);
            textView.setText(weekdayNameFormat.format(today.getTime()));
            textView.setTag(today.get(Calendar.DAY_OF_WEEK));
            view.weekTitleContainer.add(textView);
        }
        today.set(Calendar.DAY_OF_WEEK, originalDayOfWeek);
        view.listener = listener;
        return view;
    }

    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        title = (TextView) findViewById(R.id.title);
        grid = (CalendarGridView) findViewById(R.id.calendar_grid);
    }

    public void init(OnClickListener monthListener, MonthDescriptor month, List<List<MonthCellDescriptor>> cells,
                     boolean displayOnly, Typeface titleTypeface, Typeface dateTypeface, OnClickListener weekListener) {

        for (int i = 0; i < weekTitleContainer.size(); i++) {
            TextView tv = weekTitleContainer.get(i);
            tv.setOnClickListener(weekListener);
        }

        title.setTag(month.getMonth());
        title.setText(month.getLabel());
        title.setTextColor(Color.WHITE);

        title.setOnClickListener(monthListener);

        final int numRows = cells.size();
        grid.setNumRows(numRows);
        for (int i = 0; i < 6; i++) {
            CalendarRowView weekRow = (CalendarRowView) grid.getChildAt(i + 1);
            weekRow.setListener(listener);
            if (i < numRows) {
                weekRow.setVisibility(VISIBLE);
                List<MonthCellDescriptor> week = cells.get(i);
                for (int c = 0; c < week.size(); c++) {
                    MonthCellDescriptor cell = week.get(c);

                    CalendarCellView cellView = (CalendarCellView) weekRow.getChildAt(c);

                    String cellDate = Integer.toString(cell.getValue());

                    //非本月日期不显示
                    if (!cellView.getText().equals(cellDate)) {
                        if (cell.isCurrentMonth()) {
                            cellView.setText(cellDate);
                            cellView.getTextView().setTextColor(Color.WHITE);
                        } else {
                            cellView.setText("");
                        }
                    }


                    cellView.setIvBookingHere();
                    cellView.setIvBlockTimes();
                    cellView.setIvTwoTeeStart();
                    cellView.setIvMemberOnly();
                    cellView.setIvPrimeTime();


                    if (cell.isBookingHere()) {
                        cellView.getIvBookingHere().setVisibility(VISIBLE);
                    } else {
                        cellView.getIvBookingHere().setVisibility(INVISIBLE);
                    }

                    if (cell.isBlockTimes()) {
                        cellView.getIvBlockTimes().setVisibility(VISIBLE);
                    } else {
                        cellView.getIvBlockTimes().setVisibility(INVISIBLE);
                    }

                    if (cell.isTwoTeeStart() || cell.isNineHoles() || cell.isThreeTeeStart()) {
                        cellView.getIvTwoTeeStart().setVisibility(GONE);
                    } else {
                        cellView.getIvTwoTeeStart().setVisibility(GONE);
                    }

                    if (cell.isMemberOnly()) {
                        cellView.getIvMemberOnly().setVisibility(VISIBLE);
                    } else {
                        cellView.getIvMemberOnly().setVisibility(INVISIBLE);
                    }

                    if (cell.isPrimeTime()) {
                        cellView.getIvPrimeTime().setVisibility(VISIBLE);
                    } else {
                        cellView.getIvPrimeTime().setVisibility(INVISIBLE);
                    }

                    if (cell.getTeeTimeColor() == 0) {
                        cellView.setTeeTimeSetting1(false);
                        cellView.setTeeTimeSetting2(false);
                    } else if (cell.getTeeTimeColor() == 1) {
                        cellView.setTeeTimeSetting1(true);
                        cellView.setTeeTimeSetting2(false);
                    } else if (cell.getTeeTimeColor() == 2) {
                        cellView.setTeeTimeSetting1(false);
                        cellView.setTeeTimeSetting2(true);
                    }


//                    cellView.invalidate();

                    cellView.setEnabled(cell.isCurrentMonth());
                    cellView.setClickable(!displayOnly);

                    cellView.setSelectable(cell.isSelectable());
                    cellView.setSelected(cell.isSelected());
                    cellView.setCurrentMonth(false);
                    cellView.setToday(false);
                    cellView.setRangeState(cell.getRangeState());
                    cellView.setHighlighted(cell.isHighlighted());
                    cellView.setTag(cell);
                }
            } else {
                weekRow.setVisibility(GONE);
            }

        }

        if (titleTypeface != null) {
            title.setTypeface(titleTypeface);
        }
        if (dateTypeface != null) {
            grid.setTypeface(dateTypeface);
        }

        invalidate();

    }

    public void setDividerColor(int color) {
        grid.setDividerColor(color);
    }

    public void setDayBackground(int resId) {
        grid.setDayBackground(resId);
    }

    public void setDayTextColor(int resId) {
        grid.setDayTextColor(resId);
    }

    public void setTitleTextColor(int color) {
        title.setTextColor(color);
    }

    public void setDisplayHeader(boolean displayHeader) {
        grid.setDisplayHeader(displayHeader);
    }

    public void setHeaderTextColor(int color) {
        grid.setHeaderTextColor(color);
    }

    public interface Listener {
        void handleClick(MonthCellDescriptor cell);
    }
}
