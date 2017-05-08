/**
 * Project Name: itee
 * File Name:	 AgentBalanceAccountFragment.java
 * Package Name: cn.situne.itee.fragment.agent
 * Date:		 2015-05-20
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.agents;


import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonBalanceAccountGet;
import cn.situne.itee.view.IteeDayEditText;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeMoneyEditText;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.SelectOutOrInPopupWindow;

/**
 * ClassName:AgentBalanceAccountFragment <br/>
 * Function: 修改  Balance account  信息 <br/>
 * UI:  从10-1 点击 Balance account 进去
 * Date: 2015-05-20 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */

public class AgentBalanceAccountFragment extends BaseFragment {

    ArrayList<String> beforeDataList;
    private boolean isEdit;
    private RelativeLayout rlNumContainer;
    private RelativeLayout rlAccountContainer;
    private RelativeLayout rlPasscodeContainer;
    private RelativeLayout rlPrepayContainer;
    private RelativeLayout rlOverdraftContainer;
    private RelativeLayout rlTimeLimitContainer;
    private LinearLayout rlHistoryContainer;
    private IteeTextView tvNum;
    private IteeTextView tvNumValue;
    private IteeTextView tvAccount;
    private IteeEditText tvAccountValue;
    private IteeTextView tvPassCode;
    private IteeEditText tvPassCodeValue;
    private IteeTextView tvPrePay;
    private IteeTextView tvPrePayValue;
    private IteeTextView tvOverDraft;
    private IteeMoneyEditText tvOverDraftValue;
    private IteeTextView tvTimeLimit;
    private IteeDayEditText tvTimeLimitValue;
    private JsonBalanceAccountGet dataParameter;
    private Integer agentId;
    private SelectOutOrInPopupWindow selectOutOrInPopupWindow;
    private ScrollView rechargeList;

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
            rechargeList.setVisibility(View.GONE);
        } else {
            rechargeList.setVisibility(View.VISIBLE);
            for (int i = 0; i < records.size(); i++) {
                JsonBalanceAccountGet.RecordsItem recordsItem = records.get(i);

                RelativeLayout allContainer = new RelativeLayout(getActivity());
                IteeTextView date = new IteeTextView(getActivity());
                IteeTextView money = new IteeTextView(getActivity());
                date.setText(recordsItem.getDate());
                if (recordsItem.getPayStatus() != null && recordsItem.getPayStatus() == 0) {
                    money.setTextColor(getColor(R.color.common_red));
                }
                money.setText(AppUtils.getCurrentCurrency(getActivity()) + recordsItem.getAmount());
                allContainer.addView(date);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) date.getLayoutParams();
                layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                layoutParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
                layoutParams.leftMargin = getActualWidthOnThisDevice(40);
                date.setLayoutParams(layoutParams);


                allContainer.addView(money);
                RelativeLayout.LayoutParams layoutParamsLeft = (RelativeLayout.LayoutParams) money.getLayoutParams();
                layoutParamsLeft.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                layoutParamsLeft.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                layoutParamsLeft.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
                layoutParamsLeft.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
                layoutParamsLeft.rightMargin = getActualWidthOnThisDevice(40);
                money.setLayoutParams(layoutParamsLeft);

                AppUtils.addBottomSeparatorLine(allContainer,AgentBalanceAccountFragment.this);

                //set custom view
                rlHistoryContainer.addView(allContainer);
                LinearLayout.LayoutParams paramsAddressWarn = (LinearLayout.LayoutParams) allContainer.getLayoutParams();
                paramsAddressWarn.height = (int) (getScreenHeight() * 0.08f);
                allContainer.setLayoutParams(paramsAddressWarn);

            }
        }


    }


    @Override
    protected void initControls(View rootView) {

        beforeDataList = new ArrayList<>();
        selectOutOrInPopupWindow = new SelectOutOrInPopupWindow(getActivity(), null);
        rlNumContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_balance_account_num);
        rlAccountContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_balance_account);
        rlPasscodeContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_balance_passcode);
        rlPrepayContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_balance_pre_pay);
        rlOverdraftContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_balance_overdraft);
        rlTimeLimitContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_balance_time_limit);
        rlHistoryContainer = (LinearLayout) rootView.findViewById(R.id.rl_play_balance_history);
        rechargeList= (ScrollView) rootView.findViewById(R.id.rechargeList);
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
        tvOverDraftValue = new IteeMoneyEditText(this);

        tvTimeLimit = new IteeTextView(getActivity());
        tvTimeLimitValue = new IteeDayEditText(this);


        tvNumValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);

        tvPrePayValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        tvOverDraftValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        tvTimeLimitValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);

    }

    @Override
    protected void setDefaultValueOfControls() {

        if (dataParameter != null) {
            //set balance account data to layout.
            JsonBalanceAccountGet.DataList dataList = dataParameter.getDataList();

            tvNumValue.setText(AppUtils.getCurrentCurrency(getBaseActivity()) + dataList.getBalanceAccount());
            tvAccountValue.setText(dataList.getAccount());
            tvPassCodeValue.setText(dataList.getPassword());
            tvPrePayValue.setText(AppUtils.getCurrentCurrency(getBaseActivity()) + dataList.getPrePay());
            tvOverDraftValue.setValue(dataList.getOverDraft());
            tvTimeLimitValue.setText(String.valueOf(dataList.getTimeLimit()) + getString(R.string.player_info_day));

//            tvAccountValue.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//
//                    setOnBackListener(new OnPopBackListener() {
//                        @Override
//                        public boolean preDoBack() {
//
//                            TeeTimeAddFragment.OnPopupWindowWheelClickListener onPopupWindowWheelClickListener = new TeeTimeAddFragment.OnPopupWindowWheelClickListener() {
//                                @Override
//                                public void onSaveClick(String a, String b) {
//                                    setOnBackListener(null);
//
//
//                                    netAgentEditAccount();
//
//                                }
//                            };
//                            SelectSaveOrNotPopupWindow savePopupWindow = new SelectSaveOrNotPopupWindow(AgentBalanceAccountFragment.this, onPopupWindowWheelClickListener, 1, null, null);
//                            savePopupWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//                            return false;
//                        }
//                    });
//                }
//            });
//
//            tvPassCodeValue.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//
//                    setOnBackListener(new OnPopBackListener() {
//                        @Override
//                        public boolean preDoBack() {
//
//                            TeeTimeAddFragment.OnPopupWindowWheelClickListener onPopupWindowWheelClickListener = new TeeTimeAddFragment.OnPopupWindowWheelClickListener() {
//                                @Override
//                                public void onSaveClick(String a, String b) {
//                                    setOnBackListener(null);
//
//
//                                    netAgentEditAccount();
//
//                                }
//                            };
//                            SelectSaveOrNotPopupWindow savePopupWindow = new SelectSaveOrNotPopupWindow(AgentBalanceAccountFragment.this, onPopupWindowWheelClickListener, 1, null, null);
//                            savePopupWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//                            return false;
//                        }
//                    });
//                }
//            });

            addHistoryView(dataList.getRecords());


        } else {
            //init layout
            tvNum.setText(getString(R.string.agents_balance_account));
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
        rlPasscodeContainer.setLayoutParams(rlPhotoLayout);
        rlPrepayContainer.setLayoutParams(rlPhotoLayout);
        rlOverdraftContainer.setLayoutParams(rlPhotoLayout);
        rlTimeLimitContainer.setLayoutParams(rlPhotoLayout);

        rlNumContainer.setPadding(getActualHeightOnThisDevice(20), 0, getActualHeightOnThisDevice(20), 0);
        rlAccountContainer.setPadding(getActualHeightOnThisDevice(20), 0, getActualHeightOnThisDevice(20), 0);
        rlPasscodeContainer.setPadding(getActualHeightOnThisDevice(20), 0, getActualHeightOnThisDevice(20), 0);
        rlPrepayContainer.setPadding(getActualHeightOnThisDevice(20), 0, getActualHeightOnThisDevice(20), 0);
        rlOverdraftContainer.setPadding(getActualHeightOnThisDevice(20), 0, getActualHeightOnThisDevice(20), 0);
        rlTimeLimitContainer.setPadding(getActualHeightOnThisDevice(20), 0, getActualHeightOnThisDevice(20), 0);
       // rlHistoryContainer.setPadding(20, 0, 20, 0);


        rlNumContainer.addView(tvNum);
        RelativeLayout.LayoutParams paramsEtLogoutAfterTimeM = (RelativeLayout.LayoutParams) tvNum.getLayoutParams();
        paramsEtLogoutAfterTimeM.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsEtLogoutAfterTimeM.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsEtLogoutAfterTimeM.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsEtLogoutAfterTimeM.setMargins(getActualHeightOnThisDevice(20), 0, 0, 0);
        tvNum.setLayoutParams(paramsEtLogoutAfterTimeM);


        rlNumContainer.addView(tvNumValue);
        RelativeLayout.LayoutParams paramsPhotoImageViewValue = (RelativeLayout.LayoutParams) tvNumValue.getLayoutParams();
        paramsPhotoImageViewValue.width = getActualWidthOnThisDevice(300);
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
        tvAccountValue.setPadding(0, 0, getActualHeightOnThisDevice(20), 0);

        rlPasscodeContainer.addView(tvPassCode);
        RelativeLayout.LayoutParams paramsOccupation = (RelativeLayout.LayoutParams) tvPassCode.getLayoutParams();
        paramsOccupation.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsOccupation.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsOccupation.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsOccupation.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsOccupation.setMargins(getActualHeightOnThisDevice(20), 0, 0, 0);
        tvPassCode.setLayoutParams(paramsOccupation);


        rlPasscodeContainer.addView(tvPassCodeValue);
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
        paramsPrePayValue.width = getActualWidthOnThisDevice(300);
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
        paramsBirthValue.width = getActualWidthOnThisDevice(300);
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
        paramsGenderInfoValue.width = getActualWidthOnThisDevice(300);
        paramsGenderInfoValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsGenderInfoValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsGenderInfoValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvTimeLimitValue.setLayoutParams(paramsGenderInfoValue);
        tvTimeLimitValue.setPadding(0, 0, getActualHeightOnThisDevice(20), 0);

//
//        rlSignatureContainer.addView(tvSignature);
//        RelativeLayout.LayoutParams paramstvSignature = (RelativeLayout.LayoutParams) tvSignature.getLayoutParams();
//        paramstvSignature.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramstvSignature.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramstvSignature.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
//        paramstvSignature.setMargins(20, 0, 0, 0);
//        paramstvSignature.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        tvSignature.setLayoutParams(paramstvSignature);

    }

    private boolean doCheckNeedSave() {
        String account = tvAccountValue.getText().toString();
        String pass = tvPassCodeValue.getText().toString();
        String overdraft = tvOverDraftValue.getValue();
        String timeLimit = tvTimeLimitValue.getValue();


        return !beforeDataList.get(0).equals(account)
                || !beforeDataList.get(1).equals(pass)
                || !beforeDataList.get(2).equals(overdraft)
                || !beforeDataList.get(3).equals(timeLimit);
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


        tvOverDraftValue.setEnabled(isEdit);
        tvTimeLimitValue.setEnabled(isEdit);
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

    public void backSave() {
        selectOutOrInPopupWindow.showAtLocation(AgentBalanceAccountFragment.this.getRootView().findViewById(R.id.ll_do_edit_administration_container), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        selectOutOrInPopupWindow.btFirstValue.setText(R.string.msg_save_change);
        selectOutOrInPopupWindow.btSecondValue.setText(R.string.common_cancel);
        selectOutOrInPopupWindow.btThirdValue.setVisibility(View.GONE);
        selectOutOrInPopupWindow.btFirstValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOutOrInPopupWindow.dismiss();
                setOnBackListener(null);
                netAgentEditAccount();
            }
        });
        selectOutOrInPopupWindow.btSecondValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOutOrInPopupWindow.dismiss();
                setOnBackListener(null);
                getBaseActivity().doBack();
            }
        });
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        resetView();

        getTvLeftTitle().setText(getResources().getString(R.string.player_info_balance_account_title));

        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    netAgentEditAccount();
                }
                if (!isEdit) {
                    isEdit = !isEdit;
                }
                resetView();


            }
        });


        setOnBackListener(new OnPopBackListener() {
            @Override
            public boolean preDoBack() {

                if (!doCheckNeedSave()) {
                    setOnBackListener(null);
                    getBaseActivity().doBack();
                } else {

                    backSave();
                }
                return false;
            }
        });

    }

    @Override
    protected void executeOnceOnCreate() {
        netAgentBalanceAccount();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        Bundle bundle = getArguments();
        if (bundle != null) {

            agentId = bundle.getInt(TransKey.AGENTS_AGENT_ID);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * get balanceAccount data.
     */
    private void netAgentBalanceAccount() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.AGENT_AGENT_ID, String.valueOf(agentId));


        HttpManager<JsonBalanceAccountGet> hh = new HttpManager<JsonBalanceAccountGet>(AgentBalanceAccountFragment.this) {

            @Override
            public void onJsonSuccess(JsonBalanceAccountGet jo) {
                dataParameter = jo;
                beforeDataList.add(dataParameter.getDataList().getAccount());
                beforeDataList.add(dataParameter.getDataList().getPassword());

                String overStr = dataParameter.getDataList().getOverDraft();
                if (Utils.isStringNullOrEmpty(overStr)) {

                    overStr = Constants.STR_0;
                }
                beforeDataList.add(overStr);
                beforeDataList.add(String.valueOf(dataParameter.getDataList().getTimeLimit()));


                setDefaultValueOfControls();


            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.BalanceAccountAgentGet, params);
    }

    private void netAgentEditAccount() {


        Map<String, String> params = new HashMap<>();

        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.AGENT_AGENT_ID, String.valueOf(agentId));
        params.put(ApiKey.AGENT_BALANCE_ACCOUNT, tvAccountValue.getText().toString());
        params.put(ApiKey.AGENT_BALANCE_PASS_WORD, tvPassCodeValue.getText().toString());
        params.put(ApiKey.AGENT_OVERDRAFT, tvOverDraftValue.getValue());
        params.put(ApiKey.AGENT_TIME_LIMIT, tvTimeLimitValue.getValue());

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(AgentBalanceAccountFragment.this) {
            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                Utils.showShortToast(getActivity(), msg);
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
//                    getTvRight().setBackgroundResource(R.drawable.administration_icon_edit);
//                    getTvRight().setText("");
                    setOnBackListener(null);
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
