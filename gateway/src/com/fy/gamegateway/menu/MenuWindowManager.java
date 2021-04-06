package com.fy.gamegateway.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.fy.gamegateway.language.Translate;
import com.fy.gamegateway.message.GameMessageFactory;
import com.fy.gamegateway.message.NEW_OPTION_SELECT_REQ;
import com.fy.gamegateway.message.NEW_QUERY_WINDOW_REQ;
import com.fy.gamegateway.message.OPEN_WINDOW_REQ;
import com.fy.gamegateway.mieshi.server.MieshiGatewayServer;
import com.fy.gamegateway.mieshi.waigua.NewLoginHandler;
import com.xuanzhi.tools.transport.Connection;

public class MenuWindowManager {

	public static Map<Integer, MenuWindow> windows = new ConcurrentHashMap<Integer, MenuWindow>();
	
	public static void sendSimpleMsg (Connection conn, String title, String des) {
		MenuWindow mw = createMenuWindow(60000);
		Option[] ops = new Option[1];
		ops[0] = new Option_CloseWin();
		ops[0].setText(Translate.确定);
		mw.setOptions(ops);
		NEW_QUERY_WINDOW_REQ req = new NEW_QUERY_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getWinId(), title, des, new String[0], new byte[0], new byte[0], new String[0], new byte[0], mw.getOptions());
		MieshiGatewayServer.getInstance().sendMessageToClient(conn, req);
	}
	
	public static MenuWindow createMenuWindow (int time) {
		Random ran = new Random();
		MenuWindow mw = new MenuWindow();
		mw.setWinId(ran.nextInt(100000000));
		mw.setDestoryTime(System.currentTimeMillis() + time);
		windows.put(mw.getWinId(), mw);
		return mw;
	}
	
	public static void handle_NEW_OPTION_SELECT_REQ (Connection conn, NEW_OPTION_SELECT_REQ req) {
		try{
			int wid = req.getWId();
			MenuWindow mw = windows.get(wid);
			long now = System.currentTimeMillis();
			if (mw != null && mw.getDestoryTime() > now){
				int oIndex = req.getIndex();
				String[] inputText = req.getInputText();
				mw.getOptions()[oIndex].doSelect(conn, inputText);
				windows.remove(wid);
			}else {
				sendSimpleMsg(conn, "窗口失效", "窗口已经失效，按钮操作未生效。");
			}
			ArrayList<Integer> removes = new ArrayList<Integer>();
			for (Integer key : windows.keySet()) {
				if (windows.get(key).getDestoryTime() < now) {
					removes.add(key);
				}
			}
			for (Integer key : removes) {
				windows.remove(key);
			}
		} catch(Exception e) {
			NewLoginHandler.logger.error("handle_NEW_OPTION_SELECT_REQ出错:", e);
		}
	}
}
