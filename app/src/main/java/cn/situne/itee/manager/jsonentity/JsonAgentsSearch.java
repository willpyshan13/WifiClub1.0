/**
 * Project Name: itee
 * File Name:	 JsonAgentsSearch.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-07-27
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonAgentsSearch <br/>
 * Function: entity of agents_search. <br/>
 * Date: 2015-07-27 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonAgentsSearch extends BaseJsonObject {

    private ArrayList<Agent> agentsList;

    public ArrayList<Agent> getAgentsList() {
        return agentsList;
    }

    public JsonAgentsSearch(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        agentsList = new ArrayList<>();

        if (jsonObj != null) {
            try {
                JSONArray jaDataList = jsonObj.getJSONArray(JsonKey.COMMON_DATA_LIST);
                for (int i = 0; i < jaDataList.length(); i++) {
                    JSONObject joAgent = jaDataList.getJSONObject(i);
                    Agent agent = new Agent();
                    agent.setAgentId(Utils.getStringFromJsonObjectWithKey(joAgent, JsonKey.AGENTS_SEARCH_ID));
                    agent.setAgentAccount(Utils.getStringFromJsonObjectWithKey(joAgent, JsonKey.AGENTS_SEARCH_ACCOUNT));
                    agent.setAciValue(Utils.getStringFromJsonObjectWithKey(joAgent, JsonKey.AGENTS_SEARCH_ACI_VALUE));
                    agentsList.add(agent);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Agent {
        private String agentId;
        private String agentAccount;
        private String aciValue;

        public String getAgentId() {
            return agentId;
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }

        public String getAgentAccount() {
            return agentAccount;
        }

        public void setAgentAccount(String agentAccount) {
            this.agentAccount = agentAccount;
        }

        public String getAciValue() {
            return aciValue;
        }

        public void setAciValue(String aciValue) {
            this.aciValue = aciValue;
        }
    }
}