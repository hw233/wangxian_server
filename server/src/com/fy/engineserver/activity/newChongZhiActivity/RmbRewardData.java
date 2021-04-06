package com.fy.engineserver.activity.newChongZhiActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

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

public class RmbRewardData {

	public static DiskCache diskcache;
	public static Logger logger;
	
	public static String DISK_KEY = "rmbReward";
	public HashSet<Long> playerGetReward = new HashSet<Long>();
	
	public static String DISK_KEY_1 = "rmbPlayerData";					//累计充值记录玩家充值金额,首充也记录金额
	public HashMap<Long, Long> playerLeiJiMoneys = new HashMap<Long, Long>();
	
	private int dataID;
	private int type;						//活动类型  0是首充，1是累计充值
	private int platform;					//平台			0是官方    1是台湾    2是腾讯  3是马来
	private String[] serverNames;			//服务器名字
	private String[] unServerNames;			//不参加服务器名字
	private long needMoney;							//需要金额
	private String mailTitle;						//邮件标题
	private String mailMsg;							//邮件内容
	private String notifyMsg;						//广播msg
	private String[] rewardPropNames;				//奖励物品名字
	private int[] rewardPropNums;					//奖励物品数目
	private int[] rewardColors;						//奖励物品颜色
	private boolean[] rewardBinds;					//奖励绑定情况
	private boolean[] rewardRare;					//是否珍贵
	private int showType;							//显示类型 0是元 1是银子
	private long showMoney;							//显示数值
	
	private ArticleEntity[] tempEntitys;			//临时，用来客户端显示
	
	public RmbRewardData (int id, long needMoney, int type, int platform, String[] serverNames, String[] unServerNames, String mailTitle, String mailMsg, String notifyMsg, String[] rewardPropNames, int[] rewardPropNums, int[] rewardColors, boolean[] rewardBinds, boolean[] rewardRare, int showType, long showMoney) {
		this.dataID = id;
		this.needMoney = needMoney;
		this.type = type;
		this.platform = platform;
		this.serverNames = serverNames;
		this.unServerNames = unServerNames;
		this.mailTitle = mailTitle;
		this.mailMsg = mailMsg;
		this.notifyMsg = notifyMsg;
		this.rewardPropNames = rewardPropNames;
		this.rewardPropNums = rewardPropNums;
		this.rewardColors = rewardColors;
		this.rewardBinds = rewardBinds;
		this.rewardRare = rewardRare;
		this.showType = showType;
		this.showMoney = showMoney;
		Object obj = diskcache.get(DISK_KEY + getDataID());
		if (obj == null) {
			diskcache.put(DISK_KEY + getDataID(), playerGetReward);
		}else {
			playerGetReward = (HashSet<Long>)obj;
		}
		Object obj1 = diskcache.get(DISK_KEY_1 + getDataID());
		if (obj1 == null) {
			diskcache.put(DISK_KEY_1 + getDataID(), playerLeiJiMoneys);
		}else {
			playerLeiJiMoneys = (HashMap<Long, Long>)obj1;
		}
		tempEntitys = new ArticleEntity[rewardPropNames.length];
		for (int i = 0; i < rewardPropNames.length; i++) {
			String propName = rewardPropNames[i];
			Article a = ArticleManager.getInstance().getArticle(propName);
			if (a == null) {
				logger.warn("[物品不存在] [{}] [{}]", new Object[]{getDataID(), propName});
				return;
			}
			try{
				tempEntitys[i] = ArticleEntityManager.getInstance().createTempEntity(a, getRewardBinds()[i], ArticleEntityManager.活动, null, getRewardColors()[i]);
			}catch(Exception e) {
				logger.error("创建临时物品出错", e);
			}
		}
	}
	
	public String getLogString () {
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(dataID).append("] ");
		sb.append("[").append(needMoney).append("] ");
		sb.append("[").append(type).append("] ");
		sb.append("[").append(platform).append("] ");
		sb.append("[").append(Arrays.toString(getServerNames())).append("] ");
		sb.append("[").append(Arrays.toString(getUnServerNames())).append("] ");
		sb.append("[").append(Arrays.toString(rewardPropNames)).append("] ");
		sb.append("[").append(Arrays.toString(rewardPropNums)).append("]");
		return sb.toString();
	}
	
	public RmbRewardClientData getClientData(Player player) {
		if (tempEntitys == null) {
			return null;
		}
		RmbRewardClientData data = new RmbRewardClientData();
		data.setDataID(getDataID());
		data.setNeedMoney(getNeedMoney());
		data.setRewardRare(getRewardRare());
		data.setIsGetBefore(playerGetReward.contains(player.getId()));
		long[] entityID = new long[tempEntitys.length];
		for (int i = 0; i < tempEntitys.length; i++) {
			entityID[i] = tempEntitys[i].getId();
		}
		data.setEntityID(entityID);
		data.setEntityNums(rewardPropNums);
		data.setShowType(showType);
		data.setShowMoney(showMoney);
		return data;
	}
	
	public void doChongZhi (Player player, long yinzi) {
//		if (type == 0) {
//			if (yinzi >= getNeedMoney()) {
//				playerLeiJiMoneys.put(player.getId(), yinzi);
//				logger.warn("[参与首充] [{}] [{}] [{}]", new Object[]{player.getLogString(), getLogString(), yinzi});
//			}
//		}else {
		Long money = playerLeiJiMoneys.get(player.getId());
		if (money == null) {
			money = new Long(0L);
		}
		money = money + yinzi;
		playerLeiJiMoneys.put(player.getId(), money);
		logger.warn("[参与累计] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), getLogString(), yinzi, money});
//		}
		diskcache.put(DISK_KEY_1 + getDataID(), playerLeiJiMoneys);
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
	
	public String sendReward(Player player) {
		if (playerGetReward.contains(player.getId())) {
			return Translate.您已经领取过此奖励;
		}
		Long money = playerLeiJiMoneys.get(player.getId());
		if (money == null) {
			money = 0L;
		}
		if (money < getNeedMoney()) {
			return Translate.您充值金额不够不能领取此奖励;
		}
		try{
			ArticleEntity[] entitys = new ArticleEntity[rewardPropNames.length];
			for (int i = 0; i < rewardPropNames.length; i++) {
				String propName = rewardPropNames[i];
				Article a = ArticleManager.getInstance().getArticle(propName);
				if (a == null) {
					logger.warn("[物品不存在] [{}] [{}] [{}]", new Object[]{player.getLogString(), getDataID(), propName});
					return "物品不存在，领取错误";
				}
				entitys[i] = ArticleEntityManager.getInstance().createEntity(a, getRewardBinds()[i], ArticleEntityManager.活动, player, getRewardColors()[i], getRewardPropNums()[i], true);
			}
			playerGetReward.add(player.getId());
			diskcache.put(DISK_KEY + getDataID(), playerGetReward);
			if (entitys.length > 5) {
				int i = 0;
				for ( ; i < entitys.length/5; i++) {
					ArticleEntity[] entitys_1 = new ArticleEntity[5];
					int[] entityNums_1 = new int[5];
					for (int j = 0; j < 5; j++) {
						entitys_1[j] = entitys[j + i * 5];
						entityNums_1[j] = getRewardPropNums()[j + i * 5];
					}
					logger.warn("[发放物品数目] [{}] [{}]", new Object[]{Arrays.toString(entityNums_1), Arrays.toString(getRewardPropNums())});
					MailManager.getInstance().sendMail(player.getId(), entitys_1, entityNums_1, getMailTitle(), getMailMsg(), 0, 0, 0, "充值活动");
				}
				int have = entitys.length % 5;
				if (have > 0) {
					
					ArticleEntity[] entitys_1 = new ArticleEntity[have];
					int[] entityNums_1 = new int[have];
					for (int j = 0; j < have; j++) {
						entitys_1[j] = entitys[j + i * 5];
						entityNums_1[j] = getRewardPropNums()[j + i * 5];
					}
					logger.warn("[发放物品数目] [{}] [{}]", new Object[]{Arrays.toString(entityNums_1), Arrays.toString(getRewardPropNums())});
					MailManager.getInstance().sendMail(player.getId(), entitys_1, entityNums_1, getMailTitle(), getMailMsg(), 0, 0, 0, "充值活动");
				}
			}else {
				MailManager.getInstance().sendMail(player.getId(), entitys, getRewardPropNums(), getMailTitle(), getMailMsg(), 0, 0, 0, "充值活动");
			}
			logger.warn("[rmb充值累计] [{}] [{}] [{}]", new Object[]{getDataID(),getNeedMoney(), player.getLogString()});
			return "";
		}catch(Exception e){
			logger.error("", e);
		}
		return "未知错误。";
	}
	
	public void setNeedMoney(long needMoney) {
		this.needMoney = needMoney;
	}
	public long getNeedMoney() {
		return needMoney;
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

	public void setDataID(int dataID) {
		this.dataID = dataID;
	}

	public int getDataID() {
		return dataID;
	}

	public void setTempEntitys(ArticleEntity[] tempEntitys) {
		this.tempEntitys = tempEntitys;
	}

	public ArticleEntity[] getTempEntitys() {
		return tempEntitys;
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

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setShowType(int showType) {
		this.showType = showType;
	}

	public int getShowType() {
		return showType;
	}

	public void setShowMoney(long showMoney) {
		this.showMoney = showMoney;
	}

	public long getShowMoney() {
		return showMoney;
	}

	public void setNotifyMsg(String notifyMsg) {
		this.notifyMsg = notifyMsg;
	}

	public String getNotifyMsg() {
		return notifyMsg;
	}
}
