/**
 * Project Name: itee
 * File Name:  QuickScanQrCodeActivity.java
 * Package Name: cn.situne.itee.activity
 * Date:   2015-04-23
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.mining.app.zxing.MipcaActivityCapture;

import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.shopping.ShoppingGoodsListFragment;
import cn.situne.itee.fragment.shopping.ShoppingPaymentFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonEntry;
import cn.situne.itee.view.IteeButton;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:QuickScanQrCodeActivity <br/>
 * Function: QuickScanQrCodeActivity. <br/>
 * Date: 2015-04-23 <br/>
 *.
 * @author liaojian
 * @version 1.0
 * @see
 */
public class QuickScanQrCodeActivity extends MipcaActivityCapture {

    private IteeTextView tvLeftTitle;

    private IteeEditText etInputCode;
    private IteeButton btnOk;
    private String fromPage;
    private IteeTextView tvRight;

    @Override
    protected void createView(RelativeLayout parent) {

        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        if (getActionBar() != null) {
            getActionBar().hide();
        }

        RelativeLayout rlActionBar = new RelativeLayout(this);
        rlActionBar.setBackgroundResource(R.drawable.bg_action_bar);

        RelativeLayout.LayoutParams rlActionBarLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, actionBarHeight);
        rlActionBarLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rlActionBar.setLayoutParams(rlActionBarLayoutParams);

        setBackArrowAndLeftTitle(rlActionBar);

        parent.addView(rlActionBar);

        RelativeLayout rlInputAndBtn = new RelativeLayout(this);
        rlInputAndBtn.setBackgroundColor(getResources().getColor(R.color.common_white));

        RelativeLayout.LayoutParams rlInputAndBtnLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getHeight(100));
        rlInputAndBtnLayoutParams.setMargins(0, actionBarHeight, 0, 0);
        rlInputAndBtn.setLayoutParams(rlInputAndBtnLayoutParams);

        setEtInputCodeAndBtnOk(rlInputAndBtn);

        parent.addView(rlInputAndBtn);

        etInputCode.setPadding(getHeight(10), 0, 0, 0);

        Intent intent = getIntent();
        fromPage = intent.getStringExtra(TransKey.COMMON_FROM_PAGE);
        if (ShoppingGoodsListFragment.class.getName().equals(fromPage)) {
            tvLeftTitle.setText(R.string.shopping_shopping);
            etInputCode.setHint(R.string.shopping_product_code);
        } else if (ShoppingPaymentFragment.class.getName().equals(fromPage)) {
            setTvRight(rlActionBar);
            tvLeftTitle.setText(R.string.shopping_add_a_customer);
            etInputCode.setHint(R.string.check_in_confirmation_no);

            tvRight.setBackgroundResource(R.drawable.icon_search_white);
            tvRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(QuickScanQrCodeActivity.this, ShoppingSearchProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivityForResult(intent, ScanQrCodeActivity.SCANNING_GREQUEST_CODE_2);
                }
            });

        } else {
            tvLeftTitle.setText(R.string.title_qr_code);
            etInputCode.setHint(R.string.check_in_confirmation_no);
        }


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(QuickScanQrCodeActivity.this);
                String code = etInputCode.getText().toString();
                if (Utils.isStringNotNullOrEmpty(code)) {
                    if (Utils.isStringNullOrEmpty(fromPage)) {
                        getData(code);
                    } else {
                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra(TransKey.BOOKING_CODE, code);
                        //设置返回数据

                        QuickScanQrCodeActivity.this.setResult(RESULT_OK, intent);
                        QuickScanQrCodeActivity.this.finish();
                    }
                } else {
                    Utils.showShortToast(getApplicationContext(), "Enter booking code!");
                }
            }
        });

    }

    private void setTvRight(RelativeLayout rlActionBarMenu) {
        tvRight = new IteeTextView(this);
        rlActionBarMenu.addView(tvRight);

        rlActionBarMenu.setPadding(0, 0, getWidth(20), 0);

        RelativeLayout.LayoutParams paramsTvOk = (RelativeLayout.LayoutParams) tvRight.getLayoutParams();
        paramsTvOk.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvOk.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvOk.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvOk.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        tvRight.setLayoutParams(paramsTvOk);

        tvRight.setId(View.generateViewId());
        tvRight.setTextSize(Constants.FONT_SIZE_LARGER);
        tvRight.setTextColor(getResources().getColor(R.color.common_white));
        tvRight.setGravity(Gravity.CENTER_VERTICAL);
    }

    @Override
    protected void handleCode(String code) {
        Utils.hideKeyboard(QuickScanQrCodeActivity.this);
        Log.e("LC","code = "+code);
        if (Utils.isStringNotNullOrEmpty(code)) {
            if (Utils.isStringNullOrEmpty(fromPage)) {
                getData(code);
            } else {
                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra(TransKey.BOOKING_CODE, code);
                //设置返回数据
                QuickScanQrCodeActivity.this.setResult(RESULT_OK, intent);
                finish();
            }
        } else {
            Utils.showShortToast(getApplicationContext(), "Enter booking code!");
        }
    }

    private int setBackArrow(RelativeLayout parent) {
        RelativeLayout rlIconContainer = new RelativeLayout(this);
        rlIconContainer.setId(View.generateViewId());

        RelativeLayout.LayoutParams rlIconContainerLayoutParams = new RelativeLayout.LayoutParams(getWidth(100), ViewGroup.LayoutParams.MATCH_PARENT);
//        rlIconContainerLayoutParams.leftMargin = getWidth(20);
        rlIconContainer.setLayoutParams(rlIconContainerLayoutParams);

        parent.addView(rlIconContainer);

        ImageView ivIcon = new ImageView(this);
        ivIcon.setId(View.generateViewId());
        ivIcon.setBackgroundResource(R.drawable.icon_back);
        ivIcon.setScaleType(ImageView.ScaleType.FIT_XY);
        ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(QuickScanQrCodeActivity.this);
                finish();
            }
        });

        RelativeLayout.LayoutParams paramsIvIcon = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        paramsIvIcon.width = getWidth(50);
//        paramsIvIcon.height = getWidth(50);
        paramsIvIcon.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsIvIcon.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        ivIcon.setLayoutParams(paramsIvIcon);
        rlIconContainer.addView(ivIcon);

        ImageView ivSeparator = new ImageView(this);
        ivSeparator.setVisibility(View.INVISIBLE);
        ivSeparator.setId(View.generateViewId());
        ivSeparator.setImageResource(R.drawable.icon_separator);
        rlIconContainer.addView(ivSeparator);

        RelativeLayout.LayoutParams paramsIvSeparator
                = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        paramsIvSeparator.width = 5;
        paramsIvSeparator.height = 50;
        paramsIvSeparator.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsIvSeparator.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsIvSeparator.rightMargin = 5;
        ivSeparator.setLayoutParams(paramsIvSeparator);

        IteeButton btn = new IteeButton(this);
        RelativeLayout.LayoutParams btnLayoutParams
                = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        btn.setLayoutParams(btnLayoutParams);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(QuickScanQrCodeActivity.this);
                finish();
            }
        });
        btn.setBackground(null);
        rlIconContainer.addView(btn);
        rlIconContainer.bringChildToFront(ivIcon);

        return rlIconContainer.getId();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case ScanQrCodeActivity.SCANNING_GREQUEST_CODE_2:
                if (resultCode == Activity.RESULT_OK) {

                    boolean isBack = data.getBooleanExtra(TransKey.IS_BACK, false);
                    if (!isBack) {
                        String qrCode = data.getStringExtra(TransKey.BOOKING_CODE);
                        Log.e("LC", qrCode);
                        Intent intent = new Intent();
                        intent.putExtra(TransKey.BOOKING_CODE, qrCode);
                        QuickScanQrCodeActivity.this.setResult(RESULT_OK, intent);
                        finish();
                    }
                }
                break;
        }
    }

    private void setBackArrowAndLeftTitle(RelativeLayout parent) {
        int leftLayoutId = setBackArrow(parent);

        tvLeftTitle = new IteeTextView(this);
        parent.addView(tvLeftTitle);

        RelativeLayout.LayoutParams paramsTvLeftTitle = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        paramsTvLeftTitle.width = (int) (getWidth() * 0.5);
        paramsTvLeftTitle.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvLeftTitle.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvLeftTitle.addRule(RelativeLayout.RIGHT_OF, leftLayoutId);
        paramsTvLeftTitle.leftMargin = 10;
        tvLeftTitle.setLayoutParams(paramsTvLeftTitle);

        tvLeftTitle.setTextSize(Constants.FONT_SIZE_LARGER);
        tvLeftTitle.setSingleLine();
        tvLeftTitle.setEllipsize(TextUtils.TruncateAt.END);
        tvLeftTitle.setTextColor(getResources().getColor(R.color.common_white));
    }

    private void setEtInputCodeAndBtnOk(RelativeLayout parent) {

        etInputCode = new IteeEditText(this);
        btnOk = new IteeButton(this);

        parent.addView(etInputCode);
        RelativeLayout.LayoutParams paramsEtInputCode = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, getHeight(100));
        paramsEtInputCode.width = getWidth(600);
        paramsEtInputCode.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsEtInputCode.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsEtInputCode.leftMargin = 10;
        etInputCode.setLayoutParams(paramsEtInputCode);

        etInputCode.setId(View.generateViewId());
        etInputCode.setBackgroundResource(R.drawable.et_account_forgot_border);
        etInputCode.setHint(R.string.forgot_password_account);
        etInputCode.setHintTextColor(getResources().getColor(R.color.common_gray));
        etInputCode.setTextSize(Constants.FONT_SIZE_NORMAL);
        etInputCode.setPadding(10, 0, 0, 0);
        etInputCode.setSingleLine();
        etInputCode.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        etInputCode.setSelectAllOnFocus(false);

        parent.addView(btnOk);
        RelativeLayout.LayoutParams paramsBtnOk = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, getHeight(100));
        paramsBtnOk.width = getWidth(120);
        paramsBtnOk.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsBtnOk.addRule(RelativeLayout.RIGHT_OF, etInputCode.getId());
        paramsBtnOk.leftMargin = 0;
        btnOk.setLayoutParams(paramsBtnOk);

        btnOk.setBackgroundResource(R.drawable.button_send_random_password);
        btnOk.setText(R.string.common_ok);
        btnOk.setTextColor(getResources().getColor(R.color.common_white));

    }

    public int getWidth(float currentPx) {
        return (int) (currentPx / Constants.DESIGN_UI_WIDTH * (getResources().getDisplayMetrics().widthPixels));
    }

    public int getHeight(float currentPx) {
        return (int) (currentPx / Constants.DESIGN_UI_HEIGHT * (getResources().getDisplayMetrics().heightPixels));
    }

    private void getData(final String code) {

        Map<String, String> params = new HashMap<>();

        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(this));
        params.put(ApiKey.SCAN_QR_CODE, code);

        HttpManager<JsonEntry> hh = new HttpManager<JsonEntry>(this) {

            @Override
            public void onJsonSuccess(JsonEntry jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    Intent intent = new Intent();
                    intent.putExtra(TransKey.BOOKING_CODE, code);
                    QuickScanQrCodeActivity.this.setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Utils.showShortToast(QuickScanQrCodeActivity.this, msg);
                }

            }

            @Override
            public void onJsonError(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(QuickScanQrCodeActivity.this, R.string.msg_common_network_error);
            }
        };
        hh.startGet(QuickScanQrCodeActivity.this, ApiManager.HttpApi.ScanQrCodeGet, params);
    }
}
