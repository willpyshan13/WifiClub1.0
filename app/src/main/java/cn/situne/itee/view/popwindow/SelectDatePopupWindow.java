/**
 * Project Name: itee
 * File Name:  SelectCaddiePopupWindow.java
 * Package Name: cn.situne.itee.common.widget.wheel
 * Date:   2015-01-15
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.view.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.common.widget.wheel.SelectTimeNumericWheelAdapter;
import cn.situne.itee.common.widget.wheel.SelectTimeOnWheelChangedListener;
import cn.situne.itee.common.widget.wheel.SelectTimeWheelView;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:SelectDatePopupWindow <br/>
 * Function: select date popup window. <br/>
 * Date: 2015-01-20 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class SelectDatePopupWindow extends BasePopWindow {

    public SelectTimeWheelView wheelViewYear, wheelViewMonth, wheelViewDay;
    public Button btnOk, btnCancel;
    private LinearLayout llTitleContainer, llWheelContainer;
    private IteeTextView tvYear, tvMonth, tvDay;
    private View menuView;
    private String mNowDate;
    private OnDateSelectClickListener clickListener;

    private boolean isCheck;

    private String nowYear;

    private String nowMonth;
    private String nowDay;
    private SelectTimeNumericWheelAdapter yearAdapter, monthAdapter, dayAdapter;

   // public SelectDatePopupWindow(Activity context, String nowDate, OnDateSelectClickListener dateSelectClickListener,boolean isCheck) {

        public SelectDatePopupWindow(Activity context, String nowDate, OnDateSelectClickListener dateSelectClickListener) {
        super(context);
        this.clickListener = dateSelectClickListener;
        this.mNowDate = nowDate;
        this.isCheck = isCheck;

        initControls(context);

        if (isCheck){
            String[] nowDateStr =  nowDate.split(Constants.STR_SLASH);
            nowYear = nowDateStr[0];
            nowMonth = nowDateStr[1];
            nowDay = nowDateStr[2];
        }

        changeDateFormat(context);

        menuView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.popup_enter));
        //设置SelectPicPopupWindow的View
        this.setContentView(menuView);
        formatViews();
        setHideListener(menuView);
        initListener();
    }

    private void initControls(Context mContext) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.popw_date_three_wheel, null);

        llTitleContainer = (LinearLayout) menuView.findViewById(R.id.ll_title_container);
        llWheelContainer = (LinearLayout) menuView.findViewById(R.id.ll_wheel_container);

        tvYear = (IteeTextView) menuView.findViewById(R.id.tv_year);
        tvMonth = (IteeTextView) menuView.findViewById(R.id.tv_month);
        tvDay = (IteeTextView) menuView.findViewById(R.id.tv_day);

        tvYear.setTextSize(R.string.common_year);
        tvMonth.setTextSize(R.string.common_month);
        tvDay.setTextSize(R.string.common_day);

        tvYear.setGravity(Gravity.CENTER);
        tvMonth.setGravity(Gravity.CENTER);
        tvDay.setGravity(Gravity.CENTER);

        tvYear.setTextSize(Constants.FONT_SIZE_MORE_LARGER);
        tvMonth.setTextSize(Constants.FONT_SIZE_MORE_LARGER);
        tvDay.setTextSize(Constants.FONT_SIZE_MORE_LARGER);

        wheelViewYear = (SelectTimeWheelView) menuView.findViewById(R.id.wheel_year);
        yearAdapter = new SelectTimeNumericWheelAdapter(1910, 2080);
        wheelViewYear.setAdapter(yearAdapter);
        wheelViewYear.setCyclic(false);

        wheelViewMonth = (SelectTimeWheelView) menuView.findViewById(R.id.wheel_month);
        monthAdapter = new SelectTimeNumericWheelAdapter(1, 12, "%02d");
        wheelViewMonth.setAdapter(monthAdapter);
        wheelViewMonth.setCyclic(true);

        // set current time

        Calendar calendar = Calendar.getInstance();
        if (Utils.isStringNotNullOrEmpty(this.mNowDate)) {
            Date now = DateUtils.getDateFromCurrentShowYearMonthDay(mNowDate, mContext);
            calendar.setTime(now);
        }

        wheelViewDay = (SelectTimeWheelView) menuView.findViewById(R.id.wheel_day);
        int maxDay = calendar.getActualMaximum(Calendar.DATE);
        dayAdapter = new SelectTimeNumericWheelAdapter(1, maxDay, "%02d");
        wheelViewDay.setAdapter(dayAdapter);
        wheelViewDay.setCyclic(true);

        wheelViewYear.setCurrentItem(calendar.get(Calendar.YEAR) - 1910);
        wheelViewMonth.setCurrentItem(calendar.get(Calendar.MONTH));
        wheelViewDay.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);

        btnOk = (Button) menuView.findViewById(R.id.btn_ok);
        btnCancel = (Button) menuView.findViewById(R.id.btn_cancel);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE); // 画框
        drawable.setStroke(2, mContext.getResources().getColor(R.color.common_blue)); // 边框粗细及颜色
        drawable.setColor(Color.WHITE); // 边框内部颜色

        btnOk.setBackground(drawable); // 设置背景（效果就是有边框及底色）
        btnCancel.setBackground(drawable); // 设置背景（效果就是有边框及底色）
        btnOk.setHeight(30);
        btnCancel.setHeight(30);
    }

    private void changeDateFormat(Context mContext) {
        llTitleContainer.removeView(tvYear);
        llTitleContainer.removeView(tvMonth);
        llTitleContainer.removeView(tvDay);

        llWheelContainer.removeView(wheelViewYear);
        llWheelContainer.removeView(wheelViewMonth);
        llWheelContainer.removeView(wheelViewDay);

        String dateFormat = DateUtils.getShowYearMonthDayFormat(mContext);
        if (DateUtils.DATE_FORMAT_SHOW_MM_DD_YYYY.equals(dateFormat)) {
            llTitleContainer.addView(tvMonth);
            llTitleContainer.addView(tvDay);
            llTitleContainer.addView(tvYear);

            llWheelContainer.addView(wheelViewMonth);
            llWheelContainer.addView(AppUtils.getHorizonSeparatorLine(mContext));
            llWheelContainer.addView(wheelViewDay);
            llWheelContainer.addView(AppUtils.getHorizonSeparatorLine(mContext));
            llWheelContainer.addView(wheelViewYear);
        } else if (DateUtils.DATE_FORMAT_SHOW_DD_MM_YYYY.equals(dateFormat)) {
            llTitleContainer.addView(tvDay);
            llTitleContainer.addView(tvMonth);
            llTitleContainer.addView(tvYear);

            llWheelContainer.addView(wheelViewDay);
            llWheelContainer.addView(AppUtils.getHorizonSeparatorLine(mContext));
            llWheelContainer.addView(wheelViewMonth);
            llWheelContainer.addView(AppUtils.getHorizonSeparatorLine(mContext));
            llWheelContainer.addView(wheelViewYear);
        } else {
            llTitleContainer.addView(tvYear);
            llTitleContainer.addView(tvMonth);
            llTitleContainer.addView(tvDay);

            llWheelContainer.addView(wheelViewYear);
            llWheelContainer.addView(AppUtils.getHorizonSeparatorLine(mContext));
            llWheelContainer.addView(wheelViewMonth);
            llWheelContainer.addView(AppUtils.getHorizonSeparatorLine(mContext));
            llWheelContainer.addView(wheelViewDay);
        }
    }

    private void initListener() {

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendarReturn = Calendar.getInstance();
                SelectTimeNumericWheelAdapter adapter = (SelectTimeNumericWheelAdapter) wheelViewYear.getAdapter();
                String year = adapter.getItem(wheelViewYear.getCurrentItem());
                SelectTimeNumericWheelAdapter adapterMonth = (SelectTimeNumericWheelAdapter) wheelViewMonth.getAdapter();
                String month = adapterMonth.getItem(wheelViewMonth.getCurrentItem());

                SelectTimeNumericWheelAdapter adapterDay = (SelectTimeNumericWheelAdapter) wheelViewDay.getAdapter();
                String day = adapterDay.getItem(wheelViewDay.getCurrentItem());
                calendarReturn.set(Integer.valueOf(year), Integer.valueOf(month) - 1,
                        Integer.valueOf(day));
                SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_API_YYYY_MM_DD, Locale.getDefault());
                String date = sdf.format(calendarReturn.getTime());

                clickListener.OnGoodItemClick(Constants.DATE_RETURN, date);
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        wheelViewYear.addChangingListener(new SelectTimeOnWheelChangedListener() {
            @Override
            public void onChanged(SelectTimeWheelView wheel, int oldValue, int newValue) {
                setDayItem();
            }
        });

        wheelViewMonth.addChangingListener(new SelectTimeOnWheelChangedListener() {
            @Override
            public void onChanged(SelectTimeWheelView wheel, int oldValue, int newValue) {
                setDayItem();
            }
        });

    }

    private void setDayItem() {
        Calendar calendarSet = Calendar.getInstance();
        String year = yearAdapter.getItem(wheelViewYear.getCurrentItem());
        String month = monthAdapter.getItem(wheelViewMonth.getCurrentItem());
        String day = dayAdapter.getItem(wheelViewDay.getCurrentItem());

        calendarSet.set(Integer.valueOf(year), Integer.valueOf(month) - 1, 1);
        int maxDay = calendarSet.getActualMaximum(Calendar.DAY_OF_MONTH);
        wheelViewDay.setAdapter(new SelectTimeNumericWheelAdapter(1, maxDay, "%02d"));

        Utils.log("day : " + day);
        Utils.log("maxDay : " + maxDay);

        if (Integer.valueOf(day) < maxDay) {
            calendarSet.set(Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day));
        } else {
            calendarSet.set(Integer.valueOf(year), Integer.valueOf(month) - 1, maxDay);
            wheelViewDay.setCurrentItem(maxDay - 1);
        }
    }

    public interface OnDateSelectClickListener {
        void OnGoodItemClick(String flag, String content);
    }
}
