/**
 * Project Name: itee
 * File Name:	 EventsPricingFragment.java
 * Package Name: cn.situne.itee.fragment.events
 * Date:		 2015-03-09
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.events;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.ShopsProduct;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.shops.ShopsChooseProductFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonCommonProduct;
import cn.situne.itee.manager.jsonentity.JsonEventsPricing;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.PricingTableItemBase;
import cn.situne.itee.view.PricingTablePackageItem;
import cn.situne.itee.view.PricingTablePackageItemBase;
import cn.situne.itee.view.PricingTableProductItem;
import cn.situne.itee.view.SwipeLinearLayout;

/**
 * ClassName:EventsPricingFragment <br/>
 * Function: pricing table list of events. <br/>
 * UI:  09-2-1
 * Date: 2015-03-09 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class EventsPricingFragment extends BaseEditFragment {

    private Integer eventsId;
    private String token;
    private ArrayList<JsonCommonProduct> dataList;
    private ArrayList<LinearLayout> layoutList;
    private LinearLayout mBody;
    private IteeTextView tvChooseProduct;
    private float itemHeight;
    private View.OnClickListener listenerPutData;
    private StringBuffer delProductSb;
    private SwipeLinearLayout selectSwipeLinearLayout;
    private String mainId;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_event_pricing_table;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.event_pricing_table;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private JsonCommonProduct getViewData(LinearLayout lin) {

        if (lin instanceof PricingTableItemBase) {
            return ((PricingTableItemBase) lin).getViewData();
        } else {
            return ((PricingTablePackageItemBase) lin).getViewData();
        }
    }

    @Override
    protected void initControls(View rootView) {
        tvChooseProduct = (IteeTextView) rootView.findViewById(R.id.tv_choose_product);
        delProductSb = new StringBuffer();
        dataList = new ArrayList<>();
        mainId = Constants.STR_0;
        itemHeight = getActualHeightOnThisDevice(100);
        Bundle bundle = getArguments();
        if (bundle != null) {
            eventsId = bundle.getInt(TransKey.EVENT_EVENT_ID);
        }
        token = AppUtils.getToken(getActivity());
        mBody = (LinearLayout) rootView.findViewById(R.id.body);
        layoutList = new ArrayList<>();
        RelativeLayout rlShopContainer = (RelativeLayout) rootView.findViewById(R.id.rl_choose_product_container);
        rlShopContainer.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                ArrayList<String> packageProducts = new ArrayList<>();
                for (LinearLayout lin : layoutList) {

                    JsonCommonProduct data = getViewData(lin);
                    ShopsProduct shopsProduct = new ShopsProduct();
                    shopsProduct.setProductName(data.getProductName());
                    shopsProduct.setAttrId(String.valueOf(data.getProductAttr()));
                    shopsProduct.setProductId(String.valueOf(data.getProductId()));
                    if (Utils.isStringNotNullOrEmpty(data.getPackageId()) && !Constants.STR_0.equals(data.getPackageId())) {
                        shopsProduct.setProductId(data.getPackageId());
                    }
                    shopsProduct.setTypeTagId(data.getTypeTagId());
                    packageProducts.add(Utils.getStringFromObject(shopsProduct));
                }

                bundle.putStringArrayList(TransKey.SHOPS_PRODUCT_LIST, packageProducts);
                bundle.putInt(TransKey.CHOOSE_MODE, ShopsChooseProductFragment.ChooseProductMode.ModePricingTable.value());
                bundle.putString(TransKey.SHOPS_FRAGMENT_NAME, EventsPricingFragment.class.getName());
                push(ShopsChooseProductFragment.class, bundle);
            }
        });
        getEventsPricingData();
    }

    @Override
    protected void setDefaultValueOfControls() {
    }



    private Map<String, String> getItemMap(JsonCommonProduct data, JsonCommonProduct.PackageItem packageItem) {

            Map<String, String> item = new HashMap<>();
            if (packageItem != null) {
                item.put(JsonKey.EVENT_PRODUCT_ID, String.valueOf(packageItem.getProductId()));
                item.put(JsonKey.PRICING_GUEST_DISCOUNT_TYPE, packageItem.getGuestProductDiscountType());
                item.put(JsonKey.PRICING_GUEST_DISCOUNT, packageItem.getGuestProductDiscount());
                item.put(JsonKey.AGENT_PRICING_DATA_PRODUCT_ATTR, packageItem.getProductAttr());
                item.put(JsonKey.PRICING_ID,packageItem.getPricingId());
            } else {
                item.put(JsonKey.EVENT_PRODUCT_ID, String.valueOf(data.getProductId()));
                item.put(JsonKey.PRICING_GUEST_DISCOUNT_TYPE, data.getGuestDiscountType());
                item.put(JsonKey.PRICING_GUEST_DISCOUNT, data.getGuestProductDiscount());
                item.put(JsonKey.AGENT_PRICING_DATA_PACKAGE_ID, String.valueOf(data.getPackageId()));
                item.put(JsonKey.AGENT_PRICING_DATA_PRODUCT_ATTR, String.valueOf(data.getProductAttr()));
                item.put(JsonKey.PRICING_ID,data.getPricingId());

            }
            item.put(JsonKey.AGENT_PRICING_DATA_PACKAGE_ID, String.valueOf(data.getPackageId()));
            item.put(JsonKey.PRICING_MEMBER_DISCOUNT_TYPE, Constants.STR_0);
            item.put(JsonKey.PRICING_MEMBER_DISCOUNT, Constants.STR_0);
            return item;

    }



    private String getJsonDataInfo() {
        JSONObject jsObj = new JSONObject();
        JSONArray addArray = new JSONArray();
        JSONArray editArray = new JSONArray();
        for (LinearLayout lin : layoutList) {
            JsonCommonProduct data = getViewData(lin);

            if (!Constants.STR_0.equals(data.getPackageId()) && data.getPackageItemList() != null) {

                for (JsonCommonProduct.PackageItem packageItem : data.getPackageItemList()) {

                    Map<String, String> item = getItemMap(data, packageItem);
                    if (packageItem.getPricingId() == null) {
                        JSONObject itemObject = new JSONObject(item);
                        addArray.put(itemObject);

                    } else {
                        item.put(JsonKey.PRICING_ID, packageItem.getPricingId());
                        JSONObject itemObject = new JSONObject(item);
                        editArray.put(itemObject);
                    }

                }

            } else {

                Map<String, String> item = getItemMap(data, null);
                if (data.getPricingId() == null) {
                    JSONObject itemObject = new JSONObject(item);
                    addArray.put(itemObject);

                } else {
                    item.put(JsonKey.PRICING_ID, data.getPricingId());
                    JSONObject itemObject = new JSONObject(item);
                    editArray.put(itemObject);
                }
            }


        }


        try {
            jsObj.put("add", addArray);
            jsObj.put("edit", editArray);
            String delStr = delProductSb.toString();
            if (delStr.length() > 0) {
                delStr = delStr.substring(0, delStr.length() - 1);
            }

            JSONObject jsDel = new JSONObject();
            jsDel.put(JsonKey.PRICING_ID, delStr);
            jsObj.put("del", jsDel);
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

        return jsObj.toString();
    }

    private View edOld;
    private void refreshBody() {
        mBody.removeAllViews();
        layoutList.clear();
        for (JsonCommonProduct product : dataList) {
            if (!Constants.STR_0.equals(product.getPackageId()) && product.getPackageItemList() != null) {
                PricingTablePackageItem packageItem = new PricingTablePackageItem(EventsPricingFragment.this, itemHeight, PricingTableProductItem.PRICING_TABLE_PRODUCT_ITEM_MODEL_2, product, false);
                packageItem.setViewData(product);
                packageItem.setDelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PricingTablePackageItem selectItem = (PricingTablePackageItem) v.getTag();
                        JsonCommonProduct jsonCommonProduct = selectItem.getViewData();

                        for (JsonCommonProduct.PackageItem packageItem : jsonCommonProduct.getPackageItemList()) {

                            if (packageItem.getPricingId() != null) {
                                delProductSb.append(packageItem.getPricingId());
                                delProductSb.append(Constants.STR_COMMA);
                            }
                        }

                        dataList.remove(jsonCommonProduct);
                        mBody.removeView(selectItem);
                    }
                });
                mBody.addView(packageItem);
                packageItem.setShowEditLayoutListener(new PricingTableProductItem.ShowEditLayoutListener() {
                    @Override
                    public void showEditLayout(View v) {
                        if (edOld!=null&&!edOld.equals(v))edOld.setVisibility(View.GONE);
                        edOld = v;
                    }
                });
                layoutList.add(packageItem);

            } else {

                PricingTableProductItem item = new PricingTableProductItem(EventsPricingFragment.this, itemHeight, PricingTableProductItem.PRICING_TABLE_PRODUCT_ITEM_MODEL_2, false);

                item.setDelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PricingTableProductItem selectItem = (PricingTableProductItem) v.getTag();

                        JsonCommonProduct jsonCommonProduct = selectItem.getViewData();
                        if (jsonCommonProduct.getPricingId() != null) {
                            delProductSb.append(jsonCommonProduct.getPricingId());
                            delProductSb.append(Constants.STR_COMMA);
                        }
                        dataList.remove(jsonCommonProduct);
                        mBody.removeView((LinearLayout) (v.getParent().getParent()));
                    }
                });
                item.setShowEditLayoutListener(new PricingTableProductItem.ShowEditLayoutListener() {
                    @Override
                    public void showEditLayout(View v) {
                        if (edOld!=null&&!edOld.equals(v))edOld.setVisibility(View.GONE);
                        edOld = v;

                    }
                });
                item.setSwipeLayoutListener(new SwipeLinearLayout.SwipeLayoutListener() {
                    @Override
                    public void scrollView(View item) {
                        if (item instanceof PricingTableProductItem) {
                            if (selectSwipeLinearLayout != null && !selectSwipeLinearLayout.equals(item)) {
                                selectSwipeLinearLayout.hideRight();
                            }
                            PricingTableProductItem eventItem = (PricingTableProductItem) item;
                            eventItem.hiddenEditView();
                        }
                    }
                });

                item.setAfterShowRightListener(new SwipeLinearLayout.AfterShowRightListener() {
                    @Override
                    public void doAfterShowRight(SwipeLinearLayout swipeLinearLayout) {
                        selectSwipeLinearLayout = swipeLinearLayout;
                    }
                });


                item.setViewData(product);
                item.refreshView();
                mBody.addView(item);

                //mBody.addView(packageItem);
                layoutList.add(item);
            }


        }

    }


    @Override
    protected void setListenersOfControls() {
        listenerPutData = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                params.put(ApiKey.COMMON_TOKEN, token);
                params.put(ApiKey.PRICING_TYPE, Constants.PRICING_TYPE_4);
                params.put(ApiKey.PRICING_TYPE_ID, String.valueOf(eventsId));
                params.put(ApiKey.PRICING_TIMES_ACT, Constants.STR_0);
                params.put(ApiKey.PRICING_PRICING_DATA, getJsonDataInfo());
                if (Utils.isStringNullOrEmpty(mainId))mainId = Constants.STR_0;
                params.put(ApiKey.PRICING_MAIN_ID, mainId);

                HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(EventsPricingFragment.this) {

                    @Override
                    public void onJsonSuccess(BaseJsonObject jo) {
                        int returnCode = jo.getReturnCode();
                        String msg = jo.getReturnInfo();
                        Utils.showShortToast(getActivity(), msg);
                        if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY || returnCode == Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY || returnCode == Constants.RETURN_CODE_20403_DELETE_SUCCESSFULLY) {
                            getBaseActivity().doBack();
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
                hh.start(getActivity(), ApiManager.HttpApi.AgentsPricingPutX, params);


            }
        };
    }


    @Override
    protected void reShowWithBackValue() {

        Bundle bundle = getReturnValues();
        if (bundle != null) {
            ArrayList<String> chooseProductList = bundle.getStringArrayList(TransKey.CHOOSE_PRODUCT_LIST);
            if (chooseProductList != null) {
                for (String product : chooseProductList) {
                    ShopsProduct shopsProduct = (ShopsProduct) Utils.getObjectFromString(product);
                    JsonCommonProduct data = new JsonCommonProduct();
                    data.setPackageId(shopsProduct.getPackageId());
                    data.setProductId(Integer.parseInt(shopsProduct.getProductId()));
                    data.setProductName(shopsProduct.getProductName());
                    data.setProductOriginalCost(shopsProduct.getProductPrice());
                    data.setProductNowCost(shopsProduct.getProductPrice());
                   // data.setProductMoneyDefault(Constants.MONEY_DISCOUNT_PERCENT);
                    data.setGuestProductDiscount(Constants.STR_0);
                    data.setGuestDiscountType(Constants.STR_0);
                    data.setTypeTagId(shopsProduct.getTypeTagId());
                    if (shopsProduct.getAttrId() != null) {
                        data.setProductAttr(shopsProduct.getAttrId());
                    }

                    if (!Constants.STR_0.equals(shopsProduct.getPackageId()) && shopsProduct.getProductList() != null) {
                        ArrayList<JsonCommonProduct.PackageItem> packageItemList = new ArrayList<>();

                        for (ShopsProduct.ShopsSubProduct packageItem : shopsProduct.getProductList()) {
                            JsonCommonProduct.PackageItem packageDateItem = new JsonCommonProduct.PackageItem();
                            packageDateItem.setGuestProductDiscount(Constants.STR_0);
                            packageDateItem.setPrice(packageItem.getProductPrice());
                            packageDateItem.setProductId(packageItem.getProductId());
                            packageDateItem.setGuestProductDiscountType(Constants.MONEY_DISCOUNT_PERCENT);
                            packageDateItem.setProductName(packageItem.getProductName());
                            packageDateItem.setProductAttr(packageItem.getProductAttr());
                            packageItemList.add(packageDateItem);
                        }

                        data.setPackageItemList(packageItemList);
                    }

                    dataList.add(data);
                }
            }

            refreshBody();
        }
    }

    @Override
    protected void setLayoutOfControls() {
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvChooseProduct, mContext);
    }


    @Override
    protected void setPropertyOfControls() {
        tvChooseProduct.setTextSize(Constants.FONT_SIZE_NORMAL);
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getString(R.string.event_pricing_table));
        getTvRight().setVisibility(View.VISIBLE);
        getTvRight().setText(getResources().getString(R.string.common_ok));
        getTvRight().setOnClickListener(listenerPutData);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void getEventsPricingData() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, token);
        params.put(ApiKey.PRICING_TYPE, Constants.PRICING_TYPE_4);
        params.put(ApiKey.PRICING_TYPE_ID, String.valueOf(eventsId));
        HttpManager<JsonEventsPricing> hh = new HttpManager<JsonEventsPricing>(EventsPricingFragment.this) {

            @Override
            public void onJsonSuccess(JsonEventsPricing jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    dataList = jo.getDataList();
                    mainId = jo.getMainId();
                    refreshBody();
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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.PricingListPageGetX, params);
    }
}