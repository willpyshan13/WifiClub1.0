/**
 * Project Name: itee
 * File Name:  JsonShopRentalProductGet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-23
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

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonShopRentalProductGet <br/>
 * Function: To get shop rental product. <br/>
 * Date: 2015-03-23 <br/>
 *
 * @author Luochao
 * @version 1.0
 * @see
 */
public class JsonShopRentalProductGet extends BaseJsonObject implements Serializable {
    private List<DataList> dataList;


    public List<DataList> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataList> dataList) {
        this.dataList = dataList;
    }

    public JsonShopRentalProductGet(JSONObject jsonObject) {
        super(jsonObject);
    }

    public JSONObject getmJsonObj() {
        return mJsonObj;
    }

    public void setmJsonObj(JSONObject mJsonObj) {
        this.mJsonObj = mJsonObj;
    }

    private JSONObject mJsonObj;

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        this.mJsonObj = jsonObj;
        dataList = new ArrayList();
        try {
            JSONArray joDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
            for (int i = 0; i < joDataList.length(); i++) {
                JSONObject jsonObject = joDataList.getJSONObject(i);
                DataList data = new DataList();
                data.setPdId(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_RENTAL_PRODUCT_PD_ID));
                data.setPdPicId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_RENTAL_PRODUCT_PD_PIC_id));
                data.setBookingFee(Utils.getDoubleFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_RENTAL_PRODUCT_BOOKING_FEE));
                data.setProductName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_RENTAL_PRODUCT_PRODUCT_NAME));
                dataList.add(data);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }


    }

    public static class DataList implements Serializable {


        private String pdId;
        private int pdPicId;

        public int getPdPicId() {
            return pdPicId;
        }

        public void setPdPicId(int pdPicId) {
            this.pdPicId = pdPicId;
        }

        private String productName;
        private double bookingFee;

        public String getPdId() {
            return pdId;
        }

        public void setPdId(String pdId) {
            this.pdId = pdId;
        }


        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public double getBookingFee() {
            return bookingFee;
        }

        public void setBookingFee(double bookingFee) {
            this.bookingFee = bookingFee;
        }
    }
}
