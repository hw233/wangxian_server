package com.fy.engineserver.activity.farming;

import java.util.Arrays;

import com.fy.engineserver.util.RandomTool;
import com.fy.engineserver.util.RandomTool.RandomType;

/**
 * 弹出的盘子方案
 * 
 */
public class PlateProject {
	private int id;
	private int[] fruitIds = new int[FarmingManager.maxFruitNum];
	private double[] fruitRate = new double[FarmingManager.maxFruitNum];

	private String[] fruitNames = new String[FarmingManager.maxFruitNum];

	public PlateProject(int id, int[] fruitIds, double[] fruitRate) {
		this.id = id;
		this.fruitIds = fruitIds;
		this.fruitRate = fruitRate;
	}

	public int getOnceResultIndex() {
		return RandomTool.getResultIndexs(RandomType.groupRandom, fruitRate, 1).get(0);
	}

	public void initNames() {
		for (int i = 0; i < fruitIds.length; i++) {
			fruitNames[i] = FarmingManager.getInstance().getFruits().get(fruitIds[i]).getName();
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int[] getFruitIds() {
		return fruitIds;
	}

	public void setFruitIds(int[] fruitIds) {
		this.fruitIds = fruitIds;
	}

	public double[] getFruitRate() {
		return fruitRate;
	}

	public void setFruitRate(double[] fruitRate) {
		this.fruitRate = fruitRate;
	}

	public String[] getFruitNames() {
		return fruitNames;
	}

	public void setFruitNames(String[] fruitNames) {
		this.fruitNames = fruitNames;
	}

	@Override
	public String toString() {
		return "PlateProject [id=" + id + ", fruitIds=" + Arrays.toString(fruitIds) + ", fruitRate=" + Arrays.toString(fruitRate) + "]";
	}
}
