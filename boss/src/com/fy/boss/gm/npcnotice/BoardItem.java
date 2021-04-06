package com.fy.boss.gm.npcnotice;

import java.util.Date;

import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;
@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"noticetype"})
})
public class BoardItem {
	@SimpleId
	private long id;
	
	@SimpleVersion
	private int version;
	
	public String title;
	@SimpleColumn(length=3900)
	public String content;
	public int voteNormal;
	public int voteGood;
	public int voteBetter;
	public Date beginShowTime;
	public Date endShowTime;
	public String authorName;
	public String noticetype;
	public String servername;
	//
	public transient int[] score;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int[] getScore() {
		return score;
	}
	public void setScore(int[] score) {
		this.score = score;
	}
	
	public int getVoteNormal() {
		return voteNormal;
	}
	public void setVoteNormal(int voteNormal) {
		this.voteNormal = voteNormal;
	}
	public int getVoteGood() {
		return voteGood;
	}
	public void setVoteGood(int voteGood) {
		this.voteGood = voteGood;
	}
	public int getVoteBetter() {
		return voteBetter;
	}
	public void setVoteBetter(int voteBetter) {
		this.voteBetter = voteBetter;
	}
	public Date getBeginShowTime() {
		return beginShowTime;
	}
	public void setBeginShowTime(Date beginShowTime) {
		this.beginShowTime = beginShowTime;
	}
	public Date getEndShowTime() {
		return endShowTime;
	}
	public void setEndShowTime(Date endShowTime) {
		this.endShowTime = endShowTime;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getNoticetype() {
		return noticetype;
	}
	public void setNoticetype(String noticetype) {
		this.noticetype = noticetype;
	}
	public String getServername() {
		return servername;
	}
	public void setServername(String servername) {
		this.servername = servername;
	}
	@Override
	public String toString() {
		return "BoardItem [title=" + title + ", voteNormal=" + voteNormal
				+ ", voteGood=" + voteGood + ", voteBetter=" + voteBetter
				+ ", beginShowTime=" + beginShowTime + ", endShowTime="
				+ endShowTime + ", authorName=" + authorName + ", noticetype="
				+ noticetype + ", servername=" + servername + "]";
	}
	
	
}
