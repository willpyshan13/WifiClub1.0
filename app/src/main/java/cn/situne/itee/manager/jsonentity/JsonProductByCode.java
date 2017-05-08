/**
 * Project Name: itee
 * File Name:	 JsonProductByCode.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-04-27
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
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
 * ClassName:JsonProductByCode <br/>
 * Function: entity of api productByCode. <br/>
 * Date: 2015-04-27 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonProductByCode extends BaseJsonObject {

    private String id;
    private String price;
    private String qty;
    private String name;
    private String salesTypeId;
    private String attrId;
    private String attriStatus;
    private ArrayList<Product> productList;

    public JsonProductByCode(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        productList = new ArrayList<>();
        if (jsonObj != null) {

            try {
                JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);

                id = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.SHOPPING_PRODUCT_BY_CODE_ID);
                name = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.SHOPPING_PRODUCT_BY_CODE_NAME);
                price = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.SHOPPING_PRODUCT_BY_CODE_PRICE);
                qty = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.SHOPPING_PRODUCT_BY_CODE_QTY);
                salesTypeId = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.SHOPPING_PRODUCT_BY_CODE_SALES_TYPE_ID);
                if (joDataList.has(JsonKey.SHOPPING_PRODUCT_BY_CODE_PRODUCT_LIST_ATTR_STATUS)) {
                    attriStatus = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.SHOPPING_PRODUCT_BY_CODE_PRODUCT_LIST_ATTR_STATUS);
                }

                if (!joDataList.isNull(JsonKey.SHOPPING_PRODUCT_BY_CODE_PRODUCT_LIST)) {
                    JSONArray arrProductList = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.SHOPPING_PRODUCT_BY_CODE_PRODUCT_LIST);
                    for (int i = 0; i < arrProductList.length(); i++) {
                        JSONObject joProduct = arrProductList.getJSONObject(i);
                        Product product = new Product();
                        product.productId = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.SHOPPING_PRODUCT_BY_CODE_PRODUCT_LIST_ID);
                        product.productName = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.SHOPPING_PRODUCT_BY_CODE_PRODUCT_LIST_NAME);
                        product.productPrice = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.SHOPPING_PRODUCT_BY_CODE_PRODUCT_LIST_PRICE);
                        product.productAttrId = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.SHOPPING_PRODUCT_BY_CODE_PRODUCT_LIST_ATTR_ID);
                        product.productAttrStatus = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.SHOPPING_PRODUCT_BY_CODE_PRODUCT_LIST_ATTR_STATUS);
                        product.productNumber = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.SHOPPING_PRODUCT_BY_CODE_PRODUCT_LIST_NUMBER);
                        productList.add(product);
                    }
                }

            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public String getQty() {
        return qty;
    }

    public String getName() {
        return name;
    }

    public String getSalesTypeId() {
        return salesTypeId;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getAttriStatus() {
        return attriStatus;
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public static class Product implements Serializable {
        private String productId;
        private String productName;
        private String productPrice;
        private String productAttrId;
        private String productAttrStatus;
        private String productNumber;

        public String getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public String getProductPrice() {
            return productPrice;
        }

        public String getProductAttrId() {
            return productAttrId;
        }

        public String getProductNumber() {
            return productNumber;
        }

        public String getProductAttrStatus() {
            return productAttrStatus;
        }

        public void setProductAttrId(String productAttrId) {
            this.productAttrId = productAttrId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public void setProductPrice(String productPrice) {
            this.productPrice = productPrice;
        }

        public void setProductAttrStatus(String productAttrStatus) {
            this.productAttrStatus = productAttrStatus;
        }

        public void setProductNumber(String productNumber) {
            this.productNumber = productNumber;
        }
    }

}