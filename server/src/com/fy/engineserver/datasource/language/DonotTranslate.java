package com.fy.engineserver.datasource.language;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DonotTranslate {

	// Language[] types() default {};//在此列表中的语言不用翻译

}