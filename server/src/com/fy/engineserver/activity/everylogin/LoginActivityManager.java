package com.fy.engineserver.activity.everylogin;

import static com.fy.engineserver.util.TimeTool.formatter.varChar10;
import static com.fy.engineserver.util.TimeTool.formatter.varChar19;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Login_Commit;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class LoginActivityManager {

	private Map<String, LoginActivity> activityMap = new HashMap<String, LoginActivity>();

	private static LoginActivityManager instance;

	public DiskCache ddc = null;// <activityName,<PlayerId,List<Day>>

	private String diskFile;

	public static LoginActivityManager getInstance() {
		return instance;
	}

	public void putIntoMap(LoginActivity activity) throws Exception {
		if (activityMap.containsKey(activity.getName())) {
			throw new Exception("存在同名活动:" + activity.getName());
		}
		activityMap.put(activity.getName(), activity);
	}

	/**
	 * @param player
	 */
	public void noticePlayerLogin(Player player) {
		for (LoginActivity activity : activityMap.values()) {
			if (activity.open()) {
				List<String> loginRecord = getLoginRecord(activity, player);
				String date = varChar10.format(new Date());
				if (loginRecord.contains(date)) {
					ActivitySubSystem.logger.error("[连续登陆活动] [" + player.getLogString() + "] [当天登陆过:" + date + "]");
					return;
				}
				int days = getDays(loginRecord, date);
				if (activity instanceof LoginActivityDoPrizeByNotice) {
					LoginActivityDoPrizeByNotice loginNotice = (LoginActivityDoPrizeByNotice) activity;
					WindowManager wm = WindowManager.getInstance();
					MenuWindow mw = wm.createTempMenuWindow(600);
					Option_Login_Commit option = new Option_Login_Commit(loginNotice);
					option.setText(Translate.text_4857);
					mw.setTitle("领取登陆奖励");
					mw.setDescriptionInUUB("每天登陆都能领取银票250两");
					Option[] options = new Option[] { option };
					mw.setOptions(options);
					CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
					player.addMessageToRightBag(creq);
				} else {
					CompoundReturn cr = activity.doPrize(player, days);
					if (cr.getBooleanValue()) {// 奖励成功
						notifyPlayerLogin(activity, player);
						player.sendError(cr.getStringValue());
						ActivitySubSystem.logger.error("[连续登陆活动] [" + player.getLogString() + "] [获得奖励] [" + activity.getName() + "] [" + cr.getStringValue() + "]");
					}
				}
			}
		}
	}

	/**
	 * 连续登陆天数
	 * @param loginRecord
	 * @param date
	 * @return
	 */
	public static int getDays(List<String> loginRecord, String date) {
		int days = 1;
		Calendar oldDay = Calendar.getInstance();
		oldDay.setTimeInMillis(TimeTool.formatter.varChar10.parse(date));
		for (int i = loginRecord.size() - 1; i >= 0; i--) {
			Calendar tempDay = Calendar.getInstance();
			tempDay.setTimeInMillis(TimeTool.formatter.varChar10.parse(loginRecord.get(i)));
			if (oldDay.get(Calendar.DAY_OF_YEAR) - tempDay.get(Calendar.DAY_OF_YEAR) == 1) {
				days++;
				oldDay = tempDay;
			} else {
				break;
			}
		}
		return days;
	}

	/**
	 * 得到某个活动,某人的登陆记录
	 * @param activity
	 * @param player
	 * @return
	 */
	public List<String> getLoginRecord(LoginActivity activity, Player player) {
		checkActivityAndPlayer(activity, player);
		HashMap<Long, List<String>> playerRecord = (HashMap<Long, List<String>>) ddc.get(activity.getName());
		return playerRecord.get(player.getId());
	}

	private void checkActivityAndPlayer(LoginActivity activity, Player player) {
		if (ddc.get(activity.getName()) == null) {
			ddc.put(activity.getName(), new HashMap<Long, List<String>>());
		}
		HashMap<Long, List<String>> playerRecord = (HashMap<Long, List<String>>) ddc.get(activity.getName());
		if (!playerRecord.containsKey(player.getId())) {
			playerRecord.put(player.getId(), new ArrayList<String>());
		}
	}

	/**
	 * test nest day
	 * @param activity
	 * @param player
	 */
	public static String getContinueLoginDays() {
		Calendar ca = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
		String date = sdf.format(ca.getTimeInMillis());
		return date;
	}

	public void notifyPlayerLogin(LoginActivity activity, Player player) {
		checkActivityAndPlayer(activity, player);
		HashMap<Long, List<String>> playerRecord = (HashMap<Long, List<String>>) ddc.get(activity.getName());
		String date = varChar10.format(new Date());
		// String date = getContinueLoginDays();
		playerRecord.get(player.getId()).add(date);
		ddc.put(activity.getName(), playerRecord);
	}

	public Map<String, LoginActivity> getActivityMap() {
		return activityMap;
	}

	public void setActivityMap(Map<String, LoginActivity> activityMap) {
		this.activityMap = activityMap;
	}

	public DiskCache getDdc() {
		return ddc;
	}

	public void setDdc(DiskCache ddc) {
		this.ddc = ddc;
	}

	public String getDiskFile() {
		return diskFile;
	}

	public void setDiskFile(String diskFile) {
		this.diskFile = diskFile;
	}

	// 时间，服务器，平台，活动
	private void loadActivity() throws Exception {
		if (true) {
			return;
		}
		Set<String> set = new HashSet<String>();
		set.add("海纳百川");
		set.add("国内本地测试");
		putIntoMap(new GreenServerLoginActivity(varChar19.parse("2013-03-22 00:00:00"), varChar19.parse("2013-04-21 00:00:00"), "银块", 25, set, new Platform[] { Platform.官方 }));
		Set<String> set2 = new HashSet<String>();
		set2.add("琼楼金阙");
		set2.add("国内本地测试");
		putIntoMap(new LoginActivityDoPrizeByNotice(varChar19.parse("2013-03-29 00:00:00"), varChar19.parse("2013-04-30 23:59:00"), "银块", 25, "连续登录活动，公告领取20130328", set2, new Platform[] { Platform.官方 }));

		Set<String> set3 = new HashSet<String>();
		set3.add("飘渺仙道");
		putIntoMap(new LoginActivityDoPrizeByNotice(varChar19.parse("2013-03-29 00:00:00"), varChar19.parse("2013-04-30 23:59:00"), "银块", 25, "连续登录活动，公告领取0402", set3, new Platform[] { Platform.官方 }));
		Set<String> 万里苍穹 = new HashSet<String>();
		万里苍穹.add("万里苍穹");
		万里苍穹.add("盛世欢歌");
		万里苍穹.add("天下无双");
		putIntoMap(new LoginActivityDoPrizeByNotice(varChar19.parse("2013-03-29 00:00:00"), varChar19.parse("2013-04-30 23:59:00"), "银块", 25, "连续登录活动，公告领取0409", 万里苍穹, new Platform[] { Platform.官方 }));
		Set<String> 修罗转生 = new HashSet<String>();
		修罗转生.add("修罗转生");
		putIntoMap(new LoginActivityDoPrizeByNotice(varChar19.parse("2013-03-29 00:00:00"), varChar19.parse("2013-05-31 23:59:00"), "银块", 25, "连续登录活动，公告领取0425", 修罗转生, new Platform[] { Platform.官方 }));

		Set<String> servers = new HashSet<String>();
		servers.add("蜀山之道");
		servers.add("禦劍伏魔");
		servers.add("縱橫九州");
		servers.add("道骨仙風");
		servers.add("傲視遮天");
		servers.add("昆侖仙境");
		servers.add("昆华古城");
		servers.add("飄渺王城");
		servers.add("華山之巔");
		servers.add("仙侶奇緣");
		servers.add("斬龍屠魔");
		servers.add("天降神兵");
		servers.add("仙尊降世");
		servers.add("雪域冰城");
		servers.add("伏虎沖天");
		servers.add("無相如來");
		servers.add("人間仙境");
		servers.add("皇圖霸業");
		servers.add("步雲登仙");
		servers.add("仙姿玉貌");
		servers.add("瓊漿玉露");
		servers.add("仙人指路");

		LoginEveryDay 台湾老服 = new LoginEveryDay(varChar19.parse("2013-03-29 00:00:00"), varChar19.parse("2013-04-18 23:59:00"), new String[] { "火雞肉", "寶石錦囊(1級)", "煉星符" }, new int[] { 5, 1, 5 }, new int[] { 3, 0, 0 }, new Platform[] { Platform.台湾 }, "台湾活动--0416", servers);

		putIntoMap(台湾老服);
	}

	public void init() throws Exception {
		
		long now = SystemTime.currentTimeMillis();
		File file = new File(diskFile);
		ddc = new DefaultDiskCache(file, null, "loginActivity", 100L * 365 * 24 * 3600 * 1000L);
		instance = this;
		loadActivity();
		ServiceStartRecord.startLog(this);
	}

	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();

		list.add("2013-04-14");
		list.add("2013-04-15");

		String date = "2013-04-16";
		System.out.println(getDays(list, date));
	}
}
