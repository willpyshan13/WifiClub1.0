/**
 * Project Name: itee
 * File Name:	 ShopGoodsListItemFragment.java
 * Package Name: cn.situne.itee.fragment.shops
 * Date:		 2015-03-05
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.shopping;

import android.view.Gravity;
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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.ShoppingProduct;
import cn.situne.itee.entity.ShopsPackageInfo;
import cn.situne.itee.entity.ShopsProduct;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonChooseProduct;
import cn.situne.itee.manager.jsonentity.JsonPackageGet;
import cn.situne.itee.manager.jsonentity.JsonProduct;
import cn.situne.itee.manager.jsonentity.JsonPromote;
import cn.situne.itee.manager.jsonentity.JsonShopsPropertyPriceOrQtyGet;
import cn.situne.itee.view.ShopsPackageProductLine;
import cn.situne.itee.view.ShopsProductWithPicture;
import cn.situne.itee.view.ShopsSingleProductLine;
import cn.situne.itee.view.ShopsTripleProductLine;
import cn.situne.itee.view.popwindow.SelectAttributePopupWindow;

/**
 * ClassName:ShopGoodsListItemFragment <br/>
 * Function: 对应于商店里的Tab页。如，租赁、餐厅等是根据配置变化的 ysc. <br/>
 * Date: 2015-03-05 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class ShoppingGoodsListItemFragment extends BaseFragment {
    private String greenId;
    private String typeId;
    private String shopName;
    private int currentPage;
    private int nowPopWinIndex = 0;

    private JsonChooseProduct.SalesType salesType;
    private ShoppingGoodsListFragment containerFragment;
    private PullToRefreshListView refreshListViewGoods;
    private ShopChooseProductAdapter shopChooseProductAdapter;

    private SelectAttributePopupWindow nowPopUpWin;
    private JsonChooseProduct.SalesType.SalesTypeProduct product;
    private JsonChooseProduct.SalesType.SalesTypeProduct.SalesTypeSubProduct subProduct;

    private RelativeLayout rlHeader;

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
        if (salesType != null) {
            refreshListViewGoods = (PullToRefreshListView) rootView.findViewById(R.id.sticky_content);
            rlHeader = (RelativeLayout) rootView.findViewById(R.id.sticky_header);
            shopChooseProductAdapter = new ShopChooseProductAdapter(this, salesType);
        }
        initData();
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        rlHeader.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putString(TransKey.COMMON_GREEN_ID, greenId);
//                push(ShoppingSearchFragment.class, bundle);
            }
        });
    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {
        refreshListViewGoods.getRefreshableView().setDivider(null);
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

    public void setContainerFragment(ShoppingGoodsListFragment containerFragment) {
        this.containerFragment = containerFragment;
    }

    private boolean checkQty(JsonChooseProduct.SalesType.SalesTypeProduct salesTypeProduct, int qty) {
        boolean res = true;
        if (!greenId.equals(salesTypeProduct.getId())) {
            String mapId = salesType.getSalesTypeId() + Constants.STR_COMMA + salesTypeProduct.getId() + Constants.STR_COMMA;
            if (Utils.isStringNotNullOrEmpty(product.getAttrId())) {
                mapId += product.getAttrId();
            }

            LinkedHashMap<String, ShoppingProduct> shoppingCartMap = AppUtils.getShoppingCart(getActivity());
            ShoppingProduct shoppingProduct = shoppingCartMap.get(mapId);
            if (shoppingProduct != null && (shoppingProduct.getProductNumber() >= qty)&& Constants.STR_0.equals(salesTypeProduct.getUnlimitedFlag())) {
                res = false;
                String msg = AppUtils.generateNoMoreProductMessage(this, salesTypeProduct.getName());
                Utils.showShortToast(getActivity(), msg);
            }
        }
        return res;
    }

    private void resetView(String productId, JsonChooseProduct.SalesType.SalesTypeProduct product) {
        LinkedHashMap<String, ShoppingProduct> shoppingCartMap = AppUtils.getShoppingCart(getActivity());
        if (!greenId.equals(productId)) {
            String mapId = salesType.getSalesTypeId() + Constants.STR_COMMA + productId + Constants.STR_COMMA;
            if (Utils.isStringNotNullOrEmpty(product.getAttrId())) {
                mapId += product.getAttrId();
            }
            if (shoppingCartMap.containsKey(mapId)) {
                ShoppingProduct shoppingProduct = shoppingCartMap.get(mapId);
                shoppingProduct.setProductNumber(shoppingProduct.getProductNumber() + 1);
                shoppingCartMap.put(mapId, shoppingProduct);
            } else {
                ShoppingProduct shopsProduct = getShoppingProduct(product);
                shopsProduct.setProductNumber(1);
                shoppingCartMap.put(mapId, shopsProduct);
            }
        } else {
            ShoppingProduct shopsProduct = getShoppingProduct(product);
            shopsProduct.setProductId(String.valueOf(greenId) + Constants.STR_GREEN_ID_SEPARATOR + Utils.getCurrentTimeStamp());
            shopsProduct.setProductNumber(1);
            shoppingCartMap.put(shopsProduct.getProductId(), shopsProduct);
        }
        AppUtils.saveShoppingCart(shoppingCartMap, getActivity());
        containerFragment.changeState();
    }

    private void initData() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof ShopsProductWithPicture) {
                    ShopsProductWithPicture shopsProductWithPicture = (ShopsProductWithPicture) v;
                    product = shopsProductWithPicture.getProduct();
                } else if (v instanceof ShopsPackageProductLine) {
                    ShopsPackageProductLine shopsPackageProductLine = (ShopsPackageProductLine) v;
                    product = salesType.getSalesTypeList().get(shopsPackageProductLine.getCurrentPosition());
                } else {
                    ShopsSingleProductLine singleProductLine = (ShopsSingleProductLine) v;
                    product = singleProductLine.getProduct();
                }
                final String productId = product.getId();
                if (Utils.isStringNotNullOrEmpty(productId)) {
                    chooseProductAttribute(productId, product.getPrice());
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

    public ShoppingProduct getShoppingProduct(JsonChooseProduct.SalesType.SalesTypeProduct product) {
        ShoppingProduct shoppingProduct = new ShoppingProduct();
        shoppingProduct.setProductAttribute(product.getAttrId());
        if (salesType.isPackage()) {
            shoppingProduct.setPackageId(product.getId());
        } else if (salesType.isPromote()) {
            shoppingProduct.setPromoteId(product.getId());
            if (Utils.isListNotNullOrEmpty(product.getSubProductList())) {
                shoppingProduct.setProductId(product.getSubProductList().get(0).getProductId());
                if (greenId.equals(product.getSubProductList().get(0).getProductId())) {
                    shoppingProduct.setProductAttribute(product.getSubProductList().get(0).getProductAttrId());
                }
            }
        } else {
            shoppingProduct.setProductId(product.getId());
        }
        shoppingProduct.setIsCaddie(product.isCaddie());
        shoppingProduct.setProductName(product.getName());
        shoppingProduct.setProductPrice(product.getPrice());
        if (Utils.isListNotNullOrEmpty(product.getSubProductList())) {
            for (JsonChooseProduct.SalesType.SalesTypeProduct.SalesTypeSubProduct salesTypeSubProduct : product.getSubProductList()) {
                ShoppingProduct.ShoppingSubProduct subProduct = new ShoppingProduct.ShoppingSubProduct();
                subProduct.setId(salesTypeSubProduct.getId());
                subProduct.setProductId(salesTypeSubProduct.getProductId());
                subProduct.setProductName(salesTypeSubProduct.getProductName());
                if (Utils.isStringNotNullOrEmpty(salesTypeSubProduct.getProductAttrId())) {
                    subProduct.setProductAttrId(salesTypeSubProduct.getProductAttrId());
                }
                subProduct.setProductAttr(salesTypeSubProduct.getProductAttr());
                subProduct.setNumber(salesTypeSubProduct.getNumber());
                subProduct.setPrice(salesTypeSubProduct.getPrice());
                subProduct.setPromotePrice(salesTypeSubProduct.getPromotePrice());
                subProduct.setType(salesTypeSubProduct.getType());
                shoppingProduct.getProductList().add(subProduct);
            }
        }
        return shoppingProduct;
    }

    private void circulateShow() {
        if (nowPopWinIndex < product.getSubProductList().size()) {
            subProduct = product.getSubProductList().get(nowPopWinIndex);
            if (subProduct.getAttriStatus() == 1) {
                OnPropertyClickListener onPropertyClickListener = new OnPropertyClickListener() {
                    @Override
                    public void OnPropertyClick(int position, JsonShopsPropertyPriceOrQtyGet.DataItem dataItem,
                                                String name) {
                        if (position == 0) {
                            subProduct.setProductAttrId(String.valueOf(dataItem.getPraId()));
                            nowPopUpWin.dismiss();

                            if (nowPopWinIndex < product.getSubProductList().size()) {
                                if (nowPopWinIndex == product.getSubProductList().size() - 1) {
                                    nowPopWinIndex = 0;
                                    resetView(product.getId(), product);
                                } else {
                                    nowPopWinIndex++;
                                    circulateShow();
                                }
                            }
                        } else {
                            nowPopWinIndex = 0;
                        }
                    }
                };
                String attrId = StringUtils.EMPTY;
                netLinkSubClass(subProduct.getProductId(), attrId, subProduct.getProductName(), subProduct.getPrice(),
                        Constants.SHOP_TYPE_PACKAGE, false, onPropertyClickListener);
            } else {
                nowPopWinIndex++;
                circulateShow();
            }
        } else {
            resetView(product.getId(), product);
            nowPopWinIndex = 0;
        }
    }

    private void chooseProductAttribute(final String productId, final String promotePrice) {
        final JsonChooseProduct.SalesType.SalesTypeProduct salesTypeProduct
                = new JsonChooseProduct.SalesType.SalesTypeProduct(product);
        String qtyString = product.getQty();
        if (Utils.isStringNotNullOrEmpty(qtyString)) {
            int qty = Integer.valueOf(qtyString);
            if (!checkQty(product, qty)) {
                return;
            }
        }
        //add by syb.
        switch (typeId) {
            case Constants.SHOP_TYPE_RENTAL:
                if (product.getAttriStatus() == 1) {
                    OnPropertyClickListener onPropertyClickListener = new OnPropertyClickListener() {
                        @Override
                        public void OnPropertyClick(int position, JsonShopsPropertyPriceOrQtyGet.DataItem dataItem,String name) {
                            if (position == 0) {
                                if (salesTypeProduct.getPropertyPriceStatus() == 1) {
                                    salesTypeProduct.setPrice(String.valueOf(dataItem.getPraPrice()));
                                }
                                salesTypeProduct.setName(salesTypeProduct.getName()
                                        + Constants.STR_BRACKETS_START + name + Constants.STR_BRACKETS_END);
                                salesTypeProduct.setAttrId(String.valueOf(dataItem.getPraId()));
                                salesTypeProduct.setPrice(String.valueOf(dataItem.getPraPrice()));
                                if (salesTypeProduct.isCaddie()) {
                                    salesTypeProduct.setPrice(product.getPrice());
                                }
                                resetView(salesTypeProduct.getId(), salesTypeProduct);
                                nowPopUpWin.dismiss();
                            }
                        }
                    };

                    String attrId = null;
                    if (product.isCaddie()) {
                        attrId = salesTypeProduct.getAttrId();
                    }
                    netLinkSubClass(productId, attrId, product.getName(), promotePrice, typeId, product.isCaddie(),
                            onPropertyClickListener);
                } else {
                    resetView(salesTypeProduct.getId(), salesTypeProduct);
                }
                break;
            case Constants.SHOP_TYPE_PACKAGE:
                circulateShow();
                break;
            case Constants.SHOP_TYPE_PROMOTE:
                if (salesTypeProduct.getAttriStatus() == 1) {
                    OnPropertyClickListener onPropertyClickListener = new OnPropertyClickListener() {
                        @Override
                        public void OnPropertyClick(int position, JsonShopsPropertyPriceOrQtyGet.DataItem dataItem,
                                                    String name) {
                            if (position == 0) {
                                if (salesTypeProduct.getPropertyPriceStatus() == 1) {
                                    salesTypeProduct.setPrice(String.valueOf(dataItem.getPraPrice()));
                                }
                                salesTypeProduct.setAttrId(String.valueOf(dataItem.getPraId()));
                                salesTypeProduct.setPrice(String.valueOf(dataItem.getPraPrice()));
                                salesTypeProduct.setName(salesTypeProduct.getName() + Constants.STR_BRACKETS_START
                                        + name + Constants.STR_BRACKETS_END);
                                resetView(salesTypeProduct.getId(), salesTypeProduct);
                                nowPopUpWin.dismiss();
                            }
                        }
                    };
                    if (salesTypeProduct.getSubProductList() != null && salesTypeProduct.getSubProductList()
                            .size() > 0) {
                        String productIdTemp = salesTypeProduct.getSubProductList().get(0).getProductId();
                        netLinkPromoteSubClass(productIdTemp, productId, salesTypeProduct.getName(), promotePrice,
                                onPropertyClickListener);
                    }
                } else {
                    resetView(salesTypeProduct.getId(), salesTypeProduct);
                }
                break;
            default:
                if (salesTypeProduct.getAttriStatus() == 1) {
                    OnPropertyClickListener onPropertyClickListener = new OnPropertyClickListener() {
                        @Override
                        public void OnPropertyClick(int position, JsonShopsPropertyPriceOrQtyGet.DataItem dataItem,
                                                    String name) {
                            if (position == 0) {
                                if (salesTypeProduct.getPropertyPriceStatus() == 1) {
                                    salesTypeProduct.setPrice(String.valueOf(dataItem.getPraPrice()));
                                }
                                salesTypeProduct.setAttrId(String.valueOf(dataItem.getPraId()));
                                salesTypeProduct.setPrice(String.valueOf(dataItem.getPraPrice()));
                                salesTypeProduct.setName(salesTypeProduct.getName() + Constants.STR_BRACKETS_START
                                        + name + Constants.STR_BRACKETS_END);
                                resetView(salesTypeProduct.getId(), salesTypeProduct);
                                nowPopUpWin.dismiss();
                            }
                        }
                    };
                    netLinkSubClass(salesTypeProduct.getId(), salesTypeProduct.getAttrId(),
                            salesTypeProduct.getName(), promotePrice, typeId, false, onPropertyClickListener);
                } else {
                    resetView(salesTypeProduct.getId(), salesTypeProduct);
                }
                break;
        }
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

        HttpManager<JsonPromote> hh = new HttpManager<JsonPromote>(ShoppingGoodsListItemFragment.this) {
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
                    } else {
                        RelativeLayout rl = new RelativeLayout(getActivity());
                        ImageView ivNoData = new ImageView(getActivity());
                        ivNoData.setImageResource(R.drawable.bg_shop_no_data);
                        refreshListViewGoods.setEmptyView(rl);
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, LAYOUT_TRUE);
                        ivNoData.setLayoutParams(layoutParams);
                        rl.addView(ivNoData);
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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.ShopsPromoteGet, params, false);
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

        HttpManager<JsonPackageGet> hh = new HttpManager<JsonPackageGet>(ShoppingGoodsListItemFragment.this) {
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
                    shopChooseProductAdapter.setSalesType(salesType);
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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.ShopsPackageGet, params, false);
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

        HttpManager<JsonProduct> hh = new HttpManager<JsonProduct>(ShoppingGoodsListItemFragment.this) {
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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.ShopsProductGet, params, false);
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
            product.setPrice(promote.getPrice());
            product.setAttriStatus(promote.getAttrStatus());
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

            for (JsonPackageGet.DataItem dataItem : packageData.getProductList()) {
                JsonChooseProduct.SalesType.SalesTypeProduct.SalesTypeSubProduct subProduct = new JsonChooseProduct.SalesType.SalesTypeProduct.SalesTypeSubProduct();
                subProduct.setId(dataItem.getId());
                subProduct.setProductId(dataItem.getProductId());
                subProduct.setProductName(dataItem.getPdName());
                subProduct.setProductAttr(dataItem.getProductAttr());
                subProduct.setPrice(String.valueOf(dataItem.getPrice()));
                subProduct.setNumber(String.valueOf(dataItem.getNumber()));
                subProduct.setType(String.valueOf(dataItem.getType()));
                subProduct.setAttriStatus(dataItem.getAttriStatus());
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
            if (productData.getEnableProperty() == 1 && productData.getAttrCount() > 0) {
                product.setAttriStatus(1);
            } else {
                product.setAttriStatus(0);
            }
            salesType.getSalesTypeList().add(product);
        }
    }

    private void netLinkSubClass(final String productID,
                                 final String attrId,
                                 final String productName,
                                 final String promotePrice,
                                 final String typeId,
                                 final boolean isCaddie,
                                 final OnPropertyClickListener onPropertyClickListener) {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_PRODUCT_ID, productID);
        if (Utils.isStringNotNullOrEmpty(attrId)) {
            params.put(ApiKey.SHOPS_LEVEL_ID, attrId);
        }

        HttpManager<JsonShopsPropertyPriceOrQtyGet> hh = new HttpManager<JsonShopsPropertyPriceOrQtyGet>(this) {
            @Override
            public void onJsonSuccess(JsonShopsPropertyPriceOrQtyGet jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    if (Constants.SHOP_TYPE_RENTAL.equals(typeId)) {
                        if (isCaddie) {
                            nowPopUpWin = new SelectAttributePopupWindow(getActivity(), productID, productName, promotePrice,
                                    Constants.STR_0, Constants.STR_EMPTY, onPropertyClickListener, attrId);
                        } else {
                            nowPopUpWin = new SelectAttributePopupWindow(getActivity(), productID, productName, promotePrice,
                                    Constants.STR_0, Constants.STR_EMPTY, onPropertyClickListener);
                        }
                    } else if (Constants.SHOP_TYPE_PACKAGE.equals(typeId)) {
                        nowPopUpWin = new SelectAttributePopupWindow(getActivity(), productID, productName, promotePrice,
                                Constants.STR_2, Constants.STR_EMPTY, onPropertyClickListener);
                    } else if (Constants.SHOP_TYPE_PROMOTE.equals(typeId)) {
                        nowPopUpWin = null;
                    } else {
                        nowPopUpWin = new SelectAttributePopupWindow(getActivity(), productID,
                                productName, promotePrice, Constants.STR_2, null, onPropertyClickListener
                                , jo.getDataList());
                    }
                    if (nowPopUpWin != null) {
                        nowPopUpWin.showAtLocation(getRootView().findViewById(R.id.rl_content_container),
                                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    }
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
        hh.startGet(getActivity(), ApiManager.HttpApi.ShopsPriceSubclassGet, params);
    }

    private void netLinkPromoteSubClass(final String productId,
                                        final String argumentPromoteId,
                                        final String productName,
                                        final String promotePrice,
                                        final OnPropertyClickListener onPropertyClickListener) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_PRODUCT_ID, productId);
        params.put(ApiKey.SHOPS_PROMOTE_PROMOTE_ID, argumentPromoteId);
        HttpManager<JsonShopsPropertyPriceOrQtyGet> hh = new HttpManager<JsonShopsPropertyPriceOrQtyGet>(this) {
            @Override
            public void onJsonSuccess(JsonShopsPropertyPriceOrQtyGet jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    nowPopUpWin = new SelectAttributePopupWindow(getActivity(), productId,
                            productName, promotePrice, Constants.STR_1, argumentPromoteId,
                            onPropertyClickListener, jo.getDataList());
                    nowPopUpWin.showAtLocation(ShoppingGoodsListItemFragment.this.getRootView()
                            .findViewById(R.id.rl_content_container), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
        hh.startGet(getActivity(), ApiManager.HttpApi.PromoteProductSubclassGet, params);
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

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public JsonChooseProduct.SalesType getSalesType() {
        return salesType;
    }

    public void setSalesType(JsonChooseProduct.SalesType salesType) {
        this.salesType = salesType;
    }

    /**
     * 单击事件监听器
     */
    public interface OnPropertyClickListener {
        void OnPropertyClick(int position, JsonShopsPropertyPriceOrQtyGet.DataItem dataItem, String name);
    }

    class ShopChooseProductAdapter extends BaseAdapter {
        private BaseFragment mBaseFragment;
        private JsonChooseProduct.SalesType salesType;
        private View.OnClickListener listener;

        public ShopChooseProductAdapter(BaseFragment mBaseFragment, JsonChooseProduct.SalesType salesType) {
            this.mBaseFragment = mBaseFragment;
            this.salesType = salesType;
        }

        public void setSalesType(JsonChooseProduct.SalesType salesType) {
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
                } else if (salesType.isPackage()) {
                    height = getActualHeightOnThisDevice(100);
                    viewHolder.shopsPackageProductLine = new ShopsPackageProductLine(mBaseFragment);
                    viewHolder.shopsPackageProductLine.setOnClickListener(listener);
                    viewHolder.shopsPackageProductLine.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
                    container.addView(viewHolder.shopsPackageProductLine);
                } else {
                    height = getActualHeightOnThisDevice(100);
                    viewHolder.singleProductLine = new ShopsSingleProductLine(mBaseFragment);
                    viewHolder.singleProductLine.setOnClickListener(listener);
                    viewHolder.singleProductLine.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
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
                    } else {
                        middleProduct.setVisibility(View.VISIBLE);
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
                    } else {
                        rightProduct.setVisibility(View.VISIBLE);
                    }
                    rightProduct.setOnClickListener(listener);
                } else {
                    if (rightProduct != null) {
                        rightProduct.setVisibility(View.INVISIBLE);
                    }
                }
            } else if (salesType.isPackage()) {
                product = salesType.getSalesTypeList().get(position);
                ShopsPackageInfo shopsPackageInfo = new ShopsPackageInfo();
                shopsPackageInfo.setPackageId(product.getId());
                shopsPackageInfo.setPackageName(product.getName());
                shopsPackageInfo.setPackagePrice(product.getPrice());
                shopsPackageInfo.setPackageCount(product.getQty());
                shopsPackageInfo.setUnlimitedFlag(product.getUnlimitedFlag());

                for (int i = 0; i < product.getSubProductList().size(); i++) {
                    JsonChooseProduct.SalesType.SalesTypeProduct.SalesTypeSubProduct salesTypeSubProduct
                            = product.getSubProductList().get(i);
                    ShopsProduct shopsProduct = new ShopsProduct();
                    shopsProduct.setProductId(salesTypeSubProduct.getProductId());
                    if (greenId.equals(salesTypeSubProduct.getProductId())
                            && Utils.isStringNotNullOrEmpty(salesTypeSubProduct.getProductAttr())) {
                        shopsProduct.setProductName(salesTypeSubProduct.getProductName()
                                + Constants.STR_BRACKETS_START + salesTypeSubProduct.getProductAttr() + Constants.STR_BRACKETS_END);
                    } else {
                        shopsProduct.setProductName(salesTypeSubProduct.getProductName());
                    }
                    shopsProduct.setProductPrice(salesTypeSubProduct.getPrice());
                    shopsProduct.setProductNumber(Integer.valueOf(salesTypeSubProduct.getNumber()));
                    shopsPackageInfo.addProductDetail(shopsProduct);
                }
                viewHolder.shopsPackageProductLine.setPackageInfo(shopsPackageInfo);
                viewHolder.shopsPackageProductLine.setCurrentPosition(position);
                int height = getActualHeightOnThisDevice(80 * (product.getSubProductList().size() + 1));
                ListView.LayoutParams layoutParams = new ListView.LayoutParams(MATCH_PARENT, height);
                container.setLayoutParams(layoutParams);
            } else {
                product = salesType.getSalesTypeList().get(position);
                ShopsSingleProductLine singleProductLine = viewHolder.singleProductLine;
                singleProductLine.setProduct(product);
                container.removeAllViews();
                container.addView(singleProductLine);
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
        ShopsPackageProductLine shopsPackageProductLine;
    }
}