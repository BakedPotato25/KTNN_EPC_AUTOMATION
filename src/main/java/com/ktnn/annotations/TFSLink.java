package com.ktnn.annotations;

import io.qameta.allure.LinkAnnotation;

import java.lang.annotation.*;

/**
 * Class để liên kết với hệ thống quản lý bug
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@LinkAnnotation(type = "tms")
public @interface TFSLink {
    String value();
}