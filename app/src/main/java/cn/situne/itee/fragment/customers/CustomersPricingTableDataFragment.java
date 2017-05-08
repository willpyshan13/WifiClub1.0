/**
 * Project Name: itee
 * File Name:	 CustomersPricingTableDataFragment.java
 * Package Name: cn.situne.itee.fragment.customers
 * Date:		 2015-03-24
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.customers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.activity.SelectDateActivity;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.ShopsProduct;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.administration.ChooseDateFragment;
import cn.situne.itee.fragment.quick.QuickChooseProduct;
import cn.situne.itee.fragment.shops.ShopsChooseProductFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonAgentsPricingGet;
import cn.situne.itee.manager.jsonentity.JsonCommonProduct;
import cn.situne.itee.manager.jsonentity.JsonMemberPricingGet;
import cn.situne.itee.view.AgentsPricingTableListUpView;
import cn.situne.itee.view.PricingTableItemBase;
import cn.situne.itee.view.PricingTablePackageItem;
import cn.situne.itee.view.PricingTableProductItem;
import cn.situne.itee.view.SwipeLinearLayout;
import cn.situne.itee.view.SwipeListView;

/**
 * ClassName:CustomersPricingTableDataFragment <br/>
 * Function: pricing table item detail of customer. <br/>
 * Date: 2015-03-24 <br/>
 * UI:11-4
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class CustomersPricingTableDataFragment extends BaseEditFragment {


    private String mainId = Constants.STR_EMPTY;
    private int memberTypeId;
    private int guestNumber;
    private List<JsonAgentsPricingGet.PricingTime> memberDataList;
    private List<JsonCommonProduct> productDataList;
    private AgentsPricingTableListUpView upView = null;
    private String memberTypeTypeId;
    private float itemHeight;
    private StringBuffer delProductSb;

    private LinearLayout mBody;

    private ArrayList<PricingTableProductItem> viewList;

    private ArrayList<PricingTablePackageItem> packageLayoutList;

    private SwipeLinearLayout selectSwipeLinearLayout;

    private String selectedDates;

    public CustomersPricingTableDataFragment() {
    }

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_customers_pricing_table;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    private void setItemRightWidth(int width) {

        for (PricingTableProductItem item : viewList) {
            item.setmRightViewWidth(width);
        }


        for (PricingTablePackageItem pricingTablePackageItem : packageLayoutList) {
            pricingTablePackageItem.getSwipeLinearLayout().setmRightViewWidth(width);

        }
    }

    @Override
    protected void initControls(View rootView) {
        packageLayoutList = new ArrayList<>();
        delProductSb = new StringBuffer();
        viewList = new ArrayList<>();
        float screenWidth = getScreenWidth();
        itemHeight = screenWidth / 6.5f;

        Bundle bundle = getArguments();
        if (bundle != null) {
            memberTypeId = bundle.getInt(TransKey.CUSTOMERS_MEMBER_TYPE_ID);

            memberTypeTypeId = bundle.getString(TransKey.CUSTOMERS_MEMBER_TYPE_TYPE_ID, Constants.CUSTOMER_MEMBER);
            mainId = bundle.getString(TransKey.AGENTS_MAIN_ID, Constants.STR_EMPTY);
            setFragmentMode(FragmentMode.valueOf(bundle.getInt(TransKey.COMMON_FRAGMENT_MODE)));
        }


        mBody = (LinearLayout) rootView.findViewById(R.id.body);
        LinearLayout mHeader = (LinearLayout) rootView.findViewById(R.id.ll_header);

        View.OnClickListener gotoChooseProductListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentMode() != BaseEditFragment.FragmentMode.FragmentModeBrowse) {
                    Bundle bundle = new Bundle();
                    ArrayList<String> packageProducts = null;
                    if (productDataList != null) {
                        packageProducts = new ArrayList<>();
                        for (JsonCommonProduct dataItem : productDataList) {
                            ShopsProduct shopsProduct = new ShopsProduct();
                            shopsProduct.setProductName(dataItem.getProductName());
                            shopsProduct.setAttrId(String.valueOf(dataItem.getProductAttr()));
                            shopsProduct.setProductId(String.valueOf(dataItem.getProductId()));
                            shopsProduct.setTypeTagId(dataItem.getTypeTagId());
                            if (Utils.isStringNotNullOrEmpty(dataItem.getPackageId()) && !Constants.STR_0.equals(dataItem.getPackageId()))
                                shopsProduct.setProductId(dataItem.getPackageId());
                            packageProducts.add(Utils.getStringFromObject(shopsProduct));
                        }
                    }
                    bundle.putStringArrayList(TransKey.SHOPS_PRODUCT_LIST, packageProducts);
                    bundle.putInt(TransKey.CHOOSE_MODE, ShopsChooseProductFragment.ChooseProductMode.ModePricingTable.value());
                    bundle.putString(TransKey.SHOPS_FRAGMENT_NAME, CustomersPricingTableDataFragment.class.getName());
                    push(ShopsChooseProductFragment.class, bundle);
                }
            }

        };
        upView = new AgentsPricingTableListUpView(CustomersPricingTableDataFragment.this, itemHeight, gotoChooseProductListener, true);
        upView.setTag(SwipeListView.DISABLE_RIGHT);

        if (Constants.CUSTOMER_NON_MEMBER.equals(memberTypeTypeId)){
            upView.setPricingType(Constants.PRICING_TYPE_2);

        }else{
            upView.setPricingType(Constants.PRICING_TYPE_1);

        }

        upView.setSelectDateOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentMode() != BaseEditFragment.FragmentMode.FragmentModeBrowse) {

//                    Intent intent = new Intent();
//                    intent.setClass(getBaseActivity(), SelectDateActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                    intent.putExtra(TransKey.SELECTED_DATE, upView.getDateText());
//                    startActivityForResult(intent, 110);
                    Bundle bundle = new Bundle();
                    bundle.putString(TransKey.SELECTED_DATE, selectedDates);


                    bundle.putString(TransKey.COMMON_FROM_PAGE, CustomersPricingTableDataFragment.class.getName());
                    push(ChooseDateFragment.class,bundle);

                }
            }
        });

        if (getFragmentMode() == BaseEditFragment.FragmentMode.FragmentModeAdd) {


            selectedDates = "";

        }

        if (Constants.CUSTOMER_NON_MEMBER.equals(memberTypeTypeId)) {
            upView.setGuestLayoutVisibility(View.GONE);
        }
        upView.setDateText("");
        mHeader.addView(upView);

        if (getFragmentMode() != FragmentMode.FragmentModeAdd) {
            getEventsPricingData();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 110:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    selectedDates = bundle.getString(TransKey.SELECTED_DATE);
                    ArrayList<String> dateList = AppUtils.changeString2List(selectedDates, Constants.STR_COMMA);
                    String etTitle = AppUtils.getEtTimeTitle(dateList, mContext);
                    upView.setDateText(etTitle);
                }
                break;
        }
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
        getTvLeftTitle().setText(R.string.customers_pricing_table);
        if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
            getTvRight().setBackgroundResource(R.drawable.icon_common_edit);
            upView.getGuestTextView().setEnabled(false);
            upView.setLayoutStatus(getFragmentMode());
        } else {
            getTvRight().setText(R.string.common_ok);
            getTvRight().setBackground(null);
            upView.getGuestTextView().setEnabled(true);
            upView.setLayoutStatus(getFragmentMode());
        }

        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
                    upView.getGuestTextView().setEnabled(true);

                    getTvRight().setBackground(null);
                    getTvRight().setText(R.string.common_ok);
                    setItemRightWidth(AppUtils.getRightButtonWidth(getActivity()));
                    setFragmentMode(FragmentMode.FragmentModeEdit);
                    upView.setLayoutStatus(getFragmentMode());
                } else {
                    if (doCheck()) {
                        submitAgentPricingData();
                    }
                }
            }
        });
    }

    @Override
    protected void reShowWithBackValue() {
        Bundle bundle = getReturnValues();
        if (bundle != null) {
            if (ChooseDateFragment.class.getName().equals(bundle.getString(TransKey.COMMON_FROM_PAGE))) {
                selectedDates = bundle.getString(TransKey.SELECTED_DATE);
                ArrayList<String> dateList = AppUtils.changeString2List(selectedDates, Constants.STR_COMMA);
                String etTitle = AppUtils.getEtTimeTitle(dateList, mContext);
                upView.setDateText(etTitle);
            } else {
                ArrayList<String> chooseProductList = bundle.getStringArrayList(TransKey.CHOOSE_PRODUCT_LIST);

                boolean quick = false;
                if (QuickChooseProduct.class.getName().equals(bundle.getString(TransKey.COMMON_FROM_PAGE)))
                    quick = true;
                if (productDataList == null) {
                    productDataList = new ArrayList<>();
                }
                if (quick) productDataList.clear();
                for (String product : chooseProductList) {
                    ShopsProduct shopsProduct = (ShopsProduct) Utils.getObjectFromString(product);
                    JsonCommonProduct data = new JsonCommonProduct();

                    data.setPackageId(shopsProduct.getPackageId());
                    data.setProductId(Integer.parseInt(shopsProduct.getProductId()));
                    data.setProductName(shopsProduct.getProductName());
                    data.setProductOriginalCost(shopsProduct.getProductPrice());


                    data.setProductNowCost(shopsProduct.getProductPrice());
                    data.setMemberProductNowCost(shopsProduct.getProductPrice());
                    data.setGuestProductNowCost(shopsProduct.getProductPrice());
                    data.setMemberProductDiscount(Constants.STR_0);
                    data.setGuestProductDiscount(Constants.STR_0);
                    data.setGuestDiscountType(Constants.MONEY_DISCOUNT_PERCENT);
                    data.setMemberDiscountType(Constants.MONEY_DISCOUNT_PERCENT);

                    if (quick) {

                        data.setMemberProductDiscount(shopsProduct.getMemberDiscount());
                        data.setGuestProductDiscount(shopsProduct.getGuestDiscount());
                        data.setGuestDiscountType(shopsProduct.getGuestDiscountType());
                        data.setMemberDiscountType(shopsProduct.getMemberDiscountType());
                    }

                    // data.setMemberMoneyDefault(String.valueOf(Constants.SWITCH_RIGHT));
                    // data.setGuestMoneyDefault(String.valueOf(Constants.SWITCH_RIGHT));
                    data.setTypeTagId(shopsProduct.getTypeTagId());
                    if (shopsProduct.getAttrId() != null) {
                        data.setProductAttr(shopsProduct.getAttrId());
                    }

                    if (!Constants.STR_0.equals(shopsProduct.getPackageId()) && shopsProduct.getProductList() != null) {
                        ArrayList<JsonCommonProduct.PackageItem> packageItemList = new ArrayList<JsonCommonProduct.PackageItem>();

                        for (ShopsProduct.ShopsSubProduct packageItem : shopsProduct.getProductList()) {
                            JsonCommonProduct.PackageItem packageDateItem = new JsonCommonProduct.PackageItem();
                            packageDateItem.setPrice(packageItem.getProductPrice());
                            packageDateItem.setProductId(packageItem.getProductId());
                            packageDateItem.setProductName(packageItem.getProductName());
                            packageDateItem.setProductAttr(packageItem.getProductAttr());


                            packageDateItem.setGuestProductDiscount(Constants.STR_0);
                            packageDateItem.setMemberProductDiscount(Constants.STR_0);


                            packageDateItem.setGuestProductDiscountType(Constants.MONEY_DISCOUNT_PERCENT);
                            packageDateItem.setMemberProductDiscountType(Constants.MONEY_DISCOUNT_PERCENT);


                            if (quick) {
                                packageDateItem.setMemberProductDiscount(shopsProduct.getMemberDiscount());
                                packageDateItem.setGuestProductDiscount(shopsProduct.getGuestDiscount());
                                packageDateItem.setGuestProductDiscountType(shopsProduct.getGuestDiscountType());
                                packageDateItem.setMemberProductDiscountType(shopsProduct.getMemberDiscountType());
                            }

                            packageItemList.add(packageDateItem);
                        }

                        data.setPackageItemList(packageItemList);
                    }



                    productDataList.add(data);
                }
                refreshBody();
            }


        }

    }

    private Map<String, String> getItemMap(JsonCommonProduct data, JsonCommonProduct.PackageItem packageItem) {

        Map<String, String> item = new HashMap<>();

        if (packageItem != null) {
            item.put(JsonKey.EVENT_PRODUCT_ID, String.valueOf(packageItem.getProductId()));
            item.put(JsonKey.PRICING_GUEST_DISCOUNT_TYPE, packageItem.getGuestProductDiscountType());
            item.put(JsonKey.PRICING_GUEST_DISCOUNT, packageItem.getGuestProductDiscount());
            item.put(JsonKey.AGENT_PRICING_DATA_PRODUCT_ATTR, packageItem.getProductAttr());


            item.put(JsonKey.PRICING_MEMBER_DISCOUNT_TYPE, packageItem.getMemberProductDiscountType());
            item.put(JsonKey.PRICING_MEMBER_DISCOUNT, packageItem.getMemberProductDiscount());

        } else {
            item.put(JsonKey.EVENT_PRODUCT_ID, String.valueOf(data.getProductId()));
            item.put(JsonKey.AGENT_PRICING_DATA_PACKAGE_ID, String.valueOf(data.getPackageId()));
            item.put(JsonKey.AGENT_PRICING_DATA_PRODUCT_ATTR, String.valueOf(data.getProductAttr()));
            item.put(JsonKey.PRICING_GUEST_DISCOUNT_TYPE, data.getGuestDiscountType());
            item.put(JsonKey.PRICING_GUEST_DISCOUNT, data.getGuestProductDiscount());
            item.put(JsonKey.PRICING_MEMBER_DISCOUNT_TYPE, data.getMemberDiscountType());
            item.put(JsonKey.PRICING_MEMBER_DISCOUNT, data.getMemberProductDiscount());

        }
        item.put(JsonKey.AGENT_PRICING_DATA_PACKAGE_ID, String.valueOf(data.getPackageId()));

        return item;
    }

    private String getPricingDataString() {
        JSONObject jsObj = new JSONObject();
        JSONArray addArray = new JSONArray();
        JSONArray editArray = new JSONArray();
        for (JsonCommonProduct jsonData : productDataList) {

            if (!Constants.STR_0.equals(jsonData.getPackageId()) && jsonData.getPackageItemList() != null) {

                for (JsonCommonProduct.PackageItem packageItem : jsonData.getPackageItemList()) {

                    Map<String, String> item = getItemMap(jsonData, packageItem);
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

                Map<String, String> item = getItemMap(jsonData, null);
                if (jsonData.getPricingId() == null) {
                    JSONObject itemObject = new JSONObject(item);
                    addArray.put(itemObject);

                } else {
                    item.put(JsonKey.PRICING_ID, jsonData.getPricingId());
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

    private void submitAgentPricingData() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.PRICING_MAIN_ID, mainId);

        if (Constants.CUSTOMER_NON_MEMBER.equals(memberTypeTypeId)){

            params.put(ApiKey.PRICING_TYPE, Constants.PRICING_TYPE_2);
        }else{
            params.put(ApiKey.PRICING_TYPE, Constants.PRICING_TYPE_1);

        }
        params.put(ApiKey.PRICING_TYPE_ID, String.valueOf(memberTypeId));
        params.put(ApiKey.PRICING_DATE_DESC, selectedDates);
        params.put(ApiKey.PRICING_TIME_INFO, upView.getAgentDataJsonString());
        params.put(ApiKey.MEMBER_PRICING_DATA, getPricingDataString());
        ArrayList<String> dateList = AppUtils.changeString2List(selectedDates, Constants.STR_COMMA);
        String etTitle = AppUtils.getEtTimeTitle(dateList, mContext);

        params.put(ApiKey.PRICING_DATE_STR, etTitle);



        params.put("guests_qty", upView.getGuestQty());
        params.put("times_act", upView.getCsTimesFlag());
        params.put("times_number", upView.getTotalNumTimes());

        Utils.log("==========================================submitAgentPricingData====================================================");


        Utils.log(params.toString());
        Utils.log("==============================================================================================");

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(CustomersPricingTableDataFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();

                if (returnCode == Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY || returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
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

        postCustomersPricingData(hh, params);

    }

    private void postCustomersPricingData(HttpManager<BaseJsonObject> hh, Map<String, String> params) {

        hh.start(getActivity(), ApiManager.HttpApi.AgentsPricingPutX, params);
    }


    private void getEventsPricingData() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.PRICING_MAIN_ID, mainId);
        HttpManager<JsonMemberPricingGet> hh = new HttpManager<JsonMemberPricingGet>(CustomersPricingTableDataFragment.this) {

            @Override
            public void onJsonSuccess(JsonMemberPricingGet jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    guestNumber = jo.getGuestNumber();
                    memberDataList = jo.getMemberDataList();
                    productDataList = jo.getPricingDataList();
                    refreshBody();

                    selectedDates = jo.getDateDesc();

                    ArrayList<String> dateList = AppUtils.changeString2List(selectedDates, Constants.STR_COMMA);
                    String etTitle = AppUtils.getEtTimeTitle(dateList, mContext);

                    upView.setValues(etTitle, jo.getMemberDataList(), jo.getTimeQty(), jo.getTimeAct(), jo.getTimeNumber());
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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.AgentsPricingPutX, params);
    }

    public boolean doCheck() {
        boolean res = true;


        if (Utils.isStringNullOrEmpty(upView.getDateText())){

            Utils.showShortToast(getActivity(),  AppUtils.generateNotNullMessage(this, R.string.pricing_table_choose_date));
            return false;
        }


        if (!Utils.isListNotNullOrEmpty(productDataList)) {
            res = false;
            Utils.showShortToast(getActivity(), getString(R.string.msg_please_choose_product));
        }
        return res;
    }


    private View edOld;
    private void refreshBody() {

        mBody.removeAllViews();
        viewList.clear();
        packageLayoutList.clear();
        for (JsonCommonProduct product : productDataList) {
            if (!Constants.STR_0.equals(product.getPackageId()) && product.getPackageItemList() != null) {
                boolean isNonMember = false;
                if (Constants.CUSTOMER_NON_MEMBER.equals(memberTypeTypeId)) {
                    isNonMember = true;
                }
                PricingTablePackageItem packageItem = new PricingTablePackageItem(CustomersPricingTableDataFragment.this, itemHeight, PricingTableProductItem.PRICING_TABLE_PRODUCT_ITEM_MODEL_1, product, isNonMember);
                // packageItem.setProductData(product);
                packageItem.setViewData(product);

                if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
                    packageItem.getSwipeLinearLayout().setmRightViewWidth(getActualWidthOnThisDevice(0));
                }
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
                        productDataList.remove(jsonCommonProduct);
                        mBody.removeView(selectItem);
                        //productDataList.remove(selectItem);
                    }
                });
                packageItem.setShowEditLayoutListener(new PricingTableProductItem.ShowEditLayoutListener(){
                    @Override
                    public void showEditLayout(View v) {
                        if (edOld!=null&&!edOld.equals(v))
                            edOld.setVisibility(View.GONE);
                        edOld = v;
                    }
                });
                mBody.addView(packageItem);
                packageLayoutList.add(packageItem);
            } else {
                int itemModel = PricingTableProductItem.PRICING_TABLE_PRODUCT_ITEM_MODEL_1;
                if (Constants.CUSTOMER_NON_MEMBER.equals(memberTypeTypeId)) {
                    itemModel = PricingTableProductItem.PRICING_TABLE_PRODUCT_ITEM_MODEL_2;
                    product.setProductDiscount(product.getMemberProductDiscount(), product.getMemberProductDiscount(), "1");
                    // product.setProductMoneyDefault(product.getMemberMoneyDefault());
                    product.setProductNowCost(product.getMemberProductNowCost());
                }


                final PricingTableProductItem item = new PricingTableProductItem(CustomersPricingTableDataFragment.this, itemHeight, itemModel, false);
                if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
                    item.setmRightViewWidth(getActualWidthOnThisDevice(0));
                }
                item.setDelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        JsonCommonProduct jsonCommonProduct = item.getViewData();
                        if (Utils.isStringNotNullOrEmpty(jsonCommonProduct.getPricingId()) && !Constants.STR_0.equals(jsonCommonProduct.getPricingId())) {
                            delProductSb.append(String.valueOf(jsonCommonProduct.getPricingId()));
                            delProductSb.append(Constants.STR_COMMA);
                        }
                        productDataList.remove(jsonCommonProduct);
                        mBody.removeView((LinearLayout) (v.getParent().getParent()));
                        viewList.remove(v.getParent().getParent());
                    }
                });

                item.setShowEditLayoutListener(new PricingTableProductItem.ShowEditLayoutListener() {
                    @Override
                    public void showEditLayout(View v) {
                        if (edOld != null && !edOld.equals(v))
                            edOld.setVisibility(View.GONE);
                        edOld = v;
                    }
                });

                item.setSwipeLayoutListener(new SwipeLinearLayout.SwipeLayoutListener() {
                    @Override
                    public void scrollView(View item) {
                        if (item instanceof PricingTableProductItem) {
                            if (selectSwipeLinearLayout != null && !selectSwipeLinearLayout.equals(item))
                                selectSwipeLinearLayout.hideRight();
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
                viewList.add(item);

            }
        }

    }

}