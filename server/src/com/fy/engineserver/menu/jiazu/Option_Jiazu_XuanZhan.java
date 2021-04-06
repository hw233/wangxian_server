package com.fy.engineserver.menu.jiazu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuFighterManager;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_UseCancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.INPUT_WINDOW_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * 家族宣战
 * 
 */
public class Option_Jiazu_XuanZhan extends Option {

	public Option_Jiazu_XuanZhan() {
		// TODO Auto-generated constructor stub
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		JiazuFighterManager jfm = JiazuFighterManager.getInstance();
		int count = 100;
		if(jfm != null){
			count = jfm.宣战所需资金();
		}
		mw.setTitle(Translate.家族宣战);
		mw.setDescriptionInUUB(Translate.translateString(Translate.请输入宣战家族的名字, new String[][]{{Translate.COUNT_1,BillingCenter.得到带单位的银两(count)}}));
		Option_Jiazu_XuanZhan option = new Option_Jiazu_XuanZhan();
		option.setText(Translate.宣战);
		Option_UseCancel cancel = new Option_UseCancel();
		cancel.setText(Translate.取消);
		mw.setOptions(new Option[]{option,cancel});
		INPUT_WINDOW_REQ res = new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getTitle(), mw.getDescriptionInUUB(), (byte)2, (byte)100, Translate.在这里输入宣战家族名, Translate.宣战, Translate.取消, new byte[0]);
		player.addMessageToRightBag(res);
	}

	@Override
	public void doInput(Game game, Player player, String inputContent) {
		// TODO Auto-generated method stub
		JiazuManager jm = JiazuManager.getInstance();
		if(jm != null){
			Jiazu jz = jm.getJiazu(inputContent.trim());
			if(jz == null){
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.您输入的家族不存在);
				player.addMessageToRightBag(hreq);
				return;
			}
			JiazuFighterManager jf = JiazuFighterManager.getInstance();
			if(jf != null){
				jf.家族宣战(player, jz.getJiazuID());
			}else{
				if(JiazuFighterManager.logger.isWarnEnabled())
					JiazuFighterManager.logger.warn("[JiazuFighterManager为空]");
			}
		}
	}
}
