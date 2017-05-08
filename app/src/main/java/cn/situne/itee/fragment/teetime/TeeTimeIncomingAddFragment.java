/**
 * Project Name: itee
 * File Name:  TeeTimeIncomingAddFragment.java
 * Package Name: cn.situne.itee.fragment.teetime
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.teetime;


import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.situne.itee.R;
import cn.situne.itee.adapter.TeeTimeIncomingAdapter;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.entity.IncomingCall;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.view.SwipeListView;

/**
 * ClassName:TeeTimeIncomingAddFragment <br/>
 * Function: get incoming call from phone fragment.<br/>
 * UI:  04-6
 * Date: 2015-03-11 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class TeeTimeIncomingAddFragment extends BaseFragment {

    private RelativeLayout rlContainer;
    private RelativeLayout rlContainerTitle;

    private SwipeListView swipeListView;
    private List<IncomingCall> dataSource = new ArrayList<>();

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_newteetimes_incoming;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        swipeListView = new SwipeListView(getActivity());
        rlContainer = (RelativeLayout) rootView.findViewById(R.id.rl_newteetimes_incoming_container);
        rlContainerTitle = (RelativeLayout) rootView.findViewById(R.id.rl_newteetimes_incoming_title_container);
        getContact();
    }

    private void getContact() {
        dataSource.clear();
        Cursor cursor = getActivity().getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        if (cursor.moveToFirst()) {
            do {
                //号码
                String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                //呼叫类型
//                String type;
                switch (Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)))) {
                    case CallLog.Calls.INCOMING_TYPE:
//                        type = "呼入";
                        SimpleDateFormat sfd = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))));
                        //呼叫时间
                        String time = sfd.format(date);
                        //联系人
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
                        //通话时间,单位:s
                        String duration = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION));

                        IncomingCall calEntity = new IncomingCall();
                        calEntity.setName(name);
                        calEntity.setTime(time);
                        calEntity.setTel(number);
                        calEntity.setDuration(duration);
                        dataSource.add(calEntity);
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
//                        type = "呼出";
                        SimpleDateFormat sfd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        Date date1 = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))));
                        //呼叫时间
                        String time1 = sfd1.format(date1);
                        //联系人
                        String name1 = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
                        //通话时间,单位:s
                        String duration1 = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION));

                        IncomingCall calEntity1 = new IncomingCall();
                        calEntity1.setName(name1);
                        calEntity1.setTime(time1);
                        calEntity1.setTel(number);
                        calEntity1.setDuration(duration1);
                        dataSource.add(calEntity1);
                        break;
                    case CallLog.Calls.MISSED_TYPE:
//                        type = "未接";
                        SimpleDateFormat sfd2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        Date date2 = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))));
                        //呼叫时间
                        String time2 = sfd2.format(date2);
                        //联系人
                        String name2 = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
                        //通话时间,单位:s
                        String duration2 = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION));

                        IncomingCall calEntity2 = new IncomingCall();
                        calEntity2.setName(name2);
                        calEntity2.setTime(time2);
                        calEntity2.setTel(number);
                        calEntity2.setDuration(duration2);
                        dataSource.add(calEntity2);
                        break;
                    default:
//                        type = "挂断";
                        SimpleDateFormat sfd3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        Date date3 = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))));
                        //呼叫时间
                        String time3 = sfd3.format(date3);
                        //联系人
                        String name3 = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
                        //通话时间,单位:s
                        String duration3 = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION));

                        IncomingCall calEntity3 = new IncomingCall();
                        calEntity3.setName(name3);
                        calEntity3.setTime(time3);
                        calEntity3.setTel(number);
                        calEntity3.setDuration(duration3);
                        dataSource.add(calEntity3);
                        break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }


    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        swipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEdit, getActivity())
                        //&& AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEditNonmember, getActivity());
                //if (hasPermission) {
                    Bundle bundle = new Bundle();
                    //返回所选电话号码。
                    bundle.putString("fromFlag", "memberTel");
                    bundle.putSerializable("memberTel", dataSource.get(position));
                    doBackWithReturnValue(bundle, TeeTimeAddFragment.class);
                //} else {
                    //AppUtils.showHaveNoPermission(TeeTimeIncomingAddFragment.this);
                //}
            }
        });

    }


    @Override
    protected void setLayoutOfControls() {


        RelativeLayout.LayoutParams paramsIncomingTitle = (RelativeLayout.LayoutParams) rlContainerTitle.getLayoutParams();
        paramsIncomingTitle.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsIncomingTitle.height = (int) (getScreenHeight() * 0.2f);
        paramsIncomingTitle.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        rlContainerTitle.setLayoutParams(paramsIncomingTitle);
        rlContainerTitle.setVisibility(View.GONE);

        rlContainer.addView(swipeListView);
        RelativeLayout.LayoutParams paramsIncoming = (RelativeLayout.LayoutParams) swipeListView.getLayoutParams();
        paramsIncoming.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsIncoming.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsIncoming.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        swipeListView.setLayoutParams(paramsIncoming);
        swipeListView.setRightViewWidth(AppUtils.getRightButtonWidth(mContext));

         mAdapter = new TeeTimeIncomingAdapter(getActivity(), dataSource, swipeListView.getRightViewWidth());
        swipeListView.setAdapter(mAdapter);


    }
    TeeTimeIncomingAdapter mAdapter;
    @Override
    protected void setPropertyOfControls() {

    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getString(R.string.teetimes_actionbar_incoming));

    }

    @Override
    public void onResume() {
        super.onResume();
//        getContact();
    }
}
