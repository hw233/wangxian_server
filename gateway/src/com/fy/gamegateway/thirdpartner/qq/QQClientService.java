package com.fy.gamegateway.thirdpartner.qq;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fy.gamegateway.tools.JacksonManager;
import com.fy.boss.client.BossClientService;
import com.xuanzhi.tools.text.StringUtil;

public class QQClientService {
	
	static Logger logger = Logger.getLogger(QQClientService.class);
	
	private static QQClientService self;
	
	public static QQClientService getInstance() {
		if(self == null) {
			self = new QQClientService();
		}
		return self;
	}
	
	public static String getCodeDescription(int code){
		if(code == 0){
			return "登录成功";
		}
		if(code == 1){
			return "登录失败";
		}
		return "未知响应码";
	}
	
	/**
	 * 验证sessionId有效性
	 * @param uin
	 * @param sessionId
	 * @return
	 */
	public boolean validate(String uin, String sessionId){
		long startTime = System.currentTimeMillis();
		if(uin.startsWith("QQUSER_")) {
			uin = uin.split("_")[1];
		}
		try {
			BossClientService bossService = BossClientService.getInstance();
			int result = bossService.validateQQLogin(uin, sessionId);
			logger.info("[调用登录接口] [成功] [result:"+result+"] [desp:"+this.getCodeDescription(result)+"] [uid:"+uin+"] [sid:"+sessionId+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return result==0;
		} catch (Exception e) {
			logger.info("[调用登录接口] [失败] [uid:"+uin+"] [sid:"+sessionId+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
			return false;
		}
	}
	
	
}
