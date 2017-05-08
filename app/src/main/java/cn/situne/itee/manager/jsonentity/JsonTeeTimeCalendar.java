/**
 * Project Name: itee
 * File Name:  JsonTeeTimeCalendar.java
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
 * ClassName:JsonTeeTimeCalendar <br/>
 * Function: To set Tee time calendar. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonTeeTimeCalendar extends BaseJsonObject implements Serializable {
    public JsonTeeTimeCalendar(JSONObject jsonObject) {
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

        public Integer courseId;
        public List<DataList.DateStatus> dateStatus;

        public Integer getCourseId() {
            return courseId;
        }

        public void setCourseId(Integer courseId) {
            this.courseId = courseId;
        }

        public List<DateStatus> getDateStatus() {
            return dateStatus;
        }

        public void setDateStatus(List<DateStatus> dateStatus) {
            this.dateStatus = dateStatus;
        }

        public static class DateStatus implements Serializable {
            public String date;
            public List<DataList.DateStatus.StatusList> statusList;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public List<StatusList> getStatusList() {
                return statusList;
            }

            public void setStatusList(List<StatusList> statusList) {
                this.statusList = statusList;
            }

            public static class StatusList implements Serializable {
                public Integer day;
                public String teeTimeSetting;
                public Integer block;
                public Integer nineHoles;
                public Integer threeTeeStart;
                public Integer memberOnly;
                public Integer booking;
                public Integer primeTime;
                public Integer twoTeeStart;

                public Integer getNineHoles() {
                    return nineHoles;
                }

                public void setNineHoles(Integer nineHoles) {
                    this.nineHoles = nineHoles;
                }

                public Integer getThreeTeeStart() {
                    return threeTeeStart;
                }

                public void setThreeTeeStart(Integer threeTeeStart) {
                    this.threeTeeStart = threeTeeStart;
                }

                public Integer getDay() {
                    return day;
                }

                public void setDay(Integer day) {
                    this.day = day;
                }

                public String getTeeTimeSetting() {
                    return teeTimeSetting;
                }

                public void setTeeTimeSetting(String teeTimeSetting) {
                    this.teeTimeSetting = teeTimeSetting;
                }

                public Integer getBlock() {
                    return block;
                }

                public void setBlock(Integer block) {
                    this.block = block;
                }

                public Integer getMemberOnly() {
                    return memberOnly;
                }

                public void setMemberOnly(Integer memberOnly) {
                    this.memberOnly = memberOnly;
                }

                public Integer getBooking() {
                    return booking;
                }

                public void setBooking(Integer booking) {
                    this.booking = booking;
                }

                public Integer getPrimeTime() {
                    return primeTime;
                }

                public void setPrimeTime(Integer primeTime) {
                    this.primeTime = primeTime;
                }

                public Integer getTwoTeeStart() {
                    return twoTeeStart;
                }

                public void setTwoTeeStart(Integer twoTeeStart) {
                    this.twoTeeStart = twoTeeStart;
                }
            }
        }


    }


    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new DataList();

        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            dataList.courseId = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_CALENDAR_COURSE_ID);
            dataList.dateStatus = new ArrayList<>();

            JSONArray arrDateStatus = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_CALENDAR_DATE_STATUS);
            for (int i = 0; i < arrDateStatus.length(); i++) {
                JSONObject joDateStatus = arrDateStatus.getJSONObject(i);
                DataList.DateStatus ds = new DataList.DateStatus();
                ds.date = Utils.getStringFromJsonObjectWithKey(joDateStatus, JsonKey.TEE_TIME_CALENDAR_DATE);
                ds.statusList = new ArrayList<>();

                try {
                    if (!joDateStatus.isNull(JsonKey.TEE_TIME_CALENDAR_STATUS_LIST)) {
                        JSONArray arrStatusList = Utils.getArrayFromJsonObjectWithKey(joDateStatus, JsonKey.TEE_TIME_CALENDAR_STATUS_LIST);
                        for (int j = 0; j < arrStatusList.length(); j++) {
                            JSONObject joStatusList = arrStatusList.getJSONObject(j);
                            DataList.DateStatus.StatusList sl = new DataList.DateStatus.StatusList();
                            sl.day = Utils.getIntegerFromJsonObjectWithKey(joStatusList, JsonKey.TEE_TIME_CALENDAR_DAY);
                            sl.teeTimeSetting = Utils.getStringFromJsonObjectWithKey(joStatusList, JsonKey.TEE_TIME_CALENDAR_TEE_TIME_SETTING);
                            sl.block = Utils.getIntegerFromJsonObjectWithKey(joStatusList, JsonKey.TEE_TIME_CALENDAR_BLOCK);
                            sl.nineHoles = Utils.getIntegerFromJsonObjectWithKey(joStatusList, JsonKey.TEE_TIME_CALENDAR_NINE_HOLES);
                            sl.threeTeeStart = Utils.getIntegerFromJsonObjectWithKey(joStatusList, JsonKey.TEE_TIME_CALENDAR_THREE_TEE_START);
                            sl.memberOnly = Utils.getIntegerFromJsonObjectWithKey(joStatusList, JsonKey.TEE_TIME_CALENDAR_MEMBER_ONLY);
                            sl.booking = Utils.getIntegerFromJsonObjectWithKey(joStatusList, JsonKey.TEE_TIME_CALENDAR_BOOKING);
                            sl.primeTime = Utils.getIntegerFromJsonObjectWithKey(joStatusList, JsonKey.TEE_TIME_CALENDAR_PRIME_TIME);
                            sl.twoTeeStart = Utils.getIntegerFromJsonObjectWithKey(joStatusList, JsonKey.TEE_TIME_CALENDAR_TWO_TEE_START);
                            ds.statusList.add(sl);
                        }
                    }
                } catch (JSONException e) {
                    Utils.log(e.getMessage());
                }
                dataList.dateStatus.add(ds);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }


    }
}
