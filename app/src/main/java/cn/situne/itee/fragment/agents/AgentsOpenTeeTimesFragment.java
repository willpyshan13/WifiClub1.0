package cn.situne.itee.fragment.agents;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.ShoppingProduct;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonAgentsOpenTimeData;
import cn.situne.itee.manager.jsonentity.JsonShoppingPaymentGet;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.SwipeListView;

/**
 * Created by luochao on 9/28/15.
 */
public class AgentsOpenTeeTimesFragment extends BaseFragment {

    private SwipeListView listView;

    private ListAdapter listAdapter;
    private String agentsId;
    @Override
    protected int getFragmentId() {
        Log.d("AgentsOpenTeeTimesFragm", "complete");
        return R.layout.fragment_agents_open_tee_times;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        listView = (SwipeListView)rootView.findViewById(R.id.listView);
        listView.setRightViewWidth(AppUtils.getRightButtonWidth(getBaseActivity()));
        listAdapter = new ListAdapter();
        listView.setAdapter(listAdapter);
        Bundle bundle = getArguments();
        if (bundle!=null){
            agentsId = bundle.getString(TransKey.AGENTS_AGENT_ID);
        }
    }
    private ArrayList<JsonAgentsOpenTimeData.OpenTimesData> dataList;

    // API
    private void getOpenTimeList() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));

        params.put("agent_id", agentsId);

        HttpManager<JsonAgentsOpenTimeData> hh = new HttpManager<JsonAgentsOpenTimeData>(AgentsOpenTeeTimesFragment.this) {

            @Override
            public void onJsonSuccess(JsonAgentsOpenTimeData jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {

                    dataList = jo.getDataList();
                    listAdapter.notifyDataSetChanged();
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

        hh.startGet(this.getActivity(), ApiManager.HttpApi.AgentsBookingTimeList, params);
    }

    private JsonAgentsOpenTimeData.OpenTimesData findItem(String abmId){

        for (JsonAgentsOpenTimeData.OpenTimesData openTimesData:dataList){

            if (abmId.equals(openTimesData.getAbmId())){

                return openTimesData;
            }

        }
        return null;
    }


    private void deleteAgentsOpenTimes(final String abmId) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put("abmid", abmId);


        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(AgentsOpenTeeTimesFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                Integer returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();

                if (returnCode == Constants.RETURN_CODE_20301) {

                    JsonAgentsOpenTimeData.OpenTimesData openTimesData = findItem(abmId);
                    dataList.remove(openTimesData);
                    listView.hiddenRight();
                    listAdapter.notifyDataSetChanged();
                   // getOpenTimeList();

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
        hh.startDelete(getActivity(), ApiManager.HttpApi.AgentsBookingTimeDel, params);
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
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        getOpenTimeList();
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
//        getTvRight().setOnClickListener(null);
        getTvLeftTitle().setText(R.string.agents_open_tee_times);
        getTvRight().setBackgroundResource(R.drawable.icon_common_add);


        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Log.d("AgentsOpenTeeTimesFragm", "get");
                bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeAdd.value());
                bundle.putString(TransKey.AGENTS_AGENT_ID, agentsId);

                getBaseActivity().pushFragment(AgentsAddOpenTeeTimes.class, bundle);

            }
        });

    }

    class ListAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            if (dataList == null)return 0;
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
            ListViewItem  listViewItem=null;
            if (view == null){

                listViewItem = new ListViewItem(getBaseActivity());
            }else{

                listViewItem = (ListViewItem)view;

            }

            listViewItem.setTag(String.valueOf(i));
            listViewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("ListAdapter", "item");
                    Bundle bundle = new Bundle();
                    int position = Integer.parseInt(String.valueOf(view.getTag()));
                    bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeBrowse.value());
                    bundle.putString(TransKey.OLDID, dataList.get(position).getAbmId());
                    bundle.putString(TransKey.AGENTS_AGENT_ID, agentsId);
                    getBaseActivity().pushFragment(AgentsAddOpenTeeTimes.class, bundle);
                }
            });
            listViewItem.setTextView(dataList.get(i).getAbmDataDesc(),dataList.get(i).getAbmTimeDecs(),dataList.get(i).getAbmId());
            return listViewItem;
        }
    }

    class ListViewItem extends LinearLayout{

        private IteeTextView upTextView;
        private IteeTextView downTextView;

        public ListViewItem(Context context) {
            super(context);
            this.setOrientation(LinearLayout.HORIZONTAL);
            this.setBackgroundColor(getColor(R.color.common_white));



            LinearLayout.LayoutParams leftLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(100));
            RelativeLayout leftLayout = new RelativeLayout(getContext());
            leftLayout.setLayoutParams(leftLayoutParams);
//            leftLayout.setBackgroundColor(getColor(R.color.common_white));原来的代码
            leftLayout.setBackgroundResource(R.drawable.bg_linear_selector_color_white);//添加一个点击变色

            RelativeLayout.LayoutParams  upTextViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,getActualWidthOnThisDevice(50));
            upTextViewParams.leftMargin = getActualWidthOnThisDevice(40);


            RelativeLayout.LayoutParams  downTextViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,getActualWidthOnThisDevice(50));
            downTextViewParams.leftMargin = getActualWidthOnThisDevice(40);
            downTextViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            upTextView = new IteeTextView(getActivity());
            upTextView.setLayoutParams(upTextViewParams);
            downTextView = new IteeTextView(getActivity());
            downTextView.setLayoutParams(downTextViewParams);
            downTextView.setTextSize(Constants.FONT_SIZE_SMALLER);
            upTextView.setTextSize(Constants.FONT_SIZE_NORMAL);
            downTextView.setTextColor(getColor(R.color.common_gray));

            leftLayout.addView(upTextView);
            leftLayout.addView(downTextView);
            AppUtils.addBottomSeparatorLine(leftLayout, getContext());
            this.addView(leftLayout);


            LinearLayout.LayoutParams delBtnParams = new LinearLayout.LayoutParams(AppUtils.getRightButtonWidth(getContext()), getActualHeightOnThisDevice(100));
             delBtn = new Button(getContext());
            delBtn.setBackgroundResource(R.drawable.bg_common_delete);
            delBtn.setLayoutParams(delBtnParams);
            delBtn.setText(getString(R.string.delete_button));

            delBtn.setTextColor(getColor(R.color.common_white));
            delBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("ListViewItem", "del");
                    deleteAgentsOpenTimes(String.valueOf(view.getTag()));
                }
            });
            this.addView(delBtn);
        }
        private  Button delBtn;
        public void setTextView(String date,String time,String abmId){
            upTextView.setText(date);
            downTextView.setText(time);
            delBtn.setTag(abmId);

        }
    }
}
