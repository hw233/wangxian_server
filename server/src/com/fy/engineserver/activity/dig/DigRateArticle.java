package com.fy.engineserver.activity.dig;

public class DigRateArticle {
	/**随机产出的道具名*/
	private String rateName;
	/**统计名*/
	private String rateNameStat;
	/**颜色*/
	private int color;
	/**数量*/
	private int num;
	/**几率*/
	private int rate;
	/**是否绑定*/
	private boolean bind;
	public DigRateArticle(String rateName, String rateNameStat, int color, int num, int rate, boolean bind) {
		this.rateName = rateName;
		this.rateNameStat = rateNameStat;
		this.color = color;
		this.num = num;
		this.rate = rate;
		this.bind = bind;
	}
	public String getRateName() {
		return rateName;
	}
	public void setRateName(String rateName) {
		this.rateName = rateName;
	}
	public String getRateNameStat() {
		return rateNameStat;
	}
	public void setRateNameStat(String rateNameStat) {
		this.rateNameStat = rateNameStat;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public boolean getBind() {
		return bind;
	}
	public void setBind(boolean bind) {
		this.bind = bind;
	}
	@Override
	public String toString() {
		return "DigRateArticle [bind=" + bind + ", color=" + color + ", num=" + num + ", rate=" + rate + ", rateName=" + rateName + ", rateNameStat=" + rateNameStat + "]";
	}
	
	
}
