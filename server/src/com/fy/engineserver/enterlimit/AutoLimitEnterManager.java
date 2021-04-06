package com.fy.engineserver.enterlimit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fy.engineserver.enterlimit.EnterLimitManager.PlayerRecordData;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.util.ServiceStartRecord;

public class AutoLimitEnterManager extends AutoLimitEnter {

	/**
	 * 自动封号规则
	 */
	public List<AutoLimitEnter> autoLimitEnterList = new ArrayList<AutoLimitEnter>();

	private static AutoLimitEnterManager instance;

	public static AutoLimitEnterManager getInstance() {
		return instance;
	}

	public void add(AutoLimitEnter autoLimitEnter) {
		autoLimitEnterList.add(autoLimitEnter);
		EnterLimitManager.logger.warn("[自动封号] [新增自动封号规则] [" + autoLimitEnter.getName() + "] [" + autoLimitEnter.getDes() + "]");
	}

	public void remove(String name) {
		Iterator<AutoLimitEnter> iterator = autoLimitEnterList.iterator();
		while (iterator.hasNext()) {
			AutoLimitEnter autoLimitEnter = iterator.next();
			if (autoLimitEnter.getName().equals(name)) {
				iterator.remove();
				EnterLimitManager.logger.warn("[自动封号] [按名字移除自动封号规则] [" + autoLimitEnter.getName() + "] [" + autoLimitEnter.getDes() + "]");
			}
		}
	}

	@Override
	public String getDes() {
		return "AutoLimitEnterManager";
	}

	@Override
	public String getName() {
		return "AutoLimitEnterManager";
	}

	@Override
	public boolean needLimitEnter(PlayerRecordData playerRecordData) {
		Iterator<AutoLimitEnter> iterator = autoLimitEnterList.iterator();
		while (iterator.hasNext()) {
			AutoLimitEnter autoLimitEnter = iterator.next();
			if (autoLimitEnter.needLimitEnter(playerRecordData)) {
				EnterLimitManager.logger.warn("[自动封号] [被自动封号:" + playerRecordData.playername + "/" + playerRecordData.playerId + "] [" + autoLimitEnter.getName() + "] [" + autoLimitEnter.getDes() + "]");
				return true;
			}
		}
		return false;
	}

	public void init() {
		
		long startTime = SystemTime.currentTimeMillis();
		if (autoLimitEnterList == null) autoLimitEnterList = new ArrayList<AutoLimitEnter>();
		add(new AutoLimitEnter() {

			@Override
			public boolean needLimitEnter(PlayerRecordData playerRecordData) {
				if (((Integer) playerRecordData.obj5) >= EnterLimitManager.autoLimitEnter_runTimes) {
					if ("KUNLUNAPPSTORE_XUNXIAN".equals(playerRecordData.channel)) {
						return false;
					} else {
						if (playerRecordData.obj1 == null || "null".equalsIgnoreCase(playerRecordData.obj1.toString()) || "false".equalsIgnoreCase(playerRecordData.obj1.toString())) {
							return true;
						}
					}
				}
				return false;
			}

			@Override
			public String getName() {
				return "签名不一致";
			}

			@Override
			public String getDes() {
				return "5次心跳未有GET_RSA_DATA_REQ数据的";
			}
		});
		add(new AutoLimitEnter() {

			@Override
			public boolean needLimitEnter(PlayerRecordData playerRecordData) {
				if (((Integer) playerRecordData.obj5) >= EnterLimitManager.autoLimitEnter_runTimes) {
					if ("KUNLUNAPPSTORE_XUNXIAN".equals(playerRecordData.channel)) {
						return false;
					} else {
						if (playerRecordData.obj2 == null || "null".equalsIgnoreCase(playerRecordData.obj2.toString()) || "true".equalsIgnoreCase(playerRecordData.obj2.toString())) {
							return true;
						}
					}
				}
				return false;
			}

			@Override
			public String getName() {
				return "使用模拟器";
			}

			@Override
			public String getDes() {
				return "封掉使用模拟器的用户";
			}
		});

		add(new AutoLimitEnter() {

			@Override
			public boolean needLimitEnter(PlayerRecordData playerRecordData) {
				if (!PlatformManager.getInstance().isPlatformOf(Platform.腾讯)) {// 非腾讯的
					if (playerRecordData.sealEmptyCellTimes >= EnterLimitManager.autoLimitEnter_sellEmpty) {
						return true;
					}
				}
				return false;
			}

			@Override
			public String getName() {
				return "空格子";
			}

			@Override
			public String getDes() {
				return "除腾讯外,卖空格子超过8个";
			}
		});

		instance = this;
		ServiceStartRecord.startLog(this);
	}
}
