package org.gemini.httpengine.test;

import org.gemini.httpengine.library.HttpParameter;

/**
 * Created by geminiwen on 14-3-22.
 */
public class TestModel {

    @HttpParameter(name="name")
    private String message;

    @HttpParameter(name="sex")
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
