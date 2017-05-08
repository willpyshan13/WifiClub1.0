/**
 * Project Name: itee
 * File Name:  JsonEventListGet.java
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
 * ClassName:JsonEventListGet <br/>
 * Function: To get event list. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonEventListGet extends BaseJsonObject implements Serializable {

    private ArrayList<Event> event;

    public ArrayList<Event> getEvent() {
        return event;
    }

    public void setEvent(ArrayList<Event> event) {
        this.event = event;
    }

    public static class Event implements Serializable {

        public Integer eventId;
        public String eventName;
        public String eventStartDate;
        public String eventEndDate;
        public String eventStartTime;
        public String eventEndTime;
        public String eventPrize;
        public String eventCurrency;

        public Integer getEventId() {
            return eventId;
        }

        public void setEventId(Integer eventId) {
            this.eventId = eventId;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public String getEventStartDate() {
            return eventStartDate;
        }

        public void setEventStartDate(String eventStartDate) {
            this.eventStartDate = eventStartDate;
        }

        public String getEventEndDate() {
            return eventEndDate;
        }

        public void setEventEndDate(String eventEndDate) {
            this.eventEndDate = eventEndDate;
        }

        public String getEventStartTime() {
            return eventStartTime;
        }

        public void setEventStartTime(String eventStartTime) {
            this.eventStartTime = eventStartTime;
        }

        public String getEventEndTime() {
            return eventEndTime;
        }

        public void setEventEndTime(String eventEndTime) {
            this.eventEndTime = eventEndTime;
        }

        public String getEventPrize() {
            return eventPrize;
        }

        public void setEventPrize(String eventPrize) {
            this.eventPrize = eventPrize;
        }

        public String getEventCurrency() {
            return eventCurrency;
        }

        public void setEventCurrency(String eventCurrency) {
            this.eventCurrency = eventCurrency;
        }
    }

    public JsonEventListGet(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        event = new ArrayList<>();
        try {
            // JSONObject joDataList = jsonObj.getJSONObject(JsonKey.EVENT_LIST);
            JSONArray jsonArray = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.EVENT_LIST);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Event event1 = new Event();
                //cati.areaId = Utils.getStringFromJsonObjectWithKey(joCourseAreaTypeListItem, JsonKey.MAIN_COURSE_AREA_ITEM_AREA_ID);
                event1.eventId = Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.EVENT_ID);
                event1.eventName = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.EVENT_NAME);
                event1.eventStartDate = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.EVENT_START_DATE);
                event1.eventEndDate = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.EVENT_END_DATE);
                event1.eventStartTime = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.EVENT_START_TIME);
                event1.eventEndTime = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.EVENT_END_TIME);
                event1.eventPrize = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.EVENT_PRIZE);
                event1.eventCurrency = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.EVENT_CURRENCY);
                event.add(event1);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());

        }
    }

}
