package com.fy.engineserver.datasource.language;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.fy.engineserver.util.ServiceStartRecord;


public class TransferLanguage {

	private static HashMap<String,String> map = null;
	
	/**
	 * 字符转换标记，true为转换，false为不转换
	 */
	public static boolean characterTransferormFlag = false;
	
	public static File characterExcel = new File("D://mywork//工作用的资料//characterExcel.xls");

	public boolean isCharacterTransferormFlag() {
		return characterTransferormFlag;
	}

	public void setCharacterTransferormFlag(boolean characterTransferormFlag) {
		TransferLanguage.characterTransferormFlag = characterTransferormFlag;
	}

	public File getCharacterExcel() {
		return characterExcel;
	}

	public void setCharacterExcel(File characterExcel) {
		TransferLanguage.characterExcel = characterExcel;
	}

	public void init(){
		
		long time = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if(characterTransferormFlag){
			if(characterExcel != null && characterExcel.exists() && characterExcel.isFile()){
				if(TransferLanguage.map == null || TransferLanguage.map.isEmpty()){
					TransferLanguage.map = new HashMap<String,String>();
					if(TransferLanguage.characterExcel != null && TransferLanguage.characterExcel.exists() && TransferLanguage.characterExcel.isFile()){

						try {
							POIFSFileSystem pss = new POIFSFileSystem(new FileInputStream(TransferLanguage.characterExcel));
							HSSFWorkbook workbook = new HSSFWorkbook(pss);
				            HSSFSheet sheet = workbook.getSheetAt(0);
				            int rows = sheet.getPhysicalNumberOfRows();
							int line = 0;
				            for (int r = 0; r < rows; r++) {
				            	try {
					                HSSFRow row = sheet.getRow(r);
					                if (row != null) {
					                	HSSFCell cell0 = row.getCell((short)0);
										HSSFCell cell1 = row.getCell((short)1);
										map.put(cell0.getStringCellValue(), cell1.getStringCellValue());
					                }
				            	} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
				            }

						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					
					}
				}
			}
		}
		System.out.println("["+this.getClass().getName()+"] ["+(map != null?map.size():"map为空")+"] [设置成功耗时:"+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - time)+"毫秒]");
		ServiceStartRecord.startLog(this);
	}
	
	public static HashMap<String,String> getMap(){
		return map;
	}

	/**
	 * 将传进的参数转换成需要的字符串
	 * @param str
	 * @return
	 */
	public static String transferString(String str){
		String transferStr = str;
		if(TransferLanguage.characterTransferormFlag && str != null && str.trim().length() != 0 && map != null && !map.isEmpty() && map.containsKey(str)){
			transferStr = map.get(str);
		}
		return transferStr;
	}
	
	public static void main(String[] args){
		TransferLanguage.getMap();
	}
}
