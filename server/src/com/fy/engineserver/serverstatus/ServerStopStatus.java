package com.fy.engineserver.serverstatus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;

import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.boss.game.GameConstants;

public class ServerStopStatus {

	public static Logger logger = ServerStopStatus.logger;
	static ServerStopStatus self;
	public static ServerStopStatus getInstance(){
		return self;
	}
	
	/**
	 * 服务器启动进度条eg：11/200
	 * startNum:当前加载的第几个系统
	 * allNum:需要加载的总的系统数
	 */
	public static int startNum = 0; 
	public static int allNum = 248; 
	
	public void init(){}
	
	public void destroy(){
		GameConstants constants = GameConstants.getInstance();
		String serverName = "--";
		if (constants != null) {
			serverName = constants.getServerName();
		}
		String date = TimeTool.formatter.varChar19.format(new Date());
		System.out.println("[STOP-" + ServerStopStatus.class.getSimpleName()
				+ "] [服务器关闭完毕] [" + serverName + "] [" + date + "] [平台:"
				+ PlatformManager.getInstance().getPlatform().toString() + "]");
		logger.error("[STOP-" + ServerStopStatus.class.getSimpleName()
				+ "] [服务器关闭完毕] [" + serverName + "] [" + date + "] [平台:"
				+ PlatformManager.getInstance().getPlatform().toString() + "]");
		self = this;
	}
	
	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("D:\\workspace\\mieshi_server\\game_mieshi_server\\conf\\game_server_context.xml"));
			String str = null;
			int num = 0;
			while((str=br.readLine())!=null){
				if(str.contains("<bean id=")){
					num++;
					switch (num) {
						case 10:
							System.out.println(str);
							break;
						case 30:
							System.out.println(str);
							break;
						case 50:
							System.out.println(str);
							break;
						case 70:
							System.out.println(str);
							break;
						case 90:
							System.out.println(str);
							break;
						case 110:
							System.out.println(str);
							break;
						case 130:
							System.out.println(str);
							break;
						case 150:
							System.out.println(str);
							break;
						case 170:
							System.out.println(str);
							break;
						case 190:
							System.out.println(str);
							break;
						case 210:
							System.out.println(str);
							break;
						case 230:
							System.out.println(str);
							break;
					}
				}
			}
			System.out.println("系统数："+num);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
