/**
 * Project Name: itee
 * File Name:	 StaffDepartmentListFragment.java
 * Package Name: cn.situne.itee.fragment.staff
 * Date:		 2015-03-22
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.staff;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.adapter.StaffEditAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonStaffDepartmentListGet;
import cn.situne.itee.view.StickyLayout;
import cn.situne.itee.view.SwipeListView;

/**
 * ClassName:StaffDepartmentListFragment <br/>
 * Function: department list. <br/>
 * Date: 2015-03-22 <br/>
 * UI:07-1
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class StaffDepartmentListFragment extends BaseFragment {

    private StickyLayout stickyLayout;
    private RelativeLayout rlSearch;
    private SwipeListView swipeListView;
    private List<JsonStaffDepartmentListGet.ItemData> dataSource = new ArrayList<>();
    private StaffEditAdapter staffEditAdapter;


    @Override
    protected int getFragmentId() {
        return R.layout.fragment_staff_staff_edit;
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
    }

    @Override
    protected void setDefaultValueOfControls() {
        swipeListView.setRightViewWidth(AppUtils.getRightButtonWidth(mContext));
    }

    @Override
    protected void setListenersOfControls() {
        stickyLayout.setMaxHeaderHeight(100);
        stickyLayout.setOnGiveUpTouchEventListener(new StickyLayout.OnGiveUpTouchEventListener() {
            @Override
            public boolean giveUpTouchEvent(MotionEvent event) {
                return swipeListView.getScrollY() < 0.5;
            }
        });

        rlSearch.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                push(StaffSearchFragment.class);
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
        setNormalMenuActionBar();
        getTvLeftTitle().setText(R.string.staff_staff);
        getTvRight().setBackground(null);
        getTvRight().setBackgroundResource(R.drawable.icon_common_add);
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(TransKey.STAFF_IS_ADD, true);
                bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeAdd.value());
                push(DepartmentFragment.class, bundle);
            }
        });

        boolean hasPermission = Constants.ROLE_SUPER_ADMIN.equals(AppUtils.getAdminFlag(getActivity()));
        if (hasPermission) {
            getTvRight().setVisibility(View.VISIBLE);
        } else {
            getTvRight().setVisibility(View.INVISIBLE);
        }
    }

    private void getDepartmentListData() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        HttpManager<JsonStaffDepartmentListGet> hh = new HttpManager<JsonStaffDepartmentListGet>(StaffDepartmentListFragment.this) {

            @Override
            public void onJsonSuccess(JsonStaffDepartmentListGet jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    dataSource = jo.getDataList();
                    staffEditAdapter.setData(dataSource);
                    staffEditAdapter.notifyDataSetChanged();
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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.StaffDepartmentListGet, params);

    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();

        staffEditAdapter = new StaffEditAdapter(this, dataSource, swipeListView.getRightViewWidth());

        getDepartmentListData();

        swipeListView.setAdapter(staffEditAdapter);
        View.OnClickListener itemOnclickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                JsonStaffDepartmentListGet.ItemData departmentInfo = dataSource.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt(TransKey.STAFF_DEPARTMENT_TYPE, departmentInfo.getType());
                bundle.putString(TransKey.STAFF_DEPARTMENT_NAME, departmentInfo.getDepartmentName());
                bundle.putInt(TransKey.STAFF_COURSE_ID, departmentInfo.getCourseId());
                bundle.putInt(TransKey.STAFF_DEPARTMENT_ID, departmentInfo.getDepartmentId());
                push(StaffDepartmentMemberListFragment.class, bundle);
            }
        };
        staffEditAdapter.setItemListener(itemOnclickListener);

        staffEditAdapter.setOnRightItemClickListener(new StaffEditAdapter.onRightItemClickListener() {
            @Override
            public void onRightItemClick(View v, int position) {

                boolean hasPermission = Constants.ROLE_SUPER_ADMIN.equals(AppUtils.getAdminFlag(getActivity()));
                if (hasPermission) {
                    JsonStaffDepartmentListGet.ItemData departmentInfo = dataSource.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeBrowse.value());
                    bundle.putInt(TransKey.STAFF_DEPARTMENT_TYPE, departmentInfo.getType());
                    bundle.putInt(TransKey.STAFF_DEPARTMENT_ID, departmentInfo.getDepartmentId());
                    bundle.putString(TransKey.STAFF_DEPARTMENT_NAME, departmentInfo.getDepartmentName());
                    bundle.putInt(TransKey.STAFF_COURSE_ID, departmentInfo.getCourseId());
                    push(DepartmentFragment.class, bundle);
                    swipeListView.hiddenRight();
                } else {
                    AppUtils.showHaveNoPermission(StaffDepartmentListFragment.this);
                }
            }
        });
    }
}
