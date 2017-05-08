/**
 * Project Name: itee
 * File Name:  JsonTeeNameList.java
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
import java.util.List;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonTeeNameList <br/>
 * Function: To set tee name list. <br/>
 * Date: 2015-03-19 <br/>
 *
 * @author Luochao
 * @version 1.0
 * @see
 */
public class JsonTeeNameList extends BaseJsonObject implements Serializable {

    private List<DataList> dataList;

    public List<DataList> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataList> dataList) {
        this.dataList = dataList;
    }

    public static class DataList implements Serializable {
        public int teeId;
        public String teeName;

        public int getTeeId() {
            return teeId;
        }

        public void setTeeId(int teeId) {
            this.teeId = teeId;
        }

        public String getTeeName() {
            return teeName;
        }

        public void setTeeName(String teeName) {
            this.teeName = teeName;
        }
    }

    public JsonTeeNameList(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        dataList = new ArrayList<>();

        try {
            JSONArray jsonArray = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                DataList data = new DataList();
                data.teeId = Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.EDIT_HOLE_TEE_ID);
                data.teeName = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.EDIT_HOLE_TEE_NAME);
                dataList.add(data);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }

}
