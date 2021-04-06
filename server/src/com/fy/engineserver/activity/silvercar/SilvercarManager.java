package com.fy.engineserver.activity.silvercar;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.CheckAttribute;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.TimeTool;

/**
 * 押镖活动配置管理器
 * 
 */
@CheckAttribute("押镖活动配置")
public class SilvercarManager {

	public static Logger logger = LoggerFactory.getLogger(TaskSubSystem.class);

	@CheckAttribute("文件路径")
	private String filePath;

	@CheckAttribute(value = "任务配置", des = "<任务名,配置>")
	private HashMap<String, SilvercarTaskCfg> taskCfgMap = new HashMap<String, SilvercarTaskCfg>();

	@CheckAttribute(value = "掉落配置", des = "<NPC模板ID,掉率>")
	private HashMap<Integer, List<SilvercarDropCfg>> dropMap = new HashMap<Integer, List<SilvercarDropCfg>>();
	@CheckAttribute(value = "加货刷新规则")
	private double[] refreshRate;
	@CheckAttribute(value = "家族运镖任务名字")
	private String masterTask;
	@CheckAttribute(value = "家族运镖任务扣钱")
	private int masterTaskMoney;
	@CheckAttribute(value = "个人镖车ID")
	private int selfCarId;
	@CheckAttribute(value = "家族镖车ID")
	private int jiazuCarId;
	@CheckAttribute(value = "个人运镖任务组名")
	private String selfCarTaskGroupName;
	@CheckAttribute(value = "家族运镖任务组名")
	private String jiazuCarTaskGroupName;
	@CheckAttribute(value = "国运经验加成")
	private double countryTrafficRate;
	@CheckAttribute(value = "镖车求救间隔")
	private long carCallForhelpDistance;
	@CheckAttribute(value = "角色救车的时间间隔")
	private long playerHelpSilvercarDistance = 30 * TimeTool.TimeDistance.SECOND.getTimeMillis();
	@CheckAttribute(value = "角色救车的时间间隔")
	private double noticeHPLimit = 0.9;
	@CheckAttribute(value = "各种颜色车大小")
	private double[] carSize;
	@CheckAttribute(value = "家族运镖奖励物品")
	private String jiazuSilvercarOtherPrize;;
	@CheckAttribute(value = "家族运镖奖励物品颜色几率")
	private double[] jiazuSilvercarOtherPrizeRate;
	@CheckAttribute(value = "破碎镖车")
	private double[] jiazuSilvercarOtherPrizeRate4posui;
	@CheckAttribute(value = "白色镖车")
	private double[] jiazuSilvercarOtherPrizeRate4bai;
	@CheckAttribute(value = "绿色镖车")
	private double[] jiazuSilvercarOtherPrizeRate4lv;
	@CheckAttribute(value = "蓝色镖车")
	private double[] jiazuSilvercarOtherPrizeRate4lan;
	@CheckAttribute(value = "紫色镖车")
	private double[] jiazuSilvercarOtherPrizeRate4zi;
	@CheckAttribute(value = "橙色镖车")
	private double[] jiazuSilvercarOtherPrizeRate4cheng;

	private static SilvercarManager instance;

	private SilvercarManager() {

	}

	public static SilvercarManager getInstance() {
		return instance;
	}

	public static Logger getLogger() {
		return logger;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public HashMap<String, SilvercarTaskCfg> getTaskCfgMap() {
		return taskCfgMap;
	}

	public HashMap<Integer, List<SilvercarDropCfg>> getDropMap() {
		return dropMap;
	}

	public double[] getRefreshRate() {
		return refreshRate;
	}

	public String getMasterTask() {
		return masterTask;
	}

	public int getMasterTaskMoney() {
		return masterTaskMoney;
	}

	public int getSelfCarId() {
		return selfCarId;
	}

	public int getJiazuCarId() {
		return jiazuCarId;
	}

	public String getSelfCarTaskGroupName() {
		return selfCarTaskGroupName;
	}

	public String getJiazuCarTaskGroupName() {
		return jiazuCarTaskGroupName;
	}

	public double getCountryTrafficRate() {
		return countryTrafficRate;
	}

	public long getCarCallForhelpDistance() {
		return carCallForhelpDistance;
	}

	public long getPlayerHelpSilvercarDistance() {
		return playerHelpSilvercarDistance;
	}

	public double getNoticeHPLimit() {
		return noticeHPLimit;
	}

	public double[] getCarSize() {
		return carSize;
	}

	public String getJiazuSilvercarOtherPrize() {
		return jiazuSilvercarOtherPrize;
	}

	public double[] getJiazuSilvercarOtherPrizeRate() {
		return jiazuSilvercarOtherPrizeRate;
	}

	private void load() throws Exception {
		try {
			File file = new File(getFilePath());
			if (!file.exists()) {
				logger.error("[押镖文件加载][异常][文件不存在]");
				throw new Exception();
			}

			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			HSSFWorkbook workbook = new HSSFWorkbook(pss);

			HSSFSheet sheet = workbook.getSheetAt(0);
			int maxRow = sheet.getPhysicalNumberOfRows();

			for (int i = 1; i < maxRow; i++) {
				HSSFRow row = sheet.getRow(i);
				int index = 0;
				String taskName = row.getCell(index++).getStringCellValue();
				String needArticleName = row.getCell(index++).getStringCellValue();
				int needArticleColor = (int) row.getCell(index++).getNumericCellValue();
				int needMoney = (int) row.getCell(index++).getNumericCellValue();
				SilvercarTaskCfg cfg = new SilvercarTaskCfg(taskName, needArticleName, needArticleColor, needMoney);
				taskCfgMap.put(cfg.getTaskName(), cfg);
			}

			sheet = workbook.getSheetAt(1);

			maxRow = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < maxRow; i++) {
				HSSFRow row = sheet.getRow(i);
				int index = 0;
				int npcTempletId = (int) row.getCell(index++).getNumericCellValue();
				int carColor = (int) row.getCell(index++).getNumericCellValue();
				int[] dropColor = new int[]{StringTool.getCellValue(row.getCell(index++), int.class)};
				double[] dropRate = Double2double(StringTool.string2Array(StringTool.getCellValue(row.getCell(index++), String.class), ",", Double.class));
				String[] dropName = StringTool.string2Array(StringTool.getCellValue(row.getCell(index++), String.class), ",", String.class);
				SilvercarDropCfg cfg = new SilvercarDropCfg(npcTempletId, carColor, dropColor, dropName, dropRate);
				if (!dropMap.containsKey(cfg.getNpcTempletId())) {
					dropMap.put(cfg.getNpcTempletId(), new ArrayList<SilvercarDropCfg>());
				}
				dropMap.get(cfg.getNpcTempletId()).add(cfg);
			}

			sheet = workbook.getSheetAt(2);
			int index = 0;
			HSSFRow row = sheet.getRow(1);
			refreshRate = Double2double(StringTool.string2Array(StringTool.getCellValue(row.getCell(index++), String.class), ",", Double.class));
			masterTask = row.getCell(index++).getStringCellValue();
			masterTaskMoney = StringTool.getCellValue(row.getCell(index++), int.class);
			selfCarId =  StringTool.getCellValue(row.getCell(index++), int.class);
			jiazuCarId =  StringTool.getCellValue(row.getCell(index++), int.class);
			selfCarTaskGroupName = row.getCell(index++).getStringCellValue();
			jiazuCarTaskGroupName = row.getCell(index++).getStringCellValue();
			countryTrafficRate =  StringTool.getCellValue(row.getCell(index++), double.class);
			carCallForhelpDistance =  StringTool.getCellValue(row.getCell(index++), long.class) * TimeTool.TimeDistance.SECOND.getTimeMillis();
			carSize = Double2double(StringTool.string2Array(StringTool.getCellValue(row.getCell(index++), String.class), ",", Double.class));
			jiazuSilvercarOtherPrize = StringTool.getCellValue(row.getCell(index++), String.class);
			jiazuSilvercarOtherPrizeRate = Double2double(StringTool.string2Array( StringTool.getCellValue(row.getCell(index++), String.class), ",", Double.class));
			jiazuSilvercarOtherPrizeRate4posui = Double2double(StringTool.string2Array( StringTool.getCellValue(row.getCell(index++), String.class), ",", Double.class));
			jiazuSilvercarOtherPrizeRate4bai = Double2double(StringTool.string2Array( StringTool.getCellValue(row.getCell(index++), String.class), ",", Double.class));
			jiazuSilvercarOtherPrizeRate4lv = Double2double(StringTool.string2Array( StringTool.getCellValue(row.getCell(index++), String.class), ",", Double.class));
			jiazuSilvercarOtherPrizeRate4lan = Double2double(StringTool.string2Array( StringTool.getCellValue(row.getCell(index++), String.class), ",", Double.class));
			jiazuSilvercarOtherPrizeRate4zi = Double2double(StringTool.string2Array( StringTool.getCellValue(row.getCell(index++), String.class), ",", Double.class));
			jiazuSilvercarOtherPrizeRate4cheng = Double2double(StringTool.string2Array( StringTool.getCellValue(row.getCell(index++), String.class), ",", Double.class));
		} catch (Exception e) {
			logger.error("[押镖配置异常]", e);
			throw e;
		}

	}
	
	public double[] getRateByColor(int color) {
		if (color <= 0) {
			return jiazuSilvercarOtherPrizeRate4posui;
		} else if (color <= 1) {
			return jiazuSilvercarOtherPrizeRate4bai;
		} else if (color <= 2) {
			return jiazuSilvercarOtherPrizeRate4lv;
		} else if (color <= 3) {
			return jiazuSilvercarOtherPrizeRate4lan;
		} else if (color <= 4) {
			return jiazuSilvercarOtherPrizeRate4zi;
		} else {
			return jiazuSilvercarOtherPrizeRate4cheng;
		}
	}

	private double[] Double2double(Double[] value) {
		double[] res = new double[value.length];
		for (int i = 0; i < value.length; i++) {
			res[i] = value[i];
		}
		return res;
	}

	private int[] Integer2int(Integer[] value) {
		int[] res = new int[value.length];
		for (int i = 0; i < value.length; i++) {
			res[i] = value[i];
		}
		return res;
	}

	/**
	 * 通知世界某人得到了某颜色的镖车
	 * @param player
	 * @param color
	 */
	public static void sneerAt(Player player, int color) {
		if (logger.isInfoEnabled()) logger.info(player.getLogString() + "[接到了镖车颜色:{}]", new Object[] { color });
		if (color >= ArticleManager.equipment_color_紫) {// 紫色和紫色以上的通知
			ChatMessage msg = new ChatMessage();
			StringBuffer sbf = new StringBuffer();
			// 恭喜@STRING_1@ 的 @STRING_2@得到了@STRING_3@镖车!
			Translate.translateString(Translate.镖车公告, new String[][] { { Translate.STRING_1, CountryManager.得到国家名(player.getCountry()) }, { Translate.STRING_2, player.getName() }, { Translate.STRING_3, ArticleManager.color_article_Strings[color] } });
			// sbf.append("<f color='").append(ArticleManager.color_article[color]).append("'>").append("恭喜").append(CountryManager.得到国家名(player.getCountry())).append(" 的 ").append(player.getName()).append(" 得到了 ").append(ArticleManager.color_article_Strings[color]).append("镖车").append("</f>");
			sbf.append("<f color='").append(ArticleManager.color_article[color]).append("'>").append(Translate.translateString(Translate.镖车公告, new String[][] { { Translate.STRING_1, CountryManager.得到国家名(player.getCountry()) }, { Translate.STRING_2, player.getName() }, { Translate.STRING_3, ArticleManager.color_article_Strings[color] } })).append("</f>");
			msg.setMessageText(sbf.toString());
			if (logger.isInfoEnabled()) logger.info(player.getLogString() + "[接到了镖车颜色:{}][发送消息:{}]", new Object[] { color, msg.getMessageText() });
			try {
				ChatMessageService.getInstance().sendMessageToSystem(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public void init() throws Exception {
		
		instance = this;
		instance.load();
		ServiceStartRecord.startLog(this);
	}

	public double[] getJiazuSilvercarOtherPrizeRate4posui() {
		return jiazuSilvercarOtherPrizeRate4posui;
	}

	public void setJiazuSilvercarOtherPrizeRate4posui(double[] jiazuSilvercarOtherPrizeRate4posui) {
		this.jiazuSilvercarOtherPrizeRate4posui = jiazuSilvercarOtherPrizeRate4posui;
	}

	public double[] getJiazuSilvercarOtherPrizeRate4bai() {
		return jiazuSilvercarOtherPrizeRate4bai;
	}

	public void setJiazuSilvercarOtherPrizeRate4bai(double[] jiazuSilvercarOtherPrizeRate4bai) {
		this.jiazuSilvercarOtherPrizeRate4bai = jiazuSilvercarOtherPrizeRate4bai;
	}

	public double[] getJiazuSilvercarOtherPrizeRate4lv() {
		return jiazuSilvercarOtherPrizeRate4lv;
	}

	public void setJiazuSilvercarOtherPrizeRate4lv(double[] jiazuSilvercarOtherPrizeRate4lv) {
		this.jiazuSilvercarOtherPrizeRate4lv = jiazuSilvercarOtherPrizeRate4lv;
	}

	public double[] getJiazuSilvercarOtherPrizeRate4lan() {
		return jiazuSilvercarOtherPrizeRate4lan;
	}

	public void setJiazuSilvercarOtherPrizeRate4lan(double[] jiazuSilvercarOtherPrizeRate4lan) {
		this.jiazuSilvercarOtherPrizeRate4lan = jiazuSilvercarOtherPrizeRate4lan;
	}

	public double[] getJiazuSilvercarOtherPrizeRate4zi() {
		return jiazuSilvercarOtherPrizeRate4zi;
	}

	public void setJiazuSilvercarOtherPrizeRate4zi(double[] jiazuSilvercarOtherPrizeRate4zi) {
		this.jiazuSilvercarOtherPrizeRate4zi = jiazuSilvercarOtherPrizeRate4zi;
	}

	public double[] getJiazuSilvercarOtherPrizeRate4cheng() {
		return jiazuSilvercarOtherPrizeRate4cheng;
	}

	public void setJiazuSilvercarOtherPrizeRate4cheng(double[] jiazuSilvercarOtherPrizeRate4cheng) {
		this.jiazuSilvercarOtherPrizeRate4cheng = jiazuSilvercarOtherPrizeRate4cheng;
	}
	
	

}
