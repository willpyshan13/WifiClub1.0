/**
 * Project Name: itee
 * File Name:  JsonAgentListGet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
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
 * ClassName:JsonAgentListGet <br/>
 * Function: Get Json agentsList list information. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class JsonSpecialDay extends BaseJsonObject implements Serializable {


    private ArrayList<SpecialDay> dataList;

    public ArrayList<SpecialDay> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<SpecialDay> dataList) {
        this.dataList = dataList;
    }

    public static class SpecialDay implements Serializable {


        private String cdtAct;
        private String cdtFirstName;
        private String cdtId;
        private String cdtName;

        public SpecialDay() {
            cdtAct = "";
            cdtName = "";
            cdtFirstName = "";
            cdtId = "";
        }

        public String getCdtAct() {
            return cdtAct;
        }

        public void setCdtAct(String cdtAct) {
            this.cdtAct = cdtAct;
        }

        public String getCdtFirstName() {
            return cdtFirstName;
        }

        public void setCdtFirstName(String cdtFirstName) {
            this.cdtFirstName = cdtFirstName;
        }

        public String getCdtId() {
            return cdtId;
        }

        public void setCdtId(String cdtId) {
            this.cdtId = cdtId;
        }

        public String getCdtName() {
            return cdtName;
        }

        public void setCdtName(String cdtName) {
            this.cdtName = cdtName;
        }
    }

    public JsonSpecialDay(JSONObject jsonObject) {
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
                SpecialDay agent = new SpecialDay();
                agent.setCdtAct(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.ADMINISTRATION_SPECIAL_DAYS_CDT_ACT));
                agent.setCdtFirstName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.ADMINISTRATION_SPECIAL_DAYS_CDT_FIRST_NAME));
                agent.setCdtId(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.ADMINISTRATION_SPECIAL_DAYS_CDT_ID));
                agent.setCdtName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.ADMINISTRATION_SPECIAL_DAYS_CDT_NAME));
                dataList.add(agent);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());

        }
    }

}
