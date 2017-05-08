/**
 * Project Name: itee
 * File Name:	 JsonMemberProductGet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-03-24
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonMemberProductGet <br/>
 * Function: entity of api member get <br/>
 * Date: 2015-03-24 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonMemberProductGet extends BaseJsonObject implements Serializable {

    private ArrayList<MemberProductShop> dataList;

    public JsonMemberProductGet(JSONObject jsonObject) {
        super(jsonObject);
        dataList = new ArrayList<>();
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {
            dataList = new ArrayList<>();
            try {
                JSONArray arrDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
                for (int i = 0; i < arrDataList.length(); i++) {
                    JSONObject joShop = arrDataList.getJSONObject(i);
                    MemberProductShop memberProductShop = new MemberProductShop();
                    memberProductShop.setShopId(Utils.getIntegerFromJsonObjectWithKey(joShop, JsonKey.COMMON_SHOP_ID));
                    memberProductShop.setShopName(Utils.getStringFromJsonObjectWithKey(joShop, JsonKey.COMMON_SHOP_NAME));
                    try {
                        JSONArray arrProductList = Utils.getArrayFromJsonObjectWithKey(joShop, JsonKey.AGENT_PRODUCT_LIST);
                        for (int j = 0; j < arrProductList.length(); j++) {
                            JSONObject joProduct = arrProductList.getJSONObject(j);
                            MemberProductShop.ProductInfo productInfo = new MemberProductShop.ProductInfo();
                            productInfo.productId = Utils.getIntegerFromJsonObjectWithKey(joProduct, JsonKey.COMMON_PRODUCT_ID);
                            productInfo.productName = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.COMMON_PRODUCT_NAME);
                            productInfo.productPrice = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.COMMON_PRODUCT_PRICE);
                            productInfo.productMoneyType = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.COMMON_PRODUCT_MONEY_TYPE);
                            productInfo.productChildren = Utils.getIntegerFromJsonObjectWithKey(joProduct, JsonKey.COMMON_PRODUCT_CHILDREN);
                            memberProductShop.getAgentProductList().add(productInfo);
                        }
                    } catch (JSONException e) {
                        Utils.log(e.getMessage());
                    }
                    dataList.add(memberProductShop);
                }

            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }

    public ArrayList<MemberProductShop> getDataList() {
        return dataList;
    }

    public static class MemberProductShop implements Serializable {

        public MemberProductShop() {
            productInfoList = new ArrayList<>();
        }

        private Integer shopId;
        private String shopName;
        private ArrayList<ProductInfo> productInfoList;

        public Integer getShopId() {
            return shopId;
        }

        public void setShopId(Integer shopId) {
            this.shopId = shopId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public ArrayList<ProductInfo> getAgentProductList() {
            return productInfoList;
        }

        public static class ProductInfo implements Serializable {
            private Integer productId;
            private String productName;
            private String productPrice;
            private String productMoneyType;
            private int productChildren;

            public Integer getProductId() {
                return productId;
            }

            public String getProductName() {
                return productName;
            }

            public String getProductPrice() {
                return productPrice;
            }

            public String getProductMoneyType() {
                return productMoneyType;
            }

            public int getProductChildren() {
                return productChildren;
            }
        }
    }
}  