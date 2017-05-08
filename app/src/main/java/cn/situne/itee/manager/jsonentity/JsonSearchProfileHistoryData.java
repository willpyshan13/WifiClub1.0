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
 * Created by luochao on 12/2/15.
 */
public class JsonSearchProfileHistoryData extends BaseJsonObject implements Serializable {



    private List<DataItem> dataList;

    private String name;
    private String BookingNo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBookingNo() {
        return BookingNo;
    }

    public void setBookingNo(String bookingNo) {
        BookingNo = bookingNo;
    }

    public List<DataItem> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataItem> dataList) {
        this.dataList = dataList;
    }

    public JsonSearchProfileHistoryData(JSONObject jsonObject) {
        super(jsonObject);
    }

    public class DataItem implements Serializable {
        private String memberName;
        private String bookingNo;
        private String signature;
        private String total;
        private String message;
        private String email;
        private String phone;
        private String paymentPattern;


        private String payment;
        private String refundTime;


        private String sumBigVoucher;
        private String discount;
        private String subtotal;
        private String tax;
        private String balanceAccountMoney;
        private String deposit;

        private String sumVoucher;



        private ArrayList<GoodListItem> goodsList;
        private ArrayList<AAListItem> aaList;
        private ArrayList<PackageListItem> packageList;
        private ArrayList<RefundListItem> refundList;


        private ArrayList<PricingData> pricingDataList;

        public ArrayList<PricingData> getPricingDataList() {
            return pricingDataList;
        }

        public void setPricingDataList(ArrayList<PricingData> pricingDataList) {
            this.pricingDataList = pricingDataList;
        }

        private String payTime;

        private String depositBnc;


        public String getPayment() {
            return payment;
        }

        public void setPayment(String payment) {
            this.payment = payment;
        }

        public String getDepositBnc() {
            return depositBnc;
        }

        public void setDepositBnc(String depositBnc) {
            this.depositBnc = depositBnc;
        }

        public String getSumVoucher() {
            return sumVoucher;
        }

        public void setSumVoucher(String sumVoucher) {
            this.sumVoucher = sumVoucher;
        }

        public String getSumBigVoucher() {
            return sumBigVoucher;
        }

        public void setSumBigVoucher(String sumBigVoucher) {
            this.sumBigVoucher = sumBigVoucher;
        }

        public String getBalanceAccountMoney() {
            return balanceAccountMoney;
        }

        public void setBalanceAccountMoney(String balanceAccountMoney) {
            this.balanceAccountMoney = balanceAccountMoney;
        }

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        public String getPaymentPattern() {
            return paymentPattern;
        }

        public void setPaymentPattern(String paymentPattern) {
            this.paymentPattern = paymentPattern;
        }

        public String getRefundTime() {
            return refundTime;
        }

        public void setRefundTime(String refundTime) {
            this.refundTime = refundTime;
        }

        public String getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(String subtotal) {
            this.subtotal = subtotal;
        }

        public String getTax() {
            return tax;
        }

        public void setTax(String tax) {
            this.tax = tax;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public ArrayList<GoodListItem> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(ArrayList<GoodListItem> goodsList) {
            this.goodsList = goodsList;
        }

        public ArrayList<AAListItem> getAaList() {
            return aaList;
        }

        public void setAaList(ArrayList<AAListItem> aaList) {
            this.aaList = aaList;
        }

        public ArrayList<PackageListItem> getPackageList() {
            return packageList;
        }

        public void setPackageList(ArrayList<PackageListItem> packageList) {
            this.packageList = packageList;
        }

        public ArrayList<RefundListItem> getRefundList() {
            return refundList;
        }

        public void setRefundList(ArrayList<RefundListItem> refundList) {
            this.refundList = refundList;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getBookingNo() {
            return bookingNo;
        }

        public void setBookingNo(String bookingNo) {
            this.bookingNo = bookingNo;
        }

        public String getDeposit() {
            return deposit;
        }

        public void setDeposit(String deposit) {
            this.deposit = deposit;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }


        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public class AAListItem implements Serializable {

        private String id;
        private String name;
        private String amount;
        private Integer refundFlag;
        private String bkNo;
        private String player;

        public String getBkNo() {
            return bkNo;
        }

        public void setBkNo(String bkNo) {
            this.bkNo = bkNo;
        }

        public String getPlayer() {
            return player;
        }

        public void setPlayer(String player) {
            this.player = player;
        }

        private ArrayList<GoodListItem> goodsList;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getRefundFlag() {
            return refundFlag;
        }

        public void setRefundFlag(Integer refundFlag) {
            this.refundFlag = refundFlag;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public ArrayList<GoodListItem> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(ArrayList<GoodListItem> goodsList) {
            this.goodsList = goodsList;
        }
    }

    public class PackageListItem implements Serializable {


        private String id;
        private String name;
        private String amount;
        private Integer refundFlag;
        private String count;
        private ArrayList<GoodListItem> goodsList;


        private String bkNo;
        private String player;

        public String getBkNo() {
            return bkNo;
        }

        public void setBkNo(String bkNo) {
            this.bkNo = bkNo;
        }

        public String getPlayer() {
            return player;
        }

        public void setPlayer(String player) {
            this.player = player;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public Integer getRefundFlag() {
            return refundFlag;
        }

        public void setRefundFlag(Integer refundFlag) {
            this.refundFlag = refundFlag;
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

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public ArrayList<GoodListItem> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(ArrayList<GoodListItem> goodsList) {
            this.goodsList = goodsList;
        }
    }

    public class RefundListItem implements Serializable {

        private String name;
        private String price;
        private String count;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }


    public static class GoodListItem implements Serializable {

        private String id;
        private String name;
        private String price;
        private Integer count;
        private Integer refundFlag;
        private String bkNo;
        private String player;


        private ArrayList<VoucherItem> voucherItems;

        public ArrayList<VoucherItem> getVoucherItems() {
            return voucherItems;
        }

        public void setVoucherItems(ArrayList<VoucherItem> voucherItems) {
            this.voucherItems = voucherItems;
        }

        public String getBkNo() {
            return bkNo;
        }

        public void setBkNo(String bkNo) {
            this.bkNo = bkNo;
        }

        public String getPlayer() {
            return player;
        }

        public void setPlayer(String player) {
            this.player = player;
        }

        public Integer getRefundFlag() {
            return refundFlag;
        }

        public void setRefundFlag(Integer refundFlag) {
            this.refundFlag = refundFlag;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }



    public static class VoucherItem implements Serializable {

        private String voucherName;

        private String voucherMoney;

        public String getVoucherName() {
            return voucherName;
        }

        public void setVoucherName(String voucherName) {
            this.voucherName = voucherName;
        }

        public String getVoucherMoney() {
            return voucherMoney;
        }

        public void setVoucherMoney(String voucherMoney) {
            this.voucherMoney = voucherMoney;
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



        private String player;

        public String getPlayer() {
            return player;
        }

        public void setPlayer(String player) {
            this.player = player;
        }

        private String pricingTimes;

        public String getPricingTimes() {
            return pricingTimes;
        }

        public void setPricingTimes(String pricingTimes) {
            this.pricingTimes = pricingTimes;
        }

        private String bookingNo;

        public String getBookingNo() {
            return bookingNo;
        }

        public void setBookingNo(String bookingNo) {
            this.bookingNo = bookingNo;
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
    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
//        Log.e("",jsonObj.toString());
        Utils.log(jsonObj.toString());
        dataList = new ArrayList<>();
        try {


            JSONArray joDataList = jsonObj.getJSONArray(JsonKey.COMMON_DATA_LIST);

//            setName(Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_MEMBER_NAME));
//            setBookingNo(Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_MEMBER_NAME));

            for (int i = 0;i<joDataList.length();i++){
                JSONObject joDataItem = joDataList.getJSONObject(i);
                DataItem item  =new DataItem();



                item.setMemberName(Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_MEMBER_NAME));
                item.setBookingNo(Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_BOOKING_NO));
                item.setSignature(Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_SIGNATURE));
                item.setSubtotal(Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_SUBTOTAL));
                item.setTax(Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_TAX));
                item.setDiscount(Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_DISCOUNT));
                item.setDeposit(Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_DEPOSIT));
                item.setTotal(Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_TOTAL));
                item.setMessage(Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_MESSAGE));
                item.setEmail(Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_EMAIL));
                item.setPhone(Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_PHONE));
                item.setPaymentPattern(Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_HISTORY_PAYMENT_PATTERN));
                item.setRefundTime(Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_HISTORY_REFUND_TIME));
                item.setPayTime(Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_HISTORY_PAY_TIME));
                item.setBalanceAccountMoney(Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_BALANCE_ACCOUNT_MONEY));
                item.setSumBigVoucher(Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_SUM_BIG_VOUCHER));
                item.setSumVoucher(Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_SUM_VOUCHER));
                item.setDepositBnc(Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_DEPOSIT_BNC));
                item.setPayment(Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_DEPOSIT_PAYMENT));
                ArrayList<GoodListItem> goodListOne = new ArrayList<>();
                JSONArray jaGoodList = Utils.getArrayFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_GOODS_LIST);

                JSONArray joPricingList = Utils.getArrayFromJsonObjectWithKey(joDataItem, JsonKey.SHOPPING_PRICING_LIST);

                for (int j = 0; j < jaGoodList.length(); j++) {
                    JSONObject joType = jaGoodList.getJSONObject(j);
                    GoodListItem listItem = new GoodListItem();

                    listItem.setId(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_ID));
                    listItem.setName(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_NAME));
                    listItem.setPrice(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_PRICE));
                    listItem.setCount(Utils.getIntegerFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_COUNT));
                    listItem.setRefundFlag(Utils.getIntegerFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_REFUND_FLAG));
                    listItem.setBkNo(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_BK_NO));
                    listItem.setPlayer(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_PLAYER));

                    JSONArray jaVoucherList = Utils.getArrayFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_VOUCHER_LIST);
                    ArrayList<VoucherItem> voucherItems = new ArrayList<VoucherItem>();
                    for (int v = 0; v < jaVoucherList.length(); v++) {

                        JSONObject jsVoucher = jaVoucherList.getJSONObject(v);
                        VoucherItem voucherItem = new VoucherItem();
                        voucherItem.setVoucherMoney(Utils.getStringFromJsonObjectWithKey(jsVoucher, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_OV_PRICE_ACTIVE));
                        voucherItems.add(voucherItem);
                    }

                    listItem.setVoucherItems(voucherItems);

                    goodListOne.add(listItem);
                }
                item.setGoodsList(goodListOne);




                //pricing List
                ArrayList<PricingData> pricingDataList = new ArrayList<>();
                for (int pp = 0; pp < joPricingList.length(); pp++) {
                    JSONObject jsonPricingObject = joPricingList.getJSONObject(pp);
                    PricingData pricingData = new PricingData();

                    pricingData.setDiscountPrice(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.SHOPPING_PRICE));
                    pricingData.setQty(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_COUNT));
                    pricingData.setProductName(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.SHOPPING_NAME));
                    pricingData.setPackageId(Constants.STR_0);
                    pricingData.setPricingTimes(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.PRICING_TIMES));
                    pricingData.setId(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.SHOPPING_ID));
                    pricingData.setBookingNo(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_BK_NO));

                    pricingData.setPlayer(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.SHOPPING_PLAYER));
                    pricingData.setPricingTimes(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.PRICING_TIMES));

                    if (jsonPricingObject.has(JsonKey.SHOPPING_ATTRI_ID)){
                        pricingData.setAttrId(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.SHOPPING_ATTRI_ID));
                    }

                    if (jsonPricingObject.has(JsonKey.SHOPPING_PRODUCT_LIST)){
                        //  pricingData.setPackageId(Constants.STR_1);
                        pricingData.setDiscountPrice(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_AMOUNT));
                        JSONArray jsPackageProList =   jsonPricingObject.getJSONArray(JsonKey.SHOPPING_PRODUCT_LIST);
                        ArrayList<PricingData> packageProductList = new ArrayList<>();

                        for (int ppL = 0; ppL < jsPackageProList.length(); ppL++) {
                            JSONObject jsonPackagePro = jsPackageProList.getJSONObject(ppL);
                            PricingData pricingD = new PricingData();
                            pricingD.setDiscountPrice(Utils.getStringFromJsonObjectWithKey(jsonPackagePro, JsonKey.SHOPPING_PRICE));
                            pricingD.setQty(Utils.getStringFromJsonObjectWithKey(jsonPackagePro, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_COUNT));

                            pricingD.setProductName(Utils.getStringFromJsonObjectWithKey(jsonPackagePro, JsonKey.SHOPPING_NAME));
                            pricingData.setPricingTimes(Utils.getStringFromJsonObjectWithKey(jsonPricingObject, JsonKey.PRICING_TIMES));
                            pricingD.setBookingNo(Utils.getStringFromJsonObjectWithKey(jsonPackagePro, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_BK_NO));
                            packageProductList.add(pricingD);
                        }
                        pricingData.setProductList(packageProductList);
                    }

                    pricingDataList.add(pricingData);

                }
                item.setPricingDataList(pricingDataList);






                ArrayList<AAListItem> aaList = new ArrayList<>();
                JSONArray jaAaList = Utils.getArrayFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_AA_LIST);
                for (int j = 0; j < jaAaList.length(); j++) {
                    JSONObject joType = jaAaList.getJSONObject(j);
                    AAListItem listItem = new AAListItem();
                    listItem.setId(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_ID));
                    listItem.setName(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_NAME));
                    listItem.setAmount(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_AMOUNT));
                    listItem.setBkNo(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_BK_NO));
                    listItem.setPlayer(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_PLAYER));

                    listItem.setRefundFlag(Utils.getIntegerFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_REFUND_FLAG));
                    ArrayList<GoodListItem> goodListTwo = new ArrayList<>();
                    JSONArray jaGoodListTwo = Utils.getArrayFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_GOODS_LIST);
                    for (int k = 0; k < jaGoodListTwo.length(); k++) {

                        JSONObject joGoodTwo = jaGoodListTwo.getJSONObject(k);
                        GoodListItem goodListItemTwoItem = new GoodListItem();
                        goodListItemTwoItem.setId(Utils.getStringFromJsonObjectWithKey(joGoodTwo, JsonKey.PLAYER_PURCHASE_HISTORY_ID));
                        goodListItemTwoItem.setName(Utils.getStringFromJsonObjectWithKey(joGoodTwo, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_NAME));
                        goodListItemTwoItem.setPrice(Utils.getStringFromJsonObjectWithKey(joGoodTwo, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_PRICE));
                        goodListItemTwoItem.setCount(Utils.getIntegerFromJsonObjectWithKey(joGoodTwo, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_COUNT));



                        goodListTwo.add(goodListItemTwoItem);
                    }
                    listItem.setGoodsList(goodListTwo);
                    aaList.add(listItem);
                }
                item.setAaList(aaList);

                ArrayList<PackageListItem> packageList = new ArrayList<>();
                JSONArray jaPackageList = Utils.getArrayFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_PACKAGE_LIST);
                for (int j = 0; j < jaPackageList.length(); j++) {
                    JSONObject joType = jaPackageList.getJSONObject(j);
                    PackageListItem listItem = new PackageListItem();
                    listItem.setId(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_ID));
                    listItem.setName(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_NAME));
                    listItem.setAmount(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_AMOUNT));
                    listItem.setRefundFlag(Utils.getIntegerFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_REFUND_FLAG));
                    listItem.setCount(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_COUNT));
                    listItem.setBkNo(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_BK_NO));
                    listItem.setPlayer(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_PLAYER));
                    ArrayList<GoodListItem> goodListTwo = new ArrayList<>();
                    JSONArray jaGoodListTwo = Utils.getArrayFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_GOODS_LIST);
                    for (int k = 0; k < jaGoodListTwo.length(); k++) {

                        JSONObject joGoodTwo = jaGoodListTwo.getJSONObject(k);
                        GoodListItem goodListItemTwoItem = new GoodListItem();
                        goodListItemTwoItem.setId(Utils.getStringFromJsonObjectWithKey(joGoodTwo, JsonKey.PLAYER_PURCHASE_HISTORY_ID));
                        goodListItemTwoItem.setName(Utils.getStringFromJsonObjectWithKey(joGoodTwo, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_NAME));
                        goodListItemTwoItem.setPrice(Utils.getStringFromJsonObjectWithKey(joGoodTwo, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_PRICE));
                        goodListItemTwoItem.setCount(Utils.getIntegerFromJsonObjectWithKey(joGoodTwo, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_COUNT));
                        goodListItemTwoItem.setBkNo(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_BK_NO));
                        goodListItemTwoItem.setPlayer(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_PLAYER));
                        goodListTwo.add(goodListItemTwoItem);

                    }
                    listItem.setGoodsList(goodListTwo);
                    packageList.add(listItem);
                }

                item.setPackageList(packageList);

                ArrayList<RefundListItem> refundList = new ArrayList<>();
                JSONArray jaRefundList = Utils.getArrayFromJsonObjectWithKey(joDataItem, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_REFUND_LIST);
                for (int j = 0; j < jaRefundList.length(); j++) {
                    JSONObject joType = jaRefundList.getJSONObject(j);
                    RefundListItem refundListItem = new RefundListItem();
                    refundListItem.setName(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_NAME));
                    refundListItem.setPrice(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_PRICE));

                    refundListItem.setCount(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_DETAIL_COUNT));
                    refundList.add(refundListItem);
                }

                item.setRefundList(refundList);



                ///



                dataList.add(item);
            }


















        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

}
