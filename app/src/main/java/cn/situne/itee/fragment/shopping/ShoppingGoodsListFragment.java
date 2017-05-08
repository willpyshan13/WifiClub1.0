/**
 * Project Name: itee
 * File Name:	 ShopGoodsListFragment.java
 * Package Name: cn.situne.itee.fragment.shops
 * Date:		 2015-03-04
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import cn.situne.itee.activity.ProductScanQrCodeActivity;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.ShoppingProduct;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.shops.ShopsListFragment;
import cn.situne.itee.fragment.teetime.LocationListFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonChooseProduct;
import cn.situne.itee.manager.jsonentity.JsonProduct;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:ShopGoodsListFragment <br/>
 * Function: goods list of shop. <br/>
 * Date: 2015-03-04 <br/>
 * UI:06-1
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
@SuppressWarnings("unchecked")
public class ShoppingGoodsListFragment extends BaseFragment {   //这个才是默认会进入的能卖东西的商店啊~我肏，你们起的都是什么类名啊！！
    // shop names
    private TabPageIndicator tabPageIndicator;
    private RelativeLayout rlShoppingCartContainer;
    private RelativeLayout rlSearchHeader;

    private IteeTextView tvTotal;
    private IteeTextView tvCurrency;
    private IteeTextView tvTotalCost;

    private RelativeLayout rlShoppingCart;
    private ImageView ivShoppingCart;
    private IteeTextView tvNumberOfGoods;

    private ViewPager vpShops;
    private ShopsChooseProductPagerAdapter adapterShopGoods;
    // fragment container
    private ArrayList<ShoppingGoodsListItemFragment> fragments;
    private ImageView ivSetting;
    private View.OnClickListener listenerSetting;

    private String fromPage;
    private String greenId;

    private String bookingNo;
    private String playerName;

    private ArrayList<String> normalProductShopIds = new ArrayList<>();

    private RelativeLayout rlContainer;
    private View waitingView;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_shopping_product_list;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.common_edit;
    }

    @Override
    protected void initControls(View rootView) {
        fragments = new ArrayList<>();

        rlSearchHeader = (RelativeLayout) rootView.findViewById(R.id.sticky_header);
        rlContainer = (RelativeLayout) rootView.findViewById(R.id.rl_content_container);
        tabPageIndicator = (TabPageIndicator) rootView.findViewById(R.id.tab_page_indicator);
        vpShops = (ViewPager) rootView.findViewById(R.id.vp_goods_list_pager);
        rlShoppingCartContainer = (RelativeLayout) rootView.findViewById(R.id.rl_shopping_cart_container);

        tvTotal = new IteeTextView(this);
        tvCurrency = new IteeTextView(this);
        tvTotalCost = new IteeTextView(this);

        rlShoppingCart = new RelativeLayout(getActivity());
        ivShoppingCart = new ImageView(getActivity());
        tvNumberOfGoods = new IteeTextView(this);

        adapterShopGoods = new ShopsChooseProductPagerAdapter(getChildFragmentManager(), fragments);
        vpShops.setAdapter(adapterShopGoods);
        tabPageIndicator.setViewPager(vpShops);

        Bundle bundle = getArguments();
        if (bundle != null) {
            fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE);
            bookingNo = bundle.getString(TransKey.SHOPPING_BOOKING_NO, bookingNo);
            playerName = bundle.getString(TransKey.SHOPPING_PLAYER_NAME, playerName);
        }
    }

    @Override
    protected void setDefaultValueOfControls() {
        tvTotalCost.setText(Constants.STR_0);
        tvNumberOfGoods.setVisibility(View.INVISIBLE);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void setListenersOfControls() {
        listenerSetting = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(TransKey.COMMON_FROM_PAGE, ShoppingGoodsListFragment.class.getName());
                push(ShopsListFragment.class, bundle);
            }
        };

        AppUtils.NoDoubleClickListener listenerShoppingCart = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                jump2ConfirmPay();
            }
        };
        rlShoppingCart.setOnClickListener(listenerShoppingCart);

        rlSearchHeader.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(TransKey.COMMON_GREEN_ID, greenId);
                bundle.putString(TransKey.COMMON_FROM_PAGE, ShoppingGoodsListFragment.class.getName());
                bundle.putString(TransKey.COMMON_UP_FROM_PAGE, fromPage);
                bundle.putString(TransKey.SHOPPING_BOOKING_NO, bookingNo);
                bundle.putString(TransKey.SHOPPING_PLAYER_NAME, playerName);
                push(ShoppingSearchFragment.class, bundle);
            }
        });
    }

    @Override
    protected void reShowWithBackValue() {
        super.reShowWithBackValue();
        Bundle bundle = getReturnValues();
        bundle.putString(TransKey.SHOPPING_BOOKING_NO, bookingNo);
        bundle.putString(TransKey.SHOPPING_PLAYER_NAME, playerName);
        String toName = bundle.getString("toName", Constants.STR_EMPTY);
        try {
            if (ShoppingPaymentFragment.class.getName().equals(toName)) {
                if (fromPage == null) {
                    fromPage = "";
                }
                doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
            } else {
                if (fromPage != null) {
                    if (fromPage.equals(LocationListFragment.class.getName())) {
                        push(ShoppingPaymentFragment.class, bundle);
                    } else {
                        doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setLayoutOfControls() {
        tabPageIndicator.setTabWidth(getScreenWidth() / 4);
        LayoutUtils.setLayoutHeight(rlSearchHeader, 100, mContext);

        RelativeLayout.LayoutParams tabPageIndicatorLayoutParams = (RelativeLayout.LayoutParams) tabPageIndicator.getLayoutParams();
        tabPageIndicatorLayoutParams.height = getActualHeightOnThisDevice(80);
        tabPageIndicator.setLayoutParams(tabPageIndicatorLayoutParams);

        RelativeLayout.LayoutParams rlShoppingCartContainerLayoutParams = (RelativeLayout.LayoutParams) rlShoppingCartContainer.getLayoutParams();
        rlShoppingCartContainerLayoutParams.height = getActualHeightOnThisDevice(120);
        rlShoppingCartContainer.setLayoutParams(rlShoppingCartContainerLayoutParams);

        RelativeLayout.LayoutParams tvTotalLayoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        tvTotalLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvTotalLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        tvTotalLayoutParams.leftMargin = getActualWidthOnThisDevice(80);

        tvTotal.setLayoutParams(tvTotalLayoutParams);
        tvTotal.setId(View.generateViewId());

        rlShoppingCartContainer.addView(tvTotal);

        RelativeLayout.LayoutParams tvCurrencyLayoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        tvCurrencyLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvCurrencyLayoutParams.addRule(RelativeLayout.RIGHT_OF, tvTotal.getId());

        tvCurrency.setLayoutParams(tvCurrencyLayoutParams);
        tvCurrency.setId(View.generateViewId());

        rlShoppingCartContainer.addView(tvCurrency);

        RelativeLayout.LayoutParams tvTotalCostLayoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        tvTotalCostLayoutParams.leftMargin = getActualWidthOnThisDevice(5);
        tvTotalCostLayoutParams.addRule(RelativeLayout.RIGHT_OF, tvCurrency.getId());
        tvTotalCostLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);

        tvTotalCost.setLayoutParams(tvTotalCostLayoutParams);
        tvTotalCost.setId(View.generateViewId());

        rlShoppingCartContainer.addView(tvTotalCost);

        RelativeLayout.LayoutParams rlShoppingCartLayoutParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(120), getActualHeightOnThisDevice(80));
        rlShoppingCartLayoutParams.rightMargin = getActualWidthOnThisDevice(20);
        rlShoppingCartLayoutParams.bottomMargin = getActualWidthOnThisDevice(20);
        rlShoppingCartLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        rlShoppingCartLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, LAYOUT_TRUE);
        rlShoppingCart.setLayoutParams(rlShoppingCartLayoutParams);

        rlContainer.addView(rlShoppingCart);
        rlShoppingCart.setBackgroundResource(R.drawable.bg_green_btn);

        RelativeLayout.LayoutParams ivShoppingCartLayoutParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(65), getActualHeightOnThisDevice(65));
        ivShoppingCartLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        ivShoppingCartLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivShoppingCartLayoutParams.leftMargin = getActualWidthOnThisDevice(4);

        ivShoppingCart.setLayoutParams(ivShoppingCartLayoutParams);
        ivShoppingCart.setId(View.generateViewId());
        ivShoppingCart.setImageResource(R.drawable.icon_shopping_cart);

        rlShoppingCart.addView(ivShoppingCart);

        RelativeLayout.LayoutParams tvNumberOfGoodsLayoutParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(30), getActualHeightOnThisDevice(30));
        tvNumberOfGoodsLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        tvNumberOfGoodsLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, LAYOUT_TRUE);
        tvNumberOfGoodsLayoutParams.rightMargin = getActualWidthOnThisDevice(20);
        tvNumberOfGoodsLayoutParams.bottomMargin = getActualWidthOnThisDevice(15);

        tvNumberOfGoods.setLayoutParams(tvNumberOfGoodsLayoutParams);
        tvNumberOfGoods.setBackgroundResource(R.drawable.icon_number_bg);

        rlShoppingCart.addView(tvNumberOfGoods);

        AppUtils.addTopSeparatorLine(rlShoppingCartContainer, getActivity());

        rlShoppingCartContainer.setVisibility(View.GONE);
    }

    @Override
    protected void setPropertyOfControls() {
        tvTotal.setText(R.string.shopping_pay_total);
        tvTotal.setTextColor(getColor(R.color.common_red));
        tvTotal.setTextSize(Constants.FONT_SIZE_LARGER);

        tvCurrency.setText(AppUtils.getCurrentCurrency(getActivity()));
        tvCurrency.setTextColor(getColor(R.color.common_red));
        tvCurrency.setTextSize(Constants.FONT_SIZE_LARGER);

        tvTotalCost.setTextColor(getColor(R.color.common_red));
        tvTotalCost.setTextSize(Constants.FONT_SIZE_LARGER);

        tvNumberOfGoods.setTextColor(getColor(R.color.common_white));
        tvNumberOfGoods.setTextSize(Constants.FONT_SIZE_SMALLEST);
        tvNumberOfGoods.setGravity(Gravity.CENTER);
    }

    @Override
    protected void configActionBar() {
        if (Utils.isStringNullOrEmpty(fromPage)) {
            setNormalMenuActionBar();
        } else {
            setStackedActionBar();
        }
        getTvLeftTitle().setText(getString(R.string.shopping_shopping));
        getTvRight().setBackgroundResource(R.drawable.icon_action_bar_code);
        getTvRight().setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                LinkedHashMap<String, ShoppingProduct> shoppingCartMap = AppUtils.getShoppingCart(getActivity());
                Intent intent = new Intent();
                intent.setClass(getActivity(), ProductScanQrCodeActivity.class);
                intent.putExtra("shoppingCartMap", shoppingCartMap);
                intent.putExtra("shoppingGreenId", greenId);
                startActivityForResult(intent, TransKey.REQUEST_CODE_JUMP_TO_CONFIRM_PAY);
            }
        });

        RelativeLayout parent = (RelativeLayout) getTvRight().getParent();
        ivSetting = new ImageView(getActivity());
        ivSetting.setImageResource(R.drawable.icon_common_setting);
        ivSetting.setOnClickListener(listenerSetting);

        RelativeLayout.LayoutParams ivPackageLayoutParams = new RelativeLayout.LayoutParams(getActionBarHeight(), getActionBarHeight());
        ivPackageLayoutParams.addRule(RelativeLayout.LEFT_OF, getTvRight().getId());
        ivPackageLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        ivPackageLayoutParams.rightMargin = getActualWidthOnThisDevice(60);
        ivSetting.setLayoutParams(ivPackageLayoutParams);
        parent.addView(ivSetting);
    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        normalProductShopIds.clear();
        getProductData();
        changeState();
    }

    @Override
    protected void executeOnceOnCreate() {
        super.executeOnceOnCreate();
        AppUtils.removeShoppingCart(getActivity());
    }

    @Override
    public void onBeforeApi() {
        ivSetting.setEnabled(false);
    }

    @Override
    public void onAfterApi() {
        ivSetting.setEnabled(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        changeState();
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (requestCode == TransKey.REQUEST_CODE_JUMP_TO_CONFIRM_PAY) {
                String objString = bundle.getString("shoppingCartMap");
                LinkedHashMap<String, ShoppingProduct> returnShoppingCartMap = (LinkedHashMap<String, ShoppingProduct>) Utils.getObjectFromString(objString);
                boolean isJump2ConfirmPay = bundle.getBoolean(TransKey.JUMP_TO_CONFIRM_PAY);
                if (isJump2ConfirmPay) {
                    LinkedHashMap<String, ShoppingProduct> shoppingCartMap = AppUtils.getShoppingCart(getActivity());
                    shoppingCartMap.putAll(returnShoppingCartMap);
                    AppUtils.saveShoppingCart(shoppingCartMap, getActivity());
                    jump2ConfirmPay();
                }
            }
        }
    }

    private void setTotalPrice() {
        double totalPrice = 0;
        LinkedHashMap<String, ShoppingProduct> shoppingCartMap = AppUtils.getShoppingCart(getActivity());
        Utils.log(String.valueOf(shoppingCartMap.size()));
        for (String key : shoppingCartMap.keySet()) {
            ShoppingProduct shoppingProduct = shoppingCartMap.get(key);
            String priceString = Utils.isStringNotNullOrEmpty(shoppingProduct.getPromotePrice()) ? shoppingProduct.getPromotePrice() : shoppingProduct.getProductPrice();
            if (Utils.isStringNotNullOrEmpty(priceString) && !priceString.contains(Constants.STR_WAVE)) {
                double price = Double.valueOf(priceString);
                totalPrice += price * shoppingProduct.getProductNumber();
            }
        }
        tvTotalCost.setText(Utils.get2DigitDecimalString(String.valueOf(totalPrice)));
        if (shoppingCartMap.size() > 0) {
            rlShoppingCartContainer.setVisibility(View.VISIBLE);
        } else {
            rlShoppingCartContainer.setVisibility(View.GONE);
        }
    }

    /**
     * 请求商品信息，每次加载此页都会执行。从executeOnceOnCreate()方法。
     *
     */
    private void getProductData() {
        Utils.log("//////////////////////////");
        adapterShopGoods.notifyDataSetChanged();
        tabPageIndicator.notifyDataSetChanged();

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));

        HttpManager<JsonChooseProduct> hh = new HttpManager<JsonChooseProduct>(ShoppingGoodsListFragment.this) {
            @Override
            public void onJsonSuccess(JsonChooseProduct jo) {
                int returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    //这里从字面意思判断：如果没有商品、商店数量跳转到ShopsListFragment
                    if (jo.getDataStatus() == Constants.SHOPS_DATA_STATUS_0) {
                        Bundle bundle = new Bundle();
                        bundle.putString(TransKey.COMMON_FROM_PAGE, ShoppingGoodsListFragment.class.getName());
                        push(ShopsListFragment.class, bundle);
                    } else {
                        //如果请求到了数据就执行initData()
                        greenId = jo.getGreenId();
                        initData(jo);
                    }
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

    /**
     * 初始化数据基本上从这里开始，执行栈为executeOnceOnCreate()->getProductData()->initData()
     * @param jo getProductData()方法中网络请求回的数据
     */
    private void initData(JsonChooseProduct jo) {
        fragments.clear();
        for (JsonChooseProduct.SalesType salesType : jo.getSalesTypeList()) {
            ShoppingGoodsListItemFragment shoppingGoodsListItemFragment = new ShoppingGoodsListItemFragment();
            shoppingGoodsListItemFragment.setSalesType(salesType);
            shoppingGoodsListItemFragment.setGreenId(greenId);
            shoppingGoodsListItemFragment.setContainerFragment(this);
            shoppingGoodsListItemFragment.setTypeId(salesType.getSalesTypeId());
            if (salesType.isPromote()) {
                shoppingGoodsListItemFragment.setShopName(getString(R.string.shop_setting_promote));
                if (!Utils.isListNotNullOrEmpty(salesType.getSalesTypeList())) {
                    continue;
                }
            } else if (salesType.isRental()) {
                shoppingGoodsListItemFragment.setShopName(getString(R.string.shop_setting_rental));
            } else if (salesType.isPackage()) {
                shoppingGoodsListItemFragment.setShopName(getString(R.string.shop_setting_package));
            } else {
                shoppingGoodsListItemFragment.setShopName(salesType.getName());
                normalProductShopIds.add(salesType.getSalesTypeId());
            }
            fragments.add(shoppingGoodsListItemFragment);
        }

        adapterShopGoods = new ShopsChooseProductPagerAdapter(getChildFragmentManager(), fragments);
        vpShops.setAdapter(adapterShopGoods);
        tabPageIndicator.setViewPager(vpShops);

        adapterShopGoods.notifyDataSetChanged();
        tabPageIndicator.notifyDataSetChanged();

        getNormalProducts();
    }

    public void changeState() {
        setTotalPrice();
        LinkedHashMap<String, ShoppingProduct> shoppingCartMap = AppUtils.getShoppingCart(getActivity());
        if (shoppingCartMap.size() > 0) {
            tvNumberOfGoods.setVisibility(View.VISIBLE);
            int total = 0;
            for (String key : shoppingCartMap.keySet()) {
                ShoppingProduct shoppingProduct = shoppingCartMap.get(key);
                total += shoppingProduct.getProductNumber();
            }
            tvNumberOfGoods.setText(String.valueOf(total));
        } else {
            tvNumberOfGoods.setVisibility(View.INVISIBLE);
        }
    }

    private void getNormalProducts() {
        if (Utils.isListNotNullOrEmpty(normalProductShopIds)) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            waitingView = inflater.inflate(R.layout.view_common_waiting, null);
            rlContainer.addView(waitingView);

            IteeTextView tvItee = (IteeTextView) waitingView.findViewById(R.id.tv_itee);
            waitingView.setBackgroundColor(getColor(R.color.common_black));
            waitingView.setAlpha(0.4f);
            tvItee.setTextColor(getColor(R.color.common_white));

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) waitingView.getLayoutParams();
            layoutParams.width = MATCH_PARENT;
            layoutParams.height = MATCH_PARENT;
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            waitingView.setLayoutParams(layoutParams);

            for (String typeId : normalProductShopIds) {
                getProduct(typeId);
            }
        }
    }

    /**
     * 根据商品类型请求商品数据。我TMD也是服了你们这样的设计，你大爷的！！
     * @parms typeId  类型Id
     */
    private void getProduct(final String typeId) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_PRODUCT_TYPE_TYPE_ID, String.valueOf(typeId));
        params.put(ApiKey.COMMON_PAGE, Constants.STR_1);

        HttpManager<JsonProduct> hh = new HttpManager<JsonProduct>(ShoppingGoodsListFragment.this) {
            @Override
            public void onJsonSuccess(JsonProduct jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    configNormalData(jo.getProductList(), typeId);
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
                removeTypeId(typeId);
            }

            @Override
            public void onJsonError(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                removeTypeId(typeId);
            }
        };
        hh.startGet(this.getActivity(), ApiManager.HttpApi.ShopsProductGet, params, false);
    }

    private void removeTypeId(String typeId) {
        normalProductShopIds.remove(typeId);
        if (normalProductShopIds.size() == 0) {
            rlContainer.removeView(waitingView);
        }
    }

    private void configNormalData(ArrayList<JsonProduct.ProductData> productList, String typeId) {
        if (Utils.isStringNotNullOrEmpty(typeId)) {
            for (ShoppingGoodsListItemFragment shoppingGoodsListItemFragment : fragments) {
                if (typeId.equals(shoppingGoodsListItemFragment.getTypeId())) {
                    for (JsonProduct.ProductData productData : productList) {
                        JsonChooseProduct.SalesType.SalesTypeProduct product = new JsonChooseProduct.SalesType.SalesTypeProduct();
                        product.setId(String.valueOf(productData.getId()));
                        product.setName(productData.getName());
                        product.setQty(String.valueOf(productData.getQty()));
                        product.setCode(productData.getCode());
                        product.setPrice(String.valueOf(productData.getPrice()));
                        if (productData.getEnableProperty() == 1) {
                            product.setAttriStatus(1);
                        } else {
                            product.setAttriStatus(0);
                        }
                        shoppingGoodsListItemFragment.getSalesType().getSalesTypeList().add(product);
                    }
                }
            }
        }

        adapterShopGoods = new ShopsChooseProductPagerAdapter(getChildFragmentManager(), fragments);
        vpShops.setAdapter(adapterShopGoods);
        tabPageIndicator.setViewPager(vpShops);

        adapterShopGoods.notifyDataSetChanged();
        tabPageIndicator.notifyDataSetChanged();
    }

    private void jump2ConfirmPay() {
        LinkedHashMap<String, ShoppingProduct> shoppingCartMap = AppUtils.getShoppingCart(getActivity());
        if (shoppingCartMap.size() > 0) {
            ArrayList<String> packageProducts = new ArrayList<>();
            for (String key : shoppingCartMap.keySet()) {
                ShoppingProduct shopsProduct = shoppingCartMap.get(key);
                if (Utils.isStringNotNullOrEmpty(shopsProduct.getProductId()) && shopsProduct.getProductId().startsWith(String.valueOf(greenId) + Constants.STR_GREEN_ID_SEPARATOR)) {
                    shopsProduct.setProductId(greenId);
                }
                packageProducts.add(Utils.getStringFromObject(shopsProduct));
            }
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(TransKey.CHOOSE_PRODUCT_LIST, packageProducts);
            bundle.putString(TransKey.COMMON_FROM_PAGE, ShoppingGoodsListFragment.class.getName());

            if (Utils.isStringNullOrEmpty(fromPage)) {
                push(ShoppingPaymentFragment.class, bundle);
            } else if (!ShoppingPaymentFragment.class.getName().equals(fromPage)) {
                bundle.putString(TransKey.SHOPPING_BOOKING_NO, bookingNo);
                bundle.putString(TransKey.SHOPPING_PLAYER_NAME, playerName);
                bundle.putString(TransKey.COMMON_FROM_PAGE, ShoppingGoodsListFragment.class.getName());
                push(ShoppingPaymentFragment.class, bundle);
            } else {
                bundle.putString(TransKey.SHOPPING_BOOKING_NO, bookingNo);
                bundle.putString(TransKey.SHOPPING_PLAYER_NAME, playerName);
                try {
                    doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
                } catch (ClassNotFoundException e) {
                    Utils.log(e.getMessage());
                }
            }
        } else {
            Utils.showShortToast(getActivity(), R.string.msg_please_choose_product);
        }
    }

    class ShopsChooseProductPagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<ShoppingGoodsListItemFragment> fragments;

        public ShopsChooseProductPagerAdapter(FragmentManager fm, ArrayList<ShoppingGoodsListItemFragment> fragments) {
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
}