/**
 * Project Name: itee
 * File Name:	 JsonShoppingCheckPayPutReturn.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-05-31
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

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
 * ClassName:JsonShoppingCheckPayPutReturn <br/>
 * Function: ShoppingCheckPayPut 实体类. <br/>
 * UI:  06-02
 * Date: 2015-05-31 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonShoppingCheckPayPutReturn extends BaseJsonObject implements Serializable {

    private String total;
    private String deposit;
    private String bookingNo;

    private String amount;
    private String account;

    private String customer;


    private String balanceAccount;
    private String balanceAccountMoney;
    private ArrayList<PurchaseListItem> purchaseList;

    private ArrayList<DepositItem> depositList;


    public ArrayList<PurchaseListItem> getPurchaseList() {
        return purchaseList;
    }

    public ArrayList<DepositItem> getDepositList() {
        return depositList;
    }

    public void setPurchaseList(ArrayList<PurchaseListItem> purchaseList) {
        this.purchaseList = purchaseList;
    }

    public String getBalanceAccountMoney() {
        if (balanceAccountMoney == null)return Constants.STR_EMPTY;
        return balanceAccountMoney;
    }

    public void setBalanceAccountMoney(String balanceAccountMoney) {
        this.balanceAccountMoney = balanceAccountMoney;
    }

    public String getBalanceAccount() {
        return balanceAccount;
    }

    public void setBalanceAccount(String balanceAccount) {
        this.balanceAccount = balanceAccount;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public JsonShoppingCheckPayPutReturn(JSONObject jsonObject) {
        super(jsonObject);
    }


    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        try {
            purchaseList = new ArrayList<PurchaseListItem>();
            depositList = new ArrayList<DepositItem>();
            JSONObject dataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            setBookingNo(Utils.getStringFromJsonObjectWithKey(dataList, JsonKey.SHOPPING_BOOKING_NO));


            JSONArray jsPurchaseList =    dataList.getJSONArray(JsonKey.SHOPPING_PURCHASE_LIST);
            JSONArray jsDepositList =    dataList.getJSONArray(JsonKey.SHOPPING_DEPOSIT_LIST);
            JSONArray jsBalanceData =    dataList.getJSONArray(JsonKey.SHOPPING_BALANCE_ACCOUNT);



            if (jsBalanceData.length() > 0){

                JSONObject  balanceData =  jsBalanceData.getJSONObject(0);

                setBalanceAccount(Utils.getStringFromJsonObjectWithKey(balanceData, JsonKey.SHOPPING_FBA_ACCOUNT));

                double money1 = Double.parseDouble(  Utils.getStringFromJsonObjectWithKey(balanceData, JsonKey.SHOPPING_FBA_OVERDRAFT));
                double money2 = Double.parseDouble(  Utils.getStringFromJsonObjectWithKey(balanceData, JsonKey.SHOPPING_FTYPE_OVERDRAFT));

                setBalanceAccountMoney(Utils.get2DigitDecimalString(String.valueOf(money1+money2)));
            }
            for (int i = 0; i < jsPurchaseList.length(); i++) {
                JSONObject jsonObject = jsPurchaseList.getJSONObject(i);
                JSONArray joAaList = Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_AA_LIST);
                JSONArray joProductList = Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_PRODUCT_LIST);
                JSONArray joPackageList = Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_PACKAGE_LIST);
                JSONArray joPricingList = Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_PRICING_LIST);
                PurchaseListItem purchaseListItem = new PurchaseListItem();

                purchaseListItem.setPlayerName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_PLAYER));
                purchaseListItem.setBookingNo(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_BOOKING_NO));
                purchaseListItem.setCheckStatus(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_CHECK_STATUS));


                purchaseListItem.setPricingTimes(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_PRICING_TIMES));
                purchaseListItem.setPricingPrice(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_PRICING_PRICE));

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
                    proDataList.add(proData);
                }
                purchaseListItem.setProDataList(proDataList);




                //pricing List
                ArrayList<JsonShoppingPaymentGet.PricingData> pricingDataList = new ArrayList<>();
                for (int pp = 0; pp < joPricingList.length(); pp++) {
                    JSONObject jsonPricingObject = joPricingList.getJSONObject(pp);
                    JsonShoppingPaymentGet.PricingData pricingData = new JsonShoppingPaymentGet.PricingData();

                    pricingData.setDiscountPrice(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.SHOPPING_DISCOUNT_PRICE));
                    pricingData.setQty(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.SHOPPING_PRODUCT_BY_CODE_QTY));
                    pricingData.setProductName(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.COMMON_PRODUCT_NAME));
                    pricingData.setPackageId(Constants.STR_0);
                    pricingData.setId(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.SHOPPING_ID));

                    if (jsonPricingObject.has(JsonKey.SHOPPING_ATTRI_ID)){

                        pricingData.setAttrId(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.SHOPPING_ATTRI_ID));
                    }

                    if (jsonPricingObject.has(JsonKey.SHOPPING_PACKAGE_ID)){
                        String packageId = Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.SHOPPING_PACKAGE_ID);
                        pricingData.setPackageId(packageId);
                        JSONArray jsPackageProList =   jsonPricingObject.getJSONArray(JsonKey.SHOPPING_PRODUCT_LIST);
                        ArrayList<JsonShoppingPaymentGet.PricingData> packageProductList = new ArrayList<>();

                        for (int ppL = 0; ppL < jsPackageProList.length(); ppL++) {
                            JSONObject jsonPackagePro = jsPackageProList.getJSONObject(ppL);
                            JsonShoppingPaymentGet.PricingData pricingD = new JsonShoppingPaymentGet.PricingData();
                            pricingD.setDiscountPrice(Utils.getStringFromJsonObjectWithKey(jsonPackagePro, JsonKey.SHOPPING_DISCOUNT_PRICE));
                            pricingD.setQty(Utils.getStringFromJsonObjectWithKey(jsonPackagePro, JsonKey.SHOPPING_PRODUCT_BY_CODE_QTY));
                            pricingD.setProductName(Utils.getStringFromJsonObjectWithKey(jsonPackagePro, JsonKey.COMMON_PRODUCT_NAME));
                            packageProductList.add(pricingD);
                        }
                        pricingData.setProductList(packageProductList);
                    }

                    pricingDataList.add(pricingData);

                }
                purchaseListItem.setPricingDataList(pricingDataList);






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
                purchaseListItem.setPackageList(packageDataList);


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


                purchaseListItem.setFiftyDataList(fiftyDataList);

                purchaseList.add(purchaseListItem);


            }


            // jsDepositList

            for (int d = 0 ; d<jsDepositList.length();d++){
                JSONObject jsDepositItem = jsDepositList.getJSONObject(d);
                DepositItem depositItem = new DepositItem();
                depositItem.setBncAvail(Utils.getStringFromJsonObjectWithKey(jsDepositItem, JsonKey.SHOPPING_BNC_AVAIL));
                depositItem.setBncChargeType(Utils.getStringFromJsonObjectWithKey(jsDepositItem, JsonKey.SHOPPING_BNC_CHARGE_TYPE));
                depositItem.setBncId(Utils.getStringFromJsonObjectWithKey(jsDepositItem, JsonKey.SHOPPING_BNC_ID));
                depositItem.setId(Utils.getStringFromJsonObjectWithKey(jsDepositItem, JsonKey.SHOPPING_ID));
                depositList.add(depositItem);

            }


        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }


    public static class PurchaseListItem {
        private String playerId;
        private String playerName;

        private String pricingTimes;

        private String pricingPrice;

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

        private String bookingNo;

        private String checkStatus;
        private ArrayList<ProData> proDataList;
        private ArrayList<FiftyData> fiftyDataList;
        private ArrayList<PackageData> packageList;
        private ArrayList<JsonShoppingPaymentGet.PricingData> pricingDataList;

        public ArrayList<JsonShoppingPaymentGet.PricingData> getPricingDataList() {
            return pricingDataList;
        }

        public void setPricingDataList(ArrayList<JsonShoppingPaymentGet.PricingData> pricingDataList) {
            this.pricingDataList = pricingDataList;
        }

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
    private FiftyData findFiftyDataOfAaCode(String code, List<FiftyData> fiftyDataList) {

        for (FiftyData fiftyData : fiftyDataList) {
            if (code.equals(fiftyData.getAACode())) {
                return fiftyData;
            }

        }
        return null;

    }
    public static class DepositItem {
        private String bncId;
        private String bncAvail;
        private String bncChargeType;

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBncId() {
            return bncId;
        }

        public void setBncId(String bncId) {
            this.bncId = bncId;
        }

        public String getBncAvail() {
            return bncAvail;
        }

        public void setBncAvail(String bncAvail) {
            this.bncAvail = bncAvail;
        }

        public String getBncChargeType() {
            return bncChargeType;
        }

        public void setBncChargeType(String bncChargeType) {
            this.bncChargeType = bncChargeType;
        }
    }

}
