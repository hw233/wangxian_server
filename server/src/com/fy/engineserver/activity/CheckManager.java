package com.fy.engineserver.activity;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CheckManager {

	public static String check(Object object) {
		StringBuffer sbf = new StringBuffer();
		try {
			Class<?> clazz = object.getClass();
			CheckAttribute classAnnotation = clazz.getAnnotation(CheckAttribute.class);

			if (classAnnotation != null) {
				String className = classAnnotation.value();
				sbf.append("--------------------------" + className + "--------------------------<BR/>");
				sbf.append("<table border=1>");
				for (Field field : clazz.getDeclaredFields()) {
					CheckAttribute fieldAnnotation = field.getAnnotation(CheckAttribute.class);
					if (fieldAnnotation != null) {
						field.setAccessible(true);
						Object value = field.get(object);
						sbf.append("<tr>");
						sbf.append("<td width=100 bgcolor='#62BEF0'>");
						sbf.append(fieldAnnotation.value());
						sbf.append("</td>");
						sbf.append("<td width=100>");
						sbf.append(field.getName());
						sbf.append("</td>");
						sbf.append("<td width=150  bgcolor='#62BEF0'>");
						sbf.append(value.getClass());
						sbf.append("</td>");
						sbf.append("<td width=100>");
						sbf.append(fieldAnnotation.des());
						sbf.append("</td>");
						sbf.append("<td width=500 bgcolor='#62BEF0'>");
						if (value.getClass().isArray()) {
							sbf.append(printArray(value));
						} else {
							if (value instanceof Map) {
								Map map = (Map) value;
								int index = 0;
								for (Iterator itor = map.keySet().iterator(); itor.hasNext();) {
									Object key = itor.next();
									sbf.append("<font color=").append(index % 2 == 0 ? "" : "red").append(">").append("[");
									sbf.append(key).append(",");
//									sbf.append("<");
									Object mapValue = map.get(key);
									if (mapValue.getClass().isArray()) {
										sbf.append(printArray(mapValue));
									} else {
										sbf.append(mapValue);
									}
									sbf.append("]</font><BR/>");
									index++;
								}
							} else if (value instanceof List) {
								List list = (List) value;
								for (int i = 0; i < list.size(); i++) {
									Object listValue = list.get(i);
									sbf.append("<font color=").append(i % 2 == 0 ? "" : "red").append(">");
									if (listValue.getClass().isArray()) {
										sbf.append(printArray(listValue));
									} else {
										sbf.append(listValue);
									}
									sbf.append("</font></BR>");
								}
							} else {
								sbf.append(value.toString());
							}
						}
						sbf.append("</td>");
						sbf.append("</tr>");
					}

				}
				sbf.append("</table>");
			} else {
				sbf.append("未标记的class:" + clazz);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sbf.toString();
	}

	// 只实现二维数组
	private static String printArray(Object value) throws Exception {
		if (!value.getClass().isArray()) {
			throw new IllegalStateException("输入的不是数组:" + value.getClass());
		}
		StringBuffer sbf = new StringBuffer();
		Class<?> componentType = value.getClass().getComponentType();// 原始类型{String[][]的原始类型是String[]}
		if (componentType.isArray()) {// 子类型是数组,循环
			// System.out.println("子类型:" + componentType);
			int length = Array.getLength(value);
			// System.out.println("外层长度" + length);
			sbf.append("[<BR/>");
			for (int i = 0; i < length; i++) {
				Object subValue = Array.get(value, i);
				int intLength = Array.getLength(subValue);
				sbf.append("&nbsp;&nbsp;&nbsp;&nbsp;").append("[");
				for (int j = 0; j < intLength; j++) {
					sbf.append(Array.get(subValue, j)).append(",");
				}
				sbf.append("]<BR/>");
			}
			sbf.append("]");
		} else {
			int length = Array.getLength(value);
			sbf.append("[");
			for (int i = 0; i < length; i++) {
				sbf.append(Array.get(value, i)).append(",");
			}
			sbf.append("]");
			// sbf.append(Arrays.toString((Object[]) value)).append("<BR/>");
		}

		return sbf.toString();
	}

	public static void main(String[] args) {
		TestCheck check = new TestCheck();
		check.init();
		System.out.println(check(check));
	}
}
