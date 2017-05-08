/**
 * Project Name: itee
 * File Name:	 JsonProductDetailGet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-04-13
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;
/**
 * ClassName:JsonProductDetailGet <br/>
 * Function: JsonProductDetai 实体类 <br/>

 * Date: 2015-04-13 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonProductDetailGet extends BaseJsonObject {
    public JsonProductDetailGet(JSONObject jsonObject) {
        super(jsonObject);

    }

    private int productId;
    private String productName;
    private double price;
    private int qty;
    private String code;
    private int returnTime;
    private int reserveStatus;
    private int propertyPriceStatus;
    private int propertyStatus;
    private int productPicId;

    private String unlimitedFlag;

    public String getUnlimitedFlag() {
        return unlimitedFlag;
    }

    public void setUnlimitedFlag(String unlimitedFlag) {
        this.unlimitedFlag = unlimitedFlag;
    }

    public int getProductPicId() {
        return productPicId;
    }

    public void setProductPicId(int productPicId) {
        this.productPicId = productPicId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(int returnTime) {
        this.returnTime = returnTime;
    }

    public int getReserveStatus() {
        return reserveStatus;
    }

    public void setReserveStatus(int reserveStatus) {
        this.reserveStatus = reserveStatus;
    }

    public int getPropertyPriceStatus() {
        return propertyPriceStatus;
    }

    public void setPropertyPriceStatus(int propertyPriceStatus) {
        this.propertyPriceStatus = propertyPriceStatus;
    }

    public int getPropertyStatus() {
        return propertyStatus;
    }

    public void setPropertyStatus(int propertyStatus) {
        this.propertyStatus = propertyStatus;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {

            try {
                JSONObject jsDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
                this.setProductId(Utils.getIntegerFromJsonObjectWithKey(jsDataList, JsonKey.SHOPS_RENTAL_PRODUCT_ID));
                this.setUnlimitedFlag(Utils.getStringFromJsonObjectWithKey(jsDataList, JsonKey.SHOPS_UNLIMITED_FLAG));
                this.setProductName(Utils.getStringFromJsonObjectWithKey(jsDataList, JsonKey.SHOPS_RENTAL_PRODUCT_NAME));
                this.setPrice(Utils.getDoubleFromJsonObjectWithKey(jsDataList, JsonKey.SHOPS_RENTAL_PRICE));
                this.setQty(Utils.getIntegerFromJsonObjectWithKey(jsDataList, JsonKey.SHOPS_RENTAL_QTY));
                this.setCode(Utils.getStringFromJsonObjectWithKey(jsDataList, JsonKey.SHOPS_RENTAL_CODE));
                this.setReturnTime(Utils.getIntegerFromJsonObjectWithKey(jsDataList, JsonKey.SHOPS_RENTAL_RETURN_TIME));
                this.setReserveStatus(Utils.getIntegerFromJsonObjectWithKey(jsDataList, JsonKey.SHOPS_RENTAL_RESERVE_STATUS));
                this.setPropertyPriceStatus(Utils.getIntegerFromJsonObjectWithKey(jsDataList, JsonKey.SHOPS_RENTAL_PROPERTY_PRICE_STATUS));
                this.setPropertyStatus(Utils.getIntegerFromJsonObjectWithKey(jsDataList, JsonKey.SHOPS_RENTAL_PROPERTY_STATUS));
                this.setProductPicId(Utils.getIntegerFromJsonObjectWithKey(jsDataList, JsonKey.SHOPS_RENTAL_PD_PIC_ID));
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }
}
