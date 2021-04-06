package com.fy.engineserver.util.gmstat.event;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.gmstat.RecordEvent;
import com.fy.engineserver.util.gmstat.EventForJson.EventType;
import com.xuanzhi.boss.game.GameConstants;
/**
 * 邮件事件
 * 
 *
 */
public class MailEvent extends RecordEvent{

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
	
	public String tostring(){
		return "<td>发送邮件</td><td>"+servername+"</td><td>"+username+"</td><td>"+playername+"</td><td>"+pid+"</td><td>"+ipaddress+"</td>"+"<td>"+mailTitle+"</td>"+"<td>"+mailContent+"</td>"+"<td>"+Arrays.toString(colors)+"</td>"+"<td>"+Arrays.toString(articlecount)+"</td><td>"+coins+"</td><td>"+recorder+"</td>"+"<td>"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(recordtime)+"</td>";
	}
	
	@Override
	public String toString() {
		return "【发送物品】 [服务器:"+servername+"]--[角色名:"+playername+"]--[IP:"+ipaddress+"]"+"[物品名字："+Arrays.toString(articlenames)+"]"+"[物品数量："+Arrays.toString(articlecount)+"]"+"[邮件标题:"+mailTitle+"]"+"[邮件内容:"+mailContent+"]"+"[钱:"+coins+"]"+"--[操作人:"+recorder+"]"+"--[操作时间:"+TimeTool.formatter.varChar23.format(recordtime)+"]";
	}

}
