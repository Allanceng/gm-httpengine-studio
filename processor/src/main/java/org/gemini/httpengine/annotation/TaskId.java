package org.gemini.httpengine.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by geminiwen on 15/5/21.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface TaskId {
    String value();
}
