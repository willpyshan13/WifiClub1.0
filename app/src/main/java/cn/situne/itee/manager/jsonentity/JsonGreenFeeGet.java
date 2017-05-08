/**
 * Project Name: itee
 * File Name:  JsonGreenFeeGet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-30
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
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
 * ClassName:JsonAgentsPricingListGet <br/>
 * Function: List. <br/>
 * Date: 2015-03-30 <br/>
 *
 * @author Luochao
 * @version 1.0
 * @see
 */
public class JsonGreenFeeGet extends BaseJsonObject implements Serializable {
    private ArrayList<GreenData> courseList;
    private ArrayList<GreenData> holesList;

    public JsonGreenFeeGet(JSONObject jsonObject) {
        super(jsonObject);
    }


    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {
            courseList = new ArrayList<>();
            holesList = new ArrayList<>();
            try {
                JSONArray arrDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
                JSONObject joCourseObject = arrDataList.getJSONObject(0);
                JSONObject joHolesObject = arrDataList.getJSONObject(1);
                JSONArray arrCourseList = Utils.getArrayFromJsonObjectWithKey(joCourseObject, JsonKey.SHOPS_GREEN_FEE_COURSE);
                JSONArray arrHolesList = Utils.getArrayFromJsonObjectWithKey(joHolesObject, JsonKey.SHOPS_GREEN_FEE_HOLES);
                for (int i = 0; i < arrCourseList.length(); i++) {
                    JSONObject joCourse = arrCourseList.getJSONObject(i);
                    GreenData courseData = new GreenData();
                    courseData.setId(Utils.getIntegerFromJsonObjectWithKey(joCourse, JsonKey.SHOPS_GREEN_FEE_COURSE_ID));
                    courseData.setPrice(Utils.getStringFromJsonObjectWithKey(joCourse, JsonKey
                            .SHOPS_GREEN_FEE_COURSE_PRICE));
                    courseData.setName(Utils.getStringFromJsonObjectWithKey(joCourse, JsonKey.SHOPS_GREEN_FEE_COURSE_NAME));
                    courseData.setEnableStatus(Utils.getIntegerFromJsonObjectWithKey(joCourse, JsonKey.SHOPS_GREEN_FEE_COURSE_ENABLE_STATUS));
                    courseData.setAreaId(Utils.getStringFromJsonObjectWithKey(joCourse, JsonKey.SHOPS_GREEN_FEE_AREA_ID));
                    courseData.setAreaType(Utils.getStringFromJsonObjectWithKey(joCourse, JsonKey.SHOPS_GREEN_FEE_AREA_TYPE));
                    courseList.add(courseData);
                }

                for (int i = 0; i < arrHolesList.length(); i++) {
                    JSONObject joHoles = arrHolesList.getJSONObject(i);
                    GreenData holesData = new GreenData();
                    holesData.setId(Utils.getIntegerFromJsonObjectWithKey(joHoles, JsonKey.SHOPS_GREEN_FEE_HOLES_ID));
                    holesData.setPrice(Utils.getStringFromJsonObjectWithKey(joHoles, JsonKey.SHOPS_GREEN_FEE_HOLES_PRICE));
                    holesData.setName(Utils.getStringFromJsonObjectWithKey(joHoles, JsonKey.SHOPS_GREEN_FEE_HOLES_NAME));
                    holesData.setEnableStatus(Utils.getIntegerFromJsonObjectWithKey(joHoles, JsonKey.SHOPS_GREEN_FEE_HOLES_ENABLE_STATUS));

                    holesData.setAreaId(Utils.getStringFromJsonObjectWithKey(joHoles, JsonKey.SHOPS_GREEN_FEE_AREA_ID));
                    holesData.setAreaType(Utils.getStringFromJsonObjectWithKey(joHoles, JsonKey.SHOPS_GREEN_FEE_AREA_TYPE));
                    holesList.add(holesData);
                }

            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }

    public ArrayList<GreenData> getCourseList() {
        return courseList;
    }

    public ArrayList<GreenData> getHolesList() {
        return holesList;
    }

    public static class GreenData implements Serializable {
        private int id;
        private String name;
        private String price;

        private String areaType;

        private String areaId;

        public String getAreaType() {
            return areaType;
        }

        public void setAreaType(String areaType) {
            this.areaType = areaType;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getEnableStatus() {
            return enableStatus;
        }

        public void setEnableStatus(int enableStatus) {
            this.enableStatus = enableStatus;
        }

        private int enableStatus;

    }


}
