/**
 * Project Name: itee
 * File Name:	 DepartmentDetailFragment.java
 * Package Name: cn.situne.itee.fragment.shops
 * Date:		 2015-03-15
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.staff;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.adapter.StaffDepartmentDetailAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonUserListGet;

/**
 * ClassName:DepartmentDetailFragment <br/>
 * Function: add/edit users of the department. <br/>
 * Date: 2015-03-15 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class StaffDepartmentMemberListFragment extends BaseFragment {

    private ListView lvDepartmentDetail;
    private StaffDepartmentDetailAdapter adapterDepartmentDetail;

    private ArrayList<JsonUserListGet.DepartmentUserInfo> userList;

    private View.OnClickListener listenerAddProfile;

    private Integer courseId;
    private Integer departmentId;
    private String departmentName;
    private int type;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_staff_department_detail;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        lvDepartmentDetail = (ListView) rootView.findViewById(R.id.finance_department_list);
        userList = new ArrayList<>();
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void reShowWithBackValue() {

        Bundle bundle = getReturnValues();
        if (bundle != null && bundle.getBoolean(TransKey.COMMON_REFRESH)) {
            getMemberList();
        }
    }

    @Override
    protected void setListenersOfControls() {
        listenerAddProfile = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(TransKey.STAFF_IS_ADD, true);
                bundle.putInt(TransKey.STAFF_DEPARTMENT_ID, departmentId);
                bundle.putInt(TransKey.STAFF_COURSE_ID, courseId);
                bundle.putInt(TransKey.STAFF_DEPARTMENT_TYPE, type);
                bundle.putString(TransKey.STAFF_DEPARTMENT_NAME, departmentName);
                bundle.putString(TransKey.COMMON_FROM_PAGE, StaffDepartmentMemberListFragment.class.getName());
                push(AddEditProfileFragment.class, bundle);
            }
        };

        lvDepartmentDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JsonUserListGet.DepartmentUserInfo departmentUserInfo = userList.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt(TransKey.COMMON_MEMBER_ID, departmentUserInfo.getUserId());
                bundle.putString(TransKey.COMMON_PHOTO_URL, departmentUserInfo.getUserPhoto());
                bundle.putBoolean(TransKey.STAFF_IS_ADD, false);
                bundle.putInt(TransKey.STAFF_DEPARTMENT_ID, departmentId);
                bundle.putInt(TransKey.STAFF_DEPARTMENT_TYPE, type);
                bundle.putInt(TransKey.STAFF_COURSE_ID, courseId);
                bundle.putString(TransKey.STAFF_DEPARTMENT_NAME, departmentName);
                bundle.putInt(TransKey.STAFF_PASSED_USER_ID, departmentUserInfo.getUserId());
                bundle.putString(TransKey.COMMON_FROM_PAGE, StaffDepartmentMemberListFragment.class.getName());
                push(AddEditProfileFragment.class, bundle);
            }
        });
    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {

        adapterDepartmentDetail = new StaffDepartmentDetailAdapter(this, userList);
        lvDepartmentDetail.setAdapter(adapterDepartmentDetail);
        lvDepartmentDetail.setDivider(null);
        adapterDepartmentDetail.notifyDataSetChanged();
    }

    @Override
    protected void configActionBar() {
        boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlSeeOtherDepartments, getActivity());
        if (hasPermission) {
            setStackedActionBar();
        } else {
            setNormalMenuActionBar();
        }
        getTvLeftTitle().setText(departmentName);
        getTvRight().setBackgroundResource(R.drawable.icon_common_add);
        getTvRight().setOnClickListener(listenerAddProfile);
    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        getMemberList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            departmentId = bundle.getInt(TransKey.STAFF_DEPARTMENT_ID, -1);
            departmentName = bundle.getString(TransKey.STAFF_DEPARTMENT_NAME);
            courseId = bundle.getInt(TransKey.STAFF_COURSE_ID);
            type = bundle.getInt(TransKey.STAFF_DEPARTMENT_TYPE);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void getMemberList() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        if (departmentId != null && departmentId > 0) {
            params.put(ApiKey.STAFF_DEPARTMENT_ID, String.valueOf(departmentId));
        }

        HttpManager<JsonUserListGet> hh = new HttpManager<JsonUserListGet>(StaffDepartmentMemberListFragment.this) {

            @Override
            public void onJsonSuccess(JsonUserListGet jo) {
                int returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    departmentId = Integer.valueOf(jo.getDepartmentId());
                    departmentName = jo.getDepartmentName();
                    courseId = jo.getCourseId();
                    type = jo.getType();
                    if (StringUtils.isNotEmpty(departmentName)) {
                        getTvLeftTitle().setText(departmentName);
                    }
                    if (Constants.STR_FLAG_YES.equals(jo.getEditFlag())) {
                        getTvRight().setVisibility(View.VISIBLE);
                    } else {
                        getTvRight().setVisibility(View.INVISIBLE);
                    }

                    if (jo.getMemberList() != null && jo.getMemberList().size() > 0) {
                        if (userList.size() > 0) {
                            userList.clear();
                        }
                        userList.addAll(jo.getMemberList());

                        adapterDepartmentDetail.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
            }
        };
        hh.startGet(getBaseActivity(), ApiManager.HttpApi.StaffUserListGet, params);
    }
}
