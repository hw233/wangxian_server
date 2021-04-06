package com.fy.engineserver.newBillboard.monitorLog;

import java.util.LinkedList;
import java.util.List;

import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.SimplePlayer4Load;
import com.fy.boss.authorize.model.Passport;

//当日鲜花日志记录
public class MonthFlowerLogRecord extends LogRecord{


	//	6.29.12   官方  
	// 日期格式 2012-06-14 21:00:00
	
	
	public MonthFlowerLogRecord(){
		String[] dateString = {"2012-11-08 12:57:00","2012-11-08 13:00:00","2012-11-08 13:10:00","2012-11-12 12:00:00"};
		String[] platform = {"官方","官方","官方","官方"};

		this.dateString = dateString;
		this.platform = platform;
	}
	
	
	public void 打印(Billboard billboard,String platForm,String dateString){
		
		BillboardDate[] datas = billboard.getDates();
		String menu = billboard.getMenu();
		String subMenu = billboard.getSubMenu();
		List<NewServerBillboard> nsBillboards=new LinkedList<NewServerBillboard>();

		if(datas != null && datas.length > 0){
			
			PlayerManager pm = PlayerManager.getInstance();
			int length = datas[0].getDateValues().length;
			String value = "";
			for(BillboardDate data : datas){
				NewServerBillboard nsBillboard=new NewServerBillboard();
				nsBillboard.setMenu(menu);
				nsBillboard.setSubMenu(subMenu);
//				Player player = null;
				List<SimplePlayer4Load> simpleInfo = null;
				SimplePlayer4Load player = null;
				Passport pp = null;
				String channel = null;
				String userName = null;
				String name = data.getDateValues()[0];
				if(name != null){
					try{
						nsBillboard.setName(name);
//						if(pm.isOnline(name)){
//							player = pm.getPlayer(name);
						simpleInfo = pm.getSimplePlayer4Load(name);
						if (simpleInfo != null && simpleInfo.size() > 0) {
							player = simpleInfo.get(0);
						}
//						}
					}catch(Exception e){
						ArticleManager.logger.error("[打印当月鲜花活动日志异常]",e);
					}
				}
				
				if(player != null){
					nsBillboard.setUserName(player.getUsername());
					nsBillboard.setId(player.getId());
					userName = player.getUsername();
//					if(pp != null){
//						channel = pp.getRegisterChannel();
//						userName = pp.getUserName();
//					}
				}
				
				if(length> 0 && length<=data.getDateValues().length){
					value = data.getDateValues()[length -1];
				}
				nsBillboards.add(nsBillboard);
				ArticleManager.logger.error("[为活动打印日志] ["+menu+"] ["+subMenu+"] ["+platForm+"] ["+dateString+"] ["+data.getDateValues()[0]+"] [渠道:"+channel+"] [账号:"+userName+"] ["+value+"]");
			}
			BillboardsManager.getInstance().addToDisCatch(nsBillboards);
			ArticleManager.logger.error("[为活动打印日志完毕] ["+menu+"] ["+subMenu+"] ["+platForm+"] ["+dateString+"]");
			
		}
		
	}
	
	
}
