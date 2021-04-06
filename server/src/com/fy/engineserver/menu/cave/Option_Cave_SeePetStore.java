package com.fy.engineserver.menu.cave;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.faery.service.CaveSubSystem;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.CAVE_QUERY_PETSTORE_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * 查看宠物仓库
 * 
 * 
 */
public class Option_Cave_SeePetStore extends CaveOption implements FaeryConfig, NeedCheckPurview {

	public Option_Cave_SeePetStore() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doSelect(Game game, Player player) {
		try {
			if (!FaeryManager.isSelfCave(player, getNpc().getId())) {// 不是自己的庄园不能操作
				HINT_REQ hint_REQ = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_cave_010 + ":" + getNpc().getId());
				player.addMessageToRightBag(hint_REQ);
				return;
			}

			Cave cave = FaeryManager.getInstance().getCave(player);

			if (cave == null) {
				player.sendError(Translate.text_cave_047);
				return;
			}
			if (cave.getStatus() == CAVE_STATUS_KHATAM) {
				player.sendError(Translate.仙府被锁定);
				return;
			}
			CAVE_QUERY_PETSTORE_RES res = new CAVE_QUERY_PETSTORE_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, getNpc().getId(),cave.getPethouse().getGrade(), cave.getPethouse().getStorePets(), FaeryManager.petStoreNums, cave.getOwnerId());
			player.addMessageToRightBag(res);
		} catch (Exception e) {
			CaveSubSystem.logger.error(this.getClass().getName(), e);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		return FaeryManager.isSelfCave(player, getNpc().getId());
	}
}
