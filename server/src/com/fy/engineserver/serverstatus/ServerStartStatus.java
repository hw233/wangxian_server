package com.fy.engineserver.serverstatus;

import java.util.Date;

import org.slf4j.Logger;

import com.fy.engineserver.event.Event;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.boss.game.GameConstants;

public class ServerStartStatus {

	public static Logger logger = org.slf4j.LoggerFactory.getLogger(ServerStartStatus.class);

	static ServerStartStatus self;

	public static ServerStartStatus getInstance() {
		return self;
	}

	public void init() {
		
		GameConstants constants = GameConstants.getInstance();
		String serverName = "--";
		if (constants != null) {
			serverName = constants.getServerName();
		}
		String date = TimeTool.formatter.varChar19.format(new Date());
//		System.out.println("[START-" + ServerStartStatus.class.getSimpleName() + "] [服务器启动完毕] [" + serverName + "] [" + date + "] [平台:" + PlatformManager.getInstance().getPlatform().toString() + "]");
		logger.error("[START-" + ServerStartStatus.class.getSimpleName() + "] [服务器启动完毕] [" + serverName + "] [" + date + "] [平台:" + PlatformManager.getInstance().getPlatform().toString() + "]");
		self = this;
		EventRouter.getInst().addEvent(new EventWithObjParam(Event.SERVER_STARTED, null));
		ServiceStartRecord.startLog(this);

	}
}
