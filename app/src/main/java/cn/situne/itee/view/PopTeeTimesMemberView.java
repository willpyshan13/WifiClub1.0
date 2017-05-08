/**
 * Project Name: itee
 * File Name:  PopTeeTimesMemberView.java
 * Package Name: cn.situne.itee.view
 * Date:   2015-01-27
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;

/**
 * ClassName:PopTeeTimesMemberView <br/>
 * Function: select TeeTimes member popup window. <br/>
 * Date: 2015-01-27 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class PopTeeTimesMemberView extends PopupWindow {


    private LinearLayout llMember, llAgents, llEvents,llNoMember;
    private View mMenuView;
    private Context context;

    public PopTeeTimesMemberView(Activity context, boolean isNoEvent, boolean isNoMember, String nowSignType, View.OnClickListener itemsOnClick,String addAgent) {
        super(context);
        this.context = context;

        initView();

        if (isNoEvent) {
            llEvents.setVisibility(View.GONE);
        }
        if (isNoMember) {
            llMember.setVisibility(View.GONE);
        }
        switch (nowSignType) {
            case "1":
                llMember.setBackgroundColor(context.getResources().getColor(R.color.common_gray));
                break;
            case "2":
                llAgents.setBackgroundColor(context.getResources().getColor(R.color.common_gray));
                break;
            case "3":
                llEvents.setBackgroundColor(context.getResources().getColor(R.color.common_gray));
                break;

            case "4":
                llNoMember.setBackgroundColor(context.getResources().getColor(R.color.common_gray));
                break;
        }

        //设置按钮监听
        llMember.setOnClickListener(itemsOnClick);
        llAgents.setOnClickListener(itemsOnClick);
        llEvents.setOnClickListener(itemsOnClick);

        if (Constants.STR_1.equals(addAgent))llNoMember.setVisibility(View.GONE);

        llNoMember.setOnClickListener(itemsOnClick);

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y > height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_tee_time_member, null);
        llMember = (LinearLayout) mMenuView.findViewById(R.id.ll_tee_members);
        llAgents = (LinearLayout) mMenuView.findViewById(R.id.ll_tee_agents);
        llEvents = (LinearLayout) mMenuView.findViewById(R.id.ll_tee_events);

        llNoMember= (LinearLayout) mMenuView.findViewById(R.id.ll_tee_no_members);
    }
}