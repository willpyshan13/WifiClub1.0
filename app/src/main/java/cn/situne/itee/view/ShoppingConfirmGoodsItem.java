package cn.situne.itee.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.jsonentity.JsonChoosePricingTableData;
import cn.situne.itee.manager.jsonentity.JsonShoppingCheckPayPutReturn;
import cn.situne.itee.manager.jsonentity.JsonShoppingPaymentGet;

/**
 * Created by luochao on 7/10/15.
 */
public class ShoppingConfirmGoodsItem extends LinearLayout {
    private BaseFragment mBaseFragment;

    private boolean isPricing;

    private  ArrayList<JsonShoppingPaymentGet.PricingData> mPricingDataArrayList;

    public ArrayList<JsonShoppingPaymentGet.PricingData> getmPricingDataArrayList() {
        return mPricingDataArrayList;
    }

    public void setmPricingDataArrayList(ArrayList<JsonShoppingPaymentGet.PricingData> mPricingDataArrayList) {
        this.mPricingDataArrayList = mPricingDataArrayList;
    }

    public boolean isPricing() {
        return isPricing;
    }

    public void setIsPricing(boolean isPricing) {
        this.isPricing = isPricing;
    }

    private Context mContext;
    //layout
    private IteeTextView tvTitleName;
    private  IteeTextView titleNumText;
    private IteeTextView titleMoneyText;
    private ArrayList<VoucherLayout> voucherLayoutList;
    public static final int SHOPPING_CONFIRM_GOODS_ITEM_1= 1;//have voucher
    public static final int SHOPPING_CONFIRM_GOODS_ITEM_2= 2;// not voucher
    private int mHaveVoucher;

    public static final int SHOPPING_CONFIRM_ITEM_TYPE_PRODUCT= 1;//have product
    public static final int SHOPPING_CONFIRM_ITEM_TYPE_PACKAGE= 2;// not package
    public static final int SHOPPING_CONFIRM_ITEM_TYPE_FIFTY= 3;//fifty
    public static final int SHOPPING_CONFIRM_ITEM_TYPE_PRICING= 4;//pricing
    private int mType;

    public int getType() {
        return mType;
    }

    public ArrayList<VoucherLayout> getVoucherLayoutList() {
        return voucherLayoutList;
    }

    //com
    private RelativeLayout titleLayout;
    private RelativeLayout itemLayout;




    private int num;
    private String  goodsMoney;
    private String priingPrice;
    private AddVoucherLayout addVoucherLayout;
    private LinearLayout voucherListLayout;



    private  JsonShoppingCheckPayPutReturn.ProData mProData;
    private JsonShoppingCheckPayPutReturn.PackageData mPackageDate;
    private JsonShoppingCheckPayPutReturn.FiftyData mFiftyData;

    private String goodsId;

    public JsonShoppingCheckPayPutReturn.FiftyData getmFiftyData() {
        return mFiftyData;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }


    public ShoppingConfirmGoodsItem(BaseFragment baseFragment,ArrayList<JsonShoppingPaymentGet.PricingData> pricingDataArrayList,String times,String priingPrice) {
        super(baseFragment.getActivity());
        mBaseFragment = baseFragment;
        mPricingDataArrayList  = pricingDataArrayList;
        voucherLayoutList = new ArrayList<>();
        mContext = baseFragment.getActivity().getApplicationContext();
        this.priingPrice = priingPrice;
        mType = SHOPPING_CONFIRM_ITEM_TYPE_PRICING;
        this.addView(AppUtils.getSeparatorLine(baseFragment));
        this.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams rlTitleParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,baseFragment.getActualHeightOnThisDevice(100));

        RelativeLayout rlTitle = new RelativeLayout(getContext());

        IteeTextView timesTitle = new IteeTextView(getContext());
        timesTitle.setText(baseFragment.getString(R.string.pricing_deduct)+Constants.STR_SPACE+times);

        rlTitle.addView(timesTitle);

        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(timesTitle, baseFragment.getActualWidthOnThisDevice(40), getContext());

        rlTitle.setLayoutParams(rlTitleParams);
        rlTitle.setBackgroundColor(mBaseFragment.getColor(R.color.common_white));

        this.addView(rlTitle);


        for (JsonShoppingPaymentGet.PricingData pricingData:pricingDataArrayList){
            if (Constants.STR_0.equals(pricingData.getPackageId())){
                LinearLayout.LayoutParams rlProductTitleParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,baseFragment.getActualHeightOnThisDevice(100));
                RelativeLayout rlProductC = new RelativeLayout(getContext());
                IteeTextView tvProductName = new IteeTextView(getContext());
                rlProductC.addView(tvProductName);
                tvProductName.setText(pricingData.getProductName());
                LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvProductName, baseFragment.getActualWidthOnThisDevice(40), getContext());
                rlProductC.setLayoutParams(rlProductTitleParams);



                RelativeLayout.LayoutParams pNumParams = new RelativeLayout.LayoutParams(mBaseFragment.getActualWidthOnThisDevice(200),mBaseFragment.getActualHeightOnThisDevice(50));
                IteeTextView pNum = new IteeTextView(getContext());
                pNum.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                pNumParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                pNum.setLayoutParams(pNumParams);
                pNum.setTextColor(getResources().getColor(R.color.common_blue));
                pNum.setTextSize(Constants.FONT_SIZE_SMALLER);
                pNumParams.rightMargin = mBaseFragment.getActualWidthOnThisDevice(40);

                pNumParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);


                RelativeLayout.LayoutParams pMoneyParams = new RelativeLayout.LayoutParams(mBaseFragment.getActualWidthOnThisDevice(200),mBaseFragment.getActualHeightOnThisDevice(50));
                IteeTextView pMoney = new IteeTextView(getContext());
                pMoney.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
                pMoneyParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                pMoneyParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                pMoney.setLayoutParams(pMoneyParams);
                pMoneyParams.rightMargin = mBaseFragment.getActualWidthOnThisDevice(40);
                pMoney.setText(AppUtils.getCurrentCurrency(getContext())+pricingData.getDiscountPrice());
                pNum.setText(Constants.STR_MULTIPLY+Constants.STR_1);
                rlProductC.addView(pMoney);
                rlProductC.addView(pNum);

                rlProductC.setBackgroundColor(mBaseFragment.getColor(R.color.common_white));
                this.addView(rlProductC);
            }else{

                LinearLayout.LayoutParams rlPackageTitleParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,baseFragment.getActualHeightOnThisDevice(100));

                RelativeLayout rlPackageC = new RelativeLayout(getContext());

                IteeTextView tvPackageName = new IteeTextView(getContext());
                tvPackageName.setText(pricingData.getProductName());

                rlPackageC.addView(tvPackageName);
                LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvPackageName, baseFragment.getActualWidthOnThisDevice(40), getContext());

                rlPackageC.setLayoutParams(rlPackageTitleParams);
                this.addView(rlPackageC);
                rlPackageC.setBackgroundColor(mBaseFragment.getColor(R.color.common_white));

                for (JsonShoppingPaymentGet.PricingData packagePricing:pricingData.getProductList()){
                    LinearLayout.LayoutParams rlPackageItemParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,baseFragment.getActualHeightOnThisDevice(100));
                    RelativeLayout rlPackageItemC = new RelativeLayout(getContext());
                    IteeTextView tvPackageItemName = new IteeTextView(getContext());
                    tvPackageItemName.setText(packagePricing.getProductName());

                    rlPackageItemC.addView(tvPackageItemName);
                    LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvPackageItemName, baseFragment.getActualWidthOnThisDevice(40), getContext());

                    rlPackageItemC.setLayoutParams(rlPackageItemParams);



                    RelativeLayout.LayoutParams packageNumParams = new RelativeLayout.LayoutParams(mBaseFragment.getActualWidthOnThisDevice(200),mBaseFragment.getActualHeightOnThisDevice(50));
                    IteeTextView packageNum = new IteeTextView(getContext());
                    packageNum.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                    packageNumParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    packageNum.setLayoutParams(packageNumParams);
                    packageNum.setTextColor(getResources().getColor(R.color.common_blue));
                    packageNum.setTextSize(Constants.FONT_SIZE_SMALLER);
                    packageNumParams.rightMargin = mBaseFragment.getActualWidthOnThisDevice(40);

                    packageNumParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);


                    RelativeLayout.LayoutParams packageMoneyParams = new RelativeLayout.LayoutParams(mBaseFragment.getActualWidthOnThisDevice(200),mBaseFragment.getActualHeightOnThisDevice(50));
                    IteeTextView packageMoney = new IteeTextView(getContext());
                    packageMoney.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
                    packageMoneyParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    packageMoneyParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    packageMoney.setLayoutParams(packageMoneyParams);
                    packageMoneyParams.rightMargin = mBaseFragment.getActualWidthOnThisDevice(40);
                    packageMoney.setText(AppUtils.getCurrentCurrency(getContext())+pricingData.getDiscountPrice());
                    packageNum.setText(Constants.STR_MULTIPLY+Constants.STR_1);
                    rlPackageItemC.addView(packageMoney);
                    rlPackageItemC.addView(packageNum);

                    rlPackageItemC.setBackgroundColor(mBaseFragment.getColor(R.color.common_white));

                    this.addView(rlPackageItemC);

                }
            }
        }




//        this.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (addVoucherLayout.getVisibility() == View.GONE) {
//                    addVoucherLayout.setVisibility(View.VISIBLE);
//
//                } else {
//
//                    addVoucherLayout.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        initAddVoucherLayout();



    }

    public ShoppingConfirmGoodsItem(BaseFragment baseFragment,int haveVoucher,JsonShoppingCheckPayPutReturn.PurchaseListItem itemDate,int type) {
        super(baseFragment.getActivity());

        this.setOrientation(VERTICAL);
        LinearLayout.LayoutParams myParams = new  LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        voucherLayoutList = new ArrayList<>();
        mContext = baseFragment.getActivity().getApplicationContext();
        mBaseFragment = baseFragment;
        mType = type;
        //setmItemData(itemDate);

        mHaveVoucher = haveVoucher;
        this.addView(AppUtils.getSeparatorLine(mBaseFragment));
        intTitleLayout();
        if (mHaveVoucher == SHOPPING_CONFIRM_GOODS_ITEM_1)
            initAddVoucherLayout();

        this.setLayoutParams(myParams);
    }


    private void intTitleLayout(){
        RelativeLayout itemTile = getItem(Constants.STR_EMPTY, Constants.STR_EMPTY, Constants.STR_EMPTY, mBaseFragment.getColor(R.color.common_white));

        if (mHaveVoucher == SHOPPING_CONFIRM_GOODS_ITEM_1){

            itemTile.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (addVoucherLayout.getVisibility() == View.GONE) {
                        addVoucherLayout.setVisibility(View.VISIBLE);

                    } else {

                        addVoucherLayout.setVisibility(View.GONE);
                    }
                }
            });
        }

        this.addView(itemTile);

    }

    private void initAddVoucherLayout(){
         addVoucherLayout = new AddVoucherLayout(mBaseFragment);
         voucherListLayout = new LinearLayout(mBaseFragment.getBaseActivity());
         voucherListLayout.setOrientation(VERTICAL);
         this.addView(voucherListLayout);
         this.addView(addVoucherLayout);

    }


    public double getUserMoney(){
        if (mType == SHOPPING_CONFIRM_ITEM_TYPE_PRODUCT){
            double sumVoucher = 0.0;
            for (VoucherLayout voucherLayout:voucherLayoutList){
                sumVoucher+=Double.parseDouble(Utils.get2DigitDecimalString(voucherLayout.getMoney()));
            }
            return mProData.getDisCountPrice()*mProData.getQty() - sumVoucher;

        }
        if (mType == SHOPPING_CONFIRM_ITEM_TYPE_PACKAGE){
            return mPackageDate.getPackagePrice();
        }
        if (mType == SHOPPING_CONFIRM_ITEM_TYPE_FIFTY){
            return mFiftyData.getAaTotalPrice();
        }


        if (mType == SHOPPING_CONFIRM_ITEM_TYPE_PRICING){
            return Double.parseDouble(priingPrice);
        }

        return 0.0;
    }

    private RelativeLayout getItem(String title,String money,String num,int backColor){
        LinearLayout.LayoutParams titleLayoutParams = new  LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT,mBaseFragment.getActualHeightOnThisDevice(100));
        RelativeLayout titleLayout = new RelativeLayout(mContext);
        titleLayout.setBackgroundColor(mBaseFragment.getColor(R.color.common_white));
        titleLayout.setLayoutParams(titleLayoutParams);

        RelativeLayout.LayoutParams tvTitleNameParams =new  RelativeLayout.LayoutParams(mBaseFragment.getActualWidthOnThisDevice(450),RelativeLayout.LayoutParams.MATCH_PARENT);
        tvTitleNameParams.leftMargin = mBaseFragment.getActualWidthOnThisDevice(40);
        tvTitleName = new IteeTextView(mContext);
        tvTitleName.setTextColor(mBaseFragment.getColor(R.color.common_black));
        tvTitleName.setText(title);
        tvTitleName.setLayoutParams(tvTitleNameParams);
        tvTitleName.setGravity(Gravity.CENTER_VERTICAL);

        titleLayout.addView(tvTitleName);

        RelativeLayout.LayoutParams rightLayoutParams = new    RelativeLayout.LayoutParams(mBaseFragment.getActualWidthOnThisDevice(150),RelativeLayout.LayoutParams.MATCH_PARENT);
        rightLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rightLayoutParams.rightMargin = mBaseFragment.getActualWidthOnThisDevice(40);
        LinearLayout rightLayout = new LinearLayout(mBaseFragment.getBaseActivity());
        rightLayout.setLayoutParams(rightLayoutParams);
        rightLayout.setOrientation(VERTICAL);


        LinearLayout.LayoutParams  goodMoneyTextParams = new   LinearLayout.LayoutParams(mBaseFragment.getActualWidthOnThisDevice(150),0);
        goodMoneyTextParams.weight = 1;
        titleMoneyText = new IteeTextView(mBaseFragment.getBaseActivity());
        titleMoneyText.setText(AppUtils.getCurrentCurrency(mBaseFragment.getBaseActivity()) + money);
        titleMoneyText.setLayoutParams(goodMoneyTextParams);

        titleMoneyText.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        LinearLayout.LayoutParams  numTextParams = new   LinearLayout.LayoutParams(mBaseFragment.getActualWidthOnThisDevice(150),0);
        numTextParams.weight = 1;
        titleNumText = new IteeTextView(mBaseFragment.getBaseActivity());
        titleNumText.setLayoutParams(numTextParams);
        titleNumText.setText(Constants.STR_MULTIPLY + num);
        titleNumText.setGravity(Gravity.TOP | Gravity.RIGHT);
        titleNumText.setTextColor(mBaseFragment.getColor(R.color.common_blue));
        titleNumText.setTextSize(Constants.FONT_SIZE_NORMAL);


        rightLayout.addView(titleMoneyText);
        rightLayout.addView(titleNumText);
        titleLayout.addView(rightLayout);
        titleLayout.setBackgroundColor(backColor);
       return titleLayout;

    }

    public void setProductDate(JsonShoppingCheckPayPutReturn.ProData proData){
        mProData = proData;
        setGoodsId(String.valueOf(proData.getId()));
        tvTitleName.setText(proData.getProductName());
        titleNumText.setText(Constants.STR_MULTIPLY + String.valueOf(proData.getQty()));
        titleMoneyText.setText(AppUtils.getCurrentCurrency(mBaseFragment.getBaseActivity()) + Utils.get2DigitDecimalString(String.valueOf(proData.getDisCountPrice())));
    }


    public void setPackageDate(JsonShoppingCheckPayPutReturn.PackageData packageDate){
        mPackageDate = packageDate;
        setGoodsId(String.valueOf(packageDate.getId()));
        tvTitleName.setText(packageDate.getPackageName());
        titleNumText.setText(Constants.STR_MULTIPLY + String.valueOf(packageDate.getPackageNumber()));
        titleMoneyText.setText(AppUtils.getCurrentCurrency(mBaseFragment.getBaseActivity()) + Utils.get2DigitDecimalString(String.valueOf(packageDate.getPackagePrice())));
        for (JsonShoppingCheckPayPutReturn.ProData proData : packageDate.getPackageList()){

            String packageName  = proData.getProductName();
            if (Utils.isStringNotNullOrEmpty(proData.getAttriName())){
                packageName =packageName + Constants.STR_BRACKETS_START +  proData.getAttriName() +Constants.STR_BRACKETS_END;

            }

            this.addView(getItem(packageName,Utils.get2DigitDecimalString(String.valueOf(proData.getDisCountPrice())),String.valueOf(proData.getQty()),mBaseFragment.getColor(R.color.common_light_gray)));
        }

    }
    public void setFiftyDate(JsonShoppingCheckPayPutReturn.FiftyData fiftyDate){
        mFiftyData = fiftyDate;

        tvTitleName.setText(fiftyDate.getAAWith());
       // titleNumText.setText(Constants.STR_MULTIPLY + String.valueOf(fiftyDate.getPackageNumber()));
        titleNumText.setVisibility(View.GONE);
        titleMoneyText.setText(AppUtils.getCurrentCurrency(mBaseFragment.getBaseActivity()) + Utils.get2DigitDecimalString(String.valueOf(fiftyDate.getAaTotalPrice())));
        for (JsonShoppingCheckPayPutReturn.ProData proData : fiftyDate.getAaList()){
            String packageName  = mBaseFragment.getString(R.string.shopping_aa_with) + proData.getProductName();
            this.addView(getItem(packageName,Utils.get2DigitDecimalString(String.valueOf(proData.getDisCountPrice())),String.valueOf(proData.getQty()),mBaseFragment.getColor(R.color.common_light_gray)));
        }

    }

    class AddVoucherLayout extends  RelativeLayout
    {


        public AddVoucherLayout(BaseFragment baseFragment) {
            super(baseFragment.getBaseActivity());

            //editView
            LinearLayout.LayoutParams myLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, baseFragment.getActualHeightOnThisDevice(100));

            this.setVisibility(View.GONE);
            this.setBackgroundResource(R.color.common_fleet_blue);
            this.setLayoutParams(myLayoutParams);

            //editView  CheckSwitchButton
            RelativeLayout.LayoutParams editViewSwCurrencyParam = new RelativeLayout.LayoutParams(mBaseFragment.getActualWidthOnThisDevice(160), mBaseFragment.getActualHeightOnThisDevice(80));
            editViewSwCurrencyParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            editViewSwCurrencyParam.addRule(RelativeLayout.CENTER_VERTICAL);
            editViewSwCurrencyParam.leftMargin = mBaseFragment.getActualWidthOnThisDevice(20);
            editViewSwCurrencyParam.rightMargin = mBaseFragment.getActualWidthOnThisDevice(10);

            //editView  EditText
            RelativeLayout.LayoutParams reduceRateParam = new RelativeLayout.LayoutParams(mBaseFragment.getActualWidthOnThisDevice(480), mBaseFragment.getActualHeightOnThisDevice(80));
            reduceRateParam.addRule(RelativeLayout.CENTER_VERTICAL);
            reduceRateParam.leftMargin = mBaseFragment.getActualWidthOnThisDevice(40);


            final EditText edReduceRate = new EditText(mBaseFragment.getActivity());
            edReduceRate.setLayoutParams(reduceRateParam);
            edReduceRate.setBackground(mBaseFragment.getResources().getDrawable(R.drawable.textview_corner));
            edReduceRate.setRawInputType(Configuration.KEYBOARD_12KEY);
            edReduceRate.setId(View.generateViewId());
            //  edReduceRate.setTag(position);
            edReduceRate.setPadding(10, 5, 0, 0);
            edReduceRate.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
            edReduceRate.setPadding(mBaseFragment.getActualWidthOnThisDevice(50),0,mBaseFragment.getActualWidthOnThisDevice(10),0);


            AppUtils.EditViewMoneyWatcher editTextWatcher = new AppUtils.EditViewMoneyWatcher(edReduceRate);
            edReduceRate.addTextChangedListener(editTextWatcher);
            // edReduceRate.setOnFocusChangeListener(editViewFocusChangeListener);
            edReduceRate.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        if (keyCode == 66) {
                            Utils.hideKeyboard(mBaseFragment.getBaseActivity());
                        }

                    }

                    return false;
                }
            });

            //editView  Button
            RelativeLayout.LayoutParams okBtnParam = new RelativeLayout.LayoutParams(mBaseFragment.getActualWidthOnThisDevice(150), mBaseFragment.getActualWidthOnThisDevice(80));
            okBtnParam.addRule(RelativeLayout.RIGHT_OF, edReduceRate.getId());
            okBtnParam.addRule(RelativeLayout.CENTER_VERTICAL);
            okBtnParam.leftMargin = mBaseFragment.getActualWidthOnThisDevice(20);
            Button okBtn = new Button(mBaseFragment.getActivity());
            okBtn.setText(R.string.common_ok);
            okBtn.setTextSize(Constants.FONT_SIZE_NORMAL);
            okBtn.setSingleLine();
            // okBtn.setTag(position);
            okBtn.setLayoutParams(okBtnParam);
            // okBtn.setOnClickListener(okBtnClickListener);
            okBtn.setBackgroundResource(R.drawable.bg_green_btn);
            okBtn.setTextColor(mBaseFragment.getColor(R.color.common_white));
            okBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Utils.isStringNotNullOrEmpty(edReduceRate.getText().toString())){
                        VoucherLayout voucherLayout = new VoucherLayout(mBaseFragment,edReduceRate.getText().toString());
                        voucherListLayout.addView(voucherLayout);
                        voucherLayoutList.add(voucherLayout);
                        voucherListLayout.setFocusable(true);
                        voucherListLayout.setFocusableInTouchMode(true);
                        AddVoucherLayout.this.setVisibility(View.GONE);
                        if (voucherOkListener!=null)
                            voucherOkListener.onclickOk();
                    }

                }
            });

            RelativeLayout.LayoutParams currencyTextParam = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            currencyTextParam.leftMargin = mBaseFragment.getActualWidthOnThisDevice(50);
            IteeTextView currencyText = new IteeTextView(mBaseFragment.getBaseActivity());
            currencyText.setText(AppUtils.getCurrentCurrency(mBaseFragment.getBaseActivity()));
            currencyText.setGravity(Gravity.CENTER_VERTICAL);
            currencyText.setLayoutParams(currencyTextParam);
            currencyText.setTextColor(mBaseFragment.getColor(R.color.common_gray));


            this.addView(edReduceRate);
            this.addView(okBtn);
            this.addView(currencyText);
            this.setBackgroundColor(mBaseFragment.getColor(R.color.shopping_voucher_back));
        }
    }


  public   class VoucherLayout extends SwipeLinearLayout{
        private String money;
        public String getMoney() {
            return money;
        }

        private static  final  int  RIGHT_WIDTH = 150;


        public VoucherLayout(BaseFragment baseFragment,String money) {

            super(baseFragment.getBaseActivity(), mBaseFragment.getActualWidthOnThisDevice(RIGHT_WIDTH));
            this.money = money;
            LinearLayout.LayoutParams myLayout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,mBaseFragment.getActualHeightOnThisDevice(100));
            this.setLayoutParams(myLayout);
            this.setBackgroundColor(baseFragment.getColor(R.color.common_light_gray));


            LinearLayout.LayoutParams testParams = new LinearLayout.LayoutParams(AppUtils.getRightButtonWidth(mBaseFragment.getBaseActivity()), LayoutParams.MATCH_PARENT);
            LinearLayout  llDelete = new LinearLayout(mBaseFragment.getActivity());
            llDelete.setLayoutParams(testParams);
            llDelete.setBackgroundColor(Color.RED);

            LinearLayout.LayoutParams bodyParams = new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            RelativeLayout body = new RelativeLayout(baseFragment.getBaseActivity());
            AppUtils.addTopSeparatorLine(body, baseFragment.getBaseActivity());
            body.setLayoutParams(bodyParams);


            RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(mBaseFragment.getActualWidthOnThisDevice(200), LayoutParams.MATCH_PARENT);
            titleParams.leftMargin = mBaseFragment.getActualWidthOnThisDevice(40);
            IteeTextView title = new IteeTextView(mBaseFragment.getBaseActivity());
            title.setLayoutParams(titleParams);

            title.setText(mBaseFragment.getString(R.string.shopping_voucher));

            RelativeLayout.LayoutParams moneyTextParams = new RelativeLayout.LayoutParams(mBaseFragment.getActualWidthOnThisDevice(200), LayoutParams.MATCH_PARENT);
            moneyTextParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            moneyTextParams.rightMargin = mBaseFragment.getActualWidthOnThisDevice(40);
            IteeTextView moneyText = new IteeTextView(mBaseFragment.getBaseActivity());
            moneyText.setLayoutParams(moneyTextParams);

            moneyText.setText(AppUtils.getCurrentCurrency(mBaseFragment.getBaseActivity()) +Constants.STR_SPACE+Constants.STR_SEPARATOR+ money);
            moneyText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);


            body.addView(title);
            body.addView(moneyText);
            this.addView(body);
            this.addView(llDelete);

            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            IteeButton   delBtn = new IteeButton(mBaseFragment.getActivity());
            delBtn.setText(R.string.common_delete);
            delBtn.setTextColor(mBaseFragment.getColor(R.color.common_white));

            delBtn.setBackgroundResource(R.drawable.bg_common_delete);
            delBtn.setLayoutParams(btnParams);
            delBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    voucherListLayout.removeView(VoucherLayout.this);
                    voucherLayoutList.remove(VoucherLayout.this);
                    if (voucherOkListener != null)
                        voucherOkListener.onclickOk();
                }
            });

            llDelete.addView(delBtn);

        }
    }

    private  VoucherOkListener voucherOkListener;

    public VoucherOkListener getVoucherOkListener() {
        return voucherOkListener;
    }

    public void setVoucherOkListener(VoucherOkListener voucherOkListener) {
        this.voucherOkListener = voucherOkListener;
    }

    public interface VoucherOkListener{
        void onclickOk();

}
}
