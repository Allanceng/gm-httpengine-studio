package org.gemini.httpengine.library;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * Http response get from GMHttpEngine request
 * 
 * @author Gemini
 * 
 */
public class GMHttpResponse {

	private byte[] rawData;
    private int httpStatusCode;
	private HttpResponseParser responseParser;
    private Exception exception;

	private boolean isFail = false;

    public void setRawData(byte[] data) {
        if (data != null) {
            this.rawData = data;
        } else {
            isFail = true;
        }
    }

	public byte[] getRawData() {
		if (isFail) {
			throw new RuntimeException("Request is failed");
		}
		return this.rawData;
	}

	public HttpResponseParser getResponseParser() {
        return responseParser;
	}

	public void setResponseParser(HttpResponseParser responseParser) {
		this.responseParser = responseParser;
	}

	public String parseAsString() {
        return parseAsString("UTF-8");
	}

	public String parseAsString(String encode) {
		String ret = null;
		if (isFail) {
			throw new RuntimeException("Request is failed");
		}
		try {
			ret = new String(this.rawData, encode);
		} catch (UnsupportedEncodingException e) {
			isFail = true;
			throw new RuntimeException("Unsupport encoding " + e.getMessage());
		}
		return ret;
	}

    public void setHttpStatusCode(int code) {
        this.httpStatusCode = code;
    }

    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
        this.isFail = true;
    }

    public JSONObject parseAsJSON() {
		String result = parseAsString();
		JSONObject obj = null;
		try {
			obj = new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public Object parseData() {
		if (isFail) {
			throw new RuntimeException("Request is failed");
		}
		return responseParser.handleResponse(this.rawData);
	}
}
