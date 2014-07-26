package org.gemini.httpengine.library;

import java.io.IOException;

import org.gemini.httpengine.library.GMHttpParameters;

public interface HttpRequestParser {
	public byte[] parse(GMHttpParameters httpParams) throws IOException;

	public String parseContentType();

    public void setEncoding(String encoding);
}
