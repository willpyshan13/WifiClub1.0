/**
 * Project Name: itee
 * File Name:  JsonPayPostRet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-04-23
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
 * ClassName:JsonPayPostRet <br/>
 * Date: 2015-04-23 <br/>
 *
 * @author Administrator
 * @version 1.0
 * @see
 */
public class JsonPayPostRet extends BaseJsonObject implements Serializable {

    public JsonPayPostRet(JSONObject jsonObject) {
        super(jsonObject);
    }

    private DataList dataList;

    public static class DataList implements Serializable {
        public String getPayId() {
            return payId;
        }

        public void setPayId(String payId) {
            this.payId = payId;
        }

        public String payId;

    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new DataList();
        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            dataList.payId = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PURCHASE_HISTORY_PAY_ID);
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
