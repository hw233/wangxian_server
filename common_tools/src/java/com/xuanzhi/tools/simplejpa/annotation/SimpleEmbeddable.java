package com.xuanzhi.tools.simplejpa.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表明一个类为Embeddable
 * 
 * 此类将以json串的形式储存在数据库中
 * 所以，必须有公共的无参数的构造函数
 * 
 *
 */

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SimpleEmbeddable {

}
