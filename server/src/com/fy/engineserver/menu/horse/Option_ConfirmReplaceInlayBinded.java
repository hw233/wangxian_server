package com.fy.engineserver.menu.horse;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.horseInlay.HorseEquInlayEntityManager;
import com.fy.engineserver.datasource.article.data.horseInlay.instance.HorseEquInlay;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.INLAYTAKE_HORSEBAOSHI_RES;
import com.fy.engineserver.sprite.Player;

public class Option_ConfirmReplaceInlayBinded extends Option{
	
	private long horseEquId;
	
	private int inlayIndex;
	
	private long baoshiId;
	
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {//
		// TODO Auto-generated method stub
		ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(horseEquId);
		long shenxiaId = baoshiId;
		long inlayId = -1;
		if (ae instanceof EquipmentEntity) {
			String result = Translate.服务器出现错误;
			if (shenxiaId > 0) {
				result = HorseEquInlayEntityManager.getInst().inlay(player, horseEquId, inlayIndex, shenxiaId, true);
			} else {
				result = HorseEquInlayEntityManager.getInst().takeOff(player, horseEquId, inlayIndex);
			}
			if (result == null) {
				HorseEquInlay entity = HorseEquInlayEntityManager.getInst().getEntity((EquipmentEntity) ae);
				if (entity != null) {
					inlayId = entity.getInlayArticleIds()[inlayIndex];
				}
			}
		}
		INLAYTAKE_HORSEBAOSHI_RES resp = new INLAYTAKE_HORSEBAOSHI_RES(GameMessageFactory.nextSequnceNum(), horseEquId, inlayIndex, inlayId);
		player.addMessageToRightBag(resp);
	}

	public long getHorseEquId() {
		return horseEquId;
	}

	public void setHorseEquId(long horseEquId) {
		this.horseEquId = horseEquId;
	}

	public int getInlayIndex() {
		return inlayIndex;
	}

	public void setInlayIndex(int inlayIndex) {
		this.inlayIndex = inlayIndex;
	}

	public long getBaoshiId() {
		return baoshiId;
	}

	public void setBaoshiId(long baoshiId) {
		this.baoshiId = baoshiId;
	}
	
	

}
