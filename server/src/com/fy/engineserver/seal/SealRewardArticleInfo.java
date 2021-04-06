package com.fy.engineserver.seal;

public class SealRewardArticleInfo {

	public String name;
	public int color;
	public boolean isbind;
	
	public  SealRewardArticleInfo(){}
	public  SealRewardArticleInfo(String name,int color,boolean isbind){
		this.name = name;
		this.color = color;
		this.isbind = isbind;
	}
	@Override
	public String toString() {
		return "SealRewardArticleInfo [name=" + this.name + ", color=" + this.color + ", isbind=" + this.isbind + "]";
	}
	
}
