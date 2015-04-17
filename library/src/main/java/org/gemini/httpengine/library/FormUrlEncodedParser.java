package org.gemini.httpengine.library;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class FormUrlEncodedParser implements HttpRequestParser {

    private String mEncodingString = "UTF-8";

	/***
	 * parse form body
	 */
	@Override
	public byte[] parse(GMHttpParameters httpParameters) throws IOException,GMHttpException {
        if(httpParameters == null) {
            return null;
        }
        Set<String> keySet = httpParameters.getNames();
        ArrayList<NameValuePair> nvps = new ArrayList<>();
        for (String name : keySet) {
            Object value = httpParameters.getParameter(name);

            if (value == null) {
                continue;
            }

            if (value instanceof File) {
                throw new GMHttpException("FormUrlEncoding cannot have file part");
            }

            if (value instanceof List) {
                List l = (List)value;
                String n = name + "[]";
                for (Object v : l) {
                    if (v == null) {
                        continue;
                    }
                    NameValuePair p = new BasicNameValuePair(n, v.toString());
                    nvps.add(p);
                }
            } else if (value.getClass().isArray()) {
                int length = Array.getLength(value);
                String n = name + "[]";
                for (int i = 0; i < length; i ++) {
                    Object v = Array.get(value, i);
                    if (v == null) {
                        continue;
                    }
                    NameValuePair p = new BasicNameValuePair(n, String.valueOf(v));
                    nvps.add(p);
                }
            } else {
                NameValuePair p = new BasicNameValuePair(name, value.toString());
                nvps.add(p);
            }

        }
        HttpEntity entity = new UrlEncodedFormEntity(nvps, mEncodingString);
        InputStream is = entity.getContent();
        int available = is.available();
        byte[] buffer = new byte[available];
        is.read(buffer);
        is.close();
        return buffer;
	}


	@Override
	public String getContentType() {
        return "application/x-www-form-urlencoded; charset=" + mEncodingString;
	}

    @Override
    public void setEncoding(String encoding) {
        this.mEncodingString = encoding;
    }

    @Override
    public long getContentLength() {
        return -1;
    }
}
