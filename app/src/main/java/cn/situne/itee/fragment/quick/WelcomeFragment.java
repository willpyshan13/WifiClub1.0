/**
 * Project Name: itee
 * File Name:  WelcomeFragment.java
 * Package Name: cn.situne.itee.fragment.quick
 * Date:   2015-04-17
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.quick;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.test.UiThreadTest;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.activity.MainActivity;
import cn.situne.itee.activity.QuickScanQrCodeActivity;
import cn.situne.itee.adapter.QuickMenuGridViewAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.common.widget.wheel.BasePopFragment;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.player.PlayerFragment;
import cn.situne.itee.fragment.player.PlayerInfoEditFragment;
import cn.situne.itee.fragment.player.PlayerPurchaseHistoryFragment;
import cn.situne.itee.fragment.player.PlayerReservationsFragment;
import cn.situne.itee.fragment.shopping.ShoppingGoodsListFragment;
import cn.situne.itee.fragment.shopping.ShoppingPaymentFragment;
import cn.situne.itee.fragment.teetime.LocationListFragment;
import cn.situne.itee.fragment.teetime.TeeTimeAddFragment;
import cn.situne.itee.fragment.teetime.TeeTimeCheckInFragment;
import cn.situne.itee.fragment.teetime.TeeTimeChoosePriceFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonBookingDetailList;
import cn.situne.itee.manager.jsonentity.JsonCheckOut;
import cn.situne.itee.manager.jsonentity.JsonEntry;
import cn.situne.itee.manager.jsonentity.JsonNfcBindCaddieData;
import cn.situne.itee.manager.jsonentity.JsonNfcCheckCaddieData;
import cn.situne.itee.manager.jsonentity.JsonNfcCheckCardForBookingNoGet;
import cn.situne.itee.manager.jsonentity.JsonNfcCheckCardGet;
import cn.situne.itee.manager.jsonentity.jsonCheckPricingData;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.PurchasePlayersPopup;
import cn.situne.itee.view.ShoppingPurchaseItem;
import cn.situne.itee.view.popwindow.NfcSelectMemberPopup;
import cn.situne.itee.view.popwindow.SelectRechargeConfirmPopupWindow;
import cn.situne.itee.view.popwindow.SelectRechargeTypePopupWindow;
import cn.situne.itee.view.popwindow.ShoppingDeleteTimesPopupWindow;

/**
 * ClassName:WelcomeFragment <br/>
 * Function: WelcomeFragment. <br/>
 * Date: 2015-04-17 <br/>
 * UI:02-4
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class WelcomeFragment extends BaseFragment {

    public  final  static String  GRID_VIEW_BAG  = "99";
    public  final  static String  GRID_VIEW_CADDIE  = "100";





    private String nfcCardType;

    private static final int CHECK_IN = 0;
    private static final int SHOPPING = 1;
    private static final int CHECK_OUT = 2;
    private static final int RECHARGE = 3;
    private static final int PURCHASE_HISTORY = 4;
    private static final int RESERVATIONS = 5;
    private static final int CUSTOMER_INFO = 6;

    private static final String IS_PAID_ALL = Constants.STR_1;

    private RelativeLayout rlUserInfoContainer;

    private RelativeLayout rlContentContainer;


    private GridView quickMenuGridView;

    private QuickMenuGridViewAdapter adapter;

    private NetworkImageView ivPhoto;

    private IteeTextView tvMemberName;

    private IteeTextView tvMemberType;

    private IteeTextView tvBookingNo;

    private IteeTextView tvInfoTime;

    private IteeTextView tvInfoArea;

    private String code;

    private boolean isSingle;

    private String memberId;

    private String memberName;

    private String memberType;

    private String bookingOrderNo;

    private String bookingNo;

    private String infoTime;

    private String infoArea;

    private String nfcCard;
    private boolean isSellCaddie;

    private boolean isCheckCaddie;
    private boolean isOneCheckAll;


    public String getNfcCard() {
        return nfcCard;
    }

    public void setNfcCard(String nfcCard) {
        this.nfcCard = nfcCard;
    }



    private JsonEntry jsonEntry;

    private boolean isBagUnBind;
    private boolean isaCaddieUnBind;

    private PurchasePlayersPopup playersPopup;


    private NfcSelectMemberPopup selectMemberPopup;


    private  ShoppingDeleteTimesPopupWindow  confirmPopWindow;


    public boolean isBagUnBind() {
        return isBagUnBind;
    }

    public boolean isaCaddieUnBind() {
        return isaCaddieUnBind;
    }

    private JsonBookingDetailList.DataListItem.BookingListItem booking = new JsonBookingDetailList.DataListItem.BookingListItem();


    public String getNfcCardType() {
        return nfcCardType;
    }

    public void setNfcCardType(String nfcCardType) {
        this.nfcCardType = nfcCardType;
    }

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_quick_welcome;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        rlUserInfoContainer = (RelativeLayout) rootView.findViewById(R.id.rl_user_info_container);

        quickMenuGridView = (GridView) rootView.findViewById(R.id.quick_menu_grid_view);

        rlContentContainer= (RelativeLayout) rootView.findViewById(R.id.rl_content_container);

        ivPhoto = new NetworkImageView(getActivity());

        tvMemberName = new IteeTextView(getActivity());

        tvMemberType = new IteeTextView(getActivity());

        tvBookingNo = new IteeTextView(getActivity());

        tvInfoTime = new IteeTextView(getActivity());

        tvInfoArea = new IteeTextView(getActivity());

        Bundle bundle = getArguments();

        boolean noSetSellCaddie = false;
        if (bundle!=null){
            nfcCardType = bundle.getString(TransKey.NFC_CARD_TYPE, null);
            setNfcCard(bundle.getString(TransKey.NFC_CARD));
            noSetSellCaddie= bundle.getBoolean(TransKey.NFC_NO_SET_SELL_CADDIE,false);

        }
       if (!noSetSellCaddie)
        AppUtils.saveSellCaddie(getBaseActivity(), false);


    }

    @Override
    protected void setDefaultValueOfControls() {

        rlUserInfoContainer.addView(ivPhoto);
        RelativeLayout.LayoutParams paramsIvPhoto = (RelativeLayout.LayoutParams) ivPhoto.getLayoutParams();
        paramsIvPhoto.height = getActualWidthOnThisDevice(116);
        paramsIvPhoto.width = getActualWidthOnThisDevice(116);
        paramsIvPhoto.leftMargin = getActualWidthOnThisDevice(20);
        paramsIvPhoto.topMargin = getActualHeightOnThisDevice(10);
        paramsIvPhoto.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        ivPhoto.setLayoutParams(paramsIvPhoto);

        rlUserInfoContainer.addView(tvMemberName);
        RelativeLayout.LayoutParams paramsTvName = (RelativeLayout.LayoutParams) tvMemberName.getLayoutParams();
        paramsTvName.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsTvName.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsTvName.leftMargin = getActualWidthOnThisDevice(150);
        paramsTvName.topMargin = getActualHeightOnThisDevice(15);
        paramsTvName.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        tvMemberName.setLayoutParams(paramsTvName);

        rlUserInfoContainer.addView(tvMemberType);
        RelativeLayout.LayoutParams paramsTvIdentity = (RelativeLayout.LayoutParams) tvMemberType.getLayoutParams();
        paramsTvIdentity.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsTvIdentity.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsTvIdentity.topMargin = getActualHeightOnThisDevice(15);
        paramsTvIdentity.rightMargin = getActualWidthOnThisDevice(20);
        paramsTvIdentity.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        tvMemberType.setLayoutParams(paramsTvIdentity);

        rlUserInfoContainer.addView(tvBookingNo);
        RelativeLayout.LayoutParams paramsTvAppointmentId = (RelativeLayout.LayoutParams) tvBookingNo.getLayoutParams();
        paramsTvAppointmentId.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsTvAppointmentId.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsTvAppointmentId.topMargin = getActualHeightOnThisDevice(45);
        paramsTvAppointmentId.leftMargin = getActualWidthOnThisDevice(150);
        paramsTvAppointmentId.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        tvBookingNo.setLayoutParams(paramsTvAppointmentId);

        rlUserInfoContainer.addView(tvInfoTime);
        RelativeLayout.LayoutParams paramsTvDetailInfo = (RelativeLayout.LayoutParams) tvInfoTime.getLayoutParams();
        paramsTvDetailInfo.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsTvDetailInfo.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsTvDetailInfo.topMargin = getActualHeightOnThisDevice(75);
        paramsTvDetailInfo.leftMargin = getActualWidthOnThisDevice(150);
        paramsTvDetailInfo.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        tvInfoTime.setLayoutParams(paramsTvDetailInfo);

        rlUserInfoContainer.addView(tvInfoArea);
        RelativeLayout.LayoutParams paramsTvCourse = (RelativeLayout.LayoutParams) tvInfoArea.getLayoutParams();
        paramsTvCourse.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsTvCourse.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsTvCourse.topMargin = getActualHeightOnThisDevice(105);
        paramsTvCourse.leftMargin = getActualWidthOnThisDevice(150);
        paramsTvCourse.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        tvInfoArea.setLayoutParams(paramsTvCourse);
    }


    /**
     * user turn on card
     */
    public void netLinkTurnOnCard(String bookingNo,String timesFlg,String bdpId,String times) {


        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.VERIFY_NO, "1");
        params.put(ApiKey.BOOKING_NO, bookingNo);
        params.put(ApiKey.BOOKING_STATUS, "1");
        params.put(ApiKey.NFC_CARD_NUMBER, "0");

        params.put(ApiKey.SHOPPING_PRICING_TIMES_FLG, timesFlg);
        params.put(ApiKey.SHOPPING_PRICING_TIMES, times);
        params.put(ApiKey.SHOPPING_PRICING_BDP_ID, bdpId);


        HttpManager<JsonBookingDetailList> hh = new HttpManager<JsonBookingDetailList>(WelcomeFragment.this) {

            @Override
            public void onJsonSuccess(JsonBookingDetailList jo) {
                int returnCode = jo.getReturnCode();

                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    refreshLayout(WelcomeFragment.this.code);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.TurnOnCard, params);

    }

    @Override
    protected void setListenersOfControls() {

        rlUserInfoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //图04-2
                //push(TeeTimeAddFragment.class);
                Bundle bundle = new Bundle();
                bundle.putString(TransKey.BOOKING_ORDER_NO, bookingOrderNo);
                bundle.putBoolean("isAdd", false);

                bundle.putString(TransKey.COMMON_FROM_PAGE, WelcomeFragment.class.getName());
                push(TeeTimeAddFragment.class, bundle);
            }
        });


        quickMenuGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                boolean isEdit = (boolean) view.findViewById(R.id.tv_menu).getTag();
                Bundle bundle = new Bundle();
                if (isEdit) {
                    boolean hasPermission;
                    switch (position) {
                        case CHECK_IN:
                            if (isSingle) {
                                //check in
                                if (jsonEntry.getEntryList().get(0).getCheckStatus() == 2) {



                                    if (Constants.STR_0.equals(jsonEntry.getEntryList().get(0).getPricingTimesFlg())){
                                        netLinkTurnOnCard(jsonEntry.getEntryList().get(0).getBookingNo(),Constants.STR_0,Constants.STR_0,Constants.STR_0);

                                    }else{

                                        confirmPopWindow = new ShoppingDeleteTimesPopupWindow(getActivity(), new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                Utils.hideKeyboard(getBaseActivity());

                                                if (Utils.isStringNotNullOrEmpty(confirmPopWindow.getTimes())){


                                                    netLinkTurnOnCard(jsonEntry.getEntryList().get(0).getBookingNo(),jsonEntry.getEntryList().get(0).getPricingTimesFlg(),jsonEntry.getEntryList().get(0).getPricingBdpId(),confirmPopWindow.getTimes());
                                                }else{

                                                    Utils.showShortToast(getBaseActivity(), AppUtils.generateNotNullMessage(getBaseActivity(), R.string.pricing_table_times));
                                                }


                                            }
                                        }, WelcomeFragment.this);
                                        confirmPopWindow.setMessage(getString(R.string.msg_confirm_pay_wether_to_return_times));
                                        confirmPopWindow.showAtLocation(getRootView().findViewById(R.id.rl_content_container),
                                                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                                    }





                                } else {


                                    booking.setCustomerName(memberName);
                                    booking.setCustomerType(memberType);
                                    booking.setBookingNo(jsonEntry.getEntryList().get(0).getBookingNo());
                                    booking.setCustomerId(jsonEntry.getEntryList().get(0).getMemberId());
                                    booking.setBookingStatus(Constants.CHECK_IN);

                                    if (AppUtils.getAuth(AppUtils.AuthControl.AuthControlCheckInOrUndo, getActivity())) {
                                      //  push(TeeTimeCheckInFragment.class, bundle);

                                        checkPricing(false);
                                    } else {
                                        AppUtils.showHaveNoPermission(getBaseActivity());
                                    }


                                }
                            } else {
                                // 图04-2
                                bundle.putString(TransKey.BOOKING_ORDER_NO, code);
                                bundle.putBoolean("isAdd", false);
                                bundle.putString(TransKey.COMMON_FROM_PAGE, WelcomeFragment.class.getName());
                                push(TeeTimeAddFragment.class, bundle);
                            }
                            break;

                        case SHOPPING:
                            hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlSelling, getActivity());
                            if (hasPermission) {
                                if (isSingle) {
                                    // 图06-1
                                    bundle.putString(TransKey.COMMON_FROM_PAGE, WelcomeFragment.class.getName());
                                    bundle.putString(TransKey.SHOPPING_BOOKING_NO, code);
                                    bundle.putString(TransKey.SHOPPING_PLAYER_NAME, tvMemberName.getText().toString());
                                    push(ShoppingGoodsListFragment.class, bundle);
                                } else {
                                    // 图06-2
                                    bundle.putString(TransKey.SHOPPING_BOOKING_NO, code);
                                    bundle.putString(TransKey.COMMON_FROM_PAGE, WelcomeFragment.class.getName());
                                    bundle.putInt(TransKey.SHOPPING_PURCHASE_FLAG, ShoppingPaymentFragment.PURCHASE_FLAG_SHOPPING);
                                    push(ShoppingPaymentFragment.class, bundle);
                                }
                            } else {
                                AppUtils.showHaveNoPermission(WelcomeFragment.this);
                            }

                            break;

                        case CHECK_OUT:
                            // 图06-6
                            getCheckOut();
                            break;

                        case RECHARGE:
                            // 图04-8-1
                            hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlAgentsRecharge, getActivity())
                                    && AppUtils.getAuth(AppUtils.AuthControl.AuthControlRecharge, getActivity());
                            if (hasPermission) {
                                if (getView() != null) {
                                    SelectRechargeTypePopupWindow menuWindow = new SelectRechargeTypePopupWindow(WelcomeFragment.this, Integer.valueOf(memberId));
                                    menuWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                                }
                            } else {
                                AppUtils.showHaveNoPermission(WelcomeFragment.this);
                            }
                            break;

                        case PURCHASE_HISTORY:
                            // 图04-8-6
                            //push(PlayerPurchaseHistoryFragment.class);
                            bundle = new Bundle();
                            bundle.putInt(TransKey.COMMON_MEMBER_ID, Integer.valueOf(memberId));
                            push(PlayerPurchaseHistoryFragment.class, bundle);
                            break;

                        case RESERVATIONS:
                            // 04-8-7
                            //push(PlayerReservationsFragment.class);
                            bundle = new Bundle();
                            bundle.putInt(TransKey.COMMON_MEMBER_ID, Integer.valueOf(memberId));
                            push(PlayerReservationsFragment.class, bundle);
                            break;

                        case CUSTOMER_INFO:
                            if (Utils.isStringNotNullOrEmpty(memberId) && Integer.valueOf(memberId) > 0) {
                                // 图04-8-1
                                bundle = new Bundle();
                                bundle.putInt(TransKey.COMMON_MEMBER_ID, Integer.valueOf(memberId));
                                push(PlayerFragment.class, bundle);
                            } else {
                                // 图04-8-9
                                hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEdit, getActivity())
                                        && AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEditNonmember, getActivity());
                                if (hasPermission) {
                                    bundle = new Bundle();
                                    bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeAdd.value());
                                    push(PlayerInfoEditFragment.class, bundle);
                                } else {
                                    AppUtils.showHaveNoPermission(WelcomeFragment.this);
                                }
                            }
                            break;
                        case 7://bag or caddie


                            if (GRID_VIEW_BAG.equals(String.valueOf(view.getTag()))) {
                                isBagUnBind = !isBagUnBind;
                                if (isBagUnBind) {
                                    view.setBackgroundColor(Color.GRAY);
                                } else {

                                    view.setBackgroundColor(getColor(R.color.common_white));
                                }
                            } else {
                                isaCaddieUnBind = !isaCaddieUnBind;


                                if (isaCaddieUnBind) {

                                    view.setBackgroundColor(Color.GRAY);
                                } else {

                                    view.setBackgroundColor(getColor(R.color.common_white));
                                }

                            }


                            break;


                        case 8:// caddie
                            isaCaddieUnBind = !isaCaddieUnBind;
                            if (isaCaddieUnBind) {

                                view.setBackgroundColor(Color.GRAY);
                            } else {

                                view.setBackgroundColor(getColor(R.color.common_white));
                            }
                            break;

                        default:

                            break;
                    }
                }
            }
        });

    }

    @Override
    protected void setLayoutOfControls() {


    }

    @Override
    protected void setPropertyOfControls() {

        ivPhoto.setDefaultImageResId(R.drawable.icon_user_default_image);

        tvMemberName.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvMemberName.setTextColor(getResources().getColor(R.color.common_white));

        tvMemberType.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvMemberType.setTextColor(getResources().getColor(R.color.common_white));

        tvBookingNo.setTextSize(Constants.FONT_SIZE_SMALLER);
        tvBookingNo.setTextColor(getResources().getColor(R.color.common_fleet_blue));

        tvInfoTime.setTextSize(Constants.FONT_SIZE_SMALLER);
        tvInfoTime.setTextColor(getResources().getColor(R.color.common_fleet_blue));

        tvInfoArea.setTextSize(Constants.FONT_SIZE_SMALLER);
        tvInfoArea.setTextColor(getResources().getColor(R.color.common_fleet_blue));

    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(R.string.quick_welcome);
        getTvRight().setBackgroundResource(R.drawable.icon_scan_code);
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), QuickScanQrCodeActivity.class);
                getActivity().startActivityForResult(intent, QuickScanQrCodeActivity.SCANNING_GREQUEST_CODE);
            }
        });
    }


    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();

        quickMenuGridView.setAdapter(null);

        Bundle bundle = getArguments();
        code = bundle.getString(TransKey.BOOKING_ORDER_NO);
        getData(code);
        Utils.hideKeyboard(getActivity());
        ivPhoto.requestFocus();
    }

    @Override
    protected void reShowWithBackValue() {
//        super.reShowWithBackValue();
//        Bundle bundle = getArguments();
//        isSellCaddie = bundle.getBoolean(TransKey.NFC_IS_SELL_CADDIE);
    }

    public  void refreshLayout(String code){

        if (menuWindow!=null){
            menuWindow.dismiss();

        }
        if (playersPopup!=null){
            playersPopup.dismiss();

        }
        if (selectMemberPopup!=null){
            selectMemberPopup.dismiss();

        }
        if (confirmPopWindow!=null){
            confirmPopWindow.dismiss();

        }


        this.code = code;

        Bundle bundle = getArguments();
        nfcCard= bundle.getString(TransKey.NFC_CARD,Constants.STR_0);
        getData(code);
        Utils.hideKeyboard(getActivity());
        ivPhoto.requestFocus();
    }

    private void getData(String code) {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SCAN_QR_CODE, code);

        HttpManager<JsonEntry> hh = new HttpManager<JsonEntry>(WelcomeFragment.this) {

            @Override
            public void onJsonSuccess(JsonEntry jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {

                    jsonEntry = jo;
                    initDate(jo);
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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.ScanQrCodeGet, params);
    }

    private void initDate(JsonEntry jo) {

        isSingle = jo.getEntryList().size() == 1;

        AppUtils.showNetworkImage(ivPhoto, jo.getEntryList().get(0).getPhoto());

        if (Utils.isStringNotNullOrEmpty(jo.getEntryList().get(0).getMemberId())) {
            memberId = jo.getEntryList().get(0).getMemberId();
        } else {
            memberId = Constants.STR_EMPTY;
        }

        memberName = jo.getEntryList().get(0).getMemberName();
        tvMemberName.setText(memberName);

        if (isSingle) {
            memberType = jo.getEntryList().get(0).getMemberType();
        } else {
            memberType = "Group booking";
        }

        tvMemberType.setText(memberType);


        bookingOrderNo = jo.getEntryList().get(0).getBookingOrderNo();

        bookingNo = jo.getEntryList().get(0).getBookingNo();
        tvBookingNo.setText(AppUtils.getShortBookingNo(bookingNo));

        infoTime = jo.getEntryList().get(0).getInfoTime();
        tvInfoTime.setText(infoTime);

        infoArea = jo.getEntryList().get(0).getInfoArea();
        tvInfoArea.setText(infoArea);
        adapter = new QuickMenuGridViewAdapter(getActivity(), jo.getEntryList(), isSingle);
        quickMenuGridView.setAdapter(adapter);

        isSellCaddie = AppUtils.getSellCaddie(getBaseActivity());

        if (AppUtils.getIsCaddie(getBaseActivity())&&isSellCaddie){
            nfcCaddiePhoneCheckOneCardGet(nfcCard,getNfcCardType());

            AppUtils.saveSellCaddie(getBaseActivity(), false);
            isSellCaddie = false;

        }
    }

    private void putCheckOut(String no) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.BOOKING_NO, no);
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(this) {
            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();

                if (returnCode == Constants.RETURN_CODE_20125_CHECK_OUT_SUCCEEDED) {
                    jsonEntry.getEntryList().get(0).setCheckStatus(3);
                    adapter = new QuickMenuGridViewAdapter(getActivity(), jsonEntry.getEntryList(), isSingle);

                    quickMenuGridView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
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
        hh.startPut(getActivity(), ApiManager.HttpApi.TeeTimeCheckOutPut, params);
    }

    private void getCheckOut() {
        Map<String, String> params = new HashMap<>();
        final String no;
        if (isSingle) {
            no = bookingNo;
        } else {
            StringBuilder sb = new StringBuilder();
            for (JsonEntry.EntryList entryList : jsonEntry.getEntryList()) {
                if (sb.length() > 0) {
                    sb.append(Constants.STR_COMMA);
                }
                sb.append(entryList.bookingNo);
            }
            no = sb.toString();
        }
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.BOOKING_NO, no);

        HttpManager<JsonCheckOut> hh = new HttpManager<JsonCheckOut>(this) {

            @Override
            public void onJsonSuccess(JsonCheckOut jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    if (IS_PAID_ALL.equals(jo.getCheckStatus())) {
                        putCheckOut(no);
                    } else {
                        Bundle bundle = new Bundle();
                        if (isSingle) {
                            bundle.putString(TransKey.SHOPPING_BOOKING_NO, bookingNo);
                        } else {
                            bundle.putString(TransKey.SHOPPING_BOOKING_NO, bookingOrderNo);
                        }

                        bundle.putString(TransKey.COMMON_FROM_PAGE, WelcomeFragment.class.getName());

                        bundle.putInt(TransKey.SHOPPING_PURCHASE_FLAG, ShoppingPaymentFragment.PURCHASE_FLAG_CHECKOUT);
                        push(ShoppingPaymentFragment.class, bundle);
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
        hh.startGet(getActivity(), ApiManager.HttpApi.TeeTimeCheckOutGet, params);
    }



    //nfc api

    public void nfcOneCardBind(String cardNo,String cardType){
        setNfcCardType(cardType);
        switch (jsonEntry.getEntryList().get(0).getCheckStatus()) {
            //未开卡
            case 1:

                if (jsonEntry.getEntryList().size()>0){
                    if (jsonEntry.getEntryList().size() == 1){
                        booking.setCustomerName(memberName);
                        booking.setCustomerType(memberType);
                        booking.setCustomerId(jsonEntry.getEntryList().get(0).getMemberId());
                        booking.setBookingNo(jsonEntry.getEntryList().get(0).getBookingNo());
                        booking.setBookingStatus(Constants.CHECK_IN);
//                        Bundle  bundle = new Bundle();
//                        bundle.putSerializable("booking", booking);
//                        bundle.putBoolean("isQuick", true);
//                        bundle.putString("infoTime", infoTime);
//                        bundle.putString("infoArea", infoArea);
//                        bundle.putString(TransKey.NFC_CARD, nfcCard);
                        if (AppUtils.getAuth(AppUtils.AuthControl.AuthControlCheckInOrUndo, getActivity())) {
                           // push(TeeTimeCheckInFragment.class, bundle);
                            checkPricing(true);
                        } else {
                            AppUtils.showHaveNoPermission(getBaseActivity());
                        }

                        // oneCardBindPost(cardNo,jsonEntry.getEntryList().get(0).getBookingNo());
                    }else{
                        nfcOneCardCheckAll(cardNo);
                    }
                }

                break;
            //已开卡
            case 2:

                if (jsonEntry.getEntryList().size()>0){
                    if (jsonEntry.getEntryList().size() == 1){
                        oneCardBindPost(cardNo,jsonEntry.getEntryList().get(0).getBookingNo());
                    }else{
                        nfcOneCardCheckAll(cardNo);
                    }
                }
                //已结账
            case 3:

                break;

            default:
                break;

        }


    }



    private void nfcOneCardCheckAll(final String cardNo){

        if (!isOneCheckAll){
            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
            params.put(ApiKey.NFC_BOOKING_CODE, code);
            HttpManager<JsonNfcCheckCardForBookingNoGet> hh = new HttpManager<JsonNfcCheckCardForBookingNoGet>(this) {
                @Override
                public void onJsonSuccess(JsonNfcCheckCardForBookingNoGet jo) {
                    int returnCode = jo.getReturnCode();
                    String msg = jo.getReturnInfo();
                    if (returnCode == Constants.NFC_RETURN_CODE_20602) {
                        selectMemberPopup =  NfcSelectMemberPopup.createBuilder(WelcomeFragment.this, getFragmentManager()).setPlayers(jo.getBookingItems()).setSelectListener(new NfcSelectMemberPopup.nfcSelectMemberPopupSelectListener() {
                            @Override
                            public void selectItem(JsonNfcCheckCardForBookingNoGet.BookingItem selectItem) {


                                String selectBookingNo = selectItem.getBkdBkNo();
                                isOneCheckAll = false;
                                if (Constants.NFC_CHECK_IN.equals(selectItem.getCheckInStatus())) {
                                    oneCardBindPost(cardNo, selectBookingNo);
                                } else {
                                    booking.setCustomerName(memberName);
                                    booking.setCustomerType(memberType);
                                    if (isSingle) {
                                        booking.setCustomerId(jsonEntry.getEntryList().get(0).getMemberId());
                                        booking.setBookingNo(jsonEntry.getEntryList().get(0).getBookingNo());
                                    } else {
                                        booking.setCustomerName(selectItem.getBkdName());
                                        booking.setBookingNo(selectBookingNo);
                                        booking.setCustomerId(selectItem.getMemberId());
                                        booking.setCustomerType(selectItem.getMemberType());
                                    }
                                    booking.setBookingStatus(Constants.CHECK_IN);

//                                    Bundle bundle = new Bundle();
//                                    bundle.putSerializable("booking", booking);
//                                    bundle.putBoolean("isQuick", true);
//                                    bundle.putString("infoTime", infoTime);
//                                    bundle.putString("infoArea", infoArea);
//                                    bundle.putString(TransKey.NFC_CARD, nfcCard);
                                    if (AppUtils.getAuth(AppUtils.AuthControl.AuthControlCheckInOrUndo, getActivity())) {
                                       /// push(TeeTimeCheckInFragment.class, bundle);
                                        checkPricing(true);
                                    } else {
                                        AppUtils.showHaveNoPermission(getBaseActivity());
                                        isOneCheckAll = false;
                                    }
                                }

                            }
                        }).show();
                        selectMemberPopup .setDismissedListener(new BasePopFragment.OnDismissedListener() {
                            @Override
                            public void onDismissed() {
                                isOneCheckAll = false;
                            }
                        });
                        isOneCheckAll = true;
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
            hh.startGet(getActivity(), ApiManager.HttpApi.NfcOneCardCheckAllGet, params);

        }



    }

    private void oneCardBindPost(String cardNo,String bookingNo){
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.NFC_CARD_NUMBER, cardNo);
        params.put(ApiKey.NFC_BOOKING_CODE, bookingNo);
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(WelcomeFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                //  int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                Utils.showShortToast(getActivity(), msg);
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
        hh.start(this.getActivity(), ApiManager.HttpApi.NfcOneCardBindPost, params);

    }


    //nfc  bag
    public void nfcBagCardBind(String cardNo,String carType){
        setNfcCardType(carType);

        if (jsonEntry.getEntryList().size()>0){
            if (jsonEntry.getEntryList().size() == 1){
                oneBagBindPost(cardNo,jsonEntry.getEntryList().get(0).getBookingNo());

            }else{
                nfcBagCardCheckAll(cardNo);
            }
        }
    }

    private void oneBagBindPost(String cardNo,String bookingNo){
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.NFC_CARD_NUMBER, cardNo);
        params.put(ApiKey.NFC_BOOKING_CODE, bookingNo);
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(WelcomeFragment.this) {
            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                //int returnCode = jo.getReturnCode();

                if (isSingle){

                    jsonEntry.getEntryList().get(0).setBagCardStatus(Constants.NFC_BIND_STATUS_YES);
                    jsonEntry.getEntryList().get(0).setBagCardIsNowUnBind(false);
                    adapter = new QuickMenuGridViewAdapter(getActivity(), jsonEntry.getEntryList(), isSingle);
                    quickMenuGridView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                String msg = jo.getReturnInfo();
                Utils.showShortToast(getActivity(), msg);
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
        hh.start(this.getActivity(), ApiManager.HttpApi.NfcBagCardBindGet, params);

    }

    public void oneBagCardUnbindPost(String cardNo ,String cardType){

        if (this.nfcCardType ==null || !this.nfcCardType.equals(cardType)){

            setNfcCardType(cardType);
            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
            params.put(ApiKey.NFC_CARD_NUMBER, cardNo);
            params.put(ApiKey.NFC_BOOKING_CODE, jsonEntry.getEntryList().get(0).getBookingNo());
            HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(WelcomeFragment.this) {
                @Override
                public void onJsonSuccess(BaseJsonObject jo) {
                    String msg = jo.getReturnInfo();
                    int returnCode = jo.getReturnCode();
                    if (returnCode == Constants.NFC_RETURN_CODE_20602) {


                        jsonEntry.getEntryList().get(0).setBagCardIsNowUnBind(true);
                        adapter = new QuickMenuGridViewAdapter(getActivity(), jsonEntry.getEntryList(), isSingle);
                        quickMenuGridView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        isBagUnBind = false;
                    }
                    Utils.showShortToast(getActivity(), msg);
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
            hh.start(this.getActivity(), ApiManager.HttpApi.NfcBagCardUnbindPost, params);

        }


    }






    private void nfcBagCardCheckAll(final String cardNo){


        if (!isOneCheckAll){

            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
            params.put(ApiKey.NFC_BOOKING_CODE, code);

            HttpManager<JsonNfcCheckCardForBookingNoGet> hh = new HttpManager<JsonNfcCheckCardForBookingNoGet>(this) {

                @Override
                public void onJsonSuccess(JsonNfcCheckCardForBookingNoGet jo) {
                    int returnCode = jo.getReturnCode();
                    String msg = jo.getReturnInfo();
                    if (returnCode == Constants.NFC_RETURN_CODE_20602) {

                        Map<String,String> bookingMap  = new HashMap<>();

                        for (JsonNfcCheckCardForBookingNoGet.BookingItem bookingItem:jo.getBookingItems()){

                            bookingMap.put(bookingItem.getBkdBkNo(),bookingItem.getBkdName());

                        }

                        playersPopup =   PurchasePlayersPopup.createBuilder(WelcomeFragment.this, getFragmentManager()).setPlayers(bookingMap).setSelectListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String bookingNO = String.valueOf(v.getTag());
                                oneBagBindPost(cardNo,bookingNO);
                                isOneCheckAll = false;
                            }

                        }).show();

                        playersPopup.setDismissedListener(new BasePopFragment.OnDismissedListener() {
                            @Override
                            public void onDismissed() {
                                isOneCheckAll = false;
                            }
                        });

                        isOneCheckAll = true;
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
            hh.startGet(getActivity(), ApiManager.HttpApi.NfcBagCardCheckGet, params);
        }



    }


    //nfc caddie

    // caddie phone
    public void nfcCaddiePhoneBindPost(String cardNo){
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.NFC_CARD_NUMBER, cardNo);
        HttpManager<JsonNfcBindCaddieData> hh = new HttpManager<JsonNfcBindCaddieData>(WelcomeFragment.this) {
            @Override
            public void onJsonSuccess(JsonNfcBindCaddieData jo) {
                String msg = jo.getReturnInfo();
                int returnCode = jo.getReturnCode();

                Utils.log("=========================================nfcCaddiePhoneBindPost==============================================================================");


                Utils.log("returnCode : " + returnCode);

                Utils.log("===============================================================================================================");



//                if (returnCode == Constants.NFC_RETURN_CODE_20602) {
//                }

                if (isSingle){
                    jsonEntry.getEntryList().get(0).setCaddieInfo(jo.getCaddieInfo());
                    jsonEntry.getEntryList().get(0).setCaddieCardStatus(Constants.NFC_BIND_STATUS_YES);
                    jsonEntry.getEntryList().get(0).setCaddieCardIsNowUnBind(false);
                    adapter = new QuickMenuGridViewAdapter(getActivity(), jsonEntry.getEntryList(), isSingle);
                    quickMenuGridView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                Utils.showShortToast(getActivity(), msg);
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
        hh.start(this.getActivity(), ApiManager.HttpApi.NfcCaddiePhoneBindPost, params);

    }

    private  SelectRechargeConfirmPopupWindow menuWindow;

    public void nfcCaddiePhoneCheckOneCardGet(final String card,String cardType){
        setNfcCardType(cardType);

        if (!isCheckCaddie) {
            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
            params.put(ApiKey.NFC_CARD_NUMBER, card);
            HttpManager<JsonNfcCheckCaddieData> hh = new HttpManager<JsonNfcCheckCaddieData>(this) {
                @Override
                public void onJsonSuccess(JsonNfcCheckCaddieData jo) {
                    int returnCode = jo.getReturnCode();
                    String msg = jo.getReturnInfo();
                    if (returnCode == Constants.NFC_RETURN_CODE_20602) {

                        menuWindow
                                = new SelectRechargeConfirmPopupWindow(WelcomeFragment.this,
                                "0", "", 1);
                        menuWindow.setMessage(msg);
                        menuWindow.showAtLocation(rlContentContainer, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        isCheckCaddie = true;

                        menuWindow.getSave().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                menuWindow.dismiss();
                                isCheckCaddie = false;
                                nfcCaddiePhoneBindPost(card);
                            }

                        });
                        menuWindow.getCancel().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                menuWindow.dismiss();
                                isCheckCaddie = false;
                            }
                        });
                        menuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                isCheckCaddie = false;
                            }
                        });
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
            hh.startGet(getActivity(), ApiManager.HttpApi.NfcCaddiePhoneCheckOneCardGet, params);


        }
    }




    private void nfcCaddieCheckOne(final String card,String bookingNo){

        if (!isCheckCaddie){
            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
            params.put(ApiKey.NFC_CARD_NUMBER, card);
            params.put(ApiKey.NFC_BOOKING_CODE, bookingNo);
            HttpManager<JsonNfcCheckCaddieData> hh = new HttpManager<JsonNfcCheckCaddieData>(this) {
                @Override
                public void onJsonSuccess(JsonNfcCheckCaddieData jo) {
                    int returnCode = jo.getReturnCode();
                    String msg = jo.getReturnInfo();
                    if (returnCode == Constants.NFC_RETURN_CODE_20602) {
                        Utils.log("=========================================nfcCaddieCheck==============================================================================");

                        Utils.log("msg : " + msg);
                        Utils.log("returnCode : " + returnCode);

                        Utils.log("===============================================================================================================");
                        menuWindow
                                = new SelectRechargeConfirmPopupWindow(WelcomeFragment.this,
                                "0", "", 1);
                        menuWindow.setMessage(msg);
                        menuWindow.showAtLocation(rlContentContainer, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        isCheckCaddie = true;

                        menuWindow.getSave().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                menuWindow.dismiss();
                                isCheckCaddie = false;
                                nfcCaddieBindPost(card, jsonEntry.getEntryList().get(0).getBookingNo());
                            }

                        });
                        menuWindow.getCancel().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                menuWindow.dismiss();
                                isCheckCaddie = false;
                            }
                        });
                        menuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                isCheckCaddie = false;
                            }
                        });
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
            hh.startGet(getActivity(), ApiManager.HttpApi.NfcCaddieCheckGet, params);

        }
    }



    // other phone
    public  void nfcCaddieCheck(final String card,String cardType){
        setNfcCardType(cardType);
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        if (jsonEntry.getEntryList().size()>1){
            if (!isCheckCaddie) {
                params.put(ApiKey.NFC_BOOKING_CODE, code);
                HttpManager<JsonNfcCheckCardForBookingNoGet> hh = new HttpManager<JsonNfcCheckCardForBookingNoGet>(this) {
                    @Override
                    public void onJsonSuccess(JsonNfcCheckCardForBookingNoGet jo) {
                        int returnCode = jo.getReturnCode();
                        String msg = jo.getReturnInfo();
                        if (returnCode == Constants.NFC_RETURN_CODE_20602) {
                            Utils.log("=========================================NfcCaddieCardCheckAllGet==============================================================================");
                            Utils.log("msg : " + msg);
                            Utils.log("returnCode : " + returnCode);
                            Utils.log("===============================================================================================================");
                            Map<String,String> bookingMap  = new HashMap<>();

                            for (JsonNfcCheckCardForBookingNoGet.BookingItem bookingItem:jo.getBookingItems()){
                                bookingMap.put(bookingItem.getBkdBkNo(),bookingItem.getBkdName());
                            }
                            playersPopup =   PurchasePlayersPopup.createBuilder(WelcomeFragment.this, getFragmentManager()).setPlayers(bookingMap).setSelectListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String bookingNO = String.valueOf(v.getTag());
                                    nfcCaddieCheckOne(card, bookingNO);
                                    isCheckCaddie = false;
                                }

                            }).show();

                            playersPopup.setDismissedListener(new BasePopFragment.OnDismissedListener() {
                                @Override
                                public void onDismissed() {
                                    isCheckCaddie = false;
                                }
                            });
                            isCheckCaddie = true;
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
                hh.startGet(getActivity(), ApiManager.HttpApi.NfcCaddieCardCheckAllGet, params);
            }

        }else{
            nfcCaddieCheckOne(card, jsonEntry.getEntryList().get(0).getBookingNo());

        }

    }
    public void nfcCaddieBindPost(String cardNo,String bookingNo){
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.NFC_CARD_NUMBER, cardNo);
        params.put(ApiKey.NFC_BOOKING_CODE, bookingNo);
        HttpManager<JsonNfcBindCaddieData> hh = new HttpManager<JsonNfcBindCaddieData>(WelcomeFragment.this) {
            @Override
            public void onJsonSuccess(JsonNfcBindCaddieData jo) {
                String msg = jo.getReturnInfo();
                int returnCode = jo.getReturnCode();
                Utils.log("=========================================nfcCaddieBindPost==============================================================================");
                Utils.log("returnCode : " + returnCode);
                Utils.log("===============================================================================================================");
                if (returnCode == Constants.NFC_RETURN_CODE_20602) {
                    jsonEntry.getEntryList().get(0).setCaddieInfo(jo.getCaddieInfo());
                    jsonEntry.getEntryList().get(0).setCaddieCardStatus(Constants.NFC_BIND_STATUS_YES);
                    jsonEntry.getEntryList().get(0).setCaddieCardIsNowUnBind(false);
                    adapter = new QuickMenuGridViewAdapter(getActivity(), jsonEntry.getEntryList(), isSingle);
                    quickMenuGridView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    isBagUnBind = false;
                }
                Utils.showShortToast(getActivity(), msg);
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
        hh.start(this.getActivity(), ApiManager.HttpApi.NfcCaddieBindPost, params);

    }


    public void unBindCaddie(String cardNo,String cardType){
        if (Constants.NFC_CARD_TYPE_ONE.equals(cardType)){
            nfcCaddiePhoneUnBind(cardNo);

        }
        if (Constants.NFC_CARD_TYPE_BAG.equals(cardType)){


        }
        if (Constants.NFC_CARD_TYPE_CADDIE.equals(cardType)){
            nfcCaddieUnbindPost(cardNo);

        }

    }

    private void nfcCaddiePhoneUnBind(String cardNo){
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.NFC_CARD_NUMBER, cardNo);
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(WelcomeFragment.this) {
            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                String msg = jo.getReturnInfo();
                int returnCode = jo.getReturnCode();
                if (returnCode == Constants.NFC_RETURN_CODE_20602) {
                    jsonEntry.getEntryList().get(0).setCaddieCardIsNowUnBind(true);
                    adapter = new QuickMenuGridViewAdapter(getActivity(), jsonEntry.getEntryList(), isSingle);
                    quickMenuGridView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    isaCaddieUnBind = false;
                }
                Utils.showShortToast(getActivity(), msg);
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
        hh.start(this.getActivity(), ApiManager.HttpApi.NfcCaddiePhoneUnBindPost, params);

    }

    private void nfcCaddieUnbindPost(String cardNo){

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.NFC_CARD_NUMBER, cardNo);
        params.put(ApiKey.NFC_BOOKING_CODE, jsonEntry.getEntryList().get(0).getBookingNo());
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(WelcomeFragment.this) {
            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                String msg = jo.getReturnInfo();
                int returnCode = jo.getReturnCode();
                if (returnCode == Constants.NFC_RETURN_CODE_20602) {

                    jsonEntry.getEntryList().get(0).setCaddieCardIsNowUnBind(true);
                    adapter = new QuickMenuGridViewAdapter(getActivity(), jsonEntry.getEntryList(), isSingle);
                    quickMenuGridView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    isaCaddieUnBind = false;
                }
                Utils.showShortToast(getActivity(), msg);
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
        hh.start(this.getActivity(), ApiManager.HttpApi.NfcCaddieUnbindPost, params);
    }




    private void checkPricing(final boolean isNfc ) {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getBaseActivity()));
        params.put(ApiKey.BOOKING_ORDER_NO, bookingOrderNo);

        params.put(ApiKey.SHOPPING_BOOKING_NO,bookingNo);

        HttpManager<jsonCheckPricingData> hh = new HttpManager<jsonCheckPricingData>(WelcomeFragment.this) {

            @Override
            public void onJsonSuccess(jsonCheckPricingData jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {


                    Bundle bundle = new Bundle();
                    bundle.putSerializable("booking", booking);
                    bundle.putBoolean("isQuick", true);
                    bundle.putString("infoTime", infoTime);
                    bundle.putString("infoArea", infoArea);
                    if (isNfc)
                        bundle.putString(TransKey.NFC_CARD, nfcCard);

                    if (Constants.STR_0.equals(jo.getPricingDisplay())){

                        bundle.putString(TransKey.COMMON_FROM_PAGE, WelcomeFragment.class.getName());
                        push(TeeTimeCheckInFragment.class, bundle);

                    }else{
                        bundle.putString(TransKey.SHOPPING_BOOKING_NO, bookingOrderNo);
                        bundle.putString(TransKey.COMMON_FROM_PAGE, WelcomeFragment.class.getName());
                        bundle.putString(TransKey.COMMON_JUMP, TeeTimeChoosePriceFragment.JUMP_FRAGMENT_CHECK_IN);
                        push(TeeTimeChoosePriceFragment.class, bundle);
                    }

                } else {
                    Utils.showShortToast(getBaseActivity(), msg);
                }

            }

            @Override
            public void onJsonError(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(getBaseActivity(), R.string.msg_common_network_error);
            }
        };
        hh.startGet(getBaseActivity(), ApiManager.HttpApi.xcheckpricing, params);
    }


}
