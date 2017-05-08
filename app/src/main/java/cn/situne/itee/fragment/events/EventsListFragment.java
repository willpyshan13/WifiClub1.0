/**
 * Project Name: itee
 * File Name:	 EventsListFragment.java
 * Package Name: cn.situne.itee.fragment.events
 * Date:		 2015-01-10
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.events;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.adapter.EventsListItemAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonEventListGet;

/**
 * ClassName:EventsPricingFragment <br/>
 * Function: list of events. <br/>
 * UI:  09-1
 * Date: 2015-03-9 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class EventsListFragment extends BaseFragment {

    private RelativeLayout rlContainer;
    private ListView lvEvents;
    private EventsListItemAdapter adapterEventList;
    private ArrayList<JsonEventListGet.Event> dataList;

    private View.OnClickListener listenerAddEvent;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_events_list;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.newteetimes_events;
    }

    @Override
    protected void initControls(View rootView) {
        rlContainer = (RelativeLayout) rootView.findViewById(R.id.rl_item_container);
        lvEvents = new ListView(getActivity());
        dataList = new ArrayList<>();
        adapterEventList = new EventsListItemAdapter(getActivity(), dataList);
    }


    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        lvEvents.setAdapter(adapterEventList);
        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JsonEventListGet.Event event = dataList.get(position);
                if (event != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.EVENT_EVENT_ID, event.getEventId());
                    bundle.putBoolean(TransKey.EVENT_IS_ADD, false);
                    push(EventsEditFragment.class, bundle);
                }
            }
        });

        listenerAddEvent = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(TransKey.EVENT_IS_ADD, true);
                push(EventsEditFragment.class, bundle);
            }
        };
    }


    @Override
    protected void setLayoutOfControls() {

        rlContainer.addView(lvEvents);

        RelativeLayout.LayoutParams paramsLIstView = (RelativeLayout.LayoutParams) lvEvents.getLayoutParams();
        paramsLIstView.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsLIstView.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsLIstView.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        Drawable drawable = getResources().getDrawable(android.R.color.transparent);
        if (drawable != null) {
            lvEvents.setSelector(drawable);
        }
        lvEvents.setOverScrollMode(View.OVER_SCROLL_NEVER);
        lvEvents.setLayoutParams(paramsLIstView);
    }


    @Override
    protected void setPropertyOfControls() {

    }

    @Override
    protected void configActionBar() {
        setNormalMenuActionBar();
        getTvLeftTitle().setText(getString(R.string.newteetimes_events));
        getTvRight().setVisibility(View.VISIBLE);
        getTvRight().setBackgroundResource(R.drawable.icon_common_add);
        getTvRight().setOnClickListener(listenerAddEvent);
    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        getEventData();
    }

    private void getEventData() {

        dataList.clear();
        adapterEventList.notifyDataSetChanged();

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.EVENT_LIST_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.EVENT_LIST_USER_ID, AppUtils.getCurrentUserId(getActivity()));

        HttpManager<JsonEventListGet> hh = new HttpManager<JsonEventListGet>(EventsListFragment.this) {

            @Override
            public void onJsonSuccess(JsonEventListGet jo) {
                dataList.addAll(jo.getEvent());
                adapterEventList.notifyDataSetChanged();
            }

            @Override
            public void onJsonError(VolleyError error) {
                Utils.showShortToast(getActivity(), String.valueOf(error));
            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.EventsGet, params);
    }
}
