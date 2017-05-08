/**
 * Project Name: itee
 * File Name:  SignaturePopupWindow.java
 * Package Name: cn.situne.itee.view
 * Date:   2015-01-16
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */
package cn.situne.itee.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.android.volley.toolbox.NetworkImageView;

import cn.situne.itee.R;
import cn.situne.itee.common.utils.AppUtils;

/**
 * ClassName:SignaturePopupWindow <br/>
 * Function: show signature popup window. <br/>
 * Date: 2015-01-16 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class SignaturePopupWindow extends PopupWindow {


    public NetworkImageView imSignature;
    public View mMenuView;

    public SignaturePopupWindow(Context context, OnClickListener itemsOnClick, String url) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popw_signature, null);


        imSignature = (NetworkImageView) mMenuView.findViewById(R.id.im_signature);
        //设置按钮监听
//        mMenuView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.push_in_left));
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                dismiss();
                return true;
            }

        });
        imSignature.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        AppUtils.showNetworkImage(imSignature, url);

    }

}