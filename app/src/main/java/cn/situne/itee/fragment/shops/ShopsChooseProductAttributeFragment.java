/**
 * Project Name: itee
 * File Name:	 ShopsChooseProductAttributeFragment.java
 * Package Name: cn.situne.itee.fragment.shops
 * Date:		 2015-03-26
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.fragment.shops;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
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
import cn.situne.itee.manager.jsonentity.JsonProductProperty;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:ShopsChooseProductAttributeFragment <br/>
 * Function: choose attribute of product. <br/>
 * Date: 2015-03-26 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
@SuppressWarnings("unchecked")
public class ShopsChooseProductAttributeFragment extends BaseFragment {

    private String productId;
    private String productName;

    private IteeTextView tvProductName;
    private IteeTextView tvProductSelected;

    private ScrollView svProperties;
    private RelativeLayout rlTopContainer;
    private LinearLayout llContainer;

    private ArrayList<JsonProductProperty.ProductTypeDetail> dataList;
//    private ArrayList<PropertyItemLayout> propertyItemLayouts;//view

    private ArrayList<PropertyItemLayout> allPropertyItemLayouts;//view

    private AppUtils.NoDoubleClickListener listenerOk;

    private ShopsChooseProductFragment.ChooseProductMode mode;

    private ArrayList<ShopsProduct> currentSelectedAttrList;
    private ArrayList<String> alreadySelectedAttrIdList;

    private String fromPage;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_shops_choose_product_attribute;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
//        propertyItemLayouts = new ArrayList<>();
        allPropertyItemLayouts = new ArrayList<>();

        currentSelectedAttrList = new ArrayList<>();

        rlTopContainer = (RelativeLayout) rootView.findViewById(R.id.rl_top_container);
        llContainer = (LinearLayout) rootView.findViewById(R.id.ll_container);

        svProperties = (ScrollView) rootView.findViewById(R.id.sv_properties);

        tvProductName = new IteeTextView(this);
        tvProductSelected = new IteeTextView(this);

        Bundle bundle = getArguments();
        if (bundle != null) {
            alreadySelectedAttrIdList = bundle.getStringArrayList(TransKey.CHOOSE_PRODUCT_LIST);
        }
    }

    @Override
    protected void setDefaultValueOfControls() {
        tvProductName.setText(productName);
    }

    @Override
    protected void setListenersOfControls() {

        rlTopContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode != ShopsChooseProductFragment.ChooseProductMode.ModePricingTable) {
                    boolean isSelected = !rlTopContainer.isSelected();
                    if (isSelected) {
                        tvProductSelected.setText(Constants.STR_ITEM_SELECTED);
                    } else {
                        tvProductSelected.setText(Constants.STR_EMPTY);
                    }
                    rlTopContainer.setSelected(isSelected);
                    for (PropertyItemLayout itemLayout : allPropertyItemLayouts) {
                        itemLayout.getProductTypeDetail().setSelected(isSelected);
                        itemLayout.setCheckMark(isSelected);
                    }
                }
            }
        });

        listenerOk = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                getSelectedProduct();
                if (doCheck()) {
                    ArrayList<String> selectedProductList = new ArrayList<>();
                    for (ShopsProduct shopsProduct : currentSelectedAttrList) {
                        selectedProductList.add(Utils.getStringFromObject(shopsProduct));
                    }

                    try {
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList(TransKey.CHOOSE_PRODUCT_LIST, selectedProductList);
                        bundle.putString(TransKey.COMMON_PRODUCT_NAME, productName);
                        doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
                        doBack();
                    } catch (ClassNotFoundException e) {
                        Utils.log(e.getMessage());
                    }
                }
            }
        };
    }

    @Override
    protected void setLayoutOfControls() {

        RelativeLayout.LayoutParams rlTopContainerLayoutParams = new RelativeLayout.LayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(100));
        rlTopContainer.setLayoutParams(rlTopContainerLayoutParams);

        RelativeLayout.LayoutParams tvProductNameLayoutParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(200), MATCH_PARENT);
        tvProductNameLayoutParams.leftMargin = getActualWidthOnThisDevice(40);
        tvProductNameLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        tvProductName.setLayoutParams(tvProductNameLayoutParams);

        rlTopContainer.addView(tvProductName);

        RelativeLayout.LayoutParams svPropertiesLayoutParams = new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        svPropertiesLayoutParams.addRule(RelativeLayout.BELOW, rlTopContainer.getId());
        svPropertiesLayoutParams.topMargin = getActualHeightOnThisDevice(20);
        svProperties.setLayoutParams(svPropertiesLayoutParams);

        RelativeLayout.LayoutParams tvProductSelectedLayoutParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(200), MATCH_PARENT);
        tvProductSelectedLayoutParams.rightMargin = getActualWidthOnThisDevice(40);
        tvProductSelectedLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        tvProductSelected.setLayoutParams(tvProductSelectedLayoutParams);

        rlTopContainer.addView(tvProductSelected);
    }

    @Override
    protected void setPropertyOfControls() {

        tvProductSelected.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);

        rlTopContainer.setBackgroundColor(getColor(R.color.common_white));
        AppUtils.addBottomSeparatorLine(rlTopContainer, this);
    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        getSubClass();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            productId = bundle.getString(TransKey.COMMON_PRODUCT_ID);
            productName = bundle.getString(TransKey.COMMON_PRODUCT_NAME);
            mode = ShopsChooseProductFragment.ChooseProductMode.valueOf(bundle.getInt(TransKey.CHOOSE_MODE, 0));
            fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private boolean doCheck() {
        boolean res = true;
        if (mode == ShopsChooseProductFragment.ChooseProductMode.ModePackage) {
            if (currentSelectedAttrList.size() < 1) {
                res = false;
                Utils.showShortToast(getActivity(), "must choose");
            } else if (currentSelectedAttrList.size() > 1) {
                String parentId = currentSelectedAttrList.get(0).getParentId();
                for (ShopsProduct shopsProduct : currentSelectedAttrList) {
                    if (!shopsProduct.getParentId().equals(parentId)) {
                        res = false;
                        Utils.showShortToast(getActivity(), "must be same parent!");
                        break;
                    }
                }
            }
        } else if (mode == ShopsChooseProductFragment.ChooseProductMode.ModePricingTable) {
            if (currentSelectedAttrList.size() != 1) {
                res = false;
                Utils.showShortToast(getActivity(), "must be 1");
            }
        } else if (mode == ShopsChooseProductFragment.ChooseProductMode.ModePromote) {
            if (currentSelectedAttrList.size() == 0) {
                res = false;
                Utils.showShortToast(getActivity(), "must > 1");
            }
        }
        return res;
    }

    private void getSelectedProduct() {
        for (PropertyItemLayout propertyItemLayout : allPropertyItemLayouts) {
            JsonProductProperty.ProductTypeDetail productTypeDetail = propertyItemLayout.getProductTypeDetail();
            if (productTypeDetail.isSelected()) {
                ShopsProduct shopsProduct = new ShopsProduct();
                shopsProduct.setProductId(productId);
                shopsProduct.setParentId(String.valueOf(productTypeDetail.getPraParentId()));
                shopsProduct.setAttrId(String.valueOf(productTypeDetail.getPraId()));
                shopsProduct.setProductName(productName + Constants.STR_BRACKETS_START + productTypeDetail.getPraName() + Constants.STR_BRACKETS_END);
                shopsProduct.setProductPrice(productTypeDetail.getPrice());
                shopsProduct.setQty(productTypeDetail.getQty());
                currentSelectedAttrList.add(shopsProduct);
            }
        }
    }

    private void initSubclassView() {
        for (JsonProductProperty.ProductTypeDetail productTypeDetail : dataList) {
            addSubclassView(productTypeDetail);
        }
    }

    private void addSubclassView(JsonProductProperty.ProductTypeDetail productTypeDetail) {
        LinearLayout itemLayout = new LinearLayout(getBaseActivity());
        itemLayout.setBackgroundColor(getColor(R.color.common_white));
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        itemLayout.addView(AppUtils.getSeparatorLine(ShopsChooseProductAttributeFragment.this));
        LinearLayout.LayoutParams itemBoxParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout itemBox = new RelativeLayout(getBaseActivity());
        itemBox.setLayoutParams(itemBoxParams);
        PropertyItemLayout item = new PropertyItemLayout(ShopsChooseProductAttributeFragment.this, 1, getActualHeightOnThisDevice(100), productTypeDetail, allPropertyItemLayouts);
//        item.setParentId(null);

        itemBox.addView(item);
        AppUtils.addBottomSeparatorLine(itemBox, ShopsChooseProductAttributeFragment.this);
        itemLayout.addView(itemBox);

        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(20));
        View view = new View(getBaseActivity());
        view.setBackgroundColor(getColor(R.color.common_light_gray));
        view.setLayoutParams(viewParams);
        itemLayout.addView(view);

        llContainer.addView(itemLayout);
        item.setContainers(itemLayout);
//        propertyItemLayouts.add(item);
        item.refreshView();
    }

    private void doSelectProduct(ArrayList<JsonProductProperty.ProductTypeDetail> dataList, boolean isSelected) {
        for (JsonProductProperty.ProductTypeDetail productTypeDetail : dataList) {
            productTypeDetail.setSelected(isSelected);
            if (Utils.isListNotNullOrEmpty(productTypeDetail.getItems())) {
                doSelectProduct(productTypeDetail.getItems(), isSelected);
            }
        }
    }

    private void doSetCheckMark(ArrayList<PropertyItemLayout> itemLayouts, boolean isSelected) {
        for (PropertyItemLayout item : itemLayouts) {
            item.setCheckMark(isSelected);
            if (Utils.isListNotNullOrEmpty(item.getChildItems())) {
                doSetCheckMark(item.getChildItems(), isSelected);
            }
        }
    }

    private void getSubClass() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_PRODUCT_ID, productId);
        HttpManager<JsonProductProperty> hh = new HttpManager<JsonProductProperty>(ShopsChooseProductAttributeFragment.this) {

            @Override
            public void onJsonSuccess(JsonProductProperty jo) {
                Utils.log("123123123123");
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    dataList = jo.getDataList();
                    initSubclassView();
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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.ShopsProductPropertyGet, params);

    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getTvLeftTitle().getLayoutParams();
        layoutParams.width = getActualWidthOnThisDevice(500);
        getTvLeftTitle().setLayoutParams(layoutParams);

        getTvLeftTitle().setText(R.string.shop_setting_choose_product_subcategories);

        getTvRight().setText(R.string.common_ok);
        getTvRight().setOnClickListener(listenerOk);
    }

    public void setFromPage(String fromPage) {
        this.fromPage = fromPage;
    }

    public void setMode(ShopsChooseProductFragment.ChooseProductMode mode) {
        this.mode = mode;
    }

    class PropertyItemLayout extends LinearLayout {

        private IteeTextView tvName;
        private IteeTextView tvSelected;

        private ArrayList<PropertyItemLayout> mAllPropertyItemLayouts;
        private JsonProductProperty.ProductTypeDetail productTypeDetail;
        private View containers;

        private int mLevel;
        private ArrayList<PropertyItemLayout> childItems;
        private BaseFragment mFragment;

        public void setContainers(View containers) {
            this.containers = containers;
            this.containers.setTag(this);
        }

        public ArrayList<PropertyItemLayout> getChildItems() {
            return childItems;
        }


        public PropertyItemLayout(BaseFragment fragment, int level, int height, JsonProductProperty.ProductTypeDetail productTypeDetail, final ArrayList<PropertyItemLayout> allPropertyItemLayouts) {
            super(fragment.getBaseActivity());
            setOrientation(VERTICAL);
            mLevel = level;
            mFragment = fragment;
            this.productTypeDetail = productTypeDetail;
            mAllPropertyItemLayouts = allPropertyItemLayouts;

            childItems = new ArrayList<>();
            RelativeLayout.LayoutParams myParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            this.setLayoutParams(myParams);

            RelativeLayout.LayoutParams rlContainerParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);

            RelativeLayout rlContainer = new RelativeLayout(fragment.getBaseActivity());
            rlContainer.setLayoutParams(rlContainerParams);
            rlContainer.setPadding(mFragment.getActualWidthOnThisDevice(0), 0, mFragment.getActualWidthOnThisDevice(0), 0);
            switch (level) {
                case 2:
                    rlContainer.setPadding(mFragment.getActualWidthOnThisDevice(80), 0, mFragment.getActualWidthOnThisDevice(0), 0);
                    break;
                case 3:
                    rlContainer.setPadding(mFragment.getActualWidthOnThisDevice(120), 0, mFragment.getActualWidthOnThisDevice(0), 0);
                    break;
                default:
                    break;
            }

            RelativeLayout.LayoutParams tvNameParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            tvNameParams.leftMargin = mFragment.getActualWidthOnThisDevice(40);

            tvNameParams.addRule(RelativeLayout.CENTER_VERTICAL);
            tvName = new IteeTextView(mFragment);
            tvName.setLayoutParams(tvNameParams);
            tvName.setMinWidth(mFragment.getActualWidthOnThisDevice(100));
            rlContainer.addView(tvName);


            tvSelected = new IteeTextView(mFragment);

            RelativeLayout.LayoutParams tvSelectedLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            tvSelectedLayoutParams.rightMargin = mFragment.getActualWidthOnThisDevice(40);
            tvSelectedLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            tvSelectedLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

            tvSelected.setLayoutParams(tvSelectedLayoutParams);
            tvSelected.setMinWidth(mFragment.getActualWidthOnThisDevice(100));
            tvSelected.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);

            rlContainer.addView(tvSelected);

            AppUtils.addBottomSeparatorLine(rlContainer, fragment);
            this.addView(rlContainer);

            View.OnClickListener listener = new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    PropertyItemLayout item = (PropertyItemLayout) v;
                    boolean isSelected = !item.getProductTypeDetail().isSelected();

                    if (mode == ShopsChooseProductFragment.ChooseProductMode.ModePackage) {
                        doSelectProduct(item.getProductTypeDetail().getItems(), isSelected);
                        item.getProductTypeDetail().setSelected(isSelected);

                        item.setCheckMark(isSelected);
                        doSetCheckMark(item.getChildItems(), isSelected);
                    } else if (mode == ShopsChooseProductFragment.ChooseProductMode.ModePricingTable) {
                        doSelectProduct(dataList, false);
                        doSetCheckMark(allPropertyItemLayouts, false);
                        item.getProductTypeDetail().setSelected(isSelected);
                        item.setCheckMark(isSelected);
                    } else {
                        doSelectProduct(item.getProductTypeDetail().getItems(), isSelected);
                        item.getProductTypeDetail().setSelected(isSelected);

                        item.setCheckMark(isSelected);
                        doSetCheckMark(item.getChildItems(), isSelected);
                    }


                }
            };
            if (mode == ShopsChooseProductFragment.ChooseProductMode.ModePromote) {
                setOnClickListener(listener);
            } else {
                if (productTypeDetail.getPraPriceStatus() == 0) {
                    setBackgroundColor(getColor(R.color.common_deep_gray));
                    setOnClickListener(null);
                } else {
                    if (Utils.isListNotNullOrEmpty(alreadySelectedAttrIdList) && alreadySelectedAttrIdList.contains(productTypeDetail.getPraId())) {
                        setBackgroundColor(getColor(R.color.common_deep_gray));
                        setOnClickListener(null);
                    } else {
                        setOnClickListener(listener);
                    }
                }
            }


        }

        public void refreshView() {
            if (productTypeDetail != null) {
                tvName.setText(productTypeDetail.getPraName());
                initChildView(productTypeDetail.getItems());
            }
            mAllPropertyItemLayouts.add(this);
        }

        private void initChildView(ArrayList<JsonProductProperty.ProductTypeDetail> dataList) {
            if (dataList != null && dataList.size() > 0) {
                for (int i = 0; i < dataList.size(); i++) {
                    JsonProductProperty.ProductTypeDetail data = dataList.get(i);
                    PropertyItemLayout item = new PropertyItemLayout(ShopsChooseProductAttributeFragment.this, mLevel + 1, mFragment.getActualHeightOnThisDevice(100), data, mAllPropertyItemLayouts);
//                    item.setParentId(productTypeDetail.getPraId());
                    item.setContainers(this);
                    PropertyItemLayout.this.addView(item);
                    childItems.add(item);
                    item.refreshView();
                }
            }


        }

        public void setCheckMark(boolean isSelected) {
            if (isSelected) {
                tvSelected.setText(Constants.STR_ITEM_SELECTED);
            } else {
                tvSelected.setText(Constants.STR_EMPTY);
            }
        }

        public JsonProductProperty.ProductTypeDetail getProductTypeDetail() {
            return productTypeDetail;
        }
    }


}
