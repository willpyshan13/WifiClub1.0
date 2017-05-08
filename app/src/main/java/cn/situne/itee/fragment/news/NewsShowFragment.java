package cn.situne.itee.fragment.news;


import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.situne.itee.R;
import cn.situne.itee.adapter.NewsListItemAdapter;
import cn.situne.itee.entity.IncomingCall;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.view.IteeButton;
import cn.situne.itee.view.popwindow.SelectSharePopupWindow;

public class NewsShowFragment extends BaseFragment {


    private RelativeLayout rlContainer;
    private IteeButton btnShare;
    private ListView newsListView;

    private List<IncomingCall> dataSource = new ArrayList<>();

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_news_show_item;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        rlContainer = (RelativeLayout) rootView.findViewById(R.id.rl_news_body_container);
        btnShare = new IteeButton(getActivity());
        newsListView = new ListView(getActivity());


    }


    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectSharePopupWindow menuWindow = new SelectSharePopupWindow(getActivity());
                menuWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

    }


    @Override
    protected void setLayoutOfControls() {


//        rlContainer.addView(btnShare);

//        RelativeLayout.LayoutParams paramsIncomingTitle = (RelativeLayout.LayoutParams) btnShare.getLayoutParams();
//        paramsIncomingTitle.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//        paramsIncomingTitle.height = (int) (getActualHeightOnThisDevice() * 0.2f);
//        paramsIncomingTitle.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
//        btnShare.setLayoutParams(paramsIncomingTitle);
//        btnShare.setId(View.generateViewId());
//        btnShare.setText("share");

        rlContainer.addView(newsListView);

        RelativeLayout.LayoutParams paramsLIstView = (RelativeLayout.LayoutParams) newsListView.getLayoutParams();
        paramsLIstView.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsLIstView.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsLIstView.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        paramsLIstView.addRule(RelativeLayout.BELOW, btnShare.getId());
        Drawable drawable = getResources().getDrawable(android.R.color.transparent);
        newsListView.setSelector(drawable);
        newsListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        newsListView.setLayoutParams(paramsLIstView);
        int actionBarHeight = getActionBarHeight();
        NewsListItemAdapter adapter = new NewsListItemAdapter(getActivity(), actionBarHeight);
        newsListView.setAdapter(adapter);
        newsListView.setPadding(20, 20, 20, 0);
        newsListView.setVerticalScrollBarEnabled(false);
    }


    @Override
    protected void setPropertyOfControls() {

    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        setTitle(getString(R.string.teetimes_actionbar_incoming));

    }
}
