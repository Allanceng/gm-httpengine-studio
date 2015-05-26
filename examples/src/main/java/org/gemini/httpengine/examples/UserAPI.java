package org.gemini.httpengine.examples;

import org.gemini.httpengine.annotation.GET;
import org.gemini.httpengine.annotation.Path;
import org.gemini.httpengine.annotation.TaskId;
import org.gemini.httpengine.library.OnResponseListener;

/**
 * Created by geminiwen on 15/5/21.
 */
public abstract class UserAPI {
    interface TASKID {
        String TASK_GET_LOGIN = "login";
    }

    @Path("http://api.segmentfault.com/question/newest?page=1")
    @TaskId(TASKID.TASK_GET_LOGIN)
    @GET
    public abstract void login(OnResponseListener l,
               String username,
               String password);
}
