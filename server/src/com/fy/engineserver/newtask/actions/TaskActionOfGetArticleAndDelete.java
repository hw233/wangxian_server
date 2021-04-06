package com.fy.engineserver.newtask.actions;

import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.newtask.service.TaskConfig;

public class TaskActionOfGetArticleAndDelete extends TaskAction implements TaskConfig {

	private TaskActionOfGetArticleAndDelete() {
		setTargetType(TargetType.GET_ARTICLE_AND_DELETE);
	}

	public static TaskActionOfGetArticleAndDelete createAction(ArticleEntity ae) {
		TaskActionOfGetArticleAndDelete action = new TaskActionOfGetArticleAndDelete();
		action.setName(ae.getArticleName());
		action.setColor(ae.getColorType());
		action.setNum(1);
		return action;
	}

	public static TaskActionOfGetArticleAndDelete createAction(String articleName, int articleColor) {
		TaskActionOfGetArticleAndDelete action = new TaskActionOfGetArticleAndDelete();
		action.setName(articleName);
		action.setColor(articleColor);
		action.setNum(1);
		return action;
	}
	
	@Override
	public int getScore() {
		//TODO 等待公式
		int color = getColor();
		return 2 * (color + 1);
	}
}
