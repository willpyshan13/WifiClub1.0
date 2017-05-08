/**
 * Project Name: itee
 * File Name:  JsonCaddiePriceGet.java
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

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonCaddiePriceGet <br/>
 * Function: Get Caddie Price. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author luochao
 * @version 1.0
 * @see
 */

public class JsonCaddiePriceGet extends BaseJsonObject implements Serializable {


    private ArrayList<DataList> dataList;

    public ArrayList<DataList> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<DataList> dataList) {
        this.dataList = dataList;
    }

    public JsonCaddiePriceGet(JSONObject jsonObject) {
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
                    JSONObject joData = arrDataList.getJSONObject(i);
                    DataList item = new DataList();
                    item.setId(Utils.getIntegerFromJsonObjectWithKey(joData, JsonKey.SHOPS_CADDIE_PRICE_ID));
                    item.setLevel(Utils.getStringFromJsonObjectWithKey(joData, JsonKey.SHOPS_CADDIE_PRICE_LEVEL));
                    item.setPrice(Utils.getStringFromJsonObjectWithKey(joData, JsonKey.SHOPS_CADDIE_PRICE_PRICE));
                    item.setReturn_time(Utils.getIntegerFromJsonObjectWithKey(joData, JsonKey.SHOPS_CADDIE_PRICE_RETURN_TIME));
                    item.setCode(Utils.getStringFromJsonObjectWithKey(joData, JsonKey.SHOPS_CADDIE_PRICE_CODE));
                    dataList.add(item);
                }
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }

    public class DataList implements Serializable {
        private int id;
        private int return_time;
        private String level;
        private String price;

        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getReturn_time() {
            return return_time;
        }

        public void setReturn_time(int return_time) {
            this.return_time = return_time;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}