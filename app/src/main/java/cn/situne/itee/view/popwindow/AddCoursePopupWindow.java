/**
 * Project Name: itee
 * File Name:  FillInTipsPopupWindow.java
 * Package Name: cn.situne.itee.common.widget.wheel
 * Date:   2015-03-20
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.view.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.situne.itee.R;

/**
 * ClassName:FillInTipsPopupWindow <br/>
 * Function: FillInTipsPopupWindow. <br/>
 * Date: 2015-03-20 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class AddCoursePopupWindow extends PopupWindow {

    private View menuView;
    public TextView tvEvent, tvContent;
    public Button btn_ok, btn_cancel;

    private boolean notTouchCancel;

    public boolean isNotTouchCancel() {
        return notTouchCancel;
    }

    public void setNotTouchCancel(boolean notTouchCancel) {
        this.notTouchCancel = notTouchCancel;
    }

    public AddCoursePopupWindow(Activity context, View.OnClickListener itemclick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.add_course_popup_window, null);

        tvEvent = (TextView) menuView.findViewById(R.id.tv_event);
        tvContent = (TextView) menuView.findViewById(R.id.tv_content);

        btn_ok = (Button) menuView.findViewById(R.id.btn_ok);
        btn_cancel = (Button) menuView.findViewById(R.id.btn_cancel);


        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE); // 画框
        drawable.setStroke(2, R.color.common_blue); // 边框粗细及颜色
        drawable.setColor(Color.WHITE); // 边框内部颜色

        btn_ok.setBackground(drawable); // 设置背景（效果就是有边框及底色）
        btn_ok.setHeight(30);

        btn_cancel.setBackground(drawable); // 设置背景（效果就是有边框及底色）
        btn_cancel.setHeight(30);
        menuView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.popup_enter));
        //设置SelectPicPopupWindow的View
        this.setContentView(menuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
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


                if (!notTouchCancel) {

                    int height = menuView.findViewById(R.id.pop_layout).getTop();
                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y < height) {
                            dismiss();
                        }
                    }
                }


                return true;
            }
        });


    }
}
