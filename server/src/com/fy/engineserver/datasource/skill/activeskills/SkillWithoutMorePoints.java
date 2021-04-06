package com.fy.engineserver.datasource.skill.activeskills;

import java.util.Arrays;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.ActiveSkillEntity;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.npc.FireWallNPC;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPCManager;

public class SkillWithoutMorePoints  extends ActiveSkill implements Cloneable{

	
	public int[][] points = new int[0][];
	public int npcNums;
	
	@Override
	public void run(ActiveSkillEntity skillEntity, Fighter target, Game game, int level, int effectIndex, int targetX, int targetY, byte direction) {
		// TODO Auto-generated method stub
		if(logger.isInfoEnabled()){
			logger.info("[执行技能攻击] [SkillWithoutMorePoints] [{}] [Lv:{}] [{}] [npcNums:{}] [points:{}] [火圈] [直接进行命中计算，作用于范围内的某个目标]", new Object[]{this.getName(),level,skillEntity.getOwner().getName(),npcNums,Arrays.toString(points)});
		}
		for(int i = 0 ; i < npcNums ; i++){
			if(i<npcNums){
				int[]xys = points[i];
				try{
					NPCManager nm = MemoryNPCManager.getNPCManager();
					FireWallNPC fwn = (FireWallNPC)nm.createNPC(2000000);
					fwn.setOwner(skillEntity.getOwner());
					fwn.setRange(50);
					fwn.setInValidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 30*1000);
					fwn.setModulus(100);
					fwn.setX(xys[0]);
					fwn.setY(xys[1]);
					game.addSprite(fwn);
					if(logger.isInfoEnabled()){
						logger.info("[执行技能攻击] [SkillWithoutMorePoints] [{}] [Lv:{}] [{}] [{}] [{}] [{}] [{}] [火圈] [直接进行命中计算，作用于范围内的某个目标]", new Object[]{this.getName(),level,skillEntity.getOwner().getName(),fwn.getName(),fwn.isAlive(),fwn.getX(),fwn.getY()});
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
	}

	@Override
	public int check(Fighter caster, Fighter target, int level) {
		return 0;
	}

	@Override
	public short[] getMp() {
		return new short[0];
	}

}
