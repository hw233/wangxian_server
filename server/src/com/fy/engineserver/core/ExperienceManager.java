package com.fy.engineserver.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.util.ServiceStartRecord;

/**
 * 经验值管理
 * 
 */
public class ExperienceManager {

	// 打怪获得经验值
	public static final int ADDEXP_REASON_FIGHTING = 0;
	// 购买经验值
	public static final int ADDEXP_REASON_BUYING = 1;
	// 其他
	public static final int ADDEXP_REASON_TASK = 2;

	public static final int ADDEXP_REASON_BATTLE = 3;

	public static final int ADDEXP_REASON_OTHER = 4;

	public static final int ADDEXP_REASON_FINISH_APPRENTICESHIP = 5;

	public static final int ADDEXP_REASON_QUIZ = 6;

	public static final int ADDEXP_REASON_MONEYEXPHONOR_PROPS = 7;

	public static final int ADDEXP_REASON_FIRE_NPC = 8;

	public static final int ADDEXP_REASON_RETURN_PLAYER = 9;

	public static final int ADDEXP_REASON_UPGRADE_PROPS = 10;

	public static final int ADDEXP_REASON_SINGLE_PROPS = 11;

	public static final int ADDEXP_REASON_PICK_CAVE_PLANT = 12;

	public static final int ADDEXP_REASON_CHUANGONG = 13;
	public static final int ADDEXP_REASON_QINGYUAN = 14;
	public static final int ADDEXP_REASON_FIREACTIVITY = 15;
	public static final int ADDEXP_REASON_EXPLOREACTIVITY = 16;
	public static final int ADDEXP_REASON_QUIZACTIVITY = 17;
	public static final int ADDEXP_REASON_EXCHANGE_FORLUCKFRUIT = 19;
	public static final int ADDEXP_REASON_SILVERCAR = 20;
	public static final int ADDEXP_REASON_TILI = 21;
	public static final int ADDEXP_REASON_PEEKANDBRICK = 22;
	public static final int ADDEXP_REASON_Beer = 23;
	public static final int ADDEXP_REASON_GUOZHAN = 24;
	
	public static final int ADDEXP_REASON_QIANCENGTA = 25;

	public static final int ADDEXP_REASON_PEOPLESEARCH = 26;
	public static final int 采花大盗 = 27;
	public static final int 庄园活动 = 28;
	public static final int 活动 = 29;
	public static final int 观看飞升动画 = 30;
	public static final int 挖宝 = 31;
	public static final int 仙尊投票 = 32;
	public static final int 仙尊膜拜 = 33;
	public static final int 飞升炼丹 = 34;
	public static final int 仙界宝箱 = 35;
	public static final int 宝箱争夺 = 36;
	public static final int 金猴天灾活动 = 37;
	public static final int 骰子仙居胜利者 = 38;
	public static final int 吃一只小羊 = 39;
	public static final int 采集经验草 = 40;
	public static final int 小羊结算经验 = 41;
	public static final int 官员体力活动加成 = 42;

	public static final String EXP_REASON_NAMES[] = new String[] { 
		Translate.text_5772, 			//0
		Translate.text_5773,			//1
		Translate.text_5774, 			//2
		Translate.text_5775, 			//3
		Translate.text_5776,  			//4 
		Translate.text_3486,  			//5 
		Translate.text_5777,  			//6 
		Translate.text_5778,  			//7 
		Translate.text_5779,  			//8 
		Translate.text_5780,  			//9 
		Translate.text_5781,  			//10
		Translate.玩家经验丹,  			//11
		Translate.PICK_CAVE_PLANT,  	//12
		Translate.text_传功经验,  		//13 
		Translate.text_仙缘经验, 		//14
		"喝酒",  						//15
		Translate.text_寻宝经验,  		//16
		Translate.text_答题经验,  		//17
		Translate.text_feed_forluck , 	//19
		Translate.text_feed_silvercar, 	//20
		Translate.text_体力值, 			//21
		Translate.text_peekAndBrick, 	//22
		"论道",  						//23
		"国战",  						//24
		"千层塔", 						//25
		"寻人活动", 						//26
		"采花大盗", 						//27
		"庄园剪刀活动", 						//28
		"活动", 						//29
		"观看飞升动画", 						//30
		"挖宝", 						//31
		"仙尊投票", 						//32
		"仙尊膜拜", 						//33
		"飞升炼丹", 						//34
		"仙界宝箱", 						//35
		"宝箱争夺", 						//36
		"金猴天灾活动",						//37
		"骰子仙居胜利者",				//38
		"吃一只小羊",				    //39
		"采集经验草"	,					//40
		"小羊结算经验",					//41
		"官员体力活动加成"				//42
		};

	static ExperienceManager self;

	public static ExperienceManager getInstance() {
		if (self != null) return self;
		return null;
	}

	public static int AUTO_LEVEL_UP_MAX = 40;
	/**
	 * 最大级别
	 */
	public int maxLevel = 300;

	/**
	 * 每一个级别需要的经验值
	 */
	public static long maxExpOfLevel[] = null;

	File playerExpFile = new File("D:\\mywork\\工作用的资料\\data\\expData.xls");

	public File getPlayerExpFile() {
		return playerExpFile;
	}

	public void setPlayerExpFile(File playerExpFile) {
		this.playerExpFile = playerExpFile;
	}

	public static final int 人物经验表sheet = 0;

	public static final int 人物等级列 = 0;
	public static final int 人物经验列 = 1;

	public void init() throws Exception {
		
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

		if (playerExpFile != null && playerExpFile.isFile() && playerExpFile.exists()) {
			FileInputStream fis = new FileInputStream(playerExpFile);
			loadExpFrom(fis);
		}

		self = this;
		ServiceStartRecord.startLog(this);

		System.out.println("[系统初始化] [人物经验管理器] [初始化完成] [" + getClass().getName() + "] [耗时：" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "毫秒]");
	}

	public void loadExpFrom(InputStream is) throws Exception {
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);

		HSSFSheet sheet = workbook.getSheetAt(人物经验表sheet);
		int rows = sheet.getPhysicalNumberOfRows();
		maxExpOfLevel = new long[maxLevel + 1];

		maxExpOfLevel[0] = 1;
		if (rows <= maxLevel) {
			throw new Exception("行数小于等于" + maxLevel);
		}
		for (int r = 1; r < rows && r <= maxLevel; r++) {
			HSSFRow row = sheet.getRow(r);
			if (row != null) {
				HSSFCell hc = row.getCell(人物经验列);
				long exp = (long) hc.getNumericCellValue();
				maxExpOfLevel[r] = exp;
			}
		}
	}

	/**
	 * 计算根据当前级别，当前的经验值，获得的经验值，
	 * 判断是否要升级
	 * @param currentLevel
	 * @param currentExp
	 * @param additionalExp
	 * @return
	 */
	public boolean isUpgradeLevel(int currentLevel, long currentExp, long additionalExp) {
		if (currentLevel >= maxLevel) return false;
		if (currentExp + additionalExp >= maxExpOfLevel[currentLevel]) {
			return true;
		}
		return false;
	}

	/**
	 * 计算根据当前级别，当前的经验值，获得的经验值，
	 * 判断法宝是否要升级
	 * @param currentLevel
	 * @param currentExp
	 * @param additionalExp
	 * @return
	 */
	// public boolean isMagicWeaponUpgradeLevel(int currentLevel,int currentExp,int additionalExp){
	// if(currentLevel >= magicWeaponMaxLevel) return false;
	// if( currentExp + additionalExp >= maxExpOfMagicWeaponLevel[currentLevel]){
	// return true;
	// }
	// return false;
	// }

	// /**
	// * 计算升级后的级别
	// * @param currentLevel
	// * @param currentExp
	// * @param additionalExp
	// * @return
	// */
	// public int calculateMagicWeaponLevel(int currentLevel,int currentExp,int additionalExp){
	// if(currentLevel >= magicWeaponMaxLevel) return currentLevel;
	// int exp = currentExp + additionalExp;
	// while( exp >= maxExpOfMagicWeaponLevel[currentLevel]){
	// exp -= maxExpOfMagicWeaponLevel[currentLevel];
	// currentLevel++;
	// if(currentLevel >= magicWeaponMaxLevel) break;
	// }
	// return currentLevel;
	// }

	/**
	 * 计算升级后的级别
	 * @param currentLevel
	 * @param currentExp
	 * @param additionalExp
	 * @return
	 */
	public int calculateLevel(int currentLevel, long currentExp, long additionalExp) {
		if (currentLevel >= maxLevel) return currentLevel;
		long exp = currentExp + additionalExp;
		while (exp >= maxExpOfLevel[currentLevel]) {
			exp -= maxExpOfLevel[currentLevel];
			currentLevel++;
			if (currentLevel >= maxLevel) break;
		}
		return currentLevel;
	}

	// /**
	// * 计算升级后剩余的经验值
	// * @param currentLevel
	// * @param currentExp
	// * @param additionalExp
	// * @return
	// */
	// public int calculateMagicWeaponLeftExp(int currentLevel,int currentExp,int additionalExp){
	// if(currentLevel >= magicWeaponMaxLevel) return 0;
	// int exp = currentExp + additionalExp;
	// while( exp >= maxExpOfMagicWeaponLevel[currentLevel]){
	// exp -= maxExpOfMagicWeaponLevel[currentLevel];
	// currentLevel++;
	// if(currentLevel >= magicWeaponMaxLevel) return 0;
	// }
	// return exp;
	// }

	/**
	 * 计算升级后剩余的经验值
	 * @param currentLevel
	 * @param currentExp
	 * @param additionalExp
	 * @return
	 */
	public long calculateLeftExp(int currentLevel, long currentExp, long additionalExp) {
		if (currentLevel >= maxLevel) return 0;
		long exp = currentExp + additionalExp;
		while (exp >= maxExpOfLevel[currentLevel]) {
			exp -= maxExpOfLevel[currentLevel];
			currentLevel++;
			if (currentLevel >= maxLevel) return 0;
		}
		return exp;
	}

	/**
	 * 计算升一级后剩余的经验值
	 * @param currentLevel
	 * @param currentExp
	 * @param additionalExp
	 * @return
	 */
	public long calculateOneLevelLeftExp(int currentLevel, long currentExp) {
		if (currentLevel >= maxLevel) return 0;
		long exp = currentExp;
		if (exp >= maxExpOfLevel[currentLevel]) {
			exp -= maxExpOfLevel[currentLevel];
			if (currentLevel >= maxLevel) return 0;
		}
		return exp;
	}

}
