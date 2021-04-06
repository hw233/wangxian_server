package com.fy.gamegateway.language;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiLanguageTranslateTool {
	public static Logger logger = LoggerFactory.getLogger(MultiLanguageTranslateManager.class);
	public final static String webRoot = Class.class.getClass().getResource("/").getPath().replace("bin/", "").substring(1);
	public static final String translateOutputFile = webRoot + "/translate.txt";
	public static final String excelOutputFile = webRoot + "excel.txt";
	public static final String excelConfFile = webRoot + "excelConfig.xls";

	static HashMap<String, String> excelCfgFileMapped = new HashMap<String, String>(); // 配置文件名称映射

	static {
		excelCfgFileMapped.put("taskTemplet.xls", "task/taskTemplet.xls");
		excelCfgFileMapped.put("septConf.xls", "septstation/septConf.xls");
		excelCfgFileMapped.put("cave.xls", "cave/cave.xls");
		excelCfgFileMapped.put("bourn.xls", "bourn/bourn.xls");
		excelCfgFileMapped.put("activityIntroduce.xls", "activity/activityIntroduce.xls");
		excelCfgFileMapped.put("farming.xls", "activity/farming.xls");
		excelCfgFileMapped.put("silvercar.xls", "activity/silvercar.xls");
		excelCfgFileMapped.put("feedSilkworm.xls", "activity/feedSilkworm.xls");
		excelCfgFileMapped.put("forLuckFruit.xls", "activity/forLuckFruit.xls");
		excelCfgFileMapped.put("peekAndbrick.xls", "activity/peekAndbrick.xls");
		excelCfgFileMapped.put("peoplesearch.xls", "activity/peoplesearch.xls");
		excelCfgFileMapped.put("translate.xls", "conf/korea/translate.xls");
	}

	/**
	 * 得到基础的数据,写入文件,并且返回内容,返回用于检查是否已经生效
	 * (针对Translate.java)
	 * @param translate
	 * @param createFile
	 * @return
	 * @throws Exception
	 */
	private static HashMap<String, String> getTranslateClassFieldAndValue(Translate translate, boolean createFile) throws Exception {
		Field[] fileds = translate.getClass().getFields();
		HashMap<String, String> map = new HashMap<String, String>();
		if (fileds != null) {
			for (Field field : fileds) {
				DonotTranslate annotation = field.getAnnotation(DonotTranslate.class);
				if (annotation != null) {
					System.err.println("不用翻译的字段:" + field.toString());
					continue;
				}
				try {
					if (field != null) {
						String key = field.getName();
						Object o = field.get(translate);
						String value = null;
						if (o instanceof String) {
							value = ((String) o).replace("\n", MultiLanguageTranslateManager.separator_n);
						} else if (o.getClass().isArray()) {
							value = MultiLanguageTranslateManager.arrayToString(o);
							value = value.replace("\n", MultiLanguageTranslateManager.separator_n);
						}
						if (!map.containsKey(key)) {
							map.put(key, value);
						}
					}
				} catch (Exception e) {
					logger.error("[本地化] [getCurrentFieldAndValue] [异常]", e);
				}
			}
		}
		if (createFile) {
			String filePath = "/" + translateOutputFile;
			System.out.println(filePath);
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				for (Iterator<String> itor = map.keySet().iterator(); itor.hasNext();) {
					String key = itor.next();
					String value = map.get(key);
					String str = key + "\t" + value + "\t" + value + "\r\n";
					fos.write(str.getBytes("UTF-8"));
				}
				fos.flush();
			} finally {
				if (fos != null) {
					fos.close();
				}
			}
		}
		return map;
	}

	private static String modifyExcelCfgFilePath(String fileName) {
		if (excelCfgFileMapped.containsKey(fileName)) {
			return excelCfgFileMapped.get(fileName);
		}
		return fileName;
	}

	public static HashMap<String, String> getCurrentFieldAndValue(Translate translate) throws Exception {
		return getTranslateClassFieldAndValue(translate, false);
	}

	/**
	 * 生成所有的excel配置信息.
	 */
//	private static void prepareExcelConf() throws Exception {
//		File file = new File(excelConfFile);
//		if (!file.exists()) {
//			System.err.println("[excel配置文件不存在:" + file.getAbsolutePath() + "]");
//			return;
//		}
//
//		String confBaseRoot = webRoot + "conf/game_init_config/";
//
//		// <文件名,<sheet名字,List<列>>>
//		HashMap<String, HashMap<String, List<Integer>>> tempMap = new HashMap<String, HashMap<String, List<Integer>>>();
//
//		InputStream is = new FileInputStream(file);
//		POIFSFileSystem pss = new POIFSFileSystem(is);
//		HSSFWorkbook workbook = new HSSFWorkbook(pss);
//		HSSFSheet sheet = workbook.getSheetAt(0);
//
//		int maxRow = sheet.getPhysicalNumberOfRows();
//
//		for (int i = 1; i < maxRow; i++) {
//			HSSFRow row = sheet.getRow(i);
//			if (row != null) {
//				String fileName = row.getCell((short)0).getStringCellValue();
//				String sheetName = row.getCell((short)1).getStringCellValue();
//				int column = (int) row.getCell((short)2).getNumericCellValue();
//				if (!tempMap.containsKey(fileName)) {
//					tempMap.put(fileName, new HashMap<String, List<Integer>>());
//				}
//				if (!tempMap.get(fileName).containsKey(sheetName)) {
//					tempMap.get(fileName).put(sheetName, new ArrayList<Integer>());
//				}
//				tempMap.get(fileName).get(sheetName).add(column);
//			}
//		}
//		for (Iterator<String> itor = tempMap.keySet().iterator(); itor.hasNext();) {
//			String fileName = itor.next();// 文件名
//			HashMap<String, List<Integer>> hashMap = tempMap.get(fileName);
//			String[] sheets = hashMap.keySet().toArray(new String[0]);// 所有的sheet
//			Integer[][] columns = new Integer[sheets.length][];
//			for (int i = 0; i < sheets.length; i++) {
//				String sheetName = sheets[i];
//				List<Integer> list = hashMap.get(sheetName);
//				columns[i] = list.toArray(new Integer[0]);
//			}
//			ConvertFileConf conf = MultiLanguageTranslateManager.getInstance().new ConvertFileConf(confBaseRoot + modifyExcelCfgFilePath(fileName), sheets, columns);
//			excelConfList.add(conf);
//			System.out.println(conf.toString());
//		}
//	}

	/**
	 * 提取所有的需要转换的配置文件的字段(Excel)
	 */
//	private static void loadNeedConvertExcelFiles() throws Exception {
//		File outFile = new File(excelOutputFile);
//		outFile.delete();
//		outFile.createNewFile();
//
//		if (excelConfList == null || excelConfList.size() == 0) {
//			System.err.println("[config是空的]");
//			return;
//		}
//		FileOutputStream fos = new FileOutputStream(outFile);
//		Set<String> values = new HashSet<String>();
//		try {
//			for (ConvertFileConf conf : excelConfList) {
//				if (conf != null) {
//					File f = new File(conf.getPath());
//					if (!f.exists()) {
//						System.err.println("[不存在的配置文件:" + f.getAbsolutePath() + "]");
//						return;
//					}
//					InputStream is = new FileInputStream(f);
//					POIFSFileSystem pss = new POIFSFileSystem(is);
//					HSSFWorkbook workbook = new HSSFWorkbook(pss);
//
//					for (int i = 0; i < conf.getSheets().length; i++) {
//						String sheetName = conf.getSheets()[i];
//						if (sheetName != null && !"".equals(sheetName.trim())) {
//							HSSFSheet sheet = workbook.getSheet(sheetName); // 配置的某一页
//							if (sheet == null) {
//								System.err.println("[配置文件的sheet不存在] [文件:" + f.getAbsolutePath() + "] [sheet:" + sheetName + "]");
//							} else {
//								int maxRow = sheet.getPhysicalNumberOfRows();// 所有行
//								for (int rowIndex = 0; rowIndex < maxRow; rowIndex++) {
//									HSSFRow row = sheet.getRow(rowIndex);
//									if (row != null) {
//										for (int columnIndex = 0; columnIndex < conf.getColumns()[i].length; columnIndex++) {
//											// 所有列
//											int columnNum = conf.getColumns()[i][columnIndex];
//											HSSFCell cell = row.getCell(columnNum);
//											if (cell != null) {
//												String value = null;
//												try {
//													value = cell.getStringCellValue().trim().replace("\n", MultiLanguageTranslateManager.separator_n);
//												} catch (Exception e) {
//													value = String.valueOf(cell.getNumericCellValue());
//													System.err.println("[不是字符串] [file:" + conf.getPath() + "] [页:" + sheetName + "] [列:" + columnNum + "] [行:" + rowIndex + "] [value:" + value + "]");
//												}
//												if (MultiLanguageTranslateManager.isValid(value)) {
//													values.add(value);
//												}
//											}
//										}
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//			System.out.println("[所有要翻译的配置文件中的数量:" + values.size() + "]");
//			for (String value : values) {
//				String line = value + "\t" + value + "\n";
//				fos.write(line.getBytes("UTF-8"));
//			}
//			fos.flush();
//			System.out.println("[配置文件字库生成完毕]");
//		} finally {
//			if (fos != null) {
//				fos.close();
//			}
//		}
//	}

	/**
	 * 生成要翻译的字典@see
	 * @throws Exception
	 */
	public static void createDictionary() throws Exception {
		long startTime = System.currentTimeMillis();
		/** 生成translate.java中的字段和值 */
		getTranslateClassFieldAndValue(new Translate(), true);
		System.out.println("[获得Translate.java字段] [累计耗时:" + (System.currentTimeMillis() - startTime) + "ms]");
//		prepareExcelConf();
//		System.out.println("[准备Excel配置表              ] [累计耗时:" + (System.currentTimeMillis() - startTime) + "ms]");
//		/** 生成所有excel中字典表 */
//		loadNeedConvertExcelFiles();
		System.out.println("[提取Excel字段                 ] [累计耗时:" + (System.currentTimeMillis() - startTime) + "ms]");
	}

	public static void main(String[] args) throws Exception {
		
		System.out.println(webRoot);
		createDictionary();
	}
}
