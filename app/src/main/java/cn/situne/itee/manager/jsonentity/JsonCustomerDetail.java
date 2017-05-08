package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * Created by luochao on 9/11/15.
 */
public class JsonCustomerDetail  extends BaseJsonObject {
    public JsonCustomerDetail(JSONObject jsonObject) {
        super(jsonObject);
    }
    private ArrayList<DataItem> dataList;

    public ArrayList<DataItem> getDataList() {
        return dataList;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {
            dataList = new ArrayList<>();

            try {
                JSONArray arrDataList = jsonObj.getJSONArray(JsonKey.COMMON_DATA_LIST);
                for (int i = 0; i < arrDataList.length(); i++) {
                    DataItem dataItem = new DataItem();
                    JSONObject joDataItem = arrDataList.getJSONObject(i);
                    dataItem.setDate(Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.CUSTOMER_DETAIL_DATE));
                    JSONArray jsArrayDateList = joDataItem.getJSONArray(JsonKey.CUSTOMER_DETAIL_DATE_LIST);
                    ArrayList<DataItemDetailed> dataItemDetailedList = new ArrayList<>();
                    for (int j = 0; j < jsArrayDateList.length(); j++) {
                        DataItemDetailed dataItemDetailed = new DataItemDetailed();
                        JSONObject jsonObject = jsArrayDateList.getJSONObject(j);
                        dataItemDetailed.setUsName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.CUSTOMER_DETAIL_US_NAME));
                        dataItemDetailed.setmTime(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.CUSTOMER_DETAIL_MTIME));
                        dataItemDetailed.setChangeAfter(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.CUSTOMER_DETAIL_CHANGE_AFTER));
                        dataItemDetailed.setChangeBefore(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.CUSTOMER_DETAIL_CHANGE_BEFORE));
                        dataItemDetailed.setMuser(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.CUSTOMER_DETAIL_MUSER));
                        dataItemDetailedList.add(dataItemDetailed);

                    }
                    dataItem.setDataItemDetailedList(dataItemDetailedList);
                    dataList.add(dataItem);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

       public static class DataItem implements Serializable {

            private String date;
            private boolean isGroup;
            private ArrayList<DataItemDetailed> DataItemDetailedList;

           public boolean isGroup() {
               return isGroup;
           }

           public void setIsGroup(boolean isGroup) {
               this.isGroup = isGroup;
           }

           public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public ArrayList<DataItemDetailed> getDataItemDetailedList() {
            return DataItemDetailedList;
        }

        public void setDataItemDetailedList(ArrayList<DataItemDetailed> dataItemDetailedList) {
            DataItemDetailedList = dataItemDetailedList;
        }
    }

    public static class DataItemDetailed implements Serializable {

        private String usName;
        private String changeBefore;
        private String changeAfter;
        private String mTime;
        private String muser;

        public String getUsName() {
            return usName;
        }

        public void setUsName(String usName) {
            this.usName = usName;
        }

        public String getChangeBefore() {
            return changeBefore;
        }

        public void setChangeBefore(String changeBefore) {
            this.changeBefore = changeBefore;
        }

        public String getChangeAfter() {
            return changeAfter;
        }

        public void setChangeAfter(String changeAfter) {
            this.changeAfter = changeAfter;
        }

        public String getmTime() {
            return mTime;
        }

        public void setmTime(String mTime) {
            this.mTime = mTime;
        }

        public String getMuser() {
            return muser;
        }

        public void setMuser(String muser) {
            this.muser = muser;
        }
    }

}

