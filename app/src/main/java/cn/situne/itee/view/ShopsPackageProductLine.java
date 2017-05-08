/**
 * Project Name: itee
 * File Name:	 ShopsPackageProductLine.java
 * Package Name: cn.situne.itee.view
 * Date:		 2015-04-14
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.view;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.ShopsPackageInfo;
import cn.situne.itee.entity.ShopsProduct;
import cn.situne.itee.fragment.BaseFragment;

/**
 * ClassName:ShopsPackageProductLine <br/>
 * Function: FIXME. <br/>
 * Date: 2015-04-14 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class ShopsPackageProductLine extends LinearLayout {

    private BaseFragment mBaseFragment;
    private ShopsPackageInfo packageInfo;

    private LinearLayout llPackageProductContainer;
    private IteeTextView tvProductName;
    private IteeTextView tvProductCount;
    private IteeTextView tvProductPrice;

    private int currentPosition;

    public ShopsPackageProductLine(BaseFragment mBaseFragment) {
        super(mBaseFragment.getActivity());
        this.mBaseFragment = mBaseFragment;
        init();
    }

    public void setPackageInfo(ShopsPackageInfo packageInfo) {
        this.packageInfo = packageInfo;
        setValues();
    }

    private void init() {

        setOrientation(VERTICAL);
        setBackgroundColor(Color.WHITE);

        RelativeLayout rlPackageInfo = new RelativeLayout(mBaseFragment.getActivity());

        LayoutParams rlPackageInfoLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, mBaseFragment.getActualHeightOnThisDevice(80));
        rlPackageInfo.setLayoutParams(rlPackageInfoLayoutParams);

        llPackageProductContainer = new LinearLayout(mBaseFragment.getActivity());
        llPackageProductContainer.setOrientation(VERTICAL);

        LayoutParams llPackageProductContainerLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        llPackageProductContainer.setLayoutParams(llPackageProductContainerLayoutParams);

        tvProductName = new IteeTextView(mBaseFragment);
        tvProductCount = new IteeTextView(mBaseFragment);
        tvProductPrice = new IteeTextView(mBaseFragment);

        tvProductName.setId(View.generateViewId());
        tvProductPrice.setId(View.generateViewId());
        tvProductCount.setId(View.generateViewId());

        tvProductName.setTextColor(mBaseFragment.getColor(R.color.common_black));
        tvProductPrice.setTextColor(mBaseFragment.getColor(R.color.common_black));
        tvProductCount.setTextColor(mBaseFragment.getColor(R.color.common_gray));

        tvProductName.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvProductCount.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvProductPrice.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvProductName.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        tvProductCount.setGravity(Gravity.CENTER);
        tvProductPrice.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);

        RelativeLayout.LayoutParams tvProductNameLayoutParams
                = new RelativeLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tvProductNameLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        tvProductNameLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        tvProductNameLayoutParams.leftMargin = mBaseFragment.getActualWidthOnThisDevice(40);
        tvProductName.setLayoutParams(tvProductNameLayoutParams);
        tvProductName.setMaxWidth(mBaseFragment.getActualWidthOnThisDevice(450));

                RelativeLayout.LayoutParams tvProductCountLayoutParams
                = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tvProductCountLayoutParams.addRule(RelativeLayout.RIGHT_OF, tvProductName.getId());
        tvProductCountLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        tvProductCountLayoutParams.leftMargin = mBaseFragment.getActualWidthOnThisDevice(10);
        tvProductCount.setLayoutParams(tvProductCountLayoutParams);



        RelativeLayout.LayoutParams tvProductPriceLayoutParams
                = new RelativeLayout.LayoutParams(mBaseFragment.getActualWidthOnThisDevice(200), mBaseFragment.getActualHeightOnThisDevice(60));
        tvProductPriceLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        tvProductPriceLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        tvProductPriceLayoutParams.rightMargin = mBaseFragment.getActualWidthOnThisDevice(40);
        tvProductPrice.setLayoutParams(tvProductPriceLayoutParams);

        rlPackageInfo.addView(tvProductName);
        rlPackageInfo.addView(tvProductCount);
        rlPackageInfo.addView(tvProductPrice);

        addView(rlPackageInfo);
        addView(llPackageProductContainer);

        AppUtils.addTopSeparatorLine(rlPackageInfo, mBaseFragment);
        llPackageProductContainer.addView(AppUtils.getSeparatorLine(mBaseFragment));
    }

    private void setValues() {
        tvProductName.setText(packageInfo.getPackageName());

        //fix syb
        if (Constants.STR_1.equals(packageInfo.getUnlimitedFlag())){
            tvProductCount.setText(Constants.STR_BRACKETS_START+Constants.STR_SEPARATOR+Constants.STR_BRACKETS_END);
        }else {
            tvProductCount.setText(Constants.STR_BRACKETS_START+packageInfo.getPackageCount()+Constants.STR_BRACKETS_END);
        }

        tvProductPrice.setText(AppUtils.addCurrencySymbol(packageInfo.getPackagePrice(), mBaseFragment.getActivity()));

        llPackageProductContainer.removeAllViews();

        for (ShopsProduct shopsProduct : packageInfo.getProductList()) {
            PackageProductDetailLayout packageProductDetailLayout = generateProductDetailLayout(shopsProduct);
            llPackageProductContainer.addView(packageProductDetailLayout);
        }
    }

    private PackageProductDetailLayout generateProductDetailLayout(ShopsProduct shopsProduct) {
        PackageProductDetailLayout packageProductDetailLayout = new PackageProductDetailLayout(mBaseFragment, shopsProduct);
        RelativeLayout.LayoutParams rlProductDetailContainerLayoutParams
                = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mBaseFragment.getActualHeightOnThisDevice(80));
        packageProductDetailLayout.setLayoutParams(rlProductDetailContainerLayoutParams);
        packageProductDetailLayout.setBackgroundColor(mBaseFragment.getColor(R.color.common_package_gray));
        return packageProductDetailLayout;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    class PackageProductDetailLayout extends RelativeLayout {

        private ShopsProduct product;

        private IteeTextView tvProductName;
        private IteeTextView tvProductPrice;
        private IteeTextView tvProductNum;

        public PackageProductDetailLayout(BaseFragment mBaseFragment, ShopsProduct product) {
            super(mBaseFragment.getActivity());
            this.product = product;
            init();
            setValues();
        }

        private void init() {

            setBackgroundColor(mBaseFragment.getColor(R.color.common_light_gray));

            tvProductName = new IteeTextView(mBaseFragment);
            tvProductPrice = new IteeTextView(mBaseFragment);
            tvProductNum = new IteeTextView(mBaseFragment);

            tvProductName.setId(View.generateViewId());
            tvProductPrice.setId(View.generateViewId());
            tvProductNum.setId(View.generateViewId());

            tvProductName.setTextColor(mBaseFragment.getColor(R.color.common_black));
            tvProductPrice.setTextColor(mBaseFragment.getColor(R.color.common_black));
            tvProductNum.setTextColor(mBaseFragment.getColor(R.color.common_blue));

            tvProductName.setTextSize(Constants.FONT_SIZE_SMALLER);
            tvProductPrice.setTextSize(Constants.FONT_SIZE_SMALLER);
            tvProductNum.setTextSize(Constants.FONT_SIZE_SMALLER);

            tvProductName.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
            tvProductPrice.setGravity(Gravity.BOTTOM | Gravity.END);
            tvProductNum.setGravity(Gravity.TOP | Gravity.END);

            RelativeLayout.LayoutParams tvProductNameLayoutParams = new LayoutParams((int) (mBaseFragment.getScreenWidth() * 0.5),
                    ViewGroup.LayoutParams.MATCH_PARENT);
            tvProductNameLayoutParams.addRule(ALIGN_PARENT_LEFT, TRUE);
            tvProductNameLayoutParams.addRule(CENTER_VERTICAL, TRUE);
            tvProductNameLayoutParams.leftMargin = mBaseFragment.getActualWidthOnThisDevice(80);
            tvProductName.setLayoutParams(tvProductNameLayoutParams);

            RelativeLayout.LayoutParams tvProductPriceLayoutParams = new LayoutParams(mBaseFragment.getActualWidthOnThisDevice(200),
                    mBaseFragment.getActualHeightOnThisDevice(50));
            tvProductPriceLayoutParams.addRule(ALIGN_PARENT_RIGHT, TRUE);
            tvProductPriceLayoutParams.addRule(ALIGN_PARENT_TOP, TRUE);
            tvProductPriceLayoutParams.rightMargin = mBaseFragment.getActualWidthOnThisDevice(40);
            tvProductPrice.setLayoutParams(tvProductPriceLayoutParams);

            RelativeLayout.LayoutParams tvProductNumLayoutParams = new LayoutParams(mBaseFragment.getActualWidthOnThisDevice(200),
                    mBaseFragment.getActualHeightOnThisDevice(30));
            tvProductNumLayoutParams.addRule(ALIGN_PARENT_RIGHT, TRUE);
            tvProductNumLayoutParams.addRule(BELOW, tvProductPrice.getId());
            tvProductNumLayoutParams.rightMargin = mBaseFragment.getActualWidthOnThisDevice(40);
            tvProductNum.setLayoutParams(tvProductNumLayoutParams);

            addView(tvProductName);
            addView(tvProductPrice);
            addView(tvProductNum);
        }

        private void setValues() {
            if (Utils.isStringNotNullOrEmpty(product.getProductPrice())) {
                tvProductPrice.setText(AppUtils.addCurrencySymbol(product.getProductPrice(), mBaseFragment.getActivity()));
            }
            if (Utils.isStringNotNullOrEmpty(product.getProductName())) {
                tvProductName.setText(product.getProductName());
            }
            if (product.getProductNumber() != null && product.getProductNumber() >= 0) {
                tvProductNum.setText(Constants.STR_MULTIPLY + String.valueOf(product.getProductNumber()));
            }
        }
    }
}