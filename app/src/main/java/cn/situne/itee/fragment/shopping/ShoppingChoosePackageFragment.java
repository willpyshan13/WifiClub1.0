/**
 * Project Name: itee
 * File Name:	 ShoppingChoosePackageFragment.java
 * Package Name: cn.situne.itee.fragment.shopping
 * Date:		 2015-05-06
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.shopping;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.ShoppingProduct;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonChoosePackageGet;
import cn.situne.itee.manager.jsonentity.JsonShopsPropertyPriceOrQtyGet;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.SelectAttributePopupWindow;

/**
 * ClassName:ShoppingChoosePackageFragment <br/>
 * Function: 套餐匹配. <br/>
 * UI:  06-7
 * Date: 2015-05-06 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class ShoppingChoosePackageFragment extends BaseFragment {

    private ArrayList<JsonChoosePackageGet.Package> dataList;

    private ListViewAdapter listViewAdapter;

    private String bookingNo;
    private String playerName;

    private String fromPage;


    private SelectAttributePopupWindow nowPopUpWin;
    private int nowPopWinIndex;
    private ShoppingProduct shoppingProduct;
    private ShoppingProduct.ShoppingSubProduct subProduct;


    private String availableProduct;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_shopping_choose_package;

    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        dataList = new ArrayList<>();
        Bundle bundle = getArguments();

        if (bundle != null) {
            bookingNo = bundle.getString(TransKey.SHOPPING_BOOKING_NO);
            playerName = bundle.getString(TransKey.SHOPPING_PLAYER_NAME);

            fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE);

            availableProduct = bundle.getString(TransKey.SHOPPING_AVAILABLE_PRODUCT);
        }


        ListView listView = (ListView) rootView.findViewById(R.id.lv_package_list);
        listView.setSelector(android.R.color.transparent);
        listViewAdapter = new ListViewAdapter();
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                int position = (int) view.getTag();
                JsonChoosePackageGet.Package selectPackage = dataList.get(position);

                shoppingProduct = new ShoppingProduct();
                shoppingProduct.setProductName(selectPackage.getPackageName());
                shoppingProduct.setProductPrice(String.valueOf(selectPackage.getPrice()));
                shoppingProduct.setProductNumber(1);
                shoppingProduct.setPackageId(selectPackage.getPackageId());
                ArrayList<ShoppingProduct.ShoppingSubProduct> productList = new ArrayList<>();
                for (JsonChoosePackageGet.Product product : selectPackage.getProductArrayList()) {
                    ShoppingProduct.ShoppingSubProduct subProduct = new ShoppingProduct.ShoppingSubProduct();
                    subProduct.setProductId(product.getProductId());
                    subProduct.setProductStatus(product.getProductStatus());
                    subProduct.setProductName(product.getPdName());
                    subProduct.setProductAttrId(product.getProductAttrId());
                    subProduct.setNumber(product.getNumber());
                    subProduct.setAttriStatus(product.getAttriStatus());
                    subProduct.setProductAttr(product.getProductAttr());
                    subProduct.setPrice(product.getPrice());
                    productList.add(subProduct);
                }
                shoppingProduct.setProductList(productList);
                circulateShow();


            }
        });

        getRecommendPackage();

    }

    @SuppressWarnings("unchecked")
    private void circulateShow() {
        subProduct = shoppingProduct.getProductList().get(nowPopWinIndex);
        if ("1".equals(subProduct.getAttriStatus())) {
            ShoppingGoodsListItemFragment.OnPropertyClickListener onPropertyClickListener = new ShoppingGoodsListItemFragment.OnPropertyClickListener() {
                @Override
                public void OnPropertyClick(int position, JsonShopsPropertyPriceOrQtyGet.DataItem dataItem, String name) {
                    if (position == 0) {

//                        subProduct.setPrice(String.valueOf(dataItem.getPraPrice()));
                        subProduct.setProductName(subProduct.getProductName() + "(" + name + ")");
                        subProduct.setProductAttrId(String.valueOf(dataItem.getPraId()));
                        nowPopUpWin.dismiss();

                        if (nowPopWinIndex < shoppingProduct.getProductList().size()) {

                            if (nowPopWinIndex == shoppingProduct.getProductList().size() - 1) {
                                Bundle bundle = new Bundle();
                                bundle.putString(TransKey.SHOPPING_BOOKING_NO, bookingNo);
                                bundle.putString(TransKey.SHOPPING_PLAYER_NAME, playerName);
                                bundle.putString(TransKey.SHOPPING_PACKAGE, Utils.getStringFromObject(shoppingProduct));
                                bundle.putString(TransKey.COMMON_FROM_PAGE, ShoppingChoosePackageFragment.class.getName());

                                try {
                                    doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
                                } catch (ClassNotFoundException e) {
                                    Utils.log(e.getMessage());
                                }
                                nowPopWinIndex = 0;
                            } else {
                                nowPopWinIndex++;
                                ShoppingProduct.ShoppingSubProduct tempSubProduct = shoppingProduct.getProductList().get(nowPopWinIndex);
                                if (tempSubProduct.getAttriStatus().equals(Constants.STR_1)) {
                                    circulateShow();
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString(TransKey.SHOPPING_BOOKING_NO, bookingNo);
                                    bundle.putString(TransKey.SHOPPING_PLAYER_NAME, playerName);
                                    bundle.putString(TransKey.SHOPPING_PACKAGE, Utils.getStringFromObject(shoppingProduct));
                                    bundle.putString(TransKey.COMMON_FROM_PAGE, ShoppingChoosePackageFragment.class.getName());

                                    try {
                                        doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
                                    } catch (ClassNotFoundException e) {
                                        Utils.log(e.getMessage());
                                    }
                                    nowPopWinIndex = 0;
                                }

                            }
                        }
                    } else {
                        nowPopWinIndex = 0;
                    }


                }
            };

            nowPopUpWin = new SelectAttributePopupWindow(getActivity(), subProduct.getProductId(), subProduct.getProductName(), subProduct.getPrice(), Constants.STR_2, StringUtils.EMPTY, onPropertyClickListener);
            nowPopUpWin.showAtLocation(ShoppingChoosePackageFragment.this.getRootView().findViewById(R.id.rl_content_container), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(TransKey.SHOPPING_BOOKING_NO, bookingNo);
            bundle.putString(TransKey.SHOPPING_PLAYER_NAME, playerName);
            bundle.putString(TransKey.SHOPPING_PACKAGE, Utils.getStringFromObject(shoppingProduct));
            bundle.putString(TransKey.COMMON_FROM_PAGE, ShoppingChoosePackageFragment.class.getName());

            try {
                doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
            } catch (ClassNotFoundException e) {
                Utils.log(e.getMessage());
            }
        }
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {

    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {

    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(R.string.shopping_package_shop);
    }

    private void getRecommendPackage() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPPING_BOOKING_NO, bookingNo);
        params.put(ApiKey.SHOPPING_PURCHASE_PRODUCT_LIST, availableProduct);

        HttpManager<JsonChoosePackageGet> hh = new HttpManager<JsonChoosePackageGet>(ShoppingChoosePackageFragment.this) {
            @Override
            public void onJsonSuccess(JsonChoosePackageGet jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    dataList = jo.getDataList();
                    listViewAdapter.notifyDataSetChanged();

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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.ShoppingChoosePackage, params);
    }

    class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ListViewItem item;
            if (view == null) {
                item = new ListViewItem(ShoppingChoosePackageFragment.this);
            } else {
                item = (ListViewItem) view;
            }
            item.refreshView(dataList.get(i));
            item.setTag(i);

            return item;
        }
    }


    class ListViewItem extends LinearLayout {

        private BaseFragment mFragment;
        private Context mContext;

        //layout
        private IteeTextView tvPackageName;
        private IteeTextView tvPackagePrice;

        private LinearLayout llProductList;

        public ListViewItem(BaseFragment fragment) {
            super(fragment.getBaseActivity());
            mFragment = fragment;
            mContext = fragment.getBaseActivity();
            this.setOrientation(VERTICAL);
            ListView.LayoutParams myParams = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,
                    ListView.LayoutParams.WRAP_CONTENT);
            this.setLayoutParams(myParams);

            LinearLayout.LayoutParams packageTitleParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mFragment.getActualHeightOnThisDevice(100));
            RelativeLayout packageTitle = new RelativeLayout(mContext);
            packageTitle.setLayoutParams(packageTitleParams);

            RelativeLayout.LayoutParams tvPackageNameParams = new RelativeLayout.LayoutParams((int) (mFragment.getScreenWidth() * 0.7), RelativeLayout.LayoutParams.MATCH_PARENT);

            tvPackageNameParams.leftMargin = mFragment.getActualWidthOnThisDevice(20);
            tvPackageName = new IteeTextView(mContext);
            tvPackageName.setLayoutParams(tvPackageNameParams);


            RelativeLayout.LayoutParams tvPackagePriceParams = new RelativeLayout.LayoutParams(mFragment.getActualWidthOnThisDevice(200), RelativeLayout.LayoutParams.MATCH_PARENT);
            tvPackagePriceParams.rightMargin = fragment.getActualWidthOnThisDevice(20);
            tvPackagePriceParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            tvPackagePrice = new IteeTextView(mContext);
            tvPackagePrice.setLayoutParams(tvPackagePriceParams);
            tvPackagePrice.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            packageTitle.addView(tvPackageName);
            packageTitle.addView(tvPackagePrice);

            RelativeLayout.LayoutParams llProductListParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            llProductList = new LinearLayout(mContext);
            llProductList.setOrientation(VERTICAL);
            llProductList.setLayoutParams(llProductListParams);
            llProductList.setBackgroundColor(mFragment.getColor(R.color.common_gray));
            addView(packageTitle);
            addView(llProductList);
        }

        public void refreshView(JsonChoosePackageGet.Package packageData) {
            tvPackageName.setText(packageData.getPackageName());
            tvPackagePrice.setText(AppUtils.getCurrentCurrency(mContext) + packageData.getPrice());

            llProductList.removeAllViews();

            for (JsonChoosePackageGet.Product product : packageData.getProductArrayList()) {
                LinearLayout.LayoutParams productItemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mFragment.getActualHeightOnThisDevice(100));
                RelativeLayout productItem = new RelativeLayout(mContext);
                productItem.setLayoutParams(productItemParams);

                RelativeLayout.LayoutParams tvProductNameParams
                        = new RelativeLayout.LayoutParams((int) (mFragment.getScreenWidth() * 0.6), RelativeLayout.LayoutParams.MATCH_PARENT);
                tvProductNameParams.leftMargin = mFragment.getActualWidthOnThisDevice(20);
                RelativeLayout.LayoutParams tvProductPriceParams
                        = new RelativeLayout.LayoutParams(mFragment.getActualWidthOnThisDevice(200), mFragment.getActualHeightOnThisDevice(60));
                tvProductPriceParams.rightMargin = mFragment.getActualWidthOnThisDevice(20);
                tvProductPriceParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                IteeTextView tvProductName = new IteeTextView(mContext);
                tvProductName.setLayoutParams(tvProductNameParams);
                IteeTextView tvProductPrice = new IteeTextView(mContext);
                tvProductPrice.setLayoutParams(tvProductPriceParams);
                tvProductPrice.setId(View.generateViewId());

                RelativeLayout.LayoutParams tvProductNumberParams = new RelativeLayout.LayoutParams(mFragment.getActualWidthOnThisDevice(200), mFragment.getActualHeightOnThisDevice(40));

                tvProductNumberParams.rightMargin = mFragment.getActualWidthOnThisDevice(20);
                tvProductNumberParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                tvProductNumberParams.addRule(RelativeLayout.BELOW, tvProductPrice.getId());
                IteeTextView tvProductNumber = new IteeTextView(mContext);
                tvProductNumber.setLayoutParams(tvProductNumberParams);

                if (Utils.isStringNotNullOrEmpty(product.getProductAttr())) {
                    tvProductName.setText(product.getPdName() + Constants.STR_BRACKETS_START + product.getProductAttr() + Constants.STR_BRACKETS_END);
                } else {
                    tvProductName.setText(product.getPdName());
                }


                tvProductPrice.setText(AppUtils.getCurrentCurrency(mContext) + product.getPrice());
                tvProductNumber.setText(Constants.STR_MULTIPLY + product.getNumber());

                productItem.addView(tvProductName);
                productItem.addView(tvProductPrice);
                productItem.addView(tvProductNumber);
                tvProductPrice.setGravity(Gravity.END | Gravity.BOTTOM);
                tvProductNumber.setGravity(Gravity.END | Gravity.TOP);
                tvProductNumber.setTextSize(Constants.FONT_SIZE_MORE_SMALLER);

                if (Constants.STR_1.equals(product.getProductStatus())) {
                    tvProductName.setTextColor(mFragment.getColor(R.color.common_red));
                } else {
                    tvProductName.setTextColor(mFragment.getColor(R.color.common_black));
                }

                llProductList.addView(productItem);
            }
        }
    }
}
