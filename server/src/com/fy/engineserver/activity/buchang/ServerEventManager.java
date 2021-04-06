package com.fy.engineserver.activity.buchang;

import java.util.*;

import org.slf4j.Logger;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.sprite.Player;

public class ServerEventManager {

	public static Logger logger = ActivitySubSystem.logger;
	
	private static ServerEventManager self;
	
	public static ServerEventManager getInstance(){
		return self;
	}
	
	/**
	 * 临时补偿，暂不存盘记录
	 */
	private List<ServerEvent> events = new ArrayList<ServerEvent>();

	public void init(){
		self = this;
	}
	
	public void addEvent(ServerEvent event){
		events.add(event);
		logger.warn("[添加新的事件] ["+event+"] [事件数量："+events.size()+"]");
	}
	
	public void doEvent(Player player, EventType type){
		logger.warn("[执行事件] [type:"+type+"] [玩家:"+player.getName()+"] [事件数量："+events.size()+"]");
		for(ServerEvent event : events){
			if(event.type == type){
				event.entity.doEvent(player);
			}
		}
	}
	
	public static void main(String[] args) {
		self.addEvent(new ServerEvent(EventType.玩家上线,new EventExec() {
			
			@Override
			public void doEvent(Player p) {
				// TODO Auto-generated method stub
				
			}
		}));
	}
}
