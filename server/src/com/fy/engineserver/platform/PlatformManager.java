package com.fy.engineserver.platform;

import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.newtask.NewDeliverTaskManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.boss.game.GameConstants;

/*
 * 平台管理
 */
public class PlatformManager {

	private GameConstants gameConstants;
	private Platform platform;

	private static PlatformManager instance;

	public static PlatformManager getInstance() {
		return instance;
	}

	public GameConstants getGameConstants() {
		return gameConstants;
	}

	public void setGameConstants(GameConstants gameConstants) {
		this.gameConstants = gameConstants;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public Platform getPlatForm(String platformName) {
		for (Platform platForm : Platform.values()) {
			if (platForm.getPlatformName().equals(platformName)) {
				return platForm;
			}
		}
		throw new IllegalStateException("不能存在的平台:" + platformName);
	}

	public boolean currentPlatformOpen(PlatformConcern concern) {
		for (Platform temp : concern.getOptionPlatforms()) {
			if (platform.equals(temp)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否是某几个平台
	 * @param platformInput
	 * @return
	 */
	public boolean isPlatformOf(Platform ...platformInput) {
		for (Platform pf : platformInput) {
			if (platform.equals(pf)) {
				return true;
			}
		}
		return false;
	}

	public enum Platform {
		官方("sqage"),
		腾讯("tencent"),
		台湾("taiwan"),
		马来("malai"), 
		韩国("korea"), ;

		Platform(String platformName) {
			this.platformName = platformName;
		}

		private String platformName;

		public String getPlatformName() {
			return platformName;
		}

		public void setPlatformName(String platformName) {
			this.platformName = platformName;
		}
	}
	
	public Platform getPlatformByCNName (String platFormCNName) {
		for (Platform platform : Platform.values()) {
			if (platform.toString().equals(platFormCNName)) {
				return platform;
			}
		}
		return null;
	}
	
	public Platform getPlatformByENName(String platFormENName) {
		for (Platform platform : Platform.values()) {
			if (platform.getPlatformName().equals(platFormENName)) {
				return platform;
			}
		}
		return null;
	}

	// 一些平台相关的事件
	public enum PlatformConcern {
		打怪(Platform.values()), ;

		PlatformConcern(Platform... platforms) {
			this.optionPlatforms = platforms;
		}

		Platform[] optionPlatforms;// 那些平台开放

		public Platform[] getOptionPlatforms() {
			return optionPlatforms;
		}

		public void setOptionPlatforms(Platform[] optionPlatforms) {
			this.optionPlatforms = optionPlatforms;
		}

	}

	/**
	 * 判断当前服务器是不是某个平台
	 * @param platform
	 * @return
	 */
	public boolean platformOf(Platform platform) {
		return this.platform.equals(platform);
	}

	public void init() throws Exception {
		
		this.platform = getPlatForm(gameConstants.getPlatForm());
		instance = this;
		if (isPlatformOf(Platform.官方)) {
			Player.isUseOnLineTimePiLao = true;
			EnterLimitManager.isCanChongZhi = true;
		} else {
			NewDeliverTaskManager.isNewDeliverTaskAct = false;
		}
		ServiceStartRecord.startLog(this);
	}
}
