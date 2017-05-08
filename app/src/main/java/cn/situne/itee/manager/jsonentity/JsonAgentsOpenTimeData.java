package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * Created by luochao on 9/30/15.
 */
public class JsonAgentsOpenTimeData extends BaseJsonObject implements Serializable {
    public JsonAgentsOpenTimeData(JSONObject jsonObject) {
        super(jsonObject);
    }

    private ArrayList<OpenTimesData> dataList;

    public ArrayList<OpenTimesData> getDataList() {
        return dataList;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new ArrayList<>();
        try {


            JSONArray joDataList = jsonObj.getJSONArray(JsonKey.COMMON_DATA_LIST);


            for (int i = 0;i<joDataList.length();i++){
                OpenTimesData openTimesData = new OpenTimesData();

                JSONObject jsonObject = joDataList.getJSONObject(i);
                openTimesData.setAbmId(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.AGENT_ABM_ID));
                openTimesData.setAbmDataDesc( Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.AGENT_ABM_DATE_DECS));
                openTimesData.setAbmTimeDecs( Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.AGENT_ABM_TIME_DECS));



                dataList.add(openTimesData);

            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    public static class OpenTimesData implements Serializable {

        private String abmId;
        private String abmTimeDecs;
        private String abmDataDesc;

        public String getAbmId() {
            return abmId;
        }

        public void setAbmId(String abmId) {
            this.abmId = abmId;
        }

        public String getAbmTimeDecs() {
            return abmTimeDecs;
        }

        public void setAbmTimeDecs(String abmTimeDecs) {
            this.abmTimeDecs = abmTimeDecs;
        }

        public String getAbmDataDesc() {
            return abmDataDesc;
        }

        public void setAbmDataDesc(String abmDataDesc) {
            this.abmDataDesc = abmDataDesc;
        }
    }
}
