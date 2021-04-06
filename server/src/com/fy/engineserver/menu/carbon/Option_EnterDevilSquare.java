package com.fy.engineserver.menu.carbon;

import java.util.Iterator;

import com.fy.engineserver.carbon.devilSquare.DevilSquareManager;
import com.fy.engineserver.carbon.devilSquare.model.BaseModel;
import com.fy.engineserver.carbon.devilSquare.model.DevelSquareBaseConf;
import com.fy.engineserver.carbon.devilSquare.model.DevilTranslateModel;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.DEVILSQUARE_TIPS_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;
/**
 * 进入副本窗口
 * @author Administrator
 *
 */
public class Option_EnterDevilSquare extends Option implements NeedCheckPurview{
	
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		// TODO Auto-generated method stub
		if(player.getLevel() < WindowManager.LMLV){
			return false;
		}
		return true;
	}

	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		DevilSquareManager inst = DevilSquareManager.instance;
//		DevelSquareBaseConf[] df = new DevelSquareBaseConf[inst.dsInst.size()];
		DevelSquareBaseConf[] df = new DevelSquareBaseConf[inst.baseModelMap.size()];
//		Iterator<Integer> ite = inst.dsInst.keySet().iterator();
		Iterator<Integer> ite = inst.baseModelMap.keySet().iterator();
		int i=0;
		while(ite.hasNext()) {
			int level = ite.next();
			BaseModel bm = inst.baseModelMap.get(level);
			DevilTranslateModel dff = inst.transMap.get(level);
			df[i] = new DevelSquareBaseConf();
			df[i].setLevel(level);
			df[i].setPlays(dff.getPlays());
			df[i].setBcstorys(dff.getBcStory());
			df[i].setDropprops(dff.getDropProps());
			df[i].setCostprops(dff.getCostProps());
			df[i].setCostNum(dff.getCostNum());
			df[i].setMapName(bm.getcMapName());
			df[i].setDropprobabbly(dff.getDropProbabbly());
			i++;
		}
		DEVILSQUARE_TIPS_REQ resp = new DEVILSQUARE_TIPS_REQ(GameMessageFactory.nextSequnceNum(), df);
		player.addMessageToRightBag(resp);
	}

}
