package cn.situne.itee.fragment.quick;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.player.PlayerInfoEditFragment;
import cn.situne.itee.fragment.teetime.LocationListFragment;
import cn.situne.itee.fragment.teetime.TeeTimeAddFragment;
import cn.situne.itee.fragment.teetime.TeeTimeSearchProfileHistoryFragment;
import cn.situne.itee.fragment.teetime.TeeTimeSearchProfileListFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonBookingDetailListGet;
import cn.situne.itee.manager.jsonentity.JsonCustomerBookingSearch;
import cn.situne.itee.manager.jsonentity.JsonSignData;
import cn.situne.itee.manager.jsonentity.JsonSigningGuest;
import cn.situne.itee.manager.jsonentity.JsonTeeTimeBookingSearch;
import cn.situne.itee.view.IteeSearchView;
import cn.situne.itee.view.IteeTextView;

/**
 * Created by luochao on 12/4/15.
 */

//switch (fragmentSource){
//        case FRAGMENT_SOURCE_1:
//        mMember = memberDataList.get(position);
//        tvShowText.setText(memberDataList.get(position).getKeyWord());
//        break;
//
//        case FRAGMENT_SOURCE_2:
//
//        break;
//
//        case FRAGMENT_SOURCE_3:
//
//        break;
//
//        }
public class SearchBookingFragment extends BaseFragment {
    public static final String FRAGMENT_SOURCE_1 = "1"; //teetime add  fragment item up button
    public static final String FRAGMENT_SOURCE_2 = "2";//teetime add  fragment item down button
    public static final String FRAGMENT_SOURCE_3 = "3";//booking search
    public static final String FRAGMENT_TYPE_1 = "1"; //have quick
    public static final String FRAGMENT_TYPE_2 = "2";//no quick

    private ArrayList<JsonCustomerBookingSearch.Member> memberDataList;
    private ArrayList<JsonSignData.SigningData> signDataList;

    private String fromPage;
    private String fragmentType;
    private String fragmentSource;
    private int currentPage;
    //common view data
    private IteeSearchView titleSearchView;
    private PullToRefreshListView searchDataList;
    private ListViewAdapter listViewAdapter;
    private RelativeLayout body;
    private IteeTextView footerMes;
    private Button addBtn;
    private String selectDate;

    //tee time add
    private String userType;
    private String indexType;
    private boolean isClickAdd;
    private int parentPosition;
    private String nowSignType = Constants.STR_FLAG_YES;
    // teetime add  fragment item up button
    private String memberType;
    private String searchName = StringUtils.EMPTY;
    private int position;
    private String choiceId;
    private String courseAreaId;
    private String addMemberName;
    private String bookingDate;
    private int fromAdd;
    private String show;
    private String signId;
    private String count;
    private Boolean isNoMember;
    //private String searchAgent;
    private boolean isFirst;

    View.OnClickListener gotoDetailedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putString("type", String.valueOf(view.getTag()));
            bundle.putString("selectDate", selectDate);
            push(TeeTimeSearchProfileListFragment.class, bundle);
            // push(TeeTimeSearchProfileHistoryFragment.class,bundle);
        }
    };

    private LinearLayout shortcutLayout;
    private View upLine;
    private IteeTextView upTextView;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_quick_search;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        currentPage = 1;
        bookingDataList = new ArrayList<>();
        memberDataList = new ArrayList<>();
        signDataList = new ArrayList<>();
        isFirst = true;
        Bundle bundle = getArguments();
        if (bundle != null) {
            fragmentType = bundle.getString("fragmentType", FRAGMENT_TYPE_1);
            fragmentSource = bundle.getString("fragmentSource", FRAGMENT_SOURCE_1);
            selectDate = bundle.getString("selectDate", Constants.STR_EMPTY);
            fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE, StringUtils.EMPTY);
            //teetime add
            indexType = bundle.getString(TransKey.TEE_TIME_INDEX_TYPE, StringUtils.EMPTY);
            nowSignType = bundle.getString("nowSignType", Constants.STR_FLAG_YES);
            parentPosition = bundle.getInt("parentPosition");
            isClickAdd = bundle.getBoolean("isClickAdd", false);
            userType = bundle.getString(TransKey.TEE_TIME_USER_TYPE, StringUtils.EMPTY);
            courseAreaId = bundle.getString(TransKey.COURSE_AREA_ID, StringUtils.EMPTY);
            // teetime add  fragment item up button

            memberType = bundle.getString(TransKey.TEE_TIME_MEMBER_TYPE, StringUtils.EMPTY);
            searchName = bundle.getString("searchName", StringUtils.EMPTY);
            position = bundle.getInt("position");
            isNoMember = bundle.getBoolean(TransKey.TEE_TIME_MEMBER_NO_MEMBER);
            //teetime add  fragment item down button

            bookingDate = bundle.getString(TransKey.BOOKING_DATE);
            show = bundle.getString("show", Constants.STR_0);
            fromAdd = bundle.getInt("fromAdd", -1);
            signId = bundle.getString("signId");
            count = bundle.getString("count");
            searchName = bundle.getString("searchName", StringUtils.EMPTY);
        }

        searchDataList = (PullToRefreshListView) rootView.findViewById(R.id.searchDataList);
        shortcutLayout = (LinearLayout) rootView.findViewById(R.id.shortcutLayout);
        body = (RelativeLayout) rootView.findViewById(R.id.body);

        addFooterView();
        listViewAdapter = new ListViewAdapter();
        searchDataList.setAdapter(listViewAdapter);

        initLayout();
        upLine = AppUtils.getSeparatorLine(getBaseActivity());
        upLine.setId(View.generateViewId());

        body.addView(upLine);
        RelativeLayout.LayoutParams upLineParams = (RelativeLayout.LayoutParams) upLine.getLayoutParams();
        upLineParams.height = getActualHeightOnThisDevice(2);
        upLineParams.bottomMargin = getActualHeightOnThisDevice(50);
        upLineParams.width = getActualWidthOnThisDevice(650);
        upLineParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        upLineParams.addRule(RelativeLayout.ABOVE, R.id.shortcutLayout);
        upTextView = new IteeTextView(getBaseActivity());
        upTextView.setTextSize(Constants.FONT_SIZE_SMALLER);
        body.addView(upTextView);

        upTextView.setText(getString(R.string.search_more));
        upTextView.setTextColor(getColor(R.color.common_gray));
        upTextView.setGravity(Gravity.CENTER);

        RelativeLayout.LayoutParams upTextViewParams = (RelativeLayout.LayoutParams) upTextView.getLayoutParams();
        upTextViewParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        upTextViewParams.addRule(RelativeLayout.ABOVE, upLine.getId());
        upTextViewParams.bottomMargin = getActualHeightOnThisDevice(10);

        searchDataList.setMode(PullToRefreshListView.Mode.DISABLED);
        ILoadingLayout headerLayoutProxy = searchDataList.getLoadingLayoutProxy(true, false);
        ILoadingLayout footerLayoutProxy = searchDataList.getLoadingLayoutProxy(false, true);

        headerLayoutProxy.setPullLabel(getString(R.string.app_list_header_refresh_down));
        headerLayoutProxy.setRefreshingLabel(getString(R.string.app_list_header_refresh_down));
        headerLayoutProxy.setReleaseLabel(getString(R.string.app_list_header_refresh_down));

        footerLayoutProxy.setPullLabel(getString(R.string.app_list_header_refresh_loading_more));
        footerLayoutProxy.setRefreshingLabel(getString(R.string.app_list_header_refresh_loading_more));
        footerLayoutProxy.setReleaseLabel(getString(R.string.app_list_header_refresh_loading_more));

        searchDataList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                switch (fragmentSource) {
                    case FRAGMENT_SOURCE_1:
                        searchCustomer(searchName, true);
                        shortcutLayout.setVisibility(View.GONE);
                        break;
                    case FRAGMENT_SOURCE_2:
                        searchSign(searchName, true);
                        shortcutLayout.setVisibility(View.GONE);
                        break;
                    case FRAGMENT_SOURCE_3:
                        shortcutLayout.setVisibility(View.GONE);
                        getCustomerBookingSearch(searchName, true);
                        break;
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                switch (fragmentSource) {
                    case FRAGMENT_SOURCE_1:
                        searchCustomer(searchName, false);
                        shortcutLayout.setVisibility(View.GONE);
                        break;
                    case FRAGMENT_SOURCE_2:
                        searchSign(searchName, false);
                        shortcutLayout.setVisibility(View.GONE);
                        break;
                    case FRAGMENT_SOURCE_3:
                        shortcutLayout.setVisibility(View.GONE);
                        getCustomerBookingSearch(searchName, false);
                        break;
                }
            }
        });
        //searchDataList.setVisibility(View.GONE);
    }

    //region 空方法
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
//endregionf

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        initTitleView();
        initSource();
//        shortcutLayout.setFocusableInTouchMode(true);
//        shortcutLayout.setFocusable(true);
//        shortcutLayout.requestFocus();
    }

    @Override
    protected void reShowWithBackValue() {
        super.reShowWithBackValue();
    }

    private void initSource() {
        switch (fragmentSource) {
            case FRAGMENT_SOURCE_1:
                if (Utils.isStringNotNullOrEmpty(searchName)) {
                    searchCustomer(searchName, true);
                    titleSearchView.setText(searchName);
                    titleSearchView.setSelection(searchName.length());
                }
                Utils.showKeyboard(titleSearchView, getBaseActivity());
                shortcutLayout.setVisibility(View.GONE);
                searchDataList.setMode(PullToRefreshBase.Mode.BOTH);
                break;
            case FRAGMENT_SOURCE_2:
                if (Utils.isStringNotNullOrEmpty(searchName)) {
                    searchSign(searchName, true);
                    titleSearchView.setText(searchName);
                }
                Utils.showKeyboard(titleSearchView, getBaseActivity());
                shortcutLayout.setVisibility(View.GONE);
                searchDataList.setMode(PullToRefreshBase.Mode.BOTH);
                break;
            case FRAGMENT_SOURCE_3:
                if (isFirst) {
                    shortcutLayout.setVisibility(View.VISIBLE);
                    searchDataList.setMode(PullToRefreshBase.Mode.DISABLED);
                }
                titleSearchView.setText(searchName);
                getCustomerBookingSearch(searchName, true);
                break;
        }
        isFirst = false;
    }

    private void initLayout() {
        LinearLayout row1 = new LinearLayout(getBaseActivity());
        LinearLayout row2 = new LinearLayout(getBaseActivity());
        LinearLayout row3 = new LinearLayout(getBaseActivity());
        row1.setOrientation(LinearLayout.HORIZONTAL);
        row2.setOrientation(LinearLayout.HORIZONTAL);
        row3.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams bookingBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(200));
        LinearLayout.LayoutParams onCourseBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(200));
        LinearLayout.LayoutParams checkOutBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(200));
        bookingBtnParams.weight = 1;
        onCourseBtnParams.weight = 1;
        checkOutBtnParams.weight = 1;
        ClickItem bookingBtn = new ClickItem(getBaseActivity(), null);
        ClickItem onCourseBtn = new ClickItem(getBaseActivity(), null);
        ClickItem checkOutBtn = new ClickItem(getBaseActivity(), null);

        bookingBtn.setOnClickListener(gotoDetailedListener);
        onCourseBtn.setOnClickListener(gotoDetailedListener);
        checkOutBtn.setOnClickListener(gotoDetailedListener);
        bookingBtn.setLayoutParams(bookingBtnParams);
        onCourseBtn.setLayoutParams(onCourseBtnParams);
        checkOutBtn.setLayoutParams(checkOutBtnParams);
        bookingBtn.setLayout(getString(R.string.search_booking), R.drawable.search_profile_1);
        onCourseBtn.setLayout(getString(R.string.search_on_course), R.drawable.search_profile_2);
        checkOutBtn.setLayout(getString(R.string.search_check_out), R.drawable.search_profile_3);

        row1.addView(bookingBtn);
        row1.addView(AppUtils.getVerticalLine(getBaseActivity(), getActualWidthOnThisDevice(1)));
        row1.addView(onCourseBtn);
        row1.addView(AppUtils.getVerticalLine(getBaseActivity(), getActualWidthOnThisDevice(1)));
        row1.addView(checkOutBtn);
        bookingBtn.setTag("1");
        onCourseBtn.setTag("2");
        checkOutBtn.setTag("3");

        LinearLayout.LayoutParams memberBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(200));
        LinearLayout.LayoutParams guestBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(200));
        LinearLayout.LayoutParams walkInBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(200));
        memberBtnParams.weight = 1;
        guestBtnParams.weight = 1;
        walkInBtnParams.weight = 1;
        ClickItem memberBtn = new ClickItem(getBaseActivity(), AppUtils.getSeparatorLine(getBaseActivity()));
        ClickItem guestBtn = new ClickItem(getBaseActivity(), AppUtils.getSeparatorLine(getBaseActivity()));
        ClickItem walkInBtn = new ClickItem(getBaseActivity(), AppUtils.getSeparatorLine(getBaseActivity()));

        memberBtn.setTag("4");
        guestBtn.setTag("5");
        walkInBtn.setTag("6");

        memberBtn.setOnClickListener(gotoDetailedListener);
        guestBtn.setOnClickListener(gotoDetailedListener);
        walkInBtn.setOnClickListener(gotoDetailedListener);

        memberBtn.setLayoutParams(memberBtnParams);
        guestBtn.setLayoutParams(guestBtnParams);
        walkInBtn.setLayoutParams(walkInBtnParams);
        memberBtn.setLayout(getString(R.string.search_member), R.drawable.search_profile_4);
        guestBtn.setLayout(getString(R.string.search_guest), R.drawable.search_profile_5);
        walkInBtn.setLayout(getString(R.string.search_walk_in), R.drawable.search_profile_6);

        row2.addView(memberBtn);
        row2.addView(AppUtils.getVerticalLine(getBaseActivity(), getActualWidthOnThisDevice(1)));
        row2.addView(guestBtn);
        row2.addView(AppUtils.getVerticalLine(getBaseActivity(), getActualWidthOnThisDevice(1)));
        row2.addView(walkInBtn);

        LinearLayout.LayoutParams agentBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(200));
        LinearLayout.LayoutParams eventBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(200));
        LinearLayout.LayoutParams otherBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(200));
        agentBtnParams.weight = 1;
        eventBtnParams.weight = 1;
        otherBtnParams.weight = 1;
        ClickItem agentBtn = new ClickItem(getBaseActivity(), AppUtils.getSeparatorLine(getBaseActivity()));
        ClickItem eventBtn = new ClickItem(getBaseActivity(), AppUtils.getSeparatorLine(getBaseActivity()));
        ClickItem otherBtn = new ClickItem(getBaseActivity(), AppUtils.getSeparatorLine(getBaseActivity()));
        agentBtn.setLayoutParams(agentBtnParams);
        eventBtn.setLayoutParams(eventBtnParams);
        otherBtn.setLayoutParams(otherBtnParams);
        agentBtn.setLayout(getString(R.string.search_agent), R.drawable.search_profile_7);
        eventBtn.setLayout(getString(R.string.search_event), R.drawable.search_profile_8);
        otherBtn.setLayout(Constants.STR_EMPTY, 0);

        agentBtn.setOnClickListener(gotoDetailedListener);
        eventBtn.setOnClickListener(gotoDetailedListener);

        row3.addView(agentBtn);
        row3.addView(AppUtils.getVerticalLine(getBaseActivity(), getActualWidthOnThisDevice(1)));
        row3.addView(eventBtn);
        row3.addView(AppUtils.getVerticalLine(getBaseActivity(), getActualWidthOnThisDevice(1)));
        row3.addView(otherBtn);
        agentBtn.setTag("7");
        eventBtn.setTag("8");

        initLayoutParams(row1);
        initLayoutParams(row2);
        initLayoutParams(row3);
        shortcutLayout.addView(row1);
        shortcutLayout.addView(row2);
        shortcutLayout.addView(row3);
    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        startSearch(titleSearchView.getText().toString());
        searchName = titleSearchView.getText().toString();
    }

    private void initLayoutParams(LinearLayout row) {
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(getActualWidthOnThisDevice(600), getActualHeightOnThisDevice(200));
        row.setLayoutParams(rowParams);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (footerMes != null) {
            footerMes.setText(Constants.STR_EMPTY);
            addBtn.setVisibility(View.GONE);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private LinearLayout llFooterView;

    private void addFooterView() {
        llFooterView = new LinearLayout(getBaseActivity());
        llFooterView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams footerMesParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

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
                boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEdit, getActivity()) && AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEditNonmember, getActivity());
                if (hasPermission) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeAdd.value());
                    bundle.putString(TransKey.TEE_TIME_MEMBER_NAME, titleSearchView.getText().toString());
                    push(PlayerInfoEditFragment.class, bundle);
                } else {
                    AppUtils.showHaveNoPermission(SearchBookingFragment.this);
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
        searchDataList.getRefreshableView().addFooterView(llFooterView);
    }


    private void startSearch(String key) {
        switch (fragmentSource) {
            case FRAGMENT_SOURCE_1:
                searchCustomer(key, true);
                break;
            case FRAGMENT_SOURCE_2:
                searchSign(key, true);
                break;
            case FRAGMENT_SOURCE_3:
//              searchDataList.setMode(PullToRefreshBase.Mode.BOTH);
                getCustomerBookingSearch(key, true);
                break;
        }
    }

    private void initTitleView() {
        ActionBar actionBar = getBaseActivity().getSupportActionBar();
        LinearLayout.LayoutParams titleViewParams = new LinearLayout.LayoutParams(Utils.getWidth(getActivity()), getActionBarHeight());
        LinearLayout titleView = new LinearLayout(getBaseActivity());
        titleView.setBackgroundColor(getColor(R.color.common_light_gray));
        titleView.setLayoutParams(titleViewParams);
        titleView.setPadding(getActualWidthOnThisDevice(0), 0, 0, 0);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(Utils.getWidth(getActivity()) - getActualWidthOnThisDevice(160), getActionBarHeight() - 20);
        titleView.setGravity(Gravity.CENTER_VERTICAL);

        titleSearchView = new IteeSearchView(getBaseActivity());
        titleSearchView.setHint(getString(R.string.common_search));
        titleSearchView.setTextSize(Constants.FONT_SIZE_NORMAL);
        titleSearchView.setBackground(getResources().getDrawable(R.drawable.bg_search_view));
        titleSearchView.setLayoutParams(llp);
        titleSearchView.setIcon(getActionBarHeight() - getActualHeightOnThisDevice(35), R.drawable.icon_search_del, R.drawable.icon_search_del, R.drawable.icon_search, R.drawable.icon_search);
        titleSearchView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_UP == event.getAction()) {
                    if (keyCode == 66) {
                        Utils.hideKeyboard(getBaseActivity());
                        //api
                        // getCustomerBookingSearch(titleSearchView.getText().toString());
                        startSearch(titleSearchView.getText().toString());
                        searchName = titleSearchView.getText().toString();
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
                startSearch(titleSearchView.getText().toString());
                searchName = titleSearchView.getText().toString();
            }
        });

        titleSearchView.requestFocus();
        // Utils.showKeyboard(titleSearchView, getActivity());

        LinearLayout.LayoutParams titleSearchViewLayoutParams = (LinearLayout.LayoutParams) titleSearchView.getLayoutParams();
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

        LinearLayout.LayoutParams cancelBtnLayoutParams = (LinearLayout.LayoutParams) cancelBtn.getLayoutParams();
        cancelBtnLayoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        cancelBtn.setLayoutParams(cancelBtnLayoutParams);
        actionBar.setCustomView(titleView);
    }

    private List<JsonTeeTimeBookingSearch.BookingItem> bookingDataList;

    class ListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            int size = 0;
            switch (fragmentSource) {
                case FRAGMENT_SOURCE_1:
                    size = memberDataList.size();
                    break;
                case FRAGMENT_SOURCE_2:
                    size = signDataList.size();
                    break;
                case FRAGMENT_SOURCE_3:
                    size = bookingDataList.size();
                    break;
            }
            return size;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ListViewItem item = null;
            if (convertView == null) {
                item = new ListViewItem(getBaseActivity());
            } else {
                item = (ListViewItem) convertView;
            }
            item.setItemValue(position);
            return item;
        }
    }//cls ListViewItem

    //itee 11 dev
    class ListViewItem extends RelativeLayout {
        private IteeTextView tvShowText;
        private JsonCustomerBookingSearch.Member mMember;
        private JsonSignData.SigningData mSign;
        private JsonTeeTimeBookingSearch.BookingItem bookingItem;

        public ListViewItem(Context context) {
            super(context);
            ListView.LayoutParams myParams = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
            this.setLayoutParams(myParams);

            RelativeLayout.LayoutParams tvShowTextParams = new RelativeLayout.LayoutParams(ListView.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
            tvShowTextParams.leftMargin = getActualWidthOnThisDevice(40);
            tvShowText = new IteeTextView(context);
            tvShowText.setLayoutParams(tvShowTextParams);
            tvShowText.setGravity(Gravity.CENTER_VERTICAL);

            AppUtils.addBottomSeparatorLine(this, context);
            this.addView(tvShowText);
            this.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectMember();
                }
            });
        }

        public void setItemValue(int position) {
            switch (fragmentSource) {
                case FRAGMENT_SOURCE_1:
                    mMember = memberDataList.get(position);
                    tvShowText.setText(mMember.getKeyWord() + " " + mMember.getMemberNo());
                    break;
                case FRAGMENT_SOURCE_2:
                    mSign = signDataList.get(position);
                    tvShowText.setText(mSign.getKeyWord() + " " + mSign.getMemberNo());
                    break;
                case FRAGMENT_SOURCE_3:
                    bookingItem = bookingDataList.get(position);
                    tvShowText.setText(bookingItem.getKeyWord());
                    break;
            }
        }

        private void selectMember() {
            Utils.hideKeyboard(getBaseActivity());
            Bundle bundle = new Bundle();
            switch (fragmentSource) {
                case FRAGMENT_SOURCE_1:
                //region
                    if (isClickAdd) {
                        bundle.putString("fromFlag", "memberName");
                        bundle.putSerializable("member", mMember);
                        bundle.putString("signType", mMember.getMemberType());
                    } else {
                        bundle.putString("fromFlag", "memberRole");

                        bundle.putSerializable("member", mMember);
                        bundle.putInt("position", position);
                        bundle.putInt("parentPosition", parentPosition);
                        bundle.putString("nowSignType", mMember.getMemberType());
                        bundle.putString(TransKey.END_DATE_FLAG, mMember.getEndDateFlag());
                    }
                    try {
                        bundle.putBoolean("isBack", true);
                        doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
//endregion
                case FRAGMENT_SOURCE_2:
                    //region
                    JsonSigningGuest.Member tempMember = new JsonSigningGuest.Member();
                    if (Constants.STR_1.equals(mSign.getType())) {
                        tempMember.setSignNumber(mSign.getSignNumber());
                        tempMember.setPhone(mSign.getPhone());
                        tempMember.setNameSort(mSign.getNameSort());
                        tempMember.setMemberId(Integer.parseInt(mSign.getMemberId()));
                        tempMember.setMemberNo(mSign.getMemberNo());
                        tempMember.setMemberName(mSign.getMemberName());
                    }

                    if (Constants.STR_2.equals(mSign.getType())) {
                        bundle.putString("agentId", mSign.getAgentCode());
                    }

                    if (fromAdd != -1) {
                        //can not overstep Member's signNumber
                        int signNumber = 0;
                        if (Utils.isStringNotNullOrEmpty(tempMember.getSignNumber())) {
                            signNumber = Integer.valueOf(tempMember.getSignNumber());
                        }
                        int nowCount = 0;
                        Map<String, Integer> map = null;
                        if (Utils.isStringNotNullOrEmpty(count)) {
                            map = (Map<String, Integer>) Utils.getObjectFromString(count);
                        }
                        if (map != null && tempMember.getMemberId() != null && map.get(String.valueOf(tempMember.getMemberId())) != null) {
                            nowCount = map.get(String.valueOf(tempMember.getMemberId()));
                        }
                        if (Constants.STR_FLAG_YES.equals(mSign.getType()) && nowCount >= signNumber) {
                            AppUtils.showMessageWithOkButton(SearchBookingFragment.this, getString(R.string.error_mes00005));
                        } else {
                            //客户绑定人员
                            bundle.putString("fromFlag", "itemMemberDetail");
                            bundle.putString("signType", mSign.getType());
                            bundle.putInt("position", fromAdd);
                            bundle.putInt("parentPosition", parentPosition);
                            bundle.putSerializable("member", tempMember);
                            try {
                                bundle.putBoolean("isBack", true);
                                doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        if (isClickAdd) {
                            JsonCustomerBookingSearch.Member jMember = new JsonCustomerBookingSearch.Member();
                            jMember.setShowTitle(mSign.getShowTitle());
                            jMember.setMemberBirth(mSign.getMemberContact());
                            jMember.setMemberTel(mSign.getPhone());
                            jMember.setMemberNo(mSign.getAgentCode());
                            jMember.setMemberName(mSign.getMemberName());
                            jMember.setIndex(mSign.getKeyIndex());
                            jMember.setjAgent(true);
                            bundle.putSerializable("member", jMember);
                        } else {
                            bundle.putSerializable("member", tempMember);
                        }
                        //返回客户名字
                        bundle.putString("fromFlag", "memberName");
                        bundle.putString("signType", mSign.getType());
                        try {
                            bundle.putBoolean("isBack", true);
                            doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                //endregion
                case FRAGMENT_SOURCE_3:
                    Bundle bundle4Location = getArguments();
                    bundle4Location.putString(TransKey.BOOKING_ORDER_NO, bookingItem.getBookingId());
                    bundle4Location.putBoolean("isAdd", false);
                    bundle4Location.putString(TransKey.COMMON_FROM_PAGE, SearchBookingFragment.class.getName());
                    push(TeeTimeAddFragment.class, bundle4Location);
//                    push(LocationListFragment.class, bundle4Location);   //搜索后跳转到LocationList页
                    break;
            }
        }
    }//cls listViewItem

    class ClickItem extends LinearLayout {
        private ImageView upView;
        private IteeTextView tvText;

        public void setLayout(String text, int back) {
            tvText.setText(text);
            upView.setBackgroundResource(back);
        }

        public ClickItem(Context context, View top) {   //搜索页面下方预约、场下、结账等等可以点击的大方块按钮？
            super(context);
            setOrientation(VERTICAL);
            LinearLayout.LayoutParams bodyParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            LinearLayout body = new LinearLayout(context);
            body.setLayoutParams(bodyParams);
            body.setGravity(Gravity.CENTER);
            body.setOrientation(VERTICAL);
            LinearLayout.LayoutParams upViewParams = new LinearLayout.LayoutParams(getActualHeightOnThisDevice(50), getActualHeightOnThisDevice(50));
            upView = new ImageView(context);
            upView.setLayoutParams(upViewParams);
            tvText = new IteeTextView(context);
            tvText.setTextColor(getColor(R.color.common_gray));
            tvText.setGravity(Gravity.CENTER);
            tvText.setTextSize(Constants.FONT_SIZE_SMALLER);
            if (top != null)
                this.addView(top);
            this.addView(body);
            body.addView(upView);
            body.addView(tvText);
        }
    }//cls ClickItem

    private void addMemberList(ArrayList<JsonCustomerBookingSearch.Member> memberDataList) {
        for (JsonCustomerBookingSearch.Member member : memberDataList) {
            this.memberDataList.add(member);
        }
    }

    private void addSignList(ArrayList<JsonSignData.SigningData> signDataList) {
        for (JsonSignData.SigningData sign : signDataList) {
            this.signDataList.add(sign);
        }
    }
    // api
    // search customer
    private void searchCustomer(String name, boolean isRefresh) {
        if (Utils.isStringNotNullOrEmpty(name)) {
            if (isRefresh) {
                currentPage = 1;
            } else {
                currentPage++;
            }
            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
            params.put(ApiKey.TEE_TIME_MEMBER_TYPE_ID, memberType);
            params.put(ApiKey.TEE_TIME_CUSTOMER_NAME, name);
            params.put(ApiKey.COMMON_COURES_ID, AppUtils.getCurrentCourseId(getActivity()));
            params.put(ApiKey.TEE_TIME_USER_TYPE, userType);
            params.put(ApiKey.TEE_TIME_INDEX_TYPE, indexType);
            params.put(ApiKey.COMMON_PAGE, String.valueOf(currentPage));

            HttpManager<JsonCustomerBookingSearch> hh = new HttpManager<JsonCustomerBookingSearch>(SearchBookingFragment.this) {
                @Override
                public void onJsonSuccess(JsonCustomerBookingSearch jo) {
                    int tokenReturn = jo.getReturnCode();
                    if (tokenReturn == Constants.RETURN_CODE_20301) {
                        if (currentPage != 1) {
                            addMemberList(jo.getDataList());
                            //listViewAdapter.setMemberDataList(listViewAdapter.getMemberDataList().add);
                            //  listViewAdapter.setSize(listViewAdapter.getSize()+jo.getDataList().size());
                        } else {
                            memberDataList = jo.getDataList();
                        }
                        // searchDataList.setAdapter(listViewAdapter);
                        listViewAdapter.notifyDataSetChanged();
                        searchDataList.onRefreshComplete();
                        searchDataList.setMode(PullToRefreshBase.Mode.BOTH);

                        if (memberDataList.size() <= 0) {
                            if (currentPage == 1) {
                                footerMes.setText(StringUtils.EMPTY);
                                SpannableString ss = new SpannableString(titleSearchView.getText());
                                ss.setSpan(new ForegroundColorSpan(getColor(R.color.common_blue)), 0, ss.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                                ss.setSpan(new RelativeSizeSpan(1.3f), 0, ss.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                                footerMes.setVisibility(View.VISIBLE);
                                footerMes.append(getString(R.string.add_tee_time_setting_search_mes1));
                                footerMes.append(ss);
                                footerMes.append(getString(R.string.add_tee_time_setting_search_mes2));
                                addBtn.setVisibility(View.VISIBLE);

                                llFooterView.setVisibility(View.VISIBLE);
                                searchDataList.setMode(PullToRefreshBase.Mode.DISABLED);
                            } else {
                                currentPage--;
                                addBtn.setVisibility(View.GONE);
                                footerMes.setVisibility(View.GONE);
                                llFooterView.setVisibility(View.GONE);
                            }
                        } else {
                            addBtn.setVisibility(View.GONE);
                            footerMes.setVisibility(View.GONE);
                            llFooterView.setVisibility(View.GONE);
                        }
                        Utils.hideKeyboard(getBaseActivity());
                    } else {
                        currentPage--;
                    }
                }

                @Override
                public void onJsonError(VolleyError error) {
                    currentPage--;
                }
            };
            hh.start(getActivity(), ApiManager.HttpApi.CsbookingSearch, params);
        } else {
            searchDataList.onRefreshComplete();
        }
    }

    private void searchSign(String name, boolean isRefresh) {
        if (Utils.isStringNotNullOrEmpty(name)) {
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
            if (isNoMember) {
                params.put("member_disablied", "1");
            } else {
                params.put("member_disablied", "0");
            }
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
            params.put(ApiKey.COMMON_PAGE, String.valueOf(currentPage));

            HttpManager<JsonSignData> hh = new HttpManager<JsonSignData>(SearchBookingFragment.this) {
                @Override
                public void onJsonSuccess(JsonSignData jo) {
                    int tokenStatus = jo.getReturnCode();
                    //  if (tvOverlay !=null)tvOverlay.setText("")
                    if (tokenStatus == Constants.RETURN_CODE_20301) {
                        if (currentPage != 1) {
                            addSignList(jo.getDataList());
                        } else {
                            signDataList = jo.getDataList();
                        }
                        listViewAdapter.notifyDataSetChanged();
                        searchDataList.onRefreshComplete();
                        searchDataList.setMode(PullToRefreshBase.Mode.BOTH);
                        if (signDataList.size() <= 0) {
                            if (currentPage == 1) {
                                footerMes.setText(StringUtils.EMPTY);
                                SpannableString ss = new SpannableString(titleSearchView.getText());
                                ss.setSpan(new ForegroundColorSpan(getColor(R.color.common_blue)), 0, ss.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                                ss.setSpan(new RelativeSizeSpan(1.3f), 0, ss.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                                footerMes.setVisibility(View.VISIBLE);
                                footerMes.append(getString(R.string.add_tee_time_setting_search_mes1));
                                footerMes.append(ss);
                                footerMes.append(getString(R.string.add_tee_time_setting_search_mes2));
                                addBtn.setVisibility(View.VISIBLE);

                                llFooterView.setVisibility(View.VISIBLE);
                                searchDataList.setMode(PullToRefreshBase.Mode.DISABLED);
                            } else {
                                currentPage--;
                                addBtn.setVisibility(View.GONE);
                                footerMes.setVisibility(View.GONE);
                                llFooterView.setVisibility(View.GONE);
                            }
                        } else {
                            addBtn.setVisibility(View.GONE);
                            footerMes.setVisibility(View.GONE);
                            llFooterView.setVisibility(View.GONE);
                        }
                        Utils.hideKeyboard(getBaseActivity());
                    } else {
                        currentPage--;
                    }
                }

                @Override
                public void onJsonError(VolleyError error) {
                    currentPage--;
                }
            };
            hh.start(getActivity(), ApiManager.HttpApi.SigningGuest, params);
        } else {
            searchDataList.onRefreshComplete();
        }
    }

    private void addBookingDataList(List<JsonTeeTimeBookingSearch.BookingItem> bookingDataList) {
        for (JsonTeeTimeBookingSearch.BookingItem data : bookingDataList) {
            this.bookingDataList.add(data);
        }
    }

    private void getCustomerBookingSearch(String name, boolean isRefresh) {
        if (Utils.isStringNotNullOrEmpty(name)) {
            if (isRefresh) {
                currentPage = 1;
            } else {
                currentPage++;
            }
            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
            params.put(ApiKey.TEE_TIME_KEY_WORD, name);
            params.put(ApiKey.TEE_TIME_BOOKING_DATE, selectDate);
            params.put(ApiKey.COMMON_PAGE, String.valueOf(currentPage));

            HttpManager<JsonTeeTimeBookingSearch> hh = new HttpManager<JsonTeeTimeBookingSearch>(SearchBookingFragment.this) {
                @Override
                public void onJsonSuccess(JsonTeeTimeBookingSearch jo) {
                    Integer returnCode = jo.getReturnCode();
                    String msg = jo.getReturnInfo();
                    if (returnCode == Constants.RETURN_CODE_20301) {
                        shortcutLayout.setVisibility(View.GONE);
                        upLine.setVisibility(View.GONE);
                        upTextView.setVisibility(View.GONE);
                        searchDataList.setMode(PullToRefreshListView.Mode.BOTH);
                        if (currentPage != 1) {
                            addBookingDataList(jo.getDataList());
                        } else {
                            bookingDataList = jo.getDataList();
                        }
                        listViewAdapter.notifyDataSetChanged();
                        searchDataList.onRefreshComplete();

                        if (bookingDataList.size() <= 0) {
                            if (currentPage == 1) {
                                footerMes.setText(StringUtils.EMPTY);
                                SpannableString ss = new SpannableString(titleSearchView.getText());
                                ss.setSpan(new ForegroundColorSpan(getColor(R.color.common_blue)), 0, ss.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                                ss.setSpan(new RelativeSizeSpan(1.3f), 0, ss.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                                footerMes.setVisibility(View.VISIBLE);
                                footerMes.append(getString(R.string.add_tee_time_setting_search_mes1));
                                footerMes.append(ss);
                                footerMes.append(getString(R.string.add_tee_time_setting_search_mes2));
                                addBtn.setVisibility(View.VISIBLE);

                                llFooterView.setVisibility(View.VISIBLE);
                                searchDataList.setMode(PullToRefreshBase.Mode.DISABLED);
                            } else {
                                currentPage--;
                                addBtn.setVisibility(View.GONE);
                                footerMes.setVisibility(View.GONE);
                                llFooterView.setVisibility(View.GONE);
                            }
                        } else {
                            addBtn.setVisibility(View.GONE);
                            footerMes.setVisibility(View.GONE);
                            llFooterView.setVisibility(View.GONE);
                        }
                        listViewAdapter.notifyDataSetChanged();
                    } else {
                        Utils.showShortToast(getActivity(), msg);
                        currentPage--;
                    }
                }

                @Override
                public void onJsonError(VolleyError error) {
                    currentPage--;
                }
            };
            hh.start(getActivity(), ApiManager.HttpApi.BookingSearch, params);
        }
    }
}
