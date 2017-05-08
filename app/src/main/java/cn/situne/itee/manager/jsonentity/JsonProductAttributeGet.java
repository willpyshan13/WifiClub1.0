/**
 * Project Name: itee
 * File Name:	 JsonProductAttribute.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-03-25
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonProductAttribute <br/>
 * Function: entity of api productAttribute. <br/>
 * Date: 2015-03-25 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonProductAttributeGet extends BaseJsonObject implements Serializable {
    public JsonProductAttributeGet(JSONObject jsonObject) {
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
                    JSONObject jo = arrDataList.getJSONObject(i);
                    JSONObject joFirstLevel = jo.getJSONObject(JsonKey.COMMON_FIRST_LEVEL);
                    FirstLevel fl = new FirstLevel();
                    fl.setAttributeId(Utils.getIntegerFromJsonObjectWithKey(joFirstLevel, JsonKey.COMMON_ATTRIBUTE_ID));
                    fl.setAttributeName(Utils.getStringFromJsonObjectWithKey(joFirstLevel, JsonKey.COMMON_ATTRIBUTE_NAME));
                    fl.setAttributeCheck(Constants.STR_1.equals(Utils.getStringFromJsonObjectWithKey(joFirstLevel, JsonKey.COMMON_ATTRIBUTE_CHECK)));
                    try {
                        JSONArray arrSecondLevel = Utils.getArrayFromJsonObjectWithKey(joFirstLevel, JsonKey.COMMON_SECOND_LEVEL);
                        for (int j = 0; j < arrSecondLevel.length(); j++) {
                            JSONObject joSecondLevel = arrSecondLevel.getJSONObject(j);
                            FirstLevel.SecondLevel sl = new FirstLevel.SecondLevel();
                            sl.setAttributeId(Utils.getIntegerFromJsonObjectWithKey(joSecondLevel, JsonKey.COMMON_ATTRIBUTE_ID));
                            sl.setAttributeName(Utils.getStringFromJsonObjectWithKey(joSecondLevel, JsonKey.COMMON_ATTRIBUTE_NAME));
                            sl.setAttributeCheck(Constants.STR_1.equals(Utils.getStringFromJsonObjectWithKey(joSecondLevel, JsonKey.COMMON_ATTRIBUTE_CHECK)));
                            fl.secondLevels.add(sl);
                        }
                    } catch (JSONException e) {
                        Utils.log(e.getMessage());
                    }
                    dataList.add(fl);
                }
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }

    private ArrayList<FirstLevel> dataList;

    public ArrayList<FirstLevel> getDataList() {
        return dataList;
    }

    public static class FirstLevel implements Serializable {

        public FirstLevel() {
            secondLevels = new ArrayList<>();
        }

        private Integer attributeId;
        private String attributeName;
        private boolean attributeCheck;
        private ArrayList<SecondLevel> secondLevels;

        public ArrayList<SecondLevel> getSecondLevels() {
            return secondLevels;
        }

        public Integer getAttributeId() {
            return attributeId;
        }

        public void setAttributeId(Integer attributeId) {
            this.attributeId = attributeId;
        }

        public String getAttributeName() {
            return attributeName;
        }

        public void setAttributeName(String attributeName) {
            this.attributeName = attributeName;
        }

        public boolean isAttributeCheck() {
            return attributeCheck;
        }

        public void setAttributeCheck(boolean attributeCheck) {
            this.attributeCheck = attributeCheck;
        }

        public static class SecondLevel implements Serializable {
            private Integer attributeId;
            private String attributeName;
            private boolean attributeCheck;

            public Integer getAttributeId() {
                return attributeId;
            }

            public void setAttributeId(Integer attributeId) {
                this.attributeId = attributeId;
            }

            public String getAttributeName() {
                return attributeName;
            }

            public void setAttributeName(String attributeName) {
                this.attributeName = attributeName;
            }

            public boolean isAttributeCheck() {
                return attributeCheck;
            }

            public void setAttributeCheck(boolean attributeCheck) {
                this.attributeCheck = attributeCheck;
            }
        }
    }
}