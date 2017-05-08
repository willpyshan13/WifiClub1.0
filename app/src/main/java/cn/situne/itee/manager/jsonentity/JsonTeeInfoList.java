/**
 * Project Name: itee
 * File Name:  JsonTeeInfoList.java
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
 * ClassName:JsonTeeInfoList <br/>
 * Function: To set tee information list. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonTeeInfoList extends BaseJsonObject implements Serializable {

    public JsonTeeInfoList(JSONObject jsonObject) {
        super(jsonObject);
    }

    private DataList dataList;

    public static class DataList implements Serializable {

        public String index;
        public Integer pace;
        public String teeUnit;
        public List<DataInfoList> dataInfoList;

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public Integer getPace() {
            return pace;
        }

        public void setPace(Integer pace) {
            this.pace = pace;
        }

        public String getTeeUnit() {
            return teeUnit;
        }

        public void setTeeUnit(String teeUnit) {
            this.teeUnit = teeUnit;
        }

        public List<DataInfoList> getDataInfoList() {
            return dataInfoList;
        }

        public void setDataInfoList(List<DataInfoList> dataInfoList) {
            this.dataInfoList = dataInfoList;
        }

        public static class DataInfoList implements Serializable {

            public Integer teeId;
            public String teeName;
            public Integer teeYard;

            public Integer getTeeId() {
                return teeId;
            }

            public void setTeeId(Integer teeId) {
                this.teeId = teeId;
            }

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

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new DataList();

        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            dataList.index = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.EDIT_HOLE_INDEX);
            dataList.pace = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.EDIT_HOLE_PACE);
            dataList.teeUnit = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.EDIT_HOLE_TEE_UNIT);

            dataList.dataInfoList = new ArrayList<>();
            JSONArray arrDataInfoList = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.EDIT_HOLE_TEE_LIST);
            for (int i = 0; i < arrDataInfoList.length(); i++) {
                JSONObject joDataInfoListItem = arrDataInfoList.getJSONObject(i);
                DataList.DataInfoList dl = new DataList.DataInfoList();
                dl.teeId = Utils.getIntegerFromJsonObjectWithKey(joDataInfoListItem, JsonKey.EDIT_HOLE_TEE_ID);
                dl.teeName = Utils.getStringFromJsonObjectWithKey(joDataInfoListItem, JsonKey.EDIT_HOLE_TEE_NAME);
                dl.teeYard = Utils.getIntegerFromJsonObjectWithKey(joDataInfoListItem, JsonKey.EDIT_HOLE_TEE_YARD);
                dataList.dataInfoList.add(dl);
            }

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    public DataList getDataList() {
        return dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }
}
