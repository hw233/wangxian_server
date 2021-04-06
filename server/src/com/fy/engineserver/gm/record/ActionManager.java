package com.fy.engineserver.gm.record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.text.DateUtil;

public class ActionManager {
	// 记录gm操作
	// private static Logger logger = Logger.getLogger(ActionManager.class
	public static Logger logger = LoggerFactory.getLogger(ActionManager.class.getName());
	private String actionConfigFile;
	private String fileRoot;
	private static ActionManager self;
	private List<SrcFile> sfs = new ArrayList<SrcFile>();
	private List<String> srcdate = new ArrayList<String>();

	public static ActionManager getInstance() {
		return self;
	}

	public void initialize() {
		
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		try {
			sfs = RecordUtil.loadFilePage(actionConfigFile);
			for (SrcFile s : sfs)
				srcdate.add(s.getDate());
			self = this;
			if (logger.isInfoEnabled()) logger.info("{} initialize successfully [{}][saveFile:{}]", new Object[] { this.getClass().getName(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now), actionConfigFile });
		} catch (Exception e) {
			e.printStackTrace();
			if (logger.isInfoEnabled()) logger.info("{} initialize fail [saveFile:{}]", new Object[] { this.getClass().getName(), actionConfigFile });
		}
		ServiceStartRecord.startLog(this);
	}

	public void save(String gmname, String action) {
		String filename = "";
		try {
			String date = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
			String time = DateUtil.formatDate(new Date(), "HH:mm:ss");
			filename = fileRoot + "action-" + date + ".xml";
			action = action + Translate.text_4227 + time + "】";
			if (!srcdate.contains(date)) {
				List<Action> acs = new ArrayList<Action>();
				Action a = new Action();
				a.setGmname(gmname);
				a.setAction(action);
				SrcFile sf = new SrcFile();
				sf.setDate(date);
				sf.setSrcname(filename);
				srcdate.add(date);
				sfs.add(sf);
				RecordUtil.saveFilePage(sfs, actionConfigFile);
				RecordUtil.saveActionPage(acs, filename);
			} else if (srcdate.contains(date)) {
				List<Action> acs = RecordUtil.loadActionPage(filename);
				Action a = new Action();
				a.setGmname(gmname);
				a.setAction(action);
				acs.add(a);
				RecordUtil.saveActionPage(acs, filename);
			}

			// logger.info(gmname + " do a action[" + action
			// + "] save successfully at " + new Date());
			if (logger.isInfoEnabled()) logger.info("{} do a action[{}] save successfully at {}", new Object[] { gmname, action, new Date() });
		} catch (Exception e) {
			e.printStackTrace();
			// logger.info(gmname + " do a action[" + action + "] save fail at "
			// + new Date());
			if (logger.isInfoEnabled()) logger.info("{} do a action[{}] save fail at {}", new Object[] { gmname, action, new Date() });
		}

	}

	public List<Action> getActions(String date) {
		List<Action> acs = new ArrayList<Action>();
		acs = RecordUtil.loadActionPage(getsrcname(date));
		return acs;
	}

	private String getsrcname(String date) {
		for (SrcFile sf : sfs) {
			if (date.equals(sf.getDate())) {
				return sf.getSrcname();
			}
		}
		return null;
	}

	public String getActionConfigFile() {
		return actionConfigFile;
	}

	public void setActionConfigFile(String actionConfigFile) {
		this.actionConfigFile = actionConfigFile;
	}

	public String getFileRoot() {
		return fileRoot;
	}

	public void setFileRoot(String fileRoot) {
		this.fileRoot = fileRoot;
	}

	public List<SrcFile> getSfs() {
		return sfs;
	}

	public void setSfs(List<SrcFile> sfs) {
		this.sfs = sfs;
	}

	public List<String> getSrcdate() {
		return srcdate;
	}

	public void setSrcdate(List<String> srcdate) {
		this.srcdate = srcdate;
	}

}
