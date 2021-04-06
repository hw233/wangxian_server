package com.fy.engineserver.menu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager;
import com.fy.engineserver.activity.newChongZhiActivity.NewXiaoFeiActivity;
import com.fy.engineserver.activity.shop.ShopActivityManager;
import com.fy.engineserver.activity.vipExpActivity.VipExpActivityManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.InlayArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.article.manager.DiscountCardAgent;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.shop.Goods;
import com.fy.engineserver.shop.SelledGoodsData;
import com.fy.engineserver.shop.Shop;
import com.fy.engineserver.shop.ShopManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.tengxun.TengXunDataManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.datacheck.MailEventManger;
import com.fy.engineserver.util.datacheck.event.MailEvent;
import com.fy.engineserver.util.datacheck.handler.GoodsPriceLimit;
import com.fy.engineserver.util.datacheck.handler.GoodsPriceLimitManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.stat.model.PlayerActionFlow;

/**
 * 进入商店
 * 
 * 
 *
 */
public class Option_BuyBindGoodsToShop extends Option{

	protected Shop shop;
	protected Player player;
	protected int goodsId;
	protected int amount;
	protected int moneyType;
	protected double discountRate;
	
	public Shop getShop() {
		return shop;
	}


	public Player getPlayer() {
		return player;
	}


	public void setPlayer(Player player) {
		this.player = player;
	}


	public int getGoodsId() {
		return goodsId;
	}


	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}


	public int getAmount() {
		return amount;
	}


	public void setAmount(int amount) {
		this.amount = amount;
	}


	public int getMoneyType() {
		return moneyType;
	}


	public void setMoneyType(int moneyType) {
		this.moneyType = moneyType;
	}


	public double getDiscountRate() {
		return discountRate;
	}


	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}


	public void setShop(Shop shop) {
		this.shop = shop;
	}


	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		ShopManager sm = ShopManager.getInstance();
		Goods g = shop.getGoodsById(goodsId);
		ShopManager.logger.warn("[用户购买商品] [购买绑定] ["+player.getName()+"] [amount:"+amount+"] [moneyType"+moneyType+"] [g:"+g.getPayType()+"]");
		if(g.getGoodMaxNumLimit() > 0 && amount > g.getGoodMaxNumLimit()){
			if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [失败] [购买数量超过了物品设置的最大上限] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:--] [购买数量:{}]", new Object[] { player.getUsername(), player.getName(), shop.getName(), goodsId, amount });

			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_5627);
			player.addMessageToRightBag(req);
			return;
		}
		
		if (amount < 1 || amount > 100) {
			if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [失败] [输入数量有错] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:--] [购买数量:{}]", new Object[] { player.getUsername(), player.getName(), shop.getName(), goodsId, amount });

			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_5626);
			player.addMessageToRightBag(req);
			return;
		}

		ArticleManager articleManager = ArticleManager.getInstance();
		ArticleEntityManager articleEntityManager = ArticleEntityManager.getInstance();

		Article article = articleManager.getArticle(g.getArticleName());

		if (article == null) {
			if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [失败] [无法找到商品对应的物品] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });

			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_5628 + g.getArticleName() + Translate.text_5629);
			player.addMessageToRightBag(req);
			return;
		}
		
		synchronized (g) {

			long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			Calendar cal = Calendar.getInstance();
			ShopManager.logger.warn("[用户购买商品] [购买绑定] ["+player.getName()+"] [gname:"+g.getArticleName()+"] [总共卖出："+g.totalSellNum+"] [在时间限制下的数量限制:"+g.在时间限制下的数量限制+"] [全服限购："+g.getTotalSellNumLimit() +"] [时间限制类型:"+g.getTimeLimitType() +"] [个人限购："+g.getPersonSellNumLimit()+"] [剩余数量："+g.getOverNum()+"] [支付类型："+g.getPayType()+"] [===========]");
			if (g.getTotalSellNumLimit()  > ShopManager.NUM_LIMIT_NONE || g.getTimeLimitType() != ShopManager.TIME_LIMIT_NONE || g.getPersonSellNumLimit()  > ShopManager.NUM_LIMIT_NONE) {
				shop.flushGoods(now, cal, g, player);
				if(g.getTotalSellNumLimit() > 0 && g.getTotalSellNumLimit() - g.totalSellNum >= 0){
					g.overNum= (int)(g.getTotalSellNumLimit() - g.totalSellNum);
				}
				
				if (g.在时间限制下的数量限制) {
					if (g.getTimeLimitType() == ShopManager.TIME_LIMIT_NONE) {
						if (g.getTotalSellNumLimit() > ShopManager.NUM_LIMIT_NONE) {
							if (g.totalSellNum >= g.getTotalSellNumLimit()) {
								String description = Translate.商品已经售完;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
								return;
							} else if (g.totalSellNum + amount > g.getTotalSellNumLimit()) {
								String description = Translate.购买数量超过限制;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
								return;
							}
							if (g.getPersonSellNumLimit() > ShopManager.NUM_LIMIT_NONE) {
								SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(shop.getId(), g.id, g.getArticleName());
								long sellNum = sgd.getSelledNum(player.getId());

								if (sellNum >= g.getPersonSellNumLimit()) {
									String description = Translate.您不能再购买了;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
									return;
								} else if (sellNum + amount > g.getPersonSellNumLimit()) {
									String description = Translate.购买数量超过限制;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
									return;
								}

							}
						} else {
							if (g.getPersonSellNumLimit() > ShopManager.NUM_LIMIT_NONE) {
								SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(shop.getId(), g.id, g.getArticleName());
								long sellNum = sgd.getSelledNum(player.getId());

								if (sellNum >= g.getPersonSellNumLimit()) {
									String description = Translate.您不能再购买了;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
									return;
								} else if (sellNum + amount > g.getPersonSellNumLimit()) {
									String description = Translate.购买数量超过限制;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
									return;
								}

							}
						}
					} else {
						if (g.getTimeLimitType() == ShopManager.LIMIT_EVERYDAY_HOURS) {
							cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
							int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
							if (hourOfDay < g.everyDayBeginLimit || hourOfDay > g.everyDayEndLimit) {
								String description = Translate.购买时间还没到;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
								return;
							}
						}
						if (g.getTimeLimitType() == ShopManager.LIMIT_EVERYWEEK_DAYS) {
							cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
							int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
							int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
							int hourOfWeek = dayOfWeek * 24 + hourOfDay;
							if (hourOfWeek < g.everyWeekBeginLimit || hourOfWeek > g.everyWeekEndLimit) {
								String description = Translate.购买时间还没到;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
								return;
							}
						}
						if (g.getTimeLimitType() == ShopManager.LIMIT_EVERYMONTH_DAYS) {
							cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
							int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
							if (dayOfMonth < g.everyMonthBeginLimit || dayOfMonth > g.everyMonthEndLimit) {
								String description = Translate.购买时间还没到;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
								return;
							}
						}
						if (g.getTimeLimitType() == ShopManager.LIMIT_FIX_TIME) {
							if (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() < g.fixTimeBeginLimit || com.fy.engineserver.gametime.SystemTime.currentTimeMillis() > g.fixTimeEndLimit) {
								String description = Translate.购买时间还没到;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
								return;
							}
						}

						if (g.getTotalSellNumLimit() > ShopManager.NUM_LIMIT_NONE) {
							if (g.totalSellNum >= g.getTotalSellNumLimit()) {
								String description = Translate.商品已经售完;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
								return;
							} else if (g.totalSellNum + amount > g.getTotalSellNumLimit()) {
								String description = Translate.购买数量超过限制;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
								return;
							}
							if (g.getPersonSellNumLimit() > ShopManager.NUM_LIMIT_NONE) {
								SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(shop.getId(), g.id, g.getArticleName());
								long sellNum = sgd.getSelledNum(player.getId());
								if (sellNum >= g.getPersonSellNumLimit()) {
									String description = Translate.您不能再购买了;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
									return;
								} else if (sellNum + amount > g.getPersonSellNumLimit()) {
									String description = Translate.购买数量超过限制;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
									return;
								}
							}
						} else {
							if (g.getPersonSellNumLimit() > ShopManager.NUM_LIMIT_NONE) {
								SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(shop.getId(), g.id, g.getArticleName());
								long sellNum = sgd.getSelledNum(player.getId());

								if (sellNum >= g.getPersonSellNumLimit()) {
									String description = Translate.您不能再购买了;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
									return;
								} else if (sellNum + amount > g.getPersonSellNumLimit()) {
									String description = Translate.购买数量超过限制;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
									return;
								}
							}
						}
					}
				} else {
					if (g.getTimeLimitType() != ShopManager.TIME_LIMIT_NONE) {
						if (g.getTimeLimitType() == ShopManager.LIMIT_EVERYDAY_HOURS) {
							cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
							int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
							if (hourOfDay < g.everyDayBeginLimit || hourOfDay > g.everyDayEndLimit) {
								String description = Translate.购买时间还没到;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
								return;
							}
						}
						if (g.getTimeLimitType() == ShopManager.LIMIT_EVERYWEEK_DAYS) {
							cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
							int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
							int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
							int hourOfWeek = dayOfWeek * 24 + hourOfDay;
							if (hourOfWeek < g.everyWeekBeginLimit || hourOfWeek > g.everyWeekEndLimit) {
								String description = Translate.购买时间还没到;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
								return;
							}
						}
						if (g.getTimeLimitType() == ShopManager.LIMIT_EVERYMONTH_DAYS) {
							cal.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
							int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
							if (dayOfMonth < g.everyMonthBeginLimit || dayOfMonth > g.everyMonthEndLimit) {
								String description = Translate.购买时间还没到;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
								return;
							}
						}
						if (g.getTimeLimitType() == ShopManager.LIMIT_FIX_TIME) {
							if (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() < g.fixTimeBeginLimit || com.fy.engineserver.gametime.SystemTime.currentTimeMillis() > g.fixTimeEndLimit) {
								String description = Translate.购买时间还没到;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
								return;
							}
						}
						if (g.getTotalSellNumLimit() > ShopManager.NUM_LIMIT_NONE) {
							if (g.totalSellNum >= g.getTotalSellNumLimit()) {
								String description = Translate.商品已经售完;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
								return;
							} else if (g.totalSellNum + amount > g.getTotalSellNumLimit()) {
								String description = Translate.购买数量超过限制;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
								return;
							}
							if (g.getPersonSellNumLimit() > ShopManager.NUM_LIMIT_NONE) {
								SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(shop.getId(), g.getId(), g.getArticleName());
								long sellNum = sgd.getSelledNum(player.getId());

								if (sellNum >= g.getPersonSellNumLimit()) {
									String description = Translate.您不能再购买了;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
									return;
								} else if (sellNum + amount > g.getPersonSellNumLimit()) {
									String description = Translate.购买数量超过限制;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
									return;
								}

							}
						} else {
							if (g.getPersonSellNumLimit() > ShopManager.NUM_LIMIT_NONE) {
								SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(shop.getId(), g.id, g.getArticleName());
								long sellNum = sgd.getSelledNum(player.getId());

								if (sellNum >= g.getPersonSellNumLimit()) {
									String description = Translate.您不能再购买了;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足每人购买个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
									return;
								} else if (sellNum + amount > g.getPersonSellNumLimit()) {
									String description = Translate.购买数量超过限制;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [不满足个数限制] [{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { description, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });
									return;
								}

							}
						}
					}
				}
			}
			ShopManager.logger.warn("[用户购买商品] [购买绑定] ["+player.getName()+"] [gname:"+g.getArticleName()+"] [总共卖出："+g.totalSellNum+"] [在时间限制下的数量限制:"+g.在时间限制下的数量限制+"] [全服限购："+g.getTotalSellNumLimit()+"] [时间限制类型:"+g.getTimeLimitType()+"] [个人限购："+g.getPersonSellNumLimit()+"] [剩余数量："+g.getOverNum()+"] [支付类型："+g.getPayType()+"] [===========2]");
			List<ArticleEntity> elist = new ArrayList<ArticleEntity>();
			if (article.isOverlap()) {
				ArticleEntity entity = null;
				try {
					boolean bind = false;
					if (moneyType == 1) {
						bind = true;
					}
					if (g.isBuyBind()) {
						bind = true;
						if (ShopManager.logger.isInfoEnabled()) {
							ShopManager.logger.info("[购买商品] [商品设置为绑定] [" + shop.getName() + "] [" + g.getId() + "," + g.getArticleName() + "]");
						}
					}
					entity = articleEntityManager.createEntity(article, shop.isBuyBinded || bind, ArticleEntityManager.CREATE_REASON_SHOP_SELL, player, g.getColor(), amount * g.getBundleNum(), false);
					for (int i = 0; i < amount * g.getBundleNum(); i++) {
						elist.add(entity);
					}
				} catch (Exception ex) {
					if (ShopManager.logger.isErrorEnabled()) ShopManager.logger.error("[用户购买商品] [异常] [用户:" + player.getLogString() + "] [" + shop.getName() + "] [" + g.getArticleName() + "] [一捆数量:" + g.getArticleName() + "] [购买数量:" + amount + "]", ex);
					HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.服务器物品出现错误);
					player.addMessageToRightBag(req);
					return;
				}
			} else {
				for (int i = 0; i < amount; i++) {
					for (int j = 0; j < g.getBundleNum(); j++) {
						try {
							ArticleEntity entity = null;
							// if(article instanceof Equipment){
							// Equipment e = (Equipment)article;
							// if(SpecialEquipmentManager.getInstance().isSpecialEquipment(e)){
							// entity = SpecialEquipmentManager.getInstance().createEntity(e,ArticleEntityManager.CREATE_REASON_SHOP_SELL);
							// SpecialEquipmentManager.ShopManager.logger.warn("[购买了混沌万灵榜装备] ["+player.getLogString()+"] ["+e.getName()+"] [entity:"+(entity != null ? entity.getId() :
							// null)+"]");
							// }else{
							// entity = articleEntityManager.createEntity(article, shop.isBuyBinded, ArticleEntityManager.CREATE_REASON_SHOP_SELL, player, g.getColor(), 1, false);
							// }
							// }else{
							boolean bind = false;
							if (moneyType == 1) {
								bind = true;
							}
							if (g.isBuyBind()) {
								bind = true;
								if (ShopManager.logger.isInfoEnabled()) {
									ShopManager.logger.info("[购买商品] [商品设置为绑定] [" + shop.getName() + "] [" + g.getId() + "," + g.getArticleName() + "]");
								}
							}
							
							entity = articleEntityManager.createEntity(article, shop.isBuyBinded || bind, ArticleEntityManager.CREATE_REASON_SHOP_SELL, player, g.getColor(), 1, false);
							// }
							if (entity != null) {
								elist.add(entity);
							}
						} catch (Exception ex) {
							if (ShopManager.logger.isErrorEnabled()) ShopManager.logger.error("[用户购买商品] [异常] [用户:" + player.getLogString() + "] [" + shop.getName() + "] [" + g.getArticleName() + "] [一捆数量:" + g.getArticleName() + "] [购买数量:" + amount + "]", ex);
							HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.服务器物品出现错误);
							player.addMessageToRightBag(req);
							return;
						}
					}
				}
			}

			if (elist.size() <= 0) {
				if (ShopManager.logger.isErrorEnabled()) ShopManager.logger.error("[用户购买商品] [异常，生成物品为空] [用户:" + player.getLogString() + "] [" + shop.getName() + "] [" + g.getArticleName() + "] [一捆数量:" + g.getArticleName() + "] [购买数量:" + amount + "]");
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.服务器物品出现错误);
				player.addMessageToRightBag(req);
				return;
			}

			// 测试用户的背包是否能放下物品
			if (!player.putAllOK(elist.toArray(new ArticleEntity[0]))) {
				if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [失败] [背包太满] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}]", new Object[] { player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount });

//				SHOP_BUY_RES res = new SHOP_BUY_RES(GameMessageFactory.nextSequnceNum(), getName(), goodsId, g.getDescription(player));
//				player.addMessageToRightBag(res);

				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.背包空间不足);
				player.addMessageToRightBag(req);
				for (ArticleEntity ae : elist) {
					articleEntityManager.removeTransientATable(ae);
				}
				return;
			}
			
			//在扣费给商品前判断有无打折卡
			if(discountRate >0 && discountRate <1){
				boolean havecard = DiscountCardAgent.getInstance().checkHaveDiscountCard(player, g.getArticleName(), amount * g.getBundleNum(), shop.getName(),g.getColor());
				if(!havecard){
					return;
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
					price = g.getPrice() / g.getBundleNum();
					long moneyPrice = g.getPrice();

					expense = (long) (moneyPrice * amount * discountRate);

					// 国战特殊处理
					if (player.isIsGuozhan() && player.getTodayCanUseBindSilver() < expense) {
						HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.国战状态下绑银不足或已达今日使用上限购买失败);
						player.addMessageToRightBag(req);
						return;
					}

					if (!player.bindSilverEnough(expense)) {
						// BillingCenter.银子不足时弹出充值确认框(player, "绑银不足，是否去充值");
						HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.绑银和银子都不足购买失败);
						player.addMessageToRightBag(req);
						return;
					}

					economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "");
				} else if (g.getPayType() == CurrencyType.YINZI) {
					if (moneyType == 0) {
						currencyType = CurrencyType.YINZI;
						price = g.getPrice() / g.getBundleNum();
						int yprice = g.getPrice();
						expense = (long) (yprice * amount * discountRate);
						if (shop.shopType == Shop.SHOP_TYPE_YUANBAO) {
							if (g.getPrice() == g.getOldPrice()) {
								expense = (long) (expense * TengXunDataManager.instance.getShopAgio2Buy(player));
							}
						}

						if (player.getSilver() < expense) {
							BillingCenter.银子不足时弹出充值确认框(player, Translate.银子不足是否去充值);
							return;
						}
						
						economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "", VipExpActivityManager.shop_expends_activity);
						
						if (shop.shopType == Shop.SHOP_TYPE_YUANBAO) {
							NewChongZhiActivityManager.instance.doXiaoFeiActivity(player, expense, NewXiaoFeiActivity.XIAOFEI_TYPE_SHOP);
//							ChongZhiActivity.getInstance().doXiaoFeiActivity(player, expense, XiaoFeiServerConfig.XIAOFEI_TONGDAO_SHANGCHENG);
//							ChongZhiActivity.getInstance().doXiaoFeiMoneyActivity(player, expense, MoneyBillBoardActivity.XIAOFEI_TONGDAO_SHANGCHENG);
						}
						// 活跃度统计
						ActivenessManager.getInstance().record(player, ActivenessType.商城购买道具);
					} else if (moneyType == 1) {
						currencyType = CurrencyType.SHOPYINZI;
						price = g.getPrice() / g.getBundleNum();
						long yprice = g.getPrice();
						expense = (long) (yprice * amount * discountRate);
						if (shop.shopType == Shop.SHOP_TYPE_YUANBAO) {
							if (g.getPrice() == g.getOldPrice()) {
								expense = (long) (expense * TengXunDataManager.instance.getShopAgio2Buy(player));
							}
						}
						if (player.getShopSilver() + player.getSilver() < expense) {
							BillingCenter.银子不足时弹出充值确认框(player, Translate.银子不足是否去充值);
							return;
						}
						if (shop.shopType == Shop.SHOP_TYPE_YUANBAO) {
							NewChongZhiActivityManager.instance.doXiaoFeiActivity(player, expense, NewXiaoFeiActivity.XIAOFEI_TYPE_SHOP);
//							ChongZhiActivity.getInstance().doXiaoFeiActivity(player, expense, XiaoFeiServerConfig.XIAOFEI_TONGDAO_SHANGCHENG);
//							ChongZhiActivity.getInstance().doXiaoFeiMoneyActivity(player, expense, MoneyBillBoardActivity.XIAOFEI_TONGDAO_SHANGCHENG);
						}
						economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "");
					}
				} else if (g.getPayType() == CurrencyType.GONGZI) {
					currencyType = CurrencyType.GONGZI;
					price = g.getPrice() / g.getBundleNum();
					long yprice = g.getPrice();
					expense = (long) (yprice * amount * discountRate);
					economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "");
				} else if (g.getPayType() == CurrencyType.ZIYUAN) {
					currencyType = CurrencyType.ZIYUAN;
					price = g.getPrice() / g.getBundleNum();
					long yprice = g.getPrice();
					expense = (long) (yprice * amount * discountRate);
					economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "");
				} else if (g.getPayType() == CurrencyType.BANGPAIZIJIN) {
					currencyType = CurrencyType.BANGPAIZIJIN;
					price = g.getPrice() / g.getBundleNum();
					long yprice = g.getPrice();
					expense = (long) (yprice * amount * discountRate);
					economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "");
				} else if (g.getPayType() == CurrencyType.LILIAN) {
					currencyType = CurrencyType.LILIAN;
					price = g.getPrice() / g.getBundleNum();
					long yprice = g.getPrice();
					expense = (long) (yprice * amount * discountRate);
					economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "");
				} else if (g.getPayType() == CurrencyType.GONGXUN) {
					currencyType = CurrencyType.GONGXUN;
					price = g.getPrice() / g.getBundleNum();
					long yprice = g.getPrice();
					expense = (long) (yprice * amount * discountRate);
					economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "");
				} else if (g.getPayType() == CurrencyType.WENCAI) {
					currencyType = CurrencyType.WENCAI;
					price = g.getPrice() / g.getBundleNum();
					long yprice = g.getPrice();
					expense = (long) (yprice * amount * discountRate);
					economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "");
				} else if (g.getPayType() == CurrencyType.ARTICLES) {
					synchronized (player.tradeKey) {
						for (int i = 0; i < g.exchangeArticleNames.length; i++) {
							int c = player.countArticleInKnapsacksByName(g.exchangeArticleNames[i]);
							if (c < g.exchangeArticleNums[i] * amount) {
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.物品个数不足);
								player.addMessageToRightBag(hreq);
								ShopManager.logger.warn("[用户购买商品] [失败] [个数不足] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [" + shop.getName() + "] [" + g.getArticleName() + "] [一捆数量:" + g.getBundleNum() + "] [购买数量:" + amount + "] [c:" + c + "]");
								return;
							}
						}
	
						for (int i = 0; i < g.exchangeArticleNames.length; i++) {
							int n = g.exchangeArticleNums[i] * amount;
							for (int j = 0; j < n; j++) {
								ArticleEntity ae = player.getArticleEntity(g.exchangeArticleNames[i]);
								if (ae != null) {
									ArticleEntity removeAe = player.removeArticleEntityFromKnapsackByArticleId(ae.getId(), "商店物品兑换" + g.getArticleName() + "删除", true);
									if (removeAe == null) {
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.删除物品不成功);
										player.addMessageToRightBag(hreq);
										ShopManager.logger.warn("[用户购买商品] [失败] [删除物品不成功] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [" + shop.getName() + "] [" + g.getArticleName() + "] [一捆数量:" + g.getBundleNum() + "] [购买数量:" + amount + "] [j:" + j + "] [n:" + n + "]");
										return;
									}
								}
							}
						}
					}
				}else if(g.getPayType() == CurrencyType.ACTIVENESS){
					currencyType = CurrencyType.ACTIVENESS;
					price = g.getPrice() / g.getBundleNum();
					int yprice = g.getPrice();
					expense = (int) (yprice * amount * discountRate);
					long activity = player.getActivenessInfo().getTotalActiveness();//pai.getTotalActiveness();
					if(activity < expense){
						player.sendError(Translate.活跃度不足是否去充值);
						return;
					}
					economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "");
				}else if(g.getPayType() == CurrencyType.JIFEN){
					currencyType = CurrencyType.JIFEN;
					price = g.getPrice() / g.getBundleNum();
					int yprice = g.getPrice();
					expense = (int) (yprice * amount * discountRate);
					if (player.getChargePoints() < expense) {
						if(PlatformManager.getInstance().isPlatformOf(Platform.韩国)){
							player.sendError(Translate.积分不足);
						}else{
							BillingCenter.银子不足时弹出充值确认框(player, Translate.积分不足是否去充值);
						}
						return;
					}
					economicCenter.playerExpense(player, expense, currencyType, ExpenseReasonType.SHOP_BUY, "");
				}else{
					return;
				}
				for (ArticleEntity ae : elist) {
					articleEntityManager.putToRealSaveCache(ae);
				}
//				ShopManager.logger.warn("[用户购买商品] [购买绑定] ["+player.getName()+"] [gname:"+g.getArticleName()+"] [总共卖出："+g.totalSellNum+"] [在时间限制下的数量限制:"+g.在时间限制下的数量限制+"] [全服限购："+g.getTotalSellNumLimit()+"] [时间限制类型:"+g.getTimeLimitType()+"] [个人限购："+g.getPersonSellNumLimit()+"] [剩余数量："+g.getOverNum()+"] [支付类型："+g.getPayType()+"] [===========支付完]");
			} catch (NoEnoughMoneyException e) {
				for (ArticleEntity ae : elist) {
					articleEntityManager.removeTransientATable(ae);
				}
				if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [购买绑定] [失败] [余额不足] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [" + shop.getName() + "] [" + g.getArticleName() + "] [一捆数量:" + g.getBundleNum() + "] [购买数量:" + amount + "]", e);

				String msg = Translate.text_5659;
				if (g != null && g.getPayType() < Translate.余额不足2.length) {
					msg = Translate.余额不足2[g.getPayType()];
				}
				
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, msg);
				player.addMessageToRightBag(req);

				return;

			} catch (BillFailedException e) {
				for (ArticleEntity ae : elist) {
					articleEntityManager.removeTransientATable(ae);
				}
				if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [购买绑定] [失败] [计费失败] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [" + shop.getName() + "] [" + g.getArticleName() + "] [一捆数量:" + g.getBundleNum() + "] [购买数量:" + amount + "]", e);

				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_5660);
				player.addMessageToRightBag(req);

				return;
			} catch (Exception e) {
				for (ArticleEntity ae : elist) {
					articleEntityManager.removeTransientATable(ae);
				}
				if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [购买绑定] [异常] [计费失败] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [" + shop.getName() + "] [" + g.getArticleName() + "] [一捆数量:" + g.getBundleNum() + "] [购买数量:" + amount + "]", e);

				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_5660);
				player.addMessageToRightBag(req);

				return;
			}
			ShopManager.logger.warn("[用户购买商品] [购买绑定] ["+player.getName()+"] [gname:"+g.getArticleName()+"] [总共卖出："+g.totalSellNum+"] [在时间限制下的数量限制:"+g.在时间限制下的数量限制+"] [全服限购："+g.getTotalSellNumLimit()+"] [时间限制类型:"+g.getTimeLimitType()+"] [个人限购："+g.getPersonSellNumLimit()+"] [剩余数量："+g.getOverNum()+"] [支付类型："+g.getPayType()+"] [===========支付完]");
			// 把商品放入背包
			player.putAll(elist.toArray(new ArticleEntity[0]), "商店购买");
			
			//数据检查
			GoodsPriceLimit gpl=GoodsPriceLimitManager.getInstance().getGoodPriceLimit(g.getArticleName_stat(), g.getColor());
			if(gpl!=null){
				ArticleManager.logger.error("[商店检查] [实际购买数量:"+elist.size()+"预警数量:"+gpl.getLimitNum()+"]");
				if(!(elist.size()<gpl.getLimitNum())){
					ArticleManager.logger.error("[商店检查] [报警]");
					MailEventManger.getInstance().addTask(MailEvent.商店检查.立即发送(new String[][]{{shop.getName_stat(),g.getArticleName_stat(),g.getColor()+"","出售数量预警","{角色名:"+player.getName()+"}{账号:"+player.getUsername()+"}{id:"+player.getId()+"}{等级:"+player.getLevel()+"}{vip等级:"+player.getVipLevel()+"}{"+"购买数量:"+elist.size()+"}"}}));
				}
			}

			// 统计
			if (elist.size() > 0) {
				ArticleStatManager.addToArticleStat(player, null, elist.get(0), ArticleStatManager.OPERATION_物品获得和消耗, price, (byte) g.getPayType(), elist.size(), "商店购买", shop.getName_stat());
			}
			// 数量
			g.totalSellNum = g.totalSellNum + amount;
			if(g.getTotalSellNumLimit() - g.totalSellNum >=0){
				g.overNum= (int)(g.getTotalSellNumLimit() - g.totalSellNum);
			}
			
			g.totalSellNumLastFlushTime = now;
			g.dirty = true;

			// 数量
			long configtime = 0;
			long allsellnums = 0;
			long limitSellNums = 0;
			
			try{
				if (g.getPersonSellNumLimit() > ShopManager.NUM_LIMIT_NONE) {
					SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(shop.getId(), g.id, g.getArticleName());
					sgd.addSelledNum(player.getId(), amount,g.getFixTimeBeginLimit(),g.getFixTimeEndLimit());
					sm.saveSelledGoodsData(shop.getId(), g.id, g.getArticleName(), sgd);
					configtime = sgd.getConfigEndTime();
					limitSellNums = sgd.getSelledNum(player.getId());
				}
				if(g.getTotalSellNumLimit() > ShopManager.NUM_LIMIT_NONE){
					SelledGoodsData sgd = sm.getSelledGoodsDataAndCreateIfNotExists(shop.getId(), g.id, g.getArticleName());
					sgd.setConfigStartTime(g.getFixTimeBeginLimit());
					sgd.addSelledNum(player.getId(), amount,g.getFixTimeBeginLimit(),g.getFixTimeEndLimit());
					sgd.setSelledGoodsCount(sgd.getSelledGoodsCount()+amount);
					sm.saveSelledGoodsData(shop.getId(), g.id, g.getArticleName(), sgd);
					sgd.setConfigEndTime(g.getFixTimeEndLimit());
					configtime = sgd.getConfigEndTime();
					allsellnums = sgd.getSelledGoodsCount();
					limitSellNums = sgd.getSelledNum(player.getId());
				}
			}catch(Exception e){
				e.printStackTrace();
				ShopManager.logger.warn("[用户购买商品] [购买绑定] ["+e+"]");
			}
			ShopManager.logger.warn("[用户购买商品] [购买绑定] ["+player.getName()+"] [gname:"+g.getArticleName()+"] [limitSellNums:"+limitSellNums+"] [allsellnums:"+allsellnums+"] [总共卖出："+g.totalSellNum+"] [在时间限制下的数量限制:"+g.在时间限制下的数量限制+"] [全服限购："+g.getTotalSellNumLimit()+"] [时间限制类型:"+g.getTimeLimitType()+"] [个人限购："+g.getPersonSellNumLimit()+"] [剩余数量："+g.getOverNum()+"] [支付类型："+g.getPayType()+"] ["+configtime+"] ["+g.getFixTimeEndLimit()+"] [绑定===========支付完2]");
//			SHOP_BUY_RES res = new SHOP_BUY_RES(GameMessageFactory.nextSequnceNum(), getName(), goodsId, g.getDescription(player));
//			player.addMessageToRightBag(res);

			if (elist.size() > 0) {
				ArticleEntity ae = elist.get(0);
				Article article2 = articleManager.getArticle(ae.getArticleName());
				if(article2 != null && article2 instanceof InlayArticle){
					InlayArticle aaArticle = (InlayArticle)article2;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.translateString(Translate.您购买了物品, new String[][] { { Translate.COUNT_1, (g.getBundleNum() * amount) + "" }, { Translate.STRING_1, aaArticle.getShowName()} }));
					player.addMessageToRightBag(hreq);
					Player.sendPlayerAction(player, PlayerActionFlow.行为类型_购买, aaArticle.getShowName() + "*" + (g.getBundleNum() * amount), 0, new Date(), GamePlayerManager.isAllowActionStat());
				}else{
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.translateString(Translate.您购买了物品, new String[][] { { Translate.COUNT_1, (g.getBundleNum() * amount) + "" }, { Translate.STRING_1, ae.getArticleName() } }));
					player.addMessageToRightBag(hreq);
					Player.sendPlayerAction(player, PlayerActionFlow.行为类型_购买, ae.getArticleName() + "*" + (g.getBundleNum() * amount), 0, new Date(), GamePlayerManager.isAllowActionStat());
				}
				
			}

			StringBuffer ss = new StringBuffer();
			if (g.getPayType() == CurrencyType.BIND_YINZI) {
				ss.append((long) (g.getPrice() * g.getBundleNum() * amount * discountRate) + Translate.绑定银子);
			} else if (g.getPayType() == CurrencyType.YINZI) {
				ss.append((long) (g.getPrice() * g.getBundleNum() * amount * discountRate) + Translate.非绑定银子);
			}

			// 使用飞行坐骑成功活动
			if (g.getPayType() == CurrencyType.ARTICLES) {
				try {
					CompoundReturn cr = ActivityManagers.getInstance().getValue(4, player);
					if (cr != null && cr.getBooleanValue()) {
						String servername = GameConstants.getInstance().getServerName();
						if (g.getArticleName().equals(Translate.八卦仙蒲)) {
							if (ActivityManagers.getInstance().getDdc().get(servername + Translate.仙蒲圣者) == null) {
								int[] keys = player.getKeyByTitle(Translate.仙蒲圣者);
								player.addTitle(keys[0], Translate.仙蒲圣者, keys[1],"",0, Translate.仙蒲圣者,"","", -1L);
								ActivityManagers.getInstance().getDdc().put(servername + Translate.仙蒲圣者, servername + "--" + player.getId());
								String msg = Translate.translateString(Translate.成功驾驭八卦仙蒲, new String[][] { { Translate.STRING_1, player.getName() } });
								player.send_HINT_REQ(msg, (byte) 2);
								ActivitySubSystem.logger.warn("[韩国消费送称号] [全服唯一] [领取成功] [坐骑:八卦仙蒲] [称号:仙蒲圣者] [" + player.getLogString() + "]");
							} else {
								ActivitySubSystem.logger.warn("[韩国消费送称号] [全服唯一] [已经领取过了] [坐骑:八卦仙蒲] [称号:仙蒲圣者] [" + player.getLogString() + "]");
							}
						} else if (g.getArticleName().equals(Translate.乾坤葫芦)) {
							if (ActivityManagers.getInstance().getDdc().get(servername + Translate.乾坤葫芦) == null) {
								int[] keys = player.getKeyByTitle(Translate.乾坤仙人);
								player.addTitle(keys[0], Translate.乾坤仙人, keys[1],"",0, Translate.乾坤仙人,"","",-1L);
								ActivityManagers.getInstance().getDdc().put(servername + Translate.乾坤葫芦, servername + "--" + player.getId());
								String msg = Translate.translateString(Translate.成功驾驭乾坤葫芦, new String[][] { { Translate.STRING_1, player.getName() } });
								player.send_HINT_REQ(msg, (byte) 2);
								ActivitySubSystem.logger.warn("[韩国消费送称号] [全服唯一] [领取成功] [坐骑:乾坤葫芦] [称号:乾坤仙人] [" + player.getLogString() + "]");
							} else {
								ActivitySubSystem.logger.warn("[韩国消费送称号] [全服唯一] [已经领取过了] [坐骑:乾坤葫芦] [称号:乾坤仙人] [" + player.getLogString() + "]");
							}
						} else if (g.getArticleName().equals(Translate.梦瞳仙鹤)) {
							if (ActivityManagers.getInstance().getDdc().get(servername + Translate.梦瞳仙鹤) == null) {
								int[] keys = player.getKeyByTitle(Translate.驭鹤尊者);
								player.addTitle(keys[0], Translate.驭鹤尊者, keys[1],"",0, Translate.驭鹤尊者,"","", -1L);
								ActivityManagers.getInstance().getDdc().put(servername + Translate.梦瞳仙鹤, servername + "--" + player.getId());
								String msg = Translate.translateString(Translate.成功驾驭梦瞳仙鹤, new String[][] { { Translate.STRING_1, player.getName() } });
								player.send_HINT_REQ(msg, (byte) 2);
								ActivitySubSystem.logger.warn("[韩国消费送称号] [全服唯一] [领取成功] [坐骑:梦瞳仙鹤] [称号:驭鹤尊者] [" + player.getLogString() + "]");
							} else {
								ActivitySubSystem.logger.warn("[韩国消费送称号] [全服唯一] [已经领取过了] [坐骑:梦瞳仙鹤] [称号:驭鹤尊者] [" + player.getLogString() + "]");
							}
						}
					}
				} catch (Exception e) {
					ActivitySubSystem.logger.warn("[韩国消费送称号] [异常] [" + e + "]");
				}

			}
			if (ShopManager.logger.isWarnEnabled()) ShopManager.logger.warn("[用户购买商品] [购买绑定] [成功] [OK] [花费：{}] [折扣:{}] [用户:{}] [角色:{}] [{}] [{}] [一捆数量:{}] [购买数量:{}] [全服限购:{}] [个人限购:{}] [剩余数量:{}]", new Object[] { ss.toString(), discountRate, player.getUsername(), player.getName(), shop.getName(), g.getArticleName(), g.getBundleNum(), amount ,g.getTotalSellNumLimit(),g.getPersonSellNumLimit(),g.getOverNum()});
			if (shop.getName() != null && shop.getName().equals(Translate.活跃商城)) {
				try {
					EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.活跃商城购买道具次数, 1L});
					EventRouter.getInst().addEvent(evt);
				} catch (Exception ee) {
					PlayerAimManager.logger.error("[目标系统] [统计玩家活跃度购买商品次数异常][" + player.getLogString() + "]", ee);
				}
			}

			{
				DiscountCardAgent.getInstance().noticeBuySuccess(player, g.getArticleName(), amount * g.getBundleNum(), shop.getName(),g.getColor());
				if (discountRate == 1) {
					ShopActivityManager.getInstance().noticeBuySuccess(player, shop, g, amount);
				}
			}
			return;

		}
	
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_卖商店;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return "";
	}
}
