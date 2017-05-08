/**
 * Project Name: itee
 * File Name:	 JsonDateSun.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-07-28
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonDateSun <br/>
 * Function: entity of getDateSun <br/>
 * Date: 2015-07-28 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonDateSun extends BaseJsonObject {
    private String sunrise;
    private String sunset;
    private String transferTime;

    private String reservedDate;

    public String getReservedDate() {
        return reservedDate;
    }

    public void setReservedDate(String reservedDate) {
        this.reservedDate = reservedDate;
    }

    public String getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(String transferTime) {
        this.transferTime = transferTime;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public JsonDateSun(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {
            try {
                JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);

                setSunrise(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.SUN_TIME_RISE));
                setSunset(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.SUN_TIME_SET));
                setTransferTime(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_TRANSFER_TIME));
                setReservedDate(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_RESERVED_FLAG));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}