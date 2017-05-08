package cn.situne.itee.manager.jsonentity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * Created by xuyue on 2015/12/09.
 */
public class JsonCalculate extends BaseJsonObject implements Serializable {

    public JsonCalculate(JSONObject jsonObject) {
        super(jsonObject);
    }

    private DataList dataList;

    public static class DataList implements Serializable {

        public String calculate;

        public String getCalculate(){
            return calculate;
        }

        public void setCalculate(String calculate){
            this.calculate = calculate;
        }
    }


    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new DataList();

        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            dataList.calculate = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.EDIT_COURSE_CALCULATE);
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
