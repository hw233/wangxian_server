package com.fy.engineserver.newtask;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.sprite.Player;

/**
 * 接取任务时给予玩家的物品
 * 
 * 
 */
public class TaskGivenArticle {
	private int[] nums;
	private String[] names;
	private int[] colors;

	public TaskGivenArticle(int[] nums, String[] names, int[] colors) {
		this.names = names;
		this.colors = colors;
		this.nums = nums;
	}

	public int[] getNums() {
		return nums;
	}

	public void setNums(int[] nums) {
		this.nums = nums;
	}

	public String[] getNames() {
		return names;
	}

	public void setNames(String[] names) {
		this.names = names;
	}

	public int[] getColors() {
		return colors;
	}

	public void setColors(int[] colors) {
		this.colors = colors;
	}

	public void giveToPlayer(Player player) {
		for (int i = 0; i < getNames().length; i++) {
			String name = getNames()[i];
			Article article = ArticleManager.getInstance().getArticle(name);
			if (article != null) {
				TaskManager.getInstance().addArticleAndNoticeClient(player, article, getNums()[i], getColors()[i]);
			}
		}
	}
}
