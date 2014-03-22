package org.gemini.httpengine.library;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Request Object for http engine
 *
 * @author Gemini
 */
public class GMHttpRequest {

    private String url;
    private String taskId;
    private String method;
    private Boolean isCanceled;
    private GMHttpParameters httpParameters;
    private Map<String, Object> userData;
    private Map<String, String> headers;
    private WeakReference<OnResponseListener> onResponseListener;
    private OnProgressUpdateListener onProgressUpdateListener;
    private HttpRequestParser requestParser;

    public GMHttpRequest() {
        this.isCanceled = false;
        this.requestParser = new FormUrlEncodedParser();
        this.headers = new HashMap<String, String>();
        this.method = GMHttpEngine.HTTP_GET;
    }

    public GMHttpRequest(String url,
                         GMHttpParameters httpParameters) {
        this.url = url;
        this.httpParameters = httpParameters;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getUserData() {
        return userData;
    }

    public void setUserData(Map<String, Object> userData) {
        this.userData = userData;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public GMHttpParameters getHttpParameters() {
        return httpParameters;
    }

    public void setHttpParameters(GMHttpParameters httpParameters) {
        this.httpParameters = httpParameters;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContentType() {
        return this.requestParser.pareContentType();
    }

    public OnResponseListener getResponseListener() {
        return onResponseListener.get();
    }

    public void setOnResponseListener(OnResponseListener responseListener) {
        this.onResponseListener = new WeakReference<OnResponseListener>(
                responseListener);
    }

    public OnProgressUpdateListener getOnProgressUpdateListener() {
        return onProgressUpdateListener;
    }

    public void setOnProgressUpdateListener(
            OnProgressUpdateListener onProgressUpdateListener) {
        this.onProgressUpdateListener = onProgressUpdateListener;
    }

    public HttpRequestParser getRequestParser() {
        return requestParser;
    }

    public void setRequestParser(HttpRequestParser requestParser) {
        this.requestParser = requestParser;
    }

    public byte[] getHttpEntity() throws IOException {
        return this.requestParser.parse(httpParameters);
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void cancel() {
        this.isCanceled = true;
    }

    public Boolean isCancel() {
        return this.isCanceled;
    }

}
