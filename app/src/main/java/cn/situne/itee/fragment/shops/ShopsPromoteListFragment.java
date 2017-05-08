/**
 * Project Name: itee
 * File Name:	 ShopsPromoteListFragment.java
 * Package Name: cn.situne.itee.fragment.shops;
 * Date:		 2015-03-30
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.shops;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonPromote;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:ShopsPromoteListFragment <br/>
 * Function: Promote 列表 <br/>
 * UI:  05-5
 * Date: 2015-03-30 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */

public class ShopsPromoteListFragment extends BaseFragment {

    private RelativeLayout rlContainer;

    private PullToRefreshListView lvPromote;

    private PromoteTypeAdapter adapter;

    private ArrayList<JsonPromote.Promote> promoteArrayList;

    private View.OnClickListener listenerAdd;

    private int currentPage;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_shops_promote_list;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        promoteArrayList = new ArrayList<>();

        rlContainer = (RelativeLayout) rootView.findViewById(R.id.rl_container);
        rlContainer.setBackgroundColor(getColor(R.color.common_white));
        lvPromote = new PullToRefreshListView(getActivity());
        lvPromote.setBackgroundColor(getColor(R.color.common_white));
        lvPromote.getRefreshableView().setDividerHeight(0);
        lvPromote.getRefreshableView().setDivider(null);
        adapter = new PromoteTypeAdapter(getActivity(), promoteArrayList);
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {

        lvPromote.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getPromote(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getPromote(false);
            }
        });

        listenerAdd = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeAdd.value());
                push(ShopsPromoteEditFragment.class, bundle);
            }
        };

    }

    @Override
    protected void setLayoutOfControls() {

        RelativeLayout.LayoutParams lvPromoteLayoutParams = new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        lvPromote.setLayoutParams(lvPromoteLayoutParams);

        rlContainer.addView(lvPromote);
    }

    @Override
    protected void setPropertyOfControls() {
        lvPromote.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lvPromote.setMode(PullToRefreshBase.Mode.BOTH);
       // lvPromote.setBackgroundColor(getColor(R.color.common_light_gray));

        ILoadingLayout headerLayoutProxy = lvPromote.getLoadingLayoutProxy(true, false);
        ILoadingLayout footerLayoutProxy = lvPromote.getLoadingLayoutProxy(false, true);


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
        getTvLeftTitle().setText(R.string.title_promote_shop);
        getTvRight().setBackgroundResource(R.drawable.icon_common_add);
        getTvRight().setOnClickListener(listenerAdd);
    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        getPromote(true);
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
        params.put(ApiKey.SHOPS_PRODUCT_SETTING_STATUS, Constants.STR_1);

        HttpManager<JsonPromote> hh = new HttpManager<JsonPromote>(ShopsPromoteListFragment.this) {

            @Override
            public void onJsonSuccess(JsonPromote jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    if (isRefresh) {
                        promoteArrayList.clear();
                    }
                    if (jo.getPromoteList().size() > 0) {
                        currentPage = jo.getPage();
                    }
                    initData(jo.getPromoteList());
                    adapter = new PromoteTypeAdapter(getActivity(), promoteArrayList);
                    lvPromote.setAdapter(adapter);
                    lvPromote.onRefreshComplete();
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }

            }

            @Override
            public void onJsonError(VolleyError error) {
                lvPromote.onRefreshComplete();
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(getActivity(), R.string.msg_common_network_error);
            }
        };
        hh.startGet(this.getActivity(), ApiManager.HttpApi.ShopsPromoteGet, params);
    }

    private void initData(ArrayList<JsonPromote.Promote> dataList) {
        promoteArrayList.addAll(dataList);
    }

    class PromoteTypeAdapter extends BaseAdapter {

        private ArrayList<JsonPromote.Promote> dataList;
        private Context mContext;

        public PromoteTypeAdapter(Context mContext, ArrayList<JsonPromote.Promote> dataList) {
            this.mContext = mContext;
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            if (dataList != null) {
                return dataList.size();
            }
            return 0;
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
            ViewHolder vh;

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_row_shops_promote, parent, false);
                vh = new ViewHolder();

                vh.llContainer = (LinearLayout) convertView.findViewById(R.id.ll_container);
                vh.rlTop = (RelativeLayout) convertView.findViewById(R.id.rl_top_container);
                vh.rlBottom = (RelativeLayout) convertView.findViewById(R.id.rl_bottom_container);
                vh.tvProductName = (IteeTextView) convertView.findViewById(R.id.tv_product_name);
                vh.tvProductAttr = (IteeTextView) convertView.findViewById(R.id.tv_product_attr);
                vh.tvDatePeriod = (IteeTextView) convertView.findViewById(R.id.tv_date_period);
                vh.tvOriginalPrice = (IteeTextView) convertView.findViewById(R.id.tv_original_price);
                vh.tvPromotePrice = (IteeTextView) convertView.findViewById(R.id.tv_promote_price);

                formatControls(vh);

                LinearLayout resLayout = (LinearLayout) convertView;
                resLayout.addView(AppUtils.getSeparatorLine(ShopsPromoteListFragment.this));

                convertView.setBackgroundColor(Color.WHITE);

                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            final JsonPromote.Promote promote = dataList.get(position);

            if (StringUtils.isNotEmpty(promote.getQty()) && Integer.valueOf(promote.getQty()) > 0) {
                vh.tvProductName.setTextColor(getColor(R.color.common_black));
            } else {
                if (Constants.STR_FLAG_NO.equals(promote.getUnlimitedFlag())) {
                    vh.tvProductName.setTextColor(getColor(R.color.common_red));
                } else {
                    vh.tvProductName.setTextColor(getColor(R.color.common_black));
                }
            }

            vh.tvProductName.setText(promote.getPromoteName());
            if (Utils.isStringNotNullOrEmpty(promote.getProductAttr())) {
                vh.tvProductAttr.setText(Constants.STR_BRACKETS_START + promote.getProductAttr() + Constants.STR_BRACKETS_END);
            } else {
                vh.tvProductAttr.setText(Constants.STR_EMPTY);
            }

            String currentShowStartDate = DateUtils.getCurrentShowYearMonthDayFromApiDateStr(promote.getStartDate(), mContext);
            String currentShowEndDate = DateUtils.getCurrentShowYearMonthDayFromApiDateStr(promote.getEndDate(), mContext);
            vh.tvDatePeriod.setText(currentShowStartDate + Constants.STR_SPACE
                    + Constants.STR_SEPARATOR + Constants.STR_SEPARATOR + Constants.STR_SPACE + currentShowEndDate);
            vh.tvOriginalPrice.setText(AppUtils.getCurrentCurrency(getActivity()) + Constants.STR_SPACE + promote.getPrice());
            vh.tvPromotePrice.setText(AppUtils.getCurrentCurrency(getActivity()) + Constants.STR_SPACE + promote.getPromotePrice());

            vh.llContainer.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
                @Override
                public void noDoubleClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(TransKey.COMMON_PRODUCT, Utils.getStringFromObject(promote));
                    bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeBrowse.value());
                    push(ShopsPromoteEditFragment.class, bundle);
                }
            });

            return convertView;
        }

        private void formatControls(ViewHolder vh) {

            vh.tvProductName.setTextSize(Constants.FONT_SIZE_LARGER);
            vh.tvProductName.setGravity(Gravity.START | Gravity.BOTTOM);
            vh.tvProductAttr.setTextSize(Constants.FONT_SIZE_SMALLER);
            vh.tvProductAttr.setTextColor(getColor(R.color.common_gray));
            vh.tvProductAttr.setGravity(Gravity.START | Gravity.BOTTOM);
            vh.tvProductAttr.setPadding(0, 0, 0, 2);
            vh.tvDatePeriod.setTextColor(getColor(R.color.common_gray));
            vh.tvPromotePrice.setTextColor(Color.BLACK);
            vh.tvOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            LinearLayout.LayoutParams rlTopLayoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(60));
            vh.rlTop.setLayoutParams(rlTopLayoutParams);

            LinearLayout.LayoutParams rlBottomLayoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(60));
            vh.rlBottom.setLayoutParams(rlBottomLayoutParams);

            RelativeLayout.LayoutParams tvProductNameLayoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
            tvProductNameLayoutParams.leftMargin = getActualWidthOnThisDevice(40);
            tvProductNameLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
            vh.tvProductName.setLayoutParams(tvProductNameLayoutParams);

            RelativeLayout.LayoutParams tvProductAttrLayoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
            tvProductAttrLayoutParams.leftMargin = getActualWidthOnThisDevice(20);
            tvProductAttrLayoutParams.addRule(RelativeLayout.RIGHT_OF, vh.tvProductName.getId());
            vh.tvProductAttr.setLayoutParams(tvProductAttrLayoutParams);

            RelativeLayout.LayoutParams tvDatePeriodLayoutParams = new RelativeLayout.LayoutParams((int) (getScreenWidth() * 0.6), MATCH_PARENT);
            tvDatePeriodLayoutParams.leftMargin = getActualWidthOnThisDevice(40);
            tvDatePeriodLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
            vh.tvDatePeriod.setLayoutParams(tvDatePeriodLayoutParams);

            RelativeLayout.LayoutParams tvPromotePriceLayoutParams = (RelativeLayout.LayoutParams) vh.tvPromotePrice.getLayoutParams();
            tvPromotePriceLayoutParams.rightMargin = getActualWidthOnThisDevice(40);
            vh.tvPromotePrice.setLayoutParams(tvPromotePriceLayoutParams);

            vh.tvOriginalPrice.setPadding(getActualWidthOnThisDevice(40), 0, 0, 0);
        }

        class ViewHolder {
            LinearLayout llContainer;
            RelativeLayout rlTop;
            RelativeLayout rlBottom;
            IteeTextView tvProductName;
            IteeTextView tvProductAttr;
            IteeTextView tvDatePeriod;
            IteeTextView tvOriginalPrice;
            IteeTextView tvPromotePrice;
        }
    }
}
