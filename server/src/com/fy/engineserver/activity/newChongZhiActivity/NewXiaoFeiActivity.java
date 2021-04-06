package com.fy.engineserver.activity.newChongZhiActivity;

public abstract class NewXiaoFeiActivity extends NewMoneyActivity {
	
	public static int XIAOFEI_TYPE_SHOP = 0;				//商城
	public static int XIAOFEI_TYPE_TAX = 1;					//税

	private int[] xiaofeiType;				//消费类型
	
	public NewXiaoFeiActivity(int configID, int[] xiaofeiType, String name, int platform,
			String startTime, String endTime, String[] serverNames,
			String[] unServerNames, String mailTitle, String mailMsg,
			String[] parameters) throws Exception {
		super(configID, name, platform, startTime, endTime, serverNames, unServerNames,
				mailTitle, mailMsg, parameters);
		setXiaofeiType(xiaofeiType);
	}

	public boolean isXiaoFeiType (int type) {
		if (xiaofeiType == null) {
			return false;
		}
		for (int i = 0; i <xiaofeiType.length; i++) {
			if (type == xiaofeiType[i]) {
				return true;
			}
		}
		return false;
	}
	
	public void setXiaofeiType(int[] xiaofeiType) {
		this.xiaofeiType = xiaofeiType;
	}

	public int[] getXiaofeiType() {
		return xiaofeiType;
	}
}
