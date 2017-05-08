/**
 * Project Name: itee
 * File Name:  JsonAgentListGet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonAgentListGet <br/>
 * Function: Get Json agentsList list information. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class JsonDateSetting extends BaseJsonObject implements Serializable {


    public DateSetting getDataList() {
        return dataList;
    }

    public void setDataList(DateSetting dataList) {
        this.dataList = dataList;
    }

    private DateSetting dataList;


    public static class DateSetting implements Serializable {


        private String Sun;
        private String Mon;
        private String Tue;
        private String Wed;
        private String Thu;
        private String Fri;
        private String Sat;
        private String year;

        public String getSun() {
            return Sun;
        }

        public void setSun(String sun) {
            Sun = sun;
        }

        public String getMon() {
            return Mon;
        }

        public void setMon(String mon) {
            Mon = mon;
        }

        public String getTue() {
            return Tue;
        }

        public void setTue(String tue) {
            Tue = tue;
        }

        public String getWed() {
            return Wed;
        }

        public void setWed(String wed) {
            Wed = wed;
        }

        public String getThu() {
            return Thu;
        }

        public void setThu(String thu) {
            Thu = thu;
        }

        public String getFri() {
            return Fri;
        }

        public void setFri(String fri) {
            Fri = fri;
        }

        public String getSat() {
            return Sat;
        }

        public void setSat(String sat) {
            Sat = sat;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }
    }

    public JsonDateSetting(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        dataList = new DateSetting();
        try {
            JSONObject jsonObject = jsonObj.getJSONObject("data_list");
            if (jsonObject != null) {
                dataList.setSun(Utils.getStringFromJsonObjectWithKey(jsonObject, "SUN"));
                dataList.setMon(Utils.getStringFromJsonObjectWithKey(jsonObject, "MON"));
                dataList.setTue(Utils.getStringFromJsonObjectWithKey(jsonObject, "TUE"));
                dataList.setWed(Utils.getStringFromJsonObjectWithKey(jsonObject, "WED"));
                dataList.setThu(Utils.getStringFromJsonObjectWithKey(jsonObject, "THU"));
                dataList.setFri(Utils.getStringFromJsonObjectWithKey(jsonObject, "FRI"));
                dataList.setSat(Utils.getStringFromJsonObjectWithKey(jsonObject, "SAT"));
                dataList.setYear(Utils.getStringFromJsonObjectWithKey(jsonObject, "year"));
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

}
