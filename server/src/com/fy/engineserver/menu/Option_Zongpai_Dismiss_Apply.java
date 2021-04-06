package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

/**
 * 解散宗派申请
 * @author Administrator
 *
 */
public class Option_Zongpai_Dismiss_Apply extends Option{

	
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_结义;
	}
	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		
//		//权限验证
//		String result = ZongPaiManager.getInstance().宗主身份判断(player);
//		if(result.equals("")){
			
//			//弹输入密码框
//			WindowManager windowManager = WindowManager.getInstance();
//			MenuWindow mw = windowManager.createTempMenuWindow(600);
//			mw.setTitle(Translate.解散宗派);
//			mw.setDescriptionInUUB(Translate.你确定要解散宗派吗);
//			
//			Option_Zongpai_Dismiss_Inputpassword option = new Option_Zongpai_Dismiss_Inputpassword();
//			option.setText(Translate.确定);
//			
//			Option_Cancel cancel = new Option_Cancel();
//			cancel.setText(Translate.取消);
//			mw.setOptions(new Option[]{option,cancel});
//			
//			
//			INPUT_WINDOW_REQ res = new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getTitle(), mw.getDescriptionInUUB(), (byte)2, (byte)100, Translate.确定, Translate.取消, new byte[0]);
//			player.addMessageToRightBag(res);
			
//		}else{
//			player.send_HINT_REQ(result);
//		}
	}

	public byte getOType() {
		return Option.OPTION_TYPR_CLIENT_FUNCTION;
	}

}
