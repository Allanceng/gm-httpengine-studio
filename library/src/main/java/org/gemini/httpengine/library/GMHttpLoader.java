package org.gemini.httpengine.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Loader;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by geminiwen on 14-7-28.
 */
@TargetApi(11)
public class GMHttpLoader extends Loader<GMHttpResult> implements OnResponseListener{
    private GMHttpService httpService;
    private GMHttpRequest httpRequest;

    private Handler handler;

    private class UIHandler extends Handler {

        private WeakReference<GMHttpLoader> loaderWeakReference;

        public UIHandler(Looper looper, GMHttpLoader loader) {
            super(looper);
            this.loaderWeakReference = new WeakReference<GMHttpLoader>(loader);
        }

        @Override
        public void handleMessage(Message msg) {
            GMHttpLoader loader = this.loaderWeakReference.get();
            if (loader != null) {
                GMHttpResult result = (GMHttpResult) msg.obj;
                loader.deliverResult(result);
            }
        }
    }

    public GMHttpLoader(Context context, GMHttpRequest httpRequest) {
        super(context);
        this.httpService = GMHttpService.getInstance();
        this.httpRequest = httpRequest;
        this.handler = new UIHandler(Looper.getMainLooper(), this);
    }

    @Override
    protected void onStartLoading() {
        this.httpRequest.setOnResponseListener(this);
        this.httpService.executeHttpMethod(this.httpRequest);
    }

    @Override
    public void onResponse(GMHttpResponse response, GMHttpRequest request) {
        GMHttpResult result = new GMHttpResult();
        result.httpRequest = request;
        result.httpResponse = response;
        handler.obtainMessage(0, result)
               .sendToTarget();
    }
}
