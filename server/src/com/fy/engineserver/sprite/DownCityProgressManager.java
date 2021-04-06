package com.fy.engineserver.sprite;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.fy.engineserver.downcity.DownCity;
import com.fy.engineserver.downcity.DownCityInfo;
import com.fy.engineserver.downcity.DownCityManager;

/**
 * 副本进度管理器
 * 玩家数据从内存中清除是，需要通知此管理器，移除重置副本列表
 * 
 *
 */
public class DownCityProgressManager {
	/**
	 * 玩家身上保持的副本进度
	 * key : 玩家Id
	 * value : 玩家身上的副本进度map，其中:key为副本名称，value为副本id
	 */
	protected HashMap<Long,HashMap<String,String>> downCityProgressMap = new HashMap<Long,HashMap<String,String>>();
	
	/**
	 * 重置副本时间列表
	 */
	protected HashMap<Long,ArrayList<Date>> resetDownCityListMap = new HashMap<Long,ArrayList<Date>>();
	
	public HashMap<String,String> getDownCityProgressData(Player player) {
		return downCityProgressMap.get(player.getId());
	}
	
	/**
	 * 得到玩家的副本进度
	 * @return
	 */
	public HashMap<String,DownCity> getDownCityProgress(Player player){
		HashMap<String,String> data = getDownCityProgressData(player);
		if(data != null) {
			DownCityManager dcm = DownCityManager.getInstance();
			HashMap<String,DownCity> map = new HashMap<String,DownCity>();
			String keys[] = data.keySet().toArray(new String[0]);
			for(int i = 0 ; i < keys.length ; i++){
				String id = data.get(keys[i]);
				DownCity dc = dcm.getDownCityById(id);
				if(dc != null){
					map.put(keys[i], dc);
				}
			}
			return map;
		}
		return null;
	}
	
	/**
	 * 得到某个副本进度
	 * @param name
	 * @return
	 */
	public DownCity getDownCityProgress(Player player, String name){
		HashMap<String,String> data = getDownCityProgressData(player);
		if(data != null) {
			DownCityManager dcm = DownCityManager.getInstance();
			String id = data.get(name);
			if(id == null) return null;
			return dcm.getDownCityById(id);
		}
		return null;
	}
	
	/**
	 * 新增一个副本进度
	 * @param dc
	 */
	public void addDownCityProgress(Player player, DownCity dc){
		HashMap<String,String> data = getDownCityProgressData(player);
		if(data == null) {
			data = new HashMap<String,String>();
			downCityProgressMap.put(player.getId(), data);
		}
		data.put(dc.getDi().getName(), dc.getId());
	}
	
	/**
	 * 得到玩家的重置副本时间列表
	 * @param player
	 * @return
	 */
	public ArrayList<Date> getPlayerResetDownCityList(Player player) {
		return resetDownCityListMap.get(player.getId());
	}

	/**
	 * 玩家是否重置副本次数过多
	 * @return
	 */
	public boolean isOverflowResetDownCity(Player player){
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArrayList<Date> resetDownCityList = this.getPlayerResetDownCityList(player);
		if(resetDownCityList == null) {
			return false;
		}
		Iterator<Date> it = resetDownCityList.iterator();
		if(it.hasNext()){
			Date d = it.next();
			if(d.getTime() + 3600*1000 < now){
				it.remove();
			}
		}	
		if(resetDownCityList.size() > 5){
			return true;
		}
		return false;
	}
	
	/**
	 * 重置特定的副本，此重置计算重置次数
	 * @param dc
	 */
	public boolean resetDownCity(Player player, DownCity dc){
		boolean reset = false;
		DownCityManager dcm = DownCityManager.getInstance();
		dc = this.getDownCityProgress(player, dc.getDi().getName());
		if(dc != null && (dc.getDi().getResetType() == DownCityInfo.RESET_TYPE_BY_PLAYER || dc.getDownCityState() == 0)){
			
			if(dcm.isValid(dc)){
				ArrayList<Date> resetDownCityList = this.getPlayerResetDownCityList(player);
				if(resetDownCityList == null) {
					resetDownCityList = new ArrayList<Date>();
					this.resetDownCityListMap.put(player.getId(), resetDownCityList);
				}
				resetDownCityList.add(new Date());
			}
			HashMap<String,String> progressMap = this.getDownCityProgressData(player);
			if(progressMap != null) { 
				progressMap.remove(dc.getDi().getName());
				dcm.notidyPlayerResetDownCity(dc,player);
				reset = true;
			}
			dc.bossBourn = false;
		}
		return reset;
	}
	
	/**
	 * 重置所有副本进度
	 */
	public void resetDownCity(Player player){
		DownCityManager dcm = DownCityManager.getInstance();
		DownCity [] dcs = this.getDownCityProgress(player).values().toArray(new DownCity[0]);
		int c = 0;
		for(DownCity dc : dcs){
			if(dcm.isValid(dc) && (dc.getDi().getResetType() == DownCityInfo.RESET_TYPE_BY_PLAYER || dc.getDownCityState() == 0)){
				c ++;
			}
		}
		if(c > 0){
			ArrayList<Date> resetDownCityList = this.getPlayerResetDownCityList(player);
			if(resetDownCityList == null) {
				resetDownCityList = new ArrayList<Date>();
				this.resetDownCityListMap.put(player.getId(), resetDownCityList);
			}
			resetDownCityList.add(new Date());
		}
		for(DownCity dc : dcs){
			if(dcm.isValid(dc) && (dc.getDi().getResetType() == DownCityInfo.RESET_TYPE_BY_PLAYER || dc.getDownCityState() == 0)){
				HashMap<String,String> progressMap = this.getDownCityProgressData(player);
				if(progressMap != null) { 
					progressMap.remove(dc.getDi().getName());
					dcm.notidyPlayerResetDownCity(dc,player);
				}
			}
		}
	}
}
