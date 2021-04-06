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

public class Option_Authorize extends Option {
	
	@Override
	public void doSelect(Connection conn, String[] input) {
		LoginEntity entity = (LoginEntity)conn.getAttachmentData("LoginEntity");
		if (entity != null) {
			ClientAuthorization ca = AuthorizeManager.getInstance().getWaittingClientAuthorization(entity.username);
			if (ca != null) {
				MenuWindow mw = MenuWindowManager.createMenuWindow(600000);
				Option_AuthorizeOk opt1 = new Option_AuthorizeOk();
				opt1.setText(Translate.授权);
				Option_AuthorizeBack opt2 = new Option_AuthorizeBack();
				opt2.setText(Translate.返回);
				mw.setOptions(new Option[]{opt1, opt2});
				byte[] im = new byte[0];
				String titile = Translate.授权;
				String des = Translate.第一次授权;
				String[] inputTitle = new String[]{Translate.授权输入提示};
				String[] inputDF = new String[]{""};
				MieshiGatewayServer.getInstance().sendMessageToClient(conn, new NEW_QUERY_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getWinId(), titile, des, inputTitle, new byte[]{1}, new byte[]{30}, inputDF, im, mw.getOptions()));
				NewLoginHandler.logger.warn("[点击授权] [进入2次确认] [ipA="+NewLoginHandler.getConnIp(conn)+"] ["+conn.getName()+"] ["+entity.getLogString()+"]");
			}
		}
	}
}
