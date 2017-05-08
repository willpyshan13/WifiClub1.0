package cn.situne.itee.fragment.agents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.administration.ChooseDateFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonAgentListGet;
import cn.situne.itee.manager.jsonentity.JsonAgentsOpenTimeData;
import cn.situne.itee.manager.jsonentity.JsonAgentsOpenTimeDetailedData;
import cn.situne.itee.manager.jsonentity.JsonShoppingPaymentGet;
import cn.situne.itee.manager.jsonentity.JsonShoppingPurchaseAddReturn;
import cn.situne.itee.view.AgentsPricingTableListUpView;

/**
 * Created by luochao on 9/29/15.
 */
public class AgentsAddOpenTeeTimes extends BaseEditFragment {

    private ScrollView mBody;
    private String agentsId;
    private AgentsPricingTableListUpView upView;
    private String selectedDates;
    private String oldId;
    @Override
    protected int getFragmentId() {
        Log.d("AgentsAddOpenTeeTimes", "time page");
        return R.layout.fragment_agents_add_open_tee;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        Bundle bundle = getArguments();
        if (bundle != null){
            setFragmentMode(BaseEditFragment.FragmentMode.valueOf(bundle.getInt(TransKey.COMMON_FRAGMENT_MODE)));
            agentsId = bundle.getString(TransKey.AGENTS_AGENT_ID);
            oldId = bundle.getString(TransKey.OLDID);

            mBody = (ScrollView)rootView.findViewById(R.id.body);
            upView = new AgentsPricingTableListUpView(AgentsAddOpenTeeTimes.this, getActualHeightOnThisDevice(100), null, false);
            upView.setSelectDateOnClick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getFragmentMode() != BaseEditFragment.FragmentMode.FragmentModeBrowse) {

                        Bundle bundle = new Bundle();
                        bundle.putString(TransKey.SELECTED_DATE, selectedDates);

                        bundle.putString(TransKey.COMMON_FROM_PAGE, AgentsAddOpenTeeTimes.class.getName());
                        push(ChooseDateFragment.class, bundle);

                    }
                }
            });
            mBody.addView(upView);

            if (getFragmentMode() == BaseEditFragment.FragmentMode.FragmentModeAdd) {
                selectedDates = Utils.getHHMMSSFromMillionSecondsWithType(new Date().getTime(), Constants.DATE_FORMAT_YYYYMMDD_OBLIQUE);


                ArrayList<String> dateList = AppUtils.changeString2List(selectedDates, Constants.STR_COMMA);
                showTitle = AppUtils.getEtTimeTitle(dateList, mContext);
                upView.setDateText(showTitle);
            }

            if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {

                getOpenTimeList();
            }
        }


    }


    // API
    private void getOpenTimeList() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));

        params.put("abmid", oldId);
        HttpManager<JsonAgentsOpenTimeDetailedData> hh = new HttpManager<JsonAgentsOpenTimeDetailedData>(AgentsAddOpenTeeTimes.this) {

            @Override
            public void onJsonSuccess(JsonAgentsOpenTimeDetailedData jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    selectedDates = jo.getOpenTimesDetailed().getDate();
                    ArrayList<String> dateList = AppUtils.changeString2List(selectedDates, Constants.STR_COMMA);
                    showTitle = AppUtils.getEtTimeTitle(dateList, mContext);
                    upView.setDateText(showTitle);
                    for (JsonAgentsOpenTimeDetailedData.OpenTime openTime:jo.getOpenTimesDetailed().getOpenTimeList()){


                        upView.addTimeView(openTime.getStartTime(),openTime.getEndTimes(),"1");

                    }

                } else {
                    Utils.showShortToast(getActivity(), msg);
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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.AgentsBookingTimeDetail, params);
    }


    @Override
    protected void reShowWithBackValue() {
        super.reShowWithBackValue();
        Bundle bundle = getReturnValues();
        if (bundle != null) {
            if (ChooseDateFragment.class.getName().equals(bundle.getString(TransKey.COMMON_FROM_PAGE))) {
                selectedDates = bundle.getString(TransKey.SELECTED_DATE);
                ArrayList<String> dateList = AppUtils.changeString2List(selectedDates, Constants.STR_COMMA);
                showTitle = AppUtils.getEtTimeTitle(dateList, mContext);

                upView.setDateText(showTitle);
            }
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

        if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
            getTvLeftTitle().setText(R.string.agents_add_open_tee_times);
            getTvRight().setBackground(null);
            getTvRight().setText(R.string.common_save);
        } else {
            getTvLeftTitle().setText(R.string.agents_edit_open_tee_times);
            //getTvLeftTitle().setText(agentName);
            if (getFragmentMode() == FragmentMode.FragmentModeEdit) {
                getTvRight().setText(R.string.common_ok);
                getTvRight().setBackground(null);

            } else {
                getTvRight().setText(Constants.STR_EMPTY);
                getTvRight().setBackgroundResource(R.drawable.icon_common_edit);

            }
        }
       getTvRight().setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (getFragmentMode() == FragmentMode.FragmentModeAdd){

                   if (doCheck()){
                       addOpenTime();
                   } else{
                    Utils.showShortToast(getBaseActivity(),getString(R.string.agents_time_check));

                   }

               }


               if (getFragmentMode() == FragmentMode.FragmentModeEdit){
                   if (doCheck()) {
                       editOpenTime();
                   }else{
                       Utils.showShortToast(getBaseActivity(),getString(R.string.agents_time_check));

                   }
               }

               if (getFragmentMode() == FragmentMode.FragmentModeBrowse){
                   getTvRight().setText(R.string.common_ok);
                   getTvRight().setBackground(null);
                   setFragmentMode(FragmentMode.FragmentModeEdit);

               }
           }
       });


    }

    public  int compare_date(String date1, String date2)
    {
        DateFormat df = new SimpleDateFormat("hh:mm");
        try {
            java.util.Date d1 = df.parse(date1);
            java.util.Date d2 = df.parse(date2);
            if (d1.getTime() > d2.getTime())
            {

                return 1;
            }
            else if (d1.getTime() < d2.getTime())
            {

                return -1;
            }
            else
            {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;

    }


    private boolean doCheck(){


        ArrayList<AgentsPricingTableListUpView.Times> addTimes = upView.getTimesArray();

        for (AgentsPricingTableListUpView.Times times:addTimes){

            for (AgentsPricingTableListUpView.Times times1:addTimes){

                if (!times.equals(times1)){

                    if (compare_date(times.getStartTime(),times1.getStartTime())>=0&&compare_date(times.getStartTime(),times1.getEndTimes())<=0){
                    Utils.log("1");

                        return false;
                    }


                    if (compare_date(times.getEndTimes(),times1.getStartTime())>=0&&compare_date(times.getEndTimes(),times1.getEndTimes())<=0){
                        Utils.log("2");
                        return false;
                    }



                    if (compare_date(times.getStartTime(),times1.getStartTime())<=0&&compare_date(times.getEndTimes(),times1.getEndTimes())>=0){
                        Utils.log("3");
                        return false;
                    }
                }

            }

        }

        return true;
    }



    private String showTitle;

    private void addOpenTime() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put("agent_id", agentsId);
        params.put("date_decs", showTitle);
        params.put("time_decs", upView.getAgentDataString());
        params.put("date", selectedDates);
        params.put("time", upView.getAgentDataJsonString());
        HttpManager<JsonShoppingPurchaseAddReturn> hh = new HttpManager<JsonShoppingPurchaseAddReturn>(AgentsAddOpenTeeTimes.this) {

            @Override
            public void onJsonSuccess(JsonShoppingPurchaseAddReturn jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();

                if (returnCode == Constants.RETURN_CODE_20301) {


                    doBackWithRefresh();
                } else {
                    Utils.showShortToast(getActivity(), msg);

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

        hh.start(getActivity(), ApiManager.HttpApi.AgentsBookingTimeAdd, params);


    }

    private void editOpenTime() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put("agent_id", agentsId);
        params.put("date_decs", showTitle);
        params.put("time_decs", upView.getAgentDataString());
        params.put("date", selectedDates);
        params.put("time", upView.getAgentDataJsonString());
        params.put("oldid", oldId);
        HttpManager<JsonShoppingPurchaseAddReturn> hh = new HttpManager<JsonShoppingPurchaseAddReturn>(AgentsAddOpenTeeTimes.this) {

            @Override
            public void onJsonSuccess(JsonShoppingPurchaseAddReturn jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {


                    doBackWithRefresh();
                } else {
                    Utils.showShortToast(getActivity(), msg);

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

        hh.start(getActivity(), ApiManager.HttpApi.AgentsBookingTimeEdit, params);


    }
}
