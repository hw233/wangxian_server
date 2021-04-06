package com.fy.engineserver.sound.manager;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.equipments.Weapon;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.sprite.Player;

public class SoundManager {

	private static final String[] 人物_死亡 = new String[]{"","nangsiwang","hehuannansiwang","nvlsiwang","tianyinnvsiwang","nangsiwang"};
	
	private static final String 物品掉落_武器 = "qingtongjiansiwang";
	private static final String 物品掉落_装备 = "qingtongjianshoushang";
	private static final String 物品掉落_其他 = "qingtongjianshoushang";
	
	public static String getPlayerDeadSound(Player player){
		if(player != null){
			return 人物_死亡[player.getCareer()];
		}
		return "";
	}
	
	public static String getArticleFlopSound(ArticleEntity ae){
		ArticleManager am = ArticleManager.getInstance();
		if(ae != null && am != null){
			Article a = am.getArticle(ae.getArticleName());
			if(a instanceof Weapon){
				return 物品掉落_武器;
			}
			if(a instanceof Equipment){
				return 物品掉落_装备;
			}
			if(a instanceof Article){
				return 物品掉落_其他;
			}
		}
		return "";
	}
}
