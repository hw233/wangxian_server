package com.xuanzhi.tools.simplejpa.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 回调方法，对Entity和Embeddable都有效
 *
 */

@Target({METHOD})
@Retention(RUNTIME)
public @interface SimplePostPersist {

}
