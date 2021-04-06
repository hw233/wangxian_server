package com.fy.engineserver.newtask.actions;

import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.sprite.Player;

public class TaskActionOfUseArticle extends TaskAction implements TaskConfig {
	
	private Player self;
	
	private TaskActionOfUseArticle() {
		setTargetType(TargetType.USE_ARTICLE);
	}

	//TODO PropsEntity ? ArticleEntity ? 会不会存在使用更多类型的物品
	public static TaskActionOfUseArticle createAction(ArticleEntity ae, Player self) {
		TaskActionOfUseArticle action = new TaskActionOfUseArticle();
		action.setColor(ae.getColorType());
		action.setName(ae.getArticleName());
		action.setNum(1);
		action.setSelf(self);
		return action;
	}

	public Player getSelf() {
		return self;
	}

	public void setSelf(Player self) {
		this.self = self;
	}
}
