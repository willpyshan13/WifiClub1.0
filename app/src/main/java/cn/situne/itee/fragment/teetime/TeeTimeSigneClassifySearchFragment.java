package cn.situne.itee.fragment.teetime;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.adapter.MemberWithIndexAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.IndexCursor;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.agents.AgentsAddEditFragment;
import cn.situne.itee.fragment.events.EventsEditFragment;
import cn.situne.itee.fragment.player.PlayerFragment;
import cn.situne.itee.fragment.player.PlayerInfoEditFragment;
import cn.situne.itee.fragment.quick.SearchBookingFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonCustomerBookingSearch;
import cn.situne.itee.manager.jsonentity.JsonSignData;
import cn.situne.itee.manager.jsonentity.JsonSigningGuest;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.PricingTableProductItem;
import cn.situne.itee.view.StickyLayout;
import cn.situne.itee.view.SwipeLinearLayout;

/**
 * Created by luochao on 11/23/15.
 */
public class TeeTimeSigneClassifySearchFragment  extends BaseFragment {



    private PullToRefreshListView listView;
    private ListViewBaseAdapter listViewBaseAdapter;
    private RelativeLayout rlNotMember;

    private IteeTextView etSearch;
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
    public ArrayList<JsonSignData.SigningData> dataList;
    private String notCancel;
    private String userType;

    private String userTypeName;
    private int first;

    private String selectAgentId;


    private int currentPage;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_classify_search;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        rlNotMember = (RelativeLayout) rootView.findViewById(R.id.rl_not_member);
        Bundle bundle = getArguments();
        if (bundle != null) {
            fromPage= bundle.getString(TransKey.COMMON_FROM_PAGE, StringUtils.EMPTY);
            userType = bundle.getString(TransKey.TEE_TIME_USER_TYPE, StringUtils.EMPTY);

            userTypeName= bundle.getString(TransKey.TEE_TIME_USER_TYPE_NAME, StringUtils.EMPTY);
            indexType= bundle.getString(TransKey.TEE_TIME_INDEX_TYPE, StringUtils.EMPTY);
            nowSignType = bundle.getString("nowSignType", Constants.STR_FLAG_YES);
            parentPosition = bundle.getInt("parentPosition");
            courseAreaId = bundle.getString(TransKey.COURSE_AREA_ID, StringUtils.EMPTY);

            bookingDate = bundle.getString(TransKey.BOOKING_DATE);
            show  = bundle.getString("show", Constants.STR_0);
            fromAdd = bundle.getInt("fromAdd", -1);
            count = bundle.getString("count");
            signId = bundle.getString("signId",Constants.STR_EMPTY);

            selectAgentId= bundle.getString("selectAgentId",Constants.STR_EMPTY);
            notCancel = bundle.getString("notCancel",Constants.STR_0);
            first = 2;

            if (Utils.isStringNotNullOrEmpty(selectAgentId))
                signId = selectAgentId;
        }
        dataList = new ArrayList<>();



        listView= (PullToRefreshListView) rootView.findViewById(R.id.listView);
        listView.getRefreshableView().setDividerHeight(0);
        listView.getRefreshableView().setDivider(null);





        ListView.LayoutParams headerParams = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(100));
        RelativeLayout header = new RelativeLayout(getBaseActivity());
        header.setLayoutParams(headerParams);


        RelativeLayout.LayoutParams etSearchParams = new  RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);

        header.setBackgroundColor(getColor(R.color.common_gray));
        etSearch = new IteeTextView(getBaseActivity());
        etSearchParams.leftMargin = getActualWidthOnThisDevice(10);
        etSearchParams.topMargin =getActualWidthOnThisDevice(10);
        etSearchParams.bottomMargin =getActualWidthOnThisDevice(10);
        etSearchParams.rightMargin  =getActualWidthOnThisDevice(10);
        etSearchParams.rightMargin  =getActualWidthOnThisDevice(10);

        etSearch.setLayoutParams(etSearchParams);
        etSearch.setBackgroundResource(R.drawable.bg_search_view);
        RelativeLayout.LayoutParams tvSearchParams = new  RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        tvSearchParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        IteeTextView tvSearch = new IteeTextView(getBaseActivity());
        tvSearch.setGravity(Gravity.CENTER);
        tvSearch.setLayoutParams(tvSearchParams);
        tvSearch.setId(View.generateViewId());
        tvSearch.setText(getString(R.string.common_search));
        tvSearch.setTextColor(getColor(R.color.common_gray));
        header.addView(etSearch);
        header.addView(tvSearch);

        RelativeLayout.LayoutParams iconParams = new  RelativeLayout.LayoutParams(getActualHeightOnThisDevice(60),getActualHeightOnThisDevice(60));
        iconParams.addRule(RelativeLayout.LEFT_OF,tvSearch.getId());
        iconParams.addRule(RelativeLayout.CENTER_VERTICAL);
        ImageView icon = new ImageView(getBaseActivity());
        icon.setBackgroundResource(R.drawable.icon_search);
        icon.setLayoutParams(iconParams);
        header.addView(icon);


        listView.getRefreshableView().addHeaderView(header);








        listViewBaseAdapter = new ListViewBaseAdapter();
        listView.setAdapter(listViewBaseAdapter);



        listView.setMode(PullToRefreshBase.Mode.BOTH);
        ILoadingLayout headerLayoutProxy = listView.getLoadingLayoutProxy(true, false);
        ILoadingLayout footerLayoutProxy = listView.getLoadingLayoutProxy(false, true);

        headerLayoutProxy.setPullLabel(getString(R.string.app_list_header_refresh_down));
        headerLayoutProxy.setRefreshingLabel(getString(R.string.app_list_header_refresh_down));
        headerLayoutProxy.setReleaseLabel(getString(R.string.app_list_header_refresh_down));

        footerLayoutProxy.setPullLabel(getString(R.string.app_list_header_refresh_loading_more));
        footerLayoutProxy.setRefreshingLabel(getString(R.string.app_list_header_refresh_loading_more));
        footerLayoutProxy.setReleaseLabel(getString(R.string.app_list_header_refresh_loading_more));




        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                netLink(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                netLink(false);
            }
        });


    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        netLink(true);
    }

    @Override
    protected void reShowWithBackValue() {
        super.reShowWithBackValue();
        Bundle bundle = getReturnValues();
        if (bundle != null) {
            try {
                boolean isBack = bundle.getBoolean("isBack",false);

                String addId  = bundle.getString("addId", Constants.STR_EMPTY);
               // String fromPage  = bundle.getString(TransKey.COMMON_FROM_PAGE, Constants.STR_EMPTY);

                if (isBack)
                doBackWithReturnValue(bundle,(Class<? extends BaseFragment>) Class.forName(fromPage));



                if (Utils.isStringNotNullOrEmpty(addId)){
                    signId = addId;
                    netLink(true);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    protected void setListenersOfControls() {

        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("fromAdd", fromAdd);
                bundle.putString("signId", signId);
                bundle.putInt("parentPosition", parentPosition);
                bundle.putString(TransKey.TEE_TIME_USER_TYPE, userType);
                bundle.putString(TransKey.TEE_TIME_INDEX_TYPE, indexType);
                bundle.putString("count", count);
                bundle.putString("nowSignType", nowSignType);
                bundle.putString(TransKey.BOOKING_DATE, bookingDate);
                bundle.putString(TransKey.COURSE_AREA_ID, String.valueOf(courseAreaId));
                bundle.putString(TransKey.COMMON_FROM_PAGE,TeeTimeSigneClassifySearchFragment.class.getName());
                bundle.putString("addAgent", "1");
                bundle.putString("fragmentSource", SearchBookingFragment.FRAGMENT_SOURCE_2);
                bundle.putString("fragmentType", SearchBookingFragment.FRAGMENT_TYPE_2);
                push(SearchBookingFragment.class, bundle);
            }
        });
    }

    @Override
    protected void setLayoutOfControls() {
        RelativeLayout.LayoutParams paramsYards = (RelativeLayout.LayoutParams) rlNotMember.getLayoutParams();
        rlNotMember.setLayoutParams(paramsYards);

        RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        iconParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        ImageView icon = new ImageView(getBaseActivity());
        icon.setBackgroundResource(R.drawable.icon_not_data);
        icon.setLayoutParams(iconParams);
        rlNotMember.addView(icon);
    }

    @Override
    protected void setPropertyOfControls() {

    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        if (Utils.isStringNotNullOrEmpty(userTypeName)){
            getTvLeftTitle().setText(userTypeName);
        }else {
            switch (nowSignType) {
                case "1":
                    getTvLeftTitle().setText(getString(R.string.customers_member));

                    break;
                case "2":
                    getTvLeftTitle().setText(getString(R.string.agents_agent));
                    break;
                case "3":
                    getTvLeftTitle().setText(getString(R.string.event_event));
                    break;

            }
        }

        RelativeLayout parent = (RelativeLayout) getTvRight().getParent();

        ImageView ivShop = new ImageView(getActivity());
        ivShop.setImageResource(R.drawable.btn_unsign);
        RelativeLayout.LayoutParams ivPackageLayoutParams
                = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(80), getActualWidthOnThisDevice(80));
        ivPackageLayoutParams.addRule(RelativeLayout.LEFT_OF, getTvRight().getId());
        ivPackageLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivPackageLayoutParams.rightMargin = getActualWidthOnThisDevice(30);
        ivShop.setLayoutParams(ivPackageLayoutParams);
        ivShop.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                Bundle bundle = new Bundle();
                if (fromAdd != -1) {
                    //客户绑定人员
                    bundle.putString("fromFlag", "itemMemberDetail");
                    bundle.putString("unSignFlag", "1");
                    //客户绑定人员
                    bundle.putString("signType", StringUtils.EMPTY);
                    bundle.putInt("position", fromAdd);
                    bundle.putInt("parentPosition", parentPosition);
                    try {
                        bundle.putBoolean("isBack",true);
                        doBackWithReturnValue(bundle,(Class<? extends BaseFragment>) Class.forName(fromPage));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        parent.addView(ivShop);
        if (Constants.STR_1.equals(notCancel))ivShop.setVisibility(View.GONE);
        parent.invalidate();
        getTvRight().setBackground(null);
        getTvRight().setBackgroundResource(R.drawable.icon_common_add);
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.STR_1.equals(nowSignType)){


                    boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEdit, getActivity())
                            && AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEditNonmember, getActivity());
                    if (hasPermission) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeAdd.value());

                        bundle.putString(TransKey.COURSE_ADD_MEMBER_NAME, addMemberName);
                        bundle.putString(TransKey.COMMON_FROM_PAGE,TeeTimeSigneClassifySearchFragment.class.getName());
                        push(PlayerInfoEditFragment.class, bundle);
                    } else {
                        AppUtils.showHaveNoPermission(TeeTimeSigneClassifySearchFragment.this);
                    }
                }

                if (Constants.STR_2.equals(nowSignType)){

                    boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlAgentsEdit, getActivity());
                    if (hasPermission) {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(TransKey.EVENT_IS_ADD, true);
                        bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeSigneClassifySearchFragment.class.getName());
                        push(AgentsAddEditFragment.class, bundle);
                    } else {
                        AppUtils.showHaveNoPermission(TeeTimeSigneClassifySearchFragment.this);
                    }

                }

                if ("3".equals(nowSignType)){
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(TransKey.EVENT_IS_ADD, true);
                    bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeSigneClassifySearchFragment.class.getName());
                    push(EventsEditFragment.class, bundle);
                }



            }
        });

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


            item.getBox().setSwipeLayoutListener(new SwipeLinearLayout.SwipeLayoutListener() {
                @Override
                public void scrollView(View item) {

                        if (selectSwipeLinearLayout != null && !selectSwipeLinearLayout.equals(item)) {
                            selectSwipeLinearLayout.hideRight();
                        }

                }
            });
            item.getBox().setAfterShowRightListener(new SwipeLinearLayout.AfterShowRightListener() {
                @Override
                public void doAfterShowRight(SwipeLinearLayout swipeLinearLayout) {
                    selectSwipeLinearLayout = swipeLinearLayout;
                }
            });

            return item;
        }
    }
    private SwipeLinearLayout selectSwipeLinearLayout;
    class ListItem extends LinearLayout {
        private IteeTextView titleLayout;
        private RelativeLayout leftLayout;
        private NetworkImageView playerImg;
        private RelativeLayout rightLayout;
        private IteeTextView tvPlayer;
        private IteeTextView tvBirth;
        private IteeTextView tvPhone;
        private IteeTextView tvNo;
        private IteeTextView tvCard;


        private JsonSignData.SigningData mData;
        private  SwipeLinearLayout box;

        public SwipeLinearLayout getBox() {
            return box;
        }

        public ListItem(Context context) {
            super(context);
            this.setOrientation(VERTICAL);
            LayoutParams titleLayoutParams  = new   LayoutParams( LayoutParams .MATCH_PARENT,getActualHeightOnThisDevice(50));
            titleLayout = new IteeTextView(context);
            titleLayout.setLayoutParams(titleLayoutParams);
            titleLayout.setBackgroundColor(getColor(R.color.common_light_gray));
            titleLayout.setTextColor(getColor(R.color.common_gray));
            titleLayout.setPadding(getActualWidthOnThisDevice(10), 0, 0, 0);
            LayoutParams leftLayoutParams  = new   LayoutParams( getActualHeightOnThisDevice(170),getActualHeightOnThisDevice(170));
            LayoutParams rightLayoutParams  = new   LayoutParams( LayoutParams .MATCH_PARENT,getActualHeightOnThisDevice(170));
            leftLayout = new RelativeLayout(context);
            leftLayout.setLayoutParams(leftLayoutParams);
            RelativeLayout.LayoutParams playerImgParams  = new   RelativeLayout.LayoutParams( getActualHeightOnThisDevice(150),getActualHeightOnThisDevice(150));
            playerImgParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            playerImg = new NetworkImageView(context);
            playerImg.setLayoutParams(playerImgParams);
            tvPlayer = new IteeTextView(context);
            tvBirth = new IteeTextView(context);
            tvPhone = new IteeTextView(context);
            tvNo = new IteeTextView(context);
            leftLayout.addView(playerImg);
            rightLayout = new RelativeLayout(context);
            tvCard = new IteeTextView(context);
            tvPlayer.setId(View.generateViewId());
            tvBirth.setTextSize(Constants.FONT_SIZE_SMALLER);
            tvPhone.setTextSize(Constants.FONT_SIZE_SMALLER);
            tvCard.setTextSize(Constants.FONT_SIZE_SMALLER);
            tvBirth.setId(View.generateViewId());
            tvPhone.setId(View.generateViewId());
            tvNo.setId(View.generateViewId());
            tvCard.setId(View.generateViewId());
            tvNo.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            tvCard.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            rightLayout.addView(tvPlayer);
            rightLayout.addView(tvBirth);
            rightLayout.addView(tvPhone);
            rightLayout.addView(tvNo);
            rightLayout.addView(tvCard); rightLayout.setLayoutParams(rightLayoutParams);




            RelativeLayout.LayoutParams tvPlayerParams  = new   RelativeLayout.LayoutParams( getActualWidthOnThisDevice(300),getActualHeightOnThisDevice(50));
            tvPlayer.setLayoutParams(tvPlayerParams);
            RelativeLayout.LayoutParams tvBirthParams  = new   RelativeLayout.LayoutParams( getActualWidthOnThisDevice(300),getActualHeightOnThisDevice(45));
            tvBirthParams.addRule(RelativeLayout.BELOW,tvPlayer.getId());
            RelativeLayout.LayoutParams tvPhoneParams  = new   RelativeLayout.LayoutParams( getActualWidthOnThisDevice(300), getActualHeightOnThisDevice(45));

            tvPhoneParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            RelativeLayout.LayoutParams tvNoParams  = new   RelativeLayout.LayoutParams( getActualWidthOnThisDevice(200),getActualHeightOnThisDevice(50));
            tvNoParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);


            RelativeLayout.LayoutParams tvCardParams  = new   RelativeLayout.LayoutParams( getActualWidthOnThisDevice(200),getActualHeightOnThisDevice(45));
            tvCardParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            tvCardParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);



            tvCardParams.bottomMargin = getActualHeightOnThisDevice(10);
            tvNoParams.topMargin = getActualHeightOnThisDevice(20);

            tvPlayerParams.topMargin = getActualHeightOnThisDevice(20);
            tvPhoneParams.bottomMargin = getActualHeightOnThisDevice(10);

            tvBirth.setLayoutParams(tvBirthParams);
            tvPhone.setLayoutParams(tvPhoneParams);
            tvNo.setLayoutParams(tvNoParams);
            tvCard.setLayoutParams(tvCardParams);
            tvCard.setPadding(0, 0, getActualWidthOnThisDevice(20), 0);


            tvNo.setPadding(0,0,getActualWidthOnThisDevice(20),0);
            LinearLayout.LayoutParams boxParams  = new   LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            box = new SwipeLinearLayout(context,getActualWidthOnThisDevice(150));
            box.setLayoutParams(boxParams);
            box.setOrientation(HORIZONTAL);


            LinearLayout.LayoutParams bodyLayoutParams  = new   LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            LinearLayout  bodyLayout = new LinearLayout(context);
            bodyLayout.setLayoutParams(bodyLayoutParams);
            box.addView(bodyLayout);



            LinearLayout.LayoutParams profileBtnParams = new  LinearLayout.LayoutParams(getActualWidthOnThisDevice(150),LayoutParams.MATCH_PARENT);
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
                    bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeSigneClassifySearchFragment.class.getName());
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

        private void selectMember(){
            Utils.hideKeyboard(getBaseActivity());
            Bundle bundle = new Bundle();
            JsonSigningGuest.Member tempMember = new JsonSigningGuest.Member();
            tempMember.setMemberId(Integer.parseInt(mData.getMemberId()));
            tempMember.setMemberNo(mData.getMemberNo());
            if (Constants.STR_1.equals(mData.getType())){
                tempMember.setSignNumber(mData.getSignNumber());
                tempMember.setPhone(mData.getPhone());
                tempMember.setNameSort(mData.getNameSort());
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
                    AppUtils.showMessageWithOkButton(TeeTimeSigneClassifySearchFragment.this, getString(R.string.error_mes00005));
                } else {

                    //客户绑定人员
                    bundle.putString("fromFlag", "itemMemberDetail");
                    bundle.putString("signType", mData.getType());
                    bundle.putInt("position", fromAdd);
                    bundle.putInt("parentPosition", parentPosition);
                    bundle.putSerializable("member", tempMember);

                    try {
                        bundle.putBoolean("isBack",true);
                        doBackWithReturnValue(bundle,(Class<? extends BaseFragment>) Class.forName(fromPage));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //返回客户名字
                bundle.putString("fromFlag", "memberName");
                bundle.putSerializable("member", tempMember);
                bundle.putString("signType", mData.getType());


                try {
                    bundle.putBoolean("isBack",true);
                    doBackWithReturnValue(bundle,(Class<? extends BaseFragment>) Class.forName(fromPage));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

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
            tvBirth.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(data.getFreqBirth(), getActivity()));
            tvPhone.setText(data.getPhone());
            tvNo.setText(data.getMemberNo());
            tvCard.setText(data.getFciZip());
            tvBirth.setPadding(0, 0, 0, 0);
            tvPhone.setPadding(0, 0, 0, 0);
            tvPlayer.setPadding(0, 0, 0, 0);
            tvCard.setPadding(0, 0, getActualWidthOnThisDevice(20), 0);
            tvNo.setPadding(0, 0, getActualWidthOnThisDevice(20), 0);
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
                tvBirth.setText(data.getMemberContact());
                tvPhone.setPadding(getActualWidthOnThisDevice(20),0,0,0);
                tvPlayer.setPadding(getActualWidthOnThisDevice(20),0,0,0);
            }

            if ("3".equals(data.getType())){
                tvBirth.setPadding(getActualWidthOnThisDevice(20), 0, 0, 0);
                tvPhone.setPadding(getActualWidthOnThisDevice(20),0,0,0);
                tvPlayer.setPadding(getActualWidthOnThisDevice(20), 0, 0, 0);
                tvBirth.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(data.getStartDate(), getActivity())+Constants.STR_SPACE+data.getStartTime());
                tvPhone.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(data.getEndDate(), getActivity())+Constants.STR_SPACE+data.getEndTime());
            }

        }
    }




    private void netLink(boolean isRefresh) {

        if (isRefresh) {
            currentPage = 1;
        } else {
            currentPage++;
        }

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.TEE_TIME_SIGN_TYPE, nowSignType);
        params.put(ApiKey.COMMON_COURES_ID, AppUtils.getCurrentCourseId(getActivity()));
        params.put(ApiKey.COURSE_AREA_ID, courseAreaId);
        params.put(ApiKey.TEE_TIME_USER_TYPE, userType);
        params.put(ApiKey.TEE_TIME_INDEX_TYPE, indexType);
        if (currentPage == 1)
        params.put("signId", signId);
        params.put(ApiKey.COMMON_PAGE, String.valueOf(currentPage));
        params.put("show_flag", show);
        if (fromAdd != -1) {
            params.put(ApiKey.TEE_TIME_SIGN_FLAG, Constants.STR_1);
        } else {
            params.put(ApiKey.TEE_TIME_SIGN_FLAG, Constants.STR_0);
        }
        if (Utils.isStringNotNullOrEmpty(bookingDate)) {
            params.put(ApiKey.TEE_TIME_SIGN_BOOKING_DATE, bookingDate);
        }
        HttpManager<JsonSignData> hh = new HttpManager<JsonSignData>(TeeTimeSigneClassifySearchFragment.this) {
            @Override
            public void onJsonSuccess(JsonSignData jo) {
                int tokenStatus = jo.getReturnCode();
                //  if (tvOverlay !=null)tvOverlay.setText("");
                if (tokenStatus == Constants.RETURN_CODE_20301) {
                    if (currentPage == 1){
                        dataList = jo.getDataList();
                        setListTitle();
                    }else{
                        addSignList(jo.getDataList());
                    }


                    listViewBaseAdapter.notifyDataSetChanged();
                    listView.onRefreshComplete();

                    if (currentPage == 1){

                        listView.getRefreshableView().setSelection(2);
                    }
                    if(dataList.size() == 0){
                        rlNotMember.setVisibility(View.VISIBLE);
                    }else{
                        rlNotMember.setVisibility(View.GONE);
                    }
                } else  {
                    currentPage--;
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                currentPage--;

            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.SigningGuest, params);
    }


    private String index;

    private void setListTitle(){
        index = null;
        for(int i = 0;i<dataList.size();i++){
            JsonSignData.SigningData data = dataList.get(i);

            if (Utils.isStringNullOrEmpty(data.getKeyIndex())){

                data.setKeyIndex("#");
            }

            if (!data.getKeyIndex().equals(index)){
                data.setShowTitle(data.getKeyIndex());
                index = data.getKeyIndex();
            }
        }

    }

    private void addSignList(ArrayList<JsonSignData.SigningData> list){


        for (JsonSignData.SigningData data :list){
            if (Utils.isStringNullOrEmpty(data.getKeyIndex())){

                data.setKeyIndex("#");
            }

            if (!data.getKeyIndex().equals(index)){
                data.setShowTitle(data.getKeyIndex());
                index = data.getKeyIndex();
            }

            dataList.add(data);

        }
    }

}
