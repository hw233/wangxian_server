package com.fy.engineserver.menu.wafen;

import com.fy.engineserver.activity.wafen.instacne.WaFenInstance4Private;
import com.fy.engineserver.activity.wafen.manager.WaFenManager;
import com.fy.engineserver.activity.wafen.model.FenMuModel;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.DIG_FENMU_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;

public class Option_UseChanziType extends Option{
	
	private byte chanziType;
	
	private String fenmuName;
	
	private int fendiIndex;
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		try {
			long id = WaFenManager.instance.waFen(player, fenmuName, fendiIndex, chanziType);
			WaFenInstance4Private wp = WaFenManager.instance.privateMaps.get(player.getId());
			FenMuModel fmm = WaFenManager.instance.fenmuMap.get(fenmuName);
			int fenmuIndex = fmm.getId() - 1;
			byte receiveType = (byte) (fmm.getShareType() != 0 ? 2 : 1);
			DIG_FENMU_RES resp = new DIG_FENMU_RES(GameMessageFactory.nextSequnceNum(), fenmuName, fenmuIndex, fendiIndex, receiveType, id, chanziType,
					wp.getLeftYinChanzi(), wp.getLeftJinChanzi(), wp.getLeftLiuLiChanzi());
			player.addMessageToRightBag(resp);
		} catch (Exception e) {
			WaFenManager.logger.error("[挖坟活动] [挖坟] [异常] [" + player.getLogString() + "] [fenmuName:" + fenmuName + "] [fendiIndex:" + fendiIndex + "] [chanziType:" + chanziType  + "]", e);
		} 
	}
	public int getFendiIndex() {
		return fendiIndex;
	}
	public void setFendiIndex(int fendiIndex) {
		this.fendiIndex = fendiIndex;
	}
	public String getFenmuName() {
		return fenmuName;
	}
	public void setFenmuName(String fenmuName) {
		this.fenmuName = fenmuName;
	}
	public byte getChanziType() {
		return chanziType;
	}
	public void setChanziType(byte chanziType) {
		this.chanziType = chanziType;
	}
}
