package org.gemini.httpengine.library;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

public class DefaultHttpRequestParser implements HttpRequestParser {

	/***
	 * parse form body
	 */
	@Override
	public byte[] parse(GMHttpParameters httpParams) throws IOException {
		Set<String> keySet = httpParams.getNames();
		ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (String name : keySet) {
			String value = httpParams.getParameter(name);
			NameValuePair p = new BasicNameValuePair(name, value);
			nvps.add(p);
		}
		HttpEntity entity = new UrlEncodedFormEntity(nvps, "UTF-8");
		InputStream is = entity.getContent();
		int available = is.available();
		byte[] buffer = new byte[available];
		is.read(buffer);
		is.close();
		return buffer;
	}

	@Override
	public String pareContentType() {
		return "application/x-www-form-urlencoded; charset=UTF-8";
	}

}
