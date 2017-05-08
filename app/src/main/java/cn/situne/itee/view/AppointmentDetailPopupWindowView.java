/**
 * Project Name: itee
 * File Name:  AppointmentDetailPopupWindowView.java
 * Package Name: cn.situne.itee.view
 * Date:   2015-03-10
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import cn.situne.itee.R;
import cn.situne.itee.adapter.AppointmentDetailAdapter;
import cn.situne.itee.manager.jsonentity.JsonAppointmentDetail;

/**
 * ClassName:AppointmentDetailPopupWindowView <br/>
 * Function: select appointment detail popup window. <br/>
 * Date: 2015-01-26 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class AppointmentDetailPopupWindowView extends PopupWindow {

    private Context mContext;

    private View mMenuView;
    private ArrayList<JsonAppointmentDetail.Data> dataList;

    public AppointmentDetailPopupWindowView(Activity context, View view,
                                            ArrayList<JsonAppointmentDetail.Data> dataList) {
        super(context);
        this.mContext = context;
        this.dataList = dataList;

        initView();

        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x88000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        setBackgroundDrawable(dw);
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(mMenuView);
        showAtLocation(view, Gravity.BOTTOM, 0, 0);
        update();
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int bottom = mMenuView.findViewById(R.id.pop_layout).getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height || y > bottom) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popup_window_appointment_detail, null);
        TextView tvDetailTitle = (TextView) mMenuView.findViewById(R.id.tv_detail_title);
        if (dataList.size() > 0) {
            tvDetailTitle.setText(dataList.get(0).getMemberName());
        }
        ListView lvOfDetail = (ListView) mMenuView.findViewById(R.id.lv_of_detail);
        AppointmentDetailAdapter mAdapter = new AppointmentDetailAdapter(mContext, dataList);
        lvOfDetail.setAdapter(mAdapter);

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
