/**
 * Project Name: itee
 * File Name:	 JsonUserDetailGet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-03-19
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.adapter;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.jsonentity.JsonUserListGet;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:StaffDepartmentDetailAdapter <br/>
 * Function: adapter of department user list <br/>
 * Date: 2015-03-19 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class StaffDepartmentDetailAdapter extends BaseAdapter {

    private static final String LEVEL_MANAGER = "M";

    private BaseFragment mBaseFragment;
    private ArrayList<JsonUserListGet.DepartmentUserInfo> userList;

    public StaffDepartmentDetailAdapter(BaseFragment mBaseFragment, ArrayList<JsonUserListGet.DepartmentUserInfo> userList) {
        this.mBaseFragment = mBaseFragment;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        if (userList != null) {
            return userList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(mBaseFragment.getActivity()).inflate(R.layout.item_of_fragment_finance_department, null);

            holder.rlContainer = (RelativeLayout) convertView.findViewById(R.id.rl_item_department_container);
            holder.ivPhoto = (NetworkImageView) convertView.findViewById(R.id.icon_user);
            holder.tvName = (IteeTextView) convertView.findViewById(R.id.tv_name);
            holder.tvTel = (IteeTextView) convertView.findViewById(R.id.tv_tel);
            holder.tvEmail = (IteeTextView) convertView.findViewById(R.id.tv_mail);

            holder.tvTel.setTextSize(Constants.FONT_SIZE_SMALLER);
            holder.tvEmail.setTextSize(Constants.FONT_SIZE_SMALLER);

            holder.tvEmail.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);

            LinearLayout.LayoutParams paramsRlContainer = (LinearLayout.LayoutParams) holder.rlContainer.getLayoutParams();
            paramsRlContainer.height = mBaseFragment.getActualHeightOnThisDevice(160);
            holder.rlContainer.setLayoutParams(paramsRlContainer);

            RelativeLayout.LayoutParams paramsIvPhoto = (RelativeLayout.LayoutParams) holder.ivPhoto.getLayoutParams();
            paramsIvPhoto.height = mBaseFragment.getActualHeightOnThisDevice(116);
            paramsIvPhoto.width = mBaseFragment.getActualWidthOnThisDevice(116);
            paramsIvPhoto.leftMargin = mBaseFragment.getActualWidthOnThisDevice(20);
            paramsIvPhoto.topMargin = mBaseFragment.getActualHeightOnThisDevice(20);
            paramsIvPhoto.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            holder.ivPhoto.setLayoutParams(paramsIvPhoto);

            RelativeLayout.LayoutParams paramsTvName = (RelativeLayout.LayoutParams) holder.tvName.getLayoutParams();
            paramsTvName.height =ViewGroup.LayoutParams.WRAP_CONTENT ;
            paramsTvName.width = mBaseFragment.getActualHeightOnThisDevice(370);
            paramsTvName.leftMargin = mBaseFragment.getActualWidthOnThisDevice(150);
            paramsTvName.topMargin = mBaseFragment.getActualHeightOnThisDevice(25);
            paramsTvName.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            holder.tvName.setLayoutParams(paramsTvName);
            holder.tvName.setEllipsize(TextUtils.TruncateAt.END);

            RelativeLayout.LayoutParams paramsTvEmail = (RelativeLayout.LayoutParams) holder.tvEmail.getLayoutParams();
            paramsTvEmail.width = (int) (DensityUtil.getScreenWidth(mBaseFragment.getActivity()) * 0.7);
            paramsTvEmail.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            paramsTvEmail.topMargin = mBaseFragment.getActualHeightOnThisDevice(95);
            paramsTvEmail.rightMargin = mBaseFragment.getActualWidthOnThisDevice(20);
            paramsTvEmail.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            holder.tvEmail.setLayoutParams(paramsTvEmail);

            RelativeLayout.LayoutParams paramsTvMobile = (RelativeLayout.LayoutParams) holder.tvTel.getLayoutParams();
            paramsTvMobile.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            paramsTvMobile.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            paramsTvMobile.topMargin = mBaseFragment.getActualHeightOnThisDevice(35);
            paramsTvMobile.rightMargin = mBaseFragment.getActualHeightOnThisDevice(20);
            paramsTvMobile.leftMargin = mBaseFragment.getActualWidthOnThisDevice(150);
            paramsTvMobile.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            holder.tvTel.setLayoutParams(paramsTvMobile);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JsonUserListGet.DepartmentUserInfo departmentUserInfo = userList.get(position);

        if (Utils.isStringNotNullOrEmpty(departmentUserInfo.getUserPhoto())) {
            AppUtils.showNetworkImage(holder.ivPhoto, departmentUserInfo.getUserPhoto());
        } else {
            AppUtils.showNetworkImage(holder.ivPhoto, Constants.PHOTO_DEFAULT_URL);
        }

        holder.tvTel.setText(departmentUserInfo.getUserMobile());
        holder.tvEmail.setText(departmentUserInfo.getUserEmail());

        String totalName = departmentUserInfo.getUserName();
        if (LEVEL_MANAGER.equals(departmentUserInfo.getUserType())) {
            totalName += Constants.STR_SPACE + Constants.STR_BRACKETS_START + mBaseFragment.getResources().getString(R.string.staff_manager) + Constants.STR_BRACKETS_END;
        }

        SpannableString ss = new SpannableString(totalName);
        ss.setSpan(new ForegroundColorSpan(Color.BLUE), 0, departmentUserInfo.getUserName().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(20, true), 0, departmentUserInfo.getUserName().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.BLACK), departmentUserInfo.getUserName().length(), totalName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(16, true), departmentUserInfo.getUserName().length(), totalName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        holder.tvName.setText(ss);


        return convertView;
    }

    public class ViewHolder {
        RelativeLayout rlContainer;
        NetworkImageView ivPhoto;
        IteeTextView tvName;
        IteeTextView tvTel;
        IteeTextView tvEmail;
    }
}
