package cn.situne.itee.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.shopping.ShoppingPaymentFragment;
import cn.situne.itee.manager.jsonentity.JsonShoppingPaymentGet;
import cn.situne.itee.view.popwindow.ConfirmPopWindow;
import cn.situne.itee.view.popwindow.ShoppingDeleteTimesPopupWindow;

@SuppressWarnings("UnusedDeclaration")
public class ShoppingPurchaseItem extends LinearLayout {

    public final static int ITEM_HEIGHT = 100;

    private int mPurchaseFlag;

    private ArrayList<RowItem> rowItemList;
    private JsonShoppingPaymentGet.DataItem mPlayerData;

    public JsonShoppingPaymentGet.DataItem getPlayerData() {
        return mPlayerData;
    }

    private BaseFragment mFragment;
    private Context mContext;
    //title view
    private IteeCheckBox playerRadioBtn;
    private TextView tvPlayer;
    private TextView tvPlayerName;
    private Button btnGotoShop;
    private ShoppingPaymentFragment.PurchaseItemListener mPurchaseItemListener;

    public List<RowItem> getRowItemList() {
        return rowItemList;
    }

    private String bookingNo;

    private String playerName;
    private String checkStatus;

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public  void hideGotoShop(){

        btnGotoShop.setVisibility(View.GONE);

    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }
    //row2  package available

    private RelativeLayout packageAvailableLayout;

    public IteeCheckBox getPlayerRadioBtn() {

        return playerRadioBtn;
    }

    public ShoppingPurchaseItem(BaseFragment fragment, ShoppingPaymentFragment.PurchaseItemListener purchaseItemListener, JsonShoppingPaymentGet.DataItem playerData, int purchaseFlag) {
        super(fragment.getBaseActivity());
        mFragment = fragment;
        mContext = mFragment.getActivity().getApplicationContext();
        mPurchaseItemListener = purchaseItemListener;
        mPlayerData = playerData;
        mPurchaseFlag = purchaseFlag;
        playerName = mPlayerData.getPlayerName();


        setBookingNo(playerData.getBookingNo());
        setCheckStatus(playerData.getCheckStatus());
        if (checkStatus == null) checkStatus = Constants.STR_0;
        mPurchaseFlag = Integer.parseInt(checkStatus);
        rowItemList = new ArrayList<RowItem>();
        this.setOrientation(VERTICAL);

        //if (bookingNo != null) {
        initTitleLayout();
        this.addView(AppUtils.getSeparatorLine(mFragment));
        //}
        initPackageAvailableLayout();
        if (playerData.getProDataList() != null) {
            for (JsonShoppingPaymentGet.ProData proData : playerData.getProDataList()) {
                addSingleItem(proData);
            }

        }

        addMultiItemWithPlayer(playerData.getPackageList(), playerData.getFiftyDataList());

        if (playerData.getPricingDataList()!=null && playerData.getPricingDataList().size()>0){
            addPricingData(playerData);
        }

    }

    public void addMultiItemWithPlayer(ArrayList<JsonShoppingPaymentGet.PackageData> packageDataList, ArrayList<JsonShoppingPaymentGet.FiftyData> fiftyDataList) {
        if (packageDataList != null) {
            for (JsonShoppingPaymentGet.PackageData packageData : packageDataList) {
                addMuladdMultiItemtiItem(packageData, null);
            }
        }
        if (fiftyDataList != null) {
            for (JsonShoppingPaymentGet.FiftyData fiftyData : fiftyDataList) {
                addMuladdMultiItemtiItem(null, fiftyData);
            }
        }


    }

    private void initTitleLayout() {
        playerRadioBtn = new IteeCheckBox(mFragment, mFragment.getActualHeightOnThisDevice(50), R.drawable.icon_shops_green_unselected, R.drawable.icon_shop_green_selected);
        playerRadioBtn.setBookingNo(mPlayerData.getBookingNo());
        playerRadioBtn.setId(View.generateViewId());
        tvPlayer = new TextView(mContext);
        tvPlayerName = new TextView(mContext);
        tvPlayer.setText(mPlayerData.getPlayerName());
        tvPlayer.setTextColor(mFragment.getColor(R.color.common_blue));
        tvPlayer.setTextSize(Constants.FONT_SIZE_LARGER);
        tvPlayerName.setText(AppUtils.getShortBookingNo(mPlayerData.getBookingNo()));
        btnGotoShop = new Button(mContext);

        btnGotoShop.setBackgroundResource(R.drawable.icon_purchase_shop);


        btnGotoShop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mPurchaseItemListener.gotoShop(bookingNo, playerName);
            }
        });


        if (mPurchaseFlag == ShoppingPaymentFragment.PURCHASE_FLAG_CHECKOUT) {
            btnGotoShop.setVisibility(View.GONE);

        }
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, mFragment.getActualHeightOnThisDevice(150));
        RelativeLayout row = new RelativeLayout(mContext);
        row.setLayoutParams(rowParams);
        row.setBackgroundColor(Color.WHITE);


        LinearLayout playerLayout = new LinearLayout(mContext);
        playerLayout.setOrientation(VERTICAL);

        LinearLayout.LayoutParams playerParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams playerNameParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        playerParams.weight = 1;
        playerNameParams.weight = 1;

        tvPlayerName.setLayoutParams(playerNameParams);

        tvPlayer.setLayoutParams(playerParams);
        tvPlayer.setGravity(Gravity.BOTTOM);

        playerLayout.addView(tvPlayer);
        playerLayout.addView(tvPlayerName);
        tvPlayerName.setTextColor(mFragment.getColor(R.color.common_black));

        RelativeLayout.LayoutParams playerRadioBtnParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        playerRadioBtnParams.leftMargin = mFragment.getActualWidthOnThisDevice(40);
        playerRadioBtnParams.addRule(RelativeLayout.CENTER_VERTICAL);
        playerRadioBtn.setLayoutParams(playerRadioBtnParams);
        row.addView(playerRadioBtn);
        RelativeLayout.LayoutParams playerLayoutBtnParams = new RelativeLayout.LayoutParams(mFragment.getActualWidthOnThisDevice(450), LayoutParams.MATCH_PARENT);
        playerLayoutBtnParams.addRule(RelativeLayout.RIGHT_OF, playerRadioBtn.getId());
        playerLayoutBtnParams.addRule(RelativeLayout.CENTER_VERTICAL);
        playerLayout.setLayoutParams(playerLayoutBtnParams);
        row.addView(playerLayout);
        RelativeLayout.LayoutParams gotoShopBtnParams = new RelativeLayout.LayoutParams(mFragment.getActualWidthOnThisDevice(50), mFragment.getActualWidthOnThisDevice(50));
        gotoShopBtnParams.addRule(RelativeLayout.CENTER_VERTICAL);
        gotoShopBtnParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        gotoShopBtnParams.rightMargin = mFragment.getActualWidthOnThisDevice(40);
        btnGotoShop.setLayoutParams(gotoShopBtnParams);
        row.addView(btnGotoShop);
        this.addView(row);
    }

    private void initPackageAvailableLayout() {
        LinearLayout.LayoutParams packageAvailableLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, mFragment.getActualHeightOnThisDevice(100));
        packageAvailableLayout = new RelativeLayout(mContext);
        packageAvailableLayout.setBackgroundColor(mFragment.getColor(R.color.common_red));
        packageAvailableLayout.setLayoutParams(packageAvailableLayoutParams);
        RelativeLayout.LayoutParams tvKeyParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tvKeyParams.addRule(RelativeLayout.CENTER_VERTICAL);
        TextView tvKey = new TextView(mContext);
        tvKeyParams.leftMargin = mFragment.getActualWidthOnThisDevice(40);
        tvKey.setTextSize(Constants.FONT_SIZE_15);
        tvKey.setLayoutParams(tvKeyParams);
        tvKey.setText(mFragment.getString(R.string.shopping_package_available));//k4sk

        RelativeLayout.LayoutParams gotoIconParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        gotoIconParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        gotoIconParams.rightMargin = mFragment.getActualWidthOnThisDevice(40);
        gotoIconParams.addRule(RelativeLayout.CENTER_VERTICAL);
        ImageView gotoIcon = new ImageView(mContext);
        gotoIcon.setLayoutParams(gotoIconParams);
        gotoIcon.setBackgroundResource(R.drawable.icon_right_arrow);

        packageAvailableLayout.addView(tvKey);
        packageAvailableLayout.addView(gotoIcon);
        packageAvailableLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AppUtils.getAuth(AppUtils.AuthControl.AuthControlSelling, mFragment.getActivity())) {
                    mPurchaseItemListener.gotoChoosePackage(bookingNo, playerName);
                } else {
                    AppUtils.showHaveNoPermission(mFragment);
                }


            }
        });
        this.addView(packageAvailableLayout);
    }

    private void packageAvailableLayoutVisibility(int visibility) {

        packageAvailableLayout.setVisibility(visibility);
    }


    private RowItemTitle getSingleItem() {
        LinearLayout.LayoutParams showItemParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, mFragment.getActualHeightOnThisDevice(100));
        RowItemTitle showItem = new RowItemTitle(mContext);
        showItem.setLayoutParams(showItemParams);
        showItem.setBackgroundColor(Color.GRAY);
        RelativeLayout.LayoutParams cbKeyParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        cbKeyParams.leftMargin = mFragment.getActualWidthOnThisDevice(40);
        cbKeyParams.addRule(RelativeLayout.CENTER_VERTICAL);
        IteeCheckBox cbKey = new IteeCheckBox(mFragment, mFragment.getActualHeightOnThisDevice(50), R.drawable.blue_check_false, R.drawable.blue_check_true);

        cbKey.setmPurchaseItemListener(mPurchaseItemListener);
        cbKey.setLayoutParams(cbKeyParams);

        RelativeLayout.LayoutParams rightLayoutParams = new RelativeLayout.LayoutParams(mFragment.getActualWidthOnThisDevice(240), LayoutParams.MATCH_PARENT);
        rightLayoutParams.rightMargin = mFragment.getActualWidthOnThisDevice(40);
        rightLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        LinearLayout rightLayout = new LinearLayout(mContext);
        rightLayout.setOrientation(VERTICAL);
        rightLayout.setLayoutParams(rightLayoutParams);
        rightLayout.setGravity(Gravity.END);

        LinearLayout.LayoutParams tvMoneyParams = new LinearLayout.LayoutParams(mFragment.getActualWidthOnThisDevice(240), LayoutParams.MATCH_PARENT);
        tvMoneyParams.weight = 1;
        TextView tvMoney = new TextView(mContext);
        tvMoney.setLayoutParams(tvMoneyParams);
        tvMoney.setSingleLine();
        tvMoney.setEllipsize(TextUtils.TruncateAt.END);
        tvMoney.setTextColor(mFragment.getColor(R.color.common_black));
        tvMoney.setGravity(Gravity.CENTER | Gravity.END);

        LinearLayout.LayoutParams tvNumParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        tvNumParams.weight = 1;
        TextView tvNum = new TextView(mContext);

        tvNum.setLayoutParams(tvNumParams);
        tvNum.setTextColor(mFragment.getColor(R.color.common_black));
        tvNum.setGravity(Gravity.CENTER | Gravity.END);
        showItem.addView(cbKey);
        showItem.addView(rightLayout);
        rightLayout.addView(tvMoney);
        rightLayout.addView(tvNum);
        showItem.setTvMoney(tvMoney);
        showItem.setTvNum(tvNum);
        showItem.setRightLayout(rightLayout);
        showItem.setItemCheckBox(cbKey);
        showItem.setBackgroundColor(mFragment.getColor(R.color.common_light_gray));

        return showItem;

    }

    public void removeProductItem(RowItem item) {
        rowItemList.remove(item);
        this.removeView(item);

    }

    public void addSingleItemList(ArrayList<JsonShoppingPaymentGet.ProData> proDataList) {
        for (JsonShoppingPaymentGet.ProData proData : proDataList) {
            addSingleItem(proData);
        }

    }

    public void changeItemNum(int id, int num) {

        RowItem item = findRowItemOfId(String.valueOf(id));

        if (item != null) {
            if (item.getItemType() == RowItem.ROW_ITEM_TYPE_PACKAGE) {

                item.getPackageData().setPackageNumber(num);

                item.refreshViewOfPackageData();

            } else {
                item.getProData().setQty(num);
                item.refreshView();
            }

        }


    }

    public void changeItemNum(RowItem item, int num) {

        if (item.getItemType() == ShoppingPurchaseItem.RowItem.ROW_ITEM_TYPE_PACKAGE) {
            item.getPackageData().setPackageNumber(num);
        } else {
            item.getProData().setQty(num);
        }
        item.getTitleItem().getTvNum().setText(Constants.STR_MULTIPLY + num);

    }



    public void delRowItemOfId(String id) {
        RowItem item = findRowItemOfId(id);

        if (item != null) {
            removeProductItem(item);
        }
    }


    public RowItem findRowItemOfId(String id) {

        for (RowItem rowItem : rowItemList) {

            if (id.equals(rowItem.getProductIndexId())) {

                return rowItem;
            }

        }
        return null;

    }

    public void addSingleItem(final JsonShoppingPaymentGet.ProData proData) {
        //proData.setPosition(mPosition);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        RowItem row = new RowItem(mContext);
        row.setLayoutParams(rowParams);
        RelativeLayout.LayoutParams itemParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        LinearLayout item = new LinearLayout(mContext);
        item.setOrientation(VERTICAL);
        item.setLayoutParams(itemParams);
        final EditLayout editLayout = new EditLayout(mContext);
        editLayout.setVisibility(View.GONE);
        RowItemTitle rowItemTitle = getSingleItem();

        final SwipeLinearLayout box = new SwipeLinearLayout(mContext, AppUtils.getRightButtonWidth(mContext));
        box.setOrientation(HORIZONTAL);
        box.setAfterShowRightListener(new SwipeLinearLayout.AfterShowRightListener() {
            @Override
            public void doAfterShowRight(SwipeLinearLayout swipeLinearLayout) {
                editLayout.setVisibility(View.GONE);
            }
        });
        box.addView(rowItemTitle);
        LinearLayout.LayoutParams btnItemDelParams = new LinearLayout.LayoutParams(AppUtils.getRightButtonWidth(mContext), LayoutParams.MATCH_PARENT);

        Button btnItemDel = new Button(mContext);
        box.addView(btnItemDel);


        box.setSwipeLayoutListener(new SwipeLinearLayout.SwipeLayoutListener() {
            @Override
            public void scrollView(View item) {
                mPurchaseItemListener.swipeScroll(box);
            }
        });

        btnItemDel.setText(mFragment.getString(R.string.common_delete));
        btnItemDel.setTag(row);

        btnItemDel.setBackgroundResource(R.drawable.bg_common_delete);
        btnItemDel.setLayoutParams(btnItemDelParams);
        btnItemDel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AppUtils.getAuth(AppUtils.AuthControl.AuthControlSelling, mFragment.getActivity())) {
                    mPurchaseItemListener.delProductItem(String.valueOf(proData.getId()), (RowItem) view.getTag(), ShoppingPurchaseItem.this);
                } else {
                    AppUtils.showHaveNoPermission(mFragment);
                }

            }
        });

        item.addView(box);
        item.addView(editLayout);
        row.addView(item);

        row.setProData(proData);
        row.setTitleItem(rowItemTitle);
        row.setEditLayout(editLayout);
        row.setPlayerName(mPlayerData.getPlayerName());
        row.setBookingNo(mPlayerData.getBookingNo());
        row.setItemType(RowItem.ROW_ITEM_TYPE_PRO);
        row.refreshView();
        this.addView(row);

        AppUtils.addBottomSeparatorLine(row, mFragment);

    }


    public void removeRowItem(ArrayList<RowItem> rowList) {

        for (RowItem rowItem : rowList) {

            this.removeView(rowItem);
            rowItemList.remove(rowItem);
        }


    }

    public void addMuladdMultiItemtiItem(final JsonShoppingPaymentGet.PackageData packageData, JsonShoppingPaymentGet.FiftyData fiftyData) {
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        RowItem row = new RowItem(mContext);
        row.setLayoutParams(rowParams);
        RelativeLayout.LayoutParams itemParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        LinearLayout item = new LinearLayout(mContext);
        item.setOrientation(VERTICAL);
        item.setLayoutParams(itemParams);
        final EditLayout editLayout = new EditLayout(mContext);
        editLayout.setVisibility(View.GONE);
        RowItemTitle rowItemTitle = getSingleItem();
        // item.addView(rowItemTitle);
        final SwipeLinearLayout box = new SwipeLinearLayout(mContext, AppUtils.getRightButtonWidth(mContext));

        box.setSwipeLayoutListener(new SwipeLinearLayout.SwipeLayoutListener() {
            @Override
            public void scrollView(View item) {
                mPurchaseItemListener.swipeScroll(box);
            }
        });

        box.setOrientation(HORIZONTAL);
        box.setAfterShowRightListener(new SwipeLinearLayout.AfterShowRightListener() {
            @Override
            public void doAfterShowRight(SwipeLinearLayout swipeLinearLayout) {
                editLayout.setVisibility(View.GONE);
            }
        });
        box.addView(rowItemTitle);

        item.addView(box);
        item.addView(AppUtils.getSeparatorLine(mFragment));
        row.addView(item);


        LinearLayout.LayoutParams btnItemDelParams = new LinearLayout.LayoutParams(AppUtils.getRightButtonWidth(mContext), LayoutParams.MATCH_PARENT);
        Button btnItemDel = new Button(mContext);
        box.addView(btnItemDel);
        btnItemDel.setText(mFragment.getString(R.string.common_delete));
        btnItemDel.setTag(row);
        btnItemDel.setBackgroundResource(R.drawable.bg_common_delete);

        btnItemDel.setLayoutParams(btnItemDelParams);
        btnItemDel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                if (AppUtils.getAuth(AppUtils.AuthControl.AuthControlSelling, mFragment.getActivity())) {
                    mPurchaseItemListener.delProductItem(String.valueOf(packageData.getId()), (RowItem) view.getTag(), ShoppingPurchaseItem.this);
                } else {
                    AppUtils.showHaveNoPermission(mFragment);
                }


            }
        });


        if (packageData != null) {
            for (JsonShoppingPaymentGet.ProData proData : packageData.getPackageList()) {
                // RowItemTitle rowItemTitle = getSingleItem();
                RowItemTitle rowItem = getSingleItem();
                rowItem.setText(proData, true);
                item.addView(rowItem);
            }
        }
        if (fiftyData != null) {
            box.setmRightViewWidth(0);
            for (JsonShoppingPaymentGet.ProData proData : fiftyData.getAaList()) {
                // RowItemTitle rowItemTitle = getSingleItem();
                RowItemTitle rowItem = getSingleItem();
                rowItem.setText(proData, false);
                item.addView(rowItem);
            }
        }


        item.addView(editLayout);


        row.setEditLayout(editLayout);
        row.setTitleItem(rowItemTitle);

        row.setPlayerName(mPlayerData.getPlayerName());
        row.setBookingNo(mPlayerData.getBookingNo());
        if (packageData != null) {
            row.setItemType(RowItem.ROW_ITEM_TYPE_PACKAGE);
            row.setPackageData(packageData);
            row.refreshViewOfPackageData();
        } else {
            row.setItemType(RowItem.ROW_ITEM_TYPE_FIFTY);
            row.setFiftyData(fiftyData);
            row.refreshViewOfFiftyData();
        }


        this.addView(row);
        AppUtils.addBottomSeparatorLine(row, mFragment);

    }
    ShoppingDeleteTimesPopupWindow confirmPopWindow = null;
    private void addPricingData( final JsonShoppingPaymentGet.DataItem playerData){


        int pNum = 1;
        String times = playerData.getPricingTimes();

        ArrayList<JsonShoppingPaymentGet.PricingData> pricingDataList = playerData.getPricingDataList();
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final RowItem row = new RowItem(mContext);
        row.setIsPricing(true);
        row.setmPlayerData(mPlayerData);

        row.setLayoutParams(rowParams);
        row.setItemType(RowItem.ROW_ITEM_TYPE_PRICING);
        row.setPricingPrice(playerData.getPricingPrice());
        RelativeLayout.LayoutParams itemParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        LinearLayout item = new LinearLayout(mContext);
        item.setOrientation(VERTICAL);
        item.setLayoutParams(itemParams);
        RowItemTitle rowItemTitle = getSingleItem();
        rowItemTitle.getTvMoney().setText(AppUtils.getCurrentCurrency(getContext())+playerData.getPricingPrice());

        rowItemTitle.getTvMoney().setVisibility(View.GONE);
        rowItemTitle.getItemCheckBox().setText(mFragment.getString(R.string.pricing_deduct)+times);
        AppUtils.addBottomSeparatorLine(rowItemTitle,getContext());
        final SwipeLinearLayout box = new SwipeLinearLayout(mContext, AppUtils.getRightButtonWidth(mContext));

        box.setSwipeLayoutListener(new SwipeLinearLayout.SwipeLayoutListener() {
            @Override
            public void scrollView(View item) {
                mPurchaseItemListener.swipeScroll(box);
            }
        });

        box.setOrientation(HORIZONTAL);
        box.addView(rowItemTitle);

        item.addView(box);
        item.addView(AppUtils.getSeparatorLine(mFragment));
        row.addView(item);


        LinearLayout.LayoutParams btnItemDelParams = new LinearLayout.LayoutParams(AppUtils.getRightButtonWidth(mContext), LayoutParams.MATCH_PARENT);
        Button btnItemDel = new Button(mContext);
        box.addView(btnItemDel);
        btnItemDel.setText(mFragment.getString(R.string.common_delete));
        btnItemDel.setTag(row);
        btnItemDel.setBackgroundResource(R.drawable.bg_common_delete);

        btnItemDel.setLayoutParams(btnItemDelParams);
        btnItemDel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                if (AppUtils.getAuth(AppUtils.AuthControl.AuthControlSelling, mFragment.getActivity())) {

                     confirmPopWindow = new ShoppingDeleteTimesPopupWindow(mFragment.getActivity(), new OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Utils.hideKeyboard(mFragment.getBaseActivity());

                            if (Utils.isStringNotNullOrEmpty(confirmPopWindow.getTimes())){

                                mPurchaseItemListener.deleteTimes(playerData.getPricingBdpId(),confirmPopWindow.getTimes(), row, ShoppingPurchaseItem.this);
                            }else{

                                Utils.showShortToast(mFragment.getBaseActivity(), AppUtils.generateNotNullMessage(mFragment.getBaseActivity(), R.string.pricing_table_times));
                            }


                        }
                    }, mFragment);
                    confirmPopWindow.setMessage(mFragment.getString(R.string.msg_confirm_pay_wether_to_return_times));
                    confirmPopWindow.showAtLocation(mFragment.getRootView().findViewById(R.id.rl_content_container),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                } else {
                    AppUtils.showHaveNoPermission(mFragment);
                }


            }
        });


        for (JsonShoppingPaymentGet.PricingData pricingData:pricingDataList){

            RowItemTitle rowItem = getSingleItem();
            rowItem.getItemCheckBox().setBtnCheckVisibility(View.INVISIBLE);
            rowItem.getItemCheckBox().setText(pricingData.getProductName());
            rowItem.getTvMoney().setText(pricingData.getDiscountPrice());
            rowItem.getTvNum().setText(Constants.STR_MULTIPLY+pricingData.getQty());
            pNum++;
            AppUtils.addBottomSeparatorLine(rowItem,getContext());
            item.addView(rowItem);

            if (!Constants.STR_0.equals(pricingData.getPackageId())){
                for (JsonShoppingPaymentGet.PricingData p:pricingData.getProductList()){
                    RowItemTitle rowItemP = getSingleItem();
                    rowItemP.getItemCheckBox().setBtnCheckVisibility(View.INVISIBLE);
                    rowItemP.getItemCheckBox().setText(p.getProductName());
                    rowItemP.getTvMoney().setText(p.getDiscountPrice());
                    rowItemP.getTvNum().setText( Constants.STR_MULTIPLY+p.getQty());
                    item.addView(rowItemP);
                    pNum++;
                }
            }
        }
        row.setTitleItem(rowItemTitle);
        row.setPlayerName(mPlayerData.getPlayerName());
        row.setBookingNo(mPlayerData.getBookingNo());
        this.addView(row);
        AppUtils.addBottomSeparatorLine(row, mFragment);
        row.setPricingRows(pNum);
        if (Constants.STR_1.equals(playerData.getPricingPayStatus()))
        row.close(pNum);
        rowItemList.add(row);
    }


    public class RowItem extends RelativeLayout {

        private boolean isPricing;
        private int pricingRows;
        private String pricingPrice;

        public String getPricingPrice() {
            return pricingPrice;
        }

        public void setPricingPrice(String pricingPrice) {
            this.pricingPrice = pricingPrice;
        }

        public int getPricingRows() {
            return pricingRows;
        }

        public void setPricingRows(int pricingRows) {
            this.pricingRows = pricingRows;
        }

        public boolean isPricing() {
            return isPricing;
        }

        public void setIsPricing(boolean isPricing) {
            this.isPricing = isPricing;
        }


        private JsonShoppingPaymentGet.DataItem mPlayerData;

        public JsonShoppingPaymentGet.DataItem getmPlayerData() {
            return mPlayerData;
        }

        public void setmPlayerData(JsonShoppingPaymentGet.DataItem mPlayerData) {
            this.mPlayerData = mPlayerData;
        }

        public final static int ROW_ITEM_TYPE_PRO = 1;
        public final static int ROW_ITEM_TYPE_PACKAGE = 2;
        public final static int ROW_ITEM_TYPE_FIFTY = 3;

        public final static int ROW_ITEM_TYPE_PRICING = 4;
        private String productIndexId;


        private LinearLayout closeLayout;
        private JsonShoppingPaymentGet.ProData proData;
        private JsonShoppingPaymentGet.PackageData packageData;
        private JsonShoppingPaymentGet.FiftyData fiftyData;
        private RowItemTitle titleItem;
        private EditLayout editLayout;
        private int itemType;

        private String playerName;
        private String bookingNo;

        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        public String getPlayerName() {
            return playerName;
        }

        public void setPlayerName(String playerName) {
            this.playerName = playerName;
        }

        public String getBookingNo() {
            return bookingNo;
        }

        public void setBookingNo(String bookingNo) {
            this.bookingNo = bookingNo;
        }

        public JsonShoppingPaymentGet.PackageData getPackageData() {
            return packageData;
        }

        public String getProductIndexId() {
            return productIndexId;
        }

        public void setPackageData(JsonShoppingPaymentGet.PackageData packageData) {
            this.packageData = packageData;
        }

        public JsonShoppingPaymentGet.FiftyData getFiftyData() {
            return fiftyData;
        }

        public void setFiftyData(JsonShoppingPaymentGet.FiftyData fiftyData) {
            this.fiftyData = fiftyData;
        }

        public RowItemTitle getTitleItem() {
            return titleItem;
        }

        public void setTitleItem(RowItemTitle titleItem) {
            this.titleItem = titleItem;
        }

        public EditLayout getEditLayout() {
            return editLayout;
        }

        public void setEditLayout(EditLayout editLayout) {
            this.editLayout = editLayout;
        }


        public LinearLayout getCloseLayout() {
            return closeLayout;
        }

        public void setCloseLayout(LinearLayout closeLayout) {
            this.closeLayout = closeLayout;
        }

        public JsonShoppingPaymentGet.ProData getProData() {
            return proData;
        }

        public void setProData(JsonShoppingPaymentGet.ProData proData) {
            this.proData = proData;
        }

        public RowItem(Context context) {
            super(context);

            closeLayout = new LinearLayout(context);
            closeLayout.setGravity(Gravity.CENTER);

            closeLayout.setBackgroundColor(mFragment.getColor(R.color.common_gray));
            this.addView(closeLayout);
            closeLayout.setVisibility(View.GONE);
            closeLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

        public void close(int num) {
            RelativeLayout.LayoutParams closeLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, mFragment.getActualHeightOnThisDevice(ITEM_HEIGHT * num));
            closeLayout.setLayoutParams(closeLayoutParams);
            closeLayout.bringToFront();
            closeLayout.setAlpha(0.8f);
            closeLayout.setVisibility(View.VISIBLE);
            closeLayout.removeAllViews();
            TextView text = new TextView(mContext);
            text.setText(mFragment.getString(R.string.shopping_paid));
            text.setTextColor(mFragment.getColor(R.color.common_white));
            text.setTextSize(Constants.FONT_SIZE_MORE_LARGER);
            closeLayout.addView(text);
        }


        public void refreshViewOfFiftyData() {
            LinearLayout titleRightLayout = titleItem.getRightLayout();

            if (fiftyData.getPayStatus() == 1) {

                this.close(fiftyData.getAaList().size() + 1);
            } else {
                rowItemList.add(this);

            }
            titleItem.getItemCheckBox().setText(mFragment.getString(R.string.shopping_aa_with)+fiftyData.getAAWith());
            titleItem.getTvMoney().setText(AppUtils.getCurrentCurrency(mContext) + Utils.get2DigitDecimalString(String.valueOf(fiftyData.getAaTotalPrice())));
            titleItem.getItemCheckBox().setRowItem(RowItem.this);
            titleItem.getTvNum().setVisibility(View.GONE);
        }


        public void refreshViewOfPackageData() {
            productIndexId = String.valueOf(packageData.getId());
            LinearLayout titleRightLayout = titleItem.getRightLayout();
            if (editLayout != null) {
                titleRightLayout.setTag(editLayout);
                titleRightLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (AppUtils.getAuth(AppUtils.AuthControl.AuthControlSelling, mFragment.getActivity())) {
                            EditLayout e = (EditLayout) v.getTag();

                            if (e.getVisibility() == View.VISIBLE && e.getTypeStr().equals("Qty")) {
                                mPurchaseItemListener.closeItem();
                                e.setVisibility(View.GONE);
                            } else {
                                e.getNumberEdit().setMinNum(1);
                                e.getNumberEdit().setMaxNum(0);
                                e.setEditValue("Qty", packageData.getPackageNumber(), EditLayout.QTY_TYPE);
                                e.setVisibility(View.VISIBLE);
                                int[] location = new int[2];
                                titleItem.getLocationOnScreen(location);
                                int y = location[1];
//                                if (null != oldE) {
//                                    oldE.setVisibility(View.GONE);
//                                }
//                                oldE = e;
                                mPurchaseItemListener.showItem(y + titleItem.getHeight() * (packageData.getPackageList().size() + 2), e);
                            }
                        } else {
                            AppUtils.showHaveNoPermission(mFragment);
                        }


                    }
                });
                titleItem.setTag(editLayout);
                titleItem.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (AppUtils.getAuth(AppUtils.AuthControl.AuthControlSelling, mFragment.getActivity())) {
                            if (Utils.isStringNotNullOrEmpty(bookingNo)) {

                                EditLayout e = (EditLayout) v.getTag();
                                if (e.isOnePlayer() || (e.getVisibility() == View.VISIBLE && e.getTypeStr().equals(mFragment.getString(R.string.shopping_split)))) {
                                    mPurchaseItemListener.closeItem();
                                    e.setVisibility(View.GONE);
                                } else {
                                    e.getNumberEdit().setMaxNum(packageData.getPackageNumber());
                                    e.getNumberEdit().setMinNum(1);
                                    e.setEditValue(mFragment.getString(R.string.shopping_split), packageData.getPackageNumber(), EditLayout.SPLIT_TYPE);
                                    e.setVisibility(View.VISIBLE);
                                    int[] location = new int[2];
                                    titleItem.getLocationOnScreen(location);
                                    int y = location[1];
                                    mPurchaseItemListener.showItem(y + titleItem.getHeight() * (packageData.getPackageList().size() + 2), e);
                                }
                            }
                        } else {
                            AppUtils.showHaveNoPermission(mFragment);
                        }


                    }
                });
            }
            if (packageData.getPayStatus() == 1) {

                this.close(packageData.getPackageList().size() + 1);
            } else {
                rowItemList.add(this);

            }
            titleItem.getItemCheckBox().setText(packageData.getPackageName());
            titleItem.getTvMoney().setText(AppUtils.getCurrentCurrency(mContext) + Utils.get2DigitDecimalString(String.valueOf(packageData.getPackagePrice())));
            titleItem.getTvNum().setText(Constants.STR_MULTIPLY + packageData.getPackageNumber());
            titleItem.getItemCheckBox().setRowItem(RowItem.this);

            editLayout.getBtnOk().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    editLayout.setVisibility(View.GONE);
                    if (Utils.isStringNotNullOrEmpty(bookingNo)) {

                        mPurchaseItemListener.clickEditLayoutOk(editLayout.getEditType(), editLayout.getNumberEdit().getCurrentNum(), RowItem.this);
                    } else {

                        changeItemNum(RowItem.this, editLayout.getNumberEdit().getCurrentNum());

                    }
                }
            });

        }

        public void refreshView() {
            productIndexId = String.valueOf(proData.getId());
            LinearLayout titleRightLayout = titleItem.getRightLayout();
            if (editLayout != null) {
                titleRightLayout.setTag(editLayout);
                titleRightLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (AppUtils.getAuth(AppUtils.AuthControl.AuthControlSelling, mFragment.getActivity())) {
                            EditLayout e = (EditLayout) v.getTag();
                            if (e.getVisibility() == View.VISIBLE && e.getTypeStr().equals("Qty")) {
                                mPurchaseItemListener.closeItem();
                                e.setVisibility(View.GONE);
                            } else {
                                e.getNumberEdit().setMinNum(1);
                                e.getNumberEdit().setMaxNum(0);
                                if (Constants.STR_1.equals(proData.getPdSort())) {

                                    e.getNumberEdit().setMaxNum(1);
                                }
                                e.setEditValue("Qty", proData.getQty(), EditLayout.QTY_TYPE);
                                e.setVisibility(View.VISIBLE);

                                int[] location = new int[2];
                                titleItem.getLocationOnScreen(location);
                                int y = location[1];

                                mPurchaseItemListener.showItem(y + titleItem.getHeight() * 2, e);

                            }
                        } else {
                            AppUtils.showHaveNoPermission(mFragment);
                        }


                    }
                });
                titleItem.setTag(editLayout);
                titleItem.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (AppUtils.getAuth(AppUtils.AuthControl.AuthControlSelling, mFragment.getActivity())) {
                            if (Utils.isStringNotNullOrEmpty(bookingNo)) {
                                EditLayout e = (EditLayout) v.getTag();
                                if (e.isOnePlayer()||(e.getVisibility() == View.VISIBLE && e.getTypeStr().equals(mFragment.getString(R.string.shopping_split)))) {
                                    e.setVisibility(View.GONE);
                                    mPurchaseItemListener.closeItem();
                                } else {
                                    e.getNumberEdit().setMaxNum(proData.getQty());
                                    e.getNumberEdit().setMinNum(1);
                                    e.setEditValue(mFragment.getString(R.string.shopping_split), proData.getQty(), EditLayout.SPLIT_TYPE);
                                    e.setVisibility(View.VISIBLE);
                                    int[] location = new int[2];
                                    titleItem.getLocationOnScreen(location);
                                    int y = location[1];
                                    mPurchaseItemListener.showItem(y + titleItem.getHeight() * 2, e);
                                }

                            }
                        } else {
                            AppUtils.showHaveNoPermission(mFragment);
                        }


                    }
                });
            }
            if (proData.getPayStatus() == 1) {

                this.close(1);
            } else {
                rowItemList.add(this);

            }
            titleItem.getItemCheckBox().setText(proData.getProductName());
            if (!Constants.STR_0.equals(proData.getPromoteId())&& Utils.isStringNotNullOrEmpty(proData.getPromoteId())){
                titleItem.getItemCheckBox().setText(proData.getProductName()+Constants.STR_SEPARATOR+mFragment.getString(R.string.shopping_promote));

            }

            titleItem.getTvMoney().setText(AppUtils.getCurrentCurrency(mContext) + Utils.get2DigitDecimalString(String.valueOf(proData.getDisCountPrice())));
            titleItem.getTvNum().setText(Constants.STR_MULTIPLY + proData.getQty());
            titleItem.getItemCheckBox().setRowItem(RowItem.this);


            editLayout.setTag(proData);

            editLayout.getBtnOk().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    editLayout.setVisibility(View.GONE);
                    if (Utils.isStringNotNullOrEmpty(bookingNo)) {
                        mPurchaseItemListener.clickEditLayoutOk(editLayout.getEditType(), editLayout.getNumberEdit().getCurrentNum(), RowItem.this);
                    } else {

                        changeItemNum(RowItem.this, editLayout.getNumberEdit().getCurrentNum());

                    }
                }
            });


        }

        public void addNum(int num) {

            proData.setQty(proData.getQty() + num);
            titleItem.getTvNum().setText(Constants.STR_MULTIPLY + proData.getQty());
        }

        public int minusNum(int num) {
            proData.setQty(proData.getQty() - num);
            int qty = proData.getQty();
            titleItem.getTvNum().setText(Constants.STR_MULTIPLY + qty);

            if (qty <= 0) {

                ViewGroup parent = (ViewGroup) this.getParent();
                parent.removeView(this);
                rowItemList.remove(this);
            }
            return proData.getQty();

        }


        public int minusPackageNum(int num) {


            packageData.setPackageNumber(packageData.getPackageNumber() - num);
            int qty = packageData.getPackageNumber();
            titleItem.getTvNum().setText(Constants.STR_MULTIPLY + qty);
            if (qty <= 0) {

                ViewGroup parent = (ViewGroup) this.getParent();
                parent.removeView(this);
                rowItemList.remove(this);
            }
            return packageData.getPackageNumber();

        }


    }

    public class RowItemTitle extends RelativeLayout {
        private IteeCheckBox itemCheckBox;
        private TextView tvMoney;
        private TextView tvNum;
        private LinearLayout rightLayout;

        public void setText(JsonShoppingPaymentGet.ProData proData, boolean isPackage) {
            tvMoney.setText(AppUtils.getCurrentCurrency(mFragment.getActivity()) + Utils.get2DigitDecimalString(String.valueOf(proData.getDisCountPrice())));
            tvNum.setText(Constants.STR_MULTIPLY + String.valueOf(proData.getQty()));
            if (isPackage) {
                if (Utils.isStringNotNullOrEmpty(proData.getAttriName())) {

                    itemCheckBox.setText(proData.getProductName() + Constants.STR_BRACKETS_START + proData.getAttriName() + Constants.STR_BRACKETS_END);
                } else {
                    itemCheckBox.setText(proData.getProductName());
                }

            } else {

                itemCheckBox.setText(proData.getProductName());
            }

            itemCheckBox.setBtnCheckVisibility(View.INVISIBLE);
        }


        public void setText(JsonShoppingPaymentGet.FiftyData fiftyData) {

//            tvMoney.setText(AppUtils.getCurrentCurrency(mFragment.getActivity())+String.valueOf(fiftyData.getPrice()));
//            tvNum.setText(Constants.STR_MULTIPLY+String.valueOf(fiftyData.getQty()));
//            itemCheckBox.setText(fiftyData.getProductName());
//            itemCheckBox.setBtnCheckVisibility(View.INVISIBLE);
        }

        public LinearLayout getRightLayout() {
            return rightLayout;
        }

        public void setRightLayout(LinearLayout rightLayout) {
            this.rightLayout = rightLayout;
        }

        public TextView getTvMoney() {
            return tvMoney;
        }

        public void setTvMoney(TextView tvMoney) {
            this.tvMoney = tvMoney;
        }

        public TextView getTvNum() {
            return tvNum;
        }

        public void setTvNum(TextView tvNum) {
            this.tvNum = tvNum;
        }

        public IteeCheckBox getItemCheckBox() {
            return itemCheckBox;
        }

        public void setItemCheckBox(IteeCheckBox itemCheckBox) {
            this.itemCheckBox = itemCheckBox;
        }

        public RowItemTitle(Context context) {
            super(context);
        }


    }

    public class EditLayout extends RelativeLayout {

        public final static int QTY_TYPE = 1;
        public final static int SPLIT_TYPE = 2;

        private IteeTextView tvText;
        private IteeNumberEditView numberEdit;
        private Button btnOk;
        private int editType;

        private boolean isOnePlayer;

        public boolean isOnePlayer() {
            return isOnePlayer;
        }

        public void setIsOnePlayer(boolean isOnePlayer) {
            this.isOnePlayer = isOnePlayer;
        }

        public IteeNumberEditView getNumberEdit() {
            return numberEdit;
        }

        public int getEditType() {
            return editType;
        }

        public Button getBtnOk() {
            return btnOk;
        }


        public EditLayout(Context context) {
            super(context);

            this.setBackgroundColor(Color.BLUE);
            LinearLayout.LayoutParams myParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mFragment.getActualHeightOnThisDevice(100));
            tvText = new IteeTextView(context);
            tvText.setText(mFragment.getString(R.string.shop_setting_qty));
            this.setLayoutParams(myParams);
            RelativeLayout.LayoutParams tvTextParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mFragment.getActualHeightOnThisDevice(100));
            tvTextParams.leftMargin = mFragment.getActualWidthOnThisDevice(40);
            tvText.setLayoutParams(tvTextParams);
            tvText.setGravity(Gravity.CENTER_VERTICAL);
            RelativeLayout.LayoutParams btnOkParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            btnOkParams.rightMargin = mFragment.getActualWidthOnThisDevice(40);
            btnOkParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            btnOkParams.addRule(RelativeLayout.CENTER_VERTICAL);
            btnOk = new Button(mContext);
            btnOk.setLayoutParams(btnOkParams);
            btnOk.setText(mFragment.getString(R.string.common_ok));
            btnOk.setId(View.generateViewId());


            RelativeLayout.LayoutParams numberEditParams = new RelativeLayout.LayoutParams(mFragment.getActualWidthOnThisDevice(200.f / 60.f * 80.f), mFragment.getActualHeightOnThisDevice(80));
            numberEditParams.addRule(RelativeLayout.CENTER_VERTICAL);
            numberEditParams.addRule(RelativeLayout.LEFT_OF, btnOk.getId());
            numberEdit = new IteeNumberEditView(mFragment);
            numberEdit.setLayoutParams(numberEditParams);
            this.addView(tvText);
            this.addView(btnOk);
            this.addView(numberEdit);
        }

        public void setEditValue(String key, int num, int editType) {
            tvText.setText(key);
            numberEdit.setCurrentNum(num);
            this.editType = editType;
        }

        public String getTypeStr() {

            return tvText.getText().toString();
        }

        public void addNum(int num) {

            numberEdit.setCurrentNum(numberEdit.getCurrentNum() + num);

        }
    }

}
