package com.fy.engineserver.sprite.pet2;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;

import com.fy.engineserver.sprite.pet.PetCellInfo;
import com.fy.engineserver.sprite.pet.PetEatProp2Rule;
import com.fy.engineserver.sprite.pet.PetEatPropRule;
import com.fy.engineserver.sprite.pet.PetManager;

/**
 * 宠物进阶数据配置。
 * 
 * create on 2013年8月15日
 */
public class PetGrade {
	public static PetGrade[] levels;
	public static int[] scaleArr;
	/**
	 * 可进阶的宠物列表
	 */
	public static GradePet[] petList;
	public static TakePetSkillConf takePetSkillConf[];
	public static TalentHoleConf talentHoleConf[];
	public static Map<String, String> translation = new LinkedHashMap<String, String>();
	/**
	 * key -> 携带等级。
	 */
	public static Map<Integer, LianHunConf> lianHunMap = new LinkedHashMap<Integer, LianHunConf>();
	//
	public int grade;
	/**
	 * 宠物最低等级
	 */
	public int lvMin;
	public String itemName;
	public String itemChinese;
	public int itemCnt;
	//
	public int scale;
	//
	public int liLiang;
	public int shenFa;
	public int linLi;
	public int naiLi;
	public int dingLi;
	//
	public int point;
	//
	/**
	 * 五项属性加成
	 */
	public int [] arrArr;
	public PetGrade(){
		arrArr = new int[]{1,2,3,4,5};
	}
	public static PetGrade[] load(String filePath) throws Exception{
		Logger log = Pet2Manager.log;
		if(filePath == null){
			log.error("file path is null");
			return new PetGrade[0];
		}
		File f = new File(filePath);
		if(f.exists()==false || f.isFile()==false){
			String msg = "configure file doesn't exist or is not file :"+f;
			log.error(msg);
			return new PetGrade[0];
		}
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		workbook.setMissingCellPolicy(HSSFRow.CREATE_NULL_AS_BLANK);
		
		HSSFSheet sheet = workbook.getSheetAt(0);
		petList = loadPetList(workbook.getSheetAt(1), log);
		loadTakePetSkillConf(workbook.getSheetAt(2), log);
//		checkTakeBookConf();
		loadTalentHoleConf(workbook.getSheetAt(3), log);
		loadLianHunConf(workbook.getSheetAt(4), log);
		loadTranslation(workbook.getSheetAt(5), log);
		loadPetEatProp(workbook.getSheetAt(6), log);
		loadPetEatProp2(workbook.getSheetAt(7), log);
		loadPetCellInfo(workbook.getSheetAt(8), log);
		makeLockSkillTicketNames();
		PetGrade[] arr = null;
		int rows = sheet.getPhysicalNumberOfRows();
		for (int r = 1; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			PetGrade pg = new PetGrade();
			pg.lvMin = getInt(row, 0, log);
			pg.grade = getInt(row, 1, log);
			pg.itemName = getString(row, 2, log);
			pg.itemChinese = getString(row, 3, log);
			pg.itemCnt = getInt(row, 4, log);
			pg.scale = getInt(row, 5, log);
			//
			pg.liLiang = getInt(row, 6, log);	pg.arrArr[0] = pg.liLiang;
			pg.shenFa = getInt(row, 7, log);	pg.arrArr[1] = pg.shenFa;
			pg.linLi = getInt(row, 8, log);	pg.arrArr[2] = pg.linLi;
			pg.naiLi = getInt(row, 9, log);	pg.arrArr[3] = pg.naiLi;
			pg.dingLi = getInt(row, 10, log);	pg.arrArr[4] = pg.dingLi;
			pg.point = getInt(row, 11, log);	
			//
			arr = (PetGrade[]) ArrayUtils.add(arr, pg);
		}
		int len = arr.length;
		int[] scaleArr0 = new int[len];
		for(int i=0; i<len; i++){
			scaleArr0[i] = arr[i].scale;
		}
		scaleArr = scaleArr0;
		log.info("载入完毕");
		//
		is.close();
		return arr;
	}
	
	public static void loadLianHunConf(HSSFSheet sheet, Logger log) {
		int rows = sheet.getPhysicalNumberOfRows();
		for (int r = 1; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);	
			if(row == null){
				break;
			}
			LianHunConf c = new LianHunConf();
			c.takeLevel = getInt(row, 0, log);
			c.dropExp = getInt(row, 1, log);
			c.toNextLvExp = getInt(row, 2, log);
			c.articleName = getString(row, 3, log);
			c.progArtName = getString(row, 4, log);
			c.progLevel = getInt(row, 5, log);
			if(c.progLevel == 0){
				c.progLevel = c.takeLevel;
			}
			lianHunMap.put(c.takeLevel, c);
		}
		log.info("炼魂配置个数 {}", lianHunMap.size());
	}

	public static void makeLockSkillTicketNames() {
		Logger log = Pet2Manager.log;
		String c = Pet2SkillCalc.lockSk1Key;
		c = translation.get(c);
		if(c == null){
			log.error("缺失技能绑定符一 {}", c);
		}else{
			Pet2SkillCalc.lockSk1Key = c;
			log.info("技能绑定符一 {} ",c);
			Pet2SkillCalc.lockSkillNames[0] = c;
		}
		//
		c = Pet2SkillCalc.lockSk2Key;
		c = translation.get(c);
		if(c == null){
			log.error("缺失技能绑定符二 {}", c);
		}else{
			Pet2SkillCalc.lockSk2Key = c;
			log.info("技能绑定符二 {} ",c);
			Pet2SkillCalc.lockSkillNames[1] = c;
		}
		//
	}

	public static void loadTalentHoleConf(HSSFSheet sheet, Logger log) {
		int rows = sheet.getPhysicalNumberOfRows();
		talentHoleConf = new TalentHoleConf[10];//注意，10个孔硬编码了。
		int[] arrInt = new int[10];
		for (int r = 1; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);		
			TalentHoleConf thc = new TalentHoleConf();
			thc.grid = r;
			thc.maxTimesThenHit = getInt(row, 1, log);
			thc.rate = getInt(row, 1, log);
			talentHoleConf[r-1] = thc;
			arrInt[r - 1] = thc.maxTimesThenHit;
		}
		Pet2SkillCalc.openSlotNeedBooks = arrInt;
	}

	/**
	 * 这个方法没用了。
	 */
	public static void checkTakeBookConf() {
		Logger log = Pet2Manager.log;
		String[] names = Pet2SkillCalc.tianFuNames;
		if(names == null){
			log.warn("all book names is null");
			return;
		}
		for(String name : names){
			boolean hit = false;
			for(TakePetSkillConf conf : takePetSkillConf){
				if(name.equals(conf.name)){
					hit = true;
					break;
				}
			}
			if(!hit){
				log.error("take skill book article without conf {}", name);
			}
		}
	}

	public static void loadTakePetSkillConf(HSSFSheet sheet, Logger log) {
		int rows = sheet.getPhysicalNumberOfRows();
		for (int r = 1; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			TakePetSkillConf c = new TakePetSkillConf();
			c.name = getString(row, 0, log);
			c.progName = getString(row, 1, log);
			c.succRate = getInt(row, 2, log);
			c.maxLvAllow = getInt(row, 3, log);
			takePetSkillConf = (TakePetSkillConf[]) ArrayUtils.add(takePetSkillConf, c);
		}
		String[] arr = new String[takePetSkillConf.length];
		for(int i=0; i<arr.length; i++){
			arr[i] = takePetSkillConf[i].name;
		}
		Pet2SkillCalc.allTakeSkillBookNames = arr;
	}

	private static void loadTranslation(HSSFSheet sheet, Logger logger) {
		int rows = sheet.getPhysicalNumberOfRows();
		for (int r = 0; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			HSSFCell cell = row.getCell(0);
			if(cell == null){
				logger.warn("null cell at row {} col 0", row.getRowNum());
				break;
			}
			String key = cell.getStringCellValue();
			HSSFCell cell1 = row.getCell(1);
			if(cell1 == null){
				logger.warn("null cell at row {} col 1", row.getRowNum());
				break;
			}
			String v = getString(row, 1, logger);
			if(key != null){
				key = key.trim();
			}
			translation.put(key, v);
		}		
	}

	private static GradePet[] loadPetList(HSSFSheet sheet, Logger log) {
		int rows = sheet.getPhysicalNumberOfRows();
		GradePet[] arr = null;
		for (int r = 1; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			GradePet gp = new GradePet();
			gp.name = "waitFill";
			gp.progName = getString(row, 0, log);
			gp.maxGrade = getInt(row, 1, log);
			gp.lv4Avatar = getString(row, 2, log);
			gp.lv7Avatar = getString(row, 3, log);
			gp.jiChuJiNengDesc = getString(row, 9, log);
			gp.tianFuJiNengDesc = getString(row, 10, log);
			gp.gainFrom = getString(row, 11, log);
			gp.lv4Icon = getString(row, 27, log);
			gp.lv7Icon = getString(row, 28, log);
			gp.flyType = getInt(row, 29, log);
			gp.flyAvata = getString(row, 30, log);
			gp.flyIcon = getString(row, 31, log);
			gp.bornSkill = new int[]{0, 0};
			gp.icons = new String[]{"", ""};//
			gp.skDesc = new String[]{"", ""};//
			loadParticle(gp, row, 12, log);
			loadScale(gp, row, 19, log);
			arr = (GradePet[]) ArrayUtils.add(arr, gp);
		}
		return arr;
	}
	
	private static void loadPetEatProp(HSSFSheet sheet, Logger log) {
		int rows = sheet.getPhysicalNumberOfRows();
		for (int r = 1; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			PetEatPropRule gp = new PetEatPropRule();
			gp.setEatTime(getInt(row, 0, log));
			gp.setNeedEatNums(getInt(row, 1, log));
			gp.setCostData(getInt(row, 2, log));
			gp.setDelCDMoney(getInt(row, 3, log));
			PetManager.eatRules.add(gp);
		}
	}
	
	private static void loadPetEatProp2(HSSFSheet sheet, Logger log) throws Exception {
		int rows = sheet.getPhysicalNumberOfRows();
		for (int r = 1; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			PetEatProp2Rule gp = new PetEatProp2Rule();
			gp.setPropName(getString(row, 0, log));
			gp.setBasicAddPoint(getInt(row, 1, log));
			gp.setBaoJiAddPoints(stringToInt(getString(row, 2, log).split(",")));
			gp.setBaoJiNums(stringToInt(getString(row, 3, log).split(",")));
			PetManager.eat2Rules.add(gp);
		}
	}
	
	private static void loadPetCellInfo(HSSFSheet sheet, Logger log) throws Exception {
		int rows = sheet.getPhysicalNumberOfRows();
		List<PetCellInfo> list = new ArrayList<PetCellInfo>();
		for (int r = 1; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			PetCellInfo info = new PetCellInfo();
			info.setId(getInt(row, 0, log));
			info.setName(getString(row, 1, log));
			info.setLevel(getInt(row, 2, log));
			info.setAddProp(getInt(row, 3, log));
			info.setMaterialNum(getInt(row, 4, log));
			info.setExp(getLong(row, 5, log));
			info.setShowInfo(getString(row, 6, log));
			list.add(info);
			log.warn("[服务器加载助战信息] "+info+"");
		}
		PetManager.getInstance().setCellInfos(list);
	}
	
	public static int[] stringToInt(String[] a) throws Exception{
		int [] fs = new int[a.length];
		for(int k=0;k<a.length;k++){
			if(a[k]!=null && a[k].trim().length()>0){
				String newValue = a[k];
				if(newValue.contains(".0")){
					newValue = newValue.replace(".0", "");
				}
				fs[k] = Integer.parseInt(newValue);
			}
		}
		return fs;
	}

	public static void loadParticle(GradePet gp, HSSFRow row, int i, Logger log) {
		gp.partBody[5] = gp.partBody[6] = gp.partBody[7] = getString(row, i, log);
		gp.partBodyY[5] = gp.partBodyY[6] = gp.partBodyY[7] = getInt(row, i+1, log);
		
		gp.partFoot[8] = getString(row, i+2, log);
		gp.partFootY[8] = getInt(row, i+3, log);
	}
	public static void loadScale(GradePet gp, HSSFRow row, int baseC, Logger log) {
		int len  = 8;
		for(int i=1; i<len ;i++){
			int v = getInt(row, baseC + i, log);
			if(v != 0){
				gp.scaleArr[i-1] = v;
			}
		}
	}
	public static String getString(HSSFRow row, int i, Logger logger){
		HSSFCell cell = row.getCell(i);
		if(cell == null){
			logger.error("单元格是null，页签{} 行{} 列{} ",
					new Object[]{row.getSheet().getSheetName(), row.getRowNum(), i});
			return "";
		}
		int type = cell.getCellType();
		if(type == Cell.CELL_TYPE_STRING){
			return cell.getStringCellValue().trim();
		}else if(type == Cell.CELL_TYPE_FORMULA){
			int typeF = cell.getCachedFormulaResultType();
			if(typeF == Cell.CELL_TYPE_STRING){
				return cell.getStringCellValue().trim();
			}
		}
		String ret = String.valueOf(cell.getNumericCellValue());
		if(ret.equals("0.0")){
			ret = "";
		}
		return ret;
	}
	public static int getInt(HSSFRow row, int i, Logger logger){
		HSSFCell cell = row.getCell(i);
		if(cell == null){
			logger.error("单元格是null，页签{} 行{} 列{} ",
					new Object[]{row.getSheet().getSheetName(), row.getRowNum(), i});
			return 0;
		}
		int type = cell.getCellType();
		if(type == Cell.CELL_TYPE_NUMERIC){
			return (int) cell.getNumericCellValue();
		}
		String str = cell.getStringCellValue();
		if(str == null || "".equals(str.trim())){
			logger.error("单元格是空，页签{} 行{} 列{} ",
					new Object[]{row.getSheet().getSheetName(), row.getRowNum(), i});
			return 0;
		}
		int ret = 0;
		try{
			ret = Integer.parseInt(str);
		}catch(NumberFormatException e){
			logger.error("单元格是不能解析为数字，页签{} 行{} 列{} ",
					new Object[]{row.getSheet().getSheetName(), row.getRowNum(), i});
			throw e;
		}
		return ret;
	}
	
	public static long getLong(HSSFRow row, int i, Logger logger){
		HSSFCell cell = row.getCell(i);
		if(cell == null){
			logger.error("单元格是null，页签{} 行{} 列{} ",
					new Object[]{row.getSheet().getSheetName(), row.getRowNum(), i});
			return 0;
		}
		int type = cell.getCellType();
		if(type == Cell.CELL_TYPE_NUMERIC){
			return (int) cell.getNumericCellValue();
		}
		String str = cell.getStringCellValue();
		if(str == null || "".equals(str.trim())){
			logger.error("单元格是空，页签{} 行{} 列{} ",
					new Object[]{row.getSheet().getSheetName(), row.getRowNum(), i});
			return 0;
		}
		long ret = 0;
		try{
			ret = Long.parseLong(str);
		}catch(NumberFormatException e){
			logger.error("单元格是不能解析为数字，页签{} 行{} 列{} ",
					new Object[]{row.getSheet().getSheetName(), row.getRowNum(), i});
			throw e;
		}
		return ret;
	}
	
	
}
