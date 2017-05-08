/**
 * Project Name: itee
 * File Name:  JsonSegmentTime.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-02-05
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
 * ClassName:JsonSegmentTime <br/>
 * Function: To set SegmentTime. <br/>
 * Date: 2015-02-05 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonSegmentTime extends BaseJsonObject implements Serializable {

    private Data dataList;

    public JsonSegmentTime(JSONObject jsonObject) {
        super(jsonObject);
    }

    public Data getDataList() {
        return dataList;
    }

    public void setDataList(Data dataList) {
        this.dataList = dataList;
    }

    public static class Data implements Serializable {

        private Integer courseId;
        private List<CourseArea> courseAreaList;
        private List<MemberType> memberTypeList;
        private List<SegmentTimes> segmentTimesList;


        public Integer getCourseId() {
            return courseId;
        }

        public void setCourseId(Integer courseId) {
            this.courseId = courseId;
        }

        public List<CourseArea> getCourseAreaList() {
            return courseAreaList;
        }

        public void setCourseAreaList(List<CourseArea> courseAreaList) {
            this.courseAreaList = courseAreaList;
        }

        public List<MemberType> getMemberTypeList() {
            return memberTypeList;
        }

        public void setMemberTypeList(List<MemberType> memberTypeList) {
            this.memberTypeList = memberTypeList;
        }

        public List<SegmentTimes> getSegmentTimesList() {
            return segmentTimesList;
        }

        public void setSegmentTimesList(List<SegmentTimes> segmentTimesList) {
            this.segmentTimesList = segmentTimesList;
        }
    }

    public class Area implements Serializable {
        private Integer id;
        private String name;

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
    }

    public class CourseArea implements Serializable {
        private Integer areaId;
        private String areaName;

        public Integer getAreaId() {
            return areaId;
        }

        public void setAreaId(Integer areaId) {
            this.areaId = areaId;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }
    }

    public class MemberType implements Serializable {
        private Integer typeId;
        private String memberType;

        public Integer getTypeId() {
            return typeId;
        }

        public void setTypeId(Integer typeId) {
            this.typeId = typeId;
        }

        public String getMemberType() {
            return memberType;
        }

        public void setMemberType(String memberType) {
            this.memberType = memberType;
        }
    }

    public class TransferName implements Serializable {
        private From from;
        private To to;

        public From getFrom() {
            return from;
        }

        public void setFrom(From from) {
            this.from = from;
        }

        public To getTo() {
            return to;
        }

        public void setTo(To to) {
            this.to = to;
        }
    }

    public class From implements Serializable {
        private String name;
        private Integer id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }

    public class To implements Serializable {
        private String name;
        private Integer id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }

    public class SegmentTimes implements Serializable {
        private String date;
        private List<SegmentTime> segmentTimesList;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<SegmentTime> getSegmentTimesList() {
            return segmentTimesList;
        }

        public void setSegmentTimesList(List<SegmentTime> segmentTimesList) {
            this.segmentTimesList = segmentTimesList;
        }
    }

    public class SegmentTime implements Serializable {
        private Integer segmentTimeId;
        private String transferStartTime;
        private String transferEndTime;
        private Integer categoryId;
        private String categoryName;
        private List<Area> areaList;
        private List<MemberType> memberTypeList;
        private List<TransferName> transferNameList;
        private Integer threeToReserve;
        private Integer allReserveMember;
        private String info;

        public Integer getSegmentTimeId() {
            return segmentTimeId;
        }

        public void setSegmentTimeId(Integer segmentTimeId) {
            this.segmentTimeId = segmentTimeId;
        }

        public String getTransferStartTime() {
            return transferStartTime;
        }

        public void setTransferStartTime(String transferStartTime) {
            this.transferStartTime = transferStartTime;
        }

        public String getTransferEndTime() {
            return transferEndTime;
        }

        public void setTransferEndTime(String transferEndTime) {
            this.transferEndTime = transferEndTime;
        }

        public Integer getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public List<Area> getAreaList() {
            return areaList;
        }

        public void setAreaList(List<Area> areaList) {
            this.areaList = areaList;
        }

        public List<MemberType> getMemberTypeList() {
            return memberTypeList;
        }

        public void setMemberTypeList(List<MemberType> memberTypeList) {
            this.memberTypeList = memberTypeList;
        }

        public List<TransferName> getTransferNameList() {
            return transferNameList;
        }

        public void setTransferNameList(List<TransferName> transferNameList) {
            this.transferNameList = transferNameList;
        }

        public Integer getThreeToReserve() {
            return threeToReserve;
        }

        public void setThreeToReserve(Integer threeToReserve) {
            this.threeToReserve = threeToReserve;
        }

        public Integer getAllReserveMember() {
            return allReserveMember;
        }

        public void setAllReserveMember(Integer allReserveMember) {
            this.allReserveMember = allReserveMember;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        try {

            JSONObject joData = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            Data dl = new Data();

            dl.setCourseId(Utils.getIntegerFromJsonObjectWithKey(joData, JsonKey.SEGMENT_TIME_COURSE_ID));

            ArrayList<CourseArea> myCourseAreaList = new ArrayList<>();
            JSONArray arrCourseArea = Utils.getArrayFromJsonObjectWithKey(joData, JsonKey.DETAIL_SEGMENT_TIME_COURSE_AREA);
            for (int i = 0; i < arrCourseArea.length(); i++) {
                JSONObject joCourseArea = arrCourseArea.getJSONObject(i);
                CourseArea myCourseArea = new CourseArea();
                myCourseArea.setAreaId(Utils.getIntegerFromJsonObjectWithKey(joCourseArea, JsonKey.SEGMENT_TIME_AREA_ID));
                myCourseArea.setAreaName(Utils.getStringFromJsonObjectWithKey(joCourseArea, JsonKey.SEGMENT_TIME_AREA_NAME));
                myCourseAreaList.add(myCourseArea);

            }
            dl.setCourseAreaList(myCourseAreaList);

            ArrayList<MemberType> myMemberTypeList = new ArrayList<>();
            JSONArray arrMemberType = Utils.getArrayFromJsonObjectWithKey(joData, JsonKey.SEGMENT_TIME_MEMBER_TYPE_LIST);
            for (int i = 0; i < arrMemberType.length(); i++) {
                JSONObject joMemberType = arrMemberType.getJSONObject(i);
                MemberType myMemberType = new MemberType();
                myMemberType.setMemberType(Utils.getStringFromJsonObjectWithKey(joMemberType, JsonKey.SEGMENT_TIME_MEMBER_TYPE));
                myMemberType.setTypeId(Utils.getIntegerFromJsonObjectWithKey(joMemberType, JsonKey.SEGMENT_TIME_TYPE_ID));
                myMemberTypeList.add(myMemberType);
            }
            dl.setMemberTypeList(myMemberTypeList);

            ArrayList<SegmentTimes> mySegmentTimesList = new ArrayList<>();
            JSONArray arrSegmentTimes = Utils.getArrayFromJsonObjectWithKey(joData, JsonKey.DETAIL_SEGMENT_TIMES);
            for (int i = 0; i < arrSegmentTimes.length(); i++) {
                JSONObject joSegmentTimes = arrSegmentTimes.getJSONObject(i);
                SegmentTimes mySegmentTimes = new SegmentTimes();
                mySegmentTimes.setDate(Utils.getStringFromJsonObjectWithKey(joSegmentTimes, JsonKey.SEGMENT_TIME_DATE));

                ArrayList<SegmentTime> mySegmentTimeList = new ArrayList<>();
                JSONArray arrSegmentTime = Utils.getArrayFromJsonObjectWithKey(joSegmentTimes, JsonKey.SEGMENT_TIMES_LIST);
                for (int j = 0; j < arrSegmentTime.length(); j++) {

                    JSONObject joSegmentTime = arrSegmentTime.getJSONObject(j);
                    SegmentTime mySegmentTime = new SegmentTime();
                    mySegmentTime.setSegmentTimeId(Utils.getIntegerFromJsonObjectWithKey(joSegmentTime, JsonKey.SEGMENT_TIMES_ID));
                    mySegmentTime.setTransferStartTime(Utils.getStringFromJsonObjectWithKey(joSegmentTime, JsonKey.SEGMENT_TIME_TRANSFER_START_TIME));
                    mySegmentTime.setTransferEndTime(Utils.getStringFromJsonObjectWithKey(joSegmentTime, JsonKey.SEGMENT_TIME_TRANSFER_END_TIME));
                    mySegmentTime.setCategoryId(Utils.getIntegerFromJsonObjectWithKey(joSegmentTime, JsonKey.SEGMENT_TIME_CATEGORY_ID));
                    mySegmentTime.setCategoryName(Utils.getStringFromJsonObjectWithKey(joSegmentTime, JsonKey.SEGMENT_TIME_CATEGORY_NAME));
                    mySegmentTime.setThreeToReserve(Utils.getIntegerFromJsonObjectWithKey(joSegmentTime, JsonKey.SEGMENT_TIME_THREE_TO_RESERVE));
                    mySegmentTime.setAllReserveMember(Utils.getIntegerFromJsonObjectWithKey(joSegmentTime, JsonKey.SEGMENT_TIME_ALL_RESERVE_MEMBER));
                    mySegmentTime.setInfo(Utils.getStringFromJsonObjectWithKey(joSegmentTime, JsonKey.SEGMENT_TIME_INFO));

                    ArrayList<Area> myAreaList = new ArrayList<>();
                    JSONArray arrArea = Utils.getArrayFromJsonObjectWithKey(joSegmentTime, JsonKey.SEGMENT_TIME_AREA);
                    for (int n = 0; n < arrArea.length(); n++) {
                        JSONObject joArea = arrArea.getJSONObject(n);
                        Area area = new Area();
                        area.setId(Utils.getIntegerFromJsonObjectWithKey(joArea, JsonKey.SEGMENT_TIME_ID));
                        area.setName(Utils.getStringFromJsonObjectWithKey(joArea, JsonKey.SEGMENT_TIME_NAME));
                        myAreaList.add(area);
                    }
                    mySegmentTime.setAreaList(myAreaList);

                    ArrayList<MemberType> myMemberTypesList = new ArrayList<>();
                    JSONArray arrMemberTypes = Utils.getArrayFromJsonObjectWithKey(joSegmentTime, JsonKey.SEGMENT_TIME_MEMBER_TYPE);
                    for (int n = 0; n < arrMemberTypes.length(); n++) {
                        JSONObject joMemberTypes = arrMemberTypes.getJSONObject(n);
                        MemberType myMemberTypes = new MemberType();
                        myMemberTypes.setTypeId(Utils.getIntegerFromJsonObjectWithKey(joMemberTypes, JsonKey.SEGMENT_TIME_ID));
                        myMemberTypes.setMemberType(Utils.getStringFromJsonObjectWithKey(joMemberTypes, JsonKey.SEGMENT_TIME_NAME));
                        myMemberTypesList.add(myMemberTypes);
                    }
                    mySegmentTime.setMemberTypeList(myMemberTypesList);

                    ArrayList<TransferName> myTransferNameList = new ArrayList<>();
                    JSONArray arrTransferName = Utils.getArrayFromJsonObjectWithKey(joSegmentTime, JsonKey.SEGMENT_TIME_TRANSFER_NAME);
                    for (int n = 0; n < arrTransferName.length(); n++) {
                        JSONObject joTransferName = arrTransferName.getJSONObject(n);
                        TransferName transferName = new TransferName();

                        JSONObject joFrom = joTransferName.getJSONObject(JsonKey.SEGMENT_TIME_FROM);
                        From from = new From();
                        from.setId(Utils.getIntegerFromJsonObjectWithKey(joFrom, JsonKey.SEGMENT_TIME_ID));
                        from.setName(Utils.getStringFromJsonObjectWithKey(joFrom, JsonKey.SEGMENT_TIME_NAME));

                        JSONObject joTo = joTransferName.getJSONObject(JsonKey.SEGMENT_TIME_TO);
                        To to = new To();
                        to.setId(Utils.getIntegerFromJsonObjectWithKey(joTo, JsonKey.SEGMENT_TIME_ID));
                        to.setName(Utils.getStringFromJsonObjectWithKey(joTo, JsonKey.SEGMENT_TIME_NAME));

                        transferName.setFrom(from);
                        transferName.setTo(to);
                        myTransferNameList.add(transferName);
                    }
                    mySegmentTime.setTransferNameList(myTransferNameList);
                    mySegmentTimeList.add(mySegmentTime);

                }
                mySegmentTimes.setSegmentTimesList(mySegmentTimeList);
                mySegmentTimesList.add(mySegmentTimes);
            }
            dl.setSegmentTimesList(mySegmentTimesList);

            setDataList(dl);
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }
}