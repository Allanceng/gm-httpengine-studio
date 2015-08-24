package org.gemini.httpengine.library;

import android.text.TextUtils;

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
    private WeakReference<OnProgressUpdateListener> onProgressUpdateListener;
    private HttpRequestParser requestParser;

    private FormUrlEncodedParser formUrlEncodedParser = new FormUrlEncodedParser();

    private GMHttpRequest() {
        this.isCanceled = false;
    }

    public String getUrl() throws IOException {
        if(GMConfig.enableRESTfulSupport) {
            this.replaceRegexForRESTUri();
        }
        String url = this.url;

        byte[] data = formUrlEncodedParser.parse(httpParameters);
        if (null != data) {
            url += "?" + new String(data);
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

    public OnResponseListener getOnResponseListener() {
        return onResponseListener.get();
    }

    public void setOnResponseListener(OnResponseListener responseListener) {
        this.onResponseListener = new WeakReference<>(responseListener);
    }

    public OnProgressUpdateListener getOnProgressUpdateListener() {
        return onProgressUpdateListener == null? null : onProgressUpdateListener.get();
    }

    public void setOnProgressUpdateListener(OnProgressUpdateListener onProgressUpdateListener) {
        this.onProgressUpdateListener = new WeakReference<>(onProgressUpdateListener);
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

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public Map<String, String> getCookies() {
        return this.cookies;
    }

    public String getCookie(String key) {
        return this.cookies.get(key);
    }

    public void cancel() {
        this.isCanceled = true;
    }

    public Boolean isCancel() {
        return this.isCanceled;
    }

    public static class Builder {
        private GMHttpParameters httpParameters;
        private OnResponseListener responseListener;
        private String url;
        private String taskId;
        private HttpRequestParser requestParser;
        private String method;
        private Map<String, String> headers;
        private Map<String, String> cookies;
        private Map<String, Object> userData;
        private OnProgressUpdateListener progressUpdateListener;

        public Builder setHttpParameters(GMHttpParameters params) {
            this.httpParameters = params;
            return this;
        }

        public Builder setOnResponseListener(OnResponseListener l) {
            this.responseListener = l;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setTaskId(String taskId) {
            this.taskId = taskId;
            return this;
        }

        public Builder setRequestParser(HttpRequestParser requestParser) {
            this.requestParser = requestParser;
            return this;
        }

        public Builder setMethod(String method) {
            this.method = method;
            return this;
        }

        public Builder addHeader(String name, String value) {
            if (this.headers == null) {
                this.headers = new HashMap<>();
            }
            this.headers.put(name, value);
            return this;
        }

        public Builder addCookies(String name, String value) {
            if (this.cookies == null) {
                this.cookies = new HashMap<>();
            }
            this.cookies.put(name, value);
            return this;
        }

        public Builder setHeader(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder setCookies(Map<String, String> cookies) {
            this.cookies = cookies;
            return this;
        }

        public Builder setUserData(Map<String, Object> userData) {
            this.userData = userData;
            return this;
        }

        public Builder setOnProgressUpdateListener(OnProgressUpdateListener l) {
            this.progressUpdateListener = l;
            return this;
        }

        public GMHttpRequest build() {
            GMHttpRequest httpRequest = new GMHttpRequest();

            this.method = TextUtils.isEmpty(this.method) ? HttpMethod.HTTP_GET : this.method;
            this.httpParameters = this.httpParameters == null ? new GMHttpParameters() : this.httpParameters;
            this.url = TextUtils.isEmpty(this.url) ? "" : this.url;
            this.requestParser = this.requestParser == null ? (this.httpParameters.isBinaryContent() ? new MultiPartParser() : new FormUrlEncodedParser())
                                                              : this.requestParser;
            httpRequest.setMethod(this.method);
            httpRequest.setTaskId(this.taskId);
            httpRequest.setOnResponseListener(this.responseListener);
            httpRequest.setUserData(this.userData);
            httpRequest.setCookies(this.cookies);
            httpRequest.setHeaders(this.headers);
            httpRequest.setOnProgressUpdateListener(this.progressUpdateListener);
            httpRequest.setRequestParser(this.requestParser);
            httpRequest.setUrl(this.url);
            httpRequest.setHttpParameters(this.httpParameters);

            return httpRequest;
        };
    };
}
