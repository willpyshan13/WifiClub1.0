/**
 * Project Name: itee
 * File Name:	 AgentRechargeFragment.java
 * Package Name: cn.situne.itee.fragment
 * Date:		 2015-03-22
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.agents;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.player.PlayerPastBookingFragment;
import cn.situne.itee.fragment.player.PlayerPurchaseHistoryFragment;
import cn.situne.itee.fragment.player.PlayerReservationsFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonAgents;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.SelectPayForTypePopupWindow;

/**
 * ClassName:AgentRechargeFragment <br/>
 * Function: recharge page. <br/>
 * Date: 2015-03-22 <br/>
 * UI:10-2
 *
 * @author qiushuai
 * @version 1.0
 * @see
 */
public class AgentsRechargeFragment extends BaseFragment {

    private RelativeLayout rlUserMessage;
    private RelativeLayout rlPricingTable;

    private RelativeLayout rlOpenTeeTimes;
    private RelativeLayout rlBalance;
    private RelativeLayout rlHistory;
    private RelativeLayout rlReservations;
    private RelativeLayout rlPastBooking;
    private RelativeLayout rlUsername;

    private SelectPayForTypePopupWindow popupWindow;

    private IteeTextView tvAgent;
    private IteeTextView tvAgentTel;
    private IteeTextView tvAgentEmail;
    private IteeTextView tvPricingTable;
    private ImageView ivPTRightIcon;
    private IteeTextView tvBalanceKey;
    private IteeTextView tvBalanceValue;
    private ImageView ivBlRightIcon;
    private IteeTextView tvHistory;
    private IteeTextView ivHRedBollIcon;
    private ImageView ivHRightIcon;
    private IteeTextView tvReservations;
    private IteeTextView ivRRedBollIcon;
    private ImageView ivRRightIcon;
    private IteeTextView tvPastBooking;
    private ImageView ivPBRightIcon;
    private IteeTextView tvUserNameKey;
    private IteeTextView tvUserNameValue;

    private String agentName;
    private String agentAccount;
    private String balanceAccount;
    private String agentEmail;
    private String agentMobile;
    private String passWord;

    private String name;
    private Integer agentId;


    private IteeTextView tvBalanceAccountRed;
    private IteeTextView tvPurchaseRed;

    @Override
    protected int getFragmentId() {
        Log.d("AgentsRechargeFragment", "? page");
        return R.layout.fragment_agent_recharge;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        rlUserMessage = (RelativeLayout) rootView.findViewById(R.id.rl_user_message);
        rlPricingTable = (RelativeLayout) rootView.findViewById(R.id.rl_pricing_table);

        rlOpenTeeTimes = (RelativeLayout) rootView.findViewById(R.id.rl_open_tee_times);

        rlBalance = (RelativeLayout) rootView.findViewById(R.id.rl_balance);
        rlHistory = (RelativeLayout) rootView.findViewById(R.id.rl_history);
        rlReservations = (RelativeLayout) rootView.findViewById(R.id.rl_reservations);
        rlPastBooking = (RelativeLayout) rootView.findViewById(R.id.rl_past_booking);
        rlUsername = (RelativeLayout) rootView.findViewById(R.id.rl_username);
        tvAgent = new IteeTextView(getActivity());
        tvAgentTel = new IteeTextView(getActivity());
        tvAgentEmail = new IteeTextView(getActivity());
        tvPricingTable = new IteeTextView(getActivity());
        ivPTRightIcon = new ImageView(getActivity());
        tvBalanceKey = new IteeTextView(getActivity());
        tvBalanceValue = new IteeTextView(getActivity());
        ivBlRightIcon = new ImageView(getActivity());
        tvHistory = new IteeTextView(getActivity());
        ivHRedBollIcon = new IteeTextView(getActivity());
        ivHRightIcon = new ImageView(getActivity());
        tvReservations = new IteeTextView(getActivity());
        ivRRedBollIcon = new IteeTextView(getActivity());
        ivRRightIcon = new ImageView(getActivity());
        tvPastBooking = new IteeTextView(getActivity());
        ivPBRightIcon = new ImageView(getActivity());
        tvUserNameKey = new IteeTextView(getActivity());
        tvUserNameValue = new IteeTextView(getActivity());
        tvBalanceAccountRed = new IteeTextView(getActivity());
        tvPurchaseRed = new IteeTextView(getActivity());
        tvBalanceAccountRed.setVisibility(View.GONE);
        tvPurchaseRed.setVisibility(View.GONE);


        tvBalanceAccountRed.setBackground(getResources().getDrawable(R.drawable.bg_red_circle_location));
        tvBalanceAccountRed.setGravity(Gravity.CENTER);

        tvPurchaseRed.setBackground(getResources().getDrawable(R.drawable.bg_red_circle_location));
        tvPurchaseRed.setGravity(Gravity.CENTER);
        tvPurchaseRed.setTextColor(getColor(R.color.common_white));
        tvBalanceAccountRed.setTextColor(getColor(R.color.common_white));
    }

    @Override
    protected void setDefaultValueOfControls() {

        LayoutUtils.setLayoutHeight(rlUserMessage, 120, mContext);
        LayoutUtils.setLayoutHeight(rlPricingTable, 100, mContext);
        LayoutUtils.setLayoutHeight(rlOpenTeeTimes, 100, mContext);


        LayoutUtils.setLayoutHeight(rlBalance, 100, mContext);
        LayoutUtils.setLayoutHeight(rlHistory, 100, mContext);
        LayoutUtils.setLayoutHeight(rlReservations, 100, mContext);
        LayoutUtils.setLayoutHeight(rlPastBooking, 100, mContext);
        LayoutUtils.setLayoutHeight(rlUsername, 100, mContext);

        rlUserMessage.addView(tvAgentTel);
        RelativeLayout.LayoutParams paramsTvAgentTel = (RelativeLayout.LayoutParams) tvAgentTel.getLayoutParams();
        paramsTvAgentTel.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAgentTel.height = (int) (getScreenWidth() * 0.4);
        paramsTvAgentTel.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsTvAgentTel.leftMargin = getActualWidthOnThisDevice(40);
        paramsTvAgentTel.topMargin = getActualHeightOnThisDevice(60);
        tvAgentTel.setLayoutParams(paramsTvAgentTel);

        rlUserMessage.addView(tvAgentEmail);
        RelativeLayout.LayoutParams paramsTvAgentEmail = (RelativeLayout.LayoutParams) tvAgentEmail.getLayoutParams();
        paramsTvAgentEmail.width = (int) (getScreenWidth() * 0.4);
        paramsTvAgentEmail.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAgentEmail.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvAgentEmail.rightMargin = getActualWidthOnThisDevice(40);
        paramsTvAgentEmail.topMargin = getActualHeightOnThisDevice(60);
        tvAgentEmail.setLayoutParams(paramsTvAgentEmail);

        rlUserMessage.addView(tvAgent);
        rlUserMessage.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
        RelativeLayout.LayoutParams paramsTvAgent = (RelativeLayout.LayoutParams) tvAgent.getLayoutParams();
        paramsTvAgent.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAgent.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAgent.leftMargin = getActualWidthOnThisDevice(40);
        paramsTvAgent.topMargin = getActualHeightOnThisDevice(10);
        tvAgent.setLayoutParams(paramsTvAgent);


        rlPricingTable.addView(tvPricingTable);
        RelativeLayout.LayoutParams paramsTvPricingTable = (RelativeLayout.LayoutParams) tvPricingTable.getLayoutParams();
        paramsTvPricingTable.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPricingTable.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPricingTable.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsTvPricingTable.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvPricingTable.leftMargin = getActualWidthOnThisDevice(40);
        tvPricingTable.setLayoutParams(paramsTvPricingTable);

        rlPricingTable.addView(ivPTRightIcon);
        rlPricingTable.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
        LayoutUtils.setRightArrow(ivPTRightIcon, mContext);



        IteeTextView tvOpenTimes = new IteeTextView(getBaseActivity());
        tvOpenTimes.setText(getString(R.string.agents_open_tee_times));
        rlOpenTeeTimes.addView(tvOpenTimes);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvOpenTimes, 40, getBaseActivity());

        ImageView openIcon = new ImageView(getBaseActivity());
        openIcon.setBackgroundResource(R.drawable.icon_right_arrow);
        rlOpenTeeTimes.addView(openIcon);
        rlOpenTeeTimes.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
        LayoutUtils.setRightArrow(openIcon, mContext);
        rlOpenTeeTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(TransKey.AGENTS_AGENT_ID, String.valueOf(agentId));
                push(AgentsOpenTeeTimesFragment.class, bundle);
            }
        });


        rlBalance.addView(tvBalanceKey);
        RelativeLayout.LayoutParams paramsTvBalanceKey = (RelativeLayout.LayoutParams) tvBalanceKey.getLayoutParams();
        paramsTvBalanceKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvBalanceKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvBalanceKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsTvBalanceKey.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvBalanceKey.leftMargin = getActualWidthOnThisDevice(40);
        tvBalanceKey.setLayoutParams(paramsTvBalanceKey);


        RelativeLayout.LayoutParams tvBalanceAccountRedParams
                = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        tvBalanceAccountRedParams.addRule(RelativeLayout.RIGHT_OF, tvBalanceKey.getId());
        tvBalanceAccountRedParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvBalanceAccountRed.setLayoutParams(tvBalanceAccountRedParams);

        rlBalance.addView(tvBalanceAccountRed);

        rlBalance.addView(tvBalanceValue);
        RelativeLayout.LayoutParams paramsTvBalanceValue = (RelativeLayout.LayoutParams) tvBalanceValue.getLayoutParams();
        paramsTvBalanceValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvBalanceValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvBalanceValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvBalanceValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvBalanceValue.rightMargin = getActualWidthOnThisDevice(100);
        tvBalanceValue.setLayoutParams(paramsTvBalanceValue);

        rlBalance.addView(ivBlRightIcon);
        rlBalance.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
        LayoutUtils.setRightArrow(ivBlRightIcon, mContext);

        RelativeLayout.LayoutParams tvPurchaseRedParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        tvPurchaseRedParams.addRule(RelativeLayout.RIGHT_OF, tvHistory.getId());
        tvPurchaseRedParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvPurchaseRed.setLayoutParams(tvPurchaseRedParams);

        rlHistory.addView(tvPurchaseRed);

        rlHistory.addView(tvHistory);
        RelativeLayout.LayoutParams paramsTvHistory = (RelativeLayout.LayoutParams) tvHistory.getLayoutParams();
        paramsTvHistory.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvHistory.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvHistory.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsTvHistory.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvHistory.leftMargin = getActualWidthOnThisDevice(40);
        tvHistory.setLayoutParams(paramsTvHistory);

        rlHistory.addView(ivHRedBollIcon);
        RelativeLayout.LayoutParams paramsIvHRedBollIcon = (RelativeLayout.LayoutParams) ivHRedBollIcon.getLayoutParams();
        paramsIvHRedBollIcon.width = getActualWidthOnThisDevice(40);
        paramsIvHRedBollIcon.height = getActualHeightOnThisDevice(40);
        paramsIvHRedBollIcon.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsIvHRedBollIcon.leftMargin = getActualWidthOnThisDevice(326);
        ivHRedBollIcon.setLayoutParams(paramsIvHRedBollIcon);

        rlHistory.addView(ivHRightIcon);
        rlHistory.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
        LayoutUtils.setRightArrow(ivHRightIcon, mContext);

        rlReservations.addView(tvReservations);
        RelativeLayout.LayoutParams paramsTvReservations = (RelativeLayout.LayoutParams) tvReservations.getLayoutParams();
        paramsTvReservations.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvReservations.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvReservations.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsTvReservations.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvReservations.leftMargin = getActualWidthOnThisDevice(40);
        tvReservations.setLayoutParams(paramsTvReservations);

        rlReservations.addView(ivRRedBollIcon);
        RelativeLayout.LayoutParams paramsIvRRedBollIcon = (RelativeLayout.LayoutParams) ivRRedBollIcon.getLayoutParams();
        paramsIvRRedBollIcon.width = getActualWidthOnThisDevice(40);
        paramsIvRRedBollIcon.height = getActualWidthOnThisDevice(40);
        paramsIvRRedBollIcon.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsIvRRedBollIcon.leftMargin = getActualWidthOnThisDevice(270);
        ivRRedBollIcon.setLayoutParams(paramsIvRRedBollIcon);

        rlReservations.addView(ivRRightIcon);
        rlReservations.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
        LayoutUtils.setRightArrow(ivRRightIcon, mContext);

        rlPastBooking.addView(tvPastBooking);
        RelativeLayout.LayoutParams paramsTvPastBooking = (RelativeLayout.LayoutParams) tvPastBooking.getLayoutParams();
        paramsTvPastBooking.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPastBooking.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPastBooking.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsTvPastBooking.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvPastBooking.leftMargin = getActualWidthOnThisDevice(40);
        tvPastBooking.setLayoutParams(paramsTvPastBooking);

        rlPastBooking.addView(ivPBRightIcon);
        rlPastBooking.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
        RelativeLayout.LayoutParams paramsIvPBRightIcon = (RelativeLayout.LayoutParams) ivPBRightIcon.getLayoutParams();
        paramsIvPBRightIcon.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvPBRightIcon.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvPBRightIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIvPBRightIcon.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsIvPBRightIcon.rightMargin = getActualWidthOnThisDevice(40);
        ivPBRightIcon.setLayoutParams(paramsIvPBRightIcon);

        rlUsername.addView(tvUserNameKey);
        RelativeLayout.LayoutParams paramsTvUserNameKey = (RelativeLayout.LayoutParams) tvUserNameKey.getLayoutParams();
        paramsTvUserNameKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvUserNameKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvUserNameKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsTvUserNameKey.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvUserNameKey.leftMargin = getActualWidthOnThisDevice(40);
        tvUserNameKey.setLayoutParams(paramsTvUserNameKey);

        rlUsername.addView(tvUserNameValue);
        rlUsername.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
        RelativeLayout.LayoutParams paramsTvUserNameValue = (RelativeLayout.LayoutParams) tvUserNameValue.getLayoutParams();
        paramsTvUserNameValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvUserNameValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvUserNameValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvUserNameValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvUserNameValue.rightMargin = getActualWidthOnThisDevice(40);
        tvUserNameValue.setLayoutParams(paramsTvUserNameValue);

    }

    @Override
    protected void setListenersOfControls() {
        rlPricingTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlAgentsPricingTable, getActivity());
                if (hasPermission) {
                    Bundle bundle = new Bundle();
                    bundle.putString(TransKey.AGENTS_AGENT_NAME, name);
                    bundle.putInt(TransKey.AGENTS_AGENT_ID, agentId);
                    push(AgentsPricingTableListFragment.class, bundle);
                } else {
                    AppUtils.showHaveNoPermission(AgentsRechargeFragment.this);
                }
            }
        });

        rlUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlAgentsEdit, getActivity());
                if (hasPermission) {
                    Bundle bundle = new Bundle();
                    bundle.putString(TransKey.AGENTS_AGENT_ACCOUNT, agentAccount);
                    bundle.putInt(TransKey.AGENTS_AGENT_ID, agentId);
                    bundle.putString(TransKey.AGENTS_AGENT_PASS_WORD, passWord);
                    bundle.putString(TransKey.AGENTS_AGENT_NAME, agentName);
                    bundle.putBoolean(TransKey.EVENT_IS_ADD, true);
                    push(AgentsUserMessageFragment.class, bundle);
                } else {
                    AppUtils.showHaveNoPermission(AgentsRechargeFragment.this);
                }
            }
        });
        rlUserMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlAgentsEdit, getActivity());
                if (hasPermission) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.AGENTS_AGENT_ID, agentId);
                    bundle.putString(TransKey.AGENTS_AGENT_NAME, name);
                    bundle.putBoolean(TransKey.EVENT_IS_ADD, false);
                    push(AgentsAddEditFragment.class, bundle);
                } else {
                    AppUtils.showHaveNoPermission(AgentsRechargeFragment.this);
                }
            }
        });

        rlReservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt(TransKey.AGENTS_AGENT_ID, agentId);
                push(PlayerReservationsFragment.class, bundle);
            }
        });

        rlPastBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt(TransKey.AGENTS_AGENT_ID, agentId);
                push(PlayerPastBookingFragment.class, bundle);
            }
        });

        rlHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt(TransKey.AGENTS_AGENT_ID, agentId);
                push(PlayerPurchaseHistoryFragment.class, bundle);
            }
        });
        rlBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlAgentsEdit, getActivity());
                if (hasPermission) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.AGENTS_AGENT_ID, agentId);
                    push(AgentBalanceAccountFragment.class, bundle);
                } else {
                    AppUtils.showHaveNoPermission(AgentsRechargeFragment.this);
                }
            }
        });
    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {
        tvAgent.setTextColor(getColor(R.color.common_black));

        tvAgentTel.setTextColor(getColor(R.color.common_blue));
        tvAgentTel.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvAgentEmail.setTextColor(getColor(R.color.common_blue));
        tvAgentEmail.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvAgentEmail.setSingleLine();

        tvPricingTable.setText(R.string.agents_recharge_pricing_table);
        tvPricingTable.setTextColor(getColor(R.color.common_black));

        ivPTRightIcon.setId(View.generateViewId());
        ivPTRightIcon.setBackgroundResource(R.drawable.icon_right_arrow);
        ivPTRightIcon.setFocusableInTouchMode(true);

        tvBalanceKey.setText(R.string.agents_balance_account);
        tvBalanceKey.setTextColor(getColor(R.color.common_black));
        tvBalanceKey.setId(View.generateViewId());


        tvBalanceValue.setTextColor(getColor(R.color.common_black));

        ivBlRightIcon.setId(View.generateViewId());
        ivBlRightIcon.setBackgroundResource(R.drawable.icon_right_arrow);
        ivBlRightIcon.setFocusableInTouchMode(true);

        tvHistory.setText(R.string.agents_recharge_purchase_history);
        tvHistory.setId(View.generateViewId());

        tvHistory.setTextColor(getColor(R.color.common_black));

        ivHRightIcon.setId(View.generateViewId());
        ivHRightIcon.setBackgroundResource(R.drawable.icon_right_arrow);
        ivHRightIcon.setFocusableInTouchMode(true);

        tvReservations.setText(R.string.agents_recharge_reservations);
        tvReservations.setTextColor(getColor(R.color.common_black));

        ivRRightIcon.setId(View.generateViewId());
        ivRRightIcon.setBackgroundResource(R.drawable.icon_right_arrow);
        ivRRightIcon.setFocusableInTouchMode(true);

        tvPastBooking.setText(R.string.agents_recharge_past_booking);
        tvPastBooking.setTextColor(getColor(R.color.common_black));

        ivPBRightIcon.setId(View.generateViewId());
        ivPBRightIcon.setBackgroundResource(R.drawable.icon_right_arrow);
        ivPBRightIcon.setFocusableInTouchMode(true);

        tvUserNameKey.setText(R.string.agents_recharge_username);
        tvUserNameKey.setTextColor(getColor(R.color.common_black));

        tvUserNameValue.setTextColor(getColor(R.color.common_gray));

        Bundle bundle = getArguments();
        if (bundle != null) {
            name = (String) getArguments().get(TransKey.AGENTS_AGENT_NAME);
            agentId = bundle.getInt(TransKey.AGENTS_AGENT_ID);
        }


    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(name);
        getTvRight().setBackground(null);
        getTvRight().setText(R.string.agents_recharge);
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlAgentsRecharge, getActivity());
                if (hasPermission) {
                    popupWindow = new SelectPayForTypePopupWindow(AgentsRechargeFragment.this, agentId);
                    popupWindow.showAtLocation(getRootView().findViewById(R.id.popup_windows),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                } else {
                    AppUtils.showHaveNoPermission(AgentsRechargeFragment.this);
                }

            }
        });
    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        getAgentDetail();
    }

    private void getAgentDetail() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.AGENT_ID, String.valueOf(agentId));
        HttpManager<JsonAgents> hh = new HttpManager<JsonAgents>(AgentsRechargeFragment.this) {

            @Override
            public void onJsonSuccess(JsonAgents jo) {
                List<JsonAgents.AgentList> agentList = jo.getAgentList();
                for (int i = 0; i < agentList.size(); i++) {
                    agentName = agentList.get(i).getAgentName();
                    agentAccount = agentList.get(i).getAgentAccount();
                    balanceAccount = agentList.get(i).getAgentBalanceAccount();
                    agentEmail = agentList.get(i).getAgentEmail();
                    agentMobile = agentList.get(i).getAgentMobile();
                    passWord = agentList.get(i).getAgentPassWord();

                    if (agentList.get(i).getBalancesAccountCount() != 0) {
                        tvBalanceAccountRed.setVisibility(View.VISIBLE);
                        tvBalanceAccountRed.setText(String.valueOf(agentList.get(i).getBalancesAccountCount()));
                    } else {
                        tvBalanceAccountRed.setVisibility(View.GONE);
                    }

                    if (agentList.get(i).getPurchaseHistoryCount() != 0) {
                        tvPurchaseRed.setVisibility(View.VISIBLE);
                        tvPurchaseRed.setText(String.valueOf(agentList.get(i).getPurchaseHistoryCount()));
                    } else {
                        tvPurchaseRed.setVisibility(View.GONE);
                    }

                }


                tvAgent.setText(agentName);

                tvAgentTel.setText(agentMobile);

                tvAgentEmail.setText(agentEmail);

                if (!balanceAccount.equals(Constants.STR_0)) {
                    tvBalanceValue.setText(AppUtils.getCurrentCurrency(AgentsRechargeFragment.this.getBaseActivity()) + balanceAccount);
                }

                tvUserNameValue.setText(agentAccount);
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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.AgentsGet, params);
    }
}
