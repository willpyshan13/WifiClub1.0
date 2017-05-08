/**
 * Project Name: itee
 * File Name:	 ShopsChooseProductPackageEditFragment.java
 * Package Name: cn.situne.itee.fragment.shops
 * Date:		 2015-04-09
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.shops;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.HashMap;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.widget.wheel.BasePopFragment;
import cn.situne.itee.entity.ShopsProduct;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.view.IteeNumberEditView;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:ShopsChooseProductPackageEditFragment <br/>
 * Function: Edit package goods. <br/>
 * Date: 2015-04-09 <br/>
 * UI: 05-4-1-3
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class ShopsChooseProductPackageEditFragment extends BasePopFragment {

    private LinearLayout llContainer;

    private HashMap<String, ShopsProduct> packageMap;

    private ShopsChooseProductFragment containerFragment;

    private String greenId;

    @Override
    public boolean isNeedAnimation() {
        return false;
    }

    @Override
    protected boolean isTop() {
        return true;
    }

    @Override
    public void createContent(LinearLayout mParent) {

        ScrollView scrollView = new ScrollView(getActivity());
        llContainer = new LinearLayout(getActivity());

        llContainer.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams scrollViewLayoutParams = new LinearLayout.LayoutParams(BaseFragment.MATCH_PARENT, BaseFragment.MATCH_PARENT);
        scrollView.setLayoutParams(scrollViewLayoutParams);

        ScrollView.LayoutParams llContainerLayoutParams = new ScrollView.LayoutParams(BaseFragment.MATCH_PARENT, BaseFragment.WRAP_CONTENT);
        llContainer.setLayoutParams(llContainerLayoutParams);

        scrollView.addView(llContainer);

        mParent.addView(scrollView);

        for (String key : packageMap.keySet()) {
            ShopsProduct shopsProduct = packageMap.get(key);
            ProductNumberEditLayout productNumberEditLayout = generateProductLine(shopsProduct);
            llContainer.addView(productNumberEditLayout);
        }
    }

    private ProductNumberEditLayout generateProductLine(final ShopsProduct product) {
        final ProductNumberEditLayout productNumberEditLayout = new ProductNumberEditLayout(baseFragment, product);
        productNumberEditLayout.setListenerDelete(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                String productId = product.getProductId();
                if (greenId.equals(productId)) {
                    productId = String.valueOf(greenId) + Constants.STR_GREEN_ID_SEPARATOR + product.getAttrId();
                }
                llContainer.removeView(productNumberEditLayout);
                packageMap.remove(productId);
                containerFragment.changePackageIconState();
                containerFragment.changeStateOfPackage();
                if (packageMap.keySet().size() == 0) {
                    dismiss();
                }
            }
        });
        return productNumberEditLayout;
    }

    public void setPackageMap(HashMap<String, ShopsProduct> packageMap) {
        this.packageMap = packageMap;
    }

    public static Builder createBuilder(BaseFragment mBaseFragment,
                                        FragmentManager fragmentManager, HashMap<String, ShopsProduct> packageMap) {
        return new Builder(mBaseFragment, fragmentManager, packageMap);
    }

    public void setContainerFragment(ShopsChooseProductFragment containerFragment) {
        this.containerFragment = containerFragment;
    }

    public void setGreenId(String greenId) {
        this.greenId = greenId;
    }

    public static class Builder extends BasePopFragment.Builder<ShopsChooseProductPackageEditFragment> {

        private HashMap<String, ShopsProduct> packageMap;
        private ShopsChooseProductFragment containerFragment;
        private String greenId;

        public Builder(BaseFragment mBaseFragment, FragmentManager fragmentManager, HashMap<String, ShopsProduct> packageMap) {
            super(mBaseFragment, fragmentManager);
            this.packageMap = packageMap;
        }

        @Override
        public Builder setCancelableOnTouchOutside(boolean cancelable) {
            return (Builder) super.setCancelableOnTouchOutside(cancelable);
        }

        @Override
        public Builder setListener(OnDismissedListener listener) {
            return (Builder) super.setListener(listener);
        }

        public Builder setContainerFragment(ShopsChooseProductFragment containerFragment) {
            this.containerFragment = containerFragment;
            return this;
        }

        public Builder setGreenId(String greenId) {
            this.greenId = greenId;
            return this;
        }

        public ShopsChooseProductPackageEditFragment show() {
            ShopsChooseProductPackageEditFragment fragment = (ShopsChooseProductPackageEditFragment) Fragment.instantiate(
                    mBaseFragment.getActivity(), ShopsChooseProductPackageEditFragment.class.getName(), prepareArguments());
            fragment.setDismissedListener(mListener);
            fragment.setPackageMap(packageMap);
            fragment.setBaseFragment(mBaseFragment);
            fragment.setContainerFragment(containerFragment);
            fragment.setGreenId(greenId);
            fragment.show(mFragmentManager, mTag);
            return fragment;
        }
    }

    class ProductNumberEditLayout extends RelativeLayout {

        private BaseFragment mBaseFragment;
        private ShopsProduct shopsProduct;

        private ImageView ivDelete;
        private IteeTextView tvProductName;
        private IteeTextView tvProductPrice;
        private IteeNumberEditView numberEditView;

        private RelativeLayout rlDeleteContainer;

        public ProductNumberEditLayout(BaseFragment mBaseFragment, ShopsProduct shopsProduct) {
            super(mBaseFragment.getActivity());
            this.mBaseFragment = mBaseFragment;
            this.shopsProduct = shopsProduct;
            initControls();
            initData();
        }

        private void initControls() {

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(BaseFragment.MATCH_PARENT, mBaseFragment.getActualHeightOnThisDevice(100));
            setLayoutParams(layoutParams);

            ivDelete = new ImageView(mBaseFragment.getActivity());
            tvProductName = new IteeTextView(mBaseFragment);
            tvProductPrice = new IteeTextView(mBaseFragment);
            numberEditView = new IteeNumberEditView(mBaseFragment);
            rlDeleteContainer = new RelativeLayout(mBaseFragment.getActivity());

            ivDelete.setId(View.generateViewId());
            tvProductName.setId(View.generateViewId());
            tvProductPrice.setId(View.generateViewId());
            numberEditView.setId(View.generateViewId());
            rlDeleteContainer.setId(View.generateViewId());

            RelativeLayout.LayoutParams rlIvDeleteContainerLayoutParams = new LayoutParams((int) (mBaseFragment.getScreenWidth() * 0.1), ViewGroup.LayoutParams.MATCH_PARENT);
            rlIvDeleteContainerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            rlDeleteContainer.setLayoutParams(rlIvDeleteContainerLayoutParams);

            ivDelete.setBackgroundResource(R.drawable.icon_delete);

            tvProductName.setTextColor(mBaseFragment.getColor(R.color.common_black));
            tvProductName.setTextSize(Constants.FONT_SIZE_NORMAL);

            tvProductPrice.setTextColor(mBaseFragment.getColor(R.color.common_red));
            tvProductPrice.setTextSize(Constants.FONT_SIZE_SMALLER);

            RelativeLayout.LayoutParams ivDeleteLayoutParams = new LayoutParams(mBaseFragment.getActualWidthOnThisDevice(40), mBaseFragment.getActualWidthOnThisDevice(40));
            ivDeleteLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, BaseFragment.LAYOUT_TRUE);
            ivDeleteLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, BaseFragment.LAYOUT_TRUE);
            ivDelete.setLayoutParams(ivDeleteLayoutParams);

            RelativeLayout.LayoutParams tvProductNameLayoutParams = new LayoutParams(mBaseFragment.getActualWidthOnThisDevice(200), mBaseFragment.getActualHeightOnThisDevice(40));
            tvProductNameLayoutParams.leftMargin = mBaseFragment.getActualWidthOnThisDevice(20);
            tvProductNameLayoutParams.topMargin = mBaseFragment.getActualHeightOnThisDevice(5);
            tvProductNameLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, BaseFragment.LAYOUT_TRUE);
            tvProductNameLayoutParams.addRule(RelativeLayout.RIGHT_OF, rlDeleteContainer.getId());
            tvProductName.setLayoutParams(tvProductNameLayoutParams);

            RelativeLayout.LayoutParams tvProductPriceLayoutParams = new LayoutParams(mBaseFragment.getActualWidthOnThisDevice(200), mBaseFragment.getActualHeightOnThisDevice(40));
            tvProductPriceLayoutParams.leftMargin = mBaseFragment.getActualWidthOnThisDevice(20);
            tvProductPriceLayoutParams.bottomMargin = mBaseFragment.getActualHeightOnThisDevice(5);
            tvProductPriceLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, BaseFragment.LAYOUT_TRUE);
            tvProductPriceLayoutParams.addRule(RelativeLayout.RIGHT_OF, rlDeleteContainer.getId());
            tvProductPrice.setLayoutParams(tvProductPriceLayoutParams);

            RelativeLayout.LayoutParams numberEditViewLayoutParams = new LayoutParams(mBaseFragment.getActualWidthOnThisDevice(200), mBaseFragment.getActualHeightOnThisDevice(60));
            numberEditViewLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, BaseFragment.LAYOUT_TRUE);
            numberEditViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, BaseFragment.LAYOUT_TRUE);
            numberEditViewLayoutParams.rightMargin = mBaseFragment.getActualWidthOnThisDevice(40);
            numberEditView.setLayoutParams(numberEditViewLayoutParams);

            numberEditView.setMinNum(1);
            numberEditView.setListener(new IteeNumberEditView.NumberEditListener() {
                @Override
                public void onAdd(int currentNum) {
                    shopsProduct.setProductNumber(currentNum);
                }

                @Override
                public void onMinus(int currentNum) {
                    shopsProduct.setProductNumber(currentNum);
                }
            });

            rlDeleteContainer.addView(ivDelete);
            addView(rlDeleteContainer);
            addView(tvProductName);
            addView(tvProductPrice);
            addView(numberEditView);

            AppUtils.addBottomSeparatorLine(this, mBaseFragment);
        }

        private void initData() {
            tvProductName.setText(shopsProduct.getProductName());
            tvProductPrice.setText(shopsProduct.getProductPrice());
            numberEditView.setCurrentNum(shopsProduct.getProductNumber());
        }

        public void setListenerDelete(AppUtils.NoDoubleClickListener listenerDelete) {
            rlDeleteContainer.setOnClickListener(listenerDelete);
        }
    }
}