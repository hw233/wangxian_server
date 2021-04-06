package com.fy.engineserver.shop;

import java.util.Calendar;
import java.util.Hashtable;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeapon;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

/**
 * 商品：
 * 
 * 一个商品对应一捆物品，可以定义具体的物品以及一捆物品多少个
 * 同时可以定义一个商品的价格，分为多种支付方式：
 * 金币
 * 元宝
 * 绑定元宝
 * 物品交换等等
 * 
 * 
 * 
 */
public class Goods {
	// 商品的唯一id，有编辑器自动生成，一旦生成就不能在进行变化
	public  int id;

	/**
	 * 物品的名称
	 */
	protected String articleName = "";
	/**
	 * 出现概率
	 */
	public int showProbabbly;
	/**
	 * 统计,方便韩语，英语
	 */
	protected String articleName_stat = "";

	/**
	 * 物品颜色
	 */
	protected int color;

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
	protected int payType;

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
	public String exchangeArticleNames[] = new String[0];

	/**
	 * 物品交换模式下，用于交换的物品的数量，此物品必须在背包中，默认为1个
	 */
	public  int exchangeArticleNums[] = new int[0];

	// /////////////
	// 购买限制
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
	public boolean 在时间限制下的数量限制;
	/**
	 * 时间和数量限制值，
	 * 与操作可以判断具体有什么限制
	 */
	protected int limitValue;

	// 总数量限制，0代表无限制
	protected long totalSellNumLimit;

	// 每人购买数量限制，0代表无限制
	protected long personSellNumLimit;

	/**
	 * 每天定点限制，开始时间包含该点，如0代表0点
	 */
	public  int everyDayBeginLimit;

	/**
	 * 每天定点限制，结束时间包含该点，如0代表0点
	 */
	public int everyDayEndLimit;

	/**
	 * 每周定点限制，开始时间 以距离周一0点的小时计算
	 */
	public  int everyWeekBeginLimit;

	/**
	 * 每周定点限制，结束时间 以距离周一0点的小时计算
	 */
	public  int everyWeekEndLimit;

	/**
	 * 每月定点限制，开始时间 以当月日期为计算，2标识当月2日
	 */
	public int everyMonthBeginLimit;

	/**
	 * 每月定点限制，结束时间 以当月日期为计算，2标识当月2日
	 */
	public  int everyMonthEndLimit;

	/**
	 * 固定时间限制，开始时间，以系统时间为计算
	 */
	public  long fixTimeBeginLimit;

	/**
	 * 固定时间限制，结束时间 以系统时间为计算
	 */
	public  long fixTimeEndLimit;

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
	
	/**
	 * 单个商品购买上限
	 */
	protected int goodMaxNumLimit;
	/**
	 * 商品购买后是否绑定
	 */
	private boolean buyBind;
	Shop shop;
	
	/**
	 * 全服剩余数量
	 */
	public  int overNum;
	
	private int jjcLevelLimit;
	
	public Goods(Shop shop) {
		this.shop = shop;
	}

	// //////////////////////////////////////////////////////////////////////////////////
	//
	// 内部数据，不需要编辑
	//

	/**
	 * 需要物品的描述，此描述有服务器自动产生，无需编辑器设置
	 */
	protected transient String exchangeArticleDescription = "";

	/**
	 * 此方法用于客户端中的小泡泡中
	 * 
	 * 用于描述商品的一些购买限制
	 */
	public String getDescription(Player player) {

		Goods g = this;
		StringBuffer sb = new StringBuffer();
		if (g.zhiwuLimit > 0) {

		}
		if (g.gongxunLimit > 0) {

		}
		if (g.jiazuGongxianLimit > 0) {

		}
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		Calendar cal = Calendar.getInstance();

		ShopManager sm = ShopManager.getInstance();

		if (g.totalSellNumLimit > ShopManager.NUM_LIMIT_NONE || g.timeLimitType != ShopManager.TIME_LIMIT_NONE || g.personSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
			if (g.在时间限制下的数量限制) {
				if (g.timeLimitType == ShopManager.TIME_LIMIT_NONE) {
					if (g.totalSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
						if (g.totalSellNum >= g.totalSellNumLimit) {
							String description = Translate.商品已经售完;
							sb.append("\n" + description);
						} else {
							String description = Translate.translateString(Translate.商品剩余个数, new String[][] { { Translate.COUNT_1, (g.totalSellNumLimit - g.totalSellNum) + "" } });
							sb.append("\n" + description);
						}
						if (g.personSellNumLimit > ShopManager.NUM_LIMIT_NONE) {

							SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(shop.id, this.id, articleName);
							long count = g.personSellNumLimit - sgd.getSelledNum(player.getId());
							String description = Translate.translateString(Translate.个人购买剩余个数, new String[][] { { Translate.COUNT_1, count + "" } });
							sb.append("\n" + description);
						}
					} else {
						if (g.personSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
							SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(shop.id, this.id, articleName);
							long count = g.personSellNumLimit - sgd.getSelledNum(player.getId());
							String description = Translate.translateString(Translate.个人购买剩余个数, new String[][] { { Translate.COUNT_1, count + "" } });
							sb.append("\n" + description);

						}
					}
				} else {
					if (g.timeLimitType == ShopManager.LIMIT_EVERYDAY_HOURS) {
						String description = Translate.translateString(Translate.购买时间每天, new String[][] { { Translate.STRING_1, g.everyDayBeginLimit + "" }, { Translate.STRING_2, g.everyDayEndLimit + "" } });
						sb.append("\n" + description);
					}
					if (g.timeLimitType == ShopManager.LIMIT_EVERYWEEK_DAYS) {
						int startWeekDay = g.everyWeekBeginLimit / 24;
						int startHour = g.everyWeekBeginLimit % 24;
						String startWeekDayStr = "";
						switch (startWeekDay) {
						case 0:
							startWeekDayStr = Translate.星期一;
							break;
						case 1:
							startWeekDayStr = Translate.星期二;
							break;
						case 2:
							startWeekDayStr = Translate.星期三;
							break;
						case 3:
							startWeekDayStr = Translate.星期四;
							break;
						case 4:
							startWeekDayStr = Translate.星期五;
							break;
						case 5:
							startWeekDayStr = Translate.星期六;
							break;
						case 6:
							startWeekDayStr = Translate.星期日;
							break;
						}
						int endWeekDay = g.everyWeekEndLimit / 24;
						int endHour = g.everyWeekEndLimit % 24;
						String endWeekDayStr = "";
						switch (endWeekDay) {
						case 0:
							endWeekDayStr = Translate.星期一;
							break;
						case 1:
							endWeekDayStr = Translate.星期二;
							break;
						case 2:
							endWeekDayStr = Translate.星期三;
							break;
						case 3:
							endWeekDayStr = Translate.星期四;
							break;
						case 4:
							endWeekDayStr = Translate.星期五;
							break;
						case 5:
							endWeekDayStr = Translate.星期六;
							break;
						case 6:
							endWeekDayStr = Translate.星期日;
							break;
						}
						String description = Translate.translateString(Translate.购买时间星期, new String[][] { { Translate.STRING_1, startWeekDayStr }, { Translate.STRING_2, startHour + "" }, { Translate.STRING_3, endWeekDayStr }, { Translate.STRING_4, endHour + "" } });
						sb.append("\n" + description);
					}
					if (g.timeLimitType == ShopManager.LIMIT_EVERYMONTH_DAYS) {
						String description = Translate.translateString(Translate.购买时间每月, new String[][] { { Translate.STRING_1, g.everyMonthBeginLimit + "" }, { Translate.STRING_2, g.everyMonthEndLimit + "" } });
						sb.append("\n" + description);
					}
					if (g.timeLimitType == ShopManager.LIMIT_FIX_TIME) {
						cal.setTimeInMillis(g.fixTimeBeginLimit);
						String startMonth = (cal.get(Calendar.MONTH) + 1) + "";
						String startDay = (cal.get(Calendar.DAY_OF_MONTH)) + "";
						String startHour = cal.get(Calendar.HOUR_OF_DAY) + "";
						String startMinute = cal.get(Calendar.MINUTE) + "";

						cal.setTimeInMillis(g.fixTimeEndLimit);
						String endMonth = (cal.get(Calendar.MONTH) + 1) + "";
						String endDay = (cal.get(Calendar.DAY_OF_MONTH)) + "";
						String endHour = cal.get(Calendar.HOUR_OF_DAY) + "";
						String endMinute = cal.get(Calendar.MINUTE) + "";

						String description = Translate.translateString(Translate.购买时间定点, new String[][] { { Translate.STRING_1, startMonth }, { Translate.STRING_2, startDay }, { Translate.STRING_3, startHour }, { Translate.STRING_4, startMinute }, { Translate.STRING_5, endMonth }, { Translate.STRING_6, endDay }, { Translate.STRING_7, endHour }, { Translate.STRING_8, endMinute } });
						sb.append("\n" + description);
					}

					if (g.totalSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
						if (g.totalSellNum >= g.totalSellNumLimit) {
							String description = Translate.商品已经售完;
							sb.append("\n" + description);
						} else {
							String description = Translate.translateString(Translate.商品剩余个数, new String[][] { { Translate.COUNT_1, (g.totalSellNumLimit - g.totalSellNum) + "" } });
							sb.append("\n" + description);
						}
						if (g.personSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
							SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(shop.id, this.id, articleName);
							long count = g.personSellNumLimit - sgd.getSelledNum(player.getId());
							String description = Translate.translateString(Translate.个人购买剩余个数, new String[][] { { Translate.COUNT_1, count + "" } });
							sb.append("\n" + description);

						}

					} else {
						if (g.personSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
							SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(shop.id, this.id, articleName);
							long count = g.personSellNumLimit - sgd.getSelledNum(player.getId());
							String description = Translate.translateString(Translate.个人购买剩余个数, new String[][] { { Translate.COUNT_1, count + "" } });
							sb.append("\n" + description);

						}
					}
				}
			} else {
				if (g.timeLimitType != ShopManager.TIME_LIMIT_NONE) {
					if (g.timeLimitType == ShopManager.LIMIT_EVERYDAY_HOURS) {
						String description = Translate.translateString(Translate.购买时间每天, new String[][] { { Translate.STRING_1, g.everyDayBeginLimit + "" }, { Translate.STRING_2, g.everyDayEndLimit + "" } });
						sb.append("\n" + description);
					}
					if (g.timeLimitType == ShopManager.LIMIT_EVERYWEEK_DAYS) {
						int startWeekDay = g.everyWeekBeginLimit / 24;
						int startHour = g.everyWeekBeginLimit % 24;
						String startWeekDayStr = "";
						switch (startWeekDay) {
						case 0:
							startWeekDayStr = Translate.星期一;
							break;
						case 1:
							startWeekDayStr = Translate.星期二;
							break;
						case 2:
							startWeekDayStr = Translate.星期三;
							break;
						case 3:
							startWeekDayStr = Translate.星期四;
							break;
						case 4:
							startWeekDayStr = Translate.星期五;
							break;
						case 5:
							startWeekDayStr = Translate.星期六;
							break;
						case 6:
							startWeekDayStr = Translate.星期日;
							break;
						}
						int endWeekDay = g.everyWeekEndLimit / 24;
						int endHour = g.everyWeekEndLimit % 24;
						String endWeekDayStr = "";
						switch (endWeekDay) {
						case 0:
							endWeekDayStr = Translate.星期一;
							break;
						case 1:
							endWeekDayStr = Translate.星期二;
							break;
						case 2:
							endWeekDayStr = Translate.星期三;
							break;
						case 3:
							endWeekDayStr = Translate.星期四;
							break;
						case 4:
							endWeekDayStr = Translate.星期五;
							break;
						case 5:
							endWeekDayStr = Translate.星期六;
							break;
						case 6:
							endWeekDayStr = Translate.星期日;
							break;
						}
						String description = Translate.translateString(Translate.购买时间星期, new String[][] { { Translate.STRING_1, startWeekDayStr }, { Translate.STRING_2, startHour + "" }, { Translate.STRING_3, endWeekDayStr }, { Translate.STRING_4, endHour + "" } });
						sb.append("\n" + description);
					}
					if (g.timeLimitType == ShopManager.LIMIT_EVERYMONTH_DAYS) {
						String description = Translate.translateString(Translate.购买时间每月, new String[][] { { Translate.STRING_1, g.everyMonthBeginLimit + "" }, { Translate.STRING_2, g.everyMonthEndLimit + "" } });
						sb.append("\n" + description);
					}
					if (g.timeLimitType == ShopManager.LIMIT_FIX_TIME) {
						cal.setTimeInMillis(g.fixTimeBeginLimit);
						String startMonth = (cal.get(Calendar.MONTH) + 1) + "";
						String startDay = cal.get(Calendar.DAY_OF_MONTH) + "";
						String startHour = cal.get(Calendar.HOUR_OF_DAY) + "";
						String startMinute = cal.get(Calendar.MINUTE) + "";

						cal.setTimeInMillis(g.fixTimeEndLimit);
						String endMonth = (cal.get(Calendar.MONTH) + 1) + "";
						String endDay = cal.get(Calendar.DAY_OF_MONTH) + "";
						String endHour = cal.get(Calendar.HOUR_OF_DAY) + "";
						String endMinute = cal.get(Calendar.MINUTE) + "";

						String description = Translate.translateString(Translate.购买时间定点, new String[][] { { Translate.STRING_1, startMonth }, { Translate.STRING_2, startDay }, { Translate.STRING_3, startHour }, { Translate.STRING_4, startMinute }, { Translate.STRING_5, endMonth }, { Translate.STRING_6, endDay }, { Translate.STRING_7, endHour }, { Translate.STRING_8, endMinute } });
						sb.append("\n" + description);
					}
					if (g.totalSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
						if (g.totalSellNum >= g.totalSellNumLimit) {
							String description = Translate.商品已经售完;
							sb.append("\n" + description);
						} else {
							String description = Translate.translateString(Translate.商品剩余个数, new String[][] { { Translate.COUNT_1, (g.totalSellNumLimit - g.totalSellNum) + "" } });
							sb.append("\n" + description);
						}
						if (g.personSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
							SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(shop.id, this.id, articleName);
							long count = g.personSellNumLimit - sgd.getSelledNum(player.getId());
							String description = Translate.translateString(Translate.个人购买剩余个数, new String[][] { { Translate.COUNT_1, count + "" } });
							sb.append("\n" + description);

						}
					} else {
						if (g.personSellNumLimit > ShopManager.NUM_LIMIT_NONE) {
							SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(shop.id, this.id, articleName);
							long count = g.personSellNumLimit - sgd.getSelledNum(player.getId());
							String description = Translate.translateString(Translate.个人购买剩余个数, new String[][] { { Translate.COUNT_1, count + "" } });
							sb.append("\n" + description);

						}
					}
				}
			}
		}
		return sb.toString();
	}

	// 总量购买多少个
	public long totalSellNum = 0;

	// 总量,最后刷新的时间
	public long totalSellNumLastFlushTime = 0;

	public long getTotalSellNumLastFlushTime() {
		return totalSellNumLastFlushTime;
	}

	public long getTotalSellNum() {
		return totalSellNum;
	}

	public void setTotalSellNum(long totalSellNum) {
		this.totalSellNum = totalSellNum;
		dirty = true;
	}

	public void setTotalSellNumLastFlushTime(long totalSellNumLastFlushTime) {
		this.totalSellNumLastFlushTime = totalSellNumLastFlushTime;
		dirty = true;
	}

	// 每个玩家购买，还剩余多少，以及最后刷新的时间
	// Hashtable中，key为玩家id，value为数组，数组第一个值为还剩余多少，第二个值为最后刷新的时间
	public Hashtable<Long, long[]> selledPersonGoodsData_ = new Hashtable<Long, long[]>();

	public boolean dirty;

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

	public boolean isBuyBind() {
		return buyBind;
	}

	public void setBuyBind(boolean buyBind) {
		this.buyBind = buyBind;
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
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < exchangeArticleNames.length; i++) {
			sb.append(exchangeArticleNames[i] + "(" + exchangeArticleNums[i] + ")");
			if (i < exchangeArticleNames.length - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	public void setExchangeArticleDescription(String exchangeArticleDescription) {
		this.exchangeArticleDescription = exchangeArticleDescription;
	}

	public int getOverNum() {
		return overNum;
	}

	public void setOverNum(int overNum) {
		this.overNum = overNum;
	}

	public Hashtable<Long, long[]> getSelledPersonGoodsData_() {
		return selledPersonGoodsData_;
	}

	public void setSelledPersonGoodsData_(
			Hashtable<Long, long[]> selledPersonGoodsData_) {
		this.selledPersonGoodsData_ = selledPersonGoodsData_;
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

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
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

	public void setLimitReputeName(String limitReputeName) {
		this.limitReputeName = limitReputeName;
	}

	public byte getLimitReputeLevel() {
		return limitReputeLevel;
	}

	public void setLimitReputeLevel(byte limitReputeLevel) {
		this.limitReputeLevel = limitReputeLevel;
	}

	public int getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(int oldPrice) {
		this.oldPrice = oldPrice;
	}

	public int getColorForClient() {
		ArticleManager am = ArticleManager.getInstance();
		Article a = am.getArticle(articleName);
		if (a instanceof Equipment) {
			return ArticleManager.color_equipment[color];
		} else if (a instanceof MagicWeapon) {
			return ArticleManager.color_magicweapon[color];
		}else {
			return ArticleManager.color_article_2[color];
		}
	}

	public int getColor() {
		return this.color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public int getGoodMaxNumLimit() {
		return goodMaxNumLimit;
	}

	public void setGoodMaxNumLimit(int goodMaxNumLimit) {
		this.goodMaxNumLimit = goodMaxNumLimit;
	}

	public String getLittleSellIcon() {
		return littleSellIcon;
	}

	public void setLittleSellIcon(String littleSellIcon) {
		this.littleSellIcon = littleSellIcon;
	}

	public static void main(String[] args) {
		// Calendar cal = Calendar.getInstance();
		// String startMonth = (cal.get(Calendar.MONTH) + 1) + "";
		// String startDay = (cal.get(Calendar.DAY_OF_MONTH)) + "";
		// String startHour = cal.get(Calendar.HOUR_OF_DAY) + "";
		// String startMinute = cal.get(Calendar.MINUTE) + "";
		// System.out.println(startMonth+"月"+startDay+"日"+startHour+"时"+startMinute+"分");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int hourOfWeek = dayOfWeek * 24 + hourOfDay;
		System.out.println("hourOfWeek:" + hourOfWeek);
		System.out.println("hourOfDay:" + hourOfDay);
		System.out.println("dayOfWeek:" + dayOfWeek);
	}

	public boolean playerCansee(Player player, Calendar cal) {
		if (timeLimitType == ShopManager.TIME_LIMIT_NONE) {
			return true;
		}
		switch (timeLimitType) {
		case ShopManager.LIMIT_EVERYDAY_HOURS:
			cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
			if (hourOfDay < this.everyDayBeginLimit || hourOfDay > this.everyDayEndLimit) {
				return false;
			}
			break;
		case ShopManager.LIMIT_EVERYWEEK_DAYS:
			cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			int hourOfWeek = dayOfWeek * 24 + hourOfDay;
			if (hourOfWeek < this.everyWeekBeginLimit || hourOfWeek > this.everyWeekEndLimit) {
				return false;
			}
			break;
		case ShopManager.LIMIT_EVERYMONTH_DAYS:
			cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
			if (dayOfMonth < this.everyMonthBeginLimit || dayOfMonth > this.everyMonthEndLimit) {
				return false;
			}
			break;
		case ShopManager.LIMIT_FIX_TIME:
			if (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() < this.fixTimeBeginLimit || com.fy.engineserver.gametime.SystemTime.currentTimeMillis() > this.fixTimeEndLimit) {
				return false;
			}
			break;

		default:
			break;
		}
		return true;
	}

	public String getArticleName_stat() {
		return articleName_stat;
	}

	public void setArticleName_stat(String articleName_stat) {
		this.articleName_stat = articleName_stat;
	}

	public int getJjcLevelLimit() {
		return this.jjcLevelLimit;
	}

	public void setJjcLevelLimit(int jjcLevelLimit) {
		this.jjcLevelLimit = jjcLevelLimit;
	}
	
}
