/**
 * Project Name: itee
 * File Name:  PlayerInfoEditFragment.java
 * Package Name: cn.situne.itee.fragment.player
 * Date:   2015-03-22
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.player;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.activity.SelectPhotoActivity;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.ImageUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.teetime.TeeTimeAddFragment;
import cn.situne.itee.fragment.teetime.TeeTimeMemberListWithIndexFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonGeneralInfo;
import cn.situne.itee.manager.jsonentity.JsonReturnMemberId;
import cn.situne.itee.view.CheckSwitchButton;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeNetworkImageView;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.PlayerInfoAddressItemView;
import cn.situne.itee.view.PlayerInfoEmailItemView;
import cn.situne.itee.view.PlayerInfoPhoneItemView;
import cn.situne.itee.view.popwindow.SelectDatePopupWindow;
import cn.situne.itee.view.popwindow.SelectSaveOrNotPopupWindow;

/**
 * ClassName:PlayerInfoEditFragment <br/>
 * Function: login page. <br/>
 * UI:  04-8-9 .04-8-2
 * Date: 2015-03-22 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class PlayerInfoEditFragment extends BaseEditFragment {

//    private boolean isTagWindowShow;

    private Bitmap isPhotoChange;
    //    private JsonMemberProfileGet memberParent;
    private Integer memberId;

    private RelativeLayout rlPhotoContainer;
    private RelativeLayout rlNameContainer;
    private RelativeLayout rlOccupationContainer;
    private RelativeLayout rlBirthContainer;
    private RelativeLayout rlGenderInfoContainer;
    private RelativeLayout rlAcceptContainer;
    private RelativeLayout rlHobbiesContainer;
    private RelativeLayout rlNotesContainer;

    private LinearLayout rlTelContainer;
    private LinearLayout rlEmailContainer;
    private LinearLayout rlAddressContainer;

    private View lineAddressTop, lineAddressBottom;

    private RelativeLayout rlSignatureContainer;
    private LinearLayout llPhoneContent, llEmailContent, llAddressContent;
    private RelativeLayout rlPhoneAdd, rlEmailAdd, rlAddressAdd;
    private IteeTextView tvPhoto;
    private IteeNetworkImageView imPlayer;

    private IteeTextView tvName;
    private IteeEditText tvNameValue;

    private IteeTextView tvOccupation;
    private IteeEditText tvOccupationValue;

    private IteeTextView tvBirth;
    private IteeTextView tvBirthValue;

    private IteeTextView tvGenderInfo;
    private IteeTextView tvGenderInfoValue;

    private IteeTextView tvAccept;
    private CheckSwitchButton switchAccept;
    private String addMemberName;

    private IteeTextView tvHobbies;
    private IteeEditText etHobbiesValue;

    private IteeTextView tvNotes;
    private IteeEditText etNotesValue;

    private IteeTextView tvSignature;

    private IteeTextView tvAddPhone;
    private IteeTextView tvAddEmail;
    private IteeTextView tvAddAddress;

    private IteeTextView tvAddEmailWarn;
    private IteeTextView tvAddPhoneWarn;

    private SelectDatePopupWindow.OnDateSelectClickListener dateSelectReturn;

    private OnDeleteClickListener onDeleteClickListener;
    private JsonGeneralInfo dataParameter;

    private String memberName;
    private String memberTel;
    private String memberTypeId;
    private Bundle bundle;
    private String fromPage;
    private String gender;

    private ArrayList<String> startDataList;

    /**
     * add view about add phone button.
     */
    private void initMovementView() {

        rlPhoneAdd.addView(tvAddPhone);
        RelativeLayout.LayoutParams paramsTelAdd = (RelativeLayout.LayoutParams) tvAddPhone.getLayoutParams();
        paramsTelAdd.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsTelAdd.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTelAdd.width = MATCH_PARENT;
        tvAddPhone.setLayoutParams(paramsTelAdd);
        tvAddPhone.setTextColor(getColor(R.color.common_blue));
        tvAddPhone.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

        rlPhoneAdd.addView(tvAddPhoneWarn);
        RelativeLayout.LayoutParams paramsTelAddWarn = (RelativeLayout.LayoutParams) tvAddPhoneWarn.getLayoutParams();
        paramsTelAddWarn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTelAddWarn.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvAddPhoneWarn.setLayoutParams(paramsTelAddWarn);
        tvAddPhoneWarn.setText(getString(R.string.common_required));
        tvAddPhoneWarn.setTextColor(getColor(R.color.common_red));

        rlEmailAdd.addView(tvAddEmail);
        RelativeLayout.LayoutParams paramsEmailAdd = (RelativeLayout.LayoutParams) tvAddEmail.getLayoutParams();
        paramsEmailAdd.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsEmailAdd.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsEmailAdd.width = MATCH_PARENT;
        tvAddEmail.setLayoutParams(paramsEmailAdd);
        tvAddEmail.setTextColor(getColor(R.color.common_blue));
        tvAddEmail.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

        rlEmailAdd.addView(tvAddEmailWarn);
        RelativeLayout.LayoutParams paramsEmailAddWarn = (RelativeLayout.LayoutParams) tvAddEmailWarn.getLayoutParams();
        paramsEmailAddWarn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsEmailAddWarn.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvAddEmailWarn.setLayoutParams(paramsEmailAddWarn);
        tvAddEmailWarn.setText(getString(R.string.common_required));
        tvAddEmailWarn.setTextColor(getColor(R.color.common_red));

        rlAddressAdd.addView(tvAddAddress);
        RelativeLayout.LayoutParams paramsAddressAdd = (RelativeLayout.LayoutParams) tvAddAddress.getLayoutParams();
        paramsAddressAdd.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsAddressAdd.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsAddressAdd.width = MATCH_PARENT;
        tvAddAddress.setLayoutParams(paramsAddressAdd);
        tvAddAddress.setTextColor(getColor(R.color.common_blue));
        tvAddAddress.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
    }

    /**
     * add phone list item view.
     *
     * @param phoneItem items of phone
     */

    private void addPhoneView(JsonGeneralInfo.PhoneItem phoneItem) {
        tvAddPhoneWarn.setVisibility(View.GONE);
        PlayerInfoPhoneItemView playerInfoTelItemView = new PlayerInfoPhoneItemView(getActivity(),
                getFragmentMode() != FragmentMode.FragmentModeBrowse, phoneItem, llPhoneContent, onDeleteClickListener);
        llPhoneContent.addView(playerInfoTelItemView);
        playerInfoTelItemView.setTag(phoneItem);
        LinearLayout.LayoutParams paramsAddressWarn = (LinearLayout.LayoutParams) playerInfoTelItemView.getLayoutParams();
        paramsAddressWarn.height = (int) (getScreenHeight() * 0.08f);
        playerInfoTelItemView.setLayoutParams(paramsAddressWarn);
    }

    /**
     * add email list item view.
     *
     * @param emailItem items of email
     */

    private void addEmailView(JsonGeneralInfo.EmailItem emailItem) {
        tvAddEmailWarn.setVisibility(View.GONE);

        PlayerInfoEmailItemView playerInfoTelItemView = new PlayerInfoEmailItemView(getActivity(), getFragmentMode() != FragmentMode.FragmentModeBrowse, emailItem, llEmailContent, onDeleteClickListener);
        llEmailContent.addView(playerInfoTelItemView);
        playerInfoTelItemView.setTag(emailItem);
        LinearLayout.LayoutParams paramsAddressWarn = (LinearLayout.LayoutParams) playerInfoTelItemView.getLayoutParams();
        paramsAddressWarn.height = (int) (getScreenHeight() * 0.08f);
        playerInfoTelItemView.setLayoutParams(paramsAddressWarn);

    }

    /**
     * add address list item view.
     *
     * @param addressItem items of address
     */
    private void addAddressView(JsonGeneralInfo.AddressItem addressItem, int index) {
//        tvAddAddressWarn.setVisibility(View.GONE);
        boolean isBrowserMode = getFragmentMode() != FragmentMode.FragmentModeBrowse;
        PlayerInfoAddressItemView playerInfoTelItemView
                = new PlayerInfoAddressItemView(getActivity(), addressItem, index, isBrowserMode, llAddressContent, onDeleteClickListener, this);
        llAddressContent.addView(playerInfoTelItemView);
        playerInfoTelItemView.setTag(addressItem);
        if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
            LinearLayout.LayoutParams paramsAddressWarn = (LinearLayout.LayoutParams) playerInfoTelItemView.getLayoutParams();
            paramsAddressWarn.height = (int) (getScreenHeight() * 0.4f);
            playerInfoTelItemView.setLayoutParams(paramsAddressWarn);
        } else {
            LinearLayout.LayoutParams paramsAddressWarn = (LinearLayout.LayoutParams) playerInfoTelItemView.getLayoutParams();
            paramsAddressWarn.height = (int) (getScreenHeight() * 0.08f);
            playerInfoTelItemView.setLayoutParams(paramsAddressWarn);
        }
    }

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_player_info_edit;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.do_edit_administration;
    }

    @Override
    protected void initControls(View rootView) {

        bundle = getArguments();
        if (bundle != null) {
            memberId = bundle.getInt(TransKey.COMMON_MEMBER_ID);
            memberName = bundle.getString(TransKey.TEE_TIME_MEMBER_NAME, null);
            memberTel = bundle.getString(TransKey.TEE_TIME_MEMBER_TEL, null);
            memberTypeId = bundle.getString(TransKey.TEE_TIME_MEMBER_TYPE_ID, "-1");
            fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE, StringUtils.EMPTY);
            addMemberName = bundle.getString(TransKey.COURSE_ADD_MEMBER_NAME, StringUtils.EMPTY);

            if (bundle.containsKey(TransKey.COMMON_FRAGMENT_MODE)) {
                setFragmentMode(FragmentMode.valueOf(bundle.getInt(TransKey.COMMON_FRAGMENT_MODE)));
            } else {
                setFragmentMode(FragmentMode.FragmentModeBrowse);
            }
        }
        startDataList = new ArrayList<>();

        rlPhotoContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_info_photo);
        rlNameContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_info_name);
        rlOccupationContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_info_occupation);
        rlBirthContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_info_birth);
        rlGenderInfoContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_info_gender_info);
        rlAcceptContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_info_accept);
        rlHobbiesContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_info_hobbies);
        rlNotesContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_info_notes);
        rlTelContainer = (LinearLayout) rootView.findViewById(R.id.rl_play_info_tel);
        rlEmailContainer = (LinearLayout) rootView.findViewById(R.id.rl_play_info_email);
        rlAddressContainer = (LinearLayout) rootView.findViewById(R.id.rl_play_info_address);
        rlSignatureContainer = (RelativeLayout) rootView.findViewById(R.id.rl_play_info_signature);

        llPhoneContent = (LinearLayout) rootView.findViewById(R.id.ll_play_info_tel_content);
        rlPhoneAdd = (RelativeLayout) rootView.findViewById(R.id.ll_play_info_tel_add);
        llEmailContent = (LinearLayout) rootView.findViewById(R.id.ll_play_info_email_content);
        rlEmailAdd = (RelativeLayout) rootView.findViewById(R.id.ll_play_info_email_add);
        llAddressContent = (LinearLayout) rootView.findViewById(R.id.ll_play_info_address_content);
        rlAddressAdd = (RelativeLayout) rootView.findViewById(R.id.ll_play_info_address_add);

        lineAddressTop = rootView.findViewById(R.id.line_address_top);
        lineAddressBottom = rootView.findViewById(R.id.line_address_bottom);

        tvPhoto = new IteeTextView(this);
        imPlayer = new IteeNetworkImageView(getActivity());

        tvName = new IteeTextView(this);
        tvNameValue = new IteeEditText(this);
        tvNameValue.setText(addMemberName);

        tvOccupation = new IteeTextView(this);
        tvOccupationValue = new IteeEditText(getActivity());

        tvBirth = new IteeTextView(this);
        tvBirthValue = new IteeTextView(this);

        tvGenderInfo = new IteeTextView(this);
        tvGenderInfoValue = new IteeTextView(this);

        tvAccept = new IteeTextView(this);
        switchAccept = new CheckSwitchButton(this);

        tvHobbies = new IteeTextView(this);
        etHobbiesValue = new IteeEditText(getActivity());

        tvNotes = new IteeTextView(this);
        etNotesValue = new IteeEditText(getActivity());

        tvSignature = new IteeTextView(this);

        tvAddPhone = new IteeTextView(this);
        tvAddEmail = new IteeTextView(this);
        tvAddAddress = new IteeTextView(this);

        tvAddEmailWarn = new IteeTextView(this);
        tvAddPhoneWarn = new IteeTextView(this);

        onDeleteClickListener = new OnDeleteClickListener() {
            @Override
            public void OnPhoneDeleteClick(String flag, JsonGeneralInfo.PhoneItem phoneItem) {
                dataParameter.getDataList().getPhoneList().remove(phoneItem);
                if (dataParameter.getDataList().getPhoneList().size() == 0) {
                    tvAddPhoneWarn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void OnEmailDeleteClick(String flag, JsonGeneralInfo.EmailItem emailItem) {
                dataParameter.getDataList().getEmailList().remove(emailItem);
                if (dataParameter.getDataList().getEmailList().size() == 0) {
                    tvAddEmailWarn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void OnAddressDeleteClick(String flag, JsonGeneralInfo.AddressItem addressItem) {
                dataParameter.getDataList().getAddressList().remove(addressItem);
            }
        };

    }

    private void initEditView() {

        if (dataParameter != null) {
            JsonGeneralInfo.DataList dataList = dataParameter.getDataList();
            if (Utils.isStringNotNullOrEmpty(dataList.getMemberPhoto())) {
                AppUtils.showNetworkImage(imPlayer, dataList.getMemberPhoto());
            } else {
                AppUtils.showNetworkImage(imPlayer, Constants.PHOTO_DEFAULT_URL);
            }

            tvNameValue.setText(dataList.getMemberName());
            tvOccupationValue.setText(dataList.getOccupation());
            tvBirthValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(dataList.getBirth(), getActivity()));
            gender = dataList.getGender();

            if (Constants.MALE.equals(gender)) {
                tvGenderInfoValue.setText(getString(R.string.common_male));
            } else {
                tvGenderInfoValue.setText(getString(R.string.common_female));
            }

            etHobbiesValue.setText(dataList.getHobbies());
            etNotesValue.setText(dataList.getNotes());
            if (dataList.getAcceptInfoStatus() == 1) {
                switchAccept.setChecked(true);
            } else {
                switchAccept.setChecked(false);
            }

            if (dataList.getPhoneList() != null && dataList.getPhoneList().size() > 0) {
                llPhoneContent.removeAllViews();
                rlPhoneAdd.setVisibility(View.GONE);
                for (int i = 0; i < dataList.getPhoneList().size(); i++) {
                    addPhoneView(dataList.getPhoneList().get(i));
                }
            }

            if (dataList.getEmailList() != null && dataList.getEmailList().size() > 0) {
                llEmailContent.removeAllViews();
                rlEmailAdd.setVisibility(View.GONE);
                for (int i = 0; i < dataList.getEmailList().size(); i++) {
                    addEmailView(dataList.getEmailList().get(i));
                }
            }

            if (dataList.getAddressList() != null && dataList.getAddressList().size() > 0) {
                llAddressContent.removeAllViews();
                rlAddressAdd.setVisibility(View.GONE);
                lineAddressTop.setVisibility(View.VISIBLE);
                lineAddressBottom.setVisibility(View.VISIBLE);
                for (int i = 0; i < dataList.getAddressList().size(); i++) {
                    addAddressView(dataList.getAddressList().get(i), i);
                }
            } else {
                lineAddressTop.setVisibility(View.GONE);
                lineAddressBottom.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void reShowWithBackValue() {
        if (getReturnValues() != null) {
            String fromPage = getReturnValues().getString(TransKey.COMMON_FROM_PAGE);
            if (Utils.isStringNotNullOrEmpty(fromPage) && PlayerSignatureEditFragment.class.getName().equals(fromPage)) {
                netLinkGeneralInfo();
            } else {
                Bundle params = getReturnValues();
                String index = params.getString("index");
                for (int i = 0; i < llAddressContent.getChildCount(); i++) {
                    PlayerInfoAddressItemView playerInfoAddressItemView = (PlayerInfoAddressItemView) llAddressContent.getChildAt(i);
                    if (Integer.valueOf(index) == i) {

                        if (params.containsKey("country")) {
                            playerInfoAddressItemView.tvCountry.setText(params.getString("country"));
                            JsonGeneralInfo.AddressItem phoneItemTemp = (JsonGeneralInfo.AddressItem) playerInfoAddressItemView.getTag();
                            phoneItemTemp.setCountry(params.getString("country"));
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void setDefaultValueOfControls() {

        if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
            AppUtils.showNetworkImage(imPlayer, Constants.PHOTO_DEFAULT_URL);
            dataParameter = new JsonGeneralInfo(null);
            Calendar calendar = Calendar.getInstance();
            tvBirthValue.setText(DateUtils.getCurrentShowYearMonthDayFromDate(calendar.getTime(), getActivity()));
            gender = Constants.MALE;
            tvGenderInfoValue.setText(getString(R.string.common_male));
            tvSignature.setText(getString(R.string.player_info_edit_signature) + getString(R.string.common_operate_after_adding));
        } else {
            tvSignature.setText(getString(R.string.player_info_edit_signature));
        }

        tvPhoto.setText(getString(R.string.player_info_edit_photo));
        tvName.setText(getString(R.string.player_info_edit_name));
        tvOccupation.setText(getString(R.string.player_info_edit_occupaation));
        tvBirth.setText(getString(R.string.player_info_edit_birth));
        tvGenderInfo.setText(getString(R.string.player_info_edit_gender_info));
        tvAccept.setText(getString(R.string.player_info_edit_accept));
        tvHobbies.setText(getString(R.string.player_info_edit_hobbies));
        tvNotes.setText(getString(R.string.player_info_edit_notes));

        tvAddPhone.setText(getString(R.string.player_info_edit_add_phone));
        tvAddEmail.setText(getString(R.string.player_info_edit_add_email));
        tvAddAddress.setText(getString(R.string.player_info_edit_add_address));
    }

    @Override
    protected void setListenersOfControls() {


        tvAddPhone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int childCount = llPhoneContent.getChildCount();
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
                dataParameter.getDataList().getPhoneList().add(addressItem);
                addPhoneView(addressItem);
            }
        });

        tvAddEmail.setOnClickListener(new IteeTextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                int childCount = llEmailContent.getChildCount();

                String tag;
                switch (childCount) {
                    case 0:
                        tag = getString(R.string.tag_home);
                        break;
                    case 1:
                        tag = getString(R.string.tag_work);
                        break;
                    case 2:
                        tag = getString(R.string.tag_other);
                        break;
                    default:
                        tag = getString(R.string.tag_home);
                        break;
                }
                JsonGeneralInfo.EmailItem addressItem = new JsonGeneralInfo.EmailItem();
                addressItem.setTag(tag);
                dataParameter.getDataList().getEmailList().add(addressItem);
                addEmailView(addressItem);


//                //1:home 2:work 3:mobile 4:other 5:custom
//                OnDateSelectClickListener onDateSelectClickListener = new OnDateSelectClickListener() {
//                    @Override
//                    public void OnGoodItemClick(String flag, String content) {
//
//                        String tag = null;
//                        switch (flag) {
//                            case "1":
//                                tag = getString(R.string.tag_home);
//                                break;
//                            case "2":
//                                tag = getString(R.string.tag_work);
//                                break;
//                            case "3":
//                                tag = getString(R.string.tag_mobile);
//                                break;
//                            case "4":
//                                tag = getString(R.string.tag_other);
//                                break;
//                            case "5":
//                                tag = content;
//                                break;
//                            default:
//                                break;
//                        }
//                        JsonGeneralInfo generalInfo = new JsonGeneralInfo(null);
//                        JsonGeneralInfo.EmailItem addressItem = generalInfo.new EmailItem();
//                        addressItem.setTag(tag);
//                        dataParameter.getMemberList().getEmailList().add(addressItem);
//                        addEmailView(addressItem);
//
//                    }
//                };
//                AddressTypePopupWindow menuWindow = new AddressTypePopupWindow(getActivity(), onDateSelectClickListener, null);
//                menuWindow.showAtLocation(getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });


        tvAddAddress.setOnClickListener(new IteeTextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                int childCount = llAddressContent.getChildCount();
                String tag;
                switch (childCount) {
                    case 0:
                        tag = getString(R.string.tag_home);
                        break;
                    case 1:
                        tag = getString(R.string.tag_work);
                        break;
                    case 2:
                        tag = getString(R.string.tag_other);
                        break;
                    default:
                        tag = getString(R.string.tag_home);
                        break;
                }
                JsonGeneralInfo.AddressItem addressItem = new JsonGeneralInfo.AddressItem();
                addressItem.setTag(tag);
                dataParameter.getDataList().getAddressList().add(addressItem);
                addAddressView(addressItem, dataParameter.getDataList().getAddressList().size() - 1);

            }
        });

        switchAccept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dataParameter.getDataList().setAcceptInfoStatus(1);
                } else {
                    dataParameter.getDataList().setAcceptInfoStatus(0);
                }
            }
        });

        imPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), SelectPhotoActivity.class);
                    startActivityForResult(intent, SelectPhotoActivity.REQUEST_CODE_SET);
                }
            }
        });

        rlBirthContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                    Utils.hideKeyboard(getActivity());

                    dateSelectReturn = new SelectDatePopupWindow.OnDateSelectClickListener() {
                        @Override
                        public void OnGoodItemClick(String flag, String content) {
                            switch (flag) {
                                case Constants.DATE_RETURN:
                                    tvBirthValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(content, mContext));
                                    break;
                            }

                        }
                    };
                    SelectDatePopupWindow menuWindow = new SelectDatePopupWindow(getActivity(), tvBirthValue.getText()
                            .toString(), dateSelectReturn);
                    menuWindow.showAtLocation(getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
        });


        rlGenderInfoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TeeTimeAddFragment.OnPopupWindowWheelClickListener onPopupWindowWheelClickListener = new TeeTimeAddFragment.OnPopupWindowWheelClickListener() {
                    @Override
                    public void onSaveClick(String a, String b) {
                        if (Constants.MALE.equals(b)) {
                            tvGenderInfoValue.setText(getString(R.string.common_male));
                            gender = Constants.MALE;
                        } else {
                            tvGenderInfoValue.setText(getString(R.string.common_female));
                            gender = Constants.FEMALE;
                        }

                    }
                };

                if (getFragmentMode() != FragmentMode.FragmentModeBrowse && getView() != null) {
                    SelectSaveOrNotPopupWindow savePopupWindow
                            = new SelectSaveOrNotPopupWindow(PlayerInfoEditFragment.this, onPopupWindowWheelClickListener, 4, null, null);
                    savePopupWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }

            }
        });
        rlSignatureContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentMode() == FragmentMode.FragmentModeEdit) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("memberId", memberId);
                    bundle.putString("signature", dataParameter.getDataList().getSianature());
                    bundle.putString(TransKey.COMMON_FROM_PAGE, PlayerInfoEditFragment.class.getName());
                    push(PlayerSignatureEditFragment.class, bundle);
                }
            }
        });
    }

    @Override
    protected void setLayoutOfControls() {

        LinearLayout.LayoutParams rlPhotoLayoutPhoto = (LinearLayout.LayoutParams) rlPhotoContainer.getLayoutParams();
        rlPhotoLayoutPhoto.height = (int) (getScreenHeight() * 0.14f);

        LinearLayout.LayoutParams rlPhotoLayout = (LinearLayout.LayoutParams) rlNameContainer.getLayoutParams();
        rlPhotoLayout.height = (int) (getScreenHeight() * 0.08f);

        rlPhotoContainer.setLayoutParams(rlPhotoLayoutPhoto);
        rlNameContainer.setLayoutParams(rlPhotoLayout);
        rlOccupationContainer.setLayoutParams(rlPhotoLayout);
        rlBirthContainer.setLayoutParams(rlPhotoLayout);
        rlGenderInfoContainer.setLayoutParams(rlPhotoLayout);
        rlAcceptContainer.setLayoutParams(rlPhotoLayout);
        rlHobbiesContainer.setLayoutParams(rlPhotoLayout);
        rlNotesContainer.setLayoutParams(rlPhotoLayout);
        rlSignatureContainer.setLayoutParams(rlPhotoLayout);

        rlPhotoContainer.setPadding(20, 0, 20, 0);
        rlNameContainer.setPadding(20, 0, 20, 0);
        rlOccupationContainer.setPadding(20, 0, 20, 0);
        rlBirthContainer.setPadding(20, 0, 20, 0);
        rlGenderInfoContainer.setPadding(20, 0, 20, 0);
        rlAcceptContainer.setPadding(20, 0, 20, 0);
        rlHobbiesContainer.setPadding(20, 0, 20, 0);
        rlNotesContainer.setPadding(20, 0, 20, 0);
        rlTelContainer.setPadding(20, 0, 20, 0);
        rlEmailContainer.setPadding(20, 0, 20, 0);
        rlAddressContainer.setPadding(20, 0, 20, 0);
        rlSignatureContainer.setPadding(20, 0, 20, 0);

        LinearLayout.LayoutParams llTelContentLayoutParams = (LinearLayout.LayoutParams) rlPhoneAdd.getLayoutParams();
        llTelContentLayoutParams.height = (int) (getScreenHeight() * 0.08f);


//        llPhoneContent.setLayoutParams(llTelContentLayoutParams);
        rlPhoneAdd.setLayoutParams(llTelContentLayoutParams);
//        llEmailContent.setLayoutParams(llTelContentLayoutParams);
        rlEmailAdd.setLayoutParams(llTelContentLayoutParams);
//        llAddressContent.setLayoutParams(llTelContentLayoutParams);
        rlAddressAdd.setLayoutParams(llTelContentLayoutParams);

        llPhoneContent.setPadding(20, 0, 20, 0);
        rlPhoneAdd.setPadding(20, 0, 20, 0);
        llEmailContent.setPadding(20, 0, 20, 0);
        rlEmailAdd.setPadding(20, 0, 20, 0);
        llAddressContent.setPadding(20, 0, 20, 0);
        rlAddressAdd.setPadding(20, 0, 20, 0);


        rlPhotoContainer.addView(tvPhoto);
        RelativeLayout.LayoutParams paramsEtLogoutAfterTimeM = (RelativeLayout.LayoutParams) tvPhoto.getLayoutParams();
        paramsEtLogoutAfterTimeM.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsEtLogoutAfterTimeM.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsEtLogoutAfterTimeM.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsEtLogoutAfterTimeM.setMargins(20, 0, 0, 0);
        tvPhoto.setLayoutParams(paramsEtLogoutAfterTimeM);


        rlPhotoContainer.addView(imPlayer);
        RelativeLayout.LayoutParams paramsPhotoImageViewValue = (RelativeLayout.LayoutParams) imPlayer.getLayoutParams();
        paramsPhotoImageViewValue.width = getActualWidthOnThisDevice(160);
        paramsPhotoImageViewValue.height = getActualWidthOnThisDevice(120);
        paramsPhotoImageViewValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsPhotoImageViewValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        imPlayer.setLayoutParams(paramsPhotoImageViewValue);

        rlNameContainer.addView(tvName);
        RelativeLayout.LayoutParams paramsName = (RelativeLayout.LayoutParams) tvName.getLayoutParams();
        paramsName.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsName.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsName.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsName.setMargins(20, 0, 0, 0);
        tvName.setLayoutParams(paramsName);


        rlNameContainer.addView(tvNameValue);
        RelativeLayout.LayoutParams paramsNameValue = (RelativeLayout.LayoutParams) tvNameValue.getLayoutParams();
        paramsNameValue.width = (int) (getScreenWidth() * 0.7f);
        paramsNameValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsNameValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsNameValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvNameValue.setLayoutParams(paramsNameValue);

        rlOccupationContainer.addView(tvOccupation);
        RelativeLayout.LayoutParams paramsOccupation = (RelativeLayout.LayoutParams) tvOccupation.getLayoutParams();
        paramsOccupation.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsOccupation.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsOccupation.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsOccupation.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsOccupation.setMargins(20, 0, 0, 0);
        tvOccupation.setLayoutParams(paramsOccupation);

        rlOccupationContainer.addView(tvOccupationValue);
        RelativeLayout.LayoutParams paramsOccupationValue = (RelativeLayout.LayoutParams) tvOccupationValue.getLayoutParams();
        paramsOccupationValue.width = (int) (getScreenWidth() * 0.7f);
        paramsOccupationValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsOccupationValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsOccupationValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvOccupationValue.setLayoutParams(paramsOccupationValue);

        rlBirthContainer.addView(tvBirth);
        RelativeLayout.LayoutParams paramsBirth = (RelativeLayout.LayoutParams) tvBirth.getLayoutParams();
        paramsBirth.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBirth.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBirth.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsBirth.setMargins(20, 0, 0, 0);
        tvBirth.setLayoutParams(paramsBirth);

        rlBirthContainer.addView(tvBirthValue);
        RelativeLayout.LayoutParams paramsBirthValue = (RelativeLayout.LayoutParams) tvBirthValue.getLayoutParams();
        paramsBirthValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBirthValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBirthValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsBirthValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvBirthValue.setLayoutParams(paramsBirthValue);

        rlGenderInfoContainer.addView(tvGenderInfo);
        RelativeLayout.LayoutParams paramsGenderInfo = (RelativeLayout.LayoutParams) tvGenderInfo.getLayoutParams();
        paramsGenderInfo.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsGenderInfo.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsGenderInfo.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsGenderInfo.setMargins(20, 0, 0, 0);
        tvGenderInfo.setLayoutParams(paramsGenderInfo);

        rlGenderInfoContainer.addView(tvGenderInfoValue);
        RelativeLayout.LayoutParams paramsGenderInfoValue = (RelativeLayout.LayoutParams) tvGenderInfoValue.getLayoutParams();
        paramsGenderInfoValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsGenderInfoValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsGenderInfoValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsGenderInfoValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvGenderInfoValue.setLayoutParams(paramsGenderInfoValue);

        rlAcceptContainer.addView(tvAccept);
        RelativeLayout.LayoutParams paramsAccept = (RelativeLayout.LayoutParams) tvAccept.getLayoutParams();
        paramsAccept.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAccept.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAccept.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsAccept.setMargins(20, 0, 0, 0);
        tvAccept.setLayoutParams(paramsAccept);

        rlAcceptContainer.addView(switchAccept);
        RelativeLayout.LayoutParams paramsSwitchAccept = (RelativeLayout.LayoutParams) switchAccept.getLayoutParams();
        paramsSwitchAccept.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsSwitchAccept.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsSwitchAccept.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsSwitchAccept.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        switchAccept.setLayoutParams(paramsSwitchAccept);

        rlHobbiesContainer.addView(tvHobbies);
        RelativeLayout.LayoutParams paramsHobbies = (RelativeLayout.LayoutParams) tvHobbies.getLayoutParams();
        paramsHobbies.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsHobbies.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsHobbies.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsHobbies.setMargins(20, 0, 0, 0);
        tvHobbies.setLayoutParams(paramsHobbies);
        tvHobbies.setId(View.generateViewId());

        rlHobbiesContainer.addView(etHobbiesValue);
        RelativeLayout.LayoutParams paramsTvHobbiesValueLayoutParams = (RelativeLayout.LayoutParams) etHobbiesValue.getLayoutParams();
        paramsTvHobbiesValueLayoutParams.width = (int) (getScreenWidth() * 0.7f);
        paramsTvHobbiesValueLayoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvHobbiesValueLayoutParams.addRule(RelativeLayout.RIGHT_OF, tvHobbies.getId());
        paramsTvHobbiesValueLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvHobbiesValueLayoutParams.setMargins(20, 0, 0, 0);
        etHobbiesValue.setLayoutParams(paramsTvHobbiesValueLayoutParams);

        rlNotesContainer.addView(tvNotes);
        RelativeLayout.LayoutParams paramsTvNotes = (RelativeLayout.LayoutParams) tvNotes.getLayoutParams();
        paramsTvNotes.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvNotes.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvNotes.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvNotes.setMargins(20, 0, 0, 0);
        tvNotes.setLayoutParams(paramsTvNotes);
        tvNotes.setId(View.generateViewId());

        rlNotesContainer.addView(etNotesValue);
        RelativeLayout.LayoutParams paramsTvNotesValue = (RelativeLayout.LayoutParams) etNotesValue.getLayoutParams();
        paramsTvNotesValue.width = (int) (getScreenWidth() * 0.8f);
        paramsTvNotesValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvNotesValue.addRule(RelativeLayout.RIGHT_OF, tvNotes.getId());
        paramsTvNotes.setMargins(20, 0, 0, 0);
        paramsTvNotesValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        etNotesValue.setLayoutParams(paramsTvNotesValue);


        rlSignatureContainer.addView(tvSignature);
        RelativeLayout.LayoutParams paramsTvSignature = (RelativeLayout.LayoutParams) tvSignature.getLayoutParams();
        paramsTvSignature.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvSignature.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvSignature.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsTvSignature.setMargins(20, 0, 0, 0);
        paramsTvSignature.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvSignature.setLayoutParams(paramsTvSignature);
    }

    @Override
    protected void setPropertyOfControls() {

        initMovementView();
        tvPhoto.setTextColor(getColor(R.color.common_black));
        tvName.setTextColor(getColor(R.color.common_black));
        tvNameValue.setTextColor(getColor(R.color.common_black));
        tvNameValue.setGravity(Gravity.END);
        tvNameValue.setSingleLine();
        tvNameValue.setBackground(null);

        tvOccupation.setTextColor(getColor(R.color.common_black));
        tvOccupationValue.setTextColor(getColor(R.color.common_black));
        tvOccupationValue.setBackground(null);
        tvOccupationValue.setGravity(Gravity.END);

        tvBirth.setTextColor(getColor(R.color.common_black));
        tvBirth.setTextColor(getColor(R.color.common_black));

        tvGenderInfo.setTextColor(getColor(R.color.common_black));
        tvGenderInfoValue.setTextColor(getColor(R.color.common_black));

        tvAccept.setTextColor(getColor(R.color.common_black));
        switchAccept.setTextColor(getColor(R.color.common_black));
        switchAccept.setTextColor(getColor(R.color.common_black));

        tvHobbies.setTextColor(getColor(R.color.common_black));
        etHobbiesValue.setTextColor(getColor(R.color.common_black));
        etHobbiesValue.setBackground(null);
        tvNotes.setTextColor(getColor(R.color.common_black));
        etNotesValue.setTextColor(getColor(R.color.common_black));
        etNotesValue.setBackground(null);

        tvSignature.setTextColor(getColor(R.color.common_black));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case SelectPhotoActivity.REQUEST_CODE_SET:
                if (resultCode == FragmentActivity.RESULT_OK) {
                    byte[] b = data.getByteArrayExtra("bitmap");
                    if (b!=null&& b.length>0){
                        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                        if (bitmap != null) {

//                        if (isPhotoChange!=null){
//                            imPlayer.setBackground(null);
//
//                            isPhotoChange.recycle();
//                            isPhotoChange =null;
//                        }

                            imPlayer.setImageBitmap(bitmap);
                            isPhotoChange = bitmap;
                        }

                    }


                }
                break;
            case Constants.REQUEST_CODE_SELECT_ADDRESS:
                if (resultCode == FragmentActivity.RESULT_OK) {
                    String passedIndex = data.getStringExtra(TransKey.COMMON_INDEX);
                    String country = data.getStringExtra(TransKey.COMMON_ADDRESS_COUNTRY);
                    String province = data.getStringExtra(TransKey.COMMON_ADDRESS_PROVINCE);
                    String city = data.getStringExtra(TransKey.COMMON_ADDRESS_CITY);
                    String postCode = data.getStringExtra(TransKey.COMMON_ADDRESS_POST_CODE);
                    if (StringUtils.isNotEmpty(passedIndex)) {
                        int idx = Integer.valueOf(passedIndex);
                        PlayerInfoAddressItemView addressItemView = (PlayerInfoAddressItemView) llAddressContent.getChildAt(idx);
                        addressItemView.setCountry(country);
                        addressItemView.setProvince(province);
                        addressItemView.setCity(city);
                        addressItemView.setPostCode(postCode);
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
            getTvLeftTitle().setText(getResources().getString(R.string.player_info_edit_add_gender_info));
            getTvRight().setBackground(null);
            getTvRight().setText(R.string.common_ok);
        } else {
            getTvLeftTitle().setText(getResources().getString(R.string.player_info_edit_general_info));
            changeEdit();
        }
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.hideKeyboard(getActivity());
                if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
                    boolean isCanSave = doCheck();
                    if (isCanSave) {
                        netLinkAddGeneralInfo();
                    }
                } else {
                    //save data.
                    if (getFragmentMode() == FragmentMode.FragmentModeEdit) {
                        boolean isCanSave = doCheck();
                        if (isCanSave) {
                            netLinkEditGeneralInfo();
                        }
                    } else {
                        setFragmentMode(FragmentMode.FragmentModeEdit);
                        changeEdit();
                    }
                }
            }
        });
    }

    private boolean doCheck() {
        JsonGeneralInfo.DataList dataList = dataParameter.getDataList();
        //is null check
        if (Utils.isStringNullOrEmpty(tvNameValue.getText().toString())) {
            tvNameValue.setHint(getString(R.string.common_required));
            tvNameValue.setHintTextColor(getColor(R.color.common_red));
            Utils.showShortToast(mContext, R.string.staff_fill_name);
            return false;
//        }
//        else if (Utils.isStringNullOrEmpty(tvOccupationValue.getText().toString())) {
//            tvOccupationValue.setHint(getString(R.string.common_required));
//            tvOccupationValue.setHintTextColor(getColor(R.color.common_red));
//            return false;
        } else if (dataList.getPhoneList().size() == 0) {
            Utils.showShortToast(mContext, R.string.staff_add_one_phone_number_at_least);
            return false;
        } else if (dataList.getEmailList().size() == 0) {
            Utils.showShortToast(mContext, R.string.staff_add_one_email_at_least);
            return false;
        }
        //
        for (int i = 0; i < dataList.getPhoneList().size(); i++) {
            JsonGeneralInfo.PhoneItem phoneItem = dataList.getPhoneList().get(i);
            if (!Utils.isMobile(phoneItem.getValue())) {
                Utils.showShortToast(mContext, R.string.error_mes00003);
                return false;
            }
        }

        //
        for (int i = 0; i < dataList.getEmailList().size(); i++) {
            JsonGeneralInfo.EmailItem phoneItem = dataList.getEmailList().get(i);
            if (!Utils.isEmail(phoneItem.getValue())) {
                Utils.showShortToast(mContext, R.string.error_mes00004);
                return false;
            }
        }
        return true;
    }

    private boolean doBackCheck() {
        String switchAcceptStr = Constants.STR_0;
        if (switchAccept.isChecked()) {
            switchAcceptStr = Constants.STR_1;
        }


        String tvGenderInfoValueString = getString(R.string.common_female);

        if (Constants.MALE.equals(startDataList.get(3))) {
            tvGenderInfoValueString = getString(R.string.common_male);
        }

        if (!tvNameValue.getText().toString().equals(startDataList.get(0))) {
            return true;
        } else if (!tvOccupationValue.getText().toString().equals(startDataList.get(1))) {
            return true;
        } else if (!tvBirthValue.getText().toString().equals(startDataList.get(2))) {
            return true;
        } else if (!tvGenderInfoValueString.equals(tvGenderInfoValue.getText().toString())) {
            return true;
        } else if (!switchAcceptStr.equals(startDataList.get(4))) {
            return true;
        } else if (!etHobbiesValue.getText().toString().equals(startDataList.get(5))) {
            return true;
        } else if (!etNotesValue.getText().toString().equals(startDataList.get(6))) {
            return true;
        } else if (isPhotoChange != null) {
            return true;
        }
        return false;
    }

    private void changeEdit() {
        boolean isEdit = getFragmentMode() != FragmentMode.FragmentModeBrowse;
        if (isEdit) {
            getTvRight().setBackground(null);
            getTvRight().setText(R.string.common_ok);
            imPlayer.setEnabled(true);
            rlPhoneAdd.setVisibility(View.VISIBLE);
            rlEmailAdd.setVisibility(View.VISIBLE);
            rlAddressAdd.setVisibility(View.VISIBLE);

            setOnBackListener(new OnPopBackListener() {
                @Override
                public boolean preDoBack() {

                    if (doBackCheck()) {
                        TeeTimeAddFragment.OnPopupWindowWheelClickListener onPopupWindowWheelClickListener = new TeeTimeAddFragment.OnPopupWindowWheelClickListener() {
                            @Override
                            public void onSaveClick(String a, String b) {
                                setOnBackListener(null);
                                if ("cancel".equals(a)) {
                                    if (fromPage.equals(TeeTimeMemberListWithIndexFragment.class.getName())) {
                                        doBackWithRefresh(TeeTimeMemberListWithIndexFragment.class);
                                    } else {
                                        doBackWithRefresh();
                                    }
                                } else {
                                    netLinkEditGeneralInfo();
                                }
                            }
                        };
                        if (getView() != null) {
                            SelectSaveOrNotPopupWindow savePopupWindow
                                    = new SelectSaveOrNotPopupWindow(PlayerInfoEditFragment.this, onPopupWindowWheelClickListener, 1, null, null);
                            savePopupWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        }
                    } else {
                        setOnBackListener(null);
                        if (fromPage.equals(TeeTimeMemberListWithIndexFragment.class.getName())) {
                            doBackWithRefresh(TeeTimeMemberListWithIndexFragment.class);
                        } else {
                            doBackWithRefresh();
                        }
                    }
                    return false;
                }
            });
        } else {
            getTvRight().setBackgroundResource(R.drawable.administration_icon_edit);
            getTvRight().setText(Constants.STR_EMPTY);
            rlPhoneAdd.setVisibility(View.GONE);
            rlEmailAdd.setVisibility(View.GONE);
            rlAddressAdd.setVisibility(View.GONE);
            setOnBackListener(null);
        }
        tvNameValue.setEnabled(isEdit);
        tvOccupationValue.setEnabled(isEdit);
        etHobbiesValue.setEnabled(isEdit);
        etNotesValue.setEnabled(isEdit);
        switchAccept.setEnabled(isEdit);

        tvNameValue.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        tvOccupationValue.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        etHobbiesValue.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        etNotesValue.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        if (llPhoneContent.getChildCount() == 0) {
            tvAddPhoneWarn.setVisibility(View.VISIBLE);
        } else {
            tvAddPhoneWarn.setVisibility(View.GONE);
            for (int i = 0; i < llPhoneContent.getChildCount(); i++) {
                PlayerInfoPhoneItemView playerInfoPhoneItemView = (PlayerInfoPhoneItemView) llPhoneContent.getChildAt(i);
                playerInfoPhoneItemView.changeEdit(isEdit);
            }
        }
        if (llEmailContent.getChildCount() == 0) {
            tvAddEmailWarn.setVisibility(View.VISIBLE);
        } else {
            tvAddEmailWarn.setVisibility(View.GONE);
            for (int i = 0; i < llEmailContent.getChildCount(); i++) {
                PlayerInfoEmailItemView playerInfoPhoneItemView = (PlayerInfoEmailItemView) llEmailContent.getChildAt(i);
                playerInfoPhoneItemView.changeEdit(isEdit);
            }
        }

        if (llAddressContent.getChildCount() != 0) {
            llAddressContent.removeAllViews();
            if (dataParameter.getDataList().getAddressList() != null && dataParameter.getDataList().getAddressList().size() > 0) {
                for (int i = 0; i < dataParameter.getDataList().getAddressList().size(); i++) {
                    addAddressView(dataParameter.getDataList().getAddressList().get(i), i);
                }
            }
        }
    }

    @Override
    protected void executeOnceOnCreate() {
        if (bundle != null) {
            if (getFragmentMode() != FragmentMode.FragmentModeAdd) {
                netLinkGeneralInfo();
            }
        }


        //from incoming param.
        if (memberName != null) {
            tvNameValue.setText(memberName);
        }
        if (memberTel != null) {
            JsonGeneralInfo.PhoneItem addressItem = new JsonGeneralInfo.PhoneItem();
            addressItem.setTag(getString(R.string.tag_mobile));
            addressItem.setValue(memberTel);
            addressItem.setStatus(1);
            dataParameter.getDataList().getPhoneList().add(addressItem);
            addPhoneView(addressItem);
        }
    }

    private void uploadPhoto(Bitmap bitmap, final String id, final boolean isEdit) {

        Bitmap bitmapTemp = ImageUtils.imageZoom(bitmap);
        byte[] b = ImageUtils.convertBitmap2Bytes(bitmapTemp);
        final HashMap<String, byte[]> files = new HashMap<>();
        files.put("img", b);
        final HashMap<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(mContext));
        params.put("type", "1");
        params.put("id", id);

        final HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(PlayerInfoEditFragment.this) {
            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                if (isEdit) {
                    if (fromPage.equals(TeeTimeMemberListWithIndexFragment.class.getName())) {
                        getBaseActivity().doBackWithRefresh(TeeTimeMemberListWithIndexFragment.class);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("fromFlag", "refresh");
                        getBaseActivity().doBackWithReturnValue(bundle, PlayerFragment.class);
                    }
                } else {
                    if (fromPage.equals(TeeTimeMemberListWithIndexFragment.class.getName())) {
                        doBackWithRefresh(TeeTimeMemberListWithIndexFragment.class);
                    } else {

                        if (Utils.isStringNotNullOrEmpty(fromPage)){

                            Bundle bundle = new Bundle();
                            bundle.putString("addId",id);
                            bundle.putString(TransKey.COMMON_FROM_PAGE,PlayerInfoEditFragment.class.getName());
                            try {
                                doBackWithReturnValue(bundle,(Class<? extends BaseFragment>) Class.forName(fromPage));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }


                        }else{

                            doBackWithRefresh();
                        }

                    }
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.uploadFile(getActivity(), ApiManager.HttpApi.CommonPhoto, files, params);
    }

    /**
     * get generalInfo data.
     */

    private void netLinkGeneralInfo() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.PLAYER_MEMBER_ID, String.valueOf(memberId));

        HttpManager<JsonGeneralInfo> hh = new HttpManager<JsonGeneralInfo>(PlayerInfoEditFragment.this) {

            @Override
            public void onJsonSuccess(JsonGeneralInfo jo) {
                dataParameter = jo;
                startDataList.add(jo.getDataList().getMemberName());
                startDataList.add(jo.getDataList().getOccupation());
                startDataList.add(jo.getDataList().getBirth());
                startDataList.add(jo.getDataList().getGender());
                startDataList.add(String.valueOf(jo.getDataList().getAcceptInfoStatus()));
                startDataList.add(jo.getDataList().getHobbies());
                startDataList.add(jo.getDataList().getNotes());
                initEditView();
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.GeneralInfo, params);
    }

    /**
     * save GeneralInfo data. (save)
     */
    private void netLinkEditGeneralInfo() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.PLAYER_MEMBER_ID, String.valueOf(memberId));
        params.put(ApiKey.PLAYER_MEMBER_NAME, tvNameValue.getText().toString());
        params.put(ApiKey.PLAYER_MEMBER_GENERALINFO_OCCUPATION, tvOccupationValue.getText().toString());
        String birth = DateUtils.getAPIYearMonthDayFromCurrentShow(tvBirthValue.getText().toString(), mContext);
        params.put(ApiKey.PLAYER_MEMBER_GENERALINFO_BIRTH, birth);
        params.put(ApiKey.PLAYER_MEMBER_GENERALINFO_GENDER, gender);
        JsonGeneralInfo.DataList dataList = dataParameter.getDataList();
        params.put(ApiKey.PLAYER_MEMBER_GENERALINFO_ACCEPT_INFO_STATUS,
                String.valueOf(dataList.getAcceptInfoStatus()));
        params.put(ApiKey.PLAYER_MEMBER_GENERALINFO_HOBBIES, etHobbiesValue.getText().toString());
        params.put(ApiKey.PLAYER_MEMBER_GENERALINFO_NOTES, etNotesValue.getText().toString());
        ObjectMapper objectMapper = new ObjectMapper();
        String phoneListString = null;
        String emailListString = null;
        String addressListString = null;
        try {
            dataList.getPhoneList().get(0).setStatus(1);
            if (dataParameter.getDataList().getEmailList().size() > 0) {
                dataParameter.getDataList().getEmailList().get(0).setStatus(1);
            }
            if (dataList.getAddressList().size() != 0) {
                dataList.getAddressList().get(0).setStatus(1);
            }
            phoneListString = objectMapper.writeValueAsString(dataList.getPhoneList());
            emailListString = objectMapper.writeValueAsString(dataList.getEmailList());
            addressListString = objectMapper.writeValueAsString(dataList.getAddressList());
        } catch (Exception e) {
            Utils.log(e.getMessage());
        }

        params.put(ApiKey.PLAYER_MEMBER_GENERALINFO_PHONE_LIST, phoneListString);
        if (StringUtils.isNotEmpty(emailListString)) {
            params.put(ApiKey.PLAYER_MEMBER_GENERALINFO_EMAIL_LIST, emailListString);
        }
        params.put(ApiKey.PLAYER_MEMBER_GENERALINFO_ADDRESS_LIST, addressListString);

        HttpManager<JsonGeneralInfo> hh = new HttpManager<JsonGeneralInfo>(PlayerInfoEditFragment.this) {

            @Override
            public void onJsonSuccess(JsonGeneralInfo jo) {

                if (jo.getReturnCode() == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    setOnBackListener(null);

                    if (Constants.STR_1.equals(jo.getDataList().getMsgFlag())) {
                        AppUtils.showMessageWithOkButton(PlayerInfoEditFragment.this, jo.getDataList()
                                .getMsgInfo()
                                .replace("%s", DateUtils.getCurrentShowYearMonthDayFromApiDateStr(jo.getDataList().getMsgDate(), PlayerInfoEditFragment.this.getBaseActivity())), new AppUtils.OnOkClick() {
                            @Override
                            public void onClick() {
                                if (isPhotoChange != null) {
                                    uploadPhoto(isPhotoChange, String.valueOf(memberId), true);
                                } else {
                                    if (fromPage.equals(TeeTimeMemberListWithIndexFragment.class.getName())) {
                                        getBaseActivity().doBackWithRefresh(TeeTimeMemberListWithIndexFragment.class);
                                    } else {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("fromFlag", "refresh");
                                        getBaseActivity().doBackWithReturnValue(bundle, PlayerFragment.class);
                                    }
                                }
                            }
                        });

                    } else {
                        Utils.showShortToast(getActivity(), jo.getReturnInfo());
                        if (isPhotoChange != null) {
                            uploadPhoto(isPhotoChange, String.valueOf(memberId), true);
                        } else {
                            if (fromPage.equals(TeeTimeMemberListWithIndexFragment.class.getName())) {
                                getBaseActivity().doBackWithRefresh(TeeTimeMemberListWithIndexFragment.class);
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putString("fromFlag", "refresh");
                                getBaseActivity().doBackWithReturnValue(bundle, PlayerFragment.class);
                            }
                        }

                    }


                } else {
                    Utils.showShortToast(getActivity(), jo.getReturnInfo());

                }

            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.startPut(getActivity(), ApiManager.HttpApi.GeneralInfo, params);
    }

    /**
     * add GeneralInfo data. (add)
     */

    private void netLinkAddGeneralInfo() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.PLAYER_MEMBER_NAME, tvNameValue.getText().toString());
        params.put(ApiKey.PLAYER_MEMBER_GENERALINFO_OCCUPATION, tvOccupationValue.getText().toString());
        String birth = DateUtils.getAPIYearMonthDayFromCurrentShow(tvBirthValue.getText().toString(), mContext);
        params.put(ApiKey.PLAYER_MEMBER_GENERALINFO_BIRTH, birth);
        params.put(ApiKey.PLAYER_MEMBER_GENERALINFO_GENDER, gender);
        params.put(ApiKey.PLAYER_MEMBER_GENERALINFO_ACCEPT_INFO_STATUS,
                String.valueOf(dataParameter.getDataList().getAcceptInfoStatus()));

        params.put(ApiKey.PLAYER_MEMBER_GENERALINFO_MEMBER_TYPE_ID, memberTypeId);

        params.put(ApiKey.PLAYER_MEMBER_GENERALINFO_HOBBIES, etHobbiesValue.getText().toString());
        params.put(ApiKey.PLAYER_MEMBER_GENERALINFO_NOTES, etNotesValue.getText().toString());
        ObjectMapper objectMapper = new ObjectMapper();
        String phoneListString = null;
        String emailListString = null;
        String addressListString = null;
        try {
            dataParameter.getDataList().getPhoneList().get(0).setStatus(1);
            if (dataParameter.getDataList().getEmailList().size() > 0) {
                dataParameter.getDataList().getEmailList().get(0).setStatus(1);
            }
            if (dataParameter.getDataList().getAddressList().size() > 0) {
                dataParameter.getDataList().getAddressList().get(0).setStatus(1);
            }
            phoneListString = objectMapper.writeValueAsString(dataParameter.getDataList().getPhoneList());
            emailListString = objectMapper.writeValueAsString(dataParameter.getDataList().getEmailList());
            addressListString = objectMapper.writeValueAsString(dataParameter.getDataList().getAddressList());
        } catch (Exception e) {
            Utils.log(e.getMessage());
        }

        params.put(ApiKey.PLAYER_MEMBER_GENERALINFO_PHONE_LIST, phoneListString);
        if (StringUtils.isNotEmpty(emailListString)) {
            params.put(ApiKey.PLAYER_MEMBER_GENERALINFO_EMAIL_LIST, emailListString);
        }
        params.put(ApiKey.PLAYER_MEMBER_GENERALINFO_ADDRESS_LIST, addressListString);


        HttpManager<JsonReturnMemberId> hh = new HttpManager<JsonReturnMemberId>(PlayerInfoEditFragment.this) {

            @Override
            public void onJsonSuccess(final JsonReturnMemberId jo) {
                int re = jo.getReturnCode();

                if (re == Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY) {
                    setOnBackListener(null);

                    if (Constants.STR_1.equals(jo.getMsgFlag())) {
                        AppUtils.showMessageWithOkButton(PlayerInfoEditFragment.this, jo.getMsgInfo()
                                .replace("%s", DateUtils.getCurrentShowYearMonthDayFromApiDateStr(jo.getMsgDate(), PlayerInfoEditFragment.this.getBaseActivity())), new AppUtils.OnOkClick() {
                            @Override
                            public void onClick() {
                                Utils.showShortToast(getActivity(), jo.getReturnInfo());
                                if (isPhotoChange != null) {
                                    uploadPhoto(isPhotoChange, jo.getDataList().getMemberId(), false);
                                } else {


                                    if (fromPage.equals(TeeTimeMemberListWithIndexFragment.class.getName())) {
                                        doBackWithRefresh(TeeTimeMemberListWithIndexFragment.class);
                                    } else {

                                        if (Utils.isStringNotNullOrEmpty(fromPage)){

                                            Bundle bundle = new Bundle();
                                            bundle.putString("addId",jo.getDataList().getMemberId());
                                            bundle.putString(TransKey.COMMON_FROM_PAGE,PlayerInfoEditFragment.class.getName());
                                            try {
                                                doBackWithReturnValue(bundle,(Class<? extends BaseFragment>) Class.forName(fromPage));
                                            } catch (ClassNotFoundException e) {
                                                e.printStackTrace();
                                            }


                                        }else{

                                            doBackWithRefresh();
                                        }

                                    }



                                }
                            }
                        });

                    } else {

                        Utils.showShortToast(getActivity(), jo.getReturnInfo());
                        if (isPhotoChange != null) {
                            uploadPhoto(isPhotoChange, jo.getDataList().getMemberId(), false);
                        } else {
                            if (fromPage.equals(TeeTimeMemberListWithIndexFragment.class.getName())) {
                                doBackWithRefresh(TeeTimeMemberListWithIndexFragment.class);
                            } else {
                                if (Utils.isStringNotNullOrEmpty(fromPage)){

                                    Bundle bundle = new Bundle();
                                    bundle.putString("addId",jo.getDataList().getMemberId());
                                    bundle.putString(TransKey.COMMON_FROM_PAGE,PlayerInfoEditFragment.class.getName());
                                    try {
                                        doBackWithReturnValue(bundle,(Class<? extends BaseFragment>) Class.forName(fromPage));
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }


                                }else{

                                    doBackWithRefresh();
                                }
                            }
                        }
                    }


                } else {

                    Utils.showShortToast(getActivity(), jo.getReturnInfo());

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
        hh.start(getActivity(), ApiManager.HttpApi.GeneralInfo, params);
    }

    /**
     * 单击事件监听器
     */
    public interface OnDeleteClickListener {
        void OnPhoneDeleteClick(String flag, JsonGeneralInfo.PhoneItem phoneItem);

        void OnEmailDeleteClick(String flag, JsonGeneralInfo.EmailItem emailItem);

        void OnAddressDeleteClick(String flag, JsonGeneralInfo.AddressItem addressItem);
    }
}
