/**
 * Project Name: itee
 * File Name:  AddressTypePopupWindow.java
 * Package Name: cn.situne.itee.view
 * Date:   2015-03-10
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.view.popwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import org.apache.commons.lang3.StringUtils;

import cn.situne.itee.R;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:AddressTypePopupWindow <br/>
 * Function: select address type popup window. <br/>
 * Date: 2015-03-10 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class AddressTypePopupWindow extends PopupWindow {

    public IteeTextView tvTypeHome, tvTypeWork, tvTypeOther, tvTypeCustom, tvTypeMobile, tvCustomConfirm;
    public View mMenuView;
    private IteeEditText etCustom;
    private LinearLayout ll_custom;
    private boolean isCustom;

    /**
     * @param context      context
     * @param itemsOnClick listener
     * @param paramTag     null:isCustom;
     */
    public AddressTypePopupWindow(Context context, final SelectDatePopupWindow.OnDateSelectClickListener itemsOnClick, String paramTag, Boolean isPhone) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popw_address_type, null);

        tvTypeHome = (IteeTextView) mMenuView.findViewById(R.id.tv_address_type_home);
        tvTypeWork = (IteeTextView) mMenuView.findViewById(R.id.tv_repay);
        tvTypeMobile = (IteeTextView) mMenuView.findViewById(R.id.tv_choose_other_payment);
        tvTypeOther = (IteeTextView) mMenuView.findViewById(R.id.tv_address_type_other);
        tvTypeCustom = (IteeTextView) mMenuView.findViewById(R.id.tv_address_type_custom);
        ll_custom = (LinearLayout) mMenuView.findViewById(R.id.ll_address_type_custom);
        etCustom = (IteeEditText) mMenuView.findViewById(R.id.et_address_type_custom);
        tvCustomConfirm = (IteeTextView) mMenuView.findViewById(R.id.tv_address_type_custom_confirm);

        tvTypeHome.setGravity(Gravity.CENTER);
        tvTypeWork.setGravity(Gravity.CENTER);
        tvTypeMobile.setGravity(Gravity.CENTER);
        tvTypeOther.setGravity(Gravity.CENTER);
        tvTypeCustom.setGravity(Gravity.CENTER);
        tvCustomConfirm.setGravity(Gravity.CENTER);
        etCustom.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);

        int padding = DensityUtil.getActualWidthOnThisDevice(20, context);
        etCustom.setPadding(padding, 0, padding, 0);

        if (isPhone) {
            tvTypeMobile.setVisibility(View.VISIBLE);
        } else {
            tvTypeMobile.setVisibility(View.GONE);
        }
        if (paramTag != null) {
            isCustom = true;
            etCustom.setText(paramTag);
            ll_custom.setVisibility(View.VISIBLE);
        }

        //设置按钮监听
        tvTypeHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsOnClick.OnGoodItemClick("1", StringUtils.EMPTY);
                dismiss();
            }
        });
        tvTypeWork.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsOnClick.OnGoodItemClick("2", StringUtils.EMPTY);
                dismiss();
            }
        });

        tvTypeMobile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsOnClick.OnGoodItemClick("3", StringUtils.EMPTY);
                dismiss();
            }
        });
        tvTypeOther.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsOnClick.OnGoodItemClick("4", StringUtils.EMPTY);
                dismiss();
            }
        });

        tvCustomConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsOnClick.OnGoodItemClick("5", etCustom.getText().toString());
                dismiss();
            }
        });
        tvTypeCustom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isCustom = !isCustom;
                if (isCustom) {
                    ll_custom.setVisibility(View.VISIBLE);
                } else {
                    ll_custom.setVisibility(View.GONE);
                }
            }
        });
        mMenuView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.popup_enter));
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
        ColorDrawable dw = new ColorDrawable(0xb0000000);
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

}