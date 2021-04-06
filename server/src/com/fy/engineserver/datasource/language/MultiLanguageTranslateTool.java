package com.fy.engineserver.datasource.language;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.res.GameMap;
import com.fy.engineserver.datasource.language.MultiLanguageTranslateManager.ConvertFileConf;
import com.xuanzhi.tools.text.XmlContentExporter;

public class MultiLanguageTranslateTool {
	public static Logger logger = LoggerFactory.getLogger(MultiLanguageTranslateManager.class);
	public final static String webRoot = Class.class.getClass().getResource("/").getPath().replace("bin/", "").substring(1);
	public static final String translateOutputFile = webRoot + "conf/game_init_config/translate/translate.txt";
	public static final String excelOutputFile = webRoot + "conf/game_init_config/translate/excel.txt";
	public static final String excelConfFile = webRoot + "conf/game_init_config/translate/excelConfig.xls";

	public static final String xmlConOutputfFile = webRoot + "conf/game_init_config/translate/xml.txt";

	public static List<ConvertFileConf> excelConfList = new ArrayList<ConvertFileConf>();// 配置列表
	static HashMap<String, String> excelCfgFileMapped = new HashMap<String, String>(); // 配置文件名称映射
	static Set<String> xmlCfgFileMapped = new HashSet<String>(); // xml配置文件名称映射

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
	}

	private static List<String> listXmlFiles(File root) {
		List<String> list = new ArrayList<String>();

		if (!root.exists()) {
			return list;
		}
		if (root.isDirectory()) {
			File[] files = root.listFiles();
			for (File f : files) {
				list.addAll(listXmlFiles(f));
			}
		} else {
			if (root.getAbsolutePath().endsWith(".xml")) {
				list.add(root.getAbsolutePath());
			}
		}
		return list;
	}

	/**
	 * 得到基础的数据,写入文件,并且返回内容,返回用于检查是否已经生效
	 * (针对Translate.java)
	 * @param translate
	 * @param createFile
	 * @return
	 * @throws Exception
	 */
	private static Map<String, String> getTranslateClassFieldAndValue(Translate translate, boolean createFile) throws Exception {
		Field[] fileds = translate.getClass().getFields();
		Map<String, String> map = new LinkedHashMap<String, String>();
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
			File file = new File(translateOutputFile);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				for (Iterator<String> itor = map.keySet().iterator(); itor.hasNext();) {
					String key = itor.next();
					String value = map.get(key);
					String str = key + "\t" + value + "\t" + value + "\n";
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

	public static Map<String, String> getCurrentFieldAndValue(Translate translate) throws Exception {
		return getTranslateClassFieldAndValue(translate, false);
	}

	/**
	 * 生成所有的excel配置信息.
	 */
	private static void prepareExcelConf() throws Exception {
		File file = new File(excelConfFile);
		if (!file.exists()) {
			System.err.println("[excel配置文件不存在:" + file.getAbsolutePath() + "]");
			return;
		}

		String confBaseRoot = webRoot + "conf/game_init_config/";

		// <文件名,<sheet名字,List<列>>>
		HashMap<String, HashMap<String, List<Integer>>> tempMap = new HashMap<String, HashMap<String, List<Integer>>>();

		InputStream is = new FileInputStream(file);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(0);

		int maxRow = sheet.getPhysicalNumberOfRows();

		for (int i = 1; i < maxRow; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row != null) {
				String fileName = row.getCell(0).getStringCellValue();
				String sheetName = row.getCell(1).getStringCellValue();
				int column = (int) row.getCell(2).getNumericCellValue();
				if (!tempMap.containsKey(fileName)) {
					tempMap.put(fileName, new HashMap<String, List<Integer>>());
				}
				if (!tempMap.get(fileName).containsKey(sheetName)) {
					tempMap.get(fileName).put(sheetName, new ArrayList<Integer>());
				}
				tempMap.get(fileName).get(sheetName).add(column);
			}
		}
		for (Iterator<String> itor = tempMap.keySet().iterator(); itor.hasNext();) {
			String fileName = itor.next();// 文件名
			HashMap<String, List<Integer>> hashMap = tempMap.get(fileName);
			String[] sheets = hashMap.keySet().toArray(new String[0]);// 所有的sheet
			Integer[][] columns = new Integer[sheets.length][];
			for (int i = 0; i < sheets.length; i++) {
				String sheetName = sheets[i];
				List<Integer> list = hashMap.get(sheetName);
				columns[i] = list.toArray(new Integer[0]);
			}
			ConvertFileConf conf = MultiLanguageTranslateManager.getInstance().new ConvertFileConf(confBaseRoot + modifyExcelCfgFilePath(fileName), sheets, columns);
			excelConfList.add(conf);
			System.out.println(conf.toString());
		}
	}

	/**
	 * 提取所有的需要转换的配置文件的字段(Excel)
	 */
	private static void loadNeedConvertExcelFiles() throws Exception {
		File outFile = new File(excelOutputFile);
		outFile.delete();
		outFile.createNewFile();

		if (excelConfList == null || excelConfList.size() == 0) {
			System.err.println("[config是空的]");
			return;
		}
		FileOutputStream fos = new FileOutputStream(outFile);
		Set<String> values = new HashSet<String>();
		try {
			for (ConvertFileConf conf : excelConfList) {
				if (conf != null) {
					File f = new File(conf.getPath());
					if (!f.exists()) {
						System.err.println("[不存在的配置文件:" + f.getAbsolutePath() + "]");
						return;
					}
					InputStream is = new FileInputStream(f);
					POIFSFileSystem pss = new POIFSFileSystem(is);
					HSSFWorkbook workbook = new HSSFWorkbook(pss);

					for (int i = 0; i < conf.getSheets().length; i++) {
						String sheetName = conf.getSheets()[i];
						if (sheetName != null && !"".equals(sheetName.trim())) {
							HSSFSheet sheet = workbook.getSheet(sheetName); // 配置的某一页
							if (sheet == null) {
								System.err.println("[配置文件的sheet不存在] [文件:" + f.getAbsolutePath() + "] [sheet:" + sheetName + "]");
							} else {
								int maxRow = sheet.getPhysicalNumberOfRows();// 所有行
								for (int rowIndex = 0; rowIndex < maxRow; rowIndex++) {
									HSSFRow row = sheet.getRow(rowIndex);
									if (row != null) {
										for (int columnIndex = 0; columnIndex < conf.getColumns()[i].length; columnIndex++) {
											// 所有列
											int columnNum = conf.getColumns()[i][columnIndex];
											HSSFCell cell = row.getCell(columnNum);
											if (cell != null) {
												String value = null;
												try {
													value = cell.getStringCellValue().trim().replace("\n", MultiLanguageTranslateManager.separator_n);
												} catch (Exception e) {
													value = String.valueOf(cell.getNumericCellValue());
													System.err.println("[不是字符串] [file:" + conf.getPath() + "] [页:" + sheetName + "] [列:" + columnNum + "] [行:" + rowIndex + "] [value:" + value + "]");
												}
												if (MultiLanguageTranslateManager.isValid(value)) {
													values.add(value);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			System.out.println("[所有要翻译的配置文件中的数量:" + values.size() + "]");
			for (String value : values) {
				String line = value + "\t" + value + "\n";
				fos.write(line.getBytes("UTF-8"));
			}
			fos.flush();
			System.out.println("[配置文件字库生成完毕]");
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	/**
	 * 生成要翻译的字典@see
	 * @throws Exception
	 */
	public static void createDictionary() throws Exception {
		long startTime = System.currentTimeMillis();
		/** 生成translate.java中的字段和值 */
		getTranslateClassFieldAndValue(new Translate(), true);
		System.out.println("[获得Translate.java字段] [累计耗时:" + (System.currentTimeMillis() - startTime) + "ms]");

		prepareXMLConf();
		// prepareExcelConf();
		// System.out.println("[准备Excel配置表              ] [累计耗时:" + (System.currentTimeMillis() - startTime) + "ms]");
		// /** 生成所有excel中字典表 */
		// loadNeedConvertExcelFiles();
		// System.out.println("[提取Excel字段                 ] [累计耗时:" + (System.currentTimeMillis() - startTime) + "ms]");
	}

	/**
	 * 准备XML配置信息,生成文件
	 */
	private static void prepareXMLConf() throws Exception {
		long startTime = System.currentTimeMillis();
		String[] files = listXmlFiles(new File(webRoot + "/conf/game_init_config")).toArray(new String[0]);
		System.out.println("[查询所有xml配置文件] [数量:" + files.length + "] [耗时" + (System.currentTimeMillis() - startTime) + "]");
		System.out.println(Arrays.toString(files));
		XmlContentExporter exporter = new XmlContentExporter(files);
		exporter.exportData();

		List<String> list = exporter.getContentList();// 得到所有解析后的中文

		list.addAll(getBindata());

		File file = createEmptyFile(xmlConOutputfFile);

		FileOutputStream fos = new FileOutputStream(file);
		try {
			Set<String> set = new HashSet<String>();
			for (String s : list) {
				if (s != null && s.length() > 0 && s.length() != s.getBytes().length) {
					set.add(s);
				} else {
					System.err.println("被过滤掉的字符:" + s);
				}
			}
			for (String s : set) {
				// s = s.replace("\n", MultiLanguageTranslateManager.separator_n);
				String value = s + "\n";
				fos.write(value.getBytes("UTF-8"));
			}
			fos.flush();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	static Set<String> getBindata() {
		FileFilter gamemapFilter = new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getAbsolutePath().endsWith(".xmd");
			}
		};
		Set<String> set = new HashSet<String>();
		File dir = new File(webRoot + "\\conf\\game_init_config\\bindata\\map\\lowMap");
		File[] fs = dir.listFiles(gamemapFilter);
		for (File f : fs) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(f);
				DataInputStream dis = new DataInputStream(fis);
				List<String> v = GameMap.getStringArrayFromGameMapStream(dis);
				set.addAll(v);
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return set;
	}

	public static Set<String> getSendClientBindata() {
		FileFilter gamemapFilter = new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getAbsolutePath().endsWith(".xmd");
			}
		};
		Set<String> set = new HashSet<String>();
		File dir = new File(webRoot + "\\conf\\game_init_config\\bindata\\map\\lowMap");
		File[] fs = dir.listFiles(gamemapFilter);
		for (File f : fs) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(f);
				DataInputStream dis = new DataInputStream(fis);
				List<String> v = GameMap.getSendClientFromGameMapStream(dis);
				set.addAll(v);
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Set<String> s = new HashSet<String>();
		for (String ss : set) {
			if (ss != null && ss.length() != ss.getBytes().length) {
				s.add(ss);
			}
		}
		return s;
	}

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	private static File createEmptyFile(String filePath) throws Exception {
		File file = new File(filePath);
		if (file.exists()) {// 文件存在,创建备份文件,并创建新文件
			String newpath = file.getParent() + "\\bak\\" + sdf.format(new Date()) + file.getName();
			file.renameTo(new File(newpath));
		}
		File newFile = new File(filePath);
		if (!newFile.createNewFile()) {
			throw new Exception("创建文件失败:" + filePath);
		}
		return newFile;
	}

	public static void main(String[] args) throws Exception {
//		createDictionary();
//		System.out.println(getSendClientBindata());
		System.out.println(sdf.format(System.currentTimeMillis()));
		System.out.println("======================================================================================");
		System.out.println("执行步骤如下:");
		System.out.println("1) 将translate.txt中的内容copy到taiwan.xls文件的第一页的a,b列,翻译第二列,第一列为原始值");
		System.out.println("1) 将xml.txt中的内容copy到taiwan.xls文件的第二页的a,b列,翻译第二列,第一列为原始值");
		System.out.println("======================================================================================");
	}
}
