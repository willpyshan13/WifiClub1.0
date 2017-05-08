/**
 * Project Name: itee
 * File Name:	 JsonProduct.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-04-18
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonProduct <br/>
 * Function: FIXME. <br/>
 * Date: 2015-04-18 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonProduct extends BaseJsonObject {

    private int page;
    private ArrayList<ProductData> productList;

    public JsonProduct(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        productList = new ArrayList<>();

        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            page = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.COMMON_PAGE);
            JSONArray arrProductList = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.SHOPS_PRODUCT_LIST);
            for (int j = 0; j < arrProductList.length(); j++) {
                JSONObject joProduct = arrProductList.getJSONObject(j);
                ProductData product = new ProductData();
                product.id = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_TYPE_LIST_PRODUCT_LIST_ID);
                product.name = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_TYPE_LIST_PRODUCT_LIST_NAME);
                product.price = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_TYPE_LIST_PRODUCT_LIST_PRICE);
                product.enableProperty = Utils.getIntegerFromJsonObjectWithKey(joProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_TYPE_LIST_PRODUCT_LIST_ENABLE_PROPERTY);
                product.code = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_PACKAGE_PRODUCT_CODE);
                product.qty = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_PACKAGE_PRODUCT_QTY);
                product.attrCount = Utils.getIntegerFromJsonObjectWithKey(joProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_ATTR_COUNT);
                product.unlimitedFlag = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.SHOPS_UNLIMITED_FLAG);
                productList.add(product);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    public int getPage() {
        return page;
    }

    public ArrayList<ProductData> getProductList() {
        return productList;
    }

    public static class ProductData {

        private String id;
        private String name;
        private String price;
        private int enableProperty;
        private String qty;
        private String code;
        private int attrCount;
        private String unlimitedFlag;

        public String getUnlimitedFlag() {
            return unlimitedFlag;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getEnableProperty() {
            return enableProperty;
        }

        public void setEnableProperty(int enableProperty) {
            this.enableProperty = enableProperty;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getAttrCount() {
            return attrCount;
        }

        public void setAttrCount(int attrCount) {
            this.attrCount = attrCount;
        }
    }
}