package com.fy.engineserver.activity.disaster.step;

import java.util.Random;

import com.fy.engineserver.activity.disaster.DisasterConstant;
import com.fy.engineserver.activity.disaster.DisasterManager;
import com.fy.engineserver.activity.disaster.module.DisasterBaseModule;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.AppointedAttackNPC;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
/**
 * 第三阶段
 */
public class ThirdStep extends AbstractStep{
	
	public static Random ran = new Random(System.currentTimeMillis());

	@Override
	public void refreshNPC(Game game) {
		// TODO Auto-generated method stub
		try {
			game.removeAllMonster(DisasterConstant.TEMP_MONSTER_ID);
			game.removeAllAppNpc();
			
			DisasterBaseModule module = DisasterManager.getInst().baseModule;
			
			for (int i=0; i<module.getTempNpcIds().length; i++) {
				Monster monster = MemoryMonsterManager.getMonsterManager().createMonster(DisasterConstant.TEMP_MONSTER_ID);
				monster.setX(module.getSecondMonsterPoints().get(i)[0]);
				monster.setY(module.getSecondMonsterPoints().get(i)[1]);
				monster.setBornPoint(new Point(monster.getX(), monster.getY()));
				monster.setHold(true);
				game.addSprite(monster);
				AppointedAttackNPC npc = (AppointedAttackNPC)MemoryNPCManager.getNPCManager().createNPC(module.getTempNpcIds()[i]);
				npc.setX(DisasterConstant.TEMP_NPC_PINTS[0]);
				npc.setY(DisasterConstant.TEMP_NPC_PINTS[1]);
				npc.setBornPoint(new Point(npc.getX(), npc.getY()));
				npc.setF(monster);
				npc.face(monster.getX(), monster.getY());
				game.addSprite(npc);
			}
			for (int i=0; i<module.getFireNPCPoints().size(); i++) {
				Integer[] point = module.getFireNPCPoints().get(i);
				NPC npc = MemoryNPCManager.getNPCManager().createNPC(module.getThirdStepNPCId()[0]);
				npc.setX(point[0]);
				npc.setY(point[1]);
				npc.setBornPoint(new Point(npc.getX(), npc.getY()));
				game.addSprite(npc);
			}
			for (int i=0; i<module.getFireNPCOuterPoints().size(); i++) {
				Integer[] point = module.getFireNPCOuterPoints().get(i);
				NPC npc = MemoryNPCManager.getNPCManager().createNPC(module.getThirdStepNPCId()[1]);
				npc.setX(point[0]);
				npc.setY(point[1]);
				npc.setBornPoint(new Point(npc.getX(), npc.getY()));
				game.addSprite(npc);
			}
		} catch (Exception e) {
			if (DisasterManager.logger.isInfoEnabled()) {
				DisasterManager.logger.info("[第三阶段刷新NPC] [异常]", e);
			}
		}
	}

}
