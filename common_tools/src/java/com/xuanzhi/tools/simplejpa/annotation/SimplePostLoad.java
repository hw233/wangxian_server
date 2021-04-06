package com.xuanzhi.tools.simplejpa.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 回调方法，对Entity和Embeddable都有效
 * 
 * 对于Load,Persist,Update，都是先通知Entity，然后通知Entity中的Ebmeddable，然后通知Ebmeddable中的Ebmeddable....
 * 对于Remove，都是先通知对底层的Ebmeddable，然后上层的Ebmeddable，最后Entity
 */

@Target({METHOD})
@Retention(RUNTIME)
public @interface SimplePostLoad {

}
