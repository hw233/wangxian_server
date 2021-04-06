package com.fy.engineserver.newtask.actions;

import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.newtask.service.TaskConfig;

public class TaskActionOfGetArticle extends TaskAction implements TaskConfig {

	private TaskActionOfGetArticle() {
		setTargetType(TargetType.GET_ARTICLE);
	}

	public static TaskActionOfGetArticle createAction(ArticleEntity ae) {
		TaskActionOfGetArticle action = new TaskActionOfGetArticle();
		action.setName(ae.getArticleName());
		action.setColor(ae.getColorType());
		action.setNum(1);
		return action;
	}

	public static TaskActionOfGetArticle createAction(String articleName, int articleColor) {
		TaskActionOfGetArticle action = new TaskActionOfGetArticle();
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
