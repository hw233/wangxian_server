package com.fy.boss.gm.gmpagestat;

import java.io.Serializable;
import java.util.*;

import com.fy.boss.gm.gmpagestat.event.*;
import com.fy.boss.gm.gmpagestat.event.ArticleRecordEvent;
import com.fy.boss.gm.gmpagestat.event.MailEvent;
import com.fy.boss.gm.gmpagestat.event.PlayerPropertyChangeEvent;

/**
 * 事件数据,按天统计
 * @author wtx
 *
 */
public class EventData implements Serializable{

	/**
	 * 2013-06-27
	 * 哪一天的数据
	 */
	public String eventKey;
	
	public List<ArticleRecordEvent> alist = new ArrayList<ArticleRecordEvent>();
	
	public List<MailEvent> mlist = new ArrayList<MailEvent>();
	
	public List<PlayerPropertyChangeEvent> plist = new ArrayList<PlayerPropertyChangeEvent>();

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
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
