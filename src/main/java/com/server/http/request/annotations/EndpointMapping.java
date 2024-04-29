package com.server.http.request.annotations;

import com.server.http.enums.HTTP_METHOD;
import com.server.http.enums.RESPONSE_TYPE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EndpointMapping {
    String uri() default "/";
    HTTP_METHOD method() default HTTP_METHOD.GET;
}