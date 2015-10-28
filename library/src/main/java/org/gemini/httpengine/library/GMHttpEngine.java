package org.gemini.httpengine.library;


import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * Kernel HTTP engine
 *
 * @author Gemini
 */
public class GMHttpEngine {
    public static String TAG = GMHttpEngine.class.getSimpleName();

    private int connectionTimeout;
    private int readTimeout;

    private static final String SET_COOKIE_SEPARATOR = "; ";
    private static final int READ_BYTE_COUNT = 4096;
    private static final int WRITE_BYTE_COUNT = 2048;

    private OkUrlFactory urlFactory = new OkUrlFactory(new OkHttpClient());


    public GMHttpEngine() {
        HttpURLConnection.setFollowRedirects(true);
        connectionTimeout = GMConfig.TIMEOUT * 1000;
        readTimeout = GMConfig.TIMEOUT * 1000;
    }

    /**
     * start execute
     *
     * @param httpRequest request object
     * @return the raw data server response
     */
    public GMHttpResponse openUrl(GMHttpRequest httpRequest) {
        GMHttpResponse response = new GMHttpResponse();
        String method = httpRequest.getMethod();
        Map<String, String> headers = httpRequest.getHeaders();
        Map<String, String> cookies = httpRequest.getCookies();
        HttpURLConnection connection = null;
        byte[] httpEntity = null;
        try {
            String uri = httpRequest.getUrl();
            URL url = new URL(uri);

            connection = urlFactory.open(url);
            connection.setRequestMethod(method);
            if ( method.equalsIgnoreCase(HttpMethod.HTTP_POST)
              || method.equalsIgnoreCase(HttpMethod.HTTP_PUT) ) {
                httpEntity = httpRequest.getHttpEntity();

                String contentType = httpRequest.getContentType();
                if (contentType != null){
                    connection.addRequestProperty("Content-Type", contentType);
                }

                long contentLength = httpRequest.getContentLength();
                if (contentLength != -1) {
                    connection.addRequestProperty("Content-Length", String.valueOf(contentLength));
                }

            }
            LOG.d(TAG, uri);

            if (headers != null) {
                for (Entry<String, String> e : headers.entrySet()) {
                    String key = e.getKey();
                    String value = e.getValue();
                    connection.addRequestProperty(key, value);
                }
            }

            if (cookies != null) {
                StringBuilder cookieBuilder = new StringBuilder();
                boolean isFirst = true;
                for (Entry<String, String> e : cookies.entrySet()) {
                    String key = e.getKey();
                    String value = e.getValue();
                    if (value == null) {
                        continue;
                    }
                    if (isFirst)  {
                        isFirst = false;
                    } else {
                        cookieBuilder.append(SET_COOKIE_SEPARATOR);
                    }
                    cookieBuilder.append(key + "=" + value);
                }
                connection.setRequestProperty("Cookie", cookieBuilder.toString());
            }

            connection.addRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
            connection.addRequestProperty("Connection", "keep-alive");
            connection.addRequestProperty("User-Agent", "gm-httpengine v" + GMConfig.VERSION_NAME);

            connection.setDoInput(true);
            connection.setConnectTimeout(connectionTimeout);
            connection.setReadTimeout(readTimeout);
            if (httpEntity != null) {
                connection.setDoOutput(true);
                OutputStream httpBodyStream = connection.getOutputStream();
                int size = httpEntity.length;
                int hasWrite = 0;

                while (hasWrite < size) {
                    int perWrite = WRITE_BYTE_COUNT;
                    if (hasWrite + WRITE_BYTE_COUNT > size) {
                        perWrite = size - hasWrite;
                    }
                    httpBodyStream.write(httpEntity, hasWrite, perWrite);
                    hasWrite += perWrite;
                    OnProgressUpdateListener l = httpRequest.getOnProgressUpdateListener();
                    if (l != null) {
                        l.onUploadProgreessUpdate(100 * hasWrite / size, hasWrite);
                    }
                }

                httpBodyStream.flush();
                httpBodyStream.close();
            }
            connection.connect();
            int responseCode = connection.getResponseCode();
            response.setHttpStatusCode(responseCode);
            InputStream responseStream = connection.getInputStream();
            long length = connection.getContentLength();
            String contentEncoding = connection.getContentEncoding();
            if (contentEncoding != null && contentEncoding.toLowerCase().equals("gzip")) {
                responseStream = new GZIPInputStream(responseStream);
            }

            byte[] resultData = readHttpResponseAsByte(responseStream, length, httpRequest);
            Map<String, List<String>> responseHeader = connection.getHeaderFields();
            List<String> setCookies = responseHeader.get("Set-Cookie");

            responseStream.close();
            response.setRawData(resultData);
            if (setCookies != null){
                response.parseCookies(setCookies);
            }

        } catch (Exception e) {
            LOG.w(TAG, e.getClass().getSimpleName(), e);
            response.setRawData(null);
            response.setException(e);
        }
        return response;
    }

    /**
     * receive buffer;
     */
    private ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();
    private byte[] buffer = new byte[READ_BYTE_COUNT];

    private byte[] readHttpResponseAsByte(InputStream is, long length, GMHttpRequest request) throws IOException{
        try {
            int perRead;
            long nHasRead = 0;

            while ((perRead = is.read(buffer, 0, buffer.length)) != -1) {
                bufferStream.write(buffer, 0, perRead);
                nHasRead += perRead;
                OnProgressUpdateListener l = request.getOnProgressUpdateListener();
                if (null != l) {
                    l.onDownloadProgressUpdate((int)(100 * nHasRead / length), nHasRead);
                }
            }
            bufferStream.flush();
        } catch (IOException e) {
            LOG.d(TAG, e.getClass().getSimpleName(), e);
            throw e;
        }
        byte[] ret = bufferStream.toByteArray();
        bufferStream.reset();
        return ret;
    }

}
