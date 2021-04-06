package com.fy.engineserver.economic.charge;

/**
 * 兑换比例
 * 针对不同的面额比例给予不同比例的银子
 * 
 * 
 * 
 */
public class ChargeRatio {

	public static final long DEFAULT_CHARGE_RATIO = 300000;// 默认的兑换比例
	/** 区间的最小值(闭) */
	private int min;
	/** 区间的最大值(开) */
	private int max;
	private double ratio;

	public ChargeRatio(int min, int max, double ratio) {
		super();
		this.min = min;
		this.max = max;
		this.ratio = ratio;
	}

	/**
	 * 玩家充值的钱数是否在这个区间.
	 * @param rmb单位元
	 * @return
	 */
	public boolean fitthisRatio(int rmb) {
		if (rmb <= 0) {
			throw new IllegalStateException("输入钱数异常:" + rmb);
		}
		if (min <= rmb && rmb < max) {
			return true;
		}
		return false;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	public static long getDefaultChargeRatio() {
		return DEFAULT_CHARGE_RATIO;
	}

	@Override
	public String toString() {
		return "ChargeRatio [min=" + min + ", max=" + max + ", ratio=" + ratio + "]";
	}
}
