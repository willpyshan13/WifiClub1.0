/**
 * Project Name: itee
 * File Name:  JsonStaffAuthorityGet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-19
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
 * ClassName:JsonStaffAuthorityGet <br/>
 * Function: To get staff authority. <br/>
 * Date: 2015-03-19 <br/>
 *
 * @author Luochao
 * @version 1.0
 * @see
 */
public class JsonStaffAuthorityGet extends BaseJsonObject implements Serializable {

    private List<DataItem> dataList;

    public List<DataItem> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataItem> dataList) {
        this.dataList = dataList;
    }

    public JsonStaffAuthorityGet(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);


        dataList = new ArrayList<>();

        if (jsonObj != null) {
            try {
                JSONArray joDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
                for (int i = 0; i < joDataList.length(); i++) {
                    boolean g = true;
                    JSONObject jsonObject = joDataList.getJSONObject(i);
                    DataItem item = new DataItem();
                    item.setAuName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_AUTHORITY_AU_NAME));
                    item.setAuId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_AUTHORITY_AU_ID));
                    item.setAuSon(Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_AUTHORITY_AU_SON));
                    item.setAuStatus(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_AUTHORITY_AU_STATUS));
                    dataList.add(item);
                }
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }

    public void setJsonValuesForArray(JSONArray joDataList) {

        dataList = new ArrayList();
        try {

            for (int i = 0; i < joDataList.length(); i++) {
                boolean g = true;
                JSONObject jsonObject = joDataList.getJSONObject(i);
                DataItem item = new DataItem();
                item.setAuName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_AUTHORITY_AU_NAME));
                item.setAuId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_AUTHORITY_AU_ID));
                item.setAuSon(Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_AUTHORITY_AU_SON));

                item.setAuStatus(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_AUTHORITY_AU_STATUS));
                dataList.add(item);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }


    public void setJsonValuesForArrayMember(JSONArray joDataList) {

        dataList = new ArrayList<>();
        try {

            for (int i = 0; i < joDataList.length(); i++) {

                JSONObject jsonObject = joDataList.getJSONObject(i);
                DataItem item = new DataItem();
                item.setMemberTypeName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_AUTHORITY_MEMBER_TYPE_NAME));
                item.setMemberTypeId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_AUTHORITY_MEMBER_TYPE_ID));
                item.setMemberStatus(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_AUTHORITY_MEMBER_STATUS));

                dataList.add(item);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }


    public void setJsonValuesForArrayShop(JSONArray joDataList) {

        dataList = new ArrayList<>();
        try {

            for (int i = 0; i < joDataList.length(); i++) {

                JSONObject jsonObject = joDataList.getJSONObject(i);
                DataItem item = new DataItem();
                item.setShopName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_AUTHORITY_SHOP_NAME));
                item.setShopId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_AUTHORITY_SHOP_ID));
                item.setShopStatus(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_AUTHORITY_SHOP_STATUS));
                dataList.add(item);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }


    public void setJsonValuesForArrayDiscount(JSONArray joDataList) {

        dataList = new ArrayList<>();
        try {

            for (int i = 0; i < joDataList.length(); i++) {

                JSONObject jsonObject = joDataList.getJSONObject(i);
                DataItem item = new DataItem();
                item.setAuStatus(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_AUTHORITY_AU_STATUS));
                item.setAuId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_AUTHORITY_AU_ID));
                item.setAuName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_AUTHORITY_AU_NAME));
                if (jsonObject.has(JsonKey.STAFF_AUTHORITY_DISCOUNT_MONEY))
                    item.setMoney(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_AUTHORITY_DISCOUNT_MONEY));
                if (jsonObject.has(JsonKey.STAFF_AUTHORITY_DISCOUNT_DISCOUNT))
                    item.setDiscount(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_AUTHORITY_DISCOUNT_DISCOUNT));

                dataList.add(item);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }


    public class DataItem implements Serializable {
        private String auName;
        private int auStatus;
        private int auId;
        private int shopId;
        private int shopStatus;
        private String shopName;


        private int memberTypeId;
        private int memberStatus;
        private String memberTypeName;


        public int getMemberTypeId() {
            return memberTypeId;
        }

        public void setMemberTypeId(int memberTypeId) {
            this.memberTypeId = memberTypeId;
        }

        public int getMemberStatus() {
            return memberStatus;
        }

        public void setMemberStatus(int memberStatus) {
            this.memberStatus = memberStatus;
        }

        public String getMemberTypeName() {
            return memberTypeName;
        }

        public void setMemberTypeName(String memberTypeName) {
            this.memberTypeName = memberTypeName;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public int getDiscount() {
            return discount;
        }

        public void setDiscount(int discount) {
            this.discount = discount;
        }

        private int discount;
        private int money;

        public int getShopId() {
            return shopId;
        }

        public void setShopId(int shopId) {
            this.shopId = shopId;
        }

        public int getShopStatus() {
            return shopStatus;
        }

        public void setShopStatus(int shopStatus) {
            this.shopStatus = shopStatus;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getAuName() {
            return auName;
        }

        public void setAuName(String auName) {
            this.auName = auName;
        }

        public int getAuStatus() {
            return auStatus;
        }

        public void setAuStatus(int auStatus) {
            this.auStatus = auStatus;
        }

        public int getAuId() {
            return auId;
        }

        public void setAuId(int auId) {
            this.auId = auId;
        }

        public JSONArray getAuSon() {
            return auSon;
        }

        public void setAuSon(JSONArray auSon) {
            this.auSon = auSon;
        }

        private JSONArray auSon;


    }


}

