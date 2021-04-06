package com.fy.engineserver.activity.droprule;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.DropRule;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.monster.Monster.AttackRecord;
import com.fy.engineserver.sprite.npc.FlopCaijiNpc;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPCManager;
import com.fy.engineserver.sprite.pet.Pet;

/**
 * 国服15年新春活动
 * 
 */
public class DropRuleForSqage2015_2 implements DropRule {

	public static int[] monsterIds = new int[] {20113630};
	
	public static String[] articleCNNames = new String[] { "凝神魂石", "蓝色坐骑魂石锦囊" };
	/** 礼盒掉落概率 */
	public static int dropProb = 100000;
	
	public static int totalProb = 100000;
	
	public static int dropNum = 10;
	
	public static Random ran = new Random(System.currentTimeMillis());

	@Override
	public void doDrop(Monster monster, Game game) {
		ActivitySubSystem.logger.warn("["+this.getClass().getSimpleName()+"] [怪物死亡] [" + monster.getName() + "] [攻击列表长度:" + monster.attackRecordList.size() + "]");
		for (AttackRecord ar : monster.attackRecordList) {
			String log = "--";
			if (ar.living instanceof Player) {
				log = "角色:" + ((Player) ar.living).getLogString();
			} else if (ar.living instanceof Pet) {
				log = "宠物,主人是:" + ((Pet) ar.living).getOwnerName();
			} else {
				log = "未知:" + ar.living.getClass();
			}
			ActivitySubSystem.logger.warn("[DropRuleForTW99] [怪物死亡] [" + monster.getName() + "] [清算攻击者] [攻击者队列:" + log + "]");
		}
		int index = -1;
		for (int i = 0; i < monsterIds.length; i++) {
			if (monsterIds[i] == monster.getSpriteCategoryId()) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			ActivitySubSystem.logger.warn("["+this.getClass().getSimpleName()+"] [怪物死亡] [" + monster.getName() + "] [怪物不在配置列表]");
			return;
		}
		Article article = ArticleManager.getInstance().getArticleByCNname(articleCNNames[0]);
		if (article == null) {
			ActivitySubSystem.logger.warn("["+this.getClass().getSimpleName()+"] [怪物死亡] [" + monster.getName() + "] [掉落不在配置列表] [" + articleCNNames[0] + "]");
			return;
		}

		List<Player> attackers = new ArrayList<Player>();
		List<Long> attackerIds = new ArrayList<Long>();
		for (AttackRecord ar : monster.attackRecordList) {
			Player attacker = null;
			if (ar.living instanceof Player) {
				Player temp = (Player) ar.living;
				attacker = temp;
			} else if (ar.living instanceof Pet) {
				Pet pet = (Pet) ar.living;
				long ownerId = pet.getOwnerId();
				try {
					attacker = GamePlayerManager.getInstance().getPlayer(ownerId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (attacker != null && !attackers.contains(attacker)) {
				attackers.add(attacker);
				attackerIds.add(attacker.getId());
				ActivitySubSystem.logger.warn("["+this.getClass().getSimpleName()+"] [怪物死亡] [" + monster.getName() + "] [新增攻击者] [" + attacker.getLogString() + "]");
			}
		}
		
		int dropNums = dropNum + attackers.size();
		
		long now = SystemTime.currentTimeMillis();
		NPCManager nm = MemoryNPCManager.getNPCManager();
		if (dropNums > 0) {
			// TODO 掉落物品
			for (int i=0; i<dropNums; i++) {
				FlopCaijiNpc fcn = (FlopCaijiNpc) nm.createNPC(MemoryNPCManager.掉落NPC的templateId);
				fcn.setStartTime(now);
				fcn.setEndTime(now + MemoryNPCManager.掉落NPC的存在时间);
				fcn.setAllCanPickAfterTime(MemoryNPCManager.所有人都可以拾取的时长);
				fcn.setFlopType((byte)0);
				fcn.setPlayersList(attackerIds);
				ArticleEntity ae = null;
				try {
					ae = ArticleEntityManager.getInstance().createEntity(article, false, ArticleEntityManager.CREATE_REASON_SPRITE_FLOP, null, article.getColorType(), 1, false);
				} catch (Exception e) {
					ActivitySubSystem.logger.error("["+this.getClass().getSimpleName()+"] [怪物死亡] [分配奖励] [创建物品失败] [" + article.getLogString() + "]", e);
					continue;
				}
				fcn.setAe(ae);
				fcn.isMonsterFlop = true;
	
				int color = ArticleManager.COLOR_WHITE;
				try {
					if (article.getFlopNPCAvata() != null) {
						fcn.setAvataSex(article.getFlopNPCAvata());
						if (article.getFlopNPCAvata().equals("yinyuanbao")) {
							fcn.setAvataSex("baoxiang");
						}
						ResourceManager.getInstance().getAvata(fcn);
					}
					color = ArticleManager.getColorValue(article, ae.getColorType());
				} catch (Exception ex) {
					if (ActivitySubSystem.logger.isErrorEnabled()) {
						ActivitySubSystem.logger.error("[怪被打死] [" + monster.getName() + "] [物品掉落] [" + article.getLogString() + "] [" + ae.getId() + "]", ex);
					}
				}
	
				Point point = monster.得到位置();
				fcn.setX(point.x);
				fcn.setY(point.y);
	
				fcn.setName(article.getName());
				fcn.setNameColor(color);
	
				fcn.setTitle("");
				game.addSprite(fcn);
	
				// 物品掉落声音广播
				((FlopCaijiNpc) fcn).notifyAroundPlayersPlaySound(game);
				ActivitySubSystem.logger.error("["+this.getClass().getSimpleName()+"] [怪物死亡] [创建物品成功] [" + article.getLogString() + "] [" + point.toString() + "]");
			}

		}
		
		int ranNum = ran.nextInt(totalProb);
		if (ActivitySubSystem.logger.isDebugEnabled()) {
			ActivitySubSystem.logger.debug("[怪物死亡] ["+monster.getName()+"] [" + monster.getSpriteCategoryId() + "] [ranNum:" + ranNum + "] [dropProb:" + dropProb + "]");
		}
		
		if (ranNum <= dropProb) {
			Article article2 = ArticleManager.getInstance().getArticleByCNname(articleCNNames[1]);
			if (article2 == null) {
				ActivitySubSystem.logger.warn("["+this.getClass().getSimpleName()+"] [怪物死亡] [" + monster.getName() + "] [掉落不在配置列表] [" + articleCNNames[1] + "]");
				return;
			}
			FlopCaijiNpc fcn = (FlopCaijiNpc) nm.createNPC(MemoryNPCManager.掉落NPC的templateId);
			fcn.setStartTime(now);
			fcn.setEndTime(now + MemoryNPCManager.掉落NPC的存在时间);
			fcn.setAllCanPickAfterTime(MemoryNPCManager.所有人都可以拾取的时长);
			fcn.setFlopType((byte)0);
			fcn.setPlayersList(attackerIds);
			ArticleEntity ae = null;
			try {
				ae = ArticleEntityManager.getInstance().createEntity(article2, false, ArticleEntityManager.CREATE_REASON_SPRITE_FLOP, null, article2.getColorType(), 1, false);
			} catch (Exception e) {
				ActivitySubSystem.logger.error("["+this.getClass().getSimpleName()+"] [怪物死亡] [创建物品失败] [" + article2.getLogString() + "]", e);
				return;
			}
			fcn.setAe(ae);
			fcn.isMonsterFlop = true;
			int color = ArticleManager.COLOR_WHITE;
			try {
				if (article2.getFlopNPCAvata() != null) {
					fcn.setAvataSex(article2.getFlopNPCAvata());
					if (article2.getFlopNPCAvata().equals("yinyuanbao")) {
						fcn.setAvataSex("baoxiang");
					}
					ResourceManager.getInstance().getAvata(fcn);
				}
				color = ArticleManager.getColorValue(article2, ae.getColorType());
			} catch (Exception ex) {
				if (ActivitySubSystem.logger.isErrorEnabled()) {
					ActivitySubSystem.logger.error("[怪被打死] [" + monster.getName() + "] [物品掉落] [" + article2.getLogString() + "] [" + ae.getId() + "]", ex);
				}
			}

			Point point = monster.得到位置();
			fcn.setX(point.x);
			fcn.setY(point.y);

			fcn.setName(article2.getName());
			fcn.setNameColor(color);

			fcn.setTitle("");
			game.addSprite(fcn);

			// 物品掉落声音广播
			((FlopCaijiNpc) fcn).notifyAroundPlayersPlaySound(game);
		}
	}
}
