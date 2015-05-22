package org.gemini.httpengine.examples;

import org.gemini.httpengine.annotation.Path;
import org.gemini.httpengine.library.GMHttpParameters;
import org.gemini.httpengine.library.GMHttpRequest;
import org.gemini.httpengine.library.GMHttpService;
import org.gemini.httpengine.library.HttpMethod;
import org.gemini.httpengine.library.OnResponseListener;

/**
 * Created by geminiwen on 14-3-25.
 */
public class OtherAPI {
    private GMHttpService mService;

    public OtherAPI() {
        mService = GMHttpService.getInstance();
    }

    public void login(String username,String password,OnResponseListener l) {
        GMHttpParameters httpParameters = new GMHttpParameters();
        GMHttpRequest.Builder builder = new GMHttpRequest.Builder();
        builder.setUrl("http://www.baidu.com/s?wd=%E6%9D%9C%E7%91%9E%E9%9B%AA");
        builder.setHttpParameters(httpParameters);
        builder.setOnResponseListener(l);
        mService.executeHttpMethod(builder.build());
    }

    public void cookie(OnResponseListener l) {
        GMHttpParameters httpParameters = new GMHttpParameters();
        GMHttpRequest.Builder builder = new GMHttpRequest.Builder();
        builder.setUrl("https://www.baidu.com");
        builder.setHttpParameters(httpParameters);
        builder.setMethod(HttpMethod.HTTP_GET);
        builder.setOnResponseListener(l);
        mService.executeHttpMethod(builder.build());
    }

    public void image(OnResponseListener l, String image) {
        GMHttpParameters httpParameters = new GMHttpParameters();
        GMHttpRequest.Builder builder = new GMHttpRequest.Builder();
        builder.setUrl("http://dayi.im/api/app/teacher/generate_auth_code/");
        builder.setHttpParameters(httpParameters);
        builder.setMethod(HttpMethod.HTTP_GET);
        builder.setOnResponseListener(l);
        mService.executeHttpMethod(builder.build());
    }

    public void testArray() {
        GMHttpParameters httpParameters = new GMHttpParameters();
        GMHttpRequest.Builder builder = new GMHttpRequest.Builder();
        httpParameters.setParameter("username", "hello");
        httpParameters.setParameter("ids", new int[]{1, 2, 3, 4});

        builder.setUrl("http://www.baidu.com/")
               .setHttpParameters(httpParameters);
        mService.executeHttpMethod(builder.build());
    }

}
