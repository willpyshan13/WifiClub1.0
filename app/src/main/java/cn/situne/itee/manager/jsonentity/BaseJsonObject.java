/**
 * Project Name: itee
 * File Name:  BaseJsonObject.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:BaseJsonObject <br/>
 * Function: Json base class. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class BaseJsonObject implements Serializable {
    private static final long serialVersionUID = 2262925857867024387L;
    /**
     * 身份令牌的状态，1代表可继续操作此页面，0表示身份状态发生变化，失去对页面的控制
     */

    private  String accessToken;
    private  String refreshToken;

    private Integer cDataCount;

    public Integer getDataCount() {
        return cDataCount;
    }

    public void setcDataCount(Integer cDataCount) {
        this.cDataCount = cDataCount;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    private Integer tokenStatus;
    private String tokenStatusInfo;
    private JsonReturnData jsonReturnData;
    public BaseJsonObject(JSONObject jsonObject) {
        setJsonValues(jsonObject);
    }
    public void setJsonValues(JSONObject jsonObj) {
        setTokenStatus(Utils.getIntegerFromJsonObjectWithKey(jsonObj, JsonKey.TOKEN_STATUS));
        setTokenStatusInfo(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.TOKEN_STATUS_INFO));



        if (jsonObj!=null && jsonObj.has("cdatacount"))
            setcDataCount(Utils.getIntegerFromJsonObjectWithKey(jsonObj, "cdatacount"));


        setRefreshToken(Utils.getStringFromJsonObjectWithKey(jsonObj, "refresh_token"));
        setAccessToken(Utils.getStringFromJsonObjectWithKey(jsonObj, "access_token"));

//        if (Utils.isStringNotNullOrEmpty(accessToken))
//            if (Utils.isStringNotNullOrEmpty(refreshToken))


//                AppUtils.saveToken(LoginActivity.this, "tttttttt");
//        AppUtils.saveRefreshToken(LoginActivity.this, "rrrrrrrr");

        JsonReturnData rd = new JsonReturnData();
        if (jsonObj != null) {
            try {
                if (jsonObj.has(JsonKey.COMMON_RETURN_DATA)) {
                    JSONObject joReturnData = jsonObj.getJSONObject(JsonKey.COMMON_RETURN_DATA);
                    rd.setReturnCode(Utils.getIntegerFromJsonObjectWithKey(joReturnData, JsonKey.COMMON_RETURN_CODE));
                    rd.setReturnInfo(Utils.getStringFromJsonObjectWithKey(joReturnData, JsonKey.COMMON_RETURN_INFO));

                    if (joReturnData.has(JsonKey.COMMON_ADD_ID)) {
                        rd.setAddId(Utils.getStringFromJsonObjectWithKey(joReturnData, JsonKey.COMMON_ADD_ID));
                    }
                    jsonReturnData = rd;
                }
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }


    public Integer getTokenStatus() {
        return tokenStatus;
    }

    private void setTokenStatus(Integer tokenStatus) {
        this.tokenStatus = tokenStatus;
    }

    public String getTokenStatusInfo() {
        return tokenStatusInfo;
    }

    private void setTokenStatusInfo(String tokenStatusInfo) {
        this.tokenStatusInfo = tokenStatusInfo;
    }

    public void setReturnCode(int returnCode) {
        if (jsonReturnData == null) {
            jsonReturnData = new JsonReturnData();
        }
        jsonReturnData.setReturnCode(returnCode);
    }

    public Integer getReturnCode() {
        return jsonReturnData == null ? 0 : jsonReturnData.getReturnCode();
    }

    public void setReturnInfo(String returnInfo) {
        if (jsonReturnData == null) {
            jsonReturnData = new JsonReturnData();
        }
        jsonReturnData.setReturnInfo(returnInfo);
    }

    public String getReturnInfo() {
        return jsonReturnData == null ? null : jsonReturnData.getReturnInfo();
    }

    public String getAddId() {
        return jsonReturnData.getAddId();
    }


}
