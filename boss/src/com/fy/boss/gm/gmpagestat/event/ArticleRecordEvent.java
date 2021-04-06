package com.fy.boss.gm.gmpagestat.event;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import com.fy.boss.gm.gmpagestat.RecordEvent;
import com.fy.boss.gm.gmpagestat.EventForJson.EventType;
import com.xuanzhi.boss.game.GameConstants;

public class ArticleRecordEvent extends RecordEvent implements Serializable{

	public String articlenames[];
	
	public String articlenames_stat[];
	
	public int articlecount[];
	
	public int colors[];
	
	public long ids[];
	/**
	 * 关注的物品类型:
	 * 1:装备
	 * 2:一般道具
	 * 3:帖
	 * 4:酒
	 */
	public int articletype[];
	
	public ArticleRecordEvent(){}
	
	public ArticleRecordEvent(String username,long []ids,String []articlenames,String[]articlenames_stat,int []articlecount,int colors[],int articletype[],String playername,String ip,String recorder){
		this.articlenames = articlenames;
		this.articlecount = articlecount;
		this.colors = colors;
		this.articlenames_stat = articlenames_stat;
		this.articletype = articletype;
		this.ids = ids;
		setUsername(username);
		setPlayername(playername);
		setEventtype(EventType.发送物品);
		setIpaddress(ip);
		setRecorder(recorder);
		setRecordtime(System.currentTimeMillis());
		setServername(GameConstants.getInstance().getServerName());
	}
	
	public ArticleRecordEvent(String []articlenames,int []articlecount,int colors[],int articletype[],String playername,String ip,String recorder){
		this.articlenames = articlenames;
		this.articlecount = articlecount;
		this.colors = colors;
		this.articletype = articletype;
		setPlayername(playername);
		setEventtype(EventType.发送物品);
		setIpaddress(ip);
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

	public String[] getArticlenames_stat() {
		return articlenames_stat;
	}

	public void setArticlenames_stat(String[] articlenames_stat) {
		this.articlenames_stat = articlenames_stat;
	}

	public long[] getIds() {
		return ids;
	}

	public void setIds(long[] ids) {
		this.ids = ids;
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

	@Override
	public String toString() {
		return "<td>发送物品</td><td>"+servername+"</td><td>"+username+"</td><td>"+playername+"</td><td>"+pid+"</td><td>"+ipaddress+"</td>"+"<td>"+Arrays.toString(articlenames)+"</td>"+"<td>"+Arrays.toString(articlenames_stat)+"</td>"+"<td>"+Arrays.toString(colors)+"</td>"+"<td>"+Arrays.toString(articlecount)+"</td>"+"<td>"+recorder+"</td>"+"<td>"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(recordtime)+"</td>";
	}
	
}
