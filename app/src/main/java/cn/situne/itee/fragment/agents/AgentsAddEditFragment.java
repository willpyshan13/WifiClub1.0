/**
 * Project Name: itee
 * File Name:	 AddAgentFragment.java
 * Package Name: cn.situne.itee.fragment
 * Date:		 2015-03-22
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.agents;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.KeyListener;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.player.PlayerInfoEditFragment;
import cn.situne.itee.fragment.teetime.TeeTimeMemberListWithIndexFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonAgentsEdit;
import cn.situne.itee.manager.jsonentity.JsonGeneralInfo;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.PlayerInfoEmailItemView;
import cn.situne.itee.view.PlayerInfoPhoneItemView;

/**
 * ClassName:AddAgentFragment <br/>
 * Function: add agent. <br/>
 * Date: 2015-03-22 <br/>
 *
 * @author qiushuai
 * @version 1.0
 * @see
 */
public class AgentsAddEditFragment extends BaseEditFragment {

    private RelativeLayout rlAgent;

    private RelativeLayout rlId;

    private IteeEditText edId;

    private RelativeLayout rlPerson;

    private RelativeLayout rlUsername;
    private RelativeLayout rlPassword;

    private RelativeLayout rlNote;

    private LinearLayout llPhoneContainer;
    private LinearLayout llEmailContainer;
    private RelativeLayout rlAddPhoneContainer;
    private RelativeLayout rlAddEmailContainer;

    private IteeTextView tvAgentKey;
    private IteeEditText etAgentValue;
    private IteeTextView tvPersonKey;
    private IteeEditText etPersonValue;
    private IteeTextView tvPhoneKey;
    private IteeTextView tvPhoneRequired;
    private IteeTextView tvEmailKey;
    private IteeTextView tvEmailRequired;
    private IteeTextView tvUserNameKey;
    private IteeEditText etUserNameValue;
    private IteeTextView tvPassWordKey;
    private IteeEditText etPasswordValue;
    private IteeTextView tvNote;
    private IteeEditText etNoteValue;

    private boolean isAdd;
    private Integer agentId;
    private String agentName;

    private String fromPage;

    private ArrayList<JsonGeneralInfo.PhoneItem> phoneList;
    private ArrayList<JsonGeneralInfo.EmailItem> emailList;

    private View.OnClickListener listenerRightOk;

    private PlayerInfoEditFragment.OnDeleteClickListener onDeleteClickListener = new PlayerInfoEditFragment.OnDeleteClickListener() {
        @Override
        public void OnPhoneDeleteClick(String flag, JsonGeneralInfo.PhoneItem phoneItem) {
            phoneList.remove(phoneItem);
            if (phoneList.size() == 0) {
                tvPhoneRequired.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void OnEmailDeleteClick(String flag, JsonGeneralInfo.EmailItem emailItem) {
            emailList.remove(emailItem);
            if (emailList.size() == 0) {
                tvEmailRequired.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void OnAddressDeleteClick(String flag, JsonGeneralInfo.AddressItem addressItem) {

        }
    };

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_add_agent;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        rlAgent = (RelativeLayout) rootView.findViewById(R.id.rl_agent);

        rlId= (RelativeLayout) rootView.findViewById(R.id.rl_id);

        rlPerson = (RelativeLayout) rootView.findViewById(R.id.rl_person);

        rlUsername = (RelativeLayout) rootView.findViewById(R.id.rl_username);

        rlPassword = (RelativeLayout) rootView.findViewById(R.id.rl_password);

        rlNote = (RelativeLayout) rootView.findViewById(R.id.rl_note);

        llPhoneContainer = (LinearLayout) rootView.findViewById(R.id.ll_add_profile_phone_container);
        llEmailContainer = (LinearLayout) rootView.findViewById(R.id.ll_add_profile_email_container);

        rlAddPhoneContainer = (RelativeLayout) rootView.findViewById(R.id.rl_add_profile_add_phone_container);
        rlAddEmailContainer = (RelativeLayout) rootView.findViewById(R.id.rl_add_profile_add_email_container);

        tvAgentKey = new IteeTextView(getActivity());
        etAgentValue = new IteeEditText(getActivity());
        tvPersonKey = new IteeTextView(getActivity());
        etPersonValue = new IteeEditText(getActivity());
        tvPhoneKey = new IteeTextView(getActivity());
        tvPhoneRequired = new IteeTextView(getActivity());
        tvEmailKey = new IteeTextView(getActivity());
        tvEmailRequired = new IteeTextView(getActivity());
        tvUserNameKey = new IteeTextView(getActivity());
        etUserNameValue = new IteeEditText(getActivity());
        tvPassWordKey = new IteeTextView(getActivity());
        etPasswordValue = new IteeEditText(getActivity());
        tvNote = new IteeTextView(getActivity());
        etNoteValue = new IteeEditText(getActivity());

        phoneList = new ArrayList<>();
        emailList = new ArrayList<>();

        Bundle bundle = getArguments();
        if (bundle != null) {
            isAdd = bundle.getBoolean(TransKey.EVENT_IS_ADD);
            if (isAdd) {
                setFragmentMode(FragmentMode.FragmentModeAdd);
            } else {
                setFragmentMode(FragmentMode.FragmentModeBrowse);
            }
            agentId = bundle.getInt(TransKey.AGENTS_AGENT_ID);
            agentName = bundle.getString(TransKey.AGENTS_AGENT_NAME);
            fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE, "");
        }

        if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
            rlUsername.setVisibility(View.GONE);
            rlPassword.setVisibility(View.GONE);
            View rlUsernameLine = rootView.findViewById(R.id.rl_usernameLine);
            View rlPasswordLine = rootView.findViewById(R.id.rl_passwordLine);
            rlUsernameLine.setVisibility(View.GONE);
            rlPasswordLine.setVisibility(View.GONE);
        }

    }

    @Override
    protected void setDefaultValueOfControls() {

        LinearLayout.LayoutParams paramAgent = (LinearLayout.LayoutParams) rlAgent.getLayoutParams();
        paramAgent.height = getActualHeightOnThisDevice(100);
        rlAgent.setLayoutParams(paramAgent);

        rlAgent.addView(tvAgentKey);
        RelativeLayout.LayoutParams paramTvAgentKey = (RelativeLayout.LayoutParams) tvAgentKey.getLayoutParams();
        paramTvAgentKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvAgentKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvAgentKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvAgentKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvAgentKey.leftMargin = 40;
        tvAgentKey.setLayoutParams(paramTvAgentKey);

        rlAgent.addView(etAgentValue);
        RelativeLayout.LayoutParams paramsTvAgentValue = (RelativeLayout.LayoutParams) etAgentValue.getLayoutParams();
        paramsTvAgentValue.width = (int) (getScreenWidth() * 0.5);
        paramsTvAgentValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAgentValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvAgentValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvAgentValue.rightMargin = 40;
        etAgentValue.setLayoutParams(paramsTvAgentValue);

        // itee 11 dev
        LinearLayout.LayoutParams paramId = (LinearLayout.LayoutParams) rlId.getLayoutParams();
        paramId.height = getActualHeightOnThisDevice(100);
        rlId.setLayoutParams(paramAgent);
        IteeTextView tvIdKey = new IteeTextView(getBaseActivity());
        rlId.addView(tvIdKey);
        tvIdKey.setText(getString(R.string.player_info_membership_id));
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvIdKey, 40, getBaseActivity());

        edId = new IteeEditText(getBaseActivity());
        edId.setTextSize(Constants.FONT_SIZE_NORMAL);
        edId.setTextColor(getColor(R.color.common_gray));
        edId.setHint(getString(R.string.player_info_membership_id));
        edId.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        edId.setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {
                char[] numberChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z','A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
                return numberChars;
            }

            @Override
            public int getInputType() {
                  return android.text.InputType.TYPE_CLASS_PHONE;
            }
        });

        rlId.addView(edId);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(edId,40,getBaseActivity());

        LinearLayout.LayoutParams paramPerson = (LinearLayout.LayoutParams) rlPerson.getLayoutParams();
        paramPerson.height = getActualHeightOnThisDevice(100);
        rlPerson.setLayoutParams(paramPerson);

        rlPerson.addView(tvPersonKey);
        RelativeLayout.LayoutParams paramTvPersonKey = (RelativeLayout.LayoutParams) tvPersonKey.getLayoutParams();
        paramTvPersonKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvPersonKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvPersonKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvPersonKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvPersonKey.leftMargin = 40;
        tvPersonKey.setLayoutParams(paramTvAgentKey);

        rlPerson.addView(etPersonValue);
        RelativeLayout.LayoutParams paramsTvPersonValue = (RelativeLayout.LayoutParams) etPersonValue.getLayoutParams();
        paramsTvPersonValue.width = (int) (getScreenWidth() * 0.5);
        paramsTvPersonValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPersonValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvPersonValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvPersonValue.rightMargin = 40;
        etPersonValue.setLayoutParams(paramsTvPersonValue);

        LinearLayout.LayoutParams paramPhone = (LinearLayout.LayoutParams) rlAddPhoneContainer.getLayoutParams();
        paramPhone.height = getActualHeightOnThisDevice(100);
        paramPhone.topMargin = 25;
        rlAddPhoneContainer.setLayoutParams(paramPhone);

        rlAddPhoneContainer.addView(tvPhoneKey);
        RelativeLayout.LayoutParams paramTvPhoneKey = (RelativeLayout.LayoutParams) tvPhoneKey.getLayoutParams();
        paramTvPhoneKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvPhoneKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvPhoneKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvPhoneKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvPhoneKey.leftMargin = 40;
        tvPhoneKey.setLayoutParams(paramTvPhoneKey);

        rlAddPhoneContainer.addView(tvPhoneRequired);
        RelativeLayout.LayoutParams paramsTvPhoneValue = (RelativeLayout.LayoutParams) tvPhoneRequired.getLayoutParams();
        paramsTvPhoneValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPhoneValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPhoneValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvPhoneValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvPhoneValue.rightMargin = 40;
        tvPhoneRequired.setLayoutParams(paramsTvPhoneValue);

        LinearLayout.LayoutParams paramEmail = (LinearLayout.LayoutParams) rlAddEmailContainer.getLayoutParams();
        paramEmail.height = getActualHeightOnThisDevice(100);
        paramEmail.topMargin = 25;
        rlAddEmailContainer.setLayoutParams(paramEmail);

        rlAddEmailContainer.addView(tvEmailKey);
        RelativeLayout.LayoutParams paramTvEmailKey = (RelativeLayout.LayoutParams) tvEmailKey.getLayoutParams();
        paramTvEmailKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvEmailKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvEmailKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvEmailKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvEmailKey.leftMargin = 40;
        tvEmailKey.setLayoutParams(paramTvEmailKey);

        rlAddEmailContainer.addView(tvEmailRequired);
        RelativeLayout.LayoutParams paramsTvEmailValue = (RelativeLayout.LayoutParams) tvEmailRequired.getLayoutParams();
        paramsTvEmailValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvEmailValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvEmailValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvEmailValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvEmailValue.rightMargin = 40;
        tvEmailRequired.setLayoutParams(paramsTvEmailValue);

        LinearLayout.LayoutParams paramUsername = (LinearLayout.LayoutParams) rlUsername.getLayoutParams();
        paramUsername.height = getActualHeightOnThisDevice(100);
        paramUsername.topMargin = 25;
        rlUsername.setLayoutParams(paramUsername);

        rlUsername.addView(tvUserNameKey);
        RelativeLayout.LayoutParams paramTvUserNameKey = (RelativeLayout.LayoutParams) tvUserNameKey.getLayoutParams();
        paramTvUserNameKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvUserNameKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvUserNameKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvUserNameKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvUserNameKey.leftMargin = 40;
        tvUserNameKey.setLayoutParams(paramTvUserNameKey);

        rlUsername.addView(etUserNameValue);
        RelativeLayout.LayoutParams paramsTvUserNameValue = (RelativeLayout.LayoutParams) etUserNameValue.getLayoutParams();
        paramsTvUserNameValue.width = (int) (getScreenWidth() * 0.5);
        paramsTvUserNameValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvUserNameValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvUserNameValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvUserNameValue.rightMargin = 40;
        etUserNameValue.setLayoutParams(paramsTvUserNameValue);

        LinearLayout.LayoutParams paramPassword = (LinearLayout.LayoutParams) rlPassword.getLayoutParams();
        paramPassword.height = getActualHeightOnThisDevice(100);
        rlPassword.setLayoutParams(paramPassword);

        rlPassword.addView(tvPassWordKey);
        RelativeLayout.LayoutParams paramTvPassWordKey = (RelativeLayout.LayoutParams) tvPassWordKey.getLayoutParams();
        paramTvPassWordKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvPassWordKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvPassWordKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvPassWordKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvPassWordKey.leftMargin = 40;
        tvPassWordKey.setLayoutParams(paramTvPassWordKey);

        rlPassword.addView(etPasswordValue);
        RelativeLayout.LayoutParams paramsTvPassWordValue = (RelativeLayout.LayoutParams) etPasswordValue.getLayoutParams();
        paramsTvPassWordValue.width = (int) (getScreenWidth() * 0.5);
        paramsTvPassWordValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPassWordValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvPassWordValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvPassWordValue.rightMargin = 40;
        etPasswordValue.setLayoutParams(paramsTvPassWordValue);

        LinearLayout.LayoutParams paramNote = (LinearLayout.LayoutParams) rlNote.getLayoutParams();
        paramNote.height = getActualHeightOnThisDevice(100);
        rlNote.setLayoutParams(paramNote);

        rlNote.addView(tvNote);
        RelativeLayout.LayoutParams paramTvNote = (RelativeLayout.LayoutParams) tvNote.getLayoutParams();
        paramTvNote.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvNote.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvNote.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvNote.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvNote.leftMargin = 40;
        tvNote.setLayoutParams(paramTvNote);

        rlNote.addView(etNoteValue);
        RelativeLayout.LayoutParams paramsTvNoteValue = (RelativeLayout.LayoutParams) etNoteValue.getLayoutParams();
        paramsTvNoteValue.width = (int) (getScreenWidth() * 0.5);
        paramsTvNoteValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvNoteValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvNoteValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvNoteValue.rightMargin = 40;
        etNoteValue.setLayoutParams(paramsTvNoteValue);

        LinearLayout.LayoutParams llEmailContainerLayoutParams = (LinearLayout.LayoutParams) llEmailContainer.getLayoutParams();
        llEmailContainerLayoutParams.topMargin = 10;
        llEmailContainer.setLayoutParams(llEmailContainerLayoutParams);

        LinearLayout.LayoutParams llPhoneContainerLayoutParams = (LinearLayout.LayoutParams) llPhoneContainer.getLayoutParams();
        llPhoneContainerLayoutParams.topMargin = 10;
        llPhoneContainer.setLayoutParams(llPhoneContainerLayoutParams);

        int leftPadding = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        int rightPadding = DensityUtil.getActualWidthOnThisDevice(20, mContext);
        llPhoneContainer.setPadding(leftPadding, 0, rightPadding, 0);
        llEmailContainer.setPadding(leftPadding, 0, rightPadding, 0);
    }

    @Override
    protected void setListenersOfControls() {

        //fix by syb.
        etUserNameValue.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        if (" ".equals(source.toString())) {
                            return "";
                        }
                        return null;
                    }
                }
        });


        etAgentValue.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        if (" ".equals(source.toString())) {
                            return "";
                        }
                        return null;
                    }
                }
        });
        //fix by syb.
        etPasswordValue.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        if (" ".equals(source.toString())) {
                            return "";
                        }
                        return null;
                    }
                }
        });

        listenerRightOk = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(getActivity());
                if (FragmentMode.FragmentModeAdd == getFragmentMode()) {
                    if (doCheck()) {
                        addAgent();
                    }
                } else {
                    if (FragmentMode.FragmentModeEdit == getFragmentMode()) {
                        if (doCheck()) {
                            updateAgent();
                        }
                    } else {
                        rlAddPhoneContainer.setVisibility(View.VISIBLE);
                        rlAddEmailContainer.setVisibility(View.VISIBLE);
                        setFragmentMode(FragmentMode.FragmentModeEdit);
                        getTvRight().setBackground(null);
                        getTvRight().setText(R.string.common_ok);

                        change2EditState();
                    }

                }
            }
        };
        rlAddPhoneContainer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                    int childCount = llPhoneContainer.getChildCount();
                    String tag;
                    switch (childCount) {
                        case 0:
                            tag = getString(R.string.tag_home);
                            break;
                        case 1:
                            tag = getString(R.string.tag_work);
                            break;
                        case 2:
                            tag = getString(R.string.tag_mobile);
                            break;
                        case 3:
                            tag = getString(R.string.tag_other);
                            break;
                        default:
                            tag = getString(R.string.tag_home);
                            break;
                    }

                    JsonGeneralInfo.PhoneItem addressItem = new JsonGeneralInfo.PhoneItem();
                    addressItem.setTag(tag);
                    phoneList.add(addressItem);
                    addPhoneView(addressItem);
                }
            }
        });

        rlAddEmailContainer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                    int childCount = llPhoneContainer.getChildCount();
                    String tag;
                    switch (childCount) {
                        case 0:
                            tag = getString(R.string.tag_home);
                            break;
                        case 1:
                            tag = getString(R.string.tag_work);
                            break;
                        case 2:
                            tag = getString(R.string.tag_mobile);
                            break;
                        case 3:
                            tag = getString(R.string.tag_other);
                            break;
                        default:
                            tag = getString(R.string.tag_home);
                            break;
                    }

                    JsonGeneralInfo.EmailItem addressItem = new JsonGeneralInfo.EmailItem();
                    addressItem.setTag(tag);
                    emailList.add(addressItem);
                    addEmailView(addressItem);
                }
            }
        });
    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {

        tvAgentKey.setText(R.string.add_agent_agent);
        tvAgentKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvAgentKey.setTextColor(getColor(R.color.common_black));

        etAgentValue.setHint(getString(R.string.add_agent_agent));

        etAgentValue.setSingleLine();
        etAgentValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        etAgentValue.setTextColor(getColor(R.color.common_gray));
        etAgentValue.setGravity(Gravity.END);
        etAgentValue.setBackground(null);

        tvPersonKey.setText(R.string.add_agent_person_in_charge);
        tvPersonKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvPersonKey.setTextColor(getColor(R.color.common_black));
        etPersonValue.setHint(getString(R.string.add_agent_person_in_charge));
        etPersonValue.setSingleLine();
        etPersonValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        etPersonValue.setTextColor(getColor(R.color.common_gray));
        etPersonValue.setGravity(Gravity.END);
        etPersonValue.setBackground(null);

        tvPhoneKey.setText(R.string.add_agent_add_phone);
        tvPhoneKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvPhoneKey.setTextColor(getColor(R.color.common_blue));

        tvPhoneRequired.setText(R.string.common_required);
        tvPhoneRequired.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvPhoneRequired.setTextColor(getColor(R.color.common_red));

        tvEmailKey.setText(R.string.add_agent_add_email);
        tvEmailKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvEmailKey.setTextColor(getColor(R.color.common_blue));

        tvEmailRequired.setText(R.string.common_required);
        tvEmailRequired.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvEmailRequired.setTextColor(getColor(R.color.common_red));

        tvUserNameKey.setText(R.string.add_agent_userName);
        tvUserNameKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvUserNameKey.setTextColor(getColor(R.color.common_black));

        etUserNameValue.setHint(getString(R.string.add_agent_userName));

        etUserNameValue.setSingleLine();
        etUserNameValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        etUserNameValue.setTextColor(getColor(R.color.common_gray));
        etUserNameValue.setGravity(Gravity.END);
        etUserNameValue.setBackground(null);

        tvPassWordKey.setText(R.string.add_agent_passWord);
        tvPassWordKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvPassWordKey.setTextColor(getColor(R.color.common_black));


        etPasswordValue.setHint(getString(R.string.add_agent_passWord));
        etPasswordValue.setSingleLine();
        etPasswordValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        etPasswordValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        etPasswordValue.setTextColor(getColor(R.color.common_gray));
        etPasswordValue.setGravity(Gravity.END);
        etPasswordValue.setBackground(null);

        tvNote.setText(R.string.add_agent_notes);
        tvNote.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvNote.setTextColor(getColor(R.color.common_black));

        tvNote.setHint(getString(R.string.add_agent_notes));

        etNoteValue.setSingleLine();
        etNoteValue.setGravity(Gravity.END);
        etNoteValue.setBackground(null);
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
            getTvLeftTitle().setText(R.string.agents_add_an_agent);
            getTvRight().setBackground(null);
            getTvRight().setText(R.string.common_save);
        } else {
            getTvLeftTitle().setText(agentName);
            if (getFragmentMode() == FragmentMode.FragmentModeEdit) {
                getTvRight().setText(R.string.common_ok);
                getTvRight().setBackground(null);
                rlAddPhoneContainer.setVisibility(View.VISIBLE);
                rlAddEmailContainer.setVisibility(View.VISIBLE);
            } else {
                getTvRight().setText(Constants.STR_EMPTY);
                getTvRight().setBackgroundResource(R.drawable.icon_common_edit);
                etAgentValue.setEnabled(false);
                edId.setEnabled(false);
                etPersonValue.setEnabled(false);
                etUserNameValue.setEnabled(false);
                etPasswordValue.setEnabled(false);
                etNoteValue.setEnabled(false);
                rlAddPhoneContainer.setVisibility(View.GONE);
                rlAddEmailContainer.setVisibility(View.GONE);
            }
        }
        getTvRight().setOnClickListener(listenerRightOk);
    }

    public void addAgent() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        params.put(ApiKey.AGENT_NAME, String.valueOf(etUserNameValue.getText()));
        params.put(ApiKey.AGENT_CONTACT_NAME, String.valueOf(etPersonValue.getText()));
        params.put(ApiKey.AGENT_ACCOUNT, String.valueOf(etAgentValue.getText()));
        params.put(ApiKey.AGENT_PASSWORD, String.valueOf(etPasswordValue.getText()));
        params.put(ApiKey.AGENT_PHONE_LIST, getPhoneString());
        params.put(ApiKey.AGENT_EMAIL_LIST, getEmailString());
        params.put(ApiKey.AGENT_NOTES, String.valueOf(etNoteValue.getText()));

        params.put(ApiKey.AGEN_CODE, edId.getText().toString());


        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(AgentsAddEditFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                Integer returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();

                if (Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY == returnCode) {

                    if (fromPage.equals(TeeTimeMemberListWithIndexFragment.class.getName())) {
                        getBaseActivity().doBackWithRefresh(TeeTimeMemberListWithIndexFragment.class);
                    } else {
                        if (Utils.isStringNotNullOrEmpty(fromPage)){
                            Bundle bundle = new Bundle();
                            bundle.putString("addId",jo.getAddId());
                            bundle.putString(TransKey.COMMON_FROM_PAGE,PlayerInfoEditFragment.class.getName());
                            try {
                                doBackWithReturnValue(bundle,(Class<? extends BaseFragment>) Class.forName(fromPage));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }else{
                            getBaseActivity().doBackWithRefresh();

                        }



                    }

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
        hh.start(getActivity(), ApiManager.HttpApi.AgentsPost, params);
    }

    public void updateAgent() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        params.put(ApiKey.AGENT_ID, String.valueOf(agentId));
        params.put(ApiKey.AGENT_NAME, String.valueOf(etAgentValue.getText()));
        params.put(ApiKey.AGENT_ACCOUNT, String.valueOf(etUserNameValue.getText()));
        params.put(ApiKey.AGENT_CONTACT_NAME, String.valueOf(etPersonValue.getText()));
        params.put(ApiKey.AGENT_PASSWORD, String.valueOf(etPasswordValue.getText()));
        params.put(ApiKey.AGENT_PHONE_LIST, getPhoneString());
        params.put(ApiKey.AGENT_EMAIL_LIST, getEmailString());
        params.put(ApiKey.AGENT_NOTES, String.valueOf(etNoteValue.getText()));
        params.put(ApiKey.AGEN_CODE, edId.getText().toString());
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(AgentsAddEditFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                Integer returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                Utils.showShortToast(getActivity(), msg);
                if (Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY == returnCode) {
                    if (fromPage.equals(TeeTimeMemberListWithIndexFragment.class.getName())) {
                        doBackWithRefresh(TeeTimeMemberListWithIndexFragment.class);
                    } else {
                        doBackWithRefresh();
                    }
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
        hh.startPut(getActivity(), ApiManager.HttpApi.AgentsPut, params);

    }

    public void putAgentMessage() {
        //初始化界面数据方法
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        params.put(ApiKey.AGENT_ID, String.valueOf(agentId));
        HttpManager<JsonAgentsEdit> hh = new HttpManager<JsonAgentsEdit>(AgentsAddEditFragment.this) {

            @Override
            public void onJsonSuccess(JsonAgentsEdit jo) {

                int returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    JsonAgentsEdit.DataList dataList = jo.getDataList();
                    String agentName = dataList.agentName;
                    String agentContactName = dataList.agentContactName;
                    String agentAccount = dataList.agentAccount;
                    String agentPassWord = dataList.agentPassWord;
                    String agentNotes = dataList.agentNotes;

                    etAgentValue.setText(agentName);
                    etPersonValue.setText(agentContactName);
                    etUserNameValue.setText(agentAccount);
                    etPasswordValue.setText(agentPassWord);
                    etNoteValue.setText(agentNotes);
                    //itee 11 dev
                    edId.setText(dataList.getAgentCode());

                    if (Utils.isListNotNullOrEmpty(jo.getDataList().getPhoneList())) {
                        for (int i = 0; i < jo.getDataList().getPhoneList().size(); i++) {
                            JsonAgentsEdit.DataList.Phone p = jo.getDataList().getPhoneList().get(i);
                            JsonGeneralInfo.PhoneItem phoneItem = new JsonGeneralInfo.PhoneItem();
                            phoneItem.setStatus(p.getPhoneStatus());
                            phoneItem.setTag(p.getPhoneTag());
                            phoneItem.setValue(p.getPhone());
                            addPhoneView(phoneItem);
                            phoneList.add(phoneItem);
                        }
                    }

                    if (Utils.isListNotNullOrEmpty(jo.getDataList().getEmailList())) {
                        for (int i = 0; i < jo.getDataList().getEmailList().size(); i++) {
                            JsonAgentsEdit.DataList.Email e = jo.getDataList().getEmailList().get(i);
                            JsonGeneralInfo.EmailItem emailItem = new JsonGeneralInfo.EmailItem();
                            emailItem.setStatus(e.getEmailStatus());
                            emailItem.setTag(e.getEmailTag());
                            emailItem.setValue(e.getEmail());
                            addEmailView(emailItem);
                            emailList.add(emailItem);
                        }
                    }
                } else {
                    String msg = jo.getReturnInfo();
                    Utils.showShortToast(getActivity(), msg);
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
        hh.startGet(this.getActivity(), ApiManager.HttpApi.AgentsEditPost, params);
    }

    private String getEmailString() {
        JSONArray ja = new JSONArray();
        for (int i = 0; i < emailList.size(); i++) {
            JsonGeneralInfo.EmailItem emailItem = emailList.get(i);
            String format = "\"tag\":\"{0}\",\"value\":\"{1}\",\"status\":\"1\"";
            String emailString = MessageFormat.format(format, emailItem.getTag(), emailItem.getValue());
            try {
                JSONObject jo = new JSONObject("{" + emailString + "}");
                ja.put(jo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ja.toString();
    }

    private String getPhoneString() {
        JSONArray ja = new JSONArray();
        for (int i = 0; i < phoneList.size(); i++) {
            JsonGeneralInfo.PhoneItem phoneItem = phoneList.get(i);
            String format = "\"tag\":\"{0}\",\"value\":\"{1}\",\"status\":\"1\"";
            String phoneString = MessageFormat.format(format, phoneItem.getTag(), phoneItem.getValue());
            try {
                JSONObject jo = new JSONObject("{" + phoneString + "}");
                ja.put(jo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ja.toString();
    }

    private boolean doCheck() {
        boolean res = true;

        String agentValue = etAgentValue.getText().toString();
        String personValue = etPersonValue.getText().toString();
        String userNameValue = etUserNameValue.getText().toString();
        String passWordValue = etPasswordValue.getText().toString();


        if (Utils.isStringNullOrEmpty(edId.getText().toString())) {
            res = false;
            Utils.showShortToast(getActivity(), AppUtils.generateNotNullMessage(this, R.string.player_info_membership_id));
            return res;
        }


        if (Utils.isStringNullOrEmpty(agentValue)) {
            res = false;
            Utils.showShortToast(getActivity(), R.string.agent_fill_agent);
            return res;
        }
        if (Utils.isStringNullOrEmpty(personValue)) {
            res = false;
            Utils.showShortToast(getActivity(), R.string.agent_fill_person_in_charge);
            return res;
        }
        if (Utils.isStringNullOrEmpty(userNameValue)) {
            res = false;
            Utils.showShortToast(getActivity(), R.string.agent_fill_user_name);
            return res;
        }
        if (!doCheckEmail()) {
            res = false;
            return res;
        }
        if (!doCheckPhone()) {
            res = false;
            return res;
        }
        if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
            res = doCheckUserName();
            if (Utils.isStringNullOrEmpty(passWordValue)) {
                res = false;
                Utils.showShortToast(getActivity(), R.string.agent_fill_pass_word);
                return res;
            }
            if (passWordValue.length() < 6 || passWordValue.length() > 16) {
                res = false;
                Utils.showShortToast(getActivity(), R.string.common_password_size_message);
                return res;
            }
        }

        return res;
    }

    private boolean doCheckEmail() {
        boolean res = true;
        if (emailList.size() == 0) {
            res = false;
            Utils.showShortToast(getActivity(), R.string.staff_add_one_email_at_least);
        } else {
            for (int i = 0; i < emailList.size(); i++) {
                JsonGeneralInfo.EmailItem emailItem = emailList.get(i);
                if (Utils.isStringNullOrEmpty(emailItem.getTag())) {
                    res = false;
                    Utils.showShortToast(getActivity(), R.string.custom_mail_tag_must_not_be_null);
                }
                if (Utils.isStringNullOrEmpty(emailItem.getValue())) {
                    res = false;
                    Utils.showShortToast(getActivity(), R.string.staff_fill_email);
                } else {
                    boolean isEmail = Utils.isEmail(emailItem.getValue());
                    if (!isEmail) {
                        res = false;
                        Utils.showShortToast(getActivity(), getResources().getString(R.string.error_email_address_format));
                    }
                }
            }
        }
        return res;
    }

    private boolean doCheckPhone() {
        boolean res = true;
        if (phoneList.size() == 0) {
            res = false;
            Utils.showShortToast(getActivity(), R.string.staff_add_one_phone_number_at_least);
        } else {
            for (int i = 0; i < phoneList.size(); i++) {
                JsonGeneralInfo.PhoneItem phoneItem = phoneList.get(i);
                if (Utils.isStringNullOrEmpty(phoneItem.getTag())) {
                    res = false;
                    Utils.showShortToast(getActivity(), R.string.custom_phone_tag_must_not_be_null);
                }
                if (Utils.isStringNullOrEmpty(phoneItem.getValue())) {
                    res = false;
                    Utils.showShortToast(getActivity(), R.string.staff_fill_phone_number);
                } else {
                    boolean isPhone = Utils.isMobile(phoneItem.getValue());
                    if (!isPhone) {
                        res = false;
                        Utils.showShortToast(getActivity(), R.string.error_mes00003);
                    }
                }
            }
        }
        return res;
    }

    private boolean doCheckUserName() {
        String userNameValue = etUserNameValue.getText().toString();
        boolean res = true;
        boolean isPhone = Utils.isMobile(userNameValue);
        boolean isEmail = Utils.isEmail(userNameValue);
        if (!isPhone && !isEmail) {
            res = false;
            Utils.showShortToast(getActivity(), getResources().getString(R.string.error_username_format));
        }

        return res;
    }

    private void change2EditState() {

        boolean isEdit = getFragmentMode() != FragmentMode.FragmentModeBrowse;

        etAgentValue.setEnabled(isEdit);
        edId.setEnabled(isEdit);
        etPersonValue.setEnabled(isEdit);
        etUserNameValue.setEnabled(isEdit);
        etPasswordValue.setEnabled(isEdit);
        etNoteValue.setEnabled(isEdit);

        if (llPhoneContainer.getChildCount() == 0) {
            tvPhoneRequired.setVisibility(View.VISIBLE);
        } else {
            tvPhoneRequired.setVisibility(View.GONE);
            for (int i = 0; i < llPhoneContainer.getChildCount(); i++) {
                PlayerInfoPhoneItemView playerInfoPhoneItemView = (PlayerInfoPhoneItemView) llPhoneContainer.getChildAt(i);
                playerInfoPhoneItemView.changeEdit(isEdit);
            }
        }
        if (llEmailContainer.getChildCount() == 0) {
            tvEmailRequired.setVisibility(View.VISIBLE);
        } else {
            tvEmailRequired.setVisibility(View.GONE);
            for (int i = 0; i < llEmailContainer.getChildCount(); i++) {
                PlayerInfoEmailItemView playerInfoPhoneItemView = (PlayerInfoEmailItemView) llEmailContainer.getChildAt(i);
                playerInfoPhoneItemView.changeEdit(isEdit);
            }
        }
    }

    @Override
    protected void executeOnceOnCreate() {
        super.executeOnceOnCreate();
        if (!isAdd) {
            putAgentMessage();
        }
    }

    private void addPhoneView(JsonGeneralInfo.PhoneItem phoneItem) {
        tvPhoneRequired.setVisibility(View.INVISIBLE);
        PlayerInfoPhoneItemView playerInfoTelItemView = new PlayerInfoPhoneItemView(getActivity(),
                getFragmentMode() != BaseEditFragment.FragmentMode.FragmentModeBrowse, phoneItem, llPhoneContainer, onDeleteClickListener);
        llPhoneContainer.addView(playerInfoTelItemView);
        playerInfoTelItemView.setTag(phoneItem);
        LinearLayout.LayoutParams paramsAddressWarn = (LinearLayout.LayoutParams) playerInfoTelItemView.getLayoutParams();
        paramsAddressWarn.height = (int) (getScreenHeight() * 0.08f);
        playerInfoTelItemView.setLayoutParams(paramsAddressWarn);
    }

    private void addEmailView(JsonGeneralInfo.EmailItem emailItem) {
        tvEmailRequired.setVisibility(View.GONE);

        PlayerInfoEmailItemView playerInfoTelItemView = new PlayerInfoEmailItemView(getActivity(),
                getFragmentMode() != FragmentMode.FragmentModeBrowse, emailItem, llEmailContainer, onDeleteClickListener);
        llEmailContainer.addView(playerInfoTelItemView);
        playerInfoTelItemView.setTag(emailItem);
        LinearLayout.LayoutParams paramsAddressWarn = (LinearLayout.LayoutParams) playerInfoTelItemView.getLayoutParams();
        paramsAddressWarn.height = (int) (getScreenHeight() * 0.08f);
        playerInfoTelItemView.setLayoutParams(paramsAddressWarn);

    }

}
