/**
 * Project Name: itee
 * File Name:  JsonEventsEditGet.java
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
 * ClassName:JsonEventsEditGet <br/>
 * Function: To edit events. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonEventsEditGet extends BaseJsonObject implements Serializable {

    public JsonEventsEditGet(JSONObject jsonObject) {
        super(jsonObject);
    }

    private EventInfo eventInfo;

    public static class EventInfo implements Serializable {
        public Integer eventId;
        public String eventName;
        public List<DateInfo> dateInfoList;

        public static class DateInfo implements Serializable {

            public Integer eventDateId;
            public String eventDate;
            public String eventStartTime;
            public String eventEndTime;
            public String courseAreaName;
            public String courseAreaId;

            public Integer getEventDateId() {
                return eventDateId;
            }

            public void setEventDateId(Integer eventDateId) {
                this.eventDateId = eventDateId;
            }

            public String getEventDate() {
                return eventDate;
            }

            public void setEventDate(String eventDate) {
                this.eventDate = eventDate;
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

            public String getCourseAreaName() {
                return courseAreaName;
            }

            public void setCourseAreaName(String courseAreaName) {
                this.courseAreaName = courseAreaName;
            }

            public String getCourseAreaId() {
                return courseAreaId;
            }

            public void setCourseAreaId(String courseAreaId) {
                this.courseAreaId = courseAreaId;
            }
        }

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

        public List<DateInfo> getDateInfoList() {
            return dateInfoList;
        }

        public void setDateInfoList(List<DateInfo> dateInfoList) {
            this.dateInfoList = dateInfoList;
        }
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        eventInfo = new EventInfo();
        eventInfo.dateInfoList = new ArrayList<>();
        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.EVENT_LIST);
            eventInfo.eventId = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.EVENT_ID);
            eventInfo.eventName = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.EVENT_NAME);

            JSONArray arrDateInfoList = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.EVENT_EDIT_DATE_INFO);
            for (int i = 0; i < arrDateInfoList.length(); i++) {
                JSONObject joDateInfoList = arrDateInfoList.getJSONObject(i);
                EventInfo.DateInfo df = new EventInfo.DateInfo();
                df.eventDateId = Utils.getIntegerFromJsonObjectWithKey(joDateInfoList, JsonKey.EVENT_EDIT_EVENT_DATE_ID);
                df.eventDate = Utils.getStringFromJsonObjectWithKey(joDateInfoList, JsonKey.EVENT_EDIT_EVENT_DATE);
                df.eventStartTime = Utils.getStringFromJsonObjectWithKey(joDateInfoList, JsonKey.EVENT_EDIT_EVENT_START_TIME);
                df.eventEndTime = Utils.getStringFromJsonObjectWithKey(joDateInfoList, JsonKey.EVENT_EDIT_END_TIME);
                df.courseAreaName = Utils.getStringFromJsonObjectWithKey(joDateInfoList, JsonKey.EVENT_EDIT_COURSE_AREA_NAME);
                df.courseAreaId = Utils.getStringFromJsonObjectWithKey(joDateInfoList, JsonKey.EVENT_EDIT_COURSE_AREA_ID);
                eventInfo.dateInfoList.add(df);

            }

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }

    public EventInfo getEventInfo() {
        return eventInfo;
    }

    public void setEventInfo(EventInfo eventInfo) {
        this.eventInfo = eventInfo;
    }
}
