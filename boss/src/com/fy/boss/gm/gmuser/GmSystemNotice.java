package com.fy.boss.gm.gmuser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.fy.boss.gm.gmuser.server.GmSystemNoticeManager;
import com.fy.boss.gm.gmuser.NoticeTimeType;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;


@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"recorder"})
})
public class GmSystemNotice {

	@SimpleId
	protected long id;
	
	@SimpleVersion
	protected int version;
	
	public String recorder;
	
	//每条消息发送到次数
	public int sendnum;
	
	//多条内容发完到下一次发的间隔
	public long messLength;
	
	//多条内容之间的轮播间隔
	public long messesLength;
	
	public String sentsystemnotice;
	
	public String [] urlnames;
	
	public String [] cons;
	
	public long lastSendTime;
	
	public long creattime;
	
	public int state;
	
	private String[] days;
	
	private String limitBeginTime;
	
	private String limitEndTime;
	
	/**
	 * 
	 */
	@SimpleColumn(length = 65535)
	public NoticeTimeType timeType;
	
	public NoticeTimeType getTimeType() {
		return timeType;
	}

	public void setTimeType(NoticeTimeType timeType) {
		this.timeType = timeType;
	}

	public boolean isSameDay(String daytime){
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		long date;
		Calendar cl = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			date = dateformat.parse(daytime).getTime();
			cl.setTimeInMillis(date);
			c2.setTimeInMillis(System.currentTimeMillis());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int year = cl.get(Calendar.YEAR);
		int month = cl.get(Calendar.MONTH);
		int day = cl.get(Calendar.DAY_OF_MONTH);
		int curr_year = c2.get(Calendar.YEAR);
		int curr_month = c2.get(Calendar.MONTH);
		int curr_day = c2.get(Calendar.DAY_OF_MONTH);
		return year==curr_year&&day==curr_day&&curr_month==month;
	}
	
	public boolean needSendNotice(long now){
		try{
			if(!isvalid(now)){
				return false;
			}
			if(days!=null&&days.length>0){
				for(String day:days){
					if(isSameDay(day)){
						if(limitBeginTime!=null&&limitEndTime!=null){
							if(now>=GmSystemNoticeManager.获得毫秒数(day+" "+limitBeginTime)&&now<=GmSystemNoticeManager.获得毫秒数(day+" "+limitEndTime)){
								return true;
							}
						}
					}
				}
			}
		}catch(Exception e){
			GmSystemNoticeManager.logger.warn("[是否发送公告] [异常]",e);
		}
		return false;
	} 
	
	public void sendNotice(String content,int type) {
		long now = System.currentTimeMillis();
		if(content!=null&&content.trim().length()>0){
			if(this.getSendnum()>0){
				for(int k=0;k<this.getSendnum();k++){
					if(this.getUrlnames()!=null&&this.getUrlnames().length>0){
						String urls [] = this.getUrlnames();
						String result = null;
						for(int j=0;j<urls.length;j++){
							String args = "";
							if(urls[j].contains("116.213.192.203:8080")){
								args = "authorize.username=serverUser&authorize.password=kj2#($1238!salkhdo978HGm).p&msgContent="+content+"&selected="+type;
							}else{
								args = "authorize.username="+GmSystemNoticeManager.uname+"&authorize.password="+GmSystemNoticeManager.nped+"&msgContent="+content+"&selected="+type;
							}
							try {
								byte [] b = HttpUtils.webPost(new URL(urls[j].substring(0,urls[j].indexOf("gm"))+"admin/"+"system_chatmessage.jsp"), args.getBytes(), new HashMap(), 60000, 60000);
								result = new String(b,"utf-8");
							} catch (MalformedURLException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						GmSystemNoticeManager.logger.warn("[发送公告] [成功] [result:"+result+"] [发送次数"+this.getSendnum()+"] [发送人："+this.getRecorder()+"] [消息间隔："+this.getMessLength()+"] [轮播间隔："+this.getMessesLength()+"] [内容："+content+"] [耗时："+(System.currentTimeMillis()-now)+"] ");
					}
				}
			}
		}
	}
	
	public boolean isvalid(long now) {
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(days!=null && days.length>0){
			try{
				String maxDay = days[days.length-1]+" 23:59:00";
				GmSystemNoticeManager.logger.warn("[是否有效] [maxDay:"+maxDay+"] [是否有效："+(now < GmSystemNoticeManager.获得毫秒数(maxDay))+"] [下次发送:"+sdf.format(lastSendTime+messLength)+"]");
				if(now > GmSystemNoticeManager.获得毫秒数(maxDay)){
					return false;
				}
				return true;
			}catch(Exception e){
				e.printStackTrace();
				GmSystemNoticeManager.logger.warn("[是否有效] [异常]",e);
			}
		}
		return false;
	}
	public long getCreattime() {
		return creattime;
	}

	public void setCreattime(long creattime) {
		this.creattime = creattime;
		GmSystemNoticeManager.getInstance().em.notifyFieldChange(this,"creattime");
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
		GmSystemNoticeManager.getInstance().em.notifyFieldChange(this,"state");
	}

	public void setVersion(int version) {
		this.version = version;
	}
	public long getLastSendTime() {
		return lastSendTime;
	}

	public void setLastSendTime(long lastSendTime) {
		this.lastSendTime = lastSendTime;
		GmSystemNoticeManager.getInstance().em.notifyFieldChange(this,"lastSendTime");
	}

	public String[] getUrlnames() {
		return urlnames;
	}

	public void setUrlnames(String[] urlnames) {
		this.urlnames = urlnames;
		GmSystemNoticeManager.getInstance().em.notifyFieldChange(this,"urlnames");
	}

	public String[] getCons() {
		return cons;
	}

	public void setCons(String[] cons) {
		this.cons = cons;
		GmSystemNoticeManager.getInstance().em.notifyFieldChange(this,"cons");
	}

	public String getRecorder() {
		return recorder;
	}

	public void setRecorder(String recorder) {
		this.recorder = recorder;
		GmSystemNoticeManager.getInstance().em.notifyFieldChange(this,"recorder");
	}

	public int getSendnum() {
		return sendnum;
	}

	public void setSendnum(int sendnum) {
		this.sendnum = sendnum;
		GmSystemNoticeManager.getInstance().em.notifyFieldChange(this,"sendnum");
	}

	public long getMessLength() {
		return messLength;
	} 

	public void setMessLength(long messLength) {
		this.messLength = messLength;
		GmSystemNoticeManager.getInstance().em.notifyFieldChange(this,"messLength");
	}
	
	public long getMessesLength() {
		return messesLength;
	}

	public void setMessesLength(long messesLength) {
		this.messesLength = messesLength;
		GmSystemNoticeManager.getInstance().em.notifyFieldChange(this,"messesLength");
	}

	public String[] getDays() {
		return days;
	}

	public void setDays(String[] days) {
		this.days = days;
		GmSystemNoticeManager.getInstance().em.notifyFieldChange(this,"days");
	}

	public String getLimitBeginTime() {
		return limitBeginTime;
	}

	public void setLimitBeginTime(String limitBeginTime) {
		this.limitBeginTime = limitBeginTime;
		GmSystemNoticeManager.getInstance().em.notifyFieldChange(this,"limitBeginTime");
	}

	public String getLimitEndTime() {
		return limitEndTime;
	}

	public void setLimitEndTime(String limitEndTime) {
		this.limitEndTime = limitEndTime;
		GmSystemNoticeManager.getInstance().em.notifyFieldChange(this,"limitEndTime");
	}

	public String getSentsystemnotice() {
		return sentsystemnotice;
	}

	public void setSentsystemnotice(String sentsystemnotice) {
		this.sentsystemnotice = sentsystemnotice;
		GmSystemNoticeManager.getInstance().em.notifyFieldChange(this,"sentsystemnotice");
	}

}
