/**
 * Project Name: itee
 * File Name:  ReserveFeeFragment.java
 * Package Name: cn.situne.itee.fragment.shops
 * Date:   2015-03-05
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.fragment.shops;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonAgentsPricingListGet;
import cn.situne.itee.manager.jsonentity.JsonShopRentalProductGet;
import cn.situne.itee.view.IteeMoneyEditText;

/**
 * ClassName:ReserveFeeFragment <br/>
 * Function: ReserveFee. <br/>
 * Date: 2015-03-05 <br/>
 * UI:05-2-1
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class ShopsReserveFeeFragment extends BaseFragment {


    private LinearLayout bodyLayout;

    private List<JsonShopRentalProductGet.DataList> dataList;

    private List<EditText> dataListView;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_shops_reserve_fee;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        dataListView = new ArrayList<>();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String rentalShopData = bundle.getString(TransKey.SHOP_RENTAL_SHOP_DATA);

            try {
                JSONObject jsonObject = new JSONObject(rentalShopData);
                JsonShopRentalProductGet jsonShopRentalProductGet = new JsonShopRentalProductGet(jsonObject);
                dataList = jsonShopRentalProductGet.getDataList();
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }

        }
        if (dataList == null) dataList = new ArrayList<>();

        bodyLayout = (LinearLayout) rootView.findViewById(R.id.ll_body);

        for (JsonShopRentalProductGet.DataList data : dataList) {
            addView(data);
        }
    }


    // get detail Line
    private View getLine() {
        return getLine(1, R.color.common_separator_gray);
    }

    // get line  height  color
    private View getLine(int height, int color) {
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        View viewSeparator = new View(getActivity());
        viewSeparator.setLayoutParams(lineParams);
        viewSeparator.setBackgroundColor(getResources().getColor(color));
        return viewSeparator;
    }

    private void addView(JsonShopRentalProductGet.DataList data) {
        LinearLayout.LayoutParams rlLayoutParams = new LinearLayout.LayoutParams(ListView.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));

        RelativeLayout rlLayout = new RelativeLayout(getBaseActivity());
        rlLayout.setLayoutParams(rlLayoutParams);

        RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        iconParams.addRule(RelativeLayout.CENTER_VERTICAL);
        ImageView icon = new ImageView(getActivity());
        icon.setLayoutParams(iconParams);
        icon.setBackgroundResource(getIconRes(data.getPdPicId()));
        icon.setId(View.generateViewId());
        rlLayout.addView(icon);

        RelativeLayout.LayoutParams tvPdNameParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvPdNameParams.addRule(RelativeLayout.RIGHT_OF, icon.getId());
        tvPdNameParams.addRule(RelativeLayout.CENTER_VERTICAL);
        TextView tvPdName = new TextView(getActivity());
        tvPdName.setText(R.string.shop_setting_caddie);
        tvPdName.setTextColor(getResources().getColor(R.color.common_black));
        tvPdName.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvPdName.setLayoutParams(tvPdNameParams);
        tvPdName.setText(data.getProductName());
        rlLayout.addView(tvPdName);
        rlLayout.setPadding(getActualWidthOnThisDevice(40), 0, getActualWidthOnThisDevice(40), 0);


        RelativeLayout.LayoutParams edPdMoneyParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(320), RelativeLayout.LayoutParams.WRAP_CONTENT);
        edPdMoneyParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        edPdMoneyParams.addRule(RelativeLayout.CENTER_VERTICAL);

        IteeMoneyEditText edPdMoney = new IteeMoneyEditText(this);
        edPdMoney.setGravity(Gravity.END);

        edPdMoney.setTag(data.getPdId());
        edPdMoney.setLayoutParams(edPdMoneyParams);
        edPdMoney.setText(AppUtils.getCurrentCurrency(getActivity()) + Constants.STR_SPACE + Utils.get2DigitDecimalString(data.getBookingFee()));
        rlLayout.addView(edPdMoney);
        dataListView.add(edPdMoney);
        bodyLayout.addView(rlLayout);
        bodyLayout.addView(getLine());
    }

    private int getIconRes(int pdPicId) {
        int res = R.drawable.icon_shops_caddie;
        switch (pdPicId) {

            case 1:
                res = R.drawable.icon_shops_caddie;
                break;
            case 2:
                res = R.drawable.icon_shops_cart;
                break;
            case 3:
                res = R.drawable.icon_shops_clubs;
                break;
            case 4:
                res = R.drawable.icon_shops_shoes;
                break;
            case 5:
                res = R.drawable.icon_shops_trolley;
                break;
            case 6:
                res = R.drawable.icon_shops_umbrella;
                break;
            case 7:
                res = R.drawable.icon_shops_towel;
                break;

            default:
                break;
        }
        return res;
    }

    private void bookingFeePut() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOP_05_BOOKING_FEE_LIST, getBookingFeeStr());
        HttpManager<JsonAgentsPricingListGet> hh = new HttpManager<JsonAgentsPricingListGet>(ShopsReserveFeeFragment.this) {
            @Override
            public void onJsonSuccess(JsonAgentsPricingListGet jo) {
                Integer returnCode = jo.getReturnCode();

                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    Utils.hideKeyboard(getActivity());
                    doBackWithRefresh();
                }
                Utils.showShortToast(getActivity(), msg);
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
        hh.startPut(this.getActivity(), ApiManager.HttpApi.ShopsBookingFeePut, params);
    }

    private String getBookingFeeStr() {
        JSONArray array = new JSONArray();
        for (EditText ed : dataListView) {
            Map<String, String> bookingFee = new HashMap<>();
            String bookingFeeOfEd = AppUtils.removeCurrencySymbol(ed.getText().toString(), getActivity());
            bookingFee.put(ApiKey.SHOP_05_BOOKING_FEE_LIST_ID, String.valueOf(ed.getTag()));

            bookingFee.put(ApiKey.SHOP_05_BOOKING_FEE_LIST_VALUE, bookingFeeOfEd);

            JSONObject itemObject = new JSONObject(bookingFee);
            array.put(itemObject);
        }


        return array.toString();
    }

    @Override
    protected void setDefaultValueOfControls() {


    }

    @Override
    protected void setListenersOfControls() {

    }

    @Override
    protected void setLayoutOfControls() {


    }

    @Override
    protected void setPropertyOfControls() {

    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getString(R.string.shop_setting_reserve_fee));
        getTvRight().setText(getString(R.string.shop_setting_ok));
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookingFeePut();
            }
        });
    }
}
