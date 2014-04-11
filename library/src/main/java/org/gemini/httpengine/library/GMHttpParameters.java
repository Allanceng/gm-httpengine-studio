package org.gemini.httpengine.library;

import org.json.JSONObject;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GMHttpParameters {
	private TreeMap<String, String> httpParams;

	public GMHttpParameters() {
        httpParams = new TreeMap<String, String>();
	}

	public GMHttpParameters(GMHttpParameters other) {
		this.httpParams = new TreeMap<String, String>(other.httpParams);
	}

	public String getParameter(String name) {
        return httpParams.get(name);
	}

	public GMHttpParameters setParameter(String name, Object value) {
		httpParams.put(name, value.toString());
		return this;
	}

    public String removeParameter(String name) {
        return this.httpParams.remove(name);
    }

	public Set<String> getNames() {
        return httpParams.keySet();
	}

	@Override
	public String toString() {
        return httpParams.toString();
	}

}