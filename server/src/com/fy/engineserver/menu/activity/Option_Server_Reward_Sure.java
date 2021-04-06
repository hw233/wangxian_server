package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.godDown.GodDwonManager;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedRecordNPC;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.jiazu.Option_Jiazu_CallIn;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 服务器维护，领取物品
 * @author Administrator
 *
 */
public class Option_Server_Reward_Sure extends Option implements NeedRecordNPC{
	
	private NPC npc;
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		
		if(ActivityManagers.getInstance().getDdc().get(player.getId()+"用于服务器维护发送补偿桃源")==null){
//			String servernames [] = {"霹雳霞光","游龙引凤","福地洞天","黄金海岸","昆华古城","昆仑圣殿","国内本地测试"};
			String servernames [] = {"桃源仙境","国内本地测试"};
			GameConstants gc = GameConstants.getInstance();
			if(gc!=null){
				String servername = gc.getServerName();
				if(servername!=null){
					for(String name:servernames){
						if(servername.equals(name)){
							Article a = ArticleManager.getInstance().getArticle("美酒锦囊");
							if(a!=null){
								try {
									ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.CREATE_REASON_ACHIEVEMENT, player, a.getColorType(), 5, true);
									if(ae!=null){
										player.putToKnapsacks(ae, 5,"用于服务器维护发送补偿桃源");
										player.sendError("恭喜您获得了美酒锦囊*5");
										ActivityManagers.getInstance().getDdc().put(player.getId()+"用于服务器维护发送补偿桃源", 1);
									}
								} catch (Exception e) {
									e.printStackTrace();
									ActivityManagers.logger.warn("[用于服务器维护发送补偿桃源] [创建物品] 【异常】 [美酒锦囊*5] []",e);
								}
								
							}else{
								ActivityManagers.logger.warn("[用于服务器维护发送补偿桃源] [物品不存在] [美酒锦囊*5] []");
								break;
							}
						}
					}
				}
			}
		}else{
			player.sendError("您已经领取过啦！");
		}
	}
	@Override
	public NPC getNPC() {
		return npc;
	}

	@Override
	public void setNPC(NPC npc) {
		this.npc = npc;
	}
	
	
}


