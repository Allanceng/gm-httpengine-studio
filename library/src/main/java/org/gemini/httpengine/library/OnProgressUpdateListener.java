package org.gemini.httpengine.library;

public interface OnProgressUpdateListener {
	/***
	 * called when download progress update
	 * 
	 * @param progress
	 *            indicates percentage, maximum is 100
	 * @param value
	 *            the value string
	 */
	void onDownloadProgressUpdate(int progress, long value);

	void onUploadProgreessUpdate(int progress, long value);
}
