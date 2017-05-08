/**
 * Project Name: itee
 * File Name:	 ShopsPromoteEditFragment.java
 * Package Name: cn.situne.itee.fragment.shops
 * Date:		 2015-03-21
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.shops;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.activity.ScanQrCodeActivity;
import cn.situne.itee.activity.SelectPhotoActivity;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.ImageUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.ShopsProduct;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonPromote;
import cn.situne.itee.view.CheckSwitchButton;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeMoneyEditText;
import cn.situne.itee.view.IteeNetworkImageView;
import cn.situne.itee.view.IteeQtyEditText;
import cn.situne.itee.view.IteeRedDeleteButton;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.SelectDatePopupWindow;

/**
 * ClassName:ShopsPromoteEditFragment <br/>
 * Function: 新增编辑 Promote <br/>
 * UI:  05-05-01
 * Date: 2015-03-21 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */

public class ShopsPromoteEditFragment extends BaseEditFragment {

    private RelativeLayout rlPicture;
    private RelativeLayout rlChooseProduct;
    private RelativeLayout rlPromotePrice;
    private RelativeLayout rlUnlimitedQty;

    private RelativeLayout rlQty;
    private RelativeLayout rlCode;
    private RelativeLayout rlStartTime;
    private RelativeLayout rlEndTime;
    private RelativeLayout rlDelete;

    private IteeTextView tvPicture;
    private IteeNetworkImageView ivPicture;
    private IteeTextView tvChooseProductKey;
    private IteeTextView tvChooseProductValue;
    private IteeTextView tvPromotePriceKey;
    private IteeMoneyEditText etPromotePriceValue;

    private IteeTextView tvQtyKey;
    private IteeQtyEditText etQtyValue;

    private IteeTextView tvCodeKey;
    private ImageView ivCodeIcon;
    private IteeEditText etCodeValue;
    private IteeTextView tvStartTimeKey;
    private IteeTextView tvStartTimeValue;
    private IteeTextView tvEndTimeKey;
    private IteeTextView tvEndTimeValue;
    private IteeRedDeleteButton btDelete;
    private JsonPromote.Promote promote;
    private int popupDateWindowFlag = 0;
    private Bitmap isPhotoChange;
    private ArrayList<ShopsProduct> shopsProductList;
    private LinearLayout qtyShadeView;
    private SelectDatePopupWindow.OnDateSelectClickListener dateSelectReturn;

    private ImageView ivRightArrow;
    private CheckSwitchButton csbUnlimitedQty;
    private String selectQty;
    private String selectUnlimitedFlag;
    private String maxQty;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_promote_edit;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        shopsProductList = new ArrayList<>();
        maxQty = "-1";

        Bundle bundle = getArguments();
        if (bundle != null) {
            setFragmentMode(FragmentMode.valueOf(bundle.getInt(TransKey.COMMON_FRAGMENT_MODE)));
            promote = (JsonPromote.Promote) Utils.getObjectFromString(bundle.getString(TransKey.COMMON_PRODUCT));
            if (promote != null) {
                for (int i = 0; i < promote.getProductList().size(); i++) {
                    ShopsProduct shopsProduct = new ShopsProduct();
                    JsonPromote.Promote.Product product = promote.getProductList().get(i);

                    shopsProduct.setAttrId(product.getPromoteAttrId());
                    shopsProduct.setProductId(String.valueOf(product.getProductId()));
                    shopsProduct.setProductPrice(product.getPrice());
                    shopsProduct.setIsCaddie(Constants.STR_FLAG_YES.equals(promote.getCaddieFlag()));
                    shopsProductList.add(shopsProduct);
                }
            }
        }

        rlPicture = (RelativeLayout) rootView.findViewById(R.id.rl_picture);
        rlChooseProduct = (RelativeLayout) rootView.findViewById(R.id.rl_chooseProduct);
        rlPromotePrice = (RelativeLayout) rootView.findViewById(R.id.rl_promotePrice);
        rlQty = (RelativeLayout) rootView.findViewById(R.id.rl_qty);

        rlUnlimitedQty = (RelativeLayout) rootView.findViewById(R.id.rl_unlimited_qty);
        rlCode = (RelativeLayout) rootView.findViewById(R.id.rl_code);
        rlStartTime = (RelativeLayout) rootView.findViewById(R.id.rl_startTime);
        rlEndTime = (RelativeLayout) rootView.findViewById(R.id.rl_endTime);
        rlDelete = (RelativeLayout) rootView.findViewById(R.id.rl_delete);


        tvPicture = new IteeTextView(getActivity());
        ivPicture = new IteeNetworkImageView(getActivity());
        tvChooseProductKey = new IteeTextView(getActivity());
        tvChooseProductValue = new IteeTextView(getActivity());
        ivRightArrow = new ImageView(getActivity());
        tvPromotePriceKey = new IteeTextView(getActivity());
        etPromotePriceValue = new IteeMoneyEditText(this);

        tvQtyKey = new IteeTextView(getActivity());
        etQtyValue = new IteeQtyEditText(this);
        tvCodeKey = new IteeTextView(getActivity());
        ivCodeIcon = new ImageView(getActivity());
        etCodeValue = new IteeEditText(getActivity());
        tvStartTimeKey = new IteeTextView(getActivity());
        tvStartTimeValue = new IteeTextView(getActivity());
        tvEndTimeKey = new IteeTextView(getActivity());
        tvEndTimeValue = new IteeTextView(getActivity());
        btDelete = new IteeRedDeleteButton(getActivity());

        csbUnlimitedQty = new CheckSwitchButton(mContext);

        dateSelectReturn = new SelectDatePopupWindow.OnDateSelectClickListener() {
            @Override
            public void OnGoodItemClick(String flag, String content) {
                switch (flag) {
                    case Constants.DATE_RETURN:
                        if (popupDateWindowFlag == 0) {
                            tvStartTimeValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(content, getActivity()));
                        } else {
                            tvEndTimeValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(content, getActivity()));
                        }
                        break;
                }
            }
        };
    }

    @Override
    protected void setDefaultValueOfControls() {
        if (Utils.isListNotNullOrEmpty(shopsProductList)) {
            ShopsProduct shopsProduct = shopsProductList.get(0);
            if (shopsProduct.isCaddie()) {
                csbUnlimitedQty.setChecked(true);
                csbUnlimitedQty.setEnabled(false);
            } else {
                csbUnlimitedQty.setEnabled(true);
            }
        }
    }

    @Override
    protected void setListenersOfControls() {
        rlStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                    popupDateWindowFlag = 0;
                    String formatDate = tvStartTimeValue.getText().toString();
                    SelectDatePopupWindow selectDatePopupWindow
                            = new SelectDatePopupWindow(getActivity(), formatDate, dateSelectReturn);
                    View view = getView();
                    if (view != null) {
                        selectDatePopupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    }
                }
            }
        });

        rlEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                    popupDateWindowFlag = 1;
                    String formatDate = tvEndTimeValue.getText().toString();
                    SelectDatePopupWindow selectDatePopupWindow
                            = new SelectDatePopupWindow(getActivity(), formatDate, dateSelectReturn);
                    View view = getView();
                    if (view != null) {
                        selectDatePopupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    }
                }

            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentMode() == FragmentMode.FragmentModeEdit) {
                    AppUtils.showDeleteAlert(ShopsPromoteEditFragment.this, new AppUtils.DeleteConfirmListener() {
                        @Override
                        public void onClickDelete() {
                            netLinkPromoteProductDelete();
                        }
                    });
                }
            }
        });

        ivCodeIcon.setOnClickListener(new View.OnClickListener() {

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

        rlChooseProduct.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.CHOOSE_MODE, ShopsChooseProductFragment.ChooseProductMode.ModePromote.value());
                    bundle.putString(TransKey.SHOPS_FRAGMENT_NAME, ShopsPromoteEditFragment.class.getName());
                    push(ShopsChooseProductFragment.class, bundle);
                }
            }
        });

        ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), SelectPhotoActivity.class);
                    startActivityForResult(intent, SelectPhotoActivity.REQUEST_CODE_SET);
                }
            }
        });
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
    protected void setLayoutOfControls() {

        RelativeLayout.LayoutParams qtyShadeViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
        qtyShadeView = new LinearLayout(getBaseActivity());
        qtyShadeView.setAlpha(0.8f);
        qtyShadeView.setLayoutParams(qtyShadeViewParams);
        qtyShadeView.setBackgroundColor(getColor(R.color.common_gray));
        qtyShadeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        qtyShadeView.setVisibility(View.GONE);
        LinearLayout.LayoutParams paramDepartment = (LinearLayout.LayoutParams) rlPicture.getLayoutParams();
        paramDepartment.height = getActualHeightOnThisDevice(130);
        rlPicture.setLayoutParams(paramDepartment);

        rlPicture.addView(tvPicture);
        RelativeLayout.LayoutParams paramTvPicture = (RelativeLayout.LayoutParams) tvPicture.getLayoutParams();
        paramTvPicture.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvPicture.height = MATCH_PARENT;
        paramTvPicture.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvPicture.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvPicture.leftMargin = getActualWidthOnThisDevice(40);
        tvPicture.setLayoutParams(paramTvPicture);

        rlPicture.addView(ivPicture);
        RelativeLayout.LayoutParams paramsIvPicture = (RelativeLayout.LayoutParams) ivPicture.getLayoutParams();
        paramsIvPicture.width = 116;
        paramsIvPicture.height = 116;
        paramsIvPicture.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIvPicture.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsIvPicture.rightMargin = getActualWidthOnThisDevice(40);
        ivPicture.setLayoutParams(paramsIvPicture);

        LinearLayout.LayoutParams paramChooseProduct = (LinearLayout.LayoutParams) rlChooseProduct.getLayoutParams();
        paramChooseProduct.height = getActualHeightOnThisDevice(100);
        rlChooseProduct.setLayoutParams(paramChooseProduct);

        rlChooseProduct.addView(tvChooseProductKey);
        RelativeLayout.LayoutParams paramTvChooseProductKey = (RelativeLayout.LayoutParams) tvChooseProductKey.getLayoutParams();
        paramTvChooseProductKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvChooseProductKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvChooseProductKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvChooseProductKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvChooseProductKey.leftMargin = getActualWidthOnThisDevice(40);
        tvChooseProductKey.setLayoutParams(paramTvChooseProductKey);

        rlChooseProduct.addView(tvChooseProductValue);
        RelativeLayout.LayoutParams paramsIvChooseProductValue = (RelativeLayout.LayoutParams) tvChooseProductValue.getLayoutParams();
        paramsIvChooseProductValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvChooseProductValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvChooseProductValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIvChooseProductValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsIvChooseProductValue.rightMargin = getActualWidthOnThisDevice(80);
        tvChooseProductValue.setLayoutParams(paramsIvChooseProductValue);

        rlChooseProduct.addView(ivRightArrow);
        RelativeLayout.LayoutParams ivRightArrowLayoutParams = (RelativeLayout.LayoutParams) ivRightArrow.getLayoutParams();
        ivRightArrowLayoutParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        ivRightArrowLayoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        ivRightArrowLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        ivRightArrowLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivRightArrowLayoutParams.rightMargin = getActualWidthOnThisDevice(40);
        ivRightArrow.setLayoutParams(ivRightArrowLayoutParams);

        LinearLayout.LayoutParams paramPromotePrice = (LinearLayout.LayoutParams) rlPromotePrice.getLayoutParams();
        paramPromotePrice.height = getActualHeightOnThisDevice(100);
        rlPromotePrice.setLayoutParams(paramPromotePrice);

        rlPromotePrice.addView(tvPromotePriceKey);
        RelativeLayout.LayoutParams paramTvPromotePriceKey = (RelativeLayout.LayoutParams) tvPromotePriceKey.getLayoutParams();
        paramTvPromotePriceKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvPromotePriceKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvPromotePriceKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvPromotePriceKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvPromotePriceKey.leftMargin = getActualWidthOnThisDevice(40);
        tvPromotePriceKey.setLayoutParams(paramTvPromotePriceKey);

        rlPromotePrice.addView(etPromotePriceValue);
        RelativeLayout.LayoutParams paramsIvPromotePriceValue = (RelativeLayout.LayoutParams) etPromotePriceValue.getLayoutParams();
        paramsIvPromotePriceValue.width = getActualWidthOnThisDevice(400);
        paramsIvPromotePriceValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvPromotePriceValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIvPromotePriceValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsIvPromotePriceValue.rightMargin = getActualWidthOnThisDevice(20);
        etPromotePriceValue.setLayoutParams(paramsIvPromotePriceValue);
        etPromotePriceValue.setId(View.generateViewId());

        if (promote == null) {
            csbUnlimitedQty.setChecked(false);
        } else {

            if (Constants.UNLIMITED_FLAG_OFF.equals(promote.getUnlimitedFlag())) {
                csbUnlimitedQty.setChecked(false);
                etQtyValue.setEnabled(true);
            } else {
                csbUnlimitedQty.setChecked(true);
                qtyShadeView.setVisibility(View.VISIBLE);
                etQtyValue.setEnabled(false);
            }
        }


        rlUnlimitedQty.addView(getKeyTextView(getString(R.string.shop_setting_unlimited_qty)));

        csbUnlimitedQty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    qtyShadeView.setVisibility(View.VISIBLE);
                    etQtyValue.setEnabled(false);
                } else {

                    qtyShadeView.setVisibility(View.GONE);
                    etQtyValue.setEnabled(true);
                }
            }
        });

        RelativeLayout.LayoutParams csBtnParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, getActualHeightOnThisDevice(100));
        csBtnParams.rightMargin = getActualWidthOnThisDevice(40);
        csBtnParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        csBtnParams.addRule(RelativeLayout.CENTER_VERTICAL);
        csbUnlimitedQty.setLayoutParams(csBtnParams);
        rlUnlimitedQty.addView(csbUnlimitedQty);


        LinearLayout.LayoutParams paramQty = (LinearLayout.LayoutParams) rlQty.getLayoutParams();
        paramQty.height = getActualHeightOnThisDevice(100);
        rlQty.setLayoutParams(paramQty);

        rlQty.addView(tvQtyKey);
        RelativeLayout.LayoutParams paramTvQtyKey = (RelativeLayout.LayoutParams) tvQtyKey.getLayoutParams();
        paramTvQtyKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvQtyKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvQtyKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvQtyKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvQtyKey.leftMargin = getActualWidthOnThisDevice(40);
        tvQtyKey.setLayoutParams(paramTvQtyKey);

        rlQty.addView(etQtyValue);
        RelativeLayout.LayoutParams paramsIvQtyValue = (RelativeLayout.LayoutParams) etQtyValue.getLayoutParams();
        paramsIvQtyValue.width = (int) (getScreenWidth() * 0.3f);
        paramsIvQtyValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvQtyValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIvQtyValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsIvQtyValue.rightMargin = getActualWidthOnThisDevice(20);
        etQtyValue.setLayoutParams(paramsIvQtyValue);
        etQtyValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String qtyStr = charSequence.toString();

                if (!Constants.STR_EMPTY.equals(qtyStr)) {

                    int qty = Integer.parseInt(qtyStr);

                    try {
                        if (!"-1".equals(maxQty) && qty > Integer.parseInt(maxQty)) {

                            etQtyValue.setText(qtyStr.substring(0, qtyStr.length() - 1));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        rlQty.addView(qtyShadeView);


        LinearLayout.LayoutParams paramCode = (LinearLayout.LayoutParams) rlCode.getLayoutParams();
        paramCode.height = getActualHeightOnThisDevice(100);
        rlCode.setLayoutParams(paramCode);

        rlCode.addView(tvCodeKey);
        RelativeLayout.LayoutParams paramTvCodeKey = (RelativeLayout.LayoutParams) tvCodeKey.getLayoutParams();
        paramTvCodeKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvCodeKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvCodeKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvCodeKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvCodeKey.leftMargin = getActualWidthOnThisDevice(40);
        tvCodeKey.setLayoutParams(paramTvCodeKey);

        rlCode.addView(ivCodeIcon);
        RelativeLayout.LayoutParams paramIvCodeIcon = (RelativeLayout.LayoutParams) ivCodeIcon.getLayoutParams();
        paramIvCodeIcon.width = getActualWidthOnThisDevice(52);
        paramIvCodeIcon.height = getActualWidthOnThisDevice(52);
        paramIvCodeIcon.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramIvCodeIcon.addRule(RelativeLayout.CENTER_VERTICAL);
        paramIvCodeIcon.leftMargin = getActualWidthOnThisDevice(150);
        ivCodeIcon.setLayoutParams(paramIvCodeIcon);


        rlCode.addView(etCodeValue);
        RelativeLayout.LayoutParams paramsTvCodeValue = (RelativeLayout.LayoutParams) etCodeValue.getLayoutParams();
        paramsTvCodeValue.width = getActualWidthOnThisDevice(300);
        paramsTvCodeValue.height = MATCH_PARENT;
        paramsTvCodeValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvCodeValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvCodeValue.rightMargin = getActualWidthOnThisDevice(20);
        etCodeValue.setLayoutParams(paramsTvCodeValue);

        LinearLayout.LayoutParams paramStartTime = (LinearLayout.LayoutParams) rlStartTime.getLayoutParams();
        paramStartTime.height = getActualHeightOnThisDevice(100);
        rlStartTime.setLayoutParams(paramStartTime);

        rlStartTime.addView(tvStartTimeKey);
        RelativeLayout.LayoutParams paramTvStartTimeKey = (RelativeLayout.LayoutParams) tvStartTimeKey.getLayoutParams();
        paramTvStartTimeKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvStartTimeKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvStartTimeKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvStartTimeKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvStartTimeKey.leftMargin = getActualWidthOnThisDevice(40);
        tvStartTimeKey.setLayoutParams(paramTvStartTimeKey);

        rlStartTime.addView(tvStartTimeValue);
        RelativeLayout.LayoutParams paramsTvStartTimeValue = (RelativeLayout.LayoutParams) tvStartTimeValue.getLayoutParams();
        paramsTvStartTimeValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvStartTimeValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvStartTimeValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvStartTimeValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvStartTimeValue.rightMargin = getActualWidthOnThisDevice(40);
        tvStartTimeValue.setLayoutParams(paramsTvStartTimeValue);

        LinearLayout.LayoutParams paramEndTime = (LinearLayout.LayoutParams) rlEndTime.getLayoutParams();
        paramEndTime.height = getActualHeightOnThisDevice(100);
        rlEndTime.setLayoutParams(paramEndTime);

        rlEndTime.addView(tvEndTimeKey);
        RelativeLayout.LayoutParams paramTvEndTimeKey = (RelativeLayout.LayoutParams) tvEndTimeKey.getLayoutParams();
        paramTvEndTimeKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvEndTimeKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvEndTimeKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvEndTimeKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvEndTimeKey.leftMargin = getActualWidthOnThisDevice(40);
        tvEndTimeKey.setLayoutParams(paramTvPicture);

        rlEndTime.addView(tvEndTimeValue);
        RelativeLayout.LayoutParams paramsTvEndTimeValue = (RelativeLayout.LayoutParams) tvEndTimeValue.getLayoutParams();
        paramsTvEndTimeValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvEndTimeValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvEndTimeValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvEndTimeValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvEndTimeValue.rightMargin = getActualWidthOnThisDevice(40);
        tvEndTimeValue.setLayoutParams(paramsTvEndTimeValue);

        AppUtils.addBottomSeparatorLine(rlEndTime, getActivity());

        LinearLayout.LayoutParams paramDelete = (LinearLayout.LayoutParams) rlDelete.getLayoutParams();
        paramDelete.height = getActualHeightOnThisDevice(100);
        paramDelete.topMargin = 30;
        rlDelete.setLayoutParams(paramDelete);

        rlDelete.addView(btDelete);
        RelativeLayout.LayoutParams paramBtDelete = (RelativeLayout.LayoutParams) btDelete.getLayoutParams();
        paramBtDelete.width = (int) (getScreenWidth() * 0.9);
        paramBtDelete.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramBtDelete.setMargins(0, px2dp(30), 0, 0);
        paramBtDelete.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        paramBtDelete.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        btDelete.setLayoutParams(paramBtDelete);


    }

    @Override
    protected void setPropertyOfControls() {

        etPromotePriceValue.setSingleLine();
        etPromotePriceValue.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);

        etQtyValue.setSingleLine();
        etQtyValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        etQtyValue.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        etQtyValue.setBackground(null);

        tvPicture.setText(R.string.shop_setting_picture);
        tvPicture.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvPicture.setTextColor(getColor(R.color.common_black));

        tvChooseProductKey.setText(R.string.shop_setting_choose_product);
        tvChooseProductKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvChooseProductKey.setTextColor(getColor(R.color.common_black));

        tvChooseProductValue.setText(Constants.STR_EMPTY);
        tvChooseProductValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvChooseProductValue.setTextColor(getColor(R.color.common_black));

        ivRightArrow.setImageResource(R.drawable.icon_right_arrow);

        tvPromotePriceKey.setText(R.string.shop_setting_promote_price);
        tvPromotePriceKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvPromotePriceKey.setTextColor(getColor(R.color.common_black));

        etPromotePriceValue.setHint(getString(R.string.shop_setting_promote_price));
        etPromotePriceValue.setHintTextColor(getColor(R.color.offers_hint_text_color));
        etPromotePriceValue.setText(Constants.STR_EMPTY);
        etPromotePriceValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        etPromotePriceValue.setTextColor(getColor(R.color.common_gray));

        tvQtyKey.setText(R.string.shop_setting_qty);
        tvQtyKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvQtyKey.setTextColor(getColor(R.color.common_black));

        etQtyValue.setText(Constants.STR_EMPTY);
        etQtyValue.setHint(getString(R.string.shop_setting_qty));
        etQtyValue.setHintTextColor(getColor(R.color.offers_hint_text_color));
        etQtyValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        etQtyValue.setTextColor(getColor(R.color.common_gray));


        tvCodeKey.setText(R.string.shop_setting_code);
        tvCodeKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvCodeKey.setTextColor(getColor(R.color.common_black));

        ivCodeIcon.setBackgroundResource(R.drawable.icon_code_of_subclass);

        etCodeValue.setText(Constants.STR_EMPTY);
        etCodeValue.setHint(getString(R.string.shop_setting_code));
        etCodeValue.setHintTextColor(getColor(R.color.offers_hint_text_color));
        etCodeValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        etCodeValue.setTextColor(getColor(R.color.common_gray));
        etCodeValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);

        tvStartTimeKey.setText(R.string.shop_setting_start_time);
        tvStartTimeKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvStartTimeKey.setTextColor(getColor(R.color.common_black));

        tvStartTimeValue.setText(Constants.STR_EMPTY);
        tvStartTimeValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvStartTimeValue.setTextColor(getColor(R.color.common_gray));

        tvEndTimeKey.setText(R.string.shop_setting_end_time);
        tvEndTimeKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvEndTimeKey.setTextColor(getColor(R.color.common_black));

        tvEndTimeValue.setText(Constants.STR_EMPTY);
        tvEndTimeValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvEndTimeValue.setTextColor(getColor(R.color.common_gray));


        if (promote != null) {
            tvChooseProductValue.setText(promote.getPromoteName());
            etPromotePriceValue.setText(Utils.get2DigitDecimalString(promote.getPromotePrice()));
            etQtyValue.setText(promote.getQty());
            etCodeValue.setText(promote.getCode());
            tvStartTimeValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(promote.getStartDate(), mContext));
            tvEndTimeValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(promote.getEndDate(), mContext));
            if (isPhotoChange == null) {
                if (Utils.isStringNullOrEmpty(promote.getPromoteImg())) {
                    AppUtils.showNetworkImage(ivPicture, Constants.PHOTO_DEFAULT_PROMOTE_URL);
                } else {
                    AppUtils.showNetworkImage(ivPicture, promote.getPromoteImg());
                }
            }
        } else {
            if (isPhotoChange == null) {
                AppUtils.showNetworkImage(ivPicture, Constants.PHOTO_DEFAULT_PROMOTE_URL);
            }
        }

        if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
            rlDelete.setVisibility(View.GONE);
        }
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(R.string.shop_setting_promote_edit);
        changeView();
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getBaseActivity());

                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                    if (doCheck()) {
                        if (promote != null) {
                            netLinkPromoteProductModify();
                        } else {
                            netLinkPromoteProductAdd();
                        }
                    }
                } else {
                    btDelete.setVisibility(View.VISIBLE);
                    setFragmentMode(FragmentMode.FragmentModeEdit);
                    changeView();
                }


            }
        });
    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
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

            case SelectPhotoActivity.REQUEST_CODE_SET:
                if (resultCode == Activity.RESULT_OK) {
                    byte[] b = data.getByteArrayExtra("bitmap");
                    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    if (bitmap != null) {
                        isPhotoChange = bitmap;
                        ivPicture.setLocalImageBitmap(isPhotoChange);
                    }
                }
                break;
        }


    }

    @Override
    protected void reShowWithBackValue() {
        Bundle bundle = getReturnValues();
        if (bundle != null) {
            ArrayList<String> chooseProductList = bundle.getStringArrayList(TransKey.CHOOSE_PRODUCT_LIST);
            shopsProductList.clear();
            int i = 0;
            if (chooseProductList != null) {
                for (String product : chooseProductList) {
                    ShopsProduct shopsProduct = (ShopsProduct) Utils.getObjectFromString(product);
                    if (shopsProduct.getParentId() == null || Constants.STR_0.equals(shopsProduct.getParentId())) {
                        selectQty = shopsProduct.getQty();
                        selectUnlimitedFlag = shopsProduct.getUnlimitedFlag();

                    }

                    i++;
                    shopsProductList.add(shopsProduct);
                }
                String productName = bundle.getString(TransKey.COMMON_PRODUCT_NAME);
                ShopsProduct shopsProduct = shopsProductList.get(0);
                if (shopsProduct.isCaddie()) {
                    csbUnlimitedQty.setChecked(true);
                    csbUnlimitedQty.setEnabled(false);
                } else {
                    csbUnlimitedQty.setEnabled(true);
                }

                if (Utils.isStringNullOrEmpty(productName)) {
                    productName = shopsProduct.getProductName();
                }
                tvChooseProductValue.setText(productName);
            }

            if (Constants.STR_1.equals(selectUnlimitedFlag)) {

                maxQty = "-1";
                csbUnlimitedQty.setChecked(true);

            } else {
                csbUnlimitedQty.setChecked(false);
                csbUnlimitedQty.setEnabled(false);
                maxQty = selectQty;
                etQtyValue.setText(selectQty);
            }


        }
    }

    private void changeView() {

        if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
            getTvRight().setBackground(null);
            getTvRight().setText(R.string.common_ok);
            etPromotePriceValue.setEnabled(true);
            etQtyValue.setEnabled(true);
            etCodeValue.setEnabled(true);
            ivCodeIcon.setEnabled(true);
            rlChooseProduct.setEnabled(true);
            btDelete.setEnabled(true);
            if (Utils.isListNotNullOrEmpty(shopsProductList)) {
                ShopsProduct shopsProduct = shopsProductList.get(0);
                if (shopsProduct.isCaddie()) {
                    csbUnlimitedQty.setChecked(true);
                    csbUnlimitedQty.setEnabled(false);
                } else {
                    csbUnlimitedQty.setEnabled(true);
                }
            } else {
                csbUnlimitedQty.setEnabled(true);
            }

            tvStartTimeValue.setTextColor(getColor(R.color.common_black));
            tvEndTimeValue.setTextColor(getColor(R.color.common_black));
            tvChooseProductValue.setTextColor(getColor(R.color.common_black));

        } else {
            getTvRight().setBackgroundResource(R.drawable.administration_icon_edit);
            getTvRight().setText(Constants.STR_EMPTY);
            etQtyValue.setEnabled(false);
            etPromotePriceValue.setEnabled(false);
            etQtyValue.setEnabled(false);
            etCodeValue.setEnabled(false);
            ivCodeIcon.setEnabled(false);
            rlChooseProduct.setEnabled(false);
            btDelete.setEnabled(false);
            btDelete.setVisibility(View.GONE);
            tvStartTimeValue.setTextColor(getColor(R.color.common_gray));
            tvEndTimeValue.setTextColor(getColor(R.color.common_gray));
            tvChooseProductValue.setTextColor(getColor(R.color.common_gray));
            csbUnlimitedQty.setEnabled(false);
        }
    }

    private void uploadPhoto(Bitmap bitmap, String id) {
        Utils.showShortToast(getActivity(), getString(R.string.common_uploading));
        Bitmap bitmapTemp = ImageUtils.imageZoom(bitmap);
        byte[] b = ImageUtils.convertBitmap2Bytes(bitmapTemp);
        HashMap<String, byte[]> files = new HashMap<>();
        files.put("img", b);
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(mContext));
        params.put("type", Constants.UPLOAD_TYPE_PROMOTE);
        params.put("id", id);

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShopsPromoteEditFragment.this) {
            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                if (isPhotoChange != null && !isPhotoChange.isRecycled()) {
                    ivPicture.setLocalImageBitmap(null);
                }
                doBackWithRefresh();
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.uploadFile(getActivity(), ApiManager.HttpApi.CommonPhoto, files, params);
    }


    /**
     * delete PromoteProduct data.
     */
    private void netLinkPromoteProductDelete() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_PROMOTE_PROMOTE_ID, String.valueOf(promote.getPromoteId()));

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShopsPromoteEditFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20403_DELETE_SUCCESSFULLY) {
                    doBack();
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                Log.e("syb:error", error.getMessage());
            }
        };
        hh.startDelete(getActivity(), ApiManager.HttpApi.PromoteProductDel, params);
    }


    /**
     * save PromoteProduct data. (Put)
     */
    private void netLinkPromoteProductModify() {

        String productListString = null;
        try {
            JSONArray ja = new JSONArray();
            for (ShopsProduct shopsProduct : shopsProductList) {
                JSONObject jo = new JSONObject();
                jo.put("product_id", shopsProduct.getProductId());
                jo.put("attr_id", shopsProduct.getAttrId() == null ? Constants.STR_EMPTY : shopsProduct.getAttrId());
                jo.put("price", shopsProduct.getProductPrice());
                jo.put("type", String.valueOf(shopsProduct.getType()));
                ja.put(jo);
            }

            productListString = ja.toString();

        } catch (Exception e) {
            Utils.log(e.getMessage());
        }

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_PROMOTE_PROMOTE_ID, String.valueOf(promote.getPromoteId()));
        params.put(ApiKey.SHOPS_PROMOTE_PRICE, etPromotePriceValue.getValue());
        params.put(ApiKey.SHOPS_PROMOTE_PRODUCT_LIST_TITLE, productListString);
        params.put(ApiKey.SHOPS_PROMOTE_QTY, etQtyValue.getText().toString());
        if (Utils.isStringNotNullOrEmpty(etCodeValue.getText().toString())) {
            params.put(ApiKey.SHOPS_PROMOTE_CODE, etCodeValue.getText().toString());
        }
        String startDate = DateUtils.getAPIYearMonthDayFromCurrentShow(tvStartTimeValue.getText().toString(), mContext);
        String endDate = DateUtils.getAPIYearMonthDayFromCurrentShow(tvEndTimeValue.getText().toString(), mContext);
        params.put(ApiKey.SHOPS_PROMOTE_START_DATE, startDate);
        params.put(ApiKey.SHOPS_PROMOTE_END_DATE, endDate);
        if (csbUnlimitedQty.isChecked()) {
            params.put(ApiKey.SHOPS_UNLIMITED_FLAG, Constants.UNLIMITED_FLAG_ON);
        } else {
            params.put(ApiKey.SHOPS_UNLIMITED_FLAG, Constants.UNLIMITED_FLAG_OFF);
        }
        ShopsProduct shopsProduct = shopsProductList.get(0);
        if (shopsProduct.isCaddie()) {
            params.put(ApiKey.SHOPS_UNLIMITED_FLAG, Constants.UNLIMITED_FLAG_ON);
        }

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShopsPromoteEditFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                Utils.showShortToast(getActivity(), msg);
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    if (isPhotoChange != null) {
                        uploadPhoto(isPhotoChange, String.valueOf(promote.getPromoteId()));
                    } else {
                        doBackWithRefresh();
                    }
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.startPut(getActivity(), ApiManager.HttpApi.PromoteProductPut, params);
    }


    /**
     * add PromoteProduct data. (add)
     */
    private void netLinkPromoteProductAdd() {

        ObjectMapper objectMapper = new ObjectMapper();
        String productListString = null;
        try {
            productListString = objectMapper.writeValueAsString(shopsProductList);
            productListString = productListString.replace("productId", "product_id")
                    .replace("attrId", "attr_id")
                    .replace("productPrice", "price");

        } catch (Exception e) {
            Utils.log(e.getMessage());
        }

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_PROMOTE_PRICE, etPromotePriceValue.getValue());
        params.put(ApiKey.SHOPS_PROMOTE_PRODUCT_LIST_TITLE, productListString);
        params.put(ApiKey.SHOPS_PROMOTE_QTY, etQtyValue.getText().toString());
        params.put(ApiKey.SHOPS_PROMOTE_CODE, etCodeValue.getText().toString());
        String startDate = DateUtils.getAPIYearMonthDayFromCurrentShow(tvStartTimeValue.getText().toString(), mContext);
        String endDate = DateUtils.getAPIYearMonthDayFromCurrentShow(tvEndTimeValue.getText().toString(), mContext);
        params.put(ApiKey.SHOPS_PROMOTE_START_DATE, startDate);
        params.put(ApiKey.SHOPS_PROMOTE_END_DATE, endDate);

        if (csbUnlimitedQty.isChecked()) {
            params.put(ApiKey.SHOPS_UNLIMITED_FLAG, Constants.UNLIMITED_FLAG_ON);
        } else {
            params.put(ApiKey.SHOPS_UNLIMITED_FLAG, Constants.UNLIMITED_FLAG_OFF);
        }

        ShopsProduct shopsProduct = shopsProductList.get(0);
        if (shopsProduct.isCaddie()) {
            params.put(ApiKey.SHOPS_UNLIMITED_FLAG, Constants.UNLIMITED_FLAG_ON);
        }

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShopsPromoteEditFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();

                if (returnCode == Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY) {
                    if (Utils.isStringNotNullOrEmpty(jo.getAddId())) {
                        if (isPhotoChange != null) {
                            uploadPhoto(isPhotoChange, jo.getAddId());
                        } else {
                            doBackWithRefresh();
                        }
                    } else {
                        doBackWithRefresh();
                    }
                } else {
                    Utils.showShortToast(getActivity(), jo.getReturnInfo());
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.PromoteProductPost, params);
    }

    private boolean doCheck() {

        if (StringUtils.isEmpty(etPromotePriceValue.getValue())) {
            Utils.showShortToast(getActivity(), AppUtils.generateNotNullMessage(this, R.string.shop_setting_promote_price));
            return false;
        }

        if (!csbUnlimitedQty.isChecked()) {
            if (Utils.isStringNullOrEmpty(etQtyValue.getText().toString())) {
                Utils.showShortToast(getActivity(), AppUtils.generateNotNullMessage(this, R.string.shop_setting_qty));
                return false;
            }
        }


        if (Utils.isStringNullOrEmpty(tvStartTimeValue.getText().toString())) {
            Utils.showShortToast(getActivity(), AppUtils.generateNotNullMessage(this, R.string.shop_setting_start_time));
            return false;
        }

        if (Utils.isStringNullOrEmpty(tvEndTimeValue.getText().toString())) {
            Utils.showShortToast(getActivity(), AppUtils.generateNotNullMessage(this, R.string.shop_setting_end_time));
            return false;
        }

        if (!Utils.isSecondDateLaterEqualFirst(tvStartTimeValue.getText().toString(), tvEndTimeValue.getText().toString()
                , DateUtils.getShowYearMonthDayFormat(mContext))) {
            Utils.showShortToast(getActivity(),
                    AppUtils.generateLargerThanMessage(this, R.string.common_end_date, R.string.common_start_date));
            return false;
        }

        if (!Utils.isListNotNullOrEmpty(shopsProductList)) {
            Utils.showShortToast(getActivity(), AppUtils.generateNotNullMessage(this, R.string.common_product));
            return false;
        }

        return true;
    }
}
