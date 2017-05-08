/**
 * Project Name: itee
 * File Name:  JsonStaffDepartmentListGet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-19
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
 * ClassName:JsonStaffDepartmentListGet <br/>
 * Function: To get staff department list. <br/>
 * Date: 2015-03-19 <br/>
 *
 * @author Luochao
 * @version 1.0
 * @see
 */
public class JsonStaffDepartmentListGet extends BaseJsonObject implements Serializable {
    public JsonStaffDepartmentListGet(JSONObject jsonObject) {
        super(jsonObject);

    }

    private ArrayList<ItemData> dataList;

    public ArrayList<ItemData> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<ItemData> dataList) {
        this.dataList = dataList;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new ArrayList<>();

        try {
            JSONArray joDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
            for (int i = 0; i < joDataList.length(); i++) {

                JSONObject jsonObject = joDataList.getJSONObject(i);
                ItemData data = new ItemData();
                data.setCourseId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_DEPARTMENTLIST_COURES_ID));
                data.setDepartmentId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_DEPARTMENTLIST_DEPARTMENT_ID));
                data.setDepartmentName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_DEPARTMENTLIST_DEPARTMENT_NAME));
                data.setEditStatus(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_DEPARTMENTLIST_EDIT_STATUS));
                data.setNumber(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_DEPARTMENTLIST_NUMBER));
                data.setType(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_DEPARTMENTLIST_TYPE));

                dataList.add(data);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    public class ItemData implements Serializable {
        private String departmentName;
        private int departmentId;
        private int number;

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public int getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(int departmentId) {
            this.departmentId = departmentId;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getEditStatus() {
            return editStatus;
        }

        public void setEditStatus(int editStatus) {
            this.editStatus = editStatus;
        }

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        private int type;
        private int editStatus;
        private int courseId;

    }
}


