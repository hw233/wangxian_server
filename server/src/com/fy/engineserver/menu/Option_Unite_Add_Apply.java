package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.society.Relation;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.unite.UniteManager;

/**
 * 申请加入结义申请
 * @author Administrator
 *
 */
public class Option_Unite_Add_Apply extends Option{

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		
		try {
			Relation re = SocialManager.getInstance().getRelationById(player.getId());
			if(re.getUniteId() != -1){
				player.send_HINT_REQ(Translate.text_你已经有结义);
			}
			String result = UniteManager.getInstance().addToUniteCheck(player);
			if(!result.equals("")){
				player.send_HINT_REQ(result);
//				UniteManager.logger.warn("[加入结义申请失败] ["+result+"] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []");
				if(UniteManager.logger.isWarnEnabled())
					UniteManager.logger.warn("[加入结义申请失败] [{}] [{}] [{}] [{}] []", new Object[]{result,player.getId(),player.getName(),player.getUsername()});
			}
		} catch (Exception e) {
			if(UniteManager.logger.isWarnEnabled())
				UniteManager.logger.warn("[加入结义异常] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []",e);
		}
		
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
