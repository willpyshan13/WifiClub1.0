package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * Created by luochao on 10/5/15.
 */
public class JsonAgentsOpenTimeDetailedData  extends BaseJsonObject implements Serializable {
    public JsonAgentsOpenTimeDetailedData(JSONObject jsonObject) {
        super(jsonObject);
    }

    private OpenTimesDetailed openTimesDetailed;

    public OpenTimesDetailed getOpenTimesDetailed() {
        return openTimesDetailed;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        openTimesDetailed = new OpenTimesDetailed();
        try {


            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);


            openTimesDetailed.setDate( Utils.getStringFromJsonObjectWithKey(joDataList, "date"));

            JSONArray jsTimes = joDataList.getJSONArray("time");
            ArrayList<OpenTime> openTimeArrayList = new ArrayList<>();
            for (int i = 0;i<jsTimes.length();i++){
                OpenTime openTime = new OpenTime();
                JSONObject jsTime = jsTimes.getJSONObject(i);
                openTime.setEndTimes(Utils.getStringFromJsonObjectWithKey(jsTime, "end_time"));
                openTime.setStartTime(Utils.getStringFromJsonObjectWithKey(jsTime, "start_time"));
                openTimeArrayList.add(openTime);
            }
            openTimesDetailed.setOpenTimeList(openTimeArrayList);
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    public static class OpenTimesDetailed implements Serializable {

        private ArrayList<OpenTime> openTimeList;
        private String date;

        public ArrayList<OpenTime> getOpenTimeList() {
            return openTimeList;
        }

        public void setOpenTimeList(ArrayList<OpenTime> openTimeList) {
            this.openTimeList = openTimeList;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }


    public static class OpenTime implements Serializable {

        private String startTime;
        private String endTimes;

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTimes() {
            return endTimes;
        }

        public void setEndTimes(String endTimes) {
            this.endTimes = endTimes;
        }
    }
}
