package com.fy.engineserver.menu.society.zongpai;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.zongzu.ZongPaiSubSystem;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;

/**
 * 
 * @author Administrator
 *
 */
public class Option_Zongpai_Dismiss_Inputpassword extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_结义;
	}
	
	@Override
	public void doInput(Game game, Player player, String inputContent) {
		
		try {
			ZongPai zp = ZongPaiManager.getInstance().getZongPaiByPlayerId(player.getId());
			if(zp != null){
				if(zp.getPassword().equals(inputContent.trim())){
					
					ZongPaiManager zongPaiManager = ZongPaiManager.getInstance();
					zongPaiManager.dismissZongPaiConfirm(player);
				}else{
					player.send_HINT_REQ(Translate.宗派密码不正确);
				}
			}
		} catch (Exception e) {
			ZongPaiSubSystem.logger.error("[解散宗派失败] [] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []",e);
		}
	}

	
	
	
	
}
