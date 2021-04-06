package com.fy.engineserver.util.datacheck;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.util.datacheck.event.MailEvent;

/**
 * 服务器检查
 * 
 *
 */
public class MailEventManger implements Runnable{

	public static MailEventManger self;
	
	public boolean isstart = true;
	
	//立即发送队列
	public List<MailEvent> taskQueue = Collections.synchronizedList(new LinkedList<MailEvent>());
	
	public static MailEventManger getInstance(){
		return self;
	}
	
	@Override
	public void run() {
		while(isstart){
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			if(taskQueue!=null && taskQueue.size()>0){
				MailEvent event = taskQueue.remove(0);
				if(event!=null){
					handleEvent(event);
				}
			}
		}
	}
	
	public void init(){
		self = this;
		try {
			Thread.sleep(10*1000);
		} catch (InterruptedException e) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		new Thread(this,"MailEventManger").start();
	}
	public void handleEvent(MailEvent event){}
//	public void handleEvent(MailEvent event){
//		String str = "[飘渺寻仙曲] [" + GameConstants.getInstance().getServerName() + "] [" + PlatformManager.getInstance().getPlatform().toString() + "] ["+event.name+"]";
//		String title = str;
//		StringBuffer sb = new StringBuffer();
//		sb.append("<B>问题列表如下：</B><br>");
//		sb.append("<HR>");
//		sb.append("<table style='font-size=12px;' border=1>");
//		sb.append("<tr bgcolor='greend'>");
//		sb.append("<td>序号</td>");
//		for (String t : event.mailTitle) {
//			sb.append("<td>");
//			sb.append(t);
//			sb.append("</td>");
//		}
//		sb.append("</tr>");
//		int index = 0;
//		for(int i=0;i<event.mailContent.length;i++){
//			sb.append("<tr>");
//			sb.append("<td>");
//			sb.append(++index);
//			sb.append("</td>");
//			for(String con : event.mailContent[i]){
//				sb.append("<td>");
//				sb.append(con);
//				sb.append("</td>");
//			}
//			sb.append("</tr>");
//		}
//		sb.append("</table>");
//		sb.append("<HR>");
//		DataCheckManager.sendMail(title, sb.toString());
//		if(ArticleManager.logger.isWarnEnabled()){
//			ArticleManager.logger.warn("[数据检查] [处理事件] [contentsize:"+event.mailContent.length+"] [event:"+event.toString()+"]");
//		}
//	}
	
	public void addTask(MailEvent event){
		if(event.type==0){
			taskQueue.add(event);
		}
		ArticleManager.logger.error("[数据检查] [添加事件] [taskQueue:"+taskQueue.size()+"] [event:"+event.toString()+"]");
	}
}
