package com.fy.engineserver.menu.cave;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.PetHookInfo;
import com.fy.engineserver.homestead.faery.Faery;
import com.fy.engineserver.homestead.faery.service.CaveSubSystem;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.CAVE_QUERY_PETHOOK_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;

public class Option_Cave_SeeHookStore extends CaveOption implements FaeryConfig, NeedCheckPurview {

	public Option_Cave_SeeHookStore() {

	}

	@Override
	public void doSelect(Game game, Player player) {
		try {
			
			
			
			
			
			
			boolean isSelf = FaeryManager.isSelfCave(player, getNpc().getId());
			Cave cave = getNpc().getCave();
			if (cave == null) {
				player.sendError(Translate.text_cave_047);
				return;
			}
			if (cave.getStatus() == CAVE_STATUS_KHATAM) {
				player.sendError(Translate.仙府被锁定);
				return;
			}
			int maxNum = FaeryManager.getInstance().getPethouseCfg()[cave.getPethouse().getGrade() - 1].getHookNum();
			List<PetHookInfo> list = new ArrayList<PetHookInfo>();
			List<Integer> indexs = new ArrayList<Integer>();
			for (int i = 0; i < cave.getPethouse().getHookInfos().length; i++) {
				PetHookInfo hookInfo = cave.getPethouse().getHookInfos()[i];
				if (hookInfo != null) {

					hookInfo.setExp(cave.getHookExp(hookInfo));
					list.add(hookInfo);
					indexs.add(i);
				}
			}
			int[] index = new int[indexs.size()];
			for (int i = 0; i < index.length; i++) {
				index[i] = indexs.get(i);
			}
			CAVE_QUERY_PETHOOK_RES res = new CAVE_QUERY_PETHOOK_RES(GameMessageFactory.nextSequnceNum(), maxNum, (byte) (isSelf ? 2 : 1), getNpc().getId(), cave.getOwnerId(), list.toArray(new PetHookInfo[0]), index);
			player.addMessageToRightBag(res);
		} catch (Exception e) {
			CaveSubSystem.logger.error("[hookPet] [异常]", e);
		}
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		if (getNpc() == null) {
			return false;
		}
		Cave cave = getNpc().getCave();
		if (cave == null) {
			return false;
		}

		Faery faery = cave.getFaery();
		if (faery == null) {
			return false;
		}
		return faery.getCountry() == (int) player.getCountry();
	}
}
