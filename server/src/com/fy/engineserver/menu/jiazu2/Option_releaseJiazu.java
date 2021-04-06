package com.fy.engineserver.menu.jiazu2;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuFunction;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.JiazuTitle;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.minigame.MinigameConstant;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.sprite.Player;

public class Option_releaseJiazu extends Option implements NeedCheckPurview{

	@Override
	public void doSelect(Game game, Player player) {
		String result = JiazuManager2.checkJiazu(player);
		if (result != null) {
			player.sendError(result);
			return;
		}
		JiazuMember jm = JiazuManager.getInstance().getJiazuMember(player.getId(), player.getJiazuId());
		if (jm == null) {
			return ;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu.getJiazuStatus() == JiazuManager2.家族功能正常) {
			player.sendError(Translate.家族未被封印不需要解封);
			return ;
		}
		boolean perMission = JiazuTitle.hasPermission(jm.getTitle(), JiazuFunction.release_JIAZU);
		if (!perMission) {
			player.sendError(Translate.权限不足不能使用此功能);
			return;
		}
		SeptStation ss = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
		if (ss != null) {
			popConfirmWindow(player, ss.getInfo().getCurrMaintai());
		} 
	}
	
	private void popConfirmWindow(Player p, long costNum) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_ConfirmReleaseJiazu option1 = new Option_ConfirmReleaseJiazu();
		option1.setText(MinigameConstant.CONFIRM);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		String costDes = BillingCenter.得到带单位的银两(costNum);
		String msg = String.format(Translate.确认解封家族, costDes);
		mw.setDescriptionInUUB(msg);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		p.addMessageToRightBag(creq);
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
		if (jiazu.getJiazuStatus() == JiazuManager2.家族功能正常) {
			return false;
		}
		JiazuMember jm = JiazuManager.getInstance().getJiazuMember(player.getId(), player.getJiazuId());
		if (jm == null) {
			return false;
		}
		boolean perMission = JiazuTitle.hasPermission(jm.getTitle(), JiazuFunction.release_JIAZU);
		if (!perMission) {
			return false;
		}
		return true;
	}

}
