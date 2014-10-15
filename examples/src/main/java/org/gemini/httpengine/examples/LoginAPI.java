package org.gemini.httpengine.examples;

import android.content.res.AssetManager;

import org.gemini.httpengine.library.GMHttpParameters;
import org.gemini.httpengine.library.GMHttpRequest;
import org.gemini.httpengine.library.GMHttpService;
import org.gemini.httpengine.library.HttpMethod;
import org.gemini.httpengine.library.OnResponseListener;

import java.io.File;

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
        httpParameters.setParameter("password", password);
        httpParameters.setParameter("username", username);
        httpRequest.setUrl("http://cloud-monitor.seekyun.com/api/user/login");
        httpRequest.setHttpParameters(httpParameters);
        httpRequest.setOnResponseListener(l);
        mService.executeHttpMethod(httpRequest);
    }

    public void uploadImages(String token, OnResponseListener l) {
        GMHttpParameters httpParameters = new GMHttpParameters();
        GMHttpRequest httpRequest = new GMHttpRequest();
        httpParameters.setParameter("token", token);
        httpParameters.setParameter("face", new File("/sdcard/LocalFlowService.txt"));
        httpRequest.setUrl("http://api.qiaqia.tv/api/user/changeFace");
        httpRequest.setHttpParameters(httpParameters);
        httpRequest.setOnResponseListener(l);
        httpRequest.setMethod(HttpMethod.HTTP_POST);
        mService.executeHttpMethod(httpRequest);
    }
}
