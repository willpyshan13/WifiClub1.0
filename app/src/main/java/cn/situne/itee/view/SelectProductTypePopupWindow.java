/**
 * Project Name: itee
 * File Name:  SelectProductTypePopupWindow.java
 * Package Name: cn.situne.itee.fragment.teetime
 * Date:   2015-03-06
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
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.situne.itee.R;

/**
 * ClassName:SelectProductTypePopupWindow <br/>
 * Function: select product type. <br/>
 * Date: 2015-03-06 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class SelectProductTypePopupWindow extends PopupWindow {

    public ImageView ivCaddieIcon;
    public TextView tvCaddie;

    public ImageView ivCartIcon;
    public TextView tvCart;

    public ImageView ivClubsIcon;
    public TextView tvClubs;

    public ImageView ivShoesIcon;
    public TextView tvShoes;

    public ImageView ivTrolleyIcon;
    public TextView tvTrolley;

    public ImageView ivUmbrellaIcon;
    public TextView tvUmbrella;

    public ImageView ivTowelIcon;
    public TextView tvTowel;

    public View mMenuView;

    public SelectProductTypePopupWindow(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mMenuView = inflater.inflate(R.layout.popupwindow_select_product_type, null);

        ivCaddieIcon = (ImageView) mMenuView.findViewById(R.id.icon_caddie);
        tvCaddie = (TextView) mMenuView.findViewById(R.id.tv_caddie);

        ivCartIcon = (ImageView) mMenuView.findViewById(R.id.icon_cart);
        tvCart = (TextView) mMenuView.findViewById(R.id.tv_cart);

        ivClubsIcon = (ImageView) mMenuView.findViewById(R.id.icon_clubs);
        tvClubs = (TextView) mMenuView.findViewById(R.id.tv_clubs);

        ivShoesIcon = (ImageView) mMenuView.findViewById(R.id.icon_shoes);
        tvShoes = (TextView) mMenuView.findViewById(R.id.tv_shoes);

        ivTrolleyIcon = (ImageView) mMenuView.findViewById(R.id.icon_trolley);
        tvTrolley = (TextView) mMenuView.findViewById(R.id.tv_trolley);

        ivUmbrellaIcon = (ImageView) mMenuView.findViewById(R.id.icon_umbrella);
        tvUmbrella = (TextView) mMenuView.findViewById(R.id.tv_umbrella);

        ivTowelIcon = (ImageView) mMenuView.findViewById(R.id.icon_towel);
        tvTowel = (TextView) mMenuView.findViewById(R.id.tv_towel);

        ivCaddieIcon.setImageResource(R.drawable.icon_shops_product_edit_caddie_select);

        //设置按钮监听
        tvCaddie.setOnClickListener(itemsOnClick);
        tvCart.setOnClickListener(itemsOnClick);
        tvClubs.setOnClickListener(itemsOnClick);
        tvShoes.setOnClickListener(itemsOnClick);
        tvTrolley.setOnClickListener(itemsOnClick);
        tvUmbrella.setOnClickListener(itemsOnClick);
        tvTowel.setOnClickListener(itemsOnClick);

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
