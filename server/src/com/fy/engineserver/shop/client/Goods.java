package com.fy.engineserver.shop.client;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.shop.ShopManager;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.text.DateUtil;


/**
 * 商品：
 * 
 * 		一个商品对应一捆物品，可以定义具体的物品以及一捆物品多少个
 * 		同时可以定义一个商品的价格，分为3中支付方式：
 * 		金币
 * 		元宝
 * 		绑定元宝
 * 		物品交换
 * 		
 * 
 *
 */
public class Goods{

	//金币
	public static final int PAY_TYPE_MONEY = 0;
	
	//元宝
	public static final int PAY_TYPE_YUANBO = 1;
	
	//绑定元宝
	public static final int PAY_TYPE_BIND_YUANBO = 2;
	
	//物品交换
	public static final int PAY_TYPE_ARTICLE = 3;
	
	//帮派资金
	public static final int PAY_TYPE_BANG_MONEY = 4;
	
	//荣誉
	public static final int PAY_TYPE_HONOR = 5;
	
	//师徒积分
	public static final int PAY_TYPE_TEACHER_POINTS = 6;
	
	//帮派积分
	public static final int PAY_TYPE_GANG_CREDIT_POINTS = 7;
	
	public static final int PAY_TYPE_CHARGE_POINTS = 8;
	
	public static final String[] PAY_TYPE_NAMES = new String[]{Translate.text_148,Translate.text_592,Translate.text_594,Translate.text_5606,Translate.text_318,Translate.text_208,Translate.text_4113,Translate.text_4114,Translate.text_4115};
	
	//商品的唯一id，有编辑器自动生成，一旦生成就不能在进行变化
	protected int id;
	
	/**
	 * 单个商品购买上限
	 */
	protected int goodMaxNumLimit;
	
	
	public int getGoodMaxNumLimit() {
		return goodMaxNumLimit;
	}

	public void setGoodMaxNumLimit(int goodMaxNumLimit) {
		this.goodMaxNumLimit = goodMaxNumLimit;
	}

	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
	
	protected long articleId;
	private int jjcLevelLimit;
	/**
	 * 物品的名称
	 */
	protected String articleName = "";

	/**
	 * 物品颜色
	 */
	protected int color;
	

	/**
	 * 物品等级
	 */
	protected int level;
	
	/**
	 * 商品小标注，如热卖，新品，推荐
	 */
	protected String littleSellIcon = "";

	/**
	 * 一捆包含的物品数量
	 */
	protected int bundleNum;
	
	/**
	 * 支付方式
	 */
	protected int pType;
	
	/**
	 * 物品现在价格，如果支付方式为物物交换，那么不用这个值
	 */
	protected int price;
	
	/**
	 * 物品原来价格
	 */
	protected int oldPrice;

	/**
	 * 物品交换模式下，用于交换的物品，此物品必须在背包中
	 */
	protected String exchangeArticleNames[] = new String[0];
	
	/**
	 * 物品交换模式下，用于交换的物品的数量，此物品必须在背包中，默认为1个
	 */
	protected int exchangeArticleNums[] = new int[0];
	
	///////////////
	//购买限制
	/**
	 * 是否有声望限制，true标识有
	 */
	protected boolean reputeLimit;
	
	/**
	 * 限制声望的名字
	 */
	protected String limitReputeName = "";
	
	/**
	 * 限制声望的等级，大于等于此等级的才可以购买
	 */
	protected byte limitReputeLevel;

	/**
	 * 时间限制类型，0表示无限制
	 */
	protected int timeLimitType;
	
	/**
	 * true表示数量限制是和时间限制配合
	 * 比如每月2号到10号可以买10个，那么这个月2号到10号买了10个后，这个月就不能买了，下个月还可以再买10个
	 */
	protected boolean 在时间限制下的数量限制;
	/**
	 * 时间和数量限制值，
	 * 与操作可以判断具体有什么限制
	 */
	protected int limitValue;
	
	//总数量限制，0代表无限制
	protected long totalSellNumLimit;
	
	//每人购买数量限制，0代表无限制
	protected long personSellNumLimit;
	
	//每天定点限制，开始时间包含该点，如0代表0点
	protected int everyDayBeginLimit;
	
	//每天定点限制，结束时间包含该点，如0代表0点
	protected int everyDayEndLimit;
	
	//每周定点限制，开始时间 以距离周一0点的小时计算
	protected int everyWeekBeginLimit;
	
	//每周定点限制，结束时间 以距离周一0点的小时计算
	protected int everyWeekEndLimit;
	
	//每月定点限制，开始时间 以当月日期为计算，2标识当月2日
	protected int everyMonthBeginLimit;
	
	//每月定点限制，结束时间 以当月日期为计算，2标识当月2日
	protected int everyMonthEndLimit;
	
	//固定时间限制，开始时间，以系统时间为计算
	protected long fixTimeBeginLimit;
	
	//固定时间限制，结束时间 以系统时间为计算
	protected long fixTimeEndLimit;
	
	/**
	 * 职务限制，0表示无限制
	 */
	protected int zhiwuLimit;
	
	/**
	 * 功勋值限制，0表示无限制
	 */
	protected int gongxunLimit;
	
	/**
	 * 家族贡献值限制，0表示无限制
	 */
	protected int jiazuGongxianLimit;

	////////////////////////////////////////////////////////////////////////////////////
	//
	//   内部数据，不需要编辑
	//
	
	/**
	 * 需要物品的描述，此描述有服务器自动产生，无需编辑器设置
	 */
	protected transient String exchangeArticleDescription  = "";

	/**
	 * 用于描述商品的一些购买限制
	 */
	public transient String description  = "";
	
	/**
	 * 商品购买后是否绑定
	 */
	private boolean buyBind;
	
	/**
	 * 商品其他信息
	 */
	private String[] otherInfo = new String[0];
	
	/**
	 * 
	 */
	public boolean canBuy;
	
	/**
	 * 商品的时间控制，规则同上面商店一样
	 */
	protected long goodEndTime;
	
	/**
	 * 全服出售数量
	 */
	protected long serverNumlimit;
	
	/**
	 * 个人限购数量
	 */
	protected long playerNumlimit;
	/**
	 * 全服剩余数量
	 */
	protected long overNum;
	
	public long getGoodEndTime() {
		return goodEndTime;
	}

	public void setGoodEndTime(long goodEndTime) {
		this.goodEndTime = goodEndTime;
	}

	public void setServerNumlimit(int serverNumlimit) {
		this.serverNumlimit = serverNumlimit;
	}

	public long getServerNumlimit() {
		return serverNumlimit;
	}

	public void setServerNumlimit(long serverNumlimit) {
		this.serverNumlimit = serverNumlimit;
	}

	public long getPlayerNumlimit() {
		return playerNumlimit;
	}

	public void setPlayerNumlimit(long playerNumlimit) {
		this.playerNumlimit = playerNumlimit;
	}

	public void setOverNum(long overNum) {
		this.overNum = overNum;
	}

	public void setPlayerNumlimit(int playerNumlimit) {
		this.playerNumlimit = playerNumlimit;
	}

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}

	public int getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(int oldPrice) {
		this.oldPrice = oldPrice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCanBuy() {
		return canBuy;
	}

	public void setCanBuy(boolean canBuy) {
		this.canBuy = canBuy;
	}

	/**
	 * 此方法用于客户端中的小泡泡中
	 * 
	 * 用于描述商品的一些购买限制
	 */
	public String getDescription(Player player){
		
		Goods g = this;
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		
		StringBuffer sb = new StringBuffer();

		//检测时间和数量限制
		if( (g.limitValue & ShopManager.LIMIT_FIX_TIME) != 0){
			
			if(sb.length() > 0){
				sb.append("\n");
			}
			
			sb.append(Translate.text_5615+DateUtil.formatDate(new Date(g.fixTimeBeginLimit), "MM-dd")+"~"+DateUtil.formatDate(new Date(g.fixTimeEndLimit), "MM-dd"));
		}
		
		if( (g.limitValue & ShopManager.LIMIT_EVERYDAY_HOURS) != 0){
			
			if(sb.length() > 0){
				sb.append("\n");
			}
			int k1 = g.everyDayBeginLimit/60;
			int r1 = g.everyDayBeginLimit - k1 * 60;
			

			int k2 = g.everyDayEndLimit/60;
			int r2 = g.everyDayEndLimit - k2 * 60;
			
			
			sb.append(Translate.text_5616+k1+Translate.text_1469+r1+Translate.text_5617+k2+Translate.text_1469+r2+Translate.text_147);
			
		}
		
		if( (g.limitValue & ShopManager.LIMIT_EVERYWEEK_DAYS) != 0){
	
			int k1 = g.everyWeekBeginLimit/24;
			int r1 = g.everyWeekBeginLimit - k1 * 24;
			if(k1 > 6) k1 = 0;
			int k2 = g.everyWeekEndLimit/24;
			int r2 = g.everyWeekEndLimit - k2 * 24;
			if(k2 > 6) k2 = 0;
			
			if(sb.length() > 0){
				sb.append("\n");
			}
			sb.append(Translate.text_5618+ShopManager.WEEK_DAY_NAMES[k1]+r1+Translate.text_5619+ShopManager.WEEK_DAY_NAMES[k2]+(r2+1)+Translate.text_1469);
		}
		
		if( (g.limitValue & ShopManager.LIMIT_EVERYMONTH_DAYS) != 0){
			

			if(sb.length() > 0){
				sb.append("\n");
			}
			sb.append(Translate.text_5620+g.everyMonthBeginLimit+Translate.text_5621+g.everyMonthEndLimit+Translate.text_143);
			
		}
		
		if((g.limitValue & ShopManager.LIMIT_TOTAL_SELL_NUM) != 0){
			
			if(sb.length() > 0){
				sb.append("\n");
			}
			sb.append(Translate.text_5622+g.totalSellNum + Translate.text_5623);
		}
		
		if((g.limitValue & ShopManager.LIMIT_SELL_NUM_BY_PLAYER) != 0){
			long values[] = g.selledPersonGoodsData.get(player.getId());
			if(values == null){
				values = new long[2];
				values[0] = g.personSellNumLimit;
				values[1] = now;
				g.selledPersonGoodsData.put(player.getId(), values);
				g.dirty = true;
			}
			if(sb.length() > 0){
				sb.append("\n");
			}
			sb.append(Translate.text_5624+(values[0]) + Translate.text_5623);

		}
		
		
		return sb.toString();
	}

	//总量购买多少个
	public long totalSellNum = 0;
	
	//总量,最后刷新的时间
	public long totalSellNumLastFlushTime = 0;
	
	public long getTotalSellNumLastFlushTime(){
		return totalSellNumLastFlushTime;
	}
	
	public long getTotalSellNum() {
		return totalSellNum;
	}

	public void setTotalSellNum(long totalSellNum) {
		this.totalSellNum = totalSellNum;
		dirty = true;
	}

	public Hashtable<Long, long[]> getSelledPersonGoodsData() {
		return selledPersonGoodsData;
	}

	public void setSelledPersonGoodsData(
			Hashtable<Long, long[]> selledPersonGoodsData) {
		this.selledPersonGoodsData = selledPersonGoodsData;
		dirty = true;
	}

	public void setTotalSellNumLastFlushTime(long totalSellNumLastFlushTime) {
		this.totalSellNumLastFlushTime = totalSellNumLastFlushTime;
		dirty = true;
	}

	//每个玩家购买，还剩余多少，以及最后刷新的时间
	//Hashtable中，key为玩家id，value为数组，数组第一个值为还剩余多少，第二个值为最后刷新的时间
	public Hashtable<Long,long[]> selledPersonGoodsData = new Hashtable<Long,long[]>();

	protected boolean dirty;

	public int getLimitValue() {
		return limitValue;
	}

	public void setLimitValue(int limitValue) {
		this.limitValue = limitValue;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getTimeLimitType() {
		return timeLimitType;
	}

	public void setTimeLimitType(int timeLimitType) {
		this.timeLimitType = timeLimitType;
	}

	public long getTotalSellNumLimit() {
		return totalSellNumLimit;
	}

	public void setTotalSellNumLimit(long totalSellNumLimit) {
		this.totalSellNumLimit = totalSellNumLimit;
	}

	public long getPersonSellNumLimit() {
		return personSellNumLimit;
	}

	public void setPersonSellNumLimit(long personSellNumLimit) {
		this.personSellNumLimit = personSellNumLimit;
	}

	public int getEveryDayBeginLimit() {
		return everyDayBeginLimit;
	}

	public void setEveryDayBeginLimit(int everyDayBeginLimit) {
		this.everyDayBeginLimit = everyDayBeginLimit;
	}

	public int getEveryDayEndLimit() {
		return everyDayEndLimit;
	}

	public void setEveryDayEndLimit(int everyDayEndLimit) {
		this.everyDayEndLimit = everyDayEndLimit;
	}

	public int getEveryWeekBeginLimit() {
		return everyWeekBeginLimit;
	}

	public void setEveryWeekBeginLimit(int everyWeekBeginLimit) {
		this.everyWeekBeginLimit = everyWeekBeginLimit;
	}

	public int getEveryWeekEndLimit() {
		return everyWeekEndLimit;
	}

	public void setEveryWeekEndLimit(int everyWeekEndLimit) {
		this.everyWeekEndLimit = everyWeekEndLimit;
	}

	public int getEveryMonthBeginLimit() {
		return everyMonthBeginLimit;
	}

	public void setEveryMonthBeginLimit(int everyMonthBeginLimit) {
		this.everyMonthBeginLimit = everyMonthBeginLimit;
	}

	public int getEveryMonthEndLimit() {
		return everyMonthEndLimit;
	}

	public void setEveryMonthEndLimit(int everyMonthEndLimit) {
		this.everyMonthEndLimit = everyMonthEndLimit;
	}

	public long getFixTimeBeginLimit() {
		return fixTimeBeginLimit;
	}

	public void setFixTimeBeginLimit(long fixTimeBeginLimit) {
		this.fixTimeBeginLimit = fixTimeBeginLimit;
	}

	public long getFixTimeEndLimit() {
		return fixTimeEndLimit;
	}

	public void setFixTimeEndLimit(long fixTimeEndLimit) {
		this.fixTimeEndLimit = fixTimeEndLimit;
	}

	public int getZhiwuLimit() {
		return zhiwuLimit;
	}

	public void setZhiwuLimit(int zhiwuLimit) {
		this.zhiwuLimit = zhiwuLimit;
	}

	public int getGongxunLimit() {
		return gongxunLimit;
	}

	public void setGongxunLimit(int gongxunLimit) {
		this.gongxunLimit = gongxunLimit;
	}

	public int getJiazuGongxianLimit() {
		return jiazuGongxianLimit;
	}

	public void setJiazuGongxianLimit(int jiazuGongxianLimit) {
		this.jiazuGongxianLimit = jiazuGongxianLimit;
	}

	public String getExchangeArticleDescription() {
		return exchangeArticleDescription;
	}

	public void setExchangeArticleDescription(String exchangeArticleDescription) {
		this.exchangeArticleDescription = exchangeArticleDescription;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public int getBundleNum() {
		return bundleNum;
	}

	public void setBundleNum(int bundleNum) {
		this.bundleNum = bundleNum;
	}


	public int getPType() {
		return pType;
	}

	public void setPType(int pType) {
		this.pType = pType;
	}

	public String[] getExchangeArticleNames() {
		return exchangeArticleNames;
	}

	public void setExchangeArticleNames(String[] exchangeArticleNames) {
		this.exchangeArticleNames = exchangeArticleNames;
	}

	public int[] getExchangeArticleNums() {
		return exchangeArticleNums;
	}

	public void setExchangeArticleNums(int[] exchangeArticleNums) {
		this.exchangeArticleNums = exchangeArticleNums;
	}

	public boolean isReputeLimit() {
		return reputeLimit;
	}

	public void setReputeLimit(boolean reputeLimit) {
		this.reputeLimit = reputeLimit;
	}

	public String getLimitReputeName() {
		return limitReputeName;
	}
	
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setLimitReputeName(String limitReputeName) {
		this.limitReputeName = limitReputeName;
	}

	public byte getLimitReputeLevel() {
		return limitReputeLevel;
	}

	public void setLimitReputeLevel(byte limitReputeLevel) {
		this.limitReputeLevel = limitReputeLevel;
	}

	public String getLittleSellIcon() {
		return littleSellIcon;
	}

	public void setLittleSellIcon(String littleSellIcon) {
		this.littleSellIcon = littleSellIcon;
	}

	public void setBuyBind(boolean buyBind) {
		this.buyBind = buyBind;
	}

	public boolean isBuyBind() {
		return buyBind;
	}

	public void setOtherInfo(String[] otherInfo) {
		this.otherInfo = otherInfo;
	}

	public String[] getOtherInfo() {
		return otherInfo;
	}

	public long getOverNum() {
		return overNum;
	}

	public int getJjcLevelLimit() {
		return this.jjcLevelLimit;
	}

	public void setJjcLevelLimit(int jjcLevelLimit) {
		this.jjcLevelLimit = jjcLevelLimit;
	}

}
