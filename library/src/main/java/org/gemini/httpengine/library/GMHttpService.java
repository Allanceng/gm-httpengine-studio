package org.gemini.httpengine.library;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GMHttpService implements GMHttpMethodExecutor{

	public static final String TAG = GMHttpService.class.getSimpleName();
	public static final String VERSION = GMConfig.VERSION_NAME;
    private static final int MAX_THREAD_NUM = 4;

    private static GMHttpService sInstance;

	/***
	 * per thread has a {@link GMHttpEngine}
	 */
	private final ThreadLocal<GMHttpEngine> sHttpEnginePool = new ThreadLocal<GMHttpEngine>();

	private final Executor mService;

	private GMHttpService() {
        mService = Executors.newFixedThreadPool(MAX_THREAD_NUM);
	}

	public static synchronized GMHttpService getInstance() {
		if (sInstance == null) {
			makeInstance();
		}
		return sInstance;
	}

	private static synchronized void makeInstance() {
        sInstance = new GMHttpService();
	}


	private class HttpRunnable implements Runnable {

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
			this.updateResponse();
		}

		private void updateResponse() {
			if (mHttpRequest.isCancel()) {
				return;
			}
			OnResponseListener l = mHttpRequest.getOnResponseListener();
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
