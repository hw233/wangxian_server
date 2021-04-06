package com.fy.engineserver.menu.downcity;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;

/**
 * 申请打开副本窗口
 * 
 * 
 *
 */
public class Option_DownCity_tanchuang extends Option implements NeedCheckPurview{
	
	public byte yuanshenmoshi;

	public byte getYuanshenmoshi() {
		return yuanshenmoshi;
	}

	public void setYuanshenmoshi(byte yuanshenmoshi) {
		this.yuanshenmoshi = yuanshenmoshi;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		WindowManager wm = WindowManager.getInstance();
		
		if(yuanshenmoshi == 0){
			MenuWindow mw = wm.createTempMenuWindow(600);
			mw.setTitle(Translate.副本入口);
			mw.setDescriptionInUUB(Translate.副本入口);
			mw.setNpcId(-1);
			Option_DownCity_Shenqing option1 = new Option_DownCity_Shenqing();
			option1.setText(Translate.太虚幻境111);
			option1.setColor(0xFFFFFF);
			option1.downCityName = "taixuhuanjing";
			option1.yuanshenmoshi = 0;
			
			Option_DownCity_Shenqing option2 = new Option_DownCity_Shenqing();
			option2.setText(Translate.玉虚迷境131);
			option2.setColor(0xFFFFFF);
			option2.downCityName = "yuxumijing";
			option2.yuanshenmoshi = 0;
			
			Option_DownCity_Shenqing option3 = new Option_DownCity_Shenqing();
			option3.setText(Translate.清虚苦境151);
			option3.setColor(0xFFFFFF);
			option3.downCityName = "qingxukujing";
			option3.yuanshenmoshi = 0;
			
			Option_DownCity_Shenqing option4 = new Option_DownCity_Shenqing();
			option4.setText(Translate.奈何绝境171);
			option4.setColor(0xFFFFFF);
			option4.downCityName = "naihejuejing";
			option4.yuanshenmoshi = 0;
			
			Option_DownCity_Shenqing option5 = new Option_DownCity_Shenqing();
			option5.setText(Translate.望乡灭境191);
			option5.setColor(0xFFFFFF);
			option5.downCityName = "wangxiangmiejing";
			option5.yuanshenmoshi = 0;
			
			Option_DownCity_Shenqing option6 = new Option_DownCity_Shenqing();
			option6.setText(Translate.酆都鬼境211);
			option6.setColor(0xFFFFFF);
			option6.downCityName = "fengduguijing";
			option6.yuanshenmoshi = 0;
			
			mw.setOptions(new Option[]{option1,option2,option3,option4,option5,option6});
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			player.addMessageToRightBag(res);
		}else{
			MenuWindow mw = wm.createTempMenuWindow(600);
			mw.setTitle(Translate.元神副本入口);
			mw.setNpcId(-1);
			mw.setDescriptionInUUB(Translate.元神副本入口);
			Option_DownCity_Shenqing option1 = new Option_DownCity_Shenqing();
			option1.setText(Translate.太虚幻境_元神111);
			option1.setColor(0xFFFFFF);
			option1.downCityName = "taixuhuanjingyuanshen";
			option1.yuanshenmoshi = 1;
			
			Option_DownCity_Shenqing option2 = new Option_DownCity_Shenqing();
			option2.setText(Translate.玉虚迷境_元神131);
			option2.setColor(0xFFFFFF);
			option2.downCityName = "yuxumijingyuanshen";
			option2.yuanshenmoshi = 1;
			
			Option_DownCity_Shenqing option3 = new Option_DownCity_Shenqing();
			option3.setText(Translate.清虚苦境_元神151);
			option3.setColor(0xFFFFFF);
			option3.downCityName = "qingxukujingyuanshen";
			option3.yuanshenmoshi = 1;
			
			Option_DownCity_Shenqing option4 = new Option_DownCity_Shenqing();
			option4.setText(Translate.奈何绝境_元神171);
			option4.setColor(0xFFFFFF);
			option4.downCityName = "naihejuejingyuanshen";
			option4.yuanshenmoshi = 1;
			
			Option_DownCity_Shenqing option5 = new Option_DownCity_Shenqing();
			option5.setText(Translate.望乡灭境191_元神191);
			option5.setColor(0xFFFFFF);
			option5.downCityName = "wangxiangmiejingyuanshen";
			option5.yuanshenmoshi = 1;
			
			Option_DownCity_Shenqing option6 = new Option_DownCity_Shenqing();
			option6.setText(Translate.酆都鬼境211_元神211);
			option6.setColor(0xFFFFFF);
			option6.downCityName = "fengdouguijingyuanshen";
			option6.yuanshenmoshi = 1;
			
			mw.setOptions(new Option[]{option1,option2,option3,option4,option5,option6});
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			player.addMessageToRightBag(res);
		}
	}
	
	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doInput(Game game, Player player, String inputContent){}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_比武进比武场;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_5035 ;
	}

	@Override
	public boolean canSee(Player player) {
		if(player.getLevel() < WindowManager.LMLV){
			return false;
		}
		// TODO Auto-generated method stub
		return false;
	}

}
