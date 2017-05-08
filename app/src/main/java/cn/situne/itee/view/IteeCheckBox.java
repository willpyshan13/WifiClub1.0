/**
 * Project Name: itee
 * File Name:	 IteeCheckBox.java
 * Package Name: cn.situne.itee.view
 * Date:		 2015-04-16
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.shopping.ShoppingPaymentFragment;

/**
 * ClassName:IteeCheckBox <br/>
 * Function: 06模块 的 checkBox <br/>
 * <p/>
 * Date: 2015-04-16 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class IteeCheckBox extends RelativeLayout {

    private BaseFragment mFragment;
    private Context mContext;
    private int mHeight;

    private boolean isCheck;
    private IteeButton btnCheck;
    private TextView tvText;
    private ShoppingPaymentFragment.PurchaseItemListener mPurchaseItemListener;
    private ShoppingPurchaseItem.RowItem rowItem;

    private CheckBoxListener checkBoxListener;

    public CheckBoxListener getCheckBoxListener() {
        return checkBoxListener;
    }

    public void setCheckBoxListener(CheckBoxListener checkBoxListener) {
        this.checkBoxListener = checkBoxListener;
    }

    private boolean enabled;

    private int mIcon1;
    private int mIcon2;

    private String BookingNo;

    public IteeCheckBox(BaseFragment fragment, int height) {
        super(fragment.getBaseActivity());

        mFragment = fragment;
        mContext = mFragment.getActivity().getBaseContext();
        mHeight = height;
        initLayout();
    }

    public IteeCheckBox(BaseFragment fragment, int height, int icon1, int icon2) {
        super(fragment.getBaseActivity());
        mIcon1 = icon1;
        mIcon2 = icon2;
        mFragment = fragment;
        mContext = mFragment.getActivity().getBaseContext();
        mHeight = height;
        initLayout();
    }

    public String getBookingNo() {
        return BookingNo;
    }

    public void setBookingNo(String bookingNo) {
        BookingNo = bookingNo;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ShoppingPurchaseItem.RowItem getRowItem() {
        return rowItem;
    }

    public void setRowItem(ShoppingPurchaseItem.RowItem rowItem) {
        this.rowItem = rowItem;
    }

    public ShoppingPaymentFragment.PurchaseItemListener getmPurchaseItemListener() {
        return mPurchaseItemListener;
    }

    public void setmPurchaseItemListener(ShoppingPaymentFragment.PurchaseItemListener mPurchaseItemListener) {
        this.mPurchaseItemListener = mPurchaseItemListener;
    }

    public TextView getTvText() {
        return tvText;
    }

    public void setIcon(int icon1, int icon2) {
        mIcon1 = icon1;
        mIcon2 = icon2;
        btnCheck.setBackgroundResource(mIcon1);
    }


    private void initLayout() {
        enabled = true;
        RelativeLayout.LayoutParams btnCheckParams = new RelativeLayout.LayoutParams(mHeight, mHeight);
        btnCheck = new IteeButton(mContext);
        btnCheck.setBackgroundResource(mIcon1);
        btnCheck.setLayoutParams(btnCheckParams);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (enabled) {

                    setChecked(!isCheck);
                    if (mPurchaseItemListener != null) {
                        mPurchaseItemListener.clickItemCheckBox(isCheck, rowItem);
                    }


                    if (checkBoxListener!=null){

                        checkBoxListener.changeCheck(isCheck);
                    }
                }


            }
        });
        btnCheck.setId(View.generateViewId());

        btnCheck.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enabled) {
                    setChecked(!isCheck);
                    if (mPurchaseItemListener != null) {
                        mPurchaseItemListener.clickItemCheckBox(isCheck, rowItem);
                    }
                    if (checkBoxListener!=null){

                        checkBoxListener.changeCheck(isCheck);
                    }
                }
            }
        });
        RelativeLayout.LayoutParams tvTextParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, mHeight);
        tvTextParams.addRule(RelativeLayout.RIGHT_OF, btnCheck.getId());
        tvTextParams.leftMargin = mFragment.getActualWidthOnThisDevice(10);
        tvText = new TextView(mContext);
        tvText.setLayoutParams(tvTextParams);
        tvText.setGravity(Gravity.CENTER_VERTICAL);
        tvText.setTextSize(Constants.FONT_SIZE_15);
        tvText.setTextColor(mFragment.getColor(R.color.common_black));
        tvText.setMaxWidth(mFragment.getActualWidthOnThisDevice(500));
        tvText.setSingleLine();
        tvText.setSingleLine(true);
        tvText.setEllipsize(TextUtils.TruncateAt.END);
        this.addView(btnCheck);
        this.addView(tvText);
    }

    public void setText(String text) {

        tvText.setText(text);
    }


    public void setTextColor(int color) {

        tvText.setTextColor(color);
    }

    public void setClickType(OnClickListener clickListener) {
        this.setOnClickListener(clickListener);
        btnCheck.setBackgroundResource(mIcon1);
        btnCheck.setOnClickListener(clickListener);
    }


    public void setClickView(final OnClickListener clickListener) {

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enabled) {
                    setChecked(!isCheck);
                    clickListener.onClick(IteeCheckBox.this);
                }

            }
        });


        btnCheck.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enabled) {
                    setChecked(!isCheck);
                    clickListener.onClick(IteeCheckBox.this);
                }

            }
        });

    }

    public boolean getChecked() {
        return isCheck;
    }

    public void setChecked(boolean checked) {
        isCheck = checked;
        if (isCheck) {
            btnCheck.setBackgroundResource(mIcon2);
        } else {

            btnCheck.setBackgroundResource(mIcon1);
        }

    }

    public void setBtnCheckVisibility(int visibility) {
        btnCheck.setVisibility(visibility);

    }

    public interface CheckBoxListener{

        public void changeCheck(boolean checked);
    }
}
