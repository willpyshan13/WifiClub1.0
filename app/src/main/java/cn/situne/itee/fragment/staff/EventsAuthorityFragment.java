/**
 * Project Name: itee
 * File Name:	 EventsAuthorityFragment.java
 * Package Name: cn.situne.itee.fragment.staff
 * Date:		 2015-02-28
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */
package cn.situne.itee.fragment.staff;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonStaffAuthorityGet;
import cn.situne.itee.view.CheckSwitchButton;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:EventsAuthorityFragment <br/>
 * Function: Event 权限 <br/>
 * UI:  7-2-1-4
 * Date: 2015-02-28 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */


public class EventsAuthorityFragment extends BaseFragment {

    private RelativeLayout rlEvent;

    private IteeTextView tvEvent;
    private CheckSwitchButton swEvent;
    private List<JsonStaffAuthorityGet.DataItem> dataList;

    private int departmentId;
    private int passedUserId;
    private String auId;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_staff_authority_events;
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
        rlEvent = (RelativeLayout) rootView.findViewById(R.id.rl_event);
        tvEvent = new IteeTextView(getActivity());
        swEvent = new CheckSwitchButton(this);
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        LinearLayout.LayoutParams paramEventContainer = (LinearLayout.LayoutParams) rlEvent.getLayoutParams();
        paramEventContainer.height = Constants.ROW_HEIGHT;
        rlEvent.setLayoutParams(paramEventContainer);

        rlEvent.addView(tvEvent);
        RelativeLayout.LayoutParams paramTvEvent = (RelativeLayout.LayoutParams) tvEvent.getLayoutParams();
        paramTvEvent.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvEvent.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvEvent.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvEvent.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvEvent.leftMargin = 40;
        tvEvent.setLayoutParams(paramTvEvent);

        rlEvent.addView(swEvent);
        RelativeLayout.LayoutParams paramsSwEvent = (RelativeLayout.LayoutParams) swEvent.getLayoutParams();
        paramsSwEvent.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsSwEvent.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsSwEvent.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsSwEvent.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsSwEvent.rightMargin = 40;
        swEvent.setLayoutParams(paramsSwEvent);
    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {
        tvEvent.setText(R.string.staff_events);
        tvEvent.setTextColor(getColor(R.color.common_black));
        swEvent.setTag(dataList.get(0).getAuId());
        if (dataList.get(0).getAuStatus() == Constants.SWITCH_RIGHT) {
            swEvent.setChecked(true);
        } else {
            swEvent.setChecked(false);
        }
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(R.string.staff_events);
        getTvRight().setBackground(null);
        getTvRight().setText(R.string.common_save);
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putEventsAuthority();
            }
        });
    }

    private void putEventsAuthority() {


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
        String putDataStr;
        if (swEvent.isChecked()) {
            putDataStr = String.valueOf(swEvent.getTag());
        } else {
            putDataStr = Constants.STR_0;
        }
        params.put(ApiKey.STAFF_AUTH_CODE, putDataStr);

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(EventsAuthorityFragment.this) {

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
        hh.startPut(getActivity(), ApiManager.HttpApi.StaffAuthorityEventsPut, params);
    }

}
