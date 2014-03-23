package org.gemini.httpengine.library;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.GZIPInputStream;

/**
 * Kernel HTTP engine
 *
 * @author Gemini
 */
public class GMHttpEngine {
    public static String TAG = GMHttpEngine.class.getSimpleName();

    public static final String HTTP_GET = "GET";
    public static final String HTTP_POST = "POST";
    public static final String HTTP_PUT = "PUT";
    public static final String HTTP_DELETE = "DELETE";

    public static int CONNECTION_TIME_OUT = 5000;
    public static int READ_TIME_OUT = 30000;

    public GMHttpEngine() {
        HttpURLConnection.setFollowRedirects(true);
    }

    /**
     * start execute
     *
     * @param httpRequest
     * @return the raw data server response
     */
    public byte[] openUrl(GMHttpRequest httpRequest) {
        byte[] resultData = null;
        String method = httpRequest.getMethod();
        OnProgressUpdateListener progressListener = httpRequest.getOnProgressUpdateListener();
        Map<String, String> headers = httpRequest.getHeaders();
        HttpURLConnection connection = null;
        byte[] httpEntity = null;
        try {
            String uri = httpRequest.getUrl();
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            if (method.equalsIgnoreCase(HTTP_POST)) {
                String contentType = httpRequest.getContentType();
                connection.addRequestProperty("Content-Type", contentType);
                httpEntity = httpRequest.getHttpEntity();
            }
            LOG.d(TAG, uri);

            if (headers != null) {
                for (Entry<String, String> e : headers.entrySet()) {
                    String key = e.getKey();
                    String value = e.getValue();
                    connection.addRequestProperty(key, value);
                }
            }

            connection.addRequestProperty("Accept-Encoding",
                    "gzip,deflate,sdch");
            connection.addRequestProperty("Connection", "keep-alive");
            connection.addRequestProperty("User-Agent",
                    "gemini-http-engine v" + Config.VERSION_NAME);

            if (httpEntity != null) {
                connection.setDoOutput(true);
                OutputStream httpBodyStream = connection.getOutputStream();
                httpBodyStream.write(httpEntity);
                httpBodyStream.flush();
                httpBodyStream.close();
            }
            connection.setDoInput(true);
            connection.setConnectTimeout(CONNECTION_TIME_OUT);
            connection.setReadTimeout(READ_TIME_OUT);
            connection.connect();
            int responseCode = connection.getResponseCode();
            InputStream responseStream = connection.getInputStream();
            int length = connection.getContentLength();
            String contentEncoding = connection.getContentEncoding();
            if (contentEncoding != null
                    && contentEncoding.toLowerCase().equals("gzip")) {
                responseStream = new GZIPInputStream(responseStream);
            }

            resultData = readHttpResponseAsByte(responseStream, length,
                    progressListener);

        } catch (IOException e) {
            LOG.w(TAG, e.getClass().getSimpleName(), e);
        } finally {
            connection.disconnect();
        }

        return resultData;
    }

    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    byte[] bufferData = new byte[1024];

    private byte[] readHttpResponseAsByte(InputStream is, int length,
                                          OnProgressUpdateListener l) {
        try {
            int nRead;
            int nHasRead = 0;
            while ((nRead = is.read(bufferData, 0, bufferData.length)) != -1) {
                buffer.write(bufferData, 0, nRead);
                nHasRead += nRead;
                if (null != l) {
                    l.onUpdate(100 * nHasRead / length, String.valueOf(nHasRead));
                }
            }
            buffer.flush();
        } catch (IOException e) {
            LOG.d(TAG, e.getClass().getSimpleName(), e);
        }
        byte[] ret = buffer.toByteArray();
        buffer.reset();
        return ret;
    }

}
