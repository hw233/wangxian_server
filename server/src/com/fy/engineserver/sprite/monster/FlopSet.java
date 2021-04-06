package com.fy.engineserver.sprite.monster;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.util.ProbabilityUtils;
import com.fy.engineserver.util.TimeSlice;

/**
 * 掉落集合，
 * 每个集合对应一个掉率
 * 同时每个集合中的物品，都是随机掉落
 * 
 *
 */
public class FlopSet {
	public final static int DROP_TIME_LIMIT_NONE = 0;
	public final static int DROP_TIME_LIMIT_DAILY = 1;
	public final static int DROP_TIME_LIMIT_WEEKLY = 2;

	/**
	 * 掉落类型
	 * 0代表单个模式，一个物品只掉落一个，组队的玩家都可以捡。一个玩家捡了，其他玩家就没有了
	 * 1代表多个模式，一个物品掉落多个，组队的玩家都可以捡。每一个组队的人都有一份，大家可以互相捡自己的
	 */
	private byte flopType;

	/**
	 * 掉落方式，0为按照几率，独立计算掉落。1为从集合中选出一个集合
	 */
	public byte flopFormat;
	
	/**
	 * 掉落物品颜色
	 */
	public int color;
	
	public byte getFlopType() {
		return flopType;
	}

	public void setFlopType(byte flopType) {
		this.flopType = flopType;
	}

	public static Random random = new Random(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
	
	int dropTimeLimitType = DROP_TIME_LIMIT_NONE;
	
	public TimeSlice getTimeSlice() {
		return timeSlice;
	}

	public void setTimeSlice(TimeSlice timeSlice) {
		this.timeSlice = timeSlice;
	}

	public int getDropTimeLimitType() {
		return dropTimeLimitType;
	}

	public void setDropTimeLimitType(int dropTimeLimitType) {
		this.dropTimeLimitType = dropTimeLimitType;
	}

	/**
	 * 掉率
	 * 当掉落方式为0既为独立掉落时，基数为10000
	 * 当掉落方式为1既为从集合列表中选择一个，基数为所有数值的和
	 */
	public int floprates[];
	
	public String articles[];
	
	public TimeSlice timeSlice;
	
	public Article[] randomNext(){
		ArrayList<Article> al = new ArrayList<Article>();
		if(articles == null || articles.length == 0) return al.toArray(new Article[0]);
		ArticleManager am = ArticleManager.getInstance();
		if(flopFormat == 0){
			for(int i = 0 ; i < articles.length ; i++){
				Article fs = am.getArticle(articles[i]);
				double floprate = floprates[i]/MemoryMonsterManager.分母;
				if(ProbabilityUtils.randomProbability(random,floprate)){ //随机掉
					if(fs != null){
						al.add(fs);
					}
				}
			}
		}else{
			double[] probss = new double[floprates.length];
			int 分母 = 0;
			for(int i = 0; i < floprates.length; i++){
				分母 += floprates[i];
			}
			for(int i = 0; i < floprates.length; i++){
				probss[i] = floprates[i]*1d/分母;
			}
			int index = ProbabilityUtils.randomProbability(random,probss);
			Article fs = am.getArticle(articles[index]);
			if(fs != null){
				al.add(fs);
			}
		}
		return al.toArray(new Article[0]);
	}
	
	public boolean isWithinTimeLimit(){
		if(timeSlice == null){
			return true;
		}
		
		return timeSlice.isValid(new Date());
	}
	
	public String getTimeLimitAsString(){
		if(dropTimeLimitType == DROP_TIME_LIMIT_NONE){
			return Translate.text_5745;
		}else{
			return timeSlice +"";
		}
	}

	public byte getFlopFormat() {
		return flopFormat;
	}

	public void setFlopFormat(byte flopFormat) {
		this.flopFormat = flopFormat;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int[] getFloprates() {
		return floprates;
	}

	public void setFloprates(int[] floprates) {
		this.floprates = floprates;
	}

	public String[] getArticles() {
		return articles;
	}

	public void setArticles(String[] articles) {
		this.articles = articles;
	}
}
