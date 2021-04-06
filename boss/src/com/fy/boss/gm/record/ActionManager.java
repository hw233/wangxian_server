package com.fy.boss.gm.record;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.fy.boss.gm.record.Action;
import com.fy.boss.gm.record.ActionManager;
import com.fy.boss.gm.record.ActionUtil;
import com.fy.boss.gm.record.SrcFile;
import com.xuanzhi.tools.text.DateUtil;

public class ActionManager {
	private static Logger logger = Logger.getLogger(ActionManager.class
			.getName());
	private String fileroot;
	private String actionsrcfile;
	private static ActionManager self;
	private List<SrcFile> sfs = new ArrayList<SrcFile>();
	private List<String> srcdate = new ArrayList<String>();

	public static ActionManager getInstance() {
		return self;
	}

	public void initialize() {
		long now = System.currentTimeMillis();
		try {
			self = this;
			sfs = ActionUtil.loadFilePage(actionsrcfile);
			for (int i=sfs.size()-1;i>-1;i--) {
				SrcFile s = sfs.get(i);
				srcdate.add(s.getDate());
			}
			
			System.out.println(this.getClass().getName()
					+ " initialize successfully ["
					+ (System.currentTimeMillis() - now) + "]");
			logger.info(this.getClass().getName()
					+ " initialize successfully ["
					+ (System.currentTimeMillis() - now) + "][saveFile:"
					+ fileroot + "]");
		} catch (Exception e) {
			logger.info(this.getClass().getName()
					+ " initialize fail [saveFile:" + fileroot + "]");
		}
	}

	public void save(String gmname, String action) {
		String date = DateUtil.formatDate(new Date(), "yyyy-MM");
		String filename = "";
		try {
			List<Action> acs = null;
			filename = fileroot + "action-" + date + ".xml";
			Action ac = new Action();
			ac.setAction(action + com.xuanzhi.language.translate.text_1675
					+ DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss")
					+ "]");
			ac.setGmname(gmname);
			if (!srcdate.contains(date)) {
				acs = new ArrayList<Action>();
				SrcFile sf = new SrcFile();
				sf.setDate(date);
				sf.setSrcname(filename);
				sfs.add(sf);
				acs.add(ac);
				srcdate.add(date);
				ActionUtil.saveActionPage(acs, filename);
				ActionUtil.saveFilePage(sfs, actionsrcfile);
			} else {
				acs = ActionUtil.loadActionPage(filename);
				acs.add(ac);
				ActionUtil.saveActionPage(acs, filename);
			}
			logger.info(gmname + " do a action[" + action
					+ "] save successfully " );
		} catch (Exception e) {
			logger.info(gmname + " do a action[" + action + "] save fail  ");
		}

	}
    
	public List<Action> getActions(String date) {
		List<Action> acs = new ArrayList<Action>();
		acs = ActionUtil.loadActionPage(getsrcname(date));
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
	
	public String getFileroot() {
		return fileroot;
	}

	public void setFileroot(String fileroot) {
		this.fileroot = fileroot;
	}

	public String getActionsrcfile() {
		return actionsrcfile;
	}

	public void setActionsrcfile(String actionsrcfile) {
		this.actionsrcfile = actionsrcfile;
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
