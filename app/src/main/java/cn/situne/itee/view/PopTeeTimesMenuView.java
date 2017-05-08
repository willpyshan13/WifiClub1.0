/**
 * Project Name: itee
 * File Name:  PopTeeTimesMenuView.java
 * Package Name: cn.situne.itee.view
 * Date:   2015-01-27
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */
package cn.situne.itee.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import cn.situne.itee.R;
import cn.situne.itee.common.utils.AppUtils;

/**
 * ClassName:PopTeeTimesMenuView <br/>
 * Function: select TeeTimes menu popup window. <br/>
 * Date: 2015-01-27 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class PopTeeTimesMenuView extends PopupWindow {

    private LinearLayout llCourseData;
    private LinearLayout llTeeTimes;
    private LinearLayout llDataSetting;
    private View mMenuView;

    private IteeTextView tvCourseData;
    private IteeTextView tvTeeTimes;

    private IteeTextView tvDateSetting;

    public PopTeeTimesMenuView(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        initView(context);

        //设置按钮监听
        llCourseData.setOnClickListener(itemsOnClick);
        llTeeTimes.setOnClickListener(itemsOnClick);

        llDataSetting.setOnClickListener(itemsOnClick);
        tvCourseData.setText(R.string.title_course_data);
        tvTeeTimes.setText(R.string.title_tee_times);

        tvDateSetting.setText(R.string.administration_date_setting);

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }

    private void initView(Activity context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_tee_time_menu, null);
        llCourseData = (LinearLayout) mMenuView.findViewById(R.id.ll_course_data);
        llTeeTimes = (LinearLayout) mMenuView.findViewById(R.id.ll_tee_times);

        llDataSetting= (LinearLayout) mMenuView.findViewById(R.id.ll_data_setting);

        tvCourseData = (IteeTextView) mMenuView.findViewById(R.id.tv_course_data);
        tvTeeTimes = (IteeTextView) mMenuView.findViewById(R.id.tv_tee_times);

        tvDateSetting= (IteeTextView) mMenuView.findViewById(R.id.tv_data_setting);

        boolean isAuthCourseData = AppUtils.getAuth(AppUtils.AuthControl.AuthControlEditCourseData, context);
        boolean isAuthTetTimes = AppUtils.getAuth(AppUtils.AuthControl.AuthControlEditTeeTimes, context);
        if (!isAuthCourseData) {
            llCourseData.setVisibility(View.GONE);
        }
        if (!isAuthTetTimes) {
            llTeeTimes.setVisibility(View.GONE);
        }
    }

}