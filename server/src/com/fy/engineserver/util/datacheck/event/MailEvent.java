package com.fy.engineserver.util.datacheck.event;

import java.util.Arrays;

import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.util.datacheck.SendHtmlToMail;

/**
 * 发送邮件事件
 * 
 * 
 */
public enum MailEvent{
	怪物检查("启动数据检查_怪物",new String[]{"id","统计名","所在地图","身上按钮"}),
	物品检查("启动数据检查_物品",new String[]{"物品id","统计名","价格","颜色"}),
	商店检查("启动数据检查_商品",new String[]{"商店名","统计名","颜色","出错原因","出错数值"}),
	;

	private MailEvent(String name,String[] mailTitle) {
		this.name = name;
		this.mailTitle = mailTitle;
	}
	
	public String[] getReceiver() {
		if (PlatformManager.getInstance().isPlatformOf(Platform.台湾)) {
			return new String[] { "wtx062@126.com"};
		}else if (PlatformManager.getInstance().isPlatformOf(Platform.官方)){
			return new String[] {"wtx062@126.com" };
		}
		return null;
	}
	
	public MailEvent 立即发送(String [][] content){
		type = 0;
		mailContent = content;
		return this;
	}
	public MailEvent 五分钟后发生(String [][] content){
		type = 5;
		mailContent = content;
		return this;
	}

	public String name;
	public SendHtmlToMail[] datas;
	public int type;
	//标题，内容，1对多
	public String[][] mailContent;
	public String[] mailTitle;
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		if(mailContent!=null && mailContent.length>0){
			for(int i=0;i<mailContent.length;i++){
				buffer.append(Arrays.toString(mailContent[i]));
			}
		}
		
		return "[name:"+name+"] [type:"+type+"] [mailContent:"+buffer.toString()+"] [tableTitle:"+Arrays.toString(mailTitle)+"]"; //[receiver:"+Arrays.toString(getReceiver())+"]";
	}
	
	public static void main(String[] args) {
		MailEvent event = MailEvent.怪物检查.立即发送(new String[][]{{"你好","你好","你好"},{"我好","我好","我好"},{"我1好","我1好","我1好"}});
		System.out.println(event.toString());
	}
	
}
