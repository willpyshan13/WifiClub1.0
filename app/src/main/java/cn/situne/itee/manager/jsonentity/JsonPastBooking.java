/**
 * Project Name: itee
 * File Name:  JsonPastBooking.java
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
 * ClassName:JsonPastBooking <br/>
 * Function: To set past booking. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonPastBooking extends BaseJsonObject implements Serializable {

    private List<DataListItem> dataList;

    public JsonPastBooking(JSONObject jsonObject) {
        super(jsonObject);
    }

    public List<DataListItem> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataListItem> dataList) {
        this.dataList = dataList;
    }


    public class DataListItem implements Serializable {

        private String bookingOrderNo;
        private String date;
        private String time;
        private String area;
        private Integer bookingStatus;

        public Integer getBookingStatus() {
            return bookingStatus;
        }

        public void setBookingStatus(Integer bookingStatus) {
            this.bookingStatus = bookingStatus;
        }

        public String getBookingOrderNo() {
            return bookingOrderNo;
        }

        public void setBookingOrderNo(String bookingOrderNo) {
            this.bookingOrderNo = bookingOrderNo;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getTime() {
            return time;
        }
    }


    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        dataList = new ArrayList<>();
        try {
            JSONArray joDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);

            for (int i = 0; i < joDataList.length(); i++) {
                JSONObject joDataListItem = joDataList.getJSONObject(i);
                DataListItem dataListItem = new DataListItem();
                dataListItem.bookingOrderNo = Utils.getStringFromJsonObjectWithKey(joDataListItem, JsonKey.PLAYER_RESERVATION_BOOKING_ORDER_NO);
                dataListItem.date = Utils.getStringFromJsonObjectWithKey(joDataListItem, JsonKey.PLAYER_RESERVATION_DATE);
                dataListItem.time = Utils.getStringFromJsonObjectWithKey(joDataListItem, JsonKey.PLAYER_RESERVATION_TIME);
                dataListItem.area = Utils.getStringFromJsonObjectWithKey(joDataListItem, JsonKey.PLAYER_RESERVATION_AREA);
                dataListItem.bookingStatus = Utils.getIntegerFromJsonObjectWithKey(joDataListItem, JsonKey.PLAYER_RESERVATION_BOOKING_STATUS);
                dataList.add(dataListItem);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }
}
