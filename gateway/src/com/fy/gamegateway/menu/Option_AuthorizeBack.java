package com.fy.gamegateway.menu;

import com.fy.gamegateway.mieshi.waigua.AuthorizeManager;
import com.fy.gamegateway.mieshi.waigua.LoginEntity;
import com.xuanzhi.tools.transport.Connection;

public class Option_AuthorizeBack extends Option {

	@Override
	public void doSelect(Connection conn, String[] input) {
		LoginEntity entity = (LoginEntity)conn.getAttachmentData("LoginEntity");
		if (entity != null) {
			AuthorizeManager.getInstance().sendWaitAuthorizationWindow(conn);
		}
	}
}
