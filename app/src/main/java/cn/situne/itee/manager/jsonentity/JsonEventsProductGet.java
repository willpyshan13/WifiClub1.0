/**
 * Project Name: itee
 * File Name:	 JsonEventsProductGet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-03-17
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
 * ClassName:JsonEventsProductGet <br/>
 * Function: To get EventsProduct. <br/>
 * Date: 2015-03-17 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonEventsProductGet extends BaseJsonObject implements Serializable {

    public JsonEventsProductGet(JSONObject jsonObject) {
        super(jsonObject);
    }


    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new ArrayList<>();
        JSONArray arrDataList = null;
        try {
            arrDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);

            for (int i = 0; i < arrDataList.length(); i++) {
                JSONObject jsonShop = arrDataList.getJSONObject(i);
                EventsProductShop eps = new EventsProductShop();
                eps.setShopId(Utils.getIntegerFromJsonObjectWithKey(jsonShop, JsonKey.EVENT_SHOP_ID));
                eps.setShopName(Utils.getStringFromJsonObjectWithKey(jsonShop, JsonKey.EVENT_SHOP_NAME));
                ArrayList<EventsProductShop.ProductInfo> productList = new ArrayList<>();
                try {
                    JSONArray arrProductInfo = Utils.getArrayFromJsonObjectWithKey(jsonShop, JsonKey.EVENT_PRODUCT_LIST);
                    for (int j = 0; j < arrProductInfo.length(); j++) {
                        JSONObject jsonProduct = arrProductInfo.getJSONObject(j);
                        EventsProductShop.ProductInfo productInfo = new EventsProductShop.ProductInfo();
                        productInfo.setProductId(Utils.getIntegerFromJsonObjectWithKey(jsonProduct, JsonKey.EVENT_PRODUCT_ID));
                        productInfo.setProductName(Utils.getStringFromJsonObjectWithKey(jsonProduct, JsonKey.EVENT_PRODUCT_NAME));
                        productInfo.setProductPrice(Utils.getStringFromJsonObjectWithKey(jsonProduct, JsonKey.EVENT_PRODUCT_PRICE));
                        productInfo.setProductMoneyType(Utils.getStringFromJsonObjectWithKey(jsonProduct, JsonKey.EVENT_PRODUCT_MONEY_TYPE));
                        productList.add(productInfo);
                    }
                } catch (JSONException e) {
                    Utils.log(e.getMessage());
                }
                eps.setProductList(productList);
                dataList.add(eps);
            }

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    private ArrayList<EventsProductShop> dataList;

    public ArrayList<EventsProductShop> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<EventsProductShop> dataList) {
        this.dataList = dataList;
    }

    public static class EventsProductShop implements Serializable {

        private Integer shopId;
        private String shopName;
        private ArrayList<ProductInfo> productList;

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

        public ArrayList<ProductInfo> getProductList() {
            return productList;
        }

        public void setProductList(ArrayList<ProductInfo> productList) {
            this.productList = productList;
        }

        public static class ProductInfo implements Serializable {
            private Integer productId;
            private String productName;
            private String productPrice;
            private String productMoneyType;

            public Integer getProductId() {
                return productId;
            }

            public void setProductId(Integer productId) {
                this.productId = productId;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public String getProductPrice() {
                return productPrice;
            }

            public void setProductPrice(String productPrice) {
                this.productPrice = productPrice;
            }

            public String getProductMoneyType() {
                return productMoneyType;
            }

            public void setProductMoneyType(String productMoneyType) {
                this.productMoneyType = productMoneyType;
            }
        }
    }
}