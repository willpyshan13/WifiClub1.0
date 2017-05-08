/**
 * Project Name: itee
 * File Name:  JsonProductTypeDetail.java
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
 * ClassName:JsonProductTypeDetail <br/>
 * Function: To set product type detail. <br/>
 * Date: 2015-03-23 <br/>
 *
 * @author Luochao
 * @version 1.0
 * @see
 */
public class JsonProductTypeDetail extends BaseJsonObject implements Serializable {

    private List<ProductTypeDetail> dataList;

    public List<ProductTypeDetail> getDataList() {
        return dataList;
    }

    public void setDataList(List<ProductTypeDetail> dataList) {
        this.dataList = dataList;
    }

    public JsonProductTypeDetail(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new ArrayList();

        try {
            JSONArray joDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
            for (int i = 0; i < joDataList.length(); i++) {
                JSONObject jsonObject = joDataList.getJSONObject(i);
                ProductTypeDetail bean = new ProductTypeDetail();

                bean.setReturnTime(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_RETURN_TIME));
                bean.setReserveStatus(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_RESERVE_STATUS));
                bean.setEnableSubclassStatus(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_ENABLE_SUBCLASS_STATUS));
                bean.setPrice(Utils.getDoubleFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_PRICE));
                bean.setQty(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_QTY));
                bean.setCode(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_CODE));

                dataList.add(bean);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }


    }

    class ProductTypeDetail implements Serializable {

        private String returnTime;
        private int reserveStatus;
        private int enableSubclassStatus;
        private double price;
        private int qty;
        private String code;

        public String getReturnTime() {
            return returnTime;
        }

        public void setReturnTime(String returnTime) {
            this.returnTime = returnTime;
        }

        public int getReserveStatus() {
            return reserveStatus;
        }

        public void setReserveStatus(int reserveStatus) {
            this.reserveStatus = reserveStatus;
        }

        public int getEnableSubclassStatus() {
            return enableSubclassStatus;
        }

        public void setEnableSubclassStatus(int enableSubclassStatus) {
            this.enableSubclassStatus = enableSubclassStatus;
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
    }
}





