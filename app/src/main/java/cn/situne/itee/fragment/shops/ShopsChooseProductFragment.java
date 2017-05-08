/**
 * Project Name: itee
 * File Name:	 ShopsChooseProductFragment.java
 * Package Name: cn.situne.itee.fragment.shops
 * Date:		 2015-03-21
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */


package cn.situne.itee.fragment.shops;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.common.widget.wheel.BasePopFragment;
import cn.situne.itee.entity.ShopsProduct;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonChooseProduct;

import static cn.situne.itee.R.color.common_gray;

/**
 * ClassName:ShopsChooseProductFragment <br/>
 * Function: 05 选择产品 <br/>
 * UI:  05-04-01-03
 * Date: 2015-03-21 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */

public class ShopsChooseProductFragment extends BaseFragment {

    // shop names
    private TabPageIndicator tabPageIndicator;
    private ShopsChooseProductPagerAdapter adapterShopGoods;
    // fragment container
    private ArrayList<ShopsChooseProductItemFragment> fragments;

    private LinkedHashMap<String, ShopsProduct> packageMap;

    private ChooseProductMode mode = ChooseProductMode.ModePricingTable;

    private boolean isDoBack;
    private ImageView ivPackage;

    private View.OnClickListener listenerChoosePackage;

    private AppUtils.NoDoubleClickListener listenerOk;

    private ShopsChooseProductPackageEditFragment shopsChooseProductPackageEditFragment;

    private String fromPage;
    private ArrayList<ShopsProduct> selectedProductList;

    private String greenId;


    @Override
    protected int getFragmentId() {
        return R.layout.fragment_choose_product_list;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.common_edit;
    }

    @Override
    protected void initControls(View rootView) {

        packageMap = new LinkedHashMap<>();
        selectedProductList = new ArrayList<>();

        tabPageIndicator = (TabPageIndicator) rootView.findViewById(R.id.tab_page_indicator);
        ViewPager vpShops = (ViewPager) rootView.findViewById(R.id.vp_goods_list_pager);
        fragments = new ArrayList<>();
        adapterShopGoods = new ShopsChooseProductPagerAdapter(getChildFragmentManager(), fragments);
        vpShops.setAdapter(adapterShopGoods);
        tabPageIndicator.setViewPager(vpShops);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mode = ChooseProductMode.valueOf(bundle.getInt(TransKey.CHOOSE_MODE, 0));
            fromPage = bundle.getString(TransKey.SHOPS_FRAGMENT_NAME);
            greenId = bundle.getString(TransKey.COMMON_GREEN_ID);
            ArrayList<String> productStringList = bundle.getStringArrayList(TransKey.SHOPS_PRODUCT_LIST);
            if (productStringList != null) {
                for (String productString : productStringList) {
                    ShopsProduct shopsProduct = (ShopsProduct) Utils.getObjectFromString(productString);
                    if (shopsProduct != null) {
                        if (mode == ChooseProductMode.ModePackage) {
                            if (shopsProduct.getProductId().equals(greenId)) {
                                shopsProduct.setProductId(String.valueOf(greenId) + Constants.STR_GREEN_ID_SEPARATOR
                                        + String.valueOf(shopsProduct.getAttrId()));
                                packageMap.put(shopsProduct.getProductId(), shopsProduct);
                            } else {
                                if (packageMap.containsKey(shopsProduct.getProductId())) {
                                    shopsProduct.setProductNumber(shopsProduct.getProductNumber() + 1);
                                    packageMap.put(shopsProduct.getProductId(), shopsProduct);
                                } else {
                                    if (shopsProduct.getProductNumber() == 0) {
                                        shopsProduct.setProductNumber(1);
                                    }
                                    packageMap.put(shopsProduct.getProductId(), shopsProduct);
                                }
                            }
                        }
                        selectedProductList.add(shopsProduct);
                    }
                }
            }
        }
    }


    @Override
    protected void setDefaultValueOfControls() {

    }

    public void changeStateOfPackage() {
        if (mode == ChooseProductMode.ModePackage) {
            ArrayList<ShopsProduct> selectedProducts = new ArrayList<>();
            for (String key : packageMap.keySet()) {
                selectedProducts.add(packageMap.get(key));
            }
            for (ShopsChooseProductItemFragment itemFragment : fragments) {
                itemFragment.setSelectedProductList(selectedProducts);
                if (itemFragment.getRefreshListViewGoods() != null) {
                    itemFragment.getRefreshListViewGoods().getRefreshableView().invalidateViews();
                }
            }
        }
    }

    @Override
    protected void setListenersOfControls() {
        listenerChoosePackage = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (packageMap.size() == 0) {
                    Utils.showShortToast(getActivity(), R.string.msg_please_choose_product);
                    return;
                }
                if (shopsChooseProductPackageEditFragment != null) {
                    shopsChooseProductPackageEditFragment.dismiss();

                    changeStateOfPackage();

                } else {
                    ShopsChooseProductPackageEditFragment.Builder builder
                            = ShopsChooseProductPackageEditFragment.createBuilder(ShopsChooseProductFragment.this,
                            getFragmentManager(), packageMap);
                    shopsChooseProductPackageEditFragment = builder.setGreenId(greenId)
                            .setContainerFragment(ShopsChooseProductFragment.this).setListener(new BasePopFragment.OnDismissedListener() {
                                @Override
                                public void onDismissed() {
                                    if (isDoBack) {
                                        doSelectProduct();
                                    } else {
                                        shopsChooseProductPackageEditFragment = null;
                                    }
                                    changeStateOfPackage();
                                }
                            }).setCancelableOnTouchOutside(true).show();
                }
            }
        };

        listenerOk = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                if (shopsChooseProductPackageEditFragment != null) {
                    isDoBack = true;
                    shopsChooseProductPackageEditFragment.dismiss();

                } else {
                    doSelectProduct();
                }
            }
        };
    }

    @SuppressWarnings("unchecked")
    private void doSelectProduct() {
        ArrayList<String> packageProducts = new ArrayList<>();
        for (String key : packageMap.keySet()) {
            ShopsProduct shopsProduct = packageMap.get(key);
            if (shopsProduct.getProductId().startsWith(String.valueOf(greenId) + Constants.STR_GREEN_ID_SEPARATOR)) {
                shopsProduct.setProductId(greenId);
            }
            packageProducts.add(Utils.getStringFromObject(shopsProduct));
        }
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(TransKey.CHOOSE_PRODUCT_LIST, packageProducts);
        try {
            doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
        } catch (ClassNotFoundException e) {
            Utils.log(e.getMessage());
        }
    }


    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    public void onAfterApi() {
        if (ChooseProductMode.ModePackage == mode) {
            ivPackage.setVisibility(View.VISIBLE);
            changePackageIconState();
        }
    }

    private void getProductData() {
        adapterShopGoods.notifyDataSetChanged();
        tabPageIndicator.notifyDataSetChanged();

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));

        HttpManager<JsonChooseProduct> hh = new HttpManager<JsonChooseProduct>(ShopsChooseProductFragment.this) {

            @Override
            public void onJsonSuccess(JsonChooseProduct jo) {
                int returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    greenId = jo.getGreenId();
                    initData(jo);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
            }
        };
        hh.startGet(getBaseActivity(), ApiManager.HttpApi.ShopsChooseProductGet, params);
    }


    @Override
    protected void setPropertyOfControls() {

        tabPageIndicator.setTabWidth(getScreenWidth() / 4);
        RelativeLayout.LayoutParams layoutParamsTabPageIndicator
                = (RelativeLayout.LayoutParams) tabPageIndicator.getLayoutParams();
        layoutParamsTabPageIndicator.height = 80;
        tabPageIndicator.setLayoutParams(layoutParamsTabPageIndicator);

    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getString(R.string.event_choose_product));

        if (ChooseProductMode.ModePackage == mode) {

            getTvRight().setText(R.string.common_ok);
            getTvRight().setOnClickListener(listenerOk);

            RelativeLayout parent = (RelativeLayout) getTvRight().getParent();

            ivPackage = new ImageView(getActivity());
            ivPackage.setVisibility(View.INVISIBLE);
            changePackageIconState();
            ivPackage.setOnClickListener(listenerChoosePackage);

            RelativeLayout.LayoutParams ivPackageLayoutParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(60), getActualWidthOnThisDevice(60));
            ivPackageLayoutParams.addRule(RelativeLayout.LEFT_OF, getTvRight().getId());
            ivPackageLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
            ivPackageLayoutParams.rightMargin = getActualWidthOnThisDevice(20);
            ivPackage.setLayoutParams(ivPackageLayoutParams);

            parent.addView(ivPackage);

        }
    }

    @Override
    protected void executeOnceOnCreate() {
        getProductData();
    }

    public void changePackageIconState() {
        if (packageMap.size() > 0) {
            ivPackage.setImageResource(R.drawable.icon_choose_product_show_selected_package_have);
        } else {
            ivPackage.setImageResource(R.drawable.icon_choose_product_show_selected_package_none);
        }
        //set press down effect start
        ivPackage.setOnClickListener(listenerChoosePackage);
        RelativeLayout.LayoutParams ivPackageLayoutParams = new RelativeLayout.LayoutParams(getActionBarHeight(), getActionBarHeight());
        ivPackageLayoutParams.addRule(RelativeLayout.LEFT_OF, getTvRight().getId());
        ivPackageLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivPackage.setLayoutParams(ivPackageLayoutParams);
        //set press down effect end
    }

    private void initData(JsonChooseProduct jo) {

        for (JsonChooseProduct.SalesType salesType : jo.getSalesTypeList()) {
            ShopsChooseProductItemFragment shopsChooseProductItemFragment = new ShopsChooseProductItemFragment();
            shopsChooseProductItemFragment.setContainerFragment(this);
            shopsChooseProductItemFragment.setSalesType(salesType);
            shopsChooseProductItemFragment.setPackageMap(packageMap);
            shopsChooseProductItemFragment.setMode(mode);
            shopsChooseProductItemFragment.setGreenId(greenId);
            shopsChooseProductItemFragment.setFromPage(fromPage);
            shopsChooseProductItemFragment.setSelectedProductList(selectedProductList);
            shopsChooseProductItemFragment.setTypeId(salesType.getSalesTypeId());
            shopsChooseProductItemFragment.setShopName(salesType.getName());

            if (salesType.isPromote()) {
                if (mode == ChooseProductMode.ModePackage
                        || mode == ChooseProductMode.ModePromote
                        || mode == ChooseProductMode.ModePricingTable) {
                    continue;
                }
            } else if (salesType.isPackage()) {
                if (mode == ChooseProductMode.ModePackage
                        || mode == ChooseProductMode.ModePromote) {
                    continue;
                }
            }
            fragments.add(shopsChooseProductItemFragment);
        }

        adapterShopGoods.notifyDataSetChanged();
        tabPageIndicator.notifyDataSetChanged();
    }


    class ShopsChooseProductPagerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<ShopsChooseProductItemFragment> fragments;

        public ShopsChooseProductPagerAdapter(FragmentManager fm, ArrayList<ShopsChooseProductItemFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments.get(position).getShopName();
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }

    public enum ChooseProductMode {
        ModePricingTable(1),
        ModePromote(2),
        ModePackage(3);

        private int value = 0;

        ChooseProductMode(int value) {
            this.value = value;
        }

        public static ChooseProductMode valueOf(int value) {
            switch (value) {
                case 2:
                    return ModePromote;
                case 3:
                    return ModePackage;
                case 1:
                default:
                    return ModePricingTable;
            }
        }

        public int value() {
            return this.value;
        }
    }
}
