/**
 * Project Name: itee
 * File Name:	 ShopsSingleProductLine.java
 * Package Name: cn.situne.itee.view
 * Date:		 2015-04-08
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.view;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.jsonentity.JsonChooseProduct;

/**
 * ClassName:ShopsSingleProductLine <br/>
 * Function: a line with one product . <br/>
 * Date: 2015-04-08 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class ShopsSingleProductLine extends RelativeLayout {

    private BaseFragment mBaseFragment;
    private IteeTextView tvProductName;
    private IteeTextView tvProductPrice;
    private IteeTextView tvProductCount;
    private AppUtils.NoDoubleClickListener listener;

    private JsonChooseProduct.SalesType.SalesTypeProduct product;

    public ShopsSingleProductLine(BaseFragment mBaseFragment) {
        super(mBaseFragment.getActivity());
        this.mBaseFragment = mBaseFragment;
        tvProductName = new IteeTextView(mBaseFragment);
        tvProductCount = new IteeTextView(mBaseFragment);
        tvProductPrice = new IteeTextView(mBaseFragment);
        init();
        showValues();
    }

    private void showValues() {
        if (product != null) {
            tvProductName.setText(product.getName());
            //fix syb
            if (Constants.STR_1.equals(product.getUnlimitedFlag())){
                tvProductCount.setText(Constants.STR_BRACKETS_START+Constants.STR_SEPARATOR+Constants.STR_BRACKETS_END);
            }else {
                tvProductCount.setText(Constants.STR_BRACKETS_START+product.getQty()+Constants.STR_BRACKETS_END);
            }

            tvProductPrice.setText(AppUtils.addCurrencySymbol(product.getPrice(), getContext()));
        }
    }

    private void init() {

        setBackgroundColor(Color.WHITE);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(BaseFragment.MATCH_PARENT, mBaseFragment.getActualHeightOnThisDevice(100));
        this.setLayoutParams(layoutParams);

        tvProductName.setGravity(Gravity.CENTER_VERTICAL);
        tvProductName.setId(View.generateViewId());
        RelativeLayout.LayoutParams tvProductNameLayoutParams
                = new RelativeLayout.LayoutParams(BaseFragment.WRAP_CONTENT, BaseFragment.MATCH_PARENT);
        tvProductNameLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, BaseFragment.LAYOUT_TRUE);
        tvProductNameLayoutParams.leftMargin = mBaseFragment.getActualWidthOnThisDevice(40);
        tvProductName.setLayoutParams(tvProductNameLayoutParams);
        tvProductName.setMaxWidth(mBaseFragment.getActualWidthOnThisDevice(460));

        tvProductCount.setGravity(Gravity.CENTER_VERTICAL);

        RelativeLayout.LayoutParams tvProductCountLayoutParams
                = new RelativeLayout.LayoutParams(BaseFragment.WRAP_CONTENT, BaseFragment.MATCH_PARENT);
        tvProductCountLayoutParams.addRule(RelativeLayout.RIGHT_OF, tvProductName.getId());
        tvProductCountLayoutParams.leftMargin = mBaseFragment.getActualWidthOnThisDevice(40);
        tvProductCount.setLayoutParams(tvProductCountLayoutParams);


        tvProductPrice.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        RelativeLayout.LayoutParams tvProductPriceLayoutParams
                = new RelativeLayout.LayoutParams((int) (mBaseFragment.getScreenWidth() * 0.4), BaseFragment.MATCH_PARENT);
        tvProductPriceLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, BaseFragment.LAYOUT_TRUE);
        tvProductPriceLayoutParams.rightMargin = mBaseFragment.getActualWidthOnThisDevice(40);
        tvProductPrice.setLayoutParams(tvProductPriceLayoutParams);

        tvProductCount.setTextColor(mBaseFragment.getColor(R.color.common_gray));

        this.addView(tvProductName);
        this.addView(tvProductCount);
        this.addView(tvProductPrice);
        AppUtils.addBottomSeparatorLine(this, mBaseFragment);

        this.setOnClickListener(listener);
    }

    public AppUtils.NoDoubleClickListener getListener() {
        return listener;
    }

    public void setListener(AppUtils.NoDoubleClickListener listener) {
        this.listener = listener;
    }

    public boolean isHasAttr() {
        return product.getAttrCount() > 0;
    }

    public void setProduct(JsonChooseProduct.SalesType.SalesTypeProduct product) {
        this.product = product;
        showValues();
    }

    public JsonChooseProduct.SalesType.SalesTypeProduct getProduct() {
        return product;
    }
}