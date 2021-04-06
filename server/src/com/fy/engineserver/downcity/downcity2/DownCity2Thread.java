package com.fy.engineserver.downcity.downcity2;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;



public class DownCity2Thread extends Thread{
	public static int heartbeatTime = 1000;
	
	public static long giveTime = 7000;
	
	public static boolean isStart = true;

	private volatile Map<Long, PropModel> map = new Hashtable<Long, PropModel>();
	
	private Object lock = new Object();
	
	@Override
	public void run(){
		DownCityManager2.logger.warn(this.getName() + "  启动成功!");
		while (isStart) {
			synchronized (lock) {
				Iterator<Long> ite = map.keySet().iterator();
				List<Long> removeList = new ArrayList<Long>();
				while (ite.hasNext()) {
					long now = System.currentTimeMillis();
					long playerId = ite.next();
					PropModel value = map.get(playerId);
					try {
						if (now >= (value.getStartTime() + giveTime)) {
							Player player = GamePlayerManager.getInstance().getPlayer(playerId);
							Article a = ArticleManager.getInstance().getArticleByCNname(value.getArticleCNNName());
							ArticleEntity ae = DefaultArticleEntityManager.getInstance().createEntity(a, value.isBind(), ArticleEntityManager.副本结束转盘, player, 
									value.getColorType(), 1, true);
							removeList.add(playerId);
							this.put2Bag(player, ae, value.getNum());
						}
					} catch (Exception e) {
						DownCityManager2.logger.error("[副本结束转盘] [发送奖励] [异常] [" + playerId + "] [" + value + "]", e);
					}
				}
				for (Long pId : removeList) {
					map.remove(pId);
				}
			}
			try {
				Thread.sleep(heartbeatTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void put2Bag(Player p, ArticleEntity ae, int num) {
		if(!p.putToKnapsacks(ae, num, "副本结束转盘")) {
			p.sendError(Translate.背包空间不足);
			if(DownCityManager2.logger.isInfoEnabled()) {
				DownCityManager2.logger.info("[副本结束转盘] [背包空间不足发邮件] [" + p.getLogString() + "]");
			}
			try{
				MailManager.getInstance().sendMail(p.getId(), new ArticleEntity[] { ae }, new int[] { num }, Translate.系统, Translate.副本转盘, 0L, 0L, 0L, "副本结束转盘");
				DownCityManager2.logger.warn("[副本结束转盘] [实际发送奖励] [发送 邮件] [成功] [" + p.getLogString() + "] [" + ae.getArticleName() + "]");
			} catch(Exception e) {
				DownCityManager2.logger.error("[副本结束转盘] [副本结束转盘异常] [" + p.getLogString() + "][物品名:" + ae.getArticleName() + "&&颜色:" + ae.getColorType() + "&&是否绑定" + ae.isBinded() +"]");
			}
		} else {
			DownCityManager2.logger.warn("[副本结束转盘] [实际发送奖励] [发送背包] [成功] [" + p.getLogString() + "] [" + ae.getArticleName() + "]");
		}
	}
	
	public void putPropModel2Thread(Player player, PropModel pm) {
		synchronized (lock) {
			PropModel tempPm = new PropModel(pm.getArticleCNNName(), pm.getColorType(), pm.getNum(), pm.getProbabbly4Show(), 
					pm.getProbabbly4Get(), pm.getTempArticleEntityId());
			tempPm.setStartTime(pm.getStartTime());
			if (map.get(player.getId()) != null) {
				PropModel value = map.get(player.getId());
				try {
					Article a = ArticleManager.getInstance().getArticleByCNname(value.getArticleCNNName());
					ArticleEntity ae = DefaultArticleEntityManager.getInstance().createEntity(a, value.isBind(), ArticleEntityManager.副本结束转盘, player, 
							value.getColorType(), 1, true);
					this.put2Bag(player, ae, value.getNum());
				} catch (Exception e) {
					DownCityManager2.logger.error("[副本结束转盘] [发送奖励] [异常] [" + player.getLogString() + "] [" + value + "]", e);
				}
			}
			map.put(player.getId(), tempPm);
		}
	}

	public Map<Long, PropModel> getMap() {
		return map;
	}

	public void setMap(Map<Long, PropModel> map) {
		this.map = map;
	}
}
