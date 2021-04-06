package com.fy.engineserver.soulpith.instance;

import com.fy.engineserver.soulpith.SoulPithConstant;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class PlayerSoulpithInfo {
	/** 元神类型 */
	private int soulType;
	/** 镶嵌的灵髓宝石 */
	private long[] piths = new long[SoulPithConstant.MAX_FILL_NUM];

	public PlayerSoulpithInfo() {}
	
	public PlayerSoulpithInfo(int soulType) {
		this.soulType = soulType;
	}
	
	public int getSoulType() {
		return soulType;
	}

	public void setSoulType(int soulType) {
		this.soulType = soulType;
	}

	public long[] getPiths() {
		return piths;
	}

	public void setPiths(long[] piths) {
		this.piths = piths;
	}
}
