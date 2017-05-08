/**
 * Project Name: itee
 * File Name:	 AgentsPricingTableFragment.java
 * Package Name: cn.situne.itee.fragment.agents
 * Date:		 2015-03-24
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.agents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
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
import cn.situne.itee.common.utils.DateUtils;
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
import cn.situne.itee.view.AgentsPricingTableListUpView;
import cn.situne.itee.view.PricingTablePackageItem;
import cn.situne.itee.view.PricingTableProductItem;
import cn.situne.itee.view.SwipeLinearLayout;

/**
 * ClassName:AgentsPricingTableFragment <br/>
 * Function: pricing table item detail of agents. <br/>
 * Date: 2015-01-20 <br/>
 * UI:10-5
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class AgentsPricingTableFragment extends BaseEditFragment {

    private AgentsPricingTableListUpView upView = null;
    private float itemHeight;
    private List<JsonAgentsPricingGet.PricingTime> agentDataList;
    private List<JsonCommonProduct> pricingDataList;
    private int agentTimeId;
    private int agentId;
    private LinearLayout mBody;
    private String mainId = Constants.STR_EMPTY;

    private SwipeLinearLayout selectSwipeLinearLayout;

    private ArrayList<PricingTableProductItem> viewList;

    private ArrayList<PricingTablePackageItem> packageLayoutList;


    private StringBuffer delProductSb;

    private String selectedDates;

    @Override
    protected int getFragmentId() {
        Log.d("AgentsPricingTableFragm", "not changed ");
        return R.layout.fragment_pricing_table;
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
        Bundle bundle = getArguments();
        viewList = new ArrayList<>();
        delProductSb = new StringBuffer();
        packageLayoutList = new ArrayList<>();
        selectedDates ="";
        if (bundle != null) {
            setFragmentMode(FragmentMode.valueOf(bundle.getInt(TransKey.COMMON_FRAGMENT_MODE)));
            agentId = bundle.getInt(TransKey.AGENTS_AGENT_ID);

            mainId = bundle.getString(TransKey.AGENTS_MAIN_ID, Constants.STR_EMPTY);
            String bundleDateId = bundle.getString(TransKey.AGENTS_AGENT_TIME_ID);
            if (bundleDateId != null) {
                agentTimeId = Integer.parseInt(bundleDateId);
            }
        }

        mBody = (LinearLayout) rootView.findViewById(R.id.body);
        final LinearLayout mHeader = (LinearLayout) rootView.findViewById(R.id.ll_header);//
        int screenWidth = getScreenWidth();
        itemHeight = screenWidth / 6.5f;

        View.OnClickListener gotoChooseProductListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
//                    if (pricingDataList != null && pricingDataList.size() > 0) {
//                        Utils.showShortToast(getActivity(), R.string.msg_choose_only_one_product);
//                    } else {
                    Bundle bundle = new Bundle();
                    ArrayList<String> packageProducts = null;
                    if (pricingDataList != null) {
                        packageProducts = new ArrayList<>();
                        for (JsonCommonProduct dataItem : pricingDataList) {
                            ShopsProduct shopsProduct = new ShopsProduct();
                            shopsProduct.setProductName(dataItem.getProductName());
                            shopsProduct.setAttrId(String.valueOf(dataItem.getProductAttr()));
                            shopsProduct.setProductId(String.valueOf(dataItem.getProductId()));
                            shopsProduct.setTypeTagId(dataItem.getTypeTagId());
                            if (Utils.isStringNotNullOrEmpty(dataItem.getPackageId()) && !Constants.STR_0.equals(dataItem.getPackageId())) {
                                shopsProduct.setProductId(dataItem.getPackageId());
                            }

                            packageProducts.add(Utils.getStringFromObject(shopsProduct));
                        }
                    }
                    bundle.putStringArrayList(TransKey.SHOPS_PRODUCT_LIST, packageProducts);
                    bundle.putInt(TransKey.CHOOSE_MODE, ShopsChooseProductFragment.ChooseProductMode.ModePricingTable.value());
                    bundle.putString(TransKey.SHOPS_FRAGMENT_NAME, AgentsPricingTableFragment.class.getName());
                    push(ShopsChooseProductFragment.class, bundle);
                    // }
                }
            }
        };
        upView = new AgentsPricingTableListUpView(AgentsPricingTableFragment.this, itemHeight, gotoChooseProductListener, false);
        upView.setPricingType(Constants.PRICING_TYPE_3);
        upView.setAgentTimeId(agentTimeId);
        upView.setSelectDateOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentMode() != BaseEditFragment.FragmentMode.FragmentModeBrowse) {

//                    Intent intent = new Intent();
//                    intent.setClass(getBaseActivity(), SelectDateActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra(TransKey.SELECTED_DATE, upView.getDateText());
//                    startActivityForResult(intent, 110);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString(TransKey.SELECTED_DATE, selectedDates);
                    bundle1.putString(TransKey.COMMON_FROM_PAGE, AgentsPricingTableFragment.class.getName());
                    push(ChooseDateFragment.class, bundle1);
                }
            }
        });
        upView.setDateText("");
        mHeader.addView(upView);
        upView.setBackgroundResource(R.color.common_green);

    }

//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case 110:
//                if (resultCode == Activity.RESULT_OK) {
//
//                }
//                break;
//        }
//    }


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
        getTvLeftTitle().setText(R.string.agents_recharge_pricing_table);
        if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
            getTvRight().setBackgroundResource(R.drawable.icon_common_edit);
        } else {
            getTvRight().setText(R.string.common_ok);
            getTvRight().setBackground(null);
        }

        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
                    getTvRight().setBackground(null);
                    getTvRight().setText(R.string.common_ok);
                    setItemRightWidth(AppUtils.getRightButtonWidth(getBaseActivity()));
                    setFragmentMode(FragmentMode.FragmentModeEdit);
                } else {

                    if (pricingDataList == null || pricingDataList.size() <= 0) {
                        Utils.showLongToast(getActivity(), getString(R.string.msg_please_choose_product));
                    } else {
                        if (upView.getCheckDate() == null) {
                            submitAgentPricingData();
                        } else {
                            Utils.showLongToast(getActivity().getApplicationContext(), upView.getCheckDate());
                        }
                    }
                }
            }
        });
    }

    private View edOld;
    private void refreshBody() {

        mBody.removeAllViews();
        viewList.clear();
        for (JsonCommonProduct product : pricingDataList) {


            if (!Constants.STR_0.equals(product.getPackageId()) && product.getPackageItemList() != null) {
                final PricingTablePackageItem packageItem = new PricingTablePackageItem(AgentsPricingTableFragment.this, itemHeight, PricingTableProductItem.PRICING_TABLE_PRODUCT_ITEM_MODEL_2, product, false);
                packageItem.setViewData(product);
                packageItem.setDelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JsonCommonProduct jsonCommonProduct = packageItem.getViewData();
                        if (jsonCommonProduct.getPricingId() != null) {
                            delProductSb.append(jsonCommonProduct.getPricingId());
                            delProductSb.append(Constants.STR_COMMA);
                        }
                        pricingDataList.remove(jsonCommonProduct);
                        mBody.removeView(packageItem);
                    }
                });
                packageItem.setShowEditLayoutListener(new PricingTableProductItem.ShowEditLayoutListener() {
                    @Override
                    public void showEditLayout(View v) {
                        if (edOld != null && !edOld.equals(v))
                            edOld.setVisibility(View.GONE);
                        edOld = v;
                    }
                });

                if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
                    packageItem.getSwipeLinearLayout().setmRightViewWidth(getActualWidthOnThisDevice(0));
                }
                mBody.addView(packageItem);
                packageLayoutList.add(packageItem);

            } else {
                final PricingTableProductItem item = new PricingTableProductItem(AgentsPricingTableFragment.this, itemHeight, PricingTableProductItem.PRICING_TABLE_PRODUCT_ITEM_MODEL_2, false);
                if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
                    item.setmRightViewWidth(getActualWidthOnThisDevice(0));
                }
                item.setDelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JsonCommonProduct jsonCommonProduct = item.getViewData();
                        if (jsonCommonProduct.getPricingId() != null) {
                            delProductSb.append(jsonCommonProduct.getPricingId());
                            delProductSb.append(Constants.STR_COMMA);
                        }
                        pricingDataList.remove(jsonCommonProduct);

                        PricingTableProductItem viewParent = (PricingTableProductItem) v.getParent().getParent();
                        mBody.removeView(viewParent);
                        viewList.remove(viewParent);
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
                viewList.add(item);


            }
        }

    }

    @Override
    protected void executeOnceOnCreate() {
        if (getFragmentMode() != FragmentMode.FragmentModeAdd) {
            getAgentsPricingData();
        }
    }

//    private void putAgentPricingData(HttpManager<BaseJsonObject> hh, Map<String, String> params) {
//        hh.startPut(getActivity(), ApiManager.HttpApi.AgentsPricingPut, params);
//    }

    private void postAgentPricingData(HttpManager<BaseJsonObject> hh, Map<String, String> params) {
        hh.start(getActivity(), ApiManager.HttpApi.AgentsPricingPutX, params);
    }

    private void submitAgentPricingData() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.PRICING_MAIN_ID, mainId);
        params.put(ApiKey.PRICING_TYPE, Constants.PRICING_TYPE_3);
        params.put(ApiKey.PRICING_TYPE_ID, String.valueOf(agentId));

        params.put(ApiKey.PRICING_DATE_DESC, selectedDates);
        params.put(ApiKey.PRICING_TIME_INFO, upView.getAgentDataJsonString());
        params.put(ApiKey.AGENT_PRICING_DATA, getPricingDataString());


        params.put(ApiKey.PRICING_GUESTS_QTY, upView.getGuestQty());
        params.put(ApiKey.PRICING_TIMES_ACT, upView.getCsTimesFlag());
        params.put(ApiKey.PRICING_TIMES_NUMBER, upView.getTotalNumTimes());

        ArrayList<String> dateList = AppUtils.changeString2List(selectedDates, Constants.STR_COMMA);
        String etTitle = AppUtils.getEtTimeTitle(dateList, mContext);

        params.put(ApiKey.PRICING_DATE_STR, etTitle);


        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(AgentsPricingTableFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                Utils.showShortToast(getActivity(), msg);
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY
                        || returnCode == Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY) {
                    doBack();
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
        postAgentPricingData(hh, params);

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

    private String getPricingDataString() {
        JSONObject jsObj = new JSONObject();
        JSONArray addArray = new JSONArray();
        JSONArray editArray = new JSONArray();
        if (pricingDataList != null) {

            for (JsonCommonProduct data : pricingDataList) {

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

    private void getAgentsPricingData() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.PRICING_MAIN_ID, mainId);
        HttpManager<JsonAgentsPricingGet> hh = new HttpManager<JsonAgentsPricingGet>(AgentsPricingTableFragment.this) {
            @Override
            public void onJsonSuccess(JsonAgentsPricingGet jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    agentDataList = jo.getAgentDataList();
                    pricingDataList = jo.getPricingDataList();
                    selectedDates = jo.getDateDesc();

                    ArrayList<String> dateList = AppUtils.changeString2List(selectedDates, Constants.STR_COMMA);
                    String etTitle = AppUtils.getEtTimeTitle(dateList, mContext);
                    upView.setValues(etTitle, agentDataList, jo.getTimeQty(), jo.getTimeAct(), jo.getTimeNumber());
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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.AgentsPricingPutX, params);
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

                boolean quick = false;

                if (QuickChooseProduct.class.getName().equals(bundle.getString(TransKey.COMMON_FROM_PAGE)))  quick = true;

                if (pricingDataList == null) {
                    pricingDataList = new ArrayList<>();
                }
                if (quick)pricingDataList.clear();
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
                       // data.setProductMoneyDefault(String.valueOf(Constants.SWITCH_RIGHT));
                        data.setGuestDiscountType(Constants.MONEY_DISCOUNT_PERCENT);
                        data.setGuestProductDiscount(Constants.STR_0);
                        if (quick){
                           // data.setProductMoneyDefault(shopsProduct.getGuestDiscountType());
                            data.setGuestDiscountType(shopsProduct.getGuestDiscountType());
                            data.setGuestProductDiscount(shopsProduct.getGuestDiscount());
                        }

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

                        pricingDataList.add(data);
                    }
                }
                refreshBody();
            }

        }

    }
}
