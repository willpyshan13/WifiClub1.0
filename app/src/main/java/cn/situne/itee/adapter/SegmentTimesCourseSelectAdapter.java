/**
 * Project Name: itee
 * File Name:  SegmentTimesCourseAdapter.java
 * Package Name: cn.situne.itee.adapter
 * Date:   2015-03-21
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:SegmentTimesCourseAdapter <br/>
 * Function: FIXME. <br/>
 * Date: 2015-03-21 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class SegmentTimesCourseSelectAdapter extends BaseAdapter {

    public ArrayList<HashMap<String, Object>> courseAreaTypeList;

    public Context context;

    public SegmentTimesCourseSelectAdapter(Context context, ArrayList<HashMap<String, Object>> courseAreaTypeList) {
        this.courseAreaTypeList = courseAreaTypeList;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (courseAreaTypeList == null) {
            return 0;
        }
        return courseAreaTypeList.size();
    }

    @Override
    public Object getItem(int position) {
        return courseAreaTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.item_of_segment_times_course, null);
            viewHolder.courseName = (IteeTextView) convertView.findViewById(R.id.tv_course_area_name);


            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position > -1 && position < courseAreaTypeList.size()) {
            Map<String, Object> item = courseAreaTypeList.get(position);

            viewHolder.courseName.setText((String) item.get("courseAreaName"));
        }


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }

    public class ViewHolder {
        public IteeTextView courseName;
    }
}
