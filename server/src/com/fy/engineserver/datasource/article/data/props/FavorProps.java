package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.sprite.Player;

/**
 * 配方
 * 
 * 装备合成系统采用的配方
 * 
 * 配方包含如下的数据结构：
 * 
 * 需要的材料，及其数量 * 产生的物品
 * 
 *
 */
public class FavorProps extends Props{

	private long favorPoint;

	public long getFavorPoint() {
		return favorPoint;
	}

	public void setFavorPoint(long favorPoint) {
		this.favorPoint = favorPoint;
	}

	/**
	 * 使用道具
	 * 
	 * @param player
	 */
	public boolean use(Game game, Player player, ArticleEntity ae) {
//		GameServerManager gsm = GameServerManager.getInstance();
//		if(gsm != null && gsm.isCrossServer(GameConstants.getInstance().getServerName())){
//			HINT_REQ error = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.translate.text_2994+ae.getArticleName());
//			player.addMessageToRightBag(error);
//			ArticleManager.logger.warn("[使用道具] [失败] [user:"+player.getUsername()+"] ["+player.getName()+"] [playerId:"+player.getId()+"] ["+ae.getArticleName()+"] [articleId:"+ae.getId()+"] [在跨服服务器，不能使用");
//			return false;
//		}
//		if(!super.use(game,player,ae)){
//			return false;
//		}
//		if(ae != null){
//			List<Long> friends = player.getFriendlist();
//			WindowManager mm = WindowManager.getInstance();
//			MenuWindow mw = mm.createTempMenuWindow(600);
//			mw.setDescriptionInUUB(Translate.translate.text_3693);
//			mw.setTitle(Translate.translate.text_3694);
//			List<Option> optionList = new ArrayList<Option>();
//			Option cancelOption = new Option_UseCancel();
//			cancelOption.setColor(0xFFFFFF);
//			cancelOption.setText(Translate.translate.text_364);
//			int index = player.getKnapsack().indexOf(ae);
//			if(friends != null){
//				PlayerManager pm = PlayerManager.getInstance();
//				if(pm != null){
//					for(Long id : friends){
//						Player p = null;
//						try {
//							p = pm.getPlayer(id);
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						if(p != null){
//							Option_UseFavorProps op = new Option_UseFavorProps();
//							op.setFavorPlayerId(id);
//							op.setIconId(155);
//							op.setColor(0xFFFFFF);
//							op.setIndexOfKnapsack(index);
//							op.setOwner(player);
//							op.setText(p.getName());
//							optionList.add(op);
//						}
//					}	
//				}
//				
//			}
//			optionList.add(cancelOption);
//			mw.setOptions(optionList.toArray(new Option[0]));
//			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(),mw,mw.getOptions());
//			player.addMessageToRightBag(res);
//			ArticleManager.logger.warn("[友好度道具使用] [成功] ["+name+"] [user:"+player.getUsername()+"] [playerId:"+player.getId()+"] [player:"+player.getName()+"] [物品id:"+ae.getId()+"]");
//		}else{
//			ArticleManager.logger.warn("[友好度道具使用] [失败] ["+name+"没有对应的实体] [user:"+player.getUsername()+"] [playerId:"+player.getId()+"] [player:"+player.getName()+"]");
//			return false;
//		}
		return true;
	}

	public String canUse(Player p) {
		
		return super.canUse(p);
	}
}
