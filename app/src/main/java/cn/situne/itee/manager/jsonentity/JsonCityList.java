/**
 * Project Name: itee
 * File Name:	 JsonCityList.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-04-29
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonCityList <br/>
 * Function: FIXME. <br/>
 * Date: 2015-04-29 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonCityList extends BaseJsonObject {

    private ArrayList<CityInfo> dataList;

    public JsonCityList(JSONObject jsonObject) {
        super(jsonObject);
    }


    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {
            dataList = new ArrayList<>();

            try {
                JSONArray arrDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
                for (int i = 0; i < arrDataList.length(); i++) {
                    JSONObject joCity = arrDataList.getJSONObject(i);
                    CityInfo cityInfo = new CityInfo();
                    cityInfo.country = Utils.getStringFromJsonObjectWithKey(joCity, JsonKey.CITY_LIST_COUNTRY);
                    cityInfo.cityName = Utils.getStringFromJsonObjectWithKey(joCity, JsonKey.CITY_LIST_CITY_NAME);
                    cityInfo.cityId = Utils.getStringFromJsonObjectWithKey(joCity, JsonKey.CITY_LIST_CITY_ID);
                    cityInfo.citySort = Utils.getStringFromJsonObjectWithKey(joCity, JsonKey.CITY_LIST_CITY_SORT);
                    cityInfo.zoneCode = Utils.getStringFromJsonObjectWithKey(joCity, JsonKey.CITY_LIST_ZONE_CODE);
                    dataList.add(cityInfo);
                }
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }

    public ArrayList<CityInfo> getDataList() {
        return dataList;
    }

    public static class CityInfo {
        private String country;
        private String cityName;
        private String cityId;
        private String citySort;
        private String zoneCode;

        public String getCountry() {
            return country;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCityId() {
            return cityId;
        }

        public String getCitySort() {
            return citySort;
        }

        public String getZoneCode() {
            return zoneCode;
        }
    }
}