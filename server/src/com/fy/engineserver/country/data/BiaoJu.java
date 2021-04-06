package com.fy.engineserver.country.data;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 镖局
 * 每天12点到24点进行竞标，24点为结算点，给当天运营人结算，并且得出第二天运营人
 * 
 */
@SimpleEmbeddable
public class BiaoJu {

	/**
	 * 竞标成功后的竞标人id
	 */
	long playerId;

	/**
	 * 竞标成功后家族id
	 */
	long jiazuId;

	/**
	 * 当前镖局资金
	 */
	long currentMoney;

	/**
	 * 投保总违约金，当投保总违约金大于当前镖局资金时不让投保
	 */
	long toubaoweiyuejin;

	/**
	 * 竞标资金
	 */
	long jingbiaoMoney;

	/**
	 * 竞标人id
	 */
	long jingbiaoPlayerId;

	/**
	 * 竞标家族id，原则上当该家族为竞标最高时，不能再次输入竞标资金
	 */
	long jingbiaoJiazuId;

	public BiaoJu() {
		// TODO Auto-generated constructor stub
	}

	public long getCurrentMoney() {
		return currentMoney;
	}

	public void setCurrentMoney(long currentMoney) {
		this.currentMoney = currentMoney;
	}

	public long getJingbiaoMoney() {
		return jingbiaoMoney;
	}

	public void setJingbiaoMoney(long jingbiaoMoney) {
		this.jingbiaoMoney = jingbiaoMoney;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public long getJingbiaoPlayerId() {
		return jingbiaoPlayerId;
	}

	public void setJingbiaoPlayerId(long jingbiaoPlayerId) {
		this.jingbiaoPlayerId = jingbiaoPlayerId;
	}

	public long getToubaoweiyuejin() {
		return toubaoweiyuejin;
	}

	public void setToubaoweiyuejin(long toubaoweiyuejin) {
		this.toubaoweiyuejin = toubaoweiyuejin;
	}

	public long getJingbiaoJiazuId() {
		return jingbiaoJiazuId;
	}

	public void setJingbiaoJiazuId(long jingbiaoJiazuId) {
		this.jingbiaoJiazuId = jingbiaoJiazuId;
	}

	public long getJiazuId() {
		return jiazuId;
	}

	public void setJiazuId(long jiazuId) {
		this.jiazuId = jiazuId;
	}

}
