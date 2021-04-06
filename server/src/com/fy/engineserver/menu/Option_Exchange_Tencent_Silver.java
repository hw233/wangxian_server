package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.economic.charge.ChargeManager;
import com.fy.engineserver.sprite.Player;
import com.fy.boss.client.BossClientService;

public class Option_Exchange_Tencent_Silver extends Option {

	String orderid;
	int golds;
	String openkey = "";
	String pf = "";
	String pay_token = "";
	String zondid = "";
	String pfkey = "";
	String channel = "";
	String stats = "";
	String statdescString = "";
	String chargeType = "";
	
	public Option_Exchange_Tencent_Silver(String orderid,int golds){
		this.orderid = orderid;
		this.golds = golds;
	}
	
	public void setArgs(String openkey,String pf,String pay_token,String zondid,String pfkey,String channel,String stats,String statdescString){
		this.openkey = openkey;
		this.pf = pf;
		this.pay_token = pay_token;
		this.zondid = zondid;
		this.pfkey = pfkey;
		this.channel = channel;
		this.stats = stats;
		this.statdescString = statdescString;
	}
	
	public String getChargeType() {
		return this.chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	@Override
	public void doSelect(Game game, Player player) {
		try {
			String newArgs [] = new String[5];
			newArgs[0] = player.getUsername();
			newArgs[1] = "";
			newArgs[2] = pf;
			newArgs[3] = openkey;
			newArgs[4] = getChargeType();//MSDK充值,MSDK兑换
			
			BossClientService.getInstance().notifyOrderStatusChanged(player.getUsername(), orderid, channel, stats, statdescString, zondid, pfkey, pay_token, newArgs);
			
//			String servername = GameConstants.getInstance().getServerName();
//			BossClientService.getInstance().savingForQQUser(player.getUsername(), golds, "MSDK兑换", servername);
			if (ChargeManager.logger.isInfoEnabled()) {
				ChargeManager.logger.info("[MSDK全部兑换游戏币,更新订单状态] [成功] [MSDK更新充值金额] [msdk查询金额:"+golds+"] ["+player.getLogString()+"] ["+toString()+"]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (ChargeManager.logger.isInfoEnabled()) {
				ChargeManager.logger.info("[MSDK全部兑换游戏币,更新订单状态] [异常] [MSDK更新充值金额] [msdk查询金额:"+golds+"] ["+player.getLogString()+"] ["+toString()+"] ["+e+"]");
			}
		}
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public String toString() {
		return "Option_Exchange_Tencent_Silver [orderid=" + this.orderid + ", golds=" + this.golds + ", openkey=" + this.openkey + ", pf=" + this.pf + ", pay_token=" + this.pay_token + ", zondid=" + this.zondid + ", pfkey=" + this.pfkey + ", channel=" + this.channel + ", stats=" + this.stats + ", statdescString=" + this.statdescString + "]";
	}

	public String getOrderid() {
		return this.orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public int getGolds() {
		return this.golds;
	}

	public void setGolds(int golds) {
		this.golds = golds;
	}

	public String getOpenkey() {
		return this.openkey;
	}

	public void setOpenkey(String openkey) {
		this.openkey = openkey;
	}

	public String getPf() {
		return this.pf;
	}

	public void setPf(String pf) {
		this.pf = pf;
	}

	public String getPay_token() {
		return this.pay_token;
	}

	public void setPay_token(String pay_token) {
		this.pay_token = pay_token;
	}

	public String getZondid() {
		return this.zondid;
	}

	public void setZondid(String zondid) {
		this.zondid = zondid;
	}

	public String getPfkey() {
		return this.pfkey;
	}

	public void setPfkey(String pfkey) {
		this.pfkey = pfkey;
	}

	public String getChannel() {
		return this.channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getStats() {
		return this.stats;
	}

	public void setStats(String stats) {
		this.stats = stats;
	}

	public String getStatdescString() {
		return this.statdescString;
	}

	public void setStatdescString(String statdescString) {
		this.statdescString = statdescString;
	}
	
}
