/**
 * Project Name: itee
 * File Name:	 AgentsListFragment.java
 * Package Name: cn.situne.itee.fragment
 * Date:		 2015-03-22
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.agents;


import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.adapter.AgentsListAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonAgentListGet;
import cn.situne.itee.view.StickyLayout;
import cn.situne.itee.view.SwipeListView;

/**
 * ClassName:AgentsListFragment <br/>
 * Function: list of agents. <br/>
 * Date: 2015-03-22 <br/>
 *
 * @author qiushuai
 * @version 1.0
 * @see
 */
public class AgentsListFragment extends BaseFragment {

    private StickyLayout stickyLayout;
    private RelativeLayout rlSearch;
    private SwipeListView swipeListView;

    private AgentsListAdapter agentsListAdapter;

    private ArrayList<JsonAgentListGet.Agent> listAgents;

    private int rightViewWidth;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_agents_list;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        stickyLayout = (StickyLayout) rootView.findViewById(R.id.sticky_layout);
        rlSearch = (RelativeLayout) rootView.findViewById(R.id.sticky_header);
        swipeListView = (SwipeListView) rootView.findViewById(R.id.sticky_content);

        listAgents = new ArrayList<>();
        rightViewWidth = AppUtils.getRightButtonWidth(mContext);
        agentsListAdapter = new AgentsListAdapter(getActivity(), listAgents, rightViewWidth);
        swipeListView.setAdapter(agentsListAdapter);

    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {

        agentsListAdapter.setDeleteListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Integer agentId = (Integer) view.getTag();
                AppUtils.showDeleteAlert(AgentsListFragment.this, new AppUtils.DeleteConfirmListener() {
                    @Override
                    public void onClickDelete() {
                        deleteAgents(agentId);
                    }
                });
            }
        });

        agentsListAdapter.setListenerJump(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swipeListView.isDeleteStatus()) {
                    swipeListView.hiddenRight();
                } else {
                    int position = (int) v.getTag();
                    JsonAgentListGet.Agent agent = listAgents.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putString(TransKey.AGENTS_AGENT_NAME, agent.getAgentName());
                    bundle.putInt(TransKey.AGENTS_AGENT_ID, agent.getAgentId());
                    push(AgentsRechargeFragment.class, bundle);
                }
            }
        });

        stickyLayout.setMaxHeaderHeight(100);
        stickyLayout.setOnGiveUpTouchEventListener(new StickyLayout.OnGiveUpTouchEventListener() {
            @Override
            public boolean giveUpTouchEvent(MotionEvent event) {
                return swipeListView.getFirstVisiblePosition() == 0;
            }
        });

        rlSearch.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                push(AgentsSearchFragment.class);
            }
        });
    }


    @Override
    protected void setLayoutOfControls() {

        swipeListView.setRightViewWidth(rightViewWidth);
    }

    @Override
    protected void setPropertyOfControls() {

    }

    @Override
    protected void configActionBar() {
        setNormalMenuActionBar();
        getTvLeftTitle().setText(R.string.agents_delete_agents);
        getTvRight().setBackground(null);
        getTvRight().setBackgroundResource(R.drawable.icon_common_add);
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(TransKey.EVENT_IS_ADD, true);
                push(AgentsAddEditFragment.class, bundle);


            }
        });
    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        getAgentsList();
    }

    private void getAgentsList() {
        listAgents.clear();
        agentsListAdapter.notifyDataSetChanged();
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        HttpManager<JsonAgentListGet> hh = new HttpManager<JsonAgentListGet>(AgentsListFragment.this) {

            @Override
            public void onJsonSuccess(JsonAgentListGet jo) {
                ArrayList<JsonAgentListGet.Agent> agentList = jo.getAgentsList();
                if (Utils.isListNotNullOrEmpty(agentList)) {
                    listAgents.addAll(agentList);
                    agentsListAdapter.notifyDataSetChanged();

                    swipeListView.hiddenRight();
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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.AgentsListGet, params);
    }

    private void deleteAgents(Integer agentId) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        params.put(ApiKey.AGENT_ID, String.valueOf(agentId));

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(AgentsListFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                Integer returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();

                if (returnCode == Constants.RETURN_CODE_20403_DELETE_SUCCESSFULLY) {
                    getAgentsList();
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
        hh.startDelete(getActivity(), ApiManager.HttpApi.AgentsDelete, params);
    }
}
