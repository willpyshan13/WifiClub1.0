package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;


public class JsonChangeTransferArea extends BaseJsonObject implements Serializable {

    private static final long serialVersionUID = -1529846374946979809L;

    public JsonChangeTransferArea(JSONObject jsonObject) {
        super(jsonObject);
    }

    private DataList dataList;

    public DataList getDataList() {
        return dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }


    public static class DataList {

        public DefaultItem defaultItem;
        public List<TransferTimeItem> bookingList;

    }

    public static class TransferTimeItem {


        public String areaId;
        public String areaType;
        public List<String> timeList;
        public List<String> transferAreaIdList;
        public List<String> transferAreaNameList;
        public List<String> transferTimeList;


    }

    public static class DefaultItem {

        public String areaId;
        public String areaType;
        public String time;
        public int holeOnly;

    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new DataList();

        try {
            JSONObject arrDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);

            JSONObject arrDefault = arrDataList.getJSONObject(JsonKey.TEE_TIME_CUSTOMERS_BOOKING_DEFAULT);
            dataList.defaultItem = new DefaultItem();
            dataList.defaultItem.areaId = Utils.getStringFromJsonObjectWithKey(arrDefault, JsonKey.TEE_TIME_CUSTOMERS_BOOKING_AREA_ID);
            dataList.defaultItem.areaType = Utils.getStringFromJsonObjectWithKey(arrDefault, JsonKey.TEE_TIME_CUSTOMERS_BOOKING_AREA_TYPE);
            dataList.defaultItem.time = Utils.getStringFromJsonObjectWithKey(arrDefault, JsonKey.TEE_TIME_BOOKING_GOODS_LIST_TIME);
            dataList.defaultItem.holeOnly = Utils.getIntegerFromJsonObjectWithKey(arrDefault, JsonKey.TEE_TIME_BOOKING_GOODS_LIST_HOLE_ONLY);
            dataList.bookingList = new ArrayList<>();
            JSONArray arrBookingList = Utils.getArrayFromJsonObjectWithKey(arrDataList, JsonKey.TEE_TIME_BOOKING_DETAIL_BOOKING_LIST);
            for (int i = 0; i < arrBookingList.length(); i++) {
                JSONObject joBookingListItem = arrBookingList.getJSONObject(i);
                TransferTimeItem bli = new TransferTimeItem();
                bli.areaType = Utils.getStringFromJsonObjectWithKey(joBookingListItem, JsonKey.TEE_TIME_CUSTOMERS_BOOKING_AREA_TYPE);
                bli.areaId = Utils.getStringFromJsonObjectWithKey(joBookingListItem, JsonKey.TEE_TIME_CUSTOMERS_BOOKING_AREA_ID);
                bli.timeList = new ArrayList<>();
                bli.transferAreaIdList = new ArrayList<>();
                bli.transferAreaNameList = new ArrayList<>();
                bli.transferTimeList = new ArrayList<>();


                JSONArray arrTimeList = Utils.getArrayFromJsonObjectWithKey(joBookingListItem, JsonKey.TEE_TIME_BOOKING_GOODS_LIST_TIME_LIST);
                for (int j = 0; j < arrTimeList.length(); j++) {
                    JSONObject timeListItem = arrTimeList.getJSONObject(j);
                    String time = Utils.getStringFromJsonObjectWithKey(timeListItem, JsonKey.TEE_TIME_BOOKING_GOODS_LIST_TIME);


                    String startTime = Utils.getStringFromJsonObjectWithKey(timeListItem, "start_time");
                    String areaId = Utils.getStringFromJsonObjectWithKey(timeListItem, "area_id");
                    String areaName = Utils.getStringFromJsonObjectWithKey(timeListItem, "area_name");


                    bli.transferAreaIdList.add(areaId);
                    bli.transferAreaNameList.add(areaName);
                    bli.transferTimeList.add(startTime);

                    bli.timeList.add(time);

                }
                dataList.bookingList.add(bli);
            }

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

}
