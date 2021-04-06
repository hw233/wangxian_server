package com.fy.engineserver.activity.dig;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;

public class DigManager {
	private static DigManager instance;
	private String fileName;
	public static int MAXNUM = 20;

	public static DigManager getInstance() {
		return instance;
	}

	private List<DigTemplate> digList = new ArrayList<DigTemplate>();
	private List<DigRefMapInfo> digRefInfoList = new ArrayList<DigRefMapInfo>();

	public static final int EVENT_MONSTER = 0;
	public static final int EVENT_ARTICLE = 1;
	public static final int EVENT_EXP = 2;
	public static final int EVENT_BINDSILVER = 3;

	private Map<String, RateObject> rateMap = new HashMap<String, RateObject>();

	/** 刷怪事件<使用的道具,<玩家级别,怪物数据>> */
	private Map<String, Map<Integer, DigEventOfMonster>> digEventOfMonsterMap = new HashMap<String, Map<Integer, DigEventOfMonster>>();

	/** 经验事件<使用的道具,<玩家级别,经验数据>> */
	private Map<String, Map<Integer, DigEventOfExp>> digEventOfExpMap = new HashMap<String, Map<Integer, DigEventOfExp>>();

	/** 绑银事件<使用的道具,<玩家级别,绑银数据>> */
	private Map<String, Map<Integer, DigEventOfBindSilver>> digEventOfBindSilverMap = new HashMap<String, Map<Integer, DigEventOfBindSilver>>();

	/** 道具事件<使用的道具,道具数据> */
	private Map<String, DigEventOfArticle> digEventOfArticleMap = new HashMap<String, DigEventOfArticle>();

	public int getEventResult(String useArticleName) {
		int total = 0;
		RateObject ro = rateMap.get(useArticleName);
		if (ro == null) {
			throw new IllegalStateException("使用的道具不在配置的道具列表:" + useArticleName);
		}
		int[] rate = ro.rates;
		for (int everyRate : rate) {
			total += everyRate;
		}
		int result = random.nextInt(total);
		int low = 0;
		int max = 0;
		for (int i = 0; i < rate.length; i++) {
			max += rate[i];
			if (low <= result && result < max) {
				return i;
			}
			low += rate[i];
		}
		return 0;
	}

	public void noticeFindRightposition(Player player, Game game, String articleName) {
		DigEvent oneDigEvent = getOneDigEvent(player, articleName);
		if (oneDigEvent == null) {
			TaskSubSystem.logger.error("[" + player.getLogString() + "] [角色找到挖宝位置,事件未找到] [game:" + game.gi.name + "] [道具:" + articleName + "]");
			throw new IllegalStateException("[角色找到挖宝位置,事件未找到] [" + player.getLogString() + "] [game:" + game.gi.name + "] [道具:" + articleName + "]");
		}
		oneDigEvent.execute(player, game);
		TaskSubSystem.logger.error("[" + player.getLogString() + "] [角色找到挖宝位置,随机生成事件] [game:" + game.gi.name + "]");
	}

	public DigEvent getOneDigEvent(Player player, String articleName) {
		DigEvent digEvent = null;
		int eventType = getEventResult(articleName);
		switch (eventType) {
		case EVENT_MONSTER:
			digEvent = getMonsterEvent(player, articleName);
			TaskSubSystem.logger.error("[" + player.getLogString() + "] [挖宝得到怪物事件]");
			break;
		case EVENT_ARTICLE:
			digEvent = getArticleEvent(player, articleName);
			TaskSubSystem.logger.error("[" + player.getLogString() + "] [挖宝得到随机道具事件]");
			break;
		case EVENT_EXP:
			digEvent = getExpEvent(player, articleName);
			TaskSubSystem.logger.error("[" + player.getLogString() + "] [挖宝得到经验事件]");
			break;
		case EVENT_BINDSILVER:
			digEvent = getBindSilverEvent(player, articleName);
			TaskSubSystem.logger.error("[" + player.getLogString() + "] [挖宝得到绑银事件]");
			break;

		default:
			break;
		}
		return digEvent;
	}

	private DigEvent getMonsterEvent(Player player, String articleName) {
		Map<Integer, DigEventOfMonster> map = digEventOfMonsterMap.get(articleName);
		if (map == null) {
			TaskSubSystem.logger.error("[" + player.getLogString() + "] [道具对应的怪物数据不存在,道具名称:" + articleName + "]");
			throw new IllegalStateException("道具对应的怪物数据不存在,道具名称:" + articleName);
		}
		DigEventOfMonster digEventOfMonster = map.get(player.getLevel());
		if (digEventOfMonster == null) {
			TaskSubSystem.logger.error("[" + player.getLogString() + "] [玩家等级对应的怪物数据不存在,道具名称:" + articleName + ",角色等级" + player.getLevel() + "]");
			throw new IllegalStateException("玩家等级对应的怪物数据不存在,道具名称:" + articleName + ",角色等级" + player.getLevel());
		}
		return digEventOfMonster;
	}

	private DigEvent getExpEvent(Player player, String articleName) {
		Map<Integer, DigEventOfExp> map = digEventOfExpMap.get(articleName);
		if (map == null) {
			TaskSubSystem.logger.error("[" + player.getLogString() + "] [道具对应的经验数据不存在,道具名称:" + articleName + "]");
			throw new IllegalStateException("道具对应的经验数据不存在,道具名称:" + articleName);
		}
		DigEventOfExp digEventOfExp = map.get(player.getLevel());
		if (digEventOfExp == null) {
			TaskSubSystem.logger.error("[" + player.getLogString() + "] [玩家等级对应的经验数据不存在,道具名称:" + articleName + ",角色等级" + player.getLevel() + "]");
			throw new IllegalStateException("玩家等级对应的经验数据不存在,道具名称:" + articleName + ",角色等级" + player.getLevel());
		}
		return digEventOfExp;
	}

	private DigEvent getBindSilverEvent(Player player, String articleName) {
		Map<Integer, DigEventOfBindSilver> map = digEventOfBindSilverMap.get(articleName);
		if (map == null) {
			TaskSubSystem.logger.error("[" + player.getLogString() + "] [道具对应的绑银数据不存在,道具名称:" + articleName + "]");
			throw new IllegalStateException("道具对应的绑银数据不存在,道具名称:" + articleName);
		}
		DigEventOfBindSilver digEventOfBindSilver = map.get(player.getLevel());
		if (digEventOfBindSilver == null) {
			TaskSubSystem.logger.error("[" + player.getLogString() + "] [玩家等级对应的绑银数据不存在,道具名称:" + articleName + ",角色等级" + player.getLevel() + "]");
			throw new IllegalStateException("玩家等级对应的绑银数据不存在,道具名称:" + articleName + ",角色等级" + player.getLevel());
		}
		return digEventOfBindSilver;
	}

	private DigEvent getArticleEvent(Player player, String articleName) {
		DigEventOfArticle digEventOfArticle = digEventOfArticleMap.get(articleName);
		if (digEventOfArticle == null) {
			TaskSubSystem.logger.error("[" + player.getLogString() + "] [道具对应的挖宝数据不存在,道具名称:" + articleName + "]");
			throw new IllegalStateException("道具对应的挖宝道具数据不存在,道具名称:" + articleName);
		}
		return digEventOfArticle;

	}

	public void clearExtraDigInfo(Player player) {
		Map<Long, DigTemplate> map = player.getDigInfo();
		if (map != null) {
			if (map.size() >= MAXNUM) {
				int size = map.size();
				map.clear();
				player.setDigInfo(map);
				TaskSubSystem.logger.error("[" + player.getLogString() + "] [清空玩家身上的挖宝信息] [数量:" + size + "]");
			}
		}
	}

	public void loadExcel() throws Exception {
		File file = new File(fileName);
		HSSFWorkbook workbook = null;
		InputStream is = new FileInputStream(file);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = null;
		{
			/** 挖宝地点标签页 */
			this.digList.clear();
			int 挖宝index = 0;
			int 挖宝地图 = 1;
			int 挖宝地图片段 = 2;
			int 挖宝地图x = 3;
			int 挖宝地图y = 4;
			int 范围 = 5;
			int 显示地图名 = 6;
			int 是否中立地图 = 7;
			sheet = workbook.getSheetAt(0);
			if (sheet == null) return;
			int rows = sheet.getPhysicalNumberOfRows();

			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					DigTemplate dt = new DigTemplate();
					HSSFCell cell = row.getCell(挖宝index);
					int index = (int) cell.getNumericCellValue();
					dt.setIndex(index);

					cell = row.getCell(挖宝地图);
					String exploreMapName = cell.getStringCellValue();
					dt.setMapName(exploreMapName);

					cell = row.getCell(挖宝地图片段);
					String mapSegmentName = cell.getStringCellValue();

					cell = row.getCell(挖宝地图x);
					int mapX = (int) cell.getNumericCellValue();

					cell = row.getCell(挖宝地图y);
					int mapY = (int) cell.getNumericCellValue();

					dt.setMapSegmentNames(mapSegmentName);
					Point point = new Point(mapX, mapY);
					dt.setPoints(point);
					cell = row.getCell(范围);
					int range = (int) cell.getNumericCellValue();
					dt.setRange(range);

					cell = row.getCell(显示地图名);
					String showMap = cell.getStringCellValue();
					dt.setShowMap(showMap);

					cell = row.getCell(是否中立地图);
					byte inCountry = (byte) cell.getNumericCellValue();
					dt.setInCountry(inCountry);

					digList.add(dt);
				}
			}
		}
		{

			/** 刷怪点标签页 */
			int 刷新地图 = 0;
			int 刷新坐标x = 1;
			int 刷新坐标y = 2;
			sheet = workbook.getSheetAt(1);
			if (sheet == null) return;
			int rows1 = sheet.getPhysicalNumberOfRows();

			for (int i = 1; i < rows1; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					HSSFCell cell = row.getCell(刷新地图);
					String mapName = cell.getStringCellValue();

					cell = row.getCell(刷新坐标x);
					int mapX = (int) cell.getNumericCellValue();
					cell = row.getCell(刷新坐标y);
					int mapY = (int) cell.getNumericCellValue();
					Point point = new Point(mapX, mapY);

					DigRefMapInfo dInfo = new DigRefMapInfo(mapName, point);
					digRefInfoList.add(dInfo);
				}
			}
		}
		{
			/** 事件几率页 */
			int 触发道具名 = 0;
			int 统计名 = 1;
			int 刷怪几率 = 2;
			int 道具几率 = 3;
			int 经验几率 = 4;
			int 绑银几率 = 5;
			sheet = workbook.getSheetAt(2);
			if (sheet == null) return;
			int rows2 = sheet.getPhysicalNumberOfRows();
			int index2 = 2;
			for (int i = 1; i < rows2; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int[] rate = new int[4];
					HSSFCell cell = row.getCell(触发道具名);
					String useArticleName = cell.getStringCellValue();

					cell = row.getCell(统计名);
					String useArticleNameStat = cell.getStringCellValue();
					rate[0] = (int) row.getCell(刷怪几率).getNumericCellValue();
					rate[1] = (int) row.getCell(道具几率).getNumericCellValue();
					rate[2] = (int) row.getCell(经验几率).getNumericCellValue();
					rate[3] = (int) row.getCell(绑银几率).getNumericCellValue();

					RateObject ro = new RateObject(useArticleName, useArticleNameStat, rate);
					rateMap.put(ro.getArticle(), ro);
				}
			}
		}
		{
			/** 刷怪页 */
			int 触发道具名 = 0;
			int 统计名 = 1;
			int 玩家等级 = 2;
			int 怪物ID = 3;
			int 刷新方式 = 4;
			int 是否广播 = 5;
			sheet = workbook.getSheetAt(3);
			if (sheet == null) return;
			int rows3 = sheet.getPhysicalNumberOfRows();

			for (int i = 1; i < rows3; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					HSSFCell cell = row.getCell(触发道具名);
					String useArticleName = cell.getStringCellValue();

					cell = row.getCell(统计名);
					String useArticleNameStat = cell.getStringCellValue();

					cell = row.getCell(玩家等级);
					int playerLevel = (int) cell.getNumericCellValue();

					cell = row.getCell(怪物ID);
					int monsterId = (int) cell.getNumericCellValue();

					cell = row.getCell(刷新方式);
					int refType = (int) cell.getNumericCellValue();

					cell = row.getCell(是否广播);
					int sendNotice = (int) cell.getNumericCellValue();

					DigEventOfMonster deom = new DigEventOfMonster(useArticleName, useArticleNameStat, playerLevel, monsterId, refType, sendNotice);
					if (digEventOfMonsterMap.containsKey(useArticleName)) {
						if (!digEventOfMonsterMap.get(useArticleName).containsKey(playerLevel)) {
							digEventOfMonsterMap.get(useArticleName).put(playerLevel, deom);
						}
					} else {
						Map<Integer, DigEventOfMonster> digMap = new HashMap<Integer, DigEventOfMonster>();
						digMap.put(playerLevel, deom);
						digEventOfMonsterMap.put(useArticleName, digMap);
					}
				}
			}
		}
		{
			/** 随机产出道具页 */
			int 触发道具名 = 0;
			int 统计名 = 1;
			int 随机道具名 = 2;
			int 随机道具统计名 = 3;
			int 颜色 = 4;
			int 数量 = 5;
			int 几率 = 6;
			int 是否绑定 = 7;
			sheet = workbook.getSheetAt(4);
			if (sheet == null) return;
			int rows4 = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows4; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					HSSFCell cell = row.getCell(触发道具名);
					String useArticleName = cell.getStringCellValue();
					cell = row.getCell(统计名);
					String useArticleNameStat = cell.getStringCellValue();
					cell = row.getCell(随机道具名);
					String rateName = cell.getStringCellValue();
					cell = row.getCell(随机道具统计名);
					String rateNameStat = cell.getStringCellValue();
					cell = row.getCell(颜色);
					int color = (int) cell.getNumericCellValue();
					cell = row.getCell(数量);
					int num = (int) cell.getNumericCellValue();
					cell = row.getCell(几率);
					int rate = (int) cell.getNumericCellValue();
					cell = row.getCell(是否绑定);
					boolean bind = (boolean) cell.getBooleanCellValue();

					DigRateArticle dra = new DigRateArticle(rateName, rateNameStat, color, num, rate, bind);

					if (digEventOfArticleMap.containsKey(useArticleName)) {
						digEventOfArticleMap.get(useArticleName).getList().add(dra);
					} else {
						List<DigRateArticle> list = new ArrayList<DigRateArticle>();
						list.add(dra);
						DigEventOfArticle deoa = new DigEventOfArticle(useArticleName, useArticleNameStat, list);
						digEventOfArticleMap.put(useArticleName, deoa);
					}
				}
			}
		}
		{
			/** 经验页 */
			int 触发道具名 = 0;
			int 统计名 = 1;
			int 玩家等级 = 2;
			int 经验 = 3;

			sheet = workbook.getSheetAt(5);
			if (sheet == null) return;
			int rows5 = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows5; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					HSSFCell cell = row.getCell(触发道具名);
					String useArticleName = cell.getStringCellValue();
					cell = row.getCell(统计名);
					String useArticleNameStat = cell.getStringCellValue();
					cell = row.getCell(玩家等级);
					int playerLevel = (int) cell.getNumericCellValue();
					cell = row.getCell(经验);
					long exp = (long) cell.getNumericCellValue();

					DigEventOfExp deoe = new DigEventOfExp(useArticleName, useArticleNameStat, playerLevel, exp);
					if (digEventOfExpMap.containsKey(useArticleName)) {
						if (!digEventOfExpMap.get(useArticleName).containsKey(playerLevel)) {
							digEventOfExpMap.get(useArticleName).put(playerLevel, deoe);
						}
					} else {
						Map<Integer, DigEventOfExp> digMap = new HashMap<Integer, DigEventOfExp>();
						digMap.put(playerLevel, deoe);
						digEventOfExpMap.put(useArticleName, digMap);
					}
				}
			}
		}
		{
			/** 绑银页 */
			int 触发道具名 = 0;
			int 统计名 = 1;
			int 玩家等级 = 2;
			int 绑银 = 3;
			sheet = workbook.getSheetAt(6);
			if (sheet == null) return;
			int rows6 = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows6; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					HSSFCell cell = row.getCell(触发道具名);
					String useArticleName = cell.getStringCellValue();
					cell = row.getCell(统计名);
					String useArticleNameStat = cell.getStringCellValue();
					cell = row.getCell(玩家等级);
					int playerLevel = (int) cell.getNumericCellValue();
					cell = row.getCell(绑银);
					long bindSilver = (long) cell.getNumericCellValue();

					DigEventOfBindSilver deoSilver = new DigEventOfBindSilver(useArticleName, useArticleNameStat, playerLevel, bindSilver);
					if (digEventOfBindSilverMap.containsKey(useArticleName)) {
						if (!digEventOfBindSilverMap.get(useArticleName).containsKey(playerLevel)) {
							digEventOfBindSilverMap.get(useArticleName).put(playerLevel, deoSilver);
						}
					} else {
						Map<Integer, DigEventOfBindSilver> digMap = new HashMap<Integer, DigEventOfBindSilver>();
						digMap.put(playerLevel, deoSilver);
						digEventOfBindSilverMap.put(useArticleName, digMap);
					}
				}
			}
		}
	}

	static Random random = new Random();

	/**
	 * 随机获得一个DigTemplate
	 * 
	 * @return
	 */
	public DigTemplate randomGetDigTemplate() {
		return digList.get(random.nextInt(digList.size()));
	}

	public void init() throws Exception {
		
		long start = System.currentTimeMillis();
		instance = this;
		loadExcel();
		TaskSubSystem.logger.info("DigManager init");
		ServiceStartRecord.startLog(this);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<DigTemplate> getDigList() {
		return digList;
	}

	public void setDigList(List<DigTemplate> digList) {
		this.digList = digList;
	}

	public List<DigRefMapInfo> getDigRefInfoList() {
		return digRefInfoList;
	}

	public void setDigRefInfoList(List<DigRefMapInfo> digRefInfoList) {
		this.digRefInfoList = digRefInfoList;
	}

	public Map<String, Map<Integer, DigEventOfMonster>> getDigEventOfMonsterMap() {
		return digEventOfMonsterMap;
	}

	public void setDigEventOfMonsterMap(Map<String, Map<Integer, DigEventOfMonster>> digEventOfMonsterMap) {
		this.digEventOfMonsterMap = digEventOfMonsterMap;
	}

	public Map<String, Map<Integer, DigEventOfExp>> getDigEventOfExpMap() {
		return digEventOfExpMap;
	}

	public void setDigEventOfExpMap(Map<String, Map<Integer, DigEventOfExp>> digEventOfExpMap) {
		this.digEventOfExpMap = digEventOfExpMap;
	}

	public Map<String, Map<Integer, DigEventOfBindSilver>> getDigEventOfBindSilverMap() {
		return digEventOfBindSilverMap;
	}

	public void setDigEventOfBindSilverMap(Map<String, Map<Integer, DigEventOfBindSilver>> digEventOfBindSilverMap) {
		this.digEventOfBindSilverMap = digEventOfBindSilverMap;
	}

	public Map<String, DigEventOfArticle> getDigEventOfArticleMap() {
		return digEventOfArticleMap;
	}

	public void setDigEventOfArticleMap(Map<String, DigEventOfArticle> digEventOfArticleMap) {
		this.digEventOfArticleMap = digEventOfArticleMap;
	}

	class RateObject {
		String article;
		String article_stat;

		int[] rates;

		public RateObject(String article, String articleStat, int[] rates) {
			super();
			this.article = article;
			article_stat = articleStat;
			this.rates = rates;
		}

		@Override
		public String toString() {
			return "RateObject [article=" + article + ", article_stat=" + article_stat + ", rates=" + Arrays.toString(rates) + "]";
		}

		public String getArticle() {
			return article;
		}

		public void setArticle(String article) {
			this.article = article;
		}

		public String getArticle_stat() {
			return article_stat;
		}

		public void setArticle_stat(String articleStat) {
			article_stat = articleStat;
		}

		public int[] getRates() {
			return rates;
		}

		public void setRates(int[] rates) {
			this.rates = rates;
		}
	}
	// public static void main(String[] args) {
	// DigManager dm = new DigManager();
	// dm.setFileName("D:\\code\\mieshi_server\\game_mieshi_server\\conf\\game_init_config\\activity\\digActivity.xls");
	// try {
	// dm.loadExcel();
	// System.out.println(dm.getDigList().size());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
}
