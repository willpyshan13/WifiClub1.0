/**
 * Project Name: itee
 * File Name:	 TeeTimesAuthorityFragment.java
 * Package Name: cn.situne.itee.fragment.staff
 * Date:		 2015-02-28
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
 * ClassName:TeeTimesAuthorityFragment <br/>
 * Function: set authority of Teetimes <br/>
 * Date: 2015-02-28 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class TeeTimesAuthorityFragment extends BaseFragment {

    private RelativeLayout rlCourseDate;
    private RelativeLayout rlTeeTime;
    private RelativeLayout rlCheckIn;
    private RelativeLayout rlCheckOut;
    private RelativeLayout rlOtherReservations;

    private IteeTextView tvCourseDate;
    private CheckSwitchButton swCourseDate;
    private IteeTextView tvTeeTime;
    private CheckSwitchButton swTeeTime;
    private IteeTextView tvCheckIN;
    private CheckSwitchButton swCheckIN;
    private IteeTextView tvCheckOut;
    private CheckSwitchButton swCheckOut;
    private IteeTextView tvOtherReservation;
    private CheckSwitchButton swOtherReservation;
    private String auId;

    private int departmentId;
    private int passedUserId;

    private List<JsonStaffAuthorityGet.DataItem> dataList;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_staff_authority_tee_times;
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
            auId = bundle.getString(TransKey.STAFF_AU_ID);
            passedUserId = bundle.getInt(TransKey.STAFF_PASSED_USER_ID);
            String dataStr = bundle.getString(TransKey.STAFF_AU_SON);

            JsonStaffAuthorityGet jsonData = new JsonStaffAuthorityGet(null);
            try {
                jsonData.setJsonValuesForArray(new JSONArray(dataStr));
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
            dataList = jsonData.getDataList();
        }


        rlCourseDate = (RelativeLayout) rootView.findViewById(R.id.rl_courseDate);
        rlTeeTime = (RelativeLayout) rootView.findViewById(R.id.rl_teeTime);
        rlCheckIn = (RelativeLayout) rootView.findViewById(R.id.rl_checkIn);
        rlCheckOut = (RelativeLayout) rootView.findViewById(R.id.rl_checkOut);
        rlOtherReservations = (RelativeLayout) rootView.findViewById(R.id.rl_otherReservations);

        tvCourseDate = new IteeTextView(getActivity());
        swCourseDate = new CheckSwitchButton(this);

        tvTeeTime = new IteeTextView(getActivity());
        swTeeTime = new CheckSwitchButton(this);
        tvCheckIN = new IteeTextView(getActivity());
        swCheckIN = new CheckSwitchButton(this);
        tvCheckOut = new IteeTextView(getActivity());
        swCheckOut = new CheckSwitchButton(this);
        tvOtherReservation = new IteeTextView(getActivity());
        swOtherReservation = new CheckSwitchButton(this);

    }

    @Override
    protected void setDefaultValueOfControls() {

        LayoutUtils.setLayoutHeight(rlCourseDate, Constants.ROW_HEIGHT, mContext);
        LayoutUtils.setLayoutHeight(rlTeeTime, Constants.ROW_HEIGHT, mContext);
        LayoutUtils.setLayoutHeight(rlCheckIn, Constants.ROW_HEIGHT, mContext);
        LayoutUtils.setLayoutHeight(rlCheckOut, Constants.ROW_HEIGHT, mContext);
        LayoutUtils.setLayoutHeight(rlOtherReservations, Constants.ROW_HEIGHT, mContext);

        rlCourseDate.addView(tvCourseDate);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvCourseDate, mContext);

        rlCourseDate.addView(swCourseDate);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(swCourseDate, mContext);

        rlTeeTime.addView(tvTeeTime);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvTeeTime, mContext);

        rlTeeTime.addView(swTeeTime);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(swTeeTime, mContext);

        rlCheckIn.addView(tvCheckIN);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvCheckIN, mContext);

        rlCheckIn.addView(swCheckIN);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(swCheckIN, mContext);

        rlCheckOut.addView(tvCheckOut);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvCheckOut, mContext);

        rlCheckOut.addView(swCheckOut);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(swCheckOut, mContext);

        rlOtherReservations.addView(tvOtherReservation);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvOtherReservation, mContext);

        rlOtherReservations.addView(swOtherReservation);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(swOtherReservation, mContext);
    }

    @Override
    protected void setListenersOfControls() {

    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {
        tvCourseDate.setText(R.string.staff_edit_course_data);
        tvCourseDate.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvCourseDate.setTextColor(getColor(R.color.common_black));


        tvTeeTime.setText(R.string.staff_edit_tee_times);
        tvTeeTime.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvTeeTime.setTextColor(getColor(R.color.common_black));


        tvCheckIN.setText(R.string.staff_check_in_undo);
        tvCheckIN.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvCheckIN.setTextColor(getColor(R.color.common_black));


        tvCheckOut.setText(R.string.staff_check_out);
        tvCheckOut.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvCheckOut.setTextColor(getColor(R.color.common_black));


        tvOtherReservation.setText(R.string.staff_see_other_reservation);
        tvOtherReservation.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvOtherReservation.setTextColor(getColor(R.color.common_black));
        swCourseDate.setTag(dataList.get(0).getAuId());
        if (dataList.get(0).getAuStatus() == Constants.SWITCH_RIGHT) {
            swCourseDate.setChecked(true);
        } else {
            swCourseDate.setChecked(false);
        }
        swTeeTime.setTag(dataList.get(1).getAuId());
        if (dataList.get(1).getAuStatus() == Constants.SWITCH_RIGHT) {
            swTeeTime.setChecked(true);
        } else {
            swTeeTime.setChecked(false);
        }
        swCheckIN.setTag(dataList.get(2).getAuId());
        if (dataList.get(2).getAuStatus() == Constants.SWITCH_RIGHT) {
            swCheckIN.setChecked(true);
        } else {
            swCheckIN.setChecked(false);
        }

        swCheckOut.setTag(dataList.get(3).getAuId());
        if (dataList.get(3).getAuStatus() == Constants.SWITCH_RIGHT) {
            swCheckOut.setChecked(true);
        } else {
            swCheckOut.setChecked(false);
        }
        swOtherReservation.setTag(dataList.get(4).getAuId());
        if (dataList.get(4).getAuStatus() == Constants.SWITCH_RIGHT) {
            swOtherReservation.setChecked(true);
        } else {
            swOtherReservation.setChecked(false);
        }


    }

    private String getAuthCodeString() {

        StringBuilder sb = new StringBuilder();

        if (swCourseDate.isChecked()) {
            sb.append(String.valueOf(swCourseDate.getTag()));
            sb.append(Constants.STR_COMMA);
        }
        if (swTeeTime.isChecked()) {
            sb.append(String.valueOf(swTeeTime.getTag()));
            sb.append(Constants.STR_COMMA);
        }
        if (swCheckIN.isChecked()) {
            sb.append(String.valueOf(swCheckIN.getTag()));
            sb.append(Constants.STR_COMMA);
        }
        if (swCheckOut.isChecked()) {
            sb.append(String.valueOf(swCheckOut.getTag()));
            sb.append(Constants.STR_COMMA);
        }
        if (swOtherReservation.isChecked()) {
            sb.append(String.valueOf(swOtherReservation.getTag()));
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

    private void putTeeTimeAuthority() {
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
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(TeeTimesAuthorityFragment.this) {
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

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(R.string.staff_tee_times);
        getTvRight().setBackground(null);
        getTvRight().setText(R.string.common_save);
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putTeeTimeAuthority();
            }
        });
    }
}
