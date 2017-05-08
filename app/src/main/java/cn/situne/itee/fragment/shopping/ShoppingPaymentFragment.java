/**
 * Project Name: itee
 * File Name:	 ShoppingPaymentFragment.java
 * Package Name: cn.situne.itee.fragment.shopping
 * Date:		 2015-04-14
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.shopping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.activity.QuickScanQrCodeActivity;
import cn.situne.itee.activity.ScanQrCodeActivity;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.ShoppingProduct;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.teetime.LocationListFragment;
import cn.situne.itee.fragment.teetime.TeeTimeAddFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonShoppingPaymentGet;
import cn.situne.itee.manager.jsonentity.JsonShoppingPurchaseAddReturn;
import cn.situne.itee.manager.jsonentity.JsonShoppingReturnData;
import cn.situne.itee.view.IteeButton;
import cn.situne.itee.view.IteeCheckBox;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.PurchaseAaPopup;
import cn.situne.itee.view.PurchasePlayersPopup;
import cn.situne.itee.view.ShoppingPurchaseItem;
import cn.situne.itee.view.SwipeLinearLayout;

/**
 * ClassName:ShoppingPaymentFragment <br/>
 * Function: 06-2 <br/>
 * Date: 2015-04-14 <br/>
 *
 * @author luochao
 * @version 1.0
 * @see
 */
public class ShoppingPaymentFragment extends BaseFragment {

    public final static int PURCHASE_FLAG_SHOPPING = 0;
    public final static int PURCHASE_FLAG_CHECKOUT = 1;

    private boolean isFirstInit;
    private String purchaseBookingNo;//

    private List<JsonShoppingPaymentGet.DataItem> dataList;
    private LinearLayout llBody;
    private LinearLayout llFooter;
    //footerView
    private IteeCheckBox cbAll;
    private IteeCheckBox cbFifty;
    private IteeTextView tvSubTotal;
    private IteeTextView tvTax;
    private IteeTextView tvTotal;
    private ShoppingPaymentFragment.PurchaseItemListener purchaseItemListener;
    private ArrayList<ShoppingPurchaseItem> viewItems;
    private ArrayList<ShoppingPurchaseItem.RowItem> checkedProductList;
    private String bookingNo;
    private int purchaseFlag;
    private ArrayList<String> chooseProductList;
    private SwipeLinearLayout selectSwipeLinearLayout;

    private ShoppingPurchaseItem.EditLayout oldE;

    private ScrollView scrollView;
    private String fromPage;
    private String sameDayFlag;
    private IteeButton btnConfirm;

    private boolean isDeposit;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_shopping_payment;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(final View rootView) {
        isFirstInit = true;
        Bundle bundle = getArguments();
        if (bundle != null) {
            bookingNo = bundle.getString(TransKey.SHOPPING_BOOKING_NO);
            purchaseFlag = bundle.getInt(TransKey.SHOPPING_PURCHASE_FLAG);
            chooseProductList = bundle.getStringArrayList(TransKey.CHOOSE_PRODUCT_LIST);
            fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE);
            sameDayFlag = bundle.getString(TransKey.SAME_DAY_FLAG);
        }
        if (TeeTimeAddFragment.class.getName().equals(fromPage)) {
            isDeposit = true;
        }

        dataList = new ArrayList<>();
        viewItems = new ArrayList<>();
        checkedProductList = new ArrayList<>();

        // setTestData();
        llBody = (LinearLayout) rootView.findViewById(R.id.ll_body);
        llFooter = (LinearLayout) rootView.findViewById(R.id.ll_footer);

        scrollView = (ScrollView) rootView.findViewById(R.id.scrollView);
        purchaseItemListener = new PurchaseItemListener() {
            @Override
            public void clickEditLayoutOk(int editType, final int num, final ShoppingPurchaseItem.RowItem rowItem) {
                final int itemType = rowItem.getItemType();
                if (editType == ShoppingPurchaseItem.EditLayout.SPLIT_TYPE) {
                    Map<String, String> players = new HashMap<>();
                    for (ShoppingPurchaseItem item : viewItems) {
                        if (!item.getBookingNo().equals(rowItem.getBookingNo())) {
                            players.put(item.getBookingNo(), item.getPlayerName());
                        }
                    }
                    PurchasePlayersPopup.createBuilder(ShoppingPaymentFragment.this, getFragmentManager()).setPlayers(players).setSelectListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String bookingNo = String.valueOf(v.getTag());
                            ShoppingPurchaseItem splitGoPlayer = findViewItemWithBookingNo(bookingNo);
                            if (itemType == ShoppingPurchaseItem.RowItem.ROW_ITEM_TYPE_PRO) {
                                purchaseSplitPut(rowItem, splitGoPlayer, 2, num);
                            } else {
                                purchaseSplitPut(rowItem, splitGoPlayer, 1, num);
                            }
                        }

                    }).show();
                } else {
                    int id = 0;
                    if (itemType == ShoppingPurchaseItem.RowItem.ROW_ITEM_TYPE_PRO) {
                        id = rowItem.getProData().getId();
                    }
                    if (itemType == ShoppingPurchaseItem.RowItem.ROW_ITEM_TYPE_PACKAGE) {
                        id = rowItem.getPackageData().getId();
                    }
                    purchasePut(num, id, rowItem);
                }
            }

            @Override
            public void clickItemCheckBox(boolean checked, ShoppingPurchaseItem.RowItem rowItem) {
                setCheckedProductList();
                if (maybeUndo()) {
                    cbFifty.setText(getString(R.string.common_undo));
                    cbFifty.setIcon(R.drawable.icon_purchase_undo, R.drawable.icon_purchase_undo);
                } else if (maybeAa()) {
                    cbFifty.setText(getString(R.string.shopping_go_fifty_fifty));
                    cbFifty.setIcon(R.drawable.icon_purchase_fifty, R.drawable.icon_purchase_fifty);
                } else {
                    cbFifty.setText(getString(R.string.shopping_go_fifty_fifty));
                    cbFifty.setIcon(R.drawable.icon_purchase_not_fifty, R.drawable.icon_purchase_not_fifty);
                }
                cbAll.setChecked(isAll());
            }

            @Override
            public void gotoShop(String bookingNo, String playerName) {
                Bundle bundle = new Bundle();
                bundle.putString(TransKey.COMMON_FROM_PAGE, ShoppingPaymentFragment.class.getName());
                bundle.putString(TransKey.SHOPPING_BOOKING_NO, bookingNo);
                bundle.putString(TransKey.SHOPPING_PLAYER_NAME, playerName);
                push(ShoppingGoodsListFragment.class, bundle);
            }

            @Override
            public void delProductItem(String id, ShoppingPurchaseItem.RowItem rowItem, ShoppingPurchaseItem player) {
                purchaseDelete(id, rowItem, player);
            }

            @Override
            public void gotoChoosePackage(String bookingNo, String playerName) {
                Bundle bundle = new Bundle();
                bundle.putString(TransKey.COMMON_FROM_PAGE, ShoppingPaymentFragment.class.getName());
                bundle.putString(TransKey.SHOPPING_BOOKING_NO, bookingNo);
                bundle.putString(TransKey.SHOPPING_PLAYER_NAME, playerName);

                ShoppingPurchaseItem selectItem;
                if (Utils.isStringNotNullOrEmpty(bookingNo)) {
                    selectItem = findViewItemWithBookingNo(bookingNo);
                } else {
                    selectItem = viewItems.get(0);
                }

                JSONArray jsonArray = new JSONArray();
                try {
                    if (selectItem != null) {
                        for (ShoppingPurchaseItem.RowItem rowItem : selectItem.getRowItemList()) {
                            if (rowItem.getItemType() == ShoppingPurchaseItem.RowItem.ROW_ITEM_TYPE_PRO) {
                                JSONObject jsItem = new JSONObject();
                                jsItem.put(ApiKey.SHOPPING_PURCHASE_PRODUCT_ID, String.valueOf(rowItem.getProData().getProductId()));
                                jsItem.put(ApiKey.SHOPPING_ATTR_ID, rowItem.getProData().getAttriId());
                                jsonArray.put(jsItem);
                            }
                        }
                    }
                    bundle.putString(TransKey.SHOPPING_AVAILABLE_PRODUCT, jsonArray.toString());
                } catch (JSONException e) {
                    Utils.log(e.getMessage());
                }
                push(ShoppingChoosePackageFragment.class, bundle);
            }

            @Override
            public void swipeScroll(SwipeLinearLayout layout) {
                if (selectSwipeLinearLayout == null) {
                    selectSwipeLinearLayout = layout;
                } else {
                    selectSwipeLinearLayout.hideRight();
                }
                selectSwipeLinearLayout = layout;
            }

            @Override
            public void deleteTimes(String TimesId, String times, ShoppingPurchaseItem.RowItem rowItem, ShoppingPurchaseItem player) {
                deleteTimesApi(player.getBookingNo(), times, TimesId, rowItem, player);
            }

            @Override
            public void showItem(final int i, ShoppingPurchaseItem.EditLayout e) {
                if (null != oldE) {
                    oldE.setVisibility(View.GONE);
                }
                oldE = e;
                int[] location = new int[2];
                scrollView.getLocationOnScreen(location);
                final int y = location[1];
                if (i > scrollView.getBottom() + y) {
                    scrollView.post(new Runnable() {
                        public void run() {
                            scrollView.scrollBy(0, (i - scrollView.getBottom() - y));
                        }
                    });
                }
            }

            @Override
            public void closeItem() {
                oldE = null;
            }
        };

        initFooterLayout();

        if (chooseProductList != null && bookingNo == null) {
            JsonShoppingPaymentGet.DataItem dataItem = new JsonShoppingPaymentGet.DataItem();
            ArrayList<JsonShoppingPaymentGet.PackageData> packageDataList = new ArrayList<>();
            ArrayList<JsonShoppingPaymentGet.ProData> proDataList = new ArrayList<>();
            for (String product : chooseProductList) {
                ShoppingProduct shoppingProduct = (ShoppingProduct) Utils.getObjectFromString(product);
                if (shoppingProduct.getPackageId() == null || shoppingProduct.getPackageId().equals(Constants.STR_0)) {
                    JsonShoppingPaymentGet.ProData proData = new JsonShoppingPaymentGet.ProData();
                    proData.setPayStatus(0);
                    proData.setAttriId(shoppingProduct.getProductAttribute());
                    proData.setAttriName(shoppingProduct.getAttributeName());
                    proData.setDisCountPrice(Double.parseDouble(shoppingProduct.getProductPrice()));
                    proData.setPrice(Double.parseDouble(shoppingProduct.getProductPrice()));
                    proData.setQty(shoppingProduct.getProductNumber());
                    proData.setProductName(shoppingProduct.getProductName());
                    proData.setPromoteId(shoppingProduct.getPromoteId());
                    proData.setProductId(shoppingProduct.getProductId());
                    if (shoppingProduct.isCaddie())
                        proData.setPdSort(Constants.STR_1);
                    if (Utils.isStringNotNullOrEmpty(shoppingProduct.getPromoteId()) && !Constants.STR_0.equals(shoppingProduct.getPromoteId())) {
                        proData.setProductId(shoppingProduct.getProductList().get(0).getProductId());
                    }
                    if (dataItem.getProDataList() == null) {
                        proDataList.add(proData);
                        dataItem.setProDataList(proDataList);
                    } else {
                        dataItem.getProDataList().add(proData);
                    }
                } else {
                    JsonShoppingPaymentGet.PackageData packageData = new JsonShoppingPaymentGet.PackageData();
                    packageData.setPackageId(shoppingProduct.getPackageId());
                    packageData.setPackageName(shoppingProduct.getProductName());
                    packageData.setPackagePrice(Double.parseDouble(shoppingProduct.getProductPrice()));
                    packageData.setPayStatus(0);
                    packageData.setPackageNumber(shoppingProduct.getProductNumber());
                    ArrayList<JsonShoppingPaymentGet.ProData> packageList = new ArrayList<>();
                    for (ShoppingProduct.ShoppingSubProduct subProduct : shoppingProduct.getProductList()) {
                        JsonShoppingPaymentGet.ProData proData = new JsonShoppingPaymentGet.ProData();
                        proData.setPayStatus(0);
                        proData.setAttriId(subProduct.getProductAttrId());
                        proData.setAttriName(subProduct.getProductAttr());
                        proData.setDisCountPrice(Double.parseDouble(subProduct.getPrice()));
                        proData.setPrice(Double.parseDouble(subProduct.getPrice()));
                        proData.setQty(Integer.parseInt(subProduct.getNumber()));
                        proData.setProductName(subProduct.getProductName());
                        proData.setProductId(subProduct.getProductId());
                        packageList.add(proData);
                    }
                    packageData.setPackageList(packageList);
                    packageDataList.add(packageData);
                    dataItem.setPackageList(packageDataList);
                }
            }

            ShoppingPurchaseItem item = new ShoppingPurchaseItem(ShoppingPaymentFragment.this, purchaseItemListener, dataItem, purchaseFlag);
            viewItems.add(item);
            llBody.addView(item);
            setCheckedProductList();
            item.getPlayerRadioBtn().setChecked(true);
        }

        if (bookingNo != null) {
            getPurchaseDataOfBookingNo(bookingNo, true);
        }
    }

    @Override
    protected void setDefaultValueOfControls() {
        tvTax.setVisibility(View.GONE);
        tvTotal.setVisibility(View.GONE);
    }

    @Override
    protected void setListenersOfControls() {

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
        getTvLeftTitle().setText(R.string.shopping_purchase);
        getTvRight().setBackgroundResource(R.drawable.icon_add_m);
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseActivity(), QuickScanQrCodeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(TransKey.COMMON_FROM_PAGE, ShoppingPaymentFragment.class.getName());
                startActivityForResult(intent, ScanQrCodeActivity.SCANNING_GREQUEST_CODE_2);
            }
        });

        setOnBackListener(new OnPopBackListener() {
            @Override
            public boolean preDoBack() {
                if (Utils.isStringNotNullOrEmpty(fromPage)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("toName", ShoppingPaymentFragment.class.getName());
                    try {
                        doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    doBackWithRefresh();
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ScanQrCodeActivity.SCANNING_GREQUEST_CODE_2:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String qrCode = bundle.getString(TransKey.BOOKING_CODE);

//
//                    if (viewItems != null&&viewItems.size()>0&&Utils.isStringNullOrEmpty(viewItems.get(0).getBookingNo())){
//                        ArrayList<JsonShoppingPaymentGet.ProData> upProduct = new ArrayList<>();
//                        purchasePost(getAddDataNotBookingNo(null, qrCode), "", upProduct,Constants.STR_2);
//                    }else{
                    getPurchaseDataOfBookingNo(qrCode, false);

                    //}
                }
                break;
        }
    }


    public void nfcGetData(String qrCode) {
        getPurchaseDataOfBookingNo(qrCode, false);
    }

    @Override
    protected void reShowWithBackValue() {
        Bundle bundle = getReturnValues();
        StringBuilder delSb = new StringBuilder();
        ArrayList<JsonShoppingPaymentGet.ProData> upProduct = new ArrayList<>();
        if (bundle != null) {

            String fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE);
            String bookingNo = bundle.getString(TransKey.SHOPPING_BOOKING_NO);
            String playerName = bundle.getString(TransKey.SHOPPING_PLAYER_NAME);
            if (ShoppingChoosePackageFragment.class.getName().equals(fromPage)) {
                ArrayList<ShoppingPurchaseItem.RowItem> delList = new ArrayList<>();
                ShoppingPurchaseItem selectItem;
                if (bookingNo == null) {
                    selectItem = viewItems.get(0);
                } else {
                    selectItem = findViewItemWithBookingNo(bookingNo);
                }

                ShoppingProduct shoppingProduct = (ShoppingProduct) Utils.getObjectFromString(bundle.getString(TransKey.SHOPPING_PACKAGE));
                JsonShoppingPaymentGet.DataItem addData = new JsonShoppingPaymentGet.DataItem();
                ArrayList<JsonShoppingPaymentGet.PackageData> proDataList = new ArrayList<>();
                JsonShoppingPaymentGet.PackageData packageData = new JsonShoppingPaymentGet.PackageData();
                packageData.setPackageId(shoppingProduct.getPackageId());
                packageData.setPackageName(shoppingProduct.getProductName());
                packageData.setPackagePrice(Double.parseDouble(shoppingProduct.getProductPrice()));
                packageData.setPayStatus(0);
                packageData.setPackageNumber(shoppingProduct.getProductNumber());
                ArrayList<JsonShoppingPaymentGet.ProData> packageList = new ArrayList<>();
                for (ShoppingProduct.ShoppingSubProduct product : shoppingProduct.getProductList()) {
                    JsonShoppingPaymentGet.ProData proData = new JsonShoppingPaymentGet.ProData();
                    proData.setPayStatus(0);
                    proData.setAttriId(product.getProductAttrId());
                    proData.setAttriName(product.getProductAttr());
                    proData.setDisCountPrice(Double.parseDouble(product.getPrice()));
                    proData.setPrice(Double.parseDouble(product.getPrice()));
                    proData.setQty(Integer.parseInt(product.getNumber()));
                    proData.setProductName(product.getProductName());
                    proData.setProductId(product.getProductId());

                    packageList.add(proData);

                    if (Constants.STR_1.equals(product.getProductStatus())) {
                        String matchProductId = product.getProductId();
                        String matchAttrId = product.getProductAttrId();
                        int matchNumber = Integer.parseInt(product.getNumber());

                        if (selectItem != null) {
                            for (ShoppingPurchaseItem.RowItem rowItem : selectItem.getRowItemList()) {
                                if (rowItem.getItemType() == ShoppingPurchaseItem.RowItem.ROW_ITEM_TYPE_PRO) {

                                    JsonShoppingPaymentGet.ProData viewProData = rowItem.getProData();
                                    if (viewProData.getProductId().equals(matchProductId)
                                            && viewProData.getAttriId().equals(matchAttrId)) {

                                        if (viewProData.getQty() < matchNumber) {
                                            matchNumber = matchNumber - viewProData.getQty();
                                            delSb.append(viewProData.getId());
                                            delSb.append(Constants.STR_COMMA);
                                            delList.add(rowItem);

                                        } else if (viewProData.getQty() == matchNumber) {
                                            delSb.append(viewProData.getId());
                                            delSb.append(Constants.STR_COMMA);
                                            delList.add(rowItem);
                                        } else if (viewProData.getQty() > matchNumber) {
                                            JsonShoppingPaymentGet.ProData upPro = new JsonShoppingPaymentGet.ProData();
                                            upPro.setId(viewProData.getId());
                                            upPro.setQty(viewProData.getQty() - matchNumber);

                                            upProduct.add(upPro);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                packageData.setPackageList(packageList);
                proDataList.add(packageData);
                addData.setPackageList(proDataList);
                addData.setBookingNo(bookingNo);
                addData.setPlayerName(playerName);


                String delStr = delSb.toString();
                if (delStr.length() > 0) {
                    delStr = delStr.substring(0, delStr.length() - 1);
                }

                if (bookingNo == null) {
                    ArrayList<JsonShoppingPaymentGet.DataItem> addList = new ArrayList<>();
                    addList.add(addData);
                    addProductToPlayer(null, addList, delStr, upProduct, delList);

                } else {
                    purchasePost(addData, delStr, upProduct, Constants.STR_1);

                }

            } else if (ShoppingPaymentSucceededFragment.class.getName().equals(fromPage)) {
                paidItems();
            } else {

                ArrayList<String> chooseProductList = bundle.getStringArrayList(TransKey.CHOOSE_PRODUCT_LIST);
                JsonShoppingPaymentGet.DataItem playerData = new JsonShoppingPaymentGet.DataItem();
                ArrayList<JsonShoppingPaymentGet.ProData> proDataList = new ArrayList<>();
                ArrayList<JsonShoppingPaymentGet.PackageData> packageDataList = new ArrayList<>();
                if (chooseProductList != null) {
                    for (String product : chooseProductList) {
                        ShoppingProduct shoppingProduct = (ShoppingProduct) Utils.getObjectFromString(product);
                        if (shoppingProduct.getPackageId() == null || shoppingProduct.getPackageId().equals(Constants.STR_0)) {
                            JsonShoppingPaymentGet.ProData proData = new JsonShoppingPaymentGet.ProData();
                            proData.setPayStatus(0);
                            proData.setAttriId(shoppingProduct.getProductAttribute());
                            proData.setAttriName(shoppingProduct.getAttributeName());
                            proData.setDisCountPrice(Double.parseDouble(shoppingProduct.getProductPrice()));
                            proData.setPrice(Double.parseDouble(shoppingProduct.getProductPrice()));
                            proData.setQty(shoppingProduct.getProductNumber());
                            proData.setProductName(shoppingProduct.getProductName());
                            proData.setProductId(shoppingProduct.getProductId());
                            proData.setPromoteId(shoppingProduct.getPromoteId());
                            if (shoppingProduct.isCaddie()) proData.setPdSort(Constants.STR_1);
                            proDataList.add(proData);
                            playerData.setProDataList(proDataList);
                            playerData.setBookingNo(bookingNo);
                            playerData.setPlayerName(playerName);


                        } else {


                            JsonShoppingPaymentGet.PackageData packageData = new JsonShoppingPaymentGet.PackageData();
                            packageData.setPackageId(shoppingProduct.getPackageId());
                            packageData.setPackageName(shoppingProduct.getProductName());
                            packageData.setPackagePrice(Double.parseDouble(shoppingProduct.getProductPrice()));
                            packageData.setPayStatus(0);
                            packageData.setPackageNumber(shoppingProduct.getProductNumber());
                            ArrayList<JsonShoppingPaymentGet.ProData> packageList = new ArrayList<>();
                            for (ShoppingProduct.ShoppingSubProduct subProduct : shoppingProduct.getProductList()) {
                                JsonShoppingPaymentGet.ProData proData = new JsonShoppingPaymentGet.ProData();
                                proData.setPayStatus(0);
                                proData.setAttriId(subProduct.getProductAttrId());
                                proData.setAttriName(subProduct.getProductAttr());
                                proData.setDisCountPrice(Double.parseDouble(subProduct.getPrice()));
                                proData.setPrice(Double.parseDouble(subProduct.getPrice()));
                                proData.setQty(Integer.parseInt(subProduct.getNumber()));
                                proData.setProductName(subProduct.getProductName());
                                proData.setProductId(subProduct.getProductId());
                                packageList.add(proData);
                            }

                            packageData.setPackageList(packageList);
                            packageDataList.add(packageData);
                            playerData.setPackageList(packageDataList);
                            playerData.setBookingNo(bookingNo);
                            playerData.setPlayerName(playerName);


                        }
                    }
                }

                if (Utils.isStringNullOrEmpty(viewItems.get(0).getBookingNo())) {

                    ArrayList<JsonShoppingPaymentGet.DataItem> addDataList = new ArrayList<>();
                    addDataList.add(playerData);

                    addProductToPlayer(null, addDataList, delSb.toString(), upProduct, null);
                } else {
                    purchasePost(playerData, delSb.toString(), upProduct, Constants.STR_1);
                }
            }


        }


    }

    private ShoppingPurchaseItem findViewItemWithBookingNo(String bookingNo) {
        for (ShoppingPurchaseItem item : viewItems) {
            if (bookingNo.equals(item.getBookingNo())) {
                return item;
            }
        }
        return null;
    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        AppUtils.removeShoppingCart(getActivity());
    }

    private void initFooterLayout() {
        LinearLayout.LayoutParams footerBodyParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout footerBody = new RelativeLayout(getBaseActivity());
        footerBody.setLayoutParams(footerBodyParams);
        RelativeLayout.LayoutParams cbAllParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        cbAllParams.leftMargin = getActualWidthOnThisDevice(40);
        cbAllParams.topMargin = getActualHeightOnThisDevice(30);
        cbAll = new IteeCheckBox(this, getActualHeightOnThisDevice(50), R.drawable.blue_check_false, R.drawable.blue_check_true);
        cbAll.setText(getString(R.string.shopping_all));
        cbAll.setId(View.generateViewId());
        cbAll.setLayoutParams(cbAllParams);
        cbAll.setClickView(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbAll.getChecked()) {
                    setAll(true);
                } else {
                    setAll(false);
                }
                setCheckedProductList();

                if (maybeUndo()) {

                    cbFifty.setText(getString(R.string.common_undo));
                    cbFifty.setIcon(R.drawable.icon_purchase_undo, R.drawable.icon_purchase_undo);

                } else if (maybeAa()) {
                    cbFifty.setText(getString(R.string.shopping_go_fifty_fifty));
                    cbFifty.setIcon(R.drawable.icon_purchase_fifty, R.drawable.icon_purchase_fifty);
                } else {

                    cbFifty.setText(getString(R.string.shopping_go_fifty_fifty));
                    cbFifty.setIcon(R.drawable.icon_purchase_not_fifty, R.drawable.icon_purchase_not_fifty);

                }

            }
        });

        RelativeLayout.LayoutParams tvFiftyParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvFiftyParams.addRule(RelativeLayout.BELOW, cbAll.getId());
        tvFiftyParams.leftMargin = getActualWidthOnThisDevice(40);
        tvFiftyParams.topMargin = getActualHeightOnThisDevice(15);
        cbFifty = new IteeCheckBox(this, getActualHeightOnThisDevice(50), R.drawable.icon_purchase_not_fifty, R.drawable.icon_purchase_not_fifty);
        cbFifty.setLayoutParams(tvFiftyParams);
        cbFifty.setText(getString(R.string.shopping_go_fifty_fifty));

        cbFifty.setClickType(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheckedProductList();
                if (maybeAa()) {
                    PurchaseAaPopup.createBuilder(ShoppingPaymentFragment.this, getFragmentManager())
                            .setAaPopupListener(new PurchaseAaPopup.AaPopupListener() {
                                @Override
                                public void postDone(ArrayList<ShoppingPurchaseItem.RowItem> checkedProductList,
                                                     ArrayList<JsonShoppingPaymentGet.DataItem> returnAaList,
                                                     double sameMoney, String sameWithString) {

                                    addAaData(returnAaList, checkedProductList, sameMoney, sameWithString);

                                    changeFooterText();

//                                    if (isAll()) {
//
//                                        cbAll.setChecked(true);
//                                    } else {
//
//                                        cbAll.setChecked(false);
//                                    }
                                    if (cbAll.getChecked()) {
                                        for (ShoppingPurchaseItem item : viewItems) {
                                            for (ShoppingPurchaseItem.RowItem rowItem : item.getRowItemList()) {
                                                rowItem.getTitleItem().getItemCheckBox().setChecked(true);
                                            }
                                        }
                                    }
                                    setCheckedProductList();

                                }


                            })
                            .setViewItems(viewItems)
                            .setCancelableOnTouchOutside(false)
                            .setCheckedProductList(checkedProductList)
                            .setSelectListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                }
                            })
                            .show();
                }
                if (maybeUndo()) {
                    purchaseAaPut();
                }

            }
        });


        RelativeLayout.LayoutParams tvSubTotalParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvSubTotalParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tvSubTotalParams.topMargin = getActualHeightOnThisDevice(20);
        tvSubTotalParams.rightMargin = getActualWidthOnThisDevice(10);
        tvSubTotal = new IteeTextView(getBaseActivity());
        tvSubTotal.setLayoutParams(tvSubTotalParams);
        tvSubTotal.setId(View.generateViewId());
        tvSubTotal.setText(getString(R.string.shopping_subtotal) + Constants.STR_EMPTY);
        tvSubTotal.setTextColor(getColor(R.color.common_black));
        tvSubTotal.setTextSize(Constants.FONT_SIZE_NORMAL);


        RelativeLayout.LayoutParams tvTaxParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvTaxParams.addRule(RelativeLayout.BELOW, tvSubTotal.getId());
        tvTaxParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tvTaxParams.rightMargin = getActualWidthOnThisDevice(10);
        tvTax = new IteeTextView(getBaseActivity());
        tvTax.setLayoutParams(tvTaxParams);
        tvTax.setId(View.generateViewId());
        tvTax.setText(getString(R.string.shopping_tax) + Constants.STR_EMPTY);
        tvTax.setTextColor(getColor(R.color.common_black));
        tvTax.setTextSize(Constants.FONT_SIZE_NORMAL);

        RelativeLayout.LayoutParams tvTotalParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvTotalParams.addRule(RelativeLayout.BELOW, tvTax.getId());
        tvTotalParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tvTotalParams.rightMargin = getActualWidthOnThisDevice(10);
        tvTotal = new IteeTextView(getBaseActivity());
        tvTotal.setLayoutParams(tvTotalParams);
        tvTotal.setText(getString(R.string.common_total) + Constants.STR_EMPTY);
        tvTotal.setTextColor(getColor(R.color.common_black));
        tvTotal.setTextSize(Constants.FONT_SIZE_NORMAL);

        LinearLayout.LayoutParams btnConfirmParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        btnConfirmParams.width = AppUtils.getLargerButtonWidth(this);
        btnConfirmParams.height = AppUtils.getLargerButtonHeight(this);
        btnConfirmParams.topMargin = getActualHeightOnThisDevice(30);
        btnConfirmParams.bottomMargin = getActualHeightOnThisDevice(30);

        btnConfirm = new IteeButton(getBaseActivity());
        btnConfirm.setLayoutParams(btnConfirmParams);
        btnConfirm.setText(getString(R.string.shopping_confirm_to_pay));
        if (purchaseFlag == PURCHASE_FLAG_CHECKOUT) {

            btnConfirm.setText(getString(R.string.staff_check_out));
        }
        btnConfirm.setBackgroundResource(R.drawable.bg_green_btn);
        btnConfirm.setTextColor(getColor(R.color.common_white));

        //region  btnConfirm.setOnClickListener
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       //确认支付
                if (doPurchaseCheck()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.SHOPPING_PURCHASE_FLAG, purchaseFlag);
                    bundle.putString(TransKey.COMMON_FROM_PAGE, fromPage);
                    bundle.putString(TransKey.SHOPPING_PRODUCT_IDS, getPurchaseProductIds());
                    bundle.putString(TransKey.SHOPPING_BOOKING_NO, bookingNo);
                    bundle.putString(TransKey.SHOPPING_DETAIL_BOOKING_NO, getDetailBookingNo());
                    if (Utils.isStringNullOrEmpty(viewItems.get(0).getBookingNo())) {
                        bundle.putInt(TransKey.SHOPPING_BOOKING_FLAG, Constants.SHOPS_BOOKING_FLAG_2);
                    } else {
                        bundle.putInt(TransKey.SHOPPING_BOOKING_FLAG, Constants.SHOPS_BOOKING_FLAG_1);
                    }
                    bundle.putString(TransKey.SHOPPING_CHECK_PAY_PRODUCT_LIST, getCheckPayProductList());
                    push(ShoppingConfirmPay_Fragment.class, bundle);
                }
            }
        });
        //endregion

        footerBody.addView(cbAll);
        footerBody.addView(cbFifty);
        footerBody.addView(tvSubTotal);
        footerBody.addView(tvTax);
        footerBody.addView(tvTotal);
        //footerBody.addView(btnConfirm);


        llFooter.addView(AppUtils.getSeparatorLine(this));
        llFooter.addView(footerBody);

        LinearLayout.LayoutParams btnConfirmBodyParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout btnConfirmBody = new LinearLayout(getBaseActivity());

        btnConfirmBody.setLayoutParams(btnConfirmBodyParams);
        btnConfirmBody.setGravity(Gravity.CENTER);
        btnConfirmBody.addView(btnConfirm);


        llFooter.addView(btnConfirmBody);

    }

    private boolean doPurchaseCheck() {

        setCheckedProductList();

        if (checkedProductList.size() <= 0) {

            Utils.showShortToast(getBaseActivity(), getString(R.string.shopping_purchase_err_mes));
            return false;
        }

        if (purchaseFlag == PURCHASE_FLAG_CHECKOUT) {
            ShoppingPurchaseItem selectItem = findViewItemWithBookingNo(purchaseBookingNo);
            if (selectItem != null) {
                for (ShoppingPurchaseItem.RowItem rowItem : selectItem.getRowItemList()) {

                    if (!rowItem.getTitleItem().getItemCheckBox().getChecked()) {
                        Utils.showShortToast(getBaseActivity(), getString(R.string.shopping_purchase_err_mes));
                        return false;
                    }

                }
            }

        }

        return true;
    }

    private String getPurchaseProductIds() {
        StringBuilder sb = new StringBuilder();
        //setCheckedProductList();

        for (ShoppingPurchaseItem.RowItem rowItem : checkedProductList) {


            if (rowItem.isPricing()) {
                for (JsonShoppingPaymentGet.PricingData pricingData : rowItem.getmPlayerData().getPricingDataList()) {

                    sb.append(pricingData.getId());
                    sb.append(Constants.STR_COMMA);
                }

            } else {

                if (rowItem.getProData() != null) {
                    sb.append(rowItem.getProData().getId());
                    sb.append(Constants.STR_COMMA);
                }

                if (rowItem.getPackageData() != null) {
                    sb.append(rowItem.getPackageData().getId());
                    sb.append(Constants.STR_COMMA);
                }
                if (rowItem.getFiftyData() != null) {

                    for (JsonShoppingPaymentGet.ProData pro : rowItem.getFiftyData().getAaList()) {

                        sb.append(pro.getId());
                        sb.append(Constants.STR_COMMA);

                    }
                }
            }


        }

        String res = sb.toString();

        if (res.length() > 0) {
            res = res.substring(0, res.length() - 1);
        }
        return res;

    }

    private void changeFooterText() {

        double subtotal = 0;
        int checkedNum = 0;
        for (ShoppingPurchaseItem item : viewItems) {

            for (ShoppingPurchaseItem.RowItem rowItem : item.getRowItemList()) {

                if (rowItem.getTitleItem().getItemCheckBox().getChecked()) {
                    checkedNum++;
                    String numStr = rowItem.getTitleItem().getTvNum().getText().toString();

                    int num = 1;
                    if (Utils.isStringNotNullOrEmpty(numStr)) {
                        numStr = numStr.substring(1, numStr.length());
                        num = Integer.parseInt(numStr);
                    }

                    String moneyStr = rowItem.getTitleItem().getTvMoney().getText().toString();
                    moneyStr = moneyStr.substring(1, moneyStr.length());
                    double money = Double.parseDouble(moneyStr);


                    if (rowItem.isPricing()) {
                        money = Double.parseDouble(rowItem.getPricingPrice());
                        num = 1;
                    }
                    subtotal += num * money;
                }

            }

        }
        tvSubTotal.setText(getString(R.string.shopping_subtotal) + Constants.STR_EMPTY + Constants.STR_COLON
                + Constants.STR_EMPTY + AppUtils.getCurrentCurrency(getBaseActivity())
                + Utils.get2DigitDecimalString(String.valueOf(subtotal)));
        if (checkedNum <= 0) {

            tvSubTotal.setVisibility(View.GONE);
        } else {

            tvSubTotal.setVisibility(View.VISIBLE);
        }

        double tax = Double.parseDouble(AppUtils.getSalesTax(getActivity())) / 100 * subtotal;

        DecimalFormat b = new DecimalFormat("0.00");
        tvTax.setText(getString(R.string.shopping_tax) + Constants.STR_EMPTY + Constants.STR_COLON + Constants.STR_EMPTY
                + AppUtils.getCurrentCurrency(getBaseActivity()) + Utils.get2DigitDecimalString(b.format(tax)));
        if (AppUtils.isTaxExcludeGoods(getActivity())) {

            tvTotal.setText(getString(R.string.common_total) + Constants.STR_EMPTY + Constants.STR_COLON
                    + Constants.STR_EMPTY + AppUtils.getCurrentCurrency(getBaseActivity())
                    + Utils.get2DigitDecimalString(String.valueOf(tax + subtotal)));
            // total = tax + subtotal;
        } else {

            tvTotal.setText(getString(R.string.common_total) + Constants.STR_EMPTY + Constants.STR_COLON
                    + Constants.STR_EMPTY + AppUtils.getCurrentCurrency(getBaseActivity())
                    + Utils.get2DigitDecimalString(String.valueOf(subtotal)));
            // total = subtotal;
        }


    }

    private boolean isAll() {
        for (ShoppingPurchaseItem item : viewItems) {
            for (ShoppingPurchaseItem.RowItem rowItem : item.getRowItemList()) {
                if (!rowItem.getTitleItem().getItemCheckBox().getChecked()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void setAll(boolean all) {
        for (ShoppingPurchaseItem item : viewItems) {
            for (ShoppingPurchaseItem.RowItem rowItem : item.getRowItemList()) {
                rowItem.getTitleItem().getItemCheckBox().setChecked(all);
            }
        }
    }

    private void setCheckedProductList() {

        checkedProductList.clear();
        btnConfirm.setBackgroundResource(R.drawable.bg_gray_btn);
        for (ShoppingPurchaseItem item : viewItems) {
            for (ShoppingPurchaseItem.RowItem rowItem : item.getRowItemList()) {
                if (rowItem.getEditLayout() != null) {

                    if (viewItems.size() == 1) {
                        rowItem.getEditLayout().setIsOnePlayer(true);
                    } else {
                        rowItem.getEditLayout().setIsOnePlayer(false);

                    }

                }
                if (rowItem.getTitleItem().getItemCheckBox().getChecked()) {
                    btnConfirm.setBackgroundResource(R.drawable.bg_green_btn);
                    checkedProductList.add(rowItem);
                }
            }
        }
        changeFooterText();


        if (LocationListFragment.class.getName().equals(fromPage)) {

            if (Constants.STR_FLAG_NO.equals(sameDayFlag)) {
                for (ShoppingPurchaseItem item : viewItems) {
                    item.hideGotoShop();
                }
            }
        }
    }

    private JsonShoppingPaymentGet.PackageData copyPackageData(JsonShoppingPaymentGet.PackageData packageData) {
        JsonShoppingPaymentGet.PackageData resPackageData = new JsonShoppingPaymentGet.PackageData();
        resPackageData.setId(packageData.getId());
        resPackageData.setPackageId(packageData.getPackageId());
        resPackageData.setPackageNumber(packageData.getPackageNumber());
        resPackageData.setPackagePrice(packageData.getPackagePrice());
        resPackageData.setPayStatus(packageData.getPayStatus());
        resPackageData.setPackageName(packageData.getPackageName());
        ArrayList<JsonShoppingPaymentGet.ProData> resPackageList = new ArrayList<>();
        for (JsonShoppingPaymentGet.ProData proData : packageData.getPackageList()) {
            resPackageList.add(copyProData(proData));
        }
        resPackageData.setPackageList(resPackageList);
        return resPackageData;
    }

    private JsonShoppingPaymentGet.ProData copyProData(JsonShoppingPaymentGet.ProData proData) {
        JsonShoppingPaymentGet.ProData resProData = new JsonShoppingPaymentGet.ProData();

        resProData.setId(proData.getId());
        resProData.setProductId(proData.getProductId());
        resProData.setPlayer(proData.getPlayer());
        resProData.setProductName(proData.getProductName());
        resProData.setPromoteId(proData.getPromoteId());
        resProData.setPayStatus(proData.getPayStatus());
        resProData.setPdSort(proData.getPdSort());
        //resProData.setPackageId(proData.getPackageId());
        resProData.setPrice(proData.getPrice());
        resProData.setQty(proData.getQty());
        // resProData.setPosition(proData.getPosition());
        resProData.setDisCountPrice(proData.getDisCountPrice());
        resProData.setAttriName(proData.getAttriName());
        return resProData;
    }


    private boolean maybeAa() {
        if (viewItems.size() < 2) {
            return false;
        }
        if (checkedProductList.size() <= 0) {
            return false;
        }
        for (ShoppingPurchaseItem.RowItem item : checkedProductList) {
            int itemType = item.getItemType();
            if (itemType == ShoppingPurchaseItem.RowItem.ROW_ITEM_TYPE_FIFTY
                    || itemType == ShoppingPurchaseItem.RowItem.ROW_ITEM_TYPE_PACKAGE || itemType == ShoppingPurchaseItem.RowItem.ROW_ITEM_TYPE_PRICING) {
                return false;
            }
        }
        return true;
    }


    private boolean maybeUndo() {

        if (checkedProductList.size() != 1) {
            return false;
        }
        for (ShoppingPurchaseItem.RowItem item : checkedProductList) {
            int itemType = item.getItemType();
            if (itemType != ShoppingPurchaseItem.RowItem.ROW_ITEM_TYPE_FIFTY) {
                return false;
            }
        }

        return true;
    }

    private void paidItems() {
        for (ShoppingPurchaseItem.RowItem rowItem : checkedProductList) {
            if (rowItem.isPricing()) {

                rowItem.close(rowItem.getPricingRows());
            } else {
                if (rowItem.getItemType() == ShoppingPurchaseItem.RowItem.ROW_ITEM_TYPE_PRO) {
                    rowItem.close(1);

                }
                if (rowItem.getItemType() == ShoppingPurchaseItem.RowItem.ROW_ITEM_TYPE_FIFTY) {
                    rowItem.close(rowItem.getFiftyData().getAaList().size() + 1);

                }
                if (rowItem.getItemType() == ShoppingPurchaseItem.RowItem.ROW_ITEM_TYPE_PACKAGE) {
                    rowItem.close(rowItem.getPackageData().getPackageList().size() + 1);

                }

            }


            if (rowItem.getEditLayout() != null)
                rowItem.getEditLayout().setVisibility(View.GONE);
            rowItem.getTitleItem().getItemCheckBox().setChecked(false);
            ShoppingPurchaseItem item = findViewItemWithBookingNo(rowItem.getBookingNo());
            if (item == null) {
                item = viewItems.get(0);
            }
            item.getRowItemList().remove(rowItem);
        }
        checkedProductList.clear();
    }

    private void undoAa() {
        for (ShoppingPurchaseItem.RowItem checkedRowItem : checkedProductList) {
            JsonShoppingPaymentGet.FiftyData fiftyData = checkedRowItem.getFiftyData();
            String aaCode = fiftyData.getAACode();
            for (ShoppingPurchaseItem playerItem : viewItems) {
                ArrayList<JsonShoppingPaymentGet.ProData> addRowList = new ArrayList<>();
                ArrayList<ShoppingPurchaseItem.RowItem> removeRowList = new ArrayList<>();
                for (ShoppingPurchaseItem.RowItem rowItem : playerItem.getRowItemList()) {
                    if (rowItem.getFiftyData() != null && rowItem.getFiftyData().getAACode().equals(aaCode)) {
                        for (JsonShoppingPaymentGet.ProData proData : rowItem.getFiftyData().getAaList()) {
                            if (proData.getOwnerStatus().equals(Constants.STR_1)) {
                                addRowList.add(proData);
                            }
                        }
                        removeRowList.add(rowItem);
                    }
                }
                playerItem.addSingleItemList(addRowList);
                playerItem.removeRowItem(removeRowList);
            }
        }
    }

    private JsonShoppingPaymentGet.DataItem getAddDataNotBookingNo(String playerName, String bookingNo) {
        ShoppingPurchaseItem viewItem = viewItems.get(0);
        JsonShoppingPaymentGet.DataItem playerData = new JsonShoppingPaymentGet.DataItem();
        ArrayList<JsonShoppingPaymentGet.ProData> proDataList = new ArrayList<>();
        ArrayList<JsonShoppingPaymentGet.PackageData> packageDataList = new ArrayList<>();
        for (ShoppingPurchaseItem.RowItem item : viewItem.getRowItemList()) {
            if (item.getItemType() == ShoppingPurchaseItem.RowItem.ROW_ITEM_TYPE_PACKAGE) {
                JsonShoppingPaymentGet.PackageData packageData = new JsonShoppingPaymentGet.PackageData();
                JsonShoppingPaymentGet.PackageData viewPackageData = item.getPackageData();
                packageData.setPackageId(viewPackageData.getPackageId());
                packageData.setPackageName(viewPackageData.getPackageName());
                packageData.setPackagePrice(viewPackageData.getPackagePrice());
                packageData.setPayStatus(0);
                packageData.setPackageNumber(viewPackageData.getPackageNumber());
                ArrayList<JsonShoppingPaymentGet.ProData> packageList = new ArrayList<>();
                for (JsonShoppingPaymentGet.ProData subProduct : viewPackageData.getPackageList()) {
                    JsonShoppingPaymentGet.ProData proData = new JsonShoppingPaymentGet.ProData();
                    proData.setPayStatus(0);
                    proData.setAttriId(subProduct.getAttriId());
                    proData.setAttriName(subProduct.getAttriName());
                    proData.setDisCountPrice(subProduct.getDisCountPrice());
                    proData.setPrice(subProduct.getPrice());
                    proData.setQty(subProduct.getQty());
                    proData.setProductName(subProduct.getProductName());
                    proData.setProductId(subProduct.getProductId());
                    packageList.add(proData);
                }

                packageData.setPackageList(packageList);
                packageDataList.add(packageData);
                playerData.setPackageList(packageDataList);
                playerData.setBookingNo(bookingNo);
                playerData.setPlayerName(playerName);
            }

            if (item.getItemType() == ShoppingPurchaseItem.RowItem.ROW_ITEM_TYPE_PRO) {
                JsonShoppingPaymentGet.ProData proData = new JsonShoppingPaymentGet.ProData();
                JsonShoppingPaymentGet.ProData viewProData = item.getProData();

                proData.setPayStatus(0);
                proData.setAttriId(viewProData.getAttriId());
                proData.setAttriName(viewProData.getAttriName());
                proData.setDisCountPrice(viewProData.getDisCountPrice());
                proData.setPrice(viewProData.getPrice());
                proData.setQty(viewProData.getQty());
                proData.setProductName(viewProData.getProductName());
                proData.setProductId(viewProData.getProductId());
                proData.setPromoteId(viewProData.getPromoteId());
                proDataList.add(proData);
                playerData.setProDataList(proDataList);
                playerData.setBookingNo(bookingNo);
                playerData.setPlayerName(playerName);

            }


        }

        return playerData;

    }

    // first
    private void addProductToPlayer(String bookingNo, String playerName) {
        JsonShoppingPaymentGet.DataItem playerData = getAddDataNotBookingNo(playerName, bookingNo);
        llBody.removeView(viewItems.get(0));
        viewItems.remove(viewItems.get(0));
        refreshBody();
        ArrayList<JsonShoppingPaymentGet.ProData> upProduct = new ArrayList<>();
        purchasePost(playerData, StringUtils.EMPTY, upProduct, Constants.STR_1);

    }

    // add product to one player
    private void addProductToPlayer(String bookingNo, ArrayList<JsonShoppingPaymentGet.DataItem> addDataList,
                                    String delStr, ArrayList<JsonShoppingPaymentGet.ProData> upProductList, ArrayList<ShoppingPurchaseItem.RowItem> delList) {
        ShoppingPurchaseItem addItem;
        if (bookingNo == null) {
            addItem = viewItems.get(0);
        } else {
            addItem = findViewItemWithBookingNo(bookingNo);
        }


        for (JsonShoppingPaymentGet.DataItem addData : addDataList) {
            if (addData.getProDataList() != null) {
                for (JsonShoppingPaymentGet.ProData proData : addData.getProDataList()) {
                    if (addItem != null) {
                        addItem.addSingleItem(proData);
                    }
                }
            }
            if (addData.getPackageList() != null) {
                if (addItem != null) {
                    addItem.addMultiItemWithPlayer(addData.getPackageList(), null);
                }
            }
        }


        for (JsonShoppingPaymentGet.ProData proData : upProductList) {
            if (addItem != null) {
                addItem.changeItemNum(proData.getId(), proData.getQty());
            }
        }
        String[] delStrs = delStr.split(Constants.STR_COMMA);


        if (bookingNo != null) {
            for (String delId : delStrs) {
                if (addItem != null) {
                    addItem.delRowItemOfId(delId);
                }
            }
        } else {

            if (delList != null && addItem != null) {
                for (ShoppingPurchaseItem.RowItem delRow : delList) {
                    addItem.removeProductItem(delRow);
                }
            }
        }

//        if (isAll()) {
//
//            cbAll.setChecked(true);
//        } else {
//
//            cbAll.setChecked(false);
//        }


        if (cbAll.getChecked()) {
            for (ShoppingPurchaseItem item : viewItems) {
                for (ShoppingPurchaseItem.RowItem rowItem : item.getRowItemList()) {
                    rowItem.getTitleItem().getItemCheckBox().setChecked(true);
                }
            }
        }
        setCheckedProductList();

    }


    private void addAaDataToReturnData(ArrayList<ShoppingPurchaseItem.RowItem> checkedProductList,
                                       JsonShoppingPaymentGet.FiftyData fiftyData, String bookingNo) {

        for (ShoppingPurchaseItem.RowItem rowItem : checkedProductList) {
            if (rowItem.getBookingNo().equals(bookingNo)) {
                rowItem.getProData().setOwnerStatus(Constants.STR_1);
                fiftyData.getAaList().add(rowItem.getProData());
            }
        }
    }

//    private boolean productIsExistOfFiftyData(JsonShoppingPaymentGet.ProData proData,
//                                              JsonShoppingPaymentGet.FiftyData fiftyData) {
//
//        for (JsonShoppingPaymentGet.ProData aaProData : fiftyData.getAaList()) {
//            if (proData.getAttriId().equals(aaProData.getAttriId())
//                    && proData.getProductId().equals(aaProData.getProductId())) {
//                return true;
//            }
//        }
//        return false;
//    }


    private void addAaData(ArrayList<JsonShoppingPaymentGet.DataItem> returnAaList,
                           ArrayList<ShoppingPurchaseItem.RowItem> checkedProductList,
                           double sameMoney, String sameWithString) {

        JsonShoppingPaymentGet.FiftyData oneFiftyData = returnAaList.get(0).getFiftyDataList().get(0);
        String aaCode = oneFiftyData.getAACode();

        for (JsonShoppingPaymentGet.DataItem addData : returnAaList) {
            ShoppingPurchaseItem itemView = findViewItemWithBookingNo(addData.getBookingNo());
            ArrayList<JsonShoppingPaymentGet.FiftyData> fiftyDataList = addData.getFiftyDataList();
            for (JsonShoppingPaymentGet.FiftyData fiftyData : fiftyDataList) {
                addAaDataToReturnData(checkedProductList, fiftyData, addData.getBookingNo());
                if (itemView != null) {
                    itemView.addMultiItemWithPlayer(null, addData.getFiftyDataList());
                }
            }
        }

        String checkedBookingNo = checkedProductList.get(0).getBookingNo();

        boolean isSamePlayer = true;
        ArrayList<JsonShoppingPaymentGet.ProData> proDataList = new ArrayList<>();
        //remove checkedProduct
        for (ShoppingPurchaseItem.RowItem checkedProduct : checkedProductList) {
            ShoppingPurchaseItem itemView = findViewItemWithBookingNo(checkedProduct.getBookingNo());
            if (itemView != null) {
                itemView.removeProductItem(checkedProduct);
            }
            if (!checkedBookingNo.equals(checkedProduct.getBookingNo())) {
                isSamePlayer = false;
            }
            proDataList.add(checkedProduct.getProData());
        }


        if (isSamePlayer) {
            ShoppingPurchaseItem itemView = findViewItemWithBookingNo(checkedBookingNo);
            ArrayList<JsonShoppingPaymentGet.FiftyData> fiftyDataList = new ArrayList<>();
            JsonShoppingPaymentGet.FiftyData fiftyData = new JsonShoppingPaymentGet.FiftyData();
            if (itemView != null) {
                fiftyData.setPlayer(itemView.getPlayerName());
            }
            fiftyData.setAACode(aaCode);

            for (JsonShoppingPaymentGet.ProData proData : proDataList) {
                proData.setOwnerStatus(Constants.STR_1);

            }
            fiftyData.setAaList(proDataList);
            fiftyData.setAaTotalPrice(sameMoney);
            fiftyData.setAAWith(sameWithString);

            fiftyData.setPayStatus(0);
            fiftyDataList.add(fiftyData);
            if (itemView != null) {
                itemView.addMultiItemWithPlayer(null, fiftyDataList);
            }
        }

    }

    private void splitSuccessRefreshView(ShoppingPurchaseItem.RowItem rowItem, ShoppingPurchaseItem toPlayer,
                                         int productType, int num, String newId, double newPrice) {
        if (productType == 1) {//package
            rowItem.minusPackageNum(num);
            JsonShoppingPaymentGet.PackageData copyPackageData = copyPackageData(rowItem.getPackageData());
            copyPackageData.setPackagePrice(newPrice);
            copyPackageData.setPackageNumber(num);
            copyPackageData.setId(Integer.parseInt(newId));
            toPlayer.addMuladdMultiItemtiItem(copyPackageData, null);

        } else {
            rowItem.minusNum(num);
            JsonShoppingPaymentGet.ProData copyProData = copyProData(rowItem.getProData());
            copyProData.setQty(num);
            copyProData.setDisCountPrice(newPrice);
            copyProData.setId(Integer.parseInt(newId));
            toPlayer.addSingleItem(copyProData);
        }

    }

//    private void addRefreshBody(List<JsonShoppingPaymentGet.DataItem> addDataList) {
//
//        for (JsonShoppingPaymentGet.DataItem playerData : addDataList) {
//            ShoppingPurchaseItem item = new ShoppingPurchaseItem(ShoppingPaymentFragment.this, purchaseItemListener, playerData, purchaseFlag);
//
//            item.getPlayerRadioBtn().setClickView(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    for (ShoppingPurchaseItem item : viewItems) {
//                        item.getPlayerRadioBtn().setChecked(false);
//                    }
//                    IteeCheckBox checkBox = (IteeCheckBox) view;
//                    checkBox.setChecked(true);
//                    purchaseBookingNo = checkBox.getRowItem().getBookingNo();
//
//                }
//            });
//            viewItems.add(item);
//            llBody.addView(item);
//        }
//    }

    private void refreshBody() {
        for (JsonShoppingPaymentGet.DataItem playerData : dataList) {
            ShoppingPurchaseItem item = new ShoppingPurchaseItem(ShoppingPaymentFragment.this, purchaseItemListener, playerData, purchaseFlag);
            ShoppingPurchaseItem itemOld = findViewItemWithBookingNo(playerData.getBookingNo());
            if (itemOld != null) {
                llBody.removeView(itemOld);
                viewItems.remove(itemOld);
            }

            item.getPlayerRadioBtn().setClickView(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for (ShoppingPurchaseItem item : viewItems) {
                        item.getPlayerRadioBtn().setChecked(false);
                    }

                    IteeCheckBox checkBox = (IteeCheckBox) view;
                    checkBox.setChecked(true);
                    purchaseBookingNo = checkBox.getBookingNo();


                }
            });
            viewItems.add(item);
            llBody.addView(item);

        }

        if (viewItems.size() > 0) {
            viewItems.get(0).getPlayerRadioBtn().setChecked(true);

            if (purchaseFlag == PURCHASE_FLAG_CHECKOUT && isFirstInit) {
                for (ShoppingPurchaseItem.RowItem rowItem : viewItems.get(0).getRowItemList()) {
                    rowItem.getTitleItem().getItemCheckBox().setChecked(true);
                }
                if (isAll()) {

                    cbAll.setChecked(true);
                }


            }
            purchaseBookingNo = viewItems.get(0).getBookingNo();

        }
        if (cbAll.getChecked()) {
            for (ShoppingPurchaseItem item : viewItems) {
                for (ShoppingPurchaseItem.RowItem rowItem : item.getRowItemList()) {
                    rowItem.getTitleItem().getItemCheckBox().setChecked(true);
                }
            }
        }
        setCheckedProductList();

        isFirstInit = false;
    }


    // API
    private void getPurchaseDataOfBookingNo(final String bookingNo, final boolean first) {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPPING_BOOKING_NO, bookingNo);
        if (isDeposit) {
            params.put(ApiKey.SHOPPING_CHECK_DEPOSIT_FLAG, Constants.STR_1);

        } else {
            params.put(ApiKey.SHOPPING_CHECK_DEPOSIT_FLAG, Constants.STR_0);
        }
        HttpManager<JsonShoppingPaymentGet> hh = new HttpManager<JsonShoppingPaymentGet>(ShoppingPaymentFragment.this) {

            @Override
            public void onJsonSuccess(JsonShoppingPaymentGet jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    dataList = jo.getDataList();
                    if (viewItems.size() > 0 && viewItems.get(0).getBookingNo() == null) {
                        if (dataList != null && dataList.size() > 0) {
                            addProductToPlayer(dataList.get(0).getBookingNo(), dataList.get(0).getPlayerName());
                        }

                    } else {
                        refreshBody();

                        if (chooseProductList != null && chooseProductList.size() > 0) {

                            JsonShoppingPaymentGet.DataItem playerData = new JsonShoppingPaymentGet.DataItem();
                            ArrayList<JsonShoppingPaymentGet.ProData> proDataList = new ArrayList<>();
                            ArrayList<JsonShoppingPaymentGet.PackageData> packageDataList = new ArrayList<>();
                            for (String product : chooseProductList) {

                                ShoppingProduct shoppingProduct = (ShoppingProduct) Utils.getObjectFromString(product);
                                if (shoppingProduct.getPackageId() == null || shoppingProduct.getPackageId().equals(Constants.STR_0)) {
                                    JsonShoppingPaymentGet.ProData proData = new JsonShoppingPaymentGet.ProData();
                                    proData.setPayStatus(0);
                                    proData.setAttriId(shoppingProduct.getProductAttribute());
                                    proData.setAttriName(shoppingProduct.getAttributeName());
                                    proData.setDisCountPrice(Double.parseDouble(shoppingProduct.getProductPrice()));
                                    proData.setPrice(Double.parseDouble(shoppingProduct.getProductPrice()));
                                    proData.setQty(shoppingProduct.getProductNumber());
                                    proData.setProductName(shoppingProduct.getProductName());
                                    proData.setProductId(shoppingProduct.getProductId());
                                    proData.setPromoteId(shoppingProduct.getPromoteId());


                                    proDataList.add(proData);
                                    playerData.setProDataList(proDataList);
                                    if (first) {
                                        playerData.setBookingNo(dataList.get(0).getBookingNo());
                                    } else {
                                        playerData.setBookingNo(bookingNo);
                                    }

                                    playerData.setPlayerName(jo.getDataList().get(0).getPlayerName());

                                } else {


                                    JsonShoppingPaymentGet.PackageData packageData = new JsonShoppingPaymentGet.PackageData();
                                    packageData.setPackageId(shoppingProduct.getPackageId());
                                    packageData.setPackageName(shoppingProduct.getProductName());
                                    packageData.setPackagePrice(Double.parseDouble(shoppingProduct.getProductPrice()));
                                    packageData.setPayStatus(0);
                                    packageData.setPackageNumber(shoppingProduct.getProductNumber());
                                    ArrayList<JsonShoppingPaymentGet.ProData> packageList = new ArrayList<>();
                                    for (ShoppingProduct.ShoppingSubProduct subProduct : shoppingProduct.getProductList()) {
                                        JsonShoppingPaymentGet.ProData proData = new JsonShoppingPaymentGet.ProData();
                                        proData.setPayStatus(0);
                                        proData.setAttriId(subProduct.getProductAttrId());
                                        proData.setAttriName(subProduct.getProductAttr());
                                        proData.setDisCountPrice(Double.parseDouble(subProduct.getPrice()));
                                        proData.setPrice(Double.parseDouble(subProduct.getPrice()));
                                        proData.setQty(Integer.parseInt(subProduct.getNumber()));
                                        proData.setProductName(subProduct.getProductName());
                                        proData.setProductId(subProduct.getProductId());
                                        packageList.add(proData);
                                    }

                                    packageData.setPackageList(packageList);
                                    packageDataList.add(packageData);
                                    playerData.setPackageList(packageDataList);
                                    if (first) {
                                        playerData.setBookingNo(dataList.get(0).getBookingNo());
                                    } else {
                                        playerData.setBookingNo(bookingNo);
                                    }
                                    playerData.setPlayerName(jo.getDataList().get(0).getPlayerName());


                                }
                            }
                            ArrayList<JsonShoppingPaymentGet.ProData> upProduct = new ArrayList<>();
                            purchasePost(playerData, StringUtils.EMPTY, upProduct, Constants.STR_1);
                            chooseProductList.clear();
                            chooseProductList = null;
                        }


                    }

                    if (!Constants.STR_EMPTY.equals(msg) && isDeposit) {

                        Utils.showShortToast(getActivity(), msg + Constants.STR_SPACE + getString(R.string.shopping_pay_deposit_insufficient));
                        isDeposit = false;
                    }

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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.ShoppingPurchaseGet, params);
    }


//    private boolean isChooseProduct(ShoppingPurchaseItem viewItem) {
//
//
//        for (ShoppingPurchaseItem.RowItem rowItem : viewItem.getRowItemList()) {
//
//            if (rowItem.getTitleItem().getItemCheckBox().getChecked()) {
//                return true;
//            }
//        }
//        return false;
//    }


    private String getDetailBookingNo() {
        String customId = Constants.STR_EMPTY;
        for (ShoppingPurchaseItem viewItem : viewItems) {
            if (viewItem.getPlayerRadioBtn().getChecked()) {

                return viewItem.getBookingNo();
            }
        }

        return customId;
    }

    private String getCheckPayProductList() {

        JSONArray resArray = new JSONArray();

        try {
            for (ShoppingPurchaseItem viewItem : viewItems) {

                // if (isChooseProduct(viewItem)) {

                JSONObject jsItem = new JSONObject();

                if (Utils.isStringNotNullOrEmpty(viewItem.getBookingNo())) {
                    jsItem.put(ApiKey.SHOPPING_PURCHASE_BOOKING_NO, viewItem.getBookingNo());
                }


                if (viewItem.getPlayerRadioBtn().getChecked()) {
                    jsItem.put(ApiKey.SHOPPING_PURCHASE_PAY_STATUS, Constants.SHOPS_BOOKING_PAY_STATUS_1);
                } else {
                    jsItem.put(ApiKey.SHOPPING_PURCHASE_PAY_STATUS, Constants.SHOPS_BOOKING_PAY_STATUS_0);
                }

                JSONArray jsArrayPackage = new JSONArray();
                JSONArray jsArrayProduct = new JSONArray();

                // JSONArray jsArrayAa = new JSONArray();

                double maxPromoteMoney = 0;
                double maxfiftyMoney = 0;
                for (ShoppingPurchaseItem.RowItem rowItem : viewItem.getRowItemList()) {

                    if (rowItem.getTitleItem().getItemCheckBox().getChecked()) {
                        if (rowItem.isPricing()) {

                            for (JsonShoppingPaymentGet.PricingData pricingData : rowItem.getmPlayerData().getPricingDataList()) {


                                if (!Constants.STR_0.equals(pricingData.getPackageId())) {
                                    JSONObject jsPackageObject = new JSONObject();
                                    jsPackageObject.put(ApiKey.SHOPPING_PURCHASE_ID, pricingData.getId());
                                    jsPackageObject.put(ApiKey.SHOPPING_PURCHASE_PACKAGE_ID, pricingData.getPackageId());
                                    jsPackageObject.put(ApiKey.SHOPPING_PURCHASE_NUMBER, Constants.STR_1);
                                    jsPackageObject.put(ApiKey.SHOPPING_PURCHASE_DISCOUNT_PRICE, pricingData.getDiscountPrice());

                                    jsArrayPackage.put(jsPackageObject);
                                } else {

                                    JSONObject jsProductObject = new JSONObject();
                                    jsProductObject.put(ApiKey.SHOPPING_PURCHASE_ID, pricingData.getId());
                                    jsProductObject.put(ApiKey.SHOPPING_PURCHASE_PRODUCT_ID, pricingData.getProductId());
                                    jsProductObject.put(ApiKey.SHOPPING_PURCHASE_PRA_ID, pricingData.getAttrId());
                                    jsProductObject.put(ApiKey.SHOPPING_PURCHASE_NUMBER, Constants.STR_1);
                                    jsProductObject.put(ApiKey.SHOPPING_PURCHASE_DISCOUNT_PRICE, pricingData.getDiscountPrice());
                                    jsProductObject.put(ApiKey.SHOPPING_PURCHASE_PROMOTE_ID, Constants.STR_EMPTY);

                                    jsArrayProduct.put(jsProductObject);
                                }

                            }

                        } else {

                            if (rowItem.getItemType() == ShoppingPurchaseItem.RowItem.ROW_ITEM_TYPE_PACKAGE) {
                                JSONObject jsPackageObject = new JSONObject();
                                jsPackageObject.put(ApiKey.SHOPPING_PURCHASE_ID, String.valueOf(rowItem.getPackageData().getId()));
                                jsPackageObject.put(ApiKey.SHOPPING_PURCHASE_PACKAGE_ID, rowItem.getPackageData().getPackageId());
                                jsPackageObject.put(ApiKey.SHOPPING_PURCHASE_NUMBER, rowItem.getPackageData().getPackageNumber());
                                jsPackageObject.put(ApiKey.SHOPPING_PURCHASE_DISCOUNT_PRICE, String.valueOf(rowItem.getPackageData().getPackagePrice()));
                                jsArrayPackage.put(jsPackageObject);
                            }
                            if (rowItem.getItemType() == ShoppingPurchaseItem.RowItem.ROW_ITEM_TYPE_FIFTY) {

                                maxfiftyMoney += rowItem.getFiftyData().getAaTotalPrice();


                            }

                            if (Utils.isStringNullOrEmpty(viewItems.get(0).getBookingNo())) {

                                if (rowItem.getItemType() == ShoppingPurchaseItem.RowItem.ROW_ITEM_TYPE_PRO) {
                                    JSONObject jsProductObject = new JSONObject();
                                    jsProductObject.put(ApiKey.SHOPPING_PURCHASE_ID, String.valueOf(rowItem.getProData().getId()));

                                    jsProductObject.put(ApiKey.SHOPPING_PURCHASE_PRODUCT_ID, rowItem.getProData().getProductId());
                                    jsProductObject.put(ApiKey.SHOPPING_PURCHASE_PRA_ID, rowItem.getProData().getAttriId());
                                    jsProductObject.put(ApiKey.SHOPPING_PURCHASE_NUMBER, rowItem.getProData().getQty());
                                    jsProductObject.put(ApiKey.SHOPPING_PURCHASE_DISCOUNT_PRICE, String.valueOf(rowItem.getProData().getDisCountPrice()));
                                    jsProductObject.put(ApiKey.SHOPPING_PURCHASE_PROMOTE_ID, rowItem.getProData().getPromoteId());
                                    jsArrayProduct.put(jsProductObject);

                                }
                            } else {

                                if (rowItem.getItemType() == ShoppingPurchaseItem.RowItem.ROW_ITEM_TYPE_PRO) {
                                    if (Constants.STR_0.equals(rowItem.getProData().getPromoteId()) || Utils.isStringNullOrEmpty(rowItem.getProData().getPromoteId())) {
                                        JSONObject jsProductObject = new JSONObject();
                                        jsProductObject.put(ApiKey.SHOPPING_PURCHASE_ID, String.valueOf(rowItem.getProData().getId()));
                                        jsProductObject.put(ApiKey.SHOPPING_PURCHASE_PRODUCT_ID, rowItem.getProData().getProductId());
                                        jsProductObject.put(ApiKey.SHOPPING_PURCHASE_PRA_ID, rowItem.getProData().getAttriId());
                                        jsProductObject.put(ApiKey.SHOPPING_PURCHASE_NUMBER, rowItem.getProData().getQty());
                                        jsProductObject.put(ApiKey.SHOPPING_PURCHASE_DISCOUNT_PRICE, String.valueOf(rowItem.getProData().getDisCountPrice()));

                                        jsProductObject.put(ApiKey.SHOPPING_PURCHASE_PROMOTE_ID, rowItem.getProData().getPromoteId());

                                        jsArrayProduct.put(jsProductObject);
                                    } else {

                                        maxPromoteMoney += rowItem.getProData().getDisCountPrice();
                                    }


                                }

                            }
                        }


                    }
                }

                jsItem.put(ApiKey.SHOPPING_PURCHASE_PRODUCT_LIST, jsArrayProduct);
                jsItem.put(ApiKey.SHOPPING_PURCHASE_PACKAGE_LIST, jsArrayPackage);

                JSONObject jsPromoteObject = new JSONObject();
                jsPromoteObject.put(ApiKey.SHOPPING_PURCHASE_PRICE, String.valueOf(maxPromoteMoney));
                jsItem.put(ApiKey.SHOPPING_PROMOTE_LIST, jsPromoteObject);


                JSONObject jsAaObject = new JSONObject();
                jsAaObject.put(ApiKey.SHOPPING_PURCHASE_PRICE, String.valueOf(maxfiftyMoney));

                jsItem.put(ApiKey.SHOPPING_PURCHASE_AA_LIST, jsAaObject);


                resArray.put(jsItem);
                //}


            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

        return resArray.toString();
    }


    public void purchasePut(final int num, int id, final ShoppingPurchaseItem.RowItem rowItem) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPPING_PURCHASE_NUM, String.valueOf(num));
        params.put(ApiKey.SHOPPING_PURCHASE_ID, String.valueOf(id));
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShoppingPaymentFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                Utils.showShortToast(getActivity(), msg);
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY
                        || returnCode == Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY) {
                    // getBaseActivity().doBack();
                    if (rowItem.getItemType() == ShoppingPurchaseItem.RowItem.ROW_ITEM_TYPE_PACKAGE) {
                        rowItem.getPackageData().setPackageNumber(num);
                    } else {
                        rowItem.getProData().setQty(num);
                    }
                    rowItem.getTitleItem().getTvNum().setText(Constants.STR_MULTIPLY + num);
                    setCheckedProductList();
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

        hh.startPut(getActivity(), ApiManager.HttpApi.ShoppingPurchasePut, params);

    }

    // purchaseSplit(PUT)
    //   productType = 1 package
    public void purchaseSplitPut(final ShoppingPurchaseItem.RowItem fromItem, final ShoppingPurchaseItem toPlayer, final int productType, final int toNum) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        String id;
        if (productType == 1) {
            id = String.valueOf(fromItem.getPackageData().getId());
            params.put(ApiKey.SHOPPING_PURCHASE_PACKAGE_ID, fromItem.getPackageData().getPackageId());
            params.put(ApiKey.SHOPPING_PURCHASE_FROM_GOODS_NUM, String.valueOf(fromItem.getPackageData().getPackageNumber() - toNum));
            params.put(ApiKey.SHOPPING_PURCHASE_PRODUCT_ID, Constants.STR_0);
            params.put(ApiKey.SHOPPING_PURCHASE_PRA_ID, Constants.STR_EMPTY);
            params.put(ApiKey.SHOPPING_PURCHASE_PRICE, String.valueOf(fromItem.getPackageData().getPackagePrice()));
        } else {
            id = String.valueOf(fromItem.getProData().getId());
            params.put(ApiKey.SHOPPING_PURCHASE_PRODUCT_ID, fromItem.getProData().getProductId());
            params.put(ApiKey.SHOPPING_PURCHASE_PACKAGE_ID, Constants.STR_0);
            params.put(ApiKey.SHOPPING_PURCHASE_FROM_GOODS_NUM, String.valueOf(fromItem.getProData().getQty() - toNum));
            params.put(ApiKey.SHOPPING_PURCHASE_PRA_ID, fromItem.getProData().getAttriId());
            params.put(ApiKey.SHOPPING_PURCHASE_PRICE, String.valueOf(fromItem.getProData().getDisCountPrice()));
        }
        params.put(ApiKey.SHOPPING_PURCHASE_ID, id);
        params.put(ApiKey.SHOPPING_PURCHASE_TO_PLAYER, toPlayer.getPlayerName());
        params.put(ApiKey.SHOPPING_PURCHASE_TO_BOOKING_NO, toPlayer.getBookingNo());
        params.put(ApiKey.SHOPPING_PURCHASE_TO_GOODS_NUM, String.valueOf(toNum));
        params.put(ApiKey.SHOPPING_PURCHASE_FROM_PLAYER, fromItem.getPlayerName());
        params.put(ApiKey.SHOPPING_PURCHASE_FROM_BOOKING_NO, fromItem.getBookingNo());

        HttpManager<JsonShoppingReturnData> hh = new HttpManager<JsonShoppingReturnData>(ShoppingPaymentFragment.this) {

            @Override
            public void onJsonSuccess(JsonShoppingReturnData jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                Utils.showShortToast(getActivity(), msg);
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY
                        || returnCode == Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY) {
                    splitSuccessRefreshView(fromItem, toPlayer, productType, toNum, jo.getId(), jo.getPrice());
                    setCheckedProductList();
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

        hh.startPut(getActivity(), ApiManager.HttpApi.ShoppingPurchaseSplitPut, params);


    }


    //purchaseAA[PUT方式]

    private void purchaseAaPut() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        ArrayList<String> aaCodeList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (ShoppingPurchaseItem.RowItem rowItem : checkedProductList) {
            if (!aaCodeList.contains(rowItem.getFiftyData().getAACode())) {
                aaCodeList.add(rowItem.getFiftyData().getAACode());
                sb.append(rowItem.getFiftyData().getAACode());
                sb.append(Constants.STR_COMMA);
            }
        }

        String codes = sb.toString();
        if (codes.length() > 0) {
            codes = codes.substring(0, codes.length() - 1);
        }

        params.put(ApiKey.SHOPPING_PURCHASE_AA_LIST, codes);
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShoppingPaymentFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                Utils.showShortToast(getActivity(), msg);

                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY
                        || returnCode == Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY) {
                    undoAa();
                    setCheckedProductList();
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

        hh.startPut(getActivity(), ApiManager.HttpApi.PurchaseAaPut, params);
    }

    //：purchase[POST add

    private String getProductListJsonString(JsonShoppingPaymentGet.DataItem notPlayerData, String delStr, ArrayList<JsonShoppingPaymentGet.ProData> upProductList) {

        JSONObject jsonObject = new JSONObject();

        JSONArray array = new JSONArray();
        if (notPlayerData.getProDataList() != null) {
            for (JsonShoppingPaymentGet.ProData proData : notPlayerData.getProDataList()) {
                Map<String, String> map = new HashMap<>();
                map.put(ApiKey.SHOPPING_PURCHASE_PRODUCT_ID, proData.getProductId());
                map.put(ApiKey.SHOPPING_PURCHASE_PRA_ID, proData.getAttriId());
                map.put(ApiKey.SHOPPING_PURCHASE_PROMOTE_ID, proData.getPromoteId());
                map.put(ApiKey.SHOPPING_PURCHASE_NUMBER, String.valueOf(proData.getQty()));
                map.put(ApiKey.SHOPPING_PURCHASE_PRICE, String.valueOf(proData.getPrice()));
                map.put(ApiKey.SHOPPING_PURCHASE_DISCOUNT_PRICE, String.valueOf(proData.getDisCountPrice()));
                array.put(new JSONObject(map));
            }

        }
        try {
            jsonObject.put(ApiKey.SHOPPING_PURCHASE_PRODUCT_LIST, array);
            jsonObject.put(ApiKey.SHOPPING_PURCHASE_PACKAGE_LIST, getPackageListJsonString(notPlayerData));

            jsonObject.put(ApiKey.SHOPPING_PURCHASE_DEL_ID, delStr);

            JSONArray upArray = new JSONArray();
            for (JsonShoppingPaymentGet.ProData proData : upProductList) {

                JSONObject jsonProData = new JSONObject();

                jsonProData.put(ApiKey.SHOPPING_PURCHASE_ID, String.valueOf(proData.getId()));
                jsonProData.put(ApiKey.SHOPPING_PURCHASE_NUM, String.valueOf(proData.getQty()));
                upArray.put(jsonProData);


            }


            jsonObject.put(ApiKey.SHOPPING_PURCHASE_EDIT_LIST, upArray);

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
        return jsonObject.toString();
    }


    private JSONArray getPackageListJsonString(JsonShoppingPaymentGet.DataItem notPlayerData) {

        JSONArray array = new JSONArray();
        if (notPlayerData.getPackageList() != null) {
            for (JsonShoppingPaymentGet.PackageData packageData : notPlayerData.getPackageList()) {
                JSONObject jsPackage = new JSONObject();

                try {
                    jsPackage.put(ApiKey.SHOPPING_PURCHASE_PACKAGE_ID, packageData.getPackageId());

                    jsPackage.put(ApiKey.SHOPPING_PURCHASE_NUMBER, packageData.getPackageNumber());
                    jsPackage.put(ApiKey.SHOPPING_PURCHASE_PRICE, packageData.getPackagePrice());
                    jsPackage.put(ApiKey.SHOPPING_PURCHASE_DISCOUNT_PRICE, packageData.getPackagePrice());


                    JSONArray jsProductList = new JSONArray();

                    for (JsonShoppingPaymentGet.ProData proData : packageData.getPackageList()) {

                        JSONObject jsProduct = new JSONObject();
                        jsProduct.put(ApiKey.SHOPPING_PURCHASE_PRODUCT_ID, proData.getProductId());
                        jsProduct.put(ApiKey.SHOPPING_ATTRI_ID, proData.getAttriId());
                        jsProductList.put(jsProduct);
                    }

                    jsPackage.put(ApiKey.SHOPPING_PURCHASE_PRODUCT_LIST, jsProductList);
                    array.put(jsPackage);

                } catch (JSONException e) {
                    Utils.log(e.getMessage());
                }

            }


        }


        return array;
    }

    private void purchasePost(final JsonShoppingPaymentGet.DataItem notPlayerData, final String delStr, final ArrayList<JsonShoppingPaymentGet.ProData> upProductList, final String type) {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPPING_PURCHASE_BOOKING_NO, notPlayerData.getBookingNo());
        if (type.equals(Constants.STR_1)) {
            params.put(ApiKey.SHOPPING_PURCHASE_PLAYER, notPlayerData.getPlayerName());
        }
        params.put(ApiKey.SHOPPING_PURCHASE_TYPE, type);
        params.put(ApiKey.SHOPPING_PURCHASE_PRODUCT_LIST, getProductListJsonString(notPlayerData, delStr, upProductList));
        HttpManager<JsonShoppingPurchaseAddReturn> hh = new HttpManager<JsonShoppingPurchaseAddReturn>(ShoppingPaymentFragment.this) {

            @Override
            public void onJsonSuccess(JsonShoppingPurchaseAddReturn jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();

                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY || returnCode == Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY) {

                    addProductToPlayer(notPlayerData.getBookingNo(), jo.getDataList(), delStr, upProductList, null);

                    setCheckedProductList();
                    if (chooseProductList != null) {
                        chooseProductList.clear();

                    }

                    if (!Constants.STR_EMPTY.equals(msg)) {
                        Utils.showShortToast(getActivity(), msg + Constants.STR_SPACE + getString(R.string.shopping_pay_deposit_insufficient));
                    }

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

        hh.start(getActivity(), ApiManager.HttpApi.ShoppingPurchasePost, params);


    }


    public void purchaseDelete(String id, final ShoppingPurchaseItem.RowItem rowItem, final ShoppingPurchaseItem player) {

        final Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPPING_PURCHASE_ID, id);

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShoppingPaymentFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                Integer returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20403_DELETE_SUCCESSFULLY) {
                    player.removeProductItem(rowItem);


                    setCheckedProductList();
                }
                Utils.showShortToast(getActivity(), msg);
            }

            @Override
            public void onJsonError(VolleyError error) {
                Utils.showShortToast(getActivity(), String.valueOf(error));
            }
        };
        hh.startDelete(getActivity(), ApiManager.HttpApi.ShoppingPurchaseDelete, params);

    }


    public void deleteTimesApi(String bookingNo, String times, String bopId, final ShoppingPurchaseItem.RowItem rowItem, final ShoppingPurchaseItem player) {

        final Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPPING_BOOKING_NO, bookingNo);

        params.put(ApiKey.SHOPPING_PRICING_TIMES, times);
        params.put(ApiKey.SHOPPING_PRICING_BDP_ID, bopId);


        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShoppingPaymentFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                Integer returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20403_DELETE_SUCCESSFULLY) {

                    player.removeProductItem(rowItem);
                    ArrayList<JsonShoppingPaymentGet.DataItem> addDataList = new ArrayList<>();
                    JsonShoppingPaymentGet.DataItem dataItem = new JsonShoppingPaymentGet.DataItem();
                    addDataList.add(dataItem);
                    ArrayList<JsonShoppingPaymentGet.ProData> proDataList = new ArrayList<>();
                    ArrayList<JsonShoppingPaymentGet.PackageData> packageList = new ArrayList<>();
                    for (JsonShoppingPaymentGet.PricingData pricingData : rowItem.getmPlayerData().getPricingDataList()) {

                        if (!Constants.STR_0.equals(pricingData.getPackageId())) {

                            JsonShoppingPaymentGet.PackageData packageData = new JsonShoppingPaymentGet.PackageData();

                            packageData.setPackagePrice(Double.parseDouble(pricingData.getPrice()));
                            packageData.setPayStatus(0);
                            packageData.setId(Integer.parseInt(pricingData.getId()));
                            packageData.setPackageId(pricingData.getPackageId());
                            packageData.setPackageName(pricingData.getProductName());
                            packageData.setPackageNumber(Integer.parseInt(pricingData.getQty()));

                            ArrayList<JsonShoppingPaymentGet.ProData> packageProDataList = new ArrayList<>();
                            for (JsonShoppingPaymentGet.PricingData packagePricingData : pricingData.getProductList()) {

                                JsonShoppingPaymentGet.ProData proData = new JsonShoppingPaymentGet.ProData();
//                           proData.setId(Integer.parseInt(packagePricingData.getId()));
                                //proData.setProductId(packagePricingData.getProductId());
                                //proData.setAttriId(packagePricingData.getAttrId());
                                proData.setQty(Integer.parseInt(packagePricingData.getQty()));
                                // proData.setPrice(Double.parseDouble(packagePricingData.getPrice()));
                                proData.setDisCountPrice(Double.parseDouble(packagePricingData.getDiscountPrice()));
                                proData.setPayStatus(0);
                                // proData.setPromoteId(Constants.STR_0);
                                proData.setProductName(packagePricingData.getProductName());
                                //proData.setAttriName(packagePricingData.getAttriName());
                                //proData.setPdSort(packagePricingData.getPdSort());
                                // pricingData
                                packageProDataList.add(proData);


                            }

                            packageData.setPackageList(packageProDataList);
                            packageList.add(packageData);
                        } else {

                            JsonShoppingPaymentGet.ProData proData = new JsonShoppingPaymentGet.ProData();
                            proData.setId(Integer.parseInt(pricingData.getId()));
                            proData.setProductId(pricingData.getProductId());
                            proData.setAttriId(pricingData.getAttrId());
                            proData.setQty(Integer.parseInt(pricingData.getQty()));
                            proData.setPrice(Double.parseDouble(pricingData.getPrice()));
                            proData.setDisCountPrice(Double.parseDouble(pricingData.getPrice()));
                            proData.setPayStatus(0);
                            proData.setPromoteId(Constants.STR_0);
                            proData.setProductName(pricingData.getProductName());
                            proData.setAttriName(pricingData.getAttriName());
                            proData.setPdSort(pricingData.getPdSort());
                            // pricingData
                            proDataList.add(proData);

                        }

                    }
                    dataItem.setProDataList(proDataList);
                    dataItem.setPackageList(packageList);

                    ArrayList<JsonShoppingPaymentGet.ProData> upProductList = new ArrayList<>();


                    addProductToPlayer(player.getBookingNo(), addDataList, "", upProductList, null);

                    setCheckedProductList();


                }
                Utils.showShortToast(getActivity(), msg);
            }

            @Override
            public void onJsonError(VolleyError error) {
                Utils.showShortToast(getActivity(), String.valueOf(error));
            }
        };
        hh.startDelete(getActivity(), ApiManager.HttpApi.Xdeltimes, params);

    }


    public interface PurchaseItemListener {
        void clickEditLayoutOk(int editType, int num, ShoppingPurchaseItem.RowItem item);

        void clickItemCheckBox(boolean checked, ShoppingPurchaseItem.RowItem rowItem);

        void gotoShop(String bookingNo, String playerName);

        void delProductItem(String id, ShoppingPurchaseItem.RowItem rowItem, ShoppingPurchaseItem player);

        void gotoChoosePackage(String bookingNo, String playerName);


        void swipeScroll(SwipeLinearLayout layout);


        void deleteTimes(String TimesId, String times, ShoppingPurchaseItem.RowItem rowItem, ShoppingPurchaseItem player);


        void showItem(int i, ShoppingPurchaseItem.EditLayout oldE);

        void closeItem();

    }


}