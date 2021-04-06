package com.fy.gamegateway.gmaction;

import java.util.List;

import org.apache.log4j.Logger;

import com.fy.gamegateway.mieshi.waigua.NewLoginHandler;
import com.fy.gamegateway.thirdpartner.sms.SmsData;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class GmActionManager {
	
	private static GmActionManager instance;
	
	public static Logger logger = Logger.getLogger(GmActionManager.class);
	
	public SimpleEntityManager<GmAction> em;
	public SimpleEntityManager<SmsData> smsEm;

	public void init () {
		instance = this;
		
		em  = SimpleEntityManagerFactory.getSimpleEntityManager(GmAction.class);
		smsEm  = SimpleEntityManagerFactory.getSimpleEntityManager(SmsData.class);
	}
	
	public void destroy () {
		em.destroy();
		smsEm.destroy();
	}

	public static void setInstance(GmActionManager instance) {
		GmActionManager.instance = instance;
	}

	public static GmActionManager getInstance() {
		return instance;
	}
	
	public SmsData getDataByphoneNum(long phoneNum){
		try {
			List<SmsData> list = smsEm.query(SmsData.class, " phoneNumber = "+phoneNum, "lastReceiverDate desc", 1, 10);
			if(list != null && list.size() > 0){
				return list.get(0);
			}
		} catch (Exception e) {
		}
		return null;
	}
	
	public void saveData(SmsData data){
		try{
			long id = smsEm.nextId();
			data.setId(id);
			smsEm.save(data);
			NewLoginHandler.logger.warn("[快速注册存库] [成功] ["+data+"]");
		}catch(Exception e){
			e.printStackTrace();
			NewLoginHandler.logger.warn("[快速注册存库] [异常] ["+data+"]",e);
		}
	}
	
	public void handle_GM_ACTION_REQ (GmAction ac) {
		createGmAction(ac);
	}
	
	public void createGmAction(GmAction ac) {
		try{
			long id = em.nextId();
			ac.setId(id);
			ac.setVersion(0);
			em.save(ac);
			logger.warn("[收到GMACTION] ["+ac.getLogString()+"]");
		}catch(Exception e) {
			logger.error("handle_GM_ACTION_REQ出错:", e);
		}
	}
}
