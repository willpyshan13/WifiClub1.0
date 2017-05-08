/**
 * Project Name: itee
 * File Name:  JsonDetailSegmentTime.java
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
import java.util.LinkedHashMap;
import java.util.List;

import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.TimeArea;

/**
 * ClassName:JsonDetailSegmentTime <br/>
 * Function: To set Detail SegmentTime. <br/>
 * Date: 2015-02-05 <br/>
 *
 * @author xuyue
 * @version 1.0
 * @see
 */
public class JsonDetailSegmentTime extends BaseJsonObject implements Serializable {

    private static final int SEGMENT_TYPE_ID_9_HOLES_ONLY = Integer.valueOf(Constants.SEGMENT_TIME_NINE_HOLES_ONLY_ID);
    private static final int SEGMENT_TYPE_ID_BLOCK_TIME = Integer.valueOf(Constants.SEGMENT_TIME_BLOCK_TIMES_ID);
    private static final int SEGMENT_TYPE_ID_MEMBER_ONLY = Integer.valueOf(Constants.SEGMENT_TIME_MEMBER_ONLY_ID);
    private static final int SEGMENT_TIME_ID_PRIME_DISCOUNT = Integer.valueOf(Constants.SEGMENT_TIME_PRIME_DISCOUNT_ID);
    private static final int SEGMENT_TYPE_ID_TWO_TEE_START = Integer.valueOf(Constants.SEGMENT_TIME_TWO_TEE_START_ID);
    private static final int SEGMENT_TYPE_ID_THREE_TEE_START = Integer.valueOf(Constants.SEGMENT_TIME_THREE_TEE_START_ID);


    private LinkedHashMap<String, DetailSegmentCourse> detailsSegmentMap;

    private Data dataList;

    public JsonDetailSegmentTime(JSONObject jsonObject) {
        super(jsonObject);
    }

    public Data getDataList() {
        return dataList;
    }

    public void setDataList(Data dataList) {
        this.dataList = dataList;
    }

    public LinkedHashMap<String, DetailSegmentCourse> getDetailsSegmentMap() {
        return detailsSegmentMap;
    }

    public static class Data implements Serializable {
        private ArrayList<CourseArea> courseAreaList;
        private String startTime;
        private String endTime;
        private int gap;
        private ArrayList<SegmentTimes> segmentTimesList;

        public ArrayList<CourseArea> getCourseAreaList() {
            return courseAreaList;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public Integer getGap() {
            return gap;
        }

        public void setGap(Integer gap) {
            this.gap = gap;
        }

        public List<SegmentTimes> getSegmentTimesList() {
            return segmentTimesList;
        }
    }

    public class CourseArea implements Serializable {
        private String courseAreaId;
        private String courseAreaName;

        public String getCourseAreaId() {
            return courseAreaId;
        }

        public void setCourseAreaId(String courseAreaId) {
            this.courseAreaId = courseAreaId;
        }

        public String getCourseAreaName() {
            return courseAreaName;
        }

        public void setCourseAreaName(String courseAreaName) {
            this.courseAreaName = courseAreaName;
        }
    }

    public class SegmentTimes implements Serializable {

        private static final long serialVersionUID = -4537834985903003333L;

        private Integer segmentTypeId;
        private String times;
        private String courseAreaId;
        private String transferTimes;
        private String transferCourseAreaId;
        private boolean openFlg;

        public void setSegmentTypeId(Integer segmentTypeId) {
            this.segmentTypeId = segmentTypeId;
        }

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public String getCourseAreaId() {
            return courseAreaId;
        }

        public void setCourseAreaId(String courseAreaId) {
            this.courseAreaId = courseAreaId;
        }

        public void setTransferTimes(String transferTimes) {
            this.transferTimes = transferTimes;
        }

        public void setTransferCourseAreaId(String transferCourseAreaId) {
            this.transferCourseAreaId = transferCourseAreaId;
        }

        public void setOpenFlg(boolean openFlg) {
            this.openFlg = openFlg;
        }
    }

    public static class DetailSegmentCourse implements Serializable {

        public DetailSegmentCourse(String courseId, String courseName, String startTime, String endTime, int gap) {
            this.courseId = courseId;
            this.courseName = courseName;
            times = new ArrayList<>();

            ArrayList<String> timeArrayList = Utils.getTimes(startTime, endTime, gap);
            for (int i = 0; i < timeArrayList.size(); i++) {
                SegmentDetailTime sdt = new SegmentDetailTime();
                sdt.time = timeArrayList.get(i);

                times.add(sdt);
            }
        }

        private String courseId;
        private String courseName;
        private ArrayList<SegmentDetailTime> times;


        public String getCourseId() {
            return courseId;
        }

        public String getCourseName() {
            return courseName;
        }

        public ArrayList<SegmentDetailTime> getTimes() {
            return times;
        }

        public static class SegmentDetailTime implements Serializable {
            private String time;
            private boolean isMemberOnly;
            private boolean is9HoleOnly;
            private boolean isBlockTime;
            private boolean isPrimeTime;
            private String transferId;
            private String courseId;

            public String getTime() {
                return time;
            }

            public boolean isMemberOnly() {
                return isMemberOnly;
            }

            public boolean is9HoleOnly() {
                return is9HoleOnly;
            }

            public boolean isBlockTime() {
                return isBlockTime;
            }

            public boolean isPrimeTime() {
                return isPrimeTime;
            }

            public String getTransferId() {
                return transferId;
            }

            public String getCourseId() {
                return courseId;
            }
        }
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        try {

            detailsSegmentMap = new LinkedHashMap<>();

            JSONObject joData = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            Data dl = new Data();

            dl.setStartTime(Utils.getStringFromJsonObjectWithKey(joData, JsonKey.DETAIL_SEGMENT_TIME_START_TIME));
            dl.setEndTime(Utils.getStringFromJsonObjectWithKey(joData, JsonKey.DETAIL_SEGMENT_TIME_END_TIME));
            dl.setGap(Utils.getIntegerFromJsonObjectWithKey(joData, JsonKey.DETAIL_SEGMENT_TIME_GAP));

            ArrayList<CourseArea> myCourseAreaList = new ArrayList<>();
            JSONArray arrCourseArea = Utils.getArrayFromJsonObjectWithKey(joData, JsonKey.DETAIL_SEGMENT_TIME_COURSE_AREA);
            for (int i = 0; i < arrCourseArea.length(); i++) {
                JSONObject joCourseArea = arrCourseArea.getJSONObject(i);
                CourseArea myCourseArea = new CourseArea();
                myCourseArea.setCourseAreaId(Utils.getStringFromJsonObjectWithKey(joCourseArea, JsonKey.DETAIL_SEGMENT_TIME_COURSE_AREA_ID));
                myCourseArea.setCourseAreaName(Utils.getStringFromJsonObjectWithKey(joCourseArea, JsonKey.DETAIL_SEGMENT_TIME_COURSE_AREA_NAME));
                myCourseAreaList.add(myCourseArea);
                detailsSegmentMap.put(myCourseArea.getCourseAreaId(), new DetailSegmentCourse(myCourseArea.getCourseAreaId(), myCourseArea.getCourseAreaName(), dl.startTime, dl.endTime, dl.gap));
            }
            dl.courseAreaList = myCourseAreaList;

            ArrayList<SegmentTimes> mySegmentTimesList = new ArrayList<>();
            JSONArray arrSegmentTimes = Utils.getArrayFromJsonObjectWithKey(joData, JsonKey.DETAIL_SEGMENT_TIMES);
            for (int i = 0; i < arrSegmentTimes.length(); i++) {
                JSONObject joSegmentTimes = arrSegmentTimes.getJSONObject(i);
                SegmentTimes mySegmentTimes = new SegmentTimes();
                mySegmentTimes.setSegmentTypeId(Utils.getIntegerFromJsonObjectWithKey(joSegmentTimes, JsonKey.DETAIL_SEGMENT_TYPE_ID));
                mySegmentTimes.setTimes(Utils.getStringFromJsonObjectWithKey(joSegmentTimes, JsonKey.DETAIL_SEGMENT_TIME_TIMES));
                mySegmentTimes.setCourseAreaId(Utils.getStringFromJsonObjectWithKey(joSegmentTimes, JsonKey.DETAIL_SEGMENT_TIME_COURSE_AREA_ID));
                mySegmentTimes.setTransferTimes(Utils.getStringFromJsonObjectWithKey(joSegmentTimes, JsonKey.DETAIL_SEGMENT_TIME_TRANSFER_TIMES));
                mySegmentTimes.setTransferCourseAreaId(Utils.getStringFromJsonObjectWithKey(joSegmentTimes, JsonKey.DETAIL_SEGMENT_TIME_TRANSFER_COURSE_AREA_ID));
                if (joSegmentTimes.has(JsonKey.DETAIL_SEGMENT_TIME_OPEN_FLG)) {
                    mySegmentTimes.setOpenFlg(Utils.getIntegerFromJsonObjectWithKey(joSegmentTimes, JsonKey.DETAIL_SEGMENT_TIME_OPEN_FLG) == 1);
                }
                mySegmentTimesList.add(mySegmentTimes);

                if (Utils.isStringNotNullOrEmpty(mySegmentTimes.courseAreaId)) {
                    DetailSegmentCourse dsc = detailsSegmentMap.get(mySegmentTimes.courseAreaId);
                    if (dsc != null) {
                        String currentCourseTimes = mySegmentTimes.times;
                        String[] segmentTimes = currentCourseTimes.split(Constants.STR_SEPARATOR);
                        TimeArea timeArea = new TimeArea();
                        timeArea.setStartTime(segmentTimes[0]);
                        timeArea.setEndTime(segmentTimes[1]);
                        for (int j = 0; j < dsc.times.size(); j++) {
                            DetailSegmentCourse.SegmentDetailTime segmentDetailTime = dsc.times.get(j);
                            if (Utils.isTimeInArea(segmentDetailTime.time, timeArea)) {
                                if (mySegmentTimes.segmentTypeId == SEGMENT_TYPE_ID_9_HOLES_ONLY) {
                                    segmentDetailTime.is9HoleOnly = true;
                                } else if (mySegmentTimes.segmentTypeId == SEGMENT_TYPE_ID_BLOCK_TIME) {
                                    segmentDetailTime.isBlockTime = true;
                                } else if (mySegmentTimes.segmentTypeId == SEGMENT_TYPE_ID_MEMBER_ONLY) {
                                    segmentDetailTime.isMemberOnly = true;
                                } else if (mySegmentTimes.segmentTypeId == SEGMENT_TIME_ID_PRIME_DISCOUNT) {
                                    segmentDetailTime.isPrimeTime = true;
                                } else if (mySegmentTimes.segmentTypeId == SEGMENT_TYPE_ID_TWO_TEE_START || mySegmentTimes.segmentTypeId == SEGMENT_TYPE_ID_THREE_TEE_START) {
                                    segmentDetailTime.courseId = mySegmentTimes.courseAreaId;
                                }
                            }
                        }
                    }
                }

                if (Utils.isStringNotNullOrEmpty(mySegmentTimes.transferCourseAreaId)) {
                    DetailSegmentCourse transferDsc = detailsSegmentMap.get(mySegmentTimes.transferCourseAreaId);
                    if (transferDsc != null) {
                        String currentTransferTimes = mySegmentTimes.transferTimes;
                        String[] transferTimes = currentTransferTimes.split(Constants.STR_SEPARATOR);
                        TimeArea timeArea = new TimeArea();
                        timeArea.setStartTime(transferTimes[0]);
                        timeArea.setEndTime(transferTimes[1]);
                        for (int j = 0; j < transferDsc.times.size(); j++) {
                            DetailSegmentCourse.SegmentDetailTime segmentDetailTime = transferDsc.times.get(j);
                            if (Utils.isTimeInArea(segmentDetailTime.time, timeArea)) {
                                if (mySegmentTimes.segmentTypeId == SEGMENT_TYPE_ID_9_HOLES_ONLY) {
                                    segmentDetailTime.is9HoleOnly = true;
                                } else if (mySegmentTimes.segmentTypeId == SEGMENT_TYPE_ID_BLOCK_TIME) {
                                    segmentDetailTime.isBlockTime = true;
                                } else if (mySegmentTimes.segmentTypeId == SEGMENT_TYPE_ID_MEMBER_ONLY) {
                                    segmentDetailTime.isMemberOnly = true;
                                } else if (mySegmentTimes.segmentTypeId == SEGMENT_TIME_ID_PRIME_DISCOUNT) {
                                    segmentDetailTime.isPrimeTime = true;
                                }
                            }
                            if (mySegmentTimes.openFlg) {
                                if (Utils.isTransferBlockTimeInArea(segmentDetailTime.time, timeArea)) {
                                    if (mySegmentTimes.segmentTypeId == SEGMENT_TYPE_ID_TWO_TEE_START || mySegmentTimes.segmentTypeId == SEGMENT_TYPE_ID_THREE_TEE_START) {
                                        segmentDetailTime.transferId = mySegmentTimes.courseAreaId;
                                    }
                                }
                            } else {
                                if (Utils.isTimeInArea(segmentDetailTime.time, timeArea)) {
                                    if (mySegmentTimes.segmentTypeId == SEGMENT_TYPE_ID_TWO_TEE_START || mySegmentTimes.segmentTypeId == SEGMENT_TYPE_ID_THREE_TEE_START) {
                                        segmentDetailTime.transferId = mySegmentTimes.courseAreaId;
                                    }
                                }
                            }
                        }
                    }
                }

            }
            dl.segmentTimesList = mySegmentTimesList;
            setDataList(dl);

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }


    }
}
