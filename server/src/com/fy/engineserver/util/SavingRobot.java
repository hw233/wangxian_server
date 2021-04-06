package com.fy.engineserver.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.sprite.PlayerManager;
import com.fy.boss.client.BossClientService;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.timer.Executable;

/**
 * 充值机器人，测试
 * 
 *
 */
public class SavingRobot implements Runnable,Executable {
//	protected static final Log logger = LogFactory.getLog(SavingRobot.class);
public	static Logger logger = LoggerFactory.getLogger(SavingRobot.class);
	
	private static boolean running;
	
	private String cardfile;
	
	private long period = 2000;
	
	private long lastFlushTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
	
	private List<String> cardlist;
	
	private static final int cardtype = 3;
	
	public void execute(String args[]) {
		if(!running && args.length > 0) {
			running = true;
			this.cardfile = args[0];
			if(args.length > 1) {
				this.period = Long.parseLong(args[1]);
			}
			Thread t = new Thread(this, "SavingRobot");
			t.start();
		}
	}
	
	public void run() {
//		logger.info("[开始模拟] ["+cardfile+"] [是否文件:"+new File(cardfile).isFile()+"]");
		if(logger.isInfoEnabled())
			logger.info("[开始模拟] [{}] [是否文件:{}]", new Object[]{cardfile,new File(cardfile).isFile()});
		this.cardlist = loadCardFromFile(cardfile);
		BossClientService bossClientService = BossClientService.getInstance();
		PlayerManager pmanager = PlayerManager.getInstance();
		Random ran = new Random();
		int index = 0;
		int cursaved = 0;
		while(true) {
			if(cardlist.size() == 0) {
				break;
			}
			try {
				long ransleep = period + ran.nextInt(2000);
				Thread.sleep(ransleep);
//				long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//				String card = cardlist.get(index);
//				String str[] = card.split("\t");
//				String cardno = str[0];
//				String pass = str[1];
//				int mianzhi = Integer.parseInt(str[2]);
//				Player player = pmanager.getRandomPlayer();
//				String map = player.getGame();
//				if(map == null || map.length() == 0) {
//					map = player.getLastGame();
//				}
////				bossClientService.cardSaving(player.getName(), player.getUsername(), cardno, pass, mianzhi, cardtype, mianzhi, 
////						player.getLevel(), CareerManager.getInstance().careerNameByType(player.getCareer()), 
////						player.getSex(), Player.getPoliticalCampDesp(player.getPoliticalCamp()), map);
//				cursaved++;
//				logger.info("[模拟充值] [成功] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] ["+cardno+"] ["+pass+"] ["+mianzhi+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-now)+"ms]");
//				card = card +  "\tused";
//				cardlist.set(index, card);
//				index++;
//				if(now-lastFlushTime > 10*1000) {
//					lastFlushTime = now;
//					writeToFile(cardlist, cardfile);
//				}
//				if(index >= cardlist.size()) {
//					logger.info("[全部充完] ["+cursaved+"]");
//					break;
//				}
			} catch(Throwable e) {
				e.printStackTrace();
				logger.error("[模拟充值时发生异常] [失败] ["+index+"] ["+cursaved+"]", e);
			}
		}
//		logger.info("[结束模拟充值] [充值个数:"+cursaved+"]");
		if(logger.isInfoEnabled())
			logger.info("[结束模拟充值] [充值个数:{}]", new Object[]{cursaved});
		running = false;
	}

	private List<String> loadCardFromFile(String file) {
		String s = FileUtils.readFile(file);
		String lines[] = s.split("\n");
		List<String> list = new ArrayList<String>();
		for(String line : lines) {
			if(!line.endsWith("used")) {
				list.add(line);
			}
		}
//		logger.info("[从文件中加载列表] ["+list.size()+"] ["+file+"]");
		if(logger.isInfoEnabled())
			logger.info("[从文件中加载列表] [{}] [{}]", new Object[]{list.size(),file});
		return list;
	}
	
	private void writeToFile(List<String> cardlist, String file) {
		List<String> list = new ArrayList<String>();
		list.addAll(cardlist);
		StringBuffer sb = new StringBuffer();
		for(String card : list) {
			sb.append(card + "\n");
		}
		FileUtils.writeFile(sb.toString(), file);
//		logger.info("[保存列表到文件] ["+cardlist.size()+"] ["+file+"]");
		if(logger.isInfoEnabled())
			logger.info("[保存列表到文件] [{}] [{}]", new Object[]{cardlist.size(),file});
	}
}
