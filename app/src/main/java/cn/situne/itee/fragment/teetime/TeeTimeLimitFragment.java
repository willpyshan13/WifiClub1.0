package cn.situne.itee.fragment.teetime;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonGetGuestTypeList;
import cn.situne.itee.manager.jsonentity.JsonSegmentSettingDataGet;
import cn.situne.itee.view.CheckSwitchButton;
import cn.situne.itee.view.IteeCheckBox;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.IteeYesAndOnPopupWindow;

/**
 * Created by luochao on 12/8/15.
 */
public class TeeTimeLimitFragment extends BaseFragment {


    private IteeYesAndOnPopupWindow gapPopup;

    private IteeYesAndOnPopupWindow memberPopup;
    private CheckSwitchButton gapCw;
    private String chooseDates;

    private String firstTime;
    private String lastTime;
    private String gap;
    private String typeFlag;
    private String weekdayPace;
    private String holidayPace;
    private String sunriseTime;
    private String sunsetTime;
    private PagerItem pagerItem;
    private List<JsonSegmentSettingDataGet.PageData> pageDataList;
    private List<PagerItem> pageViewList;
    private RelativeLayout body;
    private BottomLayout bottomLayout;
    private ViewPager viewPager;
    private ArrayList<String> jsonDataList;
    private String fromPage;
    private String selectDate;
    private String edit;
    private SelectMemberLayout selectMemberLayout;
    private String leftTitleStr;
    private String etTitle;
    private List<JsonGetGuestTypeList.GuestTypeItem> memberTypeList;
    private MemberListViewAdapter memberListViewAdapter;

    private boolean isSelectMemberPopupOkButtonClicked;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_tee_time_limit;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        pageDataList = new ArrayList<>();
        pageViewList = new ArrayList<>();

        jsonDataList = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            chooseDates = bundle.getString("chooseDates", Constants.STR_EMPTY);
            firstTime = bundle.getString("firstTime", Constants.STR_EMPTY);
            lastTime = bundle.getString("lastTime", Constants.STR_EMPTY);
            gap = bundle.getString("gap", Constants.STR_EMPTY);
            typeFlag = bundle.getString("typeFlag", Constants.STR_EMPTY);
            weekdayPace = bundle.getString("weekdayPace", Constants.STR_EMPTY);
            holidayPace = bundle.getString("holidayPace", Constants.STR_EMPTY);
            sunriseTime = bundle.getString("sunriseTime", Constants.STR_EMPTY);
            sunsetTime = bundle.getString("sunsetTime", Constants.STR_EMPTY);

            fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE);
            jsonDataList = bundle.getStringArrayList("jsonData");

            edit = bundle.getString("edit", "0");


            SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT_YYYYMMDD_OBLIQUE, Locale.getDefault());
            Date d = (Date) bundle.getSerializable("date");
            if (d != null) {
                selectDate = format.format(d);
            } else {
                selectDate = Constants.STR_EMPTY;
                //Utils.showShortToast(getActivity(), "error date");
            }

        }
        body = (RelativeLayout) rootView.findViewById(R.id.body);
        bottomLayout = new BottomLayout(getBaseActivity(), new BottomClickListener() {
            @Override
            public void clickMember() {
                getGuestType();

            }

            @Override
            public void clickPrime() {

                gapCw.setChecked(true);
                gapPopup.showAtLocation(getRootView().findViewById(R.id.rl_content_container),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);


            }

            @Override
            public boolean clearAll() {
                pagerItem.clearAll();
                return false;
            }

            @Override
            public boolean clear() {
                pagerItem.clearData();
                return false;
            }
        });


        bottomLayout.setId(View.generateViewId());
        RelativeLayout.LayoutParams viewPagerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        viewPagerParams.addRule(RelativeLayout.ABOVE, bottomLayout.getId());
        viewPager = new ViewPager(getBaseActivity());
        viewPager.setLayoutParams(viewPagerParams);
        body.addView(viewPager);
        body.addView(bottomLayout);
        gapPopup = new IteeYesAndOnPopupWindow(getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getString(R.string.custom_clear).equals(bottomLayout.getMemberBtn().getText().toString())) {

                    pagerItem.clearData(2);

                    bottomLayout.getPrimeBtn().setText(getString(R.string.prime_time));
                } else {
                    pagerItem.clickStart(2, gapCw.isChecked());

                }

            }
        }, TeeTimeLimitFragment.this);


        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getActualWidthOnThisDevice(100));
        RelativeLayout layout = new RelativeLayout(getBaseActivity());
        layout.setLayoutParams(layoutParams);
        gapCw = new CheckSwitchButton(getBaseActivity());
        gapCw.setChecked(true);
        IteeTextView text = new IteeTextView(getBaseActivity());
        layout.addView(text);
        layout.addView(gapCw);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(text, 20, getBaseActivity());
        text.setText(getString(R.string.gap_3_check));

        LayoutUtils.setCellRightValueViewOfRelativeLayout(gapCw, 20, getBaseActivity());
        text.getLayoutParams().width = getActualWidthOnThisDevice(550);

        gapPopup.setView(layout);


        memberPopup = new IteeYesAndOnPopupWindow(getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Utils.showLongToast(getBaseActivity(),"clickPrime");

            }
        }, TeeTimeLimitFragment.this);

        // save
        selectMemberLayout = new SelectMemberLayout(getBaseActivity());
        memberPopup.setView(selectMemberLayout);
        memberPopup.setOkListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getString(R.string.custom_clear).equals(bottomLayout.getMemberBtn().getText().toString())) {
                    pagerItem.clearData(1);
                    bottomLayout.getMemberBtn().setText(getString(R.string.member_only));
                } else {

                    bottomLayout.getClearBtn().setText(getString(R.string.custom_clear_all));
                    boolean isOk = false;
                    for (JsonGetGuestTypeList.GuestTypeItem item : memberTypeList) {
                        if (item.isChecked()) {
                            isOk = true;
                            pagerItem.clickStart(1, selectMemberLayout.getMemberCw().isChecked());
                            break;
                        }
                    }
                    if (isOk) {
                        isSelectMemberPopupOkButtonClicked = true;
                        memberPopup.dismiss();
                    } else {
                        Utils.showShortToast(getBaseActivity(), getString(R.string.limit_err_1));
                    }
                }

            }
        });

        if (jsonDataList != null && jsonDataList.size() > 0) {

            for (String str : jsonDataList) {
                pageDataList.add((JsonSegmentSettingDataGet.PageData) Utils.getObjectFromString(str));
            }

            for (JsonSegmentSettingDataGet.PageData pageData : pageDataList) {
                PagerItem p = new PagerItem(getBaseActivity(), pageData);
                p.setPageListData(pageDataList);
                pageViewList.add(p);
            }

            viewPager.setAdapter(new PagerAdapter() {
                @Override
                public int getCount() {
                    return pageDataList.size();
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    ((ViewGroup) container).removeView((View) object);
                    object = null;
                }


                @Override
                public void setPrimaryItem(ViewGroup container, int position, Object object) {
                    pagerItem = (PagerItem) object;
                }


                @Override
                public boolean isViewFromObject(View view, Object object) {
                    return view == object;
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {


                    PagerItem p = pageViewList.get(position);
                    container.addView(p);
                    return p;
                }
            });

        } else {
            getTeeTimeFormatData();
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
    protected void configActionBar() {
        setStackedActionBar();


        if (Utils.isStringNotNullOrEmpty(leftTitleStr)) {
            getTvLeftTitle().setText(leftTitleStr);

            if ("1".equals(edit)) {
                String showStr = getString(R.string.tee_times_tee_times) + etTitle;
                SpannableString ss = new SpannableString(showStr);
                ss.setSpan(new ForegroundColorSpan(getColor(R.color.limit_title)), getString(R.string.tee_times_tee_times).length(), showStr.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                ss.setSpan(new RelativeSizeSpan(0.8f), getString(R.string.tee_times_tee_times).length(), showStr.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                getTvLeftTitle().setText(ss);
                leftTitleStr = getTvLeftTitle().getText().toString();

            }
        } else {
            if (!"1".equals(edit)) {
                getTvLeftTitle().setText(getString(R.string.tee_time_limit));
                leftTitleStr = getTvLeftTitle().getText().toString();
            }

        }

        getTvRight().setText(getResources().getString(R.string.common_ok));

        if (Utils.isStringNotNullOrEmpty(selectDate)) {
            bottomLayout.setVisibility(View.GONE);
            //getTvRight().setText(getResources().getString(R.string.common_next));

            getTvRight().setText(Constants.STR_EMPTY);
            getTvRight().setBackgroundResource(R.drawable.icon_common_edit);
        }

        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isStringNotNullOrEmpty(selectDate)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeLimitFragment.class.getName());
                    bundle.putString(TransKey.SELECTED_DATE, chooseDates);
                    bundle.putString("sunriseTime", sunriseTime);
                    bundle.putString("sunsetTime", sunsetTime);
                    bundle.putString("firstTime", firstTime);
                    bundle.putString("lastTime", lastTime);
                    bundle.putString("gap", gap);
                    bundle.putString("weekdayPace", weekdayPace);
                    bundle.putString("holidayPace", holidayPace);
                    bundle.putString("typeFlag", typeFlag);

                    bundle.putString("edit", "1");
                    push(SegmentATimeSettingFragment.class, bundle);

                } else {
                    saveTeeTime();
                }

            }
        });

    }

    private String getJsonData() {

        JSONArray resArray = new JSONArray();

        try {
            for (PagerItem pagerItem : pageViewList) {
                boolean haveRight = false;
                JSONObject jsItemLeft = new JSONObject();
                JSONObject jsItemRight = new JSONObject();
                jsItemLeft.put("ca_name", pagerItem.getPageData().getLeftName());
                jsItemLeft.put("ca_id", pagerItem.getPageData().getLeftId());
                if (Utils.isStringNotNullOrEmpty(pagerItem.getPageData().getRightName())) {
                    haveRight = true;
                    jsItemRight.put("ca_name", pagerItem.getPageData().getRightName());
                    jsItemRight.put("ca_id", pagerItem.getPageData().getRightId());

                }

                JSONArray jsLeftTimeArray = new JSONArray();
                JSONArray jsRightTimeArray = new JSONArray();
                for (JsonSegmentSettingDataGet.LineItemData lineItemData : pagerItem.getPageData().getDataList()) {

                    JsonSegmentSettingDataGet.ItemData leftData = lineItemData.getLeftData();
                    //time
                    JSONObject leftObject = new JSONObject();
                    leftObject.put("frequenter", leftData.getFrequenter());
                    leftObject.put("prime_flag", leftData.getPrimeFlag());
                    leftObject.put("all_reserve_member", leftData.getAllReserveMember());
                    leftObject.put("flg", leftData.getFlag());
                    leftObject.put("no", leftData.getNo());
                    leftObject.put("name", lineItemData.getTime());
                    leftObject.put("val", leftData.getVal());

                    jsLeftTimeArray.put(leftObject);
                    if (haveRight) {
                        JsonSegmentSettingDataGet.ItemData rightData = lineItemData.getRightData();
                        JSONObject rightObject = new JSONObject();
                        rightObject.put("frequenter", rightData.getFrequenter());
                        rightObject.put("prime_flag", rightData.getPrimeFlag());
                        rightObject.put("all_reserve_member", rightData.getAllReserveMember());
                        rightObject.put("flg", rightData.getFlag());
                        rightObject.put("no", rightData.getNo());
                        rightObject.put("val", rightData.getVal());
                        rightObject.put("name", lineItemData.getTime());
                        jsRightTimeArray.put(rightObject);
                    }
                }

                jsItemLeft.put("time", jsLeftTimeArray);
                resArray.put(jsItemLeft);
                if (haveRight) {
                    jsItemRight.put("time", jsRightTimeArray);
                    resArray.put(jsItemRight);
                }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //JSONObject jsItem = new JSONObject();

        return resArray.toString();
    }

    protected void getTeeTimeFormatData() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));

        if (Utils.isStringNotNullOrEmpty(selectDate) || "1".equals(edit)) {
            if (Utils.isStringNotNullOrEmpty(selectDate)) {
                params.put("tee_date", selectDate.substring(0, 10));
            } else {
                params.put("tee_date", chooseDates.substring(0, 10));
            }
        } else {
            params.put("first_tee_time", firstTime);
            params.put("last_tee_time", lastTime);
            params.put("gap_time", gap);
            String[] ws = weekdayPace.split("h");
            String[] hs = holidayPace.split("h");
            String postWeekDay = String.valueOf(Integer.parseInt(ws[0]) * 60 + Integer.parseInt(ws[1]));
            String postHoliday = String.valueOf(Integer.parseInt(hs[0]) * 60 + Integer.parseInt(hs[1]));
            params.put("weekday_pace", postWeekDay);
            params.put("holiday_pace", postHoliday);
            params.put("type_flag", typeFlag);

        }
        HttpManager<JsonSegmentSettingDataGet> hh = new HttpManager<JsonSegmentSettingDataGet>(TeeTimeLimitFragment.this) {


            @Override
            public void onJsonSuccess(JsonSegmentSettingDataGet jo) {


                if (Utils.isStringNotNullOrEmpty(selectDate) || "1".equals(edit)) {

                    if ("1".equals(edit)) {

                        // chooseDates = bundle.getString("chooseDates", Constants.STR_EMPTY);
                        firstTime = jo.getFirstTeeTime().substring(0, 5);
                        lastTime = jo.getLastTeeTime().substring(0, 5);
                        gap = jo.getGapTime();
                        typeFlag = jo.getTeeFormat();
                        int weekdayPaceTime = 0;
                        try {
                            weekdayPaceTime = Integer.parseInt(jo.getWeekdayPace());
                        } catch (NumberFormatException e) {

                        }
                        String showWeekdayPaceTime = (weekdayPaceTime / 60) + "h" + (weekdayPaceTime % 60);


                        int holidayPaceTime = 0;
                        try {
                            holidayPaceTime = Integer.parseInt(jo.getHolidayPace());
                        } catch (NumberFormatException e) {

                        }
                        String holidayPaceTimeTime = (holidayPaceTime / 60) + "h" + (holidayPaceTime % 60);

                        weekdayPace = showWeekdayPaceTime;
                        holidayPace = holidayPaceTimeTime;


                        sunriseTime = jo.getSunrise().substring(0, 5);
                        sunsetTime = jo.getSunset().substring(0, 5);
                        chooseDates = jo.getTeeDate();
                    }


                    if (Utils.isStringNotNullOrEmpty(selectDate)) {

                        String teeDate = jo.getTeeDate();

                        ArrayList<String> dateList = AppUtils.changeString2List(teeDate, Constants.STR_COMMA);
                        etTitle = " " + AppUtils.getEtTimeTitle(dateList, mContext);


                        String showStr = getString(R.string.tee_times_tee_times) + etTitle;
                        SpannableString ss = new SpannableString(showStr);
                        ss.setSpan(new ForegroundColorSpan(getColor(R.color.limit_title)), getString(R.string.tee_times_tee_times).length(), showStr.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                        ss.setSpan(new RelativeSizeSpan(0.8f), getString(R.string.tee_times_tee_times).length(), showStr.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        getTvLeftTitle().setText(ss);
                        leftTitleStr = getTvLeftTitle().getText().toString();


                    }

                    if ("1".equals(edit) && fromPage.equals(SegmentATimeFormatFragment.class.getName())) {
                        getTvLeftTitle().setText(getString(R.string.tee_time_limit));
                        leftTitleStr = getTvLeftTitle().getText().toString();

                    }


                }
                pageDataList = jo.getPageDataList();

                for (JsonSegmentSettingDataGet.PageData pageData : pageDataList) {
                    PagerItem p = new PagerItem(getBaseActivity(), pageData);
                    p.setPageListData(pageDataList);
                    pageViewList.add(p);

                }

                viewPager.setAdapter(new PagerAdapter() {
                    @Override
                    public int getCount() {
                        return pageDataList.size();
                    }

                    @Override
                    public void destroyItem(ViewGroup container, int position, Object object) {
                        ((ViewGroup) container).removeView((View) object);
                        object = null;
                    }


                    @Override
                    public void setPrimaryItem(ViewGroup container, int position, Object object) {
                        pagerItem = (PagerItem) object;
                    }


                    @Override
                    public boolean isViewFromObject(View view, Object object) {
                        return view == object;
                    }

                    @Override
                    public Object instantiateItem(ViewGroup container, int position) {


                        PagerItem p = pageViewList.get(position);
                        container.addView(p);
                        return p;
                    }
                });


            }

            @Override
            public void onJsonError(VolleyError error) {
                Log.e("DJZ", String.valueOf(error));
            }
        };

        hh.startGet(getActivity(), ApiManager.HttpApi.GetTeeTimeFormatData, params);


    }

    public void getGuestType() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        HttpManager<JsonGetGuestTypeList> hh = new HttpManager<JsonGetGuestTypeList>(TeeTimeLimitFragment.this) {
            @Override
            public void onJsonSuccess(JsonGetGuestTypeList jo) {
                Integer returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                memberTypeList = jo.getDataList();
                selectMemberLayout.getAllCheck().setChecked(false);

                if (memberTypeList.size() >= 5) {

                    selectMemberLayout.getListView().getLayoutParams().height = getActualHeightOnThisDevice(500);
                } else {
                    selectMemberLayout.getListView().getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;

                }

                selectMemberLayout.getMemberCw().setChecked(true);
                memberPopup.showAtLocation(getRootView().findViewById(R.id.rl_content_container),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                memberListViewAdapter.notifyDataSetChanged();
//                if (Constants.RETURN_CODE_20301 == returnCode) {
//                    memberTypeLayout.refreshLayout(jo.getDataList());
//                } else {
//                    Utils.showShortToast(getActivity(), msg);
//                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(getActivity(), R.string.msg_common_network_error);
            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.CsBookingGuestType, params);
    }
    //api

    private View getLineBlue() {

        View view = AppUtils.getSeparatorLine(getBaseActivity(), getActualHeightOnThisDevice(1));
        view.setBackgroundColor(getColor(R.color.calendar_blue));
        return view;
    }

    public void saveTeeTime() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put("start_date", chooseDates);

        params.put("first_tee_time", firstTime);
        params.put("last_tee_time", lastTime);
        params.put("gap_time", gap);

        String[] ws = weekdayPace.split("h");
        String[] hs = holidayPace.split("h");

        String postWeekDay = String.valueOf(Integer.parseInt(ws[0]) * 60 + Integer.parseInt(ws[1]));
        String postHoliday = String.valueOf(Integer.parseInt(hs[0]) * 60 + Integer.parseInt(hs[1]));
        params.put("weekday_pace", postWeekDay);
        params.put("holiday_pace", postHoliday);
        params.put("tee_format", typeFlag);
        params.put("sunriseTime", sunriseTime);
        params.put("sunsetTime", sunsetTime);
        params.put("data_list", getJsonData());
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(TeeTimeLimitFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                Utils.showShortToast(getActivity(), msg);
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    Bundle bundle = new Bundle();
                    bundle.putString("isOk", "1");
                    // try {


                    // if ("1".equals(edit)){

                    doBackWithSegmentReturnValue(bundle, TeeTimeCalendarFragment.class);
//                        }else{
//
//                            doBackWithReturnValue(bundle,(Class<? extends BaseFragment>) Class.forName(fromPage));
//                        }


//                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    }


                }
            }

            @Override
            public void onJsonError(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(getActivity(), R.string.msg_common_network_error);
            }
        };

        hh.start(getActivity(), ApiManager.HttpApi.SaveTeeTime, params);

    }

    @Override
    protected void reShowWithBackValue() {
        Bundle bundle = getReturnValues();
        if (bundle != null) {

            String isOk = bundle.getString("isOk", Constants.STR_EMPTY);
            if (Utils.isStringNotNullOrEmpty(isOk)) {
                try {
                    doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }


    }


    interface ListViewItemClickListener {

        public void clickRight(PagerItem.ListViewItem item);

        public void clickLeft(PagerItem.ListViewItem item);

        public void selected();

        public void cancel();
    }


    interface BottomClickListener {

        public void clickMember();

        public void clickPrime();

        public boolean clearAll();

        public boolean clear();

    }

    class BottomLayout extends LinearLayout {

        private IteeTextView memberBtn;
        private IteeTextView primeBtn;

        private IteeTextView clearBtn;
        private BottomClickListener bottomClickListener;

        public BottomLayout(Context context, final BottomClickListener bottomClickListener) {
            super(context);
            this.setOrientation(VERTICAL);
            RelativeLayout.LayoutParams myParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(120));
            myParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            this.setLayoutParams(myParams);
            this.setBackgroundColor(getColor(R.color.common_white));

            this.bottomClickListener = bottomClickListener;
            View line = AppUtils.getSeparatorLine(getContext());
            line.setBackgroundColor(getColor(R.color.common_blue));
            this.addView(line);
            line.getLayoutParams().height = getActualWidthOnThisDevice(6);


            LinearLayout downLayout = new LinearLayout(getContext());
            downLayout.setGravity(Gravity.CENTER);
            this.addView(downLayout);
            downLayout.setOrientation(HORIZONTAL);

            LinearLayout.LayoutParams r1Params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            LinearLayout.LayoutParams r2Params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            LinearLayout.LayoutParams r3Params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            RelativeLayout.LayoutParams startBtnParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            RelativeLayout.LayoutParams transferBtnParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            RelativeLayout.LayoutParams clearBtnParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            clearBtnParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            startBtnParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            transferBtnParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            RelativeLayout r1 = new RelativeLayout(getContext());
            RelativeLayout r2 = new RelativeLayout(getContext());
            RelativeLayout r3 = new RelativeLayout(getContext());
            r1.setPadding(getActualWidthOnThisDevice(10), getActualHeightOnThisDevice(13), getActualWidthOnThisDevice(10), getActualHeightOnThisDevice(15));
            r2.setPadding(getActualWidthOnThisDevice(10), getActualHeightOnThisDevice(13), getActualWidthOnThisDevice(10), getActualHeightOnThisDevice(13));
            r3.setPadding(getActualWidthOnThisDevice(10), getActualHeightOnThisDevice(13), getActualWidthOnThisDevice(10), getActualHeightOnThisDevice(13));
            r1Params.weight = 1;
            r2Params.weight = 1;
            r3Params.weight = 1;
            r3.setLayoutParams(r3Params);
            memberBtn = new IteeTextView(context);
            memberBtn.setLayoutParams(startBtnParams);
            memberBtn.setTextSize(Constants.FONT_SIZE_LARGER);
            memberBtn.setTextColor(getColor(R.color.common_white));
            memberBtn.setEnabled(false);
            r1.setLayoutParams(r1Params);

            memberBtn.setText(getString(R.string.member_only));
            memberBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomClickListener.clickMember();

                }
            });
            memberBtn.setBackgroundResource(R.drawable.bg_gray_btn);
            memberBtn.setGravity(Gravity.CENTER);
            primeBtn = new IteeTextView(context);
            primeBtn.setLayoutParams(transferBtnParams);
            primeBtn.setTextSize(Constants.FONT_SIZE_LARGER);
            r2.setLayoutParams(r2Params);
            primeBtn.setText(getString(R.string.prime_time));
            primeBtn.setGravity(Gravity.CENTER);
            primeBtn.setBackgroundResource(R.drawable.bg_gray_btn);
            primeBtn.setEnabled(false);
            primeBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomClickListener.clickPrime();
                }
            });
            primeBtn.setTextColor(getColor(R.color.common_white));
            clearBtn = new IteeTextView(context);
            clearBtn.setLayoutParams(clearBtnParams);
            r3.setLayoutParams(r3Params);
            clearBtn.setText(getString(R.string.custom_clear_all));
            clearBtn.setGravity(Gravity.CENTER);
            clearBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getString(R.string.custom_clear_all).equals(clearBtn.getText().toString())) {
                        bottomClickListener.clearAll();
                    } else {
                        bottomClickListener.clear();
                        clearBtn.setText(getString(R.string.custom_clear_all));
                    }
                }
            });
            clearBtn.setTextColor(getColor(R.color.common_white));
            clearBtn.setBackgroundResource(R.drawable.bg_green_btn);
            clearBtn.setTextSize(Constants.FONT_SIZE_LARGER);

            r1.addView(memberBtn);
            r2.addView(primeBtn);
            r3.addView(clearBtn);

            memberBtn.setTextSize(Constants.FONT_SIZE_SMALLER);
            primeBtn.setTextSize(Constants.FONT_SIZE_SMALLER);

            clearBtn.setTextSize(Constants.FONT_SIZE_SMALLER);
            downLayout.addView(r1);
            downLayout.addView(r2);
            downLayout.addView(r3);
        }

        public IteeTextView getClearBtn() {
            return clearBtn;
        }

        public void setClearBtn(IteeTextView clearBtn) {
            this.clearBtn = clearBtn;
        }

        public IteeTextView getMemberBtn() {
            return memberBtn;
        }

        public void setMemberBtn(IteeTextView memberBtn) {
            this.memberBtn = memberBtn;
        }

        public IteeTextView getPrimeBtn() {
            return primeBtn;
        }

        public void setPrimeBtn(IteeTextView primeBtn) {
            this.primeBtn = primeBtn;
        }
    }

    public class PagerItem extends LinearLayout {

        public static final String PAGE_ITEM_START_SELECT = "2";
        public static final String PAGE_ITEM_SELECTED = "3";
        public static final String PAGE_ITEM_NORMAL = "1";
        private List<JsonSegmentSettingDataGet.PageData> mPageDataList;
        private String pageStatus;
        private int leftStartIndex;
        private int rightStartIndex;
        private String startHolesId;
        private JsonSegmentSettingDataGet.PageData mPageData;
        private ListView body;
        private Context mContext;
        // private String status;
        private ListViewAdapter listViewAdapter;
        public PagerItem(Context context, JsonSegmentSettingDataGet.PageData pageData) {
            super(context);

            pageStatus = PAGE_ITEM_NORMAL;

            leftStartIndex = 0;
            this.setOrientation(VERTICAL);
            this.mPageData = pageData;
            mContext = context;
            LinearLayout.LayoutParams bodyParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            body = new ListView(context);
            body.setDivider(null);
            body.setDividerHeight(0);

            body.setLayoutParams(bodyParams);
            listViewAdapter = new ListViewAdapter();
            body.setAdapter(listViewAdapter);
            ListViewItem titleLayout = new ListViewItem(mContext, null, getActualHeightOnThisDevice(60));

            titleLayout.setTitle(pageData.getLeftName(), pageData.getRightName());
            this.addView(titleLayout);
            this.addView(body);
        }

        public void setPageListData(List<JsonSegmentSettingDataGet.PageData> data) {

            mPageDataList = data;
        }

        public JsonSegmentSettingDataGet.PageData getPageData() {
            return mPageData;
        }

        public ListViewAdapter getListViewAdapter() {
            return listViewAdapter;
        }

        public void setListViewAdapter(ListViewAdapter listViewAdapter) {
            this.listViewAdapter = listViewAdapter;
        }

        public void clearData(int type) {//1 member 2 p

            for (JsonSegmentSettingDataGet.PageData pageData : mPageDataList) {
                for (JsonSegmentSettingDataGet.LineItemData lineItemData : pageData.getDataList()) {

                    if (type == 1 && JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(lineItemData.getRightData().getStatus())) {
                        lineItemData.getRightData().setAllReserveMember("");
                        lineItemData.getRightData().setFrequenter("");
                    }


                    if (type == 1 && JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(lineItemData.getLeftData().getStatus())) {
                        lineItemData.getLeftData().setAllReserveMember("");
                        lineItemData.getLeftData().setFrequenter("");

                    }


                    if (type == 2 && JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(lineItemData.getRightData().getStatus())) {
                        lineItemData.getRightData().setPrimeFlag("");

                    }


                    if (type == 2 && JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(lineItemData.getLeftData().getStatus())) {
                        lineItemData.getLeftData().setPrimeFlag("");

                    }


                }

            }

            listViewAdapter.notifyDataSetChanged();
        }

        public boolean clickStart(int type, boolean titleChecked) {//1 member 2 p
            bottomLayout.getMemberBtn().setText(getString(R.string.member_only));
            bottomLayout.getPrimeBtn().setText(getString(R.string.prime_time));

            boolean res = false;
            for (JsonSegmentSettingDataGet.PageData pageData : mPageDataList) {
                for (JsonSegmentSettingDataGet.LineItemData lineItemData : pageData.getDataList()) {
                    if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(lineItemData.getLeftData().getStatus())) {

                        lineItemData.getLeftData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);

                        if (Utils.isStringNotNullOrEmpty(lineItemData.getLeftData().getNo())) {
                            res = true;

                            if (type == 1) {
                                String members_l = "";
                                for (JsonGetGuestTypeList.GuestTypeItem item : memberTypeList) {
                                    if (item.isChecked()) {
                                        members_l = members_l + item.getId() + ",";
                                    }
                                }

                                lineItemData.getLeftData().setFrequenter(members_l);
                                if (titleChecked) {
                                    lineItemData.getLeftData().setAllReserveMember("1");
                                } else {
                                    lineItemData.getLeftData().setAllReserveMember("0");
                                }

                            } else if (type == 2) {

                                if (titleChecked) {
                                    lineItemData.getLeftData().setPrimeFlag("0");
                                } else {
                                    lineItemData.getLeftData().setPrimeFlag("1");
                                }
                            }
                        }
                    }
                    if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(lineItemData.getRightData().getStatus())) {
                        lineItemData.getRightData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
                        if (Utils.isStringNotNullOrEmpty(lineItemData.getRightData().getNo())) {
                            res = true;
                            if (type == 1) {

                                String members_r = "";

                                for (JsonGetGuestTypeList.GuestTypeItem item : memberTypeList) {
                                    if (item.isChecked()) {
                                        members_r = members_r + item.getId() + ",";
                                    }
                                }


                                lineItemData.getRightData().setFrequenter(members_r);

                                if (titleChecked) {
                                    lineItemData.getRightData().setAllReserveMember("1");
                                } else {
                                    lineItemData.getRightData().setAllReserveMember("");
                                }
                            } else if (type == 2) {
                                if (titleChecked) {
                                    lineItemData.getRightData().setPrimeFlag("0");
                                } else {

                                    lineItemData.getRightData().setPrimeFlag("1");
                                }

                            }
                        }
                    }

                }

            }
            listViewAdapter.notifyDataSetChanged();
            return res;
        }


        public void set9Holes() {
            for (JsonSegmentSettingDataGet.LineItemData data : mPageData.getDataList()) {
                data.getRightData().setVal("-1");
                data.getRightData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
            }
            for (JsonSegmentSettingDataGet.LineItemData data : mPageData.getDataList()) {
                data.getLeftData().setVal("-1");
                data.getLeftData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
            }

            listViewAdapter.notifyDataSetChanged();
        }

        public void clearAll() {

//            show9Index =1;
//            showIndex = 1;

            for (JsonSegmentSettingDataGet.PageData pageData : mPageDataList) {
                for (JsonSegmentSettingDataGet.LineItemData lineItemData : pageData.getDataList()) {
                    lineItemData.getLeftData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
                    lineItemData.getLeftData().setFrequenter("");
                    lineItemData.getLeftData().setPrimeFlag("");
                    lineItemData.getLeftData().setAllReserveMember("");


                    lineItemData.getRightData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
                    lineItemData.getRightData().setFrequenter("");
                    lineItemData.getRightData().setPrimeFlag("");
                    lineItemData.getRightData().setAllReserveMember("");
                }

            }
            listViewAdapter.notifyDataSetChanged();

        }


        public void clearData() {

            for (JsonSegmentSettingDataGet.LineItemData lineItemData : mPageData.getDataList()) {
                if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(lineItemData.getLeftData().getStatus())) {
                    lineItemData.getLeftData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
                    lineItemData.getLeftData().setFrequenter("");
                    lineItemData.getLeftData().setPrimeFlag("");
                    lineItemData.getLeftData().setAllReserveMember("");
                }
                if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(lineItemData.getRightData().getStatus())) {
                    lineItemData.getRightData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
                    lineItemData.getRightData().setFrequenter("");
                    lineItemData.getRightData().setPrimeFlag("");
                    lineItemData.getRightData().setAllReserveMember("");

                }

            }

            listViewAdapter.notifyDataSetChanged();
        }


        class ListViewAdapter extends BaseAdapter {
            @Override
            public int getCount() {
                if (mPageData.getDataList() == null) {
                    return 0;
                }

                return mPageData.getDataList().size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                ListViewItem item = null;
                if (view == null) {

                    item = new ListViewItem(mContext, mPageData, getActualHeightOnThisDevice(80));
                } else {
                    item = (ListViewItem) view;

                }
                item.setListViewItemClickListener(new ListViewItemClickListener() {

                    @Override
                    public void clickRight(ListViewItem item) {
                        if (Utils.isStringNullOrEmpty(selectDate)) {
                            item.clickItem("right", rightStartIndex);
                            rightStartIndex = item.getIndex();
                        }
                    }

                    @Override
                    public void clickLeft(ListViewItem item) {
                        if (Utils.isStringNullOrEmpty(selectDate)) {

                            item.clickItem("left", leftStartIndex);
                            leftStartIndex = item.getIndex();
                        }


                    }

                    @Override
                    public void selected() {

                        ListViewAdapter.this.notifyDataSetChanged();
                    }

                    @Override
                    public void cancel() {
                        pageStatus = PAGE_ITEM_START_SELECT;
                        ListViewAdapter.this.notifyDataSetChanged();
                    }
                });
                item.reFreshLayout(i);

                return item;
            }
        }

        public class ListViewItem extends LinearLayout {

            private final int SELECTED_COLOR = getColor(R.color.bg_blue_of_1);
            private final int START_SELECT_COLOR = Color.YELLOW;
            private final int NORMAL_COLOR = R.color.common_white;
            private JsonSegmentSettingDataGet.LineItemData mLineItemData;
            private JsonSegmentSettingDataGet.PageData mPageData;
            private int index;
            private RelativeLayout leftLayout;
            private RelativeLayout rightLayout;
            private IteeTextView time;
            private IteeTextView leftOne;
            private IteeTextView leftTwo;
            private IteeTextView rightOne;
            private IteeTextView rightTwo;
            private IteeTextView rightThree;
            private ListViewItemClickListener listViewItemClickListener;
            public ListViewItem(Context context, JsonSegmentSettingDataGet.PageData pageData, int height) {
                super(context);
                setOrientation(HORIZONTAL);
                mPageData = pageData;
                LinearLayout.LayoutParams leftLayoutParams = new LinearLayout.LayoutParams((int) (getScreenWidth() * 0.4), height);

                leftLayout = new RelativeLayout(context);
                leftLayout.setLayoutParams(leftLayoutParams);
                AppUtils.addBottomSeparatorLine(leftLayout, context);

                RelativeLayout.LayoutParams leftOneParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                leftOneParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                leftOne = new IteeTextView(context);
                leftOne.setGravity(Gravity.CENTER);
                leftOne.setLayoutParams(leftOneParams);
                // leftOne.setText("1");
                leftOne.setTextColor(getColor(R.color.common_black));
                leftOne.setId(View.generateViewId());

                RelativeLayout.LayoutParams leftTwoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, height);
                leftTwoParams.addRule(RelativeLayout.RIGHT_OF, leftOne.getId());
                leftTwoParams.addRule(RelativeLayout.CENTER_VERTICAL);
                leftTwo = new IteeTextView(context);
                leftTwo.setLayoutParams(leftTwoParams);
                leftTwo.setId(View.generateViewId());
                // leftTwo.setText("M");
                leftTwo.setTextColor(getColor(R.color.common_black));
                leftOne.setTextSize(Constants.FONT_SIZE_SMALLER);
                leftTwo.setTextSize(Constants.FONT_SIZE_SMALLER);

                leftLayout.addView(leftOne);
                leftLayout.addView(leftTwo);


                leftLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listViewItemClickListener != null) {
                            listViewItemClickListener.clickLeft(ListViewItem.this);
                        }

                    }
                });

                LinearLayout.LayoutParams timeLayoutParams = new LinearLayout.LayoutParams((int) (getScreenWidth() * 0.2), height);

                RelativeLayout timeLayout = new RelativeLayout(context);
                timeLayout.setLayoutParams(timeLayoutParams);
                RelativeLayout.LayoutParams tvTimeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
                time = new IteeTextView(context);
                time.setLayoutParams(tvTimeParams);
                time.setTextSize(Constants.FONT_SIZE_SMALLER);
                time.setText("Time");
                time.setGravity(Gravity.CENTER);
                time.setTextColor(getColor(R.color.common_black));
                time.setBackgroundColor(getColor(R.color.common_light_gray));
                time.setSingleLine(false);
                timeLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                timeLayout.addView(time);
                AppUtils.addBottomSeparatorLine(timeLayout, context);
                timeLayout.addView(AppUtils.getVerticalLine(context, 2));
                View v = AppUtils.getVerticalLine(context, 2);
                timeLayout.addView(v);
                RelativeLayout.LayoutParams vParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                vParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);


                LinearLayout.LayoutParams rightLayoutParams = new LinearLayout.LayoutParams((int) (getScreenWidth() * 0.4), LayoutParams.MATCH_PARENT);

                rightLayout = new RelativeLayout(context);
                rightLayout.setLayoutParams(rightLayoutParams);
                AppUtils.addBottomSeparatorLine(rightLayout, context);

                RelativeLayout.LayoutParams rightOneParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                rightOneParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                rightOne = new IteeTextView(context);
                rightOne.setLayoutParams(rightOneParams);
                // rightOne.setText("1");
                rightOne.setTextColor(getColor(R.color.common_black));
                rightOne.setId(View.generateViewId());
                rightOne.setTextSize(Constants.FONT_SIZE_SMALLER);
                rightOne.setGravity(Gravity.CENTER);

                RelativeLayout.LayoutParams rightTwoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, height);
                rightTwoParams.addRule(RelativeLayout.RIGHT_OF, rightOne.getId());
                rightTwoParams.addRule(RelativeLayout.CENTER_VERTICAL);
                rightTwo = new IteeTextView(context);
                rightTwo.setTextSize(Constants.FONT_SIZE_SMALLER);
                rightTwo.setLayoutParams(rightTwoParams);
                // rightTwo.setText("M");
                rightTwo.setTextColor(getColor(R.color.common_black));
                rightLayout.addView(rightOne);
                rightLayout.addView(rightTwo);

                rightLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listViewItemClickListener != null) {
                            listViewItemClickListener.clickRight(ListViewItem.this);
                        }
                    }
                });

                this.addView(leftLayout);
                this.addView(timeLayout);
                this.addView(rightLayout);


            }

            public JsonSegmentSettingDataGet.PageData getPageData() {

                return mPageData;
            }

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public ListViewItemClickListener getListViewItemClickListener() {
                return listViewItemClickListener;
            }

            public void setListViewItemClickListener(ListViewItemClickListener listViewItemClickListener) {
                this.listViewItemClickListener = listViewItemClickListener;
            }

            private void setTitle(String left, String right) {
                if (right != null) {
                    rightOne.setText(right);
                }
                leftOne.setText(left);
            }

            // start  清除状态
            private void clearStatus() {

                for (JsonSegmentSettingDataGet.PageData pageData : mPageDataList) {
                    for (JsonSegmentSettingDataGet.LineItemData lineItemData : pageData.getDataList()) {
                        lineItemData.getRightData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
                        lineItemData.getLeftData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
                    }
                }

            }


            public void clickItem(String btnName, int startIndex) {
                JsonSegmentSettingDataGet.ItemData itemData = null;
                if ("left".equals(btnName)) {
                    itemData = mLineItemData.getLeftData();
                } else {
                    itemData = mLineItemData.getRightData();
                }

                bottomLayout.getMemberBtn().setText(getString(R.string.member_only));
                bottomLayout.getPrimeBtn().setText(getString(R.string.prime_time));


                if (itemData.isClickEnabled()) {

                    bottomLayout.getClearBtn().setText(getString(R.string.custom_clear_all));
                    if (Utils.isStringNotNullOrEmpty(itemData.getFrequenter())) {

                        bottomLayout.getClearBtn().setText(getString(R.string.custom_clear));
                    }

                    if (Utils.isStringNotNullOrEmpty(itemData.getPrimeFlag())) {

                        bottomLayout.getClearBtn().setText(getString(R.string.custom_clear));
                    }


//                    bottomLayout.getMemberBtn().setTextColor(getColor(R.color.common_gray));
//                    bottomLayout.getPrimeBtn().setTextColor(getColor(R.color.common_gray));

                    bottomLayout.getMemberBtn().setBackgroundResource(R.drawable.bg_gray_btn);
                    bottomLayout.getPrimeBtn().setBackgroundResource(R.drawable.bg_gray_btn);

                    bottomLayout.getMemberBtn().setEnabled(false);

                    bottomLayout.getPrimeBtn().setEnabled(false);
                    //设定好  直接 全选
                    //bottom btn 状态


                    String selectNo = itemData.getNo();

                    if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(itemData.getStatus())) {//蓝色  状态 取消
                        // itemData.setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
                        clearStatus();
                        listViewItemClickListener.cancel();
                    } else {
                        //点击 是否变黄
                        boolean changeYellow = true;
                        if ("left".equals(btnName)) {
                            for (JsonSegmentSettingDataGet.LineItemData lineItemData : mPageData.getDataList()) {
                                if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_START_SELECT.equals(lineItemData.getLeftData().getStatus())) {
                                    changeYellow = false;
                                    break;
                                }

                            }
                        } else {
                            for (JsonSegmentSettingDataGet.LineItemData lineItemData : mPageData.getDataList()) {
                                if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_START_SELECT.equals(lineItemData.getRightData().getStatus())) {

                                    changeYellow = false;
                                    break;
                                }
                            }
                        }


                        if (changeYellow) {//默认状态  点击 变黄
                            clearStatus();
                            itemData.setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_START_SELECT);
                            bottomLayout.getClearBtn().setText(getString(R.string.custom_clear_all));
                            listViewItemClickListener.selected();
                        }
                        if (!changeYellow) {//黄色 点击 变蓝
                            setItemStatus(startIndex, index, JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED, btnName);

                            listViewItemClickListener.selected();
                        }
                    }

                }


//                }


            }

            private void setItemStatus(int a, int b, String status, String btnName) {

                bottomLayout.getMemberBtn().setBackgroundResource(R.drawable.bg_green_btn);
                bottomLayout.getPrimeBtn().setBackgroundResource(R.drawable.bg_green_btn);

                bottomLayout.getMemberBtn().setTextColor(getColor(R.color.common_white));
                bottomLayout.getMemberBtn().setEnabled(true);
                bottomLayout.getPrimeBtn().setTextColor(getColor(R.color.common_white));
                bottomLayout.getPrimeBtn().setEnabled(true);
                if (a <= b) {
                    for (int i = a; i <= b; i++) {
                        if ("left".equals(btnName)) {
                            mPageData.getDataList().get(i).getLeftData().setStatus(status);

                            if (Utils.isStringNotNullOrEmpty(mPageData.getDataList().get(i).getLeftData().getFrequenter())) {

                                bottomLayout.getClearBtn().setText(getString(R.string.custom_clear));
                            }

                            if (Utils.isStringNotNullOrEmpty(mPageData.getDataList().get(i).getLeftData().getPrimeFlag())) {

                                bottomLayout.getClearBtn().setText(getString(R.string.custom_clear));
                            }


                        } else {
                            mPageData.getDataList().get(i).getRightData().setStatus(status);


                            if (Utils.isStringNotNullOrEmpty(mPageData.getDataList().get(i).getRightData().getFrequenter())) {

                                bottomLayout.getClearBtn().setText(getString(R.string.custom_clear));
                            }

                            if (Utils.isStringNotNullOrEmpty(mPageData.getDataList().get(i).getRightData().getPrimeFlag())) {

                                bottomLayout.getClearBtn().setText(getString(R.string.custom_clear));
                            }
                        }
                    }
                } else {
                    for (int i = b; i <= a; i++) {
                        if ("left".equals(btnName)) {
                            mPageData.getDataList().get(i).getLeftData().setStatus(status);


                            if (Utils.isStringNotNullOrEmpty(mPageData.getDataList().get(i).getLeftData().getFrequenter())) {

                                bottomLayout.getClearBtn().setText(getString(R.string.custom_clear));
                            }

                            if (Utils.isStringNotNullOrEmpty(mPageData.getDataList().get(i).getLeftData().getPrimeFlag())) {

                                bottomLayout.getClearBtn().setText(getString(R.string.custom_clear));
                            }
                        } else {
                            mPageData.getDataList().get(i).getRightData().setStatus(status);


                            if (Utils.isStringNotNullOrEmpty(mPageData.getDataList().get(i).getRightData().getFrequenter())) {

                                bottomLayout.getClearBtn().setText(getString(R.string.custom_clear));
                            }

                            if (Utils.isStringNotNullOrEmpty(mPageData.getDataList().get(i).getRightData().getPrimeFlag())) {

                                bottomLayout.getClearBtn().setText(getString(R.string.custom_clear));
                            }
                        }
                    }
                }
            }

            public void reFreshLayout(int index) {
                this.index = index;
                this.mLineItemData = mPageData.getDataList().get(index);
                leftLayout.setBackgroundColor(getColor(NORMAL_COLOR));

                rightLayout.setBackgroundColor(getColor(NORMAL_COLOR));
                String leftValue = mLineItemData.getLeftData().getNo();

                leftOne.setText("");
                leftTwo.setText("");
                rightOne.setText("");
                rightTwo.setText("");

                leftOne.setBackgroundResource(0);
                rightOne.setBackgroundResource(0);
                if ("3".equals(mLineItemData.getLeftData().getFlag())) {
                    leftValue = "9";
                    leftOne.setBackgroundResource(R.drawable.bg_black_ring);

                }
                leftOne.setText(leftValue);
                time.setText(mLineItemData.getTime());

                String mAndP = "";

                if ("3".equals(mLineItemData.getLeftData().getFlag())) {
                    mAndP = "- " + mLineItemData.getLeftData().getNo();

                }

                // leftTwo.setVisibility(View.GONE);
                if (Utils.isStringNotNullOrEmpty(mLineItemData.getLeftData().getFrequenter())) {
                    leftTwo.setVisibility(View.VISIBLE);
                    mAndP = mAndP + " M";

                }

                if (Utils.isStringNotNullOrEmpty(mLineItemData.getLeftData().getPrimeFlag())) {
                    leftTwo.setVisibility(View.VISIBLE);
                    mAndP = mAndP + " P";
                }

                leftTwo.setText(mAndP);
                //left
                if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_START_SELECT.equals(mLineItemData.getLeftData().getStatus())) {
                    leftLayout.setBackgroundColor(START_SELECT_COLOR);
                }

                if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(mLineItemData.getLeftData().getStatus())) {
                    leftLayout.setBackgroundColor(SELECTED_COLOR);
                }


                if (mLineItemData.getRightData() != null) {

                    String rightValue = mLineItemData.getRightData().getNo();
                    if ("3".equals(mLineItemData.getRightData().getFlag())) {
                        rightValue = "9";
                        rightOne.setBackgroundResource(R.drawable.bg_black_ring);

                    }
                    rightOne.setText(rightValue);

                    String mAndP_l = "";
                    if ("3".equals(mLineItemData.getRightData().getFlag())) {
                        mAndP_l = "- " + mLineItemData.getRightData().getNo();

                    }

                    //rightTwo.setVisibility(View.GONE);
                    if (Utils.isStringNotNullOrEmpty(mLineItemData.getRightData().getFrequenter())) {
                        rightTwo.setVisibility(View.VISIBLE);
                        mAndP_l = mAndP_l + " M";
                    }

                    if (Utils.isStringNotNullOrEmpty(mLineItemData.getRightData().getPrimeFlag())) {
                        rightTwo.setVisibility(View.VISIBLE);
                        mAndP_l = mAndP_l + " P";
                    }
                    rightTwo.setText(mAndP_l);
                    //right
                    if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_START_SELECT.equals(mLineItemData.getRightData().getStatus())) {
                        rightLayout.setBackgroundColor(START_SELECT_COLOR);
                    }

                    if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(mLineItemData.getRightData().getStatus())) {
                        rightLayout.setBackgroundColor(SELECTED_COLOR);
                    }

                }


            }
        }


    }

    class SelectMemberLayout extends LinearLayout {
        private ListView listView;

        private CheckSwitchButton memberCw;
        private IteeCheckBox allCheck;

        public SelectMemberLayout(Context context) {
            super(context);
            this.setOrientation(VERTICAL);

            LinearLayout.LayoutParams row1Params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));

            RelativeLayout row1 = new RelativeLayout(getBaseActivity());
            row1.setLayoutParams(row1Params);
            LinearLayout.LayoutParams row2Params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));

            RelativeLayout row2 = new RelativeLayout(getBaseActivity());
            row2.setLayoutParams(row2Params);


            memberCw = new CheckSwitchButton(getBaseActivity());
            memberCw.setChecked(true);

            IteeTextView text = new IteeTextView(getBaseActivity());
            row1.addView(text);
            row1.addView(memberCw);
            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(text, 20, getBaseActivity());
            text.setText(getString(R.string.all_members));

            LayoutUtils.setCellRightValueViewOfRelativeLayout(memberCw, 20, getBaseActivity());
            text.getLayoutParams().width = getActualWidthOnThisDevice(550);

            listView = new ListView(getBaseActivity());

            allCheck = new IteeCheckBox(TeeTimeLimitFragment.this, getActualHeightOnThisDevice(50), R.drawable.blue_check_false, R.drawable.blue_check_true);
            allCheck.setId(View.generateViewId());
            allCheck.setCheckBoxListener(new IteeCheckBox.CheckBoxListener() {
                @Override
                public void changeCheck(boolean checked) {

                    for (JsonGetGuestTypeList.GuestTypeItem item : memberTypeList) {
                        item.setChecked(checked);
                    }
                    memberListViewAdapter.notifyDataSetChanged();
                }
            });
            RelativeLayout.LayoutParams allTextParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            allTextParams.addRule(RelativeLayout.LEFT_OF, allCheck.getId());
            allTextParams.addRule(RelativeLayout.CENTER_VERTICAL);
            allTextParams.rightMargin = getActualWidthOnThisDevice(5);
            IteeTextView allText = new IteeTextView(getContext());
            allText.setLayoutParams(allTextParams);
            allText.setGravity(Gravity.CENTER_HORIZONTAL);
            allText.setText("All");


            row2.addView(allCheck);
            row2.addView(allText);


            RelativeLayout.LayoutParams aa = (RelativeLayout.LayoutParams) allCheck.getLayoutParams();
            aa.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            aa.addRule(RelativeLayout.CENTER_VERTICAL);
            aa.rightMargin = getActualWidthOnThisDevice(20);


            LinearLayout.LayoutParams listViewParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            listView.setLayoutParams(listViewParams);

            this.addView(row1);
            this.addView(getLineBlue());
            this.addView(row2);
            this.addView(getLineBlue());
            this.addView(listView);
            listView.setDivider(null);
            listView.setDividerHeight(0);
            memberListViewAdapter = new MemberListViewAdapter();
            listView.setAdapter(memberListViewAdapter);
        }

        public CheckSwitchButton getMemberCw() {
            return memberCw;
        }

        public IteeCheckBox getAllCheck() {
            return allCheck;
        }

        public ListView getListView() {
            return listView;
        }
    }

    //post api

    class MemberListViewItem extends RelativeLayout {

        private IteeTextView leftText;
        private IteeCheckBox checkBox;

        private JsonGetGuestTypeList.GuestTypeItem itemData;

        public MemberListViewItem(Context context) {
            super(context);


            ListView.LayoutParams myParams = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
            this.setLayoutParams(myParams);

            leftText = new IteeTextView(context);
            checkBox = new IteeCheckBox(TeeTimeLimitFragment.this, getActualHeightOnThisDevice(50), R.drawable.blue_check_false, R.drawable.blue_check_true);

            this.addView(leftText);
            this.addView(checkBox);


            RelativeLayout.LayoutParams checkBoxParams = (RelativeLayout.LayoutParams) checkBox.getLayoutParams();
            checkBoxParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            checkBoxParams.rightMargin = getActualWidthOnThisDevice(20);
            checkBoxParams.addRule(RelativeLayout.CENTER_VERTICAL);


            checkBox.setCheckBoxListener(new IteeCheckBox.CheckBoxListener() {
                @Override
                public void changeCheck(boolean checked) {

                    itemData.setChecked(checked);
                    selectMemberLayout.getAllCheck().setChecked(checked);


                    if (checked) {
                        for (JsonGetGuestTypeList.GuestTypeItem item : memberTypeList) {
                            if (!item.isChecked()) {
                                selectMemberLayout.getAllCheck().setChecked(false);
                                break;
                            }
                        }
                    }

                }
            });

            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(leftText, 20, getContext());

            View line = getLineBlue();
            this.addView(line);
            RelativeLayout.LayoutParams lineParams = (RelativeLayout.LayoutParams) line.getLayoutParams();

            lineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }

        public IteeTextView getLeftText() {
            return leftText;
        }

        public void setLeftText(IteeTextView leftText) {
            this.leftText = leftText;
        }

        public IteeCheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(IteeCheckBox checkBox) {
            this.checkBox = checkBox;
        }

        public void refreshLayout(JsonGetGuestTypeList.GuestTypeItem data) {

            itemData = data;
            leftText.setText(data.getName());

            checkBox.setChecked(data.isChecked());


        }
    }

    class MemberListViewAdapter extends BaseAdapter {


        @Override
        public int getCount() {

            if (memberTypeList == null) {
                return 0;
            }
            return memberTypeList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            MemberListViewItem item = null;

            if (view == null) {
                item = new MemberListViewItem(getBaseActivity());

            } else {
                item = (MemberListViewItem) view;

            }

            item.refreshLayout(memberTypeList.get(i));
            return item;
        }
    }
}
