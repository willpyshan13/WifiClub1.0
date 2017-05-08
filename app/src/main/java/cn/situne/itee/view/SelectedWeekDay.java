/**
 * Project Name: itee
 * File Name:	 SelectedWeekDay.java
 * Package Name: cn.situne.itee.view
 * Date:		 2015-03-24
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.LinkedHashMap;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:SelectedWeekDay <br/>
 * Function: weekday select control. <br/>
 * Date: 2015-03-24 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class SelectedWeekDay extends LinearLayout {

    private Context mContext;

    private boolean isCanUse;

    private LinkedHashMap<String, WeekDayLayout> textViewHashMap;

    private String[] weekDaysName;
    private String[] weekDaysValue;

    OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (isCanUse) {
                WeekDayLayout weekDayLayout = (WeekDayLayout) v;
                if (weekDayLayout.isSelected()) {
                    setWeekDayDeselected(weekDayLayout);
                } else {
                    setWeekDaySelected(weekDayLayout);
                }
            }
        }
    };

    /**
     * constructor
     *
     * @param mContext context
     */
    public SelectedWeekDay(Context mContext) {
        super(mContext);
        this.mContext = mContext;
        weekDaysName = new String[]{mContext.getString(R.string.calendar_sun)
                , mContext.getString(R.string.calendar_mon)
                , mContext.getString(R.string.calendar_tue)
                , mContext.getString(R.string.calendar_wed)
                , mContext.getString(R.string.calendar_thu)
                , mContext.getString(R.string.calendar_fri)
                , mContext.getString(R.string.calendar_sat)};
        weekDaysValue = new String[]{"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        textViewHashMap = new LinkedHashMap<>();
        this.setBackgroundColor(getResources().getColor(R.color.common_deep_blue));
        this.setOrientation(HORIZONTAL);

        initView();
    }

    private void initView() {
        int firstDay;
        String firstOfDayString = AppUtils.getCurrentFirstDayOfWeek(mContext);
        if (getResources().getString(R.string.calendar_sun).equals(firstOfDayString)) {
            firstDay = 0;
        } else {
            firstDay = 1;
        }

        for (int i = firstDay; i < weekDaysName.length; i++) {
            String name = weekDaysName[i];
            WeekDayLayout weekDayLayout = generateTextView(name, weekDaysValue[i]);

            this.addView(weekDayLayout);
        }
        if (firstDay == 1) {
            this.addView(generateTextView(weekDaysName[0], weekDaysValue[0]));
        }
    }

    private WeekDayLayout generateTextView(String name, String key) {
        WeekDayLayout weekDayLayout = new WeekDayLayout(mContext);
        weekDayLayout.getTvWeekday().setText(name);
        weekDayLayout.getTvWeekday().setTextColor(getResources().getColor(R.color.common_white));
        weekDayLayout.getTvWeekday().setGravity(Gravity.CENTER);
        weekDayLayout.setOnClickListener(listener);
        textViewHashMap.put(key, weekDayLayout);
        weekDayLayout.setWeekDayValue(key);
        LinearLayout.LayoutParams tvLayoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        tvLayoutParams.weight = 1;
        weekDayLayout.setLayoutParams(tvLayoutParams);
        return weekDayLayout;
    }

    public void setSelectedWeekDays(String days) {
        if (Utils.isStringNotNullOrEmpty(days) && days.contains(Constants.STR_COMMA)) {
            String[] weekDayArray = days.split(Constants.STR_COMMA);
            for (String weekDay : weekDayArray) {
                if (Utils.isStringNotNullOrEmpty(weekDay)) {
                    WeekDayLayout tv = textViewHashMap.get(weekDay);
                    if (tv != null) {
                        setWeekDaySelected(tv);
                    }
                }
            }
        }
    }

    public String getSelectedWeekDays() {
        StringBuilder sb = new StringBuilder();
        for (String key : textViewHashMap.keySet()) {
            WeekDayLayout weekDayLayout = textViewHashMap.get(key);
            if (weekDayLayout.isSelected()) {
                if (sb.length() > 0) {
                    sb.append(Constants.STR_COMMA);
                }
                sb.append(weekDayLayout.getWeekDayValue());
            }
        }
        return sb.toString();
    }

    private void setWeekDaySelected(WeekDayLayout weekDayLayout) {
        weekDayLayout.getTvWeekday().setTextColor(getResources().getColor(R.color.common_white));
        weekDayLayout.setMark();
        weekDayLayout.setIsSelected(true);
    }

    private void setWeekDayDeselected(WeekDayLayout weekDayLayout) {
        weekDayLayout.getTvWeekday().setTextColor(getResources().getColor(R.color.common_white));
        weekDayLayout.setIsSelected(false);
        weekDayLayout.removeMark();
    }

    public void setCanUse(boolean isCanUse) {
        this.isCanUse = isCanUse;
    }

    public void selectAllDays() {
        for (String weekDay : weekDaysValue) {
            if (Utils.isStringNotNullOrEmpty(weekDay)) {
                WeekDayLayout weekDayLayout = textViewHashMap.get(weekDay);
                if (weekDayLayout != null) {
                    setWeekDaySelected(weekDayLayout);
                }
            }
        }
    }

    class WeekDayLayout extends LinearLayout {

        private String weekDayValue;
        private boolean isSelected;

        private IteeTextView tvWeekday;
        private ImageView ivMark;

        public WeekDayLayout(Context context) {
            super(context);
            setOrientation(VERTICAL);
            initView(context);
        }

        private void initView(Context context) {
            tvWeekday = new IteeTextView(context);
            ivMark = new ImageView(context);

            LinearLayout.LayoutParams tvWeekdayLayoutParams
                    = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            tvWeekdayLayoutParams.weight = 1;
            tvWeekday.setLayoutParams(tvWeekdayLayoutParams);
            addView(tvWeekday);

            LinearLayout.LayoutParams ivMarkLayoutParams
                    = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            ivMarkLayoutParams.weight = 1;
            ivMark.setLayoutParams(ivMarkLayoutParams);
            ivMark.setScaleType(ImageView.ScaleType.CENTER);
            addView(ivMark);
        }

        public void setMark() {
            ivMark.setImageResource(R.drawable.icon_white_check_mark);
        }

        public void removeMark() {
            ivMark.setImageDrawable(null);
        }

        public String getWeekDayValue() {
            return weekDayValue;
        }

        public void setWeekDayValue(String weekDayValue) {
            this.weekDayValue = weekDayValue;
            tvWeekday.setText(weekDayValue);
        }

        public IteeTextView getTvWeekday() {
            return tvWeekday;
        }

        @Override
        public boolean isSelected() {
            return isSelected;
        }

        public void setIsSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }
    }
}