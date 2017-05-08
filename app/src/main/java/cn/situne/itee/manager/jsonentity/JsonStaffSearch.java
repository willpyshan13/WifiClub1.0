/**
 * Project Name: itee
 * File Name:	 JsonStaffSearch.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-07-27
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonStaffSearch <br/>
 * Function: entity of staff search. <br/>
 * Date: 2015-07-27 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonStaffSearch extends BaseJsonObject {

    private ArrayList<Staff> staffList;

    public JsonStaffSearch(JSONObject jsonObject) {
        super(jsonObject);
    }

    public ArrayList<Staff> getStaffList() {
        return staffList;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        staffList = new ArrayList<>();

        if (jsonObj != null) {
            try {
                JSONArray jaDataList = jsonObj.getJSONArray(JsonKey.COMMON_DATA_LIST);
                for (int i = 0; i < jaDataList.length(); i++) {
                    JSONObject joAgent = jaDataList.getJSONObject(i);
                    Staff staff = new Staff();
                    staff.setStaffId(Utils.getStringFromJsonObjectWithKey(joAgent, JsonKey.STAFF_SEARCH_ID));
                    staff.setStaffName(Utils.getStringFromJsonObjectWithKey(joAgent, JsonKey.STAFF_SEARCH_ACCOUNT));
                    staff.setStaffAciValue(Utils.getStringFromJsonObjectWithKey(joAgent, JsonKey.STAFF_SEARCH_ACI_VALUE));
                    staff.setStaffPhoto(Utils.getStringFromJsonObjectWithKey(joAgent, JsonKey.STAFF_SEARCH_PHOTO));
                    staff.setDepartmentId(Utils.getStringFromJsonObjectWithKey(joAgent, JsonKey.STAFF_SEARCH_DEPARTMENT_ID));
                    staff.setDepartmentName(Utils.getStringFromJsonObjectWithKey(joAgent, JsonKey.STAFF_SEARCH_DEPARTMENT_NAME));
                    staff.setDepartmentType(Utils.getStringFromJsonObjectWithKey(joAgent, JsonKey.STAFF_SEARCH_TYPE));
                    staff.setCourseId(Utils.getStringFromJsonObjectWithKey(joAgent, JsonKey.STAFF_SEARCH_COURSE_ID));
                    staffList.add(staff);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Staff {
        private String staffId;
        private String staffName;
        private String staffAciValue;
        private String staffPhoto;
        private String departmentType;
        private String departmentId;
        private String departmentName;
        private String courseId;

        public String getStaffId() {
            return staffId;
        }

        public void setStaffId(String staffId) {
            this.staffId = staffId;
        }

        public String getStaffName() {
            return staffName;
        }

        public void setStaffName(String staffName) {
            this.staffName = staffName;
        }

        public String getStaffAciValue() {
            return staffAciValue;
        }

        public void setStaffAciValue(String staffAciValue) {
            this.staffAciValue = staffAciValue;
        }

        public String getStaffPhoto() {
            return staffPhoto;
        }

        public void setStaffPhoto(String staffPhoto) {
            this.staffPhoto = staffPhoto;
        }

        public String getDepartmentType() {
            return departmentType;
        }

        public void setDepartmentType(String departmentType) {
            this.departmentType = departmentType;
        }

        public String getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(String departmentId) {
            this.departmentId = departmentId;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public String getCourseId() {
            return courseId;
        }

        public void setCourseId(String courseId) {
            this.courseId = courseId;
        }
    }

}  