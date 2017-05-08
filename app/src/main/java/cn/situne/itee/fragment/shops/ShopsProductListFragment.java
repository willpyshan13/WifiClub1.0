/**
 * Project Name: itee
 * File Name:  ShopsProductListFragment.java
 * Package Name: cn.situne.itee.fragment.shops
 * Date:   2015-04-22
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.fragment.shops;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.adapter.ShopsProductAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonProduct;

/**
 * ClassName:ShopsProductListFragment <br/>
 * Function: show product list. <br/>
 * Date: 2015-04-22 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class ShopsProductListFragment extends BaseFragment {

    private PullToRefreshListView productListView;

    private ShopsProductAdapter shopsProductAdapter;

    private AppUtils.NoDoubleClickListener listenerAdd;

    private ArrayList<JsonProduct.ProductData> productDataList = new ArrayList<>();

    private int currentPage = 1;

    private int shopId;

    private String shopName;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_shops_product_list;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        productListView = (PullToRefreshListView) rootView.findViewById(R.id.lv_product_list);
        productListView.setBackgroundColor(getColor(R.color.common_white));
        Bundle bundle = getArguments();
        shopId = bundle.getInt(TransKey.COMMON_SHOP_ID);
        shopName = bundle.getString(TransKey.COMMON_SHOP_NAME);
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {

        productListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getProduct(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getProduct(false);
            }
        });


        listenerAdd = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeAdd.value());
                bundle.putInt(TransKey.COMMON_SHOP_ID, shopId);
                bundle.putString(TransKey.COMMON_SHOP_NAME, shopName);
                bundle.putString(TransKey.UNLIMITED_FLAG,Constants.UNLIMITED_FLAG_OFF);
                push(ShopsProductEditFragment.class, bundle);
            }
        };

        AdapterView.OnItemClickListener listerEdit = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                position = position - 1;
                Bundle bundle = new Bundle();

                JsonProduct.ProductData productData = productDataList.get(position);
                bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeBrowse.value());
                bundle.putString(TransKey.COMMON_PRODUCT_ID, productData.getId());
                bundle.putString(TransKey.COMMON_PRODUCT_NAME, productData.getName());


                bundle.putString(TransKey.UNLIMITED_FLAG, productData.getUnlimitedFlag());

                bundle.putString(TransKey.COMMON_PRODUCT_QTY, productData.getQty());
                bundle.putString(TransKey.COMMON_PRODUCT_CODE, productData.getCode());
                bundle.putString(TransKey.COMMON_PRODUCT_PRICE, productData.getPrice());
                bundle.putString(TransKey.COMMON_SHOP_NAME, shopName);
                bundle.putInt(TransKey.COMMON_PRODUCT_ENABLE_PROPERTY, productData.getEnableProperty());
                push(ShopsProductEditFragment.class, bundle);
            }
        };

        productListView.setOnItemClickListener(listerEdit);


    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {

        productListView.setMode(PullToRefreshBase.Mode.BOTH);

        ILoadingLayout headerLayoutProxy = productListView.getLoadingLayoutProxy(true, false);
        ILoadingLayout footerLayoutProxy = productListView.getLoadingLayoutProxy(false, true);

        headerLayoutProxy.setPullLabel(getString(R.string.app_list_header_refresh_down));
        headerLayoutProxy.setRefreshingLabel(getString(R.string.app_list_header_refresh_down));
        headerLayoutProxy.setReleaseLabel(getString(R.string.app_list_header_refresh_down));

        footerLayoutProxy.setPullLabel(getString(R.string.app_list_header_refresh_loading_more));
        footerLayoutProxy.setRefreshingLabel(getString(R.string.app_list_header_refresh_loading_more));
        footerLayoutProxy.setReleaseLabel(getString(R.string.app_list_header_refresh_loading_more));
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(shopName);
        getTvRight().setBackgroundResource(R.drawable.icon_common_add);
        getTvRight().setOnClickListener(listenerAdd);

    }


    private void getProduct(final boolean isRefresh) {

        if (isRefresh) {
            currentPage = 1;
        } else {
            currentPage++;
        }

        Map<String, String> params = new HashMap<>();

        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_PRODUCT_TYPE_TYPE_ID, String.valueOf(shopId));
        params.put(ApiKey.SHOPS_PRODUCT_SETTING_STATUS, Constants.STR_1);// product setting is 1
        params.put(ApiKey.COMMON_PAGE, String.valueOf(currentPage));

        HttpManager<JsonProduct> hh = new HttpManager<JsonProduct>(ShopsProductListFragment.this) {

            @Override
            public void onJsonSuccess(JsonProduct jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    if (isRefresh) {
                        productDataList.clear();
                    }
                    configNormalData(jo);
                    shopsProductAdapter.notifyDataSetChanged();
                    productListView.onRefreshComplete();
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }

            }

            @Override
            public void onJsonError(VolleyError error) {
                productListView.onRefreshComplete();
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(getActivity(), R.string.msg_common_network_error);
            }
        };
        hh.startGet(this.getActivity(), ApiManager.HttpApi.ShopsProductGet, params);
    }


    private void configNormalData(JsonProduct jo) {

        for (JsonProduct.ProductData productData : jo.getProductList()) {
            productDataList.add(productData);
        }

    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        getFirstData();
    }

    private void getFirstData() {
        Map<String, String> params = new HashMap<>();

        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_PRODUCT_TYPE_TYPE_ID, String.valueOf(shopId));
        params.put(ApiKey.SHOPS_PRODUCT_SETTING_STATUS, Constants.STR_1);// product setting is 1
        params.put(ApiKey.COMMON_PAGE, String.valueOf(currentPage));
        HttpManager<JsonProduct> hh = new HttpManager<JsonProduct>(ShopsProductListFragment.this) {

            @Override
            public void onJsonSuccess(JsonProduct jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    productDataList.clear();
                    configNormalData(jo);
                    shopsProductAdapter = new ShopsProductAdapter(ShopsProductListFragment.this, productDataList);

                    productListView.setAdapter(shopsProductAdapter);

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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.ShopsProductGet, params);
    }
}
