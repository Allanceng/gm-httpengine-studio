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

    public void testArray() {
        GMHttpParameters httpParameters = new GMHttpParameters();
        GMHttpRequest httpRequest = new GMHttpRequest();
        httpParameters.setParameter("username", "hello");
        httpParameters.setParameter("ids", new int[]{1, 2, 3, 4});
        httpRequest.setUrl("http://www.baidu.com/");
        httpRequest.setHttpParameters(httpParameters);
        mService.executeHttpMethod(httpRequest);
    }

    public void testList() {
        ArrayList<Integer> list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        GMHttpParameters httpParameters = new GMHttpParameters();
        GMHttpRequest httpRequest = new GMHttpRequest();
        httpParameters.setParameter("username", "hello");
        httpParameters.setParameter("ids", list);
        httpRequest.setUrl("http://www.baidu.com/");
        httpRequest.setHttpParameters(httpParameters);
        mService.executeHttpMethod(httpRequest);
    }
}
