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
 * @author  12种新boss掉落
 * 
 */
public class DropRuleForSqage2015 implements DropRule {

	public static int[] monsterIds = new int[] { 20113333, 20113334, 20113335, 20113336, 20113337, 20113338, 20113339, 
		20113340, 20113341, 20113342, 20113343, 20113344 };
	public static String[] articleCNNames = new String[] { "末日天灾", "大日火灾", "黑日风灾", "地难", "人难", "魔神难", "三昧难", "本性难", "业障难", 
		"心意难", "魂魄难", "劫数难" };
	public static int dropNum = 10;

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
		Article article = ArticleManager.getInstance().getArticleByCNname(articleCNNames[index]);
		if (article == null) {
			ActivitySubSystem.logger.warn("["+this.getClass().getSimpleName()+"] [怪物死亡] [" + monster.getName() + "] [掉落不在配置列表] [" + articleCNNames[index] + "]");
			return;
		}

		List<Player> attackers = new ArrayList<Player>();
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
				ActivitySubSystem.logger.warn("["+this.getClass().getSimpleName()+"] [怪物死亡] [" + monster.getName() + "] [新增攻击者] [" + attacker.getLogString() + "]");
			}
		}
		List<Player> prizePlayers = new ArrayList<Player>();
		Random random = new Random();
		if (attackers.size() <= dropNum) {
			prizePlayers = attackers;
		} else {
			int num = dropNum;
			while (num > 0) {
				Player p = attackers.remove(random.nextInt(attackers.size()));
				if (p != null) {
					prizePlayers.add(p);
					ActivitySubSystem.logger.warn("["+this.getClass().getSimpleName()+"] [怪物死亡] [" + monster.getName() + "] [奖励攻击者] [第:" + num + "个] [" + p.getLogString() + "]");
				}
				num--;
			}
		}
		if (prizePlayers.size() > 0) {
			// TODO 掉落物品
			long now = SystemTime.currentTimeMillis();
			NPCManager nm = MemoryNPCManager.getNPCManager();
			ActivitySubSystem.logger.warn("["+this.getClass().getSimpleName()+"] [怪物死亡] [" + monster.getName() + "] [奖励攻击者] [获奖角色数量:" + prizePlayers.size() + "]");
			for (Player p : prizePlayers) {
				FlopCaijiNpc fcn = (FlopCaijiNpc) nm.createNPC(MemoryNPCManager.掉落NPC的templateId);
				fcn.setStartTime(now);
				fcn.setEndTime(now + MemoryNPCManager.掉落NPC的存在时间);
				fcn.setAllCanPickAfterTime(MemoryNPCManager.所有人都可以拾取的时长);
				fcn.setOwner(p);
				ArticleEntity ae = null;
				try {
					ae = ArticleEntityManager.getInstance().createEntity(article, false, ArticleEntityManager.CREATE_REASON_SPRITE_FLOP, null, article.getColorType(), 1, false);
				} catch (Exception e) {
					ActivitySubSystem.logger.error("["+this.getClass().getSimpleName()+"] [怪物死亡] [分配奖励] [创建物品失败] [" + p.getLogString() + "] [" + article.getLogString() + "]", e);
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
						ActivitySubSystem.logger.error("[怪被打死] [" + monster.getName() + "] [物品掉落] [" + (p == null ? "--" : p.getLogString()) + "] [" + article.getLogString() + "] [" + ae.getId() + "]", ex);
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

				ActivitySubSystem.logger.error("["+this.getClass().getSimpleName()+"] [怪物死亡] [分配奖励] [创建物品成功] [" + p.getLogString() + "] [" + article.getLogString() + "] [" + point.toString() + "]");
			}
		} else {
			ActivitySubSystem.logger.error("["+this.getClass().getSimpleName()+"] [怪物死亡] [分配奖励] [创建物品成功] [没有攻击者]");

		}
	}
}
