package com.fy.engineserver.menu.jiazu2;

import java.util.Random;

import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.sprite.Player;

public class Option_ConfirmReleaseJiazu extends Option implements NeedCheckPurview{
	
	private long taskId;
	
	static Random RANDOM = new Random();

	@Override
	public void doSelect(Game game, Player player) {
		String result = JiazuManager2.checkJiazu(player);
		if (result != null) {
			player.sendError(result);
			return;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu.getJiazuStatus() == JiazuManager2.家族功能正常) {
			player.sendError(Translate.家族未被封印不需要解封);
			return ;
		}
		SeptStation ss = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
		if (ss != null) {
			if (jiazu.getJiazuMoney() < ss.getInfo().getCurrMaintai()) {
				player.sendError(Translate.家族资金不足解封);
				return ;
			}
			if (jiazu.consumeJiazuMoney(ss.getInfo().getCurrMaintai())) {
				jiazu.setJiazuStatus(JiazuManager2.家族功能正常);
				JiazuManager2.logger.warn("[新家族] [玩家主动解封家族] [成功] [消耗家族资金:" + ss.getInfo().getCurrMaintai() + "] [剩余家族资金:" + jiazu.getJiazuMoney() + "] [" + player.getLogString() + "] [jiazuId:" + jiazu.getJiazuID() + "]");
				try {
					String msg = String.format(Translate.解封家族发送消息, player.getName());
					ChatMessageService.getInstance().sendMessageToJiazu_dianshi(jiazu, msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					ChatMessageService.logger.error("[家族公告] [异常] ", e);
				}
			}
		} 
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	@Override
	public boolean canSee(Player player) {
		// TODO Auto-generated method stub
		String result = JiazuManager2.checkJiazu(player);
		if (result != null) {
			player.sendError(result);
			return false;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu != null && jiazu.getJiazuStatus() == JiazuManager2.家族功能正常) {
			return false;
		}
		return true;
	}

}
