/**
 * Project Name: itee
 * File Name:	 PricingTableProductItem.java
 * Package Name: cn.situne.itee.fragment.customers
 * Date:		 2015-03-24
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.view;

import android.content.res.Configuration;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.jsonentity.JsonCommonProduct;

/**
 * ClassName:PricingTableProductItem <br/>
 * Function: Pricing Table Product Item. <br/>
 * Date: 2015-03-24 <br/>
 *
 * @author luochao
 * @version 1.0
 * @see
 */
public class PricingTableProductItem extends PricingTableItemBase {

    public final static int PRICING_TABLE_PRODUCT_ITEM_MODEL_1 = 1;//11-4 model
    public final static int PRICING_TABLE_PRODUCT_ITEM_MODEL_1_GUEST = 2;//11-4 model position 2
    public final static int PRICING_TABLE_PRODUCT_ITEM_MODEL_1_MEMBER = 1;//11-4 model position 1


    public final static int PRICING_TABLE_PRODUCT_ITEM_MODEL_2 = 2;
    public final static int PRICING_TABLE_PRODUCT_ITEM_MODEL_2_0 = 0;

    private ShowEditLayoutListener showEditLayoutListener;



    private String mProductMoneyType;
    private int mItemModel;
    private float mItemChildHeight;
    private IteeButton delBtn;
    private LinearLayout llDelete;

    private LinearLayout llBody;

    private LinearLayout memberLayout;

    private List<ChildItemViews> mItemViews;

    private BaseEditFragment mBaseFragment;

    public ShowEditLayoutListener getShowEditLayoutListener() {
        return showEditLayoutListener;
    }

    public void setShowEditLayoutListener(ShowEditLayoutListener showEditLayoutListener) {
        this.showEditLayoutListener = showEditLayoutListener;
    }

    private boolean mIsNonMember;

    //editViewFocusChangeListener
    private OnFocusChangeListener editViewFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            int position = (int) v.getTag();
            ChildItemViews childItemViews = mItemViews.get(position);

            if (!hasFocus) {
                if (position == PRICING_TABLE_PRODUCT_ITEM_MODEL_1_MEMBER) {
                    getViewData().setMemberTempMoneyEditText(childItemViews.getEdReduceRate().getText().toString());
                }
                if (position == PRICING_TABLE_PRODUCT_ITEM_MODEL_1_GUEST) {
                    getViewData().setGuestTempMoneyEditText(childItemViews.getEdReduceRate().getText().toString());
                }
                if (position == PRICING_TABLE_PRODUCT_ITEM_MODEL_2_0) {
                    getViewData().setTempMoneyEditText(childItemViews.getEdReduceRate().getText().toString());
                }
            } else {
                String value = childItemViews.getEdReduceRate().getText().toString();
                if (value.contains(mProductMoneyType)) {
                    value = value.substring(1, value.length());
                }
                childItemViews.getEdReduceRate().setText(value);

                EditText e = (EditText) v;
                String m = e.getText().toString();
                double editData = 0;
                try {
                    editData = Double.parseDouble(m);
                } catch (NumberFormatException error) {
                    Utils.log(error.getMessage());
                }

                if (editData == 0) {
                    e.setText(Constants.STR_EMPTY);
                }


            }
        }
    };
    //ok
    private OnClickListener okBtnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            int position = (int) v.getTag();
            ChildItemViews childItemViews = mItemViews.get(position);
            EditText editTextView = childItemViews.getEdReduceRate();
            String editText = getMoneyTypeTextString(editTextView);
            if (Utils.isStringNotNullOrEmpty(editText)) {
                Float d = Float.valueOf(editText);
                editText = Utils.get2DigitDecimalString(String.valueOf(d));
            }
            String prizeMoney = getViewData().getProductOriginalCost();
            String direction = getSwBtnDirection(childItemViews.getEditViewSwCurrency());


            String nowCost = getNowCost(direction, prizeMoney, editText);



            if (position == PRICING_TABLE_PRODUCT_ITEM_MODEL_1_MEMBER) {
                if ( childItemViews.getEditViewSwCurrency().isChecked()){

                    getViewData().setMemberDiscountType(Constants.MONEY_DISCOUNT_PERCENT);
                }else{
                    getViewData().setMemberDiscountType(Constants.MONEY_DISCOUNT_MONEY);

                }
                getViewData().setMemberProductDiscount(editText);

            }
            if (position == PRICING_TABLE_PRODUCT_ITEM_MODEL_1_GUEST) {
                if ( childItemViews.getEditViewSwCurrency().isChecked()){

                    getViewData().setGuestDiscountType(Constants.MONEY_DISCOUNT_PERCENT);
                }else{
                    getViewData().setGuestDiscountType(Constants.MONEY_DISCOUNT_MONEY);

                }
                getViewData().setGuestProductDiscount(editText);
            }

            if (position == PRICING_TABLE_PRODUCT_ITEM_MODEL_2_0) {

               if ( childItemViews.getEditViewSwCurrency().isChecked()){

                   getViewData().setGuestDiscountType(Constants.MONEY_DISCOUNT_PERCENT);
               }else{
                   getViewData().setGuestDiscountType(Constants.MONEY_DISCOUNT_MONEY);

               }
                getViewData().setGuestProductDiscount(editText);
            }
            childItemViews.getEditView().setFocusableInTouchMode(true);
            childItemViews.getEditView().setFocusable(true);
            editTextView.clearFocus();

            setEdReduceRateText(childItemViews, editText);
            setMoneyTypeText(childItemViews.getTv2(), nowCost);

            setT3Value(childItemViews, editText);
            Utils.hideKeyboard(mBaseFragment.getActivity());

        }
    };
    //layout_1  upView
    private OnClickListener upViewClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            if (Constants.STR_0.equals(getViewData().getPackageId()) && mBaseFragment.getFragmentMode() != BaseEditFragment.FragmentMode.FragmentModeBrowse) {



                int position = (int) v.getTag();
                ChildItemViews childItemViews = mItemViews.get(position);
                RelativeLayout editView = childItemViews.getEditView();
                if (editView != null && PricingTableProductItem.this.getTag(PricingTableProductItem.this.getId()).equals("hidden")) {
                    boolean isShow = true;
                    editView.requestFocus();
                    if (editView.getVisibility() == View.VISIBLE) {
                        editView.setVisibility(View.GONE);
                        isShow = false;
                    } else {
                        editView.setVisibility(View.VISIBLE);
                    }

                    if (position == PRICING_TABLE_PRODUCT_ITEM_MODEL_1_MEMBER) {
                        getViewData().setMemberEditViewShow(isShow);
                    }
                    if (position == PRICING_TABLE_PRODUCT_ITEM_MODEL_1_GUEST) {
                        getViewData().setGuestEditViewShow(isShow);
                    }
                    if (position == PRICING_TABLE_PRODUCT_ITEM_MODEL_2_0) {
                        getViewData().setShowDown(isShow);
                    }

                    if (showEditLayoutListener!=null){

                        showEditLayoutListener.showEditLayout(editView);
                    }
                }
                PricingTableProductItem.this.setTag(PricingTableProductItem.this.getId(), "hidden");


            }


        }
    };
    //check btn
    private CompoundButton.OnCheckedChangeListener checkBtnChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {



            int position = (int) buttonView.getTag();
            ChildItemViews childItemViews = mItemViews.get(position);


            childItemViews.getEditView().setFocusableInTouchMode(true);
            childItemViews.getEditView().setFocusable(true);
            childItemViews.getEdReduceRate().clearFocus();

            String edReduceValue = getMoneyTypeTextString(childItemViews.getEdReduceRate());
            double edReduceNum = 0;
            double originalCost = 0;
            try {
                originalCost = Double.parseDouble(getViewData().getProductOriginalCost());
            } catch (NumberFormatException e) {
                Utils.log(e.getMessage());
            }
            try {
                edReduceNum = Double.parseDouble(edReduceValue);
            } catch (NumberFormatException e) {
                Utils.log(e.getMessage());
            }

            String moneyDefault = Constants.MONEY_DISCOUNT_MONEY;
            if (!isChecked) {//left   money
                if (edReduceNum > originalCost) {

                    setEdReduceRateText(childItemViews,
                            String.valueOf(originalCost));
                }
            } else {// right  %
                if (edReduceNum > Constants.MAX_PERCENT_VALUE) {
                    setEdReduceRateText(childItemViews,
                            String.valueOf(Constants.MAX_PERCENT_VALUE));
                }

                moneyDefault = Constants.MONEY_DISCOUNT_PERCENT;
            }

            if (position == PRICING_TABLE_PRODUCT_ITEM_MODEL_1_MEMBER) {
                getViewData().setMemberTempProductMoneyDefault(moneyDefault);
//                getViewData().setMemberMoneyDefault(moneyDefault);
            }
            if (position == PRICING_TABLE_PRODUCT_ITEM_MODEL_1_GUEST) {
                getViewData().setGuestTempProductMoneyDefault(moneyDefault);
                //getViewData().setGuestMoneyDefault(moneyDefault);
            }
            if (position == PRICING_TABLE_PRODUCT_ITEM_MODEL_2_0) {
//                productData.setTempProductMoneyDefault(moneyDefault);
//                productData.setProductMoneyDefault(moneyDefault);

               // getViewData().setGuestDiscountType(moneyDefault);
            }


            setSwMaxValue(childItemViews);
        }
    };


    public PricingTableProductItem(BaseEditFragment baseFragment, float itemChildHeight, int itemModel,boolean isNonMember) {
        super(baseFragment.getActivity(), baseFragment.getActualWidthOnThisDevice(200));
        this.mBaseFragment = baseFragment;
        mIsNonMember = isNonMember;
        initView(itemChildHeight, itemModel);
    }

//    public JsonCommonProduct getProductData() {
//        return productData;
//    }



    private void initView(float itemChildHeight, int itemModel) {
        this.setOrientation(LinearLayout.HORIZONTAL);

        this.setTag(this.getId(), "hidden");
        this.mItemChildHeight = itemChildHeight;
        this.mItemModel = itemModel;

        mProductMoneyType = AppUtils.getCurrentCurrency(mBaseFragment.getActivity());
        mItemViews = new ArrayList<>();


        LinearLayout.LayoutParams bodyParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llBody = new LinearLayout(mBaseFragment.getActivity());
        llBody.setLayoutParams(bodyParams);
        llBody.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        delBtn = new IteeButton(mBaseFragment.getActivity());
        delBtn.setText(R.string.common_delete);
        delBtn.setTextColor(mBaseFragment.getColor(R.color.common_white));
        delBtn.setTag(this);
        delBtn.setBackgroundResource(R.drawable.bg_common_delete);
        delBtn.setLayoutParams(btnParams);


        if (mItemModel == PRICING_TABLE_PRODUCT_ITEM_MODEL_1) {
            llBody.addView(getShowItem(0, mBaseFragment.getResources().getColor(R.color.common_white), false));
            llBody.addView(getLine());
            memberLayout = getShowItem(1, mBaseFragment.getResources().getColor(R.color.common_light_gray), true);
            llBody.addView(memberLayout);
            if (mIsNonMember){
                memberLayout.setVisibility(View.GONE);
            }else{
                llBody.addView(getLine());
            }
            llBody.addView(getShowItem(2, mBaseFragment.getResources().getColor(R.color.common_light_gray), true));
            llBody.addView(getLine());
        }

        if (mItemModel == PRICING_TABLE_PRODUCT_ITEM_MODEL_2) {
            llBody.addView(getShowItem(0, mBaseFragment.getResources().getColor(R.color.common_white), true));
            llBody.addView(getLine());

        }
        this.addView(llBody);
        LinearLayout.LayoutParams testParams = new LinearLayout.LayoutParams(mBaseFragment.getActualWidthOnThisDevice(200), LayoutParams.MATCH_PARENT);
        llDelete = new LinearLayout(mBaseFragment.getActivity());
        llDelete.setLayoutParams(testParams);
        llDelete.setBackgroundColor(Color.RED);
        llDelete.addView(delBtn);
        this.addView(llDelete);


    }

    private LinearLayout getShowItem(int position, int backColor, boolean haveEditView) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout layout = new LinearLayout(mBaseFragment.getActivity());
        //layout.setPadding(40, 0, 40, 0);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(layoutParams);

        ChildItemViews childItemViews = new ChildItemViews();
        // Layout  if haveEditView  it's upView
        RelativeLayout layout_1 = new RelativeLayout(mBaseFragment.getActivity());

        layout_1.setOnClickListener(upViewClickListener);
        layout_1.setBackgroundColor(backColor);

        // left key textView
        RelativeLayout.LayoutParams tvLeftKeyLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (int) this.mItemChildHeight);
        tvLeftKeyLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, BaseFragment.LAYOUT_TRUE);
        tvLeftKeyLayoutParams.leftMargin = mBaseFragment.getActualWidthOnThisDevice(40);
        IteeTextView tvLeftKey = generateTextView(tvLeftKeyLayoutParams, mBaseFragment.getColor(R.color.common_black));
        tvLeftKey.setId(View.generateViewId());
        layout_1.addView(tvLeftKey);
        layout_1.setTag(position);
        childItemViews.setTvKey(tvLeftKey);


        // right layout
        //t1
        int textHeight = (int) this.mItemChildHeight;
        if (mItemModel == PRICING_TABLE_PRODUCT_ITEM_MODEL_2) {
            textHeight = textHeight / 2;
        }
        RelativeLayout.LayoutParams t1LayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, textHeight);
        t1LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, BaseFragment.LAYOUT_TRUE);
        IteeTextView t1 = generateTextView(t1LayoutParams, mBaseFragment.getColor(R.color.common_black));
        t1.setId(View.generateViewId());
        t1.setPadding(0, 0, mBaseFragment.getActualWidthOnThisDevice(40), 0);
        layout_1.addView(t1);
        childItemViews.setTv1(t1);


        //t2
        RelativeLayout.LayoutParams t2LayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, textHeight);
        t2LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        t2LayoutParams.addRule(RelativeLayout.BELOW, t1.getId());
        IteeTextView t2 = generateTextView(t2LayoutParams, mBaseFragment.getResources().getColor(R.color.common_black));
        t2.setId(View.generateViewId());
        t2.setPadding(0, 0, mBaseFragment.getActualWidthOnThisDevice(40), 0);
        layout_1.addView(t2);
        childItemViews.setTv2(t2);
        //t3

        RelativeLayout.LayoutParams t3LayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, textHeight);
        t3LayoutParams.addRule(RelativeLayout.LEFT_OF, t2.getId());
        t3LayoutParams.setMargins(0, 0, 15, 0);
        t3LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        IteeTextView t3 = generateTextView(t3LayoutParams, mBaseFragment.getResources().getColor(R.color.common_red));
        layout_1.addView(t3);
        t3.setLayoutParams(t3LayoutParams);
        childItemViews.setTv3(t3);

        if (position == 0) {
            t2.setVisibility(View.GONE);
            t3.setVisibility(View.GONE);
        } else {
            t1.setVisibility(View.GONE);
        }


        if (mItemModel == PRICING_TABLE_PRODUCT_ITEM_MODEL_2) {
            t2.setVisibility(View.VISIBLE);
            t3.setVisibility(View.VISIBLE);
        }
        layout.addView(layout_1);
        // if haveEditView  it's downView
        if (haveEditView) {

            setEditView(position, layout, childItemViews);
        }


        mItemViews.add(childItemViews);
        return layout;
    }

    private void setEditView(int position, LinearLayout parentLayout, ChildItemViews childItemViews) {
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




        //editView  Button
        RelativeLayout.LayoutParams okBtnParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mBaseFragment.getActualWidthOnThisDevice(80));
//        okBtnParam.addRule(RelativeLayout.RIGHT_OF, edReduceRate.getId());
        okBtnParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        okBtnParam.addRule(RelativeLayout.CENTER_VERTICAL);
        okBtnParam.leftMargin = mBaseFragment.getActualWidthOnThisDevice(10);
        okBtnParam.rightMargin = mBaseFragment.getActualWidthOnThisDevice(20);
        IteeButton okBtn = new IteeButton(mBaseFragment.getActivity());
        okBtn.setText(R.string.common_ok);
        okBtn.setTextSize(Constants.FONT_SIZE_SMALLER);
        okBtn.setSingleLine();
        okBtn.setTag(position);
        okBtn.setId(View.generateViewId());
        okBtn.setLayoutParams(okBtnParam);
        okBtn.setOnClickListener(okBtnClickListener);
        okBtn.setBackgroundResource(R.drawable.bg_pricing_ok_btn);
        okBtn.setTextColor(mBaseFragment.getColor(R.color.common_white));

        //editView  EditText
        RelativeLayout.LayoutParams reduceRateParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mBaseFragment.getActualHeightOnThisDevice(80));
        reduceRateParam.addRule(RelativeLayout.RIGHT_OF, editViewSwCurrency.getId());
        reduceRateParam.addRule(RelativeLayout.LEFT_OF, okBtn.getId());
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
        edReduceRate.setOnFocusChangeListener(editViewFocusChangeListener);
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

        // add  data
        childItemViews.setEditViewSwCurrency(editViewSwCurrency);
        childItemViews.setEdReduceRate(edReduceRate);
        childItemViews.setEditView(editViewContainer);
        childItemViews.setOkBtn(okBtn);
        childItemViews.setEditTextWatcher(editTextWatcher);
        //add layout
        editViewContainer.addView(editViewSwCurrency);
        editViewContainer.addView(edReduceRate);
        editViewContainer.addView(okBtn);

        parentLayout.addView(editViewContainer);

    }

    // generateTextView
    private IteeTextView generateTextView(RelativeLayout.LayoutParams textKayLayoutParam, int textColor) {
        IteeTextView text = new IteeTextView(mBaseFragment.getActivity());
        text.setTextSize(Constants.FONT_SIZE_SMALLER);
        text.setTextColor(textColor);
        text.setGravity(Gravity.CENTER_VERTICAL);
        text.setLayoutParams(textKayLayoutParam);
        return text;
    }

    // get detail Line
    private View getLine() {
        return getLine(1, R.color.common_separator_gray);
    }

    // get line  height  color
    private View getLine(int height, int color) {
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
        View viewSeparator = new View(mBaseFragment.getActivity());
        viewSeparator.setLayoutParams(lineParams);
        viewSeparator.setBackgroundColor(mBaseFragment.getResources().getColor(color));
        return viewSeparator;
    }



    public void refreshView() {


        String strTvKey = Constants.STR_EMPTY;
        String strT2Key = Constants.STR_EMPTY;
        String strT3Key = Constants.STR_EMPTY;
        String edReduceRateText = Constants.STR_EMPTY;
        JsonCommonProduct viewData = getViewData();

        for (int i = 0; i < mItemViews.size(); i++) {
            ChildItemViews childItem = mItemViews.get(i);
            switch (i) {
                case 0:
                    strTvKey = viewData.getProductName();
                    setMoneyTypeText(childItem.getTv1(), viewData.getProductOriginalCost());
                    if (mItemModel == PRICING_TABLE_PRODUCT_ITEM_MODEL_2) {
                        String discount = viewData.getGuestProductDiscount();
                        double discountD = 0;
                        if (Utils.isStringNotNullOrEmpty(discount))
                         discountD = Double.parseDouble(discount);
                        String originalCost = viewData.getProductOriginalCost();
                        double originalCostD = 0;
                        if (Utils.isStringNotNullOrEmpty(originalCost))
                         originalCostD = Double.parseDouble(originalCost);
                        if (Constants.MONEY_DISCOUNT_MONEY.equals(viewData.getGuestDiscountType())){
                            strT2Key = Utils.get2DigitDecimalString(String.valueOf(originalCostD - discountD));
                        }else{
                            strT2Key = Utils.get2DigitDecimalString(String.valueOf(originalCostD - (originalCostD*(discountD/100))));

                        }
                        strT3Key = discount;

                    }
                    break;

                case PRICING_TABLE_PRODUCT_ITEM_MODEL_1_MEMBER:
                    strTvKey = mBaseFragment.getResources().getString(R.string.customers_member);
                    strT2Key = getNowCost(viewData.getMemberDiscountType(), viewData.getProductOriginalCost(), viewData.getMemberProductDiscount());

                    strT3Key = viewData.getMemberProductDiscount();
                    break;
                case PRICING_TABLE_PRODUCT_ITEM_MODEL_1_GUEST:
                    strTvKey = mBaseFragment.getResources().getString(R.string.customers_guest);
                    if (mIsNonMember){

                        strTvKey = viewData.getNonMemberName();
                    }
                    strT2Key = getNowCost(viewData.getGuestDiscountType(),viewData.getProductOriginalCost(),viewData.getGuestProductDiscount());
                    strT3Key = viewData.getGuestProductDiscount();
                    break;
                default:
                    break;
            }
            if (mItemModel == PRICING_TABLE_PRODUCT_ITEM_MODEL_1 && i == 0) {
                childItem.getTvKey().setText(strTvKey);
            } else {
                childItem.getTvKey().setText(strTvKey);
                setMoneyTypeText(childItem.getTv2(), strT2Key);
                setCheckedMoneyCheckBtn(childItem.getEditViewSwCurrency(), i);
                setT3Value(childItem, strT3Key);
               // showDesignatedView(childItem.getEditView(), isShowEditView);

                setEdReduceRateText(childItem, strT3Key);
                setSwMaxValue(childItem);
            }


            if (viewData.getPackageId() != null && !viewData.getPackageId().equals(Constants.STR_0)) {
                childItem.getTv2().setVisibility(View.GONE);
                childItem.getTv3().setVisibility(View.GONE);
            }

        }


    }

    private void setSwMaxValue(ChildItemViews childItem) {
        String checkBtnDirection = getSwBtnDirection(childItem.getEditViewSwCurrency());
        if ( Constants.MONEY_DISCOUNT_PERCENT.equals(checkBtnDirection)) {
            childItem.getEditTextWatcher().setMaxValue((double) Constants.MAX_PERCENT_VALUE);

        } else {
            String originalCost = getViewData().getProductOriginalCost();
            double maxValue = 0;
            try {
                maxValue = Double.parseDouble(originalCost);
            } catch (NumberFormatException e) {
                Utils.log(e.getMessage());
            }
            childItem.getEditTextWatcher().setMaxValue(maxValue);
        }


    }

//
//    private String getItemNowCost(String originalCost, String discount, String discountType) {
//
//        double originalCostD = doubleToString(originalCost);
//        double discountD = doubleToString(discount);
//        if (Constants.MONEY_DISCOUNT_MONEY.equals(discountType)){
//            return Utils.get2DigitDecimalString(String.valueOf(originalCostD - discountD));
//        }else{
//            return Utils.get2DigitDecimalString(String.valueOf(originalCostD - (originalCostD * discountD / 100)));
//        }
//    }


//    private String getNowCost(int direction, String prize, String discount) {
//        float prizeF = 0;
//        float discountF = 0;
//        try {
//            prizeF = Float.parseFloat(prize);
//            discountF = Float.parseFloat(discount);
//        } catch (NumberFormatException e) {
//            Utils.log(e.getMessage());
//        }
//        if (direction == Constants.SWITCH_RIGHT) {
//            return String.valueOf(prizeF * (Constants.MAX_PERCENT_VALUE - discountF) / Constants.MAX_PERCENT_VALUE);
//        }
//        return String.valueOf(prizeF - discountF);
//    }

    public void hiddenEditView() {
        for (int i = 0; i < mItemViews.size(); i++) {
            ChildItemViews childItem = mItemViews.get(i);

            if (childItem.getEditView() != null) {

                childItem.getEditView().setVisibility(View.GONE);
                if (position == PRICING_TABLE_PRODUCT_ITEM_MODEL_1_MEMBER) {

                    getViewData().setMemberEditViewShow(false);
                }
                if (position == PRICING_TABLE_PRODUCT_ITEM_MODEL_1_GUEST) {

                    getViewData().setGuestEditViewShow(false);
                }
                if (position == PRICING_TABLE_PRODUCT_ITEM_MODEL_2_0) {

                    getViewData().setShowDown(false);
                }
            }
        }
    }

    //
//    private void showDesignatedView(View v, boolean show) {
////        if (show) {
////            v.setVisibility(View.VISIBLE);
////        } else {
////            v.setVisibility(View.GONE);
////        }
//    }

    private String getSwBtnDirection(CheckSwitchButton btn) {
        if (btn.isChecked()) {
            return Constants.MONEY_DISCOUNT_PERCENT;
        }
        return Constants.MONEY_DISCOUNT_MONEY;
    }

    private void setCheckedMoneyCheckBtn(CheckSwitchButton btn,int type) {

        String guestDiscountType = getViewData().getGuestDiscountType();
        switch (type) {

            case PRICING_TABLE_PRODUCT_ITEM_MODEL_1_MEMBER:
                guestDiscountType = getViewData().getMemberDiscountType();

                break;
            case PRICING_TABLE_PRODUCT_ITEM_MODEL_1_GUEST:
                guestDiscountType = getViewData().getGuestDiscountType();
                break;
            default:
                break;
        }
        if (Constants.MONEY_DISCOUNT_MONEY.equals(guestDiscountType)){
            btn.setChecked(false);
        }else{
            btn.setChecked(true);
        }



    }
    //Listener

    private void setEdReduceRateText(ChildItemViews childItemViews , String value) {
        EditText edReduceRate = childItemViews.getEdReduceRate();
        if (Utils.isStringNotNullOrEmpty(value)) {
            if (!childItemViews.getEditViewSwCurrency().isChecked()){
                edReduceRate.setText(AppUtils.getCurrentCurrency(mBaseFragment.getBaseActivity())+ Utils.get2DigitDecimalString(value));
            }else{
                edReduceRate.setText(Utils.get2DigitDecimalString(value));
            }

        } else {
            edReduceRate.setText(Constants.STR_0);
        }
    }
    private void setT3Value(ChildItemViews childItemViews, String value) {
        IteeTextView t3 = childItemViews.getTv3();

        if (Constants.STR_0.equals(value)||Utils.isStringNullOrEmpty(value)){

            t3.setVisibility(View.INVISIBLE);
        }else{
            t3.setVisibility(View.VISIBLE);

        }



        if (Utils.isStringNotNullOrEmpty(value)) {
            if (!childItemViews.getEditViewSwCurrency().isChecked()){
                t3.setText(AppUtils.getCurrentCurrency(mBaseFragment.getBaseActivity()) + Constants.STR_MINUS_MONEY + Utils.get2DigitDecimalString(value));
            }else{
                t3.setText(Utils.get2DigitDecimalString(value) + Constants.STR_PER_CENT + Constants.STR_SPACE + Constants.STR_OFF);
            }

        } else {
            t3.setText(Constants.STR_0);
        }
    }

//    private void setMoneyTypeText(TextView v, String value) {
//        v.setText(mProductMoneyType + Constants.STR_SPACE + Utils.get2DigitDecimalString(value));
//    }

    public void setDelListener(OnClickListener delListener) {

        delBtn.setOnClickListener(delListener);
    }

    class ChildItemViews {
        private TextView tvKey;

        //         t1
        //     t3  t2

        private IteeTextView tv1;// 1 storey;
        private IteeTextView tv2;// 2 storey; or only  1 storey
        private IteeTextView tv3;//2 storey;  or only  1 storey

        //EditView
        private RelativeLayout editView;
        // EditView    CheckSwitchButton
        private CheckSwitchButton editViewSwCurrency;
        // EditView    EditText
        private EditText edReduceRate;
        // EditView    Button
        private IteeButton okBtn;

        private AppUtils.EditViewMoneyWatcher editTextWatcher;

        public AppUtils.EditViewMoneyWatcher getEditTextWatcher() {
            return editTextWatcher;
        }

        public void setEditTextWatcher(AppUtils.EditViewMoneyWatcher editTextWatcher) {
            this.editTextWatcher = editTextWatcher;
        }

        public void setOkBtn(IteeButton okBtn) {
            this.okBtn = okBtn;
        }

        public EditText getEdReduceRate() {
            return edReduceRate;
        }

        public void setEdReduceRate(EditText edReduceRate) {
            this.edReduceRate = edReduceRate;
        }

        public RelativeLayout getEditView() {
            return editView;
        }

        public void setEditView(RelativeLayout editView) {
            this.editView = editView;
        }


        public TextView getTvKey() {
            return tvKey;
        }

        public void setTvKey(TextView tvKey) {
            this.tvKey = tvKey;
        }

        public CheckSwitchButton getEditViewSwCurrency() {
            return editViewSwCurrency;
        }

        public void setEditViewSwCurrency(CheckSwitchButton editViewSwCurrency) {
            this.editViewSwCurrency = editViewSwCurrency;
        }

        public IteeTextView getTv1() {
            return tv1;
        }

        public void setTv1(IteeTextView tv1) {
            this.tv1 = tv1;
        }

        public IteeTextView getTv2() {
            return tv2;
        }

        public void setTv2(IteeTextView tv2) {
            this.tv2 = tv2;
        }

        public IteeTextView getTv3() {
            return tv3;
        }

        public void setTv3(IteeTextView tv3) {
            this.tv3 = tv3;
        }
    }

    public interface ShowEditLayoutListener{

        public void showEditLayout(View v);
    }
}
