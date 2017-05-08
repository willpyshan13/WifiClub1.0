/**
 * Project Name: itee
 * File Name:	 ShopsChooseProductItemFragment.java
 * Package Name: cn.situne.itee.fragment.shops
 * Date:		 2015-03-21
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.shops;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

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
import cn.situne.itee.entity.ShopsProduct;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonChooseProduct;
import cn.situne.itee.manager.jsonentity.JsonPackageGet;
import cn.situne.itee.manager.jsonentity.JsonProduct;
import cn.situne.itee.manager.jsonentity.JsonPromote;
import cn.situne.itee.view.ShopsProductWithPicture;
import cn.situne.itee.view.ShopsSingleProductLine;
import cn.situne.itee.view.ShopsTripleProductLine;

/**
 * ClassName:ShopsChooseProductItemFragment <br/>
 * Function: 05 选择商品 子画面. <br/>
 * UI:  05-04-01-03
 * Date: 2015-03-21 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */

public class ShopsChooseProductItemFragment extends BaseFragment {

    private String greenId;
    private String typeId;
    private String shopName;
    private RelativeLayout rlContainer;

    private JsonChooseProduct.SalesType salesType;
    private ShopsChooseProductFragment containerFragment;

    private LinkedHashMap<String, ShopsProduct> packageMap;
    private ShopsChooseProductFragment.ChooseProductMode mode;

    private PullToRefreshListView refreshListViewGoods;
    private ShopChooseProductAdapter shopChooseProductAdapter;

    private int currentPage;

    private String fromPage;
    private ArrayList<ShopsProduct> selectedProductList = new ArrayList<>();
    private ArrayList<String> selectedProductIdList = new ArrayList<>();

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_shop_choose_product_item;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        currentPage = 1;

        rlContainer = (RelativeLayout) rootView.findViewById(R.id.rl_content_container);

        if (salesType != null) {
            refreshListViewGoods = new PullToRefreshListView(mContext);
            shopChooseProductAdapter = new ShopChooseProductAdapter(this, salesType);
        }
        initData();
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {

    }

    @Override
    protected void setLayoutOfControls() {

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        refreshListViewGoods.setLayoutParams(layoutParams);

        rlContainer.addView(refreshListViewGoods);
    }

    @Override
    protected void setPropertyOfControls() {
        refreshListViewGoods.setAdapter(shopChooseProductAdapter);
        shopChooseProductAdapter.notifyDataSetChanged();
        if (Constants.SHOP_TYPE_RENTAL.equals(typeId)) {
            refreshListViewGoods.setMode(PullToRefreshBase.Mode.DISABLED);
        } else {
            refreshListViewGoods.setMode(PullToRefreshBase.Mode.BOTH);
        }

        ILoadingLayout headerLayoutProxy = refreshListViewGoods.getLoadingLayoutProxy(true, false);
        ILoadingLayout footerLayoutProxy = refreshListViewGoods.getLoadingLayoutProxy(false, true);

        headerLayoutProxy.setPullLabel(getString(R.string.app_list_header_refresh_down));
        headerLayoutProxy.setRefreshingLabel(getString(R.string.app_list_header_refresh_down));
        headerLayoutProxy.setReleaseLabel(getString(R.string.app_list_header_refresh_down));

        footerLayoutProxy.setPullLabel(getString(R.string.app_list_header_refresh_loading_more));
        footerLayoutProxy.setRefreshingLabel(getString(R.string.app_list_header_refresh_loading_more));
        footerLayoutProxy.setReleaseLabel(getString(R.string.app_list_header_refresh_loading_more));

        RelativeLayout rl = new RelativeLayout(getActivity());
        ImageView ivNoData = new ImageView(getActivity());
        ivNoData.setImageResource(R.drawable.bg_shop_no_data);
        refreshListViewGoods.setEmptyView(rl);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, LAYOUT_TRUE);
        ivNoData.setLayoutParams(layoutParams);
        rl.addView(ivNoData);
    }

    @Override
    protected void configActionBar() {

    }

    @Override
    protected void executeOnceOnCreate() {
        switch (typeId) {
            case Constants.SHOP_TYPE_RENTAL:
            case Constants.SHOP_TYPE_PROMOTE:
            case Constants.SHOP_TYPE_PACKAGE:
                break;
            default:
                getProduct(true);
                break;
        }
    }

    public void setContainerFragment(ShopsChooseProductFragment containerFragment) {
        this.containerFragment = containerFragment;
    }

    private void initData() {

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasAttr;
                JsonChooseProduct.SalesType.SalesTypeProduct product;
                if (v instanceof ShopsProductWithPicture) {
                    ShopsProductWithPicture shopsProductWithPicture = (ShopsProductWithPicture) v;
                    product = shopsProductWithPicture.getProduct();
                    hasAttr = product.getAttrCount() > 0;
                } else {
                    ShopsSingleProductLine singleProductLine = (ShopsSingleProductLine) v;
                    product = singleProductLine.getProduct();
                    hasAttr = product.getAttrCount() > 0;
                }

                Utils.log("isCaddie : " + product.isCaddie());

                String productId = product.getId();
                if (Utils.isStringNotNullOrEmpty(productId)) {
                    if (ShopsChooseProductFragment.ChooseProductMode.ModePackage == mode) {
//                        if (hasAttr && product.getPropertyPriceStatus() == 1) {
//                            chooseProductAttribute(productId, product.getName());
//                        } else {
                            if (greenId.equals(productId)) {
                                productId = String.valueOf(greenId) + Constants.STR_GREEN_ID_SEPARATOR + product.getAttrId();
                            }
                            if (packageMap.containsKey(productId)) {
                                ShopsProduct shopsProduct = packageMap.get(productId);
                                shopsProduct.setProductNumber(shopsProduct.getProductNumber() + 1);
                                packageMap.put(productId, shopsProduct);
                            } else {
                                ShopsProduct shopsProduct = getShopsProduct(product);
                                shopsProduct.setProductNumber(1);
                                packageMap.put(productId, shopsProduct);
                            }
                            selectedProductIdList.add(productId);
                            if (v instanceof ShopsSingleProductLine) {
                                ShopsSingleProductLine singleProductLine = (ShopsSingleProductLine) v;
                                singleProductLine.setOnClickListener(null);
                                singleProductLine.setBackgroundColor(getColor(R.color.common_deep_gray));
                            }
                            refreshListViewGoods.getRefreshableView().invalidateViews();

                            containerFragment.changePackageIconState();
//                        }
                    } else if (ShopsChooseProductFragment.ChooseProductMode.ModePromote == mode) {
                        if (hasAttr) {
                            chooseProductAttribute(productId, product.getName());
                        } else {
                            chooseProduct(product);
                        }
                    } else {
                        if (hasAttr && product.getPropertyPriceStatus() == 1) {
                            chooseProductAttribute(productId, product.getName());
                        } else {
                            chooseProduct(product);
                        }
                    }
                }
            }
        };
        shopChooseProductAdapter.setListener(listener);

        refreshListViewGoods.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                switch (typeId) {
                    case Constants.SHOP_TYPE_RENTAL:
                        break;
                    case Constants.SHOP_TYPE_PROMOTE:
                        getPromote(true);
                        break;
                    case Constants.SHOP_TYPE_PACKAGE:
                        getPackage(true);
                        break;
                    default:
                        getProduct(true);
                        break;
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                switch (typeId) {
                    case Constants.SHOP_TYPE_RENTAL:
                        break;
                    case Constants.SHOP_TYPE_PROMOTE:
                        getPromote(false);
                        break;
                    case Constants.SHOP_TYPE_PACKAGE:
                        getPackage(false);
                        break;
                    default:
                        getProduct(false);
                        break;
                }
            }
        });
    }

    public ShopsProduct getShopsProduct(JsonChooseProduct.SalesType.SalesTypeProduct product) {
        ShopsProduct shopsProduct = new ShopsProduct();
        shopsProduct.setProductId(product.getId());
        shopsProduct.setProductName(product.getName());
        shopsProduct.setProductPrice(product.getPrice());
        shopsProduct.setAttrId(product.getAttrId());
        shopsProduct.setTypeTagId(typeId);
        return shopsProduct;
    }

    private ArrayList<String> generateSelectedProductIdList() {
        ArrayList<String> res = new ArrayList<>();
        if (selectedProductList != null) {
            for (ShopsProduct shopsProduct : selectedProductList) {
                String productId = shopsProduct.getProductId();
                if (productId.equals(greenId)) {
                    productId = String.valueOf(greenId) + Constants.STR_GREEN_ID_SEPARATOR + shopsProduct.getAttrId();
                }
                if (!res.contains(productId)) {
                    res.add(productId);
                }
            }
        }
        return res;
    }

    private void chooseProductAttribute(String productId, String productName) {
        Bundle bundle = new Bundle();
        bundle.putString(TransKey.COMMON_PRODUCT_ID, productId);
        bundle.putString(TransKey.COMMON_PRODUCT_NAME, productName);
        bundle.putInt(TransKey.CHOOSE_MODE, mode.value());
        bundle.putString(TransKey.COMMON_FROM_PAGE, fromPage);
        bundle.putStringArrayList(TransKey.CHOOSE_PRODUCT_LIST, generateSelectedProductAttr(productId));
        push(ShopsChooseProductAttributeFragment.class, bundle);
    }

    @SuppressWarnings("unchecked")
    private void chooseProduct(JsonChooseProduct.SalesType.SalesTypeProduct product) {
        ShopsProduct shopsProduct = new ShopsProduct();
        shopsProduct.setProductNumber(1);
        shopsProduct.setIsCaddie(product.isCaddie());
        shopsProduct.setProductId(product.getId());
        shopsProduct.setQty(product.getQty());
        shopsProduct.setUnlimitedFlag(product.getUnlimitedFlag());
        shopsProduct.setProductName(product.getName());
        shopsProduct.setProductPrice(product.getPrice());
        if (Utils.isStringNotNullOrEmpty(product.getAttrId())) {
            shopsProduct.setAttrId(product.getAttrId());
        }
        shopsProduct.setTypeTagId(typeId);
        if (typeId.equals(Constants.SHOP_TYPE_PACKAGE)) {
            shopsProduct.setPackageId(product.getId());

            ArrayList<ShopsProduct.ShopsSubProduct> subProducts = new ArrayList<>();

            for (int i = 0; i < product.getSubProductList().size(); i++) {
                JsonChooseProduct.SalesType.SalesTypeProduct.SalesTypeSubProduct packageSubProduct = product.getSubProductList().get(i);
                ShopsProduct.ShopsSubProduct shopsSubProduct = new ShopsProduct.ShopsSubProduct();
                shopsSubProduct.setId(String.valueOf(i));
                shopsSubProduct.setProductId(packageSubProduct.getProductId());
                shopsSubProduct.setProductAttr(packageSubProduct.getProductAttrId());
                shopsSubProduct.setProductName(packageSubProduct.getProductName());
                shopsSubProduct.setProductPrice(packageSubProduct.getPrice());
                subProducts.add(shopsSubProduct);
            }
            shopsProduct.setProductList(subProducts);
        }

        ArrayList<String> productList = new ArrayList<>();
        productList.add(Utils.getStringFromObject(shopsProduct));
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(TransKey.CHOOSE_PRODUCT_LIST, productList);
        try {
            doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
        } catch (ClassNotFoundException e) {
            Utils.log(e.getMessage());
        }
    }

    private ArrayList<String> generateSelectedProductAttr(String productId) {
        ArrayList<String> res = new ArrayList<>();
        for (ShopsProduct shopsProduct : selectedProductList) {
            if (productId.equals(shopsProduct.getProductId())) {
                res.add(shopsProduct.getAttrId());
            }
        }
        return res;
    }

    private void getPromote(final boolean isRefresh) {

        if (isRefresh) {
            currentPage = 1;
        } else {
            currentPage++;
        }

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_PAGE, String.valueOf(currentPage));

        HttpManager<JsonPromote> hh = new HttpManager<JsonPromote>(ShopsChooseProductItemFragment.this) {

            @Override
            public void onJsonSuccess(JsonPromote jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    if (isRefresh) {
                        salesType.getSalesTypeList().clear();
                    }
                    if (jo.getPromoteList().size() > 0) {
                        currentPage = jo.getPage();
                    }
                    configPromoteData(jo.getPromoteList());
                    shopChooseProductAdapter.notifyDataSetChanged();
                    refreshListViewGoods.onRefreshComplete();
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }

            }

            @Override
            public void onJsonError(VolleyError error) {
                refreshListViewGoods.onRefreshComplete();
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(getActivity(), R.string.msg_common_network_error);
            }
        };
        hh.startGet(this.getActivity(), ApiManager.HttpApi.ShopsPromoteGet, params);
    }


    private void getPackage(final boolean isRefresh) {

        if (isRefresh) {
            currentPage = 1;
        } else {
            currentPage++;
        }

        Map<String, String> params = new HashMap<>();

        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_PAGE, String.valueOf(currentPage));

        HttpManager<JsonPackageGet> hh = new HttpManager<JsonPackageGet>(ShopsChooseProductItemFragment.this) {

            @Override
            public void onJsonSuccess(JsonPackageGet jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    if (isRefresh) {
                        salesType.getSalesTypeList().clear();
                    }
                    if (jo.getPackageList().size() > 0) {
                        currentPage = jo.getPage();
                    }
                    configPackageData(jo.getPackageList());
                    shopChooseProductAdapter.notifyDataSetChanged();
                    refreshListViewGoods.onRefreshComplete();
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }

            }

            @Override
            public void onJsonError(VolleyError error) {
                refreshListViewGoods.onRefreshComplete();
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(getActivity(), R.string.msg_common_network_error);
            }
        };
        hh.startGet(this.getActivity(), ApiManager.HttpApi.ShopsPackageGet, params);
    }

    private void getProduct(final boolean isRefresh) {

        if (isRefresh) {
            currentPage = 1;
        } else {
            currentPage++;
        }

        Map<String, String> params = new HashMap<>();

        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_PRODUCT_TYPE_TYPE_ID, String.valueOf(typeId));
        params.put(ApiKey.COMMON_PAGE, String.valueOf(currentPage));

        HttpManager<JsonProduct> hh = new HttpManager<JsonProduct>(ShopsChooseProductItemFragment.this) {

            @Override
            public void onJsonSuccess(JsonProduct jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    if (isRefresh) {
                        salesType.getSalesTypeList().clear();
                    }
                    if (jo.getProductList().size() > 0) {
                        currentPage = jo.getPage();
                    }
                    configNormalData(jo.getProductList());
                    shopChooseProductAdapter.notifyDataSetChanged();
                    refreshListViewGoods.onRefreshComplete();
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }

            }

            @Override
            public void onJsonError(VolleyError error) {
                refreshListViewGoods.onRefreshComplete();
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(getActivity(), R.string.msg_common_network_error);
            }
        };
        hh.startGet(this.getActivity(), ApiManager.HttpApi.ShopsProductGet, params);
    }

    private void configPromoteData(ArrayList<JsonPromote.Promote> promoteList) {
        for (JsonPromote.Promote promote : promoteList) {
            JsonChooseProduct.SalesType.SalesTypeProduct product = new JsonChooseProduct.SalesType.SalesTypeProduct();
            product.setId(String.valueOf(promote.getPromoteId()));
            product.setImgPath(promote.getPromoteImg());
            product.setName(promote.getPromoteName());
            product.setStartDate(promote.getStartDate());
            product.setEndDate(promote.getEndDate());
            product.setQty(promote.getQty());
            product.setCode(promote.getCode());

            product.setUnlimitedFlag(promote.getUnlimitedFlag());
            product.setPrice(promote.getPrice());
            for (JsonPromote.Promote.Product promoteData : promote.getProductList()) {
                JsonChooseProduct.SalesType.SalesTypeProduct.SalesTypeSubProduct subProduct = new JsonChooseProduct.SalesType.SalesTypeProduct.SalesTypeSubProduct();
                subProduct.setId(promoteData.getId());
                subProduct.setProductId(promoteData.getProductId());

                subProduct.setProductName(promoteData.getPdName());
                subProduct.setProductAttr(promoteData.getProductAttr());
                subProduct.setPrice(promoteData.getPrice());
                subProduct.setPromotePrice(promoteData.getPromotePrice());
                subProduct.setType(String.valueOf(promoteData.getType()));
                product.getSubProductList().add(subProduct);
            }
            salesType.getSalesTypeList().add(product);
        }
    }

    private void configPackageData(ArrayList<JsonPackageGet.PackageData> packageList) {
        for (JsonPackageGet.PackageData packageData : packageList) {
            JsonChooseProduct.SalesType.SalesTypeProduct product = new JsonChooseProduct.SalesType.SalesTypeProduct();
            product.setId(String.valueOf(packageData.getPackageId()));
            product.setName(packageData.getPackageName());
            product.setQty(String.valueOf(packageData.getPackageQty()));
            product.setCode(packageData.getPackageCode());
            product.setPrice(String.valueOf(packageData.getPackagePrice()));
            product.setUnlimitedFlag(packageData.getUnlimitedFlag());
            for (JsonPackageGet.DataItem dataItem : packageData.getProductList()) {
                JsonChooseProduct.SalesType.SalesTypeProduct.SalesTypeSubProduct subProduct = new JsonChooseProduct.SalesType.SalesTypeProduct.SalesTypeSubProduct();
                subProduct.setId(dataItem.getId());
                subProduct.setProductId(dataItem.getProductId());
                subProduct.setProductName(dataItem.getPdName());
                subProduct.setProductAttr(dataItem.getProductAttr());
                subProduct.setPrice(String.valueOf(dataItem.getPrice()));
                subProduct.setNumber(String.valueOf(dataItem.getNumber()));
                subProduct.setType(String.valueOf(dataItem.getType()));
                product.getSubProductList().add(subProduct);
            }
            salesType.getSalesTypeList().add(product);
        }
    }

    private void configNormalData(ArrayList<JsonProduct.ProductData> productList) {
        for (JsonProduct.ProductData productData : productList) {
            JsonChooseProduct.SalesType.SalesTypeProduct product = new JsonChooseProduct.SalesType.SalesTypeProduct();
            product.setId(String.valueOf(productData.getId()));
            product.setName(productData.getName());
            product.setQty(String.valueOf(productData.getQty()));
            product.setCode(productData.getCode());
            product.setPrice(String.valueOf(productData.getPrice()));
            product.setUnlimitedFlag(productData.getUnlimitedFlag());
            product.setAttrCount(productData.getAttrCount());
            salesType.getSalesTypeList().add(product);
        }
    }

    public void setSelectedProductList(ArrayList<ShopsProduct> selectedProductList) {
        if (selectedProductList != null) {
            this.selectedProductList.clear();
            this.selectedProductList.addAll(selectedProductList);
            selectedProductIdList = generateSelectedProductIdList();
        }
    }

    public void setFromPage(String fromPage) {
        this.fromPage = fromPage;
    }

    public void setGreenId(String greenId) {
        this.greenId = greenId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setPackageMap(LinkedHashMap<String, ShopsProduct> packageMap) {
        this.packageMap = packageMap;
    }

    public void setMode(ShopsChooseProductFragment.ChooseProductMode mode) {
        this.mode = mode;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public void setSalesType(JsonChooseProduct.SalesType salesType) {
        this.salesType = salesType;
    }

    public PullToRefreshListView getRefreshListViewGoods() {
        return refreshListViewGoods;
    }

    class ShopChooseProductAdapter extends BaseAdapter {

        private BaseFragment mBaseFragment;
        private JsonChooseProduct.SalesType salesType;

        private View.OnClickListener listener;

        public ShopChooseProductAdapter(BaseFragment mBaseFragment, JsonChooseProduct.SalesType salesType) {
            this.mBaseFragment = mBaseFragment;
            this.salesType = salesType;
        }

        @Override
        public int getCount() {
            int count = salesType.getSalesTypeList().size();
            if (salesType.isPromote()) {
                return count % 3 == 0 ? count / 3 : count / 3 + 1;
            } else {
                return count;
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            RelativeLayout container = (RelativeLayout) convertView;
            ViewHolder viewHolder;
            JsonChooseProduct.SalesType.SalesTypeProduct product;

            if (container == null) {

                container = new RelativeLayout(mBaseFragment.getActivity());
                viewHolder = new ViewHolder();

                int height;
                if (salesType.isPromote()) {
                    height = getActualHeightOnThisDevice(260);
                    viewHolder.shopsTripleProductLine = new ShopsTripleProductLine(mBaseFragment);
                    viewHolder.shopsTripleProductLine.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    container.addView(viewHolder.shopsTripleProductLine);
                } else {
                    height = getActualHeightOnThisDevice(100);
                    viewHolder.singleProductLine = new ShopsSingleProductLine(mBaseFragment);
                    viewHolder.singleProductLine.setOnClickListener(listener);
                    container.addView(viewHolder.singleProductLine);
                }

                ListView.LayoutParams layoutParams = new ListView.LayoutParams(MATCH_PARENT, height);
                container.setLayoutParams(layoutParams);

                container.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) container.getTag();
            }

            if (salesType.isPromote()) {
                ShopsTripleProductLine shopsTripleProductLine = viewHolder.shopsTripleProductLine;
                product = salesType.getSalesTypeList().get(position * 3);
                ShopsProductWithPicture leftProduct = shopsTripleProductLine.getLeftProduct();
                if (leftProduct == null) {
                    leftProduct = generateProduct(product);
                    shopsTripleProductLine.addProduct(leftProduct);
                }
                leftProduct.setOnClickListener(listener);

                ShopsProductWithPicture middleProduct = shopsTripleProductLine.getMiddleProduct();
                if (position * ShopsTripleProductLine.MAX_NUM + 1 < salesType.getSalesTypeList().size()) {
                    product = salesType.getSalesTypeList().get(position * ShopsTripleProductLine.MAX_NUM + 1);
                    if (middleProduct == null) {
                        middleProduct = generateProduct(product);
                        shopsTripleProductLine.addProduct(middleProduct);
                    }
                    middleProduct.setOnClickListener(listener);
                } else {
                    if (middleProduct != null) {
                        middleProduct.setVisibility(View.INVISIBLE);
                    }
                }

                ShopsProductWithPicture rightProduct = shopsTripleProductLine.getRightProduct();
                if (position * ShopsTripleProductLine.MAX_NUM + 2 < salesType.getSalesTypeList().size()) {
                    product = salesType.getSalesTypeList().get(position * ShopsTripleProductLine.MAX_NUM + 2);
                    if (rightProduct == null) {
                        rightProduct = generateProduct(product);
                        shopsTripleProductLine.addProduct(rightProduct);
                    }
                    rightProduct.setOnClickListener(listener);
                } else {
                    if (rightProduct != null) {
                        rightProduct.setVisibility(View.INVISIBLE);
                    }
                }
            } else {
                product = salesType.getSalesTypeList().get(position);
                ShopsSingleProductLine singleProductLine = viewHolder.singleProductLine;
                singleProductLine.setProduct(product);

                if (selectedProductIdList != null) {
                    if (mode == ShopsChooseProductFragment.ChooseProductMode.ModePackage) {
                        String id = product.getId();
                        if (id.equals(greenId)) {
                            id = String.valueOf(greenId) + Constants.STR_GREEN_ID_SEPARATOR + product.getAttrId();
                        }
                        if (selectedProductIdList.contains(id)) {
                            singleProductLine.setOnClickListener(null);
                            singleProductLine.setBackgroundColor(getColor(R.color.common_deep_gray));
                        } else {
                            singleProductLine.setOnClickListener(listener);
                            singleProductLine.setBackgroundColor(getColor(R.color.common_white));
                        }
                    } else if (mode == ShopsChooseProductFragment.ChooseProductMode.ModePricingTable) {
                     if (selectedProductIdList.contains(product.getId()) && product.getAttrCount() < 2 && !product.getId().equals(greenId)) {

                            singleProductLine.setOnClickListener(null);
                            singleProductLine.setBackgroundColor(getColor(R.color.common_deep_gray));
                        } else {
                            singleProductLine.setOnClickListener(listener);
                        }


                        if (greenId.equals(product.getId())){

                            if (selectedProductIdList.contains(String.valueOf(greenId) + Constants.STR_GREEN_ID_SEPARATOR + product.getAttrId())) {
                                singleProductLine.setOnClickListener(null);
                                singleProductLine.setBackgroundColor(getColor(R.color.common_deep_gray));

                            }
                        }
                    }
                } else {
                    singleProductLine.setOnClickListener(listener);
                }
            }


            return container;
        }

        private ShopsProductWithPicture generateProduct(JsonChooseProduct.SalesType.SalesTypeProduct product) {
            ShopsProductWithPicture shopsProductWithPicture = new ShopsProductWithPicture(mBaseFragment);
            shopsProductWithPicture.setProduct(product);
            return shopsProductWithPicture;
        }

        public void setListener(View.OnClickListener listener) {
            this.listener = listener;
        }
    }


    class ViewHolder {
        ShopsSingleProductLine singleProductLine;
        ShopsTripleProductLine shopsTripleProductLine;
    }
}
