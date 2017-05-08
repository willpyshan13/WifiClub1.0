package cn.situne.itee.manager.jsonentity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

public class JsonConfirmPayment extends BaseJsonObject implements Serializable {

    private DataList dataList;

    public JsonConfirmPayment(JSONObject jsonObject) {
        super(jsonObject);
    }

    public DataList getDataList() {
        return dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }


    public class DataList implements Serializable {

        private String endDate;

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }
    }


    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        dataList = new DataList();
        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            dataList.setEndDate(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_MEMBER_SHIP_CONFIRM_PAYMENTEND_DATE));

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }
}
