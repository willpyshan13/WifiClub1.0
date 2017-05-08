/**
 * Project Name: itee
 * File Name:	 StaffAuthorityFragment.java
 * Package Name: cn.situne.itee.fragment.staff
 * Date:		 2015-03-02
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
 * ClassName:StaffAuthorityFragment <br/>
 * Function: set authority of staff <br/>
 * Date: 2015-03-02 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class StaffAuthorityFragment extends BaseFragment {
    private List<JsonStaffAuthorityGet.DataItem> dataList;
    private RelativeLayout rlStaffContainer;
    private RelativeLayout rlDepartmentsContainer;

    private IteeTextView tvStaff;
    private IteeTextView tvDepartments;

    private CheckSwitchButton swStaff;
    private CheckSwitchButton swDepartments;
    private String auId;

    private int passedUserId;
    private int departmentId;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_staff_authority_staff;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            passedUserId = bundle.getInt(TransKey.STAFF_PASSED_USER_ID);
            departmentId = bundle.getInt(TransKey.STAFF_DEPARTMENT_ID);
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

        rlStaffContainer = (RelativeLayout) rootView.findViewById(R.id.rl_staff_container);
        rlDepartmentsContainer = (RelativeLayout) rootView.findViewById(R.id.rl_departments_container);

        tvStaff = new IteeTextView(getActivity());
        tvDepartments = new IteeTextView(getActivity());

        swStaff = new CheckSwitchButton(this);
        swDepartments = new CheckSwitchButton(this);
    }

    @Override
    protected void setDefaultValueOfControls() {

        tvStaff.setText(R.string.staff_staff);
        tvStaff.setTextColor(getResources().getColor(R.color.common_black));

        tvDepartments.setText(R.string.staff_see_other_department);
        tvDepartments.setTextColor(getResources().getColor(R.color.common_black));

        swStaff.setTag(dataList.get(0).getAuId());
        if (dataList.get(0).getAuStatus() == Constants.SWITCH_RIGHT) {
            swStaff.setChecked(true);
        } else {
            swStaff.setChecked(false);
        }
        swDepartments.setTag(dataList.get(1).getAuId());
        if (dataList.get(1).getAuStatus() == Constants.SWITCH_RIGHT) {
            swDepartments.setChecked(true);
        } else {
            swDepartments.setChecked(false);
        }
    }

    @Override
    protected void setListenersOfControls() {

    }

    @Override
    protected void setLayoutOfControls() {

        LayoutUtils.setLayoutHeight(rlStaffContainer, Constants.ROW_HEIGHT, mContext);
        LayoutUtils.setLayoutHeight(rlDepartmentsContainer, Constants.ROW_HEIGHT, mContext);

        rlStaffContainer.addView(tvStaff);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvStaff, mContext);

        rlStaffContainer.addView(swStaff);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(swStaff, mContext);

        rlDepartmentsContainer.addView(tvDepartments);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvDepartments, mContext);

        rlDepartmentsContainer.addView(swDepartments);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(swDepartments, mContext);
    }

    @Override
    protected void setPropertyOfControls() {
        rlDepartmentsContainer.setBackgroundColor(getColor(R.color.common_white));
        rlStaffContainer.setBackgroundColor(getColor(R.color.common_white));
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(R.string.staff_staff);
        getTvRight().setBackground(null);
        getTvRight().setText(R.string.common_save);
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putStaffAuthority();
            }
        });
    }

    private String getAuthCodeString() {

        StringBuilder sb = new StringBuilder();
        if (swStaff.isChecked()) {
            sb.append(String.valueOf(swStaff.getTag()));
            sb.append(Constants.STR_COMMA);
        }

        if (swDepartments.isChecked()) {
            sb.append(String.valueOf(swDepartments.getTag()));
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

    private void putStaffAuthority() {

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
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(StaffAuthorityFragment.this) {
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
        hh.startPut(getActivity(), ApiManager.HttpApi.StaffAuthorityStaffPut, params);
    }
}
