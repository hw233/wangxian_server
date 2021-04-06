package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;

/**
 * 问答题的选项，此类表达错误选项
 * 
 * @author 
 * 
 */
public class Option_Question_Wrong extends Option {
	private static String wrongPrompt = Translate.text_5378;
	private MenuWindow window;

	public Option_Question_Wrong(String text, MenuWindow window) {
		setOType(OPTION_TYPE_SERVER_FUNCTION);
		setText(text);
		setColor(0xFFFFFF);
		this.window = window;
		this.setIconId("158");
	}

	public void doSelect(Game game, Player player) {
		if (!window.descriptionInUUB.startsWith(wrongPrompt)) {
			window.descriptionInUUB = wrongPrompt + window.descriptionInUUB;
		}
		player.addMessageToRightBag(new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), window, window
				.getOptions()));
	}
}
