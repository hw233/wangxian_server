package com.fy.engineserver.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.career.Career;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.stat.client.StatClientService;
import com.xuanzhi.stat.model.PlayerCreateFlow;
import com.xuanzhi.stat.model.PlayerLevelUpFlow;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.timer.Executable;

/**
 * 批量创建玩家
 * 
 *
 */
public class PlayerGenerator implements Runnable,Executable {
//	protected static final Log logger = LogFactory.getLog(PlayerGenerator.class);
public	static Logger logger = LoggerFactory.getLogger(PlayerGenerator.class);
	
	private static boolean running;
	
	private String userfile;
	
	private List<String> userlist;
	
	public void execute(String args[]) {
		if(!running && args.length > 0) {
			running = true;
			this.userfile = args[0];
			Thread t = new Thread(this, "PlayerGenerator");
			t.start();
		}
	}
	
	public void run() {
		PlayerManager pmanager = PlayerManager.getInstance();
		StatClientService statClientService = StatClientService.getInstance();
		userlist = loadFromFile(userfile);
		//加载出一部分玩家
		Player ps[] = null;
		try {
			int count = pmanager.getAmountOfPlayers();
			ps = pmanager.getPlayersByPage(0, count);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			logger.error("[创建玩家时发生异常]", e1);
		}
		int psindex = 0;
		int total = 0;
		for(String username : userlist) {
			try {
				long l = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				Player p = ps[psindex++];
				if(psindex >= ps.length) {
					psindex = 0;
				}
				String name = "TNAME" + total;
				total++;
				//FIXME
				Player pp = pmanager.createPlayer(username, name,(byte)0, p.getSex(),(byte)0, p.getCountry(), p.getCareer());
				pp.setGame(p.getGame());
				pp.setLevel(p.getLevel());
				pp.setLastGame(p.getLastGame());
				
				//日志
				PlayerCreateFlow flow = new PlayerCreateFlow();
				CareerManager cm = CareerManager.getInstance();
				Career career = cm.getCareer(p.getCareer());
				flow.setCareer(career.getName());
				flow.setChannel("UNKNOWN");
				flow.setChannelitem("UNKNOWN");
				flow.setChannelname(Translate.text_1211);
				flow.setChannelitemname(Translate.text_1211);
				flow.setPlayername(pp.getName());
				flow.setQuickreg(false);
				flow.setServer(GameConstants.getInstance().getServerName());
				flow.setSex(pp.getSex());
				flow.setTime(l);
				flow.setUsername(username);
				statClientService.sendPlayerCreateFlow(flow);

				// 创建用户后默认为一级， 发送升级日志
				PlayerLevelUpFlow flow2 = new PlayerLevelUpFlow();
				flow2.setCareer(CareerManager.getInstance().getCareer(p.getCareer()).getName());
				flow2.setChannel("UNKNOWN");
				flow2.setChannelitem("UNKNOWN");
				flow2.setChannelname(Translate.text_1211);
				flow2.setChannelitemname(Translate.text_1211);
				String game = p.getGame();
				if (game == null || game.length() == 0) {
					game = p.getLastGame();
				}
				flow2.setMap(game);
				flow2.setNowlevel(pp.getLevel());
				flow2.setPlayername(pp.getName());
				flow2.setServer(GameConstants.getInstance().getServerName());
				flow2.setSex(pp.getSex());
				flow2.setUsername(username);
				flow2.setRegtime(l);
				flow2.setTime(l);
				statClientService.sendPlayerLevelUpFlow(flow2);
//				logger.info("[创建玩家] ["+pp.getId()+"] ["+pp.getName()+"] ["+pp.getUsername()+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-l)+"ms]");
				if(logger.isInfoEnabled())
					logger.info("[创建玩家] [{}] [{}] [{}] [{}ms]", new Object[]{pp.getId(),pp.getName(),pp.getUsername(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-l)});
			} catch(Throwable e) {
				e.printStackTrace();
				logger.error("[创建玩家时发生异常]", e);
			}
		}
		running = false;
	}

	private List<String> loadFromFile(String file) {
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
}
