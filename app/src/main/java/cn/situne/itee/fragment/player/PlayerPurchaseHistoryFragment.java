/**
 * Project Name: itee
 * File Name:  PlayerPurchaseHistoryFragment.java
 * Package Name: cn.situne.itee.fragment.player
 * Date:   2015-03-28
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.player;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.adapter.PlayerPurchaseHistoryAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonPurchaseHistoryGet;
import cn.situne.itee.view.SwipeListView;

/**
 * ClassName:PlayerPurchaseHistoryFragment <br/>
 * Function: member's purchae history  list fragment. <br/>
 * UI:  04-8-6
 * Date: 2015-03-28 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class PlayerPurchaseHistoryFragment extends BaseFragment {

    private RelativeLayout rlContainer;

    private SwipeListView swipeListView;
    private Integer memberId;
    private Integer agentId;
    private JsonPurchaseHistoryGet dataParameter;
    private View.OnClickListener onClickRefundListener;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_player_history;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        swipeListView = new SwipeListView(getActivity());
        rlContainer = (RelativeLayout) rootView.findViewById(R.id.rl_content_container);

        onClickRefundListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlRefund, getActivity())
                        || AppUtils.getAuth(AppUtils.AuthControl.AuthControlAgentsRefund, getActivity());
                if (hasPermission) {
                    Integer index = (int) v.getTag();
                    JsonPurchaseHistoryGet.DataListItem dataListItem = dataParameter.getDataList().get(index);
                    Bundle bundle = new Bundle();
                    bundle.putString(ApiKey.PAY_ID, dataListItem.getPayId());
                    bundle.putString("member_id", String.valueOf(memberId));
                    bundle.putString(TransKey.SELECTED_DATE, dataListItem.getTime());
                    bundle.putString(TransKey.TOTAL_COST, dataListItem.getAmount());
                    push(PlayerRefundFragment.class, bundle);
                } else {
                    AppUtils.showHaveNoPermission(mContext);
                }
            }
        };
    }

    @Override
    protected void setDefaultValueOfControls() {

        if (dataParameter != null) {
            PlayerPurchaseHistoryAdapter mAdapter
                    = new PlayerPurchaseHistoryAdapter(getActivity(), dataParameter, swipeListView.getRightViewWidth(),
                    memberId, onClickRefundListener);
            swipeListView.setAdapter(mAdapter);
        }

    }

    @Override
    protected void setListenersOfControls() {

        swipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();

                JsonPurchaseHistoryGet.DataListItem dataListItem = dataParameter.getDataList().get(position);
                String idParam = dataListItem.getPayId();
                bundle.putString(ApiKey.PAY_ID, idParam);
                bundle.putString(TransKey.COMMON_DATE_TIME, dataListItem.getTime());
                push(PlayerPurchaseHistoryDetailFragment.class, bundle);
            }
        });

    }

    @Override
    protected void setLayoutOfControls() {


        rlContainer.addView(swipeListView);
        RelativeLayout.LayoutParams paramsIncoming = (RelativeLayout.LayoutParams) swipeListView.getLayoutParams();
        paramsIncoming.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsIncoming.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsIncoming.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        swipeListView.setLayoutParams(paramsIncoming);
        swipeListView.setRightViewWidth(AppUtils.getRightButtonWidth(mContext));


    }

    @Override
    protected void setPropertyOfControls() {

    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getString(R.string.player_info_purchase_history));

    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        if (memberId != null && memberId > 0) {
            netLinkPurchaseHistory();
        } else {
            netAgentPurchaseHistory();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            memberId = bundle.getInt("memberId");
            agentId = bundle.getInt(TransKey.AGENTS_AGENT_ID);
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * get purchaseHistory data.
     */

    private void netLinkPurchaseHistory() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.PLAYER_MEMBER_ID, String.valueOf(memberId));


        HttpManager<JsonPurchaseHistoryGet> hh = new HttpManager<JsonPurchaseHistoryGet>(PlayerPurchaseHistoryFragment.this) {

            @Override
            public void onJsonSuccess(JsonPurchaseHistoryGet jo) {
                dataParameter = jo;
                setDefaultValueOfControls();
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.PurchaseHistory, params);
    }

    private void netAgentPurchaseHistory() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.AGENT_AGENT_ID, String.valueOf(agentId));


        HttpManager<JsonPurchaseHistoryGet> hh = new HttpManager<JsonPurchaseHistoryGet>(PlayerPurchaseHistoryFragment.this) {

            @Override
            public void onJsonSuccess(JsonPurchaseHistoryGet jo) {
                dataParameter = jo;

                setDefaultValueOfControls();
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.PurchaseHistoryGet, params);
    }

}
