package com.fy.engineserver.util;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.sprite.PlayerManager;
import com.fy.boss.client.BossClientService;
import com.xuanzhi.stat.client.StatClientService;

/**
 * 创建玩家机器人
 * 
 *
 */
public class PlayerRobotGenerator implements Runnable {
//	protected static final Log logger = LogFactory.getLog(PlayerRobotGenerator.class);
public	static Logger logger = LoggerFactory.getLogger(PlayerRobotGenerator.class);
	
	private boolean openning = false;
	
	public void initialize() {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if(openning) {
			Thread t = new Thread(this, "PlayerRobotGenerator");
			t.start();
		}
		System.out.println(this.getClass().getName() + " initialize successfully ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-now)+"]");
			if (logger.isDebugEnabled()) logger.debug("{} initialize successfully [{}]", new Object[]{this.getClass().getName(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-now)});
	}
	
	public void setOpenning(boolean openning) {
		this.openning = openning;
	}

	public void run() {
		try {
			Thread.sleep(20000);
		} catch(Exception e) {};
		
		PlayerManager pmanager = PlayerManager.getInstance();
		StatClientService statClientService = StatClientService.getInstance();
		BossClientService bossClientService = BossClientService.getInstance();
		while(true) {
			try {
				long l = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				Thread.sleep(1000 + new Random().nextInt(1000));
//				Player p = pmanager.getRandomPlayer();
//				Passport passport = bossClientService.getRandomPassport();
//				String name = "RANPLAYER" + DateUtil.formatDate(new Date(), "yyMdhms") + StringUtil.randomIntegerString(3);
//				Player pp = pmanager.createPlayer(passport.getUsername(), name, p.getSex(), p.getPoliticalCamp(), p.getCareer());
//				pp.setGame(p.getGame());
//				pp.setLevel(p.getLevel());
//				pp.setLastGame(p.getLastGame());
//				
//				//日志
//				PlayerCreateFlow flow = new PlayerCreateFlow();
//				CareerManager cm = CareerManager.getInstance();
//				Career career = cm.getCareer(p.getCareer());
//				flow.setCareer(career.getName());
//				flow.setChannel("UNKNOWN");
//				flow.setChannelitem("UNKNOWN");
//				flow.setChannelname(Translate.translate.text_1211);
//				flow.setChannelitemname(Translate.translate.text_1211);
//				flow.setPlayername(pp.getName());
//				flow.setQuickreg(false);
//				flow.setServer(GameConstants.getInstance().getServerName());
//				flow.setSex(pp.getSex());
//				flow.setTime(l);
//				flow.setUsername(passport.getUsername());
//				statClientService.sendPlayerCreateFlow(flow);
//
//				// 创建用户后默认为一级， 发送升级日志
//				PlayerLevelUpFlow flow2 = new PlayerLevelUpFlow();
//				flow2.setCareer(CareerManager.getInstance().getCareer(p.getCareer()).getName());
//				flow2.setChannel("UNKNOWN");
//				flow2.setChannelitem("UNKNOWN");
//				flow2.setChannelname(Translate.translate.text_1211);
//				flow2.setChannelitemname(Translate.translate.text_1211);
//				String game = p.getGame();
//				if (game == null || game.length() == 0) {
//					game = p.getLastGame();
//				}
//				flow2.setMap(game);
//				flow2.setNowlevel(pp.getLevel());
//				flow2.setPlayername(pp.getName());
//				flow2.setServer(GameConstants.getInstance().getServerName());
//				flow2.setSex(pp.getSex());
//				flow2.setUsername(passport.getUsername());
//				flow2.setRegtime(passport.getCreatedate().getTime());
//				flow2.setTime(l);
//				statClientService.sendPlayerLevelUpFlow(flow2);
//				logger.info("[创建玩家] ["+pp.getId()+"] ["+pp.getName()+"] ["+pp.getUsername()+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-l)+"ms]");
			} catch(Throwable e) {
				e.printStackTrace();
				logger.error("[创建玩家时发生异常]", e);
			}
		}
	}
}
