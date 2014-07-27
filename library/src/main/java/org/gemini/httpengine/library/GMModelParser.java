package org.gemini.httpengine.library;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by geminiwen on 14-3-31.
 */
public class GMModelParser {

    public GMHttpParameters parseModel(Object httpParamsModel) {
        GMHttpParameters httpParameters = new GMHttpParameters();
        Field[] fields = httpParamsModel.getClass().getDeclaredFields();
        for (Field field : fields) {

            if (field.isAnnotationPresent(HttpParameter.class)) {
                HttpParameter parameter = field.getAnnotation(HttpParameter.class);
                String parameterName = parameter.name();
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
                    getterMethod = httpParamsModel.getClass().getMethod(getterMethodName);

                    Object value = getterMethod.invoke(httpParamsModel);
                    if (null != value) {
                        if(!(value instanceof File)) {
                            httpParameters.setParameter(parameterName, value.toString());
                        } else {
                            httpParameters.setParameter(parameterName, (File)value);
                        }
                    }

                } catch (NoSuchMethodException e) {
                    String value = getValueByField(field, httpParamsModel);
                    if(null != value) {
                        httpParameters.setParameter(parameterName, value);
                    }
                } catch (IllegalAccessException e) {
                    String value = getValueByField(field, httpParamsModel);
                    if(null != value) {
                        httpParameters.setParameter(parameterName, value);
                    }
                } catch (InvocationTargetException e) {
                    // Never in this block
                }
            }

        }
        return httpParameters;
    }

    /***
     * get field name. modelName => ModelName
     * @param fieldName modelName
     * @return  ModelName
     */
    private String getGenericFieldName(String fieldName) {
        String newStr = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return newStr;
    }

    private String getValueByField(Field field,Object model) {
        Object value = null;
        try {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            value = field.get(model);
            field.setAccessible(accessible);
        } catch (IllegalAccessException ex) {
            // Never enter this block
        }
        return value.toString();
    }

}
