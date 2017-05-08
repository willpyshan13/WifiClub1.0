package cn.situne.itee.fragment.teetime;

/**
 * Project Name: itee
 * File Name:  TeeTimeMemberListWithIndexFragment.java
 * Package Name: cn.situne.itee.fragment.teetime
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AlphabetIndexer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.adapter.MemberWithIndexAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.IndexCursor;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.agents.AgentsAddEditFragment;
import cn.situne.itee.fragment.events.EventsEditFragment;
import cn.situne.itee.fragment.player.PlayerInfoEditFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonSigningGuest;
import cn.situne.itee.view.LetterView;
import cn.situne.itee.view.PopTeeTimesMemberView;

/**
 * ClassName:TeeTimeMemberListWithIndexFragment <br/>
 * Function: order select sign guest from meber,agent,event<br/>
 * UI:  04-2-3
 * Date: 2015-03-11 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class TeeTimeMemberListWithIndexFragment extends BaseFragment {

    private RelativeLayout rlContainer;
    private RelativeLayout rlContainerList;
    private ListView memberListView;
    private List<JsonSigningGuest.Member> memberList;
    private AlphabetIndexer indexer;
    private TextView tvOverlay;
    private LinearLayout llOverlay;
    private LetterView letterView;
    private String alphabet = Constants.ALPHABET;
    private MemberWithIndexAdapter adapter;
    private Toast toast;
    private TextView tvToast;
    private PopTeeTimesMemberView menuWindow;
    private View.OnClickListener itemsOnClick;
    private int fromAdd = -1;
    private String nowSignType = "1";
    private String signId;
    private Boolean isNoEvent;
    private Boolean isNoMember;
    private int parentPosition;
    private Map<String, Integer> map;

    private String bookingDate;
    private String courseAreaId;

    private String show;
    private String addAgent;

    private void initToast() {
        toast = new Toast(getActivity());
        toast.setDuration(Toast.LENGTH_SHORT);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.member_with_index_toast, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        tvToast = (TextView) view.findViewById(R.id.tvToast);

    }

    @Override
    protected void reShowWithBackValue() {

        Bundle bundle = getReturnValues();
        if (bundle != null && bundle.getBoolean(TransKey.COMMON_REFRESH)) {

            netLink(nowSignType);
        }
    }

    private void netLink(String signType) {
        this.nowSignType = signType;
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.TEE_TIME_SIGN_TYPE, signType);
        params.put(ApiKey.COMMON_COURES_ID, AppUtils.getCurrentCourseId(getActivity()));
        params.put(ApiKey.COURSE_AREA_ID, courseAreaId);
        params.put("show_flag", show);

        if (fromAdd != -1) {
            params.put(ApiKey.TEE_TIME_SIGN_FLAG, Constants.STR_1);
        } else {
            params.put(ApiKey.TEE_TIME_SIGN_FLAG, Constants.STR_0);
        }
        if (Utils.isStringNotNullOrEmpty(bookingDate)) {
            params.put(ApiKey.TEE_TIME_SIGN_BOOKING_DATE, bookingDate);
        }

        HttpManager<JsonSigningGuest> hh = new HttpManager<JsonSigningGuest>(TeeTimeMemberListWithIndexFragment.this) {

            @Override
            public void onJsonSuccess(JsonSigningGuest jo) {
                int tokenStatus = jo.getReturnCode();
                if (tvOverlay !=null)tvOverlay.setText("");

                if (tokenStatus == Constants.RETURN_CODE_20301) {
                    memberList = jo.getDataList();

                    if (memberList != null && memberList.size() > 0) {
                        if (Constants.STR_1.equals(nowSignType)) {
                            for (int i = 0; i < memberList.size(); i++) {
                                JsonSigningGuest.Member member = memberList.get(i);
                                if (Constants.STR_0.equals(member.getSignNumber())) {
                                    memberList.remove(i);
                                    i--;
                                }
                            }
                        }
                        if (memberList != null && memberList.size() > 0) {
                            IndexCursor cursor = new IndexCursor(memberList);
                            indexer = new AlphabetIndexer(cursor, 0, alphabet);
                            adapter = new MemberWithIndexAdapter(memberList, nowSignType, TeeTimeMemberListWithIndexFragment.this, indexer, parentPosition, fromAdd);
                            adapter.setMap(map);
                            memberListView.setAdapter(adapter);
                            memberListView.setOnScrollListener(scrollListener);
                            letterView.setOnLetterChangeListener(letterChangeListener);
                        }
                    }
                } else if (tokenStatus == Constants.RETURN_CODE_20302) {
                    memberListView.setAdapter(null);
                        memberListView.setOnScrollListener(null);
                        memberListView.deferNotifyDataSetChanged();
                        letterView.setOnLetterChangeListener(null);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {


            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.SigningGuest, params);
    }


    @Override
    protected int getFragmentId() {
        return R.layout.fragment_member_with_index;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void initControls(View rootView) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            isNoEvent = bundle.getBoolean(TransKey.TEE_TIME_MEMBER_NO_EVENT);
            fromAdd = bundle.getInt("fromAdd", -1);
            parentPosition = bundle.getInt("parentPosition");
            nowSignType = bundle.getString("nowSignType", "1");
            isNoMember = bundle.getBoolean(TransKey.TEE_TIME_MEMBER_NO_MEMBER);
            signId = bundle.getString("signId");
            bookingDate = bundle.getString(TransKey.BOOKING_DATE);
            courseAreaId = bundle.getString(TransKey.COURSE_AREA_ID, StringUtils.EMPTY);
            show  = bundle.getString("show",Constants.STR_0);

            addAgent = bundle.getString("addAgent",Constants.STR_0);

            if (isNoMember) {
                nowSignType = "2";
            }

            if (Utils.isStringNotNullOrEmpty(bundle.getString("count"))) {
                map = (Map<String, Integer>) Utils.getObjectFromString(bundle.getString("count"));
            }

        }
        netLink(nowSignType);
        initToast();
        rlContainer = (RelativeLayout) rootView.findViewById(R.id.rl_content_container);
        llOverlay = (LinearLayout) rootView.findViewById(R.id.rl_member_with_index_title_container);
        tvOverlay = (TextView) rootView.findViewById(R.id.tv_member_with_index_title);

        rlContainerList = (RelativeLayout) rootView.findViewById(R.id.rl_memberwithindex_container);
        memberListView = new ListView(getActivity());
        letterView = new LetterView(getActivity(), null);

    }


    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        //为弹出窗口实现监听类
        itemsOnClick = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ll_tee_members:
                        netLink("1");
                        getTvLeftTitle().setText(getString(R.string.newteetimes_members));
                        break;
                    case R.id.ll_tee_agents:
                        netLink("2");
                        getTvLeftTitle().setText(getString(R.string.newteetimes_agents));
                        break;
                    case R.id.ll_tee_events:
                        netLink("3");
                        getTvLeftTitle().setText(getString(R.string.newteetimes_events));
                        break;

                    case R.id.ll_tee_no_members:
                        netLink("4");
                        getTvLeftTitle().setText(getString(R.string.newteetimes_no_members));
                        break;
                    default:
                        break;
                }
                v.setBackgroundColor(getColor(R.color.common_gray));
                menuWindow.dismiss();
            }

        };


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
        paramsLetterView.height = MATCH_PARENT;
        paramsLetterView.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        letterView.setLayoutParams(paramsLetterView);


    }

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
            letterView.setSelectedIndex(section);

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

    @Override
    protected void setPropertyOfControls() {
        letterView.setViewHeight(0);
        letterView.invalidate();
    }

    @Override
    protected void configActionBar() {

        setStackedActionBar();


        getTvLeftTitle().setId(View.generateViewId());
        RelativeLayout.LayoutParams paramsLeftTitle = (RelativeLayout.LayoutParams) getTvLeftTitle().getLayoutParams();
        paramsLeftTitle.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        getTvLeftTitle().setLayoutParams(paramsLeftTitle);

        switch (nowSignType) {
            case "1":
                getTvLeftTitle().setText(getString(R.string.newteetimes_members));
                break;
            case "2":
                getTvLeftTitle().setText(getString(R.string.newteetimes_agents));
                break;
            case "3":
                getTvLeftTitle().setText(getString(R.string.newteetimes_events));
                break;

            case "4":
                getTvLeftTitle().setText(getString(R.string.newteetimes_no_members));
                break;
        }


        getTvRight().setBackground(getResources().getDrawable(R.drawable.icon_shop_list_add));


        ImageView ivShop = new ImageView(getActivity());
        ivShop.setImageResource(R.drawable.icon_arrow_down);
        RelativeLayout parent = (RelativeLayout) getTvRight().getParent();
        parent.addView(ivShop);

        RelativeLayout.LayoutParams ivPackageLayoutParams = (RelativeLayout.LayoutParams) ivShop.getLayoutParams();
        ivPackageLayoutParams.height = (int) (getScreenHeight() * 0.03f);
        ivPackageLayoutParams.width = (int) (getScreenHeight() * 0.03f);
        ivPackageLayoutParams.addRule(RelativeLayout.RIGHT_OF, getTvLeftTitle().getId());
        ivPackageLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivPackageLayoutParams.leftMargin = 3;
        ivShop.setLayoutParams(ivPackageLayoutParams);


        ImageView ivUnsign = new ImageView(getActivity());
        ivUnsign.setImageResource(R.drawable.btn_unsign);
        parent.addView(ivUnsign);

        RelativeLayout.LayoutParams ivPackageLayout = (RelativeLayout.LayoutParams) ivUnsign.getLayoutParams();
        ivPackageLayout.addRule(RelativeLayout.LEFT_OF, getTvRight().getId());
        ivPackageLayout.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivPackageLayout.rightMargin = getActualWidthOnThisDevice(20);
        ivUnsign.setLayoutParams(ivPackageLayout);
        ivUnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                if (fromAdd != -1) {
                    //客户绑定人员
                    bundle.putString("fromFlag", "itemMemberDetail");
                    bundle.putString("unSignFlag", "1");
                    //客户绑定人员
                    bundle.putString("signType", StringUtils.EMPTY);
                    bundle.putInt("position", fromAdd);
                    bundle.putInt("parentPosition", parentPosition);
                    doBackWithReturnValue(bundle, TeeTimeAddFragment.class);
                }
            }
        });


        if (Utils.isStringNotNullOrEmpty(signId)) {
            ivUnsign.setVisibility(View.VISIBLE);
        } else {
            ivUnsign.setVisibility(View.GONE);
        }
        parent.invalidate();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化SelectPicPopupWindow
                menuWindow = new PopTeeTimesMemberView(getActivity(), isNoEvent, isNoMember, nowSignType, itemsOnClick,addAgent);
                //显示窗口
                menuWindow.showAsDropDown(v, 0, getActualHeightOnThisDevice(20));
            }
        };

        getTvLeftTitle().setOnClickListener(listener);
        ivShop.setOnClickListener(listener);

        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEdit, getActivity())
                        && AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEditNonmember, getActivity());
                if (hasPermission) {
                    Bundle bundle = new Bundle();
                    switch (nowSignType) {
                        case "1":
                            bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeAdd.value());
                            bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeMemberListWithIndexFragment.class.getName());
                            bundle.putString(TransKey.TEE_TIME_MEMBER_TYPE_ID,Constants.CUSTOMER_MEMBER);
                            push(PlayerInfoEditFragment.class, bundle);
                            break;
                        case "2":
                            bundle.putBoolean(TransKey.EVENT_IS_ADD, true);
                            bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeMemberListWithIndexFragment.class.getName());
                            push(AgentsAddEditFragment.class, bundle);
                            break;
                        case "3":
                            bundle.putBoolean(TransKey.EVENT_IS_ADD, true);
                            bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeMemberListWithIndexFragment.class.getName());
                            push(EventsEditFragment.class, bundle);
                            break;

                        case "4":
                            bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeAdd.value());
                            bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeMemberListWithIndexFragment.class.getName());
                            push(PlayerInfoEditFragment.class, bundle);
                            break;
                    }
                } else {
                    AppUtils.showHaveNoPermission(TeeTimeMemberListWithIndexFragment.this);
                }

            }
        });

    }


}
