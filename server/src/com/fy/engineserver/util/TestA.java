package com.fy.engineserver.util;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;

/**
 * 
 * @version 创建时间：Mar 22, 2011 6:57:54 PM
 * 
 */
public class TestA extends Test {
	private String good = "";

	public String getGood() {
		return good + "___A";
	}

	public void setGood(String good) {
		this.good = good;
	}
	
	public static void main(String args[]) {
		String line = "[INFO] 2013-03-29 00:01:11,851 [pool-3-thread-54900] - [用户消费] [成功] [绑定银子] [货币类型:绑定银子] [username:18620339473] [角色ID:1223000000000021056] [pname:风月飘凌] [服务器:千娇百媚] [消费金额:500] [消费渠道:商店购买] [地图名:wujibingyuansiceng] [角色等级:70] [账户变化:1929288 -> 1928788] [] [0]";
		String regex = ".*\\[username:(.*?)\\].*\\[pname:(.*?)\\] \\[.*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(line);
		if(m.find()) {
			String username = m.group(1);
			String pname = m.group(2);
			System.out.println("[username:"+username+"] [pname:"+pname+"]");
		}
	}
}
