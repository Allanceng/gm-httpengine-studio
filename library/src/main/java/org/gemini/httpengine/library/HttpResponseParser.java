package org.gemini.httpengine.library;

public interface HttpResponseParser<T> {
	public T handleResponse(byte[] response);
}
