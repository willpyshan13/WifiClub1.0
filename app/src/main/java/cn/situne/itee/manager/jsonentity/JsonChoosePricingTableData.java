package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * Created by luochao on 9/23/15.
 */
public class JsonChoosePricingTableData  extends BaseJsonObject implements Serializable {
    public JsonChoosePricingTableData(JSONObject jsonObject) {
        super(jsonObject);
    }

    private ArrayList<Player> dataList;

    private ArrayList<String>bookingList;

    public ArrayList<String> getBookingList() {
        return bookingList;
    }

    public void setBookingList(ArrayList<String> bookingList) {
        this.bookingList = bookingList;
    }

    public ArrayList<Player> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<Player> dataList) {
        this.dataList = dataList;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        Utils.log(jsonObj.toString());

        if (jsonObj != null) {
            dataList = new ArrayList<Player>();

            bookingList = new ArrayList<>();
            try {
                JSONArray arrDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);



                JSONArray jsBookingNo = jsonObj.getJSONArray(JsonKey.MAIN_BOOKING_LIST);

                for (int i = 0;i<jsBookingNo.length();i++){
                    JSONObject jsBooking = jsBookingNo.getJSONObject(i);
                    bookingList.add(Utils.getStringFromJsonObjectWithKey(jsBooking, JsonKey.SHOPPING_BOOKING_NO));
                }



                for (int i = 0; i < arrDataList.length(); i++) {
                    JSONObject jsonObject = arrDataList.getJSONObject(i);

                    Player player = new Player();
                    player.setBookingNo(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_BK_NO));

                    player.setMemberCard(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.PRICING_TABLE_MEM_CARD));
                    player.setMemberName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.PRICING_TABLE_MEM_NAME));
                    player.setName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.PRICING_TABLE_NAME));
                    player.setUser(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.PRICING_TABLE_USER));
                    player.setCheckType(Utils.getStringFromJsonObjectWithKey(jsonObject, "check_type"));
                    JSONArray jsPricing =  jsonObject.getJSONArray(JsonKey.PRICING_TABLE_PRICING);
                    ArrayList<Pricing> pricingList = new ArrayList<Pricing>();

                    for (int p = 0;p<jsPricing.length();p++){
                        JSONObject jsItemPricing = jsPricing.getJSONObject(p);
                        Pricing pricing = new Pricing();
                        pricing.setId(Utils.getStringFromJsonObjectWithKey(jsItemPricing, JsonKey.COMMON_ID));
                        pricing.setTimesAct(Utils.getStringFromJsonObjectWithKey(jsItemPricing, JsonKey.PRICING_TABLE_TIMES_ACT));
                        pricing.setType(Utils.getStringFromJsonObjectWithKey(jsItemPricing, JsonKey.PRODUCT_SEARCH_TYPE));
                        ArrayList<PricingData> pricingDataList = new ArrayList<PricingData>();
                        JSONArray jsPricingDataList = jsItemPricing.getJSONArray(JsonKey.PRICING_PRICING_DATA);
                        for (int pp = 0; pp<jsPricingDataList.length();pp++){
                            JSONObject   jsPricingDataItem = jsPricingDataList.getJSONObject(pp);
                            PricingData pricingData = new PricingData();
                            pricingData.setAttributeId(Utils.getStringFromJsonObjectWithKey(jsPricingDataItem, JsonKey.COMMON_ATTRIBUTE_ID));
                            pricingData.setMainId(Utils.getStringFromJsonObjectWithKey(jsPricingDataItem, JsonKey.PRICING_MAIN_ID));
                            pricingData.setPackageId(Utils.getStringFromJsonObjectWithKey(jsPricingDataItem, JsonKey.AGENT_PRICING_DATA_PACKAGE_ID));
                            pricingData.setProductId(Utils.getStringFromJsonObjectWithKey(jsPricingDataItem, JsonKey.COMMON_PRODUCT_ID));

                            pricingData.setGuestDiscount(Utils.getStringFromJsonObjectWithKey(jsPricingDataItem, JsonKey.PRICING_GUEST_DISCOUNT));
                            pricingData.setGuestDiscountType(Utils.getStringFromJsonObjectWithKey(jsPricingDataItem, JsonKey.PRICING_GUEST_DISCOUNT_TYPE));
                            pricingData.setMemberDiscount(Utils.getStringFromJsonObjectWithKey(jsPricingDataItem, JsonKey.PRICING_MEMBER_DISCOUNT));
                            pricingData.setMemberDiscountType(Utils.getStringFromJsonObjectWithKey(jsPricingDataItem, JsonKey.PRICING_MEMBER_DISCOUNT_TYPE));
                            pricingData.setProductName(Utils.getStringFromJsonObjectWithKey(jsPricingDataItem, JsonKey.PRICING_PRODUCT_NAME));
                            pricingData.setMemberPrice(Utils.getStringFromJsonObjectWithKey(jsPricingDataItem, JsonKey.PRICING_MEMBER_PRICE));
                            pricingData.setGuestPrice(Utils.getStringFromJsonObjectWithKey(jsPricingDataItem, JsonKey.PRICING_GUEST_PRICE));

                            if (!Constants.STR_0.equals(pricingData.getPackageId())){

                                ArrayList<PricingData> packagePricingList = new ArrayList<>();

                                JSONArray jsPackageProductList = jsPricingDataItem.getJSONArray(JsonKey.SHOPS_PACKAGE_PRODUCT_LIST);

                                for (int pa = 0;pa<jsPackageProductList.length();pa++){
                                    JSONObject jsPackageItem  = jsPackageProductList.getJSONObject(pa);
                                    PricingData packagePricingData = new PricingData();
                                    packagePricingData.setAttributeId(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.COMMON_ATTRIBUTE_ID));
                                    packagePricingData.setMainId(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_MAIN_ID));
                                    packagePricingData.setPackageId(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.AGENT_PRICING_DATA_PACKAGE_ID));
                                    packagePricingData.setProductId(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.COMMON_PRODUCT_ID));

                                    packagePricingData.setGuestDiscount(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_GUEST_DISCOUNT));
                                    packagePricingData.setGuestDiscountType(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_GUEST_DISCOUNT_TYPE));
                                    packagePricingData.setMemberDiscount(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_MEMBER_DISCOUNT));
                                    packagePricingData.setMemberDiscountType(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_MEMBER_DISCOUNT_TYPE));
                                    packagePricingData.setProductName(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_PRODUCT_NAME));
                                    packagePricingData.setMemberPrice(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_MEMBER_PRICE));
                                    packagePricingData.setGuestPrice(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_GUEST_PRICE));
                                    packagePricingList.add(packagePricingData);
                                }
                                pricingData.setPackageProductList(packagePricingList);
                            }

                            pricingDataList.add(pricingData);
                        }
                        pricing.setPricingDataList(pricingDataList);

                        pricingList.add(pricing);
                    }


                    player.setPricingList(pricingList);

                    dataList.add(player);
                }


            }catch(JSONException e){
                Utils.log(e.getMessage());
            }
        }
    }

    public static class Pricing implements Serializable {


        private String id;
        private String timesAct;
        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        private ArrayList<PricingData> pricingDataList;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTimesAct() {
            return timesAct;
        }

        public void setTimesAct(String timesAct) {
            this.timesAct = timesAct;
        }

        public ArrayList<PricingData> getPricingDataList() {
            return pricingDataList;
        }

        public void setPricingDataList(ArrayList<PricingData> pricingDataList) {
            this.pricingDataList = pricingDataList;
        }
    }



    public static class PricingData implements Serializable {

        private String memberDiscount;
        private String memberDiscountType;
        private String guestDiscount;
        private String guestDiscountType;

        private String packageId;
        private String productId;
        private String attributeId;
        private String mainId;

        private String productName;
        private String memberPrice;

        private String guestPrice;


        private ArrayList<PricingData>packageProductList;

        public ArrayList<PricingData> getPackageProductList() {
            return packageProductList;
        }

        public void setPackageProductList(ArrayList<PricingData> packageProductList) {
            this.packageProductList = packageProductList;
        }

        public String getGuestPrice() {
            return guestPrice;
        }

        public void setGuestPrice(String guestPrice) {
            this.guestPrice = guestPrice;
        }

        public String getMemberPrice() {
            return memberPrice;
        }

        public void setMemberPrice(String memberPrice) {
            this.memberPrice = memberPrice;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getMemberDiscount() {
            return memberDiscount;
        }

        public void setMemberDiscount(String memberDiscount) {
            this.memberDiscount = memberDiscount;
        }

        public String getMemberDiscountType() {
            return memberDiscountType;
        }

        public void setMemberDiscountType(String memberDiscountType) {
            this.memberDiscountType = memberDiscountType;
        }

        public String getGuestDiscount() {
            return guestDiscount;
        }

        public void setGuestDiscount(String guestDiscount) {
            this.guestDiscount = guestDiscount;
        }

        public String getGuestDiscountType() {
            return guestDiscountType;
        }

        public void setGuestDiscountType(String guestDiscountType) {
            this.guestDiscountType = guestDiscountType;
        }

        public String getPackageId() {
            return packageId;
        }

        public void setPackageId(String packageId) {
            this.packageId = packageId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getAttributeId() {
            return attributeId;
        }

        public void setAttributeId(String attributeId) {
            this.attributeId = attributeId;
        }

        public String getMainId() {
            return mainId;
        }

        public void setMainId(String mainId) {
            this.mainId = mainId;
        }
    }

    public class Player {
        private String bookingNo;
        private String user;//>0 member

        private String name;
        private String memberName;
        private String memberCard;

        private String checkType;

        public String getCheckType() {
            return checkType;
        }

        public void setCheckType(String checkType) {
            this.checkType = checkType;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getMemberCard() {
            return memberCard;
        }

        public void setMemberCard(String memberCard) {
            this.memberCard = memberCard;
        }

        private ArrayList<Pricing> pricingList;

        public String getBookingNo() {
            return bookingNo;
        }

        public void setBookingNo(String bookingNo) {
            this.bookingNo = bookingNo;
        }

        public ArrayList<Pricing> getPricingList() {
            return pricingList;
        }

        public void setPricingList(ArrayList<Pricing> pricingList) {
            this.pricingList = pricingList;
        }
    }
}
