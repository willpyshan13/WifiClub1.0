/**
 * Project Name: itee
 * File Name:	 JsonCheckOut.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-05-15
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonCheckOut <br/>
 * Function: FIXME. <br/>
 * Date: 2015-05-15 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonCheckOut extends BaseJsonObject {

    private String checkStatus;

    public String getCheckStatus() {
        return checkStatus;
    }

    public JsonCheckOut(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {
            try {
                JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
                checkStatus = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_CHECK_OUT_CHECK_STATUS);
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }
}