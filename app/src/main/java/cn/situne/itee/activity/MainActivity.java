package cn.situne.itee.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.adapter.SlidingMenuAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.ChangePasswordFragment;
import cn.situne.itee.fragment.administration.AdministrationEditFragment;
import cn.situne.itee.fragment.agents.AgentsListFragment;
import cn.situne.itee.fragment.charts.ChartViewFragment;
import cn.situne.itee.fragment.customers.CustomersTypeListFragment;
import cn.situne.itee.fragment.events.EventsListFragment;
import cn.situne.itee.fragment.quick.WelcomeFragment;
import cn.situne.itee.fragment.shopping.ShoppingGoodsListFragment;
import cn.situne.itee.fragment.shops.ShopsListFragment;
import cn.situne.itee.fragment.staff.StaffDepartmentListFragment;
import cn.situne.itee.fragment.staff.StaffDepartmentMemberListFragment;
import cn.situne.itee.fragment.teetime.TeeTimePageListFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.IteeApplication;
import cn.situne.itee.manager.RequestImageManager;
import cn.situne.itee.manager.jsonentity.JsonLogin;
import cn.situne.itee.manager.jsonentity.JsonProductCount;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.SlidingMenuItem;
import cn.situne.itee.view.SlidingMenuListView;
import cn.situne.itee.view.WaitingView;


public class MainActivity extends BaseActivity {

    private BaseFragment.OnPopBackListener onBackListener;

    private RelativeLayout rlTotalContainer;
    private RelativeLayout llMenuContainer;
    private RelativeLayout rlSlidingMenuContainer;
    private WaitingView waitingView;

    private IteeTextView tvLogout;
    private IteeTextView tvUserName;   //左滑菜单上的用户名

    private SlidingMenu menu;
    private SlidingMenuListView slidingMenuListView;
    private SlidingMenuAdapter smAdapter;
    private int actionBarType;
    private boolean isBackEnable;
    private String showConfigFlag;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
                //此处做你的处理
                IteeApplication.getInstance().finishActivity(MainActivity.this);
            }
        }
    };

    public SlidingMenu getMenu() {
        return menu;
    }

    public void setActionBarType(int type) {
        this.actionBarType = type;
    }

    @Override
    protected void initControls() {
        isBackEnable = true;

        rlTotalContainer = (RelativeLayout) findViewById(R.id.rl_total_container);
        llMenuContainer = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.fragment_sliding_menu, null);
        rlSlidingMenuContainer = (RelativeLayout) llMenuContainer.findViewById(R.id.rl_sliding_menu_container);
        tvUserName = (IteeTextView) llMenuContainer.findViewById(R.id.tv_menu_user_name);
        tvLogout = (IteeTextView) llMenuContainer.findViewById(R.id.tv_menu_logout);

        slidingMenuListView = new SlidingMenuListView(this);
        smAdapter = new SlidingMenuAdapter(this,showConfigFlag);
        menu = new SlidingMenu(this);

        RequestImageManager.init(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_LOCALE_CHANGED);

        this.registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void setDefaultValueOfControls() {
        rlSlidingMenuContainer.addView(slidingMenuListView);

        RelativeLayout.LayoutParams paramsSlidingMenuListView = (RelativeLayout.LayoutParams) slidingMenuListView.getLayoutParams();
        paramsSlidingMenuListView.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsSlidingMenuListView.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        slidingMenuListView.setLayoutParams(paramsSlidingMenuListView);

        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.sliding_menu_shadow);
        menu.setBehindOffset((int) (getWidth() * 0.33f));
        menu.setFadeDegree(0.35f);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        menu.setMenu(llMenuContainer);
        menu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
            @Override
            public void onOpened() {
                Utils.hideKeyboard(MainActivity.this);
            }
        });

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_action_bar));
        JsonLogin jl = (JsonLogin) Utils.readFromSP(this, Constants.KEY_SP_LOGIN_INFO);
        if (jl != null) {
            tvUserName.setText(jl.getUserName());
        }
    }

    @Override
    protected void setListenersOfControls() {
        slidingMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SlidingMenuItem smi = smAdapter.getItem(position);
                int menuId = smi.getMenuId();
                if (menuId == Constants.MENU_ID_SCAN_QR_CODE) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, QuickScanQrCodeActivity.class);
                    startActivityForResult(intent, 1);
                    showContent();
                    return;
                }
                if (smAdapter.getCurrentSelectedView() != null) {
                    removeMenuSelectedState(smAdapter.getCurrentSelectedView());
                }
                ImageView ivSelectedMark = (ImageView) view.findViewById(R.id.iv_menu_selected_mark);
                ivSelectedMark.setBackgroundColor(getColor(R.color.menu_selected_mark));
                view.setBackgroundResource(R.drawable.menu_item_selected_bg);

                smAdapter.setCurrentSelectedView(view);
                doSelectMenu(menuId);
                smAdapter.setCurrentSelectedIdx(position);
            }
        });

        tvUserName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pushFragment(ChangePasswordFragment.class);
                showContent();
                if (smAdapter.getCurrentSelectedView() != null) {
                    removeMenuSelectedState(smAdapter.getCurrentSelectedView());
                }
                smAdapter.setCurrentSelectedView(null);
            }
        });
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogout();
            }
        });
    }

    private void doSelectMenu(int menuId) {
        // 处理程序
        if (menuId == Constants.MENU_ID_TEE_TIMES) {
            MainActivity.this.changeFragment(TeeTimePageListFragment.class);
        }
        if (menuId == Constants.MENU_ID_EVENTS) {
            MainActivity.this.changeFragment(EventsListFragment.class);
        }
        if (menuId == Constants.MENU_ID_SHOPS) {
            boolean isAuthSelling = AppUtils.getAuth(AppUtils.AuthControl.AuthControlSelling, this);
            if (isAuthSelling) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                waitingView = (WaitingView) inflater.inflate(R.layout.view_common_waiting, null);
                waitingView.setBackgroundColor(getColor(R.color.common_black));
                waitingView.setAlpha(0.4f);
                rlTotalContainer.addView(waitingView);
                LayoutUtils.setMatchParentLayout(waitingView);
                getProductCount();
            } else {
                MainActivity.this.changeFragment(ShopsListFragment.class);
            }
        }
        if (menuId == Constants.MENU_ID_CUSTOMERS) {
            MainActivity.this.changeFragment(CustomersTypeListFragment.class);
        }
        if (menuId == Constants.MENU_ID_AGENTS) {
            MainActivity.this.changeFragment(AgentsListFragment.class);
        }
        if (menuId == Constants.MENU_ID_STAFF) {
            boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlSeeOtherDepartments, this);
            if (hasPermission) {
                MainActivity.this.changeFragment(StaffDepartmentListFragment.class);
            } else {
                MainActivity.this.changeFragment(StaffDepartmentMemberListFragment.class);
            }
        }
        if (menuId == Constants.MENU_ID_NEWS) {
            MainActivity.this.changeFragment(ShoppingGoodsListFragment.class);
        }
        if (menuId == Constants.MENU_ID_CHARTS) {
            MainActivity.this.changeFragment(ChartViewFragment.class);
        }
        if (menuId == Constants.MENU_ID_ADMINISTRATION) {
            MainActivity.this.changeFragment(AdministrationEditFragment.class);
        }
        showContent();
    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {
        smAdapter.configMenuItems();
        slidingMenuListView.setAdapter(smAdapter);

        slidingMenuListView.setDivider(null);

        tvUserName.setTextSize(Constants.FONT_SIZE_MORE_LARGER);
        tvUserName.setTextColor(getColor(R.color.menu_user_name));
        tvUserName.setText(R.string.menu_user_name);
        tvLogout.setText(R.string.menu_logout);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        Bundle bundle = getIntent().getExtras();
//        showConfigFlag =bundle.getString("showConfigFlag", Constants.STR_0);
        super.onCreate(savedInstanceState);
//        if( Constants.STR_0.equals(showConfigFlag)){
//            doSelectTeeTimes();
//        }else{
//            doSelectAdministrationEditFragment();
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (actionBarType == 0) {
                    showMenu();
                } else {
                    doBack();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void changeFragment(Class<? extends BaseFragment> clazz) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        List<Fragment> list = fragmentManager.getFragments();
        if (list != null && list.size() > 0) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        pushFragment(clazz);
    }

    public void changeFragment(Class<? extends BaseFragment> clazz,Bundle bundle) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        List<Fragment> list = fragmentManager.getFragments();
        if (list != null && list.size() > 0) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        pushFragment(clazz,bundle);
    }

    public void pushFragment(Class<? extends BaseFragment> clazz) {
        pushFragment(clazz, null, clazz.getName());
    }

    public void pushFragment(Class<? extends BaseFragment> clazz, Bundle bundle) {
        pushFragment(clazz, bundle, null);
    }

    public void pushFragment(Class<? extends BaseFragment> clazz, Bundle bundle, String fragmentName) {
        Utils.hideKeyboard(this);
        setOnBackListener(null);
        try {
            FragmentManager fragmentManager = this.getSupportFragmentManager();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(
                    R.anim.push_in_right, R.anim.push_out_left, R.anim.push_in_right, R.anim.push_out_left);
            BaseFragment baseFragment = clazz.newInstance();
            if (bundle != null) {
                baseFragment.setArguments(bundle);
            }
            fragmentTransaction.replace(R.id.rl_container, baseFragment, clazz.getName());
            if (Utils.isStringNullOrEmpty(fragmentName)) {
                fragmentName = clazz.getName();
            }
            fragmentTransaction.addToBackStack(fragmentName);
            Utils.log("fragmentName : " + fragmentName);
            fragmentTransaction.commit();
        } catch (InstantiationException e) {
            Utils.log(e.getMessage());
        } catch (IllegalAccessException e) {
            Utils.log(e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        if (menu.isMenuShowing()) {
            showContent();
        } else {
            if (isBackEnable) {
                isBackEnable = false;
                FragmentManager fragmentManager = this.getSupportFragmentManager();
                List<Fragment> list = fragmentManager.getFragments();
                ArrayList<Fragment> fragments = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    Fragment f = list.get(i);
                    if (f != null) {
                        fragments.add(f);
                    }
                }
                if (fragments.size() > 1) {
                    doBack();
                } else {
                    final Dialog alertDialog = new AlertDialog.Builder(this).
                            setTitle(R.string.msg_main_application_exit_tile).
                            setMessage(R.string.msg_main_application_exit_message).
                            setIcon(R.drawable.ic_launcher).
                            setNegativeButton(R.string.common_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).
                            setPositiveButton(R.string.common_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Utils.removeFromSP(MainActivity.this, Constants.KEY_SP_LOGIN_USER_NAME);
                                    FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
                                    List<Fragment> list = fragmentManager.getFragments();
                                    if (list != null) {
                                        for (int i = 0; i < list.size(); i++) {
                                            fragmentManager.popBackStack();
                                        }
                                    }
                                    MainActivity.this.finish();
                                    IteeApplication.getInstance().AppExit();

                                }
                            }).create();
                    alertDialog.show();
                }
                setBackKeyEnable();
            }
        }
    }

    private void getProductCount() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(this));

        HttpManager<JsonProductCount> hm = new HttpManager<JsonProductCount>(this) {
            @Override
            public void onJsonSuccess(JsonProductCount jo) {
                if (Constants.STR_1.equals(jo.getExistFlag())) {
                    MainActivity.this.changeFragment(ShoppingGoodsListFragment.class);
                } else {
                    MainActivity.this.changeFragment(ShopsListFragment.class);
                }
                rlTotalContainer.removeView(waitingView);
            }

            @Override
            public void onJsonError(VolleyError error) {
                rlTotalContainer.removeView(waitingView);
            }
        };
        hm.startGet(this, ApiManager.HttpApi.ProductCount, params);
    }

    private void setBackKeyEnable() {
        BackKeyHandler handler = new BackKeyHandler(this);
        handler.sendMessageDelayed(new Message(), 500);
    }

    public void doBack() {
        Utils.hideKeyboard(this);
        boolean isContinueBack = true;
        if (this.onBackListener != null) {
            isContinueBack = onBackListener.preDoBack();
        }
        if (isContinueBack) {
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            List<Fragment> list = fragmentManager.getFragments();
            Fragment fragment = list.get(list.size() - 1);
            if (list.size() > 0 && fragment != null) {
                if (fragment instanceof BaseFragment) {
                    BaseFragment baseFragment = (BaseFragment) fragment;
                    HttpManager.cancelRequestWithFragmentTag(baseFragment.getClass().getName(), this);
                }
            }
            if (list.size() > 1) {
                if (list.size() == 2 && list.get(0) == null) {
                    int currentSelectedIdx = smAdapter.getCurrentSelectedIdx();
                    SlidingMenuItem smi = smAdapter.getItem(currentSelectedIdx);
                    int menuId = smi.getMenuId();
                    doSelectMenu(menuId);
                } else {
                    fragmentManager.popBackStack();
                }
            }
            if (onBackListener != null) {
                setOnBackListener(null);
            }
        }
    }

    public void doBackWithRefresh() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(TransKey.COMMON_REFRESH, true);
        doBackWithReturnValue(bundle);
    }

    public void doBackWithRefresh(Class<? extends BaseFragment> clazz) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(TransKey.COMMON_REFRESH, true);
        doBackWithReturnValue(bundle, clazz);
    }

    public void doBackWithReturnValue(Bundle bundle) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        List<Fragment> li = fragmentManager.getFragments();
        int count = li.size();
        while (count > 0 && li.get(count - 1) == null) {
            count -= 1;
        }
//
//        if (count > 1) {
//            Fragment lastFragment;// = (BaseFragment) li.get(1);
//
//            if (count - 2 == 1) {
//                lastFragment = li.get(0);
//            } else {
//                lastFragment = li.get(count - 2);
//            }
//
//            if (bundle != null && lastFragment != null) {
//                if (lastFragment instanceof BaseFragment) {
//                    ((BaseFragment) lastFragment).setReturnValues(bundle);
//                }
//            }
//        }
//        fragmentManager.popBackStack();
        List<Fragment> list = fragmentManager.getFragments();
        Fragment fragment = list.get(list.size() - 1);
        if (list.size() > 0 && fragment != null) {
            if (fragment instanceof BaseFragment) {
                BaseFragment baseFragment = (BaseFragment) fragment;
                HttpManager.cancelRequestWithFragmentTag(baseFragment.getClass().getName(), this);
            }
        }
        if (list.size() > 1) {
            Fragment lastFragment;// = (BaseFragment) li.get(1);
            if (count - 2 == 1) {
                lastFragment = li.get(0);
            } else {
                lastFragment = li.get(count - 2);
            }
            if (bundle != null && lastFragment != null) {
                if (lastFragment instanceof BaseFragment) {
                    ((BaseFragment) lastFragment).setReturnValues(bundle);
                }
            }
            if (list.size() == 2 && list.get(0) == null) {
                int currentSelectedIdx = smAdapter.getCurrentSelectedIdx();
                SlidingMenuItem smi = smAdapter.getItem(currentSelectedIdx);
                int menuId = smi.getMenuId();
                doSelectMenu(menuId);
            } else {
                fragmentManager.popBackStack();
            }
        }
    }

    public void doBackWithReturnValue(Bundle bundle, Class<? extends BaseFragment> clazz) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        BaseFragment lastFragment = (BaseFragment) fragmentManager.findFragmentByTag(clazz.getName());

        if (bundle != null && lastFragment != null) {
            lastFragment.setReturnValues(bundle);
        }
        fragmentManager.popBackStack();
    }


    public void doBackWithSegmentReturnValue(Bundle bundle, Class<? extends BaseFragment> clazz) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        BaseFragment lastFragment = (BaseFragment) fragmentManager.findFragmentByTag(clazz.getName());

        if (bundle != null && lastFragment != null) {
            lastFragment.setReturnValues(bundle);
        }
        fragmentManager.popBackStack(clazz.getName(),0);
    }

    public void doLogout() {
        Utils.removeFromSP(MainActivity.this, Constants.KEY_SP_LOGIN_INFO);
        Utils.popActivity(MainActivity.this, LoginActivity.class, true);
        MainActivity.this.finish();
    }

    private void removeMenuSelectedState(View menuView) {
        ImageView ivSelectedMark = (ImageView) menuView.findViewById(R.id.iv_menu_selected_mark);
        ivSelectedMark.setBackgroundColor(0);
        menuView.setBackground(null);
    }

    public void showContent() {
        Utils.hideKeyboard(this);
        menu.showContent();
    }

    public void showMenu() {
        Utils.hideKeyboard(this);
        menu.showMenu();
    }

    private void doSelectTeeTimes() {
        MainActivity.this.changeFragment(TeeTimePageListFragment.class);
    }

    private void doSelectAdministrationEditFragment() {
        MainActivity.this.changeFragment(AdministrationEditFragment.class);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (menu.isMenuShowing()) {
                showContent();
            } else {
                showMenu();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onOptionsMenuClosed(Menu m) {
        if (menu.isMenuShowing()) {
            showContent();
        } else {
            showMenu();
        }
        super.onOptionsMenuClosed(m);
    }

    public void setOnBackListener(BaseFragment.OnPopBackListener onBackListener) {
        this.onBackListener = onBackListener;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == QuickScanQrCodeActivity.SCANNING_GREQUEST_CODE) {
                String bookingCode = data.getExtras().getString("bookingCode");
                if (Utils.isStringNotNullOrEmpty(bookingCode)) {
                    Fragment fragment = getVisibleFragment();
                    if (WelcomeFragment.class.getName().equals(fragment.getClass().getName())){
                        WelcomeFragment welcomeFragment  =(WelcomeFragment)fragment;
                        welcomeFragment.refreshLayout(bookingCode);
                    }else{
                        Bundle bundle = new Bundle();
                        bundle.putString(TransKey.BOOKING_ORDER_NO, bookingCode);
                        MainActivity.this.pushFragment(WelcomeFragment.class, bundle);
                    }
                }
            }
        }
    }

    public boolean isBackEnable() {
        return isBackEnable;
    }

    public void setIsBackEnable(boolean isBackEnable) {
        this.isBackEnable = isBackEnable;
    }

    static class BackKeyHandler extends Handler {
        private final WeakReference<MainActivity> mainActivityWeakReference;

        public BackKeyHandler(MainActivity activity) {
            mainActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity mainActivity = mainActivityWeakReference.get();
            if (!mainActivity.isBackEnable()) {
                mainActivity.setIsBackEnable(true);
            }
        }
    }
}
