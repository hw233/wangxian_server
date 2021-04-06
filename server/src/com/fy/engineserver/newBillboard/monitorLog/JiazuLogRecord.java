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



//家族活动排名日志
public class JiazuLogRecord extends LogRecord{

	//九游 6.28.12
	////当乐 7.4.10
	
	public JiazuLogRecord(){
		String[] dateString = {"2012-11-20 12:00:00","2012-11-19 12:00:00","2012-11-22 12:00:00","2012-11-23 12:00:00",
				"2012-11-29 12:00:00","2012-11-30 12:00:00","2012-12-01 12:00:00","2012-12-06 12:00:00",
				"2012-12-07 12:00:00","2012-12-10 12:00:00","2012-12-13 12:00:00","2012-12-14 12:00:00",
				"2012-12-19 12:00:00","2012-12-20 12:00:00","2012-12-21 12:00:00","2012-12-27 12:00:00","2012-12-28 12:00:00"};
		String[] platform = {"官方","9游","九游","当乐","91","UC","台湾","台湾91","UC","台湾","app91","uc","uc","91","台湾","91","ucapp"};
		this.dateString = dateString;
		this.platform = platform;
	}
	
	public void 打印(Billboard billboard,String platForm,String dateString){
		
		BillboardDate[] datas = billboard.getDates();
		String menu = billboard.getMenu();
		String subMenu = billboard.getSubMenu();
		List<NewServerBillboard> nsBillboards=new LinkedList<NewServerBillboard>();

		if(datas != null && datas.length > 0){

			for(BillboardDate data : datas){
				PlayerManager pm = PlayerManager.getInstance();
//				Player player = null;
				List<SimplePlayer4Load> simpleInfo = null;
				SimplePlayer4Load player = null;
				int length = datas[0].getDateValues().length;
				String value = "";
				NewServerBillboard nsBillboard=new NewServerBillboard();
				nsBillboard.setMenu(menu);
				nsBillboard.setSubMenu(subMenu);
				String name = data.getDateValues()[0];
				if(name != null){
					nsBillboard.setName(name);
					try{
//						if(pm.isOnline(name)){
//							player = pm.getPlayer(name);
						simpleInfo = pm.getSimplePlayer4Load(name);
						if (simpleInfo != null && simpleInfo.size() > 0) {
							player = simpleInfo.get(0);
						}
//						}
					}catch(Exception e){
						ArticleManager.logger.error("[打印当日家族活动日志异常]",e);
					}
				}
				
				if(player != null){
					nsBillboard.setId(player.getId());
					nsBillboard.setUserName(player.getUsername());
				}
				
				if(length> 0 && length<=data.getDateValues().length){
					value = data.getDateValues()[length -1];
				}
				nsBillboards.add(nsBillboard);
				ArticleManager.logger.error("[为活动打印日志] ["+menu+"] ["+subMenu+"] ["+platForm+"] ["+dateString+"] ["+data.getDateValues()[0]+"] ["+data.getDateValues()[3]+"]");
			}
			BillboardsManager.getInstance().addToDisCatch(nsBillboards);
			ArticleManager.logger.error("[为活动打印日志完毕] ["+menu+"] ["+subMenu+"] ["+platForm+"] ["+dateString+"]");
		}
		
	}
	
	
}
