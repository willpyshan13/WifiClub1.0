/**
 * Project Name: itee
 * File Name:	 AgentUserMessageFragment.java
 * Package Name: cn.situne.itee.fragment
 * Date:		 2015-03-22
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */
package cn.situne.itee.fragment.agents;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
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
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:AgentUserMessageFragment <br/>
 * Function: login page. <br/>
 * Date: 2015-03-22 <br/>
 *
 * @author qiushuai
 * @version 1.0
 * @see
 */
public class AgentsUserMessageFragment extends BaseFragment {

    private boolean isEdit;

    private RelativeLayout rlUserName;
    private RelativeLayout rlPassWord;

    private IteeTextView tvUserNameKey;
    private IteeEditText tvUserNameValue;
    private IteeTextView tvPassWordKey;
    private IteeTextView tvPassWordValue;

    private String agentAccount;
    private Integer agentId;
    private String passWord;


    @Override
    protected int getFragmentId() {
        return R.layout.fragment_agent_user_message;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        rlUserName = (RelativeLayout) rootView.findViewById(R.id.rl_userName);
        rlPassWord = (RelativeLayout) rootView.findViewById(R.id.rl_passWord);
        tvUserNameKey = new IteeTextView(getActivity());
        tvUserNameValue = new IteeEditText(getActivity());
        tvPassWordKey = new IteeTextView(getActivity());
        tvPassWordValue = new IteeTextView(getActivity());

        Bundle bundle = getArguments();
        if (bundle != null) {
            agentAccount = bundle.getString(TransKey.AGENTS_AGENT_ACCOUNT);
            agentId = bundle.getInt(TransKey.AGENTS_AGENT_ID);
            passWord = bundle.getString(TransKey.AGENTS_AGENT_PASS_WORD);
        }

        tvUserNameValue.setText(agentAccount);
        tvPassWordValue.setText(passWord);
    }

    @Override
    protected void setDefaultValueOfControls() {

        LinearLayout.LayoutParams paramUserName = (LinearLayout.LayoutParams) rlUserName.getLayoutParams();
        paramUserName.height = getActualHeightOnThisDevice(100);
        rlUserName.setLayoutParams(paramUserName);

        rlUserName.addView(tvUserNameKey);
        RelativeLayout.LayoutParams paramTvUserNameKey = (RelativeLayout.LayoutParams) tvUserNameKey.getLayoutParams();
        paramTvUserNameKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvUserNameKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvUserNameKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvUserNameKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvUserNameKey.leftMargin = getActualWidthOnThisDevice(40);
        tvUserNameKey.setLayoutParams(paramTvUserNameKey);

        rlUserName.addView(tvUserNameValue);
        RelativeLayout.LayoutParams paramsTvUserNameValue = (RelativeLayout.LayoutParams) tvUserNameValue.getLayoutParams();
        paramsTvUserNameValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvUserNameValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvUserNameValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvUserNameValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvUserNameValue.rightMargin = getActualWidthOnThisDevice(40);
        tvUserNameValue.setLayoutParams(paramsTvUserNameValue);

        LinearLayout.LayoutParams paramPassWord = (LinearLayout.LayoutParams) rlPassWord.getLayoutParams();
        paramPassWord.height = getActualHeightOnThisDevice(100);
        rlPassWord.setLayoutParams(paramPassWord);

        rlPassWord.addView(tvPassWordKey);
        RelativeLayout.LayoutParams paramTvPassWordKey = (RelativeLayout.LayoutParams) tvPassWordKey.getLayoutParams();
        paramTvPassWordKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvPassWordKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvPassWordKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvPassWordKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvPassWordKey.leftMargin = getActualWidthOnThisDevice(40);
        tvPassWordKey.setLayoutParams(paramTvPassWordKey);

        rlPassWord.addView(tvPassWordValue);
        RelativeLayout.LayoutParams paramsTvPassWordValue = (RelativeLayout.LayoutParams) tvPassWordValue.getLayoutParams();
        paramsTvPassWordValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPassWordValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPassWordValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvPassWordValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvPassWordValue.rightMargin = getActualWidthOnThisDevice(40);
        tvPassWordValue.setLayoutParams(paramsTvPassWordValue);

    }

    @Override
    protected void setListenersOfControls() {

        tvUserNameKey.setText(R.string.agent_user_message_userName);

        tvUserNameValue.setTextColor(getColor(R.color.common_gray));
        tvUserNameValue.setBackground(null);

        tvPassWordKey.setText(R.string.agent_user_message_passWord);
        tvPassWordKey.setTextColor(getColor(R.color.common_gray));

        tvPassWordValue.setTextColor(getColor(R.color.common_gray));
        tvPassWordValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {

    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(agentAccount);
        tvUserNameValue.setEnabled(isEdit);
        if (isEdit) {
            tvUserNameValue.setEnabled(true);
            getTvRight().setBackground(null);
            getTvRight().setText(R.string.common_ok);
        } else {
            tvUserNameValue.setEnabled(false);
            getTvRight().setBackgroundResource(R.drawable.administration_icon_edit);
            getTvRight().setText(Constants.STR_EMPTY);
        }

        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = !isEdit;
                if (isEdit) {
                    tvUserNameValue.setEnabled(true);
                    getTvRight().setBackground(null);
                    getTvRight().setText(R.string.common_ok);
                    getTvRight().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Utils.hideKeyboard(getActivity());
                            if (doCheck()) {
                                updateUserMessage();
                            }
                        }
                    });

                }
            }
        });

    }

    private boolean doCheck() {
        boolean res = true;
        if (StringUtils.isEmpty(tvUserNameValue.getValue())) {
            res = false;
            Utils.showShortToast(getActivity(), AppUtils.generateNotNullMessage(this, R.string.agent_user_message_userName));
        }
//        if (StringUtils.isNotEmpty(tvUserNameValue.getValue())) {
//            if (tvUserNameValue.getValue().length() < Constants.PASSWORD_MIN_SIZE
//                    || tvUserNameValue.getValue().length() > Constants.PASSWORD_MAX_SIZE) {
//                res = false;
//                Utils.showShortToast(getActivity(), R.string.common_username_size_message);
//            }
//        }
        if (!AppUtils.doCheckUserName(tvUserNameValue.getValue())) {
            res = false;
            Utils.showShortToast(getActivity(), getResources().getString(R.string.error_username_format));
        }
        return res;
    }

    public void updateUserMessage() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        params.put(ApiKey.AGENT_ID, String.valueOf(agentId));
        params.put(ApiKey.AGENT_PASSWORD, passWord);
        params.put(ApiKey.AGENT_ACCOUNT, tvUserNameValue.getText().toString());

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(AgentsUserMessageFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                Integer returnCode = jo.getReturnCode();

                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    getBaseActivity().doBackWithRefresh();
                }
                Utils.showShortToast(getActivity(), msg);

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
        hh.startPut(getActivity(), ApiManager.HttpApi.AgentsAccountPut, params);
    }
}
