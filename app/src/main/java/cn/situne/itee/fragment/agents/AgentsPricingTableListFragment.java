/**
 * Project Name: itee
 * File Name:	 AgentPricingTableListFragment.java
 * Package Name: cn.situne.itee.fragment
 * Date:		 2015-03-22
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.agents;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

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
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonAgentsPricingListGet;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.SwipeLinearLayout;

/**
 * ClassName:AgentPricingTableListFragment <br/>
 * Function: login page. <br/>
 * Date: 2015-03-22 <br/>
 * UI:10-4
 *
 * @author qiushuai
 * @version 1.0
 * @see
 */
public class AgentsPricingTableListFragment extends BaseFragment {

    private RelativeLayout rlAgentPricingTableListContainer;
    private ListView swipeListView;

    // private AgentPricingTableListAdapter agentPricingTableListAdapter;
    private View.OnClickListener listenerDelete;
    private View.OnClickListener listenerItemClick;

    private String agentName;
    private Integer agentId;
    private SwipeLinearLayout selectSwipeLinearLayout;

    private ListAdapter listAdapter;

    @Override
    protected int getFragmentId() {
        Log.d("AgentsPricingTableListF", "price table");
        return R.layout.fragment_agent_message_delete;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            agentId = bundle.getInt(TransKey.AGENTS_AGENT_ID);
            agentName = bundle.getString(TransKey.AGENTS_AGENT_NAME);
        }

        rlAgentPricingTableListContainer = (RelativeLayout) rootView.findViewById(R.id.rl_agent_pricing_table_list_container);
        swipeListView = new ListView(getActivity());

        listAdapter = new ListAdapter();
        swipeListView.setAdapter(listAdapter);
    }

    @Override
    protected void setDefaultValueOfControls() {

    }


    @Override
    protected void setListenersOfControls() {
        listenerItemClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeBrowse.value());
                bundle.putString(TransKey.AGENTS_MAIN_ID, String.valueOf(v.getTag()));
                //bundle.putString(TransKey.AGENTS_AGENT_TIME_ID, agentsPricingList.get((int) v.getTag()).getAgentTimeId());
                push(AgentsPricingTableFragment.class, bundle);

            }
        };
        listenerDelete = new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AppUtils.showDeleteAlert(AgentsPricingTableListFragment.this, new AppUtils.DeleteConfirmListener() {
                    @Override
                    public void onClickDelete() {
                        deleteAgentPricingList(String.valueOf(view.getTag()));
                    }
                });

            }
        };
    }


    @Override
    protected void setLayoutOfControls() {

        rlAgentPricingTableListContainer.addView(swipeListView);
        RelativeLayout.LayoutParams paramsAgents = (RelativeLayout.LayoutParams) swipeListView.getLayoutParams();
        paramsAgents.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsAgents.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsAgents.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        swipeListView.setLayoutParams(paramsAgents);
        //swipeListView.setRightViewWidth(AppUtils.getRightButtonWidth(mContext));

    }

    @Override
    protected void setPropertyOfControls() {

    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(agentName);
        getTvRight().setBackground(null);
        getTvRight().setBackgroundResource(R.drawable.icon_common_add);

        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(TransKey.AGENTS_AGENT_ID, agentId);
                bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeAdd.value());
                getBaseActivity().pushFragment(AgentsPricingTableFragment.class, bundle);
            }
        });
    }


    private List<JsonAgentsPricingListGet.PricingItem> agentsPricingList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            agentName = bundle.getString(TransKey.AGENTS_AGENT_NAME);
            agentId = bundle.getInt(TransKey.AGENTS_AGENT_ID);
        }


        getAgentPricingList();
        return v;
    }

    private void getAgentPricingList() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.PRICING_TYPE, Constants.PRICING_TYPE_3);
        params.put(ApiKey.PRICING_TYPE_ID, String.valueOf(agentId));

        Utils.log("getAgentPricingList :params = " + params);

        HttpManager<JsonAgentsPricingListGet> hh = new HttpManager<JsonAgentsPricingListGet>(AgentsPricingTableListFragment.this) {

            @Override
            public void onJsonSuccess(JsonAgentsPricingListGet jo) {
                agentsPricingList = jo.getAgentsPricingList();
                listAdapter.setAgentsPricingList(agentsPricingList);
                listAdapter.notifyDataSetChanged();
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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.PricingListPageGetX, params);
    }


    private void deleteAgentPricingList(String mainId) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.PRICING_MAIN_ID, mainId);

        Utils.log("deleteAgentPricingList params = " + params);
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(AgentsPricingTableListFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                Integer returnCode = jo.getReturnCode();

                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20403_DELETE_SUCCESSFULLY) {
                    getAgentPricingList();
                }
                Utils.showShortToast(getActivity(), msg);
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
        hh.startDelete(getActivity(), ApiManager.HttpApi.PricingListPageGetX, params);
    }

    class ListAdapter extends BaseAdapter {


        private List<JsonAgentsPricingListGet.PricingItem> agentsPricingList;

        public List<JsonAgentsPricingListGet.PricingItem> getAgentsPricingList() {
            return agentsPricingList;
        }

        public void setAgentsPricingList(List<JsonAgentsPricingListGet.PricingItem> agentsPricingList) {
            this.agentsPricingList = agentsPricingList;
        }

        @Override
        public int getCount() {
            if (agentsPricingList == null) return 0;
            return agentsPricingList.size();
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

            ListItem listItem = null;
            if (view == null) {

                listItem = new ListItem(getBaseActivity(), i);
            } else {

                listItem = (ListItem) view;
            }
            listItem.setTag(agentsPricingList.get(i).getMainId());
            listItem.getTitleLayout().hideRight();

            listItem.getTitleLayout().setSwipeLayoutListener(new SwipeLinearLayout.SwipeLayoutListener() {
                @Override
                public void scrollView(View item) {
                    if (selectSwipeLinearLayout != null)
                        selectSwipeLinearLayout.hideRight();
                }
            });


            listItem.getTitleLayout().setAfterShowRightListener(new SwipeLinearLayout.AfterShowRightListener() {
                @Override
                public void doAfterShowRight(SwipeLinearLayout swipeLinearLayout) {
                    selectSwipeLinearLayout = swipeLinearLayout;
                }
            });


            listItem.refreshLayout(agentsPricingList.get(i), i);


            listItem.setItemListener(listenerItemClick, listenerDelete);
            return listItem;
        }
    }


    class ListItem extends LinearLayout {

        private SwipeLinearLayout titleLayout;
        private View v;

        public SwipeLinearLayout getTitleLayout() {
            return titleLayout;
        }

        public void setTitleLayout(SwipeLinearLayout titleLayout) {
            this.titleLayout = titleLayout;
        }

        private IteeTextView tvTitleText;
        private IteeTextView tvSubDate;

        private Button delBtn;
        private LinearLayout bodyLayout;

        public ListItem(Context context, int position) {
            super(context);
            this.setOrientation(VERTICAL);

            LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
            titleLayout = new SwipeLinearLayout(getContext(), AppUtils.getRightButtonWidth(getContext()));
            titleLayout.setLayoutParams(titleLayoutParams);
            titleLayout.setOrientation(HORIZONTAL);

            LinearLayout.LayoutParams bodyLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            bodyLayout = new LinearLayout(getContext());
            bodyLayout.setLayoutParams(bodyLayoutParams);
            bodyLayout.setOrientation(VERTICAL);

            setTitleLayout();

            LinearLayout.LayoutParams vParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(20));
            v = new View(getContext());

            v.setLayoutParams(vParams);


            this.addView(v);
            this.addView(AppUtils.getSeparatorLine(getContext()));
            this.addView(AppUtils.getSeparatorLine(getContext()));
            this.addView(titleLayout);
            this.addView(bodyLayout);

            titleLayout.setBackgroundColor(getColor(R.color.common_white));
//            bodyLayout.setBackgroundColor(getColor(R.color.common_white));

            bodyLayout.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
        }

        RelativeLayout rlTitle;

        private void setTitleLayout() {
            LinearLayout.LayoutParams rlTitleParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
            rlTitle = new RelativeLayout(getContext());
            rlTitle.setLayoutParams(rlTitleParams);
            LinearLayout.LayoutParams delBtnParams = new LinearLayout.LayoutParams(AppUtils.getRightButtonWidth(getContext()), getActualHeightOnThisDevice(100));
            delBtn = new Button(getContext());
            delBtn.setBackgroundResource(R.drawable.bg_common_delete);
            delBtn.setLayoutParams(delBtnParams);

            tvTitleText = new IteeTextView(getContext());
            tvSubDate = new IteeTextView(getContext());
            rlTitle.addView(tvTitleText);
            rlTitle.addView(tvSubDate);
            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvTitleText, 20, getContext());

            RelativeLayout.LayoutParams titleParams = (RelativeLayout.LayoutParams) tvTitleText.getLayoutParams();

            titleParams.width = getActualWidthOnThisDevice(500);
            tvTitleText.setTextColor(mContext.getResources().getColor(R.color.common_blue));
            RelativeLayout.LayoutParams paramsTvSubDate = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(500), RelativeLayout.LayoutParams.WRAP_CONTENT);
            paramsTvSubDate.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            paramsTvSubDate.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            paramsTvSubDate.leftMargin = getActualWidthOnThisDevice(20);
            paramsTvSubDate.bottomMargin = getActualHeightOnThisDevice(2);
            tvSubDate.setLayoutParams(paramsTvSubDate);

            tvSubDate.setTextColor(mContext.getResources().getColor(R.color.common_gray));
            tvSubDate.setTextSize(Constants.FONT_SIZE_MORE_SMALLER);
            ImageView rightIcon = new ImageView(getContext());
            rightIcon.setBackgroundResource(R.drawable.icon_right_arrow);
            rlTitle.addView(rightIcon);
            rlTitle.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
            titleLayout.addView(rlTitle);
            titleLayout.addView(delBtn);
            LayoutUtils.setRightArrow(rightIcon, 20, getContext());


            delBtn.setText(getString(R.string.delete_button));

            delBtn.setTextColor(getColor(R.color.common_white));
        }

        private void addBodyLayout(JsonAgentsPricingListGet.PricingItem data) {
            bodyLayout.removeAllViews();
            data.getPricingDataList();

            for (JsonAgentsPricingListGet.PricingData pricingData : data.getPricingDataList()) {
                LinearLayout.LayoutParams rlBodyItemParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
                RelativeLayout rlBodyItem = new RelativeLayout(getContext());
                rlBodyItem.setLayoutParams(rlBodyItemParams);
                IteeTextView tvProduct = new IteeTextView(getContext());
                tvProduct.setText(pricingData.getProductName());
                IteeTextView tvShowMoney = new IteeTextView(getContext());
                tvShowMoney.setText(AppUtils.getCurrentCurrency(getContext()) + Utils.get2DigitDecimalString(pricingData.getGuestPrice()));
                rlBodyItem.addView(tvProduct);
                rlBodyItem.addView(tvShowMoney);

                AppUtils.addTopSeparatorLine(rlBodyItem, getContext());
                bodyLayout.addView(rlBodyItem);
                LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvProduct, 20, getContext());
                LayoutUtils.setCellRightValueViewOfRelativeLayout(tvShowMoney, 20, getContext());
            }

        }

        public void refreshLayout(JsonAgentsPricingListGet.PricingItem data, int position) {

            if (position != 0) {
                v.setVisibility(View.VISIBLE);
            } else {
                v.setVisibility(View.GONE);
            }

//            ArrayList<String> dateList = AppUtils.changeString2List(data.getDateDesc(), Constants.STR_COMMA);
//            String etTitle = AppUtils.getEtTimeTitle(dateList, mContext);
            tvTitleText.setText(data.getDateDescStr());
            tvSubDate.setText(data.getTimeInfo());
            delBtn.setTag(data.getMainId());
            addBodyLayout(data);


        }

        private void setItemListener(OnClickListener selectListener, OnClickListener delListener) {
            delBtn.setOnClickListener(delListener);
            rlTitle.setOnClickListener(selectListener);
            rlTitle.setTag(this.getTag());
            this.setOnClickListener(selectListener);
        }
    }
}
