/**
 * Project Name: itee
 * File Name:  JsonEventAreaGet.java
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
 * ClassName:JsonEventAreaGet <br/>
 * Function: To get vent area. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonEventAreaGet extends BaseJsonObject implements Serializable {

    private ArrayList<EventArea> event;

    public ArrayList<EventArea> getEvent() {
        return event;
    }

    public void setEvent(ArrayList<EventArea> event) {
        this.event = event;
    }

    public static class EventArea implements Serializable {

        public Integer CourseAreaId;
        public String CourseArea;

        public Integer getCourseAreaId() {
            return CourseAreaId;
        }

        public void setCourseAreaId(Integer courseAreaId) {
            CourseAreaId = courseAreaId;
        }

        public String getCourseArea() {
            return CourseArea;
        }

        public void setCourseArea(String courseArea) {
            CourseArea = courseArea;
        }
    }

    public JsonEventAreaGet(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        event = new ArrayList<>();
        try {
            JSONArray jsonArray = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.EVENT_LIST);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                EventArea event1 = new EventArea();
                event1.CourseAreaId = Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.EVENT_AREA_COURSE_AREA_ID);
                event1.CourseArea = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.EVENT_AREA_COURSE_AREA);
                event.add(event1);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());

        }
    }

}
