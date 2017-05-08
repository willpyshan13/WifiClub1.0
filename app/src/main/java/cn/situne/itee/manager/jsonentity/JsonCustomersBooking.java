/**
 * Project Name: itee
 * File Name:  JsonCustomersBooking.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-11
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
 * ClassName:JsonCustomersBooking <br/>
 * Function: To set customers booking. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author luochao
 * @version 1.0
 * @see
 */
public class JsonCustomersBooking extends BaseJsonObject implements Serializable {

    private static final long serialVersionUID = -1529846374946979809L;

    public JsonCustomersBooking(JSONObject jsonObject) {
        super(jsonObject);
    }

    private DataList dataList;

    public DataList getDataList() {
        return dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }

    public static class DataList implements Serializable {

        private List<CategoryCourseListItem> categoryCourseList;

        private List<BookingListItem> bookingList;

        public List<BookingListItem> getBookingList() {
            return bookingList;
        }

        public void setBookingList(List<BookingListItem> bookingList) {
            this.bookingList = bookingList;
        }

        public List<CategoryCourseListItem> getCategoryCourseList() {
            return categoryCourseList;
        }
    }

    public static class CategoryCourseListItem implements Serializable {


        public Integer id;
        public Integer status;
        public Integer productId;
        public String price;
        public boolean isCheck;


    }

    public static class BookingListItem implements Serializable {

        private String bookingTime;
        private String courseAreaType;
        private Integer courseAreaId;
        private Integer transferAreaId;
        private String transferAreaType;
        private String transferTime;
        private String transferFlag;
        private String signId;
        private String signType;

        public String getBookingTime() {
            return bookingTime;
        }

        public String getCourseAreaType() {
            return courseAreaType;
        }

        public Integer getCourseAreaId() {
            return courseAreaId;
        }

        public Integer getTransferAreaId() {
            return transferAreaId;
        }

        public String getTransferAreaType() {
            return transferAreaType;
        }

        public String getTransferTime() {
            return transferTime;
        }

        public String getTransferFlag() {
            return transferFlag;
        }

        public String getSignId() {
            return signId;
        }

        public String getSignType() {
            return signType;
        }
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new DataList();
        try {

            JSONObject jsonObject = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            dataList.categoryCourseList = new ArrayList<>();
            if (!jsonObject.isNull(JsonKey.TEE_TIME_BOOKING_DETAIL_CATEGORY_COURSE_LIST)) {
                JSONArray jsonCategoryCourseList = Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_BOOKING_DETAIL_CATEGORY_COURSE_LIST);
                for (int j = 0; j < jsonCategoryCourseList.length(); j++) {
                    JSONObject jsonCategoryCourseListItem = jsonCategoryCourseList.getJSONObject(j);
                    CategoryCourseListItem categoryCourseListItem = new CategoryCourseListItem();
                    categoryCourseListItem.id = Utils.getIntegerFromJsonObjectWithKey(jsonCategoryCourseListItem, JsonKey.TEE_TIME_CATEGORY_COURSE_LIST_ITEM_ID);
                    categoryCourseListItem.status = Utils.getIntegerFromJsonObjectWithKey(jsonCategoryCourseListItem, JsonKey.TEE_TIME_CATEGORY_COURSE_LIST_ITEM_STATUS);
                    categoryCourseListItem.productId = Utils.getIntegerFromJsonObjectWithKey(jsonCategoryCourseListItem, JsonKey.TEE_TIME_CATEGORY_COURSE_LIST_ITEM_PRODUCT_ID);
                    categoryCourseListItem.price = Utils.getStringFromJsonObjectWithKey(jsonCategoryCourseListItem, JsonKey.TEE_TIME_CATEGORY_COURSE_LIST_ITEM_PRICE);
                    dataList.categoryCourseList.add(categoryCourseListItem);
                }
            }

            List<BookingListItem> bookingList = new ArrayList<>();
            JSONArray arrBookingList = Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_BOOKING_DETAIL_BOOKING_LIST);
            for (int i = 0; i < arrBookingList.length(); i++) {
                JSONObject joBookingListItem = arrBookingList.getJSONObject(i);
                BookingListItem bli = new BookingListItem();
                bli.bookingTime = Utils.getStringFromJsonObjectWithKey(joBookingListItem, JsonKey.TEE_TIME_CUSTOMERS_BOOKING_TIME);
                bli.courseAreaType = Utils.getStringFromJsonObjectWithKey(joBookingListItem, JsonKey.TEE_TIME_CUSTOMERS_BOOKING_COURSE_AREA_TYPE);
                bli.courseAreaId = Utils.getIntegerFromJsonObjectWithKey(joBookingListItem, JsonKey.TEE_TIME_CUSTOMERS_BOOKING_COURSE_AREA_ID);
                bli.transferAreaId = Utils.getIntegerFromJsonObjectWithKey(joBookingListItem, JsonKey.TEE_TIME_CUSTOMERS_BOOKING_TRANSFER_AREA_ID);
                bli.transferTime = Utils.getStringFromJsonObjectWithKey(joBookingListItem, JsonKey.TEE_TIME_CUSTOMERS_BOOKING_TRANSFER_TIME);
                bli.transferFlag = Utils.getStringFromJsonObjectWithKey(joBookingListItem, JsonKey.TEE_TIME_CUSTOMERS_BOOKING_TRANSFER_FLAG);
                bli.transferAreaType = Utils.getStringFromJsonObjectWithKey(joBookingListItem, JsonKey.TEE_TIME_CUSTOMERS_BOOKING_TRANSFER_AREA_TYPE);
                bli.signId = Utils.getStringFromJsonObjectWithKey(joBookingListItem, JsonKey.TEE_TIME_CUSTOMERS_BOOKING_SIGN_ID);
                bli.signType = Utils.getStringFromJsonObjectWithKey(joBookingListItem, JsonKey.TEE_TIME_CUSTOMERS_BOOKING_SIGN_TYPE);
                bookingList.add(bli);
            }
            dataList.setBookingList(bookingList);

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }
}
