/**
 * Project Name: itee
 * File Name:	 JsonUserListGet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-03-19
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
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
 * ClassName:JsonUserListGet <br/>
 * Function: To get user list. <br/>
 * Date: 2015-03-19 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonUserListGet extends BaseJsonObject implements Serializable {

    private ArrayList<DepartmentUserInfo> memberList;
    private String departmentId;
    private int type;
    private String departmentName;
    private int courseId;
    private String editFlag;

    public JsonUserListGet(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {
            try {
                memberList = new ArrayList<>();

                JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
                departmentId = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.STAFF_DEPARTMENT_DEPARTMENT_ID);
                courseId = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.STAFF_DEPARTMENTLIST_COURES_ID);
                departmentName = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.STAFF_DEPARTMENTLIST_DEPARTMENT_NAME);
                type = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.STAFF_DEPARTMENTLIST_TYPE);
                editFlag = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.STAFF_DEPARTMENT_EDIT_FLAG);

                JSONArray jsonArray = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.STAFF_DEPARTMENT_MEMBER_LIST);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    DepartmentUserInfo dui = new DepartmentUserInfo();
                    dui.setUserId(Utils.getIntegerFromJsonObjectWithKey(jo, JsonKey.STAFF_DEPARTMENT_USER_ID));
                    dui.setUserPhoto(Utils.getStringFromJsonObjectWithKey(jo, JsonKey.STAFF_DEPARTMENT_USER_PHOTO));
                    dui.setUserName(Utils.getStringFromJsonObjectWithKey(jo, JsonKey.STAFF_DEPARTMENT_USER_NAME));
                    dui.setUserEmail(Utils.getStringFromJsonObjectWithKey(jo, JsonKey.STAFF_DEPARTMENT_USER_EMAIL));
                    dui.setUserMobile(Utils.getStringFromJsonObjectWithKey(jo, JsonKey.STAFF_DEPARTMENT_USER_MOBILE));
                    dui.setUserLevel(Utils.getStringFromJsonObjectWithKey(jo, JsonKey.STAFF_DEPARTMENT_USER_LEVEL));
                    dui.setUserType(Utils.getStringFromJsonObjectWithKey(jo, JsonKey.STAFF_DEPARTMENT_USER_TYPE));
                    memberList.add(dui);
                }
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }

    public ArrayList<DepartmentUserInfo> getMemberList() {
        return memberList;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public int getType() {
        return type;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getEditFlag() {
        return editFlag;
    }

    public static class DepartmentUserInfo implements Serializable {
        private String userPhoto;
        private Integer userId;
        private String userName;
        private String userMobile;
        private String userEmail;
        private String userLevel;
        private String userType;

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getUserPhoto() {
            return userPhoto;
        }

        public void setUserPhoto(String userPhoto) {
            this.userPhoto = userPhoto;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserMobile() {
            return userMobile;
        }

        public void setUserMobile(String userMobile) {
            this.userMobile = userMobile;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }

        public String getUserLevel() {
            return userLevel;
        }

        public void setUserLevel(String userLevel) {
            this.userLevel = userLevel;
        }
    }
}