/**
 * Project Name: itee
 * File Name:  JsonSubclassCode.java
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
 * ClassName:JsonSubclassCode <br/>
 * Function: To set subclass code. <br/>
 * Date: 2015-03-19 <br/>
 *
 * @author Luochao
 * @version 1.0
 * @see
 */
public class JsonSubclassCode extends BaseJsonObject implements Serializable {

    private List<SubclassCode> dataList;

    public List<SubclassCode> getDataList() {
        return dataList;
    }

    public void setDataList(List<SubclassCode> dataList) {
        this.dataList = dataList;
    }

    public JsonSubclassCode(JSONObject jsonObject) {
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
                SubclassCode bean = new SubclassCode();

//                bean.setPraId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_PRA_ID));
//                bean.setPraParentId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_PRA_PARENT_ID));
//                bean.setPraLevel(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_PRA_LEVEL));
//                bean.setPraPdtId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_PRA_PDT_ID));
//                bean.setPraName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_PRA_NAME));
                bean.setPraCode(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_PRA_CODE));

                dataList.add(bean);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }


    }

    class SubclassCode implements Serializable {
        private int praId;
        private int praParentId;
        private int praLevel;
        private int praPdtId;
        private String praName;
        private String praCode;

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

        public String getPraCode() {
            return praCode;
        }

        public void setPraCode(String praCode) {
            this.praCode = praCode;
        }
    }
}





