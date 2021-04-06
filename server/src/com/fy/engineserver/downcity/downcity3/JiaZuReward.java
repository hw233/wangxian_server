package com.fy.engineserver.downcity.downcity3;

import java.util.Arrays;

public class JiaZuReward {

	private int rank[];
	private int lingMai[];
	private int ziJin[];
	
	public int[] getRank() {
		return rank;
	}

	public void setRank(int[] rank) {
		this.rank = rank;
	}

	public int[] getLingMai() {
		return lingMai;
	}

	public void setLingMai(int[] lingMai) {
		this.lingMai = lingMai;
	}

	public int[] getZiJin() {
		return ziJin;
	}

	public void setZiJin(int[] ziJin) {
		this.ziJin = ziJin;
	}

	@Override
	public String toString() {
		return "[lingMai=" + Arrays.toString(lingMai) + ", rank="
				+ Arrays.toString(rank) + ", ziJin=" + Arrays.toString(ziJin)
				+ "]";
	}

	
}
