package cn.situne.itee.fragment.teetime;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.android.volley.NetworkResponse;
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
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.player.PlayerInfoEditFragment;
import cn.situne.itee.fragment.quick.SearchBookingFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonGetGuestTypeList;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.StickyLayout;

/**
 * Created by luochao on 11/18/15.
 */
public class TeeTimeSignedByFragment  extends BaseFragment {

    private  MemberTypeLayout memberTypeLayout;
    private StickyLayout stickyLayout;
    private ScrollView scrollView;
    private LinearLayout body;

    private IteeTextView etSearch;
    private String addMemberName;


    private String fromPage;
    private int fromAdd = -1;
    private String signId;
    private int parentPosition;
    private Map<String, Integer> map;
    private Boolean isNoMember;
    private String bookingDate;
    private String courseAreaId;
    private String show;
    private String addAgent;
    private Boolean isNoEvent;

    private String count;
    @Override
    protected int getFragmentId() {
        return R.layout.fragment_choose_customer;
    }
    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {


        Bundle bundle = getArguments();
        if (bundle != null) {
            fromPage= bundle.getString(TransKey.COMMON_FROM_PAGE, StringUtils.EMPTY);
            isNoEvent = bundle.getBoolean(TransKey.TEE_TIME_MEMBER_NO_EVENT);
            fromAdd = bundle.getInt("fromAdd", -1);
            parentPosition = bundle.getInt("parentPosition");
            isNoMember = bundle.getBoolean(TransKey.TEE_TIME_MEMBER_NO_MEMBER);
            signId = bundle.getString("signId");
        bookingDate = bundle.getString(TransKey.BOOKING_DATE);
        courseAreaId = bundle.getString(TransKey.COURSE_AREA_ID, StringUtils.EMPTY);
        show  = bundle.getString("show",Constants.STR_0);
        addAgent = bundle.getString("addAgent",Constants.STR_0);
        if (Utils.isStringNotNullOrEmpty(bundle.getString("count"))) {
            count = bundle.getString("count");
            map = (Map<String, Integer>) Utils.getObjectFromString(bundle.getString("count"));
        }

        addMemberName = bundle.getString(TransKey.COURSE_ADD_MEMBER_NAME, StringUtils.EMPTY);
    }

    stickyLayout = (StickyLayout)rootView.findViewById(R.id.sticky_layout);

    RelativeLayout stickyHeader = (RelativeLayout) rootView.findViewById(R.id.sticky_header);
    stickyHeader.getLayoutParams().height = getActualHeightOnThisDevice(100);
        scrollView= (ScrollView) rootView.findViewById(R.id.scrollView);
        body= (LinearLayout) rootView.findViewById(R.id.body);

        etSearch = (IteeTextView)rootView.findViewById(R.id.et_search);


            LinearLayout.LayoutParams tvMemberParmas = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(100));
            IteeTextView tvMember = new IteeTextView(getBaseActivity());
            tvMember.setLayoutParams(tvMemberParmas);
            tvMember.setText(getString(R.string.customers_member));
            tvMember.setPadding(getActualWidthOnThisDevice(20), 0, 0, 0);
            body.addView(tvMember);
            body.addView(AppUtils.getSeparatorLine(getBaseActivity()));


            LinearLayout.LayoutParams chooseLetterLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            ChooseLetterLayout chooseLetterMemLayout = new ChooseLetterLayout(getBaseActivity());
            chooseLetterMemLayout.setSignType(Constants.STR_1);
            chooseLetterMemLayout.setLayoutParams(chooseLetterLayoutParams);
            body.addView(chooseLetterMemLayout);
            LinearLayout.LayoutParams memberTypeLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            memberTypeLayout = new MemberTypeLayout(getBaseActivity());
            memberTypeLayout.setLayoutParams(memberTypeLayoutParams);
            body.addView(memberTypeLayout);


            View v = AppUtils.getSeparatorLine(getBaseActivity());
            body.addView(v);
            v.getLayoutParams().height = getActualHeightOnThisDevice(50);
            v.setBackgroundColor(getColor(R.color.common_light_gray));


        if (isNoMember){
            tvMember.setVisibility(View.GONE);
            memberTypeLayout.setVisibility(View.GONE);
            v.setVisibility(View.GONE);

            chooseLetterMemLayout.setVisibility(View.GONE);
        }




        LinearLayout.LayoutParams tvAgentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(100));
        IteeTextView tvAgent = new IteeTextView(getBaseActivity());
        tvAgent.setLayoutParams(tvAgentParams);
        tvAgent.setText(getString(R.string.agents_agent));
        tvAgent.setPadding(getActualWidthOnThisDevice(20), 0, 0, 0);
        body.addView(tvAgent);
        body.addView(AppUtils.getSeparatorLine(getBaseActivity()));

        LinearLayout.LayoutParams chooseLetterNonMemLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        ChooseLetterLayout chooseLetterNonMemLayout = new ChooseLetterLayout(getBaseActivity());
        chooseLetterNonMemLayout.setLayoutParams(chooseLetterNonMemLayoutParams);
        chooseLetterNonMemLayout.setSignType(Constants.STR_2);

        body.addView(chooseLetterNonMemLayout);


        View v1 = AppUtils.getSeparatorLine(getBaseActivity());
        body.addView(v1);
        v1.getLayoutParams().height = getActualHeightOnThisDevice(50);
        v1.setBackgroundColor(getColor(R.color.common_light_gray));


        LinearLayout.LayoutParams tvEventParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(100));
        IteeTextView tvEvent = new IteeTextView(getBaseActivity());
        tvEvent.setLayoutParams(tvEventParams);
        tvEvent.setText(getString(R.string.event_event));
        tvEvent.setPadding(getActualWidthOnThisDevice(20), 0, 0, 0);
        body.addView(tvEvent);
        body.addView(AppUtils.getSeparatorLine(getBaseActivity()));


        LinearLayout.LayoutParams chooseLetterEventLayoutLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        ChooseLetterLayout chooseLetterEventLayout = new ChooseLetterLayout(getBaseActivity(),true);
        chooseLetterNonMemLayout.setLayoutParams(chooseLetterEventLayoutLayoutParams);
        chooseLetterEventLayout.setSignType("3");
        body.addView(chooseLetterEventLayout);

        getGuestType();
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        stickyLayout.setMaxHeaderHeight(100);
        stickyLayout.setOnGiveUpTouchEventListener(new StickyLayout.OnGiveUpTouchEventListener() {
            @Override
            public boolean giveUpTouchEvent(MotionEvent event) {

                return scrollView.getScrollY() <= 0;
            }
        });

        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("fromAdd", fromAdd);
                bundle.putString("signId", signId);
                bundle.putInt("parentPosition", parentPosition);
                bundle.putString("count", count);
                bundle.putString("nowSignType",  Constants.STR_0);
                bundle.putString(TransKey.BOOKING_DATE, bookingDate);
                bundle.putString(TransKey.COURSE_AREA_ID, String.valueOf(courseAreaId));
                bundle.putString(TransKey.COMMON_FROM_PAGE,TeeTimeSignedByFragment.class.getName());
                bundle.putString("addAgent", "1");
                bundle.putBoolean(TransKey.TEE_TIME_MEMBER_NO_MEMBER, isNoMember);


                bundle.putString("fragmentSource", SearchBookingFragment.FRAGMENT_SOURCE_2);
                bundle.putString("fragmentType", SearchBookingFragment.FRAGMENT_TYPE_2);

                push(SearchBookingFragment.class, bundle);

               // push(TeeTimeSigneSearchFragment.class,bundle);

            }
        });
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
        getTvLeftTitle().setText(getString(R.string.sign_by));

        getTvRight().setBackground(null);
        getTvRight().setBackgroundResource(R.drawable.icon_common_add);
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEdit, getActivity())
                        && AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEditNonmember, getActivity());
                if (hasPermission) {


                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeAdd.value());

                    bundle.putString(TransKey.COURSE_ADD_MEMBER_NAME, addMemberName);

                    push(PlayerInfoEditFragment.class, bundle);
                } else {
                    AppUtils.showHaveNoPermission(TeeTimeSignedByFragment.this);
                }
            }
        });
    }
    class ChooseLetterLayout extends LinearLayout{
        private String signType;

        public String getSignType() {
            return signType;
        }

        public void setSignType(String signType) {
            this.signType = signType;
        }

        private LinearLayout upLayout;
        private LinearLayout downLayout;
        private String[] dataUp = {"ALL","A-D","E-I","J-M"};
        private String[] dataDown = {"","N-S","T-W","X-Z"};
        private String[] dataUp1 = {"ALL","","",""};
        public ChooseLetterLayout(Context context,boolean isAll) {
            super(context);
            setOrientation(VERTICAL);

            LinearLayout.LayoutParams upLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,getActualWidthOnThisDevice(100));

            upLayout = new LinearLayout(context);
            upLayout.setLayoutParams(upLayoutParams);
            upLayout.setOrientation(HORIZONTAL);
            for (String upStr:dataUp1){
                LinearLayout.LayoutParams upTParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                upTParams.weight = 1;
                IteeTextView upT = new IteeTextView(context);
                upT.setText(upStr);

                upT.setLayoutParams(upTParams);
                upT.setGravity(Gravity.CENTER);
                upT.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("fromAdd", fromAdd);
                        bundle.putString("signId", signId);
                        bundle.putInt("parentPosition", parentPosition);
                        bundle.putString("count", count);
                        bundle.putString("nowSignType", signType);
                        bundle.putString(TransKey.BOOKING_DATE, bookingDate);

                        bundle.putString(TransKey.TEE_TIME_INDEX_TYPE, Constants.STR_EMPTY);
                        bundle.putString(TransKey.COURSE_AREA_ID, String.valueOf(courseAreaId));
                        bundle.putString(TransKey.COMMON_FROM_PAGE,TeeTimeSignedByFragment.class.getName());
                        bundle.putString("addAgent", "1");
                        bundle.putString("notCancel", Constants.STR_1);
                        push(TeeTimeSigneClassifySearchFragment.class, bundle);

                    }
                });
                upLayout.addView(upT);

            }


            this.addView(upLayout);
            this.addView(AppUtils.getSeparatorLine(context));

        }
        public ChooseLetterLayout(Context context) {
            super(context);
            setOrientation(VERTICAL);
            LinearLayout.LayoutParams upLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,getActualWidthOnThisDevice(100));
            upLayout = new LinearLayout(context);
            upLayout.setLayoutParams(upLayoutParams);
            upLayout.setOrientation(HORIZONTAL);
            int u = 0;
            for (String upStr:dataUp){
                LinearLayout.LayoutParams upTParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                upTParams.weight = 1;
                IteeTextView upT = new IteeTextView(context);
                upT.setText(upStr);
                upT.setLayoutParams(upTParams);
                upT.setGravity(Gravity.CENTER);
                upT.setTag(u + "");
                upT.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String index = String.valueOf(view.getTag());
                        if (Constants.STR_0.equals(index))
                            index = Constants.STR_EMPTY;
                        Bundle bundle = new Bundle();
                        bundle.putInt("fromAdd", fromAdd);
                        bundle.putString("signId", signId);
                        bundle.putInt("parentPosition", parentPosition);
                        bundle.putString("count", count);
                        bundle.putString(TransKey.TEE_TIME_INDEX_TYPE, index);
                        bundle.putString("nowSignType", signType);
                        bundle.putString(TransKey.BOOKING_DATE, bookingDate);
                        bundle.putString(TransKey.COURSE_AREA_ID, String.valueOf(courseAreaId));
                        bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeSignedByFragment.class.getName());
                        bundle.putString("addAgent", "1");
                        bundle.putString("notCancel", Constants.STR_1);
                        push(TeeTimeSigneClassifySearchFragment.class, bundle);
                    }
                });
                upLayout.addView(upT);
                upLayout.addView(AppUtils.getVerticalLine(context,1));
                u++;
            }

            LinearLayout.LayoutParams downLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,getActualWidthOnThisDevice(100));
            downLayout = new LinearLayout(context);
            downLayout.setLayoutParams(downLayoutParams);
            downLayout.setOrientation(HORIZONTAL);
            int i = 3;
            for (String downStr:dataDown){
                LinearLayout.LayoutParams upTParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                upTParams.weight = 1;
                IteeTextView downT = new IteeTextView(context);
                downT.setText(downStr);
                downT.setLayoutParams(upTParams);
                downT.setGravity(Gravity.CENTER);
                downT.setTag(i + "");

                downT.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!"3".equals(String.valueOf(view.getTag()))){
                            String index = String.valueOf(view.getTag());
                            if (Constants.STR_0.equals(index))
                                index = Constants.STR_EMPTY;
                            Bundle bundle = new Bundle();
                            bundle.putInt("fromAdd", fromAdd);
                            bundle.putString("signId", signId);
                            bundle.putInt("parentPosition", parentPosition);
                            bundle.putString(TransKey.TEE_TIME_INDEX_TYPE, index);
                            bundle.putString("count", count);
                            bundle.putString("nowSignType", signType);
                            bundle.putString(TransKey.BOOKING_DATE, bookingDate);
                            bundle.putString(TransKey.COURSE_AREA_ID, String.valueOf(courseAreaId));
                            bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeSignedByFragment.class.getName());
                            bundle.putString("addAgent", "1");
                            bundle.putString("notCancel", Constants.STR_1);
                            push(TeeTimeSigneClassifySearchFragment.class, bundle);

                        }



                    }
                });
                downLayout.addView(downT);
                downLayout.addView(AppUtils.getVerticalLine(context,1));
                i++;
            }



            this.addView(upLayout);
            this.addView(AppUtils.getSeparatorLine(context));
            this.addView(downLayout);
            this.addView(AppUtils.getSeparatorLine(context));
        }
    }



    class MemberTypeLayout extends LinearLayout{
        private Context  mContext;
        public MemberTypeLayout(Context context) {
            super(context);
            this.setOrientation(VERTICAL);
            mContext = context;
        }

        public  void refreshLayout(List<JsonGetGuestTypeList.GuestTypeItem> dataList){


            if (dataList !=null&&dataList.size()>0){

                for (int i = 0;i<( dataList.size()/2+dataList.size()%2);i++){

                    int leftPosition = i*2;
                    int rightPosition = (i*2)+1;
                    LinearLayout.LayoutParams lineLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,getActualWidthOnThisDevice(100));
                    LinearLayout lineLayout = new LinearLayout(mContext);
                    lineLayout.setLayoutParams(lineLayoutParams);
                    if (leftPosition<dataList.size()){
                        LinearLayout.LayoutParams leftTParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                        leftTParams.weight = 1;
                        final IteeTextView leftT = new IteeTextView(mContext);
                        leftT.setText(dataList.get(leftPosition).getName());
                        leftT.setLayoutParams(leftTParams);
                        leftT.setGravity(Gravity.CENTER);
                        leftT.setTag( dataList.get(leftPosition).getId());
                        leftT.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle bundle = new Bundle();
                                bundle.putString(TransKey.TEE_TIME_USER_TYPE, String.valueOf(view.getTag()));
                                bundle.putString(TransKey.TEE_TIME_USER_TYPE_NAME, leftT.getText().toString());
                                bundle.putInt("fromAdd", fromAdd);
                                bundle.putString("signId", signId);
                                bundle.putInt("parentPosition", parentPosition);
                                bundle.putString("count", count);
                                bundle.putString("nowSignType", Constants.STR_1);
                                bundle.putString(TransKey.BOOKING_DATE, bookingDate);
                                bundle.putString(TransKey.TEE_TIME_INDEX_TYPE, Constants.STR_EMPTY);
                                bundle.putString(TransKey.COURSE_AREA_ID, String.valueOf(courseAreaId));
                                bundle.putString(TransKey.COMMON_FROM_PAGE,TeeTimeSignedByFragment.class.getName());
                                bundle.putString("addAgent", "1");
                                bundle.putString("notCancel", Constants.STR_1);
                                push(TeeTimeSigneClassifySearchFragment.class, bundle);


                            }
                        });

                        lineLayout.addView(leftT);
                        lineLayout.addView(AppUtils.getVerticalLine(mContext, 1));
                    }




                    LinearLayout.LayoutParams rightTParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    rightTParams.weight = 1;
                    final IteeTextView rightT = new IteeTextView(mContext);
                    rightT.setLayoutParams(rightTParams);
                    rightT.setGravity(Gravity.CENTER);
                    if (rightPosition<dataList.size()) {
                        rightT.setTag(dataList.get(rightPosition).getId());
                        rightT.setText(dataList.get(rightPosition).getName());
                        rightT.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle bundle = new Bundle();



                                bundle.putString(TransKey.TEE_TIME_USER_TYPE, String.valueOf(view.getTag()));

                                bundle.putString(TransKey.TEE_TIME_USER_TYPE_NAME, rightT.getText().toString());
                                bundle.putInt("fromAdd", fromAdd);
                                bundle.putString("signId", signId);
                                bundle.putInt("parentPosition", parentPosition);
                                bundle.putString("count", count);
                                bundle.putString("nowSignType", Constants.STR_1);
                                bundle.putString(TransKey.BOOKING_DATE, bookingDate);

                                bundle.putString(TransKey.TEE_TIME_INDEX_TYPE, Constants.STR_EMPTY);
                                bundle.putString(TransKey.COURSE_AREA_ID, String.valueOf(courseAreaId));
                                bundle.putString(TransKey.COMMON_FROM_PAGE,TeeTimeSignedByFragment.class.getName());
                                bundle.putString("addAgent", "1");
                                bundle.putString("notCancel", Constants.STR_1);
                                push(TeeTimeSigneClassifySearchFragment.class, bundle);
                            }
                        });
                    }



                    lineLayout.addView(rightT);
                    lineLayout.addView(AppUtils.getVerticalLine(mContext, 1));

                    this.addView(lineLayout);
                    this.addView(AppUtils.getSeparatorLine(mContext));
                }
            }
        }
    }

    public void getGuestType() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        HttpManager<JsonGetGuestTypeList> hh = new HttpManager<JsonGetGuestTypeList>(TeeTimeSignedByFragment.this) {
            @Override
            public void onJsonSuccess(JsonGetGuestTypeList jo) {
                Integer returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();

                if (Constants.RETURN_CODE_20301 == returnCode) {
                    memberTypeLayout.refreshLayout(jo.getDataList());
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
        hh.startGet(getActivity(), ApiManager.HttpApi.SignInGuestType, params);
    }


    @Override
    protected void reShowWithBackValue() {
        super.reShowWithBackValue();
        Bundle bundle = getReturnValues();
        if (bundle != null) {
            try {
                boolean isBack = bundle.getBoolean("isBack",false);
                if (isBack)
                doBackWithReturnValue(bundle,(Class<? extends BaseFragment>) Class.forName(fromPage));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }
    }
}
