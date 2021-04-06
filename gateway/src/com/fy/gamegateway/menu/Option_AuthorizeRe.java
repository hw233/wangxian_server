package com.fy.gamegateway.menu;

import com.fy.gamegateway.language.Translate;
import com.fy.gamegateway.message.GameMessageFactory;
import com.fy.gamegateway.message.NEW_QUERY_WINDOW_REQ;
import com.fy.gamegateway.mieshi.server.MieshiGatewayServer;
import com.fy.gamegateway.mieshi.waigua.AuthorizeManager;
import com.fy.gamegateway.mieshi.waigua.ClientAuthorization;
import com.fy.gamegateway.mieshi.waigua.LoginEntity;
import com.fy.gamegateway.mieshi.waigua.NewLoginHandler;
import com.xuanzhi.tools.transport.Connection;

/**
 * 拒绝授权
 * 
 *
 */
public class Option_AuthorizeRe extends Option {
	
	@Override
	public void doSelect(Connection conn, String[] input) {
		LoginEntity entity = (LoginEntity)conn.getAttachmentData("LoginEntity");
		if (entity != null) {
			ClientAuthorization ca = AuthorizeManager.getInstance().getWaittingClientAuthorization(entity.username);
			if (ca != null) {
				MenuWindow mw = MenuWindowManager.createMenuWindow(600000);
				Option_AuthorizeRefuse opt1 = new Option_AuthorizeRefuse();
				opt1.setText(Translate.拒绝);
				Option_AuthorizeBack opt2 = new Option_AuthorizeBack();
				opt2.setText(Translate.返回);
				mw.setOptions(new Option[]{opt1, opt2});
				String title = Translate.拒绝授权;
				String des = Translate.拒绝授权说明;
				MieshiGatewayServer.getInstance().sendMessageToClient(conn, new NEW_QUERY_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getWinId(), title, des, new String[0], new byte[0], new byte[0], new String[0], new byte[0], mw.getOptions()));
				NewLoginHandler.logger.warn("[拒绝授权] [去2次确认] ["+NewLoginHandler.getConnIp(conn)+"] ["+conn.getName()+"] ["+entity.getLogString()+"]");
			}
		}
	}
}
