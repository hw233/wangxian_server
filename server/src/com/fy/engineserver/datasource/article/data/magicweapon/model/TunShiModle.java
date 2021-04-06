package com.fy.engineserver.datasource.article.data.magicweapon.model;
/**
 * 吞噬动画
 */
public class TunShiModle {
	
	public long startNums;
	public long endNums;
	public long upgradEexps;
	
	public long getStartNums() {
		return startNums;
	}

	public void setStartNums(long startNums) {
		this.startNums = startNums;
	}

	public long getEndNums() {
		return endNums;
	}

	public void setEndNums(long endNums) {
		this.endNums = endNums;
	}

	public long getUpgradEexps() {
		return upgradEexps;
	}

	public void setUpgradEexps(long upgradEexps) {
		this.upgradEexps = upgradEexps;
	}

	@Override
	public String toString() {
		return "TunShiModle [startNums=" + startNums + ", endNums=" + endNums + ", upgradEexps=" + upgradEexps + "]";
	}
}
