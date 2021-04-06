package com.fy.engineserver.util;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;

public class GameSystemGlobal {
//	public static Logger log = Logger.getLogger(GameSystemGlobal.class);
public	static Logger log = LoggerFactory.getLogger(GameSystemGlobal.class);
	
	public void init() {
			if (log.isDebugEnabled()) log.debug("[初始化GameSystemGlobal]");
	}
	
	public void destroy() {
		//
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		PlayerManager pm = PlayerManager.getInstance();
		Player ps[] = pm.getOnlinePlayers();
		for(Player p : ps) {
			if(!p.isLeavedServer()) {
				p.leaveServer();
			}
		}
//		log.debug("[销毁GameSystemGlobal] [下线玩家:"+ps.length+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-now)+"]");
			if (log.isDebugEnabled()) log.debug("[销毁GameSystemGlobal] [下线玩家:{}] [{}]", new Object[]{ps.length,(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-now)});
	}
}
