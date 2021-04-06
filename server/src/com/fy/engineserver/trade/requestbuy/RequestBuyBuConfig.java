package com.fy.engineserver.trade.requestbuy;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.trade.requestbuy.service.RequestBuyManager;

public class RequestBuyBuConfig {
	
	public static Logger logger = LoggerFactory.getLogger(RequestBuyBuConfig.class);
	
	public static final int REQUEST_TYPE_PLAYEREQUIP_1 = 0;						//装备
	public static final int REQUEST_TYPE_PLAYEREQUIP_2 = 1;
	public static final int REQUEST_TYPE_PLAYEREQUIP_3 = 2;
	public static final int REQUEST_TYPE_PLAYEREQUIP_4 = 3;
	public static final int REQUEST_TYPE_PLAYEREQUIP_5 = 4;
	public static final int REQUEST_TYPE_PLAYEREQUIP_6 = 5;
	public static final int REQUEST_TYPE_PLAYEREQUIP_7 = 6;
	public static final int REQUEST_TYPE_PLAYEREQUIP_8 = 7;
	public static final int REQUEST_TYPE_PLAYEREQUIP_9 = 8;
	public static final int REQUEST_TYPE_PLAYEREQUIP_10 = 9;
	public static final int REQUEST_TYPE_PLAYEREQUIP_11 = 10;
	public static final int REQUEST_TYPE_HORSEEQUIP_1 = 11;						//坐骑装备
	public static final int REQUEST_TYPE_HORSEEQUIP_2 = 12;
	public static final int REQUEST_TYPE_HORSEEQUIP_3 = 13;
	public static final int REQUEST_TYPE_HORSEEQUIP_4 = 14;
	public static final int REQUEST_TYPE_HORSEEQUIP_5 = 15;
	public static final int REQUEST_TYPE_HORSEEQUIP_6 = 16;
	public static final int REQUEST_TYPE_HORSEEQUIP_7 = 17;
	public static final int REQUEST_TYPE_HORSEEQUIP_8 = 18;
	public static final int REQUEST_TYPE_XINGHUACUN = 19;							//白玉泉
	public static final int REQUEST_TYPE_TUSUJIU = 20;							//金浆醒
	public static final int REQUEST_TYPE_MIAOQINYAOJIU = 21;						//香桂郢酒
	public static final int REQUEST_TYPE_TUMOTIE_1 = 22;							//封魔录
	public static final int REQUEST_TYPE_TUMOTIE_2 = 23;
	public static final int REQUEST_TYPE_TUMOTIE_3 = 24;
	public static final int REQUEST_TYPE_TUMOTIE_4 = 25;
	public static final int REQUEST_TYPE_TUMOTIE_5 = 26;
	public static final int REQUEST_TYPE_TUMOTIE_6 = 27;
	public static final int REQUEST_TYPE_TUMOTIE_7 = 28;
	public static final int REQUEST_TYPE_TUMOTIE_8 = 29;
	public static final int REQUEST_TYPE_TUMOTIE_9 = 30;
	public static final int REQUEST_TYPE_TUMOTIE_10 = 31;
	public static final int REQUEST_TYPE_YABIAOLING = 32;							//押镖令
	
	private static final int[][] LEVEL_SECTION = new int[][] { { 1, 20 }, { 21, 40 }, { 41, 60 }, { 61, 80 }, { 81, 100 }, { 101, 120 }, { 121, 140 }, { 141, 160 }, { 161, 180 }, { 181, 200 }, { 201, 220 } };
	private static final int[][] HORSE_SECTION = new int[][] { { 61, 80 }, { 81, 100 }, { 101, 120 }, { 121, 140 }, { 141, 160 }, { 161, 180 }, { 181, 200 }, { 201, 220 } };
	
	public static String[] REQUEST_TYPES = new String[]{"角色装备1-20","角色装备21-40","角色装备41-60","角色装备61-80","角色装备81-100","角色装备101-120","角色装备121-140","角色装备141-160","角色装备161-180","角色装备181-200","角色装备201-220",
		"坐骑装备61-80", "坐骑装备81-100", "坐骑装备101-120", "坐骑装备121-140", "坐骑装备141-160", "坐骑装备161-180", "坐骑装备181-200", "坐骑装备201-220",
		Translate.白玉泉, Translate.金浆醒, Translate.香桂郢酒,
		Translate.封魔录降魔,Translate.封魔录逍遥,Translate.封魔录霸者,Translate.封魔录朱雀,Translate.封魔录水晶,Translate.封魔录倚天,Translate.封魔录青虹, Translate.封魔录赤霄, Translate.封魔录震天, Translate.封魔录天罡,
		Translate.押镖令};
	public static String[] REQUEST_COLOR_TYPES = new String[]{"白色","绿色","蓝色"};
	
	//如果15分钟这个求购未有人卖东西 就补
//	public static long TIME_NOSALE = 3 * 60 * 1000;
	public static long TIME_NOSALE = 1 * 60 * 1000;
	
	//当求购比例小于这个比例之前，如果10求购发布30分钟了，就给他补
	public static float SALE_RATIO = 0.5f;
	public static long TIME_SALERATIO = 2 * 60 * 1000;
//	public static long TIME_SALERATIO = 5 * 60 * 1000;
	
	public static int BU_RANDOM = 4;
	
	//类型
	private int RequestType;
	//颜色
	private int colorType = 0;
	//最低价钱
	private long price;
	//频率   毫秒
	private int timeSpace = 60;
	//总量
	private int maxNum;
	//以补多少个
	private int buNum;
	//求购ID
	private long requestID;
	//求购进入这个对象监控的时间
	private long inTime;
	//求购最后一次出售时间
	private long lastSaleTime;
	//上次补求购的时间
	private long lastBuTime;
	//是否达到   N时间无人求购
	private boolean isInLast = false;
	//上次心跳时间
	private long lastHeatTime = 0;

	public HashMap<String, String> buMsg = new HashMap<String, String>();
	public long buAllMoney = 0;
	
	public String getLogString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("[").append(REQUEST_TYPES[RequestType]).append("] ");
		buffer.append("[").append(REQUEST_COLOR_TYPES[colorType]).append("] ");
		buffer.append("[").append(price).append("] ");
		buffer.append("[").append(timeSpace).append("] ");
		buffer.append("[").append(maxNum).append("] ");
		buffer.append("[").append(buNum).append("] ");
		buffer.append("[").append(buAllMoney).append("] ");
		buffer.append("[").append(requestID).append("] ");
		buffer.append("[").append(inTime).append("] ");
		buffer.append("[").append(lastSaleTime).append("] ");
		buffer.append("[").append(lastBuTime).append("] ");
		return buffer.toString();
	}
	
	public void isRefBuConfig () {
		try{
			if (lastHeatTime == 0) {
				lastHeatTime = System.currentTimeMillis();
				return;
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date data = new Date(lastHeatTime);
			String old = format.format(data);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(data);
			int lastDay = calendar.get(Calendar.DAY_OF_MONTH);
			data = new Date(System.currentTimeMillis());
			calendar.setTime(data);
			int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
			if (nowDay != lastDay) {
				logger.warn("[每天刷新补求购数量] [{}]", new Object[]{getLogString()});
				buMsg.put(old, getBuNum() + "个" + buAllMoney);
				buAllMoney = 0;
				setBuNum(0);
			}
			lastHeatTime = System.currentTimeMillis();
		} catch (Exception e) {
			logger.error("isRefBuConfig出错", e);
		}
	}
	
	public void doBuHuo() {
		if (requestID <= 0) {
			return;
		}
		
		if (getMaxNum() <= getBuNum()) {
			return;
		}
		RequestBuy buy = RequestBuyManager.getInstance().getRequestBuyMap().get(requestID);
		if (buy == null) {
			setRequestID(-1);
			return;
		}
		synchronized(buy){
			try{
				if (buy.isBeOver() || buy.isTimeOut()) {
					setRequestID(-1);
					return;
				}
				if (getPrice() > buy.getItem().getPerPrice()) {
					return;
				}
				long now = System.currentTimeMillis();
				if (now - getLastBuTime() < getTimeSpace()) {
					//未到补间隔
					return;
				}
				float radio = (float)(buy.getItem().getStartNum() - buy.getItem().getEntityNum())/buy.getItem().getStartNum();
				if (now - getLastSaleTime() > TIME_NOSALE || isInLast()) {
					//如果上次出售时间 到现在   差距  15分钟就补
					realyBu(buy);
					setInLast(true);
					setLastBuTime(now);
					logger.warn("[一段时间未求购] [{}] [{}]", new Object[]{getLogString(), radio});
				}else if (radio < SALE_RATIO && now - getInTime() > TIME_SALERATIO) {
					//如果出售比例小于设定比例，并上次出售时间超过 一定时间  补
					realyBu(buy);
					setLastBuTime(now);
					logger.warn("[比例未达到] [{}] [{}]", new Object[]{getLogString(), radio});
				}
			}catch(Exception e) {
				logger.error("doBuHuo出错----", e);
			}
		}
	}
	
	private void realyBu(RequestBuy buy) {
		try{
			String mainType = buy.getRule().getMainType();
			int num = 1 +( new Random().nextInt(BU_RANDOM));
			if (num > buy.getItem().getEntityNum()) {
				num = buy.getItem().getEntityNum();
			}
			if (num + getBuNum() > getMaxNum()) {
				num = getMaxNum() - getBuNum();
			}
			if (mainType.equals(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类])) {
				//角色装备
				for (int i = 0 ; i < num; i++) {
					Equipment equipment = getRandomPlayerEquipment();
					if (equipment != null) {
						ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(equipment, false, ArticleEntityManager.CREATE_REASON_BU_REQUEST, null, buy.getRule().getColor(), 1, true);
						if (entity != null) {
							try{
								MailManager.getInstance().sendMail(buy.getReleaserId(), new ArticleEntity[] {entity}, new int[] {num}, Translate.text_requestbuy_004, Translate.text_requestbuy_005, 0, 0, 0, "给求购者发物品");
							}catch(Exception e) {
								logger.error("[realyBu装备发送邮件] [出错] ["+buy.getReleaserId()+"-"+buy.getRule().getArticleName()+"]", e);
							}
							setBuNum(getBuNum() + 1);
							buy.getItem().subNum(buy, 1);
							buAllMoney += buy.getItem().getPerPrice();
							logger.warn("[求购补货] p={}] [rid={}] [eID={}] [num={}] [price={}] [color={}] [{}] [收取玩家钱:{}]", new Object[]{buy.getReleaserId(), buy.getId(), entity.getId()+"~"+entity.getArticleName()+"~"+equipment.getPlayerLevelLimit(), 1, buy.getItem().getPerPrice(), buy.getRule().getColor(), getLogString(), buy.getItem().getPerPrice()});
						}
					}
				}
			}else if (mainType.equals(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_马匹装备类])) {
				//坐骑装备
				for (int i = 0; i < num; i++) {
					Equipment equipment = getRandomHouseEquipment();
					if (equipment != null) {
						ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(equipment, false, ArticleEntityManager.CREATE_REASON_BU_REQUEST, null, buy.getRule().getColor(), 1, true);
						if (entity != null) {
							try{
								MailManager.getInstance().sendMail(buy.getReleaserId(), new ArticleEntity[] {entity}, new int[] {num}, Translate.text_requestbuy_004, Translate.text_requestbuy_005, 0, 0, 0, "给求购者发物品");
							}catch(Exception e) {
								logger.error("[realyBu装备发送邮件] [出错] ["+buy.getReleaserId()+"-"+buy.getRule().getArticleName()+"]", e);
							}
							setBuNum(getBuNum() + 1);
							buy.getItem().subNum(buy, 1);
							buAllMoney += buy.getItem().getPerPrice();
							logger.warn("[求购补货] p={}] [rid={}] [eID={}] [num={}] [price={}] [color={}] [{}] [收取玩家钱:{}]", new Object[]{buy.getReleaserId(), buy.getId(), entity.getId()+"~"+entity.getArticleName()+"~"+equipment.getPlayerLevelLimit(), 1, buy.getItem().getPerPrice(), buy.getRule().getColor(), getLogString(), buy.getItem().getPerPrice()});
						}
					}
				}
			}else {
				Article a = ArticleManager.getInstance().getArticle(buy.getRule().getArticleName());
				if (a != null) {
					ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(a, false, ArticleEntityManager.CREATE_REASON_BU_REQUEST, null, buy.getRule().getColor(), num, true);
					if (entity != null) {
						try{
							MailManager.getInstance().sendMail(buy.getReleaserId(), new ArticleEntity[] {entity}, new int[] {num}, Translate.text_requestbuy_004, Translate.text_requestbuy_005, 0, 0, 0, "给求购者发物品");
						}catch(Exception e) {
							logger.error("[realyBu一般物品发送邮件] [出错] ["+buy.getReleaserId()+"-"+buy.getRule().getArticleName()+"]", e);
						}
						setBuNum(getBuNum() + num);
						buy.getItem().subNum(buy, num);
						buAllMoney += buy.getItem().getPerPrice() * num;
						logger.warn("[求购补货] p={}] [rid={}] [eID={}] [num={}] [price={}] [color={}] [{}] [收取玩家钱:{}]", new Object[]{buy.getReleaserId(), buy.getId(), entity.getId()+"~"+entity.getArticleName(), num, buy.getItem().getPerPrice(), buy.getRule().getColor(), getLogString(), buy.getItem().getPerPrice() * num});
					}
				}
			}
		}catch(Exception e) {
			logger.error("realyBu出错----", e);
		}
	}
	
	//得到求购的规则对象，再根据规则对象去找该规则下最贵的
	public RequestBuyRule getRule() {
		RequestBuyRule rule = null;
		switch (getRequestType()) {
		case REQUEST_TYPE_PLAYEREQUIP_1:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类], "", "1-20", getColorType());
			return rule;
		case REQUEST_TYPE_PLAYEREQUIP_2:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类], "", "21-40", getColorType());
			return rule;
		case REQUEST_TYPE_PLAYEREQUIP_3:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类], "", "41-60", getColorType());
			return rule;
		case REQUEST_TYPE_PLAYEREQUIP_4:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类], "", "61-80", getColorType());
			return rule;
		case REQUEST_TYPE_PLAYEREQUIP_5:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类], "", "81-100", getColorType());
			return rule;
		case REQUEST_TYPE_PLAYEREQUIP_6:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类], "", "101-120", getColorType());
			return rule;
		case REQUEST_TYPE_PLAYEREQUIP_7:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类], "", "121-140", getColorType());
			return rule;
		case REQUEST_TYPE_PLAYEREQUIP_8:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类], "", "141-160", getColorType());
			return rule;
		case REQUEST_TYPE_PLAYEREQUIP_9:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类], "", "161-180", getColorType());
			return rule;
		case REQUEST_TYPE_PLAYEREQUIP_10:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类], "", "181-200", getColorType());
			return rule;
		case REQUEST_TYPE_PLAYEREQUIP_11:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类], "", "201-220", getColorType());
			return rule;
		case REQUEST_TYPE_HORSEEQUIP_1:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_马匹装备类], "", "61-80", getColorType());
			return rule;
		case REQUEST_TYPE_HORSEEQUIP_2:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_马匹装备类], "", "81-100", getColorType());
			return rule;
		case REQUEST_TYPE_HORSEEQUIP_3:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_马匹装备类], "", "101-120", getColorType());
			return rule;
		case REQUEST_TYPE_HORSEEQUIP_4:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_马匹装备类], "", "121-140", getColorType());
			return rule;
		case REQUEST_TYPE_HORSEEQUIP_5:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_马匹装备类], "", "141-160", getColorType());
			return rule;
		case REQUEST_TYPE_HORSEEQUIP_6:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_马匹装备类], "", "161-180", getColorType());
			return rule;
		case REQUEST_TYPE_HORSEEQUIP_7:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_马匹装备类], "", "181-200", getColorType());
			return rule;
		case REQUEST_TYPE_HORSEEQUIP_8:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_马匹装备类], "", "201-220", getColorType());
			return rule;
		case REQUEST_TYPE_XINGHUACUN:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_人物消耗品类], ArticleManager.物品二级分类类名_人物消耗品类[ArticleManager.物品二级分类_酒], REQUEST_TYPES[REQUEST_TYPE_XINGHUACUN], getColorType());
			return rule;
		case REQUEST_TYPE_TUSUJIU:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_人物消耗品类], ArticleManager.物品二级分类类名_人物消耗品类[ArticleManager.物品二级分类_酒], REQUEST_TYPES[REQUEST_TYPE_TUSUJIU], getColorType());
			return rule;
		case REQUEST_TYPE_MIAOQINYAOJIU:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_人物消耗品类], ArticleManager.物品二级分类类名_人物消耗品类[ArticleManager.物品二级分类_酒], REQUEST_TYPES[REQUEST_TYPE_MIAOQINYAOJIU], getColorType());
			return rule;
		case REQUEST_TYPE_TUMOTIE_1:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_人物消耗品类], ArticleManager.物品二级分类类名_人物消耗品类[ArticleManager.物品二级分类_封魔录], REQUEST_TYPES[REQUEST_TYPE_TUMOTIE_1], getColorType());
			return rule;
		case REQUEST_TYPE_TUMOTIE_2:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_人物消耗品类], ArticleManager.物品二级分类类名_人物消耗品类[ArticleManager.物品二级分类_封魔录], REQUEST_TYPES[REQUEST_TYPE_TUMOTIE_2], getColorType());
			return rule;
		case REQUEST_TYPE_TUMOTIE_3:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_人物消耗品类], ArticleManager.物品二级分类类名_人物消耗品类[ArticleManager.物品二级分类_封魔录], REQUEST_TYPES[REQUEST_TYPE_TUMOTIE_3], getColorType());
			return rule;
		case REQUEST_TYPE_TUMOTIE_4:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_人物消耗品类], ArticleManager.物品二级分类类名_人物消耗品类[ArticleManager.物品二级分类_封魔录], REQUEST_TYPES[REQUEST_TYPE_TUMOTIE_4], getColorType());
			return rule;
		case REQUEST_TYPE_TUMOTIE_5:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_人物消耗品类], ArticleManager.物品二级分类类名_人物消耗品类[ArticleManager.物品二级分类_封魔录], REQUEST_TYPES[REQUEST_TYPE_TUMOTIE_5], getColorType());
			return rule;
		case REQUEST_TYPE_TUMOTIE_6:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_人物消耗品类], ArticleManager.物品二级分类类名_人物消耗品类[ArticleManager.物品二级分类_封魔录], REQUEST_TYPES[REQUEST_TYPE_TUMOTIE_6], getColorType());
			return rule;
		case REQUEST_TYPE_TUMOTIE_7:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_人物消耗品类], ArticleManager.物品二级分类类名_人物消耗品类[ArticleManager.物品二级分类_封魔录], REQUEST_TYPES[REQUEST_TYPE_TUMOTIE_7], getColorType());
			return rule;
		case REQUEST_TYPE_TUMOTIE_8:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_人物消耗品类], ArticleManager.物品二级分类类名_人物消耗品类[ArticleManager.物品二级分类_封魔录], REQUEST_TYPES[REQUEST_TYPE_TUMOTIE_8], getColorType());
			return rule;
		case REQUEST_TYPE_TUMOTIE_9:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_人物消耗品类], ArticleManager.物品二级分类类名_人物消耗品类[ArticleManager.物品二级分类_封魔录], REQUEST_TYPES[REQUEST_TYPE_TUMOTIE_9], getColorType());
			return rule;
		case REQUEST_TYPE_TUMOTIE_10:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_人物消耗品类], ArticleManager.物品二级分类类名_人物消耗品类[ArticleManager.物品二级分类_封魔录], REQUEST_TYPES[REQUEST_TYPE_TUMOTIE_10], getColorType());
			return rule;
		case REQUEST_TYPE_YABIAOLING:
			rule = RequestBuyManager.getInstance().getRequestBuyRule(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_人物消耗品类], ArticleManager.物品二级分类类名_人物消耗品类[ArticleManager.物品二级分类_押镖令], REQUEST_TYPES[REQUEST_TYPE_YABIAOLING], getColorType());
			return rule;
		}
		return rule;
	}
	
	public void addRequestBuy(RequestBuy buy) {
		if (buy.getId() == requestID) {
			return;
		}
		if (getPrice() > buy.getItem().getPerPrice()) {
			return;
		}
		setRequestID(buy.getId());
		setInTime(System.currentTimeMillis());
		setLastSaleTime(getInTime());
		setLastBuTime(0);
		setInLast(false);
		logger.warn("[换求购ID] [{}]", new Object[]{getLogString()});
	}
	
	public static RequestBuyBuConfig creat(DataInputStream dis) throws Exception {
		RequestBuyBuConfig config = new RequestBuyBuConfig();
		config.setRequestType(dis.readInt());
		config.setColorType(dis.readInt());
		config.setPrice(dis.readLong());
		config.setTimeSpace(dis.readInt());
		config.setMaxNum(dis.readInt());
		config.setBuNum(dis.readInt());
		config.setLastHeatTime(dis.readLong());
		config.buAllMoney = dis.readLong();
		int num = dis.readInt();
		for (int i = 0; i < num; i++) {
			config.buMsg.put(dis.readUTF(), dis.readUTF());
		}
		logger.warn(config.getLogString());
		return config;
	}
	
	public void save(DataOutputStream dos) throws Exception {
		dos.writeInt(getRequestType());
		dos.writeInt(getColorType());
		dos.writeLong(getPrice());
		dos.writeInt(getTimeSpace());
		dos.writeInt(getMaxNum());
		dos.writeInt(getBuNum());
		dos.writeLong(getLastHeatTime());
		dos.writeLong(buAllMoney);
		dos.writeInt(buMsg.size());
		for (String key : buMsg.keySet()) {
			dos.writeUTF(key);
			dos.writeUTF(buMsg.get(key));
		}
	}
	
	public void setColorType(int colorType) {
		this.colorType = colorType;
	}
	public int getColorType() {
		return colorType;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public long getPrice() {
		return price;
	}
	public void setTimeSpace(int timeSpace) {
		this.timeSpace = timeSpace;
	}
	public int getTimeSpace() {
		return timeSpace;
	}
	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setRequestType(int requestType) {
		RequestType = requestType;
	}

	public int getRequestType() {
		return RequestType;
	}

	public void setBuNum(int buNum) {
		this.buNum = buNum;
	}

	public int getBuNum() {
		return buNum;
	}

	public void setRequestID(long requestID) {
		this.requestID = requestID;
	}

	public long getRequestID() {
		return requestID;
	}

	public void setInTime(long inTime) {
		this.inTime = inTime;
	}

	public long getInTime() {
		return inTime;
	}

	public void setLastSaleTime(long lastSaleTime) {
		this.lastSaleTime = lastSaleTime;
	}

	public long getLastSaleTime() {
		return lastSaleTime;
	}

	public void setLastBuTime(long lastBuTime) {
		this.lastBuTime = lastBuTime;
	}

	public long getLastBuTime() {
		return lastBuTime;
	}

	public void setInLast(boolean isInLast) {
		this.isInLast = isInLast;
	}

	public boolean isInLast() {
		return isInLast;
	}
	
	public static HashMap<Integer, ArrayList<Equipment>> playerEquipments = new HashMap<Integer, ArrayList<Equipment>>();
	public Equipment getRandomPlayerEquipment() {
		ArrayList<Equipment> equipments = playerEquipments.get(getRequestType());
		if (equipments == null ) {
			equipments = new ArrayList<Equipment>();
			for (int i = 0 ; i < ArticleManager.getInstance().getAllArticles().length; i++) {
				Article a = ArticleManager.getInstance().getAllArticles()[i];
				if (a instanceof Equipment) {
					Equipment equipment = (Equipment)a;
					int equipmentType = equipment.getEquipmentType();
					if(equipmentType == EquipmentColumn.EQUIPMENT_TYPE_ChiBang){
					}else if (equipmentType <= EquipmentColumn.EQUIPMENT_TYPE_FOR_PLAYER) {
//						if (equipment.getSpecial() == SpecialEquipmentManager.普通) {
//							if (equipment.getPlayerLevelLimit() >= LEVEL_SECTION[getRequestType()][0] && equipment.getPlayerLevelLimit() <= LEVEL_SECTION[getRequestType()][1]) {
//								equipments.add(equipment);
//							}
//						}
					}
				}
			}
			playerEquipments.put(getRequestType(), equipments);
			logger.warn("[人物装备情况] [{}] [{}]", new Object[]{getLogString(), equipments.size()});
		}
		if (equipments.size() == 0) {
			return null;
		}
		int index = new Random().nextInt(equipments.size());
		return equipments.get(index);
	}
	
	public static HashMap<Integer, ArrayList<Equipment>> houseEquipments = new HashMap<Integer, ArrayList<Equipment>>();
	public Equipment getRandomHouseEquipment() {
		ArrayList<Equipment> equipments = houseEquipments.get(getRequestType());
		if (equipments == null ) {
			equipments = new ArrayList<Equipment>();
			for (int i = 0 ; i < ArticleManager.getInstance().getAllArticles().length; i++) {
				Article a = ArticleManager.getInstance().getAllArticles()[i];
				if (a instanceof Equipment) {
					Equipment equipment = (Equipment)a;
					int equipmentType = equipment.getEquipmentType();
					if(equipmentType == EquipmentColumn.EQUIPMENT_TYPE_ChiBang){
					}else if (equipmentType > EquipmentColumn.EQUIPMENT_TYPE_FOR_PLAYER) {
//						if (equipment.getSpecial() == SpecialEquipmentManager.普通) {
//							if (equipment.getPlayerLevelLimit() >= HORSE_SECTION[getRequestType() - REQUEST_TYPE_HORSEEQUIP_1][0] && equipment.getPlayerLevelLimit() <= HORSE_SECTION[getRequestType() - REQUEST_TYPE_HORSEEQUIP_1][1]) {
//								equipments.add(equipment);
//							}
//						}
					}
				}
			}
			houseEquipments.put(getRequestType(), equipments);
			logger.warn("[人物装备情况] [{}] [{}]", new Object[]{getLogString(), equipments.size()});
		}
		
		if (equipments.size() == 0) {
			return null;
		}
		int index = new Random().nextInt(equipments.size());
		return equipments.get(index);
	}
	
	public long getLastHeatTime() {
		return lastHeatTime;
	}

	public void setLastHeatTime(long lastHeatTime) {
		this.lastHeatTime = lastHeatTime;
	}
}
