/**
 * Project Name: itee
 * File Name:	 TeeTimeSearchProfileFragment.java
 * Package Name: cn.situne.itee.fragment.teetime
 * Date:		 2015-03-26
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.teetime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.PositionInformationEntity;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.player.PlayerFragment;
import cn.situne.itee.fragment.player.PlayerInfoEditFragment;
import cn.situne.itee.fragment.shopping.ShoppingGoodsListFragment;
import cn.situne.itee.fragment.shopping.ShoppingPaymentFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonCustomerBookingSearch;
import cn.situne.itee.manager.jsonentity.JsonTeeTimeBookingSearch;
import cn.situne.itee.view.IteeSearchView;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:TeeTimeSearchProfileFragment <br/>
 * Function: search profile. <br/>
 * Date: 2015-03-26 <br/>
 * UI:03-5
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class TeeTimeSearchProfileFragment extends BaseFragment {

//    private static final String STATUS_F = "F";
//    private static final String STATUS_R = "R";
//
//    private static final String BOOKING_COLOR_RED = "2";
//    private static final String BOOKING_COLOR_BLUE = "3";
//
////    public ArrayList<JsonCustomerBookingSearch.Member> dataList;
//    private IteeSearchView titleSearchView;
//    private ListView searchDataList;
//    private ListViewAdapter listViewAdapter;
//    private IteeTextView footerMes;
//    private Button addBtn;
//
//    private String selectDate;
//
//    private RelativeLayout body;
//    View.OnClickListener gotoDetailedListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//
//            Bundle bundle = new Bundle();
//            bundle.putString("type",String.valueOf(view.getTag()));
//            bundle.putString("selectDate", selectDate);
//            push(TeeTimeSearchProfileListFragment.class,bundle);
//           // push(TeeTimeSearchProfileHistoryFragment.class,bundle);
//        }
//    };
//    private LinearLayout shortcutLayout;
//    private View upLine ;
//
//    private  IteeTextView upTextView;
//
//    @Override
//    protected int getFragmentId() {
//        return R.layout.fragment_tee_time_search;
//    }
//
//    @Override
//    public int getTitleResourceId() {
//        return TITLE_NONE;
//    }
//
//    @Override
//    protected void initControls(View rootView) {
//
//        dataList = new ArrayList<>();
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//
//            selectDate = bundle.getString("selectDate",Constants.STR_EMPTY);
//
//        }
//        dataList = new ArrayList<>();
//        searchDataList = (ListView) rootView.findViewById(R.id.searchDataList);
//        shortcutLayout= (LinearLayout) rootView.findViewById(R.id.shortcutLayout);
//        body= (RelativeLayout) rootView.findViewById(R.id.body);
//        searchDataList.setDividerHeight(0);
//        addFooterView();
//        listViewAdapter = new ListViewAdapter();
//        searchDataList.setAdapter(listViewAdapter);
//
//        initLayout();
//        upLine = AppUtils.getSeparatorLine(getBaseActivity());
//        upLine.setId(View.generateViewId());
//
//        body.addView(upLine);
//        RelativeLayout.LayoutParams upLineParams = (RelativeLayout.LayoutParams)upLine.getLayoutParams();
//        upLineParams.height = getActualHeightOnThisDevice(2);
//        upLineParams.addRule(RelativeLayout.ABOVE, R.id.shortcutLayout);
//
//
//
//        upTextView = new IteeTextView(getBaseActivity());
//        upTextView.setTextSize(Constants.FONT_SIZE_LARGER);
//        body.addView(upTextView);
//
//        upTextView.setText(getString(R.string.search_more));
//        upTextView.setTextColor(getColor(R.color.common_gray));
//        upTextView.setGravity(Gravity.CENTER);
//        RelativeLayout.LayoutParams upTextViewParams = (RelativeLayout.LayoutParams)upTextView.getLayoutParams();
//        upTextViewParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//        upTextViewParams.addRule(RelativeLayout.ABOVE, upLine.getId());
//
//    }
//
//    @Override
//    protected void setDefaultValueOfControls() {
//
//    }
//
//    @Override
//    protected void setListenersOfControls() {
//
//    }
//
//    @Override
//    protected void setLayoutOfControls() {
//
//    }
//
//    @Override
//    protected void setPropertyOfControls() {
//
//    }
//
//    @Override
//    protected void configActionBar() {
//        setStackedActionBar();
//        initTitleView();
//    }
//
//
//    @Override
//    protected void reShowWithBackValue() {
//        super.reShowWithBackValue();
//
//
//
//    }
//
//    private void initLayout(){
//
//        LinearLayout row1 = new LinearLayout(getBaseActivity());
//        LinearLayout row2 = new LinearLayout(getBaseActivity());
//        LinearLayout row3 = new LinearLayout(getBaseActivity());
//        row1.setOrientation(LinearLayout.HORIZONTAL);
//        row2.setOrientation(LinearLayout.HORIZONTAL);
//        row3.setOrientation(LinearLayout.HORIZONTAL);
//
//        LinearLayout.LayoutParams bookingBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(200));
//        LinearLayout.LayoutParams onCourseBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(200));
//        LinearLayout.LayoutParams checkOutBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(200));
//        bookingBtnParams.weight = 1;
//        onCourseBtnParams.weight = 1;
//        checkOutBtnParams.weight = 1;
//        ClickItem bookingBtn = new ClickItem(getBaseActivity());
//
//        ClickItem onCourseBtn = new ClickItem(getBaseActivity());
//        ClickItem checkOutBtn = new ClickItem(getBaseActivity());
//
//        bookingBtn.setOnClickListener(gotoDetailedListener);
//        onCourseBtn.setOnClickListener(gotoDetailedListener);
//        checkOutBtn.setOnClickListener(gotoDetailedListener);
//        bookingBtn.setLayoutParams(bookingBtnParams);
//        onCourseBtn.setLayoutParams(onCourseBtnParams);
//        checkOutBtn.setLayoutParams(checkOutBtnParams);
//        bookingBtn.setLayout(getString(R.string.search_booking), R.drawable.search_profile_1);
//        onCourseBtn.setLayout(getString(R.string.search_on_course),R.drawable.search_profile_2);
//        checkOutBtn.setLayout(getString(R.string.search_check_out), R.drawable.search_profile_3);
//
//
//        row1.addView(bookingBtn);
//        row1.addView(onCourseBtn);
//        row1.addView(checkOutBtn);
//        bookingBtn.setTag("1");
//        onCourseBtn.setTag("2");
//        checkOutBtn.setTag("3");
//
//
//        LinearLayout.LayoutParams memberBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(200));
//        LinearLayout.LayoutParams guestBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(200));
//        LinearLayout.LayoutParams walkInBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(200));
//        memberBtnParams.weight = 1;
//        guestBtnParams.weight = 1;
//        walkInBtnParams.weight = 1;
//        ClickItem memberBtn = new ClickItem(getBaseActivity());
//        ClickItem guestBtn = new ClickItem(getBaseActivity());
//        ClickItem walkInBtn = new ClickItem(getBaseActivity());
//
//        memberBtn.setTag("4");
//        guestBtn.setTag("5");
//        walkInBtn.setTag("6");
//
//
//        memberBtn.setOnClickListener(gotoDetailedListener);
//        guestBtn.setOnClickListener(gotoDetailedListener);
//        walkInBtn.setOnClickListener(gotoDetailedListener);
//
//        memberBtn.setLayoutParams(memberBtnParams);
//        guestBtn.setLayoutParams(guestBtnParams);
//        walkInBtn.setLayoutParams(walkInBtnParams);
//        memberBtn.setLayout(getString(R.string.search_member), R.drawable.search_profile_4);
//        guestBtn.setLayout(getString(R.string.search_guest), R.drawable.search_profile_5);
//        walkInBtn.setLayout(getString(R.string.search_walk_in), R.drawable.search_profile_6);
//
//        row2.addView(memberBtn);
//        row2.addView(guestBtn);
//        row2.addView(walkInBtn);
//
//        LinearLayout.LayoutParams agentBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(200));
//        LinearLayout.LayoutParams eventBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(200));
//        LinearLayout.LayoutParams otherBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(200));
//        agentBtnParams.weight = 1;
//        eventBtnParams.weight = 1;
//        otherBtnParams.weight = 1;
//        ClickItem agentBtn = new ClickItem(getBaseActivity());
//        ClickItem eventBtn = new ClickItem(getBaseActivity());
//        ClickItem otherBtn = new ClickItem(getBaseActivity());
//        agentBtn.setLayoutParams(agentBtnParams);
//        eventBtn.setLayoutParams(eventBtnParams);
//        otherBtn.setLayoutParams(otherBtnParams);
//        agentBtn.setLayout(getString(R.string.search_agent), R.drawable.search_profile_7);
//        eventBtn.setLayout(getString(R.string.search_event), R.drawable.search_profile_8);
//        otherBtn.setLayout(getString(R.string.search_walk_in), R.drawable.search_profile_8);
//
//
//        agentBtn.setOnClickListener(gotoDetailedListener);
//        eventBtn.setOnClickListener(gotoDetailedListener);
//        otherBtn.setVisibility(View.INVISIBLE);
//        row3.addView(agentBtn);
//        row3.addView(eventBtn);
//        row3.addView(otherBtn);
//
//        agentBtn.setTag("7");
//        eventBtn.setTag("8");
//
//
//
//
//
//
//        initLayoutParams(row1);
//        initLayoutParams(row2);
//        initLayoutParams(row3);
//        shortcutLayout.addView(row1);
//        shortcutLayout.addView(row2);
//        shortcutLayout.addView(row3);
//    }
//
//    private void initLayoutParams(LinearLayout row){
//        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(getActualWidthOnThisDevice(600),getActualHeightOnThisDevice(200));
//        row.setLayoutParams(rowParams);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        if (footerMes != null) {
//            footerMes.setText(Constants.STR_EMPTY);
//            addBtn.setVisibility(View.GONE);
//        }
//
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }
//
//    private void addFooterView() {
//        LinearLayout llFooterView = new LinearLayout(getBaseActivity());
//        llFooterView.setOrientation(LinearLayout.VERTICAL);
//        LinearLayout.LayoutParams btnParams
//                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        LinearLayout.LayoutParams footerMesParams
//                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//        footerMes = new IteeTextView(getBaseActivity());
//        footerMes.setLayoutParams(footerMesParams);
//        footerMes.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
//        footerMes.setVisibility(View.GONE);
//        footerMes.setSingleLine(false);
//
//        addBtn = new Button(getBaseActivity());
//        addBtn.setTextColor(Color.WHITE);
//        addBtn.setLayoutParams(btnParams);
//        addBtn.setText(R.string.newteetimes_btn_add_a_profile);
//        addBtn.setBackground(getResources().getDrawable(R.drawable.bg_green_btn));
//        addBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Utils.hideKeyboard(getBaseActivity());
//                boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEdit, getActivity())
//                        && AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEditNonmember, getActivity());
//                if (hasPermission) {
//                    Bundle bundle = new Bundle();
//                    bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeAdd.value());
//                    bundle.putString(TransKey.TEE_TIME_MEMBER_NAME, titleSearchView.getText().toString());
//                    push(PlayerInfoEditFragment.class, bundle);
//                } else {
//                    AppUtils.showHaveNoPermission(TeeTimeSearchProfileFragment.this);
//                }
//            }
//        });
//        addBtn.setVisibility(View.GONE);
//        llFooterView.addView(footerMes);
//
//        LinearLayout.LayoutParams vParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(20));
//        View v = new View(getBaseActivity());
//        v.setLayoutParams(vParams);
//        llFooterView.setPadding(40, 30, 40, 200);
//        llFooterView.addView(v);
//        llFooterView.addView(addBtn);
//        searchDataList.addFooterView(llFooterView);
//
//    }
//
//
//    private void initTitleView() {
//        ActionBar actionBar = getBaseActivity().getSupportActionBar();
//        LinearLayout.LayoutParams titleViewParams = new LinearLayout.LayoutParams(Utils.getWidth(getActivity()), getActionBarHeight());
//        LinearLayout titleView = new LinearLayout(getBaseActivity());
//        titleView.setBackgroundColor(getColor(R.color.common_light_gray));
//        titleView.setLayoutParams(titleViewParams);
//        titleView.setPadding(getActualWidthOnThisDevice(0), 0, 0, 0);
//        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(Utils.getWidth(getActivity()) - getActualWidthOnThisDevice(160), getActionBarHeight() - 20);
//        titleView.setGravity(Gravity.CENTER_VERTICAL);
//
//        titleSearchView = new IteeSearchView(getBaseActivity());
//        titleSearchView.setHint(getString(R.string.common_search));
//
//
//        titleSearchView.setTextSize(Constants.FONT_SIZE_NORMAL);
//        titleSearchView.setBackground(getResources().getDrawable(R.drawable.bg_search_view));
//        titleSearchView.setLayoutParams(llp);
//        titleSearchView.setIcon(getActionBarHeight() - 20, R.drawable.icon_search_del, R.drawable.icon_search_del, R.drawable.icon_search, R.drawable.icon_search);
//        titleSearchView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (KeyEvent.ACTION_UP == event.getAction()) {
//                    if (keyCode == 66) {
//                        Utils.hideKeyboard(getBaseActivity());
//                        //api
//                        getCustomerBookingSearch(titleSearchView.getText().toString());
//                    }
//                }
//
//                return false;
//            }
//        });
//
//        titleSearchView.setRightIconListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                titleSearchView.setText(StringUtils.EMPTY);
//            }
//        });
//        titleSearchView.setLeftIconListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Utils.hideKeyboard(getBaseActivity());
//                getCustomerBookingSearch(titleSearchView.getText().toString());
//            }
//        });
//
//        titleSearchView.requestFocus();
//        Utils.showKeyboard(titleSearchView, getActivity());
//
//        LinearLayout.LayoutParams titleSearchViewLayoutParams
//                = (LinearLayout.LayoutParams) titleSearchView.getLayoutParams();
//        titleSearchViewLayoutParams.leftMargin = getActualWidthOnThisDevice(10);
//        titleSearchView.setLayoutParams(titleSearchViewLayoutParams);
//        titleView.addView(titleSearchView);
//
//        Button cancelBtn = new Button(getBaseActivity());
//        cancelBtn.setText(getString(R.string.common_cancel));
//        cancelBtn.setBackgroundColor(Color.TRANSPARENT);
//        cancelBtn.setTextColor(getColor(R.color.common_blue));
//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                doBack();
//            }
//        });
//        cancelBtn.setTextSize(Constants.FONT_SIZE_NORMAL);
//
//        titleView.addView(cancelBtn);
//        actionBar.setCustomView(titleView);
//
//
//    }
//    private List<JsonTeeTimeBookingSearch.BookingItem> dataList;
//
//    private void getCustomerBookingSearch(String name) {
//        if (titleSearchView.getText().toString().length() > 0) {
//
//            Map<String, String> params = new HashMap<>();
//            params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
//           // params.put(ApiKey.TEE_TIME_MEMBER_TYPE_ID, Constants.STR_EMPTY);
//            params.put(ApiKey.TEE_TIME_KEY_WORD, name);
//            params.put(ApiKey.TEE_TIME_BOOKING_DATE, selectDate);
//
//
//            HttpManager<JsonTeeTimeBookingSearch> hh = new HttpManager<JsonTeeTimeBookingSearch>(TeeTimeSearchProfileFragment.this) {
//
//                @Override
//                public void onJsonSuccess(JsonTeeTimeBookingSearch jo) {
//
//                    Integer returnCode = jo.getReturnCode();
//                    String msg = jo.getReturnInfo();
//                    if (returnCode == Constants.RETURN_CODE_20301) {
//                        dataList.clear();
//                        dataList = jo.getDataList();
//                        shortcutLayout.setVisibility(View.GONE);
//                        upLine.setVisibility(View.GONE);
//                        upTextView.setVisibility(View.GONE);
//
//                        if (dataList.size() <= 0) {
//                            footerMes.setText(StringUtils.EMPTY);
//                            SpannableString ss = new SpannableString(titleSearchView.getText());
//                            ss.setSpan(new ForegroundColorSpan(getColor(R.color.common_blue)), 0, ss.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                            ss.setSpan(new RelativeSizeSpan(1.3f), 0, ss.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//
//                            footerMes.setVisibility(View.VISIBLE);
//
//                            footerMes.append(getString(R.string.add_tee_time_setting_search_mes1));
//                            footerMes.append(ss);
//                            footerMes.append(getString(R.string.add_tee_time_setting_search_mes2));
//
//                            addBtn.setVisibility(View.VISIBLE);
//                        } else {
//                            footerMes.setVisibility(View.GONE);
//                        }
//
//                        listViewAdapter.notifyDataSetChanged();
//                    } else {
//                        Utils.showShortToast(getActivity(), msg);
//                    }
//                }
//
//                @Override
//                public void onJsonError(VolleyError error) {
//
//                }
//            };
//            hh.start(getActivity(), ApiManager.HttpApi.BookingSearch, params);
//        }
//    }
//
//    class ListViewAdapter extends BaseAdapter {
//
//
//        @Override
//        public int getCount() {
//            return dataList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            ListViewItem item = new ListViewItem(getBaseActivity());
//            item.setItemValue(dataList.get(position));
////            item.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    Utils.hideKeyboard(getBaseActivity());
////                    Bundle bundle = new Bundle();
////                    bundle.putInt(TransKey.COMMON_MEMBER_ID, dataList.get(position).getMemberId());
////                    bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeBrowse.value());
////                    push(PlayerFragment.class, bundle);
////                }
////            });
//
//            item.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
//            return item;
//        }
//    }
//
//    //itee 11 dev
//    class ListViewItem extends LinearLayout {
//        private LinearLayout upLayout;
//        private IteeTextView tvBookingNo;
//        private IteeTextView tvPlayer;
//        private IteeTextView tvPhone;
//
//        private LinearLayout downLayout;
//
//
//        private  RelativeLayout playerLayout;
//        @SuppressLint("WrongViewCast")
//        public ListViewItem(Context context) {
//            super(context);
//            setOrientation(VERTICAL);
//
//            upLayout = new LinearLayout(getContext());
//            upLayout.setOrientation(VERTICAL);
//            LinearLayout.LayoutParams tvBookingNoParams = new  LinearLayout.LayoutParams( LayoutParams.WRAP_CONTENT,getActualWidthOnThisDevice(60));
//            tvBookingNo = new IteeTextView(getBaseActivity());
//            tvBookingNo.setLayoutParams(tvBookingNoParams);
//            tvBookingNo.setPadding(getActualWidthOnThisDevice(20), 0, 0, 0);
//            tvBookingNo.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
//            LinearLayout.LayoutParams playerLayoutParams = new  LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT,getActualWidthOnThisDevice(60));
//            playerLayout = new RelativeLayout(getBaseActivity());
//            playerLayout.setLayoutParams(playerLayoutParams);
//
//            tvPlayer = new IteeTextView(getBaseActivity());
//            tvPhone = new IteeTextView(getBaseActivity());
//
//
//            playerLayout.addView(tvPlayer);
//            playerLayout.addView(tvPhone);
//            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvPlayer, 20, getBaseActivity());
//            LayoutUtils.setCellRightValueViewOfRelativeLayout(tvPhone,20,getBaseActivity());
//            upLayout.addView(tvBookingNo);
//
//            upLayout.addView(playerLayout);
//            downLayout = new LinearLayout(getContext());
//            downLayout.setOrientation(VERTICAL);
//            LinearLayout.LayoutParams rlLineParams = new  LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
//            this.addView(upLayout);
//            RelativeLayout rlLine = new RelativeLayout(getActivity());
//            rlLine.setPadding(getActualWidthOnThisDevice(15),0,getActualWidthOnThisDevice(15),0);
//            rlLine.setLayoutParams(rlLineParams);
//            rlLine.addView(AppUtils.getSeparatorLine(getBaseActivity()));
//            this.addView(rlLine);
//            this.addView(downLayout);
//
//            this.addView(AppUtils.getSeparatorLine(getBaseActivity(),getActualHeightOnThisDevice(1)));
//        }
//
//
//
//        public void setItemValue(JsonTeeTimeBookingSearch.BookingItem data) {
//
//            tvBookingNo.setText(data.getCode());
//            tvPlayer.setText(data.getName());
//            tvPhone.setText(data.getTel());
//
//
//            playerLayout.setVisibility(View.GONE);
//
//            downLayout.removeAllViews();
//
//
//
//            for(JsonTeeTimeBookingSearch.BookingItemDetailed itemDetailed:data.getItemList()){
//               View  childView = LayoutInflater.from(getActivity()).inflate(R.layout.item_of_location_show, null);
//                RelativeLayout rlRowContainer = (RelativeLayout) childView.findViewById(R.id.rl_row_container);
//               // IteeTextView tvStatus = (IteeTextView) childView.findViewById(R.id.tv_status);
//                IteeTextView tvUserName = (IteeTextView) childView.findViewById(R.id.tv_user_name);
//                IteeTextView tvCheckOut = (IteeTextView) childView.findViewById(R.id.tv_check_out);
//                IteeTextView tvShoppingCart = (IteeTextView) childView.findViewById(R.id.tv_shopping_cart);
//
//                LinearLayout llCommodity = (LinearLayout) childView.findViewById(R.id.ll_commodity);
//                ImageView ivCommodityBoy = (ImageView) childView.findViewById(R.id.iv_commodity_boy);
//                ImageView ivCommodityCar = (ImageView) childView.findViewById(R.id.iv_commodity_car);
//                ImageView ivCommodityClub = (ImageView) childView.findViewById(R.id.iv_commodity_club);
//                ImageView ivCommodityShoes = (ImageView) childView.findViewById(R.id.iv_commodity_shoes);
//                ImageView ivCommodityBag = (ImageView) childView.findViewById(R.id.iv_commodity_bag);
//                ImageView ivCommodityTowel = (ImageView) childView.findViewById(R.id.iv_commodity_towel);
//                ImageView ivCommodityUmbrella = (ImageView) childView.findViewById(R.id.iv_commodity_umbrella);
//                IteeTextView tvSex = (IteeTextView) childView.findViewById(R.id.tv_sex);
//                IteeTextView tvField = (IteeTextView) childView.findViewById(R.id.tv_field);
//
//                tvField.setTextSize(Constants.FONT_SIZE_SMALLER);
//                tvField.setGravity(Gravity.CENTER);
//
//                RelativeLayout.LayoutParams tvUserNameLayoutParams
//                        = (RelativeLayout.LayoutParams) tvUserName.getLayoutParams();
//                tvUserNameLayoutParams.width = getActualWidthOnThisDevice(200);
//                tvUserNameLayoutParams.height = getActualHeightOnThisDevice(70);
//                tvUserName.setLayoutParams(tvUserNameLayoutParams);
//
//                tvCheckOut.setGravity(Gravity.CENTER);
//                tvShoppingCart.setGravity(Gravity.CENTER);
//
//                tvCheckOut.setTextSize(Constants.FONT_SIZE_SMALLER);
//                tvShoppingCart.setTextSize(Constants.FONT_SIZE_SMALLER);
//
//                tvUserName.setGravity(Gravity.CENTER_VERTICAL);
//
//
//
//
//
//                tvUserName.setText(itemDetailed.getName());
//                tvSex.setText(itemDetailed.getAppointmentMemberype());
//                if (Constants.STR_1.equals(itemDetailed.getMemberGender())) {
//                    tvSex.setTextColor(getColor(R.color.common_blue));
//                } else   if (Constants.STR_2.equals(itemDetailed.getMemberGender())) {
//                    tvSex.setTextColor(getColor(R.color.common_red));
//                } else {
//                    tvSex.setTextColor(getColor(R.color.common_black));
//                }
//
////                if (BOOKING_COLOR_RED.equals(itemDetailed.getBookingColor()) ) {
////                    tvStatus.setBackgroundColor(getColor(R.color.common_red));
////                }
////
////                if (BOOKING_COLOR_BLUE.equals(itemDetailed.getBookingColor())) {
////                    tvStatus.setBackgroundColor(getColor(R.color.common_blue));
////                }
//
//             //  final PositionInformationEntity finalPositionInformation = positionInformation;
////                tvShoppingCart.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
////
////                    @Override
////                    public void noDoubleClick(View v) {
////                        if (Constants.STR_FLAG_YES.equals(finalPositionInformation.getSameDayFlag())) {
////                            boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlSelling, getActivity());
////                            if (hasPermission) {
////                                if (!finalPositionInformation.isCheckOutStatus()) {
////                                    Bundle bundle = new Bundle();
////                                    bundle.putString(TransKey.SHOPPING_BOOKING_NO, finalPositionInformation.getBookingNo());
////                                    bundle.putString(TransKey.COMMON_FROM_PAGE, LocationListFragment.class.getName());
////                                    bundle.putInt(TransKey.SHOPPING_PURCHASE_FLAG, ShoppingPaymentFragment.PURCHASE_FLAG_SHOPPING);
////                                    push(ShoppingGoodsListFragment.class, bundle);
////                                }
////                            } else {
////                                AppUtils.showHaveNoPermission(getActivity());
////                            }
////                        } else {
////                            Utils.showShortToast(getActivity(), R.string.msg_error_this_time_has_past);
////                        }
////                    }
////                });
//
//                //final ViewHolder final=
////                tvCheckOut.setOnClickListener(new AppUtils.NoDoubleClickListener(fragment.getActivity()) {
////
////                    @Override
////                    public void noDoubleClick(View v) {
////
////                        boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlCheckOut, fragment.getActivity());
////                        if (hasPermission) {
////                            if (!finalPositionInformation.isCheckOutStatus()) {
////                                getCheckOut(finalPositionInformation, final;
////                            }
////                        } else {
////                            AppUtils.showHaveNoPermission(fragment);
////                        }
////                    }
////                });
//
//                llCommodity.setVisibility(View.GONE);
//                //TOD
////                positionInformation.setPayStatus(2);
////                positionInformation.setCheckInStatus(0);
////                positionInformation.setAppointmentGoods("1,2");
//
//                if ( Constants.STR_1.equals(itemDetailed.getPayStatus())) {// 已结账（已经打完球了）
////            黑色圆圈F;
//                    tvField.setText(STATUS_F);
//                    tvField.setTextColor(getColor(R.color.common_black));
//                    tvField.setBackgroundResource(R.drawable.bg_black_ring);
//                    tvCheckOut.setEnabled(false);
//                    tvShoppingCart.setEnabled(false);
//                    tvCheckOut.setBackgroundColor(getColor(R.color.common_light_gray));
//                    tvShoppingCart.setBackgroundResource(R.drawable.bg_gray_stroke);
//                    tvCheckOut.setBackgroundResource(R.drawable.bg_gray_stroke);
//                } else {// 未结账
//                    if (itemDetailed.getCurrentHole() != null && !Constants.STR_0.equals(itemDetailed.getCurrentHole())) {//正在打球
//                        if (Constants.STR_0.equals(itemDetailed.getCurrentHoleStatus())) {
////                    黑色圆圈洞号；
//                            tvField.setText(itemDetailed.getCurrentHole());
//                            tvField.setTextColor(getColor(R.color.common_black));
//                            tvField.setBackgroundResource(R.drawable.bg_black_ring);
//                        } else if ( Constants.STR_1.equals(itemDetailed.getCurrentHoleStatus())) {
////                    红色圆圈洞号；
//                            tvField.setText(String.valueOf(itemDetailed.getCurrentHole()));
//                            tvField.setTextColor(getColor(R.color.common_white));
//                            tvField.setBackgroundResource(R.drawable.bg_red_circle_location);
//                        } else if (Constants.STR_2.equals(itemDetailed.getCurrentHoleStatus())) {
////                    黄色圆圈洞号；
//                            tvField.setText(String.valueOf(itemDetailed.getCurrentHole()));
//                            tvField.setTextColor(getColor(R.color.common_black));
//                            tvField.setBackgroundResource(R.drawable.bg_yellow_circle_location);
//                        }
//                    } else {// 没在打球
//                        if (Constants.STR_0.equals(itemDetailed.getCheckInStatus())) {// 没开卡
//                            //setCanSlide("can");
//                            if (itemDetailed.getAppointmentGoods().length() > 0) {// 有预约商品
//                                tvField.setVisibility(View.GONE);
//                                llCommodity.setVisibility(View.VISIBLE);
//                                ivCommodityBoy.setVisibility(View.GONE);
//                                ivCommodityCar.setVisibility(View.GONE);
//                                ivCommodityClub.setVisibility(View.GONE);
//                                ivCommodityShoes.setVisibility(View.GONE);
//                                ivCommodityBag.setVisibility(View.GONE);
//                                ivCommodityTowel.setVisibility(View.GONE);
//                                ivCommodityUmbrella.setVisibility(View.GONE);
//                                if (itemDetailed.getAppointmentGoods().contains("1")) {
//                                    ivCommodityBoy.setVisibility(View.VISIBLE);
//                                }
//                                if (itemDetailed.getAppointmentGoods().contains("2")) {
//                                    ivCommodityCar.setVisibility(View.VISIBLE);
//                                }
//                                if (itemDetailed.getAppointmentGoods().contains("3")) {
//                                    ivCommodityClub.setVisibility(View.VISIBLE);
//                                }
//                                if (itemDetailed.getAppointmentGoods().contains("4")) {
//                                    ivCommodityShoes.setVisibility(View.VISIBLE);
//                                }
//                                if (itemDetailed.getAppointmentGoods().contains("5")) {
//                                    ivCommodityBag.setVisibility(View.VISIBLE);
//                                }
//                                if (itemDetailed.getAppointmentGoods().contains("6")) {
//                                    ivCommodityUmbrella.setVisibility(View.VISIBLE);
//                                }
//                                if (itemDetailed.getAppointmentGoods().contains("7")) {
//                                    ivCommodityTowel.setVisibility(View.VISIBLE);
//                                }
//                                if (Constants.STR_0.equals(itemDetailed.getAppointmentGoodsStatus())) {
////                            蓝色商品；
//                                    llCommodity.setBackgroundResource(R.drawable.bg_light_blue_location);
//                                } else {
////                            绿色商品；
//                                    llCommodity.setBackgroundResource(R.drawable.bg_green_location);
//                                }
//                            } else {// 没预约商品
//                                if (Constants.STR_0.equals(itemDetailed.getDepositStatus())) {
//////                            蓝色圆圈R；
//
//                                    tvField.setText(STATUS_R);
//                                    tvField.setTextColor(getColor(R.color.common_blue));
//                                    tvField.setBackgroundResource(R.drawable.bg_blue_ring);
//                                } else {
////                            绿色圆圈R；
//                                    tvField.setText(STATUS_R);
//                                    tvField.setTextColor(getColor(R.color.bg_green_of_2));
//                                    tvField.setBackgroundResource(R.drawable.bg_green_ring);
//                                }
//                            }
//                        } else {// 已开卡
////                    黑色对钩；
//                            tvField.setText(StringUtils.EMPTY);
//                            tvField.setBackgroundResource(R.drawable.bg_right_ring);
//                        }
//                    }
//
//
////                    if (AppUtils.isAgent(getActivity()) && !Constants.STR_1.equals(positionInformation.getSelfFlag())) {
////                        tvStatus.setEnabled(false);
////                        tvUserName.setEnabled(false);
////                        llCommodity.setEnabled(false);
////                        ivCommodityBoy.setEnabled(false);
////                        ivCommodityCar.setEnabled(false);
////                        ivCommodityClub.setEnabled(false);
////                        ivCommodityShoes.setEnabled(false);
////                        ivCommodityBag.setEnabled(false);
////                        ivCommodityTowel.setEnabled(false);
////                        ivCommodityUmbrella.setEnabled(false);
////                        tvSex.setEnabled(false);
////                        tvField.setEnabled(false);
////                        tvCheckOut.setEnabled(false);
////                        tvShoppingCart.setEnabled(false);
////                        setCanSlide("can");
////                    }
//                }
////
////                final LinearLayout commodity = llCommodity;
////                final int p = position;
////                commodity.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////                        commodity.setTag(positionInformationList.get(p).getBookingNo());
////                        listener.onClick(commodity);
//
//
//                downLayout.addView(childView);
//            }
//
//        }
//    }
//
//
//    class ClickItem extends LinearLayout{
//
//        private ImageView upView;
//        private IteeTextView tvText;
//        public void setLayout(String text,int back){
//
//            tvText.setText(text);
//            upView.setBackgroundResource(back);
//        }
//
//
//        public ClickItem(Context context) {
//            super(context);
//
//            setOrientation(VERTICAL);
//            setGravity(Gravity.CENTER);
//
//            LinearLayout.LayoutParams upViewParams = new   LinearLayout.LayoutParams(getActualHeightOnThisDevice(100) ,getActualHeightOnThisDevice(100));
//            upView = new ImageView(context);
//            upView.setLayoutParams(upViewParams);
//            tvText = new IteeTextView(context);
//            tvText.setTextColor(getColor(R.color.common_gray));
//            tvText.setGravity(Gravity.CENTER);
//
//            this.addView(upView);
//            this.addView(tvText);
//        }
//    }
public ArrayList<JsonCustomerBookingSearch.Member> dataList;
    private IteeSearchView titleSearchView;
    private ListView searchDataList;
    private ListViewAdapter listViewAdapter;
    private IteeTextView footerMes;
    private Button addBtn;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_tee_time_search;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        dataList = new ArrayList<>();
        searchDataList = (ListView) rootView.findViewById(R.id.searchDataList);

        addFooterView();
        searchDataList.setDividerHeight(0);

        listViewAdapter = new ListViewAdapter();
        searchDataList.setAdapter(listViewAdapter);
    }

    @Override
    protected void setDefaultValueOfControls() {

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
        initTitleView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (footerMes != null) {
            footerMes.setText(Constants.STR_EMPTY);
            addBtn.setVisibility(View.GONE);
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void addFooterView() {
        LinearLayout llFooterView = new LinearLayout(getBaseActivity());
        llFooterView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams btnParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams footerMesParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        footerMes = new IteeTextView(getBaseActivity());
        footerMes.setLayoutParams(footerMesParams);
        footerMes.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        footerMes.setVisibility(View.GONE);
        footerMes.setSingleLine(false);

        addBtn = new Button(getBaseActivity());
        addBtn.setTextColor(Color.WHITE);
        addBtn.setLayoutParams(btnParams);
        addBtn.setText(R.string.newteetimes_btn_add_a_profile);
        addBtn.setBackground(getResources().getDrawable(R.drawable.bg_green_btn));
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getBaseActivity());
                boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEdit, getActivity())
                        && AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEditNonmember, getActivity());
                if (hasPermission) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeAdd.value());
                    bundle.putString(TransKey.TEE_TIME_MEMBER_NAME, titleSearchView.getText().toString());
                    push(PlayerInfoEditFragment.class, bundle);
                } else {
                    AppUtils.showHaveNoPermission(TeeTimeSearchProfileFragment.this);
                }
            }
        });
        addBtn.setVisibility(View.GONE);
        llFooterView.addView(footerMes);

        LinearLayout.LayoutParams vParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(20));
        View v = new View(getBaseActivity());
        v.setLayoutParams(vParams);
        llFooterView.setPadding(40, 30, 40, 200);
        llFooterView.addView(v);
        llFooterView.addView(addBtn);
        searchDataList.addFooterView(llFooterView);

    }


    private void initTitleView() {
        ActionBar actionBar = getBaseActivity().getSupportActionBar();
        LinearLayout.LayoutParams titleViewParams = new LinearLayout.LayoutParams(Utils.getWidth(getActivity()), getActionBarHeight());
        LinearLayout titleView = new LinearLayout(getBaseActivity());
        titleView.setBackgroundColor(getColor(R.color.common_light_gray));
        titleView.setLayoutParams(titleViewParams);
        titleView.setPadding(getActualWidthOnThisDevice(0), 0, 0, 0);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(Utils.getWidth(getActivity()) - getActualWidthOnThisDevice(160), getActionBarHeight() - 20);
        titleView.setGravity(Gravity.CENTER_VERTICAL);

        titleSearchView = new IteeSearchView(getBaseActivity());
        titleSearchView.setHint(getString(R.string.common_search));


        titleSearchView.setTextSize(Constants.FONT_SIZE_NORMAL);
        titleSearchView.setBackground(getResources().getDrawable(R.drawable.bg_search_view));
        titleSearchView.setLayoutParams(llp);
        titleSearchView.setIcon(getActionBarHeight() - getActualHeightOnThisDevice(35), R.drawable.icon_search_del, R.drawable.icon_search_del, R.drawable.icon_search, R.drawable.icon_search);
        titleSearchView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_UP == event.getAction()) {
                    if (keyCode == 66) {
                        Utils.hideKeyboard(getBaseActivity());
                        //api
                        getCustomerBookingSearch(titleSearchView.getText().toString());
                    }
                }

                return false;
            }
        });

        titleSearchView.setRightIconListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleSearchView.setText(StringUtils.EMPTY);
            }
        });
        titleSearchView.setLeftIconListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getBaseActivity());
                getCustomerBookingSearch(titleSearchView.getText().toString());
            }
        });

        titleSearchView.requestFocus();
        Utils.showKeyboard(titleSearchView, getActivity());

        LinearLayout.LayoutParams titleSearchViewLayoutParams
                = (LinearLayout.LayoutParams) titleSearchView.getLayoutParams();
        titleSearchViewLayoutParams.leftMargin = getActualWidthOnThisDevice(10);
        titleSearchView.setLayoutParams(titleSearchViewLayoutParams);
        titleView.addView(titleSearchView);

        Button cancelBtn = new Button(getBaseActivity());
        cancelBtn.setText(getString(R.string.common_cancel));
        cancelBtn.setBackgroundColor(Color.TRANSPARENT);
        cancelBtn.setTextColor(getColor(R.color.common_blue));
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doBack();
            }
        });
        cancelBtn.setTextSize(Constants.FONT_SIZE_NORMAL);

        titleView.addView(cancelBtn);
        actionBar.setCustomView(titleView);


    }


    private void getCustomerBookingSearch(String name) {
        if (titleSearchView.getText().toString().length() > 0) {
            addBtn.setVisibility(View.VISIBLE);
            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
            params.put(ApiKey.TEE_TIME_MEMBER_TYPE_ID, Constants.STR_EMPTY);
            params.put(ApiKey.TEE_TIME_CUSTOMER_NAME, name);

            HttpManager<JsonCustomerBookingSearch> hh = new HttpManager<JsonCustomerBookingSearch>(TeeTimeSearchProfileFragment.this) {

                @Override
                public void onJsonSuccess(JsonCustomerBookingSearch jo) {


                    Integer returnCode = jo.getReturnCode();

                    String msg = jo.getReturnInfo();
                    if (returnCode == Constants.RETURN_CODE_20301) {
                        dataList.clear();
                        dataList = jo.getDataList();
                        listViewAdapter.notifyDataSetChanged();

                        if (dataList.size() <= 0) {
                            footerMes.setText(StringUtils.EMPTY);
                            SpannableString ss = new SpannableString(titleSearchView.getText());
                            ss.setSpan(new ForegroundColorSpan(getColor(R.color.common_blue)), 0, ss.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            ss.setSpan(new RelativeSizeSpan(1.3f), 0, ss.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                            footerMes.setVisibility(View.VISIBLE);

                            footerMes.append(getString(R.string.add_tee_time_setting_search_mes1));
                            footerMes.append(ss);
                            footerMes.append(getString(R.string.add_tee_time_setting_search_mes2));
                        } else {
                            footerMes.setVisibility(View.GONE);
                        }
                    } else {
                        Utils.showShortToast(getActivity(), msg);
                    }
                }

                @Override
                public void onJsonError(VolleyError error) {

                }
            };
            hh.start(getActivity(), ApiManager.HttpApi.CsbookingSearch, params);
        }
    }

    class ListViewAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ListViewItem item = new ListViewItem(getBaseActivity());
            item.setItemValue(dataList.get(position));
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.hideKeyboard(getBaseActivity());
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.COMMON_MEMBER_ID, dataList.get(position).getMemberId());
                    bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeBrowse.value());
                    push(PlayerFragment.class, bundle);
                }
            });
            return item;
        }
    }

    class ListViewItem extends RelativeLayout {

        private NetworkImageView iconPhoto;

        private TextView tvTelValue;
        private TextView tvMemberNo;
        private TextView tvMemberName;
        private TextView tvBirthValue;
        private TextView tvZipCode;

        @SuppressLint("WrongViewCast")
        public ListViewItem(Context context) {
            super(context);

            View convertView = LayoutInflater.from(context).inflate(R.layout.list_row_newteetimes_member_name, null, false);
            tvTelValue = (TextView) convertView.findViewById(R.id.tv_tel_value);
            tvMemberNo = (TextView) convertView.findViewById(R.id.tv_member_no);
            tvMemberName = (TextView) convertView.findViewById(R.id.tv_member_name);
            tvBirthValue = (TextView) convertView.findViewById(R.id.tv_birth_value);
            tvZipCode = (TextView) convertView.findViewById(R.id.tv_zip_code);
            iconPhoto = (NetworkImageView) convertView.findViewById(R.id.iv_icon);
            tvMemberNo.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);

            iconPhoto.setPadding(5, 5, 5, 5);

            tvBirthValue.setTextSize(Constants.FONT_SIZE_SMALLER);
            tvTelValue.setTextSize(Constants.FONT_SIZE_SMALLER);
            this.addView(convertView);
        }


        public void setItemValue(JsonCustomerBookingSearch.Member data) {
            tvMemberNo.setText(data.getMemberNo());
            tvMemberName.setText(data.getMemberName());
            tvBirthValue.setText(data.getMemberBirth());
            tvTelValue.setText(data.getMemberTel());
            tvZipCode.setText(data.getZipCode());
            if (StringUtils.isNotEmpty(data.getMemberPic())) {
                AppUtils.showNetworkImage(iconPhoto, data.getMemberPic());
            } else {
                iconPhoto.setBackgroundResource(R.drawable.member_photo);
            }
        }
    }
}