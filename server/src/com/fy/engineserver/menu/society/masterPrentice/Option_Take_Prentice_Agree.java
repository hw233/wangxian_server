package com.fy.engineserver.menu.society.masterPrentice;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.masterAndPrentice.MasterPrenticeManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;

/**
 * 师傅发送收徒请求,徒弟同意
 * @author Administrator
 *
 */
public class Option_Take_Prentice_Agree extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_师徒;
	}
	
	private Player master;
	
	public Player getMaster() {
		return master;
	}
	public void setMaster(Player master) {
		this.master = master;
	}
	@Override
	public void doSelect(Game game, Player player) {
		try {
			String result = MasterPrenticeManager.getInstance().takeMasterConfirm(player, master);
			if(result.equals("")){
				master.send_HINT_REQ(Translate.徒弟添加成功);
				player.send_HINT_REQ(Translate.师傅添加成功);
			}else{
				player.sendError(result);
			}
		} catch (Exception e) {
			if(MasterPrenticeManager.logger.isWarnEnabled())
				MasterPrenticeManager.logger.warn("[徒弟同意师傅异常] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []",e);
		}
			
	}
	
}
