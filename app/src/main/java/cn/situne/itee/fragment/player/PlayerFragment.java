/**
 * Project Name: itee
 * File Name:  PlayerFragment.java
 * Package Name: cn.situne.itee.fragment.player
 * Date:   2015-03-22
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonMemberProfileGet;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.SignaturePopupWindow;
import cn.situne.itee.view.popwindow.SelectConfirmMakeCallPopupWindow;
import cn.situne.itee.view.popwindow.SelectRechargeTypePopupWindow;

/**
 * ClassName:PlayerFragment <br/>
 * Function: player info page. <br/>
 * Date: 2015-03-22 <br/>
 * UI:04-8-1
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class PlayerFragment extends BaseFragment {

    private JsonMemberProfileGet dataParameter;
    private RelativeLayout playerHeaderContainer;
    private RelativeLayout playerMemberShipContainer;
    private RelativeLayout playerBalanceAccountContainer;
    private RelativeLayout playerPurchaseHistoryContainer;
    private RelativeLayout playerReservationsContainer;
    private RelativeLayout playerPastBookingContainer;
    private RelativeLayout playerSignatureContainer;
    private NetworkImageView imPlayer;

    private ImageView ivMembership, ivBalanceAccount, ivPurchaseHistory, ivReservations, ivPastBooking, ivSignature;

    private IteeTextView tvPlayerName;
    private IteeTextView tvTel;

    private IteeTextView tvTelValue;
    private IteeTextView tvEmail;

    private IteeTextView tvEmailValue;
    private IteeTextView tvMembership;
    private IteeTextView tvMembershipType;
    private IteeTextView tvMembershipTypeId;

    private IteeTextView tvBalanceAccount;
    private IteeTextView tvBalanceAccountRed;
    private IteeTextView tvBalanceAccountMoney;

    private IteeTextView tvPurchase;
    private IteeTextView tvPurchaseRed;


    private IteeTextView tvReservation;
    private IteeTextView tvPastBooking;
    private IteeTextView tvSignature;
    private String fromPage;
    private int memberId;

    private String overdueFlag;

    private ImageView overdueIcon;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("update")){
                netLinkMemberProfile();
            }
        }

    };


    @Override
    protected void reShowWithBackValue() {
//        Bundle bundle = getReturnValues();
//        if (bundle != null && bundle.getBoolean(TransKey.COMMON_REFRESH)) {
//            netLinkMemberProfile();
//        }
    }

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_player_profile;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.do_edit_administration;
    }

    @Override
    protected void initControls(View rootView) {

        playerHeaderContainer = (RelativeLayout) rootView.findViewById(R.id.player_header_container);
        playerMemberShipContainer = (RelativeLayout) rootView.findViewById(R.id.palyer_member_ship_container);
        playerBalanceAccountContainer = (RelativeLayout) rootView.findViewById(R.id.palyer_balance_account_container);
        playerPurchaseHistoryContainer = (RelativeLayout) rootView.findViewById(R.id.palyer_purchase_history_container);
        playerReservationsContainer = (RelativeLayout) rootView.findViewById(R.id.palyer_reservations_container);
        playerPastBookingContainer = (RelativeLayout) rootView.findViewById(R.id.palyer_past_booking_container);
        playerSignatureContainer = (RelativeLayout) rootView.findViewById(R.id.player_signature_container);


        imPlayer = new NetworkImageView(getActivity());

        ivMembership = new ImageView(getActivity());
        ivBalanceAccount = new ImageView(getActivity());
        ivPurchaseHistory = new ImageView(getActivity());
        ivReservations = new ImageView(getActivity());
        ivPastBooking = new ImageView(getActivity());
        ivSignature = new ImageView(getActivity());

        tvPlayerName = new IteeTextView(getActivity());
        tvTel = new IteeTextView(getActivity());

        tvTelValue = new IteeTextView(getActivity());
        tvEmail = new IteeTextView(getActivity());

        tvEmailValue = new IteeTextView(getActivity());
        tvMembership = new IteeTextView(getActivity());
        tvMembershipType = new IteeTextView(getActivity());
        tvMembershipTypeId = new IteeTextView(getActivity());

        tvBalanceAccount = new IteeTextView(getActivity());
        tvBalanceAccountRed = new IteeTextView(getActivity());
        tvBalanceAccountMoney = new IteeTextView(getActivity());

        tvPurchase = new IteeTextView(getActivity());
        tvPurchaseRed = new IteeTextView(getActivity());

        tvReservation = new IteeTextView(getActivity());
        tvPastBooking = new IteeTextView(getActivity());
        tvSignature = new IteeTextView(getActivity());

        Bundle bundle = getArguments();
        if (bundle != null) {
            memberId = bundle.getInt(TransKey.COMMON_MEMBER_ID);
            fromPage= bundle.getString(TransKey.COMMON_FROM_PAGE, StringUtils.EMPTY);
        }
        registerBoradcastReceiver();
    }

    private void initEditView() {

        final JsonMemberProfileGet.DataList dataList = dataParameter.getDataList();
        //set data to layout.
        AppUtils.showNetworkImage(imPlayer, dataList.getMemberPhoto());

        tvPlayerName.setText(dataList.getMemberName());
        tvTel.setText(getString(R.string.player_tel));

        tvTelValue.setText(dataList.getMemberTel());
        tvEmail.setText(getString(R.string.player_email));

        tvEmailValue.setText(dataList.getMemberEmail());
        tvMembership.setText(getString(R.string.player_member_ship));
        tvMembershipType.setText(dataList.getMemberType());
        tvMembershipTypeId.setText(dataList.getMemberNo());

        tvBalanceAccount.setText(getString(R.string.player_balance_account));
        if (dataList.getBalancesAccountCount() != 0) {
            tvBalanceAccountRed.setText(String.valueOf(dataList.getBalancesAccountCount()));
        } else {
            tvBalanceAccountRed.setVisibility(View.GONE);
        }
        tvBalanceAccountMoney.setText(AppUtils.getCurrentCurrency(getActivity()) + dataList.getBalancesAccount());

        tvPurchase.setText(getString(R.string.player_purchase_history));

        if (dataList.getPurchaseHistoryCount() != 0) {
            tvPurchaseRed.setText(String.valueOf(dataList.getPurchaseHistoryCount()));
        } else {
            tvPurchaseRed.setVisibility(View.GONE);
        }
        if (Constants.STR_0.equals(overdueFlag)){
            overdueIcon.setVisibility(View.GONE);
        }else {
            overdueIcon.setVisibility(View.VISIBLE);
        }
        tvReservation.setText(getString(R.string.player_reservations));
        tvPastBooking.setText(getString(R.string.player_past_booking));
        tvSignature.setText(getString(R.string.player_signature));


        tvTelValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectConfirmMakeCallPopupWindow savePopupWindow = new SelectConfirmMakeCallPopupWindow(PlayerFragment.this, null, tvTelValue.getText().toString());
                View rootView = getView();
                if (rootView != null) {
                    savePopupWindow.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
        });

        tvEmailValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] receiver = new String[]{tvEmailValue.getText().toString()};
                String myCc = "cc";
                String myBody = Constants.STR_EMPTY;
                Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);
                myIntent.setType(Constants.EMAIL_TEXT_TYPE);
                myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, receiver);
                myIntent.putExtra(android.content.Intent.EXTRA_CC, myCc);
                myIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "test");
                myIntent.putExtra(android.content.Intent.EXTRA_TEXT, myBody);
                startActivity(Intent.createChooser(myIntent, getString(R.string.create_chooser_send_mail)));
            }
        });


        playerHeaderContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEdit, getActivity())
                        && AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEditNonmember, getActivity());
                if (hasPermission) {
                    Bundle bundle = new Bundle();
                    JsonMemberProfileGet.DataList dataList = dataParameter.getDataList();
                    bundle.putInt(TransKey.COMMON_MEMBER_ID, dataList.getMemberId());
                    bundle.putSerializable("member", dataParameter);
                    bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeBrowse.value());
                    push(PlayerInfoEditFragment.class, bundle);
                } else {
                    AppUtils.showHaveNoPermission(PlayerFragment.this);
                }
            }
        });

        playerMemberShipContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                JsonMemberProfileGet.DataList dataList = dataParameter.getDataList();
                bundle.putInt("memberId", dataList.getMemberId());
                bundle.putString("memberType", dataList.getMemberType());
                bundle.putString("memberEmail", dataList.getMemberEmail());
                bundle.putInt("memberTypeId", dataList.getMemberTypeId());
                bundle.putString("memberNo", dataList.getMemberNo());
                bundle.putString("overdueFlag", dataList.getOverdueFlag());
                push(PlayerMemberShipEditFragment.class, bundle);
            }
        });


        playerBalanceAccountContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEdit, getActivity())
                        && AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEditNonmember, getActivity());
                if (hasPermission) {
                    Bundle bundle = new Bundle();
                    JsonMemberProfileGet.DataList dataList = dataParameter.getDataList();
                    bundle.putInt("memberId", dataList.getMemberId());
                    bundle.putInt("balancesAccountCount", dataList.getBalancesAccountCount());

                    push(PlayerBalanceAccountFragment.class, bundle);
                } else {
                    AppUtils.showHaveNoPermission(PlayerFragment.this);
                }
            }
        });
        playerPurchaseHistoryContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                JsonMemberProfileGet.DataList dataList = dataParameter.getDataList();
                bundle.putInt("memberId", dataList.getMemberId());
                push(PlayerPurchaseHistoryFragment.class, bundle);
            }
        });
        playerReservationsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                JsonMemberProfileGet.DataList dataList = dataParameter.getDataList();
                bundle.putInt("memberId", dataList.getMemberId());
                push(PlayerReservationsFragment.class, bundle);
            }
        });
        playerPastBookingContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                JsonMemberProfileGet.DataList dataList = dataParameter.getDataList();
                bundle.putInt("memberId", dataList.getMemberId());
                push(PlayerPastBookingFragment.class, bundle);
            }
        });
        playerSignatureContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isNotEmpty(dataList.getSignature())) {
                    SignaturePopupWindow signaturePopupWindow = new SignaturePopupWindow(getActivity(), null, dataList.getSignature());
                    signaturePopupWindow.showAtLocation(getRootView(), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                } else {
                    Utils.showShortToast(mContext, R.string.msg_no_signature);
                }
            }
        });


    }

    @Override
    protected void setDefaultValueOfControls() {

        //init layout data.
        imPlayer.setDefaultImageResId(R.drawable.member_photo);
        tvTel.setText(R.string.player_tel);
        tvEmail.setText(R.string.player_email);
        tvMembership.setText(getString(R.string.player_member_ship));
        tvBalanceAccount.setText(getString(R.string.player_balance_account));
        tvPurchase.setText(getString(R.string.player_purchase_history));
        tvReservation.setText(getString(R.string.player_reservations));
        tvPastBooking.setText(getString(R.string.player_past_booking));
        tvSignature.setText(getString(R.string.player_signature));
    }

    @Override
    protected void setListenersOfControls() {


    }

    @Override
    protected void setLayoutOfControls() {


        LinearLayout.LayoutParams rlPhotoLayout = (LinearLayout.LayoutParams) playerHeaderContainer.getLayoutParams();
        rlPhotoLayout.height = getActualHeightOnThisDevice(180);
        LinearLayout.LayoutParams rlMemberShip = (LinearLayout.LayoutParams) playerMemberShipContainer.getLayoutParams();
        rlMemberShip.height = (int) (getScreenHeight() * 0.08f);

        playerHeaderContainer.setLayoutParams(rlPhotoLayout);
        playerMemberShipContainer.setLayoutParams(rlMemberShip);
        playerBalanceAccountContainer.setLayoutParams(rlMemberShip);
        playerPurchaseHistoryContainer.setLayoutParams(rlMemberShip);
        playerReservationsContainer.setLayoutParams(rlMemberShip);
        playerPastBookingContainer.setLayoutParams(rlMemberShip);
        playerSignatureContainer.setLayoutParams(rlMemberShip);


        playerHeaderContainer.addView(imPlayer);
        RelativeLayout.LayoutParams paramsImPlayer = (RelativeLayout.LayoutParams) imPlayer.getLayoutParams();
        paramsImPlayer.width = getActualWidthOnThisDevice(160);
        paramsImPlayer.height = getActualWidthOnThisDevice(160);
        paramsImPlayer.topMargin = getActualWidthOnThisDevice(10);
        paramsImPlayer.bottomMargin = getActualWidthOnThisDevice(10);
        paramsImPlayer.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsImPlayer.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsImPlayer.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        imPlayer.setLayoutParams(paramsImPlayer);
        imPlayer.setId(View.generateViewId());

        playerHeaderContainer.addView(tvPlayerName);
        RelativeLayout.LayoutParams paramsTvCityName = (RelativeLayout.LayoutParams) tvPlayerName.getLayoutParams();
        paramsTvCityName.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvCityName.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvCityName.leftMargin = getActualWidthOnThisDevice(40);
        paramsTvCityName.addRule(RelativeLayout.ALIGN_PARENT_TOP, LAYOUT_TRUE);
        paramsTvCityName.addRule(RelativeLayout.RIGHT_OF, imPlayer.getId());
        tvPlayerName.setLayoutParams(paramsTvCityName);
        tvPlayerName.setId(View.generateViewId());

        playerHeaderContainer.addView(tvTel);
        RelativeLayout.LayoutParams paramsTvTimeZone = (RelativeLayout.LayoutParams) tvTel.getLayoutParams();
        paramsTvTimeZone.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvTimeZone.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvTimeZone.leftMargin = getActualWidthOnThisDevice(40);
        paramsTvTimeZone.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvTimeZone.addRule(RelativeLayout.RIGHT_OF, imPlayer.getId());
        tvTel.setLayoutParams(paramsTvTimeZone);
        tvTel.setId(View.generateViewId());

        playerHeaderContainer.addView(tvTelValue);
        RelativeLayout.LayoutParams paramsTvTimeZoneName = (RelativeLayout.LayoutParams) tvTelValue.getLayoutParams();
        paramsTvTimeZoneName.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvTimeZoneName.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvTimeZoneName.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvTimeZoneName.addRule(RelativeLayout.RIGHT_OF, tvTel.getId());
        paramsTvTimeZoneName.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        tvTelValue.setLayoutParams(paramsTvTimeZoneName);

        playerHeaderContainer.addView(tvEmail);
        RelativeLayout.LayoutParams paramsTvCurrency = (RelativeLayout.LayoutParams) tvEmail.getLayoutParams();
        paramsTvCurrency.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvCurrency.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvCurrency.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, LAYOUT_TRUE);
        paramsTvCurrency.leftMargin = getActualWidthOnThisDevice(40);
        paramsTvCurrency.addRule(RelativeLayout.RIGHT_OF, imPlayer.getId());
        tvEmail.setLayoutParams(paramsTvCurrency);
        tvEmail.setId(View.generateViewId());

        playerHeaderContainer.addView(tvEmailValue);
        RelativeLayout.LayoutParams paramsTvCurrencyName = (RelativeLayout.LayoutParams) tvEmailValue.getLayoutParams();
        paramsTvCurrencyName.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvCurrencyName.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvCurrencyName.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, LAYOUT_TRUE);
        paramsTvCurrencyName.addRule(RelativeLayout.RIGHT_OF, tvEmail.getId());

        paramsTvCurrencyName.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        tvEmailValue.setLayoutParams(paramsTvCurrencyName);


        playerMemberShipContainer.addView(tvMembership);
        RelativeLayout.LayoutParams paramsTvLogoutAfter = (RelativeLayout.LayoutParams) tvMembership.getLayoutParams();
        paramsTvLogoutAfter.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvLogoutAfter.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvLogoutAfter.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvLogoutAfter.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        tvMembership.setLayoutParams(paramsTvLogoutAfter);
        tvMembership.setId(View.generateViewId());



        playerMemberShipContainer.addView(ivMembership);
        RelativeLayout.LayoutParams paramsImMembership = (RelativeLayout.LayoutParams) ivMembership.getLayoutParams();
        paramsImMembership.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsImMembership.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsImMembership.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        paramsImMembership.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsImMembership.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        ivMembership.setLayoutParams(paramsImMembership);
        ivMembership.setId(View.generateViewId());

        playerMemberShipContainer.addView(tvMembershipType);
        RelativeLayout.LayoutParams paramsMembershipType = (RelativeLayout.LayoutParams) tvMembershipType.getLayoutParams();
        paramsMembershipType.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsMembershipType.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsMembershipType.addRule(RelativeLayout.ALIGN_PARENT_TOP, LAYOUT_TRUE);
        paramsMembershipType.addRule(RelativeLayout.LEFT_OF, ivMembership.getId());
        tvMembershipType.setLayoutParams(paramsMembershipType);

        playerMemberShipContainer.addView(tvMembershipTypeId);
        RelativeLayout.LayoutParams paramsTvMembershipTypeId = (RelativeLayout.LayoutParams) tvMembershipTypeId.getLayoutParams();
        paramsTvMembershipTypeId.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvMembershipTypeId.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvMembershipTypeId.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, LAYOUT_TRUE);
        paramsTvMembershipTypeId.addRule(RelativeLayout.LEFT_OF, ivMembership.getId());
        tvMembershipTypeId.setLayoutParams(paramsTvMembershipTypeId);
         overdueIcon = new ImageView(getBaseActivity());
        overdueIcon.setBackgroundResource(R.drawable.overdue_flag);

        RelativeLayout.LayoutParams overdueIconParams = new RelativeLayout.LayoutParams(getActualHeightOnThisDevice(40),getActualHeightOnThisDevice(40));
        overdueIcon.setLayoutParams(overdueIconParams);
        overdueIconParams.addRule(RelativeLayout.RIGHT_OF, tvMembership.getId());
        overdueIconParams.leftMargin = getActualWidthOnThisDevice(2);
        overdueIconParams.addRule(RelativeLayout.CENTER_VERTICAL);

        playerMemberShipContainer.addView(overdueIcon);


        playerBalanceAccountContainer.addView(tvBalanceAccount);
        RelativeLayout.LayoutParams paramsTvBalanceAccount = (RelativeLayout.LayoutParams) tvBalanceAccount.getLayoutParams();
        paramsTvBalanceAccount.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvBalanceAccount.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvBalanceAccount.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvBalanceAccount.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        tvBalanceAccount.setId(View.generateViewId());
        tvBalanceAccount.setLayoutParams(paramsTvBalanceAccount);

        playerBalanceAccountContainer.addView(tvBalanceAccountRed);
        RelativeLayout.LayoutParams paramsTvBalanceAccountRed = (RelativeLayout.LayoutParams) tvBalanceAccountRed.getLayoutParams();
        paramsTvBalanceAccountRed.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvBalanceAccountRed.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvBalanceAccountRed.addRule(RelativeLayout.RIGHT_OF, tvBalanceAccount.getId());
        paramsTvBalanceAccountRed.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvBalanceAccountRed.setLayoutParams(paramsTvBalanceAccountRed);

        playerBalanceAccountContainer.addView(ivBalanceAccount);
        RelativeLayout.LayoutParams paramsIvBalanceAccount = (RelativeLayout.LayoutParams) ivBalanceAccount.getLayoutParams();
        paramsIvBalanceAccount.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvBalanceAccount.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvBalanceAccount.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        paramsIvBalanceAccount.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIvBalanceAccount.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivBalanceAccount.setLayoutParams(paramsIvBalanceAccount);
        ivBalanceAccount.setId(View.generateViewId());

        playerBalanceAccountContainer.addView(tvBalanceAccountMoney);
        RelativeLayout.LayoutParams paramsTvBalanceAccountMoney = (RelativeLayout.LayoutParams) tvBalanceAccountMoney.getLayoutParams();
        paramsTvBalanceAccountMoney.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvBalanceAccountMoney.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvBalanceAccountMoney.addRule(RelativeLayout.LEFT_OF, ivBalanceAccount.getId());
        paramsTvBalanceAccountMoney.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvBalanceAccountMoney.setLayoutParams(paramsTvBalanceAccountMoney);

        playerPurchaseHistoryContainer.addView(tvPurchase);
        RelativeLayout.LayoutParams paramsTvPurchase = (RelativeLayout.LayoutParams) tvPurchase.getLayoutParams();
        paramsTvPurchase.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPurchase.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPurchase.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsTvPurchase.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvPurchase.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        tvPurchase.setLayoutParams(paramsTvPurchase);
        tvPurchase.setId(View.generateViewId());

        playerPurchaseHistoryContainer.addView(tvPurchaseRed);
        RelativeLayout.LayoutParams paramsTvPurchaseRed = (RelativeLayout.LayoutParams) tvPurchaseRed.getLayoutParams();
        paramsTvPurchaseRed.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPurchaseRed.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPurchaseRed.addRule(RelativeLayout.RIGHT_OF, tvPurchase.getId());
        paramsTvPurchaseRed.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvPurchaseRed.setLayoutParams(paramsTvPurchaseRed);

        playerPurchaseHistoryContainer.addView(ivPurchaseHistory);
        RelativeLayout.LayoutParams paramsIvPurchaseHistory = (RelativeLayout.LayoutParams) ivPurchaseHistory.getLayoutParams();
        paramsIvPurchaseHistory.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvPurchaseHistory.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvPurchaseHistory.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        paramsIvPurchaseHistory.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIvPurchaseHistory.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivPurchaseHistory.setLayoutParams(paramsIvPurchaseHistory);

        playerReservationsContainer.addView(tvReservation);
        RelativeLayout.LayoutParams paramsTvReservation = (RelativeLayout.LayoutParams) tvReservation.getLayoutParams();
        paramsTvReservation.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvReservation.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvReservation.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsTvReservation.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvReservation.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        tvReservation.setLayoutParams(paramsTvReservation);

        playerReservationsContainer.addView(ivReservations);
        RelativeLayout.LayoutParams paramsIvReservations = (RelativeLayout.LayoutParams) ivReservations.getLayoutParams();
        paramsIvReservations.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvReservations.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvReservations.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        paramsIvReservations.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIvReservations.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivReservations.setLayoutParams(paramsIvReservations);

        playerPastBookingContainer.addView(tvPastBooking);
        RelativeLayout.LayoutParams paramsTvPastBooking = (RelativeLayout.LayoutParams) tvPastBooking.getLayoutParams();
        paramsTvPastBooking.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPastBooking.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPastBooking.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsTvPastBooking.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvPastBooking.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        tvPastBooking.setLayoutParams(paramsTvPastBooking);

        playerPastBookingContainer.addView(ivPastBooking);
        RelativeLayout.LayoutParams paramsIvPastBooking = (RelativeLayout.LayoutParams) ivPastBooking.getLayoutParams();
        paramsIvPastBooking.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvPastBooking.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvPastBooking.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        paramsIvPastBooking.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIvPastBooking.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivPastBooking.setLayoutParams(paramsIvPastBooking);


        playerSignatureContainer.addView(tvSignature);
        RelativeLayout.LayoutParams paramsTvSignature = (RelativeLayout.LayoutParams) tvSignature.getLayoutParams();
        paramsTvSignature.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvSignature.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvSignature.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsTvSignature.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvSignature.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        tvSignature.setLayoutParams(paramsTvSignature);


        playerSignatureContainer.addView(ivSignature);
        RelativeLayout.LayoutParams paramsIvSignature = (RelativeLayout.LayoutParams) ivSignature.getLayoutParams();
        paramsIvSignature.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvSignature.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvSignature.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        paramsIvSignature.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIvSignature.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivSignature.setLayoutParams(paramsIvSignature);
    }

    @Override
    protected void setPropertyOfControls() {

        tvPlayerName.setTextSize(Constants.FONT_SIZE_MORE_LARGER);
        ivMembership.setBackground(getResources().getDrawable(R.drawable.icon_right_arrow));
        ivBalanceAccount.setBackground(getResources().getDrawable(R.drawable.icon_right_arrow));
        ivPurchaseHistory.setBackground(getResources().getDrawable(R.drawable.icon_right_arrow));
        ivReservations.setBackground(getResources().getDrawable(R.drawable.icon_right_arrow));
        ivPastBooking.setBackground(getResources().getDrawable(R.drawable.icon_right_arrow));
        ivSignature.setBackground(getResources().getDrawable(R.drawable.icon_right_arrow));

        tvBalanceAccountRed.setBackground(getResources().getDrawable(R.drawable.bg_red_circle_location));
        tvBalanceAccountRed.setGravity(Gravity.CENTER);
        tvPurchaseRed.setBackground(getResources().getDrawable(R.drawable.bg_red_circle_location));
        tvPurchaseRed.setGravity(Gravity.CENTER);

        tvPurchaseRed.setTextColor(getColor(R.color.common_white));
        tvBalanceAccountRed.setTextColor(getColor(R.color.common_white));
//
//        tvMembership.setTextSize(20);
//        tvBalanceAccount.setTextSize(20);
//        tvPurchase.setTextSize(20);
//        tvReservation.setTextSize(20);
//        tvPastBooking.setTextSize(20);
//        tvSignature.setTextSize(20);


        tvMembership.setTextColor(getColor(R.color.common_black));
        tvBalanceAccount.setTextColor(getColor(R.color.common_black));
        tvPurchase.setTextColor(getColor(R.color.common_black));
        tvReservation.setTextColor(getColor(R.color.common_black));
        tvPastBooking.setTextColor(getColor(R.color.common_black));
        tvSignature.setTextColor(getColor(R.color.common_black));


        tvTelValue.setTextColor(getColor(R.color.common_blue));
        tvEmailValue.setTextColor(getColor(R.color.common_blue));
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getResources().getString(R.string.player_profile));
        getTvRight().setVisibility(View.VISIBLE);
        getTvRight().setText(getString(R.string.recharge));

        View.OnClickListener listenerRecharge = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //if (Constants.STR_0.equals(overdueFlag)){

                    boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlRecharge, getActivity());
                    if (hasPermission) {
                        View rootView = getView();
                        SelectRechargeTypePopupWindow menuWindow = new SelectRechargeTypePopupWindow(PlayerFragment.this, dataParameter.getDataList().getMemberId());
                        if (rootView != null) {
                            menuWindow.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        }
                    } else {
                        AppUtils.showHaveNoPermission(PlayerFragment.this);
                    }

                //}else{

                   // Utils.showShortToast(getBaseActivity(),getString(R.string.overdue_mes));
               // }


            }
        };

        getTvRight().setOnClickListener(listenerRecharge);

        setOnBackListener(new OnPopBackListener() {
            @Override
            public boolean preDoBack() {
                setOnBackListener(null);
             getActivity(). unregisterReceiver(mBroadcastReceiver);
                Bundle bundle = new Bundle();

                try {
                    doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        netLinkMemberProfile();
    }

    private void netLinkMemberProfile() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.PLAYER_MEMBER_ID, String.valueOf(memberId));

        HttpManager<JsonMemberProfileGet> hh = new HttpManager<JsonMemberProfileGet>(this) {

            @Override
            public void onJsonSuccess(JsonMemberProfileGet jo) {
                dataParameter = jo;
                overdueFlag = jo.getDataList().getOverdueFlag();
                initEditView();
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.MemberProfile, params);
    }

    public void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("update");
        //注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }


}
