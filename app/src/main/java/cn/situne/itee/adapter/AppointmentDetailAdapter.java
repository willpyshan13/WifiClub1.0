/**
 * Project Name: itee
 * File Name:  AppointmentDetailAdapter.java
 * Package cn.situne.itee.adapter
 * Date:   2015-01-26
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.manager.jsonentity.JsonAppointmentDetail;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:AppointmentDetailAdapter <br/>
 * Function: AppointmentDetailPopupWindowView 文件 使用.  变量名 mAdapter <br/>
 * <p/>
 * Date: 2015-01-10 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class AppointmentDetailAdapter extends BaseAdapter {

    private Context mContext;
    private List<JsonAppointmentDetail.Data> dataList;

    public AppointmentDetailAdapter(Context context, List<JsonAppointmentDetail.Data> dataList) {
        this.mContext = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_of_appointment_detail, null);
            holder = new ViewHolder();

            holder.ivItemIcon = (ImageView) view.findViewById(R.id.iv_item_icon);
            holder.tvItemName = (IteeTextView) view.findViewById(R.id.tv_item_name);
            holder.llItemContent = (LinearLayout) view.findViewById(R.id.ll_item_content);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.contentList.clear();
        holder.llItemContent.removeAllViews();
//        holder.tvItemName.setText(dataList.get(position).getGoodsName());

        int goodsId = dataList.get(position).getCategoryId();

        if (goodsId == 1) {
            holder.ivItemIcon.setImageResource(R.drawable.icon_caddie);
            holder.tvItemName.setText(R.string.staff_caddies);
        } else if (goodsId == 2) {
            holder.ivItemIcon.setImageResource(R.drawable.icon_cart);
            holder.tvItemName.setText(R.string.shop_setting_cart);
        } else if (goodsId == 3) {
            holder.ivItemIcon.setImageResource(R.drawable.icon_club);
            holder.tvItemName.setText(R.string.shop_setting_clubs);
        } else if (goodsId == 4) {
            holder.ivItemIcon.setImageResource(R.drawable.icon_shoes);
            holder.tvItemName.setText(R.string.shop_setting_shoes);
        } else if (goodsId == 5) {
            holder.ivItemIcon.setImageResource(R.drawable.icon_trolley);
            holder.tvItemName.setText(R.string.shop_setting_trolley);
        } else if (goodsId == 6) {
            holder.ivItemIcon.setImageResource(R.drawable.icon_umbrella);
            holder.tvItemName.setText(R.string.shop_setting_umbrella);
        } else if (goodsId == 7) {
            holder.ivItemIcon.setImageResource(R.drawable.icon_towel);
            holder.tvItemName.setText(R.string.shop_setting_towel);
        }
        String s[] = dataList.get(position).getGoodsAttribute().split(Constants.STR_COMMA);
        for (int index = 0; index < s.length; index++) {

            if (index > 0) {
                ImageView ivItemLine = new ImageView(mContext);
                ivItemLine.setBackgroundColor(mContext.getResources().getColor(R.color.common_white));

                holder.llItemContent.addView(ivItemLine);
                LinearLayout.LayoutParams paramsIvItemLine = (LinearLayout.LayoutParams) ivItemLine.getLayoutParams();
                paramsIvItemLine.width = DensityUtil.dip2px(mContext, 1);
                paramsIvItemLine.height = LinearLayout.LayoutParams.MATCH_PARENT;
                ivItemLine.setLayoutParams(paramsIvItemLine);
            }

            IteeTextView tvItemContent = new IteeTextView(mContext);
            tvItemContent.setText(s[index]);
            tvItemContent.setTextColor(mContext.getResources().getColor(R.color.common_black));
            holder.llItemContent.addView(tvItemContent);
            LinearLayout.LayoutParams paramsTvItemContent = (LinearLayout.LayoutParams) tvItemContent.getLayoutParams();
            paramsTvItemContent.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            paramsTvItemContent.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            tvItemContent.setLayoutParams(paramsTvItemContent);
            holder.contentList.add(tvItemContent);

        }

        return view;
    }

    class ViewHolder {

        ImageView ivItemIcon;
        IteeTextView tvItemName;

        LinearLayout llItemContent;

        ArrayList<IteeTextView> contentList = new ArrayList<>();

    }
}
