package com.fy.engineserver.menu.society.zongpai;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.zongzu.ZongPaiSubSystem;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;

/**
 * 
 * @author Administrator
 *
 */
public class Option_Zongpai_Update extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doInput(Game game, Player player, String inputContent) {
		
		try {
			ZongPai zp = ZongPaiManager.getInstance().getZongPaiByPlayerId(player.getId());
			if(zp != null){
				if(zp.getDeclaration().equals(inputContent)){
					player.send_HINT_REQ(Translate.俩次一样确认这样);
				}else{
					ZongPaiManager.getInstance().updateDeclaration(player, inputContent);
					if(ZongPaiSubSystem.logger.isWarnEnabled()){
						ZongPaiSubSystem.logger.warn("[修改宗派宗旨成功] ["+player.getLogString()+"] [zp:"+zp.getLogString()+"]");
					}
				}
			}
		} catch (Exception e) {
			ZongPaiSubSystem.logger.error("[修改宗派宗旨异常] ["+player.getLogString()+"]",e);
		}
	}

	
	
	
	
}
