/**
 * Project Name: itee
 * File Name:	 ShopsCaddieEditFragmemt.java
 * Package Name: cn.situne.itee.fragment.shops
 * Date:		 2015-03-30
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.shops;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.activity.ScanQrCodeActivity;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonCaddiePriceGet;
import cn.situne.itee.view.IteeButton;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeMoneyEditText;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.SelectTimePopupWindow;

/**
 * ClassName:ShopsCaddieEditFragment <br/>
 * Function: caddie edit. <br/>
 * Date: 2015-03-30 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class ShopsCaddieEditFragment extends BaseFragment {

    private RelativeLayout rlTileLayout;
    private LinearLayout llBody;
    private IteeTextView leftValue;
    private ArrayList<JsonCaddiePriceGet.DataList> dataList;

    private ArrayList<ItemLayout> itemList;

    private String caddieId;
    private int selectRow;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_shops_caddie_edit;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(final View rootView) {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            caddieId = bundle.getString(TransKey.SHOPS_CADDIE_ID);
        }
        LinearLayout.LayoutParams rlTitleParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
        rlTileLayout = (RelativeLayout) rootView.findViewById(R.id.rl_tileLayout);
        rlTileLayout.setLayoutParams(rlTitleParams);

        llBody = (LinearLayout) rootView.findViewById(R.id.ll_body);
        RelativeLayout.LayoutParams leftKeyParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        leftKeyParams.leftMargin = getActualWidthOnThisDevice(40);
        IteeTextView leftKey = new IteeTextView(getBaseActivity());
        leftKey.setText(R.string.shop_setting_return_time);
        leftKey.setLayoutParams(leftKeyParams);
        leftKey.setGravity(Gravity.CENTER_VERTICAL);
        leftValue = new IteeTextView(getBaseActivity());
        rlTileLayout.setVisibility(View.GONE);

        leftValue.setTextColor(getColor(R.color.common_black));
        RelativeLayout.LayoutParams leftValueParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        leftValueParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        leftValueParams.rightMargin = getActualWidthOnThisDevice(40);
        leftValue.setLayoutParams(leftValueParams);
        leftValue.setGravity(Gravity.CENTER_VERTICAL);
        rlTileLayout.addView(leftKey);
        rlTileLayout.addView(leftValue);

        itemList = new ArrayList<>();

        getCaddiePrice();

    }

    private void addItemCaddie(JsonCaddiePriceGet.DataList data) {
        LinearLayout ll = new LinearLayout(getBaseActivity());
        ll.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams rl1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(80));
        RelativeLayout rlLevel = new RelativeLayout(getBaseActivity());

        RelativeLayout.LayoutParams tv1KeyParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(80));
        tv1KeyParams.addRule(RelativeLayout.CENTER_VERTICAL);

        IteeTextView tvLevelKey = new IteeTextView(getActivity());
        tvLevelKey.setText(data.getLevel());
        tvLevelKey.setTextColor(getColor(R.color.common_blue));
        tvLevelKey.setLayoutParams(tv1KeyParams);
        rlLevel.addView(tvLevelKey);
        rlLevel.setPadding(getActualWidthOnThisDevice(40), 0, getActualWidthOnThisDevice(40), 0);


        rlLevel.setLayoutParams(rl1Params);
        LinearLayout.LayoutParams rl2Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
        RelativeLayout rlPrice = new RelativeLayout(getBaseActivity());

        RelativeLayout.LayoutParams tv2KeyParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        IteeTextView tvPriceKey = new IteeTextView(getActivity());
        tvPriceKey.setLayoutParams(tv2KeyParams);
        tvPriceKey.setText(R.string.shop_setting_price);
        rlPrice.addView(tvPriceKey);
        rlPrice.setLayoutParams(rl2Params);
        rlPrice.setPadding(getActualWidthOnThisDevice(40), 0, getActualWidthOnThisDevice(20), 0);

        RelativeLayout.LayoutParams etPriceParams
                = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(320), MATCH_PARENT);
        etPriceParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        etPriceParams.addRule(RelativeLayout.CENTER_VERTICAL);

        IteeMoneyEditText etPrice = new IteeMoneyEditText(this);
        etPrice.setLayoutParams(etPriceParams);

        if (Utils.isStringNullOrEmpty(data.getPrice())) {
            etPrice.setText(Constants.STR_EMPTY);
        } else {
            etPrice.setValue(data.getPrice());
        }
        etPrice.setHint(R.string.shop_setting_price);
        etPrice.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);

        rlPrice.addView(etPrice);
        RelativeLayout.LayoutParams tv3KeyParams
                = new RelativeLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        LinearLayout.LayoutParams rl3Params = new LinearLayout.LayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(100));
        RelativeLayout rlCode = new RelativeLayout(getBaseActivity());
        rlCode.setLayoutParams(rl3Params);

        IteeTextView tvCodeKey = new IteeTextView(getActivity());
        tvCodeKey.setText(R.string.shop_setting_code);
        tvCodeKey.setLayoutParams(tv3KeyParams);
        tvCodeKey.setId(View.generateViewId());

        rlCode.addView(tvCodeKey);
        RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(getActualHeightOnThisDevice(50), getActualHeightOnThisDevice(50));
        btnParams.addRule(RelativeLayout.CENTER_VERTICAL);
        btnParams.leftMargin = getActualWidthOnThisDevice(15);
        btnParams.addRule(RelativeLayout.RIGHT_OF, tvCodeKey.getId());
        IteeButton btn = new IteeButton(getBaseActivity());
        btn.setLayoutParams(btnParams);
        btn.setTag(itemList.size());

        btn.setBackgroundResource(R.drawable.icon_shops_code);
        btn.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                selectRow = (int) v.getTag();
                Intent intent = new Intent();
                intent.setClass(getBaseActivity(), ScanQrCodeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, ScanQrCodeActivity.SCANNING_GREQUEST_CODE);
            }
        });
        rlCode.addView(btn);

        RelativeLayout.LayoutParams etCodeParams
                = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(320), RelativeLayout.LayoutParams.MATCH_PARENT);
        etCodeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        etCodeParams.addRule(RelativeLayout.CENTER_VERTICAL);
        IteeEditText etCodeValue = new IteeEditText(this);
        etCodeValue.setLayoutParams(etCodeParams);
        etCodeValue.setText(data.getCode());
        etCodeValue.setTextColor(getColor(R.color.common_gray));
        etCodeValue.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        etCodeValue.setHint(getString(R.string.shop_setting_code));

        rlCode.addView(etCodeValue);

        rlCode.setPadding(getActualWidthOnThisDevice(40), 0, getActualWidthOnThisDevice(20), 0);

        LinearLayout.LayoutParams vParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(30));
        View v = new View(getBaseActivity());
        v.setLayoutParams(vParams);
        v.setBackgroundColor(getColor(R.color.common_light_gray));

        ll.addView(v);
        ll.addView(AppUtils.getSeparatorLine(this));
        ll.addView(rlLevel);
        ll.addView(AppUtils.getSeparatorLine(this));
        ll.addView(rlPrice);
        ll.addView(AppUtils.getSeparatorLine(this));
        ll.addView(rlCode);
        ll.addView(AppUtils.getSeparatorLine(this));

        ItemLayout item = new ItemLayout();
        item.setEdPrice(etPrice);
        item.setEdCode(etCodeValue);
        item.setData(data);
        ll.setBackgroundColor(getColor(R.color.common_white));

        itemList.add(item);
        llBody.addView(ll);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ScanQrCodeActivity.SCANNING_GREQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String qrCode = bundle.getString(TransKey.QR_CODE);

                    itemList.get(selectRow).getEdCode().setText(qrCode);
                    //  upView.setEdCodeText();
                }
                break;
        }
    }

    private void setInitView() {

        if (dataList.size() > 0) {

            leftValue.setText(AppUtils.getStringHmWithMinute(dataList.get(0).getReturn_time()));
            rlTileLayout.setVisibility(View.VISIBLE);
        }

        for (JsonCaddiePriceGet.DataList data : dataList) {

            addItemCaddie(data);

        }

    }

    private void getCaddiePrice() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_CADDIE_ID, caddieId);

        HttpManager<JsonCaddiePriceGet> hh = new HttpManager<JsonCaddiePriceGet>(ShopsCaddieEditFragment.this) {
            @Override
            public void onJsonSuccess(JsonCaddiePriceGet jo) {
                Integer returnCode = jo.getReturnCode();

                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    dataList = jo.getDataList();
                    setInitView();
                } else {
                    Utils.showShortToast(getActivity(), msg);
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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.ShopCaddiePriceGet, params);

    }

    private String getPriceList() {

        JSONArray array = new JSONArray();

        for (ItemLayout item : itemList) {
            Map<String, String> priceItem = new HashMap<>();
            priceItem.put(ApiKey.SHOPS_ID, String.valueOf(item.getData().getId()));
            priceItem.put(ApiKey.SHOPS_NAME, item.getData().getLevel());
            priceItem.put(ApiKey.SHOPS_CODE, item.getEdCode().getText().toString());

            priceItem.put(ApiKey.SHOPS_PRICE, item.getEdPrice().getEmptyValue());
            array.put(new JSONObject(priceItem));
        }

        JSONObject obj = new JSONObject();

        try {
            obj.put(ApiKey.SHOPS_RETURN_TIME, String.valueOf(AppUtils.getMinuteWithHmString(leftValue.getText().toString())));
            obj.put(ApiKey.SHOPS_PRICE, array);
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

        return obj.toString();
    }

    private void putPriceList() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_CADDIE_ID, String.valueOf(caddieId));
        params.put(ApiKey.SHOPS_PRICE_LIST, getPriceList());

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShopsCaddieEditFragment.this) {
            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                Integer returnCode = jo.getReturnCode();

                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
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
        hh.startPut(this.getActivity(), ApiManager.HttpApi.ShopCaddiePricePut, params);
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        rlTileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.hideKeyboard(getActivity());
                String str = leftValue.getText().toString();
                str = str.replace("h", Constants.STR_COLON);
                str = str.replace("m", Constants.STR_COLON);
                String[] times = str.split(Constants.STR_COLON);

                final SelectTimePopupWindow popupWindow = new SelectTimePopupWindow(getActivity(), times, 1);
                popupWindow.showAtLocation(getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                popupWindow.btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        leftValue.setText(popupWindow.wheelViewHour.getCurrentItem() + "h" + popupWindow.wheelViewMin.getCurrentItem() + "m");
                        popupWindow.dismiss();
                    }
                });
            }
        });
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
        getTvLeftTitle().setText(R.string.shop_setting_caddie);
        getTvRight().setText(R.string.shop_setting_ok);
        getTvRight().setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                putPriceList();
            }
        });
    }

    class ItemLayout {
        private IteeMoneyEditText edPrice;
        private IteeEditText edCode;
        private JsonCaddiePriceGet.DataList data;

        public JsonCaddiePriceGet.DataList getData() {
            return data;
        }

        public void setData(JsonCaddiePriceGet.DataList data) {
            this.data = data;
        }

        public IteeMoneyEditText getEdPrice() {
            return edPrice;
        }

        public void setEdPrice(IteeMoneyEditText edPrice) {
            this.edPrice = edPrice;
        }

        public IteeEditText getEdCode() {
            return edCode;
        }

        public void setEdCode(IteeEditText edCode) {
            this.edCode = edCode;
        }
    }
}