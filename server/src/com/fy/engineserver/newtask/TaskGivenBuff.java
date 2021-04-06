package com.fy.engineserver.newtask;

/**
 * 接任务给予的BUFF
 * 
 * 
 */
public class TaskGivenBuff {

	private String[] names;

	private int[] colors;

	private double[] rates;

	public TaskGivenBuff(String[] names, int[] colors, double[] rates) {
		this.names = names;
		this.colors = colors;
		this.rates = rates;
	}

	public String[] getNames() {
		return names;
	}

	public void setNames(String[] names) {
		this.names = names;
	}

	public int[] getColors() {
		return colors;
	}

	public void setColors(int[] colors) {
		this.colors = colors;
	}

	public double[] getRates() {
		return rates;
	}

	public void setRates(double[] rates) {
		this.rates = rates;
	}
}
