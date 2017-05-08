package cn.situne.itee.manager.jsonentity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.situne.itee.common.utils.Utils;

/**
 * Created by luochao on 4/29/15.
 */
public class JsonShoppingReturnData extends BaseJsonObject {

    private String id;

    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JsonShoppingReturnData(JSONObject jsonObject) {
        super(jsonObject);
    }


    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        try {
            JSONObject dataList = jsonObj.getJSONObject("data_list");
            setId(Utils.getStringFromJsonObjectWithKey(dataList, "id"));

            setPrice(Utils.getDoubleFromJsonObjectWithKey(dataList, "price"));

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }
}
