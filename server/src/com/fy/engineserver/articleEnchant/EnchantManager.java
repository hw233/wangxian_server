package com.fy.engineserver.articleEnchant;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.articleEnchant.model.EnchantModel;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.buff.BuffTemplate_addAttackWithRandom;
import com.fy.engineserver.datasource.buff.Buff_AddAttackWithRandom;
import com.fy.engineserver.datasource.buff.Buff_DecreaseDmgRate;
import com.fy.engineserver.datasource.buff.Buff_HuDun;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;

public class EnchantManager {
	
	public static Logger logger = LoggerFactory.getLogger(EnchantManager.class);
	
	public static EnchantManager instance;
	/** key=附魔id */
	public Map<Integer, EnchantModel> modelMap = new HashMap<Integer, EnchantModel>();
	
	public Map<Integer, String> translate = new HashMap<Integer, String>();
	
	private String fileName;
	//装备类型，0武器,1:头盔,2护肩,3胸,4护腕,5腰带,6靴子,7首饰,8项链,9戒指
	public static int[] 开放附魔的位置 = new int[]{0, 1, 3, 9}; 
	public static byte[] 对应物品类型 = new byte[]{93, 94, 95, 96}; 
	
	public static String[] equipDes = new String[]{"武器","头盔","护肩","胸甲","护腕","腰带","靴子","首饰","项链","戒指"};
	
	public static int 锁定读条时间 = 5000;
	public static int 附魔buff延迟时间 = 500;
	public static long 锁定附魔消耗 = 10000;
	
	public static int 耐久 = 10;		//附魔灵性低于10点法通知
	
	public static RecordAction[] actions = new RecordAction[]{RecordAction.武器附魔次数, RecordAction.头盔附魔次数, null ,RecordAction.胸甲附魔次数
		,null, null, null, null, null, RecordAction.戒指附魔次数};
	public static RecordAction[] actionMax = new RecordAction[]{RecordAction.武器附魔到max, RecordAction.头盔附魔到max, null ,RecordAction.胸甲附魔到max
		,null, null, null, null, null, RecordAction.戒指附魔到max};
	
	public boolean isOpenEnchant(int equipmentType) {
		for (int i=0; i<开放附魔的位置.length; i++) {
			if (开放附魔的位置[i] == equipmentType) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 获取玩家可附魔部位装备信息
	 * @param player
	 * @return
	 */
	public static long[] getPlayerEcLists(Player player) {
		long[] result = new long[开放附魔的位置.length];
		for (int i=0; i<result.length; i++) {
			ArticleEntity ae = player.getEquipmentColumns().get(开放附魔的位置[i]);
			if (ae == null) {
				result[i] = -1;
			} else {
				result[i] = ae.getId();
			}
		}
		return result;
	}
	
	public void init() throws Exception {
		instance = this;
		loadFile();
	}
	/**
	 * 
	 * @param player
	 * @param equiptId
	 * @return  -1代表玩家身上没有此id的装备
	 */
	public static int getEquiptIndex(Player player, long equiptId) {
		for (int i=0; i<player.getEquipmentColumns().getEquipmentIds().length; i++) {
			if (player.getEquipmentColumns().getEquipmentIds()[i] == equiptId) {
				return i;
			}
		}
		return -1;
	}
	
	public static void fireBuff(Player player, String buffName, int buffLevel, String des) {
		fireBuff(player, buffName, buffLevel, des, 1);
	}
	
	public static void fireBuff(Player player, String buffName, int buffLevel, String des, int time) {
		try {
			BuffTemplateManager btm = BuffTemplateManager.getInstance();
			BuffTemplate bt = btm.getBuffTemplateByName(buffName);
			Buff buff = bt.createBuff(buffLevel);
			buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + time);
			buff.setCauser(player);
			if (des != null && !des.isEmpty()) {
				buff.setDescription(des);
			}
			player.placeBuff(buff);
		} catch (Exception e) {
			logger.warn("[fireBuff] [异常] [" + player.getLogString4Knap() + "] [" + buffName + "] [" + buffLevel + "]", e);
		}
	}
	
	/**
	 * 被动触发buff
	 * @param player
	 * @param model
	 */
	public static void fireBuff(Player player, EnchantModel model, int ranNum) {
		try { 
			String buffName = model.getBuffName();
			int level = model.getBuffLevel();
			long lastTime = model.getLastTime();
			BuffTemplateManager btm = BuffTemplateManager.getInstance();
			BuffTemplate bt = btm.getBuffTemplateByName(buffName);
			Buff buff = bt.createBuff(level);
			buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + lastTime);
			buff.setCauser(player);
			if (buff instanceof Buff_AddAttackWithRandom) {
				BuffTemplate_addAttackWithRandom tempLate = (BuffTemplate_addAttackWithRandom) buff.getTemplate();
				if (tempLate.getMaxMagicAttackYs()[buff.getLevel()] > 0) {
					((Buff_AddAttackWithRandom)buff).setMagicAttackY(ranNum);
				}
				if (tempLate.getMaxPhyAttackYs()[buff.getLevel()] > 0) {
					((Buff_AddAttackWithRandom)buff).setPhyAttackY(ranNum);
				}
			} else if (buff instanceof Buff_HuDun) {
				((Buff_HuDun)buff).setDamage(ranNum);
				buff.setDescription(Translate.text_977+ranNum+Translate.text_3201);
			} else if (buff instanceof Buff_DecreaseDmgRate) {
				((Buff_DecreaseDmgRate)buff).setDecreaseRate(ranNum);
				float dd = ranNum / 10f;
				buff.setDescription(String.format(Translate.减少百分比受到伤害, dd + ""));
			}
			player.placeBuff(buff);
			if (logger.isInfoEnabled()) {
				logger.info("[fireBuff] [sucess] [ " + player.getLogString() + "] [" + buffName + "] [" + level + "] [" + lastTime + "] [ranNum:" + ranNum + "]");
			}
		} catch (Exception e) {
			logger.warn("[fireBuff] [异常] [" + player.getLogString() + "] [" + model.getBuffName() + "] [" + model.getBuffLevel() + "]", e);
		}
		
	}
	
	
	public void loadFile() throws Exception {
		
		File f = new File(getFileName());
		if(!f.exists()){
			throw new Exception(getFileName() + "配表不存在! " + f.getAbsolutePath());
		}
		
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		
		HSSFSheet sheet = workbook.getSheetAt(0);			
		int rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				EnchantModel model = getEnchantModel(row);
				if (modelMap.containsKey(model.getId())) {
					throw new Exception(fileName + " 出现重复附魔id," + model.getId());
				}
				modelMap.put(model.getId(), model);
			}catch(Exception e) {
				throw new Exception(fileName + "第" + (i+1) + "行异常！！", e);
			}
		}
		sheet = workbook.getSheetAt(1);			//传送门刷出规则
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				translate.put(ReadFileTool.getInt(row, 0, logger), ReadFileTool.getString(row, 1, logger));
			}catch(Exception e) {
				throw new Exception(fileName + "第" + (i+1) + "行异常！！", e);
			}
		}
		ServiceStartRecord.startLog(this);
	}
	
	private EnchantModel getEnchantModel(HSSFRow row) throws Exception {
		int index = 0;
		EnchantModel model = new EnchantModel();
		model.setId(ReadFileTool.getInt(row, index++, logger));
		model.setEnchatName(ReadFileTool.getString(row, index++, logger));
		model.setEquiptLevelLimit(ReadFileTool.getInt(row, index++, logger));
		model.setDurable(ReadFileTool.getInt(row, index++, logger));
		model.setType(ReadFileTool.getByte(row, index++, logger));
		model.setPassiveType(ReadFileTool.getByte(row, index++, logger));
		model.setProbType(ReadFileTool.getInt(row, index++, logger));
		model.setProbabbly(ReadFileTool.getInt(row, index++, logger));
		model.setEquiptmentType(ReadFileTool.getInt(row, index++, logger));
		model.setAddAttrTypes(ReadFileTool.getIntArrByString(row, index++, logger, ","));
		model.setFirstProb(ReadFileTool.getIntArrByString(row, index++, logger, ","));
		model.setAttrNums(ReadFileTool.getIntArrByString(row, index++, logger, ","));
		model.setBuffName(ReadFileTool.getString(row, index++, logger));
		model.setBuffLevel(ReadFileTool.getInt(row, index++, logger));
		model.setLastTime(ReadFileTool.getLong(row, index++, logger, 0));
		model.setDes(ReadFileTool.getString(row, index++, logger));
		model.setCd(ReadFileTool.getLong(row, index++, logger, 0));
		model.setColorType(ReadFileTool.getInt(row, index++, logger));
		if (model.getFirstProb().length > 0 && (model.getFirstProb().length+1) != model.getAttrNums().length) {
			throw new Exception("[档位随机概率与数值数量不匹配] [附魔id:" + model.getId() + "]");
		}
		return model;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
