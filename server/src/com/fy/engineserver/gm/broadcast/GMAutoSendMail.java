package com.fy.engineserver.gm.broadcast;

import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.sprite.Player;

public class GMAutoSendMail {

//	protected static final Log logger = LogFactory.getLog(GMAutoSendMail.class
public	static Logger logger = LoggerFactory.getLogger(GMAutoSendMail.class
			.getName());
	private static GMAutoSendMail self;

	public static GMAutoSendMail getInstance() {
		return self;
	}

	private boolean running = false;
	private long stop = -1l;
	private String[] arslist;
	private String title;
	private String content;
	long starttime;
	/**
	 * 用户名和发送时间对应
	 */
	public static Hashtable<String, String> compensateMap = new Hashtable<String, String>();

	public void init() throws Exception {
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		self = this;
		System.out.println("[系统初始化] [自动发公测补偿礼品开启] [初始化成功] ["
				+ getClass().getName() + "] [耗时："
				+ (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "毫秒]");
if(logger.isInfoEnabled())
	logger.info("{} initialize successfully [{}]", new Object[]{this.getClass().getName(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)});
	}

	public void start() {
		if (!running) {
			running = true;
			starttime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			/*
			 * PlayerManager pmanager = PlayerManager.getInstance(); Player[] ps
			 * = pmanager.getOnlinePlayers(); for(Player p :ps){ sendLogic(p); }
			 */
		}
	}

	public void stop() {
		running = false;
		stop = -1l;
		compensateMap.clear();
	}

	public void sendLogic(Player player) {}

	public static Hashtable<String, String> getCompensateMap() {
		return compensateMap;
	}

	public static void setCompensateMap(Hashtable<String, String> compensateMap) {
		GMAutoSendMail.compensateMap = compensateMap;
	}

	public String[] getArslist() {
		return arslist;
	}

	 
	public boolean setArslist(String[] arslist) {
		ArticleManager amanager = ArticleManager.getInstance();
		this.arslist = arslist;
		for(int i=0;i<arslist.length;i++){
			if(amanager.getArticle(arslist[i])==null)
				return false;
		}
		return true;
	}

	public ArticleEntity[] getEntitylist(String[] arslist1,Player p) {
		
		return null;
	}

	public long getStop() {
		return stop;
	}

	public void setStop(long stop) {
		this.stop = stop;
	}

	public long getStarttime() {
		return starttime;
	}

	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
