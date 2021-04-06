package com.fy.engineserver.sprite;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.gateway.GameSubSystem;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class TitleSubSystem implements GameSubSystem{

//	public static Logger logger = Logger.getLogger(TitleSubSystem.class);
public	static Logger logger = LoggerFactory.getLogger(TitleSubSystem.class);
	
	protected PlayerManager playerManager;
	
	public void init() throws Exception{
		self = this;
	}
	
	private static TitleSubSystem self;

	public static TitleSubSystem getInstance() {
		return self;
	}

	public String[] getInterestedMessageTypes() {
		return new String[] {"QUERY_PLAYER_TITLE_REQ","SET_DEFAULT_TITLE_REQ"};
	}


	public String getName() {
		return "TitleSubSystem";
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn,
			RequestMessage message) throws ConnectionException, Exception {
		return null;
	}

	@Override
	public boolean handleResponseMessage(Connection conn,
			RequestMessage request, ResponseMessage response)
			throws ConnectionException, Exception {
		return false;
	}

}
