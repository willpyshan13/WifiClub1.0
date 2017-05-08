package cn.situne.itee.fragment.teetime;

import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

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
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.player.PlayerPurchaseHistoryDetailFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonBookingDetailListGet;
import cn.situne.itee.manager.jsonentity.JsonPurchaseHistoryGet;
import cn.situne.itee.manager.jsonentity.JsonTeeTimeBookingSearch;
import cn.situne.itee.view.IteeTextView;

/**
 * Created by luochao on 12/1/15.
 */
public class TeeTimeSearchProfileListFragment  extends BaseFragment

{

    private PullToRefreshListView listView;
    private ListViewAdapter listViewAdapter;
    private RelativeLayout rlNotMember;
    private String  selectDate;
    private String type;
    private boolean isRefresh;

    private int currentPage;

    private List<JsonBookingDetailListGet.BookingDetailItem> dataList;
    @Override
    protected int getFragmentId() {
        return R.layout.fragment_search_profile_list;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        dataList = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {

            selectDate = bundle.getString("selectDate",Constants.STR_EMPTY);
            type = bundle.getString("type",Constants.STR_EMPTY);
        }
        rlNotMember = (RelativeLayout) rootView.findViewById(R.id.rl_not_member);
        listView = (PullToRefreshListView)rootView.findViewById(R.id.listView);


        listViewAdapter = new ListViewAdapter();
        listView.setAdapter(listViewAdapter);
        listView.onRefreshComplete();
        getCustomerBookingSearch();
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = true;
                getCustomerBookingSearch();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = false;
                getCustomerBookingSearch();
            }
        });
    }

    @Override
    protected void setLayoutOfControls() {
        RelativeLayout.LayoutParams paramsYards = (RelativeLayout.LayoutParams) rlNotMember.getLayoutParams();
        rlNotMember.setLayoutParams(paramsYards);

        RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        iconParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        ImageView icon = new ImageView(getBaseActivity());
        icon.setBackgroundResource(R.drawable.icon_not_data);
        icon.setLayoutParams(iconParams);
        rlNotMember.addView(icon);
    }

    @Override
    protected void setPropertyOfControls() {
        listView.setMode(PullToRefreshBase.Mode.BOTH);

        ILoadingLayout headerLayoutProxy = listView.getLoadingLayoutProxy(true, false);
        ILoadingLayout footerLayoutProxy = listView.getLoadingLayoutProxy(false, true);

        headerLayoutProxy.setPullLabel(getString(R.string.app_list_header_refresh_down));
        headerLayoutProxy.setRefreshingLabel(getString(R.string.app_list_header_refresh_down));
        headerLayoutProxy.setReleaseLabel(getString(R.string.app_list_header_refresh_down));

        footerLayoutProxy.setPullLabel(getString(R.string.app_list_header_refresh_loading_more));
        footerLayoutProxy.setRefreshingLabel(getString(R.string.app_list_header_refresh_loading_more));
        footerLayoutProxy.setReleaseLabel(getString(R.string.app_list_header_refresh_loading_more));
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();


        switch (type){
            case "1":
                getTvLeftTitle().setText(getString(R.string.search_booking));
                break;
            case "2":
                getTvLeftTitle().setText(getString(R.string.search_on_course));
                break;
            case "3":
                getTvLeftTitle().setText(getString(R.string.search_check_out));
                break;
            case "4":
                getTvLeftTitle().setText(getString(R.string.search_member));
                break;
            case "5":
                getTvLeftTitle().setText(getString(R.string.search_guest));
                break;
            case "6":
                getTvLeftTitle().setText(getString(R.string.search_walk_in));
                break;
            case "7":
                getTvLeftTitle().setText(getString(R.string.search_agent));
                break;
            case "8":
                getTvLeftTitle().setText(getString(R.string.search_event));
                break;

        }
    }


    class ListViewItem extends RelativeLayout{

        private IteeTextView tvTime;
        private IteeTextView tvName;
        private IteeTextView tvBookingNo;
        private IteeTextView tvCardNo;
        private ImageView icon;
        private IteeTextView tvMemberType;

        private JsonBookingDetailListGet.BookingDetailItem mData;

        public  JsonBookingDetailListGet.BookingDetailItem getData(){

            return mData;
        }


        public ListViewItem(Context context) {
            super(context);

            ListView.LayoutParams myParams = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(110));
            this.setLayoutParams(myParams);
            tvTime = new IteeTextView(context);
            tvName = new IteeTextView(context);
            tvBookingNo = new IteeTextView(context);
            tvCardNo = new IteeTextView(context);
            tvTime.setId(View.generateViewId());
            tvName.setId(View.generateViewId());
            tvBookingNo.setId(View.generateViewId());
            tvCardNo.setId(View.generateViewId());
            RelativeLayout.LayoutParams tvMemberTypeParams = new  RelativeLayout.LayoutParams(getActualWidthOnThisDevice(60),getActualWidthOnThisDevice(60));
            tvMemberType= new IteeTextView(context);
            tvMemberType.setGravity(Gravity.CENTER);
            tvMemberType.setLayoutParams(tvMemberTypeParams);
            tvMemberTypeParams.rightMargin = getActualWidthOnThisDevice(20);
            tvMemberTypeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            tvMemberTypeParams.addRule(RelativeLayout.CENTER_VERTICAL);
            this.addView(tvTime);
            this.addView(tvName);
            this.addView(tvBookingNo);
            this.addView(tvCardNo);

            this.addView(tvMemberType);
            RelativeLayout.LayoutParams tvTimeParams = new  RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,getActualWidthOnThisDevice(60));
            tvTimeParams.leftMargin = getActualWidthOnThisDevice(20);
            RelativeLayout.LayoutParams tvNameParams = new  RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,getActualWidthOnThisDevice(60));
            tvNameParams.leftMargin = getActualWidthOnThisDevice(20);
            tvNameParams.addRule(RelativeLayout.RIGHT_OF,tvTime.getId());
            RelativeLayout.LayoutParams tvBookingNoParams = new  RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,getActualWidthOnThisDevice(50));
            tvBookingNoParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            RelativeLayout.LayoutParams tvCardNoParams = new  RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,getActualWidthOnThisDevice(50));
            tvCardNoParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            tvCardNoParams.addRule(RelativeLayout.RIGHT_OF,tvBookingNo.getId());

            tvCardNoParams.leftMargin = getActualWidthOnThisDevice(20);
            tvBookingNoParams.leftMargin = getActualWidthOnThisDevice(20);

            tvTime.setLayoutParams(tvTimeParams);
            tvTime.setTextSize(Constants.FONT_SIZE_LARGER);
            tvTime.setGravity(Gravity.BOTTOM);
            tvName.setLayoutParams(tvNameParams);
            tvName.setTextSize(Constants.FONT_SIZE_LARGER);
            tvName.setGravity(Gravity.BOTTOM);
            tvBookingNo.setLayoutParams(tvBookingNoParams);
            tvCardNo.setLayoutParams(tvCardNoParams);
            tvCardNo.setTextColor(getColor(R.color.common_gray));
            tvBookingNo.setTextColor(getColor(R.color.common_gray));
            AppUtils.addBottomSeparatorLine(this,context);

        }

        public void  refreshLayout(JsonBookingDetailListGet.BookingDetailItem data) {
            mData = data;
            tvTime.setText(data.getBkdTime());
            tvName.setText(data.getName());
            tvBookingNo.setText(data.getBookingNo());
            tvCardNo.setText(data.getCaddieNo());


            tvMemberType.setText(Constants.STR_EMPTY);
            tvMemberType.setText(data.getMemberType());
            if (Constants.STR_1.equals(data.getMemberGender())) {
                tvMemberType.setTextColor(getColor(R.color.common_blue));
            } else if (Constants.STR_2.equals(data.getMemberGender())) {
                tvMemberType.setTextColor(getColor(R.color.common_red));
            } else {
                tvMemberType.setTextColor(getColor(R.color.common_black));
            }


            int intType = Integer.parseInt(type);
            if (intType>3){
                tvMemberType.setText(Constants.STR_EMPTY);

                if (Constants.STR_1.equals(data.getPayStatus())) {// 已结账（已经打完球了）
//            黑色圆圈F;
                    tvMemberType.setText(STATUS_F);
                    tvMemberType.setTextColor(getColor(R.color.common_black));
                    tvMemberType.setBackgroundResource(R.drawable.bg_black_ring);
//                tvCheckOut.setEnabled(false);
//                tvShoppingCart.setEnabled(false);
//                tvCheckOut.setBackgroundColor(getColor(R.color.common_light_gray));
//                tvShoppingCart.setBackgroundResource(R.drawable.bg_gray_stroke);
//                tvCheckOut.setBackgroundResource(R.drawable.bg_gray_stroke);
                } else {// 未结账
                    if (data.getCurrentHole() != null && !Constants.STR_0.equals(data.getCurrentHole())) {//正在打球
                        if (Constants.STR_0.equals(data.getCurrentHoleStatus())) {
//                    黑色圆圈洞号；
                            tvMemberType.setText(data.getCurrentHole());
                            tvMemberType.setTextColor(getColor(R.color.common_black));
                            tvMemberType.setBackgroundResource(R.drawable.bg_black_ring);
                        } else if ( Constants.STR_1.equals(data.getCurrentHoleStatus())) {
//                    红色圆圈洞号；
                            tvMemberType.setText(String.valueOf(data.getCurrentHole()));
                            tvMemberType.setTextColor(getColor(R.color.common_white));
                            tvMemberType.setBackgroundResource(R.drawable.bg_red_circle_location);
                        } else if (Constants.STR_2.equals(data.getCurrentHoleStatus())) {
//                    黄色圆圈洞号；
                            tvMemberType.setText(String.valueOf(data.getCurrentHole()));
                            tvMemberType.setTextColor(getColor(R.color.common_black));
                            tvMemberType.setBackgroundResource(R.drawable.bg_yellow_circle_location);
                        }
                    } else {// 没在打球
                        if (Constants.STR_0.equals(data.getCheckInStatus())) {// 没开卡
                            //setCanSlide("can");
                            if (data.getAppointmentGoods().length() > 0) {// 有预约商品
//                            tvField.setVisibility(View.GONE);
//                            llCommodity.setVisibility(View.VISIBLE);
//                            ivCommodityBoy.setVisibility(View.GONE);
//                            ivCommodityCar.setVisibility(View.GONE);
//                            ivCommodityClub.setVisibility(View.GONE);
//                            ivCommodityShoes.setVisibility(View.GONE);
//                            ivCommodityBag.setVisibility(View.GONE);
//                            ivCommodityTowel.setVisibility(View.GONE);
//                            ivCommodityUmbrella.setVisibility(View.GONE);
//                            if (itemDetailed.getAppointmentGoods().contains("1")) {
//                                ivCommodityBoy.setVisibility(View.VISIBLE);
//                            }
//                            if (itemDetailed.getAppointmentGoods().contains("2")) {
//                                ivCommodityCar.setVisibility(View.VISIBLE);
//                            }
//                            if (itemDetailed.getAppointmentGoods().contains("3")) {
//                                ivCommodityClub.setVisibility(View.VISIBLE);
//                            }
//                            if (itemDetailed.getAppointmentGoods().contains("4")) {
//                                ivCommodityShoes.setVisibility(View.VISIBLE);
//                            }
//                            if (itemDetailed.getAppointmentGoods().contains("5")) {
//                                ivCommodityBag.setVisibility(View.VISIBLE);
//                            }
//                            if (itemDetailed.getAppointmentGoods().contains("6")) {
//                                ivCommodityUmbrella.setVisibility(View.VISIBLE);
//                            }
//                            if (itemDetailed.getAppointmentGoods().contains("7")) {
//                                ivCommodityTowel.setVisibility(View.VISIBLE);
//                            }
//                            if (Constants.STR_0.equals(itemDetailed.getAppointmentGoodsStatus())) {
////                            蓝色商品；
//                                llCommodity.setBackgroundResource(R.drawable.bg_light_blue_location);
//                            } else {
////                            绿色商品；
//                                llCommodity.setBackgroundResource(R.drawable.bg_green_location);
//                            }
                            } else {// 没预约商品
                                if (Constants.STR_0.equals(data.getDepositStatus())) {
////                            蓝色圆圈R；

                                    tvMemberType.setText(STATUS_R);
                                    tvMemberType.setTextColor(getColor(R.color.common_blue));
                                    tvMemberType.setBackgroundResource(R.drawable.bg_blue_ring);
                                } else {
//                            绿色圆圈R；
                                    tvMemberType.setText(STATUS_R);
                                    tvMemberType.setTextColor(getColor(R.color.bg_green_of_2));
                                    tvMemberType.setBackgroundResource(R.drawable.bg_green_ring);
                                }
                            }
                        } else {// 已开卡
//                    黑色对钩；
                            tvMemberType.setText(StringUtils.EMPTY);
                            tvMemberType.setBackgroundResource(R.drawable.bg_right_ring);
                        }
                    }




                }
            }


        }

    }
        private static final String STATUS_F = "F";
        private static final String STATUS_R = "R";
    class  ListViewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ListViewItem item  = null;

            if (view == null){

                item = new ListViewItem(getBaseActivity());
            }else{

                item = (ListViewItem)view;
            }
            item.refreshLayout(dataList.get(i));
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ListViewItem item = (ListViewItem) view;
                    if (Constants.STR_1.equals(item.getData().getPayStatus())) {
//                        Bundle bundle = new Bundle();
//
//                        bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeSearchProfileListFragment.class.getName());
//                        bundle.putString("bookingNo",item.getData().getBookingNo());
//                        bundle.putString("selectDate",selectDate);
//
//
//                        push(TeeTimeSearchProfileHistoryFragment.class,bundle);




                        Bundle bundle = new Bundle();



                        bundle.putString(ApiKey.PAY_ID, item.getData().getPayId());
                        bundle.putString(TransKey.COMMON_DATE_TIME, item.getData().getPayDate());
                        push(PlayerPurchaseHistoryDetailFragment.class, bundle);

                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString(TransKey.BOOKING_ORDER_NO, item.getData().getId());
                        bundle.putBoolean("isAdd", false);
                        bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeSearchProfileListFragment.class.getName());
                        push(TeeTimeAddFragment.class, bundle);
                    }

                }
            });
            return item;
        }
    }


    private void getCustomerBookingSearch() {
        if (isRefresh) {
            currentPage = 1;
        } else {
            currentPage++;
        }
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put("type", type);
        params.put("booking_date", selectDate);
        params.put(ApiKey.COMMON_PAGE, String.valueOf(currentPage));
        HttpManager<JsonBookingDetailListGet> hh = new HttpManager<JsonBookingDetailListGet>(TeeTimeSearchProfileListFragment.this) {

            @Override
            public void onJsonSuccess(JsonBookingDetailListGet jo) {

                Integer returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    if (currentPage == 1){
                        dataList = jo.getDataList();
                    }else{
                        dataList.addAll(jo.getDataList());
                    }


                    listViewAdapter.notifyDataSetChanged();
                    listView.onRefreshComplete();
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
                if(dataList.size() == 0){
                    rlNotMember.setVisibility(View.VISIBLE);
                }else{
                    rlNotMember.setVisibility(View.GONE);
                }
            }
            @Override
            public void onJsonError(VolleyError error) {

                currentPage--;
            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.BookingDetailList, params);

    }

}
