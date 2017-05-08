/**
 * Project Name: itee
 * File Name:	 ShopsSelectRentalProductFragment.java
 * Package Name: cn.situne.itee.fragment.shops
 * Date:		 2015-04-11
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.fragment.shops;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.widget.wheel.BasePopFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:ShopsSelectRentalProductFragment <br/>
 * Function: . <br/>
 * Date: 2015-04-11 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class ShopsSelectRentalProductFragment extends BasePopFragment {

    private SelectedShopsRentalProductType selectedListener;
    private ArrayList<String> rentalProductIds;

    @Override
    public void createContent(LinearLayout mParent) {

        for (String typeId : rentalProductIds) {
            RentalProductTypeLayout rentalProductTypeLayout = generateRentalProductLayout(typeId);
            rentalProductTypeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (selectedListener != null) {
                        RentalProductTypeLayout rentalType = (RentalProductTypeLayout) v;
                        selectedListener.onSelected(rentalType.getTypeId(), rentalType.getStringResId(), rentalType.getIconResId());
                    }
                }
            });
            AppUtils.addBottomSeparatorLine(rentalProductTypeLayout, getActivity());
            mParent.addView(rentalProductTypeLayout);
        }
    }

    private RentalProductTypeLayout generateRentalProductLayout(String typeId) {
        return new RentalProductTypeLayout(this, typeId);
    }

    public static Builder createBuilder(BaseFragment mBaseFragment,
                                        FragmentManager fragmentManager) {
        return new Builder(mBaseFragment, fragmentManager);
    }

    public void setSelectedListener(SelectedShopsRentalProductType selectedListener) {
        this.selectedListener = selectedListener;
    }

    public void setRentalProductIds(ArrayList<String> rentalProductIds) {
        this.rentalProductIds = rentalProductIds;
    }

    public static class Builder extends BasePopFragment.Builder<ShopsSelectRentalProductFragment> {

        private SelectedShopsRentalProductType selectedListener;
        private ArrayList<String> rentalProductIds;

        public Builder(BaseFragment mBaseFragment, FragmentManager fragmentManager) {
            super(mBaseFragment, fragmentManager);
            super.setCancelableOnTouchOutside(true);
        }

        @Override
        public Builder setCancelableOnTouchOutside(boolean cancelable) {
            return (Builder) super.setCancelableOnTouchOutside(cancelable);
        }

        @Override
        public Builder setListener(OnDismissedListener listener) {
            return (Builder) super.setListener(listener);
        }

        public Builder setSelectedListener(SelectedShopsRentalProductType selectedListener) {
            this.selectedListener = selectedListener;
            return this;
        }

        public Builder setRentalProductIds(ArrayList<String> rentalProductIds) {
            this.rentalProductIds = rentalProductIds;
            return this;
        }

        public ShopsSelectRentalProductFragment show() {
            ShopsSelectRentalProductFragment fragment = (ShopsSelectRentalProductFragment) Fragment.instantiate(
                    mBaseFragment.getActivity(), ShopsSelectRentalProductFragment.class.getName(), prepareArguments());
            fragment.setDismissedListener(mListener);
            fragment.setBaseFragment(mBaseFragment);
            fragment.setRentalProductIds(rentalProductIds);
            fragment.setSelectedListener(selectedListener);
            fragment.show(mFragmentManager, mTag);
            return fragment;
        }
    }

    class RentalProductTypeLayout extends RelativeLayout {

        private BasePopFragment mPopFragment;
        private String typeId;
        private int stringResId;
        private int iconResId;

        private ImageView ivIcon;
        private IteeTextView tvTypeName;

        public RentalProductTypeLayout(BasePopFragment mPopFragment, String typeId) {
            super(mPopFragment.getActivity());
            this.mPopFragment = mPopFragment;
            this.typeId = typeId;
            init();
        }

        private void init() {

            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mPopFragment.getHeight(100));
            setLayoutParams(layoutParams);

            ivIcon = new ImageView(mPopFragment.getActivity());

            ivIcon.setId(View.generateViewId());
            iconResId = getTypeIcon(typeId);
            ivIcon.setImageResource(iconResId);

            RelativeLayout.LayoutParams ivIconLayoutParams = new LayoutParams(mPopFragment.getWidth(60), mPopFragment.getWidth(60));
            ivIconLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            ivIconLayoutParams.leftMargin = mPopFragment.getWidth() / 2 - mPopFragment.getWidth(100);
            ivIcon.setLayoutParams(ivIconLayoutParams);

            tvTypeName = new IteeTextView(mPopFragment.getActivity());

            tvTypeName.setId(ViewGroup.generateViewId());
            stringResId = getTypeName(typeId);
            tvTypeName.setText(stringResId);

            RelativeLayout.LayoutParams tvTypeNameLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            tvTypeNameLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            tvTypeNameLayoutParams.addRule(RelativeLayout.RIGHT_OF, ivIcon.getId());
            tvTypeNameLayoutParams.leftMargin = mPopFragment.getWidth(5);
            tvTypeName.setLayoutParams(tvTypeNameLayoutParams);

            addView(ivIcon);
            addView(tvTypeName);
        }

        private int getTypeName(String typeId) {
            int res;
            if (Constants.RENTAL_PRODUCT_TYPE_CADDIE.equals(typeId)) {
                res = R.string.rental_type_caddie;
            } else if (Constants.RENTAL_PRODUCT_TYPE_CART.equals(typeId)) {
                res = R.string.rental_type_cart;
            } else if (Constants.RENTAL_PRODUCT_TYPE_CLUBS.equals(typeId)) {
                res = R.string.rental_type_clubs;
            } else if (Constants.RENTAL_PRODUCT_TYPE_SHOES.equals(typeId)) {
                res = R.string.rental_type_shoes;
            } else if (Constants.RENTAL_PRODUCT_TYPE_TROLLEY.equals(typeId)) {
                res = R.string.rental_type_trolley;
            } else if (Constants.RENTAL_PRODUCT_TYPE_UMBRELLA.equals(typeId)) {
                res = R.string.rental_type_umbrella;
            } else {
                res = R.string.rental_type_towel;
            }
            return res;
        }

        private int getTypeIcon(String typeId) {
            int res;
            if (Constants.RENTAL_PRODUCT_TYPE_CADDIE.equals(typeId)) {
                res = R.drawable.icon_shops_product_edit_caddie_select;
            } else if (Constants.RENTAL_PRODUCT_TYPE_CART.equals(typeId)) {
                res = R.drawable.icon_shops_product_edit_cart_select;
            } else if (Constants.RENTAL_PRODUCT_TYPE_CLUBS.equals(typeId)) {
                res = R.drawable.icon_shops_product_edit_clubs_select;
            } else if (Constants.RENTAL_PRODUCT_TYPE_SHOES.equals(typeId)) {
                res = R.drawable.icon_shops_product_edit_shoes_select;
            } else if (Constants.RENTAL_PRODUCT_TYPE_TROLLEY.equals(typeId)) {
                res = R.drawable.icon_shops_product_edit_trolley_select;
            } else if (Constants.RENTAL_PRODUCT_TYPE_UMBRELLA.equals(typeId)) {
                res = R.drawable.icon_shops_product_edit_umbrella_select;
            } else {
                res = R.drawable.icon_shops_product_edit_towel_select;
            }
            return res;
        }

        public String getTypeId() {
            return typeId;
        }

        public int getStringResId() {
            return stringResId;
        }

        public int getIconResId() {
            return iconResId;
        }
    }

    public interface SelectedShopsRentalProductType {
        void onSelected(String rentalTypeId, int stringResId, int iconResId);
    }
}