/**
 * Project Name: itee
 * File Name:	 ShopsRentalEditFragment.java
 * Package Name: cn.situne.itee.fragment.shops
 * Date:		 2015-04-02
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.shops;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import cn.situne.itee.manager.jsonentity.JsonProductDetailGet;
import cn.situne.itee.view.CheckSwitchButton;
import cn.situne.itee.view.IteeButton;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeIntegerEditText;
import cn.situne.itee.view.IteeMoneyEditText;
import cn.situne.itee.view.IteeRedDeleteButton;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.AddCoursePopupWindow;
import cn.situne.itee.view.popwindow.SelectTimePopupWindow;

/**
 * ClassName:ShopsRentalEditFragment <br/>
 * Function: rental edit. <br/>
 * Date: 2015-04-02 <br/>
 * UI:05-3-1
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class ShopsRentalEditFragment extends BaseEditFragment {

    private final static String RENTAL_RETURN_TIME = "2h0m";

    private String productName;
    private LinearLayout mBody;
    private IteeTextView etProductName;
    private IteeTextView etReturnTime;
    private CheckSwitchButton reserveCSBtn;
    private CheckSwitchButton propertyCSBtn;
    private CheckSwitchButton pricePropertyCSBtn;


    private CheckSwitchButton unlimitedQtyCSBtn;
    private IteeMoneyEditText etPrice;
    private IteeIntegerEditText etQty;
    private IteeEditText edCode;
    private List<RowLayout> bodyViews;
    private ArrayList<String> propertyPicIdList;
    private IteeRedDeleteButton btnDelete;

    private String productId;


    private int productPicId;
    private AddCoursePopupWindow addCoursePopupWindow;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_shops_rental_edit;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            setFragmentMode(BaseEditFragment.FragmentMode.valueOf(bundle.getInt(TransKey.COMMON_FRAGMENT_MODE)));
            propertyPicIdList = bundle.getStringArrayList(TransKey.SHOPS_PROPERTY_PIC_IDS);
            productId = bundle.getString(TransKey.SHOPS_PRODUCT_ID);
        }

        bodyViews = new ArrayList<>();
        mBody = (LinearLayout) rootView.findViewById(R.id.body);
        etProductName = new IteeTextView(getBaseActivity());
        mBody.addView(getLineLayoutTypeTextView(getString(R.string.shop_setting_product_name), etProductName));
        mBody.addView(AppUtils.getSeparatorLine(ShopsRentalEditFragment.this));
        etPrice = new IteeMoneyEditText(ShopsRentalEditFragment.this);
        etPrice.setHint(getString(R.string.shop_setting_price));

        mBody.addView(getLineLayoutTypeTextView(getString(R.string.shop_setting_price), etPrice));

        mBody.addView(AppUtils.getSeparatorLine(ShopsRentalEditFragment.this));
        unlimitedQtyCSBtn = new CheckSwitchButton(this);
        mBody.addView(getLineLayoutTypeNewAddCheckSwitchButton(getString(R.string.shop_setting_unlimited_qty), unlimitedQtyCSBtn));


        mBody.addView(AppUtils.getSeparatorLine(ShopsRentalEditFragment.this));
        etQty = new IteeIntegerEditText(this);
        mBody.addView(getLineLayoutTypeTextView(getString(R.string.shop_setting_qty), etQty));
        etQty.setHint(getString(R.string.shop_setting_qty));
        mBody.addView(AppUtils.getSeparatorLine(ShopsRentalEditFragment.this));
        etQty.addTextChangedListener(new AppUtils.EditViewIntegerWatcher(etQty));

        edCode = new IteeEditText(getBaseActivity());
        RowLayout codeLayout = new RowLayout(getBaseActivity(), getActualHeightOnThisDevice(100));
        RelativeLayout.LayoutParams edParams
                = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(300), getActualHeightOnThisDevice(100));
        edParams.rightMargin = getActualWidthOnThisDevice(20);
        edParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        edCode.setLayoutParams(edParams);
//        edCode.setMinWidth(getActualWidthOnThisDevice(30));
        edCode.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        edCode.setHint(getString(R.string.shop_setting_code));

        IteeTextView codeKey = getKeyTextView(getString(R.string.shop_setting_code));
        codeKey.setId(View.generateViewId());
        codeLayout.addView(codeKey);
        RelativeLayout.LayoutParams btnParams
                = new RelativeLayout.LayoutParams(getActualHeightOnThisDevice(65), getActualHeightOnThisDevice(65));
        btnParams.addRule(RelativeLayout.RIGHT_OF, codeKey.getId());
        btnParams.leftMargin = getActualWidthOnThisDevice(10);
        btnParams.addRule(RelativeLayout.CENTER_VERTICAL);

        IteeButton codeBtn = new IteeButton(getBaseActivity());
        codeBtn.setLayoutParams(btnParams);
        codeBtn.setBackgroundResource(R.drawable.icon_shops_code);

        codeLayout.addView(edCode);
        codeLayout.addView(codeBtn);
        codeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                    Intent intent = new Intent();
                    intent.setClass(getBaseActivity(), ScanQrCodeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, ScanQrCodeActivity.SCANNING_GREQUEST_CODE);
                }
            }
        });
        bodyViews.add(codeLayout);
        codeLayout.addShadeView();
        mBody.addView(codeLayout);
        mBody.addView(AppUtils.getSeparatorLine(ShopsRentalEditFragment.this));
        etReturnTime = new IteeTextView(getBaseActivity());

        RelativeLayout returnTimeLayout = getLineLayoutTypeTextView(getString(R.string.shop_setting_return_time), etReturnTime);
        returnTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.hideKeyboard(getBaseActivity());
                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                    String str = etReturnTime.getText().toString();
                    if (str.length() <= 0) {
                        str = RENTAL_RETURN_TIME;
                    }

                    str = str.replace("h", Constants.STR_COLON);
                    str = str.replace("m", Constants.STR_COLON);
                    String[] times = str.split(Constants.STR_COLON);

                    final SelectTimePopupWindow popupWindow = new SelectTimePopupWindow(getActivity(), times, 1);
                    popupWindow.showAtLocation(getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    popupWindow.btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            etReturnTime.setText(popupWindow.wheelViewHour.getCurrentItem() + "h" + popupWindow.wheelViewMin.getCurrentItem() + "m");
                            popupWindow.dismiss();
                        }
                    });
                }
            }


        });
        mBody.addView(returnTimeLayout);
        mBody.addView(AppUtils.getSeparatorLine(ShopsRentalEditFragment.this));
        reserveCSBtn = new CheckSwitchButton(this);
        mBody.addView(getLineLayoutTypeCheckSwitchButton(getString(R.string.shop_setting_reserve_product), reserveCSBtn));
        mBody.addView(AppUtils.getSeparatorLine(ShopsRentalEditFragment.this));
        propertyCSBtn = new CheckSwitchButton(this);
        mBody.addView(getLineLayoutTypeCheckSwitchButton(getString(R.string.shop_setting_enable_subcategories), propertyCSBtn));
        mBody.addView(AppUtils.getSeparatorLine(ShopsRentalEditFragment.this));
        pricePropertyCSBtn = new CheckSwitchButton(this);
        mBody.addView(getLineLayoutTypeCheckSwitchButton(getString(R.string.shop_setting_enable_price_of_subcategories), pricePropertyCSBtn));

        mBody.addView(AppUtils.getSeparatorLine(ShopsRentalEditFragment.this));
        mBody.addView(getLineLayoutTypeClick(getString(R.string.shop_setting_subcategories)));

        mBody.addView(AppUtils.getSeparatorLine(ShopsRentalEditFragment.this));
        mBody.addView(getLineLayoutTypeClick(getString(R.string.shop_setting_price_of_subcategories)));

        mBody.addView(AppUtils.getSeparatorLine(ShopsRentalEditFragment.this));
        mBody.addView(getLineLayoutTypeClick(getString(R.string.shop_setting_qty_of_subcategories)));

        mBody.addView(AppUtils.getSeparatorLine(ShopsRentalEditFragment.this));
        btnDelete = new IteeRedDeleteButton(getBaseActivity());
        btnDelete.setText(R.string.common_delete);
        LinearLayout.LayoutParams delLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout delLayout = new LinearLayout(getBaseActivity());
        delLayout.setGravity(Gravity.CENTER);
        delLayout.setLayoutParams(delLayoutParams);
        delLayout.setBackgroundColor(getColor(R.color.common_light_gray));

        LinearLayout.LayoutParams btnDeleteParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        btnDeleteParams.width = AppUtils.getLargerButtonWidth(this);
        btnDeleteParams.height = AppUtils.getLargerButtonHeight(this);
        btnDeleteParams.topMargin = getActualHeightOnThisDevice(30);
        btnDeleteParams.bottomMargin = getActualHeightOnThisDevice(30);


        btnDelete.setLayoutParams(btnDeleteParams);

        delLayout.addView(btnDelete);
        mBody.addView(delLayout);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppUtils.showDeleteAlert(ShopsRentalEditFragment.this, new AppUtils.DeleteConfirmListener() {
                    @Override
                    public void onClickDelete() {
                        deleteRentalProduct();
                    }
                });
            }
        });
    }

    public boolean doCheck() {
        if (Utils.isStringNullOrEmpty(etPrice.getValue())
                || etPrice.getValue().equals(AppUtils.getCurrentCurrency(getActivity()))) {
            Utils.showShortToast(getActivity(), AppUtils.generateNotNullMessage(this, R.string.shop_setting_price));
            return false;
        }
        if (!unlimitedQtyCSBtn.isChecked()) {
            if (Constants.STR_EMPTY.equals(etQty.getText().toString()) || Integer.parseInt(etQty.getText().toString()) <= 0) {
                Utils.showShortToast(getActivity(), AppUtils.generateNotNullMessage(this, R.string.shop_setting_qty));
                return false;
            }
        }
        if (Constants.STR_EMPTY.equals(etReturnTime.getText().toString())) {
            Utils.showShortToast(getActivity(), AppUtils.generateNotNullMessage(this, R.string.shop_setting_return_time));
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ScanQrCodeActivity.SCANNING_GREQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String qrCode = bundle.getString(TransKey.QR_CODE);

                    edCode.setText(qrCode);
                }
                break;
        }
    }

    @Override
    protected void reShowWithBackValue() {
        Bundle bundle = getReturnValues();

        if (bundle != null) {
            String qytTotal = bundle.getString(TransKey.SHOPS_QTY_TOTAL, Constants.STR_COMMA);

            if (!Constants.STR_COMMA.equals(qytTotal)) {
                etQty.setText(qytTotal);
            }
        }
    }

    private void deleteRentalProduct() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_PRODUCT_ID, productId);


        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShopsRentalEditFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                Integer returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20403_DELETE_SUCCESSFULLY) {
                    getBaseActivity().doBackWithRefresh();
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }

            }

            @Override
            public void onJsonError(VolleyError error) {
                Utils.showShortToast(getActivity(), String.valueOf(error));
            }
        };
        hh.startDelete(getActivity(), ApiManager.HttpApi.ShopsRentalProductDel, params);
    }

    private void getProductDetail() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_PRODUCT_ID, productId);
        HttpManager<JsonProductDetailGet> hh = new HttpManager<JsonProductDetailGet>(ShopsRentalEditFragment.this) {

            @Override
            public void onJsonSuccess(JsonProductDetailGet jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    setViewText(jo);
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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.ShopsProductDetailGet, params);


    }

    private void setViewText(JsonProductDetailGet jo) {

        etProductName.setText(jo.getProductName());
        productPicId = jo.getProductPicId();
        etProductName.setCompoundDrawablesWithIntrinsicBounds(getTypeIcon(String.valueOf(jo.getProductPicId())), 0, 0, 0);
        etPrice.setValue(Utils.get2DigitDecimalString(jo.getPrice()));

        etQty.setText(String.valueOf(jo.getQty()));
        edCode.setText(jo.getCode());
        etReturnTime.setText(AppUtils.getStringHmWithMinute(jo.getReturnTime()));

        int reserveStatus = jo.getReserveStatus();
        int propertyPriceStatus = jo.getPropertyPriceStatus();
        int propertyStatus = jo.getPropertyStatus();

        if (reserveStatus == Constants.SWITCH_RIGHT) {
            reserveCSBtn.setChecked(true);
        } else {
            reserveCSBtn.setChecked(false);
        }

        RowLayout priceRow = bodyViews.get(1);
        RowLayout priceOfPropertyRow = bodyViews.get(9);
        if (propertyPriceStatus == Constants.SWITCH_RIGHT) {
            priceRow.layoutDisable();
            priceOfPropertyRow.layoutLiftedDisable();
            pricePropertyCSBtn.setChecked(true);
        } else {
            pricePropertyCSBtn.setChecked(false);
            priceRow.layoutLiftedDisable();
            priceOfPropertyRow.layoutDisable();

        }

        RowLayout qtyRow = bodyViews.get(2);
        RowLayout propertyRow = bodyViews.get(8);
        RowLayout qtyOfPropertyRow = bodyViews.get(10);
        if (propertyStatus == Constants.SWITCH_RIGHT) {
            propertyCSBtn.setChecked(true);
            qtyRow.layoutDisable();
            etQty.setEnabled(false);
            qtyOfPropertyRow.layoutLiftedDisable();
            propertyRow.layoutLiftedDisable();
        } else {
            propertyCSBtn.setChecked(false);
            qtyRow.layoutLiftedDisable();
            etQty.setEnabled(true);
            qtyOfPropertyRow.layoutDisable();
            propertyRow.layoutDisable();

            pricePropertyCSBtn.setChecked(false);
            priceRow.layoutLiftedDisable();
            priceOfPropertyRow.layoutDisable();
        }

        unlimitedQtyCSBtn.setChecked(false);
        if (Constants.UNLIMITED_FLAG_ON.equals(jo.getUnlimitedFlag())) {
            unlimitedQtyCSBtn.setChecked(true);
            qtyRow.layoutDisable();
            etQty.setEnabled(false);
            qtyOfPropertyRow.layoutDisable();
        }

        setBrowseViewEnabled(false);
    }

    @Override
    protected void setDefaultValueOfControls() {
        if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
            etProductName.setText(getString(getTypeName(String.valueOf(propertyPicIdList.get(0)))));
            etProductName.setCompoundDrawablesWithIntrinsicBounds(getTypeIcon(String.valueOf(propertyPicIdList.get(0))), 0, 0, 0);
            propertyCSBtn.setEnabled(false);
            pricePropertyCSBtn.setEnabled(false);
            unlimitedQtyCSBtn.setEnabled(false);
            productName = getString(getTypeName(propertyPicIdList.get(0)));
            productPicId = Integer.parseInt(propertyPicIdList.get(0));
        }

        setBrowseViewEnabled(true);
        if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
            setBrowseViewEnabled(false);
        }
    }

    private void setBrowseViewEnabled(boolean b) {
        etProductName.setEnabled(b);
        etPrice.setEnabled(b);
        etQty.setEnabled(b);
        edCode.setEnabled(b);
        etReturnTime.setEnabled(b);
        reserveCSBtn.setEnabled(b);
        pricePropertyCSBtn.setEnabled(b);
        propertyCSBtn.setEnabled(b);
        btnDelete.setEnabled(b);

        unlimitedQtyCSBtn.setEnabled(b);
    }

    @Override
    protected void setListenersOfControls() {
        addCoursePopupWindow = new AddCoursePopupWindow(getActivity(), null);
        addCoursePopupWindow.setNotTouchCancel(true);
        propertyCSBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RowLayout qtyRow = bodyViews.get(2);
                RowLayout propertyRow = bodyViews.get(8);
                RowLayout qtyOfPropertyRow = bodyViews.get(10);
                RowLayout priceRow = bodyViews.get(1);
                RowLayout priceOfPropertyRow = bodyViews.get(9);

                if (isChecked) {
                    qtyRow.layoutDisable();
                    etQty.setEnabled(false);
                    if (!unlimitedQtyCSBtn.isChecked()) {
                        qtyOfPropertyRow.layoutLiftedDisable();
                    }
                    propertyRow.layoutLiftedDisable();
                    pricePropertyCSBtn.setEnabled(true);

                    if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
                        pricePropertyCSBtn.setEnabled(false);
                    }


                    if (Utils.isStringNullOrEmpty(productId)) {

                        if (doCheck()) {

                            addCoursePopupWindow.showAtLocation(ShopsRentalEditFragment.this.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                            addCoursePopupWindow.tvEvent.setVisibility(View.GONE);
                            addCoursePopupWindow.tvContent.setText(getString(R.string.common_add) + productName);

                            addCoursePopupWindow.btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    addCoursePopupWindow.dismiss();
                                    submitRentalData(1);

                                }
                            });

                            addCoursePopupWindow.btn_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    addCoursePopupWindow.dismiss();

                                    propertyCSBtn.setChecked(false);

                                    RowLayout qtyRow = bodyViews.get(2);
                                    RowLayout propertyRow = bodyViews.get(8);
                                    RowLayout qtyOfPropertyRow = bodyViews.get(10);
                                    RowLayout priceRow = bodyViews.get(1);
                                    RowLayout priceOfPropertyRow = bodyViews.get(9);
                                    qtyRow.layoutLiftedDisable();
                                    etQty.setEnabled(true);
                                    qtyOfPropertyRow.layoutDisable();
                                    propertyRow.layoutDisable();

                                    pricePropertyCSBtn.setChecked(false);
                                    priceRow.layoutLiftedDisable();
                                    priceOfPropertyRow.layoutDisable();
                                }
                            });

                        } else {
                            propertyCSBtn.setChecked(false);

                            qtyRow.layoutLiftedDisable();
                            etQty.setEnabled(true);
                            qtyOfPropertyRow.layoutDisable();
                            propertyRow.layoutDisable();

                            pricePropertyCSBtn.setChecked(false);
                            pricePropertyCSBtn.setEnabled(false);
                            priceRow.layoutLiftedDisable();
                            priceOfPropertyRow.layoutDisable();
                        }
                    }


                } else {
                    if (!unlimitedQtyCSBtn.isChecked()) {
                        qtyRow.layoutLiftedDisable();
                        etQty.setEnabled(true);
                        qtyOfPropertyRow.layoutDisable();
                        propertyRow.layoutDisable();
                    }

                    pricePropertyCSBtn.setChecked(false);
                    pricePropertyCSBtn.setEnabled(false);
                    priceRow.layoutLiftedDisable();
                    priceOfPropertyRow.layoutDisable();
                }
            }
        });

        pricePropertyCSBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                RowLayout priceRow = bodyViews.get(1);
                RowLayout priceOfPropertyRow = bodyViews.get(9);

                if (isChecked) {
                    priceRow.layoutDisable();
                    priceOfPropertyRow.layoutLiftedDisable();
                } else {
                    priceRow.layoutLiftedDisable();
                    priceOfPropertyRow.layoutDisable();
                }
            }
        });


        unlimitedQtyCSBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                RowLayout qtyRow = bodyViews.get(2);
                RowLayout qtyOfPropertyRow = bodyViews.get(10);
                if (isChecked) {
                    qtyRow.layoutDisable();
                    qtyOfPropertyRow.layoutDisable();
                    etQty.setEnabled(false);
                } else {

                    if (!propertyCSBtn.isChecked()) {
                        qtyRow.layoutLiftedDisable();
                        etQty.setEnabled(true);
                    } else {
                        qtyOfPropertyRow.layoutLiftedDisable();

                    }
                }
            }
        });


        if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
            reserveCSBtn.setChecked(false);
            propertyCSBtn.setChecked(false);
            pricePropertyCSBtn.setChecked(false);
            unlimitedQtyCSBtn.setChecked(false);
            RowLayout propertyRow = bodyViews.get(8);
            RowLayout qtyOfPropertyRow = bodyViews.get(10);
            RowLayout pricePropertyRow = bodyViews.get(9);
            propertyRow.layoutDisable();
            qtyOfPropertyRow.layoutDisable();
            pricePropertyRow.layoutDisable();
            btnDelete.setVisibility(View.GONE);
        }

        etProductName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(propertyPicIdList == null||propertyPicIdList.size() == 0){

                    Utils.showShortToast(getBaseActivity(),R.string.shops_rental_mes);

                }else{
                    ShopsSelectRentalProductFragment.createBuilder(ShopsRentalEditFragment.this, getFragmentManager()).setSelectedListener(new ShopsSelectRentalProductFragment.SelectedShopsRentalProductType() {
                        @Override
                        public void onSelected(String rentalTypeId, int stringResId, int iconResId) {
                            productName = getString(stringResId);
                            productPicId = Integer.parseInt(rentalTypeId);
                            etProductName.setText(productName);
                            etProductName.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
                        }
                    }).setRentalProductIds(propertyPicIdList).show();

                }


            }
        });

    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {


        if (getFragmentMode() != FragmentMode.FragmentModeAdd) {
            getProductDetail();
        }


        etPrice.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        etQty.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        edCode.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getString(R.string.shop_setting_product_edit_rental));
        if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
            getTvRight().setBackgroundResource(R.drawable.icon_common_edit);
            btnDelete.setVisibility(View.GONE);
        } else {

            getTvRight().setText(R.string.common_ok);
            getTvRight().setBackground(null);
        }

        getTvRight().setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                btnDelete.setEnabled(true);

                if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
                    getTvRight().setBackground(null);
                    getTvRight().setText(R.string.common_ok);
                    setFragmentMode(FragmentMode.FragmentModeEdit);
                    btnDelete.setVisibility(View.VISIBLE);
                    setBrowseViewEnabled(true);
                } else {
                    v.setFocusable(true);
                    v.setFocusableInTouchMode(true);
                    Utils.hideKeyboard(getBaseActivity());
                    if (doCheck()) {
                        submitRentalData(0);
                    }
                }
            }
        });
    }


    //type = 1 not back
    private void submitRentalData(final int type) {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));

        params.put(ApiKey.SHOPS_PRODUCT_SORT, String.valueOf(productPicId));
        params.put(ApiKey.SHOPS_PRODUCT_PRICE, String.valueOf(etPrice.getValue()));

        params.put(ApiKey.SHOPS_PRODUCT_NAME_SUBMIT, etProductName.getText().toString());

        params.put(ApiKey.SHOPS_PRODUCT_CODE, edCode.getText().toString());
        params.put(ApiKey.SHOPS_PRODUCT_QTY, etQty.getText().toString());
        params.put(ApiKey.SHOPS_RETURN_TIME, String.valueOf(AppUtils.getMinuteWithHmString(etReturnTime.getText().toString())));

        String reserveStr = Constants.STR_0;
        if (reserveCSBtn.isChecked()) {
            reserveStr = Constants.STR_1;
        }
        params.put(ApiKey.SHOPS_RESERVE_STATUS, reserveStr);
        if (getFragmentMode() != FragmentMode.FragmentModeAdd) {

            String propertyStr = Constants.STR_0;
            String pricePropertyStr = Constants.STR_0;

            if (propertyCSBtn.isChecked()) {
                propertyStr = Constants.STR_1;
            }
            if (pricePropertyCSBtn.isChecked()) {
                pricePropertyStr = Constants.STR_1;
            }
            params.put(ApiKey.SHOPS_PRODUCT_ID, productId);

            if (unlimitedQtyCSBtn.isChecked()) {
                params.put(ApiKey.SHOPS_UNLIMITED_FLAG, Constants.UNLIMITED_FLAG_ON);

            } else {
                params.put(ApiKey.SHOPS_UNLIMITED_FLAG, Constants.UNLIMITED_FLAG_OFF);
            }


            params.put(ApiKey.SHOPS_RESERVE_PROPERTY_STATUS, propertyStr);
            params.put(ApiKey.SHOPS_RESERVE_PRICE_PROPERTY_STATUS, pricePropertyStr);
        }

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShopsRentalEditFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                Utils.showShortToast(getActivity(), msg);
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY || returnCode == Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY) {
                    if (type != 1) {
                        doBackWithRefresh();
                    } else {
                        productId = jo.getAddId();
                        setFragmentMode(FragmentMode.FragmentModeEdit);
                    }
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

        if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
            postRental(hh, params);
        } else {
            putRental(hh, params);
        }
    }


    private void postRental(HttpManager<BaseJsonObject> hh, Map<String, String> params) {
        hh.start(getActivity(), ApiManager.HttpApi.ShopsRentalProductPost, params);
    }

    private void putRental(HttpManager<BaseJsonObject> hh, Map<String, String> params) {
        hh.startPut(getActivity(), ApiManager.HttpApi.ShopsRentalProductPut, params);
    }


    private int getTypeName(String typeId) {
        int res;
        if (Constants.RENTAL_PRODUCT_TYPE_CADDIE.equals(typeId)) {
            res = R.string.rental_type_caddie;
        } else if (Constants.RENTAL_PRODUCT_TYPE_CART.equals(typeId)) {
            res = R.string.rental_type_cart;
        } else if (Constants.RENTAL_PRODUCT_TYPE_CLUBS.equals(typeId)) {
            res = R.string.rental_type_clubs;
        } else if (Constants.RENTAL_PRODUCT_TYPE_SHOES.equals(typeId)) {
            res = R.string.rental_type_shoes;
        } else if (Constants.RENTAL_PRODUCT_TYPE_TROLLEY.equals(typeId)) {
            res = R.string.rental_type_trolley;
        } else if (Constants.RENTAL_PRODUCT_TYPE_UMBRELLA.equals(typeId)) {
            res = R.string.rental_type_umbrella;
        } else {
            res = R.string.rental_type_towel;
        }
        return res;
    }

    private int getTypeIcon(String typeId) {
        int res;
        if (Constants.RENTAL_PRODUCT_TYPE_CADDIE.equals(typeId)) {
            res = R.drawable.icon_shops_product_edit_caddie_select;
        } else if (Constants.RENTAL_PRODUCT_TYPE_CART.equals(typeId)) {
            res = R.drawable.icon_shops_product_edit_cart_select;
        } else if (Constants.RENTAL_PRODUCT_TYPE_CLUBS.equals(typeId)) {
            res = R.drawable.icon_shops_product_edit_clubs_select;
        } else if (Constants.RENTAL_PRODUCT_TYPE_SHOES.equals(typeId)) {
            res = R.drawable.icon_shops_product_edit_shoes_select;
        } else if (Constants.RENTAL_PRODUCT_TYPE_TROLLEY.equals(typeId)) {
            res = R.drawable.icon_shops_product_edit_trolley_select;
        } else if (Constants.RENTAL_PRODUCT_TYPE_UMBRELLA.equals(typeId)) {
            res = R.drawable.icon_shops_product_edit_umbrella_select;
        } else {
            res = R.drawable.icon_shops_product_edit_towel_select;
        }
        return res;
    }

    private RelativeLayout getLineLayoutTypeTextView(String key, TextView ed) {
        RowLayout res = new RowLayout(getBaseActivity(), getActualHeightOnThisDevice(100));
        int width = getActualWidthOnThisDevice(320);
        if (ed == etProductName) {
            width = WRAP_CONTENT;
        }
        RelativeLayout.LayoutParams edParams = new RelativeLayout.LayoutParams(width, getActualHeightOnThisDevice(100));
        edParams.rightMargin = getActualWidthOnThisDevice(40);
        edParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ed.setLayoutParams(edParams);
        ed.setMinWidth(getActualWidthOnThisDevice(30));
        ed.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);

        res.addView(getKeyTextView(key));
        res.addView(ed);

        bodyViews.add(res);
        res.addShadeView();
        return res;
    }

    private RelativeLayout getLineLayoutTypeCheckSwitchButton(String key, CheckSwitchButton csBtn) {
        RowLayout res = new RowLayout(getBaseActivity(), getActualHeightOnThisDevice(100));
        res.addView(getKeyTextView(key));

        RelativeLayout.LayoutParams csBtnParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, getActualHeightOnThisDevice(100));
        csBtnParams.rightMargin = getActualWidthOnThisDevice(40);
        csBtnParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        csBtnParams.addRule(RelativeLayout.CENTER_VERTICAL);
        csBtn.setLayoutParams(csBtnParams);
        res.addView(csBtn);
        bodyViews.add(res);
        res.addShadeView();
        return res;
    }


    private RelativeLayout getLineLayoutTypeNewAddCheckSwitchButton(String key, CheckSwitchButton csBtn) {
        RowLayout res = new RowLayout(getBaseActivity(), getActualHeightOnThisDevice(100));
        res.addView(getKeyTextView(key));

        RelativeLayout.LayoutParams csBtnParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, getActualHeightOnThisDevice(100));
        csBtnParams.rightMargin = getActualWidthOnThisDevice(40);
        csBtnParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        csBtnParams.addRule(RelativeLayout.CENTER_VERTICAL);
        csBtn.setLayoutParams(csBtnParams);
        res.addView(csBtn);

        res.addShadeView();
        return res;
    }

    private RelativeLayout getLineLayoutTypeClick(String key) {

        RowLayout res = new RowLayout(getBaseActivity(), getActualHeightOnThisDevice(100));
        res.setTag(key);
        res.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = String.valueOf(v.getTag());
                Bundle bundle = new Bundle();

                bundle.putString(TransKey.SHOPS_PRODUCT_ID, productId);
                bundle.putString(TransKey.COMMON_FROM_PAGE, ShopsRentalEditFragment.class.getName());


                if (key.equals(getString(R.string.shop_setting_subcategories))) {
                    //  bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeAdd.value());
                    if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                        push(ShopsPropertyEditFragment.class, bundle);
                    }
                }

                if (key.equals(getString(R.string.shop_setting_price_of_subcategories))) {

                    bundle.putBoolean(TransKey.SHOPS_PACKAGE_IS_QTY, false);
                    if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                        push(ShopsPropertyPriceOrQtyEditFragment.class, bundle);
                    }
                }

                if (key.equals(getString(R.string.shop_setting_qty_of_subcategories))) {
                    bundle.putBoolean(TransKey.SHOPS_PACKAGE_IS_QTY, true);
                    if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                        push(ShopsPropertyPriceOrQtyEditFragment.class, bundle);
                    }
                }

            }
        });
        res.addView(getKeyTextView(key));
        bodyViews.add(res);
        res.addShadeView();
        return res;
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

    class RowLayout extends RelativeLayout {

        private LinearLayout shadeView;

        public RowLayout(Context context) {
            super(context);
        }

        public RowLayout(Context context, int height) {
            super(context);
            LinearLayout.LayoutParams myParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
            this.setLayoutParams(myParams);
            RelativeLayout.LayoutParams shadeViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
            shadeView = new LinearLayout(context);
            shadeView.setAlpha(0.8f);
            shadeView.setLayoutParams(shadeViewParams);
            shadeView.setBackgroundColor(getColor(R.color.common_gray));
            shadeView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void addShadeView() {
            this.addView(shadeView);
            shadeView.setVisibility(View.GONE);
        }

        public void layoutDisable() {
            shadeView.setVisibility(View.VISIBLE);
        }

        public void layoutLiftedDisable() {
            shadeView.setVisibility(View.GONE);
        }
    }
}