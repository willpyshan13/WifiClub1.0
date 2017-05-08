/**
 * Project Name: itee
 * File Name:  TeeTimeSettingTransferCourseAdapter.java
 * Package Name: cn.situne.itee.adapter
 * Date:   2015-03-13
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

import cn.situne.itee.R;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:TeeTimeSettingTransferCourseAdapter <br/>
 * Function: TeeTimeSettingTransferCourseAdapter. <br/>
 * Date: 2015-03-13 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class TeeTimeSettingTransferCourseAdapter extends BaseAdapter {

    ArrayList<HashMap<String, Object>> transferTimeData;
    private Context context;


    public TeeTimeSettingTransferCourseAdapter(Context context, ArrayList<HashMap<String, Object>> transferTimeData) {
        this.context = context;
        this.transferTimeData = transferTimeData;
    }

    @Override
    public int getCount() {
        return transferTimeData.size();
    }

    @Override
    public Object getItem(int position) {
        return transferTimeData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder = new ViewHolder();

        if (view == null) {

            LayoutInflater mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(R.layout.item_of_tee_time_setting_transfer_course, null);
            holder.tvName = (IteeTextView) view.findViewById(R.id.tv_transfer_name);
            holder.tvTime = (IteeTextView) view.findViewById(R.id.tv_transfer_time);
            view.setTag(holder);

            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(holder.tvName, context);
            LayoutUtils.setCellRightValueViewOfRelativeLayout(holder.tvTime, context);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tvName.setText((String) (transferTimeData.get(position).get("name")));
        holder.tvTime.setText((String) (transferTimeData.get(position).get("time")));

        return view;
    }

    public class ViewHolder {
        IteeTextView tvName;
        IteeTextView tvTime;
    }
}
