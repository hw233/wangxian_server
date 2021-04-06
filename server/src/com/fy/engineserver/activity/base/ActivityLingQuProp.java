package com.fy.engineserver.activity.base;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class ActivityLingQuProp {

	public static ActivityLingQuProp instance;
	
	public static boolean isOpen = true;
	
	private String diskPath;
	
	public DiskCache cache;
	
	public String GETPROP_PLAYERS = "GETPROP_PLAYERS";
	public HashMap<String, ArrayList<Long>> getPlayers = new HashMap<String, ArrayList<Long>>();
	
	public ArrayList<String> props = new ArrayList<String>();
	
	public void init() {
		
		instance = this;
		if (!isOpen) {
			return;
		}
		
//		props.add(Translate.登录礼包);
		
		cache = new DefaultDiskCache(new File(getDiskPath()), "给物品diskcatch", 1000L * 60 * 60 * 24 * 5);
		
		getPlayers = (HashMap<String, ArrayList<Long>>) cache.get(GETPROP_PLAYERS);
		if (getPlayers == null) {
			getPlayers = new HashMap<String, ArrayList<Long>>();
			cache.put(GETPROP_PLAYERS, getPlayers);
		}
		ServiceStartRecord.startLog(this);
		
	}
	
	public synchronized void option_getProp(Player player, String propName) {
		if (!isOpen) {
			return;
		}
		if (propName == null) {
			return;
		}
		try{
			if (!props.contains(propName)) {
				player.sendError("此物品不在奖励领取列表中。");
				return;
			}
			ArrayList<Long> players = getPlayers.get(propName);
			if (players == null) {
				players = new ArrayList<Long>();
				getPlayers.put(propName, players);
			}
			
			if (players.contains(player.getId())) {
				player.sendError("您已经领取过此奖励了。");
				return;
			}
			
			Article a = ArticleManager.getInstance().getArticle(propName);
			ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), 1, true);
			MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{entity}, "登顶大礼包", "恭喜您获得了《飘渺寻仙曲》等登顶活动大礼包，感谢您的支持，祝您游戏愉快！", 0, 0, 0, "腾讯登陆奖励");
			player.sendError("领取成功，物品已经通过邮件发送到您邮箱中，请查收!");
			players.add(player.getId());
			cache.put(GETPROP_PLAYERS, getPlayers);
			ActivitySubSystem.logger.warn("[领取奖励成功] [{}] [{}] [{}]", new Object[]{player.getLogString(), propName, entity.getId()});
		}catch(Exception e) {
			ActivitySubSystem.logger.error("option_getProp出错", e);
		}
	}

	public void setDiskPath(String diskPath) {
		this.diskPath = diskPath;
	}

	public String getDiskPath() {
		return diskPath;
	}
	
}
