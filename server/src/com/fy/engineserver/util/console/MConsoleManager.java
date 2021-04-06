package com.fy.engineserver.util.console;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * M控制台管理器
 * 
 * 
 */
public class MConsoleManager {

	/** 所有注册过的控制台对象 */
	private static List<MConsole> consoles = new ArrayList<MConsole>();

	/**
	 * 注册
	 * @param mConsole
	 */
	public static void register(MConsole mConsole) {
		consoles.add(mConsole);
	}

	/**
	 * 解除注册
	 * @param mConsole
	 */
	public static void unRegister(MConsole mConsole) {
		consoles.remove(mConsole);
	}

	/**
	 * 得到所有标注可在后台修改的字段
	 * @param console
	 * @return
	 */
	public static Field[] getMConsoleFields(MConsole console) {
		Field[] fields = console.getClass().getDeclaredFields();
		List<Field> temp = new ArrayList<Field>();
		for (Field f : fields) {
			if (f.getAnnotation(ChangeAble.class) != null) {
				f.setAccessible(true);
				temp.add(f);
			}
		}
		return temp.toArray(new Field[temp.size()]);
	}

	/**
	 * 通过简单类名获得MConsole
	 * @param simpleClassName
	 * @return
	 */
	public static MConsole getMConsole(String simpleClassName) {
		for (MConsole console : getConsoles()) {
			if (console.getClass().getSimpleName().equals(simpleClassName)) {
				return console;
			}
		}
		return null;
	}

	/**
	 * 给一个对象的字段赋值
	 * @param object
	 * @param field
	 * @param value
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static void setValue(Object object, Field field, Object value) throws Throwable {
		field.setAccessible(true);
		value = getValue(value, field.getType());
		field.set(object, value);
	}

	private static Object getValue(Object paramObject, Class paramClass) throws Exception {
		if (paramObject == null) return null;
		String str;
		if ((str = paramClass.getName()).equals("java.lang.String")) return String.valueOf(paramObject);
		if ((str.equals("java.lang.Integer")) || (str.equals("int"))) return Integer.valueOf(Integer.parseInt(paramObject.toString()));
		if ((str.equals("java.lang.Long")) || (str.equals("long"))) return Long.valueOf(Long.parseLong(paramObject.toString()));
		if ((str.equals("java.lang.Boolean")) || (str.equals("boolean"))) return Boolean.valueOf(paramObject.toString());
		if ((str.equals("java.lang.Double")) || (str.equals("double"))) return Double.valueOf(paramObject.toString());
		throw new Exception("不支持数据类型:" + paramClass.getName());
	}

	public static List<MConsole> getConsoles() {
		return consoles;
	}
}
