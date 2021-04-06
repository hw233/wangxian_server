package com.fy.engineserver.menu.fairy;

import com.fy.engineserver.activity.fairyBuddha.FairyBuddhaManager;
import com.fy.engineserver.activity.fairyBuddha.challenge.FairyChallengeManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;

public class Option_StartChallenge extends Option implements NeedCheckPurview{
	/** 每周几不开放(目前写死--可以使用配表设置)   (1代表周天)*/
	private int[] notOpenDays = new int[]{};			
	/** 挑战地图名 */
	private String mapName;
	/** 职业 */
	private byte career;
	
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		if(!FairyBuddhaManager.open){
			player.sendError(Translate.活动未开启);
			return;
		}
		if(UnitServerFunctionManager.needCloseFunctuin(Function.仙尊)) {
			player.sendError(Translate.合服功能关闭提示);
			return ;
		}
		FairyChallengeManager.getInst().startChallenge(player, mapName, notOpenDays);
	}
	
	@Override
	public boolean canSee(Player player) {
		// TODO Auto-generated method stub
		return checkTime(player);
	}
	/**
	 * @return true可以看到或者进入  
	 */
	public boolean checkTime(Player player) {
//		String result = FairyChallengeManager.getInst().verificationData(player, mapName, notOpenDays);
//		if(FairyChallengeManager.logger.isDebugEnabled()) {
//			FairyChallengeManager.logger.debug("[仙尊挑战] [判断窗口是否可见，结果:" + result + "] [" + player.getLogString() + "]");
//		}
//		return result == null;
		if (player.getCurrSoul().getCareer() != career) {
			return false;
		}
		return true;
	}

	public int[] getNotOpenDays() {
		return notOpenDays;
	}

	public void setNotOpenDays(int[] notOpenDays) {
		this.notOpenDays = notOpenDays;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public byte getCareer() {
		return career;
	}

	public void setCareer(byte career) {
		this.career = career;
	}

}
