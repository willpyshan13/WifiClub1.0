/**
 * Project Name: itee
 * File Name:	 ShopsPackageListFragment.java
 * Package Name: cn.situne.itee.fragment.shops
 * Date:		 2015-03-21
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.shops;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonPackageGet;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:ShopsPackageListFragment <br/>
 * Function: 套餐 列表 <br/>
 * UI:  05-04
 * Date: 2015-03-21 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */

public class ShopsPackageListFragment extends BaseFragment {


    private PullToRefreshListView packageListView;
    private ArrayList<JsonPackageGet.PackageData> packageList;
    private PackageTypeAdapter packageTypeAdapter;
    private String greenId;

    private int currentPage;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_package_shop;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        packageList = new ArrayList<>();

        packageListView = (PullToRefreshListView) rootView.findViewById(R.id.packageList);
        packageListView.getRefreshableView().setDividerHeight(0);
        packageListView.setBackgroundColor(getColor(R.color.common_white));
        packageListView.getRefreshableView().setDivider(null);
        packageTypeAdapter = new PackageTypeAdapter();
    }

    @Override
    protected void setDefaultValueOfControls() {
        packageListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getPackage(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getPackage(false);
            }
        });
    }

    @Override
    protected void setListenersOfControls() {

    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {
        packageListView.setAdapter(packageTypeAdapter);
        packageTypeAdapter.notifyDataSetChanged();

        packageListView.setMode(PullToRefreshBase.Mode.BOTH);

        ILoadingLayout headerLayoutProxy = packageListView.getLoadingLayoutProxy(true, false);
        ILoadingLayout footerLayoutProxy = packageListView.getLoadingLayoutProxy(false, true);

        headerLayoutProxy.setPullLabel(getString(R.string.app_list_header_refresh_down));
        headerLayoutProxy.setRefreshingLabel(getString(R.string.app_list_header_refresh_down));
        headerLayoutProxy.setReleaseLabel(getString(R.string.app_list_header_refresh_down));

        footerLayoutProxy.setPullLabel(getString(R.string.app_list_header_refresh_loading_more));
        footerLayoutProxy.setRefreshingLabel(getString(R.string.app_list_header_refresh_loading_more));
        footerLayoutProxy.setReleaseLabel(getString(R.string.app_list_header_refresh_loading_more));
    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        getPackage(true);
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getString(R.string.shop_setting_package));
        getTvRight().setVisibility(View.VISIBLE);

        getTvRight().setBackgroundResource(R.drawable.icon_common_add);
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString(TransKey.COMMON_GREEN_ID, greenId);
                bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeAdd.value());
                bundle.putString(TransKey.UNLIMITED_FLAG, Constants.UNLIMITED_FLAG_OFF);
                push(ShopsPackageEditFragment.class, bundle);
            }
        });
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
        params.put(ApiKey.SHOPS_PRODUCT_SETTING_STATUS, Constants.STR_1);

        HttpManager<JsonPackageGet> hh = new HttpManager<JsonPackageGet>(ShopsPackageListFragment.this) {

            @Override
            public void onJsonSuccess(JsonPackageGet jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    if (isRefresh) {
                        packageList.clear();
                    }
                    if (jo.getPackageList().size() > 0) {
                        currentPage = jo.getPage();
                    }
                    greenId = jo.getGreenId();
                    initData(jo.getPackageList());
                    packageListView.onRefreshComplete();
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }

            }

            @Override
            public void onJsonError(VolleyError error) {
                packageListView.onRefreshComplete();
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(getActivity(), R.string.msg_common_network_error);
            }
        };
        hh.startGet(this.getActivity(), ApiManager.HttpApi.ShopsPackageGet, params);
    }

    private void initData(ArrayList<JsonPackageGet.PackageData> dataList) {
        packageList.addAll(dataList);
        packageTypeAdapter.notifyDataSetChanged();
    }


    class PackageTypeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return packageList == null ? 0 : packageList.size();
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
            // ListView.LayoutParams myParams = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
            ListViewItem item = new ListViewItem(getBaseActivity());
            //  item.setLayoutParams(myParams);
            item.refreshView(packageList.get(position), getActualWidthOnThisDevice(40));

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    JsonPackageGet.PackageData data = packageList.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeBrowse.value());
                    bundle.putInt(TransKey.SHOPS_PRODUCT_PACKAGE_ID, data.getPackageId());
                    bundle.putString(TransKey.SHOPS_PRODUCT_PACKAGE_NAME, data.getPackageName());
                    bundle.putString(TransKey.SHOPS_PRODUCT_PACKAGE_CODE, data.getPackageCode());
                    bundle.putString(TransKey.UNLIMITED_FLAG, data.getUnlimitedFlag());

                    bundle.putInt(TransKey.SHOPS_PRODUCT_PACKAGE_QTY, data.getPackageQty());
                    bundle.putString(TransKey.SHOPS_PRODUCT_PACKAGE_CHILD_ITEM, data.getJsProductItem().toString());
                    bundle.putString(TransKey.COMMON_GREEN_ID, greenId);
                    push(ShopsPackageEditFragment.class, bundle);
                }
            });

            item.setTag(position);
            return item;
        }
    }


    class ListViewItem extends LinearLayout {
        public RelativeLayout rlPackage;
        public LinearLayout llBody;
        public IteeTextView tvPackageName;
        public IteeTextView tvPackageValue;

        public ListViewItem(Context context) {
            super(context);
            this.setOrientation(VERTICAL);
            initView(context);

        }

        private void initView(Context context) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            View convertView = mInflater.inflate(R.layout.package_shop_item, null);
            tvPackageName = (IteeTextView) convertView.findViewById(R.id.tvPackageName);

            RelativeLayout.LayoutParams tvPackageNameParams = (RelativeLayout.LayoutParams) tvPackageName.getLayoutParams();
            tvPackageNameParams.width = ShopsPackageListFragment.this.getActualWidthOnThisDevice(500);

            tvPackageValue = (IteeTextView) convertView.findViewById(R.id.tvPackageValue);
            rlPackage = (RelativeLayout) convertView.findViewById(R.id.rl_package);
            rlPackage.setBackgroundColor(ShopsPackageListFragment.this.getColor(R.color.common_white));

            llBody = (LinearLayout) convertView.findViewById(R.id.ll_body);
            this.addView(convertView);

        }


        private JsonPackageGet.DataItem findPackageItemForProductId(List<JsonPackageGet.DataItem> resList, String productId) {

            for (JsonPackageGet.DataItem item : resList) {

                if (item.getProductId().equals(productId)) {
                    return item;
                }
            }

            return null;
        }


        private ArrayList<JsonPackageGet.DataItem> getNewPackageData(ArrayList<JsonPackageGet.DataItem> dataList) {
            ArrayList<JsonPackageGet.DataItem> resList = new ArrayList<>();
            for (JsonPackageGet.DataItem item : dataList) {
                JsonPackageGet.DataItem resItem = findPackageItemForProductId(resList, item.getProductId());
                if (resItem == null) {
                    resItem = new JsonPackageGet.DataItem();
                    resItem.setId(item.getId());
                    resItem.setPrice(item.getPrice());
                    resItem.setProductAttrId(item.getProductAttrId());
                    resItem.setProductId(item.getProductId());
                    resItem.setProductAttr(item.getProductAttr());
                    resItem.setNumber(item.getNumber());
                    resItem.setType(item.getType());
                    resItem.setMaxPrice(item.getMaxPrice());
                    resItem.setDiscountPrice(item.getDiscountPrice());
                    resItem.setPdName(item.getPdName());
                    resItem.setMinPrice(item.getPrice());
                    resList.add(resItem);

                } else {

                    if (greenId.equals(item.getProductId())) {
                        resList.add(item);
                    } else {
                        resItem.setNumber(resItem.getNumber() + item.getNumber());
                        double itemPrice = item.getPrice();

                        if (itemPrice < resItem.getMinPrice()) {
                            resItem.setMaxPrice(resItem.getMinPrice());
                            resItem.setMinPrice(itemPrice);
                        }
                        if (itemPrice > resItem.getMinPrice()) {
                            if (resItem.getMaxPrice() < 0) {
                                resItem.setMaxPrice(itemPrice);
                            } else {
                                if (itemPrice > resItem.getMaxPrice()) {
                                    resItem.setMaxPrice(itemPrice);
                                }
                            }
                        }
                    }
                }
            }
            return resList;
        }

        public void refreshView(JsonPackageGet.PackageData data, int padding) {
            rlPackage.setPadding(padding, 0, padding, 0);
            tvPackageName.setText(data.getPackageName());
            tvPackageValue.setText(AppUtils.getCurrentCurrency(getActivity()) + Constants.STR_SPACE
                    + Utils.get2DigitDecimalString(data.getPackagePrice()));

            if (data.getPackageQty() > 0) {
                tvPackageName.setTextColor(getColor(R.color.common_black));
            } else {
                if (Constants.STR_FLAG_NO.equals(data.getUnlimitedFlag())) {
                    tvPackageName.setTextColor(getColor(R.color.common_red));
                } else {
                    tvPackageName.setTextColor(getColor(R.color.common_black));
                }
            }

            List<JsonPackageGet.DataItem> showList = getNewPackageData(data.getProductList());

            for (JsonPackageGet.DataItem item : showList) {
                RelativeLayout childLayout = new RelativeLayout(getBaseActivity());
                childLayout.setBackgroundColor(getColor(R.color.common_package_gray));
                childLayout.setPadding(padding, 0, padding, 0);

                RelativeLayout.LayoutParams tvChildNameParams
                        = new RelativeLayout.LayoutParams(ShopsPackageListFragment.this.getActualWidthOnThisDevice(500),
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                tvChildNameParams.addRule(RelativeLayout.CENTER_VERTICAL);

                IteeTextView tvChildName = new IteeTextView(getBaseActivity());
                tvChildName.setGravity(Gravity.CENTER_VERTICAL);
                tvChildName.setLayoutParams(tvChildNameParams);
                if (Utils.isStringNotNullOrEmpty(item.getProductAttr())) {
                    tvChildName.setText(item.getPdName() + Constants.STR_BRACKETS_START + item.getProductAttr() + Constants.STR_BRACKETS_END);
                } else {
                    tvChildName.setText(item.getPdName());
                }

                RelativeLayout.LayoutParams llChildRightLayoutParams = new RelativeLayout.LayoutParams(ShopsPackageListFragment.this.getActualWidthOnThisDevice(300), RelativeLayout.LayoutParams.MATCH_PARENT);
                llChildRightLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                LinearLayout llChildRightLayout = new LinearLayout(getBaseActivity());
                llChildRightLayout.setLayoutParams(llChildRightLayoutParams);
                llChildRightLayout.setOrientation(VERTICAL);

                LinearLayout.LayoutParams tvChildPriceParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                tvChildPriceParams.weight = 1;
                IteeTextView tvChildPrice = new IteeTextView(getBaseActivity());
                tvChildPrice.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
                tvChildPrice.setLayoutParams(tvChildNameParams);
                tvChildPrice.setLayoutParams(tvChildPriceParams);
                tvChildPrice.setText(AppUtils.getCurrentCurrency(getActivity()) + Constants.STR_SPACE
                        + Utils.get2DigitDecimalString(item.getDiscountPrice()));


                LinearLayout.LayoutParams tvChildNumParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                tvChildNumParams.weight = 1;

                IteeTextView tvChildNum = new IteeTextView(getBaseActivity());
                tvChildNum.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
                tvChildNum.setLayoutParams(tvChildNameParams);
                tvChildNum.setText(item.getPdName());
                tvChildNum.setLayoutParams(tvChildNumParams);
                tvChildNum.setText(Constants.STR_MULTIPLY + String.valueOf(item.getNumber()));
                tvChildNum.setTextColor(getColor(R.color.common_blue));

                llChildRightLayout.addView(tvChildPrice);
                llChildRightLayout.addView(tvChildNum);

                childLayout.addView(tvChildName);

                childLayout.addView(llChildRightLayout);
                llBody.addView(childLayout);
            }

            llBody.addView(AppUtils.getSeparatorLine(ShopsPackageListFragment.this));
            LinearLayout.LayoutParams vParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ShopsPackageListFragment.this.getActualHeightOnThisDevice(30));
            View v = new View(getBaseActivity());
            v.setLayoutParams(vParams);
            v.setBackgroundColor(getColor(R.color.common_package_separator_gray));
            llBody.addView(v);
            llBody.addView(AppUtils.getSeparatorLine(ShopsPackageListFragment.this));

        }
    }


}
