/**
 * Project Name: itee
 * File Name:  SelectChangeCoursePopupWindow.java
 * Package Name: cn.situne.itee.common.widget.wheel
 * Date:   2015-01-15
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.view.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:SelectChangeCoursePopupWindow <br/>
 * Function: change course popup window. <br/>
 * Date: 2015-01-20 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class SelectChangeCoursePopupWindow extends BasePopWindow {

    private Button btnOk;
    private Button btnCancel;

    private ListView lvPopupWindow;

    public LinearLayout rlOkAndCancelContainer;
    public RelativeLayout rlAddListView;

    private Context mContext;

    public SelectChangeCoursePopupWindow(final Activity context, View.OnClickListener onClickListener) {
        super(context);
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View menuView = inflater.inflate(R.layout.select_change_course_popup_window, null);
        lvPopupWindow = (ListView) menuView.findViewById(R.id.list);
        btnOk = (Button) menuView.findViewById(R.id.btn_ok);
        btnCancel = (Button) menuView.findViewById(R.id.btn_cancel);
        rlOkAndCancelContainer = (LinearLayout) menuView.findViewById(R.id.rl_ok_and_cancel_container);
        rlAddListView = new RelativeLayout(context);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE); // 画框
        drawable.setStroke(2, Color.parseColor("#003A78")); // 边框粗细及颜色
        drawable.setColor(Color.WHITE); // 边框内部颜色

        //设置SelectPicPopupWindow的View
        setContentView(menuView);
        setHideListener(menuView);

        formatViews();
    }

    public Button getBtnOk() {
        return btnOk;
    }

    public Button getBtnCancel() {
        return btnCancel;
    }

    public ListView getLvPopupWindow() {
        return lvPopupWindow;
    }

    public static class ChangeCourseAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<HashMap<String, String>> courseAreaTypeList;

        public ChangeCourseAdapter(Context mContext, ArrayList<HashMap<String, String>> courseAreaTypeList) {
            this.mContext = mContext;
            this.courseAreaTypeList = courseAreaTypeList;
        }

        @Override
        public int getCount() {
            if (courseAreaTypeList != null) {
                return courseAreaTypeList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return courseAreaTypeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.select_change_course_popup_window_item, parent, false);
                viewHolder = new ViewHolder();

                viewHolder.tvCourse = (IteeTextView) convertView.findViewById(R.id.tv_course);

                viewHolder.tvCourse.setGravity(Gravity.CENTER);
                viewHolder.tvCourse.setTextSize(Constants.FONT_SIZE_SMALLER);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            HashMap<String, String> hashMap = courseAreaTypeList.get(position);
            String courseName = hashMap.get(TransKey.COURSE_AREA_TYPE);
            viewHolder.tvCourse.setText(courseName);

            return convertView;
        }

        class ViewHolder {
            IteeTextView tvCourse;
        }
    }
}
