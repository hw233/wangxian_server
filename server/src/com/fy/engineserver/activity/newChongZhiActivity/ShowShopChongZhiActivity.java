package com.fy.engineserver.activity.newChongZhiActivity;

import java.util.HashMap;

import com.fy.engineserver.sprite.Player;

/**
 *	周期内累计充值多少金额后可以看见某个商店
 */
public class ShowShopChongZhiActivity extends NewMoneyActivity {
	
	public static String DISK_KEY = "sShop";
	public HashMap<Long, Long> playerMoneys = new HashMap<Long, Long>();
	
	private long needMoney;							//累计奖励需要的金钱数目
	private String shopName;						//商店名字
	
	public boolean isShow (long pID, String shopName) {
		if (shopName.equals(this.shopName)) {
			Long m = playerMoneys.get(pID);
			if (m != null) {
				if (m.longValue() >= needMoney) {
					return true;
				}
			}
		}
		return false;
	}
	
	public ShowShopChongZhiActivity(int configID, String name, int platform,
			String startTime, String endTime, String[] serverNames,
			String[] unServerNames, String mailTitle, String mailMsg, String[] parameters)
			throws Exception {
		super(configID, name, platform, startTime, endTime, serverNames, unServerNames,
				mailTitle, mailMsg, parameters);
	}

	public void loadDiskCache() {
		try{
			Object obj = diskcache.get(DISK_KEY + getConfigID());
			if (obj == null) {
				diskcache.put(DISK_KEY + getConfigID(), playerMoneys);
			}else {
				playerMoneys = (HashMap<Long, Long>)obj;
			}
		}catch(Exception e) {
			logger.error("累计 loadDiskCache", e);
		}
	}

	public void doActivity(Player player, long yinzi) {
		try{
			long now = System.currentTimeMillis();
			if (now < getStartTimeLong() || now > getEndTimeLong()) {
				//活动还未开始
				return;
			}
			Long oldMoney = playerMoneys.get(player.getId());
			if (oldMoney == null) {
				oldMoney = new Long(0);
			}
			
			boolean isGive = false;
			if (oldMoney >= getNeedMoney()) {
				isGive = true;
			}
			oldMoney += yinzi;
			playerMoneys.put(player.getId(), oldMoney);
			diskcache.put(DISK_KEY + getConfigID(), playerMoneys);
			logger.warn("[商店充] {} [{}] [{}] [{}]", new Object[]{getLogString(), player.getLogString(), yinzi, oldMoney});
		}catch(Exception e) {
			logger.error("累计 doActivity", e);
		}
	}
	
	public void sendReward(Player player) {
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
			logger.error("累计 heatbeat", e);
		}
	}

	public void creatParameter(String[] parameters) {
		try{
			//金额
			setNeedMoney(Long.parseLong(parameters[0]));
			setShopName(parameters[1]);
		}catch(Exception e) {
			logger.error("累计 creatParameter", e);
		}
	}

	public String getParameter() {
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(getNeedMoney()).append("]");
		sb.append(" [").append(getShopName()).append("]");
		return sb.toString();
	}
	
	public String[] getActivityPartake() {
		String[] s = new String[playerMoneys.size()];
		int i = 0;
		for (Long pID : playerMoneys.keySet()) {
			s[i] = pID.longValue() + "---" + playerMoneys.get(pID);
			i++;
		}
		return s;
	}

	public void cleanActivityData() {
		if (playerMoneys.size() > 0) {
			logger.warn("[活动数据清除] {} [{}]", new Object[]{getLogString(), playerMoneys.size()});
			playerMoneys.clear();
			diskcache.put(DISK_KEY + getConfigID(), playerMoneys);
		}
	}

	public String getLogString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(getConfigID()).append("] ");
		sb.append("[").append(getName()).append("] ");
		sb.append("[").append(getStartTime()).append("] ");
		sb.append("[").append(getEndTime()).append("] ");
		sb.append("[").append(getNeedMoney()).append("] ");
		sb.append("[").append(getShopName()).append("]");
		return sb.toString();
	}

	public void setNeedMoney(long needMoney) {
		this.needMoney = needMoney;
	}

	public long getNeedMoney() {
		return needMoney;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopName() {
		return shopName;
	}
}
