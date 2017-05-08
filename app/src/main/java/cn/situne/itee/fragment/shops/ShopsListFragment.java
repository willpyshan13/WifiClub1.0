/**
 * Project Name: itee
 * File Name:	 ShopsListFragment.java
 * Package Name: cn.situne.itee.fragment.shops
 * Date:		 2015-03-28
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.shops;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonProductType;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.SwipeLinearLayout;

/**
 * ClassName:ShopsListFragment <br/>
 * Function: show shops list. <br/>
 * Date: 2015-03-28 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class ShopsListFragment extends BaseFragment {
    private LinearLayout llContainer;
    private ArrayList<SwipeLinearLayout> swipeLinearLayoutList;
    private AppUtils.NoDoubleClickListener listenerAdd;
    private String fromPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_shops_shop_list;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        llContainer = (LinearLayout) rootView.findViewById(R.id.ll_container);
        swipeLinearLayoutList = new ArrayList<>();

        Bundle bundle = getArguments();
        if (bundle != null) {
            fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE);
        }
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        listenerAdd = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeAdd.value());
                push(ShopsTypeEditFragment.class, bundle);
            }
        };
    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {

    }

    @Override
    protected void configActionBar() {
        if (Utils.isStringNullOrEmpty(fromPage)) {
            setNormalMenuActionBar();
        } else {
            setStackedActionBar();
        }
        getTvLeftTitle().setText(R.string.title_shopping_setting);

        boolean isAuthShopsEdit = AppUtils.getAuth(AppUtils.AuthControl.AuthControlShopsEdit, getActivity());
        if (isAuthShopsEdit) {
            getTvRight().setBackgroundResource(R.drawable.icon_common_add);
            getTvRight().setOnClickListener(listenerAdd);
        }

        setOnBackListener(new OnPopBackListener() {
            @Override
            public boolean preDoBack() {
                Bundle bundle = new Bundle();

                try {
                    doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        getShops();
    }

    private void getShops() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));

        HttpManager<JsonProductType> hh = new HttpManager<JsonProductType>(ShopsListFragment.this) {
            @Override
            public void onJsonSuccess(JsonProductType jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    initData(jo);
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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.ShopsProductTypeGet, params);
    }

    private void initData(JsonProductType jo) {
        llContainer.removeAllViews();
        swipeLinearLayoutList.clear();

        if (jo.getShoppingTypeArrayList() != null) {
            llContainer.addView(AppUtils.getSeparatorLine(this));
            for (JsonProductType.ProductType product : jo.getShoppingTypeArrayList()) {
                llContainer.addView(generateDefaultShops(product));
                llContainer.addView(AppUtils.getSeparatorLine(this));
            }
        }
        if (jo.getProductTypeArrayList() != null) {
            for (JsonProductType.ProductType product : jo.getProductTypeArrayList()) {
                llContainer.addView(generateOtherShops(product));
                llContainer.addView(AppUtils.getSeparatorLine(this));
            }
        }
    }

    private SwipeLinearLayout generateOtherShops(final JsonProductType.ProductType product) {
        SwipeLinearLayout.AfterShowRightListener afterShowRightListener = new SwipeLinearLayout.AfterShowRightListener() {
            @Override
            public void doAfterShowRight(SwipeLinearLayout swipeLinearLayout) {
                for (SwipeLinearLayout sll : swipeLinearLayoutList) {
                    if (swipeLinearLayout != sll) {
                        sll.hideRight();
                    }
                }
            }
        };

        int rightViewWidth = AppUtils.getRightButtonWidth(mContext);
        final SwipeLinearLayout swipeLinearLayout = new SwipeLinearLayout(getActivity(), rightViewWidth);
        swipeLinearLayout.setAfterShowRightListener(afterShowRightListener);

        RelativeLayout rlShop = generateShopsLayout(product);
        RelativeLayout.LayoutParams rlShopLayoutParams = new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        rlShop.setLayoutParams(rlShopLayoutParams);
        swipeLinearLayout.addView(rlShop);

        RelativeLayout rlRight = new RelativeLayout(getActivity());
        rlRight.setBackgroundResource(R.drawable.bg_common_edit);

        LinearLayout.LayoutParams rlRightLayoutParams = new LinearLayout.LayoutParams(rightViewWidth, MATCH_PARENT);
        rlRight.setLayoutParams(rlRightLayoutParams);

        IteeTextView tvEdit = new IteeTextView(getActivity());
        tvEdit.setText(R.string.common_edit);
        tvEdit.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvEdit.setTextColor(getColor(R.color.common_white));
        tvEdit.setGravity(Gravity.CENTER);

        RelativeLayout.LayoutParams tvEditLayoutParams = new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        tvEdit.setLayoutParams(tvEditLayoutParams);
        rlRight.addView(tvEdit);

        boolean isAuthShopsEdit = AppUtils.getAuth(AppUtils.AuthControl.AuthControlShopsEdit, getActivity());
        if (isAuthShopsEdit) {
            rlRight.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
                @Override
                public void noDoubleClick(View v) {
                    swipeLinearLayout.hideRight();
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.COMMON_SHOP_ID, product.getTypeId());
                    bundle.putString(TransKey.SHOPS_TYPE_NAME, product.getTypeName());
                    push(ShopsTypeEditFragment.class, bundle);
                }
            });
        }
        swipeLinearLayout.addView(rlRight);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(100));
        swipeLinearLayout.setLayoutParams(layoutParams);
        swipeLinearLayoutList.add(swipeLinearLayout);
        return swipeLinearLayout;
    }

    private RelativeLayout generateDefaultShops(JsonProductType.ProductType product) {
        return generateShopsLayout(product);
    }

    private RelativeLayout generateShopsLayout(final JsonProductType.ProductType product) {
        RelativeLayout relativeLayout = new RelativeLayout(getActivity());
        relativeLayout.setBackgroundResource(R.drawable.bg_linear_selector_color_white);

        IteeTextView tvLeft = new IteeTextView(getActivity());
        tvLeft.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvLeft.setText(product.getTypeName());
        tvLeft.setGravity(Gravity.CENTER_VERTICAL);

        RelativeLayout.LayoutParams tvLeftLayoutParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(300), getActualHeightOnThisDevice(100));
        tvLeftLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        tvLeftLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvLeftLayoutParams.leftMargin = getActualWidthOnThisDevice(40);
        tvLeft.setLayoutParams(tvLeftLayoutParams);
        relativeLayout.addView(tvLeft);

        ImageView ivRightArrow = new ImageView(getActivity());
        ivRightArrow.setImageResource(R.drawable.icon_right_arrow);

        RelativeLayout.LayoutParams ivRightArrowParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        ivRightArrowParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        ivRightArrowParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivRightArrowParams.rightMargin = getActualWidthOnThisDevice(40);
        ivRightArrow.setLayoutParams(ivRightArrowParams);
        relativeLayout.addView(ivRightArrow);

        LinearLayout.LayoutParams relativeLayoutLayoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(100));
        relativeLayout.setLayoutParams(relativeLayoutLayoutParams);

        boolean isAuthProductEdit = AppUtils.getAuth(AppUtils.AuthControl.AuthControlProductEdit, getActivity());
        if (isAuthProductEdit) {
            relativeLayout.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
                @Override
                public void noDoubleClick(View v) {
                    int typeId = product.getTypeId();
                    String typeName = product.getTypeName();
                    int canEdit = product.getTypeEdit();
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.COMMON_SHOP_ID, typeId);

                    if (typeId == Integer.valueOf(Constants.SHOP_TYPE_PROMOTE) && canEdit == 0) {
                        push(ShopsPromoteListFragment.class, bundle);
                    } else if (typeId == Integer.valueOf(Constants.SHOP_TYPE_RENTAL) && canEdit == 0) {
                        push(ShopsRentalShopFragment.class, bundle);
                    } else if (typeId == Integer.valueOf(Constants.SHOP_TYPE_PACKAGE) && canEdit == 0) {
                        push(ShopsPackageListFragment.class, bundle);
                    } else {
                        bundle.putString(TransKey.COMMON_SHOP_NAME, typeName);
                        push(ShopsProductListFragment.class, bundle);
                    }
                }
            });
        }
        return relativeLayout;
    }
}
