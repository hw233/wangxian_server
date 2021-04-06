package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

/**
 * 随机答题流程的入口菜单项
 * 
 * @author 
 * 
 */
public class Option_Question_Entrance extends Option_Question_Random {
	public Option_Question_Entrance(String text, int goalIndex, int taskId) {
		super(text, goalIndex, taskId);
	}

	public void doSelect(Game game, Player player) {
//		TaskGoal goal = TaskManager.getInstance().getTaskById(taskId).getGoals()[goalIndex];
//		String[] questionRange = goal.getMonsterNames();
//		short num = goal.getGoalAmount();
//		String[] result = null;
//
//		if (num == questionRange.length) {
//			result = questionRange;
//		} else {
//			result = new String[num];
//			if (num < questionRange.length) {
//				Random random = new Random();
//				boolean[] marks = new boolean[questionRange.length];
//				for (int i = 0; i < num; i++) {
//					int s = (random.nextInt() >>> 1) % questionRange.length;
//					while (marks[s]) {
//						s++;
//						if (s == questionRange.length) {
//							s = 0;
//						}
//					}
//					marks[s] = true;
//					result[i] = questionRange[s];
//				}
//			} else {
//				for (int i = 0; i < num; i++) {
//					result[i] = questionRange[i % questionRange.length];
//				}
//			}
//		}
//
//		MenuWindow window = createQuestionWindow(result, (short) 0);
//		if (window != null) {
//			player.addMessageToRightBag(new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), window, window
//					.getOptions()));
//		}
	}
}
