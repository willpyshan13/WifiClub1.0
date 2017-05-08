/**
 * Project Name: itee
 * File Name:	 ShopsPropertyPriceOrQtyEditFragment.java
 * Package Name: cn.situne.itee.fragment.shops
 * Date:		 2015-04-09
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.shops;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.MessageFormat;
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
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonShopsPropertyPriceOrQtyGet;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeIntegerEditText;
import cn.situne.itee.view.IteeMoneyEditText;
import cn.situne.itee.view.popwindow.AddCoursePopupWindow;

/**
 * ClassName:ShopsPropertyPriceOrQtyEditFragment <br/>
 * Function: PropertyPrice Or Qty Edit. <br/>
 * Date: 2015-04-09 <br/>
 *
 * @author luochao
 * @version 1.0
 * @see
 */
public class ShopsPropertyPriceOrQtyEditFragment extends BaseEditFragment {
    private String productId;
    private LinearLayout llBody;
    private boolean isQty;
    private int qtyTotal;
    private ArrayList<JsonShopsPropertyPriceOrQtyGet.DataItem> dataList;
    private ArrayList<PropertyItem> allPropertyItems;//view

    private String fromPage;
    private AddCoursePopupWindow addCoursePopupWindow;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_property_edit;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        addCoursePopupWindow = new AddCoursePopupWindow(getActivity(), null);
        allPropertyItems = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            setFragmentMode(BaseEditFragment.FragmentMode.valueOf(bundle.getInt(TransKey.COMMON_FRAGMENT_MODE)));
            isQty = bundle.getBoolean(TransKey.SHOPS_PACKAGE_IS_QTY);
            productId = bundle.getString(TransKey.SHOPS_PRODUCT_ID);
            fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE);
        }
        llBody = (LinearLayout) rootView.findViewById(R.id.ll_body);
        getSubClass();
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

    private void initSubclassView() {
        for (JsonShopsPropertyPriceOrQtyGet.DataItem item : dataList) {
            addSubclassView(item);
        }
    }

    private void addSubclassView(JsonShopsPropertyPriceOrQtyGet.DataItem data) {
        LinearLayout itemLayout = new LinearLayout(getBaseActivity());
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        itemLayout.addView(AppUtils.getSeparatorLine(ShopsPropertyPriceOrQtyEditFragment.this));
        LinearLayout.LayoutParams itemBoxParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout itemBox = new RelativeLayout(getBaseActivity());
        itemBox.setLayoutParams(itemBoxParams);
        PropertyItem item = new PropertyItem(ShopsPropertyPriceOrQtyEditFragment.this, 1, getActualHeightOnThisDevice(100), data, isQty, allPropertyItems);
        item.setParentId(-1);
        item.refreshView(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == 66) {
                        v.clearFocus();
                        Utils.hideKeyboard(getBaseActivity());
                    }

                }
                return false;
            }
        }, new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int praId = (int) v.getTag();
                if (!hasFocus) {
                    if (isQty) {
                        changeQtyNumWithPraId(praId);
                    } else {
                        IteeMoneyEditText ed = (IteeMoneyEditText) v;
                        changePriceNumWithPraId(praId, ed.getValue());
                    }
                }

            }
        });
        itemBox.addView(item);
        AppUtils.addBottomSeparatorLine(itemBox, ShopsPropertyPriceOrQtyEditFragment.this);
        itemLayout.addView(itemBox);
        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 30);
        View view = new View(getBaseActivity());
        view.setLayoutParams(viewParams);
        itemLayout.addView(view);
        llBody.addView(itemLayout);
        item.setContainers(itemLayout);
    }


    private PropertyItem findViewItemWithPraId(int praId) {
        for (PropertyItem viewItem : allPropertyItems) {
            if (praId == viewItem.getmData().getPraId()) {
                return viewItem;
            }
        }
        return null;
    }

    private void changeQtyNumWithPraId(int parentId) {
        PropertyItem parentItem = findViewItemWithPraId(parentId);
        while (parentItem != null) {
            int sumQty = 0;
            for (PropertyItem childItem : parentItem.getChildItems()) {
                int qty = 0;
                try {
                    qty = Integer.parseInt(childItem.getEdQty().getText().toString());
                } catch (NumberFormatException e) {
                    Utils.log(e.getMessage());
                }
                sumQty += qty;
            }
            parentItem.getEdQty().setText(String.valueOf(sumQty));
            parentItem = findViewItemWithPraId(parentItem.getParentId());
        }
    }


    private void changePriceNumWithPraId(int parentId, String value) {
        // while (parentItem != null) {
        for (PropertyItem viewItem : allPropertyItems) {

            int pId = (int)viewItem.getEdPrice().getTag();
            if (parentId == viewItem.getParentId() && pId != parentId) {
                viewItem.getEdPrice().setValue(value);
                changePriceNumWithPraId(pId,value);
//                if (value.equals(Constants.STR_EMPTY)) {
//                    viewItem.getEdPrice().setValue(value);
//                }
            }
        }
    }


    @SuppressWarnings("unchecked")
    private void putSubClass() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        if (isQty) {
            params.put(ApiKey.SHOPS_SUB_CLASS_LIST, getQtyPutData());
            params.put(ApiKey.SHOPS_SUB_CLASS_LIST_QTY_TOTAL, String.valueOf(qtyTotal));
        } else {
            params.put(ApiKey.SHOPS_SUB_CLASS_LIST, getPricePutData());
        }
        params.put(ApiKey.SHOPS_SUB_CLASS_SETTING_STATUS, Constants.STR_1);
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShopsPropertyPriceOrQtyEditFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                Utils.showShortToast(getActivity(), msg);
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    Bundle bundle = new Bundle();
                    if (isQty) {
                        if (qtyTotal <= 0) {
                            bundle.putString(TransKey.SHOPS_QTY_TOTAL, Constants.STR_EMPTY);
                        } else {

                            bundle.putString(TransKey.SHOPS_QTY_TOTAL, String.valueOf(qtyTotal));
                        }

                    }

                    try {
                        doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
                    } catch (ClassNotFoundException e) {
                        Utils.log(e.getMessage());
                    }
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

        if (isQty) {
            putQtySubClass(hh, params);
        } else {
            pubPriceSubClass(hh, params);
        }
    }

    private String getQtyPutData() {
        qtyTotal = 0;
        JSONArray putQtyArray = new JSONArray();
        for (PropertyItem item : allPropertyItems) {
            Map<String, String> map = new HashMap<>();


            map.put(ApiKey.SHOPS_SUB_CLASS_LIST_ID, String.valueOf(item.getmData().getPraId()));
            map.put(ApiKey.SHOPS_SUB_CLASS_LIST_VALUE, item.getEdQty().getText().toString());
            putQtyArray.put(new JSONObject(map));
            if (item.getLevel() == 1) {

                int total = 0;
                try {
                    total = Integer.parseInt(item.getEdQty().getText().toString());
                } catch (NumberFormatException e) {
                    Utils.log(e.getMessage());
                }

                qtyTotal += total;
            }
        }
        return putQtyArray.toString();

    }


    private String getPricePutData() {


        JSONArray putQtyArray = new JSONArray();
        for (PropertyItem item : allPropertyItems) {
            Map<String, String> map = new HashMap<>();

            String praPrice = item.getEdPrice().getEmptyValue();
            map.put(ApiKey.SHOPS_SUB_CLASS_LIST_ID, String.valueOf(item.getmData().getPraId()));
            map.put(ApiKey.SHOPS_SUB_CLASS_LIST_VALUE, praPrice);
            putQtyArray.put(new JSONObject(map));

        }
        return putQtyArray.toString();

    }


    private void putQtySubClass(HttpManager<BaseJsonObject> hh, Map<String, String> params) {
        hh.startPut(this.getActivity(), ApiManager.HttpApi.ShopsQtySubclassGet, params);

    }


    private void pubPriceSubClass(HttpManager<BaseJsonObject> hh, Map<String, String> params) {
        hh.startPut(this.getActivity(), ApiManager.HttpApi.ShopsPriceSubclassGet, params);
    }


    private void getSubClass() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_PRODUCT_ID, productId);
        params.put(ApiKey.SHOPS_SUB_CLASS_SETTING_STATUS, Constants.STR_1);
        HttpManager<JsonShopsPropertyPriceOrQtyGet> hh = new HttpManager<JsonShopsPropertyPriceOrQtyGet>(ShopsPropertyPriceOrQtyEditFragment.this) {

            @Override
            public void onJsonSuccess(JsonShopsPropertyPriceOrQtyGet jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    dataList = jo.getDataList();
                    initSubclassView();
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

        if (isQty) {
            getQtySubClass(hh, params);
        } else {
            getPriceSubClass(hh, params);
        }
    }


    private void getQtySubClass(HttpManager<JsonShopsPropertyPriceOrQtyGet> hh, Map<String, String> params) {
        hh.startGet(this.getActivity(), ApiManager.HttpApi.ShopsQtySubclassGet, params);
    }


    private void getPriceSubClass(HttpManager<JsonShopsPropertyPriceOrQtyGet> hh, Map<String, String> params) {
        hh.startGet(this.getActivity(), ApiManager.HttpApi.ShopsPriceSubclassGet, params);
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        if (isQty) {
            getTvLeftTitle().setText(R.string.shop_setting_qty_of_subcategories);
        } else {
            getTvLeftTitle().setText(R.string.shop_setting_price_of_subcategories);
        }

        ViewGroup.LayoutParams layoutParams = getTvLeftTitle().getLayoutParams();
        layoutParams.width = (int) (getScreenWidth() * 0.7);
        getTvLeftTitle().setLayoutParams(layoutParams);

        getTvRight().setText(R.string.common_ok);
        getTvRight().setBackground(null);

        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getQtyPutData();
                String msg = MessageFormat.format(getString(R.string.msg_the_total_quantity_will_be_changed_to),
                        String.valueOf(qtyTotal));

                v.setFocusableInTouchMode(true);
                v.setFocusable(true);
                Utils.hideKeyboard(getBaseActivity());


                if (isQty) {

                    addCoursePopupWindow.showAtLocation(ShopsPropertyPriceOrQtyEditFragment.this.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    addCoursePopupWindow.tvEvent.setVisibility(View.GONE);
                    addCoursePopupWindow.tvContent.setText(msg);

                    addCoursePopupWindow.btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addCoursePopupWindow.dismiss();

                            putSubClass();
                        }
                    });

                    addCoursePopupWindow.btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            addCoursePopupWindow.dismiss();


                        }
                    });
                } else {

                    putSubClass();
                }


            }
        });


    }

//    private String doCheck() {
//        for (PropertyItem item : allPropertyItems) {
//            if (isQty) {
//                if (item.getEdQty().getValue().length() <= 0 || Integer.parseInt(item.getEdQty().getText().toString()) < 1) {
//                    return AppUtils.generateNotNullMessage(this, R.string.shop_setting_qty);
//                }
//            } else {
//                if (item.getEdPrice().getValue().length() <= 0 || Double.parseDouble(item.getEdPrice().getValue()) <= 0) {
//                    return AppUtils.generateNotNullMessage(this, R.string.shop_setting_price);
//                }
//            }
//        }
//        return null;
//    }


    class PropertyItem extends LinearLayout {
        private IteeIntegerEditText edQty;
        private IteeMoneyEditText edPrice;
        private int parentId;
        private ArrayList<PropertyItem> mAllPropertyItems;
        private JsonShopsPropertyPriceOrQtyGet.DataItem mData;
        private View containers;
        private IteeEditText tvName;
        private View.OnClickListener delItemListener;
        private int mLevel;
        private ArrayList<PropertyItem> childItems;
        private BaseFragment mFragment;
        private OnKeyListener mEdOnKeyListener;
        private OnFocusChangeListener mEdOnFocusChangeListener;

        public PropertyItem(BaseFragment fragment, int level, int height, JsonShopsPropertyPriceOrQtyGet.DataItem data, boolean isQty, ArrayList<PropertyItem> allPropertyItems) {
            super(fragment.getBaseActivity());
            setOrientation(VERTICAL);
            mLevel = level;
            mFragment = fragment;
            mData = data;
            mAllPropertyItems = allPropertyItems;

            childItems = new ArrayList<>();
            RelativeLayout.LayoutParams myParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            this.setLayoutParams(myParams);
            RelativeLayout.LayoutParams bodyParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
            RelativeLayout body = new RelativeLayout(fragment.getBaseActivity());
            body.setLayoutParams(bodyParams);
            body.setPadding(mFragment.getActualWidthOnThisDevice(0), 0, mFragment.getActualWidthOnThisDevice(0), 0);
            switch (level) {
                case 2:
                    body.setPadding(mFragment.getActualWidthOnThisDevice(60), 0, mFragment.getActualWidthOnThisDevice(0), 0);
                    break;

                case 3:
                    body.setPadding(mFragment.getActualWidthOnThisDevice(100), 0, mFragment.getActualWidthOnThisDevice(0), 0);
                    break;
                default:
                    break;

            }

            RelativeLayout.LayoutParams tvNameParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            tvNameParams.leftMargin = mFragment.getActualWidthOnThisDevice(10);

            tvNameParams.addRule(RelativeLayout.CENTER_VERTICAL);
            tvName = new IteeEditText(fragment.getBaseActivity());
            tvName.setLayoutParams(tvNameParams);
            tvName.setMinWidth(mFragment.getActualWidthOnThisDevice(100));
            body.addView(tvName);


            RelativeLayout.LayoutParams edQtyParams = new RelativeLayout.LayoutParams(mFragment.getActualWidthOnThisDevice(300), LayoutParams.WRAP_CONTENT);
            edQtyParams.rightMargin = mFragment.getActualWidthOnThisDevice(10);
            edQtyParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            edQtyParams.addRule(RelativeLayout.CENTER_VERTICAL);
            edQty = new IteeIntegerEditText(fragment);
            edQty.setLayoutParams(edQtyParams);
            edQty.setMinWidth(mFragment.getActualWidthOnThisDevice(100));
            edQty.setGravity(Gravity.END);
            edQty.setVisibility(GONE);
            edQty.setHint(mFragment.getString(R.string.shop_setting_qty));
            edQty.addTextChangedListener(new AppUtils.EditViewIntegerWatcher(edQty));


            body.addView(edQty);


            RelativeLayout.LayoutParams edPriceParams = new RelativeLayout.LayoutParams(mFragment.getActualWidthOnThisDevice(300), LayoutParams.WRAP_CONTENT);
            edPriceParams.rightMargin = mFragment.getActualWidthOnThisDevice(10);
            edPriceParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            edPriceParams.addRule(RelativeLayout.CENTER_VERTICAL);
            edPrice = new IteeMoneyEditText(mFragment);
            edPrice.setLayoutParams(edPriceParams);
            edPrice.setMinWidth(mFragment.getActualWidthOnThisDevice(100));
            body.addView(edPrice);

            edPrice.setVisibility(GONE);
            edPrice.setGravity(Gravity.END);

            if (isQty) {

                edQty.setVisibility(View.VISIBLE);
            } else {
                edPrice.setVisibility(View.VISIBLE);

            }

            AppUtils.addBottomSeparatorLine(body, fragment);
            this.addView(body);


        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public IteeMoneyEditText getEdPrice() {
            return edPrice;
        }

        public IteeEditText getEdQty() {
            return edQty;
        }

        public JsonShopsPropertyPriceOrQtyGet.DataItem getmData() {
            return mData;
        }

        public IteeEditText getTvName() {
            return tvName;
        }

        public int getLevel() {

            return mLevel;
        }

        public void setDelItemListener(OnClickListener delItemListener) {
            this.delItemListener = delItemListener;
        }

        public void setContainers(View containers) {
            this.containers = containers;
            this.containers.setTag(this);
        }

        public ArrayList<PropertyItem> getChildItems() {
            return childItems;
        }

        private boolean isChange;
        private void refreshView(OnKeyListener edOnKeyListener, OnFocusChangeListener edOnFocusChangeListener) {
            mEdOnKeyListener = edOnKeyListener;
            mEdOnFocusChangeListener = edOnFocusChangeListener;
            if (mData != null) {
                tvName.setText(mData.getPraName());
                edQty.setText(String.valueOf(mData.getPraQty()));
                edQty.setOnKeyListener(edOnKeyListener);
                edPrice.setTag(mData.getPraId());
                edPrice.setValue(String.valueOf(mData.getPraPrice()));
                edPrice.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (!charSequence.toString().contains(AppUtils.getCurrentCurrency(mFragment.getBaseActivity()))) {
                            isChange = true;
                        } else {
                            isChange = false;
                        }
                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (!charSequence.toString().contains(AppUtils.getCurrentCurrency(mFragment.getBaseActivity()))) {
                            if (isChange) {
                                mEdOnFocusChangeListener.onFocusChange(edPrice, false);
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable afterTextChanged) {

                    }
                });


                edQty.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        mEdOnFocusChangeListener.onFocusChange(edQty, false);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                if (mData.getSubClass() != null && mData.getSubClass().size() > 0) {

                    edQty.setEnabled(false);
                } else {
                    edQty.setEnabled(true);

                }

                initChildView(mData.getSubClass());
            }
            edQty.setTag(parentId);
            mAllPropertyItems.add(this);
        }

        private void initChildView(List<JsonShopsPropertyPriceOrQtyGet.DataItem> dataList) {
            if (dataList != null && dataList.size() > 0) {
                for (int i = 0; i < dataList.size(); i++) {
                    JsonShopsPropertyPriceOrQtyGet.DataItem data = dataList.get(i);
                    PropertyItem item = new PropertyItem(ShopsPropertyPriceOrQtyEditFragment.this, mLevel + 1, mFragment.getActualHeightOnThisDevice(100), data, isQty, mAllPropertyItems);
                    item.setDelItemListener(delItemListener);
                    item.setParentId(mData.getPraId());
                    item.setContainers(this);
                    PropertyItem.this.addView(item);
                    childItems.add(item);
                    item.refreshView(mEdOnKeyListener, mEdOnFocusChangeListener);
                }
            }


        }

    }
}