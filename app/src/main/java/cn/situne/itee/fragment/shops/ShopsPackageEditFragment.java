/**
 * Project Name: itee
 * File Name:	 ShopsPackageEditFragment.java
 * Package Name: cn.situne.itee.fragment.shop
 * Date:		 2015-03-31
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.shops;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

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
import cn.situne.itee.activity.ScanQrCodeActivity;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.ShopsProduct;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonPackageGet;
import cn.situne.itee.view.IteeMoneyEditText;
import cn.situne.itee.view.IteeRedDeleteButton;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.ShopsPackageEditUpView;
import cn.situne.itee.view.SwipeListView;

/**
 * ClassName:ShopsPackageEditFragment <br/>
 * Function: 新增 编辑 套餐. <br/>
 * UI:  05-04-01
 * Date: 2015-03-31 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class ShopsPackageEditFragment extends BaseEditFragment {

    private int packageId;
    private String packageChildItemStr;
    private ArrayList<JsonPackageGet.DataItem> baseDataSource;
    private ArrayList<JsonPackageGet.DataItem> showBodyViewDataSource;
    private ArrayList<ListViewItem> bodyViews;
    private LinearLayout llBody;
    private LinearLayout llFooter;
    private String greenId;
    private ShopsPackageEditUpView upView;
    private IteeRedDeleteButton btnDelete;
    private IteeTextView totalTextView;
    private String unlimitedFlag;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_shops_package_edit;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        btnDelete = new IteeRedDeleteButton(getBaseActivity());

        Bundle bundle = getArguments();
        baseDataSource = new ArrayList<>();
        showBodyViewDataSource = new ArrayList<>();
        bodyViews = new ArrayList<>();
        String packageName = Constants.STR_EMPTY;
        int packageQty = 0;
        String packageCode = Constants.STR_EMPTY;
        if (bundle != null) {
            setFragmentMode(BaseEditFragment.FragmentMode.valueOf(bundle.getInt(TransKey.COMMON_FRAGMENT_MODE)));
            packageId = bundle.getInt(TransKey.SHOPS_PRODUCT_PACKAGE_ID);
            packageName = bundle.getString(TransKey.SHOPS_PRODUCT_PACKAGE_NAME);
            packageCode = bundle.getString(TransKey.SHOPS_PRODUCT_PACKAGE_CODE);
            packageQty = bundle.getInt(TransKey.SHOPS_PRODUCT_PACKAGE_QTY);
            packageChildItemStr = bundle.getString(TransKey.SHOPS_PRODUCT_PACKAGE_CHILD_ITEM);
            greenId = bundle.getString(TransKey.COMMON_GREEN_ID);
            unlimitedFlag = bundle.getString(TransKey.UNLIMITED_FLAG);
        }

        if (packageChildItemStr != null) {
            try {
                JSONArray jsPackageChild = new JSONArray(packageChildItemStr);
                baseDataSource.clear();
                showBodyViewDataSource.clear();
                for (int j = 0; j < jsPackageChild.length(); j++) {
                    JsonPackageGet.DataItem dataItem = new JsonPackageGet.DataItem();

                    JSONObject jsProductObject = jsPackageChild.getJSONObject(j);

                    dataItem.setId(Utils.getStringFromJsonObjectWithKey(jsProductObject, JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_ID));
                    dataItem.setNumber(Utils.getIntegerFromJsonObjectWithKey(jsProductObject, JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_NUMBER));
                    dataItem.setPdName(Utils.getStringFromJsonObjectWithKey(jsProductObject, JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_PD_NAME));
                    dataItem.setPrice(Utils.getDoubleFromJsonObjectWithKey(jsProductObject, JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_PRICE));
                    dataItem.setProductAttr(Utils.getStringFromJsonObjectWithKey(jsProductObject, JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_PRODUCT_ATTR));
                    dataItem.setProductAttrId(Utils.getStringFromJsonObjectWithKey(jsProductObject, JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_PRODUCT_ATTR_ID));
                    dataItem.setProductId(Utils.getStringFromJsonObjectWithKey(jsProductObject, JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_PRODUCT_ID));
                    dataItem.setType(Utils.getIntegerFromJsonObjectWithKey(jsProductObject, JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_TYPE));

                    dataItem.setDiscountPrice(Utils.getDoubleFromJsonObjectWithKey(jsProductObject, JsonKey.SHOPS_CHOOSE_PRODUCT_PACKAGE_PRODUCT_DISCOUNT_PRICE));
                    dataItem.setTypeTagId(Utils.getStringFromJsonObjectWithKey(jsProductObject, JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_TYPE_TAG_ID));
                    baseDataSource.add(dataItem);
                }

                showBodyViewDataSource.clear();
                showBodyViewDataSource = getNewPackageData(baseDataSource);

            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }

        llBody = (LinearLayout) rootView.findViewById(R.id.ll_body);
        llFooter = (LinearLayout) rootView.findViewById(R.id.ll_footer);
        LinearLayout llHeader = (LinearLayout) rootView.findViewById(R.id.ll_header);
        upView = new ShopsPackageEditUpView(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                    setDisMoney();
                    Bundle bundle = new Bundle();
                    ArrayList<String> packageProducts = new ArrayList<>();
                    for (JsonPackageGet.DataItem dataItem : showBodyViewDataSource) {
                        ShopsProduct shopsProduct = new ShopsProduct();
                        if (Utils.isStringNotNullOrEmpty(dataItem.getProductAttr())) {
                            shopsProduct.setProductName(dataItem.getPdName() + Constants.STR_BRACKETS_START
                                    + dataItem.getProductAttr() + Constants.STR_BRACKETS_END);
                        } else {
                            shopsProduct.setProductName(dataItem.getPdName());
                        }
                        shopsProduct.setAttrId(dataItem.getProductAttrId());
                        shopsProduct.setProductId(dataItem.getProductId());
                        shopsProduct.setProductNumber(dataItem.getNumber());
                        shopsProduct.setProductPrice(String.valueOf(dataItem.getPrice()));
                        shopsProduct.setTypeTagId(dataItem.getTypeTagId());

                        ListViewItem item = findItemOfProductId(dataItem.getProductId());
                        if (item != null) {
                            shopsProduct.setDiscountPrice(item.getEdNowCost().getValue());
                        }
                        packageProducts.add(Utils.getStringFromObject(shopsProduct));
                    }
                    bundle.putStringArrayList(TransKey.SHOPS_PRODUCT_LIST, packageProducts);
                    bundle.putInt(TransKey.CHOOSE_MODE, ShopsChooseProductFragment.ChooseProductMode.ModePackage.value());
                    bundle.putString(TransKey.SHOPS_FRAGMENT_NAME, ShopsPackageEditFragment.class.getName());
                    bundle.putString(TransKey.COMMON_GREEN_ID, greenId);
                    push(ShopsChooseProductFragment.class, bundle);

                }

            }
        }, unlimitedFlag);
        upView.setTag(SwipeListView.DISABLE_RIGHT);
        upView.setFragmentMode(getFragmentMode());
        upView.setViewText(packageName, packageCode, packageQty);

        llHeader.addView(upView);
        addFooterView();
        refreshBody();
    }

    private void refreshBody() {
        llBody.removeAllViews();
        bodyViews.clear();
        for (JsonPackageGet.DataItem showItem : showBodyViewDataSource) {
            ListViewItem item = new ListViewItem(ShopsPackageEditFragment.this);
            item.setBackgroundColor(getColor(R.color.common_white));
            item.refresh(showItem, new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        if (keyCode == KeyEvent.KEYCODE_ENTER || KeyEvent.KEYCODE_BACK == keyCode) {
                            changeTotalMoney();
                            upView.setFocusableInTouchMode(true);
                            upView.setFocusable(true);
                            Utils.hideKeyboard(getBaseActivity());
                        }
                    }
                    return false;
                }
            });
            llBody.addView(item);
            bodyViews.add(item);
        }
        changeTotalMoney();
    }

    private void changeTotalMoney() {
        totalTextView.setText(getString(R.string.common_total) + Constants.STR_COLON + Constants.STR_SPACE
                + AppUtils.getCurrentCurrency(getActivity()) + Constants.STR_SPACE
                + Utils.get2DigitDecimalString(String.valueOf(getTotal())));
    }

    @Override
    protected void setDefaultValueOfControls() {
        if (getFragmentMode() != FragmentMode.FragmentModeAdd) {
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void setListenersOfControls() {
        upView.getCodeBtn().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                    Intent intent = new Intent();
                    intent.setClass(getBaseActivity(), ScanQrCodeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, ScanQrCodeActivity.SCANNING_GREQUEST_CODE);
                }
            }
        });
    }

    @Override
    protected void setLayoutOfControls() {
        LinearLayout.LayoutParams btnDeleteLayoutParams = (LinearLayout.LayoutParams) btnDelete.getLayoutParams();
        btnDeleteLayoutParams.width = AppUtils.getLargerButtonWidth(this);
        btnDeleteLayoutParams.height = AppUtils.getLargerButtonHeight(this);
        btnDeleteLayoutParams.topMargin = getActualHeightOnThisDevice(30);
        btnDeleteLayoutParams.bottomMargin = getActualHeightOnThisDevice(30);
        btnDelete.setLayoutParams(btnDeleteLayoutParams);
    }

    @Override
    protected void setPropertyOfControls() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ScanQrCodeActivity.SCANNING_GREQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String qrCode = bundle.getString(TransKey.QR_CODE);
                    upView.setEdCodeText(qrCode);
                }
                break;
        }
    }


    @Override
    protected void reShowWithBackValue() {
        Bundle bundle = getReturnValues();
        if (bundle != null) {
            List<String> addProducts = bundle.getStringArrayList(TransKey.CHOOSE_PRODUCT_LIST);
            showBodyViewDataSource.clear();
            if (addProducts != null) {
                for (String itemStr : addProducts) {
                    ShopsProduct shopsProduct = (ShopsProduct) Utils.getObjectFromString(itemStr);
                    JsonPackageGet.DataItem dataItem = new JsonPackageGet.DataItem();

                    dataItem.setNumber(shopsProduct.getProductNumber());
                    dataItem.setPdName(shopsProduct.getProductName());
                    String priceStr = shopsProduct.getProductPrice();

                    if (priceStr.contains(Constants.STR_WAVE)) {
                        priceStr = priceStr.split(Constants.STR_WAVE)[0];
                    }
                    dataItem.setPrice(Double.parseDouble(priceStr));
                    dataItem.setProductId(shopsProduct.getProductId());
                    dataItem.setProductAttrId(shopsProduct.getAttrId());
                    dataItem.setTypeTagId(shopsProduct.getTypeTagId());
                    dataItem.setDiscountPrice(Double.parseDouble(priceStr));

                    dataItem.setType(shopsProduct.getType());
                    showBodyViewDataSource.add(dataItem);
                }
            }

            refreshBody();
        }
    }

    private ListViewItem findItemOfProductId(String productId) {
        for (ListViewItem item : bodyViews) {
            if (productId.equals(item.getProductId())) {
                return item;
            }
        }
        return null;
    }

    private ListViewItem findItemOfProductId(String productId, String attrId) {
        for (ListViewItem item : bodyViews) {
            if (productId.equals(item.getProductId()) && attrId.equals(item.getAttrId())) {
                return item;
            }
        }
        return null;
    }


    private void setDisMoney() {
        for (ListViewItem item : bodyViews) {
            String disMoney = item.getEdNowCost().getValue();
            String productId = item.getProductId();
            for (JsonPackageGet.DataItem dataItem : baseDataSource) {
                if (productId.equals(item.getProductId())) {
                    if (Utils.isStringNotNullOrEmpty(disMoney)) {
                        dataItem.setDiscountPrice(Double.parseDouble(disMoney));
                    } else {
                        dataItem.setDiscountPrice(0);
                    }
                }
            }
        }
    }

    private double getTotal() {
        double totalMoney = 0;
        for (ListViewItem item : bodyViews) {
            double itemMoney = 0;
            try {
                itemMoney = Double.parseDouble(item.getEdNowCost().getValue());
                itemMoney = itemMoney * item.getmNum();
            } catch (NumberFormatException e) {
                Utils.log(e.getMessage());
            }
            totalMoney += itemMoney;
        }
        return totalMoney;
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();

        getTvLeftTitle().setText(R.string.shop_setting_product_edit_package);

        ViewGroup.LayoutParams layoutParams = getTvLeftTitle().getLayoutParams();
        layoutParams.width = (int) (getScreenWidth() * 0.7);
        getTvLeftTitle().setLayoutParams(layoutParams);

        if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
            getTvRight().setBackgroundResource(R.drawable.icon_common_edit);
            btnDelete.setVisibility(View.GONE);
        } else {
            getTvRight().setText(R.string.common_ok);
            getTvRight().setBackground(null);
        }

        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
                btnDelete.setEnabled(true);

                if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
                    btnDelete.setVisibility(View.VISIBLE);
                    getTvRight().setBackground(null);
                    getTvRight().setText(R.string.common_ok);
                    setFragmentMode(FragmentMode.FragmentModeEdit);
                    upView.setFragmentMode(getFragmentMode());

                    for (ListViewItem item : bodyViews) {
                        item.getEdNowCost().setEnabled(true);
                    }
                } else {
                    if (doCheck()) {
                        setDisMoney();
                        v.requestFocus();
                        v.setFocusable(true);
                        v.setFocusableInTouchMode(true);
                        submitPackageData();
                    }
                }

            }
        });

    }


    private String getPackageProductList() {

        JSONArray array1 = new JSONArray();
        if (Utils.isListNotNullOrEmpty(showBodyViewDataSource)) {
            for (JsonPackageGet.DataItem dataItem : showBodyViewDataSource) {
                HashMap<String, String> packageProductData = new HashMap<>();
                packageProductData.put(JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_PRODUCT_ID, String.valueOf(dataItem.getProductId()));
                packageProductData.put(JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_ATTR_ID, dataItem.getProductAttrId() != null ?
                        dataItem.getProductAttrId() : Constants.STR_EMPTY);
                packageProductData.put(JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_TYPE, String.valueOf(dataItem.getType()));
                ListViewItem item;
                if (dataItem.getProductAttrId() == null
                        || Constants.STR_0.equals(dataItem.getProductAttrId())
                        || Constants.STR_EMPTY.equals(dataItem.getProductAttrId())) {
                    item = findItemOfProductId(dataItem.getProductId());
                } else {
                    item = findItemOfProductId(dataItem.getProductId(), dataItem.getProductAttrId());
                }

                packageProductData.put(JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_NUMBER, String.valueOf(dataItem.getNumber()));
                if (item != null) {
                    packageProductData.put(JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_PRICE, item.getEdNowCost().getValue());
                }
                array1.put(new JSONObject(packageProductData));
            }
        }
        return array1.toString();

    }

    private void submitPackageData() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_PACKAGE_ID, String.valueOf(packageId));
        params.put(ApiKey.SHOPS_PACKAGE_NAME, upView.getPackageName());
        if (Utils.isStringNullOrEmpty(upView.getPackageQty())) {
            params.put(ApiKey.SHOPS_PACKAGE_QTY, "0");
        } else {
            params.put(ApiKey.SHOPS_PACKAGE_QTY, upView.getPackageQty());
        }
        if (Utils.isStringNotNullOrEmpty(upView.getPackageCode())) {
            params.put(ApiKey.SHOPS_PACKAGE_CODE, upView.getPackageCode());
        }
        params.put(ApiKey.SHOPS_PACKAGE_PRODUCT_LIST, getPackageProductList());
        params.put(ApiKey.SHOPS_PACKAGE_PRICE, String.valueOf(getTotal()));
        params.put(ApiKey.SHOPS_UNLIMITED_FLAG, upView.getUnlimitedFlag());
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShopsPackageEditFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();

                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY || returnCode == Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY) {
                    doBack();
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

        postPackage(hh, params);

    }


    private void postPackage(HttpManager<BaseJsonObject> hh, Map<String, String> params) {
        hh.start(getActivity(), ApiManager.HttpApi.ShopsPackagePost, params);
    }

    private void addFooterView() {
        LinearLayout footLayout = new LinearLayout(getBaseActivity());
        footLayout.setOrientation(LinearLayout.VERTICAL);
        footLayout.setGravity(Gravity.CENTER);

        RelativeLayout totalLayout = new RelativeLayout(getBaseActivity());
        RelativeLayout.LayoutParams totalTextViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, getActualHeightOnThisDevice(90));
        totalTextViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        totalTextViewParams.rightMargin = getActualWidthOnThisDevice(40);
        totalTextView = new IteeTextView(getBaseActivity());
        totalTextView.setLayoutParams(totalTextViewParams);
        totalLayout.addView(totalTextView);

        totalTextView.setTextColor(getColor(R.color.common_red));
        totalTextView.setTextSize(Constants.FONT_SIZE_LARGER);
        totalTextView.setGravity(Gravity.CENTER_VERTICAL);
        footLayout.addView(totalLayout);
        totalLayout.setBackgroundColor(getColor(R.color.common_white));
        footLayout.addView(AppUtils.getSeparatorLine(ShopsPackageEditFragment.this));
        footLayout.addView(btnDelete);
        footLayout.setTag(SwipeListView.DISABLE_RIGHT);
        llFooter.addView(footLayout);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePackageProduct();
            }
        });

        if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
            btnDelete.setEnabled(false);
        }
    }


    private void deletePackageProduct() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_PACKAGE_ID, String.valueOf(packageId));


        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShopsPackageEditFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                Integer returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20403_DELETE_SUCCESSFULLY) {
                    getBaseActivity().doBackWithRefresh();
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }

            }

            @Override
            public void onJsonError(VolleyError error) {
                Utils.showShortToast(getActivity(), String.valueOf(error));
            }
        };
        hh.startDelete(getActivity(), ApiManager.HttpApi.ShopsPackageDel, params);
    }

    private boolean doCheck() {
        boolean res = true;
        if (Utils.isStringNullOrEmpty(upView.getPackageName())) {
            Utils.showShortToast(getActivity(), getResources().getString(R.string.shop_setting_package_name));
            res = false;
        }

        if (Constants.UNLIMITED_FLAG_OFF.equals(upView.getUnlimitedFlag())
                && Utils.isStringNullOrEmpty(upView.getPackageQty())) {
            Utils.showShortToast(getActivity(), getResources().getString(R.string.shop_setting_enter_qty));
            res = false;
        }

        if (!Utils.isListNotNullOrEmpty(showBodyViewDataSource)) {
            Utils.showShortToast(getActivity(), getResources().getString(R.string.msg_please_choose_product));
            res = false;
        }
        return res;
    }

    private JsonPackageGet.DataItem findPackageItemForProductId(List<JsonPackageGet.DataItem> resList, String productId) {
        for (JsonPackageGet.DataItem item : resList) {
            if (item.getProductId().equals(productId)) {
                return item;
            }
        }
        return null;
    }

    private ArrayList<JsonPackageGet.DataItem> getNewPackageData(ArrayList<JsonPackageGet.DataItem> dataList) {
        ArrayList<JsonPackageGet.DataItem> resList = new ArrayList<>();
        for (JsonPackageGet.DataItem item : dataList) {
            JsonPackageGet.DataItem resItem = findPackageItemForProductId(resList, item.getProductId());
            if (resItem == null) {
                resItem = new JsonPackageGet.DataItem();
                resItem.setId(item.getId());
                resItem.setPrice(item.getPrice());
                resItem.setProductAttrId(item.getProductAttrId());
                resItem.setProductId(item.getProductId());
                resItem.setProductAttr(item.getProductAttr());
                resItem.setNumber(item.getNumber());
                resItem.setType(item.getType());
                resItem.setMaxPrice(item.getMaxPrice());
                resItem.setDiscountPrice(item.getDiscountPrice());
                resItem.setPdName(item.getPdName());
                resItem.setMinPrice(item.getPrice());
                resItem.setTypeTagId(item.getTypeTagId());
                resList.add(resItem);
            } else {
                if (greenId.equals(item.getProductId())) {
                    resList.add(item);
                } else {
                    resItem.setNumber(resItem.getNumber() + item.getNumber());//
                    resItem.setNumber(item.getNumber());
                    double itemPrice = item.getPrice();
                    if (itemPrice < resItem.getMinPrice()) {
                        resItem.setMaxPrice(resItem.getMinPrice());
                        resItem.setMinPrice(itemPrice);
                    }
                    if (itemPrice > resItem.getMinPrice()) {

                        if (resItem.getMaxPrice() < 0) {

                            resItem.setMaxPrice(itemPrice);
                        } else {
                            if (itemPrice > resItem.getMaxPrice()) {
                                resItem.setMaxPrice(itemPrice);
                            }
                        }
                    }
                    resItem.setProductAttrId(resItem.getProductAttrId() + Constants.STR_COMMA + item.getProductAttrId());
                }
            }
        }
        return resList;
    }


    class ListViewItem extends RelativeLayout {
        private int mNum;
        private String productId;

        private String attrId;
        private IteeTextView tvPdName;
        private IteeTextView tvPrice;
        private IteeTextView tvNum;
        private IteeMoneyEditText edNowCost;
        private BaseEditFragment mFragment;

        public ListViewItem(BaseEditFragment fragment) {
            super(fragment.getActivity());
            ListView.LayoutParams myParams = new ListView.LayoutParams(LayoutParams.WRAP_CONTENT, fragment.getActualHeightOnThisDevice(180));
            this.setLayoutParams(myParams);
            mFragment = fragment;
            initView(fragment);
        }

        public String getProductId() {
            return productId;
        }

        public int getmNum() {
            return mNum;
        }

        public String getAttrId() {
            return attrId;
        }

        public BaseEditFragment getmFragment() {
            return mFragment;
        }

        public void setmFragment(BaseEditFragment mFragment) {
            this.mFragment = mFragment;
        }

        public IteeMoneyEditText getEdNowCost() {
            return edNowCost;
        }

        private void refresh(JsonPackageGet.DataItem itemData, OnKeyListener moneyOnKeyListener) {
            productId = itemData.getProductId();
            attrId = itemData.getProductAttrId();
            if (Utils.isStringNotNullOrEmpty(itemData.getProductAttr())) {
                tvPdName.setText(itemData.getPdName() + Constants.STR_BRACKETS_START + itemData.getProductAttr() + Constants.STR_BRACKETS_END);
            } else {
                tvPdName.setText(itemData.getPdName());
            }

            Double disMoney = itemData.getDiscountPrice();

            edNowCost.setText(AppUtils.getCurrentCurrency(getActivity()) + Utils.get2DigitDecimalString(String.valueOf(disMoney)));
            edNowCost.setOnKeyListener(moneyOnKeyListener);
            edNowCost.setTextColor(getColor(R.color.common_gray));

            edNowCost.addTextChangedListener(new AppUtils.EditViewMoneyWatcher(edNowCost));
            edNowCost.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    changeTotalMoney();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

//            edNowCost.setOnFocusChangeListener(new OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View view, boolean b) {
//                    if (!b) {
//                        changeTotalMoney();
//                    }
//                }
//            });

            tvNum.setText(Constants.STR_MULTIPLY + String.valueOf(itemData.getNumber()));
            mNum = itemData.getNumber();
//            if (itemData.getType() == Constants.SHOPS_PACKAGE_TYPE_2) {
//                double minP = itemData.getMinPrice();
//                double maxP = itemData.getMaxPrice();
//                if (minP >= maxP) {
//                    tvPrice.setText(AppUtils.getCurrentCurrency(getActivity()) + Constants.STR_EMPTY + String.valueOf(minP));
//                } else {
//                    tvPrice.setText(AppUtils.getCurrentCurrency(getActivity()) + Constants.STR_EMPTY + String.valueOf(minP)
//                            + Constants.STR_SEPARATOR + AppUtils.getCurrentCurrency(getActivity()) + Constants.STR_EMPTY + String.valueOf(maxP));
//                }
//            } else {
            tvPrice.setText(AppUtils.getCurrentCurrency(getActivity()) + Constants.STR_EMPTY + String.valueOf(itemData.getPrice()));
            // }
            if (mFragment.getFragmentMode() == FragmentMode.FragmentModeBrowse) {
                edNowCost.setEnabled(false);
            } else {
                edNowCost.setEnabled(true);
            }
        }

        private void initView(final BaseFragment fragment) {

            RelativeLayout.LayoutParams llLeftLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            llLeftLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            LinearLayout llLeftLayout = new LinearLayout(fragment.getActivity());
            llLeftLayout.setOrientation(LinearLayout.VERTICAL);
            llLeftLayout.setLayoutParams(llLeftLayoutParams);
            llLeftLayout.setPadding(fragment.getActualWidthOnThisDevice(40), 0, 0, 0);

            RelativeLayout.LayoutParams tvPdNameParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            tvPdName = new IteeTextView(fragment);
            tvPdName.setLayoutParams(tvPdNameParams);
            tvPdName.setId(View.generateViewId());

            RelativeLayout.LayoutParams tvPriceParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            tvPriceParams.addRule(RelativeLayout.BELOW, tvPdName.getId());
            tvPrice = new IteeTextView(fragment);
            tvPrice.setLayoutParams(tvPriceParams);
            tvPrice.setId(View.generateViewId());

            RelativeLayout.LayoutParams tvNumParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            tvNumParams.addRule(RelativeLayout.BELOW, tvPrice.getId());
            tvNum = new IteeTextView(fragment);
            tvNum.setLayoutParams(tvNumParams);

            llLeftLayout.addView(tvPdName);
            llLeftLayout.addView(tvPrice);
            llLeftLayout.addView(tvNum);

            tvPdName.setTextSize(Constants.FONT_SIZE_LARGER);
            tvPdName.setTextColor(fragment.getColor(R.color.common_black));
            tvPrice.setTextSize(Constants.FONT_SIZE_NORMAL);
            tvPrice.setTextColor(fragment.getColor(R.color.common_gray));
            tvNum.setTextSize(Constants.FONT_SIZE_NORMAL);
            tvNum.setTextColor(fragment.getColor(R.color.common_blue));
            this.addView(llLeftLayout);

            RelativeLayout.LayoutParams edNowCostParams = new RelativeLayout.LayoutParams(fragment.getActualWidthOnThisDevice(300), LayoutParams.WRAP_CONTENT);
            edNowCostParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            edNowCostParams.addRule(RelativeLayout.CENTER_VERTICAL);
            edNowCostParams.rightMargin = fragment.getActualWidthOnThisDevice(40);
            edNowCost = new IteeMoneyEditText(fragment);
            edNowCost.setLayoutParams(edNowCostParams);
            edNowCost.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            // edNowCost.setMinimumWidth(fragment.getScreenWidth(80));
            this.addView(edNowCost);
            AppUtils.addBottomSeparatorLine(this, fragment);
        }
    }

}
