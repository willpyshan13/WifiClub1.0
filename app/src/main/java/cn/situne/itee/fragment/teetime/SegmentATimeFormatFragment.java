package cn.situne.itee.fragment.teetime;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.BeanPropertyFilter;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;

import java.util.ArrayList;
import java.util.HashMap;
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
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonBookingDetailList;
import cn.situne.itee.manager.jsonentity.JsonLogin;
import cn.situne.itee.manager.jsonentity.JsonReturnBookingOrderNo;
import cn.situne.itee.view.IteeTextView;

/**
 * Created by luochao on 12/8/15.
 */
public class SegmentATimeFormatFragment  extends BaseEditFragment {

    private LinearLayout body;

    private String chooseDates;
    private String firstTime;
    private String lastTime;
    private String gap;
    private String typeFlag;
    private String beforeTypeFlag;
    private String weekdayPace;
    private String holidayPace;
    private  LineLayout row1;
    private  LineLayout row2;
    private  LineLayout row3;
    private  LineLayout row4;
    private LineLayout row5;
    private String sunriseTime;
    private String sunsetTime;
    private String edit;

    private String fromPage;

    private boolean isTwo;



    @Override
    protected int getFragmentId() {
        return R.layout.fragment_tee_time_format;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }


    class LineLayout extends LinearLayout{

        private ImageView icon;

        public ImageView getIcon() {
            return icon;
        }

        public void setIcon(ImageView icon) {
            this.icon = icon;
        }

        public LineLayout(Context context) {
            super(context);
        }
    }


    private View.OnClickListener itemListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            row1.setBackgroundColor(getColor(R.color.common_white));
            row2.setBackgroundColor(getColor(R.color.common_white));

            if (!isTwo)
            row3.setBackgroundColor(getColor(R.color.common_white));
            row4.setBackgroundColor(getColor(R.color.common_white));


            row5.setBackgroundColor(getColor(R.color.common_white));
            view.setBackgroundColor(getColor(R.color.bg_blue_of_1));
            typeFlag   = String.valueOf(view.getTag());


        }
    };

    @Override
    protected void initControls(View rootView) {

        Bundle bundle = getArguments();
        isTwo  =false;
        if (bundle!=null){

            chooseDates = bundle.getString("chooseDates", Constants.STR_EMPTY);
            firstTime = bundle.getString("firstTime", Constants.STR_EMPTY);
            lastTime = bundle.getString("lastTime", Constants.STR_EMPTY);
            gap = bundle.getString("gap", Constants.STR_EMPTY);
            weekdayPace = bundle.getString("weekdayPace", Constants.STR_EMPTY);
            holidayPace = bundle.getString("holidayPace", Constants.STR_EMPTY);
            fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE);
            sunriseTime = bundle.getString("sunriseTime", Constants.STR_EMPTY);
            sunsetTime = bundle.getString("sunsetTime", Constants.STR_EMPTY);
            typeFlag =  bundle.getString("typeFlag", Constants.STR_0);
            beforeTypeFlag = typeFlag;
            edit =  bundle.getString("edit", Constants.STR_0);
        }

        if ("0".equals(edit))
            typeFlag = "0";

         body = (LinearLayout)rootView.findViewById(R.id.body);
         row1 = getItem(getString(R.string.one_tee_start),R.drawable.one_tee_start);
         row1.setTag("1");
         row2 = getItem(getString(R.string.two_tee_start),R.drawable.two_tee_start);
         row3 = getItem(getString(R.string.three_tee_start),R.drawable.three_tee_start);
         row4 = getItem(getString(R.string.mix_tee_start),R.drawable.mix_tee_start);

        row2.setTag("2");
        row3.setTag("3");
        row4.setTag("4");


        row1.setOnClickListener(itemListener);
        row2.setOnClickListener(itemListener);

        row3.setBackgroundColor(getColor(R.color.common_gray));
        row4.setOnClickListener(itemListener);

        row4.setVisibility(View.GONE);

        if (("8".equals(gap)||"10".equals(gap))&&"2h0".equals(weekdayPace)&&"2h0".equals(holidayPace)){

            row4.setVisibility(View.VISIBLE);
        }

         row5 = getItem(getString(R.string.tag_custom),0);

        row5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                typeFlag = "5";

                if (checkChange()){
                    edit ="0";
                }else{
                    edit ="1";

                }
                Bundle bundle = new Bundle();
                bundle.putString(TransKey.COMMON_FROM_PAGE,SegmentATimeFormatFragment.class.getName());
                bundle.putString("chooseDates",chooseDates);
                bundle.putString("firstTime",firstTime);
                bundle.putString("lastTime",lastTime);
                bundle.putString("gap",gap);
                bundle.putString("weekdayPace",weekdayPace);
                bundle.putString("holidayPace",holidayPace);
                bundle.putString("sunriseTime",sunriseTime);
                bundle.putString("sunsetTime",sunsetTime);
                bundle.putString("edit",edit);
                push(SegmentACustomFragment.class,bundle);
            }
        });
        body.addView(row1);
        body.addView(row2);
        body.addView(row3);
        body.addView(row4);
        body.addView(row5);
        switch (typeFlag){

            case "1":
                row1.setBackgroundColor(getColor(R.color.bg_blue_of_1));
                break;
            case "2":
                row2.setBackgroundColor(getColor(R.color.bg_blue_of_1));
                break;
            case "3":
                row3.setBackgroundColor(getColor(R.color.bg_blue_of_1));
                break;
            case "4":
                row4.setBackgroundColor(getColor(R.color.bg_blue_of_1));
                break;
            case "5":
                row5.setBackgroundColor(getColor(R.color.bg_blue_of_1));
                break;
        }


    }



    private boolean checkChange(){

        if (!"1".equals(edit)){
            return true;
        }
        if (!beforeTypeFlag.equals(typeFlag)){
            return true;
        }
        return false;
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

        getTvLeftTitle().setText(getString(R.string.tee_times_tee_times_format));

        getTvRight().setBackground(null);
        getTvRight().setText(R.string.common_next);

        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Constants.STR_0.equals(typeFlag)){

                    Utils.showShortToast(getBaseActivity(),getString(R.string.segment_err_mes_2));
                } else if("5".equals(typeFlag)){
                    typeFlag = "5";

                    if (checkChange()){
                        edit ="0";
                    }else{
                        edit ="1";

                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(TransKey.COMMON_FROM_PAGE,SegmentATimeFormatFragment.class.getName());
                    bundle.putString("chooseDates",chooseDates);
                    bundle.putString("firstTime",firstTime);
                    bundle.putString("lastTime",lastTime);
                    bundle.putString("gap",gap);
                    bundle.putString("weekdayPace",weekdayPace);
                    bundle.putString("holidayPace",holidayPace);
                    bundle.putString("sunriseTime",sunriseTime);
                    bundle.putString("sunsetTime",sunsetTime);
                    bundle.putString("edit",edit);
                    push(SegmentACustomFragment.class,bundle);
                }else{
                    if (checkChange()){
                        edit ="0";
                    }else{
                        edit ="1";

                    }

                    Bundle bundle = new Bundle();
                    bundle.putString(TransKey.COMMON_FROM_PAGE,SegmentATimeFormatFragment.class.getName());

                    bundle.putString("chooseDates",chooseDates);
                    bundle.putString("firstTime",firstTime);
                    bundle.putString("lastTime",lastTime);
                    bundle.putString("gap",gap);
                    bundle.putString("weekdayPace",weekdayPace);
                    bundle.putString("holidayPace", holidayPace);
                    bundle.putString("typeFlag", typeFlag);

                    bundle.putString("sunriseTime",sunriseTime);
                    bundle.putString("sunsetTime",sunsetTime);
                    bundle.putString("edit",edit);
                    push(TeeTimeLimitFragment.class, bundle);


                }


            }
        });
    }


    @Override
    protected void reShowWithBackValue() {
        Bundle bundle = getReturnValues();

        String isOk = bundle.getString("isOk",Constants.STR_EMPTY);

        if (Utils.isStringNotNullOrEmpty(isOk)){

            try {
                doBackWithReturnValue(bundle,(Class<? extends BaseFragment>) Class.forName(fromPage));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private List<ImageView> imageList;
    private LineLayout getItem(String key,int imgRes){
        LinearLayout.LayoutParams linearLayoutParams = new  LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LineLayout linearLayout = new LineLayout(getActivity());
        linearLayout.setBackgroundColor(getColor(R.color.common_white));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(linearLayoutParams);

        LinearLayout.LayoutParams rlTitleLayoutParams = new  LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(100));

        RelativeLayout rlTitleLayout  = new RelativeLayout(getBaseActivity());
        rlTitleLayout.setLayoutParams(rlTitleLayoutParams);
        final ImageView icon = new ImageView(getBaseActivity());
        IteeTextView tvText = new IteeTextView(getBaseActivity());
        tvText.setText(key);
        icon.setBackgroundResource(R.drawable.icon_down);
        rlTitleLayout.addView(icon);
        rlTitleLayout.addView(tvText);

        if (key.equals(getString(R.string.tag_custom))){
            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvText, 100, getBaseActivity());
        }else{

            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvText, 100, getBaseActivity());
        }


        RelativeLayout.LayoutParams iconParams = (RelativeLayout.LayoutParams)icon.getLayoutParams();
        iconParams.width = getActualHeightOnThisDevice(100);
        iconParams.height = getActualHeightOnThisDevice(100);;
       // iconParams.leftMargin = getActualWidthOnThisDevice(30);
        iconParams.addRule(RelativeLayout.CENTER_VERTICAL);

        LinearLayout.LayoutParams showImgParams = new  LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, getActualWidthOnThisDevice(350));

        View shortLine  = AppUtils.getSeparatorLine(getBaseActivity());
        rlTitleLayout.addView(shortLine);


        RelativeLayout.LayoutParams lineParams = (RelativeLayout.LayoutParams)shortLine.getLayoutParams();
        lineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lineParams.width = getActualWidthOnThisDevice(600);
        lineParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        ImageView showImg = new ImageView(getBaseActivity());
        showImg.setTag(shortLine);
        showImg.setLayoutParams(showImgParams);
        showImg.setBackgroundResource(imgRes);
        showImg.setVisibility(View.GONE);
        linearLayout.addView(rlTitleLayout);
        linearLayout.addView(showImg);
        icon.setTag(showImg);

        if (imageList ==null){

            imageList = new ArrayList<>();
        }
        imageList.add(icon);
        linearLayout.setIcon(icon);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = (View) view.getTag();
                View v1 = (View) v.getTag();

                for (ImageView imageView : imageList) {

                    if (imageView != null) {


                        if (!view.equals(imageView)) {
                            View vv = (View) imageView.getTag();
                            View vv1 = (View) vv.getTag();
                            vv.setVisibility(View.GONE);
                            vv1.setVisibility(View.GONE);

                            imageView.setBackgroundResource(R.drawable.icon_down);

                        }

                    }
                }


                if (v.getVisibility() == View.GONE) {
                    v.setVisibility(View.VISIBLE);
                    v1.setVisibility(View.VISIBLE);
                    icon.setBackgroundResource(R.drawable.icon_up);
                } else {
                    v.setVisibility(View.GONE);
                    v1.setVisibility(View.GONE);
                    icon.setBackgroundResource(R.drawable.icon_down);

                }
            }
        });

        if (imgRes == 0){

            icon.setVisibility(View.GONE);
            showImg.setVisibility(View.GONE);
        }

        linearLayout.addView(AppUtils.getSeparatorLine(getBaseActivity()));
        return linearLayout;
    }


    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        getCount();
    }

    public void getCount() {


        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));


        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(SegmentATimeFormatFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {


                    if (jo.getDataCount()>2){
                        isTwo = false;
                        row3.setOnClickListener(itemListener);

                        if(checkChange() && "3".equals(typeFlag)){
                            row3.setBackgroundColor(getColor(R.color.bg_blue_of_1));
                        }else{
                            row3.setBackgroundColor(getColor(R.color.common_white));
                        }

                        row3.getIcon().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                View v = (View) view.getTag();
                                View v1 = (View) v.getTag();

                                for (ImageView imageView : imageList) {

                                    if (imageView != null) {


                                        if (!view.equals(imageView)) {
                                            View vv = (View) imageView.getTag();
                                            View vv1 = (View) vv.getTag();
                                            vv.setVisibility(View.GONE);
                                            vv1.setVisibility(View.GONE);

                                            imageView.setBackgroundResource(R.drawable.icon_down);

                                        }

                                    }
                                }


                                if (v.getVisibility() == View.GONE) {
                                    v.setVisibility(View.VISIBLE);
                                    v1.setVisibility(View.VISIBLE);
                                    row3.getIcon().setBackgroundResource(R.drawable.icon_up);
                                } else {
                                    v.setVisibility(View.GONE);
                                    v1.setVisibility(View.GONE);
                                    row3.getIcon().setBackgroundResource(R.drawable.icon_down);

                                }
                            }
                        });
                    }else{
                        isTwo = true;
                        row3.setOnClickListener(null);
                        row3.getIcon().setOnClickListener(null);
                        row3.setBackgroundColor(getColor(R.color.common_gray ));
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
        hh.startGet(getActivity(), ApiManager.HttpApi.Cdatacount, params);
    }

}
