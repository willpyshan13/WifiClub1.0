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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.common.widget.wheel.SelectEntityWheelAdapter;
import cn.situne.itee.common.widget.wheel.SelectTimeOnWheelChangedListener;
import cn.situne.itee.common.widget.wheel.SelectTimeWheelView;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonBookingDetailList;
import cn.situne.itee.manager.jsonentity.JsonBookingGoodsList;
import cn.situne.itee.view.TeeTimeAddWithGoodDetailView;

import static android.widget.LinearLayout.LayoutParams;

/**
 * ClassName:SelectShoesPopupWindow <br/>
 * Function: select rental product popup window. <br/>
 * UI: 04-5_3,4,5,6
 * Date: 2015-01-26 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */

//预约时用于选择球童的popupWindow
public class SelectShoesPopupWindow extends PopupWindow {
    List<JsonBookingGoodsList.DataList.CategoryListItem> choiceList;

    private View menuView;
    private TextView tvTitle, tvPrice;
    private Button btnOk, btnCancel;
    private BaseFragment mFragment;
    private View rootView;
    private LinearLayout wheelContainer;    //滚动的球童列表
    private List<JsonBookingGoodsList.DataList.CategoryListItem> categoryAttributeList;
    private TeeTimeAddWithGoodDetailView.OnGoodItemClickListener clickListener;
    private JsonBookingGoodsList.DataList dataSource;
    private String argumentCategory;
    private int maxLevel = 0;
    private Map<String, List<JsonBookingGoodsList.DataList.CategoryListItem>> map;

    private SelectTimeOnWheelChangedListener selectTimeOnWheelChangedListener = new SelectTimeOnWheelChangedListener() {
        @Override
        public void onChanged(SelectTimeWheelView wheel, int oldValue, int newValue) {
            SelectEntityWheelAdapter adapter = (SelectEntityWheelAdapter) wheel.getAdapter();
            JsonBookingGoodsList.DataList.CategoryListItem temp = adapter.getDataSource().get(newValue);
            Integer categoryId = temp.getAttrId();
            resetNextWheelData(temp.getAttrLevel(), categoryId);
        }
    };

    public SelectShoesPopupWindow(BaseFragment mFragment, View rootView, String argumentCategory, TeeTimeAddWithGoodDetailView.OnGoodItemClickListener clickListener) {
        super(mFragment.getActivity());
        this.mFragment = mFragment;
        this.clickListener = clickListener;
        this.rootView = rootView;
        this.argumentCategory = argumentCategory;

        LayoutInflater inflater = (LayoutInflater) mFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.popw_caddie_two_wheel, null);
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
                        dismiss();
                    }
                }
                return true;
            }
        });
        netLinkBookingGoodsList();

    }

    private void resetPriceTitleData() {
        SelectTimeWheelView wheelView = getLastWheel();
        if (wheelView != null) {
            SelectEntityWheelAdapter adapter = (SelectEntityWheelAdapter) wheelView.getAdapter();

            String priceString = Constants.STR_EMPTY;
            if (adapter.getDataSource() != null && adapter.getDataSource().size() > 0) {

                JsonBookingGoodsList.DataList.CategoryListItem temp = adapter.getDataSource().get(wheelView.getCurrentItem());
                String price = null;

                //according goodsPid ,get goodid.
                for (int i = 0; i < dataSource.getGoodsList().size(); i++) {
                    JsonBookingGoodsList.DataList.GoodsListItem goodsListItem = dataSource.getGoodsList().get(i);
                    if (goodsListItem.getGoodsAttrId().equals(temp.getAttrId())) {
                        price = goodsListItem.getGoodsPrice();
                    }
                }

                if (Utils.isStringNotNullOrEmpty(dataSource.getBookingFee()) && Utils.isStringNotNullOrEmpty(price)) {
                    if (StringUtils.isNotEmpty(dataSource.getBookingFee())
                            && !Constants.STR_0.equals(dataSource.getBookingFee())) {
                        priceString = AppUtils.getCurrentCurrency(mFragment.getActivity())
                                + dataSource.getBookingFee()
                                + Constants.STR_PLUS;
                    }
                    priceString += AppUtils.getCurrentCurrency(mFragment.getActivity()) + price;
                } else {
                    priceString = AppUtils.getCurrentCurrency(mFragment.getActivity()) + price;
                }
            }
            tvPrice.setText(priceString);
        }
    }


    private void resetNextWheelData(int currentLevel, int currentCategoryId) {

        List<JsonBookingGoodsList.DataList.CategoryListItem> values = map.get(new BigDecimal(currentLevel + 1).toString());
        List<JsonBookingGoodsList.DataList.CategoryListItem> valuesTemp = new ArrayList<>();

        if (values != null) {
            for (int i = 0; i < values.size(); i++) {
                valuesTemp.add(values.get(i));
            }
            for (Iterator it = valuesTemp.iterator(); it.hasNext(); ) {
                JsonBookingGoodsList.DataList.CategoryListItem entity = (JsonBookingGoodsList.DataList.CategoryListItem) it.next();
                if (entity.getAttrParentId() != currentCategoryId) {
                    it.remove();
                }
            }
        }
        for (int i = 0; i < wheelContainer.getChildCount(); i++) {
            SelectTimeWheelView nextwheelView = (SelectTimeWheelView) wheelContainer.getChildAt(i);
            String keyNext = (String) nextwheelView.getTag();
            if (keyNext.equals(String.valueOf(currentLevel + 1))) {
                nextwheelView.setAdapter(new SelectEntityWheelAdapter(valuesTemp));
                nextwheelView.setCurrentItem(0);
                if (currentLevel + 1 < maxLevel) {

                    if (valuesTemp != null && valuesTemp.size() > 0) {
                        resetNextWheelData(currentLevel + 1, valuesTemp.get(0).getAttrId());
                    } else {
                        resetNextWheelData(currentLevel + 1, 0);
                    }

                }
            }
        }

        resetPriceTitleData();
    }


    private JsonBookingGoodsList.DataList.CategoryListItem resetCurrentWheelData(JsonBookingGoodsList.DataList.CategoryListItem currentItem, boolean isfirst) {

        Integer index = 0;
        JsonBookingGoodsList.DataList.CategoryListItem frontItem = null;

        List<JsonBookingGoodsList.DataList.CategoryListItem> values = map.get(new BigDecimal(currentItem.getAttrLevel()).toString());
        List<JsonBookingGoodsList.DataList.CategoryListItem> valuesTemp = new ArrayList<>();

        if (values != null) {
            for (int i = 0; i < values.size(); i++) {
                valuesTemp.add(values.get(i));
            }
            for (int i = 0; i < valuesTemp.size(); i++) {
                JsonBookingGoodsList.DataList.CategoryListItem entity = valuesTemp.get(i);
                if (!entity.getAttrParentId().equals(currentItem.getAttrParentId())) {
                    valuesTemp.remove(entity);
                    --i;
                }
            }
            for (int i = 0; i < valuesTemp.size(); i++) {
                JsonBookingGoodsList.DataList.CategoryListItem entity = valuesTemp.get(i);
                if (entity.getAttrId().equals(currentItem.getAttrId())) {
                    index = i;
                }
            }
        }
        for (int i = 0; i < wheelContainer.getChildCount(); i++) {
            SelectTimeWheelView nextwheelView = (SelectTimeWheelView) wheelContainer.getChildAt(i);
            String keyNext = (String) nextwheelView.getTag();
            if (keyNext.equals(String.valueOf(currentItem.getAttrLevel()))) {
                nextwheelView.removeChangingListener(selectTimeOnWheelChangedListener);
                nextwheelView.setAdapter(new SelectEntityWheelAdapter(valuesTemp));
                nextwheelView.setCurrentItem(index);
                if (isfirst) {
                    resetNextWheelData(currentItem.getAttrLevel(), currentItem.getAttrId());
                }

                nextwheelView.addChangingListener(selectTimeOnWheelChangedListener);

            }
        }


        List<JsonBookingGoodsList.DataList.CategoryListItem> valuesParent = map.get(new BigDecimal(currentItem.getAttrLevel() - 1).toString());

        if (valuesParent != null) {
            for (int i = 0; i < valuesParent.size(); i++) {
                JsonBookingGoodsList.DataList.CategoryListItem entity = valuesParent.get(i);
                if (entity.getAttrId().equals(currentItem.getAttrParentId())) {
                    frontItem = entity;
                }
            }

        }

        return frontItem;

    }

    private SelectTimeWheelView getLastWheel() {
        SelectTimeWheelView wheelView = null;

        for (int i = 0; i < wheelContainer.getChildCount(); i++) {
            wheelView = (SelectTimeWheelView) wheelContainer.getChildAt(i);
            SelectEntityWheelAdapter adapter = (SelectEntityWheelAdapter) wheelView.getAdapter();
            if (adapter.getDataSource().size() == 0) {
                wheelView = (SelectTimeWheelView) wheelContainer.getChildAt(i - 1);
                return wheelView;
            }
        }
        return wheelView;

    }


    private void initView(List<JsonBookingGoodsList.DataList.CategoryListItem> datasource) {
        tvTitle = (TextView) menuView.findViewById(R.id.tv_title);
        tvPrice = (TextView) menuView.findViewById(R.id.tv_price);
        tvPrice.setTextSize(Constants.FONT_SIZE_LARGER);
        tvPrice.setSingleLine();
        tvPrice.setGravity(Gravity.CENTER);

        switch (argumentCategory) {
            case "1":
                tvTitle.setText(mFragment.getString(R.string.newteetimes_caddie));
                break;
            case "2":
                tvTitle.setText(mFragment.getString(R.string.newteetimes_menu_cart));
                break;
            case "3":
                tvTitle.setText(mFragment.getString(R.string.newteetimes_menu_clubs));
                break;
            case "4":
                tvTitle.setText(mFragment.getString(R.string.newteetimes_menu_shoes));
                break;
            case "5":
                tvTitle.setText(mFragment.getString(R.string.newteetimes_menu_trolley));
                break;
            case "6":
                tvTitle.setText(mFragment.getString(R.string.newteetimes_menu_umbrella));
                break;
            case "7":
                tvTitle.setText(mFragment.getString(R.string.newteetimes_menu_towel));
                break;
            default:
                break;
        }

        map = new LinkedHashMap<>();
        for (int i = 0; i < datasource.size(); i++) {
            JsonBookingGoodsList.DataList.CategoryListItem temp = datasource.get(i);
            if (!map.containsKey(temp.getAttrLevel().toString())) {
                List<JsonBookingGoodsList.DataList.CategoryListItem> values = new ArrayList<>();
                values.add(temp);
                map.put(temp.getAttrLevel().toString(), values);
            } else {
                map.get(temp.getAttrLevel().toString()).add(temp);
            }
        }

        maxLevel = map.keySet().size();
        int textSize = 50;
        for (String key : map.keySet()) {
            List<JsonBookingGoodsList.DataList.CategoryListItem> values = map.get(key);
            SelectEntityWheelAdapter adapter = new SelectEntityWheelAdapter(values);
            int maximumLength = adapter.getMaximumLength();
            if (maximumLength >= 0 && maximumLength < 8) {
                if (textSize > 40) {
                    textSize = 40;
                }
            } else if (maximumLength >= 8 && maximumLength < 16) {
                if (textSize > 30) {
                    textSize = 30;
                }
            } else {
                textSize = 20;
            }
        }

        wheelContainer = (LinearLayout) menuView.findViewById(R.id.ll_wheel_container);
        for (String key : map.keySet()) {
            List<JsonBookingGoodsList.DataList.CategoryListItem> values = map.get(key);
            SelectTimeWheelView wheelViewHour = new SelectTimeWheelView(mFragment.getActivity());
            wheelViewHour.setMaxHeight(1);
            wheelContainer.addView(wheelViewHour);
            LayoutParams layoutParams = (LayoutParams) wheelViewHour.getLayoutParams();
            layoutParams.width = 0;
            layoutParams.weight = 1;
            layoutParams.height = LayoutParams.WRAP_CONTENT;
            layoutParams.setMargins(0, 0, 1, 0);
            wheelViewHour.setLayoutParams(layoutParams);

            SelectEntityWheelAdapter adapter = new SelectEntityWheelAdapter(values);
            wheelViewHour.setTextSize(textSize);
            wheelViewHour.setBackground(mFragment.getDrawable(R.drawable.list_item_right_line));
            wheelViewHour.setAdapter(adapter);
            wheelViewHour.setCyclic(false);
            wheelViewHour.setTag(key);
            wheelViewHour.addChangingListener(selectTimeOnWheelChangedListener);
        }

        //init data , according  the first wheel's current item ,make sure the second wheel data.
//        for (int i = 0; i < wheelContainer.getChildCount(); i++) {
        SelectTimeWheelView nextWheelView = (SelectTimeWheelView) wheelContainer.getChildAt(0);
        SelectEntityWheelAdapter adapter = (SelectEntityWheelAdapter) nextWheelView.getAdapter();

        JsonBookingGoodsList.DataList.CategoryListItem temp = adapter.getDataSource().get(nextWheelView.getCurrentItem());
        resetNextWheelData(temp.getAttrLevel(), temp.getAttrId());
//        }

        btnOk = (Button) menuView.findViewById(R.id.btn_ok);
        btnCancel = (Button) menuView.findViewById(R.id.btn_cancel);
        btnOk.setHeight(30);
        btnCancel.setHeight(30);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectTimeWheelView wheelView = getLastWheel();
                if (wheelView != null) {
                    SelectEntityWheelAdapter adapter = (SelectEntityWheelAdapter) wheelView.getAdapter();
                    JsonBookingGoodsList.DataList.CategoryListItem temp = adapter.getDataSource().get(wheelView.getCurrentItem());
                    Integer goodsId = null;
                    Integer goodsAttrId = null;
                    String goodsPrice = StringUtils.EMPTY;
                    //according goodsPid ,get goods id.
                    for (int i = 0; i < dataSource.getGoodsList().size(); i++) {
                        JsonBookingGoodsList.DataList.GoodsListItem goodsListItem = dataSource.getGoodsList().get(i);
                        if (goodsListItem.getGoodsAttrId().equals(temp.getAttrId())) {
                            goodsId = goodsListItem.getGoodsId();
                            goodsAttrId = goodsListItem.getGoodsAttrId();
                            if (Utils.isStringNotNullOrEmpty(goodsListItem.getGoodsPrice())) {
                                goodsPrice = goodsListItem.getGoodsPrice();
                            }
                        }
                    }
                    JsonBookingDetailList.DataListItem.GoodListItem goodListItem = (JsonBookingDetailList.DataListItem.GoodListItem) rootView.getTag();
                    goodListItem.setGoodsId(goodsId);
                    goodListItem.setGoodsLastAttr(goodsAttrId);
                    goodListItem.setGoodsPrice(goodsPrice);
                    clickListener.OnGoodItemClick(0, rootView, String.valueOf(goodsId));
                }
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                netLinkDoDelBookingGoods();
                JsonBookingDetailList.DataListItem.GoodListItem goodListItem = (JsonBookingDetailList.DataListItem.GoodListItem) rootView.getTag();
                goodListItem.setGoodsId(null);
                goodListItem.setGoodsLastAttr(null);
                switch (argumentCategory) {
                    case "1":
                        rootView.setBackground(mFragment.getDrawable(R.drawable.caddie_off));
                    case "2":
                        rootView.setBackground(mFragment.getDrawable(R.drawable.cart_off));
                        break;
                    case "3":
                        rootView.setBackground(mFragment.getDrawable(R.drawable.club_off));
                        break;
                    case "4":
                        rootView.setBackground(mFragment.getDrawable(R.drawable.shoes_off));
                        break;
                    case "5":
                        rootView.setBackground(mFragment.getDrawable(R.drawable.trolley_off));
                        break;
                    case "6":
                        rootView.setBackground(mFragment.getDrawable(R.drawable.umbrella_off));
                        break;
                    case "7":
                        rootView.setBackground(mFragment.getDrawable(R.drawable.towel_off));
                        break;
                    default:
                        break;
                }
                dismiss();
            }
        });

        //init the data from parent.
        JsonBookingDetailList.DataListItem.GoodListItem goodListItem = (JsonBookingDetailList.DataListItem.GoodListItem) rootView.getTag();
        Integer parentId = goodListItem.getGoodsLastAttr();

        JsonBookingGoodsList.DataList.CategoryListItem lastSelect = null;
        for (int i = 0; i < dataSource.getCategoryList().size(); i++) {
            JsonBookingGoodsList.DataList.CategoryListItem listItem = dataSource.getCategoryList().get(i);
            if (parentId != null) {
                if (parentId.equals(listItem.getAttrId())) {
                    lastSelect = new JsonBookingGoodsList.DataList.CategoryListItem();
                    lastSelect.setAttrName(listItem.getAttrName());
                    lastSelect.setAttrLevel(listItem.getAttrLevel());
                    lastSelect.setAttrId(listItem.getAttrId());
                    lastSelect.setAttrParentId(listItem.getAttrParentId());
                }
            }
        }

        if (lastSelect != null) {

            int level = lastSelect.getAttrLevel();
            boolean isfirst = true;
            while (level > 0) {
                lastSelect = resetCurrentWheelData(lastSelect, isfirst);
                isfirst = false;
                level--;
            }
        }
    }

    public void netLinkBookingGoodsList() {
        //接口_04-5-2预约消费列表
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(mFragment.getActivity()));
        params.put(ApiKey.TEE_TIME_CATEGORY_ID, argumentCategory);
        params.put(ApiKey.COURSE_ID, AppUtils.getCurrentCourseId(mFragment.getActivity()));

        HttpManager<JsonBookingGoodsList> hh = new HttpManager<JsonBookingGoodsList>(mFragment) {

            @Override
            public void onJsonSuccess(JsonBookingGoodsList jo) {
                int returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    dataSource = jo.getDataList();
                    initView(dataSource.getCategoryList());
                } else {
                    Utils.showShortToast(mFragment.getActivity(), jo.getReturnInfo());
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.start(mFragment.getActivity(), ApiManager.HttpApi.BookingGoodsList, params);
    }
}
