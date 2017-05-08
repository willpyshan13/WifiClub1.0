/**
 * Project Name: itee
 * File Name:  JsonTransferTime.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-19
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
 * ClassName:JsonTransferTime <br/>
 * Function: To set transfer time. <br/>
 * Date: 2015-03-19 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonTransferTime extends BaseJsonObject implements Serializable {

    public JsonTransferTime(JSONObject jsonObject) {
        super(jsonObject);
    }

    private DataList dataList;

    public static class DataList implements Serializable {

        public String transferTime;

        public String getTransferTime() {
            return transferTime;
        }

        public void setTransferTime(String transferTime) {
            this.transferTime = transferTime;
        }
    }


    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new DataList();

        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            dataList.transferTime = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.EDIT_COURSE_HOLES);
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
