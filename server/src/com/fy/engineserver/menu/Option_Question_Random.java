package com.fy.engineserver.menu;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.question.Question;
import com.fy.engineserver.menu.question.QuestionManager;

public abstract class Option_Question_Random extends Option {
	/**
	 * 通过任务编号和目标索引定位到任务目标
	 */
	protected final int taskId;
	protected final int goalIndex;

	public Option_Question_Random(String text, int goalIndex, int taskId) {
		setOType(OPTION_TYPE_SERVER_FUNCTION);
		setText(text);
		setColor(0xFFFFFF);
		this.goalIndex = goalIndex;
		this.taskId = taskId;
		this.setIconId("158");
	}

	protected MenuWindow createQuestionWindow(String[] allQuestion, short index) {
		MenuWindow window = null;
		Question question = QuestionManager.getInstance().getQuestionByName(allQuestion[index]);
		if (question != null) {
			window = WindowManager.getInstance().createTempMenuWindow(180);
			window.setTitle(Translate.text_5376);
			window.setDescriptionInUUB(question.getDescription());
			String[] options = question.getOptions();
			Option[] winOptions = new Option[options.length];
			for (int i = 0; i < options.length; i++) {
				winOptions[i] = new Option_Question_Anyway(options[i], i == question.getRightOption(), index,
						goalIndex, allQuestion, taskId);
			}
			window.setOptions(winOptions);
		}
		return window;
	}
}
