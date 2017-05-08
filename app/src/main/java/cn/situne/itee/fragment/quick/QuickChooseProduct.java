package cn.situne.itee.fragment.quick;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.ShopsProduct;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.agents.AgentsPricingTableFragment;
import cn.situne.itee.fragment.customers.CustomersPricingTableDataFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonAgentsPricingListGet;
import cn.situne.itee.view.IteeTextView;

;

/**
 * Created by luochao on 9/18/15.
 */
public class QuickChooseProduct extends BaseFragment {


    private ListView listView;
    private ListAdapter listAdapter;
    private boolean isAgent;
    private String fromPage;

    private String pricingType;

    private List<JsonAgentsPricingListGet.PricingItem> dataSource;

    @Override
    protected int getFragmentId() {
        return R.layout.quick_choose_product;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        listView = (ListView) rootView.findViewById(R.id.listView);
        Bundle bundle = getArguments();
        if (bundle != null) {
            fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE, "");
            if (AgentsPricingTableFragment.class.getName().equals(fromPage)) {
                isAgent = true;
            }

            pricingType = bundle.getString(TransKey.PRICING_TYPE, Constants.PRICING_TYPE_1);

        }

        listAdapter = new ListAdapter();
        //  listAdapter.setAgentsPricingList(jo.getAgentsPricingList());
        listView.setAdapter(listAdapter);

        listView.setCacheColorHint(0);
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        listView.setDivider(null);
        listAdapter.notifyDataSetChanged();
        netLinkGetPricingList();
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        JsonAgentsPricingListGet.PricingItem data = dataSource.get(position);

                        ArrayList<String> productProducts = new ArrayList<>();

                        for (JsonAgentsPricingListGet.PricingData pricingData : data.getPricingDataList()) {
                            ShopsProduct shopsProduct = new ShopsProduct();
                            shopsProduct.setProductId(pricingData.getProductId());
                            shopsProduct.setAttrId(pricingData.getAttributeId());
                            shopsProduct.setProductName(pricingData.getProductName());
                            shopsProduct.setProductNumber(1);
                            shopsProduct.setPackageId(pricingData.getPackageId());
                            shopsProduct.setMemberDiscount(pricingData.getMemberDiscount());
                            shopsProduct.setMemberDiscountType(pricingData.getMemberDiscountType());
                            shopsProduct.setGuestDiscountType(pricingData.getGuestDiscountType());
                            shopsProduct.setGuestDiscount(pricingData.getGuestDiscount());
                            shopsProduct.setDiscountPrice(pricingData.getGuestPrice());
                            shopsProduct.setTypeTagId(pricingData.getProductShopId());
                            shopsProduct.setProductPrice(pricingData.getProductPrice());
                            if (!Constants.STR_0.equals(pricingData.getPackageId())) {
                                ArrayList<ShopsProduct.ShopsSubProduct> productList = new ArrayList<>();

                                for (JsonAgentsPricingListGet.PricingData packageItem : pricingData.getProductList()) {
                                    ShopsProduct.ShopsSubProduct shopsSubProduct = new ShopsProduct.ShopsSubProduct();
                                    shopsSubProduct.setProductPrice(packageItem.getProductPrice());

                                    shopsSubProduct.setProductName(packageItem.getProductName());
                                    //  shopsSubProduct.setId();  TODO
                                    shopsSubProduct.setProductAttr(packageItem.getAttributeId());
                                    shopsSubProduct.setProductId(packageItem.getProductId());

                                    shopsSubProduct.setMemberDiscount(pricingData.getMemberDiscount());
                                    shopsSubProduct.setMemberDiscountType(pricingData.getMemberDiscountType());
                                    shopsSubProduct.setGuestDiscountType(pricingData.getGuestDiscountType());
                                    shopsSubProduct.setGuestDiscount(pricingData.getGuestDiscount());
                                    productList.add(shopsSubProduct);
                                }

                                shopsProduct.setProductList(productList);

                            }
                            productProducts.add(Utils.getStringFromObject(shopsProduct));
                        }


                        //String returnObject = Utils.getStringFromObject(data);
                        Bundle bundle = new Bundle();
                        bundle.putString(TransKey.COMMON_FROM_PAGE, QuickChooseProduct.class.getName());
                        bundle.putStringArrayList(TransKey.CHOOSE_PRODUCT_LIST, productProducts);
                        if (isAgent) {
                            doBackWithReturnValue(bundle, AgentsPricingTableFragment.class);
                        } else {
                            doBackWithReturnValue(bundle, CustomersPricingTableDataFragment.class);
                        }
                    }
                }
        );
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
        getTvLeftTitle().setText(R.string.agents_choose_product);

    }


    class ListItemMember extends LinearLayout {

        private LinearLayout titleLayout;


        private IteeTextView tvTitleText;


        private LinearLayout bodyLayout;

        private View v;

        public View getTopView() {
            return topView;
        }

        private View topView;

        public ListItemMember(Context context, int position) {
            super(context);
            this.setOrientation(VERTICAL);

            LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
            titleLayout = new LinearLayout(getContext());
            titleLayout.setLayoutParams(titleLayoutParams);
            titleLayout.setOrientation(HORIZONTAL);

            LinearLayout.LayoutParams bodyLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            bodyLayout = new LinearLayout(getContext());
            bodyLayout.setLayoutParams(bodyLayoutParams);
            bodyLayout.setOrientation(VERTICAL);

            setTitleLayout();

            LinearLayout.LayoutParams vParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(20));
            topView = new View(getContext());
            topView.setLayoutParams(vParams);

            this.addView(topView);
            this.addView(AppUtils.getSeparatorLine(getContext()));
            this.addView(titleLayout);
            this.addView(bodyLayout);

//            titleLayout.setBackgroundColor(getColor(R.color.common_white));
//            bodyLayout.setBackgroundColor(getColor(R.color.common_white));
            titleLayout.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
            bodyLayout.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
        }

        private void setTitleLayout() {
            LinearLayout.LayoutParams rlTitleParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
            RelativeLayout rlTitle = new RelativeLayout(getContext());
            rlTitle.setLayoutParams(rlTitleParams);
            tvTitleText = new IteeTextView(getContext());
            rlTitle.addView(tvTitleText);
            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvTitleText, getActualWidthOnThisDevice(40), getContext());
            AppUtils.addBottomSeparatorLine(rlTitle, getContext());
            titleLayout.addView(rlTitle);

        }


        private RelativeLayout getProductItem(JsonAgentsPricingListGet.PricingData dataSource, String key) {

            LinearLayout.LayoutParams rlMemberParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(50));
            RelativeLayout item = new RelativeLayout(getContext());
            item.setBackgroundColor(getResources().getColor(R.color.common_white_gray));
            item.setLayoutParams(rlMemberParams);

            IteeTextView tvMember = new IteeTextView(getContext());
            tvMember.setText(key);
            tvMember.setId(View.generateViewId());
            item.addView(tvMember);

            IteeTextView tvMoney = new IteeTextView(getContext());
            item.addView(tvMoney);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvMember.getLayoutParams();
            layoutParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            layoutParams.leftMargin = getActualWidthOnThisDevice(40);

            LayoutUtils.setCellRightValueViewOfRelativeLayout(tvMoney, getActualWidthOnThisDevice(40), getContext());
            RelativeLayout.LayoutParams tvMemberParams = (RelativeLayout.LayoutParams) tvMember.getLayoutParams();
            tvMemberParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;

            RelativeLayout.LayoutParams tvRateParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            tvRateParams.leftMargin = getActualWidthOnThisDevice(5);
            tvRateParams.addRule(RelativeLayout.RIGHT_OF, tvMember.getId());
            IteeTextView tvRate = new IteeTextView(getContext());
            tvRate.setLayoutParams(tvRateParams);

            tvRate.setTextColor(getColor(R.color.common_red));


            if (key.equals(getString(R.string.customers_member))) {
                tvMoney.setText(dataSource.getMemberPrice());
                if (Constants.STR_1.equals(dataSource.getMemberDiscountType())) {
                    tvRate.setText(Constants.STR_DOUBLE_SPACE + dataSource.getMemberDiscount() + Constants.STR_PER_CENT + Constants.STR_OFF);
                } else {
                    tvRate.setText(Constants.STR_DOUBLE_SPACE + AppUtils.getCurrentCurrency(mContext) + Constants.STR_SEPARATOR_SPACE + dataSource.getMemberDiscount() + Constants.STR_SPACE + Constants.STR_OFF);
                }

            } else if (key.equals(getString(R.string.customers_guest))) {
                tvMoney.setText(dataSource.getGuestPrice());
                if (Constants.STR_1.equals(dataSource.getGuestDiscountType())) {
                    tvRate.setText(Constants.STR_DOUBLE_SPACE + dataSource.getGuestDiscount() + Constants.STR_PER_CENT + Constants.STR_OFF);
                } else {
                    tvRate.setText(Constants.STR_DOUBLE_SPACE + AppUtils.getCurrentCurrency(mContext) + Constants.STR_SEPARATOR_SPACE + dataSource.getGuestDiscount() + Constants.STR_SPACE + Constants.STR_OFF);
                }
            }

            item.addView(tvRate);
            return item;

        }


        private RelativeLayout getPackageItem(JsonAgentsPricingListGet.PricingData productData) {
            LinearLayout.LayoutParams rlMemberParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(50));
            RelativeLayout item = new RelativeLayout(getContext());
            item.setLayoutParams(rlMemberParams);
            IteeTextView tvMember = new IteeTextView(getContext());
            tvMember.setText(productData.getProductName());
            tvMember.setId(View.generateViewId());
            item.addView(tvMember);

            IteeTextView tvMoney = new IteeTextView(getContext());
            item.addView(tvMoney);
            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvMember, getActualWidthOnThisDevice(40), getContext());
            LayoutUtils.setCellRightValueViewOfRelativeLayout(tvMoney, getActualWidthOnThisDevice(40), getContext());
            RelativeLayout.LayoutParams tvMemberParams = (RelativeLayout.LayoutParams) tvMember.getLayoutParams();
            tvMemberParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;

            RelativeLayout.LayoutParams tvRateParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            tvRateParams.leftMargin = getActualWidthOnThisDevice(5);
            tvRateParams.addRule(RelativeLayout.RIGHT_OF, tvMember.getId());
            IteeTextView tvRate = new IteeTextView(getContext());
            tvRate.setLayoutParams(tvRateParams);

            tvRate.setTextColor(getColor(R.color.common_red));
            item.addView(tvRate);
            return item;

        }


        private RelativeLayout getAgentPackageItem(JsonAgentsPricingListGet.PricingData productData) {
            LinearLayout.LayoutParams rlMemberParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(50));

            RelativeLayout item = new RelativeLayout(getContext());
            item.setLayoutParams(rlMemberParams);
            IteeTextView tvMember = new IteeTextView(getContext());
            tvMember.setText(productData.getProductName());
            tvMember.setId(View.generateViewId());
            item.addView(tvMember);

            IteeTextView tvMoney = new IteeTextView(getContext());
            item.addView(tvMoney);
            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvMember, getActualWidthOnThisDevice(40), getContext());
            LayoutUtils.setCellRightValueViewOfRelativeLayout(tvMoney, getActualWidthOnThisDevice(40), getContext());
            RelativeLayout.LayoutParams tvMemberParams = (RelativeLayout.LayoutParams) tvMember.getLayoutParams();
            tvMemberParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;

            RelativeLayout.LayoutParams tvRateParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            tvRateParams.leftMargin = getActualWidthOnThisDevice(5);
            tvRateParams.addRule(RelativeLayout.RIGHT_OF, tvMember.getId());
            IteeTextView tvRate = new IteeTextView(getContext());
            tvRate.setLayoutParams(tvRateParams);

            tvRate.setTextColor(getColor(R.color.common_red));
            tvMoney.setText(AppUtils.getCurrentCurrency(getContext()) + productData.getGuestPrice());

            if (Constants.STR_1.equals(productData.getGuestDiscountType())) {
                tvRate.setText(Constants.STR_DOUBLE_SPACE + productData.getGuestDiscount() + Constants.STR_PER_CENT + Constants.STR_OFF);
            } else {
                tvRate.setText(Constants.STR_DOUBLE_SPACE + AppUtils.getCurrentCurrency(mContext) + Constants.STR_SEPARATOR_SPACE + productData.getGuestDiscount() + Constants.STR_SPACE + Constants.STR_OFF);
            }

            item.addView(tvRate);
            return item;

        }


        private LinearLayout getProduct(JsonAgentsPricingListGet.PricingData dataSource) {

            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setLayoutParams(linearLayoutParams);
            linearLayout.setOrientation(VERTICAL);
            LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, getActualWidthOnThisDevice(60));

            RelativeLayout title = new RelativeLayout(getContext());
            title.setLayoutParams(titleParams);
            IteeTextView tvProductName = new IteeTextView(getContext());
            title.addView(tvProductName);
            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvProductName, getActualWidthOnThisDevice(40), getContext());

            LinearLayout.LayoutParams bMemberParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, getActualWidthOnThisDevice(50));
            LinearLayout.LayoutParams bGuestParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, getActualWidthOnThisDevice(50));

            RelativeLayout bMember = new RelativeLayout(getContext());
            bMember.setLayoutParams(bMemberParams);
            RelativeLayout bGuest = new RelativeLayout(getContext());
            bGuest.setLayoutParams(bGuestParams);


            linearLayout.addView(title);
            linearLayout.addView(getProductItem(dataSource, getString(R.string.customers_member)));
            linearLayout.addView(getProductItem(dataSource, getString(R.string.customers_guest)));
            linearLayout.addView(AppUtils.getSeparatorLine(getContext()));
            tvProductName.setText(dataSource.getProductName());
            return linearLayout;
        }


        private LinearLayout getAgentProduct(JsonAgentsPricingListGet.PricingData dataSource) {
            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setLayoutParams(linearLayoutParams);
            linearLayout.setOrientation(VERTICAL);
            LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, getActualWidthOnThisDevice(50));

            RelativeLayout title = new RelativeLayout(getContext());
            title.setLayoutParams(titleParams);
            IteeTextView tvProductName = new IteeTextView(getContext());
            tvProductName.setId(View.generateViewId());
            IteeTextView tvOFF = new IteeTextView(getContext());
            IteeTextView tvProductValue = new IteeTextView(getContext());
            title.addView(tvProductName);
            title.addView(tvOFF);
            title.addView(tvProductValue);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvProductName.getLayoutParams();
            layoutParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            layoutParams.leftMargin = getActualWidthOnThisDevice(40);

            LayoutUtils.setCellRightValueViewOfRelativeLayout(tvProductValue, getActualWidthOnThisDevice(40), getContext());


            RelativeLayout.LayoutParams tvRateParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            tvRateParams.leftMargin = getActualWidthOnThisDevice(5);
            tvRateParams.addRule(RelativeLayout.RIGHT_OF, tvProductName.getId());
            tvOFF.setLayoutParams(tvRateParams);
            linearLayout.addView(title);

            tvOFF.setTextColor(getColor(R.color.common_red));

            tvProductName.setText(dataSource.getProductName());
            tvProductValue.setText(AppUtils.getCurrentCurrency(getContext()) + dataSource.getGuestPrice());
            if (Constants.STR_1.equals(dataSource.getGuestDiscountType())) {
                tvOFF.setText(Constants.STR_DOUBLE_SPACE + dataSource.getGuestDiscount() + Constants.STR_PER_CENT + Constants.STR_OFF);
            } else {
                tvOFF.setText(Constants.STR_DOUBLE_SPACE + AppUtils.getCurrentCurrency(mContext) + Constants.STR_SEPARATOR_SPACE + dataSource.getGuestDiscount() + Constants.STR_SPACE + Constants.STR_OFF);
            }
            AppUtils.addBottomSeparatorLine(title, getContext());

            return linearLayout;
        }

        private LinearLayout getPackage(JsonAgentsPricingListGet.PricingData dataSource) {


            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setLayoutParams(linearLayoutParams);
            linearLayout.setOrientation(VERTICAL);
            LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, getActualWidthOnThisDevice(50));

            RelativeLayout title = new RelativeLayout(getContext());
            title.setLayoutParams(titleParams);
            IteeTextView tvProductName = new IteeTextView(getContext());
            IteeTextView tvProductValue = new IteeTextView(getContext());
            title.addView(tvProductName);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvProductName.getLayoutParams();
            layoutParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            layoutParams.leftMargin = getActualWidthOnThisDevice(40);

            if (isAgent) {
                title.addView(tvProductValue);
                LayoutUtils.setCellRightValueViewOfRelativeLayout(tvProductValue, getActualWidthOnThisDevice(40), getContext());
                tvProductValue.setText(AppUtils.getCurrentCurrency(mContext) + dataSource.getGuestPrice());
            }
            RelativeLayout.LayoutParams tvMemberParams = (RelativeLayout.LayoutParams) tvProductName.getLayoutParams();
            tvMemberParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;

            LinearLayout.LayoutParams bMemberParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, getActualWidthOnThisDevice(50));
            LinearLayout.LayoutParams bGuestParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, getActualWidthOnThisDevice(50));

            RelativeLayout bMember = new RelativeLayout(getContext());
            bMember.setLayoutParams(bMemberParams);
            RelativeLayout bGuest = new RelativeLayout(getContext());
            bGuest.setLayoutParams(bGuestParams);


            linearLayout.addView(title);


            if (dataSource.getProductList() != null) {
                for (int i = 0; i < dataSource.getProductList().size(); i++) {
                    JsonAgentsPricingListGet.PricingData productData = dataSource.getProductList().get(i);
                    if (isAgent) {
                        linearLayout.addView(getAgentPackageItem(productData));
                    } else {

                        linearLayout.addView(getPackageItem(productData));
                    }
                }
            }

            linearLayout.addView(AppUtils.getSeparatorLine(mContext));
            tvProductName.setText(dataSource.getProductName());
            return linearLayout;
        }

        private void addBodyLayout(JsonAgentsPricingListGet.PricingItem data) {

            bodyLayout.removeAllViews();

            ArrayList<JsonAgentsPricingListGet.PricingData> dataAll = data.getPricingDataList();

            for (int i = 0; i < dataAll.size(); i++) {
                JsonAgentsPricingListGet.PricingData dataItem = dataAll.get(i);

                if (isAgent) {
                    //is package.
                    if (!Constants.STR_0.equals(dataItem.getPackageId())) {
                        bodyLayout.addView(getPackage(dataItem));
                    } else {
                        bodyLayout.addView(getAgentProduct(dataItem));
                    }
                } else {
                    //is package.
                    if (!Constants.STR_0.equals(dataItem.getPackageId())) {
                        bodyLayout.addView(getPackage(dataItem));
                    } else {
                        bodyLayout.addView(getProduct(dataItem));
                    }
                }


            }

        }

        public void refreshLayout(JsonAgentsPricingListGet.PricingItem data, int position) {

            tvTitleText.setText(getString(R.string.pricing_group) + Constants.STR_SPACE + (position + 1));
            //  delBtn.setTag(data.getMainId());
            addBodyLayout(data);
        }

        private void setItemListener(OnClickListener selectListener, OnClickListener delListener) {

            titleLayout.setOnClickListener(selectListener);
            this.setOnClickListener(selectListener);
        }
    }


    class ListAdapter extends BaseAdapter {


        private List<JsonAgentsPricingListGet.PricingItem> agentsPricingList;

        public List<JsonAgentsPricingListGet.PricingItem> getAgentsPricingList() {
            return agentsPricingList;
        }

        public void setAgentsPricingList(List<JsonAgentsPricingListGet.PricingItem> agentsPricingList) {
            this.agentsPricingList = agentsPricingList;
        }

        @Override
        public int getCount() {
            if (agentsPricingList == null)
                return 0;
            return agentsPricingList.size();
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

            ListItemMember listItem = null;
            if (view == null) {

                listItem = new ListItemMember(getBaseActivity(), i);
            } else {

                listItem = (ListItemMember) view;
            }
            if (i == 0) {
                listItem.getTopView().setVisibility(View.GONE);
            } else {
                listItem.getTopView().setVisibility(View.VISIBLE);
            }

            if (agentsPricingList != null) {
                listItem.setTag(agentsPricingList.get(i).getMainId());
                listItem.refreshLayout(agentsPricingList.get(i), i);
            }
            return listItem;
        }

    }


    private void netLinkGetPricingList() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.PRICING_TYPE, pricingType);
        HttpManager<JsonAgentsPricingListGet> hh = new HttpManager<JsonAgentsPricingListGet>(QuickChooseProduct.this) {
            @Override
            public void onJsonSuccess(JsonAgentsPricingListGet jo) {
                listAdapter = new ListAdapter();
                dataSource = jo.getAgentsPricingList();
                listAdapter.setAgentsPricingList(jo.getAgentsPricingList());
                listView.setAdapter(listAdapter);

                listAdapter.notifyDataSetChanged();
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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.PricingListPageGetX, params);
    }
}
