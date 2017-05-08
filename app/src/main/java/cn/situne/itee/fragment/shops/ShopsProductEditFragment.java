/**
 * Project Name: itee
 * File Name:  ShopsProductEditProFragment.java
 * Package Name: cn.situne.itee.fragment.shops
 * Date:   2015-04-13
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.shops;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.activity.ScanQrCodeActivity;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.view.CheckSwitchButton;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeMoneyEditText;
import cn.situne.itee.view.IteeQtyEditText;
import cn.situne.itee.view.IteeRedDeleteButton;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.AddCoursePopupWindow;


/**
 * ClassName:ShopsProductEditProFragment <br/>
 * Function: 普通商店 子画面. <br/>
 * UI:   05-04-01-03
 * Date: 2015-04-13 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */

public class ShopsProductEditFragment extends BaseEditFragment {

    private RelativeLayout rlProductName;
    private RelativeLayout rlEnableSubClass;
    private RelativeLayout rlProperty;
    private RelativeLayout rlQtyOfSubClass;
    private RelativeLayout rlPrice;
    private RelativeLayout rlUnlimitedQty;
    private RelativeLayout rlQty;
    private RelativeLayout rlCode;
    private LinearLayout rlDelete;

    private IteeTextView tvProductNameKey;
    private IteeEditText etProductNameValue;
    private IteeTextView tvEnableSubClass;
    private CheckSwitchButton swEnableSubClass;
    private IteeTextView tvProperty;
    private ImageView ivSubClass;
    private IteeTextView tvQtyOfSubClass;
    private ImageView ivQtyOfSubClass;
    private IteeTextView tvPriceKey;
    private IteeTextView tvPriceCurrency;
    private IteeMoneyEditText etPriceValue;
    private IteeTextView tvQtyKey;
    private IteeQtyEditText etQtyValue;
    private IteeTextView tvCodeKey;
    private ImageView ivCodeIcon;
    private EditText etCodeValue;
    private IteeRedDeleteButton btnDelete;

    private String productId;
    private String productType;

    private String productName;
    private String productPrice;
    private String productQty;
    private String productCode;
    private int isEnableProperty;
    private String shopName;
    private CheckSwitchButton unlimitedQtyCSBtn;
    private AppUtils.NoDoubleClickListener okListener;

    private String unlimitedFlag;

    private FragmentMode mode = FragmentMode.FragmentModeBrowse;
    View.OnClickListener rlPropertyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mode != FragmentMode.FragmentModeBrowse) {
                Bundle bundle = new Bundle();
                bundle.putString(TransKey.SHOPS_PRODUCT_ID, productId);
                push(ShopsPropertyEditFragment.class, bundle);
            }
        }
    };
    View.OnClickListener rlQtyOfSubClassListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mode != FragmentMode.FragmentModeBrowse) {
                Bundle bundle = new Bundle();
                bundle.putString(TransKey.SHOPS_PRODUCT_ID, productId);

                bundle.putBoolean(TransKey.SHOPS_PACKAGE_IS_QTY, true);

                bundle.putString(TransKey.COMMON_FROM_PAGE, ShopsProductEditFragment.class.getName());
                push(ShopsPropertyPriceOrQtyEditFragment.class, bundle);
            }
        }
    };
    private AddCoursePopupWindow addCoursePopupWindow;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_product_edit;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        addCoursePopupWindow = new AddCoursePopupWindow(getActivity(), null);
        addCoursePopupWindow.setNotTouchCancel(true);
        Bundle bundle = getArguments();

        if (bundle != null) {
            productType = String.valueOf(bundle.getInt(TransKey.COMMON_SHOP_ID));

            productId = bundle.getString(TransKey.COMMON_PRODUCT_ID);
            productName = bundle.getString(TransKey.COMMON_PRODUCT_NAME);
            productQty = bundle.getString(TransKey.COMMON_PRODUCT_QTY);
            productPrice = bundle.getString(TransKey.COMMON_PRODUCT_PRICE);
            productCode = bundle.getString(TransKey.COMMON_PRODUCT_CODE);
            shopName = bundle.getString(TransKey.COMMON_SHOP_NAME);
            isEnableProperty = bundle.getInt(TransKey.COMMON_PRODUCT_ENABLE_PROPERTY);
            unlimitedFlag = bundle.getString(TransKey.UNLIMITED_FLAG);
            mode = FragmentMode.valueOf(bundle.getInt(TransKey.COMMON_FRAGMENT_MODE));
            setFragmentMode(mode);

        }
        rlProductName = (RelativeLayout) rootView.findViewById(R.id.rl_productName);
        rlEnableSubClass = (RelativeLayout) rootView.findViewById(R.id.rl_enableSubClass);
        rlProperty = (RelativeLayout) rootView.findViewById(R.id.rl_subClass);
        rlQtyOfSubClass = (RelativeLayout) rootView.findViewById(R.id.rl_qtyOfSubClass);
        rlPrice = (RelativeLayout) rootView.findViewById(R.id.rl_price);
        rlUnlimitedQty = (RelativeLayout) rootView.findViewById(R.id.rl_unlimited_qty);
        rlQty = (RelativeLayout) rootView.findViewById(R.id.rl_qty);
        rlCode = (RelativeLayout) rootView.findViewById(R.id.rl_code);
        rlDelete = (LinearLayout) rootView.findViewById(R.id.rl_delete);

        tvProductNameKey = new IteeTextView(getActivity());
        etProductNameValue = new IteeEditText(this);
        tvEnableSubClass = new IteeTextView(getActivity());
        swEnableSubClass = new CheckSwitchButton(this);
        tvProperty = new IteeTextView(getActivity());
        ivSubClass = new ImageView(getActivity());
        tvQtyOfSubClass = new IteeTextView(getActivity());
        ivQtyOfSubClass = new ImageView(getActivity());
        tvPriceKey = new IteeTextView(getActivity());
        tvPriceCurrency = new IteeTextView(getActivity());
        etPriceValue = new IteeMoneyEditText(this);
        tvQtyKey = new IteeTextView(getActivity());
        etQtyValue = new IteeQtyEditText(this);
        tvCodeKey = new IteeTextView(getActivity());
        ivCodeIcon = new ImageView(getActivity());
        etCodeValue = new EditText(getActivity());
        btnDelete = new IteeRedDeleteButton(getActivity());

    }

    @Override
    protected void setDefaultValueOfControls() {
        LinearLayout.LayoutParams paramDepartment = (LinearLayout.LayoutParams) rlProductName.getLayoutParams();
        paramDepartment.height = getActualHeightOnThisDevice(100);
        rlProductName.setLayoutParams(paramDepartment);

        rlProductName.addView(tvProductNameKey);
        RelativeLayout.LayoutParams paramTvProductNameKey = (RelativeLayout.LayoutParams) tvProductNameKey.getLayoutParams();
        paramTvProductNameKey.width = WRAP_CONTENT;
        paramTvProductNameKey.height = WRAP_CONTENT;
        paramTvProductNameKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvProductNameKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvProductNameKey.leftMargin = getActualWidthOnThisDevice(40);
        tvProductNameKey.setLayoutParams(paramTvProductNameKey);

        rlProductName.addView(etProductNameValue);
        RelativeLayout.LayoutParams paramsTvProductNameValue = (RelativeLayout.LayoutParams) etProductNameValue.getLayoutParams();
        paramsTvProductNameValue.width = getActualWidthOnThisDevice(300);
        paramsTvProductNameValue.height = WRAP_CONTENT;
        paramsTvProductNameValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvProductNameValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvProductNameValue.rightMargin = getActualWidthOnThisDevice(40);
        etProductNameValue.setLayoutParams(paramsTvProductNameValue);

        LinearLayout.LayoutParams paramEnableSubClass = (LinearLayout.LayoutParams) rlEnableSubClass.getLayoutParams();
        paramEnableSubClass.height = getActualHeightOnThisDevice(100);
        rlEnableSubClass.setLayoutParams(paramEnableSubClass);

        rlEnableSubClass.addView(tvEnableSubClass);
        RelativeLayout.LayoutParams paramTvEnableSubClass = (RelativeLayout.LayoutParams) tvEnableSubClass.getLayoutParams();
        paramTvEnableSubClass.width = WRAP_CONTENT;
        paramTvEnableSubClass.height = WRAP_CONTENT;
        paramTvEnableSubClass.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvEnableSubClass.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvEnableSubClass.leftMargin = getActualWidthOnThisDevice(40);
        tvEnableSubClass.setLayoutParams(paramTvEnableSubClass);

        rlEnableSubClass.addView(swEnableSubClass);
        RelativeLayout.LayoutParams paramsSwEnableSubClass = (RelativeLayout.LayoutParams) swEnableSubClass.getLayoutParams();
        paramsSwEnableSubClass.width = WRAP_CONTENT;
        paramsSwEnableSubClass.height = WRAP_CONTENT;
        paramsSwEnableSubClass.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsSwEnableSubClass.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsSwEnableSubClass.rightMargin = getActualWidthOnThisDevice(40);
        swEnableSubClass.setLayoutParams(paramsSwEnableSubClass);

        LinearLayout.LayoutParams paramSubClass = (LinearLayout.LayoutParams) rlProperty.getLayoutParams();
        paramSubClass.height = getActualHeightOnThisDevice(100);
        rlProperty.setLayoutParams(paramSubClass);

        rlProperty.addView(tvProperty);
        RelativeLayout.LayoutParams paramTvSubClass = (RelativeLayout.LayoutParams) tvProperty.getLayoutParams();
        paramTvSubClass.width = WRAP_CONTENT;
        paramTvSubClass.height = WRAP_CONTENT;
        paramTvSubClass.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvSubClass.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvSubClass.leftMargin = getActualWidthOnThisDevice(40);
        tvProperty.setLayoutParams(paramTvSubClass);

        rlProperty.addView(ivSubClass);
        RelativeLayout.LayoutParams paramsIvSubClass = (RelativeLayout.LayoutParams) ivSubClass.getLayoutParams();
        paramsIvSubClass.width = WRAP_CONTENT;
        paramsIvSubClass.height = WRAP_CONTENT;
        paramsIvSubClass.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIvSubClass.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsIvSubClass.rightMargin = getActualWidthOnThisDevice(40);
        ivSubClass.setLayoutParams(paramsIvSubClass);


        unlimitedQtyCSBtn = new CheckSwitchButton(mContext);


        rlUnlimitedQty.addView(getKeyTextView(getString(R.string.shop_setting_unlimited_qty)));

        unlimitedQtyCSBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    rlQty.setBackgroundColor(getColor(R.color.common_deep_gray));
                    etQtyValue.setBackgroundColor(getColor(R.color.common_deep_gray));
                    etQtyValue.setEnabled(false);


                    rlQtyOfSubClass.setBackgroundColor(getColor(R.color.common_deep_gray));
                    rlQtyOfSubClass.setOnClickListener(null);
                    //qtyShadeView.setVisibility(View.VISIBLE);
                } else {

                    if (swEnableSubClass.isChecked()) {
                        rlQtyOfSubClass.setBackgroundColor(getColor(R.color.common_white));
                        rlQtyOfSubClass.setOnClickListener(rlQtyOfSubClassListener);

                    } else {
                        rlQty.setBackgroundColor(getColor(R.color.common_white));
                        etQtyValue.setBackgroundColor(getColor(R.color.common_white));
                        etQtyValue.setEnabled(true);

                    }

                }
            }
        });

        RelativeLayout.LayoutParams csBtnParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, getActualHeightOnThisDevice(100));
        csBtnParams.rightMargin = getActualWidthOnThisDevice(40);
        csBtnParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        csBtnParams.addRule(RelativeLayout.CENTER_VERTICAL);
        unlimitedQtyCSBtn.setLayoutParams(csBtnParams);
        rlUnlimitedQty.addView(unlimitedQtyCSBtn);


        LinearLayout.LayoutParams paramQtyOfSubClass = (LinearLayout.LayoutParams) rlQtyOfSubClass.getLayoutParams();
        paramQtyOfSubClass.height = getActualHeightOnThisDevice(100);
        rlQtyOfSubClass.setLayoutParams(paramQtyOfSubClass);

        rlQtyOfSubClass.addView(tvQtyOfSubClass);
        RelativeLayout.LayoutParams paramTvQtyOfSubClass = (RelativeLayout.LayoutParams) tvQtyOfSubClass.getLayoutParams();
        paramTvQtyOfSubClass.width = WRAP_CONTENT;
        paramTvQtyOfSubClass.height = WRAP_CONTENT;
        paramTvQtyOfSubClass.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvQtyOfSubClass.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvQtyOfSubClass.leftMargin = getActualWidthOnThisDevice(40);
        tvQtyOfSubClass.setLayoutParams(paramTvQtyOfSubClass);

        rlQtyOfSubClass.addView(ivQtyOfSubClass);
        RelativeLayout.LayoutParams paramsIvQtyOfSubClass = (RelativeLayout.LayoutParams) ivQtyOfSubClass.getLayoutParams();
        paramsIvQtyOfSubClass.width = WRAP_CONTENT;
        paramsIvQtyOfSubClass.height = WRAP_CONTENT;
        paramsIvQtyOfSubClass.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIvQtyOfSubClass.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsIvQtyOfSubClass.rightMargin = getActualWidthOnThisDevice(40);
        ivQtyOfSubClass.setLayoutParams(paramsIvQtyOfSubClass);


        LinearLayout.LayoutParams paramPrice = (LinearLayout.LayoutParams) rlPrice.getLayoutParams();
        paramPrice.height = getActualHeightOnThisDevice(100);
        rlPrice.setLayoutParams(paramPrice);

        rlPrice.addView(tvPriceKey);
        RelativeLayout.LayoutParams paramTvPriceKey = (RelativeLayout.LayoutParams) tvPriceKey.getLayoutParams();
        paramTvPriceKey.width = WRAP_CONTENT;
        paramTvPriceKey.height = WRAP_CONTENT;
        paramTvPriceKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvPriceKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvPriceKey.leftMargin = getActualWidthOnThisDevice(40);
        tvPriceKey.setLayoutParams(paramTvPriceKey);

        rlPrice.addView(etPriceValue);
        RelativeLayout.LayoutParams paramsTvPriceValue = (RelativeLayout.LayoutParams) etPriceValue.getLayoutParams();
        paramsTvPriceValue.width = getActualWidthOnThisDevice(250);
        paramsTvPriceValue.height = WRAP_CONTENT;
        paramsTvPriceValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvPriceValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvPriceValue.rightMargin = getActualWidthOnThisDevice(40);
        etPriceValue.setLayoutParams(paramsTvPriceValue);
        etPriceValue.setId(View.generateViewId());


        rlPrice.addView(tvPriceCurrency);
        RelativeLayout.LayoutParams paramTvCurrency = (RelativeLayout.LayoutParams) tvPriceCurrency.getLayoutParams();
        paramTvCurrency.width = WRAP_CONTENT;
        paramTvCurrency.height = WRAP_CONTENT;
        paramTvCurrency.addRule(RelativeLayout.LEFT_OF, etPriceValue.getId());
        paramTvCurrency.addRule(RelativeLayout.CENTER_VERTICAL);
        tvPriceCurrency.setLayoutParams(paramTvCurrency);

        LinearLayout.LayoutParams paramQty = (LinearLayout.LayoutParams) rlQty.getLayoutParams();
        paramQty.height = getActualHeightOnThisDevice(100);
        rlQty.setLayoutParams(paramQty);

        rlQty.addView(tvQtyKey);
        RelativeLayout.LayoutParams paramTvQtyKey = (RelativeLayout.LayoutParams) tvQtyKey.getLayoutParams();
        paramTvQtyKey.width = WRAP_CONTENT;
        paramTvQtyKey.height = WRAP_CONTENT;
        paramTvQtyKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvQtyKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvQtyKey.leftMargin = getActualWidthOnThisDevice(40);
        tvQtyKey.setLayoutParams(paramTvQtyKey);

        rlQty.addView(etQtyValue);
        RelativeLayout.LayoutParams paramsTvQtyValue = (RelativeLayout.LayoutParams) etQtyValue.getLayoutParams();
        paramsTvQtyValue.width = getActualWidthOnThisDevice(250);
        paramsTvQtyValue.height = WRAP_CONTENT;
        paramsTvQtyValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvQtyValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvQtyValue.rightMargin = getActualWidthOnThisDevice(40);
        etQtyValue.setLayoutParams(paramsTvQtyValue);

        LinearLayout.LayoutParams paramCode = (LinearLayout.LayoutParams) rlCode.getLayoutParams();
        paramCode.height = getActualHeightOnThisDevice(100);
        rlCode.setLayoutParams(paramCode);

        rlCode.addView(tvCodeKey);
        RelativeLayout.LayoutParams paramTvCodeKey = (RelativeLayout.LayoutParams) tvCodeKey.getLayoutParams();
        paramTvCodeKey.width = WRAP_CONTENT;
        paramTvCodeKey.height = WRAP_CONTENT;
        paramTvCodeKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvCodeKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvCodeKey.leftMargin = getActualWidthOnThisDevice(40);
        tvCodeKey.setLayoutParams(paramTvCodeKey);

        rlCode.addView(ivCodeIcon);
        RelativeLayout.LayoutParams paramIvCodeIcon = (RelativeLayout.LayoutParams) ivCodeIcon.getLayoutParams();
        paramIvCodeIcon.width = getActualWidthOnThisDevice(50);
        paramIvCodeIcon.height = getActualWidthOnThisDevice(50);
        paramIvCodeIcon.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramIvCodeIcon.addRule(RelativeLayout.CENTER_VERTICAL);
        paramIvCodeIcon.leftMargin = getActualWidthOnThisDevice(150);
        ivCodeIcon.setLayoutParams(paramIvCodeIcon);

        rlCode.addView(etCodeValue);
        RelativeLayout.LayoutParams paramsTvCodeValue = (RelativeLayout.LayoutParams) etCodeValue.getLayoutParams();
        paramsTvCodeValue.width = getActualWidthOnThisDevice(250);
        paramsTvCodeValue.height = WRAP_CONTENT;
        paramsTvCodeValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvCodeValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvCodeValue.rightMargin = getActualWidthOnThisDevice(40);
        etCodeValue.setLayoutParams(paramsTvCodeValue);

        LinearLayout.LayoutParams paramDelete = (LinearLayout.LayoutParams) rlDelete.getLayoutParams();
        paramDelete.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        paramDelete.width = LinearLayout.LayoutParams.MATCH_PARENT;
        rlDelete.setLayoutParams(paramDelete);

        rlDelete.addView(btnDelete);
        rlDelete.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams paramBtDelete = (LinearLayout.LayoutParams) btnDelete.getLayoutParams();
        paramBtDelete.width = AppUtils.getLargerButtonWidth(this);
        paramBtDelete.height = AppUtils.getLargerButtonHeight(this);
        paramBtDelete.topMargin = getActualHeightOnThisDevice(30);
        paramBtDelete.bottomMargin = getActualHeightOnThisDevice(30);
        btnDelete.setLayoutParams(paramBtDelete);

        if (Constants.UNLIMITED_FLAG_OFF.equals(unlimitedFlag)) {

            unlimitedQtyCSBtn.setChecked(false);
        } else {
            unlimitedQtyCSBtn.setChecked(true);
            rlQty.setBackgroundColor(getColor(R.color.common_deep_gray));
            etQtyValue.setBackgroundColor(getColor(R.color.common_deep_gray));
            etQtyValue.setEnabled(false);
            rlQtyOfSubClass.setBackgroundColor(getColor(R.color.common_deep_gray));
            rlQtyOfSubClass.setOnClickListener(null);

        }

    }

    private IteeTextView getKeyTextView(String key) {
        RelativeLayout.LayoutParams leftTextParams
                = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, getActualHeightOnThisDevice(100));
        leftTextParams.leftMargin = getActualWidthOnThisDevice(40);
        IteeTextView leftText = new IteeTextView(getBaseActivity());
        leftText.setText(key);
        leftText.setLayoutParams(leftTextParams);
        return leftText;
    }

    @Override
    protected void setListenersOfControls() {

        AppUtils.NoDoubleClickListener deleteListener = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                    AppUtils.showDeleteAlert(ShopsProductEditFragment.this, new AppUtils.DeleteConfirmListener() {
                        @Override
                        public void onClickDelete() {
                            netLinkProductDelete();
                        }
                    });
                }
            }
        };
        btnDelete.setOnClickListener(deleteListener);

        okListener = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {

                if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
                    if (doCheck()) {
                        netLinkProductAdd(0);
                    }
                } else {


                    if (mode == FragmentMode.FragmentModeBrowse) {
                        btnDelete.setVisibility(View.VISIBLE);
                        mode = FragmentMode.FragmentModeEdit;
                        setFragmentMode(mode);
                        changeState();
                        getTvRight().setBackground(null);
                        getTvRight().setText(R.string.common_ok);
                    } else {
                        if (doCheck()) {
                            netLinkProductModify();
                        }
                    }

                }
            }
        };

        swEnableSubClass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rlQty.setBackgroundColor(getColor(R.color.common_deep_gray));
                    etQtyValue.setBackgroundColor(getColor(R.color.common_deep_gray));
                    etQtyValue.setEnabled(false);
                    rlProperty.setBackgroundColor(getColor(R.color.common_white));
                    rlProperty.setOnClickListener(rlPropertyListener);

                    if (!unlimitedQtyCSBtn.isChecked()) {
                        rlQtyOfSubClass.setBackgroundColor(getColor(R.color.common_white));
                        rlQtyOfSubClass.setOnClickListener(rlQtyOfSubClassListener);
                    }


                    if (Utils.isStringNullOrEmpty(productId)) {
                        Utils.hideKeyboard(getBaseActivity());

                        if (doCheck()) {
                            addCoursePopupWindow.showAtLocation(ShopsProductEditFragment.this.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                            addCoursePopupWindow.tvEvent.setVisibility(View.GONE);
                            addCoursePopupWindow.tvContent.setText(getString(R.string.common_add) + etProductNameValue.getText().toString());

                            addCoursePopupWindow.btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    addCoursePopupWindow.dismiss();
                                    netLinkProductAdd(1);
                                }
                            });

                            addCoursePopupWindow.btn_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    addCoursePopupWindow.dismiss();
                                    swEnableSubClass.setChecked(false);
                                    if (!unlimitedQtyCSBtn.isChecked()) {
                                        rlQty.setBackgroundColor(getColor(R.color.common_white));
                                        etQtyValue.setBackgroundColor(getColor(R.color.common_white));
                                        etQtyValue.setEnabled(true);
                                    }
                                    rlProperty.setBackgroundColor(getColor(R.color.common_deep_gray));
                                    rlQtyOfSubClass.setBackgroundColor(getColor(R.color.common_deep_gray));
                                    rlProperty.setOnClickListener(null);
                                    rlQtyOfSubClass.setOnClickListener(null);

                                }
                            });


                        } else {

                            swEnableSubClass.setChecked(false);
                            if (!unlimitedQtyCSBtn.isChecked()) {
                                rlQty.setBackgroundColor(getColor(R.color.common_white));
                                etQtyValue.setBackgroundColor(getColor(R.color.common_white));
                                etQtyValue.setEnabled(true);
                            }
                            rlProperty.setBackgroundColor(getColor(R.color.common_deep_gray));
                            rlQtyOfSubClass.setBackgroundColor(getColor(R.color.common_deep_gray));
                            rlProperty.setOnClickListener(null);
                            rlQtyOfSubClass.setOnClickListener(null);
                        }

                    }


                } else {

                    if (!unlimitedQtyCSBtn.isChecked()) {
                        rlQty.setBackgroundColor(getColor(R.color.common_white));
                        etQtyValue.setBackgroundColor(getColor(R.color.common_white));
                        etQtyValue.setEnabled(true);
                    }

                    rlProperty.setBackgroundColor(getColor(R.color.common_deep_gray));
                    rlQtyOfSubClass.setBackgroundColor(getColor(R.color.common_deep_gray));
                    rlProperty.setOnClickListener(null);
                    rlQtyOfSubClass.setOnClickListener(null);


                }
            }
        });

        ivCodeIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseActivity(), ScanQrCodeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, ScanQrCodeActivity.SCANNING_GREQUEST_CODE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ScanQrCodeActivity.SCANNING_GREQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String qrCode = bundle.getString(TransKey.QR_CODE);
                    etCodeValue.setText(qrCode);
                }
                break;
        }
    }

    @Override
    protected void setLayoutOfControls() {

    }


    @Override
    protected void reShowWithBackValue() {
        Bundle bundle = getReturnValues();

        if (bundle != null) {
            String qytTotal = bundle.getString(TransKey.SHOPS_QTY_TOTAL);
            etQtyValue.setText(qytTotal);
        }
    }

    @Override
    protected void setPropertyOfControls() {
        tvProductNameKey.setText(getString(R.string.shop_setting_product_name));
        tvProductNameKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvProductNameKey.setTextColor(getColor(R.color.common_black));

        etProductNameValue.setText(productName);
        etProductNameValue.setTextSize(Constants.FONT_SIZE_NORMAL);

        etProductNameValue.setBackgroundColor(getColor(R.color.common_white));
        etProductNameValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        etProductNameValue.setSingleLine();

        tvEnableSubClass.setText(getString(R.string.shop_setting_enable_subcategories));
        tvEnableSubClass.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvEnableSubClass.setTextColor(getColor(R.color.common_black));

        if (isEnableProperty == Constants.SWITCH_RIGHT) {
            swEnableSubClass.setChecked(true);
        } else {
            swEnableSubClass.setChecked(false);
        }

        if (swEnableSubClass.isChecked()) {
            rlQty.setBackgroundColor(getColor(R.color.common_deep_gray));
            etQtyValue.setBackgroundColor(getColor(R.color.common_deep_gray));
            etQtyValue.setEnabled(false);
            rlProperty.setBackgroundColor(getColor(R.color.common_white));
            rlQtyOfSubClass.setBackgroundColor(getColor(R.color.common_white));
            rlProperty.setOnClickListener(rlPropertyListener);
            rlQtyOfSubClass.setOnClickListener(rlQtyOfSubClassListener);
        } else {
            rlQty.setBackgroundColor(getColor(R.color.common_white));
            etQtyValue.setBackgroundColor(getColor(R.color.common_white));
            etQtyValue.setEnabled(true);
            rlProperty.setBackgroundColor(getColor(R.color.common_deep_gray));
            rlQtyOfSubClass.setBackgroundColor(getColor(R.color.common_deep_gray));
            rlProperty.setOnClickListener(null);
            rlQtyOfSubClass.setOnClickListener(null);
        }


        tvProperty.setText(getString(R.string.shop_setting_subcategories));
        tvProperty.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvProperty.setTextColor(getColor(R.color.common_black));

        ivSubClass.setBackgroundResource(R.drawable.icon_right_arrow);


        tvQtyOfSubClass.setText(getString(R.string.shop_setting_qty_of_subcategories));
        tvQtyOfSubClass.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvQtyOfSubClass.setTextColor(getColor(R.color.common_black));

        ivQtyOfSubClass.setBackgroundResource(R.drawable.icon_right_arrow);
        tvPriceKey.setText(getString(R.string.shop_setting_price));
        tvPriceKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvPriceKey.setTextColor(getColor(R.color.common_black));

        etPriceValue.setText(Utils.get2DigitDecimalString(productPrice));
        etPriceValue.setTextSize(Constants.FONT_SIZE_NORMAL);

        etPriceValue.setBackgroundColor(getColor(R.color.common_white));
        etPriceValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        etPriceValue.setSingleLine();


        tvPriceCurrency.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvPriceCurrency.setTextColor(getColor(R.color.common_gray));
        tvPriceCurrency.setText(AppUtils.getCurrentCurrency(getActivity()));
        tvPriceCurrency.setVisibility(View.GONE);

        tvQtyKey.setText(getString(R.string.shop_setting_qty));
        tvQtyKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvQtyKey.setTextColor(getColor(R.color.common_black));

        etQtyValue.setText(productQty);
        etQtyValue.setTextSize(Constants.FONT_SIZE_NORMAL);

        etQtyValue.setBackgroundColor(getColor(R.color.common_white));
        etQtyValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        etQtyValue.setSingleLine();
        etQtyValue.setInputType(InputType.TYPE_CLASS_NUMBER);

        tvCodeKey.setText(getString(R.string.shop_setting_code));
        tvCodeKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvCodeKey.setTextColor(getColor(R.color.common_black));

        ivCodeIcon.setBackgroundResource(R.drawable.icon_code_of_subclass);

        etCodeValue.setText(productCode);
        etCodeValue.setTextSize(Constants.FONT_SIZE_NORMAL);

        etCodeValue.setBackgroundColor(getColor(R.color.common_white));
        etCodeValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        etCodeValue.setSingleLine();

        // btnDelete.setBackgroundColor(getColor(R.color.common_red));


        if (StringUtils.isNotEmpty(productQty) && Integer.valueOf(productQty) > 0) {
            etQtyValue.setTextColor(getColor(R.color.common_gray));
        } else {
            etQtyValue.setTextColor(getColor(R.color.common_red));
        }

    }


    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getString(R.string.shop_setting_product_edit) + shopName);
        changeState();
        getTvRight().setOnClickListener(okListener);

    }


    private void changeState() {
        if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
            btnDelete.setVisibility(View.GONE);
            getTvRight().setBackground(null);
            getTvRight().setText(R.string.common_ok);

            swEnableSubClass.setEnabled(true);
            etProductNameValue.setEnabled(true);
            unlimitedQtyCSBtn.setEnabled(true);
            etPriceValue.setEnabled(true);
            etQtyValue.setEnabled(true);
            etCodeValue.setEnabled(true);

            if (swEnableSubClass.isChecked()) {
                etQtyValue.setBackgroundColor(getColor(R.color.common_deep_gray));
                etQtyValue.setEnabled(false);
            } else {
                if (!unlimitedQtyCSBtn.isChecked()) {
                    etQtyValue.setBackgroundColor(getColor(R.color.common_white));
                    etQtyValue.setEnabled(true);
                }

            }

//            rlEnableSubClass.setVisibility(View.GONE);
//            rlProperty.setVisibility(View.GONE);
//            rlQtyOfSubClass.setVisibility(View.GONE);

        } else if (getFragmentMode() == FragmentMode.FragmentModeEdit) {
            rlEnableSubClass.setVisibility(View.VISIBLE);
            rlProperty.setVisibility(View.VISIBLE);
            rlQtyOfSubClass.setVisibility(View.VISIBLE);

            getTvRight().setText(R.string.common_ok);

            swEnableSubClass.setEnabled(true);
            unlimitedQtyCSBtn.setEnabled(true);
            etProductNameValue.setEnabled(true);
            etPriceValue.setEnabled(true);
            etQtyValue.setEnabled(true);
            etCodeValue.setEnabled(true);

            if (swEnableSubClass.isChecked()) {
                etQtyValue.setBackgroundColor(getColor(R.color.common_deep_gray));
                etQtyValue.setEnabled(false);
            } else {
                if (!unlimitedQtyCSBtn.isChecked()) {
                    etQtyValue.setBackgroundColor(getColor(R.color.common_white));
                    etQtyValue.setEnabled(true);
                }else{
                    etQtyValue.setBackgroundColor(getColor(R.color.common_deep_gray));
                    etQtyValue.setEnabled(false);
                    etQtyValue.setTextColor(getColor(R.color.common_gray));

                }
            }
        } else {
            btnDelete.setVisibility(View.GONE);

            if (swEnableSubClass.isChecked()) {
                etQtyValue.setBackgroundColor(getColor(R.color.common_deep_gray));
                etQtyValue.setEnabled(false);
            } else {
                if (!unlimitedQtyCSBtn.isChecked()) {
                    etQtyValue.setBackgroundColor(getColor(R.color.common_white));
                    etQtyValue.setEnabled(true);
                }else{
                    etQtyValue.setBackgroundColor(getColor(R.color.common_deep_gray));
                    etQtyValue.setEnabled(false);
                    etQtyValue.setTextColor(getColor(R.color.common_gray));

                }
            }

            rlEnableSubClass.setVisibility(View.VISIBLE);
            rlProperty.setVisibility(View.VISIBLE);
            rlQtyOfSubClass.setVisibility(View.VISIBLE);

            getTvRight().setBackgroundResource(R.drawable.administration_icon_edit);
            getTvRight().setText(Constants.STR_EMPTY);

            swEnableSubClass.setEnabled(false);
            etProductNameValue.setEnabled(false);
            etPriceValue.setEnabled(false);
            etQtyValue.setEnabled(false);
            etCodeValue.setEnabled(false);
            unlimitedQtyCSBtn.setEnabled(false);

            if (StringUtils.isNotEmpty(productQty) && Integer.valueOf(productQty) > 0) {
                etQtyValue.setTextColor(getColor(R.color.common_gray));
            } else {
                etQtyValue.setTextColor(getColor(R.color.common_red));
            }

            if (swEnableSubClass.isChecked()) {
                etQtyValue.setBackgroundColor(getColor(R.color.common_deep_gray));
                etQtyValue.setEnabled(false);
                etQtyValue.setTextColor(getColor(R.color.common_gray));
            } else {
                if (!unlimitedQtyCSBtn.isChecked()) {
                    etQtyValue.setBackgroundColor(getColor(R.color.common_white));
                    etQtyValue.setEnabled(true);
                }else{
                    etQtyValue.setBackgroundColor(getColor(R.color.common_deep_gray));
                    etQtyValue.setEnabled(false);
                    etQtyValue.setTextColor(getColor(R.color.common_gray));

                }
            }
        }
    }

    /**
     * delete product data.
     */
    private void netLinkProductDelete() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_PRODUCT_ID, productId);

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShopsProductEditFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20403_DELETE_SUCCESSFULLY) {
                    doBack();
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                Log.e("syb:error", error.getMessage());
            }
        };
        hh.startDelete(getActivity(), ApiManager.HttpApi.ShopProductDel, params);
    }


    /**
     * save Product data. (Put)
     */
    private void netLinkProductModify() {


        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_PRODUCT_ID, productId);
        params.put(ApiKey.SHOPS_PRODUCT_NAME, etProductNameValue.getText().toString());
        params.put(ApiKey.SHOPS_PRODUCT_QTY, etQtyValue.getText().toString());
        if (Utils.isStringNotNullOrEmpty(etCodeValue.getText().toString())) {
            params.put(ApiKey.SHOPS_PRODUCT_CODE, etCodeValue.getText().toString());
        }
        params.put(ApiKey.SHOPS_PRODUCT_PRICE, etPriceValue.getValue());

        String swLogoutAfterStatus;
        if (swEnableSubClass.isChecked()) {
            swLogoutAfterStatus = Constants.STR_1;
        } else {
            swLogoutAfterStatus = Constants.STR_0;
        }
        params.put(ApiKey.SHOPS_PRODUCT_ENABLE_PROPERTY, swLogoutAfterStatus);
        if (unlimitedQtyCSBtn.isChecked()) {
            params.put(ApiKey.SHOPS_UNLIMITED_FLAG, Constants.UNLIMITED_FLAG_ON);

        } else {
            params.put(ApiKey.SHOPS_UNLIMITED_FLAG, Constants.UNLIMITED_FLAG_OFF);
        }

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShopsProductEditFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    doBack();
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                Log.e("syb:error", error.getMessage());
            }
        };
        hh.startPut(getActivity(), ApiManager.HttpApi.ShopProductPut, params);
    }


    /**
     * add Product data. (add)
     * type = 1 no doback
     */
    private void netLinkProductAdd(final int type) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_PRODUCT_TYPE, productType);
        params.put(ApiKey.SHOPS_PRODUCT_NAME, etProductNameValue.getText().toString());
        params.put(ApiKey.SHOPS_PRODUCT_QTY, etQtyValue.getText().toString());
        if (Utils.isStringNotNullOrEmpty(etCodeValue.getText().toString())) {
            params.put(ApiKey.SHOPS_PRODUCT_CODE, etCodeValue.getText().toString());
        }
        params.put(ApiKey.SHOPS_PRODUCT_PRICE, etPriceValue.getValue());

        params.put(ApiKey.SHOPS_PRODUCT_ENABLE_PROPERTY, Constants.STR_0);

        String swLogoutAfterStatus;
        if (swEnableSubClass.isChecked()) {
            swLogoutAfterStatus = Constants.STR_1;
        } else {
            swLogoutAfterStatus = Constants.STR_0;
        }
        params.put(ApiKey.SHOPS_PRODUCT_ENABLE_PROPERTY, swLogoutAfterStatus);
        if (unlimitedQtyCSBtn.isChecked()) {
            params.put(ApiKey.SHOPS_UNLIMITED_FLAG, Constants.UNLIMITED_FLAG_ON);

        } else {
            params.put(ApiKey.SHOPS_UNLIMITED_FLAG, Constants.UNLIMITED_FLAG_OFF);
        }

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShopsProductEditFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY) {
                    if (type != 1) {

                        doBack();
                    } else {

                        productId = jo.getAddId();
                        setFragmentMode(FragmentMode.FragmentModeEdit);
                    }

                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                Log.e("syb:error", error.getMessage());
            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.ShopProductPost, params);
    }

    private boolean doCheck() {
        if (Utils.isStringNullOrEmpty(etProductNameValue.getText().toString())) {
            Utils.showShortToast(getActivity(), getResources().getString(R.string.shop_setting_enter_product_name));
            return false;
        }

        if (Utils.isStringNullOrEmpty(etPriceValue.getValue())) {
            Utils.showShortToast(getActivity(), getResources().getString(R.string.shop_setting_enter_price));
            return false;
        }
        if (!unlimitedQtyCSBtn.isChecked()) {
            if (Utils.isStringNullOrEmpty(etQtyValue.getText().toString())) {
                Utils.showShortToast(getActivity(), getResources().getString(R.string.shop_setting_enter_qty));
                return false;
            }
        }
        return true;
    }
}