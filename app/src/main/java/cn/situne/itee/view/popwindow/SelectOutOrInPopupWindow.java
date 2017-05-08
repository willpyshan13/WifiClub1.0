package cn.situne.itee.view.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import cn.situne.itee.R;

import static android.widget.LinearLayout.LayoutParams;


public class SelectOutOrInPopupWindow extends BasePopWindow {

    private View menuView;
    public Button btFirstValue;
    public Button btSecondValue;
    public Button btThirdValue;


    public SelectOutOrInPopupWindow(Activity context, View.OnClickListener clickListener) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.select_out_or_in_popup_window, null);

        btFirstValue = (Button) menuView.findViewById(R.id.bt_course_out);
        btSecondValue = (Button) menuView.findViewById(R.id.bt_course_in);
        btThirdValue = (Button) menuView.findViewById(R.id.bt_course_other);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE); // 画框
        drawable.setStroke(2, Color.parseColor("#003A78")); // 边框粗细及颜色
        drawable.setColor(Color.WHITE); // 边框内部颜色

        btFirstValue.setBackground(drawable); // 设置背景（效果就是有边框及底色）
        btSecondValue.setBackground(drawable); // 设置背景（效果就是有边框及底色）
        btThirdValue.setBackground(drawable);// 设置背景（效果就是有边框及底色）
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

        setHideListener(menuView);


    }

}
