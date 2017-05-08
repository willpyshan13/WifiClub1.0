/**
 * Project Name: itee
 * File Name:	 SelectDateActivity.java
 * Package Name: cn.situne.itee.activity
 * Date:		 2015-07-22
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.JsonTeeTimeCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.LayoutUtils;

/**
 * ClassName:SelectDateActivity <br/>
 * Function: SelectDateActivity. <br/>
 * Date: 2015-07-22 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class SelectDateActivity extends BaseActivity {

    private RelativeLayout rlContainer;

    private CalendarPickerView calendarPickerView;

    private String selectedDates;

    @Override
    protected void initControls() {

        Intent intent = getIntent();
        if (intent != null) {
            selectedDates = intent.getStringExtra(TransKey.SELECTED_DATE);
        }

        rlContainer = (RelativeLayout) findViewById(R.id.rl_content_container);
        calendarPickerView = new CalendarPickerView(this, null);

        Calendar today = Calendar.getInstance();
        final Calendar startTime = Calendar.getInstance();
        startTime.set(today.get(Calendar.YEAR), 0, 1);
        final Calendar endTime = Calendar.getInstance();
        endTime.set(today.get(Calendar.YEAR) + 1, 0, 1);

        if (Constants.FIRST_DAY_SUN.equals(AppUtils.getCurrentFirstDayOfWeek(this))) {
            //将星期日作为一个星期中的第一天
            calendarPickerView.init(startTime.getTime(), endTime.getTime(), Locale.getDefault(), new ArrayList<JsonTeeTimeCalendar.DataList.DateStatus>(), 0)
                    .inMode(CalendarPickerView.SelectionMode.MULTIPLE);
        } else {
            //将星期一作为一个星期中的第一天
            calendarPickerView.init(startTime.getTime(), endTime.getTime(), Locale.getDefault(), new ArrayList<JsonTeeTimeCalendar.DataList.DateStatus>(), 1)
                    .inMode(CalendarPickerView.SelectionMode.MULTIPLE);
        }

//        if (StringUtils.isNotEmpty(selectedDates) && selectedDates.contains(Constants.STR_COMMA)) {
//            String[] dates = selectedDates.split(Constants.STR_COMMA);
//            List<Date> dateList = new ArrayList<>();
//            for (String dateStr : dates) {
//                Date d = DateUtils.getDateFromAPIYearMonthDay(dateStr);
//                dateList.add(d);
//            }
//            calendarPickerView.setSelectedDates(dateList);
//        }

        calendarPickerView.scrollToDate(new Date());
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {

    }

    @Override
    protected void setLayoutOfControls() {
        rlContainer.addView(calendarPickerView);
        LayoutUtils.setWidthAndHeight(calendarPickerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void setPropertyOfControls() {
        if (getTvLeftTitle() != null) {
            getTvLeftTitle().setText(R.string.title_select_date);

        }
        if (getTvRight() != null) {
            getTvRight().setText(R.string.common_ok);
            getTvRight().setOnClickListener(new AppUtils.NoDoubleClickListener(this) {
                @Override
                public void noDoubleClick(View v) {
                    List<Date> dates = calendarPickerView.getSelectedDates();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Date d : dates) {
                        if (stringBuilder.length() > 0) {
                            stringBuilder.append(Constants.STR_COMMA);
                        }
                        String dateStr = DateUtils.getAPIYearMonthDay(d);
                        stringBuilder.append(dateStr);
                    }
                    selectedDates = stringBuilder.toString();
                    Intent intent = new Intent();
                    intent.putExtra(TransKey.SELECTED_DATE, selectedDates);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            });
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_date;
    }

    @Override
    protected View.OnClickListener getBackListener() {
        return new AppUtils.NoDoubleClickListener(this) {
            @Override
            public void noDoubleClick(View v) {
                finish();
            }
        };
    }
}