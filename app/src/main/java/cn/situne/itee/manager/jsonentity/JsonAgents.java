/**
 * Project Name: itee
 * File Name:  JsonAgents.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonAgents <br/>
 * Function: Set Json agents list information. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class JsonAgents extends BaseJsonObject implements Serializable {

    private static final long serialVersionUID = -4350546933779017349L;

    private List<AgentList> agentList;

    public List<AgentList> getAgentList() {
        return agentList;
    }

    public void setAgentList(List<AgentList> agentList) {
        this.agentList = agentList;
    }


    public static class AgentList implements Serializable {
        public String agentName;
        public String agentAccount;
        public String agentMobile;
        public String agentEmail;
        public String agentBalanceAccount;
        public String agentHistory;
        public String agentMoneyType;
        public String agentPassWord;


        public int purchaseHistoryCount;
        public int balancesAccountCount;


        public int getPurchaseHistoryCount() {
            return purchaseHistoryCount;
        }

        public void setPurchaseHistoryCount(int purchaseHistoryCount) {
            this.purchaseHistoryCount = purchaseHistoryCount;
        }

        public int getBalancesAccountCount() {
            return balancesAccountCount;
        }

        public void setBalancesAccountCount(int balancesAccountCount) {
            this.balancesAccountCount = balancesAccountCount;
        }

        public String getAgentName() {
            return agentName;
        }

        public void setAgentName(String agentName) {
            this.agentName = agentName;
        }

        public String getAgentAccount() {
            return agentAccount;
        }

        public void setAgentAccount(String agentAccount) {
            this.agentAccount = agentAccount;
        }

        public String getAgentMobile() {
            return agentMobile;
        }

        public void setAgentMobile(String agentMobile) {
            this.agentMobile = agentMobile;
        }

        public String getAgentEmail() {
            return agentEmail;
        }

        public void setAgentEmail(String agentEmail) {
            this.agentEmail = agentEmail;
        }

        public String getAgentBalanceAccount() {
            return agentBalanceAccount;
        }

        public void setAgentBalanceAccount(String agentBalanceAccount) {
            this.agentBalanceAccount = agentBalanceAccount;
        }

        public String getAgentHistory() {
            return agentHistory;
        }

        public void setAgentHistory(String agentHistory) {
            this.agentHistory = agentHistory;
        }

        public String getAgentMoneyType() {
            return agentMoneyType;
        }

        public void setAgentMoneyType(String agentMoneyType) {
            this.agentMoneyType = agentMoneyType;
        }

        public String getAgentPassWord() {
            return agentPassWord;
        }

        public void setAgentPassWord(String agentPassWord) {
            this.agentPassWord = agentPassWord;
        }
    }

    public JsonAgents(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        agentList = new ArrayList<>();
        //JSONArray jsonArray = jsonObj.Utils.getArrayFromJsonObjectWithKey(JsonKey.EVENT_LIST);

        try {
            JSONObject jsonObject = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
                AgentList agent = new AgentList();
                agent.agentName = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.AGENT_NAME);
                agent.agentAccount = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.AGENT_ACCOUNT);
                agent.agentMobile = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.AGENT_MOBILE);
                agent.agentEmail = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.AGENT_EMAIL);
                agent.agentBalanceAccount = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.AGENT_BALANCE_ACCOUNT);
                agent.agentHistory = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.AGENT_HISTORY);
                agent.agentMoneyType = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.AGENT_MONEY_TYPE);
                agent.agentPassWord = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.AGENT_PASS_WORD);
            agent.setBalancesAccountCount(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.AGENT_BALANCES_ACCOUNT_COUNT));
            agent.setPurchaseHistoryCount(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.AGENT_PURCHASE_HISTORY_COUNT));
                agentList.add(agent);

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }

}
