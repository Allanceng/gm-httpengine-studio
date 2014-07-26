package org.gemini.httpengine.test;

import android.test.InstrumentationTestCase;

import org.gemini.httpengine.library.GMHttpParameters;
import org.gemini.httpengine.library.GMHttpRequest;

import java.lang.reflect.Method;

/**
 * Created by geminiwen on 14-3-22.
 */
public class TestRequestParser extends InstrumentationTestCase{

    public void testRequestParser() throws Exception{
        TestModel model = new TestModel();

        String data = null;
        byte[] result = null;
        GMHttpRequest request = new GMHttpRequest();

        model.setMessage("thisisamessage");
        request.parseParametersFromModel(model);
        result = request.getHttpEntity();
        data = new String(result);
        assertEquals(data,"name=thisisamessage&sex=false");

        model.setMessage(null);
        request.parseParametersFromModel(model);
        result = request.getHttpEntity();
        data = new String(result);
        assertEquals(data,"sex=false");
    }

    public void testRESTRegex() throws Exception {
        GMHttpParameters httpParameters = null;
        GMHttpRequest request = new GMHttpRequest();
        String url = null,resultUrl = null;
        Method method = request.getClass().getDeclaredMethod("replaceRegexForREST");
        method.setAccessible(true);

        httpParameters = new GMHttpParameters();
        httpParameters.setParameter("id","2");
        url = "http://url/products/:id";
        request.setHttpParameters(httpParameters);
        request.setUrl(url);
        resultUrl = (String)method.invoke(request);
        assertEquals(resultUrl, "http://url/products/2");


        httpParameters = new GMHttpParameters();
        httpParameters.setParameter("id","2");
        url = "http://url/products/:id/test";
        request.setHttpParameters(httpParameters);
        request.setUrl(url);
        resultUrl = (String)method.invoke(request);
        assertEquals(resultUrl, "http://url/products/2/test");

        httpParameters = new GMHttpParameters();
        httpParameters.setParameter("id","2");
        httpParameters.setParameter("test", "3");
        url = "http://url/products/:id/test";
        request.setHttpParameters(httpParameters);
        request.setUrl(url);
        resultUrl = (String)method.invoke(request);
        assertEquals(resultUrl, "http://url/products/2/test");


        httpParameters = new GMHttpParameters();
        httpParameters.setParameter("id","2");
        httpParameters.setParameter("test", "3");
        url = "http://url/products/:id/:test";
        request.setHttpParameters(httpParameters);
        request.setUrl(url);
        resultUrl = (String)method.invoke(request);
        assertEquals(resultUrl, "http://url/products/2/3");


        method.setAccessible(false);
    }

}
