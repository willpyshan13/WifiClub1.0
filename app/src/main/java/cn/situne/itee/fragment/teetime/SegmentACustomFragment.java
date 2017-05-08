package cn.situne.itee.fragment.teetime;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.mining.app.zxing.decoding.CaptureActivityHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
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
import cn.situne.itee.fragment.administration.ChooseDateFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonSegmentSettingDataGet;
import cn.situne.itee.manager.jsonentity.JsonTeeTimeCalendar;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.IteeYesAndOnPopupWindow;

/**
 * Created by luochao on 12/10/15.
 */
public class SegmentACustomFragment extends BaseEditFragment {

    private int showIndex;
    private int show9Index;

    private List<JsonSegmentSettingDataGet.PageData> pageDataList;
    private RelativeLayout body ;
    private BottomLayout bottomLayout;
    private ViewPager viewPager;
    private String chooseDates;
    private String firstTime;
    private String lastTime;
    private String gap;
    //  private String typeFlag;
    private String weekdayPace;
    private String holidayPace;
    private PagerItem pagerItem;

    private String clickBtnStatus;

    private List<PagerItem>pageViewList;

    public final String  START_STATUS ="1";
    public final String  TRANSFER_STATUS = "2";

    private String sunriseTime;
    private String sunsetTime;
    private String fromPage;
    private String edit;

    private IteeYesAndOnPopupWindow     noDataPopup;





    @Override
    protected int getFragmentId() {
        return R.layout.fragment_tee_time_limit;
    }
    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }
    @Override
    protected void initControls(View rootView) {
        showIndex = 1;
        show9Index = 1;
        clickBtnStatus = START_STATUS;
        pageDataList  =new ArrayList<>();
        pageViewList =new ArrayList<>();
        Bundle bundle = getArguments();

        if (bundle!=null){

            chooseDates = bundle.getString("chooseDates", Constants.STR_EMPTY);
            firstTime = bundle.getString("firstTime", Constants.STR_EMPTY);
            lastTime = bundle.getString("lastTime", Constants.STR_EMPTY);
            gap = bundle.getString("gap", Constants.STR_EMPTY);
//            typeFlag = bundle.getString("typeFlag", Constants.STR_EMPTY);
            weekdayPace = bundle.getString("weekdayPace", Constants.STR_EMPTY);
            holidayPace = bundle.getString("holidayPace", Constants.STR_EMPTY);
            sunriseTime = bundle.getString("sunriseTime", Constants.STR_EMPTY);
            sunsetTime = bundle.getString("sunsetTime", Constants.STR_EMPTY);
            edit= bundle.getString("edit", Constants.STR_EMPTY);
            fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE, Constants.STR_EMPTY);
        }


        noDataPopup = new IteeYesAndOnPopupWindow(getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if ("2".equals(noDataLayout.getType())){

                    saveTeeTime();


                }else{
                    Bundle bundle = new Bundle();
                    bundle.putString(TransKey.COMMON_FROM_PAGE, SegmentACustomFragment.class.getName());
                    bundle.putString("chooseDates", chooseDates);
                    bundle.putString("firstTime", firstTime);
                    bundle.putString("lastTime", lastTime);
                    bundle.putString("gap", gap);
                    bundle.putString("weekdayPace", weekdayPace);
                    bundle.putString("holidayPace", holidayPace);
                    bundle.putString("typeFlag", Constants.CUSTOM_FLAG);
                    bundle.putString("sunriseTime", sunriseTime);
                    bundle.putString("sunsetTime", sunsetTime);
                    bundle.putStringArrayList("jsonData", getJsonData(noDataLayout.getType()));
                    push(TeeTimeLimitFragment.class, bundle);
                }





            }
        }, SegmentACustomFragment.this);






        noDataLayout = new NoDataLayout(getActivity());
        noDataPopup.setView(noDataLayout);


        body  = (RelativeLayout)rootView.findViewById(R.id.body);
        bottomLayout  = new BottomLayout(getBaseActivity(), new BottomClickListener() {
            @Override
            public boolean clickStart() {
                return pagerItem.clickStart(1);
            }
            @Override
            public boolean clickTransfer() {



                return  pagerItem.clickStart(2);



            }



            @Override
            public void clickHoles9() {
                pagerItem.set9Holes();
            }

            @Override
            public boolean clearAll() {
                pagerItem.clearAll();
                return false;
            }

            @Override
            public boolean clear() {
                pagerItem.clearData();

                if (Constants.CUSTOM_FLAG_START.equals(pagerItem.getClearType())){

                    return true;
                }

                return false;
            }
        });
        bottomLayout.setId(View.generateViewId());
        RelativeLayout.LayoutParams viewPagerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        viewPagerParams.addRule(RelativeLayout.ABOVE,bottomLayout.getId());
        viewPager = new ViewPager(getBaseActivity());
        viewPager.setLayoutParams(viewPagerParams);

        body.addView(viewPager);
        body.addView(bottomLayout);

        getTeeTimeFormatData();
    }
    private  NoDataLayout noDataLayout;
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


    class NoDataLayout extends LinearLayout{


        private IteeTextView titleText;

        private IteeTextView upText;
        private IteeTextView downText;

        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public NoDataLayout(Context context) {
            super(context);
            this.setOrientation(VERTICAL);
            titleText = new IteeTextView(context);
            upText = new IteeTextView(context);
            downText = new IteeTextView(context);
            titleText.setText(getString(R.string.segment_custom_mes_1));
            upText.setText(getString(R.string.segment_custom_mes_2));
            downText.setText(getString(R.string.segment_custom_mes_3));


            titleText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            titleText.setPadding(getActualWidthOnThisDevice(20), 0, 0, 0);

            upText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            upText.setPadding(getActualWidthOnThisDevice(40), 0, 0, 0);
            downText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            downText.setPadding(getActualWidthOnThisDevice(40), 0, 0, 0);
            upText.setTextColor(getColor(R.color.common_blue));
            downText.setTextColor(getColor(R.color.common_blue));
            upText.setBackgroundColor(getColor(R.color.bg_blue_of_1));
            type ="1";
            upText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    type ="1";
                    upText.setBackgroundColor(getColor(R.color.bg_blue_of_1));
                    downText.setBackgroundColor(getColor(R.color.common_white));
                }
            });


            downText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    type ="2";
                    upText.setBackgroundColor(getColor(R.color.common_white));
                    downText.setBackgroundColor(getColor(R.color.bg_blue_of_1));
                }
            });

            this.addView(titleText);
            this.addView(getBlueLine());
            this.addView(upText);
            this.addView(getBlueLine());
            this.addView(downText);

            titleText.getLayoutParams().height = getActualWidthOnThisDevice(100);
            upText.getLayoutParams().height = getActualWidthOnThisDevice(100);
            downText.getLayoutParams().height = getActualWidthOnThisDevice(100);
        }
    }

    private  View getBlueLine(){

        View v = AppUtils.getSeparatorLine(getBaseActivity());

        v.setBackgroundColor(getColor(R.color.common_blue));
        return v;
    }
    private boolean checkPush(){

        for (JsonSegmentSettingDataGet.PageData pageData:pageDataList){
            for (JsonSegmentSettingDataGet.LineItemData lineItemData :pageData.getDataList()){
                if (Utils.isStringNotNullOrEmpty(lineItemData.getRightData().getNo()))return true;
                if (Utils.isStringNotNullOrEmpty(lineItemData.getLeftData().getNo()))return true;
            }
        }

        return false;
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getString(R.string.tag_custom));
        getTvRight().setBackground(null);
        getTvRight().setText(R.string.common_next);
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPush()){
                    Bundle bundle = new Bundle();
                    bundle.putString(TransKey.COMMON_FROM_PAGE, SegmentACustomFragment.class.getName());
                    bundle.putString("chooseDates", chooseDates);
                    bundle.putString("firstTime", firstTime);
                    bundle.putString("lastTime", lastTime);
                    bundle.putString("gap", gap);
                    bundle.putString("weekdayPace", weekdayPace);
                    bundle.putString("holidayPace", holidayPace);
                    bundle.putString("typeFlag", Constants.CUSTOM_FLAG);
                    bundle.putString("sunriseTime", sunriseTime);
                    bundle.putString("sunsetTime", sunsetTime);
                    bundle.putStringArrayList("jsonData", getJsonData());
                    push(TeeTimeLimitFragment.class, bundle);


                }else{

                    noDataPopup.showAtLocation(getRootView().findViewById(R.id.rl_content_container),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }






            }
        });
    }


    private ArrayList<String> getJsonData(String type){
        String flag = "3";
        String no ="1";
        if ("2".equals(type)){
            flag = "";
            no ="";
        }

        ArrayList<String> resList = new ArrayList<>();

        for (PagerItem pagerItem:pageViewList){
            JsonSegmentSettingDataGet.PageData d = new JsonSegmentSettingDataGet.PageData();
            d.setLeftName(pagerItem.getmPageData().getLeftName());
            d.setLeftId(pagerItem.getmPageData().getLeftId());
            d.setRightName(pagerItem.getmPageData().getRightName());
            d.setRightId(pagerItem.getmPageData().getRightId());
            List<JsonSegmentSettingDataGet.LineItemData> lineDataList = new ArrayList<>();

            for (JsonSegmentSettingDataGet.LineItemData lineItemData:pagerItem.getmPageData().getDataList()){
                JsonSegmentSettingDataGet.LineItemData newData = new JsonSegmentSettingDataGet.LineItemData();
                newData.setTime(lineItemData.getTime());
                JsonSegmentSettingDataGet.ItemData lData = new JsonSegmentSettingDataGet.ItemData();
                lData.setFlag(flag);
                lData.setIndex9("");
                lData.setAllReserveMember("");
                lData.setPrimeFlag("");
                lData.setFrequenter("");

                lData.setClickEnabled(true);
                lData.setNo(no);
                lData.setStatus("");
                lData.setVal(lineItemData.getLeftData().getVal());

                JsonSegmentSettingDataGet.ItemData rData = new JsonSegmentSettingDataGet.ItemData();
                rData.setFlag(flag);
                rData.setIndex9("1");
                rData.setAllReserveMember("");
                rData.setPrimeFlag("");
                rData.setFrequenter("");
                rData.setNo(no);
                rData.setStatus("");
                rData.setVal(lineItemData.getRightData().getVal());

                rData.setClickEnabled(true);


                newData.setLeftData(lData);
                newData.setRightData(rData);
                newData.setCaId(lineItemData.getCaId());
                newData.setCaName(lineItemData.getCaName());

                lineDataList.add(newData);
            }

            d.setDataList(lineDataList);

            resList.add(Utils.getStringFromObject(d));
        }


        return resList;
    }

    private ArrayList<String> getJsonData(){

        ArrayList<String> resList = new ArrayList<>();

        for (PagerItem pagerItem:pageViewList){
            JsonSegmentSettingDataGet.PageData d = new JsonSegmentSettingDataGet.PageData();
            d.setLeftName(pagerItem.getmPageData().getLeftName());
            d.setLeftId(pagerItem.getmPageData().getLeftId());
            d.setRightName(pagerItem.getmPageData().getRightName());
            d.setRightId(pagerItem.getmPageData().getRightId());
            List<JsonSegmentSettingDataGet.LineItemData> lineDataList = new ArrayList<>();

            for (JsonSegmentSettingDataGet.LineItemData lineItemData:pagerItem.getmPageData().getDataList()){
                JsonSegmentSettingDataGet.LineItemData newData = new JsonSegmentSettingDataGet.LineItemData();
                newData.setTime(lineItemData.getTime());
                JsonSegmentSettingDataGet.ItemData lData = new JsonSegmentSettingDataGet.ItemData();
                lData.setFlag(lineItemData.getLeftData().getFlag());
                lData.setIndex9(lineItemData.getLeftData().getIndex9());
                lData.setAllReserveMember(lineItemData.getLeftData().getAllReserveMember());
                lData.setPrimeFlag(lineItemData.getLeftData().getPrimeFlag());
                lData.setFrequenter(lineItemData.getLeftData().getFrequenter());
                lData.setClickEnabled(lineItemData.getLeftData().isClickEnabled());
                lData.setNo(lineItemData.getLeftData().getNo());
                lData.setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
                lData.setVal(lineItemData.getLeftData().getVal());

                JsonSegmentSettingDataGet.ItemData rData = new JsonSegmentSettingDataGet.ItemData();
                rData.setFlag(lineItemData.getRightData().getFlag());
                rData.setIndex9(lineItemData.getRightData().getIndex9());
                rData.setAllReserveMember(lineItemData.getRightData().getAllReserveMember());
                rData.setPrimeFlag(lineItemData.getRightData().getPrimeFlag());
                rData.setFrequenter(lineItemData.getRightData().getFrequenter());
                rData.setClickEnabled(lineItemData.getRightData().isClickEnabled());
                rData.setNo(lineItemData.getRightData().getNo());
                rData.setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
                rData.setVal(lineItemData.getRightData().getVal());

                newData.setLeftData(lData);
                newData.setRightData(rData);
                newData.setCaId(lineItemData.getCaId());
                newData.setCaName(lineItemData.getCaName());

                lineDataList.add(newData);
            }

            d.setDataList(lineDataList);

            resList.add(Utils.getStringFromObject(d));
        }


        return resList;
    }
    class BottomLayout extends LinearLayout{

        private IteeTextView startBtn;
        private IteeTextView hole9Btn;
        private IteeTextView clearBtn;

        private IteeYesAndOnPopupWindow     yesOrNoPopup;

//        private boolean startE;
//        private boolean transferE;

        public IteeTextView getStartBtn() {
            return startBtn;
        }

        public void setStartBtn(IteeTextView startBtn) {
            this.startBtn = startBtn;
        }

        public IteeTextView getTransferBtn() {
            return hole9Btn;
        }

        public void setTransferBtn(IteeTextView hole9Btn) {
            this.hole9Btn = hole9Btn;
        }

        public IteeTextView getClearBtn() {
            return clearBtn;
        }

        public void setClearBtn(IteeTextView clearBtn) {
            this.clearBtn = clearBtn;
        }

        private BottomClickListener bottomClickListener;

        public BottomLayout(Context context, final BottomClickListener bottomClickListener) {
            super(context);
            this.setOrientation(VERTICAL);
            RelativeLayout.LayoutParams myParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(120));
            myParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            this.setLayoutParams(myParams);
            this.setBackgroundColor(getColor(R.color.common_white));

            this.bottomClickListener = bottomClickListener;
            View line = AppUtils.getSeparatorLine(getContext());
            line.setBackgroundColor(getColor(R.color.common_blue));
            this.addView(line);
            line.getLayoutParams().height = getActualWidthOnThisDevice(6);


            LinearLayout downLayout = new LinearLayout(getContext());
            downLayout.setGravity(Gravity.CENTER);
            this.addView(downLayout);
            downLayout.setOrientation(HORIZONTAL);

            LinearLayout.LayoutParams r1Params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
            LinearLayout.LayoutParams r2Params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
            LinearLayout.LayoutParams r3Params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
            RelativeLayout.LayoutParams startBtnParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
            RelativeLayout.LayoutParams transferBtnParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
            RelativeLayout.LayoutParams clearBtnParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
            clearBtnParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            startBtnParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            transferBtnParams.addRule(RelativeLayout.CENTER_IN_PARENT);

            RelativeLayout r1 = new RelativeLayout(getContext());
            RelativeLayout r2 = new RelativeLayout(getContext());
            RelativeLayout r3 = new RelativeLayout(getContext());
            r1.setPadding(getActualWidthOnThisDevice(10),getActualHeightOnThisDevice(13),getActualWidthOnThisDevice(10),getActualHeightOnThisDevice(15));
            r2.setPadding(getActualWidthOnThisDevice(10),getActualHeightOnThisDevice(13),getActualWidthOnThisDevice(10),getActualHeightOnThisDevice(13));
            r3.setPadding(getActualWidthOnThisDevice(10),getActualHeightOnThisDevice(13),getActualWidthOnThisDevice(10),getActualHeightOnThisDevice(13));
            r1Params.weight  = 1;
            r2Params.weight  = 1;
            r3Params.weight  = 1;
            startBtn = new IteeTextView(context);
            startBtn.setLayoutParams(startBtnParams);
            startBtn.setTextSize(Constants.FONT_SIZE_LARGER);
            startBtn.setTextColor(getColor(R.color.common_white));
            r1.setLayoutParams(r1Params);



            yesOrNoPopup = new IteeYesAndOnPopupWindow(getActivity(), new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (bottomClickListener.clickTransfer()){

                        startBtn.setText(getString(R.string.custom_start));
                    }

                    startBtn.setBackgroundResource(R.drawable.bg_gray_btn);
                    startBtn.setEnabled(false);
                    hole9Btn.setBackgroundResource(R.drawable.bg_gray_btn);
                    hole9Btn.setEnabled(false);

                }
            }, SegmentACustomFragment.this);
            yesOrNoPopup.getOkBtn().setText(getString(R.string.common_continue));




            //startE  =true;
            startBtn.setText(getString(R.string.custom_start));
            startBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (getString(R.string.custom_start).equals(startBtn.getText().toString())) {
                        if (bottomClickListener.clickStart())
                            startBtn.setText(getString(R.string.custom_transfer));

                        startBtn.setBackgroundResource(R.drawable.bg_gray_btn);
                        startBtn.setEnabled(false);
                        hole9Btn.setBackgroundResource(R.drawable.bg_gray_btn);
                        hole9Btn.setEnabled(false);
                    } else if (getString(R.string.custom_transfer).equals(startBtn.getText().toString())) {

                        if (pagerItem.checkTr()){
                            String mes = pagerItem.checkNum();

                            if (Utils.isStringNotNullOrEmpty(mes)){
                                yesOrNoPopup.setMessage(mes);

                                yesOrNoPopup.showAtLocation(getRootView().findViewById(R.id.rl_content_container),
                                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                            }else{


                                if (bottomClickListener.clickTransfer())
                                    startBtn.setText(getString(R.string.custom_start));

                                startBtn.setBackgroundResource(R.drawable.bg_gray_btn);
                                startBtn.setEnabled(false);
                                hole9Btn.setBackgroundResource(R.drawable.bg_gray_btn);
                                hole9Btn.setEnabled(false);
                            }


                        }else{

                            Utils.showShortToast(getContext(),getString(R.string.segment_custom_mes_4));
                        }






                    }

                    hole9Btn.setBackgroundResource(R.drawable.bg_gray_btn);
                    hole9Btn.setEnabled(false);
                }
            });
            startBtn.setBackgroundResource(R.drawable.bg_gray_btn);
            startBtn.setEnabled(false);
            startBtn.setGravity(Gravity.CENTER);


            hole9Btn = new IteeTextView(context);

            hole9Btn.setLayoutParams(transferBtnParams);
            hole9Btn.setTextSize(Constants.FONT_SIZE_LARGER);
            r2.setLayoutParams(r2Params);
            hole9Btn.setText(getString(R.string.custom_9_holes));
            hole9Btn.setGravity(Gravity.CENTER);
            hole9Btn.setBackgroundResource(R.drawable.bg_gray_btn);

            hole9Btn.setEnabled(false);
            hole9Btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {


                    bottomClickListener.clickHoles9();

                    startBtn.setBackgroundResource(R.drawable.bg_gray_btn);
                    startBtn.setEnabled(false);
                    hole9Btn.setBackgroundResource(R.drawable.bg_gray_btn);
                    hole9Btn.setEnabled(false);
                }
            });
            hole9Btn.setTextColor(getColor(R.color.common_white));

            // transferE = false;
            clearBtn = new IteeTextView(context);
            clearBtn.setLayoutParams(clearBtnParams);

            r3.setLayoutParams(r3Params);
            clearBtn.setText(getString(R.string.custom_clear_all));
            clearBtn.setGravity(Gravity.CENTER);
            clearBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getString(R.string.custom_clear_all).equals(clearBtn.getText().toString())) {
                        bottomClickListener.clearAll();
                        startBtn.setText(getString(R.string.custom_start));
                        startBtn.setBackgroundResource(R.drawable.bg_gray_btn);
                        startBtn.setEnabled(false);
                        hole9Btn.setBackgroundResource(R.drawable.bg_gray_btn);
                        hole9Btn.setEnabled(false);

                    } else {

                        bottomClickListener.clear();
                        startBtn.setText(getString(R.string.custom_start));

                        startBtn.setBackgroundResource(R.drawable.bg_gray_btn);
                        startBtn.setEnabled(false);
                        hole9Btn.setBackgroundResource(R.drawable.bg_gray_btn);
                        hole9Btn.setEnabled(false);


                    }
                    clearBtn.setText(getString(R.string.custom_clear_all));
                    // bottomClickListener.clickHoles9();
                }
            });
            clearBtn.setTextColor(getColor(R.color.common_white));
            clearBtn.setBackgroundResource(R.drawable.bg_green_btn);

            clearBtn.setTextSize(Constants.FONT_SIZE_LARGER);
            r1.addView(startBtn);

            r2.addView(hole9Btn);
            r3.addView(clearBtn);


            startBtn.setTextSize(Constants.FONT_SIZE_SMALLER);
            hole9Btn.setTextSize(Constants.FONT_SIZE_SMALLER);
            clearBtn.setTextSize(Constants.FONT_SIZE_SMALLER);

            downLayout.addView(r1);

            downLayout.addView(r2);
            downLayout.addView(r3);
        }
    }



    public   class PagerItem extends LinearLayout  {



        private String clearType;
        public String getClearType() {
            return clearType;
        }
        public void setClearType(String clearType) {
            this.clearType = clearType;
        }
        private List<JsonSegmentSettingDataGet.PageData> mPageDataList;
        public  void setPageListData(List<JsonSegmentSettingDataGet.PageData>  data){
            mPageDataList = data;
        }
        public static final String PAGE_ITEM_START_SELECT = "2";
        public static final String PAGE_ITEM_SELECTED = "3";
        public static final String PAGE_ITEM_NORMAL = "1";
        private String pageStatus;
        private int leftStartIndex;

        private int rightStartIndex;

        private JsonSegmentSettingDataGet.PageData mPageData;

        public JsonSegmentSettingDataGet.PageData getmPageData() {
            return mPageData;
        }

        public void setmPageData(JsonSegmentSettingDataGet.PageData mPageData) {
            this.mPageData = mPageData;
        }
        // private String status;

        private ListView body;
        private Context mContext;
        private ListViewAdapter listViewAdapter;

        public ListViewAdapter getListViewAdapter() {
            return listViewAdapter;
        }

        public void setListViewAdapter(ListViewAdapter listViewAdapter) {
            this.listViewAdapter = listViewAdapter;
        }

        public PagerItem(Context context,JsonSegmentSettingDataGet.PageData pageData) {
            super(context);

            pageStatus = PAGE_ITEM_NORMAL;

            leftStartIndex = 0;
            this.setOrientation(VERTICAL);
            this.mPageData = pageData;
            mContext = context;
            LinearLayout.LayoutParams bodyParams = new LinearLayout.LayoutParams(LayoutParams .MATCH_PARENT, LayoutParams .MATCH_PARENT);
            body = new ListView(context);
            body.setDivider(null);
            body.setDividerHeight(0);

            body.setLayoutParams(bodyParams);
            listViewAdapter = new ListViewAdapter();
            body.setAdapter(listViewAdapter);
            ListViewItem   titleLayout = new ListViewItem(mContext,null,getActualHeightOnThisDevice(60));

            titleLayout.setTitle(pageData.getLeftName(),pageData.getRightName());
            this.addView(titleLayout);
            this.addView(body);
        }

        public boolean checkTr(){
            int b = 0;
            for (JsonSegmentSettingDataGet.LineItemData data:mPageData.getDataList()){
                if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(data.getLeftData().getStatus())){

                    b = 1;
                    break;
                }
                if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(data.getRightData().getStatus())){
                    b = 2;
                    break;
                }
            }
            for (JsonSegmentSettingDataGet.LineItemData data:mPageData.getDataList()){

                if (b == 1){
                    if ((showIndex+"").equals(data.getLeftData().getNo())){
                        return false;
                    }

                }else{
                    if ((showIndex+"").equals(data.getRightData().getNo())){
                        return false;
                    }
                }
            }



            return true;
        }



        public   String checkNum(){

            int k = 0;
            int s = 0;
            for (JsonSegmentSettingDataGet.LineItemData data:mPageData.getDataList()){

                if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(data.getLeftData().getStatus())){

                    s++;
                }


                if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(data.getRightData().getStatus())){
                    s++;

                }

                if ((showIndex+"").equals(data.getLeftData().getNo())){
                    k++;
                }


                if ((showIndex+"").equals(data.getRightData().getNo())) {
                    k++;
                }

            }

            if (k >s){
                return getString(R.string.transfer_s);
            }else if(k<s){
                return getString(R.string.transfer_b);

            }else{

                return null;
            }


        }
        public boolean clickStart(int type){//1 start 2 tr

//            if (type == 2){
//                if (!checkTr()){
//                    Utils.showShortToast(getContext(),getString(R.string.segment_custom_mes_4));
//                    return false;
//                }
//            }

            boolean res  =false;
            for (JsonSegmentSettingDataGet.PageData pageData:mPageDataList){
                for (JsonSegmentSettingDataGet.LineItemData lineItemData:pageData.getDataList()){

                    if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(lineItemData.getLeftData().getStatus())){
                        res = true;
                        lineItemData.getLeftData().setNo(showIndex + "");
                        lineItemData.getLeftData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
                        lineItemData.getLeftData().setFlag(""+type);
                    }
                    if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(lineItemData.getRightData().getStatus())){
                        res = true;
                        lineItemData.getRightData().setNo(showIndex + "");
                        lineItemData.getRightData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
                        lineItemData.getRightData().setFlag("" + type);
                    }
                }

            }

            if (res && type == 2){
                showIndex++;
            }
            listViewAdapter.notifyDataSetChanged();
            return res;
        }



        public void clearData(){

            for (JsonSegmentSettingDataGet.PageData pageData:mPageDataList){
                for (JsonSegmentSettingDataGet.LineItemData lineItemData :pageData.getDataList()){
                    if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(lineItemData.getLeftData().getStatus())){
                        lineItemData.getLeftData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
                        lineItemData.getLeftData().setNo("");
                        lineItemData.getLeftData().setFrequenter("");
                        lineItemData.getLeftData().setPrimeFlag("");
                        lineItemData.getLeftData().setFlag("");
                        lineItemData.getLeftData().setAllReserveMember("");
                    }
                    if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(lineItemData.getRightData().getStatus())) {
                        lineItemData.getRightData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
                        lineItemData.getRightData().setNo("");
                        lineItemData.getRightData().setFrequenter("");
                        lineItemData.getRightData().setPrimeFlag("");
                        lineItemData.getRightData().setFlag("");
                        lineItemData.getRightData().setAllReserveMember("");

                    }

                }
            }
            listViewAdapter.notifyDataSetChanged();
        }


        private List<JsonSegmentSettingDataGet.ItemData> typeItemList;
        public void clearAll(){

            show9Index =1;
            showIndex = 1;

            for (JsonSegmentSettingDataGet.PageData pageData:mPageDataList){
                for (JsonSegmentSettingDataGet.LineItemData lineItemData:pageData.getDataList()){
                    lineItemData.getLeftData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
                    lineItemData.getLeftData().setNo("");
                    lineItemData.getLeftData().setFrequenter("");
                    lineItemData.getLeftData().setPrimeFlag("");
                    lineItemData.getLeftData().setFlag("");
                    lineItemData.getLeftData().setAllReserveMember("");


                    lineItemData.getRightData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
                    lineItemData.getRightData().setNo("");
                    lineItemData.getRightData().setFrequenter("");
                    lineItemData.getRightData().setPrimeFlag("");
                    lineItemData.getRightData().setFlag("");
                    lineItemData.getRightData().setAllReserveMember("");
                }

            }

            listViewAdapter.notifyDataSetChanged();
        }
//        public void clearAll(){
//            if (typeItemList  == null){
//                typeItemList = new ArrayList<>();
//            }
//            typeItemList.clear();
//            for (JsonSegmentSettingDataGet.LineItemData line :mPageData.getDataList()){
//                if ("1".equals(line.getLeftData().getFlag())){
//                    typeItemList.add(line.getLeftData());
//                }
//                if ("1".equals(line.getRightData().getFlag())){
//                    typeItemList.add(line.getRightData());
//
//                }
//
//                if ("2".equals(line.getLeftData().getFlag())){
//                    typeItemList.add(line.getLeftData());
//                }
//                if ("2".equals(line.getRightData().getFlag())){
//                    typeItemList.add(line.getRightData());
//
//                }
//
//            }
//
//            for (JsonSegmentSettingDataGet.PageData pageData:mPageDataList){
//                for (JsonSegmentSettingDataGet.LineItemData lineItemData :pageData.getDataList()){
//                    clearOtherPage(lineItemData.getLeftData());
//                    clearOtherPage(lineItemData.getRightData());
//                }
//            }
//
//            for (JsonSegmentSettingDataGet.LineItemData line :mPageData.getDataList()){
//                line.getLeftData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
//                line.getLeftData().setNo("");
//                line.getLeftData().setFrequenter("");
//                line.getLeftData().setPrimeFlag("");
//                line.getLeftData().setFlag("");
//                line.getLeftData().setAllReserveMember("");
//
//
//                line.getRightData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
//                line.getRightData().setNo("");
//                line.getRightData().setFrequenter("");
//                line.getRightData().setPrimeFlag("");
//                line.getRightData().setFlag("");
//                line.getRightData().setAllReserveMember("");
//            }
//
//            listViewAdapter.notifyDataSetChanged();
//        }


        private void clearOtherPage(JsonSegmentSettingDataGet.ItemData selectItem){
            for (JsonSegmentSettingDataGet.ItemData item :typeItemList){

                if (item.getNo().equals(selectItem.getNo())){
                    selectItem.setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
                    selectItem.setNo("");
                    selectItem.setFrequenter("");
                    selectItem.setPrimeFlag("");
                    selectItem.setFlag("");
                    selectItem.setAllReserveMember("");
                }


            }

        }



        public void set9Holes(){
            for (JsonSegmentSettingDataGet.PageData pageData:mPageDataList){
                for (JsonSegmentSettingDataGet.LineItemData lineItemData:pageData.getDataList()){

                    if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(lineItemData.getLeftData().getStatus())){

                        lineItemData.getLeftData().setNo(show9Index + "");
                        lineItemData.getLeftData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
                        lineItemData.getLeftData().setIndex9(show9Index + "");
                        lineItemData.getLeftData().setFlag(Constants.CUSTOM_FLAG_9_HLLES);
                    }
                    if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(lineItemData.getRightData().getStatus())){
                        lineItemData.getRightData().setNo(show9Index + "");
                        lineItemData.getRightData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
                        lineItemData.getRightData().setIndex9(show9Index+"");
                        lineItemData.getRightData().setFlag(Constants.CUSTOM_FLAG_9_HLLES);
                    }
                }

            }

            show9Index++;


            listViewAdapter.notifyDataSetChanged();
        }



        class ListViewAdapter extends BaseAdapter {
            @Override
            public int getCount() {
                if (mPageData.getDataList() == null)
                    return 0;

                return mPageData.getDataList().size();
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
                ListViewItem item = null;
                if (view == null){

                    item = new ListViewItem(mContext,mPageData,getActualHeightOnThisDevice(80));
                }else{
                    item = (ListViewItem)view;

                }
                item.setListViewItemClickListener(new ListViewItemClickListener() {

                    @Override
                    public void clickRight(ListViewItem item) {

                        item.clickItem("right", pageStatus, rightStartIndex);
                        rightStartIndex = item.getIndex();
                        if ( pageStatus == PAGE_ITEM_NORMAL){
                            pageStatus  = PAGE_ITEM_START_SELECT;

                        }else{
                            pageStatus = PAGE_ITEM_NORMAL;
                        }

                    }

                    @Override
                    public void clickLeft(ListViewItem item) {
                        item.clickItem("left",pageStatus,leftStartIndex);
                        leftStartIndex = item.getIndex();
                        if ( pageStatus == PAGE_ITEM_NORMAL){
                            pageStatus  = PAGE_ITEM_START_SELECT;

                        }else{
                            pageStatus = PAGE_ITEM_NORMAL;
                        }

                    }

                    @Override
                    public void selected() {

                        ListViewAdapter.this.notifyDataSetChanged();
                    }

                    @Override
                    public void cancel() {
                        pageStatus = PAGE_ITEM_START_SELECT;
                        ListViewAdapter.this.notifyDataSetChanged();
                    }
                });
                item.reFreshLayout(i);

                return item;
            }
        }
        public  class  ListViewItem extends  LinearLayout{



            private JsonSegmentSettingDataGet.LineItemData mLineItemData;

            private JsonSegmentSettingDataGet.PageData mPageData;

            public JsonSegmentSettingDataGet.PageData getPageData(){

                return mPageData;
            }

            private final int  SELECTED_COLOR = getColor(R.color.bg_blue_of_1);
            private final int  START_SELECT_COLOR = Color.YELLOW;
            private final int  NORMAL_COLOR = R.color.common_white;

            private int index;

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            private RelativeLayout leftLayout;
            private RelativeLayout rightLayout;

            private IteeTextView time;
            private IteeTextView leftOne;
            private IteeTextView leftTwo;
            private IteeTextView rightOne;
            private IteeTextView rightTwo;

            private ListViewItemClickListener listViewItemClickListener;

            public ListViewItemClickListener getListViewItemClickListener() {
                return listViewItemClickListener;
            }

            public void setListViewItemClickListener(ListViewItemClickListener listViewItemClickListener) {
                this.listViewItemClickListener = listViewItemClickListener;
            }

            public ListViewItem(Context context,JsonSegmentSettingDataGet.PageData pageData,int height) {
                super(context);
                setOrientation(HORIZONTAL);
                mPageData = pageData;
                LinearLayout.LayoutParams leftLayoutParams = new LinearLayout.LayoutParams((int)(getScreenWidth()*0.4), height);

                leftLayout = new RelativeLayout(context);
                leftLayout.setLayoutParams(leftLayoutParams);
                AppUtils.addBottomSeparatorLine(leftLayout, context);

                RelativeLayout.LayoutParams leftOneParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                leftOneParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                leftOne = new IteeTextView(context);
                leftOne.setGravity(Gravity.CENTER);
                leftOne.setLayoutParams(leftOneParams);
                leftOne.setTextSize(Constants.FONT_SIZE_SMALLER);
                // leftOne.setText("1");
                leftOne.setTextColor(getColor(R.color.common_black));
                leftOne.setId(View.generateViewId());

                RelativeLayout.LayoutParams leftTwoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, height);
                leftTwoParams.addRule(RelativeLayout.RIGHT_OF, leftOne.getId());
                leftTwoParams.addRule(RelativeLayout.CENTER_VERTICAL);
                leftTwo = new IteeTextView(context);
                leftTwo.setLayoutParams(leftTwoParams);
                // leftTwo.setText("M");
                leftTwo.setTextColor(getColor(R.color.common_black));
                leftLayout.addView(leftOne);
                leftLayout.addView(leftTwo);
                leftTwo.setTextSize(Constants.FONT_SIZE_SMALLER);
                leftLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listViewItemClickListener != null)
                            listViewItemClickListener.clickLeft(ListViewItem.this);

                    }
                });

                LinearLayout.LayoutParams timeLayoutParams = new LinearLayout.LayoutParams((int)(getScreenWidth()*0.2), height);

                RelativeLayout timeLayout = new RelativeLayout(context);
                timeLayout.setLayoutParams(timeLayoutParams);
                RelativeLayout.LayoutParams tvTimeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
                time = new IteeTextView(context);
                time.setLayoutParams(tvTimeParams);
                time.setText("Time");
                time.setGravity(Gravity.CENTER);
                time.setTextColor(getColor(R.color.common_black));
                time.setBackgroundColor(getColor(R.color.common_light_gray));
                time.setTextSize(Constants.FONT_SIZE_SMALLER);

                time.setSingleLine(false);
                timeLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                timeLayout.addView(time);
                AppUtils.addBottomSeparatorLine(timeLayout, context);
                timeLayout.addView(AppUtils.getVerticalLine(context, 2));
                View v = AppUtils.getVerticalLine(context, 2);
                timeLayout.addView(v);
                RelativeLayout.LayoutParams vParams = (RelativeLayout.LayoutParams)v.getLayoutParams();
                vParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);





                LinearLayout.LayoutParams rightLayoutParams = new LinearLayout.LayoutParams((int)(getScreenWidth()*0.4), LayoutParams.MATCH_PARENT);

                rightLayout = new RelativeLayout(context);
                rightLayout.setLayoutParams(rightLayoutParams);
                AppUtils.addBottomSeparatorLine(rightLayout, context);

                RelativeLayout.LayoutParams rightOneParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                rightOneParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                rightOne = new IteeTextView(context);
                rightOne.setLayoutParams(rightOneParams);
                rightOne.setGravity(Gravity.CENTER);
                // rightOne.setText("1");
                rightOne.setTextColor(getColor(R.color.common_black));
                rightOne.setId(View.generateViewId());

                RelativeLayout.LayoutParams rightTwoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,height);
                rightTwoParams.addRule(RelativeLayout.RIGHT_OF, rightOne.getId());
                rightTwoParams.addRule(RelativeLayout.CENTER_VERTICAL);
                rightTwo = new IteeTextView(context);
                rightTwo.setLayoutParams(rightTwoParams);
                // rightTwo.setText("M");
                rightTwo.setTextColor(getColor(R.color.common_black));
                rightLayout.addView(rightOne);
                rightLayout.addView(rightTwo);

                rightLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listViewItemClickListener != null)
                            listViewItemClickListener.clickRight(ListViewItem.this);
                    }
                });
                rightTwo.setTextSize(Constants.FONT_SIZE_SMALLER);
                rightOne.setTextSize(Constants.FONT_SIZE_SMALLER);
                this.addView(leftLayout);
                this.addView(timeLayout);
                this.addView(rightLayout);


            }


            private void setTitle(String left,String right){
                if (right!=null)
                    rightOne.setText(right);
                leftOne.setText(left);
            }

            // start  清除状态
            private void clearStatus(){

                for (JsonSegmentSettingDataGet.PageData pageData:mPageDataList){
                    for (JsonSegmentSettingDataGet.LineItemData lineItemData :pageData.getDataList()){
                        lineItemData.getRightData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
                        lineItemData.getLeftData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
                    }
                }

            }


            public  void clickItem(String btnName,String clickStatus,int startIndex){
                JsonSegmentSettingDataGet.ItemData itemData =null;
                if ("left".equals(btnName)){
                    itemData = mLineItemData.getLeftData();
                }else{
                    itemData = mLineItemData.getRightData();
                }
                //设定好  直接 全选
                //bottom btn 状态

                if (itemData.isClickEnabled()){
                    String selectVal = itemData.getNo();
                    String selectFlag = itemData.getFlag();

                    bottomLayout.getClearBtn().setText(getString(R.string.custom_clear_all));


                    bottomLayout.getStartBtn().setBackgroundResource(R.drawable.bg_gray_btn);

                    bottomLayout.getTransferBtn().setBackgroundResource(R.drawable.bg_gray_btn);

                    bottomLayout.getStartBtn().setEnabled(false);

                    bottomLayout.getTransferBtn().setEnabled(false);


                    if (Utils.isStringNotNullOrEmpty(selectVal)){//点种 全选 变蓝  按钮变clear

                        if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(itemData.getStatus())){
                            clearStatus();

                        }else{
                            clearStatus();
                            for (JsonSegmentSettingDataGet.PageData pageData:mPageDataList){
                                for (JsonSegmentSettingDataGet.LineItemData lineItemData :pageData.getDataList()){
                                    if (selectVal.equals(lineItemData.getRightData().getNo())){

                                        if (Constants.CUSTOM_FLAG_9_HLLES.equals(itemData.getFlag())&&selectFlag.equals(lineItemData.getRightData().getFlag())){
                                            lineItemData.getRightData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED);

                                        }
                                        if (!Constants.CUSTOM_FLAG_9_HLLES.equals(itemData.getFlag())){

                                            if (Constants.CUSTOM_FLAG_START.equals(lineItemData.getRightData().getFlag())||Constants.CUSTOM_FLAG_TRANSFER.equals(lineItemData.getRightData().getFlag())){
                                                lineItemData.getRightData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED);
                                            }

                                        }



                                    }
                                    if (selectVal.equals(lineItemData.getLeftData().getNo())){

                                        if (Constants.CUSTOM_FLAG_9_HLLES.equals(itemData.getFlag())&&selectFlag.equals(lineItemData.getLeftData().getFlag())){
                                            lineItemData.getLeftData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED);

                                        }
                                        if (!Constants.CUSTOM_FLAG_9_HLLES.equals(itemData.getFlag())){

                                            if (Constants.CUSTOM_FLAG_START.equals(lineItemData.getLeftData().getFlag())||Constants.CUSTOM_FLAG_TRANSFER.equals(lineItemData.getLeftData().getFlag())){
                                                lineItemData.getLeftData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED);
                                            }

                                        }


                                    }


                                }
                            }

                            if (Constants.CUSTOM_FLAG_START.equals(itemData.getFlag())){
                                clearType = Constants.CUSTOM_FLAG_START;
                                bottomLayout.getClearBtn().setText(getString(R.string.custom_clear)+itemData.getNo());
                            }
                            if (Constants.CUSTOM_FLAG_TRANSFER.equals(itemData.getFlag())){
                                bottomLayout.getClearBtn().setText(getString(R.string.custom_clear)+itemData.getNo());
                                clearType = Constants.CUSTOM_FLAG_TRANSFER;
                            }

                            if (Constants.CUSTOM_FLAG_9_HLLES.equals(itemData.getFlag())){
                                bottomLayout.getClearBtn().setText(getString(R.string.custom_clear)+getString(R.string.q_9)+"-"+itemData.getNo());
                                clearType = Constants.CUSTOM_FLAG_9_HLLES;
                            }




                        }


                        listViewItemClickListener.selected();
                    }else{
                        if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(itemData.getStatus())){//蓝色  状态 取消
                            clearStatus();
                            listViewItemClickListener.selected();
                        }else{
                            //点击 是否变黄
                            boolean changeYellow = true;
                            if ("left".equals(btnName)){
                                for (JsonSegmentSettingDataGet.LineItemData lineItemData :mPageData.getDataList()){
                                    if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_START_SELECT.equals(lineItemData.getLeftData().getStatus())){
                                        changeYellow = false;
                                        break;
                                    }

                                }
                            }else{
                                for (JsonSegmentSettingDataGet.LineItemData lineItemData :mPageData.getDataList()){
                                    if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_START_SELECT.equals(lineItemData.getRightData().getStatus())){

                                        changeYellow = false;
                                        break;
                                    }
                                }
                            }


                            if (changeYellow){//默认状态  点击 变黄
                                clearStatus();
                                itemData.setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_START_SELECT);
                                listViewItemClickListener.selected();
                            }
                            if (!changeYellow){//黄色 点击 变蓝
                                setLeftStatus(startIndex, index, JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED, btnName);

                                if (!checkBlueSelect()){
                                    for (JsonSegmentSettingDataGet.LineItemData lineItemData :mPageData.getDataList()){
                                        lineItemData.getRightData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);
                                        lineItemData.getLeftData().setStatus(JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_NORMAL);

                                    }

                                    bottomLayout.getStartBtn().setBackgroundResource(R.drawable.bg_gray_btn);
                                    bottomLayout.getStartBtn().setEnabled(false);
                                    bottomLayout.getTransferBtn().setBackgroundResource(R.drawable.bg_gray_btn);
                                    bottomLayout.getTransferBtn().setEnabled(false);
                                    Utils.showShortToast(getContext(),getString(R.string.segment_custom_err_1));
                                }

                                listViewItemClickListener.selected();
                            }
                        }

                    }

                }






            }

            private boolean checkBlueSelect(){
                for (JsonSegmentSettingDataGet.LineItemData lineItemData :mPageData.getDataList()){
                    if (Utils.isStringNotNullOrEmpty(lineItemData.getLeftData().getNo())&&JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(lineItemData.getLeftData().getStatus()))return false;
                    if (Utils.isStringNotNullOrEmpty(lineItemData.getRightData().getNo())&&JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(lineItemData.getRightData() .getStatus()))return false;
                }

                return true;
            }

            private void setLeftStatus(int a ,int b,String status, String btnName){
                if (a <= b){
                    for (int i = a;i<=b;i++){
                        if ("left".equals(btnName)){
                            mPageData.getDataList().get(i).getLeftData().setStatus(status);
                        }else{
                            mPageData.getDataList().get(i).getRightData().setStatus(status);
                        }
                    }
                }else{
                    for (int i = b;i<=a;i++){
                        if ("left".equals(btnName)){
                            mPageData.getDataList().get(i).getLeftData().setStatus(status);
                        }else{
                            mPageData.getDataList().get(i).getRightData().setStatus(status);
                        }
                    }
                }
                bottomLayout.getStartBtn().setBackgroundResource(R.drawable.bg_green_btn);
                bottomLayout.getTransferBtn().setBackgroundResource(R.drawable.bg_green_btn);
                bottomLayout.getStartBtn().setEnabled(true);
                bottomLayout.getTransferBtn().setEnabled(true);
            }

            public void reFreshLayout(int index){
                leftOne.setText("");
                leftTwo.setText("");

                rightOne.setText("");
                rightTwo.setText("");

                this.index = index;
                this.mLineItemData = mPageData.getDataList().get(index);
                leftLayout.setBackgroundColor(getColor(NORMAL_COLOR));

                rightLayout.setBackgroundColor(getColor(NORMAL_COLOR));
                String leftValue = mLineItemData.getLeftData().getNo();


                leftOne.setBackgroundResource(0);
                rightOne.setBackgroundResource(0);
                String mAndP = "";
                if ("3".equals(mLineItemData.getLeftData().getFlag())){
                    leftValue = "9";
                    mAndP = "- "+mLineItemData.getLeftData().getNo();
                    leftOne.setBackgroundResource(R.drawable.bg_black_ring);

                }
                leftTwo.setText(mAndP);
                leftOne.setText(leftValue);
                time.setText(mLineItemData.getTime());

                //left
                if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_START_SELECT.equals(mLineItemData.getLeftData().getStatus())){
                    leftLayout.setBackgroundColor(START_SELECT_COLOR);
                }

                if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(mLineItemData.getLeftData().getStatus())){
                    leftLayout.setBackgroundColor(SELECTED_COLOR);
                }

                String mAndP_l = "";

                if (mLineItemData.getRightData() != null){
                    String rightValue = mLineItemData.getRightData().getNo();
                    if ("3".equals(mLineItemData.getRightData().getFlag())){
                        rightValue = "9";
                        mAndP_l = "- "+mLineItemData.getRightData().getNo();
                        rightOne.setBackgroundResource(R.drawable.bg_black_ring);
                    }

                    rightTwo.setText(mAndP_l);
                    rightOne.setText(rightValue);
                    //right
                    if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_START_SELECT.equals(mLineItemData.getRightData().getStatus())){
                        rightLayout.setBackgroundColor(START_SELECT_COLOR);
                    }

                    if (JsonSegmentSettingDataGet.ItemData.ITEM_DATA_STATUS_SELECTED.equals(mLineItemData.getRightData().getStatus())){
                        rightLayout.setBackgroundColor(SELECTED_COLOR);
                    }

                }




            }
        }





    }

    interface ListViewItemClickListener{

        public void clickRight(PagerItem.ListViewItem item);
        public void clickLeft(PagerItem.ListViewItem item);

        public void selected();

        public void cancel();
    }


    interface BottomClickListener{

        public boolean clickStart();
        public boolean clickTransfer();


        public void clickHoles9();


        public boolean clearAll();
        public boolean clear();
    }

    //api

    protected void getTeeTimeFormatData() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));

        if ("1".equals(edit)){
            params.put("tee_date", chooseDates);

        }else{

            params.put("first_tee_time", firstTime);
            params.put("last_tee_time", lastTime);
            params.put("gap_time", gap);
            params.put("type_flag", Constants.CUSTOM_FLAG);

            String[] ws =    weekdayPace.split("h");
            String[] hs =    holidayPace.split("h");

            String postWeekDay =String.valueOf(Integer.parseInt(ws[0]) * 60 + Integer.parseInt(ws[1]));
            String postHoliday =String.valueOf( Integer.parseInt(hs[0])*60+ Integer.parseInt(hs[1]));
            params.put("weekday_pace", postWeekDay);
            params.put("holiday_pace", postHoliday);
        }



        HttpManager<JsonSegmentSettingDataGet> hh = new HttpManager<JsonSegmentSettingDataGet>(SegmentACustomFragment.this) {

            @Override
            public void onJsonSuccess(JsonSegmentSettingDataGet jo) {

                pageDataList = jo.getPageDataList();

                showIndex = 1;
                show9Index = 1;
                try{
                    showIndex = Integer.parseInt(jo.getStartMaxNo())+1;
                }catch (NumberFormatException e){


                }

                try{
                    show9Index = Integer.parseInt(jo.getNineHolesMaxNo())+1;
                }catch (NumberFormatException e){


                }


                for (JsonSegmentSettingDataGet.PageData pageData :pageDataList){
                    PagerItem p = new PagerItem(getBaseActivity(), pageData);
                    p.setPageListData(pageDataList);
                    pageViewList.add(p);
                }

                viewPager.setAdapter(new PagerAdapter() {
                    @Override
                    public int getCount() {
                        return pageDataList.size();
                    }

                    @Override
                    public void destroyItem(ViewGroup container, int position, Object object) {
                        ((ViewGroup) container).removeView((View) object);
                        object = null;
                    }



                    @Override
                    public void setPrimaryItem(ViewGroup container, int position, Object object) {
                        pagerItem = (PagerItem)object;
                        pagerItem.getListViewAdapter().notifyDataSetChanged();

                    }


                    @Override
                    public boolean isViewFromObject(View view, Object object) {
                        return view == object;
                    }

                    @Override
                    public Object instantiateItem(ViewGroup container, int position) {

                        PagerItem p  =pageViewList.get(position);
                        container.addView(p);
                        return p;
                    }
                });


            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };

        hh.startGet(getActivity(), ApiManager.HttpApi.GetTeeTimeFormatData, params);


    }


    @Override
    protected void reShowWithBackValue() {
        super.reShowWithBackValue();
        Bundle bundle = getReturnValues();
        if (bundle != null) {
            if (ChooseDateFragment.class.getName().equals(bundle.getString(TransKey.COMMON_FROM_PAGE))) {
//                selectedDates = bundle.getString(TransKey.SELECTED_DATE);
//                ArrayList<String> dateList = AppUtils.changeString2List(selectedDates, Constants.STR_COMMA);
//                String etTitle = AppUtils.getEtTimeTitle(dateList, mContext);
//                tvShowDate.setText(etTitle);
//
//                getSunRiseSetTime();
            }else{
                String isOk = bundle.getString("isOk",Constants.STR_EMPTY);
                if (Utils.isStringNotNullOrEmpty(isOk)){
                    try {
                        doBackWithReturnValue(bundle,(Class<? extends BaseFragment>) Class.forName(fromPage));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }



        }
    }



    public void saveTeeTime() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put("start_date", chooseDates);

        params.put("first_tee_time", firstTime);
        params.put("last_tee_time", lastTime);
        params.put("gap_time", gap);

        String[] ws =    weekdayPace.split("h");
        String[] hs =    holidayPace.split("h");

        String postWeekDay =String.valueOf(Integer.parseInt(ws[0]) * 60 + Integer.parseInt(ws[1]));
        String postHoliday =String.valueOf( Integer.parseInt(hs[0])*60+ Integer.parseInt(hs[1]));
        params.put("weekday_pace", postWeekDay);
        params.put("holiday_pace", postHoliday);
        params.put("tee_format", "5");
        params.put("sunriseTime", sunriseTime);
        params.put("sunsetTime", sunsetTime);
        params.put("data_list", getJsonDataPost());
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(SegmentACustomFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                Utils.showShortToast(getActivity(), msg);
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    Bundle bundle = new Bundle();
                    bundle.putString("isOk","1");

                    doBackWithSegmentReturnValue(bundle, TeeTimeCalendarFragment.class);



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

        hh.start(getActivity(), ApiManager.HttpApi.SaveTeeTime, params);

    }


    private String getJsonDataPost(){

        JSONArray resArray = new JSONArray();

        try {
            for (PagerItem pagerItem:pageViewList){
                boolean haveRight = false;
                JSONObject jsItemLeft = new JSONObject();
                JSONObject jsItemRight = new JSONObject();
                jsItemLeft.put("ca_name", pagerItem.getmPageData().getLeftName());
                jsItemLeft.put("ca_id",pagerItem.getmPageData().getLeftId());
                if (Utils.isStringNotNullOrEmpty(pagerItem.getmPageData().getRightName())){
                    haveRight = true;
                    jsItemRight.put("ca_name",pagerItem.getmPageData().getRightName());
                    jsItemRight.put("ca_id", pagerItem.getmPageData().getRightId());

                }

                JSONArray jsLeftTimeArray = new JSONArray();
                JSONArray jsRightTimeArray = new JSONArray();
                for (JsonSegmentSettingDataGet.LineItemData lineItemData:pagerItem.getmPageData().getDataList()){

                    JsonSegmentSettingDataGet.ItemData leftData = lineItemData.getLeftData();
                    //time
                    JSONObject leftObject = new JSONObject();
                    leftObject.put("frequenter",leftData.getFrequenter());
                    leftObject.put("prime_flag",leftData.getPrimeFlag());
                    leftObject.put("all_reserve_member",leftData.getAllReserveMember());
                    leftObject.put("flg",leftData.getFlag());
                    leftObject.put("no",leftData.getNo());
                    leftObject.put("name",lineItemData.getTime());
                    leftObject.put("val",leftData.getVal());

                    jsLeftTimeArray.put(leftObject);
                    if (haveRight){
                        JsonSegmentSettingDataGet.ItemData rightData = lineItemData.getRightData();
                        JSONObject rightObject = new JSONObject();
                        rightObject.put("frequenter",rightData.getFrequenter());
                        rightObject.put("prime_flag",rightData.getPrimeFlag());
                        rightObject.put("all_reserve_member",rightData.getAllReserveMember());
                        rightObject.put("flg",rightData.getFlag());
                        rightObject.put("no",rightData.getNo());
                        rightObject.put("val",rightData.getVal());
                        rightObject.put("name",lineItemData.getTime());
                        jsRightTimeArray.put(rightObject);
                    }
                }



                jsItemLeft.put("time",jsLeftTimeArray);
                resArray.put(jsItemLeft);
                if (haveRight){
                    jsItemRight.put("time",jsRightTimeArray);
                    resArray.put(jsItemRight);
                }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //JSONObject jsItem = new JSONObject();

        return resArray.toString();
    }
}
