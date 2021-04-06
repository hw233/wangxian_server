package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.articleEnchant.EnchantEntityManager;
import com.fy.engineserver.articleEnchant.EnchantManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.MoveTrace;
import com.fy.engineserver.core.MoveTrace4Client;
import com.fy.engineserver.core.g2d.Graphics2DUtil;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.g2d.Road;
import com.fy.engineserver.core.g2d.SignPost;
import com.fy.engineserver.datasource.skill.master.SkEnhanceManager;
import com.fy.engineserver.datasource.skill.passivetrigger.PassiveTriggerImmune;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.SPECIAL_SKILL_MOVETRACE_REQ;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.monster.Monster;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 将目标瞬间拉向自己，并使目标晕眩
 * 
 * 
 * 
 * 
 */
@SimpleEmbeddable
public class Buff_ZhuaRenAndXuanYun extends Buff{

	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner) {
		if (owner instanceof Player) {
			Player p = (Player) owner;
			if (p.getCurrentGame() != null) {
				pathFinding(p.getCurrentGame(), p, this.getCauser().getX(), this.getCauser().getY(), 1000);
			}
			try {
				mianyiStun = p.checkPassiveEnchant(EnchantEntityManager.受到控制类技能) == 100;
			} catch (Exception e) {
				EnchantManager.logger.warn("["+this.getClass().getSimpleName()+"] [检测玩家附魔效果] [异常] [" + p.getLogString() + "]");
			}
				long decreaseTime = (this.getInvalidTime() - this.getStartTime()) * p.decreaseConTimeRate / 1000;
				this.setInvalidTime(this.getInvalidTime() - decreaseTime);
				EnchantEntityManager.instance.notifyCheckEnchant(p);
		} else if (owner instanceof Monster) {
			if (!(owner instanceof BossMonster)) {
				Monster p = (Monster) owner;
				Game game = GameManager.getInstance().getGameByName(p.getGameName(), 1);
				if (game == null) {
					game = GameManager.getInstance().getGameByName(p.getGameName(), 0);
				}
				if (game != null) {
					pathFinding(game, p, this.getCauser().getX(), this.getCauser().getY(), 1000);
				}
			}
		}
	}

	protected void pathFinding(Game de, LivingObject sprite, int dx, int dy, int speed) {
		int distance = 20;
		Point s = new Point(sprite.getX(), sprite.getY());
		Point e = new Point(dx, dy);
		int L = Graphics2DUtil.distance(s, e);
		if (L > distance && de.getGameInfo().navigator.isVisiable(s.x, s.y, e.x, e.y)) {
			int L1 = L - distance;
			int L2 = distance;

			Point ps[] = new Point[2];
			short roadLen[] = new short[1];
			ps[0] = new Point(s.x, s.y);
			ps[1] = new Point((L1 * e.x + L2 * s.x) / L, (L1 * e.y + L2 * s.y) / L);
			roadLen[0] =  (short)L1;
			MoveTrace path = new MoveTrace(roadLen, ps, (long) (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + (long) L1 * 1000 / speed));
			sprite.setMoveTrace(path);
			try{
				if(sprite instanceof Player){
					MoveTrace4Client moveTrace4Client = path.getLeftPath();
					moveTrace4Client.setSpeed(speed);
					SPECIAL_SKILL_MOVETRACE_REQ req = new SPECIAL_SKILL_MOVETRACE_REQ(GameMessageFactory.nextSequnceNum(), moveTrace4Client);
					((Player)sprite).addMessageToRightBag(req);
				}
			}catch(Exception ex){
				ex.printStackTrace();
				Game.logger.error("[{}] [{}]", new Object[]{sprite.getClassType(),sprite.getId()},ex);
			}
		} else if (L > distance) {
			SignPost sps[] = de.getGameInfo().navigator.findPath(s, e);
			if (sps == null) return;
			Point ps[] = new Point[sps.length + 1];
			short roadLen[] = new short[sps.length];
			for (int i = 0; i < ps.length; i++) {
				if (i == 0) {
					ps[i] = new Point(s.x, s.y);
				} else {
					ps[i] = new Point(sps[i - 1].x, sps[i - 1].y);
				}
			}
			int totalLen = 0;
			for (int i = 0; i < roadLen.length; i++) {
				if (i == 0) {
					roadLen[i] = (short) Graphics2DUtil.distance(ps[0], ps[1]);
				} else {
					Road r = de.getGameInfo().navigator.getRoadBySignPost(sps[i - 1].id, sps[i].id);
					if (r != null) {
						roadLen[i] = r.len;
					} else {
						roadLen[i] = (short) Graphics2DUtil.distance(ps[i], ps[i + 1]);
					}
				}
				totalLen += roadLen[i];
			}
			MoveTrace path = new MoveTrace(roadLen, ps, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + totalLen * 1000 / speed);
			sprite.setMoveTrace(path);
			try{
				if(sprite instanceof Player){
					MoveTrace4Client moveTrace4Client = path.getLeftPath();
					moveTrace4Client.setSpeed(speed);
					SPECIAL_SKILL_MOVETRACE_REQ req = new SPECIAL_SKILL_MOVETRACE_REQ(GameMessageFactory.nextSequnceNum(), moveTrace4Client);
					((Player)sprite).addMessageToRightBag(req);
				}
			}catch(Exception ex){
				ex.printStackTrace();
				Game.logger.error("[{}] [{}]", new Object[]{sprite.getClassType(),sprite.getId()},ex);
			}
		}
	}
	
	private transient boolean mianyiStun = false;

	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner) {
		if (owner instanceof Player) {
			Player p = (Player) owner;
			p.setStun(false);
		} else if (owner instanceof Sprite) {
			Sprite p = (Sprite) owner;
			p.setStun(false);
		}
	}

	long startTime;
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner, long heartBeatStartTime, long interval, Game game) {
		super.heartbeat(owner, heartBeatStartTime, interval, game);
		if(startTime == 0){
			startTime = heartBeatStartTime;
		}
		if (owner instanceof Player) {
			Player p = (Player) owner;
			if (!p.isStun() && p.getImmuType() != PassiveTriggerImmune.免疫晕眩 && !mianyiStun){// && p.getMoveTrace() == null) {
				p.setStun(true);
				SkEnhanceManager.getInst().addSkillBuff(p);
			}
			if(startTime > heartBeatStartTime + 1000 && !p.isStun() && p.getImmuType() != PassiveTriggerImmune.免疫晕眩 && !mianyiStun){
				p.setStun(true);
				SkEnhanceManager.getInst().addSkillBuff(p);
			}
		} else if (owner instanceof Sprite) {
			Sprite p = (Sprite) owner;
			if (!(owner instanceof BossMonster)) {
				if (!p.isStun()){// && p.getMoveTrace() == null) {
					p.setStun(true);
				}
				if(startTime > heartBeatStartTime + 1000 && !p.isStun()){
					p.setStun(true);
				}
			}
		}
	}

}
