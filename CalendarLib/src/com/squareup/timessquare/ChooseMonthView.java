package com.squareup.timessquare;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChooseMonthView extends LinearLayout {
    TextView title;
    TextView quickSelect;
    ChooseCalendarGridView grid;
    private Listener listener;

    private ArrayList<TextView> weekTitleContainer;

    public static ChooseMonthView create(ViewGroup parent, LayoutInflater inflater,
                                         DateFormat weekdayNameFormat, Listener listener, Calendar today, int dividerColor,
                                         int dayBackgroundResId, int dayTextColorResId, int titleTextColor, boolean displayHeader,
                                         int headerTextColor, int firstDay) {

        final ChooseMonthView view = (ChooseMonthView) inflater.inflate(R.layout.month_choose, parent, false);
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
        final ChooseCalendarRowView headerRow = (ChooseCalendarRowView) view.grid.getChildAt(0);
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

    public ChooseMonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        title = (TextView) findViewById(R.id.title);
        quickSelect = (TextView) findViewById(R.id.quick_select);
        grid = (ChooseCalendarGridView) findViewById(R.id.calendar_grid);
    }

    public void init(OnClickListener monthListener, final ChooseCalendarPickerView.QuickSelectedClickListener quickSelectedClickListener,
                     final CustomMonthDescriptor month, List<List<CustomMonthCellDescriptor>> cells,
                     boolean displayOnly, Typeface titleTypeface, Typeface dateTypeface, OnClickListener weekListener) {

        for (int i = 0; i < weekTitleContainer.size(); i++) {
            TextView tv = weekTitleContainer.get(i);
            tv.setOnClickListener(weekListener);
        }

        title.setTag(month.getMonth());
        title.setText(month.getLabel());
        title.setTextColor(Color.WHITE);
        title.setOnClickListener(monthListener);

        quickSelect.setTag(month.getMonth());
        quickSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                quickSelectedClickListener.onQuickSelect(month.getDate());
            }
        });

        //
        if (month.getMonthDataSourch() != null && month.getMonthDataSourch().size() == 0) {
            quickSelect.setVisibility(GONE);
        }

        final int numRows = cells.size();
        grid.setNumRows(numRows);
        for (int i = 0; i < 6; i++) {
            ChooseCalendarRowView weekRow = (ChooseCalendarRowView) grid.getChildAt(i + 1);
            weekRow.setListener(listener);
            if (i < numRows) {
                weekRow.setVisibility(VISIBLE);
                List<CustomMonthCellDescriptor> week = cells.get(i);
                for (int c = 0; c < week.size(); c++) {
                    CustomMonthCellDescriptor cell = week.get(c);

                    CustomCalendarCellView cellView = (CustomCalendarCellView) weekRow.getChildAt(c);

                    String cellDate = Integer.toString(cell.getValue());

                    //非本月日期不显示
                    if (!cellView.getText().equals(cellDate)) {
                        if (cell.isCurrentMonth()) {
                            cellView.setText(cellDate);
                            cellView.getTextView().setTextColor(Color.WHITE);
                        } else {
                            cellView.setText("");
                            cellView.getTvTopLeft().setText("");
                            cellView.getTvTopRight().setText("");
                            cellView.getTvBottomLeft().setText("");
                            cellView.getTvBottomRight().setText("");
                        }
                    }

                    //set every cell in the calendar.
                    if (cell.getDaySetting() != null) {
                        CustomJsonAdvancedSetting.DaySetting daySetting = cell.getDaySetting();
                        List<CustomJsonAdvancedSetting.CdTypeArrItem> list1 = daySetting.getCdTypeArr();
                        List<CustomJsonAdvancedSetting.CdTypeArrItem> list2 = daySetting.getCdTypeFlagArr();
                        List<CustomJsonAdvancedSetting.CdTypeArrItem> allList = new ArrayList<>();

                        if (list2 != null) {
                            allList.addAll(list2);
                        }
                        if (list1 != null) {
                            allList.addAll(list1);
                        }


                        for (int j = 0; j < allList.size(); j++) {
                            switch (j) {
                                case 0:
                                    cellView.getTvTopLeft().setText(allList.get(0).getFirstName());
                                    cellView.getTvTopLeft().setVisibility(VISIBLE);
                                    break;
                                case 1:
                                    cellView.getTvTopRight().setText("/" + allList.get(1).getFirstName());
                                    cellView.getTvTopRight().setVisibility(VISIBLE);
                                    break;
                                case 2:
                                    cellView.getTvBottomLeft().setText(allList.get(2).getFirstName());
                                    cellView.getTvBottomLeft().setVisibility(VISIBLE);
                                    break;
                                case 3:
                                    cellView.getTvBottomRight().setText("/" + allList.get(3).getFirstName());
                                    cellView.getTvBottomRight().setVisibility(VISIBLE);
                                    break;

                            }

                        }
                    }
//
//                    cellView.setIvBookingHere();
//                    cellView.setIvBlockTimes();
//                    cellView.setIvTwoTeeStart();
//                    cellView.setIvMemberOnly();
//                    cellView.setIvPrimeTime();
//
//

//                    cellView.getTvTopLeft().setVisibility(VISIBLE);
//                    cellView.getTvTopRight().setVisibility(VISIBLE);
//                    cellView.getTvBottomLeft().setVisibility(VISIBLE);
//                    cellView.getTvBottomRight().setVisibility(VISIBLE);
//                    if (cell.isBookingHere()) {
//                        cellView.getIvBookingHere().setVisibility(VISIBLE);
//                    } else {
//                        cellView.getIvBookingHere().setVisibility(INVISIBLE);
//                    }
//
//                    if (cell.isBlockTimes()) {
//                        cellView.getIvBlockTimes().setVisibility(VISIBLE);
//                    } else {
//                        cellView.getIvBlockTimes().setVisibility(INVISIBLE);
//                    }
//
//                    if (cell.isTwoTeeStart() || cell.isNineHoles() || cell.isThreeTeeStart()) {
//                        cellView.getIvTwoTeeStart().setVisibility(VISIBLE);
//                    } else {
//                        cellView.getIvTwoTeeStart().setVisibility(INVISIBLE);
//                    }
//
//                    if (cell.isMemberOnly()) {
//                        cellView.getIvMemberOnly().setVisibility(VISIBLE);
//                    } else {
//                        cellView.getIvMemberOnly().setVisibility(INVISIBLE);
//                    }
//
//                    if (cell.isPrimeTime()) {
//                        cellView.getIvPrimeTime().setVisibility(VISIBLE);
//                    } else {
//                        cellView.getIvPrimeTime().setVisibility(INVISIBLE);
//                    }

//                    if (cell.getTeeTimeColor() == 0) {
//                        cellView.setTeeTimeSetting1(false);
//                        cellView.setTeeTimeSetting2(false);
//                    } else if (cell.getTeeTimeColor() == 1) {
//                        cellView.setTeeTimeSetting1(true);
//                        cellView.setTeeTimeSetting2(false);
//                    } else if (cell.getTeeTimeColor() == 2) {
//                        cellView.setTeeTimeSetting1(false);
//                        cellView.setTeeTimeSetting2(true);
//                    }


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
        void handleClick(CustomMonthCellDescriptor cell);
    }
}
