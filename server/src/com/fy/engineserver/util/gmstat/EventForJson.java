package com.fy.engineserver.util.gmstat;
import java.util.*;

import com.fy.engineserver.util.gmstat.event.*;

/**
 * json传递泛型简单结构
 * 
 *
 */
public class EventForJson {
	
	public List<PlayerPropertyChangeEvent> plist = new ArrayList<PlayerPropertyChangeEvent>();
	public List<ArticleRecordEvent> alist = new ArrayList<ArticleRecordEvent>();
	public List<MailEvent> mlist = new ArrayList<MailEvent>();
	
	public enum EventType {
		发送邮件,发送物品,属性修改;
	}
	
	public void addEvents(RecordEvent e){
		EventType type = e.eventtype;
		if(type==EventType.属性修改){
			plist.add((PlayerPropertyChangeEvent)e);
		}else if(type==EventType.发送邮件){
			mlist.add((MailEvent)e);
		}else if(type==EventType.发送物品){
			alist.add((ArticleRecordEvent)e);
		} 
		EventManager.logger.warn("[addevent] [add] [success] [type:"+type+"] [alist:"+alist.size()+"] [mlist:"+mlist.size()+"] [plist:"+plist.size()+"]");
	}
	
	public void clearEvents(){
		try{
			alist.clear();
			mlist.clear();
			plist.clear();
		}catch (Exception e) {
			EventManager.logger.warn("[clearEvents] [excepiont] ["+e+"]");
		}
	}
	
	public List getEvents(EventType type){
		if(type==EventType.发送物品){
			return alist;
		}else if(type==EventType.发送邮件){
			return mlist;
		}else if(type==EventType.属性修改){
			return plist;
		}
		return null;
	}
	
	public List<ArticleRecordEvent> getAlist() {
		return alist;
	}
	public void setAlist(List<ArticleRecordEvent> alist) {
		this.alist = alist;
	}
	public List<MailEvent> getMlist() {
		return mlist;
	}
	public void setMlist(List<MailEvent> mlist) {
		this.mlist = mlist;
	}
	public List<PlayerPropertyChangeEvent> getPlist() {
		return plist;
	}
	public void setPlist(List<PlayerPropertyChangeEvent> plist) {
		this.plist = plist;
	}

}
