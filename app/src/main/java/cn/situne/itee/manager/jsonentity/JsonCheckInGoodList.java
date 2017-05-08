/**
 * Project Name: itee
 * File Name:	 JsonCheckInGoodList.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-03-23
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
 * ClassName:JsonCheckInGoodList <br/>
 * Function: To CheckIn Good List. <br/>
 * Date: 2015-03-23 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonCheckInGoodList extends BaseJsonObject implements Serializable {


    private DataList dataList;

    public DataList getDataList() {
        return dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }

    public JsonCheckInGoodList(JSONObject jsonObject) {
        super(jsonObject);
    }

    public static class DataList implements Serializable {

        private String bookingDate;
        private String bookingUser;
        private String bookingTele;

        private String signGuest;

        private String guestFlag;
        private String guestUserId;

        public String getGuestFlag() {
            return guestFlag;
        }

        public void setGuestFlag(String guestFlag) {
            this.guestFlag = guestFlag;
        }

        public String getGuestUserId() {
            return guestUserId;
        }

        public void setGuestUserId(String guestUserId) {
            this.guestUserId = guestUserId;
        }

        public String getSignGuest() {
            return signGuest;
        }

        public void setSignGuest(String signGuest) {
            this.signGuest = signGuest;
        }

        private int signCheckIn;

        private ArrayList<GoodInfo> checkList;

        public int getSignCheckIn() {
            return signCheckIn;
        }

        public void setSignCheckIn(int signCheckIn) {
            this.signCheckIn = signCheckIn;
        }

        public String getBookingDate() {
            return bookingDate;
        }

        public void setBookingDate(String bookingDate) {
            this.bookingDate = bookingDate;
        }

        public String getBookingUser() {
            return bookingUser;
        }

        public void setBookingUser(String bookingUser) {
            this.bookingUser = bookingUser;
        }

        public String getBookingTele() {
            return bookingTele;
        }

        public void setBookingTele(String bookingTele) {
            this.bookingTele = bookingTele;
        }

        public ArrayList<GoodInfo> getCheckList() {
            return checkList;
        }

        public void setCheckList(ArrayList<GoodInfo> checkList) {
            this.checkList = checkList;
        }
    }

    public static class GoodInfo implements Serializable {
        private Integer categoryId;
        private String productAttr;
        private Integer productNum;

        public Integer getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }

        public String getProductAttr() {
            return productAttr;
        }

        public void setProductAttr(String productAttr) {
            this.productAttr = productAttr;
        }

        public Integer getProductNum() {
            return productNum;
        }

        public void setProductNum(Integer productNum) {
            this.productNum = productNum;
        }
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {
            dataList = new DataList();
            try {
                JSONObject jsonObject = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);

                dataList.setBookingUser(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_BOOKING_BOOKING_USER));
                dataList.setBookingDate(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_BOOKING_BOOKING_DATE));
                dataList.setBookingTele(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_BOOKING_BOOKING_TELE));
                dataList.setSignCheckIn(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_BOOKING_SIGN_CHECK_IN));
                dataList.setGuestFlag(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_GUEST_FLAGT));
                dataList.setGuestUserId(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_GUEST_USERID));
                dataList.setSignGuest(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_BOOKING_SIGN_GUEST));

                ArrayList<GoodInfo> checkListTemp = new ArrayList<>();
                JSONArray arrDataList = Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_BOOKING_CHECK_LIST);
                for (int i = 0; i < arrDataList.length(); i++) {
                    JSONObject joShop = arrDataList.getJSONObject(i);
                    GoodInfo goodinfo = new GoodInfo();
                    goodinfo.setCategoryId(Utils.getIntegerFromJsonObjectWithKey(joShop, JsonKey.TEE_TIME_BOOKING_CATEGORY_ID));
                    goodinfo.setProductAttr(Utils.getStringFromJsonObjectWithKey(joShop, JsonKey.TEE_TIME_BOOKING_PRODUCT_ATTR));
                    goodinfo.setProductNum(Utils.getIntegerFromJsonObjectWithKey(joShop, JsonKey.TEE_TIME_BOOKING_PRODUCT_NUM));
                    checkListTemp.add(goodinfo);
                }
                dataList.setCheckList(checkListTemp);
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }

}