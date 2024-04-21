package com.server.app;

import com.server.app.annotation.ServerApplication;
import com.server.http.request.EndpointMapper;

public class ServerRunnableApplication{
    public static void run(Class<?> clazz, String[] args) {
        if (clazz.isAnnotationPresent(ServerApplication.class)) {
            try {
                EndpointMapper.mapEndpoints(clazz);
                ApplicationEntryPoint.runApp(args);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        } else {
            throw new IllegalArgumentException("Class must be annotated with @MyServerApp");
        }
    }
}
