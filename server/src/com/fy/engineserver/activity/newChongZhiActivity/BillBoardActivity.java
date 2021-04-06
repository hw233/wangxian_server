package com.fy.engineserver.activity.newChongZhiActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.sprite.Player;

/**
 * 充值排行活动
 *
 */
public class BillBoardActivity extends NewMoneyActivity {
	
	public static String DISK_CATCH = "paihang";
	public HashMap<Long, Long> playerMoneys = new HashMap<Long, Long>();
	public static String DISK_CATCH_1 = "paihangAAA";
	public boolean isSendReward = false;
	
	public static long clean_time = 1000L * 60 * 60 * 24 * 3;
	
	private long needMoney;							//进入排行的金额
	private String[] rewardPropNames;				//奖励物品名字
	private int[] rewardPropNums;					//奖励物品数目
	private int[] rewardColors;						//奖励物品颜色
	private boolean[] rewardBinds;					//奖励绑定情况
	
	public BillBoardActivity(int configID, String name, int platform,
			String startTime, String endTime, String[] serverNames,
			String[] unServerNames, String mailTitle, String mailMsg,
			String[] parameters) throws Exception {
		super(configID, name, platform, startTime, endTime, serverNames, unServerNames,
				mailTitle, mailMsg, parameters);
	}

	@Override
	public void loadDiskCache() {
		try {
			Object obj = diskcache.get(DISK_CATCH + getConfigID());
			if (obj == null) {
				diskcache.put(DISK_CATCH + getConfigID(), playerMoneys);
			}else {
				playerMoneys = (HashMap<Long, Long>)obj;
			}
			Object obj1 = diskcache.get(DISK_CATCH_1 + getConfigID());
			if (obj1 == null) {
				diskcache.put(DISK_CATCH_1 + getConfigID(), isSendReward);
			}else {
				isSendReward = Boolean.getBoolean(obj1.toString());
			}
		}catch (Exception e) {
			logger.error("排行loadDiskCache", e);
		}
	}

	@Override
	public void doActivity(Player player, long yinzi) {
		try {
			long now = System.currentTimeMillis();
			if (now < getStartTimeLong() || now > getEndTimeLong()) {
				//活动还未开始
				return;
			}
			Long oldMoney = playerMoneys.get(player.getId());
			if (oldMoney == null) {
				oldMoney = new Long(0);
			}
			oldMoney += yinzi;
			playerMoneys.put(player.getId(), oldMoney);
			diskcache.put(DISK_CATCH + getConfigID(), playerMoneys);
		}catch (Exception e) {
			logger.error("排行doActivity", e);
		}
	}

	@Override
	public void heatbeat() {
		try {
			if (!isSendReward) {
				long now = System.currentTimeMillis();
				if (now > getEndTimeLong()) {
					//发奖励
					List<MoneyBillBoardActivityData> datas = getSortList(rewardPropNames.length);
					for (int i = 0 ; i < datas.size(); i++) {
						MoneyBillBoardActivityData d = datas.get(i);
						if (d.getMoney() >= getNeedMoney()) {
							try {
								Article a = ArticleManager.getInstance().getArticle(rewardPropNames[i]);
								if (a != null) {
									ArticleEntity en = ArticleEntityManager.getInstance().createEntity(a, rewardBinds[i], ArticleEntityManager.活动, null, rewardColors[i], rewardPropNums[i], true);
									MailManager.getInstance().sendMail(d.getPlayerID(), new ArticleEntity[]{en}, new int[]{rewardPropNums[i]}, getMailTitle(), getMailMsg(), 0, 0, 0, "充值活动");
									logger.warn("[奖励成功] [{}] [{}] [{}] [{}]", new Object[]{i, getLogString(), rewardPropNames[i], d.getPlayerID() + "-" + d.getMoney()});
								}else {
									logger.error("[奖励不存在] [{}] [{}] [{}] [{}]", new Object[]{getLogString(), rewardPropNames[i], d.getPlayerID(), d.getMoney()});
								}
							} catch (Exception e) {
								logger.error("[奖励发失败] ["+getLogString()+"] ["+d.getPlayerID()+"] ["+d.getMoney()+"]", e);
							}
						}
					}
					isSendReward = true;
					diskcache.put(DISK_CATCH_1 + getConfigID(), isSendReward);
				}
			} else {
				long now = System.currentTimeMillis();
				if (now - getEndTimeLong() > clean_time) {
					cleanActivityData();
				}
			}
		}catch (Exception e) {
			logger.error("排行heatbeat", e);
		}
	}

	@Override
	public void creatParameter(String[] parameters) {
		try {//金额
			setNeedMoney(Long.parseLong(parameters[0]));
			//奖励物品名字
			setRewardPropNames(parameters[1].split(","));
			//奖励物品数目
			String[] numString = parameters[2].split(",");
			rewardPropNums = new int[numString.length];
			for (int i = 0 ; i < numString.length; i++) {
				rewardPropNums[i] = Integer.parseInt(numString[i]);
			}
			//奖励物品颜色
			String[] colorString = parameters[3].split(",");
			rewardColors = new int[colorString.length];
			for (int i = 0 ; i < colorString.length; i++) {
				rewardColors[i] = Integer.parseInt(colorString[i]);
			}
			//奖励物品绑定
			String[] bingString = parameters[4].split(",");
			rewardBinds = new boolean[bingString.length];
			for (int i = 0 ; i < bingString.length; i++) {
				rewardBinds[i] = Boolean.parseBoolean(bingString[i]);
			}
		}catch (Exception e) {
			logger.error("e", e);
		}
	}

	@Override
	public String getParameter() {
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(getNeedMoney()).append("]");
		for (int i = 0; i < rewardPropNames.length; i++) {
			sb.append("[").append(rewardPropNames[i]).append("-").append(rewardPropNums[i]).append("-").append(rewardBinds[i]).append("]");
		}
		return sb.toString();
	}

	@Override
	public void cleanActivityData() {
		if (playerMoneys.size() > 0) {
			logger.warn("[活动数据清除] {} [{}]", new Object[]{getLogString(), playerMoneys.size()});
			playerMoneys.clear();
			diskcache.put(DISK_CATCH + getConfigID(), playerMoneys);
			isSendReward = false;
			diskcache.put(DISK_CATCH_1 + getConfigID(), isSendReward);
		}
	}

	@Override
	public String getLogString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(getConfigID()).append("] ");
		sb.append("[").append(getName()).append("] ");
		sb.append("[").append(getStartTime()).append("] ");
		sb.append("[").append(getEndTime()).append("] ");
		sb.append("[").append(getNeedMoney()).append("]");
		return sb.toString();
	}

	@Override
	public void sendReward(Player player) {
	}

	@Override
	public String[] getActivityPartake() {
		String[] s = new String[playerMoneys.size()];
		int i = 0;
		for (Long pID : playerMoneys.keySet()) {
			s[i] = pID.longValue() + "---" + playerMoneys.get(pID);
			i++;
		}
		return s;
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
	
	public List<MoneyBillBoardActivityData> getSortList(int num) {
		ArrayList<MoneyBillBoardActivityData> re = new ArrayList<MoneyBillBoardActivityData>(playerMoneys.size());
		for (Long key : playerMoneys.keySet()) {
			Long money = playerMoneys.get(key);
			MoneyBillBoardActivityData data = new MoneyBillBoardActivityData();
			data.setMoney(money);
			data.setPlayerID(key);
			re.add(data);
		}
		Collections.sort(re);
		if (re.size() < num) {
			num = re.size();
		}
		List<MoneyBillBoardActivityData> aaa = re.subList(0, num);
		
		logger.warn("[查询排行情况] [{}] [{}]", new Object[]{num, aaa.toString()});
		
		return aaa;
	}
	
}
