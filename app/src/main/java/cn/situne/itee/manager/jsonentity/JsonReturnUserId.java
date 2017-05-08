/**
 * Project Name: itee
 * File Name:  JsonReturnUserId.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-04-29
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
 * ClassName:JsonReturnUserId <br/>
 * Function: FIXME. <br/>
 * Date: 2015-04-29 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class JsonReturnUserId extends BaseJsonObject implements Serializable {


    private User dataList;


    public JsonReturnUserId(JSONObject jsonObject) {
        super(jsonObject);
    }

    public User getDataList() {
        return dataList;
    }

    public void setDataList(User dataList) {
        this.dataList = dataList;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        try {
            JSONObject jsonObject = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            dataList = new User();
            dataList.setUserId(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.STAFF_DEPARTMENT_USER_ID));
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }

    public class User implements Serializable {
        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        private String userId;


    }
}
