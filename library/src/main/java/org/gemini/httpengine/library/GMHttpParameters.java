package org.gemini.httpengine.library;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeMap;

/**
 * http parameter collection
 */
public class GMHttpParameters {
	private TreeMap<String, Object> httpParameters;
    private boolean binaryData = false;

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
            if (value instanceof File) {
                binaryData = true;
            }
            httpParameters.put(name, value);
        }
        return this;
    }


    public boolean isBinaryContent() {
        return this.binaryData;
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