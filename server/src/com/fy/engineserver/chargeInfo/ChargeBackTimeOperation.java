/**
 * 
 */
package com.fy.engineserver.chargeInfo;

import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;

/**
 * @author Administrator
 * 
 */
public class ChargeBackTimeOperation extends ChargeBackOperation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7724581766890900607L;

	/**
	 * 开始时间
	 */
	long startTime;

	/**
	 * 时间规则下，最后一次领取奖励的时间
	 */
	long lastTakeMoneyTime;

	/**
	 * 每次返还玩家的金额
	 */
	int amount;

	/**
	 * 目前玩家得到返还的总额
	 */
	int takingTotal;

	/**
	 * 玩家可得到的返还总额
	 */
	int TOTAL;

	final int[] DAYS = { 2, 5, 10, 15 };

	/**
	 * 返还记录
	 */
	int[] takingAmount;

	/**
	 * 领取次数
	 */
	int takingTimes;

	/**
	 * @param playerId
	 */
	public ChargeBackTimeOperation(int playerId, int amount) {
		super(playerId);
		// TODO Auto-generated constructor stub
		this.startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		this.amount = amount;
		this.TOTAL = amount * this.DAYS.length;
		this.takingAmount = new int[this.DAYS.length];
		Player player=null;
		String playerName="";
		try {
			player=PlayerManager.getInstance().getPlayer(this.playerId);
			playerName=player.getName();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ChargeBackManager.log.isInfoEnabled()) {
//ChargeBackManager.log.info("[充值返还] [时间规则] [添加成功] [玩家："
//+ playerName + "] [玩家ID："
//+ this.playerId + "] [金额：" + this.amount
//+ "] [应领取金额：" + this.TOTAL + "]");
if(ChargeBackManager.log.isInfoEnabled())
	ChargeBackManager.log.info("[充值返还] [时间规则] [添加成功] [玩家：{}] [玩家ID：{}] [金额：{}] [应领取金额：{}]", new Object[]{playerName,this.playerId,this.amount,this.TOTAL});
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fy.engineserver.chargeInfo.ChargeBackOperation#doOperation()
	 */
	@Override
	public void doOperation() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fy.engineserver.chargeInfo.ChargeBackOperation#isDisabled()
	 */
	@Override
	public boolean isDisabled() {
		// TODO Auto-generated method stub
		return this.takingTimes>=this.DAYS.length;
	}

}
