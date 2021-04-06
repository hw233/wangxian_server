package com.fy.boss.gm.gmpagestat.handler;

import java.util.ArrayList;
import java.util.List;

import com.fy.boss.gm.gmpagestat.GmEventManager;
import com.fy.boss.gm.gmpagestat.RecordEvent;
import com.fy.boss.gm.gmpagestat.EventForJson.EventType;
import com.fy.boss.gm.gmpagestat.event.MailEvent;
import com.fy.boss.gm.gmpagestat.event.PlayerPropertyChangeEvent;
import com.fy.boss.gm.gmpagestat.handler.EventHandler;

public class MailEventHandle extends EventHandler{
	
	
	/**
	 * 符合规则的事件集合
	 */
	public List<MailEvent> events = new ArrayList<MailEvent>();
	
	private String [] titles =	{"类型","服务器","账号","角色名","id","IP地址","邮件标题","邮件内容","颜色集","数量集","绑银","操作人","操作时间"};
	
	public MailEventHandle(){
		this.eventtype = EventType.发送邮件;
	}

	@Override
	public void initFitRule(List<RecordEvent> es) {
		for(RecordEvent e : es){
			MailEvent aeh = (MailEvent)e;
		}
		if(events.size()>0){
			handle();
		}
	}

	@Override
	public void handle() {
		GmEventManager.log.warn("【发邮件咯】 [in MailEventHandle..] [in handle()..] [events:"+events.size()+"] ["+events+"]");
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
			
			for(MailEvent ae:events){
				content.append("<tr>"+ae.toString()+"</tr>");
			}
			content.append("</table>");
			GmEventManager.sendMail(title, content.toString());
			events.clear();
		}
	}
	


}
