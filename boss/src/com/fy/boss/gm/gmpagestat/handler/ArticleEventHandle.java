package com.fy.boss.gm.gmpagestat.handler;


import java.util.ArrayList;
import java.util.List;

import com.fy.boss.gm.gmpagestat.GmEventManager;
import com.fy.boss.gm.gmpagestat.RecordEvent;
import com.fy.boss.gm.gmpagestat.EventForJson.EventType;
import com.fy.boss.gm.gmpagestat.event.ArticleRecordEvent;
import com.fy.boss.gm.gmpagestat.handler.EventHandler;

public class ArticleEventHandle extends EventHandler{

	private int 物品报警数量 = 5;
	
	private String [] titles =	{"类型","服务器","账号","角色名","角色id","IP地址","物品集","物品统计集","颜色集","数量集","操作人","操作时间"};
	
	/**
	 * 符合规则的事件集合
	 */
	public List<ArticleRecordEvent> events = new ArrayList<ArticleRecordEvent>();
	
	public ArticleEventHandle(){
		this.eventtype = EventType.发送物品;
	}
	
	@Override
	public void initFitRule(List<RecordEvent> es) {
//		GmEventManager.getInstance().updateEventData(es);
		for(RecordEvent e : es){
			//TODO 
			ArticleRecordEvent aeh = (ArticleRecordEvent)e;
//			int[] articlecounts = aeh.getArticlecount();
//			for(int i=0; i<articlecounts.length;i++){
//				if(articlecounts[i]>=物品报警数量){
					events.add(aeh);
//				}
//			}
		}
		if(events.size()>0){
			handle();
		}
	}

	@Override
	public void handle() {
		GmEventManager.log.warn("【发邮件咯】 [in ArticleEventHandle..] [in handle()..] [events:"+events.size()+"] ["+events+"]");
		// TODO Auto-generated method stub
		String title = "后台物品相关操作";
		StringBuffer content = new StringBuffer();
		if(events!=null && events.size()>0){
			content.append("<table style='font-size=12px;' border=1><tr bgcolor='greend'>");
			for(String t:titles){
				content.append("<th>");
				content.append(t);
				content.append("</th>");
			}
			content.append("</tr>");
			
			for(ArticleRecordEvent ae:events){
				content.append("<tr>"+ae.toString()+"</tr>");
			}
			content.append("</table>");
			GmEventManager.sendMail(title, content.toString());
			events.clear();
		}
	}
	
}
