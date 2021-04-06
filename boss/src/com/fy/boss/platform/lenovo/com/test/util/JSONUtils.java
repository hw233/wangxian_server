package com.fy.boss.platform.lenovo.com.test.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONUtils {

	/**
	 * 作用是用于,如果用@Expose注解,表示此字段需要json.
	 */
	protected static final GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();

	protected static final Gson gson = builder.create();

	public static Gson getGson() {
		return gson;
	}

	/**
	 * 将对象转化为JSON字符串
	 * @param obj
	 * @return
	 */
	public static String toJson(Object obj) {
		return gson.toJson(obj);
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		return gson.fromJson(json, clazz);
	}

}
