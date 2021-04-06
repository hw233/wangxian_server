package com.fy.engineserver.validate;

import com.fy.engineserver.sprite.Player;

public interface ValidateManager {

	public static int VALIDATE_STATE_通过 = 0;
	public static int VALIDATE_STATE_拒绝 = 1;
	public static int VALIDATE_STATE_需要答题 = 2;
	
	/**
	 * 获取验证状态,有三种:
	 * 
	 * 通过,直接可以接取任务
	 * 拒绝,不能接取任务
	 * 需要答题,需要答题正确后才能接取任务
	 * 
	 * 玩家不在线直接拒绝.
	 * 玩家所在IP如果完成了一定的次数后,需要答题.
	 * 玩家多次答题错误,也拒绝.
	 * 
	 * @param p
	 * @return
	 */
	public int getValidateState(Player p, int askType);
	
	/**
	 * 获取验证状态,有三种:
	 * 
	 * 通过,直接可以接取任务
	 * 拒绝,不能接取任务
	 * 需要答题,需要答题正确后才能接取任务
	 * 
	 * 玩家不在线直接拒绝.
	 * 玩家所在IP如果完成了一定的次数后,需要答题.
	 * 玩家多次答题错误,也拒绝.
	 * 
	 * 返回各种状态的原因.
	 * @param p
	 * @return
	 */
	public String getValidateStateReason(Player p, int askType);
	
	/**
	 * 此方法必须确保玩家需要答题的情况下调用.
	 * 否则返回null
	 * @param p
	 * @return
	 */
	public ValidateAsk getNextValidteAsk(Player p, int askType);
	
	/**
	 * 通知此玩家接取了一个体力任务
	 */
	public void notifyTakeOneTask(Player p);
	
	/**
	 * 通知此玩家快速出售了一次
	 * */
	public void notifyRequestBuySale(Player p);
	
	/**
	 * 通知此玩家答题了,对与错由ask判断
	 * 并返回答对了还是错了.
	 * true表示答对了
	 */
	public boolean notifyAnswerAsk(Player p,ValidateAsk ask,String inputStr, int asktype);
	
}
