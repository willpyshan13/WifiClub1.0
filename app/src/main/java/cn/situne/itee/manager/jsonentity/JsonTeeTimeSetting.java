/**
 * Project Name: itee
 * File Name:  JsonEditDoTeeTimeSetting.java
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
 * ClassName:JsonEditDoTeeTimeSetting <br/>
 * Function: edit tee time setting. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
@SuppressWarnings("UnusedDeclaration")
public class JsonTeeTimeSetting extends BaseJsonObject implements Serializable {
    public JsonTeeTimeSetting(JSONObject jsonObject) {
        super(jsonObject);
    }

    public DataList dataList;

    public DataList getDataList() {
        return dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }

    public static class DataList implements Serializable {

        private Integer courseId;
        private String startDate;
        private String endDate;
        private String sunrise;
        private String sunset;
        private String longitude;
        private String latitude;
        private String timeZone;
        private String firstTeeTime;
        private String lastTeeTime;
        private Integer gapTime;

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public ArrayList<TransferTime> transferTimeList;

        public Integer getCourseId() {
            return courseId;
        }

        public void setCourseId(Integer courseId) {
            this.courseId = courseId;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getFirstTeeTime() {
            return firstTeeTime;
        }

        public void setFirstTeeTime(String firstTeeTime) {
            this.firstTeeTime = firstTeeTime;
        }

        public String getLastTeeTime() {
            return lastTeeTime;
        }

        public void setLastTeeTime(String lastTeeTime) {
            this.lastTeeTime = lastTeeTime;
        }

        public Integer getGapTime() {
            return gapTime;
        }

        public void setGapTime(Integer gapTime) {
            this.gapTime = gapTime;
        }

        public ArrayList<TransferTime> getTransferTimeList() {
            return transferTimeList;
        }

        public void setTransferTimeList(ArrayList<TransferTime> transferTimeList) {
            this.transferTimeList = transferTimeList;
        }

        public static class TransferTime implements Serializable {

            Integer id;
            String name;
            String time;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new DataList();

        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            dataList.courseId = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_COURSE_ID);
            dataList.startDate = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_START_DATE);
            dataList.endDate = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_END_DATE);
            dataList.sunrise = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_SUNRISE);
            dataList.sunset = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_SUNSET);
            dataList.longitude = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_LONGITUDE);
            dataList.timeZone = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_TIME_ZONE);
            dataList.latitude = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_LATITUDE);

            dataList.firstTeeTime = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_FIRST_TEE_TIME);
            dataList.lastTeeTime = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_LAST_TEE_TIME);
            dataList.gapTime = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_GAP_TIME);

            dataList.transferTimeList = new ArrayList<>();

            JSONArray arrayTransferTime = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_TRANSFER_TIME);
            for (int i = 0; i < arrayTransferTime.length(); i++) {
                JSONObject joTransferTime = arrayTransferTime.getJSONObject(i);
                DataList.TransferTime transferTime = new DataList.TransferTime();
                transferTime.id = Utils.getIntegerFromJsonObjectWithKey(joTransferTime, JsonKey.TEE_TIME_EDIT_SETTING_TRANSFER_TIME_ID);
                transferTime.name = Utils.getStringFromJsonObjectWithKey(joTransferTime, JsonKey.TEE_TIME_EDIT_SETTING_TRANSFER_TIME_NAME);
                transferTime.time = Utils.getStringFromJsonObjectWithKey(joTransferTime, JsonKey.TEE_TIME_EDIT_SETTING_TRANSFER_TIME_TIME);
                dataList.transferTimeList.add(transferTime);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }
}
