/**
 * Project Name: itee
 * File Name:	 ShoppingConfirmPayFragment.java
 * Package Name: cn.situne.itee.fragment.shopping
 * Date:		 2015-03-05
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.shopping;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.baoyz.actionsheet.ActionSheet;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonBalanceList;
import cn.situne.itee.manager.jsonentity.JsonPayPostRet;
import cn.situne.itee.view.CheckSwitchButton;
import cn.situne.itee.view.IteeButton;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeMoneyEditText;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.SelectDatePopupWindow;
import cn.situne.itee.view.popwindow.ShoppingPaymentFailedPopupWindow;


/**
 * ClassName:ShoppingConfirmPayFragment <br/>
 * Function: 商品 支付<br/>
 * UI:  06-2-1 06-2-2
 * Date: 2015-03-05 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */

public class ShoppingConfirmPayFragment {//extends BaseFragment

//    public String amount;
//    public String account;
////    private RelativeLayout rlPaymentPatternContainer;
//    private RelativeLayout rlApplyDiscountContainer;
//    private RelativeLayout rlDiscountReasonContainer;
//    private RelativeLayout rlDiscountPriceContainer;
//    private RelativeLayout rlAccountContainer;
//    private RelativeLayout rlDiscountPasswordContainer;
//    private RelativeLayout rlSubTotalContainer;
//    private RelativeLayout rlTaxContainer;
//    private RelativeLayout rlDiscountContainer;
//    private RelativeLayout rlTotalContainer;
//    private RelativeLayout rlDepositContainer;
//
//    private RelativeLayout rlDepositUserMoney;
//    private RelativeLayout rlConfirmContainer;
//    private LinearLayout llBalanceAccount;
//    private IteeTextView tvPaymentPattern;
//    private IteeTextView tvPaymentPatternValue;
//    private IteeTextView tvApplyDiscount;
//    private IteeEditText etDiscountReason;
//    private CheckSwitchButton csbDiscountType;
//    private IteeTextView tvPriceCurrency;
//    private IteeMoneyEditText etPriceValue;
//    private AppUtils.EditViewMoneyWatcher editViewMoneyWatcher;
//    private IteeTextView tvManager;
//    private IteeEditText tvManagerValue;
//    private IteeTextView tvPassword;
//    private IteeEditText etPasswordValue;
//    private IteeTextView tvSubTotal;
//    private IteeTextView tvSubTotalCurrency;
//    private IteeTextView tvSubTotalValue;
//    private IteeTextView tvTax;
//    private IteeTextView tvTaxCurrency;
//    private IteeTextView tvTaxValue;
//    private IteeTextView tvDiscount;
//    private IteeTextView tvDiscountCurrency;
//    private IteeTextView tvDiscountValue;
//    private IteeTextView tvDepositValue;
//
//    private IteeTextView tvUserMoney;
//    private IteeTextView tvTotal;
//    private IteeTextView tvTotalCurrency;
//    private IteeTextView tvTotalValue;
//    private IteeTextView tvConfirm;
//    private IteeEditText etPassCodeValue;
//    private ShoppingPaymentFailedPopupWindow shoppingPaymentFailedPopupWindow;
//    //balance account
//    private IteeEditText tvAccountValue;
//    private IteeTextView tvBalanceValue;
//    private String rechargeType;
//    private Integer memberId;
//    private Integer paymentId;
//    private int padding;
//    private String payId, bookingNo, subtotal, tax, total;
//    private String playerName;
//    private int purchaseFlag;
//
//    private String fromPagePayment;
//
//    private String customId;
//
//    private String deposit;
//
//    private int bookingFlag;
//    private IteeButton btnPassCode;
//
//    private int timeMax = Constants.SECOND_60;
//
//    private boolean isTiming;
//    private Handler btnPassCodeHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            int time = msg.what;
//            btnPassCode.setText(String.valueOf(time));
//            if (time >= 0) {
//                startTime();
//            } else {
//                isTiming = false;
//                timeMax = Constants.SECOND_60;
//                btnPassCode.setText(getString(R.string.shopping_send));
//                btnPassCode.setEnabled(true);
//            }
//
//        }
//    };
//    final ActionSheet.ActionSheetListener actionSheetListenerAddress = new ActionSheet.ActionSheetListener() {
//        @Override
//        public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
//        }
//
//        @Override
//        public void onDismissWithCancelButton(ActionSheet actionSheet) {
//        }
//
//        @Override
//        public void onOtherButtonClick(ActionSheet actionSheet, int index) {
//
//            switch (index) {
//                case 0:
//                    rechargeType = Constants.PAYMENT_PATTERN_CASH;
//                    tvPaymentPatternValue.setText(getString(R.string.tag_pay_cash));
//                    llBalanceAccount.setVisibility(View.GONE);
//                    break;
//                case 1:
//                    rechargeType = Constants.PAYMENT_PATTERN_BALANCE_ACCOUNT;
//                    tvPaymentPatternValue.setText(getString(R.string.tag_balance_account));
//                    addBalanceAccount();
//                    netLinkBalance();
//
//                    break;
//                case 2:
//                    rechargeType = Constants.PAYMENT_PATTERN_VOUCHERS;
//                    tvPaymentPatternValue.setText(getString(R.string.tag_voucher));
//                    llBalanceAccount.setVisibility(View.GONE);
//                    break;
//                case 3:
//                    rechargeType = Constants.PAYMENT_PATTERN_CREDIT_CARD;
//                    tvPaymentPatternValue.setText(getString(R.string.tag_credit_card));
//                    llBalanceAccount.setVisibility(View.GONE);
//                    break;
//                case 4:
//                    rechargeType = Constants.PAYMENT_PATTERN_THIRD_PARTY;
//                    tvPaymentPatternValue.setText(getString(R.string.tag_third_party));
//                    llBalanceAccount.setVisibility(View.GONE);
//                    break;
//
//            }
//            actionSheet.dismiss();
//        }
//    };
//
//    @Override
//    protected int getFragmentId() {
//        return R.layout.fragment_shopping_confirm_pay;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        isTiming = false;
//        timeMax = 99999;
//        if (btnPassCode != null) {
//            btnPassCode.setText(getString(R.string.shopping_send));
//        }
//
//    }
//
//    @Override
//    public int getTitleResourceId() {
//        return R.string.player_info_renewal;
//    }
//
//    @Override
//    protected void initControls(View rootView) {
//        padding = getActualHeightOnThisDevice(40);
//
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            bookingNo = bundle.getString(TransKey.SHOPPING_BOOKING_NO);
//            playerName = bundle.getString(TransKey.SHOPPING_PLAYER_NAME);
//            payId = bundle.getString(TransKey.SHOPPING_ID);
//            subtotal = bundle.getString(TransKey.SHOPPING_SUBTOTAL, Constants.STR_0);
//            tax = bundle.getString(TransKey.SHOPPING_TAX, Constants.STR_0);
//            total = bundle.getString(TransKey.SHOPPING_TOTAL, Constants.STR_0);
//            purchaseFlag = bundle.getInt(TransKey.SHOPPING_PURCHASE_FLAG);
//            deposit = bundle.getString(TransKey.SHOPPING_DEPOSIT);
//            fromPagePayment = bundle.getString(TransKey.COMMON_FROM_PAGE);
//            bookingFlag = bundle.getInt(TransKey.SHOPPING_BOOKING_FLAG);
//            amount = bundle.getString(TransKey.SHOPPING_AMOUNT);
//            account = bundle.getString(TransKey.SHOPPING_ACCOUNT);
//            customId = bundle.getString(TransKey.SHOPPING_CUSTOMER);
//        }
//
//        rlPaymentPatternContainer = (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_payment_pattern);
//        rlPaymentPatternContainer.setBackgroundColor(getColor(R.color.common_white));
//
//        llBalanceAccount = (LinearLayout) rootView.findViewById(R.id.ll_confirm_pay_balance_account);
//
//        llBalanceAccount.setBackgroundColor(getColor(R.color.common_white));
//
//        rlApplyDiscountContainer = (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_applay_discount);
//        rlApplyDiscountContainer.setBackgroundColor(getColor(R.color.common_white));
//
//        rlDiscountReasonContainer = (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_discount_reason);
//        rlDiscountPriceContainer = (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_discount_price);
//        rlDiscountReasonContainer.setBackgroundColor(getColor(R.color.common_white));
//        rlDiscountPriceContainer.setBackgroundColor(getColor(R.color.common_white));
//        rlAccountContainer = (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_discount_manager_account);
//        rlAccountContainer.setBackgroundColor(getColor(R.color.common_white));
//        rlDiscountPasswordContainer = (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_discount_manager_password);
//        rlDiscountPasswordContainer.setBackgroundColor(getColor(R.color.common_white));
//
//        rlSubTotalContainer = (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_sub_total);
//
//        rlTaxContainer = (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_tax);
//        rlDiscountContainer = (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_discount);
//
//        rlTotalContainer = (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_discount_total);
//        rlDepositContainer = (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_deposit_container);
//
//        rlDepositUserMoney= (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_user_money);
//        rlDepositUserMoney.setVisibility(View.GONE);
//        rlConfirmContainer = (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_confirm);
//
//        rlSubTotalContainer.setBackgroundColor(getColor(R.color.common_white));
//        rlTaxContainer.setBackgroundColor(getColor(R.color.common_white));
//        rlDiscountContainer.setBackgroundColor(getColor(R.color.common_white));
//        rlDepositContainer.setBackgroundColor(getColor(R.color.common_white));
//        rlDepositUserMoney.setBackgroundColor(getColor(R.color.common_white));
//        rlTotalContainer.setBackgroundColor(getColor(R.color.common_white));
//
//        tvPaymentPattern = new IteeTextView(this);
//        tvPaymentPatternValue = new IteeTextView(this);
//
//        tvApplyDiscount = new IteeTextView(this);
//        etDiscountReason = new IteeEditText(getActivity());
//        etDiscountReason.setPadding(getActualWidthOnThisDevice(4), 0, 0, 0);
//
//        csbDiscountType = new CheckSwitchButton(this, CheckSwitchButton.TYPE_DISCOUNT_OR_CURRENCY);
//        tvPriceCurrency = new IteeTextView(this);
//        etPriceValue = new IteeMoneyEditText(ShoppingConfirmPayFragment.this);
//
//
//        editViewMoneyWatcher = new AppUtils.EditViewMoneyWatcher(etPriceValue);
//
//        etPriceValue.addTextChangedListener(editViewMoneyWatcher);
//        tvManager = new IteeTextView(this);
//        tvManagerValue = new IteeEditText(getActivity());
//
//        tvPassword = new IteeTextView(this);
//        etPasswordValue = new IteeEditText(getActivity());
//
//        tvSubTotal = new IteeTextView(this);
//        tvSubTotalCurrency = new IteeTextView(this);
//        tvSubTotalValue = new IteeTextView(this);
//
//        tvTax = new IteeTextView(this);
//        tvTaxCurrency = new IteeTextView(this);
//        tvTaxValue = new IteeTextView(this);
//
//        tvDiscount = new IteeTextView(this);
//        tvDiscountCurrency = new IteeTextView(this);
//        tvDiscountValue = new IteeTextView(this);
//
//        tvDepositValue = new IteeTextView(this);
//        tvDepositValue.setTextSize(Constants.FONT_SIZE_SMALLER);
//
//        tvUserMoney = new IteeTextView(this);
//        tvUserMoney.setTextSize(Constants.FONT_SIZE_SMALLER);
//
//
//        tvTotal = new IteeTextView(this);
//        tvTotalCurrency = new IteeTextView(this);
//        tvTotalValue = new IteeTextView(this);
//        tvConfirm = new IteeTextView(this);
//
//    }
//
//    @Override
//    protected void setDefaultValueOfControls() {
//
//        tvSubTotalValue.setText(Utils.get2DigitDecimalString(subtotal));
//        tvTaxValue.setText(Utils.get2DigitDecimalString(tax));
//
//        double temp = Utils.getDoubleFromString(total);
//        if (AppUtils.isTaxExcludeGoods(getActivity()) && !AppUtils.getShowSalesTax(getActivity())) {
//            temp += Utils.getDoubleFromString(tax);
//        }
//
//        total = String.valueOf(temp);
//        tvTotalValue.setText(Utils.get2DigitDecimalString(total));
//        tvDiscountValue.setText(Constants.STR_0);
//
//        String messageFormat;
//        String totalDeposit = AppUtils.getCurrentCurrency(getActivity()) + Utils.get2DigitDecimalString(deposit);
//        String leftDeposit;
//        Double leftDepositValue = 0.0;
//
//        Double userDeposit = 0.0;
//        Double userMoney = Double.valueOf(total) - Double.valueOf(deposit);
//        if (userMoney<0){
//            tvUserMoney.setText("dfdf ＝ "+"0");
//        }else{
//
//            tvUserMoney.setText("dfdf ＝ "+userMoney);
//        }
//
//        if (Double.valueOf(total)>=Double.valueOf(deposit)){
//            userDeposit = Double.valueOf(deposit);
//
//        }else{
//
//            userDeposit = Double.valueOf(total);
//        }
//
//        if (Double.valueOf(deposit) >= Double.valueOf(total)) {
//            leftDepositValue = Double.valueOf(deposit) - Double.valueOf(total);
//            leftDeposit = AppUtils.getCurrentCurrency(getActivity()) + Utils.get2DigitDecimalString(String.valueOf(leftDepositValue));
//        } else {
//            leftDeposit = AppUtils.getCurrentCurrency(getActivity()) + Constants.STR_0;
//        }
//
//        if (purchaseFlag == ShoppingPaymentFragment.PURCHASE_FLAG_CHECKOUT) {
//            messageFormat = getString(R.string.msg_confirm_pay_deposit_check_out);
//            if (leftDepositValue <= 0) {
//                messageFormat = getString(R.string.msg_confirm_pay_deposit_0_check_out);
//            }
//
//        } else {
//            messageFormat = getString(R.string.msg_confirm_pay_deposit_pay);
//            if (leftDepositValue <= 0) {
//                messageFormat = getString(R.string.msg_confirm_pay_deposit_0_pay);
//            }
//        }
//
//        String message = MessageFormat.format(messageFormat, String.valueOf(userDeposit), leftDeposit);
//        if (leftDepositValue <= 0) {
//            message = MessageFormat.format(messageFormat, String.valueOf(userDeposit));
//        }
//        tvDepositValue.setText(message);
//
//
//    }
//
//    @Override
//    protected void setListenersOfControls() {
//        tvConfirm.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
//            @Override
//            public void noDoubleClick(View v) {
//                netLinkPayConfirm();
//            }
//        });
//
//        csbDiscountType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                csbDiscountType.setFocusable(true);
//                csbDiscountType.setFocusableInTouchMode(true);
//                csbDiscountType.requestFocus();
//                String currentCurrency = AppUtils.getCurrentCurrency(getBaseActivity());
//                Utils.hideKeyboard(getBaseActivity());
//
//
//                BigDecimal totalTemp = new BigDecimal(total);
//                if (Utils.isStringNullOrEmpty(etPriceValue.getValue())) {
//                    return;
//                }
//
//                String Str = etPriceValue.getText().toString();
//                double temp;
//                if (isChecked) {
//                    if (Str.contains(currentCurrency)) {
//                        etPriceValue.setText(Str.replace(currentCurrency, Constants.STR_EMPTY).trim());
//                    }
//
//                    if (Double.parseDouble(etPriceValue.getValue()) > 100) {
//                        etPriceValue.setText("100");
//                    }
//                    double rat = Utils.getDoubleFromString(etPriceValue.getValue());
//                    tvDiscountValue.setText(Utils.get2DigitDecimalString("-" + Utils.getDoubleFromString(total) * rat / 100.0));
//
//                    temp = Utils.getDoubleFromString(total) - (Utils.getDoubleFromString(total) * rat / 100.0);
//
//                    double taxValue = Double.parseDouble(AppUtils.getSalesTax(getActivity())) / 100 * temp;
//                    if (taxValue < 0) {
//                        taxValue = 0;
//                    }
//                    tax = Utils.get2DigitDecimalString(String.valueOf(taxValue));
//                    tvTaxValue.setText(tax);
//
//                    if (temp < 0) {
//                        temp = 0;
//                    }
//
//                    if (AppUtils.isTaxExcludeGoods(getActivity())) {
//                        temp += Utils.getDoubleFromString(tax);
//                    }
//
//                    tvTotalValue.setText(Utils.get2DigitDecimalString(String.valueOf(temp)));
//
//                    editViewMoneyWatcher.setMaxValue(100.0);
//                } else {
//                    if (!Str.contains(currentCurrency)) {
//                        etPriceValue.setValue(Str);
//                    }
//
//                    if (Utils.isStringNotNullOrEmpty(etPriceValue.getValue())) {
//                        if (Double.valueOf(etPriceValue.getValue()) > Double.valueOf(subtotal)) {
//                            tvDiscountValue.setText("-" + Utils.get2DigitDecimalString(subtotal));
//                        } else {
//                            tvDiscountValue.setText("-" + Utils.get2DigitDecimalString(etPriceValue.getValue()));
//                        }
//                    }
//                    String totalString = totalTemp.subtract(new BigDecimal(etPriceValue.getValue())).toString();
//
//                    if (Utils.isStringNotNullOrEmpty(etPriceValue.getValue())) {
//                        double taxValue = Double.parseDouble(AppUtils.getSalesTax(getActivity())) / 100
//                                * (Double.valueOf(subtotal) - Double.valueOf(etPriceValue.getValue()));
//                        if (taxValue < 0) {
//                            taxValue = 0;
//                        }
//                        tax = Utils.get2DigitDecimalString(String.valueOf(taxValue));
//                        tvTaxValue.setText(tax);
//                    }
//
//                    temp = Utils.getDoubleFromString(totalString);
//
//                    if (temp < 0) {
//                        temp = 0;
//                    }
//
//                    if (AppUtils.isTaxExcludeGoods(getActivity())) {
//                        temp += Utils.getDoubleFromString(tax);
//                    }
//                    tvTotalValue.setText(Utils.get2DigitDecimalString(String.valueOf(temp)));
//                    editViewMoneyWatcher.setMaxValue(null);
//                }
//
//            }
//        });
//        etPriceValue.addTextChangedListener(new TextWatcher() {
//            private CharSequence temp;
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                temp = s;
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//
//                if (!temp.toString().contains(AppUtils.getCurrentCurrency(getBaseActivity()))) {
//                    if (Constants.STR_EMPTY.equals(temp.toString())) {
//                        temp = Constants.STR_0;
//                    }
//
//                    BigDecimal totalTemp = new BigDecimal(total);
//                    BigDecimal totalString = new BigDecimal(0.0);
//
//                    String discount = Constants.STR_EMPTY;
//
//                    if (csbDiscountType.isChecked()) {
//                        if (Utils.isStringNotNullOrEmpty(etPriceValue.getValue())) {
//                            BigDecimal cut = new BigDecimal(etPriceValue.getValue()).divide(new BigDecimal(100))
//                                    .multiply(totalTemp);
//                            discount = cut.toString();
//                            tvDiscountValue.setText(Constants.STR_MINUS + Utils.get2DigitDecimalString(discount));
//                            totalString = totalTemp.subtract(cut);
//                        }
//
//                    } else {
//                        discount = temp.toString();
//                        if (Double.valueOf(discount) > Double.valueOf(subtotal)) {
//                            tvDiscountValue.setText(Constants.STR_MINUS + Utils.get2DigitDecimalString(subtotal));
//                        } else {
//                            tvDiscountValue.setText(Constants.STR_MINUS + Utils.get2DigitDecimalString(etPriceValue.getValue()));
//                        }
//                        totalString = totalTemp.subtract(new BigDecimal(temp.toString()));
//                    }
//
//                    if (Utils.isStringNotNullOrEmpty(discount)) {
//                        double taxValue = Double.parseDouble(AppUtils.getSalesTax(getActivity())) / 100
//                                * (Double.valueOf(subtotal) - Double.valueOf(discount));
//                        if (taxValue < 0) {
//                            taxValue = 0;
//                        }
//                        tax = Utils.get2DigitDecimalString(String.valueOf(taxValue));
//                        tvTaxValue.setText(tax);
//                    }
//
//                    double temp = Utils.getDoubleFromString(totalString.toString());
//
//                    if (temp < 0) {
//                        temp = 0;
//                    }
//
//                    if (AppUtils.isTaxExcludeGoods(getActivity())) {
//                        temp += Utils.getDoubleFromString(tax);
//                    }
//
//                    tvTotalValue.setText(Utils.get2DigitDecimalString(String.valueOf(temp)));
//                }
//            }
//
//        });
//
//        rlPaymentPatternContainer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                getBaseActivity().setTheme(com.baoyz.actionsheet.R.style.ActionSheetStyleIOS7);
//                String[] tTags = new String[]{getString(R.string.tag_pay_cash), getString(R.string.tag_balance_account), getString(R.string.tag_voucher)
//                        , getString(R.string.tag_credit_card), getString(R.string.tag_third_party)};
//
//                ActionSheet.createBuilder(getActivity(), getFragmentManager())
//                        .setCancelButtonTitle(getString(R.string.common_cancel))
//                        .setOtherButtonTitles(tTags)
//                        .setCancelButtonHidden(true)
//                        .setCancelableOnTouchOutside(true).setListener(actionSheetListenerAddress).show();
//            }
//        });
//    }
//
//    private void startTime() {
//        if (timeMax <= 60) {
//            Thread t = new Thread() {
//
//                @Override
//                public void run() {
//                    try {
//                        timeMax--;
//                        sleep(1000);
//                        Message mes = new Message();
//                        mes.what = timeMax;
//                        btnPassCodeHandler.sendMessage(mes);
//                    } catch (InterruptedException e) {
//                        Utils.log(e.getMessage());
//                    }
//                }
//            };
//            t.start();
//        }
//    }
//
//    private void addBalanceAccount() {
//        llBalanceAccount.setVisibility(View.VISIBLE);
//        llBalanceAccount.removeAllViews();
//        LinearLayout.LayoutParams paramsRlPhotoLayout = (LinearLayout.LayoutParams) llBalanceAccount.getLayoutParams();
//        paramsRlPhotoLayout.height = LinearLayout.LayoutParams.WRAP_CONTENT;
//        llBalanceAccount.setLayoutParams(paramsRlPhotoLayout);
//        RelativeLayout rlAccount = new RelativeLayout(getActivity());
//        RelativeLayout rlBalance = new RelativeLayout(getActivity());
//        RelativeLayout rlPassCode = new RelativeLayout(getActivity());
//
//        View viewLineTop = new View(getActivity());
//        viewLineTop.setBackgroundColor(getColor(R.color.common_gray));
//        View viewLineTop1 = new View(getActivity());
//        viewLineTop1.setBackgroundColor(getColor(R.color.common_gray));
//
//        View viewLine = new View(getActivity());
//        viewLine.setBackgroundColor(getColor(R.color.common_gray));
//
//        View viewLine1 = new View(getActivity());
//        viewLine1.setBackgroundColor(getColor(R.color.common_gray));
//
//
//        LinearLayout.LayoutParams viewTopParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(20));
//
//        View viewTop = new View(getActivity());
//        viewTop.setBackgroundColor(getColor(R.color.common_light_gray));
//
//        viewTop.setLayoutParams(viewTopParams);
//
//        llBalanceAccount.addView(AppUtils.getSeparatorLine(ShoppingConfirmPayFragment.this));
//        llBalanceAccount.addView(viewTop);
//        llBalanceAccount.addView(viewLineTop);
//        llBalanceAccount.addView(rlAccount);
//        llBalanceAccount.addView(viewLine);
//        llBalanceAccount.addView(rlBalance);
//        llBalanceAccount.addView(viewLine1);
//        llBalanceAccount.addView(rlPassCode);
//
//        LinearLayout.LayoutParams paramsLineTop = (LinearLayout.LayoutParams) viewLineTop.getLayoutParams();
//        paramsLineTop.height = 2;
//        paramsLineTop.width = LinearLayout.LayoutParams.MATCH_PARENT;
//        viewLineTop.setLayoutParams(paramsLineTop);
//
//        LinearLayout.LayoutParams paramsLine = (LinearLayout.LayoutParams) viewLine.getLayoutParams();
//        paramsLine.height = 2;
//        paramsLine.width = LinearLayout.LayoutParams.MATCH_PARENT;
//        viewLine.setLayoutParams(paramsLine);
//
//        LinearLayout.LayoutParams paramsLine1 = (LinearLayout.LayoutParams) viewLine1.getLayoutParams();
//        paramsLine1.height = 2;
//        paramsLine1.width = LinearLayout.LayoutParams.MATCH_PARENT;
//        viewLine1.setLayoutParams(paramsLine1);
//
//        LinearLayout.LayoutParams paramsLayout = (LinearLayout.LayoutParams) rlAccount.getLayoutParams();
//        paramsLayout.height = getActualHeightOnThisDevice(100);
//        rlAccount.setLayoutParams(paramsLayout);
//        rlBalance.setLayoutParams(paramsLayout);
//        rlPassCode.setLayoutParams(paramsLayout);
//        rlAccount.setPadding(padding, 0, padding, 0);
//        rlBalance.setPadding(padding, 0, padding, 0);
//        rlPassCode.setPadding(padding, 0, padding, 0);
//
//        //account
//        IteeTextView account = new IteeTextView(this);
//        tvAccountValue = new IteeEditText(getActivity());
//        tvAccountValue.setHint(getString(R.string.shopping_pay_account));
//        tvAccountValue.setTextColor(getColor(R.color.common_gray));
//        tvAccountValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
//        tvAccountValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (!b) {
//
//                    Map<String, String> params = new HashMap<>();
//                    params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
//                    params.put(ApiKey.SHOPPING_ACCOUNT, tvAccountValue.getText().toString());
//                    HttpManager<JsonBalanceList> hh = new HttpManager<JsonBalanceList>(ShoppingConfirmPayFragment.this) {
//                        @Override
//                        public void onJsonSuccess(JsonBalanceList jo) {
//                            int returnCode = jo.getReturnCode();
//                            if (returnCode == Constants.RETURN_CODE_20301) {
//                                //  tvAccountValue.setText(jo.getMemberList().getAccount());
//                                tvBalanceValue.setText(String.valueOf(jo.getDataList().getBalance()));
//                                customId = jo.getDataList().getCustomId();
//                                Utils.hideKeyboard(getBaseActivity());
//                            }
//                        }
//
//                        @Override
//                        public void onJsonError(VolleyError error) {
//
//                        }
//                    };
//                    hh.startGet(getActivity(), ApiManager.HttpApi.Balance, params);
//                }
//            }
//        });
//
//        tvAccountValue.setOnKeyListener(new OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//
//                if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
//                    if (i == 66) {
//                        Map<String, String> params = new HashMap<>();
//                        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
//                        params.put(ApiKey.SHOPPING_ACCOUNT, tvAccountValue.getText().toString());
//                        HttpManager<JsonBalanceList> hh = new HttpManager<JsonBalanceList>(ShoppingConfirmPayFragment.this) {
//                            @Override
//                            public void onJsonSuccess(JsonBalanceList jo) {
//                                int returnCode = jo.getReturnCode();
//                                if (returnCode == Constants.RETURN_CODE_20301) {
//                                    //  tvAccountValue.setText(jo.getMemberList().getAccount());
//                                    tvBalanceValue.setText(String.valueOf(jo.getDataList().getBalance()));
//                                    customId = jo.getDataList().getCustomId();
//                                    Utils.hideKeyboard(getBaseActivity());
//                                }
//                            }
//
//                            @Override
//                            public void onJsonError(VolleyError error) {
//
//                            }
//                        };
//                        hh.startGet(getActivity(), ApiManager.HttpApi.Balance, params);
//
//                    }
//
//                }
//
//                return false;
//            }
//        });
//
//        account.setText(getString(R.string.shopping_pay_account));
//        account.setTextColor(getColor(R.color.common_black));
//
//        rlAccount.addView(account);
//        RelativeLayout.LayoutParams paramsAccount = (RelativeLayout.LayoutParams) account.getLayoutParams();
//        paramsAccount.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramsAccount.height = getActualHeightOnThisDevice(100);
//        paramsAccount.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        paramsAccount.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
//        account.setLayoutParams(paramsAccount);
//
//        rlAccount.addView(tvAccountValue);
//        RelativeLayout.LayoutParams paramsAccountValue = (RelativeLayout.LayoutParams) tvAccountValue.getLayoutParams();
//        paramsAccountValue.width = getActualWidthOnThisDevice(400);
//        paramsAccountValue.height = getActualHeightOnThisDevice(100);
//        paramsAccountValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        paramsAccountValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
//        tvAccountValue.setLayoutParams(paramsAccountValue);
//
//        //balance
//
//        IteeTextView balance = new IteeTextView(this);
//        IteeTextView balanceCurrency = new IteeTextView(this);
//        tvBalanceValue = new IteeTextView(this);
//        balance.setText(getString(R.string.shopping_pay_balance));
//        balance.setTextColor(getColor(R.color.common_black));
////        balanceCurrency.setText(AppUtils.getCurrentCurrency(getActivity()));
//
//        rlBalance.addView(balance);
//        RelativeLayout.LayoutParams paramsBalance = (RelativeLayout.LayoutParams) balance.getLayoutParams();
//        paramsBalance.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramsBalance.height = getActualHeightOnThisDevice(100);
//        paramsBalance.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        paramsBalance.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
//        balance.setLayoutParams(paramsBalance);
//
//        rlBalance.addView(tvBalanceValue);
//        RelativeLayout.LayoutParams paramsBalanceValue = (RelativeLayout.LayoutParams) tvBalanceValue.getLayoutParams();
//        paramsBalanceValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramsBalanceValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramsBalanceValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        paramsBalanceValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
//        tvBalanceValue.setLayoutParams(paramsBalanceValue);
//        tvBalanceValue.setId(View.generateViewId());
//
//
//        rlBalance.addView(balanceCurrency);
//        RelativeLayout.LayoutParams paramsBalanceCurrency = (RelativeLayout.LayoutParams) balanceCurrency.getLayoutParams();
//        paramsBalanceCurrency.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramsBalanceCurrency.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramsBalanceCurrency.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        paramsBalanceCurrency.addRule(RelativeLayout.LEFT_OF, tvBalanceValue.getId());
//        balanceCurrency.setLayoutParams(paramsBalanceCurrency);
//
//        //passcode
//
//        IteeTextView tvPassCode = new IteeTextView(this);
//        etPassCodeValue = new IteeEditText(getActivity());
//        etPassCodeValue.setHint(Constants.STR_SPACE + getString(R.string.shopping_pay_passcode));
//        btnPassCode = new IteeButton(getActivity());
//        etPassCodeValue.setPadding(0, 0, 0, 0);
//        etPassCodeValue.setGravity(Gravity.CENTER_VERTICAL);
//
//        tvPassCode.setTextColor(getColor(R.color.common_black));
//        tvPassCode.setText(getString(R.string.shopping_pay_passcode));
//        etPassCodeValue.setBackgroundResource(R.drawable.textview_corner);
//        etPassCodeValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        btnPassCode.setBackgroundResource(R.drawable.bg_green_btn);
//        btnPassCode.setText(getString(R.string.shopping_send));
//
//        btnPassCode.setTextColor(getColor(R.color.common_white));
//
//        rlPassCode.addView(tvPassCode);
//        RelativeLayout.LayoutParams paramsPasscode = (RelativeLayout.LayoutParams) tvPassCode.getLayoutParams();
//        paramsPasscode.width = (int) (getScreenWidth() * 0.3f);
//        paramsPasscode.height = RelativeLayout.LayoutParams.MATCH_PARENT;
//        paramsPasscode.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        paramsPasscode.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
//        tvPassCode.setLayoutParams(paramsPasscode);
//
//
//        rlPassCode.addView(btnPassCode);
//        RelativeLayout.LayoutParams paramsBtnPasscode = (RelativeLayout.LayoutParams) btnPassCode.getLayoutParams();
//        paramsBtnPasscode.width = getActualWidthOnThisDevice(150);
//        paramsBtnPasscode.height = getActualHeightOnThisDevice(80);
//        paramsBtnPasscode.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        paramsBtnPasscode.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
//        btnPassCode.setLayoutParams(paramsBtnPasscode);
//        btnPassCode.setId(View.generateViewId());
//
//        btnPassCode.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
//            @Override
//            public void noDoubleClick(View v) {
//                if (!isTiming) {
//                    btnPassCode.setEnabled(false);
//                    sendTempPwdGet();
//                }
//            }
//        });
//
//        rlPassCode.addView(etPassCodeValue);
//        RelativeLayout.LayoutParams paramsPasscodeValue = (RelativeLayout.LayoutParams) etPassCodeValue.getLayoutParams();
//        paramsPasscodeValue.width = (int) (getScreenWidth() * 0.3f);
//        paramsPasscodeValue.height = getActualHeightOnThisDevice(80);
//        paramsPasscodeValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        paramsPasscodeValue.addRule(RelativeLayout.LEFT_OF, btnPassCode.getId());
//        etPassCodeValue.setLayoutParams(paramsPasscodeValue);
//
//
//    }
//
//    @Override
//    protected void setLayoutOfControls() {
//
//        LinearLayout.LayoutParams paramsRlPhotoLayout = (LinearLayout.LayoutParams) rlPaymentPatternContainer.getLayoutParams();
//        paramsRlPhotoLayout.height = getActualHeightOnThisDevice(100);
//
//        LinearLayout.LayoutParams paramsDetail = (LinearLayout.LayoutParams) rlSubTotalContainer.getLayoutParams();
//        paramsDetail.height = getActualHeightOnThisDevice(50);
//
//        LinearLayout.LayoutParams paramsConfirmContainer = (LinearLayout.LayoutParams) rlConfirmContainer.getLayoutParams();
//        paramsConfirmContainer.height = getActualHeightOnThisDevice(240);
//
//        LinearLayout.LayoutParams paramsTotalContainer = new LinearLayout.LayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(60));
//
//        rlPaymentPatternContainer.setLayoutParams(paramsRlPhotoLayout);
//        rlApplyDiscountContainer.setLayoutParams(paramsRlPhotoLayout);
//        rlDiscountReasonContainer.setLayoutParams(paramsRlPhotoLayout);
//        rlDiscountPriceContainer.setLayoutParams(paramsRlPhotoLayout);
//        rlAccountContainer.setLayoutParams(paramsRlPhotoLayout);
//        rlDiscountPasswordContainer.setLayoutParams(paramsRlPhotoLayout);
//
//        rlSubTotalContainer.setLayoutParams(paramsDetail);
//        rlTaxContainer.setLayoutParams(paramsDetail);
//       // rlDiscountContainer.setLayoutParams(paramsDetail);
//        rlTotalContainer.setLayoutParams(paramsTotalContainer);
//        rlConfirmContainer.setLayoutParams(paramsConfirmContainer);
//
//
//        rlPaymentPatternContainer.setPadding(padding, 0, padding, 0);
//        rlApplyDiscountContainer.setPadding(padding, 0, padding, 0);
//        rlDiscountReasonContainer.setPadding(padding, 0, padding, 0);
//        rlDiscountPriceContainer.setPadding(padding, 0, padding, 0);
//        rlAccountContainer.setPadding(padding, 0, padding, 0);
//        rlDiscountPasswordContainer.setPadding(padding, 0, padding, 0);
//
//        rlSubTotalContainer.setPadding(padding, 0, padding, 0);
//        rlTaxContainer.setPadding(padding, 0, padding, 0);
//        rlDiscountContainer.setPadding(padding, 0, padding, 10);
//        rlDepositContainer.setPadding(padding, 0, padding, getActualHeightOnThisDevice(10));
//
//        rlDepositUserMoney.setPadding(padding, 0, padding, getActualHeightOnThisDevice(10));
//        rlTotalContainer.setPadding(padding, 0, padding, 0);
//        rlConfirmContainer.setPadding(padding, 0, padding, 0);
//
//        rlPaymentPatternContainer.addView(tvPaymentPattern);
//        RelativeLayout.LayoutParams paramsBirth = (RelativeLayout.LayoutParams) tvPaymentPattern.getLayoutParams();
//        paramsBirth.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramsBirth.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramsBirth.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        tvPaymentPattern.setLayoutParams(paramsBirth);
//
//
//        rlPaymentPatternContainer.addView(tvPaymentPatternValue);
//        RelativeLayout.LayoutParams paramsBirthValue = (RelativeLayout.LayoutParams) tvPaymentPatternValue.getLayoutParams();
//        paramsBirthValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramsBirthValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramsBirthValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
//        paramsBirthValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        tvPaymentPatternValue.setLayoutParams(paramsBirthValue);
//
//
//        rlApplyDiscountContainer.addView(tvApplyDiscount);
//        RelativeLayout.LayoutParams paramsGenderInfo = (RelativeLayout.LayoutParams) tvApplyDiscount.getLayoutParams();
//        paramsGenderInfo.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramsGenderInfo.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramsGenderInfo.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        tvApplyDiscount.setLayoutParams(paramsGenderInfo);
//
//
//        rlDiscountReasonContainer.addView(etDiscountReason);
//        RelativeLayout.LayoutParams paramsGenderInfoValue = (RelativeLayout.LayoutParams) etDiscountReason.getLayoutParams();
//        paramsGenderInfoValue.width = (int) (getScreenWidth() * 0.9f);
//        paramsGenderInfoValue.height = getActualHeightOnThisDevice(80);
//        paramsGenderInfoValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        etDiscountReason.setLayoutParams(paramsGenderInfoValue);
//
//        rlDiscountPriceContainer.addView(csbDiscountType);
//        RelativeLayout.LayoutParams paramsAccept = (RelativeLayout.LayoutParams) csbDiscountType.getLayoutParams();
//        paramsAccept.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramsAccept.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramsAccept.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        csbDiscountType.setLayoutParams(paramsAccept);
//
//
//        rlDiscountPriceContainer.addView(etPriceValue);
//        RelativeLayout.LayoutParams paramsEffective = (RelativeLayout.LayoutParams) etPriceValue.getLayoutParams();
//        paramsEffective.width = (int) (getScreenWidth() * 0.3f);
//        paramsEffective.height = RelativeLayout.LayoutParams.MATCH_PARENT;
//        paramsEffective.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        paramsEffective.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
//        paramsEffective.rightMargin = getActualWidthOnThisDevice(20);
//        etPriceValue.setLayoutParams(paramsEffective);
//        etPriceValue.setTextColor(getColor(R.color.common_gray));
//        etPriceValue.setId(View.generateViewId());
//
//
//        rlDiscountPriceContainer.addView(tvPriceCurrency);
//        RelativeLayout.LayoutParams paramsSwitchAccept = (RelativeLayout.LayoutParams) tvPriceCurrency.getLayoutParams();
//        paramsSwitchAccept.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramsSwitchAccept.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramsSwitchAccept.addRule(RelativeLayout.LEFT_OF, etPriceValue.getId());
//        paramsSwitchAccept.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        tvPriceCurrency.setLayoutParams(paramsSwitchAccept);
//
//
//        rlAccountContainer.addView(tvManager);
//        RelativeLayout.LayoutParams paramstvHobbies = (RelativeLayout.LayoutParams) tvManager.getLayoutParams();
//        paramstvHobbies.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramstvHobbies.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramstvHobbies.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        paramstvHobbies.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
//        tvManager.setLayoutParams(paramstvHobbies);
//
//
//        rlAccountContainer.addView(tvManagerValue);
//        RelativeLayout.LayoutParams paramsImSendMail = (RelativeLayout.LayoutParams) tvManagerValue.getLayoutParams();
//        paramsImSendMail.width = (int) (getScreenWidth() * 0.4f);
//        paramsImSendMail.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramsImSendMail.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        paramsImSendMail.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
//
//        tvManagerValue.setLayoutParams(paramsImSendMail);
//        tvManagerValue.setTextColor(getColor(R.color.common_gray));
//        tvManagerValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
//        rlDiscountPasswordContainer.addView(tvPassword);
//        RelativeLayout.LayoutParams paramstvCurrency = (RelativeLayout.LayoutParams) tvPassword.getLayoutParams();
//        paramstvCurrency.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramstvCurrency.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramstvCurrency.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        paramstvCurrency.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
//        tvPassword.setLayoutParams(paramstvCurrency);
//
//
//        rlDiscountPasswordContainer.addView(etPasswordValue);
//        RelativeLayout.LayoutParams paramcheckIn = (RelativeLayout.LayoutParams) etPasswordValue.getLayoutParams();
//        paramcheckIn.width = (int) (getScreenWidth() * 0.4f);
//        paramcheckIn.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramcheckIn.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        paramcheckIn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
//
//        etPasswordValue.setLayoutParams(paramcheckIn);
//        etPasswordValue.setTextColor(getColor(R.color.common_gray));
//        etPasswordValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
//        rlSubTotalContainer.addView(tvSubTotalValue);
//
//        RelativeLayout.LayoutParams paramSubTotalValue = (RelativeLayout.LayoutParams) tvSubTotalValue.getLayoutParams();
//        paramSubTotalValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramSubTotalValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramSubTotalValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
//        paramSubTotalValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        tvSubTotalValue.setLayoutParams(paramSubTotalValue);
//        tvSubTotalValue.setId(View.generateViewId());
//
//        rlSubTotalContainer.addView(tvSubTotalCurrency);
//        RelativeLayout.LayoutParams paramSubTotalCurrency = (RelativeLayout.LayoutParams) tvSubTotalCurrency.getLayoutParams();
//        paramSubTotalCurrency.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramSubTotalCurrency.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramSubTotalCurrency.addRule(RelativeLayout.LEFT_OF, tvSubTotalValue.getId());
//        paramSubTotalCurrency.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        tvSubTotalCurrency.setLayoutParams(paramSubTotalCurrency);
//        tvSubTotalCurrency.setId(View.generateViewId());
//
//        rlSubTotalContainer.addView(tvSubTotal);
//        RelativeLayout.LayoutParams paramSubTotal = (RelativeLayout.LayoutParams) tvSubTotal.getLayoutParams();
//        paramSubTotal.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramSubTotal.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramSubTotal.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        paramSubTotal.addRule(RelativeLayout.LEFT_OF, tvSubTotalCurrency.getId());
//        tvSubTotal.setLayoutParams(paramSubTotal);
//
//
//        rlTaxContainer.addView(tvTaxValue);
//        RelativeLayout.LayoutParams paramTaxValue = (RelativeLayout.LayoutParams) tvTaxValue.getLayoutParams();
//        paramTaxValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramTaxValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramTaxValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
//        paramTaxValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        tvTaxValue.setLayoutParams(paramTaxValue);
//        tvTaxValue.setId(View.generateViewId());
//
//        rlTaxContainer.addView(tvTaxCurrency);
//        RelativeLayout.LayoutParams paramTaxCurrency = (RelativeLayout.LayoutParams) tvTaxCurrency.getLayoutParams();
//        paramTaxCurrency.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramTaxCurrency.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramTaxCurrency.addRule(RelativeLayout.LEFT_OF, tvTaxValue.getId());
//        paramTaxCurrency.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        tvTaxCurrency.setLayoutParams(paramTaxCurrency);
//        tvTaxCurrency.setId(View.generateViewId());
//
//        rlTaxContainer.addView(tvTax);
//        RelativeLayout.LayoutParams paramTax = (RelativeLayout.LayoutParams) tvTax.getLayoutParams();
//        paramTax.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramTax.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramTax.addRule(RelativeLayout.LEFT_OF, tvTaxCurrency.getId());
//        paramTax.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        tvTax.setLayoutParams(paramTax);
//
//
//        rlDiscountContainer.addView(tvDiscountValue);
//
//        RelativeLayout.LayoutParams paramDiscountValue = (RelativeLayout.LayoutParams) tvDiscountValue.getLayoutParams();
//        paramDiscountValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramDiscountValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramDiscountValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
//        paramDiscountValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        tvDiscountValue.setLayoutParams(paramDiscountValue);
//        tvDiscountValue.setId(View.generateViewId());
//
//        rlDiscountContainer.addView(tvDiscountCurrency);
//        RelativeLayout.LayoutParams paramDiscountCurrency = (RelativeLayout.LayoutParams) tvDiscountCurrency.getLayoutParams();
//        paramDiscountCurrency.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramDiscountCurrency.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramDiscountCurrency.addRule(RelativeLayout.LEFT_OF, tvDiscountValue.getId());
//
//        paramDiscountCurrency.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        tvDiscountCurrency.setLayoutParams(paramDiscountCurrency);
//        tvDiscountCurrency.setId(View.generateViewId());
//
//        rlDiscountContainer.addView(tvDiscount);
//        RelativeLayout.LayoutParams paramDiscount = (RelativeLayout.LayoutParams) tvDiscount.getLayoutParams();
//        paramDiscount.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramDiscount.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramDiscount.addRule(RelativeLayout.LEFT_OF, tvDiscountCurrency.getId());
//        paramDiscount.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        tvDiscount.setLayoutParams(paramDiscount);
//
//        rlTotalContainer.addView(tvTotalValue);
//        RelativeLayout.LayoutParams paramTotalValue = (RelativeLayout.LayoutParams) tvTotalValue.getLayoutParams();
//        paramTotalValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramTotalValue.height = RelativeLayout.LayoutParams.MATCH_PARENT;
//        paramTotalValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        paramTotalValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
//        tvTotalValue.setLayoutParams(paramTotalValue);
//        tvTotalValue.setId(View.generateViewId());
//        tvTotalValue.setTextColor(getColor(R.color.common_red));
//
//        rlTotalContainer.addView(tvTotalCurrency);
//        RelativeLayout.LayoutParams paramTotalCurrency = (RelativeLayout.LayoutParams) tvTotalCurrency.getLayoutParams();
//        paramTotalCurrency.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramTotalCurrency.height = RelativeLayout.LayoutParams.MATCH_PARENT;
//        paramTotalCurrency.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        paramTotalCurrency.addRule(RelativeLayout.LEFT_OF, tvTotalValue.getId());
//        tvTotalCurrency.setLayoutParams(paramTotalCurrency);
//        tvTotalCurrency.setId(View.generateViewId());
//
//        rlTotalContainer.addView(tvTotal);
//        RelativeLayout.LayoutParams paramTotal = (RelativeLayout.LayoutParams) tvTotal.getLayoutParams();
//        paramTotal.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramTotal.height = RelativeLayout.LayoutParams.MATCH_PARENT;
//        paramTotal.addRule(RelativeLayout.CENTER_VERTICAL, tvTotalCurrency.getId());
//        paramTotal.addRule(RelativeLayout.LEFT_OF, tvTotalCurrency.getId());
//        tvTotal.setLayoutParams(paramTotal);
//
//
//        rlDepositContainer.addView(tvDepositValue);
//        RelativeLayout.LayoutParams paramDepositValue = (RelativeLayout.LayoutParams) tvDepositValue.getLayoutParams();
//        paramDepositValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramDepositValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramDepositValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
//        paramDepositValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        tvDepositValue.setLayoutParams(paramDepositValue);
//        tvDepositValue.setId(View.generateViewId());
//
//
//        rlDepositUserMoney.addView(tvUserMoney);
//        RelativeLayout.LayoutParams tvUserMoneyParams =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
//        tvUserMoneyParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
//        tvUserMoneyParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        tvUserMoney.setLayoutParams(tvUserMoneyParams);
//
//
//
//
//
//
//        double dDeposit = 0;
//        try {
//            dDeposit = Double.parseDouble(deposit);
//        } catch (NumberFormatException e) {
//            Utils.log(e.getMessage());
//        }
//
//        if (dDeposit <= 0) {
//            rlDepositContainer.setVisibility(View.GONE);
//        }
//
//        rlConfirmContainer.addView(tvConfirm);
//        RelativeLayout.LayoutParams paramConfirm = (RelativeLayout.LayoutParams) tvConfirm.getLayoutParams();
//        paramConfirm.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//        paramConfirm.height = getActualHeightOnThisDevice(100);
//        paramConfirm.addRule(RelativeLayout.CENTER_IN_PARENT, LAYOUT_TRUE);
//        paramConfirm.topMargin = getActualHeightOnThisDevice(40);
//        tvConfirm.setLayoutParams(paramConfirm);
//
//        if (!AppUtils.getShowSalesTax(mContext)) {
//            rlTaxContainer.setVisibility(View.GONE);
//        }
//
//    }
//
//    @Override
//    protected void setPropertyOfControls() {
//
//        tvPaymentPattern.setTextColor(getColor(R.color.common_black));
//        tvPaymentPatternValue.setTextColor(getColor(R.color.common_gray));
//        String paymentPattern = AppUtils.getDefaultPaymentPattern(mContext);
//        switch (paymentPattern) {
//            case "1":
//                rechargeType = "1";
//                tvPaymentPatternValue.setText(getString(R.string.tag_pay_cash));
//                break;
//            case "5":
//                rechargeType = "5";
//                tvPaymentPatternValue.setText(getString(R.string.tag_balance_account));
//                addBalanceAccount();
//                netLinkBalance();
//                break;
//            case "2":
//                rechargeType = "2";
//                tvPaymentPatternValue.setText(getString(R.string.tag_voucher));
//                break;
//            case "3":
//                rechargeType = "3";
//                tvPaymentPatternValue.setText(getString(R.string.tag_credit_card));
//                break;
//            case "4":
//                rechargeType = "4";
//                tvPaymentPatternValue.setText(getString(R.string.tag_third_party));
//                break;
//            default:
//                break;
//        }
//
//        tvPaymentPattern.setText(getString(R.string.shopping_pay_payment_pattern));
//
//        tvApplyDiscount.setText(getString(R.string.shopping_pay_applay_discount));
//        tvApplyDiscount.setTextColor(getColor(R.color.common_deep_blue));
//        etDiscountReason.setHint(getString(R.string.shopping_pay_discount_reason));
//        etDiscountReason.setBackgroundResource(R.drawable.textview_corner);
//        etDiscountReason.setGravity(Gravity.CENTER_VERTICAL);
//
//        etPriceValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
//        etPriceValue.setPadding(0, 0, 0, 0);
//        etPriceValue.setHint(getString(R.string.shopping_pay_discount_hit));
//
//        etPriceValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//
//                EditText editText = (EditText) view;
//                String currentString = editText.getText().toString();
//
//                double money = 0;
//                if (hasFocus) {
//                    if (currentString.contains(AppUtils.getCurrentCurrency(getBaseActivity()))) {
//                        currentString = currentString.replace(AppUtils.getCurrentCurrency(getBaseActivity()), Constants.STR_EMPTY);
//                    }
//
//                    try {
//                        money = Double.parseDouble(currentString);
//                    } catch (NumberFormatException e) {
//                        Utils.log(e.getMessage());
//                    }
//                    if (money != 0) {
//                        editText.setText(currentString);
//                    } else {
//                        editText.setText(Constants.STR_EMPTY);
//                    }
//                } else {
//                    if (!currentString.contains(AppUtils.getCurrentCurrency(getBaseActivity())) && !csbDiscountType.isChecked()) {
//                        currentString = AppUtils.getCurrentCurrency(getBaseActivity()) + Constants.STR_EMPTY + currentString;
//                    }
//                    editText.setText(currentString);
//                }
//            }
//        });
//
//        etPasswordValue.setHint(getString(R.string.shopping_pay_password));
//        etPasswordValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//
//        tvManager.setText(getString(R.string.shopping_pay_manager));
//
//        tvManagerValue.setSingleLine();
//        tvManagerValue.setHint(getString(R.string.shopping_pay_manager));
//
//        tvPassword.setText(getString(R.string.shopping_pay_password));
//
//        tvSubTotal.setText(getString(R.string.shopping_pay_subtotal) + Constants.STR_SPACE + AppUtils.getCurrentCurrency(getBaseActivity()) + Constants.STR_SPACE);
//
//        tvTax.setText(getString(R.string.shopping_pay_tax) + Constants.STR_SPACE + AppUtils.getCurrentCurrency(getBaseActivity()) + Constants.STR_SPACE);
//
//        tvDiscount.setText(getString(R.string.shopping_pay_discount) + Constants.STR_SPACE + AppUtils.getCurrentCurrency(getBaseActivity()) + Constants.STR_SPACE);
//
//        tvTotal.setText(getString(R.string.shopping_pay_total) + Constants.STR_SPACE + AppUtils.getCurrentCurrency(getBaseActivity()) + Constants.STR_SPACE);
//        tvTotal.setTextColor(getColor(R.color.common_wanted_red));
//
//        tvConfirm.setText(getString(R.string.shopping_pay_confirm_payment));
//        tvConfirm.setBackground(getResources().getDrawable(R.drawable.member_add_button));
//        tvConfirm.setTextColor(getColor(R.color.common_white));
//        tvConfirm.setGravity(Gravity.CENTER);
//
//        tvSubTotal.setTextSize(Constants.FONT_SIZE_SMALLER);
//        tvTax.setTextSize(Constants.FONT_SIZE_SMALLER);
//        tvDiscount.setTextSize(Constants.FONT_SIZE_SMALLER);
//
//        tvTotal.setTextSize(Constants.FONT_SIZE_LARGER);
//        tvTotalCurrency.setTextSize(Constants.FONT_SIZE_LARGER);
//        tvTotalValue.setTextSize(Constants.FONT_SIZE_LARGER);
//    }
//
//    @Override
//    protected void configActionBar() {
//        setStackedActionBar();
//        getTvLeftTitle().setText(getResources().getString(R.string.shopping_pay_title));
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        Bundle bundle = getArguments();
//        if (bundle != null) {
////            annualFee = bundle.getString("annualFee");
//            memberId = bundle.getInt("member_id");
//            paymentId = bundle.getInt("payment_id");
//        }
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }
//
//    private void netLinkBalance() {
//        Map<String, String> params = new HashMap<>();
//
//        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
//        params.put(ApiKey.SHOPPING_CONFIRM_PAY_BOOKING_NO, bookingNo);
//
//        HttpManager<JsonBalanceList> hh = new HttpManager<JsonBalanceList>(ShoppingConfirmPayFragment.this) {
//            @Override
//            public void onJsonSuccess(JsonBalanceList jo) {
//                int returnCode = jo.getReturnCode();
//                if (returnCode == Constants.RETURN_CODE_20301) {
//                    tvAccountValue.setText(jo.getDataList().getAccount());
//                    tvBalanceValue.setText(String.valueOf(jo.getDataList().getBalance()));
//                    customId = jo.getDataList().getCustomId();
//                }
//            }
//
//            @Override
//            public void onJsonError(VolleyError error) {
//
//            }
//        };
//        hh.startGet(getActivity(), ApiManager.HttpApi.Balance, params);
//
//    }
//
//    private void sendTempPwdGet() {
//        Map<String, String> params = new HashMap<>();
//
//        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
//        params.put(ApiKey.SHOPPING_CONFIRM_PAY_ACCOUNT, tvAccountValue.getText().toString());
//
//        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShoppingConfirmPayFragment.this) {
//            @Override
//            public void onJsonSuccess(BaseJsonObject jo) {
//                int returnCode = jo.getReturnCode();
//                Utils.showShortToast(getActivity(), jo.getReturnInfo());
//
//                if (returnCode == Constants.RETURN_CODE_FORGOT_PASSWORD_USER_EXIST) {
//                    isTiming = true;
//                    btnPassCode.setText(String.valueOf(Constants.SECOND_60));
//                    startTime();
//                } else {
//                    btnPassCode.setEnabled(true);
//                }
//            }
//
//            @Override
//            public void onJsonError(VolleyError error) {
//                btnPassCode.setEnabled(true);
//            }
//        };
//        hh.startGet(getActivity(), ApiManager.HttpApi.SendTempPwd, params);
//
//    }
//
//
//    private void netLinkPayConfirm() {
//        Map<String, String> params = new HashMap<>();
//
//        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
//        params.put(ApiKey.SHOPPING_CONFIRM_PAY_BOOKING_NO, bookingNo);
//
//        params.put(ApiKey.SHOPPING_CONFIRM_PAY_PAY_TYPE, rechargeType);
//
//        if ("5".equals(rechargeType)) {
//            params.put(ApiKey.SHOPPING_CONFIRM_PAY_ACCOUNT, tvAccountValue.getText().toString());
//            params.put(ApiKey.SHOPPING_CONFIRM_PAY_CUSTOM_ID, customId);
//            params.put(ApiKey.SHOPPING_CONFIRM_PAY_PASSCODE, etPassCodeValue.getText().toString());
//        } else {
//            params.put(ApiKey.SHOPPING_CONFIRM_PAY_CUSTOM_ID, StringUtils.EMPTY);
//            params.put(ApiKey.SHOPPING_CONFIRM_PAY_PASSCODE, StringUtils.EMPTY);
//        }
//
//        params.put(ApiKey.SHOPPING_CONFIRM_PAY_ID, payId);
//        params.put(ApiKey.SHOPPING_CONFIRM_PAY_DISCOUNT_REASON, etDiscountReason.getValue());
//        if (csbDiscountType.isChecked()) {
//            params.put(ApiKey.SHOPPING_CONFIRM_PAY_DISCOUNT_TYPE, "1");
//        } else {
//            params.put(ApiKey.SHOPPING_CONFIRM_PAY_DISCOUNT_TYPE, "2");
//        }
//        params.put(ApiKey.SHOPPING_CONFIRM_PAY_DISCOUNT_ACCOUNT, tvManagerValue.getText().toString());
//        params.put(ApiKey.SHOPPING_CONFIRM_PAY_DISCOUNT_PASSCODE, etPasswordValue.getText().toString());
//        params.put(ApiKey.SHOPPING_CONFIRM_PAY_SUBTOTAL, tvSubTotalValue.getText().toString());
//        params.put(ApiKey.SHOPPING_CONFIRM_PAY_TAX, tvTaxValue.getText().toString());
//        params.put(ApiKey.SHOPPING_CONFIRM_PAY_DISCOUNT, etPriceValue.getValue());
//
//
//        double dDeposit = 0;
//        try {
//            dDeposit = Double.parseDouble(deposit);
//        } catch (NumberFormatException e) {
//            Utils.log(e.getMessage());
//        }
//        double dTotal = 0;
//        try {
//            dTotal = Double.parseDouble(tvTotalValue.getText().toString());
//        } catch (NumberFormatException e) {
//            Utils.log(e.getMessage());
//        }
//        double userDeposit = dDeposit - dTotal;
//        if (userDeposit < 0) {
//            userDeposit = dDeposit;
//        } else {
//
//            userDeposit = dTotal;
//        }
//        params.put(ApiKey.SHOPPING_CONFIRM_PAY_DEPOSIT, String.valueOf(userDeposit));
//        params.put(ApiKey.SHOPPING_CONFIRM_PAY_TOTAL, tvTotalValue.getText().toString());
//        if (purchaseFlag == ShoppingPaymentFragment.PURCHASE_FLAG_CHECKOUT) {
//            params.put(ApiKey.SHOPPING_CHECK_OUT, Constants.PAYMENT_CHECK_OUT);
//        } else {
//            params.put(ApiKey.SHOPPING_CHECK_OUT, Constants.PAYMENT_NOT_CHECK_OUT);
//        }
//
//
//        HttpManager<JsonPayPostRet> hh = new HttpManager<JsonPayPostRet>(ShoppingConfirmPayFragment.this) {
//            @Override
//            public void onJsonSuccess(JsonPayPostRet jo) {
//                int returnCode = jo.getReturnCode();
//                if (returnCode == Constants.RETURN_CODE_20118_PAY_SUCCESSFULLY) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString(ApiKey.PAY_ID, jo.getDataList().getPayId());
//                    bundle.putString(TransKey.SHOPPING_PLAYER_NAME, playerName);
//                    bundle.putInt(TransKey.SHOPPING_PURCHASE_FLAG, purchaseFlag);
//                    bundle.putString(TransKey.COMMON_FROM_PAGE, fromPagePayment);
//                    bundle.putInt(TransKey.SHOPPING_BOOKING_FLAG, bookingFlag);
//
//                    push(ShoppingPaymentSucceededFragment.class, bundle);
//                } else {
//
//                    SelectDatePopupWindow.OnDateSelectClickListener itemsOnClick = new SelectDatePopupWindow.OnDateSelectClickListener() {
//                        @Override
//                        public void OnGoodItemClick(String flag, String content) {
//                            switch (flag) {
//                                case "2":
//                                    netLinkPayConfirm();
//                                    shoppingPaymentFailedPopupWindow.dismiss();
//                                    break;
//                                case "3":
//                                    shoppingPaymentFailedPopupWindow.dismiss();
//                                    break;
//                            }
//
//                        }
//                    };
//
//                    shoppingPaymentFailedPopupWindow = new ShoppingPaymentFailedPopupWindow(getActivity(), itemsOnClick);
//                    shoppingPaymentFailedPopupWindow.setMes(jo.getReturnInfo());
//
//                    //显示窗口
//                    shoppingPaymentFailedPopupWindow.showAtLocation(ShoppingConfirmPayFragment.this.getRootView()
//                            .findViewById(R.id.ll_container), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//                }
//            }
//
//            @Override
//            public void onJsonError(VolleyError error) {
//
//            }
//        };
//        hh.start(getActivity(), ApiManager.HttpApi.Pay, params);
//    }
}
