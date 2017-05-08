/**
 * Project Name: itee
 * File Name:  JsonAppointment.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-01-27
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
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
 * ClassName:JsonAgentsPricingListGet <br/>
 * Function: Set Appointment. <br/>
 * Date: 2015-01-27<br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class JsonAppointment extends BaseJsonObject implements Serializable {

    private DataList dataList;


    private String bookingTimes;
    private String timeStatus;

    public String getBookingTimes() {
        return bookingTimes;
    }

    public void setBookingTimes(String bookingTimes) {
        this.bookingTimes = bookingTimes;
    }

    public String getTimeStatus() {
        return timeStatus;
    }

    public void setTimeStatus(String timeStatus) {
        this.timeStatus = timeStatus;
    }

    private Map<String, ArrayList<Appointment>> appointmentMap;

    public JsonAppointment(JSONObject jsonObject) {
        super(jsonObject);
    }

    public DataList getDataList() {
        return dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }

    public Map<String, ArrayList<JsonAppointment.Appointment>> getAppointmentMap() {
        return appointmentMap;
    }

    public void setAppointmentMap(Map<String, ArrayList<Appointment>> appointmentMap) {
        this.appointmentMap = appointmentMap;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        if (jsonObj != null) {
            Utils.log(jsonObj.toString());
        }
        try {

            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            DataList dl = new DataList();

            setTimeStatus(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.MAIN_TIME_STATUS));
            setBookingTimes(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.MAIN_BOOKING_TIMES));

            ArrayList<SegmentTime> mySegmentTimeList = new ArrayList<>();
            JSONArray arrSegmentTime = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.APPOINTMENT_SEGEMENT_TIME_LIST);
            for (int i = 0; i < arrSegmentTime.length(); i++) {
                JSONObject joSegmentTime = arrSegmentTime.getJSONObject(i);
                SegmentTime segmentTime = new SegmentTime();

                segmentTime.setSegmentTimeType(Utils.getStringFromJsonObjectWithKey(joSegmentTime, JsonKey.APPOINTMENT_SEGEMENT_TIME_TYPE));
                segmentTime.setMemberTypeId(Utils.getStringFromJsonObjectWithKey(joSegmentTime, JsonKey.APPOINTMENT_SEGEMENT_MEMBER_TYPE_ID));
                segmentTime.setSegmentTimeTypeId(Utils.getStringFromJsonObjectWithKey(joSegmentTime, JsonKey.APPOINTMENT_SEGEMENT_TIME_TYPE_ID));
                segmentTime.setTime(Utils.getStringFromJsonObjectWithKey(joSegmentTime, JsonKey.APPOINTMENT_SEGEMENT_TIME));
                segmentTime.setSegmentTimeSetting(Utils.getStringFromJsonObjectWithKey(joSegmentTime, JsonKey.APPOINTMENT_SEGEMENT_TIME_SETTING));
                segmentTime.setTransferTime(Utils.getIntegerFromJsonObjectWithKey(joSegmentTime, JsonKey.APPOINTMENT_TRANSFER_TIME));
                if (joSegmentTime.has(JsonKey.APPOINTMENT_OPEN_FLAG)) {
                    segmentTime.setOpenFlg(Utils.getIntegerFromJsonObjectWithKey(joSegmentTime, JsonKey.APPOINTMENT_OPEN_FLAG) == 1);
                }

                mySegmentTimeList.add(segmentTime);
            }
            dl.setSegmentTimeList(mySegmentTimeList);

            JSONArray arrAppointment = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.APPOINTMENT_APPOINTMENT_LIST);
            Map<String, ArrayList<Appointment>> myAppointmentMap = new HashMap<>();
            ArrayList<Appointment> app;
            for (int i = 0; i < arrAppointment.length(); i++) {
                JSONObject joAp = arrAppointment.getJSONObject(i);
                Appointment appointment = new Appointment();
                appointment.appointmentOrderNo = Utils.getStringFromJsonObjectWithKey(joAp, JsonKey.APPOINTMENT_APPOINTMENT_ORDER_NO);
                appointment.appointmentTime = Utils.getStringFromJsonObjectWithKey(joAp, JsonKey.APPOINTMENT_APPOINTMENT_TIME);
                appointment.bookingNo = Utils.getStringFromJsonObjectWithKey(joAp, JsonKey.APPOINTMENT_BOOKING_NO);
                appointment.appointmentMid = Utils.getIntegerFromJsonObjectWithKey(joAp, JsonKey.APPOINTMENT_APPOINTMENT_MID);
                appointment.appointmentMemberType = Utils.getStringFromJsonObjectWithKey(joAp, JsonKey.APPOINTMENT_APPOINTMENT_MEMBER_TYPE);
                appointment.memberGender = Utils.getIntegerFromJsonObjectWithKey(joAp, JsonKey.APPOINTMENT_MEMBER_GENDER);
                appointment.appointmentMName = Utils.getStringFromJsonObjectWithKey(joAp, JsonKey.APPOINTMENT_APPOINTMENT_M_NAME);
                appointment.payStatus = Utils.getIntegerFromJsonObjectWithKey(joAp, JsonKey.APPOINTMENT_PAY_STATUS);
                appointment.checkInStatus = Utils.getIntegerFromJsonObjectWithKey(joAp, JsonKey.APPOINTMENT_CHECK_IN_STATUS);
                appointment.depositStatus = Utils.getIntegerFromJsonObjectWithKey(joAp, JsonKey.APPOINTMENT_DEPOSIT_STATUS);
                appointment.appointmentSort = Utils.getIntegerFromJsonObjectWithKey(joAp, JsonKey.APPOINTMENT_APPOINTMENT_SORT);
                appointment.appointmentGoods = Utils.getStringFromJsonObjectWithKey(joAp, JsonKey.APPOINTMENT_APPOINTMENT_GOODS);
                appointment.appointmentGoodsStatus = Utils.getIntegerFromJsonObjectWithKey(joAp, JsonKey.APPOINTMENT_APPOINTMENT_GOODS_STATUS);
                appointment.currentHole = Utils.getIntegerFromJsonObjectWithKey(joAp, JsonKey.APPOINTMENT_CURRENT_HOLE);
                appointment.currentHoleStatus = Utils.getIntegerFromJsonObjectWithKey(joAp, JsonKey.APPOINTMENT_CURRENT_HOLE_STATUS);
                appointment.bookingColor = Utils.getIntegerFromJsonObjectWithKey(joAp, JsonKey.APPOINTMENT_BOOKING_COLOR);
                appointment.lookFlag = Utils.getStringFromJsonObjectWithKey(joAp, JsonKey.APPOINTMENT_LOOK_FLAG);
                appointment.selfFlag = Utils.getStringFromJsonObjectWithKey(joAp, JsonKey.APPOINTMENT_SELF_FLAG);
                appointment.sameDayFlag = Utils.getStringFromJsonObjectWithKey(joAp, JsonKey.APPOINTMENT_SAME_DAY_FLAG);

                // 页面显示数据的MAP组织
                String time = appointment.getAppointmentTime();
                app = myAppointmentMap.get(time);
                if (app == null) {
                    app = new ArrayList<>();
                }
                app.add(appointment);
                myAppointmentMap.put(time, app);
            }

            setAppointmentMap(myAppointmentMap);
            setDataList(dl);
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    public class DataList implements Serializable {
        private ArrayList<SegmentTime> segmentTimeList;

        public ArrayList<SegmentTime> getSegmentTimeList() {
            return segmentTimeList;
        }

        public void setSegmentTimeList(ArrayList<SegmentTime> segmentTimeList) {
            this.segmentTimeList = segmentTimeList;
        }
    }

    public class SegmentTime implements Serializable {
        private String segmentTimeType;
        private String memberTypeId;
        private String segmentTimeTypeId;
        private String time;
        private String segmentTimeSetting;
        private Integer transferTime;
        private boolean openFlg;

        public String getSegmentTimeType() {
            return segmentTimeType;
        }

        public void setSegmentTimeType(String segmentTimeType) {
            this.segmentTimeType = segmentTimeType;
        }

        public String getMemberTypeId() {
            return memberTypeId;
        }

        public void setMemberTypeId(String memberTypeId) {
            this.memberTypeId = memberTypeId;
        }

        public String getSegmentTimeTypeId() {
            return segmentTimeTypeId;
        }

        public void setSegmentTimeTypeId(String segmentTimeTypeId) {
            this.segmentTimeTypeId = segmentTimeTypeId;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getSegmentTimeSetting() {
            return segmentTimeSetting;
        }

        public void setSegmentTimeSetting(String segmentTimeSetting) {
            this.segmentTimeSetting = segmentTimeSetting;
        }

        public Integer getTransferTime() {
            return transferTime;
        }

        public void setTransferTime(Integer transferTime) {
            this.transferTime = transferTime;
        }

        public boolean isOpenFlg() {
            return openFlg;
        }

        public void setOpenFlg(boolean openFlg) {
            this.openFlg = openFlg;
        }
    }

    public class Appointment implements Serializable {
        private String appointmentOrderNo;
        private String appointmentTime;
        private String bookingNo;
        private Integer appointmentMid;
        private String appointmentMemberType;
        private Integer memberGender;
        private String appointmentMName;
        private Integer payStatus;
        private Integer checkInStatus;
        private Integer depositStatus;
        private Integer appointmentSort;
        private String appointmentGoods;
        private Integer appointmentGoodsStatus;
        private Integer currentHole;
        private Integer currentHoleStatus;
        private Integer bookingColor;
        private String lookFlag;
        private String selfFlag;
        private String sameDayFlag;

        public Integer getBookingColor() {
            return bookingColor;
        }

        public String getAppointmentOrderNo() {
            return appointmentOrderNo;
        }

        public String getAppointmentTime() {
            return appointmentTime;
        }

        public String getBookingNo() {
            return bookingNo;
        }

        public void setBookingNo(String bookingNo) {
            this.bookingNo = bookingNo;
        }

        public Integer getAppointmentMid() {
            return appointmentMid;
        }

        public String getAppointmentMemberType() {
            return appointmentMemberType;
        }

        public Integer getMemberGender() {
            return memberGender;
        }

        public String getAppointmentMName() {
            return appointmentMName;
        }

        public Integer getPayStatus() {
            return payStatus;
        }

        public Integer getCheckInStatus() {
            return checkInStatus;
        }

        public Integer getDepositStatus() {
            return depositStatus;
        }

        public Integer getAppointmentSort() {
            return appointmentSort;
        }

        public String getAppointmentGoods() {
            return appointmentGoods;
        }

        public Integer getAppointmentGoodsStatus() {
            return appointmentGoodsStatus;
        }

        public Integer getCurrentHole() {
            return currentHole;
        }

        public Integer getCurrentHoleStatus() {
            return currentHoleStatus;
        }

        public String getLookFlag() {
            return lookFlag;
        }

        public String getSelfFlag() {
            return selfFlag;
        }

        public String getSameDayFlag() {
            return sameDayFlag;
        }
    }
}
