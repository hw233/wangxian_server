package com.fy.engineserver.activity.newChongZhiActivity;

import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.sprite.Player;

public class FanLiTimelyActivity extends NewMoneyActivity {

	private long needMoney;				//需要的金额
	private int backBiLi;				//返回比例			万分之
	
	public FanLiTimelyActivity(int configID, String name, int platform,
			String startTime, String endTime, String[] serverNames,
			String[] unServerNames, String mailTitle, String mailMsg,
			String[] parameters) throws Exception {
		super(configID, name, platform, startTime, endTime, serverNames, unServerNames,
				mailTitle, mailMsg, parameters);
	}
	
	@Override
	public void loadDiskCache() {}

	@Override
	public void doActivity(Player player, long yinzi) {
		try{
			long now = System.currentTimeMillis();
			if (now < getStartTimeLong() || now > getEndTimeLong()) {
				//活动还未开始
				return;
			}
			if (yinzi < getNeedMoney()) {
				//充值金额未达到最低上限
				return;
			}
			long backMoney = yinzi * backBiLi / 10000;
			if (backMoney <= 0) {
				backMoney = 1;
			}
			MailManager.getInstance().sendMail(player.getId(), null, getMailTitle(), getMailMsg(), backMoney, 0, 0, "充值活动");
			logger.warn("[充值当时返利] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), getLogString(), backMoney, yinzi});
		}catch(Exception e) {
			logger.error("当时返doActivity", e);
		}
	}

	@Override
	public void heatbeat() {}

	@Override
	public void creatParameter(String[] parameters) {
		try{
			setNeedMoney(Long.parseLong(parameters[0]));
			setBackBiLi(Integer.parseInt(parameters[1]));
		}catch(Exception e) {
			logger.error("当时返creatParameter", e);
		}
	}

	@Override
	public String getParameter() {
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(getNeedMoney()).append("]");
		sb.append(" [").append(getBackBiLi()).append("]");
		return sb.toString();
	}

	@Override
	public void cleanActivityData() {}

	@Override
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

	@Override
	public void sendReward(Player player) {}

	@Override
	public String[] getActivityPartake() {
		String[] s = new String[0];
		return s;
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
