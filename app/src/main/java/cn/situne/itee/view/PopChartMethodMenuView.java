/**
 * Project Name: itee
 * File Name:  PopTeeTimesMenuView.java
 * Package Name: cn.situne.itee.view
 * Date:   2015-01-27
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
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

/**
 * ClassName:PopTeeTimesMenuView <br/>
 * Function: select TeeTimes menu popup window. <br/>
 * Date: 2015-01-27 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class PopChartMethodMenuView extends PopupWindow {

    private View mMenuView;
    private LinearLayout llYear, llMonth, llWeek, llDay;
    private IteeTextView tvYear, tvMonth, tvWeek, tvDay;

    public PopChartMethodMenuView(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        initView(context);

        //设置按钮监听
        llYear.setOnClickListener(itemsOnClick);
        llMonth.setOnClickListener(itemsOnClick);
        llWeek.setOnClickListener(itemsOnClick);
        llDay.setOnClickListener(itemsOnClick);

        tvYear.setText(context.getString(R.string.common_year));
        tvMonth.setText(context.getString(R.string.common_month));
        tvWeek.setText(R.string.common_week);
        tvDay.setText(context.getString(R.string.common_day));

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
        mMenuView = inflater.inflate(R.layout.pop_chart_method_menu, null);
        llYear = (LinearLayout) mMenuView.findViewById(R.id.ll_year);
        llMonth = (LinearLayout) mMenuView.findViewById(R.id.ll_month);
        llWeek = (LinearLayout) mMenuView.findViewById(R.id.ll_week);
        llDay = (LinearLayout) mMenuView.findViewById(R.id.ll_day);

        tvYear = (IteeTextView) mMenuView.findViewById(R.id.tv_year);
        tvMonth = (IteeTextView) mMenuView.findViewById(R.id.tv_month);
        tvWeek = (IteeTextView) mMenuView.findViewById(R.id.tv_week);
        tvDay = (IteeTextView) mMenuView.findViewById(R.id.tv_day);
    }

}