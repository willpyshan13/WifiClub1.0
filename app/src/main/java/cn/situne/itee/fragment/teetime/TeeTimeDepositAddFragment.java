

/**
 * Project Name: itee
 * File Name:  TeeTimeDepositAddFragment.java
 * Package Name: cn.situne.itee.fragment.teetime
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.teetime;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.baoyz.actionsheet.ActionSheet;

import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonBookingDetailList;
import cn.situne.itee.view.IteeMoneyEditText;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:TeeTimeDepositAddFragment <br/>
 * Function: order deposit  fragment.<br/>
 * UI:  04-2-3
 * Date: 2015-03-11 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class TeeTimeDepositAddFragment extends BaseFragment {

    private RelativeLayout rl_member_message, rl_member_name;
    private RelativeLayout rl_member_money;
    private RelativeLayout rl_member_button;
    private String rechargeType;

    private IteeTextView tvName, tvNo, tvDepositTitle, tvDepositTitleValue, tvPayTitle;
    private IteeMoneyEditText etPayValue;
    private JsonBookingDetailList.DataListItem.BookingListItem booking;

    private Button bt_confirm;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_new_tee_times_deposit;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            booking = (JsonBookingDetailList.DataListItem.BookingListItem) bundle.getSerializable("booking");
        }
        rl_member_name = (RelativeLayout) rootView.findViewById(R.id.rl_member_name);
        rl_member_message = (RelativeLayout) rootView.findViewById(R.id.rl_member_message);
        rl_member_money = (RelativeLayout) rootView.findViewById(R.id.rl_member_money);
        rl_member_button = (RelativeLayout) rootView.findViewById(R.id.rl_member_button);

        tvName = new IteeTextView(getActivity());
        tvNo = new IteeTextView(getActivity());
        tvDepositTitle = new IteeTextView(getActivity());
        tvDepositTitleValue = new IteeTextView(getActivity());
//        tvPayCurrency = new TextView(getActivity());
        tvPayTitle = new IteeTextView(getActivity());
        etPayValue = new IteeMoneyEditText(this);
        bt_confirm = new Button(getActivity());

    }

    @Override
    protected void setDefaultValueOfControls() {
        tvName.setText(booking.getCustomerName());
        tvNo.setText(AppUtils.getShortBookingNo(booking.getBookingNo()));
        tvDepositTitle.setText(getString(R.string.play_purchase_history_deposit));
        tvPayTitle.setText(getString(R.string.pay));
        tvDepositTitleValue.setText(AppUtils.getCurrentCurrency(mContext) + " " + booking.getBookingDeposit());
//        tvPayCurrency.setText(AppUtils.getCurrentCurrency(getActivity()));

    }

    @Override
    protected void setListenersOfControls() {
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String depositValue = etPayValue.getValue();
                if (Utils.isStringNotNullOrEmpty(depositValue) && Integer.valueOf(depositValue) > 0) {

                    getBaseActivity().setTheme(com.baoyz.actionsheet.R.style.ActionSheetStyleIOS7);
                    String[] tTags = new String[]{getString(R.string.tag_pay_cash), getString(R.string.tag_balance_account), getString(R.string.tag_voucher)
                            , getString(R.string.tag_credit_card)};

                    ActionSheet.createBuilder(getActivity(), getFragmentManager())
                            .setCancelButtonTitle(getString(R.string.common_cancel))
                            .setOtherButtonTitles(tTags)
                            .setCancelableOnTouchOutside(true).setListener(actionSheetListenerAddress).show();
                }

            }
        });

    }

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
                    break;
                case 1:
                    rechargeType = "5";
                    break;
                case 2:
                    rechargeType = "2";
                    break;
                case 3:
                    rechargeType = "3";
                    break;

            }
            actionSheet.dismiss();

            netLinkDepositRecharge();


        }
    };

    @Override
    protected void setLayoutOfControls() {

        LinearLayout.LayoutParams paramsRLMemberName = (LinearLayout.LayoutParams) rl_member_name.getLayoutParams();

        paramsRLMemberName.width = LinearLayout.LayoutParams.MATCH_PARENT;
        paramsRLMemberName.height = (int) (getScreenHeight() * 0.06f);
        rl_member_name.setLayoutParams(paramsRLMemberName);
        rl_member_message.setLayoutParams(paramsRLMemberName);

        LinearLayout.LayoutParams paramsRLMemberMoney = (LinearLayout.LayoutParams) rl_member_money.getLayoutParams();

        paramsRLMemberMoney.width = LinearLayout.LayoutParams.MATCH_PARENT;
        paramsRLMemberMoney.height = (int) (getScreenHeight() * 0.1f);
        rl_member_money.setLayoutParams(paramsRLMemberMoney);

        LinearLayout.LayoutParams paramsRLButton = (LinearLayout.LayoutParams) rl_member_button.getLayoutParams();

        paramsRLButton.width = LinearLayout.LayoutParams.MATCH_PARENT;
        paramsRLButton.height = (int) (getScreenHeight() * 0.08f);
        rl_member_button.setLayoutParams(paramsRLButton);


        rl_member_name.addView(tvName);
        RelativeLayout.LayoutParams paramsName = (RelativeLayout.LayoutParams) tvName.getLayoutParams();
        paramsName.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsName.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsName.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsName.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsName.setMargins(20, 0, 0, 0);
        tvName.setLayoutParams(paramsName);


        rl_member_name.addView(tvNo);
        RelativeLayout.LayoutParams paramsNameValue = (RelativeLayout.LayoutParams) tvNo.getLayoutParams();
        paramsNameValue.width = (int) (getScreenWidth() * 0.5f);
        paramsNameValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsNameValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsNameValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsNameValue.setMargins(0, 0, 20, 0);
        tvNo.setLayoutParams(paramsNameValue);

        rl_member_message.addView(tvDepositTitle);
        RelativeLayout.LayoutParams paramsOccupation = (RelativeLayout.LayoutParams) tvDepositTitle.getLayoutParams();
        paramsOccupation.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsOccupation.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsOccupation.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsOccupation.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsOccupation.setMargins(20, 0, 0, 0);
        tvDepositTitle.setLayoutParams(paramsOccupation);


        rl_member_message.addView(tvDepositTitleValue);
        RelativeLayout.LayoutParams paramsOccupationValue = (RelativeLayout.LayoutParams) tvDepositTitleValue.getLayoutParams();
        paramsOccupationValue.width = (int) (getScreenWidth() * 0.5f);
        paramsOccupationValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsOccupationValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsOccupationValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsOccupationValue.setMargins(0, 0, 20, 0);
        tvDepositTitleValue.setLayoutParams(paramsOccupationValue);


        rl_member_money.addView(tvPayTitle);
        RelativeLayout.LayoutParams paramsPrePay = (RelativeLayout.LayoutParams) tvPayTitle.getLayoutParams();
        paramsPrePay.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsPrePay.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsPrePay.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsPrePay.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsPrePay.setMargins(20, 0, 0, 0);
        tvPayTitle.setLayoutParams(paramsPrePay);


        rl_member_money.addView(etPayValue);
        RelativeLayout.LayoutParams paramsPrePayValue = (RelativeLayout.LayoutParams) etPayValue.getLayoutParams();
        paramsPrePayValue.width = (int) (getScreenWidth() * 0.4f);
        paramsPrePayValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsPrePayValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsPrePayValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        etPayValue.setLayoutParams(paramsPrePayValue);
        etPayValue.setId(View.generateViewId());

//        rl_member_money.addView(tvPayCurrency);
//        RelativeLayout.LayoutParams paramsCurrency = (RelativeLayout.LayoutParams) tvPayCurrency.getLayoutParams();
//        paramsCurrency.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramsCurrency.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramsCurrency.addRule(RelativeLayout.LEFT_OF, etPayValue.getId());
//        paramsCurrency.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        tvPayCurrency.setLayoutParams(paramsCurrency);

        rl_member_button.addView(bt_confirm);
        RelativeLayout.LayoutParams paramsMemberButton = (RelativeLayout.LayoutParams) bt_confirm.getLayoutParams();
        paramsMemberButton.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsMemberButton.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsMemberButton.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);

        paramsMemberButton.setMargins(20, 0, 20, 0);
        bt_confirm.setLayoutParams(paramsMemberButton);


    }

    @Override
    protected void setPropertyOfControls() {

        tvNo.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        tvDepositTitleValue.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        etPayValue.setSingleLine();
        etPayValue.setGravity(Gravity.END);
        etPayValue.setBackground(null);
        etPayValue.setValue("0");
        tvName.setTextColor(getColor(R.color.common_blue));
        tvNo.setTextColor(getColor(R.color.common_blue));

        bt_confirm.setText(R.string.common_confirm);
        bt_confirm.setBackgroundResource(R.drawable.member_add_button);
        bt_confirm.setTextColor(getColor(R.color.common_white));
        bt_confirm.setCompoundDrawablePadding(dp2px(25));
        bt_confirm.setTextSize(Constants.FONT_SIZE_LARGER);
        bt_confirm.setId(View.generateViewId());
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getString(R.string.play_purchase_history_deposit));

    }


    /**
     * deposit recharge .
     *
     */
    public void netLinkDepositRecharge() {

        if (Utils.isStringNullOrEmpty(etPayValue.getValue()) ||
                "0".equals(etPayValue.getValue())) {
            Utils.showShortToast(getActivity(), getString(R.string.error_mes00002));
            return;

        }


        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.BOOKING_NO, booking.getBookingNo());
        params.put(ApiKey.PLAYER_RECHARGE_RECHARGE_TYPE, rechargeType);
        params.put(ApiKey.DEPOSIT, etPayValue.getValue());
        HttpManager<JsonBookingDetailList> hh = new HttpManager<JsonBookingDetailList>(TeeTimeDepositAddFragment.this) {

            @Override
            public void onJsonSuccess(JsonBookingDetailList jo) {
                int returnCode = jo.getReturnCode();
                Bundle bundle = new Bundle();
                bundle.putString("fromFlag", "refresh");
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    getBaseActivity().doBackWithReturnValue(bundle, TeeTimeAddFragment.class);

                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.DepositRecharge, params);

    }


}
