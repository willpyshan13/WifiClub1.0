/**
 * Project Name: itee
 * File Name:	 CityListFragment.java
 * Package Name: cn.situne.itee.fragment.administration
 * Date:		 2015-01-22
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.administration;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AlphabetIndexer;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.adapter.CityListAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.IndexCursorCity;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.player.PlayerInfoEditFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonCityList;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.LetterView;

/**
 * ClassName:CityListFragment <br/>
 * Function: city list page. <br/>
 * UI:  02-3-1
 * Date: 2015-01-22 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class CityListFragment extends BaseFragment {

    private final static String KEY_NAME = "name";
    private final static String KEY_COUNTRY = "country";
    private final static String KEY_ID = "id";
    private final static String KEY_ZONE = "zone";
    private final static String KEY_SORT = "sort";

    private RelativeLayout rlContainer;
    private RelativeLayout rlContainerList;
    private ListView memberListView;
    private AlphabetIndexer indexer;
    private IteeTextView tvOverlay;
    private LinearLayout llOverlay;
    private LetterView letterView;
    private String alphabet = Constants.ALPHABET;
    private Toast toast;
    private IteeTextView tvToast;
    private String index;

    private ArrayList<HashMap<String, Object>> listItems;
    private LetterView.OnLetterChangeListener letterChangeListener = new LetterView.OnLetterChangeListener() {

        @Override
        public void onLetterChange(int selectedIndex) {
            int position = indexer.getPositionForSection(selectedIndex);
            memberListView.setSelection(position);
            tvToast.setText(String.valueOf(alphabet.charAt(selectedIndex)));
            toast.show();

        }
    };
    private AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {


            int section = indexer.getSectionForPosition(firstVisibleItem);
            if (view.getLastVisiblePosition() != (view.getCount() - 1)) {
                letterView.setSelectedIndex(section);
            }


            int nextSection = section + 1;
            int pos = indexer.getPositionForSection(nextSection);
            if (pos == firstVisibleItem + 1) {
                View v = view.getChildAt(0);
                if (v == null) {
                    return;
                }
                int dex = v.getBottom() - tvOverlay.getHeight();
                if (dex <= 0) {
                    llOverlay.setPadding(0, dex, 0, 0);
                    tvOverlay.setText(String.valueOf(alphabet.charAt(section)));
                } else {
                    llOverlay.setPadding(0, 0, 0, 0);
                    tvOverlay.setText(String.valueOf(alphabet.charAt(section)));
                }
            } else {
                llOverlay.setPadding(0, 0, 0, 0);
                tvOverlay.setText(String.valueOf(alphabet.charAt(section)));
            }
        }
    };

    private void initToast() {
        toast = new Toast(getActivity());
        toast.setDuration(Toast.LENGTH_SHORT);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.member_with_index_toast, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        tvToast = (IteeTextView) view.findViewById(R.id.tvToast);

    }

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_city_select;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            index = bundle.getString("index", null);
        }

        initToast();

        listItems = new ArrayList<>();

        rlContainer = (RelativeLayout) rootView.findViewById(R.id.rl_citywithindex);
        llOverlay = (LinearLayout) rootView.findViewById(R.id.rl_city_with_index_title_container);
        tvOverlay = (IteeTextView) rootView.findViewById(R.id.tv_city_with_index_title);

        rlContainerList = (RelativeLayout) rootView.findViewById(R.id.rl_citywithindex_container);
        memberListView = new ListView(getActivity());
        letterView = new LetterView(getActivity(), null);


    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {

        memberListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> stringObjectMap = listItems.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("cityId", (String) stringObjectMap.get(KEY_ID));
                bundle.putString("cityName", (String) stringObjectMap.get(KEY_NAME));
                bundle.putString("country", (String) stringObjectMap.get(KEY_COUNTRY));
                bundle.putString("timeZone", (String) stringObjectMap.get(KEY_ZONE));
                if (index != null) {
                    bundle.putString("index", index);
                    doBackWithReturnValue(bundle, PlayerInfoEditFragment.class);
                } else {

                    doBackWithReturnValue(bundle, AdministrationEditFragment.class);
                }
            }
        });

    }

    @Override
    protected void setLayoutOfControls() {
        rlContainerList.addView(memberListView);
        RelativeLayout.LayoutParams paramsOrderPeopleAdd = (RelativeLayout.LayoutParams) memberListView.getLayoutParams();
        paramsOrderPeopleAdd.width = MATCH_PARENT;
        paramsOrderPeopleAdd.height = WRAP_CONTENT;
        paramsOrderPeopleAdd.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        memberListView.setLayoutParams(paramsOrderPeopleAdd);


        rlContainer.addView(letterView);
        RelativeLayout.LayoutParams paramsLetterView = (RelativeLayout.LayoutParams) letterView.getLayoutParams();
        paramsLetterView.width = (int) (getScreenWidth() * 0.1f);
        paramsLetterView.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsLetterView.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        letterView.setLayoutParams(paramsLetterView);

    }

    @Override
    protected void setPropertyOfControls() {
        letterView.setViewHeight(0);
        letterView.invalidate();
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(R.string.title_city_select);
        getTvLeftTitle().setVisibility(View.VISIBLE);

    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        getCityList();
    }

    private void getCityList() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        HttpManager<JsonCityList> hh = new HttpManager<JsonCityList>(this) {

            @Override
            public void onJsonSuccess(JsonCityList jo) {
                Integer returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    initData(jo.getDataList());
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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.CityList, params);
    }

    private void initData(ArrayList<JsonCityList.CityInfo> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            JsonCityList.CityInfo cityInfo = dataList.get(i);
            HashMap<String, Object> item = new HashMap<>();
            item.put(KEY_NAME, cityInfo.getCityName() + Constants.STR_COMMA);
            item.put(KEY_COUNTRY, cityInfo.getCountry());
            item.put(KEY_ID, cityInfo.getCityId());
            item.put(KEY_ZONE, cityInfo.getZoneCode());
            item.put(KEY_SORT, cityInfo.getCitySort());
            listItems.add(item);
        }

        IndexCursorCity cursor = new IndexCursorCity(listItems);
        indexer = new AlphabetIndexer(cursor, 0, alphabet);
        CityListAdapter adapter = new CityListAdapter(listItems, getActivity(), indexer);
        memberListView.setAdapter(adapter);
        memberListView.setOnScrollListener(scrollListener);
        letterView.setOnLetterChangeListener(letterChangeListener);
    }
}
