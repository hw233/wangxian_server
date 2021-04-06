package com.fy.engineserver.activity.newChongZhiActivity;

import java.util.HashSet;

import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.sprite.Player;

/**
 *	首充 返利
 */
public class FirstChongZhiFanLiActivity extends NewMoneyActivity {

	public static String DISK_KEY = "shouchongFanLi";
	public HashSet<Long> activityPlayers = new HashSet<Long>();
	
	private long needMoney;							//首充奖励需要的金钱数目
	private int backBiLi;							//返利比例
	
	public FirstChongZhiFanLiActivity(int configID, String name, int platform, String startTime,
			String endTime, String[] serverNames, String[] unServerNames,
			String mailTitle, String mailMsg, String[] parameters) throws Exception {
		super(configID, name, platform, startTime, endTime, serverNames, unServerNames,
				mailTitle, mailMsg, parameters);
		
	}

	public void doActivity(Player player, long yinzi) {
		try{
			long now = System.currentTimeMillis();
			if (now < getStartTimeLong() || now > getEndTimeLong()) {
				//活动还未开始
				return;
			}
			//已经参加过首充
			if (activityPlayers.contains(player.getId())) {
				return;
			}
			//钱不够
			if (yinzi < getNeedMoney()) {
				return;
			}
			//给东西
			activityPlayers.add(player.getId());
			diskcache.put(DISK_KEY + getConfigID(), activityPlayers);
			long backMoney = (long)(yinzi * ((double)backBiLi / 10000));
			if (backMoney <= 0) {
				backMoney = 1;
			}
			MailManager.getInstance().sendMail(player.getId(), null, getMailTitle(), getMailMsg(), backMoney, 0, 0, "充值活动");
			logger.warn("[首充返利得奖] {} [{}] [{}]", new Object[]{getLogString(), player.getLogString()});
		}catch(Exception e) {
			logger.error("首充 doActivity", e);
		}
	}
	
	public void sendReward(Player player) {
	}
	
	public String getLogString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(getConfigID()).append("] ");
		sb.append("[").append(getName()).append("] ");
		sb.append("[").append(getStartTime()).append("] ");
		sb.append("[").append(getEndTime()).append("] ");
		sb.append("[").append(getNeedMoney()).append("] ");
		sb.append("[").append(getBackBiLi()).append("]");
		return sb.toString();
	}

	public void heatbeat() {
		try{
			long now = System.currentTimeMillis();
			if (now < getStartTimeLong() || now > getEndTimeLong()) {
				//活动还未开始
				cleanActivityData();
				return;
			}
		}catch(Exception e) {
			logger.error("首充 heatbeat", e);
		}
	}

	public void creatParameter(String[] parameters) {
		try{
			//金额
			setNeedMoney(Long.parseLong(parameters[0]));
			//返利比例
			setBackBiLi(Integer.parseInt(parameters[1]));
		}catch(Exception e) {
			logger.error("首充 creatParameter", e);
		}
	}
	
	public String getParameter() {
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(getNeedMoney()).append("]");
		sb.append(" [").append(getBackBiLi()).append("]");
		return sb.toString();
	}
	
	public void cleanActivityData() {
		if (activityPlayers.size() > 0) {
			logger.warn("[活动数据清除] {} [{}]", new Object[]{getLogString(), activityPlayers.size()});
			activityPlayers.clear();
			diskcache.put(DISK_KEY + getConfigID(), activityPlayers);
		}
	}
	
	public String[] getActivityPartake() {
		String[] s = new String[activityPlayers.size()];
		int i = 0;
		for (Long pID : activityPlayers) {
			s[i] = pID.longValue() + "";
			i++;
		}
		return s;
	}

	public void loadDiskCache() {
		try{
			Object obj = diskcache.get(DISK_KEY + getConfigID());
			if (obj == null) {
				diskcache.put(DISK_KEY + getConfigID(), activityPlayers);
			}else {
				activityPlayers = (HashSet<Long>)obj;
			}
		}catch(Exception e) {
			logger.error("首充 loadDiskCache", e);
		}
	}

	public void setNeedMoney(long needMoney) {
		this.needMoney = needMoney;
	}

	public long getNeedMoney() {
		return needMoney;
	}

	public void setBackBiLi(int backBiLi) {
		this.backBiLi = backBiLi;
	}

	public int getBackBiLi() {
		return backBiLi;
	}

}
