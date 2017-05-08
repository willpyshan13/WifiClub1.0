/**
 * Project Name: itee
 * File Name:	 ShoppingSearchFragment.java
 * Package Name: cn.situne.itee.fragment.shopping
 * Date:		 2015-08-10
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.shopping;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.activity.MainActivity;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.BaseViewHolder;
import cn.situne.itee.entity.ShoppingProduct;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonProductSearch;
import cn.situne.itee.manager.jsonentity.JsonShopsPropertyPriceOrQtyGet;
import cn.situne.itee.view.IteeSearchView;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.SelectAttributePopupWindow;

/**
 * ClassName:ShoppingSearchFragment <br/>
 * Function: Goods Search Page. <br/>
 * Date: 2015-08-10 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class ShoppingSearchFragment extends BaseFragment {

    public ArrayList<JsonProductSearch.SearchProduct> dataList;
    private IteeSearchView titleSearchView;
    private ListView searchDataList;
    private ListViewAdapter listViewAdapter;
    private IteeTextView footerMes;

    private IteeTextView tvTotalCost;
    private IteeTextView tvNumberOfGoods;

    private SelectAttributePopupWindow nowPopUpWin;

    private String greenId;

    private RelativeLayout rlShoppingCartContainer;

    private String bookingNo;

    private String fromPage;

    private String upFromPage;

    private String playerName;
    @Override
    protected int getFragmentId() {
        return R.layout.fragment_shopping_product_search;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            greenId = bundle.getString(TransKey.COMMON_GREEN_ID);
            bookingNo = bundle.getString(TransKey.SHOPPING_BOOKING_NO, Constants.STR_EMPTY);
            fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE);
            playerName = bundle.getString(TransKey.SHOPPING_PLAYER_NAME, playerName);

            upFromPage = bundle.getString(TransKey.COMMON_UP_FROM_PAGE);
        }
        dataList = new ArrayList<>();
        searchDataList = (ListView) rootView.findViewById(R.id.searchDataList);
        initShoppingCart(rootView);
    }

    @Override
    protected void setDefaultValueOfControls() {
        searchDataList.setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void setListenersOfControls() {
        searchDataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JsonProductSearch.SearchProduct searchProduct = dataList.get(position);
                doSelect(searchProduct);
            }
        });
    }

    @Override
    protected void setLayoutOfControls() {
        addFooterView();
        searchDataList.setDividerHeight(0);

        listViewAdapter = new ListViewAdapter();
        searchDataList.setAdapter(listViewAdapter);
    }

    @Override
    protected void setPropertyOfControls() {

    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        initTitleView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (footerMes != null) {
            footerMes.setText(Constants.STR_EMPTY);
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initShoppingCart(View rootView) {

        rlShoppingCartContainer = (RelativeLayout) rootView.findViewById(R.id.rl_shopping_cart_container);
        rlShoppingCartContainer.setBackgroundColor(getResources().getColor(R.color.common_white));

        rlShoppingCartContainer.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                LinkedHashMap<String, ShoppingProduct> shoppingCart = AppUtils.getShoppingCart(getActivity());
                if (shoppingCart.size() > 0) {
                    ArrayList<String> packageProducts = new ArrayList<>();
                    for (String key : shoppingCart.keySet()) {
                        ShoppingProduct shopsProduct = shoppingCart.get(key);
                        if (Utils.isStringNotNullOrEmpty(shopsProduct.getProductId())
                                && shopsProduct.getProductId().startsWith(
                                String.valueOf(greenId) + Constants.STR_GREEN_ID_SEPARATOR)) {
                            shopsProduct.setProductId(greenId);
                        }
                        packageProducts.add(Utils.getStringFromObject(shopsProduct));
                    }
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList(TransKey.CHOOSE_PRODUCT_LIST, packageProducts);
                    if (Utils.isStringNotNullOrEmpty(bookingNo)){
                        try {
                            doBackWithReturnValue(bundle,(Class<? extends BaseFragment>) Class.forName(fromPage));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                    }else{

                        push(ShoppingPaymentFragment.class, bundle);
                    }
                } else {
                    Utils.showShortToast(getActivity(), R.string.msg_please_choose_product);
                }
            }
        });

        IteeTextView tvTotal = new IteeTextView(this);
        IteeTextView tvCurrency = new IteeTextView(this);
        tvTotalCost = new IteeTextView(this);

        RelativeLayout rlShoppingCart = new RelativeLayout(mContext);
        ImageView ivShoppingCart = new ImageView(mContext);
        tvNumberOfGoods = new IteeTextView(this);

        LayoutUtils.setLayoutHeight(rlShoppingCartContainer, 120, mContext);

        RelativeLayout.LayoutParams tvTotalLayoutParams
                = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tvTotalLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        tvTotalLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        tvTotalLayoutParams.leftMargin = DensityUtil.getActualWidthOnThisDevice(80, mContext);

        tvTotal.setLayoutParams(tvTotalLayoutParams);
        tvTotal.setId(View.generateViewId());

        rlShoppingCartContainer.addView(tvTotal);

        RelativeLayout.LayoutParams tvCurrencyLayoutParams
                = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tvCurrencyLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        tvCurrencyLayoutParams.addRule(RelativeLayout.RIGHT_OF, tvTotal.getId());

        tvCurrency.setLayoutParams(tvCurrencyLayoutParams);
        tvCurrency.setId(View.generateViewId());

        rlShoppingCartContainer.addView(tvCurrency);

        RelativeLayout.LayoutParams tvTotalCostLayoutParams
                = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tvTotalCostLayoutParams.leftMargin = DensityUtil.getActualWidthOnThisDevice(5, mContext);
        tvTotalCostLayoutParams.addRule(RelativeLayout.RIGHT_OF, tvCurrency.getId());
        tvTotalCostLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

        tvTotalCost.setLayoutParams(tvTotalCostLayoutParams);
        tvTotalCost.setId(View.generateViewId());

        rlShoppingCartContainer.addView(tvTotalCost);

        int width = DensityUtil.getActualWidthOnThisDevice(120, mContext);
        int height = DensityUtil.getActualHeightOnThisDevice(80, mContext);
        RelativeLayout.LayoutParams rlShoppingCartLayoutParams
                = new RelativeLayout.LayoutParams(width, height);
        rlShoppingCartLayoutParams.rightMargin = DensityUtil.getActualWidthOnThisDevice(20, mContext);
        rlShoppingCartLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        rlShoppingCartLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

        rlShoppingCart.setLayoutParams(rlShoppingCartLayoutParams);

        rlShoppingCartContainer.addView(rlShoppingCart);
        rlShoppingCart.setBackgroundResource(R.drawable.bg_green_btn);

        int shoppingCartWidth = DensityUtil.getActualWidthOnThisDevice(65, mContext);
        int shoppingCartHeight = DensityUtil.getActualHeightOnThisDevice(65, mContext);
        RelativeLayout.LayoutParams ivShoppingCartLayoutParams
                = new RelativeLayout.LayoutParams(shoppingCartWidth, shoppingCartHeight);
        ivShoppingCartLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        ivShoppingCartLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
//        ivShoppingCartLayoutParams.leftMargin = DensityUtil.getActualWidthOnThisDevice(20, mContext);

        ivShoppingCart.setLayoutParams(ivShoppingCartLayoutParams);
        ivShoppingCart.setId(View.generateViewId());
        ivShoppingCart.setImageResource(R.drawable.icon_shopping_cart);

        rlShoppingCart.addView(ivShoppingCart);

        int tvNumberWidth = DensityUtil.getActualWidthOnThisDevice(30, mContext);
        RelativeLayout.LayoutParams tvNumberOfGoodsLayoutParams
                = new RelativeLayout.LayoutParams(tvNumberWidth, tvNumberWidth);
        tvNumberOfGoodsLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        tvNumberOfGoodsLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        tvNumberOfGoodsLayoutParams.rightMargin = DensityUtil.getActualWidthOnThisDevice(20, mContext);
        tvNumberOfGoodsLayoutParams.bottomMargin = DensityUtil.getActualHeightOnThisDevice(15, mContext);

        tvNumberOfGoods.setLayoutParams(tvNumberOfGoodsLayoutParams);
        tvNumberOfGoods.setBackgroundResource(R.drawable.icon_number_bg);

        tvNumberOfGoods.setTextColor(getResources().getColor(R.color.common_white));
        tvNumberOfGoods.setTextSize(Constants.FONT_SIZE_MORE_SMALLER);
        tvNumberOfGoods.setGravity(Gravity.CENTER);

        rlShoppingCart.addView(tvNumberOfGoods);

        AppUtils.addTopSeparatorLine(rlShoppingCartContainer, this);

        tvTotal.setText(R.string.shopping_pay_total);
        tvTotal.setTextColor(getResources().getColor(R.color.common_red));
        tvTotal.setTextSize(Constants.FONT_SIZE_LARGER);

        tvCurrency.setText(AppUtils.getCurrentCurrency(mContext));
        tvCurrency.setTextColor(getResources().getColor(R.color.common_red));
        tvCurrency.setTextSize(Constants.FONT_SIZE_LARGER);

        tvTotalCost.setTextColor(getResources().getColor(R.color.common_red));
        tvTotalCost.setTextSize(Constants.FONT_SIZE_LARGER);

        tvTotalCost.setText(Constants.STR_0);
        tvNumberOfGoods.setVisibility(View.INVISIBLE);

        setTotalPrice();
    }

    private void setTotalPrice() {
        double totalPrice = 0.0;
        int totalNumbers = 0;
        LinkedHashMap<String, ShoppingProduct> shoppingCart = AppUtils.getShoppingCart(getActivity());
        for (String key : shoppingCart.keySet()) {
            ShoppingProduct shoppingProduct = shoppingCart.get(key);
            double price = Double.valueOf(Utils.isStringNotNullOrEmpty(shoppingProduct.getPromotePrice()) ? shoppingProduct.getPromotePrice() : shoppingProduct.getProductPrice());
            totalPrice += price * shoppingProduct.getProductNumber();
            totalNumbers += shoppingProduct.getProductNumber();
        }
        tvTotalCost.setText(Utils.get2DigitDecimalString(String.valueOf(totalPrice)));
        if (shoppingCart.size() > 0) {
            tvNumberOfGoods.setVisibility(View.VISIBLE);
            tvNumberOfGoods.setText(String.valueOf(totalNumbers));
        } else {
            tvNumberOfGoods.setVisibility(View.INVISIBLE);
        }
    }

    private void addFooterView() {
        LinearLayout llFooterView = new LinearLayout(getBaseActivity());
        llFooterView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams footerMesParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        footerMes = new IteeTextView(getBaseActivity());
        footerMes.setLayoutParams(footerMesParams);
        footerMes.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        footerMes.setVisibility(View.GONE);
        footerMes.setSingleLine(false);
        footerMes.setPadding(DensityUtil.getActualWidthOnThisDevice(40, mContext), 0, 0, 0);

        llFooterView.addView(footerMes);
        llFooterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        searchDataList.addFooterView(llFooterView);

    }


    private void initTitleView() {
        ActionBar actionBar = getBaseActivity().getSupportActionBar();
        LinearLayout.LayoutParams titleViewParams
                = new LinearLayout.LayoutParams(Utils.getWidth(getActivity()), getActionBarHeight());
        LinearLayout titleView = new LinearLayout(getBaseActivity());
        titleView.setBackgroundColor(getColor(R.color.common_light_gray));
        titleView.setLayoutParams(titleViewParams);
        titleView.setPadding(getActualWidthOnThisDevice(0), 0, 0, 0);
        LinearLayout.LayoutParams llp
                = new LinearLayout.LayoutParams(Utils.getWidth(getActivity()) - getActualWidthOnThisDevice(160), getActionBarHeight() - 20);
        titleView.setGravity(Gravity.CENTER_VERTICAL);

        titleSearchView = new IteeSearchView(getBaseActivity());
        titleSearchView.setHint(getString(R.string.common_search));


        titleSearchView.setTextSize(14);
        titleSearchView.setBackground(getResources().getDrawable(R.drawable.bg_search_view));
        titleSearchView.setLayoutParams(llp);
        titleSearchView.setIcon(getActionBarHeight() - 20, R.drawable.icon_search_del, R.drawable.icon_search_del, R.drawable.icon_search, R.drawable.icon_search);
        titleSearchView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_UP == event.getAction()) {
                    if (keyCode == 66) {
                        Utils.hideKeyboard(getBaseActivity());
                        //api
                        getProductSearch(titleSearchView.getText().toString());
                    }
                }

                return false;
            }
        });

        titleSearchView.setRightIconListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleSearchView.setText(StringUtils.EMPTY);
            }
        });
        titleSearchView.setLeftIconListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getBaseActivity());
                getProductSearch(titleSearchView.getText().toString());
            }
        });

        titleSearchView.requestFocus();
        Utils.showKeyboard(titleSearchView, getActivity());

        LinearLayout.LayoutParams titleSearchViewLayoutParams
                = (LinearLayout.LayoutParams) titleSearchView.getLayoutParams();
        titleSearchViewLayoutParams.leftMargin = getActualWidthOnThisDevice(10);
        titleSearchView.setLayoutParams(titleSearchViewLayoutParams);
        titleView.addView(titleSearchView);

        Button cancelBtn = new Button(getBaseActivity());
        cancelBtn.setText(getString(R.string.common_cancel));
        cancelBtn.setBackgroundColor(Color.TRANSPARENT);
        cancelBtn.setTextColor(getColor(R.color.common_blue));
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity baseActivity = getBaseActivity();
                Bundle bundle = new Bundle();
                bundle.putString(TransKey.COMMON_FROM_PAGE,upFromPage);
                bundle.putString(TransKey.SHOPPING_BOOKING_NO,bookingNo);
                bundle.putString(TransKey.SHOPPING_PLAYER_NAME, playerName);
                baseActivity.changeFragment(ShoppingGoodsListFragment.class);
            }
        });
        cancelBtn.setTextSize(Constants.FONT_SIZE_NORMAL);

        setOnBackListener(new OnPopBackListener() {
            @Override
            public boolean preDoBack() {
                MainActivity baseActivity = getBaseActivity();

                Bundle bundle = new Bundle();
                bundle.putString(TransKey.COMMON_FROM_PAGE,upFromPage);
                bundle.putString(TransKey.SHOPPING_BOOKING_NO,bookingNo);

                bundle.putString(TransKey.SHOPPING_PLAYER_NAME, playerName);
                baseActivity.changeFragment(ShoppingGoodsListFragment.class,bundle);
                return false;
            }
        });
        titleView.addView(cancelBtn);
        LinearLayout.LayoutParams cancelBtnLayoutParams
                = (LinearLayout.LayoutParams) cancelBtn.getLayoutParams();
        cancelBtnLayoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        cancelBtn.setLayoutParams(cancelBtnLayoutParams);
        actionBar.setCustomView(titleView);


    }


    private void getProductSearch(String name) {
        if (titleSearchView.getText().toString().length() > 0) {
            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
            params.put(ApiKey.SHOPPING_KEYWORD, name);

            HttpManager<JsonProductSearch> hh = new HttpManager<JsonProductSearch>(this) {

                @Override
                public void onJsonSuccess(JsonProductSearch jo) {

                    Integer returnCode = jo.getReturnCode();

                    String msg = jo.getReturnInfo();
                    if (returnCode == Constants.RETURN_CODE_20301) {
                        dataList.clear();
                        dataList = jo.getDataList();
                        listViewAdapter.notifyDataSetChanged();

                        if (dataList.size() <= 0) {
                            footerMes.setText(StringUtils.EMPTY);
                            SpannableString ss = new SpannableString(titleSearchView.getText());
                            ss.setSpan(new ForegroundColorSpan(getColor(R.color.common_blue)), 0, ss.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            ss.setSpan(new RelativeSizeSpan(1.3f), 0, ss.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                            footerMes.setVisibility(View.VISIBLE);

                            footerMes.append(getString(R.string.shopping_product_search_mes1));
                            footerMes.append(ss);
                            footerMes.append(getString(R.string.shopping_product_search_mes2));
                        } else {
                            footerMes.setVisibility(View.GONE);
                        }
                    } else {
                        Utils.showShortToast(getActivity(), msg);
                    }
                }

                @Override
                public void onJsonError(VolleyError error) {

                }
            };
            hh.start(getActivity(), ApiManager.HttpApi.ProductSearch, params);
        }
    }

    private void doSelect(JsonProductSearch.SearchProduct searchProduct) {
        if (Constants.SEARCH_PRODUCT_TYPE_PACKAGE.equals(searchProduct.getType())) {
            doSelectPackage(searchProduct);
        } else if (Constants.SEARCH_PRODUCT_TYPE_PROMOTE.equals(searchProduct.getType())) {
            doSelectPromote(searchProduct);
        } else {
            doSelectProduct(searchProduct);
        }
    }

    private void doSelectPackage(final JsonProductSearch.SearchProduct searchProduct) {
        if (searchProduct.getProductList().size() > 0) {
            int index = 0;
            showPackagePop(searchProduct, index);
        }
    }

    private void showPackagePop(final JsonProductSearch.SearchProduct searchProduct, final int index) {
        if (index < searchProduct.getProductList().size()) {
            JsonProductSearch.SearchProduct.SearchSubProduct product = searchProduct.getProductList().get(index);
            if (Constants.STR_1.equals(product.getAttrStatus())) {
                ShoppingGoodsListItemFragment.OnPropertyClickListener onPropertyClickListener = new ShoppingGoodsListItemFragment.OnPropertyClickListener() {
                    @Override
                    public void OnPropertyClick(int position, JsonShopsPropertyPriceOrQtyGet.DataItem dataItem, String name) {
                        if (position == 0) {
                            searchProduct.getProductList().get(index).setProductAttrId(String.valueOf(dataItem.getPraId()));
                            nowPopUpWin.dismiss();

                            int nextIndex = index + 1;
                            showPackagePop(searchProduct, nextIndex);
                        }
                    }
                };

                nowPopUpWin = new SelectAttributePopupWindow(getActivity(), product.getProductId(),
                        product.getProductName(), product.getPrice(), Constants.STR_2, Constants.STR_EMPTY, onPropertyClickListener);
                nowPopUpWin.showAtLocation(rlShoppingCartContainer, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            } else {
                int nextIndex = index + 1;
                showPackagePop(searchProduct, nextIndex);
            }
        } else {
            addPackage(searchProduct);
        }
    }

    private void doSelectPromote(final JsonProductSearch.SearchProduct searchProduct) {
        JsonProductSearch.SearchProduct.SearchSubProduct product = searchProduct.getProductList().get(0);
        if (Constants.STR_1.equals(product.getAttrStatus())) {
            ShoppingGoodsListItemFragment.OnPropertyClickListener onPropertyClickListener
                    = new ShoppingGoodsListItemFragment.OnPropertyClickListener() {
                @Override
                public void OnPropertyClick(int position, JsonShopsPropertyPriceOrQtyGet.DataItem dataItem, String name) {
                    if (position == 0) {
                        searchProduct.setAttrId(String.valueOf(dataItem.getPraId()));
                        searchProduct.setPrice(String.valueOf(dataItem.getPraPrice()));
                        addProduct(searchProduct);
                        nowPopUpWin.dismiss();
                    }
                }
            };

            nowPopUpWin = new SelectAttributePopupWindow(getActivity(), product.getProductId(),
                    product.getProductName(), product.getPrice(), Constants.STR_1, Constants.STR_EMPTY, onPropertyClickListener);
            nowPopUpWin.showAtLocation(rlShoppingCartContainer, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else {
            searchProduct.setAttrId(product.getProductAttrId());
            addProduct(searchProduct);
        }
    }

    private void doSelectProduct(final JsonProductSearch.SearchProduct jo) {
        if (Constants.STR_1.equals(jo.getAttrStatus())) {
            JsonProductSearch.SearchProduct.SearchSubProduct product = jo.getProductList().get(0);
            ShoppingGoodsListItemFragment.OnPropertyClickListener onPropertyClickListener
                    = new ShoppingGoodsListItemFragment.OnPropertyClickListener() {
                @Override
                public void OnPropertyClick(int position, JsonShopsPropertyPriceOrQtyGet.DataItem dataItem, String name) {
                    if (position == 0) {
                        jo.setAttrId(String.valueOf(dataItem.getPraId()));
                        jo.setPrice(String.valueOf(dataItem.getPraPrice()));
                        addProduct(jo);
                        nowPopUpWin.dismiss();
                    }
                }
            };

            nowPopUpWin = new SelectAttributePopupWindow(getActivity(), product.getProductId(),
                    product.getProductName(), product.getPrice(), Constants.STR_2, Constants.STR_EMPTY,
                    onPropertyClickListener);
            nowPopUpWin.showAtLocation(rlShoppingCartContainer, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        } else {
            addProduct(jo);
        }
    }

    private void addProduct(JsonProductSearch.SearchProduct product) {
        if (!greenId.equals(product.getId())) {
            if (product.getProductList().size() == 1) {
                String mapId = product.getSalesTypeId() + Constants.STR_COMMA + product.getId() + Constants.STR_COMMA;
                if (Utils.isStringNotNullOrEmpty(product.getAttrId())) {
                    mapId += product.getAttrId();
                }
                add2ShoppingCart(product, mapId);
            } else {
                String mapId = product.getSalesTypeId() + Constants.STR_COMMA + product.getId() + Constants.STR_COMMA;
                add2ShoppingCart(product, mapId);
            }
        }
    }

    private void addPackage(JsonProductSearch.SearchProduct jo) {
        String mapId = jo.getSalesTypeId() + Constants.STR_COMMA + jo.getId() + Constants.STR_COMMA;
        add2ShoppingCart(jo, mapId);
    }

    private ShoppingProduct getShoppingProduct(JsonProductSearch.SearchProduct searchProduct) {
        ShoppingProduct shoppingProduct = new ShoppingProduct();
        shoppingProduct.setProductPrice(searchProduct.getPrice());
        if (Constants.SEARCH_PRODUCT_TYPE_PACKAGE.equals(searchProduct.getType())) {
            shoppingProduct.setPackageId(searchProduct.getId());
        } else if (Constants.SEARCH_PRODUCT_TYPE_PROMOTE.equals(searchProduct.getType())) {
            shoppingProduct.setPromoteId(searchProduct.getId());
            JsonProductSearch.SearchProduct.SearchSubProduct product = searchProduct.getProductList().get(0);
            shoppingProduct.setPromotePrice(product.getDiscountPrice());
            shoppingProduct.setProductPrice(product.getDiscountPrice());
        } else {
            shoppingProduct.setProductId(searchProduct.getId());
        }
        shoppingProduct.setProductAttribute(searchProduct.getAttrId());
        shoppingProduct.setProductName(searchProduct.getName());
        if (Utils.isListNotNullOrEmpty(searchProduct.getProductList())) {
            for (JsonProductSearch.SearchProduct.SearchSubProduct product : searchProduct.getProductList()) {
                ShoppingProduct.ShoppingSubProduct subProduct = new ShoppingProduct.ShoppingSubProduct();
                subProduct.setProductId(product.getProductId());
                subProduct.setProductName(product.getProductName());
                if (Utils.isStringNotNullOrEmpty(product.getProductAttrId())) {
                    subProduct.setProductAttrId(product.getProductAttrId());
                }
                subProduct.setProductAttr(product.getProductName());
                subProduct.setNumber(product.getNumber());
                subProduct.setPrice(product.getPrice());
                shoppingProduct.getProductList().add(subProduct);
            }
        }
        return shoppingProduct;
    }

    private void add2ShoppingCart(JsonProductSearch.SearchProduct jo, String mapId) {
        LinkedHashMap<String, ShoppingProduct> shoppingCart = AppUtils.getShoppingCart(getActivity());
        ShoppingProduct shoppingProduct = shoppingCart.get(mapId);
        if (shoppingProduct != null) {
            shoppingProduct.setProductNumber(shoppingProduct.getProductNumber() + 1);
        } else {
            shoppingProduct = getShoppingProduct(jo);
            shoppingProduct.setProductNumber(1);
        }
        shoppingCart.put(mapId, shoppingProduct);
        AppUtils.saveShoppingCart(shoppingCart, getActivity());
        setTotalPrice();
    }

    class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataList.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_of_product_search, null);
                viewHolder = new ViewHolder();

                viewHolder.llSubContainer = (LinearLayout) convertView.findViewById(R.id.ll_sub_container);
                viewHolder.rlContainer = (RelativeLayout) convertView.findViewById(R.id.rl_row_container);
                viewHolder.tvName = (IteeTextView) convertView.findViewById(R.id.tv_name);
                viewHolder.tvOriginalPrice = (IteeTextView) convertView.findViewById(R.id.tv_original_price);
                viewHolder.tvPrice = (IteeTextView) convertView.findViewById(R.id.tv_price);

                viewHolder.tvOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

                //LayoutUtils.setCellLeftKeyViewOfRelativeLayout(viewHolder.tvName, mContext);
                //LayoutUtils.setCellRightValueViewOfRelativeLayout(viewHolder.tvPrice, mContext);
                //LayoutUtils.setWidthAndHeight(viewHolder.tvPrice, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

                LayoutUtils.setLayoutHeight(viewHolder.rlContainer, 100, mContext);
                LayoutUtils.setViewHeightAndAlignParentBottom(viewHolder.tvPrice, 45, mContext);
                LayoutUtils.setLeftOfView(viewHolder.tvOriginalPrice, viewHolder.tvPrice, 20, mContext);


                viewHolder.tvName.setGravity(Gravity.START);
                viewHolder.tvOriginalPrice.setGravity(Gravity.END);
                viewHolder.tvPrice.setGravity(Gravity.END);

                viewHolder.tvOriginalPrice.setTextColor(getColor(R.color.common_gray));

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            JsonProductSearch.SearchProduct searchProduct = dataList.get(position);
            if (Constants.SEARCH_PRODUCT_TYPE_PACKAGE.equals(searchProduct.getType())) {
                viewHolder.tvOriginalPrice.setVisibility(View.GONE);
                viewHolder.llSubContainer.setVisibility(View.VISIBLE);
                viewHolder.tvPrice.setText(AppUtils.addCurrencySymbol(searchProduct.getPrice(), mContext));
                viewHolder.llSubContainer.removeAllViews();
                for (JsonProductSearch.SearchProduct.SearchSubProduct subProduct : searchProduct.getProductList()) {
                    RelativeLayout relativeLayout = generatePackageSubProductLayout(subProduct);
                    viewHolder.llSubContainer.addView(relativeLayout);
                    LayoutUtils.setLayoutHeight(relativeLayout, 60, mContext);
                }
            } else if (Constants.SEARCH_PRODUCT_TYPE_PROMOTE.equals(searchProduct.getType())) {
                viewHolder.tvOriginalPrice.setVisibility(View.VISIBLE);
                viewHolder.llSubContainer.setVisibility(View.GONE);
                JsonProductSearch.SearchProduct.SearchSubProduct subProduct
                        = searchProduct.getProductList().get(0);
                viewHolder.tvOriginalPrice.setText(AppUtils.addCurrencySymbol(searchProduct.getPrice(), mContext));
                viewHolder.tvPrice.setText(AppUtils.addCurrencySymbol(subProduct.getDiscountPrice(), mContext));
            } else {
                viewHolder.tvOriginalPrice.setVisibility(View.GONE);
                viewHolder.llSubContainer.setVisibility(View.GONE);
                viewHolder.tvPrice.setText(AppUtils.addCurrencySymbol(searchProduct.getPrice(), mContext));
            }
            viewHolder.tvName.setText(searchProduct.getName());


            return convertView;
        }

        private RelativeLayout generatePackageSubProductLayout(JsonProductSearch.SearchProduct.SearchSubProduct subProduct) {

            RelativeLayout relativeLayout = new RelativeLayout(mContext);
            relativeLayout.setBackgroundColor(getColor(R.color.common_light_gray));

            IteeTextView tvName = new IteeTextView(mContext);
            IteeTextView tvPrice = new IteeTextView(mContext);
            IteeTextView tvNumber = new IteeTextView(mContext);

            tvPrice.setTextSize(Constants.FONT_SIZE_SMALLER);
            tvNumber.setTextSize(Constants.FONT_SIZE_MORE_SMALLER);
            tvNumber.setTextColor(getColor(R.color.common_blue));

            tvName.setText(subProduct.getProductName());
            tvPrice.setText(AppUtils.addCurrencySymbol(subProduct.getPrice(), mContext));
            tvNumber.setText(Constants.STR_MULTIPLY + subProduct.getNumber());

            relativeLayout.addView(tvName);
            relativeLayout.addView(tvPrice);
            relativeLayout.addView(tvNumber);

            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvName, 80, mContext);
            LayoutUtils.setCellRightValueViewOfRelativeLayout(tvPrice, mContext);
            LayoutUtils.setCellRightValueViewOfRelativeLayout(tvNumber, mContext);
            LayoutUtils.setViewHeightAndAlignParentTop(tvPrice, 50, mContext);
            LayoutUtils.setViewHeightAndAlignParentBottom(tvNumber, 30, mContext);

            return relativeLayout;
        }

        class ViewHolder extends BaseViewHolder {

            IteeTextView tvName;
            IteeTextView tvOriginalPrice;
            IteeTextView tvPrice;
            LinearLayout llSubContainer;
            RelativeLayout rlContainer;
        }
    }
}