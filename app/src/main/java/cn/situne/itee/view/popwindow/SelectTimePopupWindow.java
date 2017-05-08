/**
 * Project Name: itee
 * File Name:  SelectTimePopupWindow.java
 * Package Name: cn.situne.itee.common.widget.wheel
 * Date:   2015-01-15
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.view.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.Calendar;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.widget.wheel.SelectTimeNumericWheelAdapter;
import cn.situne.itee.common.widget.wheel.SelectTimeWheelView;
import cn.situne.itee.view.IteeTextView;

import static android.widget.LinearLayout.LayoutParams;

/**
 * ClassName:SelectTimePopupWindow <br/>
 * Function: select time popup window. <br/>
 * Date: 2015-01-20 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class SelectTimePopupWindow extends PopupWindow {

    public SelectTimeWheelView wheelViewHour, wheelViewMin;
    public Button btnOk, btnCancel;
    private View menuView;
    private LinearLayout llTitleContainer;

    private Context mContext;

    public SelectTimePopupWindow(Context context, View.OnClickListener itemclick) {
        super(context);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.select_time_popup_window, null);

        llTitleContainer = (LinearLayout) menuView.findViewById(R.id.ll_hour_and_min_container);

        LayoutUtils.setLinearLayoutHeight(llTitleContainer, 100, context);

        IteeTextView tvHour = (IteeTextView) menuView.findViewById(R.id.tv_hour);
        IteeTextView tvMinute = (IteeTextView) menuView.findViewById(R.id.tv_min);

        tvHour.setGravity(Gravity.CENTER);
        tvMinute.setGravity(Gravity.CENTER);

        tvHour.setText(R.string.common_hour);
        tvMinute.setText(R.string.common_minute);

        tvHour.setTextSize(Constants.FONT_SIZE_LARGER);
        tvMinute.setTextSize(Constants.FONT_SIZE_LARGER);

        tvHour.setTextColor(context.getResources().getColor(R.color.wheel_blue));
        tvMinute.setTextColor(context.getResources().getColor(R.color.wheel_blue));

        wheelViewHour = (SelectTimeWheelView) menuView.findViewById(R.id.wheel_hour);
        wheelViewHour.setAdapter(new SelectTimeNumericWheelAdapter(0, 23));
        wheelViewHour.setCyclic(true);

        wheelViewMin = (SelectTimeWheelView) menuView.findViewById(R.id.wheel_min);
        wheelViewMin.setAdapter(new SelectTimeNumericWheelAdapter(0, 59, "%02d"));
        wheelViewMin.setCyclic(true);

        // set current time
        Calendar c = Calendar.getInstance();
        int curHours = c.get(Calendar.HOUR_OF_DAY);
        int curMinutes = c.get(Calendar.MINUTE);

        wheelViewHour.setCurrentItem(curHours);
        wheelViewMin.setCurrentItem(curMinutes);

        btnOk = (Button) menuView.findViewById(R.id.btn_ok);
        btnCancel = (Button) menuView.findViewById(R.id.btn_cancel);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE); // 画框
        drawable.setStroke(2, R.color.common_blue); // 边框粗细及颜色
        drawable.setColor(Color.WHITE); // 边框内部颜色

        btnOk.setBackground(drawable); // 设置背景（效果就是有边框及底色）
        btnCancel.setBackground(drawable); // 设置背景（效果就是有边框及底色）
//        btnOk.setHeight(30);
//        btnCancel.setHeight(30);
        menuView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.popup_enter));
        //设置SelectPicPopupWindow的View
        this.setContentView(menuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);

        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.PopupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框

        menuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = menuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });


    }

    public SelectTimePopupWindow(Activity context, String[] times, int flag) {

        super(context);
        mContext = context;
        initSelectTimeView(context, times, flag, null);
    }


    public SelectTimePopupWindow(Activity context, final TextView clickView, int flag) {
        super(context);
        mContext = context;
        String timeStr = clickView.getText().toString();
        String[] times = timeStr.split(Constants.STR_COLON);
        initSelectTimeView(context, times, flag, clickView);

    }


    public SelectTimePopupWindow(Activity context,String time, final TextView clickView, int flag) {
        super(context);
        mContext = context;
        String[] times = time.split(Constants.STR_COLON);
        initSelectTimeView(context, times, flag, clickView);

    }

    private void initSelectTimeView(Activity context, String[] times, int flag, final TextView clickView) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.select_time_popup_window, null);
        mContext = context;
        IteeTextView tvHour = (IteeTextView) menuView.findViewById(R.id.tv_hour);
        IteeTextView tvMinute = (IteeTextView) menuView.findViewById(R.id.tv_min);

        tvHour.setGravity(Gravity.CENTER);
        tvMinute.setGravity(Gravity.CENTER);

        tvHour.setText(R.string.common_hour);
        tvMinute.setText(R.string.common_minute);

        tvHour.setTextSize(Constants.FONT_SIZE_LARGER);
        tvMinute.setTextSize(Constants.FONT_SIZE_LARGER);

        tvHour.setTextColor(context.getResources().getColor(R.color.wheel_blue));
        tvMinute.setTextColor(context.getResources().getColor(R.color.wheel_blue));

        wheelViewHour = (SelectTimeWheelView) menuView.findViewById(R.id.wheel_hour);
        wheelViewHour.setAdapter(new SelectTimeNumericWheelAdapter(0, 23));
        wheelViewHour.setCyclic(true);
        wheelViewMin = (SelectTimeWheelView) menuView.findViewById(R.id.wheel_min);
        wheelViewMin.setAdapter(new SelectTimeNumericWheelAdapter(0, 59, "%02d"));
        wheelViewMin.setCyclic(true);


        wheelViewHour.setCurrentItem(Integer.parseInt(times[0]));
        wheelViewMin.setCurrentItem(Integer.parseInt(times[1]));

        btnOk = (Button) menuView.findViewById(R.id.btn_ok);
        btnCancel = (Button) menuView.findViewById(R.id.btn_cancel);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE); // 画框
        drawable.setStroke(2, R.color.common_blue); // 边框粗细及颜色
        drawable.setColor(Color.WHITE); // 边框内部颜色

        btnOk.setBackground(drawable); // 设置背景（效果就是有边框及底色）
        btnCancel.setBackground(drawable); // 设置背景（效果就是有边框及底色）

        btnOk.setTextSize(Constants.FONT_SIZE_NORMAL);
        btnCancel.setTextSize(Constants.FONT_SIZE_NORMAL);

        menuView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.popup_enter));
        //设置SelectPicPopupWindow的View
        this.setContentView(menuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);

        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.PopupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String formatTime = String.format(Constants.STRING_FORMAT_02D_02D, wheelViewHour.getCurrentItem(), wheelViewMin.getCurrentItem());
                if (clickView != null) {
                    clickView.setText(formatTime);
                    clickView.setTextColor(mContext.getResources().getColor(R.color.common_black));
                }
                dismiss();
            }
        });

        menuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = menuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });


    }

}
