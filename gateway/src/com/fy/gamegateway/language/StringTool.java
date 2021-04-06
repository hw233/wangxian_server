package com.fy.gamegateway.language;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.ParseException;

public class StringTool {

	public static String[] string2StringArr(String content, String separator) {
		return content.split(separator);
	}

	public static int[] stringArr2IntArr(String[] stringArr) {
		int[] intArr = new int[stringArr.length];
		for (int i = stringArr.length - 1; i >= 0; i--) {
			intArr[i] = Integer.valueOf(stringArr[i]);
		}
		return intArr;
	}

	public static double[] stringArr2DoubleArr(String[] stringArr) {
		double[] doubleArr = new double[stringArr.length];
		for (int i = stringArr.length - 1; i >= 0; i--) {
			doubleArr[i] = Double.valueOf(stringArr[i]);
		}
		return doubleArr;
	}

	public static double[] stringArr2DoubleArr(String[] stringArr, DecimalFormat df) {
		double[] doubleArr = new double[stringArr.length];
		for (int i = stringArr.length - 1; i >= 0; i--) {
			try {
				doubleArr[i] = df.parse(stringArr[i]).doubleValue();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return doubleArr;
	}

	public static long[] stringArr2LongArr(String[] stringArr) {
		long[] longArr = new long[stringArr.length];
		for (int i = stringArr.length - 1; i >= 0; i--) {
			longArr[i] = Long.valueOf(stringArr[i]);
		}
		return longArr;
	}

	public static int[] string2IntArr(String content, String separator) {
		return stringArr2IntArr(string2StringArr(content, separator));
	}

	public static double[] string2DoubleArr(String content, String separator) {
		return stringArr2DoubleArr(string2StringArr(content, separator));
	}

	public static long[] string2LongArr(String content, String separator) {
		return stringArr2LongArr(string2StringArr(content, separator));
	}

	public static String longArr2String(long[] arr, String separator) {
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i < arr.length - 1; i++) {
			sbf.append(arr[i]).append(separator);
		}
		sbf.append(arr[arr.length - 1]);
		return sbf.toString();
	}

	public static String stringArr2String(String[] arr, String separator) {
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i < arr.length - 1; i++) {
			sbf.append(arr[i]).append(separator);
		}
		sbf.append(arr[arr.length - 1]);
		return sbf.toString();
	}

	public static String intArr2String(int[] arr, String separator) {
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i < arr.length - 1; i++) {
			sbf.append(arr[i]).append(separator);
		}
		sbf.append(arr[arr.length - 1]);
		return sbf.toString();
	}

	/**
	 * 可由字符串转成数组的类型列表
	 */
	private final static Class<?>[] canturnTypes = { byte.class, Byte.class, Short.class, Integer.class, Double.class, Float.class, String.class, Long.class };

	/**
	 * 将字符串转化成数组
	 * @param <T>
	 * @param content
	 * @param separator
	 * @param clazz
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] string2Array(String content, String separator, Class<T> clazz) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		if (content == null) {
			throw new IllegalStateException("input is null");
		}
		boolean find = false;
		int checkIndex = 0;
		while (!find) {
			if (checkIndex > canturnTypes.length - 1) {
				break;
			}
			find = canturnTypes[checkIndex++].equals(clazz);
		}
		if (!find) {
			throw new IllegalStateException("Can't deal with class :" + clazz.getName());
		}
		Constructor<T> constructor = clazz.getConstructor(String.class);
		String[] tempValue = content.split(separator);
		if (String.class.equals(clazz)) {
			return (T[]) tempValue;
		}

		if (tempValue != null && tempValue.length > 0) {
			T[] result = (T[]) Array.newInstance(clazz, tempValue.length);
			for (int i = 0; i < tempValue.length; i++) {
				String value = tempValue[i];
				result[i] = constructor.newInstance(value);

			}
			return result;
		}
		throw new IllegalStateException();
	}

	/**
	 * 将数组内容转成String
	 * @param <T>
	 * @param array
	 * @param separator
	 * @return
	 */
	public static <T> String array2String(T[] array, String separator) {
		if (array != null && array.length > 0) {
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < array.length - 1; i++) {
				buffer.append(array[i].toString()).append(separator);
			}
			buffer.append(array[array.length - 1]);
			return buffer.toString();
		}
		return null;
	}

	/**
	 * 给客户端发送可点击的信息
	 * @return
	 */
	public static String toClientClickMsg(int color, String msg, long objectId, int onclickType) {
		StringBuffer sbf = new StringBuffer("<f ");
		sbf.append("decoration='8' ").append(" onclickType='").append(onclickType).append("' color='").append(color).append("' playerId='").append(objectId).append("'>【").append(msg).append("】</f>");
		return sbf.toString();
	}

	public static String toColorfulMessage(String msg, int color) {
		StringBuffer sbf = new StringBuffer("<f color='");
		sbf.append(color);
		sbf.append("'>").append(msg).append("</f>");
		return sbf.toString();

	}

	/**
	 * 对excel表中输入的内容做修正<BR/>
	 * 1.删除所有的半角空格[ ]<BR/>
	 * 2.删除所有的全角空格[　]<BR/>
	 * @param input
	 * @return
	 */
	public static String modifyContent(String input) {
		String result = input;
		if (result.indexOf(" ") != -1) {
			result = result.replaceAll("[ ]*", "");
		}
		if (result.indexOf("　") != -1) {
			result = result.replaceAll("[　]*", "");
		}
		return result;
	}

//	public static String modifyContent(Cell cell) {
//		return modifyContent(cell.getContents());
//	}

}
