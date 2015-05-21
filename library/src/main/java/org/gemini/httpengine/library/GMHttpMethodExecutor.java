package org.gemini.httpengine.library;

/**
 * Created by geminiwen on 14-8-14.
 */
public interface GMHttpMethodExecutor {
    void executeHttpMethod(GMHttpRequest httpRequest);

    void cancelRequest(GMHttpRequest httpRequest);
}
