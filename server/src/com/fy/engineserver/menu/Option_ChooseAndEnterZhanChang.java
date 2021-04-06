package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

public class Option_ChooseAndEnterZhanChang extends Option {
	String zhanChangName;
	byte side;
	public String getZhanChangName() {
		return zhanChangName;
	}

	public void setZhanChangName(String zhanChangName) {
		this.zhanChangName = zhanChangName;
	}
	
	
	public byte getSide() {
		return side;
	}

	public void setSide(byte side) {
		this.side = side;
	}

	public void doSelect(Game game, Player player) {
//		BattleFieldManager dfm = BattleFieldManager.getInstance();
//		
//		BattleFieldInfo info = dfm.getBattleFieldInfoByName(zhanChangName);
//		
//		DotaBattleField df = dfm.getBattleField(zhanChangName);
//		if(df == null){
//			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,"服务器配置错误，战场 不存在!!!");
//			player.addMessageToRightBag(hreq);
//			return;
//		}
//		
//		if(side != 1 && side != 2){
//			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,"玩家没有选择战场归属方!!!");
//			player.addMessageToRightBag(hreq);
//			return;
//		}
//		
//		if(!df.isOpen()){
//			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,"战场没有开启!!!");
//			player.addMessageToRightBag(hreq);
//			return;
//		}
//		
//		if(df.getPlayersBySide(side).length >= DotaBattleField.PLAYER_NUM_LIMIT[df.getBattleType()]){
//			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,"战场已满，请稍候再试!!!");
//			player.addMessageToRightBag(hreq);
//			return;
//		}
//		
//		Point p = null;
//		
//		if(side == 1){
//			//A方
//			p = df.getRevivedPointForA();
//		}else if(side == 2){
//			//B方
//			p = df.getRevivedPointForB();
//		}
//		
//		if(p == null){
//			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)1,"服务器配置错误，无法获得进入战场位置!!!");
//			player.addMessageToRightBag(hreq);
//			return;				
//		}
//		
//		player.setBattleFieldSide(side);
//		player.setBattleField(df);
//		
//		try {
//			TransportData td = new TransportData(0,0,0,0,df.getGame().gi.getName(), p.x ,p.y);
//			game.transferGame(player, td);
//		} catch (Exception e) {
//			e.printStackTrace();
//			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,"传送到战场失败，服务器错误！！！");
//			player.addMessageToRightBag(hreq);
//		}
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_进入战场;
	}
}
