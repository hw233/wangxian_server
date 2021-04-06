package com.fy.engineserver.core;

import java.lang.reflect.Method;

import org.slf4j.Logger;

import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.jiazu.Option_Jiazu_Warehouse;
import com.fy.engineserver.menu.qianCeng.Option_OpenQianCeng;
import com.fy.engineserver.menu.sifang.Option_SiFang_BOSS;
import com.fy.engineserver.menu.sifang.Option_SiFang_EnList;
import com.fy.engineserver.menu.sifang.Option_SiFang_Jingru;
import com.fy.engineserver.menu.sifang.Option_SiFang_Join;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.SIFANG_CHOOSE_PLAYER_REQ;
import com.fy.engineserver.message.SIFANG_CHOOSE_PLAYER_RES;
import com.fy.engineserver.sifang.SiFangManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class SiFangSubSystem  extends SubSystemAdapter {
	
	public static Logger logger = SiFangManager.logger;
	
	public static SiFangSubSystem instance;
	
	public static SiFangSubSystem getInstance(){
		return instance;
	}

	public void init(){
		
		instance = this;
		ServiceStartRecord.startLog(this);
	}
	
	@Override
	public String getName() {
		return "SiFangSubSystem";
	}

	@Override
	public String[] getInterestedMessageTypes() {
		return new String[]{"SIFANG_TEST_NPCMENU_REQ", "SIFANG_CHOOSE_PLAYER_REQ", "SIFANG_OVER_MSG_REQ"};
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn,
			RequestMessage message) throws ConnectionException, Exception {
		Player player = check(conn, message, logger);
		if(useMethodCache){
			return super.handleReqeustMessage(conn, message, player);
		}

		Class clazz = this.getClass();
		Method m1 = clazz.getDeclaredMethod("handle_" + message.getTypeDescription(), Connection.class, RequestMessage.class, Player.class);
		m1.setAccessible(true);
		return (ResponseMessage) m1.invoke(this, conn, message, player);
	}

	@Override
	public boolean handleResponseMessage(Connection conn,
			RequestMessage request, ResponseMessage response)
			throws ConnectionException, Exception {
		return false;
	}
	
	public  ResponseMessage handle_SIFANG_TEST_NPCMENU_REQ (Connection conn, RequestMessage message, Player player){
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
		mw.setTitle("");
		mw.setDescriptionInUUB("");
		Option_SiFang_Join opt1 = new Option_SiFang_Join(0);
		opt1.setText("参加青龙");
		Option_SiFang_Join opt2 = new Option_SiFang_Join(1);
		opt2.setText("参加朱雀");
		Option_SiFang_Join opt3 = new Option_SiFang_Join(2);
		opt3.setText("参加白虎");
		Option_SiFang_Join opt4 = new Option_SiFang_Join(3);
		opt4.setText("参加玄武");
		Option_SiFang_Join opt5 = new Option_SiFang_Join(4);
		opt5.setText("参加麒麟");
		Option_SiFang_EnList opt6 = new Option_SiFang_EnList(0);
		opt6.setText("设置青龙参赛");
		Option_SiFang_EnList opt7 = new Option_SiFang_EnList(1);
		opt7.setText("设置朱雀参赛");
		Option_SiFang_EnList opt8 = new Option_SiFang_EnList(2);
		opt8.setText("设置白虎参赛");
		Option_SiFang_EnList opt9 = new Option_SiFang_EnList(3);
		opt9.setText("设置玄武参赛");
		Option_SiFang_EnList opt10 = new Option_SiFang_EnList(4);
		opt10.setText("设置麒麟参赛");
		Option_SiFang_Jingru opt11 = new Option_SiFang_Jingru(0);
		opt11.setText("参赛青龙");
		Option_SiFang_Jingru opt12 = new Option_SiFang_Jingru(1);
		opt12.setText("参赛朱雀");
		Option_SiFang_Jingru opt13 = new Option_SiFang_Jingru(2);
		opt13.setText("参赛白虎");
		Option_SiFang_Jingru opt14 = new Option_SiFang_Jingru(3);
		opt14.setText("参赛玄武");
		Option_SiFang_Jingru opt15 = new Option_SiFang_Jingru(4);
		opt15.setText("参赛麒麟");
		Option_Jiazu_Warehouse opt16 = new Option_Jiazu_Warehouse();
		opt16.setText("家族仓库");
		Option_SiFang_BOSS opt17 = new Option_SiFang_BOSS(0);
		opt17.setText("青龙BOSS");
		Option_SiFang_BOSS opt18 = new Option_SiFang_BOSS(1);
		opt18.setText("朱雀BOSS");
		Option_SiFang_BOSS opt19 = new Option_SiFang_BOSS(2);
		opt19.setText("白虎BOSS");
		Option_SiFang_BOSS opt20 = new Option_SiFang_BOSS(3);
		opt20.setText("玄武BOSS");
		Option_SiFang_BOSS opt21 = new Option_SiFang_BOSS(4);
		opt21.setText("麒麟BOSS");
		Option_OpenQianCeng opt22 = new Option_OpenQianCeng();
		opt22.setText("千层塔");
		mw.setOptions(new Option[]{opt1, opt2, opt3, opt4, opt5, 
				opt6, opt7, opt8, opt9, opt10, 
				opt11, opt12, opt13, opt14, opt15, opt16, opt17, opt18, opt19, opt20, opt21, opt22});
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
		player.addMessageToRightBag(creq);
		return null;
	}
	
	public ResponseMessage handle_SIFANG_CHOOSE_PLAYER_REQ(Connection conn, RequestMessage message, Player player){
		SIFANG_CHOOSE_PLAYER_REQ req = (SIFANG_CHOOSE_PLAYER_REQ)message;
		boolean isC = req.getChoOrDel();
		long playerId = req.getPlayerId();
		int type = req.getSifangType();
//		if (isC) {
//			SiFangManager.getInstance().msg_add_enterPlayer(type, player, playerId);
//		}else {
//			SiFangManager.getInstance().msg_remove_enterPlayer(type, player, playerId);
//		}
		return new SIFANG_CHOOSE_PLAYER_RES(message.getSequnceNum());
	}
	
	public ResponseMessage handle_SIFANG_OVER_MSG_REQ(Connection conn, RequestMessage message, Player player){
		return SiFangManager.getInstance().msg_over_msg(player);
	}

}
