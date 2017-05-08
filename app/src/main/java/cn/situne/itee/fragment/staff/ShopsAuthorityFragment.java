/**
 * Project Name: itee
 * File Name:	 ShopsAuthorityFragment.java
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
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:ShopsAuthorityFragment <br/>
 * Function: set authority of shops <br/>
 * Date: 2015-03-20 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class ShopsAuthorityFragment extends BaseFragment {
    private List<JsonStaffAuthorityGet.DataItem> dataList;
    private RelativeLayout rlShop;
    private RelativeLayout rlProduct;
    private RelativeLayout rlSelling;

    private RelativeLayout rlBadInventory;
    private RelativeLayout rlAddInventory;

    private IteeTextView tvShop;
    private CheckSwitchButton swShop;
    private IteeTextView tvProduct;
    private CheckSwitchButton swProduct;
    private IteeTextView tvSelling;
    private CheckSwitchButton swSelling;

    private IteeTextView tvBadInventory;
    private CheckSwitchButton swBadInventory;
    private IteeTextView tvAddInventory;
    private CheckSwitchButton swAddInventory;
    private String auId;

    private int departmentId;
    private int passedUserId;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_staff_authority_shops;
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

        rlShop = (RelativeLayout) rootView.findViewById(R.id.rl_shop);
        rlProduct = (RelativeLayout) rootView.findViewById(R.id.rl_product);

        rlSelling = (RelativeLayout) rootView.findViewById(R.id.rl_selling);


        rlBadInventory = (RelativeLayout) rootView.findViewById(R.id.rl_badInventory);
        rlAddInventory = (RelativeLayout) rootView.findViewById(R.id.rl_addInventory);

        tvShop = new IteeTextView(getActivity());
        swShop = new CheckSwitchButton(this);
        tvProduct = new IteeTextView(getActivity());
        swProduct = new CheckSwitchButton(this);
        tvSelling = new IteeTextView(getActivity());
        swSelling = new CheckSwitchButton(this);
        tvBadInventory = new IteeTextView(getActivity());
        swBadInventory = new CheckSwitchButton(this);
        tvAddInventory = new IteeTextView(getActivity());
        swAddInventory = new CheckSwitchButton(this);
    }

    @Override
    protected void setDefaultValueOfControls() {

        swShop.setTag(dataList.get(0).getAuId());
        if (dataList.get(0).getAuStatus() == Constants.SWITCH_RIGHT) {
            swShop.setChecked(true);
        } else {
            swShop.setChecked(false);
        }

        swProduct.setTag(dataList.get(1).getAuId());
        if (dataList.get(1).getAuStatus() == Constants.SWITCH_RIGHT) {
            swProduct.setChecked(true);
        } else {
            swProduct.setChecked(false);
        }

        swSelling.setTag(dataList.get(2).getAuId());
        if (dataList.get(2).getAuStatus() == Constants.SWITCH_RIGHT) {
            swSelling.setChecked(true);
        } else {
            swSelling.setChecked(false);
        }

        swBadInventory.setTag(dataList.get(3).getAuId());
        if (dataList.get(3).getAuStatus() == Constants.SWITCH_RIGHT) {
            swBadInventory.setChecked(true);
        } else {
            swBadInventory.setChecked(false);
        }

        swAddInventory.setTag(dataList.get(4).getAuId());
        if (dataList.get(4).getAuStatus() == Constants.SWITCH_RIGHT) {
            swAddInventory.setChecked(true);
        } else {
            swAddInventory.setChecked(false);
        }
    }

    @Override
    protected void setListenersOfControls() {

    }

    @Override
    protected void setLayoutOfControls() {
        LayoutUtils.setLayoutHeight(rlShop, Constants.ROW_HEIGHT, mContext);
        LayoutUtils.setLayoutHeight(rlProduct, Constants.ROW_HEIGHT, mContext);
        LayoutUtils.setLayoutHeight(rlSelling, Constants.ROW_HEIGHT, mContext);
        LayoutUtils.setLayoutHeight(rlBadInventory, Constants.ROW_HEIGHT, mContext);
        LayoutUtils.setLayoutHeight(rlAddInventory, Constants.ROW_HEIGHT, mContext);

        rlShop.addView(tvShop);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvShop, mContext);

        rlShop.addView(swShop);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(swShop, mContext);

        rlProduct.addView(tvProduct);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvProduct, mContext);

        rlProduct.addView(swProduct);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(swProduct, mContext);

        rlSelling.addView(tvSelling);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvSelling, mContext);

        rlSelling.addView(swSelling);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(swSelling, mContext);

        rlBadInventory.addView(tvBadInventory);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvBadInventory, mContext);

        rlBadInventory.addView(swBadInventory);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(swBadInventory, mContext);

        rlAddInventory.addView(tvAddInventory);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvAddInventory, mContext);

        rlAddInventory.addView(swAddInventory);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(swAddInventory, mContext);
    }

    @Override
    protected void setPropertyOfControls() {

        tvShop.setText(R.string.staff_shop_edit);
        tvShop.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvShop.setTextColor(getColor(R.color.common_black));


        tvProduct.setText(R.string.staff_product_edit);
        tvProduct.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvProduct.setTextColor(getColor(R.color.common_black));


        tvSelling.setText(R.string.staff_selling);
        tvSelling.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvSelling.setTextColor(getColor(R.color.common_black));

        tvBadInventory.setText(R.string.staff_minus_bad_inventory);
        tvBadInventory.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvBadInventory.setTextColor(getColor(R.color.common_black));


        tvAddInventory.setText(R.string.staff_add_inventory);
        tvAddInventory.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvAddInventory.setTextColor(getColor(R.color.common_black));

    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(R.string.staff_shops);
        getTvRight().setBackground(null);
        getTvRight().setText(R.string.common_save);
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putShopAuthority();
            }
        });
    }


    private String getAuthCodeString() {
        Map<String, String> shop = null;
        Map<String, String> product = null;
        Map<String, String> selling = null;
        Map<String, String> badInventory = null;
        Map<String, String> addInventory = null;

        if (swShop.isChecked()) {
            shop = new HashMap<>();
            shop.put(JsonKey.STAFF_DEPARTMENT_ID, String.valueOf(swShop.getTag()));
            shop.put(JsonKey.STAFF_DEPARTMENT_RELEVANCE, Constants.STR_EMPTY);
        }
        if (swProduct.isChecked()) {
            product = new HashMap<>();
            product.put(JsonKey.STAFF_DEPARTMENT_ID, String.valueOf(swProduct.getTag()));
            product.put(JsonKey.STAFF_DEPARTMENT_RELEVANCE, Constants.STR_EMPTY);
        }

        if (swSelling.isChecked()) {
            selling = new HashMap<>();
            selling.put(JsonKey.STAFF_DEPARTMENT_ID, String.valueOf(swSelling.getTag()));
            selling.put(JsonKey.STAFF_DEPARTMENT_RELEVANCE, Constants.STR_EMPTY);
        }

        if (swBadInventory.isChecked()) {
            badInventory = new HashMap<>();
            badInventory.put(JsonKey.STAFF_DEPARTMENT_ID, String.valueOf(swBadInventory.getTag()));
            badInventory.put(JsonKey.STAFF_DEPARTMENT_RELEVANCE, Constants.STR_EMPTY);
        }

        if (swAddInventory.isChecked()) {
            addInventory = new HashMap<>();
            addInventory.put(JsonKey.STAFF_DEPARTMENT_ID, String.valueOf(swAddInventory.getTag()));
            addInventory.put(JsonKey.STAFF_DEPARTMENT_RELEVANCE, Constants.STR_EMPTY);
        }

        JSONArray array = new JSONArray();
        if (shop != null) {
            array.put(new JSONObject(shop));
        }

        if (product != null) {
            array.put(new JSONObject(product));
        }

        if (selling != null) {
            array.put(new JSONObject(selling));
        }

        if (badInventory != null) {
            array.put(new JSONObject(badInventory));
        }

        if (addInventory != null) {
            array.put(new JSONObject(addInventory));
        }

        return array.toString();
    }

    private void putShopAuthority() {

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
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShopsAuthorityFragment.this) {
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
        hh.startPut(getActivity(), ApiManager.HttpApi.StaffShopAuthorityPut, params);
    }
}
