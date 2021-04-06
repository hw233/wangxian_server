package com.fy.engineserver.activity.chongzhiActivity;

import java.text.SimpleDateFormat;

public class ChongZhiFanLiAcivity extends ChongZhiServerConfig {

	private long lingqu_Time;
	private String lingquTime;
	
	public ChongZhiFanLiAcivity() {
		setType(ChongZhiServerConfig.TOTAL_CHONGZHI_BACK_TYPE);
	}
	
	@Override
	public void creatLongTime() throws Exception {
		super.creatLongTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		setLingqu_Time(format.parse(lingquTime).getTime());
	}
	
	public boolean canLingQu() {
		long now = System.currentTimeMillis();
		if (getEnd_time() <= now && now <= getLingqu_Time()) {
			return true;
		}
		return false;
	}

	public void setLingqu_Time(long lingqu_Time) {
		this.lingqu_Time = lingqu_Time;
	}

	public long getLingqu_Time() {
		return lingqu_Time;
	}

	public void setLingquTime(String lingquTime) {
		this.lingquTime = lingquTime;
	}

	public String getLingquTime() {
		return lingquTime;
	}
}
