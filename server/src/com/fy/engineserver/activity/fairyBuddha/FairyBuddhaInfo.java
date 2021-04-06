package com.fy.engineserver.activity.fairyBuddha;

import java.io.Serializable;
import java.util.Vector;

import com.fy.engineserver.datasource.language.Translate;

public class FairyBuddhaInfo implements Serializable {
	private long id; // 玩家id
	private String name; // 玩家角色名
	private byte career; // 玩家职业
	private int level; // 玩家等级
	private int country; // 玩家国家
	private String declaration; // 玩家宣言
	private int beWorshipped; // 仙尊被膜拜次数
	private int score; // 参选者的得票数
	private String lastChangeTime;// 若是参选者,此为最后一次被投票的时间;(若是仙尊,此为最后一次被膜拜时间//这个取消记录了,因为玩家既是仙尊又在投票榜里的时候,两个时间就无法区分了.)
	private Vector<Voter> voters;// 给其投票的人
	private Vector<Voter> worshippers;// 膜拜仙尊的人,每天清空

	private StatueForFairyBuddha statue;

	public FairyBuddhaInfo() {

	}

	public FairyBuddhaInfo(long id, String name, byte career, int level, int country, int beWorshipped, int score, String lastChangeTime, Vector<Voter> voters, Vector<Voter> worshippers, StatueForFairyBuddha statue) {
		this.id = id;
		this.name = name;
		this.career = career;
		this.level = level;
		this.country = country;
		this.declaration = Translate.仙尊默认宣言;
		this.beWorshipped = beWorshipped;
		this.score = score;
		this.lastChangeTime = lastChangeTime;
		this.voters = voters;
		this.worshippers = worshippers;
		this.statue = statue;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getCareer() {
		return career;
	}

	public void setCareer(byte career) {
		this.career = career;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public String getDeclaration() {
		return declaration;
	}

	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}

	public int getBeWorshipped() {
		return beWorshipped;
	}

	public void setBeWorshipped(int beWorshipped) {
		this.beWorshipped = beWorshipped;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getLastChangeTime() {
		return lastChangeTime;
	}

	public void setLastChangeTime(String lastChangeTime) {
		this.lastChangeTime = lastChangeTime;
	}

	public Vector<Voter> getVoters() {
		return voters;
	}

	public void setVoters(Vector<Voter> voters) {
		this.voters = voters;
	}

	public Vector<Voter> getWorshippers() {
		return worshippers;
	}

	public void setWorshippers(Vector<Voter> worshippers) {
		this.worshippers = worshippers;
	}

	public StatueForFairyBuddha getStatue() {
		return statue;
	}

	public void setStatue(StatueForFairyBuddha statue) {
		this.statue = statue;
	}

	public String getLogString() {
		return "{career:" + career + "}{id:" + id + "}{name:" + name + "}";
	}

}
