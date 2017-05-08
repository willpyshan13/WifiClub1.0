/**
 * Project Name: itee
 * File Name:	 CustomersMemberListFragment.java
 * Package Name: cn.situne.itee.fragment.customers
 * Date:		 2015-03-26
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.customers;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.player.PlayerFragment;
import cn.situne.itee.fragment.player.PlayerInfoEditFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonMemberList;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.SwipeListView;

/**
 * ClassName:CustomersMemberListFragment <br/>
 * Function: member list of the passed member type id. <br/>
 * Date: 2015-03-26 <br/>
 * UI:11-5
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class CustomersMemberListFragment extends BaseFragment {

    private LinearLayout llContainer;
    private SwipeListView slMemberList;
    private ArrayList<JsonMemberList.Member> memberArrayList;
    private CustomersMemberListAdapter memberListAdapter;
    private AppUtils.NoDoubleClickListener listenerAddProfile;

    private int memberTypeId;
    private String memberTypeName;
    private String memberTypeTypeId;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_customers_member_list;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        llContainer = (LinearLayout) rootView.findViewById(R.id.ll_container);
        slMemberList = new SwipeListView(getActivity());
        memberArrayList = new ArrayList<>();
        memberListAdapter = new CustomersMemberListAdapter(this, memberArrayList);


        Bundle bundle = getArguments();
        if (bundle != null) {
            memberTypeId = bundle.getInt(TransKey.CUSTOMERS_MEMBER_TYPE_ID);
            memberTypeName = bundle.getString(TransKey.CUSTOMERS_MEMBER_TYPE_NAME);
            memberTypeTypeId = bundle.getString(TransKey.CUSTOMERS_MEMBER_TYPE_TYPE_ID);
        }
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        memberListAdapter.setLeftClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                int memberId = (int) v.getTag();
                Bundle bundle = new Bundle();
                bundle.putInt(TransKey.COMMON_MEMBER_ID, memberId);
                bundle.putString(TransKey.COMMON_FROM_PAGE,CustomersMemberListFragment.class.getName());
                push(PlayerFragment.class, bundle);
            }
        });

        memberListAdapter.setRightClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(final View v) {
                boolean hasPermission;
                if (Constants.CUSTOMER_NON_MEMBER.equals(memberTypeTypeId)) {
                    hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEditNonmember, mContext);
                } else {
                    hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEditMember, mContext);
                }
                if (hasPermission) {
                    final int memberId = (Integer) v.getTag();
                    AppUtils.showDeleteAlert(CustomersMemberListFragment.this, new AppUtils.DeleteConfirmListener() {
                        @Override
                        public void onClickDelete() {
                            deleteMember(memberId);
                        }
                    });
                } else {
                    AppUtils.showHaveNoPermission(CustomersMemberListFragment.this);
                }
            }
        });

        listenerAddProfile = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEdit, getActivity());

                if (Constants.CUSTOMER_MEMBER.equals(memberTypeTypeId)) {
                    hasPermission = hasPermission && AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEditNonmember, getActivity());
                } else {
                    hasPermission = hasPermission && AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEditMember, getActivity());
                }

                if (hasPermission) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeAdd.value());
                    if (Constants.CUSTOMER_MEMBER.equals(memberTypeTypeId)) {
                        bundle.putString(TransKey.TEE_TIME_MEMBER_TYPE_ID, String.valueOf(memberTypeId));
                    }
                    push(PlayerInfoEditFragment.class, bundle);
                } else {
                    AppUtils.showHaveNoPermission(CustomersMemberListFragment.this);
                }
            }
        };
    }

    @Override
    protected void setLayoutOfControls() {
        llContainer.addView(slMemberList);
        LinearLayout.LayoutParams llContainerLayoutParams = new LinearLayout.LayoutParams(getScreenWidth(), MATCH_PARENT);
        slMemberList.setLayoutParams(llContainerLayoutParams);
    }

    @Override
    protected void setPropertyOfControls() {
        slMemberList.setAdapter(memberListAdapter);
        slMemberList.setRightViewWidth(AppUtils.getRightButtonWidth(mContext));
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(memberTypeName);
        getTvRight().setBackgroundResource(R.drawable.icon_common_add);
        getTvRight().setOnClickListener(listenerAddProfile);
    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        getMemberList();
    }

    private void getMemberList() {

        memberArrayList.clear();
        memberListAdapter.notifyDataSetChanged();

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        params.put(ApiKey.CUSTOMERS_MEMBER_TYPE_ID, String.valueOf(memberTypeId));
        params.put(ApiKey.CUSTOMERS_MEMBER_TYPE_TYPE_ID, memberTypeTypeId);
        HttpManager<JsonMemberList> hh = new HttpManager<JsonMemberList>(CustomersMemberListFragment.this) {

            @Override
            public void onJsonSuccess(JsonMemberList jo) {
                int returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    if (jo.getDataList().size() > 0) {
                        memberArrayList.addAll(jo.getDataList());
                        memberListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
            }
        };
        hh.startGet(getBaseActivity(), ApiManager.HttpApi.MemberListGet, params);
    }

    private void deleteMember(int memberId) {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        params.put(ApiKey.CUSTOMERS_MEMBER_ID, String.valueOf(memberId));
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(CustomersMemberListFragment.this) {
            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                Integer returnCode = jo.getReturnCode();

                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20403_DELETE_SUCCESSFULLY) {
                    Utils.showShortToast(getActivity(), msg);
                    getMemberList();
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
        hh.startDelete(this.getActivity(), ApiManager.HttpApi.MemberListDelete, params);
    }

    class CustomersMemberListAdapter extends BaseAdapter {

        private BaseFragment mBaseFragment;
        private ArrayList<JsonMemberList.Member> memberArrayList;
        private View.OnClickListener rightClickListener;
        private View.OnClickListener leftClickListener;

        public CustomersMemberListAdapter(BaseFragment mBaseFragment, ArrayList<JsonMemberList.Member> memberArrayList) {
            this.mBaseFragment = mBaseFragment;
            this.memberArrayList = memberArrayList;
        }

        @Override
        public int getCount() {
            if (memberArrayList != null) {
                return memberArrayList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            JsonMemberList.Member member = memberArrayList.get(position);

            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mBaseFragment.getActivity()).inflate(R.layout.list_row_customer_member, parent, false);
                holder = new ViewHolder();
                holder.ivPhoto = (NetworkImageView) convertView.findViewById(R.id.iv_icon);
                holder.itemLeft = (RelativeLayout) convertView.findViewById(R.id.item_left);
                holder.itemRight = (LinearLayout) convertView.findViewById(R.id.item_right);
                holder.itemRightTxt = (IteeTextView) convertView.findViewById(R.id.item_right_txt);
                holder.itemRightAdd = (IteeTextView) convertView.findViewById(R.id.item_right_refund);
                holder.itemRightDelete = (IteeTextView) convertView.findViewById(R.id.item_right_delete);
                holder.tvTelValue = (IteeTextView) convertView.findViewById(R.id.tv_tel_value);
                holder.tvMemberNo = (IteeTextView) convertView.findViewById(R.id.tv_member_no);
                holder.tvMemberName = (IteeTextView) convertView.findViewById(R.id.tv_member_name);
                holder.tvBirthValue = (IteeTextView) convertView.findViewById(R.id.tv_birth_value);
                holder.tvZipCode = (IteeTextView) convertView.findViewById(R.id.tv_zip_code);

                holder.itemRightTxt.setGravity(Gravity.CENTER);

                holder.tvTelValue.setTextSize(Constants.FONT_SIZE_SMALLER);
                holder.tvMemberNo.setTextSize(Constants.FONT_SIZE_SMALLER);
                holder.tvMemberName.setTextSize(Constants.FONT_SIZE_SMALLER);
                holder.tvBirthValue.setTextSize(Constants.FONT_SIZE_SMALLER);
                holder.tvZipCode.setTextSize(Constants.FONT_SIZE_SMALLER);
                holder.tvMemberNo.setGravity(Gravity.END);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            formatRow(holder, member);

            return convertView;
        }

        private void formatRow(ViewHolder holder, JsonMemberList.Member member) {

            if (Utils.isStringNotNullOrEmpty(member.getMemberPhoto())) {
                AppUtils.showNetworkImage(holder.ivPhoto, member.getMemberPhoto());
            } else {
                AppUtils.showNetworkImage(holder.ivPhoto, Constants.PHOTO_DEFAULT_URL);
            }

            holder.itemRightTxt.setText(R.string.common_delete);
            holder.itemRightTxt.setBackgroundResource(R.drawable.bg_common_delete);

            ViewGroup.LayoutParams layoutParams = holder.itemRight.getLayoutParams();
            layoutParams.width = AppUtils.getRightButtonWidth(getActivity());
            layoutParams.height = MATCH_PARENT;
            holder.itemRight.setLayoutParams(layoutParams);

            holder.tvTelValue.setText(member.getTelNumber());
            holder.tvMemberNo.setText(String.valueOf(member.getMemberCard()));
            holder.tvMemberName.setText(member.getMemberName());
            holder.tvBirthValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(member.getMemberBirth(), mContext));
            holder.tvZipCode.setText(member.getZipCode());

            holder.itemLeft.setTag(member.getMemberId());
            holder.itemRight.setTag(member.getMemberId());

            holder.itemRightTxt.setTextSize(Constants.FONT_SIZE_NORMAL);

            if (getLeftClickListener() != null) {
                holder.itemLeft.setOnClickListener(getLeftClickListener());
            }
            if (getRightClickListener() != null) {
                holder.itemRight.setOnClickListener(getRightClickListener());
            }
        }

        public View.OnClickListener getRightClickListener() {
            return rightClickListener;
        }

        public void setRightClickListener(View.OnClickListener rightClickListener) {
            this.rightClickListener = rightClickListener;
        }

        public View.OnClickListener getLeftClickListener() {
            return leftClickListener;
        }

        public void setLeftClickListener(View.OnClickListener leftClickListener) {
            this.leftClickListener = leftClickListener;
        }

        class ViewHolder {
            RelativeLayout itemLeft;
            LinearLayout itemRight;
            NetworkImageView ivPhoto;
            IteeTextView itemRightTxt;
            IteeTextView itemRightAdd;
            IteeTextView itemRightDelete;
            IteeTextView tvTelValue;
            IteeTextView tvMemberNo;
            IteeTextView tvMemberName;
            IteeTextView tvBirthValue;
            IteeTextView tvZipCode;
        }

    }
}