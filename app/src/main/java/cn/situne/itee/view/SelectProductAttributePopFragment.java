/**
 * Project Name: itee
 * File Name:	 SelectProductAttributePopFragment.java
 * Package Name: cn.situne.itee.view
 * Date:		 2015-04-29
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.view;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.common.widget.wheel.BasePopFragment;
import cn.situne.itee.common.widget.wheel.SelectTimeOnWheelChangedListener;
import cn.situne.itee.common.widget.wheel.SelectTimeWheelAdapter;
import cn.situne.itee.common.widget.wheel.SelectTimeWheelView;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonProductProperty;

/**
 * ClassName:SelectProductAttributePopFragment <br/>
 * Function: Select attribute of product. <br/>
 * Date: 2015-04-29 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class SelectProductAttributePopFragment extends BasePopFragment {

    private String productId;
    private AppUtils.NoDoubleClickListener onClickListener;
    private ArrayList<JsonProductProperty.ProductTypeDetail> dataList;

    private ArrayList<SelectTimeWheelView> wheelViewArrayList;

    private ProductAttributeAdapter adapter;

    @Override
    public void createContent(LinearLayout mParent) {

    }

    private void initView() {
        wheelViewArrayList = new ArrayList<>();
        adapter = new ProductAttributeAdapter(dataList);

        RelativeLayout relativeLayout = generateWheels();

        getPanel().addView(relativeLayout);
    }

    private SelectTimeOnWheelChangedListener changedListener = new SelectTimeOnWheelChangedListener() {
        @Override
        public void onChanged(SelectTimeWheelView wheel, int oldValue, int newValue) {
            int idx = wheelViewArrayList.indexOf(wheel);
            if (idx + 1 < wheelViewArrayList.size()) {
                ProductAttributeAdapter currentLevelAdapter = (ProductAttributeAdapter) wheel.getAdapter();
                JsonProductProperty.ProductTypeDetail currentProductTypeDetail = currentLevelAdapter.getDataList().get(newValue);
                SelectTimeWheelView nextLevel = wheelViewArrayList.get(idx + 1);
                ProductAttributeAdapter adapter = new ProductAttributeAdapter(currentProductTypeDetail.getItems());
                nextLevel.setAdapter(adapter);
            }
        }
    };

    private RelativeLayout generateWheels() {
        RelativeLayout rlContainer = new RelativeLayout(getActivity());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getHeight(700));
        rlContainer.setLayoutParams(layoutParams);

        LinearLayout llWheelContainer = new LinearLayout(getActivity());
        llWheelContainer.setOrientation(LinearLayout.HORIZONTAL);

        int maxLevel = getMaxLevel(dataList, 0);
        for (int i = 0; i < maxLevel; i++) {
            SelectTimeWheelView wheelView = new SelectTimeWheelView(getActivity());
            if (i == 0) {
                wheelView.setAdapter(adapter);
                wheelView.setCurrentItem(0);
            }
            wheelView.addChangingListener(changedListener);

            wheelView.setBackgroundColor(Color.RED);

            LinearLayout.LayoutParams wheelViewLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            wheelViewLayoutParams.weight = 1;
            wheelView.setLayoutParams(wheelViewLayoutParams);

            llWheelContainer.addView(wheelView);

            wheelViewArrayList.add(wheelView);
        }

        RelativeLayout.LayoutParams llWheelContainerLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llWheelContainer.setLayoutParams(llWheelContainerLayoutParams);

        rlContainer.addView(llWheelContainer);

        return rlContainer;
    }

    public void getSubClass() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(baseFragment.getActivity()));
        params.put(ApiKey.SHOPS_PRODUCT_ID, productId);
        HttpManager<JsonProductProperty> hh = new HttpManager<JsonProductProperty>(baseFragment) {

            @Override
            public void onJsonSuccess(JsonProductProperty jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    dataList = jo.getDataList();
                    initView();
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }

            }

            @Override
            public void onJsonError(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(getActivity(), R.string.msg_common_network_error);
            }
        };
        hh.startGet(baseFragment.getActivity(), ApiManager.HttpApi.ShopsProductPropertyGet, params);

    }

    private int getMaxLevel(ArrayList<JsonProductProperty.ProductTypeDetail> dataList, Integer parentLevel) {
        int res = parentLevel;
        for (JsonProductProperty.ProductTypeDetail detail : dataList) {
            if (Utils.isListNotNullOrEmpty(detail.getItems())) {
                parentLevel++;
                res = getMaxLevel(detail.getItems(), parentLevel);
            }
        }
        return res;
    }

    public void setDataList(ArrayList<JsonProductProperty.ProductTypeDetail> dataList) {
        this.dataList = dataList;
    }

    public void setOnClickListener(AppUtils.NoDoubleClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public static Builder createBuilder(BaseFragment mBaseFragment,
                                        FragmentManager fragmentManager) {
        return new Builder(mBaseFragment, fragmentManager);
    }

    public static class Builder extends BasePopFragment.Builder<SelectProductAttributePopFragment> {

        private String productId;
        private AppUtils.NoDoubleClickListener onClickListener;
        private ArrayList<JsonProductProperty.ProductTypeDetail> dataList;

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

        public Builder setOnClickListener(AppUtils.NoDoubleClickListener onClickListener) {
            this.onClickListener = onClickListener;
            return this;
        }

        public Builder setDataList(ArrayList<JsonProductProperty.ProductTypeDetail> dataList) {
            this.dataList = dataList;
            return this;
        }

        public Builder setProductId(String productId) {
            this.productId = productId;
            return this;
        }

        public SelectProductAttributePopFragment show() {
            SelectProductAttributePopFragment fragment = (SelectProductAttributePopFragment) Fragment.instantiate(
                    mBaseFragment.getActivity(), SelectProductAttributePopFragment.class.getName(), prepareArguments());
            fragment.setDismissedListener(mListener);
            fragment.setBaseFragment(mBaseFragment);
            fragment.setDataList(dataList);
            fragment.setOnClickListener(onClickListener);
            fragment.setProductId(productId);
            fragment.show(mFragmentManager, mTag);
            fragment.getSubClass();
            return fragment;
        }
    }

    class ProductAttributeAdapter implements SelectTimeWheelAdapter {

        private ArrayList<JsonProductProperty.ProductTypeDetail> dataList;

        public ProductAttributeAdapter(ArrayList<JsonProductProperty.ProductTypeDetail> dataList) {
            this.dataList = dataList;
        }

        @Override
        public int getItemsCount() {
            if (Utils.isListNotNullOrEmpty(dataList)) {
                Log.e("DJZ", String.valueOf(dataList.size()));
                return dataList.size();
            }
            return 0;
        }

        @Override
        public String getItem(int index) {
            if (Utils.isListNotNullOrEmpty(dataList)) {
                return dataList.get(index).getPraName();
            }
            return Constants.STR_EMPTY;
        }

        @Override
        public int getMaximumLength() {
            return 0;
        }

        public ArrayList<JsonProductProperty.ProductTypeDetail> getDataList() {
            return dataList;
        }
    }

}