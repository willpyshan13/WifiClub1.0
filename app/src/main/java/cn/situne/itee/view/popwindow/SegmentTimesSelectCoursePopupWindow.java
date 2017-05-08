/**
 * Project Name: itee
 * File Name:  SelectTransferPopupWindow.java
 * Package Name: cn.situne.itee.common.widget.wheel
 * Date:   2015-03-21
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.view.popwindow;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import cn.situne.itee.R;

/**
 * ClassName:SelectTransferPopupWindow <br/>
 * Function: SelectTransferPopupWindow. <br/>
 * Date: 2015-03-21 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class SegmentTimesSelectCoursePopupWindow extends BasePopWindow {
    public ListView transferListView;

    public SegmentTimesSelectCoursePopupWindow(Activity context, View.OnClickListener itemclick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View menuView = inflater.inflate(R.layout.select_course_popup_window, null);

        transferListView = (ListView) menuView.findViewById(R.id.course_list_view);

        menuView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.popup_enter));
        setContentView(menuView);

        formatViews();

        setHideListener(menuView);
    }
}
