/**
 * Project Name: itee
 * File Name:  PlayerPastBookingFragment.java
 * Package Name: cn.situne.itee.fragment.player
 * Date:   2015-03-28
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.teetime.TeeTimeAddFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonPastBooking;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:PlayerPastBookingFragment <br/>
 * Function: member's pastbooking list fragment. <br/>
 * UI:  04-8-8
 * Date: 2015-03-28 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */

public class PlayerPastBookingFragment extends BaseFragment {

    private LinearLayout rlGoodListContainer;
    private Integer memberId;
    private Integer agentId;
    private JsonPastBooking dataParameter;


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
     */
    private void addGoodListView() {
        List<JsonPastBooking.DataListItem> dataList = dataParameter.getDataList();
        for (int i = 0; i < dataList.size(); i++) {

            JsonPastBooking.DataListItem dataListItem = dataList.get(i);
            RelativeLayout allContainer = new RelativeLayout(getActivity());

            View viewLine = new View(getActivity());
            IteeTextView tvDate = new IteeTextView(getActivity());
            IteeTextView inOrOut = new IteeTextView(getActivity());

            IteeTextView tvCopyRight = new IteeTextView(getActivity());
            ImageView arrow = new ImageView(getActivity());
            tvCopyRight.setText("®");
            tvCopyRight.setTextSize(Constants.FONT_SIZE_NORMAL);

            viewLine.setBackgroundColor(getColor(R.color.common_gray));
            arrow.setImageResource(R.drawable.icon_right_arrow);

            tvDate.setTextColor(getColor(R.color.common_black));

            String date = DateUtils.getCurrentShowYearMonthDayFromApiDateStr(dataListItem.getDate(), mContext);
            String dateTime = date + Constants.STR_SPACE + dataListItem.getTime();
            tvDate.setText(dateTime);

            inOrOut.setText(dataListItem.getArea());

//            allContainer.setBackgroundColor(getColor(R.color.common_white));//原来白色背景
            allContainer.setBackgroundResource(R.drawable.bg_linear_selector_color_white);//修改后的点击变色
            allContainer.addView(tvDate);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvDate.getLayoutParams();
            layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, LAYOUT_TRUE);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
            tvDate.setPadding(DensityUtil.getActualWidthOnThisDevice(40, mContext), 0, 0, 0);
            tvDate.setLayoutParams(layoutParams);
            tvDate.setId(View.generateViewId());

            if (dataListItem.getBookingStatus() == 0) {
                allContainer.addView(tvCopyRight);
                RelativeLayout.LayoutParams layoutParamsR = (RelativeLayout.LayoutParams) tvCopyRight.getLayoutParams();
                layoutParamsR.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                layoutParamsR.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                layoutParamsR.leftMargin = DensityUtil.getActualWidthOnThisDevice(20, mContext);
                layoutParamsR.addRule(RelativeLayout.ALIGN_BASELINE, tvDate.getId());
                layoutParamsR.addRule(RelativeLayout.RIGHT_OF, tvDate.getId());
                tvCopyRight.setLayoutParams(layoutParamsR);
            }

            allContainer.addView(inOrOut);
            RelativeLayout.LayoutParams layoutParamsLeft = (RelativeLayout.LayoutParams) inOrOut.getLayoutParams();
            layoutParamsLeft.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            layoutParamsLeft.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            layoutParamsLeft.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
            layoutParamsLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
            layoutParamsLeft.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, LAYOUT_TRUE);
            inOrOut.setLayoutParams(layoutParamsLeft);

            allContainer.addView(arrow);
            RelativeLayout.LayoutParams layoutParamsRightArrow = (RelativeLayout.LayoutParams) arrow.getLayoutParams();
            layoutParamsRightArrow.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            layoutParamsRightArrow.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            layoutParamsRightArrow.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
            layoutParamsRightArrow.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
            layoutParamsRightArrow.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
            arrow.setLayoutParams(layoutParamsRightArrow);

            allContainer.setTag(dataListItem.getBookingOrderNo());
            allContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(TransKey.BOOKING_ORDER_NO, (String) v.getTag());
                    bundle.putBoolean("isAdd", false);

                    bundle.putString(TransKey.COMMON_FROM_PAGE, PlayerPastBookingFragment.class.getName());
                    push(TeeTimeAddFragment.class, bundle);
                }
            });
            //set custom view
            rlGoodListContainer.addView(allContainer);
            LinearLayout.LayoutParams paramsAddressWarn = (LinearLayout.LayoutParams) allContainer.getLayoutParams();
            paramsAddressWarn.height = (int) (getScreenHeight() * 0.08f);
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
        getTvLeftTitle().setText(getString(R.string.player_past_booking));
    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();

        if (memberId != null && memberId > 0) {
            netLinkPastBooking();

        } else {
            netAgentPastBooking();
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
     * get pastBooking data.
     */

    private void netLinkPastBooking() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.PLAYER_MEMBER_ID, String.valueOf(memberId));


        HttpManager<JsonPastBooking> hh = new HttpManager<JsonPastBooking>(PlayerPastBookingFragment.this) {

            @Override
            public void onJsonSuccess(JsonPastBooking jo) {
                dataParameter = jo;

                setDefaultValueOfControls();
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.PastBooking, params);
    }

    private void netAgentPastBooking() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.AGENT_AGENT_ID, String.valueOf(agentId));


        HttpManager<JsonPastBooking> hh = new HttpManager<JsonPastBooking>(PlayerPastBookingFragment.this) {

            @Override
            public void onJsonSuccess(JsonPastBooking jo) {
                dataParameter = jo;

                setDefaultValueOfControls();
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.PastBookingAgentGet, params);
    }

    /**
     * 单击事件监听器
     */

    public interface OnDateSelectClickListener {
        void OnGoodItemClick(String flag, String content);
    }
}
