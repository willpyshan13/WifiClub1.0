/**
 * Project Name: itee
 * File Name:  RentalShopFragment.java
 * Package Name: cn.situne.itee.fragment.shops
 * Date:   2015-03-05
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.shops;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

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
import cn.situne.itee.manager.jsonentity.JsonShopRentalProductGet;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:RentalShopFragment <br/>
 * Function: Rental shops list. <br/>
 * Date: 2015-03-05 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class ShopsRentalShopFragment extends BaseFragment {


    private String rentalShopData;
    private ListViewAdapter ad;
    private ArrayList<String> propertyPicIdList;
    private List<JsonShopRentalProductGet.DataList> dataList;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_shops_rental_shop;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        ListView rentalList = (ListView) rootView.findViewById(R.id.rentalList);
        propertyPicIdList = new ArrayList<>();
        propertyPicIdList.clear();

        propertyPicIdList.add(Constants.RENTAL_PRODUCT_TYPE_CADDIE);

        propertyPicIdList.add(Constants.RENTAL_PRODUCT_TYPE_CART);
        propertyPicIdList.add(Constants.RENTAL_PRODUCT_TYPE_CLUBS);
        propertyPicIdList.add(Constants.RENTAL_PRODUCT_TYPE_SHOES);
        propertyPicIdList.add(Constants.RENTAL_PRODUCT_TYPE_TROLLEY);
        propertyPicIdList.add(Constants.RENTAL_PRODUCT_TYPE_UMBRELLA);
        propertyPicIdList.add(Constants.RENTAL_PRODUCT_TYPE_TOWEL);

        ad = new ListViewAdapter();

        dataList = new ArrayList<>();


        rentalList.setAdapter(ad);

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
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        getRentalShopData();
    }

    private void getRentalShopData() {

        Map<String, String> params = new HashMap<>();

        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        HttpManager<JsonShopRentalProductGet> hh = new HttpManager<JsonShopRentalProductGet>(ShopsRentalShopFragment.this) {

            @Override
            public void onJsonSuccess(JsonShopRentalProductGet jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {

                    rentalShopData = jo.getmJsonObj().toString();
                    addDataList(jo.getDataList());
                    ad.notifyDataSetChanged();
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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.ShopsRentalProductGet, params);
    }


    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(R.string.shop_setting_rental);
        getTvRight().setBackgroundResource(R.drawable.icon_common_add);
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (propertyPicIdList.size() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeAdd.value());
                    bundle.putString(TransKey.SHOPS_RENTAL_SHOP_DATA, rentalShopData);
                    bundle.putStringArrayList(TransKey.SHOPS_PROPERTY_PIC_IDS, propertyPicIdList);
                    push(ShopsRentalEditFragment.class, bundle);
                } else {
                    Utils.showShortToast(getBaseActivity(), R.string.msg_no_more_rental);

                }


            }
        });
    }

    private void addDataList(List<JsonShopRentalProductGet.DataList> data) {

        propertyPicIdList.clear();

        propertyPicIdList.add(Constants.RENTAL_PRODUCT_TYPE_CADDIE);

        propertyPicIdList.add(Constants.RENTAL_PRODUCT_TYPE_CART);
        propertyPicIdList.add(Constants.RENTAL_PRODUCT_TYPE_CLUBS);
        propertyPicIdList.add(Constants.RENTAL_PRODUCT_TYPE_SHOES);
        propertyPicIdList.add(Constants.RENTAL_PRODUCT_TYPE_TROLLEY);
        propertyPicIdList.add(Constants.RENTAL_PRODUCT_TYPE_UMBRELLA);
        propertyPicIdList.add(Constants.RENTAL_PRODUCT_TYPE_TOWEL);


        dataList.clear();
        JsonShopRentalProductGet.DataList reserveFee = new JsonShopRentalProductGet.DataList();
        reserveFee.setPdPicId(-1);
        reserveFee.setProductName(getString(R.string.shop_setting_reserve_fee));
        reserveFee.setBookingFee(-1);

        dataList.add(reserveFee);


        JsonShopRentalProductGet.DataList green = new JsonShopRentalProductGet.DataList();
        green.setPdPicId(-1);
        green.setProductName(getString(R.string.common_green_fee));
        green.setBookingFee(-1);
        dataList.add(green);
        dataList.addAll(data);
        for (JsonShopRentalProductGet.DataList item : data) {
            propertyPicIdList.remove(String.valueOf(item.getPdPicId()));
        }
    }

    private int getIconRes(int pdPicId) {
        int res = R.drawable.icon_shops_caddie;
        switch (pdPicId) {

            case 1:
                res = R.drawable.icon_shops_caddie;
                break;
            case 2:
                res = R.drawable.icon_shops_cart;
                break;
            case 3:
                res = R.drawable.icon_shops_clubs;
                break;
            case 4:
                res = R.drawable.icon_shops_shoes;
                break;
            case 5:
                res = R.drawable.icon_shops_trolley;
                break;
            case 6:
                res = R.drawable.icon_shops_umbrella;
                break;
            case 7:
                res = R.drawable.icon_shops_towel;
                break;

            default:
                break;
        }

        return res;

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
            ListViewItem item;
            if (convertView == null) {
                ListView.LayoutParams itemParams = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, getActualWidthOnThisDevice(100));
                item = new ListViewItem(getBaseActivity());
                item.setPadding(getActualWidthOnThisDevice(40), 0, getActualWidthOnThisDevice(40), 0);
                item.setLayoutParams(itemParams);
//                item.setBackgroundColor(getColor(R.color.common_white));
                item.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
            } else {
                item = (ListViewItem) convertView;
            }

            if (position < 2) {
                item.getIcon().setVisibility(View.GONE);
            } else {
                item.getIcon().setBackgroundResource(getIconRes(dataList.get(position).getPdPicId()));
            }

            item.getTvPdName().setText(dataList.get(position).getProductName());


            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    if (position == 0) {
                        bundle.putString(TransKey.SHOP_RENTAL_SHOP_DATA, rentalShopData);
                        push(ShopsReserveFeeFragment.class, bundle);
                    } else if (position == 1) {
                        bundle.putString(TransKey.SHOP_RENTAL_SHOP_DATA, rentalShopData);
                        push(ShopsGreenEditFragment.class, bundle);
                    } else if (position == 2) {
                        bundle.putString(TransKey.SHOPS_CADDIE_ID, dataList.get(position).getPdId());
                        push(ShopsCaddieEditFragment.class, bundle);
                    } else {
                        bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeBrowse.value());
                        bundle.putString(TransKey.SHOPS_RENTAL_SHOP_DATA, rentalShopData);
                        bundle.putStringArrayList(TransKey.SHOPS_PROPERTY_PIC_IDS, propertyPicIdList);
                        bundle.putString(TransKey.SHOPS_PRODUCT_ID, dataList.get(position).getPdId());
                        push(ShopsRentalEditFragment.class, bundle);
                    }
                }
            });
            return item;
        }
    }


    class ListViewItem extends RelativeLayout {

        ImageView icon;
        ImageView iconRightArrow;
        IteeTextView tvPdName;

        public ListViewItem(Context context) {
            super(context);
            initView(context);
        }

        public IteeTextView getTvPdName() {
            return tvPdName;
        }

        public ImageView getIcon() {
            return icon;
        }

        public void setIcon(ImageView icon) {
            this.icon = icon;
        }

        private void initView(Context context) {
            RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            iconParams.addRule(RelativeLayout.CENTER_VERTICAL);
            icon = new ImageView(context);
            icon.setLayoutParams(iconParams);
            this.addView(icon);
            icon.setId(View.generateViewId());
            RelativeLayout.LayoutParams tvPdNameParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            tvPdNameParams.addRule(RelativeLayout.RIGHT_OF, icon.getId());
            tvPdNameParams.addRule(RelativeLayout.CENTER_VERTICAL);
            tvPdName = new IteeTextView(context);
            tvPdName.setText(R.string.shop_setting_caddie);
            tvPdName.setTextColor(getResources().getColor(R.color.common_black));
            tvPdName.setTextSize(Constants.FONT_SIZE_NORMAL);
            tvPdName.setLayoutParams(tvPdNameParams);
            this.addView(tvPdName);

            RelativeLayout.LayoutParams iconRParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            iconRParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            iconRParams.addRule(RelativeLayout.CENTER_VERTICAL);
            iconRightArrow = new ImageView(getActivity());
            iconRightArrow.setLayoutParams(iconRParams);
            this.addView(iconRightArrow);
            iconRightArrow.setBackgroundResource(R.drawable.icon_black);

        }
    }
}
