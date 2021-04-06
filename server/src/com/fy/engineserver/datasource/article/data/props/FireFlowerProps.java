package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.ParticleData;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_PLAY_PARTICLE_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;


/**
 * 烟花道具
 *
 */
public class FireFlowerProps extends Props{

	
	private int npcId;
	private String particleName;
	private int duration;
	private int left;
	private int top;
	private int front;
	
	
	@Override
	public String canUse(Player p) {
		return super.canUse(p);
	}

	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {
		
		
		if(npcId < 0){
			//全图放烟花
			LivingObject[] los = game.getLivingObjects();
			for(LivingObject lo : los){
				if(lo instanceof Player){
					ParticleData[] particleDatas = new ParticleData[1];
					particleDatas[0] = new ParticleData(lo.getId(), this.getParticleName(),this.getDuration(), this.getTop(),this.getLeft(),this.getFront());
					NOTICE_CLIENT_PLAY_PARTICLE_RES client_PLAY_PARTICLE_RES = new NOTICE_CLIENT_PLAY_PARTICLE_RES(GameMessageFactory.nextSequnceNum(), particleDatas);
					((Player)lo).addMessageToRightBag(client_PLAY_PARTICLE_RES);
				}
			}
			ArticleManager.logger.warn("[玩家使用烟花] ["+player.getLogString()+"] [烟花:"+this.getName()+"]");
		}else{
			//招个npc  在npc处放烟花
			NPC npc = MemoryNPCManager.getNPCManager().createNPC(npcId);
			if(npc == null){
				ArticleManager.logger.error("[玩家使用烟花错误] ["+player.getLogString()+"] [烟花:"+this.getName()+"] [没有指定npc:"+npcId+"]");
				return false;
			}else{
				npc.setX(player.getX());
				npc.setY(player.getY());
				game.addSprite(npc);
				npc.setDeadLastingTime(120*1000);
				npc.setHp(-1);
				ArticleManager.logger.warn("[玩家使用烟花] ["+player.getLogString()+"] [烟花:"+this.getName()+"]");
			}
		}
		
		
		return true;
	}

	public long getNpcId() {
		return npcId;
	}

	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	public String getParticleName() {
		return particleName;
	}

	public void setParticleName(String particleName) {
		this.particleName = particleName;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getFront() {
		return front;
	}

	public void setFront(int front) {
		this.front = front;
	}
}
