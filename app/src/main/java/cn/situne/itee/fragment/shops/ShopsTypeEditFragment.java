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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeRedDeleteButton;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:ShopsTypeEditFragment <br/>
 * Function: show type edit. <br/>
 * Date: 2015-03-28 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class ShopsTypeEditFragment extends BaseEditFragment {

    private RelativeLayout rlShopContainer;
    private LinearLayout rlDeleteContainer;

    private IteeTextView tvShopName;
    private IteeEditText etShopName;
    private IteeRedDeleteButton btnDelete;

    private int typeId;
    private String typeName;

    private View.OnClickListener listenerOk;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_shops_shop_edit;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        rlShopContainer = (RelativeLayout) rootView.findViewById(R.id.rl_shop_container);
        rlDeleteContainer = (LinearLayout) rootView.findViewById(R.id.rl_delete_container);

        tvShopName = new IteeTextView(this);
        etShopName = new IteeEditText(this);
        btnDelete = new IteeRedDeleteButton(getActivity());
    }

    @Override
    protected void setDefaultValueOfControls() {
        tvShopName.setText(R.string.shop_setting_type);
        tvShopName.setTextColor(getResources().getColor(R.color.common_black));
        etShopName.setHint(getString(R.string.shop_setting_type));
        etShopName.setText(typeName);
        btnDelete.setText(R.string.common_delete);

    }

    @Override
    protected void setListenersOfControls() {
        btnDelete.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                    AppUtils.showDeleteAlert(ShopsTypeEditFragment.this, new AppUtils.DeleteConfirmListener() {
                        @Override
                        public void onClickDelete() {
                            deleteProductType();
                        }
                    });
                }
            }
        });

        listenerOk = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
                    setFragmentMode(FragmentMode.FragmentModeEdit);
                    changeEditState();
                    btnDelete.setVisibility(View.VISIBLE);
                } else if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
                    if (doCheck()) {
                        postProductType();
                    }
                } else {
                    if (doCheck()) {
                        putProductType();
                    }
                }
            }
        };
    }

    @Override
    protected void setLayoutOfControls() {

        LinearLayout.LayoutParams rlShopLayoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(100));
        rlShopContainer.setLayoutParams(rlShopLayoutParams);

        rlShopContainer.addView(tvShopName);
        RelativeLayout.LayoutParams paramsTvShopName = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(200), MATCH_PARENT);
        paramsTvShopName.leftMargin = getActualWidthOnThisDevice(40);
        paramsTvShopName.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        tvShopName.setLayoutParams(paramsTvShopName);

        rlShopContainer.addView(etShopName);
        RelativeLayout.LayoutParams paramsEtShopName = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(300), MATCH_PARENT);
        paramsEtShopName.rightMargin = getActualWidthOnThisDevice(40);
        paramsEtShopName.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        etShopName.setLayoutParams(paramsEtShopName);

        AppUtils.addBottomSeparatorLine(rlShopContainer, this);

        LinearLayout.LayoutParams rlDeleteContainerLayoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlDeleteContainer.setLayoutParams(rlDeleteContainerLayoutParams);
        rlDeleteContainer.setGravity(Gravity.CENTER);

        rlDeleteContainer.addView(btnDelete);
        LinearLayout.LayoutParams paramsBtnDelete = (LinearLayout.LayoutParams) btnDelete.getLayoutParams();
        paramsBtnDelete.width = AppUtils.getLargerButtonWidth(this);
        paramsBtnDelete.height = AppUtils.getLargerButtonHeight(this);
        paramsBtnDelete.topMargin = getActualHeightOnThisDevice(30);
        paramsBtnDelete.bottomMargin = getActualHeightOnThisDevice(30);

        btnDelete.setLayoutParams(paramsBtnDelete);
    }

    @Override
    protected void setPropertyOfControls() {
        if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
            rlDeleteContainer.setVisibility(View.INVISIBLE);
        }
        etShopName.setEnabled(getFragmentMode() != FragmentMode.FragmentModeBrowse);

        etShopName.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        etShopName.setPadding(0, 5, 0, 0);
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(R.string.shop_setting_type_edit);
        if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
            getTvRight().setText(Constants.STR_EMPTY);
            getTvRight().setBackgroundResource(R.drawable.icon_common_edit);
            btnDelete.setVisibility(View.GONE);
        } else {
            getTvRight().setText(R.string.common_save);
            getTvRight().setBackground(null);
        }
        getTvRight().setOnClickListener(listenerOk);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            typeId = bundle.getInt(TransKey.COMMON_SHOP_ID);
            typeName = bundle.getString(TransKey.SHOPS_TYPE_NAME);
            FragmentMode mode = FragmentMode.valueOf(bundle.getInt(TransKey.COMMON_FRAGMENT_MODE));
            setFragmentMode(mode);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void changeEditState() {
        etShopName.setEnabled(true);
        btnDelete.setEnabled(true);

        getTvRight().setText(R.string.common_save);
        getTvRight().setBackground(null);
    }

    private void deleteProductType() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_PRODUCT_TYPE_TYPE_ID, String.valueOf(typeId));

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShopsTypeEditFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();

                if (returnCode == Constants.RETURN_CODE_20403_DELETE_SUCCESSFULLY) {
                    doBackWithRefresh();
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
        hh.startDelete(this.getActivity(), ApiManager.HttpApi.ShopsProductTypeDelete, params);
    }

    private void putProductType() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_PRODUCT_TYPE_TYPE_ID, String.valueOf(typeId));
        params.put(ApiKey.SHOPS_PRODUCT_TYPE_TYPE_NAME, etShopName.getValue());

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShopsTypeEditFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                Utils.showShortToast(getActivity(), msg);
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    doBackWithRefresh();
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
        hh.startPut(this.getActivity(), ApiManager.HttpApi.ShopsProductTypePut, params);
    }

    private void postProductType() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_PRODUCT_TYPE_TYPE_NAME, etShopName.getValue());

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShopsTypeEditFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();

                if (returnCode == Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY) {
                    doBackWithRefresh();
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
        hh.start(this.getActivity(), ApiManager.HttpApi.ShopsProductTypePost, params);
    }

    private boolean doCheck() {
        boolean res = true;
        if (Utils.isStringNullOrEmpty(etShopName.getValue())) {
            res = false;
            Utils.showShortToast(getActivity(), AppUtils.generateNotNullMessage(this, R.string.shop_setting_type));
        }
        return res;
    }
}
