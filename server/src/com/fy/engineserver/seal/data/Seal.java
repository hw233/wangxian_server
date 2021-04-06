package com.fy.engineserver.seal.data;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.util.TimeTool;

public class Seal {

	/**
	 * 封印等级
	 */
	public int sealLevel;
	
	/**
	 * 封印阶段
	 */
	public int sealStep;
	
	/**
	 * 解除封印最早时间
	 */
	public long sealCanOpenTime;
	
	/**
	 * 各个国家第一个到达封印等级的玩家id
	 */
	public long[] firstPlayerIdInCountry = new long[3];

	public int getSealLevel() {
		return sealLevel;
	}

	public void setSealLevel(int sealLevel) {
		this.sealLevel = sealLevel;
	}

	public long getSealCanOpenTime() {
		return sealCanOpenTime;
	}

	public void setSealCanOpenTime(long sealCanOpenTime,String reason) {
		this.sealCanOpenTime = sealCanOpenTime;
		Game.logger.warn("[设置封印开启时间] [reason:"+reason+"] [sealCanOpenTime:"+sealCanOpenTime+"] ["+TimeTool.formatter.varChar23.format(sealCanOpenTime)+"]");
	}

	public long[] getFirstPlayerIdInCountry() {
		return firstPlayerIdInCountry;
	}

	public void setFirstPlayerIdInCountry(long[] firstPlayerIdInCountry) {
		this.firstPlayerIdInCountry = firstPlayerIdInCountry;
	}

	public int getSealStep() {
		return sealStep;
	}

	public void setSealStep(int sealStep) {
		this.sealStep = sealStep;
	}
	
}
