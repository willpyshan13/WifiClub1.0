/**
 * Project Name: itee
 * File Name:  JsonGeneralInfo.java
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
import java.util.List;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonGeneralInfo <br/>
 * Function: To set general information. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonGeneralInfo extends BaseJsonObject implements Serializable {

    private DataList dataList;

    public JsonGeneralInfo(JSONObject jsonObject) {
        super(jsonObject);
    }

    public DataList getDataList() {
        return dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }


    public class DataList implements Serializable {
        private String memberName;
        private String occupation;
        private String birth;
        private String gender;
        private Integer acceptInfoStatus;
        private String hobbies;
        private String notes;

        private String sianature;

        private String memberPhoto;


        private String msgFlag;

        private String msgDate;

        private String msgInfo;

        public String getMsgFlag() {
            return msgFlag;
        }

        public void setMsgFlag(String msgFlag) {
            this.msgFlag = msgFlag;
        }

        public String getMsgDate() {
            return msgDate;
        }

        public void setMsgDate(String msgDate) {
            this.msgDate = msgDate;
        }

        public String getMsgInfo() {
            return msgInfo;
        }

        public void setMsgInfo(String msgInfo) {
            this.msgInfo = msgInfo;
        }

        public String getSianature() {
            return sianature;
        }

        public void setSianature(String sianature) {
            this.sianature = sianature;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getMemberPhoto() {
            return memberPhoto;
        }

        public void setMemberPhoto(String memberPhoto) {
            this.memberPhoto = memberPhoto;
        }

        private List<PhoneItem> phoneList;
        private List<EmailItem> emailList;
        private List<AddressItem> addressList;

        public String getOccupation() {
            return occupation;
        }

        public void setOccupation(String occupation) {
            this.occupation = occupation;
        }

        public String getBirth() {
            return birth;
        }

        public void setBirth(String birth) {
            this.birth = birth;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Integer getAcceptInfoStatus() {
            return acceptInfoStatus;
        }

        public void setAcceptInfoStatus(Integer acceptInfoStatus) {
            this.acceptInfoStatus = acceptInfoStatus;
        }

        public String getHobbies() {
            return hobbies;
        }

        public void setHobbies(String hobbies) {
            this.hobbies = hobbies;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public List<PhoneItem> getPhoneList() {
            return phoneList;
        }

        public void setPhoneList(List<PhoneItem> phoneList) {
            this.phoneList = phoneList;
        }

        public List<EmailItem> getEmailList() {
            return emailList;
        }

        public void setEmailList(List<EmailItem> emailList) {
            this.emailList = emailList;
        }

        public List<AddressItem> getAddressList() {
            return addressList;
        }

        public void setAddressList(List<AddressItem> addressList) {
            this.addressList = addressList;
        }
    }


    public static class PhoneItem implements Serializable {

        private Integer status;
        private String tag;
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }

    public static class EmailItem implements Serializable {

        private Integer status;
        private String tag;
        private String value;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class AddressItem implements Serializable {

        private Integer status;
        private String tag;
        private String value;
        private String country;
        private String province;
        private String city;
        private String zip;


        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        if (jsonObj != null) {
            dataList = new DataList();
            try {
                JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
                dataList.setOccupation(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_GENERAL_INFO_OCCUPATION));
                dataList.setBirth(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_GENERAL_INFO_BIRTH));
                dataList.setGender(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_GENERAL_INFO_GENDER));
                dataList.setAcceptInfoStatus(Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_GENERAL_INFO_ACCEPT_INFO_STATUS));
                dataList.setHobbies(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_GENERAL_INFO_HOBBIES));
                dataList.setNotes(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_GENERAL_INFO_NOTES));
                dataList.setSianature(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_GENERAL_INFO_SIANATURE));
                dataList.setMemberName(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_GENERAL_INFO_MEMBER_NAME));
                dataList.setMemberPhoto(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_GENERAL_INFO_MEMBER_PHOTO));
                if (joDataList.has(JsonKey.PLAYER_GENERAL_MSG_FLAG)){
                    dataList.setMsgFlag(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_GENERAL_MSG_FLAG));
                }
                if (joDataList.has(JsonKey.PLAYER_GENERAL_MSG_INFO)){
                    dataList.setMsgInfo(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_GENERAL_MSG_INFO));
                }
                if (joDataList.has(JsonKey.PLAYER_GENERAL_MSG_DATE)){
                    dataList.setMsgDate(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_GENERAL_MSG_DATE));
                }
                List<PhoneItem> phoneList = new ArrayList<>();
                JSONArray jaPhoneList = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_GENERAL_INFO_PHONE_LIST);
                for (int i = 0; i < jaPhoneList.length(); i++) {

                    JSONObject joPhoneItem = jaPhoneList.getJSONObject(i);
                    PhoneItem listItem = new PhoneItem();
                    listItem.setStatus(Utils.getIntegerFromJsonObjectWithKey(joPhoneItem, JsonKey.PLAYER_GENERAL_INFO_PHOTO_STATUS));
                    listItem.setTag(Utils.getStringFromJsonObjectWithKey(joPhoneItem, JsonKey.PLAYER_GENERAL_INFO_PHONE_TAG));
                    listItem.setValue(Utils.getStringFromJsonObjectWithKey(joPhoneItem, JsonKey.PLAYER_GENERAL_INFO_PHONE));
                    phoneList.add(listItem);
                }
                dataList.setPhoneList(phoneList);
                List<EmailItem> emailList = new ArrayList<>();
                JSONArray jaEmailList = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_GENERAL_INFO_EMAIL_LIST);
                for (int i = 0; i < jaEmailList.length(); i++) {

                    JSONObject joPhoneItem = jaEmailList.getJSONObject(i);
                    EmailItem listItem = new EmailItem();
                    listItem.setStatus(Utils.getIntegerFromJsonObjectWithKey(joPhoneItem, JsonKey.PLAYER_GENERAL_INFO_PHOTO_STATUS));
                    listItem.setTag(Utils.getStringFromJsonObjectWithKey(joPhoneItem, JsonKey.PLAYER_GENERAL_INFO_PHONE_TAG));
                    listItem.setValue(Utils.getStringFromJsonObjectWithKey(joPhoneItem, JsonKey.PLAYER_GENERAL_INFO_PHONE));
                    emailList.add(listItem);
                }
                dataList.setEmailList(emailList);

                List<AddressItem> addressList = new ArrayList<>();
                JSONArray jaAddressList = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_GENERAL_INFO_ADDRESS_LIST);
                for (int i = 0; i < jaAddressList.length(); i++) {

                    JSONObject joPhoneItem = jaAddressList.getJSONObject(i);
                    AddressItem listItem = new AddressItem();
                    listItem.setStatus(Utils.getIntegerFromJsonObjectWithKey(joPhoneItem, JsonKey.PLAYER_GENERAL_INFO_PHOTO_STATUS));
                    listItem.setTag(Utils.getStringFromJsonObjectWithKey(joPhoneItem, JsonKey.PLAYER_GENERAL_INFO_PHONE_TAG));
                    listItem.setValue(Utils.getStringFromJsonObjectWithKey(joPhoneItem, JsonKey.PLAYER_GENERAL_INFO_PHONE));

                    listItem.setCountry(Utils.getStringFromJsonObjectWithKey(joPhoneItem, JsonKey.PLAYER_GENERAL_INFO_COUNTRY));
                    listItem.setProvince(Utils.getStringFromJsonObjectWithKey(joPhoneItem, JsonKey.PLAYER_GENERAL_INFO_PROVINCE));
                    listItem.setCity(Utils.getStringFromJsonObjectWithKey(joPhoneItem, JsonKey.PLAYER_GENERAL_INFO_CITY));
                    listItem.setZip(Utils.getStringFromJsonObjectWithKey(joPhoneItem, JsonKey.PLAYER_GENERAL_INFO_ZIP));


                    addressList.add(listItem);
                }
                dataList.setAddressList(addressList);

            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        } else {
            dataList = new DataList();
            List<PhoneItem> phoneList = new ArrayList<>();
            dataList.setPhoneList(phoneList);
            List<EmailItem> emailList = new ArrayList<>();
            dataList.setEmailList(emailList);
            List<AddressItem> addressList = new ArrayList<>();
            dataList.setAddressList(addressList);

        }
    }
}