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
        Model reqModel = httpParameters.getRequestModel();
        byte[] data = null;
        if(reqModel != null ) {
            data = parseModel(reqModel);
        } else {
            data = parseMap(httpParameters);
        }
        return data;
	}

    private byte[] parseMap(GMHttpParameters httpParameters) throws IOException{
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

    private byte[] parseModel(Model model) throws IOException{
        byte[] result = null;
        Field[] fields = model.getClass().getDeclaredFields();
        ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for (Field field : fields) {

            if (field.isAnnotationPresent(HTTPParameter.class)) {
                HTTPParameter parameter = field.getAnnotation(HTTPParameter.class);
                String parameterName = parameter.name();
                NameValuePair p = null;
                try {
                    String fieldName = getGenericFieldName(field.getName());
                    Method getterMethod = null;
                    StringBuilder getterBuilder = new StringBuilder("");
                    String getterMethodName;

                    if (field.getType().equals(boolean.class)) {
                        getterBuilder.append("is");
                    } else {
                        getterBuilder.append("get");
                    }
                    getterBuilder.append(fieldName);
                    getterMethodName = getterBuilder.toString();
                    getterMethod = model.getClass().getMethod(getterMethodName);

                    Object value = getterMethod.invoke(model);
                    if( value != null ) {
                        p = new BasicNameValuePair(parameterName, value.toString());
                    }

                } catch (NoSuchMethodException e) {
                    String value = getValueByField(field,model);
                    p = new BasicNameValuePair(parameterName, value);
                } catch (IllegalAccessException e) {
                    String value = getValueByField(field,model);
                    p = new BasicNameValuePair(parameterName, value);
                } catch (InvocationTargetException e) {
                    // Never in this block
                }
                if( p != null ) {
                    nvps.add(p);
                }
            }

        }
        HttpEntity entity = new UrlEncodedFormEntity(nvps, "UTF-8");
        InputStream is = entity.getContent();
        int available = is.available();
        result = new byte[available];
        is.read(result);
        is.close();
        return result;
    }

    private String getGenericFieldName(String fieldName) {
        String newStr = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return newStr;
    }

    private String getValueByField(Field field,Model model) {
        Object value = null;
        try {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            value = field.get(model);
            field.setAccessible(accessible);
        } catch (IllegalAccessException ex) {
            // Never in this block
        }
        return value.toString();
    }

	@Override
	public String pareContentType() {
        return "application/x-www-form-urlencoded; charset=UTF-8";
	}

}
