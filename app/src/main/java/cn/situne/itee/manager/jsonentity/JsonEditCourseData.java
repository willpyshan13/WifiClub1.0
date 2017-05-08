/**
 * Project Name: itee
 * File Name:  JsonEditCourseData.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonEditCourseData <br/>
 * Function: To edit course data. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author qiushuai
 * @version 1.0
 * @see
 */
public class JsonEditCourseData extends BaseJsonObject implements Serializable {

    private DataList dataList;

    public DataList getDataList() {
        return dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }

    public static class DataList implements Serializable {
        public Integer holes;
        public Integer transferCourseId;
        public String transferTime;
        public String transferCourseType;
        public Integer transferCalculate;

        public Integer getHoles() {
            return holes;
        }

        public void setHoles(Integer holes) {
            this.holes = holes;
        }

        public Integer getTransferCourseId() {
            return transferCourseId;
        }

        public void setTransferCourseId(Integer transferCourseId) {
            this.transferCourseId = transferCourseId;
        }

        public String getTransferTime() {
            return transferTime;
        }

        public void setTransferTime(String transferTime) {
            this.transferTime = transferTime;
        }

        public String getTransferCourseType() {
            return transferCourseType;
        }

        public void setTransferCourseType(String transferCourseType) {
            this.transferCourseType = transferCourseType;
        }

        public Integer getTransferCalculate() {
            return transferCalculate;
        }

        public void setTransferCalculate(Integer transferCalculate) {
            this.transferCalculate = transferCalculate;
        }
    }

    public JsonEditCourseData(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        dataList = new DataList();
        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.EDIT_COURSE_DATA_LIST);
            dataList.holes = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.EDIT_COURSE_HOLES);
            dataList.transferCourseId = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.EDIT_COURSE_TRANSFER_COURSE_ID);
            dataList.transferTime = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.EDIT_COURSE_TRANSFER_TIME);
            dataList.transferCourseType = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.EDIT_COURSE_TRANSFER_COURSE_TYPE);
            dataList.transferCalculate = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.EDIT_COURSE_CALCULATE);
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }

}
