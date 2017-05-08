/**
 * Project Name: itee
 * File Name:  TeeTimeCalendarPopupWindowView.java
 * Package Name: cn.situne.itee.view
 * Date:   2015-01-23
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */
package cn.situne.itee.view;

import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonCalendar;

/**
 * ClassName:TeeTimeCalendarPopupWindowView <br/>
 * Function: show tee time calendar popup window. <br/>
 * Date: 2015-01-23 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class TeeTimeCalendarPopupWindowView extends PopupWindow {

    private String date = null; // 设置默认选中的日期
    private String[] months;
    private BaseFragment mFragment;
    private View view;
    private KCalendarView calendar;
    private List<String> dateList = new ArrayList<>();

    private String today;

    private boolean isBlock;

    public boolean isBlock() {
        return isBlock;
    }

    public void setIsBlock(boolean isBlock) {
        this.isBlock = isBlock;
    }

    private DateClickListener dateClickListener;

    public TeeTimeCalendarPopupWindowView(BaseFragment mFragment, View parent, String selectedDate,boolean isBlock) {
        this.mFragment = mFragment;
        this.date = selectedDate;
        this.isBlock = isBlock;

        view = View.inflate(mFragment.getActivity(), R.layout.popupwindow_calendar,
                null);

        months = new String[]{mFragment.getString(R.string.calendar_jan)
                , mFragment.getString(R.string.calendar_feb)
                , mFragment.getString(R.string.calendar_mar)
                , mFragment.getString(R.string.calendar_apr)
                , mFragment.getString(R.string.calendar_may)
                , mFragment.getString(R.string.calendar_jun)
                , mFragment.getString(R.string.calendar_jul)
                , mFragment.getString(R.string.calendar_aug)
                , mFragment.getString(R.string.calendar_sep)
                , mFragment.getString(R.string.calendar_oct)
                , mFragment.getString(R.string.calendar_nov)
                , mFragment.getString(R.string.calendar_dec)};


        IteeTextView tvToday = (IteeTextView) view.findViewById(R.id.tv_today);
        tvToday.setGravity(Gravity.CENTER);


        view.startAnimation(AnimationUtils.loadAnimation(mFragment.getActivity(),
                R.anim.fade_in));
        LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(mFragment.getActivity(), R.anim.push_bottom_in_1));

        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);

        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        setBackgroundDrawable(dw);


        setOutsideTouchable(true);
        setContentView(view);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        update();

        final TextView popupWindowCalendarMonth = (TextView) view
                .findViewById(R.id.popupwindow_calendar_month);
        calendar = (KCalendarView) view
                .findViewById(R.id.popupwindow_calendar);
        calendar.setIsBlock(isBlock());

        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_YYYYMMDD_OBLIQUE, Locale.getDefault());
        try {
            calendar.setThisDay(sdf.parse(date));
        } catch (ParseException e) {
            Utils.log(e.getMessage());
        }

        tvToday.setTextColor(mFragment.getColor(R.color.common_white));
        tvToday.setTextSize(Constants.FONT_SIZE_MORE_LARGER);
        tvToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.showCalendar();
                getDate(calendar.getCalendarYear(), calendar.getCalendarMonth() - 1);
                popupWindowCalendarMonth.setText(months[calendar.getCalendarMonth() - 1] + Constants.STR_SPACE + calendar
                        .getCalendarYear());
            }
        });

        popupWindowCalendarMonth.setText(months[calendar.getCalendarMonth() - 1] + Constants.STR_SPACE + calendar.getCalendarYear());

        if (null != date) {

            int years = Integer.parseInt(date.substring(0,
                    date.indexOf(Constants.STR_SLASH)));
            int month = Integer.parseInt(date.substring(
                    date.indexOf(Constants.STR_SLASH) + 1, date.lastIndexOf(Constants.STR_SLASH)));
            popupWindowCalendarMonth.setText(months[month - 1] + Constants.STR_SPACE + years);

            calendar.showCalendar(years, month);
            calendar.setCalendarDayBgColor(date,
                    R.drawable.calendar_date_focused);
        }
        getDate(calendar.getCalendarYear(), calendar.getCalendarMonth() - 1);

        //监听所选中的日期
        calendar.setOnCalendarClickListener(new KCalendarView.OnCalendarClickListener() {

            public void onCalendarClick(int row, int col, String dateFormat) {
                int month = Integer.parseInt(dateFormat.substring(
                        dateFormat.indexOf(Constants.STR_SLASH) + 1,
                        dateFormat.lastIndexOf(Constants.STR_SLASH)));
                // current year
                if (month - calendar.getCalendarMonth() != 1
                        && month - calendar.getCalendarMonth() != -11) {
                    calendar.removeAllBgColor();


                    if (isBlock()){
                        Calendar cal = Calendar.getInstance();
                        Date nowDate = new Date(cal.getTimeInMillis());
                        if ( Utils.isSecondDateLaterThanFirst(AppUtils.getYMDWithSlash(nowDate),dateFormat,Constants.DATE_FORMAT_YYYYMMDD_OBLIQUE)){
                            calendar.setCalendarDayBgColor(dateFormat, R.drawable.calendar_date_focused);
                            //return to 03-1
                            date = dateFormat; //selected date
                            if (dateClickListener != null) {
                                dateClickListener.onDateClick(date);
                            }


                            dismiss();
                        }


                    }else if (dateList == null || dateList.contains(dateFormat)) {

                            calendar.setCalendarDayBgColor(dateFormat, R.drawable.calendar_date_focused);
                            //return to 03-1
                            date = dateFormat; //selected date
                            if (dateClickListener != null) {
                                dateClickListener.onDateClick(date);
                            }

                            dismiss();

                    }
                }
            }
        });

        //监听当前月份
        calendar.setOnCalendarDateChangedListener(new KCalendarView.OnCalendarDateChangedListener() {
            public void onCalendarDateChanged(int year, int month) {
                Log.d("Calendar", String.valueOf(month));
                popupWindowCalendarMonth
                        .setText(months[month - 1] + " " + year);
                getDate(year, month - 1);
            }
        });

        //上月监听按钮
        RelativeLayout popupWindowCalendarLastMonth = (RelativeLayout) view.findViewById(R.id.popupwindow_calendar_last_month);
        popupWindowCalendarLastMonth.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                calendar.lastMonth();
            }

        });

        //下月监听按钮
        RelativeLayout popupWindowCalendarNextMonth = (RelativeLayout) view.findViewById(R.id.popupwindow_calendar_next_month);
        popupWindowCalendarNextMonth.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                calendar.nextMonth();
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.ll_popup).getTop();
                int bottom = view.findViewById(R.id.ll_popup).getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height || y > bottom) {

                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    //请求日历信息
    private void getDate(final int year, final int month) {


        if (!isBlock){
            String currentMonth = String.format("%d-%02d", year, month + 1);

            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(mFragment.getActivity()));
            params.put(ApiKey.ADMINISTRATION_DATE, currentMonth);
            params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(mFragment.getActivity()));


            HttpManager<JsonCalendar> hh = new HttpManager<JsonCalendar>(mFragment) {

                @Override
                public void onJsonSuccess(JsonCalendar jo) {
                    Utils.debug(jo.toString());
                    ArrayList<String> unavailableDateList = jo.getMyList();
                    if (unavailableDateList != null) {
                        // remove the date in the dateList from this month
                        Calendar c = Calendar.getInstance();
                        List<String> availableDate = new ArrayList<>();
                        for (int i = 1; i <= 31; i++) {
                            c.set(Calendar.YEAR, year);
                            c.set(Calendar.MONTH, month);
                            c.set(Calendar.DAY_OF_MONTH, i);
//                        if (c.get(Calendar.MONTH) == month) {
                            String dateStr = AppUtils.getYMDWithSlash(c.getTime());
                            if (!unavailableDateList.contains(dateStr)) {
                                availableDate.add(dateStr);
                            }
//                        }
                        }
                        dateList = availableDate;
                        calendar.changeCalendarDate(availableDate);
                    }
                }

                @Override
                public void onJsonError(VolleyError error) {

                }
            };
            hh.start(mFragment.getActivity(), ApiManager.HttpApi.Calendar, params);
        }else{
            calendar.changeCalendarDate();

        }



    }

    public void setDateClickListener(DateClickListener dateClickListener) {
        this.dateClickListener = dateClickListener;
    }

    public interface DateClickListener {
        void onDateClick(String date);
    }
}