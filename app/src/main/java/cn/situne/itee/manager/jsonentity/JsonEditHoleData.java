/**
 * Project Name: itee
 * File Name:  JsonEditHoleData.java
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
 * ClassName:JsonEditHoleData <br/>
 * Function: To edit HoleData. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author qiushuai
 * @version 1.0
 * @see
 */

public class JsonEditHoleData extends BaseJsonObject implements Serializable {

    public JsonEditHoleData(JSONObject jsonObject) {
        super(jsonObject);
    }


    private DataList dataList;

    public DataList getDataList() {
        return dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }

    public static class DataList implements Serializable {
        public Integer courseAreaId;
        public Integer holeNo;
        public Integer par;
        public Integer index;
        public Integer pace;
        public Integer blackTee;
        public Integer goldTee;
        public Integer blueTee;
        public Integer whiteTee;
        public Integer redTee;

        public Integer getCourseAreaId() {
            return courseAreaId;
        }

        public void setCourseAreaId(Integer courseAreaId) {
            this.courseAreaId = courseAreaId;
        }

        public Integer getHoleNo() {
            return holeNo;
        }

        public void setHoleNo(Integer holeNo) {
            this.holeNo = holeNo;
        }

        public Integer getPar() {
            return par;
        }

        public void setPar(Integer par) {
            this.par = par;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public Integer getPace() {
            return pace;
        }

        public void setPace(Integer pace) {
            this.pace = pace;
        }

        public Integer getBlackTee() {
            return blackTee;
        }

        public void setBlackTee(Integer blackTee) {
            this.blackTee = blackTee;
        }

        public Integer getGoldTee() {
            return goldTee;
        }

        public void setGoldTee(Integer goldTee) {
            this.goldTee = goldTee;
        }

        public Integer getBlueTee() {
            return blueTee;
        }

        public void setBlueTee(Integer blueTee) {
            this.blueTee = blueTee;
        }

        public Integer getWhiteTee() {
            return whiteTee;
        }

        public void setWhiteTee(Integer whiteTee) {
            this.whiteTee = whiteTee;
        }

        public Integer getRedTee() {
            return redTee;
        }

        public void setRedTee(Integer redTee) {
            this.redTee = redTee;
        }
    }


    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        dataList = new DataList();
        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.EDIT_HOLE_DATA_LIST);
            dataList.courseAreaId = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.EDIT_HOLE_COURSE_AREA_ID);
            dataList.holeNo = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.EDIT_HOLE_NO);
            dataList.par = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.EDIT_HOLE_PAR);
            dataList.index = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.EDIT_HOLE_INDEX);
            dataList.pace = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.EDIT_HOLE_PACE);
            dataList.blackTee = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.EDIT_HOLE_BLACK_TEE);
            dataList.goldTee = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.EDIT_HOLE_GOLD_TEE);
            dataList.blueTee = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.EDIT_HOLE_BLUE_TEE);
            dataList.whiteTee = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.EDIT_HOLE_WHITE_TEE);
            dataList.redTee = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.EDIT_HOLE_RED_TEE);
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }


}

