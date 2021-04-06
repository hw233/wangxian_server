package com.fy.engineserver.economic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.client.BossClientService;
import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.dailyTurnActivity.DailyTurnManager;
import com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.charge.ChargeCardConfig;
import com.fy.engineserver.economic.charge.ChargeManager;
import com.fy.engineserver.economic.charge.ChargeMode;
import com.fy.engineserver.economic.charge.ChargeRatio;
import com.fy.engineserver.message.FIRST_CHARGE_STATE_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.stat.GameStatClientService;
import com.fy.engineserver.util.ServerWatchDog;
import com.fy.engineserver.util.server.TestServerConfigManager;
import com.sqage.stat.client.StatClientService;
import com.sqage.stat.model.GameChongZhiFlow;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.text.StringUtil;

public class GameServerSavingNotifyService extends HttpServlet {

	public static Logger logger = LoggerFactory.getLogger(GameServerSavingNotifyService.class);

	public static List<String> whiteAddress;
	private final String privateKey = "12345asdfg7y6ydsudkf8HHGsds44loiu";
	public static final String FAIL_DEFAULT_CHANNEL = "YOUAI_XUNXIAN";

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		if (whiteAddress == null) {
			whiteAddress = new ArrayList<String>();
		}
		whiteAddress.add("119.254.154.201");// 韩服
		//System.out.println("[初始化充值通知接口成功] [" + GameServerSavingNotifyService.class.getName() + "]");
	}

	public static int koreaRate = 440;

	public List<String> getWhiteAddress() {
		return whiteAddress;
	}

	public void setWhiteAddress(List<String> whiteAddress) {
		this.whiteAddress = whiteAddress;
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		String remoteAddr = req.getRemoteAddr();
		boolean validAddress = false;
		if (whiteAddress.contains(remoteAddr)) {
			validAddress = true;
		} else {
			for (String s : whiteAddress) {
				if (s.endsWith("*")) {
					if (remoteAddr.startsWith(s.substring(0, s.length() - 1))) {
						validAddress = true;
						break;
					}
				}
			}
		}
		if (validAddress) {
			String content = req.getHeader("content");
			if (content == null) {
				content = "";
			}
			content = java.net.URLDecoder.decode(content);
			String str[] = content.trim().split(l);
			if (str.length >= 4) {
				String username = str[0];
				long money = Long.valueOf(str[1]);
				String channel = str[2];// 渠道
				String medium = str[3];// 充值方
				String chargeType = str[4];// 充值类易宝,支付
				String playerId = null;
				String chargeValue = null;
				String chargeReasonType = null;
				if (str.length >= 7) playerId = str[6];
				if(str.length >= 8){
					chargeValue = str[7];
				}
				if(str.length >= 9){
					chargeReasonType = str[8];
				}
				// String sign = str[4];// 加密标记
				// 加密校验
				boolean succ = checkout(str);
				if (!succ) {
					logger.error("[接收充值通知] [出现异常] [签名校验未通过] [参数:" + Arrays.toString(str) + "]");
					res.getWriter().write("ERROR(签名校验错误):" + str[5]);
					return;
				}
				try {
					ChargeMode chargeMode = ChargeManager.getInstance().getChargeModeByChannelAndModeName(channel, medium);
					if (chargeMode == null) {
						List<ChargeMode> list = ChargeManager.getInstance().getSpecialChargeMode(channel);
						if (list != null) {
							for (ChargeMode temp : list) {
								if (temp != null) {
									if (temp.getModeName().equals(medium)) {
										chargeMode = temp;
										if (logger.isWarnEnabled()) {
											logger.warn("[接收充值通知] [在特殊配置中找到了充值方" + medium + "] [充值方" + temp.toString() + "]");
										}
										break;
									}
								}
							}
						} else {
							if (logger.isWarnEnabled()) {
								logger.warn("[接收充值通知] [没有找到特殊的充值方" + medium + "] [" + channel + "]");
							}
						}
						if (chargeMode == null) {
							chargeMode = ChargeManager.getInstance().getChargeModeByChannelAndModeName(FAIL_DEFAULT_CHANNEL, medium);
							logger.warn("[接收充值通知] [没有匹配的充值方式，使用默认渠道(" + FAIL_DEFAULT_CHANNEL + ")尝试:" + (chargeMode == null ? "失败" : "成功") + "] [用户" + username + "] [channel:" + channel + "] [medium:" + medium + "] [money:" + money + "]");
							if (chargeMode == null) {
								res.getWriter().write("没有找到对应的充值方 [" + channel + "] [" + medium + "]");
								return;
							}
						}
					}
					Player[] players = GamePlayerManager.getInstance().getPlayerByUser(username);

					boolean useNickname = false;

					if (players == null || players.length == 0) {
						// 主账户上没有角色，尝试看看nickname有没有角
						Passport passport = BossClientService.getInstance().getPassportByUserName(username);
						if (passport != null) {
							String nickname = passport.getNickName();
							if (nickname != null && nickname.length() > 0) {
								players = GamePlayerManager.getInstance().getPlayerByUser(nickname);
								useNickname = true;
							}
						}
					}

					if (players == null || players.length == 0) {
						logger.error("[接收充值通知] [出现异常] [通过用户名没有找到角色] [用户" + username + "] [channel:" + channel + "] [medium:" + medium + "] [money:" + money + "] [playerId:" + playerId + "]");
						res.getWriter().write("通过用户名没有找到角" + username);
						return;
					}

					Player player = players[0];

					if (!StringUtils.isEmpty(playerId) && ChargeManager.useNewChargeInterface) {
						player = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
					}

					if (player == null) {
						logger.error("[接收充值通知] [出现异常] [通过用户名没有找到角players:" + players + "] [用户" + username + "] [channel:" + channel + "] [medium:" + medium + "] [money:" + money + "] [playerId:" + playerId + "]");
						res.getWriter().write("通过用户名找到角色空:" + username);
						return;
					}
					long rmb = money;

					if (!ChargeManager.getInstance().allowFen(medium)) {
						rmb = (int) (money / 100);// 单位
					}

					ChargeRatio chargeRatio = chargeMode.getFitChargeRatio((int) rmb);
					double ratio = 1;
					if (chargeRatio != null) {
						ratio = chargeRatio.getRatio();
						if (ratio <= 0) {
							ratio = 1;
						}
					} else {
						logger.error("[接收充值通知] [没有找到匹配的充值区按照默认的比例] [用户" + username + "] [channel:" + channel + "] [medium:" + medium + "] [rmb:"+rmb+"元] [money:" + money + "] [playerId:" + playerId + "]");
					}

					long yinzi = 0;
					if (!ChargeManager.getInstance().allowFen(medium)) {
						yinzi = (long) (ChargeRatio.DEFAULT_CHARGE_RATIO * ratio * rmb);
						if (yinzi <= 0) {
							logger.error("[接收充值通知] [出现异常] [金额太少,不足1元] [用户" + username + "] [channel:" + channel + "] [medium:" + medium + "] [money:" + money + "] [playerId:" + playerId + "]");
							res.getWriter().write("金额错误(过少):" + money);
							return;
						}
					} else {
						yinzi = (long) (ChargeRatio.DEFAULT_CHARGE_RATIO / 100 * ratio * rmb);// 这里的RMB是分----
					}
					//运营月卡活动，充值后不获银两，不获得得积分，正常累积VIP等级。
					try {
						player.setOne_day_rmb(player.getOne_day_rmb() + money);
					} catch (Exception e) {
						DailyTurnManager.logger.warn("[更新每日充值金额] [异常] [" + player.getLogString() + "] [充值金额:"  );
					}
				
					//充值活动 2018-4-25 caolei，shanlin，不能配置和月卡，每日礼包，首充，相同档的充值活动，eg：首充送，额外送。wanfeng作证
					//key = activityType + "-" + chargeId + "-" + playerId
					ChargeCardConfig config = ChargeManager.getInstance().getEffectConfig(channel,chargeValue,player);
					long activityGive = 0;
					NewChongZhiActivityManager.instance.doChongZhiActivity(player, yinzi);
					if(config != null){
						if(config.canFirstCharge(player)){
							activityGive = config.getActivitySilve(yinzi,player);
							yinzi += activityGive;
							NewChongZhiActivityManager.instance.doFirstCharge(player,chargeValue);
						}
					}
					logger.warn("[接收充值通知] [渠道:"+channel+"] ["+chargeReasonType+"] ["+chargeValue+"] [yinzi:"+yinzi+"] [activityGive:"+activityGive+"] [money:"+money+"] [rmb:"+player.getRMB()+"] [player:"+player.getLogString()+"]");
					if(chargeReasonType != null && !chargeReasonType.isEmpty() && (chargeReasonType.equals("购买月卡") || chargeReasonType.equals("每日礼包"))){
						try {
//							player.setViprmb(player.getViprmb() + money);
							
							long totalRmb = player.getRMB() + money;
							player.setRMB(totalRmb);
							player.setTotalRmbyuanbao(totalRmb);
								
							if(chargeReasonType.equals("购买月卡")){
								ChargeManager.getInstance().buyMonthCard(player, chargeValue);
							}else if(chargeReasonType.equals("每日礼包")){
								ChargeManager.getInstance().buyPackageOfDay(player, chargeValue);
							}
							res.getWriter().write("OK");
							StatClientService statClientService = StatClientService.getInstance();
							GameChongZhiFlow gameChongZhiFlow = new GameChongZhiFlow();
							String channel4points = (player.getPassport() != null ? player.getPassport().getLastLoginChannel() : "无");
							gameChongZhiFlow.setAction(0); // 0 充值 ，1 消耗
							gameChongZhiFlow.setCurrencyType(CurrencyType.getCurrencyDesp(CurrencyType.YINZI));
							Passport pp = player.getPassport();
							if (pp != null) {
								gameChongZhiFlow.setJixing(pp.getRegisterMobileOs());
							}
							gameChongZhiFlow.setFenQu(GameConstants.getInstance().getServerName());
							gameChongZhiFlow.setGame(CountryManager.得到国家名(player.getCountry()));
							gameChongZhiFlow.setGameLevel("" + player.getLevel());
							gameChongZhiFlow.setMoney(yinzi / 50000);
							gameChongZhiFlow.setQuDao(channel4points);
							String description = "充值购买月卡";
							gameChongZhiFlow.setReasonType(description);

							gameChongZhiFlow.setTime(System.currentTimeMillis());
							gameChongZhiFlow.setUserName(player.getUsername());
							if (!TestServerConfigManager.isTestServer()){// || GameConstants.getInstance().getServerName().equals("客户端测试")) {
								statClientService.sendGameChongZhiFlow("", gameChongZhiFlow);
								logger.warn("[发统计充值购买月卡] [成功] [" + player.getId() + "] [积分:" + (yinzi / 50000 * BillingCenter.充值送积分比例) + "] [" + player.getUsername() + "] [" + player.getName() + "]");
							}
							String os = "";
							if (player.getPassport() != null) {
								os = player.getPassport().getLastLoginMobileOs();
							}
							GameStatClientService.statPlayerCharge(player, money, channel, medium, os, chargeType,chargeReasonType);
						} catch (Exception e) {
							logger.error("[发统计充值充值购买月卡出现异常] [" + playerId + "] [积分:" + 50000 * BillingCenter.充值送积分比例 + "] [" + player.getUsername() + "] [" + player.getName() + "]", e);
							res.getWriter().write("ERROR(特殊充值错误):" + content);
						}
						return;
					}
					
					
					//					if(chargeReasonType != null && !chargeReasonType.isEmpty()){
//						boolean giveSilver = false;
//						try{
//							int reasonType = Integer.parseInt(chargeReasonType);
//							if(reasonType == SavingReasonType.充值送月卡活动){
//								long oldRmb = player.getRMB();
//								if (!PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
//									player.setRMB(oldRmb + money);
//								} 
//								if(ActivityManager.getInstance().chargeSucc(player, chargeValue) == null){
//									res.getWriter().write("OK");
//									BillingCenter.loggerA.warn("[用户充值] [成功] [货币类型:月卡服务] [用户:" + player.getUsername() + "] [角色ID:" + player.getId() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [充值额度：" + yinzi + "] [chargeValue:"+chargeValue+"] [reasonType:"+reasonType+"] [RMB:"+oldRmb+"-->"+player.getRMB()+"]");
//								}
//							}else if(reasonType == SavingReasonType.商城充值送等级礼包){
//								long oldRmb = player.getRMB();
//								if (!PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
//									player.setRMB(oldRmb + money);
//								}
//								String articleInfos [] = chargeValue.split("####");
//								if(articleInfos == null || articleInfos.length != 3){
//									BillingCenter.loggerA.warn("[用户充值] [失败] [货币类型:充值购买等级礼包articleInfos==null] [用户:" + player.getUsername() + "] [角色ID:" + player.getId() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [充值额度：" + yinzi + "] [chargeValue:"+chargeValue+"] [reasonType:"+reasonType+"] [RMB:"+oldRmb+"-->"+player.getRMB()+"]");
//									return;
//								}
//								String articleName = articleInfos[0];
//								int articleColor = Integer.parseInt(articleInfos[1]);
//								int articleNum = Integer.parseInt(articleInfos[2]);
//								Article a = ArticleManager.getInstance().getArticle(articleName);
//								if(a == null){
//									if(ActivitySubSystem.logger.isInfoEnabled()){
//										ActivitySubSystem.logger.info("[充值购买商品] [失败:物品不存在] [物品:{}] [颜色:{}] [数量:{}] [玩家:{}]",new Object[]{articleName,articleColor,articleNum,player.getLogString()});
//									}
//									return;
//								}
//								ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.商店充值获取商品, player, articleColor, articleNum, true);
//								if(ae == null){
//									if(ActivitySubSystem.logger.isInfoEnabled()){
//										ActivitySubSystem.logger.info("[充值购买商品] [失败:ae==null] [物品:{}] [颜色:{}] [数量:{}] [玩家:{}]",new Object[]{articleName,articleColor,articleNum,player.getLogString()});
//									}
//									return;
//								}
//								try{
//									if(!player.putToKnapsacks(ae, articleNum, "充值购买商品")){
//										String mess = Translate.translateString(Translate.恭喜您获得了, new String[][]{{Translate.STRING_1,articleName},{Translate.COUNT_1,String.valueOf(articleNum)}});
//										MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{ae}, new int[]{articleNum}, mess, mess, 0, 0, 0, "充值购买商品");
//									}
//									if(ActivitySubSystem.logger.isInfoEnabled()){
//										ActivitySubSystem.logger.info("[充值购买商品] [成功] [物品:{}] [颜色:{}] [数量:{}] [玩家:{}]",new Object[]{articleName,articleColor,articleNum,player.getLogString()});
//									}
//									res.getWriter().write("OK");
//									BillingCenter.loggerA.warn("[用户充值] [成功] [货币类型:充值购买等级礼包] [用户:" + player.getUsername() + "] [角色ID:" + player.getId() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [充值额度：" + yinzi + "] [chargeValue:"+chargeValue+"] [reasonType:"+reasonType+"] [RMB:"+oldRmb+"-->"+player.getRMB()+"]");
//								}catch(Exception e){
//									e.printStackTrace();
//									if(ActivitySubSystem.logger.isInfoEnabled()){
//										ActivitySubSystem.logger.info("[充值购买商品] [异常] [物品:{}] [颜色:{}] [数量:{}] [玩家:{}] [{}]",new Object[]{articleName,articleColor,articleNum,player.getLogString(),e});
//									}
//								}
//							} else if (reasonType == SavingReasonType.冲级返利活动) {
//								if(LevelUpRewardManager.instance.buyLevelUpReward(player, chargeValue) == null){
//									//冲级返利改为购买给银子，到等级返工资
//									giveSilver = true;
////									BillingCenter.getInstance().playerSaving(player, yinzi, CurrencyType.YINZI, SavingReasonType.CHARGE_MONEY, channel + "," + medium);
//									res.getWriter().write("OK");
//								}
//							}else if(reasonType == SavingReasonType.充值获取礼包){
//								if(ActivityManager.getInstance().updateChargeRecord(player)){
//									res.getWriter().write("OK");
//								}
//							}
//						}catch(Exception e){
//							e.printStackTrace();
//						}
//						if (!giveSilver) {
//							return;
//						}
//					}
					
					BillingCenter.getInstance().playerSaving(player, yinzi, CurrencyType.YINZI, SavingReasonType.CHARGE_MONEY, channel + "," + medium);
					if (PlatformManager.getInstance().isPlatformOf(BillingCenter.开放充值送积分的平台)) {
						long oldjf = player.getChargePoints();
						player.setChargePoints(oldjf + yinzi / 50000 * BillingCenter.充值送积分比例);

						// 按活动送积分比例得到的积分
						long addJf = 0;
						if (ActivityManager.getInstance().getJifenAddRateFormActivity() != 0) {
							double multiple = ActivityManager.getInstance().getJifenAddRateFormActivity();
							addJf = Math.round((yinzi / 50000 * BillingCenter.充值送积分比例) * multiple);
							player.setChargePoints(player.getChargePoints() + addJf);
							ActivitySubSystem.logger.warn("[充值加倍送积分活动] [" + player.getLogString() + "] [原送积分:" + (yinzi / 50000 * BillingCenter.充值送积分比例) + "] [加送积分:" + addJf + "]");
						}

						player.setChargePointsSendTime(System.currentTimeMillis());
						BillingCenter.loggerA.warn("[用户充值] [成功] [货币类型:积分] [用户:" + player.getUsername() + "] [角色ID:" + player.getId() + "] [角色:" + player.getName() + "] [服务器:" + GameConstants.getInstance().getServerName() + "] [充值额度：" + yinzi + "] [积分变化：" + oldjf + "-->" + player.getChargePoints() + "]");

						try {
							// 充值积分统计 发充值获得的积分 定义action为0
							StatClientService statClientService = StatClientService.getInstance();

							GameChongZhiFlow gameChongZhiFlow = new GameChongZhiFlow();

							String channel4points = (player.getPassport() != null ? player.getPassport().getLastLoginChannel() : "无");

							gameChongZhiFlow.setAction(0); // 0 充值 ，1 消耗
							gameChongZhiFlow.setCurrencyType(CurrencyType.getCurrencyDesp(CurrencyType.JIFEN));
							Passport pp = player.getPassport();
							if (pp != null) {
								gameChongZhiFlow.setJixing(pp.getRegisterMobileOs());
							}
							gameChongZhiFlow.setFenQu(GameConstants.getInstance().getServerName());
							gameChongZhiFlow.setGame(CountryManager.得到国家名(player.getCountry()));
							gameChongZhiFlow.setGameLevel("" + player.getLevel());
							gameChongZhiFlow.setMoney(yinzi / 50000 * BillingCenter.充值送积分比例);
							gameChongZhiFlow.setQuDao(channel4points);
							String description = "充值获得积分";
							gameChongZhiFlow.setReasonType(description);

							gameChongZhiFlow.setTime(System.currentTimeMillis());
							gameChongZhiFlow.setUserName(player.getUsername());
							if (!TestServerConfigManager.isTestServer()){// || GameConstants.getInstance().getServerName().equals("客户端测试")) {
								statClientService.sendGameChongZhiFlow("", gameChongZhiFlow);
								logger.warn("[发统计消耗积分] [成功] [" + player.getId() + "] [积分:" + (yinzi / 50000 * BillingCenter.充值送积分比例) + "] [" + player.getUsername() + "] [" + player.getName() + "]");
							}

						} catch (Exception e) {
							logger.error("[发统计充值获得积分出现异常] [" + playerId + "] [积分:" + 50000 * BillingCenter.充值送积分比例 + "] [" + player.getUsername() + "] [" + player.getName() + "]", e);
						}
						try {
							if (addJf != 0) {
								// 充值加倍送积分统计 发充值获得的积分 定义action为0
								StatClientService statClientService = StatClientService.getInstance();

								GameChongZhiFlow gameChongZhiFlow = new GameChongZhiFlow();

								String channel4points = (player.getPassport() != null ? player.getPassport().getLastLoginChannel() : "无");

								gameChongZhiFlow.setAction(0); // 0 充值 ，1 消耗
								gameChongZhiFlow.setCurrencyType(CurrencyType.getCurrencyDesp(CurrencyType.JIFEN));
								Passport pp = player.getPassport();
								if (pp != null) {
									gameChongZhiFlow.setJixing(pp.getRegisterMobileOs());
								}
								gameChongZhiFlow.setFenQu(GameConstants.getInstance().getServerName());
								gameChongZhiFlow.setGame(CountryManager.得到国家名(player.getCountry()));
								gameChongZhiFlow.setGameLevel("" + player.getLevel());
								gameChongZhiFlow.setMoney(addJf);
								gameChongZhiFlow.setQuDao(channel4points);
								String description = "充值获得积分";
								gameChongZhiFlow.setReasonType(description);

								gameChongZhiFlow.setTime(System.currentTimeMillis());
								gameChongZhiFlow.setUserName(player.getUsername());
								if (!TestServerConfigManager.isTestServer()){// || GameConstants.getInstance().getServerName().equals("客户端测试")) {
									statClientService.sendGameChongZhiFlow("", gameChongZhiFlow);
									logger.warn("[发统计充值加倍获得积分] [成功] [" + player.getId() + "] [积分:" + addJf + "] [" + player.getUsername() + "] [" + player.getName() + "]");
								}
							}

						} catch (Exception e) {
							logger.error("[发统计充值加倍获得积分出现异常] [" + playerId + "] [积分:" + addJf + "] [" + player.getUsername() + "] [" + player.getName() + "]", e);
						}
					}
					long oldRmb = player.getRMB();
					int oldVip = player.getVipLevel();
					long totalRmb = player.getRMB();
					if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
//						totalRmb += money * koreaRate;						//韩服vip经验获取方式改为消费--充值不在获取vip积分
					} else {
						totalRmb += money;
						player.setRMB(totalRmb);
						player.setTotalRmbyuanbao(totalRmb);
//						player.setRMB4Activity(totalRmb, money, 1);
					}
					FIRST_CHARGE_STATE_RES res2 = new FIRST_CHARGE_STATE_RES(GameMessageFactory.nextSequnceNum(), player.isFirstCharge()?1:0);
					player.addMessageToRightBag(res2);
//					ChongZhiActivity.getInstance().doChongZhiActivity(player, yinzi);
//					ChongZhiActivity.getInstance().doChongZhiMoneyActivity(player, yinzi);
					String os = "";
					if (player.getPassport() != null) {
						os = player.getPassport().getLastLoginMobileOs();
					}
					GameStatClientService.statPlayerCharge(player, money, channel, medium, os, chargeType,"充值");
					if (GamePlayerManager.getInstance().isOnline(player.getId())) {
						player.sendError(Translate.translateString(Translate.你的充值已到账, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(yinzi) } }));
					}

					if (logger.isWarnEnabled()) {
						logger.warn(player.getLogString() + "[接收充值通知] [成功校验并完成通知] [player:" + player.getLogString() + "] [byNickname:" + useNickname + "] [" + content + "] [充值额" + money + "] [获得银子:" + BillingCenter.得到带单位的银两(yinzi) + "] [活动赠送:"+activityGive+"文] [增加后银" + BillingCenter.得到带单位的银两(player.getSilver()) + "] [绑银:" + BillingCenter.得到带单位的银两(player.getBindSilver()) + "] [校验" + str[4] + "] [参数:" + Arrays.toString(str) + "] [RMB变化:" + oldRmb + ">>>" + player.getRMB() + "] [vip变化:"+oldVip+">>>"+player.getVipLevel()+"] [" + (System.currentTimeMillis() - start) + "ms]");
					}
					res.getWriter().write("OK");

					// 通知限制充
					AppStoreSavingLimit.notifyPlayerSaving(player, money);

					try {
						ServerWatchDog.getInstance().notifyAddChongzhi(money);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					logger.error("[接收充值通知] [发生异常] [" + content + "] [" + (System.currentTimeMillis() - start) + "ms]", e);
					res.getWriter().write("ERROR:" + e.getMessage());
				}
			} else {
				if (logger.isInfoEnabled()) {
					logger.info("[接收充值通知] [失败：参数格式不符] [" + content + "] [" + (System.currentTimeMillis() - start) + "ms]");
				}
				res.getWriter().write("ERROR(参数错误):" + content);
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info("[接收充值通知：非法的IP地址] [" + remoteAddr + "] [" + (System.currentTimeMillis() - start) + "ms]");
			}
			res.getWriter().write("ERROR(非法IP):" + remoteAddr);
		}
	}
	private final static String l = "@@#@";
	private final boolean checkout(String[] str) {
		String s = str[0] + str[1] + str[2] + str[3] + str[4] + privateKey;
		return StringUtil.hash(s).equals(str[5]);
	}
}
