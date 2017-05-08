/**
 * Project Name: itee
 * File Name:	 AuthorityFragment.java
 * Package Name: cn.situne.itee.fragment.staff
 * Date:		 2015-03-20
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */
package cn.situne.itee.fragment.staff;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonStaffAuthorityGet;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:AuthorityFragment <br/>
 * Function: show list of authority <br/>
 * Date: 2015-03-20 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class AuthorityFragment extends BaseFragment {

    private static final int STATUS_AVAILABLE = 1;

    private RelativeLayout rlTeeTime;
    private RelativeLayout rlEvents;
    private RelativeLayout rlShops;
    private RelativeLayout rlCustomers;
    private RelativeLayout rlAgents;
    private RelativeLayout rlStaff;
    private RelativeLayout rlNews;
    private RelativeLayout rlDiscount;

    private IteeTextView tvTeeTime;
    private ImageView ivTeeTimeRightIcon;
    private IteeTextView tvEvents;
    private ImageView ivEventsRightIcon;
    private IteeTextView tvShops;
    private ImageView ivShopsRightIcon;
    private IteeTextView tvCustomers;
    private ImageView ivCustomersRightIcon;
    private IteeTextView tvAgents;
    private ImageView ivAgentsRightIcon;
    private IteeTextView tvStaff;
    private ImageView ivStaffFRightIcon;
    private IteeTextView tvNews;
    private ImageView ivNewsRightIcon;
    private IteeTextView tvDiscount;
    private ImageView ivDiscountRightIcon;

    private int departmentId;
    private int courseId;
    private int passedUserId;

    @Override
    protected int getFragmentId() {
        Log.d("AuthorityFragment", "this need page");
        return R.layout.fragment_staff_authority;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        rlTeeTime = (RelativeLayout) rootView.findViewById(R.id.rl_time);
        rlEvents = (RelativeLayout) rootView.findViewById(R.id.rl_event);
        rlShops = (RelativeLayout) rootView.findViewById(R.id.rl_shop);
        rlCustomers = (RelativeLayout) rootView.findViewById(R.id.rl_customers);
        rlAgents = (RelativeLayout) rootView.findViewById(R.id.rl_agents);
        rlStaff = (RelativeLayout) rootView.findViewById(R.id.rl_staff);
        rlNews = (RelativeLayout) rootView.findViewById(R.id.rl_news);
        rlDiscount = (RelativeLayout) rootView.findViewById(R.id.rl_discount);

        tvTeeTime = new IteeTextView(getActivity());
        ivTeeTimeRightIcon = new ImageView(getActivity());
        tvEvents = new IteeTextView(getActivity());
        ivEventsRightIcon = new ImageView(getActivity());
        tvShops = new IteeTextView(getActivity());
        ivShopsRightIcon = new ImageView(getActivity());
        tvCustomers = new IteeTextView(getActivity());
        ivCustomersRightIcon = new ImageView(getActivity());
        tvAgents = new IteeTextView(getActivity());
        ivAgentsRightIcon = new ImageView(getActivity());
        tvStaff = new IteeTextView(getActivity());
        ivStaffFRightIcon = new ImageView(getActivity());
        tvNews = new IteeTextView(getActivity());
        ivNewsRightIcon = new ImageView(getActivity());
        tvDiscount = new IteeTextView(getActivity());
        ivDiscountRightIcon = new ImageView(getActivity());


    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        getAuthorityDataList();
    }

    @Override
    protected void setDefaultValueOfControls() {

        LinearLayout.LayoutParams paramTime = (LinearLayout.LayoutParams) rlTeeTime.getLayoutParams();
        paramTime.height = getActualHeightOnThisDevice(Constants.ROW_HEIGHT);
        rlTeeTime.setLayoutParams(paramTime);

        rlTeeTime.addView(tvTeeTime);
        RelativeLayout.LayoutParams paramTvTime = (RelativeLayout.LayoutParams) tvTeeTime.getLayoutParams();
        paramTvTime.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvTime.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvTime.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvTime.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvTime.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        tvTeeTime.setLayoutParams(paramTvTime);
        rlTeeTime.setBackgroundResource(R.drawable.bg_linear_selector_color_white);

        rlTeeTime.addView(ivTeeTimeRightIcon);
        RelativeLayout.LayoutParams paramsIvTRightIcon = (RelativeLayout.LayoutParams) ivTeeTimeRightIcon.getLayoutParams();
        paramsIvTRightIcon.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvTRightIcon.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvTRightIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIvTRightIcon.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsIvTRightIcon.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        ivTeeTimeRightIcon.setLayoutParams(paramsIvTRightIcon);

        LinearLayout.LayoutParams paramEvent = (LinearLayout.LayoutParams) rlEvents.getLayoutParams();
        paramEvent.height = getActualHeightOnThisDevice(Constants.ROW_HEIGHT);
        rlEvents.setLayoutParams(paramTime);
        rlEvents.setBackgroundResource(R.drawable.bg_linear_selector_color_white);

        rlEvents.addView(tvEvents);
        RelativeLayout.LayoutParams paramTvEvent = (RelativeLayout.LayoutParams) tvEvents.getLayoutParams();
        paramTvEvent.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvEvent.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvEvent.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvEvent.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvEvent.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        tvEvents.setLayoutParams(paramTvEvent);

        rlEvents.addView(ivEventsRightIcon);
        RelativeLayout.LayoutParams paramsIvERightIcon = (RelativeLayout.LayoutParams) ivEventsRightIcon.getLayoutParams();
        paramsIvERightIcon.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvERightIcon.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvERightIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIvERightIcon.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsIvERightIcon.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        ivEventsRightIcon.setLayoutParams(paramsIvERightIcon);

        LinearLayout.LayoutParams paramShop = (LinearLayout.LayoutParams) rlShops.getLayoutParams();
        paramShop.height = getActualHeightOnThisDevice(Constants.ROW_HEIGHT);
        rlShops.setLayoutParams(paramShop);
        rlShops.setBackgroundResource(R.drawable.bg_linear_selector_color_white);

        rlShops.addView(tvShops);
        RelativeLayout.LayoutParams paramTvShop = (RelativeLayout.LayoutParams) tvShops.getLayoutParams();
        paramTvShop.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvShop.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvShop.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvShop.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvShop.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        tvShops.setLayoutParams(paramTvShop);

        rlShops.addView(ivShopsRightIcon);
        RelativeLayout.LayoutParams paramsIvSRightIcon = (RelativeLayout.LayoutParams) ivShopsRightIcon.getLayoutParams();
        paramsIvSRightIcon.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvSRightIcon.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvSRightIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIvSRightIcon.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsIvSRightIcon.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        ivShopsRightIcon.setLayoutParams(paramsIvSRightIcon);

        LinearLayout.LayoutParams paramCustomer = (LinearLayout.LayoutParams) rlCustomers.getLayoutParams();
        paramCustomer.height = getActualHeightOnThisDevice(Constants.ROW_HEIGHT);
        rlCustomers.setLayoutParams(paramCustomer);
        rlCustomers.setBackgroundResource(R.drawable.bg_linear_selector_color_white);

        rlCustomers.addView(tvCustomers);
        RelativeLayout.LayoutParams paramTvCustomer = (RelativeLayout.LayoutParams) tvCustomers.getLayoutParams();
        paramTvCustomer.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvCustomer.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvCustomer.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvCustomer.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvCustomer.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        tvCustomers.setLayoutParams(paramTvCustomer);
        rlCustomers.setBackgroundResource(R.drawable.bg_linear_selector_color_white);

        rlCustomers.addView(ivCustomersRightIcon);
        RelativeLayout.LayoutParams paramsIvCRightIcon = (RelativeLayout.LayoutParams) ivCustomersRightIcon.getLayoutParams();
        paramsIvCRightIcon.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvCRightIcon.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvCRightIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIvCRightIcon.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsIvCRightIcon.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        ivCustomersRightIcon.setLayoutParams(paramsIvCRightIcon);

        LinearLayout.LayoutParams paramAgent = (LinearLayout.LayoutParams) rlAgents.getLayoutParams();
        paramAgent.height = getActualHeightOnThisDevice(Constants.ROW_HEIGHT);
        rlAgents.setLayoutParams(paramAgent);
        rlAgents.setBackgroundResource(R.drawable.bg_linear_selector_color_white);

        rlAgents.addView(tvAgents);
        RelativeLayout.LayoutParams paramTvAgent = (RelativeLayout.LayoutParams) tvAgents.getLayoutParams();
        paramTvAgent.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvAgent.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvAgent.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvAgent.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvAgent.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        tvAgents.setLayoutParams(paramTvAgent);

        rlAgents.addView(ivAgentsRightIcon);
        RelativeLayout.LayoutParams paramsIvARightIcon = (RelativeLayout.LayoutParams) ivAgentsRightIcon.getLayoutParams();
        paramsIvARightIcon.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvARightIcon.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvARightIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIvARightIcon.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsIvARightIcon.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        ivAgentsRightIcon.setLayoutParams(paramsIvARightIcon);

        LinearLayout.LayoutParams paramStaff = (LinearLayout.LayoutParams) rlStaff.getLayoutParams();
        paramStaff.height = getActualHeightOnThisDevice(Constants.ROW_HEIGHT);
        rlStaff.setLayoutParams(paramStaff);
        rlStaff.setBackgroundResource(R.drawable.bg_linear_selector_color_white);

        rlStaff.addView(tvStaff);
        RelativeLayout.LayoutParams paramTvStaff = (RelativeLayout.LayoutParams) tvStaff.getLayoutParams();
        paramTvStaff.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvStaff.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvStaff.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvStaff.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvStaff.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        tvStaff.setLayoutParams(paramTvStaff);

        rlStaff.addView(ivStaffFRightIcon);
        RelativeLayout.LayoutParams paramsIvSFRightIcon = (RelativeLayout.LayoutParams) ivStaffFRightIcon.getLayoutParams();
        paramsIvSFRightIcon.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvSFRightIcon.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvSFRightIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIvSFRightIcon.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsIvSFRightIcon.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        ivStaffFRightIcon.setLayoutParams(paramsIvSFRightIcon);

        LinearLayout.LayoutParams paramNew = (LinearLayout.LayoutParams) rlNews.getLayoutParams();
        paramNew.height = getActualHeightOnThisDevice(Constants.ROW_HEIGHT);
        rlNews.setLayoutParams(paramNew);
        rlNews.setBackgroundResource(R.drawable.bg_linear_selector_color_white);

        rlNews.addView(tvNews);
        RelativeLayout.LayoutParams paramTvNew = (RelativeLayout.LayoutParams) tvNews.getLayoutParams();
        paramTvNew.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvNew.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvNew.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvNew.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvNew.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        tvNews.setLayoutParams(paramTvNew);

        rlNews.addView(ivNewsRightIcon);
        RelativeLayout.LayoutParams paramsIvNRightIcon = (RelativeLayout.LayoutParams) ivNewsRightIcon.getLayoutParams();
        paramsIvNRightIcon.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvNRightIcon.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvNRightIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIvNRightIcon.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsIvNRightIcon.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        ivNewsRightIcon.setLayoutParams(paramsIvNRightIcon);

        LinearLayout.LayoutParams paramDiscount = (LinearLayout.LayoutParams) rlDiscount.getLayoutParams();
        paramDiscount.height = getActualHeightOnThisDevice(Constants.ROW_HEIGHT);
        rlDiscount.setLayoutParams(paramDiscount);
        rlDiscount.setBackgroundResource(R.drawable.bg_linear_selector_color_white);

        rlDiscount.addView(tvDiscount);
        RelativeLayout.LayoutParams paramTvDiscount = (RelativeLayout.LayoutParams) tvDiscount.getLayoutParams();
        paramTvDiscount.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvDiscount.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvDiscount.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvDiscount.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvDiscount.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        tvDiscount.setLayoutParams(paramTvDiscount);

        rlDiscount.addView(ivDiscountRightIcon);
        RelativeLayout.LayoutParams paramsIvDRightIcon = (RelativeLayout.LayoutParams) ivDiscountRightIcon.getLayoutParams();
        paramsIvDRightIcon.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvDRightIcon.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvDRightIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIvDRightIcon.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsIvDRightIcon.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        ivDiscountRightIcon.setLayoutParams(paramsIvDRightIcon);
        hideView();
    }


    @Override
    protected void reShowWithBackValue() {
        Bundle bundle = getReturnValues();
        if (bundle != null && bundle.getBoolean(TransKey.COMMON_REFRESH)) {
            hideView();
            getAuthorityDataList();
        }
    }

    @Override
    protected void setListenersOfControls() {

        rlTeeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonStaffAuthorityGet.DataItem dataItem = dataList.get(0);
                if (dataItem.getAuStatus() == STATUS_AVAILABLE) {
                    Bundle bundle = new Bundle();
                    bundle.putString(TransKey.STAFF_AU_SON, dataItem.getAuSon().toString());
                    bundle.putString(TransKey.STAFF_AU_ID, String.valueOf(view.getTag()));
                    bundle.putInt(TransKey.STAFF_DEPARTMENT_ID, departmentId);
                    bundle.putInt(TransKey.STAFF_COURSE_ID, courseId);
                    bundle.putInt(TransKey.STAFF_PASSED_USER_ID, passedUserId);
                    push(TeeTimesAuthorityFragment.class, bundle);
                }
            }
        });
        rlEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonStaffAuthorityGet.DataItem dataItem = dataList.get(1);
                if (dataItem.getAuStatus() == STATUS_AVAILABLE) {
                    Bundle bundle = new Bundle();
                    bundle.putString(TransKey.STAFF_AU_SON, dataItem.getAuSon().toString());
                    bundle.putString(TransKey.STAFF_AU_ID, String.valueOf(view.getTag()));
                    bundle.putInt(TransKey.STAFF_DEPARTMENT_ID, departmentId);
                    bundle.putInt(TransKey.STAFF_COURSE_ID, courseId);
                    bundle.putInt(TransKey.STAFF_PASSED_USER_ID, passedUserId);
                    push(EventsAuthorityFragment.class, bundle);
                }
            }
        });
        rlShops.setOnClickListener(new View.OnClickListener() {//TOTOOTOTSHOP  TODO LC
            @Override
            public void onClick(View view) {
                JsonStaffAuthorityGet.DataItem dataItem = dataList.get(2);
                if (dataItem.getAuStatus() == STATUS_AVAILABLE) {
                    Bundle bundle = new Bundle();
                    bundle.putString(TransKey.STAFF_AU_SON, dataItem.getAuSon().toString());
                    bundle.putString(TransKey.STAFF_AU_ID, String.valueOf(view.getTag()));
                    bundle.putInt(TransKey.STAFF_DEPARTMENT_ID, departmentId);
                    bundle.putInt(TransKey.STAFF_COURSE_ID, courseId);
                    bundle.putInt(TransKey.STAFF_PASSED_USER_ID, passedUserId);
                    push(ShopsAuthorityFragment.class, bundle);
                }
            }
        });
        rlCustomers.setOnClickListener(new View.OnClickListener() {//TOTOTOTO TODO LC
            @Override
            public void onClick(View view) {
                JsonStaffAuthorityGet.DataItem dataItem = dataList.get(3);
                if (dataItem.getAuStatus() == STATUS_AVAILABLE) {
                    Bundle bundle = new Bundle();
                    bundle.putString(TransKey.STAFF_AU_SON, dataItem.getAuSon().toString());
                    bundle.putString(TransKey.STAFF_AU_ID, String.valueOf(view.getTag()));
                    bundle.putInt(TransKey.STAFF_DEPARTMENT_ID, departmentId);
                    bundle.putInt(TransKey.STAFF_COURSE_ID, courseId);
                    bundle.putInt(TransKey.STAFF_PASSED_USER_ID, passedUserId);
                    push(CustomersAuthorityFragment.class, bundle);
                }
            }
        });
        rlAgents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonStaffAuthorityGet.DataItem dataItem = dataList.get(4);
                if (dataItem.getAuStatus() == STATUS_AVAILABLE) {
                    Bundle bundle = new Bundle();
                    bundle.putString(TransKey.STAFF_AU_SON, dataItem.getAuSon().toString());
                    bundle.putString(TransKey.STAFF_AU_ID, String.valueOf(view.getTag()));
                    bundle.putInt(TransKey.STAFF_DEPARTMENT_ID, departmentId);
                    bundle.putInt(TransKey.STAFF_COURSE_ID, courseId);
                    bundle.putInt(TransKey.STAFF_PASSED_USER_ID, passedUserId);
                    push(AgentsAuthorityFragment.class, bundle);
                }
            }
        });
        rlStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonStaffAuthorityGet.DataItem dataItem = dataList.get(5);
                if (dataItem.getAuStatus() == STATUS_AVAILABLE) {
                    Bundle bundle = new Bundle();
                    bundle.putString(TransKey.STAFF_AU_SON, dataItem.getAuSon().toString());
                    bundle.putString(TransKey.STAFF_AU_ID, String.valueOf(view.getTag()));
                    bundle.putInt(TransKey.STAFF_DEPARTMENT_ID, departmentId);
                    bundle.putInt(TransKey.STAFF_COURSE_ID, courseId);
                    bundle.putInt(TransKey.STAFF_PASSED_USER_ID, passedUserId);
                    push(StaffAuthorityFragment.class, bundle);
                }
            }
        });
        rlNews.setOnClickListener(new View.OnClickListener() {//TODO LC  NEW API
            @Override
            public void onClick(View view) {//TODO LC
//                JsonStaffAuthorityGet.DataItem dataItem = dataList.get(6);
//                if (dataItem.getAuStatus() == STATUS_AVAILABLE) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString(TransKey.STAFF_AU_SON, dataItem.getAuSon().toString());
//                    bundle.putString(TransKey.STAFF_AU_ID, String.valueOf(view.getTag()));
//                    bundle.putInt(TransKey.STAFF_DEPARTMENT_ID, departmentId);
//
//   bundle.putInt(TransKey.STAFF_PASSED_USER_ID, passedUserId);
//                    push(NewsAuthorityFragment.class, bundle);
//                }
            }
        });
        rlDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonStaffAuthorityGet.DataItem dataItem = dataList.get(7);
                if (dataItem.getAuStatus() == STATUS_AVAILABLE) {
                    Bundle bundle = new Bundle();
                    bundle.putString(TransKey.STAFF_AU_SON, dataItem.getAuSon().toString());
                    bundle.putString(TransKey.STAFF_AU_ID, String.valueOf(view.getTag()));
                    bundle.putInt(TransKey.STAFF_DEPARTMENT_ID, departmentId);
                    bundle.putInt(TransKey.STAFF_COURSE_ID, courseId);
                    bundle.putInt(TransKey.STAFF_PASSED_USER_ID, passedUserId);
                    push(DiscountAuthorityFragment.class, bundle);
                }
            }
        });
    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {

        tvTeeTime.setText(R.string.staff_tee_times);
        tvTeeTime.setTextColor(getColor(R.color.common_black));

        ivTeeTimeRightIcon.setBackgroundResource(R.drawable.icon_right_arrow);

        tvEvents.setText(R.string.staff_events);
        tvEvents.setTextColor(getColor(R.color.common_black));

        ivEventsRightIcon.setBackgroundResource(R.drawable.icon_right_arrow);

        tvShops.setText(R.string.staff_shops);
        tvShops.setTextColor(getColor(R.color.common_black));

        ivShopsRightIcon.setBackgroundResource(R.drawable.icon_right_arrow);

        tvCustomers.setText(R.string.staff_customers);
        tvCustomers.setTextColor(getColor(R.color.common_black));

        ivCustomersRightIcon.setBackgroundResource(R.drawable.icon_right_arrow);

        tvAgents.setText(R.string.staff_agents);
        tvAgents.setTextColor(getColor(R.color.common_black));

        ivAgentsRightIcon.setBackgroundResource(R.drawable.icon_right_arrow);

        tvStaff.setText(R.string.staff_staff);
        tvStaff.setTextColor(getColor(R.color.common_black));

        ivStaffFRightIcon.setBackgroundResource(R.drawable.icon_right_arrow);

        tvNews.setText(R.string.staff_news);
        tvNews.setTextColor(getColor(R.color.common_black));

        ivNewsRightIcon.setBackgroundResource(R.drawable.icon_right_arrow);

        tvDiscount.setText(R.string.staff_discount);
        tvDiscount.setTextColor(getColor(R.color.common_black));

        ivDiscountRightIcon.setBackgroundResource(R.drawable.icon_right_arrow);
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(R.string.staff_authority);
    }

    private List<JsonStaffAuthorityGet.DataItem> dataList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            departmentId = bundle.getInt(TransKey.STAFF_DEPARTMENT_ID);
            courseId = bundle.getInt(TransKey.STAFF_COURSE_ID);
            passedUserId = bundle.getInt(TransKey.STAFF_PASSED_USER_ID);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void getAuthorityDataList() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        if (passedUserId != 0) {
            params.put(ApiKey.STAFF_USER_ID, String.valueOf(passedUserId));
        } else {
            params.put(ApiKey.STAFF_DEPARTMENT_ID, String.valueOf(departmentId));
        }
        HttpManager<JsonStaffAuthorityGet> hh = new HttpManager<JsonStaffAuthorityGet>(AuthorityFragment.this) {

            @Override
            public void onJsonSuccess(JsonStaffAuthorityGet jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    dataList = jo.getDataList();
                    showView();
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(getActivity(), R.string.msg_common_network_error);
            }
        };
        hh.startGet(this.getActivity(), ApiManager.HttpApi.StaffAutorityGet, params);
    }


    private void showView() {

        if (dataList.size() >= 8) {
            rlTeeTime.setVisibility(View.VISIBLE);
            rlTeeTime.setTag(dataList.get(0).getAuId());
            rlEvents.setVisibility(View.VISIBLE);
            rlEvents.setTag(dataList.get(1).getAuId());
            rlShops.setVisibility(View.VISIBLE);
            rlShops.setTag(dataList.get(2).getAuId());
            rlCustomers.setVisibility(View.VISIBLE);
            rlCustomers.setTag(dataList.get(3).getAuId());
            rlAgents.setVisibility(View.VISIBLE);
            rlAgents.setTag(dataList.get(4).getAuId());
            rlStaff.setVisibility(View.VISIBLE);
            rlStaff.setTag(dataList.get(5).getAuId());
            rlNews.setVisibility(View.VISIBLE);
            rlNews.setTag(dataList.get(6).getAuId());
            rlDiscount.setVisibility(View.VISIBLE);
            rlDiscount.setTag(dataList.get(7).getAuId());
        }
    }

    private void hideView() {
        rlTeeTime.setVisibility(View.GONE);
        rlEvents.setVisibility(View.GONE);
        rlShops.setVisibility(View.GONE);
        rlCustomers.setVisibility(View.GONE);
        rlAgents.setVisibility(View.GONE);
        rlStaff.setVisibility(View.GONE);
        rlNews.setVisibility(View.GONE);
        rlDiscount.setVisibility(View.GONE);
    }
}
