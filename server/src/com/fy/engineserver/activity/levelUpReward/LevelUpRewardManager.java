package com.fy.engineserver.activity.levelUpReward;

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

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.AllActivityManager;
import com.fy.engineserver.activity.levelUpReward.instance.LevelUpRewardEntity;
import com.fy.engineserver.activity.levelUpReward.instance.RewardInfo;
import com.fy.engineserver.activity.levelUpReward.model.LevelUpRewardModel;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.SavingFailedException;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

public class LevelUpRewardManager {
	
	public static Logger logger = ActivitySubSystem.logger;
	
	public static LevelUpRewardManager instance;
	/** 配置文件路径 */
	private String fileName;
	/** 冲级返利配置  key=id **/
	public Map<Integer, LevelUpRewardModel> rewardMaps = new HashMap<Integer, LevelUpRewardModel>();
	/** 充值解析时需要根据此前缀判断购买的是否为冲级返利，  使用_分隔，[1]为返利类型 */
	public static final String 冲级返利前缀 = "levelUpReward";
	
	public void init() throws Exception {
		instance = this;
		this.initFile();
	}
	
	public boolean isActivityAct() {
		CompoundReturn cr = AllActivityManager.instance.notifySthHappened(AllActivityManager.levelUpRewardActivity);
		if (cr != null) {
			return cr.getBooleanValue();
		}
		return false;
	}
	/**
	 * 购买冲级返利
	 * @param player
	 */
	public String buyLevelUpReward(Player player, String type) {
//		if (!this.isActivityAct()) {
//			return;
//		}
		try {
			int ids = Integer.parseInt(type);
			LevelUpRewardModel model = rewardMaps.get(ids);
			LevelUpRewardModel model2 = getModelByPlayer(player);
			if (model == null) {
				if (logger.isWarnEnabled()) {
					logger.warn("[购买冲级返利] [异常] [" + player.getLogString() + "] [type:" + type + "]");
				}
				player.sendError("没有对应冲级返利商品");
				return "没有对应冲级返利商品,type:" + type;
			}
			if (model2 == null || model.getType() != model2.getType()) {
				if (logger.isWarnEnabled()) {
					logger.warn("[购买冲级返利] [异常] [购买商品与登记段不匹配] [" + player.getLogString() + "] [type:" + type + "]");
				}
				return "错误,type:" + type;
			}
			String result = LevelUpRewardEntityManager.instance.buyLevelUpReward(player, model.getType());
			return result;
		} catch (Exception e) {
			logger.warn("[购买冲级返利] [异常] [" + player.getLogString() + "] [type:" + type + "]", e);
		}
		return "异常,type:" + type;
	}
	/**
	 * 验证玩家是否可以购买冲级返利商品
	 * @param player
	 * @return
	 */
	public String verlificata4Buy(Player player) {
		if (!this.isActivityAct()) {
			return "活动未开启";
		}
		LevelUpRewardModel model = getModelByPlayer(player);
		if (model == null) {
			return "没有对应冲级返利商品";
		}
		LevelUpRewardEntity entity = LevelUpRewardEntityManager.instance.getEntity(player.getId());
		if (entity.getRewardInfo() != null && entity.getRewardInfo().size() > 0) {
			for (RewardInfo info : entity.getRewardInfo()) {
				if (info.getType() == model.getType()) {
					return "已经购买过当前等级对应冲级奖励商品";
				}
			}
		}
		return null;
	}
	/**
	 * 自动发放冲级返利（玩家升级时调用）
	 * @param player
	 */
	public void autoReceiveReward(Player player) {
		LevelUpRewardEntity entity = LevelUpRewardEntityManager.instance.getEntity(player.getId());
		if (entity.getRewardInfo() == null || entity.getRewardInfo().size() <= 0) {
			return ;
		}
		boolean save = false;
		try {
			for (int i=0; i<entity.getRewardInfo().size(); i++) {
				RewardInfo info = entity.getRewardInfo().get(i);
				LevelUpRewardModel lum = rewardMaps.get(info.getType());
				if (lum == null) {
					if (logger.isWarnEnabled()) {
						logger.warn("[autoReceiveReward] [异常] [" + player.getLogString() + "] [lum:" + lum + "] [type:" + entity.getRewardInfo().get(i) + "]");
					}
					continue;
				}
				if (info.getReceiveTimes() >= lum.getMaxRewardTimes()) {		//已经全部领取完
					continue ;
				}
				for (int j=0; j<lum.getRewardLevels().size(); j++) {
					if (info.getReceiveTimes() >= (j+1)) {		//已经领取过奖励
						continue;
					}
					if (player.getLevel() < lum.getRewardLevels().get(j)) {		//领取等级为顺序排列
						break;
					}
					{		//设置领取次数
						info.setReceiveTimes(info.getReceiveTimes() + 1);
						save = true;
					}
					{			//发送返利奖励给玩家
						long savingAmount = lum.getRewardSilvers().get(j);
						try {
							BillingCenter.getInstance().playerSaving(player, savingAmount, CurrencyType.GONGZI, SavingReasonType.冲级返利活动, "冲级返利活动");
							if (logger.isWarnEnabled()) {
								logger.warn("[领取冲级返利] [成功] [" + player.getLogString() + "] [type:" + info.getType() + "] [领取次数:" + info.getReceiveTimes() + "] [领取到的银子:" + savingAmount + "]");
							}
							player.sendError(String.format(Translate.恭喜您获得冲级返利, BillingCenter.得到带单位的银两(savingAmount)));
						} catch (SavingFailedException e) {
							// TODO Auto-generated catch block
							logger.warn("[领取冲级返利] [异常] [" + player.getLogString() + "] [savingAmount:" + savingAmount + "]", e);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.warn("["+this.getClass().getSimpleName()+"] [autoReceiveReward] [异常] [" + player.getLogString() + "]", e);
		} finally {
			if (save) {
				LevelUpRewardEntityManager.em.notifyFieldChange(entity, "rewardInfo");
			}
		}
	}
	/**
	 * 领取冲级返利(防止自动发放失败预留)
	 * @param player
	 * @param rewardId
	 */
	public void receiveReward(Player player, int rewardId) {
		
	}
	/***
	 * 根据玩家level获取对应的冲级返利model
	 * @param player
	 * @return	如果等级没有对应的冲级返利则返回null、
	 */
	public LevelUpRewardModel getModelByPlayer(Player player) {
		for (LevelUpRewardModel rm : rewardMaps.values()) {
			if (rm.getLowLevelLimit() <= player.getLevel() && rm.getHighLevelLimit() >= player.getLevel()) {
				return rm;
			}
		}
		return null;
	}
	
	public void initFile() throws Exception{
		File f = new File(fileName);
		if (!f.exists()) {
			throw new Exception(fileName + " 配表不存在! " + f.getAbsolutePath());
		}
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(0); 			//基础配置
		int rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			try {
				int index = 0;
				String startTime = ReadFileTool.getString(row, index++, logger);
				String endTime = ReadFileTool.getString(row, index++, logger);
				String platForms = ReadFileTool.getString(row, index++, logger);
				String openServerNames = ReadFileTool.getString(row, index++, logger);
				String notOpenServers = ReadFileTool.getString(row, index++, logger);
				LevelUpRewardActivity activity = new LevelUpRewardActivity(startTime, endTime, platForms, openServerNames, notOpenServers);
				AllActivityManager.instance.add2AllActMap(AllActivityManager.levelUpRewardActivity, activity);
			} catch (Exception e) {
				throw new Exception("[" + fileName + "] [" + sheet.getSheetName() + "] [" + "第" + (i + 1) + "行异常！！]", e);
			}
		}
		
		sheet = workbook.getSheetAt(1); 			
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			try {
				int index = 0;
				LevelUpRewardModel lrm = new LevelUpRewardModel();
				lrm.setType(ReadFileTool.getInt(row, index++, logger));
				lrm.setName(ReadFileTool.getString(row, index++, logger));
				lrm.setLowLevelLimit(ReadFileTool.getInt(row, index++, logger));
				lrm.setHighLevelLimit(ReadFileTool.getInt(row, index++, logger));
				lrm.setMaxRewardTimes(ReadFileTool.getInt(row, index++, logger));
				int[] levels = ReadFileTool.getIntArrByString(row, index++, logger, ",");
				long[] silvers = ReadFileTool.getLongArrByString(row, index++, logger, ",", 1000);
				if (levels.length != silvers.length || levels.length != lrm.getMaxRewardTimes()) {
					throw new Exception("[返利等级与返利银子配置错误]");
				}
				for (int j=0; j<lrm.getMaxRewardTimes(); j++) {
					lrm.getRewardLevels().add(levels[j]);
					lrm.getRewardSilvers().add(silvers[j]);
				}
				lrm.setDescription(ReadFileTool.getString(row, index++, logger));
				lrm.setNeedRmb(ReadFileTool.getInt(row, index++, logger));
				if (lrm.getNeedRmb() <= 0) {
					throw new Exception("[冲级红利活动配置错误] [购买所需充值金额：" + lrm.getNeedRmb() + "]");
				}
				rewardMaps.put(lrm.getType(), lrm);
			} catch (Exception e) {
				throw new Exception("[" + fileName + "] [" + sheet.getSheetName() + "] [" + "第" + (i + 1) + "行异常！！]", e);
			}
		}
	}
	/**
	 * 关闭服务器
	 */
	public void destroy() {
		
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
