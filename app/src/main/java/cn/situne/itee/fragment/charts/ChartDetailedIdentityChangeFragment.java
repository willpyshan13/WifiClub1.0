package cn.situne.itee.fragment.charts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonCustomerDetail;
import cn.situne.itee.manager.jsonentity.JsonShoppingCustomerListGet;
import cn.situne.itee.manager.jsonentity.JsonShoppingPaymentGet;
import cn.situne.itee.manager.jsonentity.JsonShoppingPurchaseAddReturn;
import cn.situne.itee.view.IteeTextView;

/**
 * Created by luochao on 9/10/15.
 */
public class ChartDetailedIdentityChangeFragment extends BaseFragment {

    private final  static int ITEM_HEIGHT = 70;

    private PullToRefreshListView detailedDataList;

    private ListViewAdapter listViewAdapter;

    private String beginDate;
    private String endDate;

    private String selectedMethod;

    private int nowPage;

    private ArrayList<ListViewDataItem> dataList;

    @Override
    protected int getFragmentId() {
        return R.layout.chart_detailed_identity_change;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        dataList = new ArrayList<>();
        nowPage = 1;

        Bundle bundle = getArguments();
        if (bundle != null) {
            beginDate = bundle.getString(TransKey.SHOPPING_BEGIN_DATE);
            endDate = bundle.getString(TransKey.SHOPPING_END_DATE);
            selectedMethod = bundle.getString(TransKey.SHOPPING_SELECTED_METHOD);

        }


        RelativeLayout.LayoutParams detailedDataListParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        detailedDataList = new PullToRefreshListView(getBaseActivity());
        detailedDataList.setLayoutParams(detailedDataListParams);
//        detailedDataList.getRefreshableView().setDivider(new ColorDrawable(Color.GRAY));
        detailedDataList.getRefreshableView().setDividerHeight(0);
        detailedDataList.setMode(PullToRefreshBase.Mode.BOTH);


        detailedDataList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                nowPage = 1;
                getCustomerConsumeChangeDetail(nowPage);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getCustomerConsumeChangeDetail(nowPage);
            }
        });


        RelativeLayout  rlContainer  = (RelativeLayout)rootView.findViewById(R.id.rl_total_container);
        rlContainer.addView(detailedDataList);
        listViewAdapter = new ListViewAdapter();
        detailedDataList.setAdapter(listViewAdapter);
        detailedDataList.onRefreshComplete();


        getCustomerConsumeChangeDetail(nowPage);

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

        String s = getString(R.string.chart_detailed_ide_change_title)+Constants.STR_SPACE+Constants.STR_SPACE;

        String dateStr = beginDate;


        if (selectedMethod == Constants.CHART_SELECT_METHOD_YEAR){

            dateStr = dateStr.substring(0,4);
        }

        if (selectedMethod == Constants.CHART_SELECT_METHOD_MONTH){
            dateStr = dateStr.substring(0,7);

        }

        SpannableStringBuilder spannable=new SpannableStringBuilder(s+dateStr);
        AbsoluteSizeSpan span=new AbsoluteSizeSpan(30);
        spannable.setSpan(span, s.length(), (s + dateStr).length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        RelativeLayout.LayoutParams leftTitleParams = (RelativeLayout.LayoutParams)getTvLeftTitle().getLayoutParams();
        leftTitleParams.width = getActualWidthOnThisDevice(500);
        getTvLeftTitle().setText(spannable);
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
            ListViewItem item = new ListViewItem(ChartDetailedIdentityChangeFragment.this.getBaseActivity());
            if (position%2 == 0){
                 item.setBackgroundColor(getColor(R.color.chart_detail_line1));
            }else{
                item.setBackgroundColor(getColor(R.color.chart_detail_line2));
            }
            item.setData(dataList.get(position));
            return item;
        }
    }


    private void getCustomerConsumeChangeDetail(int page) {

        Map<String, String> params = new HashMap<>();

        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getBaseActivity()));
        params.put(ApiKey.COMMON_BEGIN_DATE, beginDate);
        params.put(ApiKey.COMMON_END_DATE, endDate);
        params.put(ApiKey.COMMON_PAGE, String.valueOf(page));


        HttpManager<JsonCustomerDetail> hh = new HttpManager<JsonCustomerDetail>(ChartDetailedIdentityChangeFragment.this) {

            @Override
            public void onJsonSuccess(JsonCustomerDetail jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {

                    nowPage++;
                    initDataList(jo.getDataList());
                    listViewAdapter.notifyDataSetChanged();
                    detailedDataList.onRefreshComplete();

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

        hh.startGet(getActivity(), ApiManager.HttpApi.CustomerConsumeChangeDetailGet, params);


    }



    private void initDataList(ArrayList<JsonCustomerDetail.DataItem> dataList){
        if (nowPage<=2){
            this.dataList.clear();
        }
        for (JsonCustomerDetail.DataItem dataItem:dataList){
            ListViewDataItem  newDataItem = new  ListViewDataItem();
            newDataItem.setDate(dataItem.getDate());
            newDataItem.setIsGroup(true);
            int addFlag = 1;

            if ( this.dataList.size()>0){
                if (!dataItem.getDate().equals(this.dataList.get(this.dataList.size()-1).getDate())){
                    this.dataList.add(newDataItem);
                }else{
                    addFlag = 0;
                }
            }else{
                this.dataList.add(newDataItem);
            }
            for (JsonCustomerDetail.DataItemDetailed dataItemDetailed:dataItem.getDataItemDetailedList()){
                if (addFlag ==0){
                    addFlag++;
                    continue;
                }
                ListViewDataItem newDataItemC = new  ListViewDataItem();
                newDataItemC.setMuser(dataItemDetailed.getMuser());
                newDataItemC.setChangeBefore(dataItemDetailed.getChangeBefore());
                newDataItemC.setChangeAfter(dataItemDetailed.getChangeAfter());
                newDataItemC.setmTime(dataItemDetailed.getmTime());
                newDataItemC.setUsName(dataItemDetailed.getUsName());
                newDataItemC.setDate(dataItem.getDate());
                this.dataList.add(newDataItemC);
                addFlag++;
            }

        }

    }

    class  ListViewDataItem{


        private String date;
        private boolean isGroup;
        private String usName;
        private String changeBefore;
        private String changeAfter;
        private String mTime;
        private String muser;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public boolean isGroup() {
            return isGroup;
        }

        public void setIsGroup(boolean isGroup) {
            this.isGroup = isGroup;
        }

        public String getUsName() {
            return usName;
        }

        public void setUsName(String usName) {
            this.usName = usName;
        }

        public String getChangeBefore() {
            return changeBefore;
        }

        public void setChangeBefore(String changeBefore) {
            this.changeBefore = changeBefore;
        }

        public String getChangeAfter() {
            return changeAfter;
        }

        public void setChangeAfter(String changeAfter) {
            this.changeAfter = changeAfter;
        }

        public String getmTime() {
            return mTime;
        }

        public void setmTime(String mTime) {
            this.mTime = mTime;
        }

        public String getMuser() {
            return muser;
        }

        public void setMuser(String muser) {
            this.muser = muser;
        }
    }

    class ListViewItem extends LinearLayout {
        private  LinearLayout titleLayout;
        private IteeTextView tvTitle;
        private LinearLayout bodyItemLayout;

        private ArrayList<IteeTextView> textList;
        @SuppressLint("WrongViewCast")
        public ListViewItem(Context context) {
            super(context);
            textList = new ArrayList<>();
            this.setOrientation(VERTICAL);
            ListView.LayoutParams titleLayoutParams = new  ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,ChartDetailedIdentityChangeFragment.this.getActualHeightOnThisDevice(ITEM_HEIGHT));

            titleLayout = new LinearLayout(context);
            titleLayout.setLayoutParams(titleLayoutParams);
            ListView.LayoutParams tvTitleParams = new  ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,ChartDetailedIdentityChangeFragment.this.getActualHeightOnThisDevice(ITEM_HEIGHT));

            tvTitle = new IteeTextView(context);
            tvTitle.setPadding(ChartDetailedIdentityChangeFragment.this.getActualWidthOnThisDevice(20),0,0,0);
            tvTitle.setLayoutParams(tvTitleParams);
            tvTitle.setGravity(Gravity.CENTER_VERTICAL);
            titleLayout.addView(tvTitle);




            //itemLayout
            ListView.LayoutParams bodyItemLayoutParams = new  ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,ChartDetailedIdentityChangeFragment.this.getActualHeightOnThisDevice(ITEM_HEIGHT));
            bodyItemLayout = new LinearLayout(context);
            bodyItemLayout.setLayoutParams(bodyItemLayoutParams);

            for (int i = 0;i<5;i++){
                LinearLayout.LayoutParams itemLayoutParams = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                itemLayoutParams.weight = 1;
                LinearLayout itemLayout = new LinearLayout(context);
                itemLayout.setOrientation(HORIZONTAL);
                itemLayout.setLayoutParams(itemLayoutParams);
                LinearLayout.LayoutParams itemParams = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                IteeTextView item = new IteeTextView(context);
                item.setTextSize(getColor(R.color.chart_detail_text));
                textList.add(item);
                item.setLayoutParams(itemParams);
                item.setGravity(Gravity.CENTER);
                item.setTextSize(Constants.FONT_SIZE_SMALLER);


                if (i>0){
                    LinearLayout.LayoutParams verticalLineParams = new  LinearLayout.LayoutParams(ChartDetailedIdentityChangeFragment.this.getActualWidthOnThisDevice(2),LinearLayout.LayoutParams.MATCH_PARENT);
                    ImageView verticalLine = new ImageView(context);
                    verticalLine.setLayoutParams(verticalLineParams);
                    verticalLine.setBackgroundColor(getColor(R.color.common_white));
                    itemLayout.addView(verticalLine);
                }

                itemLayout.addView(item);

                bodyItemLayout.addView(itemLayout);
            }

            this.addView(titleLayout);
            this.addView(bodyItemLayout);

        }



        public void setData(ListViewDataItem data) {
            if (data.isGroup()){
                tvTitle.setText(data.getDate());
                titleLayout.setVisibility(View.VISIBLE);
                bodyItemLayout.setVisibility(View.GONE);
            }else{
                titleLayout.setVisibility(View.GONE);
                bodyItemLayout.setVisibility(View.VISIBLE);
                textList.get(0).setText(data.getUsName());
                textList.get(1).setText(data.getChangeBefore());
                textList.get(2).setText(data.getChangeAfter());
                textList.get(3).setText(data.getMuser());
                textList.get(4).setText(data.getmTime());

            }
        }
    }
}
