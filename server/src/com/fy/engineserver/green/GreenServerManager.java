package com.fy.engineserver.green;

import java.util.HashSet;
import java.util.Set;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.MoneyProps;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.xuanzhi.boss.game.GameConstants;

public class GreenServerManager {

	public static Set<String> greenServers = new HashSet<String>();

	static {
		//greenServers.add("刀剑如梦");
//		greenServers.add("国内本地测试");
	}

	public static boolean isGreenServer() {
		GameConstants gs = GameConstants.getInstance();
		if (gs == null) {
			return false;
		}
		return greenServers.contains(gs.getServerName());
	}
	
	public static Set<String> bindServers_官方 = new HashSet<String>();
	public static Set<String> bindServers_台服 = new HashSet<String>();
	
	static {
		
		bindServers_台服.add("氣衝霄漢");
		bindServers_台服.add("神魔仙界");
		bindServers_台服.add("縱橫九州");
		bindServers_台服.add("蜀山之道");
		bindServers_台服.add("禦劍伏魔");
		bindServers_台服.add("仙尊降世");
		
//		bindServers_官方.add("修罗转生");
//		bindServers_官方.add("盛世欢歌");
//		bindServers_官方.add("万里苍穹");
//		bindServers_官方.add("飘渺仙道");
		/*bindServers_官方.add("琼楼金阙");
		bindServers_官方.add("pan3");
		bindServers_官方.add("天下无双");
		bindServers_官方.add("海纳百川");
		bindServers_官方.add("群龙聚首");
		bindServers_官方.add("刀剑如梦");*/
	}
	
	public static boolean isUseBindProp = true;
	public static String bindpropName = Translate.银票;
	
	//银子交易绑定服务器
	public static boolean isBindYinZiServer() {
		GameConstants gs = GameConstants.getInstance();
		if (gs == null) {
			return false;
		}
		if (PlatformManager.getInstance().platformOf(Platform.台湾)) {
			return bindServers_台服.contains(gs.getServerName());
		}
		if (PlatformManager.getInstance().platformOf(Platform.官方)) {
			return bindServers_官方.contains(gs.getServerName());
		}
		return false;
	}
	
	public static boolean canToOtherPlayer(ArticleEntity ae) {
		if (!isBindYinZiServer()) {
			return true;
		}
		try{
			if (ae == null) {
				return false;
			}
			ArticleManager am = ArticleManager.getInstance();
			Article a = am.getArticle(ae.getArticleName());
			if (a == null) {
				return false;
			}
			if (a instanceof MoneyProps) {
				return false;
			}else {
				return true;
			}
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
}
