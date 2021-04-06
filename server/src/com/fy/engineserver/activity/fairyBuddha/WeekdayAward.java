package com.fy.engineserver.activity.fairyBuddha;

import java.util.Arrays;

import com.fy.engineserver.activity.shop.ActivityProp;

public class WeekdayAward {
	private byte dayOfWeek; // 一周的第几天
	private double expMul; // 膜拜奖励经验倍数
	private double silverMul;// 膜拜奖励绑银倍数
	private ActivityProp[] props;// 膜拜奖励道具

	public WeekdayAward(byte dayOfWeek, double expMul, double silverMul) {
		this.dayOfWeek = dayOfWeek;
		this.expMul = expMul;
		this.silverMul = silverMul;
	}

	public WeekdayAward(byte dayOfWeek, double expMul, double silverMul, ActivityProp[] props) {
		this.dayOfWeek = dayOfWeek;
		this.expMul = expMul;
		this.silverMul = silverMul;
		this.props = props;
	}

	public byte getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(byte dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public double getExpMul() {
		return expMul;
	}

	public void setExpMul(double expMul) {
		this.expMul = expMul;
	}

	public double getSilverMul() {
		return silverMul;
	}

	public void setSilverMul(double silverMul) {
		this.silverMul = silverMul;
	}

	public ActivityProp[] getProps() {
		return props;
	}

	public void setProps(ActivityProp[] props) {
		this.props = props;
	}

	@Override
	public String toString() {
		return "WeekdayAward [dayOfWeek=" + dayOfWeek + ", expMul=" + expMul + ", props=" + Arrays.toString(props) + ", silverMul=" + silverMul + "]";
	}

}
