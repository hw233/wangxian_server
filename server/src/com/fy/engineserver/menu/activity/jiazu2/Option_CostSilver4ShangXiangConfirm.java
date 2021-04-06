package com.fy.engineserver.menu.activity.jiazu2;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.jiazu2.instance.JiazuMember2;
import com.fy.engineserver.jiazu2.manager.JiazuEntityManager2;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.JIAZU_XIULIAN_RES;
import com.fy.engineserver.sprite.Player;

public class Option_CostSilver4ShangXiangConfirm extends Option{
	
	private int qifuLevel;
	
	
	@Override
	public void doSelect(Game game, Player player) {
		JiazuEntityManager2.instance.qifu(player, qifuLevel, true);
		JiazuMember2 jm2 = JiazuEntityManager2.instance.getEntity(player.getId());
		long gongzi = player.getWage();
		long xiulianZhi = jm2.getPracticeValue();
		int maxShangxiangNums = JiazuManager2.instance.qifuTimes.get(player.getVipLevel());
		int currentShangxiang = jm2.getQifuTimes();
		JIAZU_XIULIAN_RES resp = new JIAZU_XIULIAN_RES(GameMessageFactory.nextSequnceNum(), qifuLevel, gongzi, xiulianZhi, 
				maxShangxiangNums, currentShangxiang);
		player.addMessageToRightBag(resp);
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public int getQifuLevel() {
		return qifuLevel;
	}

	public void setQifuLevel(int qifuLevel) {
		this.qifuLevel = qifuLevel;
	}

}
