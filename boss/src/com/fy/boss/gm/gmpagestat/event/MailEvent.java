package com.fy.boss.gm.gmpagestat.event;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import com.fy.boss.gm.gmpagestat.RecordEvent;
import com.fy.boss.gm.gmpagestat.EventForJson.EventType;
import com.xuanzhi.boss.game.GameConstants;
public class MailEvent extends RecordEvent implements Serializable{

	private String mailTitle;
	
	private String mailContent;
	
	public String articlenames[];
	
	public int articlecount[];
	
	/**
	 * 关注的物品类型:
	 * 1:装备
	 * 2:一般道具
	 * 3:帖
	 * 4:酒
	 */
	public int articletype[];

	public int colors[];
	
	public long coins;

	public MailEvent(){}
	
	public MailEvent(String mailTitle,String mailContent,String articlenames[],int articlecount[],int colors[],int articletype[],long coins,String playername,String ip,String recorder){
		this.mailTitle = mailTitle;
		this.mailContent = mailContent;
		this.articlenames = articlenames;
		this.articlecount = articlecount;
		this.articletype = articletype;
		this.colors = colors;
		this.coins = coins;
		setPlayername(playername);
		setEventtype(EventType.发送邮件);
		setIpaddress(ip);
		setRecorder(recorder);
		setRecordtime(System.currentTimeMillis());
		setServername(GameConstants.getInstance().getServerName());
	}
	
	public MailEvent(String mailTitle,String mailContent,String articlenames[],int articlecount[],long coins,String playername,String ip,String recorder){
		this.mailTitle = mailTitle;
		this.mailContent = mailContent;
		this.articlenames = articlenames;
		this.articlecount = articlecount;
		this.coins = coins;
		setPlayername(playername);
		setEventtype(EventType.发送邮件);
		setIpaddress(ip);
		setRecorder(recorder);
		setRecordtime(System.currentTimeMillis());
		setServername(GameConstants.getInstance().getServerName());
	}
	
	public String getMailTitle() {
		return mailTitle;
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	public String[] getArticlenames() {
		return articlenames;
	}

	public void setArticlenames(String[] articlenames) {
		this.articlenames = articlenames;
	}

	public int[] getArticlecount() {
		return articlecount;
	}

	public void setArticlecount(int[] articlecount) {
		this.articlecount = articlecount;
	}

	public long getCoins() {
		return coins;
	}

	public void setCoins(long coins) {
		this.coins = coins;
	}
	
	public int[] getColors() {
		return colors;
	}

	public void setColors(int[] colors) {
		this.colors = colors;
	}

	public int[] getArticletype() {
		return articletype;
	}

	public void setArticletype(int[] articletype) {
		this.articletype = articletype;
	}
	@Override
	public String toString() {
		return "<td>发送邮件</td><td>"+servername+"</td><td>"+username+"</td><td>"+playername+"</td><td>"+pid+"</td><td>"+ipaddress+"</td>"+"<td>"+mailTitle+"</td>"+"<td>"+mailContent+"</td>"+"<td>"+Arrays.toString(colors)+"</td>"+"<td>"+Arrays.toString(articlecount)+"</td><td>"+coins+"</td><td>"+recorder+"</td>"+"<td>"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(recordtime)+"</td>";
	}
}
