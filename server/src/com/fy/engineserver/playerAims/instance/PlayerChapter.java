package com.fy.engineserver.playerAims.instance;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class PlayerChapter {
	/** 章节积分 */
	private transient int score = -1;
	/** 章节名 */
	private String chapterName;
	/** 章节奖励领取状态 (默认为-1  0为可领取  1为普通领取   2为vip领取  3为普通+vip都已领取)*/
	private byte receiveType = -1;
	
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getChapterName() {
		return chapterName;
	}
	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}
	public byte getReceiveType() {
		return receiveType;
	}
	public void setReceiveType(byte receiveType) {
		this.receiveType = receiveType;
	}
	
	
}
