/**
 * Project Name: itee
 * File Name:	 AddProfileFragment.java
 * Package Name: cn.situne.itee.fragment.staff
 * Date:		 2015-03-03
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.administration;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonSpecialDay;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:AgentsAuthorityFragment <br/>
 * Function: add/edit/delete profile <br/>
 * Date: 2015-9-16 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class SpecialDayFragment extends BaseEditFragment {

    private LinearLayout ll_special_days_content;
    private RelativeLayout rl_add;
    private IteeTextView tv_add;

    private ArrayList<JsonSpecialDay.SpecialDay> dataList;
    private ArrayList<JsonSpecialDay.SpecialDay> deleteList;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_special_days;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.add_title_add_course;
    }

    @Override
    protected void initControls(View rootView) {
        deleteList = new ArrayList<>();
        rl_add = (RelativeLayout) getRootView().findViewById(R.id.ll_add);
        ll_special_days_content = (LinearLayout) getRootView().findViewById(R.id.ll_special_days_content);
        tv_add = (IteeTextView) getRootView().findViewById(R.id.tv_add);

    }

    private void addDayView(JsonSpecialDay.SpecialDay defaultName) {
        SpecialDaysItemView playerInfoTelItemView = new SpecialDaysItemView(getActivity(), ll_special_days_content, defaultName, dataList, deleteList);
        ll_special_days_content.addView(playerInfoTelItemView);
        LinearLayout.LayoutParams paramsAddressWarn = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
        playerInfoTelItemView.setLayoutParams(paramsAddressWarn);
    }


    @Override
    protected void setDefaultValueOfControls() {
        if (dataList != null && dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                JsonSpecialDay.SpecialDay specialDay = dataList.get(i);
                addDayView(specialDay);
            }
        }
    }

    @Override
    protected void setListenersOfControls() {

        rl_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonSpecialDay.SpecialDay specialDay = new JsonSpecialDay.SpecialDay();
                dataList.add(specialDay);
                addDayView(specialDay);

            }
        });


    }

    @Override
    protected void setLayoutOfControls() {

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv_add.getLayoutParams();
        layoutParams.leftMargin = getActualWidthOnThisDevice(40);
        tv_add.setLayoutParams(layoutParams);

        LinearLayout.LayoutParams paramsAddressWarn1 = (LinearLayout.LayoutParams) rl_add.getLayoutParams();
        paramsAddressWarn1.height = getActualHeightOnThisDevice(100);
        rl_add.setLayoutParams(paramsAddressWarn1);
        AppUtils.addBottomSeparatorLine(rl_add, mContext);

    }

    @Override
    protected void setPropertyOfControls() {


    }

    @Override
    protected void executeOnceOnCreate() {
        netLinkGetAccount();
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getString(R.string.administration_special_title));
        getTvRight().setText(getString(R.string.common_ok));
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netLinkEditAccount();
            }
        });
    }

    private void netLinkGetAccount() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        HttpManager<JsonSpecialDay> hh = new HttpManager<JsonSpecialDay>(SpecialDayFragment.this) {
            @Override
            public void onJsonSuccess(JsonSpecialDay jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    dataList = jo.getDataList();
                    setDefaultValueOfControls();
                } else {
                    Utils.showShortToast(mContext, msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.XDEVELOPX02361, params);
    }


    private void netLinkEditAccount() {


        Map<String, String> params = new HashMap<>();

        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(JsonKey.COMMON_DATA_LIST, getPriceList());
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(SpecialDayFragment.this) {
            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    doBack();
                } else {
                    Utils.showShortToast(mContext, msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        Utils.log(params.toString());
        hh.start(getActivity(), ApiManager.HttpApi.XDEVELOPX02361, params);
    }


    private String getPriceList() {
        JSONObject jsonObject = new JSONObject();


        JSONArray array = new JSONArray();
        JSONArray arrayDelete = new JSONArray();

        for (int i = 0; i < dataList.size(); i++) {
            Map<String, String> priceItem = new HashMap<>();

            JsonSpecialDay.SpecialDay param = dataList.get(i);
            if (Utils.isStringNotNullOrEmpty(param.getCdtName())) {
                priceItem.put(JsonKey.ADMINISTRATION_SPECIAL_DAYS_CDT_ACT, param.getCdtAct());
                priceItem.put(JsonKey.ADMINISTRATION_SPECIAL_DAYS_CDT_FIRST_NAME, param.getCdtFirstName());
                priceItem.put(JsonKey.ADMINISTRATION_SPECIAL_DAYS_CDT_ID, param.getCdtId());
                priceItem.put(JsonKey.ADMINISTRATION_SPECIAL_DAYS_CDT_NAME, param.getCdtName());
                array.put(new JSONObject(priceItem));
            }
        }
        for (int i = 0; i < deleteList.size(); i++) {
            Map<String, String> priceItem = new HashMap<>();

            JsonSpecialDay.SpecialDay param = deleteList.get(i);
            priceItem.put(JsonKey.ADMINISTRATION_SPECIAL_DAYS_CDT_ACT, param.getCdtAct());
            priceItem.put(JsonKey.ADMINISTRATION_SPECIAL_DAYS_CDT_FIRST_NAME, param.getCdtFirstName());
            priceItem.put(JsonKey.ADMINISTRATION_SPECIAL_DAYS_CDT_ID, param.getCdtId());
            priceItem.put(JsonKey.ADMINISTRATION_SPECIAL_DAYS_CDT_NAME, param.getCdtName());
            arrayDelete.put(new JSONObject(priceItem));
        }

        try {
            jsonObject.put(JsonKey.ADMINISTRATION_SPECIAL_DAYS_SAVE, array);
            jsonObject.put(JsonKey.ADMINISTRATION_SPECIAL_DAYS_DEL, arrayDelete);

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
        return jsonObject.toString();
    }

}
