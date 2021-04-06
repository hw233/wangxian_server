package com.fy.engineserver.menu.transitrobbery;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.TransitRobbery.Robbery.BaseRobbery;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;

public class Option_HuiCheng extends Option{
	private BaseRobbery robbery;
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		endRobbery(player);
	}
	
	public void endRobbery(Player player){
		if(robbery == null){
			return;
		}
		TransitRobberyEntityManager.getInstance().removeFromRobbering(player.getId());
		TransitRobberyManager.getInstance().removeRobbery(robbery);			//渡劫完成直接剔除心跳
		Player p = null;
		try {
			p = GamePlayerManager.getInstance().getPlayer(player.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			TransitRobberyManager.logger.error("[渡劫结束取player错误][ " + player.getId() + "]");
		}
		回城(p, robbery.game);
	}
	
	private void 回城(Player p, Game game){
		try{
			Game currentGame = p.getCurrentGame();
			if(currentGame != null && !currentGame.gi.name.equals(game.gi.name)){
				TransitRobberyManager.logger.info("[玩家已经不再这个区域了，不需要在回程了]");
				return;
			}
			String mapName = p.getResurrectionMapName();
			int x = p.getResurrectionX();
			int y = p.getResurrectionY();
			if(!p.isOnline()){
				mapName = "kunhuagucheng";
			}
			game.transferGame(p, new TransportData(0, 0, 0, 0, mapName, x, y), true);
			TransitRobberyManager.logger.warn("[渡劫结束] [回城] [成功] ["+p.getLogString()+"]");
		}catch(Exception e){
			TransitRobberyManager.logger.warn("[渡劫结束] [回城] [异常]"+e.getMessage());
		}
	}

	public BaseRobbery getRobbery() {
		return robbery;
	}

	public void setRobbery(BaseRobbery robbery) {
		this.robbery = robbery;
	}
}
