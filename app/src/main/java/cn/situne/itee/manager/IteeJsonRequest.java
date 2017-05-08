package cn.situne.itee.manager;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;

public class IteeJsonRequest extends JsonObjectRequest {

    private Map<String, String> params;

    public IteeJsonRequest(int method, String url, Map<String, String> params, Response.Listener<JSONObject> listener,
                           Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.params = params;
    }

    public static void startNetAPI(RequestQueue mQueue, ApiManager.HttpApi api, Map<String, String> params,
                                   final Response.Listener<JSONObject> listener, Response.ErrorListener errorListener,
                                   Context mContext, BaseFragment fragment) {
        startNetAPI(mQueue, api, params, Method.POST, listener, errorListener, mContext, fragment);
    }

    public static void startDeleteNetAPI(RequestQueue mQueue, ApiManager.HttpApi api, Map<String, String> params,
                                         final Response.Listener<JSONObject> listener, Response.ErrorListener errorListener
            , Context mContext, BaseFragment fragment) {
        startNetAPI(mQueue, api, params, Method.DELETE, listener, errorListener, mContext, fragment);
    }

    public static void startPutNetAPI(RequestQueue mQueue, ApiManager.HttpApi api, Map<String, String> params,
                                      final Response.Listener<JSONObject> listener, Response.ErrorListener errorListener
            , Context mContext, BaseFragment fragment) {
        startNetAPI(mQueue, api, params, Method.PUT, listener, errorListener, mContext, fragment);
    }

    public static void startGetNetAPI(RequestQueue mQueue, ApiManager.HttpApi api, Map<String, String> params,
                                      final Response.Listener<JSONObject> listener, Response.ErrorListener errorListener
            , Context mContext, BaseFragment fragment) {

        startNetAPI(mQueue, api, params, Method.GET, listener, errorListener, mContext, fragment);
    }

    private static void startNetAPI(RequestQueue mQueue, ApiManager.HttpApi api, Map<String, String> params,
                                    int method, final Response.Listener<JSONObject> listener,
                                    Response.ErrorListener errorListener, Context mContext, BaseFragment fragment) {
        String url;
        if (api == ApiManager.HttpApi.CheckVersion) {
            url = Constants.CHECK_VERSION_URL;
        } else {
            url = ApiManager.getUrlWithNetApi(api, mContext);
        }

        if (method == Method.GET || method == Method.DELETE) {
            StringBuilder encodedParams = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(entry.getKey());
                encodedParams.append('=');
                encodedParams.append(entry.getValue());
                encodedParams.append('&');
            }
            url += Constants.STR_QUESTION + encodedParams.toString();
        }
        IteeJsonRequest request = new IteeJsonRequest(method, url, params, listener, errorListener);
        if (fragment != null) {
            request.setTag(fragment.getClass().getName());
        }
        request.setRetryPolicy(request.getRetryPolicy());
        mQueue.add(request);
    }

    @Override
    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
    }

    @Override
    public byte[] getBody() {
        Map<String, String> params = this.params;
        if (params != null && params.size() > 0) {
            return encodeParameters(params, getParamsEncoding());
        }
        return null;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        return super.parseNetworkResponse(response);
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        Utils.debug(volleyError.toString());
        return super.parseNetworkError(volleyError);
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        return new DefaultRetryPolicy(Constants.SOCKET_TIMEOUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }

    private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                if (entry.getValue() == null) {
                    Log.e("DJZ", entry.getKey());
                }
                Utils.log("----------------");
                Utils.log("key : " + URLEncoder.encode(entry.getKey(), paramsEncoding)
                        + "\nValue : " + URLEncoder.encode(entry.getValue(), paramsEncoding));
                Utils.log("----------------");
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append('&');
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }
}
