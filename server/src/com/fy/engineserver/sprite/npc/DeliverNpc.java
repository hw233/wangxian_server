package com.fy.engineserver.sprite.npc;

import java.util.ArrayList;

import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.sprite.Sprite;

/**
 * 商店NPC
 * 
 *
 */
public class DeliverNpc extends NPC{

	private static final long serialVersionUID = 1L;

	public static class Destination{
		public String mapName;
		public String mapAreaName;
	}
	/**
	 * 此车夫能传送的地点
	 */
	protected ArrayList<Destination> destinationList = new ArrayList<Destination>();
	
	/**
	 * 跨越一个地图需要多少钱
	 */
	protected int priceForOneStep = 0;
	
	public int getPriceForOneStep() {
		return priceForOneStep;
	}

	public void setPriceForOneStep(int priceForOneStep) {
		this.priceForOneStep = priceForOneStep;
	}

	public byte getNpcType(){
		return Sprite.NPC_TYPE_DELIVER;
	}
	
	public Destination[] getDestinations(){
		return destinationList.toArray(new Destination[0]);
	}
	
	public Destination getDestinationByIndex(int index){
		return destinationList.get(index);
	}
	
	/**
	 * 设置此npc可以达到的地点
	 * 每个目标组织方式为： 地图名称#地图区域名称
	 * @param d
	 */
	public void setDestinations(String destinations[]){
		for(int i = 0 ; i < destinations.length ; i++){
			int k = destinations[i].indexOf("#");
			if(k > 0){
				Destination d = new Destination();
			
				d.mapName = destinations[i].substring(0,k).trim();
				d.mapAreaName = destinations[i].substring(k+1).trim();
				GameManager gm = GameManager.getInstance();
				GameInfo gi = gm.getGameInfo(d.mapName);
				if(gi == null){
//					System.out.println("【警告】车夫["+name+"]传递的某个目的地["+destinations[i]+"]对应的地图["+d.mapName+"]不存在！");
				}else{
					MapArea ma = gi.getMapAreaByName(d.mapAreaName);
					if(ma != null){
						destinationList.add(d);
					}else{
//						System.out.println("【警告】车夫["+name+"]传递的某个目的地["+destinations[i]+"]对应的地图区域["+d.mapAreaName+"]不存在！");
					}
				}
			}else{
//				System.out.println("【警告】车夫["+name+"]传递的某个目的地["+destinations[i]+"]数据格式错误！");
			}
		}
	}
	/**
	 * clone出一个对象
	 */
	public Object clone() {
		DeliverNpc p = new DeliverNpc();
		p.cloneAllInitNumericalProperty(this);
		
		p.country = country;
		p.setnPCCategoryId(this.getnPCCategoryId());
		
		
		p.priceForOneStep = priceForOneStep;
		p.destinationList = destinationList;
		p.windowId = windowId;
		p.id = nextId();
		return p;
	}
}
