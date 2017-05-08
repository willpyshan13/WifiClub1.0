/**
 * Project Name: itee
 * File Name:  JsonCourseList.java
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
import java.util.List;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonCourseList <br/>
 * Function: To set Course List. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author qiushuai
 * @version 1.0
 * @see
 */
public class JsonCourseList extends BaseJsonObject implements Serializable {


    public JsonCourseList(JSONObject jsonObject) {
        super(jsonObject);
    }

    private ArrayList<DataList> dataList;

    public ArrayList<DataList> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<DataList> dataList) {
        this.dataList = dataList;
    }

    public static class DataList implements Serializable {
        private Integer showCourseAreaId;
        private String showCourseAreaType;
        private String showCourseUnit;
        private Integer showCourseId;
        private List<HoleDataItem> holeDataItemList;

        public static class HoleDataItem implements Serializable {
            public Integer holeNO;
            public Integer par;
            public Integer blackTee;
            public Integer goldTee;
            public Integer blueTee;
            public Integer whiteTee;
            public Integer redTee;
            public Integer hdcp;

            public Integer getHdcp() {
                return hdcp;
            }

            public void setHdcp(Integer hdcp) {
                this.hdcp = hdcp;
            }

            public Integer getHoleNO() {
                return holeNO;
            }

            public void setHoleNO(Integer holeNO) {
                this.holeNO = holeNO;
            }

            public Integer getPar() {
                return par;
            }

            public void setPar(Integer par) {
                this.par = par;
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


        public Integer getShowCourseAreaId() {
            return showCourseAreaId;
        }

        public void setShowCourseAreaId(Integer showCourseAreaId) {
            this.showCourseAreaId = showCourseAreaId;
        }

        public String getShowCourseAreaType() {
            return showCourseAreaType;
        }

        public void setShowCourseAreaType(String showCourseAreaType) {
            this.showCourseAreaType = showCourseAreaType;
        }

        public String getShowCourseUnit() {
            return showCourseUnit;
        }

        public void setShowCourseUnit(String showCourseUnit) {
            this.showCourseUnit = showCourseUnit;
        }

        public Integer getShowCourseId() {
            return showCourseId;
        }

        public void setShowCourseId(Integer showCourseId) {
            this.showCourseId = showCourseId;
        }

        public List<HoleDataItem> getHoleDataItemList() {
            return holeDataItemList;
        }

        public void setHoleDataItemList(List<HoleDataItem> holeDataItemList) {
            this.holeDataItemList = holeDataItemList;
        }
    }


    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new ArrayList<>();

        try {
            JSONArray joDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.SHOW_COURSE_DATA_LIST);

            for (int i = 0; i < joDataList.length(); i++) {
                JSONObject jsonObject = joDataList.getJSONObject(i);

                DataList dl = new DataList();
                dl.setShowCourseAreaId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOW_COURSE_AREA_ID));
                dl.setShowCourseAreaType(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOW_COURSE_AREA_TYPE));
                dl.setShowCourseUnit(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOW_COURSE_UNIT));
                dl.setShowCourseId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOW_COURSE_ID));

                List<DataList.HoleDataItem> holeDataItemList = new ArrayList<>();
                JSONArray arrHoleDataList = Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.SHOW_COURSE_HOLE_DATA);

                for (int k = 0; k < arrHoleDataList.length(); k++) {
                    JSONObject joHoleListItem = arrHoleDataList.getJSONObject(k);
                    DataList.HoleDataItem item = new DataList.HoleDataItem();
                    item.setHoleNO(Utils.getIntegerFromJsonObjectWithKey(joHoleListItem, JsonKey.SHOW_COURSE_HOLE_NO));
                    item.setPar(Utils.getIntegerFromJsonObjectWithKey(joHoleListItem, JsonKey.SHOW_COURSE_PAR));
                    item.setHdcp(Utils.getIntegerFromJsonObjectWithKey(joHoleListItem, JsonKey.SHOW_COURSE_HDCP));
                    item.setBlackTee(Utils.getIntegerFromJsonObjectWithKey(joHoleListItem, JsonKey.SHOW_COURSE_BLACK_TEE));
                    item.setGoldTee(Utils.getIntegerFromJsonObjectWithKey(joHoleListItem, JsonKey.SHOW_COURSE_GOLD_TEE));
                    item.setBlueTee(Utils.getIntegerFromJsonObjectWithKey(joHoleListItem, JsonKey.SHOW_COURSE_BLUE_TEE));
                    item.setWhiteTee(Utils.getIntegerFromJsonObjectWithKey(joHoleListItem, JsonKey.SHOW_COURSE_WHITE_TEE));
                    item.setRedTee(Utils.getIntegerFromJsonObjectWithKey(joHoleListItem, JsonKey.SHOW_COURSE_RED_TEE));
                    holeDataItemList.add(item);
                }
                dl.setHoleDataItemList(holeDataItemList);

                dataList.add(dl);
            }


        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }

}
