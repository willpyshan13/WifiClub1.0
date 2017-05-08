/**
 * Project Name: itee
 * File Name:  DeleteComfirmPopupWindow.java
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
import android.widget.TextView;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;

/**
 * ClassName:DeleteConfirmPopupWindow <br/>
 * Function: DeleteConfirmPopupWindow. <br/>
 * Date: 2015-03-20 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class DeleteConfirmPopupWindow extends BasePopWindow {

    private View menuView;
    public TextView tvShowMessage;
    public Button btn_ok, btn_cancel;

    public DeleteConfirmPopupWindow(final Activity context, View.OnClickListener itemclick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.delete_comfirm_popup_window, null);

        btn_ok = (Button) menuView.findViewById(R.id.btn_ok);
        btn_cancel = (Button) menuView.findViewById(R.id.btn_cancel);
        tvShowMessage = (TextView) menuView.findViewById(R.id.tv_show_message);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE); // 画框
        drawable.setStroke(2, Color.parseColor("#003A78")); // 边框粗细及颜色
        drawable.setColor(Color.WHITE); // 边框内部颜色
        btn_ok.setHeight(30);
        btn_cancel.setHeight(30);
        tvShowMessage.setText(context.getResources().getString(R.string.tee_times_cover_old_tee_times));
        tvShowMessage.setTextSize(Constants.FONT_SIZE_LARGER);
        tvShowMessage.setTextColor(context.getResources().getColor(R.color.common_blue));

        menuView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.popup_enter));
       /* course_out.setBackgroundDrawable(drawable); // 设置背景（效果就是有边框及底色）
        course_in.setBackgroundDrawable(drawable); // 设置背景（效果就是有边框及底色）*/
        //设置SelectPicPopupWindow的View
        this.setContentView(menuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.PopupAnimation);
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


    }

}
