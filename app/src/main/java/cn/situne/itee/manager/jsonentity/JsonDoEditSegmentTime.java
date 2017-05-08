/**
 * Project Name: itee
 * File Name:  JsonDoEditSegmentTime.java
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

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonDoEditSegmentTime <br/>
 * Function: To edit SegmentTime. <br/>
 * Date: 2015-02-05 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class JsonDoEditSegmentTime extends BaseJsonObject implements Serializable {
    private DataList dataList;

    public JsonDoEditSegmentTime(JSONObject jsonObject) {
        super(jsonObject);
    }

    public DataList getDataList() {
        return dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }

    public static class DataList implements Serializable {

        private String addId;

        private Integer courseId;
        private ArrayList<CourseArea> courseAreaList;
        private ArrayList<MemberTypeList> memberTypeListList;
        private ArrayList<SegmentTimes> segmentTimesList;

        public Integer getCourseId() {
            return courseId;
        }

        public void setCourseId(Integer courseId) {
            this.courseId = courseId;
        }

        public ArrayList<CourseArea> getCourseAreaList() {
            return courseAreaList;
        }

        public void setCourseAreaList(ArrayList<CourseArea> courseAreaList) {
            this.courseAreaList = courseAreaList;
        }

        public ArrayList<MemberTypeList> getMemberTypeList() {
            return memberTypeListList;
        }

        public void setMemberTypeList(ArrayList<MemberTypeList> memberTypeList) {
            this.memberTypeListList = memberTypeList;
        }

        public ArrayList<SegmentTimes> getSegmentTimesList() {
            return segmentTimesList;
        }

        public void setSegmentTimesList(ArrayList<SegmentTimes> segmentTimesList) {
            this.segmentTimesList = segmentTimesList;
        }

        public String getAddId() {
            return addId;
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

    public class MemberTypeList implements Serializable {
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

    public class MemberType implements Serializable {
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

    public class TransferName implements Serializable {
        private FromOrTo from;
        private FromOrTo to;

        public FromOrTo getFrom() {
            return from;
        }

        public void setFrom(FromOrTo from) {
            this.from = from;
        }

        public FromOrTo getTo() {
            return to;
        }

        public void setTo(FromOrTo to) {
            this.to = to;
        }
    }

    public class FromOrTo implements Serializable {
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
        private ArrayList<SegmentTime> segmentTimesList;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public ArrayList<SegmentTime> getSegmentTimesList() {
            return segmentTimesList;
        }

        public void setSegmentTimesList(ArrayList<SegmentTime> segmentTimesList) {
            this.segmentTimesList = segmentTimesList;
        }
    }

    public class SegmentTime implements Serializable {
        private Integer segmentTimeId;
        private String transferStartTime;
        private String transferEndTime;
        private Integer categoryId;
        private String categoryName;
        private ArrayList<Area> areaList;
        private ArrayList<MemberType> memberTypeList;
        private ArrayList<TransferName> transferNameList;
        private Integer threeToReserve;
        private String info;
        private Integer allReserveMember;
        private Integer conflictFlg;

        public Integer getConflictFlg() {
            return conflictFlg;
        }

        public void setConflictFlg(Integer conflictFlg) {
            this.conflictFlg = conflictFlg;
        }

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

        public ArrayList<Area> getAreaList() {
            return areaList;
        }

        public void setAreaList(ArrayList<Area> areaList) {
            this.areaList = areaList;
        }

        public ArrayList<MemberType> getMemberTypeList() {
            return memberTypeList;
        }

        public void setMemberTypeList(ArrayList<MemberType> memberTypeList) {
            this.memberTypeList = memberTypeList;
        }

        public ArrayList<TransferName> getTransferNameList() {
            return transferNameList;
        }

        public void setTransferNameList(ArrayList<TransferName> transferNameList) {
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
            DataList dl = new DataList();
            setDataList(dl);

            dl.addId = Utils.getStringFromJsonObjectWithKey(joData, JsonKey.SEGMENT_TIME_ADD_ID);
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

            ArrayList<MemberTypeList> myMemberTypeList = new ArrayList<>();
            JSONArray arrMemberType = Utils.getArrayFromJsonObjectWithKey(joData, JsonKey.SEGMENT_TIME_MEMBER_TYPE_LIST);
            for (int i = 0; i < arrMemberType.length(); i++) {
                JSONObject joMemberType = arrMemberType.getJSONObject(i);
                MemberTypeList myMemberType = new MemberTypeList();
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
                    mySegmentTime.setInfo(Utils.getStringFromJsonObjectWithKey(joSegmentTime, JsonKey.SEGMENT_TIME_INFO));
                    mySegmentTime.setAllReserveMember(Utils.getIntegerFromJsonObjectWithKey(joSegmentTime, JsonKey.SEGMENT_TIME_ALL_RESERVE_MEMBER));
                    mySegmentTime.setConflictFlg(Utils.getIntegerFromJsonObjectWithKey(joSegmentTime, JsonKey.EDIT_SEGMENT_TIME_CONFIICT_FLG));

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
                        myMemberTypes.setId(Utils.getIntegerFromJsonObjectWithKey(joMemberTypes, JsonKey.SEGMENT_TIME_ID));
                        myMemberTypes.setName(Utils.getStringFromJsonObjectWithKey(joMemberTypes, JsonKey.SEGMENT_TIME_NAME));
                        myMemberTypesList.add(myMemberTypes);
                    }
                    mySegmentTime.setMemberTypeList(myMemberTypesList);

                    ArrayList<TransferName> myTransferNameList = new ArrayList<>();
                    JSONArray arrTransferName = Utils.getArrayFromJsonObjectWithKey(joSegmentTime, JsonKey.SEGMENT_TIME_TRANSFER_NAME);
                    for (int n = 0; n < arrTransferName.length(); n++) {
                        JSONObject joTransferName = arrTransferName.getJSONObject(n);
                        TransferName transferName = new TransferName();

                        JSONObject joFrom = joTransferName.getJSONObject(JsonKey.SEGMENT_TIME_FROM);
                        FromOrTo from = new FromOrTo();
                        from.setId(Utils.getIntegerFromJsonObjectWithKey(joFrom, JsonKey.SEGMENT_TIME_ID));
                        from.setName(Utils.getStringFromJsonObjectWithKey(joFrom, JsonKey.SEGMENT_TIME_NAME));

                        JSONObject joTo = joTransferName.getJSONObject(JsonKey.SEGMENT_TIME_TO);
                        FromOrTo to = new FromOrTo();
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


        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }
}
