package org.gemini.httpengine.library;

import java.util.Set;
import java.util.TreeMap;

public class GMHttpParameters {
	private TreeMap<String, String> httpParameters;

	public GMHttpParameters() {
        httpParameters = new TreeMap<String, String>();
	}

	public GMHttpParameters(GMHttpParameters other) {
		this.httpParameters = new TreeMap<String, String>(other.httpParameters);
	}

	public String getParameter(String name) {
        return httpParameters.get(name);
	}

    public GMHttpParameters setParameter(String name, Object value) {
        if (value != null) {
            httpParameters.put(name, value.toString());
        }
        return this;
    }

    public String removeParameter(String name) {
        return this.httpParameters.remove(name);
    }

	public Set<String> getNames() {
        return httpParameters.keySet();
	}

	@Override
	public String toString() {
        return httpParameters.toString();
	}

}