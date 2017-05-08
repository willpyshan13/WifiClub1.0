/**
 * Project Name: itee
 * File Name:  PlayerRenewalFragment.java
 * Package Name: cn.situne.itee.fragment.player
 * Date:   2015-03-28
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.player;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.baoyz.actionsheet.ActionSheet;

import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.common.utils.UtilsIsSerialDate;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.view.IteeMoneyEditText;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.SelectDatePopupWindow;

/**
 * ClassName:PlayerRenewalFragment <br/>
 * Function: member's renewal detai list  fragment. <br/>
 * UI:  04-8-3-1
 * Date: 2015-03-28 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */

public class PlayerRenewalFragment extends BaseFragment {

    private RelativeLayout rlAnnualFeeContainer;
    private RelativeLayout rlSplitContainer;
    private RelativeLayout rlPaymentPatternContainer;
    private RelativeLayout rlEndDateContainer;
    private RelativeLayout rlConfirmContainer;

    private ImageView imEndDate;

    private IteeTextView tvAnnualFee;
    private IteeTextView tvAnnualFeeValue;

    private IteeTextView tvSplit;
    private IteeMoneyEditText tvSplitValue;

    private IteeTextView tvPaymentDate;

    private IteeTextView tvPaymentDateValue;
    private IteeTextView tvEndDate;

    private IteeTextView tvEndDateValue;

    private IteeTextView tvConfirm;
    private String annualFee;
    private String endDateValueString;

    private Integer memberId;
    private Integer paymentId;
    private String splitAmount;
    private String rechargeType;
    final ActionSheet.ActionSheetListener actionSheetListenerAddress = new ActionSheet.ActionSheetListener() {
        @Override
        public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
        }

        @Override
        public void onDismissWithCancelButton(ActionSheet actionSheet) {
        }

        @Override
        public void onOtherButtonClick(ActionSheet actionSheet, int index) {

            switch (index) {
                case 0:
                    rechargeType = "1";
                    tvPaymentDateValue.setText(getString(R.string.tag_pay_cash));
                    break;
                case 1:
                    rechargeType = "5";
                    tvPaymentDateValue.setText(getString(R.string.tag_balance_account));

                    break;
                case 2:
                    rechargeType = "2";
                    tvPaymentDateValue.setText(getString(R.string.tag_voucher));
                    break;
                case 3:
                    rechargeType = "3";
                    tvPaymentDateValue.setText(getString(R.string.tag_credit_card));
                    break;
                case 4:
                    rechargeType = "4";
                    tvPaymentDateValue.setText(getString(R.string.tag_third_party));
                    break;
                case 5:
                    rechargeType = "6";
                    tvPaymentDateValue.setText(getString(R.string.tag_bank_transfer));
                    break;

            }
            actionSheet.dismiss();
        }
    };
    private SelectDatePopupWindow.OnDateSelectClickListener dateSelectReturn;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_player_renewal;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.player_info_renewal;
    }

    @Override
    protected void initControls(View rootView) {

        rlAnnualFeeContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_renewal_annual_fee);
        rlPaymentPatternContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_renewal_payment_pattern);
        rlEndDateContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_renewal_end_date);
        rlSplitContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_renewal_split);
        rlConfirmContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_renewal_confirm);

        imEndDate = new ImageView(getActivity());

        tvAnnualFee = new IteeTextView(getActivity());
        tvAnnualFeeValue = new IteeTextView(getActivity());

        tvPaymentDate = new IteeTextView(getActivity());
        tvPaymentDateValue = new IteeTextView(getActivity());

        tvEndDate = new IteeTextView(getActivity());
        tvEndDateValue = new IteeTextView(getActivity());

        tvSplit = new IteeTextView(getActivity());
        tvSplitValue = new IteeMoneyEditText(this);
        tvConfirm = new IteeTextView(getActivity());

    }

    @Override
    protected void setDefaultValueOfControls() {

        imEndDate.setImageResource(R.drawable.icon_right_arrow);

        tvAnnualFee.setText(getString(R.string.player_info_renewal_annual_fee));
        tvAnnualFeeValue.setText(AppUtils.getCurrentCurrency(getActivity()) + annualFee);

        tvSplit.setText(getString(R.string.player_info_renewal_split));
        tvSplitValue.setValue(splitAmount);

        tvEndDate.setText(getString(R.string.player_info_renewal_end_date));
        tvEndDateValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(endDateValueString, mContext));

        tvPaymentDate.setText(getString(R.string.player_info_renewal_payment_pattern));

        String paymentPattern = AppUtils.getDefaultPaymentPattern(mContext);
        if (paymentPattern != null) {
            switch (paymentPattern) {
                case "1":
                    rechargeType = "1";
                    tvPaymentDateValue.setText(getString(R.string.tag_pay_cash));
                    break;
                case "5":
                    rechargeType = "5";
                    tvPaymentDateValue.setText(getString(R.string.tag_balance_account));
                    break;
                case "2":
                    rechargeType = "2";
                    tvPaymentDateValue.setText(getString(R.string.tag_voucher));
                    break;
                case "3":
                    rechargeType = "3";
                    tvPaymentDateValue.setText(getString(R.string.tag_credit_card));
                    break;
                case "4":
                    rechargeType = "4";
                    tvPaymentDateValue.setText(getString(R.string.tag_third_party));
                    break;
                case "6":
                    rechargeType = "6";
                    tvPaymentDateValue.setText(getString(R.string.tag_bank_transfer));
                    break;
                default:
                    break;
            }
        }


        tvConfirm.setText(getResources().getString(R.string.common_confirm));

    }

    @Override
    protected void setListenersOfControls() {

        rlPaymentPatternContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseActivity().setTheme(com.baoyz.actionsheet.R.style.ActionSheetStyleIOS7);
                String[] tTags = new String[]{getString(R.string.tag_pay_cash), getString(R.string.tag_balance_account), getString(R.string.tag_voucher)
                        , getString(R.string.tag_credit_card), getString(R.string.tag_third_party), getString(R.string.tag_bank_transfer)};

                ActionSheet.createBuilder(getActivity(), getFragmentManager())
                        .setOtherButtonTitles(tTags)
                        .setCancelButtonHidden(true)
                        .setCancelableOnTouchOutside(true).setListener(actionSheetListenerAddress).show();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netLinkAnnualFeeRenewal();
            }
        });

        rlEndDateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.hideKeyboard(getActivity());
                tvSplitValue.clearFocus();
                dateSelectReturn = new SelectDatePopupWindow.OnDateSelectClickListener() {
                    @Override
                    public void OnGoodItemClick(String flag, String content) {
                        switch (flag) {
                            case Constants.DATE_RETURN:
                                tvEndDateValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(content, getActivity()));
                                break;
                        }

                    }
                };
                String apiFormatDate = DateUtils.getAPIYearMonthDayFromCurrentShow(tvEndDateValue.getText().toString(), mContext);
                SelectDatePopupWindow menuWindow = new SelectDatePopupWindow(getActivity(), apiFormatDate, dateSelectReturn);
                menuWindow.showAtLocation(getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }

    @Override
    protected void setLayoutOfControls() {


        LinearLayout.LayoutParams rlPhotoLayout = (LinearLayout.LayoutParams) rlAnnualFeeContainer.getLayoutParams();
        rlPhotoLayout.height = (int) (getScreenHeight() * 0.08f);


        rlAnnualFeeContainer.setLayoutParams(rlPhotoLayout);
        rlPaymentPatternContainer.setLayoutParams(rlPhotoLayout);
        rlEndDateContainer.setLayoutParams(rlPhotoLayout);
        rlSplitContainer.setLayoutParams(rlPhotoLayout);
        rlConfirmContainer.setLayoutParams(rlPhotoLayout);

        rlAnnualFeeContainer.setPadding(20, 0, 20, 0);
        rlPaymentPatternContainer.setPadding(20, 0, 20, 0);
        rlEndDateContainer.setPadding(20, 0, 20, 0);
        rlSplitContainer.setPadding(20, 0, 20, 0);
        rlConfirmContainer.setPadding(20, 0, 20, 0);

        rlAnnualFeeContainer.addView(tvAnnualFee);
        RelativeLayout.LayoutParams paramsBirth = (RelativeLayout.LayoutParams) tvAnnualFee.getLayoutParams();
        paramsBirth.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBirth.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBirth.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsBirth.setMargins(20, 0, 0, 0);
        tvAnnualFee.setLayoutParams(paramsBirth);


        rlAnnualFeeContainer.addView(tvAnnualFeeValue);
        RelativeLayout.LayoutParams paramsBirthValue = (RelativeLayout.LayoutParams) tvAnnualFeeValue.getLayoutParams();
        paramsBirthValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBirthValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBirthValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsBirthValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvAnnualFeeValue.setLayoutParams(paramsBirthValue);


        rlPaymentPatternContainer.addView(tvPaymentDate);
        RelativeLayout.LayoutParams paramsGenderInfo = (RelativeLayout.LayoutParams) tvPaymentDate.getLayoutParams();
        paramsGenderInfo.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsGenderInfo.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsGenderInfo.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsGenderInfo.setMargins(20, 0, 0, 0);
        tvPaymentDate.setLayoutParams(paramsGenderInfo);


        rlPaymentPatternContainer.addView(tvPaymentDateValue);
        RelativeLayout.LayoutParams paramsGenderInfoValue = (RelativeLayout.LayoutParams) tvPaymentDateValue.getLayoutParams();
        paramsGenderInfoValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsGenderInfoValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsGenderInfoValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsGenderInfoValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvPaymentDateValue.setLayoutParams(paramsGenderInfoValue);

        rlEndDateContainer.addView(tvEndDate);
        RelativeLayout.LayoutParams paramsAccept = (RelativeLayout.LayoutParams) tvEndDate.getLayoutParams();
        paramsAccept.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAccept.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAccept.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsAccept.setMargins(20, 0, 0, 0);
        tvEndDate.setLayoutParams(paramsAccept);


        rlEndDateContainer.addView(imEndDate);
        RelativeLayout.LayoutParams paramsEffective = (RelativeLayout.LayoutParams) imEndDate.getLayoutParams();
        paramsEffective.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsEffective.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsEffective.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        paramsEffective.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsEffective.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        imEndDate.setLayoutParams(paramsEffective);
        imEndDate.setId(View.generateViewId());


        rlEndDateContainer.addView(tvEndDateValue);
        RelativeLayout.LayoutParams paramsSwitchAccept = (RelativeLayout.LayoutParams) tvEndDateValue.getLayoutParams();
        paramsSwitchAccept.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsSwitchAccept.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsSwitchAccept.addRule(RelativeLayout.LEFT_OF, imEndDate.getId());
        paramsSwitchAccept.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvEndDateValue.setLayoutParams(paramsSwitchAccept);


        rlSplitContainer.addView(tvSplit);
        RelativeLayout.LayoutParams paramstvHobbies = (RelativeLayout.LayoutParams) tvSplit.getLayoutParams();
        paramstvHobbies.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramstvHobbies.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramstvHobbies.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramstvHobbies.setMargins(20, 0, 0, 0);
        tvSplit.setLayoutParams(paramstvHobbies);
        tvSplit.setId(View.generateViewId());


        rlSplitContainer.addView(tvSplitValue);
        RelativeLayout.LayoutParams paramsImSendMail = (RelativeLayout.LayoutParams) tvSplitValue.getLayoutParams();
        paramsImSendMail.width = (int) (getScreenWidth() * 0.3f);
        paramsImSendMail.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsImSendMail.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsImSendMail.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsImSendMail.setMargins(20, 0, 0, 0);
        tvSplitValue.setLayoutParams(paramsImSendMail);

        rlConfirmContainer.addView(tvConfirm);

        RelativeLayout.LayoutParams paramcheckIn = (RelativeLayout.LayoutParams) tvConfirm.getLayoutParams();
        paramcheckIn.width = (int) (getScreenWidth() * 0.9f);
        paramcheckIn.height = (int) (getScreenHeight() * 0.08f);
        paramcheckIn.addRule(RelativeLayout.CENTER_IN_PARENT);
        paramcheckIn.setMargins(20, 20, 20, 0);
        tvConfirm.setLayoutParams(paramcheckIn);

    }

    @Override
    protected void setPropertyOfControls() {


        tvAnnualFee.setTextColor(getColor(R.color.common_black));
        tvAnnualFeeValue.setTextColor(getColor(R.color.common_black));
        tvAnnualFeeValue.setGravity(Gravity.CENTER);
        tvPaymentDate.setTextColor(getColor(R.color.common_black));
        tvPaymentDateValue.setTextColor(getColor(R.color.common_black));
        tvPaymentDateValue.setGravity(Gravity.CENTER);
        tvEndDate.setTextColor(getColor(R.color.common_black));
        tvEndDateValue.setTextColor(getColor(R.color.common_black));
        tvEndDateValue.setGravity(Gravity.END);

        tvSplit.setTextColor(getColor(R.color.common_black));
        tvSplitValue.setTextColor(getColor(R.color.common_black));
        tvSplitValue.setGravity(Gravity.END);
//        tvSplitValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//        tvSplitValue.setBackground(null);

        tvConfirm.setBackground(getResources().getDrawable(R.drawable.member_add_button));
        tvConfirm.setTextColor(getColor(R.color.common_white));
        tvConfirm.setTextSize(Constants.FONT_SIZE_LARGER);
        tvConfirm.setGravity(Gravity.CENTER);
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getResources().getString(R.string.player_info_renewal));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            annualFee = bundle.getString("annualFee");
            endDateValueString = UtilsIsSerialDate.getCutMonthDateOblique(bundle.getString("endDate"), 12);
            memberId = bundle.getInt("member_id");
            paymentId = bundle.getInt("payment_id");
            splitAmount = bundle.getString("split_amount");

        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void netLinkAnnualFeeRenewal() {
        Map<String, String> params = new HashMap<>();

        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.PLAYER_MEMBER_ID, String.valueOf(memberId));
        params.put(ApiKey.PLAYER_MEMBER_PAYMENT_ID, String.valueOf(paymentId));
        params.put(ApiKey.PLAYER_MEMBER_ANNUAL_FEE, annualFee);
        params.put(ApiKey.PLAYER_MEMBER_SPLIT_AMOUNT, tvSplitValue.getValue());
        final String endDate = DateUtils.getAPIYearMonthDayFromCurrentShow(tvEndDateValue.getText().toString(), mContext);
        params.put(ApiKey.PLAYER_MEMBER_SHIP_END_DATE, endDate);
        params.put(ApiKey.PLAYER_MEMBER_PAYMENT_PATTERN, rechargeType);

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(PlayerRenewalFragment.this) {
            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();

                if (returnCode == Constants.RETURN_CODE_20112_RECHARGE_SUCCESSFULLY) {
                    Bundle bundle = new Bundle();
                    bundle.putString(TransKey.SELECTED_DATE, endDate);
                    getBaseActivity().doBackWithReturnValue(bundle, PlayerMemberShipEditFragment.class);
                } else {
                    Utils.showShortToast(mContext, msg);

                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.AnnualFeeRenewal, params);

    }

}
