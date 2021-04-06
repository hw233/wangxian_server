package com.fy.engineserver.menu.activity.jiazu2;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.instance.BiaoCheEntity;
import com.fy.engineserver.jiazu2.manager.BiaocheEntityManager;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.jiazu2.model.BiaoCheQianghuaModel;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.JIAZU_BIAOCHE_QIANGHUA_INFO_RES;
import com.fy.engineserver.sprite.Player;

public class Option_Open_BiaocheQIanghua extends Option implements NeedCheckPurview{
	
	
	@Override
	public void doSelect(Game game, Player player) {
		String result = JiazuManager2.checkJiazu(player);
		if (result != null) {
			player.sendError(result);
			return;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		int[] maxLevels = new int[]{BiaocheEntityManager.镖车强化最大等级, BiaocheEntityManager.镖车强化最大等级};
		int[] currentLevels = new int[2];
		int[] maxProcess = new int[]{BiaocheEntityManager.强化最大进度, BiaocheEntityManager.强化最大进度};
		int[] process = new int[2];
		long[] costNums = new long[2];
		int[] needJiazuLevels = new int[2];
		String[] des1 = new String[]{"", ""};
		String[] des2 = new String[]{"", ""};
		BiaoCheQianghuaModel bqm = null;
		BiaoCheQianghuaModel nbqm = null;
		BiaoCheEntity entity = BiaocheEntityManager.instance.getEntity(jiazu.getJiazuID());
		for (int i=0; i<entity.getStrongerType().length; i++) {
			if (entity.getStrongerType()[i] == BiaocheEntityManager.强化血量) {
				currentLevels[0] = entity.getStrongerLevel()[i];
				process[0] = entity.getProcess()[i];
				if (currentLevels[0] >= BiaocheEntityManager.镖车强化最大等级) {
					process[0] = maxProcess[0];
				}
				bqm = JiazuManager2.instance.biaocheMap.get(BiaocheEntityManager.强化血量).get(currentLevels[0]);
				int addNums = bqm == null? 0 : bqm.getAddNum();
				des1[0] = String.format(Translate.镖车血量描述, addNums + "%");
				if (currentLevels[0] < BiaocheEntityManager.镖车强化最大等级) {
					nbqm = JiazuManager2.instance.biaocheMap.get(BiaocheEntityManager.强化血量).get(currentLevels[0] + 1);
					des2[0] = String.format(Translate.镖车血量描述, nbqm.getAddNum() + "%");
					needJiazuLevels[0] = nbqm.getNeedJiazuLevel();
					if (nbqm.getCostJiazuMoney() > 0) {
						costNums[0] = nbqm.getCostJiazuMoney() / BiaocheEntityManager.强化最大进度;
					} else {
						costNums[0] = nbqm.getCostLingmai() / BiaocheEntityManager.强化最大进度;
					}
				}
			} else if (entity.getStrongerType()[i] == BiaocheEntityManager.强化双防) {
				currentLevels[1] = entity.getStrongerLevel()[i];
				process[1] = entity.getProcess()[i];
				if (currentLevels[1] >= BiaocheEntityManager.镖车强化最大等级) {
					process[1] = maxProcess[1];
				}
				bqm = JiazuManager2.instance.biaocheMap.get(BiaocheEntityManager.强化双防).get(currentLevels[1]);
				int addNums = bqm == null? 0 : bqm.getAddNum();
				des1[1] = String.format(Translate.镖车双防描述, addNums/BiaocheEntityManager.infoShowNumRate + "%");
				if (currentLevels[1] < BiaocheEntityManager.镖车强化最大等级) {
					nbqm = JiazuManager2.instance.biaocheMap.get(BiaocheEntityManager.强化双防).get(currentLevels[1] + 1);
					des2[1] = String.format(Translate.镖车双防描述, nbqm.getAddNum()/BiaocheEntityManager.infoShowNumRate + "%");
					needJiazuLevels[1] = nbqm.getNeedJiazuLevel();
					if (nbqm.getCostJiazuMoney() > 0) {
						costNums[1] = nbqm.getCostJiazuMoney() / BiaocheEntityManager.强化最大进度;
					} else {
						costNums[1] = nbqm.getCostLingmai() / BiaocheEntityManager.强化最大进度;
					}
				}
			}
		}
		JIAZU_BIAOCHE_QIANGHUA_INFO_RES resp = new JIAZU_BIAOCHE_QIANGHUA_INFO_RES(GameMessageFactory.nextSequnceNum(), jiazu.getLevel(), jiazu.getJiazuMoney(), jiazu.getConstructionConsume(),
				maxLevels, currentLevels, maxProcess, process, 
				costNums, des1, des2, needJiazuLevels);
		player.addMessageToRightBag(resp);
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		// TODO Auto-generated method stub
		String result = JiazuManager2.checkJiazu(player);
		if (result != null) {
			return false;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu.getLevel() < 5) {
			return false;
		}
		return true;
	}


}
