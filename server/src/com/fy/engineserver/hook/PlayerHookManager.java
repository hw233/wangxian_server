package com.fy.engineserver.hook;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.PLAYER_HOOK_INFO_REQ;
import com.fy.engineserver.message.PLAYER_HOOK_INFO_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;

public class PlayerHookManager implements Runnable {

	public static boolean isRunAble = true;
	
	public static Logger logger = LoggerFactory.getLogger(PlayerHookManager.class);
	
	public Map<Long, PlayerHookInfo> hookInfos = new ConcurrentHashMap<Long, PlayerHookInfo>();
	
	public static long CLEAN_INFO_TIME = 1000L * 60 * 60 * 8;
	
	public static PlayerHookManager instance;
	
	public void init() {
		
		instance = this;
		new Thread(this).start();
		ServiceStartRecord.startLog(this);
	}
	
	public void handle_PLAYER_HOOK_INFO_REQ(Player player, PLAYER_HOOK_INFO_REQ req) {
		if(player.isShouStatus()){
//			player.sendError(Translate.兽形态不能挂机);
			return;
		}
		PlayerHookInfo info = req.getPlayerHookInfo();
		info.setPlayerID(player.getId());
		info.setLastTime(System.currentTimeMillis());
		
		PlayerHookInfo oldInfo = getHookInfo(player.getId());
		if (oldInfo == null) {
			oldInfo = info;
			if (info.isIsinHook()) {
				info.setStartHookTime(System.currentTimeMillis());
			}else {
				info.setStartHookTime(0L);
			}
			if (info.isIsAutoAtt()) {
				info.setStartAutoTime(System.currentTimeMillis());
				info.setEndAutoTime(0);
			}else {
				info.setStartAutoTime(0L);
				info.setEndAutoTime(System.currentTimeMillis());
			}
			hookInfos.put(player.getId(), info);
		}else {
			if (info.isIsinHook() != oldInfo.isIsinHook()) {
				if (info.isIsinHook()) {
					oldInfo.setStartHookTime(System.currentTimeMillis());
					oldInfo.setIsinHook(info.isIsinHook());
				}
			}
			if (info.isIsAutoAtt() != oldInfo.isIsAutoAtt()) {
				if (info.isIsAutoAtt()) {
					oldInfo.setStartAutoTime(System.currentTimeMillis());
				}else {
					oldInfo.setEndAutoTime(System.currentTimeMillis());
				}
				oldInfo.setIsAutoAtt(info.isIsAutoAtt());
			}
			oldInfo.setIsUseYinZi(info.isIsUseYinZi());
			oldInfo.setPlayerHpName(info.getPlayerHpName());
			oldInfo.setPlayerMpName(info.getPlayerMpName());
		}
		logger.warn("[挂机信息] [{}] {}", new Object[]{player.getLogString(), oldInfo.getLogString()});
	}
	
	public void sendHookRes(Player player, boolean isHook, boolean isAutoAtt, boolean isUseYinZi, String hpName, String mpName) {
		if (player == null) {
			return;
		}
		PlayerHookInfo oldInfo = getHookInfo(player.getId());
		if (oldInfo == null) {
			oldInfo = new PlayerHookInfo();
			oldInfo.setPlayerID(player.getId());
			oldInfo.setLastTime(System.currentTimeMillis());
			hookInfos.put(player.getId(), oldInfo);
			if (isHook) {
				oldInfo.setStartHookTime(System.currentTimeMillis());
			}
			oldInfo.setIsinHook(isHook);
			if (isAutoAtt) {
				oldInfo.setStartAutoTime(System.currentTimeMillis());
			}else {
				oldInfo.setEndAutoTime(System.currentTimeMillis());
			}
			oldInfo.setIsAutoAtt(isAutoAtt);
			oldInfo.setIsUseYinZi(isUseYinZi);
			oldInfo.setPlayerHpName(hpName);
			oldInfo.setPlayerMpName(mpName);
		}else {
			if (oldInfo.isIsinHook() != isHook) {
				if (isHook) {
					oldInfo.setStartHookTime(System.currentTimeMillis());
				}
				oldInfo.setIsinHook(isHook);
			}
			if (oldInfo.isIsAutoAtt() != isAutoAtt) {
				if (isAutoAtt) {
					oldInfo.setStartAutoTime(System.currentTimeMillis());
				}else {
					oldInfo.setEndAutoTime(System.currentTimeMillis());
				}
				oldInfo.setIsAutoAtt(isAutoAtt);
			}
			oldInfo.setIsUseYinZi(isUseYinZi);
			oldInfo.setPlayerHpName(hpName);
			oldInfo.setPlayerMpName(mpName);
		}
		
		PLAYER_HOOK_INFO_RES res = new PLAYER_HOOK_INFO_RES(GameMessageFactory.nextSequnceNum(), oldInfo);
		player.addMessageToRightBag(res);
		logger.warn("[发送设置挂机协议] [{}] [{}]", new Object[]{player.getLogString(), oldInfo.getLogString()});
	}
	
	public PlayerHookInfo getHookInfo(long id) {
		PlayerHookInfo oldInfo = hookInfos.get(id);
		if (oldInfo != null) {
			oldInfo.setLastTime(System.currentTimeMillis());
		}
		return null;
	}
	
	@Override
	public void run() {
		while(isRunAble) {
			try{
				Thread.sleep(10000);
			}catch(Exception e) {
				logger.error("心跳出错:", e);
			}
		}
	}

}
