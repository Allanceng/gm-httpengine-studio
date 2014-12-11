package org.gemini.httpengine.library;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Request Object for http engine
 *
 * add RESTful support
 * @author Gemini
 * @version 2.0
 */
public class GMHttpRequest {

    private String url;
    private String taskId;
    private String method;
    private Boolean isCanceled;

    private GMHttpParameters httpParameters;

    private Map<String, Object> userData;
    private Map<String, String> headers;
    private Map<String, String> cookies;
    private WeakReference<OnResponseListener> onResponseListener = new WeakReference<OnResponseListener>(null);
    private OnProgressUpdateListener onProgressUpdateListener;
    private HttpRequestParser requestParser;
    private GMModelParser modelParser;

    public GMHttpRequest() {
        this.isCanceled = false;
        this.headers = new HashMap<String, String>();
        this.cookies = new HashMap<String, String>();
        this.method = HttpMethod.HTTP_GET;
        this.modelParser = new GMModelParser();
    }

    public GMHttpRequest(String url, GMHttpParameters httpParameters) {
        this();
        this.url = url;
        this.httpParameters = httpParameters;
    }

    public String getUrl() throws IOException {
        String url = this.url;
        if(Config.enableRESTfulSupport) {
            this.replaceRegexForRESTUri();
        }
        if (method.equalsIgnoreCase(HttpMethod.HTTP_GET)  ||
            method.equalsIgnoreCase(HttpMethod.HTTP_DELETE)) {
            FormUrlEncodedParser parser = new FormUrlEncodedParser();
            byte[] data = parser.parse(httpParameters);
            if (null != data) {
                url += "?" + new String(data);
            }
        }
        return url;
    }

    private String replaceRegexForRESTUri() {
        GMHttpParameters httpParameters = this.getHttpParameters();
        String url = this.url;
        String regex = "/:(\\w+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        while (matcher.find()) {
            String parameterName = matcher.group(1);
            String parameterValue = httpParameters.getParameter(parameterName).toString();
            url = url.replace(":" + matcher.group(1), parameterValue);
            httpParameters.removeParameter(parameterName);
        }
        this.url = url;
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

    public void setHttpParameters(GMHttpParameters httpParameters) {
        this.httpParameters = httpParameters;
        if (httpParameters.isBinaryContent()) {
            this.requestParser = new MultiPartParser();
        } else {
            this.requestParser = new FormUrlEncodedParser();
        }
    }

    public GMHttpParameters getHttpParameters() {
        return httpParameters;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContentType() {
        return this.requestParser.getContentType();
    }

    public long getContentLength() {
        return this.getRequestParser().getContentLength();
    }

    public OnResponseListener getResponseListener() {
        return onResponseListener.get();
    }

    public void setOnResponseListener(OnResponseListener responseListener) {
        this.onResponseListener = new WeakReference<OnResponseListener>(responseListener);
    }

    public OnProgressUpdateListener getOnProgressUpdateListener() {
        return onProgressUpdateListener;
    }

    public void setOnProgressUpdateListener(OnProgressUpdateListener onProgressUpdateListener) {
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

    public String getHeader(String key) {
        return this.headers.get(key);
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void addRequestProperty(String key, String value) {
        this.cookies.put(key, value);
    }

    public Map<String, String> getRequestProperties() {
        return this.cookies;
    }

    public String getRequestProperty(String key) {
        return this.cookies.get(key);
    }

    public void cancel() {
        this.isCanceled = true;
    }

    public Boolean isCancel() {
        return this.isCanceled;
    }

    public void parseParametersFromModel(Object requestModel) {
        GMHttpParameters httpParameters = this.modelParser.parseModel(requestModel);
        this.setHttpParameters(httpParameters);
    }

}
