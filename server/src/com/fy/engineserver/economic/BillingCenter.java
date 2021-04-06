package com.fy.engineserver.economic;

//import org.apache.log4j.Logger;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;
import com.fy.boss.authorize.model.Passport;
import com.fy.boss.client.BossClientService;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.activeness.PlayerActivenessInfo;
import com.fy.engineserver.activity.vipExpActivity.VipExpActivityManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.green.GreenServerManager;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.ResourceCollection;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_ChongZhi;
import com.fy.engineserver.menu.Option_Query_Activity;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.util.ServerWatchDog;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.server.TestServerConfigManager;
import com.sqage.stat.client.StatClientService;
import com.sqage.stat.model.GameChongZhiFlow;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 消费充值中心
 * 和用户账户金钱相关的操作统一通过本类的方法完成
 * 
 * 
 */
public class BillingCenter implements Runnable {
	public static Logger loggerA = LoggerFactory.getLogger(BillingCenter.class.getName());
	public static Logger loggerB = LoggerFactory.getLogger(BillingCenter.class.getName() + ".B");

	protected PlayerManager playerManager;

	protected BossClientService bossClientService;

	protected static BillingCenter self;
	
	/**
	 * 积分只有充值才能获得
	 * 1元获得几积分
	 */
	protected static int 充值送积分比例 = 60;
	public static Platform [] 开放老玩家送积分的平台 = {Platform.官方,Platform.台湾};
	public static Platform [] 开放充值送积分的平台 = {Platform.官方,Platform.台湾,Platform.腾讯};

	public static BillingCenter getInstance() {
		return self;
	}

	public void initialize() {
		
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		self = this;
		Thread t = new Thread(this, "BillingCenter");
		t.start();
		ServiceStartRecord.startLog(this);
	}

	public static String 得到带单位的银两(long money) {
		StringBuffer sb = new StringBuffer();
		long 文 = money % 1000;
		long 两 = money / 1000;
		两 = 两 % 1000;
		long 锭 = money / 1000000;
		if (锭 > 0) {
			sb.append(锭 + Translate.锭);
		}
		if (两 > 0) {
			sb.append(两 + Translate.两);
		}
		if (文 > 0) {
			sb.append(文 + Translate.文);
		}
		return sb.toString();
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public void setBossClientService(BossClientService bossClientService) {
		this.bossClientService = bossClientService;
	}

	/**
	 * 玩家充元宝,银子，绑定银子
	 * 值都充在角色上
	 * @param player
	 * @param saveAmount
	 * @param currencyType
	 * @see CurrencyType
	 * @param reasonType
	 * @see SavingReasonType
	 * @param description
	 *            描述
	 * @return
	 */
	public boolean playerSaving(Player player, long savingAmount, int currencyType, int reasonType, String description) throws SavingFailedException {
		long startTime = SystemTime.currentTimeMillis();
		if (player == null) {
			if (loggerA.isWarnEnabled()) loggerA.warn("[用户充值] [失败:玩家不存在] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:--] [服务器:" + GameConstants.getInstance().getServerName() + "] [充值金额:" + savingAmount + "] [地图名:--] [角色等级:--] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
			throw new SavingFailedException("玩家不存在，无法购买商品");
		}
		if (!CurrencyType.isValidCurrencyType(currencyType)) {
			loggerA.error("[用户充值] [失败:未知货币类型] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色ID:" + player.getId() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [充值金额:" + savingAmount + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
			throw new SavingFailedException("充值失败，未知货币");
		}
		if (!SavingReasonType.isValidReasonType(reasonType)) {
			loggerA.error("[用户充值] [失败:未知充值渠道] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色ID:" + player.getId() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [充值金额:" + savingAmount + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
			throw new SavingFailedException("充值失败，未知充值渠道");
		}
		if (savingAmount <= 0) {
			if (loggerA.isWarnEnabled()) loggerA.warn("[用户充值] [失败:充值额度为0] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色ID:" + player.getId() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [充值金额:" + savingAmount + "] [消费渠道:" + ExpenseReasonType.getExpenseReason(reasonType) + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
			return false;
		}
		boolean succ = false;
		long oldAmount = 0;
		long newAmount = 0;
		synchronized (player) {
			try {
				if (currencyType == CurrencyType.BIND_YINZI) {
					oldAmount = player.getBindSilver();
					player.setBindSilver(oldAmount + savingAmount);
					newAmount = player.getBindSilver();
				} else if (currencyType == CurrencyType.RMB_YUANBAO) {
					oldAmount = player.getRmbyuanbao();
					player.setRmbyuanbao(oldAmount + savingAmount);
					player.setTotalRmbyuanbao(player.getTotalRmbyuanbao() + savingAmount);
					newAmount = player.getRmbyuanbao();
				} else if (currencyType == CurrencyType.YINZI) {
					oldAmount = player.getSilver();
					player.setSilver(oldAmount + savingAmount);
					newAmount = player.getSilver();
					try {
						if(!GameConstants.getInstance().getServerName().contains("潘多拉"))
						{
							if(savingAmount/1000000 >= 1) {
								ServerWatchDog.getInstance().notifyAddYingzi(player.getId(), player.getName(), player.getUsername(), savingAmount, SavingReasonType.getSavingReason(reasonType)+"/"+description);
							}
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
				} else if (currencyType == CurrencyType.SHOPYINZI) {
					if (GreenServerManager.isBindYinZiServer()) {
						oldAmount = player.getShopSilver();
						player.setShopSilver(oldAmount + savingAmount);
						newAmount = player.getShopSilver();
					} else {
						return this.playerSaving(player, savingAmount, CurrencyType.YINZI, reasonType, description);
					}
				} else if (currencyType == CurrencyType.GONGZI) {
					oldAmount = player.getWage();
					player.setWage(oldAmount + savingAmount);
					newAmount = player.getWage();
				} else if (currencyType == CurrencyType.ZIYUAN) {
					// oldAmount = player.getRmbyuanbao();
					// player.setRmbyuanbao(oldAmount + savingAmount);
					// player.setTotalRmbyuanbao(player.getTotalRmbyuanbao()+savingAmount);
					// newAmount = player.getRmbyuanbao();
				} else if (currencyType == CurrencyType.GONGXUN) {
					oldAmount = player.getGongxun();
					player.setGongxun(oldAmount + savingAmount);
					newAmount = player.getGongxun();
				} else if (currencyType == CurrencyType.WENCAI) {
					oldAmount = player.getCulture();
					player.setCulture((int) (oldAmount + savingAmount));
					newAmount = player.getCulture();
				} else if (currencyType == CurrencyType.LILIAN) {
					oldAmount = player.getLilian();
					player.setLilian(oldAmount + savingAmount);
					newAmount = player.getLilian();
				}else if (currencyType == CurrencyType.ACTIVENESS) {
					PlayerActivenessInfo pai = player.getActivenessInfo();
					oldAmount = pai.getTotalActiveness();
					pai.setTotalActiveness((int)(oldAmount + savingAmount));
					newAmount = pai.getTotalActiveness();
				}
				succ = true;
			} catch (Exception e) {
				e.printStackTrace();
				if (loggerA.isWarnEnabled()) loggerA.warn("[用户充值] [失败:更新用户时发生异常] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色ID:" + player.getId() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [充值金额:" + savingAmount + "] [充值渠道:" + SavingReasonType.getSavingReason(reasonType) + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]", e);
			}
		}
		if (succ) {
			try {
				StatClientService statClientService = StatClientService.getInstance();

				GameChongZhiFlow gameChongZhiFlow = new GameChongZhiFlow();

				String channel = (player.getPassport() != null ? player.getPassport().getLastLoginChannel() : "无");

				gameChongZhiFlow.setAction(0); // 0 充值 ，1 消耗
				gameChongZhiFlow.setCurrencyType(CurrencyType.getCurrencyDesp(currencyType));
				Passport pp = player.getPassport();
				if (pp != null) {
					gameChongZhiFlow.setJixing(pp.getRegisterMobileOs());
				}
				gameChongZhiFlow.setFenQu(GameConstants.getInstance().getServerName());
				gameChongZhiFlow.setGame(CountryManager.得到国家名(player.getCountry()));
				gameChongZhiFlow.setGameLevel("" + player.getLevel());
				gameChongZhiFlow.setMoney(savingAmount);
				gameChongZhiFlow.setQuDao(channel);
				if (description != null && !description.trim().equals("")) {
					gameChongZhiFlow.setReasonType(SavingReasonType.getSavingReason(reasonType) + description);
				} else {
					gameChongZhiFlow.setReasonType(SavingReasonType.getSavingReason(reasonType));
				}

				gameChongZhiFlow.setTime(System.currentTimeMillis());
				gameChongZhiFlow.setUserName(player.getUsername());
				if (!TestServerConfigManager.isTestServer() ) {
					statClientService.sendGameChongZhiFlow("", gameChongZhiFlow);
				}

			} catch (Exception e) {
				e.printStackTrace();
				loggerA.error("[用户充值] [警告:发送统计日志时发生异常]  [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色ID:" + player.getId() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [充值金额:" + savingAmount + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]", e);
			}
			if (loggerA.isInfoEnabled()) loggerA.info("[用户充值] [成功] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色ID:" + player.getId() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [充值金额:" + savingAmount + "] [充值渠道:" + SavingReasonType.getSavingReason(reasonType) + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [账户变化:" + oldAmount + " -> " + newAmount + "] [" + description + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
			return true;
		}
		return false;
	}

	public Calendar calendar = Calendar.getInstance();
	/**
	 * 玩家消费
	 * 消费元宝，银子，绑定银子
	 * 优先消费角色上的元宝，再消费账户上的元宝
	 * @param player
	 *            玩家
	 * @param expenseAmount
	 *            消费额度
	 * @param currencyType
	 *            货币类型 @see CurrencyType
	 * @param reasonType
	 *            消费种类 @see ExpenseReasonType
	 * @param description
	 *            描述
	 * @throws NoEnoughMoneyException
	 *             用户钱不够
	 * @throws BillFailedException
	 *             计费失败
	 */
	public void playerExpense(Player player, long expenseAmount, int currencyType, int reasonType, String description) throws NoEnoughMoneyException, BillFailedException {
		playerExpense(player, expenseAmount, currencyType, reasonType, description, VipExpActivityManager.default_add_rmb_expense);
	}
	/**
	 * 玩家消费---------只有有韩服vip经验活动翻倍的活动消耗类型使用此方法
	 * 消费元宝，银子，绑定银子
	 * 优先消费角色上的元宝，再消费账户上的元宝
	 * @param player
	 *            玩家
	 * @param expenseAmount
	 *            消费额度
	 * @param currencyType
	 *            货币类型 @see CurrencyType
	 * @param reasonType
	 *            消费种类 @see ExpenseReasonType
	 * @param description
	 *            描述
	 * @throws NoEnoughMoneyException
	 *             用户钱不够
	 * @throws BillFailedException
	 *             计费失败
	 */
	public void playerExpense(Player player, long expenseAmount, int currencyType, int reasonType, String description, int addType) throws NoEnoughMoneyException, BillFailedException {
		long startTime = SystemTime.currentTimeMillis();
		if (player == null) {
			if (loggerA.isWarnEnabled()) loggerA.warn("[用户消费] [失败:玩家不存在] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [消费金额:" + expenseAmount + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
			throw new BillFailedException("玩家不存在，无法购买商品");
		}
		if (!CurrencyType.isValidCurrencyType(currencyType)) {
			loggerA.error("[用户消费] [失败:未知货币类型] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [消费金额:" + expenseAmount + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
			throw new BillFailedException("消费失败，未知货币");
		}
		if (!ExpenseReasonType.isValidReasonType(reasonType)) {
			loggerA.error("[用户消费] [失败:未知消费渠道] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [消费金额:" + expenseAmount + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
			throw new BillFailedException("消费失败，未知消费渠道");
		}
		if (expenseAmount <= 0) {
			if (loggerA.isWarnEnabled()) loggerA.warn("[用户消费] [失败:消费额度为0] [" + CurrencyType.getCurrencyDesp(currencyType) + "] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [消费金额:" + expenseAmount + "] [消费渠道:" + ExpenseReasonType.getExpenseReason(reasonType) + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
			throw new BillFailedException("消费失败，输入的数量错误:" + expenseAmount + "," + player.getLogString());
		}
		boolean billed = false;
		long balance = -1;
		long oldAmount = 0;
		long newAmount = 0;
		// 绑银不够时银子转化的绑银
		long silver_change_othersilver = 0;
		long oldSilver = 0;
		long newSilver = 0;
		// 绑银不够时商城银子的转化
		long shopsilver_changer_bindSilver = 0;
		long oldshopsilver = 0;
		long newshopsilver = 0;
		synchronized (player) {
			if (currencyType == CurrencyType.BIND_YINZI) {
				long todayCanUseBindSilver = player.getTodayCanUseBindSilver();
				oldAmount = player.getBindSilver();
				oldSilver = player.getSilver();
				oldshopsilver = player.getShopSilver();
				// 今天可以使用的绑银不够，用银子代替
				if (todayCanUseBindSilver < expenseAmount) {
					if (todayCanUseBindSilver + player.getSilver() + player.getShopSilver() < expenseAmount) {
						if (loggerA.isWarnEnabled()) loggerA.warn("[用户消费] [失败:可用绑银和银子之和余额不足] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [消费金额:" + expenseAmount + "] [消费渠道:" + ExpenseReasonType.getExpenseReason(reasonType) + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
						throw new NoEnoughMoneyException("消费失败，用户余额不足");
					}
				}
				if (todayCanUseBindSilver >= expenseAmount) {
					player.setBindSilver(player.getBindSilver() - expenseAmount);
					player.setTodayUsedBindSilver(player.getTodayUsedBindSilver() + expenseAmount);
				} else {
					player.setBindSilver(player.getBindSilver() - todayCanUseBindSilver);
					if (expenseAmount - todayCanUseBindSilver > player.getShopSilver()) {
						if (player.getShopSilver() == 0) {
							silver_change_othersilver = expenseAmount - todayCanUseBindSilver;
							player.setSilver(player.getSilver() - silver_change_othersilver);
//							if(silver_change_othersilver > 0 && CostBillboardManager.getInstance().isEffectCost(reasonType)){
//								CostBillboardManager.getInstance().addCostRecord(player, CostType.总的消费, silver_change_othersilver);
//							}
//							if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) { 			//韩服vip经验改为消费增加
//								if(addType > 0) {
//									long totalRmb = player.getRMB();
//									long addRmb = (long) (silver_change_othersilver * 880 / 1000L);
//									totalRmb += addRmb;
//									player.setRMB4Activity(totalRmb, addRmb, addType);
//								}
//							}
							player.setTodayUsedBindSilver(player.getTodayUsedBindSilver() + todayCanUseBindSilver);
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.translateString(Translate.您的可用绑银不足消耗银子, new String[][] { { Translate.COUNT_1, 得到带单位的银两(silver_change_othersilver) } }));
							player.addMessageToRightBag(hreq);
						} else {
							shopsilver_changer_bindSilver = player.getShopSilver();
							player.setShopSilver(0);
							player.setTodayUsedBindSilver(player.getTodayUsedBindSilver() + todayCanUseBindSilver);
							silver_change_othersilver = expenseAmount - todayCanUseBindSilver - shopsilver_changer_bindSilver;
							player.setSilver(player.getSilver() - silver_change_othersilver);
//							if(silver_change_othersilver > 0 && CostBillboardManager.getInstance().isEffectCost(reasonType)){
//								CostBillboardManager.getInstance().addCostRecord(player, CostType.总的消费, silver_change_othersilver);
//							}
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.translateString(Translate.您的可用绑银不足消耗商城银子和银子, new String[][] { { Translate.COUNT_1, 得到带单位的银两(shopsilver_changer_bindSilver) }, { Translate.COUNT_2, 得到带单位的银两(silver_change_othersilver) } }));
							player.addMessageToRightBag(hreq);
						}
					} else {
						shopsilver_changer_bindSilver = expenseAmount - todayCanUseBindSilver;
						player.setShopSilver(player.getShopSilver() - shopsilver_changer_bindSilver);
						player.setTodayUsedBindSilver(player.getTodayUsedBindSilver() + todayCanUseBindSilver);
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.translateString(Translate.您的可用绑银不足消耗商城银子, new String[][] { { Translate.COUNT_1, 得到带单位的银两(shopsilver_changer_bindSilver) } }));
						player.addMessageToRightBag(hreq);
					}
				}
				billed = true;
				newAmount = player.getBindSilver();
				newSilver = player.getSilver();
				newshopsilver = player.getShopSilver();
			} else if (currencyType == CurrencyType.YINZI) {
				long bindyuanbao = player.getSilver();
				oldAmount = bindyuanbao;
				if (bindyuanbao < expenseAmount) {
					if (loggerA.isWarnEnabled()) loggerA.warn("[用户消费] [失败:余额不足] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [消费金额:" + expenseAmount + "] [消费渠道:" + ExpenseReasonType.getExpenseReason(reasonType) + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
					throw new NoEnoughMoneyException("消费失败，用户余额不足");
				}
				player.setSilver(bindyuanbao - expenseAmount);
				billed = true;
				newAmount = player.getSilver();
//				if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) { 			//韩服vip经验改为消费增加
//					if(addType > 0) {
//						long totalRmb = player.getRMB();
//						long addRmb = (long) (expenseAmount * 880 / 1000L);
//						totalRmb += addRmb;
//						player.setRMB4Activity(totalRmb, addRmb, addType);
//					}
//				}
//				if(CostBillboardManager.getInstance().isEffectCost(reasonType)){
//					CostBillboardManager.getInstance().addCostRecord(player, CostType.总的消费, expenseAmount);
//				}
				
			} else if (currencyType == CurrencyType.SHOPYINZI) {
				if (GreenServerManager.isBindYinZiServer()) {
					long shopsilver = player.getShopSilver();
					oldAmount = shopsilver;
					if (shopsilver < expenseAmount) {
						silver_change_othersilver = expenseAmount - shopsilver;
						oldSilver = player.getSilver();
						if (expenseAmount - shopsilver > oldSilver) {
							if (loggerA.isWarnEnabled()) loggerA.warn("[用户消费] [失败:可用商城银子和银子之和余额不足] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [消费金额:" + expenseAmount + "] [消费渠道:" + ExpenseReasonType.getExpenseReason(reasonType) + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
							throw new NoEnoughMoneyException("消费失败，用户余额不足");
						}
						player.setShopSilver(0);
						player.setSilver(player.getSilver() - silver_change_othersilver);
//						if(silver_change_othersilver > 0 && CostBillboardManager.getInstance().isEffectCost(reasonType)){
//							CostBillboardManager.getInstance().addCostRecord(player, CostType.总的消费, silver_change_othersilver);
//						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.translateString(Translate.您的可用商城银子不足消耗银子, new String[][] { { Translate.COUNT_1, 得到带单位的银两(silver_change_othersilver) } }));
						player.addMessageToRightBag(hreq);
					} else {
						player.setShopSilver(oldAmount - expenseAmount);
					}
					billed = true;
					newAmount = player.getShopSilver();
					newSilver = player.getSilver();
				} else {
					playerExpense(player, expenseAmount, CurrencyType.YINZI, reasonType, description, addType);
					return;
				}
			} else if (currencyType == CurrencyType.GONGZI) {
				long bindyuanbao = player.getWage();
				oldAmount = bindyuanbao;
				if (bindyuanbao < expenseAmount) {
					if (loggerA.isWarnEnabled()) loggerA.warn("[用户消费] [失败:余额不足] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [消费金额:" + expenseAmount + "] [消费渠道:" + ExpenseReasonType.getExpenseReason(reasonType) + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
					throw new NoEnoughMoneyException("消费失败，用户余额不足");
				}
				player.setWage(bindyuanbao - expenseAmount);
				billed = true;
				newAmount = player.getWage();
			} else if (currencyType == CurrencyType.ZIYUAN) {
				// 资源是一次扣除3中资源
				FaeryManager fm = FaeryManager.getInstance();
				if (fm != null) {
					Cave cave = fm.getCave(player);
					if (cave != null) {
						ResourceCollection rc = cave.getCurrRes();
						if (rc != null) {
							ResourceCollection rcTemp = new ResourceCollection((int) expenseAmount, (int) expenseAmount, (int) expenseAmount);
							if (rc.isEnough(rcTemp)) {
								rc.deduct(rcTemp);
								cave.notifyFieldChange("currRes");
								billed = true;
								//通知玩家资源变了
								cave.noticeResource();
								
							} else {
								if (loggerA.isWarnEnabled()) loggerA.warn("[用户消费] [失败:余额不足] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [消费金额:" + expenseAmount + "] [消费渠道:" + ExpenseReasonType.getExpenseReason(reasonType) + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
								throw new NoEnoughMoneyException("消费失败，用户余额不足");
							}
						} else {
							if (loggerA.isWarnEnabled()) loggerA.warn("[用户消费] [失败:资源为空] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [消费金额:" + expenseAmount + "] [消费渠道:" + ExpenseReasonType.getExpenseReason(reasonType) + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
							throw new NoEnoughMoneyException("消费失败，用户余额不足");
						}
					} else {
						if (loggerA.isWarnEnabled()) loggerA.warn("[用户消费] [失败:家园为空] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [消费金额:" + expenseAmount + "] [消费渠道:" + ExpenseReasonType.getExpenseReason(reasonType) + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
						throw new NoEnoughMoneyException("消费失败，用户余额不足");
					}
				} else {
					if (loggerA.isWarnEnabled()) loggerA.warn("[用户消费] [失败:家园管理为空] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [消费金额:" + expenseAmount + "] [消费渠道:" + ExpenseReasonType.getExpenseReason(reasonType) + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
					throw new NoEnoughMoneyException("消费失败，用户余额不足");
				}

				// oldAmount = bindyuanbao;
				// if(bindyuanbao < expenseAmount) {
				// loggerA.warn("[用户消费] [失败:余额不足] [货币类型:"+CurrencyType.getCurrencyDesp(currencyType)+"] [用户:"+player.getUsername()+"] [角色:"+player.getName()+"] [服务器:"+GameConstants.getInstance().getServerName()+"] [消费金额:"+expenseAmount+"] [消费渠道:"+ExpenseReasonType.getExpenseReason(reasonType)+"] [地图名:"+player.getMapName()+"] [角色等级:"+player.getLevel()+"] ["+(SystemTime.currentTimeMillis()
				// - startTime)+"]");
				// throw new NoEnoughMoneyException("消费失败，用户余额不足");
				// }
				// player.setBindSilver(bindyuanbao-expenseAmount);
				// billed = true;
				// newAmount = player.getBindSilver();
			} else if (currencyType == CurrencyType.LILIAN) {
				long bindyuanbao = player.getLilian();
				oldAmount = bindyuanbao;
				if (bindyuanbao < expenseAmount) {
					if (loggerA.isWarnEnabled()) loggerA.warn("[用户消费] [失败:余额不足] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [消费金额:" + expenseAmount + "] [消费渠道:" + ExpenseReasonType.getExpenseReason(reasonType) + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
					throw new NoEnoughMoneyException("消费失败，用户余额不足");
				}
				player.setLilian(bindyuanbao - expenseAmount);
				billed = true;
				newAmount = player.getLilian();
			} else if (currencyType == CurrencyType.GONGXUN) {
				long bindyuanbao = player.getGongxun();
				oldAmount = bindyuanbao;
				if (bindyuanbao < expenseAmount) {
					if (loggerA.isWarnEnabled()) loggerA.warn("[用户消费] [失败:余额不足] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [消费金额:" + expenseAmount + "] [消费渠道:" + ExpenseReasonType.getExpenseReason(reasonType) + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
					throw new NoEnoughMoneyException("消费失败，用户余额不足");
				}
				player.setGongxun(bindyuanbao - expenseAmount);
				billed = true;
				newAmount = player.getGongxun();
			} else if (currencyType == CurrencyType.WENCAI) {
				long bindyuanbao = player.getCulture();
				oldAmount = bindyuanbao;
				if (bindyuanbao < expenseAmount) {
					if (loggerA.isWarnEnabled()) loggerA.warn("[用户消费] [失败:余额不足] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [消费金额:" + expenseAmount + "] [消费渠道:" + ExpenseReasonType.getExpenseReason(reasonType) + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
					throw new NoEnoughMoneyException("消费失败，用户余额不足");
				}
				player.setCulture((int) (bindyuanbao - expenseAmount));
				billed = true;
				newAmount = player.getCulture();
			}else if(currencyType == CurrencyType.ACTIVENESS){
//				PlayerActivenessInfo pai = ActivenessManager.getInstance().getPlayerActivenessInfoFromDB(player);
				long activity = player.getActivenessInfo().getTotalActiveness();//pai.getTotalActiveness();
				oldAmount = activity;
				if (activity < expenseAmount) {
					if(loggerA.isWarnEnabled())
						loggerA.warn("[用户消费] [失败:余额不足ACTIVENESS] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [消费金额:" + expenseAmount + "] [消费渠道:" + ExpenseReasonType.getExpenseReason(reasonType) + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
					throw new NoEnoughMoneyException("消费失败，用户余额不足");
				}
				player.getActivenessInfo().setTotalActiveness((int)(activity - expenseAmount));
//				pai.setTotalActiveness((int)(activity - expenseAmount));
				billed = true;
				newAmount = player.getActivenessInfo().getTotalActiveness();
			}else if(currencyType == CurrencyType.JIFEN){
				long chargePoints = player.getChargePoints();
				oldAmount = chargePoints;
				if (chargePoints < expenseAmount) {
					if(loggerA.isWarnEnabled())
						loggerA.warn("[用户消费] [失败:余额不足JIFEN] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [消费金额:" + expenseAmount + "] [消费渠道:" + ExpenseReasonType.getExpenseReason(reasonType) + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
					throw new NoEnoughMoneyException("消费失败，用户余额不足");
				}
				player.setChargePoints(chargePoints - expenseAmount);
				billed = true;
				newAmount = player.getChargePoints();
				
				try
				{
					StatClientService statClientService = StatClientService.getInstance();

					GameChongZhiFlow gameChongZhiFlow = new GameChongZhiFlow();

					String channel4points = (player.getPassport() != null ? player.getPassport().getLastLoginChannel() : "无");

					gameChongZhiFlow.setAction(1); // 0 充值 ，1 消耗 2 老用户获得积分
					gameChongZhiFlow.setCurrencyType(CurrencyType.getCurrencyDesp(CurrencyType.JIFEN));
					Passport pp = player.getPassport();
					if (pp != null) {
						gameChongZhiFlow.setJixing(pp.getRegisterMobileOs());
					}
					gameChongZhiFlow.setFenQu(GameConstants.getInstance().getServerName());
					gameChongZhiFlow.setGame(CountryManager.得到国家名(player.getCountry()));
					gameChongZhiFlow.setGameLevel("" + player.getLevel());
					gameChongZhiFlow.setMoney(expenseAmount);
					gameChongZhiFlow.setQuDao(channel4points);
					String desc = "消耗积分";
					gameChongZhiFlow.setReasonType(desc);

					gameChongZhiFlow.setTime(System.currentTimeMillis());
					gameChongZhiFlow.setUserName(player.getUsername());
					if (!TestServerConfigManager.isTestServer() ) {
						statClientService.sendGameChongZhiFlow("", gameChongZhiFlow);
						ActivitySubSystem.logger.warn("[发统计消耗积分] [成功] ["+player.getId()+"] [积分:"+( expenseAmount)+"] ["+player.getUsername()+"] ["+player.getName()+"]");
					}
					
					
				}
				catch(Exception e)
				{
					ActivitySubSystem.logger.error("[发统计消耗积分出现异常] ["+player.getId()+"] [积分:"+( expenseAmount)+"] ["+player.getUsername()+"] ["+player.getName()+"]",e);
				}
				
				
			}
			else if(currencyType == CurrencyType.CROSS_POINT){
			long jjcPoints = player.getHonorPoint();
			oldAmount = jjcPoints;
			if (jjcPoints < expenseAmount) {
				if(loggerA.isWarnEnabled())
					loggerA.warn("[用户消费] [失败:余额不足CROSS_POINT] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [消费金额:" + expenseAmount + "] [消费渠道:" + ExpenseReasonType.getExpenseReason(reasonType) + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
				throw new NoEnoughMoneyException("消费失败，用户余额不足");
			}
			player.setHonorPoint(jjcPoints - expenseAmount,true);
			billed = true;
			newAmount = player.getHonorPoint();
			try
			{
				StatClientService statClientService = StatClientService.getInstance();
				GameChongZhiFlow gameChongZhiFlow = new GameChongZhiFlow();
				String channel4points = (player.getPassport() != null ? player.getPassport().getLastLoginChannel() : "无");
				gameChongZhiFlow.setAction(1); // 0 充值 ，1 消耗 2 老用户获得积分
				gameChongZhiFlow.setCurrencyType(CurrencyType.getCurrencyDesp(CurrencyType.CROSS_POINT));
				Passport pp = player.getPassport();
				if (pp != null) {
					gameChongZhiFlow.setJixing(pp.getRegisterMobileOs());
				}
				gameChongZhiFlow.setFenQu(GameConstants.getInstance().getServerName());
				gameChongZhiFlow.setGame(CountryManager.得到国家名(player.getCountry()));
				gameChongZhiFlow.setGameLevel("" + player.getLevel());
				gameChongZhiFlow.setMoney(expenseAmount);
				gameChongZhiFlow.setQuDao(channel4points);
				String desc = "消耗跨服积分";
				gameChongZhiFlow.setReasonType(desc);
				gameChongZhiFlow.setTime(System.currentTimeMillis());
				gameChongZhiFlow.setUserName(player.getUsername());
				if (!TestServerConfigManager.isTestServer()) {
					statClientService.sendGameChongZhiFlow("", gameChongZhiFlow);
					ActivitySubSystem.logger.warn("[发统计消耗跨服积分] [成功] ["+player.getId()+"] [积分:"+( expenseAmount)+"] ["+player.getUsername()+"] ["+player.getName()+"]");
				}
				
			}
			catch(Exception e)
			{
				ActivitySubSystem.logger.error("[发统计消耗跨服积分出现异常] ["+player.getId()+"] [积分:"+( expenseAmount)+"] ["+player.getUsername()+"] ["+player.getName()+"]",e);
			}
			 			}
		}
		
		if (billed) {
			try {
				StatClientService statClientService = StatClientService.getInstance();

				String channel = (player.getPassport() != null ? player.getPassport().getLastLoginChannel() : "无");

				if (currencyType == CurrencyType.BIND_YINZI && (silver_change_othersilver != 0 || shopsilver_changer_bindSilver != 0)) {
					GameChongZhiFlow gameChongZhiFlow = new GameChongZhiFlow();
					Passport pp = player.getPassport();
					if (pp != null) {
						gameChongZhiFlow.setJixing(pp.getRegisterMobileOs());
					}
					gameChongZhiFlow.setAction(1); // 0 充值 ，1 消耗
					gameChongZhiFlow.setCurrencyType(CurrencyType.getCurrencyDesp(currencyType));
					gameChongZhiFlow.setFenQu(GameConstants.getInstance().getServerName());
					gameChongZhiFlow.setGame(CountryManager.得到国家名(player.getCountry()));
					gameChongZhiFlow.setGameLevel("" + player.getLevel());
					gameChongZhiFlow.setMoney(expenseAmount - silver_change_othersilver - shopsilver_changer_bindSilver);
					gameChongZhiFlow.setQuDao(channel);
					if (description != null && !description.trim().equals("")) {
						gameChongZhiFlow.setReasonType(ExpenseReasonType.getExpenseReason(reasonType) + description);
					} else {
						gameChongZhiFlow.setReasonType(ExpenseReasonType.getExpenseReason(reasonType));
					}
					gameChongZhiFlow.setTime(System.currentTimeMillis());
					gameChongZhiFlow.setUserName(player.getUsername());

					if (!TestServerConfigManager.isTestServer() ) {
						statClientService.sendGameChongZhiFlow("", gameChongZhiFlow);
					}

					if (silver_change_othersilver != 0) {
						gameChongZhiFlow.setAction(1); // 0 充值 ，1 消耗
						gameChongZhiFlow.setCurrencyType(CurrencyType.getCurrencyDesp(CurrencyType.YINZI));
						gameChongZhiFlow.setFenQu(GameConstants.getInstance().getServerName());
						gameChongZhiFlow.setGame(CountryManager.得到国家名(player.getCountry()));
						gameChongZhiFlow.setGameLevel("" + player.getLevel());
						gameChongZhiFlow.setMoney(silver_change_othersilver);
						gameChongZhiFlow.setQuDao(channel);
						if (description != null && !description.trim().equals("")) {
							gameChongZhiFlow.setReasonType("绑银不足" + ExpenseReasonType.getExpenseReason(reasonType) + description);
						} else {
							gameChongZhiFlow.setReasonType("绑银不足" + ExpenseReasonType.getExpenseReason(reasonType));
						}

						gameChongZhiFlow.setTime(System.currentTimeMillis());
						gameChongZhiFlow.setUserName(player.getUsername());

						if (!TestServerConfigManager.isTestServer() ) {
							statClientService.sendGameChongZhiFlow("", gameChongZhiFlow);
						}
					}
					if (shopsilver_changer_bindSilver != 0) {
						gameChongZhiFlow.setAction(1); // 0 充值 ，1 消耗
						gameChongZhiFlow.setCurrencyType(CurrencyType.getCurrencyDesp(CurrencyType.SHOPYINZI));
						gameChongZhiFlow.setFenQu(GameConstants.getInstance().getServerName());
						gameChongZhiFlow.setGame(CountryManager.得到国家名(player.getCountry()));
						gameChongZhiFlow.setGameLevel("" + player.getLevel());
						gameChongZhiFlow.setMoney(shopsilver_changer_bindSilver);
						gameChongZhiFlow.setQuDao(channel);
						if (description != null && !description.trim().equals("")) {
							gameChongZhiFlow.setReasonType("绑银不足" + ExpenseReasonType.getExpenseReason(reasonType) + description);
						} else {
							gameChongZhiFlow.setReasonType("绑银不足" + ExpenseReasonType.getExpenseReason(reasonType));
						}

						gameChongZhiFlow.setTime(System.currentTimeMillis());
						gameChongZhiFlow.setUserName(player.getUsername());
						if (!TestServerConfigManager.isTestServer() ) {
							statClientService.sendGameChongZhiFlow("", gameChongZhiFlow);
						}
					}
				} else if (currencyType == CurrencyType.SHOPYINZI && silver_change_othersilver != 0) {
					GameChongZhiFlow gameChongZhiFlow = new GameChongZhiFlow();
					Passport pp = player.getPassport();
					if (pp != null) {
						gameChongZhiFlow.setJixing(pp.getRegisterMobileOs());
					}
					gameChongZhiFlow.setAction(1); // 0 充值 ，1 消耗
					gameChongZhiFlow.setCurrencyType(CurrencyType.getCurrencyDesp(currencyType));
					gameChongZhiFlow.setFenQu(GameConstants.getInstance().getServerName());
					gameChongZhiFlow.setGame(CountryManager.得到国家名(player.getCountry()));
					gameChongZhiFlow.setGameLevel("" + player.getLevel());
					gameChongZhiFlow.setMoney(expenseAmount - silver_change_othersilver);
					gameChongZhiFlow.setQuDao(channel);
					if (description != null && !description.trim().equals("")) {
						gameChongZhiFlow.setReasonType(ExpenseReasonType.getExpenseReason(reasonType) + description);
					} else {
						gameChongZhiFlow.setReasonType(ExpenseReasonType.getExpenseReason(reasonType));
					}
					gameChongZhiFlow.setTime(System.currentTimeMillis());
					gameChongZhiFlow.setUserName(player.getUsername());
					if (!TestServerConfigManager.isTestServer() ) {
						statClientService.sendGameChongZhiFlow("", gameChongZhiFlow);
					}

					if (silver_change_othersilver != 0) {
						gameChongZhiFlow.setAction(1); // 0 充值 ，1 消耗
						gameChongZhiFlow.setCurrencyType(CurrencyType.getCurrencyDesp(CurrencyType.YINZI));
						gameChongZhiFlow.setFenQu(GameConstants.getInstance().getServerName());
						gameChongZhiFlow.setGame(CountryManager.得到国家名(player.getCountry()));
						gameChongZhiFlow.setGameLevel("" + player.getLevel());
						gameChongZhiFlow.setMoney(silver_change_othersilver);
						gameChongZhiFlow.setQuDao(channel);
						if (description != null && !description.trim().equals("")) {
							gameChongZhiFlow.setReasonType("银票不够" + ExpenseReasonType.getExpenseReason(reasonType) + description);
						} else {
							gameChongZhiFlow.setReasonType("银票不够" + ExpenseReasonType.getExpenseReason(reasonType));
						}

						gameChongZhiFlow.setTime(System.currentTimeMillis());
						gameChongZhiFlow.setUserName(player.getUsername());
						if (!TestServerConfigManager.isTestServer() ) {
							statClientService.sendGameChongZhiFlow("", gameChongZhiFlow);
						}
					}
				} else {
					GameChongZhiFlow gameChongZhiFlow = new GameChongZhiFlow();
					Passport pp = player.getPassport();
					if (pp != null) {
						gameChongZhiFlow.setJixing(pp.getRegisterMobileOs());
					}
					gameChongZhiFlow.setAction(1); // 0 充值 ，1 消耗
					gameChongZhiFlow.setCurrencyType(CurrencyType.getCurrencyDesp(currencyType));
					gameChongZhiFlow.setFenQu(GameConstants.getInstance().getServerName());
					gameChongZhiFlow.setGame(CountryManager.得到国家名(player.getCountry()));
					gameChongZhiFlow.setGameLevel("" + player.getLevel());
					gameChongZhiFlow.setMoney(expenseAmount - silver_change_othersilver);
					gameChongZhiFlow.setQuDao(channel);
					if (description != null && !description.trim().equals("")) {
						gameChongZhiFlow.setReasonType(ExpenseReasonType.getExpenseReason(reasonType) + description);
					} else {
						gameChongZhiFlow.setReasonType(ExpenseReasonType.getExpenseReason(reasonType));
					}

					gameChongZhiFlow.setTime(System.currentTimeMillis());
					gameChongZhiFlow.setUserName(player.getUsername());
					if (!TestServerConfigManager.isTestServer() ) {
						statClientService.sendGameChongZhiFlow("", gameChongZhiFlow);
					}
				}

				if (currencyType == CurrencyType.BIND_YINZI && (silver_change_othersilver != 0 || shopsilver_changer_bindSilver != 0)) {
					if (loggerA.isInfoEnabled()) loggerA.info("[用户消费] [成功] [" + CurrencyType.getCurrencyDesp(currencyType) + "和银子] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "和银子] [用户:" + player.getUsername() + "] [角色ID:" + player.getId() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [消费金额:" + expenseAmount + "] [消费渠道:" + ExpenseReasonType.getExpenseReason(reasonType) + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [账户变化:绑银:" + oldAmount + " -> " + newAmount + "商城银子:" + oldshopsilver + "->" + newshopsilver + "银子:" + oldSilver + " -> " + newSilver + "] [" + description + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
				} else if (currencyType == CurrencyType.SHOPYINZI && silver_change_othersilver != 0) {
					if (loggerA.isInfoEnabled()) loggerA.info("[用户消费] [成功] [" + CurrencyType.getCurrencyDesp(currencyType) + "和银子] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "和银子] [用户:" + player.getUsername() + "] [角色ID:" + player.getId() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [消费金额:" + expenseAmount + "] [消费渠道:" + ExpenseReasonType.getExpenseReason(reasonType) + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [账户变化:商城银子:" + oldAmount + " -> " + newAmount + "银子:" + oldSilver + " -> " + newSilver + "] [" + description + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
				} else {
					if (loggerA.isInfoEnabled()) loggerA.info("[用户消费] [成功] [" + CurrencyType.getCurrencyDesp(currencyType) + "] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色ID:" + player.getId() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [消费金额:" + expenseAmount + "] [消费渠道:" + ExpenseReasonType.getExpenseReason(reasonType) + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [账户变化:" + oldAmount + " -> " + newAmount + "] [" + description + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]");
				}
				return;
			} catch (Exception e) {
				e.printStackTrace();
				loggerA.error("[用户消费] [失败:更新用户数据异常]  [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [充值金额:" + expenseAmount + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "] [" + (SystemTime.currentTimeMillis() - startTime) + "]", e);
			}
		} else {
			if (loggerA.isWarnEnabled()) loggerA.warn("[用户消费] [失败:扣费不成功] [货币类型:" + CurrencyType.getCurrencyDesp(currencyType) + "] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [消费金额:" + expenseAmount + "] [消费渠道:" + ExpenseReasonType.getExpenseReason(reasonType) + "] [地图名:" + player.getMapName() + "] [角色等级:" + player.getLevel() + "]  [" + (SystemTime.currentTimeMillis() - startTime) + "]");
			throw new BillFailedException("消费失败，扣费不成功");
		}
	}

	public static void 银子不足时弹出充值确认框(Player player, String description) {
		if (player == null) {
			return;
		}
		if (!EnterLimitManager.canChongZhi(player.getConn())) {
			player.sendError("您银子不足。");
			loggerA.warn("[不能查看充值的MD5] + " + player.getLogString());
			return;
		}
		String str = description;
		if (description == null || description.trim().equals("")) {
			str = Translate.银子不足是否去充值;
		}
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		mw.setDescriptionInUUB(str);
		Option_ChongZhi option = new Option_ChongZhi();
		option.setText(Translate.去充值);
		Option_Cancel cancel = new Option_Cancel();
		cancel.setText(Translate.取消);
		Option[] options = new Option[] { option, cancel };
		mw.setOptions(options);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		player.addMessageToRightBag(creq);
	}
	
	public static void 活跃度不足时弹出充值确认框(Player player, String description){
		if(player == null){
			return;
		}
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		mw.setDescriptionInUUB(description);
		Option_Query_Activity option = new Option_Query_Activity();
		option.setText(Translate.确定);
		Option[] options = new Option[] { option };
		mw.setOptions(options);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		player.addMessageToRightBag(creq);
	}

	public void run() {
	}

}
