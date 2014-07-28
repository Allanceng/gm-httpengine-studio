package org.gemini.httpengine.library;

import android.annotation.TargetApi;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by geminiwen on 14-7-28.
 */
@TargetApi(11)
public class GMHttpLoader extends Loader<GMHttpResult> implements OnResponseListener{
    private GMHttpService httpService;
    private GMHttpRequest httpRequest;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            GMHttpResult result = (GMHttpResult) msg.obj;
            deliverResult(result);
        }
    };

    public GMHttpLoader(Context context, GMHttpRequest httpRequest) {
        super(context);
        this.httpService = GMHttpService.getInstance();
        this.httpRequest = httpRequest;
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
