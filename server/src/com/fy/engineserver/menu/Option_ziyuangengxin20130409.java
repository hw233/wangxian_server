/**
 * 
 */
package com.fy.engineserver.menu;

import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.boss.game.GameConstants;

/**
 * @author Administrator
 *
 */
public class Option_ziyuangengxin20130409 extends Option implements NeedCheckPurview{

	/**
	 * 
	 */
	public Option_ziyuangengxin20130409() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		MenuWindow mw=WindowManager.getInstance().createTempMenuWindow(300);
		mw.setTitle("领取资源更新补偿");
		mw.setDescriptionInUUB("飘渺寻仙曲团队对于4月8日凌晨维护之后无法正常更新资源、无法正常登陆游戏的事件向各位玩家表达真诚深刻的道歉！为表达我们深刻的歉意，送出<f color='# ffff00' >诚意锦囊：内含橙酒*1,紫酒*2，蓝酒*3</f>，里面包含着我们对您的<f color='# ff6699' >一份诚意，两份歉意，三份谢意</f>。补偿领取时间<f color='# ff6699' >截止到4月12日23:59</f>，所有<f color='# ff6699' >10级以上</f>的仙友都可以领取一次。非常感谢您的谅解！在未来的日子里我们将努力提升游戏品质和游戏服务，为您带来更好的游戏体验！");
		Option_ziyuangengxin20130409_sure ougi=new Option_ziyuangengxin20130409_sure();
		ougi.setColor(0xffffff);
		ougi.setText("领取");
		mw.setOptions(new Option[]{ougi});
		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
		player.addMessageToRightBag(res);
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_领取奖励;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return "4月8日资源更新补偿方案";
	}

	public static String limitServers[] = { "琼华烬染", "万里苍穹", "浩瀚乾坤", "蓬莱仙境" };
	@Override
	public boolean canSee(Player player) {
		try {
			String servername = GameConstants.getInstance().getServerName();
			if (!PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
				return false;
			}
			if (servername == null) {
				return false;
			}
			for (String name : limitServers) {
				if (name.equals(servername)) {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			CoreSubSystem.logger.warn("[4月8日资源更新补偿方案] [异常] [" + player.toString() + "]", e);
			return false;
		}
		return true;
	}

}
