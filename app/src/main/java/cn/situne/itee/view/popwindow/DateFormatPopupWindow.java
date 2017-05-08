/**
 * Project Name: itee
 * File Name:  PaymentPatternPopupWindow.java
 * Package Name: cn.situne.itee.view
 * Date:   2015-04-14
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.view.popwindow;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
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
public class DateFormatPopupWindow extends BasePopWindow {

    public TextView tvYMD, tvDMY, tvMDY;
    public View mMenuView;

    public DateFormatPopupWindow(Context context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.date_format_popup_window, null);

        tvYMD = (TextView) mMenuView.findViewById(R.id.tv_yyyy_mm_dd);
        tvDMY = (TextView) mMenuView.findViewById(R.id.tv_dd_mm_yyyy);
        tvMDY = (TextView) mMenuView.findViewById(R.id.tv_mm_dd_yyyy);

        tvYMD.setGravity(Gravity.CENTER);
        tvDMY.setGravity(Gravity.CENTER);
        tvMDY.setGravity(Gravity.CENTER);

        //设置按钮监听
        tvYMD.setOnClickListener(itemsOnClick);
        tvDMY.setOnClickListener(itemsOnClick);
        tvMDY.setOnClickListener(itemsOnClick);

        mMenuView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.popup_enter));
        setContentView(mMenuView);

        formatViews();
        setHideListener(mMenuView);
    }

}
