/**
 * Project Name: itee
 * File Name:  ShopsProductAdapter.java
 * Package Name: cn.situne.itee.adapter
 * Date:   2015-04-22
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.jsonentity.JsonProduct;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:ShopsProductAdapter <br/>
 * Function: ShopsProductAdapter. <br/>
 * Date: 2015-04-22 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class ShopsProductAdapter extends BaseAdapter {

    private BaseFragment mBaseFragment;

    private ArrayList<JsonProduct.ProductData> productDataList = new ArrayList<>();


    public ShopsProductAdapter(BaseFragment mBaseFragment, ArrayList<JsonProduct.ProductData> productDataList) {
        this.mBaseFragment = mBaseFragment;
        this.productDataList = productDataList;
    }

    @Override
    public int getCount() {
        return productDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return productDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mBaseFragment.getActivity()).inflate(R.layout.item_of_shops_product, null);
            holder = new ViewHolder();
            holder.tvProductName = (IteeTextView) convertView.findViewById(R.id.tv_product_name);
            holder.tvProductPrice = (IteeTextView) convertView.findViewById(R.id.tv_product_price);

            holder.tvProductName.getLayoutParams().width = mBaseFragment.getActualWidthOnThisDevice(550);

            ListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mBaseFragment.getActualHeightOnThisDevice(100));
            convertView.setLayoutParams(layoutParams);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JsonProduct.ProductData productData = productDataList.get(position);

        if (StringUtils.isNotEmpty(productData.getQty()) && Integer.valueOf(productData.getQty()) > 0) {
            holder.tvProductName.setTextColor(mBaseFragment.getColor(R.color.common_black));
        } else {
            if (Constants.STR_FLAG_NO.equals(productData.getUnlimitedFlag())) {
                holder.tvProductName.setTextColor(mBaseFragment.getColor(R.color.common_red));
            } else {
                holder.tvProductName.setTextColor(mBaseFragment.getColor(R.color.common_black));
            }
        }

        holder.tvProductName.setText(productData.getName());
        holder.tvProductPrice.setText(
                AppUtils.addCurrencySymbol(productData.getPrice(), mBaseFragment.getActivity()));

        return convertView;
    }

    class ViewHolder {
        IteeTextView tvProductName;
        IteeTextView tvProductPrice;
    }
}
