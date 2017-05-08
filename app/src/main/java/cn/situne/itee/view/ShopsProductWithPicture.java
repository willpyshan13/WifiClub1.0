/**
 * Project Name: itee
 * File Name:	 ShopsProductWithPicture.java
 * Package Name: cn.situne.itee.view
 * Date:		 2015-04-08
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.view;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.NetworkImageView;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.jsonentity.JsonChooseProduct;

/**
 * ClassName:ShopsProductWithPicture <br/>
 * Function: product with pic . <br/>
 * Date: 2015-04-08 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class ShopsProductWithPicture extends RelativeLayout {

    private RelativeLayout rlContainer;
    private IteeTextView tvProductName;
    private IteeTextView tvProductPrice;
    private NetworkImageView ivProductPic;

    private BaseFragment mBaseFragment;

    private JsonChooseProduct.SalesType.SalesTypeProduct product;

    public ShopsProductWithPicture(BaseFragment mBaseFragment) {
        super(mBaseFragment.getActivity());
        this.mBaseFragment = mBaseFragment;
        init();
        showValues();
    }

    private void showValues() {
        if (product != null) {
            tvProductName.setText(product.getName());
            tvProductPrice.setText(AppUtils.addCurrencySymbol(product.getPrice(), getContext()));
            setProductPicture(product.getImgPath());
        }
    }

    private void init() {
        setGravity(Gravity.CENTER);
        rlContainer = new RelativeLayout(mBaseFragment.getActivity());

        RelativeLayout.LayoutParams rlContainerLayoutParams
                = new LayoutParams(mBaseFragment.getActualWidthOnThisDevice(210), mBaseFragment.getActualHeightOnThisDevice(260));
        rlContainer.setLayoutParams(rlContainerLayoutParams);

        ivProductPic = new NetworkImageView(mBaseFragment.getActivity());
        ivProductPic.setId(View.generateViewId());
        ivProductPic.setDefaultImageResId(R.drawable.icon_user_default_image);
        ivProductPic.setScaleType(ImageView.ScaleType.FIT_XY);

        RelativeLayout.LayoutParams ivProductPicLayoutParams = new LayoutParams(mBaseFragment.getActualWidthOnThisDevice(200), mBaseFragment.getActualHeightOnThisDevice(200));
        ivProductPicLayoutParams.leftMargin = mBaseFragment.getActualWidthOnThisDevice(5);
        ivProductPicLayoutParams.topMargin = mBaseFragment.getActualWidthOnThisDevice(5);
        ivProductPicLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, BaseFragment.LAYOUT_TRUE);
        ivProductPicLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, BaseFragment.LAYOUT_TRUE);
        ivProductPic.setLayoutParams(ivProductPicLayoutParams);

        tvProductPrice = new IteeTextView(mBaseFragment);
        tvProductPrice.setGravity(Gravity.CENTER_VERTICAL);
        tvProductPrice.setTextColor(mBaseFragment.getColor(R.color.common_red));
        tvProductPrice.setId(View.generateViewId());
        tvProductPrice.setPadding(mBaseFragment.getActualWidthOnThisDevice(20), 0, 0, 0);
        tvProductPrice.setTextSize(Constants.FONT_SIZE_MORE_SMALLER);

        RelativeLayout.LayoutParams tvProductPriceLayoutParams = new LayoutParams(BaseFragment.MATCH_PARENT, mBaseFragment.getActualHeightOnThisDevice(40));
        tvProductPriceLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, BaseFragment.LAYOUT_TRUE);
        tvProductPriceLayoutParams.bottomMargin = mBaseFragment.getActualHeightOnThisDevice(5);
        tvProductPrice.setLayoutParams(tvProductPriceLayoutParams);

        tvProductName = new IteeTextView(mBaseFragment);
        tvProductName.setBackgroundColor(mBaseFragment.getColor(R.color.common_half_white));
        tvProductName.setTextColor(mBaseFragment.getColor(R.color.common_black));
        tvProductName.setTextSize(Constants.FONT_SIZE_MORE_SMALLER);
        tvProductName.setPadding(mBaseFragment.getActualWidthOnThisDevice(20), 0, 0, 0);

        RelativeLayout.LayoutParams tvProductNameLayoutParams
                = new LayoutParams(BaseFragment.MATCH_PARENT, mBaseFragment.getActualHeightOnThisDevice(35));
        tvProductNameLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, ivProductPic.getId());
        tvProductNameLayoutParams.leftMargin = mBaseFragment.getActualWidthOnThisDevice(5);
        tvProductNameLayoutParams.rightMargin = mBaseFragment.getActualWidthOnThisDevice(5);
        tvProductName.setLayoutParams(tvProductNameLayoutParams);

        rlContainer.addView(ivProductPic);
        rlContainer.addView(tvProductPrice);
        rlContainer.addView(tvProductName);

        addView(rlContainer);
        setBackground(mBaseFragment.getDrawable(R.drawable.bg_gray_layout));
    }

    public void setProductName(String productName) {
        tvProductName.setText(productName);
    }

    public String getProductName() {
        return String.valueOf(tvProductName.getText());
    }

    public void setProductPrice(String productPrice) {
        tvProductPrice.setText(AppUtils.addCurrencySymbol(productPrice, getContext()));
    }

    public String getProductPrice() {
        return String.valueOf(tvProductPrice.getText());
    }

    public void setProductPicture(String productPicture) {
        AppUtils.showNetworkImage(ivProductPic, productPicture);
    }

    public JsonChooseProduct.SalesType.SalesTypeProduct getProduct() {
        return product;
    }

    public void setProduct(JsonChooseProduct.SalesType.SalesTypeProduct product) {
        this.product = product;
        showValues();
    }
}