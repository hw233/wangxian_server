package com.fy.engineserver.menu;

import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.NOTIFY_EQUIPMENT_CHANGE_REQ;
import com.fy.engineserver.message.XILIAN_EQUIPMENT_SURE_RES;
import com.fy.engineserver.sprite.Player;

public class Option_Confirm_Xilian_Equipment extends Option {
	private long materialId;
	private EquipmentEntity ee;
	private boolean bind;

	public Option_Confirm_Xilian_Equipment(long materialId, EquipmentEntity ee, boolean bind) {
		this.materialId = materialId;
		this.ee = ee;
		this.bind = bind;
	}

	@Override
	public void doSelect(Game game, Player player) {
		ArticleEntityManager aem = DefaultArticleEntityManager.getInstance();
		DefaultArticleEntityManager daem = (DefaultArticleEntityManager) aem;
		try {
			ArticleEntity aeDel = player.removeArticleEntityFromKnapsackByArticleId(materialId, "装备洗练删除", false);
			if (aeDel != null) {
				boolean result = daem.xilianEquipmentAddProp(ee, bind);
				if (result) {
					NOTIFY_EQUIPMENT_CHANGE_REQ nreq = new NOTIFY_EQUIPMENT_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] { CoreSubSystem.translate(ee) });
					player.addMessageToRightBag(nreq);
					player.sendError(Translate.洗炼成功);
					player.addMessageToRightBag(new XILIAN_EQUIPMENT_SURE_RES(GameMessageFactory.nextSequnceNum(), result));
					CoreSubSystem.notifyEquipmentChange(player, new EquipmentEntity[] { ee });
				} else {
					ArticleManager.logger.error(player.getLogString() + "[洗炼失败] [装备:" + ee.getArticleName() + "] [id:" + ee.getId() + "]");
				}
			} else {
				String description = Translate.未放入装备或者洗炼符;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[装备洗练确认] [未放入洗练符] [materialId:" + materialId + "] [" + player.getLogString() + "] [" + description + "]");
			}

		} catch (Exception e) {
			ArticleManager.logger.error(player.getLogString() + "装备洗练", e);
		}
	}

	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
}
