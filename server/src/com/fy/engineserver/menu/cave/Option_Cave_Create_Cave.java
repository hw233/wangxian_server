package com.fy.engineserver.menu.cave;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.ParticleData;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.exceptions.AlreadyHasCaveException;
import com.fy.engineserver.homestead.exceptions.CountyNotSameException;
import com.fy.engineserver.homestead.exceptions.FertyNotFountException;
import com.fy.engineserver.homestead.exceptions.IndexHasBeUsedException;
import com.fy.engineserver.homestead.exceptions.LevelToolowException;
import com.fy.engineserver.homestead.exceptions.PointNotFoundException;
import com.fy.engineserver.homestead.faery.service.CaveSubSystem;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_PLAY_PARTICLE_RES;
import com.fy.engineserver.sprite.Player;

public class Option_Cave_Create_Cave extends Option implements NeedCheckPurview {

	public Option_Cave_Create_Cave() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doSelect(Game game, Player player) {
		if (CaveSubSystem.logger.isDebugEnabled()) CaveSubSystem.logger.debug(player.getLogString() + "[申请创建仙府]");
		FaeryManager manager = FaeryManager.getInstance();
		String failreason = Translate.text_cave_001;
		try {
			manager.createDefaultCave(player);
			FaeryManager.getInstance().noticeDynamic(player);
			player.addMessageToRightBag(FaeryManager.getInstance().caveSimple(player,null));
		} catch (PointNotFoundException e) {
			CaveSubSystem.logger.error("坐标信息没找到:" + e.getMsg(), e);
			failreason = Translate.text_cave_002;
		} catch (FertyNotFountException e) {
			CaveSubSystem.logger.error("Faery不存在:" + e.getMsg(), e);
			failreason = Translate.text_cave_003;
		} catch (CountyNotSameException e) {
			CaveSubSystem.logger.error("国家不符:" + e.getMsg(), e);
			failreason = Translate.text_cave_004;
		} catch (IndexHasBeUsedException e) {
			CaveSubSystem.logger.error("该位置已经被占用了:" + e.getMsg(), e);
			failreason = Translate.text_cave_005;
		} catch (AlreadyHasCaveException e) {
			CaveSubSystem.logger.error("已经有仙府了:" + e.getMsg(), e);
			failreason = Translate.text_cave_006;
		} catch (LevelToolowException e) {
			CaveSubSystem.logger.error("等级太低:" + e.getMsg(), e);
			failreason = Translate.translateString(Translate.text_cave_093, new String[][] { { Translate.COUNT_1, "20" } });
		} catch (Exception e) {
			CaveSubSystem.logger.error("[严重错误] [仙府创建ID失败]", e);
			failreason = Translate.text_cave_095;
		}
		player.sendError(failreason);
		if (Translate.text_cave_001.equals(failreason)) {
			ParticleData[] particleDatas = new ParticleData[1];
			particleDatas[0] = new ParticleData(player.getId(), "任务光效/获得仙府文字", -1, 2, 1, 1);
			NOTICE_CLIENT_PLAY_PARTICLE_RES client_PLAY_PARTICLE_RES = new NOTICE_CLIENT_PLAY_PARTICLE_RES(GameMessageFactory.nextSequnceNum(), particleDatas);
			player.addMessageToRightBag(client_PLAY_PARTICLE_RES);

			{
				// 统计&成就
				AchievementManager.getInstance().record(player, RecordAction.获得庄园);
			}

			if (CaveSubSystem.logger.isDebugEnabled()) CaveSubSystem.logger.debug(player.getLogString() + "[申请仙府发送例子效果]");
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		CaveSubSystem.logger.warn(player.getLogString() + " [玩家点击菜单] [是否能看到创建仙府(Option_Cave_Create_Cave):"+(player.getCaveId() <= 0)+"]");
		return player.getCaveId() <= 0;
	}
}
