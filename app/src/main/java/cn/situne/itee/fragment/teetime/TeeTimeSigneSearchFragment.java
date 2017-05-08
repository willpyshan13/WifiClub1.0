package cn.situne.itee.fragment.teetime;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.player.PlayerFragment;
import cn.situne.itee.fragment.player.PlayerInfoEditFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonCustomerBookingSearch;
import cn.situne.itee.manager.jsonentity.JsonSignData;
import cn.situne.itee.manager.jsonentity.JsonSigningGuest;
import cn.situne.itee.view.IteeSearchView;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.SwipeLinearLayout;

/**
 * Created by luochao on 11/23/15.
 */
public class TeeTimeSigneSearchFragment extends BaseFragment {

    public final static int SEARCH_TYPE_MEMBER_NON_MEMBER = 1;// member android  on member
    public final static int SEARCH_TYPE_MEMBER = 2;// member
    public final static int SEARCH_TYPE_NON_MEMBER = 3;// no  member
    public final static int SEARCH_TYPE_MEMBER_AGENT_EVENT = 4;// member  agent  event
    public final static int SEARCH_TYPE_AGENT = 5;// agent

    public final static int SEARCH_TYPE_EVENT = 6;// event
    private int  searchType;
    private ListView listView;
    private IteeSearchView titleSearchView;
    private ListViewBaseAdapter listViewBaseAdapter;


    private String userType;
    private String memberType;
    private String indexType;
    private String fromPage;


    private String nowSignType = Constants.STR_FLAG_YES;
    private String searchName = StringUtils.EMPTY;
    private int position, parentPosition;
    private String choiceId;
    private String courseAreaId;
    private String addMemberName;
    private String bookingDate;
    private int fromAdd;
    private String show;
    private String signId;
    private String count;
    private boolean isClickAdd;



    private IteeTextView footerMes;

    private String searchAgent;

    public ArrayList<JsonSignData.SigningData> dataList;
    @Override
    protected int getFragmentId() {
        return R.layout.fragment_tee_time_search_coustomer;
    }
    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        Bundle bundle = getArguments();
        dataList = new ArrayList<>();
        if (bundle != null) {

            fromPage= bundle.getString(TransKey.COMMON_FROM_PAGE, StringUtils.EMPTY);
            indexType= bundle.getString(TransKey.TEE_TIME_INDEX_TYPE, StringUtils.EMPTY);
            nowSignType = bundle.getString("nowSignType", Constants.STR_FLAG_YES);
            parentPosition = bundle.getInt("parentPosition");
            courseAreaId = bundle.getString(TransKey.COURSE_AREA_ID, StringUtils.EMPTY);
            bookingDate = bundle.getString(TransKey.BOOKING_DATE);
            show  = bundle.getString("show",Constants.STR_0);
            fromAdd = bundle.getInt("fromAdd", -1);
            signId = bundle.getString("signId");
            count = bundle.getString("count");
            userType= bundle.getString(TransKey.TEE_TIME_USER_TYPE, StringUtils.EMPTY);
            isClickAdd = bundle.getBoolean("isClickAdd", false);
            searchAgent =  bundle.getString("searchAgent", StringUtils.EMPTY);
        }

        listView = (ListView)rootView.findViewById(R.id.searchDataList);
        listView.setDividerHeight(0);
        listView.setDivider(null);
        addFooterView();
        listViewBaseAdapter = new ListViewBaseAdapter();

        listView.setAdapter(listViewBaseAdapter);




    }

    @Override
    protected void setDefaultValueOfControls() {

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
        setStackedActionBar();
        initTitleView();

        if (Utils.isStringNotNullOrEmpty(searchAgent)){

            netLink(searchAgent);
            titleSearchView.setText(searchAgent);
        }else{
            netLink("");

        }
    }
    private void initTitleView() {
        ActionBar actionBar = getBaseActivity().getSupportActionBar();
        LinearLayout.LayoutParams titleViewParams = new LinearLayout.LayoutParams(Utils.getWidth(getActivity()), getActionBarHeight());
        LinearLayout titleView = new LinearLayout(getBaseActivity());
        titleView.setBackgroundColor(getColor(R.color.common_light_gray));
        titleView.setLayoutParams(titleViewParams);
        titleView.setPadding(getActualWidthOnThisDevice(0), 0, 0, 0);

        LinearLayout.LayoutParams llp
                = new LinearLayout.LayoutParams(Utils.getWidth(getActivity()) - getActualWidthOnThisDevice(160), getActionBarHeight() - 20);
        titleView.setGravity(Gravity.CENTER_VERTICAL);
        titleView.setPadding(0, 0, 0, 0);
        titleSearchView = new IteeSearchView(getBaseActivity());
        titleSearchView.setHint(getString(R.string.common_search));
        titleSearchView.setTextSize(Constants.FONT_SIZE_NORMAL);
        titleSearchView.setBackground(getResources().getDrawable(R.drawable.bg_search_view));
        titleSearchView.setLayoutParams(llp);
        titleSearchView.setIcon(getActionBarHeight() - 20, R.drawable.icon_search_del, R.drawable.icon_search_del, R.drawable.icon_search, R.drawable.icon_search);
        titleSearchView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_UP == event.getAction()) {
                    if (keyCode == 66) {
                        Utils.hideKeyboard(getBaseActivity());
                        netLink(titleSearchView.getText().toString());
                        //api
                        //getAgentsSearch(titleSearchView.getText().toString());
                    }
                }

                return false;
            }
        });

        titleSearchView.setRightIconListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleSearchView.setText(StringUtils.EMPTY);
            }
        });
        titleSearchView.setLeftIconListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getBaseActivity());
                netLink(titleSearchView.getText().toString());
                //  getAgentsSearch(titleSearchView.getText().toString());
            }
        });

        titleSearchView.requestFocus();
        Utils.showKeyboard(titleSearchView, getActivity());


        LinearLayout.LayoutParams titleSearchViewLayoutParams
                = (LinearLayout.LayoutParams) titleSearchView.getLayoutParams();
        titleSearchViewLayoutParams.leftMargin = getActualWidthOnThisDevice(10);


        titleSearchView.setLayoutParams(titleSearchViewLayoutParams);


        titleView.addView(titleSearchView);


        Button cancelBtn = new Button(getBaseActivity());
        cancelBtn.setText(getString(R.string.common_cancel));
        cancelBtn.setBackgroundColor(Color.TRANSPARENT);
        cancelBtn.setTextColor(getColor(R.color.common_blue));
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doBack();
            }
        });
        cancelBtn.setTextSize(Constants.FONT_SIZE_NORMAL);
        titleView.addView(cancelBtn);

        actionBar.setCustomView(titleView);
    }

    class ListViewBaseAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return dataList.size();
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
            ListItem item = null;
            if (view == null){
                item = new ListItem(getBaseActivity());

            }else{

                item = (ListItem)view;
            }
            item.reFreshLayout(dataList.get(i));
            return item;
        }
    }

    class ListItem extends LinearLayout{

        private IteeTextView titleLayout;

        private JsonCustomerBookingSearch.Member mMember;
        private RelativeLayout leftLayout;
        private NetworkImageView playerImg;

        private RelativeLayout rightLayout;

        private IteeTextView tvPlayer;
        private IteeTextView tvBirth;
        private IteeTextView tvPhone;
        private IteeTextView tvNo;
        private IteeTextView tvCard;

        private  SwipeLinearLayout box;
        private JsonSignData.SigningData mData;

        public ListItem(Context context) {
            super(context);
            this.setOrientation(VERTICAL);
            LayoutParams titleLayoutParams  = new   LayoutParams( LayoutParams .MATCH_PARENT,getActualHeightOnThisDevice(50));
            titleLayout = new IteeTextView(context);
            titleLayout.setLayoutParams(titleLayoutParams);
            titleLayout.setBackgroundColor(getColor(R.color.common_light_gray));
            titleLayout.setTextColor(getColor(R.color.common_gray));
            titleLayout.setPadding(getActualWidthOnThisDevice(10), 0, 0, 0);
            LayoutParams leftLayoutParams  = new   LayoutParams( getActualHeightOnThisDevice(200),getActualHeightOnThisDevice(200));
            LayoutParams rightLayoutParams  = new   LayoutParams( LayoutParams .MATCH_PARENT,getActualHeightOnThisDevice(200));
            leftLayout = new RelativeLayout(context);
            leftLayout.setLayoutParams(leftLayoutParams);
            RelativeLayout.LayoutParams playerImgParams  = new   RelativeLayout.LayoutParams( getActualHeightOnThisDevice(180),getActualHeightOnThisDevice(180));
            playerImgParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            playerImg = new NetworkImageView(context);
            playerImg.setLayoutParams(playerImgParams);

            leftLayout.addView(playerImg);
            rightLayout = new RelativeLayout(context);
            rightLayout.setLayoutParams(rightLayoutParams);
            tvPlayer = new IteeTextView(context);
            tvBirth = new IteeTextView(context);
            tvPhone = new IteeTextView(context);
            tvNo = new IteeTextView(context);
            tvCard = new IteeTextView(context);
            tvPlayer.setId(View.generateViewId());
            tvBirth.setTextSize(Constants.FONT_SIZE_SMALLER);
            tvPhone.setTextSize(Constants.FONT_SIZE_SMALLER);
            tvCard.setTextSize(Constants.FONT_SIZE_SMALLER);

            tvBirth.setId(View.generateViewId());
            tvPhone.setId(View.generateViewId());
            tvNo.setId(View.generateViewId());
            tvCard.setId(View.generateViewId());
            tvNo.setGravity(Gravity.RIGHT);
            tvCard.setGravity(Gravity.RIGHT);
            rightLayout.addView(tvPlayer);
            rightLayout.addView(tvBirth);
            rightLayout.addView(tvPhone);
            rightLayout.addView(tvNo);
            rightLayout.addView(tvCard);


            RelativeLayout.LayoutParams tvPlayerParams  = new   RelativeLayout.LayoutParams( getActualWidthOnThisDevice(300),getActualHeightOnThisDevice(70));
            tvPlayer.setLayoutParams(tvPlayerParams);
            RelativeLayout.LayoutParams tvBirthParams  = new   RelativeLayout.LayoutParams( getActualWidthOnThisDevice(300),getActualHeightOnThisDevice(65));
            tvBirthParams.addRule(RelativeLayout.BELOW,tvPlayer.getId());
            RelativeLayout.LayoutParams tvPhoneParams  = new   RelativeLayout.LayoutParams( getActualWidthOnThisDevice(300),getActualHeightOnThisDevice(65));
            tvPhoneParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            RelativeLayout.LayoutParams tvNoParams  = new   RelativeLayout.LayoutParams( getActualWidthOnThisDevice(200),getActualHeightOnThisDevice(70));
            tvNoParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            RelativeLayout.LayoutParams tvCardParams  = new   RelativeLayout.LayoutParams( getActualWidthOnThisDevice(200),getActualHeightOnThisDevice(65));

            tvCardParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            tvCardParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            tvBirth.setLayoutParams(tvBirthParams);
            tvPhone.setLayoutParams(tvPhoneParams);
            tvNo.setLayoutParams(tvNoParams);
            tvCard.setLayoutParams(tvCardParams);

            LinearLayout.LayoutParams boxParams  = new   LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            box = new SwipeLinearLayout(context,getActualWidthOnThisDevice(180));
            box.setLayoutParams(boxParams);
            box.setOrientation(HORIZONTAL);


            LinearLayout.LayoutParams bodyLayoutParams  = new   LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            LinearLayout  bodyLayout = new LinearLayout(context);
            bodyLayout.setLayoutParams(bodyLayoutParams);
            box.addView(bodyLayout);



            LinearLayout.LayoutParams profileBtnParams = new  LinearLayout.LayoutParams(getActualWidthOnThisDevice(180),LayoutParams.MATCH_PARENT);
            Button profileBtn = new Button(context);
            profileBtn.setText(getString(R.string.tee_time_profile));
            profileBtn.setTextColor(getColor(R.color.common_white));
            profileBtn.setLayoutParams(profileBtnParams);

            profileBtn.setBackgroundColor(getColor(R.color.common_green));
            box.addView(profileBtn);

            profileBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.COMMON_MEMBER_ID, Integer.parseInt(mData.getMemberId()));
                    push(PlayerFragment.class, bundle);
                }
            });


            this.addView(titleLayout);
            this.addView(AppUtils.getSeparatorLine(context));
            this.addView(box);
            this.addView(AppUtils.getSeparatorLine(context));
            bodyLayout.addView(leftLayout);
            bodyLayout.addView(rightLayout);
            leftLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectMember();
                }
            });

            rightLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectMember();
                }
            });




        }


        public void reFreshLayout(JsonSignData.SigningData data){
            titleLayout.setText("");
            titleLayout.setVisibility(View.GONE);

            mData = data;
            box.setmRightViewWidth(0);
            if (Utils.isStringNotNullOrEmpty(data.getShowTitle())){
                titleLayout.setVisibility(View.VISIBLE);
                titleLayout.setText(data.getShowTitle());
            }

            leftLayout.setVisibility(View.GONE);

            tvPlayer.setText(data.getMemberName());
            tvBirth.setText(data.getFreqBirth());
            tvPhone.setText(data.getPhone());
            tvNo.setText(data.getMemberNo());
            tvCard.setText(data.getFciZip());
            tvBirth.setPadding(0, 0, 0, 0);
            tvPhone.setPadding(0, 0, 0, 0);
            tvPlayer.setPadding(0, 0, 0, 0);
            tvCard.setPadding(0, 0, 0, 0);
            tvNo.setPadding(0, 0, 0, 0);
            if (Constants.STR_1.equals(data.getType())){
                leftLayout.setVisibility(View.VISIBLE);
                if (Utils.isStringNotNullOrEmpty(data.getPhoto())) {
                    AppUtils.showNetworkImage(playerImg, data.getPhoto());
                } else {
                    AppUtils.showNetworkImage(playerImg, Constants.PHOTO_DEFAULT_URL);
                }
                box.setmRightViewWidth(getActualWidthOnThisDevice(180));
            }
            if (Constants.STR_2.equals(data.getType())){

                tvCard.setText(Constants.STR_EMPTY);
                tvNo.setText(data.getAgentCode());
                tvNo.setPadding(0, 0, getActualWidthOnThisDevice(20), 0);
                tvBirth.setPadding(getActualWidthOnThisDevice(20), 0, 0, 0);
                tvPhone.setPadding(getActualWidthOnThisDevice(20),0,0,0);
                tvPlayer.setPadding(getActualWidthOnThisDevice(20),0,0,0);
                tvBirth.setText(data.getMemberContact());
            }

            if ("3".equals(data.getType())){
                tvBirth.setPadding(getActualWidthOnThisDevice(20), 0, 0, 0);
                tvPhone.setPadding(getActualWidthOnThisDevice(20),0,0,0);
                tvPlayer.setPadding(getActualWidthOnThisDevice(20),0,0,0);

                tvBirth.setText(data.getStartDate()+Constants.STR_SPACE+data.getStartTime());
                tvPhone.setText(data.getEndDate()+Constants.STR_SPACE+data.getEndTime());
            }

        }

        private void selectMember(){

            Utils.hideKeyboard(getBaseActivity());

            Bundle bundle = new Bundle();
            JsonSigningGuest.Member tempMember = new JsonSigningGuest.Member();
            if (Constants.STR_1.equals(mData.getType())){
                tempMember.setSignNumber(mData.getSignNumber());
                tempMember.setPhone(mData.getPhone());
                tempMember.setNameSort(mData.getNameSort());
                tempMember.setMemberId(Integer.parseInt(mData.getMemberId()));
                tempMember.setMemberNo(mData.getMemberNo());
                tempMember.setMemberName(mData.getMemberName());
            }


            if (Constants.STR_2.equals(mData.getType())){
                bundle.putString("agentId", mData.getAgentCode());
            }


            if (fromAdd != -1) {

                //can not overstep Member's signNumber
                int signNumber = 0;
                if (Utils.isStringNotNullOrEmpty(tempMember.getSignNumber())) {
                    signNumber = Integer.valueOf(tempMember.getSignNumber());
                }
                int nowCount = 0;
                Map<String, Integer> map = null;
                if (Utils.isStringNotNullOrEmpty(count )){
                    map = (Map<String, Integer>) Utils.getObjectFromString(count);
                }


                if (map != null && tempMember.getMemberId() != null && map.get(String.valueOf(tempMember.getMemberId())) != null) {
                    nowCount = map.get(String.valueOf(tempMember.getMemberId()));
                }




                if (Constants.STR_FLAG_YES.equals(mData.getType()) && nowCount >= signNumber) {
                    AppUtils.showMessageWithOkButton(TeeTimeSigneSearchFragment.this, getString(R.string.error_mes00005));
                } else {

                    //客户绑定人员
                    bundle.putString("fromFlag", "itemMemberDetail");
                    bundle.putString("signType", mData.getType());
                    bundle.putInt("position", fromAdd);
                    bundle.putInt("parentPosition", parentPosition);
                    bundle.putSerializable("member", tempMember);

                    try {
                        bundle.putBoolean("isBack",true);
                        doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else {

                if (isClickAdd){
                    JsonCustomerBookingSearch.Member jMember = new  JsonCustomerBookingSearch.Member();
                    jMember.setShowTitle(mData.getShowTitle());
                    jMember.setMemberBirth(mData.getMemberContact());
                    jMember.setMemberTel(mData.getPhone());
                    jMember.setMemberNo(mData.getAgentCode());
                    jMember.setMemberName(mData.getMemberName());

                    jMember.setIndex(mData.getKeyIndex());
                    jMember.setjAgent(true);
                    bundle.putSerializable("member", jMember);
                }else{
                    bundle.putSerializable("member", tempMember);
                }
                //返回客户名字
                bundle.putString("fromFlag", "memberName");

                bundle.putString("signType", mData.getType());
                try {
                    bundle.putBoolean("isBack",true);
                    doBackWithReturnValue(bundle,(Class<? extends BaseFragment>) Class.forName(fromPage));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void netLink(String name) {
        if (Utils.isStringNotNullOrEmpty(name)){
            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
            params.put(ApiKey.TEE_TIME_SIGN_TYPE, nowSignType);
            params.put(ApiKey.COMMON_COURES_ID, AppUtils.getCurrentCourseId(getActivity()));
            params.put(ApiKey.COURSE_AREA_ID, courseAreaId);
            params.put("key_word", name);
            params.put("show_flag", show);
            params.put(ApiKey.TEE_TIME_USER_TYPE, userType);
            params.put(ApiKey.TEE_TIME_INDEX_TYPE, indexType);
            if (fromAdd != -1) {
                params.put(ApiKey.TEE_TIME_SIGN_FLAG, Constants.STR_1);
            } else {
                params.put(ApiKey.TEE_TIME_SIGN_FLAG, Constants.STR_0);
            }
            if (Utils.isStringNotNullOrEmpty(bookingDate)) {
                params.put(ApiKey.TEE_TIME_SIGN_BOOKING_DATE, bookingDate);
            }
            HttpManager<JsonSignData> hh = new HttpManager<JsonSignData>(TeeTimeSigneSearchFragment.this) {
                @Override
                public void onJsonSuccess(JsonSignData jo) {
                    int tokenStatus = jo.getReturnCode();
                    //  if (tvOverlay !=null)tvOverlay.setText("");

                    if (tokenStatus == Constants.RETURN_CODE_20301) {
                        dataList = jo.getDataList();
                        listViewBaseAdapter.notifyDataSetChanged();



                        if (dataList.size() <= 0) {
                            footerMes.setText(StringUtils.EMPTY);
                            SpannableString ss = new SpannableString(titleSearchView.getText());
                            ss.setSpan(new ForegroundColorSpan(getColor(R.color.common_blue)), 0, ss.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            ss.setSpan(new RelativeSizeSpan(1.3f), 0, ss.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                            footerMes.setVisibility(View.VISIBLE);

                            footerMes.append(getString(R.string.add_tee_time_setting_search_mes1));
                            footerMes.append(ss);
                            footerMes.append(getString(R.string.add_tee_time_setting_search_mes2));
                            addBtn.setVisibility(View.VISIBLE);
                        } else {
                            footerMes.setVisibility(View.GONE);
                        }




                    } else if (tokenStatus == Constants.RETURN_CODE_20302) {

                    }
                }

                @Override
                public void onJsonError(VolleyError error) {


                }
            };
            hh.start(getActivity(), ApiManager.HttpApi.SigningGuest, params);

        }

    }
    private Button addBtn;
    private void addFooterView() {
        LinearLayout llFooterView = new LinearLayout(getBaseActivity());
        llFooterView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams btnParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams footerMesParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        footerMes = new IteeTextView(getBaseActivity());
        footerMes.setLayoutParams(footerMesParams);
        footerMes.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        footerMes.setVisibility(View.GONE);
        footerMes.setSingleLine(false);

        addBtn = new Button(getBaseActivity());
        addBtn.setTextColor(Color.WHITE);
        addBtn.setLayoutParams(btnParams);
        addBtn.setText(R.string.newteetimes_btn_add_a_profile);
        addBtn.setBackground(getResources().getDrawable(R.drawable.bg_green_btn));
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getBaseActivity());
                boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEdit, getActivity())
                        && AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEditNonmember, getActivity());
                if (hasPermission) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeAdd.value());
                    bundle.putString(TransKey.TEE_TIME_MEMBER_NAME, titleSearchView.getText().toString());
                    push(PlayerInfoEditFragment.class, bundle);
                } else {
                    AppUtils.showHaveNoPermission(TeeTimeSigneSearchFragment.this);
                }
            }
        });
        addBtn.setVisibility(View.GONE);
        llFooterView.addView(footerMes);

        LinearLayout.LayoutParams vParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(20));
        View v = new View(getBaseActivity());
        v.setLayoutParams(vParams);
        llFooterView.setPadding(40, 30, 40, 0);
        llFooterView.addView(v);
        llFooterView.addView(addBtn);
        llFooterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        listView.addFooterView(llFooterView);

    }
}
