/**
 * Project Name: itee
 * File Name:	 JsonProductType.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-03-30
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
 * ClassName:JsonProductType <br/>
 * Function: entity of api productType. <br/>
 * Date: 2015-03-30 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonProductType extends BaseJsonObject implements Serializable {

    public JsonProductType(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {
            shoppingTypeArrayList = new ArrayList<>();
            productTypeArrayList = new ArrayList<>();
            try {
                JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
                JSONArray arrShoppingType = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.SHOPS_PRODUCT_TYPE_SHOPPING_TYPE);
                for (int i = 0; i < arrShoppingType.length(); i++) {
                    JSONObject joProductType = arrShoppingType.getJSONObject(i);
                    ProductType productType = new ProductType();
                    productType.typeId = Utils.getIntegerFromJsonObjectWithKey(joProductType, JsonKey.SHOPS_PRODUCT_TYPE_TYPE_ID);
                    productType.typeName = Utils.getStringFromJsonObjectWithKey(joProductType, JsonKey.SHOPS_PRODUCT_TYPE_TYPE_NAME);
                    productType.typeEdit = 0;
                    shoppingTypeArrayList.add(productType);
                }
                JSONArray arrProductType = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.SHOPS_PRODUCT_TYPE_PRODUCT_TYPE);
                for (int i = 0; i < arrProductType.length(); i++) {
                    JSONObject joProductType = arrProductType.getJSONObject(i);
                    ProductType productType = new ProductType();
                    productType.typeId = Utils.getIntegerFromJsonObjectWithKey(joProductType, JsonKey.SHOPS_PRODUCT_TYPE_TYPE_ID);
                    productType.typeName = Utils.getStringFromJsonObjectWithKey(joProductType, JsonKey.SHOPS_PRODUCT_TYPE_TYPE_NAME);
                    productType.typeEdit = Utils.getIntegerFromJsonObjectWithKey(joProductType, JsonKey.SHOPS_PRODUCT_TYPE_TYPE_EDIT);
                    productTypeArrayList.add(productType);
                }
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }

    private ArrayList<ProductType> shoppingTypeArrayList;
    private ArrayList<ProductType> productTypeArrayList;

    public static class ProductType implements Serializable {
        private int typeId;
        private String typeName;
        private int typeEdit;

        public int getTypeId() {
            return typeId;
        }

        public String getTypeName() {
            return typeName;
        }

        public int getTypeEdit() {
            return typeEdit;
        }
    }

    public ArrayList<ProductType> getShoppingTypeArrayList() {
        return shoppingTypeArrayList;
    }

    public ArrayList<ProductType> getProductTypeArrayList() {
        return productTypeArrayList;
    }
}