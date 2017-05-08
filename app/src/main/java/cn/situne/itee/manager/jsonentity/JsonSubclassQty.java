/**
 * Project Name: itee
 * File Name:  JsonSubclassQty.java
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
 * ClassName:JsonSubclassQty <br/>
 * Function: To set subclassQty. <br/>
 * Date: 2015-03-19 <br/>
 *
 * @author Luochao
 * @version 1.0
 * @see
 */
public class JsonSubclassQty extends BaseJsonObject implements Serializable {

    private List<SubclassQty> dataList;

    public List<SubclassQty> getDataList() {
        return dataList;
    }

    public void setDataList(List<SubclassQty> dataList) {
        this.dataList = dataList;
    }

    public JsonSubclassQty(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new ArrayList();

        try {
            JSONArray joDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
            for (int i = 0; i < joDataList.length(); i++) {
                JSONObject jsonObject = joDataList.getJSONObject(i);
                SubclassQty bean = new SubclassQty();

//                bean.setPraId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_PRA_ID));
//                bean.setPraParentId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_PRA_PARENT_ID));
//                bean.setPraLevel(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_PRA_LEVEL));
//                bean.setPraPdtId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_PRA_PDT_ID));
//                bean.setPraName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_PRA_NAME));
                bean.setPraQty(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_PRA_QTY));

                dataList.add(bean);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }


    }

    class SubclassQty implements Serializable {

        private int praId;
        private int praParentId;
        private int praLevel;
        private int praPdtId;
        private String praName;
        private int praQty;

        public int getPraId() {
            return praId;
        }

        public void setPraId(int praId) {
            this.praId = praId;
        }

        public int getPraParentId() {
            return praParentId;
        }

        public void setPraParentId(int praParentId) {
            this.praParentId = praParentId;
        }

        public int getPraLevel() {
            return praLevel;
        }

        public void setPraLevel(int praLevel) {
            this.praLevel = praLevel;
        }

        public int getPraPdtId() {
            return praPdtId;
        }

        public void setPraPdtId(int praPdtId) {
            this.praPdtId = praPdtId;
        }

        public String getPraName() {
            return praName;
        }

        public void setPraName(String praName) {
            this.praName = praName;
        }

        public int getPraQty() {
            return praQty;
        }

        public void setPraQty(int praQty) {
            this.praQty = praQty;
        }
    }
}





