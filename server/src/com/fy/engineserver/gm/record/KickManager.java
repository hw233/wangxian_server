package com.fy.engineserver.gm.record;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.configuration.Configuration;
import com.xuanzhi.tools.configuration.ConfigurationException;
import com.xuanzhi.tools.configuration.DefaultConfiguration;
import com.xuanzhi.tools.configuration.DefaultConfigurationBuilder;
import com.xuanzhi.tools.configuration.DefaultConfigurationSerializer;
import com.xuanzhi.tools.text.FileUtils;

public class KickManager {
	Logger logger = Logger.getLogger(KickManager.class.getName());
	private static KickManager self;
	private List<String> kickName = new ArrayList<String>();
	private List<Kick> kicks = new ArrayList<Kick>();
	List<Kick> kickList = new ArrayList<Kick>();
	List<Kick> silentList = new ArrayList<Kick>();
	List<Kick> talkList = new ArrayList<Kick>();
	private String bfFile;

	public void initialize() {
		
		long old = System.currentTimeMillis();
		try {
			kicks = loadPage(bfFile);
			if (kicks != null && kicks.size() > 0) {
				for (Kick bp : kicks)
					kickName.add(bp.getUsername());
				for (int i = 0; i < kicks.size(); i++) {
					if (kicks.get(i).getType().equals("talk")) {
						talkList.add(kicks.get(i));
					} else if (kicks.get(i).getType().equals("kick")) {
						kickList.add(kicks.get(i));
					} else if (kicks.get(i).getType().equals("silent")) {
						silentList.add(kicks.get(i));
					}
				}
			}
			self = this;
		} catch (Exception e) {
			logger.warn("[load][kickplayfile] [fail]");
		}
		ServiceStartRecord.startLog(this);
	}

	public static KickManager getInstance() {
		return self;
	}

	public void addKicks(Kick kick) {

		try {
			if (kick != null) {
				kicks.add(kick);
				if (kick.getType().equals("talk")) {
					talkList.add(kick);
				} else if (kick.getType().equals("silent")) {
					silentList.add(kick);
				} else if (kick.getType().equals("kick")) {
					kickList.add(kick);
				}
			}
			logger.info("[添加记录] [成功] [操作人+" + kick.getGmname() + "] [添加时间+" + kick.getDate() + "] [添加的用户+" + kick.getUsername() + "]");
			savePage(kicks, bfFile);

		} catch (Exception e) {
			logger.warn("[添加记录] [失败] [操作人+" + kick.getGmname() + "] [添加时间+" + kick.getDate() + "] [添加的用户+" + kick.getUsername() + "]");
			e.printStackTrace();
		}
	}

	public void deleteKicks(String username) {
		Kick kick = kicks.get(kickName.indexOf(username));
		try {
			if (kickName.contains(username)) {
				kicks.remove(kickName.indexOf(username));
				kickName.remove(username);
			} else {
				return;
			}
			savePage(kicks, bfFile);
			logger.warn("[删除记录] [失败] [操作人+" + username + "] [删除时间+" + kick.getDate() + "] [删除的用户+" + kick.getUsername() + "]");
		} catch (Exception e) {
			logger.warn("[删除记录] [失败] [操作人+" + username + "] [删除时间+" + kick.getDate() + "] [删除的用户+" + kick.getUsername() + "]");
			e.printStackTrace();
		}

	}

	public void savePage(List<Kick> kicks, String saveFile) throws IllegalArgumentException, IllegalAccessException, SAXException, IOException, ConfigurationException, InvocationTargetException {
		Configuration rootConf = new DefaultConfiguration("kickplays", "-");
		for (Kick ki : kicks) {
			Configuration bpConf = new DefaultConfiguration("kickplay", "-");
			rootConf.addChild(bpConf);
			bpConf.setAttribute("username", ki.getUsername());
			bpConf.setAttribute("gmname", ki.getGmname());
			bpConf.setAttribute("result", ki.getResult());
			bpConf.setAttribute("times", ki.getTimes());
			bpConf.setAttribute("date", ki.getDate());
			bpConf.setAttribute("type", ki.getType());
		}
		FileUtils.chkFolder(saveFile);
		new DefaultConfigurationSerializer().serializeToFile(new File(saveFile), rootConf);
		if (logger.isInfoEnabled()) {
			logger.info("[保存记录] [成功]");
		}
	}

	public List<Kick> loadPage(String xmlname) {
		List<Kick> kickss = new ArrayList<Kick>();
		try {
			Configuration rootConf = new DefaultConfigurationBuilder().buildFromFile(xmlname);
			Configuration rolesConf[] = rootConf.getChildren();
			for (int i = 0; i < rolesConf.length; i++) {
				Kick kick = new Kick();
				kick.setUsername(rolesConf[i].getAttribute("username", ""));
				kick.setGmname(rolesConf[i].getAttribute("gmname", ""));
				kick.setResult(rolesConf[i].getAttribute("result", ""));
				kick.setTimes(rolesConf[i].getAttribute("times", ""));
				kick.setDate(rolesConf[i].getAttribute("date", ""));
				kick.setType(rolesConf[i].getAttribute("type", ""));
				kickss.add(kick);
			}
			return kickss;
		} catch (Exception e) {
			logger.warn("[load记录] [失败]");
			return new ArrayList<Kick>();
		}
	}

	public List<String> getKickName() {
		return kickName;
	}

	public void setKickName(List<String> kickName) {
		this.kickName = kickName;
	}

	public List<Kick> getKicks() {
		return kicks;
	}

	public void setKicks(List<Kick> kicks) {
		this.kicks = kicks;
	}

	public String getBfFile() {
		return bfFile;
	}

	public void setBfFile(String bfFile) {
		this.bfFile = bfFile;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public static KickManager getSelf() {
		return self;
	}

	public static void setSelf(KickManager self) {
		KickManager.self = self;
	}

	public List<Kick> getKickList() {
		return kickList;
	}

	public void setKickList(List<Kick> kickList) {
		this.kickList = kickList;
	}

	public List<Kick> getSilentList() {
		return silentList;
	}

	public void setSilentList(List<Kick> silentList) {
		this.silentList = silentList;
	}

	public List<Kick> getTalkList() {
		return talkList;
	}

	public void setTalkList(List<Kick> talkList) {
		this.talkList = talkList;
	}
}
