package cn.situne.itee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.TimeArea;
import cn.situne.itee.manager.jsonentity.JsonMain;
import cn.situne.itee.view.IteeTextView;


public class TeeTimePageItemAdapter extends BaseAdapter {

    private static final int BOOKING_STATUS_AVAILABLE = 1;
    private static final int BOOKING_STATUS_NOT_AVAILABLE = 2;
    private static final int BOOKING_STATUS_CHECK_OUT = 3;

    private String leftCourseArea;
    private String rightCourseArea;

    private int leftCourseAreaId;
    private int rightCourseAreaId;

    private int maxColumnNum;

    private int gapTime;
    private int eachGroupNum;
    private int startHour;
    private int endHour;
    private int currentPage;
    private String startTime;
    private String endTime;

    private View.OnClickListener onClickListener;

    private JsonMain data;

    private Context mContext;

    private String selectedDate;

    private ArrayList<String> showTimeList;

    private boolean isUserShowTime;
    public TeeTimePageItemAdapter(Context context,boolean isUserShowTime,ArrayList<String> showTimeList) {
        mContext = context;
        this.isUserShowTime = isUserShowTime;
        this.showTimeList = showTimeList;
    }



    @Override
    public int getCount() {
        int res = endHour - startHour + 1;
        int currentMomentStartMinute = AppUtils.getCurrentTimeWithColIdx(gapTime, startTime, endHour, 0);
        int endMinuteOfDay = AppUtils.getMinuteFromHHMMSS(endTime);
        if (currentMomentStartMinute > endMinuteOfDay) {
            res--;
        }
        if (isUserShowTime) return showTimeList.size();
        return res;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_of_appointment_browse_fragment, null);
            holder = new ViewHolder();

            view.setTag(holder);

            holder.llItemContent = (LinearLayout) view.findViewById(R.id.ll_item_content);
            holder.tvItemTime = (IteeTextView) view.findViewById(R.id.tv_item_time);
            holder.tvItemPrice = (IteeTextView) view.findViewById(R.id.tv_item_price);

            holder.tvItemTime.setTextSize(Constants.FONT_SIZE_LARGER);
            holder.tvItemPrice.setTextSize(Constants.FONT_SIZE_MORE_SMALLER);

            holder.llItemContentLeft = (LinearLayout) view.findViewById(R.id.ll_item_content_left);
            holder.llItemContentRight = (LinearLayout) view.findViewById(R.id.ll_item_content_right);

            holder.ivTimeLine = (ImageView) view.findViewById(R.id.iv_time_line);

            for (int indexY = 0; indexY < eachGroupNum; indexY++) {

                LinearLayout llOne = new LinearLayout(mContext);
                llOne.setOrientation(LinearLayout.HORIZONTAL);

                holder.llItemContentLeft.addView(llOne);
                LinearLayout.LayoutParams paramsLlOne = (LinearLayout.LayoutParams) llOne.getLayoutParams();
                paramsLlOne.width = LinearLayout.LayoutParams.MATCH_PARENT;
                paramsLlOne.height = 0;
                paramsLlOne.weight = 1;
                paramsLlOne.topMargin = 5;
                llOne.setLayoutParams(paramsLlOne);

                LinearLayout llTwo = new LinearLayout(mContext);
                llTwo.setOrientation(LinearLayout.HORIZONTAL);

                holder.llItemContentRight.addView(llTwo);
                LinearLayout.LayoutParams paramsLlTwo = (LinearLayout.LayoutParams) llTwo.getLayoutParams();
                paramsLlTwo.width = LinearLayout.LayoutParams.MATCH_PARENT;
                paramsLlTwo.height = 0;
                paramsLlTwo.weight = 1;
                paramsLlTwo.topMargin = 5;
                llTwo.setLayoutParams(paramsLlTwo);

                for (int indexX = 0; indexX < maxColumnNum; indexX++) {

                    ImageView ivContentLeft = new ImageView(mContext);
                    llOne.addView(ivContentLeft);
                    LinearLayout.LayoutParams paramsIvContentOne = (LinearLayout.LayoutParams) ivContentLeft.getLayoutParams();
                    paramsIvContentOne.width = 0;
                    paramsIvContentOne.height = LinearLayout.LayoutParams.MATCH_PARENT;
                    paramsIvContentOne.weight = 1;
                    ivContentLeft.setLayoutParams(paramsIvContentOne);
                    holder.firstImageViewList.add(ivContentLeft);

                    ImageView ivContentRight = new ImageView(mContext);

                    llTwo.addView(ivContentRight);
                    LinearLayout.LayoutParams paramsIvContentTwo = (LinearLayout.LayoutParams) ivContentRight.getLayoutParams();
                    paramsIvContentTwo.width = 0;
                    paramsIvContentTwo.height = LinearLayout.LayoutParams.MATCH_PARENT;
                    paramsIvContentTwo.weight = 1;
                    ivContentRight.setLayoutParams(paramsIvContentTwo);
                    holder.secondImageViewList.add(ivContentRight);
                }

            }

        } else {
            holder = (ViewHolder) view.getTag();
        }

        int currentHour = startHour + i;
        if (isUserShowTime){

            SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT_HHMM, Locale.getDefault());
            Date startTimeDate = null;
            try {
                startTimeDate = sdf.parse(showTimeList.get(i));

            } catch (ParseException e) {
                Utils.log(e.getMessage());
            }
            Calendar startTimeCalendar = Calendar.getInstance();
            startTimeCalendar.setTime(startTimeDate);

            currentHour = startTimeCalendar.get(Calendar.HOUR_OF_DAY);
        }


        ViewGroup.LayoutParams ivTimeLineLayoutParams = holder.ivTimeLine.getLayoutParams();
        if (currentHour == Utils.getCurrentHour() && AppUtils.getTodaySlash().equals(selectedDate)) {
            ivTimeLineLayoutParams.height = 3;
            holder.ivTimeLine.setBackgroundColor(mContext.getResources().getColor(R.color.common_high_light_blue));
        } else {
            ivTimeLineLayoutParams.height = 1;
            holder.ivTimeLine.setBackgroundColor(mContext.getResources().getColor(R.color.common_separator_gray));
        }
        holder.ivTimeLine.setLayoutParams(ivTimeLineLayoutParams);

        int totalCols = AppUtils.getAvailableTimePointInCurrentHour(gapTime, startTime, currentHour);
        if (currentHour == startHour) {
            totalCols += AppUtils.getUnavailableTimePointInStartHour(gapTime, startTime, currentHour);
        }

        Map<String, Object> leftAreaParams = new HashMap<>();
        leftAreaParams.put(TransKey.IS_FIRST_COURSE, currentPage == 0);
        leftAreaParams.put(TransKey.EACH_GROUP_NUM, eachGroupNum);
        leftAreaParams.put(TransKey.START_TIME, startTime);
        leftAreaParams.put(TransKey.END_TIME, endTime);
        leftAreaParams.put(TransKey.COURSE_AREA_TYPE, leftCourseArea);
        leftAreaParams.put(TransKey.CURRENT_HOUR, currentHour);
        leftAreaParams.put(TransKey.SELECTED_DATE, selectedDate);
        leftAreaParams.put(TransKey.COURSE_AREA_TYPE_ID, leftCourseAreaId);

        Map<String, Object> rightAreaParams = new HashMap<>();
        rightAreaParams.put(TransKey.IS_FIRST_COURSE, false);
        rightAreaParams.put(TransKey.EACH_GROUP_NUM, eachGroupNum);
        rightAreaParams.put(TransKey.START_TIME, startTime);
        rightAreaParams.put(TransKey.END_TIME, endTime);
        rightAreaParams.put(TransKey.COURSE_AREA_TYPE, rightCourseArea);
        rightAreaParams.put(TransKey.CURRENT_HOUR, currentHour);
        rightAreaParams.put(TransKey.SELECTED_DATE, selectedDate);
        rightAreaParams.put(TransKey.COURSE_AREA_TYPE_ID, rightCourseAreaId);

        holder.llItemContentLeft.setTag(leftAreaParams);
        holder.llItemContentRight.setTag(rightAreaParams);

        if (leftCourseAreaId > 0) {
            holder.llItemContentLeft.setOnClickListener(getOnClickListener());
        }
        if (rightCourseAreaId > 0) {
            holder.llItemContentRight.setOnClickListener(getOnClickListener());
        }

        String currentTimeString = Utils.getHHMMTimeStringWithHour(currentHour);
        holder.tvItemTime.setText(currentTimeString);
        for (JsonMain.DataList.PriceListItem item : data.getDataList().getPriceList()) {
            TimeArea timeArea = new TimeArea();
            String[] split = item.getTime().split(Constants.STR_SEPARATOR);
            if (split.length > 1) {
                timeArea.setStartTime(split[0]);
                timeArea.setEndTime(split[1]);
                if (Utils.isTimeInArea(currentTimeString, timeArea)) {
                    holder.tvItemPrice.setText(AppUtils.addCurrencySymbol(item.getPrice(), mContext));
                    break;
                }
            }
        }

        ArrayList<JsonMain.DataList.LockListItem> lockList = getData().getDataList().lockList;
        ArrayList<JsonMain.DataList.BookingListItem> bookingList = getData().getDataList().bookingList;

        ArrayList<TimeArea> leftLockAreaList = getTimeAreaListFromLockListItem(String.valueOf(leftCourseAreaId), lockList);
        ArrayList<TimeArea> rightLockAreaList = getTimeAreaListFromLockListItem(String.valueOf(rightCourseAreaId), lockList);

        int startMinuteOfDay = AppUtils.getMinuteFromHHMMSS(startTime);
        int endMinuteOfDay = AppUtils.getMinuteFromHHMMSS(endTime);

        for (int indexY = 0; indexY < eachGroupNum; indexY++) {

            for (int indexX = 0; indexX < maxColumnNum; indexX++) {

                int currentMomentStartMinute = AppUtils.getCurrentTimeWithColIdx(gapTime, startTime, currentHour, indexX);
                if (indexX > totalCols || currentMomentStartMinute >= Constants.MINUTES_PER_HOUR) {
                    ImageView ivCurrent;
                    if (Utils.isStringNotNullOrEmpty(leftCourseArea)) {
                        ivCurrent = holder.firstImageViewList.get(indexY * maxColumnNum + indexX);
                        if (ivCurrent != null) {
                            ivCurrent.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (Utils.isStringNotNullOrEmpty(rightCourseArea)) {
                        ivCurrent = holder.secondImageViewList.get(indexY * maxColumnNum + indexX);
                        if (ivCurrent != null) {
                            ivCurrent.setVisibility(View.INVISIBLE);
                        }
                    }
                    continue;
                }

                if (currentMomentStartMinute < startMinuteOfDay && currentHour == startHour) {
                    ImageView ivCurrentLeft = holder.firstImageViewList.get(indexY * maxColumnNum + indexX);
                    ImageView ivCurrentRight = holder.secondImageViewList.get(indexY * maxColumnNum + indexX);
                    if (Utils.isStringNotNullOrEmpty(leftCourseArea)) {
                        ivCurrentLeft.setImageResource(R.drawable.bg_gray_location);
                    } else {
                        ivCurrentLeft.setVisibility(View.GONE);
                    }
                    if (Utils.isStringNotNullOrEmpty(rightCourseArea)) {
                        ivCurrentRight.setImageResource(R.drawable.bg_gray_location);
                    } else {
                        ivCurrentRight.setVisibility(View.GONE);
                    }
                    continue;
                }

                if (currentMomentStartMinute > endMinuteOfDay && currentHour == endHour) {
                    ImageView ivCurrentLeft = holder.firstImageViewList.get(indexY * maxColumnNum + indexX);
                    ImageView ivCurrentRight = holder.secondImageViewList.get(indexY * maxColumnNum + indexX);
                    if (Utils.isStringNotNullOrEmpty(leftCourseArea)) {
                        ivCurrentLeft.setImageResource(R.drawable.bg_gray_location);
                    } else {
                        ivCurrentLeft.setVisibility(View.GONE);
                    }
                    if (Utils.isStringNotNullOrEmpty(rightCourseArea)) {
                        ivCurrentRight.setImageResource(R.drawable.bg_gray_location);
                    } else {
                        ivCurrentRight.setVisibility(View.GONE);
                    }
                    continue;
                }

                String currentTime = Utils.getHHMMTimeStringWithHourMinute(currentHour, currentMomentStartMinute);

                if (Utils.isStringNotNullOrEmpty(leftCourseArea)) {
                    configTeeTimeItem(holder, leftLockAreaList, bookingList.get(currentPage * 2), indexY, indexX, currentTime, leftCourseArea);
                }

                if (Utils.isStringNotNullOrEmpty(rightCourseArea)) {
                    configTeeTimeItem(holder, rightLockAreaList, bookingList.get(currentPage * 2 + 1), indexY, indexX, currentTime, rightCourseArea);
                }
            }
        }

        return view;
    }

    private ArrayList<TimeArea> getTimeAreaListFromLockListItem(String courseArea,
                                                                ArrayList<JsonMain.DataList.LockListItem> lockList) {
        ArrayList<TimeArea> res = new ArrayList<>();
        for (JsonMain.DataList.LockListItem item : lockList) {
            if (courseArea != null && courseArea.equalsIgnoreCase(item.lockAreaType)) {
                String strLockTimeArea = item.lockTime;
                if (strLockTimeArea.contains(Constants.STR_COMMA)) {
                    String[] arrTimeArea = strLockTimeArea.split(Constants.STR_COMMA);
                    for (String strTimeAreaItem : arrTimeArea) {
                        TimeArea area = getTimeAreaFromString(strTimeAreaItem);
                        res.add(area);
                    }
                } else {
                    TimeArea area = getTimeAreaFromString(strLockTimeArea);
                    res.add(area);
                }
            }
        }
        return res;
    }

    private TimeArea getTimeAreaFromString(String areaString) {
        TimeArea timeArea = null;
        if (areaString != null && areaString.contains(Constants.STR_SEPARATOR)) {
            String[] arrTimeArea = areaString.split(Constants.STR_SEPARATOR);
            timeArea = new TimeArea();
            timeArea.setStartTime(arrTimeArea[0]);
            timeArea.setEndTime(arrTimeArea[1]);
        }
        return timeArea;
    }

    private void configTeeTimeItem(ViewHolder holder, ArrayList<TimeArea> lockTimeAreaList,
                                   JsonMain.DataList.BookingListItem bookingListItem,
                                   int indexY, int indexX, String currentTime, String courseArea) {
        ImageView ivCurrent;
        if (courseArea.equalsIgnoreCase(leftCourseArea)) {
            ivCurrent = holder.firstImageViewList.get(indexY * maxColumnNum + indexX);
        } else {
            ivCurrent = holder.secondImageViewList.get(indexY * maxColumnNum + indexX);
        }

        if (ivCurrent != null) {

            if (ivCurrent.getVisibility() != View.VISIBLE) {
                ivCurrent.setVisibility(View.VISIBLE);
            }

            boolean isSet = false;

            if (lockTimeAreaList != null) {
                if (Utils.isTimeInAreaList(changeHourMinute2HourMinuteSecond(currentTime), lockTimeAreaList)) {
                    ivCurrent.setImageResource(R.drawable.bg_gray_location);
                    isSet = true;
                }
            }

            if (!isSet) {
                if (bookingListItem != null) {

                    String currentTimeWithSeconds = changeHourMinute2HourMinuteSecond(currentTime);

                    Map<String, ArrayList<JsonMain.DataList.BookingListItem.BookingDetailItem>> bookingDetailMap
                            = bookingListItem.bookingDetailMap;

                    ArrayList<JsonMain.DataList.BookingListItem.BookingDetailItem> bookingDetailItemList
                            = bookingDetailMap.get(currentTimeWithSeconds);

                    if (bookingDetailItemList != null) {
                        if (indexY < bookingDetailItemList.size()) {
                            JsonMain.DataList.BookingListItem.BookingDetailItem bookingDetailItem
                                    = bookingDetailItemList.get(indexY);
                            if (currentTimeWithSeconds.equalsIgnoreCase(bookingDetailItem.bookingTime)) {
                                if (AppUtils.isAgent(mContext)) {
                                    if (bookingDetailItem.bookingColor == BOOKING_STATUS_AVAILABLE
                                            || bookingDetailItem.bookingColor == BOOKING_STATUS_NOT_AVAILABLE
                                            || bookingDetailItem.bookingColor == BOOKING_STATUS_CHECK_OUT) {
                                        ivCurrent.setImageResource(R.drawable.bg_half_green_location);
                                        isSet = true;
                                    }
                                } else {
                                    if (bookingDetailItem.bookingColor == BOOKING_STATUS_AVAILABLE) {
                                        ivCurrent.setImageResource(R.drawable.bg_half_green_location);
                                        isSet = true;
                                    }
                                    if (bookingDetailItem.bookingColor == BOOKING_STATUS_NOT_AVAILABLE) {
                                        ivCurrent.setImageResource(R.drawable.bg_red_location);
                                        isSet = true;
                                    }
                                    if (bookingDetailItem.bookingColor == BOOKING_STATUS_CHECK_OUT) {
                                        ivCurrent.setImageResource(R.drawable.bg_blue_location);
                                        isSet = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!isSet) {
                ivCurrent.setImageResource(R.drawable.bg_green_location);
            }
        }
    }

    private String changeHourMinute2HourMinuteSecond(String hourMinuteString) {
        return hourMinuteString + ":00";
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public JsonMain getData() {
        return data;
    }

    public void setData(JsonMain data) {
        this.data = data;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setLeftCourseArea(String leftCourseArea) {
        this.leftCourseArea = leftCourseArea;
    }

    public void setRightCourseArea(String rightCourseArea) {
        this.rightCourseArea = rightCourseArea;
    }

    public void setLeftCourseAreaId(int leftCourseAreaId) {
        this.leftCourseAreaId = leftCourseAreaId;
    }

    public void setRightCourseAreaId(int rightCourseAreaId) {
        this.rightCourseAreaId = rightCourseAreaId;
    }

    public void setMaxColumnNum(int maxColumnNum) {
        this.maxColumnNum = maxColumnNum;
    }

    public void setGapTime(int gapTime) {
        this.gapTime = gapTime;
    }

    public void setEachGroupNum(int eachGroupNum) {
        this.eachGroupNum = eachGroupNum;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    class ViewHolder {

        LinearLayout llItemContent;
        LinearLayout llItemContentLeft;
        IteeTextView tvItemTime;
        IteeTextView tvItemPrice;
        LinearLayout llItemContentRight;
        ImageView ivTimeLine;
        ArrayList<ImageView> firstImageViewList = new ArrayList<>();
        ArrayList<ImageView> secondImageViewList = new ArrayList<>();
    }
}