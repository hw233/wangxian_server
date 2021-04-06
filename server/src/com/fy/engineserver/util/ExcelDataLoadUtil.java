package com.fy.engineserver.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;

public class ExcelDataLoadUtil {

	public static List<?> loadExcelDataAsList(File file, int sheetIndex, Class<?> clazz, Logger log) throws Exception {
		// 固定格式---文件第一行为描述，留给策划使用 第二行为属性名（反射填充数值用） 数组使用;分隔 SimpleKey为map主键
//		File file = new File(this.fileName);
//		HSSFWorkbook book = null;
//		book = ExcelUtil.getWorkbook(file);
		InputStream is = new FileInputStream(file);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook book = new HSSFWorkbook(pss);
		HSSFSheet sheet = book.getSheetAt(sheetIndex);
		int num_row = sheet.getPhysicalNumberOfRows(); // 行
		HSSFRow row2 = sheet.getRow(1);
		int num_column = row2.getPhysicalNumberOfCells(); // 列
		List<Object> list = new ArrayList<Object>();
		for (int i = 2; i < num_row; i++) { // 有两行多余的
			Object o = clazz.newInstance();
			list.add(o);
		}
		for (int i = 0; i < num_column; i++) {
			Field f = null;
			for (int j = 1; j < num_row; j++) {
				HSSFRow row = sheet.getRow(j);
				Cell cell = row.getCell(i);
				if (j == 1) { // 属性名
					try {
						f = getDeclareField(clazz, cell.getStringCellValue());
					} catch (Exception e) {
						
					}
					if (f == null) {
						throw new NoSuchFieldException(cell.getStringCellValue());
					}
					f.setAccessible(true);
				} else {
					if (cell == null) {
//						log.error("[单元格为空] [filename:" + file.getAbsolutePath() + "] [sheetIndex:" + sheetIndex + "] [sheetName:" + sheet.getSheetName() + "] [cellIndex:" + i + "]");
						continue;
					}
					Object o = list.get(j - 2);
					if (f.getGenericType().equals(String.class)) {
						try {
							f.set(o, cell.getStringCellValue());
						} catch (Exception e) {
							f.set(o, cell.getNumericCellValue() + "");
						}
					} else if (f.getGenericType().equals(int.class)) {
						try {
							f.set(o, (int) cell.getNumericCellValue());
						} catch (Exception e) {
							f.set(o, Integer.parseInt(cell.getStringCellValue()));
						}
					} else if (f.getGenericType().equals(long.class)) {
						try {
							f.set(o, (long) cell.getNumericCellValue());
						} catch (Exception e) {
							f.set(o, Long.parseLong(cell.getStringCellValue()));
						}
					} else if (f.getGenericType().equals(short.class)) {
						try {
							f.set(o, (short) cell.getNumericCellValue());
						} catch (Exception e) {
							f.set(o, Short.parseShort(cell.getStringCellValue()));
						}
					} else if (f.getGenericType().equals(boolean.class)) {
						try {
							f.set(o, cell.getBooleanCellValue());
						} catch (Exception e) {
							f.set(o, !cell.getStringCellValue().equalsIgnoreCase("FALSE"));
						}
					} else if (f.getGenericType().equals(boolean[].class)) {
						try {
							String temp = cell.getStringCellValue();
							String[] split = temp.split(";");
							boolean[] result = new boolean[split.length];
							for (int kk=0; kk<result.length; kk++) {
								result[kk] = Boolean.parseBoolean(split[kk]);
							}
							f.set(o, result);
						} catch (Exception e) {
							f.set(o, new boolean[]{cell.getBooleanCellValue()});
						}
						
					} else if (f.getGenericType().equals(int[].class)) {
						try {
							String temp = cell.getStringCellValue();
							String[] split = temp.split(";");
							int[] result = new int[split.length];
							for (int kk = 0; kk < result.length; kk++) {
								result[kk] = Integer.parseInt(split[kk]);
							}
							f.set(o, result);
						} catch (Exception e) {
							try {
								f.set(o, new int[] { (int) cell.getNumericCellValue() });
							} catch (Exception e2) {
								f.set(o, new int[] { Integer.parseInt(cell.getStringCellValue()) });
							}
						}
					} else if (f.getGenericType().equals(long[].class)) {
						try {
							String temp = cell.getStringCellValue();
							String[] split = temp.split(";");
							long[] result = new long[split.length];
							for (int kk = 0; kk < result.length; kk++) {
								result[kk] = Long.parseLong(split[kk]);
							}
							f.set(o, result);
						} catch (Exception e) {
							f.set(o, new long[] { (long) cell.getNumericCellValue() });
						}
					} else if (f.getGenericType().equals(String[].class)) {
						try {
							String temp = cell.getStringCellValue();
							String[] split = temp.split(";");
							String[] result = new String[split.length];
							for (int kk = 0; kk < result.length; kk++) {
								result[kk] = split[kk];
							}
							f.set(o, result);
						} catch (Exception e) {
							String temp = cell.getNumericCellValue() + "";
							f.set(o, new String[] { temp });
						}
					} else {
						// System.out.println("[未知的数据类型] [filename:" + fileName + "] [数据类型:" + f.getGenericType() + "] [列民:" + f.getName() + "]");
						log.error("[未知的数据类型] [filename:" + file.getAbsolutePath() + "] [数据类型:" + f.getGenericType() + "] [列民:" + f.getName() + "]");
					}
				}
			}
		}
		for (int i = 0; i < list.size(); i++) {
			Object o = list.get(i);
			if (o instanceof NeedBuildExtraData) {
				((NeedBuildExtraData) o).buildExtraData();
			}
		}
		return list;
	}
	
	public static Field getDeclareField(Class<?> clazz, String filedName) {
		Field f = null;
		for (;clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				f = clazz.getDeclaredField(filedName);
			} catch (Exception e) {
				
			}
		}
		return f;
	}

	/**
	 * 加载excel文件，方法需要手动调用
	 * @param sheetIndex
	 *            sheet页index 0开始
	 * @param clazz
	 *            数据转换类
	 * @param log
	 * @return map<key,value> key=类中SimpleKey定义属性
	 * @throws Exception
	 */
	public static Map<?, ?> loadExcelData(File file, int sheetIndex, Class<?> clazz, Logger log) throws Exception {
		// 固定格式---文件第一行为描述，留给策划使用 第二行为属性名（反射填充数值用） 数组使用;分隔 SimpleKey为map主键
		@SuppressWarnings("unchecked")
		List<Object> list = (List<Object>) loadExcelDataAsList(file, sheetIndex, clazz, log);
		Map<Object, Object> resultMap = new LinkedHashMap<Object, Object>();
		Field idField = getFieldByAnnotation(clazz, SimpleKey.class);
		if (idField == null) {
			throw new Exception("[转化为map需要设置SimpleKey]");
		}
		idField.setAccessible(true);
		for (int i = 0; i < list.size(); i++) {
			Object o = list.get(i);
			Object key = idField.get(o);
			resultMap.put(key, o);
		}
		return resultMap;
	}

	@SuppressWarnings("rawtypes")
	public static Field getFieldByAnnotation(Class<?> clazz, Class annotation) {
		Field found = null;
		for (Field f : clazz.getDeclaredFields()) {
			@SuppressWarnings("unchecked")
			Annotation ann = f.getAnnotation(annotation);
			if (ann == null) {
				continue;
			} else {
				found = f;
				break;
			}
		}
		if (found == null) {
			if (clazz.getSuperclass().equals(Object.class)) {
				return null;
			}
			return getFieldByAnnotation(clazz.getSuperclass(), annotation);
		}
		return found;
	}

}
