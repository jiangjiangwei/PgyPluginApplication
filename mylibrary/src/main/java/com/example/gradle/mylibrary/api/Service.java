package com.example.gradle.mylibrary.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author : 江伟
 * e-mail : jiangwei@sip.sh.cn
 * time   : 2022/5/29
 * desc   : 网络请求baseUrl注解
 * version: 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Service {
    String baseUrl();
}
