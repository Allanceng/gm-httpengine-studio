package org.gemini.httpengine.library;

import android.content.Context;
import android.net.http.HttpResponseCache;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class GMHttpService implements GMHttpMethodExecutor{

	public static final String TAG = GMHttpService.class.getSimpleName();
	public static final String VERSION = Config.VERSION_NAME;
    private static final int MAX_THREAD_NUM = 3;

    private static GMHttpService sInstance;

	/***
	 * per thread has a {@link GMHttpEngine}
	 */
	private final ThreadLocal<GMHttpEngine> sHttpEnginePool = new ThreadLocal<GMHttpEngine>();

	private final Executor mService;

	private final Handler.Callback mResponseCallBack;

	/**
	 * default thread that handler run on
	 */
	private HandlerThread mHandlerThread;
	private Handler mCallbackHandler;

	private GMHttpService() {
		mService = new ThreadPoolExecutor(0, MAX_THREAD_NUM,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(MAX_THREAD_NUM));
		mResponseCallBack = new ResponseDataCallback();

		mHandlerThread = new HandlerThread(TAG + "-HandlerThread");
		mHandlerThread.start();
		mCallbackHandler = new Handler(mHandlerThread.getLooper(),
				mResponseCallBack);
	}

	/***
	 * Set the callback looper for response. Just set it to parse response on
	 * the right thread
	 * 
	 * @param looper Android Looper
	 */
	public void setCallbackLooper(Looper looper) {
		this.mCallbackHandler = new Handler(looper, mResponseCallBack);
	}

	/***
	 * Single Instance
	 * 
	 * @return
	 */
	public static synchronized GMHttpService getInstance() {
		if (sInstance == null) {
			makeInstance();
		}
		return sInstance;
	}

	private static synchronized void makeInstance() {
        sInstance = new GMHttpService();
	}

	/**
	 * receive the data of the runnable and response to the listener
	 * 
	 * @author GeminiWen
	 * 
	 */
	private static class ResponseDataCallback implements Handler.Callback {
		@Override
		public boolean handleMessage(Message msg) {
			ResponseUpdater runnable = (ResponseUpdater) msg.obj;
			runnable.updateResponse();
			return true;
		}
	}

	interface ResponseUpdater {
		public void updateResponse();
	}

	private class HttpRunnable implements Runnable, ResponseUpdater {

		private GMHttpRequest mHttpRequest;
		private GMHttpResponse mHttpResponse;

		public HttpRunnable(GMHttpRequest httpRequest) {
			mHttpRequest = httpRequest;
		}

		@Override
		public void run() {
			if (mHttpRequest.isCancel()) {
				return;
			}
			GMHttpEngine httpEngine = sHttpEnginePool.get();
			if (httpEngine == null) {
				httpEngine = new GMHttpEngine();
				sHttpEnginePool.set(httpEngine);
			}

			// execute request and get response
            mHttpResponse = httpEngine.openUrl(mHttpRequest);
			Message msg = mCallbackHandler.obtainMessage(0, this);
			msg.sendToTarget();
		}

		@Override
		public void updateResponse() {
			if (mHttpRequest.isCancel()) {
				return;
			}
			OnResponseListener l = mHttpRequest.getResponseListener();
			if (null != l) {
				l.onResponse(mHttpResponse, mHttpRequest);
			}
		}
	}

	/***
	 * Execute http request asynchronous and get the response
	 * 
	 * @param httpRequest
	 *            the http request object
	 */
    @Override
	public void executeHttpMethod(GMHttpRequest httpRequest) {
		Runnable runnable = new HttpRunnable(httpRequest);

		mService.execute(runnable);
	}

	/***
	 * Cancel request
	 * 
	 * @param httpRequest
	 *            the request to cancel
	 */
    @Override
	public void cancelRequest(GMHttpRequest httpRequest) {
		//@TODO: to be completed;
        httpRequest.cancel();
	}

}
