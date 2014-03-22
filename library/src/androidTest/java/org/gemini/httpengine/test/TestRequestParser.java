package org.gemini.httpengine.test;

import android.test.InstrumentationTestCase;
import android.util.Log;

import org.gemini.httpengine.library.GMHttpParameters;
import org.gemini.httpengine.library.GMHttpRequest;

import java.io.IOException;

/**
 * Created by geminiwen on 14-3-22.
 */
public class TestRequestParser extends InstrumentationTestCase{

    public void testRequestParser() throws  IOException{
        TestModel model = new TestModel();

        GMHttpParameters parameters = null;
        String data = null;
        byte[] result = null;

        parameters = new GMHttpParameters();
        parameters.setRequestModel(model);
        GMHttpRequest request = new GMHttpRequest();

        model.setMessage("thisisamessage");
        result = request.getRequestParser().parse(parameters);
        data = new String(result);
        assertEquals(data,"name=thisisamessage&sex=false");

        model.setMessage(null);
        result = request.getRequestParser().parse(parameters);
        data = new String(result);
        assertEquals(data,"sex=false");

    }

}
