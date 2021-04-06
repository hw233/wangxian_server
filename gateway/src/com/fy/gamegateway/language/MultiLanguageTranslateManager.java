package com.fy.gamegateway.language;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MultiLanguageTranslateManager {

	public static Logger logger = LoggerFactory.getLogger(MultiLanguageTranslateManager.class);

	/** 读取文件路径 */
	private String filePath;
	/** 是否需要翻译 */
	private boolean needTranslate;

	private HashMap<String, String> translatedMap = new HashMap<String, String>();

	private static MultiLanguageTranslateManager instance;

	public MultiLanguageTranslateManager() {

	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isNeedTranslate() {
		return needTranslate;
	}

	public void setNeedTranslate(boolean needTranslate) {
		this.needTranslate = needTranslate;
	}

	public HashMap<String, String> getTranslatedMap() {
		return translatedMap;
	}

	public void setTranslatedMap(HashMap<String, String> translatedMap) {
		this.translatedMap = translatedMap;
	}

	public static MultiLanguageTranslateManager getInstance() {
		if (instance == null) {
			System.out.println("新建MultiLanguageTranslateManager对象");
			instance = new MultiLanguageTranslateManager();
		}
		return instance;
	}

	public void init() throws Throwable {
		long start = System.currentTimeMillis();
		if (needTranslate) { // 需要加载文件
			File file = new File(filePath);
			if (!file.exists()) {
				throw new IllegalStateException("[本地化文件不存在:" + file.getAbsolutePath() + "]");
			}
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			HSSFWorkbook workbook = new HSSFWorkbook(pss);

			HSSFSheet sheet = workbook.getSheetAt(0);// translate.java中的内容
			int maxRow = sheet.getPhysicalNumberOfRows();
			for (int i = 0; i < maxRow; i++) {
				HSSFRow row = sheet.getRow(i);
				HSSFCell cell = row.getCell((short)0);
				if (cell != null) {
					String baseName = cell.getStringCellValue();
					cell = row.getCell((short)1);
					if (cell != null) {
						String translate = cell.getStringCellValue();
						putTranslatedMap(baseName, translate);
					} else {
						logger.error("[本地化] [含有空的value] [key:" + baseName + "]");
					}
				} else {
					logger.error("[本地化] [含有空的key] [行数:" + i + "]");
				}
			}

			putValueToTranslate();
			// 读文件.取出所有的配置表的数据,放入map
//			sheet = workbook.getSheetAt(0);// translate.java中的内容
//			maxRow = sheet.getPhysicalNumberOfRows();
//			for (int i = 0; i < maxRow; i++) {
//				HSSFRow row = sheet.getRow(i);
//				HSSFCell cell = row.getCell((short)0);
//				if (cell != null) {
//					String baseName = null;
//					try {
//						baseName = cell.getStringCellValue();
//					} catch (Exception e) {
//						baseName = String.valueOf(cell.getNumericCellValue());
//					}
//					cell = row.getCell((short)1);
//					if (cell != null) {
//						String translate = null;
//						try {
//							translate = cell.getStringCellValue().trim().replace(separator_n, "\n");
//						} catch (Exception e) {
//							translate = String.valueOf(cell.getNumericCellValue());
//						}
//						putTranslatedMap(baseName, translate);
//					} else {
//						logger.error("[本地化] [含有空的value] [key:" + baseName + "]");
//					}
//				} else {
//					logger.error("[本地化] [含有空的key] [行数:" + i + "]");
//				}
//			}
		}
		instance = this;
		logger.error("[本地化] [执行完毕] [是否需要本地化:" + needTranslate + "] [耗时:" + (System.currentTimeMillis() - start) + "ms]");
	}

	private void putTranslatedMap(String baseName, String translate) {
		if (isValid(baseName) && isValid(translate)) {
			if (!translatedMap.containsKey(baseName)) {
				translatedMap.put(baseName, translate);
			}
		}
	}

	// public static String needTranslate(HSSFCell cell, String value) {
	// if (!getInstance().needTranslate) {
	// return value;
	// }
	// if (cell == null) {
	// return value;
	// }
	// return value;
	// }
	// private static boolean needTranslate(HSSFCell cell) {
	// cell.getSheet().getWorkbook().
	// return false;
	// }

	/**
	 * 获得翻译后的文字
	 * @param value
	 * @return
	 */
	public static String languageTranslate(String value) {
		if (!getInstance().needTranslate) {
			return value;
		}
		if (getInstance().translatedMap.containsKey(value)) {
			return getInstance().translatedMap.get(value);
		}
		return value;
	}

	private void putValueToTranslate() throws Throwable {
		if (needTranslate) {
			Translate translate = new Translate();
			Field[] fields = translate.getClass().getDeclaredFields();
			if (fields != null) {
				for (Field field : fields) {

					DonotTranslate annotation = field.getAnnotation(DonotTranslate.class);
					if (annotation != null) {
						System.err.println("不用翻译的字段:" + field.toString());
						continue;
					}
					if (field != null) {
						String fieldName = field.getName();
						String value = translatedMap.get(fieldName);
						if (value != null) {
							Object object = field.get(translate);
							if (object.getClass().isArray()) {// 如果是数组
								logger.warn("[本地化] [数组] [" + object.getClass() + "] [" + field.getName() + "] [" + stringToArray(value) + "]");
								field.set(translate, stringToArray(value));
							} else {
								logger.warn("[本地化] [非数组] [" + object.getClass() + "] [" + field.getName() + "] [" + value + "]");
								value = value.replace(separator_n, "\n");
								field.set(translate, value);
							}
						} else {
							logger.warn("[本地化] [文件缺少字段] [" + field.getName() + "] [" + value + "]");
						}
					}
				}
			}
		}
	}

	public static final String array_separator = "#####";
	public static final String separator_n = "^^^^^";

	static String arrayToString(Object object) {
		int length = Array.getLength(object);
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sbf.append(Array.get(object, i));
			if (i != length - 1) {
				sbf.append(array_separator);
			}
		}
		return sbf.toString();
	}

	private String[] stringToArray(String string) throws Throwable {
		string = string.replace(separator_n, "\n");
		return StringTool.string2Array(string, array_separator, String.class);
	}

	static boolean isValid(String value) {
		if (value == null) {
			return false;
		}
		if ("".equals(value.trim())) {
			return false;
		}
		return true;
	}

	class ConvertFileConf {
		String path;
		String[] sheets;
		Integer[][] columns;

		public ConvertFileConf(String path, String[] sheets, Integer[][] columns) {
			super();
			this.path = path;
			this.sheets = sheets;
			this.columns = columns;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String[] getSheets() {
			return sheets;
		}

		public void setSheets(String[] sheets) {
			this.sheets = sheets;
		}

		public Integer[][] getColumns() {
			return columns;
		}

		public void setColumns(Integer[][] columns) {
			this.columns = columns;
		}

		@Override
		public String toString() {
			return "ConvertFileConf [path=" + path + ", sheets=" + Arrays.toString(sheets) + ", columns=" + Arrays.toString(columns) + "]";
		}
	}
}
