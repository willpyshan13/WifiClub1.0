/**
 * Project Name: itee
 * File Name:  JsonNormalShop.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-23
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonNormalShop <br/>
 * Function: To set normal shop. <br/>
 * Date: 2015-03-23 <br/>
 *
 * @author luochao
 * @version 1.0
 * @see
 */

public class JsonReturnMemberId extends BaseJsonObject implements Serializable {


    private Member dataList;

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

    public JsonReturnMemberId(JSONObject jsonObject) {
        super(jsonObject);
    }

    public Member getDataList() {
        return dataList;
    }

    public void setDataList(Member dataList) {
        this.dataList = dataList;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        try {
            JSONObject jsonObject = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            dataList = new Member();
            dataList.setMemberId(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.PLAYER_PROFILE_MEMBER_ID));

            if (jsonObject.has(JsonKey.PLAYER_GENERAL_MSG_FLAG)){
               setMsgFlag(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.PLAYER_GENERAL_MSG_FLAG));
            }
            if (jsonObject.has(JsonKey.PLAYER_GENERAL_MSG_INFO)){
                setMsgInfo(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.PLAYER_GENERAL_MSG_INFO));
            }
            if (jsonObject.has(JsonKey.PLAYER_GENERAL_MSG_DATE)){
                setMsgDate(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.PLAYER_GENERAL_MSG_DATE));
            }




        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }

    public class Member implements Serializable {
        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        private String memberId;


    }

}





