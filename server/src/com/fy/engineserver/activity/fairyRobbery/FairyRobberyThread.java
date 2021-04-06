package com.fy.engineserver.activity.fairyRobbery;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.fy.engineserver.activity.fairyRobbery.instance.FairyRobbery;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

public class FairyRobberyThread extends Thread{
	
	public static boolean isopen = true;
	
	public List<FairyRobbery> list = new ArrayList<FairyRobbery>();
	
	Queue<FairyRobbery> newAdd = new ConcurrentLinkedQueue<FairyRobbery>();
	
	public void addNewRobbery(FairyRobbery f) {
		newAdd.add(f);
	}
	
	public Game getPlayerGame(Player player) {
		FairyRobbery[] frb = list.toArray(new FairyRobbery[0]);
		for (FairyRobbery f : frb) {
			if (f.playerId == player.getId()) {
				return f.game;
			}
		}
		frb = null;
		return null;
	}
	public boolean notifyLeaveGame(Player player) {
		FairyRobbery[] frb = list.toArray(new FairyRobbery[0]);
		for (FairyRobbery f : frb) {
			if (f.playerId == player.getId()) {
				return f.notifyLeaveGame(player);
			}
		}
		frb = null;
		return false;
	}
	
	public boolean notifyStart(Player player) {
		FairyRobbery[] frb = list.toArray(new FairyRobbery[0]);
		for (FairyRobbery f : frb) {
			if (f.playerId == player.getId()) {
				return f.notifyStart(player);
			}
		}
		frb = null;
		return false;
	}
	
	public boolean notifyKillBoss(Player player,long bossId) {
		FairyRobbery[] frb = list.toArray(new FairyRobbery[0]);
		for (FairyRobbery f : frb) {
			if (f.playerId == player.getId()) {
				return f.notifyKillBoss(player.getId(), bossId);
			}
		}
		frb = null;
		return false;
	}
	
	public boolean notifyPlayerDead(Player player) {
		FairyRobbery[] frb = list.toArray(new FairyRobbery[0]);
		for (FairyRobbery f : frb) {
			if (f.playerId == player.getId()) {
				return f.notifyPlayerDead(player);
			}
		}
		frb = null;
		return false;
	}
	
	@Override
	public void run() {
		long now = System.currentTimeMillis();
		FairyRobberyManager.logger.warn("*****************************************************");
		FairyRobberyManager.logger.warn("*************["+this.getName()+"] [管理线程] [启动]**********");
		FairyRobberyManager.logger.warn("*****************************************************");
		while (isopen) {
			List<FairyRobbery> needRemove = new ArrayList<FairyRobbery>();
			FairyRobbery[] frb = list.toArray(new FairyRobbery[0]);
			if (newAdd.size() > 0) {
				FairyRobbery f = newAdd.poll();
				if (f != null) {
					list.add(f);
				}
			}
			now = System.currentTimeMillis();
			for (FairyRobbery f : frb) {				//心跳
				try {
					f.heartBeat(now);
					if (f.getState() == FairyRobbery.结束) {
						needRemove.add(f);
					}
				} catch (Exception e) {
					FairyRobberyManager.logger.warn("[仙界渡劫] [心跳异常] [playerId:" + f.playerId + "]", e);
				}
			}	
			if (needRemove.size() > 0) {				//移除需要移除的副本
				for (FairyRobbery f : needRemove) {
					list.remove(f);
				}
			}
			needRemove = null;
			frb = null;
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				
			}
		}
		FairyRobberyManager.logger.warn("*****************************************************");
		FairyRobberyManager.logger.warn("*************["+this.getName()+"] [管理线程] [关闭]**********");
		FairyRobberyManager.logger.warn("*****************************************************");
	}
}
