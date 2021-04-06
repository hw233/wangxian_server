package com.fy.engineserver.menu.quizActivity;

import com.fy.engineserver.activity.quiz.QuizManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 玩家不同意答题活动
 * @author Administrator
 *
 */
public class Option_QuizActivity_DisAgree extends Option {

	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
//		QuizManager.getInstance().disAgreeQuiz(player);
		QuizManager.getInstance().confirmClose(player);
	}
	
}
