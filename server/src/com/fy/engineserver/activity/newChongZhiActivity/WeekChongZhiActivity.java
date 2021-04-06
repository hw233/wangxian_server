package com.fy.engineserver.activity.newChongZhiActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import jxl.Cell;

import org.slf4j.Logger;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.charge.ChargeRatio;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.diskcache.DiskCache;

/**
 * 周充值活动，每天充值有奖励，累计满足充值天数N天有奖励
 *
 */
public class WeekChongZhiActivity {
	private static int addIndex = 0;
	
	public static final int WEEK_CZ_TYPE_0 = 1;
	public static final int WEEK_CZ_TYPE_1 = 8;
	public static final int WEEK_CZ_TYPE_2 = 9;
	
	private WeekClientActivity[] clients;
	
	public static DiskCache diskcache;
	public static Logger logger;
	
	public static long ACTIVITY_SPACE = 1000*60*60*24;
	
	//累计充值的金额
	public static String WEEK_DIS_KEY1 = "WeekCZk1_";
	public HashMap<Long, long[]> playerTotal4Week = new HashMap<Long, long[]>();
	public long GetPlayerTotalMoney(long pID) {
		long[] moneys = playerTotal4Week.get(pID);
		if (moneys == null) {
			return 0;
		}else {
			return moneys[getActivityIndex()];
		}
	}
	
	public int GetPlayerTotalDay(long pID) {
		long[] moneys = playerTotal4Week.get(pID);
		if (moneys == null) {
			return 0;
		}else {
			int num = 0;
			for (int i = 0; i < moneys.length; i++) {
				if (moneys[i] >= needMonty) {
					num++;
				}
			}
			return num;
		}
	}
	
	//领取情况
	public static String WEEK_DIS_KEY2 = "WeekCZk2_";
	public HashMap<Long, boolean[]> playerGetReward = new HashMap<Long, boolean[]>();
	public boolean[] GetIsGetRewards(long pId) {
		boolean[] isGets = new boolean[3];
		boolean[] grs = playerGetReward.get(pId);
		if (grs == null) {
			isGets[0] = false;
			isGets[1] = false;
			isGets[2] = false;
		}else {
			int index = getActivityIndex();
			isGets[0] = grs[index];
			isGets[1] = grs[grs.length-2];
			isGets[2] = grs[grs.length-1];
		}
		return isGets;
	}

	private int id;
	private int platform;					//平台			0是官方    1是台湾    2是腾讯  3是马来
	private String[] serverNames;			//服务器名字
	private String[] unServerNames;			//不参加服务器名字
	private String str_startTime;			//开始时间

	private long startTime;					//开始时间
	private String str_endTime;				//结束时间
	private long endTime;					//结束时间
	private long needMonty;					//需要累计的金额
	private String msg;
	private String timeMsg;
	
	private ArrayList<TodayActivity> weekActivitys = new ArrayList<TodayActivity>();			//周1-周天

	private TotalActivity small;				//累计天数短的那个
	private TotalActivity big;					//累计天数长的那个
	
	public WeekChongZhiActivity(Cell[][] cells) throws Exception {
		
		for (int i = 0; i < cells[0].length; i++) {
			String s = cells[0][i].getContents();
			switch(i) {
			case 0:
				id = Integer.parseInt(s);
				break;
			case 1:
				break;
			case 2:
				platform = Integer.parseInt(s);
				break;
			case 3:
				serverNames = s.split(",");
				break;
			case 4:
				unServerNames = s.split(",");
				break;
			case 5:
				str_startTime = s;
				break;
			case 6:
				str_endTime = s;
				break;
			case 7:
				break;
			case 8:
				needMonty = Long.parseLong(s);
				break;
			case 14:
				msg = s;
				break;
			case 15:
				timeMsg = s;
				break;
			}
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		startTime = format.parse(str_startTime).getTime();
		endTime = format.parse(str_endTime).getTime();
		
		int i = 1;
		while(cells[i][1].getContents().equals("1")) {
			weekActivitys.add(new TodayActivity(cells[i]));
			i++;
		}
		
		setSmall(new TotalActivity(cells[i]));
		setBig(new TotalActivity(cells[i+1]));
		
		LoadDiskCatch();
		
		clients = new WeekClientActivity[weekActivitys.size() + 2];
		for (int x = 0; x < weekActivitys.size(); x++) {
			clients[x] = new WeekClientActivity();
			clients[x].setTitle(Translate.今日充值大礼);
			clients[x].setRewardEntityIDs(weekActivitys.get(x).getEntityIDs());
			clients[x].setRewardEntityNums(weekActivitys.get(x).getRewardPropNums());
			clients[x].setNeedValue(needMonty/ChargeRatio.DEFAULT_CHARGE_RATIO);
		}
		clients[weekActivitys.size()] = new WeekClientActivity();
		clients[weekActivitys.size()].setTitle(Translate.translateString(Translate.累计充值大礼, new String[][]{{Translate.STRING_1, Translate.多少日[small.totalNum-1]}}));
		clients[weekActivitys.size()].setRewardEntityIDs(small.getEntityIDs());
		clients[weekActivitys.size()].setRewardEntityNums(small.getRewardPropNums());
		clients[weekActivitys.size()].setNeedValue(small.getTotalNum());
		
		clients[weekActivitys.size()+1] = new WeekClientActivity();
		clients[weekActivitys.size()+1].setTitle(Translate.translateString(Translate.累计充值大礼, new String[][]{{Translate.STRING_1, Translate.多少日[big.totalNum-1]}}));
		clients[weekActivitys.size()+1].setRewardEntityIDs(big.getEntityIDs());
		clients[weekActivitys.size()+1].setRewardEntityNums(big.getRewardPropNums());
		clients[weekActivitys.size()+1].setNeedValue(big.getTotalNum());
	}
	
	public void DoChongZhi(Player player, long money) {
		if (!isStart()) {
			return;
		}
		int index = getActivityIndex();
		if (index >= weekActivitys.size()) {
			logger.warn("周充值活动出错:id=[{}] [{}] [{}]", new Object[]{id, index, weekActivitys.size()});
			return;
		}
		long[] totals = playerTotal4Week.get(player.getId());
		if (totals == null) {
			totals = new long[weekActivitys.size()];
		}
		
		totals[index] += money; 
		
		playerTotal4Week.put(player.getId(), totals);
		
		diskcache.put(WEEK_DIS_KEY1 + id, playerTotal4Week);
	}
	
	public int getActivityIndex() {
		long now = System.currentTimeMillis();
		long space = now - startTime;
		return (int)(space/(ACTIVITY_SPACE)) + addIndex;
	}
	
	
	/**
	 * @param player
	 * @param type
	 * @return 1未知错误，2不满足领取条件，3已经领取过
	 */
	public int GetReward(Player player, int type) {
		boolean[] isGets = playerGetReward.get(player.getId());
		if (isGets == null) {
			isGets = new boolean[weekActivitys.size() + 2];
			playerGetReward.put(player.getId(), isGets);
		}
		if (type == WEEK_CZ_TYPE_0) {
			int index = getActivityIndex();
			if (index >= weekActivitys.size()) {
				logger.warn("周充值活动出错:id=[{}] [{}] [{}]", new Object[]{id, index, weekActivitys.size()});
				return 1;
			}
			
			long[] totals = playerTotal4Week.get(player.getId());
			if (totals == null) {
				player.send_HINT_REQ(Translate.未参加周充值活动);
				return 2;
			}else if (totals[index] < needMonty) {
				player.send_HINT_REQ(Translate.未参加周充值活动);
				return 2;
			}
			
			if (isGets[index]) {
				player.send_HINT_REQ(Translate.已经领取过奖励);
				return 3;
			}
			isGets[index] = true;
			playerGetReward.put(player.getId(), isGets);
			diskcache.put(WEEK_DIS_KEY2 + id, playerGetReward);
			
			if (weekActivitys.get(index).SendReward(player, this)) {
				logger.warn("用户领取奖励成功 [{}] 活动ID[{}] index[{}]", new Object[]{player.getLogString(), id, index});
			}
		}else if (type == WEEK_CZ_TYPE_1) {
			int totalNum = 0;
			long[] totals = playerTotal4Week.get(player.getId());
			if (totals == null) {
				player.send_HINT_REQ(Translate.未参加周充值活动);
				return 2;
			}
			
			for (int i = 0; i < totals.length; i++) {
				if (totals[i] >= needMonty) {
					totalNum += 1;
				}
			}
			if (totalNum < small.totalNum) {
				player.send_HINT_REQ(Translate.未参加周充值活动);
				return 2;
			}
			if (isGets[weekActivitys.size()]) {
				player.send_HINT_REQ(Translate.已经领取过奖励);
				return 3;
			}
			isGets[weekActivitys.size()] = true;
			playerGetReward.put(player.getId(), isGets);
			diskcache.put(WEEK_DIS_KEY2 + id, playerGetReward);
			if (small.SendReward(player, this)) {
				logger.warn("用户领取奖励成功 [{}] 活动ID[{}] num[{}]", new Object[]{player.getLogString(), id, small.totalNum});
			}
			
		}else if (type == WEEK_CZ_TYPE_2) {
			int totalNum = 0;
			long[] totals = playerTotal4Week.get(player.getId());
			if (totals == null) {
				player.send_HINT_REQ(Translate.未参加周充值活动);
				return 2;
			}
			
			for (int i = 0; i < totals.length; i++) {
				if (totals[i] >= needMonty) {
					totalNum += 1;
				}
			}
			if (totalNum < big.totalNum) {
				player.send_HINT_REQ(Translate.未参加周充值活动);
				return 2;
			}
			if (isGets[weekActivitys.size()+1]) {
				player.send_HINT_REQ(Translate.已经领取过奖励);
				return 3;
			}
			
			isGets[weekActivitys.size()+1] = true;
			playerGetReward.put(player.getId(), isGets);
			diskcache.put(WEEK_DIS_KEY2 + id, playerGetReward);
			if (big.SendReward(player, this)) {
				logger.warn("用户领取奖励成功 [{}] 活动ID[{}] num[{}]", new Object[]{player.getLogString(), id, big.totalNum});
			}
		}
		
		return 0;
	}
	
	public void LoadDiskCatch() {
		Object obj = diskcache.get(WEEK_DIS_KEY1 + id);
		if (obj == null) {
			diskcache.put(WEEK_DIS_KEY1 + id, playerTotal4Week);
		}else {
			playerTotal4Week = (HashMap<Long, long[]>)obj;
		}
		obj = null;
		obj = diskcache.get(WEEK_DIS_KEY2 + id);
		if (obj == null) {
			diskcache.put(WEEK_DIS_KEY2+id, playerGetReward);
		}else {
			playerGetReward = (HashMap<Long, boolean[]>)obj;
		}
	}
	
	public boolean isStart() {
		long now = System.currentTimeMillis();
		if (startTime < now && now < endTime && isServer()) {
			return true;
		}
		return false;
	}
	
	public boolean isServer() {
		if (getPlatform() == 0) {
			if (!PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
				return false;
			}
		}else if (getPlatform() == 1) {
			if (!PlatformManager.getInstance().isPlatformOf(Platform.台湾)) {
				return false;
			}
		}else if (getPlatform() == 2) {
			if (!PlatformManager.getInstance().isPlatformOf(Platform.腾讯)) {
				return false;
			}
		}else if (getPlatform() == 3) {
			if (!PlatformManager.getInstance().isPlatformOf(Platform.马来)) {
				return false;
			}
		}else if (getPlatform() == 4) {
			if (!PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
				return false;
			}
		}
		String serverName = GameConstants.getInstance().getServerName();
		//如果服务器名字在 unserver名字中，就不参加活动
		for (String s : getUnServerNames()) {
			if (s.equals(serverName)) {
				return false;
			}
		}
		//如果是allserver 的  或者是这个活动的就参加
		for (String s : getServerNames()) {
			if (s.equals("ALL_SERVER")) {
				return true;
			}else if (s.equals(serverName)) {
				return true;
			}
		}
		return false;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPlatform() {
		return platform;
	}
	public void setPlatform(int platform) {
		this.platform = platform;
	}
	public String[] getServerNames() {
		return serverNames;
	}
	public void setServerNames(String[] serverNames) {
		this.serverNames = serverNames;
	}
	public String[] getUnServerNames() {
		return unServerNames;
	}
	public void setUnServerNames(String[] unServerNames) {
		this.unServerNames = unServerNames;
	}
	
	public long getNeedMonty() {
		return needMonty;
	}
	public void setNeedMonty(long needMonty) {
		this.needMonty = needMonty;
	}
	
	public String getStr_startTime() {
		return str_startTime;
	}

	public void setStr_startTime(String str_startTime) {
		this.str_startTime = str_startTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
	public void setStr_endTime(String str_endTime) {
		this.str_endTime = str_endTime;
	}

	public String getStr_endTime() {
		return str_endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getEndTime() {
		return endTime;
	}
	
	public ArrayList<TodayActivity> getWeekActivitys() {
		return weekActivitys;
	}

	public void setWeekActivitys(ArrayList<TodayActivity> weekActivitys) {
		this.weekActivitys = weekActivitys;
	}

	public void setSmall(TotalActivity small) {
		this.small = small;
	}

	public TotalActivity getSmall() {
		return small;
	}

	public void setBig(TotalActivity big) {
		this.big = big;
	}

	public TotalActivity getBig() {
		return big;
	}

	public void setPlayerTotal4Week(HashMap<Long, long[]> playerTotal4Week) {
		this.playerTotal4Week = playerTotal4Week;
	}

	public HashMap<Long, long[]> getPlayerTotal4Week() {
		return playerTotal4Week;
	}

	public void setPlayerGetReward(HashMap<Long, boolean[]> playerGetReward) {
		this.playerGetReward = playerGetReward;
	}

	public HashMap<Long, boolean[]> getPlayerGetReward() {
		return playerGetReward;
	}

	public void setClients(WeekClientActivity[] clients) {
		this.clients = clients;
	}

	public WeekClientActivity[] getClients() {
		return clients;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setTimeMsg(String timeMsg) {
		this.timeMsg = timeMsg;
	}

	public String getTimeMsg() {
		return timeMsg;
	}

	public static void setAddIndex(int addIndex) {
		WeekChongZhiActivity.addIndex = addIndex;
	}

	public static int getAddIndex() {
		return addIndex;
	}

	//今天活动
	public class TodayActivity {
		
		private String[] rewardPropNames;				//奖励物品名字
		private long[] entityIDs;						//奖励物品ID
		private ArticleEntity[] rewardTempEntitys;		//奖励物品Entity
		private int[] rewardPropNums;					//奖励物品数目
		private int[] rewardColors;						//奖励物品颜色
		private boolean[] rewardRare;					//是否珍贵
		private boolean[] rewardBinds;					//奖励绑定情况
		
		private String mailTitle;
		private String mailMsg;
		
		public TodayActivity(Cell[] cells) {
			rewardPropNames = cells[9].getContents().split(",");
			String[] rewardPropNums_s = cells[10].getContents().split(",");
			rewardPropNums = new int[rewardPropNums_s.length];
			for (int i = 0; i < rewardPropNums_s.length; i++) {
				rewardPropNums[i] = Integer.parseInt(rewardPropNums_s[i]);
			}
			String[] rewardColors_s = cells[11].getContents().split(",");
			rewardColors = new int[rewardColors_s.length];
			for (int i = 0; i < rewardColors_s.length; i++) {
				rewardColors[i] = Integer.parseInt(rewardColors_s[i]);
			}
			String[] rewardRare_s = cells[12].getContents().split(",");
			rewardRare = new boolean[rewardRare_s.length];
			for (int i = 0; i < rewardRare_s.length; i++) {
				rewardRare[i] = Boolean.parseBoolean(rewardRare_s[i]);
			}
			String[] rewardBinds_s = cells[13].getContents().split(",");
			rewardBinds = new boolean[rewardBinds_s.length];
			for (int i = 0; i < rewardBinds_s.length; i++) {
				rewardBinds[i] = Boolean.parseBoolean(rewardBinds_s[i]);
			}
			
			mailTitle = cells[14].getContents();
			mailMsg = cells[15].getContents();
			
			rewardTempEntitys = new ArticleEntity[rewardPropNames.length];
			entityIDs = new long[rewardPropNames.length];
			for (int i = 0; i < rewardPropNames.length; i++) {
				String propName = rewardPropNames[i];
				Article a = ArticleManager.getInstance().getArticle(propName);
				if (a == null) {
					NewChongZhiActivityManager.logger.warn("[周累计活动] [{}]", new Object[]{propName});
					return;
				}
				try{
					rewardTempEntitys[i] = ArticleEntityManager.getInstance().createTempEntity(a, getRewardBinds()[i], ArticleEntityManager.活动, null, getRewardColors()[i]);
					entityIDs[i] = rewardTempEntitys[i].getId();
				}catch(Exception e) {
					NewChongZhiActivityManager.logger.error("创建临时物品出错", e);
				}
			}
		}
		
		public boolean SendReward(Player player, WeekChongZhiActivity ac) {
			try{
				ArticleEntity[] entitys = new ArticleEntity[rewardPropNames.length];
				for (int i = 0; i < rewardPropNames.length; i++) {
					String propName = rewardPropNames[i];
					Article a = ArticleManager.getInstance().getArticle(propName);
					if (a == null) {
						logger.warn("[物品不存在] [{}] [{}] [{}]", new Object[]{player.getLogString(), getId(), propName});
					}
					entitys[i] = ArticleEntityManager.getInstance().createEntity(a, getRewardBinds()[i], ArticleEntityManager.活动, player, getRewardColors()[i], getRewardPropNums()[i], true);
				}
				MailManager.getInstance().sendMail(player.getId(), entitys, getRewardPropNums(), mailTitle, mailMsg, 0, 0, 0, "2016周累计活动");
				player.send_HINT_REQ(Translate.周充值领取成功, (byte)5);
				return true;
			}catch(Exception e) {
				logger.error("发送奖励出错:["+player.getLogString()+"]["+ac.id+"] ["+Arrays.toString(rewardPropNames)+"]", e);
			}
			return false;
		}
		
		public String[] getRewardPropNames() {
			return rewardPropNames;
		}
		public void setRewardPropNames(String[] rewardPropNames) {
			this.rewardPropNames = rewardPropNames;
		}
		public ArticleEntity[] getRewardTempEntitys() {
			return rewardTempEntitys;
		}
		public void setRewardTempEntitys(ArticleEntity[] rewardTempEntitys) {
			this.rewardTempEntitys = rewardTempEntitys;
		}
		public int[] getRewardPropNums() {
			return rewardPropNums;
		}
		public void setRewardPropNums(int[] rewardPropNums) {
			this.rewardPropNums = rewardPropNums;
		}
		public int[] getRewardColors() {
			return rewardColors;
		}
		public void setRewardColors(int[] rewardColors) {
			this.rewardColors = rewardColors;
		}
		public boolean[] getRewardBinds() {
			return rewardBinds;
		}
		public void setRewardBinds(boolean[] rewardBinds) {
			this.rewardBinds = rewardBinds;
		}
		public boolean[] getRewardRare() {
			return rewardRare;
		}
		public void setRewardRare(boolean[] rewardRare) {
			this.rewardRare = rewardRare;
		}
		public String getMailTitle() {
			return mailTitle;
		}

		public void setMailTitle(String mailTitle) {
			this.mailTitle = mailTitle;
		}

		public String getMailMsg() {
			return mailMsg;
		}

		public void setMailMsg(String mailMsg) {
			this.mailMsg = mailMsg;
		}

		public void setEntityIDs(long[] entityIDs) {
			this.entityIDs = entityIDs;
		}

		public long[] getEntityIDs() {
			return entityIDs;
		}
	}
	
	//累计N天活动
	public class TotalActivity {
		private int totalNum;							//累计天数
		private String[] rewardPropNames;				//奖励物品名字
		private long[] entityIDs;						//奖励物品ID
		private ArticleEntity[] rewardTempEntitys;		//奖励物品Entity
		private int[] rewardPropNums;					//奖励物品数目
		private int[] rewardColors;						//奖励物品颜色
		private boolean[] rewardBinds;					//奖励绑定情况
		private boolean[] rewardRare;					//是否珍贵
		
		private String mailTitle;
		private String mailMsg;
		
		public TotalActivity(Cell[] cells) {
			totalNum = Integer.parseInt(cells[7].getContents());
			
			rewardPropNames = cells[9].getContents().split(",");
			String[] rewardPropNums_s = cells[10].getContents().split(",");
			rewardPropNums = new int[rewardPropNums_s.length];
			for (int i = 0; i < rewardPropNums_s.length; i++) {
				rewardPropNums[i] = Integer.parseInt(rewardPropNums_s[i]);
			}
			String[] rewardColors_s = cells[11].getContents().split(",");
			rewardColors = new int[rewardColors_s.length];
			for (int i = 0; i < rewardColors_s.length; i++) {
				rewardColors[i] = Integer.parseInt(rewardColors_s[i]);
			}
			String[] rewardRare_s = cells[12].getContents().split(",");
			rewardRare = new boolean[rewardRare_s.length];
			for (int i = 0; i < rewardRare_s.length; i++) {
				rewardRare[i] = Boolean.parseBoolean(rewardRare_s[i]);
			}
			String[] rewardBinds_s = cells[13].getContents().split(",");
			rewardBinds = new boolean[rewardBinds_s.length];
			for (int i = 0; i < rewardBinds_s.length; i++) {
				rewardBinds[i] = Boolean.parseBoolean(rewardBinds_s[i]);
			}
			
			mailTitle = cells[14].getContents();
			mailMsg = cells[15].getContents();
			
			rewardTempEntitys = new ArticleEntity[rewardPropNames.length];
			entityIDs = new long[rewardPropNames.length];
			for (int i = 0; i < rewardPropNames.length; i++) {
				String propName = rewardPropNames[i];
				Article a = ArticleManager.getInstance().getArticle(propName);
				if (a == null) {
					NewChongZhiActivityManager.logger.warn("[周累计活动] [{}]", new Object[]{propName});
					return;
				}
				try{
					rewardTempEntitys[i] = ArticleEntityManager.getInstance().createTempEntity(a, getRewardBinds()[i], ArticleEntityManager.活动, null, getRewardColors()[i]);
					entityIDs[i] = rewardTempEntitys[i].getId();
				}catch(Exception e) {
					NewChongZhiActivityManager.logger.error("创建临时物品出错", e);
				}
			}
		}
		
		public boolean SendReward(Player player, WeekChongZhiActivity ac) {
			try{
				ArticleEntity[] entitys = new ArticleEntity[rewardPropNames.length];
				for (int i = 0; i < rewardPropNames.length; i++) {
					String propName = rewardPropNames[i];
					Article a = ArticleManager.getInstance().getArticle(propName);
					if (a == null) {
						logger.warn("[物品不存在] [{}] [{}] [{}]", new Object[]{player.getLogString(), getId(), propName});
					}
					entitys[i] = ArticleEntityManager.getInstance().createEntity(a, getRewardBinds()[i], ArticleEntityManager.活动, player, getRewardColors()[i], getRewardPropNums()[i], true);
				}
				MailManager.getInstance().sendMail(player.getId(), entitys, getRewardPropNums(), mailTitle, mailMsg, 0, 0, 0, "2016周累计活动");
				player.send_HINT_REQ(Translate.周充值领取成功, (byte)5);
				return true;
			}catch(Exception e) {
				logger.error("发送奖励出错:["+player.getLogString()+"]["+ac.id+"] ["+Arrays.toString(rewardPropNames)+"]", e);
			}
			return false;
		}
		
		public void setTotalNum(int totalNum) {
			this.totalNum = totalNum;
		}
		public int getTotalNum() {
			return totalNum;
		}
		
		public String[] getRewardPropNames() {
			return rewardPropNames;
		}
		public void setRewardPropNames(String[] rewardPropNames) {
			this.rewardPropNames = rewardPropNames;
		}
		public ArticleEntity[] getRewardTempEntitys() {
			return rewardTempEntitys;
		}
		public void setRewardTempEntitys(ArticleEntity[] rewardTempEntitys) {
			this.rewardTempEntitys = rewardTempEntitys;
		}
		public int[] getRewardPropNums() {
			return rewardPropNums;
		}
		public void setRewardPropNums(int[] rewardPropNums) {
			this.rewardPropNums = rewardPropNums;
		}
		public int[] getRewardColors() {
			return rewardColors;
		}
		public void setRewardColors(int[] rewardColors) {
			this.rewardColors = rewardColors;
		}
		public boolean[] getRewardBinds() {
			return rewardBinds;
		}
		public void setRewardBinds(boolean[] rewardBinds) {
			this.rewardBinds = rewardBinds;
		}
		public boolean[] getRewardRare() {
			return rewardRare;
		}
		public void setRewardRare(boolean[] rewardRare) {
			this.rewardRare = rewardRare;
		}
		public String getMailTitle() {
			return mailTitle;
		}

		public void setMailTitle(String mailTitle) {
			this.mailTitle = mailTitle;
		}

		public String getMailMsg() {
			return mailMsg;
		}

		public void setMailMsg(String mailMsg) {
			this.mailMsg = mailMsg;
		}

		public void setEntityIDs(long[] entityIDs) {
			this.entityIDs = entityIDs;
		}

		public long[] getEntityIDs() {
			return entityIDs;
		}
	}
}
