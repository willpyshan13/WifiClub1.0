/**
 * Project Name: itee
 * File Name:	 CustomersAuthorityFragment.java
 * Package Name: cn.situne.itee.fragment.staff
 * Date:		 2015-02-28
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */
package cn.situne.itee.fragment.staff;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonStaffAuthorityGet;
import cn.situne.itee.view.CheckSwitchButton;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:CustomersAuthorityFragment <br/>
 * Function: set authority of customers <br/>
 * Date: 2015-02-28 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class CustomersAuthorityFragment extends BaseFragment {

    private List<JsonStaffAuthorityGet.DataItem> dataList;
    private RelativeLayout rlCustomerTypeEditContainer;
    private RelativeLayout rlProfileEditContainer;

    private LinearLayout llProfileSon;

    private List<CheckSwitchButton> profileSonCheckButtons;


    private RelativeLayout rlRechargeContainer;
    private RelativeLayout rlMembershipManagementContainer;
    private RelativeLayout rlRefundContainer;
    private String auId;

    private int departmentId;
    private int passedUserId;

    private IteeTextView tvCustomerTypeEdit;
    private IteeTextView tvProfileEdit;
    private IteeTextView tvMember;
    private IteeTextView tvNonMember;
    private IteeTextView tvRecharge;
    private IteeTextView tvMembershipManagement;
    private IteeTextView tvRefund;


    private CheckSwitchButton swCustomerTypeEdit;
    private CheckSwitchButton swProfileEdit;

    private CheckSwitchButton swRecharge;
    private CheckSwitchButton swMembershipManagement;
    private CheckSwitchButton swRefund;

    private AppUtils.NoDoubleClickListener noDoubleClickListener;


    @Override
    protected int getFragmentId() {
        return R.layout.fragment_staff_authority_customers;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            departmentId = bundle.getInt(TransKey.STAFF_DEPARTMENT_ID);
            passedUserId = bundle.getInt(TransKey.STAFF_PASSED_USER_ID);
            auId = bundle.getString(TransKey.STAFF_AU_ID);
            String dataStr = bundle.getString(TransKey.STAFF_AU_SON);

            JsonStaffAuthorityGet jsonData = new JsonStaffAuthorityGet(null);
            try {
                jsonData.setJsonValuesForArray(new JSONArray(dataStr));
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
            dataList = jsonData.getDataList();

        }

        rlCustomerTypeEditContainer = (RelativeLayout) rootView.findViewById(R.id.rl_customer_type_edit_container);
        rlProfileEditContainer = (RelativeLayout) rootView.findViewById(R.id.rl_profile_edit_container);
        llProfileSon = (LinearLayout) rootView.findViewById(R.id.ll_profile_son);

        rlRechargeContainer = (RelativeLayout) rootView.findViewById(R.id.rl_recharge_container);
        rlMembershipManagementContainer = (RelativeLayout) rootView.findViewById(R.id.rl_membership_management_container);
        rlRefundContainer = (RelativeLayout) rootView.findViewById(R.id.rl_refund_container);

        tvCustomerTypeEdit = new IteeTextView(getActivity());
        tvProfileEdit = new IteeTextView(getActivity());
        tvMember = new IteeTextView(getActivity());
        tvNonMember = new IteeTextView(getActivity());
        tvRecharge = new IteeTextView(getActivity());
        tvMembershipManagement = new IteeTextView(getActivity());
        tvRefund = new IteeTextView(getActivity());

        swCustomerTypeEdit = new CheckSwitchButton(this);
        swProfileEdit = new CheckSwitchButton(this);
        swRecharge = new CheckSwitchButton(this);
        swMembershipManagement = new CheckSwitchButton(this);
        swRefund = new CheckSwitchButton(this);
    }

    @Override
    protected void setDefaultValueOfControls() {

        tvCustomerTypeEdit.setText(R.string.staff_customers_type_edit);
        tvCustomerTypeEdit.setTextColor(getResources().getColor(R.color.common_black));

        tvProfileEdit.setText(R.string.staff_profile_edit);
        tvProfileEdit.setTextColor(getResources().getColor(R.color.common_black));

        tvMember.setText(R.string.staff_member);
        tvMember.setTextColor(getResources().getColor(R.color.common_black));

        tvNonMember.setText(R.string.staff_non_member);
        tvNonMember.setTextColor(getResources().getColor(R.color.common_black));

        tvRecharge.setText(R.string.staff_recharge);
        tvRecharge.setTextColor(getResources().getColor(R.color.common_black));

        tvMembershipManagement.setText(R.string.staff_membership_management);
        tvMembershipManagement.setTextColor(getResources().getColor(R.color.common_black));

        tvRefund.setText(R.string.staff_refund);
        tvRefund.setTextColor(getResources().getColor(R.color.common_black));


        swCustomerTypeEdit.setTag(dataList.get(0).getAuId());
        if (dataList.get(0).getAuStatus() == Constants.SWITCH_RIGHT) {
            swCustomerTypeEdit.setChecked(true);
        } else {
            swCustomerTypeEdit.setChecked(false);
        }

        swProfileEdit.setTag(dataList.get(1).getAuId());
        if (dataList.get(1).getAuStatus() == Constants.SWITCH_RIGHT) {
            llProfileSon.setVisibility(View.VISIBLE);
            swProfileEdit.setChecked(true);
        } else {
            llProfileSon.setVisibility(View.GONE);
            swProfileEdit.setChecked(false);
        }

        addMemberSon(dataList.get(1).getAuSon());

        swRecharge.setTag(dataList.get(2).getAuId());
        if (dataList.get(2).getAuStatus() == Constants.SWITCH_RIGHT) {
            swRecharge.setChecked(true);
        } else {
            swRecharge.setChecked(false);
        }

        swMembershipManagement.setTag(dataList.get(3).getAuId());
        if (dataList.get(3).getAuStatus() == Constants.SWITCH_RIGHT) {
            swMembershipManagement.setChecked(true);
        } else {
            swMembershipManagement.setChecked(false);
        }
        swRefund.setTag(dataList.get(4).getAuId());
        if (dataList.get(4).getAuStatus() == Constants.SWITCH_RIGHT) {
            swRefund.setChecked(true);
        } else {
            swRefund.setChecked(false);
        }
    }

    @Override
    protected void setListenersOfControls() {
        swProfileEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    llProfileSon.setVisibility(View.VISIBLE);
                } else {
                    llProfileSon.setVisibility(View.GONE);
                }
            }
        });

        noDoubleClickListener = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                putCustomersAuthority();
            }
        };

    }

    @Override
    protected void setLayoutOfControls() {

        LayoutUtils.setLayoutHeight(rlCustomerTypeEditContainer, Constants.ROW_HEIGHT, mContext);
        LayoutUtils.setLayoutHeight(rlProfileEditContainer, Constants.ROW_HEIGHT, mContext);
        LayoutUtils.setLayoutHeight(rlRechargeContainer, Constants.ROW_HEIGHT, mContext);
        LayoutUtils.setLayoutHeight(rlMembershipManagementContainer, Constants.ROW_HEIGHT, mContext);
        LayoutUtils.setLayoutHeight(rlRefundContainer, Constants.ROW_HEIGHT, mContext);

        rlCustomerTypeEditContainer.addView(tvCustomerTypeEdit);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvCustomerTypeEdit, mContext);

        rlCustomerTypeEditContainer.addView(swCustomerTypeEdit);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(swCustomerTypeEdit, mContext);

        rlProfileEditContainer.addView(tvProfileEdit);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvProfileEdit, mContext);

        rlProfileEditContainer.addView(swProfileEdit);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(swProfileEdit, mContext);

        rlRechargeContainer.addView(tvRecharge);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvRecharge, mContext);

        rlRechargeContainer.addView(swRecharge);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(swRecharge, mContext);

        rlMembershipManagementContainer.addView(tvMembershipManagement);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvMembershipManagement, mContext);

        rlMembershipManagementContainer.addView(swMembershipManagement);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(swMembershipManagement, mContext);

        rlRefundContainer.addView(tvRefund);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvRefund, mContext);

        rlRefundContainer.addView(swRefund);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(swRefund, mContext);

    }

    @Override
    protected void setPropertyOfControls() {

        rlCustomerTypeEditContainer.setBackgroundColor(getColor(R.color.common_white));
        rlProfileEditContainer.setBackgroundColor(getColor(R.color.common_white));
        llProfileSon.setBackgroundColor(getColor(R.color.common_white));
        rlRechargeContainer.setBackgroundColor(getColor(R.color.common_white));
        rlMembershipManagementContainer.setBackgroundColor(getColor(R.color.common_white));
        rlRefundContainer.setBackgroundColor(getColor(R.color.common_white));

    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(R.string.staff_customers);
        getTvRight().setBackground(null);
        getTvRight().setText(R.string.common_save);
        getTvRight().setOnClickListener(noDoubleClickListener);
    }


    private void addMemberSon(JSONArray son) {

        String dataStr = son.toString();

        JsonStaffAuthorityGet jsonData = new JsonStaffAuthorityGet(null);
        try {
            jsonData.setJsonValuesForArrayMember(new JSONArray(dataStr));
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
        List<JsonStaffAuthorityGet.DataItem> sonList = jsonData.getDataList();


        profileSonCheckButtons = new ArrayList<>();


        for (int i = 0; i < sonList.size(); i++) {
            JsonStaffAuthorityGet.DataItem itemData = sonList.get(i);
            RelativeLayout itemRelativeLayout;
            IteeTextView textView;
            CheckSwitchButton checkBtn;
            itemRelativeLayout = new RelativeLayout(getActivity());

            itemRelativeLayout.setBackgroundColor(getColor(R.color.common_white));

            textView = new IteeTextView(getActivity());
            textView.setId(View.LAYOUT_DIRECTION_LTR);
            textView.setText(itemData.getMemberTypeName());
            textView.setTextColor(getResources().getColor(R.color.common_black));

            checkBtn = new CheckSwitchButton(this);
            checkBtn.setId(View.LAYOUT_DIRECTION_RTL);
            itemRelativeLayout.addView(textView);
            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(textView, 40, mContext);

            itemRelativeLayout.addView(checkBtn);
            LayoutUtils.setCellRightValueViewOfRelativeLayout(checkBtn, mContext);
            checkBtn.setTag(itemData.getMemberTypeId());
            if (itemData.getMemberStatus() == Constants.SWITCH_RIGHT) {
                checkBtn.setChecked(true);
            } else {
                checkBtn.setChecked(false);
            }
            profileSonCheckButtons.add(checkBtn);
            llProfileSon.addView(itemRelativeLayout);
            LayoutUtils.setLayoutHeight(itemRelativeLayout, 100, mContext);
            if (i < sonList.size() - 1) {
                llProfileSon.addView(AppUtils.getSeparatorLine(mContext));
            }
        }
    }

    private String getAuthCodeString() {
        Map<String, String> customerType = null;
        Map<String, String> profile = null;
        Map<String, String> recharge = null;
        Map<String, String> membership = null;
        Map<String, String> refund = null;

        if (swCustomerTypeEdit.isChecked()) {
            customerType = new HashMap<>();
            customerType.put(JsonKey.STAFF_DEPARTMENT_ID, String.valueOf(swCustomerTypeEdit.getTag()));
            customerType.put(JsonKey.STAFF_DEPARTMENT_RELEVANCE, Constants.STR_EMPTY);
        }

        if (swProfileEdit.isChecked()) {
            profile = new HashMap<>();
            profile.put(JsonKey.STAFF_DEPARTMENT_ID, String.valueOf(swProfileEdit.getTag()));
            StringBuilder sb = new StringBuilder();
            for (CheckSwitchButton btn : profileSonCheckButtons) {
                if (btn.isChecked()) {
                    sb.append(String.valueOf(btn.getTag()));
                    sb.append(Constants.STR_COMMA);
                }
            }
            String res = sb.toString();
            if (res.length() > 0) {
                res = res.substring(0, res.length() - 1);
            } else {
                res = Constants.STR_0;
            }
            profile.put(JsonKey.STAFF_DEPARTMENT_RELEVANCE, res);
        }

        if (swRecharge.isChecked()) {
            recharge = new HashMap<>();
            recharge.put(JsonKey.STAFF_DEPARTMENT_ID, String.valueOf(swRecharge.getTag()));
            recharge.put(JsonKey.STAFF_DEPARTMENT_RELEVANCE, Constants.STR_EMPTY);
        }

        if (swMembershipManagement.isChecked()) {
            membership = new HashMap<>();
            membership.put(JsonKey.STAFF_DEPARTMENT_ID, String.valueOf(swMembershipManagement.getTag()));
            membership.put(JsonKey.STAFF_DEPARTMENT_RELEVANCE, Constants.STR_EMPTY);
        }


        if (swRefund.isChecked()) {
            refund = new HashMap<>();
            refund.put(JsonKey.STAFF_DEPARTMENT_ID, String.valueOf(swRefund.getTag()));
            refund.put(JsonKey.STAFF_DEPARTMENT_RELEVANCE, Constants.STR_EMPTY);
        }

        JSONArray array = new JSONArray();
        if (customerType != null) {
            array.put(new JSONObject(customerType));
        }
        if (profile != null) {
            array.put(new JSONObject(profile));
        }
        if (recharge != null) {
            array.put(new JSONObject(recharge));
        }
        if (membership != null) {
            array.put(new JSONObject(membership));
        }
        if (refund != null) {
            array.put(new JSONObject(refund));
        }

        return array.toString();
    }

    private void putCustomersAuthority() {


        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_COURES_ID, AppUtils.getCurrentCourseId(getActivity()));
        if (passedUserId != 0) {
            params.put(ApiKey.STAFF_USER_ID, String.valueOf(passedUserId));
            params.put(ApiKey.STAFF_DEPARTMENT_ID, String.valueOf(departmentId));
        } else {
            params.put(ApiKey.STAFF_DEPARTMENT_ID, String.valueOf(departmentId));
        }
        params.put(ApiKey.STAFF_AUTH_ID, auId);
        String putDataStr = getAuthCodeString();
        params.put(ApiKey.STAFF_AUTH_CODE, putDataStr);
        Log.i("--customer", params.toString());
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(CustomersAuthorityFragment.this) {
            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                Utils.showShortToast(getActivity(), msg);
                Log.i("--msg", msg);
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    getBaseActivity().doBackWithRefresh();
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
        Utils.hideKeyboard(getBaseActivity());
        hh.startPut(getActivity(), ApiManager.HttpApi.StaffCustomersAuthorityPut, params);
    }

}
