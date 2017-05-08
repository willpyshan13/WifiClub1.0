/**
 * Project Name: itee
 * File Name:  PaymentPatternPopupWindow.java
 * Package Name: cn.situne.itee.view
 * Date:   2015-04-14
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */
package cn.situne.itee.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.situne.itee.R;

/**
 * ClassName:PaymentPatternPopupWindow <br/>
 * Function: pay pattern popup window. <br/>
 * Date: 2015-04-14 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class PaymentPatternPopupWindow extends PopupWindow {
    public IteeTextView btnPayCash, btnCreditCard, btnThirdParty;
    public View mMenuView;

    public PaymentPatternPopupWindow(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.payment_pattern_popup_window, null);
        btnPayCash = (IteeTextView) mMenuView.findViewById(R.id.btn_pay_cash);
        btnThirdParty = (IteeTextView) mMenuView.findViewById(R.id.btn_third_party_payment);
        btnCreditCard = (IteeTextView) mMenuView.findViewById(R.id.btn_credit_card);

        //设置按钮监听
        btnPayCash.setOnClickListener(itemsOnClick);
        btnThirdParty.setOnClickListener(itemsOnClick);
        btnCreditCard.setOnClickListener(itemsOnClick);

        btnPayCash.setGravity(Gravity.CENTER);
        btnThirdParty.setGravity(Gravity.CENTER);
        btnCreditCard.setGravity(Gravity.CENTER);

        mMenuView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.popup_enter));
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.PopupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x88000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

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

}
