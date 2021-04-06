package com.fy.engineserver.menu.society.unite;

import java.util.List;

import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.society.Relation;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.unite.Unite;
import com.fy.engineserver.unite.UniteManager;

public class Option_Unite_zhaoji extends Option {

	private Player invite;
	private String articleName;
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doInput(Game game, Player player, String inputContent) {
	
		Relation relation = SocialManager.getInstance().getRelationById(player.getId());
		if(relation.getUniteId() > 0){
			Unite u = UniteManager.getInstance().getUnite(relation.getUniteId());
			if(u != null){
				boolean isXianjie = false;
				try {
					String mapname = player.getCurrentGame().gi.name;
					isXianjie = RobberyConstant.没飞升玩家不可进入的地图.contains(mapname);
				} catch (Exception e) {
					
				}
				PlayerManager pm = PlayerManager.getInstance();
				List<Long> memberIds = u.getMemberIds();
				
				// 删除兄弟令
				ArticleEntity ae = player.getArticleEntity(articleName);
				if(ae == null){
					player.sendError(Translate.text_你包里没有兄弟令);
					SocialManager.logger.error("[使用兄弟令] ["+player.getLogString()+"] [包里没有兄弟令]");
					return ;
				}
				player.removeFromKnapsacks(ae,Translate.兄弟令, true);
				ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte)0, 1, "使用兄弟令", null);
				for(long id: memberIds){
					if(id == player.getId()){
						continue;
					}
					if (!GamePlayerManager.getInstance().isOnline(id)) {
						continue;
					}
					try {
						WindowManager windowManager = WindowManager.getInstance();
						MenuWindow mw = windowManager.createTempMenuWindow(600);
						mw.setTitle(articleName);
//						mw.setDescriptionInUUB(invite.getName()+Translate.使用了兄弟令需要把你召唤到他身边+inputContent);
						mw.setDescriptionInUUB(Translate.translateString(Translate.xx使用了兄弟令需要把你召唤到他身边xx, new String[][]{{Translate.STRING_1,invite.getName()},{Translate.STRING_2,inputContent},{Translate.STRING_3,articleName}}));
						
						Option_Unite_zhaoji_Agree agree = new Option_Unite_zhaoji_Agree();
						agree.setInviteId(player.getId());
						agree.setText(Translate.确定);
						Option_Unite_zhaoji_DisAgree disAgree = new Option_Unite_zhaoji_DisAgree();
						disAgree.setInviteId(player.getId());
						disAgree.setText(Translate.取消);
						mw.setOptions(new Option[] { agree, disAgree });
						
						QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
						
						Player invited = pm.getPlayer(id);
						if (isXianjie && invited.getLevel() <= 220) {
							continue;
						}
						invited.addMessageToRightBag(res);
					} catch (Exception e) {
						UniteManager.logger.error("[使用结义令异常] ["+player.getLogString()+"]",e);
					}
				}
				
				player.sendError(Translate.translateString(Translate.使用兄弟令完成请等待他们的到来, new String[][]{{Translate.STRING_1,articleName}}));
				if (UniteManager.logger.isWarnEnabled()){
					UniteManager.logger.warn("[使用结义令完成] ["+player.getLogString()+"]");
				}
				
			}else{
				player.sendError(Translate.你还没有结义);
				SocialManager.logger.error("[使用兄弟令] ["+player.getLogString()+"] [你还没有结义]");
			}
		}else{
			player.sendError(Translate.你还没有结义);
			SocialManager.logger.error("[使用兄弟令] ["+player.getLogString()+"] [你还没有结义]");
		}
			
		
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public Player getInvite() {
		return invite;
	}

	public void setInvite(Player invite) {
		this.invite = invite;
	}
	
	
}
