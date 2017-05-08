/**
 * Project Name: itee
 * File Name:	 AgentsAuthorityFragment.java
 * Package Name: cn.situne.itee.fragment.staff
 * Date:		 2015-03-20
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */
package cn.situne.itee.fragment.staff;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
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
 * ClassName:AgentsAuthorityFragment <br/>
 * Function: set authority of agents <br/>
 * Date: 2015-03-03 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class AgentsAuthorityFragment extends BaseFragment {
    private List<JsonStaffAuthorityGet.DataItem> dataList;
    private RelativeLayout rlAgentsContainer;
    private RelativeLayout rlAgentsEditContainer;
    private RelativeLayout rlRechargeContainer;
    private RelativeLayout rlPricingTableContainer;
    private RelativeLayout rlRefundContainer;

    private IteeTextView tvAgents;
    private IteeTextView tvAgentsEdit;
    private IteeTextView tvRecharge;
    private IteeTextView tvPricingTable;
    private IteeTextView tvRefund;

    private CheckSwitchButton swAgents;
    private CheckSwitchButton swAgentsEdit;
    private CheckSwitchButton swRecharge;
    private CheckSwitchButton swPricingTable;
    private CheckSwitchButton swRefund;
    private String auId;

    private int departmentId;
    private int passedUserId;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_staff_authority_agents;
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

        rlAgentsContainer = (RelativeLayout) rootView.findViewById(R.id.rl_agents_container);
        rlAgentsEditContainer = (RelativeLayout) rootView.findViewById(R.id.rl_agents_edit_container);
        rlRechargeContainer = (RelativeLayout) rootView.findViewById(R.id.rl_recharge_container);
        rlPricingTableContainer = (RelativeLayout) rootView.findViewById(R.id.rl_pricing_table_container);
        rlRefundContainer = (RelativeLayout) rootView.findViewById(R.id.rl_refund_container);

        tvAgents = new IteeTextView(getActivity());
        tvAgentsEdit = new IteeTextView(getActivity());
        tvRecharge = new IteeTextView(getActivity());
        tvPricingTable = new IteeTextView(getActivity());
        tvRefund = new IteeTextView(getActivity());

        swAgents = new CheckSwitchButton(this);
        swAgentsEdit = new CheckSwitchButton(this);
        swRecharge = new CheckSwitchButton(this);
        swPricingTable = new CheckSwitchButton(this);
        swRefund = new CheckSwitchButton(this);
    }

    @Override
    protected void setDefaultValueOfControls() {
        tvAgents.setText(R.string.staff_agents_browse);
        tvAgents.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvAgents.setTextColor(getResources().getColor(R.color.common_black));

        tvAgentsEdit.setText(R.string.staff_agents_edit);
        tvAgentsEdit.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvAgentsEdit.setTextColor(getResources().getColor(R.color.common_black));


        tvRecharge.setText(R.string.staff_recharge);
        tvRecharge.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvRecharge.setTextColor(getResources().getColor(R.color.common_black));

        tvPricingTable.setText(R.string.staff_pricing_table);
        tvPricingTable.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvPricingTable.setTextColor(getResources().getColor(R.color.common_black));

        tvRefund.setText(R.string.staff_refund);
        tvRefund.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvRefund.setTextColor(getResources().getColor(R.color.common_black));
    }

    @Override
    protected void setListenersOfControls() {

    }

    @Override
    protected void setLayoutOfControls() {

        LayoutUtils.setLayoutHeight(rlAgentsContainer, 100, mContext);
        LayoutUtils.setLayoutHeight(rlAgentsEditContainer, 100, mContext);
        LayoutUtils.setLayoutHeightAndTopMargin(rlRechargeContainer, 100, 25, mContext);
        LayoutUtils.setLayoutHeight(rlPricingTableContainer, 100, mContext);
        LayoutUtils.setLayoutHeight(rlRefundContainer, 100, mContext);

        rlAgentsContainer.addView(tvAgents);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvAgents, mContext);

        rlAgentsContainer.addView(swAgents);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(swAgents, mContext);

        rlAgentsEditContainer.addView(tvAgentsEdit);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvAgentsEdit, mContext);

        rlAgentsEditContainer.addView(swAgentsEdit);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(swAgentsEdit, mContext);

        rlRechargeContainer.addView(tvRecharge);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvRecharge, mContext);

        rlRechargeContainer.addView(swRecharge);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(swRecharge, mContext);

        rlPricingTableContainer.addView(tvPricingTable);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvPricingTable, mContext);

        rlPricingTableContainer.addView(swPricingTable);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(swPricingTable, mContext);

        rlRefundContainer.addView(tvRefund);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvRefund, mContext);

        rlRefundContainer.addView(swRefund);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(swRefund, mContext);
    }

    @Override
    protected void setPropertyOfControls() {
        swAgents.setTag(dataList.get(0).getAuId());
        if (dataList.get(0).getAuStatus() == Constants.SWITCH_RIGHT) {
            swAgents.setChecked(true);
        } else {
            swAgents.setChecked(false);
        }
        swAgentsEdit.setTag(dataList.get(1).getAuId());
        if (dataList.get(1).getAuStatus() == Constants.SWITCH_RIGHT) {
            swAgentsEdit.setChecked(true);
        } else {
            swAgentsEdit.setChecked(false);
        }
        swRecharge.setTag(dataList.get(2).getAuId());
        if (dataList.get(2).getAuStatus() == Constants.SWITCH_RIGHT) {
            swRecharge.setChecked(true);
        } else {
            swRecharge.setChecked(false);
        }

        swPricingTable.setTag(dataList.get(3).getAuId());
        if (dataList.get(3).getAuStatus() == Constants.SWITCH_RIGHT) {
            swPricingTable.setChecked(true);
        } else {
            swPricingTable.setChecked(false);
        }
        swRefund.setTag(dataList.get(4).getAuId());
        if (dataList.get(4).getAuStatus() == Constants.SWITCH_RIGHT) {
            swRefund.setChecked(true);
        } else {
            swRefund.setChecked(false);
        }

        rlAgentsContainer.setBackgroundColor(getColor(R.color.common_white));
        rlAgentsEditContainer.setBackgroundColor(getColor(R.color.common_white));
        rlRechargeContainer.setBackgroundColor(getColor(R.color.common_white));
        rlPricingTableContainer.setBackgroundColor(getColor(R.color.common_white));
        rlRefundContainer.setBackgroundColor(getColor(R.color.common_white));
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(R.string.staff_agents);
        getTvRight().setBackground(null);
        getTvRight().setText(R.string.common_save);

        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putAgentsAuthority();
            }
        });

    }

    private String getAuthCodeString() {

        StringBuilder sb = new StringBuilder();
        if (swAgents.isChecked()) {
            sb.append(String.valueOf(swAgents.getTag()));
            sb.append(Constants.STR_COMMA);
        }
        if (swAgentsEdit.isChecked()) {
            sb.append(String.valueOf(swAgentsEdit.getTag()));
            sb.append(Constants.STR_COMMA);
        }
        if (swRecharge.isChecked()) {
            sb.append(String.valueOf(swRecharge.getTag()));
            sb.append(Constants.STR_COMMA);
        }
        if (swPricingTable.isChecked()) {
            sb.append(String.valueOf(swPricingTable.getTag()));
            sb.append(Constants.STR_COMMA);
        }
        if (swRefund.isChecked()) {
            sb.append(String.valueOf(swRefund.getTag()));
            sb.append(Constants.STR_COMMA);
        }
        String res = sb.toString();
        if (res.length() > 0) {
            res = res.substring(0, res.length() - 1);
        } else {
            res = Constants.STR_0;
        }
        return res;
    }

    private void putAgentsAuthority() {
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
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(AgentsAuthorityFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                Utils.showShortToast(getActivity(), msg);
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
        hh.startPut(getActivity(), ApiManager.HttpApi.StaffAuthorityTeeTimePut, params);


    }
}
