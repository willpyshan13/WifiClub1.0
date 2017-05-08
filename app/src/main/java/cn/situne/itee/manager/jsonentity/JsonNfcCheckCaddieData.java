package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * Created by luochao on 9/13/15.
 */
public class JsonNfcCheckCaddieData  extends BaseJsonObject implements Serializable {
    public JsonNfcCheckCaddieData(JSONObject jsonObject) {
        super(jsonObject);
    }
    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        try {
            JSONArray joDataList = jsonObj.getJSONArray(JsonKey.NFC_RETURN_BOOKING);
            for (int i = 0;i<joDataList.length();i++){

            }

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }
}
