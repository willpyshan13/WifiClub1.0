/**
 * Project Name: itee
 * File Name:	 JsonCaddieLevelGet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-03-21
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonCaddieLevelGet <br/>
 * Function: entity of caddieLevel get. <br/>
 * Date: 2015-03-21 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonCaddieLevelGet extends BaseJsonObject {
    public JsonCaddieLevelGet(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {
            dataList = new ArrayList<>();
            try {
                JSONArray arrDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
                for (int i = 0; i < arrDataList.length(); i++) {
                    JSONObject joLevel = arrDataList.getJSONObject(i);
                    Level ll = new Level();
                    ll.setLevId(Utils.getIntegerFromJsonObjectWithKey(joLevel, JsonKey.STAFF_CADDIE_LEVEL_ID));
                    ll.setLevName(Utils.getStringFromJsonObjectWithKey(joLevel, JsonKey.STAFF_CADDIE_LEVEL_NAME));
                    dataList.add(ll);
                }
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }

    private ArrayList<Level> dataList;

    public ArrayList<Level> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<Level> dataList) {
        this.dataList = dataList;
    }

    public static class Level {
        private int levId;
        private String levName;

        public int getLevId() {
            return levId;
        }

        public void setLevId(int levId) {
            this.levId = levId;
        }

        public String getLevName() {
            return levName;
        }

        public void setLevName(String levName) {
            this.levName = levName;
        }
    }

}