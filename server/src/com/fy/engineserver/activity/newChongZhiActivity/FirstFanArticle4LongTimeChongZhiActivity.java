package com.fy.engineserver.activity.newChongZhiActivity;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.sprite.Player;

public class FirstFanArticle4LongTimeChongZhiActivity extends NewMoneyActivity {
	
	public static String DISK_KEY = "fristfanArticle";
	public HashMap<Long, Long> playerMoneys = new HashMap<Long, Long>();	//玩家充值金额
	public static String DISK_KEY1 = "1firstfanArticle";
	public long sendFanLiTime;												//返利时间

	private String[] fanliTimes;				//返利时间字符串
	private long[] fanliSpaces;					//返利的时间间隔			//毫秒
	private long[] fanliDatas;					//返利时间
	private String[] fanArticleNames;			//返还物品名字
	private int[] fanArticleNums;				//返还物品数目
	private boolean[] fanArticleBinds;			//返还物品绑定
	
	public FirstFanArticle4LongTimeChongZhiActivity(int configID, String name,
			int platform, String startTime, String endTime,
			String[] serverNames, String[] unServerNames, String mailTitle,
			String mailMsg, String[] parameters) throws Exception {
		super(configID, name, platform, startTime, endTime, serverNames, unServerNames,
				mailTitle, mailMsg, parameters);
	}

	public void loadDiskCache() {
		try{
			Object obj = diskcache.get(DISK_KEY+ getConfigID());
			if (obj == null) {
				diskcache.put(DISK_KEY + getConfigID(), playerMoneys);
			}else {
				playerMoneys = (HashMap<Long, Long>)obj;
			}
			Object obj1 = diskcache.get(DISK_KEY1+ getConfigID());
			if (obj1 == null) {
				diskcache.put(DISK_KEY1 + getConfigID(), sendFanLiTime);
			}else {
				sendFanLiTime = Long.parseLong(obj1.toString());
			}
		}catch(Exception e) {
			logger.error("返LT loadDiskCache", e);
		}
	}

	public void doActivity(Player player, long yinzi) {
		try{
			if (player.getRMB() > 0) {
				return;
			}
			long now = System.currentTimeMillis();
			if (now < getStartTimeLong() || now > getEndTimeLong()) {
				return;
			}
			Long oldMoney = playerMoneys.get(player.getId());
			if (oldMoney == null) {
				oldMoney = 0L;
			}
			oldMoney += yinzi;
			playerMoneys.put(player.getId(), oldMoney);
			diskcache.put(DISK_KEY + getConfigID(), playerMoneys);
			logger.warn("[首次返物品充] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), yinzi, oldMoney, getConfigID(), getName()});
		}catch(Exception e) {
			logger.error("首次返物品 heatbeat", e);
		}
	}

	public void heatbeat() {
		try{
			long now = System.currentTimeMillis();
			if (now >= fanliDatas[0] && now <= fanliDatas[fanliDatas.length-1]) {
				//在返利期间内
				int index = -1;
				for (int i = 1; i < fanliDatas.length; i++) {
					if (fanliDatas[i] >= now) {
						index = i - 1;
						break;
					}
				}
				if (index >= 0) {
					if (now - sendFanLiTime >= fanliSpaces[index]) {
						sendFanLiTime = now;
						diskcache.put(DISK_KEY1 + getConfigID(), sendFanLiTime);
						for (Long playerID : playerMoneys.keySet()) {
							try{
								Long money = playerMoneys.get(playerID);
								Article a = ArticleManager.getInstance().getArticle(getFanArticleNames()[index]);
								if (a == null) {
									logger.warn("[首次返物品物品不存在] [{}] [{}] [{}] [{}]", new Object[]{playerID, money, getConfigID(), getFanArticleNames()[index]});
									return;
								}
								ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(a, getFanArticleBinds()[index], ArticleEntityManager.活动, null, -1, getFanArticleNums()[index], true);
								MailManager.getInstance().sendMail(playerID, new ArticleEntity[]{entity}, new int[]{getFanArticleNums()[index]}, getMailTitle(), getMailMsg(), 0, 0, 0, "充值活动");
								logger.warn("[首次返物品奖] {} [{}] [{}] [{}]", new Object[]{getConfigID() + " " + getName(), playerID, getFanArticleNames()[index], index});
							}catch(Exception e) {
								logger.error("首次返物品奖出错", e);
							}
						}
						logger.warn("[首次返物品一次] {} [{}] [{}]", new Object[]{getLogString(), playerMoneys.size(), index});
					}
				}
			}else if (now > fanliDatas[fanliDatas.length-1]) {
				cleanActivityData();
			}
		}catch(Exception e) {
			logger.error("首次返物品 heatbeat", e);
		}
	}

	public void creatParameter(String[] parameters) {
		try{
			//返利时间
			fanliTimes = parameters[0].split(",");
			fanliDatas = new long[fanliTimes.length];
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i < fanliDatas.length; i++) {
				fanliDatas[i] = format.parse(fanliTimes[i]).getTime();
			}
			//返利间隔
			String[] fanliSpaceString = parameters[1].split(",");
			fanliSpaces = new long[fanliSpaceString.length];
			for (int i = 0; i < fanliSpaceString.length; i++) {
				fanliSpaces[i] = Long.parseLong(fanliSpaceString[i]);
			}
			//返物品名字
			String[] fanArticleNameString = parameters[2].split(",");
			setFanArticleNames(fanArticleNameString);
			//返物品个数
			String[] fanArticleNumString = parameters[3].split(",");
			fanArticleNums = new int[fanArticleNumString.length];
			for (int i = 0; i < fanArticleNumString.length; i++) {
				fanArticleNums[i] = Integer.parseInt(fanArticleNumString[i]);
			}
			//返物品绑定
			String[] fanArticleBindString = parameters[4].split(",");
			fanArticleBinds = new boolean[fanArticleBindString.length];
			for (int i = 0; i < fanArticleNumString.length; i++) {
				fanArticleBinds[i] = Boolean.parseBoolean(fanArticleBindString[i]);
			}
			
		}catch(Exception e) {
			logger.error("首次返物品 creatParameter", e);
		}
	}

	public String getParameter() {
		StringBuffer sb = new StringBuffer("");
		sb.append(Arrays.toString(fanliTimes));
		sb.append(" ").append(Arrays.toString(fanliSpaces));
		sb.append(" ").append(Arrays.toString(getFanArticleNames()));
		sb.append(" ").append(Arrays.toString(getFanArticleNums()));
		sb.append(" ").append(Arrays.toString(getFanArticleBinds()));
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
		if (playerMoneys.size() > 0 || sendFanLiTime != 0L) {
			logger.warn("[首次返物品数据清除] {} [{}] [{}]", new Object[]{getLogString(), playerMoneys.size(), sendFanLiTime});
			playerMoneys.clear();
			diskcache.put(DISK_KEY + getConfigID(), playerMoneys);
			sendFanLiTime = 0L;
			diskcache.put(DISK_KEY1 + getConfigID(), sendFanLiTime);
		}
	}

	public String getLogString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(getConfigID()).append("]");
		sb.append("[").append(getName()).append("]");
		sb.append("[").append(getStartTime()).append("]");
		sb.append("[").append(getEndTime()).append("]");
		sb.append("[").append(getParameter()).append("]");
		return sb.toString();
	}

	public void sendReward(Player player) {}

	public void setFanliSpaces(long[] fanliSpaces) {
		this.fanliSpaces = fanliSpaces;
	}

	public long[] getFanliSpaces() {
		return fanliSpaces;
	}

	public void setFanliTimes(String[] fanliTimes) {
		this.fanliTimes = fanliTimes;
	}

	public String[] getFanliTimes() {
		return fanliTimes;
	}

	public void setFanliDatas(long[] fanliDatas) {
		this.fanliDatas = fanliDatas;
	}

	public long[] getFanliDatas() {
		return fanliDatas;
	}

	public void setFanArticleNames(String[] fanArticleNames) {
		this.fanArticleNames = fanArticleNames;
	}

	public String[] getFanArticleNames() {
		return fanArticleNames;
	}

	public void setFanArticleNums(int[] fanArticleNums) {
		this.fanArticleNums = fanArticleNums;
	}

	public int[] getFanArticleNums() {
		return fanArticleNums;
	}

	public void setFanArticleBinds(boolean[] fanArticleBinds) {
		this.fanArticleBinds = fanArticleBinds;
	}

	public boolean[] getFanArticleBinds() {
		return fanArticleBinds;
	}

}
