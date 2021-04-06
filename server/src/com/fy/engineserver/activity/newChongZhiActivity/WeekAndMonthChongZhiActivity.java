package com.fy.engineserver.activity.newChongZhiActivity;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.slf4j.Logger;

import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.vipExpActivity.VipExpActivityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.economic.charge.ChargeRatio;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.server.TestServerConfigManager;
import com.sqage.stat.client.StatClientService;
import com.sqage.stat.model.LibaoFlow;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.diskcache.DiskCache;

public class WeekAndMonthChongZhiActivity {

	public static DiskCache diskcache;
	public static Logger logger;
	
	public static String DISK_KEY_MONEY = "weekAndMonth_moneys";
	public HashMap<Long, Long> player_moneys = new HashMap<Long, Long>();			//活动期间充值金额
	
	public static String DISK_KEY_GET = "weekAndMonth_get";
	public HashSet<Long> player_gets = new HashSet<Long>();							//活动是否领取奖励
	
	public static String DISK_KEY_BUY = "weekAndMonth_buy";
	public HashSet<Long> player_buys = new HashSet<Long>();							//活动是否买过奖励
	
	private int dataID;
	private int type;								//活动类型  0是周，1是月
	private int platform;							//平台			0是官方    1是台湾    2是腾讯  3是马来  4韩服
	private String[] serverNames;					//服务器名字
	private String[] unServerNames;					//不参加服务器名字
	private long showRMBMoney;						//显示的RMB金额
	private long needMoney;							//需要金额
	private String startTime;						//开始时间
	private String endTime;							//结束时间
	private long startTimeLong;						//开始时间
	private long endTimeLong;						//开始时间
	private String mailTitle;						//邮件标题
	private String mailMsg;							//邮件内容
	private String[] rewardPropNames;				//奖励物品名字
	private ArticleEntity[] rewardTempEntitys;		//奖励物品Entity
	private int[] rewardPropNums;					//奖励物品数目
	private int[] rewardColors;						//奖励物品颜色
	private boolean[] rewardBinds;					//奖励绑定情况
	private boolean[] rewardRare;					//是否珍贵
	private String[] buyPropNames;					//购买物品名字
	private ArticleEntity[] buyTempEntitys;			//购买物品Entity
	private int[] buyPropNums;						//购买物品数目
	private int[] buyColors;						//购买物品颜色
	private boolean[] buyBinds;						//购买绑定情况
	private boolean[] buyRare;						//购买是否珍贵
	private long buyPrice;							//购买价钱
	
	public WeekAndMonthChongZhiActivity (int dataID, int type, int platform, String[] serverNames, String[] unServerNames,
			long showRMBMoney, long needMoney, String startTime, String endTime, String mailTitle, String mailMsg,
			String[] rewardPropNames, int[] rewardPropNums, int[] rewardColors, boolean[] rewardBinds, boolean[] rewardRare, 
			String[] buyPropNames, int[] buyPropNums, int[] buyColors, boolean[] buyBinds, boolean[] buyRare, 
			long buyPrice) throws Exception{
		this.dataID = dataID;
		this.type = type;
		this.platform = platform;
		this.serverNames = serverNames;
		this.unServerNames = unServerNames;
		this.showRMBMoney = showRMBMoney;
		this.needMoney = needMoney;
		this.startTime = startTime;
		this.endTime = endTime;
		this.mailTitle = mailTitle;
		this.mailMsg = mailMsg;
		this.rewardPropNames = rewardPropNames;
		this.rewardPropNums = rewardPropNums;
		this.rewardColors = rewardColors;
		this.rewardBinds = rewardBinds;
		this.rewardRare = rewardRare;
		this.buyPropNames = buyPropNames;
		this.buyPropNums = buyPropNums;
		this.buyColors = buyColors;
		this.buyBinds = buyBinds;
		this.buyRare = buyRare;
		this.buyPrice = buyPrice;
		
		//时间转换long
		createLongTime();
		
		//读DiskCache
		Object obj = diskcache.get(DISK_KEY_MONEY + getDataID());
		if (obj == null) {
			diskcache.put(DISK_KEY_MONEY + getDataID(), player_moneys);
		}else {
			player_moneys = (HashMap<Long, Long>)obj;
		}
		Object obj1 = diskcache.get(DISK_KEY_GET + getDataID());
		if (obj1 == null) {
			diskcache.put(DISK_KEY_GET + getDataID(), player_gets);
		}else {
			player_gets = (HashSet<Long>)obj1;
		}
		Object obj2 = diskcache.get(DISK_KEY_BUY + getDataID());
		if (obj2 == null) {
			diskcache.put(DISK_KEY_BUY + getDataID(), player_buys);
		}else {
			player_buys = (HashSet<Long>)obj2;
		}
		
		//生成临时Entity
		rewardTempEntitys = new ArticleEntity[rewardPropNames.length];
		for (int i = 0; i < rewardPropNames.length; i++) {
			String propName = rewardPropNames[i];
			Article a = ArticleManager.getInstance().getArticle(propName);
			if (a == null) {
				logger.warn("[周月物品不存在] [{}] [{}]", new Object[]{getDataID(), propName});
				return;
			}
			try{
				rewardTempEntitys[i] = ArticleEntityManager.getInstance().createTempEntity(a, getRewardBinds()[i], ArticleEntityManager.活动, null, getRewardColors()[i]);
			}catch(Exception e) {
				logger.error("创建临时物品出错", e);
			}
		}
		
		buyTempEntitys = new ArticleEntity[buyPropNames.length];
		for (int i = 0; i < buyPropNames.length; i++) {
			String propName = buyPropNames[i];
			Article a = ArticleManager.getInstance().getArticle(propName);
			if (a == null) {
				logger.warn("[周月物品不存在] [{}] [{}]", new Object[]{getDataID(), propName});
				return;
			}
			try{
				buyTempEntitys[i] = ArticleEntityManager.getInstance().createTempEntity(a, getBuyBinds()[i], ArticleEntityManager.活动, null, getBuyColors()[i]);
			}catch(Exception e) {
				logger.error("创建临时物品出错", e);
			}
		}
	}
	
	public WeekAndMonthClientData getClientData(Player player) {
		WeekAndMonthClientData data = new WeekAndMonthClientData();
		data.setDataID(dataID);
		data.setType(type);
		data.setShowRMBMoney(showRMBMoney);
		data.setNeedMoney(needMoney);
		String startTimeString = startTime.split(" ")[0];
		data.setStartTime(startTimeString);
		String endTimeString = endTime.split(" ")[0];
		data.setEndTime(endTimeString);
		long[] rewardIDs = new long[rewardTempEntitys.length];
		for (int i = 0; i < rewardTempEntitys.length; i++) {
			rewardIDs[i] = rewardTempEntitys[i].getId();
		}
		data.setRewardEntityIDs(rewardIDs);
		data.setRewardEntityNums(rewardPropNums);
		data.setRewardRare(rewardRare);
		long[] buyIDs = new long[buyTempEntitys.length];
		for (int i = 0; i < buyTempEntitys.length; i++) {
			buyIDs[i] = buyTempEntitys[i].getId();
		}
		data.setBuyEntityIDs(buyIDs);
		data.setBuyEntityNums(buyPropNums);
		data.setBuyRare(buyRare);
		data.setBuyPrice(buyPrice);
		Long money = player_moneys.get(player.getId());
		long tMoney = 0;
		if (money != null) {
			tMoney = money.longValue();
		}
		data.setTotalMoney(tMoney);
		data.setTotalRMB(tMoney/ChargeRatio.DEFAULT_CHARGE_RATIO);
		if (PlatformManager.getInstance().isPlatformOf(Platform.台湾)) {
			data.setTotalRMB(tMoney/1000);
		}
		int getV = -1;
		if (tMoney >= needMoney) {
			if (player_gets.contains(player.getId())){
				getV = 1;
			}else {
				getV = 0;
			}
		}
		data.setGetValue(getV);
		int canBuy = -1;
		if (tMoney >= needMoney) {
			if (player_buys.contains(player.getId())) {
				canBuy = 1;
			}else {
				canBuy = 0;
			}
		}
		data.setCanBuy(canBuy);
		return data;
	}
	
	public String getLogString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(dataID).append("] ");
		sb.append("[").append(needMoney).append("] ");
		sb.append("[").append(type).append("] ");
		sb.append("[").append(platform).append("] ");
		sb.append("[").append(Arrays.toString(getServerNames())).append("] ");
		sb.append("[").append(Arrays.toString(getUnServerNames())).append("] ");
		sb.append("[").append(Arrays.toString(rewardPropNames)).append("] ");
		sb.append("[").append(Arrays.toString(rewardPropNums)).append("]");
		sb.append("[").append(Arrays.toString(buyPropNames)).append("] ");
		sb.append("[").append(Arrays.toString(buyPropNums)).append("] ");
		sb.append("[").append(buyPrice).append("]");
		return sb.toString();
	}
	
	public void doChongZhi (Player player, long yinzi) {
		if (!isInTime()) {
			return;
		}
		Long money = player_moneys.get(player.getId());
		if (money == null) {
			money = new Long(0L);
		}
		long oldMoney = money.longValue();
		money = money + yinzi;
		player_moneys.put(player.getId(), money);
		if (money >= getNeedMoney() && oldMoney < getNeedMoney()) {
			sendTongJi(0, 1);
		}
		logger.warn("[参与周月] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), getLogString(), yinzi, money});
//		}
		diskcache.put(DISK_KEY_MONEY + getDataID(), player_moneys);
	}
	
	//领取奖励
	public String getRewards (Player player) {
		Long money = player_moneys.get(player.getId());
		if (money == null) {
			return Translate.您还未充值达到领取奖励要求;
		}
		if (money.longValue() < getNeedMoney()) {
			return Translate.您还未充值达到领取奖励要求;
		}
		if (player_gets.contains(player.getId())) {
			return Translate.您已经领取过此充值奖励;
		}
		try {
			ArticleEntity[] entitys = new ArticleEntity[rewardPropNames.length];
			for (int i = 0; i < rewardPropNames.length; i++) {
				String propName = rewardPropNames[i];
				Article a = ArticleManager.getInstance().getArticle(propName);
				if (a == null) {
					logger.warn("[物品不存在] [{}] [{}] [{}]", new Object[]{player.getLogString(), getDataID(), propName});
				}
				entitys[i] = ArticleEntityManager.getInstance().createEntity(a, getRewardBinds()[i], ArticleEntityManager.活动, player, getRewardColors()[i], getRewardPropNums()[i], true);
			}
			
			player_gets.add(player.getId());
			diskcache.put(DISK_KEY_GET + getDataID(), player_gets);
			
			MailManager.getInstance().sendMail(player.getId(), entitys, getRewardPropNums(), getMailTitle(), getMailMsg(), 0, 0, 0, "周月活动");
			sendTongJi(0, 2);
			logger.warn("[周月领取奖励] [{}] [{}]", new Object[]{player.getLogString(), getLogString()});
			return null;
		} catch (Exception e) {
			logger.error("领取奖励失败" + player.getLogString(), e);
		}
		return Translate.领取奖励失败;
	}
	
	//购买物品
	public String buyProps (Player player) {
		Long money = player_moneys.get(player.getId());
		if (money == null) {
			return Translate.您还未充值达到领取奖励要求;
		}
		if (money.longValue() < getNeedMoney()) {
			return Translate.您还未充值达到领取奖励要求;
		}
		if (player_buys.contains(player.getId())) {
			return Translate.您已经领取过此充值奖励;
		}
		if (player.getSilver() < getBuyPrice()) {
			return Translate.text_jiazu_103;
		}
		if (player.getKnapsack_common().getEmptyNum() < buyPropNames.length) {
			return Translate.您背包空间不够;
		}
		try {
			ArticleEntity[] entitys = new ArticleEntity[buyPropNames.length];
			for (int i = 0; i < buyPropNames.length; i++) {
				String propName = buyPropNames[i];
				Article a = ArticleManager.getInstance().getArticle(propName);
				if (a == null) {
					logger.warn("[物品不存在] [{}] [{}] [{}]", new Object[]{player.getLogString(), getDataID(), propName});
					return Translate.购买奖励失败;
				}
				entitys[i] = ArticleEntityManager.getInstance().createEntity(a, getBuyBinds()[i], ArticleEntityManager.活动, player, getBuyColors()[i], getBuyPropNums()[i], true);
			}
			
			BillingCenter.getInstance().playerExpense(player, getBuyPrice(), CurrencyType.YINZI, ExpenseReasonType.weekAndMonthActivity, "周月反馈购买", VipExpActivityManager.default_add_rmb_expense);
			
			player_buys.add(player.getId());
			diskcache.put(DISK_KEY_BUY + getDataID(), player_buys);
			for (int i = 0; i < entitys.length; i++) {
				player.putToKnapsacks(entitys[i], getBuyPropNums()[i], "购买周月奖励");
			}
			sendTongJi(1, 2);
			logger.warn("[周月购买奖励] [{}] [{}]", new Object[]{player.getLogString(), getLogString()});
			try {
				EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.购买单月礼包次数, 1L});
				EventRouter.getInst().addEvent(evt2);
			} catch (Exception eex) {
				PlayerAimManager.logger.error("[目标系统] [统计购买周月礼包次数异常] [" + player.getLogString() + "]", eex);
			}
			return null;
		} catch (Exception e) {
			logger.error("购买奖励失败" + player.getLogString(), e);
		}
		return Translate.购买奖励失败;
	}
	
	public void createLongTime() throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		startTimeLong = format.parse(getStartTime()).getTime();
		endTimeLong = format.parse(getEndTime()).getTime();
	}
	
	public boolean isInTime () {
		long now = System.currentTimeMillis();
		if (getStartTimeLong() > now || getEndTimeLong() < now) {
			return false;
		}
		return true;
	}
	
	public boolean isCanServer() {
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
	
	public void setDataID(int dataID) {
		this.dataID = dataID;
	}
	public int getDataID() {
		return dataID;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
	}
	public void setPlatform(int platform) {
		this.platform = platform;
	}
	public int getPlatform() {
		return platform;
	}
	public void setServerNames(String[] serverNames) {
		this.serverNames = serverNames;
	}
	public String[] getServerNames() {
		return serverNames;
	}
	public void setUnServerNames(String[] unServerNames) {
		this.unServerNames = unServerNames;
	}
	public String[] getUnServerNames() {
		return unServerNames;
	}
	public void setNeedMoney(long needMoney) {
		this.needMoney = needMoney;
	}
	public long getNeedMoney() {
		return needMoney;
	}
	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}
	public String getMailTitle() {
		return mailTitle;
	}
	public void setMailMsg(String mailMsg) {
		this.mailMsg = mailMsg;
	}
	public String getMailMsg() {
		return mailMsg;
	}
	public void setShowRMBMoney(long showRMBMoney) {
		this.showRMBMoney = showRMBMoney;
	}
	public long getShowRMBMoney() {
		return showRMBMoney;
	}
	public void setRewardPropNames(String[] rewardPropNames) {
		this.rewardPropNames = rewardPropNames;
	}
	public String[] getRewardPropNames() {
		return rewardPropNames;
	}
	public void setRewardPropNums(int[] rewardPropNums) {
		this.rewardPropNums = rewardPropNums;
	}
	public int[] getRewardPropNums() {
		return rewardPropNums;
	}
	public void setRewardColors(int[] rewardColors) {
		this.rewardColors = rewardColors;
	}
	public int[] getRewardColors() {
		return rewardColors;
	}
	public void setRewardBinds(boolean[] rewardBinds) {
		this.rewardBinds = rewardBinds;
	}
	public boolean[] getRewardBinds() {
		return rewardBinds;
	}
	public void setRewardRare(boolean[] rewardRare) {
		this.rewardRare = rewardRare;
	}
	public boolean[] getRewardRare() {
		return rewardRare;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setStartTimeLong(long startTimeLong) {
		this.startTimeLong = startTimeLong;
	}
	public long getStartTimeLong() {
		return startTimeLong;
	}
	public void setEndTimeLong(long endTimeLong) {
		this.endTimeLong = endTimeLong;
	}
	public long getEndTimeLong() {
		return endTimeLong;
	}
	public void setBuyPropNames(String[] buyPropNames) {
		this.buyPropNames = buyPropNames;
	}
	public String[] getBuyPropNames() {
		return buyPropNames;
	}
	public void setBuyPropNums(int[] buyPropNums) {
		this.buyPropNums = buyPropNums;
	}
	public int[] getBuyPropNums() {
		return buyPropNums;
	}
	public void setBuyColors(int[] buyColors) {
		this.buyColors = buyColors;
	}
	public int[] getBuyColors() {
		return buyColors;
	}
	public void setBuyBinds(boolean[] buyBinds) {
		this.buyBinds = buyBinds;
	}
	public boolean[] getBuyBinds() {
		return buyBinds;
	}
	public void setBuyRare(boolean[] buyRare) {
		this.buyRare = buyRare;
	}
	public boolean[] getBuyRare() {
		return buyRare;
	}
	public void setBuyPrice(long buyPrice) {
		this.buyPrice = buyPrice;
	}
	public long getBuyPrice() {
		return buyPrice;
	}

	public void setRewardTempEntitys(ArticleEntity[] rewardTempEntitys) {
		this.rewardTempEntitys = rewardTempEntitys;
	}

	public ArticleEntity[] getRewardTempEntitys() {
		return rewardTempEntitys;
	}

	public void setBuyTempEntitys(ArticleEntity[] buyTempEntitys) {
		this.buyTempEntitys = buyTempEntitys;
	}

	public ArticleEntity[] getBuyTempEntitys() {
		return buyTempEntitys;
	}
	
	public void sendTongJi (int type, int state) {
		if (TestServerConfigManager.isTestServer()) {
			return;
		}
		LibaoFlow libaoFlow=new LibaoFlow();
	    libaoFlow.setColumn1("");
	    libaoFlow.setColumn2("");
	    libaoFlow.setCount(1);
	    libaoFlow.setCreateDate(System.currentTimeMillis());
	    if (type == 0) {
	    	libaoFlow.setDanjia(0L);
	    }else {
	    	libaoFlow.setDanjia(getBuyPrice());
	    }
	    if (getType() == 0) {
	    	libaoFlow.setDaoJuName(getShowRMBMoney()+"元周礼包活动");
	    }else {
	    	libaoFlow.setDaoJuName(getShowRMBMoney()+"元月礼包活动");
	    }
	    libaoFlow.setFenQu(GameConstants.getInstance().getServerName());
	    libaoFlow.setType(state);
	    StatClientService.getInstance().sendLibaoFlow("",libaoFlow);
	}
}
