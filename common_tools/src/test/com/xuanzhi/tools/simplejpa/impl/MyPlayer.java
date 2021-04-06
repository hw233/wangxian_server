package com.xuanzhi.tools.simplejpa.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.xuanzhi.tools.simplejpa.annotation.*;

@SimpleEntity(name="MyPlayer")
@SimpleIndices({
	@SimpleIndex(members={"username"}),
	@SimpleIndex(members={"playerName"}),
	@SimpleIndex(members={"levelOn2"}),
	@SimpleIndex(name="MyPlayer_xxxxx_idx",members={"level2","exp2","hp2","mp2"}),
})
public class MyPlayer extends AbstractPlayer{
	
	@SimpleId
	private long id2;
	
	@SimpleVersion
	private int version2;
	
	private String username;
	
	public int getMoney2() {
		return money2;
	}

	public void setMoney2(int money2) {
		this.money2 = money2;
	}

	public int getLevelOn2() {
		return levelOn2;
	}

	public void setLevelOn2(int levelOn2) {
		this.levelOn2 = levelOn2;
	}

	public String getAskldee() {
		return askldee;
	}

	public void setAskldee(String askldee) {
		this.askldee = askldee;
	}

	public String getNewAddColumn() {
		return newAddColumn;
	}

	public void setNewAddColumn(String newAddColumn) {
		this.newAddColumn = newAddColumn;
	}

	public ArrayList<String> getTestString() {
		return testString;
	}

	public void setTestString(ArrayList<String> testString) {
		this.testString = testString;
	}

	private String playerName;
	
	private String playerName1;
	
	private String playerName2;
	
	
	private String playerName3;
	
	private String playerName4;
	
	private String playerName5;
	
	private String playerName6;
	
	private String playerName7;
	
	
	private String playerName8;
	
	
	private String playerName9;
	
	private String playerName10;
	
	private String playerName11;
	
	private String playerName12;
	
	private String playerName13;
	
	private String playerName14;
	
	private String playerName15;
	
	private String playerName16;
	
	
	private int money2;
	
	private int levelOn2;
	
	private String askldee;
	
	private String newAddColumn;
	
	@SimpleColumn(name="playerLevel")
	private int level2;
	
	private int hp2;
	
	private int mp2 = 0;
	
	private long exp2;

	private byte[] avatar;
	
	@SimpleColumn(name="ARTICLEIDS",length=21000)
	private long[] articleIds2;
	
	@SimpleColumn(name="ARTICLE_MAPS",length=10000)
	private HashMap<Long,Long> map;
	
	protected transient List<Object> al2;
	
	protected transient ArrayList<MyPlayer> al;

	protected ArrayList<Knapsack> knapsacks;
	
	protected Knapsack common_knapsack;
	
	@SimpleColumn(length=10000)
	protected ArrayList<ArrayList<HashMap<Long,Knapsack>>> al3;
	
	protected Date lastRequestTime;
	
	protected java.sql.Date lastHeartBeatTime;
	
	@SimpleColumn(scale=2,saveInterval=30)
	protected double money;
	
	protected boolean isXXXX;

	@SimpleColumn(length=20000)
	ArrayList<String> testString = new ArrayList<String> ();
	
	public long getId2() {
		return id2;
	}

	public void setId2(long id2) {
		this.id2 = id2;
	}

	public int getVersion2() {
		return version2;
	}

	public void setVersion2(int version2) {
		this.version2 = version2;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getLevel2() {
		return level2;
	}

	public void setLevel2(int level2) {
		this.level2 = level2;
	}

	public int getHp2() {
		return hp2;
	}

	public void setHp2(int hp2) {
		this.hp2 = hp2;
	}

	public int getMp2() {
		return mp2;
	}

	public void setMp2(int mp2) {
		this.mp2 = mp2;
	}

	public long getExp2() {
		return exp2;
	}

	public void setExp2(long exp2) {
		this.exp2 = exp2;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

	public long[] getArticleIds2() {
		return articleIds2;
	}

	public void setArticleIds2(long[] articleIds2) {
		this.articleIds2 = articleIds2;
	}

	public HashMap<Long, Long> getMap() {
		return map;
	}

	public void setMap(HashMap<Long, Long> map) {
		this.map = map;
	}

	public List<Object> getAl2() {
		return al2;
	}

	public void setAl2(List<Object> al2) {
		this.al2 = al2;
	}

	public ArrayList<MyPlayer> getAl() {
		return al;
	}

	public void setAl(ArrayList<MyPlayer> al) {
		this.al = al;
	}

	public ArrayList<Knapsack> getKnapsacks() {
		return knapsacks;
	}

	public void setKnapsacks(ArrayList<Knapsack> knapsacks) {
		this.knapsacks = knapsacks;
	}

	public ArrayList<ArrayList<HashMap<Long, Knapsack>>> getAl3() {
		return al3;
	}

	public void setAl3(ArrayList<ArrayList<HashMap<Long, Knapsack>>> al3) {
		this.al3 = al3;
	}

	public Date getLastRequestTime() {
		return lastRequestTime;
	}

	public void setLastRequestTime(Date lastRequestTime) {
		this.lastRequestTime = lastRequestTime;
	}

	public java.sql.Date getLastHeartBeatTime() {
		return lastHeartBeatTime;
	}

	public void setLastHeartBeatTime(java.sql.Date lastHeartBeatTime) {
		this.lastHeartBeatTime = lastHeartBeatTime;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public boolean isXXXX() {
		return isXXXX;
	}

	public void setXXXX(boolean isXXXX) {
		this.isXXXX = isXXXX;
	}

	public Knapsack getCommon_knapsack() {
		return common_knapsack;
	}

	public void setCommon_knapsack(Knapsack common_knapsack) {
		this.common_knapsack = common_knapsack;
	}
	
	@SimplePostLoad
	public void postLdsfdsfoad(){
		System.out.println("======================== "+this.id2+" @SimplePostLoad =====================");
	}
	
	@SimplePrePersist
	public void afklajfdlkadsf(){
		System.out.println("======================== "+this.id2+" @SimplePrePersist =====================");
	}
	
	@SimplePostPersist
	public void affsaffsfd(){
		System.out.println("======================== "+this.id2+" @SimplePostPersist =====================");
	}
}
