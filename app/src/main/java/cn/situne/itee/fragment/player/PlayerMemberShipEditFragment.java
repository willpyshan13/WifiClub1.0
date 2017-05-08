/**
 * Project Name: itee
 * File Name:  PlayerMemberShipEditFragment.java
 * Package Name: cn.situne.itee.fragment.player
 * Date:   2015-03-28
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.player;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.common.utils.UtilsIsSerialDate;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonConfirmPayment;
import cn.situne.itee.manager.jsonentity.JsonMemberShip;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.SelectDatePopupWindow;
import cn.situne.itee.view.popwindow.SelectIDTypePopupWindow;

/**
 * ClassName:PlayerMemberShipEditFragment <br/>
 * Function: membership edit fragment <br/>
 * UI:  04-8-3
 * Date: 2015-03-28 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */

public class PlayerMemberShipEditFragment extends BaseEditFragment {

    private RelativeLayout rlIDTypeContainer;
    private RelativeLayout rlIDContainer;
    private RelativeLayout rlCreateDateContainer;
    private RelativeLayout rlAnnualFeeContainer;
    private LinearLayout llPricingTable;


    private LinearLayout llAnnualFeeAllContainer;
    private RelativeLayout rlPaymentDateContainer;
    private RelativeLayout rlEndDateContainer;
    private RelativeLayout rlSendMailContainer;
    private RelativeLayout rlEffectiveDateContainer;

    private IteeTextView tvIDType;
    private IteeTextView tvIDTypeValue;
    private ImageView imIDType, imEffectiveDate, imEndDateFirst, imEndDate, imSendMail;

    private IteeTextView tvID;
    private IteeEditText tvIDValue;

    private IteeTextView tvEffectiveDate;
    private IteeTextView tvEffectiveDateValue;

    private IteeTextView tvEndDateFirst;
    private IteeTextView tvEndDateFirstValue;

    private IteeTextView tvAnnualFee;
    private IteeTextView tvAnnualFeeValue;

    private IteeTextView tvPaymentDate;
    private IteeTextView tvPaymentDateValue;

    private IteeTextView tvPaymentEndDate;
    private IteeTextView tvPaymentEndDateValue;

    private IteeTextView tvSendEmail;
    private JsonMemberShip dataParameter;
    private SelectDatePopupWindow.OnDateSelectClickListener dateSelectReturn;

    //parent view parameter.
    private Integer memberId;
    private String memberType;
    private String memberEmail;
    private Integer memberTypeId;
    private String memberNo;
    private Integer annualFeeStatus;
    private Integer memberPeriod;
    private String annualFee;
    private String memberEndDate;

    private Boolean isNonMember;
    private String overdueFlag;
    private ArrayList<LinearLayout> closeItemList;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_player_membership;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.player_info_membership;
    }

    @Override
    protected void initControls(View rootView) {
        closeItemList = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            memberId = bundle.getInt("memberId");
            memberType = bundle.getString("memberType");
            memberEmail = bundle.getString("memberEmail");
            memberTypeId = bundle.getInt("memberTypeId");
            overdueFlag = bundle.getString("overdueFlag");

            isNonMember = memberTypeId == 0;
            memberNo = bundle.getString("memberNo");
        }

        setFragmentMode(FragmentMode.FragmentModeBrowse);

        rlIDTypeContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_member_id_type);
        rlIDContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_member_id);
        rlEffectiveDateContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_member_effective);
        rlCreateDateContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_member_create_date);
        llAnnualFeeAllContainer = (LinearLayout) rootView.findViewById(R.id.ll_member_annual_fee);

        rlAnnualFeeContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_member_annual_fee);


        llPricingTable = (LinearLayout) rootView.findViewById(R.id.ll_pricing_table);
        rlPaymentDateContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_member_payment_date);
        rlEndDateContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_member_end_date_down);
        rlSendMailContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_member_send_mail);

        tvIDType = new IteeTextView(getActivity());
        tvIDTypeValue = new IteeTextView(getActivity());
        imIDType = new ImageView(getActivity());
        imEffectiveDate = new ImageView(getActivity());
        imEndDateFirst = new ImageView(getActivity());
        imEndDate = new ImageView(getActivity());
        imSendMail = new ImageView(getActivity());

        tvID = new IteeTextView(getActivity());
        tvIDValue = new IteeEditText(getActivity());

        tvEffectiveDate = new IteeTextView(getActivity());
        tvEffectiveDateValue = new IteeTextView(getActivity());

        tvEndDateFirst = new IteeTextView(getActivity());
        tvEndDateFirstValue = new IteeTextView(getActivity());

        tvAnnualFee = new IteeTextView(getActivity());
        tvAnnualFeeValue = new IteeTextView(getActivity());

        tvPaymentDate = new IteeTextView(getActivity());
        tvPaymentDateValue = new IteeTextView(getActivity());

        tvPaymentEndDate = new IteeTextView(getActivity());
        tvPaymentEndDateValue = new IteeTextView(getActivity());

        tvSendEmail = new IteeTextView(getActivity());
    }

    private void setPricingTable() {

        llPricingTable.removeAllViews();

        ArrayList<JsonMemberShip.MemberTimes> timesArrayList = dataParameter.getDataList().getMemberTimesList();
        for (JsonMemberShip.MemberTimes times : timesArrayList) {
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
            RelativeLayout item = new RelativeLayout(getBaseActivity());
            item.setLayoutParams(itemParams);

            IteeTextView tvKey = new IteeTextView(getBaseActivity());
            tvKey.setText(times.getPricingType());
            tvKey.setTextColor(getColor(R.color.common_black));
            item.addView(tvKey);


            IteeTextView tvRight = new IteeTextView(getBaseActivity());
            tvRight.setText(times.getAvailTimes());
            tvRight.setTextColor(getColor(R.color.common_gray));
            item.addView(tvRight);


            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvKey, 40, getBaseActivity());
            LayoutUtils.setCellRightValueViewOfRelativeLayout(tvRight, getActualWidthOnThisDevice(40), getBaseActivity());

            AppUtils.addTopSeparatorLine(item, getBaseActivity());
            llPricingTable.addView(item);

        }

    }

    @Override
    protected void setDefaultValueOfControls() {

        if (dataParameter != null) {
            JsonMemberShip.DataList dataList = dataParameter.getDataList();

            if (StringUtils.isEmpty(memberType)) {
                memberType = getString(R.string.customers_non_member);
            }

            tvIDTypeValue.setText(memberType);
            tvIDValue.setText(memberNo);

            tvEffectiveDateValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(dataList.getEffectiveDate(), mContext));
            tvEndDateFirstValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(dataList.getEffectiveEndDate(), mContext));

            for (int i = 0; i < dataList.getMemberTypeList().size(); i++) {
                JsonMemberShip.MemberTypeListItem item = dataList.getMemberTypeList().get(i);
                if (isNonMember == null || isNonMember) {
                    if (item.getMemberParentId() == 2) {
                        annualFeeStatus = item.getAnnualFeeStatus();
                        memberPeriod = item.getMemberPeriod();
                        annualFee = item.getAnnualFee();
                    }
                } else {
                    if (memberTypeId.equals(item.getMemberTypeId())) {
                        annualFeeStatus = item.getAnnualFeeStatus();
                        memberPeriod = item.getMemberPeriod();
                        memberEndDate = item.getMemberEndDate();
                        annualFee = item.getAnnualFee();
                    }
                }
            }

            IteeTextView tvOverdue = new IteeTextView(getBaseActivity());
            tvOverdue.setText(getString(R.string.overdue_mes));
            tvOverdue.setTextSize(Constants.FONT_SIZE_MORE_SMALLER);
            RelativeLayout.LayoutParams tvOverdueParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, getActualWidthOnThisDevice(50));
            tvOverdue.setGravity(Gravity.BOTTOM);
            tvOverdue.setTextColor(getColor(R.color.common_red));

            if (Constants.STR_0.equals(overdueFlag)) {

                tvOverdue.setVisibility(View.GONE);
            } else {
                tvOverdue.setVisibility(View.VISIBLE);

            }


            tvOverdueParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            tvOverdueParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            tvOverdueParams.rightMargin = getActualWidthOnThisDevice(40);

            tvOverdue.setLayoutParams(tvOverdueParams);
            // set layout by annualFeeStatus
            if (annualFeeStatus == null || annualFeeStatus == 0) {
                rlAnnualFeeContainer.setVisibility(View.GONE);
                rlPaymentDateContainer.setVisibility(View.GONE);
                rlEndDateContainer.setVisibility(View.GONE);
                llAnnualFeeAllContainer.setVisibility(View.GONE);

                rlCreateDateContainer.addView(tvOverdue);
            } else {
                rlEndDateContainer.addView(tvOverdue);
                rlAnnualFeeContainer.setVisibility(View.VISIBLE);
                tvAnnualFeeValue.setText(getString(R.string.player_info_renewal));
                tvAnnualFeeValue.setBackgroundResource(R.drawable.btn_blue_frame);
            }

            //get member's annualFeeStatus.


            Integer confirmStatus = dataList.getConfirmStatus();
            if (confirmStatus == 0) {
                tvPaymentDateValue.setText(getString(R.string.common_confirm));
                tvPaymentDateValue.setBackgroundResource(R.drawable.btn_blue_frame);
                tvPaymentDateValue.setTextColor(getColor(R.color.common_blue));
                if (getFragmentMode() != FragmentMode.FragmentModeBrowse){
                    rlPaymentDateContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            netLinkConfirmPayment();
                        }
                    });
                }
            } else if (confirmStatus == 1) {
                String date = DateUtils.getCurrentShowYearMonthDayFromApiDateStr(dataParameter.getDataList().getPaymentDate(), mContext);
                tvPaymentDateValue.setText(date);
                tvPaymentDateValue.setBackground(null);
                tvPaymentDateValue.setTextColor(getColor(R.color.common_black));
                rlPaymentDateContainer.setOnClickListener(null);
            }

            tvPaymentEndDateValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(dataList.getEndDateOld(), mContext));

            if (isNonMember != null && isNonMember) {
                tvEffectiveDateValue.setText(Constants.STR_EMPTY);
                tvEndDateFirstValue.setText(Constants.STR_EMPTY);
            }
        }


        tvIDType.setText(getString(R.string.player_info_membership_id_type));
        imIDType.setImageResource(R.drawable.icon_right_arrow);
        imEffectiveDate.setImageResource(R.drawable.icon_right_arrow);
        imEndDateFirst.setImageResource(R.drawable.icon_right_arrow);
        imEndDate.setImageResource(R.drawable.icon_right_arrow);
        imSendMail.setImageResource(R.drawable.icon_right_arrow);

        tvID.setText(getString(R.string.player_info_membership_id));
        tvEffectiveDate.setText(getString(R.string.player_info_membership_effective_date));
        tvEndDateFirst.setText(getString(R.string.player_info_membership_create_date));
        tvAnnualFee.setText(getString(R.string.player_info_membership_annual_fee));
        tvPaymentDate.setText(getString(R.string.player_info_membership_payment_date));
        tvPaymentEndDate.setText(getString(R.string.player_info_membership_end_date));
        tvSendEmail.setText(getString(R.string.player_info_membership_send_email));
    }

    @Override
    protected void setListenersOfControls() {

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentMode() == FragmentMode.FragmentModeEdit) {
                    Bundle bundle = new Bundle();
                    bundle.putString("annualFee", annualFee);
                    bundle.putString("endDate", DateUtils.getAPIYearMonthDayFromCurrentShow(tvPaymentEndDateValue.getText().toString(), mContext));
                    bundle.putInt("member_id", memberId);
                    bundle.putInt("payment_id", dataParameter.getDataList().getPaymentId());
                    bundle.putString("split_amount", dataParameter.getDataList().getSplitAmount());
                    push(PlayerRenewalFragment.class, bundle);
                }

            }
        };

        rlAnnualFeeContainer.setOnClickListener(onClickListener);
        tvAnnualFeeValue.setOnClickListener(onClickListener);

        rlIDTypeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getFragmentMode() == FragmentMode.FragmentModeEdit) {
                    List<JsonMemberShip.MemberTypeListItem> list = dataParameter.getDataList().getMemberTypeList();

                    OnIDTypeSelectClickListener listener = new OnIDTypeSelectClickListener() {
                        @Override
                        public void returnMember(String flag, JsonMemberShip.MemberTypeListItem member) {
                            switch (flag) {
                                case "1":
                                    if (member.getMemberParentId() == 2) {
                                        setCloseItemList(View.VISIBLE);
                                    } else {
                                        setCloseItemList(View.GONE);
                                    }
                                    tvIDTypeValue.setText(member.getMemberType());
                                    memberTypeId = member.getMemberTypeId();
                                    memberType = member.getMemberType();
                                    memberPeriod = member.getMemberPeriod();
                                    memberEndDate = member.getMemberEndDate();
                                    if (StringUtils.isEmpty(tvEffectiveDateValue.getText())) {
                                        tvEffectiveDateValue.setText(DateUtils.getCurrentShowYearMonthDayFromDate(new Date(), mContext));
                                    }
                                    String currentShowEffectiveDate = tvEffectiveDateValue.getText().toString();
                                    String apiEffectiveDate = DateUtils.getAPIYearMonthDayFromCurrentShow(currentShowEffectiveDate, mContext);
                                    if (memberPeriod != null && memberPeriod > 0) {
                                        String endDate = UtilsIsSerialDate.getCutMonthDateOblique(apiEffectiveDate, memberPeriod);
                                        tvEndDateFirstValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(endDate, mContext));
                                    } else {
                                        tvEndDateFirstValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(memberEndDate, mContext));
                                    }

                                    if (Constants.CUSTOMER_NON_MEMBER.equals(String.valueOf(member.getMemberParentId()))) {
                                        tvEffectiveDateValue.setText(StringUtils.EMPTY);
                                        tvEndDateFirstValue.setText(StringUtils.EMPTY);
                                    }

                                    break;
                                default:
                                    break;
                            }

                        }
                    };
                    Utils.hideKeyboard(getActivity());
                    Bundle bundle = new Bundle();
                    bundle.putInt("memberId", memberId);
                    bundle.putString("memberType", memberType);
                    bundle.putString("memberEmail", memberEmail);
                    bundle.putInt("memberTypeId", memberTypeId);
                    bundle.putString("memberNo", memberNo);

                    SelectIDTypePopupWindow menuWindow = new SelectIDTypePopupWindow(getActivity(), list, listener, bundle);
                    menuWindow.showAtLocation(getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }

            }
        });

        rlSendMailContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                    boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlMembershipManagement, getActivity());
                    if (hasPermission) {
                        Utils.hideKeyboard(getActivity());

                        String message = dataParameter.getDataList().getMessage();
                        String subject = getString(R.string.app_name) + Constants.STR_SEPARATOR
                                + getString(R.string.player_info_membership);
                        String[] receiver = new String[]{memberEmail};
                        String myCc = "cc";
                        Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);
                        myIntent.setType(Constants.EMAIL_HTML_TYPE);
                        myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, receiver);
                        myIntent.putExtra(android.content.Intent.EXTRA_CC, myCc);
                        myIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                        myIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(message));
                        startActivity(Intent.createChooser(myIntent, "Send Mail To"));
                    }
                }
            }
        });


        rlEndDateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getBaseActivity());
                if (getFragmentMode() == FragmentMode.FragmentModeEdit) {

                    dateSelectReturn = new SelectDatePopupWindow.OnDateSelectClickListener() {
                        @Override
                        public void OnGoodItemClick(String flag, String content) {
                            switch (flag) {
                                case Constants.DATE_RETURN:
                                    tvPaymentEndDateValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(content, getActivity()));
                                    break;
                            }
                        }
                    };
                    String date = DateUtils.getAPIYearMonthDayFromCurrentShow(tvPaymentEndDateValue.getText().toString(), mContext);
                    if (Utils.isStringNullOrEmpty(date)) {
                        date = DateUtils.getCurrentShowYearMonthDayFromDate(new Date(), mContext);
                    }
                    SelectDatePopupWindow menuWindow = new SelectDatePopupWindow(getActivity(), date, dateSelectReturn);
                    menuWindow.showAtLocation(getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
        });

        rlEffectiveDateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.hideKeyboard(getBaseActivity());

                if (getFragmentMode() == FragmentMode.FragmentModeEdit) {

                    dateSelectReturn = new SelectDatePopupWindow.OnDateSelectClickListener() {
                        @Override
                        public void OnGoodItemClick(String flag, String content) {
                            switch (flag) {
                                case Constants.DATE_RETURN:
                                    tvEffectiveDateValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(content, getActivity()));
                                    if (memberPeriod != null && memberPeriod > 0) {
                                        String endDate = UtilsIsSerialDate.getCutMonthDateOblique(content, memberPeriod);
                                        tvEndDateFirstValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(endDate, mContext));
                                    } else {
                                        tvEndDateFirstValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(memberEndDate, mContext));
                                    }
                                    break;
                            }

                        }
                    };

                    SelectDatePopupWindow menuWindow = new SelectDatePopupWindow(getActivity(),
                            tvEffectiveDateValue.getText().toString(), dateSelectReturn);
                    menuWindow.showAtLocation(getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                    rlEffectiveDateContainer.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
                }

            }
        });

        rlCreateDateContainer.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                Utils.hideKeyboard(getBaseActivity());

                if (getFragmentMode() == FragmentMode.FragmentModeEdit) {

                    dateSelectReturn = new SelectDatePopupWindow.OnDateSelectClickListener() {
                        @Override
                        public void OnGoodItemClick(String flag, String content) {
                            switch (flag) {
                                case Constants.DATE_RETURN:
                                    tvEndDateFirstValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(content, getActivity()));
                                    break;
                            }

                        }
                    };
                    SelectDatePopupWindow menuWindow = new SelectDatePopupWindow(getActivity(),
                            tvEndDateFirstValue.getText().toString(), dateSelectReturn);
                    menuWindow.showAtLocation(getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    rlCreateDateContainer.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
                }
            }
        });
    }

    @Override
    protected void setLayoutOfControls() {

        LinearLayout.LayoutParams rlPhotoLayout = (LinearLayout.LayoutParams) rlIDTypeContainer.getLayoutParams();
        rlPhotoLayout.height = DensityUtil.getActualHeightOnThisDevice(100, mContext);

        rlIDTypeContainer.setLayoutParams(rlPhotoLayout);
        rlIDContainer.setLayoutParams(rlPhotoLayout);
        rlEffectiveDateContainer.setLayoutParams(rlPhotoLayout);
        rlCreateDateContainer.setLayoutParams(rlPhotoLayout);
        rlAnnualFeeContainer.setLayoutParams(rlPhotoLayout);
        rlPaymentDateContainer.setLayoutParams(rlPhotoLayout);
        rlEndDateContainer.setLayoutParams(rlPhotoLayout);
        rlSendMailContainer.setLayoutParams(rlPhotoLayout);

        rlIDTypeContainer.addView(tvIDType);
        RelativeLayout.LayoutParams paramsEtLogoutAfterTimeM = (RelativeLayout.LayoutParams) tvIDType.getLayoutParams();
        paramsEtLogoutAfterTimeM.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsEtLogoutAfterTimeM.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsEtLogoutAfterTimeM.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsEtLogoutAfterTimeM.setMargins(getActualWidthOnThisDevice(40), 0, 0, 0);
        tvIDType.setLayoutParams(paramsEtLogoutAfterTimeM);

        rlIDTypeContainer.addView(imIDType);
        RelativeLayout.LayoutParams paramsPhotoImageViewValue = (RelativeLayout.LayoutParams) imIDType.getLayoutParams();
        paramsPhotoImageViewValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsPhotoImageViewValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsPhotoImageViewValue.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        paramsPhotoImageViewValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsPhotoImageViewValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        imIDType.setLayoutParams(paramsPhotoImageViewValue);
        imIDType.setId(View.generateViewId());

        rlIDTypeContainer.addView(tvIDTypeValue);
        RelativeLayout.LayoutParams paramsIdTypeValue = (RelativeLayout.LayoutParams) tvIDTypeValue.getLayoutParams();
        paramsIdTypeValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIdTypeValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIdTypeValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsIdTypeValue.addRule(RelativeLayout.LEFT_OF, imIDType.getId());
        tvIDTypeValue.setLayoutParams(paramsIdTypeValue);


        rlIDContainer.addView(tvID);
        RelativeLayout.LayoutParams paramsId = (RelativeLayout.LayoutParams) tvID.getLayoutParams();
        paramsId.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsId.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsId.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsId.setMargins(getActualWidthOnThisDevice(40), 0, 0, 0);
        tvID.setLayoutParams(paramsId);


        rlIDContainer.addView(tvIDValue);
        RelativeLayout.LayoutParams paramsIdValue = (RelativeLayout.LayoutParams) tvIDValue.getLayoutParams();
        paramsIdValue.width = (int) (getScreenWidth() * 0.4f);
        paramsIdValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIdValue.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        paramsIdValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIdValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvIDValue.setLayoutParams(paramsIdValue);

        rlEffectiveDateContainer.addView(tvEffectiveDate);
        RelativeLayout.LayoutParams paramsName = (RelativeLayout.LayoutParams) tvEffectiveDate.getLayoutParams();
        paramsName.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsName.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsName.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsName.setMargins(getActualWidthOnThisDevice(40), 0, 0, 0);
        tvEffectiveDate.setLayoutParams(paramsName);


        rlEffectiveDateContainer.addView(imEffectiveDate);
        RelativeLayout.LayoutParams paramsImEffectiveDate = (RelativeLayout.LayoutParams) imEffectiveDate.getLayoutParams();
        paramsImEffectiveDate.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsImEffectiveDate.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsImEffectiveDate.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        paramsImEffectiveDate.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsImEffectiveDate.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        imEffectiveDate.setLayoutParams(paramsImEffectiveDate);
        imEffectiveDate.setId(View.generateViewId());

        rlEffectiveDateContainer.addView(tvEffectiveDateValue);
        RelativeLayout.LayoutParams paramsEffectiveValue = (RelativeLayout.LayoutParams) tvEffectiveDateValue.getLayoutParams();
        paramsEffectiveValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsEffectiveValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsEffectiveValue.addRule(RelativeLayout.LEFT_OF, imEffectiveDate.getId());
        paramsEffectiveValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);


        tvEffectiveDateValue.setLayoutParams(paramsEffectiveValue);


        rlCreateDateContainer.addView(tvEndDateFirst);
        RelativeLayout.LayoutParams paramsOccupation = (RelativeLayout.LayoutParams) tvEndDateFirst.getLayoutParams();
        paramsOccupation.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsOccupation.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsOccupation.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsOccupation.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsOccupation.setMargins(getActualWidthOnThisDevice(40), 0, 0, 0);
        tvEndDateFirst.setLayoutParams(paramsOccupation);

        rlCreateDateContainer.addView(imEndDateFirst);
        RelativeLayout.LayoutParams paramsImEndDateFirst = (RelativeLayout.LayoutParams) imEndDateFirst.getLayoutParams();
        paramsImEndDateFirst.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsImEndDateFirst.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsImEndDateFirst.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        paramsImEndDateFirst.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsImEndDateFirst.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        imEndDateFirst.setLayoutParams(paramsImEndDateFirst);
        imEndDateFirst.setId(View.generateViewId());

        rlCreateDateContainer.addView(tvEndDateFirstValue);
        RelativeLayout.LayoutParams paramsTvEndDateFirstValue = (RelativeLayout.LayoutParams) tvEndDateFirstValue.getLayoutParams();
        paramsTvEndDateFirstValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvEndDateFirstValue.setLayoutParams(paramsTvEndDateFirstValue);
        LayoutUtils.setLeftOfView(tvEndDateFirstValue, imEndDateFirst, 0, mContext);


        rlAnnualFeeContainer.addView(tvAnnualFee);
        RelativeLayout.LayoutParams paramsAnnualFee = (RelativeLayout.LayoutParams) tvAnnualFee.getLayoutParams();
        paramsAnnualFee.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAnnualFee.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAnnualFee.leftMargin = getActualWidthOnThisDevice(40);
        paramsAnnualFee.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsAnnualFee.setMargins(getActualWidthOnThisDevice(40), 0, 0, 0);
        tvAnnualFee.setLayoutParams(paramsAnnualFee);

        rlAnnualFeeContainer.addView(tvAnnualFeeValue);
        RelativeLayout.LayoutParams paramsAnnualFeeValue = (RelativeLayout.LayoutParams) tvAnnualFeeValue.getLayoutParams();
        paramsAnnualFeeValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAnnualFeeValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAnnualFeeValue.rightMargin = getActualWidthOnThisDevice(40);
        paramsAnnualFeeValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsAnnualFeeValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvAnnualFeeValue.setLayoutParams(paramsAnnualFeeValue);


        rlPaymentDateContainer.addView(tvPaymentDate);
        RelativeLayout.LayoutParams paramsGenderInfo = (RelativeLayout.LayoutParams) tvPaymentDate.getLayoutParams();
        paramsGenderInfo.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsGenderInfo.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsGenderInfo.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        paramsGenderInfo.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsGenderInfo.setMargins(getActualWidthOnThisDevice(40), 0, 0, 0);
        tvPaymentDate.setLayoutParams(paramsGenderInfo);


        rlPaymentDateContainer.addView(tvPaymentDateValue);
        RelativeLayout.LayoutParams paramsGenderInfoValue = (RelativeLayout.LayoutParams) tvPaymentDateValue.getLayoutParams();
        paramsGenderInfoValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsGenderInfoValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsGenderInfoValue.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsGenderInfoValue.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        paramsGenderInfoValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsGenderInfoValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvPaymentDateValue.setLayoutParams(paramsGenderInfoValue);

        rlEndDateContainer.addView(tvPaymentEndDate);
        RelativeLayout.LayoutParams paramsAccept = (RelativeLayout.LayoutParams) tvPaymentEndDate.getLayoutParams();
        paramsAccept.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAccept.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAccept.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsAccept.setMargins(getActualWidthOnThisDevice(40), 0, 0, 0);
        tvPaymentEndDate.setLayoutParams(paramsAccept);

        rlEndDateContainer.addView(imEndDate);
        RelativeLayout.LayoutParams paramsEffective = (RelativeLayout.LayoutParams) imEndDate.getLayoutParams();
        paramsEffective.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsEffective.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsEffective.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        paramsEffective.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsEffective.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        imEndDate.setLayoutParams(paramsEffective);
        imEndDate.setId(View.generateViewId());

        rlEndDateContainer.addView(tvPaymentEndDateValue);
        RelativeLayout.LayoutParams paramsSwitchAccept = (RelativeLayout.LayoutParams) tvPaymentEndDateValue.getLayoutParams();
        paramsSwitchAccept.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsSwitchAccept.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsSwitchAccept.addRule(RelativeLayout.LEFT_OF, imEndDate.getId());
        paramsSwitchAccept.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        paramsSwitchAccept.rightMargin = getActualWidthOnThisDevice(40);
        tvPaymentEndDateValue.setLayoutParams(paramsSwitchAccept);


        rlSendMailContainer.addView(tvSendEmail);
        RelativeLayout.LayoutParams paramstvHobbies = (RelativeLayout.LayoutParams) tvSendEmail.getLayoutParams();
        paramstvHobbies.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramstvHobbies.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramstvHobbies.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramstvHobbies.setMargins(getActualWidthOnThisDevice(40), 0, 0, 0);
        tvSendEmail.setLayoutParams(paramstvHobbies);
        tvSendEmail.setId(View.generateViewId());

        rlSendMailContainer.addView(imSendMail);
        RelativeLayout.LayoutParams paramsImSendMail = (RelativeLayout.LayoutParams) imSendMail.getLayoutParams();
        paramsImSendMail.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsImSendMail.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsImSendMail.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        paramsImSendMail.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsImSendMail.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        imSendMail.setLayoutParams(paramsImSendMail);


        RelativeLayout.LayoutParams l2Params
                = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        LinearLayout l2 = new LinearLayout(getBaseActivity());
        l2.setLayoutParams(l2Params);


        RelativeLayout.LayoutParams l3Params
                = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        LinearLayout l3 = new LinearLayout(getBaseActivity());
        l3.setLayoutParams(l3Params);

        RelativeLayout.LayoutParams l4Params
                = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        LinearLayout l4 = new LinearLayout(getBaseActivity());
        l4.setLayoutParams(l4Params);

        RelativeLayout.LayoutParams l5Params
                = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        LinearLayout l5 = new LinearLayout(getBaseActivity());
        l5.setLayoutParams(l5Params);

        RelativeLayout.LayoutParams l6Params
                = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        LinearLayout l6 = new LinearLayout(getBaseActivity());
        l6.setLayoutParams(l6Params);

        RelativeLayout.LayoutParams l7Params
                = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        LinearLayout l7 = new LinearLayout(getBaseActivity());
        l7.setLayoutParams(l7Params);

        l2.setBackgroundColor(getColor(R.color.common_gray));
        l3.setBackgroundColor(getColor(R.color.common_gray));
        l4.setBackgroundColor(getColor(R.color.common_gray));
        l5.setBackgroundColor(getColor(R.color.common_gray));
        l6.setBackgroundColor(getColor(R.color.common_gray));
        l7.setBackgroundColor(getColor(R.color.common_gray));

        rlEffectiveDateContainer.addView(l2);
        rlCreateDateContainer.addView(l3);
        rlAnnualFeeContainer.addView(l4);
        llAnnualFeeAllContainer.addView(l5);
        rlPaymentDateContainer.addView(l6);
        rlEndDateContainer.addView(l7);

        l2.setOnClickListener(null);
        l3.setOnClickListener(null);
        l4.setOnClickListener(null);
        l5.setOnClickListener(null);
        l6.setOnClickListener(null);
        l7.setOnClickListener(null);

        closeItemList.add(l2);
        closeItemList.add(l3);
        closeItemList.add(l4);
        closeItemList.add(l5);
        closeItemList.add(l6);
        closeItemList.add(l7);

        l2.setAlpha(0.8f);
        l3.setAlpha(0.8f);
        l4.setAlpha(0.8f);
        l5.setAlpha(0.8f);
        l6.setAlpha(0.8f);
        l7.setAlpha(0.8f);

        setCloseItemList(View.GONE);

        if (isNonMember) {
            setCloseItemList(View.VISIBLE);
        }
    }

    private void setCloseItemList(int visibility) {
        for (LinearLayout l : closeItemList) {
            l.setVisibility(visibility);
        }
    }

    @Override
    protected void setPropertyOfControls() {

        tvIDType.setTextColor(getColor(R.color.common_black));
        tvIDTypeValue.setTextColor(getColor(R.color.common_black));
        tvIDTypeValue.setGravity(Gravity.END);
        tvID.setTextColor(getColor(R.color.common_black));

        tvIDValue.setTextSize(Constants.FONT_SIZE_SMALLER);
        tvIDValue.setSingleLine();
        tvIDValue.setBackground(null);
        tvIDValue.setGravity(Gravity.END);

        tvEffectiveDate.setTextColor(getColor(R.color.common_black));
        tvEffectiveDateValue.setTextColor(getColor(R.color.common_black));


        tvEndDateFirst.setTextColor(getColor(R.color.common_black));

        tvAnnualFee.setTextColor(getColor(R.color.common_black));
        tvAnnualFeeValue.setTextColor(getColor(R.color.common_blue));
        tvAnnualFeeValue.setGravity(Gravity.CENTER);
        tvPaymentDate.setTextColor(getColor(R.color.common_black));
        tvPaymentDateValue.setGravity(Gravity.CENTER);
        tvPaymentEndDate.setTextColor(getColor(R.color.common_black));
        tvSendEmail.setTextColor(getColor(R.color.common_black));
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getResources().getString(R.string.player_info_membership));
        getTvRight().setBackground(null);
        if (getFragmentMode() == FragmentMode.FragmentModeEdit) {
            getTvRight().setText(R.string.common_ok);
            getTvRight().setBackground(null);
        } else {
            getTvRight().setBackgroundResource(R.drawable.administration_icon_edit);
            getTvRight().setText(Constants.STR_EMPTY);
        }

        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentMode() == FragmentMode.FragmentModeEdit) {
                    if (doCheck()) {
                        netLinkEditMemberShip();
                    }
                } else {
                    setFragmentMode(FragmentMode.FragmentModeEdit);
                    changeEdit();
                }
            }
        });

        boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlMembershipManagement, getActivity());
        if (hasPermission) {
            getTvRight().setVisibility(View.VISIBLE);
        } else {
            getTvRight().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (tvIDValue != null) {
            CloseKeyboardHandler handler = new CloseKeyboardHandler(this);
            handler.sendMessageDelayed(new Message(), 20);
        }
    }

    private void changeEdit() {
        if (getTvRight() != null) {
            if (getFragmentMode() == FragmentMode.FragmentModeEdit) {
                tvIDValue.setEnabled(true);
                tvIDTypeValue.setEnabled(true);
                getTvRight().setText(R.string.common_ok);
                getTvRight().setBackground(null);
            } else {
                tvIDValue.setEnabled(false);
                tvIDTypeValue.setEnabled(false);
                getTvRight().setBackgroundResource(R.drawable.administration_icon_edit);
                getTvRight().setText(Constants.STR_EMPTY);
            }
        }
    }

    private boolean doCheck() {
        boolean res = Utils.isSecondDateLaterThanFirst(tvEffectiveDateValue.getText().toString(),
                tvEndDateFirstValue.getText().toString(),
                DateUtils.getShowYearMonthDayFormat(mContext));
        if (!res) {
            Utils.showShortToast(mContext, getString(R.string.msg_end_date_must_be_later_than_effective_date));
        }
        return res;
    }

    @Override
    protected void reShowWithBackValue() {
        Bundle bundle = getReturnValues();
        if (bundle != null) {
            netLinkMemberShip();
        }
    }

    @Override
    protected void executeOnceOnCreate() {
        netLinkMemberShip();
        super.executeOnceOnCreate();
    }

    @Override
    protected void executeEveryOnCreate() {
        Utils.hideKeyboard(getActivity());
        super.executeEveryOnCreate();
    }

    private void netLinkMemberShip() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.PLAYER_MEMBER_ID, String.valueOf(memberId));

        HttpManager<JsonMemberShip> hh = new HttpManager<JsonMemberShip>(this) {

            @Override
            public void onJsonSuccess(JsonMemberShip jo) {
                dataParameter = jo;
                changeEdit();
                setDefaultValueOfControls();
                setPricingTable();
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.MemberShip, params);
    }

    private void netLinkEditMemberShip() {


        Map<String, String> params = new HashMap<>();

        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.PLAYER_MEMBER_ID, String.valueOf(memberId));
        params.put(ApiKey.PLAYER_MEMBER_SHIP_MEMBER_TYPE_ID, String.valueOf(memberTypeId));
        params.put(ApiKey.PLAYER_MEMBER_SHIP_MEMBER_NO, tvIDValue.getText().toString());
        String effectiveDate = DateUtils.getAPIYearMonthDayFromCurrentShow(tvEffectiveDateValue.getText().toString(), mContext);
        params.put(ApiKey.PLAYER_MEMBER_SHIP_EFFECTIVE_DATE, effectiveDate);
        String effectiveEndDate = DateUtils.getAPIYearMonthDayFromCurrentShow(tvEndDateFirstValue.getText().toString(), mContext);
        params.put(ApiKey.PLAYER_MEMBER_SHIP_EFFECTIVE_END_DATE, effectiveEndDate);
        String endDate = DateUtils.getAPIYearMonthDayFromCurrentShow(tvPaymentEndDateValue.getText().toString(), mContext);
        params.put(ApiKey.PLAYER_MEMBER_SHIP_END_DATE, endDate);

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(this) {
            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    doBack();
                } else {
                    Utils.showShortToast(mContext, msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.MemberShipEdit, params);
    }

    private void netLinkConfirmPayment() {


        Map<String, String> params = new HashMap<>();

        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.PLAYER_MEMBER_PAYMENT_ID, String.valueOf(dataParameter.getDataList().getPaymentId()));

        HttpManager<JsonConfirmPayment> hh = new HttpManager<JsonConfirmPayment>(this) {
            @Override
            public void onJsonSuccess(JsonConfirmPayment jo) {
                int returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    tvPaymentDateValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(jo.getDataList().getEndDate(), mContext));
                    tvPaymentDateValue.setBackground(null);
                    tvPaymentDateValue.setTextColor(getColor(R.color.common_black));
                    rlPaymentDateContainer.setOnClickListener(null);


                }

            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.ConfirmPayment, params);
    }

    /**
     * 单击事件监听器
     */

    public interface OnIDTypeSelectClickListener {
        void returnMember(String flag, JsonMemberShip.MemberTypeListItem member);
    }

    static class CloseKeyboardHandler extends Handler {
        private final WeakReference<PlayerMemberShipEditFragment> mFragment;

        public CloseKeyboardHandler(PlayerMemberShipEditFragment fragment) {
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            PlayerMemberShipEditFragment fragment = mFragment.get();
            if (fragment != null) {
                fragment.tvIDValue.setEnabled(true);
                InputMethodManager keyboard = (InputMethodManager) fragment.getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(fragment.tvIDValue, InputMethodManager.SHOW_FORCED);

                keyboard.hideSoftInputFromWindow(fragment.tvIDValue.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                fragment.tvIDValue.setEnabled(false);
            }
        }
    }
}
