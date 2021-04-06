package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.society.zongpai.Option_Zongpai_Update;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.INPUT_WINDOW_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.zongzu.ZongPaiSubSystem;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;

/**
 * 更改宗派宗旨
 * @author Administrator
 * 
 */
public class Option_Zongpai_Update_Declaration extends Option {

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game, Player player) {

		// 权限验证
		String result = ZongPaiManager.getInstance().宗主身份判断(player);

		if (result.equals("")) {
			ZongPai zp = ZongPaiManager.getInstance().getZongPaiByPlayerId(player.getId());
			// 弹输入密码框
			WindowManager windowManager = WindowManager.getInstance();
			MenuWindow mw = windowManager.createTempMenuWindow(600);
			mw.setTitle(Translate.修改宗派宗旨);
			mw.setDescriptionInUUB(Translate.translateString(Translate.修改宗派宗旨_提示, new String[][] { { Translate.STRING_1, zp.getDeclaration() } }));// "修改宗派宗旨，当前宗旨："+zp.getDeclaration());

			Option_Zongpai_Update option = new Option_Zongpai_Update();
			option.setText(Translate.确定);

			Option_Cancel cancel = new Option_Cancel();
			cancel.setText(Translate.取消);
			mw.setOptions(new Option[] { option, cancel });

			INPUT_WINDOW_REQ res = new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getTitle(), mw.getDescriptionInUUB(), (byte) 2, (byte) 100, Translate.在这里输入, Translate.确定, Translate.取消, new byte[0]);
			player.addMessageToRightBag(res);
			// ZongPaiSubSystem.logger.info("[修改宗派宗旨申请成功] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []");
			if (ZongPaiSubSystem.logger.isInfoEnabled()) ZongPaiSubSystem.logger.info("[修改宗派宗旨申请成功] [{}] [{}] [{}] []", new Object[] { player.getId(), player.getName(), player.getUsername() });

		} else {
			player.sendError(result);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
