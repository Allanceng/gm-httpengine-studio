package org.gemini.httpengine.examples;

import org.gemini.httpengine.library.*;

public class UserAPI$$APIINJECTOR implements org.gemini.httpengine.examples.UserAPI {

    public void login(org.gemini.httpengine.library.OnResponseListener l, java.lang.String username, java.lang.String password) {
        final String FIELD_USERNAME = "username";
        final String FIELD_PASSWORD = "password";
        GMHttpParameters httpParameter = new GMHttpParameters();
        httpParameter.setParameter(FIELD_USERNAME, username);
        httpParameter.setParameter(FIELD_PASSWORD, password);
        GMHttpRequest.Builder builder = new GMHttpRequest.Builder();
        builder.setHttpParameters(httpParameter);
        builder.setTaskId("login");
        builder.setUrl("http://www.baidu.com");
        builder.setMethod("GET");
        builder.setOnResponseListener(l);
        GMHttpService service = GMHttpService.getInstance();
        service.executeHttpMethod(builder.build());

    }
}
