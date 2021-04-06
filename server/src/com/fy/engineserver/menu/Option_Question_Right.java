package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.question.Question;
import com.fy.engineserver.sprite.Player;

/**
 * 答题窗口的选项，此类表示正确的选项，选中后会开启下一个题目或者结束整个答题过程
 * 
 * @author 
 * 
 */
public class Option_Question_Right extends Option {
	/**
	 * 通过任务编号和目标索引定位到任务目标
	 */
	private int taskId;
	private int goalIndex;
	/**
	 * 表示第几道题目，从1开始，即第一道题的取值为1，依次递增<br>
	 * 也可以理解为下一道题目的索引
	 */
	private short goalValue;

	public Option_Question_Right(String text, int goalIndex, short goalValue, int taskId) {
		setOType(OPTION_TYPE_SERVER_FUNCTION);
		setText(text);
		setColor(0xFFFFFF);
		this.goalIndex = goalIndex;
		this.goalValue = goalValue;
		this.taskId = taskId;
		this.setIconId("158");
	}

	public void doSelect(Game game, Player player) {}

	/**
	 * 把题目里的数据填充到窗口里
	 * 
	 * @param question
	 *            需要发送给玩家的题目
	 * @param taskId
	 * @param goalIndex
	 * @param goalValue
	 * @return 题目的显示窗口，发送玩家供选择
	 */
	private MenuWindow createWindow(Question question, int taskId, int goalIndex, short goalValue) {
		MenuWindow window = WindowManager.getInstance().createTempMenuWindow(180);
		window.setTitle(Translate.text_5376);
		window.setDescriptionInUUB(question.getDescription());
		String[] options = question.getOptions();
		Option[] winOptions = new Option[options.length];
		for (int i = 0; i < options.length; i++) {
			if (i == question.getRightOption()) {
				winOptions[i] = new Option_Question_Right(options[i], goalIndex, goalValue, taskId);
				winOptions[i].setIconId("158");
			} else {
				winOptions[i] = new Option_Question_Wrong(options[i], window);
				winOptions[i].setIconId("158");
			}
		}
		window.setOptions(winOptions);
		return window;
	}
}
