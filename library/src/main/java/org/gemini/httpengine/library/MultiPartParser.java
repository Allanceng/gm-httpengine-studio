package org.gemini.httpengine.library;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Created by geminiwen on 14-7-26.
 *
 * file part parser
 */
public class MultiPartParser implements HttpRequestParser{

    private ByteArrayOutputStream mByteArrayBufferStream = new ByteArrayOutputStream();
    private MultipartEntity multipartEntity;

    @Override
    public byte[] parse(GMHttpParameters httpParameters) throws IOException {
        multipartEntity = new MultipartEntity();
        Set<String> keySet = httpParameters.getNames();
        for (String name : keySet) {
            Object value = httpParameters.getParameter(name);
            if ((value instanceof File) || (value instanceof Number )) {
                multipartEntity.addPart(name, value.toString());
            } else if (value instanceof  File) {
                multipartEntity.addPart(name, (File)value);
            }
        }
        mByteArrayBufferStream.reset();
        multipartEntity.writeTo(mByteArrayBufferStream);
        byte[] result = mByteArrayBufferStream.toByteArray();
        mByteArrayBufferStream.reset();
        return result;
    }

    @Override
    public String getContentType() {
        return multipartEntity.getContentType();
    }

    @Override
    public void setEncoding(String encoding) {

    }

    @Override
    public long getContentLength() {
        return multipartEntity.getContentLength();
    }
}
