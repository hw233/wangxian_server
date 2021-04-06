package com.fy.gamegateway.menu;

import java.util.Arrays;

import com.fy.gamegateway.language.Translate;
import com.fy.gamegateway.mieshi.waigua.AuthorizeManager;
import com.fy.gamegateway.mieshi.waigua.ClientAuthorization;
import com.fy.gamegateway.mieshi.waigua.LoginEntity;
import com.fy.gamegateway.mieshi.waigua.NewLoginHandler;
import com.xuanzhi.tools.transport.Connection;

public class Option_AuthorizeOk extends Option {

	@Override
	public void doSelect(Connection conn, String[] input) {
		LoginEntity entity = (LoginEntity)conn.getAttachmentData("LoginEntity");
		if (entity != null) {
			ClientAuthorization waitting = AuthorizeManager.getInstance().getWaittingClientAuthorization(entity.username);
			if (waitting != null) {
				if (Translate.授权.equals(input[0])) {
					boolean isOk = AuthorizeManager.getInstance().doManualAuthorize(entity.username, entity.clientId, waitting.getClientID(), true);
					if (isOk) {
						MenuWindowManager.sendSimpleMsg(conn, "", Translate.您已经成功授权给该设备);
					}else {
						MenuWindowManager.sendSimpleMsg(conn, "", Translate.拒绝授权申请失败未知错误);
					}
				}else {
					MenuWindowManager.sendSimpleMsg(conn, Translate.授权失败, Translate.您的输入有误授权未成功);
				}
			}else {
				MenuWindowManager.sendSimpleMsg(conn, Translate.授权失败, Translate.将要授权的等待授权信息不存在授权未成功);
				NewLoginHandler.logger.warn("[授权的等待不存在] ["+NewLoginHandler.getConnIp(conn)+"] ["+conn.getName()+"] ["+entity.getLogString()+"]");
			}
		}else {
			NewLoginHandler.logger.warn("[Option_AuthorizeOk连接中没找LoginEntity] ["+conn.getName()+"] ["+Arrays.toString(input)+"]");
		}
	}
}
