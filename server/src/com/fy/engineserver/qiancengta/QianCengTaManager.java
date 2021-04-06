package com.fy.engineserver.qiancengta;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NEWQIANCENGTA_AUTO_PATA_REQ;
import com.fy.engineserver.message.NEWQIANCENGTA_FLUSH_REQ;
import com.fy.engineserver.message.NEWQIANCENGTA_GET_FIRST_REWARD_REQ;
import com.fy.engineserver.message.NEWQIANCENGTA_GET_FIRST_REWARD_RES;
import com.fy.engineserver.message.NEWQIANCENGTA_GET_REWARD_REQ;
import com.fy.engineserver.message.NEWQIANCENGTA_MANUAL_PATA_REQ;
import com.fy.engineserver.message.NEWQIANCENGTA_QUERY_INFO_REQ;
import com.fy.engineserver.message.NEWQIANCENGTA_REWARD_INFO_REQ;
import com.fy.engineserver.message.NEWQIANCENGTA_REWARD_INFO_RES;
import com.fy.engineserver.message.QIANCENGTA_AUTO_PATA_REQ;
import com.fy.engineserver.message.QIANCENGTA_FLUSH_REQ;
import com.fy.engineserver.message.QIANCENGTA_GET_REWARD_REQ;
import com.fy.engineserver.message.QIANCENGTA_MANUAL_PATA_REQ;
import com.fy.engineserver.message.QIANCENGTA_QUERY_INFO_REQ;
import com.fy.engineserver.message.QIANCENGTA_REWARD_INFO_REQ;
import com.fy.engineserver.message.QIANCENGTA_REWARD_INFO_RES;
import com.fy.engineserver.message.QIANCHENGTA_MANUAL_START_RES;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.qiancengta.info.QianCengTa_CengInfo;
import com.fy.engineserver.qiancengta.info.QianCengTa_CengMonsterInfo;
import com.fy.engineserver.qiancengta.info.QianCengTa_DaoInfo;
import com.fy.engineserver.qiancengta.info.QianCengTa_FlopSet;
import com.fy.engineserver.qiancengta.info.QianCengTa_TaInfo;
import com.fy.engineserver.qiancengta.info.RewardMsg;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.config.ConfigServiceManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class QianCengTaManager  extends SubSystemAdapter{

	public SimpleEntityManager<QianCengTa_Ta> em;
	
	private String file;
	private String activityFile; 
	
	private ArrayList<String> allMapNames = new ArrayList<String>();
	
	public static boolean QianCengTa_OPEN ;
	public static Random random = new Random();
	static QianCengTaManager self;
	public  Logger logger = LoggerFactory.getLogger(QianCengTa_Ta.class);
	
	public static QianCengTaManager getInstance(){
		return self;
	}
	public QianCengTaManager(){}
	public LruMapCache cache = new LruMapCache(10240,60 * 60 * 1000,false, Translate.千层塔);
	
	//排行榜单
	ArrayList<QianCengTa_Player> paihangList = new ArrayList<QianCengTa_Player>();
	boolean paihangNeedSave = false;
	
	//通过某难度道奖励活动
	public ArrayList<QianCengTaActivity_PassDao> passActivitys = new ArrayList<QianCengTaActivity_PassDao>();
	public ArrayList<QianCengTaActivity_RefDao> refActivitys = new ArrayList<QianCengTaActivity_RefDao>();
	
	private static int THREADS_NUM = 5;
	private QianCengTa_Thread[] threads;
	
	public QianCengTa_Thread[] getThreads() {
		return threads;
	}
	public void setThreads(QianCengTa_Thread[] threads) {
		this.threads = threads;
	}
	public void init() throws Exception{
		
		self = this;
		loadFile();
		em = SimpleEntityManagerFactory.getSimpleEntityManager(QianCengTa_Ta.class);
		QianCengTa_OPEN = true;
//		mainThread = new Thread(this,"QianCengta heartbeat");
//		mainThread.start();
		threads = new QianCengTa_Thread[THREADS_NUM];
		for (int i = 0; i < THREADS_NUM; i++) {
			threads[i] = new QianCengTa_Thread();
			threads[i].name = "QianCengta heartbeat" + (i+1);
			threads[i].start();
		}
		String serverName = GameConstants.getInstance().getServerName();
		if (serverName.equals("ST") && PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
			QianCengTa_Ta.isOpenHard = true;
		}
		
		File file = new File(getActivityFile());
		file = new File(ConfigServiceManager.getInstance().getFilePath(file));
		if (!file.exists()) {
			throw new Exception(getActivityFile() + " 配表不存在! " + file.getAbsolutePath());
		}
		InputStream is = new FileInputStream(file);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet1 = workbook.getSheetAt(0);
		for (int i = 1; i < sheet1.getPhysicalNumberOfRows(); i++) {
			HSSFRow row = sheet1.getRow(i);
			int nandu = StringTool.getCellValue(row.getCell(0), Integer.class);
			String startTime = StringTool.getCellValue(row.getCell(1), String.class);
			String endTime = StringTool.getCellValue(row.getCell(2), String.class);
			int platformType = StringTool.getCellValue(row.getCell(3), Integer.class);
			String[] serverNames = StringTool.getCellValue(row.getCell(4), String.class).split(",");
			String mailTile = StringTool.getCellValue(row.getCell(5), String.class);
			String mailMsg = StringTool.getCellValue(row.getCell(6), String.class);
			String[] rewardNames = StringTool.getCellValue(row.getCell(7), String.class).split(",");
			String[] rewardNums_str = StringTool.getCellValue(row.getCell(8), String.class).split(",");
			int[] rewardNums = new int[rewardNums_str.length];
			for(int j = 0; j < rewardNums_str.length; j++) {
				rewardNums[j] = Integer.parseInt(rewardNums_str[j]);
			}
			String[] rewardColors_str = StringTool.getCellValue(row.getCell(9), String.class).split(",");
			int[] rewardColors = new int[rewardColors_str.length];
			for(int j = 0; j < rewardColors_str.length; j++) {
				rewardColors[j] = Integer.parseInt(rewardColors_str[j]);
			}
			String[] rewardBinds_str = StringTool.getCellValue(row.getCell(10), String.class).split(",");
			boolean[] rewardBinds = new boolean[rewardBinds_str.length];
			for(int j = 0; j < rewardBinds_str.length; j++) {
				rewardBinds[j] = Boolean.parseBoolean(rewardBinds_str[j]);
			}
			QianCengTaActivity_RefDao r1 = new QianCengTaActivity_RefDao(
					TimeTool.formatter.varChar19.parse(startTime),
					TimeTool.formatter.varChar19.parse(endTime),
					nandu, 100000, platformType, serverNames, null, mailTile, mailMsg,
					rewardNames, rewardNums, rewardColors, rewardBinds
					);
			refActivitys.add(r1);
		}
		if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
			QianCengTa_Ta.isOpenHard = true;
//			QianCengTa_Ta.isOpenGulf = true;
		}
		if (PlatformManager.getInstance().isPlatformOf(Platform.腾讯)) {
			QianCengTa_Ta.isOpenHard = true;
		}
		
		if(logger.isWarnEnabled())
			logger.warn("[千层塔] [QianCengTaManager初始化成功，心跳线程启动] [成功]  ");
		ServiceStartRecord.startLog(this);
	}
	
	public void doPassDaoActivity (Player p, int nandu, int daoIndex) {
		for (QianCengTaActivity_PassDao passActivity : this.passActivitys) {
			if (passActivity.isStart(nandu, daoIndex)) {
				passActivity.sendReward(p);
			}
		}
	}
	
	public void doRefDaoActivity (Player p, int nandu, long money) {
		for (QianCengTaActivity_RefDao refActivity : this.refActivitys) {
			if (refActivity.isStart(nandu)) {
				refActivity.sendReward(p, money);
			}
		}
	}
	
	public void loadFile() throws Exception{
		File file = new File(getFile());
		InputStream is = new FileInputStream(file);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		{
			HSSFSheet sheet1 = workbook.getSheetAt(0);
			QianCengTa_TaInfo.taInfo = new QianCengTa_TaInfo();
			for (int i = 1; i < sheet1.getPhysicalNumberOfRows(); i++) {
				try{
					HSSFRow row = sheet1.getRow(i);
					int daoIndex = (int)(row.getCell(0).getNumericCellValue());
					QianCengTa_DaoInfo daoInfo = QianCengTa_TaInfo.taInfo.daoList.get(daoIndex);
					int cengIndex = (int)(row.getCell(1).getNumericCellValue());
					QianCengTa_CengInfo cengInfo = null;
					if (cengIndex < 0) {
						cengInfo = daoInfo.getHiddenCeng();
					}else {
						cengInfo = daoInfo.getCengList().get(cengIndex);
					}
					//地图名字
					cengInfo.setCengMapName(row.getCell(2).getStringCellValue());
					if (!allMapNames.contains(cengInfo.getCengMapName())) {
						allMapNames.add(cengInfo.getCengMapName());
					}
					//层时间限制
					cengInfo.setCengTime((long)row.getCell(3).getNumericCellValue());
					if (row.getCell(4) != null) {
						String winP = row.getCell(4).getStringCellValue();
						if (winP != null && !"".equals(winP)) {
							String[] winPs = winP.split("!");
							for (int pp = 0; pp < winPs.length; pp++) {
								String aaa = winPs[pp];
								String[] aaas = aaa.split(",");
								cengInfo.getKillMonster().put(Integer.valueOf(aaas[0]), Integer.valueOf(aaas[1]));
							}
						}
					}
				}catch(Exception e){
					logger.error("loadFile1=====" + i,e);
					throw e;
				}
			}
			
			HSSFSheet sheet2 = workbook.getSheetAt(1);
			for (int i = 1; i < sheet2.getPhysicalNumberOfRows(); i++) {
				try{
					HSSFRow row = sheet2.getRow(i);
					int daoIndex = (int)(row.getCell(0).getNumericCellValue());
					QianCengTa_DaoInfo daoInfo = QianCengTa_TaInfo.taInfo.daoList.get(daoIndex);
					int cengIndex = (int)(row.getCell(1).getNumericCellValue());
					QianCengTa_CengInfo cengInfo = null;
					if (cengIndex < 0) {
						cengInfo = daoInfo.getHiddenCeng();
					}else {
						cengInfo = daoInfo.getCengList().get(cengIndex);
					}
					QianCengTa_FlopSet info = new QianCengTa_FlopSet();
					info.setClientShowName(row.getCell(2).getStringCellValue());
					info.setArticleNames(row.getCell(3).getStringCellValue().split(","));
					info.setRandom((int)row.getCell(4).getNumericCellValue());
					info.setNum((int)row.getCell(5).getNumericCellValue());
					info.setColorType((int)row.getCell(6).getNumericCellValue());
					info.setBind(((int)row.getCell(7).getNumericCellValue()) == 0);
					cengInfo.getFlopSets().add(info);
				}catch(Exception e){
					logger.error("loadFile2=====" + i,e);
					throw e;
				}
			}
			
			HSSFSheet sheet3 = workbook.getSheetAt(2);
			for (int i = 1; i < sheet3.getPhysicalNumberOfRows(); i++) {
				try{
					HSSFRow row = sheet3.getRow(i);
					int daoIndex = (int)(row.getCell(0).getNumericCellValue());
					QianCengTa_DaoInfo daoInfo = QianCengTa_TaInfo.taInfo.daoList.get(daoIndex);
					int cengIndex = (int)(row.getCell(1).getNumericCellValue());
					QianCengTa_CengInfo cengInfo = null;
					if (cengIndex < 0) {
						cengInfo = daoInfo.getHiddenCeng();
					}else {
						cengInfo = daoInfo.getCengList().get(cengIndex);
					}
					QianCengTa_CengMonsterInfo monInfo = new QianCengTa_CengMonsterInfo();
					monInfo.setMonsterID((int)row.getCell(2).getNumericCellValue());
					String pos = row.getCell(3).getStringCellValue();
					String[] poss = pos.split("!");
					for (int ppL = 0; ppL < poss.length; ppL++) {
						String tm = poss[ppL];
						String[] tms = tm.split(",");
						monInfo.getPoss().add(new int[]{Integer.parseInt(tms[0]), Integer.parseInt(tms[1])});
					}
					monInfo.setRefTime((long)row.getCell(4).getNumericCellValue());
					cengInfo.addMonster(monInfo);
					logger.warn("[千层塔坐标] [道:"+daoIndex+"] [层:"+cengIndex+"] [mid:"+row.getCell(2).getNumericCellValue()+"] [poss:"+Arrays.toString(poss)+"]<br>");
				}catch(Exception e){
					logger.error("loadFile3=====" + i,e);
					throw e;
				}
			}
			
			for (int i = 0; i < QianCengTa_TaInfo.taInfo.daoList.size(); i++) {
				QianCengTa_DaoInfo daoInfo = QianCengTa_TaInfo.taInfo.daoList.get(i);
				for (int j = 0; j < daoInfo.getCengList().size(); j++) {
					QianCengTa_CengInfo cengInfo = daoInfo.getCengList().get(j);
					HashMap<Integer, Integer> err = new HashMap<Integer, Integer>();
					for (Iterator<Integer> key = cengInfo.getKillMonster().keySet().iterator(); key.hasNext();) {
						Integer k = key.next();
						Integer num = cengInfo.getKillMonster().get(k);
						int findNum = 0;
						for (int oo = 0; oo < cengInfo.getMonsters().size(); oo ++) {
							QianCengTa_CengMonsterInfo monInfo = cengInfo.getMonsters().get(oo);
							if (monInfo.getMonsterID() == k.intValue()) {
								findNum += monInfo.getPoss().size();
							}
						}
						if (findNum < num.intValue()) {
							logger.warn("录入存在问题，道{}，层{}，怪物ID{}，数目{}，刷新数目{}", new Object[]{daoInfo.getDaoIndex(), cengInfo.getCengIndex(), k, num, findNum});
							err.put(k, findNum);
						}
					}
					cengInfo.getKillMonster().putAll(err);
				}
			}
			QianCengTa_TaInfo.taInfo.costSilver = QianCengTa_TaInfo.commonCost;
		}
		
		//困难
		{
			HSSFSheet sheet1 = workbook.getSheetAt(3);
			QianCengTa_TaInfo.hardTaInfo = new QianCengTa_TaInfo();
			for (int i = 1; i < sheet1.getPhysicalNumberOfRows(); i++) {
				try{
					HSSFRow row = sheet1.getRow(i);
					int daoIndex = (int)(row.getCell(0).getNumericCellValue());
					QianCengTa_DaoInfo daoInfo = QianCengTa_TaInfo.hardTaInfo.daoList.get(daoIndex);
					int cengIndex = (int)(row.getCell(1).getNumericCellValue());
					QianCengTa_CengInfo cengInfo = null;
					if (cengIndex < 0) {
						cengInfo = daoInfo.getHiddenCeng();
					}else {
						cengInfo = daoInfo.getCengList().get(cengIndex);
					}
					//地图名字
					cengInfo.setCengMapName(row.getCell(2).getStringCellValue());
					if (!allMapNames.contains(cengInfo.getCengMapName())) {
						allMapNames.add(cengInfo.getCengMapName());
					}
					//层时间限制
					cengInfo.setCengTime((long)row.getCell(3).getNumericCellValue());
					if (row.getCell(4) != null) {
						String winP = row.getCell(4).getStringCellValue();
						if (winP != null && !"".equals(winP)) {
							String[] winPs = winP.split("!");
							for (int pp = 0; pp < winPs.length; pp++) {
								String aaa = winPs[pp];
								String[] aaas = aaa.split(",");
								cengInfo.getKillMonster().put(Integer.valueOf(aaas[0]), Integer.valueOf(aaas[1]));
							}
						}
					}
				}catch(Exception e){
					logger.error("loadFile1=====" + i,e);
					throw e;
				}
			}
			
			HSSFSheet sheet2 = workbook.getSheetAt(4);
			for (int i = 1; i < sheet2.getPhysicalNumberOfRows(); i++) {
				try{
					HSSFRow row = sheet2.getRow(i);
					int daoIndex = (int)(row.getCell(0).getNumericCellValue());
					QianCengTa_DaoInfo daoInfo = QianCengTa_TaInfo.hardTaInfo.daoList.get(daoIndex);
					int cengIndex = (int)(row.getCell(1).getNumericCellValue());
					QianCengTa_CengInfo cengInfo = null;
					if (cengIndex < 0) {
						cengInfo = daoInfo.getHiddenCeng();
					}else {
						cengInfo = daoInfo.getCengList().get(cengIndex);
					}
					QianCengTa_FlopSet info = new QianCengTa_FlopSet();
					info.setClientShowName(row.getCell(2).getStringCellValue());
					info.setArticleNames(row.getCell(3).getStringCellValue().split(","));
					info.setRandom((int)row.getCell(4).getNumericCellValue());
					info.setNum((int)row.getCell(5).getNumericCellValue());
					info.setColorType((int)row.getCell(6).getNumericCellValue());
					info.setBind(((int)row.getCell(7).getNumericCellValue()) == 0);
					cengInfo.getFlopSets().add(info);
				}catch(Exception e){
					logger.error("loadFile2=====" + i,e);
					throw e;
				}
			}
			
			HSSFSheet sheet3 = workbook.getSheetAt(5);
			for (int i = 1; i < sheet3.getPhysicalNumberOfRows(); i++) {
				try{
					HSSFRow row = sheet3.getRow(i);
					int daoIndex = (int)(row.getCell(0).getNumericCellValue());
					QianCengTa_DaoInfo daoInfo = QianCengTa_TaInfo.hardTaInfo.daoList.get(daoIndex);
					int cengIndex = (int)(row.getCell(1).getNumericCellValue());
					QianCengTa_CengInfo cengInfo = null;
					if (cengIndex < 0) {
						cengInfo = daoInfo.getHiddenCeng();
					}else {
						cengInfo = daoInfo.getCengList().get(cengIndex);
					}
					QianCengTa_CengMonsterInfo monInfo = new QianCengTa_CengMonsterInfo();
					monInfo.setMonsterID((int)row.getCell(2).getNumericCellValue());
					String pos = row.getCell(3).getStringCellValue();
					String[] poss = pos.split("!");
					for (int ppL = 0; ppL < poss.length; ppL++) {
						String tm = poss[ppL];
						String[] tms = tm.split(",");
						monInfo.getPoss().add(new int[]{Integer.parseInt(tms[0]), Integer.parseInt(tms[1])});
					}
					monInfo.setRefTime((long)row.getCell(4).getNumericCellValue());
					cengInfo.addMonster(monInfo);
				}catch(Exception e){
					logger.error("loadFile3=====" + i,e);
					throw e;
				}
			}
			
			for (int i = 0; i < QianCengTa_TaInfo.hardTaInfo.daoList.size(); i++) {
				QianCengTa_DaoInfo daoInfo = QianCengTa_TaInfo.hardTaInfo.daoList.get(i);
				for (int j = 0; j < daoInfo.getCengList().size(); j++) {
					QianCengTa_CengInfo cengInfo = daoInfo.getCengList().get(j);
					HashMap<Integer, Integer> err = new HashMap<Integer, Integer>();
					for (Iterator<Integer> key = cengInfo.getKillMonster().keySet().iterator(); key.hasNext();) {
						Integer k = key.next();
						Integer num = cengInfo.getKillMonster().get(k);
						int findNum = 0;
						for (int oo = 0; oo < cengInfo.getMonsters().size(); oo ++) {
							QianCengTa_CengMonsterInfo monInfo = cengInfo.getMonsters().get(oo);
							if (monInfo.getMonsterID() == k.intValue()) {
								findNum += monInfo.getPoss().size();
							}
						}
						if (findNum < num.intValue()) {
							logger.warn("录入存在问题，道{}，层{}，怪物ID{}，数目{}，刷新数目{}", new Object[]{daoInfo.getDaoIndex(), cengInfo.getCengIndex(), k, num, findNum});
							err.put(k, findNum);
						}
					}
					cengInfo.getKillMonster().putAll(err);
				}
			}
			QianCengTa_TaInfo.hardTaInfo.costSilver = QianCengTa_TaInfo.hardCost;
		}
		
		//深渊
		{
			HSSFSheet sheet1 = workbook.getSheetAt(6);
			QianCengTa_TaInfo.gulfTaInfo = new QianCengTa_TaInfo();
			for (int i = 1; i < sheet1.getPhysicalNumberOfRows(); i++) {
				try{
					HSSFRow row = sheet1.getRow(i);
					int daoIndex = (int)(row.getCell(0).getNumericCellValue());
					QianCengTa_DaoInfo daoInfo = QianCengTa_TaInfo.gulfTaInfo.daoList.get(daoIndex);
					int cengIndex = (int)(row.getCell(1).getNumericCellValue());
					QianCengTa_CengInfo cengInfo = null;
					if (cengIndex < 0) {
						cengInfo = daoInfo.getHiddenCeng();
					}else {
						cengInfo = daoInfo.getCengList().get(cengIndex);
					}
					//地图名字
					cengInfo.setCengMapName(row.getCell(2).getStringCellValue());
					if (!allMapNames.contains(cengInfo.getCengMapName())) {
						allMapNames.add(cengInfo.getCengMapName());
					}
					//层时间限制
					cengInfo.setCengTime((long)row.getCell(3).getNumericCellValue());
					if (row.getCell(4) != null) {
						String winP = row.getCell(4).getStringCellValue();
						if (winP != null && !"".equals(winP)) {
							String[] winPs = winP.split("!");
							for (int pp = 0; pp < winPs.length; pp++) {
								String aaa = winPs[pp];
								String[] aaas = aaa.split(",");
								cengInfo.getKillMonster().put(Integer.valueOf(aaas[0]), Integer.valueOf(aaas[1]));
							}
						}
					}
				}catch(Exception e){
					logger.error("loadFile1=====" + i,e);
					throw e;
				}
			}
			
			HSSFSheet sheet2 = workbook.getSheetAt(7);
			for (int i = 1; i < sheet2.getPhysicalNumberOfRows(); i++) {
				try{
					HSSFRow row = sheet2.getRow(i);
					int daoIndex = (int)(row.getCell(0).getNumericCellValue());
					QianCengTa_DaoInfo daoInfo = QianCengTa_TaInfo.gulfTaInfo.daoList.get(daoIndex);
					int cengIndex = (int)(row.getCell(1).getNumericCellValue());
					QianCengTa_CengInfo cengInfo = null;
					if (cengIndex < 0) {
						cengInfo = daoInfo.getHiddenCeng();
					}else {
						cengInfo = daoInfo.getCengList().get(cengIndex);
					}
					QianCengTa_FlopSet info = new QianCengTa_FlopSet();
					info.setClientShowName(row.getCell(2).getStringCellValue());
					info.setArticleNames(row.getCell(3).getStringCellValue().split(","));
					info.setRandom((int)row.getCell(4).getNumericCellValue());
					info.setNum((int)row.getCell(5).getNumericCellValue());
					info.setColorType((int)row.getCell(6).getNumericCellValue());
					info.setBind(((int)row.getCell(7).getNumericCellValue()) == 0);
					cengInfo.getFlopSets().add(info);
				}catch(Exception e){
					logger.error("loadFile2=====" + i,e);
					throw e;
				}
			}
			
			HSSFSheet sheet3 = workbook.getSheetAt(8);
			for (int i = 1; i < sheet3.getPhysicalNumberOfRows(); i++) {
				try{
					HSSFRow row = sheet3.getRow(i);
					int daoIndex = (int)(row.getCell(0).getNumericCellValue());
					QianCengTa_DaoInfo daoInfo = QianCengTa_TaInfo.gulfTaInfo.daoList.get(daoIndex);
					int cengIndex = (int)(row.getCell(1).getNumericCellValue());
					QianCengTa_CengInfo cengInfo = null;
					if (cengIndex < 0) {
						cengInfo = daoInfo.getHiddenCeng();
					}else {
						cengInfo = daoInfo.getCengList().get(cengIndex);
					}
					QianCengTa_CengMonsterInfo monInfo = new QianCengTa_CengMonsterInfo();
					monInfo.setMonsterID((int)row.getCell(2).getNumericCellValue());
					String pos = row.getCell(3).getStringCellValue();
					String[] poss = pos.split("!");
					for (int ppL = 0; ppL < poss.length; ppL++) {
						String tm = poss[ppL];
						String[] tms = tm.split(",");
						monInfo.getPoss().add(new int[]{Integer.parseInt(tms[0]), Integer.parseInt(tms[1])});
					}
					monInfo.setRefTime((long)row.getCell(4).getNumericCellValue());
					cengInfo.addMonster(monInfo);
				}catch(Exception e){
					logger.error("loadFile3=====" + i,e);
					throw e;
				}
			}
			
			for (int i = 0; i < QianCengTa_TaInfo.gulfTaInfo.daoList.size(); i++) {
				QianCengTa_DaoInfo daoInfo = QianCengTa_TaInfo.gulfTaInfo.daoList.get(i);
				for (int j = 0; j < daoInfo.getCengList().size(); j++) {
					QianCengTa_CengInfo cengInfo = daoInfo.getCengList().get(j);
					HashMap<Integer, Integer> err = new HashMap<Integer, Integer>();
					for (Iterator<Integer> key = cengInfo.getKillMonster().keySet().iterator(); key.hasNext();) {
						Integer k = key.next();
						Integer num = cengInfo.getKillMonster().get(k);
						int findNum = 0;
						for (int oo = 0; oo < cengInfo.getMonsters().size(); oo ++) {
							QianCengTa_CengMonsterInfo monInfo = cengInfo.getMonsters().get(oo);
							if (monInfo.getMonsterID() == k.intValue()) {
								findNum += monInfo.getPoss().size();
							}
						}
						if (findNum < num.intValue()) {
							logger.warn("录入存在问题，道{}，层{}，怪物ID{}，数目{}，刷新数目{}", new Object[]{daoInfo.getDaoIndex(), cengInfo.getCengIndex(), k, num, findNum});
							err.put(k, findNum);
						}
					}
					cengInfo.getKillMonster().putAll(err);
				}
			}
			QianCengTa_TaInfo.gulfTaInfo.costSilver = QianCengTa_TaInfo.gulfCost;
		}
		
		{
			//难度 道 奖励
			HSSFSheet sheet9 = workbook.getSheetAt(9);
			for (int i = 1; i < sheet9.getPhysicalNumberOfRows(); i++) {
				try{
					HSSFRow row = sheet9.getRow(i);
					int nandu = -1;
					try {
						nandu = Integer.parseInt(row.getCell(0).getStringCellValue());
					}catch (Exception e) {
						nandu = (int)(row.getCell(0).getNumericCellValue());
					}
					int daoNum = -1;
					try {
						daoNum = Integer.parseInt(row.getCell(1).getStringCellValue());
					}catch (Exception e) {
						daoNum = (int)(row.getCell(1).getNumericCellValue());
					}
					String articleNameS = row.getCell(2).getStringCellValue();
					String[] names = articleNameS.split(",");
					String articleColorS = row.getCell(3).getStringCellValue();
					String[] colors = articleColorS.split(",");
					String[] firstNames = row.getCell(4).getStringCellValue().split(",");
					String[] firstNums = null;
					try {
						firstNums = row.getCell(5).getStringCellValue().split(",");
					}catch(Exception e) {
						firstNums = ((int)(row.getCell(5).getNumericCellValue())+"").split(",");
					}
					int[] firstNumsInt = new int[firstNums.length];
					for (int kk = 0; kk < firstNums.length; kk++) {
						firstNumsInt[kk] = Integer.parseInt(firstNums[kk]);
					}
					String[] firstColors = null;
					try {
						firstColors = row.getCell(6).getStringCellValue().split(",");
					} catch(Exception e) {
						firstColors = ((int)(row.getCell(6).getNumericCellValue())+"").split(",");
					}
					int[] firstColorsInt = new int[firstColors.length];
					for (int kk = 0; kk < firstColors.length; kk++) {
						firstColorsInt[kk] = Integer.parseInt(firstColors[kk]);
					}
					String[] firstBinds = row.getCell(7).getStringCellValue().split(",");
					boolean[] firstBindsBoolean = new boolean[firstBinds.length];
					for (int kk = 0; kk < firstBinds.length; kk++) {
						firstBindsBoolean[kk] = Boolean.parseBoolean(firstBinds[kk]);
					}
//					logger.warn("[{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{nandu, daoNum, articleNameS, articleColorS, Arrays.toString(names), Arrays.toString(colors)});
					if (nandu == 0) {
						QianCengTa_DaoInfo daoInfo = QianCengTa_TaInfo.taInfo.daoList.get(daoNum);
						for (int x = 0; x < names.length; x++) {
							daoInfo.getRewardArticleNames().add(names[x]);
							daoInfo.getRewardArticleColors().add(Integer.parseInt(colors[x]));
						}
						daoInfo.setFirstRewardArticleNames(firstNames);
						daoInfo.setFirstRewardArticleColors(firstColorsInt);
						daoInfo.setFirstRewardArticleNums(firstNumsInt);
						daoInfo.setFirstRewardArticleBinds(firstBindsBoolean);
					}else if (nandu == 1) {
						QianCengTa_DaoInfo daoInfo = QianCengTa_TaInfo.hardTaInfo.daoList.get(daoNum);
						for (int x = 0; x < names.length; x++) {
							daoInfo.getRewardArticleNames().add(names[x]);
							daoInfo.getRewardArticleColors().add(Integer.parseInt(colors[x]));
						}
						daoInfo.setFirstRewardArticleNames(firstNames);
						daoInfo.setFirstRewardArticleColors(firstColorsInt);
						daoInfo.setFirstRewardArticleNums(firstNumsInt);
						daoInfo.setFirstRewardArticleBinds(firstBindsBoolean);
					}else if (nandu == 2) {
						QianCengTa_DaoInfo daoInfo = QianCengTa_TaInfo.gulfTaInfo.daoList.get(daoNum);
						for (int x = 0; x < names.length; x++) {
							daoInfo.getRewardArticleNames().add(names[x]);
							daoInfo.getRewardArticleColors().add(Integer.parseInt(colors[x]));
						}
						daoInfo.setFirstRewardArticleNames(firstNames);
						daoInfo.setFirstRewardArticleColors(firstColorsInt);
						daoInfo.setFirstRewardArticleNums(firstNumsInt);
						daoInfo.setFirstRewardArticleBinds(firstBindsBoolean);
					}
				}catch (Exception e) {
					logger.error("读取难度奖励出错:" + i, e);
				}
			}
		}
		{
			//载入层奖励
			HSSFSheet sheet10 = workbook.getSheetAt(10);
			for (int i = 1; i < sheet10.getPhysicalNumberOfRows(); i++) {
				try{
					HSSFRow row = sheet10.getRow(i);
					int nandu = -1;
					try {
						nandu = Integer.parseInt(row.getCell(0).getStringCellValue());
					}catch (Exception e) {
						nandu = (int)(row.getCell(0).getNumericCellValue());
					}
					int daoNum = -1;
					try {
						daoNum = Integer.parseInt(row.getCell(1).getStringCellValue());
					}catch (Exception e) {
						daoNum = (int)(row.getCell(1).getNumericCellValue());
					}
					int ceng = -1;
					try {
						ceng = Integer.parseInt(row.getCell(2).getStringCellValue());
					}catch (Exception e) {
						ceng = (int)(row.getCell(2).getNumericCellValue());
					}
					
					QianCengTa_TaInfo info = null;
					if (nandu == 0) {
						info = QianCengTa_TaInfo.taInfo;
					}else if (nandu == 1) {
						info = QianCengTa_TaInfo.hardTaInfo;
					}else if (nandu == 2) {
						info = QianCengTa_TaInfo.gulfTaInfo;
					}
					QianCengTa_CengInfo cengInfo = info.getCengInfo(daoNum, ceng);
					String ANames = row.getCell(3).getStringCellValue();
					cengInfo.setShowCengNames(ANames.split(","));
					int[] cc = new int[cengInfo.getShowCengNames().length];
					String CNames = "";
					try {
						CNames = row.getCell(4).getStringCellValue();
					} catch (Exception e) {
						CNames = ""+(int)(row.getCell(4).getNumericCellValue());
					}
					String[] ccS = CNames.split(",");
					for (int kk = 0; kk < ccS.length; kk++) {
						cc[kk] = Integer.parseInt(ccS[kk]);
					}
					cengInfo.setShowCengColors(cc);
				}catch (Exception e) {
					logger.error("读取道层奖励:" + i, e);
				}
			}
		}
	}
	
	public void destroy(){
		logger.warn("千层塔，系统destroy");
		em.destroy();
		QianCengTa_OPEN = false;
	}
	
	public QianCengTa_Player getPlayerInPaiHang(long playerId){
		for(int i = 0 ; i < paihangList.size() ; i++){
			QianCengTa_Player p = paihangList.get(i);
			if(p.getPlayerId() == playerId){
				return p;
			}
		}
		return null;
	}
	
	public QianCengTa_Ta getTa(long playerId){
		QianCengTa_Ta ta = (QianCengTa_Ta)cache.get(playerId);
		if(ta != null){
			return ta;
		}
		ta = loadTa(playerId);
		if(ta != null){
			cache.put(playerId, ta);
			int minIndex = 0;
			int minNum = 100000;
			for (int i = 0; i < threads.length; i++) {
				if (threads[i].getTaSize() < minNum) {
					minNum = threads[i].getTaSize();
					minIndex = i;
				}
			}
			threads[minIndex].addTa(ta);
			ta.setThreadIndex(minIndex);
		}
		return ta;
	}
	
	public QianCengTa_Ta getTaForUnitedServer(long playerId){
		QianCengTa_Ta ta = (QianCengTa_Ta)cache.get(playerId);
		if(ta != null){
			return ta;
		}
		try {
			ta = em.find(playerId);
			return ta;
		} catch (Exception e) {
			logger.error("从数据库中加载塔,出错：" + playerId, e);
			return null;
		}
	}
	
	public void removeTa(QianCengTa_Ta ta) {
		try{
			QianCengTaManager.getInstance().em.save(ta);
			threads[ta.getThreadIndex()].removeTa(ta);
		}catch(Exception e){
			logger.error("千层塔对象catch移除时保存:", e);
		}
	}
	
	/**
	 * 从数据库中加载塔
	 * @param playerId
	 * @return
	 */
	QianCengTa_Ta loadTa(long playerId){
		QianCengTa_Ta ta = null;
		try {
			ta = em.find(playerId);
		} catch (Exception e) {
			logger.error("从数据库中加载塔,出错：" + playerId, e);
			return null;
		}
		if (ta == null) {
			//如果数据库里没有
			ta = new QianCengTa_Ta();
			ta.setPlayerId(playerId);
			ta.init();
			em.notifyNewObject(ta);
		}
		ta.doInitBef();
		return ta;
	}
	
	public boolean isInQianCengTanGame(String gameName){
		for (int i = 0; i < allMapNames.size(); i++) {
			if(allMapNames.get(i).equals(gameName)) {
				return true;
			}
		}
		return false;
	}
	
	public void opt_open(Player player){
		try{
			QianCengTa_Ta ta =  getTa(player.getId());
			ta.setNew(false);
			player.addMessageToRightBag(ta.getInfo(0, ta.getMaxDao()));
		}catch(Exception e){
			logger.error("opt_open", e);
		}
	}
	
	/**
	 * 通知管理器，某个玩家打通的某个�
	 * 记录玩家的数据，看是否进入榜�
	 * 
	 * @param player
	 * @param daoIndex
	 * @param cengIndex
	 */
	public synchronized void notifyPlayerPassOneCeng(Player player,QianCengTa_Ta ta,int daoIndex,int cengIndex){
		QianCengTa_Player p = getPlayerInPaiHang(player.getId());
		boolean modify =false;
		if(p == null){
			p = new QianCengTa_Player();
			p.setCareer(player.getCareer());
			p.setCountry(player.getCountry());
			p.setPlayerId(player.getId());
			p.setPlayerLevel(player.getLevel());
			p.setPlayerName(player.getName());
			p.setReachMaxTime(System.currentTimeMillis());
			
			p.setMaxCengIndex(cengIndex);
			p.setMaxDaoIndex(daoIndex);
			modify = true;
			paihangList.add(p);
		}
		
		if(p.getMaxDaoIndex() < daoIndex){
			p.setMaxDaoIndex(daoIndex);
			p.setMaxCengIndex(cengIndex);
			modify = true;
		}else if(p.getMaxDaoIndex() == daoIndex){
			if(p.getMaxCengIndex() < cengIndex){
				p.setMaxCengIndex(cengIndex);
				modify = true;
			}
		}
		
		if(modify){
			int k = paihangList.indexOf(p);
			for(int i = k-1 ; i > 0 ; i--){
				QianCengTa_Player pp = paihangList.get(i);
				if(pp.getMaxDaoIndex() < p.getMaxDaoIndex() || (pp.getMaxDaoIndex() == p.getMaxDaoIndex() && pp.getMaxCengIndex() < p.getMaxCengIndex())){
					//迁移
					paihangList.set(i, p);
					paihangList.set(i+1, pp);
				}else{
					break;
				}
			}
			paihangNeedSave = true;
		}
	}

	@Override
	public String getName() {
		return "QianCengTa";
	}
	@Override
	public String[] getInterestedMessageTypes() {
		// 客户端主动请求更新界面信�刷新,自动爬塔,挑战,全部领取,放弃某个层的奖励
		return new String[]{"QIANCENGTA_QUERY_INFO_REQ","QIANCENGTA_FLUSH_REQ",
				"QIANCENGTA_AUTO_PATA_REQ","QIANCENGTA_MANUAL_PATA_REQ",
				"QIANCENGTA_GET_REWARD_REQ","QIANCENGTA_GIVEUP_REWARD_REQ", "QIANCENGTA_REWARD_INFO_REQ",
				"QIANCHENGTA_MANUAL_START_REQ", "QIANCHENGTA_MANUAL_BACKHOME_REQ", 
				
				
				
				"NEWQIANCENGTA_QUERY_INFO_REQ", "NEWQIANCENGTA_FLUSH_REQ", "NEWQIANCENGTA_AUTO_PATA_REQ", 
				"NEWQIANCENGTA_MANUAL_PATA_REQ", "NEWQIANCENGTA_GET_REWARD_REQ", "NEWQIANCENGTA_REWARD_INFO_REQ", 
				"NEWQIANCENGTA_GET_FIRST_REWARD_REQ"};
	}
	@Override
	public ResponseMessage handleReqeustMessage(Connection conn,
			RequestMessage message) throws ConnectionException, Exception {
		Player player = check(conn, message, logger);
		if(useMethodCache){
			return super.handleReqeustMessage(conn, message, player);
		}

		Class<?> clazz = this.getClass();
		Method m1 = clazz.getDeclaredMethod("handle_" + message.getTypeDescription(), Connection.class, RequestMessage.class, Player.class);
		m1.setAccessible(true);
		Object obj = m1.invoke(this, conn, message, player);
		return obj == null ? null : (ResponseMessage) obj;
	}
	@Override
	public boolean handleResponseMessage(Connection conn,
			RequestMessage request, ResponseMessage response)
			throws ConnectionException, Exception {
		return false;
	}
	
	/**
	 * 客户端请求获取某个道的信息
	 * @param conn
	 * @param request
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QIANCENGTA_QUERY_INFO_REQ(Connection conn,RequestMessage request,Player player){
		try{
			QIANCENGTA_QUERY_INFO_REQ queryInfo = (QIANCENGTA_QUERY_INFO_REQ) request;
			QianCengTa_Ta ta =  getTa(player.getId());
			RequestMessage openInfo = ta.getInfo(0, queryInfo.getDaoIndex());
			if(openInfo!=null){
				player.addMessageToRightBag(openInfo);
			}
			if (logger.isWarnEnabled()) {
				logger.warn("[handle_QIANCENGTA_QUERY_INFO_REQ] [{}] [{}] [道数:{}]", new Object[]{player.getLogString(), ta.getLogString(0), queryInfo.getDaoIndex()});
			}
		}catch(Exception e){
			logger.error("handle_QIANCENGTA_QUERY_INFO_REQ", e);
		}
		return null;
	}
	
	/**
	 * 刷新，如果成功，客户端会收到QIANCENGTA_OPEN_INFO_REQ 
	 * @param conn
	 * @param request
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QIANCENGTA_FLUSH_REQ(Connection conn,RequestMessage request,Player player){
		try{
			QIANCENGTA_FLUSH_REQ flush = (QIANCENGTA_FLUSH_REQ) request;
			QianCengTa_Ta ta =  getTa(player.getId());
			int daoIndex = flush.getDaoIndex();
			ta.msg_flush(player, 0, daoIndex);
			if (logger.isWarnEnabled()) {
				logger.warn("[handle_QIANCENGTA_FLUSH_REQ] [{}] [{}] [道数:{}]", new Object[]{player.getLogString(), ta.getLogString(0), daoIndex});
			}
		}catch(Exception e){
			logger.error("handle_QIANCENGTA_FLUSH_REQ", e);
		}
		return null;
	}
	
	/**
	 * 自动爬塔，如果成功，客户端会收到QIANCENGTA_OPEN_INFO_REQ
	 * @param conn
	 * @param request
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QIANCENGTA_AUTO_PATA_REQ(Connection conn,RequestMessage request,Player player){
		QIANCENGTA_AUTO_PATA_REQ autopata = (QIANCENGTA_AUTO_PATA_REQ) request;
		QianCengTa_Ta ta =  getTa(player.getId());
		int action = autopata.getAction();
		if (logger.isWarnEnabled()) {
			logger.warn("[handle_QIANCENGTA_AUTO_PATA_ REQ] [{}] [{}] [action:{}]", new Object[]{player.getLogString(), ta.getLogString(0), action});
		}
		if(action==1){
			ta.autoPaTa(player, 0, autopata.getDaoIndex());
		}else if(action==2){
			ta.stopAutoPaTa(player, 0);
			return null;
		}else{
			return null;
		}
		RequestMessage req = ta.getInfo(0, autopata.getDaoIndex());
		if(req!=null){
			player.addMessageToRightBag(req);
		}
		return null;
	}
	
	/**
	 * 挑战爬塔，如果成功，客户端会收到跳地图的协议
	 * @param conn
	 * @param request
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QIANCENGTA_MANUAL_PATA_REQ(Connection conn,RequestMessage request,Player player){
		
		try{
			QIANCENGTA_MANUAL_PATA_REQ manualPaTa = (QIANCENGTA_MANUAL_PATA_REQ) request;
			QianCengTa_Ta ta =  getTa(player.getId());
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [挑战爬塔] [收到客户端请求] [daoIndex="+manualPaTa.getDaoIndex()+"] [cengIndex="+manualPaTa.getCengIndex()+"] [status="+ ta.status+ player.getLogString()+"]");
			ta.manualPaTa(player, 0, manualPaTa.getDaoIndex(), manualPaTa.getCengIndex());
			if (logger.isWarnEnabled()) {
				logger.warn("[handle_QIANCENGTA_MANUAL_PATA_REQ] [{}] [{}] [dao:{}] [ceng:{}]", new Object[]{player.getLogString(), ta.getLogString(0), manualPaTa.getDaoIndex(), manualPaTa.getCengIndex()});
			}
		}catch(Exception e){
			logger.error("handle_QIANCENGTA_MANUAL_PATA_REQ:",e);
		}
		return null;
	}
	
	public ResponseMessage handle_QIANCENGTA_REWARD_INFO_REQ(Connection conn,RequestMessage request,Player player){
		QIANCENGTA_REWARD_INFO_REQ req = (QIANCENGTA_REWARD_INFO_REQ)request;
		long playerID = req.getPlayerId();
		int daoIndex = req.getDaoIndex();
		if (playerID != player.getId()) {
			player.sendError(Translate.text_qiancengta_013);
			return null;
		}
		QianCengTa_Ta ta = QianCengTaManager.getInstance().getTa(playerID);
		QianCengTa_Dao dao = ta.getDao(0, daoIndex);
		if (dao == null) {
			player.sendError(Translate.text_qiancengta_009);
			return null;
		}
		ArrayList<RewardMsg> list = dao.getRewardIds();
		QIANCENGTA_REWARD_INFO_RES res = new QIANCENGTA_REWARD_INFO_RES(req.getSequnceNum(), list.toArray(new RewardMsg[0]));
		if (logger.isWarnEnabled()) {
			logger.warn("[handle_QIANCENGTA_REWARD_INFO_REQ] [{}] [{}] [dao:{}] [Rsize:{}]", new Object[]{player.getLogString(), ta.getLogString(0),daoIndex, list.size()});
		}
		return res;
	}
	
	/**
	 * 领取奖励 -1为隐藏层 -100为全部领取，客户端会收到QIANCENGTA_OPEN_INFO_REQ
	 * @param conn
	 * @param request
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QIANCENGTA_GET_REWARD_REQ(Connection conn,RequestMessage request,Player player) {
		try{
			QIANCENGTA_GET_REWARD_REQ rewards = (QIANCENGTA_GET_REWARD_REQ) request;
			QianCengTa_Ta ta =  getTa(player.getId());
			boolean isReturn = rewards.getIsOver();
			int daoIndex = rewards.getDaoIndex();
			int cengIndex = rewards.getCengIndex();
			QianCengTa_Dao dao = ta.getDao(0, daoIndex);
			if(dao == null){
				return null;
			}
			if (cengIndex >= 0) {
				QianCengTa_Ceng ceng = dao.getCeng(cengIndex);
				if (ceng == null) {
					return null;
				}
				ceng.givePlayerRewards(player);
			}else if (cengIndex == -100){
				QianCengTa_Ceng ceng = dao.getHiddenCeng();
				if (ceng != null && ceng.rewardStatus == QianCengTa_Ceng.STATUS_奖励未领取){
					ceng.givePlayerRewards(player);
					if ((ceng.rewardStatus == QianCengTa_Ceng.STATUS_奖励已领取 || ceng.rewardStatus == QianCengTa_Ceng.STATUS_无奖励) && ceng.getRewards().size() <= 0) {
						dao.setOpenHidden(false);
						dao.setHiddenCeng(null);
					}
				}
				for (int i = 0; i < dao.getCengList().size(); i++) {
					if (player.getKnapsack_common().isFull() && player.getKnapsacks_QiLing().isFull()) {
						break;
					}
					dao.getCeng(i).givePlayerRewards(player);
				}
			}else if (cengIndex == -1) {
				//隐藏层
				QianCengTa_Ceng ceng = dao.getHiddenCeng();
				if (ceng != null && ceng.rewardStatus == QianCengTa_Ceng.STATUS_奖励未领取){
					ceng.givePlayerRewards(player);
					if ((ceng.rewardStatus == QianCengTa_Ceng.STATUS_奖励已领取 || ceng.rewardStatus == QianCengTa_Ceng.STATUS_无奖励) && ceng.getRewards().size() <= 0) {
						dao.setOpenHidden(false);
						dao.setHiddenCeng(null);
					}
				}
			}
			if (logger.isWarnEnabled()) {
				logger.warn("[handle_QIANCENGTA_GET_REWARD_REQ] [{}] [{}] [dao:{}] [ceng:{}]", new Object[]{player.getLogString(), ta.getLogString(0),daoIndex, cengIndex});
			}
			ta.notifyChanager("daoList");
			if (isReturn) {
				return null;
			}else{
				QIANCENGTA_REWARD_INFO_RES res = new QIANCENGTA_REWARD_INFO_RES(GameMessageFactory.nextSequnceNum(), dao.getRewardIds().toArray(new RewardMsg[0]));
				player.addMessageToRightBag(res);
			}
		}catch(Exception e){
			logger.error("handle_QIANCENGTA_GET_REWARD_REQ出错", e);
		}
		return null;
	}
	
	public static int LAST_TIME =  5 * 1000;
	//开始刷怪
	public ResponseMessage handle_QIANCHENGTA_MANUAL_START_REQ(Connection conn,RequestMessage request,Player player){
		QianCengTa_Ta ta =  getTa(player.getId());
		long time = ta.startRefMonster();
		if (logger.isWarnEnabled()) {
			logger.warn("[handle_QIANCHENGTA_MANUAL_START_REQ] [{}] [{}] [time:{}]", new Object[]{player.getLogString(), ta.getLogString(0), time});
		}
		return new QIANCHENGTA_MANUAL_START_RES(request.getSequnceNum(), LAST_TIME, time);
	}
	
	//回城
	public ResponseMessage handle_QIANCHENGTA_MANUAL_BACKHOME_REQ(Connection conn,RequestMessage request,Player player){
		QianCengTa_Ta ta =  getTa(player.getId());
		ta.backPlayerToFormGame();
		if(logger.isWarnEnabled()){
			logger.warn("handle_QIANCHENGTA_MANUAL_BACKHOME_REQ [{}] [{}]", new String[]{player.getLogString(), ta.getLogString(0)});
		}
		return null;
	}
	
	public void setFile(String file) {
		this.file = file;
	}
	public String getFile() {
		return file;
	}
	
	public boolean isInQianCengTa(Player player) {
		if (player.getCurrentGame() != null) {
			for (int i = 0; i < QianCengTa_TaInfo.QianCengMapNames.length; i++) {
				if (QianCengTa_TaInfo.QianCengMapNames[i].equals(player.getCurrentGame().gi.getName())) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	/**
	 * 
	 * 新的千层塔协议
	 * 
	 */
	/**
	 * 客户端请求获取某个道的信息
	 * @param conn
	 * @param request
	 * @param player
	 * @return 
	 */
	public ResponseMessage handle_NEWQIANCENGTA_QUERY_INFO_REQ(Connection conn,RequestMessage request,Player player){
		try{
			NEWQIANCENGTA_QUERY_INFO_REQ queryInfo = (NEWQIANCENGTA_QUERY_INFO_REQ) request;
			QianCengTa_Ta ta =  getTa(player.getId());
			int nanduType = queryInfo.getNanduType();
			ta.setNew(true);
			RequestMessage openInfo = ta.getInfo(nanduType, queryInfo.getDaoIndex());
			if(openInfo!=null){
				player.addMessageToRightBag(openInfo);
			}
			if (logger.isWarnEnabled()) {
				logger.warn("[handle_NEWQIANCENGTA_QUERY_INFO_REQ] [{}] [{}] [难度:{}] [道数:{}]", new Object[]{player.getLogString(), ta.getLogString(nanduType), nanduType, queryInfo.getDaoIndex()});
			}
		}catch(Exception e){
			logger.error("handle_NEWQIANCENGTA_QUERY_INFO_REQ", e);
		}
		return null;
	}
	
	/**
	 * 刷新，如果成功，客户端会收到NEWQIANCENGTA_OPEN_INFO_REQ 
	 * @param conn
	 * @param request
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_NEWQIANCENGTA_FLUSH_REQ(Connection conn,RequestMessage request,Player player){
		try{
			NEWQIANCENGTA_FLUSH_REQ flush = (NEWQIANCENGTA_FLUSH_REQ) request;
			QianCengTa_Ta ta =  getTa(player.getId());
			int nandu = flush.getNanduType();
			int daoIndex = flush.getDaoIndex();
			ta.msg_flush(player, nandu, daoIndex);
			if (logger.isWarnEnabled()) {
				logger.warn("[handle_NEWQIANCENGTA_FLUSH_REQ] [{}] [{}] [难度:{}] [道数:{}]", new Object[]{player.getLogString(), ta.getLogString(nandu), nandu, daoIndex});
			}
		}catch(Exception e){
			logger.error("handle_QIANCENGTA_FLUSH_REQ", e);
		}
		return null;
	}
	
	public void opt_Newopen(Player player){
		try{
			QianCengTa_Ta ta =  getTa(player.getId());
			ta.setNew(true);
			player.addMessageToRightBag(ta.getInfo(0, ta.getMaxDao()));
		}catch(Exception e){
			logger.error("opt_open", e);
		}
	}
	
	/**
	 * 自动爬塔，如果成功，客户端会收到NEWQIANCENGTA_OPEN_INFO_REQ
	 * @param conn
	 * @param request
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_NEWQIANCENGTA_AUTO_PATA_REQ(Connection conn,RequestMessage request,Player player){
		NEWQIANCENGTA_AUTO_PATA_REQ autopata = (NEWQIANCENGTA_AUTO_PATA_REQ) request;
		QianCengTa_Ta ta =  getTa(player.getId());
		int nandu = autopata.getNanduType();
		int action = autopata.getAction();
		if (logger.isWarnEnabled()) {
			logger.warn("[handle_NEWQIANCENGTA_AUTO_PATA_ REQ] [{}] [{}] [难度:{}] [action:{}]", new Object[]{player.getLogString(), ta.getLogString(0), nandu, action});
		}
		if(action==1){
			ta.autoPaTa(player, nandu, autopata.getDaoIndex());
		}else if(action==2){
			ta.stopAutoPaTa(player, nandu);
			return null;
		}else{
			return null;
		}
		RequestMessage req = ta.getInfo(nandu, autopata.getDaoIndex());
		if(req!=null){
			player.addMessageToRightBag(req);
		}
		return null;
	}
	
	/**
	 * 挑战爬塔，如果成功，客户端会收到跳地图的协议
	 * @param conn
	 * @param request
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_NEWQIANCENGTA_MANUAL_PATA_REQ(Connection conn,RequestMessage request,Player player){
		
		try{
			NEWQIANCENGTA_MANUAL_PATA_REQ manualPaTa = (NEWQIANCENGTA_MANUAL_PATA_REQ) request;
			QianCengTa_Ta ta =  getTa(player.getId());
			int nandu = manualPaTa.getNanduType();
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [挑战爬塔] [收到客户端请求] [难度:"+nandu+"] [daoIndex="+manualPaTa.getDaoIndex()+"] [cengIndex="+manualPaTa.getCengIndex()+"] [status="+ ta.status+ player.getLogString()+"]");
			ta.manualPaTa(player, nandu, manualPaTa.getDaoIndex(), manualPaTa.getCengIndex());
			if (logger.isWarnEnabled()) {
				logger.warn("[handle_NEWQIANCENGTA_MANUAL_PATA_REQ] [{}] [{}] [{}] [dao:{}] [ceng:{}]", new Object[]{player.getLogString(), ta.getLogString(0), nandu, manualPaTa.getDaoIndex(), manualPaTa.getCengIndex()});
			}
		}catch(Exception e){
			logger.error("handle_NEWQIANCENGTA_MANUAL_PATA_REQ:",e);
		}
		return null;
	}
	
	public ResponseMessage handle_NEWQIANCENGTA_REWARD_INFO_REQ(Connection conn,RequestMessage request,Player player){
		NEWQIANCENGTA_REWARD_INFO_REQ req = (NEWQIANCENGTA_REWARD_INFO_REQ)request;
		long playerID = req.getPlayerId();
		int daoIndex = req.getDaoIndex();
		int nandu = req.getNanduType();
		if (playerID != player.getId()) {
			player.sendError(Translate.text_qiancengta_013);
			return null;
		}
		QianCengTa_Ta ta = QianCengTaManager.getInstance().getTa(playerID);
		QianCengTa_Dao dao = ta.getDao(nandu, daoIndex);
		if (dao == null) {
			player.sendError(Translate.text_qiancengta_009);
			return null;
		}
		ArrayList<RewardMsg> list = dao.getRewardIds();
		NEWQIANCENGTA_REWARD_INFO_RES res = new NEWQIANCENGTA_REWARD_INFO_RES(req.getSequnceNum(), nandu, list.toArray(new RewardMsg[0]));
		if (logger.isWarnEnabled()) {
			logger.warn("[handle_QIANCENGTA_REWARD_INFO_REQ] [{}] [{}] [nandu:{}] [dao:{}] [Rsize:{}]", new Object[]{player.getLogString(), ta.getLogString(0), nandu, daoIndex, list.size()});
		}
		return res;
	}
	
	/**
	 * 领取奖励 -1为隐藏层 -100为全部领取，客户端会收到QIANCENGTA_OPEN_INFO_REQ
	 * @param conn
	 * @param request
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_NEWQIANCENGTA_GET_REWARD_REQ(Connection conn,RequestMessage request,Player player) {
		try{
			NEWQIANCENGTA_GET_REWARD_REQ rewards = (NEWQIANCENGTA_GET_REWARD_REQ) request;
			QianCengTa_Ta ta =  getTa(player.getId());
			int nandu = rewards.getNanduType();
			boolean isReturn = rewards.getIsOver();
			int daoIndex = rewards.getDaoIndex();
			int cengIndex = rewards.getCengIndex();
			QianCengTa_Dao dao = ta.getDao(nandu, daoIndex);
			if(dao == null){
				return null;
			}
			if (cengIndex >= 0) {
				QianCengTa_Ceng ceng = dao.getCeng(cengIndex);
				if (ceng == null) {
					return null;
				}
				ceng.givePlayerRewards(player);
			}else if (cengIndex == -100){
				QianCengTa_Ceng ceng = dao.getHiddenCeng();
				if (ceng != null && ceng.rewardStatus == QianCengTa_Ceng.STATUS_奖励未领取){
					ceng.givePlayerRewards(player);
					if ((ceng.rewardStatus == QianCengTa_Ceng.STATUS_奖励已领取 || ceng.rewardStatus == QianCengTa_Ceng.STATUS_无奖励) && ceng.getRewards().size() <= 0) {
						dao.setOpenHidden(false);
						dao.setHiddenCeng(null);
					}
				}
				for (int i = 0; i < dao.getCengList().size(); i++) {
					if (player.getKnapsack_common().isFull() && player.getKnapsacks_QiLing().isFull()) {
						break;
					}
					dao.getCeng(i).givePlayerRewards(player);
				}
			}else if (cengIndex == -1) {
				//隐藏层
				QianCengTa_Ceng ceng = dao.getHiddenCeng();
				if (ceng != null && ceng.rewardStatus == QianCengTa_Ceng.STATUS_奖励未领取){
					ceng.givePlayerRewards(player);
					if ((ceng.rewardStatus == QianCengTa_Ceng.STATUS_奖励已领取 || ceng.rewardStatus == QianCengTa_Ceng.STATUS_无奖励) && ceng.getRewards().size() <= 0) {
						dao.setOpenHidden(false);
						dao.setHiddenCeng(null);
					}
				}
			}
			if (logger.isWarnEnabled()) {
				logger.warn("[handle_QIANCENGTA_GET_REWARD_REQ] [{}] [{}] [dao:{}] [ceng:{}]", new Object[]{player.getLogString(), ta.getLogString(0),daoIndex, cengIndex});
			}
			if (nandu == 0) {
				ta.notifyChanager("daoList");
			}else if (nandu == 1) {
				ta.notifyChanager("hardDaoList");
			}else if (nandu == 2) {
				ta.notifyChanager("gulfDaoList");
			}
			if (isReturn) {
				return null;
			}else{
				NEWQIANCENGTA_REWARD_INFO_RES res = new NEWQIANCENGTA_REWARD_INFO_RES(GameMessageFactory.nextSequnceNum(), nandu, dao.getRewardIds().toArray(new RewardMsg[0]));
				player.addMessageToRightBag(res);
			}
		}catch(Exception e){
			logger.error("handle_QIANCENGTA_GET_REWARD_REQ出错", e);
		}
		return null;
	}
	
	public ResponseMessage handle_NEWQIANCENGTA_GET_FIRST_REWARD_REQ(Connection conn,RequestMessage request,Player player) {
		NEWQIANCENGTA_GET_FIRST_REWARD_REQ req = (NEWQIANCENGTA_GET_FIRST_REWARD_REQ)request;
		QianCengTa_Ta ta =  getTa(player.getId());
		int nandu = req.getNanduType();
		int daoIndex = req.getDaoIndex();
		String re = ta.getFirstRewards(player, nandu, daoIndex);
		NEWQIANCENGTA_GET_FIRST_REWARD_RES res = null;
		if (re.length() > 0) {
			res = new NEWQIANCENGTA_GET_FIRST_REWARD_RES(req.getSequnceNum(), 1, re);
		}else {
			res = new NEWQIANCENGTA_GET_FIRST_REWARD_RES(req.getSequnceNum(), 0, re);
		}
		logger.warn("[NEWQIANCENGTA_GET_FIRST_REWARD_REQ] [{}] [{}] [{}]", new Object[]{ta.getLogString(nandu), player.getLogString(), re});
		return res;
	}
	public void setActivityFile(String activityFile) {
		this.activityFile = activityFile;
	}
	public String getActivityFile() {
		return activityFile;
	}
	
}
