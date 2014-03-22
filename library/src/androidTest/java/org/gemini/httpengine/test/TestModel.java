package org.gemini.httpengine.test;

import org.gemini.httpengine.library.HTTPParameter;
import org.gemini.httpengine.library.RequestModel;

/**
 * Created by geminiwen on 14-3-22.
 */
public class TestModel extends RequestModel {

    @HTTPParameter(name="name")
    private String message;

    @HTTPParameter(name="sex")
    private boolean really;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isReally() {
        return really;
    }

    public void setReally(boolean really) {
        this.really = really;
    }
}
