/**
 * Project Name: itee
 * File Name:	 AgentsPricingTableListUpView.java
 * Package Name:  cn.situne.itee.view;
 * Date:		 2015-03-21
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.agents.AgentsPricingTableFragment;
import cn.situne.itee.fragment.customers.CustomersPricingTableDataFragment;
import cn.situne.itee.fragment.quick.QuickChooseProduct;
import cn.situne.itee.manager.jsonentity.JsonAgentsPricingGet;
import cn.situne.itee.view.popwindow.SelectTimePopupWindow;

/**
 * ClassName:AgentsPricingTableListUpView <br/>
 * Function: agents customers 部分 pricing table 画面 choose product 以上的UI 包括 choose product <br/>
 * UI:  10-5 11-4
 * Date:  2015-03-21 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
@SuppressLint("ViewConstructor")
public class AgentsPricingTableListUpView extends LinearLayout {
    private Context mContext;
    private float itemHeight;

    private RelativeLayout rlStartDate;
//    private RelativeLayout rlEndDate;

    private IteeTextView tvStartDateKey;
    private IteeTextView tvStartDateValue;
    private ImageView rightArrow;

    private IteeTextView tvAddTimeText;

    private LinearLayout addTimeLayoutParent;
    private LinearLayout addTimeEventLayout;


    private RelativeLayout rlGuestsQty;
    private RelativeLayout rlTimes;
    private RelativeLayout rlTotalNumTimes;


    private RelativeLayout rlTotalNumTimesClose;


    private IteeIntegerEditText edGuestsQty;
    private IteeIntegerEditText edTotalNumTimes;
    private CheckSwitchButton csTimes;

    public String getGuestQty() {

        return edGuestsQty.getText().toString();
    }


    public String getTotalNumTimes() {

        return edTotalNumTimes.getText().toString();
    }

    private List<View> addTimeDates;

    private boolean isInit;
    private int agentTimeId;
    private IteeIntegerEditText tvGuestTimeTextValue;
    private int guestNumber;
    private RelativeLayout rlGuestLayout;

    private BaseEditFragment mBaseEditFragment;
    private boolean isFromAgent;

    private String pricingType;

    public String getPricingType() {
        return pricingType;
    }

    public void setPricingType(String pricingType) {
        this.pricingType = pricingType;
        if (!Constants.PRICING_TYPE_1.equals(this.pricingType)){

            rlGuestsQty.setVisibility(View.GONE);
        }else{
            rlGuestsQty.setVisibility(View.VISIBLE);

        }
    }

    public AgentsPricingTableListUpView(BaseEditFragment baseEditFragment, float itemHeight,
                                        OnClickListener gotoChooseProductListener, boolean guestFlag) {
        super(baseEditFragment.getActivity().getApplicationContext());
        this.mBaseEditFragment = baseEditFragment;
        this.itemHeight = itemHeight;
        this.mContext = this.mBaseEditFragment.getActivity().getApplicationContext();
        this.isFromAgent = !guestFlag;

        setOrientation(LinearLayout.VERTICAL);
        isInit = true;
        addTimeDates = new ArrayList<>();

        rlStartDate = new RelativeLayout(mContext);
        tvStartDateKey = new IteeTextView(mContext);
        tvStartDateValue = new IteeTextView(mContext);
        rightArrow = new ImageView(mContext);
        addTimeLayoutParent = new LinearLayout(mContext);
        addTimeEventLayout = new LinearLayout(mContext);
        tvAddTimeText = new IteeTextView(mContext);

//        rlStartDate.setBackgroundColor(mContext.getResources().getColor(R.color.common_white));
        rlStartDate.setBackgroundResource(R.drawable.bg_linear_selector_color_white);//添加点击变色

        addTimeLayoutParent.setOrientation(LinearLayout.VERTICAL);

        RelativeLayout.LayoutParams addTimeParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) itemHeight);

        tvAddTimeText.setLayoutParams(addTimeParams);
        tvAddTimeText.setGravity(Gravity.CENTER_VERTICAL);
        tvAddTimeText.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvAddTimeText.setText(mContext.getResources().getString(R.string.pricing_table_addTime));

        tvAddTimeText.setTextColor(mContext.getResources().getColor(R.color.common_blue));

        addTimeEventLayout.setBackgroundColor(mContext.getResources().getColor(R.color.common_white));
        addTimeEventLayout.setPadding(mBaseEditFragment.getActualWidthOnThisDevice(40), 0, 0, 0);

        addTimeEventLayout.addView(tvAddTimeText);

        addView(rlStartDate);
        addView(getLine());

        addView(addTimeLayoutParent);
        addView(getLine());
        addView(addTimeEventLayout);
        addView(getLine());


        LinearLayout.LayoutParams rlGuestsQtyParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) itemHeight);

        LinearLayout.LayoutParams rlTimesParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) itemHeight);

        LinearLayout.LayoutParams rlTotalNumTimesParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) itemHeight);


        RelativeLayout.LayoutParams rlTotalNumTimesCloseParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) itemHeight);


        rlGuestsQty = new RelativeLayout(getContext());
        rlTimes = new RelativeLayout(getContext());
        rlTotalNumTimes = new RelativeLayout(getContext());


        rlTotalNumTimesClose = new RelativeLayout(getContext());
        rlTotalNumTimesClose.setLayoutParams(rlTotalNumTimesCloseParams);
        rlTotalNumTimesClose.setOnClickListener(null);

        rlGuestsQty.setLayoutParams(rlGuestsQtyParams);
        rlTimes.setLayoutParams(rlTimesParams);
        rlTotalNumTimes.setLayoutParams(rlTotalNumTimesParams);


        IteeTextView tvGuestsQty = new IteeTextView(getContext());
        IteeTextView tvTimes = new IteeTextView(getContext());
        IteeTextView tvTotalNumTimes = new IteeTextView(getContext());
        tvGuestsQty.setText(mBaseEditFragment.getString(R.string.pricing_table_guests_days));
        tvTimes.setText(mBaseEditFragment.getString(R.string.pricing_table_times));
        tvTotalNumTimes.setText(mBaseEditFragment.getString(R.string.pricing_table_total_num));

        tvGuestsQty.setTextColor(mBaseEditFragment.getColor(R.color.common_black));
        tvTimes.setTextColor(mBaseEditFragment.getColor(R.color.common_black));
        tvTotalNumTimes.setTextColor(mBaseEditFragment.getColor(R.color.common_black));


        edGuestsQty = new IteeIntegerEditText(mBaseEditFragment.getBaseActivity());
        edGuestsQty.setHint(mBaseEditFragment.getString(R.string.pricing_table_day));

        csTimes = new CheckSwitchButton(mBaseEditFragment.getBaseActivity());
        edTotalNumTimes = new IteeIntegerEditText(mBaseEditFragment.getBaseActivity());
        edTotalNumTimes.setHint(mBaseEditFragment.getString(R.string.pricing_table_number));

        csTimes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    rlTotalNumTimesClose.setVisibility(View.GONE);

                } else {
                    rlTotalNumTimesClose.setVisibility(View.VISIBLE);
                    rlTotalNumTimesClose.setFocusableInTouchMode(true);
                    rlTotalNumTimesClose.setFocusable(true);

                }
            }
        });
        if (gotoChooseProductListener != null){




            rlGuestsQty.addView(tvGuestsQty);
            rlGuestsQty.addView(edGuestsQty);
            rlTimes.addView(tvTimes);
            rlTimes.addView(csTimes);
            rlTotalNumTimes.addView(tvTotalNumTimes);
            rlTotalNumTimes.addView(edTotalNumTimes);
            rlTotalNumTimes.addView(rlTotalNumTimesClose);
            rlTotalNumTimesClose.setBackgroundColor(mBaseEditFragment.getColor(R.color.common_gray));
            rlTotalNumTimesClose.setAlpha(0.8f);


            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvGuestsQty, 40, mBaseEditFragment.getBaseActivity());
            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvTimes, 40, mBaseEditFragment.getBaseActivity());
            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvTotalNumTimes, 40, mBaseEditFragment.getBaseActivity());


            LayoutUtils.setCellRightValueViewOfRelativeLayout(edGuestsQty, 20, mContext);
            LayoutUtils.setCellRightValueViewOfRelativeLayout(csTimes, 20, mContext);
            LayoutUtils.setCellRightValueViewOfRelativeLayout(edTotalNumTimes, 20, mContext);


            addView(rlGuestsQty);
            rlGuestsQty.setBackgroundColor(mBaseEditFragment.getColor(R.color.common_white));
            addView(getLine());
            addView(rlTimes);
            rlTimes.setBackgroundColor(mBaseEditFragment.getColor(R.color.common_white));
            addView(getLine());
            addView(rlTotalNumTimes);
            rlTotalNumTimes.setBackgroundColor(mBaseEditFragment.getColor(R.color.common_white));
            addView(getLine());


            LinearLayout.LayoutParams vParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, mBaseEditFragment.getActualHeightOnThisDevice(20));
            View v = new View(getContext());

            v.setBackgroundColor(mBaseEditFragment.getColor(R.color.common_light_gray));
            v.setLayoutParams(vParams);
            addView(v);


            //11-4 guest
            RelativeLayout.LayoutParams rlGuestLayoutParams
                    = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) itemHeight);
            TextView tvGuestTimeText = new TextView(mContext);
            tvGuestTimeTextValue = new IteeIntegerEditText(mBaseEditFragment);
            tvGuestTimeTextValue.setHint(mBaseEditFragment.getString(R.string.customers_guest));
            rlGuestLayout = new RelativeLayout(mContext);
            rlGuestLayout.setPadding(40, 0, 40, 0);
            rlGuestLayout.setLayoutParams(rlGuestLayoutParams);

            RelativeLayout.LayoutParams guestParams
                    = new RelativeLayout.LayoutParams(DensityUtil.getActualWidthOnThisDevice(300, mContext), (int) itemHeight);
            RelativeLayout.LayoutParams guestValueParams
                    = new RelativeLayout.LayoutParams(DensityUtil.getActualWidthOnThisDevice(200, mContext), (int) itemHeight);
            guestParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            guestValueParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            tvGuestTimeTextValue.setLayoutParams(guestValueParams);
            tvGuestTimeTextValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
            tvGuestTimeTextValue.setTextSize(Constants.FONT_SIZE_NORMAL);
            tvGuestTimeTextValue.setTextColor(mContext.getResources().getColor(R.color.common_gray));


            tvGuestTimeText.setLayoutParams(guestParams);
            tvGuestTimeText.setGravity(Gravity.CENTER_VERTICAL);
            tvGuestTimeText.setTextSize(Constants.FONT_SIZE_NORMAL);
            tvGuestTimeText.setText(mContext.getResources().getString(R.string.customers_guest));
            tvGuestTimeText.setTextColor(mContext.getResources().getColor(R.color.common_black));
            rlGuestLayout.addView(tvGuestTimeText);
            rlGuestLayout.addView(tvGuestTimeTextValue);

            if (guestFlag) {
                this.addView(rlGuestLayout);
                // this.addView(getLine());
            }
            rlGuestLayout.setVisibility(View.GONE);

            //quick choose;


            LinearLayout.LayoutParams quickChooseProductParams
                    = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) this.itemHeight);
            RelativeLayout quickChooseProduct = new RelativeLayout(getContext());
            quickChooseProduct.setLayoutParams(quickChooseProductParams);
            ImageView quickIcon = new ImageView(getContext());
            quickIcon.setBackgroundResource(R.drawable.icon_right_arrow);

            IteeTextView tvQuickChoose = new IteeTextView(getContext());
            tvQuickChoose.setText(mBaseEditFragment.getString(R.string.pricing_table_quick_choose));
            tvQuickChoose.setTextColor(mBaseEditFragment.getColor(R.color.common_black));

            quickChooseProduct.addView(tvQuickChoose);
            quickChooseProduct.addView(quickIcon);
            quickChooseProduct.setBackgroundColor(Color.WHITE);

            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvQuickChoose, 40, mBaseEditFragment.getBaseActivity());
            LayoutUtils.setRightArrow(quickIcon, mBaseEditFragment.getActualWidthOnThisDevice(20), mBaseEditFragment.getBaseActivity());
            this.addView(quickChooseProduct);
            quickChooseProduct.setTag(this.getTag());
            quickChooseProduct.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    if (isFromAgent){
                        bundle.putString(TransKey.COMMON_FROM_PAGE, AgentsPricingTableFragment.class.getName());
                    }else {
                        bundle.putString(TransKey.COMMON_FROM_PAGE, CustomersPricingTableDataFragment.class.getName());
                    }

                    bundle.putString(TransKey.PRICING_TYPE, pricingType);

                    mBaseEditFragment.push(QuickChooseProduct.class, bundle);
                }
            });


            // choose product

            LinearLayout.LayoutParams chooseProductLayoutParams
                    = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) this.itemHeight);
            RelativeLayout chooseProductLayout = new RelativeLayout(mContext);


            ImageView icon = new ImageView(mContext);

            icon.setBackgroundResource(R.drawable.icon_right_arrow);

            IteeTextView tvText = new IteeTextView(mContext);
            tvText.setTextColor(mContext.getResources().getColor(R.color.common_black));
            tvText.setText(mContext.getResources().getString(R.string.pricing_table_custom_choose));
            tvText.setGravity(Gravity.CENTER_VERTICAL);

            chooseProductLayout.setLayoutParams(chooseProductLayoutParams);
            chooseProductLayout.addView(tvText);
            chooseProductLayout.addView(icon);
            chooseProductLayout.setBackgroundColor(mContext.getResources().getColor(R.color.common_white));
            chooseProductLayout.setOnClickListener(gotoChooseProductListener);

            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvText, 40, mBaseEditFragment.getBaseActivity());
            tvText.getLayoutParams().width = mBaseEditFragment.getActualWidthOnThisDevice(500);
            LayoutUtils.setRightArrow(icon, mBaseEditFragment.getActualWidthOnThisDevice(20), mBaseEditFragment.getBaseActivity());
            AppUtils.addTopSeparatorLine(chooseProductLayout, mBaseEditFragment);

            this.addView(chooseProductLayout);
            this.addView(getLine());
        }
            initViewLayoutParams();
            setLayoutView();

        if (mBaseEditFragment.getFragmentMode() == BaseEditFragment.FragmentMode.FragmentModeAdd) {

            addTimeView(Constants.AGENTS_DEFAULT_START_TIME, Constants.AGENTS_DEFAULT_END_TIME, Constants.STR_0);
            String nowDate = Utils.getHHMMSSFromMillionSecondsWithType(new Date().getTime(), Constants.DATE_FORMAT_YYYYMMDD_OBLIQUE);
            tvStartDateValue.setText(nowDate);
        }





    }

    public void setSelectDateOnClick(OnClickListener selectDateListener) {
        rlStartDate.setOnClickListener(selectDateListener);
    }


    public void setLayoutStatus(BaseEditFragment.FragmentMode fragmentMode) {

        if (fragmentMode == BaseEditFragment.FragmentMode.FragmentModeBrowse) {
            edGuestsQty.setEnabled(false);
            edTotalNumTimes.setEnabled(false);
            csTimes.setEnabled(false);
        } else {
            edGuestsQty.setEnabled(true);
            edTotalNumTimes.setEnabled(true);
            csTimes.setEnabled(true);
        }


    }

    public String getDateText() {
        return tvStartDateValue.getText().toString();
    }

    public void setDateText(String date) {
        tvStartDateValue.setText(date);
    }

    public void setGuestLayoutVisibility(int visibility) {
        rlGuestLayout.setVisibility(visibility);
    }

    public TextView getGuestTextView() {
        return tvGuestTimeTextValue;
    }

    public int getAgentTimeId() {
        return agentTimeId;
    }

    public void setAgentTimeId(int agentTimeId) {
        this.agentTimeId = agentTimeId;
    }

    public boolean isInit() {
        return true;
        //return isInit;
    }

    public void setInit(boolean isInit) {
        this.isInit = isInit;
    }

    public void setValues(String agentStartDate, List<JsonAgentsPricingGet.PricingTime> agentDataList, String guestQty, String timeAct, String timeNumber) {
        if (isInit()) {
            tvStartDateValue.setText(agentStartDate);
            edGuestsQty.setText(guestQty);

            edTotalNumTimes.setText(timeNumber);

            if (Constants.CHECK_YES.equals(timeAct)) {
                csTimes.setChecked(true);
                rlTotalNumTimesClose.setVisibility(View.GONE);

            } else {
                csTimes.setChecked(false);
                rlTotalNumTimesClose.setVisibility(View.VISIBLE);

            }
            for (JsonAgentsPricingGet.PricingTime data : agentDataList) {
                addTimeView(data.getStartTime(), data.getEndTime(), String.valueOf(data.getTimeId()));
            }
            isInit = false;
        }
    }

    public String getGuestNumber() {
        return tvGuestTimeTextValue.getText().toString();
    }

    public void setGuestNumber(int guestNumber) {
        this.guestNumber = guestNumber;
        tvGuestTimeTextValue.setText(String.valueOf(guestNumber));
    }

    public String getCsTimesFlag() {
        if (csTimes.isChecked()) {

            return Constants.STR_1;
        } else {

            return Constants.STR_0;
        }

    }

    public void setValuesCustomer(String agentStartDate, String agentEndDate, List<JsonAgentsPricingGet.PricingTime> memberDataList) {
        if (isInit()) {
            tvStartDateValue.setText(agentStartDate);
            for (JsonAgentsPricingGet.PricingTime data : memberDataList) {
                addTimeView(data.getStartTime(), data.getEndTime(), String.valueOf(data.getTimeId()));
            }
            isInit = false;
        }
    }


    public String getDataInfo() {
        return tvStartDateValue.getText().toString();
    }

    public String getAgentDataJsonString() {
        JSONArray array = new JSONArray();
        for (View view : addTimeDates) {
            Map<String, String> agentData = new HashMap<>();
            ItemLayout item = (ItemLayout) view.getTag();
            agentData.put(JsonKey.PRICING_START_TIME, item.getStartText().getText().toString());
            agentData.put(JsonKey.PRICING_END_TIME, item.getEndText().getText().toString());
            array.put(new JSONObject(agentData));

        }
        return array.toString();
    }

    public ArrayList<Times> getTimesArray(){

        ArrayList<Times> res = new ArrayList<>();
        for (View view : addTimeDates) {
            ItemLayout item = (ItemLayout) view.getTag();
            Times times = new Times();

            times.setStartTime(item.getStartText().getText().toString());
            times.setEndTimes(item.getEndText().getText().toString());
            res.add(times);

        }
        return res;


    }

  public static  class Times{

        private String startTime;
        private String endTimes;

        public String getEndTimes() {
            return endTimes;
        }

        public void setEndTimes(String endTimes) {
            this.endTimes = endTimes;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }
    }



    public String getAgentDataString() {
       String res = "";
        for (View view : addTimeDates) {
            ItemLayout item = (ItemLayout) view.getTag();
            res = res+item.getStartText().getText().toString()+Constants.STR_SEPARATOR_SPACE+item.getEndText().getText().toString()+Constants.STR_SPACE;


        }
        return res;
    }

    public String getCheckDate() {

        if (Utils.isStringNullOrEmpty(tvStartDateValue.getText().toString())){
            return AppUtils.generateNotNullMessage(mBaseEditFragment, R.string.pricing_table_choose_date);
        }


        for (View view : addTimeDates) {
            ItemLayout item = (ItemLayout) view.getTag();
            if (!Utils.isSecondDateLaterThanFirst(item.getStartText().getText().toString(),
                    item.getEndText().getText().toString(), Constants.TIME_FORMAT_HHMM)) {
                return AppUtils.generateLargerThanMessage(mBaseEditFragment, R.string.agents_end_time, R.string.agents_start_time);
            }
        }
        return null;
    }

    public String getCustomersDataJsonString() {
        JSONArray array = new JSONArray();
        for (View view : addTimeDates) {
            Map<String, String> agentData = new HashMap<>();
            ItemLayout item = (ItemLayout) view.getTag();
            agentData.put(ApiKey.MEMBER_DATA_TIME_ID, item.getTimeId());
            agentData.put(ApiKey.MEMBER_DATA_START_TIME, item.getStartText().getText().toString());
            agentData.put(ApiKey.MEMBER_DATA_END_TIME, item.getEndText().getText().toString());
            array.put(new JSONObject(agentData));
        }
        return array.toString();
    }

    private View getLine() {
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);
        View v = new View(mContext);
        v.setLayoutParams(lineParams);
        v.setBackgroundColor(mContext.getResources().getColor(R.color.common_separator_gray));
        return v;
    }

    // set layout
    private void initViewLayoutParams() {


        rlStartDate.addView(tvStartDateKey);
        RelativeLayout.LayoutParams paramTvDateKey = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (int) itemHeight);

        paramTvDateKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        paramTvDateKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvDateKey.leftMargin = mBaseEditFragment.getActualWidthOnThisDevice(40);
        tvStartDateKey.setLayoutParams(paramTvDateKey);

        rlStartDate.addView(tvStartDateValue);
        tvStartDateValue.setGravity(Gravity.END);
        RelativeLayout.LayoutParams paramsTvDateValue = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsTvDateValue.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        paramsTvDateValue.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        paramsTvDateValue.bottomMargin = mBaseEditFragment.getActualHeightOnThisDevice(2);
        paramsTvDateValue.leftMargin = mBaseEditFragment.getActualWidthOnThisDevice(40);
        tvStartDateValue.setLayoutParams(paramsTvDateValue);
        tvStartDateValue.setTextSize(Constants.FONT_SIZE_MORE_SMALLER);

        rightArrow.setBackgroundResource(R.drawable.icon_right_arrow);
        rlStartDate.addView(rightArrow);
        LayoutUtils.setRightArrow(rightArrow, mBaseEditFragment.getActualWidthOnThisDevice(20), mBaseEditFragment.getBaseActivity());

    }

    // set detail value
    private void setLayoutView() {

        tvStartDateKey.setText(R.string.pricing_table_choose_date);
        tvStartDateKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvStartDateKey.setTextColor(mBaseEditFragment.getColor(R.color.common_black));

        tvStartDateValue.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        tvStartDateValue.setTextColor(mContext.getResources().getColor(R.color.common_gray));

        addTimeEventLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBaseEditFragment.getFragmentMode() != BaseEditFragment.FragmentMode.FragmentModeBrowse) {
                    addTimeView(Constants.AGENTS_DEFAULT_START_TIME, Constants.AGENTS_DEFAULT_END_TIME, Constants.STR_EMPTY);
                }
            }
        });
    }





    // add time view

    public void addTimeView(String startTime, String endTime, String timeId) {

        addTimeLayoutParent.addView(getLine());
        LinearLayout liAddTimeLayout = new LinearLayout(this.mContext);
        liAddTimeLayout.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout rlStarTimeLayout = new RelativeLayout(this.mContext);
        RelativeLayout rlEndTimeLayout = new RelativeLayout(this.mContext);


        Button delBtn = new Button(this.mContext);
        delBtn.setBackgroundResource(R.drawable.icon_delete);
        //if (position == 0)
        delBtn.setVisibility(View.GONE);

//        if (mFragmentModel == Constants.FRAGMENT_MODEL_BROWSE)
//            delBtn.setVisibility(View.GONE);

        liAddTimeLayout.addView(rlStarTimeLayout);
        rlStarTimeLayout.setId(LinearLayout.LAYOUT_DIRECTION_RTL);
        liAddTimeLayout.addView(getLine());
        liAddTimeLayout.addView(rlEndTimeLayout);
        rlEndTimeLayout.setId(LinearLayout.LAYOUT_DIRECTION_LTR);


        IteeTextView tvStartTimeKey = new IteeTextView(mContext);
        IteeTextView tvStartTimeValue = new IteeTextView(mContext);

        tvStartTimeKey.setGravity(Gravity.CENTER_VERTICAL);
        tvStartTimeKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvStartTimeKey.setTextColor(Color.BLACK);

        tvStartTimeValue.setGravity(Gravity.CENTER_VERTICAL);
        tvStartTimeValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvStartTimeKey.setText(mContext.getResources().getString(R.string.agents_start_time));
        tvStartTimeValue.setText(startTime);
        tvStartTimeValue.setTextColor(Color.BLACK);

        tvStartTimeValue.setTextColor(mBaseEditFragment.getColor(R.color.common_gray));
        RelativeLayout.LayoutParams paramTvDateKey = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (int) itemHeight);
        paramTvDateKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        paramTvDateKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvDateKey.leftMargin = mBaseEditFragment.getActualWidthOnThisDevice(40);
        tvStartTimeKey.setLayoutParams(paramTvDateKey);
        RelativeLayout.LayoutParams paramsTvDateValue = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (int) itemHeight);

        paramsTvDateValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        paramsTvDateValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvDateValue.rightMargin = mBaseEditFragment.getActualWidthOnThisDevice(40);
        tvStartTimeValue.setLayoutParams(paramsTvDateValue);

        tvStartTimeValue.setId(View.generateViewId());
        tvStartTimeKey.setId(View.generateViewId());

        rlStarTimeLayout.addView(tvStartTimeKey);
        rlStarTimeLayout.addView(tvStartTimeValue);

        rlStarTimeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.common_white));
        rlEndTimeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.common_white));


        IteeTextView tvEndTimeKey = new IteeTextView(this.mContext);
        IteeTextView tvEndTimeValue = new IteeTextView(this.mContext);

        tvEndTimeKey.setGravity(Gravity.CENTER_VERTICAL);
        tvEndTimeKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvEndTimeKey.setTextColor(mContext.getResources().getColor(R.color.common_black));

        tvEndTimeValue.setGravity(Gravity.CENTER_VERTICAL);
        tvEndTimeValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvEndTimeValue.setTextColor(mContext.getResources().getColor(R.color.common_black));


        tvEndTimeKey.setText(this.mContext.getResources().getString(R.string.agents_end_time));
        tvEndTimeValue.setText(endTime);
        tvEndTimeValue.setTextColor(mBaseEditFragment.getColor(R.color.common_gray));
        RelativeLayout.LayoutParams paramTvEndDateKey = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (int) itemHeight);

        paramTvEndDateKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        paramTvEndDateKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvEndDateKey.leftMargin = mBaseEditFragment.getActualWidthOnThisDevice(40);
        tvEndTimeKey.setLayoutParams(paramTvEndDateKey);


        RelativeLayout.LayoutParams paramsTvEndDateValue = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (int) itemHeight);
        paramsTvEndDateValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvEndDateValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvEndDateValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        paramsTvEndDateValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvEndDateValue.rightMargin = mBaseEditFragment.getActualWidthOnThisDevice(40);
        tvEndTimeValue.setLayoutParams(paramsTvEndDateValue);
        tvEndTimeValue.setId(RelativeLayout.LAYOUT_DIRECTION_RTL);

        tvEndTimeKey.setId(RelativeLayout.LAYOUT_DIRECTION_LTR);
        rlEndTimeLayout.addView(tvEndTimeKey);
        rlEndTimeLayout.addView(tvEndTimeValue);


        addTimeLayoutParent.addView(liAddTimeLayout);
        ItemLayout item = new ItemLayout();
        item.setTimeId(timeId);
        item.setStartText(tvStartTimeValue);
        item.setEndText(tvEndTimeValue);
        item.setAddTimeLayoutParent(addTimeLayoutParent);
        item.setLiAddTimeLayout(liAddTimeLayout);
        delBtn.setTag(item);
        RelativeLayout.LayoutParams delParams = new RelativeLayout.LayoutParams((int) itemHeight / 2, (int) itemHeight / 2);
        delParams.addRule(RelativeLayout.RIGHT_OF, tvStartTimeKey.getId());
        delBtn.setLayoutParams(delParams);
        delParams.setMargins(10, 0, 0, 0);
        delParams.addRule(RelativeLayout.CENTER_VERTICAL);
        rlStarTimeLayout.addView(delBtn);
        addTimeDates.add(delBtn);
        delBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemLayout item = (ItemLayout) v.getTag();
                addTimeDates.remove(v);
                item.getAddTimeLayoutParent().removeView(item.getLiAddTimeLayout());

            }
        });
        tvStartTimeValue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBaseEditFragment.getFragmentMode() != BaseEditFragment.FragmentMode.FragmentModeBrowse) {
                    SelectTimePopupWindow popupWindow = new SelectTimePopupWindow(mBaseEditFragment.getActivity(), (TextView) v, 1);
                    popupWindow.showAtLocation(AgentsPricingTableListUpView.this, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
        });


        tvEndTimeValue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBaseEditFragment.getFragmentMode() != BaseEditFragment.FragmentMode.FragmentModeBrowse) {
                    SelectTimePopupWindow popupWindow = new SelectTimePopupWindow(mBaseEditFragment.getActivity(), (TextView) v, 1);
                    popupWindow.showAtLocation(AgentsPricingTableListUpView.this, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
        });


//
        // private SelectTimePopupWindow popupWindow;
//        tvStartTimeValue
    }

    class ItemLayout {

        private String timeId;
        private IteeTextView startText;
        private IteeTextView endText;
        private LinearLayout liAddTimeLayout;
        private LinearLayout addTimeLayoutParent;

        public String getTimeId() {
            return timeId;
        }

        public void setTimeId(String timeId) {
            this.timeId = timeId;
        }

        public LinearLayout getAddTimeLayoutParent() {
            return addTimeLayoutParent;
        }

        public void setAddTimeLayoutParent(LinearLayout addTimeLayoutParent) {
            this.addTimeLayoutParent = addTimeLayoutParent;
        }

        public LinearLayout getLiAddTimeLayout() {
            return liAddTimeLayout;
        }

        public void setLiAddTimeLayout(LinearLayout liAddTimeLayout) {
            this.liAddTimeLayout = liAddTimeLayout;
        }

        public IteeTextView getStartText() {
            return startText;
        }

        public void setStartText(IteeTextView startText) {
            this.startText = startText;
        }

        public IteeTextView getEndText() {
            return endText;
        }

        public void setEndText(IteeTextView endText) {
            this.endText = endText;
        }
    }
}
