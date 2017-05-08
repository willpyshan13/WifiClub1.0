package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * Created by luochao on 9/17/15.
 */
public class JsonTeeTimePriceTableData extends BaseJsonObject implements Serializable {
    public JsonTeeTimePriceTableData(JSONObject jsonObject) {
        super(jsonObject);
    }


    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        //  dataList = new DataList();

        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
//            dataList.courseId = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_COURSE_ID);
//            dataList.startDate = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_START_DATE);
//            dataList.endDate = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_END_DATE);
//            dataList.sunrise = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_SUNRISE);
//            dataList.sunset = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_SUNSET);
//            dataList.longitude = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_LONGITUDE);
//            dataList.timeZone = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_TIME_ZONE);
//            dataList.latitude = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_LATITUDE);
//
//            dataList.firstTeeTime = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_FIRST_TEE_TIME);
//            dataList.lastTeeTime = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_LAST_TEE_TIME);
//            dataList.gapTime = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_GAP_TIME);
//
//            dataList.transferTimeList = new ArrayList<>();
//
//            JSONArray arrayTransferTime = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_EDIT_SETTING_TRANSFER_TIME);
//            for (int i = 0; i < arrayTransferTime.length(); i++) {
//                JSONObject joTransferTime = arrayTransferTime.getJSONObject(i);
//                DataList.TransferTime transferTime = new DataList.TransferTime();
//                transferTime.id = Utils.getIntegerFromJsonObjectWithKey(joTransferTime, JsonKey.TEE_TIME_EDIT_SETTING_TRANSFER_TIME_ID);
//                transferTime.name = Utils.getStringFromJsonObjectWithKey(joTransferTime, JsonKey.TEE_TIME_EDIT_SETTING_TRANSFER_TIME_NAME);
//                transferTime.time = Utils.getStringFromJsonObjectWithKey(joTransferTime, JsonKey.TEE_TIME_EDIT_SETTING_TRANSFER_TIME_TIME);
//                dataList.transferTimeList.add(transferTime);
//            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }
    public static class CustomerPriceTable implements Serializable {


        private String customerName;
        private String customerTypeStr;
        private String customerNum;
        private String customerId;
        private String customerBookingNo;
        private ArrayList<PriceTable> customerPriceTableList;

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerTypeStr() {
            return customerTypeStr;
        }

        public void setCustomerTypeStr(String customerTypeStr) {
            this.customerTypeStr = customerTypeStr;
        }

        public String getCustomerNum() {
            return customerNum;
        }

        public void setCustomerNum(String customerNum) {
            this.customerNum = customerNum;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getCustomerBookingNo() {
            return customerBookingNo;
        }

        public void setCustomerBookingNo(String customerBookingNo) {
            this.customerBookingNo = customerBookingNo;
        }

        public ArrayList<PriceTable> getCustomerPriceTableList() {
            return customerPriceTableList;
        }

        public void setCustomerPriceTableList(ArrayList<PriceTable> customerPriceTableList) {
            this.customerPriceTableList = customerPriceTableList;
        }
    }
    public static class PriceTable implements Serializable {


    }


}
