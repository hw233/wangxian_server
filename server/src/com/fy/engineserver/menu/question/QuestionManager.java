package com.fy.engineserver.menu.question;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.engineserver.datasource.language.TransferLanguage;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.text.XmlUtil;
import com.xuanzhi.tools.watchdog.ConfigFileChangedAdapter;
import com.xuanzhi.tools.watchdog.ConfigFileChangedListener;

public class QuestionManager implements ConfigFileChangedListener {
	private static QuestionManager instance;

	private File configFile;

	private File configFileTeacherDay;

	private File configFileMidAutumnDay;

	private File configFileAsianGames;

	private Map<String, Question> questionMap = new HashMap<String, Question>();

	private Map<String, Question> questionMapTeacherDay = new HashMap<String, Question>();

	private Map<String, Question> questionMapMidAutumnDay = new HashMap<String, Question>();

	private Map<String, Question> questionMapAsianGames = new HashMap<String, Question>();

	public static Random random = new Random(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());

	public Map<String, Question> getQuestionMapMidAutumnDay() {
		return questionMapMidAutumnDay;
	}

	// protected static Logger logger = Logger.getLogger(QuestionManager.class);
	public static Logger logger = LoggerFactory.getLogger(QuestionManager.class);

	public static QuestionManager getInstance() {
		return instance;
	}

	public File getConfigFileMidAutumnDay() {
		return configFileMidAutumnDay;
	}

	public void setConfigFileMidAutumnDay(File configFileMidAutumnDay) {
		this.configFileMidAutumnDay = configFileMidAutumnDay;
	}

	public File getConfigFile() {
		return configFile;
	}

	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}

	public void loadFrom(File file) throws Exception {
		InputStream is = new FileInputStream(file);
		loadFromInputStream(is);
		is.close();
	}

	private void loadTeacherDayQuestions(File file) throws Exception {
		this.questionMapTeacherDay.clear();
		InputStream is = new FileInputStream(file);
		Document document = XmlUtil.load(is);
		Element de = document.getDocumentElement();

		Element[] es = XmlUtil.getChildrenByName(de, "question");
		for (Element e : es) {
			int id = XmlUtil.getAttributeAsInteger(e, "id", -1);
			String name = XmlUtil.getAttributeAsString(e, "name", "", TransferLanguage.getMap());
			String description = XmlUtil.getAttributeAsString(e, "description", "", TransferLanguage.getMap());
			int answerIndex = XmlUtil.getAttributeAsInteger(e, "answerIndex", -1);

			Question q = new Question();
			q.setName(name);
			q.setId(id);
			q.setDescription(description);
			q.setRightOption((byte) answerIndex);

			Element[] es2 = XmlUtil.getChildrenByName(e, "answer");
			List<String> l = new ArrayList<String>();
			for (Element e2 : es2) {
				String value = XmlUtil.getAttributeAsString(e2, "value", "", TransferLanguage.getMap());
				l.add(value);
			}

			q.setOptions(l.toArray(new String[0]));

			this.questionMapTeacherDay.put(name, q);
		}
		// logger.warn("[教师节问题初始化] [成功] [数量："+this.questionMapTeacherDay.size()+"]");
		if (logger.isWarnEnabled()) logger.warn("[教师节问题初始化] [成功] [数量：{}]", new Object[] { this.questionMapTeacherDay.size() });
	}

	private void loadAsianGamesQuestions(File file) throws Exception {
		this.questionMapAsianGames.clear();
		InputStream is = new FileInputStream(file);
		Document document = XmlUtil.load(is);
		Element de = document.getDocumentElement();

		Element[] es = XmlUtil.getChildrenByName(de, "question");
		for (Element e : es) {
			int id = XmlUtil.getAttributeAsInteger(e, "id", -1);
			String name = XmlUtil.getAttributeAsString(e, "name", "", TransferLanguage.getMap());
			String description = XmlUtil.getAttributeAsString(e, "description", "", TransferLanguage.getMap());
			int answerIndex = XmlUtil.getAttributeAsInteger(e, "answerIndex", -1);

			Question q = new Question();
			q.setName(name);
			q.setId(id);
			q.setDescription(description);
			q.setRightOption((byte) answerIndex);

			Element[] es2 = XmlUtil.getChildrenByName(e, "answer");
			List<String> l = new ArrayList<String>();
			for (Element e2 : es2) {
				String value = XmlUtil.getAttributeAsString(e2, "value", "", TransferLanguage.getMap());
				l.add(value);
			}

			q.setOptions(l.toArray(new String[0]));

			this.questionMapAsianGames.put(name, q);
		}
		// logger.warn("[亚运会问题初始化] [成功] [数量："+this.questionMapAsianGames.size()+"]");
		if (logger.isWarnEnabled()) logger.warn("[亚运会问题初始化] [成功] [数量：{}]", new Object[] { this.questionMapAsianGames.size() });
	}

	private void loadMidAutumnDayQuestions(File file) throws Exception {
		this.questionMapMidAutumnDay.clear();
		InputStream is = new FileInputStream(file);
		Document document = XmlUtil.load(is);
		Element de = document.getDocumentElement();

		Element[] es = XmlUtil.getChildrenByName(de, "question");
		for (Element e : es) {
			int id = XmlUtil.getAttributeAsInteger(e, "id", -1);
			String name = XmlUtil.getAttributeAsString(e, "name", "", TransferLanguage.getMap());
			String description = XmlUtil.getAttributeAsString(e, "description", "", TransferLanguage.getMap());
			int answerIndex = XmlUtil.getAttributeAsInteger(e, "answerIndex", -1);

			Question q = new Question();
			q.setName(name);
			q.setId(id);
			q.setDescription(description);
			q.setRightOption((byte) answerIndex);

			Element[] es2 = XmlUtil.getChildrenByName(e, "answer");
			List<String> l = new ArrayList<String>();
			for (Element e2 : es2) {
				String value = XmlUtil.getAttributeAsString(e2, "value", "", TransferLanguage.getMap());
				l.add(value);
			}

			q.setOptions(l.toArray(new String[0]));

			this.questionMapMidAutumnDay.put(name, q);
		}
		// logger.warn("[中秋节问题初始化] [成功] [数量："+this.questionMapMidAutumnDay.size()+"]");
		if (logger.isWarnEnabled()) logger.warn("[中秋节问题初始化] [成功] [数量：{}]", new Object[] { this.questionMapMidAutumnDay.size() });
	}

	public void loadFromInputStream(InputStream is) throws Exception {
		Document document = XmlUtil.load(is);
		Element de = document.getDocumentElement();

		Element[] es = XmlUtil.getChildrenByName(de, "question");
		for (Element e : es) {
			int id = XmlUtil.getAttributeAsInteger(e, "id", -1);
			String name = XmlUtil.getAttributeAsString(e, "name", "", TransferLanguage.getMap());
			String description = XmlUtil.getAttributeAsString(e, "description", "", TransferLanguage.getMap());
			int answerIndex = XmlUtil.getAttributeAsInteger(e, "answerIndex", -1);

			Question q = new Question();
			q.setName(name);
			q.setId(id);
			q.setDescription(description);
			q.setRightOption((byte) answerIndex);

			Element[] es2 = XmlUtil.getChildrenByName(e, "answer");
			List<String> l = new ArrayList<String>();
			for (Element e2 : es2) {
				String value = XmlUtil.getAttributeAsString(e2, "value", "", TransferLanguage.getMap());
				l.add(value);
			}

			q.setOptions(l.toArray(new String[0]));

			questionMap.put(name, q);
		}
	}

	/**
	 * 从configFile里读取Question的内容，用名字作为主键保存到questionMap里
	 */
	public void init() {
		
		instance = this;
		long time = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ConfigFileChangedAdapter.getInstance().addListener(configFile, this);
		String className = getClass().getName();
		if (configFile == null || !configFile.exists()) {
			// logger.error("[系统初始化] [问题管理器] [初始化错误] [" + className + "] [耗时：" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - time)
			// + "毫秒][没有配置题库]");
			logger.error("[系统初始化] [问题管理器] [初始化错误] [{}] [耗时：{}毫秒][没有配置题库]", new Object[] { className, (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - time) });
		} else {
			questionMap.clear();
			try {
				loadFrom(configFile);
				this.loadTeacherDayQuestions(this.configFileTeacherDay);
				loadMidAutumnDayQuestions(configFileMidAutumnDay);
				this.loadAsianGamesQuestions(this.configFileAsianGames);
			} catch (Exception e) {
				e.printStackTrace();
				// logger.error("[系统初始化] [问题管理器] [初始化错误] [" + className + "] [耗时：" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - time)
				// + "毫秒]", e);
				logger.error("[系统初始化] [问题管理器] [初始化错误] [" + className + "] [耗时：" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - time) + "毫秒]", e);
			}
		}

		time = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - time;
		ServiceStartRecord.startLog(this);
	}

	public Question getQuestionByName(String questionName) {
		return questionMap.get(questionName);
	}

	public Collection<Question> getAllQuestion() {
		return questionMap.values();
	}

	public Collection<Question> getAllTeacherDayQuestion() {
		return this.questionMapTeacherDay.values();
	}

	public Collection<Question> getAllAsianGamesQuestion() {
		return this.questionMapAsianGames.values();
	}

	public Question getQuestionById(int id) {
		for (Question q : questionMap.values()) {
			if (q.getId() == id) {
				return q;
			}
		}

		return null;
	}

	public Question getMidAutumnDayQuestionById(int id) {
		for (Question q : questionMapMidAutumnDay.values()) {
			if (q.getId() == id) {
				return q;
			}
		}

		return null;
	}

	public Question getMidAutumnDayQuestionByRandom() {
		Question qq = null;
		int mapIndex = random.nextInt(questionMapMidAutumnDay.size());
		int count = 0;
		for (Question q : questionMapMidAutumnDay.values()) {
			if (count == mapIndex) {
				qq = q;
				break;
			}
			count++;
		}

		return qq;
	}

	public void fileChanged(File file) {
		questionMap.clear();
		try {
			loadFrom(file);
		} catch (Exception e) {
			logger.error("[问题管理器] [重新装载错误] [" + getClass().getName() + "]", e);
		}
	}

	public File getConfigFileTeacherDay() {
		return configFileTeacherDay;
	}

	public void setConfigFileTeacherDay(File configFileTeacherDay) {
		this.configFileTeacherDay = configFileTeacherDay;
	}

	public File getConfigFileAsianGames() {
		return configFileAsianGames;
	}

	public void setConfigFileAsianGames(File configFileAsianGames) {
		this.configFileAsianGames = configFileAsianGames;
	}
}
