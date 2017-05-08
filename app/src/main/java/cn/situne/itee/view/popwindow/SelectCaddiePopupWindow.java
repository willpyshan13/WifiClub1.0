/**
 * Project Name: itee
 * File Name:  SelectCaddiePopupWindow.java
 * Package Name: cn.situne.itee.common.widget.wheel
 * Date:   2015-01-15
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.view.popwindow;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
 * ClassName:SelectCaddiePopupWindow <br/>
 * Function: select caddie popup window. <br/>
 * ysc 预约界面用于选择球童的弹窗    <br/>
 * UI:UI: 04-5_2
 * Date: 2015-01-26 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class SelectCaddiePopupWindow extends BasePopWindow {
    private static final String TEE_TIME_CATEGORY_ID_CADDIE = "1";

    private View menuView;
    private TextView tvTitle, tvPrice;
    private Button btnOk, btnCancel;
    private EditText etSearchCaddie;
    private BaseFragment mFragment;
    private View rootView;
    private LinearLayout wheelContainer;
    private TeeTimeAddWithGoodDetailView.OnGoodItemClickListener itemClickListener;
    private JsonBookingGoodsList.DataList dataSource;
    private SelectTimeWheelView wheelViewHour;
    private List<JsonBookingGoodsList.DataList.CategoryListItem> levelList;
    private List<JsonBookingGoodsList.DataList.CategoryListItem> caddieList;

    private String bookingDateTime;

    public SelectCaddiePopupWindow(BaseFragment mFragment, View rootView,
                                   TeeTimeAddWithGoodDetailView.OnGoodItemClickListener itemClickListener, String bookingDateTime) {
        super(mFragment.getActivity());
        this.mFragment = mFragment;
        this.rootView = rootView;
        this.itemClickListener = itemClickListener;
        this.bookingDateTime = bookingDateTime;

        LayoutInflater inflater = (LayoutInflater) mFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.popw_caddie_two_wheel, null);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setContentView(menuView);
        formatViews();
        setHideListener(menuView);

        netLinkBookingGoodsList();
    }

    private void resetPriceTitleData() {
        SelectTimeWheelView wheelView = (SelectTimeWheelView) wheelContainer.getChildAt(wheelContainer.getChildCount() - 1);
        SelectEntityWheelAdapter adapter = (SelectEntityWheelAdapter) wheelView.getAdapter();

        String price = StringUtils.EMPTY;
        if (adapter.getDataSource() != null && adapter.getDataSource().size() > 0) {
            JsonBookingGoodsList.DataList.CategoryListItem temp = adapter.getDataSource()
                    .get(wheelView.getCurrentItem());
            //according goodsPid ,get good id.
            for (int i = 0; i < dataSource.getGoodsList().size(); i++) {
                JsonBookingGoodsList.DataList.GoodsListItem goodsListItem = dataSource.getGoodsList().get(i);
                if (goodsListItem.getGoodsAttrId().equals(temp.getAttrParentId())) {
                    price = goodsListItem.getGoodsPrice();
                }
            }
        }

        tvPrice.setTextSize(14);
        String priceString = Constants.STR_EMPTY;
        if (StringUtils.isNotEmpty(dataSource.getBookingFee())
                && !Constants.STR_0.equals(dataSource.getBookingFee())) {
            priceString += AppUtils.getCurrentCurrency(mFragment.getActivity())
                    + dataSource.getBookingFee() + Constants.STR_PLUS;
        }
        priceString += AppUtils.getCurrentCurrency(mFragment.getActivity()) + price;
        tvPrice.setText(priceString);
    }

    private void initView() {
        tvTitle = (TextView) menuView.findViewById(R.id.tv_title);
        tvPrice = (TextView) menuView.findViewById(R.id.tv_price);
        tvPrice.setSingleLine();
        tvPrice.setGravity(Gravity.CENTER);
        etSearchCaddie = (EditText) menuView.findViewById(R.id.et_search_caddie);
        etSearchCaddie.setVisibility(View.VISIBLE);

        wheelContainer = (LinearLayout) menuView.findViewById(R.id.ll_wheel_container);
        tvTitle.setText(mFragment.getString(R.string.newteetimes_caddie));

        levelList = new ArrayList<>();
        caddieList = new ArrayList<>();
        for (int i = 0; i < dataSource.getCategoryList().size(); i++) {
            JsonBookingGoodsList.DataList.CategoryListItem temp = dataSource.getCategoryList().get(i);
            if (Constants.STR_1.equals(String.valueOf(temp.getAttrLevel()))) {
                levelList.add(temp);
//                caddieList.add(temp);
            } else {
                caddieList.add(temp);
            }
        }

        JsonBookingDetailList.DataListItem.GoodListItem goodListItem =
                (JsonBookingDetailList.DataListItem.GoodListItem) rootView.getTag();

        //fixed by syb.
        if (goodListItem.getGoodsId() == null){
            if (caddieList.size() <= 0){
                Utils.showShortToast(mFragment.getBaseActivity(),mFragment.getString(R.string.no_caddies_mes));
                this.dismiss();
                return ;
            }
        }

        int index = 0;
        for (int i = 0; i < caddieList.size(); i++) {
            JsonBookingGoodsList.DataList.CategoryListItem temp = caddieList.get(i);
            if (goodListItem.getGoodsLastAttr() != null) {
                if (goodListItem.getGoodsLastAttr().equals(temp.getAttrId())) {
                    index = i;
                }
            }
            for (int j = 0; j < levelList.size(); j++) {
                JsonBookingGoodsList.DataList.CategoryListItem tempLevel = levelList.get(j);
                if (temp.getAttrParentId().equals(tempLevel.getAttrId())) {
                    temp.setAttrName(temp.getAttrName() + Constants.STR_BRACKETS_START + tempLevel.getAttrName() + Constants.STR_BRACKETS_END);
                }
            }
        }

        wheelViewHour = new SelectTimeWheelView(mFragment.getActivity());
        wheelViewHour.setMaxHeight(1);
        wheelContainer.addView(wheelViewHour);
        LayoutParams layoutParams = (LayoutParams) wheelViewHour.getLayoutParams();
        layoutParams.width = 0;
        layoutParams.weight = 1;
        layoutParams.height =300;// LayoutParams.WRAP_CONTENT;
        layoutParams.setMargins(0, 0, 1, 0);
        wheelViewHour.setLayoutParams(layoutParams);

        SelectEntityWheelAdapter adapter = new SelectEntityWheelAdapter(caddieList);
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
        wheelViewHour.setCurrentItem(index);

        resetPriceTitleData();
        wheelViewHour.addChangingListener(new SelectTimeOnWheelChangedListener() {
            @Override
            public void onChanged(SelectTimeWheelView wheel, int oldValue, int newValue) {
                resetPriceTitleData();
            }
        });

        btnOk = (Button) menuView.findViewById(R.id.btn_ok);
        btnCancel = (Button) menuView.findViewById(R.id.btn_cancel);

        btnOk.setHeight(30);
        btnCancel.setHeight(30);

        etSearchCaddie.addTextChangedListener(new TextWatcher() {
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
                if (StringUtils.EMPTY.equals(tempString)) {
                    levelList.clear();
                    caddieList.clear();
                    for (int i = 0; i < dataSource.getCategoryList().size(); i++) {
                        JsonBookingGoodsList.DataList.CategoryListItem temp = dataSource.getCategoryList().get(i);
                        if (temp.getAttrLevel() == 1) {
                            levelList.add(temp);
                        } else {
                            caddieList.add(temp);
                        }
                    }
                    SelectEntityWheelAdapter adapter = new SelectEntityWheelAdapter(caddieList);
                    wheelViewHour.setAdapter(adapter);
                    wheelViewHour.setCurrentItem(0);
                } else if (tempString.matches(reg)) {
                    Integer parentId = null;

                    for (int i = 0; i < levelList.size(); i++) {
                        if (tempString.equals(levelList.get(i).getAttrName())) {
                            parentId = levelList.get(i).getAttrId();
                        }
                    }
                    if (parentId != null) {
                        caddieList.clear();
                        for (int i = 0; i < dataSource.getCategoryList().size(); i++) {
                            JsonBookingGoodsList.DataList.CategoryListItem temp = dataSource.getCategoryList().get(i);
                            if (temp.getAttrLevel() == 2) {
                                caddieList.add(temp);
                            }
                        }
                        for (int j = 0; j < caddieList.size(); j++) {

                            if (!parentId.equals(caddieList.get(j).getAttrParentId())) {
                                caddieList.remove(caddieList.get(j));
                                --j;
                            }
                        }
                        SelectEntityWheelAdapter adapter = new SelectEntityWheelAdapter(caddieList);
                        wheelViewHour.setAdapter(adapter);
                        wheelViewHour.setCurrentItem(0);
                    }


                } else if (tempString.matches(reg1)) {  // mumber input
                    for (int i = 0; i < caddieList.size(); i++) {
                        String[] args = caddieList.get(i).getAttrName().split("\\(");
                        if (tempString.equals(args[0])) {
                            wheelViewHour.setCurrentItem(i);
                        }
                    }
                }
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectTimeWheelView wheelView = (SelectTimeWheelView) wheelContainer.getChildAt(wheelContainer.getChildCount() - 1);

                SelectEntityWheelAdapter adapter = (SelectEntityWheelAdapter) wheelView.getAdapter();
                JsonBookingGoodsList.DataList.CategoryListItem temp = adapter.getDataSource()
                        .get(wheelView.getCurrentItem());
                Integer goodsId = null;
                Integer goodsAttrId = null;
                String caddieNo = null;

                String currentCaddieName = adapter.getItem(wheelView.getCurrentItem());
                if (Utils.isStringNotNullOrEmpty(currentCaddieName)
                        && currentCaddieName.contains(Constants.STR_BRACKETS_START)) {
                    caddieNo = currentCaddieName.substring(0, currentCaddieName.indexOf(Constants.STR_BRACKETS_START));
                }

                String price = Constants.STR_0;
                //according goodsPid ,get good id.
                for (int i = 0; i < dataSource.getGoodsList().size(); i++) {
                    JsonBookingGoodsList.DataList.GoodsListItem goodsListItem = dataSource.getGoodsList().get(i);
                    if (goodsListItem.getGoodsAttrId().equals(temp.getAttrParentId())) {
                        goodsId = goodsListItem.getGoodsId();
                        goodsAttrId = temp.getAttrId();//TODO
                        price = goodsListItem.getGoodsPrice();
                    }
                }
                JsonBookingDetailList.DataListItem.GoodListItem goodListItem = (JsonBookingDetailList.DataListItem.GoodListItem) rootView
                        .getTag();
                goodListItem.setGoodsId(goodsId);
                goodListItem.setGoodsLastAttr(goodsAttrId);
                goodListItem.setGoodsPrice(price);
                goodListItem.setCaddieNo(caddieNo);

                itemClickListener.OnGoodItemClick(0, rootView, String.valueOf(goodsId));
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonBookingDetailList.DataListItem.GoodListItem goodListItem
                        = (JsonBookingDetailList.DataListItem.GoodListItem) rootView.getTag();
                goodListItem.setGoodsId(null);
                goodListItem.setGoodsLastAttr(null);
                rootView.setBackground(mFragment.getDrawable(R.drawable.caddie_off));
                dismiss();
            }
        });
    }

    public void netLinkBookingGoodsList() {
        //接口_04-5-2预约消费列表
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(mFragment.getActivity()));
        params.put(ApiKey.TEE_TIME_CATEGORY_ID, TEE_TIME_CATEGORY_ID_CADDIE);
        params.put(ApiKey.TEE_TIME_BOOKING_TIME, bookingDateTime);
        params.put(ApiKey.COURSE_ID, AppUtils.getCurrentCourseId(mFragment.getActivity()));

        HttpManager<JsonBookingGoodsList> hh = new HttpManager<JsonBookingGoodsList>(mFragment) {
            @Override
            public void onJsonSuccess(JsonBookingGoodsList jo) {
                int returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    dataSource = jo.getDataList();
                    initView();
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
