package org.gemini.httpengine.library;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

public class FormUrlEncodedParser implements HttpRequestParser {

	/***
	 * parse form body
	 */
	@Override
	public byte[] parse(GMHttpParameters httpParameters) throws IOException {
        if(httpParameters == null) {
            return null;
        }
        Set<String> keySet = httpParameters.getNames();
        ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for (String name : keySet) {
            String value = httpParameters.getParameter(name);
            NameValuePair p = new BasicNameValuePair(name, value);
            nvps.add(p);
        }
        HttpEntity entity = new UrlEncodedFormEntity(nvps, "UTF-8");
        InputStream is = entity.getContent();
        int available = is.available();
        byte[] buffer = new byte[available];
        is.read(buffer);
        is.close();
        return buffer;
	}


	@Override
	public String pareContentType() {
        return "application/x-www-form-urlencoded; charset=UTF-8";
	}

}
