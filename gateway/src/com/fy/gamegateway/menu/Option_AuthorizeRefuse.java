package com.fy.gamegateway.menu;

import java.util.Arrays;

import com.fy.gamegateway.language.Translate;
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
public class Option_AuthorizeRefuse extends Option {

	@Override
	public void doSelect(Connection conn, String[] input) {
		LoginEntity entity = (LoginEntity)conn.getAttachmentData("LoginEntity");
		if (entity != null) {
			ClientAuthorization waitting = AuthorizeManager.getInstance().getWaittingClientAuthorization(entity.username);
			if (waitting != null) {
				boolean isOk = AuthorizeManager.getInstance().doManualAuthorize(entity.username, entity.clientId, waitting.getClientID(), false);
				if (isOk) {
					MenuWindowManager.sendSimpleMsg(conn, "", Translate.您已经拒绝了该设备的授权申请);
				}else {
					MenuWindowManager.sendSimpleMsg(conn, "", Translate.拒绝授权申请失败未知错误);
				}
			}else {
				MenuWindowManager.sendSimpleMsg(conn, Translate.拒绝授权失败, Translate.拒绝授权的等待授权信息已经过期);
				NewLoginHandler.logger.warn("[拒绝授权的等待不存在] ["+NewLoginHandler.getConnIp(conn)+"] ["+conn.getName()+"] ["+entity.getLogString()+"]");
			}
		}else {
			NewLoginHandler.logger.warn("[Option_AuthorizeRefuse连接中没找LoginEntity] ["+conn.getName()+"] ["+Arrays.toString(input)+"]");
		}
	}
}