/**
 * Project Name: itee
 * File Name:  SelectIDTypePopupWindow.java
 * Package Name: cn.situne.itee.common.widget.wheel
 * Date:   2015-01-20
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */
package cn.situne.itee.view.popwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import cn.situne.itee.R;
import cn.situne.itee.common.widget.wheel.SelectTextWheelAdapter;
import cn.situne.itee.common.widget.wheel.SelectTimeOnWheelChangedListener;
import cn.situne.itee.common.widget.wheel.SelectTimeWheelView;
import cn.situne.itee.fragment.player.PlayerMemberShipEditFragment;
import cn.situne.itee.manager.jsonentity.JsonMemberShip;
import cn.situne.itee.view.IteeButton;
import cn.situne.itee.view.IteeTextView;

import static android.widget.LinearLayout.LayoutParams;

/**
 * ClassName:SelectIDTypePopupWindow <br/>
 * Function: select id type popup window. <br/>
 * Date: 2015-01-20 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class SelectIDTypePopupWindow extends PopupWindow {

    private View menuView;
    public IteeButton btnOk;
    public IteeButton btnCancel;
    private SelectTimeWheelView type, typeDetail;
    private JsonMemberShip.MemberTypeListItem nonMember;
    private SelectTextWheelAdapter adapterDetail;
    private SelectTextWheelAdapter adapterNonMember;
    private List<JsonMemberShip.MemberTypeListItem> typeDetailEntity;

    public SelectIDTypePopupWindow(Context context, final List<JsonMemberShip.MemberTypeListItem> list,
                                   final PlayerMemberShipEditFragment.OnIDTypeSelectClickListener clickListener,
                                   Bundle bundle) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.popw_id_type_two_wheel, null);
        IteeTextView tvTitle = (IteeTextView) menuView.findViewById(R.id.tv_title);
        tvTitle.setText(context.getString(R.string.player_info_membership_id_type));
        tvTitle.setGravity(Gravity.CENTER);
        btnOk = (IteeButton) menuView.findViewById(R.id.btn_ok);
        btnCancel = (IteeButton) menuView.findViewById(R.id.btn_cancel);

        type = (SelectTimeWheelView) menuView.findViewById(R.id.wheel_type);
        final List<String> typeDateSource = new ArrayList<>();
        typeDetailEntity = new ArrayList<>();
        typeDateSource.add(context.getString(R.string.customers_non_member));
        typeDateSource.add(context.getString(R.string.customers_member));
        SelectTextWheelAdapter adapter = new SelectTextWheelAdapter(typeDateSource, 20);
        type.setAdapter(adapter);
        typeDetail = (SelectTimeWheelView) menuView.findViewById(R.id.wheel_type_detail);

        Integer memberTypeId = bundle.getInt("memberTypeId");

        int index = 0;
        int typeIndex = 0;
        ArrayList<String> typeDetailDateSource = new ArrayList<>();
        ArrayList<String> noMemberDateSource = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                JsonMemberShip.MemberTypeListItem item = list.get(i);

                if (item.getMemberParentId() == 1) {
                    typeDetailEntity.add(item);
                    typeDetailDateSource.add(item.getMemberType());

                } else {
                    noMemberDateSource.add(item.getMemberType());
                    nonMember = item;
                }

                if (memberTypeId.intValue() == item.getMemberTypeId()) {
                    if (item.getMemberParentId() == 1) {
                        typeIndex = 1;
                        index = typeDetailEntity.size() - 1;
                    } else {
                        typeIndex = 0;
                        index = 0;
                    }
                }


            }
        }
        adapterDetail = new SelectTextWheelAdapter(typeDetailDateSource, 20);
        adapterNonMember = new SelectTextWheelAdapter(noMemberDateSource, 20);
        if (typeIndex == 0) {
            typeDetail.setAdapter(adapterNonMember);
        } else {
            typeDetail.setAdapter(adapterDetail);
        }

        //init TextWheel.
        type.setCurrentItem(typeIndex);
        typeDetail.setCurrentItem(index);

        type.addChangingListener(new SelectTimeOnWheelChangedListener() {
            @Override
            public void onChanged(SelectTimeWheelView wheel, int oldValue, int newValue) {

                if (newValue == 0) {
                    typeDetail.setAdapter(adapterNonMember);
                    typeDetail.setCurrentItem(0);
                } else {
                    typeDetail.setAdapter(adapterDetail);
                }
            }
        });


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
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int nonMemberIndex = type.getCurrentItem();
                if (nonMemberIndex == 0) {
                    clickListener.returnMember("1", nonMember);
                } else {
                    int memberIndex = typeDetail.getCurrentItem();

                    clickListener.returnMember("1", typeDetailEntity.get(memberIndex));
                }
                dismiss();

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

}
