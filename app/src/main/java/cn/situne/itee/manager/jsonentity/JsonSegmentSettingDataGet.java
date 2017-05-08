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
 * Created by luochao on 12/10/15.
 */
public class JsonSegmentSettingDataGet extends BaseJsonObject implements Serializable {


    private String gapTime;
    private String weekdayPace;
    private String firstTeeTime;
    private String teeFormat;
    private String startMaxNo;
    private String lastTeeTime;
    private String nineHolesMaxNo;

    private String sunrise;
    private String sunset;


    private String teeDate;
    private String holidayPace;

    public String getTeeDate() {
        return teeDate;
    }

    public void setTeeDate(String teeDate) {
        this.teeDate = teeDate;
    }

    public String getHolidayPace() {
        return holidayPace;
    }

    public void setHolidayPace(String holidayPace) {
        this.holidayPace = holidayPace;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }


    public String getGapTime() {
        return gapTime;
    }

    public void setGapTime(String gapTime) {
        this.gapTime = gapTime;
    }

    public String getWeekdayPace() {
        return weekdayPace;
    }

    public void setWeekdayPace(String weekdayPace) {
        this.weekdayPace = weekdayPace;
    }

    public String getFirstTeeTime() {
        return firstTeeTime;
    }

    public void setFirstTeeTime(String firstTeeTime) {
        this.firstTeeTime = firstTeeTime;
    }

    public String getTeeFormat() {
        return teeFormat;
    }

    public void setTeeFormat(String teeFormat) {
        this.teeFormat = teeFormat;
    }

    public String getStartMaxNo() {
        return startMaxNo;
    }

    public void setStartMaxNo(String startMaxNo) {
        this.startMaxNo = startMaxNo;
    }

    public String getLastTeeTime() {
        return lastTeeTime;
    }

    public void setLastTeeTime(String lastTeeTime) {
        this.lastTeeTime = lastTeeTime;
    }

    public String getNineHolesMaxNo() {
        return nineHolesMaxNo;
    }

    public void setNineHolesMaxNo(String nineHolesMaxNo) {
        this.nineHolesMaxNo = nineHolesMaxNo;
    }

    private List<PageData> pageDataList;

    public List<PageData> getPageDataList() {
        return pageDataList;
    }

    public void setPageDataList(List<PageData> pageDataList) {
        this.pageDataList = pageDataList;
    }

    public JsonSegmentSettingDataGet(JSONObject jsonObject) {
        super(jsonObject);
    }


    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        try {
            pageDataList  = new ArrayList<>();
            if (jsonObj.has("data_conf")){
                JSONObject dataConfig= jsonObj.getJSONObject("data_conf");
                setFirstTeeTime( Utils.getStringFromJsonObjectWithKey(dataConfig, "first_tee_time"));
                setGapTime(Utils.getStringFromJsonObjectWithKey(dataConfig, "gap_time"));
                setLastTeeTime(Utils.getStringFromJsonObjectWithKey(dataConfig, "last_tee_time"));
                setNineHolesMaxNo(Utils.getStringFromJsonObjectWithKey(dataConfig, "nine_holes_max_no"));
                setStartMaxNo(Utils.getStringFromJsonObjectWithKey(dataConfig, "start_max_no"));
                setTeeFormat(Utils.getStringFromJsonObjectWithKey(dataConfig, "tee_format"));
                setWeekdayPace(Utils.getStringFromJsonObjectWithKey(dataConfig, "weekday_pace"));
                setSunrise(Utils.getStringFromJsonObjectWithKey(dataConfig, "sunrise"));
                setSunset(Utils.getStringFromJsonObjectWithKey(dataConfig, "sunset"));
                setHolidayPace(Utils.getStringFromJsonObjectWithKey(dataConfig, "holiday_pace"));
                setTeeDate(Utils.getStringFromJsonObjectWithKey(dataConfig, "tee_date"));

            }

            JSONArray jsonArray = jsonObj.getJSONArray(JsonKey.COMMON_DATA_LIST);
            int size = jsonArray.length();
            pageDataList = new ArrayList<>();

            for(int i = 0;i<((size/2)+(size%2));i++){

                PageData pageData = new PageData();
                JSONArray jsRightTimes = null;

                List<LineItemData> lineList = new ArrayList<>();

                int leftPosition = i*2;
                int rightPosition = i*2+1;
                JSONObject jsonObjectPage = jsonArray.getJSONObject(leftPosition);
                JSONArray jsTimes =jsonObjectPage.getJSONArray("time");
                String caId =  Utils.getStringFromJsonObjectWithKey(jsonObjectPage, "ca_id");
                String caName =  Utils.getStringFromJsonObjectWithKey(jsonObjectPage, "ca_name");
                pageData.setLeftId(caId);
                pageData.setLeftName(caName);


                if (rightPosition<size){
                    JSONObject jsonObjectRight = jsonArray.getJSONObject(rightPosition);

                    String rightCaId =  Utils.getStringFromJsonObjectWithKey(jsonObjectRight, "ca_id");
                    String rightCaName =  Utils.getStringFromJsonObjectWithKey(jsonObjectRight, "ca_name");
                    jsRightTimes=jsonObjectRight.getJSONArray("time");
                    pageData.setRightId(rightCaId);
                    pageData.setRightName(rightCaName);

                    //jsRightTimes =jsonObjectPage.getJSONArray("time");
                }

                for(int t = 0;t<jsTimes.length();t++){

                    JSONObject lObject = jsTimes.getJSONObject(t);
                    JSONObject rObject = null;
                    ItemData leftItemData = new ItemData();
                    ItemData rightItemData = new ItemData();

                    if (jsRightTimes!=null){
                        rObject = jsRightTimes.getJSONObject(t);

                    }

                    LineItemData lineItemData = new LineItemData();

                    lineItemData.setTime(Utils.getStringFromJsonObjectWithKey(lObject, "name"));
                    leftItemData.setVal(Utils.getStringFromJsonObjectWithKey(lObject, "val"));
                    leftItemData.setFlag(Utils.getStringFromJsonObjectWithKey(lObject, "flg"));
                    leftItemData.setClickEnabled(true);

                    leftItemData.setFrequenter(Utils.getStringFromJsonObjectWithKey(lObject, "frequenter"));
                    leftItemData.setPrimeFlag(Utils.getStringFromJsonObjectWithKey(lObject, "prime_flag"));
                    leftItemData.setAllReserveMember(Utils.getStringFromJsonObjectWithKey(lObject, "all_reserve_member"));
                    leftItemData.setNo(Utils.getStringFromJsonObjectWithKey(lObject, "no"));


                    if ("3".equals(leftItemData.getFlag())){
                        leftItemData.setVal("-1");

                    }
                    lineItemData.setLeftData(leftItemData);

                    leftItemData.setStatus("1");

                    if (rObject!=null){

                        rightItemData.setVal(Utils.getStringFromJsonObjectWithKey(rObject, "val"));
                        rightItemData.setFlag(Utils.getStringFromJsonObjectWithKey(rObject, "flg"));
                        rightItemData.setClickEnabled(true);
                        rightItemData.setFrequenter(Utils.getStringFromJsonObjectWithKey(rObject, "frequenter"));
                        rightItemData.setPrimeFlag(Utils.getStringFromJsonObjectWithKey(rObject, "prime_flag"));
                        rightItemData.setAllReserveMember(Utils.getStringFromJsonObjectWithKey(rObject, "all_reserve_member"));
                        rightItemData.setNo(Utils.getStringFromJsonObjectWithKey(rObject, "no"));

                        if ("3".equals(rightItemData.getFlag())){
                            rightItemData.setVal("-1");

                        }

                        lineItemData.setRightData(rightItemData);
                        rightItemData.setStatus("1");
                    }else{
                        rightItemData.setClickEnabled(false);
                        lineItemData.setRightData(rightItemData);
                    }

                    lineList.add(lineItemData);
                }




                pageData.setDataList(lineList);

                pageDataList.add(pageData);
            }





        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    public  static  class PageData implements Serializable{
        private List<LineItemData> dataList;




        private String leftName;
        private String leftId;
        private String rightName;
        private String rightId;

        public String getLeftName() {
            return leftName;
        }

        public void setLeftName(String leftName) {
            this.leftName = leftName;
        }

        public String getLeftId() {
            return leftId;
        }

        public void setLeftId(String leftId) {
            this.leftId = leftId;
        }

        public String getRightName() {
            return rightName;
        }

        public void setRightName(String rightName) {
            this.rightName = rightName;
        }

        public String getRightId() {
            return rightId;
        }

        public void setRightId(String rightId) {
            this.rightId = rightId;
        }

        public List<LineItemData> getDataList() {
            return dataList;
        }

        public void setDataList(List<LineItemData> dataList) {
            this.dataList = dataList;
        }
    }

    public  static  class ItemData implements Serializable{
        public final  static String ITEM_DATA_STATUS_NORMAL = "1";
        public final  static String ITEM_DATA_STATUS_START_SELECT = "2";
        public final  static String ITEM_DATA_STATUS_SELECTED = "3";


        private boolean clickEnabled;

        public boolean isClickEnabled() {
            return clickEnabled;
        }

        public void setClickEnabled(boolean clickEnabled) {
            this.clickEnabled = clickEnabled;
        }

        private String status;
        private String val;
        private String no;//显示
        private String flag;// 1开球 2转场 3九洞 4


        private String index9;//9dong group

        public String getIndex9() {
            return index9;
        }

        public void setIndex9(String index9) {
            this.index9 = index9;
        }

        private String frequenter;// 会员 逗号

        private String primeFlag; //  空 没有黄金  0 管 1 事 开开

        private String allReserveMember;//会员开关

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getFrequenter() {
            return frequenter;
        }

        public void setFrequenter(String frequenter) {
            this.frequenter = frequenter;
        }

        public String getPrimeFlag() {
            return primeFlag;
        }

        public void setPrimeFlag(String primeFlag) {
            this.primeFlag = primeFlag;
        }

        public String getAllReserveMember() {
            return allReserveMember;
        }

        public void setAllReserveMember(String allReserveMember) {
            this.allReserveMember = allReserveMember;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getStatus() {
            return status;
        }
        public void setStatus(String status) {
            this.status = status;
        }


    }

    public  static  class LineItemData implements Serializable{
        private ItemData leftData;
        private ItemData rightData;
        private String time;
        private String caId;
        private String caName;
        public String getCaId() {
            return caId;
        }

        public void setCaId(String caId) {
            this.caId = caId;
        }

        public String getCaName() {
            return caName;
        }

        public void setCaName(String caName) {
            this.caName = caName;
        }

        public ItemData getLeftData() {
            return leftData;
        }

        public void setLeftData(ItemData leftData) {
            this.leftData = leftData;
        }

        public ItemData getRightData() {
            return rightData;
        }

        public void setRightData(ItemData rightData) {
            this.rightData = rightData;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

}
