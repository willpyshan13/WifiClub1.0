package cn.situne.itee.fragment.teetime;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.player.PlayerInfoEditFragment;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.SwipeLinearLayout;
import cn.situne.itee.view.popwindow.SelectYearPopupWindow;

/**
 * Created by luochao on 12/6/15.
 */
public class SegmentASetingList extends BaseFragment {

    private PullToRefreshListView listView;

    private ListAdapter listAdapter;

    private String selectedYear;
    private String fromPage;
    private SelectYearPopupWindow selectYearPopupWindow;
    @Override
    protected int getFragmentId() {
        return R.layout.fragment_segment_a_setting_list;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {


        Bundle bundle = getArguments();
        if (bundle != null) {

            selectedYear = bundle.getString("selectedYear", Constants.STR_EMPTY);
            fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE, StringUtils.EMPTY);
        }


        listView = (PullToRefreshListView)rootView.findViewById(R.id.listView);



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

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        listAdapter   = new ListAdapter();

        listView.setAdapter(listAdapter);
        listView.onRefreshComplete();
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
        setCalendarActionBar();

        setTitle(selectedYear);


        getTvRight().setBackgroundResource(R.drawable.icon_common_add);
        getTvRight().setText(StringUtils.EMPTY);

        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Bundle bundle = new Bundle();

               bundle.putString(TransKey.COMMON_FROM_PAGE,SegmentASetingList.class.getName());
                push(SegmentATimeSettingFragment.class, bundle);
            }
        });

        ImageView ivShop = new ImageView(getActivity());
        ivShop.setImageResource(R.drawable.tee_time_icon_2);


        ivShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                try {
                    doBackWithReturnValue(bundle,(Class<? extends BaseFragment>) Class.forName(fromPage));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });


        RelativeLayout.LayoutParams ivPackageLayoutParams
                = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(60), getActualWidthOnThisDevice(60));
        ivPackageLayoutParams.addRule(RelativeLayout.LEFT_OF, getTvRight().getId());
        ivPackageLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivPackageLayoutParams.rightMargin = getActualWidthOnThisDevice(30);
        ivShop.setLayoutParams(ivPackageLayoutParams);
        RelativeLayout parent = (RelativeLayout) getTvRight().getParent();
//        getTvRight().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isEdit = !isEdit;
//                if (isEdit) {
//                    getTvRight().setBackground(null);
//                    getTvRight().setText(R.string.common_ok);
//                    gridView.setVisibility(View.GONE);
//                   // llTeeTimeBtnContainer.setVisibility(View.VISIBLE);
//
//                } else {
//                    getTvRight().setBackgroundResource(R.drawable.tee_time_calendar_icon_edit);
//                    getTvRight().setText(StringUtils.EMPTY);
//                    llTeeTimeBtnContainer.setVisibility(View.GONE);
//                   // gridView.setVisibility(View.VISIBLE);
//                    if (calendarPickerView != null) {
//                        calendarPickerView.clearOldSelections();
//                    }
//
//                }
//            }
//        });
        parent.addView(ivShop);

        parent.invalidate();



//        getTvRight().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Bundle bundle = new Bundle();
//
//                bundle.putString(TransKey.COMMON_FROM_PAGE,SegmentASetingList.class.getName());
//                push(SegmentATimeSettingFragment.class, bundle);
//
//            }
//        });

        getTvTitle().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // select year
                selectYearPopupWindow = new SelectYearPopupWindow(getActivity(), null,0);
                selectYearPopupWindow.showAtLocation(SegmentASetingList.this.getRootView().findViewById(R.id.rl_content_container),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                selectYearPopupWindow.btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        selectedYear = selectYearPopupWindow.wheelViewYear.getCurrentItem() + Constants.STR_EMPTY;
                        setTitle(selectedYear);
//                        yearDateStatuses.clear();
//
//                        getData(selectedYear);
//                        calendarPickerView.validateAndUpdate();
                        selectYearPopupWindow.dismiss();
                    }
                });
                selectYearPopupWindow.btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectYearPopupWindow.dismiss();
                    }
                });
            }
        });
    }

    class ListViewItem extends SwipeLinearLayout{

        private IteeTextView tvShowText;


        public ListViewItem(Context context) {


            super(context, AppUtils.getRightButtonWidth(context));

            ListView.LayoutParams myParams = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(100));

            this.setLayoutParams(myParams);
            this.setOrientation(HORIZONTAL);
            tvShowText = new IteeTextView(context);

            LinearLayout.LayoutParams bodyParams = new LinearLayout.LayoutParams(ListView.LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(100));
            RelativeLayout body = new RelativeLayout(context);
            body.setLayoutParams(bodyParams);
            body.addView(tvShowText);

            ImageView icon = new ImageView(context);
            icon.setBackgroundResource(R.drawable.icon_right_arrow);
            body.addView(icon);

            LayoutUtils.setRightArrow(icon, 40, context);
            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvShowText, 40, context);
            AppUtils.addBottomSeparatorLine(body, context);


            Button delBtn = new Button(context);
            this.addView(body);
        }

        public void refreshLayout(){

            tvShowText.setText("text");
        }
    }

    @Override
    protected void reShowWithBackValue() {
        super.reShowWithBackValue();
    }

    class ListAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 50;
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
                item = new ListViewItem(getBaseActivity());

            }else{

                item = (ListViewItem)view;
            }

            item.refreshLayout();
            return item;
        }
    }




}
