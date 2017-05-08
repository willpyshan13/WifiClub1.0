package cn.situne.itee.manager;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.baoyz.actionsheet.ActionSheet;
import com.umeng.update.UmengUpdateAgent;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.view.WaitingView;

public abstract class HttpManager<T extends BaseJsonObject> {

    private static final String KEY_CONTEXT = "Context";
    private static final String KEY_API = "Api";
    private static final String KEY_PARAMS = "Params";
    private static final String KEY_METHOD_GET = "MethodGet";
    private static final String KEY_QUEUE = "Queue";

    private static final int HANDLER_UPDATE_UI_ADD_WAITING_VIEW = 100004;
    private static final int HANDLER_UPDATE_UI_ENABLE = 100005;
    private static final int HANDLER_UPDATE_UI_DISABLE = 100006;
    private static RequestQueue mQueue;
    RelativeLayout rlContentContainer;
    private WaitingView waitingView;
    private BaseFragment mFragment;
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (waitingView != null) {
                TextView tvItee = (TextView) waitingView.findViewById(R.id.tv_itee);
                ProgressBar pbWaiting = (ProgressBar) waitingView.findViewById(R.id.pb_waiting);
                boolean isMethodGet = (boolean) waitingView.getTag();
                if (isMethodGet) {
                    RelativeLayout rlTryAgainContainer = (RelativeLayout) waitingView.findViewById(R.id.rl_try_again_container);
                    if (rlTryAgainContainer != null) {
                        rlTryAgainContainer.setVisibility(View.VISIBLE);
                        tvItee.setText(StringUtils.EMPTY);
                        tvItee.setBackgroundResource(R.drawable.icon_waiting);
                        pbWaiting.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (rlContentContainer != null) {
                        rlContentContainer.removeView(waitingView);
                    }
                }
            }
            if (mFragment != null) {
                Utils.showShortToast(mFragment.getActivity(), R.string.msg_common_network_error);
                mFragment.onAfterApi();
            }
            onJsonError(error);
            changeActionBarControlState(true);
        }
    };
    private FragmentActivity mActivity;
    ActionSheet.ActionSheetListener listenerLogout = new ActionSheet.ActionSheetListener() {
        @Override
        public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
            if (mFragment != null) {
                mFragment.getBaseActivity().doLogout();
            } else {
                if (mActivity != null) {
                    mActivity.finish();
                }
            }
        }

        @Override
        public void onDismissWithCancelButton(ActionSheet actionSheet) {

        }

        @Override
        public void onOtherButtonClick(ActionSheet actionSheet, int index) {

        }
    };
    @SuppressWarnings("unchecked")
    Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            T to = null;
            Class genericClazz = getGenericType(0);
            try {
                Constructor<T> cs = genericClazz.getConstructor(JSONObject.class);
                to = cs.newInstance(response);
            } catch (NoSuchMethodException e) {
                Utils.log(e.getMessage());
            } catch (IllegalAccessException e) {
                Utils.log(e.getMessage());
            } catch (InvocationTargetException e) {
                Utils.log(e.getMessage());
            } catch (InstantiationException e) {
                Utils.log(e.getMessage());
            }

            if (to != null) {
                if (mFragment == null) {    // not login
                    if (mActivity != null) {
                        if (to.getTokenStatus() != null) {
                            String msg = to.getTokenStatusInfo();

                            if (Utils.isStringNotNullOrEmpty(to.getRefreshToken())){
                                AppUtils.saveRefreshToken(mActivity,to.getRefreshToken());
                            }
                            if (Utils.isStringNotNullOrEmpty(to.getAccessToken())){
                                AppUtils.saveToken(mActivity,to.getRefreshToken());
                            }

                            if (to.getTokenStatus() == -1) {
                                mActivity.setTheme(com.baoyz.actionsheet.R.style.ActionSheetStyleIOS7);
                                ActionSheet.createBuilder(mActivity, mActivity.getSupportFragmentManager())
                                        .setCancelButtonTitle(msg)
                                        .setCancelableOnTouchOutside(false).setListener(listenerLogout).show();
                            } else if (to.getTokenStatus() == 0) {
                                if (Utils.isStringNullOrEmpty(msg)) {
                                    msg = mActivity.getString(R.string.common_log_in_elsewhere_exit_now);
                                }
                                mActivity.setTheme(com.baoyz.actionsheet.R.style.ActionSheetStyleIOS7);
                                ActionSheet.createBuilder(mActivity, mActivity.getSupportFragmentManager())
                                        .setCancelButtonTitle(msg)
                                        .setCancelableOnTouchOutside(false).setListener(listenerLogout).show();
                            } else if (to.getTokenStatus() == -2) {
                                // add by zf 2015.7.27
//                                UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
//                                    @Override
//                                    public void onClick(int status) {
//                                        switch (status) {
//                                            case UpdateStatus.Update:
//                                                mActivity.moveTaskToBack(true);
//                                                break;
//                                            case UpdateStatus.Ignore:
//                                                break;
//                                            case UpdateStatus.NotNow:
//                                                break;
//                                        }
//                                    }
//                                });
                                UmengUpdateAgent.forceUpdate(mActivity);
                            } else {
                                onJsonSuccess(to);
                            }
                        }
                    } else {
                        onJsonSuccess(to);
                    }
                } else {
                    FragmentManager fragmentManager = mFragment.getFragmentManager();

                    if (fragmentManager != null) {
                        if (to.getTokenStatus() != null) {
                            String msg = to.getTokenStatusInfo();
                            if (to.getTokenStatus() == -1) {
                                mFragment.getActivity().setTheme(com.baoyz.actionsheet.R.style.ActionSheetStyleIOS7);
                                ActionSheet.createBuilder(mFragment.getActivity(), fragmentManager)
                                        .setCancelButtonTitle(msg)
                                        .setCancelableOnTouchOutside(false).setListener(listenerLogout).show();
                            } else if (to.getTokenStatus() == 0) {
                                if (Utils.isStringNullOrEmpty(msg)) {
                                    msg = mFragment.getString(R.string.common_log_in_elsewhere_exit_now);
                                }
                                mFragment.getActivity().setTheme(com.baoyz.actionsheet.R.style.ActionSheetStyleIOS7);
                                ActionSheet.createBuilder(mFragment.getActivity(), fragmentManager)
                                        .setCancelButtonTitle(msg)
                                        .setCancelableOnTouchOutside(false).setListener(listenerLogout).show();
                            } else if (to.getTokenStatus() == -2) {
                                // add by zf 2015.7.27
//                                UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
//
//                                    @Override
//                                    public void onClick(int status) {
//                                        switch (status) {
//                                            case UpdateStatus.Update:
//                                                mFragment.getBaseActivity().moveTaskToBack(true);
//                                                break;
//                                            case UpdateStatus.Ignore:
//                                                break;
//                                            case UpdateStatus.NotNow:
//                                                break;
//                                        }
//                                    }
//                                });
                                UmengUpdateAgent.forceUpdate(mFragment.getActivity());
                            } else {
                                onJsonSuccess(to);
                            }
                        }
                    }
                }
                removeWaiting();
                changeActionBarControlState(true);
            }
            if (mFragment != null) {
                mFragment.onAfterApi();
            }
        }
    };

    public HttpManager(BaseFragment mFragment) {
        this.mFragment = mFragment;
        if (mFragment != null) {
            rlContentContainer = mFragment.getRlContentContainer();
        }
    }

    public HttpManager(FragmentActivity mActivity) {
        this.mActivity = mActivity;
    }

    public HttpManager(boolean isLogin) {
    }

    private static RequestQueue getQueue(Context mContext) {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(mContext, new MultiPartStack());
        }
        return mQueue;
    }

    public static void cancelRequestWithFragmentTag(String fragmentTag, Context mContext) {
        RequestQueue requestQueue = getQueue(mContext);
        requestQueue.cancelAll(fragmentTag);
    }

    public void start(final Context mContext, ApiManager.HttpApi api, Map<String, String> params) {
        start(mContext, api, params, true);
    }

    public void startPut(final Context mContext, ApiManager.HttpApi api, Map<String, String> params) {
        startPut(mContext, api, params, true);
    }

    public void startGet(final Context mContext, final ApiManager.HttpApi api, final Map<String, String> params) {
        startGet(mContext, api, params, true);
    }

    public void startDelete(final Context mContext, ApiManager.HttpApi api, Map<String, String> params) {
        startDelete(mContext, api, params, true);
    }

    public void start(final Context mContext, ApiManager.HttpApi api, Map<String, String> params, boolean isNeedWaiting) {
        RequestQueue mQueue = getQueue(mContext);
        if (isNeedWaiting) {
            addWaitingViewHandler(mContext, false, mQueue, api, params);
        } else {
            disableActionBar(mContext);
        }


        IteeJsonRequest.startNetAPI(mQueue, api, params, successListener, errorListener, mContext, mFragment);
    }

    public void startPut(final Context mContext, ApiManager.HttpApi api, Map<String, String> params, boolean isNeedWaiting) {
        RequestQueue mQueue = getQueue(mContext);
        if (isNeedWaiting) {
            addWaitingViewHandler(mContext, false, mQueue, api, params);
        } else {
            disableActionBar(mContext);
        }
        IteeJsonRequest.startPutNetAPI(mQueue, api, params, successListener, errorListener, mContext, mFragment);
    }

    public void startGet(final Context mContext, final ApiManager.HttpApi api, final Map<String, String> params, boolean isNeedWaiting) {
        final RequestQueue mQueue = getQueue(mContext);
        if (isNeedWaiting) {
            addWaitingViewHandler(mContext, false, mQueue, api, params);
        } else {
            disableActionBar(mContext);
        }
        IteeJsonRequest.startGetNetAPI(mQueue, api, params, successListener, errorListener, mContext, mFragment);
    }

    public void startDelete(final Context mContext, ApiManager.HttpApi api, Map<String, String> params, boolean isNeedWaiting) {
        RequestQueue mQueue = getQueue(mContext);
        if (isNeedWaiting) {
            addWaitingViewHandler(mContext, false, mQueue, api, params);
        } else {
            disableActionBar(mContext);
        }
        IteeJsonRequest.startDeleteNetAPI(mQueue, api, params, successListener, errorListener, mContext, mFragment);
    }

    public void uploadFile(Context mContext, ApiManager.HttpApi api, HashMap<String, byte[]> files, HashMap<String, String> params) {
        RequestQueue mQueue = getQueue(mContext);
        String url = ApiManager.getUrlWithNetApi(api, mContext);
        addWaitingViewHandler(mContext, false, mQueue, api, params);
        addPutUploadFileRequest(url, files, params, mQueue);
    }

    public void addPutUploadFileRequest(final String url,
                                        final HashMap<String, byte[]> files, final HashMap<String, String> params,
                                        final RequestQueue mQueue) {
        if (null == url) {
            return;
        }

        MultiPartStringRequest multiPartRequest = new MultiPartStringRequest(
                Request.Method.POST, url, successListener, errorListener) {

            @Override
            public Map<String, byte[]> getFileUploads() {
                return files;
            }

            @Override
            public Map<String, String> getStringUploads() {
                return params;
            }

        };
        multiPartRequest.setRetryPolicy(multiPartRequest.getRetryPolicy());

        mQueue.add(multiPartRequest);
    }

    private HashMap<String, Object> generateArguments(Context mContext, boolean isMethodGet, final RequestQueue mQueue, final ApiManager.HttpApi api, final Map<String, String> params) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(KEY_CONTEXT, mContext);
        args.put(KEY_API, api);
        args.put(KEY_PARAMS, params);
        args.put(KEY_METHOD_GET, isMethodGet);
        args.put(KEY_QUEUE, mQueue);
        return args;
    }

    @SuppressWarnings("unchecked")
    private void addWaitingView(HashMap<String, Object> args, final Context mContext) {
        final ApiManager.HttpApi api = (ApiManager.HttpApi) args.get(KEY_API);
        final Map<String, String> params = (Map<String, String>) args.get(KEY_PARAMS);
        boolean isMethodGet = (boolean) args.get(KEY_METHOD_GET);
        final RequestQueue mQueue = (RequestQueue) args.get(KEY_QUEUE);
        if (rlContentContainer != null && mFragment.getActivity() != null) {
            mFragment.onBeforeApi();
            changeActionBarControlState(false);
            LayoutInflater inflater = (LayoutInflater) mFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            removeWaiting();
            waitingView = (WaitingView) inflater.inflate(R.layout.view_common_waiting, null);
            rlContentContainer.addView(waitingView);

            TextView tvItee = (TextView) waitingView.findViewById(R.id.tv_itee);
            ProgressBar pbWaiting = (ProgressBar) waitingView.findViewById(R.id.pb_waiting);

            pbWaiting.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams paramsWaitingView
                    = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            waitingView.setLayoutParams(paramsWaitingView);

            tvItee.setText(R.string.app_name);
            tvItee.setBackground(null);

            waitingView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            if (isMethodGet) {
                waitingView.setBackgroundColor(mFragment.getColor(R.color.common_white));
                tvItee.setTextColor(mFragment.getColor(R.color.common_black));
            } else {
                waitingView.setBackgroundColor(mFragment.getColor(R.color.common_black));
                waitingView.setAlpha(0.4f);
                tvItee.setTextColor(mFragment.getColor(R.color.common_white));
            }

            waitingView.setTag(isMethodGet);

            TextView tvRetry = (TextView) waitingView.findViewById(R.id.tv_try_again);
            if (tvRetry != null) {
                tvRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IteeJsonRequest.startGetNetAPI(mQueue, api, params, successListener, errorListener, mContext, mFragment);
                    }
                });
            }
        }

    }

    private void removeWaiting() {
        if (rlContentContainer != null) {
            for (int i = 0; i < rlContentContainer.getChildCount(); i++) {
                View v = rlContentContainer.getChildAt(i);
                if (v instanceof WaitingView) {
                    rlContentContainer.removeView(v);
                }
            }
        }
    }

    public void addWaitingViewHandler(final Context mContext, final boolean isMethodGet, final RequestQueue mQueue, final ApiManager.HttpApi api, final Map<String, String> params) {
        params.put(JsonKey.LOGIN_REFRESH_TOKEN, AppUtils.getRefreshToken(mContext));
        HashMap<String, Object> args = generateArguments(mContext, isMethodGet, mQueue, api, params);
        Message message = new Message();
        message.what = HANDLER_UPDATE_UI_ADD_WAITING_VIEW;
        IteeHandler handler = new IteeHandler(mContext.getMainLooper(), this, args, mContext);
        handler.sendMessage(message);
    }

    private void changeActionBarControlState(boolean isEnable) {
        if (mFragment != null) {
            TextView tvRight = mFragment.getTvRight();
            if (tvRight != null) {
                tvRight.setEnabled(isEnable);
            }
        }
    }

    public abstract void onJsonSuccess(T jo);

    public abstract void onJsonError(VolleyError error);

    private Class getGenericType(int index) {
        Type genType = getClass().getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            throw new RuntimeException("Index out of bounds");
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

    private void disableActionBar(Context context) {
        Message message = new Message();
        message.what = HANDLER_UPDATE_UI_DISABLE;
        IteeHandler handler = new IteeHandler(context.getMainLooper(), this, null, context);
        handler.sendMessage(message);
    }

    static class IteeHandler extends Handler {

        WeakReference<HttpManager> mManager;
        HashMap<String, Object> arguments;
        Context mContext;

        public IteeHandler(Looper looper, HttpManager manager, HashMap<String, Object> args, Context context) {
            super(looper);
            mManager = new WeakReference<>(manager);
            arguments = args;
            mContext = context;
        }

        @Override
        public void handleMessage(Message msg) {
            HttpManager httpManager = mManager.get();
            switch (msg.what) {
                case HANDLER_UPDATE_UI_ENABLE:
                    httpManager.changeActionBarControlState(true);
                    break;
                case HANDLER_UPDATE_UI_DISABLE:
                    httpManager.changeActionBarControlState(false);
                    break;
                case HANDLER_UPDATE_UI_ADD_WAITING_VIEW:
                    if (httpManager != null) {
                        httpManager.addWaitingView(arguments, mContext);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
