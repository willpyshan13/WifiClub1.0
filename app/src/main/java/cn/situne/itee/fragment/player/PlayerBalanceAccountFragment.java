/**
 * Project Name: itee
 * File Name:	 PlayerBalanceAccountFragment.java
 * Package Name: cn.situne.itee.fragment.player
 * Date:		 2015-03-22
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.player;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.teetime.TeeTimeAddFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonBalanceAccountGet;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.SelectSaveOrNotPopupWindow;

/**
 * ClassName:PlayerBalanceAccountFragment <br/>
 * Function: member balance account detail fragment. <br/>
 * UI:  04-8-5
 * Date: 2015-03-22 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */

public class PlayerBalanceAccountFragment extends BaseFragment {

    private boolean isEdit;

    private RelativeLayout rlNumContainer;
    private RelativeLayout rlAccountContainer;
    private RelativeLayout rlPassCodeContainer;
    private RelativeLayout rlPrepayContainer;
    private RelativeLayout rlOverdraftContainer;
    private RelativeLayout rlTimeLimitContainer;
    private LinearLayout rlHistoryContainer;
    private LinearLayout rlHistoryContainerAll;

    private IteeTextView tvNum;
    private IteeTextView tvNumValue;

    private IteeTextView tvAccount;
    private IteeEditText tvAccountValue;

    private IteeTextView tvPassCode;
    private IteeEditText tvPassCodeValue;

    private IteeTextView tvPrePay;
    private IteeTextView tvPrePayValue;

    private IteeTextView tvOverDraft;
    private IteeTextView tvOverDraftValue;

    private IteeTextView tvTimeLimit;
    private IteeTextView tvTimeLimitValue;

    private JsonBalanceAccountGet dataParameter;
    private Integer memberId;
    private Integer agentId;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_player_info_balance_account;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.do_edit_administration;
    }

    /**
     * addHistoryView:added  balance account recharge history view.
     */
    private void addHistoryView(List<JsonBalanceAccountGet.RecordsItem> records) {
        if (records == null || records.size() == 0) {
            rlHistoryContainer.setVisibility(View.GONE);
            rlHistoryContainerAll.setVisibility(View.GONE);
        } else {
            rlHistoryContainer.setVisibility(View.VISIBLE);
            rlHistoryContainerAll.setVisibility(View.VISIBLE);
            for (int i = 0; i < records.size(); i++) {
                JsonBalanceAccountGet.RecordsItem recordsItem = records.get(i);

                RelativeLayout allContainer = new RelativeLayout(getActivity());
                IteeTextView tvDate = new IteeTextView(getActivity());
                IteeTextView money = new IteeTextView(getActivity());
                IteeTextView tvRefund = new IteeTextView(getActivity());
                tvDate.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(recordsItem.getDate(), mContext));
                if (recordsItem.getPayStatus() != null && recordsItem.getPayStatus() == 0) {
                    money.setTextColor(getColor(R.color.common_red));
                }
                money.setText(AppUtils.getCurrentCurrency(getActivity()) + recordsItem.getAmount());
                allContainer.addView(tvDate);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvDate.getLayoutParams();
                layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                layoutParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
                layoutParams.leftMargin = getActualHeightOnThisDevice(40);
                tvDate.setLayoutParams(layoutParams);
                tvDate.setId(View.generateViewId());

                allContainer.addView(tvRefund);

                RelativeLayout.LayoutParams layoutParamsRefund = (RelativeLayout.LayoutParams) tvRefund.getLayoutParams();
                layoutParamsRefund.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                layoutParamsRefund.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                layoutParamsRefund.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
                layoutParamsRefund.addRule(RelativeLayout.RIGHT_OF, tvDate.getId());
                layoutParamsRefund.leftMargin = getActualHeightOnThisDevice(40);
                tvRefund.setLayoutParams(layoutParamsRefund);
                if (recordsItem.getRefundFlg().intValue() == 1){
                    tvRefund.setText(getString(R.string.player_info_purchase_refund));
                }
                allContainer.addView(money);
                RelativeLayout.LayoutParams layoutParamsLeft = (RelativeLayout.LayoutParams) money.getLayoutParams();
                layoutParamsLeft.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                layoutParamsLeft.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                layoutParamsLeft.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
                layoutParamsLeft.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
                layoutParamsLeft.rightMargin = getActualHeightOnThisDevice(40);
                money.setLayoutParams(layoutParamsLeft);

                AppUtils.addBottomSeparatorLine(allContainer, PlayerBalanceAccountFragment.this);
                //set custom view
                rlHistoryContainer.addView(allContainer);
                LinearLayout.LayoutParams paramsAddressWarn = (LinearLayout.LayoutParams) allContainer.getLayoutParams();
                paramsAddressWarn.height = (int) (getScreenHeight() * 0.08f);

                allContainer.setLayoutParams(paramsAddressWarn);
                allContainer.setBackgroundColor(getColor(R.color.common_white));

            }
        }


    }

    @Override
    protected void initControls(View rootView) {

        rlNumContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_balance_account_num);
        rlAccountContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_balance_account);
        rlPassCodeContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_balance_passcode);
        rlPrepayContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_balance_pre_pay);
        rlOverdraftContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_balance_overdraft);
        rlTimeLimitContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_balance_time_limit);
        rlHistoryContainer = (LinearLayout) rootView.findViewById(R.id.rl_play_balance_history);
        rlHistoryContainerAll = (LinearLayout) rootView.findViewById(R.id.rl_play_balance_history_container);

        tvNum = new IteeTextView(getActivity());
        tvNumValue = new IteeTextView(getActivity());

        tvAccount = new IteeTextView(getActivity());
        tvAccountValue = new IteeEditText(getActivity());

        tvPassCode = new IteeTextView(getActivity());
        tvPassCodeValue = new IteeEditText(getActivity());
        tvAccountValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        tvPassCodeValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);

        tvPrePay = new IteeTextView(getActivity());
        tvPrePayValue = new IteeTextView(getActivity());

        tvOverDraft = new IteeTextView(getActivity());
        tvOverDraftValue = new IteeTextView(getActivity());

        tvTimeLimit = new IteeTextView(getActivity());
        tvTimeLimitValue = new IteeTextView(getActivity());
    }

    @Override
    protected void setDefaultValueOfControls() {

        if (dataParameter != null) {
            //set balance account data to layout.
            JsonBalanceAccountGet.DataList dataList = dataParameter.getDataList();
            String currency = AppUtils.getCurrentCurrency(getActivity());
            tvNumValue.setText(currency + dataList.getBalanceAccount());
            tvAccountValue.setText(dataList.getAccount());
            tvPassCodeValue.setText(dataList.getPassword());
            tvPrePayValue.setText(currency + dataList.getPrePay());
            tvOverDraftValue.setText(currency + dataList.getOverDraft());
            tvTimeLimitValue.setText(String.valueOf(dataList.getTimeLimit()) + getString(R.string.player_info_day));

            tvAccountValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    setOnBackListener(new OnPopBackListener() {
                        @Override
                        public boolean preDoBack() {

                            TeeTimeAddFragment.OnPopupWindowWheelClickListener onPopupWindowWheelClickListener = new TeeTimeAddFragment.OnPopupWindowWheelClickListener() {
                                @Override
                                public void onSaveClick(String a, String b) {
                                    setOnBackListener(null);

                                    //netLinkEditAccount();
                                    if (memberId > 0) {
                                        netLinkEditAccount();

                                    } else {
                                        netAgentEditAccount();
                                    }
                                }
                            };
                            SelectSaveOrNotPopupWindow savePopupWindow
                                    = new SelectSaveOrNotPopupWindow(PlayerBalanceAccountFragment.this, onPopupWindowWheelClickListener, 1, null, null);
                            View rootView = getView();
                            if (rootView != null) {
                                savePopupWindow.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                            }
                            return false;
                        }
                    });
                }
            });

            tvPassCodeValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    setOnBackListener(new OnPopBackListener() {
                        @Override
                        public boolean preDoBack() {

                            TeeTimeAddFragment.OnPopupWindowWheelClickListener onPopupWindowWheelClickListener = new TeeTimeAddFragment.OnPopupWindowWheelClickListener() {
                                @Override
                                public void onSaveClick(String a, String b) {
                                    setOnBackListener(null);

                                    //netLinkEditAccount();
                                    if (memberId > 0) {
                                        netLinkEditAccount();

                                    } else {
                                        netAgentEditAccount();
                                    }
                                }
                            };
                            SelectSaveOrNotPopupWindow savePopupWindow
                                    = new SelectSaveOrNotPopupWindow(PlayerBalanceAccountFragment.this, onPopupWindowWheelClickListener, 1, null, null);
                            View rootView = getView();
                            if (rootView != null) {
                                savePopupWindow.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                            }
                            return false;
                        }
                    });
                }
            });

            addHistoryView(dataList.getRecords());


        } else {
            //init layout
            tvNum.setText(getString(R.string.player_info_balance_account_remaining));
            tvAccount.setText(getString(R.string.player_info_balance_account));
            tvPassCode.setText(getString(R.string.player_info_balance_account_passcode));
            tvPrePay.setText(getString(R.string.player_info_balance_account_pre_pay));
            tvOverDraft.setText(getString(R.string.player_info_balance_account_overdraft));
            tvTimeLimit.setText(getString(R.string.player_info_balance_account_time_limit));
        }


    }

    @Override
    protected void setListenersOfControls() {


    }

    @Override
    protected void setLayoutOfControls() {


        LinearLayout.LayoutParams rlPhotoLayout = (LinearLayout.LayoutParams) rlNumContainer.getLayoutParams();
        rlPhotoLayout.height = (int) (getScreenHeight() * 0.08f);


        rlNumContainer.setLayoutParams(rlPhotoLayout);
        rlAccountContainer.setLayoutParams(rlPhotoLayout);
        rlPassCodeContainer.setLayoutParams(rlPhotoLayout);
        rlPrepayContainer.setLayoutParams(rlPhotoLayout);
        rlOverdraftContainer.setLayoutParams(rlPhotoLayout);
        rlTimeLimitContainer.setLayoutParams(rlPhotoLayout);

        rlNumContainer.setPadding(getActualHeightOnThisDevice(20), 0, getActualHeightOnThisDevice(20), 0);
        rlAccountContainer.setPadding(getActualHeightOnThisDevice(20), 0, getActualHeightOnThisDevice(20), 0);
        rlPassCodeContainer.setPadding(getActualHeightOnThisDevice(20), 0, getActualHeightOnThisDevice(20), 0);
        rlPrepayContainer.setPadding(getActualHeightOnThisDevice(20), 0, getActualHeightOnThisDevice(20), 0);
        rlOverdraftContainer.setPadding(getActualHeightOnThisDevice(20), 0, getActualHeightOnThisDevice(20), 0);
        rlTimeLimitContainer.setPadding(getActualHeightOnThisDevice(20), 0, getActualHeightOnThisDevice(20), 0);


        rlNumContainer.addView(tvNum);
        RelativeLayout.LayoutParams paramsEtLogoutAfterTimeM = (RelativeLayout.LayoutParams) tvNum.getLayoutParams();
        paramsEtLogoutAfterTimeM.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsEtLogoutAfterTimeM.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsEtLogoutAfterTimeM.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsEtLogoutAfterTimeM.setMargins(getActualHeightOnThisDevice(20), 0, 0, 0);
        tvNum.setLayoutParams(paramsEtLogoutAfterTimeM);


        rlNumContainer.addView(tvNumValue);
        RelativeLayout.LayoutParams paramsPhotoImageViewValue = (RelativeLayout.LayoutParams) tvNumValue.getLayoutParams();
        paramsPhotoImageViewValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsPhotoImageViewValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsPhotoImageViewValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsPhotoImageViewValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);

        tvNumValue.setLayoutParams(paramsPhotoImageViewValue);
        tvNumValue.setPadding(0, 0, getActualHeightOnThisDevice(20), 0);

        rlAccountContainer.addView(tvAccount);
        RelativeLayout.LayoutParams paramsName = (RelativeLayout.LayoutParams) tvAccount.getLayoutParams();
        paramsName.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsName.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsName.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsName.setMargins(getActualHeightOnThisDevice(20), 0, 0, 0);
        tvAccount.setLayoutParams(paramsName);


        rlAccountContainer.addView(tvAccountValue);
        RelativeLayout.LayoutParams paramsNameValue = (RelativeLayout.LayoutParams) tvAccountValue.getLayoutParams();
        paramsNameValue.width = (int) (getScreenWidth() * 0.3f);
        paramsNameValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsNameValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsNameValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvAccountValue.setLayoutParams(paramsNameValue);

        rlPassCodeContainer.addView(tvPassCode);
        RelativeLayout.LayoutParams paramsOccupation = (RelativeLayout.LayoutParams) tvPassCode.getLayoutParams();
        paramsOccupation.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsOccupation.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsOccupation.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsOccupation.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsOccupation.setMargins(getActualHeightOnThisDevice(20), 0, 0, 0);
        tvPassCode.setLayoutParams(paramsOccupation);


        rlPassCodeContainer.addView(tvPassCodeValue);
        RelativeLayout.LayoutParams paramsOccupationValue = (RelativeLayout.LayoutParams) tvPassCodeValue.getLayoutParams();
        paramsOccupationValue.width = (int) (getScreenWidth() * 0.3f);
        paramsOccupationValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsOccupationValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsOccupationValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvPassCodeValue.setLayoutParams(paramsOccupationValue);

        tvPassCodeValue.setPadding(0, 0, getActualHeightOnThisDevice(20), 0);


        rlPrepayContainer.addView(tvPrePay);
        RelativeLayout.LayoutParams paramsPrePay = (RelativeLayout.LayoutParams) tvPrePay.getLayoutParams();
        paramsPrePay.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsPrePay.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsPrePay.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsPrePay.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsPrePay.setMargins(getActualHeightOnThisDevice(20), 0, 0, 0);
        tvPrePay.setLayoutParams(paramsPrePay);


        rlPrepayContainer.addView(tvPrePayValue);
        RelativeLayout.LayoutParams paramsPrePayValue = (RelativeLayout.LayoutParams) tvPrePayValue.getLayoutParams();
        paramsPrePayValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsPrePayValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsPrePayValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsPrePayValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvPrePayValue.setLayoutParams(paramsPrePayValue);
        tvPrePayValue.setPadding(0, 0, getActualHeightOnThisDevice(20), 0);


        rlOverdraftContainer.addView(tvOverDraft);
        RelativeLayout.LayoutParams paramsBirth = (RelativeLayout.LayoutParams) tvOverDraft.getLayoutParams();
        paramsBirth.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBirth.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBirth.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsBirth.setMargins(getActualHeightOnThisDevice(20), 0, 0, 0);
        tvOverDraft.setLayoutParams(paramsBirth);


        rlOverdraftContainer.addView(tvOverDraftValue);
        RelativeLayout.LayoutParams paramsBirthValue = (RelativeLayout.LayoutParams) tvOverDraftValue.getLayoutParams();
        paramsBirthValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBirthValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBirthValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsBirthValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvOverDraftValue.setLayoutParams(paramsBirthValue);

        tvOverDraftValue.setPadding(0, 0, getActualHeightOnThisDevice(20), 0);


        rlTimeLimitContainer.addView(tvTimeLimit);
        RelativeLayout.LayoutParams paramsGenderInfo = (RelativeLayout.LayoutParams) tvTimeLimit.getLayoutParams();
        paramsGenderInfo.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsGenderInfo.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsGenderInfo.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsGenderInfo.setMargins(getActualHeightOnThisDevice(20), 0, 0, 0);
        tvTimeLimit.setLayoutParams(paramsGenderInfo);


        rlTimeLimitContainer.addView(tvTimeLimitValue);
        RelativeLayout.LayoutParams paramsGenderInfoValue = (RelativeLayout.LayoutParams) tvTimeLimitValue.getLayoutParams();
        paramsGenderInfoValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsGenderInfoValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsGenderInfoValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsGenderInfoValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvTimeLimitValue.setLayoutParams(paramsGenderInfoValue);
        tvTimeLimitValue.setPadding(0, 0, getActualHeightOnThisDevice(20), 0);
    }

    @Override
    protected void setPropertyOfControls() {

        tvNum.setTextColor(getColor(R.color.common_black));
        tvNumValue.setTextColor(getColor(R.color.common_black));

        tvAccount.setTextColor(getColor(R.color.common_black));

        tvAccountValue.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        tvAccountValue.setSingleLine();
        tvAccountValue.setBackground(null);
        tvPassCodeValue.setSingleLine();
        tvPassCodeValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        tvPassCodeValue.setBackground(null);
        tvPassCode.setTextColor(getColor(R.color.common_black));
        tvPrePay.setTextColor(getColor(R.color.common_black));
        tvPrePayValue.setTextColor(getColor(R.color.common_black));

        tvOverDraft.setTextColor(getColor(R.color.common_black));
        tvOverDraftValue.setTextColor(getColor(R.color.common_black));

        tvTimeLimit.setTextColor(getColor(R.color.common_black));
        tvTimeLimitValue.setTextColor(getColor(R.color.common_black));
    }

    /**
     * reset layout. edit mode.
     */
    private void resetView() {
        //set layout
        tvAccountValue.setEnabled(isEdit);
        tvPassCodeValue.setEnabled(isEdit);
        if (isEdit) {
            tvAccountValue.setTextColor(getColor(R.color.common_black));
            tvPassCodeValue.setTextColor(getColor(R.color.common_black));
            getTvRight().setBackground(null);
            getTvRight().setText(R.string.common_ok);
        } else {
            tvAccountValue.setTextColor(getColor(R.color.common_gray));
            tvPassCodeValue.setTextColor(getColor(R.color.common_gray));
            getTvRight().setBackgroundResource(R.drawable.administration_icon_edit);
            getTvRight().setText(StringUtils.EMPTY);
        }

    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        resetView();


        getTvLeftTitle().setText(getResources().getString(R.string.player_info_balance_account_title));

        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEdit = !isEdit;
                resetView();
                if (!isEdit) {
                    if (memberId > 0) {
                        netLinkEditAccount();
                    } else {
                        netAgentEditAccount();
                    }
                    setOnBackListener(null);
                }

            }
        });
    }

    @Override
    protected void executeOnceOnCreate() {
        if (memberId != null && memberId > 0) {
            netLinkBalanceAccount();

        } else {
            netAgentBalanceAccount();
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
     * get balaceAccount data.
     */

    private void netLinkBalanceAccount() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.PLAYER_MEMBER_ID, String.valueOf(memberId));


        HttpManager<JsonBalanceAccountGet> hh = new HttpManager<JsonBalanceAccountGet>(PlayerBalanceAccountFragment.this) {

            @Override
            public void onJsonSuccess(JsonBalanceAccountGet jo) {
                dataParameter = jo;

                setDefaultValueOfControls();
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.BalanceAccount, params);
    }

    private void netAgentBalanceAccount() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.AGENT_AGENT_ID, String.valueOf(agentId));


        HttpManager<JsonBalanceAccountGet> hh = new HttpManager<JsonBalanceAccountGet>(PlayerBalanceAccountFragment.this) {

            @Override
            public void onJsonSuccess(JsonBalanceAccountGet jo) {
                dataParameter = jo;

                setDefaultValueOfControls();

            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.BalanceAccountAgentGet, params);
    }

    private void netLinkEditAccount() {


        Map<String, String> params = new HashMap<>();

        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.PLAYER_MEMBER_ID, String.valueOf(memberId));
        params.put(ApiKey.PLAYER_BALANCE_ACCOUNT_ACCOUNT, tvAccountValue.getText().toString());
        params.put(ApiKey.PLAYER_BALANCE_ACCOUNT_PASSWORD, tvPassCodeValue.getText().toString());

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(PlayerBalanceAccountFragment.this) {
            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    doBack();
                } else {
                    Utils.showShortToast(mContext, msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.BalanceAccount, params);
    }

    private void netAgentEditAccount() {


        Map<String, String> params = new HashMap<>();

        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.AGENT_AGENT_ID, String.valueOf(agentId));
        params.put(ApiKey.AGENT_BALANCE_ACCOUNT, tvAccountValue.getText().toString());
        params.put(ApiKey.AGENT_BALANCE_PASS_WORD, tvPassCodeValue.getText().toString());

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(PlayerBalanceAccountFragment.this) {
            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                Utils.showShortToast(getActivity(), msg);
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    getTvRight().setBackgroundResource(R.drawable.administration_icon_edit);
                    getTvRight().setText(StringUtils.EMPTY);
                    doBack();
                }

            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.startPut(getActivity(), ApiManager.HttpApi.BalanceAccountAgentPut, params);
    }

    /**
     * 单击事件监听器
     */

    public interface OnDateSelectClickListener {
        void OnGoodItemClick(String flag, String content);
    }

}
