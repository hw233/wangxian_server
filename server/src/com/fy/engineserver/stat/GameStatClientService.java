package com.fy.engineserver.stat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.fy.boss.authorize.model.Passport;
import com.fy.engineserver.constants.InitialPlayerConstant;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.server.TestServerConfigManager;
import com.sqage.stat.client.StatClientService;
import com.sqage.stat.model.AcceptTaskFlow;
import com.sqage.stat.model.CreatePlayerFlow;
import com.sqage.stat.model.DayChangFlow;
import com.sqage.stat.model.EnterGameFlow;
import com.sqage.stat.model.FinishTaskFlow;
import com.sqage.stat.model.LogOutGameFlow;
import com.sqage.stat.model.OnLineUsersCountFlow;
import com.sqage.stat.model.PayMoneyUpGradeFlow;
import com.xuanzhi.boss.game.GameConstants;

public class GameStatClientService {

	private static StatClientService statClientService = StatClientService.getInstance();
	private static GameConstants gameConstants = GameConstants.getInstance();

	private static Timer timer = new Timer("数据统计Timer");

	private static GameStatClientService self;

	/**
	 * 记录隔天不下线的玩家
	 */
	private static void statDayChangFlow() {
		if (TestServerConfigManager.isTestServer()) {
			return;
		}
		try {
			long now = SystemTime.currentTimeMillis();

			for (Player player : GamePlayerManager.getInstance().getOnlinePlayers()) {
				DayChangFlow dayChangFlow = new DayChangFlow();
				Passport pp = player.getPassport();
				if (pp != null) {
					dayChangFlow.setQuDao(pp.getRegisterChannel());
					dayChangFlow.setJixing(pp.getLastLoginMobileOs());
				}

				dayChangFlow.setDate(now - 3600000);
				dayChangFlow.setFenQu(gameConstants.getServerName());
				dayChangFlow.setGame(CountryManager.得到国家名(player.getCountry()));
				dayChangFlow.setLevel(String.valueOf(player.getLevel()));
				dayChangFlow.setUserName(player.getUsername());
				if (player.getOnlineTimeOneDayClear() > 0) {
					dayChangFlow.setOnLineTime(now - player.getOnlineTimeOneDayClear());
				} else {
					dayChangFlow.setOnLineTime(now - player.getEnterServerTime()); // /在线时长 单位毫秒
				}
				player.setOnlineTimeOneDayClear(now);
				dayChangFlow.setYouXiBi((long)player.getVipLevel());
				dayChangFlow.setYuanBaoCount(player.getSilver());
				dayChangFlow.setZhiye(CareerManager.careerNameByType(player.getCareer()));

				statClientService.sendDayChangFlow(gameConstants.getGameName(), dayChangFlow);
				SocialManager.logger.error("[统计隔天在线时间] [" + player.getLogString() + "] [上线时间:" + player.getEnterServerTime() + "] [时长:" + dayChangFlow.getOnLineTime() + "]");
			}
		} catch (Exception e) {
			StatClientService.logger.error("[统计隔天在线玩家异常]", e);
		}
	}

	/**
	 * 记录在线人数
	 */
	private static void statOnlineNum() {
		try {
			if (TestServerConfigManager.isTestServer()) {
				return;
			}
			HashMap<String, Long> onLineNums = new HashMap<String, Long>();// <渠道,人数>
			for (Player player : GamePlayerManager.getInstance().getOnlinePlayers()) {
				String channeItemKey = player.getPassport().getLastLoginChannel();
				if (!onLineNums.containsKey(player.getPassport().getLastLoginChannel())) {
					onLineNums.put(channeItemKey, 0L);
				}
				onLineNums.put(channeItemKey, onLineNums.get(channeItemKey) + 1);
			}

			for (Iterator<String> itor = onLineNums.keySet().iterator(); itor.hasNext();) {
				String channel = itor.next();

				OnLineUsersCountFlow onLineUsersCountFlow = new OnLineUsersCountFlow();
				onLineUsersCountFlow.setJixing("");
				onLineUsersCountFlow.setFenQu(gameConstants.getServerName());
				onLineUsersCountFlow.setGame(gameConstants.getGameName());
				onLineUsersCountFlow.setOnlineTime(SystemTime.currentTimeMillis());
				onLineUsersCountFlow.setQuDao(channel);
				onLineUsersCountFlow.setUserCount(onLineNums.get(channel)); // 在线用户数
				statClientService.sendOnLineUsersCountFlow(gameConstants.getGameName(), onLineUsersCountFlow);
				if (StatClientService.logger.isInfoEnabled()) {
					StatClientService.logger.info("[在线玩家] [游戏名:" + gameConstants.getGameName() + "] [服务器名:" + gameConstants.getServerName() + "] [渠道:" + channel + "] [人数:" + onLineUsersCountFlow.getUserCount() + "]");
				}
			}
		} catch (Exception e) {
			StatClientService.logger.error("[在线人数统计异常]", e);
		}

	}
	
	public static void statDayChangFlow(Player player) {
		long now = System.currentTimeMillis();
		DayChangFlow dayChangFlow = new DayChangFlow();
		Passport pp = player.getPassport();
		if (pp != null) {
			dayChangFlow.setQuDao(pp.getRegisterChannel());
			dayChangFlow.setJixing(pp.getLastLoginMobileOs());
		}

		dayChangFlow.setDate(now - 3600000);
		dayChangFlow.setFenQu(gameConstants.getServerName());
		dayChangFlow.setGame(CountryManager.得到国家名(player.getCountry()));
		dayChangFlow.setLevel(String.valueOf(player.getLevel()));
		dayChangFlow.setUserName(player.getUsername());
		if (player.getOnlineTimeOneDayClear() > 0) {
			dayChangFlow.setOnLineTime(now - player.getOnlineTimeOneDayClear());
		} else {
			dayChangFlow.setOnLineTime(now - player.getEnterServerTime()); // /在线时长 单位毫秒
		}
		player.setOnlineTimeOneDayClear(now);
		dayChangFlow.setYouXiBi((long)player.getVipLevel());
		dayChangFlow.setYuanBaoCount(player.getSilver());
		dayChangFlow.setZhiye(CareerManager.careerNameByType(player.getCareer()));

		statClientService.sendDayChangFlow(gameConstants.getGameName(), dayChangFlow);
		SocialManager.logger.error("[统计隔天在线时间] [" + player.getLogString() + "] [上线时间:" + player.getEnterServerTime() + "] [时长:" + dayChangFlow.getOnLineTime() + "]");
	}

	public static void statPlayerCreate(Player player) {
		// /////////////////用户创建角色 start///////////////////////////////
		try {
			if (TestServerConfigManager.isTestServer()) {
				return;
			}
			Passport passport = player.getPassport();
			CreatePlayerFlow createPlayerFlow = new CreatePlayerFlow();
			createPlayerFlow.setCreatPlayerTime(SystemTime.currentTimeMillis());
			createPlayerFlow.setGame(CountryManager.getInstance().getCountryMap().get(player.getCountry()).getCount() + "");
			createPlayerFlow.setPlayerName(player.getName()); // //角色名称
			createPlayerFlow.setUserName(player.getUsername()); // //用户名称

			createPlayerFlow.setFenQu(gameConstants.getServerName());
			createPlayerFlow.setSex((player.getSex() == (byte) 0) ? "男" : "女");
			createPlayerFlow.setNation(CountryManager.得到国家名(player.getCountry()));
			createPlayerFlow.setQudao(passport.getLastLoginChannel());

			statClientService.sendCreatePlayerFlow(gameConstants.getGameName(), createPlayerFlow);

			if (StatClientService.logger.isInfoEnabled()) {
				StatClientService.logger.info("[创建角色] [" + player.getLogString() + "] [登陆渠道:" + passport.getLastLoginChannel() + "] [注册渠道:" + passport.getRegisterChannel() + "]");
			}
		} catch (Exception e) {
			StatClientService.logger.error(player.getLogString() + "[用户创建统计异常]", e);
		}
		// /////////////////用户创建角色 end///////////////////////////////

	}

	public static void statPlayerLeaveServer(Player player, long thisOnlineTime) {
		try {
			if (TestServerConfigManager.isTestServer()) {
				return;
			}
			// ////////////////添加退出游戏用户 start///////////////////////////
			Passport passport = player.getPassport();
			LogOutGameFlow logOutGameFlow = new LogOutGameFlow();
			logOutGameFlow.setJixing(passport.getLastLoginMobileOs());
			logOutGameFlow.setDate(SystemTime.currentTimeMillis());
			logOutGameFlow.setFenQu(gameConstants.getServerName());
			logOutGameFlow.setGame(CountryManager.得到国家名(player.getCountry()));
			logOutGameFlow.setLevel(String.valueOf(player.getLevel()));
			logOutGameFlow.setUserName(player.getUsername());
			logOutGameFlow.setOnLineTime(thisOnlineTime);

			logOutGameFlow.setYouXiBi((long)player.getVipLevel());
			logOutGameFlow.setYuanBaoCount(player.getSilver());
			logOutGameFlow.setQuDao(passport.getLastLoginChannel());
			

			statClientService.sendLogOutGameFlow(gameConstants.getGameName(), logOutGameFlow);
			if (StatClientService.logger.isInfoEnabled()) {
				StatClientService.logger.info("[角色退出] [" + player.getLogString() + "] [登陆渠道:" + passport.getLastLoginChannel() + "] [注册渠道:" + passport.getRegisterChannel() + "]");
			}
			// //////////////添加退出游戏用户 end/////////////////////////////
		} catch (Exception e) {
			StatClientService.logger.error(player.getLogString() + "[玩家下线异常]", e);
		}
	}

	public static List<String> needModifyChannel = new ArrayList<String>();

	static {
		needModifyChannel.add("baoruan");
		needModifyChannel.add("duokuapi");
		needModifyChannel.add("baiduyy");
	}

	public static void statPlayerEnterServer(Player player) {
		// ////////////////添加进入游戏用户 start///////////////////////////
		try {
			if (TestServerConfigManager.isTestServer()) {
				return;
			}
			Passport passport = player.getPassport();
			EnterGameFlow enterGameFlow = new EnterGameFlow();
			enterGameFlow.setJixing(passport.getLastLoginMobileOs());
			enterGameFlow.setDate(SystemTime.currentTimeMillis());
			enterGameFlow.setFenQu(gameConstants.getServerName());
			enterGameFlow.setZhiye(CareerManager.careerNameByType(player.getCareer()));
			enterGameFlow.setGame(CountryManager.得到国家名(player.getCountry()));
			enterGameFlow.setLevel(String.valueOf(player.getLevel())); // /用户当前级别
			if (needSetUsernameFromPassport(passport)) {
				enterGameFlow.setUserName(passport.getUserName());
			} else {
				enterGameFlow.setUserName(player.getUsername());
			}
			enterGameFlow.setQuDao(passport.getLastLoginChannel());

			statClientService.sendEnterGameFlow(gameConstants.getGameName(), enterGameFlow);
			if (TaskManager.logger.isWarnEnabled()) {
				TaskManager.logger.warn("[角色登陆] [" + player.getLogString() + "] [登陆渠道:" + passport.getLastLoginChannel() + "] [注册渠道:" + passport.getRegisterChannel() + "]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			StatClientService.logger.error(player.getLogString() + "[用户上线统计异常]", e);
		}
		// //////////////添加进入游戏用户 end/////////////////////////////
	}

	public static boolean needSetUsernameFromPassport(Passport passport) {
		if (passport.getLastLoginChannel() == null || passport.getRegisterChannel() == null) {
			return false;
		}
		for (String channel : needModifyChannel) {
			if (passport.getRegisterChannel().toLowerCase().contains(channel) && passport.getLastLoginChannel().toLowerCase().contains(channel)) {
				return true;
			}
		}
		return false;
	}

	public static void statPlayerDeliverTask(Player player, Task task) {
		// ////////////////完成任务 start///////////////////////////
		try {
			if (TestServerConfigManager.isTestServer()) {
				return;
			}
			FinishTaskFlow finishTaskFlow = new FinishTaskFlow();
			Passport pp = player.getPassport();
			if (pp != null) {
				finishTaskFlow.setJixing(pp.getRegisterMobileOs());
			}
			finishTaskFlow.setDate(System.currentTimeMillis());
			finishTaskFlow.setFenQu(gameConstants.getServerName());
			finishTaskFlow.setGetDaoJu(10);
			finishTaskFlow.setGetWuPin(0);
			finishTaskFlow.setGetYOuXiBi(0);

			finishTaskFlow.setId(task.getId());
			finishTaskFlow.setStatus("完成");
			finishTaskFlow.setTaskName(task.getName_stat());
			finishTaskFlow.setUserName(player.getUsername());
			finishTaskFlow.setAward("");
			// 2012-5-31 9:48:27 新增
			finishTaskFlow.setCareer(InitialPlayerConstant.各门派的名称[player.getMainCareer()]); // 职业
			finishTaskFlow.setTaskGroup(task.getGroupName() == null ? "" : task.getGroupName()); // 当前任务分组
			finishTaskFlow.setPreTaskGroup(task.getFrontGroupName() == null ? "" : task.getFrontGroupName()); // 前置任务分组

			statClientService.sendFinishTaskFlow(gameConstants.getGameName(), finishTaskFlow);

			if (StatClientService.logger.isInfoEnabled()) {
				StatClientService.logger.info("[角色完成任务] [" + player.getLogString() + "] [任务:" + task.getName() + "] [taskID:" + task.getId() + "]");
			}
		} catch (Exception e) {
			StatClientService.logger.error(player.getLogString() + "[完成任务统计异常]", e);
		}
		// //////////////完成任务 end/////////////////////////////
	}

	public static void statPlayerAcceptTask(Player player, Task task) {
		// ////////////////添加接受任务 start///////////////////////////
		try {
			if (TestServerConfigManager.isTestServer()) {
				return;
			}
			AcceptTaskFlow acceptTaskFlow = new AcceptTaskFlow();
			Passport pp = player.getPassport();
			acceptTaskFlow.setJixing("");
			if (pp != null) {
				acceptTaskFlow.setJixing(pp.getRegisterMobileOs());
			}
			acceptTaskFlow.setFenQu(gameConstants.getServerName());
			acceptTaskFlow.setUserName(player.getUsername());
			acceptTaskFlow.setTaskName(task.getName_stat());
			acceptTaskFlow.setTaskType(TaskConfig.SHOW_NAMES[task.getShowType()]);
			acceptTaskFlow.setId(task.getId());
			acceptTaskFlow.setGameLevel("" + player.getLevel());
			acceptTaskFlow.setCreateDate(SystemTime.currentTimeMillis());
			// 2012-5-31 9:46:19 新增
			acceptTaskFlow.setCareer(InitialPlayerConstant.各门派的名称[player.getMainCareer()]);
			acceptTaskFlow.setTaskGroup(task.getGroupName() == null ? "" : task.getGroupName());
			acceptTaskFlow.setPreTaskGroup(task.getFrontGroupName() == null ? "" : task.getFrontGroupName());
			statClientService.sendAcceptTaskFlow(gameConstants.getGameName(), acceptTaskFlow);

			if (StatClientService.logger.isInfoEnabled()) {
				StatClientService.logger.info("[角色接受任务] [" + player.getLogString() + "] [任务:" + task.getName() + "] [taskID:" + task.getId() + "]");
			}
			// //////////////添加接受任务 end/////////////////////////////
		} catch (Exception e) {
			StatClientService.logger.error(player.getLogString() + "[接受任务统计异常]", e);
		}
	}

	public static void statPlayerCharge(Player player, long rmb, String channel, String medium, String os, String chargeType,String chargeFunction) {
		/*try {
			PayMoneyFlow payMoneyFlow = new PayMoneyFlow();
			payMoneyFlow.setDate(new Date().getTime());
			payMoneyFlow.setFenQu(gameConstants.getServerName());
			payMoneyFlow.setGame(CountryManager.得到国家名(player.getCountry()));
			payMoneyFlow.setLevel(String.valueOf(player.getLevel()));
			payMoneyFlow.setUserName(player.getUsername());
			payMoneyFlow.setPayMoney(rmb); // 充值人民币， 单位:分
			payMoneyFlow.setQuDao(channel);
			payMoneyFlow.setCost("0"); // 本次充值付给支付商的手续费（人民币），单位：分

			payMoneyFlow.setType(chargeType);
			payMoneyFlow.setCardType(medium);
			payMoneyFlow.setJixing(os); // 玩家机型: Android或者IOS

			statClientService.sendPayMoneyFlow(gameConstants.getGameName(), payMoneyFlow);

			StatClientService.logger.warn(player.getLogString() + "[发送充值统计] [钱数:" + rmb + "] [渠道:" + channel + "] [medium:" + medium + "] [chargeType:" + chargeType + "] [os:" + os + "]");
		} catch (Exception e) {
			StatClientService.logger.error(player.getLogString() + "[发送充值统计异常,rmb:" + rmb + "]", e);
		}*/
		
		try {
			if (TestServerConfigManager.isTestServer()){// && !GameConstants.getInstance().getServerName().equals("客户端测试")) {
				return;
			}
			PayMoneyUpGradeFlow payMoneyUpGradeFlow = new PayMoneyUpGradeFlow();
			payMoneyUpGradeFlow.setDate(new Date().getTime());
			payMoneyUpGradeFlow.setFenQu(gameConstants.getServerName());
			payMoneyUpGradeFlow.setGame(CountryManager.得到国家名(player.getCountry()));
			payMoneyUpGradeFlow.setLevel(String.valueOf(player.getLevel()));
			payMoneyUpGradeFlow.setUserName(player.getUsername());
			payMoneyUpGradeFlow.setPayMoney(rmb); // 充值人民币， 单位:分
			payMoneyUpGradeFlow.setQuDao(channel);
			payMoneyUpGradeFlow.setCost("0"); // 本次充值付给支付商的手续费（人民币），单位：分
			payMoneyUpGradeFlow.setType(chargeType);
			payMoneyUpGradeFlow.setCardType(medium);
			payMoneyUpGradeFlow.setJixing(os); // 玩家机型: Android或者IOS
			payMoneyUpGradeFlow.setColum1(chargeFunction); // 充值功能
			
			String ua = player.getUserAgent();
			if(ua == null) {
				ua = "offline_UNKNOWN";
			}
			payMoneyUpGradeFlow.setModelType(ua);  ///设备型号
			payMoneyUpGradeFlow.setVip(String.valueOf(player.getVipLevel()));               //用户vip
			long regtime = 0;
			if(player.getPassport()!=null && player.getPassport().getRegisterDate() != null) {
				regtime = player.getPassport().getRegisterDate().getTime();
			} else {
				regtime = System.currentTimeMillis();
			}
			payMoneyUpGradeFlow.setRegistDate(regtime);  //用户的注册时间
			payMoneyUpGradeFlow.setPlayName(player.getName());       //角色名称
		
			statClientService.sendPayMoneyUpGradeFlow(gameConstants.getGameName(), payMoneyUpGradeFlow);
		} catch(Exception e) {
			StatClientService.logger.error(player.getLogString() + "[发送充值统计异常,rmb:" + rmb + "]", e);
		}
	}

	public void init() {
		

		self = this;

		long statTime = SystemTime.currentTimeMillis();

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// 每分钟记录一次当前在线人数
				statOnlineNum();
			}
		}, 0, 60 * 1000L);

		Calendar calendar = Calendar.getInstance();

		// calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		calendar.add(Calendar.DAY_OF_MONTH, 1);

		long delayToMidnight = calendar.getTimeInMillis() - statTime;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// 每天凌晨记录那些不下线的玩家
				statDayChangFlow();
			}
		}, delayToMidnight, 24L * 60 * 60 * 1000L);

		ServiceStartRecord.startLog(this);
	}
}