/**
 * Project Name: itee
 * File Name:	 CustomersTypeListFragment.java
 * Package Name: cn.situne.itee.fragment.customers
 * Date:		 2015-03-24
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.customers;


import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.teetime.TeeTimeSearchProfileFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonMemberGet;
import cn.situne.itee.view.IteeButton;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.StickyLayout;
import cn.situne.itee.view.SwipeLinearLayout;

/**
 * ClassName:CustomersTypeListFragment <br/>
 * Function: show customers type list. <br/>
 * Date: 2015-03-24 <br/>
 * UI:11-1
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class CustomersTypeListFragment extends BaseFragment {

    private ScrollView scrollView;
    private StickyLayout stickyLayout;

    private RelativeLayout rlHeader;

    private RelativeLayout rlTitleMemberContainer;
    private RelativeLayout rlTitleNonmemberContainer;

    private LinearLayout llMemberContainer;
    private LinearLayout llNonmemberContainer;

    private IteeTextView tvMemberTitle;
    private IteeButton btnMemberAdd;
    private IteeTextView tvNonmemberTitle;

    private ArrayList<SwipeLinearLayout> swipeLinearLayoutList;

    private int heightCell;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_customers_type_list;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.customer_edit_title;
    }

    @Override
    protected void initControls(View rootView) {

        heightCell = (int) (getScreenHeight() * 1.0 / 12);

        swipeLinearLayoutList = new ArrayList<>();

        stickyLayout = (StickyLayout) rootView.findViewById(R.id.sticky_layout);
        scrollView = (ScrollView) rootView.findViewById(R.id.sticky_content);

        rlHeader = (RelativeLayout) rootView.findViewById(R.id.sticky_header);

        llMemberContainer = (LinearLayout) rootView.findViewById(R.id.ll_member_container);
        llNonmemberContainer = (LinearLayout) rootView.findViewById(R.id.ll_non_member_container);

        rlTitleMemberContainer = (RelativeLayout) rootView.findViewById(R.id.rl_title_member_container);
        rlTitleNonmemberContainer = (RelativeLayout) rootView.findViewById(R.id.rl_title_non_member_container);

        tvMemberTitle = new IteeTextView(getActivity());
        btnMemberAdd = new IteeButton(getActivity());
        tvNonmemberTitle = new IteeTextView(getActivity());
    }


    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        btnMemberAdd.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlCustomersTypeEdit, getActivity());



                if (hasPermission) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeAdd.value());
                    push(CustomersTypeItemDataFragment.class, bundle);
                } else {
                    AppUtils.showHaveNoPermission(CustomersTypeListFragment.this);
                }

            }
        });

        stickyLayout.setMaxHeaderHeight(100);
        stickyLayout.setOnGiveUpTouchEventListener(new StickyLayout.OnGiveUpTouchEventListener() {
            @Override
            public boolean giveUpTouchEvent(MotionEvent event) {
                return scrollView.getScrollY() < 0.5;
            }
        });

        rlHeader.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                Bundle bundle = new Bundle();

               // bundle.putString("selectDate",currentDate);
                push(TeeTimeSearchProfileFragment.class,bundle);
            }
        });
    }

    @Override
    protected void setLayoutOfControls() {

        LayoutUtils.setLayoutHeight(rlHeader, 100, mContext);

        ViewGroup.LayoutParams rlTitleMemberContainerLayoutParams = rlTitleMemberContainer.getLayoutParams();
        rlTitleMemberContainerLayoutParams.height = heightCell;
        rlTitleMemberContainer.setLayoutParams(rlTitleMemberContainerLayoutParams);

        ViewGroup.LayoutParams rlTitleNonmemberContainerLayoutParams = rlTitleNonmemberContainer.getLayoutParams();
        rlTitleNonmemberContainerLayoutParams.height = heightCell;
        rlTitleNonmemberContainer.setLayoutParams(rlTitleNonmemberContainerLayoutParams);

        AppUtils.addTopSeparatorLine(rlTitleNonmemberContainer, this);
        AppUtils.addBottomSeparatorLine(rlTitleNonmemberContainer, this);

        ViewGroup.LayoutParams llNonmemberContainerLayoutParams = llNonmemberContainer.getLayoutParams();
        llNonmemberContainerLayoutParams.height = heightCell;
        llNonmemberContainer.setLayoutParams(llNonmemberContainerLayoutParams);

        tvMemberTitle.setId(View.generateViewId());
        rlTitleMemberContainer.addView(tvMemberTitle);
        RelativeLayout.LayoutParams tvMemberLayoutParams = (RelativeLayout.LayoutParams) tvMemberTitle.getLayoutParams();
        tvMemberLayoutParams.width = (int) (getScreenWidth() * 0.5);
        tvMemberLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        tvMemberLayoutParams.leftMargin = getActualWidthOnThisDevice(40);
        tvMemberLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvMemberLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        tvMemberTitle.setLayoutParams(tvMemberLayoutParams);

        btnMemberAdd.setId(View.generateViewId());
        rlTitleMemberContainer.addView(btnMemberAdd);
        RelativeLayout.LayoutParams btnMemberAddLayoutParams = (RelativeLayout.LayoutParams) btnMemberAdd.getLayoutParams();
        btnMemberAddLayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        btnMemberAddLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        btnMemberAddLayoutParams.rightMargin = getActualWidthOnThisDevice(40);

        btnMemberAddLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        btnMemberAddLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        btnMemberAdd.setLayoutParams(btnMemberAddLayoutParams);

        tvNonmemberTitle.setId(View.generateViewId());
        rlTitleNonmemberContainer.addView(tvNonmemberTitle);
        RelativeLayout.LayoutParams tvNonMemberLayoutParams = (RelativeLayout.LayoutParams) tvNonmemberTitle.getLayoutParams();
        tvNonMemberLayoutParams.width = (int) (getScreenWidth() * 0.5);
        tvNonMemberLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        tvNonMemberLayoutParams.leftMargin = getActualWidthOnThisDevice(40);
        tvNonMemberLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvNonMemberLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        tvNonmemberTitle.setLayoutParams(tvNonMemberLayoutParams);

    }

    @Override
    protected void setPropertyOfControls() {

        llNonmemberContainer.setBackgroundColor(getColor(R.color.common_light_gray));


        tvMemberTitle.setText(R.string.customers_member);
        tvMemberTitle.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvMemberTitle.setTextColor(getResources().getColor(R.color.common_black));
        tvMemberTitle.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);

        btnMemberAdd.setText(R.string.customers_add);
        btnMemberAdd.setTextSize(Constants.FONT_SIZE_NORMAL);
        btnMemberAdd.setTextColor(getResources().getColor(R.color.common_blue));
        btnMemberAdd.setBackgroundResource(R.drawable.btn_border_history_setting);
        //btnMemberAdd.setBackgroundResource(R.drawable.bg_shops_goods_item_containers);

        tvNonmemberTitle.setText(R.string.customers_non_member);
        tvNonmemberTitle.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvNonmemberTitle.setTextColor(getResources().getColor(R.color.common_black));
        tvNonmemberTitle.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);

    }

    @Override
    protected void configActionBar() {
        setNormalMenuActionBar();
        getTvLeftTitle().setText(getString(R.string.customer_edit_title));
        getTvRight().setVisibility(View.INVISIBLE);
    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        getMemberList();
    }

    private void getMemberList() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        HttpManager<JsonMemberGet> hh = new HttpManager<JsonMemberGet>(CustomersTypeListFragment.this) {

            @Override
            public void onJsonSuccess(JsonMemberGet jo) {
                Integer returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    JsonMemberGet.CustomerList customerList = jo.getDataList().get(0);
                    initData(customerList);
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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.CustomerMemberGet, params);
    }

    private void initData(JsonMemberGet.CustomerList customerList) {
        llNonmemberContainer.removeAllViews();
        llMemberContainer.removeAllViews();
        swipeLinearLayoutList.clear();

        for (int i = 0; i < customerList.getMemberList().size(); i++) {
            JsonMemberGet.CustomerList.MemberListItem memberListItem = customerList.getMemberList().get(i);
            SwipeLinearLayout sllMember = generateSwipeLinearLayout(memberListItem.getMemberTypeId(),
                    memberListItem.getMemberTypeName(), memberListItem.getMemberNum(), llMemberContainer);
            swipeLinearLayoutList.add(sllMember);

            View ivSeparator = AppUtils.getSeparatorLine(this);
            llMemberContainer.addView(ivSeparator);
        }

        SwipeLinearLayout sllNonmember = generateSwipeLinearLayout(customerList.getNonMemberId(),
                customerList.getNonMemberName(), customerList.getNonMemberNum(), llNonmemberContainer);
        swipeLinearLayoutList.add(sllNonmember);
    }

    private SwipeLinearLayout generateSwipeLinearLayout(final int id, final String name, int number, final LinearLayout parent) {

        SwipeLinearLayout.AfterShowRightListener afterShowRightListener = new SwipeLinearLayout.AfterShowRightListener() {
            @Override
            public void doAfterShowRight(SwipeLinearLayout swipeLinearLayout) {
                for (SwipeLinearLayout sll : swipeLinearLayoutList) {
                    if (swipeLinearLayout != sll) {
                        sll.hideRight();
                    }
                }
            }
        };

        int widthRightView = AppUtils.getRightButtonWidth(mContext);
        final SwipeLinearLayout swipeLinearLayout = new SwipeLinearLayout(getBaseActivity(), widthRightView);
        swipeLinearLayout.setPadding(0, 0, 0, 0);
        swipeLinearLayout.setAfterShowRightListener(afterShowRightListener);

        parent.addView(swipeLinearLayout);
        ViewGroup.LayoutParams sllLayoutParams = swipeLinearLayout.getLayoutParams();
        sllLayoutParams.height = heightCell;
        swipeLinearLayout.setLayoutParams(sllLayoutParams);

        RelativeLayout rlLeft = new RelativeLayout(getActivity());
        rlLeft.setBackgroundResource(R.drawable.bg_linear_selector_color_gray);//20160126 modify by jh
        rlLeft.setTag(id);
        swipeLinearLayout.addView(rlLeft);

        ViewGroup.LayoutParams rlLeftLayoutParams = rlLeft.getLayoutParams();
        rlLeftLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        rlLeftLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        rlLeft.setLayoutParams(rlLeftLayoutParams);

        IteeTextView tvMemberTypeName = new IteeTextView(getActivity());
        tvMemberTypeName.setText(name);
        tvMemberTypeName.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvMemberTypeName.setTextColor(getResources().getColor(R.color.common_black));
        tvMemberTypeName.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);

        rlLeft.addView(tvMemberTypeName);
        RelativeLayout.LayoutParams tvMemberTypeNameLayoutParams = (RelativeLayout.LayoutParams) tvMemberTypeName.getLayoutParams();
        tvMemberTypeNameLayoutParams.width = (int) (getScreenWidth()*0.9);
        tvMemberTypeNameLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        tvMemberTypeNameLayoutParams.leftMargin = getActualWidthOnThisDevice(20);
        tvMemberTypeNameLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvMemberTypeNameLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        tvMemberTypeName.setLayoutParams(tvMemberTypeNameLayoutParams);

        ImageView ivARightIcon = new ImageView(getActivity());
        ivARightIcon.setBackgroundResource(R.drawable.icon_right_arrow);
        ivARightIcon.setId(View.generateViewId());

        rlLeft.addView(ivARightIcon);
        RelativeLayout.LayoutParams paramsTvARightIcon = (RelativeLayout.LayoutParams) ivARightIcon.getLayoutParams();
        paramsTvARightIcon.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvARightIcon.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvARightIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvARightIcon.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvARightIcon.rightMargin = getActualWidthOnThisDevice(20);
        ivARightIcon.setLayoutParams(paramsTvARightIcon);

        IteeTextView tvMemberNum = new IteeTextView(getActivity());
        tvMemberNum.setText(String.valueOf(number));
        tvMemberNum.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvMemberNum.setTextColor(getResources().getColor(R.color.common_black));
        tvMemberNum.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);

        rlLeft.addView(tvMemberNum);
        RelativeLayout.LayoutParams tvNonmemberNumLayoutParams = (RelativeLayout.LayoutParams) tvMemberNum.getLayoutParams();
        tvNonmemberNumLayoutParams.width = (int) (getScreenWidth() * 0.4);
        tvNonmemberNumLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        tvNonmemberNumLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvNonmemberNumLayoutParams.addRule(RelativeLayout.LEFT_OF, ivARightIcon.getId());
        tvMemberNum.setLayoutParams(tvNonmemberNumLayoutParams);

        rlLeft.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(TransKey.CUSTOMERS_MEMBER_TYPE_ID, id);
                bundle.putString(TransKey.CUSTOMERS_MEMBER_TYPE_NAME, name);
                if (parent == llNonmemberContainer) {
                    bundle.putString(TransKey.CUSTOMERS_MEMBER_TYPE_TYPE_ID, Constants.CUSTOMER_NON_MEMBER);
                } else {
                    bundle.putString(TransKey.CUSTOMERS_MEMBER_TYPE_TYPE_ID, Constants.CUSTOMER_MEMBER);
                }
                push(CustomersMemberListFragment.class, bundle);
            }
        });

        RelativeLayout rlRight = new RelativeLayout(getActivity());
        rlRight.setBackgroundResource(R.drawable.bg_common_edit);
        swipeLinearLayout.addView(rlRight);

        ViewGroup.LayoutParams rlRightLayoutParams = rlRight.getLayoutParams();
        rlRightLayoutParams.width = widthRightView;
        rlRightLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        rlRight.setLayoutParams(rlRightLayoutParams);

        IteeTextView tvEdit = new IteeTextView(getActivity());
        tvEdit.setBackgroundResource(R.drawable.bg_common_edit);
        tvEdit.setText(R.string.common_edit);
        tvEdit.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvEdit.setGravity(Gravity.CENTER);
        tvEdit.setTextColor(getColor(R.color.common_white));
        tvEdit.setTag(id);
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeLinearLayout.hideRight();

                Bundle bundle = new Bundle();
                bundle.putInt(TransKey.CUSTOMERS_MEMBER_TYPE_ID, id);
                bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeBrowse.value());
                if (parent == llNonmemberContainer) {
                    bundle.putString(TransKey.CUSTOMERS_MEMBER_TYPE_TYPE_ID, Constants.CUSTOMER_NON_MEMBER);
                } else {
                    bundle.putString(TransKey.CUSTOMERS_MEMBER_TYPE_TYPE_ID, Constants.CUSTOMER_MEMBER);
                }
                push(CustomersTypeItemDataFragment.class, bundle);
            }
        });
        rlRight.addView(tvEdit);

        ViewGroup.LayoutParams tvEditLayoutParams = tvEdit.getLayoutParams();
        tvEditLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        tvEditLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        tvEdit.setLayoutParams(tvEditLayoutParams);

        return swipeLinearLayout;
    }
}