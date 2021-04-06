package com.fy.engineserver.activity.fairyBuddha;

import java.util.Arrays;

import com.fy.engineserver.util.config.ServerFit4Activity;

public class ThankAward {
	private String startTime;
	private String endTime;
	private ServerFit4Activity sf4a;
	private String[] articleCNNames;
	private long[] price;

	public ThankAward(String startTime, String endTime, ServerFit4Activity sf4a, String[] articleCNNames, long[] price) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.sf4a = sf4a;
		this.articleCNNames = articleCNNames;
		this.price = price;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public ServerFit4Activity getSf4a() {
		return sf4a;
	}

	public void setSf4a(ServerFit4Activity sf4a) {
		this.sf4a = sf4a;
	}

	public String[] getArticleCNNames() {
		return articleCNNames;
	}

	public void setArticleCNNames(String[] articleCNNames) {
		this.articleCNNames = articleCNNames;
	}

	public long[] getPrice() {
		return price;
	}

	public void setPrice(long[] price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "ThankAward [articleCNNames=" + Arrays.toString(articleCNNames) + ", endTime=" + endTime + ", price=" + Arrays.toString(price) + ", sf4a=" + sf4a + ", startTime=" + startTime + "]";
	}

}
