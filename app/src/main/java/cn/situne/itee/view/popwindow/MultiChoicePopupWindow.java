/**
 * Project Name: itee
 * File Name:  MutilChoicePopupWindow.java
 * Package Name: cn.situne.itee.common.widget.wheel
 * Date:   2015-03-19
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.view.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import cn.situne.itee.R;
import cn.situne.itee.activity.MainActivity;
import cn.situne.itee.adapter.MultiChoiceAdapter;

/**
 * ClassName:MultiChoicePopupWindow <br/>
 * Function: MultiChoicePopupWindow. <br/>
 * Date: 2015-03-19 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class MultiChoicePopupWindow extends BasePopWindow {

    public ListView lvPopupWindow;
    public MultiChoiceAdapter adapter;
    public Button btn_ok, btn_cancel;
    private View menuView;

    public MultiChoicePopupWindow(final Activity context, View.OnClickListener itemclick, int dataSize) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.select_change_course_popup_window, null);

        btn_ok = (Button) menuView.findViewById(R.id.btn_ok);
        btn_cancel = (Button) menuView.findViewById(R.id.btn_cancel);
        lvPopupWindow = (ListView) menuView.findViewById(R.id.list);
        lvPopupWindow.setDivider(null);
        lvPopupWindow.setDividerHeight(0);

        LinearLayout.LayoutParams lvPopupWindowParams = (LinearLayout.LayoutParams) lvPopupWindow.getLayoutParams();

        if (dataSize > 7) {
            MainActivity main = (MainActivity) context;
            lvPopupWindowParams.height = main.getHeight(600);
        }

        setContentView(menuView);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE); // 画框
        drawable.setStroke(2, Color.parseColor("#003A78")); // 边框粗细及颜色
        drawable.setColor(Color.WHITE); // 边框内部颜色
        btn_ok.setHeight(30);
        btn_cancel.setHeight(30);

        formatViews();

        setHideListener(menuView);
    }
}
