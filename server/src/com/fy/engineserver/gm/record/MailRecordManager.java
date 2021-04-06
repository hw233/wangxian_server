package com.fy.engineserver.gm.record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuanzhi.tools.text.DateUtil;

public class MailRecordManager {
	// 记录gm操作
//	private static Logger logger = Logger.getLogger(MailRecordManager.class
public	static Logger logger = LoggerFactory.getLogger(MailRecordManager.class
			.getName());
	private String mailrecordConfigFile;
	private String fileRoot;
	private static MailRecordManager self;
	private List<SrcFile> sfs = new ArrayList<SrcFile>();
	private List<String> srcdate = new ArrayList<String>();

	public static MailRecordManager getInstance() {
		return self;
	}

	public void initialize() {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		try {
			sfs = RecordUtil.loadFilePage(mailrecordConfigFile);
			for (SrcFile s : sfs)
				srcdate.add(s.getDate());
			self = this;
			System.out.println(this.getClass().getName()
					+ " initialize successfully ["
					+ (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "]");
if(logger.isInfoEnabled())
	logger.info("{} initialize successfully [{}][saveFile:{}]", new Object[]{this.getClass().getName(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now),mailrecordConfigFile});
		} catch (Exception e) {
			e.printStackTrace();
if(logger.isInfoEnabled())
	logger.info("{} initialize fail [saveFile:{}]", new Object[]{this.getClass().getName(),mailrecordConfigFile});
		}
	}

	public void save(MailRecord mr) {
		String filename = "";
		try {
			String date = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
			String time = DateUtil.formatDate(new Date(), "HH:mm:ss");
			filename = fileRoot + "mailrecord-" + date + ".xml";
			mr.setResdate(time);
			if (!srcdate.contains(date)) {
				List<MailRecord> mrs = new ArrayList<MailRecord>();
				mrs.add(mr);
				SrcFile sf = new SrcFile();
				sf.setDate(date);
				sf.setSrcname(filename);
				srcdate.add(date);
				sfs.add(sf);
				RecordUtil.saveFilePage(sfs, mailrecordConfigFile);
				RecordUtil.saveMailRecordPage(mrs, filename);
			} else if (srcdate.contains(date)) {
				List<MailRecord> mrs = RecordUtil.loadMailRecordPage(filename);
				mrs.add(mr);
				RecordUtil.saveMailRecordPage(mrs, filename);
			}

//			logger.info(mr.getGmname() + " replay a mail successfully filename :"+filename);
			if(logger.isInfoEnabled())
				logger.info("{} replay a mail successfully filename :{}", new Object[]{mr.getGmname(),filename});
		} catch (Exception e) {
			e.printStackTrace();
//			logger.info(mr.getGmname() + " replay a mail fail filename:"+filename);
			if(logger.isInfoEnabled())
				logger.info("{} replay a mail fail filename:{}", new Object[]{mr.getGmname(),filename});
		}

	}

	public List<MailRecord> getActions(String date) {
		try {
			List<MailRecord> mrs = new ArrayList<MailRecord>();
			mrs = RecordUtil.loadMailRecordPage(getsrcname(date));
			if(logger.isInfoEnabled())
				logger.info("get mailrecord success");
			return mrs;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<MailRecord>();
		}
	}

	private String getsrcname(String date) {
		for (SrcFile sf : sfs) {
			if (date.equals(sf.getDate())) {
				return sf.getSrcname();
			}
		}
		return null;
	}

	public String getMailrecordConfigFile() {
		return mailrecordConfigFile;
	}

	public void setMailrecordConfigFile(String mailrecordConfigFile) {
		this.mailrecordConfigFile = mailrecordConfigFile;
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
