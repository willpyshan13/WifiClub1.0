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

import java.util.Calendar;

import cn.situne.itee.R;
import cn.situne.itee.common.widget.wheel.SelectTimeNumericWheelAdapter;
import cn.situne.itee.common.widget.wheel.SelectTimeWheelView;
import cn.situne.itee.view.IteeTextView;

/**
 * Created by luochao on 12/10/15.
 */
public class SelectMinPopupWindow  extends PopupWindow {

    private View menuView;
    public SelectTimeWheelView wheelViewYear;
    public Button btnOk, btnCancel;

    public void setYear(int minYear, int maxYear, int nowYear) {
        wheelViewYear.setAdapter(new SelectTimeNumericWheelAdapter(minYear, maxYear));
        wheelViewYear.setCurrentItem(nowYear - minYear);
        wheelViewYear.setCyclic(false);
    }

    public SelectMinPopupWindow(Activity context, View.OnClickListener itemclick,int showMin) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.popup_window_select_year, null);

        wheelViewYear = (SelectTimeWheelView) menuView.findViewById(R.id.wheel_year);
        wheelViewYear.setAdapter(new SelectTimeNumericWheelAdapter(1, 60));
        wheelViewYear.setCyclic(true);

        IteeTextView t =  (IteeTextView) menuView.findViewById(R.id.tv_year);
        t.setText("Min");

        wheelViewYear.setCurrentItem(showMin - 1);

        btnOk = (Button) menuView.findViewById(R.id.btn_ok);
        btnCancel = (Button) menuView.findViewById(R.id.btn_cancel);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE); // 画框
        drawable.setStroke(2, R.color.common_blue); // 边框粗细及颜色
        drawable.setColor(Color.WHITE); // 边框内部颜色

        btnOk.setBackground(drawable); // 设置背景（效果就是有边框及底色）
        btnCancel.setBackground(drawable); // 设置背景（效果就是有边框及底色）
        btnOk.setHeight(30);
        btnCancel.setHeight(30);
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
