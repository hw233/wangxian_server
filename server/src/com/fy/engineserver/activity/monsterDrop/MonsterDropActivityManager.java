package com.fy.engineserver.activity.monsterDrop;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.util.ServiceStartRecord;

public class MonsterDropActivityManager {
	
	public static Logger logger = LoggerFactory.getLogger(MonsterDropActivityManager.class);

	private static MonsterDropActivityManager instance;
	
	private String filePath;
	
	public static final long ONE_DATE = 24 * 60 * 60 * 1000;
	
	public String ACTIVITY_START_TIME = "2012-09-29 00:00:00";
	public long start_time;
	public String ACTIVITY_END_TIME = "2012-10-07 23:59:59";
	public long end_time;
	
	public boolean isActivityStart = false;
	
//	public int DROP_RANDOM = 1000;								//掉落几率  基准
	public int DROP_RANDOM = 100000000;							//掉落几率  基准
	
	public ArrayList<String> dropLog = new ArrayList<String>();		//掉落日志
	public static int DROP_LOG_MAX = 200;							//日志条数
	
	public boolean isOpen = false;								//这个是总开关
	
	public void addToDropLog(String log) {
		if (dropLog.size() < DROP_LOG_MAX) {
			dropLog.add(log);
		}else {
			dropLog.remove(0);
			dropLog.add(log);
		}
	}
	
	
	//基础数据
	private ArrayList<MonsterDropInfo> baseInfos = new ArrayList<MonsterDropInfo>();
	//掉落数据
	private ArrayList<Integer> dropNums = new ArrayList<Integer>();
	
	//掉落的年月日
	private long todayTime;
//	private int todayYue;
//	private int todayRi;
	
	//设置时间
	public boolean setStartEndTime(String startTime, String endTime){
		String start_end = " 00:00:00";
		String end_end = " 23:59:59";
		
		startTime = startTime + start_end;
		endTime = endTime + end_end;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long start = 0;
		long end = 0;
		try {
			start = format.parse(startTime).getTime();
		} catch (ParseException e) {
			logger.error("设置时间出错", e);
			return false;
		}
		try {
			end = format.parse(endTime).getTime();
		} catch (ParseException e) {
			logger.error("设置时间出错", e);
			return false;
		}
		
		ACTIVITY_START_TIME = startTime;
		ACTIVITY_END_TIME = endTime;
		this.start_time = start;
		this.end_time = end;
		isActivity(System.currentTimeMillis());
		return true;
	}
	
	public static MonsterDropActivityManager getInstance() {
		return instance;
	}
	
	Random random = new Random(System.currentTimeMillis());
	
	public synchronized void doDrop(Player player) {
		if (!isOpen) {
			return;
		}
		if (System.currentTimeMillis() - todayTime < ONE_DATE) {
			//还是今天
		}else {
			//不是今天，重新初始化掉落,并重新判断是不是  start end
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			todayTime = calendar.getTime().getTime();
			flushDropNum();
			isActivity(todayTime);
		}
		
		//时间在活动期间内，计算掉落
		if (isActivityStart) {
			for (int i = 0; i < dropNums.size(); i++) {
				int dropNum = dropNums.get(i);
				MonsterDropInfo info = baseInfos.get(i);
				if (dropNum < info.getNum() && player.getLevel() >= info.getDropLevelMin() && player.getLevel() < info.getDropLevelMax()) {
					//没掉落够 掉落
					int ramNum = random.nextInt(DROP_RANDOM);
					if (ramNum <= info.getDropRandom() * Player.得到玩家打怪掉落的pk惩罚百分比(player)) {
						//掉落
						createDrop(player, info);
						dropNum++;
						dropNums.remove(i);
						dropNums.add(i, dropNum);
						if (logger.isWarnEnabled()) {
							logger.warn("[掉落] [成功] {} {} [掉落数:{}] [随机几率：{}]", new Object[]{player.getLogString(), info.getLogString(), dropNum, ramNum});
						}
						break;
					}
				}
			}
		}
	}
	
	private void createDrop(Player player, MonsterDropInfo info) {
		if (info.getDropArticleNames() == null) {
			info.createDropArticleNames();
		}
		Random random = new Random();
		int index = random.nextInt(info.getDropArticleNames().size());
		Article article = ArticleManager.getInstance().getArticle(info.getDropArticleNames().get(index));
		if (article == null) {
			logger.error("[发放掉落失败] [物品不存在]"+info.getLogString());
			return;
		}
		int color = info.getColorType();
		if (info.getColorType() < 0) {
			color = article.getColorType();
		}
		ArticleEntity entity = null;
		try {
			entity = ArticleEntityManager.getInstance().createEntity(article, info.isBind(), ArticleEntityManager.运营活动_福泽天降祥瑞仙班, player, color, 1, true);
		} catch (Exception e) {
			logger.error("createDrop创建掉落出错:", e);
		}
		if (entity == null) {
			logger.error("[发放掉落失败] [物品创建失败]"+info.getLogString());
			return;
		}
		if (player.getKnapsack_common().isFull()) {
			//发邮件
			try {
				MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{entity}, new int[]{1}, "福泽天降祥瑞仙班", "恭喜您获得" + entity.getArticleName(), 0, 0, 0, "福泽天降祥瑞仙班");
			} catch (Exception e) {
				logger.error("createDrop发送邮件出错:", e);
			}
		}else {
			//直接加包裹
			player.putToKnapsacks(entity, "福泽天降祥瑞仙班");
		}
		try{
			Player[] players = PlayerManager.getInstance().getOnlinePlayers();
			for (int i = 0; i < players.length; i++) {
				players[i].send_HINT_REQ(player.getName()+"运气太好了，在国庆福泽天降活动中获得" + entity.getArticleName() + "道具，真心让人羡慕啊！", (byte)2);
			}
		}catch (Exception e) {
			logger.error("发消息出错:", e);
		}
		StringBuffer sb = new StringBuffer("");
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sb.append("[").append(format.format(date)).append("]");
		sb.append(" [").append(player.getId()).append("] [").append(player.getName()).append("]");
		sb.append(" [").append(player.getLevel()).append("] [").append(entity.getId()).append("]");
		sb.append(" [").append(entity.getArticleName()).append("] [").append(entity.getColorType()).append("]");
		addToDropLog(sb.toString());
		if (logger.isWarnEnabled()) {
			logger.warn("[掉落] [entityID:{}] [名字:{}] {} {}", new Object[]{entity.getId(), entity.getArticleName(), player.getLogString(), info.getLogString()});
		}
		//广播
		
	}
	
	public void init() throws Exception {
		
		instance = this;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		start_time = format.parse(ACTIVITY_START_TIME).getTime();
		end_time = format.parse(ACTIVITY_END_TIME).getTime();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		todayTime = calendar.getTime().getTime();
		isActivity(todayTime);
		loadFile();
		flushDropNum();
		ServiceStartRecord.startLog(this);
	}
	
	//看  这个日期   是不是在  start  和 end 之间
	private void isActivity(long time) {
		isActivityStart = false;
		if (time < end_time && time >= start_time) {
			isActivityStart = true;
		}
	}
	
	//读配置文件
	public void loadFile() throws Exception {
		File f = new File(getFilePath());
		if (!f.exists()) {
			throw new Exception("配置文件不存在");
		}
		
		Workbook workbook = Workbook.getWorkbook(f);
		Sheet sheet = workbook.getSheet(0);
		int maxRow = sheet.getRows();
		
		for (int i = 1; i < maxRow; i++) {
			Cell[] cells = sheet.getRow(i);
			if (cells.length != 8) {
				throw new Exception("配置错误，缺项");
			}
			MonsterDropInfo dropInfo = new MonsterDropInfo();
			baseInfos.add(dropInfo);
			for (int j = 0; j < cells.length; j++) {
				Cell cell = cells[j];
				switch(j) {
				case 0:
					dropInfo.setErjiName(cell.getContents());
					break;
				case 1:
					dropInfo.setPropName(cell.getContents());
					break;
				case 2:
					dropInfo.setColorType(Integer.parseInt(cell.getContents()));
					break;
				case 3:
					dropInfo.setBind(Integer.parseInt(cell.getContents()) == 1);
					break;
				case 4:
					dropInfo.setNum(Integer.parseInt(cell.getContents()));
					break;
				case 5:
					dropInfo.setDropRandom(Integer.parseInt(cell.getContents()));
					break;
				case 6:
					dropInfo.setDropLevelMin(Integer.parseInt(cell.getContents()));
					break;
				case 7:
					dropInfo.setDropLevelMax(Integer.parseInt(cell.getContents()));
					break;
				}
			}
			logger.warn("[初始化] {}", new Object[]{dropInfo.getLogString()});
		}
		
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePath() {
		return filePath;
	}
	
	public ArrayList<MonsterDropInfo> getBaseInfos() {
		return baseInfos;
	}

	public void setBaseInfos(ArrayList<MonsterDropInfo> baseInfos) {
		this.baseInfos = baseInfos;
	}
	
	public ArrayList<Integer> getDropNums() {
		return dropNums;
	}

	public void setDropNums(ArrayList<Integer> dropNums) {
		this.dropNums = dropNums;
	}
	
	public void flushDropNum() {
		if (logger.isWarnEnabled()) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < dropNums.size(); i++) {
				sb.append(baseInfos.get(i).getErjiName()).append(" ").append(baseInfos.get(i).getPropName()).append(" ");
				sb.append(baseInfos.get(i).getColorType()).append(" ").append(dropNums.get(i)).append("/").append(baseInfos.get(i).getNum());
			}
			logger.warn("[刷新掉落] [掉落情况{}]", new Object[]{sb.toString()});
		}
		dropNums.clear();
		for (int i = 0; i < baseInfos.size(); i++) {
			dropNums.add(0);
		}
	}
}
