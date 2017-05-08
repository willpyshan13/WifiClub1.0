/**
 * Project Name: itee
 * File Name:  JsonCalendar.java
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

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonCalendar <br/>
 * Function: Set Calendar. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class JsonCalendar extends BaseJsonObject implements Serializable {

    private ArrayList<Data> dataList;
    private ArrayList<String> myList;

    public JsonCalendar(JSONObject jsonObject) {
        super(jsonObject);
    }

    public ArrayList<Data> getDataList() {
        return dataList;
    }

    public ArrayList<String> getMyList() {
        return myList;
    }

    public void setMyList(ArrayList<String> myList) {
        this.myList = myList;
    }

    public void setDataList(ArrayList<Data> dataList) {
        this.dataList = dataList;
    }

    public class Data implements Serializable {
        private String bookingDate;

        public String getBookingDate() {
            return bookingDate;
        }

        public void setBookingDate(String bookingDate) {
            this.bookingDate = bookingDate;
        }
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        try {

            ArrayList<Data> myDataList = new ArrayList<>();
            ArrayList<String> list = new ArrayList<>();
            JSONArray arrDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.CALENDAR_BOOK_DATE_LIST);
            for (int i = 0; i < arrDataList.length(); i++) {
                JSONObject joData = arrDataList.getJSONObject(i);
                Data data = new Data();
                data.setBookingDate(Utils.getStringFromJsonObjectWithKey(joData, JsonKey.CALENDAR_BOOKING_DATE));
                myDataList.add(data);
                list.add(Utils.getStringFromJsonObjectWithKey(joData, JsonKey.CALENDAR_BOOKING_DATE));
            }

            setDataList(myDataList);
            setMyList(list);
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }
}
