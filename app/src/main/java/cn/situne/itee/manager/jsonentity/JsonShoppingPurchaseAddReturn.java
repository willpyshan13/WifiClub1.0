package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * Created by luochao on 4/30/15.
 */
public class JsonShoppingPurchaseAddReturn extends BaseJsonObject implements Serializable {

    private ArrayList<JsonShoppingPaymentGet.DataItem> dataList;
    private String bookingNo;

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public ArrayList<JsonShoppingPaymentGet.DataItem> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<JsonShoppingPaymentGet.DataItem> dataList) {
        this.dataList = dataList;
    }

    public JsonShoppingPurchaseAddReturn(JSONObject jsonObject) {
        super(jsonObject);
    }


    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new ArrayList<JsonShoppingPaymentGet.DataItem>();
        try {
            JSONArray jsDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
            for (int i = 0; i < jsDataList.length(); i++) {
                JSONObject jsonObject = jsDataList.getJSONObject(i);
                JSONArray joProductList = Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_PRODUCT_LIST);
                JSONArray joPackageList = Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_PACKAGE_LIST);

                JsonShoppingPaymentGet.DataItem dataItem = new JsonShoppingPaymentGet.DataItem();


                dataItem.setPlayerName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_PLAYER));
                dataItem.setBookingNo(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_BOOKING_NO));

                setBookingNo(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_BOOKING_NO));
                //productList
                ArrayList<JsonShoppingPaymentGet.ProData> proDataList = new ArrayList<JsonShoppingPaymentGet.ProData>();
                for (int proIndex = 0; proIndex < joProductList.length(); proIndex++) {
                    JSONObject jsonProObject = joProductList.getJSONObject(proIndex);
                    JsonShoppingPaymentGet.ProData proData = new JsonShoppingPaymentGet.ProData();
                    proData.setId(Utils.getIntegerFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_ID));
                    proData.setProductId(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_PRODUCT_ID));
                    proData.setAttriId(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_ATTRI_ID));
                    proData.setQty(Utils.getIntegerFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_QTY));
                    proData.setPromoteId(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_PROMOTE_ID));
                    proData.setPrice(Utils.getDoubleFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_PRICE));
                    proData.setDisCountPrice(Utils.getDoubleFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_DISCOUNT_PRICE));
                    proData.setPayStatus(Utils.getIntegerFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_PAY_STATUS));
                    proData.setProductName(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_PRODUCT_NAME));
                    proData.setAttriName(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_ATTRI_NAME));
                    proDataList.add(proData);
                }
                dataItem.setProDataList(proDataList);

                //packageList
                ArrayList<JsonShoppingPaymentGet.PackageData> packageDataList = new ArrayList<JsonShoppingPaymentGet.PackageData>();
                for (int pacIndex = 0; pacIndex < joPackageList.length(); pacIndex++) {
                    JSONObject jsonPackageObject = joPackageList.getJSONObject(pacIndex);
                    JsonShoppingPaymentGet.PackageData packageData = new JsonShoppingPaymentGet.PackageData();
                    packageData.setPackageId(Utils.getStringFromJsonObjectWithKey(jsonPackageObject, JsonKey.SHOPPING_PACKAGE_ID));
                    packageData.setPackageName(Utils.getStringFromJsonObjectWithKey(jsonPackageObject, JsonKey.SHOPPING_PACKAGE_NAME));
                    packageData.setId(Utils.getIntegerFromJsonObjectWithKey(jsonPackageObject, JsonKey.SHOPPING_ID));
                    packageData.setPackageNumber(Utils.getIntegerFromJsonObjectWithKey(jsonPackageObject, JsonKey.SHOPPING_PACKAGE_NUMBER));
                    packageData.setPackagePrice(Utils.getDoubleFromJsonObjectWithKey(jsonPackageObject, JsonKey.SHOPPING_PACKAGE_PRICE));
                    packageData.setPayStatus(Utils.getIntegerFromJsonObjectWithKey(jsonPackageObject, JsonKey.SHOPPING_PAY_STATUS));

                    ArrayList<JsonShoppingPaymentGet.ProData> pProDataList = new ArrayList<JsonShoppingPaymentGet.ProData>();
                    if (jsonPackageObject.has(JsonKey.SHOPPING_PRODUCT_LIST)) {
                        JSONArray joPackageProductList = Utils.getArrayFromJsonObjectWithKey(jsonPackageObject, JsonKey.SHOPPING_PRODUCT_LIST);
                        for (int ppIndex = 0; ppIndex < joPackageProductList.length(); ppIndex++) {
                            JSONObject joProduct = joPackageProductList.getJSONObject(ppIndex);
                            JsonShoppingPaymentGet.ProData proData = new JsonShoppingPaymentGet.ProData();
                            proData.setQty(Utils.getIntegerFromJsonObjectWithKey(joProduct, JsonKey.SHOPPING_QTY));
                            proData.setDisCountPrice(Utils.getDoubleFromJsonObjectWithKey(joProduct, JsonKey.SHOPPING_DISCOUNT_PRICE));
                            proData.setProductName(Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.SHOPPING_PRODUCT_NAME));
                            proData.setAttriName(Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.SHOPPING_ATTRI_NAME));
                            pProDataList.add(proData);
                        }

                    }
                    packageData.setPackageList(pProDataList);
                    packageDataList.add(packageData);
                }
                dataItem.setPackageList(packageDataList);

                dataList.add(dataItem);


            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }


}
