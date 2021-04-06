package com.fy.engineserver.activity.disaster.step;

import java.util.List;

import com.fy.engineserver.activity.disaster.DisasterConstant;
import com.fy.engineserver.activity.disaster.DisasterManager;
import com.fy.engineserver.activity.disaster.module.DisasterBaseModule;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.NOTICE_DISASTER_COUNTDOWN_REQ;
import com.fy.engineserver.sprite.CountdownAgent;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.AppointedAttackNPC;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
/**
 * 第一阶段
 */
public class FirstStep extends AbstractStep{

	@Override
	public void refreshNPC(Game game) {
		// TODO Auto-generated method stub
		try {
			List<Player> players = game.getPlayers();
			NOTICE_DISASTER_COUNTDOWN_REQ resp = new NOTICE_DISASTER_COUNTDOWN_REQ();
			resp.setCountdownType(CountdownAgent.COUNT_TYPE_DISASTER);
			resp.setLeftTime(300);
			resp.setDescription(Translate.空岛大冒险结束倒计时);
			for (int i=0; i<players.size(); i++) {
				players.get(i).addMessageToRightBag(resp);
			}
			DisasterBaseModule module = DisasterManager.getInst().baseModule;
			
			for (int i=0; i<module.getMonsterPoints().size(); i++) {
				Monster monster = MemoryMonsterManager.getMonsterManager().createMonster(DisasterConstant.TEMP_MONSTER_ID);
				monster.setX(module.getMonsterPoints().get(i)[0]);
				monster.setY(module.getMonsterPoints().get(i)[1]);
				monster.setBornPoint(new Point(monster.getX(), monster.getY()));
				monster.setHold(true);
				game.addSprite(monster);
				AppointedAttackNPC npc = (AppointedAttackNPC)MemoryNPCManager.getNPCManager().createNPC(DisasterConstant.TEMP_NPC_ID);
				npc.setX(DisasterConstant.TEMP_NPC_PINTS[0]);
				npc.setY(DisasterConstant.TEMP_NPC_PINTS[1]);
				npc.setBornPoint(new Point(npc.getX(), npc.getY()));
				npc.setF(monster);
				npc.face(monster.getX(), monster.getY());
				game.addSprite(npc);
			}
			for (int i=0; i<module.getMonsterPoints2().size(); i++) {
				Monster monster = MemoryMonsterManager.getMonsterManager().createMonster(DisasterConstant.TEMP_MONSTER_ID);
				monster.setX(module.getMonsterPoints2().get(i)[0]);
				monster.setY(module.getMonsterPoints2().get(i)[1]);
				monster.setBornPoint(new Point(monster.getX(), monster.getY()));
				monster.setHold(true);
				game.addSprite(monster);
				AppointedAttackNPC npc = (AppointedAttackNPC)MemoryNPCManager.getNPCManager().createNPC(DisasterConstant.TEMP_NPC_ID2);
				npc.setX(DisasterConstant.TEMP_NPC_PINTS[0]);
				npc.setY(DisasterConstant.TEMP_NPC_PINTS[1]);
				npc.setBornPoint(new Point(npc.getX(), npc.getY()));
				npc.setF(monster);
				npc.face(monster.getX(), monster.getY());
				game.addSprite(npc);
			}
			
			
			for (int i=0; i<module.getMonkeyNPCId().length; i++) {
				BossMonster m = (BossMonster) MemoryMonsterManager.getMonsterManager().createMonster(module.getMonkeyNPCId()[i]);
				m.setX(module.getMonkeyPoints().get(i)[0]);
				m.setY(module.getMonkeyPoints().get(i)[1]);
				m.setBornPoint(new Point(m.getX(), m.getY()));
				m.setMonsterLocat((byte) 2);
				m.turnOnBoss();
				m.setHold(true);
				Player[] p = game.getPlayers().toArray(new Player[0]);
				for (int j=0; j<p.length; j++) {
					m.updateDamageRecord(p[j], 100, 100);
					if (DisasterManager.logger.isDebugEnabled()) {
						DisasterManager.logger.debug("[设置怪物仇恨] [成功] [" + p[j].getLogString() + "] [" + m.getName() + "]");
					}
				}
				game.addSprite(m);
			}
			for (int i=0; i<module.getFireNPCOuterPoints().size(); i++) {
				Integer[] point = module.getFireNPCOuterPoints().get(i);
				NPC npc = MemoryNPCManager.getNPCManager().createNPC(module.getFirstStepNPCId()[1]);
				npc.setX(point[0]);
				npc.setY(point[1]);
				npc.setBornPoint(new Point(npc.getX(), npc.getY()));
				game.addSprite(npc);
			}
			
			
			if (DisasterManager.logger.isDebugEnabled()) {
				DisasterManager.logger.debug("[第一阶段刷新NPC] [成功]");
			}
		} catch (Exception e) {
			if (DisasterManager.logger.isInfoEnabled()) {
				DisasterManager.logger.info("[第一阶段刷新NPC] [异常]", e);
			}
		}
	}

}
