/**
 * Project Name: itee
 * File Name:  PlayerRefundFragment.java
 * Package Name: cn.situne.itee.fragment.player
 * Date:   2015-03-28
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.player;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.baoyz.actionsheet.ActionSheet;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.adapter.RefundDetailAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.ParamRefundExpandable;
import cn.situne.itee.entity.PurchaseRefundExpandable;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonPurchaseHistoryDetailRecord;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:PlayerRefundFragment <br/>
 * Function: member's refund  fragment. <br/>
 * UI:  04-8-6
 * Date: 2015-03-28 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class PlayerRefundFragment extends BaseFragment {

    private RelativeLayout rlNameContainer;
    private RelativeLayout rlDepositTitleContainer;
    private RelativeLayout rlTipsContainer;
    private RelativeLayout rlConfirmContainer;
    private RelativeLayout rlTotalContainer;


    private RelativeLayout rlShowTax;
    private RelativeLayout rlShowBalance;
    private RelativeLayout rlShowVoucher;
    private RelativeLayout rlShowDisCount;
    private RelativeLayout rlShowSubtotal;
    private RelativeLayout rlShowTotal;

    private RelativeLayout rlShowDeposit;


    private IteeTextView tvShowDisCount;
    private IteeTextView tvShowSubtotal;
    private IteeTextView tvShowTax;
    private IteeTextView tvShowBalance;
    private IteeTextView tvShowDeposit;
    private IteeTextView tvShowTotal;

    private LinearLayout lastMoneyLayout;

    private RefundDetailAdapter adapter;

    private IteeTextView tvName;
    private IteeTextView tvNameValue;

    private IteeTextView tvSignature;
    private ImageView imSignature;

    //  private IteeTextView tvTipValue;

    private IteeTextView tvConfirm;

    private IteeTextView tvPaymentPattern;
    private IteeTextView tvPaymentPatternValue;

    private IteeTextView tvTotal;
    private IteeTextView tvTotalCurrency;
    private IteeTextView tvTotalValue;

    private IteeTextView tvShowVoucher;


    private String payId;
    private String date;
    private JsonPurchaseHistoryDetailRecord dataParameter;
    private ExpandableListView elvShoppingListView;

    private OnDateSelectClickListener dateSelectReturn;
    private int allChildCount;

    private String subtotal;

    private String totalCost;

    private IteeTextView paymentTextType;
    private String payment;

    private  LinearLayout llTitle;

    ArrayList<JsonPurchaseHistoryDetailRecord.PricingData> playerPricingList;


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
                    payment = Constants.PAYMENT_PATTERN_CASH;
                    paymentTextType.setText(getString(R.string.tag_pay_cash));
                    break;
                case 1:
                    payment = Constants.PAYMENT_PATTERN_CREDIT_CARD;
                    paymentTextType.setText(getString(R.string.tag_credit_card));

                    break;
                case 2:
                    payment = Constants.PAYMENT_PATTERN_BALANCE_ACCOUNT;
                    paymentTextType.setText(getString(R.string.tag_balance_account));
                    break;

            }


            actionSheet.dismiss();
        }
    };

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_player_info_refund;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.do_edit_administration;
    }

    /**
     * addGoodListView:add all good's view.
     */
    private void addGoodListView() {

        subtotal = dataParameter.getDataList().getSubtotal();

        //last money value set
        double dSumBigVoucher = 0;
        double dSumVoucher = 0;

        try {
            dSumBigVoucher = Double.parseDouble(dataParameter.getDataList().getSumBigVoucher());
        } catch (NumberFormatException e) {
            dSumBigVoucher = 0;
        }
        try {
            dSumVoucher = Double.parseDouble(dataParameter.getDataList().getSumVoucher());
        } catch (NumberFormatException e) {
            dSumVoucher = 0;
        }
        double dVoucher = dSumBigVoucher + dSumVoucher;
        tvShowVoucher.setText(getShowMoneyText(R.string.shopping_voucher, "-" + Utils.get2DigitDecimalString(String.valueOf(dVoucher))));
        tvShowDeposit.setText(getShowMoneyText(R.string.shopping_deposit, "-" + dataParameter.getDataList().getDeposit()));
        tvShowSubtotal.setText(getShowMoneyText(R.string.shopping_subtotal, dataParameter.getDataList().getSubtotal()));
        tvShowBalance.setText(getShowMoneyText(R.string.shopping_balance, "-" + dataParameter.getDataList().getBalanceAccountMoney()));
        tvShowDisCount.setText(getShowMoneyText(R.string.shopping_discount, "-" + dataParameter.getDataList().getDiscount()));
        tvShowTax.setText(getShowMoneyText(R.string.shopping_tax, dataParameter.getDataList().getTax()));

        tvShowTotal.setText(getShowMoneyText(R.string.shopping_total,  dataParameter.getDataList().getTotal()));


        if (!AppUtils.isTaxExcludeGoods(getActivity())) {
            rlShowTax.setVisibility(View.GONE);
        }

        if (dVoucher <= 0) {
            rlShowVoucher.setVisibility(View.GONE);
        }

        if (Constants.STR_0.equals(dataParameter.getDataList().getDiscount())) {
            rlShowDisCount.setVisibility(View.GONE);
        }

        if (Constants.STR_0.equals(dataParameter.getDataList().getBalanceAccountMoney())) {
            rlShowBalance.setVisibility(View.GONE);
        }
        if (Constants.STR_0.equals(dataParameter.getDataList().getDeposit())) {
            rlShowDeposit.setVisibility(View.GONE);
        }


        Date refundDateObj = DateUtils.getDateFromAPIYearMonthDayHourMonthSecond(date);
        String currentShowDate = DateUtils.getCurrentShowYearMonthDayFromDate(refundDateObj, mContext);
        JsonPurchaseHistoryDetailRecord.DataList dataList = dataParameter.getDataList();
        tvName.setText(currentShowDate);

        if (dataList.getPaymentPattern() != null) {
            String temp = StringUtils.EMPTY;



            switch (dataList.getPayment()) {

                case Constants.PAYMENT_PATTERN_CASH:
                    temp = getString(R.string.tag_pay_cash);
                    break;
                case Constants.PAYMENT_PATTERN_CREDIT_CARD:
                    temp = getString(R.string.tag_credit_card);
                    break;
                case Constants.PAYMENT_PATTERN_BALANCE_ACCOUNT:
                    temp = getString(R.string.tag_balance_account);
                    break;

                case Constants.PAYMENT_PATTERN_VOUCHERS:
                    temp = getString(R.string.tag_voucher);
                    break;

                case Constants.PAYMENT_PATTERN_THIRD_PARTY:
                    temp = getString(R.string.tag_third_party);
                    break;

                case Constants.PAYMENT_PATTERN_BANK_TRANSFER:
                    temp = getString(R.string.tag_bank_transfer);
                    break;

            }
            tvPaymentPatternValue.setText(temp);

        }

        List<PurchaseRefundExpandable> dataAdapter = new ArrayList<>();


        List<JsonPurchaseHistoryDetailRecord.GoodListItem> goodListItems = dataParameter.getDataList().getGoodsList();
        List<JsonPurchaseHistoryDetailRecord.AAListItem> aaListItems = dataParameter.getDataList().getAaList();
        List<JsonPurchaseHistoryDetailRecord.PackageListItem> packageListItems = dataParameter.getDataList().getPackageList();


        List<JsonPurchaseHistoryDetailRecord.PricingData> pricingDataList = dataParameter.getDataList().getPricingDataList();






        for (JsonPurchaseHistoryDetailRecord.PricingData pricingData:pricingDataList){
            JsonPurchaseHistoryDetailRecord.PricingData historyItemData = findHistoryItemData(pricingData.getBookingNo());

            ArrayList<JsonPurchaseHistoryDetailRecord.PricingData> pricingList;
            if (historyItemData == null) {
                historyItemData = new JsonPurchaseHistoryDetailRecord.PricingData();
                historyItemData.setBookingNo(pricingData.getBookingNo());
                pricingList = new ArrayList<>();
                pricingList.add(pricingData);
                historyItemData.setProductList(pricingList);
                playerPricingList.add(historyItemData);

            }else {
                historyItemData.getProductList().add(pricingData);
            }
        }

        if (playerPricingList != null && playerPricingList.size() > 0) {


            for (JsonPurchaseHistoryDetailRecord.PricingData pricingData:playerPricingList){

                PurchaseRefundExpandable toGoodListItem = new PurchaseRefundExpandable();
                toGoodListItem.setType(RefundDetailAdapter.TYPE_TIMES);
                toGoodListItem.setName(getString(R.string.pricing_deduct)+pricingData.getPricingTimes());


                toGoodListItem.setPricingDataList(pricingDataList);
                dataAdapter.add(toGoodListItem);
            }


        }



        if (goodListItems != null && goodListItems.size() > 0) {
            for (int i = 0; i < goodListItems.size(); i++) {
                JsonPurchaseHistoryDetailRecord.GoodListItem fromGoodListItem = goodListItems.get(i);
                PurchaseRefundExpandable toGoodListItem = new PurchaseRefundExpandable();
                toGoodListItem.setName(fromGoodListItem.getName());
                toGoodListItem.setCount(fromGoodListItem.getCount());
                toGoodListItem.setPrice(fromGoodListItem.getPrice());
                toGoodListItem.setType(RefundDetailAdapter.TYPE_GOOD);
                toGoodListItem.setRefundflag(fromGoodListItem.getRefundFlag());
                toGoodListItem.setId(fromGoodListItem.getId());
                toGoodListItem.setVoucherItems(fromGoodListItem.getVoucherItems());
                dataAdapter.add(toGoodListItem);
            }
        }


        if (aaListItems != null && aaListItems.size() > 0) {
            for (int i = 0; i < aaListItems.size(); i++) {
                JsonPurchaseHistoryDetailRecord.AAListItem fromGoodListItem = aaListItems.get(i);

                List<JsonPurchaseHistoryDetailRecord.GoodListItem> fromGoodListItemChild = fromGoodListItem.getGoodsList();
                PurchaseRefundExpandable toGoodListItem = new PurchaseRefundExpandable();
                toGoodListItem.setName(fromGoodListItem.getName());
                toGoodListItem.setPrice(fromGoodListItem.getAmount());
                toGoodListItem.setType(RefundDetailAdapter.TYPE_AA);
                toGoodListItem.setRefundflag(fromGoodListItem.getRefundFlag());
                toGoodListItem.setId(fromGoodListItem.getId());
                List<PurchaseRefundExpandable.GoodListItem> toGoodListItemChild = new ArrayList<>();
                for (int j = 0; j < fromGoodListItemChild.size(); j++) {
                    JsonPurchaseHistoryDetailRecord.GoodListItem from = fromGoodListItemChild.get(j);
                    PurchaseRefundExpandable.GoodListItem to = new PurchaseRefundExpandable.GoodListItem();
                    to.setId(from.getId());
                    to.setCount(from.getCount());
                    to.setName(from.getName());
                    to.setPrice(from.getPrice());
                    to.setCheck(false);
                    allChildCount++;
                    toGoodListItemChild.add(to);
                }
                toGoodListItem.setGoodList(toGoodListItemChild);
                dataAdapter.add(toGoodListItem);
            }
        }

        if (packageListItems != null && packageListItems.size() > 0) {
            for (int i = 0; i < packageListItems.size(); i++) {
                JsonPurchaseHistoryDetailRecord.PackageListItem fromGoodListItem = packageListItems.get(i);

                List<JsonPurchaseHistoryDetailRecord.GoodListItem> fromGoodListItemChild = fromGoodListItem.getGoodsList();
                PurchaseRefundExpandable toGoodListItem = new PurchaseRefundExpandable();
                toGoodListItem.setName(fromGoodListItem.getName());
                toGoodListItem.setPrice(fromGoodListItem.getAmount());
                toGoodListItem.setId(fromGoodListItem.getId());
                toGoodListItem.setRefundflag(fromGoodListItem.getRefundFlag());
                toGoodListItem.setType(RefundDetailAdapter.TYPE_PACKAGE);
                List<PurchaseRefundExpandable.GoodListItem> toGoodListItemChild = new ArrayList<>();
                for (int j = 0; j < fromGoodListItemChild.size(); j++) {


                    JsonPurchaseHistoryDetailRecord.GoodListItem from = fromGoodListItemChild.get(j);

                    PurchaseRefundExpandable.GoodListItem to = new PurchaseRefundExpandable.GoodListItem();
                    to.setId(from.getId());
                    to.setCount(from.getCount());
                    to.setName(from.getName());
                    to.setPrice(from.getPrice());
                    to.setCheck(false);
                    allChildCount++;
                    toGoodListItemChild.add(to);

                }
                toGoodListItem.setGoodList(toGoodListItemChild);
                dataAdapter.add(toGoodListItem);
            }
        }

        adapter = new RefundDetailAdapter(PlayerRefundFragment.this, elvShoppingListView, dataAdapter, dateSelectReturn, allChildCount);

        elvShoppingListView.addHeaderView(llTitle);
        elvShoppingListView.addFooterView(lastMoneyLayout);
        elvShoppingListView.setAdapter(adapter);


        //all expand
        for (int i = 0; i < dataAdapter.size(); i++) {
            elvShoppingListView.expandGroup(i);
        }
        //LayoutUtils.setListViewHeightBasedOnChildren(elvShoppingListView);
        //can not collapse group
        elvShoppingListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });



    }

    @Override
    protected void initControls(View rootView) {
        playerPricingList = new ArrayList<>();
        dateSelectReturn = new OnDateSelectClickListener() {
            @Override
            public void OnGoodItemClick(String flag, String content) {

                List<ParamRefundExpandable> param = new ArrayList<>();
                Map<Integer, PurchaseRefundExpandable> map = adapter.getCheckedItem();
                for (Map.Entry<Integer, PurchaseRefundExpandable> entry : map.entrySet()) {
                    PurchaseRefundExpandable temp = entry.getValue();
                    ParamRefundExpandable paramRefundExpandable = new ParamRefundExpandable();
                    paramRefundExpandable.setId(temp.getId());
                    paramRefundExpandable.setCount(String.valueOf(temp.getCount()));
                    paramRefundExpandable.setTotal_price(temp.getPrice());
                    param.add(paramRefundExpandable);
                }

                if (param.size() > 0) {
                    BigDecimal bigDecimal = new BigDecimal(0);

                    for (int i = 0; i < param.size(); i++) {
                        ParamRefundExpandable paramRefundExpandable = param.get(i);
                        bigDecimal = bigDecimal.add(new BigDecimal(paramRefundExpandable.getTotal_price()));

                    }
                    tvTotalValue.setText(Utils.get2DigitDecimalString(bigDecimal.toString()));


                } else {
                    tvTotalValue.setText(Constants.STR_0);
                }


            }
        };



        elvShoppingListView = (ExpandableListView) rootView.findViewById(R.id.elv_play_purchase_history_shopping_list);
        rlTipsContainer = new RelativeLayout(getBaseActivity());
        rlConfirmContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_purchase_history_send_sms);

        lastMoneyLayout =new LinearLayout(getBaseActivity());

        lastMoneyLayout.setOrientation(LinearLayout.VERTICAL);
        lastMoneyLayout.setBackgroundColor(getColor(R.color.common_white));

        LinearLayout.LayoutParams rlTotalContainerParams = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(80));
        rlTotalContainer = new RelativeLayout(getBaseActivity());
        rlTotalContainer.setLayoutParams(rlTotalContainerParams);


        AbsListView.LayoutParams llTitleParams = new AbsListView.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        llTitle = new LinearLayout(getBaseActivity());
        llTitle.setOrientation(LinearLayout.VERTICAL);
        llTitle.setLayoutParams(llTitleParams);

        llTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




        rlNameContainer =new RelativeLayout(getBaseActivity());
        AppUtils.addBottomSeparatorLine(rlNameContainer, getBaseActivity());
        rlDepositTitleContainer = new RelativeLayout(getBaseActivity());
        AppUtils.addBottomSeparatorLine(rlDepositTitleContainer, getBaseActivity());

        llTitle.addView(rlNameContainer);
        llTitle.addView(rlDepositTitleContainer);

//        <RelativeLayout
//        android:id="@+id/rl_play_purchase_history_name"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"/>
//
//        <RelativeLayout
//        android:id="@+id/rl_play_purchase_history_deposit"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:layout_below="@+id/rl_play_purchase_history_name"
//                />


        tvName = new IteeTextView(getActivity());
        tvNameValue = new IteeTextView(getActivity());
        tvPaymentPattern = new IteeTextView(getActivity());
        tvPaymentPatternValue = new IteeTextView(getActivity());
        tvSignature = new IteeTextView(getActivity());
        imSignature = new ImageView(getActivity());
        tvConfirm = new IteeTextView(getActivity());
        tvTotal = new IteeTextView(getActivity());
        tvTotalCurrency = new IteeTextView(getActivity());
        tvTotalValue = new IteeTextView(getActivity());


    }


    /**
     * addGoodListView:add all good's view.
     */

    private String getShowMoneyText(int resId, String money) {
        return getString(resId) + Constants.STR_COLON + AppUtils.getCurrentCurrency(getBaseActivity()) + money;

    }

    @Override
    protected void setDefaultValueOfControls() {
        tvTotal.setText(getString(R.string.play_refund) + ":");
        tvTotalCurrency.setText(AppUtils.getCurrentCurrency(mContext));
        tvTotalValue.setText("0");
        tvSignature.setText(getString(R.string.play_purchase_history_signature));
        String tmp = getString(R.string.play_refund_tips);
        SpannableString sp = new SpannableString(getString(R.string.play_refund_message));
        sp.setSpan(new StyleSpan(Typeface.BOLD), 0, tmp.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(getColor(R.color.common_black)), 0, tmp.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        sp.setSpan(new AbsoluteSizeSpan(30), 0, tmp.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvConfirm.setText(getString(R.string.play_refund_confirm));
        tvPaymentPattern.setText(getString(R.string.play_refund_payment));
    }

    @Override
    protected void setListenersOfControls() {
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doCheck()) {
                    netLinkDRefund();
                }
            }
        });

    }

    private JsonPurchaseHistoryDetailRecord.PricingData findHistoryItemData(String bookingNo) {
        for (JsonPurchaseHistoryDetailRecord.PricingData item : playerPricingList) {
            if (bookingNo.equals(item.getBookingNo())) {
                return item;
            }
        }
        return null;
    }

    @Override
    protected void setLayoutOfControls() {
        int padding = px2dp(40);
        LinearLayout.LayoutParams rlNameContainerParams = (LinearLayout.LayoutParams) rlNameContainer.getLayoutParams();
        rlNameContainerParams.height = (int) (getScreenHeight() * 0.05f);
        rlNameContainer.setLayoutParams(rlNameContainerParams);
        LinearLayout.LayoutParams rlDepositTitleContainerParams = (LinearLayout.LayoutParams) rlDepositTitleContainer.getLayoutParams();
        rlDepositTitleContainerParams.height = getActualHeightOnThisDevice(100);
        rlDepositTitleContainer.setLayoutParams(rlDepositTitleContainerParams);


        // rlTipsContainer.setPadding(padding, 0, padding, 0);
        rlConfirmContainer.setPadding(padding, padding, padding, padding);
        rlTotalContainer.setPadding(padding, 0, padding, 0);

        rlNameContainer.addView(tvName);
        RelativeLayout.LayoutParams paramsEtLogoutAfterTimeM = (RelativeLayout.LayoutParams) tvName.getLayoutParams();
        paramsEtLogoutAfterTimeM.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsEtLogoutAfterTimeM.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsEtLogoutAfterTimeM.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsEtLogoutAfterTimeM.setMargins(getActualWidthOnThisDevice(40), 0, 0, 0);
        tvName.setLayoutParams(paramsEtLogoutAfterTimeM);
        rlNameContainer.addView(tvNameValue);
        RelativeLayout.LayoutParams paramsPhotoImageViewValue = (RelativeLayout.LayoutParams) tvNameValue.getLayoutParams();
        paramsPhotoImageViewValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsPhotoImageViewValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsPhotoImageViewValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsPhotoImageViewValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvNameValue.setLayoutParams(paramsPhotoImageViewValue);

        rlDepositTitleContainer.addView(tvPaymentPattern);
        RelativeLayout.LayoutParams paramsName = (RelativeLayout.LayoutParams) tvPaymentPattern.getLayoutParams();
        paramsName.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsName.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsName.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsName.setMargins(getActualWidthOnThisDevice(40), 0, 0, 0);
        tvPaymentPattern.setLayoutParams(paramsName);


        rlDepositTitleContainer.addView(tvPaymentPatternValue);
        RelativeLayout.LayoutParams paramsNameValue = (RelativeLayout.LayoutParams) tvPaymentPatternValue.getLayoutParams();
        paramsNameValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsNameValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsNameValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsNameValue.rightMargin = getActualWidthOnThisDevice(20);
        paramsNameValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvPaymentPatternValue.setLayoutParams(paramsNameValue);

        LinearLayout.LayoutParams paymentPatternLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
        paymentPatternLayoutParams.topMargin = getActualHeightOnThisDevice(20);
        paymentPatternLayoutParams.rightMargin = getActualWidthOnThisDevice(20);
        RelativeLayout paymentPattern = new RelativeLayout(getBaseActivity());
        paymentPattern.setBackgroundColor(getColor(R.color.common_white));
        paymentPattern.setLayoutParams(paymentPatternLayoutParams);
        IteeTextView paymentText = new IteeTextView(getBaseActivity());
        paymentTextType = new IteeTextView(getBaseActivity());
        paymentTextType.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        paymentTextType.setTextColor(getColor(R.color.common_gray));


        RelativeLayout.LayoutParams paymentTextParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(400), RelativeLayout.LayoutParams.MATCH_PARENT);
        paymentTextParams.leftMargin = getActualWidthOnThisDevice(20);
        RelativeLayout.LayoutParams paymentTextTypeParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(400), RelativeLayout.LayoutParams.MATCH_PARENT);
        paymentTextTypeParams.rightMargin = getActualWidthOnThisDevice(20);
        paymentTextTypeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        paymentText.setLayoutParams(paymentTextParams);
        paymentTextType.setLayoutParams(paymentTextTypeParams);
        paymentText.setText(getString(R.string.refund_method));

        paymentPattern.addView(paymentText);
        paymentPattern.addView(paymentTextType);
        rlTipsContainer.addView(paymentPattern);

        paymentPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getBaseActivity().setTheme(com.baoyz.actionsheet.R.style.ActionSheetStyleIOS7);
                String[] tTags = new String[]{getString(R.string.tag_pay_cash)
                        , getString(R.string.tag_credit_card), getString(R.string.tag_balance_account)};

                ActionSheet.createBuilder(getActivity(), getFragmentManager())
                        .setCancelButtonTitle(getString(R.string.common_cancel))
                        .setOtherButtonTitles(tTags)
                        .setCancelButtonHidden(true)
                        .setCancelableOnTouchOutside(true).setListener(actionSheetListenerAddress).show();
            }
        });


        payment = Constants.PAYMENT_PATTERN_CASH;
        paymentTextType.setText(getString(R.string.tag_pay_cash));

        rlConfirmContainer.addView(tvConfirm);
        RelativeLayout.LayoutParams paramsBirth = (RelativeLayout.LayoutParams) tvConfirm.getLayoutParams();
        paramsBirth.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsBirth.height = getActualHeightOnThisDevice(100);
        paramsBirth.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvConfirm.setLayoutParams(paramsBirth);
        tvConfirm.setPadding(0, 5, 0, 5);


        rlTotalContainer.addView(tvTotalValue);

        RelativeLayout.LayoutParams paramTotalValue = (RelativeLayout.LayoutParams) tvTotalValue.getLayoutParams();
        paramTotalValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTotalValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTotalValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramTotalValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        tvTotalValue.setLayoutParams(paramTotalValue);
        tvTotalValue.setId(View.generateViewId());

        rlTotalContainer.addView(tvTotalCurrency);
        RelativeLayout.LayoutParams paramTotalCurrency = (RelativeLayout.LayoutParams) tvTotalCurrency.getLayoutParams();
        paramTotalCurrency.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTotalCurrency.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTotalCurrency.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramTotalCurrency.addRule(RelativeLayout.LEFT_OF, tvTotalValue.getId());
        tvTotalCurrency.setLayoutParams(paramTotalCurrency);
        tvTotalCurrency.setId(View.generateViewId());

        rlTotalContainer.addView(tvTotal);
        RelativeLayout.LayoutParams paramTotal = (RelativeLayout.LayoutParams) tvTotal.getLayoutParams();
        paramTotal.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTotal.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTotal.addRule(RelativeLayout.CENTER_VERTICAL, tvTotalCurrency.getId());
        paramTotal.addRule(RelativeLayout.LEFT_OF, tvTotalCurrency.getId());
        tvTotal.setLayoutParams(paramTotal);

        //last Money layout
        //voucher
        rlShowVoucher = new RelativeLayout(getBaseActivity());
        tvShowVoucher = new IteeTextView(getBaseActivity());
        rlShowVoucher.addView(tvShowVoucher);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvShowVoucher, getActualWidthOnThisDevice(20), getBaseActivity());
        //discount
        rlShowDisCount = new RelativeLayout(getBaseActivity());
        tvShowDisCount = new IteeTextView(getBaseActivity());
        rlShowDisCount.addView(tvShowDisCount);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvShowDisCount, getActualWidthOnThisDevice(20), getBaseActivity());

        //subtotal
        rlShowSubtotal = new RelativeLayout(getBaseActivity());
        tvShowSubtotal = new IteeTextView(getBaseActivity());
        rlShowSubtotal.addView(tvShowSubtotal);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvShowSubtotal, getActualWidthOnThisDevice(20), getBaseActivity());

        //tax
        rlShowTax = new RelativeLayout(getBaseActivity());
        tvShowTax = new IteeTextView(getBaseActivity());
        rlShowTax.addView(tvShowTax);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvShowTax, getActualWidthOnThisDevice(20), getBaseActivity());
        //balance
        rlShowBalance = new RelativeLayout(getBaseActivity());
        tvShowBalance = new IteeTextView(getBaseActivity());
        rlShowBalance.addView(tvShowBalance);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvShowBalance, getActualWidthOnThisDevice(20), getBaseActivity());

        //deposit
        rlShowDeposit = new RelativeLayout(getBaseActivity());
        tvShowDeposit = new IteeTextView(getBaseActivity());
        rlShowDeposit.addView(tvShowDeposit);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvShowDeposit, getActualWidthOnThisDevice(20), getBaseActivity());




        rlShowTotal= new RelativeLayout(getBaseActivity());
        tvShowTotal =  new IteeTextView(getBaseActivity());


        rlShowTotal.addView(tvShowTotal);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvShowTotal, getActualWidthOnThisDevice(20), getBaseActivity());



        lastMoneyLayout.addView(rlShowVoucher);
        lastMoneyLayout.addView(rlShowDisCount);
        lastMoneyLayout.addView(rlShowSubtotal);
        lastMoneyLayout.addView(rlShowTax);
        lastMoneyLayout.addView(rlShowBalance);
        lastMoneyLayout.addView(rlShowDeposit);
        lastMoneyLayout.addView(rlShowTotal);

        lastMoneyLayout.addView(AppUtils.getSeparatorLine(getBaseActivity()));
        lastMoneyLayout.addView(rlTotalContainer);

        lastMoneyLayout.addView(AppUtils.getSeparatorLine(getBaseActivity()));
        View v = AppUtils.getSeparatorLine(getBaseActivity());
        LinearLayout.LayoutParams  vParams = new   LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,getActualWidthOnThisDevice(30));
        v.setLayoutParams(vParams);
        v.setBackgroundColor(getColor(R.color.common_light_gray));
        lastMoneyLayout.addView(v);
        lastMoneyLayout.addView(rlTipsContainer);

    }

    @Override
    protected void setPropertyOfControls() {

        elvShoppingListView.setGroupIndicator(null);
        tvTotal.setTextColor(getColor(R.color.common_wanted_red));
        tvTotalCurrency.setTextColor(getColor(R.color.common_wanted_red));
        tvTotalValue.setTextColor(getColor(R.color.common_wanted_red));

        tvConfirm.setGravity(Gravity.CENTER);
        tvConfirm.setTextColor(getColor(R.color.common_white));
        tvConfirm.setBackgroundResource(R.drawable.bg_green_btn);

        tvName.setTextColor(getColor(R.color.common_blue));
        tvConfirm.setTextSize(Constants.FONT_SIZE_LARGER);
        tvName.setTextSize(Constants.FONT_SIZE_MORE_LARGER);
        tvNameValue.setTextColor(getColor(R.color.common_black));

        tvSignature.setTextColor(getColor(R.color.common_black));
//        tvTip.setTextColor(getColor(R.color.common_black));
        //tvTipValue.setTextColor(getColor(R.color.common_gray));

        tvPaymentPattern.setTextColor(getColor(R.color.common_black));
        tvPaymentPatternValue.setTextColor(getColor(R.color.common_black));

        imSignature.setImageResource(R.drawable.icon_right_arrow);
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getString(R.string.play_refund));
        getTvLeftTitle().setId(View.generateViewId());
    }

    @Override
    protected void executeOnceOnCreate() {
        netLinkDetailRecord();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            payId = bundle.getString(ApiKey.PAY_ID);
            date = bundle.getString(TransKey.SELECTED_DATE);
            totalCost = bundle.getString(TransKey.TOTAL_COST);
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * get detailRecord data.
     */

    private void netLinkDetailRecord() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.PAY_ID, payId);


        HttpManager<JsonPurchaseHistoryDetailRecord> hh = new HttpManager<JsonPurchaseHistoryDetailRecord>(PlayerRefundFragment.this) {

            @Override
            public void onJsonSuccess(JsonPurchaseHistoryDetailRecord jo) {
                dataParameter = jo;
                addGoodListView();
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.PurchaseDetail, params);
    }

    /**
     * get detailRecord data.
     */

    private void netLinkDRefund() {

        List<ParamRefundExpandable> param = new ArrayList<>();
        Map<Integer, PurchaseRefundExpandable> map = adapter.getCheckedItem();
        for (Map.Entry<Integer, PurchaseRefundExpandable> entry : map.entrySet()) {
            PurchaseRefundExpandable temp = entry.getValue();

//            if (temp.getType() == RefundDetailAdapter.TYPE_GOOD) {
            ParamRefundExpandable paramRefundExpandable = new ParamRefundExpandable();
            paramRefundExpandable.setId(temp.getId());
            paramRefundExpandable.setCount(String.valueOf(temp.getCount()));
            paramRefundExpandable.setTotal_price(temp.getPrice());
            param.add(paramRefundExpandable);
//
//            } else {
//                if (temp.getGoodList() != null && temp.getGoodList().size() > 0) {
//
//                    for (int i = 0; i < temp.getGoodList().size(); i++) {
//                        ParamRefundExpandable paramRefundExpandable = new ParamRefundExpandable();
//                        PurchaseRefundExpandable.GoodListItem tempGood = temp.getGoodList().get(i);
//                        paramRefundExpandable.setId(tempGood.getId());
//                        paramRefundExpandable.setCount(String.valueOf(tempGood.getCount()));
//                        paramRefundExpandable.setTotal_price(tempGood.getPrice());
//                        param.add(paramRefundExpandable);
//                    }
//
//                }
//
//            }


        }
        //list to json string.

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonValue = null;
        try {
            jsonValue = objectMapper.writeValueAsString(param);
        } catch (Exception e) {
            Log.e(Constants.APP_TAG, e.getMessage());
        }

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.REFUND_LIST, jsonValue);

        params.put(ApiKey.REFUND_PAYMENT, payment);

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(PlayerRefundFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                if (jo.getReturnCode() == Constants.RETURN_CODE_20301) {
                    doBack();
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.Refund, params);
    }

    private boolean doCheck() {


        boolean res = true;
        if (Utils.isStringNotNullOrEmpty(subtotal)) {
            double totalRefund = 0;
            Map<Integer, PurchaseRefundExpandable> map = adapter.getCheckedItem();
            for (Map.Entry<Integer, PurchaseRefundExpandable> entry : map.entrySet()) {
                PurchaseRefundExpandable temp = entry.getValue();
                totalRefund += Double.valueOf(temp.getPrice());
            }

            if (totalRefund > Double.valueOf(subtotal)) {
                res = false;
                Utils.showShortToast(getActivity(), R.string.msg_refunds_can_not_exceed_the_total_amount);
            }
        }
        return res;
    }

    /**
     * 单击事件监听器
     */

    public interface OnDateSelectClickListener {
        void OnGoodItemClick(String flag, String content);
    }

}
