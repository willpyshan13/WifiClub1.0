/**
 * Project Name: itee
 * File Name:  JsonAgentListGet.java
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
 * ClassName:JsonAgentListGet <br/>
 * Function: Get Json agentsList list information. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class JsonAgentListGet extends BaseJsonObject implements Serializable {

    private ArrayList<Agent> agentsList;

    public ArrayList<Agent> getAgentsList() {
        return agentsList;
    }

    public static class Agent implements Serializable {

        public Integer agentId;
        public String agentName;

        public Integer getAgentId() {
            return agentId;
        }

        public void setAgentId(Integer agentId) {
            this.agentId = agentId;
        }

        public String getAgentName() {
            return agentName;
        }
    }

    public JsonAgentListGet(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        agentsList = new ArrayList<>();
        try {
            JSONArray jsonArray = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.EVENT_LIST);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Agent agent = new Agent();
                agent.agentId = Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.AGENT_ID);
                agent.agentName = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.AGENT_NAME);
                agentsList.add(agent);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());

        }
    }

}
