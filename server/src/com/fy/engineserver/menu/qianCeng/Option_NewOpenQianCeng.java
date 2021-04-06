package com.fy.engineserver.menu.qianCeng;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.qiancengta.QianCengTaManager;
import com.fy.engineserver.sprite.Player;

//打开千层塔界面
public class Option_NewOpenQianCeng extends Option implements NeedCheckPurview{
	public Option_NewOpenQianCeng(){
		super();
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		QianCengTaManager.getInstance().opt_Newopen(player);
	}

	@Override
	public boolean canSee(Player player) {
		if(player.getLevel() < WindowManager.LMLV){
			return false;
		}
		return true;
	}
}
