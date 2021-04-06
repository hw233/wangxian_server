package com.fy.engineserver.menu.society.masterPrentice;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.masterAndPrentice.MasterPrenticeManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;

/**
 * 徒弟发送拜师请求,师傅同意
 * @author Administrator
 *
 */
public class Option_Take_Master_Agree extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_师徒;
	}
	
	private Player prentice;
	public Player getPrentice() {
		return prentice;
	}
	public void setPrentice(Player prentice) {
		this.prentice = prentice;
	}
	@Override
	public void doSelect(Game game, Player player) {
		try {
			String result = MasterPrenticeManager.getInstance().takePrenticeConfirm(player, prentice);
			if(result.equals("")){
				prentice.sendError(Translate.师傅添加成功);
				player.sendError(Translate.徒弟添加成功);
			}else{
//			prentice.send_HINT_REQ(result);
				player.sendError(result);
			}
		} catch (Exception e) {
			if(MasterPrenticeManager.logger.isWarnEnabled())
				MasterPrenticeManager.logger.warn("[师傅同意拜师异常] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []",e);
		}
	}
	
}
