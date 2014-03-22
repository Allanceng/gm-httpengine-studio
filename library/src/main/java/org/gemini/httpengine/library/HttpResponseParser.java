package org.gemini.httpengine.library;

public interface HttpResponseParser {
	public Object handleResponse(byte[] response);
}
