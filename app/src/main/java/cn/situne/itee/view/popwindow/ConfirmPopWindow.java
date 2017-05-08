/**
 * Project Name: itee
 * File Name:	 ConfirmPopWindow.java
 * Package Name: cn.situne.itee.view.popwindow
 * Date:		 2015-07-01
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.view.popwindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import cn.situne.itee.R;

/**
 * ClassName:ConfirmPopWindow <br/>
 * Function: confirm window. <br/>
 * Date: 2015-07-01 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class ConfirmPopWindow extends BasePopWindow {

    private TextView tvMessage;
    private Button btnOk, btnCancel;

    public ConfirmPopWindow(Context context, View.OnClickListener listener) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View menuView = inflater.inflate(R.layout.confirm_popup_window, null);

        tvMessage = (TextView) menuView.findViewById(R.id.tv_message);
        tvMessage.setSingleLine(false);

        btnOk = (Button) menuView.findViewById(R.id.btn_ok);
        btnCancel = (Button) menuView.findViewById(R.id.btn_cancel);


        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE); // 画框
        drawable.setStroke(2, context.getResources().getColor(R.color.common_blue)); // 边框粗细及颜色
        drawable.setColor(Color.WHITE); // 边框内部颜色

        btnOk.setBackground(drawable); // 设置背景（效果就是有边框及底色）
        btnOk.setHeight(30);

        btnCancel.setBackground(drawable); // 设置背景（效果就是有边框及底色）
        btnCancel.setHeight(30);

        menuView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.popup_enter));
        //设置SelectPicPopupWindow的View
        this.setContentView(menuView);

        formatViews();

        setHideListener(menuView);

        setListeners(listener);
    }

    private void setListeners(final View.OnClickListener listener) {
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                listener.onClick(v);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setMessage(String message) {
        tvMessage.setText(message);
    }
}