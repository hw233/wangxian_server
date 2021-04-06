/**
 * 
 */
package com.fy.engineserver.downcity.downcity2;

import java.util.Arrays;

/**
 * @author Administrator
 *
 */
public class MapFlop {
	
	private String mapName;
	private String articleName;
	private int articleColor;
	private int articleCount;
	private int monsterType;
	private int articleFlop;
	private int [] monsterArticleFlop;
	private int [] bossArticleFlop;
	private int limitFlopNum;
	private int limitFlopType;
	
	
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	public String getArticleName() {
		return articleName;
	}
	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}
	public int getArticleColor() {
		return articleColor;
	}
	public void setArticleColor(int articleColor) {
		this.articleColor = articleColor;
	}
	public int getArticleCount() {
		return articleCount;
	}
	public void setArticleCount(int articleCount) {
		this.articleCount = articleCount;
	}
	public int getMonsterType() {
		return monsterType;
	}
	public void setMonsterType(int monsterType) {
		this.monsterType = monsterType;
	}
	public int getArticleFlop() {
		return articleFlop;
	}
	public void setArticleFlop(int articleFlop) {
		this.articleFlop = articleFlop;
	}
	public int[] getMonsterArticleFlop() {
		return monsterArticleFlop;
	}
	public void setMonsterArticleFlop(int[] monsterArticleFlop) {
		this.monsterArticleFlop = monsterArticleFlop;
	}
	public int[] getBossArticleFlop() {
		return bossArticleFlop;
	}
	public void setBossArticleFlop(int[] bossArticleFlop) {
		this.bossArticleFlop = bossArticleFlop;
	}
	public int getLimitFlopNum() {
		return limitFlopNum;
	}
	public void setLimitFlopNum(int limitFlopNum) {
		this.limitFlopNum = limitFlopNum;
	}
	public int getLimitFlopType() {
		return limitFlopType;
	}
	public void setLimitFlopType(int limitFlopType) {
		this.limitFlopType = limitFlopType;
	}
	
	@Override
	public String toString() {
		return "MapFLop [articleColor=" + articleColor + ", articleCount="
				+ articleCount + ", articleFlop=" + articleFlop
				+ ", articleName=" + articleName 
				+ ", bossArticleFlop=" + Arrays.toString(bossArticleFlop)
				+ ", limitFlopNum=" + limitFlopNum + ", limitFlopType="
				+ limitFlopType + ", mapName=" + mapName
				+ ", monsterArticleFlop=" + Arrays.toString(monsterArticleFlop)
				+ ", monsterType=" + monsterType + "]";
	}
	
}
