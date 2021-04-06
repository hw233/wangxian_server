/**
 * 
 */
package com.fy.engineserver.menu;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.INPUT_WINDOW_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.boss.authorize.model.Passport;

/**
 * @author Administrator
 * 
 */
public class Option_LockPlayer extends Option {

	// Logger log = Logger.getLogger("com.fy.engineserver.sprite");
	public static Logger log = LoggerFactory.getLogger("com.fy.engineserver.sprite");

	/**
	 * 
	 */
	public Option_LockPlayer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		Passport pp = player.getPassport();
		if (pp != null) {
			String password = pp.getSecretQuestion();
			if (password != null && password.length() > 0) {
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(300);
				if (pp.getStatus() == Passport.STATUS_NORMAL) {// getProtecting() == 0) {
					mw.setTitle(Translate.text_5328);
					mw.setDescriptionInUUB(Translate.text_5329);
				} else {
					mw.setTitle(Translate.text_5330);
					mw.setDescriptionInUUB(Translate.text_5331);
				}
				Option_InputAdvancedPassword oiap = new Option_InputAdvancedPassword(false);
				oiap.setColor(0xffffff);
				oiap.setText(Translate.text_62);
				Option_Cancel oc = new Option_Cancel();
				oc.setColor(0xffffff);
				oc.setText(Translate.text_364);
				mw.setOptions(new Option[] { oiap, oc });

				INPUT_WINDOW_REQ iwr = new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getTitle(), mw.getDescriptionInUUB(), (byte) 2, (byte) 10, Translate.在这里输入, oiap.getText(), oc.getText(), new byte[0]);
				player.addMessageToRightBag(iwr);
			} else {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_5332);
				player.addMessageToRightBag(req);
				// this.log.warn("[设置角色锁定] [失败] [密码不存在] [玩家："+player.getName()+"] [玩家ID："+player.getId()+"]");
				if (this.log.isWarnEnabled()) this.log.warn("[设置角色锁定] [失败] [密码不存在] [玩家：{}] [玩家ID：{}]", new Object[] { player.getName(), player.getId() });
			}
		} else {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_5333);
			player.addMessageToRightBag(req);
			// this.log.warn("[设置角色锁定] [失败] [passport不存在] [玩家："+player.getName()+"] [玩家ID："+player.getId()+"]");
			if (this.log.isWarnEnabled()) this.log.warn("[设置角色锁定] [失败] [passport不存在] [玩家：{}] [玩家ID：{}]", new Object[] { player.getName(), player.getId() });
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		// oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_LOCK_PLAYER;
	}

	public void setOId(int oid) {
	}

	public String toString() {
		return Translate.text_5334;
	}

}
