package org.gemini.httpengine.library;

import android.util.Log;

/***
 * Log Util
 * 
 * @author geminiwen
 * 
 */
public class LOG {
	public static boolean DEBUG = false;

	public static void setDebug(boolean debug) {
		LOG.DEBUG = debug;
	}

	public static void v(String tag, String msg) {
		if (DEBUG) {
			Log.v(tag, msg);
		}
	}

	public static void d(String tag, String msg, Throwable t) {
		if (DEBUG) {
			Log.d(tag, msg, t);
		}
	}

	public static void d(String tag, String msg) {
		LOG.d(tag, msg, null);
	}

	public static void i(String tag, String msg, Throwable t) {
		if (DEBUG) {
			Log.i(tag, msg, t);
		}
	}

	public static void i(String tag, String msg) {
		LOG.i(tag, msg, null);
	}

	public static void w(String tag, String msg, Throwable t) {
		if (DEBUG) {
			Log.w(tag, msg, t);
		}
	}

	public static void w(String tag, String msg) {
		LOG.w(tag, msg);
	}

	public static void e(String tag, String msg, Throwable t) {
		if (DEBUG) {
			Log.e(tag, msg, t);
		}
	}

	public static void e(String tag, String msg) {
		LOG.e(tag, msg, null);
	}
}
