package com.fy.engineserver.util.gmstat.event;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.gmstat.RecordEvent;
import com.fy.engineserver.util.gmstat.EventForJson.EventType;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 发送物品事件
 * 
 *
 */
public class ArticleRecordEvent extends RecordEvent{

	public String articlenames[];
	public String articlenames_stat[];
	
	public int articlecount[];
	
	public int colors[];
	
	/**
	 * 关注的物品类型:
	 * 1:装备
	 * 2:一般道具
	 * 3:帖
	 * 4:酒
	 */
	public int articletype[];
	
	public ArticleRecordEvent(){}
	
	public ArticleRecordEvent(String []articlenames,String []articlenames_stat,int []articlecount,int colors[],int articletype[],String username,String playername,long pid,String ip,String recorder){
		this.articlenames = articlenames;
		this.articlecount = articlecount;
		this.articlenames_stat = articlenames_stat;
		this.colors = colors;
		this.articletype = articletype;
		setPlayername(playername);
		setEventtype(EventType.发送物品);
		setIpaddress(ip);
		setUsername(username);
		setPid(pid);
		setRecorder(recorder);
		setRecordtime(System.currentTimeMillis());
		setServername(GameConstants.getInstance().getServerName());
	}
	
	public ArticleRecordEvent(String []articlenames,int []articlecount,String playername,String ip,String recorder){
		this.articlenames = articlenames;
		this.articlecount = articlecount;
		setPlayername(playername);
		setEventtype(EventType.发送物品);
		setIpaddress(ip);
		setRecorder(recorder);
		setRecordtime(System.currentTimeMillis());
		setServername(GameConstants.getInstance().getServerName());
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

	public String[] getArticlenames_stat() {
		return articlenames_stat;
	}

	public void setArticlenames_stat(String[] articlenames_stat) {
		this.articlenames_stat = articlenames_stat;
	}

	public String getHtmlStr(){
		return "<td>发送物品</td><td>"+servername+"</td><td>"+username+"</td><td>"+playername+"</td><td>"+pid+"</td><td>"+ipaddress+"</td>"+"<td>"+Arrays.toString(articlenames)+"</td>"+"<td>"+Arrays.toString(articlenames_stat)+"</td>"+"<td>"+Arrays.toString(colors)+"</td>"+"<td>"+Arrays.toString(articlecount)+"</td>"+"<td>"+recorder+"</td>"+"<td>"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(recordtime)+"</td>";
	}
	public String getHtmlStr2(){
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<articlenames.length;i++){
			sb.append("["+articlenames[i]+","+articlenames_stat[i]+","+colors[i]+","+articlecount[i]+"]");
		}
		return "<td>发送物品</td><td>"+servername+"</td><td>"+username+"</td><td>"+playername+"</td><td>"+pid+"</td><td>"+ipaddress+"</td>"+"<td>"+sb.toString()+"</td>"+"<td>"+recorder+"</td>"+"<td>"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(recordtime)+"</td>";
	}
	
	@Override
	public String toString() {
		return "【发送物品】 [服务器:"+servername+"]--[角色名:"+playername+"]--[IP:"+ipaddress+"]"+"[物品名字："+Arrays.toString(articlenames)+"]"+"[物品数量："+Arrays.toString(articlecount)+"]"+"[颜色："+Arrays.toString(colors)+"]"+"--[操作人:"+recorder+"]"+"--[操作时间:"+TimeTool.formatter.varChar23.format(recordtime)+"]";
	}
}
