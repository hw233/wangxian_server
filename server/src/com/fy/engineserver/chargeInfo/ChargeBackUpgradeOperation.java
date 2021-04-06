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
public class ChargeBackUpgradeOperation extends ChargeBackOperation {


	/**
	 * 
	 */
	private static final long serialVersionUID = 381267458787856788L;
	
	/**
	 * 升级规则下，领取奖励的记录，
	 */
	public int[] takeRecord;
	
	/**
	 * 可以连续领取的等级
	 */
	public int LEVELS;
	
	/**
	 * 升级规则下，每级领取的金额
	 */
	public int takeMoneyAmountPerLevel;
	
	/**
	 * 领取次数
	 */
	public int takingTimes;
	
	/**
	 * 每次返还玩家的金额
	 */
	public int amount;

	/**
	 * @param playerId
	 */
	public ChargeBackUpgradeOperation(long playerId,int amount,int levels) {
		super(playerId);
		// TODO Auto-generated constructor stub
		this.amount=amount;
		this.LEVELS=levels;
		this.takeRecord=new int[this.LEVELS];
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
//ChargeBackManager.log.info("[充值返还] [升级规则] [添加成功] [玩家："
//+ playerName + "] [玩家ID："
//+ this.playerId + "] [金额：" + this.amount
//+ "]");
if(ChargeBackManager.log.isInfoEnabled())
	ChargeBackManager.log.info("[充值返还] [升级规则] [添加成功] [玩家：{}] [玩家ID：{}] [金额：{}]", new Object[]{playerName,this.playerId,this.amount});
		}
	}

	/* (non-Javadoc)
	 * @see com.fy.engineserver.chargeInfo.ChargeBackOperation#doOperation()
	 */
	@Override
	public void doOperation() {}

	/* (non-Javadoc)
	 * @see com.fy.engineserver.chargeInfo.ChargeBackOperation#isDisabled()
	 */
	@Override
	public boolean isDisabled() {
		// TODO Auto-generated method stub
		return this.takingTimes>=this.LEVELS;
	}
	
	public void setPlayerId(long playerId){
		this.playerId=playerId;
	}

}
