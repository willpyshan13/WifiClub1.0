/**
 * Project Name: itee
 * File Name:  JsonAgentListGet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.manager.jsonentity;

import com.squareup.timessquare.CustomJsonAdvancedSetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.administration.AdvancedSettingFragment;

/**
 * ClassName:JsonAgentListGet <br/>
 * Function: Get Json agentsList list information. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class JsonAdvancedSetting extends BaseJsonObject implements Serializable {



    private List<HashMap<String, DaySetting>> dataList;

    public List<HashMap<String, DaySetting>> getDataList() {
        return dataList;
    }

    public void setDataList(List<HashMap<String, DaySetting>> dataList) {
        this.dataList = dataList;
    }
    public static class CdTypeArrItem implements Serializable {
        private String id;
        private String firstName;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class DaySetting implements Serializable {

        private String cdDate;
        private String cdId;
        private String cdType;
        //1:holiday 2:weekday
        private String cdTypeFlag;

        private List<CdTypeArrItem> cdTypeArr;
        private List<CdTypeArrItem> cdTypeFlagArr;

        public String getCdDate() {
            return cdDate;
        }

        public void setCdDate(String cdDate) {
            this.cdDate = cdDate;
        }

        public String getCdId() {
            return cdId;
        }

        public void setCdId(String cdId) {
            this.cdId = cdId;
        }

        public String getCdType() {
            return cdType;
        }

        public void setCdType(String cdType) {
            this.cdType = cdType;
        }

        public String getCdTypeFlag() {
            return cdTypeFlag;
        }

        public void setCdTypeFlag(String cdTypeFlag) {
            this.cdTypeFlag = cdTypeFlag;
        }

        public List<CdTypeArrItem> getCdTypeArr() {
            return cdTypeArr;
        }

        public void setCdTypeArr(List<CdTypeArrItem> cdTypeArr) {
            this.cdTypeArr = cdTypeArr;
        }

        public List<CdTypeArrItem> getCdTypeFlagArr() {
            return cdTypeFlagArr;
        }

        public void setCdTypeFlagArr(List<CdTypeArrItem> cdTypeFlagArr) {
            this.cdTypeFlagArr = cdTypeFlagArr;
        }
    }


    public JsonAdvancedSetting(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        dataList = new ArrayList<>();
        try {
            JSONObject jsonObjectAll = jsonObj.getJSONObject("data_list");
            if (jsonObjectAll != null) {
                String month = null;
                for (int i = 1; i < 13; i++) {
                    if (i < 10) {
                        month = "0" + i;
                    } else {
                        month = i + "";
                    }
                    JSONObject jsonObject = jsonObjectAll.getJSONObject(month);
                    HashMap<String, DaySetting> map = new HashMap<>();
                    String day = null;
                    for (int k = 1; k < 32; k++) {

                        if (k < 10) {
                            day = "0" + k ;
                        } else {
                            day = k + "";
                        }

                        if (jsonObject.has(day)) {
                            DaySetting dateSetting = new DaySetting();

                            JSONObject jsonDay = jsonObject.getJSONObject(day);

                            dateSetting.setCdDate(Utils.getStringFromJsonObjectWithKey(jsonDay, JsonKey.ADMINISTRATION_ADVANCED_SETTING_CD_DATE));
                            dateSetting.setCdId(Utils.getStringFromJsonObjectWithKey(jsonDay, JsonKey.ADMINISTRATION_ADVANCED_SETTING_CD_ID));
                            dateSetting.setCdType(Utils.getStringFromJsonObjectWithKey(jsonDay, JsonKey.ADMINISTRATION_ADVANCED_SETTING_CD_TYPE));
                            dateSetting.setCdTypeFlag(Utils.getStringFromJsonObjectWithKey(jsonDay, JsonKey.ADMINISTRATION_ADVANCED_SETTING_CD_TYPE_FLAG));

                            JSONArray jsonArray2 = jsonDay.getJSONArray(JsonKey.ADMINISTRATION_ADVANCED_SETTING_CD_TYPE_ARR);
                            JSONArray jsonArray3 = jsonDay.getJSONArray(JsonKey.ADMINISTRATION_ADVANCED_SETTING_CD_TYPE_FLAG_ARR);
                            List<CdTypeArrItem> listCdTypeArr = new ArrayList<>();
                            List<CdTypeArrItem> listCdTypeFlagArr = new ArrayList<>();
                            for (int z = 0; z < jsonArray2.length(); z++) {
                                CdTypeArrItem cdTypeArrItem = new CdTypeArrItem();
                                JSONObject jsonObject2 = jsonArray2.getJSONObject(z);

                                cdTypeArrItem.setFirstName(Utils.getStringFromJsonObjectWithKey(jsonObject2, JsonKey.ADMINISTRATION_ADVANCED_SETTING_FIRST_NAME));
                                cdTypeArrItem.setId(Utils.getStringFromJsonObjectWithKey(jsonObject2, JsonKey.ADMINISTRATION_ADVANCED_SETTING_ID));
                                cdTypeArrItem.setName(Utils.getStringFromJsonObjectWithKey(jsonObject2, JsonKey.ADMINISTRATION_ADVANCED_SETTING_NAME));
                                listCdTypeArr.add(cdTypeArrItem);
                            }
                            for (int x = 0; x < jsonArray3.length(); x++) {
                                CdTypeArrItem cdTypeArrItem = new CdTypeArrItem();
                                JSONObject jsonObject2 = jsonArray3.getJSONObject(x);

                                cdTypeArrItem.setFirstName(Utils.getStringFromJsonObjectWithKey(jsonObject2, JsonKey.ADMINISTRATION_ADVANCED_SETTING_FIRST_NAME));
                                cdTypeArrItem.setId(Utils.getStringFromJsonObjectWithKey(jsonObject2, JsonKey.ADMINISTRATION_ADVANCED_SETTING_ID));
                                cdTypeArrItem.setName(Utils.getStringFromJsonObjectWithKey(jsonObject2, JsonKey.ADMINISTRATION_ADVANCED_SETTING_NAME));
                                listCdTypeFlagArr.add(cdTypeArrItem);
                            }

                            dateSetting.setCdTypeArr(listCdTypeArr);
                            dateSetting.setCdTypeFlagArr(listCdTypeFlagArr);
                            map.put(day, dateSetting);

                        }

                    }
                    dataList.add(map);
                }


            }
        } catch (
                JSONException e
                )

        {
            Utils.log(e.getMessage());
        }
    }

}
