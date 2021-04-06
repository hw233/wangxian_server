package com.fy.boss.gm.faq;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.fy.boss.gm.faq.FaqGameModule;
import com.fy.boss.gm.faq.FaqManager;
import com.fy.boss.gm.faq.FaqRecord;
import com.xuanzhi.stat.model.Map;
import com.xuanzhi.tools.configuration.Configuration;
import com.xuanzhi.tools.configuration.ConfigurationException;
import com.xuanzhi.tools.configuration.DefaultConfiguration;
import com.xuanzhi.tools.configuration.DefaultConfigurationBuilder;
import com.xuanzhi.tools.configuration.DefaultConfigurationSerializer;
import com.xuanzhi.tools.text.FileUtils;


public class FaqManager {
	
	public static Logger logger = Logger.getLogger(FaqManager.class);

	protected String faqrecord; 
	
	protected String faqgame;
	
	private static FaqManager self;
	
	public static FaqManager getInstance(){
		  return self;
	}
	
	//游戏和其对于的faq模块
	public List<FaqGameModule> games = new ArrayList<FaqGameModule>();
	
	public void setFaqrecord(String faqrecord) {
		this.faqrecord = faqrecord;
	}

	//faq记录
	private List<FaqRecord> records = new ArrayList<FaqRecord>();
	
	
	public void init(){
		long now = System.currentTimeMillis();
		try {
			games = loadgames(faqgame);
			records = loadRecords(faqrecord);
			self = this;
			if (logger.isInfoEnabled()) {
				logger.info(this.getClass().getName() + " initialize successfully ["+(System.currentTimeMillis()-now)+"][saveFile:"+faqgame+"]");
			}
		} catch (Exception e) {
			logger.warn(this.getClass().getName()+"init..."+e);
		}
		
	}
	//可以写成类方法
	public  List<FaqGameModule> loadgames(String xmlname) {
		long now = System.currentTimeMillis();
		try {
			Configuration rootConf = new DefaultConfigurationBuilder().buildFromFile(xmlname);
			Configuration typesConf[] = rootConf.getChildren();
			for (int i = 0; i < typesConf.length; i++) {
				FaqGameModule game = new FaqGameModule();
				String gamename = typesConf[i].getAttribute("gamename","");
				String recorder = typesConf[i].getAttribute("recorder", "");
				String recordtime = typesConf[i].getAttribute("recordtime", "");
				String url = typesConf[i].getAttribute("url", "");
				String module = typesConf[i].getAttribute("module", "");
				game.setModule(module);
				game.setGamename(gamename);
				game.setRecorder(recorder);
				game.setRecordtime(recordtime);
				game.setUrl(url);
//				Configuration serverConf = typesConf[i].getChild("modules");
//				Configuration modulesConf[] = serverConf.getChildren();
//				List<String> modules = new ArrayList<String>();
//				for (int j = 0; j < modulesConf.length; j++) {
//					modules.add(modulesConf[j].getAttribute("name"));
//				}
//				game.setModules(modules);
				games.add(game);
			}
			if (logger.isInfoEnabled()) {
				logger.info("[加载faq游戏模块] [成功] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			}
			return games;
		} catch (Exception e) {
			logger.warn("[加载faq游戏模块] [异常] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
			return new ArrayList<FaqGameModule>();
		}
	}
	
	//加载records
	public  List<FaqRecord> loadRecords(String xmlname) {
		long now = System.currentTimeMillis();
		try {
			Configuration rootConf = new DefaultConfigurationBuilder().buildFromFile(xmlname);
			Configuration typesConf[] = rootConf.getChildren();
			for (int i = 0; i < typesConf.length; i++) {
				FaqRecord record = new FaqRecord();
				String gamename = typesConf[i].getAttribute("gamename","");
				String modulename = typesConf[i].getAttribute("modulename","");
				String recorder = typesConf[i].getAttribute("recorder","");
				String recordtime = typesConf[i].getAttribute("recordtime", "");
				String title = typesConf[i].getAttribute("title", "");
				String content = typesConf[i].getAttribute("content", "");
				record.setGamename(gamename);
				record.setModulename(modulename);
				record.setRecorder(recorder);
				record.setRecordtime(recordtime);
				record.setTitle(title);
				record.setContent(content);
				records.add(record);
			}
			if (logger.isInfoEnabled()) {
				logger.info("[加载faqRecords] [成功] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			}
			return records;
		} catch (Exception e) {
			logger.warn("[加载faqRecords] [异常] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
			return records;
		}
	}
	
	public boolean addNewGame(FaqGameModule game){
//		String sb = "";
		try {
			games.add(game);
//			List<String> modules = game.getModules();
//			for (int i = 0; i < modules.size(); i++) {
//				sb=sb+modules.get(i)+",";
//			}
			savegames(games,faqgame);
			if(logger.isInfoEnabled()){
				logger.info("[添加新游戏faq] [成功] [游戏名字："+game.getGamename()+"] [记录人："+game.getRecorder()+"] [记录时间："+game.getRecordtime()+"] [模块："+game.getModule()+"] [URL:"+game.getUrl()+"]");
			}
			return true;
		} catch (Exception e) {
			logger.warn("[添加新游戏faq] [异常] [游戏名字："+game.getGamename()+"] [记录人："+game.getRecorder()+"] [记录时间："+game.getRecordtime()+"] [模块："+game.getModule()+"] [URL:"+game.getUrl()+"]");
			return false;	
		}
	}
	
	//add a new record
	public boolean addNewRecord(FaqRecord record){
		try {
			records.add(record);
			saveRecords(records,faqrecord);
			if(logger.isInfoEnabled()){
				logger.info("[添加faqrecord] [成功] [游戏字名："+record.getGamename()+"] [记录人："+record.getRecorder()+"] [记录时间："+record.getRecordtime()+"] [模块名字："+record.getModulename()+"] [标题："+record.getTitle()+"] [内容："+record.getContent()+"] ");
			}
			return true;
		} catch (Exception e) {
			logger.info("[添加faqrecord] [成功] [游戏字名："+record.getGamename()+"] [记录人："+record.getRecorder()+"] [记录时间："+record.getRecordtime()+"] [模块名字："+record.getModulename()+"] [标题："+record.getTitle()+"] [内容："+record.getContent()+"] ",e);
			return false;	
		}
	}
	
	public static void savegames(List<FaqGameModule> games, String saveFile)
			throws IllegalArgumentException, IllegalAccessException,
			SAXException, IOException, ConfigurationException,
			InvocationTargetException {
		long now = System.currentTimeMillis();
		Configuration rootConf = new DefaultConfiguration("games", "-");
		for (FaqGameModule type : games) {
			Configuration typeConf = new DefaultConfiguration("game", "-");
			typeConf.setAttribute("gamename", type.getGamename());
			typeConf.setAttribute("recorder", type.getRecorder());
			typeConf.setAttribute("recordtime", type.getRecordtime());
			typeConf.setAttribute("url", type.getUrl());
			typeConf.setAttribute("module", type.getModule());
			rootConf.addChild(typeConf);
//			Configuration modulesConf = new DefaultConfiguration("modules", "-");
//			rootConf.addChild(modulesConf);
//			for (String m : type.getModules()) {
//				Configuration moduleConf1 = new DefaultConfiguration("module","-");
//				moduleConf1.setAttribute("name", m);
//				modulesConf.addChild(moduleConf1);
//			}
		}
		FileUtils.chkFolder(saveFile);
		new DefaultConfigurationSerializer().serializeToFile(new File(saveFile), rootConf);
		if (logger.isInfoEnabled()) {
			logger.info("[保存faq游戏模块] [成功] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		}
	}
	
	//save records
	public static void saveRecords(List<FaqRecord> records, String saveFile)
			throws IllegalArgumentException, IllegalAccessException,
			SAXException, IOException, ConfigurationException,
			InvocationTargetException {
		long now = System.currentTimeMillis();
		Configuration rootConf = new DefaultConfiguration("records", "-");
		for (FaqRecord type : records) {
			Configuration typeConf = new DefaultConfiguration("record", "-");
			typeConf.setAttribute("gamename", type.getGamename());
			typeConf.setAttribute("modulename", type.getModulename());
			typeConf.setAttribute("recorder", type.getRecorder());
			typeConf.setAttribute("recordtime", type.getRecordtime());
			typeConf.setAttribute("title", type.getTitle());
			typeConf.setAttribute("content", type.getContent());
			rootConf.addChild(typeConf);
		}
		FileUtils.chkFolder(saveFile);
		new DefaultConfigurationSerializer().serializeToFile(new File(saveFile), rootConf);
		if (logger.isInfoEnabled()) {
			logger.info("[保存faqRecords] [成功] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		}
	}
	
	public LinkedHashMap<String, ArrayList<String[]>> getMap(){
		LinkedHashMap<String, ArrayList<String[]>> gameurl = new LinkedHashMap<String, ArrayList<String[]>>();
		long now = System.currentTimeMillis();
		for(FaqGameModule faq:games){
			String[] module = new String[2];
			if(faq.getModule().trim().length()>0&&faq.getUrl().trim().length()>0){
				if(gameurl.size()>0&&gameurl.containsKey(faq.getGamename())){
					ArrayList<String[]> modules = new ArrayList<String[]>();
					modules = gameurl.get(faq.getGamename());
					module[0]=faq.getModule();
					module[1]=faq.getUrl();
					modules.add(module);
					gameurl.put(faq.getGamename(), modules);
				}else {
					ArrayList<String[]> mods = new ArrayList<String[]>();
					module[0]=faq.getModule();
					module[1]=faq.getUrl();
					mods.add(module);
					gameurl.put(faq.getGamename(), mods);
				}
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("[获得gameurls] [成功] [长度："+gameurl.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		}
		return gameurl;	
	}
	
	public List<FaqRecord> getRecords(){
		return records;
	}
	
	public String getFaqgame() {
		return faqgame;
	}

	public void setFaqgame(String faqgame) {
		this.faqgame = faqgame;
	}

	public String getFaqrecord() {
		return faqrecord;
	}

	public List<FaqGameModule> getGames() {
		return games;
	}
	
}
