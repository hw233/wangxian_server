package com.fy.engineserver.core;

import java.lang.reflect.Method;

import org.slf4j.Logger;

import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.hotspot.HotspotManager;
import com.fy.engineserver.message.HOTSPOT_SEE_REQ;
import com.fy.engineserver.message.HOTSPOT_SEE_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

//热点相关协议处理
public class HotspotSubSystem extends SubSystemAdapter  {
	public	static Logger logger = HotspotManager.logger;
	
	public static HotspotSubSystem instance;
	public void init() {
		
		instance = this;
		ServiceStartRecord.startLog(this);
	}

	@Override
	public String getName() {
		return "HotspotSubSystem";
	}

	@Override
	public String[] getInterestedMessageTypes() {
		return new String[]{"HOTSPOT_SEE_REQ"};
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn,
			RequestMessage message) throws ConnectionException, Exception {
		Player player = check(conn, message, logger);
		if(useMethodCache){
			return super.handleReqeustMessage(conn, message, player);
		}

		Class clazz = this.getClass();
		Method m1 = clazz.getDeclaredMethod("handle_" + message.getTypeDescription(), Connection.class, RequestMessage.class, Player.class);
		m1.setAccessible(true);
		return (ResponseMessage) m1.invoke(this, conn, message, player);
	}

	@Override
	public boolean handleResponseMessage(Connection conn,
			RequestMessage request, ResponseMessage response)
			throws ConnectionException, Exception {
		return false;
	}

	public ResponseMessage handle_HOTSPOT_SEE_REQ(Connection conn, RequestMessage message, Player player){
		HOTSPOT_SEE_REQ req = (HOTSPOT_SEE_REQ)message;
		int hotspotId = req.getHotspotID();
		CompoundReturn re = HotspotManager.getInstance().seeHotspot(player, hotspotId);
		if (re != null) {
			HOTSPOT_SEE_RES res = new HOTSPOT_SEE_RES(req.getSequnceNum(), re.getIntValue(), re.getStringValue());
			return res;
		}
		return null;
	}
}
