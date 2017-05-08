/**
 * Project Name: itee
 * File Name:  TeeTimeCheckInFragment.java
 * Package Name: cn.situne.itee.fragment.teetime
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.teetime;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.player.PlayerSignatureEditFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonBookingDetailList;
import cn.situne.itee.manager.jsonentity.JsonCheckInGoodList;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.SignaturePopupWindow;
import cn.situne.itee.view.TeeTimeCheckInItemView;

/**
 * ClassName:TeeTimeCheckInFragment <br/>
 * Function: player check in  fragment.<br/>
 * UI:  04-2-1
 * Date: 2015-03-11 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
//开卡确认页面
public class TeeTimeCheckInFragment extends BaseFragment {

    private JsonCheckInGoodList jsonCheckInGoodList;
    private LinearLayout rlContainer, rlContainerTitle, llGoodsListContainer;
    private RelativeLayout rlContainerBottom;

    private IteeTextView checkIn, tvName, tvCardNo; //Add by ysc.tvCardNo标签
    private IteeEditText txtCardNo;//Add by ysc. 用于显示、输入卡号
    private NetworkImageView imPhoto;
    private List<JsonBookingDetailList.DataListItem.BookingAreaListItem> areaList;
    private JsonBookingDetailList.DataListItem.BookingListItem booking;
    private boolean isQuick;
    private String infoTime;
    private String infoArea;
    private String customersType;

    private String nfcCard;
    private String fromPage;

    private String needJump;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_newteetimes_check_in;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        areaList = new ArrayList<>();

        Bundle bundle = getArguments();
        if (bundle != null) {
            booking = (JsonBookingDetailList.DataListItem.BookingListItem) bundle.getSerializable("booking");
            customersType = bundle.getString(TransKey.CUSTOMERS_TYPE, StringUtils.EMPTY);
            nfcCard = bundle.getString(TransKey.NFC_CARD, Constants.STR_0);
            fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE, Constants.STR_EMPTY);
            needJump = bundle.getString("needJump", Constants.STR_EMPTY);
            //快速入口
            isQuick = bundle.getBoolean("isQuick");
            infoTime = bundle.getString("infoTime");
            infoArea = bundle.getString("infoArea");
            //正常入口
            ArrayList<String> areaListTemp = bundle.getStringArrayList("areaList");
            if (areaListTemp != null) {
                for (int i = 0; i < areaListTemp.size(); i++) {
                    JsonBookingDetailList.DataListItem.BookingAreaListItem temp = (JsonBookingDetailList.DataListItem.BookingAreaListItem) Utils
                            .getObjectFromString(areaListTemp.get(i));
                    areaList.add(temp);
                }
            }
        }
        checkIn = new IteeTextView(getActivity());
        //txtCardNo、tvCardNo  Add by ysc
        txtCardNo = (IteeEditText) rootView.findViewById(R.id.txtCardNo);
        tvCardNo = (IteeTextView) rootView.findViewById(R.id.lblCardNo);

        tvName = (IteeTextView) rootView.findViewById(R.id.tv_newteetimes_memeber_name);
        imPhoto = (NetworkImageView) rootView.findViewById(R.id.im_newteetimes_memeber_photo);
        rlContainerTitle = (LinearLayout) rootView.findViewById(R.id.rl_newteetimes_memeber_select_title);
        rlContainer = (LinearLayout) rootView.findViewById(R.id.rl_newteetimes_memeber_select_container);
        llGoodsListContainer = (LinearLayout) rootView.findViewById(R.id.rl_goods_list_container);
        rlContainerBottom = (RelativeLayout) rootView.findViewById(R.id.rl_newteetimes_memeber_select_bottom_container);

    }


    @Override
    protected void setDefaultValueOfControls() {
        if (booking.getCustomerName() != null) {
            tvName.setText(booking.getCustomerName());
        }
    }

    @Override
    protected void setListenersOfControls() {
        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.STR_1.equals(jsonCheckInGoodList.getDataList().getGuestFlag())) {
                    if (Constants.STR_1.equals(jsonCheckInGoodList.getDataList().getSignGuest())) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("memberId", Integer.valueOf(jsonCheckInGoodList.getDataList().getGuestUserId()));
                        bundle.putString("booking_no", booking.getBookingNo());
                        bundle.putString("signature", StringUtils.EMPTY);
                        bundle.putString(TransKey.NFC_CARD, nfcCard);
                        bundle.putString("booking_status", String.valueOf(booking.getBookingStatus()));
                        bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeCheckInFragment.class.getName());
                        push(PlayerSignatureEditFragment.class, bundle);

                    } else {
                        netLinkTurnOnCard();
                    }
                } else {
                    if (Utils.isStringNotNullOrEmpty(booking.getCustomerId())
                            && jsonCheckInGoodList.getDataList().getSignCheckIn() == 1) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("memberId", Integer.valueOf(booking.getCustomerId()));
                        bundle.putString("booking_no", booking.getBookingNo());
                        bundle.putString("signature", StringUtils.EMPTY);
                        bundle.putString(TransKey.NFC_CARD, nfcCard);
                        bundle.putString("booking_status", String.valueOf(booking.getBookingStatus()));
                        bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeCheckInFragment.class.getName());
                        push(PlayerSignatureEditFragment.class, bundle);
                    } else {
                        netLinkTurnOnCard();
                    }
                }

//                if ( Constants.STR_1.equals(jsonCheckInGoodList.getDataList().getSignGuest()) ){
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("memberId", Integer.valueOf(booking.getSignId()));
//                    bundle.putString("booking_no", booking.getBookingNo());
//                    bundle.putString("signature", StringUtils.EMPTY);
//                    bundle.putString(TransKey.NFC_CARD, nfcCard);
//                    bundle.putString("booking_status", String.valueOf(booking.getBookingStatus()));
//                    bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeCheckInFragment.class.getName());
//                    push(PlayerSignatureEditFragment.class, bundle);
//
//                }else{
//                }
            }
        });

        llGoodsListContainer.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                doBack();
            }
        });

        imPhoto.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                if (Utils.isStringNotNullOrEmpty(booking.getCustomerPic())) {
                    SignaturePopupWindow signaturePopupWindow = new SignaturePopupWindow(getActivity(), null,
                            booking.getCustomerPic());
                    signaturePopupWindow.showAtLocation(getRootView(), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
        });
        txtCardNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                nfcCard = txtCardNo.getText().toString().trim();
            }
        });
    }

    @Override
    protected void setLayoutOfControls() {
        rlContainerTitle.setPadding((int) (getScreenWidth() * 0.04f), 0, (int) (getScreenWidth() * 0.04f), 0);
        rlContainerBottom.addView(checkIn);
        RelativeLayout.LayoutParams paramCheckIn = (RelativeLayout.LayoutParams) checkIn.getLayoutParams();
        paramCheckIn.width = (int) (getScreenWidth() * 0.9f);
        paramCheckIn.height = (int) (getScreenHeight() * 0.08f);
        paramCheckIn.addRule(RelativeLayout.CENTER_IN_PARENT);
        checkIn.setLayoutParams(paramCheckIn);
        checkIn.setBackground(getResources().getDrawable(R.drawable.member_add_button));
        checkIn.setGravity(Gravity.CENTER);
        checkIn.setText(getResources().getString(R.string.check_in));
        checkIn.setTextSize(Constants.FONT_SIZE_LARGER);
        checkIn.setTextColor(getColor(R.color.common_white));
    }

    @Override
    protected void setPropertyOfControls() {
        tvName.setTextSize(Constants.FONT_SIZE_LARGER);
        tvName.setTextColor(getColor(R.color.common_black));

        if (Utils.isStringNotNullOrEmpty(booking.getCustomerPic())) {
            AppUtils.showNetworkImage(imPhoto, booking.getCustomerPic());
        } else {
            AppUtils.showNetworkImage(imPhoto, Constants.PHOTO_DEFAULT_URL);
        }
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getString(R.string.check_in));
    }

    /**
     * user turn on card
     */
    public void netLinkTurnOnCard() {
        if (!checkNfcCardNo()) {

            return; //增加对卡号的检查
        }

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.VERIFY_NO, "1");
        params.put(ApiKey.BOOKING_NO, String.valueOf(booking.getBookingNo()));
        params.put(ApiKey.BOOKING_STATUS, String.valueOf(booking.getBookingStatus()));
        params.put(ApiKey.NFC_CARD_NUMBER, nfcCard);
        HttpManager<JsonBookingDetailList> hh = new HttpManager<JsonBookingDetailList>(TeeTimeCheckInFragment.this) {

            @Override
            public void onJsonSuccess(JsonBookingDetailList jo) {
                int returnCode = jo.getReturnCode();

                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    Bundle bundle = new Bundle();
                    if (Constants.STR_0.equals(nfcCard)) {
                        AppUtils.saveSellCaddie(getBaseActivity(), false);
                    } else {
                        AppUtils.saveSellCaddie(getBaseActivity(), true);
                    }
                    bundle.putString("fromFlag", "refresh");

                    if (Constants.STR_1.equals(needJump))
                        doBack();
                    try {
                        doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
                    } catch (ClassNotFoundException e) {
                        Utils.log(e.getMessage());
                    }
                   // Utils.showShortToast(getActivity(), jo.getReturnInfo());
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                Utils.showShortToast(getActivity(), error.getMessage());
            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.TurnOnCard, params);

    }

    @Override
    protected void executeOnceOnCreate() {
        netLinkCheckInGoodsList();
        super.executeOnceOnCreate();
    }

    @Override
    protected void reShowWithBackValue() {
        Bundle bundle = getReturnValues();
        if (bundle != null && bundle.getBoolean(TransKey.COMMON_REFRESH)) {
            netLinkTurnOnCard();
        }
    }

    private void resetView(boolean isQuick) {
        //init reservation time and course
        StringBuilder stringTime = new StringBuilder();
        StringBuilder stringType = new StringBuilder();

        for (int i = 0; i < areaList.size(); i++) {
            JsonBookingDetailList.DataListItem.BookingAreaListItem areaItem = areaList.get(i);
            stringTime.append(areaItem.getBookingTime().substring(0, 5));
            stringType.append(areaItem.getCourseAreaType());
            if (i != areaList.size() - 1) {
                stringTime.append(Constants.STR_SLASH);
                stringType.append(Constants.STR_SLASH);
            }
        }

        for (int i = 0; i < 6; i++) {
            TeeTimeCheckInItemView view = new TeeTimeCheckInItemView(this, null, 0, 0);
            view.setPadding((int) (getScreenWidth() * 0.04f), 0, (int) (getScreenWidth() * 0.04f), 0);
            view.setTag(i);
            switch (i) {
                case 0:
                    view.setTvName(customersType);
                    view.setTvDescribe(getString(R.string.check_in_customer_type));
                    break;
                case 1:
                    if (booking.getBookingNo() != null) {
                        view.setTvName(String.valueOf(AppUtils.getShortBookingNo(booking.getBookingNo())));
                    }
                    view.setTvDescribe(getString(R.string.check_in_confirmation_no));
                    break;
                case 2:
                    if (isQuick) {
                        view.setTvName(infoTime);
                    } else {
                        String bookingDate = jsonCheckInGoodList.getDataList().getBookingDate();
                        if (bookingDate != null) {
                            String currentShowDate = DateUtils.getCurrentShowYearMonthDayFromApiDateStr(bookingDate, mContext);
                            view.setTvName(currentShowDate + Constants.STR_SPACE + stringTime);
                        }
                    }
                    view.setTvDescribe(getString(R.string.check_in_reservation_time));
                    break;
                case 3:
                    if (isQuick) {
                        view.setTvName(infoArea);
                    } else {
                        stringType.append(Constants.STR_SPACE)
                                .append(new BigDecimal(areaList.size()).multiply(new BigDecimal(9)))
                                .append(Constants.STR_SPACE).append("holes");
                        view.setTvName(stringType.toString());
                    }
                    view.setTvDescribe(getString(R.string.check_in_course));
                    break;
                case 4:
                    if (jsonCheckInGoodList.getDataList().getBookingUser() != null) {
                        view.setTvName(jsonCheckInGoodList.getDataList().getBookingUser());
                    }
                    view.setTvDescribe(getString(R.string.check_in_caller));
                    break;
                case 5:
                    if (jsonCheckInGoodList.getDataList().getBookingTele() != null) {
                        view.setTvName(jsonCheckInGoodList.getDataList().getBookingTele());
                    }
                    view.setTvDescribe(getString(R.string.check_in_caller_phone_no));
                    break;
                default:
                    break;
            }
            rlContainer.addView(view);
            View viewLine = new View(getActivity());
            viewLine.setBackgroundColor(getColor(R.color.common_gray));
            rlContainer.addView(viewLine);
            LinearLayout.LayoutParams paramsLine = (LinearLayout.LayoutParams) viewLine.getLayoutParams();
            paramsLine.height = DensityUtil.px2dip(getActivity(), 2);
            paramsLine.width = LinearLayout.LayoutParams.MATCH_PARENT;
            viewLine.setLayoutParams(paramsLine);
        }
    }

    private void addGoodView() {

        resetView(isQuick);

        IteeTextView allTitle = new IteeTextView(getActivity());
        allTitle.setText(getString(R.string.shopping_rental));
        allTitle.setTextColor(getColor(R.color.common_blue));
        allTitle.setTextSize(Constants.FONT_SIZE_SMALLER);
        allTitle.setBackground(getDrawable(R.drawable.list_item_bottom_line));
        llGoodsListContainer.addView(allTitle);
        allTitle.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams paramsAllTitle = (LinearLayout.LayoutParams) allTitle.getLayoutParams();
        paramsAllTitle.width = LinearLayout.LayoutParams.MATCH_PARENT;
        paramsAllTitle.height = (int) (getScreenHeight() * 0.08F);
        allTitle.setLayoutParams(paramsAllTitle);
        allTitle.setPadding((int) (getScreenWidth() * 0.04f), 0, (int) (getScreenWidth() * 0.04f), 0);

        for (int i = 0; i < jsonCheckInGoodList.getDataList().getCheckList().size(); i++) {
            RelativeLayout container = new RelativeLayout(getActivity());
            llGoodsListContainer.addView(container);

            LinearLayout.LayoutParams paramsContainer = (LinearLayout.LayoutParams) container.getLayoutParams();
            paramsContainer.width = LinearLayout.LayoutParams.MATCH_PARENT;
            paramsContainer.height = (int) (getScreenHeight() * 0.06F);
            container.setPadding((int) (getScreenWidth() * 0.04f), 0, 0, 0);
            container.setLayoutParams(paramsContainer);

            JsonCheckInGoodList.GoodInfo goodInfo = jsonCheckInGoodList.getDataList().getCheckList().get(i);
            ImageView imageView = new ImageView(getActivity());
            IteeTextView goodTitle = new IteeTextView(getActivity());
            IteeTextView goodType = new IteeTextView(getActivity());
            IteeTextView goodNum = new IteeTextView(getActivity());

            goodTitle.setTextColor(getColor(R.color.common_blue));

            goodType.setTextColor(getColor(R.color.common_blue));
            goodNum.setTextColor(getColor(R.color.common_blue));

            goodTitle.setGravity(Gravity.CENTER_VERTICAL);
            goodType.setGravity(Gravity.CENTER_VERTICAL);
            goodNum.setGravity(Gravity.CENTER_VERTICAL);
            if (goodInfo.getCategoryId() != null) {
                switch (goodInfo.getCategoryId()) {
                    case 1:
                        imageView.setImageResource(R.drawable.icon_shops_product_edit_caddie_select);
                        break;
                    case 2:
                        imageView.setImageResource(R.drawable.icon_shops_product_edit_cart_select);
                        break;
                    case 3:
                        imageView.setImageResource(R.drawable.icon_shops_product_edit_clubs_select);
                        break;
                    case 4:
                        imageView.setImageResource(R.drawable.icon_shops_product_edit_shoes_select);
                        break;
                    case 5:
                        imageView.setImageResource(R.drawable.icon_shops_product_edit_trolley_select);
                        break;
                    case 6:
                        imageView.setImageResource(R.drawable.icon_shops_product_edit_umbrella_select);
                        break;
                    case 7:
                        imageView.setImageResource(R.drawable.icon_shops_product_edit_towel_select);
                        break;
                }

            }

            String type = goodInfo.getProductAttr();
            StringBuffer attrString = new StringBuffer();
            String[] attrs = type.split(Constants.STR_COMMA);
            for (int j = 0; j < attrs.length; j++) {
                if (j != 0) {
                    attrString.append(attrs[j]);
                }
            }

            goodTitle.setText(attrs[0]);
            goodType.setText(attrString);
            goodNum.setText(Constants.STR_MULTIPLY + goodInfo.getProductNum());

            container.addView(imageView);
            container.addView(goodTitle);
            container.addView(goodType);
            container.addView(goodNum);


            RelativeLayout.LayoutParams paramsIm = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
            paramsIm.width = (int) (getScreenHeight() * 0.04F);
            paramsIm.height = (int) (getScreenHeight() * 0.04F);
            paramsIm.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
            paramsIm.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
            imageView.setLayoutParams(paramsIm);
            imageView.setId(View.generateViewId());

            RelativeLayout.LayoutParams paramsGoodNum = (RelativeLayout.LayoutParams) goodNum.getLayoutParams();
            paramsGoodNum.width = (int) (getScreenWidth() * 0.1F);
            paramsGoodNum.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            paramsGoodNum.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
            goodNum.setLayoutParams(paramsGoodNum);
            goodNum.setId(View.generateViewId());

            RelativeLayout.LayoutParams paramsGoodTitle = (RelativeLayout.LayoutParams) goodTitle.getLayoutParams();
            paramsGoodTitle.width = (int) (getScreenWidth() * 0.3F);
            paramsGoodTitle.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            paramsGoodTitle.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
            goodTitle.setLayoutParams(paramsGoodTitle);

            RelativeLayout.LayoutParams paramsGoodType = (RelativeLayout.LayoutParams) goodType.getLayoutParams();
            paramsGoodType.width = (int) (getScreenWidth() * 0.5F);
            paramsGoodType.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            paramsGoodType.addRule(RelativeLayout.LEFT_OF, goodNum.getId());
            goodType.setLayoutParams(paramsGoodType);
        }

    }

    /**
     * get the goods list by  booking no.
     */
    public void netLinkCheckInGoodsList() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.BOOKING_NO, booking.getBookingNo());
        if (Utils.isStringNotNullOrEmpty(booking.getCustomerId())) {
            params.put(ApiKey.CUSTOMERS_MEMBER_ID, booking.getCustomerId());
        }

        HttpManager<JsonCheckInGoodList> hh = new HttpManager<JsonCheckInGoodList>(TeeTimeCheckInFragment.this) {

            @Override
            public void onJsonSuccess(JsonCheckInGoodList jo) {
                int returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    jsonCheckInGoodList = jo;
                    addGoodView();
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.Checkinlist, params);
    }

    public void writeNfcCardNo(String cardNo) {
        try {
            IteeEditText txtCardNo = (IteeEditText) getRootView().findViewById(R.id.txtCardNo);
            txtCardNo.setText(cardNo);
        } catch (Exception ex) {
            Log.e("CheckIn Fragment", ex.getMessage());
        }
    }

//    private TextWatcher textChangedWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//            nfcCard = txtCardNo.getText().toString().trim();
//        }
//    };

    private boolean checkNfcCardNo() {
        //这里不能直接用 nfcCard这个变量，因为在初始化的时候就给nfcCard赋了默认值“0”，因此要用txtCardNo的值
        String cardNo = txtCardNo.getText().toString().trim();
        boolean pass = false;
        if (cardNo != null && !cardNo.isEmpty()) {
            pass = true;
        } else {
            pass = false;
            Toast.makeText(getActivity(), "必须输入或刷入NFC卡号才能开卡", Toast.LENGTH_LONG).show();
        }
        return pass;

    }

}
