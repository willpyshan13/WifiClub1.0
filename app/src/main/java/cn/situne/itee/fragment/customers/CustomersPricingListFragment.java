/**
 * Project Name: itee
 * File Name:	 CustomersPricingListFragment.java
 * Package Name: cn.situne.itee.fragment.customers
 * Date:		 2015-03-24
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.customers;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.adapter.CustomerPricingTableListAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonAgentsPricingListGet;
import cn.situne.itee.manager.jsonentity.JsonLifeMember;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.SwipeLinearLayout;
import cn.situne.itee.view.SwipeListView;

/**
 * ClassName:CustomersPricingListFragment <br/>
 * Function: pricing item list of customer. <br/>
 * Date: 2015-03-24 <br/>
 * UI:11-3
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class CustomersPricingListFragment extends BaseFragment {

    private RelativeLayout rlLifeMemberContainer;
    private ListView swipeListMemberView;
    private View.OnClickListener deleteButtonListener;
  //  private CustomerPricingTableListAdapter customerPricingTableListAdapter;

    private ListAdapter listAdapter;
    private int memberTypeId;
    private View.OnClickListener itemOnclickListener;
    private String typeName;

    private String memberTypeTypeId;


    private SwipeLinearLayout selectSwipeLinearLayout;
    @Override
    protected int getFragmentId() {
        return R.layout.fragment_pricing_list;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.customer_edit_title;
    }

    @Override
    protected void initControls(View rootView) {
        rlLifeMemberContainer = (RelativeLayout) rootView.findViewById(R.id.rl_life_member_container);
        swipeListMemberView = new ListView(getActivity());
        listAdapter = new ListAdapter();
        swipeListMemberView.setAdapter(listAdapter);
        Bundle bundle = getArguments();
        if (bundle != null) {
            memberTypeId = bundle.getInt(TransKey.CUSTOMERS_MEMBER_TYPE_ID);
            typeName = bundle.getString(TransKey.CUSTOMERS_MEMBER_TYPE_NAME);
            memberTypeTypeId = bundle.getString(TransKey.CUSTOMERS_MEMBER_TYPE_TYPE_ID, Constants.CUSTOMER_MEMBER);

        }
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        itemOnclickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString(TransKey.AGENTS_MAIN_ID, String.valueOf(v.getTag()));
                bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeBrowse.value());
                bundle.putString(TransKey.CUSTOMERS_MEMBER_TYPE_TYPE_ID, memberTypeTypeId);
                push(CustomersPricingTableDataFragment.class, bundle);
            }
        };
        deleteButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, String> params = new HashMap<>();
                params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
                params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));
                params.put(ApiKey.MEMBER_FREQUENTER_DATA_ID, String.valueOf(view.getTag()));

                final JsonAgentsPricingListGet.PricingItem item = findItem(String.valueOf(view.getTag()));

                HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(CustomersPricingListFragment.this) {

                    @Override
                    public void onJsonSuccess(BaseJsonObject jo) {
                        Integer returnCode = jo.getReturnCode();

                        String msg = jo.getReturnInfo();
                        if (returnCode == Constants.RETURN_CODE_20403_DELETE_SUCCESSFULLY) {
                            memberPricing.remove(item);
                            listAdapter.notifyDataSetChanged();
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
                hh.startDelete(getActivity(), ApiManager.HttpApi.MemberPricingListDelete, params);
            }
        };

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setLayoutOfControls() {

        rlLifeMemberContainer.addView(swipeListMemberView);
        RelativeLayout.LayoutParams paramsSwipeListMemberView = (RelativeLayout.LayoutParams) swipeListMemberView.getLayoutParams();
        paramsSwipeListMemberView.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsSwipeListMemberView.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsSwipeListMemberView.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        swipeListMemberView.setLayoutParams(paramsSwipeListMemberView);

    }


    @Override
    protected void setPropertyOfControls() {
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        if (Utils.isStringNotNullOrEmpty(typeName)) {
            getTvLeftTitle().setText(typeName);
        } else {
            getTvLeftTitle().setText(R.string.customers_pricing_table);
        }
        getTvRight().setVisibility(View.VISIBLE);
        getTvRight().setBackgroundResource(R.drawable.icon_common_add);
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(TransKey.CUSTOMERS_MEMBER_TYPE_ID, memberTypeId);
                bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeAdd.value());
                bundle.putString(TransKey.CUSTOMERS_MEMBER_TYPE_TYPE_ID, memberTypeTypeId);
                getBaseActivity().pushFragment(CustomersPricingTableDataFragment.class, bundle);
            }
        });

    }

    private List<JsonAgentsPricingListGet.PricingItem> memberPricing;
    private  JsonAgentsPricingListGet.PricingItem findItem(String mainId){
        for (JsonAgentsPricingListGet.PricingItem item:memberPricing){

            if (mainId.equals(item.getMainId()))return item;
        }
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        getPricingList();
    }

    private void getPricingList() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));



        if (Constants.CUSTOMER_NON_MEMBER.equals(memberTypeTypeId)){
            params.put(ApiKey.PRICING_TYPE, Constants.PRICING_TYPE_2);

        }else{

            params.put(ApiKey.PRICING_TYPE, Constants.PRICING_TYPE_1);
        }

        params.put(ApiKey.PRICING_TYPE_ID, String.valueOf(memberTypeId));
        HttpManager<JsonAgentsPricingListGet> hh = new HttpManager<JsonAgentsPricingListGet>(CustomersPricingListFragment.this) {

            @Override
            public void onJsonSuccess(JsonAgentsPricingListGet jo) {

                memberPricing = jo.getAgentsPricingList();
                listAdapter.setAgentsPricingList(memberPricing);
                listAdapter.notifyDataSetChanged();
//                customerPricingTableListAdapter = new CustomerPricingTableListAdapter(CustomersPricingListFragment.this,
//                        memberPricing, swipeListMemberView.getRightViewWidth(), memberTypeTypeId);
//                customerPricingTableListAdapter.setItemOnclickListener(itemOnclickListener);
//                customerPricingTableListAdapter.setDeleteButtonListener(deleteButtonListener);
//                swipeListMemberView.setAdapter(customerPricingTableListAdapter);

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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.PricingListPageGetX, params);
    }



    class ListItem extends LinearLayout {

        private SwipeLinearLayout titleLayout;

        public SwipeLinearLayout getTitleLayout() {
            return titleLayout;
        }

        private IteeTextView tvTitleText;
        private IteeTextView tvSubDate;
        private Button delBtn;
        private LinearLayout bodyLayout;

        private View v;

        public ListItem(Context context) {
            super(context);
            this.setOrientation(VERTICAL);
            LinearLayout.LayoutParams titleLayoutParams = new  LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(100));
            titleLayout = new SwipeLinearLayout(getContext(),AppUtils.getRightButtonWidth(getContext()));
            titleLayout.setLayoutParams(titleLayoutParams);
            titleLayout.setOrientation(HORIZONTAL);

            LinearLayout.LayoutParams bodyLayoutParams = new  LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            bodyLayout = new LinearLayout(getContext());
            bodyLayout.setLayoutParams(bodyLayoutParams);
            bodyLayout.setOrientation(VERTICAL);

            setTitleLayout();


            LinearLayout.LayoutParams vParams = new  LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(20));
            v = new View(getContext());
            v.setLayoutParams(vParams);
            v.setBackgroundColor(getColor(R.color.common_light_gray));
            this.addView(v);
            this.addView(AppUtils.getSeparatorLine(getContext()));
            this.addView(AppUtils.getSeparatorLine(getContext()));


            this.addView(titleLayout);
            this.addView(bodyLayout);
        }
        private  RelativeLayout rlTitle;
        private void setTitleLayout(){
            LinearLayout.LayoutParams rlTitleParams = new  LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(100));
            rlTitle = new RelativeLayout(getContext());
            rlTitle.setLayoutParams(rlTitleParams);
            LinearLayout.LayoutParams delBtnParams = new  LinearLayout.LayoutParams(AppUtils.getRightButtonWidth(getContext()),getActualHeightOnThisDevice(100));
            delBtn = new Button(getContext());
            delBtn.setBackgroundResource(R.drawable.bg_common_delete);
            delBtn.setLayoutParams(delBtnParams);

            tvTitleText  =new   IteeTextView(getContext());
            tvSubDate = new IteeTextView(getContext());

            rlTitle.addView(tvTitleText);
            rlTitle.addView(tvSubDate);
            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvTitleText, 20, getContext());
            tvTitleText.setTextColor(mContext.getResources().getColor(R.color.common_blue));


            RelativeLayout.LayoutParams titleParams = (RelativeLayout.LayoutParams)tvTitleText.getLayoutParams();

            titleParams.width = getActualWidthOnThisDevice(500);
            RelativeLayout.LayoutParams paramsTvSubDate = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(500), RelativeLayout.LayoutParams.WRAP_CONTENT);
            paramsTvSubDate.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            paramsTvSubDate.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            paramsTvSubDate.leftMargin = getActualWidthOnThisDevice(20);
            paramsTvSubDate.bottomMargin = getActualHeightOnThisDevice(2);
            tvSubDate.setLayoutParams(paramsTvSubDate);
            tvSubDate.setTextColor(mContext.getResources().getColor(R.color.common_gray));
            tvSubDate.setTextSize(Constants.FONT_SIZE_MORE_SMALLER);

            ImageView rightIcon = new ImageView(getContext());
            rightIcon.setBackgroundResource(R.drawable.icon_right_arrow);
            rlTitle.addView(rightIcon);
            titleLayout.addView(rlTitle);
            titleLayout.addView(delBtn);
            LayoutUtils.setRightArrow(rightIcon, getActualWidthOnThisDevice(20), getContext());

            delBtn.setText(getString(R.string.delete_button));

            delBtn.setTextColor(getColor(R.color.common_white));

            AppUtils.addBottomSeparatorLine(rlTitle, getBaseActivity());

        }

        private void addBodyLayout(JsonAgentsPricingListGet.PricingItem data){
            bodyLayout.removeAllViews();
            data.getPricingDataList();

            for (JsonAgentsPricingListGet.PricingData pricingData:data.getPricingDataList()){
                LinearLayout.LayoutParams rlBodyItemParams = new  LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(100));
                RelativeLayout rlBodyItem = new  RelativeLayout(getContext());
                rlBodyItem.setLayoutParams(rlBodyItemParams);
                IteeTextView tvProduct = new IteeTextView(getContext());
                tvProduct.setText(pricingData.getProductName());
                IteeTextView tvShowMemberMoney = new IteeTextView(getContext());
                tvShowMemberMoney.setGravity(Gravity.BOTTOM|Gravity.RIGHT);

                tvShowMemberMoney.setText(getString(R.string.customers_member)+Constants.STR_SPACE  + AppUtils.getCurrentCurrency(getContext())+Constants.STR_SPACE + Utils.get2DigitDecimalString(pricingData.getMemberPrice()));
                IteeTextView tvShowGuestMoney = new IteeTextView(getContext());
                tvShowGuestMoney.setGravity(Gravity.TOP | Gravity.RIGHT);
                tvShowGuestMoney.setText(getString(R.string.customers_guest)+Constants.STR_SPACE  + AppUtils.getCurrentCurrency(getContext()) +Constants.STR_SPACE + Utils.get2DigitDecimalString(pricingData.getGuestPrice()));
                rlBodyItem.addView(tvProduct);
                rlBodyItem.addView(tvShowMemberMoney);
                rlBodyItem.addView(tvShowGuestMoney);
                bodyLayout.addView(rlBodyItem);
                LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvProduct, 20, getContext());


                RelativeLayout.LayoutParams tvShowMemberMoneyParams = new    RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,getActualHeightOnThisDevice(50));
                tvShowMemberMoneyParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                tvShowMemberMoneyParams.rightMargin = getActualWidthOnThisDevice(40);
                tvShowMemberMoney.setLayoutParams(tvShowMemberMoneyParams);

                RelativeLayout.LayoutParams tvShowGuestMoneyParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,getActualHeightOnThisDevice(50));
                tvShowGuestMoney.setLayoutParams(tvShowGuestMoneyParams);


                tvShowGuestMoneyParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                tvShowGuestMoneyParams.rightMargin = getActualWidthOnThisDevice(40);

                AppUtils.addBottomSeparatorLine(rlBodyItem,getContext());

                if (Constants.CUSTOMER_NON_MEMBER.equals(memberTypeTypeId)){
                    tvShowMemberMoney.setVisibility(View.GONE);
                    tvShowGuestMoneyParams.addRule(RelativeLayout.CENTER_VERTICAL);
                    tvShowGuestMoney.setText(AppUtils.getCurrentCurrency(getContext()) + Utils.get2DigitDecimalString(pricingData.getGuestPrice()));
                }else{

                    tvShowGuestMoneyParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                }

            }

        }


        public void refreshLayout(JsonAgentsPricingListGet.PricingItem data,int position){
            if (position != 0){
                v.setVisibility(View.VISIBLE);
            }else{
                v.setVisibility(View.GONE);
            }
//            ArrayList<String> dateList = AppUtils.changeString2List(data.getDateDescStr(), Constants.STR_COMMA);
//            String etTitle = AppUtils.getEtTimeTitle(dateList, mContext);
            tvTitleText.setText(data.getDateDescStr());
            tvSubDate.setText(data.getTimeInfo());
            delBtn.setTag(data.getMainId());
            titleLayout.setTag(data.getMainId());
            addBodyLayout(data);
        }

        private void setItemListener(OnClickListener selectListener,OnClickListener delListener){


            delBtn.setOnClickListener(delListener);
            rlTitle.setOnClickListener(selectListener);
            rlTitle.setTag(this.getTag());
            this.setOnClickListener(selectListener);
        }
    }
    class ListAdapter extends BaseAdapter {
        private List<JsonAgentsPricingListGet.PricingItem> agentsPricingList;
        public List<JsonAgentsPricingListGet.PricingItem> getAgentsPricingList() {
            return agentsPricingList;
        }

        public void setAgentsPricingList(List<JsonAgentsPricingListGet.PricingItem> agentsPricingList) {
            this.agentsPricingList = agentsPricingList;
        }

        @Override
        public int getCount() {
            if (agentsPricingList ==null) return 0;
            return agentsPricingList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ListItem listItem = null;
            if (view == null) {

                listItem = new ListItem(getBaseActivity());
            }else{

                listItem = (ListItem)view;
            }

            listItem.getTitleLayout().setSwipeLayoutListener(new SwipeLinearLayout.SwipeLayoutListener() {
                @Override
                public void scrollView(View item) {
                    if (selectSwipeLinearLayout != null)
                        selectSwipeLinearLayout.hideRight();
                }
            });


            listItem.getTitleLayout().setAfterShowRightListener(new SwipeLinearLayout.AfterShowRightListener() {
                @Override
                public void doAfterShowRight(SwipeLinearLayout swipeLinearLayout) {
                    selectSwipeLinearLayout = swipeLinearLayout;
                }
            });

            listItem.setTag(agentsPricingList.get(i).getMainId());

            listItem.refreshLayout(agentsPricingList.get(i), i);
            listItem.getTitleLayout().hideRight();
//
            listItem.setItemListener(itemOnclickListener, deleteButtonListener);
            return listItem;
        }
    }

}
