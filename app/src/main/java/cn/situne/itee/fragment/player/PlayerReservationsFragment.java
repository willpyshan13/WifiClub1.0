
/**
 * Project Name: itee
 * File Name:  PlayerReservationsFragment.java
 * Package Name: cn.situne.itee.fragment.player
 * Date:   2015-04-01
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.player;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.teetime.LocationListFragment;
import cn.situne.itee.fragment.teetime.TeeTimeAddFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonReservation;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:PlayerReservationsFragment <br/>
 * Function: member's reservation list fragment. <br/>
 * UI:  04-2-2
 * Date: 2015-04-01 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */

public class PlayerReservationsFragment extends BaseFragment {

    private Integer memberId;
    private Integer agentId;


    private LinearLayout rlGoodListContainer;
    private JsonReservation dataParameter;


    /**
     * 单击事件监听器
     */
    public interface OnDateSelectClickListener {
        void OnGoodItemClick(String flag, String content);
    }


    @Override
    protected int getFragmentId() {
        return R.layout.fragment_player_info_reservations;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.do_edit_administration;
    }

    /**
     * addGoodListView:add all good's view.
     *
     */
    private void addGoodListView() {

        ArrayList<JsonReservation.DataListItem> dataList = dataParameter.getDataList();

        for (int i = 0; i < dataList.size(); i++) {

            JsonReservation.DataListItem dataListItem = dataList.get(i);

            RelativeLayout allContainer = new RelativeLayout(getActivity());

            View viewLine = new View(getActivity());
            IteeTextView tvDateAndGapTime = new IteeTextView(this);
            IteeTextView inOrOut = new IteeTextView(this);
            ImageView arrow = new ImageView(getActivity());

            viewLine.setBackgroundColor(getColor(R.color.common_gray));
            arrow.setImageDrawable(getDrawable(R.drawable.icon_black));
            tvDateAndGapTime.setTextColor(getColor(R.color.common_black));

            String date = DateUtils.getCurrentShowYearMonthDayFromApiDateStr(dataListItem.getDate(), mContext);
            String dateAndGapTimes = date + Constants.STR_SPACE + dataListItem.getTime();
            tvDateAndGapTime.setText(dateAndGapTimes);
            tvDateAndGapTime.setSingleLine(true);
            tvDateAndGapTime.setHorizontallyScrolling(true);
            tvDateAndGapTime.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            tvDateAndGapTime.setMovementMethod(ScrollingMovementMethod.getInstance());

            inOrOut.setText(dataListItem.getArea());

            allContainer.addView(tvDateAndGapTime);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvDateAndGapTime.getLayoutParams();
            layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.width = (int) (getScreenWidth() * 0.8);
            layoutParams.topMargin = getActualHeightOnThisDevice(10);
            layoutParams.leftMargin = getActualHeightOnThisDevice(20);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, LAYOUT_TRUE);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
            tvDateAndGapTime.setLayoutParams(layoutParams);


            allContainer.addView(inOrOut);
            RelativeLayout.LayoutParams layoutParamsLeft = (RelativeLayout.LayoutParams) inOrOut.getLayoutParams();
            layoutParamsLeft.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            layoutParamsLeft.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            layoutParamsLeft.bottomMargin = getActualHeightOnThisDevice(10);
            layoutParamsLeft.leftMargin = getActualHeightOnThisDevice(20);
            layoutParamsLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
            layoutParamsLeft.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, LAYOUT_TRUE);
            inOrOut.setLayoutParams(layoutParamsLeft);

            allContainer.addView(arrow);
            RelativeLayout.LayoutParams layoutParamsLeftCount = (RelativeLayout.LayoutParams) arrow.getLayoutParams();
            layoutParamsLeftCount.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            layoutParamsLeftCount.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            layoutParamsLeftCount.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
            layoutParamsLeftCount.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
            arrow.setLayoutParams(layoutParamsLeftCount);

            allContainer.setTag(dataListItem.getBookingOrderNo());
            allContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();

                    bundle.putString(TransKey.BOOKING_ORDER_NO, (String) v.getTag());
                    bundle.putBoolean("isAdd", false);

                    bundle.putString(TransKey.COMMON_FROM_PAGE, PlayerReservationsFragment.class.getName());
                    push(TeeTimeAddFragment.class, bundle);
                }
            });


            //set custom view
            rlGoodListContainer.addView(allContainer);
            LinearLayout.LayoutParams paramsAddressWarn = (LinearLayout.LayoutParams) allContainer.getLayoutParams();
            paramsAddressWarn.height = (int) (getScreenHeight() * 0.08f);
            paramsAddressWarn.width = LinearLayout.LayoutParams.MATCH_PARENT;
            paramsAddressWarn.setMargins(20, 0, 20, 0);
            allContainer.setLayoutParams(paramsAddressWarn);

            //set custom view
            rlGoodListContainer.addView(viewLine);
            LinearLayout.LayoutParams paramsLine = (LinearLayout.LayoutParams) viewLine.getLayoutParams();
            paramsLine.height = 2;
            paramsLine.width = LinearLayout.LayoutParams.MATCH_PARENT;
            viewLine.setLayoutParams(paramsLine);

        }

    }


    @Override
    protected void initControls(View rootView) {
        rlGoodListContainer = (LinearLayout) rootView.findViewById(R.id.rl_play_purchase_history_good_list);
    }

    @Override
    protected void setDefaultValueOfControls() {
        if (dataParameter != null) {
            addGoodListView();
        }
    }

    @Override
    protected void setListenersOfControls() {
    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {


    }

    @Override
    protected void configActionBar() {
        rlGoodListContainer.removeAllViews();
        setStackedActionBar();
        getTvLeftTitle().setText(getString(R.string.play_reservations));
    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        if (memberId != null && memberId > 0) {
            netLinkReservation();
        } else {
            netAgentReservation();
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

    private void netLinkReservation() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.PLAYER_MEMBER_ID, String.valueOf(memberId));


        HttpManager<JsonReservation> hh = new HttpManager<JsonReservation>(PlayerReservationsFragment.this) {

            @Override
            public void onJsonSuccess(JsonReservation jo) {
                dataParameter = jo;

                setDefaultValueOfControls();
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.Reservation, params);
    }

    private void netAgentReservation() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.AGENT_AGENT_ID, String.valueOf(agentId));


        HttpManager<JsonReservation> hh = new HttpManager<JsonReservation>(PlayerReservationsFragment.this) {

            @Override
            public void onJsonSuccess(JsonReservation jo) {
                dataParameter = jo;

                setDefaultValueOfControls();
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.ReservationAgentGet, params);
    }
}
