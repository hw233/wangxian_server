package com.fy.engineserver.gm.custom;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.configuration.Configuration;
import com.xuanzhi.tools.configuration.DefaultConfiguration;
import com.xuanzhi.tools.configuration.DefaultConfigurationBuilder;
import com.xuanzhi.tools.configuration.DefaultConfigurationSerializer;
import com.xuanzhi.tools.text.FileUtils;

public class WordFilter {
	// protected static Logger logger = Logger.getLogger(WordFilter.class.getName());
	public static Logger logger = LoggerFactory.getLogger(WordFilter.class.getName());
	protected String wordFile;

	private List<String> words = new ArrayList<String>();
	private static WordFilter self;

	public static WordFilter getInstance() {
		return self;
	}

	public void initialize() {
		
		// ......
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		try {
			words = loadPage(wordFile);
			self = this;
			if (logger.isInfoEnabled()) logger.info("{} initialize successfully [{}][saveFile:{}]", new Object[] { this.getClass().getName(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now), wordFile });
		} catch (Exception e) {
			if (logger.isInfoEnabled()) logger.info("{} initialize fail [saveFile:{}]", new Object[] { this.getClass().getName(), wordFile });
		}
		ServiceStartRecord.startLog(this);
	}

	public void insertCustom(String value, int index) {
		try {// 测试是否是值改变了
			words.add(index, value);
			savePage(words, wordFile);
			// logger.info("value: ["+value+"to "+wordFile+"] insert success at");
			if (logger.isInfoEnabled()) logger.info("value: [{}to {}] insert success at", new Object[] { value, wordFile });
		} catch (Exception e) {
			// logger.info("value: ["+value+"to "+wordFile+"] insert fail at");
			if (logger.isInfoEnabled()) logger.info("value: [{}to {}] insert fail at", new Object[] { value, wordFile });
		}
	}

	public void updateCustom(String value, int index) {
		try {
			words.set(index, value);
			savePage(words, wordFile);
			// logger.info("value :["+value+"] at "+index+" update success  ");
			if (logger.isInfoEnabled()) logger.info("value :[{}] at {} update success  ", new Object[] { value, index });
		} catch (Exception e) {
			// logger.info("value :["+value+"] at "+index+" update fail  ");
			if (logger.isInfoEnabled()) logger.info("value :[{}] at {} update fail  ", new Object[] { value, index });
		}

	}

	public void deleteCustom(int index) {
		try {
			words.remove(index);
			savePage(words, wordFile);
			// logger.info("index :["+index+"] delete success  ");
			if (logger.isInfoEnabled()) logger.info("index :[{}] delete success  ", new Object[] { index });
		} catch (Exception e) {
			// logger.info("index :["+index+"] delete faile  ");
			if (logger.isInfoEnabled()) logger.info("index :[{}] delete faile  ", new Object[] { index });
		}
	}

	public List<String> loadPage(String xmlname) {
		try {
			List<String> customs = new ArrayList<String>();
			Configuration rootConf = new DefaultConfigurationBuilder().buildFromFile(xmlname);
			Configuration customsConf[] = rootConf.getChildren();
			for (int i = 0; i < customsConf.length; i++) {
				customs.add(customsConf[i].getAttribute("value"));
			}
			return customs;
		} catch (Exception e) {
			if (logger.isInfoEnabled()) logger.info(" load page faile  ");
			return new ArrayList<String>();
		}
	}

	public void savePage(List<String> customs, String saveFile) throws Exception {
		Configuration rootConf = new DefaultConfiguration("words", "-");
		for (String custom : customs) {
			Configuration customConf = new DefaultConfiguration("word", "-");
			rootConf.addChild(customConf);
			customConf.setAttribute("value", custom);
		}
		FileUtils.chkFolder(saveFile);
		new DefaultConfigurationSerializer().serializeToFile(new File(saveFile), rootConf, "UTF-8");
	}

	public String getWordFile() {
		return wordFile;
	}

	public void setWordFile(String wordFile) {
		this.wordFile = wordFile;
	}

	public List<String> getWords() {
		return words;
	}

	public void setWords(List<String> words) {
		this.words = words;
	}

}
