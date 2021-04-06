package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.FireNpc;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.npc.NPCManager;
import com.fy.engineserver.sprite.npc.TimeNpc;

/**
 * 召唤NPC道具，根据名字创建NPC，所以必须保证服务器上这种名称的NPC
 *
 */
public class NPCProps extends Props{

	/**
	 * NPC名字
	 */
	private String npcName;

	public String getNpcName() {
		return npcName;
	}

	public void setNpcName(String npcName) {
		this.npcName = npcName;
	}

	/**
	 * 使用道具
	 * @param player
	 */
	public boolean use(Game game,Player player, ArticleEntity ae){
		if(!super.use(game,player,ae)){
			return false;
		}
		if(npcName == null){
			return false;
		}
		if(game != null && game.getGameInfo() != null){
			NPCManager nm = MemoryNPCManager.getNPCManager();
			if(nm != null){
				MemoryNPCManager.NPCTempalte npctemp = null;
				MemoryNPCManager.NPCTempalte[] npctemplates = ((MemoryNPCManager)nm).getNPCTemaplates();
				if(npctemplates != null){
					for(MemoryNPCManager.NPCTempalte npctemplate : npctemplates){
						if(npctemplate != null && npctemplate.npc != null && npcName.equals(npctemplate.npc.getName())){
							npctemp = npctemplate;
							break;
						}
					}
				}
				if(npctemp != null){
					NPC npc = ((MemoryNPCManager)nm).createNPC(npctemp.NPCCategoryId);
					if(npc instanceof TimeNpc || npc instanceof FireNpc){
						npc.setX(player.getX());
						npc.setY(player.getY());
						npc.setAlive(true);
						npc.setGameNames(game.getGameInfo());
						game.addSprite(npc);
						if(ArticleManager.logger.isDebugEnabled()){
//							ArticleManager.logger.debug("[使用道具] [NPC召唤] [成功] [位置:"+game.getGameInfo().getName()+"("+player.getX()+","+player.getY()+")"+"][user]["+player.getUsername()+"]["+player.getId()+"]["+player.getName()+"] ["+this.getName()+"] ["+getComment()+"][npcName]["+this.npcName+"]");
							if(ArticleManager.logger.isDebugEnabled())
								ArticleManager.logger.debug("[使用道具] [NPC召唤] [成功] [位置:{}({},{}){}][{}][{}] [{}] [{}][npcName][{}]", new Object[]{game.getGameInfo().getName(),player.getX(),player.getY(),"][user]["+player.getUsername(),player.getId(),player.getName(),this.getName(),getComment(),this.npcName});
						}
					}else{
						if(ArticleManager.logger.isDebugEnabled()){
//							ArticleManager.logger.debug("[使用道具] [NPC召唤] [失败][NPC不是TimeNpc] [user]["+player.getUsername()+"]["+player.getId()+"]["+player.getName()+"] ["+this.getName()+"] ["+getComment()+"][npcName]["+this.npcName+"]");
							if(ArticleManager.logger.isDebugEnabled())
								ArticleManager.logger.debug("[使用道具] [NPC召唤] [失败][NPC不是TimeNpc] [user][{}][{}][{}] [{}] [{}][npcName][{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),this.getName(),getComment(),this.npcName});
						}
						return false;
					}
				}else{
					if(ArticleManager.logger.isDebugEnabled()){
//						ArticleManager.logger.debug("[使用道具] [NPC召唤] [失败][NPCTemplate为空] [user]["+player.getUsername()+"]["+player.getId()+"]["+player.getName()+"] ["+this.getName()+"] ["+getComment()+"][npcName]["+this.npcName+"]");
						if(ArticleManager.logger.isDebugEnabled())
							ArticleManager.logger.debug("[使用道具] [NPC召唤] [失败][NPCTemplate为空] [user][{}][{}][{}] [{}] [{}][npcName][{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),this.getName(),getComment(),this.npcName});
					}
					return false;
				}
			}else{
				if(ArticleManager.logger.isDebugEnabled()){
//					ArticleManager.logger.debug("[使用道具] [NPC召唤] [失败][NPCManager为空] [user]["+player.getUsername()+"]["+player.getId()+"]["+player.getName()+"] ["+this.getName()+"] ["+getComment()+"][npcName]["+this.npcName+"]");
					if(ArticleManager.logger.isDebugEnabled())
						ArticleManager.logger.debug("[使用道具] [NPC召唤] [失败][NPCManager为空] [user][{}][{}][{}] [{}] [{}][npcName][{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),this.getName(),getComment(),this.npcName});
				}
				return false;
			}
		}else{
			if(ArticleManager.logger.isDebugEnabled()){
//				ArticleManager.logger.debug("[使用道具] [NPC召唤] [失败][game为空或gameInfo为空] [user]["+player.getUsername()+"]["+player.getId()+"]["+player.getName()+"] ["+this.getName()+"] ["+getComment()+"][npcName]["+this.npcName+"]");
				if(ArticleManager.logger.isDebugEnabled())
					ArticleManager.logger.debug("[使用道具] [NPC召唤] [失败][game为空或gameInfo为空] [user][{}][{}][{}] [{}] [{}][npcName][{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),this.getName(),getComment(),this.npcName});
			}
			return false;
		}
		return true;
	}
	/**
	 * 判断某个玩家是否可以使用此物品 子类可以重载此方法
	 * 返回null表示可以使用
	 * 返回字符串表示不能使用
	 * 字符串为不能使用的详细信息
	 * @param p
	 * @return
	 */
	public String canUse(Player p) {
		
		String useFlag = super.canUse(p);
		return useFlag;
	}
}
