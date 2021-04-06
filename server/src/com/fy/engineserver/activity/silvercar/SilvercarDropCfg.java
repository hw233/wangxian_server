package com.fy.engineserver.activity.silvercar;

import java.util.Arrays;

import com.fy.engineserver.activity.CheckAttribute;

/**
 * 镖车被打爆时的配置
 * 
 */
@CheckAttribute("镖车被打爆时的配置")
public class SilvercarDropCfg {

	@CheckAttribute(value = "NPC模板ID")
	private int npcTempletId;
	@CheckAttribute(value = "NPC颜色")
	private int carColor;
	@CheckAttribute("掉落物品颜色")
	private int[] dropColor;
	@CheckAttribute("掉落物品 名字")
	private String[] dropName;
	@CheckAttribute("掉落物品几率")
	private double[] dropRate;

	public SilvercarDropCfg(int npcTempletId, int carColor, int[] dropColor, String[] dropName, double[] dropRate) {
		this.npcTempletId = npcTempletId;
		this.carColor = carColor;
		this.dropColor = dropColor;
		this.dropName = dropName;
		this.dropRate = dropRate;
	}

	public int getNpcTempletId() {
		return npcTempletId;
	}

	public void setNpcTempletId(int npcTempletId) {
		this.npcTempletId = npcTempletId;
	}

	public int getCarColor() {
		return carColor;
	}

	public void setCarColor(int carColor) {
		this.carColor = carColor;
	}

	public int[] getDropColor() {
		return dropColor;
	}

	public void setDropColor(int[] dropColor) {
		this.dropColor = dropColor;
	}

	public String[] getDropName() {
		return dropName;
	}

	public void setDropName(String[] dropName) {
		this.dropName = dropName;
	}

	public double[] getDropRate() {
		return dropRate;
	}

	public void setDropRate(double[] dropRate) {
		this.dropRate = dropRate;
	}

	@Override
	public String toString() {
		return "SilvercarDrop [npcTempletId=" + npcTempletId + ", carColor=" + carColor + ", dropColor=" + Arrays.toString(dropColor) + ", dropName=" + Arrays.toString(dropName) + ", dropRate=" + Arrays.toString(dropRate) + "]";
	}

}
