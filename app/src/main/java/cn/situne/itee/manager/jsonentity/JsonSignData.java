package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * Created by luochao on 11/23/15.
 */
public class JsonSignData extends BaseJsonObject implements Serializable {


    public JsonSignData(JSONObject jsonObject) {
        super(jsonObject);
    }

    public ArrayList<SigningData> dataList;

    private String memberName;

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public ArrayList<SigningData> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<SigningData> dateList) {
        this.dataList = dateList;
    }

    public static class SigningData implements Serializable {
        private String type;//1 mem 2 agent 3 event
        private String showTitle;

        public String getShowTitle() {
            return showTitle;
        }

        public void setShowTitle(String showTitle) {
            this.showTitle = showTitle;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        private String keyIndex;
        private String memberName;//name
        private String memberId;//id
        private String memberNo;//  agent not
        private String phone;
        private String nameSort;
        private String signNumber;//member have
        private String agentCode;//agent id


        private String fciZip;
        private String freqBirth;
        private String photo;



        private String startDate;
        private String endDate;
        private String startTime;
        private String endTime;

        private String keyWord;

        public String getKeyWord() {
            return keyWord;
        }

        public void setKeyWord(String keyWord) {
            this.keyWord = keyWord;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        private String memberContact;

        public String getMemberContact() {
            return memberContact;
        }

        public void setMemberContact(String memberContact) {
            this.memberContact = memberContact;
        }

        public String getFciZip() {
            return fciZip;
        }

        public void setFciZip(String fciZip) {
            this.fciZip = fciZip;
        }

        public String getFreqBirth() {
            return freqBirth;
        }

        public void setFreqBirth(String freqBirth) {
            this.freqBirth = freqBirth;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getKeyIndex() {
            return keyIndex;
        }

        public void setKeyIndex(String keyIndex) {
            this.keyIndex = keyIndex;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getMemberNo() {
            return memberNo;
        }

        public void setMemberNo(String memberNo) {
            this.memberNo = memberNo;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNameSort() {
            return nameSort;
        }

        public void setNameSort(String nameSort) {
            this.nameSort = nameSort;
        }

        public String getSignNumber() {
            return signNumber;
        }

        public void setSignNumber(String signNumber) {
            this.signNumber = signNumber;
        }

        public String getAgentCode() {
            return agentCode;
        }

        public void setAgentCode(String agentCode) {
            this.agentCode = agentCode;
        }
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        dataList = new ArrayList<>();
        try {



            if (jsonObj.has(JsonKey.TEE_TIME_SIGNINGGUEST_MEMBER_NAME)){

                setMemberName(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.TEE_TIME_SIGNINGGUEST_MEMBER_NAME));
            }

            JSONObject jsonObject = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            JSONArray jsMemberArray = jsonObject.getJSONArray(JsonKey.TEE_TIME_SIGNINGGUEST_MEMBER);
            JSONArray jsAgentArray = jsonObject.getJSONArray(JsonKey.TEE_TIME_SIGNINGGUEST_AGENT);
            JSONArray jsEventArray =jsonObject.getJSONArray(JsonKey.TEE_TIME_SIGNINGGUEST_EVENT);
            for (int i = 0; i < jsMemberArray.length(); i++) {
                JSONObject jsMemberArrayObject = jsMemberArray.getJSONObject(i);
                JsonSignData.SigningData  signingData = new JsonSignData.SigningData();
                signingData.setMemberNo(Utils.getStringFromJsonObjectWithKey(jsMemberArrayObject, JsonKey.TEE_TIME_SIGNINGGUEST_MEMBER_NO));
                signingData.setSignNumber(Utils.getStringFromJsonObjectWithKey(jsMemberArrayObject, JsonKey.TEE_TIME_SIGNINGGUEST_SIGN_NUMBER));
                signingData.setMemberName(Utils.getStringFromJsonObjectWithKey(jsMemberArrayObject, JsonKey.TEE_TIME_SIGNINGGUEST_MEMBER_NAME));
                signingData.setMemberId(Utils.getStringFromJsonObjectWithKey(jsMemberArrayObject, JsonKey.TEE_TIME_SIGNINGGUEST_MEMBER_ID));
                signingData.setNameSort(Utils.getStringFromJsonObjectWithKey(jsMemberArrayObject, JsonKey.TEE_TIME_SIGNINGGUEST_NAME_SORT));
                signingData.setPhone(Utils.getStringFromJsonObjectWithKey(jsMemberArrayObject, JsonKey.TEE_TIME_SIGNINGGUEST_MEMBER_PHONE));
                signingData.setKeyIndex(Utils.getStringFromJsonObjectWithKey(jsMemberArrayObject, JsonKey.TEE_TIME_SIGNINGGUEST_KEY_INDEX));
                signingData.setFciZip(Utils.getStringFromJsonObjectWithKey(jsMemberArrayObject, JsonKey.TEE_TIME_SIGNINGGUEST_FCI_ZIP));
                signingData.setPhoto(Utils.getStringFromJsonObjectWithKey(jsMemberArrayObject, JsonKey.TEE_TIME_SIGNINGGUEST_PHOTO));
                signingData.setFreqBirth(Utils.getStringFromJsonObjectWithKey(jsMemberArrayObject, JsonKey.TEE_TIME_SIGNINGGUEST_FREQ_BIRTH));

                signingData.setKeyWord(Utils.getStringFromJsonObjectWithKey(jsMemberArrayObject, JsonKey.COMMON_KEY_WORD));
                signingData.setType("1");
                dataList.add(signingData);
            }
            for (int i = 0; i < jsAgentArray.length(); i++) {
                JSONObject jsAgentObject = jsAgentArray.getJSONObject(i);
                JsonSignData.SigningData  signingData = new JsonSignData.SigningData();
                signingData.setMemberName(Utils.getStringFromJsonObjectWithKey(jsAgentObject, JsonKey.TEE_TIME_SIGNINGGUEST_MEMBER_NAME));
                signingData.setMemberId(Utils.getStringFromJsonObjectWithKey(jsAgentObject, JsonKey.TEE_TIME_SIGNINGGUEST_MEMBER_ID));
                signingData.setAgentCode(Utils.getStringFromJsonObjectWithKey(jsAgentObject, JsonKey.TEE_TIME_SIGNINGGUEST_AGEN_CODE));
                signingData.setNameSort(Utils.getStringFromJsonObjectWithKey(jsAgentObject, JsonKey.TEE_TIME_SIGNINGGUEST_NAME_SORT));
                signingData.setPhone(Utils.getStringFromJsonObjectWithKey(jsAgentObject, JsonKey.TEE_TIME_SIGNINGGUEST_MEMBER_PHONE));
                signingData.setKeyIndex(Utils.getStringFromJsonObjectWithKey(jsAgentObject, JsonKey.TEE_TIME_SIGNINGGUEST_KEY_INDEX));
                signingData.setMemberContact(Utils.getStringFromJsonObjectWithKey(jsAgentObject, JsonKey.TEE_TIME_SIGNINGGUEST_MEMBER_CONTACT));
                signingData.setKeyWord(Utils.getStringFromJsonObjectWithKey(jsAgentObject, JsonKey.COMMON_KEY_WORD));
                signingData.setType("2");
                dataList.add(signingData);
            }
            for (int i = 0; i < jsEventArray.length(); i++) {
                JSONObject jsEventObject = jsEventArray.getJSONObject(i);
                JsonSignData.SigningData  signingData = new JsonSignData.SigningData();
                signingData.setMemberName(Utils.getStringFromJsonObjectWithKey(jsEventObject, JsonKey.TEE_TIME_SIGNINGGUEST_MEMBER_NAME));
                signingData.setMemberId(Utils.getStringFromJsonObjectWithKey(jsEventObject, JsonKey.TEE_TIME_SIGNINGGUEST_MEMBER_ID));
                signingData.setMemberNo(Utils.getStringFromJsonObjectWithKey(jsEventObject, JsonKey.TEE_TIME_SIGNINGGUEST_MEMBER_NO));
                signingData.setNameSort(Utils.getStringFromJsonObjectWithKey(jsEventObject, JsonKey.TEE_TIME_SIGNINGGUEST_NAME_SORT));
                signingData.setPhone(Utils.getStringFromJsonObjectWithKey(jsEventObject, JsonKey.TEE_TIME_SIGNINGGUEST_MEMBER_PHONE));
                signingData.setKeyIndex(Utils.getStringFromJsonObjectWithKey(jsEventObject, JsonKey.TEE_TIME_SIGNINGGUEST_KEY_INDEX));

                signingData.setStartDate(Utils.getStringFromJsonObjectWithKey(jsEventObject, JsonKey.TEE_TIME_SIGNINGGUEST_START_DATE));
                signingData.setStartTime(Utils.getStringFromJsonObjectWithKey(jsEventObject, JsonKey.TEE_TIME_SIGNINGGUEST_START_TIME));
                signingData.setEndDate(Utils.getStringFromJsonObjectWithKey(jsEventObject, JsonKey.TEE_TIME_SIGNINGGUEST_END_DATE));
                signingData.setEndTime(Utils.getStringFromJsonObjectWithKey(jsEventObject, JsonKey.TEE_TIME_SIGNINGGUEST_END_TIME));

                signingData.setKeyWord(Utils.getStringFromJsonObjectWithKey(jsEventObject, JsonKey.COMMON_KEY_WORD));
                signingData.setType("3");
                dataList.add(signingData);
            }

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }


    }
}


