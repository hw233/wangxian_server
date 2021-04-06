package com.fy.engineserver.newBillboard.monitorLog;

import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.boss.authorize.model.Passport;

//连斩活动日志记录
public class MonthKillJoinLogRecord extends LogRecord{


	//	6.29.12   官方  
	// 日期格式 2012-06-14 21:00:00
	
	
	public MonthKillJoinLogRecord(){
		String[] dateString = {"2012-11-08 12:57:00","2012-11-08 13:00:00","2012-11-08 13:10:00","2012-11-12 12:00:00"};
		String[] platform = {"官方","官方","官方","官方"};
		this.dateString = dateString;
		this.platform = platform;
	}
	
	
	public void 打印(Billboard billboard,String platForm,String dateString){
		
		BillboardDate[] datas = billboard.getDates();
		String menu = billboard.getMenu();
		String subMenu = billboard.getSubMenu();
		

		if(datas != null && datas.length > 0){
			
			PlayerManager pm = PlayerManager.getInstance();
			for(BillboardDate data : datas){
				Player player = null;
				Passport pp = null;
				String channel = null;
				String userName = null;
				String name = data.getDateValues()[0];
				if(name != null){
					try{
						if(pm.isOnline(name)){
							player = pm.getPlayer(name);
						}
					}catch(Exception e){
						ArticleManager.logger.error("[打印当月连斩活动日志异常]",e);
					}
				}
				
				if(player != null){
					pp = player.getPassport();
					if(pp != null){
						channel = pp.getRegisterChannel();
						userName = pp.getUserName();
					}
				}
				ArticleManager.logger.error("[为当月活动打印日志] ["+menu+"] ["+subMenu+"] ["+platForm+"] ["+dateString+"] ["+data.getDateValues()[0]+"] [渠道:"+channel+"] [账号:"+userName+"] [数目:"+data.getDateValues()[3]+"]");
			}
			ArticleManager.logger.error("[为当月活动打印日志完毕] ["+menu+"] ["+subMenu+"] ["+platForm+"] ["+dateString+"]");
			
		}
		
	}
	
	
}
