package com.fy.engineserver.playerAims.tool;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;

public class ReadFileTool {
	
	public static String getString(HSSFRow row, int i, Logger logger){
		HSSFCell cell = row.getCell(i);
		if(cell == null){
//			logger.error("单元格是null，页签{} 行{} 列{} ",
//					new Object[]{row.getSheet().getSheetName(), row.getRowNum(), i});
			return "";
		}
		int type = cell.getCellType();
		if(type == Cell.CELL_TYPE_STRING){
			return cell.getStringCellValue();
		}else if(type == Cell.CELL_TYPE_FORMULA){
			int typeF = cell.getCachedFormulaResultType();
			if(typeF == Cell.CELL_TYPE_STRING){
				return cell.getStringCellValue();
			}
		}
		String ret = String.valueOf(cell.getNumericCellValue());
		if(ret.equals("0.0")){
			ret = "";
		}
		return ret;
	}
	/**
	 * 
	 * @param row
	 * @param cellIdx
	 * @param logger
	 * @param defaultRe  默认返回值
	 * @return
	 */
	public static long getLong(HSSFRow row, int cellIdx, Logger logger, long defaultRe) {
		HSSFCell cell = row.getCell(cellIdx);
		if (cell == null) {
//			logger.error("单元格是null " + cellIdx + " 行 " + row.getRowNum() + " at " + row.getSheet().getSheetName());
			return defaultRe;
		}
		int type = cell.getCellType();
		if (type == HSSFCell.CELL_TYPE_NUMERIC || type == HSSFCell.CELL_TYPE_FORMULA) {
			return (long) cell.getNumericCellValue();
		}
		if(cell.getStringCellValue().trim().length()>0){
			return Long.parseLong(cell.getStringCellValue());
		}
		return defaultRe;
	}
	
	public static boolean getBoolean (HSSFRow row, int cellIdx, Logger logger) {
		HSSFCell cell = row.getCell(cellIdx);
		if (cell == null) {
//			logger.error("单元格是null " + cellIdx + " 行 " + row.getRowNum() + " at " + row.getSheet().getSheetName());
			return false;
		}
		return cell.getBooleanCellValue();
	}
	
	public static int getInt(HSSFRow row, int cellIdx, Logger logger) {
		HSSFCell cell = row.getCell(cellIdx);
		if (cell == null) {
//			logger.error("单元格是null " + cellIdx + " 行 " + row.getRowNum() + " at " + row.getSheet().getSheetName());
			return 0;
		}
		int type = cell.getCellType();
		if (type == HSSFCell.CELL_TYPE_NUMERIC) {
			return (int) cell.getNumericCellValue();
		}
		if(cell.getStringCellValue().trim().length()>0){
			return Integer.parseInt(cell.getStringCellValue());
		}
		return 0;
	}
	public static byte getByte(HSSFRow row, int cellIdx, Logger logger) {
		HSSFCell cell = row.getCell(cellIdx);
		if (cell == null) {
//			logger.error("单元格是null " + cellIdx + " 行 " + row.getRowNum() + " at " + row.getSheet().getSheetName());
			return 0;
		}
		int type = cell.getCellType();
		if (type == HSSFCell.CELL_TYPE_NUMERIC) {
			return (byte) cell.getNumericCellValue();
		}
		if(cell.getStringCellValue().trim().length()>0){
			return Byte.parseByte(cell.getStringCellValue());
		}
		return 0;
	}
	
	public static double getDouble(HSSFRow row, int cellIdx, Logger logger) {
		HSSFCell cell = row.getCell(cellIdx);
		if (cell == null) {
			return 0;
		}
		int type = cell.getCellType();
		if (type == HSSFCell.CELL_TYPE_NUMERIC) {
			return cell.getNumericCellValue();
		}
		if(cell.getStringCellValue().trim().length()>0){
			return Double.parseDouble(cell.getStringCellValue());
		}
		return 0;
	}
	
	public static String[] getStringArr(HSSFRow row, int cellIdx, Logger logger, String splitFlag) {
		String tempStr = ReadFileTool.getString(row, cellIdx, logger);
		if(tempStr != null && !tempStr.isEmpty()) {
			String[] arr = tempStr.split(splitFlag);
			return arr;
		} else {
			return new String[0];
		}
	}
	
	public static int[] getIntArrByString(HSSFRow row, int cellIdx, Logger logger, String splitFlag){
		String tempStr = ReadFileTool.getString(row, cellIdx, logger);
		if(tempStr != null && !tempStr.isEmpty()) {
		String[] arr = tempStr.split(splitFlag);
			int[] result = new int[arr.length];
			for(int i=0; i<arr.length; i++) {
				try {
					result[i] = Integer.parseInt(arr[i]);
				} catch (Exception e) {
					result[i] = Integer.parseInt(arr[i].split("\\.")[0]);
				}
			}
			return result;
		} else {
			return new int[0];
		}
	}
	public static byte[] getByteArrByString(HSSFRow row, int cellIdx, Logger logger, String splitFlag){
		String tempStr = ReadFileTool.getString(row, cellIdx, logger);
		if(tempStr != null && !tempStr.isEmpty()) {
			String[] arr = tempStr.split(splitFlag);
			byte[] result = new byte[arr.length];
			for(int i=0; i<arr.length; i++) {
				try {
					result[i] = Byte.parseByte(arr[i]);
				} catch (Exception e) {
					result[i] =Byte.parseByte(arr[i].split("\\.")[0]);
				}
			}
			return result;
		} else {
			return new byte[0];
		}
	}
	public static double[] getDoubleArrByString(HSSFRow row, int cellIdx, Logger logger, String splitFlag){
		String tempStr = ReadFileTool.getString(row, cellIdx, logger);
		if(tempStr != null) {
			String[] arr = tempStr.split(splitFlag);
			double[] result = new double[arr.length];
			for(int i=0; i<arr.length; i++) {
				result[i] = Double.parseDouble((arr[i]));
			}
			return result;
		} else {
			return new double[0];
		}
	}
	/**
	 * 
	 * @param row
	 * @param cellIdx
	 * @param logger
	 * @param splitFlag
	 * @param mul   配表中数值固定倍数
	 * @return
	 */
	public static long[] getLongArrByString(HSSFRow row, int cellIdx, Logger logger, String splitFlag, long mul){
		String tempStr = ReadFileTool.getString(row, cellIdx, logger);
		if(tempStr != null) {
			String[] arr = tempStr.split(splitFlag);
			long[] result = new long[arr.length];
			for(int i=0; i<arr.length; i++) {
				result[i] = Long.parseLong(arr[i]);
				if(mul > 0) {
					result[i] *= mul;
				}
			}
			return result;
		} else {
			return new long[0];
		}
	}
	public static Long[] getLongArrByString(HSSFRow row, int cellIdx, Logger logger, String splitFlag){
		String tempStr = ReadFileTool.getString(row, cellIdx, logger);
		if(tempStr != null) {
			String[] arr = tempStr.split(splitFlag);
			Long[] result = new Long[arr.length];
			for(int i=0; i<arr.length; i++) {
				result[i] = Long.parseLong(arr[i]);
			}
			return result;
		} else {
			return new Long[0];
		}
	}
}
