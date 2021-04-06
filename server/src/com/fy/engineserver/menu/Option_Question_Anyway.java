package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

/**
 * 答题选项，无论正确与否都显示下一题
 * 
 * @author 
 * 
 */
public class Option_Question_Anyway extends Option_Question_Random {
	/**
	 * 当前选项是否代表正确答案
	 */
	private final boolean correct;
	/**
	 * 当前是第几个题目
	 */
	private final short current;
	/**
	 * 事先选好的题目
	 */
	private final String[] questions;

	public Option_Question_Anyway(String text, boolean correct, short current, int goalIndex, String[] questions,
			int taskId) {
		super(text, goalIndex, taskId);
		this.correct = correct;
		this.current = current;
		this.questions = questions;
		
	}

	public void doSelect(Game game, Player player) {}
}
