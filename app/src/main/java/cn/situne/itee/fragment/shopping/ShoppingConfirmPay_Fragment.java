package cn.situne.itee.fragment.shopping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.baoyz.actionsheet.ActionSheet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonBalanceList;
import cn.situne.itee.manager.jsonentity.JsonPayPostRet;
import cn.situne.itee.manager.jsonentity.JsonShoppingCheckPayPutReturn;
import cn.situne.itee.manager.jsonentity.JsonShoppingPaymentGet;
import cn.situne.itee.view.CheckSwitchButton;
import cn.situne.itee.view.IteeButton;
import cn.situne.itee.view.IteeCheckBox;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeMoneyEditText;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.ShoppingConfirmGoodsItem;
import cn.situne.itee.view.ShoppingPurchaseItem;
import cn.situne.itee.view.SwipeLinearLayout;
import cn.situne.itee.view.popwindow.SelectDatePopupWindow;
import cn.situne.itee.view.popwindow.ShoppingPaymentFailedPopupWindow;

/**
 * 确认支付页  ysc
 * Created by luochao on 7/10/15.
 */
public class ShoppingConfirmPay_Fragment extends BaseFragment {
    private LinearLayout llBody;
    private RelativeLayout rlBottom;
    private ChooseItemLayout voucherLayout;
    private ApplyDisCountLayout applyDisCountLayout;
    private BalanceLayout balanceLayout;
    private LastMoneyLayout lastMoneyLayout;
    private ArrayList<ChooseItemLayout> depositLayoutList;
    private ArrayList<ShoppingConfirmGoodsItem> shoppingConfirmGoodsList;

    private IteeTextView tvTotal;
    private ShoppingPaymentFailedPopupWindow shoppingPaymentFailedPopupWindow;
    private String checkedIds;
    private int purchaseFlag;
    private String fromPagePayment;
    private int bookingFlag;
    private String bookingNo;
    private String detailBookingNo;
    private IteeTextView tvGoodsSumMoney;
    private IteeTextView tvSubtotalMoney;

    private String payment;   //支付方式，TMD为什么不叫paymentType
    private IteeTextView paymentTextType;

    double total = 0.0;    //从setSumMoney方法提出，用于其他需要总金额的地方 ysc

    //region Lkl支付相关
    private String mPayTp;
    private String mProcCd;
    /**
     * 撤单凭证号
     */
    private String cancelNum = "";
    /**
     * 交易时间
     */
    private String payTime;
    //endregion Lkl支付相关 End

    private String checkPayProductList;
    private ArrayList<JsonShoppingCheckPayPutReturn.PurchaseListItem> purchaseList;
    // private ShoppingPaymentFailedPopupWindow shoppingPaymentFailedPopupWindow;
    private ArrayList<JsonShoppingCheckPayPutReturn.DepositItem> depositList;
    private double sumGoodMoney;
    private double subtotalMoney;
    private int timeMax = Constants.SECOND_60;
    private boolean isTiming;


    @Override
    protected int getFragmentId() {
        return R.layout.fragment_shopping_confirm_pay;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.player_info_renewal;
    }

    @Override
    protected void initControls(View rootView) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        purchaseList = new ArrayList<>();
        depositList = new ArrayList<>();
        shoppingConfirmGoodsList = new ArrayList<>();

        llBody = (LinearLayout) rootView.findViewById(R.id.ll_body);
        rlBottom = (RelativeLayout) rootView.findViewById(R.id.rl_bottom);

        Bundle bundle = getArguments();
        if (bundle != null) {
            depositLayoutList = new ArrayList<>();
            purchaseFlag = bundle.getInt(TransKey.SHOPPING_PURCHASE_FLAG);
            fromPagePayment = bundle.getString(TransKey.COMMON_FROM_PAGE);
            bookingFlag = bundle.getInt(TransKey.SHOPPING_BOOKING_FLAG);
            checkedIds = bundle.getString(TransKey.SHOPPING_PRODUCT_IDS);
            bookingNo = bundle.getString(TransKey.SHOPPING_BOOKING_NO, Constants.STR_EMPTY);
            detailBookingNo = bundle.getString(TransKey.SHOPPING_DETAIL_BOOKING_NO, Constants.STR_EMPTY);
            checkPayProductList = bundle.getString(TransKey.SHOPPING_CHECK_PAY_PRODUCT_LIST, Constants.STR_EMPTY);
        }
        payment = AppUtils.getDefaultPaymentPattern(mContext);
        // payment = Constants.PAYMENT_PATTERN_THIRD_PARTY;
        checkPayPut();
    }

    @Override
    protected void setDefaultValueOfControls() {
        //bottom
        RelativeLayout.LayoutParams rlBottomParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(160));
        rlBottomParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rlBottom.setLayoutParams(rlBottomParams);
        RelativeLayout.LayoutParams tvConfirmParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
        tvConfirmParams.addRule(RelativeLayout.CENTER_IN_PARENT, LAYOUT_TRUE);
        tvConfirmParams.topMargin = getActualHeightOnThisDevice(40);
        IteeTextView tvConfirm = new IteeTextView(this);
        tvConfirm.setText(getString(R.string.shopping_pay_confirm_payment));
        tvConfirm.setBackground(getResources().getDrawable(R.drawable.member_add_button));
        tvConfirm.setTextColor(getColor(R.color.common_white));
        tvConfirm.setLayoutParams(tvConfirmParams);
        tvConfirm.setOnClickListener(confirmPayClick);
        tvConfirm.setGravity(Gravity.CENTER);
        rlBottom.addView(tvConfirm);
        rlBottom.setPadding(getActualWidthOnThisDevice(40), 0, getActualWidthOnThisDevice(40), 0);
    }

    @Override
    protected void setListenersOfControls() {

    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {

    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getResources().getString(R.string.shopping_pay_title));
    }


    //region  Events
    //确认支付按钮
    View.OnClickListener confirmPayClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //region CODE
            AlertDialog.Builder builder;
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View layout = inflater.inflate(R.layout.dialog_cash_pay_comfirm, (ViewGroup) getActivity().findViewById(R.id.dialog));
            TextView tvTotalMoney = (TextView) layout.findViewById(R.id.tvTotalMoney);
            tvTotalMoney.setText(tvTotal.getText());
            TextView tvMsg = (TextView) layout.findViewById(R.id.tvMsg);
            builder = new AlertDialog.Builder(getActivity());

            //netLinkPayConfirm();
            switch (payment) {
                case Constants.PAYTYPE_CASH:
                    tvMsg.setText("请确认已收讫客人现金！");
                    builder.setTitle("请确认已收讫客人现金！").setIcon(
                            android.R.drawable.ic_dialog_info).setView(layout);
                    builder.setPositiveButton(R.string.common_confirm, new DialogInterface.OnClickListener() {
                        //LayoutInflater inflater = getLayoutInflater();
                        //View layout = inflater.inflate(R.layout.dialog_new_nfc_card, (ViewGroup) findViewById(R.id.dialog));
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            netLinkPayConfirm();
                        }
                    });
                    builder.setNegativeButton(R.string.common_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            Utils.showShortToast(getActivity(), "已取消现金支付！");
                        }
                    });
                    builder.show();

                    break;
                case Constants.PAYTYPE_CARD:
                    netLinkPayConfirm();
                    break;
                case Constants.PAYTYPE_ZHIFUBAO:
                    netLinkPayConfirm();
                    break;
                case Constants.PAYTYPE_WECHAT:
                    netLinkPayConfirm();
                    break;
                case Constants.PAYTYPE_CHEQUE:
                    tvMsg.setText("请确认已收讫客人支票！");
                    builder.setTitle("请确认已收讫客人支票！").setIcon(
                            android.R.drawable.ic_dialog_info).setView(layout);
                    builder.setPositiveButton(R.string.common_confirm, new DialogInterface.OnClickListener() {
                        //LayoutInflater inflater = getLayoutInflater();
                        //View layout = inflater.inflate(R.layout.dialog_new_nfc_card, (ViewGroup) findViewById(R.id.dialog));
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            netLinkPayConfirm();
                        }
                    });
                    builder.setNegativeButton(R.string.common_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            Utils.showShortToast(getActivity(), "已取消支票支付！");
                        }
                    });
                    builder.show();
                    break;
//                case 6:
//                    payment = Constants.PAYTYPE_VOUCHERS;
//                    paymentTextType.setText(getString(R.string.paytype_vouchers));
//                    Utils.showShortToast(getActivity(),"此功能尚在开发中...");
////                    netLinkPayConfirm();
//                    break;
                case Constants.PAYTYPE_SING:
                    tvMsg.setText("请确认已正确签单！");
                    builder.setTitle("请确认已正确签单！").setIcon(
                            android.R.drawable.ic_dialog_info).setView(layout);
                    builder.setPositiveButton(R.string.common_confirm, new DialogInterface.OnClickListener() {
                        //LayoutInflater inflater = getLayoutInflater();
                        //View layout = inflater.inflate(R.layout.dialog_new_nfc_card, (ViewGroup) findViewById(R.id.dialog));
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            netLinkPayConfirm();
                        }
                    });
                    builder.setNegativeButton(R.string.common_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            Utils.showShortToast(getActivity(), "已取消签单！");
                        }
                    });
                    builder.show();
                    break;
                case Constants.PAYTYPE_SING_COMPANY:
                    tvMsg.setText("请确认已正确公司签单！");
                    builder.setTitle("请确认已正确公司签单！").setIcon(
                            android.R.drawable.ic_dialog_info).setView(layout);
                    builder.setPositiveButton(R.string.common_confirm, new DialogInterface.OnClickListener() {
                        //LayoutInflater inflater = getLayoutInflater();
                        //View layout = inflater.inflate(R.layout.dialog_new_nfc_card, (ViewGroup) findViewById(R.id.dialog));
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            netLinkPayConfirm();
                        }
                    });
                    builder.setNegativeButton(R.string.common_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            Utils.showShortToast(getActivity(), "已取消签单！");
                        }
                    });
                    builder.show();
                    break;
                case Constants.PAYTYPE_SING_GUDONG:
                    tvMsg.setText("请确认已正确股东签单！");
                    builder.setTitle("请确认已正确股东签单！").setIcon(
                            android.R.drawable.ic_dialog_info).setView(layout);
                    builder.setPositiveButton(R.string.common_confirm, new DialogInterface.OnClickListener() {
                        //LayoutInflater inflater = getLayoutInflater();
                        //View layout = inflater.inflate(R.layout.dialog_new_nfc_card, (ViewGroup) findViewById(R.id.dialog));
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            netLinkPayConfirm();
                        }
                    });
                    builder.setNegativeButton(R.string.common_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            Utils.showShortToast(getActivity(), "已取消签单！");
                        }
                    });
                    builder.show();
                    break;
            }
            //endregion
        }
    };

    //btnPassCodeHandler
    private Handler btnPassCodeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int time = msg.what;
            balanceLayout.getBtnPassCode().setText(String.valueOf(time));
            if (time >= 0) {
                startTime();
            } else {
                isTiming = false;
                timeMax = Constants.SECOND_60;
                balanceLayout.getBtnPassCode().setText(getString(R.string.shopping_send));
                balanceLayout.getBtnPassCode().setEnabled(true);
            }

        }
    };

    private TextWatcher addMoneyTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            setSumMoney();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private ShoppingPaymentFragment.PurchaseItemListener purchaseItemListener = new ShoppingPaymentFragment.PurchaseItemListener() {
        @Override
        public void clickEditLayoutOk(int editType, int num, ShoppingPurchaseItem.RowItem item) {

        }

        @Override
        public void clickItemCheckBox(boolean checked, ShoppingPurchaseItem.RowItem rowItem) {

            setSumMoney();

        }

        @Override
        public void gotoShop(String bookingNo, String playerName) {

        }

        @Override
        public void delProductItem(String id, ShoppingPurchaseItem.RowItem rowItem, ShoppingPurchaseItem player) {

        }

        @Override
        public void gotoChoosePackage(String bookingNo, String playerName) {

        }

        @Override
        public void swipeScroll(SwipeLinearLayout layout) {

        }

        @Override
        public void deleteTimes(String TimesId, String times, ShoppingPurchaseItem.RowItem rowItem, ShoppingPurchaseItem player) {

        }

        @Override
        public void showItem(int i, ShoppingPurchaseItem.EditLayout e) {

        }

        @Override
        public void closeItem() {
        }
    };


    final ActionSheet.ActionSheetListener actionSheetListenerAddress = new ActionSheet.ActionSheetListener() {
        @Override
        public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
        }

        @Override
        public void onDismissWithCancelButton(ActionSheet actionSheet) {
        }

        AlertDialog.Builder builder;

        //支付方式选择
        @Override
        public void onOtherButtonClick(ActionSheet actionSheet, int index) {
/*          getString(R.string.paytype_cash),
            getString(R.string.paytype_card),
//                getString(R.string.paytype_balance),
            getString(R.string.paytype_zhifubao),
            getString(R.string.paytype_wechat),
            getString(R.string.paytype_cheque),
//                getString(R.string.paytype_vouchers),
//                getString(R.string.paytype_sign)
            getString(R.string.paytype_sign_company),
            getString(R.string.paytype_sign_gudong)*/

            switch (index) {
                case 0:
                    payment = Constants.PAYTYPE_CASH;
                    paymentTextType.setText(getString(R.string.tag_pay_cash));
                    break;
                case 1:
                    payment = Constants.PAYTYPE_CARD;
                    paymentTextType.setText(getString(R.string.paytype_card));
                    break;
                case 2:
                    payment = Constants.PAYTYPE_ZHIFUBAO;
                    paymentTextType.setText(getString(R.string.paytype_zhifubao));
                    break;
                case 3:
                    payment = Constants.PAYTYPE_WECHAT;
                    paymentTextType.setText(getString(R.string.paytype_wechat));
                    break;
                case 4:
                    payment = Constants.PAYTYPE_CHEQUE;
                    paymentTextType.setText(getString(R.string.paytype_cheque));
                    break;
//                case 6:
//                    payment = Constants.PAYTYPE_VOUCHERS;
//                    paymentTextType.setText(getString(R.string.paytype_vouchers));
//                    Utils.showShortToast(getActivity(),"此功能尚在开发中...");
////                    netLinkPayConfirm();
//                    break;
                case 5:
                    payment = Constants.PAYTYPE_SING;
                    paymentTextType.setText(getString(R.string.paytype_sign));
                    break;
                case 6:
                    payment = Constants.PAYTYPE_SING_COMPANY;
                    paymentTextType.setText(getString(R.string.paytype_sign_company));
                    break;
                case 7:
                    payment = Constants.PAYTYPE_SING_GUDONG;
                    paymentTextType.setText(getString(R.string.paytype_sign_gudong));
                    break;
            }
            actionSheet.dismiss();
        }
    };
    //endregion

//    item view
//    class SumMoneyLayout extends RelativeLayout{
//        public SumMoneyLayout(Context context) {
//            super(context);
//        }
//    }

    private void initGoodList() {
        for (JsonShoppingCheckPayPutReturn.PurchaseListItem purchaseListItem : purchaseList) {
            if (purchaseListItem.getProDataList() != null && purchaseListItem.getProDataList().size() > 0) {
                for (JsonShoppingCheckPayPutReturn.ProData proData : purchaseListItem.getProDataList()) {
                    ShoppingConfirmGoodsItem item1 = new ShoppingConfirmGoodsItem(
                            ShoppingConfirmPay_Fragment.this,
                            ShoppingConfirmGoodsItem.SHOPPING_CONFIRM_GOODS_ITEM_1,
                            purchaseListItem, ShoppingConfirmGoodsItem.SHOPPING_CONFIRM_ITEM_TYPE_PRODUCT);
                    item1.setProductDate(proData);
                    item1.setVoucherOkListener(new ShoppingConfirmGoodsItem.VoucherOkListener() {
                        @Override
                        public void onclickOk() {
                            setSumMoney();
                        }
                    });
                    llBody.addView(item1);
                    shoppingConfirmGoodsList.add(item1);
                }
            }

            if (purchaseListItem.getPackageList() != null && purchaseListItem.getPackageList().size() > 0) {
                for (JsonShoppingCheckPayPutReturn.PackageData packageData : purchaseListItem.getPackageList()) {
                    ShoppingConfirmGoodsItem item1 = new ShoppingConfirmGoodsItem(ShoppingConfirmPay_Fragment.this, ShoppingConfirmGoodsItem.SHOPPING_CONFIRM_GOODS_ITEM_2, purchaseListItem, ShoppingConfirmGoodsItem.SHOPPING_CONFIRM_ITEM_TYPE_PACKAGE);
                    item1.setPackageDate(packageData);
                    llBody.addView(item1);
                    shoppingConfirmGoodsList.add(item1);
                }

            }

            if (purchaseListItem.getFiftyDataList() != null && purchaseListItem.getFiftyDataList().size() > 0) {
                for (JsonShoppingCheckPayPutReturn.FiftyData fiftyData : purchaseListItem.getFiftyDataList()) {
                    ShoppingConfirmGoodsItem item1 = new ShoppingConfirmGoodsItem(ShoppingConfirmPay_Fragment.this, ShoppingConfirmGoodsItem.SHOPPING_CONFIRM_GOODS_ITEM_2, purchaseListItem, ShoppingConfirmGoodsItem.SHOPPING_CONFIRM_ITEM_TYPE_FIFTY);
                    item1.setFiftyDate(fiftyData);
                    llBody.addView(item1);
                    shoppingConfirmGoodsList.add(item1);
                }
            }

            if (purchaseListItem.getPricingDataList() != null && purchaseListItem.getPricingDataList().size() > 0) {

                ShoppingConfirmGoodsItem itemPricing = new ShoppingConfirmGoodsItem(ShoppingConfirmPay_Fragment.this, purchaseListItem.getPricingDataList(), purchaseListItem.getPricingTimes(), purchaseListItem.getPricingPrice());
                itemPricing.setIsPricing(true);
                llBody.addView(itemPricing);
                shoppingConfirmGoodsList.add(itemPricing);
                shoppingConfirmGoodsList.add(itemPricing);
            }
        }
    }

    private void iniDepositLayout() {
        depositLayoutList.clear();
        for (JsonShoppingCheckPayPutReturn.DepositItem depositItem : depositList) {
            LinearLayout.LayoutParams depositLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
            ChooseItemLayout depositLayout = new ChooseItemLayout(ShoppingConfirmPay_Fragment.this);
            depositLayout.setLayoutParams(depositLayoutParams);
            depositLayout.setCloseVisibility(View.GONE);
            depositLayout.setListener(purchaseItemListener, null);

            switch (depositItem.getBncChargeType()) {
                case "1":
                    depositLayout.getCheckBox().setText(getString(R.string.shopping_deposit) + Constants.STR_SPACE + depositItem.getId());
                    depositLayout.setPaymentTypeText(Constants.STR_SPACE + Constants.STR_BRACKETS_START + getString(R.string.tag_pay_cash) + Constants.STR_BRACKETS_END);
                    break;
                case "5":
                    depositLayout.setPaymentTypeText(Constants.STR_SPACE + Constants.STR_BRACKETS_START + getString(R.string.tag_balance_account) + Constants.STR_BRACKETS_END);
                    depositLayout.getCheckBox().setText(getString(R.string.shopping_deposit) + Constants.STR_SPACE + depositItem.getId());
                    break;
                case "2":
                    depositLayout.getCheckBox().setText(getString(R.string.shopping_deposit) + Constants.STR_SPACE + depositItem.getId());
                    depositLayout.setPaymentTypeText(Constants.STR_SPACE + Constants.STR_BRACKETS_START + getString(R.string.tag_voucher) + Constants.STR_BRACKETS_END);
                    break;
                case "3":
                    depositLayout.getCheckBox().setText(getString(R.string.shopping_deposit) + Constants.STR_SPACE + depositItem.getId());
                    depositLayout.setPaymentTypeText(Constants.STR_SPACE + Constants.STR_BRACKETS_START + getString(R.string.tag_credit_card) + Constants.STR_BRACKETS_END);
                    break;
                case "4":
                    depositLayout.setPaymentTypeText(Constants.STR_SPACE + Constants.STR_BRACKETS_START + getString(R.string.tag_third_party) + Constants.STR_BRACKETS_END);
                    depositLayout.getCheckBox().setText(getString(R.string.shopping_deposit) + Constants.STR_SPACE + depositItem.getId());
                    break;
                default:
                    break;
            }
            depositLayout.getMoneyEditText().setValue(depositItem.getBncAvail());
            depositLayout.getMoneyEditText().setEnabled(false);
            depositLayout.getMoneyEditText().setTextColor(getColor(R.color.common_black));
            depositLayout.setDepositData(depositItem);
            llBody.addView(depositLayout);
            depositLayoutList.add(depositLayout);
        }
    }

    private void initLayout() {
        //1  goodList
        initGoodList();

        //2  goods sum  money
        LinearLayout.LayoutParams rlSumMoneyLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
        RelativeLayout rlSumMoneyLayout = new RelativeLayout(mContext);
        rlSumMoneyLayout.setBackgroundColor(getColor(R.color.common_white));
        rlSumMoneyLayout.setLayoutParams(rlSumMoneyLayoutParams);
        RelativeLayout.LayoutParams tvSumMoneyParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        tvSumMoneyParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tvSumMoneyParams.rightMargin = getActualWidthOnThisDevice(40);
        tvGoodsSumMoney = new IteeTextView(mContext);
        tvGoodsSumMoney.setLayoutParams(tvSumMoneyParams);
        tvGoodsSumMoney.setGravity(Gravity.CENTER_VERTICAL);
        rlSumMoneyLayout.addView(tvGoodsSumMoney);
        AppUtils.addTopSeparatorLine(rlSumMoneyLayout, this);
        llBody.addView(rlSumMoneyLayout);

        View line1 = getIntervalView(getActualHeightOnThisDevice(40));
        line1.setBackgroundColor(getColor(R.color.common_light_gray));
        llBody.addView(AppUtils.getSeparatorLine(this));
        llBody.addView(line1);

        //3  sum Voucher
        LinearLayout.LayoutParams voucherLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
        voucherLayout = new ChooseItemLayout(ShoppingConfirmPay_Fragment.this);
        voucherLayout.setLayoutParams(voucherLayoutParams);
        voucherLayout.getCheckBox().setText(getString(R.string.shopping_voucher));
        voucherLayout.setCloseVisibility(View.GONE);
        voucherLayout.setTextHint(getString(R.string.shopping_voucher));
        voucherLayout.setListener(purchaseItemListener, addMoneyTextWatcher);
        llBody.addView(voucherLayout);

        //4 apply discount
        LinearLayout.LayoutParams applyDisCountLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        applyDisCountLayout = new ApplyDisCountLayout(ShoppingConfirmPay_Fragment.this);
        applyDisCountLayout.setLayoutParams(applyDisCountLayoutParams);
        applyDisCountLayout.setCloseVisibility(View.GONE);
        applyDisCountLayout.setListener(purchaseItemListener, addMoneyTextWatcher);
        llBody.addView(applyDisCountLayout);

        LinearLayout.LayoutParams rlSubtotalLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
        final  RelativeLayout rlSubtotalLayout = new RelativeLayout(mContext);
        rlSubtotalLayout.setBackgroundColor(getColor(R.color.common_white));
        rlSubtotalLayout.setLayoutParams(rlSubtotalLayoutParams);
        rlSubtotalLayout.setVisibility(View.GONE);

        RelativeLayout.LayoutParams tvSubtotalMoneyParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        tvSubtotalMoneyParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tvSubtotalMoneyParams.rightMargin = getActualWidthOnThisDevice(40);
        tvSubtotalMoney = new IteeTextView(mContext);
        tvSubtotalMoney.setLayoutParams(tvSubtotalMoneyParams);
        tvSubtotalMoney.setGravity(Gravity.CENTER_VERTICAL);
        rlSubtotalLayout.addView(tvSubtotalMoney);
        AppUtils.addTopSeparatorLine(rlSumMoneyLayout, this);
        llBody.addView(rlSubtotalLayout);

        applyDisCountLayout.getCheckBox().setCheckBoxListener(new IteeCheckBox.CheckBoxListener() {
            @Override
            public void changeCheck(boolean checked) {
                if(checked){
                    rlSubtotalLayout.setVisibility(View.VISIBLE);
                   applyDisCountLayout .row1.setVisibility(View.VISIBLE);
                   applyDisCountLayout .row2.setVisibility(View.VISIBLE);
                   applyDisCountLayout .row3.setVisibility(View.VISIBLE);
                   applyDisCountLayout .row4.setVisibility(View.VISIBLE);
                }else{
                    rlSubtotalLayout.setVisibility(View.GONE);
                    applyDisCountLayout.row1.setVisibility(View.GONE);
                    applyDisCountLayout.row2.setVisibility(View.GONE);
                    applyDisCountLayout.row3.setVisibility(View.GONE);
                    applyDisCountLayout.row4.setVisibility(View.GONE);
                }
            }
        });

        //balance account
        View line2 = getIntervalView(getActualHeightOnThisDevice(40));
        line2.setBackgroundColor(getColor(R.color.common_light_gray));
        llBody.addView(AppUtils.getSeparatorLine(this));
        llBody.addView(line2);
        LinearLayout.LayoutParams balanceLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        balanceLayout = new BalanceLayout(ShoppingConfirmPay_Fragment.this);
        balanceLayout.setLayoutParams(balanceLayoutParams);
        balanceLayout.setCloseVisibility(View.GONE);
        balanceLayout.setListener(purchaseItemListener);
        llBody.addView(balanceLayout);

        //last sum money
        LinearLayout.LayoutParams lastMoneyLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lastMoneyLayout = new LastMoneyLayout(ShoppingConfirmPay_Fragment.this);
        lastMoneyLayout.setLayoutParams(lastMoneyLayoutParams);
        lastMoneyLayout.setVisibility(View.GONE);
        llBody.addView(lastMoneyLayout);

        balanceLayout.getCheckBox().setCheckBoxListener(new IteeCheckBox.CheckBoxListener() {
            @Override
            public void changeCheck(boolean checked) {
                if(checked){
                    balanceLayout.row1.setVisibility(View.VISIBLE);
                    balanceLayout.row2.setVisibility(View.VISIBLE);
                    balanceLayout.row3.setVisibility(View.VISIBLE);
                    lastMoneyLayout.setVisibility(View.VISIBLE);
                }else{
                    balanceLayout.row1.setVisibility(View.GONE);
                    balanceLayout.row2.setVisibility(View.GONE);
                    balanceLayout.row3.setVisibility(View.GONE);
                    lastMoneyLayout.setVisibility(View.GONE);
                }

            }
        });

        //5  deposit
        iniDepositLayout();
        LinearLayout.LayoutParams totalLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
        RelativeLayout totalLayout = new RelativeLayout(getActivity());
        totalLayout.setLayoutParams(totalLayoutParams);

        RelativeLayout.LayoutParams tvTotalParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        tvTotalParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tvTotalParams.rightMargin = getActualWidthOnThisDevice(40);
        tvTotal = new IteeTextView(getActivity());
        tvTotal.setLayoutParams(tvTotalParams);
        tvTotal.setTextColor(getColor(R.color.common_red));
        tvTotal.setTextSize(Constants.FONT_SIZE_MORE_LARGER);
        totalLayout.addView(tvTotal);
        llBody.addView(totalLayout);
        AppUtils.addTopSeparatorLine(totalLayout, this);

        //payment pattern
        View line3 = getIntervalView(getActualHeightOnThisDevice(40));
        line3.setBackgroundColor(getColor(R.color.common_black));
        llBody.addView(AppUtils.getSeparatorLine(this));
        llBody.addView(line3);
        llBody.addView(AppUtils.getSeparatorLine(this));
        LinearLayout.LayoutParams paymentPatternLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
        RelativeLayout paymentPattern = new RelativeLayout(getBaseActivity());
        paymentPattern.setBackgroundColor(getColor(R.color.common_white));
        paymentPattern.setLayoutParams(paymentPatternLayoutParams);

        IteeTextView paymentText = new IteeTextView(getBaseActivity());
        paymentTextType = new IteeTextView(getBaseActivity());
        paymentTextType.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        paymentTextType.setTextColor(getColor(R.color.common_gray));

        // paymentText.setText("Payment pattern");

        RelativeLayout.LayoutParams paymentTextParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(400), RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams paymentTextTypeParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(400), RelativeLayout.LayoutParams.MATCH_PARENT);
        paymentTextTypeParams.rightMargin = getActualWidthOnThisDevice(20);
        paymentTextTypeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        paymentText.setLayoutParams(paymentTextParams);
        paymentTextType.setLayoutParams(paymentTextTypeParams);

        paymentPattern.addView(paymentText);
        paymentPattern.addView(paymentTextType);
        llBody.addView(paymentPattern);

        paymentPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseActivity().setTheme(com.baoyz.actionsheet.R.style.ActionSheetStyleIOS7);
                String[] tTags = new String[]{
                        getString(R.string.paytype_cash),
                        getString(R.string.paytype_card),
//                        getString(R.string.paytype_balance),
                        getString(R.string.paytype_zhifubao),
                        getString(R.string.paytype_wechat),
                        getString(R.string.paytype_cheque),
//                        getString(R.string.paytype_vouchers),
                        getString(R.string.paytype_sign),
                        getString(R.string.paytype_sign_company),
                        getString(R.string.paytype_sign_gudong)
                };

                ActionSheet.createBuilder(getActivity(), getFragmentManager())
                        .setCancelButtonTitle(getString(R.string.common_cancel))
                        .setOtherButtonTitles(tTags)
                        .setCancelButtonHidden(true)
                        .setCancelableOnTouchOutside(true).setListener(actionSheetListenerAddress).show();
            }
        });


        llBody.addView(AppUtils.getSeparatorLine(this));
        setSumMoney();

        switch (payment) {
            case Constants.PAYTYPE_CASH:
                paymentTextType.setText(getString(R.string.paytype_cash));
                break;
            case Constants.PAYTYPE_CARD:
                paymentTextType.setText(getString(R.string.paytype_card));
                break;
            case Constants.PAYTYPE_WECHAT:
                paymentTextType.setText(getString(R.string.paytype_wechat));
                break;
            case Constants.PAYTYPE_ZHIFUBAO:
                paymentTextType.setText(getString(R.string.paytype_zhifubao));
                break;
            case Constants.PAYTYPE_BALANCE:
                paymentTextType.setText(getString(R.string.paytype_balance));
                break;
            case Constants.PAYTYPE_SING:
                paymentTextType.setText(getString(R.string.paytype_sign));
                break;
            case Constants.PAYTYPE_SING_COMPANY:
                paymentTextType.setText(getString(R.string.paytype_sign_company));
                break;
            case Constants.PAYTYPE_SING_GUDONG:
                paymentTextType.setText(getString(R.string.paytype_sign_gudong));
                break;

            case Constants.PAYTYPE_CHEQUE:
                paymentTextType.setText(getString(R.string.paytype_cheque));
                break;
            default:
                break;
        }
        paymentText.setText(getString(R.string.shopping_pay_payment_pattern));
        paymentText.setPadding(getActualWidthOnThisDevice(20), 0, 0, 0);
    }

    private View getIntervalView(float height) {
        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) height);
        View v = new View(getBaseActivity());
        v.setBackgroundColor(getColor(R.color.common_gray));
        v.setLayoutParams(viewParams);
        return v;
    }

    private void checkPayPut() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPPING_CHECKED_IDS, checkedIds);
        params.put(ApiKey.SHOPPING_BOOKING_NO, bookingNo);
        params.put(ApiKey.SHOPPING_DETAIL_BOOKING_NO, detailBookingNo);
        params.put(ApiKey.SHOPPING_CHECK_PAY_PRODUCT_LIST, checkPayProductList);
//      params.put(ApiKey.SHOPPING_PURCHASE_PRODUCT_LIST, getCheckPayProductList());

        HttpManager<JsonShoppingCheckPayPutReturn> hh = new HttpManager<JsonShoppingCheckPayPutReturn>(ShoppingConfirmPay_Fragment.this) {
            @Override
            public void onJsonSuccess(JsonShoppingCheckPayPutReturn jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    purchaseList = jo.getPurchaseList();
                    depositList = jo.getDepositList();
                    initLayout();
                    balanceLayout.getAccountText().setText(jo.getBalanceAccount());
                    balanceLayout.getBalanceText().setValue(jo.getBalanceAccountMoney());
                    bookingNo = jo.getBookingNo();
                } else {
                    if (Constants.RETURN_CODE_20127_LOW_STOCK == returnCode) {
                        doBack();
                    }
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
        hh.startPut(getActivity(), ApiManager.HttpApi.ShoppingCheckPayPut, params);
    }

    private void setSumMoney() {
        boolean isOpen = true;
        sumGoodMoney = 0.0;
        subtotalMoney = 0.0;
//        double total = 0.0;

        for (ShoppingConfirmGoodsItem item : shoppingConfirmGoodsList) {
            sumGoodMoney += item.getUserMoney();
        }
        total = sumGoodMoney;
        if (sumGoodMoney <= 0) {
            sumGoodMoney = 0;
        }
        if (sumGoodMoney == 0) {
            setLayoutCloseType(0);
            isOpen = false;
        }
        //voucher
        if (voucherLayout.getCheckBox().getChecked()) {
            total -= Double.parseDouble(voucherLayout.getMoneyEditText().getValue());
        }
        if (total <= 0 && isOpen) {
            setLayoutCloseType(1);
            isOpen = false;
        }
        //apply discount
        if (applyDisCountLayout.getChecked()) {
            if (applyDisCountLayout.getManagerDiscountType().equals(Constants.SHOPPING_MANAGER_DISCOUNT_TYPE_MONEY)) {
                total -= Double.parseDouble(applyDisCountLayout.getMoneyText().getValue());

            } else {
                total -= Double.parseDouble(applyDisCountLayout.getMoneyText().getValue()) / 100 * total;
            }
        }
        if (total <= 0 && isOpen) {
            setLayoutCloseType(2);
            isOpen = false;
        }
        //balance account
        double userBalance = 0;
        double balanceMoney = 0;

        try {
            balanceMoney = Double.parseDouble(balanceLayout.getMoney());
        } catch (NumberFormatException e) {

        }
        subtotalMoney = total;
        double tax = Double.parseDouble(AppUtils.getSalesTax(getActivity())) / 100 * subtotalMoney;
        if (AppUtils.isTaxExcludeGoods(getActivity())) {
            total = total + tax;
        }

        if (balanceLayout.getChecked()) {
            if (total > balanceMoney) {
                userBalance = balanceMoney;
            } else {
                userBalance = total;
            }
            total -= balanceMoney;
        }

        if (total <= 0 && isOpen) {
            setLayoutCloseType(3);
            isOpen = false;
        }

        if (total > 0) {
            for (ChooseItemLayout depositLayout : depositLayoutList) {
                if (total > 0) {
                    if (depositLayout.getCheckBox().getChecked()) {
                        total -= Double.parseDouble(depositLayout.getMoneyEditText().getValue());
                    }
                } else {
                    depositLayout.getCheckBox().setChecked(false);
                    depositLayout.setCloseVisibility(View.VISIBLE);
                    isOpen = false;
                }
            }
        }
        //
        tvGoodsSumMoney.setText(AppUtils.getCurrentCurrency(getBaseActivity()) + Utils.get2DigitDecimalString(String.valueOf(sumGoodMoney)));

        if (subtotalMoney <= 0) {
            subtotalMoney = 0;
        }
        tvSubtotalMoney.setText(getString(R.string.shopping_pay_subtotal) + Constants.STR_SPACE + AppUtils.getCurrentCurrency(getBaseActivity()) + Utils.get2DigitDecimalString(String.valueOf(subtotalMoney)));
        lastMoneyLayout.setSubtotalText(Utils.get2DigitDecimalString(String.valueOf(subtotalMoney)));

        //fixed by syb.  total=>subtotalMoney
        // double tax = Double.parseDouble(AppUtils.getSalesTax(getActivity())) / 100 * subtotalMoney;
        lastMoneyLayout.setTaxText(Utils.get2DigitDecimalString(String.valueOf(tax)));
        lastMoneyLayout.setBalanceText(Constants.STR_SEPARATOR + Utils.get2DigitDecimalString(String.valueOf(userBalance)));
//        if (AppUtils.isTaxExcludeGoods(getActivity())) {
//            total  = total- tax;
//
//        }
        if (total <= 0) {
            total = 0;
        }
        // double total = subtotalMoney;

//        if (AppUtils.isTaxExcludeGoods(getActivity())) {
//            total += tax;
//
//        }
        tvTotal.setText(getString(R.string.shopping_pay_total) + AppUtils.getCurrentCurrency(getBaseActivity()) + Utils.get2DigitDecimalString(String.valueOf(total)));
        if (isOpen) {
            setLayoutCloseType(5);
        }
    }

    private void setDepositVisibility(int visibility) {
        for (ChooseItemLayout depositLayout : depositLayoutList) {
            depositLayout.setCloseVisibility(visibility);
            if (visibility == View.VISIBLE) {
                depositLayout.getCheckBox().setChecked(false);
            }
        }
    }

    // top 0 item voucher 1 voucher 2 apply 3 balance 4 deposit  5 open
    private void setLayoutCloseType(int top) {
        voucherLayout.setCloseVisibility(View.GONE);
        applyDisCountLayout.setCloseVisibility(View.GONE);
        balanceLayout.setCloseVisibility(View.GONE);
        setDepositVisibility(View.GONE);

        if (top < 1) {
            voucherLayout.setCloseVisibility(View.VISIBLE);
            if (voucherLayout.getCheckBox().getChecked()) {
                voucherLayout.getCheckBox().setChecked(false);
            }
        }

        if (top < 2) {
            applyDisCountLayout.setCloseVisibility(View.VISIBLE);
            if (applyDisCountLayout.getChecked()) {
                applyDisCountLayout.getCheckBox().setChecked(false);
            }
        }

        if (top < 3) {
            balanceLayout.setCloseVisibility(View.VISIBLE);
            if (balanceLayout.getChecked()) {
                balanceLayout.getCheckBox().setChecked(false);
            }
        }
        if (top < 4) {
            setDepositVisibility(View.VISIBLE);
        }
    }

    private String getGoodsPayList() {
        JSONArray jsResArray = new JSONArray();
        try {
            for (ShoppingConfirmGoodsItem viewItem : shoppingConfirmGoodsList) {
                if (viewItem.isPricing()) {
                    for (JsonShoppingPaymentGet.PricingData pricingData : viewItem.getmPricingDataArrayList()) {
                        if (Constants.STR_0.equals(pricingData.getPackageId())) {
                            JSONObject jsItem = new JSONObject();
                            jsItem.put(JsonKey.SHOPPING_ID, pricingData.getId());
                            JSONArray jsVoucherList = new JSONArray();
//                            for (ShoppingConfirmGoodsItem.VoucherLayout voucherLayout : viewItem.getVoucherLayoutList()) {
//                                jsVoucherList.put(voucherLayout.getMoney());
//                            }
                            // jsItem.put(JsonKey.SHOPPING_VOUCHER_LIST, jsVoucherList);
                            jsResArray.put(jsItem);
                        } else {
                            JSONObject jsItem = new JSONObject();
                            jsItem.put(JsonKey.SHOPPING_ID, pricingData.getId());
                            jsResArray.put(jsItem);
                        }
                    }
                } else {
                    if (viewItem.getType() == ShoppingConfirmGoodsItem.SHOPPING_CONFIRM_ITEM_TYPE_PRODUCT) {
                        JSONObject jsItem = new JSONObject();
                        jsItem.put(JsonKey.SHOPPING_ID, viewItem.getGoodsId());
                        JSONArray jsVoucherList = new JSONArray();
                        for (ShoppingConfirmGoodsItem.VoucherLayout voucherLayout : viewItem.getVoucherLayoutList()) {
                            jsVoucherList.put(voucherLayout.getMoney());
                        }
                        jsItem.put(JsonKey.SHOPPING_VOUCHER_LIST, jsVoucherList);
                        jsResArray.put(jsItem);
                    }

                    if (viewItem.getType() == ShoppingConfirmGoodsItem.SHOPPING_CONFIRM_ITEM_TYPE_PACKAGE) {
                        JSONObject jsItem = new JSONObject();
                        jsItem.put(JsonKey.SHOPPING_ID, viewItem.getGoodsId());
                        jsResArray.put(jsItem);
                    }

                    if (viewItem.getType() == ShoppingConfirmGoodsItem.SHOPPING_CONFIRM_ITEM_TYPE_FIFTY) {

                        for (JsonShoppingCheckPayPutReturn.ProData proData : viewItem.getmFiftyData().getAaList()) {
                            JSONObject jsItem = new JSONObject();
                            jsItem.put(JsonKey.SHOPPING_ID, proData.getId());
                            jsResArray.put(jsItem);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsResArray.toString();
    }

    private String getApplyDiscountParams() {
        JSONObject resObject = new JSONObject();
        try {
            resObject.put(ApiKey.SHOPPING_MANAGER, applyDisCountLayout.getManagerText().getText().toString());
            resObject.put(ApiKey.SHOPPING_PASSWORD, applyDisCountLayout.getPasswordText().getText().toString());
            resObject.put(ApiKey.SHOPPING_MANAGER_DISCOUNT_TYPE, applyDisCountLayout.getManagerDiscountType());
            resObject.put(ApiKey.SHOPPING_MANAGER_DISCOUNT, applyDisCountLayout.getMoneyText().getValue());
            resObject.put(ApiKey.SHOPPING_REASON, applyDisCountLayout.getReasonText().getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resObject.toString();
    }

    public String getDepositsParams() {
        JSONArray jsResArray = new JSONArray();
        for (ChooseItemLayout viewItem : depositLayoutList) {

            if (viewItem.getCheckBox().getChecked()) {
                jsResArray.put(viewItem.getDepositData().getBncId());
            }
        }
        return jsResArray.toString();
    }

    private String getBalanceAccountParams() {
        JSONObject resObject = new JSONObject();
        try {
            resObject.put(ApiKey.SHOPPING_BALANCE_ACCOUNT, balanceLayout.getAccountText().getText().toString());
            String money = balanceLayout.getBalanceText().getValue();
            resObject.put(ApiKey.SHOPPING_BALANCE_MONEY, money);
            resObject.put(ApiKey.SHOPPING_BALANCE_PASSCODE, balanceLayout.getPassCodeText().getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resObject.toString();
    }

    private void netLinkPayConfirm(boolean isTest) {
        Utils.showShortToast(getActivity(), payment);
        return;

    }

    //api
    private void netLinkPayConfirm() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPPING_CONFIRM_PAY_BOOKING_NO, bookingNo);
        params.put(ApiKey.SHOPPING_GOODS_PAY_LIST, getGoodsPayList());
        if (purchaseFlag == ShoppingPaymentFragment.PURCHASE_FLAG_CHECKOUT) {
            params.put(ApiKey.SHOPPING_CHECK_OUT, Constants.PAYMENT_CHECK_OUT);
        } else {
            params.put(ApiKey.SHOPPING_CHECK_OUT, Constants.PAYMENT_NOT_CHECK_OUT);
        }

        if (voucherLayout.getCheckBox().getChecked()) {
            params.put(ApiKey.SHOPPING_BIG_VOUCHER, voucherLayout.getMoneyEditText().getValue());
        }
        if (applyDisCountLayout.getChecked()) {
            params.put(ApiKey.SHOPPING_APPLY_DISCOUNT, getApplyDiscountParams());
        }
        if (balanceLayout.getChecked()) {
            params.put(ApiKey.SHOPPING_BALANCE_ACCOUNT, getBalanceAccountParams());
        }
        params.put(ApiKey.SHOPPING_DEPOSITS, getDepositsParams());
        params.put(ApiKey.SHOPPING_PAYMENT, payment);

        HttpManager<JsonPayPostRet> hh = new HttpManager<JsonPayPostRet>(ShoppingConfirmPay_Fragment.this) {
            @Override
            public void onJsonSuccess(JsonPayPostRet jo) {
                int returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20118_PAY_SUCCESSFULLY) {
                    Bundle bundle = new Bundle();
                    bundle.putString(ApiKey.PAY_ID, jo.getDataList().getPayId());
                    bundle.putString(TransKey.SHOPPING_PLAYER_NAME, "lc");
                    bundle.putInt(TransKey.SHOPPING_PURCHASE_FLAG, purchaseFlag);
                    bundle.putString(TransKey.COMMON_FROM_PAGE, fromPagePayment);
                    bundle.putInt(TransKey.SHOPPING_BOOKING_FLAG, bookingFlag);

                    push(ShoppingPaymentSucceededFragment.class, bundle);
                } else if (returnCode == 20120 || returnCode == 20102 || returnCode == 20136) {
                    Utils.showShortToast(getBaseActivity(), jo.getReturnInfo());
                } else {
                    SelectDatePopupWindow.OnDateSelectClickListener itemsOnClick = new SelectDatePopupWindow.OnDateSelectClickListener() {
                        @Override
                        public void OnGoodItemClick(String flag, String content) {
                            switch (flag) {
                                case "2":
                                    netLinkPayConfirm();
                                    shoppingPaymentFailedPopupWindow.dismiss();
                                    break;
                                case "3":
                                    shoppingPaymentFailedPopupWindow.dismiss();
                                    break;
                            }
                        }
                    };

                    shoppingPaymentFailedPopupWindow = new ShoppingPaymentFailedPopupWindow(getActivity(), itemsOnClick);
                    shoppingPaymentFailedPopupWindow.setMes(jo.getReturnInfo());
                    shoppingPaymentFailedPopupWindow.showAtLocation(ShoppingConfirmPay_Fragment.this.getRootView()
                            .findViewById(R.id.rl_content_container), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.Pay, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isTiming = false;
        timeMax = 99999;
        if (balanceLayout != null && balanceLayout.getBtnPassCode() != null) {
            balanceLayout.getBtnPassCode().setText(getString(R.string.shopping_send));
        }

    }

    private void startTime() {
        if (timeMax <= 60) {
            Thread t = new Thread() {

                @Override
                public void run() {
                    try {
                        timeMax--;
                        sleep(1000);
                        Message mes = new Message();
                        mes.what = timeMax;
                        btnPassCodeHandler.sendMessage(mes);
                    } catch (InterruptedException e) {
                        Utils.log(e.getMessage());
                    }
                }
            };
            t.start();
        }
    }

    private void sendTempPwdGet() {
        Map<String, String> params = new HashMap<>();

        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPPING_CONFIRM_PAY_ACCOUNT, balanceLayout.getAccountText().getText().toString());

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShoppingConfirmPay_Fragment.this) {
            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                Utils.showShortToast(getActivity(), jo.getReturnInfo());
                if (returnCode == Constants.RETURN_CODE_FORGOT_PASSWORD_USER_EXIST) {
                    isTiming = true;
                    balanceLayout.getBtnPassCode().setText(String.valueOf(Constants.SECOND_60));
                    startTime();
                } else {
                    balanceLayout.getBtnPassCode().setEnabled(true);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                //btnPassCode.setEnabled(true);
            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.SendTempPwd, params);

    }

    //拉卡拉支付
    private void payMoney() {
        try {
            Intent intent = new Intent();
            ComponentName component = new ComponentName("com.lkl.cloudpos.payment", "com.lkl.cloudpos.payment.activity.MainMenuActivity");

            Bundle bundle = new Bundle();
            // 0200 请求　0210 应答
            bundle.putString("msg_tp", "0200");
            // 0 银行卡 1 表示扫码（扫码包括微信、支付宝、银联钱包）
            bundle.putString("pay_tp", mPayTp);
            // 00－消费类01-授权类
            bundle.putString("proc_tp", "00");
            // 000000 消费  200000 消费撤销   660000 扫码支付  80000 扫码撤销
            // 700000 扫码补单  900000 结算
            bundle.putString("proc_cd", mProcCd);
            // 金额
            bundle.putString("amt", Utils.get2DigitDecimalString(String.valueOf(total)));//("amt", Utils.get2DigitDecimalString(String.valueOf(sumGoodMoney)));

            payTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            // 非必填第三方传入的订单号
            bundle.putString("order_no", bookingNo + payTime);
            // 调用者应用包名
            bundle.putString("appid", "cn.situne.itee");

            // 交易时间戳
            bundle.putString("time_stamp", payTime);
            bundle.putString("return_type", "1");
            // 订单信息-订单商品明细单价等
//        bundle.putString("order_info", "--》订单商品明细单价 苹果：10元");
//        bundle.putString("print_info", "--》订单商品明细单价 苹果：10元");
            intent.putExtras(bundle);
            intent.setComponent(component);
            startActivityForResult(intent, 0x01);

        } catch (Exception ex) {
            String ss = ex.getMessage();
            Utils.showShortToast(getActivity(), "当前设备不支持拉卡拉金融硬件！");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            // 支付成功
            case Activity.RESULT_OK:
                // TODO:
                netLinkPayConfirm(true);
                break;
            // 支付取消
            case Activity.RESULT_CANCELED:
                Bundle bundle = data.getExtras();
                String reason = bundle.getString("reason");
                if (reason != null) {
                    // TODO:
                    Utils.showShortToast(getActivity(), "交易已取消");
                }
                break;
            case -2:
                //交易失败
                Bundle bundle2 = data.getExtras();
                String reason2 = bundle2.getString("reason");
                if (reason2 != null) {
                    // TODO:
                    Utils.showShortToast(getActivity(), "交易失败：" + reason2);
                }
                break;
            default:
                // TODO:
                break;
        }
    }

//Cls ShoppingConfirmPay_Fragment End

    class ChooseItemLayout extends RelativeLayout {
        //data
        private JsonShoppingCheckPayPutReturn.DepositItem depositData;
        private IteeCheckBox checkBox;
        private IteeMoneyEditText moneyEditText;
        private LinearLayout closeLayout;
        private IteeTextView tvPaymentType;

        public void setTextHint(String hint) {
            moneyEditText.setHint(hint);
        }

        public void setPaymentTypeText(String str) {
            tvPaymentType.setText(str);
        }

        public ChooseItemLayout(BaseFragment fragment) {
            super(fragment.getBaseActivity());
            this.setBackgroundColor(getColor(R.color.common_white));
            RelativeLayout.LayoutParams closeLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            closeLayout = new LinearLayout(getBaseActivity());
            closeLayout.setAlpha(0.5f);

            closeLayout.setLayoutParams(closeLayoutParams);
            closeLayout.setBackgroundColor(getColor(R.color.common_deep_gray));

            RelativeLayout.LayoutParams checkBoxParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            checkBoxParams.addRule(RelativeLayout.CENTER_VERTICAL);
            checkBoxParams.leftMargin = fragment.getActualWidthOnThisDevice(40);
            checkBox = new IteeCheckBox(fragment, fragment.getActualHeightOnThisDevice(50), R.drawable.blue_check_false, R.drawable.blue_check_true);
            checkBox.setLayoutParams(checkBoxParams);
            checkBox.setTextColor(getColor(R.color.common_blue));
            checkBox.setId(View.generateViewId());


            RelativeLayout.LayoutParams tvPaymentTypeParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(200), LayoutParams.MATCH_PARENT);
            tvPaymentTypeParams.addRule(RelativeLayout.RIGHT_OF, checkBox.getId());
            tvPaymentTypeParams.leftMargin = getActualWidthOnThisDevice(5);
            tvPaymentType = new IteeTextView(getContext());
            tvPaymentType.setLayoutParams(tvPaymentTypeParams);
            tvPaymentType.setGravity(Gravity.CENTER_VERTICAL);
            tvPaymentType.setTextSize(Constants.FONT_SIZE_SMALLER);

            this.addView(checkBox);
            this.addView(tvPaymentType);
            AppUtils.addTopSeparatorLine(ChooseItemLayout.this, fragment);

            RelativeLayout.LayoutParams moneyEditTextParams = new RelativeLayout.LayoutParams(fragment.getActualWidthOnThisDevice(400), fragment.getActualHeightOnThisDevice(100));
            moneyEditTextParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            moneyEditTextParams.rightMargin = getActualWidthOnThisDevice(20);
            moneyEditText = new IteeMoneyEditText(fragment);
            moneyEditText.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            moneyEditText.setLayoutParams(moneyEditTextParams);
            moneyEditText.setTextColor(getColor(R.color.common_black));

            this.addView(moneyEditText);
            this.addView(closeLayout);
            closeLayout.setOnClickListener(null);
        }

        public IteeCheckBox getCheckBox() {
            return checkBox;
        }

        public IteeMoneyEditText getMoneyEditText() {
            return moneyEditText;
        }

        public JsonShoppingCheckPayPutReturn.DepositItem getDepositData() {
            return depositData;
        }

//        public void setMoneyEditText(IteeMoneyEditText moneyEditText) {
//            this.moneyEditText = moneyEditText;
//        }

        public void setDepositData(JsonShoppingCheckPayPutReturn.DepositItem depositData) {
            this.depositData = depositData;
        }

        public void setListener(ShoppingPaymentFragment.PurchaseItemListener checkBoxListener, TextWatcher textWatcher) {
            checkBox.setmPurchaseItemListener(checkBoxListener);
            if (textWatcher != null) {
                moneyEditText.addTextChangedListener(textWatcher);
            }
        }

        public void setCloseVisibility(int visibility) {
            closeLayout.setVisibility(visibility);
        }
    }

    class ApplyDisCountLayout extends RelativeLayout {
        private ChooseItemLayout titleLayout;
        private IteeEditText managerText;
        private IteeEditText passwordText;
        private IteeMoneyEditText moneyText;
        private IteeEditText reasonText;
        private AppUtils.EditViewMoneyWatcher editViewMoneyWatcher;
        private CheckSwitchButton editViewSwCurrency;
        private LinearLayout body;
        private LinearLayout closeLayout;

        final RelativeLayout row1;
        final RelativeLayout row2;
        final RelativeLayout row3;
        final RelativeLayout row4;

        public ApplyDisCountLayout(BaseFragment fragment) {
            super(fragment.getBaseActivity());
            RelativeLayout.LayoutParams closeLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(500));
            closeLayout = new LinearLayout(getBaseActivity());
            closeLayout.setAlpha(0.5f);
            closeLayout.setLayoutParams(closeLayoutParams);
            closeLayout.setBackgroundColor(getColor(R.color.common_deep_gray));

            RelativeLayout.LayoutParams bodyParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            body = new LinearLayout(fragment.getBaseActivity());
            body.setLayoutParams(bodyParams);
            this.addView(body);
            body.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
            titleLayout = new ChooseItemLayout(ShoppingConfirmPay_Fragment.this);
            titleLayout.setLayoutParams(titleLayoutParams);
            titleLayout.setCloseVisibility(View.GONE);
            titleLayout.getCheckBox().setText(getString(R.string.shopping_pay_applay_discount));
            titleLayout.getMoneyEditText().setVisibility(View.GONE);
            AppUtils.addTopSeparatorLine(titleLayout, fragment);
            body.addView(titleLayout);
            //row1
            LinearLayout.LayoutParams row1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
            row1 = new RelativeLayout(fragment.getBaseActivity());
            row1.setLayoutParams(row1Params);
            row1.setBackgroundColor(fragment.getColor(R.color.common_light_gray));
            row1.setVisibility(View.GONE);

            RelativeLayout.LayoutParams row1TitleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, fragment.getActualHeightOnThisDevice(100));
            row1TitleParams.leftMargin = getActualWidthOnThisDevice(40);
            IteeTextView row1Title = new IteeTextView(fragment.getBaseActivity());
            row1Title.setText(getString(R.string.shopping_manager));
            row1Title.setLayoutParams(row1TitleParams);
            row1Title.setGravity(Gravity.CENTER_VERTICAL);

            RelativeLayout.LayoutParams managerTextParams = new RelativeLayout.LayoutParams(fragment.getActualWidthOnThisDevice(400), fragment.getActualHeightOnThisDevice(100));
            managerTextParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            managerTextParams.rightMargin = getActualWidthOnThisDevice(20);
            managerText = new IteeEditText(fragment);
            managerText.setLayoutParams(managerTextParams);
            managerText.setText("");
            managerText.setHint(getString(R.string.shopping_manager));
            managerText.setTextColor(getColor(R.color.common_gray));
            managerText.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
            row1.addView(managerText);
            row1.addView(row1Title);
            AppUtils.addTopSeparatorLine(row1, fragment);
            body.addView(row1);

            //row2
            LinearLayout.LayoutParams row2Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
            row2 = new RelativeLayout(fragment.getBaseActivity());
            row2.setLayoutParams(row2Params);
            row2.setVisibility(View.GONE);

            RelativeLayout.LayoutParams row2TitleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, fragment.getActualHeightOnThisDevice(100));
            row2TitleParams.leftMargin = getActualWidthOnThisDevice(40);
            IteeTextView row2Title = new IteeTextView(fragment.getBaseActivity());
            row2Title.setText(getString(R.string.shopping_password));
            row2Title.setLayoutParams(row2TitleParams);
            row2Title.setGravity(Gravity.CENTER_VERTICAL);
            AppUtils.addTopSeparatorLine(row2, fragment);

            RelativeLayout.LayoutParams passwordTextParams = new RelativeLayout.LayoutParams(fragment.getActualWidthOnThisDevice(400), fragment.getActualHeightOnThisDevice(100));
            passwordTextParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            passwordTextParams.rightMargin = getActualWidthOnThisDevice(20);
            passwordText = new IteeEditText(fragment);
            passwordText.setLayoutParams(passwordTextParams);
            passwordText.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
            passwordText.setText("");
            passwordText.setHint(getString(R.string.shopping_password));
            passwordText.setTextColor(getColor(R.color.common_gray));
            passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            row2.addView(passwordText);
            row2.addView(row2Title);
            body.addView(row2);
            row2.setBackgroundColor(fragment.getColor(R.color.common_light_gray));
            //row3
            LinearLayout.LayoutParams row3Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
            row3 = new RelativeLayout(fragment.getBaseActivity());
            row3.setLayoutParams(row3Params);
            row3.setVisibility(View.GONE);
            AppUtils.addTopSeparatorLine(row3, ShoppingConfirmPay_Fragment.this);

            RelativeLayout.LayoutParams editViewSwCurrencyParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            editViewSwCurrencyParams.addRule(RelativeLayout.CENTER_VERTICAL);
            editViewSwCurrencyParams.leftMargin = getActualWidthOnThisDevice(40);
            editViewSwCurrency = new CheckSwitchButton(fragment, CheckSwitchButton.TYPE_DISCOUNT_OR_CURRENCY);
            editViewSwCurrency.setLayoutParams(editViewSwCurrencyParams);

            editViewSwCurrency.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    buttonView.setFocusable(true);
                    buttonView.setFocusableInTouchMode(true);
                    buttonView.requestFocus();
                    String currentCurrency = AppUtils.getCurrentCurrency(getBaseActivity());
                    Utils.hideKeyboard(getBaseActivity());

                    String Str = moneyText.getText().toString();
                    //double temp;
                    if (isChecked) {
                        if (Str.contains(currentCurrency)) {
                            moneyText.setText(Str.replace(currentCurrency, Constants.STR_EMPTY).trim());
                        }

                        if (Double.parseDouble(moneyText.getValue()) > 100) {
                            moneyText.setText("100");
                        }
                        editViewMoneyWatcher.setMaxValue(100.0);
                    } else {
                        if (!Str.contains(currentCurrency)) {
                            moneyText.setValue(Str);
                        }
                        editViewMoneyWatcher.setMaxValue(null);
                    }
                }
            });
            moneyText = new IteeMoneyEditText(fragment);
            moneyText.setHint(getString(R.string.shopping_apply_discount));
            editViewMoneyWatcher = new AppUtils.EditViewMoneyWatcher(moneyText);
            moneyText.addTextChangedListener(editViewMoneyWatcher);
            moneyText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    EditText editText = (EditText) view;
                    String currentString = editText.getText().toString();

                    double money = 0;
                    if (hasFocus) {
                        if (currentString.contains(AppUtils.getCurrentCurrency(getBaseActivity()))) {
                            currentString = currentString.replace(AppUtils.getCurrentCurrency(getBaseActivity()), Constants.STR_EMPTY);
                        }
                        try {
                            money = Double.parseDouble(currentString);
                        } catch (NumberFormatException e) {
                            Utils.log(e.getMessage());
                        }
                        if (money != 0) {
                            editText.setText(currentString);
                        } else {
                            editText.setText(Constants.STR_EMPTY);
                        }
                    } else {
                        if (!currentString.contains(AppUtils.getCurrentCurrency(getBaseActivity())) && !editViewSwCurrency.isChecked()) {
                            currentString = AppUtils.getCurrentCurrency(getBaseActivity()) + Constants.STR_EMPTY + currentString;
                        }
                        editText.setText(currentString);
                    }
                }
            });

            RelativeLayout.LayoutParams moneyTextParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(300), LayoutParams.MATCH_PARENT);
            moneyTextParams.addRule(RelativeLayout.CENTER_VERTICAL);
            moneyTextParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            moneyTextParams.rightMargin = getActualWidthOnThisDevice(20);

            moneyText.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
            moneyText.setTextColor(getColor(R.color.common_gray));
            moneyText.setLayoutParams(moneyTextParams);

            row3.addView(editViewSwCurrency);
            row3.addView(moneyText);
            row3.setBackgroundColor(fragment.getColor(R.color.common_light_gray));
            AppUtils.addBottomSeparatorLine(row3, fragment);
            body.addView(row3);
            //row4
            LinearLayout.LayoutParams row4Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
            row4 = new RelativeLayout(fragment.getBaseActivity());
            row4.setLayoutParams(row4Params);
            row4.setBackgroundColor(getColor(R.color.common_light_gray));
            row4.setVisibility(View.GONE);

            RelativeLayout.LayoutParams edDiscountReasonParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(640), getActualHeightOnThisDevice(86));
            edDiscountReasonParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            reasonText = new IteeEditText(getBaseActivity());
            reasonText.setBackgroundResource(R.drawable.textview_corner);
            reasonText.setLayoutParams(edDiscountReasonParams);
            reasonText.setHint(getString(R.string.shopping_discount_reason));
            row4.addView(reasonText);
            body.addView(row4);
            row4.setBackgroundColor(fragment.getColor(R.color.common_light_gray));
            //row5

            this.addView(closeLayout);
            closeLayout.setOnClickListener(null);
        }

        public IteeEditText getManagerText() {
            return managerText;
        }

        public IteeCheckBox getCheckBox() {
            return titleLayout.getCheckBox();
        }

        public IteeEditText getPasswordText() {
            return passwordText;
        }

        public IteeMoneyEditText getMoneyText() {
            return moneyText;
        }

        public IteeEditText getReasonText() {
            return reasonText;
        }

        public boolean getChecked() {
            return titleLayout.getCheckBox().getChecked();
        }

        public String getManagerDiscountType() {
            if (editViewSwCurrency.isChecked()) {
                return Constants.SHOPPING_MANAGER_DISCOUNT_TYPE_PERCENT;
            } else {
                return Constants.SHOPPING_MANAGER_DISCOUNT_TYPE_MONEY;
            }
        }

        public void setCloseVisibility(int visibility) {
            closeLayout.setVisibility(visibility);
        }

        public void setListener(ShoppingPaymentFragment.PurchaseItemListener checkBoxListener, TextWatcher textWatcher) {
            titleLayout.setListener(checkBoxListener, null);
            moneyText.addTextChangedListener(textWatcher);
        }
    }

    class BalanceLayout extends RelativeLayout {
        private ChooseItemLayout titleLayout;
        private IteeEditText accountText;
        private IteeMoneyEditText balanceText;
        private LinearLayout closeLayout;
        private LinearLayout body;
        private IteeEditText passCodeText;
        private IteeButton btnPassCode;

        public  final RelativeLayout row1;
        public  final RelativeLayout row2;
        public  final RelativeLayout row3;

        public BalanceLayout(BaseFragment fragment) {
            super(fragment.getBaseActivity());
            RelativeLayout.LayoutParams closeLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(400));
            closeLayout = new LinearLayout(getBaseActivity());
            closeLayout.setAlpha(0.5f);
            closeLayout.setLayoutParams(closeLayoutParams);
            closeLayout.setBackgroundColor(getColor(R.color.common_deep_gray));

            RelativeLayout.LayoutParams bodyParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            body = new LinearLayout(fragment.getBaseActivity());
            body.setLayoutParams(bodyParams);
            body.setOrientation(LinearLayout.VERTICAL);
            body.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
            titleLayout = new ChooseItemLayout(ShoppingConfirmPay_Fragment.this);
            titleLayout.setLayoutParams(titleLayoutParams);
            titleLayout.setCloseVisibility(View.GONE);
            titleLayout.getCheckBox().setText(getString(R.string.shopping_balance_account));
            titleLayout.getMoneyEditText().setVisibility(View.GONE);

            AppUtils.addTopSeparatorLine(titleLayout, fragment);
            body.addView(titleLayout);
            //row1
            LinearLayout.LayoutParams row1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
             row1 = new RelativeLayout(fragment.getBaseActivity());
            row1.setLayoutParams(row1Params);
            row1.setBackgroundColor(fragment.getColor(R.color.common_light_gray));
            row1.setVisibility(View.GONE);

            RelativeLayout.LayoutParams row1TitleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, fragment.getActualHeightOnThisDevice(100));
            row1TitleParams.leftMargin = getActualWidthOnThisDevice(40);
            IteeTextView row1Title = new IteeTextView(fragment.getBaseActivity());
            row1Title.setText(getString(R.string.shopping_account));
            row1Title.setLayoutParams(row1TitleParams);
            row1Title.setGravity(Gravity.CENTER_VERTICAL);

            RelativeLayout.LayoutParams managerTextParams = new RelativeLayout.LayoutParams(fragment.getActualWidthOnThisDevice(400), fragment.getActualHeightOnThisDevice(100));
            managerTextParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            managerTextParams.rightMargin = getActualWidthOnThisDevice(20);
            accountText = new IteeEditText(fragment);
            accountText.setLayoutParams(managerTextParams);
            accountText.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b) {
                        Map<String, String> params = new HashMap<>();
                        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
                        params.put(ApiKey.SHOPPING_ACCOUNT, accountText.getText().toString());
                        HttpManager<JsonBalanceList> hh = new HttpManager<JsonBalanceList>(ShoppingConfirmPay_Fragment.this) {
                            @Override
                            public void onJsonSuccess(JsonBalanceList jo) {
                                int returnCode = jo.getReturnCode();
                                if (returnCode == Constants.RETURN_CODE_20301) {
                                    balanceText.setValue(Utils.get2DigitDecimalString(String.valueOf(jo.getDataList().getBalance())));
                                    setSumMoney();
                                    Utils.hideKeyboard(getBaseActivity());
                                }
                            }

                            @Override
                            public void onJsonError(VolleyError error) {

                            }
                        };
                        hh.startGet(getActivity(), ApiManager.HttpApi.Balance, params);
                    }
                }
            });

            accountText.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                        if (i == 66) {
                            Map<String, String> params = new HashMap<>();
                            params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
                            params.put(ApiKey.SHOPPING_ACCOUNT, accountText.getText().toString());
                            HttpManager<JsonBalanceList> hh = new HttpManager<JsonBalanceList>(ShoppingConfirmPay_Fragment.this) {
                                @Override
                                public void onJsonSuccess(JsonBalanceList jo) {
                                    int returnCode = jo.getReturnCode();
                                    if (returnCode == Constants.RETURN_CODE_20301) {
                                        balanceText.setValue(Utils.get2DigitDecimalString(String.valueOf(jo.getDataList().getBalance())));
                                        setSumMoney();
                                        Utils.hideKeyboard(getBaseActivity());
                                    }
                                }

                                @Override
                                public void onJsonError(VolleyError error) {

                                }
                            };
                            hh.startGet(getActivity(), ApiManager.HttpApi.Balance, params);
                        }
                    }
                    return false;
                }
            });
            accountText.setTextColor(getColor(R.color.common_gray));
            accountText.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
            row1.addView(accountText);
            row1.addView(row1Title);
            AppUtils.addTopSeparatorLine(row1, fragment);
            body.addView(row1);

            //row2
            LinearLayout.LayoutParams row2Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
           row2 = new RelativeLayout(fragment.getBaseActivity());
            row2.setLayoutParams(row2Params);
            row2.setVisibility(View.GONE);

            RelativeLayout.LayoutParams row2TitleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, fragment.getActualHeightOnThisDevice(100));
            row2TitleParams.leftMargin = getActualWidthOnThisDevice(40);
            IteeTextView row2Title = new IteeTextView(fragment.getBaseActivity());
            row2Title.setText(getString(R.string.shopping_balance));
            row2Title.setLayoutParams(row2TitleParams);
            row2Title.setGravity(Gravity.CENTER_VERTICAL);
            AppUtils.addTopSeparatorLine(row2, fragment);

            RelativeLayout.LayoutParams passwordTextParams = new RelativeLayout.LayoutParams(fragment.getActualWidthOnThisDevice(400), fragment.getActualHeightOnThisDevice(100));
            passwordTextParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            passwordTextParams.rightMargin = getActualWidthOnThisDevice(40);
            balanceText = new IteeMoneyEditText(fragment);
            balanceText.setEnabled(false);
            balanceText.setLayoutParams(passwordTextParams);
            balanceText.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
            balanceText.setTextColor(getColor(R.color.common_black));
            balanceText.setFocusable(false);
            balanceText.setFocusableInTouchMode(false);
            row2.addView(balanceText);
            row2.addView(row2Title);
            body.addView(row2);
            row2.setBackgroundColor(fragment.getColor(R.color.common_light_gray));

            //row3
            LinearLayout.LayoutParams row3Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
            row3 = new RelativeLayout(fragment.getBaseActivity());
            row3.setLayoutParams(row3Params);
            AppUtils.addTopSeparatorLine(row3, fragment);
            row3.setBackgroundColor(fragment.getColor(R.color.common_light_gray));
            RelativeLayout.LayoutParams row3TitleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, fragment.getActualHeightOnThisDevice(100));
            row3TitleParams.leftMargin = getActualWidthOnThisDevice(40);
            IteeTextView row3Title = new IteeTextView(fragment.getBaseActivity());
            row3Title.setText(getString(R.string.shopping_passcode));
            row3Title.setLayoutParams(row3TitleParams);
            row3Title.setGravity(Gravity.CENTER_VERTICAL);
            row3.setVisibility(View.GONE);

            btnPassCode = new IteeButton(getActivity());
            btnPassCode.setBackgroundResource(R.drawable.bg_green_btn);
            btnPassCode.setText(getString(R.string.shopping_send));
            btnPassCode.setTextColor(getColor(R.color.common_white));
            RelativeLayout.LayoutParams btnPassCodeParams = new RelativeLayout.LayoutParams(fragment.getActualWidthOnThisDevice(150), fragment.getActualHeightOnThisDevice(80));
            btnPassCodeParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
            btnPassCodeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            btnPassCodeParams.rightMargin = getActualWidthOnThisDevice(40);
            btnPassCode.setLayoutParams(btnPassCodeParams);
            btnPassCode.setId(View.generateViewId());

            RelativeLayout.LayoutParams edPasscodeParams = new RelativeLayout.LayoutParams(fragment.getActualWidthOnThisDevice(260), fragment.getActualHeightOnThisDevice(80));
            edPasscodeParams.addRule(CENTER_VERTICAL);
            edPasscodeParams.addRule(RelativeLayout.LEFT_OF, btnPassCode.getId());
            passCodeText = new IteeEditText(getBaseActivity());
            passCodeText.setLayoutParams(edPasscodeParams);
            passCodeText.setId(View.generateViewId());

            btnPassCode.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
                @Override
                public void noDoubleClick(View v) {
                    if (!isTiming) {
                        btnPassCode.setEnabled(false);
                        sendTempPwdGet();
                    }
                }
            });
            passCodeText.setHint(Constants.STR_SPACE + getString(R.string.shopping_pay_passcode));
            passCodeText.setPadding(0, 0, 0, 0);
            passCodeText.setGravity(Gravity.CENTER_VERTICAL);
            passCodeText.setBackgroundResource(R.drawable.textview_corner);
            passCodeText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            row3.addView(btnPassCode);
            row3.addView(passCodeText);
            row3.addView(row3Title);
            body.addView(row3);

            this.addView(body);
            this.addView(closeLayout);
            closeLayout.setOnClickListener(null);

            titleLayout.getCheckBox().setCheckBoxListener(new IteeCheckBox.CheckBoxListener() {
                @Override
                public void changeCheck(boolean checked) {
                    if (checked) {
                        row1.setVisibility(View.VISIBLE);
                        row2.setVisibility(View.VISIBLE);
                        row3.setVisibility(View.VISIBLE);
                    } else {
                        row1.setVisibility(View.GONE);
                        row2.setVisibility(View.GONE);
                        row3.setVisibility(View.GONE);
                    }

                }
            });
        }

        public IteeButton getBtnPassCode() {
            return btnPassCode;
        }

        public String getMoney() {
            return balanceText.getValue();
        }

        public IteeCheckBox getCheckBox() {
            return titleLayout.getCheckBox();
        }

        public IteeEditText getAccountText() {
            return accountText;
        }

        public IteeMoneyEditText getBalanceText() {
            return balanceText;
        }

        public IteeEditText getPassCodeText() {
            return passCodeText;
        }

        public void setListener(ShoppingPaymentFragment.PurchaseItemListener checkBoxListener) {
            titleLayout.setListener(checkBoxListener, null);
        }

        public void setCloseVisibility(int visibility) {
            closeLayout.setVisibility(visibility);
        }


        public boolean getChecked() {
            return titleLayout.getCheckBox().getChecked();
        }
    }

    class LastMoneyLayout extends LinearLayout {
        private IteeTextView subtotalText;
        private IteeTextView taxText;
        private IteeTextView balanceText;
        private BaseFragment mBaseFragment;

        public LastMoneyLayout(BaseFragment baseFragment) {
            super(baseFragment.getBaseActivity());
            mBaseFragment = baseFragment;
            this.setOrientation(LinearLayout.VERTICAL);
            subtotalText = new IteeTextView(baseFragment);
            taxText = new IteeTextView(baseFragment);
            balanceText = new IteeTextView(baseFragment);
            this.addView(getRow(baseFragment, baseFragment.getString(R.string.shopping_pay_subtotal), "0", subtotalText));
            if (AppUtils.isTaxExcludeGoods(getActivity())) {
                this.addView(getRow(baseFragment, baseFragment.getString(R.string.shopping_pay_tax), "0", taxText));
            }
            this.addView(getRow(baseFragment, baseFragment.getString(R.string.shopping_pay_balance) + Constants.STR_COLON, "0", balanceText));
            this.addView(AppUtils.getSeparatorLine(baseFragment));
        }

        public void setSubtotalText(String subtotal) {
            subtotalText.setText(mBaseFragment.getString(R.string.shopping_pay_subtotal) + Constants.STR_SPACE + AppUtils.getCurrentCurrency(mBaseFragment.getBaseActivity()) + Constants.STR_SPACE + subtotal);
        }

        public void setTaxText(String tax) {
            taxText.setText(mBaseFragment.getString(R.string.shopping_pay_tax) + Constants.STR_SPACE + AppUtils.getCurrentCurrency(mBaseFragment.getBaseActivity()) + Constants.STR_SPACE + tax);
        }

        public void setBalanceText(String balance) {
            balanceText.setText(mBaseFragment.getString(R.string.shopping_pay_balance) + Constants.STR_SPACE + AppUtils.getCurrentCurrency(mBaseFragment.getBaseActivity()) + Constants.STR_SPACE + balance);
        }

        private RelativeLayout getRow(BaseFragment baseFragment, String title, String money, IteeTextView textView) {
            RelativeLayout row = new RelativeLayout(baseFragment.getBaseActivity());
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(45));
            row.setBackgroundColor(getColor(R.color.common_white));
            row.setLayoutParams(rowParams);
            RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, getActualHeightOnThisDevice(45));
            textViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            textViewParams.rightMargin = getActualWidthOnThisDevice(40);
            textView.setLayoutParams(textViewParams);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
            textView.setText(title + Constants.STR_SPACE + AppUtils.getCurrentCurrency(baseFragment.getBaseActivity()) + Constants.STR_SPACE + money);
            textView.setTextSize(Constants.FONT_SIZE_NORMAL);
            row.addView(textView);
            return row;
        }
    }
}
