/**
 * Project Name: itee
 * File Name:  JsonReservation.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
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
 * ClassName:JsonReservation <br/>
 * Function: To set reservation. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonReservation extends BaseJsonObject implements Serializable {

    private ArrayList<DataListItem> dataList;

    public JsonReservation(JSONObject jsonObject) {
        super(jsonObject);
    }

    public ArrayList<DataListItem> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<DataListItem> dataList) {
        this.dataList = dataList;
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
                dataList.add(dataListItem);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    public class DataListItem implements Serializable {

        private String bookingOrderNo;
        private String date;
        private String time;
        private String area;

        public String getBookingOrderNo() {
            return bookingOrderNo;
        }

        public String getDate() {
            return date;
        }

        public String getArea() {
            return area;
        }

        public String getTime() {
            return time;
        }
    }
}
