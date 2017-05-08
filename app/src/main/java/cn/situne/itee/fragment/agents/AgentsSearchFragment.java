/**
 * Project Name: itee
 * File Name:	 AgentsSearchFragment.java
 * Package Name: cn.situne.itee.fragment.agents
 * Date:		 2015-07-27
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.agents;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

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
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.BaseViewHolder;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonAgentsSearch;
import cn.situne.itee.view.IteeSearchView;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:AgentsSearchFragment <br/>
 * Function: agents quick search. <br/>
 * Date: 2015-07-27 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class AgentsSearchFragment extends BaseFragment {

    public ArrayList<JsonAgentsSearch.Agent> dataList;
    private IteeSearchView titleSearchView;
    private ListView searchDataList;
    private AgentsSearchAdapter listViewAdapter;
    private IteeTextView footerMes;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_agents_search;
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
        listViewAdapter = new AgentsSearchAdapter();
        searchDataList.setAdapter(listViewAdapter);
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        searchDataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (dataList != null && dataList.size() > position) {
                    JsonAgentsSearch.Agent agent = dataList.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putString(TransKey.AGENTS_AGENT_NAME, agent.getAgentAccount());
                    bundle.putInt(TransKey.AGENTS_AGENT_ID, Integer.valueOf(agent.getAgentId()));
                    push(AgentsRechargeFragment.class, bundle);
                }
            }
        });
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

    private void addFooterView() {
        LinearLayout llFooterView = new LinearLayout(getBaseActivity());
        llFooterView.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams footerMesParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        footerMes = new IteeTextView(getBaseActivity());
        footerMes.setLayoutParams(footerMesParams);
        footerMes.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        footerMes.setVisibility(View.GONE);
        footerMes.setSingleLine(false);

        llFooterView.addView(footerMes);

        footerMes.setVisibility(View.GONE);

        searchDataList.addFooterView(llFooterView);
    }

    private void initTitleView() {
        ActionBar actionBar = getBaseActivity().getSupportActionBar();
        LinearLayout.LayoutParams titleViewParams = new LinearLayout.LayoutParams(Utils.getWidth(getActivity()), getActionBarHeight());
        LinearLayout titleView = new LinearLayout(getBaseActivity());
        titleView.setBackgroundColor(getColor(R.color.common_light_gray));
        titleView.setLayoutParams(titleViewParams);
        titleView.setPadding(getActualWidthOnThisDevice(0), 0, 0, 0);

        LinearLayout.LayoutParams llp
                = new LinearLayout.LayoutParams(Utils.getWidth(getActivity()) - getActualWidthOnThisDevice(160), getActionBarHeight() - 20);
        titleView.setGravity(Gravity.CENTER_VERTICAL);
        titleView.setPadding(0, 0, 0, 0);
        titleSearchView = new IteeSearchView(getBaseActivity());
        titleSearchView.setHint(getString(R.string.common_search));
        titleSearchView.setTextSize(Constants.FONT_SIZE_NORMAL);
        titleSearchView.setBackground(getResources().getDrawable(R.drawable.bg_search_view));
        titleSearchView.setLayoutParams(llp);
        titleSearchView.setIcon(getActionBarHeight() - 20, R.drawable.icon_search_del, R.drawable.icon_search_del, R.drawable.icon_search, R.drawable.icon_search);
        titleSearchView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_UP == event.getAction()) {
                    if (keyCode == 66) {
                        Utils.hideKeyboard(getBaseActivity());
                        //api
                        getAgentsSearch(titleSearchView.getText().toString());
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
                getAgentsSearch(titleSearchView.getText().toString());
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

    private void getAgentsSearch(String keyword) {
        if (titleSearchView.getText().toString().length() > 0) {
            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
            params.put(ApiKey.AGENTS_SEARCH_KEYWORD, keyword);

            HttpManager<JsonAgentsSearch> hh = new HttpManager<JsonAgentsSearch>(getActivity()) {

                @Override
                public void onJsonSuccess(JsonAgentsSearch jo) {

                    Integer returnCode = jo.getReturnCode();

                    String msg = jo.getReturnInfo();
                    if (returnCode == Constants.RETURN_CODE_20301) {
                        dataList.clear();
                        dataList = jo.getAgentsList();
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
            hh.start(getActivity(), ApiManager.HttpApi.AgentsSearch, params);
        }
    }

    class AgentsSearchAdapter extends BaseAdapter {

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

            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.item_of_agents_search, null);
                viewHolder = new ViewHolder();

                viewHolder.rlItemContainer = (RelativeLayout) convertView.findViewById(R.id.rl_item_container);
                viewHolder.tvName = (IteeTextView) convertView.findViewById(R.id.tv_name);
                viewHolder.tvTel = (IteeTextView) convertView.findViewById(R.id.tv_tel);

                LayoutUtils.setLayoutHeight(viewHolder.rlItemContainer, 100, mContext);

                viewHolder.tvName.setTextSize(Constants.FONT_SIZE_MORE_LARGER);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            JsonAgentsSearch.Agent agent = dataList.get(position);

            viewHolder.tvName.setText(agent.getAgentAccount());
            viewHolder.tvTel.setText(agent.getAciValue());

            return convertView;
        }

        class ViewHolder extends BaseViewHolder {
            RelativeLayout rlItemContainer;
            IteeTextView tvName;
            IteeTextView tvTel;
        }
    }
}