/**
 * Project Name: itee
 * File Name:	 ShoppingPaymentSucceededFragment.java
 * Package Name: cn.situne.itee.fragment.shopping
 * Date:		 2015-03-06
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */


package cn.situne.itee.fragment.shopping;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.adapter.PurchaseHistoryAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.PurchaseHistoryExpandable;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonPurchaseHistoryDetailRecord;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.SignaturePopupWindow;


/**
 * ClassName:ShoppingPaymentSucceededFragment <br/>
 * Function: 商品 支付 成功<br/>
 * UI:  06-2-3
 * Date: 2015-03-06 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */

public class ShoppingPaymentSucceededFragment extends BaseFragment {
    private LinearLayout historyListLayout;
    private RelativeLayout rlSignatureContainer;
    private RelativeLayout rlSendMailContainer;
    private RelativeLayout rlSendSMSContainer;
    private RelativeLayout rlSubTotalContainer;
    private RelativeLayout rlTaxContainer;
    private RelativeLayout rlDiscountContainer;
    private RelativeLayout rlMoneyContainer;
    private RelativeLayout rlDepositContainer;
    private RelativeLayout rlTotalContainer;
    private IteeTextView tvSignature;
    private ImageView imSignature;
    private IteeTextView tvSendEmail;
    private ImageView imSendEmail;
    private IteeTextView tvSendSms;
    private ImageView imSendSms;
    private IteeTextView tvSubTotal;
    private IteeTextView tvSubTotalCurrency;
    private IteeTextView tvSubTotalValue;
    private IteeTextView tvTax;
    private IteeTextView tvTaxCurrency;
    private IteeTextView tvTaxValue;
    private IteeTextView tvDiscount;
    private IteeTextView tvDiscountCurrency;
    private IteeTextView tvDiscountValue;
    private IteeTextView tvMoneyValue;
    private IteeTextView tvDeposit;
    private IteeTextView tvDepositCurrency;
    private IteeTextView tvDepositValue;
    private IteeTextView tvTotal;
    private IteeTextView tvTotalCurrency;
    private IteeTextView tvTotalValue;
    private String payId;
    private String dateTime;
    private JsonPurchaseHistoryDetailRecord dataParameter;
    private LinearLayout llRefundListView;

    private int purchaseFlag;
    private String fromPagePayment;

    private int bookingFlag;


    private ArrayList<HistoryItemData> historyItemDataArrayList;
    private String bookingTime;


    private LinearLayout lastMoneyLayout;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_shopping_payment_succeeded;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.do_edit_administration;
    }
    private HistoryItemData findHistoryItemData(String player, String bookingNo) {
        for (HistoryItemData item : historyItemDataArrayList) {
            if (player.equals(item.getPlayer()) && bookingNo.equals(item.getBookingNo())) {
                return item;
            }
        }
        return null;
    }


    private HistoryItemData findHistoryItemData(String bookingNo) {
        for (HistoryItemData item : historyItemDataArrayList) {
            if (bookingNo.equals(item.getBookingNo())) {
                return item;
            }
        }
        return null;
    }

    private void initHistoryItemData() {
        historyItemDataArrayList.clear();
        JsonPurchaseHistoryDetailRecord.DataList dataList = dataParameter.getDataList();
        for (JsonPurchaseHistoryDetailRecord.GoodListItem goodListItem : dataList.getGoodsList()) {
            String player = goodListItem.getPlayer();
            String bookingNo = goodListItem.getBkNo();
            HistoryItemData historyItemData = findHistoryItemData(player, bookingNo);
            ArrayList<JsonPurchaseHistoryDetailRecord.GoodListItem> goodsList;
            if (historyItemData == null) {
                historyItemData = new HistoryItemData();
                goodsList = new ArrayList<>();
                historyItemData.setGoodsList(goodsList);
                historyItemDataArrayList.add(historyItemData);
            } else {
                goodsList = historyItemData.getGoodsList();
                if (goodsList == null) {
                    goodsList = new ArrayList<>();
                    historyItemData.setGoodsList(goodsList);
                }

            }
            historyItemData.setPlayer(player);
            historyItemData.setBookingNo(bookingNo);
            goodsList.add(goodListItem);


        }


        for (JsonPurchaseHistoryDetailRecord.PricingData pricingData:dataList.getPricingDataList()){
            HistoryItemData historyItemData = findHistoryItemData(pricingData.getBookingNo());
            ArrayList<JsonPurchaseHistoryDetailRecord.PricingData> pricingList;
            String player = pricingData.getPlayer();
            if (historyItemData == null) {
                historyItemData = new HistoryItemData();
                pricingList = new ArrayList<>();
                historyItemData.setPricingDataList(pricingList);
                historyItemDataArrayList.add(historyItemData);

            }else {

                pricingList = historyItemData.getPricingDataList();
                if (pricingList == null) {
                    pricingList = new ArrayList<>();
                    historyItemData.setPricingDataList(pricingList);
                }

            }

            historyItemData.setPlayer(player);
            historyItemData.setBookingNo(pricingData.getBookingNo());
            pricingList.add(pricingData);
        }


        for (JsonPurchaseHistoryDetailRecord.PackageListItem packageItem : dataList.getPackageList()) {
            String player = packageItem.getPlayer();
            String bookingNo = packageItem.getBkNo();
            HistoryItemData historyItemData = findHistoryItemData(player, bookingNo);

            ArrayList<JsonPurchaseHistoryDetailRecord.PackageListItem> packageList;
            if (historyItemData == null) {
                historyItemData = new HistoryItemData();
                packageList = new ArrayList<>();
                historyItemData.setPackageList(packageList);
                historyItemDataArrayList.add(historyItemData);
            } else {
                packageList = historyItemData.getPackageList();
                if (packageList == null) {
                    packageList = new ArrayList<>();
                    historyItemData.setPackageList(packageList);
                }

            }
            historyItemData.setPlayer(player);
            historyItemData.setBookingNo(bookingNo);
            packageList.add(packageItem);
        }

        for (JsonPurchaseHistoryDetailRecord.AAListItem aaItem : dataList.getAaList()) {
            String player = aaItem.getPlayer();
            String bookingNo = aaItem.getBkNo();
            HistoryItemData historyItemData = findHistoryItemData(player, bookingNo);
            ArrayList<JsonPurchaseHistoryDetailRecord.AAListItem> aaList;
            if (historyItemData == null) {
                historyItemData = new HistoryItemData();
                aaList = new ArrayList<>();
                historyItemData.setAaList(aaList);
                historyItemDataArrayList.add(historyItemData);
            } else {
                aaList = historyItemData.getAaList();
                if (aaList == null) {
                    aaList = new ArrayList<>();
                    historyItemData.setAaList(aaList);
                }
            }
            historyItemData.setPlayer(player);
            historyItemData.setBookingNo(bookingNo);
            aaList.add(aaItem);
        }
    }
    //1 goods 2 package child  , 99 voucher
    private RelativeLayout getHistoryItemLayout(String name, String price, String count, int type) {
        LinearLayout.LayoutParams goodsLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams goodsNameParams = new RelativeLayout.LayoutParams((int) (Utils.getWidth(getBaseActivity()) * 0.7f), (int) (Utils.getHeight(getBaseActivity()) * 0.08f));
        goodsNameParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        goodsNameParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        goodsNameParams.setMargins(getActualWidthOnThisDevice(40), 0, 0, 0);
        if (type == 2) {
            goodsNameParams.setMargins(getActualWidthOnThisDevice(60), 0, 0, 0);
        }
        RelativeLayout.LayoutParams goodsPriceParams = new RelativeLayout.LayoutParams((int) (Utils.getWidth(getBaseActivity()) * 0.2f), (int) (Utils.getHeight(getBaseActivity()) * 0.04f));
        goodsPriceParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

        goodsPriceParams.setMargins(0, 0, getActualWidthOnThisDevice(40), 0);
        RelativeLayout goodsLayout = new RelativeLayout(getBaseActivity());
        if (type != 2)
        AppUtils.addTopSeparatorLine(goodsLayout, this);

        IteeTextView goodsName = new IteeTextView(getBaseActivity());
        IteeTextView goodsPrice = new IteeTextView(getBaseActivity());
        goodsPrice.setId(View.generateViewId());

        RelativeLayout.LayoutParams goodsCountParams = new RelativeLayout.LayoutParams((int) (Utils.getWidth(getBaseActivity()) * 0.4f), (int) (Utils.getHeight(getBaseActivity()) * 0.04f));

        goodsCountParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        goodsCountParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        goodsCountParams.addRule(RelativeLayout.BELOW, goodsPrice.getId());
        goodsCountParams.setMargins(0, 0, getActualWidthOnThisDevice(40), 0);

        IteeTextView goodsCount = new IteeTextView(getBaseActivity());

        goodsLayout.setLayoutParams(goodsLayoutParams);
        goodsName.setLayoutParams(goodsNameParams);
        goodsPrice.setLayoutParams(goodsPriceParams);
        goodsCount.setLayoutParams(goodsCountParams);




        goodsLayout.addView(goodsName);
        goodsLayout.addView(goodsPrice);
        goodsLayout.addView(goodsCount);
        goodsName.setText(name);
        goodsPrice.setText(AppUtils.getCurrentCurrency(getBaseActivity()) + Utils.get2DigitDecimalString(price));
        goodsPrice.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        goodsCount.setText(Constants.STR_MULTIPLY + count);
        goodsCount.setGravity(Gravity.END);
        if (Constants.STR_EMPTY.equals(count)) {
            goodsPriceParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            goodsCount.setVisibility(View.GONE);
        } else {
            goodsPriceParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);

        }

        goodsCount.setTextColor(getResources().getColor(R.color.common_blue));
        goodsCount.setTextSize(Constants.FONT_SIZE_SMALLER);

        if (type == 3){
            goodsCount.setVisibility(View.GONE);
            goodsPrice.setVisibility(View.GONE);

        }

        if (type == 2) {
            goodsLayout.setBackgroundColor(getColor(R.color.common_light_gray));
        }

        if (type == 99){

            goodsLayout.setBackgroundColor(getColor(R.color.common_light_gray));
            goodsPrice.setText(AppUtils.getCurrentCurrency(getBaseActivity()) + "-" + Utils.get2DigitDecimalString(price));
        }


        return goodsLayout;
    }

    private LinearLayout getHistoryItem(HistoryItemData itemData) {
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout row = new LinearLayout(getBaseActivity());
        row.setLayoutParams(rowParams);
        row.setOrientation(LinearLayout.VERTICAL);
        row.addView(AppUtils.getSeparatorLine(this));

        //player
        LinearLayout.LayoutParams playerLayoutParams
                = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout playerLayout = new RelativeLayout(getBaseActivity());
        playerLayout.setLayoutParams(playerLayoutParams);
        playerLayout.setBackgroundColor(getColor(R.color.common_white));


        RelativeLayout.LayoutParams tvNameParams
                = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, getActualHeightOnThisDevice(80));
        tvNameParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvNameParams.setMargins(getActualWidthOnThisDevice(40), 0, 0, 0);
        IteeTextView tvName = new IteeTextView(getBaseActivity());
        tvName.setLayoutParams(tvNameParams);
        tvName.setText(itemData.getPlayer());

        tvName.setTextColor(getColor(R.color.common_blue));
        tvName.setTextSize(Constants.FONT_SIZE_LARGER);
        //booking


        LinearLayout.LayoutParams bookingLayoutParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(45));
        RelativeLayout bookingLayout = new RelativeLayout(getBaseActivity());
        bookingLayout.setLayoutParams(bookingLayoutParams);

        RelativeLayout.LayoutParams tvBookingNoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvBookingNoParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvBookingNoParams.setMargins(getActualWidthOnThisDevice(40), 0, 0, 0);
        IteeTextView tvBookingNo = new IteeTextView(getBaseActivity());
        tvBookingNo.setLayoutParams(tvBookingNoParams);
        tvBookingNo.setText(itemData.getBookingNo());


        RelativeLayout.LayoutParams tvBookingNoValueParams
                = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvBookingNoValueParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        tvBookingNoValueParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvBookingNoValueParams.rightMargin = getActualWidthOnThisDevice(40);
        IteeTextView tvBookingNoValue = new IteeTextView(getBaseActivity());
        tvBookingNoValue.setLayoutParams(tvBookingNoValueParams);
        tvBookingNoValue.setText(bookingTime);
        tvBookingNo.setTextColor(getColor(R.color.common_black));
        tvBookingNoValue.setTextColor(getColor(R.color.common_black));

        playerLayout.addView(tvName);
        bookingLayout.addView(tvBookingNo);
        bookingLayout.addView(tvBookingNoValue);
        row.addView(playerLayout);
        row.addView(bookingLayout);

        if (itemData.getGoodsList() != null) {
            for (JsonPurchaseHistoryDetailRecord.GoodListItem goodItem : itemData.getGoodsList()) {
                row.addView(getHistoryItemLayout(goodItem.getName(), goodItem.getPrice(), String.valueOf(goodItem.getCount()), 1));
                for (JsonPurchaseHistoryDetailRecord.VoucherItem voucherItem: goodItem.getVoucherItems()){
                    row.addView(getHistoryItemLayout(getString(R.string.shopping_voucher), voucherItem.getVoucherMoney(), Constants.STR_EMPTY, 99));
                }

            }

        }


        if (itemData.getAaList() != null) {
            for (JsonPurchaseHistoryDetailRecord.AAListItem aaItem : itemData.getAaList()) {

                row.addView(getHistoryItemLayout(getString(R.string.shopping_aa_with) + aaItem.getName(), aaItem.getAmount(), Constants.STR_EMPTY, 1));
                row.addView(AppUtils.getSeparatorLine(getBaseActivity()));
                row.addView(AppUtils.getSeparatorLine(getBaseActivity()));
                for (JsonPurchaseHistoryDetailRecord.GoodListItem goodItem : aaItem.getGoodsList()) {
                    row.addView(getHistoryItemLayout(goodItem.getName(), goodItem.getPrice(), String.valueOf(goodItem.getCount()), 2));

                }
            }


        }

        if (itemData.getPackageList() != null) {
            for (JsonPurchaseHistoryDetailRecord.PackageListItem packItem : itemData.getPackageList()) {
                row.addView(getHistoryItemLayout(packItem.getName(), packItem.getAmount(), String.valueOf(packItem.getCount()), 1));
                row.addView(AppUtils.getSeparatorLine(getBaseActivity()));
                row.addView(AppUtils.getSeparatorLine(getBaseActivity()));
                for (JsonPurchaseHistoryDetailRecord.GoodListItem goodItem : packItem.getGoodsList()) {
                    row.addView(getHistoryItemLayout(goodItem.getName(), goodItem.getPrice(), String.valueOf(goodItem.getCount()), 2));

                }
            }

        }


        if (itemData.getPricingDataList() != null&&itemData.getPricingDataList().size()>0) {
            RelativeLayout pricingTitle = getHistoryItemLayout(getString(R.string.pricing_deduct) + itemData.getPricingDataList().get(0).getPricingTimes(), "1", "1", 3);
            pricingTitle.setBackgroundColor(getColor(R.color.common_white));
            row.addView(pricingTitle);
            row.addView(AppUtils.getSeparatorLine(getBaseActivity()));

            for (JsonPurchaseHistoryDetailRecord.PricingData pricingData : itemData.getPricingDataList()) {

                RelativeLayout pricingItemTitle = getHistoryItemLayout(pricingData.getProductName(), pricingData.getDiscountPrice(), pricingData.getQty(), 1);

                pricingItemTitle.setBackgroundColor(getColor(R.color.common_white));
                row.addView(pricingItemTitle);

                row.addView(AppUtils.getSeparatorLine(getBaseActivity()));

                if (pricingData.getProductList()!=null){

                    for (JsonPurchaseHistoryDetailRecord.PricingData packagePricing : pricingData.getProductList()) {


                        row.addView(getHistoryItemLayout(packagePricing.getProductName(), packagePricing.getDiscountPrice(), pricingData.getQty(), 2));

                    }
                }

            }

        }

        return row;
    }

    /**
     * addGoodListView:add all good's view.
     */

    private String getShowMoneyText(int resId,String money){
        return getString(resId)+Constants.STR_COLON+AppUtils.getCurrentCurrency(getBaseActivity())+money;

    }
    private void addGoodListView() {

        JsonPurchaseHistoryDetailRecord.DataList dataList = dataParameter.getDataList();
        Date historyDateObj = DateUtils.getDateFromAPIYearMonthDayHourMonthSecond(dateTime);
        bookingTime = DateUtils.getTimeHourMinuteSecond(historyDateObj);
        initHistoryItemData();
        for (HistoryItemData itemData : historyItemDataArrayList) {
            historyListLayout.addView(getHistoryItem(itemData));
        }
        String currency = AppUtils.getCurrentCurrency(mContext);
        tvSubTotalCurrency.setText(currency);
        tvTaxCurrency.setText(currency);
        tvDiscountCurrency.setText(currency);
        tvDepositCurrency.setText(currency);
        tvTotalCurrency.setText(currency);
        tvSubTotalValue.setText(Constants.STR_SPACE + dataList.getSubtotal());
        tvTaxValue.setText(Constants.STR_SPACE + dataList.getTax());
        tvDiscountValue.setText(Constants.STR_SPACE + dataList.getDiscount());
        tvMoneyValue.setText("");

        String deposit = dataList.getDeposit();
        String total = dataList.getTotal();
        String usedDeposit = Constants.STR_0;
        if (Utils.isStringNotNullOrEmpty(dataList.getDeposit())) {
            if (Double.valueOf(deposit) >= Double.valueOf(total)) {
                usedDeposit = Utils.get2DigitDecimalString(total);
            } else {
                usedDeposit = Utils.get2DigitDecimalString(deposit);
            }
        }

        tvDepositValue.setText(Constants.STR_SPACE + usedDeposit);
        tvTotalValue.setText(dataList.getTotal());

        if (StringUtils.isEmpty(usedDeposit) || Constants.STR_0.equals(usedDeposit)) {
            rlDepositContainer.setVisibility(View.GONE);
        }

        //last Money layout
        //voucher
        RelativeLayout rlShowVoucher = new RelativeLayout(getBaseActivity());




        IteeTextView tvShowVoucher = new IteeTextView(getBaseActivity());
        rlShowVoucher.addView(tvShowVoucher);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvShowVoucher, getActualWidthOnThisDevice(40), getBaseActivity());

        double dSumBigVoucher = 0;
        double dSumVoucher = 0;

        try {
            dSumBigVoucher = Double.parseDouble(dataList.getSumBigVoucher());
        }catch (NumberFormatException e){
            dSumBigVoucher = 0;
        }
        try {
            dSumVoucher = Double.parseDouble(dataList.getSumVoucher());
        }catch (NumberFormatException e){
            dSumVoucher = 0;
        }
        double dVoucher = dSumBigVoucher + dSumVoucher;
        tvShowVoucher.setText(getShowMoneyText(R.string.shopping_voucher, "-"+Utils.get2DigitDecimalString(String.valueOf(dVoucher))));
        //discount
        RelativeLayout rlShowDisCount = new RelativeLayout(getBaseActivity());
        IteeTextView tvShowDisCount = new IteeTextView(getBaseActivity());
        rlShowDisCount.addView(tvShowDisCount);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvShowDisCount, getActualWidthOnThisDevice(40), getBaseActivity());
        tvShowDisCount.setText(getShowMoneyText(R.string.shopping_discount, "-"+dataList.getDiscount()));

        //subtotal
        RelativeLayout rlShowSubtotal = new RelativeLayout(getBaseActivity());
        IteeTextView tvShowSubtotal = new IteeTextView(getBaseActivity());
        rlShowSubtotal.addView(tvShowSubtotal);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvShowSubtotal, getActualWidthOnThisDevice(40), getBaseActivity());
        tvShowSubtotal.setText(getShowMoneyText(R.string.shopping_subtotal, dataList.getSubtotal()));

        //tax
        RelativeLayout rlShowTax = new RelativeLayout(getBaseActivity());
        IteeTextView tvShowTax = new IteeTextView(getBaseActivity());
        rlShowTax.addView(tvShowTax);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvShowTax, getActualWidthOnThisDevice(40), getBaseActivity());
        tvShowTax.setText(getShowMoneyText(R.string.shopping_tax, dataList.getTax()));
        //balance
        RelativeLayout rlShowBalance = new RelativeLayout(getBaseActivity());
        IteeTextView tvShowBalance = new IteeTextView(getBaseActivity());
        rlShowBalance.addView(tvShowBalance);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvShowBalance, getActualWidthOnThisDevice(40), getBaseActivity());
        tvShowBalance.setText(getShowMoneyText(R.string.shopping_balance, "-"+dataList.getBalanceAccountMoney()));

        //deposit
        RelativeLayout rlShowDeposit = new RelativeLayout(getBaseActivity());
        IteeTextView tvShowDeposit = new IteeTextView(getBaseActivity());
        rlShowDeposit.addView(tvShowDeposit);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvShowDeposit, getActualWidthOnThisDevice(40), getBaseActivity());
        tvShowDeposit.setText(getShowMoneyText(R.string.shopping_deposit, "-"+dataList.getDeposit()));


        lastMoneyLayout.addView(rlShowVoucher);
        lastMoneyLayout.addView(rlShowDisCount);
        lastMoneyLayout.addView(rlShowSubtotal);
        lastMoneyLayout.addView(rlShowTax);
        lastMoneyLayout.addView(rlShowBalance);
        lastMoneyLayout.addView(rlShowDeposit);
        if (!AppUtils.isTaxExcludeGoods(getActivity())) {
            rlShowTax.setVisibility(View.GONE);
        }

        if (dVoucher <= 0){
            rlShowVoucher.setVisibility(View.GONE);
        }

        if (Constants.STR_0.equals(dataList.getDiscount())){
            rlShowDisCount.setVisibility(View.GONE);
        }

        if (Constants.STR_0.equals(dataList.getBalanceAccountMoney())){
            rlShowBalance.setVisibility(View.GONE);
        }
        if (Constants.STR_0.equals(dataList.getDeposit())){
            rlShowDeposit.setVisibility(View.GONE);
        }

        if (!Constants.STR_0.equals(dataList.getDepositBnc())){

            if (purchaseFlag == ShoppingPaymentFragment.PURCHASE_FLAG_CHECKOUT) {
                tvAvailableDeposit.setText(MessageFormat.format(getString(R.string.available_deposit_check_out_mes),dataList.getDepositBnc()));

            }else{

                tvAvailableDeposit.setText(MessageFormat.format(getString(R.string.available_deposit_pay_mes),dataList.getDepositBnc()));
            }
        }else{
            tvAvailableDeposit.setGravity(View.GONE);

        }



        List<JsonPurchaseHistoryDetailRecord.GoodListItem> goodListItems = dataParameter.getDataList().getGoodsList();
        List<JsonPurchaseHistoryDetailRecord.AAListItem> aaListItems = dataParameter.getDataList().getAaList();
        List<JsonPurchaseHistoryDetailRecord.PackageListItem> packageListItems
                = dataParameter.getDataList().getPackageList();

        if (goodListItems != null && goodListItems.size() > 0) {
            for (int i = 0; i < goodListItems.size(); i++) {
                JsonPurchaseHistoryDetailRecord.GoodListItem fromGoodListItem = goodListItems.get(i);
                PurchaseHistoryExpandable toGoodListItem = new PurchaseHistoryExpandable();
                toGoodListItem.setName(fromGoodListItem.getName());
                toGoodListItem.setCount(fromGoodListItem.getCount());
                toGoodListItem.setPrice(fromGoodListItem.getPrice());
                toGoodListItem.setType(PurchaseHistoryAdapter.TYPE_GOOD);
            }
        }

        if (aaListItems != null && aaListItems.size() > 0) {
            for (int i = 0; i < aaListItems.size(); i++) {
                JsonPurchaseHistoryDetailRecord.AAListItem fromGoodListItem = aaListItems.get(i);

                List<JsonPurchaseHistoryDetailRecord.GoodListItem> fromGoodListItemChild = fromGoodListItem.getGoodsList();
                PurchaseHistoryExpandable toGoodListItem = new PurchaseHistoryExpandable();
                toGoodListItem.setName(fromGoodListItem.getName());
                toGoodListItem.setPrice(fromGoodListItem.getAmount());
                toGoodListItem.setType(PurchaseHistoryAdapter.TYPE_AA);
                List<JsonPurchaseHistoryDetailRecord.GoodListItem> toGoodListItemChild = new ArrayList<>();
                for (int j = 0; j < fromGoodListItemChild.size(); j++) {
                    toGoodListItemChild.add(fromGoodListItemChild.get(j));
                }
                toGoodListItem.setGoodList(toGoodListItemChild);
            }
        }

        if (packageListItems != null && packageListItems.size() > 0) {
            for (int i = 0; i < packageListItems.size(); i++) {
                JsonPurchaseHistoryDetailRecord.PackageListItem fromGoodListItem = packageListItems.get(i);

                List<JsonPurchaseHistoryDetailRecord.GoodListItem> fromGoodListItemChild = fromGoodListItem.getGoodsList();
                PurchaseHistoryExpandable toGoodListItem = new PurchaseHistoryExpandable();
                toGoodListItem.setName(fromGoodListItem.getName());
                toGoodListItem.setPrice(fromGoodListItem.getAmount());
                toGoodListItem.setId(fromGoodListItem.getId());
                toGoodListItem.setType(PurchaseHistoryAdapter.TYPE_PACKAGE);
                toGoodListItem.setCount(Integer.parseInt(fromGoodListItem.getCount()));
                List<JsonPurchaseHistoryDetailRecord.GoodListItem> toGoodListItemChild = new ArrayList<>();
                for (int j = 0; j < fromGoodListItemChild.size(); j++) {
                    toGoodListItemChild.add(fromGoodListItemChild.get(j));
                }
                toGoodListItem.setGoodList(toGoodListItemChild);
            }
        }
    }

    @Override
    protected void initControls(View rootView) {
        Bundle bundle = getArguments();
        historyItemDataArrayList = new ArrayList<>();
        if (bundle != null) {
            payId = bundle.getString(ApiKey.PAY_ID);


            dateTime = bundle.getString(TransKey.COMMON_DATE_TIME,Constants.STR_EMPTY);
            purchaseFlag = bundle.getInt(TransKey.SHOPPING_PURCHASE_FLAG);
            fromPagePayment = bundle.getString(TransKey.COMMON_FROM_PAGE, fromPagePayment);

            bookingFlag = bundle.getInt(TransKey.SHOPPING_BOOKING_FLAG);

            bundle.putInt(TransKey.SHOPPING_PURCHASE_FLAG, purchaseFlag);
        }

        historyListLayout = (LinearLayout) rootView.findViewById(R.id.historyListLayout);

        llRefundListView = (LinearLayout) rootView.findViewById(R.id.ll_play_purchase_history_refund_list);
        rlSubTotalContainer = (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_sub_total);
        rlTaxContainer = (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_tax);

        if (!AppUtils.isTaxExcludeGoods(getActivity())) {

            rlTaxContainer.setVisibility(View.GONE);
        }

        rlDiscountContainer = (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_discount);

        rlMoneyContainer = (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_money);
        rlDepositContainer = (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_deposit);
        rlTotalContainer = (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_discount_total);

        rlSignatureContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_purchase_history_signature);
        rlSendMailContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_purchase_history_send_email);
        rlSendSMSContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_purchase_history_send_sms);

        lastMoneyLayout= (LinearLayout) rootView.findViewById(R.id.lastMoneyLayout);

        tvSignature = new IteeTextView(getActivity());
        imSignature = new ImageView(getActivity());

        tvSendEmail = new IteeTextView(getActivity());
        imSendEmail = new ImageView(getActivity());

        tvSendSms = new IteeTextView(getActivity());
        imSendSms = new ImageView(getActivity());


        tvSubTotal = new IteeTextView(getActivity());
        tvSubTotalCurrency = new IteeTextView(getActivity());
        tvSubTotalValue = new IteeTextView(getActivity());

        tvTax = new IteeTextView(getActivity());
        tvTaxCurrency = new IteeTextView(getActivity());
        tvTaxValue = new IteeTextView(getActivity());

        tvDiscount = new IteeTextView(getActivity());
        tvDiscountCurrency = new IteeTextView(getActivity());
        tvDiscountValue = new IteeTextView(getActivity());

        tvMoneyValue = new IteeTextView(getActivity());


        tvDeposit = new IteeTextView(getActivity());
        tvDepositCurrency = new IteeTextView(getActivity());
        tvDepositValue = new IteeTextView(getActivity());

        tvTotal = new IteeTextView(getActivity());
        tvTotalCurrency = new IteeTextView(getActivity());
        tvTotalValue = new IteeTextView(getActivity());

        tvTotal.setTextSize(Constants.FONT_SIZE_LARGER);
        tvTotalCurrency.setTextSize(Constants.FONT_SIZE_LARGER);
        tvTotalValue.setTextSize(Constants.FONT_SIZE_LARGER);
    }

    @Override
    protected void setDefaultValueOfControls() {


//        tvBookingNo.setText(getString(R.string.play_purchase_history_deposit));

        tvSignature.setText(getString(R.string.play_purchase_history_signature));

        tvSendEmail.setText(getString(R.string.play_purchase_history_send_email));

        tvSendSms.setText(getString(R.string.play_purchase_history_send_sms));

    }

    @Override
    protected void setListenersOfControls() {

        tvSendEmail.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {

            }
        });

        rlSignatureContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignaturePopupWindow signaturePopupWindow = new SignaturePopupWindow(getActivity(), null, dataParameter.getDataList()
                        .getSignature());
                signaturePopupWindow.showAtLocation(getRootView(), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });


        rlSendMailContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = dataParameter.getDataList().getEmail();
                String cc = "cc";
                String subject = "Email";
                String content = getEmailString();
                AppUtils.sendEmail(address, subject, cc, content, getActivity());
                Utils.log(content);
            }
        });

        rlSendSMSContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number = dataParameter.getDataList().getPhone();
                String sms_content = dataParameter.getDataList().getMessage();
                if (Utils.isStringNotNullOrEmpty(phone_number)) {
                    SmsManager smsManager = SmsManager.getDefault();
                    if (sms_content.length() > 70) {
                        List<String> contents = smsManager.divideMessage(sms_content);
                        for (String sms : contents) {
                            smsManager.sendTextMessage(phone_number, null, sms, null, null);
                        }
                    } else {
                        smsManager.sendTextMessage(phone_number, null, getSmsString(), null, null);
                    }
                    Utils.showShortToast(getActivity(), R.string.msg_send_success);
                } else {
                    AppUtils.openSMS(getSmsString(), getActivity());
                }
            }
        });
    }

    private String getSmsString() {
        StringBuilder message = new StringBuilder();
        message.append(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(dataParameter.getDataList().getPayTime(), mContext))
                .append(Constants.STR_LINE_BREAK);

        if (StringUtils.isNotEmpty(dataParameter.getDataList().getMemberName())) {
            message.append(dataParameter.getDataList().getMemberName())
                    .append(Constants.STR_SPACE);
        }

        message.append(dataParameter.getDataList().getBookingNo())
                .append(Constants.STR_LINE_BREAK);

        message.append(getMessageMoneyContent(false));
        message.append(Constants.STR_LINE_BREAK);

        return message.toString();
    }

    private String getMessageMoneyContent(boolean isEmail) {

        String lineBreak = isEmail ? Constants.STR_HTML_LINE_BREAK : Constants.STR_LINE_BREAK;

        JsonPurchaseHistoryDetailRecord.DataList dataList = dataParameter.getDataList();

        StringBuilder message = new StringBuilder();
        message.append(getString(R.string.shopping_pay_subtotal))
                .append(AppUtils.getCurrentCurrency(getActivity()))
                .append(Constants.STR_SPACE)
                .append(dataList.getSubtotal())
                .append(lineBreak);

        //fix syb
        if (AppUtils.getShowSalesTax(mContext)){
            message.append(getString(R.string.shopping_pay_tax))
                    .append(AppUtils.getCurrentCurrency(getActivity()))
                    .append(Constants.STR_SPACE)
                    .append(dataList.getTax())
                    .append(lineBreak);
        }

        message.append(getString(R.string.shopping_pay_discount))
                .append(AppUtils.getCurrentCurrency(getActivity()))
                .append(Constants.STR_SPACE)
                .append(dataList.getDiscount())
                .append(lineBreak);

        String deposit = dataList.getDeposit();
        String total = dataList.getTotal();
        String usedDeposit = Constants.STR_0;
        if (Utils.isStringNotNullOrEmpty(dataList.getDeposit())) {
            if (Double.valueOf(deposit) >= Double.valueOf(total)) {
                usedDeposit = Utils.get2DigitDecimalString(total);
            } else {
                usedDeposit = Utils.get2DigitDecimalString(deposit);
            }
        }

        message.append(getString(R.string.shopping_pay_deposit))
                .append(AppUtils.getCurrentCurrency(getActivity()))
                .append(Constants.STR_SPACE)
                .append(usedDeposit)
                .append(lineBreak);

        message.append(getString(R.string.shopping_pay_total))
                .append(AppUtils.getCurrentCurrency(getActivity()))
                .append(Constants.STR_SPACE)
                .append(dataList.getTotal());

        return message.toString();
    }
    private String getEmailString() {
        StringBuilder message = new StringBuilder();
        JsonPurchaseHistoryDetailRecord.DataList dataList = dataParameter.getDataList();
        String currentShowDateTime = DateUtils.getCurrentShowYearMonthDayHourMinuteSecondFromApiDateTime(dataList.getPayTime(), mContext);
        message.append(currentShowDateTime).append(Constants.STR_HTML_LINE_BREAK);
        for (HistoryItemData historyItemData:historyItemDataArrayList){

            message.append(historyItemData.getPlayer())
                    .append(Constants.STR_SPACE)
                    .append(historyItemData.getBookingNo())
                    .append(Constants.STR_HTML_LINE_BREAK);

            List<JsonPurchaseHistoryDetailRecord.GoodListItem> goodListItems = historyItemData.getGoodsList();
            List<JsonPurchaseHistoryDetailRecord.AAListItem> aaListItems = historyItemData.getAaList();
            List<JsonPurchaseHistoryDetailRecord.PackageListItem> packageListItems = historyItemData.getPackageList();
            if (goodListItems != null && goodListItems.size() > 0) {
                for (int i = 0; i < goodListItems.size(); i++) {
                    JsonPurchaseHistoryDetailRecord.GoodListItem fromGoodListItem = goodListItems.get(i);
                    message.append(fromGoodListItem.getName())
                            .append(Constants.STR_SPACE)
                            .append(fromGoodListItem.getPrice())
                            .append(Constants.STR_SPACE)
                            .append(Constants.STR_MULTIPLY)
                            .append(fromGoodListItem.getCount())
                            .append(Constants.STR_HTML_LINE_BREAK);
                }
            }


            if (aaListItems != null && aaListItems.size() > 0) {
                for (int i = 0; i < aaListItems.size(); i++) {
                    JsonPurchaseHistoryDetailRecord.AAListItem fromGoodListItem = aaListItems.get(i);
                    List<JsonPurchaseHistoryDetailRecord.GoodListItem> fromGoodListItemChild = fromGoodListItem.getGoodsList();
                    message.append(fromGoodListItem.getName())
                            .append(Constants.STR_SPACE)
                            .append(fromGoodListItem.getAmount())
                            .append(Constants.STR_HTML_LINE_BREAK);
                    for (int j = 0; j < fromGoodListItemChild.size(); j++) {
                        message.append("&nbsp; &nbsp; &nbsp;")
                                .append(fromGoodListItemChild.get(j).getName())
                                .append(Constants.STR_SPACE)
                                .append(fromGoodListItemChild.get(j).getPrice())
                                .append(Constants.STR_SPACE)
                                .append(Constants.STR_MULTIPLY)
                                .append(fromGoodListItemChild.get(j).getCount())
                                .append(Constants.STR_HTML_LINE_BREAK);
                    }
                }
            }

            if (packageListItems != null && packageListItems.size() > 0) {
                for (int i = 0; i < packageListItems.size(); i++) {
                    JsonPurchaseHistoryDetailRecord.PackageListItem fromGoodListItem = packageListItems.get(i);
                    List<JsonPurchaseHistoryDetailRecord.GoodListItem> fromGoodListItemChild = fromGoodListItem.getGoodsList();
                    message.append(fromGoodListItem.getName())
                            .append(Constants.STR_SPACE)
                            .append(fromGoodListItem.getAmount())
                            .append(Constants.STR_SPACE)
                            .append(Constants.STR_MULTIPLY)
                            .append(fromGoodListItem.getCount())
                            .append(Constants.STR_HTML_LINE_BREAK);
                    for (int j = 0; j < fromGoodListItemChild.size(); j++) {
                        message.append("&nbsp; &nbsp; &nbsp;")
                                .append(fromGoodListItemChild.get(j).getName())
                                .append(Constants.STR_SPACE)
                                .append(fromGoodListItemChild.get(j).getPrice())
                                .append(Constants.STR_SPACE)
                                .append(Constants.STR_MULTIPLY)
                                .append(fromGoodListItemChild.get(j).getCount())
                                .append(Constants.STR_HTML_LINE_BREAK);
                    }
                }
            }
        }



        message.append(Constants.STR_HTML_LINE_BREAK)
                .append(Constants.STR_HTML_LINE_BREAK);

        message.append(getString(R.string.player_info_purchase_refund))
                .append(Constants.STR_HTML_LINE_BREAK)
                .append(dataList.getRefundTime())
                .append(Constants.STR_HTML_LINE_BREAK);

        List<JsonPurchaseHistoryDetailRecord.RefundListItem> refundListItems = dataList.getRefundList();
        if (refundListItems != null && refundListItems.size() > 0) {
            for (int i = 0; i < refundListItems.size(); i++) {
                JsonPurchaseHistoryDetailRecord.RefundListItem fromGoodListItem = refundListItems.get(i);
                message.append(fromGoodListItem.getName())
                        .append(Constants.STR_SPACE)
                        .append(fromGoodListItem.getPrice())
                        .append(Constants.STR_SPACE)
                        .append(Constants.STR_MULTIPLY)
                        .append(fromGoodListItem.getCount())
                        .append(Constants.STR_HTML_LINE_BREAK);
            }
        }
        return message.toString();
    }

    @Override
    protected void setLayoutOfControls() {

        LinearLayout.LayoutParams paramsDetail = (LinearLayout.LayoutParams) rlSubTotalContainer.getLayoutParams();
        paramsDetail.height = getActualHeightOnThisDevice(40);

        rlSubTotalContainer.setLayoutParams(paramsDetail);
        rlTaxContainer.setLayoutParams(paramsDetail);
        rlDiscountContainer.setLayoutParams(paramsDetail);
        paramsDetail.height = getActualHeightOnThisDevice(60);
        rlDepositContainer.setLayoutParams(paramsDetail);

        rlSubTotalContainer.setGravity(Gravity.CENTER_VERTICAL);
        rlTaxContainer.setGravity(Gravity.CENTER_VERTICAL);
        rlDiscountContainer.setGravity(Gravity.CENTER_VERTICAL);
        rlDepositContainer.setGravity(Gravity.CENTER_VERTICAL);

        int padding = getActualWidthOnThisDevice(20);
        rlSubTotalContainer.setPadding(padding, 0, padding, 0);
        rlTaxContainer.setPadding(padding, 0, padding, 0);
        rlDiscountContainer.setPadding(padding, 0, padding, 0);
        rlDepositContainer.setPadding(padding, 0, padding, 0);
        rlTotalContainer.setPadding(padding, 0, padding, 0);

        LinearLayout.LayoutParams rlPhotoLayout = new LinearLayout.LayoutParams(  LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT);
        rlPhotoLayout.height = getActualHeightOnThisDevice(80);


        LinearLayout.LayoutParams rlOtherLayout = (LinearLayout.LayoutParams) rlSignatureContainer.getLayoutParams();
        rlOtherLayout.height = (int) (getScreenHeight() * 0.08f);

        rlTotalContainer.setLayoutParams(rlPhotoLayout);
//        rlNameContainer.setLayoutParams(rlPhotoLayout);
//        rlDepositTitleContainer.setLayoutParams(rlPhotoLayout);
        rlSignatureContainer.setLayoutParams(rlOtherLayout);
        rlSendMailContainer.setLayoutParams(rlOtherLayout);
        rlSendSMSContainer.setLayoutParams(rlOtherLayout);

        rlTotalContainer.setBackgroundColor(getColor(R.color.common_white));
        rlSignatureContainer.setBackgroundColor(getColor(R.color.common_white));
        rlSendMailContainer.setBackgroundColor(getColor(R.color.common_white));
        rlSendSMSContainer.setBackgroundColor(getColor(R.color.common_white));
        rlSubTotalContainer.setBackgroundColor(getColor(R.color.common_white));
        rlTaxContainer.setBackgroundColor(getColor(R.color.common_white));
        rlDiscountContainer.setBackgroundColor(getColor(R.color.common_white));
        rlDepositContainer.setBackgroundColor(getColor(R.color.common_white));
        rlTotalContainer.setBackgroundColor(getColor(R.color.common_white));



        rlSignatureContainer.addView(tvSignature);
        RelativeLayout.LayoutParams paramsOccupation = (RelativeLayout.LayoutParams) tvSignature.getLayoutParams();
        paramsOccupation.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsOccupation.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsOccupation.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsOccupation.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsOccupation.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        tvSignature.setLayoutParams(paramsOccupation);


        rlSignatureContainer.addView(imSignature);
        LayoutUtils.setRightArrow(imSignature, mContext);

        rlSendMailContainer.addView(tvSendEmail);
        RelativeLayout.LayoutParams paramsPrePay = (RelativeLayout.LayoutParams) tvSendEmail.getLayoutParams();
        paramsPrePay.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsPrePay.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsPrePay.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsPrePay.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsPrePay.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        tvSendEmail.setLayoutParams(paramsPrePay);


        rlSendMailContainer.addView(imSendEmail);
        LayoutUtils.setRightArrow(imSendEmail, mContext);

        rlSendSMSContainer.addView(tvSendSms);
        RelativeLayout.LayoutParams paramsBirth = (RelativeLayout.LayoutParams) tvSendSms.getLayoutParams();
        paramsBirth.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBirth.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBirth.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsBirth.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        tvSendSms.setLayoutParams(paramsBirth);


        rlSendSMSContainer.addView(imSendSms);
        LayoutUtils.setRightArrow(imSendSms, mContext);

        rlSubTotalContainer.addView(tvSubTotalValue);

        RelativeLayout.LayoutParams paramSubTotalValue = (RelativeLayout.LayoutParams) tvSubTotalValue.getLayoutParams();
        paramSubTotalValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramSubTotalValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramSubTotalValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramSubTotalValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvSubTotalValue.setLayoutParams(paramSubTotalValue);
        tvSubTotalValue.setId(View.generateViewId());

        rlSubTotalContainer.addView(tvSubTotalCurrency);
        RelativeLayout.LayoutParams paramSubTotalCurrency = (RelativeLayout.LayoutParams) tvSubTotalCurrency.getLayoutParams();
        paramSubTotalCurrency.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramSubTotalCurrency.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramSubTotalCurrency.addRule(RelativeLayout.LEFT_OF, tvSubTotalValue.getId());
        paramSubTotalCurrency.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvSubTotalCurrency.setLayoutParams(paramSubTotalCurrency);
        tvSubTotalCurrency.setId(View.generateViewId());

        rlSubTotalContainer.addView(tvSubTotal);
        RelativeLayout.LayoutParams paramSubTotaln = (RelativeLayout.LayoutParams) tvSubTotal.getLayoutParams();
        paramSubTotaln.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramSubTotaln.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramSubTotaln.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramSubTotaln.addRule(RelativeLayout.LEFT_OF, tvSubTotalCurrency.getId());
        tvSubTotal.setLayoutParams(paramSubTotaln);


        rlTaxContainer.addView(tvTaxValue);

        RelativeLayout.LayoutParams paramTaxValue = (RelativeLayout.LayoutParams) tvTaxValue.getLayoutParams();
        paramTaxValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTaxValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTaxValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramTaxValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvTaxValue.setLayoutParams(paramTaxValue);
        tvTaxValue.setId(View.generateViewId());

        rlTaxContainer.addView(tvTaxCurrency);
        RelativeLayout.LayoutParams paramTaxCurrency = (RelativeLayout.LayoutParams) tvTaxCurrency.getLayoutParams();
        paramTaxCurrency.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTaxCurrency.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTaxCurrency.addRule(RelativeLayout.LEFT_OF, tvTaxValue.getId());
        paramTaxCurrency.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvTaxCurrency.setLayoutParams(paramTaxCurrency);
        tvTaxCurrency.setId(View.generateViewId());

        rlTaxContainer.addView(tvTax);
        RelativeLayout.LayoutParams paramTax = (RelativeLayout.LayoutParams) tvTax.getLayoutParams();
        paramTax.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTax.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTax.addRule(RelativeLayout.LEFT_OF, tvTaxCurrency.getId());
        paramTax.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvTax.setLayoutParams(paramTax);


        rlDiscountContainer.addView(tvDiscountValue);

        RelativeLayout.LayoutParams paramDiscountValue = (RelativeLayout.LayoutParams) tvDiscountValue.getLayoutParams();
        paramDiscountValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramDiscountValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramDiscountValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramDiscountValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvDiscountValue.setLayoutParams(paramDiscountValue);
        tvDiscountValue.setId(View.generateViewId());

        rlDiscountContainer.addView(tvDiscountCurrency);
        RelativeLayout.LayoutParams paramDiscountCurrency = (RelativeLayout.LayoutParams) tvDiscountCurrency.getLayoutParams();
        paramDiscountCurrency.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramDiscountCurrency.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramDiscountCurrency.addRule(RelativeLayout.LEFT_OF, tvDiscountValue.getId());

        paramDiscountCurrency.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvDiscountCurrency.setLayoutParams(paramDiscountCurrency);
        tvDiscountCurrency.setId(View.generateViewId());

        rlDiscountContainer.addView(tvDiscount);
        RelativeLayout.LayoutParams paramDiscount = (RelativeLayout.LayoutParams) tvDiscount.getLayoutParams();
        paramDiscount.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramDiscount.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramDiscount.addRule(RelativeLayout.LEFT_OF, tvDiscountCurrency.getId());
        paramDiscount.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvDiscount.setLayoutParams(paramDiscount);


        rlMoneyContainer.addView(tvMoneyValue);

        RelativeLayout.LayoutParams tvMoneyValueParams = new  RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvMoneyValueParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        tvMoneyValueParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvMoneyValue.setLayoutParams(tvMoneyValueParams);
        rlMoneyContainer.setVisibility(View.GONE);



        rlDepositContainer.addView(tvDepositValue);

        RelativeLayout.LayoutParams paramDepositValue = (RelativeLayout.LayoutParams) tvDepositValue.getLayoutParams();
        paramDepositValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramDepositValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramDepositValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramDepositValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvDepositValue.setLayoutParams(paramDepositValue);
        tvDepositValue.setId(View.generateViewId());

        rlDepositContainer.addView(tvDepositCurrency);
        RelativeLayout.LayoutParams paramDepositCurrency = (RelativeLayout.LayoutParams) tvDepositCurrency.getLayoutParams();
        paramDepositCurrency.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramDepositCurrency.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramDepositCurrency.addRule(RelativeLayout.LEFT_OF, tvDepositValue.getId());
        paramDepositCurrency.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvDepositCurrency.setLayoutParams(paramDepositCurrency);
        tvDepositCurrency.setId(View.generateViewId());

        rlDepositContainer.addView(tvDeposit);
        RelativeLayout.LayoutParams paramDeposit = (RelativeLayout.LayoutParams) tvDeposit.getLayoutParams();
        paramDeposit.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramDeposit.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramDeposit.addRule(RelativeLayout.LEFT_OF, tvDepositCurrency.getId());
        paramDeposit.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvDeposit.setLayoutParams(paramDeposit);


        rlTotalContainer.addView(tvTotalValue);

        RelativeLayout.LayoutParams paramTotalValue = (RelativeLayout.LayoutParams) tvTotalValue.getLayoutParams();
        paramTotalValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTotalValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTotalValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramTotalValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramTotalValue.rightMargin = getActualWidthOnThisDevice(20);
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
        tvTotal.setId(View.generateViewId());




        tvAvailableDeposit = new IteeTextView(getBaseActivity());
        RelativeLayout.LayoutParams  tvAvailableDepositParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        tvAvailableDepositParams.addRule(RelativeLayout.LEFT_OF,tvTotal.getId());
        tvAvailableDepositParams.leftMargin = getActualWidthOnThisDevice(2);
        tvAvailableDeposit.setTextSize(Constants.FONT_SIZE_SMALLER);
        tvAvailableDeposit.setTextColor(getColor(R.color.common_gray));

        tvAvailableDeposit.setLayoutParams(tvAvailableDepositParams);
        rlTotalContainer.addView(tvAvailableDeposit);
    }

    private IteeTextView tvAvailableDeposit;
    @Override
    protected void setPropertyOfControls() {

        llRefundListView.setVisibility(View.GONE);
        tvSignature.setTextColor(getColor(R.color.common_black));

        tvSendEmail.setTextColor(getColor(R.color.common_black));

        tvSendSms.setTextColor(getColor(R.color.common_black));
        imSignature.setImageResource(R.drawable.icon_right_arrow);
        imSendEmail.setImageResource(R.drawable.icon_right_arrow);
        imSendSms.setImageResource(R.drawable.icon_right_arrow);

        tvSubTotal.setText(getString(R.string.shopping_pay_subtotal));

        tvTax.setText(getString(R.string.shopping_pay_tax));
        tvDiscount.setText(getString(R.string.shopping_pay_discount));

        tvDeposit.setText(R.string.shopping_used_deposit);

        tvTotal.setText(getString(R.string.shopping_pay_total));
        tvTotal.setTextColor(getColor(R.color.common_wanted_red));

        tvTotalCurrency.setTextColor(getColor(R.color.common_wanted_red));
        tvTotalValue.setTextColor(getColor(R.color.common_wanted_red));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getString(R.string.shopping_payment_succeeded));

        RelativeLayout.LayoutParams paramsTvLeftTitle = (RelativeLayout.LayoutParams) getTvLeftTitle().getLayoutParams();
        paramsTvLeftTitle.width = (int) (getScreenWidth() * 0.7);
        paramsTvLeftTitle.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        getTvLeftTitle().setLayoutParams(paramsTvLeftTitle);
        getTvLeftTitle().setId(View.generateViewId());

        setOnBackListener(new OnPopBackListener() {
            @Override
            public boolean preDoBack() {
                setOnBackListener(null);
                doBack();
                Bundle bundle = new Bundle();
                bundle.putString(TransKey.COMMON_FROM_PAGE, ShoppingPaymentSucceededFragment.class.getName());
                if (bookingFlag == Constants.SHOPS_BOOKING_FLAG_2) {
                    doBack();
                    doBackWithReturnValue(bundle, ShoppingGoodsListFragment.class);
                } else {
                    if (purchaseFlag == ShoppingPaymentFragment.PURCHASE_FLAG_CHECKOUT) {
                        try {
                            doBack();
                            doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPagePayment));
                        } catch (ClassNotFoundException e) {
                            Utils.log(e.getMessage());
                        }
                    } else {
                        doBackWithReturnValue(bundle, ShoppingPaymentFragment.class);
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void executeOnceOnCreate() {
        netLinkDetailRecord();
    }

    /**
     * get detailRecord data.
     */

    private void netLinkDetailRecord() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.PAY_ID, payId);


        HttpManager<JsonPurchaseHistoryDetailRecord> hh = new HttpManager<JsonPurchaseHistoryDetailRecord>(ShoppingPaymentSucceededFragment.this) {

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
     * 单击事件监听器
     */
    public interface OnDateSelectClickListener {
        void OnGoodItemClick(String flag, String content);
    }


    class HistoryItemData {
        private String player;
        private String bookingNo;
        private ArrayList<JsonPurchaseHistoryDetailRecord.GoodListItem> goodsList;
        private ArrayList<JsonPurchaseHistoryDetailRecord.AAListItem> aaList;
        private ArrayList<JsonPurchaseHistoryDetailRecord.PackageListItem> packageList;

        private ArrayList<JsonPurchaseHistoryDetailRecord.PricingData> pricingDataList;

        public ArrayList<JsonPurchaseHistoryDetailRecord.PricingData> getPricingDataList() {
            return pricingDataList;
        }

        public void setPricingDataList(ArrayList<JsonPurchaseHistoryDetailRecord.PricingData> pricingDataList) {
            this.pricingDataList = pricingDataList;
        }

        public String getPlayer() {
            return player;
        }

        public void setPlayer(String player) {
            this.player = player;
        }

        public String getBookingNo() {
            return bookingNo;
        }

        public void setBookingNo(String bookingNo) {
            this.bookingNo = bookingNo;
        }


        public ArrayList<JsonPurchaseHistoryDetailRecord.GoodListItem> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(ArrayList<JsonPurchaseHistoryDetailRecord.GoodListItem> goodsList) {
            this.goodsList = goodsList;
        }

        public ArrayList<JsonPurchaseHistoryDetailRecord.AAListItem> getAaList() {
            return aaList;
        }

        public void setAaList(ArrayList<JsonPurchaseHistoryDetailRecord.AAListItem> aaList) {
            this.aaList = aaList;
        }

        public ArrayList<JsonPurchaseHistoryDetailRecord.PackageListItem> getPackageList() {
            return packageList;
        }

        public void setPackageList(ArrayList<JsonPurchaseHistoryDetailRecord.PackageListItem> packageList) {
            this.packageList = packageList;
        }
    }
}
