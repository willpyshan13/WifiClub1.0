package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * Created by luochao on 9/13/15.
 */
public class JsonCaddieListData extends BaseJsonObject {
    public JsonCaddieListData(JSONObject jsonObject) {
        super(jsonObject);
    }

    private ArrayList<String> dataList;

    public ArrayList<String> getDataList() {
        return dataList;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {
            dataList = new ArrayList<>();
            try {
                JSONArray arrDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
                for (int i = 0; i < arrDataList.length(); i++) {
                    JSONObject joCode = arrDataList.getJSONObject(i);
                    dataList.add(Utils.getStringFromJsonObjectWithKey(joCode, JsonKey.NFC_NC_CODE));

                }
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }
}
