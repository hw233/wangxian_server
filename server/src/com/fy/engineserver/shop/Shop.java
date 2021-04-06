package com.fy.engineserver.shop;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;
import com.fy.boss.authorize.model.Passport;
import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.activity.chongzhiActivity.LevelPackageActivity;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager;
import com.fy.engineserver.activity.newChongZhiActivity.NewXiaoFeiActivity;
import com.fy.engineserver.activity.shop.ShopActivityManager;
import com.fy.engineserver.activity.vipExpActivity.VipExpActivityManager;
import com.fy.engineserver.articleProtect.ArticleProtectDataValues;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.InlayArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.article.manager.DiscountCardAgent;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.economic.SavingFailedException;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.economic.charge.ChargeManager;
import com.fy.engineserver.economic.charge.ChargeMode;
import com.fy.engineserver.economic.charge.ChargeMoneyConfigure;
import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.enterlimit.EnterLimitManager.PlayerRecordType;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_BuyBindGoodsToShop;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_SellGoodsToShop;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CHARGE_AGRS_RES;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.tengxun.TengXunDataManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.datacheck.MailEventManger;
import com.fy.engineserver.util.datacheck.event.MailEvent;
import com.fy.engineserver.util.datacheck.handler.GoodsPriceLimit;
import com.fy.engineserver.util.datacheck.handler.GoodsPriceLimitManager;
import com.xuanzhi.stat.model.PlayerActionFlow;
import com.xuanzhi.tools.text.DateUtil;

/**
 * 商店
 * 
 * 
 */
public class Shop {

	// static Logger logger = Logger.getLogger(ShopManager.class);
	public static Logger logger = LoggerFactory.getLogger(ShopManager.class);

	public static final byte SHOP_TYPE_JINBI = 0;
	public static final byte SHOP_TYPE_YUANBAO = 1;
	public static final byte SHOP_TYPE_BINbuyGoodsYUANBAO = 2;

	/**
	 * 由于客户端ui限制，只能要求一种商店使用一种货币，商店类型为货币类型
	 * 0 普通商店，普通商店绑定银不够的时候可以用不绑银购买
	 * 1 元宝商店
	 */
	public byte shopType = 0;

	// 商店的唯一id，一旦生成，就不能在修改

	protected int id;

	/**
	 * 是否购买时绑定，如果为true那么购买的物品都绑定，如果为false那么购买物品的绑定状态由物种决定
	 */
	public boolean isBuyBinded;

	/**
	 * 商店的版本，由策划编辑，当策划想在服务器运行过程中上传商店，为了避免玩家买错，需要把版本提高
	 */
	int version;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName_stat() {
		return name_stat;
	}

	public void setName_stat(String name_stat) {
		this.name_stat = name_stat;
	}

	/**
	 * 商店的唯一标识
	 */
	protected String name;
	
	/**
	 * 用于方便统计商店的名称
	 */
	protected String name_stat;

	/**
	 * 商店中的物品
	 */
	protected List<Goods> goods = new ArrayList<Goods>();
	
	/**
	 * 临时物品---暂时保存，用于客户端显示，在固定时间点更新
	 */
	private Map<Long,List<Integer>> tempGoods = new HashMap<Long, List<Integer>>();
	/**
	 * 临时物品上次更新时间s
	 * */
	protected volatile Map<Long, Integer[]> lastUpdataMaps = new Hashtable<Long, Integer[]>();
	/**
	 * 商店中显示的商品数
	 */
//	public int displayNums;
	/**
	 * 商店中物品更新时间
	 */
	public List<Integer[]> refreashTimesList = new ArrayList<Integer[]>();
	
	private Random ran = new Random(System.currentTimeMillis());
	/**
	 * 刷新tempgoods
	 */
	public synchronized void flushTempGoods(Player player) {
		if(refreashTimesList.size() <= 0) {
			getTempGoods().clear();
			return;
		}
		Integer[] lastUpdataGoodsTime = lastUpdataMaps.get(player.getId());
		if (lastUpdataGoodsTime == null) {
			lastUpdataGoodsTime = new Integer[]{0,0,0};
		}
		boolean isRefreash = false;
		Calendar currentCalender = Calendar.getInstance();
		int day = currentCalender.get(Calendar.DAY_OF_YEAR);
		int currentHour = currentCalender.get(Calendar.HOUR_OF_DAY);
		int min = currentCalender.get(Calendar.MINUTE);
		if(getTempGoods().size() <= 0 || tempGoods.get(player.getId()) == null || tempGoods.get(player.getId()).size() <= 0) {
			isRefreash = true;
		} else {
			if (day - lastUpdataGoodsTime[0] > 1 || lastUpdataGoodsTime[0] - day > 1) {
				isRefreash = true;
			}
			if (!isRefreash) {	//
				if(currentHour != lastUpdataGoodsTime[1] || min == lastUpdataGoodsTime[2]) {		//相等就不用再刷新了
					for(int i=0; i<refreashTimesList.size(); i++) {
						if(lastUpdataGoodsTime[0] < day) {
							if(currentHour < refreashTimesList.get(0)[0] || (currentHour == refreashTimesList.get(0)[0] && min >= refreashTimesList.get(0)[1])) {
								isRefreash = true;
								break;
							}
						} else if(lastUpdataGoodsTime[0] == day){
							if(currentHour < refreashTimesList.get(i)[0] || (currentHour == refreashTimesList.get(i)[0] && min < refreashTimesList.get(i)[1])) {
								continue;
							}
							if((refreashTimesList.get(i)[0]  > lastUpdataGoodsTime[1]) || (refreashTimesList.get(i)[0]  == lastUpdataGoodsTime[1] && refreashTimesList.get(i)[1]  > lastUpdataGoodsTime[2])) {
								isRefreash = true;
								break;
							}
						}
					}
				}
			}
		}
		
		if(isRefreash) {
			List<Integer> list = new ArrayList<Integer>();
			ShopManager.logger.warn("[name:"+name+"] 【刷新商品】 [刷新后商品:" + list + "] [" + player.getLogString() + "] [上次更新时间:" + Arrays.toString(lastUpdataGoodsTime) + "]");
//			getTempGoods().clear();
			lastUpdataGoodsTime[0] = day;
			lastUpdataGoodsTime[1] = currentHour;
			lastUpdataGoodsTime[2] = min;
			lastUpdataMaps.put(player.getId(), lastUpdataGoodsTime);
			int tempP = 0;
			ShopManager sm = ShopManager.getInstance();
			for(Goods gs : goods) {
				tempP = ran.nextInt(100);
				if(tempP <= gs.showProbabbly) {
					list.add(gs.getId());
					synchronized (gs) {
						gs.totalSellNum = 0;
						SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(this.id, gs.id, gs.articleName);
						sgd.getSelledPersonGoodsData().remove(player.getId());
						sm.saveSelledGoodsData(this.getId(), gs.id, gs.articleName, sgd);
						if (AchievementManager.logger.isDebugEnabled()) {
							AchievementManager.logger.debug("[商店] [刷新商品] [商店id:" + this.id + "] [商品id:" + gs.id + "] [物品名:" + gs.articleName +
									"] [" + player.getLogString() + "]");
							AchievementManager.logger.debug("[商店] [清除原有记录] [现有记录数量] [" + sgd.getSelledPersonGoodsData() + "]");
						}
					}
				}
			}
			tempGoods.put(player.getId(), list);
			ShopManager.logger.warn("[name:"+name+"] 【刷新商品】 [刷新后商品:" + list + "] [" + player.getLogString() + "] [上次更新时间:" + Arrays.toString(lastUpdataGoodsTime) + "]");
		}
	}
	
	/**
	 * 用户卖给商店的物品，此数据不需要 编辑器编辑
	 * 
	 * @return
	 */
	protected Hashtable<String, ArrayList<SellItem>> sellMap = new Hashtable<String, ArrayList<SellItem>>();
	
	protected boolean isOpenShop;
	protected String timelimits;
//	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
	/**
	 * 该商场是否开放，格式如下
	 * 2011-1-1-11-11###2012-2-2-2-20,2011-3-3-11-11###2012-4-4-2-20
	 * 返回的是开放和即将开放的时间
	 * @return
	 */
	public CompoundReturn isOpen(){
		CompoundReturn cr = CompoundReturn.create();
		try{
			long now = System.currentTimeMillis();
			
			if(!isOpenShop){
				ShopManager.logger.warn("[name:"+name+"] 【还没开放】 [商店是否开放] [配置表里设置的false] ");
				return cr.setBooleanValue(false);
			}
			
			if(timelimits==null || timelimits.trim().length()==0){
				ShopManager.logger.warn("[name:"+name+"] 【开放】 [商店是否开放] [限制开始结束时间为空] ");
				return cr.setBooleanValue(true).setLongValue(0);
			}
			
			String onedaytimes [] = timelimits.split(",");
			//SimpleDateFormat是线程不安全的
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
			for(String onedaytime:onedaytimes){
				long starttime = StringToLong(onedaytime.split("#"),sdf)[0];
				long endtime = StringToLong(onedaytime.split("#"),sdf)[1];
				if(now > endtime){
//					ShopManager.logger.warn("[name:"+name+"] [商店是否开放] [当前时间大于结束时间] [starttime:"+onedaytime.split("#")[0]+"] [endtime:"+onedaytime.split("#")[1]+"] [onedaytimes:"+Arrays.toString(onedaytimes)+"]");
					continue;
				}
				if(now >= starttime && now < endtime){
//					ShopManager.logger.warn("[商店是否开放] 【开放状态】 [start:"+onedaytime.split("#")[0]+"] [end:"+onedaytime.split("#")[1]+"] [剩余时间："+(endtime-now)/1000+"秒]");
					return cr.setBooleanValue(true).setLongValue(0);
				}
				if(now < starttime){
					ShopManager.logger.warn("[商店是否开放] 【还没开放】 [start:"+onedaytime.split("#")[0]+"] [end:"+onedaytime.split("#")[1]+"]  [即将开启时间："+(starttime-now)/1000+"秒]");
					return cr.setBooleanValue(false).setLongValue(starttime);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			ShopManager.logger.warn("[商店是否开放] [异常] [timelimits:"+timelimits+"] ["+this.getName_stat()+"] ["+e+"]");
		}
		return cr.setBooleanValue(false).setLongValue(0);	
	}
	
	private long[] StringToLong(String[] a,SimpleDateFormat sdf) throws ParseException{
		long [] fs = new long[a.length];
		for(int k=0;k<a.length;k++){
			fs[k] = sdf.parse(a[k]).getTime();
		}
		return fs;
	}
	
	
	
	
	
	
	
	
	

	public String getTimelimits() {
		return timelimits;
	}

	public void setTimelimits(String timelimits) {
		this.timelimits = timelimits;
	}

	public boolean isOpenShop() {
		return isOpenShop;
	}

	public void setOpenShop(boolean isOpenShop) {
		this.isOpenShop = isOpenShop;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isBuyBinded() {
		return isBuyBinded;
	}

	public void setBuyBinded(boolean isBuyBinded) {
		this.isBuyBinded = isBuyBinded;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * 得到商店里所有的商品
	 * 
	 * 此方法会刷新商店的数据，包括总量限制和每个人的量限制
	 * 
	 * @return
	 */
	public synchronized Goods[] getGoods(Player player) {

		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		List<Goods> gs = new ArrayList<Goods>();
		for (int i = 0; i < goods.size(); i++) {
			Goods g = goods.get(i);
			flushGoods(now, cal, g, player);
			if (g.playerCansee(player, cal)) {
				gs.add(g);
			}
		}

		return gs.toArray(new Goods[0]);
	}
	/**
	 * 得到商店里所有的商品
	 * 
	 * 此方法会刷新商店的数据，包括总量限制和每个人的量限制
	 * 
	 * @return
	 */
	public synchronized Goods[] gettempGoods(Player player) {
		
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		List<Goods> gs = new ArrayList<Goods>();
		List<Integer> lis = tempGoods.get(player.getId());
		for (int i = 0; i < lis.size(); i++) {
			Goods g = null;
			for (Goods gss : goods) {
				if (gss.getId() == lis.get(i)) {
					g = gss;
					break;
				}
			}
			if (g == null) {
				logger.warn("[商店] [" + this.getName_stat() + "] [刷新出来的物品不存在，id : " + lis.get(i) + "] [" + player.getLogString() + "]");
				continue;
			}
			flushGoods(now, cal, g, player);
			if (g.playerCansee(player, cal)) {
				gs.add(g);
			}
		}
		
		return gs.toArray(new Goods[0]);
	}

	public void flushGoods(long now, Calendar cal, Goods g, Player player) {
		ShopManager sm = ShopManager.getInstance();

		if (g.在时间限制下的数量限制) {
			if (g.timeLimitType == ShopManager.LIMIT_EVERYDAY_HOURS) {
				if (g.totalSellNumLimit > ShopManager.NUM_LIMIT_NONE) {

					cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
					cal.setTimeInMillis(g.getTotalSellNumLastFlushTime());
					int sellDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
					if (dayOfYear != sellDayOfYear) {
						g.totalSellNum = 0;
						g.totalSellNumLastFlushTime = now;
						g.dirty = true;
						if (logger.isInfoEnabled()) {
							if (logger.isInfoEnabled()) logger.info("[商品刷新] [{}] [{}] [{}] [{}] [LIMIT_EVERYDAY_HOURS] [刷新总购买个数] [{}] [Player：{}] [{}] [{}] [{}]", new Object[] { this.getLogString(), this.getId(), g.getArticleName(), g.getId(), g.totalSellNumLimit, player.getId(), player.getName(), player.getUsername(), (DateUtil.formatDate(new Date(now), "yyyy-MM-dd HH:mm:ss")) });
						}
					}
					if (g.personSellNumLimit > ShopManager.NUM_LIMIT_NONE) {

						SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(id, g.id, g.getArticleName());
						cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
						dayOfYear = cal.get(Calendar.DAY_OF_YEAR);

						cal.setTimeInMillis(sgd.getLastFlushTime(player.getId()));

						sellDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
						if (dayOfYear != sellDayOfYear) {

							sgd.clearSellData(player.getId());
							sm.saveSelledGoodsData(id, g.id, g.getArticleName(), sgd);

							g.dirty = true;
							if (logger.isInfoEnabled()) {
								if (logger.isInfoEnabled()) logger.info("[商品刷新] [{}] [{}] [{}] [{}] [LIMIT_EVERYDAY_HOURS] [刷新单人购买个数] [{}] [Player：{}] [{}] [{}] [{}]", new Object[] { this.getLogString(), this.getId(), g.getArticleName(), g.getId(), g.personSellNumLimit, player.getId(), player.getName(), player.getUsername(), (DateUtil.formatDate(new Date(now), "yyyy-MM-dd HH:mm:ss")) });
							}
						}
					}
				} else {
					if (g.personSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
						cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
						int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);

						SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(id, g.id, g.getArticleName());
						cal.setTimeInMillis(sgd.getLastFlushTime(player.getId()));
						int sellDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
						if (dayOfYear != sellDayOfYear) {
							sgd.clearSellData(player.getId());
							sm.saveSelledGoodsData(id, g.id, g.getArticleName(), sgd);

							g.dirty = true;
							if (logger.isInfoEnabled()) {
								if (logger.isInfoEnabled()) logger.info("[商品刷新] [{}] [{}] [{}] [{}] [LIMIT_EVERYDAY_HOURS] [刷新单人购买个数] [{}] [Player：{}] [{}] [{}] [{}]", new Object[] { this.getLogString(), this.getId(), g.getArticleName(), g.getId(), g.personSellNumLimit, player.getId(), player.getName(), player.getUsername(), (DateUtil.formatDate(new Date(now), "yyyy-MM-dd HH:mm:ss")) });
							}
						}
					}
				}
			}
			if (g.timeLimitType == ShopManager.LIMIT_EVERYWEEK_DAYS) {
				if (g.totalSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
					cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
					cal.setTimeInMillis(g.getTotalSellNumLastFlushTime());
					int sellWeekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
					if (weekOfYear != sellWeekOfYear) {
						g.totalSellNum = 0;
						g.totalSellNumLastFlushTime = now;
						g.dirty = true;
						if (logger.isInfoEnabled()) {
							if (logger.isInfoEnabled()) logger.info("[商品刷新] [{}] [{}] [{}] [{}] [LIMIT_EVERYWEEK_DAYS] [刷新总购买个数] [{}] [Player：{}] [{}] [{}] [{}]", new Object[] { this.getLogString(), this.getId(), g.getArticleName(), g.getId(), g.totalSellNumLimit, player.getId(), player.getName(), player.getUsername(), (DateUtil.formatDate(new Date(now), "yyyy-MM-dd HH:mm:ss")) });
						}
					}
					if (g.personSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
						cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
						weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
						SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(id, g.id, g.getArticleName());
						cal.setTimeInMillis(sgd.getLastFlushTime(player.getId()));
						sellWeekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
						if (weekOfYear != sellWeekOfYear) {
							sgd.clearSellData(player.getId());
							sm.saveSelledGoodsData(id, g.id, g.getArticleName(), sgd);

							g.dirty = true;
							if (logger.isInfoEnabled()) {
								if (logger.isInfoEnabled()) logger.info("[商品刷新] [{}] [{}] [{}] [{}] [LIMIT_EVERYWEEK_DAYS] [刷新单人购买个数] [{}] [Player：{}] [{}] [{}] [{}]", new Object[] { this.getLogString(), this.getId(), g.getArticleName(), g.getId(), g.personSellNumLimit, player.getId(), player.getName(), player.getUsername(), (DateUtil.formatDate(new Date(now), "yyyy-MM-dd HH:mm:ss")) });
							}
						}
					}
				} else {
					if (g.personSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
						cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
						int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
						SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(id, g.id, g.getArticleName());
						cal.setTimeInMillis(sgd.getLastFlushTime(player.getId()));
						int sellWeekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
						if (weekOfYear != sellWeekOfYear) {
							sgd.clearSellData(player.getId());
							sm.saveSelledGoodsData(id, g.id, g.getArticleName(), sgd);

							g.dirty = true;
							if (logger.isInfoEnabled()) {
								if (logger.isInfoEnabled()) logger.info("[商品刷新] [{}] [{}] [{}] [{}] [LIMIT_EVERYWEEK_DAYS] [刷新单人购买个数] [{}] [Player：{}] [{}] [{}] [{}]", new Object[] { this.getLogString(), this.getId(), g.getArticleName(), g.getId(), g.personSellNumLimit, player.getId(), player.getName(), player.getUsername(), (DateUtil.formatDate(new Date(now), "yyyy-MM-dd HH:mm:ss")) });
							}
						}
					}
				}
			}
			if (g.timeLimitType == ShopManager.LIMIT_EVERYMONTH_DAYS) {
				if (g.totalSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
					cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					int monthOfYear = cal.get(Calendar.MONTH);
					cal.setTimeInMillis(g.getTotalSellNumLastFlushTime());
					int sellMonthOfYear = cal.get(Calendar.MONTH);
					if (monthOfYear != sellMonthOfYear) {
						g.totalSellNum = 0;
						g.totalSellNumLastFlushTime = now;
						g.dirty = true;
						if (logger.isInfoEnabled()) {
							if (logger.isInfoEnabled()) logger.info("[商品刷新] [{}] [{}] [{}] [{}] [LIMIT_EVERYMONTH_DAYS] [刷新总购买个数] [{}] [Player：{}] [{}] [{}] [{}]", new Object[] { this.getLogString(), this.getId(), g.getArticleName(), g.getId(), g.totalSellNumLimit, player.getId(), player.getName(), player.getUsername(), (DateUtil.formatDate(new Date(now), "yyyy-MM-dd HH:mm:ss")) });
						}
					}
					if (g.personSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
						cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
						monthOfYear = cal.get(Calendar.MONTH);
						SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(id, g.id, g.getArticleName());
						cal.setTimeInMillis(sgd.getLastFlushTime(player.getId()));
						sellMonthOfYear = cal.get(Calendar.MONTH);
						if (monthOfYear != sellMonthOfYear) {
							sgd.clearSellData(player.getId());
							sm.saveSelledGoodsData(id, g.id, g.getArticleName(), sgd);

							g.dirty = true;
							if (logger.isInfoEnabled()) {
								if (logger.isInfoEnabled()) logger.info("[商品刷新] [{}] [{}] [{}] [{}] [LIMIT_EVERYMONTH_DAYS] [刷新单人购买个数] [{}] [Player：{}] [{}] [{}] [{}]", new Object[] { this.getLogString(), this.getId(), g.getArticleName(), g.getId(), g.personSellNumLimit, player.getId(), player.getName(), player.getUsername(), (DateUtil.formatDate(new Date(now), "yyyy-MM-dd HH:mm:ss")) });
							}
						}
					}
				} else {
					if (g.personSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
						cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
						int monthOfYear = cal.get(Calendar.MONTH);
						SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(id, g.id, g.getArticleName());
						cal.setTimeInMillis(sgd.getLastFlushTime(player.getId()));
						int sellMonthOfYear = cal.get(Calendar.MONTH);
						if (monthOfYear != sellMonthOfYear) {
							sgd.clearSellData(player.getId());
							sm.saveSelledGoodsData(id, g.id, g.getArticleName(), sgd);

							g.dirty = true;
							if (logger.isInfoEnabled()) {
								if (logger.isInfoEnabled()) logger.info("[商品刷新] [{}] [{}] [{}] [{}] [LIMIT_EVERYMONTH_DAYS``] [刷新单人购买个数] [{}] [Player：{}] [{}] [{}] [{}]", new Object[] { this.getLogString(), this.getId(), g.getArticleName(), g.getId(), g.personSellNumLimit, player.getId(), player.getName(), player.getUsername(), (DateUtil.formatDate(new Date(now), "yyyy-MM-dd HH:mm:ss")) });
							}
						}
					}
				}
			}
			//TODO 时间段限购
			if (g.timeLimitType == ShopManager.LIMIT_FIX_TIME) {
				if (g.totalSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
					long fixTimeEndLimit = g.getFixTimeEndLimit();
					long fixTimeBeginLimit = g.getFixTimeBeginLimit();
					if(fixTimeEndLimit >= now && now >= fixTimeBeginLimit){
						SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(id, g.id, g.getArticleName());
						long starttime = sgd.getConfigStartTime();
						long endtime = sgd.getConfigEndTime();
						if((starttime/1000) != (fixTimeBeginLimit/1000) || (endtime/1000) != (fixTimeEndLimit/1000)){
							g.totalSellNum = 0;
							g.totalSellNumLastFlushTime = now;
							g.dirty = true;
							if (logger.isInfoEnabled()) {
								if (logger.isInfoEnabled()) logger.info("[商品刷新] [{}] [{}] [{}] [{}] [LIMIT_FIX_TIME] [刷新总购买个数] [{}] [Player：{}] [{}] [{}] [{}]  [starttime:{}] [fixTimeBeginLimit:{}] [endtime:{}] [fixTimeEndLimit:{}]", new Object[] { this.getLogString(), this.getId(), g.getArticleName(), g.getId(), g.totalSellNumLimit, player.getId(), player.getName(), player.getUsername(), (DateUtil.formatDate(new Date(now), "yyyy-MM-dd HH:mm:ss")) ,starttime,fixTimeBeginLimit,endtime,fixTimeEndLimit});
							}
						}
					}
					if (g.personSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
						if(fixTimeEndLimit >= now && now >= fixTimeBeginLimit){
							SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(id, g.id, g.getArticleName());
							if((sgd.getConfigStartTime()/1000) != (fixTimeBeginLimit/1000) || (sgd.getConfigEndTime()/1000) != (fixTimeEndLimit/1000)){
								sgd.clearSellData(player.getId());
								sm.saveSelledGoodsData(id, g.id, g.getArticleName(), sgd);
	
								g.dirty = true;
								if (logger.isInfoEnabled()) {
									if (logger.isInfoEnabled()) logger.info("[商品刷新] [{}] [{}] [{}] [{}] [LIMIT_FIX_TIME--] [刷新单人购买个数] [{}] [Player：{}] [{}] [{}] [{}]", new Object[] { this.getLogString(), this.getId(), g.getArticleName(), g.getId(), g.personSellNumLimit, player.getId(), player.getName(), player.getUsername(), (DateUtil.formatDate(new Date(now), "yyyy-MM-dd HH:mm:ss")) });
								}
							}
						}
					}
				} else {
					if (g.personSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
						long fixTimeEndLimit = g.getFixTimeEndLimit();
						long fixTimeBeginLimit = g.getFixTimeBeginLimit();
						if(fixTimeEndLimit >= now && now >= fixTimeBeginLimit){
							SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(id, g.id, g.getArticleName());
							if((sgd.getConfigStartTime(player.getId())/1000) != (fixTimeBeginLimit/1000) || (sgd.getConfigEndTime(player.getId())/1000) != (fixTimeEndLimit/1000)){
								sgd.clearSellData(player.getId());
								sm.saveSelledGoodsData(id, g.id, g.getArticleName(), sgd);
								g.dirty = true;
								if (logger.isInfoEnabled()) {
									if (logger.isInfoEnabled()) logger.info("[商品刷新] [时间段刷新2] [配置的开始:{}] [存储的开始:{}] [配置的结束:{}] [存储的结束:{}] [{}] [{}] [{}] [{}] [LIMIT_EVERYMONTH_DAYS``] [刷新单人购买个数] [{}] [Player：{}] [{}] [{}] [{}] [{}]", new Object[] {(DateUtil.formatDate(new Date(fixTimeBeginLimit), "yyyy-MM-dd HH:mm:ss")),(DateUtil.formatDate(new Date(sgd.getConfigStartTime(player.getId())), "yyyy-MM-dd HH:mm:ss")),(DateUtil.formatDate(new Date(fixTimeEndLimit), "yyyy-MM-dd HH:mm:ss")),(DateUtil.formatDate(new Date(sgd.getConfigEndTime(player.getId())), "yyyy-MM-dd HH:mm:ss")), this.getLogString(), this.getId(), g.getArticleName(), g.getId(), g.personSellNumLimit, player.getId(), player.getName(), player.getUsername(), (DateUtil.formatDate(new Date(now), "yyyy-MM-dd HH:mm:ss")),(sgd.getSelledNum(player.getId()) +"-->" +g.personSellNumLimit) });
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 玩家购买商品，此方法处理商品购买，购买的商品自动
	 * 放入到玩家的背包中，并且扣除玩家的钱或者物品，
	 * 成功或者失败都通知玩家
	 * 
	 * @param player
	 * @param goodsId
	 *            商品在商店中的索引
	 * @param amount
	 *            购买多少个
	 * @param moneyType
	 *            这个只有在绿色服的时候有用，是用来看用户是选择用哪种货币购买的 0是银子 1是非流通银子
	 */
	public boolean buyGoods(Player player, int goodsId, int amount, int moneyType) {
		return buyGoods(player, goodsId, amount, moneyType, 1);
	}

	/**
	 * 玩家购买商品，此方法处理商品购买，购买的商品自动
	 * 放入到玩家的背包中，并且扣除玩家的钱或者物品，
	 * 成功或者失败都通知玩家
	 * 
	 * @param player
	 * @param goodsId
	 *            商品在商店中的索引
	 * @param amount
	 *            购买多少个
	 * @param moneyType
	 *            这个只有在绿色服的时候有用，是用来看用户是选择用哪种货币购买的 0是银子 1是非流通银子
	 * @param discountRate
	 *            折扣
	 */
	public boolean buyGoods(Player player, int goodsId, int amount, int moneyType, double discountRate) {
		
		ShopManager sm = ShopManager.getInstance();
		Goods g = getGoodsById(goodsId);
		if (g == null) {
			if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [失败] [无法找到商品] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:--] [购买数量:{}]", new Object[] { player.getUsername(), player.getName(), name, goodsId, amount });
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_5625);
			player.addMessageToRightBag(req);
			return false;
		}
		if(getTempGoods() != null && getTempGoods().size() > 0) {
			this.flushTempGoods(player);
			List<Integer> list = tempGoods.get(player.getId());
			boolean existFlag = false;
			for(Integer tgs : list) {
				if(tgs == goodsId) {
					existFlag = true;
					break;
				}
			}
			if(!existFlag) {
				if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [失败] [商品已经刷新] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:--] [购买数量:{}]", new Object[] { player.getUsername(), player.getName(), name, goodsId, amount });
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.商品刷新);
				player.addMessageToRightBag(req);
				return false;
			}
		}
		
		//由于客户端问题，做绑定处理
		boolean isshowbind = false;
		String[] 新商城拥有的商店名字_VIP = ShopManager.新商城拥有的商店名字_VIP;
		for(String shopname : 新商城拥有的商店名字_VIP){
			if(name.equals(shopname)){
				isshowbind = true;
				break;
			}
		}
		
		if(!isshowbind){
			if(name.contains("VIP") && name.contains("商店") && name.contains("限时抢购")){
				isshowbind = true;
			}
		}
		
//		logger.warn("[用户购买商品] [name:"+name+"] [gname:"+g.getArticleName()+"] [isshowbind:"+isshowbind+"] ["+player.getName()+"] [amount:"+amount+"] [moneyType"+moneyType+"] [g:"+g.getPayType()+"]");
		if(g.isBuyBind() && isshowbind){
			WindowManager wm = WindowManager.getInstance();
			MenuWindow mw = wm.createTempMenuWindow(180);
			mw.setDescriptionInUUB(Translate.是否购买绑定商品);

			Option_BuyBindGoodsToShop op1 = new Option_BuyBindGoodsToShop();
			op1.setShop(this);
			op1.setPlayer(player);
			op1.setGoodsId(goodsId);
			op1.setAmount(amount);
			op1.setMoneyType(moneyType);
			op1.setDiscountRate(discountRate);
			op1.setText(Translate.text_62);

			Option_Cancel op2 = new Option_Cancel();
			op2.setText(Translate.text_364);
			Option[] options = new Option[] { op1, op2 };
			mw.setOptions(options);

			CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
			player.addMessageToRightBag(req);
			
			return true;
		}else{
			if(g.goodMaxNumLimit > 0 && amount > g.goodMaxNumLimit){
				if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [失败] [购买数量超过了物品设置的最大上限] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:--] [购买数量:{}]", new Object[] { player.getUsername(), player.getName(), name, goodsId, amount });

				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_5627);
				player.addMessageToRightBag(req);
				return false;
			}
			
			if (amount < 1 || amount > 100) {
				if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [失败] [输入数量有错] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:--] [购买数量:{}]", new Object[] { player.getUsername(), player.getName(), name, goodsId, amount });

				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_5626);
				player.addMessageToRightBag(req);
				return false;
			}

			ArticleManager articleManager = ArticleManager.getInstance();
			ArticleEntityManager articleEntityManager = ArticleEntityManager.getInstance();

			Article article = articleManager.getArticle(g.articleName);

			if (article == null) {
				if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [失败] [无法找到商品对应的物品] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });

				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_5628 + g.articleName + Translate.text_5629);
				player.addMessageToRightBag(req);
				return false;
			}
//			logger.warn("[用户购买商品] ["+player.getName()+"] [gname:"+g.getArticleName()+"] [全服限购："+g.getTotalSellNumLimit()+"] [个人限购："+g.getPersonSellNumLimit()+"] [剩余数量："+g.getOverNum()+"] [支付类型："+g.getPayType()+"] [===========]");
			if (g.isReputeLimit()) {
			}
			
			synchronized (g) {

				long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				Calendar cal = Calendar.getInstance();
				logger.warn("[用户购买商品] ["+player.getName()+"] [gname:"+g.getArticleName()+"] [总共卖出："+g.totalSellNum+"] [在时间限制下的数量限制:"+g.在时间限制下的数量限制+"] [全服限购："+g.totalSellNumLimit+"] [时间限制类型:"+g.timeLimitType+"] [个人限购："+g.getPersonSellNumLimit()+"] [剩余数量："+g.getOverNum()+"] [支付类型："+g.getPayType()+"] [===========]");
				if (g.totalSellNumLimit > ShopManager.NUM_LIMIT_NONE || g.timeLimitType != ShopManager.TIME_LIMIT_NONE || g.personSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
					flushGoods(now, cal, g, player);
					if(g.totalSellNumLimit > 0 && g.totalSellNumLimit - g.totalSellNum >= 0){
						g.overNum = (int)(g.totalSellNumLimit - g.totalSellNum);
					}
					
					if (g.在时间限制下的数量限制) {
						if (g.timeLimitType == ShopManager.TIME_LIMIT_NONE) {
							if (g.totalSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
								if (g.totalSellNum >= g.totalSellNumLimit) {
									String description = Translate.商品已经售完;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
									return false;
								} else if (g.totalSellNum + amount > g.totalSellNumLimit) {
									String description = Translate.购买数量超过限制;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
									return false;
								}
								if (g.personSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
									SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(id, g.id, g.getArticleName());
									long sellNum = sgd.getSelledNum(player.getId());

									if (sellNum >= g.personSellNumLimit) {
										String description = Translate.您不能再购买了;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
										return false;
									} else if (sellNum + amount > g.personSellNumLimit) {
										String description = Translate.购买数量超过限制;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
										return false;
									}

								}
							} else {
								if (g.personSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
									SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(id, g.id, g.getArticleName());
									long sellNum = sgd.getSelledNum(player.getId());

									if (sellNum >= g.personSellNumLimit) {
										String description = Translate.您不能再购买了;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
										return false;
									} else if (sellNum + amount > g.personSellNumLimit) {
										String description = Translate.购买数量超过限制;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
										return false;
									}

								}
							}
						} else {
							if (g.timeLimitType == ShopManager.LIMIT_EVERYDAY_HOURS) {
								cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
								int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
								if (hourOfDay < g.everyDayBeginLimit || hourOfDay > g.everyDayEndLimit) {
									String description = Translate.购买时间还没到;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
									return false;
								}
							}
							if (g.timeLimitType == ShopManager.LIMIT_EVERYWEEK_DAYS) {
								cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
								int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
								int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
								int hourOfWeek = dayOfWeek * 24 + hourOfDay;
								if (hourOfWeek < g.everyWeekBeginLimit || hourOfWeek > g.everyWeekEndLimit) {
									String description = Translate.购买时间还没到;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
									return false;
								}
							}
							if (g.timeLimitType == ShopManager.LIMIT_EVERYMONTH_DAYS) {
								cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
								int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
								if (dayOfMonth < g.everyMonthBeginLimit || dayOfMonth > g.everyMonthEndLimit) {
									String description = Translate.购买时间还没到;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
									return false;
								}
							}
							if (g.timeLimitType == ShopManager.LIMIT_FIX_TIME) {
								if (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() < g.fixTimeBeginLimit || com.fy.engineserver.gametime.SystemTime.currentTimeMillis() > g.fixTimeEndLimit) {
									String description = Translate.购买时间还没到;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
									return false;
								}
							}

							if (g.totalSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
								if (g.totalSellNum >= g.totalSellNumLimit) {
									String description = Translate.商品已经售完;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
									return false;
								} else if (g.totalSellNum + amount > g.totalSellNumLimit) {
									String description = Translate.购买数量超过限制;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
									return false;
								}
								if (g.personSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
									SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(id, g.id, g.getArticleName());
									long sellNum = sgd.getSelledNum(player.getId());
									if (sellNum >= g.personSellNumLimit) {
										String description = Translate.您不能再购买了;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
										return false;
									} else if (sellNum + amount > g.personSellNumLimit) {
										String description = Translate.购买数量超过限制;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
										return false;
									}
								}
							} else {
								if (g.personSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
									SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(id, g.id, g.getArticleName());
									long sellNum = sgd.getSelledNum(player.getId());

									if (sellNum >= g.personSellNumLimit) {
										String description = Translate.您不能再购买了;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
										return false;
									} else if (sellNum + amount > g.personSellNumLimit) {
										String description = Translate.购买数量超过限制;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
										return false;
									}
								}
							}
						}
					} else {
						if (g.timeLimitType != ShopManager.TIME_LIMIT_NONE) {
							if (g.timeLimitType == ShopManager.LIMIT_EVERYDAY_HOURS) {
								cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
								int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
								if (hourOfDay < g.everyDayBeginLimit || hourOfDay > g.everyDayEndLimit) {
									String description = Translate.购买时间还没到;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
									return false;
								}
							}
							if (g.timeLimitType == ShopManager.LIMIT_EVERYWEEK_DAYS) {
								cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
								int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
								int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
								int hourOfWeek = dayOfWeek * 24 + hourOfDay;
								if (hourOfWeek < g.everyWeekBeginLimit || hourOfWeek > g.everyWeekEndLimit) {
									String description = Translate.购买时间还没到;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
									return false;
								}
							}
							if (g.timeLimitType == ShopManager.LIMIT_EVERYMONTH_DAYS) {
								cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
								int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
								if (dayOfMonth < g.everyMonthBeginLimit || dayOfMonth > g.everyMonthEndLimit) {
									String description = Translate.购买时间还没到;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
									return false;
								}
							}
							if (g.timeLimitType == ShopManager.LIMIT_FIX_TIME) {
								if (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() < g.fixTimeBeginLimit || com.fy.engineserver.gametime.SystemTime.currentTimeMillis() > g.fixTimeEndLimit) {
									String description = Translate.购买时间还没到;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
									return false;
								}
							}
							if (g.totalSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
								if (g.totalSellNum >= g.totalSellNumLimit) {
									String description = Translate.商品已经售完;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
									return false;
								} else if (g.totalSellNum + amount > g.totalSellNumLimit) {
									String description = Translate.购买数量超过限制;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
									return false;
								}
								if (g.personSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
									SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(id, g.id, g.getArticleName());
									long sellNum = sgd.getSelledNum(player.getId());

									if (sellNum >= g.personSellNumLimit) {
										String description = Translate.您不能再购买了;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
										return false;
									} else if (sellNum + amount > g.personSellNumLimit) {
										String description = Translate.购买数量超过限制;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
										return false;
									}

								}
							} else {
								if (g.personSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
									SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(id, g.id, g.getArticleName());
									long sellNum = sgd.getSelledNum(player.getId());

									if (sellNum >= g.personSellNumLimit) {
										String description = Translate.您不能再购买了;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
										return false;
									} else if (sellNum + amount > g.personSellNumLimit) {
										String description = Translate.购买数量超过限制;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });
										return false;
									}

								}
							}
						}
					}
				}
//				logger.warn("[用户购买商品] ["+player.getName()+"] [gname:"+g.getArticleName()+"] [总共卖出："+g.totalSellNum+"] [在时间限制下的数量限制:"+g.在时间限制下的数量限制+"] [全服限购："+g.totalSellNumLimit+"] [时间限制类型:"+g.timeLimitType+"] [个人限购："+g.getPersonSellNumLimit()+"] [剩余数量："+g.getOverNum()+"] [支付类型："+g.getPayType()+"] [===========2]");
				List<ArticleEntity> elist = new ArrayList<ArticleEntity>();
				String chargeValue = article.getName_stat()+"####"+article.getColorType()+"####"+amount * g.bundleNum;
				if (article.isOverlap()) {
					ArticleEntity entity = null;
					try {
						boolean bind = false;
						if (moneyType == 1) {
							bind = true;
						}
						entity = articleEntityManager.createEntity(article, isBuyBinded || bind, ArticleEntityManager.CREATE_REASON_SHOP_SELL, player, g.getColor(), amount * g.bundleNum, false);
						for (int i = 0; i < amount * g.bundleNum; i++) {
							elist.add(entity);
						}
					} catch (Exception ex) {
						if (logger.isErrorEnabled()) logger.error("[用户购买商品] [异常] [用户:" + player.getLogString() + "] [" + name + "] [" + g.articleName + "] [一捆数量:" + g.articleName + "] [购买数量:" + amount + "]", ex);
						HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.服务器物品出现错误);
						player.addMessageToRightBag(req);
						return false;
					}
				} else {
					for (int i = 0; i < amount; i++) {
						for (int j = 0; j < g.bundleNum; j++) {
							try {
								ArticleEntity entity = null;
								// if(article instanceof Equipment){
								// Equipment e = (Equipment)article;
								// if(SpecialEquipmentManager.getInstance().isSpecialEquipment(e)){
								// entity = SpecialEquipmentManager.getInstance().createEntity(e,ArticleEntityManager.CREATE_REASON_SHOP_SELL);
								// SpecialEquipmentManager.logger.warn("[购买了混沌万灵榜装备] ["+player.getLogString()+"] ["+e.getName()+"] [entity:"+(entity != null ? entity.getId() :
								// null)+"]");
								// }else{
								// entity = articleEntityManager.createEntity(article, isBuyBinded, ArticleEntityManager.CREATE_REASON_SHOP_SELL, player, g.getColor(), 1, false);
								// }
								// }else{
								boolean bind = false;
								if (moneyType == 1) {
									bind = true;
								}
								entity = articleEntityManager.createEntity(article, isBuyBinded || bind, ArticleEntityManager.CREATE_REASON_SHOP_SELL, player, g.getColor(), 1, false);
								// }
								if (entity != null) {
									elist.add(entity);
								}
							} catch (Exception ex) {
								if (logger.isErrorEnabled()) logger.error("[用户购买商品] [异常] [用户:" + player.getLogString() + "] [" + name + "] [" + g.articleName + "] [一捆数量:" + g.articleName + "] [购买数量:" + amount + "]", ex);
								HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.服务器物品出现错误);
								player.addMessageToRightBag(req);
								return false;
							}
						}
					}
				}

				if (elist.size() <= 0) {
					if (logger.isErrorEnabled()) logger.error("[用户购买商品] [异常，生成物品为空] [用户:" + player.getLogString() + "] [" + name + "] [" + g.articleName + "] [一捆数量:" + g.articleName + "] [购买数量:" + amount + "]");
					HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.服务器物品出现错误);
					player.addMessageToRightBag(req);
					return false;
				}

				// 测试用户的背包是否能放下物品
				if (!player.putAllOK(elist.toArray(new ArticleEntity[0]))) {
					if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [失败] [背包太满] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount });

//					SHOP_BUY_RES res = new SHOP_BUY_RES(GameMessageFactory.nextSequnceNum(), getName(), goodsId, g.getDescription(player));
//					player.addMessageToRightBag(res);

					HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.背包空间不足);
					player.addMessageToRightBag(req);
					for (ArticleEntity ae : elist) {
						articleEntityManager.removeTransientATable(ae);
					}
					return false;
				}
				
				//在扣费给商品前判断有无打折卡
				if(discountRate >0 && discountRate <1){
					boolean havecard = DiscountCardAgent.getInstance().checkHaveDiscountCard(player, g.articleName, amount * g.bundleNum, this.name,g.getColor());
					if(!havecard){
						return false;
					}
				}

				BillingCenter economicCenter = BillingCenter.getInstance();
				int currencyType = CurrencyType.BIND_YINZI;
				long expense = 0;
				long price = 0;
				// 扣费
				try {
					if (g.getPayType() == CurrencyType.BIND_YINZI) {
						currencyType = CurrencyType.BIND_YINZI;
						price = g.price / g.bundleNum;
						long moneyPrice = g.price;

						expense = (long) (moneyPrice * amount * discountRate);
						if (player.getMedicineDiscount() > 0 && "角色药品".equals(article.get物品一级分类_stat())) {
							float tempF = player.getMedicineDiscount() / 100f;
							long tempDiscount = (long) (expense * tempF);
							if (tempDiscount > 0 && tempDiscount < expense) {
								expense -= tempDiscount;
							}
						}
						// 国战特殊处理
						if (player.isIsGuozhan() && player.getTodayCanUseBindSilver() < expense) {
							HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.国战状态下绑银不足或已达今日使用上限购买失败);
							player.addMessageToRightBag(req);
							return false;
						}

						if (!player.bindSilverEnough(expense)) {
							// BillingCenter.银子不足时弹出充值确认框(player, "绑银不足，是否去充值");
							HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.绑银和银子都不足购买失败);
							player.addMessageToRightBag(req);
							return false;
						}

						economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "");
					} else if (g.getPayType() == CurrencyType.YINZI) {
						if (moneyType == 0) {
							currencyType = CurrencyType.YINZI;
							price = g.price / g.bundleNum;
							int yprice = g.price;
							expense = (long) (yprice * amount * discountRate);
							if (shopType == Shop.SHOP_TYPE_YUANBAO) {
//								if (g.price == g.oldPrice) {
									expense = (long) (expense * TengXunDataManager.instance.getShopAgio2Buy(player));
//								}
							}

							if (player.getSilver() < expense) {
								BillingCenter.银子不足时弹出充值确认框(player, Translate.银子不足是否去充值);
								return false;
							}
							
							economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "", VipExpActivityManager.shop_expends_activity);
							
							if (shopType == Shop.SHOP_TYPE_YUANBAO) {
								NewChongZhiActivityManager.instance.doXiaoFeiJiFenActivity(player, expense);
								NewChongZhiActivityManager.instance.doXiaoFeiActivity(player, expense, NewXiaoFeiActivity.XIAOFEI_TYPE_SHOP);
//								ChongZhiActivity.getInstance().doXiaoFeiActivity(player, expense, XiaoFeiServerConfig.XIAOFEI_TONGDAO_SHANGCHENG);
//								ChongZhiActivity.getInstance().doXiaoFeiMoneyActivity(player, expense, MoneyBillBoardActivity.XIAOFEI_TONGDAO_SHANGCHENG);
							}
							// 活跃度统计
							ActivenessManager.getInstance().record(player, ActivenessType.商城购买道具);
//							CostBillboardManager.getInstance().addCostRecord(player, CostType.商城消费, expense);
						} else if (moneyType == 1) {
							currencyType = CurrencyType.SHOPYINZI;
							price = g.price / g.bundleNum;
							long yprice = g.price;
							expense = (long) (yprice * amount * discountRate);
							if (shopType == Shop.SHOP_TYPE_YUANBAO) {
//								if (g.price == g.oldPrice) {
									expense = (long) (expense * TengXunDataManager.instance.getShopAgio2Buy(player));
//								}
							}
							if (player.getShopSilver() + player.getSilver() < expense) {
								BillingCenter.银子不足时弹出充值确认框(player, Translate.银子不足是否去充值);
								return false;
							}
							if (shopType == Shop.SHOP_TYPE_YUANBAO) {
								NewChongZhiActivityManager.instance.doXiaoFeiActivity(player, expense, NewXiaoFeiActivity.XIAOFEI_TYPE_SHOP);
//								ChongZhiActivity.getInstance().doXiaoFeiActivity(player, expense, XiaoFeiServerConfig.XIAOFEI_TONGDAO_SHANGCHENG);
//								ChongZhiActivity.getInstance().doXiaoFeiMoneyActivity(player, expense, MoneyBillBoardActivity.XIAOFEI_TONGDAO_SHANGCHENG);
							}
							economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "");
						}
					} else if (g.getPayType() == CurrencyType.GONGZI) {
						currencyType = CurrencyType.GONGZI;
						price = g.price / g.bundleNum;
						long yprice = g.price;
						expense = (long) (yprice * amount * discountRate);
						economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "");
					} else if (g.getPayType() == CurrencyType.ZIYUAN) {
						currencyType = CurrencyType.ZIYUAN;
						price = g.price / g.bundleNum;
						long yprice = g.price;
						expense = (long) (yprice * amount * discountRate);
						economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "");
					} else if (g.getPayType() == CurrencyType.BANGPAIZIJIN) {
						currencyType = CurrencyType.BANGPAIZIJIN;
						price = g.price / g.bundleNum;
						long yprice = g.price;
						expense = (long) (yprice * amount * discountRate);
						economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "");
					} else if (g.getPayType() == CurrencyType.LILIAN) {
						currencyType = CurrencyType.LILIAN;
						price = g.price / g.bundleNum;
						long yprice = g.price;
						expense = (long) (yprice * amount * discountRate);
						economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "");
					} else if (g.getPayType() == CurrencyType.GONGXUN) {
						currencyType = CurrencyType.GONGXUN;
						price = g.price / g.bundleNum;
						long yprice = g.price;
						expense = (long) (yprice * amount * discountRate);
						economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "");
					} else if (g.getPayType() == CurrencyType.WENCAI) {
						currencyType = CurrencyType.WENCAI;
						price = g.price / g.bundleNum;
						long yprice = g.price;
						expense = (long) (yprice * amount * discountRate);
						economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "");
					} else if (g.getPayType() == CurrencyType.ARTICLES) {
						synchronized (player.tradeKey) {
							for (int i = 0; i < g.exchangeArticleNames.length; i++) {
								int c = player.countArticleInKnapsacksByName(g.exchangeArticleNames[i]);
								if (c < g.exchangeArticleNums[i] * amount) {
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.物品个数不足);
									player.addMessageToRightBag(hreq);
									logger.warn("[用户购买商品] [失败] [个数不足] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [" + name + "] [" + g.articleName + "] [一捆数量:" + g.bundleNum + "] [购买数量:" + amount + "] [c:" + c + "]");
									return false;
								}
							}
	
							for (int i = 0; i < g.exchangeArticleNames.length; i++) {
								int n = g.exchangeArticleNums[i] * amount;
								for (int j = 0; j < n; j++) {
									ArticleEntity ae = player.getArticleEntity(g.exchangeArticleNames[i]);
									if (ae != null) {
										ArticleEntity removeAe = player.removeArticleEntityFromKnapsackByArticleId(ae.getId(), "商店物品兑换" + g.articleName + "删除", true);
										if (removeAe == null) {
											HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.删除物品不成功);
											player.addMessageToRightBag(hreq);
											logger.warn("[用户购买商品] [失败] [删除物品不成功] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [" + name + "] [" + g.articleName + "] [一捆数量:" + g.bundleNum + "] [购买数量:" + amount + "] [j:" + j + "] [n:" + n + "]");
											return false;
										}
									}
								}
							}
						}
					}else if(g.getPayType() == CurrencyType.ACTIVENESS){
						currencyType = CurrencyType.ACTIVENESS;
						price = g.price / g.bundleNum;
						int yprice = g.price;
						expense = (int) (yprice * amount * discountRate);
						long activity = player.getActivenessInfo().getTotalActiveness();//pai.getTotalActiveness();
						if(activity < expense){
							player.sendError(Translate.活跃度不足是否去充值);
							return false;
						}
						economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "");
					}else if(g.getPayType() == CurrencyType.JIFEN){
						currencyType = CurrencyType.JIFEN;
						price = g.price / g.bundleNum;
						int yprice = g.price;
						expense = (int) (yprice * amount * discountRate);
						if (player.getChargePoints() < expense) {
							//韩国充值不给积分
							if(PlatformManager.getInstance().isPlatformOf(Platform.韩国)){
								player.sendError(Translate.积分不足);
							}else{
								BillingCenter.银子不足时弹出充值确认框(player, Translate.积分不足是否去充值);
							}
							return false;
						}
						economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "");
					}else if(g.getPayType() == CurrencyType.XUESHI){
						currencyType = CurrencyType.XUESHI;
						price = g.price / g.bundleNum;
						int yprice = g.price;
						expense = (int) (yprice * amount * discountRate);
						if(player.getJjcPoint() < expense){
							HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.血石不足);
							player.addMessageToRightBag(req);
							return false;
						}
//						if(this.getName_stat().equals("战勋商店")){
//							if(g.getJjcLevelLimit() > 0){
//								BattleTeamMember btm = JJCManager.getInstance().getPlayerInfo(player.getId(), 1);
//								if(btm != null){
//									if(g.getJjcLevelLimit() > btm.getBattleLevel()){
//										HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.个人战勋不满足);
//										player.addMessageToRightBag(req);
//										return false;
//									}
//								}
//							}
//						}
						economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "");
					}else if(g.getPayType() == CurrencyType.CROSS_POINT){
						currencyType = CurrencyType.CROSS_POINT;
						price = g.price / g.bundleNum;
						int yprice = g.price;
						expense = (int) (yprice * amount * discountRate);
						if(player.getHonorPoint() < expense){
							HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.积分不足);
							player.addMessageToRightBag(req);
							return false;
						}
						economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "");
						if(g.getPersonSellNumLimit() > 0){
							player.goodName.remove(new Integer(g.id));
						}
					}
					else if(g.getPayType() == CurrencyType.CHONGZHI){
						Passport passport = player.getPassport();
						if(passport != null){
							String channelName = passport.getLastLoginChannel();
							if(channelName != null && !channelName.isEmpty()){
								List<ChargeMode> list = ChargeManager.getInstance().getChannelChargeModes(channelName);
								if (list == null || list.size() == 0) {
									player.sendError(Translate.无匹的充值信息请联系GM);
									logger.error(player.getLogString() + "[查询充值列表] [异常] [无匹配的充值列表] [shopName:{}] [gname:{}] [渠道:{}] [玩家:{}]",new Object[]{this.getName(),g.getArticleName(), channelName,player.getLogString()});
									return false;
								}
								
								LevelPackageActivity activity = null;
								int index = -1;
								end:for(LevelPackageActivity a : ActivityManager.getInstance().levelPackageActivity){
									if(a.getLowLevel() <= player.getLevel() && player.getLevel() <= a.getMaxLevel()){
										if(a != null && a.getShopName().equals(this.getName_stat())){
											for(int i=0;i<a.getGoodsname().length;i++){
												if(a.getGoodsname()[i].equals(g.getArticleName_stat())){
													activity = a;
													index = i;
													break end;
												}
											}
										}
									}
								}
								
								if(activity == null || !activity.isEffectActivity(player)){
									if(logger.isInfoEnabled()){
										logger.info("[用户购买商品] [失败] [activity==null] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [" + name + "] [" + g.articleName + "] [一捆数量:" + g.bundleNum + "] [购买数量:" + amount + "]");
									}
									return false;
								}
								
								long chargeMoney = activity.getMoneys()[index] * 100;
								ChargeMode chargemode = null;
								String chargeId = "";
								String specConfig = "";
								end:for(ChargeMode mode : list){
									if(mode != null && mode.getModeName().equals("支付宝")){
										if(mode.getMoneyConfigures() != null){
											for(ChargeMoneyConfigure config : mode.getMoneyConfigures()){
												if(config.getDenomination() == chargeMoney){
													chargemode = mode;
													chargeId = config.getId();
													specConfig = config.getSpecialConf();
													break end;
												}
											}
										}
									}
								}
								
								if(chargemode == null){
									end:for(ChargeMode mode : list){
										if(mode != null && mode.getMoneyConfigures() != null){
											for(ChargeMoneyConfigure config : mode.getMoneyConfigures()){
												if(config.getDenomination() == chargeMoney){
													chargemode = mode;
													chargeId = config.getId();
													specConfig = config.getSpecialConf();
													break end;
												}
											}
										}
									}
								}
								
								if(chargemode == null){
									player.sendError(Translate.暂时不能充值联系GM);
									if(logger.isInfoEnabled()){
										logger.info("[用户购买商品] [失败] [chargemode==null] [channelName:"+channelName+"] [chargeMoney:"+chargeMoney+"] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [" + name + "] [" + g.articleName + "] [一捆数量:" + g.bundleNum + "] [购买数量:" + amount + "]");
									}
									return false;
								}
								
								CHARGE_AGRS_RES res = new CHARGE_AGRS_RES(GameMessageFactory.nextSequnceNum(), chargemode.getModeName(), chargeId, specConfig, chargeValue, SavingReasonType.商城充值送等级礼包, chargeMoney, new String[0]);
								player.addMessageToRightBag(res);	
							}else{
								if(logger.isInfoEnabled()){
									logger.info("[用户购买商品] [失败] [channelName==null] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [" + name + "] [" + g.articleName + "] [一捆数量:" + g.bundleNum + "] [购买数量:" + amount + "]");
								}
								return false;
							}
						}else{
							if(logger.isInfoEnabled()){
								logger.info("[用户购买商品] [失败] [passport==null] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [" + name + "] [" + g.articleName + "] [一捆数量:" + g.bundleNum + "] [购买数量:" + amount + "]");
							}
							return false;
						}
						
					}else{
						return false;
					}
					for (ArticleEntity ae : elist) {
						articleEntityManager.putToRealSaveCache(ae);
					}
					logger.warn("[用户购买商品] ["+player.getName()+"] [gname:"+g.getArticleName()+"] [总共卖出："+g.totalSellNum+"] [在时间限制下的数量限制:"+g.在时间限制下的数量限制+"] [全服限购："+g.totalSellNumLimit+"] [时间限制类型:"+g.timeLimitType+"] [个人限购："+g.getPersonSellNumLimit()+"] [剩余数量："+g.getOverNum()+"] [支付类型："+g.getPayType()+"] [===========支付完]");
				} catch (NoEnoughMoneyException e) {
					for (ArticleEntity ae : elist) {
						articleEntityManager.removeTransientATable(ae);
					}
					if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [失败] [余额不足] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [" + name + "] [" + g.articleName + "] [一捆数量:" + g.bundleNum + "] [购买数量:" + amount + "]", e);

					String msg = Translate.text_5659;
					if (g != null && g.getPayType() < Translate.余额不足2.length) {
						msg = Translate.余额不足2[g.getPayType()];
					}
					HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, msg);
					player.addMessageToRightBag(req);

					return false;

				} catch (BillFailedException e) {
					for (ArticleEntity ae : elist) {
						articleEntityManager.removeTransientATable(ae);
					}
					if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [失败] [计费失败] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [" + name + "] [" + g.articleName + "] [一捆数量:" + g.bundleNum + "] [购买数量:" + amount + "]", e);

					HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_5660);
					player.addMessageToRightBag(req);

					return false;
				} catch (Exception e) {
					for (ArticleEntity ae : elist) {
						articleEntityManager.removeTransientATable(ae);
					}
					if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [异常] [计费失败] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [" + name + "] [" + g.articleName + "] [一捆数量:" + g.bundleNum + "] [购买数量:" + amount + "]", e);

					HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_5660);
					player.addMessageToRightBag(req);

					return false;
				}

				// 把商品放入背包
				player.putAll(elist.toArray(new ArticleEntity[0]), "商店购买");
				
				//数据检查
				GoodsPriceLimit gpl=GoodsPriceLimitManager.getInstance().getGoodPriceLimit(g.getArticleName_stat(), g.getColor());
				if(gpl!=null){
					ArticleManager.logger.error("[商店检查] [实际购买数量:"+elist.size()+"预警数量:"+gpl.getLimitNum()+"]");
					if(!(elist.size()<gpl.getLimitNum())){
						ArticleManager.logger.error("[商店检查] [报警]");
						MailEventManger.getInstance().addTask(MailEvent.商店检查.立即发送(new String[][]{{this.getName_stat(),g.getArticleName_stat(),g.getColor()+"","出售数量预警","{角色名:"+player.getName()+"}{账号:"+player.getUsername()+"}{id:"+player.getId()+"}{等级:"+player.getLevel()+"}{vip等级:"+player.getVipLevel()+"}{"+"购买数量:"+elist.size()+"}"}}));
					}
				}

				// 统计
				if (elist.size() > 0) {
					ArticleStatManager.addToArticleStat(player, null, elist.get(0), ArticleStatManager.OPERATION_物品获得和消耗, price, (byte) g.getPayType(), elist.size(), "商店购买", this.getName_stat());
				}

				// 数量
				g.totalSellNum = g.totalSellNum + amount;
				if(g.totalSellNumLimit - g.totalSellNum >=0){
					g.overNum = (int)(g.totalSellNumLimit - g.totalSellNum);
				}
				
				g.totalSellNumLastFlushTime = now;
				g.dirty = true;

				// 数量
				long configtime = 0;
				long allsellnums = 0;
				if (g.personSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
					SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(id, g.id, g.getArticleName());
					sgd.addSelledNum(player.getId(), amount,g.getFixTimeBeginLimit(),g.getFixTimeEndLimit());
					sm.saveSelledGoodsData(id, g.id, g.getArticleName(), sgd);
					configtime = sgd.getConfigEndTime();
				}
				long starttime = 0;
				if(g.totalSellNumLimit > ShopManager.NUM_LIMIT_NONE){
					SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(id, g.id, g.getArticleName());
					sgd.addSelledNum(player.getId(), amount,g.getFixTimeBeginLimit(),g.getFixTimeEndLimit());
					sgd.setSelledGoodsCount(sgd.getSelledGoodsCount()+amount);
					sm.saveSelledGoodsData(id, g.id, g.getArticleName(), sgd);
					configtime = sgd.getConfigEndTime();
					starttime = sgd.getConfigStartTime();
					allsellnums = sgd.getSelledGoodsCount();
				}
				logger.warn("[用户购买商品] ["+player.getName()+"] [gname:"+g.getArticleName()+"] [allsellnums:"+allsellnums+"] [总共卖出："+g.totalSellNum+"] [在时间限制下的数量限制:"+g.在时间限制下的数量限制+"] [全服限购："+g.totalSellNumLimit+"] [时间限制类型:"+g.timeLimitType+"] [个人限购："+g.getPersonSellNumLimit()+"] [剩余数量："+g.getOverNum()+"] [支付类型："+g.getPayType()+"] [starttime:"+starttime+"] [endtime:"+configtime+"] ["+g.getFixTimeEndLimit()+"] [===========支付完2]");
//				SHOP_BUY_RES res = new SHOP_BUY_RES(GameMessageFactory.nextSequnceNum(), getName(), goodsId, g.getDescription(player));
//				player.addMessageToRightBag(res);

				if (elist.size() > 0) {
					ArticleEntity ae = elist.get(0);
					try {
						Article article2 = articleManager.getArticle(ae.getArticleName());
						if(article2 != null && article2 instanceof InlayArticle){
							InlayArticle aaArticle = (InlayArticle)article2;
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.translateString(Translate.您购买了物品, new String[][] { { Translate.COUNT_1, (g.bundleNum * amount) + "" }, { Translate.STRING_1,aaArticle.getShowName() } }));
							player.addMessageToRightBag(hreq);
							Player.sendPlayerAction(player, PlayerActionFlow.行为类型_购买, aaArticle.getShowName() + "*" + (g.bundleNum * amount), 0, new Date(), GamePlayerManager.isAllowActionStat());
							
						}else{
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.translateString(Translate.您购买了物品, new String[][] { { Translate.COUNT_1, (g.bundleNum * amount) + "" }, { Translate.STRING_1, ae.getArticleName() } }));
							player.addMessageToRightBag(hreq);
							Player.sendPlayerAction(player, PlayerActionFlow.行为类型_购买, ae.getArticleName() + "*" + (g.bundleNum * amount), 0, new Date(), GamePlayerManager.isAllowActionStat());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				StringBuffer ss = new StringBuffer();
				if (g.getPayType() == CurrencyType.BIND_YINZI) {
					ss.append((long) (g.price * g.bundleNum * amount * discountRate) + Translate.绑定银子);
				} else if (g.getPayType() == CurrencyType.YINZI) {
					ss.append((long) (g.price * g.bundleNum * amount * discountRate) + Translate.非绑定银子);
				}

				// 使用飞行坐骑成功活动
//				if (g.getPayType() == CurrencyType.ARTICLES) {
//					try {
//						CompoundReturn cr = ActivityManagers.getInstance().getValue(4, player);
//						if (cr != null && cr.getBooleanValue()) {
//							String servername = GameConstants.getInstance().getServerName();
//							if (g.getArticleName().equals(Translate.八卦仙蒲)) {
//								if (ActivityManagers.getInstance().getDdc().get(servername + Translate.仙蒲圣者) == null) {
//									int[] keys = player.getKeyByTitle(Translate.仙蒲圣者);
//									player.addTitle(keys[0], Translate.仙蒲圣者, keys[1],"",0, Translate.仙蒲圣者,"","", -1L);
//									ActivityManagers.getInstance().getDdc().put(servername + Translate.仙蒲圣者, servername + "--" + player.getId());
//									String msg = Translate.translateString(Translate.成功驾驭八卦仙蒲, new String[][] { { Translate.STRING_1, player.getName() } });
//									player.send_HINT_REQ(msg, (byte) 2);
//									ActivitySubSystem.logger.warn("[韩国消费送称号] [全服唯一] [领取成功] [坐骑:八卦仙蒲] [称号:仙蒲圣者] [" + player.getLogString() + "]");
//								} else {
//									ActivitySubSystem.logger.warn("[韩国消费送称号] [全服唯一] [已经领取过了] [坐骑:八卦仙蒲] [称号:仙蒲圣者] [" + player.getLogString() + "]");
//								}
//							} else if (g.getArticleName().equals(Translate.乾坤葫芦)) {
//								if (ActivityManagers.getInstance().getDdc().get(servername + Translate.乾坤葫芦) == null) {
//									int[] keys = player.getKeyByTitle(Translate.乾坤仙人);
//									player.addTitle(keys[0], Translate.乾坤仙人, keys[1],"",0, Translate.乾坤仙人,"","", -1L);
//									ActivityManagers.getInstance().getDdc().put(servername + Translate.乾坤葫芦, servername + "--" + player.getId());
//									String msg = Translate.translateString(Translate.成功驾驭乾坤葫芦, new String[][] { { Translate.STRING_1, player.getName() } });
//									player.send_HINT_REQ(msg, (byte) 2);
//									ActivitySubSystem.logger.warn("[韩国消费送称号] [全服唯一] [领取成功] [坐骑:乾坤葫芦] [称号:乾坤仙人] [" + player.getLogString() + "]");
//								} else {
//									ActivitySubSystem.logger.warn("[韩国消费送称号] [全服唯一] [已经领取过了] [坐骑:乾坤葫芦] [称号:乾坤仙人] [" + player.getLogString() + "]");
//								}
//							} else if (g.getArticleName().equals(Translate.梦瞳仙鹤)) {
//								if (ActivityManagers.getInstance().getDdc().get(servername + Translate.梦瞳仙鹤) == null) {
//									int[] keys = player.getKeyByTitle(Translate.驭鹤尊者);
//									player.addTitle(keys[0], Translate.驭鹤尊者, keys[1],"",0, Translate.驭鹤尊者,"","", -1L);
//									ActivityManagers.getInstance().getDdc().put(servername + Translate.梦瞳仙鹤, servername + "--" + player.getId());
//									String msg = Translate.translateString(Translate.成功驾驭梦瞳仙鹤, new String[][] { { Translate.STRING_1, player.getName() } });
//									player.send_HINT_REQ(msg, (byte) 2);
//									ActivitySubSystem.logger.warn("[韩国消费送称号] [全服唯一] [领取成功] [坐骑:梦瞳仙鹤] [称号:驭鹤尊者] [" + player.getLogString() + "]");
//								} else {
//									ActivitySubSystem.logger.warn("[韩国消费送称号] [全服唯一] [已经领取过了] [坐骑:梦瞳仙鹤] [称号:驭鹤尊者] [" + player.getLogString() + "]");
//								}
//							}
//						}
//					} catch (Exception e) {
//						ActivitySubSystem.logger.warn("[韩国消费送称号] [异常] [" + e + "]");
//					}
//
//				}
				
//				if(this.shopType == ShopManager.跨服商店){
//					List<Goods> list = new ArrayList<Goods>();
//					for(int id : player.goodName){
//						Goods sg = this.getGoodsById(id);
//						if(sg != null){
//							list.add(sg);
//						}
//					}
//					Goods gs[] = list.toArray(new Goods[]{});
//					long coins[] = new long[1];
//					coins[0] = player.getHonorPoint();
//					com.fy.engineserver.shop.client.Goods[] cGoods = ShopManager.translate(player,this,ActivityManager.getInstance().getLevelGoods(this.getName_stat(), player, gs));//gs);
//					SHOP_GET_RES res = new SHOP_GET_RES(GameMessageFactory.nextSequnceNum(),(byte)0,name,this.getVersion(),this.shopType,coins,cGoods);
//					player.addMessageToRightBag(res);
//					SHOP_OTHER_INFO_RES shopOtherRes = new SHOP_OTHER_INFO_RES(GameMessageFactory.nextSequnceNum(), (byte)0, name, this.shopType, cGoods);
//					player.addMessageToRightBag(shopOtherRes);
//				}
				
				if (logger.isWarnEnabled()) logger.warn("[用户购买商品] [成功] [OK] [花费：{}] [折扣:{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}] [全服限购:{}] [个人限购:{}] [剩余数量:{}]", new Object[] { ss.toString(), discountRate, player.getUsername(), player.getName(), name, g.articleName, g.bundleNum, amount ,g.totalSellNumLimit,g.personSellNumLimit,g.overNum});

				if (name != null && name.equals(Translate.活跃商城)) {
					try {
						EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.活跃商城购买道具次数, 1L});
						EventRouter.getInst().addEvent(evt);
					} catch (Exception ee) {
						PlayerAimManager.logger.error("[目标系统] [统计玩家活跃度购买商品次数异常][" + player.getLogString() + "]", ee);
					}
				}
				{
					DiscountCardAgent.getInstance().noticeBuySuccess(player, g.articleName, amount * g.bundleNum, this.name,g.getColor());
					if (discountRate == 1) {
						ShopActivityManager.getInstance().noticeBuySuccess(player, this, g, amount);
					}
				}
				
				{
					CompoundReturn cr = ActivityManagers.getInstance().getValue(8, player);
					if (cr != null && cr.getBooleanValue()) {
						ActivitySubSystem.logger.warn("[购买东西发公告] ["+player.getLogString()+"] [g.bundleNum:"+g.bundleNum+"] [g.articleName:"+g.articleName+"] [g.getPayType():"+g.getPayType()+"] [1]");
						if(g.articleName.equals(cr.getStringValue()) && g.bundleNum==cr.getIntValue()){
							String mess = Translate.translateString(Translate.恭喜获得开天礼包, new String[][] { { Translate.STRING_1, player.getName() } });
							if(g.getPayType() == CurrencyType.YINZI){
								try {
									ChatMessageService cms = ChatMessageService.getInstance();
									ChatMessage msg = new ChatMessage();
									msg.setMessageText(mess);
									cms.sendMessageToSystem(msg);
									player.sendError(mess);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}	
				}
				return true;

			}
		}
		
	}

	public Goods getGoodsById(int goodsId) {
		if (this.goods != null) {
			for (int i = 0; i < this.goods.size(); i++) {
				if (this.goods.get(i) != null && this.goods.get(i).id == goodsId) {
					return this.goods.get(i);
				}
			}
		}
		return null;
	}

	/**
	 * 玩家将一些物品卖给商店
	 * 
	 * @param player
	 * @param knapsackIndex
	 *            背包中那个格子上的物品
	 * 
	 */
	public void sellArticleEntity(Player player, boolean fangbaoFlag, int knapsackIndex) {
		doSellArticleEntity(player, fangbaoFlag, knapsackIndex, true, true);
	}

	//
	// /**
	// * 玩家将一些物品卖给商店
	// *
	// * @param player
	// * @param knapsackIndex 背包中那个格子上的物品
	// *
	// */
	// public void sellArticleEntity(Player player, int knapsackIndex , boolean notifyPlayer){
	// doSellArticleEntity(player,knapsackIndex,notifyPlayer,true);
	// }
	/**
	 * 玩家将一些物品卖给商店
	 * 
	 * @param player
	 * @param knapsackIndex
	 *            背包中那个格子上的物品
	 * 
	 */
	public void doSellArticleEntity(Player player, boolean fangbaoFlag, int knapsackIndex, boolean notifyPlayer, boolean 紫装是否提示) {
		Knapsack knapsack = null;
		if (fangbaoFlag) {
			knapsack = player.getKnapsack_fangbao();
		} else {
			knapsack = player.getKnapsack_common();
		}
		Cell cell = knapsack.getCell(knapsackIndex);
		if (cell == null || cell.getEntityId() == -1 || cell.getCount() <= 0) {

			// logger.warn("[出售物品] [失败] [对应背包格子中没有物品] ["+player.getName()+"] ["+player.getId()+"] [背包下标:"+knapsackIndex+"] [实体ID:-] [出售数量:-]");
			if (logger.isWarnEnabled()) logger.warn("[出售物品] [失败] [对应背包格子中没有物品] [{}] [{}] [背包下标:{}] [实体ID:-] [出售数量:-]", new Object[] { player.getName(), player.getId(), knapsackIndex });

			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_5662);
			player.addMessageToRightBag(req);
			EnterLimitManager.setValues(player, PlayerRecordType.出售空格子次数);
			return;
		}

		ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(cell.getEntityId());
		if (entity == null) {
			// logger.warn("[出售物品] [失败] [对应背包格子中没有物品] ["+player.getName()+"] ["+player.getId()+"] [背包下标:"+knapsackIndex+"] [实体ID:"+cell.getEntityId()+"] [出售数量:"+cell.getCount()+"]");
			if (logger.isWarnEnabled()) logger.warn("[出售物品] [失败] [对应背包格子中没有物品] [{}] [{}] [背包下标:{}] [实体ID:{}] [出售数量:{}]", new Object[] { player.getName(), player.getId(), knapsackIndex, cell.getEntityId(), cell.getCount() });

			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_5662);
			player.addMessageToRightBag(req);
			EnterLimitManager.setValues(player, PlayerRecordType.出售空格子次数);
			return;
		}

		Article article = ArticleManager.getInstance().getArticle(entity.getArticleName());
		if (article == null) {
			// logger.warn("[出售物品] [失败] [对应背包格子中的物体没有对应的物种] ["+player.getName()+"] ["+player.getId()+"] [背包下标:"+knapsackIndex+"] [实体ID:"+cell.getEntityId()+"] [出售数量:"+cell.getCount()+"]");
			if (logger.isWarnEnabled()) logger.warn("[出售物品] [失败] [对应背包格子中的物体没有对应的物种] [{}] [{}] [背包下标:{}] [实体ID:{}] [出售数量:{}]", new Object[] { player.getName(), player.getId(), knapsackIndex, cell.getEntityId(), cell.getCount() });

			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_5663);
			player.addMessageToRightBag(req);
			return;
		}

		if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_Common, entity.getId())) {
			player.sendError(Translate.锁魂物品不能做此操作);
			return;
		}

		if (article instanceof InlayArticle) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.宝石不能卖店);
			player.addMessageToRightBag(hreq);
			return;
		}
		if (entity instanceof EquipmentEntity) {
			if (((EquipmentEntity) entity).getInlayArticleIds() != null) {
				for (long l : ((EquipmentEntity) entity).getInlayArticleIds()) {
					if (l > 0) {
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.镶嵌了宝石的装备不能卖店);
						player.addMessageToRightBag(hreq);
						return;
					}
				}
			}
		}

		if (article.isSailFlag() == false) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_5664);
			player.addMessageToRightBag(req);
			return;
		}

		if (article instanceof Equipment) {
			Equipment e = (Equipment) article;
			if (紫装是否提示 && e.getColorType() >= ArticleManager.equipment_color_紫) {

				WindowManager wm = WindowManager.getInstance();
				MenuWindow mw = wm.createTempMenuWindow(180);
				mw.setDescriptionInUUB(Translate.text_5666);

				Option_SellGoodsToShop op1 = new Option_SellGoodsToShop();
				op1.setShop(this);
				op1.setPlayer(player);
				op1.setKnapsackIndex(knapsackIndex);
				op1.setFangbaoFlag(fangbaoFlag);
				op1.setText(Translate.text_62);

				Option_Cancel op2 = new Option_Cancel();
				op2.setText(Translate.text_364);
				Option[] options = new Option[] { op1, op2 };
				mw.setOptions(options);

				CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
				player.addMessageToRightBag(req);
				return;
			}
		}

		int price = article.getPrice();
		int total = (int) (price * cell.getCount() * Player.得到玩家卖给npc物品时的pk惩罚百分比(player));
		if (total <= 0) {
			total = 1;
		}

		BillingCenter economicCenter = BillingCenter.getInstance();

		try {
			economicCenter.playerSaving(player, total, CurrencyType.BIND_YINZI, SavingReasonType.SELL_ARTICLE, "");
			int count = cell.getCount();

			ArticleEntity ae = knapsack.getArticleEntityByCell(knapsackIndex);

			for (int i = 0; i < count; i++) {
				ArticleEntity removeAe = knapsack.remove(knapsackIndex, "商店出售删除", true);
				if (removeAe != null) {
					// 统计
					ArticleStatManager.addToArticleStat(player, null, removeAe, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "商店出售删除", null);
				}
			}

			ArrayList<SellItem> al = sellMap.get(player.getName());
			if (al == null) {
				al = new ArrayList<SellItem>();
				sellMap.put(player.getName(), al);
			}
			SellItem si = new SellItem();
			si.ae = entity;
			si.count = count;
			si.sellMoney = total;
			si.sellTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			al.add(si);

			// logger.warn("[出售物品] [成功] [-] ["+player.getName()+"] ["+player.getId()+"] [获得："+total+"] [背包下标:"+knapsackIndex+"] [实体:"+(ae != null ?
			// ae.getArticleName():"--")+"] [出售数量:"+count+"]");
			if (logger.isWarnEnabled()) logger.warn("[出售物品] [成功] [-] [{}] [{}] [获得：{}] [背包下标:{}] [实体:{}] [出售数量:{}]", new Object[] { player.getName(), player.getId(), total, knapsackIndex, (ae != null ? ae.getArticleName() : "--"), count });

			if (notifyPlayer) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.translateString(Translate.出售成功获得绑银, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(total) } }));
				player.addMessageToRightBag(req);
			}
			return;

		} catch (SavingFailedException e) {
			// logger.warn("[出售物品] [失败] [存钱失败，出现异常] ["+player.getName()+"] ["+player.getId()+"] [背包下标:"+knapsackIndex+"] [实体ID:"+cell.getEntityId()+"] [出售数量:"+cell.getCount()+"]");
			if (logger.isWarnEnabled()) logger.warn("[出售物品] [失败] [存钱失败，出现异常] [{}] [{}] [背包下标:{}] [实体ID:{}] [出售数量:{}]", new Object[] { player.getName(), player.getId(), knapsackIndex, cell.getEntityId(), cell.getCount() });

			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_5660);
			player.addMessageToRightBag(req);
			return;
		} catch (Exception ex) {
			// logger.warn("[出售物品] [异常] [存钱失败，出现异常] ["+player.getName()+"] ["+player.getId()+"] [背包下标:"+knapsackIndex+"] [实体ID:"+cell.getEntityId()+"] [出售数量:"+cell.getCount()+"]");
			if (logger.isWarnEnabled()) logger.warn("[出售物品] [异常] [存钱失败，出现异常] [{}] [{}] [背包下标:{}] [实体ID:{}] [出售数量:{}]", new Object[] { player.getName(), player.getId(), knapsackIndex, cell.getEntityId(), cell.getCount() });

			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_5660);
			player.addMessageToRightBag(req);
			return;
		}
	}

	/**
	 * 玩家回购商品，此方法处理回购。
	 * 成功或者失败都提醒玩家。
	 * 
	 * @param player
	 * @param goodsId
	 *            商品在商店中的索引
	 * @param amount
	 *            购买多少个
	 */
	public void buyBack(Player player, long articleEntityId, int amount) {
		// 检查背包对应的entity以及数量是否正确

		HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_5668);
		player.addMessageToRightBag(req);

		// 如实现回购功能，需要修改物品废弃状态
	}

	/**
	 * 获得玩家卖掉的所有的物品
	 * @param player
	 * @return
	 */
	public ArticleEntity[] getSellArticleEntities(Player player) {
		ArrayList<SellItem> al = sellMap.get(player.getName());
		ArticleEntity aes[] = new ArticleEntity[al.size()];
		for (int i = 0; i < aes.length; i++) {
			aes[i] = al.get(i).ae;
		}
		return aes;
	}

	/**
	 * 获得玩家卖掉的所有的物品对应的数量，此顺序必须和getSellArticleEntities方法返回的物品一一对应
	 * @param player
	 * @return
	 */
	public int[] getSellArticleEntityNums(Player player) {
		ArrayList<SellItem> al = sellMap.get(player.getName());
		int aes[] = new int[al.size()];
		for (int i = 0; i < aes.length; i++) {
			aes[i] = al.get(i).count;
		}
		return aes;
	}

	/**
	 * 获得玩家卖掉的所有的物品对应的数量，此顺序必须和getSellArticleEntities方法返回的物品一一对应
	 * @param player
	 * @return
	 */
	public int[] getSellArticleEntityMoneys(Player player) {
		ArrayList<SellItem> al = sellMap.get(player.getName());
		int aes[] = new int[al.size()];
		for (int i = 0; i < aes.length; i++) {
			aes[i] = al.get(i).sellMoney;
		}
		return aes;
	}

	/**
	 * 得到配置的一个商品
	 * @param articleName
	 * @param color
	 * @return
	 */
	public Goods getGoods(String articleName, int color) {
		if (goods != null) {
			for (Goods g : goods) {
				if (g != null) {
					if (g.getArticleName().equals(articleName) && (color == -1 || g.getColor() == color)) {
						return g;
					}
				}
			}
		}
		return null;
	}

	public List<Goods> getGoods() {
		return goods;
	}

	public String getLogString() {
		return "[" + this.id + "," + this.name + "/" + this.name_stat + "]";
	}

	public Map<Long,List<Integer>> getTempGoods() {
		return tempGoods;
	}

	public void setTempGoods(Map<Long,List<Integer>> tempGoods) {
		this.tempGoods = tempGoods;
	}
	public int getGoodsIdByGoodName(String goodName) {
		if (this.goods != null) {
			for (int i = 0; i < this.goods.size(); i++) {
				if (this.goods.get(i) != null && this.goods.get(i).articleName.equals(goodName)) {
					return this.goods.get(i).id;
				}
			}
		}
		return -1;
	}
	public Goods getGoodsByGoodName(String goodName) {
		if (this.goods != null) {
			for (int i = 0; i < this.goods.size(); i++) {
				if (this.goods.get(i) != null && this.goods.get(i).articleName.equals(goodName)) {
					return this.goods.get(i);
				}
			}
		}
		return null;
	}
}
