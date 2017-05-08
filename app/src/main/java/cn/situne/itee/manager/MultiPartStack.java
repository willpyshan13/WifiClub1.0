package cn.situne.itee.manager;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HurlStack;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import cn.situne.itee.common.utils.Utils;

/**
 * @author ZhiCheng Guo
 * @version 2014年10月7日 上午11:00:52 这个Stack用于上传文件, 如果没有这个Stack, 则上传文件不成功
 */
public class MultiPartStack extends HurlStack {

    private final static String HEADER_CONTENT_TYPE = "Content-Type";

    @Override
    public HttpResponse performRequest(Request<?> request,
                                       Map<String, String> additionalHeaders) throws IOException, AuthFailureError {

        if (!(request instanceof MultiPartRequest)) {
            return super.performRequest(request, additionalHeaders);
        } else {
            return performMultiPartRequest(request, additionalHeaders);
        }
    }

    private static void addHeaders(HttpUriRequest httpRequest, Map<String, String> headers) {
        for (String key : headers.keySet()) {
            httpRequest.setHeader(key, headers.get(key));
        }
    }

    public HttpResponse performMultiPartRequest(Request<?> request,
                                                Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
        HttpUriRequest httpRequest = createMultiPartRequest(request, additionalHeaders);
        addHeaders(httpRequest, additionalHeaders);
        addHeaders(httpRequest, request.getHeaders());
        HttpParams httpParams = httpRequest.getParams();
        int timeoutMs = request.getTimeoutMs();

        if (timeoutMs != -1) {
            HttpConnectionParams.setSoTimeout(httpParams, timeoutMs);
        }
        
        /* Make a thread safe connection manager for the client */
        HttpClient httpClient = new DefaultHttpClient(httpParams);

        return httpClient.execute(httpRequest);
    }


    static HttpUriRequest createMultiPartRequest(Request<?> request,
                                                 Map<String, String> additionalHeaders) throws AuthFailureError {
        switch (request.getMethod()) {
            case Method.DEPRECATED_GET_OR_POST: {
                // This is the deprecated way that needs to be handled for backwards compatibility.
                // If the post body of request is null, then the assumption is that the request is
                // GET.  Otherwise, it is assumed that the request is a POST.
                byte[] postBody = request.getBody();
                if (postBody != null) {
                    HttpPost postRequest = new HttpPost(request.getUrl());
                    if (request.getBodyContentType() != null)
                        postRequest.addHeader(HEADER_CONTENT_TYPE, request.getBodyContentType());
                    HttpEntity entity;
                    entity = new ByteArrayEntity(postBody);
                    postRequest.setEntity(entity);
                    return postRequest;
                } else {
                    return new HttpGet(request.getUrl());
                }
            }
            case Method.GET:
                return new HttpGet(request.getUrl());
            case Method.DELETE:
                return new HttpDelete(request.getUrl());
            case Method.POST: {
                HttpPost postRequest = new HttpPost(request.getUrl());
                if (request.getBodyContentType() != null) {
                    postRequest.addHeader(HEADER_CONTENT_TYPE, request.getBodyContentType());
                }
                setMultiPartBody(postRequest, request);
                return postRequest;
            }
            case Method.PUT: {
                HttpPut putRequest = new HttpPut(request.getUrl());
                if (request.getBodyContentType() != null)
                    putRequest.addHeader(HEADER_CONTENT_TYPE, request.getBodyContentType());
                setMultiPartBody(putRequest, request);
                return putRequest;
            }
            // Added in source code of Volley library.
            case Method.PATCH: {
                HttpClientStack.HttpPatch patchRequest = new HttpClientStack.HttpPatch(request.getUrl());
                if (request.getBodyContentType() != null)
                    patchRequest.addHeader(HEADER_CONTENT_TYPE, request.getBodyContentType());
                return patchRequest;
            }
            default:
                throw new IllegalStateException("Unknown request method.");
        }
    }

    /**
     * If Request is MultiPartRequest type, then set MultipartEntity in the
     * httpRequest object.
     *
     * @param httpRequest httpRequest
     * @param request     request
     * @throws com.android.volley.AuthFailureError
     */
    private static void setMultiPartBody(HttpEntityEnclosingRequestBase httpRequest,
                                         Request<?> request) throws AuthFailureError {

        // Return if Request is not MultiPartRequest
        if (!(request instanceof MultiPartRequest)) {
            return;
        }

        // MultipartEntity multipartEntity = new
        // MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

		/* example for setting a HttpMultipartMode */
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.setCharset(Charset.forName("UTF8"));

        // Iterate the fileUploads
        Map<String, byte[]> fileUpload = ((MultiPartRequest) request).getFileUploads();
        for (Map.Entry<String, byte[]> entry : fileUpload.entrySet()) {
            builder.addPart(entry.getKey(), new ByteArrayBody(entry.getValue(), entry.getKey()));
        }

        ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
        // Iterate the stringUploads
        Map<String, String> stringUpload = ((MultiPartRequest) request).getStringUploads();
        for (Map.Entry<String, String> entry : stringUpload.entrySet()) {
            try {
                builder.addPart((entry.getKey()),
                        new StringBody(entry.getValue(), contentType));
            } catch (Exception e) {
                Utils.log(e.getMessage());
            }
        }

        httpRequest.setEntity(builder.build());
    }

}
