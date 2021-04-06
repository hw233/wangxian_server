package com.fy.engineserver.menu.fairyBuddha;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.fy.engineserver.activity.fairyBuddha.FairyBuddhaInfo;
import com.fy.engineserver.activity.fairyBuddha.FairyBuddhaManager;
import com.fy.engineserver.activity.fairyBuddha.Voter;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.FAIRY_VOTERECORD_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;

public class Option_voter_record extends Option implements NeedCheckPurview {
	private byte career;

	@Override
	public void doSelect(Game game, Player player) {
		if(UnitServerFunctionManager.needCloseFunctuin(Function.仙尊)) {
			player.sendError(Translate.合服功能关闭提示);
			return ;
		}
		FairyBuddhaManager.logger.warn("["+player.getLogString()+"] [仙尊] [查看投票记录] [查看者职业:"+player.getCurrSoul().getCareer()+"] [雕像职业:"+career+"]");
		FairyBuddhaManager fbm = FairyBuddhaManager.getInstance();
		Map<Byte, List<FairyBuddhaInfo>> electorMap = fbm.getCurrentElectorMap(fbm.getKey(0)+fbm.KEY_参选者);
		FairyBuddhaInfo fbi = fbm.getElectorById(player.getId(),player.getCurrSoul().getCareer(),electorMap);
		if (fbi != null) {
			Vector<Voter> voters = fbi.getVoters();
			if (voters != null && voters.size() > 0) {
				FAIRY_VOTERECORD_RES res = new FAIRY_VOTERECORD_RES(GameMessageFactory.nextSequnceNum(), voters.toArray(new Voter[0]));
				player.addMessageToRightBag(res);
			} else {
//				FAIRY_VOTERECORD_RES res = new FAIRY_VOTERECORD_RES(GameMessageFactory.nextSequnceNum(), null);
				player.sendError(Translate.查看记录无人投票);
			}
		}
	}

	@Override
	public boolean canSee(Player player) {
		FairyBuddhaManager fbm = FairyBuddhaManager.getInstance();
		FairyBuddhaManager.logger.warn("["+player.getLogString()+"] [仙尊] [查看投票记录] [是否为参选者:"+fbm.inElectors(player.getId(),player.getCurrSoul().getCareer())+"]");
		if(player.getCurrSoul().getCareer()==career&&fbm.inElectors(player.getId(),player.getCurrSoul().getCareer())){
			return true;
		}
		return false;
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public byte getCareer() {
		return career;
	}

	public void setCareer(byte career) {
		this.career = career;
	}

}
