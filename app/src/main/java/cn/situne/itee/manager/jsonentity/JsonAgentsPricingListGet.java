/**
 * Project Name: itee
 * File Name:  JsonAgentsPricingListGet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-21
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonAgentsPricingListGet <br/>
 * Function: Get Json Agents Pricing List. <br/>
 * Date: 2015-03-21 <br/>
 *
 * @author Luochao
 * @version 1.0
 * @see
 */
public class JsonAgentsPricingListGet extends BaseJsonObject implements Serializable {

    private List<PricingItem> agentsPricingList;

    public List<PricingItem> getAgentsPricingList() {
        return agentsPricingList;
    }

    public void setAgentsPricingList(List<PricingItem> agentsPricingList) {
        this.agentsPricingList = agentsPricingList;
    }

    public static class PricingData implements Serializable {

        private String memberDiscount;
        private String memberDiscountType;
        private String guestDiscount;
        private String guestDiscountType;

        private String packageId;
        private String productId;
        private String attributeId;
        private String mainId;

        private String productName;
        private String memberPrice;

        private String guestPrice;

        private String productPrice;


        private String Qty;

        private String productShopId;

        public String getProductShopId() {
            return productShopId;
        }

        public void setProductShopId(String productShopId) {
            this.productShopId = productShopId;
        }

        public String getQty() {
            return Qty;
        }

        public void setQty(String qty) {
            Qty = qty;
        }

        public String getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(String productPrice) {
            this.productPrice = productPrice;
        }

        public List<PricingData> getProductList() {
            return productList;
        }

        public void setProductList(List<PricingData> productList) {
            this.productList = productList;
        }

        private List<PricingData> productList;

        public String getGuestPrice() {
            return guestPrice;
        }

        public void setGuestPrice(String guestPrice) {
            this.guestPrice = guestPrice;
        }

        public String getMemberPrice() {
            return memberPrice;
        }

        public void setMemberPrice(String memberPrice) {
            this.memberPrice = memberPrice;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getMemberDiscount() {
            return memberDiscount;
        }

        public void setMemberDiscount(String memberDiscount) {
            this.memberDiscount = memberDiscount;
        }

        public String getMemberDiscountType() {
            return memberDiscountType;
        }

        public void setMemberDiscountType(String memberDiscountType) {
            this.memberDiscountType = memberDiscountType;
        }

        public String getGuestDiscount() {
            return guestDiscount;
        }

        public void setGuestDiscount(String guestDiscount) {
            this.guestDiscount = guestDiscount;
        }

        public String getGuestDiscountType() {
            return guestDiscountType;
        }

        public void setGuestDiscountType(String guestDiscountType) {
            this.guestDiscountType = guestDiscountType;
        }

        public String getPackageId() {
            return packageId;
        }

        public void setPackageId(String packageId) {
            this.packageId = packageId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getAttributeId() {
            return attributeId;
        }

        public void setAttributeId(String attributeId) {
            this.attributeId = attributeId;
        }

        public String getMainId() {
            return mainId;
        }

        public void setMainId(String mainId) {
            this.mainId = mainId;
        }
    }


    public static class PricingItem implements Serializable {

        private ArrayList<PricingData> pricingDataList;

        public ArrayList<PricingData> getPricingDataList() {
            return pricingDataList;
        }

        public void setPricingDataList(ArrayList<PricingData> pricingDataList) {
            this.pricingDataList = pricingDataList;
        }

        private String mainId;

        private String timeInfo;
        private String dateDesc;

        private String dateDescStr;

        public String getDateDescStr() {
            return dateDescStr;
        }

        public void setDateDescStr(String dateDescStr) {
            this.dateDescStr = dateDescStr;
        }

        public String getTimeInfo() {
            return timeInfo;
        }

        public void setTimeInfo(String timeInfo) {
            this.timeInfo = timeInfo;
        }

        public String getDateDesc() {
            return dateDesc;
        }

        public void setDateDesc(String dateDesc) {
            this.dateDesc = dateDesc;
        }

        public String getMainId() {
            return mainId;
        }

        public void setMainId(String mainId) {
            this.mainId = mainId;
        }

    }

    public JsonAgentsPricingListGet(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        agentsPricingList = new ArrayList<>();
        Utils.log(jsonObj.toString());
        try {
            JSONArray jsonArray = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                PricingItem pricingItem = new PricingItem();
                //pricing_data
                ArrayList<PricingData> pricingDataList = new ArrayList<PricingData>();
                JSONArray jsPricingList = jsonObject.getJSONArray(JsonKey.PRICING_PRICING_DATA);


                for (int p = 0; p < jsPricingList.length(); p++) {
                    PricingData pricingData = new PricingData();

                    JSONObject jsPricing = jsPricingList.getJSONObject(p);
                    pricingData.setAttributeId(Utils.getStringFromJsonObjectWithKey(jsPricing, JsonKey.COMMON_ATTRIBUTE_ID));
                    pricingData.setMainId(Utils.getStringFromJsonObjectWithKey(jsPricing, JsonKey.PRICING_MAIN_ID));
                    pricingData.setPackageId(Utils.getStringFromJsonObjectWithKey(jsPricing, JsonKey.AGENT_PRICING_DATA_PACKAGE_ID));
                    pricingData.setProductId(Utils.getStringFromJsonObjectWithKey(jsPricing, JsonKey.COMMON_PRODUCT_ID));

                    pricingData.setGuestDiscount(Utils.getStringFromJsonObjectWithKey(jsPricing, JsonKey.PRICING_GUEST_DISCOUNT));
                    pricingData.setGuestDiscountType(Utils.getStringFromJsonObjectWithKey(jsPricing, JsonKey.PRICING_GUEST_DISCOUNT_TYPE));
                    pricingData.setMemberDiscount(Utils.getStringFromJsonObjectWithKey(jsPricing, JsonKey.PRICING_MEMBER_DISCOUNT));
                    pricingData.setMemberDiscountType(Utils.getStringFromJsonObjectWithKey(jsPricing, JsonKey.PRICING_MEMBER_DISCOUNT_TYPE));

                    pricingData.setProductName(Utils.getStringFromJsonObjectWithKey(jsPricing, JsonKey.PRICING_PRODUCT_NAME));
                    pricingData.setProductPrice(Utils.getStringFromJsonObjectWithKey(jsPricing, JsonKey.PRICING_PRODUCT_PRICE));
                    pricingData.setProductShopId(Utils.getStringFromJsonObjectWithKey(jsPricing, JsonKey.PRICING_PRODUCT_SHOP_ID));

                    pricingData.setMemberPrice(Utils.getStringFromJsonObjectWithKey(jsPricing, JsonKey.PRICING_MEMBER_PRICE));

                    pricingData.setGuestPrice(Utils.getStringFromJsonObjectWithKey(jsPricing, JsonKey.PRICING_GUEST_PRICE));

                    if (!jsPricing.isNull(JsonKey.PRICING_PRICING_PRODUCT_LIST)){

                        JSONArray jsProductList = jsPricing.getJSONArray(JsonKey.PRICING_PRICING_PRODUCT_LIST);
                        ArrayList<PricingData> productDataList = new ArrayList<PricingData>();

                        for (int w = 0; w < jsProductList.length(); w++) {
                            PricingData productData = new PricingData();

                            JSONObject jsProduct = jsProductList.getJSONObject(w);
                            productData.setAttributeId(Utils.getStringFromJsonObjectWithKey(jsProduct, JsonKey.COMMON_ATTRIBUTE_ID));
                            productData.setMainId(Utils.getStringFromJsonObjectWithKey(jsProduct, JsonKey.PRICING_MAIN_ID));
                            productData.setPackageId(Utils.getStringFromJsonObjectWithKey(jsProduct, JsonKey.AGENT_PRICING_DATA_PACKAGE_ID));
                            productData.setProductId(Utils.getStringFromJsonObjectWithKey(jsProduct, JsonKey.COMMON_PRODUCT_ID));
                            productData.setProductPrice(Utils.getStringFromJsonObjectWithKey(jsPricing, JsonKey.PRICING_PRODUCT_PRICE));
                            productData.setGuestDiscount(Utils.getStringFromJsonObjectWithKey(jsProduct, JsonKey.PRICING_GUEST_DISCOUNT));
                            productData.setGuestDiscountType(Utils.getStringFromJsonObjectWithKey(jsProduct, JsonKey.PRICING_GUEST_DISCOUNT_TYPE));
                            productData.setMemberDiscount(Utils.getStringFromJsonObjectWithKey(jsProduct, JsonKey.PRICING_MEMBER_DISCOUNT));
                            productData.setMemberDiscountType(Utils.getStringFromJsonObjectWithKey(jsProduct, JsonKey.PRICING_MEMBER_DISCOUNT_TYPE));

                            productData.setProductName(Utils.getStringFromJsonObjectWithKey(jsProduct, JsonKey.PRICING_PRODUCT_NAME));

                            productData.setMemberPrice(Utils.getStringFromJsonObjectWithKey(jsProduct, JsonKey.PRICING_MEMBER_PRICE));

                            productData.setGuestPrice(Utils.getStringFromJsonObjectWithKey(jsProduct, JsonKey.PRICING_GUEST_PRICE));

                            productDataList.add(productData);
                        }
                        pricingData.setProductList(productDataList);
                    }

                    pricingDataList.add(pricingData);
                }

                pricingItem.setPricingDataList(pricingDataList);


                // pricing item data
                pricingItem.setMainId(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.PRICING_MAIN_ID));
                pricingItem.setTimeInfo(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.PRICING_TIME_FMT));
                pricingItem.setDateDesc(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.PRICING_DATE_DESC));
                pricingItem.setDateDescStr(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.PRICING_DATE_STR));


                agentsPricingList.add(pricingItem);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }

}
