/**
 * Project Name: itee
 * File Name:  SelectShoesPopupWindow.java
 * Package Name: cn.situne.itee.common.widget.wheel
 * Date:   2015-01-26
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.view.popwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.common.widget.wheel.SelectEntityWheelAdapter;
import cn.situne.itee.common.widget.wheel.SelectPropertyWheelAdapter;
import cn.situne.itee.common.widget.wheel.SelectTimeOnWheelChangedListener;
import cn.situne.itee.common.widget.wheel.SelectTimeWheelView;
import cn.situne.itee.fragment.shopping.ShoppingGoodsListItemFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonBookingGoodsList;
import cn.situne.itee.manager.jsonentity.JsonShopsPropertyPriceOrQtyGet;

import static android.widget.LinearLayout.LayoutParams;

/**
 * ClassName:SelectShoesPopupWindow <br/>
 * Function: select rental product popup window.商店购物时用于选择 商品属性的对话框，现在球童的选择用的也是这个框，要改成和预约一样的！！！ ysc <br/>
 * Date: 2015-01-26 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class SelectAttributePopupWindow extends BasePopWindow {
    List<JsonShopsPropertyPriceOrQtyGet.DataItem> choiceList;
    private View menuView;
    private TextView tvTitle, tvPrice;
    private Button btnOk, btnCancel;
    private Context context;
    private LinearLayout wheelContainer;
    private ShoppingGoodsListItemFragment.OnPropertyClickListener clickListener;
    private List<JsonShopsPropertyPriceOrQtyGet.DataItem> dataSource;
    private String productID;
    private int maxLevel = 0;
    private String argumentPromoteId;
    private String title;
    // 0: retal 1:promote 2:package
    private String type;
    //  when type = 1 || 2  show it.
    private String defaultPrice;
    private String attrId;
    EditText etSearchCaddie;

//region TextWatcher
    TextWatcher searchTextWatcher = new TextWatcher() {
        CharSequence temp;
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String reg = "[a-zA-Z]";
            String reg1 = "[0-9]*";
            String tempString = temp.toString();
            tempString = tempString.toUpperCase();

            SelectTimeWheelView nextwheelView = (SelectTimeWheelView) wheelContainer.getChildAt(1);
            if(nextwheelView == null) return;
            SelectPropertyWheelAdapter adpSelectProperty = (SelectPropertyWheelAdapter)nextwheelView.getAdapter();
            if(adpSelectProperty == null) return;
            List<JsonShopsPropertyPriceOrQtyGet.DataItem> lstData =  adpSelectProperty.getDataSource();
            if(lstData == null || lstData.size()==0) return;

            for(int i =0;i<lstData.size();i++){
                JsonShopsPropertyPriceOrQtyGet.DataItem item = lstData.get(i);
                if(item.getPraName().equals(tempString)){
                    nextwheelView.setCurrentItem(i);
                    return;
                }
            }
        }
    };
//endregion

    public SelectAttributePopupWindow(Context context, String productID, String title,
                                      String defaultPrice, String type, String argumentPromoteId,
                                      final ShoppingGoodsListItemFragment.OnPropertyClickListener clickListener) {
        super(context);
        this.context = context;
        this.clickListener = clickListener;
        this.productID = productID;
        this.title = title;
        this.defaultPrice = defaultPrice;
        this.argumentPromoteId = argumentPromoteId;
        this.type = type;
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initial();
    }

    public SelectAttributePopupWindow(Context context, String productID, String title,
                                      String defaultPrice, String type, String argumentPromoteId,
                                      final ShoppingGoodsListItemFragment.OnPropertyClickListener clickListener,
                                      List<JsonShopsPropertyPriceOrQtyGet.DataItem> dataSource) {
        super(context);
        this.context = context;
        this.clickListener = clickListener;
        this.productID = productID;
        this.title = title;
        this.defaultPrice = defaultPrice;
        this.argumentPromoteId = argumentPromoteId;
        this.type = type;
        this.dataSource = dataSource;
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initial();
    }

    public SelectAttributePopupWindow(Context context, String productID, String title,
                                      String defaultPrice, String type, String argumentPromoteId,
                                      final ShoppingGoodsListItemFragment.OnPropertyClickListener clickListener,
                                      String attrId) {
        super(context);
        this.context = context;
        this.clickListener = clickListener;
        this.productID = productID;
        this.title = title;
        this.defaultPrice = defaultPrice;
        this.argumentPromoteId = argumentPromoteId;
        this.type = type;
        this.attrId = attrId;
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initial();
    }

    public SelectAttributePopupWindow(Context context, String productID, String title,
                                      String defaultPrice, String type, String argumentPromoteId,
                                      final ShoppingGoodsListItemFragment.OnPropertyClickListener clickListener,
                                      String attrId, List<JsonShopsPropertyPriceOrQtyGet.DataItem> dataSource) {
        super(context);
        this.context = context;
        this.clickListener = clickListener;
        this.productID = productID;
        this.title = title;
        this.defaultPrice = defaultPrice;
        this.argumentPromoteId = argumentPromoteId;
        this.type = type;
        this.attrId = attrId;
        this.dataSource = dataSource;
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initial();
    }

    private void initial() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.popw_caddie_two_wheel, null);

        etSearchCaddie = (EditText) menuView.findViewById(R.id.et_search_caddie);
        etSearchCaddie.setVisibility(View.VISIBLE);
        etSearchCaddie.addTextChangedListener(searchTextWatcher);

        choiceList = new ArrayList<>();
        //设置SelectPicPopupWindow的View
        this.setContentView(menuView);

        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.PopupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        menuView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = menuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        clickListener.OnPropertyClick(1, null, null);
                        dismiss();
                    }
                }
                return true;
            }
        });

        if (dataSource != null) {   //从选择球童进来时 dataSource为null
            initView();
        } else {
            if (Constants.STR_0.equals(type)) {
                netLinksubClass();
            } else if (Constants.STR_1.equals(type)) {
                netLinkPromotesubClass();
            } else {
                netLinksubClass();
            }
        }
    }

    private void resetPriceTitleData() {
        SelectTimeWheelView wheelView = getLastWheel();
        if (wheelView != null) {
            SelectPropertyWheelAdapter adapter = (SelectPropertyWheelAdapter) wheelView.getAdapter();
            String count = StringUtils.EMPTY;
            if (adapter.getDataSource() != null && adapter.getDataSource().size() > 0) {
                JsonShopsPropertyPriceOrQtyGet.DataItem temp = adapter.getDataSource().get(wheelView.getCurrentItem());
                count = String.valueOf(temp.getPraPrice());
            }

            if (Constants.STR_0.equals(type)) {
                tvPrice.setText(AppUtils.getCurrentCurrency(context) + count);
            } else {
                tvPrice.setText(AppUtils.getCurrentCurrency(context) + defaultPrice);
            }
        }
    }

    private void resetNextWheelData(int currentLevel, JsonShopsPropertyPriceOrQtyGet.DataItem temp) {
        for (int i = 0; i < wheelContainer.getChildCount(); i++) {
            SelectTimeWheelView nextwheelView = (SelectTimeWheelView) wheelContainer.getChildAt(i);
            int keyNext = (int) nextwheelView.getTag();
            if (keyNext == (currentLevel + 1)) {
                if (temp.getSubClass() != null && temp.getSubClass().size() > 0) {
                    nextwheelView.setAdapter(new SelectPropertyWheelAdapter(temp.getSubClass()));
                } else {
                    nextwheelView.setAdapter(new SelectPropertyWheelAdapter(new ArrayList<JsonShopsPropertyPriceOrQtyGet.DataItem>()));
                }
                nextwheelView.setCurrentItem(0);
                if (currentLevel + 1 < maxLevel) {
                    if (temp.getSubClass() != null && temp.getSubClass().size() > 0) {
                        resetNextWheelData(currentLevel + 1, temp.getSubClass().get(0));
                    } else {
                        JsonShopsPropertyPriceOrQtyGet tempParaP = new JsonShopsPropertyPriceOrQtyGet(null);
                        JsonShopsPropertyPriceOrQtyGet.DataItem tempPara = tempParaP.new DataItem();
                        resetNextWheelData(currentLevel + 1, tempPara);
                    }
                }
            }
        }
        resetPriceTitleData();
    }

    private SelectTimeWheelView getLastWheel() {
        SelectTimeWheelView wheelView = null;
        for (int i = 0; i < wheelContainer.getChildCount(); i++) {
            wheelView = (SelectTimeWheelView) wheelContainer.getChildAt(i);
            SelectPropertyWheelAdapter adapter = (SelectPropertyWheelAdapter) wheelView.getAdapter();
            if (adapter.getDataSource().size() == 0) {
                wheelView = (SelectTimeWheelView) wheelContainer.getChildAt(i - 1);
                return wheelView;
            }
        }
        return wheelView;
    }

    private void maxLevel(List<JsonShopsPropertyPriceOrQtyGet.DataItem> dataTemp) {
        for (int i = 0; i < dataTemp.size(); i++) {
            JsonShopsPropertyPriceOrQtyGet.DataItem temp = dataTemp.get(i);
            if (temp.getPraLevel() > maxLevel) {
                maxLevel = temp.getPraLevel();
            }

            if (temp.getSubClass() != null && temp.getSubClass().size() > 0) {
                maxLevel(temp.getSubClass());
            }
        }
    }

    private void initView() {
        tvTitle = (TextView) menuView.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        tvPrice = (TextView) menuView.findViewById(R.id.tv_price);
        tvPrice.setTextSize(20);
        tvPrice.setSingleLine();
        tvPrice.setGravity(Gravity.CENTER);
        wheelContainer = (LinearLayout) menuView.findViewById(R.id.ll_wheel_container);
        maxLevel(dataSource);

        for (int i = 0; i < maxLevel; i++) {
            ArrayList<JsonShopsPropertyPriceOrQtyGet.DataItem> values = new ArrayList<>();
            SelectTimeWheelView wheelViewHour = new SelectTimeWheelView(context);

            wheelContainer.addView(wheelViewHour);
            LayoutParams layoutParams = (LayoutParams) wheelViewHour.getLayoutParams();
            layoutParams.width = 0;
            layoutParams.weight = 1;
            layoutParams.height = LayoutParams.WRAP_CONTENT;
            layoutParams.setMargins(0, 0, 1, 0);
            wheelViewHour.setLayoutParams(layoutParams);

            SelectPropertyWheelAdapter adapter = new SelectPropertyWheelAdapter(values);
            int maximumLength = adapter.getMaximumLength();
            if (maximumLength >= 0 && maximumLength < 8) {
                wheelViewHour.setTextSize(40);
            } else if (maximumLength >= 8 && maximumLength < 16) {
                wheelViewHour.setTextSize(30);
            } else {
                wheelViewHour.setTextSize(20);
            }
            wheelViewHour.setAdapter(adapter);
            wheelViewHour.setCyclic(false);
            wheelViewHour.setTag(i + 1);
            wheelViewHour.setCurrentItem(0);
            wheelViewHour.setBackgroundResource(R.drawable.list_item_right_line);

            wheelViewHour.addChangingListener(new SelectTimeOnWheelChangedListener() {
                @Override
                public void onChanged(SelectTimeWheelView wheel, int oldValue, int newValue) {
                    SelectPropertyWheelAdapter adapter = (SelectPropertyWheelAdapter) wheel.getAdapter();
                    JsonShopsPropertyPriceOrQtyGet.DataItem temp = adapter.getDataSource().get(newValue);
                    resetNextWheelData(temp.getPraLevel(), temp);
                }
            });

            if (Utils.isStringNotNullOrEmpty(attrId) && i == 0) {
                wheelViewHour.setVisibility(View.GONE);
            }
        }

        //init data , according  the first wheel's current item ,make sure the second wheel data.
        if (wheelContainer.getChildCount() > 0) {
            SelectTimeWheelView nextWheelView = (SelectTimeWheelView) wheelContainer.getChildAt(0);
            nextWheelView.setAdapter(new SelectPropertyWheelAdapter(dataSource));
            JsonShopsPropertyPriceOrQtyGet.DataItem temp = dataSource.get(nextWheelView.getCurrentItem());
            resetNextWheelData(temp.getPraLevel(), temp);
        }
        btnOk = (Button) menuView.findViewById(R.id.btn_ok);
        btnCancel = (Button) menuView.findViewById(R.id.btn_cancel);
        btnOk.setHeight(30);
        btnCancel.setHeight(30);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectTimeWheelView wheelView = getLastWheel();
                if (wheelView != null) {
                    SelectPropertyWheelAdapter adapter = (SelectPropertyWheelAdapter) wheelView.getAdapter();
                    JsonShopsPropertyPriceOrQtyGet.DataItem temp = adapter.getDataSource().get(wheelView.getCurrentItem());
                    if (Constants.STR_0.equals(type)) {
                        //TO-DO
                    } else if (Constants.STR_1.equals(type)) {
                        temp.setPraPrice(defaultPrice);
                    } else {
                        temp.setPraPrice(defaultPrice);
                    }
                    String attrName = StringUtils.EMPTY;
                    for (int i = 0; i < wheelContainer.getChildCount(); i++) {
                        SelectTimeWheelView wheel = (SelectTimeWheelView) wheelContainer.getChildAt(i);
                        SelectPropertyWheelAdapter adapterTemp = (SelectPropertyWheelAdapter) wheel
                                .getAdapter();
                        if (adapterTemp.getDataSource().size() == 0) {
                            break;
                        } else {
                            if (i != 0) {
                                attrName += Constants.STR_SLASH;
                            }
                            attrName += adapterTemp.getDataSource()
                                    .get(wheel.getCurrentItem())
                                    .getPraName();
                        }
                    }
                    clickListener.OnPropertyClick(0, temp, attrName);
                }
                dismiss();
            }
        };
        btnOk.setOnClickListener(onClickListener);
        btnCancel.setOnClickListener(new View.OnClickListener()

                                     {
                                         @Override
                                         public void onClick(View v) {
                                             clickListener.OnPropertyClick(1, null, null);
                                             dismiss();
                                         }
                                     }
        );
    }

    private void netLinksubClass() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(context));
        params.put(ApiKey.SHOPS_PRODUCT_ID, productID);
        if (Utils.isStringNotNullOrEmpty(attrId)) {
            params.put(ApiKey.SHOPS_LEVEL_ID, attrId);
        }

        HttpManager<JsonShopsPropertyPriceOrQtyGet> hh = new HttpManager<JsonShopsPropertyPriceOrQtyGet>(false) {
            @Override
            public void onJsonSuccess(JsonShopsPropertyPriceOrQtyGet jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    dataSource = jo.getDataList();
                    initView();
                } else {
                    Utils.showShortToast(context, msg);
                }
            }
            @Override
            public void onJsonError(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(context, R.string.msg_common_network_error);
            }
        };
        hh.startGet(context, ApiManager.HttpApi.ShopsPriceSubclassGet, params);
    }


    private void netLinkPromotesubClass() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(context));
        params.put(ApiKey.SHOPS_PRODUCT_ID, productID);
        params.put(ApiKey.SHOPS_PROMOTE_PROMOTE_ID, argumentPromoteId);

        HttpManager<JsonShopsPropertyPriceOrQtyGet> hh = new HttpManager<JsonShopsPropertyPriceOrQtyGet>(false) {
            @Override
            public void onJsonSuccess(JsonShopsPropertyPriceOrQtyGet jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    dataSource = jo.getDataList();
                    initView();
                } else {
                    Utils.showShortToast(context, msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(context, R.string.msg_common_network_error);
            }
        };
        hh.startGet(context, ApiManager.HttpApi.PromoteProductSubclassGet, params);
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }
}
