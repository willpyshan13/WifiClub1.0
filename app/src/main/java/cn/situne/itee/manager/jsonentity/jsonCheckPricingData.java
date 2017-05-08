package cn.situne.itee.manager.jsonentity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * Created by luochao on 9/24/15.
 */
public class jsonCheckPricingData extends BaseJsonObject {
    private String pricingDisplay;

    public String getPricingDisplay() {
        return pricingDisplay;
    }

    public void setPricingDisplay(String pricingDisplay) {
        this.pricingDisplay = pricingDisplay;
    }

    public jsonCheckPricingData(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {
            try {
                JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);

                setPricingDisplay( Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PRICING_TABLE_PRICING_DISPLAY));
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }
}
