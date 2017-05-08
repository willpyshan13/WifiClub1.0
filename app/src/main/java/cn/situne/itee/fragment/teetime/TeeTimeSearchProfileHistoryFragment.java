package cn.situne.itee.fragment.teetime;

import android.content.Context;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.adapter.PurchaseHistoryAdapter;
import cn.situne.itee.adapter.PurchaseHistoryRefundAdapter;
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
import cn.situne.itee.manager.jsonentity.JsonSearchProfileHistoryData;
import cn.situne.itee.manager.jsonentity.JsonSearchProfileHistoryData;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.SignaturePopupWindow;

/**
 * Created by luochao on 12/1/15.
 */
public class TeeTimeSearchProfileHistoryFragment extends BaseFragment {
    private LinearLayout historyListLayout;

    private LinearLayout refundListLayout;

    private RelativeLayout rlSignatureContainer;
    private RelativeLayout rlSendMailContainer;
    private RelativeLayout rlSendSMSContainer;

    private String  selectDate;
//    private RelativeLayout rlSubTotalContainer;
//    private RelativeLayout rlTaxContainer;
//    private RelativeLayout rlDiscountContainer;
//    private RelativeLayout rlDepositContainer;
//    private RelativeLayout rlTotalContainer;

    private IteeTextView tvSignature;
    private ImageView imSignature;

    private IteeTextView tvSendEmail;
    private ImageView imSendEmail;
    private IteeTextView tvSendSms;
    private ImageView imSendSms;
    private String payId;
    private String dateTime;
    private ExpandableListView elvRefundListView;
    private LinearLayout llRefundListViewContainer;
    private ArrayList<HistoryItemData> historyItemDataArrayList;
    private String bookingTime;



    private List<JsonSearchProfileHistoryData.DataItem> dataList;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_search_booking_member;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.do_edit_administration;
    }

    /**
     * addGoodListView:add all good's view.
     */

    private final  int SHORT_LINE_WIDTH = 650;
//1 goods 2 package child 3 pricing
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
//        if (type != 2){
//            View line = AppUtils.getSeparatorLine(getBaseActivity());
//            line.bringToFront();
//
//            goodsLayout.addView(line);
//
//            RelativeLayout.LayoutParams lineParams = (RelativeLayout.LayoutParams )line.getLayoutParams();
//            lineParams.width = getActualWidthOnThisDevice(SHORT_LINE_WIDTH);
//            lineParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//
//        }

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

        if (type == 3){
            goodsPrice.setVisibility(View.GONE);
            goodsLayout.setBackgroundColor(getColor(R.color.common_white));
            goodsCount.setVisibility(View.GONE);
        }
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

        if (type == 2) {
           // goodsLayout.setBackgroundColor(getColor(R.color.common_gray));
        }

        if (type == 99) {

            //goodsLayout.setBackgroundColor(getColor(R.color.common_light_gray));
            goodsPrice.setText(AppUtils.getCurrentCurrency(getBaseActivity()) + "-" + Utils.get2DigitDecimalString(price));
        }
        return goodsLayout;
    }

    private LinearLayout getHistoryItem(HistoryItemData itemData) {
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout row = new LinearLayout(getBaseActivity());
        row.setLayoutParams(rowParams);
        row.setOrientation(LinearLayout.VERTICAL);

//        row.addView(AppUtils.getSeparatorView(this.getBaseActivity(), getActualWidthOnThisDevice(600), getActualHeightOnThisDevice(1)));

        LinearLayout.LayoutParams bookingLayoutParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(80));
        RelativeLayout bookingLayout = new RelativeLayout(getBaseActivity());
        bookingLayout.setLayoutParams(bookingLayoutParams);

        RelativeLayout.LayoutParams tvBookingNoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvBookingNoParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvBookingNoParams.setMargins(getActualWidthOnThisDevice(40), 0, 0, 0);
        IteeTextView tvBookingNo = new IteeTextView(getBaseActivity());
        tvBookingNo.setLayoutParams(tvBookingNoParams);
        tvBookingNo.setText(bookingTime);


        RelativeLayout.LayoutParams tvBookingNoValueParams
                = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvBookingNoValueParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        tvBookingNoValueParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvBookingNoValueParams.rightMargin = getActualWidthOnThisDevice(40);

       bookingLayout.addView(tvBookingNo);



        View line = AppUtils.getSeparatorLine(getBaseActivity());
        bookingLayout.addView(line);

        RelativeLayout.LayoutParams lineParams = (RelativeLayout.LayoutParams)line.getLayoutParams();
        lineParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lineParams.width = getActualWidthOnThisDevice(SHORT_LINE_WIDTH);

        row.addView(bookingLayout);

        if (itemData.getGoodsList() != null) {
            for (JsonSearchProfileHistoryData.GoodListItem goodItem : itemData.getGoodsList()) {
                row.addView(getHistoryItemLayout(goodItem.getName(), goodItem.getPrice(), String.valueOf(goodItem.getCount()), 1));

                for (JsonSearchProfileHistoryData.VoucherItem voucherItem : goodItem.getVoucherItems()) {

                    row.addView(getHistoryItemLayout(getString(R.string.shopping_voucher), voucherItem.getVoucherMoney(), Constants.STR_EMPTY, 99));


                }
            }

        }


        if (itemData.getAaList() != null) {
            for (JsonSearchProfileHistoryData.AAListItem aaItem : itemData.getAaList()) {

                row.addView(getHistoryItemLayout(getString(R.string.shopping_aa_with) + aaItem.getName(), aaItem.getAmount(), Constants.STR_EMPTY, 1));
//                row.addView(AppUtils.getSeparatorView(this.getBaseActivity(), getActualWidthOnThisDevice(600), getActualHeightOnThisDevice(1)));
//                row.addView(AppUtils.getSeparatorView(this.getBaseActivity(),getActualWidthOnThisDevice(600),getActualHeightOnThisDevice(1)));
                for (JsonSearchProfileHistoryData.GoodListItem goodItem : aaItem.getGoodsList()) {
                    row.addView(getHistoryItemLayout(goodItem.getName(), goodItem.getPrice(), String.valueOf(goodItem.getCount()), 2));

                }
            }


        }

        if (itemData.getPackageList() != null) {
            for (JsonSearchProfileHistoryData.PackageListItem packItem : itemData.getPackageList()) {
                row.addView(getHistoryItemLayout(packItem.getName(), packItem.getAmount(), String.valueOf(packItem.getCount()), 1));
//                row.addView(AppUtils.getSeparatorView(this.getBaseActivity(), getActualWidthOnThisDevice(600), getActualHeightOnThisDevice(1)));
//                row.addView(AppUtils.getSeparatorView(this.getBaseActivity(),getActualWidthOnThisDevice(600),getActualHeightOnThisDevice(1)));
                for (JsonSearchProfileHistoryData.GoodListItem goodItem : packItem.getGoodsList()) {
                    row.addView(getHistoryItemLayout(goodItem.getName(), goodItem.getPrice(), String.valueOf(goodItem.getCount()), 2));

                }
            }

        }

        if (itemData.getPricingDataList() != null &&itemData.getPricingDataList().size()>0) {
            RelativeLayout pricingTitle = getHistoryItemLayout(getString(R.string.pricing_deduct) + itemData.getPricingDataList().get(0).getPricingTimes(), "1", "1", 3);
            pricingTitle.setBackgroundColor(getColor(R.color.common_gray));
            row.addView(pricingTitle);
//            row.addView(AppUtils.getSeparatorView(this.getBaseActivity(), getActualWidthOnThisDevice(600), getActualHeightOnThisDevice(1)));
//            row.addView(AppUtils.getSeparatorView(this.getBaseActivity(),getActualWidthOnThisDevice(600),getActualHeightOnThisDevice(1)));
            for (JsonSearchProfileHistoryData.PricingData pricingData : itemData.getPricingDataList()) {
                RelativeLayout pricingItemTitle = getHistoryItemLayout(pricingData.getProductName(), pricingData.getDiscountPrice(), pricingData.getQty(), 1);

                pricingItemTitle.setBackgroundColor(getColor(R.color.common_gray));
                row.addView(pricingItemTitle);

//                row.addView(AppUtils.getSeparatorLine(getBaseActivity()));
//                row.addView(AppUtils.getSeparatorLine(getBaseActivity()));

                if (pricingData.getProductList()!=null){

                    for (JsonSearchProfileHistoryData.PricingData packagePricing : pricingData.getProductList()) {


                        row.addView(getHistoryItemLayout(packagePricing.getProductName(), packagePricing.getDiscountPrice(), pricingData.getQty(), 2));

                    }
                }

            }

        }


        return row;
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

    private void initHistoryItemData(JsonSearchProfileHistoryData.DataItem dataItem) {
        historyItemDataArrayList.clear();
        for (JsonSearchProfileHistoryData.GoodListItem goodListItem : dataItem.getGoodsList()) {
            String player = goodListItem.getPlayer();
            String bookingNo = goodListItem.getBkNo();
            HistoryItemData historyItemData = findHistoryItemData(player, bookingNo);
            ArrayList<JsonSearchProfileHistoryData.GoodListItem> goodsList;
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



        for (JsonSearchProfileHistoryData.PricingData pricingData:dataItem.getPricingDataList()){
            HistoryItemData historyItemData = findHistoryItemData(pricingData.getBookingNo());
            ArrayList<JsonSearchProfileHistoryData.PricingData> pricingList;
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

        for (JsonSearchProfileHistoryData.PackageListItem packageItem : dataItem.getPackageList()) {
            String player = packageItem.getPlayer();
            String bookingNo = packageItem.getBkNo();
            HistoryItemData historyItemData = findHistoryItemData(player, bookingNo);

            ArrayList<JsonSearchProfileHistoryData.PackageListItem> packageList;
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

        for (JsonSearchProfileHistoryData.AAListItem aaItem : dataItem.getAaList()) {
            String player = aaItem.getPlayer();
            String bookingNo = aaItem.getBkNo();
            HistoryItemData historyItemData = findHistoryItemData(player, bookingNo);
            ArrayList<JsonSearchProfileHistoryData.AAListItem> aaList;
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

    private String getShowMoneyText(int resId, String money) {
        return getString(resId) + Constants.STR_COLON + AppUtils.getCurrentCurrency(getBaseActivity()) + money;

    }

    private void addGoodListView(JsonSearchProfileHistoryData.DataItem dataItem) {

        Date historyDateObj = DateUtils.getDateFromAPIYearMonthDayHourMonthSecond(dateTime);
        bookingTime = DateUtils.getTimeHourMinuteSecond(historyDateObj);
        initHistoryItemData(dataItem);

        historyListLayout.addView(AppUtils.getSeparatorLine(getBaseActivity()));
        for (HistoryItemData itemData : historyItemDataArrayList) {
            historyListLayout.addView(getHistoryItem(itemData));
        }


          View line1 = AppUtils.getSeparatorLine(getBaseActivity());
        line1.bringToFront();

            historyListLayout.addView(line1);

            LinearLayout.LayoutParams line1Params = (LinearLayout.LayoutParams )line1.getLayoutParams();
        line1Params.width = getActualWidthOnThisDevice(SHORT_LINE_WIDTH);
           // lineParams.addRule(RelativeLayout.CENTER_HORIZONTAL);




        String currency = AppUtils.getCurrentCurrency(mContext);
//        tvSubTotalCurrency.setText(currency);
//        tvTaxCurrency.setText(currency);
//        tvDiscountCurrency.setText(currency);
//        tvDepositCurrency.setText(currency);
//       // tvTotalCurrency.setText(currency);
//        tvSubTotalValue.setText(dataItem.getSubtotal());
//        tvTaxValue.setText(dataItem.getTax());
//        tvDiscountValue.setText(dataItem.getDiscount());

        String deposit = dataItem.getDeposit();
        String subtotal = dataItem.getSubtotal();
        String usedDeposit = Constants.STR_0;
        if (Utils.isStringNotNullOrEmpty(dataItem.getDeposit())) {
            if (Double.valueOf(deposit) >= Double.valueOf(subtotal)) {
                usedDeposit = Utils.get2DigitDecimalString(subtotal);
            } else {
                usedDeposit = Utils.get2DigitDecimalString(deposit);
            }
        }

       // tvDepositValue.setText(usedDeposit);

        IteeTextView tvTotalValue = new IteeTextView(getBaseActivity());
        tvTotalValue.setText(getString(R.string.shopping_pay_total)+currency+dataItem.getTotal());
        tvTotalValue.setTextColor(getColor(R.color.common_red));


        //last Money layout
        //voucher
        RelativeLayout rlShowVoucher = new RelativeLayout(getBaseActivity());


        IteeTextView tvShowVoucher = new IteeTextView(getBaseActivity());
        rlShowVoucher.addView(tvShowVoucher);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvShowVoucher, getActualWidthOnThisDevice(45), getBaseActivity());

        double dSumBigVoucher = 0;
        double dSumVoucher = 0;

        try {
            dSumBigVoucher = Double.parseDouble(dataItem.getSumBigVoucher());
        } catch (NumberFormatException e) {
            dSumBigVoucher = 0;
        }
        try {
            dSumVoucher = Double.parseDouble(dataItem.getSumVoucher());
        } catch (NumberFormatException e) {
            dSumVoucher = 0;
        }
        double dVoucher = dSumBigVoucher + dSumVoucher;
        tvShowVoucher.setText(getShowMoneyText(R.string.shopping_voucher, "-" + Utils.get2DigitDecimalString(String.valueOf(dVoucher))));
        //discount
        RelativeLayout rlShowDisCount = new RelativeLayout(getBaseActivity());
        IteeTextView tvShowDisCount = new IteeTextView(getBaseActivity());
        rlShowDisCount.addView(tvShowDisCount);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvShowDisCount, getActualWidthOnThisDevice(45), getBaseActivity());
        tvShowDisCount.setText(getShowMoneyText(R.string.shopping_discount, "-" + dataItem.getDiscount()));

        //subtotal
        RelativeLayout rlShowSubtotal = new RelativeLayout(getBaseActivity());
        IteeTextView tvShowSubtotal = new IteeTextView(getBaseActivity());
        rlShowSubtotal.addView(tvShowSubtotal);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvShowSubtotal, getActualWidthOnThisDevice(45), getBaseActivity());
        tvShowSubtotal.setText(getShowMoneyText(R.string.shopping_subtotal, dataItem.getSubtotal()));

        //tax
        RelativeLayout rlShowTax = new RelativeLayout(getBaseActivity());
        IteeTextView tvShowTax = new IteeTextView(getBaseActivity());
        rlShowTax.addView(tvShowTax);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvShowTax, getActualWidthOnThisDevice(45), getBaseActivity());
        tvShowTax.setText(getShowMoneyText(R.string.shopping_tax, dataItem.getTax()));
        //balance
        RelativeLayout rlShowBalance = new RelativeLayout(getBaseActivity());
        IteeTextView tvShowBalance = new IteeTextView(getBaseActivity());
        rlShowBalance.addView(tvShowBalance);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvShowBalance, getActualWidthOnThisDevice(45), getBaseActivity());
        tvShowBalance.setText(getShowMoneyText(R.string.shopping_balance, "-" + dataItem.getBalanceAccountMoney()));

        //deposit
        RelativeLayout rlShowDeposit = new RelativeLayout(getBaseActivity());
        IteeTextView tvShowDeposit = new IteeTextView(getBaseActivity());
        rlShowDeposit.addView(tvShowDeposit);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvShowDeposit, getActualWidthOnThisDevice(45), getBaseActivity());
        tvShowDeposit.setText(getShowMoneyText(R.string.shopping_deposit, "-" + dataItem.getDeposit()));

      //  historyListLayout.addView(AppUtils.getSeparatorLine(getBaseActivity()));
        historyListLayout.addView(rlShowVoucher);
        historyListLayout.addView(rlShowDisCount);
        historyListLayout.addView(rlShowSubtotal);
        historyListLayout.addView(rlShowTax);
        historyListLayout.addView(rlShowBalance);
        historyListLayout.addView(rlShowDeposit);
        View line2 = AppUtils.getSeparatorLine(getBaseActivity());
        line2.bringToFront();

        historyListLayout.addView(line2);

        LinearLayout.LayoutParams line2Params = (LinearLayout.LayoutParams )line2.getLayoutParams();
        line2Params.width = getActualWidthOnThisDevice(SHORT_LINE_WIDTH);

        tvTotalValue.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
        tvTotalValue.setPadding(0, 0, getActualWidthOnThisDevice(40), 0);
        historyListLayout.addView(tvTotalValue);
        tvTotalValue.getLayoutParams().height = getActualHeightOnThisDevice(80);
        if (!AppUtils.isTaxExcludeGoods(getActivity())) {
            rlShowTax.setVisibility(View.GONE);
        }

        if (dVoucher <= 0) {
            rlShowVoucher.setVisibility(View.GONE);
        }

        if (Constants.STR_0.equals(dataItem.getDiscount())) {
            rlShowDisCount.setVisibility(View.GONE);
        }

        if (Constants.STR_0.equals(dataItem.getBalanceAccountMoney())) {
            rlShowBalance.setVisibility(View.GONE);
        }
        if (Constants.STR_0.equals(dataItem.getDeposit())) {
            rlShowDeposit.setVisibility(View.GONE);
        }


//        List<JsonSearchProfileHistoryData.GoodListItem> goodListItems = dataItem.getGoodsList();
//        List<JsonSearchProfileHistoryData.AAListItem> aaListItems = dataItem.getAaList();
//        List<JsonSearchProfileHistoryData.PackageListItem> packageListItems = dataItem.getPackageList();
//
//
//        if (goodListItems != null && goodListItems.size() > 0) {
//            for (int i = 0; i < goodListItems.size(); i++) {
//                JsonSearchProfileHistoryData.GoodListItem fromGoodListItem = goodListItems.get(i);
//                PurchaseHistoryExpandable toGoodListItem = new PurchaseHistoryExpandable();
//                toGoodListItem.setName(fromGoodListItem.getName());
//                toGoodListItem.setCount(fromGoodListItem.getCount());
//                toGoodListItem.setPrice(fromGoodListItem.getPrice());
//                toGoodListItem.setType(PurchaseHistoryAdapter.TYPE_GOOD);
////                dataAdapter.add(toGoodListItem);
//            }
//        }
//
//
//        if (aaListItems != null && aaListItems.size() > 0) {
//            for (int i = 0; i < aaListItems.size(); i++) {
//                JsonSearchProfileHistoryData.AAListItem fromGoodListItem = aaListItems.get(i);
//
//                List<JsonSearchProfileHistoryData.GoodListItem> fromGoodListItemChild = fromGoodListItem.getGoodsList();
//                PurchaseHistoryExpandable toGoodListItem = new PurchaseHistoryExpandable();
//                toGoodListItem.setName(fromGoodListItem.getName());
//                toGoodListItem.setPrice(fromGoodListItem.getAmount());
//                toGoodListItem.setType(PurchaseHistoryAdapter.TYPE_AA);
//                List<JsonSearchProfileHistoryData.GoodListItem> toGoodListItemChild = new ArrayList<>();
//                for (int j = 0; j < fromGoodListItemChild.size(); j++) {
//                    toGoodListItemChild.add(fromGoodListItemChild.get(j));
//                }
//                toGoodListItem.setGoodList(toGoodListItemChild);
////                dataAdapter.add(toGoodListItem);
//            }
//        }
//
//        if (packageListItems != null && packageListItems.size() > 0) {
//            for (int i = 0; i < packageListItems.size(); i++) {
//                JsonSearchProfileHistoryData.PackageListItem fromGoodListItem = packageListItems.get(i);
//
//                List<JsonSearchProfileHistoryData.GoodListItem> fromGoodListItemChild = fromGoodListItem.getGoodsList();
//                PurchaseHistoryExpandable toGoodListItem = new PurchaseHistoryExpandable();
//                toGoodListItem.setName(fromGoodListItem.getName());
//                toGoodListItem.setPrice(fromGoodListItem.getAmount());
//                toGoodListItem.setId(fromGoodListItem.getId());
//                toGoodListItem.setCount(Integer.parseInt(fromGoodListItem.getCount()));
//                toGoodListItem.setType(PurchaseHistoryAdapter.TYPE_PACKAGE);
//                List<JsonSearchProfileHistoryData.GoodListItem> toGoodListItemChild = new ArrayList<>();
//                for (int j = 0; j < fromGoodListItemChild.size(); j++) {
//                    toGoodListItemChild.add(fromGoodListItemChild.get(j));
//                }
//                toGoodListItem.setGoodList(toGoodListItemChild);
////                dataAdapter.add(toGoodListItem);
//            }
//        }
//
////        PurchaseHistoryAdapter adapter = new PurchaseHistoryAdapter(getActivity(), dataAdapter);
////        elvShoppingListView.setAdapter(adapter);
//
//        List<JsonSearchProfileHistoryData.RefundListItem> refundListItems = dataItem.getRefundList();
//        String refundTime = dataItem.getRefundTime();
//
//        List<PurchaseHistoryExpandable> dataRefundAdapter = new ArrayList<>();
//        if (refundListItems != null && refundListItems.size() > 0) {
//
//            PurchaseHistoryExpandable toGoodListItem = new PurchaseHistoryExpandable();
//            toGoodListItem.setName(getString(R.string.play_refund));
//            toGoodListItem.setPrice(refundTime);
//            toGoodListItem.setType(PurchaseHistoryAdapter.TYPE_REFUND);
//
//            List<JsonSearchProfileHistoryData.GoodListItem> toGoodListItemChild = new ArrayList<>();
//            for (int i = 0; i < refundListItems.size(); i++) {
//                JsonSearchProfileHistoryData.RefundListItem fromGoodListItem = refundListItems.get(i);
//
//                JsonSearchProfileHistoryData.GoodListItem toGoodListItem1 = new JsonSearchProfileHistoryData.GoodListItem();
//                toGoodListItem1.setCount(Integer.valueOf(fromGoodListItem.getCount()));
//                toGoodListItem1.setName(fromGoodListItem.getName());
//                toGoodListItem1.setPrice(fromGoodListItem.getPrice());
//                toGoodListItemChild.add(toGoodListItem1);
//            }
//            toGoodListItem.setGoodList(toGoodListItemChild);
//
//            dataRefundAdapter.add(toGoodListItem);
//        }
//
//        PurchaseHistoryRefundAdapter adapterRefund = new PurchaseHistoryRefundAdapter(getActivity(), dataRefundAdapter);
//        elvRefundListView.setAdapter(adapterRefund);
//
//        if (dataRefundAdapter.size() == 0) {
//            llRefundListViewContainer.setVisibility(View.GONE);
//        }
//
//
//        //all expand
//        for (int i = 0; i < dataRefundAdapter.size(); i++) {
//            elvRefundListView.expandGroup(i);
//        }
////        for (int i = 0; i < dataAdapter.size(); i++) {
////            elvShoppingListView.expandGroup(i);
////        }
//
////        LayoutUtils.setListViewHeightBasedOnChildren(elvShoppingListView);
//        LayoutUtils.setListViewHeightBasedOnChildren(elvRefundListView);
//        //can not collapse group
//        elvRefundListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                return true;
//            }
//        });
//        elvShoppingListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                return true;
//            }
//        });
    }

    @Override
    protected void initControls(View rootView) {
        dataList = new ArrayList<>();
        historyItemDataArrayList = new ArrayList<>();

        historyListLayout = (LinearLayout) rootView.findViewById(R.id.historyListLayout);
        historyListLayout.setGravity(Gravity.CENTER);


        refundListLayout= (LinearLayout) rootView.findViewById(R.id.refundListLayout);
        refundListLayout.setGravity(Gravity.CENTER);
        elvRefundListView = (ExpandableListView) rootView.findViewById(R.id.elv_play_purchase_history_refund_list);
        llRefundListViewContainer = (LinearLayout) rootView.findViewById(R.id.ll_play_purchase_history_refund_list);

//        rlSubTotalContainer = (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_sub_total);
//        rlTaxContainer = (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_tax);
//        rlDiscountContainer = (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_discount);
//        rlDepositContainer = (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_deposit);
       // rlTotalContainer = (RelativeLayout) rootView.findViewById(R.id.rl_confirm_pay_discount_total);

        rlSignatureContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_purchase_history_signature);
        rlSendMailContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_purchase_history_send_email);
        rlSendSMSContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_purchase_history_send_sms);

        tvSignature = new IteeTextView(getActivity());
        imSignature = new ImageView(getActivity());

        tvSendEmail = new IteeTextView(getActivity());
        imSendEmail = new ImageView(getActivity());

        tvSendSms = new IteeTextView(getActivity());
        imSendSms = new ImageView(getActivity());


//        tvSubTotal = new IteeTextView(getActivity());
//        tvSubTotalCurrency = new IteeTextView(getActivity());
//        tvSubTotalValue = new IteeTextView(getActivity());
//
//        tvTax = new IteeTextView(getActivity());
//        tvTaxCurrency = new IteeTextView(getActivity());
//        tvTaxValue = new IteeTextView(getActivity());
//
//        tvDiscount = new IteeTextView(getActivity());
//        tvDiscountCurrency = new IteeTextView(getActivity());
//        tvDiscountValue = new IteeTextView(getActivity());
//
//
//        tvDeposit = new IteeTextView(getActivity());
//        tvDepositCurrency = new IteeTextView(getActivity());
//        tvDepositValue = new IteeTextView(getActivity());


//        tvTotal = new IteeTextView(getActivity());
//        tvTotalCurrency = new IteeTextView(getActivity());
//        tvTotalValue = new IteeTextView(getActivity());
//        tvTotalCurrency.setTextColor(getColor(R.color.common_red));

    }

    @Override
    protected void setDefaultValueOfControls() {
        tvSignature.setText(getString(R.string.play_purchase_history_signature));
        tvSendEmail.setText(getString(R.string.play_purchase_history_send_email));
        tvSendSms.setText(getString(R.string.play_purchase_history_send_sms));

    }


    private void addRefundLayout(String time, ArrayList<JsonSearchProfileHistoryData.RefundListItem> itemList){
            refundListLayout.addView(AppUtils.getSeparatorLine(getBaseActivity()));
        RelativeLayout rlTitle = new RelativeLayout(getBaseActivity());

        IteeTextView tvRefund = new IteeTextView(getBaseActivity());
        tvRefund.setText(getString(R.string.play_refund));
        IteeTextView tvRefundTime = new IteeTextView(getBaseActivity());
        tvRefundTime.setText("2014/05/10 :15:10:10");
        tvRefundTime.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        rlTitle.addView(tvRefund);
        rlTitle.addView(tvRefundTime);
        RelativeLayout.LayoutParams tvRefundTimeParams = (RelativeLayout.LayoutParams )tvRefundTime.getLayoutParams();
        tvRefundTimeParams.rightMargin  = getActualWidthOnThisDevice(40);
        tvRefundTimeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tvRefundTimeParams.addRule(RelativeLayout.CENTER_VERTICAL);




        RelativeLayout.LayoutParams tvRefundParams = (RelativeLayout.LayoutParams )tvRefund.getLayoutParams();
        tvRefundParams.leftMargin  = getActualWidthOnThisDevice(40);
        tvRefundParams.addRule(RelativeLayout.CENTER_VERTICAL);

        RelativeLayout rlItem= new RelativeLayout(getBaseActivity());
        View line = AppUtils.getSeparatorLine(getBaseActivity());
        line.bringToFront();
        rlItem.addView(line);

        RelativeLayout.LayoutParams lineParams = ( RelativeLayout.LayoutParams) line.getLayoutParams();
        lineParams.width = getActualWidthOnThisDevice(SHORT_LINE_WIDTH);
        lineParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        IteeTextView tvName = new IteeTextView(getBaseActivity());
        IteeTextView tvNum = new IteeTextView(getBaseActivity());
        IteeTextView tvMoney = new IteeTextView(getBaseActivity());

        rlItem.addView(tvName);
        rlItem.addView(tvNum);
        rlItem.addView(tvMoney);





        refundListLayout.addView(rlTitle);
        refundListLayout.addView(rlItem);

        rlTitle.getLayoutParams().height = getActualHeightOnThisDevice(100);
        rlItem.getLayoutParams().height = getActualHeightOnThisDevice(80);
    }
    private String getEmailString() {
        StringBuilder message = new StringBuilder();
//        JsonSearchProfileHistoryData.DataList dataList = dataParameter.getDataList();
//        String currentShowDateTime = DateUtils.getCurrentShowYearMonthDayHourMinuteSecondFromApiDateTime(dataList.getPayTime(), mContext);
//        message.append(currentShowDateTime).append(Constants.STR_HTML_LINE_BREAK);
//        for (HistoryItemData historyItemData : historyItemDataArrayList) {
//
//            message.append(historyItemData.getPlayer())
//                    .append(Constants.STR_SPACE)
//                    .append(historyItemData.getBookingNo())
//                    .append(Constants.STR_HTML_LINE_BREAK);
//
//            List<JsonSearchProfileHistoryData.GoodListItem> goodListItems = historyItemData.getGoodsList();
//            List<JsonSearchProfileHistoryData.AAListItem> aaListItems = historyItemData.getAaList();
//            List<JsonSearchProfileHistoryData.PackageListItem> packageListItems = historyItemData.getPackageList();
//            if (goodListItems != null && goodListItems.size() > 0) {
//                for (int i = 0; i < goodListItems.size(); i++) {
//                    JsonSearchProfileHistoryData.GoodListItem fromGoodListItem = goodListItems.get(i);
//                    message.append(fromGoodListItem.getName())
//                            .append(Constants.STR_SPACE)
//                            .append(fromGoodListItem.getPrice())
//                            .append(Constants.STR_SPACE)
//                            .append(Constants.STR_MULTIPLY)
//                            .append(fromGoodListItem.getCount())
//                            .append(Constants.STR_HTML_LINE_BREAK);
//                }
//            }
//
//
//            if (aaListItems != null && aaListItems.size() > 0) {
//                for (int i = 0; i < aaListItems.size(); i++) {
//                    JsonSearchProfileHistoryData.AAListItem fromGoodListItem = aaListItems.get(i);
//                    List<JsonSearchProfileHistoryData.GoodListItem> fromGoodListItemChild = fromGoodListItem.getGoodsList();
//                    message.append(fromGoodListItem.getName())
//                            .append(Constants.STR_SPACE)
//                            .append(fromGoodListItem.getAmount())
//                            .append(Constants.STR_HTML_LINE_BREAK);
//                    for (int j = 0; j < fromGoodListItemChild.size(); j++) {
//                        message.append("&nbsp; &nbsp; &nbsp;")
//                                .append(fromGoodListItemChild.get(j).getName())
//                                .append(Constants.STR_SPACE)
//                                .append(fromGoodListItemChild.get(j).getPrice())
//                                .append(Constants.STR_SPACE)
//                                .append(Constants.STR_MULTIPLY)
//                                .append(fromGoodListItemChild.get(j).getCount())
//                                .append(Constants.STR_HTML_LINE_BREAK);
//                    }
//                }
//            }
//
//            if (packageListItems != null && packageListItems.size() > 0) {
//                for (int i = 0; i < packageListItems.size(); i++) {
//                    JsonSearchProfileHistoryData.PackageListItem fromGoodListItem = packageListItems.get(i);
//                    List<JsonSearchProfileHistoryData.GoodListItem> fromGoodListItemChild = fromGoodListItem.getGoodsList();
//                    message.append(fromGoodListItem.getName())
//                            .append(Constants.STR_SPACE)
//                            .append(fromGoodListItem.getAmount())
//                            .append(Constants.STR_SPACE)
//                            .append(Constants.STR_MULTIPLY)
//                            .append(fromGoodListItem.getCount())
//                            .append(Constants.STR_HTML_LINE_BREAK);
////                    ArrayList<JsonSearchProfileHistoryData.GoodListItem> toGoodListItemChild = new ArrayList<>();
//                    for (int j = 0; j < fromGoodListItemChild.size(); j++) {
////                        toGoodListItemChild.add(fromGoodListItemChild.get(j));
//                        message.append("&nbsp; &nbsp; &nbsp;")
//                                .append(fromGoodListItemChild.get(j).getName())
//                                .append(Constants.STR_SPACE)
//                                .append(fromGoodListItemChild.get(j).getPrice())
//                                .append(Constants.STR_SPACE)
//                                .append(Constants.STR_MULTIPLY)
//                                .append(fromGoodListItemChild.get(j).getCount())
//                                .append(Constants.STR_HTML_LINE_BREAK);
//                    }
//                }
//            }
//        }
//
//
//        message.append(Constants.STR_HTML_LINE_BREAK)
//                .append(Constants.STR_HTML_LINE_BREAK);
//
//        message.append(getString(R.string.player_info_purchase_refund))
//                .append(Constants.STR_HTML_LINE_BREAK)
//                .append(dataList.getRefundTime())
//                .append(Constants.STR_HTML_LINE_BREAK);
//
//        List<JsonSearchProfileHistoryData.RefundListItem> refundListItems = dataList.getRefundList();
//        if (refundListItems != null && refundListItems.size() > 0) {
//            for (int i = 0; i < refundListItems.size(); i++) {
//                JsonSearchProfileHistoryData.RefundListItem fromGoodListItem = refundListItems.get(i);
//                message.append(fromGoodListItem.getName())
//                        .append(Constants.STR_SPACE)
//                        .append(fromGoodListItem.getPrice())
//                        .append(Constants.STR_SPACE)
//                        .append(Constants.STR_MULTIPLY)
//                        .append(fromGoodListItem.getCount())
//                        .append(Constants.STR_HTML_LINE_BREAK);
//            }
//        }
        return message.toString();
    }

    @Override
    protected void setListenersOfControls() {


        rlSignatureContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SignaturePopupWindow signaturePopupWindow = new SignaturePopupWindow(getActivity(), null, dataParameter.getDataList().getSignature());
//                signaturePopupWindow.showAtLocation(getRootView(), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });


        rlSendMailContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String address = dataParameter.getDataList().getEmail();
//                String cc = "cc";
//                String subject = "Email";
//                String content = getEmailString();
//                AppUtils.sendEmail(address, subject, cc, content, getActivity());

            }
        });


        rlSendSMSContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String phone_number = dataParameter.getDataList().getPhone();
//                String sms_content = dataParameter.getDataList().getMessage();
//                if (Utils.isStringNullOrEmpty(phone_number)) {
//                    Utils.showShortToast(mContext, getString(R.string.staff_fill_phone_number));
//                }
//                if (Utils.isStringNotNullOrEmpty(phone_number)) {
//                    SmsManager smsManager = SmsManager.getDefault();
//                    if (sms_content.length() > 70) {
//                        List<String> contents = smsManager.divideMessage(sms_content);
//                        for (String sms : contents) {
//                            smsManager.sendTextMessage(phone_number, null, sms, null, null);
//                        }
//                    } else {
//                        smsManager.sendTextMessage(phone_number, null, getSmsString(), null, null);
//                    }
//                } else {
//                    AppUtils.openSMS(getSmsString(), getActivity());
//                }
            }
        });
    }

//    private String getSmsString() {
//        JsonSearchProfileHistoryData.DataList dataList = dataParameter.getDataList();
//
//        StringBuilder message = new StringBuilder();
//
//        message.append(getString(R.string.shopping_pay_subtotal))
//                .append(AppUtils.getCurrentCurrency(getBaseActivity()))
//                .append(Constants.STR_SPACE)
//                .append(dataList.getSubtotal())
//                .append(Constants.STR_LINE_BREAK);
//
//        //fix syb.
//        if (AppUtils.getShowSalesTax(mContext)) {
//
//            message.append(getString(R.string.shopping_pay_tax))
//                    .append(AppUtils.getCurrentCurrency(getBaseActivity()))
//                    .append(Constants.STR_SPACE)
//                    .append(dataList.getTax())
//                    .append(Constants.STR_LINE_BREAK);
//        }
//
//        message.append(getString(R.string.shopping_pay_discount))
//                .append(AppUtils.getCurrentCurrency(getBaseActivity()))
//                .append(Constants.STR_SPACE)
//                .append(dataList.getDiscount())
//                .append(Constants.STR_LINE_BREAK);
//
//        message.append(getString(R.string.shopping_pay_deposit))
//                .append(AppUtils.getCurrentCurrency(getBaseActivity()))
//                .append(Constants.STR_SPACE)
//                .append(dataList.getDeposit())
//                .append(Constants.STR_LINE_BREAK);
//
//        message.append(getString(R.string.shopping_pay_total))
//                .append(AppUtils.getCurrentCurrency(getBaseActivity()))
//                .append(Constants.STR_SPACE)
//                .append(dataList.getTotal())
//                .append(Constants.STR_LINE_BREAK);
//
//        double refundMoney = 0;
//        List<JsonSearchProfileHistoryData.RefundListItem> refundListItems = dataParameter.getDataList().getRefundList();
//        if (refundListItems != null && refundListItems.size() > 0) {
//            for (int i = 0; i < refundListItems.size(); i++) {
//                JsonSearchProfileHistoryData.RefundListItem fromGoodListItem = refundListItems.get(i);
//                refundMoney += Integer.parseInt(fromGoodListItem.getCount()) * Double.parseDouble(fromGoodListItem.getPrice());
//            }
//        }
//
//        message.append(getString(R.string.player_info_purchase_refund))
//                .append(Constants.STR_COLON)
//                .append(AppUtils.getCurrentCurrency(getBaseActivity())).append(Constants.STR_SPACE).append(refundMoney);
//        return message.toString();
//
//
//    }

    @Override
    protected void setLayoutOfControls() {

//        LinearLayout.LayoutParams paramsDetail = (LinearLayout.LayoutParams) rlSubTotalContainer.getLayoutParams();
//        paramsDetail.height = Constants.ROW_HEIGHT / 2;
//
//        rlSubTotalContainer.setLayoutParams(paramsDetail);
//        rlTaxContainer.setLayoutParams(paramsDetail);
//        rlDiscountContainer.setLayoutParams(paramsDetail);
//        rlDepositContainer.setLayoutParams(paramsDetail);

        int padding = 20;
//        rlSubTotalContainer.setPadding(padding, 0, DensityUtil.getActualWidthOnThisDevice(40, mContext), 0);
//        rlTaxContainer.setPadding(padding, 0, DensityUtil.getActualWidthOnThisDevice(40, mContext), 0);
//        rlDiscountContainer.setPadding(padding, 0, DensityUtil.getActualWidthOnThisDevice(40, mContext), 0);
//        rlDepositContainer.setPadding(padding, 0, DensityUtil.getActualWidthOnThisDevice(40, mContext), 0);
       // rlTotalContainer.setPadding(padding, 0, DensityUtil.getActualWidthOnThisDevice(40, mContext), 0);

        LinearLayout.LayoutParams rlOtherLayout = (LinearLayout.LayoutParams) rlSignatureContainer.getLayoutParams();
        rlOtherLayout.height = (int) (getScreenHeight() * 0.08f);

        rlSignatureContainer.setLayoutParams(rlOtherLayout);
        rlSendMailContainer.setLayoutParams(rlOtherLayout);
        rlSendSMSContainer.setLayoutParams(rlOtherLayout);

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

//        rlSubTotalContainer.addView(tvSubTotalValue);
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
//        RelativeLayout.LayoutParams paramSubTotaln = (RelativeLayout.LayoutParams) tvSubTotal.getLayoutParams();
//        paramSubTotaln.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramSubTotaln.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramSubTotaln.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        paramSubTotaln.addRule(RelativeLayout.LEFT_OF, tvSubTotalCurrency.getId());
//        tvSubTotal.setLayoutParams(paramSubTotaln);
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
//
//        rlDepositContainer.addView(tvDepositValue);
//
//        RelativeLayout.LayoutParams paramDepositValue = (RelativeLayout.LayoutParams) tvDepositValue.getLayoutParams();
//        paramDepositValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramDepositValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramDepositValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
//        paramDepositValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        tvDepositValue.setLayoutParams(paramDepositValue);
//        tvDepositValue.setId(View.generateViewId());
//
//        rlDepositContainer.addView(tvDepositCurrency);
//        RelativeLayout.LayoutParams paramDepositCurrency = (RelativeLayout.LayoutParams) tvDepositCurrency.getLayoutParams();
//        paramDepositCurrency.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramDepositCurrency.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramDepositCurrency.addRule(RelativeLayout.LEFT_OF, tvDepositValue.getId());
//        paramDepositCurrency.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        tvDepositCurrency.setLayoutParams(paramDepositCurrency);
//        tvDepositCurrency.setId(View.generateViewId());
//
//        rlDepositContainer.addView(tvDeposit);
//        RelativeLayout.LayoutParams paramDeposit = (RelativeLayout.LayoutParams) tvDeposit.getLayoutParams();
//        paramDeposit.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramDeposit.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramDeposit.addRule(RelativeLayout.LEFT_OF, tvDepositCurrency.getId());
//        paramDeposit.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        tvDeposit.setLayoutParams(paramDeposit);


       // rlTotalContainer.addView(tvTotalValue);

//        RelativeLayout.LayoutParams paramTotalValue = (RelativeLayout.LayoutParams) tvTotalValue.getLayoutParams();
//        paramTotalValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramTotalValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramTotalValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        paramTotalValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
//        tvTotalValue.setLayoutParams(paramTotalValue);
//        tvTotalValue.setId(View.generateViewId());

//        rlTotalContainer.addView(tvTotalCurrency);
//        RelativeLayout.LayoutParams paramTotalCurrency = (RelativeLayout.LayoutParams) tvTotalCurrency.getLayoutParams();
//        paramTotalCurrency.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramTotalCurrency.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramTotalCurrency.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        paramTotalCurrency.addRule(RelativeLayout.LEFT_OF, tvTotalValue.getId());
//        tvTotalCurrency.setLayoutParams(paramTotalCurrency);
//        tvTotalCurrency.setId(View.generateViewId());
//
//        rlTotalContainer.addView(tvTotal);
//        RelativeLayout.LayoutParams paramTotal = (RelativeLayout.LayoutParams) tvTotal.getLayoutParams();
//        paramTotal.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramTotal.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramTotal.addRule(RelativeLayout.CENTER_VERTICAL, tvTotalCurrency.getId());
//        paramTotal.addRule(RelativeLayout.LEFT_OF, tvTotalCurrency.getId());
//        tvTotal.setLayoutParams(paramTotal);


    }

    @Override
    protected void setPropertyOfControls() {

//        elvRefundListView.setGroupIndicator(null);
        tvSignature.setTextColor(getColor(R.color.common_black));

        tvSendEmail.setTextColor(getColor(R.color.common_black));

        tvSendSms.setTextColor(getColor(R.color.common_black));
        imSignature.setImageResource(R.drawable.icon_right_arrow);
        imSendEmail.setImageResource(R.drawable.icon_right_arrow);
        imSendSms.setImageResource(R.drawable.icon_right_arrow);

//        tvSubTotal.setText(getString(R.string.shopping_pay_subtotal));
//
//        tvTax.setText(getString(R.string.shopping_pay_tax));
//
//        tvDiscount.setText(getString(R.string.shopping_pay_discount));
//
//        tvDeposit.setText(getString(R.string.shopping_pay_deposit));

//        tvTotal.setText(getString(R.string.shopping_pay_total));
//        tvTotal.setTextColor(getColor(R.color.common_wanted_red));
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getString(R.string.player_purchase_history));
        getTvLeftTitle().setId(View.generateViewId());


        RelativeLayout parent = (RelativeLayout) getTvRight().getParent();

        IteeTextView tvDate = new IteeTextView(getActivity());
        Date historyDateObj = DateUtils.getDateFromAPIYearMonthDayHourMonthSecond(dateTime);
        String historyDateString = DateUtils.getCurrentShowYearMonthDayFromDate(historyDateObj, mContext);
        tvDate.setText(historyDateString);
        tvDate.setTextColor(getColor(R.color.calendar_blue));
        tvDate.setGravity(Gravity.CENTER_VERTICAL);

        RelativeLayout.LayoutParams ivPackageLayoutParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(60), getActualWidthOnThisDevice(60));
        ivPackageLayoutParams.addRule(RelativeLayout.RIGHT_OF, getTvLeftTitle().getId());
        ivPackageLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivPackageLayoutParams.width = (int) (getScreenWidth() * 0.3f);
        ivPackageLayoutParams.rightMargin = getActualWidthOnThisDevice(20);
        tvDate.setLayoutParams(ivPackageLayoutParams);
        parent.addView(tvDate);
        parent.invalidate();
    }

    @Override
    protected void executeOnceOnCreate() {
       // netLinkDetailRecord();
    }

    private String bookingNo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {

            bookingNo = bundle.getString("bookingNo",Constants.STR_EMPTY);
            selectDate = bundle.getString("selectDate",Constants.STR_EMPTY);
        }


        netLinkDetailRecord(bookingNo);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * get detailRecord data.
     */

    private void netLinkDetailRecord(String bookingNo) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPPING_PURCHASE_BOOKING_NO, bookingNo);
       // params.put(ApiKey.TEE_TIME_BOOKING_DATE, selectDate);
        params.put(ApiKey.TEE_TIME_BOOKING_DATE, "2015/12/02");

        HttpManager<JsonSearchProfileHistoryData> hh = new HttpManager<JsonSearchProfileHistoryData>(TeeTimeSearchProfileHistoryFragment.this) {

            @Override
            public void onJsonSuccess(JsonSearchProfileHistoryData jo) {
                dataList = jo.getDataList();


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
                if (Utils.isStringNullOrEmpty(jo.getDataList().get(0).getMemberName())){
                    tvName.setText("Walk In");

                }else{
                    tvName.setText(jo.getDataList().get(0).getMemberName());
                }
                tvName.setTextColor(getColor(R.color.common_blue));
                tvName.setTextSize(Constants.FONT_SIZE_LARGER);



                RelativeLayout.LayoutParams tvBookingNoParams
                        = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, getActualHeightOnThisDevice(80));
                tvBookingNoParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
                tvBookingNoParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                tvBookingNoParams.setMargins(0, 0, getActualWidthOnThisDevice(40), 0);


                IteeTextView tvBookingNo = new IteeTextView(getBaseActivity());
                tvBookingNo.setLayoutParams(tvBookingNoParams);

                tvBookingNo.setText(jo.getDataList().get(0).getGoodsList().get(0).getBkNo());
                //booking

                playerLayout.addView(tvName);
                playerLayout.addView(tvBookingNo);
                historyListLayout.addView(playerLayout);

                for (JsonSearchProfileHistoryData.DataItem  item :jo.getDataList()){

                    addGoodListView(item);

                    addRefundLayout(item.getRefundTime(),item.getRefundList());
                }




            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.BookingPurchaseDetail, params);
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
        private ArrayList<JsonSearchProfileHistoryData.GoodListItem> goodsList;
        private ArrayList<JsonSearchProfileHistoryData.AAListItem> aaList;
        private ArrayList<JsonSearchProfileHistoryData.PackageListItem> packageList;

        private ArrayList<JsonSearchProfileHistoryData.PricingData> pricingDataList;

        public ArrayList<JsonSearchProfileHistoryData.PricingData> getPricingDataList() {
            return pricingDataList;
        }

        public void setPricingDataList(ArrayList<JsonSearchProfileHistoryData.PricingData> pricingDataList) {
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


        public ArrayList<JsonSearchProfileHistoryData.GoodListItem> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(ArrayList<JsonSearchProfileHistoryData.GoodListItem> goodsList) {
            this.goodsList = goodsList;
        }

        public ArrayList<JsonSearchProfileHistoryData.AAListItem> getAaList() {
            return aaList;
        }

        public void setAaList(ArrayList<JsonSearchProfileHistoryData.AAListItem> aaList) {
            this.aaList = aaList;
        }

        public ArrayList<JsonSearchProfileHistoryData.PackageListItem> getPackageList() {
            return packageList;
        }

        public void setPackageList(ArrayList<JsonSearchProfileHistoryData.PackageListItem> packageList) {
            this.packageList = packageList;
        }
    }



    public class PurchaseHistoryRefundAdapter extends BaseExpandableListAdapter {

        public static final int TYPE_GOOD = 0;
        public static final int TYPE_AA = 1;
        public static final int TYPE_PACKAGE = 2;
        public static final int TYPE_REFUND = 3;
        List<PurchaseHistoryExpandable> data;
        Context context;
        private String currency;

        public PurchaseHistoryRefundAdapter(Context context, List<PurchaseHistoryExpandable> data) {

            this.data = data;
            this.context = context;
            currency = AppUtils.getCurrentCurrency(context);

        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {

            return data.get(groupPosition).getGoodList().get(childPosition);

        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }


        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


            ChildViewHolder holder;
            if (convertView == null) {

                holder = new ChildViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_of_purchase_history_child, null);
                holder.textViewName = (IteeTextView) convertView.findViewById(R.id.tv_name);
                holder.textViewPrice = (IteeTextView) convertView.findViewById(R.id.tv_price);
                holder.textViewCount = (IteeTextView) convertView.findViewById(R.id.tv_count);

                RelativeLayout.LayoutParams paramsIvArrow = (RelativeLayout.LayoutParams) holder.textViewName.getLayoutParams();
                paramsIvArrow.width = (int) (Utils.getWidth(context) * 0.4f);
                paramsIvArrow.height = (int) (Utils.getHeight(context) * 0.04f);
                paramsIvArrow.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                paramsIvArrow.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                paramsIvArrow.setMargins(60, 0, 0, 0);
                holder.textViewName.setLayoutParams(paramsIvArrow);
                holder.textViewName.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                holder.textViewName.setSingleLine(true);
                holder.textViewName.setSingleLine();
                holder.textViewName.setEllipsize(TextUtils.TruncateAt.END);
                holder.textViewName.setId(View.generateViewId());

                RelativeLayout.LayoutParams paramsTextView = (RelativeLayout.LayoutParams) holder.textViewPrice.getLayoutParams();
                paramsTextView.width = (int) (Utils.getWidth(context) * 0.4f);
                paramsTextView.height = (int) (Utils.getHeight(context) * 0.08f);
                paramsTextView.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                paramsTextView.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                paramsTextView.setMargins(0, 0, 60, 0);
                holder.textViewPrice.setLayoutParams(paramsTextView);
                holder.textViewPrice.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                holder.textViewPrice.setId(View.generateViewId());

                RelativeLayout.LayoutParams paramsIvIcon = (RelativeLayout.LayoutParams) holder.textViewCount.getLayoutParams();
                paramsIvIcon.width = (int) (Utils.getWidth(context) * 0.4f);
                paramsIvIcon.height = (int) (Utils.getHeight(context) * 0.04f);
                paramsIvIcon.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                paramsIvIcon.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                paramsIvIcon.addRule(RelativeLayout.BELOW, holder.textViewName.getId());
                paramsIvIcon.setMargins(60, 0, 0, 0);
                holder.textViewCount.setLayoutParams(paramsIvIcon);
                holder.textViewCount.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                convertView.setTag(holder);

            } else {
                holder = (ChildViewHolder) convertView.getTag();
            }

            JsonPurchaseHistoryDetailRecord.GoodListItem goodListItem = data.get(groupPosition).getGoodList().get(childPosition);
            holder.textViewName.setText(goodListItem.getName());
            holder.textViewPrice.setText(currency + Utils.get2DigitDecimalString(goodListItem.getPrice()));
            holder.textViewCount.setText(Constants.STR_MULTIPLY + String.valueOf(goodListItem.getCount()));
            holder.textViewCount.setTextColor(context.getResources().getColor(R.color.common_blue));
            holder.textViewCount.setTextSize(Constants.FONT_SIZE_SMALLER);
            return convertView;

        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (data.get(groupPosition).getGoodList() == null) {
                return 0;
            } else {
                return data.get(groupPosition).getGoodList().size();
            }


        }

        @Override
        public Object getGroup(int groupPosition) {

            return data.get(groupPosition);

        }

        @Override
        public int getGroupCount() {

            return data.size();

        }

        @Override
        public long getGroupId(int groupPosition) {

            return groupPosition;

        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            GroupViewHolder holder;
            if (convertView == null) {

                holder = new GroupViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_of_purchase_history_group, null);
                holder.textViewName = (IteeTextView) convertView.findViewById(R.id.tv_name);
                holder.textViewPrice = (IteeTextView) convertView.findViewById(R.id.tv_price);
                holder.textViewCount = (IteeTextView) convertView.findViewById(R.id.tv_count);

                RelativeLayout.LayoutParams paramsIvArrow = (RelativeLayout.LayoutParams) holder.textViewName.getLayoutParams();
                paramsIvArrow.width = (int) (Utils.getWidth(context) * 0.4f);
                paramsIvArrow.height = (int) (Utils.getHeight(context) * 0.08f);
                paramsIvArrow.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                paramsIvArrow.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                paramsIvArrow.setMargins(40, 0, 0, 0);
                holder.textViewName.setLayoutParams(paramsIvArrow);
                holder.textViewName.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                holder.textViewName.setSingleLine(true);
                holder.textViewName.setSingleLine();
                holder.textViewName.setEllipsize(TextUtils.TruncateAt.END);
                RelativeLayout.LayoutParams paramsTextView = (RelativeLayout.LayoutParams) holder.textViewPrice.getLayoutParams();
                paramsTextView.width = (int) (Utils.getWidth(context) * 0.4f);
                paramsTextView.height = (int) (Utils.getHeight(context) * 0.04f);
                paramsTextView.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                paramsTextView.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                paramsTextView.setMargins(0, 0, 40, 0);
                holder.textViewPrice.setLayoutParams(paramsTextView);
                holder.textViewPrice.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                holder.textViewPrice.setId(View.generateViewId());
                holder.textViewPrice.setTextSize(Constants.FONT_SIZE_SMALLER);

                RelativeLayout.LayoutParams paramsIvIcon = (RelativeLayout.LayoutParams) holder.textViewCount.getLayoutParams();
                paramsIvIcon.width = (int) (Utils.getWidth(context) * 0.4f);
                paramsIvIcon.height = (int) (Utils.getHeight(context) * 0.04f);
                paramsIvIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                paramsIvIcon.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                paramsIvIcon.addRule(RelativeLayout.BELOW, holder.textViewPrice.getId());
                paramsIvIcon.setMargins(0, 0, 40, 0);
                holder.textViewCount.setLayoutParams(paramsIvIcon);
                holder.textViewCount.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                convertView.setTag(holder);

            } else {
                holder = (GroupViewHolder) convertView.getTag();
            }

            PurchaseHistoryExpandable expandable = data.get(groupPosition);

            switch (expandable.getType()) {
                case TYPE_GOOD:
                    holder.textViewName.setText(expandable.getName());
                    holder.textViewPrice.setText(currency + Utils.get2DigitDecimalString(expandable.getPrice()));
                    holder.textViewCount.setText(Constants.STR_MULTIPLY + String.valueOf(expandable.getCount()));
                    holder.textViewCount.setVisibility(View.VISIBLE);
                    break;
                case TYPE_AA:

                    holder.textViewName.setText(expandable.getName());
                    holder.textViewPrice.setText(currency + expandable.getPrice());
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.textViewPrice.getLayoutParams();
                    layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                    holder.textViewPrice.setLayoutParams(layoutParams);
                    holder.textViewCount.setVisibility(View.GONE);
                    break;
                case TYPE_PACKAGE:
                    holder.textViewName.setText(expandable.getName());
                    holder.textViewPrice.setText(currency + expandable.getPrice());
                    RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) holder.textViewPrice.getLayoutParams();
                    layoutParams1.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                    layoutParams1.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                    holder.textViewPrice.setLayoutParams(layoutParams1);
                    holder.textViewCount.setVisibility(View.GONE);
                    break;
                case TYPE_REFUND:
                    holder.textViewName.setText(expandable.getName());
                    holder.textViewPrice.setText(DateUtils.getCurrentShowYearMonthDayHourMinuteSecondFromApiDateTime(expandable.getPrice(), context));
                    RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) holder.textViewPrice.getLayoutParams();
                    layoutParams2.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                    layoutParams2.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                    holder.textViewPrice.setLayoutParams(layoutParams2);
                    holder.textViewCount.setVisibility(View.GONE);


                    break;
            }

            holder.textViewCount.setTextColor(context.getResources().getColor(R.color.common_blue));
            holder.textViewCount.setTextSize(Constants.FONT_SIZE_SMALLER);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        class GroupViewHolder {
            IteeTextView textViewName;
            IteeTextView textViewPrice;
            IteeTextView textViewCount;
        }

        class ChildViewHolder {
            IteeTextView textViewName;
            IteeTextView textViewPrice;
            IteeTextView textViewCount;
        }

    }

}
