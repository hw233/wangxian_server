/**
 * 
 */
package com.fy.engineserver.menu;

import java.text.SimpleDateFormat;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.boss.game.GameConstants;

public class Option_ziyuangengxin20130411 extends Option implements NeedCheckPurview {

	public Option_ziyuangengxin20130411() {

	}

	public static String startTime = "2013-04-09 00:00:00";

	public static String endTime = "2013-04-13 23:59:59";

	@Override
	public void doSelect(Game game, Player player) {
		try {
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(300);
			mw.setTitle("[诚意锦囊]金诚相送");
			mw.setDescriptionInUUB("为了减小本次临时维护给您造成的损失，特赠送您[诚意锦囊]一份(内含橙酒*1、紫酒*2、蓝酒*3)，聊表歉意！感谢您对飘渺寻仙曲的支持！");
			Option_ziyuangengxin20130411_sure ougi = new Option_ziyuangengxin20130411_sure();
			ougi.setColor(0xffffff);
			ougi.setText("领取");
			mw.setOptions(new Option[] { ougi });
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			player.addMessageToRightBag(res);
		} catch (Exception e) {
			ActivitySubSystem.logger.warn("[异常]",e);
		}
	}
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		try {
			String servername = GameConstants.getInstance().getServerName();
			if (!PlatformManager.getInstance().isPlatformOf(Platform.腾讯)) {
				return false;
			}
			if (servername == null) {
				return false;
			}
			long starttime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime).getTime();
			long endtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime).getTime();
			long now = System.currentTimeMillis();

			if (starttime > now || now > endtime) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			CoreSubSystem.logger.warn("[4月8日资源更新补偿方案] [异常] [" + player.toString() + "]", e);
			return false;
		}
		return true;
	}

}
