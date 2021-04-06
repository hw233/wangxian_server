package com.fy.engineserver.activity.chongzhiActivity;

import java.text.SimpleDateFormat;
import java.util.Arrays;

public class ChongZhiActivityFanLiToDay extends ChongZhiServerConfig {

	private long[] fanliSpaces;					//返利的时间间隔			//毫秒
	private String[] fanliTimes;				//返利时间字符串
	private long[] fanliDatas;					//返利时间
	private int[] fanliBiLi;						//返利比例
	
	public ChongZhiActivityFanLiToDay (int id, String serverName, String startTime, String endTime, String mailTitle,
			String mailMag, String propName, long[] fanliSpaces, String[] fanliTimes, int[] fanliBiLi) throws Exception {
		setActivityID(id);
		setType(CHONGZHI_FANLI_TODAY_TYPE);
		setServerName(serverName);
		setStartTime(startTime);
		setEndTime(endTime);
		setMailTitle(mailTitle);
		setMailMsg(mailMag);
		setMoney(0);
		setPropName(propName);
		setPropNum(1);
		setColorType(-1);
		setBind(true);
		setFanliSpaces(fanliSpaces);
		setFanliTimes(fanliTimes);
		setFanliBiLi(fanliBiLi);
		creatLongTime();
	}
	
	public int findSendTimeIndex (long now) {
		if (now < fanliDatas[0]) {
			return -1;
		}
		for (int i = 0 ; i < fanliDatas.length; i++) {
			if (now < fanliDatas[i]) {
				return i - 1;
			}
		}
		return -2;
	}
	
	public void creatLongTime() throws Exception {
		super.creatLongTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		fanliDatas = new long[fanliTimes.length];
		for (int i = 0;i < fanliTimes.length; i++) {
			fanliDatas[i] = format.parse(fanliTimes[i]).getTime();
		}
	}
	
	public String toLogString() {
		StringBuffer sb = new StringBuffer("");
		sb.append(super.toLogString());
		sb.append(" [").append(Arrays.toString(fanliSpaces)).append("] ");
		sb.append("[").append(Arrays.toString(fanliTimes)).append("] ");
		sb.append("[").append(Arrays.toString(fanliBiLi)).append("]");
		return sb.toString();
	}
	
	
	public void setFanliBiLi(int[] fanliBiLi) {
		this.fanliBiLi = fanliBiLi;
	}
	public int[] getFanliBiLi() {
		return fanliBiLi;
	}

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
	
}
