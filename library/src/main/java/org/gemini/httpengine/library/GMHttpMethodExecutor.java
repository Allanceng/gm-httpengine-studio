package org.gemini.httpengine.library;

/**
 * Created by geminiwen on 14-8-14.
 */
public interface GMHttpMethodExecutor {
    public void executeHttpMethod(GMHttpRequest httpRequest);

    public void cancelRequest(GMHttpRequest httpRequest);
}
