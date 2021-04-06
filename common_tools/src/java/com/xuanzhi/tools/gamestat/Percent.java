package com.xuanzhi.tools.gamestat;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({FIELD})
@Retention(RUNTIME)

public @interface Percent {
	//设置分母
	public String denominator() default "";
	
	//如果分母制定的是某个分布属性，那么keyword用于取分布中的数值作为分母
	public String keyword() default "";
}