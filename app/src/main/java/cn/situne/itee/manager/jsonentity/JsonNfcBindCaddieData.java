package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * Created by luochao on 9/15/15.
 */
public class JsonNfcBindCaddieData   extends BaseJsonObject implements Serializable {
    public JsonNfcBindCaddieData(JSONObject jsonObject) {
        super(jsonObject);
    }

    private String caddieInfo;

    public String getCaddieInfo() {
        return caddieInfo;
    }

    public void setCaddieInfo(String caddieInfo) {
        this.caddieInfo = caddieInfo;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            setCaddieInfo(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.NFC_CADDIE_INFO));

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }
}
