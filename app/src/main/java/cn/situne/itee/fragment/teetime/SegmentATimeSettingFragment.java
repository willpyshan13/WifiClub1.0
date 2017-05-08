package cn.situne.itee.fragment.teetime;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.administration.ChooseDateFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonDateSun;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.SelectMinPopupWindow;
import cn.situne.itee.view.popwindow.SelectTimePopupWindow;
import cn.situne.itee.view.popwindow.SelectYearPopupWindow;

/**
 * Created by luochao on 12/7/15.
 */
public class SegmentATimeSettingFragment  extends BaseEditFragment {



    private LinearLayout body;


    private String firstTime;
    private String lastTime;

    private String typeFlag;
    private String weekdayPace;
    private String holidayPace;

    private String sunriseTime;
    private String sunsetTime;
    private int gap;

    private  int beforeGap;
    private  String beforeSelectedDates;

    private IteeTextView tvShowDate;
    private IteeTextView tvSunrise;
    private IteeTextView tvSunset;
    private IteeTextView tvFirstTime;
    private IteeTextView tvLastTime;
    private IteeTextView tvGap;
    private IteeTextView tvWeekPace;
    private IteeTextView tvHolidayPace;
    private String fromPage;

    private boolean isFirst;

    private String selectedDates;

    private  String reservedDate;


    private  String beforeWeek;
    private  String beforeHoliday;

    private String edit;

    private SelectMinPopupWindow selectMinPopupWindow;
    @Override
    protected int getFragmentId() {
        return R.layout.fragment_segment_a_time_setting;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        isFirst = true;
        gap = 8;
        Bundle bundle = getArguments();
        if (bundle != null) {
            fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE, StringUtils.EMPTY);
            selectedDates = bundle.getString(TransKey.SELECTED_DATE);

            edit = bundle.getString("edit","0");
        }
       // selectedDates = "2015/12/12";

        beforeSelectedDates = selectedDates;



        body = (LinearLayout)rootView.findViewById(R.id.body);
        tvShowDate = new IteeTextView(getBaseActivity());
        tvSunrise = new IteeTextView(getBaseActivity());
        tvSunset = new IteeTextView(getBaseActivity());
        tvFirstTime = new IteeTextView(getBaseActivity());
        tvLastTime = new IteeTextView(getBaseActivity());
        tvGap = new IteeTextView(getBaseActivity());
        tvWeekPace = new IteeTextView(getBaseActivity());
        tvHolidayPace = new IteeTextView(getBaseActivity());


        tvFirstTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IteeTextView clickView = (IteeTextView) view;
                String time = clickView.getText().toString();
                if ("Time".equals(time)) {

                    time = tvSunrise.getText().toString();
                }
                SelectTimePopupWindow popupWindow = new SelectTimePopupWindow(getActivity(), time, clickView, 1);
                popupWindow.showAtLocation(body, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

        tvLastTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IteeTextView clickView = (IteeTextView) view;
                String time = clickView.getText().toString();
                if ("Time".equals(time)) {

                    time = tvSunset.getText().toString();
                }
                SelectTimePopupWindow popupWindow = new SelectTimePopupWindow(getActivity(), time, clickView, 1);
                popupWindow.showAtLocation(body, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });


        tvWeekPace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final IteeTextView clickView = (IteeTextView) view;
                String time = clickView.getText().toString();
                time = time.replace("h",":");
                final SelectTimePopupWindow popupWindow = new SelectTimePopupWindow(getActivity(), time, clickView, 1);
                popupWindow.showAtLocation(body, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                popupWindow.btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tvWeekPace.setText(popupWindow.wheelViewHour.getCurrentItem()+"h"+popupWindow.wheelViewMin.getCurrentItem());
                        popupWindow.dismiss();
                    }
                });
            }
        });

        tvHolidayPace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IteeTextView clickView = (IteeTextView) view;
                String time = clickView.getText().toString();
                time = time.replace("h",":");
                final SelectTimePopupWindow popupWindow = new SelectTimePopupWindow(getActivity(), time, clickView, 1);
                popupWindow.showAtLocation(body, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                popupWindow.btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tvHolidayPace.setText(popupWindow.wheelViewHour.getCurrentItem() + "h" + popupWindow.wheelViewMin.getCurrentItem());
                        popupWindow.dismiss();
                    }
                });
            }
        });


        RelativeLayout row1 = getItem(getString(R.string.title_select_date),null,true);
        RelativeLayout row2 = getItem(getString(R.string.tee_times_sunrise),tvSunrise,false);
        RelativeLayout row3 = getItem(getString(R.string.tee_times_sunset),tvSunset,false);
        RelativeLayout row4 = getItem(getString(R.string.tee_times_first_tee_time),tvFirstTime,true);
        RelativeLayout row5 = getItem(getString(R.string.tee_times_last_tee_time),tvLastTime,true);
        RelativeLayout row6 = getItem(getString(R.string.tee_times_gap_time),tvGap,true);
        RelativeLayout row7 = getItem(getString(R.string.weekday_pace),tvWeekPace,true);
        RelativeLayout row8 = getItem(getString(R.string.holiday_pace),tvHolidayPace,true);


        ArrayList<String> dateList = AppUtils.changeString2List(selectedDates, Constants.STR_COMMA);
        String etTitle = AppUtils.getEtTimeTitle(dateList, mContext);
        tvShowDate.setText(etTitle);
        tvShowDate.setTextSize(Constants.FONT_SIZE_MORE_SMALLER);


        row1.addView(tvShowDate);

        row1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = new Bundle();
                bundle1.putString(TransKey.SELECTED_DATE, selectedDates);
                bundle1.putString(TransKey.COMMON_FROM_PAGE, SegmentATimeSettingFragment.class.getName());
                push(ChooseDateFragment.class, bundle1);
            }
        });

        row6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMinPopupWindow = new SelectMinPopupWindow(getActivity(), null,gap);
//                selectYearPopupWindow.setYear(Utils.getCurrentYear(), 5000, Integer.valueOf(tvYearValue.getText().toString()).intValue());
                selectMinPopupWindow.showAtLocation(getRootView(),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                selectMinPopupWindow.btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = selectMinPopupWindow.wheelViewYear.getCurrentItem();
                        gap = Integer.parseInt(selectMinPopupWindow.wheelViewYear.getAdapter().getItem(index));
                        selectMinPopupWindow.dismiss();
                        tvGap.setText(gap+" min");
                    }
                });
                selectMinPopupWindow.btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectMinPopupWindow.dismiss();
                    }
                });
            }
        });

        RelativeLayout.LayoutParams tvShowDateParams = (RelativeLayout.LayoutParams)tvShowDate.getLayoutParams();
        tvShowDateParams.width = getActualWidthOnThisDevice(500);
        tvShowDateParams.leftMargin = getActualWidthOnThisDevice(40);
        tvShowDateParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        body.addView(row1);
        body.addView(row2);
        body.addView(row3);
        body.addView(row4);
        body.addView(row5);
        body.addView(row6);
        body.addView(row7);
        body.addView(row8);
        tvFirstTime.setText("Time");
        tvLastTime.setText("Time");
        tvFirstTime.setTextColor(getColor(R.color.common_gray));
        tvLastTime.setTextColor(getColor(R.color.common_gray));
        tvGap.setText(gap+" min");
        tvWeekPace.setText("2h10");
        tvHolidayPace.setText("2h0");


        if (TeeTimeLimitFragment.class.getName().equals(fromPage)){

            firstTime = bundle.getString("firstTime",Constants.STR_EMPTY);

            lastTime = bundle.getString("lastTime",Constants.STR_EMPTY);
            typeFlag = bundle.getString("typeFlag",Constants.STR_EMPTY);
            weekdayPace = bundle.getString("weekdayPace",Constants.STR_EMPTY);
            holidayPace = bundle.getString("holidayPace",Constants.STR_EMPTY);
            sunriseTime = bundle.getString("sunriseTime",Constants.STR_EMPTY);
            sunsetTime = bundle.getString("sunsetTime",Constants.STR_EMPTY);
            String gapStr = bundle.getString("gap",Constants.STR_0);


            gap = Integer.parseInt(gapStr);
            beforeGap = gap;

            tvSunrise.setText(sunriseTime);

            tvSunset.setText(sunsetTime);

            tvFirstTime.setText(firstTime);
            tvLastTime.setText(lastTime);


            tvFirstTime.setTextColor(getColor(R.color.common_black));
            tvLastTime.setTextColor(getColor(R.color.common_black));
            tvGap.setText(gap+" min");





            beforeWeek = weekdayPace;
            beforeHoliday = holidayPace;
            tvWeekPace.setText(weekdayPace);
            tvHolidayPace.setText(holidayPace);




            getEditSunRiseSetTime();

        }else{
            getSunRiseSetTime();

        }

    }




    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {

    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {

    }

    @Override
    protected void reShowWithBackValue() {
        super.reShowWithBackValue();
        Bundle bundle = getReturnValues();
        if (bundle != null) {
            if (ChooseDateFragment.class.getName().equals(bundle.getString(TransKey.COMMON_FROM_PAGE))) {
                selectedDates = bundle.getString(TransKey.SELECTED_DATE);
                ArrayList<String> dateList = AppUtils.changeString2List(selectedDates, Constants.STR_COMMA);
                String etTitle = AppUtils.getEtTimeTitle(dateList, mContext);
                tvShowDate.setText(etTitle);

                getSunRiseSetTime();
            }else{
                String isOk = bundle.getString("isOk",Constants.STR_EMPTY);
                if (Utils.isStringNotNullOrEmpty(isOk)){
                    try {
                        doBackWithReturnValue(bundle,(Class<? extends BaseFragment>) Class.forName(fromPage));


                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }


    private boolean checkChange(){
        if(!tvSunrise.getText().toString().equals(sunriseTime))
            return true;
        if(!tvSunset.getText().toString().equals(sunsetTime))
            return true;
        if(!tvFirstTime.getText().toString().equals(firstTime))
            return true;

        if(!tvLastTime.getText().toString().equals(lastTime))
            return true;


        if(!tvWeekPace.getText().toString().equals(beforeWeek))
            return true;
        if(!tvHolidayPace.getText().toString().equals(beforeHoliday))
            return true;

        if (beforeGap!=gap)
            return true;
        if (!beforeSelectedDates.equals(selectedDates))
            return true;
        return false;
    }


    private boolean doCheck(){

        if (Utils.isStringNullOrEmpty(selectedDates)){

            Utils.showShortToast(getBaseActivity(),getString(R.string.segment_setting_check1));
            return false;
        }

        if ("Time".equals(tvFirstTime.getText().toString())){
            Utils.showShortToast(getBaseActivity(),getString(R.string.segment_setting_check2));
            return false;
        }

        if ("Time".equals(tvLastTime.getText().toString())){
            Utils.showShortToast(getBaseActivity(),getString(R.string.segment_setting_check3));
            return false;
        }
        return true;
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();

        getTvLeftTitle().setText(getString(R.string.tee_times_tee_times_setting));
        getTvRight().setBackground(null);
        getTvRight().setText(R.string.common_next);
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (doCheck()){
                    if (Utils.isStringNullOrEmpty(reservedDate)){
                        if (checkChange()){
                            edit ="0";
                        }else{
                            edit ="1";

                        }
                        Bundle bundle = new Bundle();
                        bundle.putString(TransKey.COMMON_FROM_PAGE, SegmentATimeSettingFragment.class.getName());
                        bundle.putString("chooseDates",selectedDates);
                        bundle.putString("sunriseTime",sunriseTime);
                        bundle.putString("sunsetTime",sunsetTime);
                        if ("Time".equals(tvFirstTime.getText().toString())){
                            bundle.putString("firstTime",tvSunrise.getText().toString());
                        }else{
                            bundle.putString("firstTime",tvFirstTime.getText().toString());
                        }
                        if ("Time".equals(tvLastTime.getText().toString())){
                            bundle.putString("lastTime",tvSunset.getText().toString());
                        }else{
                            bundle.putString("lastTime",tvLastTime.getText().toString());
                        }
                        bundle.putString("gap",gap+"");
                        bundle.putString("weekdayPace",tvWeekPace.getText().toString());
                        bundle.putString("holidayPace", tvHolidayPace.getText().toString());

                        bundle.putString("typeFlag",typeFlag);

                        bundle.putString("edit",edit);
                        push(SegmentATimeFormatFragment.class, bundle);
                    }else{

                        Utils.showShortToast(getActivity(),reservedDate+" "+getString(R.string.segment_err_mes_1));
                    }

                }





            }
        });
    }

    @Override
    public void registerForContextMenu(View view) {
        Bundle bundle = getReturnValues();

        String isOk = bundle.getString("isOk",Constants.STR_EMPTY);

        if (Utils.isStringNotNullOrEmpty(isOk)){

            try {
                doBackWithReturnValue(bundle,(Class<? extends BaseFragment>) Class.forName(fromPage));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private RelativeLayout getItem(String key,IteeTextView text,boolean isIcon){

        LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(100));
        RelativeLayout item = new RelativeLayout(getBaseActivity());
        item.setLayoutParams(itemParams);
        ImageView icon = null;
        if (isIcon){
            icon = new ImageView(getBaseActivity());
            icon.setBackgroundResource(R.drawable.icon_black);
            icon.setId(View.generateViewId());
            item.addView(icon);
            LayoutUtils.setRightArrow(icon, 40, getBaseActivity());

        }

        IteeTextView tvText = new IteeTextView(getBaseActivity());
        item.addView(tvText);
        tvText.setText(key);
        tvText.setId(View.generateViewId());
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvText, 40, getBaseActivity());

        if (text!=null){

            RelativeLayout.LayoutParams textParams = new   RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            textParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            textParams.rightMargin = getActualWidthOnThisDevice(40);
            textParams.addRule(RelativeLayout.CENTER_VERTICAL);
            if (icon!=null)
                textParams.rightMargin  =getActualWidthOnThisDevice(80);
            text.setLayoutParams(textParams);
            text.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
            item.addView(text);
        }

        AppUtils.addBottomSeparatorLine(item, getBaseActivity());
        return item;

    }


    private void getSunRiseSetTime() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_DATE, selectedDates);

        HttpManager<JsonDateSun> hh = new HttpManager<JsonDateSun>(SegmentATimeSettingFragment.this) {

            @Override
            public void onJsonSuccess(JsonDateSun jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();

                if (returnCode == Constants.RETURN_CODE_20301) {
                    sunriseTime = jo.getSunrise();
                    sunsetTime = jo.getSunset();


                    tvSunrise.setText(sunriseTime);
                    tvSunset.setText(sunsetTime);
                    reservedDate = jo.getReservedDate();
                    if (isFirst){
                        int  time = 0;
                        try{
                            time = Integer.parseInt(jo.getTransferTime());
                        }catch (NumberFormatException e){

                        }
                        String showTime =(time/60)+"h"+(time%60);
                        tvHolidayPace.setText(showTime);
                        tvWeekPace.setText(showTime);
                        isFirst = false;
                    }

                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.GetDateSun, params);
    }


    private void getEditSunRiseSetTime() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_DATE, selectedDates);

        HttpManager<JsonDateSun> hh = new HttpManager<JsonDateSun>(SegmentATimeSettingFragment.this) {

            @Override
            public void onJsonSuccess(JsonDateSun jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();

                if (returnCode == Constants.RETURN_CODE_20301) {

                    reservedDate = jo.getReservedDate();

                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.GetDateSun, params);
    }


}
