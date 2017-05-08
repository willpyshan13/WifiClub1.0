/**
 * Project Name: itee
 * File Name:  JsonAgentsEdit.java
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
 * ClassName:JsonAgentsEdit <br/>
 * Function: Edit Json agents. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class JsonAgentsEdit extends BaseJsonObject implements Serializable {

    public JsonAgentsEdit(JSONObject jsonObject) {
        super(jsonObject);
    }

    private DataList dataList;

    public static class DataList implements Serializable {
        public String agentName;
        public String agentContactName;
        public String agentAccount;
        public String agentPassWord;
        public String agentNotes;

        public String agentCode;

        public String getAgentCode() {
            return agentCode;
        }

        public void setAgentCode(String agentCode) {
            this.agentCode = agentCode;
        }

        private ArrayList<Phone> phoneList;
        private ArrayList<Email> emailList;

        public String getAgentName() {
            return agentName;
        }

        public void setAgentName(String agentName) {
            this.agentName = agentName;
        }

        public String getAgentContactName() {
            return agentContactName;
        }

        public void setAgentContactName(String agentContactName) {
            this.agentContactName = agentContactName;
        }

        public String getAgentAccount() {
            return agentAccount;
        }

        public void setAgentAccount(String agentAccount) {
            this.agentAccount = agentAccount;
        }

        public String getAgentPassWord() {
            return agentPassWord;
        }

        public void setAgentPassWord(String agentPassWord) {
            this.agentPassWord = agentPassWord;
        }

        public String getAgentNotes() {
            return agentNotes;
        }

        public void setAgentNotes(String agentNotes) {
            this.agentNotes = agentNotes;
        }

        public ArrayList<Phone> getPhoneList() {
            return phoneList;
        }

        public void setPhoneList(ArrayList<Phone> phoneList) {
            this.phoneList = phoneList;
        }

        public ArrayList<Email> getEmailList() {
            return emailList;
        }

        public void setEmailList(ArrayList<Email> emailList) {
            this.emailList = emailList;
        }

        public static class Phone implements Serializable {
            private Integer phoneStatus;
            private String phoneTag;
            private String phone;

            public Integer getPhoneStatus() {
                return phoneStatus;
            }

            public void setPhoneStatus(Integer phoneStatus) {
                this.phoneStatus = phoneStatus;
            }

            public String getPhoneTag() {
                return phoneTag;
            }

            public void setPhoneTag(String phoneTag) {
                this.phoneTag = phoneTag;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }
        }

        public static class Email implements Serializable {
            private Integer emailStatus;
            private String emailTag;
            private String email;

            public Integer getEmailStatus() {
                return emailStatus;
            }

            public void setEmailStatus(Integer emailStatus) {
                this.emailStatus = emailStatus;
            }

            public String getEmailTag() {
                return emailTag;
            }

            public void setEmailTag(String emailTag) {
                this.emailTag = emailTag;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }
        }

    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new DataList();
        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            dataList.agentName = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.AGENT_NAME);
            dataList.agentContactName = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.AGENT_CONTACT_NAME);
            dataList.agentAccount = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.AGENT_ACCOUNT);
            dataList.agentPassWord = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.AGENT_PASS_WORD);
            dataList.agentNotes = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.AGENT_NOTES);


            dataList.setAgentCode( Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.AGEN_CODE));

            dataList.emailList = new ArrayList<>();
            try {
                JSONArray arrEmail = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.STAFF_DEPARTMENT_EMAIL_LIST);
                for (int i = 0; i < arrEmail.length(); i++) {
                    JSONObject joEmail = arrEmail.getJSONObject(i);
                    DataList.Email e = new DataList.Email();
                    e.setEmail(Utils.getStringFromJsonObjectWithKey(joEmail, JsonKey.AGENT_EMAIL_LIST_EMAIL));
                    e.setEmailTag(Utils.getStringFromJsonObjectWithKey(joEmail, JsonKey.AGENT_EMAIL_LIST_EMAIL_TAG));
                    e.setEmailStatus(Utils.getIntegerFromJsonObjectWithKey(joEmail, JsonKey.AGENT_EMAIL_LIST_EMAIL_STATUS));
                    dataList.emailList.add(e);
                }
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }

            dataList.phoneList = new ArrayList<>();
            try {
                JSONArray arrPhone = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.STAFF_DEPARTMENT_PHONE_LIST);
                for (int i = 0; i < arrPhone.length(); i++) {
                    JSONObject joPhone = arrPhone.getJSONObject(i);
                    DataList.Phone p = new DataList.Phone();
                    p.setPhone(Utils.getStringFromJsonObjectWithKey(joPhone, JsonKey.AGENT_PHONE_LIST_PHONE));
                    p.setPhoneTag(Utils.getStringFromJsonObjectWithKey(joPhone, JsonKey.AGENT_PHONE_LIST_PHONE_TAG));
                    p.setPhoneStatus(Utils.getIntegerFromJsonObjectWithKey(joPhone, JsonKey.AGENT_PHONE_LIST_PHONE_STATUS));
                    dataList.phoneList.add(p);
                }
            } catch (JSONException e) {
                Utils.log(e.getMessage());
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
