/**
 * Project Name: itee
 * File Name:  JsonCourseListInfo.java
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
 * ClassName:JsonCourseListInfo <br/>
 * Function: To set Course list Info. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author qiushuai
 * @version 1.0
 * @see
 */
public class JsonCourseListInfo extends BaseJsonObject implements Serializable {


    public JsonCourseListInfo(JSONObject jsonObject) {
        super(jsonObject);
    }

    private List<DataList> dataList;

    public List<DataList> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataList> dataList) {
        this.dataList = dataList;
    }

    public static class DataList implements Serializable {
        private Integer showCourseAreaId;
        private String showCourseAreaType;
        private String showCourseUnit;
        private Integer showCourseId;
        private ArrayList<HoleDataItem> holeDataItemList;

        public static class HoleDataItem implements Serializable {
            public Integer holeNO;
            public Integer par;
            public Integer hdcp;
            public Integer pace;
            //public List<HoleDataItem.TeeDataList>  teeDataList;
            public List<TeeDataList> teeDataList;


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

            public Integer getPace() {
                return pace;
            }

            public void setPace(Integer pace) {
                this.pace = pace;
            }

            public List<TeeDataList> getTeeDataList() {
                return teeDataList;
            }

            public void setTeeDataList(List<TeeDataList> teeDataList) {
                this.teeDataList = teeDataList;
            }

            public static class TeeDataList implements Serializable {

                public String teeName;
                public Integer teeYard;

                public String getTeeName() {
                    return teeName;
                }

                public void setTeeName(String teeName) {
                    this.teeName = teeName;
                }

                public Integer getTeeYard() {
                    return teeYard;
                }

                public void setTeeYard(Integer teeYard) {
                    this.teeYard = teeYard;
                }
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

        public ArrayList<HoleDataItem> getHoleDataItemList() {
            return holeDataItemList;
        }

        public void setHoleDataItemList(ArrayList<HoleDataItem> holeDataItemList) {
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

                ArrayList<DataList.HoleDataItem> holeDataItemList = new ArrayList<>();
                JSONArray arrHoleDataList = Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.SHOW_COURSE_HOLE_DATA);

                for (int k = 0; k < arrHoleDataList.length(); k++) {
                    JSONObject joHoleListItem = arrHoleDataList.getJSONObject(k);

                    DataList.HoleDataItem holeItem = new DataList.HoleDataItem();
                    holeItem.setHoleNO(Utils.getIntegerFromJsonObjectWithKey(joHoleListItem, JsonKey.SHOW_COURSE_HOLE_NO));
                    holeItem.setPar(Utils.getIntegerFromJsonObjectWithKey(joHoleListItem, JsonKey.SHOW_COURSE_PAR));
                    holeItem.setHdcp(Utils.getIntegerFromJsonObjectWithKey(joHoleListItem, JsonKey.SHOW_COURSE_HDCP));
                    holeItem.setPace(Utils.getIntegerFromJsonObjectWithKey(joHoleListItem, JsonKey.EDIT_HOLE_PACE));
                    holeDataItemList.add(holeItem);

                    List<DataList.HoleDataItem.TeeDataList> teeDataList = new ArrayList<>();
                    if (joHoleListItem.has(JsonKey.EDIT_HOLE_TEE_DATA)) {
                        JSONArray arrTeeDataList = Utils.getArrayFromJsonObjectWithKey(joHoleListItem, JsonKey.EDIT_HOLE_TEE_DATA);

                        for (int j = 0; j < arrTeeDataList.length(); j++) {
                            try {

                                JSONObject joTeeDataListItem = arrTeeDataList.getJSONObject(j);
                                DataList.HoleDataItem.TeeDataList teeItem = new DataList.HoleDataItem.TeeDataList();
                                teeItem.setTeeYard(Utils.getIntegerFromJsonObjectWithKey(joTeeDataListItem, JsonKey.EDIT_HOLE_TEE_YARD));
                                teeItem.setTeeName(Utils.getStringFromJsonObjectWithKey(joTeeDataListItem, JsonKey.EDIT_HOLE_TEE_NAME));

                                teeDataList.add(teeItem);
                            } catch (JSONException e) {
                                Utils.log(e.getMessage());
                            }

                        }
                    }

                    holeItem.setTeeDataList(teeDataList);

                }
                dl.setHoleDataItemList(holeDataItemList);

                dataList.add(dl);
            }


        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }

}
