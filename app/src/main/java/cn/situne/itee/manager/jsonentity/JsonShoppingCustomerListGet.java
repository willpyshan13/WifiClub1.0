package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * Created by luochao on 7/19/15.
 */
public class JsonShoppingCustomerListGet  extends BaseJsonObject implements Serializable {
    public JsonShoppingCustomerListGet(JSONObject jsonObject) {
        super(jsonObject);
    }

    private ArrayList<DataItem> dataList;

    public ArrayList<DataItem> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<DataItem> dataList) {
        this.dataList = dataList;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new ArrayList<DataItem>();
        try {
            JSONArray joDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
            for (int i = 0; i < joDataList.length(); i++) {
                JSONObject jsonObject = joDataList.getJSONObject(i);
                DataItem data = new DataItem();
               // data.setPdId(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_RENTAL_PRODUCT_PD_ID));

                data.setName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_NAME));
                data.setBkNo(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_BK_NO));
                data.setHolesArea(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_HOLES_AREA));

                data.setMemCard(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_FREQ_MEM_CARD));
                data.setDateTime(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_BKD_TIME));

                data.setBkNoAll(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_BK_NO_ALL));
                dataList.add(data);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }


    }

    public static class DataItem {
        private String name;
        private String bkdUser;
        private String memCard;
        private String bkNo;

        private String holesArea;
        private String dateTime;

        private String bkNoAll;

        public String getBkNoAll() {
            return bkNoAll;
        }

        public void setBkNoAll(String bkNoAll) {
            this.bkNoAll = bkNoAll;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getHolesArea() {
            return holesArea;
        }

        public void setHolesArea(String holesArea) {
            this.holesArea = holesArea;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBkdUser() {
            return bkdUser;
        }

        public void setBkdUser(String bkdUser) {
            this.bkdUser = bkdUser;
        }

        public String getMemCard() {
            return memCard;
        }

        public void setMemCard(String memCard) {
            this.memCard = memCard;
        }

        public String getBkNo() {
            return bkNo;
        }

        public void setBkNo(String bkNo) {
            this.bkNo = bkNo;
        }
    }
}
