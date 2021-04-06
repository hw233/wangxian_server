package com.fy.engineserver.activity.loginActivity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.util.TimeTool;

public class VipLoginConfig {

	private long startTime;
	private long endTime;
	
	private Map<Integer,String> configs = new HashMap<Integer,String>();
	
	private Map<Integer,String> rewardNames = new HashMap<Integer,String>();
	private Map<Integer,Integer> rewardNums = new HashMap<Integer,Integer>();
	
	//活动是否开启
	public boolean isOpen(){
		return System.currentTimeMillis() >= startTime && System.currentTimeMillis() < endTime;
	}
	
	//获取相应的奖励名字
	public String getRewardName(int vipLevel){
		int level = vipLevel;
		if(level > rewardNames.size()){
			level = rewardNames.size();
		}
		if(level == 0){
			level = 1;
		}
		return rewardNames.get(level);
	}
	
	//获取相应的奖励数量
	public int getRewardNum(int vipLevel){
		int level = vipLevel;
		if(level > rewardNums.size()){
			level = rewardNums.size();
		}
		if(level == 0){
			level = 1;
		}
		return rewardNums.get(level);
	}
	
	public void init() throws Exception{
		for(Iterator<Integer> it = configs.keySet().iterator();it.hasNext();){
			Integer vip = it.next();
			String config[] = configs.get(vip).split("#");
			if(config[0] == null || config[0].isEmpty()){
				throw new Exception("奖励物品名字为空");
			}
			Article a = ArticleManager.getInstance().getArticle(config[0]);
			if(a == null){
				throw new Exception("奖励物品不存在");
			}
			if(config[1] == null || Integer.valueOf(config[1]) <= 0){
				throw new Exception("奖励物品数量不正确");
			}
			rewardNames.put(vip, config[0]);
			rewardNums.put(vip, Integer.valueOf(config[1]));
		}
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public Map<Integer, String> getConfigs() {
		return configs;
	}

	public void setConfigs(Map<Integer, String> configs) {
		this.configs = configs;
	}

	@Override
	public String toString() {
		return "[configs=" + configs + ", endTime=" + TimeTool.formatter.varChar23.format(endTime)
				+ ", rewardNames=" + rewardNames + ", rewardNums=" + rewardNums
				+ ", startTime=" + TimeTool.formatter.varChar23.format(startTime) + "]";
	}
	
}
