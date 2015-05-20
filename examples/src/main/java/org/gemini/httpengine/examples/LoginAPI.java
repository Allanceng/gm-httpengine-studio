package org.gemini.httpengine.examples;

import android.content.res.AssetManager;

import org.gemini.httpengine.library.GMHttpParameters;
import org.gemini.httpengine.library.GMHttpRequest;
import org.gemini.httpengine.library.GMHttpService;
import org.gemini.httpengine.library.HttpMethod;
import org.gemini.httpengine.library.OnResponseListener;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by geminiwen on 14-3-25.
 */
public class LoginAPI {
    private GMHttpService mService;

    public LoginAPI() {
        mService = GMHttpService.getInstance();
    }

    public void login(String username,String password,OnResponseListener l) {
        GMHttpParameters httpParameters = new GMHttpParameters();
        GMHttpRequest httpRequest = new GMHttpRequest();
        httpRequest.setUrl("http://www.baidu.com/s?wd=%E6%9D%9C%E7%91%9E%E9%9B%AA");
        httpRequest.setHttpParameters(httpParameters);
        httpRequest.setOnResponseListener(l);
        mService.executeHttpMethod(httpRequest);
    }

    public void cookie(OnResponseListener l) {
        GMHttpParameters httpParameters = new GMHttpParameters();
        GMHttpRequest httpRequest = new GMHttpRequest();
        httpRequest.setUrl("https://www.baidu.com");
        httpRequest.setHttpParameters(httpParameters);
        httpRequest.setMethod(HttpMethod.HTTP_GET);
        httpRequest.setOnResponseListener(l);
        mService.executeHttpMethod(httpRequest);
    }

    public void image(OnResponseListener l) {
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
