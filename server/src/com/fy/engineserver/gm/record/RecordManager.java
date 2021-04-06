package com.fy.engineserver.gm.record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.text.DateUtil;

public class RecordManager {
	// 聊天记录控制
	// protected static Logger logger = Logger.getLogger(RecordManager.class
	public static Logger logger = LoggerFactory.getLogger(RecordManager.class.getName());
	private String fileConfigFile;
	/**
	 * 目录文件
	 */
	private static RecordManager self;
	private String rootaddress;
	/**
	 * 根地址
	 */
	private List<SrcFile> srcfiles = new ArrayList<SrcFile>();
	private List<String> srcdate = new ArrayList<String>();

	public void initialize() {
		
		// 初始化
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		try {
			srcfiles = RecordUtil.loadFilePage(fileConfigFile);// 加载目录文件
			for (SrcFile s : srcfiles)
				// 加载简单文件标题
				srcdate.add(s.getDate());
			self = this;
			if (logger.isInfoEnabled()) logger.info("{} initialize successfully srcfiles length is {}[{}][saveFile:{}]", new Object[] { this.getClass().getName(), srcfiles.size(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now), fileConfigFile });
		} catch (Exception e) {
			e.printStackTrace();
			if (logger.isInfoEnabled()) logger.info("{} initialize fail [saveFile:{}]", new Object[] { this.getClass().getName(), fileConfigFile });
		}
		ServiceStartRecord.startLog(this);
	}

	public static RecordManager getInstance() {
		return self;
	}

	public void saveRecord(List<RecordMessage> rms, String sname) {
		// 保存记录
		String filename = "";
		try {
			String date = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
			String sdate = sname + "-" + date;
			filename = rootaddress + "chatlog-" + sdate + ".xml";
			if (rms.size() > 0) {
				if (!srcdate.contains(sdate)) {
					srcdate.add(sdate);
					SrcFile sf = new SrcFile();
					sf.setDate(sdate);
					sf.setSrcname(filename);
					srcfiles.add(sf);
					RecordUtil.saveFilePage(srcfiles, fileConfigFile);
					RecordUtil.saveRecordPage(rms, filename);
				} else if (srcdate.contains(sdate)) {
					List<RecordMessage> rss = RecordUtil.loadRecordPage(filename);
					rss.addAll(rms);
					RecordUtil.saveRecordPage(rss, filename);
				}
				// logger.info("save a group of recordmessage success at "
				// + new Date());
				if (logger.isInfoEnabled()) logger.info("save a group of recordmessage success at {}", new Object[] { new Date() });
			}
		} catch (Exception e) {
			// logger.info(filename + "save a group of recordmessage fail at "
			// + new Date() );
			if (logger.isInfoEnabled()) logger.info("{}save a group of recordmessage fail at {}", new Object[] { filename, new Date() });
			e.printStackTrace();
		}
	}

	public List<RecordMessage> getSDayMessage(String date, String gmname, String sname) {
		// 获取某一天某一个gm的私聊日志
		String sdate = sname + date;
		try {
			List<RecordMessage> rms = new ArrayList<RecordMessage>();
			List<RecordMessage> rss = RecordUtil.loadRecordPage(getsrcname(sdate));
			for (RecordMessage r : rss) {
				if (gmname.equals(r.getToname()) || gmname.equals(r.getFname())) rms.add(r);
			}
			// logger.info("it has been read success and firename is "+getsrcname(sdate)+" and gmname is "+gmname);
			if (logger.isInfoEnabled()) logger.info("it has been read success and firename is {} and gmname is {}", new Object[] { getsrcname(sdate), gmname });
			return rms;
		} catch (Exception e) {
			// logger.info("it has been read fail and firename is "+getsrcname(sdate)+" and gmname is "+gmname);
			if (logger.isInfoEnabled()) logger.info("it has been read fail and firename is {} and gmname is {}", new Object[] { getsrcname(sdate), gmname });
			e.printStackTrace();
			return null;
		}
	}

	public List<RecordMessage> getSDayMessage(String date, String sname) {
		// 获取某一天的某一个日志
		String sdate = sname + date;
		// logger.info("sdate is "+sdate);
		if (logger.isInfoEnabled()) logger.info("sdate is {}", new Object[] { sdate });
		try {
			List<RecordMessage> rss = RecordUtil.loadRecordPage(getsrcname(sdate));
			// logger.info("it has been read success and firename is "+getsrcname(sdate));
			if (logger.isInfoEnabled()) logger.info("it has been read success and firename is {}", new Object[] { getsrcname(sdate) });
			return rss;
		} catch (Exception e) {
			// logger.info("it has been read fail and firename is "+getsrcname(sdate));
			if (logger.isInfoEnabled()) logger.info("it has been read fail and firename is {}", new Object[] { getsrcname(sdate) });
			e.printStackTrace();
			return null;
		}
	}

	public List<RecordMessage> getSDayMessage(String date, String gmname, String pname, String sname) {
		// 获取某两个人的日志
		String sdate = sname + date;
		try {
			List<RecordMessage> rms = new ArrayList<RecordMessage>();
			List<RecordMessage> rss = RecordUtil.loadRecordPage(getsrcname(sdate));
			for (RecordMessage r : rss) {
				if ((gmname.equals(r.getToname()) && pname.equals(r.getFname())) || (gmname.equals(r.getFname()) && pname.equals(r.getToname()))) rms.add(r);
			}
			// logger.info("it has been read success and firename is "+getsrcname(sdate)+"  and playername is "+pname );
			if (logger.isInfoEnabled()) logger.info("it has been read success and firename is {}  and playername is {}", new Object[] { getsrcname(sdate), pname });
			return rms;
		} catch (Exception e) {
			// logger.info("it has been read fail and firename is "+getsrcname(sdate)+"  and playername is "+pname );
			if (logger.isInfoEnabled()) logger.info("it has been read fail and firename is {}  and playername is {}", new Object[] { getsrcname(sdate), pname });
			e.printStackTrace();
			return null;
		}
	}

	private String getsrcname(String sdate) {
		for (SrcFile sf : srcfiles) {
			if (sdate.equals(sf.getDate())) {
				return sf.getSrcname();
			}
		}
		return null;
	}

	public String getFileConfigFile() {
		return fileConfigFile;
	}

	public void setFileConfigFile(String fileConfigFile) {
		this.fileConfigFile = fileConfigFile;
	}

	public List<SrcFile> getSrcfiles() {
		return srcfiles;
	}

	public void setSrcfiles(List<SrcFile> srcfiles) {
		this.srcfiles = srcfiles;
	}

	public String getRootaddress() {
		return rootaddress;
	}

	public void setRootaddress(String rootaddress) {
		this.rootaddress = rootaddress;
	}

	public List<String> getSrcdate() {
		return srcdate;
	}

	public void setSrcdate(List<String> srcdate) {
		this.srcdate = srcdate;
	}

}
