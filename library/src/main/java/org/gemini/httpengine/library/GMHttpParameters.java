package org.gemini.httpengine.library;

import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

public class GMHttpParameters {
	private TreeMap<String, Object> httpParameters;

	public GMHttpParameters() {
        httpParameters = new TreeMap<String, Object>();
	}

	public GMHttpParameters(GMHttpParameters other) {
		this.httpParameters = new TreeMap<String, Object>(other.httpParameters);
	}

	public Object getParameter(String name) {
        return httpParameters.get(name);
	}

    public GMHttpParameters setParameter(String name, Object value) {
        if (value != null) {
            httpParameters.put(name, value);
        }
        return this;
    }

    public Object removeParameter(String name) {
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