/**
 * Project Name: itee
 * File Name:  JsonMain.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-02-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonMain <br/>
 * Function: To set Main. <br/>
 * Date: 2015-02-11 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonMain extends BaseJsonObject implements Serializable {

    private static final long serialVersionUID = -1529846374946979809L;

    public JsonMain(JSONObject jsonObject) {
        super(jsonObject);
    }

    private DataList dataList;

    public static class DataList implements Serializable {
        public String startTime;
        public String endTime;
        public int timeInterval;
        public Integer eachGroupNum;

        private String timeStatus;
        private String bookingTimes;

        public String getBookingTimes() {
            return bookingTimes;
        }

        public void setBookingTimes(String bookingTimes) {
            this.bookingTimes = bookingTimes;
        }

        private ArrayList showTimeList;

        public ArrayList getShowTimeList() {
            return showTimeList;
        }

        public void setShowTimeList(ArrayList showTimeList) {
            this.showTimeList = showTimeList;
        }

        public String getTimeStatus() {
            return timeStatus;
        }

        public void setTimeStatus(String timeStatus) {
            this.timeStatus = timeStatus;
        }

        private ArrayList<PriceListItem> priceList;

        public ArrayList<DataList.CourseAreaTypeItem> courseAreaTypeList;

        public ArrayList<DataList.LockListItem> lockList;

        public ArrayList<DataList.BookingListItem> bookingList;

        public static class CourseAreaTypeItem implements Serializable {
            public int areaId;
            public String areaName;
            public Integer lockStatus;
        }

        public static class LockListItem implements Serializable {
            public String lockAreaType;
            public String lockTime;
        }

        public static class BookingListItem implements Serializable {
            public String bookingArea;
            public Integer bookingAreaId;

            public Map<String, ArrayList<BookingDetailItem>> bookingDetailMap;

            public static class BookingDetailItem implements Serializable {
                public String bookingTime;
                public Integer bookingStatus;
                public Integer bookingSort;
                public Integer bookingColor;
            }
        }

        public static class PriceListItem implements Serializable {
            private String time;
            private String price;

            public String getTime() {
                return time;
            }

            public String getPrice() {
                return price;
            }
        }

        public String getStartTime() {
            return startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public int getTimeInterval() {
            return timeInterval;
        }

        public Integer getEachGroupNum() {
            return eachGroupNum;
        }

        public ArrayList<PriceListItem> getPriceList() {
            return priceList;
        }

        public ArrayList<CourseAreaTypeItem> getCourseAreaTypeList() {
            return courseAreaTypeList;
        }

        public ArrayList<LockListItem> getLockList() {
            return lockList;
        }

        public ArrayList<BookingListItem> getBookingList() {
            return bookingList;
        }
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new DataList();
        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            dataList.startTime = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.MAIN_START_TIME);
            dataList.endTime = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.MAIN_END_TIME);
            dataList.timeInterval = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.MAIN_TIME_INTERVAL);
            dataList.eachGroupNum = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.MAIN_EACH_GROUP_NUM);
            dataList.setTimeStatus(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.MAIN_TIME_STATUS));

            dataList.setBookingTimes(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.MAIN_BOOKING_TIMES));
            dataList.priceList = new ArrayList<>();
            JSONArray arrPriceList = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.MAIN_PRICE_LIST);
            for (int i = 0; i < arrPriceList.length(); i++) {
                JSONObject joPriceListItem = arrPriceList.getJSONObject(i);
                DataList.PriceListItem item = new DataList.PriceListItem();
                item.time = Utils.getStringFromJsonObjectWithKey(joPriceListItem, JsonKey.MAIN_PRICE_LIST_TIME);
                item.price = Utils.getStringFromJsonObjectWithKey(joPriceListItem, JsonKey.MAIN_PRICE_LIST_PRICE);
                dataList.priceList.add(item);
            }

            dataList.courseAreaTypeList = new ArrayList<>();
            JSONArray arrCourseAreaTypeList = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.MAIN_COURSE_AREA_TYPE);
            for (int i = 0; i < arrCourseAreaTypeList.length(); i++) {
                JSONObject joCourseAreaTypeListItem = arrCourseAreaTypeList.getJSONObject(i);
                DataList.CourseAreaTypeItem cati = new DataList.CourseAreaTypeItem();
                cati.areaId = Utils.getIntegerFromJsonObjectWithKey(joCourseAreaTypeListItem, JsonKey.MAIN_COURSE_AREA_ITEM_AREA_ID);
                cati.areaName = Utils.getStringFromJsonObjectWithKey(joCourseAreaTypeListItem, JsonKey.MAIN_COURSE_AREA_ITEM_AREA_NAME);
                cati.lockStatus = Utils.getIntegerFromJsonObjectWithKey(joCourseAreaTypeListItem, JsonKey.MAIN_COURSE_AREA_ITEM_LOCK_STATUS);
                dataList.courseAreaTypeList.add(cati);
            }

            dataList.lockList = new ArrayList<>();
            JSONArray arrLockList = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.MAIN_LOCK_LIST);
            for (int i = 0; i < arrLockList.length(); i++) {
                JSONObject joLockListItem = arrLockList.getJSONObject(i);
                DataList.LockListItem lli = new DataList.LockListItem();
                lli.lockAreaType = Utils.getStringFromJsonObjectWithKey(joLockListItem, JsonKey.MAIN_LOCK_AREA_ID);
                lli.lockTime = Utils.getStringFromJsonObjectWithKey(joLockListItem, JsonKey.MAIN_LOCK_TIME);
                dataList.lockList.add(lli);
            }

            dataList.bookingList = new ArrayList<>();
            JSONArray arrBookingList = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.MAIN_BOOKING_LIST);
            for (int i = 0; i < arrBookingList.length(); i++) {
                JSONObject joBookingListItem = arrBookingList.getJSONObject(i);
                DataList.BookingListItem bli = new DataList.BookingListItem();
                bli.bookingArea = Utils.getStringFromJsonObjectWithKey(joBookingListItem, JsonKey.MAIN_BOOKING_AREA);
                bli.bookingAreaId = Utils.getIntegerFromJsonObjectWithKey(joBookingListItem, JsonKey.MAIN_BOOKING_AREA_ID);
                bli.bookingDetailMap = new HashMap<>();

                JSONArray arrBooking = Utils.getArrayFromJsonObjectWithKey(joBookingListItem, JsonKey.MAIN_BOOKING);
                for (int j = 0; j < arrBooking.length(); j++) {
                    JSONObject joBookingItem = arrBooking.getJSONObject(j);
                    DataList.BookingListItem.BookingDetailItem bi = new DataList.BookingListItem.BookingDetailItem();
                    bi.bookingTime = Utils.getStringFromJsonObjectWithKey(joBookingItem, JsonKey.MAIN_BOOKING_TIME);
                    bi.bookingStatus = Utils.getIntegerFromJsonObjectWithKey(joBookingItem, JsonKey.MAIN_BOOKING_STATUS);
                    bi.bookingSort = Utils.getIntegerFromJsonObjectWithKey(joBookingItem, JsonKey.MAIN_BOOKING_SORT);
                    bi.bookingColor = Utils.getIntegerFromJsonObjectWithKey(joBookingItem, JsonKey.MAIN_BOOKING_COLOR);

                    String key = bi.bookingTime;
                    ArrayList<DataList.BookingListItem.BookingDetailItem> listBookingDetailItemList = bli.bookingDetailMap.get(key);
                    if (listBookingDetailItemList == null) {
                        listBookingDetailItemList = new ArrayList<>();
                    }
                    listBookingDetailItemList.add(bi);
                    bli.bookingDetailMap.put(key, listBookingDetailItemList);
                }
                dataList.bookingList.add(bli);
            }

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    public DataList getDataList() {
        return dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }
}
