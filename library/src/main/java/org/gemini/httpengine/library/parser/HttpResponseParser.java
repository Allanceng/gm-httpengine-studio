package org.gemini.httpengine.library.parser;

public interface HttpResponseParser {
	public Object handleResponse(byte[] response);
}
