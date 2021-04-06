package com.fy.engineserver.menu.zhaoHuiMiMa;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.INPUT_WINDOW_REQ;
import com.fy.engineserver.sprite.Player;

public class Option_CangKuMiMa extends Option {

	public Option_CangKuMiMa () {
		super();
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game, Player player) {
		if (player.getPassport().getSecretAnswer() == null || "".equals(player.getPassport().getSecretAnswer())) {
			player.sendError(Translate.您还未设置密保);
			return;
		}
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
		mw.setTitle(Translate.找回仓库密码);
		mw.setDescriptionInUUB(Translate.输入密保提示 + player.getPassport().getSecretQuestion());
		Option_InputMiBao option = new Option_InputMiBao(0);
		option.setText(Translate.确定);
		mw.setOptions(new Option[] {option});
		INPUT_WINDOW_REQ req = new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getTitle(), mw.getDescriptionInUUB(), (byte)1, (byte)30, "", Translate.确定, Translate.取消, new byte[]{});
		player.addMessageToRightBag(req);
	}
}
