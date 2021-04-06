package com.fy.engineserver.menu.horse;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.horseInlay.HorseEquInlayEntityManager;
import com.fy.engineserver.datasource.article.data.horseInlay.HorseEquInlayManager;
import com.fy.engineserver.datasource.article.data.horseInlay.instance.HorseEquInlay;
import com.fy.engineserver.datasource.article.data.horseInlay.module.InlayModule;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.ACTIVITY_HORSEEQU_INLAY_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;

public class Option_ConfirmReplaceHorseEquStrong extends Option{
	
	private long eeId;
	
	private int index;
	
	private int opt;
	
	private long[] costIds1;
	
	private int[] costNums2;
	
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {//
		// TODO Auto-generated method stub
		String[] needArticles = new String[HorseEquInlayManager.MAX_INLAY_NUM];
		int[] haveNums = new int[HorseEquInlayManager.MAX_INLAY_NUM];
		long horseEquId = eeId;
		ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(horseEquId);
		int inlayIndex = index;
		int inlayColor = -1;
		if (ae instanceof EquipmentEntity) {
			String result = HorseEquInlayEntityManager.getInst().punch(player, horseEquId, inlayIndex, opt, costIds1, costNums2, true);
			if (result == null) {
				HorseEquInlay entity = HorseEquInlayEntityManager.getInst().getEntity((EquipmentEntity) ae);
				if (entity != null) {
					inlayColor = entity.getInlayColorType()[inlayIndex];
				}
			} else {
				player.sendError(result);
			}
		}
		Knapsack bag = player.getKnapsack_common();
		for (int i=0; i<needArticles.length; i++) {
			InlayModule module = HorseEquInlayManager.getInst().inlayMap.get(i);
			String[] tempStr = module.getCostByType(HorseEquInlayEntityManager.开孔);
			needArticles[i] = tempStr[0];
			haveNums[i] = bag.countArticle(needArticles[i]);
		}
		ACTIVITY_HORSEEQU_INLAY_RES resp = new ACTIVITY_HORSEEQU_INLAY_RES(GameMessageFactory.nextSequnceNum(), horseEquId, inlayIndex, inlayColor, needArticles, haveNums);
		player.addMessageToRightBag(resp);
	}

	public long getEeId() {
		return eeId;
	}

	public void setEeId(long eeId) {
		this.eeId = eeId;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getOpt() {
		return opt;
	}

	public void setOpt(int opt) {
		this.opt = opt;
	}

	public long[] getCostIds1() {
		return costIds1;
	}

	public void setCostIds1(long[] costIds1) {
		this.costIds1 = costIds1;
	}

	public int[] getCostNums2() {
		return costNums2;
	}

	public void setCostNums2(int[] costNums2) {
		this.costNums2 = costNums2;
	}

}
