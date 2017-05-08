package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * Created by luochao on 11/19/15.
 */
public class JsonGetGuestTypeList extends BaseJsonObject implements Serializable {


    private List<GuestTypeItem> dataList;

    public List<GuestTypeItem> getDataList() {
        return dataList;
    }

    public void setDataList(List<GuestTypeItem> dataList) {
        this.dataList = dataList;
    }

    public JsonGetGuestTypeList(JSONObject jsonObject) {
        super(jsonObject);
    }


    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        try {
            JSONArray joDataList = jsonObj.getJSONArray(JsonKey.COMMON_DATA_LIST);
            dataList = new ArrayList<>();
            for (int i = 0; i < joDataList.length(); i++) {
                JSONObject jsonObject = joDataList.getJSONObject(i);
                GuestTypeItem item = new GuestTypeItem();

                item.setId(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.COMMON_ID));
                item.setName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.COMMON_NAME));
                dataList.add(item);
            }

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }


    }


   public class GuestTypeItem implements Serializable {

        private String id;
        private String name;
       private boolean checked;

       public boolean isChecked() {
           return checked;
       }

       public void setChecked(boolean checked) {
           this.checked = checked;
       }

       public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
