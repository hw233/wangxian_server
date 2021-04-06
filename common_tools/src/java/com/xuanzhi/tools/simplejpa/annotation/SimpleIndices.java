package com.xuanzhi.tools.simplejpa.annotation;

@java.lang.annotation.Target(value={java.lang.annotation.ElementType.TYPE})
@java.lang.annotation.Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface SimpleIndices {
 	public SimpleIndex[] value() default {};
}