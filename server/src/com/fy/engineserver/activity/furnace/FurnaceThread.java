package com.fy.engineserver.activity.furnace;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.activity.furnace.instance.FrunaceInstance;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

public class FurnaceThread extends Thread{
	
	private List<FrunaceInstance> furnaceList = new ArrayList<FrunaceInstance>();
	
	private List<FrunaceInstance> newAdd = new ArrayList<FrunaceInstance>();
	
	private List<FrunaceInstance> needRemove = new ArrayList<FrunaceInstance>();
	
	public static boolean isOpen = true;
	
	public void addNewInst(FrunaceInstance inst) {
		synchronized (newAdd) {
			newAdd.add(inst);
		}
	}
	
	public List<FrunaceInstance> getFurnaceList() {
		return furnaceList;
	}
	public void notifyLeaveGame(Player player) {
		FrunaceInstance[] bons = furnaceList.toArray(new FrunaceInstance[furnaceList.size()]);
		for (FrunaceInstance inst : bons) {
			inst.notifyLeaveGame(player);
		}
		bons = null;
	}
	
	public Game findPlayerGame(Player player) {
		FrunaceInstance[] bons = furnaceList.toArray(new FrunaceInstance[furnaceList.size()]);
		for (FrunaceInstance inst : bons) {
			if (inst.playerId == player.getId()) {
				return inst.game;
			}
		}
		bons = null;
		FrunaceInstance[] nd = newAdd.toArray(new FrunaceInstance[newAdd.size()]);
		for (FrunaceInstance inst : nd) {
			if (inst.playerId == player.getId()) {
				return inst.game;
			}
		}
		return null;
	}
	
	public boolean pickup(Player player, int stayIndex,long npcId) {
		FrunaceInstance[] bons = furnaceList.toArray(new FrunaceInstance[furnaceList.size()]);
		for (FrunaceInstance inst : bons) {
			if (inst.playerId == player.getId()) {
				inst.notifyPickup(player, stayIndex,npcId);
				return true;
			}
		}
		bons = null;
		return false;
	}
	
	public boolean isPicked(Player player, long npcId) {
		FrunaceInstance[] bons = furnaceList.toArray(new FrunaceInstance[furnaceList.size()]);
		for (FrunaceInstance inst : bons) {
			if (inst.playerId == player.getId()) {
				return inst.isPicked(player, npcId);
			}
		}
		bons = null;
		return false;
	}
	
	public void run() {
		while (isOpen) {
			try {
				FrunaceInstance[] bons = furnaceList.toArray(new FrunaceInstance[furnaceList.size()]);
				for (FrunaceInstance ins : bons) {
					ins.heartBeat();
					if (ins.state == FrunaceInstance.结束) {
						needRemove.add(ins);
					}
				}
				synchronized (newAdd) {
					if (newAdd.size() > 0) {
						FrunaceInstance[] add = newAdd.toArray(new FrunaceInstance[newAdd.size()]);
						newAdd.clear();
						for (FrunaceInstance fi : add) {
							furnaceList.add(fi);
						}
						add = null;
					}
				}
				if (needRemove.size() > 0) {
					for (FrunaceInstance fi : needRemove) {
						furnaceList.remove(fi);
					}
					needRemove.clear();
				}
				bons = null;
				
			} catch(Exception e) {
				FurnaceManager.logger.warn("[线程异常]", e);
			}
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				
			}
		}
	}
}
