package com.fy.engineserver.menu.activity.jiazu2;

import java.util.Iterator;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.instance.JiazuMember2;
import com.fy.engineserver.jiazu2.manager.JiazuEntityManager2;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.jiazu2.model.QifuModel;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.JIAZU_XIULIAN_INFO_RES;
import com.fy.engineserver.sprite.Player;

public class Option_Open_Shangxiang extends Option {
	
	@Override
	public void doSelect(Game game, Player player) {
		String result = JiazuManager2.checkJiazu(player);
		if (result != null) {
			player.sendError(result);
			return;
		}
//		JiazuMember jm = JiazuManager.getInstance().getJiazuMember(player.getId(), player.getJiazuId());
		JiazuMember2 jm2 = JiazuEntityManager2.instance.getEntity(player.getId());
		jm2.reset();
		long gongzi = player.getWage();
		long xiulianZhi = jm2.getPracticeValue();
		int maxShangxiangNums = JiazuManager2.instance.qifuTimes.get(player.getVipLevel());
		int currentShangxiang = jm2.getQifuTimes();
		int[] shangxiangId = new int[JiazuManager2.instance.qifuMaps.size()];
		int[] costType = new int[JiazuManager2.instance.qifuMaps.size()];
		int[] rewardXiulian = new int[JiazuManager2.instance.qifuMaps.size()];
		long[] costNums = new long[JiazuManager2.instance.qifuMaps.size()];
		String[] description = new String[shangxiangId.length]; 
		
		Iterator<Integer> ite = JiazuManager2.instance.qifuMaps.keySet().iterator();
		int index = 0;
		while(ite.hasNext()) {
			int key = ite.next();
			QifuModel qm = JiazuManager2.instance.qifuMaps.get(key);
			shangxiangId[index] = key;
			costType[index] = qm.getCostType();
			rewardXiulian[index] = qm.getAddNum();
			costNums[index] = qm.getCostNum();
			String dess = BillingCenter.得到带单位的银两(qm.getAddJiazuZiyuan());
			description[index] = String.format(Translate.增加修炼值, qm.getAddNum() + "", dess);
			index++;
		}
		
		JIAZU_XIULIAN_INFO_RES resp = new JIAZU_XIULIAN_INFO_RES(GameMessageFactory.nextSequnceNum(), gongzi, xiulianZhi, 
				maxShangxiangNums, currentShangxiang, shangxiangId, description, costType, 
				costNums, rewardXiulian);
		player.addMessageToRightBag(resp);
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
