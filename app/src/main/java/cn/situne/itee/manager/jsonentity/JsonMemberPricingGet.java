/**
 * Project Name: itee
 * File Name:  JsonMemberPricingGet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

import static cn.situne.itee.manager.jsonentity.JsonAgentsPricingGet.*;

/**
 * ClassName:JsonMemberPricingGet <br/>
 * Function: To get member pricing. <br/>
 * Date: 2015-03-24 <br/>
 *
 * @author Luochao
 * @version 1.0
 * @see
 */

public class JsonMemberPricingGet extends BaseJsonObject implements Serializable {


    private String timeAct;
    private String timeNumber;
    private String timeQty;

    public String getTimeAct() {
        return timeAct;
    }

    public void setTimeAct(String timeAct) {
        this.timeAct = timeAct;
    }

    public String getTimeNumber() {
        return timeNumber;
    }

    public void setTimeNumber(String timeNumber) {
        this.timeNumber = timeNumber;
    }

    public String getTimeQty() {
        return timeQty;
    }

    public void setTimeQty(String timeQty) {
        this.timeQty = timeQty;
    }

    private int guestNumber;

    private String dateDesc;
    private List<JsonAgentsPricingGet.PricingTime> memberDataList;
    private List<JsonCommonProduct> pricingDataList;
    public String getDateDesc() {
        return dateDesc;
    }
    public void setDateDesc(String dateDesc) {
        this.dateDesc = dateDesc;
    }

    public int getGuestNumber() {
        return guestNumber;
    }

    public void setGuestNumber(int guestNumber) {
        this.guestNumber = guestNumber;
    }

    public List<JsonAgentsPricingGet.PricingTime> getMemberDataList() {
        return memberDataList;
    }

    public void setMemberDataList(List<JsonAgentsPricingGet.PricingTime> memberDataList) {
        this.memberDataList = memberDataList;
    }

    public List<JsonCommonProduct> getPricingDataList() {
        return pricingDataList;
    }

    public void setPricingDataList(List<JsonCommonProduct> pricingDataList) {
        this.pricingDataList = pricingDataList;
    }

    public JsonMemberPricingGet(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        memberDataList = new ArrayList<>();
        pricingDataList = new ArrayList<>();
        try {
            JSONObject joCustomerList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            setDateDesc(Utils.getStringFromJsonObjectWithKey(joCustomerList, JsonKey.PRICING_DATE_DESC));

            setTimeAct(Utils.getStringFromJsonObjectWithKey(joCustomerList, JsonKey.PRICING_TIMES_ACT));
            setTimeNumber(Utils.getStringFromJsonObjectWithKey(joCustomerList, JsonKey.PRICING_TIMES_NUMBER));
            setTimeQty(Utils.getStringFromJsonObjectWithKey(joCustomerList, JsonKey.PRICING_GUESTS_QTY));
            JSONArray packageArray = Utils.getArrayFromJsonObjectWithKey(joCustomerList, JsonKey.PRICING_PACKAGE_DATA);

            JSONArray jsTimeInfoArray = Utils.getArrayFromJsonObjectWithKey(joCustomerList, JsonKey.PRICING_TIME_INFO);
            for (int i = 0; i < jsTimeInfoArray.length(); i++) {
                try {
                    JSONObject jsonMemberDate = jsTimeInfoArray.getJSONObject(i);
                    JsonAgentsPricingGet.PricingTime memberData = new JsonAgentsPricingGet.PricingTime();
                    memberData.setStartTime(Utils.getStringFromJsonObjectWithKey(jsonMemberDate, JsonKey.PRICING_START_TIME));
                    memberData.setEndTime(Utils.getStringFromJsonObjectWithKey(jsonMemberDate, JsonKey.PRICING_END_TIME));
                    memberDataList.add(memberData);
                } catch (JSONException e) {
                    Utils.log(e.getMessage());
                }
            }

            JSONArray jsonPricingDates = Utils.getArrayFromJsonObjectWithKey(joCustomerList, JsonKey.CUSTOMER_PRICING_DATA);
            for (int i = 0; i < jsonPricingDates.length(); i++) {
                try {

                    JSONObject itemObject = jsonPricingDates.getJSONObject(i);
                    JsonCommonProduct item = new JsonCommonProduct();

                    String packageId = Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.AGENT_PRICING_DATA_PACKAGE_ID);
                    item.setProductId(Utils.getIntegerFromJsonObjectWithKey(itemObject, JsonKey.EVENT_PRODUCT_ID));
                    item.setProductOriginalCost(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.PRICING_PRODUCT_PRICE));
                    item.setProductName(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.EVENT_PRODUCT_NAME));
                    item.setProductAttr(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.COMMON_ATTRIBUTE_ID));
                    item.setPackageId(packageId);
                    item.setPricingId(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.PRICING_ID));
                    item.setTypeTagId(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.COMMON_TYPE_TAG_ID));
                    item.setGuestProductDiscount(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.PRICING_GUEST_DISCOUNT));
                    item.setGuestDiscountType(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.PRICING_GUEST_DISCOUNT_TYPE));
                    item.setMemberProductDiscount(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.PRICING_MEMBER_DISCOUNT));
                    item.setMemberDiscountType(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.PRICING_MEMBER_DISCOUNT_TYPE));
                    if (!Constants.STR_0.equals(packageId)){
                        ArrayList< JsonCommonProduct.PackageItem > packageItemList = new ArrayList<JsonCommonProduct.PackageItem >();
                        JSONArray jsPackageList = Utils.getArrayFromJsonObjectWithKey(itemObject, JsonKey.PRICING_PRODUCT_LIST);
                        for (int x = 0; x < jsPackageList.length(); x++) {
                            JSONObject jsPackageItem = jsPackageList.getJSONObject(x);
                            JsonCommonProduct.PackageItem packageItem = new JsonCommonProduct.PackageItem();
                            packageItem.setProductId(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.EVENT_PRODUCT_ID));
                            packageItem.setPrice(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_PRODUCT_PRICE));
                            packageItem.setPricingId(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_ID));
                            packageItem.setGuestProductDiscount(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_GUEST_DISCOUNT));
                            packageItem.setGuestProductDiscountType(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_GUEST_DISCOUNT_TYPE));
                            packageItem.setProductName(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.EVENT_PRODUCT_NAME));
                            packageItem.setMemberProductDiscount(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_MEMBER_DISCOUNT));
                            packageItem.setMemberProductDiscountType(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_MEMBER_DISCOUNT_TYPE));
                            packageItem.setProductAttr(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.COMMON_ATTRIBUTE_ID));
                            packageItemList.add(packageItem);
                        }

                        item.setPackageItemList(packageItemList);
                    }

                    pricingDataList.add(item);
                } catch (JSONException e) {
                    Utils.log(e.getMessage());
                }
            }


//            for (int i = 0; i < packageArray.length(); i++) {
//                JSONObject itemObject = packageArray.getJSONObject(i);
//                JsonCommonProduct item = new JsonCommonProduct();
//                item.setProductName(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.EVENT_PACKAGE_NAME));
//                item.setPackageId(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.EVENT_PACKAGE_ID));
//                item.setTypeTagId(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.COMMON_TYPE_TAG_ID));
//                item.setProductOriginalCost(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.PRICING_COST_PRICE));
//                JSONArray jsPackageList = Utils.getArrayFromJsonObjectWithKey(itemObject, JsonKey.PRICING_PRODUCT_LIST);
//                ArrayList< JsonCommonProduct.PackageItem > packageItemList = new ArrayList<JsonCommonProduct.PackageItem >();
//                for (int x = 0; x < jsPackageList.length(); x++) {
//                    JSONObject jsPackageItem = jsPackageList.getJSONObject(x);
//                    JsonCommonProduct.PackageItem packageItem = new JsonCommonProduct.PackageItem();
//                    packageItem.setProductId(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.EVENT_PRODUCT_ID));
//                    packageItem.setPrice(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_COST_PRICE));
//                    packageItem.setPricingId(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_ID));
//                    packageItem.setGuestProductDiscount(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_GUEST_DISCOUNT));
//                    packageItem.setGuestProductDiscountType(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_GUEST_DISCOUNT_TYPE));
//                    packageItem.setProductName(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.EVENT_PRODUCT_NAME));
//                    packageItem.setMemberProductDiscount(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.PRICING_MEMBER_DISCOUNT));
//                    packageItem.setMemberProductDiscountType(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.PRICING_MEMBER_DISCOUNT_TYPE));
//                    packageItemList.add(packageItem);
//                }
//
//                item.setPackageItemList(packageItemList);
//                pricingDataList.add(item);
//            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }


}
