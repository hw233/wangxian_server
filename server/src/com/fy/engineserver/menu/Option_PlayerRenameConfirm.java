/**
 * 
 */
package com.fy.engineserver.menu;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.SpriteSubSystem;
import com.xuanzhi.tools.text.WordFilter;

/**
 * @author Administrator
 *
 */
public class Option_PlayerRenameConfirm extends Option {
	String name;
	/**
	 * 
	 */
	public Option_PlayerRenameConfirm(String name) {
		// TODO Auto-generated constructor stub
		this.name=name;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		Player p=null;
		HINT_REQ req;
		if(player.getName().endsWith(Translate.text_5231)||player.getName().endsWith(Translate.text_5232)||player.getName().endsWith(Translate.text_5233)||player.getName().endsWith(Translate.text_5234)){
			try{
				p=PlayerManager.getInstance().getPlayer(this.name);
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if(p==null){
				WordFilter filter = WordFilter.getInstance();
				SpriteSubSystem sss=SpriteSubSystem.getInstance();
				boolean valid = false;
				valid = this.name.getBytes().length <= 14 && filter.cvalid(name, 0) && filter.evalid(name, 1) && sss.prefixValid(name) && sss.tagValid(name) && sss.gmValid(name);
				if(valid){
					String oldName=player.getName();
					PlayerManager.getInstance().updatePlayerName(player, this.name);
					player.setDirty(true,"name");
					req=new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5355);
					player.addMessageToRightBag(req);
//					Logger log = Logger.getLogger("com.fy.engineserver.sprite");
					Logger log = LoggerFactory.getLogger("com.fy.engineserver.sprite");
					if(log!=null){
//						log.warn("[修改姓名] [成功] [玩家ID："+player.getId()+"] [原名："+oldName+"] [新名："+player.getName()+"]");
						if(log.isWarnEnabled())
							log.warn("[修改姓名] [成功] [玩家ID：{}] [原名：{}] [新名：{}]", new Object[]{player.getId(),oldName,player.getName()});
					}
					
				}else{
					req=new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5356);
					player.addMessageToRightBag(req);
				}
			}else{
				req=new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5242);
				player.addMessageToRightBag(req);
			}
		}else{
			req=new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5354);
			player.addMessageToRightBag(req);
		}
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_PLAYER_RENAME_CONFIRM;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_4927 ;
	}

}
