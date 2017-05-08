/**
 * Project Name: itee
 * File Name:  TeeTimeMemberListFragment.java
 * Package Name: cn.situne.itee.fragment.teetime
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.teetime;


import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.adapter.TeeTimeMemberAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.player.PlayerFragment;
import cn.situne.itee.fragment.player.PlayerInfoEditFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonCustomerBookingSearch;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.PopTeeTimesMemberNoMemberView;
import cn.situne.itee.view.SwipeListView;

/**
 * ClassName:TeeTimeMemberListFragment <br/>
 * Function: order select member or non_member fragment.<br/>
 * UI:  04-1-2
 * Date: 2015-03-11 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class TeeTimeMemberListFragment extends BaseFragment {

    private RelativeLayout rlContainer;
    private RelativeLayout rlContainerBottom;

    private IteeTextView tvAddProfile;

    private SwipeListView swipeListView;
    private List<JsonCustomerBookingSearch.Member> dataSource;
    private PopTeeTimesMemberNoMemberView menuWindow;
    private View.OnClickListener itemsOnClick;
    private String nowSignType = Constants.STR_FLAG_YES;
    private String searchName = StringUtils.EMPTY;
    private int position, parentPosition;
    private String choiceId;
    private String addMemberName;
    private String courseAreaId;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_newteetimes_member_select;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            nowSignType = bundle.getString("nowSignType", Constants.STR_FLAG_YES);
            searchName = bundle.getString("searchName", StringUtils.EMPTY);
            position = bundle.getInt("position");
            choiceId = bundle.getString("choiceId", StringUtils.EMPTY);
            parentPosition = bundle.getInt("parentPosition");
            courseAreaId = bundle.getString(TransKey.COURSE_AREA_ID, StringUtils.EMPTY);


            addMemberName = bundle.getString(TransKey.COURSE_ADD_MEMBER_NAME, StringUtils.EMPTY);
        }

        swipeListView = new SwipeListView(getActivity());
        tvAddProfile = new IteeTextView(getActivity());
        rlContainer = (RelativeLayout) rootView.findViewById(R.id.rl_newteetimes_memeber_select_container);
        rlContainerBottom = (RelativeLayout) rootView.findViewById(R.id.rl_newteetimes_memeber_select_bottom_container);
    }


    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        tvAddProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEdit, getActivity())
                        && AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEditNonmember, getActivity());
                if (hasPermission) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeAdd.value());

                    bundle.putString(TransKey.COURSE_ADD_MEMBER_NAME, addMemberName);

                    push(PlayerInfoEditFragment.class, bundle);
                } else {
                    AppUtils.showHaveNoPermission(TeeTimeMemberListFragment.this);
                }
            }
        });

        //为弹出窗口实现监听类
        itemsOnClick = new View.OnClickListener() {

            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ll_tee_members:
                        nowSignType = "1";
                        netLinkCustomerBookingSearch(searchName);
                        getTvLeftTitle().setText(getString(R.string.newteetimes_members));
                        break;
                    case R.id.ll_tee_agents:
                        nowSignType = "2";
                        netLinkCustomerBookingSearch(searchName);
                        getTvLeftTitle().setText(getString(R.string.newteetimes_no_members));
                        break;
                    default:
                        break;
                }
                v.setBackgroundColor(getColor(R.color.common_gray));
                menuWindow.dismiss();
            }

        };
        swipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JsonCustomerBookingSearch.Member member = dataSource.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt(TransKey.COMMON_MEMBER_ID, member.getMemberId());
                push(PlayerFragment.class, bundle);
            }
        });

    }


    @Override
    protected void setLayoutOfControls() {

        rlContainerBottom.addView(tvAddProfile);
        RelativeLayout.LayoutParams paramAddAProfile = (RelativeLayout.LayoutParams) tvAddProfile.getLayoutParams();
        paramAddAProfile.width = (int) (getScreenWidth() * 0.9f);
        paramAddAProfile.height = (int) (getScreenHeight() * 0.08f);
        paramAddAProfile.addRule(RelativeLayout.CENTER_IN_PARENT);
        tvAddProfile.setLayoutParams(paramAddAProfile);
        tvAddProfile.setBackground(getResources().getDrawable(R.drawable.member_add_button));
        tvAddProfile.setGravity(Gravity.CENTER);
        tvAddProfile.setText(getResources().getString(R.string.newteetimes_btn_add_a_profile));
        tvAddProfile.setTextSize(Constants.FONT_SIZE_LARGER);
        tvAddProfile.setTextColor(getColor(R.color.common_white));

        rlContainer.addView(swipeListView);
        swipeListView.setRightViewWidth(AppUtils.getRightButtonWidth(mContext));
    }

    @Override
    protected void executeOnceOnCreate() {
        netLinkCustomerBookingSearch(searchName);
    }

    @Override
    protected void setPropertyOfControls() {
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setId(View.generateViewId());
        //set title.
        switch (nowSignType) {
            case "1":
                getTvLeftTitle().setText(getString(R.string.newteetimes_members));
                break;
            case "2":
                getTvLeftTitle().setText(getString(R.string.newteetimes_no_members));
                break;
        }

        RelativeLayout.LayoutParams paramsLeftTitle = (RelativeLayout.LayoutParams) getTvLeftTitle().getLayoutParams();
        paramsLeftTitle.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        getTvLeftTitle().setLayoutParams(paramsLeftTitle);

        RelativeLayout parent = (RelativeLayout) getTvRight().getParent();
        ImageView ivDropdown = new ImageView(getActivity());
        ivDropdown.setImageResource(R.drawable.icon_arrow_down);
        parent.addView(ivDropdown);
        RelativeLayout.LayoutParams ivPackageLayoutParams = (RelativeLayout.LayoutParams) ivDropdown.getLayoutParams();
        ivPackageLayoutParams.height = (int) (getScreenHeight() * 0.03f);
        ivPackageLayoutParams.width = (int) (getScreenHeight() * 0.03f);
        ivPackageLayoutParams.addRule(RelativeLayout.RIGHT_OF, getTvLeftTitle().getId());
        ivPackageLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivPackageLayoutParams.leftMargin = 3;
        ivDropdown.setLayoutParams(ivPackageLayoutParams);


        parent.invalidate();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化SelectPicPopupWindow
                menuWindow = new PopTeeTimesMemberNoMemberView(getActivity(), nowSignType, itemsOnClick);
                //显示窗口
                menuWindow.showAsDropDown(v, 0, getActualHeightOnThisDevice(20));
            }
        };

        getTvLeftTitle().setOnClickListener(listener);
        ivDropdown.setOnClickListener(listener);
    }

    @Override
    protected void reShowWithBackValue() {
        Bundle bundle = getReturnValues();
        if (bundle != null && bundle.getBoolean(TransKey.COMMON_REFRESH)) {
            netLinkCustomerBookingSearch(searchName);
        }
    }

    private void netLinkCustomerBookingSearch(String name) {
        //04-1-2输入预约人姓名时查找相似客户姓名资料 接口
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.TEE_TIME_MEMBER_TYPE_ID, nowSignType);
        params.put(ApiKey.TEE_TIME_CUSTOMER_NAME, name);
        params.put(ApiKey.COMMON_COURES_ID, AppUtils.getCurrentCourseId(getActivity()));
        HttpManager<JsonCustomerBookingSearch> hh = new HttpManager<JsonCustomerBookingSearch>(TeeTimeMemberListFragment.this) {

            @Override
            public void onJsonSuccess(JsonCustomerBookingSearch jo) {
                int tokenReturn = jo.getReturnCode();

                if (tokenReturn == Constants.RETURN_CODE_20301) {
                    dataSource = jo.getDataList();
                    JsonCustomerBookingSearch.Member choiceMember = null;
                    //sort list
                    for (int i = 0; i < dataSource.size(); i++) {
                        JsonCustomerBookingSearch.Member members = dataSource.get(i);
                        if (choiceId.equals(String.valueOf(members.getMemberId()))) {
                            choiceMember = members;
                            dataSource.remove(i);
                            i--;
                        }
                    }
                    if (choiceMember != null) {
                        dataSource.add(0, choiceMember);
                    }
                    if (dataSource != null) {
                        TeeTimeMemberAdapter mAdapter = new TeeTimeMemberAdapter(getActivity(), dataSource, choiceId,
                                position, parentPosition, swipeListView.getRightViewWidth());
                        mAdapter.setNowSignType(nowSignType);
                        swipeListView.setAdapter(mAdapter);
                    }
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.CsbookingSearch, params);
    }
}
