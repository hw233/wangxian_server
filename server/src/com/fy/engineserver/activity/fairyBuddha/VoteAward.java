package com.fy.engineserver.activity.fairyBuddha;

import java.util.Arrays;

import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.util.config.ServerFit4Activity;

public class VoteAward {
	private String startTime;
	private String endTime;
	private ServerFit4Activity sf4a;
	private ActivityProp[] props;

	public VoteAward(String startTime, String endTime, ServerFit4Activity sf4a, ActivityProp[] props) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.sf4a = sf4a;
		this.props = props;
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

	public ActivityProp[] getProps() {
		return props;
	}

	public void setProps(ActivityProp[] props) {
		this.props = props;
	}

	@Override
	public String toString() {
		return "VoteAward [endTime=" + endTime + ", props=" + Arrays.toString(props) + ", sf4a=" + sf4a + ", startTime=" + startTime + "]";
	}

}
