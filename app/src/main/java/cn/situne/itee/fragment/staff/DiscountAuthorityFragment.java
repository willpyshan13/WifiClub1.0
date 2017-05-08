/**
 * Project Name: itee
 * File Name:	 DiscountAuthorityFragment.java
 * Package Name: cn.situne.itee.fragment.staff
 * Date:		 2015-31-03
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.staff;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import cn.situne.itee.view.IteeIntegerEditText;
import cn.situne.itee.view.IteeMoneyEditText;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:DiscountAuthorityFragment <br/>
 * Function: 打折 权限 <br/>
 * UI:  7-2-1-3
 * Date: 2015-31-03 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */

public class DiscountAuthorityFragment extends BaseFragment {
    private List<JsonStaffAuthorityGet.DataItem> dataList;
    private RelativeLayout rlDiscount;
    private RelativeLayout rlPercent;
    private RelativeLayout rlMoney;

    private IteeTextView tvDiscount;
    private CheckSwitchButton swDiscount;
    private ImageView ivPercent;
    private IteeIntegerEditText etPercent;
    private IteeTextView tvMoney;
    private IteeMoneyEditText etMoney;
    private String auId;

    private int departmentId;
    private int passedUserId;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_staff_authority_discount;
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
                jsonData.setJsonValuesForArrayDiscount(new JSONArray(dataStr));
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
            dataList = jsonData.getDataList();

        }

        rlDiscount = (RelativeLayout) rootView.findViewById(R.id.rl_discount);
        rlPercent = (RelativeLayout) rootView.findViewById(R.id.rl_percent);
        rlMoney = (RelativeLayout) rootView.findViewById(R.id.rl_money);

        tvDiscount = new IteeTextView(getActivity());
        swDiscount = new CheckSwitchButton(this);
        ivPercent = new ImageView(getActivity());
        etPercent = new IteeIntegerEditText(this);
        tvMoney = new IteeTextView(getActivity());
        etMoney = new IteeMoneyEditText(this);

    }

    private void putDiscountAuthority() {
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

        JSONArray paramArray = new JSONArray();
        if (swDiscount.isChecked()) {
            Map<String, String> discountAuthority = new HashMap<>();
            discountAuthority.put(JsonKey.STAFF_DEPARTMENT_ID, String.valueOf(swDiscount.getTag()));
            discountAuthority.put(JsonKey.STAFF_DEPARTMENT_RELEVANCE, Constants.STR_EMPTY);
            paramArray.put(new JSONObject(discountAuthority));

            Map<String, String> discount = new HashMap<>();
            discount.put(JsonKey.STAFF_DEPARTMENT_ID, String.valueOf(etPercent.getTag()));
            discount.put(JsonKey.STAFF_DEPARTMENT_RELEVANCE, etPercent.getText().toString());
            paramArray.put(new JSONObject(discount));

            Map<String, String> money = new HashMap<>();
            money.put(JsonKey.STAFF_DEPARTMENT_ID, String.valueOf(etMoney.getTag()));
            money.put(JsonKey.STAFF_DEPARTMENT_RELEVANCE, etMoney.getText().toString());
            paramArray.put(new JSONObject(money));


        }
        putDataStr = paramArray.toString();
        params.put(ApiKey.STAFF_AUTH_CODE, putDataStr);

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(DiscountAuthorityFragment.this) {

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
        hh.startPut(getActivity(), ApiManager.HttpApi.StaffDiscountAuthorityPut, params);

    }


    @Override
    protected void setDefaultValueOfControls() {

        LayoutUtils.setLayoutHeight(rlDiscount, Constants.ROW_HEIGHT, mContext);
        LayoutUtils.setLayoutHeight(rlPercent, Constants.ROW_HEIGHT, mContext);
        LayoutUtils.setLayoutHeight(rlMoney, Constants.ROW_HEIGHT, mContext);

        rlDiscount.addView(tvDiscount);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvDiscount, mContext);

        rlDiscount.addView(swDiscount);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(swDiscount, mContext);

        rlPercent.addView(ivPercent);
        LayoutUtils.setCellLeftSquareViewOfRelativeLayout(ivPercent, 70, mContext);

        rlPercent.addView(etPercent);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(etPercent, mContext);

        rlMoney.addView(tvMoney);
        LayoutUtils.setCellLeftSquareViewOfRelativeLayout(tvMoney, 70, mContext);
        tvMoney.setGravity(Gravity.CENTER);

        rlMoney.addView(etMoney);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(etMoney, mContext);
    }

    @Override
    protected void setListenersOfControls() {

    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {
        tvDiscount.setText(R.string.staff_discount);
        tvDiscount.setTextColor(getColor(R.color.common_black));
        swDiscount.setTag(dataList.get(0).getAuId());

        ivPercent.setBackgroundResource(R.drawable.icon_discount_percent);

        etPercent.setText(String.valueOf(dataList.get(1).getDiscount()));
        etPercent.setTag(dataList.get(1).getAuId());
        etPercent.setSingleLine();
        etPercent.setBackground(null);
        etPercent.setTextColor(getColor(R.color.common_gray));
        etPercent.setGravity(Gravity.END);
        etPercent.addTextChangedListener(new AppUtils.EditViewIntegerWatcher(etPercent, Constants.MAX_PERCENT));

        tvMoney.setText(AppUtils.getCurrentCurrency(getActivity()));
        tvMoney.setGravity(Gravity.CENTER);
        tvMoney.setTextColor(getResources().getColor(R.color.common_blue));
        tvMoney.setBackgroundResource(R.drawable.icon_discount_currency);

        etMoney.setText(String.valueOf(dataList.get(2).getMoney()));
        etMoney.setTag(dataList.get(2).getAuId());
        etMoney.setTextColor(getColor(R.color.common_gray));
        etMoney.setSingleLine();
        etMoney.setBackground(null);
        etMoney.setGravity(Gravity.END);
        etMoney.addTextChangedListener(new AppUtils.EditViewMoneyWatcher(etMoney));


        if (dataList.get(0).getAuStatus() == Constants.SWITCH_RIGHT) {
            swDiscount.setChecked(true);
            etPercent.setEnabled(true);
            etMoney.setEnabled(true);
            etPercent.setTextColor(getColor(R.color.common_black));
            etMoney.setTextColor(getColor(R.color.common_black));
        } else {
            swDiscount.setChecked(false);
            etPercent.setEnabled(false);
            etMoney.setEnabled(false);
            etPercent.setTextColor(getColor(R.color.common_gray));
            etMoney.setTextColor(getColor(R.color.common_gray));
        }

        swDiscount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {
                    etPercent.setEnabled(false);
                    etMoney.setEnabled(false);
                    etPercent.setTextColor(getColor(R.color.common_gray));
                    etMoney.setTextColor(getColor(R.color.common_gray));
                } else {
                    etPercent.setEnabled(true);
                    etMoney.setEnabled(true);
                    etPercent.setTextColor(getColor(R.color.common_black));
                    etMoney.setTextColor(getColor(R.color.common_black));
                }
            }
        });

    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(R.string.staff_discount);
        getTvRight().setBackground(null);
        getTvRight().setText(R.string.common_save);
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putDiscountAuthority();
            }
        });
    }
}
