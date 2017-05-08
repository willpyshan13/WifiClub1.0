package cn.situne.itee.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.jsonentity.JsonCommonProduct;



/**
 * ClassName:PricingTablePackageItem <br/>
 * Function: PricingTablePackageItem. <br/>
 * Date: 2015-07-21 <br/>
 *
 * @author luochao
 * @version 1.0
 * @see
 */

public class PricingTablePackageItem extends PricingTablePackageItemBase {

    public final static int PRICING_TABLE_PRODUCT_ITEM_MODEL_1 = 1;//11-4 model
    public final static int PRICING_TABLE_PRODUCT_ITEM_MODEL_1_GUEST = 2;//11-4 model position 2
    public final static int PRICING_TABLE_PRODUCT_ITEM_MODEL_1_MEMBER = 1;//11-4 model position 1


    public final static int PRICING_TABLE_PRODUCT_ITEM_MODEL_2 = 2;
   // public final static int PRICING_TABLE_PRODUCT_ITEM_MODEL_2_0 = 0;

    private PricingTableProductItem.ShowEditLayoutListener showEditLayoutListener;

    public PricingTableProductItem.ShowEditLayoutListener getShowEditLayoutListener() {
        return showEditLayoutListener;
    }

    public void setShowEditLayoutListener(PricingTableProductItem.ShowEditLayoutListener showEditLayoutListener) {
        this.showEditLayoutListener = showEditLayoutListener;
    }

    private String mProductMoneyType;
    private int mItemModel;
    private float mItemChildHeight;
    private IteeButton delBtn;
    private LinearLayout llDelete;

    private LinearLayout llBody;

    private ArrayList<RowLayout> rowLayoutList;

    private IteeTextView tvPackageNoePrice;

    private IteeTextView tvPackageMemberPrice;
    private IteeTextView tvPackageGuestPrice;

    private boolean mIsNonMember;


    //private List<ChildItemViews> mItemViews;

    private BaseEditFragment mBaseFragment;
    public PricingTablePackageItem(BaseEditFragment baseFragment, float itemChildHeight, int itemModel,JsonCommonProduct productData,boolean isNonMeber) {
        super(baseFragment.getActivity());
        this.mBaseFragment = baseFragment;
        mIsNonMember = isNonMeber;
        initView(productData,itemChildHeight, itemModel);
    }


    private void initView(JsonCommonProduct productData,float itemChildHeight, int itemModel) {
        this.setOrientation(LinearLayout.HORIZONTAL);

        setViewData(productData);
//        this.setTag(this.getId(), "hidden");
        this.mItemChildHeight = itemChildHeight;
        this.mItemModel = itemModel;
        mProductMoneyType = AppUtils.getCurrentCurrency(mBaseFragment.getActivity());
        rowLayoutList = new ArrayList<>();

        LinearLayout.LayoutParams bodyParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llBody = new LinearLayout(mBaseFragment.getActivity());
        llBody.setLayoutParams(bodyParams);
        llBody.setOrientation(LinearLayout.VERTICAL);
        llBody.addView(getTitleLayout());
        llBody.addView(getChildLayout());
        this.addView(llBody);
        setPackageNowPrice();
    }



    private LinearLayout getChildLayout(){
        LinearLayout.LayoutParams childLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout childLayout  = new LinearLayout(mBaseFragment.getBaseActivity());
        childLayout.setLayoutParams(childLayoutParams);
        childLayout.setOrientation(VERTICAL);


        for (JsonCommonProduct.PackageItem packageItem:getViewData().getPackageItemList()){

            //member
            if (mItemModel == PRICING_TABLE_PRODUCT_ITEM_MODEL_1) {
                childLayout.addView(getMemberItem(packageItem));
            }
            //no member
            if (mItemModel == PRICING_TABLE_PRODUCT_ITEM_MODEL_2) {
                childLayout.addView(getItem(packageItem));
            }

        }



        return childLayout;
    }
    private LinearLayout getMemberItem(JsonCommonProduct.PackageItem packageItem){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout layout = new LinearLayout(mBaseFragment.getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(layoutParams);
        layout.addView(getMemberTitleItem(packageItem.getProductName(), packageItem.getPrice(), mBaseFragment.getColor(R.color.common_light_gray), null));
        if (mIsNonMember){

            layout.addView(getMemberItemView(mBaseFragment.getString(R.string.customers_non_member), packageItem));
        }else{
            layout.addView(getMemberItemView(mBaseFragment.getString(R.string.customers_member), packageItem));
            layout.addView(getMemberItemView(mBaseFragment.getString(R.string.customers_guest), packageItem));
        }

        return layout;
    }

    private LinearLayout getMemberItemView(String title,JsonCommonProduct.PackageItem packageItem){

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        RowLayout layout = new RowLayout(mBaseFragment.getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(layoutParams);
        layout.setOnClickListener(upViewClickListener);

        LinearLayout.LayoutParams upViewParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,mBaseFragment.getActualHeightOnThisDevice(100));
        RelativeLayout upView = new RelativeLayout(mBaseFragment.getActivity());
        upView.setLayoutParams(upViewParams);
        upView.setBackgroundColor(mBaseFragment.getColor(R.color.common_light_gray));
        // left key textView
        RelativeLayout.LayoutParams tvLeftKeyLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (int) this.mItemChildHeight);
        tvLeftKeyLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, BaseFragment.LAYOUT_TRUE);
        tvLeftKeyLayoutParams.leftMargin = mBaseFragment.getActualWidthOnThisDevice(40);
        IteeTextView tvLeftKey = generateTextView(tvLeftKeyLayoutParams, mBaseFragment.getColor(R.color.common_black));
        tvLeftKey.setId(View.generateViewId());
        upView.addView(tvLeftKey);
        tvLeftKey.setText(title);
        layout.addView(upView);

        //current
        RelativeLayout.LayoutParams currentPriceLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        currentPriceLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        IteeTextView tvCurrentPrice = generateTextView(currentPriceLayoutParams, mBaseFragment.getResources().getColor(R.color.common_black));
        tvCurrentPrice.setId(View.generateViewId());
        tvCurrentPrice.setPadding(0, 0, mBaseFragment.getActualWidthOnThisDevice(40), 0);
        tvCurrentPrice.setGravity(Gravity.CENTER_VERTICAL);


        upView.addView(tvCurrentPrice);
        //tvCurrentPrice.setText(AppUtils.getCurrentCurrency(mBaseFragment.getBaseActivity()) + "777");



        RelativeLayout.LayoutParams tvDiscountLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        tvDiscountLayoutParams.addRule(RelativeLayout.LEFT_OF, tvCurrentPrice.getId());
        tvDiscountLayoutParams.setMargins(0, 0, 15, 0);
        tvDiscountLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        IteeTextView tvDiscount = generateTextView(currentPriceLayoutParams, mBaseFragment.getResources().getColor(R.color.common_red));
        upView.addView(tvDiscount);
        tvDiscount.setLayoutParams(tvDiscountLayoutParams);
       // tvDiscount.setText("666");

        tvDiscount.setGravity(Gravity.CENTER_VERTICAL);

        layout.setPackageItem(packageItem);
        setEditView(0, layout);

        AppUtils.addTopSeparatorLine(upView, mBaseFragment);


        String discount = packageItem.getGuestProductDiscount();

        String discountType = packageItem.getGuestProductDiscountType();
        layout.setRowModel(PRICING_TABLE_PRODUCT_ITEM_MODEL_1_GUEST);

        if (title.equals(mBaseFragment.getString(R.string.customers_member))){
            discount = packageItem.getMemberProductDiscount();
            discountType= packageItem.getMemberProductDiscountType();
            layout.setRowModel(PRICING_TABLE_PRODUCT_ITEM_MODEL_1_MEMBER);
        }
        double discountD = 0;
        if (Utils.isStringNotNullOrEmpty(discount))
            discountD = Double.parseDouble(discount);

        String originalCost = packageItem.getPrice();
        double originalCostD = 0;
        if (Utils.isStringNotNullOrEmpty(originalCost))
            originalCostD = Double.parseDouble(originalCost);


        String currentPrice;
        if (Constants.MONEY_DISCOUNT_MONEY.equals(discountType)){
            currentPrice = Utils.get2DigitDecimalString(String.valueOf(originalCostD - discountD));
        }else{
            currentPrice = Utils.get2DigitDecimalString(String.valueOf(originalCostD - (originalCostD*(discountD/100))));

        }

        tvCurrentPrice.setText(AppUtils.getCurrentCurrency(mBaseFragment.getBaseActivity()) +Constants.STR_SPACE+ currentPrice);
        setT3Value(tvDiscount, discount, discountType);
        layout.setTvCurrentPrice(tvCurrentPrice);
        layout.setTvCurrentPrice(tvCurrentPrice);
        layout.setTvDiscount(tvDiscount);
        rowLayoutList.add(layout);
        return layout;

    }


    private LinearLayout getItem(JsonCommonProduct.PackageItem packageItem){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        RowLayout layout = new RowLayout(mBaseFragment.getActivity());

        layout.setRowModel(PRICING_TABLE_PRODUCT_ITEM_MODEL_1_GUEST);

        //layout.setPadding(40, 0, 40, 0);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(layoutParams);
        layout.setOnClickListener(upViewClickListener);
        // Layout  if haveEditView  it's upView
        RelativeLayout layout_1 = new RelativeLayout(mBaseFragment.getActivity());
        layout_1.setBackgroundColor(mBaseFragment.getColor(R.color.common_white));
        // left key textView
        RelativeLayout.LayoutParams tvLeftKeyLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (int) this.mItemChildHeight);
        tvLeftKeyLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, BaseFragment.LAYOUT_TRUE);
        tvLeftKeyLayoutParams.leftMargin = mBaseFragment.getActualWidthOnThisDevice(40);
        TextView tvLeftKey = generateTextView(tvLeftKeyLayoutParams, mBaseFragment.getColor(R.color.common_black));
        tvLeftKey.setId(View.generateViewId());
        layout_1.addView(tvLeftKey);
        //layout_1.setTag(position);
        tvLeftKey.setText(packageItem.getProductName());
        // right layout
        //cost price
        int textHeight = mBaseFragment.getActualWidthOnThisDevice(50);
        RelativeLayout.LayoutParams tvCostPriceLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, textHeight);
        tvCostPriceLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, BaseFragment.LAYOUT_TRUE);
        IteeTextView tvCostPrice = generateTextView(tvCostPriceLayoutParams, mBaseFragment.getColor(R.color.common_black));
        tvCostPrice.setId(View.generateViewId());
        tvCostPrice.setPadding(0, 0, mBaseFragment.getActualWidthOnThisDevice(40), 0);
        layout_1.addView(tvCostPrice);
        tvCostPrice.setText(AppUtils.getCurrentCurrency(mBaseFragment.getBaseActivity()) + Constants.STR_SPACE + packageItem.getPrice());
        //current
        RelativeLayout.LayoutParams currentPriceLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, textHeight);
        currentPriceLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        currentPriceLayoutParams.addRule(RelativeLayout.BELOW, tvCostPrice.getId());
        IteeTextView tvCurrentPrice = generateTextView(currentPriceLayoutParams, mBaseFragment.getResources().getColor(R.color.common_black));
        tvCurrentPrice.setId(View.generateViewId());
        tvCurrentPrice.setPadding(0, 0, mBaseFragment.getActualWidthOnThisDevice(40), 0);

        layout_1.addView(tvCurrentPrice);
        //discount
        RelativeLayout.LayoutParams tvDiscountLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, textHeight);
        tvDiscountLayoutParams.addRule(RelativeLayout.LEFT_OF, tvCurrentPrice.getId());
        tvDiscountLayoutParams.addRule(RelativeLayout.BELOW, tvCostPrice.getId());
        tvDiscountLayoutParams.setMargins(0, 0, 15, 0);

        IteeTextView tvDiscount = generateTextView(currentPriceLayoutParams, mBaseFragment.getResources().getColor(R.color.common_red));
        layout_1.addView(tvDiscount);
        tvDiscount.setLayoutParams(tvDiscountLayoutParams);

       // tvDiscount.setText("666");
        layout.addView(layout_1);
        // if haveEditView  it's downView
        layout.addView(AppUtils.getSeparatorLine(mBaseFragment));
       // setEditView(position, layout);// TODO
        layout.setPackageItem(packageItem);
        setEditView(0, layout);
        layout_1.setBackgroundColor(mBaseFragment.getColor(R.color.common_light_gray));
        String discount = packageItem.getGuestProductDiscount();
        double discountD = 0;
        if (Utils.isStringNotNullOrEmpty(discount))
            discountD = Double.parseDouble(discount);
        String originalCost = packageItem.getPrice();
        double originalCostD = 0;
        if (Utils.isStringNotNullOrEmpty(originalCost))
            originalCostD = Double.parseDouble(originalCost);
        String strT2Key;
        if (Constants.MONEY_DISCOUNT_MONEY.equals(packageItem.getGuestProductDiscountType())){
            strT2Key = Utils.get2DigitDecimalString(String.valueOf(originalCostD - discountD));
        }else{
            strT2Key = Utils.get2DigitDecimalString(String.valueOf(originalCostD - (originalCostD*(discountD/100))));

        }


        tvCurrentPrice.setText(AppUtils.getCurrentCurrency(mBaseFragment.getBaseActivity()) +Constants.STR_SPACE+ strT2Key);
        setT3Value(tvDiscount, discount,packageItem.getGuestProductDiscountType());
        layout.setTvCurrentPrice(tvCurrentPrice);
        layout.setTvDiscount(tvDiscount);


        setPackageSwMaxValue(layout);
        rowLayoutList.add(layout);
        return layout;

    }
    private void setT3Value(IteeTextView t3, String value,String discountType) {
        if (Constants.STR_0.equals(value)||Utils.isStringNullOrEmpty(value)){

            t3.setVisibility(View.INVISIBLE);
        }else{
            t3.setVisibility(View.VISIBLE);

        }
        if (Utils.isStringNotNullOrEmpty(value)) {

            if (Constants.MONEY_DISCOUNT_MONEY.equals(discountType)){
                t3.setText(mProductMoneyType + Constants.STR_MINUS_MONEY + Utils.get2DigitDecimalString(value));
            }else{
                t3.setText(Utils.get2DigitDecimalString(value) + Constants.STR_PER_CENT + Constants.STR_SPACE + Constants.STR_OFF);
            }

        } else {
            t3.setText(Constants.STR_0);
        }
    }
    private void setEditView(int position, RowLayout parentLayout) {
        //editView
        LinearLayout.LayoutParams editViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) mItemChildHeight);
        RelativeLayout editViewContainer = new RelativeLayout(mBaseFragment.getActivity());
        editViewContainer.setVisibility(View.GONE);


        editViewContainer.setBackgroundResource(R.color.common_fleet_blue);
        editViewContainer.setLayoutParams(editViewLayoutParams);

        //editView  CheckSwitchButton
        RelativeLayout.LayoutParams editViewSwCurrencyParam = new RelativeLayout.LayoutParams(mBaseFragment.getActualWidthOnThisDevice(160), mBaseFragment.getActualHeightOnThisDevice(80));
        editViewSwCurrencyParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        editViewSwCurrencyParam.addRule(RelativeLayout.CENTER_VERTICAL);
        editViewSwCurrencyParam.leftMargin = mBaseFragment.getActualWidthOnThisDevice(20);
        editViewSwCurrencyParam.rightMargin = mBaseFragment.getActualWidthOnThisDevice(10);
        CheckSwitchButton editViewSwCurrency = new CheckSwitchButton(mBaseFragment, CheckSwitchButton.TYPE_DISCOUNT_OR_CURRENCY);
        editViewSwCurrency.setLayoutParams(editViewSwCurrencyParam);
        editViewSwCurrency.setId(View.generateViewId());
        editViewSwCurrency.setOnCheckedChangeListener(checkBtnChangeListener);
        editViewSwCurrency.setTag(position);


        //editView  EditText
        RelativeLayout.LayoutParams reduceRateParam = new RelativeLayout.LayoutParams(mBaseFragment.getActualWidthOnThisDevice(400), mBaseFragment.getActualHeightOnThisDevice(80));
        reduceRateParam.addRule(RelativeLayout.RIGHT_OF, editViewSwCurrency.getId());
        reduceRateParam.addRule(RelativeLayout.CENTER_VERTICAL);
        reduceRateParam.leftMargin = mBaseFragment.getActualWidthOnThisDevice(10);
        reduceRateParam.setMargins(0, 15, 0, 0);

        EditText edReduceRate = new EditText(mBaseFragment.getActivity());
        edReduceRate.setLayoutParams(reduceRateParam);
        edReduceRate.setBackground(mBaseFragment.getResources().getDrawable(R.drawable.textview_corner));
        edReduceRate.setRawInputType(Configuration.KEYBOARD_12KEY);
        edReduceRate.setId(View.generateViewId());
        edReduceRate.setTag(position);
        edReduceRate.setPadding(10, 5, 0, 0);


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
        RelativeLayout.LayoutParams okBtnParam = new RelativeLayout.LayoutParams(mBaseFragment.getActualWidthOnThisDevice(100), mBaseFragment.getActualWidthOnThisDevice(80));
        okBtnParam.addRule(RelativeLayout.RIGHT_OF, edReduceRate.getId());
        okBtnParam.addRule(RelativeLayout.CENTER_VERTICAL);
        IteeButton okBtn = new IteeButton(mBaseFragment.getActivity());
        okBtn.setText(R.string.common_ok);
        okBtn.setTextSize(Constants.FONT_SIZE_SMALLER);
        okBtn.setSingleLine();
        okBtn.setTag(position);
        okBtn.setLayoutParams(okBtnParam);
        okBtn.setOnClickListener(okBtnClickListener);
        okBtn.setBackgroundResource(R.drawable.bg_pricing_ok_btn);
        okBtn.setTextColor(mBaseFragment.getColor(R.color.common_white));


        //add layout
        editViewContainer.addView(editViewSwCurrency);
        editViewContainer.addView(edReduceRate);
        editViewContainer.addView(okBtn);

        parentLayout.setOkBtn(okBtn);
        parentLayout.setEditTextWatcher(editTextWatcher);
        parentLayout.setEdReduceRate(edReduceRate);
        parentLayout.setEditViewContainer(editViewContainer);
        parentLayout.setEditViewSwCurrency(editViewSwCurrency);
        parentLayout.addView(editViewContainer);




        String guestDiscountType = parentLayout.getPackageItem().getGuestProductDiscountType();
        if (Constants.MONEY_DISCOUNT_MONEY.equals(guestDiscountType)){
            editViewSwCurrency.setChecked(false);
        }else{
            editViewSwCurrency.setChecked(true);
        }

    }

    public void setDelListener(OnClickListener delListener) {

        delBtn.setOnClickListener(delListener);
    }

    private SwipeLinearLayout titleLayout;

    public SwipeLinearLayout getSwipeLinearLayout() {
        return titleLayout;
    }

    private LinearLayout getTitleLayout(){
        LinearLayout.LayoutParams titleLayoutParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
         titleLayout
                = new SwipeLinearLayout(mBaseFragment.getBaseActivity(), AppUtils.getRightButtonWidth(mBaseFragment.getActivity()));
        LinearLayout.LayoutParams titleBodyParams
                = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout titleBody = new LinearLayout(mBaseFragment.getBaseActivity());
        titleLayout.setLayoutParams(titleLayoutParams);
        titleLayout.setOrientation(HORIZONTAL);
        titleBody.setLayoutParams(titleBodyParams);
        titleBody.setOrientation(VERTICAL);
        //member
        if (mItemModel == PRICING_TABLE_PRODUCT_ITEM_MODEL_1) {
            RelativeLayout.LayoutParams t1LayoutParams
                    = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, mBaseFragment.getActualHeightOnThisDevice(100));
            t1LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, BaseFragment.LAYOUT_TRUE);
            tvPackageMemberPrice = generateTextView(t1LayoutParams, mBaseFragment.getColor(R.color.common_black));
            RelativeLayout.LayoutParams t2LayoutParams
                    = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, mBaseFragment.getActualHeightOnThisDevice(100));
            t2LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, BaseFragment.LAYOUT_TRUE);
            tvPackageGuestPrice = generateTextView(t2LayoutParams, mBaseFragment.getColor(R.color.common_black));
            titleBody.addView(getMemberTitleItem(getViewData().getProductName(), getViewData().getProductOriginalCost(), mBaseFragment.getColor(R.color.common_white), null));
            if (mIsNonMember){
                titleBody.addView( getMemberTitleItem(mBaseFragment.getString(R.string.customers_non_member), Constants.STR_0, mBaseFragment.getColor(R.color.common_white),tvPackageGuestPrice));
            }else{
                titleBody.addView( getMemberTitleItem(mBaseFragment.getString(R.string.customers_member),Constants.STR_0,mBaseFragment.getColor(R.color.common_white),tvPackageMemberPrice));
                titleBody.addView( getMemberTitleItem(mBaseFragment.getString(R.string.customers_guest), Constants.STR_0, mBaseFragment.getColor(R.color.common_white),tvPackageGuestPrice));
            }
        }
        //no member
        if (mItemModel == PRICING_TABLE_PRODUCT_ITEM_MODEL_2) {

            RelativeLayout layout_1 = new RelativeLayout(mBaseFragment.getActivity());

            layout_1.setBackgroundColor(mBaseFragment.getColor(R.color.common_white));

            // left key textView
            RelativeLayout.LayoutParams tvLeftKeyLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (int) this.mItemChildHeight);
            tvLeftKeyLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, BaseFragment.LAYOUT_TRUE);
            tvLeftKeyLayoutParams.leftMargin = mBaseFragment.getActualWidthOnThisDevice(40);
            TextView tvLeftKey = generateTextView(tvLeftKeyLayoutParams, mBaseFragment.getColor(R.color.common_black));
            tvLeftKey.setId(View.generateViewId());
            layout_1.addView(tvLeftKey);
            //layout_1.setTag(position);

            tvLeftKey.setText(getViewData().getProductName());


            // right layout

            int textHeight = (int) this.mItemChildHeight;
            if (mItemModel == PRICING_TABLE_PRODUCT_ITEM_MODEL_2) {
                textHeight = textHeight / 2;
            }
            RelativeLayout.LayoutParams t1LayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, textHeight);
            t1LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, BaseFragment.LAYOUT_TRUE);
            TextView t1 = generateTextView(t1LayoutParams, mBaseFragment.getColor(R.color.common_black));
            t1.setId(View.generateViewId());
            t1.setPadding(0, 0, mBaseFragment.getActualWidthOnThisDevice(40), 0);
            layout_1.addView(t1);
            t1.setText(AppUtils.getCurrentCurrency(mBaseFragment.getBaseActivity())+Constants.STR_SPACE + getViewData().getProductOriginalCost());
            titleBody.addView(layout_1);

            RelativeLayout.LayoutParams ttvPackageNoePriceParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, textHeight);
            ttvPackageNoePriceParams.addRule(RelativeLayout.BELOW, t1.getId());
            ttvPackageNoePriceParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, BaseFragment.LAYOUT_TRUE);
            ttvPackageNoePriceParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            tvPackageNoePrice = generateTextView(ttvPackageNoePriceParams, mBaseFragment.getResources().getColor(R.color.common_black));
            tvPackageNoePrice.setPadding(0, 0, mBaseFragment.getActualWidthOnThisDevice(40), 0);
            layout_1.addView(tvPackageNoePrice);
            tvPackageNoePrice.setLayoutParams(ttvPackageNoePriceParams);




        }
        titleBody.addView(AppUtils.getSeparatorLine(mBaseFragment));

        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        delBtn = new IteeButton(mBaseFragment.getActivity());
        delBtn.setText(R.string.common_delete);
        delBtn.setTextColor(mBaseFragment.getColor(R.color.common_white));

        delBtn.setBackgroundResource(R.drawable.bg_common_delete);
        delBtn.setLayoutParams(btnParams);
        delBtn.setTag(this);
        LinearLayout.LayoutParams testParams = new LinearLayout.LayoutParams(AppUtils.getRightButtonWidth(getContext()), LayoutParams.MATCH_PARENT);
        llDelete = new LinearLayout(mBaseFragment.getActivity());
        llDelete.setLayoutParams(testParams);
        llDelete.setBackgroundColor(Color.RED);
        llDelete.addView(delBtn);
        titleLayout.addView(titleBody);
        titleLayout.addView(llDelete);




        return titleLayout;

    }
    private IteeTextView generateTextView(RelativeLayout.LayoutParams textKayLayoutParam, int textColor) {
        IteeTextView text = new IteeTextView(mBaseFragment.getActivity());
        text.setTextSize(Constants.FONT_SIZE_SMALLER);
        text.setTextColor(textColor);
        text.setGravity(Gravity.CENTER_VERTICAL);
        text.setLayoutParams(textKayLayoutParam);

        return text;
    }

    private RelativeLayout getMemberTitleItem(String title ,String money,int color,IteeTextView textView){
        RelativeLayout layout_1 = new RelativeLayout(mBaseFragment.getActivity());
        layout_1.setBackgroundColor(mBaseFragment.getColor(R.color.common_white));
        // left key textView
        RelativeLayout.LayoutParams tvLeftKeyLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (int) this.mItemChildHeight);
        tvLeftKeyLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, BaseFragment.LAYOUT_TRUE);
        tvLeftKeyLayoutParams.leftMargin = mBaseFragment.getActualWidthOnThisDevice(40);
        TextView tvLeftKey = generateTextView(tvLeftKeyLayoutParams, mBaseFragment.getColor(R.color.common_black));
        tvLeftKey.setId(View.generateViewId());
        layout_1.addView(tvLeftKey);
        //layout_1.setTag(position);

        tvLeftKey.setText(title);
        int textHeight = (int) this.mItemChildHeight;
        if (mItemModel == PRICING_TABLE_PRODUCT_ITEM_MODEL_2) {
            textHeight = textHeight / 2;
        }
        RelativeLayout.LayoutParams t1LayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, textHeight);
        t1LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, BaseFragment.LAYOUT_TRUE);


        if (textView == null){
            textView = generateTextView(t1LayoutParams, mBaseFragment.getColor(R.color.common_black));

        }
        textView.setId(View.generateViewId());
        textView.setPadding(0, 0, mBaseFragment.getActualWidthOnThisDevice(40), 0);
        layout_1.addView(textView);
        textView.setText(money);
        layout_1.setBackgroundColor(color);
        AppUtils.addTopSeparatorLine(layout_1,mBaseFragment);





        return layout_1;
    }

    class RowLayout extends LinearLayout{

        private IteeTextView tvCurrentPrice;
        private IteeTextView tvDiscount;
        private RelativeLayout editViewContainer;
        private Button okBtn;
        private EditText edReduceRate;
        private CheckSwitchButton editViewSwCurrency;
        private  AppUtils.EditViewMoneyWatcher editTextWatcher;
        private JsonCommonProduct.PackageItem packageItem;//TODO

        private int rowModel;
        public RowLayout(Context context) {
            super(context);
        }

        public int getRowModel() {
            return rowModel;
        }

        public void setRowModel(int rowModel) {
            this.rowModel = rowModel;
        }

        public EditText getEdReduceRate() {
            return edReduceRate;
        }

        public void setEdReduceRate(EditText edReduceRate) {
            this.edReduceRate = edReduceRate;
        }

        public Button getOkBtn() {
            return okBtn;
        }

        public void setOkBtn(Button okBtn) {
            this.okBtn = okBtn;
        }

        public RelativeLayout getEditViewContainer() {
            return editViewContainer;
        }

        public void setEditViewContainer(RelativeLayout editViewContainer) {
            this.editViewContainer = editViewContainer;
        }

        public CheckSwitchButton getEditViewSwCurrency() {
            return editViewSwCurrency;
        }

        public void setEditViewSwCurrency(CheckSwitchButton editViewSwCurrency) {
            this.editViewSwCurrency = editViewSwCurrency;
        }

        public IteeTextView getTvCurrentPrice() {
            return tvCurrentPrice;
        }

        public void setTvCurrentPrice(IteeTextView tvCurrentPrice) {
            this.tvCurrentPrice = tvCurrentPrice;
        }

        public IteeTextView getTvDiscount() {
            return tvDiscount;
        }

        public void setTvDiscount(IteeTextView tvDiscount) {
            this.tvDiscount = tvDiscount;
        }

        public AppUtils.EditViewMoneyWatcher getEditTextWatcher() {
            return editTextWatcher;
        }

        public void setEditTextWatcher(AppUtils.EditViewMoneyWatcher editTextWatcher) {
            this.editTextWatcher = editTextWatcher;
        }

        public JsonCommonProduct.PackageItem getPackageItem() {
            return packageItem;
        }

        public void setPackageItem(JsonCommonProduct.PackageItem packageItem) {
            this.packageItem = packageItem;
        }
    }


    private OnClickListener upViewClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mBaseFragment.getFragmentMode() != BaseEditFragment.FragmentMode.FragmentModeBrowse) {
                RowLayout layout = (RowLayout) v;
                RelativeLayout editView = layout.getEditViewContainer();
                if (editView != null) {
                    editView.requestFocus();
                    if (editView.getVisibility() == View.VISIBLE) {
                        editView.setVisibility(View.GONE);
                    } else {
                        editView.setVisibility(View.VISIBLE);
                    }

                    if (showEditLayoutListener!=null){
                        showEditLayoutListener.showEditLayout(editView);
                    }
                }
            }
        }


    };

    private RowLayout findRowOfOkBtn(View btn){
        for (RowLayout rowLayout:rowLayoutList){

           if (btn.equals(rowLayout.getOkBtn())){

               return rowLayout;
           }

        }

        return null;
    }

    private RowLayout findRowOfCheckSwitchButton(View btn){
        for (RowLayout rowLayout:rowLayoutList){

            if (btn.equals(rowLayout.getEditViewSwCurrency())){

                return rowLayout;
            }

        }

        return null;
    }


    private OnClickListener okBtnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            RowLayout row = findRowOfOkBtn(v);
            EditText editTextView = row.getEdReduceRate();

            if (editTextView!=null){
                String editText = getMoneyTypeTextString(editTextView);
                if (Utils.isStringNotNullOrEmpty(editText)) {
                    Float d = Float.valueOf(editText);
                    editText = Utils.get2DigitDecimalString(String.valueOf(d));
                }
                String originalCost = row.getPackageItem().getPrice();
                String  discountType = getSwBtnDiscountType(row.getEditViewSwCurrency());
                String nowCost = getNowCost(discountType, originalCost, editText);
                if (row.getRowModel() == PRICING_TABLE_PRODUCT_ITEM_MODEL_1_MEMBER) {
                    if (row.getEditViewSwCurrency().isChecked()){
                        row.getPackageItem().setMemberProductDiscountType(Constants.MONEY_DISCOUNT_PERCENT);
                    }else{
                        row.getPackageItem().setMemberProductDiscountType(Constants.MONEY_DISCOUNT_MONEY);

                    }
                    row.getPackageItem().setMemberProductDiscount(editText);
                }else{
                    if (row.getEditViewSwCurrency().isChecked()){
                        row.getPackageItem().setGuestProductDiscountType(Constants.MONEY_DISCOUNT_PERCENT);
                    }else{
                        row.getPackageItem().setGuestProductDiscountType(Constants.MONEY_DISCOUNT_MONEY);

                    }
                    row.getPackageItem().setGuestProductDiscount(editText);
                }
                row.setFocusableInTouchMode(true);
                row.setFocusable(true);
                editTextView.clearFocus();

                setEdReduceRateText(editTextView, editText, discountType);

                setMoneyTypeText(row.getTvCurrentPrice(), nowCost);
                setT3Value(row.getTvDiscount(), editText,discountType);

                Utils.hideKeyboard(mBaseFragment.getActivity());
                setPackageNowPrice();
            }

        }


    };




    private void setPackageNowPrice(){
        if (mItemModel == PRICING_TABLE_PRODUCT_ITEM_MODEL_1) {

            double sumMemberMoney = 0;
            double sumGuestMoney = 0;


            for (JsonCommonProduct.PackageItem packageItem:getViewData().getPackageItemList()){
                double itemPrice = doubleToString(packageItem.getPrice());
                double memberDiscount = doubleToString(packageItem.getMemberProductDiscount());
                double guestDiscount = doubleToString(packageItem.getGuestProductDiscount());



                String memberDiscountType = packageItem.getMemberProductDiscountType();
                String guestDiscountType = packageItem.getGuestProductDiscountType();
                if (Constants.MONEY_DISCOUNT_MONEY.equals(memberDiscountType)){
                    sumMemberMoney += (itemPrice - memberDiscount);
                }else{
                    sumMemberMoney += (itemPrice - (itemPrice * memberDiscount/100));
                }
                if (Constants.MONEY_DISCOUNT_MONEY.equals(guestDiscountType)){
                    sumGuestMoney += (itemPrice - guestDiscount);

                }else{
                    sumGuestMoney += (itemPrice - (itemPrice * guestDiscount/100));
                }
            }
            tvPackageGuestPrice.setText(AppUtils.getCurrentCurrency(mBaseFragment.getBaseActivity())+Constants.STR_SPACE+Utils.get2DigitDecimalString(String.valueOf(sumGuestMoney)));
            tvPackageMemberPrice.setText(AppUtils.getCurrentCurrency(mBaseFragment.getBaseActivity())+Constants.STR_SPACE+Utils.get2DigitDecimalString(String.valueOf(sumMemberMoney)));
        }
        if (mItemModel == PRICING_TABLE_PRODUCT_ITEM_MODEL_2) {
            double sumMoney = 0;
            for (JsonCommonProduct.PackageItem packageItem:getViewData().getPackageItemList()){
                double itemPrice =doubleToString(packageItem.getPrice());

                double guestDiscount = doubleToString(packageItem.getGuestProductDiscount());
                String guestDiscountType = packageItem.getGuestProductDiscountType();

                if (Constants.MONEY_DISCOUNT_MONEY.equals(guestDiscountType)){
                    sumMoney += (itemPrice - guestDiscount);

                }else{
                    sumMoney += (itemPrice - (itemPrice * guestDiscount/100));

                }

            }
            tvPackageNoePrice.setText(AppUtils.getCurrentCurrency(mBaseFragment.getBaseActivity())+Constants.STR_SPACE + Utils.get2DigitDecimalString(String.valueOf(sumMoney)));
        }
    }

    private CompoundButton.OnCheckedChangeListener checkBtnChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            RowLayout rowLayout = findRowOfCheckSwitchButton(buttonView);

            if (rowLayout!=null){
                String edReduceValue = getMoneyTypeTextString(rowLayout.getEdReduceRate());
                double edReduceNum = 0;
                double originalCost = 0;
                try {
                    originalCost = Double.parseDouble(rowLayout.getPackageItem().getPrice());
                } catch (NumberFormatException e) {
                    Utils.log(e.getMessage());
                }
                try {
                    edReduceNum = Double.parseDouble(edReduceValue);
                } catch (NumberFormatException e) {
                    Utils.log(e.getMessage());
                }

                //String moneyDefault = Constants.MONEY_DISCOUNT_MONEY;
                if (!isChecked) {//left   money
                    if (edReduceNum > originalCost) {
                        setEdReduceRateText(rowLayout.getEdReduceRate(),
                                String.valueOf(originalCost),Constants.MONEY_DISCOUNT_MONEY);
                    }
                } else {// right  %
                    if (edReduceNum > Constants.MAX_PERCENT_VALUE) {
                        setEdReduceRateText(rowLayout.getEdReduceRate(),
                                String.valueOf(Constants.MAX_PERCENT_VALUE),Constants.MONEY_DISCOUNT_PERCENT);
                    }

                   // moneyDefault = Constants.MONEY_DISCOUNT_PERCENT;
                }
//
//            if (position == PRICING_TABLE_PRODUCT_ITEM_MODEL_1_MEMBER) {
//                productData.setMemberTempProductMoneyDefault(moneyDefault);
//                productData.setMemberMoneyDefault(moneyDefault);
//            }
//            if (position == PRICING_TABLE_PRODUCT_ITEM_MODEL_1_GUEST) {
//                productData.setGuestTempProductMoneyDefault(moneyDefault);
//                productData.setGuestMoneyDefault(moneyDefault);
//            }
//            if (position == PRICING_TABLE_PRODUCT_ITEM_MODEL_2_0) {
////                productData.setTempProductMoneyDefault(moneyDefault);
////                productData.setProductMoneyDefault(moneyDefault);
//
//                // getViewData().setGuestDiscountType(moneyDefault);
//            }


                setPackageSwMaxValue(rowLayout);

            }


        }
    };


    public void setEdReduceRateText(EditText edReduceRate, String value ,String discountType) {

        if (Utils.isStringNotNullOrEmpty(value)) {
            if (Constants.MONEY_DISCOUNT_MONEY.equals(discountType)){
                edReduceRate.setText(AppUtils.getCurrentCurrency(mBaseFragment.getBaseActivity())+ Utils.get2DigitDecimalString(value));
            }else{
                edReduceRate.setText(Utils.get2DigitDecimalString(value));
            }

        } else {
            edReduceRate.setText(Constants.STR_0);
        }
    }

}
