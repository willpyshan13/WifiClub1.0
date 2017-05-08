/**
 * Project Name: itee
 * File Name:	 JsonUserDetailGet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-03-21
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonUserDetailGet <br/>
 * Function: FIXME. <br/>
 * Date: 2015-03-21 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonUserDetailGet extends BaseJsonObject implements Serializable {

    public JsonUserDetailGet(JSONObject jsonObject) {
        super(jsonObject);
    }

    private String userPhoto;
    private String userName;
    private String departmentName;
    private String levelName;
    private String staffNo;
    private String manager;
    private String account;
    private String password;

    private Integer userId;
    private Integer departmentId;
    private Integer levelId;

    private String attrId;

    private boolean canEdit;

    private ArrayList<Phone> phoneList;
    private ArrayList<Email> emailList;

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public static class Phone implements Serializable {
        private Integer phoneStatus;
        private String phoneTag;
        private String phone;

        public Integer getPhoneStatus() {
            return phoneStatus;
        }

        public void setPhoneStatus(Integer phoneStatus) {
            this.phoneStatus = phoneStatus;
        }

        public String getPhoneTag() {
            return phoneTag;
        }

        public void setPhoneTag(String phoneTag) {
            this.phoneTag = phoneTag;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    public static class Email implements Serializable {
        private Integer emailStatus;
        private String emailTag;
        private String email;

        public Integer getEmailStatus() {
            return emailStatus;
        }

        public void setEmailStatus(Integer emailStatus) {
            this.emailStatus = emailStatus;
        }

        public String getEmailTag() {
            return emailTag;
        }

        public void setEmailTag(String emailTag) {
            this.emailTag = emailTag;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(String staffNo) {
        this.staffNo = staffNo;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public ArrayList<Phone> getPhoneList() {
        return phoneList;
    }

    public ArrayList<Email> getEmailList() {
        return emailList;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {
            try {

                JSONArray dataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);

                if (dataList.length() > 0) {

                    JSONObject data = dataList.getJSONObject(0);

                    setUserPhoto(Utils.getStringFromJsonObjectWithKey(data, JsonKey.STAFF_DEPARTMENT_USER_PHOTO));
                    setUserId(Utils.getIntegerFromJsonObjectWithKey(data, JsonKey.STAFF_DEPARTMENT_USER_ID));
                    setUserName(Utils.getStringFromJsonObjectWithKey(data, JsonKey.STAFF_DEPARTMENT_USER_NAME));
                    setDepartmentId(Utils.getIntegerFromJsonObjectWithKey(data, JsonKey.STAFF_DEPARTMENT_DEPARTMENT_ID));
                    setDepartmentName(Utils.getStringFromJsonObjectWithKey(data, JsonKey.STAFF_DEPARTMENT_DEPARTMENT_NAME));
                    setLevelId(Utils.getIntegerFromJsonObjectWithKey(data, JsonKey.STAFF_DEPARTMENT_LEVEL_ID));
                    setLevelName(Utils.getStringFromJsonObjectWithKey(data, JsonKey.STAFF_DEPARTMENT_LEVEL_NAME));
                    setStaffNo(Utils.getStringFromJsonObjectWithKey(data, JsonKey.STAFF_DEPARTMENT_STAFF_NO));
                    setManager(Utils.getStringFromJsonObjectWithKey(data, JsonKey.STAFF_DEPARTMENT_MANAGER));
                    setAccount(Utils.getStringFromJsonObjectWithKey(data, JsonKey.STAFF_DEPARTMENT_ACCOUNT));
                    setPassword(Utils.getStringFromJsonObjectWithKey(data, JsonKey.STAFF_DEPARTMENT_PASSWORD));

                    setAttrId(Utils.getStringFromJsonObjectWithKey(data, JsonKey.STAFF_DEPARTMENT_ATTRI_ID));

                    String editFlag = Utils.getStringFromJsonObjectWithKey(data, JsonKey.STAFF_DEPARTMENT_EDIT_FLAG);
                    canEdit = Constants.STR_1.equals(editFlag);

                    emailList = new ArrayList<>();
                    try {
                        JSONArray arrEmail = Utils.getArrayFromJsonObjectWithKey(data, JsonKey.STAFF_DEPARTMENT_EMAIL_LIST);
                        for (int i = 0; i < arrEmail.length(); i++) {
                            JSONObject joEmail = arrEmail.getJSONObject(i);
                            Email e = new Email();
                            e.setEmail(Utils.getStringFromJsonObjectWithKey(joEmail, JsonKey.STAFF_DEPARTMENT_EMAIL_LIST_EMAIL));
                            e.setEmailTag(Utils.getStringFromJsonObjectWithKey(joEmail, JsonKey.STAFF_DEPARTMENT_EMAIL_LIST_EMAIL_TAG));
                            e.setEmailStatus(Utils.getIntegerFromJsonObjectWithKey(joEmail, JsonKey.STAFF_DEPARTMENT_EMAIL_LIST_EMAIL_STATUS));
                            emailList.add(e);
                        }
                    } catch (JSONException e) {
                        Utils.log(e.getMessage());
                    }

                    phoneList = new ArrayList<>();
                    try {
                        JSONArray arrPhone = Utils.getArrayFromJsonObjectWithKey(data, JsonKey.STAFF_DEPARTMENT_PHONE_LIST);
                        for (int i = 0; i < arrPhone.length(); i++) {
                            JSONObject joPhone = arrPhone.getJSONObject(i);
                            Phone p = new Phone();
                            p.setPhone(Utils.getStringFromJsonObjectWithKey(joPhone, JsonKey.STAFF_DEPARTMENT_PHONE_LIST_PHONE));
                            p.setPhoneTag(Utils.getStringFromJsonObjectWithKey(joPhone, JsonKey.STAFF_DEPARTMENT_PHONE_LIST_PHONE_TAG));
                            p.setPhoneStatus(Utils.getIntegerFromJsonObjectWithKey(joPhone, JsonKey.STAFF_DEPARTMENT_PHONE_LIST_PHONE_STATUS));
                            phoneList.add(p);
                        }
                    } catch (JSONException e) {
                        Utils.log(e.getMessage());
                    }
                }
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }
}