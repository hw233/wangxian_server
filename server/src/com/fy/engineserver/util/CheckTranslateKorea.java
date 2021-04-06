package com.fy.engineserver.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.HashMap;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class CheckTranslateKorea {

	public static String projectPath = "";
	
//	public static void jiejueChongTu()throws Exception {
//		File f1 = new File("E://work//mieshi_server//game_mieshi_server//conf//game_init_config_korea//translate//korea111.xls");
//		File f2 = new File("E://work//mieshi_server//game_mieshi_server//conf//game_init_config_korea//translate//korea.xls");
//		
//		Workbook workbook1 = Workbook.getWorkbook(f1);
//		Sheet sheet1 = workbook1.getSheet(0);
//		
//		Workbook workbook2 = Workbook.getWorkbook(f2);
//		Sheet sheet2 = workbook2.getSheet(0);
//		
//		for (int i = 0, n = sheet1.getRows(); i < n; i++) {
//			Cell[] cells1 = sheet1.getRow(i);
//			Cell[] cells2 = sheet2.getRow(i);
//			if (!cells1[1].getContents().equals(cells2[1].getContents())) {
//				System.out.println(cells1[0].getContents());
//			}
//		}
//	}
	
	public static void translateMore(File old, File current, File out1, File out2) throws Exception {
//		File f1 = new File("E://work//mieshi_server//game_mieshi_server//conf//game_init_config_korea//translate//korea111.xls");
//		File f2 = new File("E://work//mieshi_server//game_mieshi_server//conf//game_init_config_korea//translate//korea.xls");
		
		{
			Workbook workbook1 = Workbook.getWorkbook(current);
			Sheet sheet1 = workbook1.getSheet(0);
			HashMap<String, String> map1 = new HashMap<String, String>();
			for (int i = 0, n = sheet1.getRows(); i < n; i++) {
				Cell[] cells1 = sheet1.getRow(i);
				if (cells1.length > 1) {
					map1.put(cells1[0].getContents(), cells1[1].getContents());
				}
			}
			
			Workbook workbook2 = Workbook.getWorkbook(old);
			Sheet sheet2 = workbook2.getSheet(0);
			HashMap<String, String> map2 = new HashMap<String, String>();
			HashMap<String, String> map22 = new HashMap<String, String>();
			for (int i = 0, n = sheet2.getRows(); i < n; i++) {
				Cell[] cells2 = sheet2.getRow(i);
				if (cells2.length > 1) {
					map2.put(cells2[0].getContents(), cells2[1].getContents());
				}
			}
			FileOutputStream fo = new FileOutputStream(out1);
			for (String key : map1.keySet()) {
				String v = map2.get(key);
				if (v == null) {
					fo.write(key.getBytes());
					fo.write("\t".getBytes());
					fo.write(map1.get(key).getBytes());
					fo.write("\n".getBytes());
				}
			}
			fo.close();
		}
		

		{
			Workbook workbook1 = Workbook.getWorkbook(current);
			Sheet sheet1 = workbook1.getSheet(1);
			HashMap<String, String> map1 = new HashMap<String, String>();
			for (int i = 0, n = sheet1.getRows(); i < n; i++) {
				Cell[] cells1 = sheet1.getRow(i);
				if (cells1.length > 1) {
					map1.put(cells1[0].getContents(), cells1[1].getContents());
				}
			}
			
			Workbook workbook2 = Workbook.getWorkbook(old);
			Sheet sheet2 = workbook2.getSheet(1);
			HashMap<String, String> map2 = new HashMap<String, String>();
			HashMap<String, String> map22 = new HashMap<String, String>();
			for (int i = 0, n = sheet2.getRows(); i < n; i++) {
				Cell[] cells2 = sheet2.getRow(i);
				if (cells2.length > 1) {
					map2.put(cells2[0].getContents(), cells2[1].getContents());
				}
			}
			FileOutputStream fo = new FileOutputStream(out2);
			for (String key : map1.keySet()) {
				String v = map2.get(key);
				if (v == null) {
					fo.write(key.getBytes());
					fo.write("\t".getBytes());
					fo.write(map1.get(key).getBytes());
					fo.write("\n".getBytes());
				}
			}
			fo.close();
		}
	}
	
	/*
	 * 执行此方法前请先执行MultiLanguageTranslateTool
	 * 把game_init_config/translate/translate.txt和xml.txt分别拷贝到koreaCut.xls中的第一页和第二页
	 * 第二页要拷贝2次具体参照原来格式
	 * 然后执行此main方法生成此版本与之前版本在数据上上的不一致，
	 * 数据分别保存在game_init_config_korea//translate//bak//translateMore和xmlMore中
	 * 
	 * */
	public static void main(String[] args) throws Exception {
		projectPath = System.getProperty("user.dir");
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		String v = year+""+(month > 9 ? ""+month : "0"+month)+(day>9 ? ""+day : "0"+day)+""+(hour>9 ? ""+hour : "0"+hour)+""+(minute>9 ? ""+minute : "0"+minute);
		File old = new File(projectPath + "//conf//game_init_config_korea//translate//korea.xls");
		File current = new File(projectPath + "//conf//game_init_config_korea//translate//koreaCut.xls");
		File outTranslate = new File(projectPath+"//conf//game_init_config_korea//translate//bak//translateMore" + v + ".txt");
		File outXml = new File(projectPath+"//conf//game_init_config_korea//translate//bak//xmlMore" + v + ".txt");
		translateMore(old, current, outTranslate, outXml);
	}

}
