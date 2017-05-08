package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * Created by luochao on 4/15/15.
 */
public class JsonShoppingPaymentGet extends BaseJsonObject implements Serializable {
    private ArrayList<DataItem> dataList;
    private String bookingNo;

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public ArrayList<DataItem> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<DataItem> dataList) {
        this.dataList = dataList;
    }

    public JsonShoppingPaymentGet(JSONObject jsonObject) {
        super(jsonObject);
    }


    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new ArrayList<DataItem>();
        try {
            JSONArray joDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
            // setBookingNo(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.SHOPPING_BOOKING_NO));


            for (int i = 0; i < joDataList.length(); i++) {
                JSONObject jsonObject = joDataList.getJSONObject(i);
                JSONArray joAaList = Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_AA_LIST);
                JSONArray joProductList = Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_PRODUCT_LIST);
                JSONArray joPackageList = Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_PACKAGE_LIST);


                JSONArray joPricingList = Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_PRICING_LIST);

                DataItem dataItem = new DataItem();

                dataItem.setPlayerName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_PLAYER));
                dataItem.setBookingNo(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_BOOKING_NO));
                dataItem.setCheckStatus(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_CHECK_STATUS));

                dataItem.setPricingBdpId(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_PRICING_BDP_ID));
                dataItem.setPricingTimes(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_PRICING_TIMES));

                dataItem.setPricingPrice(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_PRICING_PRICE));

                dataItem.setPricingPayStatus(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_PRICING_PAY_STATUS));


                //productList
                ArrayList<ProData> proDataList = new ArrayList<ProData>();
                for (int proIndex = 0; proIndex < joProductList.length(); proIndex++) {
                    JSONObject jsonProObject = joProductList.getJSONObject(proIndex);
                    ProData proData = new ProData();
                    proData.setId(Utils.getIntegerFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_ID));
                    proData.setProductId(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_PRODUCT_ID));
                    proData.setAttriId(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_ATTRI_ID));
                    proData.setQty(Utils.getIntegerFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_QTY));
                    proData.setPrice(Utils.getDoubleFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_PRICE));
                    proData.setDisCountPrice(Utils.getDoubleFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_DISCOUNT_PRICE));
                    proData.setPayStatus(Utils.getIntegerFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_PAY_STATUS));
                    proData.setPromoteId(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_PROMOTE_ID));
                    proData.setProductName(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_PRODUCT_NAME));
                    proData.setAttriName(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_ATTRI_NAME));
                    proData.setPdSort(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_PD_SORT));
                    proDataList.add(proData);
                }
                dataItem.setProDataList(proDataList);

                //packageList
                ArrayList<PackageData> packageDataList = new ArrayList<PackageData>();

                for (int pacIndex = 0; pacIndex < joPackageList.length(); pacIndex++) {
                    JSONObject jsonPackageObject = joPackageList.getJSONObject(pacIndex);
                    PackageData packageData = new PackageData();
                    packageData.setPackageId(Utils.getStringFromJsonObjectWithKey(jsonPackageObject, JsonKey.SHOPPING_PACKAGE_ID));
                    packageData.setPackageName(Utils.getStringFromJsonObjectWithKey(jsonPackageObject, JsonKey.SHOPPING_PACKAGE_NAME));
                    packageData.setId(Utils.getIntegerFromJsonObjectWithKey(jsonPackageObject, JsonKey.SHOPPING_ID));
                    packageData.setPackageNumber(Utils.getIntegerFromJsonObjectWithKey(jsonPackageObject, JsonKey.SHOPPING_PACKAGE_NUMBER));
                    packageData.setPackagePrice(Utils.getDoubleFromJsonObjectWithKey(jsonPackageObject, JsonKey.SHOPPING_PACKAGE_PRICE));
                    packageData.setPayStatus(Utils.getIntegerFromJsonObjectWithKey(jsonPackageObject, JsonKey.SHOPPING_PAY_STATUS));

                    ArrayList<ProData> pProDataList = new ArrayList<ProData>();
                    if (jsonPackageObject.has(JsonKey.SHOPPING_PRODUCT_LIST)) {
                        JSONArray joPackageProductList = Utils.getArrayFromJsonObjectWithKey(jsonPackageObject, JsonKey.SHOPPING_PRODUCT_LIST);
                        for (int ppIndex = 0; ppIndex < joPackageProductList.length(); ppIndex++) {
                            JSONObject joProduct = joPackageProductList.getJSONObject(ppIndex);
                            ProData proData = new ProData();
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

                //pricing List
                ArrayList<PricingData> pricingDataList = new ArrayList<>();
                for (int pp = 0; pp < joPricingList.length(); pp++) {
                    JSONObject jsonPricingObject = joPricingList.getJSONObject(pp);
                    PricingData pricingData = new PricingData();

                    pricingData.setId(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.SHOPPING_ID));
                    pricingData.setOpBdpId(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, "op_bdp_id"));
                    if (jsonPricingObject.has(JsonKey.SHOPPING_PACKAGE_ID)){
                        String packageId = Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.SHOPPING_PACKAGE_ID);
                        pricingData.setPackageId(packageId);


                        pricingData.setQty(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.SHOPPING_PRODUCT_BY_CODE_QTY));
                        pricingData.setProductName(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, "package_name"));
                        pricingData.setPrice(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, "package_price"));

                        pricingData.setDiscountPrice(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, "discount_price"));







                        JSONArray jsPackageProList =   jsonPricingObject.getJSONArray(JsonKey.SHOPPING_PRODUCT_LIST);
                        ArrayList<PricingData> packageProductList = new ArrayList<>();

                        for (int ppL = 0; ppL < jsPackageProList.length(); ppL++) {
                            JSONObject jsonPackagePro = jsPackageProList.getJSONObject(ppL);
                            PricingData pricingD = new PricingData();

                            pricingD.setDiscountPrice(Utils.getStringFromJsonObjectWithKey(jsonPackagePro, JsonKey.SHOPPING_DISCOUNT_PRICE));
                            pricingD.setQty(Utils.getStringFromJsonObjectWithKey(jsonPackagePro, JsonKey.SHOPPING_PRODUCT_BY_CODE_QTY));
                            pricingD.setProductName(Utils.getStringFromJsonObjectWithKey(jsonPackagePro, JsonKey.COMMON_PRODUCT_NAME));
//                            pricingData.setPackageId(Constants.STR_0);
//                            pricingData.setId(Utils.getStringFromJsonObjectWithKey(jsonPackagePro, JsonKey.SHOPPING_ID));
//                            if (jsonPricingObject.has(JsonKey.SHOPPING_ATTRI_ID)){
//
//                                pricingData.setAttrId(Utils.getStringFromJsonObjectWithKey(jsonPackagePro, JsonKey.SHOPPING_ATTRI_ID));
//                            }
//                            pricingData.setAttriName(Utils.getStringFromJsonObjectWithKey(jsonPackagePro, JsonKey.SHOPPING_ATTRI_NAME));
//                            pricingData.setPrice(Utils.getStringFromJsonObjectWithKey(jsonPackagePro, JsonKey.SHOPPING_PRICE));
//                            pricingData.setProductId(Utils.getStringFromJsonObjectWithKey(jsonPackagePro, JsonKey.SHOPPING_PRODUCT_ID));
//                            pricingData.setPdSort(Utils.getStringFromJsonObjectWithKey(jsonPackagePro, JsonKey.SHOPPING_PD_SORT));
//                            pricingData.setPromoteId(Utils.getStringFromJsonObjectWithKey(jsonPackagePro, JsonKey.SHOPPING_PROMOTE_ID));
//                            pricingData.setPayStatus(Utils.getStringFromJsonObjectWithKey(jsonPackagePro, JsonKey.SHOPPING_PAY_STATUS));
                            packageProductList.add(pricingD);
                        }
                          pricingData.setProductList(packageProductList);
                    }else{

                        pricingData.setDiscountPrice(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.SHOPPING_DISCOUNT_PRICE));
                        pricingData.setQty(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.SHOPPING_PRODUCT_BY_CODE_QTY));
                        pricingData.setProductName(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.COMMON_PRODUCT_NAME));
                        pricingData.setPackageId(Constants.STR_0);


                        if (jsonPricingObject.has(JsonKey.SHOPPING_ATTRI_ID)){

                            pricingData.setAttrId(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.SHOPPING_ATTRI_ID));
                        }
                        pricingData.setAttriName(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.SHOPPING_ATTRI_NAME));
                        pricingData.setPrice(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.SHOPPING_PRICE));
                        pricingData.setProductId(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.SHOPPING_PRODUCT_ID));
                        pricingData.setPdSort(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.SHOPPING_PD_SORT));
                        pricingData.setPromoteId(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.SHOPPING_PROMOTE_ID));

                        pricingData.setPayStatus(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.SHOPPING_PAY_STATUS));


                    }

                    pricingDataList.add(pricingData);

                }
                 dataItem.setPricingDataList(pricingDataList);







                //aaList
                ArrayList<FiftyData> fiftyDataList = new ArrayList<FiftyData>();
                for (int aaIndex = 0; aaIndex < joAaList.length(); aaIndex++) {
                    JSONObject jsonProObject = joAaList.getJSONObject(aaIndex);

                    String aaCode = Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_AA_CODE);

                    FiftyData fiftyData = findFiftyDataOfAaCode(aaCode, fiftyDataList);
                    if (fiftyData == null) {
                        fiftyData = new FiftyData();
                        fiftyData.setPlayer(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_PLAYER));
                        fiftyData.setPayStatus(Utils.getIntegerFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_PAY_STATUS));
                        fiftyData.setDiscountPrice(Utils.getDoubleFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_DISCOUNT_PRICE));
                        fiftyData.setAACode(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_AA_CODE));
                        fiftyData.setAAWith(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_AA_WITH));
                        fiftyData.setAaTotalPrice(Utils.getDoubleFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_AA_TOTAL_PRICE));
                        ArrayList<ProData> aaList = new ArrayList<ProData>();
                        ProData proData = new ProData();
                        proData.setQty(Utils.getIntegerFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_QTY));
                        proData.setDisCountPrice(Utils.getDoubleFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_DISCOUNT_PRICE));
                        proData.setProductName(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_PRODUCT_NAME));
                        proData.setAttriName(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_ATTRI_NAME));
                        proData.setAttriId(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_ATTRI_ID));
                        proData.setId(Utils.getIntegerFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_ID));
                        proData.setOwnerStatus(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_OWNER_STATUS));

                        aaList.add(proData);
                        fiftyData.setAaList(aaList);
                        fiftyDataList.add(fiftyData);
                    } else {

                        ProData proData = new ProData();
                        proData.setQty(Utils.getIntegerFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_QTY));
                        proData.setDisCountPrice(Utils.getDoubleFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_DISCOUNT_PRICE));
                        proData.setProductName(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_PRODUCT_NAME));
                        proData.setAttriName(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_ATTRI_NAME));
                        proData.setAttriId(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_ATTRI_ID));
                        proData.setId(Utils.getIntegerFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_ID));
                        proData.setOwnerStatus(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_OWNER_STATUS));
                        fiftyData.getAaList().add(proData);

                    }

                }


                dataItem.setFiftyDataList(fiftyDataList);

                dataList.add(dataItem);


            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }


    }

    private FiftyData findFiftyDataOfAaCode(String code, List<FiftyData> fiftyDataList) {

        for (FiftyData fiftyData : fiftyDataList) {
            if (code.equals(fiftyData.getAACode())) {
                return fiftyData;
            }

        }
        return null;

    }

    public static class DataItem {
        private String playerId;
        private String playerName;

        private String bookingNo;

        private String checkStatus;

        private String pricingTimes;
        private String pricingBdpId;

        private String pricingPrice;


        private String pricingPayStatus;

        public String getPricingPayStatus() {
            return pricingPayStatus;
        }

        public void setPricingPayStatus(String pricingPayStatus) {
            this.pricingPayStatus = pricingPayStatus;
        }

        public String getPricingPrice() {
            return pricingPrice;
        }

        public void setPricingPrice(String pricingPrice) {
            this.pricingPrice = pricingPrice;
        }

        public String getPricingTimes() {
            return pricingTimes;
        }

        public void setPricingTimes(String pricingTimes) {
            this.pricingTimes = pricingTimes;
        }

        public String getPricingBdpId() {
            return pricingBdpId;
        }

        public void setPricingBdpId(String pricingBdpId) {
            this.pricingBdpId = pricingBdpId;
        }

        private ArrayList<ProData> proDataList;
        private ArrayList<FiftyData> fiftyDataList;
        private ArrayList<PackageData> packageList;

        public ArrayList<PricingData> getPricingDataList() {
            return pricingDataList;
        }

        public void setPricingDataList(ArrayList<PricingData> pricingDataList) {
            this.pricingDataList = pricingDataList;
        }

        private ArrayList<PricingData> pricingDataList;


        public ArrayList<PackageData> getPackageList() {
            return packageList;
        }

        public void setPackageList(ArrayList<PackageData> packageList) {
            this.packageList = packageList;
        }

        public String getCheckStatus() {
            return checkStatus;
        }

        public void setCheckStatus(String checkStatus) {
            this.checkStatus = checkStatus;
        }

        public String getBookingNo() {
            return bookingNo;
        }

        public void setBookingNo(String bookingNo) {
            this.bookingNo = bookingNo;
        }

        public String getPlayerId() {
            return playerId;
        }

        public void setPlayerId(String playerId) {
            this.playerId = playerId;
        }

        public String getPlayerName() {
            return playerName;
        }

        public void setPlayerName(String playerName) {
            this.playerName = playerName;
        }

        public List<ProData> getProDataList() {
            return proDataList;
        }

        public void setProDataList(ArrayList<ProData> proDataList) {
            this.proDataList = proDataList;
        }

        public ArrayList<FiftyData> getFiftyDataList() {
            return fiftyDataList;
        }

        public void setFiftyDataList(ArrayList<FiftyData> fiftyDataList) {
            this.fiftyDataList = fiftyDataList;
        }
    }

    public static class PricingData {

        private String id;
        private String qty;
        private String attrId;
        public String getAttrId() {
            return attrId;
        }
        public void setAttrId(String attrId) {
            this.attrId = attrId;
        }
        private String productName;
        private String discountPrice;
        private String productId;
        private String packageId;


        private String attriName;
        private String price;
        private String pdSort;
        private String promoteId;

        private String opBdpId;
        private String payStatus;

        public String getAttriName() {
            return attriName;
        }

        public void setAttriName(String attriName) {
            this.attriName = attriName;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPdSort() {
            return pdSort;
        }

        public void setPdSort(String pdSort) {
            this.pdSort = pdSort;
        }

        public String getPromoteId() {
            return promoteId;
        }

        public void setPromoteId(String promoteId) {
            this.promoteId = promoteId;
        }

        public String getOpBdpId() {
            return opBdpId;
        }

        public void setOpBdpId(String opBdpId) {
            this.opBdpId = opBdpId;
        }

        public String getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(String payStatus) {
            this.payStatus = payStatus;
        }

        private ArrayList<PricingData> productList;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(String discountPrice) {
            this.discountPrice = discountPrice;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getPackageId() {
            return packageId;
        }

        public void setPackageId(String packageId) {
            this.packageId = packageId;
        }

        public ArrayList<PricingData> getProductList() {
            return productList;
        }

        public void setProductList(ArrayList<PricingData> productList) {
            this.productList = productList;
        }
    }


    public static class ProData {
        private int id;
        private String player;
        private String productId;
        private String productName;
        private int payStatus;
        private double price;
        private double disCountPrice;
        private int qty;
        private String attriId;
        private String attriName;
        private String promoteId;

        private String pdSort;// Caddie type
        public String getPromoteId() {
            if (Utils.isStringNullOrEmpty(promoteId)) return Constants.STR_0;
            return promoteId;
        }

        public void setPromoteId(String promoteId) {
            this.promoteId = promoteId;
        }

        private String ownerStatus;

        public String getOwnerStatus() {
            return ownerStatus;
        }

        public void setOwnerStatus(String ownerStatus) {
            this.ownerStatus = ownerStatus;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }


        public String getPdSort() {
            return pdSort;
        }

        public void setPdSort(String pdSort) {
            this.pdSort = pdSort;
        }

        public String getPlayer() {
            return player;
        }

        public void setPlayer(String player) {
            this.player = player;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(int payStatus) {
            this.payStatus = payStatus;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getDisCountPrice() {
            return disCountPrice;
        }

        public void setDisCountPrice(double disCountPrice) {
            this.disCountPrice = disCountPrice;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public String getAttriId() {
            return attriId == null ? "0" : attriId;
        }

        public void setAttriId(String attriId) {
            this.attriId = attriId;
        }

        public String getAttriName() {
            return attriName;
        }

        public void setAttriName(String attriName) {
            this.attriName = attriName;
        }
    }

    public static class FiftyData {

        private double discountPrice;
        private String AAWith;
        private String AACode;
        private int payStatus;
        private ArrayList<ProData> aaList;
        private String player;


        private double aaTotalPrice;

        public double getAaTotalPrice() {
            return aaTotalPrice;
        }

        public void setAaTotalPrice(double aaTotalPrice) {
            this.aaTotalPrice = aaTotalPrice;
        }

        public double getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(double discountPrice) {
            this.discountPrice = discountPrice;
        }

        public String getAAWith() {
            return AAWith;
        }

        public void setAAWith(String AAWith) {
            this.AAWith = AAWith;
        }

        public String getAACode() {
            return AACode;
        }

        public void setAACode(String AACode) {
            this.AACode = AACode;
        }

        public int getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(int payStatus) {
            this.payStatus = payStatus;
        }

        public ArrayList<ProData> getAaList() {
            return aaList;
        }

        public void setAaList(ArrayList<ProData> aaList) {
            this.aaList = aaList;
        }

        public String getPlayer() {
            return player;
        }

        public void setPlayer(String player) {
            this.player = player;
        }
    }


//    public class FiftyData1 {
//        private int id;
//        private String player;
//        private String attriId;
//        private String productName;
//        private String productId;
//        private String attriName;
//        private int qty;
//        private double price;
//        private double discountPrice;
//        private String AAWith;
//        private String AACode;
//        private int payStatus;
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public String getPlayer() {
//            return player;
//        }
//
//        public void setPlayer(String player) {
//            this.player = player;
//        }
//
//        public String getAttriId() {
//            return attriId;
//        }
//
//        public void setAttriId(String attriId) {
//            this.attriId = attriId;
//        }
//
//        public String getProductName() {
//            return productName;
//        }
//
//        public void setProductName(String productName) {
//            this.productName = productName;
//        }
//
//        public String getProductId() {
//            return productId;
//        }
//
//        public void setProductId(String productId) {
//            this.productId = productId;
//        }
//
//        public String getAttriName() {
//            return attriName;
//        }
//
//        public void setAttriName(String attriName) {
//            this.attriName = attriName;
//        }
//
//        public int getQty() {
//            return qty;
//        }
//
//        public void setQty(int qty) {
//            this.qty = qty;
//        }
//
//        public double getPrice() {
//            return price;
//        }
//
//        public void setPrice(double price) {
//            this.price = price;
//        }
//
//        public double getDiscountPrice() {
//            return discountPrice;
//        }
//
//        public void setDiscountPrice(double discountPrice) {
//            this.discountPrice = discountPrice;
//        }
//
//        public String getAAWith() {
//            return AAWith;
//        }
//
//        public void setAAWith(String AAWith) {
//            this.AAWith = AAWith;
//        }
//
//        public String getAACode() {
//            return AACode;
//        }
//
//        public void setAACode(String AACode) {
//            this.AACode = AACode;
//        }
//
//        public int getPayStatus() {
//            return payStatus;
//        }
//
//        public void setPayStatus(int payStatus) {
//            this.payStatus = payStatus;
//        }
//    }

    public static class PackageData {
        private String packageName;
        private String packageId;
        private ArrayList<ProData> packageList;

        private int id;
        private double packagePrice;
        private int packageNumber;
        private int payStatus;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getPackagePrice() {
            return packagePrice;
        }

        public void setPackagePrice(double packagePrice) {
            this.packagePrice = packagePrice;
        }

        public int getPackageNumber() {
            return packageNumber;
        }

        public void setPackageNumber(int packageNumber) {
            this.packageNumber = packageNumber;
        }

        public int getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(int payStatus) {
            this.payStatus = payStatus;
        }

        public ArrayList<ProData> getPackageList() {
            return packageList;
        }

        public void setPackageList(ArrayList<ProData> packageList) {
            this.packageList = packageList;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getPackageId() {
            return packageId;
        }

        public void setPackageId(String packageId) {
            this.packageId = packageId;
        }
    }

}
