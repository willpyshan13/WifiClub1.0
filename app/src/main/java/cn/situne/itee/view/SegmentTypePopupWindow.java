/**
 * Project Name: itee
 * File Name:  SegmentTypePopupWindow.java
 * Package Name: cn.situne.itee.view
 * Date:   2015-01-16
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.situne.itee.R;

/**
 * ClassName:SegmentTypePopupWindow <br/>
 * Function: select segment type popup window. <br/>
 * Date: 2015-01-16 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class SegmentTypePopupWindow extends PopupWindow {

    public TextView btnTwoTeeStart, btnNineHolesOnly, btnBlockTimes, btnMemberOnly, btnThreeTeeStart, btnPrimeTime;
    public View mMenuView;

    public SegmentTypePopupWindow(Activity context, OnClickListener itemsOnClick) {
        super(context);
        initView(context);

        //设置按钮监听
        btnTwoTeeStart.setOnClickListener(itemsOnClick);
        btnNineHolesOnly.setOnClickListener(itemsOnClick);
        btnBlockTimes.setOnClickListener(itemsOnClick);
        btnMemberOnly.setOnClickListener(itemsOnClick);
        btnThreeTeeStart.setOnClickListener(itemsOnClick);
        btnPrimeTime.setOnClickListener(itemsOnClick);
        mMenuView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.popup_enter));
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.PopupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x88000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
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

    private void initView(Activity context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.segment_type_popup_window, null);
        btnTwoTeeStart = (IteeTextView) mMenuView.findViewById(R.id.btn_two_tee_start);
        btnNineHolesOnly = (IteeTextView) mMenuView.findViewById(R.id.btn_nine_holes_only);
        btnBlockTimes = (IteeTextView) mMenuView.findViewById(R.id.btn_block_times);
        btnMemberOnly = (IteeTextView) mMenuView.findViewById(R.id.btn_member_only);
        btnThreeTeeStart = (IteeTextView) mMenuView.findViewById(R.id.btn_three_tee_start);
        btnPrimeTime = (IteeTextView) mMenuView.findViewById(R.id.btn_prime_time);

        btnTwoTeeStart.setGravity(Gravity.CENTER);
        btnNineHolesOnly.setGravity(Gravity.CENTER);
        btnBlockTimes.setGravity(Gravity.CENTER);
        btnMemberOnly.setGravity(Gravity.CENTER);
        btnThreeTeeStart.setGravity(Gravity.CENTER);
        btnPrimeTime.setGravity(Gravity.CENTER);
    }

}