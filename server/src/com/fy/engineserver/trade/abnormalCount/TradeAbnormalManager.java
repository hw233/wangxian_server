package com.fy.engineserver.trade.abnormalCount;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.deal.Deal;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.server.TestServerConfigManager;
import com.sqage.stat.client.StatClientService;
import com.sqage.stat.model.Transaction_SpecialFlow;
import com.xuanzhi.boss.game.GameConstants;

//异常交易管理类
public class TradeAbnormalManager {
	
	public static Logger logger = LoggerFactory.getLogger(TradeAbnormalManager.class);

	private static TradeAbnormalManager instance;
	
	public static TradeAbnormalManager getInstance() {
		return instance;
	}
	
	//装备的  等级范围  int[] 里对应的是各个颜色的价值
	private HashMap<String, long[]> playerEquipValues = new HashMap<String, long[]>();
	private HashMap<String, long[]> horseEquipValues = new HashMap<String, long[]>();
	//一般物品的，名字  int[]里对应的是各个颜色的价值
	private HashMap<String, long[]> propValues = new HashMap<String, long[]>();
	
	private static final int[][] LEVEL_SECTION = new int[][] { { 1, 20 }, { 21, 40 }, { 41, 60 }, { 61, 80 }, { 81, 100 }, { 101, 120 }, { 121, 140 }, { 141, 160 }, { 161, 180 }, { 181, 200 }, { 201, 220 } };
	
	public static float TRADE_NORMAL_POWER = 1.3f;	//达到多少倍
	
	public static int DEAL_NORMAL_MONEY = 3 * 1000 * 1000;			//最多钱5锭
	
	private String file;
	
	public void init() {
		
		logger.warn("初始化开始");
		instance = this;
		initPlayerEquip();
		initHorseEquip();
		initProp();
		logger.warn("初始化完成");
		ServiceStartRecord.startLog(this);
	}
	
	public void initPlayerEquip() {
		String[] equLevels = new String[]{"1-20", "21-40", "41-60", "61-80", "81-100", "101-120", "121-140", "141-160", "161-180", "181-200", "201-220"};
		long[][] values = new long[][] {{1500, 1500*5, 1500*5*5, 1500*5*5*5, 1500*5*5*5*5, 1500*5*5*5*5*5, 1500*5*5*5*5*5*5},
				{1500, 1500*5, 1500*5*5, 1500*5*5*5, 1500*5*5*5*5, 1500*5*5*5*5*5, 1500*5*5*5*5*5*5},
				{1500, 1500*5, 1500*5*5, 1500*5*5*5, 1500*5*5*5*5, 1500*5*5*5*5*5, 1500*5*5*5*5*5*5},
				{1500, 1500*5, 1500*5*5, 1500*5*5*5, 1500*5*5*5*5, 1500*5*5*5*5*5, 1500*5*5*5*5*5*5},
				{1500, 1500*5, 1500*5*5, 1500*5*5*5, 1500*5*5*5*5, 1500*5*5*5*5*5, 1500*5*5*5*5*5*5},
				{1500, 1500*5, 1500*5*5, 1500*5*5*5, 1500*5*5*5*5, 1500*5*5*5*5*5, 1500*5*5*5*5*5*5},
				{1500, 1500*5, 1500*5*5, 1500*5*5*5, 1500*5*5*5*5, 1500*5*5*5*5*5, 1500*5*5*5*5*5*5},
				{1500, 1500*5, 1500*5*5, 1500*5*5*5, 1500*5*5*5*5, 1500*5*5*5*5*5, 1500*5*5*5*5*5*5},
				{1500, 1500*5, 1500*5*5, 1500*5*5*5, 1500*5*5*5*5, 1500*5*5*5*5*5, 1500*5*5*5*5*5*5},
				{1500, 1500*5, 1500*5*5, 1500*5*5*5, 1500*5*5*5*5, 1500*5*5*5*5*5, 1500*5*5*5*5*5*5},
				{1500, 1500*5, 1500*5*5, 1500*5*5*5, 1500*5*5*5*5, 1500*5*5*5*5*5, 1500*5*5*5*5*5*5}};
		for (int i = 0 ; i < equLevels.length; i++) {
			playerEquipValues.put(equLevels[i], values[i]);
		}
	}
	
	public void initHorseEquip() {
		String[] equLevels = new String[]{"1-20", "21-40", "41-60", "61-80", "81-100", "101-120", "121-140", "141-160", "161-180", "181-200", "201-220"};
		long[][] values = new long[][] {{500, 500*5, 500*5*5, 500*5*5*5, 500*5*5*5*5, 500*5*5*5*5*5, 500*5*5*5*5*5*5},
				{500, 500*5, 500*5*5, 500*5*5*5, 500*5*5*5*5, 500*5*5*5*5*5, 500*5*5*5*5*5*5},
				{500, 500*5, 500*5*5, 500*5*5*5, 500*5*5*5*5, 500*5*5*5*5*5, 500*5*5*5*5*5*5},
				{500, 500*5, 500*5*5, 500*5*5*5, 500*5*5*5*5, 500*5*5*5*5*5, 500*5*5*5*5*5*5},
				{500, 500*5, 500*5*5, 500*5*5*5, 500*5*5*5*5, 500*5*5*5*5*5, 500*5*5*5*5*5*5},
				{500, 500*5, 500*5*5, 500*5*5*5, 500*5*5*5*5, 500*5*5*5*5*5, 500*5*5*5*5*5*5},
				{500, 500*5, 500*5*5, 500*5*5*5, 500*5*5*5*5, 500*5*5*5*5*5, 500*5*5*5*5*5*5},
				{500, 500*5, 500*5*5, 500*5*5*5, 500*5*5*5*5, 500*5*5*5*5*5, 500*5*5*5*5*5*5},
				{500, 500*5, 500*5*5, 500*5*5*5, 500*5*5*5*5, 500*5*5*5*5*5, 500*5*5*5*5*5*5},
				{500, 500*5, 500*5*5, 500*5*5*5, 500*5*5*5*5, 500*5*5*5*5*5, 500*5*5*5*5*5*5},
				{500, 500*5, 500*5*5, 500*5*5*5, 500*5*5*5*5, 500*5*5*5*5*5, 500*5*5*5*5*5*5}};
		for (int i = 0 ; i < equLevels.length; i++) {
			horseEquipValues.put(equLevels[i], values[i]);
		}
	}
	
	public void initProp() {
		try{
			File f = new File(getFile());
			logger.warn("~~~~" + getFile());
			if (!f.exists()) {
				// logger.fatal("任务配置文件不存在");
				return;
			}
			Workbook workbook = Workbook.getWorkbook(f);
			// 特殊品质颜色的
			Sheet sheet1 = workbook.getSheet(0);
			for (int i = 1, n = sheet1.getRows(); i < n; i++) {
				jxl.Cell[] cells1 = sheet1.getRow(i);
				if (cells1.length <= 0 || cells1[0].getContents() == null || cells1[1].equals("")) {
					continue;
				}
				String name = cells1[0].getContents();
				long bai = Long.parseLong(cells1[1].getContents());
				long lv = Long.parseLong(cells1[2].getContents());
				long lan = Long.parseLong(cells1[3].getContents());
				long zi = Long.parseLong(cells1[4].getContents());
				long cheng = Long.parseLong(cells1[5].getContents());
				propValues.put(name, new long[]{bai, lv, lan, zi, cheng});
			}
		}catch(Exception e) {
			logger.error("initProp------", e);
		}
	}
	
	public long getValue2Entity(ArticleEntity entity) {
		if (entity == null) {
			return 0;
		}
		int colotType = entity.getColorType();
		Article a = ArticleManager.getInstance().getArticle(entity.getArticleName());
		if (a instanceof Equipment) {
			Equipment e = (Equipment)a;
			String levelString = null;
			int level = e.getPlayerLevelLimit();
			for (int i = 0 ; i < LEVEL_SECTION.length; i++) {
				if (level <= LEVEL_SECTION[i][1] && level >= LEVEL_SECTION[i][0]) {
					levelString = LEVEL_SECTION[i][0] + "-" + LEVEL_SECTION[i][1];
					break;
				}
			}
			if (levelString == null) {
				return 0;
			}
			int equipmentType = e.getEquipmentType();
			if (equipmentType <= EquipmentColumn.EQUIPMENT_TYPE_FOR_PLAYER || 
					equipmentType == EquipmentColumn.EQUIPMENT_TYPE_ChiBang) {
				//人物装备
				long[] values = playerEquipValues.get(levelString);
				if (values != null && values.length > colotType) {
					return values[colotType];
				}
			}else {
				//坐骑装备
				long[] values = horseEquipValues.get(levelString);
				if (values != null && values.length > colotType) {
					return values[colotType];
				}
			}
		} else {
			//一般物品
			long[] values = propValues.get(entity.getArticleName());
			if (values != null && values.length > colotType) {
				return values[colotType];
			}
		}
		return 0;
	}
	
	public void isNormalDeal(Deal deal) {
		try{
			if (deal == null) {
				return;
			}
			Player playerA = deal.getPlayerA();
			Player playerB = deal.getPlayerB();
			if (playerA == null || playerB == null) {
				return;
			}
			
			long valueA = 0;
			long valueB = 0;
			StringBuffer entityABuffer = new StringBuffer("");
			StringBuffer entityBBuffer = new StringBuffer("");
			StringBuffer entityABuff = new StringBuffer("");
			StringBuffer entityBBuff = new StringBuffer("");
			for (int i = 0; i < deal.getEntityIdsA().length; i++) {
				if (deal.getEntityIdsA()[i] > 0 && deal.getCountsA()[i] > 0) {
					ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(deal.getEntityIdsA()[i]);
					if (entity != null) {
						entityABuffer.append("{").append(entity.getId()).append("-").append(entity.getArticleName()).append("-").append(entity.getColorType()).append("-").append(deal.getCountsA()[i]).append("}");
						entityABuff.append("{").append(entity.getArticleName()).append("-").append(entity.getColorType()).append("-").append(deal.getCountsA()[i]).append("}");
						valueA += getValue2Entity(entity) * deal.getCountsA()[i];
					}
				}
			}
			for (int i = 0; i < deal.getEntityIdsB().length; i++) {
				if (deal.getEntityIdsB()[i] > 0 && deal.getCountsB()[i] > 0) {
					ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(deal.getEntityIdsB()[i]);
					if (entity != null) {
						entityBBuffer.append("{").append(entity.getId()).append("-").append(entity.getArticleName()).append("-").append(entity.getColorType()).append("-").append(deal.getCountsB()[i]).append("}");
						entityBBuff.append("{").append(entity.getArticleName()).append("-").append(entity.getColorType()).append("-").append(deal.getCountsB()[i]).append("}");
						valueB += getValue2Entity(entity) * deal.getCountsB()[i];
					}
				}
			}
			
			long coinsA = deal.getCoinsA();
			long coinsB = deal.getCoinsB();
			
			if (valueA == 0 && coinsB >= DEAL_NORMAL_MONEY) {
				//交易有问题  B给A钱
				logger.warn("[交易异常] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{playerA.getLogString(), playerB.getLogString(), valueA+"/"+coinsA, valueB+"/"+coinsB, entityABuffer.toString(), entityBBuffer.toString()});
				sendToArticleStat("不正常面对面交易", playerA, playerB, coinsA, coinsB, valueA, valueB, entityABuff.toString(), entityBBuff.toString());
			}else if (valueB == 0 && coinsA >= DEAL_NORMAL_MONEY) {
				//交易有问题  A给B钱
				logger.warn("[交易异常] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{playerA.getLogString(), playerB.getLogString(), valueA+"/"+coinsA, valueB+"/"+coinsB, entityABuffer.toString(), entityBBuffer.toString()});
				sendToArticleStat("不正常面对面交易", playerA, playerB, coinsA, coinsB, valueA, valueB, entityABuff.toString(), entityBBuff.toString());
			}else if(valueA != 0 && (float)coinsB / valueA >= TRADE_NORMAL_POWER && coinsB >= DEAL_NORMAL_MONEY) {
				//交易A的物品有价值   但与B交易的钱差距5倍  且交易金额超过5锭
				logger.warn("[交易异常] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{playerA.getLogString(), playerB.getLogString(), valueA+"/"+coinsA, valueB+"/"+coinsB, entityABuffer.toString(), entityBBuffer.toString()});
				sendToArticleStat("不正常面对面交易", playerA, playerB, coinsA, coinsB, valueA, valueB, entityABuff.toString(), entityBBuff.toString());
			}else if (valueB != 0 && (float)coinsA / valueB >= TRADE_NORMAL_POWER && coinsA >= DEAL_NORMAL_MONEY) {
				//交易B的物品有价值   但与A交易的钱差距5倍  且交易金额超过5锭
				logger.warn("[交易异常] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{playerA.getLogString(), playerB.getLogString(), valueA+"/"+coinsA, valueB+"/"+coinsB, entityABuffer, entityBBuffer.toString()});
				sendToArticleStat("不正常面对面交易", playerA, playerB, coinsA, coinsB, valueA, valueB, entityABuff.toString(), entityBBuff.toString());
			}else {
				//交易没有问题
				logger.warn("[交易正常] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{playerA.getLogString(), playerB.getLogString(), valueA+"/"+coinsA, valueB+"/"+coinsB, entityABuffer, entityBBuffer.toString()});
			}
		}catch (Exception e) {
			logger.error("isNormalDeal", e);
		}
	}
	
	public void isNormalBoothsale(Player sale, Player buy, ArticleEntity entity, int num, long price) {
		try{
			if (sale == null || buy == null || entity == null) {
				return;
			}
			long value = getValue2Entity(entity) * num;
			//如果价值为0的话判断摆摊金额是不是大于5锭，如果价值不为0的话判断  金额与价值比例是不是  5倍    且交易金额超过5锭
			if ((value == 0 && price >= DEAL_NORMAL_MONEY )|| (value != 0 && (float)price / value >= TRADE_NORMAL_POWER && price >= DEAL_NORMAL_MONEY)) {
				//不正常摆摊
				logger.warn("[不正常摆摊] [sale={}] [buy={}] [Entity={}] [Num={}] [price={}] [value={}]", new Object[]{sale.getLogString(), buy.getLogString(), entity.getId() + "-" + entity.getArticleName() + "-" + entity.getColorType(), num, price, value});
				String entityA = "{" + entity.getArticleName() + "-" + entity.getColorType() + "-" + num + "}";
				sendToArticleStat("不正常摆摊交易", sale, buy, 0, price, value, 0, entityA, "");
			}else {
				//正常摆摊
				logger.warn("[没问题摆摊] [sale={}] [buy={}] [Entity={}] [Num={}] [price={}] [value={}]", new Object[]{sale.getLogString(), buy.getLogString(), entity.getId() + "-" + entity.getArticleName() + "-" + entity.getColorType(), num, price, value});
			
			}
		}catch(Exception e) {
			logger.error("isNormalBoothsale", e);
		}
	}
	
	public void sendToArticleStat(String type,Player A, Player B, long yinziA, long yinziB, long jiazhiA, long jiazhiB, String entityNameA, String entityNameB) {
		StatClientService statClientService = StatClientService.getInstance();
		Transaction_SpecialFlow transaction_SpecialFlow = new Transaction_SpecialFlow();
        transaction_SpecialFlow.setId(0L);
        transaction_SpecialFlow.setCreateDate(new Date().getTime());					//时间
        transaction_SpecialFlow.setFdaoJuName(entityNameA);								//物品说明
        transaction_SpecialFlow.setFenQu(GameConstants.getInstance().getServerName());	//服务器名字
        transaction_SpecialFlow.setFjiazhi(jiazhiA);									//交易物品价值
        transaction_SpecialFlow.setFplayerName(A.getName());							//名字
        transaction_SpecialFlow.setFquDao(A.getPassport().getLastLoginChannel());		//渠道
        transaction_SpecialFlow.setFuserName(A.getUsername());							//用户名
        transaction_SpecialFlow.setFuuId(A.getPassport().getLastLoginClientId());		//clientID
        transaction_SpecialFlow.setFyinzi(yinziA);										//银子
        transaction_SpecialFlow.setTodaoJuName(entityNameB);							//物品说明
        transaction_SpecialFlow.setTojiazhi(jiazhiB);									//交易物品价值
        transaction_SpecialFlow.setToPlayername(B.getName());							//名字
        transaction_SpecialFlow.setToquDao(B.getPassport().getLastLoginChannel());		//渠道
        transaction_SpecialFlow.setToUserName(B.getUsername());							//用户名
        transaction_SpecialFlow.setTouuId(B.getPassport().getLastLoginClientId());		//clientID
        transaction_SpecialFlow.setToyinzi(yinziB);										//银子
        transaction_SpecialFlow.setTransactionType(type);								//类型Type
        
        transaction_SpecialFlow.setFregisttime(A.getPassport().getRegisterDate().getTime());      //发出者注册时间
        transaction_SpecialFlow.setFcreatPlayerTime(0L); 				//发出者创建角色时间
        transaction_SpecialFlow.setFlevel(A.getLevel()+"");                            				//发出者级别
        transaction_SpecialFlow.setFvip(A.getVipLevel()+"");                             				//发出vip
        transaction_SpecialFlow.setFtotalMoney(A.getRMB());                      				//出货方总充值钱数
        
        transaction_SpecialFlow.setToRegisttime(B.getPassport().getRegisterDate().getTime());   				//收货者注册时间
        transaction_SpecialFlow.setToTotalMoney(0L);								//收货者创建角色时间
        transaction_SpecialFlow.setToLevel(B.getLevel()+"");										//收货者级别
        transaction_SpecialFlow.setToVip(B.getVipLevel()+"");											//收货vip
        transaction_SpecialFlow.setToTotalMoney(B.getRMB());									//收货方总充值钱数
        if (!TestServerConfigManager.isTestServer() ) {
        	statClientService.sendTransaction_SpecialFlow("",transaction_SpecialFlow);
		}
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getFile() {
		return file;
	}
}
