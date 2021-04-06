package com.fy.engineserver.downcity.downcity3;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;

public class BossRoom {
	
	//房间人数，副本人数
	private List<Long> ids = new ArrayList<Long>();
	
	private CityGame city;
	
	//是否在运行
	private boolean isrun;
	
	public void clearRoomData(){
		try {
			for(long id : ids){
				if(id > 0){
					Player p = PlayerManager.getInstance().getPlayer(id);
					if(p != null && p.room != null){
						p.room = null;
						BossCityManager.logger.warn("[新活动开启] [清理缓存数据2] [成功] [id:"+p.getId()+"] [角色:"+p.getName()+"] [账号:"+p.getUsername()+"]");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			BossCityManager.logger.warn("[新活动开启] [清理缓存数据2] [异常]",e);
		}
	}
	
	public boolean isIsrun() {
		return isrun;
	}
	public void setIsrun(boolean isrun) {
		this.isrun = isrun;
	}
	public List<Long> getIds() {
		return ids;
	}
	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
	public boolean isFull() {
		return ids.size() >= BossCityManager.roomEnterLimit;
	}
	public void entenRoom(long id){
		ids.add(id);
	}
	public void leaveRoom(long id){
		ids.remove(id);
	}
	public CityGame getCity() {
		return city;
	}
	public void setCity(CityGame city) {
		this.city = city;
	}
	
}
