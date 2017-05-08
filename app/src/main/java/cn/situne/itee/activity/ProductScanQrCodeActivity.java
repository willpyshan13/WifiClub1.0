/**
 * Project Name: itee
 * File Name:  QuickScanQrCodeActivity.java
 * Package Name: cn.situne.itee.activity
 * Date:   2015-04-23
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.activity;

import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.mining.app.zxing.MipcaActivityCapture;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.ShoppingProduct;
import cn.situne.itee.fragment.shopping.ShoppingGoodsListItemFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonProductByCode;
import cn.situne.itee.manager.jsonentity.JsonShopsPropertyPriceOrQtyGet;
import cn.situne.itee.view.IteeButton;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.SelectAttributePopupWindow;

/**
 * ClassName:QuickScanQrCodeActivity <br/>
 * Function: QuickScanQrCodeActivity. <br/>
 * Date: 2015-04-23 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
@SuppressWarnings("unchecked")
public class ProductScanQrCodeActivity extends MipcaActivityCapture {

    private IteeTextView tvLeftTitle;

    private EditText etInputCode;
    private IteeButton btnOk;
    private String greenId;

    private IteeTextView tvTotalCost;
    private IteeTextView tvNumberOfGoods;

    private SelectAttributePopupWindow nowPopUpWin;

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

        RelativeLayout.LayoutParams rlInputAndBtnLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.getActualHeightOnThisDevice(100, this));
        rlInputAndBtnLayoutParams.setMargins(0, actionBarHeight, 0, 0);
        rlInputAndBtn.setLayoutParams(rlInputAndBtnLayoutParams);

        setEtInputCodeAndBtnOk(rlInputAndBtn);

        parent.addView(rlInputAndBtn);

        Intent intent = getIntent();
        if (intent != null) {
            HashMap<String, ShoppingProduct> map = (HashMap<String, ShoppingProduct>) intent.getSerializableExtra("shoppingCartMap");
            Log.e("ttt", String.valueOf(map));
            LinkedHashMap<String, ShoppingProduct> shoppingCart = AppUtils.getShoppingCart(this);
            shoppingCart.putAll(map);
            AppUtils.saveShoppingCart(shoppingCart, this);
            greenId = intent.getStringExtra("shoppingGreenId");
        }

        tvLeftTitle.setText(R.string.shopping_shopping);
        etInputCode.setHint(R.string.shopping_product_code);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(ProductScanQrCodeActivity.this);
                String code = etInputCode.getText().toString();
                if (Utils.isStringNotNullOrEmpty(code)) {
                    getProductByCode(code);
                } else {
                    Utils.showShortToast(getApplicationContext(), "Please scan the qr code or input one!");
                }
            }
        });

        RelativeLayout rlShoppingCartContainer = new RelativeLayout(this);
        rlShoppingCartContainer.setBackgroundColor(getResources().getColor(R.color.common_white));

        rlShoppingCartContainer.setOnClickListener(new AppUtils.NoDoubleClickListener(this) {
            @Override
            public void noDoubleClick(View v) {
                LinkedHashMap<String, ShoppingProduct> shoppingCart = AppUtils.getShoppingCart(ProductScanQrCodeActivity.this);
                if (shoppingCart.size() > 0) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("shoppingCartMap", Utils.getStringFromObject(shoppingCart));
                    resultIntent.putExtra(TransKey.JUMP_TO_CONFIRM_PAY, true);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Utils.showShortToast(ProductScanQrCodeActivity.this, R.string.msg_please_choose_product);
                }
            }
        });

        IteeTextView tvTotal = new IteeTextView(this);
        IteeTextView tvCurrency = new IteeTextView(this);
        tvTotalCost = new IteeTextView(this);

        RelativeLayout rlShoppingCart = new RelativeLayout(this);
        ImageView ivShoppingCart = new ImageView(this);
        tvNumberOfGoods = new IteeTextView(this);

        RelativeLayout.LayoutParams rlShoppingCartContainerLayoutParams
                = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlShoppingCartContainerLayoutParams.height = getHeight(120);
        rlShoppingCartContainerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlShoppingCartContainer.setLayoutParams(rlShoppingCartContainerLayoutParams);

        RelativeLayout.LayoutParams tvTotalLayoutParams
                = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tvTotalLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        tvTotalLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        tvTotalLayoutParams.leftMargin = getWidth(80);

        tvTotal.setLayoutParams(tvTotalLayoutParams);
        tvTotal.setId(View.generateViewId());

        rlShoppingCartContainer.addView(tvTotal);

        RelativeLayout.LayoutParams tvCurrencyLayoutParams
                = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tvCurrencyLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        tvCurrencyLayoutParams.addRule(RelativeLayout.RIGHT_OF, tvTotal.getId());

        tvCurrency.setLayoutParams(tvCurrencyLayoutParams);
        tvCurrency.setId(View.generateViewId());

        rlShoppingCartContainer.addView(tvCurrency);

        RelativeLayout.LayoutParams tvTotalCostLayoutParams
                = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tvTotalCostLayoutParams.leftMargin = getWidth(5);
        tvTotalCostLayoutParams.addRule(RelativeLayout.RIGHT_OF, tvCurrency.getId());
        tvTotalCostLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

        tvTotalCost.setLayoutParams(tvTotalCostLayoutParams);
        tvTotalCost.setId(View.generateViewId());

        rlShoppingCartContainer.addView(tvTotalCost);

        RelativeLayout.LayoutParams rlShoppingCartLayoutParams
                = new RelativeLayout.LayoutParams(getWidth(120), getHeight(80));
        rlShoppingCartLayoutParams.rightMargin = getWidth(20);
        rlShoppingCartLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        rlShoppingCartLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

        rlShoppingCart.setLayoutParams(rlShoppingCartLayoutParams);

        rlShoppingCartContainer.addView(rlShoppingCart);
        rlShoppingCart.setBackgroundResource(R.drawable.bg_green_btn);

        int shoppingCartWidth = DensityUtil.getActualWidthOnThisDevice(65, ProductScanQrCodeActivity.this);
        int shoppingCartHeight = DensityUtil.getActualHeightOnThisDevice(65, ProductScanQrCodeActivity.this);

        RelativeLayout.LayoutParams ivShoppingCartLayoutParams = new RelativeLayout.LayoutParams(shoppingCartWidth, shoppingCartHeight);
        ivShoppingCartLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        ivShoppingCartLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
//        ivShoppingCartLayoutParams.leftMargin = getWidth(20);

        ivShoppingCart.setLayoutParams(ivShoppingCartLayoutParams);
        ivShoppingCart.setId(View.generateViewId());
        ivShoppingCart.setImageResource(R.drawable.icon_shopping_cart);

        rlShoppingCart.addView(ivShoppingCart);

        RelativeLayout.LayoutParams tvNumberOfGoodsLayoutParams = new RelativeLayout.LayoutParams(getWidth(30), getHeight(30));
        tvNumberOfGoodsLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        tvNumberOfGoodsLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        tvNumberOfGoodsLayoutParams.rightMargin = getWidth(20);
        tvNumberOfGoodsLayoutParams.bottomMargin = getWidth(15);

        tvNumberOfGoods.setLayoutParams(tvNumberOfGoodsLayoutParams);
        tvNumberOfGoods.setBackgroundResource(R.drawable.icon_number_bg);

        tvNumberOfGoods.setTextColor(getResources().getColor(R.color.common_white));
        tvNumberOfGoods.setTextSize(Constants.FONT_SIZE_MORE_SMALLER);
        tvNumberOfGoods.setGravity(Gravity.CENTER);

        rlShoppingCart.addView(tvNumberOfGoods);

        AppUtils.addTopSeparatorLine(rlShoppingCartContainer, this);

        parent.addView(rlShoppingCartContainer);

        tvTotal.setText(R.string.shopping_pay_total);
        tvTotal.setTextColor(getResources().getColor(R.color.common_red));
        tvTotal.setTextSize(Constants.FONT_SIZE_LARGER);

        tvCurrency.setText(AppUtils.getCurrentCurrency(this));
        tvCurrency.setTextColor(getResources().getColor(R.color.common_red));
        tvCurrency.setTextSize(Constants.FONT_SIZE_LARGER);

        tvTotalCost.setTextColor(getResources().getColor(R.color.common_red));
        tvTotalCost.setTextSize(Constants.FONT_SIZE_LARGER);

        tvTotalCost.setText(Constants.STR_0);
        tvNumberOfGoods.setVisibility(View.INVISIBLE);

        setTotalPrice();
    }

    @Override
    protected void handleCode(String code) {
        etInputCode.setText(code);
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
                Utils.hideKeyboard(ProductScanQrCodeActivity.this);
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
                Utils.hideKeyboard(ProductScanQrCodeActivity.this);
                finish();
            }
        });
        btn.setBackground(null);
        rlIconContainer.addView(btn);
        rlIconContainer.bringChildToFront(ivIcon);

        return rlIconContainer.getId();
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
        parent.setPadding(getWidth(5), getWidth(5), getWidth(5) ,getWidth(5));
        RelativeLayout.LayoutParams paramsEtInputCode = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DensityUtil.getActualHeightOnThisDevice(100, this));
        paramsEtInputCode.width = getWidth(600);
        paramsEtInputCode.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsEtInputCode.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        etInputCode.setLayoutParams(paramsEtInputCode);

        etInputCode.setId(View.generateViewId());
        etInputCode.setBackgroundResource(R.drawable.et_account_forgot_border);
        etInputCode.setHint(R.string.forgot_password_account);
        etInputCode.setHintTextColor(getResources().getColor(R.color.common_gray));
        etInputCode.setTextSize(Constants.FONT_SIZE_NORMAL);
        etInputCode.setPadding(getWidth(10), 0, 0, 0);
        etInputCode.setSingleLine();
        etInputCode.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        etInputCode.setSelectAllOnFocus(false);

        parent.addView(btnOk);
        RelativeLayout.LayoutParams paramsBtnOk = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DensityUtil.getActualHeightOnThisDevice(100, this));
        paramsBtnOk.width = getWidth(120);
        paramsBtnOk.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsBtnOk.addRule(RelativeLayout.RIGHT_OF, etInputCode.getId());
        btnOk.setLayoutParams(paramsBtnOk);

        btnOk.setBackgroundResource(R.drawable.button_send_random_password);
        btnOk.setText(R.string.common_ok);
        btnOk.setTextColor(getResources().getColor(R.color.common_white));

    }

    private void setTotalPrice() {
        double totalPrice = 0.0;
        int totalNumbers = 0;
        LinkedHashMap<String, ShoppingProduct> shoppingCart = AppUtils.getShoppingCart(this);
        for (String key : shoppingCart.keySet()) {
            ShoppingProduct shoppingProduct = shoppingCart.get(key);
            double price = Double.valueOf(Utils.isStringNotNullOrEmpty(shoppingProduct.getPromotePrice()) ? shoppingProduct.getPromotePrice() : shoppingProduct.getProductPrice());
            totalPrice += price * shoppingProduct.getProductNumber();
            totalNumbers += shoppingProduct.getProductNumber();
        }
        tvTotalCost.setText(Utils.get2DigitDecimalString(String.valueOf(totalPrice)));
        if (shoppingCart.size() > 0) {
            tvNumberOfGoods.setVisibility(View.VISIBLE);
            tvNumberOfGoods.setText(String.valueOf(totalNumbers));
        } else {
            tvNumberOfGoods.setVisibility(View.INVISIBLE);
        }
    }

    private void getProductByCode(String code) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(this));
        params.put(ApiKey.SHOPS_CODE, code);
        HttpManager<JsonProductByCode> httpManager = new HttpManager<JsonProductByCode>(this) {

            @Override
            public void onJsonSuccess(JsonProductByCode jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();

                if (returnCode == Constants.RETURN_CODE_20301) {
                    doSelectProduct(jo);
                } else {
                    Utils.showShortToast(ProductScanQrCodeActivity.this, msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        httpManager.startGet(this, ApiManager.HttpApi.ShoppingProductByCode, params);
    }

    private void doSelectProduct(JsonProductByCode jo) {
        if (Constants.SHOP_TYPE_PACKAGE.equals(jo.getSalesTypeId())) {
            doSelectPackageByCode(jo);
        } else if (Constants.SHOP_TYPE_PROMOTE.equals(jo.getSalesTypeId())) {
            doSelectPromoteByCode(jo);
        } else if (Constants.SHOP_TYPE_RENTAL.equals(jo.getSalesTypeId())) {
            doSelectProductByCode(jo);
        } else {
            doSelectProductByCode(jo);
        }
    }

    private void doSelectPackageByCode(final JsonProductByCode jo) {
        if (jo.getProductList().size() > 0) {
            int index = 0;
            showPackagePop(jo, index);
        }
    }

    private void showPackagePop(final JsonProductByCode jo, final int index) {
        if (index < jo.getProductList().size()) {
            JsonProductByCode.Product product = jo.getProductList().get(index);
            if (Constants.STR_1.equals(product.getProductAttrStatus())) {
                ShoppingGoodsListItemFragment.OnPropertyClickListener onPropertyClickListener = new ShoppingGoodsListItemFragment.OnPropertyClickListener() {
                    @Override
                    public void OnPropertyClick(int position, JsonShopsPropertyPriceOrQtyGet.DataItem dataItem, String name) {
                        if (position == 0) {
                            jo.getProductList().get(index).setProductAttrId(String.valueOf(dataItem.getPraId()));
                            nowPopUpWin.dismiss();

                            int nextIndex = index + 1;
                            showPackagePop(jo, nextIndex);
                        }
                    }
                };

                nowPopUpWin = new SelectAttributePopupWindow(this, product.getProductId(),
                        product.getProductName(), product.getProductPrice(), Constants.STR_2, Constants.STR_EMPTY, onPropertyClickListener);
                nowPopUpWin.showAtLocation(rlContainer, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            } else {
                int nextIndex = index + 1;
                showPackagePop(jo, nextIndex);
            }
        } else {
            addPackage(jo);
        }
    }

    private void doSelectPromoteByCode(final JsonProductByCode jo) {
        JsonProductByCode.Product product = jo.getProductList().get(0);
        if (Constants.STR_1.equals(product.getProductAttrStatus())) {
            ShoppingGoodsListItemFragment.OnPropertyClickListener onPropertyClickListener = new ShoppingGoodsListItemFragment.OnPropertyClickListener() {
                @Override
                public void OnPropertyClick(int position, JsonShopsPropertyPriceOrQtyGet.DataItem dataItem, String name) {
                    if (position == 0) {
                        jo.setAttrId(String.valueOf(dataItem.getPraId()));
                        jo.setPrice(String.valueOf(dataItem.getPraPrice()));
                        addProduct(jo);
                        nowPopUpWin.dismiss();
                    }
                }
            };

            nowPopUpWin = new SelectAttributePopupWindow(this, product.getProductId(),
                    product.getProductName(), product.getProductPrice(), Constants.STR_1, Constants.STR_EMPTY, onPropertyClickListener);
            nowPopUpWin.showAtLocation(rlContainer, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else {
            jo.setAttrId(product.getProductAttrId());
            addProduct(jo);
        }
    }

    private void doSelectProductByCode(final JsonProductByCode jo) {
        JsonProductByCode.Product product = jo.getProductList().get(0);
        if (Constants.STR_1.equals(jo.getAttriStatus())) {

            ShoppingGoodsListItemFragment.OnPropertyClickListener onPropertyClickListener = new ShoppingGoodsListItemFragment.OnPropertyClickListener() {
                @Override
                public void OnPropertyClick(int position, JsonShopsPropertyPriceOrQtyGet.DataItem dataItem, String name) {
                    if (position == 0) {
                        jo.setAttrId(String.valueOf(dataItem.getPraId()));
                        jo.setPrice(String.valueOf(dataItem.getPraPrice()));
                        addProduct(jo);
                        nowPopUpWin.dismiss();
                    }
                }
            };

            nowPopUpWin = new SelectAttributePopupWindow(this, product.getProductId(),
                    product.getProductName(), product.getProductPrice(), Constants.STR_2, Constants.STR_EMPTY,
                    onPropertyClickListener);
            nowPopUpWin.showAtLocation(rlContainer, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        } else {
            addProduct(jo);
        }
    }

    private void add2ShoppingCart(JsonProductByCode jo, String mapId) {
        LinkedHashMap<String, ShoppingProduct> shoppingCart = AppUtils.getShoppingCart(this);
        ShoppingProduct shoppingProduct = shoppingCart.get(mapId);
        if (shoppingProduct != null) {
            shoppingProduct.setProductNumber(shoppingProduct.getProductNumber() + 1);
        } else {
            shoppingProduct = getShoppingProduct(jo);
            shoppingProduct.setProductNumber(1);
        }
        shoppingCart.put(mapId, shoppingProduct);
        AppUtils.saveShoppingCart(shoppingCart, this);
        setTotalPrice();
    }

    private void addProduct(JsonProductByCode jo) {
        JsonProductByCode.Product product = jo.getProductList().get(0);
        if (!greenId.equals(product.getProductId())) {
            if (jo.getProductList().size() == 1) {
                String mapId = jo.getSalesTypeId() + Constants.STR_COMMA + product.getProductId() + Constants.STR_COMMA;
                if (Utils.isStringNotNullOrEmpty(product.getProductAttrId())) {
                    mapId += product.getProductAttrId();
                }
                add2ShoppingCart(jo, mapId);
            } else {
                String mapId = jo.getSalesTypeId() + Constants.STR_COMMA + jo.getId() + Constants.STR_COMMA;
                add2ShoppingCart(jo, mapId);
            }
        }
    }

    private void addPackage(JsonProductByCode jo) {
        String mapId = jo.getSalesTypeId() + Constants.STR_COMMA + jo.getId() + Constants.STR_COMMA;
        add2ShoppingCart(jo, mapId);
    }

    public ShoppingProduct getShoppingProduct(JsonProductByCode jo) {
        ShoppingProduct shoppingProduct = new ShoppingProduct();
        if (Constants.SHOP_TYPE_PACKAGE.equals(jo.getSalesTypeId())) {
            shoppingProduct.setPackageId(jo.getId());
        } else if (Constants.SHOP_TYPE_PROMOTE.equals(jo.getSalesTypeId())) {
            shoppingProduct.setPromoteId(jo.getId());
            if (Utils.isListNotNullOrEmpty(jo.getProductList())) {
                shoppingProduct.setProductId(jo.getProductList().get(0).getProductId());
            }
        } else {
            shoppingProduct.setProductId(jo.getId());
        }
        shoppingProduct.setProductAttribute(jo.getAttrId());
        shoppingProduct.setProductName(jo.getName());
        shoppingProduct.setProductPrice(jo.getPrice());
        if (Utils.isListNotNullOrEmpty(jo.getProductList())) {
            for (JsonProductByCode.Product product : jo.getProductList()) {
                ShoppingProduct.ShoppingSubProduct subProduct = new ShoppingProduct.ShoppingSubProduct();
                subProduct.setProductId(product.getProductId());
                subProduct.setProductName(product.getProductName());
                if (Utils.isStringNotNullOrEmpty(product.getProductAttrId())) {
                    subProduct.setProductAttrId(product.getProductAttrId());
                }
                subProduct.setProductAttr(product.getProductName());
                subProduct.setNumber(product.getProductNumber());
                subProduct.setPrice(product.getProductPrice());
                shoppingProduct.getProductList().add(subProduct);
            }
        }
        return shoppingProduct;
    }


    public int getWidth(float currentPx) {
        return (int) (currentPx / Constants.DESIGN_UI_WIDTH * (getResources().getDisplayMetrics().widthPixels));
    }

    public int getHeight(float currentPx) {
        return (int) (currentPx / Constants.DESIGN_UI_HEIGHT * (getResources().getDisplayMetrics().heightPixels));
    }
}
