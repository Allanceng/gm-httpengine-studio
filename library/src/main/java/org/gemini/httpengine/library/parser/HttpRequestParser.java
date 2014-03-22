package org.gemini.httpengine.library.parser;

import java.io.IOException;

import org.gemini.httpengine.library.net.GMHttpParameters;

public interface HttpRequestParser {
	public byte[] parse(GMHttpParameters httpParams) throws IOException;

	public String pareContentType();
}
