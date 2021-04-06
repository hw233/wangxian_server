package com.fy.engineserver.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import jxl.Cell;

/**
 * 字符串工具
 * 
 * 
 */
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
		try {
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

//			if (tempValue != null && tempValue.length > 0) {
//				T[] result = (T[]) Array.newInstance(clazz, tempValue.length);
//				for (int i = 0; i < tempValue.length; i++) {
//					String value = tempValue[i];
//					result[i] = constructor.newInstance(value);
//
//				}
//				return result;
//			}
			
			if (tempValue != null && tempValue.length > 0) {
				T[] result = (T[]) Array.newInstance(clazz, tempValue.length);
				for (int i = 0; i < tempValue.length; i++) {
					String value = tempValue[i];
					if (value.endsWith(".0") && (clazz == int.class || clazz == Integer.class)) {
						value = value.substring(0, value.length() - 2);
					}
					result[i] = constructor.newInstance(value);

				}
				return result;
			}
			
		} catch (Exception e) {
			System.out.println("解析异常:" + content + " | " + e.getMessage());
			e.printStackTrace();
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
			result = result.trim();// .replaceAll("[ ]*", "");
		}
		if (result.indexOf("　") != -1) {
			result = result.trim();// replaceAll("[　]*", "");
		}
		return result;
	}

	public static String modifyContent(Cell cell) {
		return modifyContent(cell.getContents());
	}

	public static Map<Class<?>, String> classDefaultValue = new HashMap<Class<?>, String>();
	public static Map<Class<?>, Class<?>> classMappedBox = new HashMap<Class<?>, Class<?>>();//

	static {
		classDefaultValue.put(String.class, "");
		classDefaultValue.put(Boolean.class, "false");
		classDefaultValue.put(boolean.class, "false");
		classDefaultValue.put(Integer.class, "0");
		classDefaultValue.put(int.class, "0");
		classDefaultValue.put(Short.class, "0");
		classDefaultValue.put(short.class, "0");
		classDefaultValue.put(Byte.class, "0");
		classDefaultValue.put(byte.class, "0");
		classDefaultValue.put(Long.class, "0");
		classDefaultValue.put(long.class, "0");
		classDefaultValue.put(Double.class, "0");
		classDefaultValue.put(double.class, "0");
		classDefaultValue.put(Float.class, "0");
		classDefaultValue.put(float.class, "0");
	}

	/**
	 * 根据要返回的类型获得excel某个单元格的值
	 * 如果单元格是空的,则返回每种类型的默认值:分别是 <li>String:""</li> <li>Boolean:false</li> <li>boolean:false</li> <li>Integer:0</li> <li>int:0</li> <li>Short:0</li> <li>short:0</li> <li>Byte:0</li>
	 * <li>byte:0</li> <li>Long:0</li> <li>long:0</li> <li>Double:0</li> <li>double:0</li> <li>Float:0</li> <li>float:0</li>
	 * @param cell
	 * @param clazz
	 * @return
	 */
	public static <T> T getCellValue(HSSFCell cell, Class<T> clazz) {
		return getCellValue(cell, clazz, null);
	}

	/**
	 * 根据要返回的类型获得excel某个单元格的值
	 * 如果单元格是空的,则返回预定义的默认值defaluetValue
	 * @param cell
	 * @param clazz
	 * @param defaultValue
	 * @return
	 */
	public static <T> T getCellValue(HSSFCell cell, Class<T> clazz, Object defaultValue) {
		clazz = (Class<T>) modifyClassToBox(clazz);
		if (cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			try {
				Constructor<T> constructor = clazz.getConstructor(String.class);
				return constructor.newInstance(defaultValue == null ? classDefaultValue.get(clazz) : defaultValue.toString());
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(clazz.getName() + "异常");
				return null;
			}
		}
		int cellType = cell.getCellType();
		String value = null;
		switch (cellType) {
		case HSSFCell.CELL_TYPE_BOOLEAN:
			value = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			value = String.valueOf(cell.getNumericCellValue());
			break;
		case HSSFCell.CELL_TYPE_STRING:
			value = cell.getStringCellValue();
			break;
		default:
			break;
		}
		if (value != null) {
			try {
				if (clazz.equals(String.class)) {
					return (T) String.valueOf(value);
				} else if (Boolean.class.equals(clazz) || boolean.class.equals(clazz)) {
					return (T) Boolean.valueOf(value);
				} else {// 数字
					Double d = Double.valueOf(value);
					if (Integer.class.equals(clazz) || int.class.equals(clazz)) {
						return (T) Integer.valueOf(d.intValue());
					} else if (Long.class.equals(clazz) || long.class.equals(clazz)) {
						return (T) Long.valueOf(d.longValue());
					} else if (Byte.class.equals(clazz) || byte.class.equals(clazz)) {
						return (T) Byte.valueOf(d.byteValue());
					} else if (Short.class.equals(clazz) || short.class.equals(clazz)) {
						return (T) Short.valueOf(d.shortValue());
					} else if (Double.class.equals(clazz) || double.class.equals(clazz)) {
						return (T) Double.valueOf(d.doubleValue());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
		throw new IllegalStateException("未识别的类型:" + cell.getCellType() + ",class:" + clazz.getName() + "[value:" + value + "]");
	}

	public static Class<?> modifyClassToBox(Class<?> clazz) {
		if (classMappedBox.containsKey(clazz)) {
			return classMappedBox.get(clazz);
		}
		return clazz;
	}

	public static void main(String[] args) {
		String file = "C:/Users//Desktop/exchangeActivity.xls";
		try {
			InputStream is = new FileInputStream(new File(file));
			POIFSFileSystem pss = new POIFSFileSystem(is);
			HSSFWorkbook workbook = new HSSFWorkbook(pss);

			HSSFSheet sheet = workbook.getSheetAt(0);
			HSSFRow row = sheet.getRow(2);
			for (int i = 0; i < 9; i++) {
				HSSFCell cell = row.getCell(i);
				System.out.println("[" + i + "] " + getCellValue(cell, Integer.class, 10000));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}