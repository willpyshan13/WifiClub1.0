/**
 * Copyright 2013 Mani Selvaraj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.situne.itee.manager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.RetryPolicy;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.common.constant.Constants;

/**
 * MultipartRequest - To handle the large file uploads.
 * Extended from JSONRequest. You might want to change to StringRequest based on your response type.
 *
 * @author Mani Selvaraj
 */
public class MultiPartStringRequest extends IteeJsonRequest implements MultiPartRequest {

    private final Response.Listener<JSONObject> mListener;
    /* To hold the parameter name and the File to upload */
    private Map<String, byte[]> fileUploads = new HashMap<>();

    /* To hold the parameter name and the string content to upload */
    private Map<String, String> stringUploads = new HashMap<String, String>();

    /**
     * Creates a new request with the given method.
     *
     * @param method        the request {@link com.android.volley.Request.Method} to use
     * @param url           URL to fetch the string at
     * @param listener      Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public MultiPartStringRequest(int method, String url, Response.Listener<JSONObject> listener,
                                  ErrorListener errorListener) {
        super(method, url, null, listener, errorListener);
        mListener = listener;
    }


    public void addFileUpload(String param, byte[] data) {
        fileUploads.put(param, data);
    }

    public void addStringUpload(String param, String content) {
        stringUploads.put(param, content);
    }

    /**
     * 要上传的文件
     */
    public Map<String, byte[]> getFileUploads() {
        return fileUploads;
    }

    /**
     * 要上传的参数
     */
    public Map<String, String> getStringUploads() {
        return stringUploads;
    }


//    @Override
//    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//        String parsed;
//        try {
//            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//        } catch (UnsupportedEncodingException e) {
//            parsed = new String(response.data);
//        }
//        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
//    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        return super.parseNetworkResponse(response);
    }

//    @Override
//    protected void deliverResponse(String response) {
//        if (mListener != null) {
//            mListener.onResponse(response);
//        }
//    }

    /**
     * 空表示不上传
     */
    public String getBodyContentType() {
        return null;
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        return new DefaultRetryPolicy(Constants.SOCKET_TIMEOUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }
}