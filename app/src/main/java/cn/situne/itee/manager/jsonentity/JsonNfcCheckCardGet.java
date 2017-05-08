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
 * Created by luochao on 9/12/15.
 */
public class JsonNfcCheckCardGet  extends BaseJsonObject implements Serializable {
    public JsonNfcCheckCardGet(JSONObject jsonObject) {
        super(jsonObject);
    }

    private String cardType;
    private String cardMessage;

    private String cardBindBookingNo;
    private String cardCode;

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardBindBookingNo() {
        return cardBindBookingNo;
    }

    public void setCardBindBookingNo(String cardBindBookingNo) {
        this.cardBindBookingNo = cardBindBookingNo;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardMessage() {
        return cardMessage;
    }

    public void setCardMessage(String cardMessage) {
        this.cardMessage = cardMessage;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.NFC_CARD);
            setCardType(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.NFC_CARD_TYPE));
            setCardMessage(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.NFC_CARD_MESSAGE));
            setCardCode(Utils.getStringFromJsonObjectWithKey(jsonObj,"cardCode"));
            setCardBindBookingNo(Utils.getStringFromJsonObjectWithKey(jsonObj.getJSONObject(JsonKey.NFC_RETURN_BOOKING), JsonKey.NFC_BKD_NO));
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

}
