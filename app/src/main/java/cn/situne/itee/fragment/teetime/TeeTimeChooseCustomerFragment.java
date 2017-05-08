package cn.situne.itee.fragment.teetime;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.android.volley.NetworkResponse;
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
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.agents.AgentsAddEditFragment;
import cn.situne.itee.fragment.player.PlayerInfoEditFragment;
import cn.situne.itee.fragment.quick.SearchBookingFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonGetGuestTypeList;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.StickyLayout;

/**
 * Created by luochao on 11/11/15.
 */
public class TeeTimeChooseCustomerFragment extends BaseFragment {



    public  final  static  String INDEX_AD = "1";
    public  final  static  String INDEX_EI = "2";
    public  final  static  String INDEX_JM = "3";
    public  final  static  String INDEX_NS = "4";
    public  final  static  String INDEX_TW = "5";
    public  final  static  String INDEX_XZ = "6";


    public  final  static  String MEMBER_TYPE_MEMBER = "1";
    public  final  static  String MEMBER_TYPE_NON_MEMBER = "2";

    private StickyLayout stickyLayout;
    private ScrollView scrollView;
    private LinearLayout body;

    private IteeTextView etSearch;
    private List<JsonGetGuestTypeList.GuestTypeItem> dataList;
    private  MemberTypeLayout memberTypeLayout;

    private String fromPage;




    private String searchName = StringUtils.EMPTY;
    private int position, parentPosition;
    private String choiceId;
    private String courseAreaId;
    private String addMemberName;
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

        guestTypeList = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {

            searchName = bundle.getString("searchName", StringUtils.EMPTY);
            position = bundle.getInt("position");
            choiceId = bundle.getString("choiceId", StringUtils.EMPTY);
            parentPosition = bundle.getInt("parentPosition");
            courseAreaId = bundle.getString(TransKey.COURSE_AREA_ID, StringUtils.EMPTY);
            addMemberName = bundle.getString(TransKey.COURSE_ADD_MEMBER_NAME, StringUtils.EMPTY);
            fromPage= bundle.getString(TransKey.COMMON_FROM_PAGE, StringUtils.EMPTY);
        }

        stickyLayout = (StickyLayout) rootView.findViewById(R.id.sticky_layout);
        scrollView= (ScrollView) rootView.findViewById(R.id.scrollView);


        RelativeLayout stickyHeader = (RelativeLayout) rootView.findViewById(R.id.sticky_header);
        stickyHeader.getLayoutParams().height = getActualHeightOnThisDevice(100);
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
        chooseLetterMemLayout.setLayoutParams(chooseLetterLayoutParams);
        chooseLetterMemLayout.setMemberType(MEMBER_TYPE_MEMBER);
        body.addView(chooseLetterMemLayout);
        LinearLayout.LayoutParams memberTypeLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        memberTypeLayout = new MemberTypeLayout(getBaseActivity());
        memberTypeLayout.setLayoutParams(memberTypeLayoutParams);

        body.addView(memberTypeLayout);
        View v = AppUtils.getSeparatorLine(getBaseActivity());

        body.addView(v);
        v.getLayoutParams().height = getActualHeightOnThisDevice(50);
        v.setBackgroundColor(getColor(R.color.common_light_gray));


        LinearLayout.LayoutParams tvNonMemberParmas = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(100));
        IteeTextView tvNonMember = new IteeTextView(getBaseActivity());
        tvNonMember.setLayoutParams(tvNonMemberParmas);
        tvNonMember.setText(getString(R.string.customers_non_member));
        tvNonMember.setPadding(getActualWidthOnThisDevice(20), 0, 0, 0);
        body.addView(tvNonMember);
        body.addView(AppUtils.getSeparatorLine(getBaseActivity()));



        LinearLayout.LayoutParams chooseLetterNonMemLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        ChooseLetterLayout chooseLetterNonMemLayout = new ChooseLetterLayout(getBaseActivity());
        chooseLetterNonMemLayout.setLayoutParams(chooseLetterNonMemLayoutParams);
        chooseLetterNonMemLayout.setMemberType(MEMBER_TYPE_NON_MEMBER);

        body.addView(chooseLetterNonMemLayout);

        getGuestType();
  

    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        stickyLayout.setMaxHeaderHeight(100);
        //stickyLayout.setVisibility(View.VISIBLE);
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
                bundle.putString(TransKey.TEE_TIME_USER_TYPE, Constants.STR_EMPTY);
                bundle.putString(TransKey.TEE_TIME_MEMBER_TYPE, Constants.STR_EMPTY);
                bundle.putString(TransKey.COMMON_FROM_PAGE,TeeTimeChooseCustomerFragment.class.getName());
                bundle.putString(TransKey.TEE_TIME_INDEX_TYPE, Constants.STR_EMPTY);

                bundle.putString("nowSignType",Constants.STR_0);
                bundle.putString("searchName", searchName);
                bundle.putInt("position", position);
                bundle.putString("choiceId", choiceId);
                bundle.putInt("parentPosition", parentPosition);
                bundle.putString("courseAreaId", courseAreaId);
                bundle.putString(TransKey.COURSE_ADD_MEMBER_NAME, addMemberName);


                bundle.putString("fragmentSource", SearchBookingFragment.FRAGMENT_SOURCE_1);
                bundle.putString("fragmentType", SearchBookingFragment.FRAGMENT_TYPE_2);


                push(SearchBookingFragment.class,bundle);


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
        getTvLeftTitle().setText(getString(R.string.itee_choose_customer));



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
                    bundle.putString(TransKey.COMMON_FROM_PAGE,TeeTimeChooseCustomerFragment.class.getName());
                    push(PlayerInfoEditFragment.class, bundle);
                } else {
                    AppUtils.showHaveNoPermission(TeeTimeChooseCustomerFragment.this);
                }
            }
        });
    }
    class ChooseLetterLayout extends LinearLayout{
        private LinearLayout upLayout;
        private LinearLayout downLayout;
        private String[] dataUp = {"ALL","A-D","E-I","J-M"};
        private String[] dataDown = {"","N-S","T-W","X-Z"};

        private String memberType;

        public String getMemberType() {
            return memberType;
        }

        public void setMemberType(String memberType) {
            this.memberType = memberType;
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
                        Bundle bundle = new Bundle();
                        String index = String.valueOf(view.getTag());
                        if (Constants.STR_0.equals(index))
                            index = Constants.STR_EMPTY;
                        bundle.putString(TransKey.TEE_TIME_USER_TYPE, Constants.STR_EMPTY);
                        bundle.putString(TransKey.TEE_TIME_MEMBER_TYPE, memberType);
                        bundle.putString(TransKey.TEE_TIME_INDEX_TYPE, index);
                        bundle.putString(TransKey.COMMON_FROM_PAGE,TeeTimeChooseCustomerFragment.class.getName());

                        bundle.putString("nowSignType",memberType);
                        bundle.putString("searchName",searchName);
                        bundle.putInt("position", position);
                        bundle.putString("choiceId", choiceId);
                        bundle.putInt("parentPosition", parentPosition);
                        bundle.putString("courseAreaId", courseAreaId);
                        bundle.putString(TransKey.COURSE_ADD_MEMBER_NAME,addMemberName);


                        bundle.putString("notCancel", Constants.STR_1);



                        push(TeeTimeMemberClassifySearchFragment.class, bundle);
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

            int i  = 3;
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

                            Bundle bundle = new Bundle();
                            String index = String.valueOf(view.getTag());
                            if (Constants.STR_0.equals(index))
                                index = Constants.STR_EMPTY;
                            bundle.putString(TransKey.TEE_TIME_USER_TYPE, Constants.STR_EMPTY);
                            bundle.putString(TransKey.TEE_TIME_MEMBER_TYPE, memberType);
                            bundle.putString(TransKey.TEE_TIME_INDEX_TYPE, index);
                            bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeChooseCustomerFragment.class.getName());


                            bundle.putString("nowSignType", memberType);
                            bundle.putString("searchName", searchName);
                            bundle.putInt("position", position);
                            bundle.putString("choiceId", choiceId);
                            bundle.putInt("parentPosition", parentPosition);
                            bundle.putString("courseAreaId", courseAreaId);
                            bundle.putString(TransKey.COURSE_ADD_MEMBER_NAME, addMemberName);
                            bundle.putString("notCancel", Constants.STR_1);
                            push(TeeTimeMemberClassifySearchFragment.class, bundle);
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


    private List<MemberTypeItem> guestTypeList;
    class MemberTypeItem{
        private JsonGetGuestTypeList.GuestTypeItem left;
        private JsonGetGuestTypeList.GuestTypeItem right;

        public JsonGetGuestTypeList.GuestTypeItem getLeft() {
            return left;
        }

        public void setLeft(JsonGetGuestTypeList.GuestTypeItem left) {
            this.left = left;
        }

        public JsonGetGuestTypeList.GuestTypeItem getRight() {
            return right;
        }

        public void setRight(JsonGetGuestTypeList.GuestTypeItem right) {
            this.right = right;
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

                                bundle.putString(TransKey.TEE_TIME_MEMBER_TYPE, Constants.STR_1);
                                bundle.putString(TransKey.TEE_TIME_INDEX_TYPE, Constants.STR_EMPTY);
                                bundle.putString(TransKey.COMMON_FROM_PAGE,TeeTimeChooseCustomerFragment.class.getName());
                                bundle.putString("nowSignType",Constants.STR_1);
                                bundle.putString("searchName",searchName);
                                bundle.putInt("position", position);
                                bundle.putString("choiceId", choiceId);
                                bundle.putInt("parentPosition", parentPosition);
                                bundle.putString("courseAreaId", courseAreaId);
                                bundle.putString(TransKey.COURSE_ADD_MEMBER_NAME,addMemberName);


                                bundle.putString("notCancel", Constants.STR_1);
                                push(TeeTimeMemberClassifySearchFragment.class, bundle);

                            }
                        });

                        lineLayout.addView(leftT);
                        lineLayout.addView(AppUtils.getVerticalLine(mContext, 1));
                    }

                    LinearLayout.LayoutParams rightTParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    rightTParams.weight = 1;
                    final IteeTextView rightT = new IteeTextView(mContext);
                    if (rightPosition<dataList.size()) {
                        rightT.setText(dataList.get(rightPosition).getName());

                        rightT.setTag(dataList.get(rightPosition).getId());
                        rightT.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle bundle = new Bundle();
                                bundle.putString(TransKey.TEE_TIME_USER_TYPE, String.valueOf(view.getTag()));
                                bundle.putString(TransKey.TEE_TIME_USER_TYPE_NAME, rightT.getText().toString());
                                bundle.putString(TransKey.TEE_TIME_MEMBER_TYPE, Constants.STR_1);
                                bundle.putString(TransKey.TEE_TIME_INDEX_TYPE, Constants.STR_EMPTY);
                                bundle.putString(TransKey.COMMON_FROM_PAGE,TeeTimeChooseCustomerFragment.class.getName());
                                bundle.putString("nowSignType",Constants.STR_1);
                                bundle.putString("searchName",searchName);
                                bundle.putInt("position", position);
                                bundle.putString("choiceId", choiceId);
                                bundle.putInt("parentPosition", parentPosition);
                                bundle.putString("courseAreaId", courseAreaId);
                                bundle.putString(TransKey.COURSE_ADD_MEMBER_NAME,addMemberName);

                                bundle.putString("notCancel", Constants.STR_1);

                                push(TeeTimeMemberClassifySearchFragment.class, bundle);
                            }
                        });
                    }
                    rightT.setLayoutParams(rightTParams);
                    rightT.setGravity(Gravity.CENTER);



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
        HttpManager<JsonGetGuestTypeList> hh = new HttpManager<JsonGetGuestTypeList>(TeeTimeChooseCustomerFragment.this) {
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
        hh.startGet(getActivity(), ApiManager.HttpApi.CsBookingGuestType, params);
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
