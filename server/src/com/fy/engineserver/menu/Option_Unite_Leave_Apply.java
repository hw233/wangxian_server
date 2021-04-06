package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.society.unite.Option_Unite_Exit;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.society.Relation;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.unite.Unite;
import com.fy.engineserver.unite.UniteManager;
import com.fy.engineserver.unite.UniteSubSystem;

/**
 * 离开结义申请
 * @author Administrator
 *
 */
public class Option_Unite_Leave_Apply extends Option{

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		Relation relation = SocialManager.getInstance().getRelationById(player.getId());
		if(relation != null){
			Unite u = UniteManager.getInstance().getUnite(relation.getUniteId());
			if(u != null){
				
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
				mw.setTitle(Translate.text_退出结义);
				mw.setDescriptionInUUB(Translate.text_您确定要和兄弟姐妹绝交吗);
				
				Option_Unite_Exit agree = new Option_Unite_Exit();
				agree.setText(Translate.text_62);
				agree.setColor(0xffffff);
				
				Option_Cancel disagree = new Option_Cancel();
				disagree.setText(Translate.text_364);
				disagree.setColor(0xffffff);
				mw.setOptions(new Option[]{agree,disagree});
				
				QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
				player.addMessageToRightBag(res);
//				UniteSubSystem.logger.info("[退出结义申请] []["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []");
				if(UniteSubSystem.logger.isInfoEnabled())
					UniteSubSystem.logger.info("[退出结义申请] [][{}] [{}] [{}] []", new Object[]{player.getId(),player.getName(),player.getUsername()});
			}else{
				player.send_HINT_REQ(Translate.你还没有结义);
			}
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
