/**
 * Project Name: itee
 * File Name:	 CurrencyListFragment.java
 * Package Name: cn.situne.itee.fragment.administration
 * Date:		 2015-01-22
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.administration;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.fragment.BaseFragment;

/**
 * ClassName:CurrencyListFragment <br/>
 * Function: currency list page. <br/>
 * UI:  02-3-2
 * Date: 2015-01-22 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class CurrencyListFragment extends BaseFragment {

    private final static String KEY_NAME = "name";
    private final static String KEY_VALUE = "value";
    private final static String KEY_ID = "id";

    private RelativeLayout rlCurrencyContainer;
    private ListView currListView;
    private SimpleAdapter currencyAdapter;

    private ArrayList<String> currencySymbolList;
    private ArrayList<String> currencyNameList;
    private ArrayList<Integer> currencyIdList;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_currency_select;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        rlCurrencyContainer = (RelativeLayout) rootView.findViewById(R.id.currency_container);
        currListView = new ListView(getActivity());

        Bundle bundle = getArguments();
        currencySymbolList = bundle.getStringArrayList("currencySymbolList");
        currencyNameList = bundle.getStringArrayList("currencyNameList");
        currencyIdList = bundle.getIntegerArrayList("currencyIdList");

        List<Map<String, Object>> listItems = new ArrayList<>();

        for (int i = 0; i < currencyNameList.size(); i++) {
            Map<String, Object> item = new HashMap<>();
            item.put(KEY_NAME, currencyNameList.get(i));
            item.put(KEY_VALUE, currencySymbolList.get(i));
            item.put(KEY_ID, currencyIdList.get(i));
            listItems.add(item);
        }

        currencyAdapter = new SimpleAdapter(getActivity(), listItems,
                R.layout.listitem_currency_select, new String[]{KEY_NAME, KEY_VALUE},
                new int[]{R.id.currencyMessage, R.id.message});

    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {

        currListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();
                bundle.putInt("currencyId", currencyIdList.get(position));
                bundle.putString("currencyName", currencyNameList.get(position));
                bundle.putString("currencySymbol", currencySymbolList.get(position));
                doBackWithReturnValue(bundle, AdministrationEditFragment.class);

            }
        });

    }

    @Override
    protected void setLayoutOfControls() {

        rlCurrencyContainer.addView(currListView);
        RelativeLayout.LayoutParams paramsCurrencyListView = (RelativeLayout.LayoutParams) currListView.getLayoutParams();
        paramsCurrencyListView.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsCurrencyListView.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsCurrencyListView.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        currListView.setLayoutParams(paramsCurrencyListView);

    }

    @Override
    protected void setPropertyOfControls() {
        currListView.setDivider(null);
        currListView.setAdapter(currencyAdapter);
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(R.string.title_currency_select);
    }

}
