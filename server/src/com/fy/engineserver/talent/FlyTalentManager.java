package com.fy.engineserver.talent;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.core.ParticleData;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.InlayArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.BaoShiXiaZiData;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.buff.BuffTemplate_JiaXue;
import com.fy.engineserver.datasource.buff.Buff_JiaXue;
import com.fy.engineserver.datasource.buff.Buff_XueShangXian;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gateway.GameNetworkFramework;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_Reset_Talent_Skills;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_FLY_TALENT_ADD_POINTS_REQ;
import com.fy.engineserver.message.CONFIRM_FLY_TALENT_ADD_POINTS_RES;
import com.fy.engineserver.message.CONFIRM_TALENT_EXP_REQ;
import com.fy.engineserver.message.CONFIRM_TALENT_EXP_RES;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.FLY_TALENT_ADD_POINTS_REQ;
import com.fy.engineserver.message.FLY_TALENT_ADD_POINTS_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.NOTICE_CLIENT_PLAY_PARTICLE_RES;
import com.fy.engineserver.message.NOTICE_TALENT_BUTTON_RES;
import com.fy.engineserver.message.QUERY_TALENT_EXP_REQ;
import com.fy.engineserver.message.QUERY_TALENT_EXP_RES;
import com.fy.engineserver.message.QUERY_TALENT_SKILLS_REQ;
import com.fy.engineserver.message.QUERY_TALENT_SKILLS_RES;
import com.fy.engineserver.message.TALENT_FIRST_PAGE_REQ;
import com.fy.engineserver.message.TALENT_FIRST_PAGE_RES;
import com.fy.engineserver.message.XIULIAN_TALENT_EXP_RES;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

/**
 * 飞升天赋
 * 
 *
 */
public class FlyTalentManager extends SubSystemAdapter{

	private static FlyTalentManager self;
	
	public static Logger logger = LoggerFactory.getLogger(FlyTalentManager.class);
	
	public static SimpleEntityManager<TalentData> em;
	
	private Random random = new Random();
	
	public Map<Long, TalentData> datas = new Hashtable<Long, TalentData>();
	
	public List<TalentPageInfo> infos = new ArrayList<TalentPageInfo>();
	public Map<Integer, TalentSkillInfo> skillInfos = new HashMap<Integer, TalentSkillInfo>();
	public Map<Long, Long> xiuLianExps = new HashMap<Long, Long>();
	
	public Map<String, Map<Integer, TalentExpProp>> expProps = new HashMap<String, Map<Integer,TalentExpProp>>();
	
	public static int GET_ONE_POINT_NEED_LEVEL = 15;
	public static int GET_MAX_POINT = 20;
	public static int MAX_XIANYIN_LEVEL = 300;
	public static long TALENT_SKILL_CD_TIME = 20*60*1000;
	public static long TALENT_SKILL_CD_LAST_TIME = 30*1000;
	
	private String filePath;
	
	public long ONE_DAY_MILLISECOND = 24*60*60*1000L;
	
	private final int 基础属性配置 = 0;
	private final int 基础技能配置 = 1;
	private final int 修炼兑换经验配置 = 2;
	private final int 道具兑换经验配置 = 3;
	private final int 帮助信息 = 4;
	
	public static final int 体质 = 0;
	public static final int 幸运 = 1;
	public static final int 神赐 = 2;
	public static final int 强化神元 = 3;
	public static final int 闪避 = 4;
	public static final int 强化神赐 = 5;
	public static final int 灵巧 = 6;
	public static final int 领悟 = 7;
	public static final int 仙疗 = 8;
	public static final int 温和 = 9;
	public static final int 宝石神悟 = 10;
	public static final int 坚固 = 11;
	public static final int 仙婴体质 = 12;
	public static final int 仙婴固化 = 13;
	
	public static final int 猛兽 = 14;
	public static final int 怒火 = 15;
	public static final int 神力 = 16;
	public static final int 强化猛兽 = 17;
	public static final int 碎甲 = 18;
	public static final int 强化神力 = 19;
	public static final int 专注 = 20;
	public static final int 仙悟 = 21;
	public static final int 暴燃 = 22;
	public static final int 会心 = 23;
	public static final int 羽翼神悟 = 24;
	public static final int 破斧 = 25;
	public static final int 仙婴兽化 = 26;
	public static final int 仙婴悟攻 = 27;
	
	public static final int[] TALENT_SKILL = {体质,幸运,神赐,强化神元,闪避,强化神赐,灵巧,领悟,仙疗,温和,宝石神悟,坚固,仙婴体质,仙婴固化,猛兽,怒火,神力,强化猛兽,碎甲,强化神力,专注,仙悟,暴燃,会心,羽翼神悟,破斧,仙婴兽化,仙婴悟攻};
	public static final int[] TALENT_SKILL_A = {体质,幸运,神赐,强化神元,闪避,强化神赐,灵巧,领悟,仙疗,温和,宝石神悟,坚固,仙婴体质,仙婴固化};
	public static final int[] TALENT_SKILL_B = {猛兽,怒火,神力,强化猛兽,碎甲,强化神力,专注,仙悟,暴燃,会心,羽翼神悟,破斧,仙婴兽化,仙婴悟攻};
	public static final int[] 第一层天赋技能 = {体质,幸运,神赐,猛兽,怒火,神力};
	
	public static String 首界面帮助信息 = "";
	public static String 天赋界面帮助信息 = "";
	
	public int 任务经验和仙婴经验兑换比例 = 20;
	
	public static FlyTalentManager getInstance(){
		if(self == null){
			self = new FlyTalentManager();
		}
		return self;
	}
	
	public void init() throws Exception{
		
		long start = System.currentTimeMillis();
		self = this;
		GameNetworkFramework.getInstance().addSubSystem(this);
		em = SimpleEntityManagerFactory.getSimpleEntityManager(TalentData.class); 
		initFile();
		ServiceStartRecord.startLog(this);
	}
	
	public void destroy() {
		em.destroy();
		logger.warn("[[飞升天赋管理] [destroy]");
	}
	
	public void initFile() throws Exception{
		File file = new File(filePath);
		if(!file.exists() || !file.isFile()){
			throw new Exception("文件不存在:"+filePath);
		}
		InputStream is = new FileInputStream(file);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		
		HSSFSheet sheet = workbook.getSheetAt(基础属性配置);		
		try{
			int rows = sheet.getPhysicalNumberOfRows();
			for(int i = 1;i < rows;i++){
				int index = 0;
				HSSFRow row = sheet.getRow(i);
				if(row != null){
					int level = StringTool.getCellValue(row.getCell(index++), Integer.class); 
					int phyAttact = StringTool.getCellValue(row.getCell(index++), Integer.class); 
					int magicAttact = StringTool.getCellValue(row.getCell(index++), Integer.class); 
					int phyDefence = StringTool.getCellValue(row.getCell(index++), Integer.class); 
					int magicDefence = StringTool.getCellValue(row.getCell(index++), Integer.class); 
					int hp = StringTool.getCellValue(row.getCell(index++), Integer.class); 
					long exp = StringTool.getCellValue(row.getCell(index++), Long.class); 
					TalentPageInfo info = new TalentPageInfo();
					info.setLevel(level);
					info.setPhyAttact(phyAttact);
					info.setMagicAttact(magicAttact);
					info.setPhyDefence(phyDefence);
					info.setMagicDefence(magicDefence);
					info.setHp(hp);
					info.setUpgradeExp(exp);
					infos.add(info);
				}
			}
			logger.warn("[基础属性配置] [加载完成] [rows:"+rows+"] [数量:"+infos.size()+"]");
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("[加载基础属性配置] [异常]"+e);
		}
		
		try{
			sheet = workbook.getSheetAt(基础技能配置);		
			int rows = sheet.getPhysicalNumberOfRows();
			for(int i = 1;i < rows;i++){
				int index = 0;
				HSSFRow row = sheet.getRow(i);
				if(row != null){
					int id = StringTool.getCellValue(row.getCell(index++), Integer.class); 
					String name = StringTool.getCellValue(row.getCell(index++), String.class); 
					String icon = StringTool.getCellValue(row.getCell(index++), String.class); 
					int maxLevel = StringTool.getCellValue(row.getCell(index++), Integer.class); 
					String valueStr = StringTool.getCellValue(row.getCell(index++), String.class); 
					double [] levelValues = stringToDouble(valueStr.split(","));
					int propType = StringTool.getCellValue(row.getCell(index++), Integer.class); 
					String mess = StringTool.getCellValue(row.getCell(index++), String.class); 
					String mess1 = StringTool.getCellValue(row.getCell(index++), String.class); 
					String mess2 = StringTool.getCellValue(row.getCell(index++), String.class); 
					String mess3 = StringTool.getCellValue(row.getCell(index++), String.class); 
					int relyId = StringTool.getCellValue(row.getCell(index++), Integer.class); 
					String typeStr = StringTool.getCellValue(row.getCell(index++), String.class); 
					int layer = StringTool.getCellValue(row.getCell(index++), Integer.class); 
					String skillLimitMess = StringTool.getCellValue(row.getCell(index++), String.class); 
					int talentType = 0;
					if(typeStr != null && typeStr.equals("B")){
						talentType = 1;
					}
					TalentSkillInfo info = new TalentSkillInfo();
					info.setId(id);
					info.setName(name);
					info.setMaxLevel(maxLevel);
					info.setIcon(icon);
					info.setMess(mess);
					info.setMess2(mess2);
					info.setMess3(mess3);
					info.setMess1(mess1);
					info.setRelyId(relyId);
					info.setLevelValues(levelValues);
					info.setPropType(propType);
					info.setTalentType(talentType);
					info.setLayer(layer);
					info.setSkillLimitMess(skillLimitMess);
					skillInfos.put(id, info);
				}
			}
			logger.warn("[基础技能配置] [加载完成] [rows:"+rows+"] [数量:"+skillInfos.size()+"]");
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("[加载基础技能配置] [异常]"+e);
		}
		
		try{
			sheet = workbook.getSheetAt(修炼兑换经验配置);		
			int rows = sheet.getPhysicalNumberOfRows();
			for(int i = 1;i < rows;i++){
				int index = 0;
				HSSFRow row = sheet.getRow(i);
				if(row != null){
					long level = StringTool.getCellValue(row.getCell(index++), Long.class); 
					long exp = StringTool.getCellValue(row.getCell(index++), Long.class); 
					xiuLianExps.put(level, exp);
				}
			}
			logger.warn("[修炼兑换经验配置] [加载完成] [rows:"+rows+"] [数量:"+xiuLianExps.size()+"]");
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("[加载修炼兑换经验配置] [异常]"+e);
		}
		
		try{
			sheet = workbook.getSheetAt(道具兑换经验配置);		
			int rows = sheet.getPhysicalNumberOfRows();
			String propName1 = "";
			String propName2 = "";
			String propName3 = "";
			for(int i = 0;i < rows;i++){
				int index = 0;
				HSSFRow row = sheet.getRow(i);
				if(row != null){
					if(i == 0){
						StringTool.getCellValue(row.getCell(index++), String.class); 
						String 初级仙婴元魄 = StringTool.getCellValue(row.getCell(index++), String.class); 
						String 中级仙婴元魄 = StringTool.getCellValue(row.getCell(index++), String.class); 
						String 高级仙婴元魄 = StringTool.getCellValue(row.getCell(index++), String.class); 
						propName1 = 初级仙婴元魄;
						propName2 = 中级仙婴元魄;
						propName3 = 高级仙婴元魄;
						expProps.put(初级仙婴元魄, new HashMap<Integer, TalentExpProp>());
						expProps.put(中级仙婴元魄, new HashMap<Integer, TalentExpProp>());
						expProps.put(高级仙婴元魄, new HashMap<Integer, TalentExpProp>());
					}else{
						if(propName1.isEmpty() || propName2.isEmpty() || propName3.isEmpty()){
							throw new Exception("道具兑换经验配置加载失败，请检查"+propName1+"--"+propName2+"--"+propName3);
						}
						Integer level = StringTool.getCellValue(row.getCell(index++), Integer.class); 
						long exp1 = StringTool.getCellValue(row.getCell(index++), Long.class); 
						long exp2 = StringTool.getCellValue(row.getCell(index++), Long.class); 
						long exp3 = StringTool.getCellValue(row.getCell(index++), Long.class); 
						
						TalentExpProp expProp = new TalentExpProp();
						expProp.setLevel(level);
						expProp.setExp(exp1);
						expProp.setName(propName1);
						expProps.get(propName1).put(level, expProp);
						
						TalentExpProp expProp2 = new TalentExpProp();
						expProp2.setLevel(level);
						expProp2.setExp(exp2);
						expProp2.setName(propName2);
						expProps.get(propName2).put(level, expProp2);
						
						TalentExpProp expProp3 = new TalentExpProp();
						expProp3.setLevel(level);
						expProp3.setExp(exp3);
						expProp3.setName(propName3);
						expProps.get(propName3).put(level, expProp3);
					}
				}
			}
			logger.warn("[道具兑换经验配置] [加载完成] [rows:"+rows+"] [数量:"+expProps.size()+"]");
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("[加载道具兑换经验配置] [异常]"+e);
		}
		
		try{
			sheet = workbook.getSheetAt(帮助信息);		
			int rows = sheet.getPhysicalNumberOfRows();
			for(int i = 0;i < rows;i++){
				HSSFRow row = sheet.getRow(i);
				if(row != null && i == 0){
					首界面帮助信息 = StringTool.getCellValue(row.getCell(0), String.class); 
				}
				if(row != null && i == 1){
					天赋界面帮助信息 = StringTool.getCellValue(row.getCell(0), String.class); 
				}
			}
			logger.warn("[帮助信息] [加载完成] [rows:"+rows+"] [数量:"+expProps.size()+"]");
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("[加载帮助信息] [异常]"+e);
		}
		
	}
	
	public TalentData getTalentData(long playerId){
		long start = System.currentTimeMillis();
		if(datas.get(new Long(playerId)) != null){
			return datas.get(new Long(playerId));
		}
		
		try {
			TalentData data = em.find(playerId);
			if(data != null){
				datas.put(playerId,data);
				logger.warn("[加载飞升天赋] [成功] [playerId:{}] [datas:{}] [{}] [耗时:{}ms]",new Object[]{playerId,datas.size(), (data==null?"nul":data),(System.currentTimeMillis()-start)});
			}
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[加载飞升天赋] [异常] [playerId:{}] [datas:{}] [耗时:{}ms] [{}]",new Object[]{playerId,datas.size(),(System.currentTimeMillis()-start),e});
		}
		return null;
	}
	
	public void saveTalentData(Player player ,TalentData data){
		long start = System.currentTimeMillis();
		try{
			boolean isNewData = false;
			if(data.getId() <= 0){
				isNewData= true;
				data.setId(player.getId());
				em.flush(data);
				datas.put(player.getId(),data);
			}else{
				datas.put(player.getId(),data);
				em.flush(data);
			}
			logger.warn("[保存飞升天赋数据] [成功] [{}] [datas:{}] [{}] [{}] [耗时:{}ms]",new Object[]{isNewData?"创建数据":"更新数据",datas.size(),data,player.getLogString(),(System.currentTimeMillis()-start)});
		}catch(Exception e){
			e.printStackTrace();
			logger.warn("[保存飞升天赋数据] [异常] [datas:{}] [{}] [{}] [耗时:{}ms] [{}]",new Object[]{datas.size(),(data!=null?data:"nul"),(player!=null?player.getLogString():"nul"),(System.currentTimeMillis()-start),e});
		}
	}
	
	public int getOwnPoints(Player player){
		TalentData data = getTalentData(player.getId());
		if(data == null){
			data = new TalentData();
		}
		return data.getXylevel() / GET_ONE_POINT_NEED_LEVEL;
	}
	
	public void calTalentPoint(){
		
	}
	
	public CONFIRM_FLY_TALENT_ADD_POINTS_RES handle_CONFIRM_FLY_TALENT_ADD_POINTS_REQ(RequestMessage message, Player player){
		CONFIRM_FLY_TALENT_ADD_POINTS_REQ req = (CONFIRM_FLY_TALENT_ADD_POINTS_REQ)message;
		int handleType = req.getHandleType();	//0:重置   ,1:确认 ,2:取消
		if(player.flyState == 1){
			player.sendError(Translate.仙婴附体中不能操作);
			return null;
		}
		TalentData data = getTalentData(player.getId());
		if(data == null){
			data = new TalentData();
		}
		if(handleType == 0){
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(200);
			Option_Cancel opt1 = new Option_Cancel();
			Option_Reset_Talent_Skills opt2 = new Option_Reset_Talent_Skills();
			opt2.setText(Translate.确定);
			opt1.setText(Translate.取消);
			mw.setOptions(new Option[]{opt2,opt1});
			mw.setDescriptionInUUB(Translate.消耗仙婴天赋重置丹);
			CONFIRM_WINDOW_REQ req2 = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
			player.addMessageToRightBag(req2);
			return null;
		}else if(handleType == 1){
			Map<Integer, Integer> taneltSkillTempAddPoints = player.taneltSkillTempAddPoints;
			int allPoints = 0;
			Iterator<Map.Entry<Integer,Integer>> it = taneltSkillTempAddPoints.entrySet().iterator();
			HashMap<Integer, Integer> points = new HashMap<Integer, Integer>();
			points.putAll(data.getPoints());
			int 领悟加点 = 0;
			while(it.hasNext()){
				Entry<Integer, Integer> entry = it.next();
				if(entry != null){
					int skillId = entry.getKey();
					int level = entry.getValue();
					if(level > 0){
						allPoints+=level;
						Integer value = data.getPoints().get(new Integer(skillId));
						if(value == null){
							data.getPoints().put(skillId, 0);
							value = data.getPoints().get(new Integer(skillId));
						}
						points.put(skillId, value+level);
						if(skillId == 领悟){
							领悟加点  = value+level;
						}
					}
				}
			}
			
			if(领悟加点 == 1){
				data.setXylevelA(10);
			}else if(领悟加点 == 2){
				data.setXylevelA(20);
			}else if(领悟加点 == 3){
				data.setXylevelA(30);
			}
			
			if(allPoints <= 0){
				player.sendError(Translate.您还没有分配加点);
				return null;
			}
			
			data.setPoints(points);
			taneltSkillTempAddPoints.clear();
			saveTalentData(player, data);
			player.sendError(Translate.确认加点);
			try {
				int allNum = getAllPoints(player);
				AchievementManager.getInstance().record(player, RecordAction.元婴天赋总数,allNum);
			} catch (Exception e) {
				PlayerAimManager.logger.warn("[仙录] [统计天赋加点次数] [异常] [" + player.getLogString() + "]", e);
			}
			logger.warn("[确认加点] [成功] [加点点数:{}] [加点分布:{}] [库中加点分布:{}] [{}]",new Object[]{allPoints,player.taneltSkillTempAddPoints,points, player.getLogString()});
			return  new CONFIRM_FLY_TALENT_ADD_POINTS_RES(req.getSequnceNum(), 1);
		}else if(handleType == 2){
			player.taneltSkillTempAddPoints.clear();
			//player.sendError(Translate.取消加点);
		}
		return null;
	}
	
	public int getAllPoints(Player player){
		int allPoints = 0;
		TalentData data = getTalentData(player.getId());
		if(data != null){
			Iterator<Integer> it = data.getPoints().values().iterator();
			while(it.hasNext()){
				Integer p = it.next();
				if(p != null){
					allPoints += p;
				}
			}
		}
		return allPoints;
	}
	
	public void endTalentSkillEffect(Player player){
		if(player.flyState == 1){
			TalentData data = getTalentData(player.getId());
			
			if(data != null){
				int realLevel = data.getXylevel()+data.getXylevelA();
				if(realLevel > MAX_XIANYIN_LEVEL){
					realLevel = MAX_XIANYIN_LEVEL;
				}
				if(data.getNbEndTimes() < System.currentTimeMillis() || data.getCdEndTime() < System.currentTimeMillis()){
					
					player.flyState = 0;
					data.setNbEndTimes(0);
					calTalentSkillEffect(player, 2);
					
					
					boolean 仙悟开启 = false;
					if(data.getPoints() != null){
						Integer point = data.getPoints().get(new Integer(仙悟));
						if(point != null){
							仙悟开启 = true;
							if(point == 1){
								calBasicSkillEffect(player, realLevel, 2,1.02);
							}else if(point == 2){
								calBasicSkillEffect(player, realLevel, 2,1.04);
							}else if(point == 3){
								calBasicSkillEffect(player, realLevel, 2,1.06);
							}
						}
					}
					
					if(!仙悟开启){
						calBasicSkillEffect(player,realLevel,2,1.0);
					}
					
					player.setObjectScale((short)1000);
					logger.warn("[结束附体效果] [成功] [正常cd结束:{}] [非正常:{}] [仙悟开启:{}] [{}]",new Object[]{(data.getNbEndTimes() < System.currentTimeMillis()),(data.getCdEndTime() < System.currentTimeMillis()),仙悟开启,player.getLogString()});
				}
			}else{
				player.flyState = 0;
				calTalentSkillEffect(player, 2);
				calBasicSkillEffect(player,player.getLevel(),2,1.0);
				player.setObjectScale((short)1000);
				logger.warn("[结束附体效果] [异常] [强制结束] [{}]",new Object[]{player.getLogString()});
			}
		}
	}
	
	//人物进行攻击时，有几率额外造成20%伤害
	public int handle_仙婴悟攻(Player player,int damage){
		int rNum = random.nextInt(100);
		int newDamage = damage;
		int addPoint = -1;
		if(player != null && player.flyState == 1){
			if(rNum <= 40){
				TalentData data = getTalentData(player.getId());
				if(data != null && data.getPoints() != null){
					Integer level = data.getPoints().get(new Integer(仙婴悟攻));
					if(level != null){
						addPoint = level;
						if(level == 1){
							newDamage = (int)(damage*1.2);
						}
						logger.info("[仙婴悟攻] [造成伤害提升] [rNum:"+rNum+"] [技能等级:"+addPoint+"] [伤害:"+damage+"-->"+newDamage+"] [玩家:"+player.getName()+"]");
					}
				}
			}
		}
		return newDamage;
	}
	
	//增加1%/5%/10%控制类技能持续时间
	public int handle_专注(Player player,Player causte,long lastTimes){
		int times = 0;
		if(causte.flyState == 1){
			TalentData data = getTalentData(causte.getId());
			if(data != null && data.getPoints() != null){
				Integer level = data.getPoints().get(new Integer(专注));
				if(level != null){
					if(level == 1){
						times = (int)(lastTimes * 0.01);
					}else if(level == 2){
						times = (int)(lastTimes * 0.05);
					}else if(level == 3){
						times = (int)(lastTimes * 0.1);
					}
					logger.info("[专注] [控制类技能持续时间] [times:"+times+"] [lastTimes:"+lastTimes+"] [state:"+causte.flyState+"] [控制技能千分比:"+player.decreaseConTimeRate+"] [攻击玩家:"+causte.getName()+"] [被打玩家:"+player.getName()+"]");
				}
			}
		}
		return times;
	}
	
	//额外获得15%减伤效果
	public int handle_仙婴固化(Player player,int damage){
		int newDamage = damage;
		int addPoint = -1;
		if(player != null && player.flyState == 1){
			TalentData data = getTalentData(player.getId());
			if(data != null && data.getPoints() != null){
				Integer level = data.getPoints().get(new Integer(仙婴固化));
				if(level != null){
					addPoint = level;
					if(level > 0){
						newDamage = (int)(damage * 0.85);
						logger.info("[仙婴固化] [15%减伤效果] [技能等级:"+addPoint+"] [伤害:"+damage+"-->"+newDamage+"] [玩家:"+player.getName()+"]");
					}
				}
			}
		}
		return newDamage;
	}
	
	//所有技能造成伤害增加0.5%/1%/2%
	public int handle_暴燃(Player player,int damage){
		int newDamage = damage;
		int addPoint = -1;
		if(player != null && player.flyState == 1){
			TalentData data = getTalentData(player.getId());
			if(data != null && data.getPoints() != null){
				Integer level = data.getPoints().get(new Integer(暴燃));
				if(level != null){
					addPoint = level;
					if(level == 1){
						newDamage = (int)(damage * 1.005);
					}else if(level == 2){
						newDamage = (int)(damage * 1.01);
					}else if(level == 3){
						newDamage = (int)(damage * 1.02);
					}
					logger.info("[暴燃] [造成伤害提升] [技能等级:"+addPoint+"] [伤害:"+damage+"-->"+newDamage+"] [玩家:"+player.getName()+"]");
				}
			}
		}
		return newDamage;
	}
	
	//宠物攻击时，有几率额外造成30%伤害
	public int handle_仙婴兽化(Pet pet,int damage){
		int rNum = random.nextInt(100);
		int newDamage = damage;
		int addPoint = 0;
		if(pet != null && pet.getMaster() != null && pet.getMaster().flyState == 1){
			if(rNum <= 40){
				TalentData data = getTalentData(pet.getMaster().getId());
				if(data != null && data.getPoints() != null){
					Integer level = data.getPoints().get(new Integer(仙婴兽化));
					if(level != null && level > 0){
						newDamage = (int)(damage*1.3);
						addPoint = level;
						logger.info("[仙婴兽化] [造成伤害提升] [技能等级:"+addPoint+"] [rNum:"+rNum+"] [伤害:"+damage+"-->"+newDamage+"] [宠物:"+pet.getName()+"] [主人:"+(pet.getMaster()==null?"nul":pet.getMaster().getName())+"]");
					}
				}
			}
		}
		return newDamage;
	}
	
	//降低0.5%/1%/1.5%双防，提高宠物1%/2%/3%伤害
	public int handle_会心(Pet pet,int damage){
		int newDamage = damage;
		int addPoint = 0;
		if(pet != null && pet.getMaster() != null && pet.getMaster().flyState == 1){
			TalentData data = getTalentData(pet.getMaster().getId());
			if(data != null && data.getPoints() != null){
				Integer level = data.getPoints().get(new Integer(会心));
				if(level != null && level > 0){
					if(level == 1){
						newDamage = (int)(damage*1.01);
						addPoint = level;
					}else if(level == 2){
						newDamage = (int)(damage*1.02);
						addPoint = level;
					}else if(level == 3){
						newDamage = (int)(damage*1.03);
						addPoint = level;
					}
					logger.info("[会心] [提高宠物伤害] [技能等级:"+addPoint+"] [伤害:"+damage+"-->"+newDamage+"] [宠物:"+pet.getName()+"] [主人:"+(pet.getMaster()==null?"nul":pet.getMaster().getName())+"]");
				}
			}
		}
		return newDamage;
	}
	
	//宠物每次攻击时可额外造成20000/40000/60000伤害
	public int handle_猛兽(Pet pet,int damage){
		int newDamage = damage;
		int addPoint = 0;
		if(pet != null && pet.getMaster() != null && pet.getMaster().flyState == 1){
			TalentData data = getTalentData(pet.getMaster().getId());
			if(data != null && data.getPoints() != null){
				Integer level = data.getPoints().get(new Integer(猛兽));
				if(level != null){
					addPoint = level;
					if(level == 1){
						newDamage = damage + 20000;
					}else if(level == 2){
						newDamage = damage + 40000;
					}else if(level == 3){
						newDamage = damage + 60000;
					}
					logger.info("[猛兽] [造成伤害提升] [技能等级:"+addPoint+"] [伤害:"+damage+"-->"+newDamage+"] [宠物:"+pet.getName()+"] [主人:"+(pet.getMaster()==null?"nul":pet.getMaster().getName())+"]");
				}
			}
		}
		return newDamage;
	}
	
	//增加3%宠物伤害
	public int handle_强化猛兽(Pet pet,int damage){
		int newDamage = damage;
		int addPoint = 0;
		if(pet != null && pet.getMaster() != null && pet.getMaster().flyState == 1){
			TalentData data = getTalentData(pet.getMaster().getId());
			if(data != null && data.getPoints() != null){
				Integer level = data.getPoints().get(new Integer(强化猛兽));
				if(level != null && level == 1){
					addPoint = level;
					newDamage = (int)(damage * 1.03);
					logger.info("[强化猛兽] [造成伤害提升] [技能等级:"+addPoint+"] [伤害:"+damage+"-->"+newDamage+"] [宠物:"+pet.getName()+"] [主人:"+(pet.getMaster()==null?"nul":pet.getMaster().getName())+"]");
				}
			}
		}
		return newDamage;
	}
	
	/**
	 * 计算天赋技能属性
	 * @param player
	 * @param calType 1:附体，2:附体结束
	 */
	public void calTalentSkillEffect(Player player,int calType){
		int 增加气血上限 = 0;
		double 增加气血上限百分比 = 0;
		double 降低气血上限百分比 = 0;
		double 增加免爆百分比 = 0;
		double 增加暴击百分比 = 0;
		int 增加双防 = 0;
		double 增加双防百分比 = 0;
		double 增加闪躲百分比 = 0;
		int 增加移动速度百分比 = 0;
		int 增加仙婴等级 = 0;
		double 增加吃药恢复百分比 = 0;
		double 降低双功百分比 = 0;
		double 降低双防百分比 = 0;
		double 增加双功百分比 = 0;
		int 增加双功 = 0;
		double 提供宠物伤害百分比 = 0;
		double 增加破甲百分比 = 0;
		if(logger.isInfoEnabled()){
			logger.info("[计算天赋属性前] ["+(calType==1?"附体":"附体结束")+"] [角色:"+player.getName()+"] ["+player.getPlayerPropsString()+"]");
		}
		TalentData data = getTalentData(player.getId());
		if(data == null){
			logger.warn("[仙婴附体计算] [失败:天赋数据不存在] [calType:{}] [{}]",new Object[]{calType,player.getLogString()});
			return;
		}
		Iterator<Entry<Integer, Integer>> it = data.getPoints().entrySet().iterator();
		while(it.hasNext()){
			Entry<Integer, Integer> entry = it.next();
			if(entry != null){
				Integer id = entry.getKey();
				Integer point = entry.getValue();
				if(id != null && point != null && point.intValue() > 0){
					switch (id) {
					case 体质:	//增加50000/100000/150000气血上限
						if(point != null && point == 1){
							增加气血上限 += 50000;
						}else if(point != null && point == 2){
							增加气血上限 += 100000;
						}else if(point != null && point == 3){
							增加气血上限 += 150000;
						}
						break;
					case 幸运:	//增加1%/2%/3%免爆
						if(point != null && point == 1){
							增加免爆百分比 += 1;
						}else if(point != null && point == 2){
							增加免爆百分比 += 2;
						}else if(point != null && point == 3){
							增加免爆百分比 += 3;
						}
						break;
					case 神赐:	//增加20000/40000/60000双防
						if(point != null && point == 1){
							增加双防 += 20000;
						}else if(point != null && point == 2){
							增加双防 += 40000;
						}else if(point != null && point == 3){
							增加双防 += 60000;
						}
						break;
					case 强化神元:	//增加3%气血
						增加气血上限百分比 += 3;
						break;
					case 闪避:	//增加1%/2%/3%闪躲
						if(point != null && point == 1){
							增加闪躲百分比 += 1;
						}else if(point != null && point == 2){
							增加闪躲百分比 += 2;
						}else if(point != null && point == 3){
							增加闪躲百分比 += 3;
						}
						break;
					case 强化神赐:	//增加3%双防
						增加双防百分比 += 3;
						break;
					case 灵巧:	//增加1%/4%/7%移动速度
						if(point != null && point == 1){
							增加移动速度百分比 += 1;
						}else if(point != null && point == 2){
							增加移动速度百分比 += 4;
						}else if(point != null && point == 3){
							增加移动速度百分比 += 7;
						}
						break;
					case 领悟:	//提升10/20/30级仙婴等级 （最多提升至300级）
						if(point != null && point == 1){
							增加仙婴等级 += 10;
						}else if(point != null && point == 2){
							增加仙婴等级 += 20;
						}else if(point != null && point == 3){
							增加仙婴等级 += 30;
						}
						break;
					case 仙疗:	//吃药恢复效果增加10%/20%/30%
						if(point != null && point == 1){
							增加吃药恢复百分比 += 10;
						}else if(point != null && point == 2){
							增加吃药恢复百分比 += 20;
						}else if(point != null && point == 3){
							增加吃药恢复百分比 += 30;
						}
						break;
					case 温和:	//降低0.5%/1%/1.5%双攻，提高1%/2%/3%气血上限
						if(point != null && point == 1){
							降低双功百分比 += 0.5;
							增加气血上限百分比 += 1;
						}else if(point != null && point == 2){
							降低双功百分比 += 1;
							增加气血上限百分比 += 2;
						}else if(point != null && point == 3){
							降低双功百分比 += 1.5;
							增加气血上限百分比 += 3;
						}
						break;
					case 坚固:	//降低0.5%/1%/1.5%双攻，提高1%/2%/3%双防
						if(point != null && point == 1){
							降低双功百分比 += 0.5;
							增加双防百分比 += 1;
						}else if(point != null && point == 2){
							降低双功百分比 += 1;
							增加双防百分比 += 2;
						}else if(point != null && point == 3){
							降低双功百分比 += 1.5;
							增加双防百分比 += 3;
						}
						break;
					case 仙婴体质:	//每2秒回复3%的气血
						if(calType == 1){
							BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateById(1378);
							if(bt == null || !(bt instanceof BuffTemplate_JiaXue)){
								logger.warn("[仙婴附体计算] [仙婴体质] [出错:没找到buff模板] ["+player.getLogString()+"]");
							}else{
								BuffTemplate_JiaXue bj = new BuffTemplate_JiaXue();
								bj.setId(bt.getId());
								bj.setName(bt.getName());
								bj.setBuffType(bt.getBuffType());
								bj.setCanUseType(bt.getCanUseType());
								bj.setSyncWithClient(bt.isSyncWithClient());
								bj.setTimeType(bt.getTimeType());
								bj.setIconId(bt.getIconId());
								bj.setDeadNotdisappear(bt.isDeadNotdisappear());
								bj.setGroupId(bt.getGroupId());
								bj.setHp(new int[]{20});
//								bj.setLastingTime(new long[]{TALENT_SKILL_CD_LAST_TIME});
								Buff buff = bj.createBuff(0);
								if(buff != null && buff instanceof Buff_JiaXue){
									Buff_JiaXue b = (Buff_JiaXue)buff;
									b.setStartTime(System.currentTimeMillis());
									b.setInvalidTime(buff.getStartTime() + TALENT_SKILL_CD_LAST_TIME);
									player.placeBuff(buff);
									logger.warn("[仙婴附体计算] [仙婴体质] [种植buff成功] ["+player.getLogString()+"]");
								}
							}
						}
						break;
					case 怒火:	//增加1%/2%/3%暴击
						if(point != null && point == 1){
							增加暴击百分比 += 1;
						}else if(point != null && point == 2){
							增加暴击百分比 += 2;
						}else if(point != null && point == 3){
							增加暴击百分比 += 3;
						}
						break;
					case 神力:	//增加20000/40000/60000双攻
						if(point != null && point == 1){
							增加双功 += 20000;
						}else if(point != null && point == 2){
							增加双功 += 40000;
						}else if(point != null && point == 3){
							增加双功 += 60000;
						}
						break;
					case 碎甲:	//增加1%/2%/3%破甲
						if(point != null && point == 1){
							增加破甲百分比 += 1;
						}else if(point != null && point == 2){
							增加破甲百分比 += 2;
						}else if(point != null && point == 3){
							增加破甲百分比 += 3;
						}
						break;
					case 强化神力:	//增加3%双攻
						增加双功百分比 += 3;
						break;
					case 会心:	//降低0.5%/1%/1.5%双防，提高宠物1%/2%/3%伤害
						if(point != null && point == 1){
							降低双防百分比 += 0.5;
							提供宠物伤害百分比 += 1;
						}else if(point != null && point == 2){
							降低双防百分比 += 1;
							提供宠物伤害百分比 += 2;
						}else if(point != null && point == 3){
							降低双防百分比 += 1.5;
							提供宠物伤害百分比 += 3;
						}
						break;
					case 破斧:	//降低0.5%/1%/1.5%气血，提高1%/2%/3%自身双攻
						if(point != null && point == 1){
							降低气血上限百分比 += 0.5;
							增加双功百分比 += 1;
						}else if(point != null && point == 2){
							降低气血上限百分比 += 1;
							增加双功百分比 += 2;
						}else if(point != null && point == 3){
							降低气血上限百分比 += 1.5;
							增加双功百分比 += 3;
						}
						break;
					case 宝石神悟:	//提高自身宝石收益1%/2%/3%
						EquipmentColumn ec = player.getEquipmentColumns();
						int allInlayProppertyValues [] = new int[InlayArticle.propertysValuesStrings.length];
						int inlayNums = 0;
						int equipNums = 0;
						int xiaziNums = 0;
						if(ec != null){
							long ids[] = ec.getEquipmentIds();
							if(ids != null){
								for (int i = 0; i < ids.length; i++) {
									if (ids[i] > 0) {
										equipNums++;
										ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(ids[i]);
										if (ae != null && ae instanceof EquipmentEntity) {
											EquipmentEntity ee = (EquipmentEntity) ae;
											Article a = ArticleManager.getInstance().getArticle(ee.getArticleName());
											if (a != null && (a instanceof Equipment)) {
												long[] inlays = ee.getInlayArticleIds();
												if(inlays != null && inlays.length > 0){
													for(long bid : inlays){
														if(bid <= 0){
															continue;
														}
														ArticleEntity bae = ArticleEntityManager.getInstance().getEntity(bid);
														if(bae != null){
															Article ba = ArticleManager.getInstance().getArticle(bae.getArticleName());
															if(ba != null && ba instanceof InlayArticle){
																InlayArticle inlay = (InlayArticle)ba;
																if(inlay.getInlayType() > 1){
																	
																	BaoShiXiaZiData shenXia = ArticleManager.getInstance().getxiLianData(player,bid);
																	if(shenXia != null){
																		xiaziNums++;
																		String names[] = shenXia.getNames();
																		if(names != null && names.length > 0){
																			for(String name : names){
																				if(name != null && !name.isEmpty()){
																					Article baoshi = ArticleManager.getInstance().getArticle(name);
																					if(baoshi != null && baoshi instanceof InlayArticle){
																						inlayNums++;
																						int[] props = ((InlayArticle)baoshi).getPropertysValues();
																						if(props != null){
																							for(int j=0;j<props.length;j++){
																								if(props[j] > 0){
																									allInlayProppertyValues[j] += props[j];
																								}
																							}
																							logger.warn("[宝石神悟] [{}] [计算宝石匣子属性] [装备：{}] [匣子里的宝石:{}] [匣子id:{}] [宝石数:{}] [宝石:{}] [宝石属性:{}] [总属性:{}] [{}]",new Object[]{calType==1?"开始附体":"附体结束",ee.getArticleName(),Arrays.toString(names), bid,inlays.length,inlay.getName(),Arrays.toString(props),Arrays.toString(allInlayProppertyValues),player.getName()});
																						}
																					}
																				}
																			}
																		}
																	}
																	
																}else{
																	inlayNums++;
																	int props[] = inlay.getPropertysValues();
																	if(props != null){
																		for(int j=0;j<props.length;j++){
																			if(props[j] > 0){
																				allInlayProppertyValues[j] += props[j];
																			}
																		}
																		logger.warn("[宝石神悟] [{}] [计算宝石属性] [装备：{}] [宝石数:{}] [宝石:{}] [宝石属性:{}] [总属性:{}] [{}]",new Object[]{calType==1?"开始附体":"附体结束",ee.getArticleName(),inlays.length,inlay.getName(),Arrays.toString(props),Arrays.toString(allInlayProppertyValues),player.getName()});
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
						
						{
							int newAllInlayProppertyValues [] = new int[InlayArticle.propertysValuesStrings.length];
							for(int i=0;i<newAllInlayProppertyValues.length;i++){
								if(allInlayProppertyValues[i] > 0){
									if(point == 1){
										newAllInlayProppertyValues[i] = (int)(allInlayProppertyValues[i] * 0.01);
									}else if(point == 2){
										newAllInlayProppertyValues[i] = (int)(allInlayProppertyValues[i] * 0.02);
									}else if(point == 3){
										newAllInlayProppertyValues[i] = (int)(allInlayProppertyValues[i] * 0.03);
										
									}
								}
							}
							logger.warn("[宝石神悟] [{}] [计算总属性] [完毕] [总装备数：{}] [总宝石数:{}] [总的匣子数:{}] [总属性:{}] [附体后额外的属性:{}] [{}]",new Object[]{calType==1?"开始附体":"附体结束", equipNums,inlayNums,xiaziNums,Arrays.toString(allInlayProppertyValues),Arrays.toString(newAllInlayProppertyValues), player.getName()});
							if(calType == 1){
								addPropertyValue(player, newAllInlayProppertyValues);
							}else if(calType == 2){
								removePropertyValue(player, newAllInlayProppertyValues);
							}
						}
						
						break;
					case 羽翼神悟:	
						break;
					case 专注:	//增加1%/5%/10%控制类技能持续时间
//						if(calType == 1){
//							if(point == 1){
//								player.decreaseConTimeRate -= 10;
//							}else if(point == 2){
//								player.decreaseConTimeRate -= 50;
//							}else if(point == 3){
//								player.decreaseConTimeRate -= 100;
//							}
//						}else if(calType == 2){
//							if(point == 1){
//								player.decreaseConTimeRate += 10;
//							}else if(point == 2){
//								player.decreaseConTimeRate += 50;
//							}else if(point == 3){
//								player.decreaseConTimeRate += 100;
//							}
//						}
						break;
					case 仙悟:	//增加2%/4%/6%仙婴全属性
						if(point != null && point >=1 && point <= 3){
							int realLevel = data.getXylevel()+data.getXylevelA();
							if(realLevel > MAX_XIANYIN_LEVEL){
								realLevel = MAX_XIANYIN_LEVEL;
							}
						}
						break;
					case 仙婴固化:	//额外获得15%减伤效果
//						BuffTemplate tt = BuffTemplateManager.getInstance().getBuffTemplateByName(Translate.千层减伤);
//						Buff buff = tt.createBuff(7);
//						buff.setStartTime(SystemTime.currentTimeMillis());
//						buff.setInvalidTime(buff.getStartTime() + 28000);
//						buff.setCauser(player);
//						player.placeBuff(buff);
//						logger.warn("[仙婴固化] [{}] [千层减伤buff] [{}]",new Object[]{calType==1?"开始附体":"附体结束",player.getName()});
						break;
					default:
						break;
					}
				}
			}
		}
		//先求同属性加成总和
		//之后计算具体数值，再计算百分比
		if(calType == 1){
			if(增加气血上限  > 0){
				player.setMaxHPA(player.getMaxHPA() + 增加气血上限);
				player.setHp(player.getHp() + 增加气血上限);
			}
			
			if(增加双防 > 0){
				player.setPhyDefenceA(player.getPhyDefenceA() + 增加双防);
				player.setMagicDefenceA(player.getMagicDefenceA() + 增加双防);
			}
			
			if(增加双功 > 0){
				player.setPhyAttackA(player.getPhyAttackA() + 增加双功);
				player.setMagicAttackA(player.getMagicAttackA() + 增加双功);
			}
			
			if(增加气血上限百分比 > 0){
				player.setMaxHPC(player.getMaxHPC() + 增加气血上限百分比);
				player.setHp((int)(player.getHp()*((100+增加气血上限百分比)/100)));
			}
			
			if(增加暴击百分比 > 0){
				player.setCriticalHitC(player.getCriticalHitC() + 增加暴击百分比);
			}
			
			if(降低气血上限百分比 > 0){
				double hpc = player.getMaxHPC() - 降低气血上限百分比;
				if(hpc < -99){
					hpc = -99;
				}
				player.setMaxHPC(hpc);
				double hp = (100-降低气血上限百分比)/100;
				if(hp <= 0){
					hp = 1;
				}
				player.setHp((int)(player.getHp()*hp));
			}
			
			if(增加双功百分比 > 0){
				player.setPhyAttackC(player.getPhyAttackC() + 增加双功百分比);
				player.setMagicAttackC(player.getMagicAttackC() + 增加双功百分比);
			}
			
			if(降低双功百分比 > 0){
				double phyc = player.getPhyAttackC() - 降低双功百分比;
				if(phyc < -99){
					phyc = -99;
				}
				player.setPhyAttackC(phyc);
				double magc = player.getMagicAttackC() - 降低双功百分比;
				if(magc < -99){
					magc = -99;
				}
				player.setMagicAttackC(magc);
			}
			
			if(降低双防百分比 > 0){
				double phyc = player.getPhyDefenceC() - 降低双防百分比;
				if(phyc < -99){
					phyc = -99;
				}
				player.setPhyDefenceC(phyc);
				double magc = player.getMagicDefenceC() - 降低双防百分比;
				if(magc < -99){
					magc = -99;
				}
				player.setMagicDefenceC(magc);
			}
			
			if(增加双防百分比 > 0){
				player.setPhyDefenceC(player.getPhyDefenceC() + 增加双防百分比);
				player.setMagicDefenceC(player.getMagicDefenceC() + 增加双防百分比);
			}
		}else if(calType == 2){
			if(增加双防百分比 > 0){
				double phyDefence = player.getPhyDefenceC() - 增加双防百分比;
				double magicDefence = player.getMagicDefenceC() - 增加双防百分比;
				if(phyDefence < -99){
					phyDefence = -99;
				}
				if(magicDefence < -99){
					magicDefence = -99;
				}
				player.setPhyDefenceC(phyDefence);
				player.setMagicDefenceC(magicDefence);
			}
			
			if(增加双功百分比 > 0){
				double phyAttactC = player.getPhyAttackC() - 增加双功百分比;
				if(phyAttactC < -99){
					phyAttactC = -99;
				}
				player.setPhyAttackC(phyAttactC);
				double magicAttactC = player.getMagicAttackC() - 增加双功百分比;
				if(magicAttactC < -99){
					magicAttactC = -99;
				}
				player.setMagicAttackC(magicAttactC);
			}
			
			if(降低双功百分比 > 0){
				player.setPhyAttackC(player.getPhyAttackC() + 降低双功百分比);
				player.setMagicAttackC(player.getMagicAttackC() + 降低双功百分比);
			}
			
			if(降低双防百分比 > 0){
				double phyc = player.getPhyDefenceC() + 降低双防百分比;
				if(phyc < -99){
					phyc = -99;
				}
				player.setPhyDefenceC(phyc);
				double magc = player.getMagicDefenceC() + 降低双防百分比;
				if(magc < -99){
					magc = -99;
				}
				player.setMagicDefenceC(magc);
			}
			
			if(增加气血上限百分比 > 0){
				double hpc = player.getMaxHPC() - 增加气血上限百分比;
				if(hpc < -99){
					hpc = -99;
				}
				player.setMaxHPC(hpc);
				double hp = player.getHp()/((100+增加气血上限百分比)/100);
				if(hp <= 0){
					hp = 1;
				}
				player.setHp((int)hp);
			}
			
			if(降低气血上限百分比 > 0){
				double hpc = player.getMaxHPC() + 降低气血上限百分比;
				if(hpc < -99){
					hpc = -99;
				}
				player.setMaxHPC(hpc);
				double hp = player.getHp()/((100+降低气血上限百分比)/100);
				if(hp <= 0){
					hp = 1;
				}
				player.setHp((int)hp);
			}
			
			if(增加暴击百分比 > 0){
				double criC = player.getCriticalHitC() - 增加暴击百分比;
				if(criC < -99){
					criC = -99;
				}
				player.setCriticalHitC(criC);
			}
			
			if(增加气血上限  > 0){
				int maxhp = player.getMaxHPA() - 增加气血上限;
				if(maxhp <= 0){
					maxhp = 1;
				}
				player.setMaxHPA(maxhp);
				int hp = player.getHp() - 增加气血上限;
				if(hp <= 0){
					hp = 1;
				}
				player.setHp(hp);
			}
			
			if(增加双功 > 0){
				int phyAttack = player.getPhyAttackA() - 增加双功;
				int magicAttack = player.getMagicAttackA() - 增加双功;
				if(phyAttack < 0){
					phyAttack = 0;
				}
				if(magicAttack < 0){
					magicAttack = 0;
				}
				player.setPhyAttackA(phyAttack);
				player.setMagicAttackA(magicAttack);
			}
			
			if(增加双防 > 0){
				int phyDefence = player.getPhyDefenceA() - 增加双防;
				int magicDefence = player.getMagicDefenceA() - 增加双防;
				if(phyDefence < 0){
					phyDefence = 0;
				}
				if(magicDefence < 0){
					magicDefence = 0;
				}
				player.setPhyDefenceA(phyDefence);
				player.setMagicDefenceA(magicDefence);
			}
		}
		
		if(增加免爆百分比 > 0){
			if(calType == 1){
				player.setCriticalDefenceC(player.getCriticalDefenceC() + 增加免爆百分比);
			}else if(calType == 2){
				double critical = player.getCriticalDefenceC() - 增加免爆百分比;
				if(critical < -99){
					critical = -99;
				}
				player.setCriticalDefenceC(critical);
			}
		}
		
		if(增加闪躲百分比 > 0){
			if(calType == 1){
				player.setDodgeC(player.getDodgeC() + 增加闪躲百分比);
			}else if(calType == 2){
				double dodge = player.getDodgeC() - 增加闪躲百分比;
				if(dodge < -99){
					dodge = -99;
				}
				player.setDodgeC(dodge);
			}
		}
		
		if(增加破甲百分比 > 0){
			if(calType == 1){
				player.setBreakDefenceC(player.getBreakDefenceC() + 增加破甲百分比);
			}else if(calType == 2){
				double breakdefence = player.getBreakDefenceC() - 增加破甲百分比;
				if(breakdefence < -99){
					breakdefence = -99;
				}
				player.setBreakDefenceC(breakdefence);
			}
		}
		if(增加移动速度百分比 > 0){
			//仙婴刷移动速度bug修改
			if(calType == 1){
				player.setTempSpeedC(增加移动速度百分比);
			}else if(calType == 2){
				player.setTempSpeedC(0);
			}
		}
		
		if(calType == 1){
			player.setObjectScale((short)1300);
			player.flyState = 1;
		}else{
			player.setObjectScale((short)1000);
			player.flyState = 0;
		}
		if(logger.isInfoEnabled()){
			logger.info("[计算天赋属性后] ["+(calType==1?"附体":"附体结束")+"] [角色:"+player.getName()+"] ["+player.getPlayerPropsString()+"]");
		}
		logger.warn("[仙婴附体计算] [成功] [{}] [增加气血上限:{}] [增加气血上限百分比:{}] [降低气血上限百分比:{}] [增加免爆百分比:{}] [增加暴击百分比:{}] [增加双防:{}] [增加双防百分比:{}] [增加闪躲百分比:{}] [增加移动速度百分比:{}] [增加仙婴等级:{}] [增加双功:{}] [增加双功百分比:{}] [减少控制持续时间千分比:{}] [增加吃药恢复百分比:{}] [降低双功百分比:{}] [降低双防百分比:{}] [{}]",new Object[]{(calType==1?"附体":"附体结束"),
					增加气血上限,增加气血上限百分比,降低气血上限百分比,增加免爆百分比,增加暴击百分比,增加双防,增加双防百分比,增加闪躲百分比,增加移动速度百分比,增加仙婴等级,增加双功,增加双功百分比,player.decreaseConTimeRate,增加吃药恢复百分比,降低双功百分比,降低双防百分比,player.getLogString()});
	}
	
	public void handle_ONCLICK_TALENT_BUTTON_REQ(RequestMessage message, Player player){
		long now = System.currentTimeMillis();
		if (player.isIceState()) {
			player.sendError(Translate.您处于冰冻状态下不能使用);
			return;
		}
		if(player.flyState == 1){
			player.sendError(Translate.仙婴附体中不能操作);
			return;
		}
		
		TalentData data = getTalentData(player.getId());
		long cdTimes = 0;
		boolean 仙悟开启 = false;
		if(data != null){
			if(data.getCdEndTime() > now){
				player.sendError(Translate.cd中不能使用);
				return;
			}
			cdTimes = now + TALENT_SKILL_CD_TIME - data.getMinusCDTimes();
			if(cdTimes <= 0){
				logger.warn("[仙婴附体] [出错:没cd了] [cdTimes:{}] [{}] [{}]",new Object[]{cdTimes,(data==null?"nul":data),player.getLogString()});
				return;
			}
			data.setNbEndTimes(now + TALENT_SKILL_CD_LAST_TIME);
			data.setCdEndTime(cdTimes);
			saveTalentData(player, data);
			int realLevel = data.getXylevel()+data.getXylevelA();
			if(realLevel > MAX_XIANYIN_LEVEL){
				realLevel = MAX_XIANYIN_LEVEL;
			}
			
			if(data.getPoints() != null){
				Integer point = data.getPoints().get(new Integer(仙悟));
				if(point != null){
					仙悟开启 = true;
					if(point == 1){
						calBasicSkillEffect(player, realLevel, 1,1.02);
					}else if(point == 2){
						calBasicSkillEffect(player, realLevel, 1,1.04);
					}else if(point == 3){
						calBasicSkillEffect(player, realLevel, 1,1.06);
					}
				}
			}
			
			if(!仙悟开启){
				calBasicSkillEffect(player,realLevel,1,1.0);
			}
			calTalentSkillEffect(player, 1);
			NOTICE_TALENT_BUTTON_RES res = new NOTICE_TALENT_BUTTON_RES(GameMessageFactory.nextSequnceNum(), 1,0, FlyTalentManager.TALENT_SKILL_CD_TIME-data.getMinusCDTimes());
			player.addMessageToRightBag(res);
			placeBuff(player);
		}
		logger.warn("[仙婴附体] [仙悟开启:{}] [lastTimes:{}] [{}] [{}]",new Object[]{仙悟开启,(cdTimes-now),(data==null?"nul":data),player.getLogString()});
	}
	
	public void calTalentState(Player p){
		TalentData data = FlyTalentManager.getInstance().getTalentData(p.getId());
		if(data != null){
			data.setCdEndTime(0);
			NOTICE_TALENT_BUTTON_RES noticeRes = new NOTICE_TALENT_BUTTON_RES(GameMessageFactory.nextSequnceNum(), 1, FlyTalentManager.TALENT_SKILL_CD_TIME - data.getMinusCDTimes(), FlyTalentManager.TALENT_SKILL_CD_TIME - data.getMinusCDTimes());
			p.addMessageToRightBag(noticeRes);
			logger.warn("[仙婴附体] [取消仙婴附体] [{}] [{}]",new Object[]{p.getLogString()});
		}
	}
	
	public void placeBuff(Player player){
		BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateById(1843);
		if(bt != null){
			Buff buff = bt.createBuff(0);
			if(buff != null && buff instanceof Buff_XueShangXian){
				Buff_XueShangXian b = (Buff_XueShangXian)buff;
				b.setStartTime(System.currentTimeMillis());
				b.setDescription("");
				b.setInvalidTime(buff.getStartTime() + TALENT_SKILL_CD_LAST_TIME);
				player.placeBuff(b);
				logger.warn("[仙婴附体] [种植buff] ["+player.getLogString()+"]");
			}
		}
		
		ParticleData[] particleDatas = new ParticleData[1];
		particleDatas[0] = new ParticleData(player.getId(), "角色光效/仙婴附体", 20000, 2, 1, 1);
		NOTICE_CLIENT_PLAY_PARTICLE_RES resp = new NOTICE_CLIENT_PLAY_PARTICLE_RES(GameMessageFactory.nextSequnceNum(), particleDatas);
		player.addMessageToRightBag(resp);
	}
	
	public void resetTalentProperty(Player p){
		p.flyState = 0;
		p.setObjectScale((short)1000);
		p.setPhyDefenceA(0);
		p.setMagicDefenceA(0);
	}
	
	/**
	 * 计算基础属性
	 * @param level
	 * @param type 1:附体，2:附体结束
	 */
	public void calBasicSkillEffect(Player player,int level,int type,double geneNum){
		TalentPageInfo info = infos.get(level - 1);
		if(info != null){
			if(logger.isInfoEnabled()){
				logger.info("[计算天赋基础前] ["+(type==1?"附体":"附体结束")+"] [角色:"+player.getName()+"] ["+player.getPlayerPropsString()+"]");
			}
			if(type == 1){
				player.setPhyAttackA(player.getPhyAttackA() + (int)(info.getPhyAttact() * geneNum));
				player.setPhyDefenceA(player.getPhyDefenceA() + (int)(info.getPhyDefence()* geneNum));
				player.setMaxHPA(player.getMaxHPA() + (int)(info.getHp()* geneNum));
				player.setHp(player.getHp() + (int)(info.getHp()* geneNum));
				player.setMagicAttackA(player.getMagicAttackA() + (int)(info.getMagicAttact()* geneNum));
				player.setMagicDefenceA(player.getMagicDefenceA() + (int)(info.getMagicDefence()* geneNum));
			}else if(type == 2){
				
				int phya = player.getPhyAttackA() - (int)(info.getPhyAttact()* geneNum);
				if(phya < 0){
					phya = PlayerManager.各个职业各个级别的基础属性值[player.getCurrSoul().getCareer()][player.getCurrSoul().getGrade() - 1][2];
				}
				player.setPhyAttackA(phya);
				
				int phyd = player.getPhyDefenceA() - (int)(info.getPhyDefence()* geneNum);
				if(phyd < 0){
					phyd = 0;
				}
				player.setPhyDefenceA(phyd);
				
				int hpa = player.getMaxHPA() - (int)(info.getHp()* geneNum);
				if(hpa <= 0){
					hpa = PlayerManager.各个职业各个级别的基础属性值[player.getCurrSoul().getCareer()][player.getCurrSoul().getGrade() - 1][0];
				}
				player.setMaxHPA(hpa);
				
				int hp = player.getHp() - (int)(info.getHp()* geneNum);
				if(hp <= 0){
					hp = 1;
				}
				player.setHp(hp);
				
				int maga = player.getMagicAttackA() - (int)(info.getMagicAttact()* geneNum);
				if(maga < 0){
					maga = PlayerManager.各个职业各个级别的基础属性值[player.getCurrSoul().getCareer()][player.getCurrSoul().getGrade() - 1][3];
				}
				player.setMagicAttackA(maga);
				 
				int magd = player.getMagicDefenceA() - (int)(info.getMagicDefence()* geneNum);
				if(magd < 0){
					magd = 0;
				}
				player.setMagicDefenceA(magd);
			}
			if(logger.isInfoEnabled()){
				logger.info("[计算天赋基础属性后] ["+(type==1?"附体":"附体结束")+"] [角色:"+player.getName()+"] [geneNum:"+geneNum+"] ["+player.getPlayerPropsString()+"]");
			}
		}
	}
	
	public FLY_TALENT_ADD_POINTS_RES handle_FLY_TALENT_ADD_POINTS_REQ(RequestMessage message, Player player){
		FLY_TALENT_ADD_POINTS_REQ req = (FLY_TALENT_ADD_POINTS_REQ)message;
		int calType = req.getCalType();		
		int skillId = req.getSkillId();
		if (player.isIceState()) {
			player.sendError(Translate.您处于冰冻状态下不能使用);
			return null;
		}
		TalentSkillInfo sInfo = skillInfos.get(new Integer(skillId));
		if(sInfo == null){
			logger.warn("[飞升天赋技能点操作] [出错:技能信息不存在] [{}] [{}] [{}]",new Object[]{(calType==0?"加点":"减点"),skillId,player.getLogString()});
			return null;
		}
		if(player.flyState == 1){
			player.sendError(Translate.仙婴附体中不能操作);
			return null;
		}
		
		//synchronized (player) {
			TalentData data = getTalentData(player.getId());
			if(data == null){
				data = new TalentData();
			}
			
			int aPoints = 0;
			int bPoints = 0;
			int tempPoints = 0;
			
			for(int id : TALENT_SKILL_A){
				if(data.getPoints().get(new Integer(id)) != null){
					aPoints += data.getPoints().get(new Integer(id));
				}
			}
			
			for(int id : TALENT_SKILL_B){
				if(data.getPoints().get(new Integer(id)) != null){
					bPoints += data.getPoints().get(new Integer(id));
				}
			}
			Map<Integer, Integer> taneltSkillTempAddPoints = player.taneltSkillTempAddPoints;
			Integer hasAddTempPoints = player.taneltSkillTempAddPoints.get(new Integer(skillId));
			Iterator<Integer> it = taneltSkillTempAddPoints.values().iterator();
			while(it.hasNext()){
				Integer value = it.next();
				if(value != null){
					tempPoints += value.intValue();
				}
			}
			int overCanUsePoints = getOwnPoints(player) - aPoints - bPoints - tempPoints;
			if(calType == 0){
				if(overCanUsePoints <= 0){
					player.sendError(Translate.没有剩余点数);
					return null;
				}
				
				Integer hasAddPoints = data.getPoints().get(new Integer(skillId));
				int allHasAddPoints = 0;
				if(hasAddTempPoints != null){
					allHasAddPoints += hasAddTempPoints;
				}
				if(hasAddPoints != null){
					allHasAddPoints += hasAddPoints;
				}
				if(allHasAddPoints >= sInfo.getMaxLevel()){
					player.sendError(Translate.天赋技能点已经加满);
					return null;
				}
				if(hasAddTempPoints == null){
					player.taneltSkillTempAddPoints.put(skillId, 0);
				}
				
				TalentSkillClientInfo skillInfo[] = Arrays.copyOf(getClientTemplateData(),getClientTemplateData().length);
				TalentSkillInfo currAddSkill = skillInfos.get(new Integer(skillId));
				if(currAddSkill == null){
					logger.warn("[飞升天赋技能点操作] [出错:没找到技能信息] [技能id:"+skillId+"] [calType:"+calType+"] ["+taneltSkillTempAddPoints+"] [aPoints:"+aPoints+"] [bPoints:"+bPoints+"] [剩余可用点数:"+overCanUsePoints+"] [临时加的点数:"+hasAddTempPoints+"] [player:"+player.getName()+"]");
					return null;
				}
				
				int 上一层技能总点数 = 0;
				int 前置技能加点数 = 0;
				int 前置技能最大级别 = 0;
				int 当前操作技能所在层数 = sInfo.getLayer();
				int 前置id = sInfo.getRelyId();
				int 开启限制点数 = (sInfo.getLayer() - 1) * 4;
				for(TalentSkillClientInfo info : skillInfo){
					if(info != null){
						if(前置id == info.getId()){
							TalentSkillInfo relyData = skillInfos.get(new Integer(前置id));
							if(relyData != null){
								前置技能最大级别  = relyData.getMaxLevel();
							}
							Integer nums = data.getPoints().get(new Integer(前置id));
							Integer temppoints = player.taneltSkillTempAddPoints.get(new Integer(前置id));
							if(nums != null){
								前置技能加点数 += nums;
							}
							if(temppoints != null){
								前置技能加点数 += temppoints;
							}
						}
						if(currAddSkill.getTalentType() == info.getTalentType() && info.getLayer() <= 当前操作技能所在层数 - 1){
							Integer temppoints = player.taneltSkillTempAddPoints.get(new Integer(info.getId()));
							Integer nums = data.getPoints().get(new Integer(info.getId()));
							if(nums != null){
								上一层技能总点数 += nums;
							}
							if(temppoints != null){
								上一层技能总点数 += temppoints;
							}
						}
					}
				}
				
				//技能还没亮操作加减点
				if(currAddSkill.getLayer() > 1){
					if(上一层技能总点数 < 开启限制点数){
						player.sendError(Translate.上一层天赋点数不够);
						return null;
					}
					if(currAddSkill.getRelyId() >= 0){
						if(前置技能加点数 < 前置技能最大级别){
							player.sendError(Translate.前置技能没加满);
							return null;
						}
					}
				}
				
				hasAddTempPoints = player.taneltSkillTempAddPoints.get(new Integer(skillId));
				player.taneltSkillTempAddPoints.put(skillId, hasAddTempPoints.intValue()+1);
				
				Iterator<Entry<Integer, Integer>> itt = player.taneltSkillTempAddPoints.entrySet().iterator();
				while(itt.hasNext()){
					Entry<Integer, Integer> entry = itt.next();
					if(entry != null){
						Integer id = entry.getKey();
						Integer num = entry.getValue();
						if(num != null){
							if(id != null){
								TalentSkillInfo skinfo = skillInfos.get(id);
								if(skinfo != null){
									if(skinfo.getTalentType() == 0){
										aPoints+=num;
									}else if(skinfo.getTalentType() == 1){
										bPoints+=num;
									}
								}
							}
						}
					}
				}
				
				for(TalentSkillClientInfo info : skillInfo){
					if(info != null){
						Integer point = data.getPoints().get(new Integer(info.getId()));
						Integer temppoints = player.taneltSkillTempAddPoints.get(new Integer(info.getId()));
						if(point != null){
							info.setCurrLevel(info.getCurrLevel() + point);
						}
						if(temppoints != null){
							info.setCurrLevel(info.getCurrLevel() + temppoints);
						}
//						System.out.println("[等级测试] [技能："+info.getName()+"] [icon:"+info.getIcon()+"] [id:"+skillId+"] [skillLimitmess:"+info.getSkillLimitMess()+"] [point:"+point+"] [temppoints:"+temppoints+"] [等级:"+info.getCurrLevel()+"/"+info.getMaxLevel()+"] ["+player.getName()+"]");
					}
				}
				
				overCanUsePoints = getOwnPoints(player) - aPoints - bPoints;
				
				synchSkillOpenState(player, skillInfo,overCanUsePoints);
				synchClientSkillInfo(player, data, skillInfo);
				
				logger.warn("[飞升天赋技能点操作] [成功] [{}] [技能:{}] [可用点:{}] [a天赋加点:{}] [b天赋加点::{}] [加点情况:{}] [前置id:{}] [前置技能加点数:{}] [前置技能最大级别:{}] [上一层技能总点数:{}] [{}]",new Object[]{(calType==0?"加点":"减点"),skillId,overCanUsePoints,aPoints,bPoints,player.taneltSkillTempAddPoints,前置id,前置技能加点数,前置技能最大级别,上一层技能总点数,player.getLogString()});
				FLY_TALENT_ADD_POINTS_RES res = new FLY_TALENT_ADD_POINTS_RES(req.getSequnceNum(), overCanUsePoints, aPoints, bPoints, skillInfo);
				return res;
			}else if(calType == 1){
				if(hasAddTempPoints != null && hasAddTempPoints.intValue() > 0){
					//撤销加点，连带操作技能的下层
					Iterator<Entry<Integer, TalentSkillInfo>> it2 = skillInfos.entrySet().iterator();
					while(it2.hasNext()){
						Entry<Integer, TalentSkillInfo> entry = it2.next();
						if(entry != null){
							TalentSkillInfo info = entry.getValue();
							if(info != null && info.getTalentType() == sInfo.getTalentType()){
								if(info.getLayer() > sInfo.getLayer()){
									player.taneltSkillTempAddPoints.put(new Integer(info.getId()),0);
								}
							}
						}
					}
					
					player.taneltSkillTempAddPoints.put(new Integer(skillId),hasAddTempPoints-1);
					TalentSkillClientInfo skillInfo[] = Arrays.copyOf(getClientTemplateData(),getClientTemplateData().length);
					for(TalentSkillClientInfo info : skillInfo){
						if(info != null){
							Integer point = data.getPoints().get(new Integer(info.getId()));
							if(point != null && point.intValue() > 0){
								info.setCurrLevel(point.intValue());
							}
							Integer temppoints = player.taneltSkillTempAddPoints.get(new Integer(info.getId()));
							if(temppoints != null){
								info.setCurrLevel(info.getCurrLevel() + temppoints);
							}
						}
					}


					Iterator<Entry<Integer, Integer>> itt = player.taneltSkillTempAddPoints.entrySet().iterator();
					while(itt.hasNext()){
						Entry<Integer, Integer> entry = itt.next();
						if(entry != null){
							Integer id = entry.getKey();
							Integer num = entry.getValue();
							if(num != null){
								if(id != null){
									TalentSkillInfo skinfo = skillInfos.get(id);
									if(skinfo != null){
										if(skinfo.getTalentType() == 0){
											aPoints+=num;
										}else if(skinfo.getTalentType() == 1){
											bPoints+=num;
										}
									}
								}
							}
						}
					}
					
					overCanUsePoints = getOwnPoints(player) - aPoints - bPoints;
					
					skillInfo = synchSkillOpenState(player, skillInfo,overCanUsePoints);
					
					synchClientSkillInfo(player, data, skillInfo);
					
					logger.warn("[飞升天赋技能点操作] [成功] [{}] [可用点:{}] [技能:{}] [a天赋加点:{}] [b天赋加点::{}] [加点情况:{}] [{}]",new Object[]{(calType==0?"加点":"减点"),overCanUsePoints,skillId,aPoints,bPoints,player.taneltSkillTempAddPoints,player.getLogString()});
					FLY_TALENT_ADD_POINTS_RES res = new FLY_TALENT_ADD_POINTS_RES(req.getSequnceNum(), overCanUsePoints, aPoints, bPoints, skillInfo);
					return res;
				}else{
					player.sendError(Translate.还没分配点数);
					return null;
				}
			}
		//}
		return null;
	}
	
	/**
	 * 同步天赋技能开启状态
	 * @param player
	 * @param skillInfo
	 * @param overPoints：剩余可用点数
	 * @return
	 */
	public TalentSkillClientInfo[] synchSkillOpenState(Player player,TalentSkillClientInfo [] skillInfo,int overPoints){
		TalentData data = getTalentData(player.getId());
		if(data == null){
			data = new TalentData();
		}
		for(TalentSkillClientInfo info : skillInfo){
			if(info != null){
				TalentSkillClientInfo relyInfo = null;
				//仙婴小于15级
				if(data.getXylevel() < 15){
					info.setIsOpen(false);
					continue;
				}
				if(overPoints <= 0 && info.getCurrLevel() <= 0){
					info.setIsOpen(false);
					continue;
				}
				int point = info.getCurrLevel();
				if(point > 0){	
					info.setIsOpen(true);		//加过点点
				}else{
					boolean isFirstLayer = false;
					for(int layer : 第一层天赋技能){
						if(layer == info.getId()){
							isFirstLayer = true;
							break;
						}
					}
					if(isFirstLayer){
						info.setIsOpen(true);	//第一层
					}else{
						int upLayerAllPoints = 0;
						if(info.getLayer() > 1){
							int limitPoints = (info.getLayer() - 1) * 4;
							for(TalentSkillClientInfo info2 : skillInfo){
								if(info2.getTalentType() == info.getTalentType()){
									if(info2.getLayer() <= info.getLayer() - 1){
										Integer relyPoint = info2.getCurrLevel();//data.getPoints().get(new Integer(info2.getId()));
										if(relyPoint != null){
											upLayerAllPoints += relyPoint;
										}
									}
								}
								if(info.getRelyId() == info2.getId()){
									relyInfo = info2;
								}
							}
							if(upLayerAllPoints >= limitPoints){	//上面层限制
								if(info.getRelyId() >= 0){
									if(relyInfo != null){
										Integer relyPoint = relyInfo.getCurrLevel();//data.getPoints().get(new Integer(info.getRelyId()));
										if(relyPoint != null && relyPoint >= relyInfo.getMaxLevel()){
											info.setIsOpen(true);	
										}
//									System.out.println("[天赋技能开启测试] [有前置id:"+info.getRelyId()+"] [前置id点:"+relyPoint+"] [前置id最大点:"+relyInfo.getMaxLevel()+"] [技能："+info.getName()+"] [技能id:"+info.getId()+"] [点："+point+"] [玩家:"+player.getName()+"]");
									}
								}else{
									info.setIsOpen(true);	
								}
							}
//							System.out.println("[天赋技能开启测试] ["+info.isIsOpen()+"] [上一层天赋总加点:"+upLayerAllPoints+"] [上面层限制:"+limitPoints+"] [技能："+info.getName()+"] [技能id:"+info.getId()+"] [点："+point+"] [玩家:"+player.getName()+"]");
						}
					}
				}
			}
		}
		return skillInfo;
	}
	
	public void synchClientSkillInfo(Player player,TalentData data ,TalentSkillClientInfo [] skillInfo){
		try{
			for(TalentSkillClientInfo info : skillInfo){
				if(info != null){
					Integer point = info.getCurrLevel();
					if(point == null){
						point = 0;
					}
					switch (info.getId()) {
						case 仙疗:	//吃药恢复效果增加10%/20%/30%
						case 领悟:	//提升10/20/30级仙婴等级 （最多提升至300级）
						case 灵巧:	//增加1%/4%/7%移动速度
						case 闪避:	//增加1%/2%/3%闪躲
						case 体质:	//增加50000/100000/150000气血上限
						case 幸运:	//增加1%/2%/3%免爆
						case 神赐:	//增加20000/40000/60000双防
						case 猛兽:	//宠物每次攻击时可额外造成20000/40000/60000伤害
						case 怒火:	//增加1%/2%/3%暴击
						case 神力:	//增加20000/40000/60000双攻
						case 碎甲:	//增加1%/2%/3%破甲
						case 专注:	//增加1%/5%/10%控制类技能持续时间
						case 仙悟:	//增加2%/4%/6%仙婴全属性
						case 暴燃:	//所有技能造成伤害增加0.5%/1%/2%
						case 宝石神悟:	//提高自身宝石收益1%/2%/3%
						case 羽翼神悟:	//翅膀基础属性收益提高0.5%/1%/1.5%
							if(info.getPropType() == 1){
								if(point == 1){
									info.setMess(String.format(info.getMess(), info.getLevelValues()[point-1]+"%",info.getLevelValues()[point]+"%").replace(".0", ""));
								}else if(point == 2){
									info.setMess(String.format(info.getMess(), info.getLevelValues()[point-1]+"%",info.getLevelValues()[point]+"%").replace(".0", ""));
								}else if(point == 3){
									info.setMess(String.format(info.getMess2(), info.getLevelValues()[point-1]+"%").replace(".0", ""));
								}else{
									info.setMess(String.format(info.getMess1(), info.getLevelValues()[0]+"%").replace(".0", ""));
								}
							}else{
								if(point == 1){
									info.setMess(String.format(info.getMess(), info.getLevelValues()[point-1],info.getLevelValues()[point]).replace(".0", ""));
								}else if(point == 2){
									info.setMess(String.format(info.getMess(), info.getLevelValues()[point-1],info.getLevelValues()[point]).replace(".0", ""));
								}else if(point == 3){
									info.setMess(String.format(info.getMess2(), info.getLevelValues()[point-1]).replace(".0", ""));
								}else{
									info.setMess(String.format(info.getMess1(), info.getLevelValues()[0]).replace(".0", ""));
								}
							}
//							logger.warn("[技能id:"+info.getId()+"] [point:"+point+"] [mess:"+info.getMess()+"]");
							break;
						case 强化神元:	//增加3%气血
						case 强化神赐:	//增加3%双防
						case 仙婴体质:	//每2秒回复3%的气血
						case 仙婴固化:	//额外获得15%减伤效果
						case 强化猛兽:	//增加3%宠物伤害
						case 强化神力:	//增加3%双攻
						case 仙婴兽化:	//宠物攻击时，有几率额外造成30%伤害
						case 仙婴悟攻:	//人物进行攻击时，有几率额外造成20%伤害
							if(point > 0){
								info.setMess(String.format(info.getMess2(), info.getLevelValues()[point-1]+"%").replace(".0", ""));
							}else{
								info.setMess(String.format(info.getMess1(), info.getLevelValues()[0]+"%").replace(".0", ""));
							}
							break;
						case 会心:	//降低0.5%/1%/1.5%双防，提高宠物1%/2%/3%伤害
						case 破斧:	//降低0.5%/1%/1.5%气血，提高1%/2%/3%自身双攻
						case 温和:	//降低0.5%/1%/1.5%双攻，提高1%/2%/3%气血上限
						case 坚固:	//降低0.5%/1%/1.5%双攻，提高1%/2%/3%双防
							if(point == 1){
								info.setMess(String.format(info.getMess(), "0.5%","1%","1%", "2%"));
							}else if(point == 2){
								info.setMess(String.format(info.getMess(),"1%", "2%","1.5%","3%"));
							}else if(point == 3){
								info.setMess(String.format(info.getMess2(), "1.5%","3%"));
							}else{
								info.setMess(String.format(info.getMess1(), "0.5%","1%"));
							}
							break;
						default:
							break;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.warn("[飞升天赋技能点操作] [{}] [异常:{}]",new Object[]{player.getLogString(), e});
		}
	}
	
	public void resetTalentPoints(Player player){
		if(player.flyState == 1){
			player.sendError(Translate.仙婴附体中不能操作);
			return;
		}
		TalentData data = getTalentData(player.getId());
		if(data == null){
			logger.warn("[重置飞升天赋] [失败:天赋数据不存在] [{}]",new Object[]{player.getLogString()});	
			return;
		}
		saveTalentData(player, data);
		logger.warn("[重置飞升天赋] [成功] [{}] [{}]",new Object[]{data,player.getLogString()});
	}
	 
	/**
	 * 客户端天赋技能模板数据
	 * @return
	 */
	public TalentSkillClientInfo[] getClientTemplateData(){
		List<TalentSkillClientInfo> tempSkillInfo = new ArrayList<TalentSkillClientInfo>();
		if(tempSkillInfo.size() > 0){
			return tempSkillInfo.toArray(new TalentSkillClientInfo[]{});
		}
		for(int id : TALENT_SKILL){
			Iterator<Entry<Integer, TalentSkillInfo>> it = skillInfos.entrySet().iterator();
			while(it.hasNext()){
				Entry<Integer, TalentSkillInfo> entry = it.next();
				if(entry != null){
					TalentSkillInfo info = entry.getValue();
					if(id == info.getId()){
						TalentSkillClientInfo cinfo = new TalentSkillClientInfo();
						cinfo.setId(info.getId());
						cinfo.setName(info.getName());
						cinfo.setMess2(info.getMess2());
						cinfo.setMess3(info.getMess3());
						cinfo.setMess1(info.getMess1());
						cinfo.setIcon(info.getIcon());
						cinfo.setMess(info.getMess());
						cinfo.setMaxLevel(info.getMaxLevel());
						cinfo.setRelyId(info.getRelyId());
						cinfo.setTalentType(info.getTalentType());
						cinfo.setLevelValues(info.getLevelValues());
						cinfo.setLayer(info.getLayer());
						cinfo.setPropType(info.getPropType());
						cinfo.setSkillLimitMess(info.getSkillLimitMess());
						tempSkillInfo.add(cinfo);
					}
				}
			}
		}
		return tempSkillInfo.toArray(new TalentSkillClientInfo[]{});
	}
	
	public int 得到剩余点数(Player player){
		TalentData data = getTalentData(player.getId());
		if(data == null){
			data = new TalentData();
		}
		
		int aPoints = 0;
		int bPoints = 0;
		int tempPoints = 0;
		
		for(int id : TALENT_SKILL_A){
			if(data.getPoints().get(new Integer(id)) != null){
				aPoints += data.getPoints().get(new Integer(id));
			}
		}
		
		for(int id : TALENT_SKILL_B){
			if(data.getPoints().get(new Integer(id)) != null){
				bPoints += data.getPoints().get(new Integer(id));
			}
		}
		
		Map<Integer, Integer> taneltSkillTempAddPoints = player.taneltSkillTempAddPoints;
		Iterator<Integer> it = taneltSkillTempAddPoints.values().iterator();
		while(it.hasNext()){
			Integer value = it.next();
			if(value != null){
				tempPoints += value.intValue();
			}
		}
		int overCanUsePoints = getOwnPoints(player) - aPoints - bPoints - tempPoints;
		return overCanUsePoints;
	}
	
	public QUERY_TALENT_SKILLS_RES handle_QUERY_TALENT_SKILLS_REQ(RequestMessage message, Player player){
		QUERY_TALENT_SKILLS_REQ req = (QUERY_TALENT_SKILLS_REQ)message;
		
		Player seePlayer = null;
		try{
			seePlayer = PlayerManager.getInstance().getPlayer(req.getPlayerId());
		}catch(Exception e){
			logger.warn("[请求天赋技能界面信息] [异常] [查看的player不存储] [{}] [{}]",new Object[]{req.getPlayerId(),player.getLogString()});
			return null;
		}
		String helpMess = 天赋界面帮助信息;
		int aPoints = 0;
		int bPoints = 0;
		
		TalentData data = getTalentData(seePlayer.getId());
		if(data == null){
			data = new TalentData();
		}
		TalentSkillClientInfo playerInfos [] = Arrays.copyOf(getClientTemplateData(),getClientTemplateData().length);
		
		for(int id : TALENT_SKILL_A){
			if(data.getPoints().get(new Integer(id)) != null){
				aPoints += data.getPoints().get(new Integer(id));
			}
		}
		
		for(int id : TALENT_SKILL_B){
			if(data.getPoints().get(new Integer(id)) != null){
				bPoints += data.getPoints().get(new Integer(id));
			}
		}
		
		if(playerInfos == null){
			playerInfos = new TalentSkillClientInfo[]{};
		}
		
		int tempPoints = 0;
		Map<Integer, Integer> taneltSkillTempAddPoints = seePlayer.taneltSkillTempAddPoints;
		Iterator<Integer> it = taneltSkillTempAddPoints.values().iterator();
		while(it.hasNext()){
			Integer value = it.next();
			if(value != null){
				tempPoints += value.intValue();
			}
		}
		
		int overCanUsePoints = getOwnPoints(seePlayer) - aPoints - bPoints - tempPoints;
		
		for(TalentSkillClientInfo info : playerInfos){
			if(info != null){
				Integer point = data.getPoints().get(new Integer(info.getId()));
				Integer temppoints = seePlayer.taneltSkillTempAddPoints.get(new Integer(info.getId()));
				if(point != null){
					info.setCurrLevel(info.getCurrLevel() + point);
				}
				if(temppoints != null){
					info.setCurrLevel(info.getCurrLevel() + temppoints);
				}
			}
		}
		
		synchClientSkillInfo(seePlayer, data, playerInfos);
		
		synchSkillOpenState(seePlayer, playerInfos,overCanUsePoints);
		
//		for(TalentSkillClientInfo info : playerInfos){
//			if(info != null){
//				System.out.println("测试："+info);
//			}
//		}
		
		logger.warn("[请求天赋技能界面信息] [aPoints:{}] [bPoints:{}] [tempPoints:{}] [overCanUsePoints:{}] [playerInfos:{}] [{}]",new Object[]{aPoints,bPoints,tempPoints,overCanUsePoints,playerInfos.length,player.getLogString()});
		QUERY_TALENT_SKILLS_RES res = new QUERY_TALENT_SKILLS_RES(message.getSequnceNum(),req.getPlayerId(), overCanUsePoints, aPoints, bPoints, helpMess, Translate.稳如泰山,Translate.势不可挡,playerInfos);
		return res;
	}
	
	public CONFIRM_TALENT_EXP_RES handle_CONFIRM_TALENT_EXP_REQ(RequestMessage message, Player player){
		CONFIRM_TALENT_EXP_REQ req = (CONFIRM_TALENT_EXP_REQ)message;
		int feedType = req.getFeedType();	//1:人物，2:道具
		long inputExp = req.getInputExp();
		long aeIds [] = req.getAeId();
		int aeNums[] = req.getAeNums();
		TalentData data = getTalentData(player.getId());
		if(data == null){
			data = new TalentData();
		}
		int oldLevel = data.getXylevel();
		
		if(oldLevel > player.getLevel()){
			player.sendError(Translate.仙婴等级不能大于玩家等级);
			return null;
		}
		if(oldLevel >= MAX_XIANYIN_LEVEL){
			player.sendError(Translate.仙婴已到最高等级);
			return null;
		}
		
		if(feedType == 1){
			//扣除角色经验
			//300仙婴等级限制
			if(inputExp <= 0 || inputExp > player.getExp()){
				logger.warn("[兑换飞升天赋经验] [人物经验] [错误:输入经验值不合法] [inputExp:{}] [{}]",new Object[]{inputExp,player.getLogString()});
				return null;
			}
			long canAddExp = (long)(inputExp/任务经验和仙婴经验兑换比例);	//兑换后的仙婴经验
			if(canAddExp <= 0){
				player.sendError(Translate.可兑换的仙婴为0);
				return null;
			}
			
			int canUpgradeLevel = 0;
			long overExps = canAddExp + data.getExp();
			long costAllExps = canAddExp;		//仙婴经验总消耗
			long nextNeedExps = 0;
			for(int i = oldLevel-1;i < infos.size();i++){
				TalentPageInfo info = infos.get(i);
				if(info != null){
					nextNeedExps = info.getUpgradeExp();
					if(overExps >= nextNeedExps){
						if(oldLevel + canUpgradeLevel >= player.getLevel()){
							if(overExps > info.getUpgradeExp()){
								costAllExps -= (overExps - info.getUpgradeExp());
								overExps = info.getUpgradeExp();
								break;
							}
						}else{
							canUpgradeLevel++;
							if((oldLevel + canUpgradeLevel) >= MAX_XIANYIN_LEVEL){
								
							}
							overExps -= nextNeedExps;
						}
					}else{
						break;
					}
				}
			}
			
			//仙婴等级到达上限且拥有的经验大于升级经验
			if(data.getExp() >= nextNeedExps && (oldLevel >= player.getLevel())){
				player.sendError(Translate.仙婴等级不能大于玩家等级);
				return null;
			}
			
			long playerCostExps = costAllExps * 任务经验和仙婴经验兑换比例;
			if(playerCostExps < 0){
				playerCostExps = 0;
			}
			//等级到达上限，仙婴拥有的经验不满
			if(canUpgradeLevel + oldLevel >= player.getLevel()){
				player.setExp(player.getExp() - playerCostExps);
				data.setExp(overExps);
				data.setXylevel(player.getLevel());
				try {
					AchievementManager.getInstance().record(player, RecordAction.元婴修炼等级, data.getXylevel());
				} catch (Exception e) {
					PlayerAimManager.logger.warn("[目标系统] [统计元婴修炼等级] [异常] [" + player.getLogString() + "]", e);
				}
				saveTalentData(player, data);
				if(data.getXylevel() % GET_ONE_POINT_NEED_LEVEL != 0 ||  playerCostExps > 0){
					String mess = Translate.translateString(Translate.经验兑换成功, new String[][] { { Translate.COUNT_1, String.valueOf(costAllExps)}});
					player.sendError(mess);
				}
				logger.warn("[兑换飞升天赋经验] [人物经验] [成功:升到角色限制等级] [升的级数:{}] [等级变化:{}] [输入的人物经验:{}] [兑换的仙婴经验:{}] [兑换之前兑换经验:{}] [剩余兑换经验:{}] [infos:{}] [{}]",new Object[]{canUpgradeLevel,oldLevel+"-->"+data.getXylevel(),inputExp,canAddExp,data.getExp(),overExps, infos.size(), player.getLogString()});
				CONFIRM_TALENT_EXP_RES res = new CONFIRM_TALENT_EXP_RES(req.getSequnceNum(), 1);
				return res;
			}
			
			//仙婴等级+天赋提升的等级 <=300
			if(data.getXylevelA() > 0 && canUpgradeLevel > 0){
				if((oldLevel + canUpgradeLevel + data.getXylevelA()) >= MAX_XIANYIN_LEVEL){
					int overLevel = player.getLevel() - oldLevel;
					if(canUpgradeLevel > overLevel){
						canUpgradeLevel = overLevel;
					}
					data.setXylevel(oldLevel + canUpgradeLevel);
					try {
						AchievementManager.getInstance().record(player, RecordAction.元婴修炼等级, data.getXylevel());
					} catch (Exception e) {
						PlayerAimManager.logger.warn("[目标系统] [统计元婴修炼等级] [异常] [" + player.getLogString() + "]", e);
					}
					player.setExp(player.getExp() - playerCostExps);
					data.setExp(overExps);
					saveTalentData(player, data);
					if(data.getXylevel() % GET_ONE_POINT_NEED_LEVEL != 0 ||  playerCostExps > 0){
						String mess = Translate.translateString(Translate.经验兑换成功, new String[][] { { Translate.COUNT_1, String.valueOf(costAllExps)}});
						player.sendError(mess);
					}
					logger.warn("[兑换飞升天赋经验] [人物经验] [成功:等级替换] [升的级数:{}] [替换等级:{}] [等级变化:{}] [输入的人物经验:{}] [兑换的仙婴经验:{}] [兑换之前兑换经验:{}] [剩余兑换经验:{}] [infos:{}] [{}]",new Object[]{canUpgradeLevel,overLevel,oldLevel+"-->"+data.getXylevel(),inputExp,canAddExp,data.getExp(),overExps, infos.size(), player.getLogString()});
					CONFIRM_TALENT_EXP_RES res = new CONFIRM_TALENT_EXP_RES(req.getSequnceNum(), 1);
					return res;
				}
			}
			
			player.setExp(player.getExp() - playerCostExps);
			data.setExp(overExps);
			data.setXylevel(oldLevel + canUpgradeLevel);
			saveTalentData(player, data);
			try {
				AchievementManager.getInstance().record(player, RecordAction.元婴修炼等级, data.getXylevel());
			} catch (Exception e) {
				PlayerAimManager.logger.warn("[目标系统] [统计元婴修炼等级] [异常] [" + player.getLogString() + "]", e);
			}
			if(data.getXylevel() % GET_ONE_POINT_NEED_LEVEL != 0 ||  playerCostExps > 0){
				String mess = Translate.translateString(Translate.经验兑换成功, new String[][] { { Translate.COUNT_1, String.valueOf(costAllExps)}});
				player.sendError(mess);
			}
			logger.warn("[兑换飞升天赋经验] [人物经验] [成功] [升的级数:{}] [等级变化:{}] [输入的人物经验:{}] [兑换后的仙婴经验:{}] [总消耗仙婴经验:{}] [兑换之前兑换经验:{}] [剩余兑换经验:{}] [infos:{}] [{}]",
					new Object[]{canUpgradeLevel,oldLevel+"-->"+data.getXylevel(),inputExp,canAddExp,costAllExps,data.getExp(),overExps, infos.size(), player.getLogString()});
			CONFIRM_TALENT_EXP_RES res = new CONFIRM_TALENT_EXP_RES(req.getSequnceNum(), 1);
			return res;
		}else if(feedType == 2){
			if(aeIds == null || aeIds.length == 0 || aeNums == null || aeNums.length == 0 || aeIds.length != aeNums.length){
				logger.warn("[兑换飞升天赋经验] [道具] [错误:客户端道具数据不对] [aeNums:{}] [aeId:{}] [{}]",new Object[]{(aeNums==null?"nul":aeNums.length),(aeIds==null?"nul":aeIds.length),player.getLogString()});
				return null;
			}
			
			long tryId = 0;
			for(long id : aeIds){
				if(id > 0){
					tryId = id;
					break;
				}
			}
			
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(tryId);
			if(ae == null){
				logger.warn("[兑换飞升天赋经验] [道具] [错误:道具不存在] [tryId:{}] [ids:{}] [aeNums:{}] [{}]",new Object[]{tryId,(aeIds==null?"nul":Arrays.toString(aeIds)),(aeNums==null?"nul":Arrays.toString(aeNums)),player.getLogString()});
				return null;
			}
			
			int allNeedNums = 0;
			for(int i=0;i<aeNums.length;i++){
				allNeedNums+=aeNums[i];
			}
			
			int hasNums = player.getKnapsack_common().countArticle(ae.getArticleName());
			if(hasNums < allNeedNums){
				player.sendError(Translate.translateString(Translate.您背包的材料不足, new String[][] { { Translate.STRING_1, ae.getArticleName() }, { Translate.COUNT_1, String.valueOf(allNeedNums)} }));
				return null;
			}
			
			long canAddExp = 0;
			TalentPageInfo info2 = null;
			for(TalentPageInfo pageInfo : infos){
				if(pageInfo != null && pageInfo.getLevel() == data.getXylevel()){
					info2 = pageInfo;
					break;
				}
			}
			String aeName = ae.getArticleName();
			for(int i = 0;i < aeIds.length;i++){
				if(aeIds[i] > 0 && aeNums[i] > 0){
					if(aeName.equals(Translate.完美仙婴元魄)){
						if(info2 != null){
							double calExps = Math.ceil(info2.getUpgradeExp()*0.1);
							canAddExp += (long)calExps*aeNums[i];
						}
					}else{
						Map<Integer, TalentExpProp> map = expProps.get(ae.getArticleName());
						if(map != null){
							if(map.get(new Integer(data.getXylevel())) != null){
								canAddExp += map.get(new Integer(data.getXylevel())).getExp() * aeNums[i];
							}
						}
					}
				}
			}
			
			if(canAddExp <= 0){
				player.sendError(Translate.可兑换的仙婴为0);
				return null;
			}
			
			int canUpgradeLevel = 0;
			long overExps = canAddExp + data.getExp();
			long nextNeedExps = 0;
			for(int i = oldLevel-1;i < infos.size();i++){
				TalentPageInfo info = infos.get(i);
				if(info != null){
					if((oldLevel + canUpgradeLevel) >= MAX_XIANYIN_LEVEL){
						break;
					}
					nextNeedExps = info.getUpgradeExp();
					if(overExps >= nextNeedExps){
						if(oldLevel + canUpgradeLevel >= player.getLevel()){
							if(overExps > info.getUpgradeExp()){
								overExps = info.getUpgradeExp();
								break;
							}
						}else{
							canUpgradeLevel++;
							overExps -= nextNeedExps;
						}
					}else{
						break;
					}
				}
			}
			
			if(data.getExp() >= nextNeedExps && (oldLevel >= player.getLevel())){
				player.sendError(Translate.仙婴等级不能大于玩家等级);
				return null;
			}
			
			long allCostExps = 0;	//可以消耗的最大仙婴经验
			for(int i = oldLevel-1;i < infos.size();i++){
				TalentPageInfo info = infos.get(i);
				if(info != null){
					if(info.getLevel() <= player.getLevel()){
						allCostExps += info.getUpgradeExp();
					}
				}
			}
			
			if(allCostExps > 0){
				allCostExps -= data.getExp();
			}
			
			long removeAllExps = 0;	//删除经验
			end:for(int i=0;i<aeIds.length;i++){
				if(aeIds[i] > 0){
					int delNums = aeNums[i];
					while(delNums > 0){
						if(removeAllExps >= allCostExps){
							break end;
						}
						ArticleEntity aee = player.removeArticleEntityFromKnapsackByArticleId(aeIds[i], "兑换天赋经验", true);
						if(aee == null){
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.删除物品失败);
							player.addMessageToRightBag(hreq);
							logger.warn("[兑换飞升天赋经验] [道具] [失败:删除物品失败] [aeId:{}] [正在删除第:{}个] [总个数:{}] [{}]",new Object[]{aeIds[i],i,(aeNums==null?"nul":Arrays.toString(aeNums)),player.getLogString()});
							return null;
						}
						
						if(aee.getArticleName().equals(Translate.完美仙婴元魄)){
							if(info2 != null){
								removeAllExps += (long)(info2.getUpgradeExp()*0.1);
							}
						}else{
							Map<Integer, TalentExpProp> map = expProps.get(ae.getArticleName());
							if(map != null){
								if(map.get(new Integer(data.getXylevel())) != null){
									removeAllExps += map.get(new Integer(data.getXylevel())).getExp();
								}
							}
						}
						
						logger.warn("[兑换飞升天赋经验] [道具] [删除道具] [成功] [物品:{}] [aeId:{}] [正在删除第:{}个] [总个数:{}] [allCostExps:{}] [removeAllExps:{}] [{}]",new Object[]{aee.getArticleName(), aeIds[i],i,(aeNums==null?"nul":Arrays.toString(aeNums)),allCostExps,removeAllExps,player.getLogString()});
						ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "兑换天赋经验", null);
						delNums--;
					}
				}
			}
			
			String mess = Translate.translateString(Translate.经验兑换成功, new String[][] { { Translate.COUNT_1, String.valueOf(canAddExp)}});
			if(canUpgradeLevel + oldLevel >= player.getLevel()){
				data.setExp(overExps);
				data.setXylevel(player.getLevel());
				try {
					AchievementManager.getInstance().record(player, RecordAction.元婴修炼等级, data.getXylevel());
				} catch (Exception e) {
					PlayerAimManager.logger.warn("[目标系统] [统计元婴修炼等级] [异常] [" + player.getLogString() + "]", e);
				}
				saveTalentData(player, data);
				player.sendError(mess);
				logger.warn("[兑换飞升天赋经验] [道具] [成功:升到角色限制等级] [升的级数:{}] [等级变化:{}] [输入的人物经验:{}] [兑换的仙婴经验:{}] [兑换之前兑换经验:{}] [剩余兑换经验:{}] [infos:{}] [{}]",new Object[]{canUpgradeLevel,oldLevel+"-->"+data.getXylevel(),inputExp,canAddExp,data.getExp(),overExps, infos.size(), player.getLogString()});
			}else{
				data.setExp(overExps);
				data.setXylevel(data.getXylevel() + canUpgradeLevel);
				try {
					AchievementManager.getInstance().record(player, RecordAction.元婴修炼等级, data.getXylevel());
				} catch (Exception e) {
					PlayerAimManager.logger.warn("[目标系统] [统计元婴修炼等级] [异常] [" + player.getLogString() + "]", e);
				}
				saveTalentData(player, data);
				player.sendError(mess);
				logger.warn("[兑换飞升天赋经验] [道具] [成功] [升的级数:{}] [等级变化:{}] [道具兑换经验:{}] [个数:{}] [ids:{}] [nums:{}] [infos:{}] [{}]",new Object[]{canUpgradeLevel,oldLevel+"-->"+data.getXylevel(),canAddExp,aeNums,(aeIds==null?"nul":Arrays.toString(aeIds)),(aeNums==null?"nul":Arrays.toString(aeNums)),infos.size(), player.getLogString()});
			}
			
			CONFIRM_TALENT_EXP_RES res = new CONFIRM_TALENT_EXP_RES(req.getSequnceNum(), 1);
			return res;
		}
		return null;
	}
	
	public QUERY_TALENT_EXP_RES handle_QUERY_TALENT_EXP_REQ(RequestMessage message, Player player){
		QUERY_TALENT_EXP_REQ req = (QUERY_TALENT_EXP_REQ)message;
		int feedType = req.getFeedType();	//1:人物，2:道具
		long inputExp = req.getInputExp();
		long aeId[] = req.getAeId();
		int aeNums[] = req.getAeNums();
		
		TalentData data = getTalentData(player.getId());
		if(data == null){
			data = new TalentData();
		}
		
		long upgradeExps = 0;
		for(TalentPageInfo pageInfo : infos){
			if(pageInfo != null && (pageInfo.getLevel() == data.getXylevel() )){
				upgradeExps = pageInfo.getUpgradeExp();
			}
		}
		
		if(feedType == 1){
			if(inputExp > player.getExp()){
				logger.warn("[请求飞升天赋经验] [人物经验] [错误:输入经验值不合法] [inputExp:{}] [玩家经验:{}] [{}]",new Object[]{inputExp,player.getExp(), player.getLogString()});
				return null;
			}
			
			long canAddExp = (long)(inputExp/任务经验和仙婴经验兑换比例);	//兑换后的仙婴经验
			
			QUERY_TALENT_EXP_RES res = new QUERY_TALENT_EXP_RES(req.getSequnceNum(), canAddExp,upgradeExps-data.getExp());
			logger.warn("[请求飞升天赋经验] [人物经验] [成功] [输入的经验:{}] [转换后的经验:{}] [{}]",new Object[]{inputExp,canAddExp,player.getLogString()});
			return res;
		}else if(feedType == 2){
			if(aeId == null || aeId.length == 0 || aeNums == null || aeNums.length == 0 || aeId.length != aeNums.length){
				logger.warn("[请求飞升天赋经验] [道具] [错误:客户端道具数据不对] [aeNums:{}] [aeId:{}] [{}]",new Object[]{(aeNums==null?"nul":aeNums.length),(aeId==null?"nul":aeId.length),player.getLogString()});
				return null;
			}
			long allGetExps = 0;
			String aeName = "";
			for(int i = 0;i < aeId.length;i++){
				if(aeId[i] > 0){
					ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(aeId[i]);
					if(ae == null){
						logger.warn("[请求飞升天赋经验] [道具] [错误:道具不存在] [道具id:{}] [aeids:{}] [aeNums:{}] [{}]",new Object[]{aeId[i],(aeId==null?"nul":Arrays.toString(aeId)),(aeNums==null?"nul":Arrays.toString(aeNums)), player.getLogString()});
						return null;
					}
					aeName = ae.getArticleName();
					if(aeName.equals(Translate.完美仙婴元魄)){
						TalentPageInfo info = null;
						for(TalentPageInfo pageInfo : infos){
							if(pageInfo != null && pageInfo.getLevel() == data.getXylevel()){
								info = pageInfo;
								break;
							}
						}
						if(info != null){
							double costExps = Math.ceil(info.getUpgradeExp()*0.1);
							allGetExps += (long)costExps*aeNums[i];
						}
					}else{
						long canAddExp = 1;		//默认1点
						Map<Integer, TalentExpProp> map = expProps.get(ae.getArticleName());
						if(map != null){
							if(map.get(new Integer(data.getXylevel())) != null){
								canAddExp = map.get(new Integer(data.getXylevel())).getExp();
							}
						}
						allGetExps += canAddExp*aeNums[i];
					}
				}
			}
			
			QUERY_TALENT_EXP_RES res = new QUERY_TALENT_EXP_RES(req.getSequnceNum(), allGetExps,upgradeExps-data.getExp());
			logger.warn("[请求飞升天赋经验] [道具:{}] [成功] [aeids:{}] [aeNums:{}] [allGetExps:{}] [升级所需经验:{}] [{}]",new Object[]{aeName,(aeId==null?"nul":Arrays.toString(aeId)),(aeNums==null?"nul":Arrays.toString(aeNums)),allGetExps,(upgradeExps-data.getExp()),player.getLogString()});
			return res;
		}
		return null;
	}
	
	public XIULIAN_TALENT_EXP_RES handle_XIULIAN_TALENT_EXP_REQ(RequestMessage message, Player player){
		long now = System.currentTimeMillis();
		TalentData data = getTalentData(player.getId());
		if(data == null){
			data = new TalentData();
		}
		
		Long conPoints = xiuLianExps.get(new Long(data.getXylevel()));
		if(conPoints == null || conPoints.longValue() <= 0){
			player.sendError(Translate.赛季奖励配置错误);
			return null;
		}
		
		if(isSameDay(data.getLastXLTime(), now)){
			player.sendError(Translate.今天已经修炼过了);
			return null;
		}
		if(data.getXylevel() >= MAX_XIANYIN_LEVEL){
			player.sendError(Translate.仙婴已到最高等级);
			return null;
		}
		
		if(data.getXylevel() > player.getLevel()){
			player.sendError(Translate.仙婴等级不能大于玩家等级);
			return null;
		}
		
		Long addExpObj = xiuLianExps.get(new Long(data.getXylevel()));
		long addExp = addExpObj==null?1:addExpObj.longValue();	
		long allExp = addExp + data.getExp();
		long overExps = allExp;	//剩余经验 
		int addLevel = 0;	//可升级数
		int realLevel = data.getXylevel();
		long nextNeedExps = 0;
		int maxLevel = player.getLevel() + data.getXylevelA();	//仙婴能达到的最大等级
		for(int i = realLevel - 1;i < infos.size();i++){
			TalentPageInfo info = infos.get(i);
			if(info != null){
				if(info.getLevel() == data.getXylevel()+addLevel){
					nextNeedExps = info.getUpgradeExp();
				}
			}
			if(overExps >= nextNeedExps){
				if(addLevel + data.getXylevel() >= player.getLevel()){
					if(overExps > info.getUpgradeExp()){
						overExps = info.getUpgradeExp();
					}
				}else{
					addLevel++;
					overExps = overExps - nextNeedExps;
				}
			}else{
				break;
			}
		}
		
		if(overExps < 0){
			overExps = 0;
		}
		
		if(data.getExp() >= nextNeedExps && (data.getXylevel() >= player.getLevel())){
			player.sendError(Translate.仙婴等级不能大于玩家等级);
			return null;
		}
		
		if(data.getXylevelA() > 0 && addLevel > 0){
			if((realLevel + addLevel + data.getXylevelA()) >= MAX_XIANYIN_LEVEL){
				int overLevel = player.getLevel() - realLevel;
				if(addLevel > overLevel){
					addLevel = overLevel;
				}
				data.setXylevel(realLevel + addLevel);
				try {
					AchievementManager.getInstance().record(player, RecordAction.元婴修炼等级, data.getXylevel());
				} catch (Exception e) {
					PlayerAimManager.logger.warn("[目标系统] [统计元婴修炼等级] [异常] [" + player.getLogString() + "]", e);
				}
				data.setExp(overExps);
				saveTalentData(player, data);
				if(data.getXylevel() % GET_ONE_POINT_NEED_LEVEL != 0){
					String mess = Translate.translateString(Translate.经验兑换成功, new String[][] { { Translate.COUNT_1, String.valueOf(addExp)}});
					player.sendError(mess);
				}
				logger.warn("[仙婴修炼] [成功:等级替换] [升的级数:{}] [替换等级:{}] [等级变化:{}] [兑换的仙婴经验:{}] [兑换之前兑换经验:{}] [剩余兑换经验:{}] [infos:{}] [{}]",new Object[]{addLevel,overLevel,realLevel+"-->"+data.getXylevel(),addExp,data.getExp(),overExps, infos.size(), player.getLogString()});
				return new XIULIAN_TALENT_EXP_RES(message.getSequnceNum(), 1);
			}
		}
		
		String mess = Translate.translateString(Translate.经验兑换成功, new String[][] { { Translate.COUNT_1, String.valueOf(addExp)}});
		if(addLevel + data.getXylevel() >= maxLevel){
			data.setXylevel(maxLevel);
			try {
				AchievementManager.getInstance().record(player, RecordAction.元婴修炼等级, data.getXylevel());
			} catch (Exception e) {
				PlayerAimManager.logger.warn("[目标系统] [统计元婴修炼等级] [异常] [" + player.getLogString() + "]", e);
			}
		}else{
			data.setXylevel(addLevel + data.getXylevel());
			try {
				AchievementManager.getInstance().record(player, RecordAction.元婴修炼等级, data.getXylevel());
			} catch (Exception e) {
				PlayerAimManager.logger.warn("[目标系统] [统计元婴修炼等级] [异常] [" + player.getLogString() + "]", e);
			}
		}
		
		data.setExp(overExps);
		data.setLastXLTime(now);
		saveTalentData(player, data);
		player.sendError(mess);
		logger.warn("[仙婴修炼] [成功] [剩余经验:{}] [当前等级:{}] [增加的等级:{}] [{}]",new Object[]{overExps,data.getXylevel(),addLevel,player.getLogString()});
		return new XIULIAN_TALENT_EXP_RES(message.getSequnceNum(), 1);
	}

	public TALENT_FIRST_PAGE_RES handle_TALENT_FIRST_PAGE_REQ(RequestMessage message, Player player){
		TALENT_FIRST_PAGE_REQ req = (TALENT_FIRST_PAGE_REQ)message;
		long pid = req.getPlayerId();
		TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(pid);
		if (entity == null || entity.getFeisheng() != 1) { 
			if(pid == player.getId()){
				player.sendError(Translate.飞升后才能仙婴);
			}else{
				player.sendError(Translate.查看的玩家飞升后才能仙婴);
			}
			return null;
		}
		TalentData data = getTalentData(pid);
		int xyLevel = 1;
		String helpMess = 首界面帮助信息;
		String talentAddMess = Translate.仙婴还未加点;
		
		long currExp = 0; 
		int levelA = 0;
		if(data == null){
			player.sendError(Translate.需飞升后完成赐予仙婴任务才可开启本系统);
			return null;
		}
		int 仙悟等级 = 0;
		if(data != null){
			if(data.getXyButtonState() != 1){
				player.sendError(Translate.需飞升后完成赐予仙婴任务才可开启本系统);
				return null;
			}
			levelA = data.getXylevelA();
			currExp = data.getExp();
			xyLevel = data.getXylevel();
			HashMap<Integer, Integer> points = data.getPoints();
			if(points != null && points.size() > 0){
				StringBuffer sbuff = new StringBuffer();
				Iterator<Entry<Integer, Integer>>  it = points.entrySet().iterator();
				while(it.hasNext()){
					Entry<Integer, Integer> entry = it.next();
					if(entry != null){
						Integer id = entry.getKey();
						Integer point = entry.getValue();
						if(point != null && point > 0 && id != null){
							if(id != null && id == 仙悟){
								仙悟等级 = point;
							}
							
							TalentSkillInfo sinfo = skillInfos.get(id);
							if(sinfo == null){
								logger.warn("[请求飞升天赋首界面] [出错：天赋技能信息不存在,请检查flyTalendData表] [仙婴等级:{}] [天赋id:{}] [天赋加点:{}] [{}]",new Object[]{xyLevel,id,point,player.getLogString()});
								continue;
							}
							double levelValues[] = sinfo.getLevelValues();
							if(levelValues != null){
								try{
									switch (id) {
									case 仙疗:	//吃药恢复效果增加10%/20%/30%
									case 领悟:	//提升10/20/30级仙婴等级 （最多提升至300级）
									case 灵巧:	//增加1%/4%/7%移动速度
									case 闪避:	//增加1%/2%/3%闪躲
									case 体质:	//增加50000/100000/150000气血上限
									case 幸运:	//增加1%/2%/3%免爆
									case 神赐:	//增加20000/40000/60000双防
									case 猛兽:	//宠物每次攻击时可额外造成20000/40000/60000伤害
									case 怒火:	//增加1%/2%/3%暴击
									case 神力:	//增加20000/40000/60000双攻
									case 碎甲:	//增加1%/2%/3%破甲
									case 专注:	//增加1%/5%/10%控制类技能持续时间
									case 仙悟:	//增加2%/4%/6%仙婴全属性
									case 暴燃:	//所有技能造成伤害增加0.5%/1%/2%
									case 宝石神悟:	//提高自身宝石收益1%/2%/3%
									case 羽翼神悟:	//翅膀基础属性收益提高0.5%/1%/1.5%
										if(point > levelValues.length){
											point = levelValues.length;
										}
										if(sinfo.getPropType() == 1){
											sbuff.append(String.format(sinfo.getMess3(), levelValues[point-1]+"%").replace(".0", "")).append("\n");
										}else{
											sbuff.append(String.format(sinfo.getMess3(), levelValues[point-1]).replace(".0", "")).append("\n");
										}
										break;
									case 强化神元:	//增加3%气血
									case 强化神赐:	//增加3%双防
									case 仙婴体质:	//每2秒回复3%的气血
									case 仙婴固化:	//额外获得15%减伤效果
									case 强化猛兽:	//增加3%宠物伤害
									case 强化神力:	//增加3%双攻
									case 仙婴兽化:	//宠物攻击时，有几率额外造成30%伤害
									case 仙婴悟攻:	//人物进行攻击时，有几率额外造成20%伤害
										if(point > levelValues.length){
											point = levelValues.length;
										}
										sbuff.append(String.format(sinfo.getMess3(), levelValues[point-1]+"%").replace(".0", "")).append("\n");
										break;
									case 会心:	//降低0.5%/1%/1.5%双防，提高宠物1%/2%/3%伤害
									case 破斧:	//降低0.5%/1%/1.5%气血，提高1%/2%/3%自身双攻
									case 温和:	//降低0.5%/1%/1.5%双攻，提高1%/2%/3%气血上限
									case 坚固:	//降低0.5%/1%/1.5%双攻，提高1%/2%/3%双防
										if(point == 3){
											sbuff.append(String.format(sinfo.getMess3(), "1.5%","3%")).append("\n");
										}else if(point == 2){
											sbuff.append(String.format(sinfo.getMess3(), "1%","2%")).append("\n");
										}else if(point == 1){
											sbuff.append(String.format(sinfo.getMess3(), "0.5%","1%")).append("\n");
										}
										break;
									default:
										break;
									}
								}catch(Exception e){
									continue;
								}
							}
						}
					}
				}
				if(sbuff.length() > 0){
					talentAddMess = sbuff.toString();
				}
			}
		}
		
		TalentPageInfo info = null;
		long upgradeExps = 0;
		for(TalentPageInfo pageInfo : infos){
			if(xyLevel + levelA > 300){
				if(pageInfo != null && (pageInfo.getLevel() == 300 )){
					info = pageInfo;
				}
			}else{
				if(pageInfo != null && (pageInfo.getLevel() == xyLevel + levelA )){
					info = pageInfo;
				}
			}
			if(pageInfo != null && (pageInfo.getLevel() == xyLevel )){
				upgradeExps = pageInfo.getUpgradeExp();
			}
		}
		if(info == null){
			player.sendError(Translate.赛季奖励配置错误);
			return null;
		}
		
		String levelMess = 1+"";
		long minuseTime = 0;
		int fullLevel = 0;
		if(data != null){
			minuseTime = data.getMinusCDTimes();
			levelMess = data.getXylevel()+"";
			if(data.getPoints().get(new Integer(领悟)) != null && (data.getPoints().get(new Integer(领悟)))> 0){
				if(data.getXylevel() == MAX_XIANYIN_LEVEL){
					levelMess = data.getXylevel()+"";
					String mess = Translate.translateString(Translate.天赋提升等级, new String[][] { { Translate.COUNT_1, String.valueOf(0)}});
					levelMess += mess;
				}
			}
			if(data.getXylevelA() > 0){
				levelMess = data.getXylevel()+"";
				String mess = Translate.translateString(Translate.天赋提升等级, new String[][] { { Translate.COUNT_1, String.valueOf(data.getXylevelA())}});
				levelMess += mess;
			}
			if(data.getXylevel() == MAX_XIANYIN_LEVEL){
				fullLevel = 1;
			}
		}
		String names [] = expProps.keySet().toArray(new String[]{});
		String newNames [] = new String[names.length+1];
		System.arraycopy(names, 0, newNames, 0, names.length);
		newNames[newNames.length-1] = Translate.完美仙婴元魄;
		
		long cdTimes = TALENT_SKILL_CD_TIME-minuseTime;
		String cdMess = (cdTimes / 60000)+Translate.分钟;
		if(cdTimes < 60*1000){
			cdMess = Translate.小于1分钟;
		}
		
		int phyAttact = info.getPhyAttact();
		int phyDefence = info.getPhyDefence();
		int magicAttact = info.getMagicAttact();
		int magicDefence = info.getMagicDefence();
		int hp = info.getHp();
		if(仙悟等级 == 1){
			phyAttact = (int)(info.getPhyAttact() * 1.02);
			phyDefence = (int)(info.getPhyDefence() * 1.02);
			magicAttact = (int)(info.getMagicAttact() * 1.02);
			magicDefence = (int)(info.getMagicDefence() * 1.02);
			hp = (int)(info.getHp() * 1.02);
		}else if(仙悟等级 == 2){
			phyAttact = (int)(info.getPhyAttact() * 1.04);
			phyDefence = (int)(info.getPhyDefence() * 1.04);
			magicAttact = (int)(info.getMagicAttact() * 1.04);
			magicDefence = (int)(info.getMagicDefence() * 1.04);
			hp = (int)(info.getHp() * 1.04);
		}else if(仙悟等级 == 3){
			phyAttact = (int)(info.getPhyAttact() * 1.06);
			phyDefence = (int)(info.getPhyDefence() * 1.06);
			magicAttact = (int)(info.getMagicAttact() * 1.06);
			magicDefence = (int)(info.getMagicDefence() * 1.06);
			hp = (int)(info.getHp() * 1.06);
		}
		
		TALENT_FIRST_PAGE_RES res = new TALENT_FIRST_PAGE_RES(req.getSequnceNum(),fullLevel,pid, helpMess, levelMess, phyAttact, phyDefence, magicAttact, 
				magicDefence, hp, talentAddMess, currExp, upgradeExps,newNames,cdMess);
		logger.warn("[请求飞升天赋首界面] [pid:{}] [仙婴等级:{}] [仙悟等级:{}] [可兑换经验道具:{}] [当前拥有经验:{}] [升级所需经验:{}] [{}] [{}]",new Object[]{pid,xyLevel,仙悟等级,Arrays.toString(newNames), currExp,upgradeExps,info,player.getLogString()});
		return res;
	}
	
	public void handle_附体期间穿脱翅膀(Player player,int calType){
		if(player.flyState != 1){
			return;
		}
		TalentData data = getTalentData(player.getId());
		if(data == null || data.getPoints() == null){
			return;
		}
		Integer point = data.getPoints().get(宝石神悟);
		if(point != null && point.intValue() > 0){
			int inlayNums = 0;
			int allInlayProppertyValues [] = new int[InlayArticle.propertysValuesStrings.length];
			
			int newAllInlayProppertyValues [] = new int[InlayArticle.propertysValuesStrings.length];
			for(int i=0;i<newAllInlayProppertyValues.length;i++){
				if(allInlayProppertyValues[i] > 0){
					if(point == 1){
						newAllInlayProppertyValues[i] = (int)(allInlayProppertyValues[i] * 0.01);
					}else if(point == 2){
						newAllInlayProppertyValues[i] = (int)(allInlayProppertyValues[i] * 0.02);
					}else if(point == 3){
						newAllInlayProppertyValues[i] = (int)(allInlayProppertyValues[i] * 0.03);
						
					}
				}
			}
			logger.warn("[宝石神悟] [{}] [计算总属性] [完毕] [总宝石数:{}] [总属性:{}] [附体后额外的属性:{}] [{}]",new Object[]{calType==1?"穿":"脱",inlayNums,Arrays.toString(allInlayProppertyValues),Arrays.toString(newAllInlayProppertyValues), player.getName()});
			if(calType == 1){
				addPropertyValue(player, newAllInlayProppertyValues);
			}else if(calType == 2){
				removePropertyValue(player, newAllInlayProppertyValues);
			}
		}
		
	}
	
	/**
	 * 
	 * @param player
	 * @param ee
	 * @param calType	1:穿，2：脱
	 */
	public void handle_附体期间穿脱装备(Player player,EquipmentEntity ee,int calType){
		if(player.flyState != 1){
			return;
		}
		
		TalentData data = getTalentData(player.getId());
		if(data == null || data.getPoints() == null){
			return;
		}
		
		Integer point = data.getPoints().get(宝石神悟);
		if(point == null || point.intValue() <= 0){
			return;
		}
		
		int inlayNums = 0;
		int xiaziNums = 0;
		int allInlayProppertyValues [] = new int[InlayArticle.propertysValuesStrings.length];
		long[] inlays = ee.getInlayArticleIds();
		if(inlays != null && inlays.length > 0){
			for(long bid : inlays){
				if(bid <= 0){
					continue;
				}
				ArticleEntity bae = ArticleEntityManager.getInstance().getEntity(bid);
				if(bae != null){
					Article ba = ArticleManager.getInstance().getArticle(bae.getArticleName());
					if(ba != null && ba instanceof InlayArticle){
						InlayArticle inlay = (InlayArticle)ba;
						if(inlay.getInlayType() > 1){
							BaoShiXiaZiData shenXia = ArticleManager.getInstance().getxiLianData(player,bid);
							if(shenXia != null){
								xiaziNums++;
								String names[] = shenXia.getNames();
								if(names != null && names.length > 0){
									for(String name : names){
										if(name != null && !name.isEmpty()){
											Article baoshi = ArticleManager.getInstance().getArticle(name);
											if(baoshi != null && baoshi instanceof InlayArticle){
												inlayNums++;
												int[] props = ((InlayArticle)baoshi).getPropertysValues();
												if(props != null){
													for(int j=0;j<props.length;j++){
														if(props[j] > 0){
															allInlayProppertyValues[j] += props[j];
														}
													}
													logger.warn("[宝石神悟穿脱装备] [{}] [计算宝石匣子属性] [装备：{}] [匣子里的宝石:{}] [匣子id:{}] [宝石数:{}] [宝石:{}] [宝石属性:{}] [总属性:{}] [{}]",new Object[]{calType==1?"穿装备":"脱装备",ee.getArticleName(),Arrays.toString(names), bid,inlays.length,inlay.getName(),Arrays.toString(props),Arrays.toString(allInlayProppertyValues),player.getName()});
												}
											}
										}
									}
								}
							}
							
						}else{
							inlayNums++;
							int props[] = inlay.getPropertysValues();
							if(props != null){
								for(int j=0;j<props.length;j++){
									if(props[j] > 0){
										allInlayProppertyValues[j] += props[j];
									}
								}
								logger.warn("[宝石神悟穿脱装备] [{}] [计算宝石属性] [装备：{}] [宝石数:{}] [宝石:{}] [宝石属性:{}] [总属性:{}] [{}]",new Object[]{calType==1?"穿装备":"脱装备",ee.getArticleName(),inlays.length,inlay.getName(),Arrays.toString(props),Arrays.toString(allInlayProppertyValues),player.getName()});
							}
						}
					}
				}
			}
		}
		int newAllInlayProppertyValues [] = new int[InlayArticle.propertysValuesStrings.length];
		for(int i=0;i<newAllInlayProppertyValues.length;i++){
			if(allInlayProppertyValues[i] > 0){
				if(point == 1){
					newAllInlayProppertyValues[i] = (int)(allInlayProppertyValues[i] * 0.01);
				}else if(point == 2){
					newAllInlayProppertyValues[i] = (int)(allInlayProppertyValues[i] * 0.02);
				}else if(point == 3){
					newAllInlayProppertyValues[i] = (int)(allInlayProppertyValues[i] * 0.03);
					
				}
			}
		}
		logger.warn("[宝石神悟穿脱装备] [{}] [计算总属性] [完毕] [总宝石数:{}] [总的匣子数:{}] [总属性:{}] [附体后额外的属性:{}] [{}]",new Object[]{calType==1?"穿装备":"脱装备", inlayNums,xiaziNums,Arrays.toString(allInlayProppertyValues),Arrays.toString(newAllInlayProppertyValues), player.getName()});
		if(calType == 1){
			addPropertyValue(player, newAllInlayProppertyValues);
		}else if(calType == 2){
			removePropertyValue(player, newAllInlayProppertyValues);
		}
		
	}
	
	public int handle_仙疗(Player player,int value){
		int result = 0;
		TalentData data = getTalentData(player.getId());
		if(player.flyState == 1 && data != null && data.getPoints() != null){
			Integer p = data.getPoints().get(new Integer(仙疗));
			if(p != null && p > 0){
				if(p == 1){
					result = (int)(value * 0.1);
				}else if(p == 2){
					result = (int)(value * 0.2);
				}else if(p == 3){
					result = (int)(value * 0.3);
				}
				logger.info("[仙疗] [增加吃药恢复百分比] [value:"+value+"] [result:"+result+"] [玩家:"+player.getName()+"]");
			}
		}
		return result;
	}
	
	public void addBasicProp(Player player,double basicPropertyValue[]) {
		double[] values = basicPropertyValue;
		if (player != null && values != null) {
			for (int i = 0; i < values.length; i++) {
				double value = values[i];
				if (value != 0) {
					switch (i) {
					case 0:
						player.setMaxHPC(player.getMaxHPC() + value);
						break;
					case 1:
						player.setPhyAttackC(player.getPhyAttackC() + value);
						break;
					case 2:
						player.setMagicAttackC(player.getMagicAttackC() + value);
						break;
					case 3:
						player.setPhyDefenceC(player.getPhyDefenceC() + value);
						break;
					case 4:
						player.setMagicDefenceC(player.getMagicDefenceC() + value);
						break;
					default:
						break;
					}
				}
			}
		}
	}
	
	public void delBasicProp(Player player,double basicPropertyValue[]) {
		double[] values = basicPropertyValue;
		if (player != null && values != null) {
			for (int i = 0; i < values.length; i++) {
				double value =  values[i];
				if (value != 0) {
					switch (i) {
					case 0:
						double maxhpc = player.getMaxHPC() - value;
						if(maxhpc < 0){
							maxhpc = 0;
						}
						player.setMaxHPC(maxhpc);
						break;
					case 1:
						double phyac = player.getPhyAttackC() - value;
						if(phyac < 0){
							phyac = 0;
						}
						player.setPhyAttackC(phyac);
						break;
					case 2:
						double magac = player.getMagicAttackC() - value;
						if(magac < 0){
							magac = 0;
						}
						player.setMagicAttackC(magac);
						break;
					case 3:
						double phydc = player.getPhyDefenceC() - value;
						if(phydc < 0){
							phydc = 0;
						}
						player.setPhyDefenceC(phydc);
						break;
					case 4:
						double magdc = player.getMagicDefenceC() - value;
						if(magdc < 0){
							magdc = 0;
						}
						player.setMagicDefenceC(magdc);
						break;
					default:
						break;
					}
				}
			}
		}
	}
	
	public void addPropertyValue(Player player , int propertysValues[]){
		int[] values = propertysValues;
		if (player != null && values != null) {
			for (int i = 0; i < values.length; i++) {
				int value = values[i];
				if (value != 0) {
					switch (i) {
					case 0:
						player.setMaxHPB(player.getMaxHPB() + value);
						break;
					case 1:
						player.setPhyAttackB(player.getPhyAttackB() + value);
						break;
					case 2:
						player.setMagicAttackB(player.getMagicAttackB() + value);
						break;
					case 3:
						player.setPhyDefenceB(player.getPhyDefenceB() + value);
						break;
					case 4:
						player.setMagicDefenceB(player.getMagicDefenceB() + value);
						break;
					case 5:
						player.setDodgeB(player.getDodgeB() + value);
						break;
					case 6:
						player.setCriticalDefenceB(player.getCriticalDefenceB() + value);
						break;
					case 7:
						player.setHitB(player.getHitB() + value);
						break;
					case 8:
						player.setCriticalHitB(player.getCriticalHitB() + value);
						break;
					case 9:
						player.setAccurateB(player.getAccurateB() + value);
						break;
					case 10:
						player.setBreakDefenceB(player.getBreakDefenceB() + value);
						break;
					case 11:
						player.setFireAttackB(player.getFireAttackB() + value);
						break;
					case 12:
						player.setFireDefenceB(player.getFireDefenceB() + value);
						break;
					case 13:
						player.setFireIgnoreDefenceB(player.getFireIgnoreDefenceB() + value);
						break;
					case 14:
						player.setBlizzardAttackB(player.getBlizzardAttackB() + value);
						break;
					case 15:
						player.setBlizzardDefenceB(player.getBlizzardDefenceB() + value);
						break;
					case 16:
						player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() + value);
						break;
					case 17:
						player.setWindAttackB(player.getWindAttackB() + value);
						break;
					case 18:
						player.setWindDefenceB(player.getWindDefenceB() + value);
						break;
					case 19:
						player.setWindIgnoreDefenceB(player.getWindIgnoreDefenceB() + value);
						break;
					case 20:
						player.setThunderAttackB(player.getThunderAttackB() + value);
						break;
					case 21:
						player.setThunderDefenceB(player.getThunderDefenceB() + value);
						break;
					case 22:
						player.setThunderIgnoreDefenceB(player.getThunderIgnoreDefenceB() + value);
						break;
					}
				}
			}
		}
	}
	
	public void removePropertyValue(Player player,int propertysValues[]){
		int[] values = propertysValues;
		if (player != null && values != null) {
			for (int i = 0; i < values.length; i++) {
				int value = values[i];
				if (value != 0) {
					switch (i) {
					case 0:
						player.setMaxHPB(player.getMaxHPB() - value);
						break;
					case 1:
						player.setPhyAttackB(player.getPhyAttackB() - value);
						break;
					case 2:
						player.setMagicAttackB(player.getMagicAttackB() - value);
						break;
					case 3:
						player.setPhyDefenceB(player.getPhyDefenceB() - value);
						break;
					case 4:
						player.setMagicDefenceB(player.getMagicDefenceB() - value);
						break;
					case 5:
						player.setDodgeB(player.getDodgeB() - value);
						break;
					case 6:
						player.setCriticalDefenceB(player.getCriticalDefenceB() - value);
						break;
					case 7:
						player.setHitB(player.getHitB() - value);
						break;
					case 8:
						player.setCriticalHitB(player.getCriticalHitB() - value);
						break;
					case 9:
						player.setAccurateB(player.getAccurateB() - value);
						break;
					case 10:
						player.setBreakDefenceB(player.getBreakDefenceB() - value);
						break;
					case 11:
						player.setFireAttackB(player.getFireAttackB() - value);
						break;
					case 12:
						player.setFireDefenceB(player.getFireDefenceB() - value);
						break;
					case 13:
						player.setFireIgnoreDefenceB(player.getFireIgnoreDefenceB() - value);
						break;
					case 14:
						player.setBlizzardAttackB(player.getBlizzardAttackB() - value);
						break;
					case 15:
						player.setBlizzardDefenceB(player.getBlizzardDefenceB() - value);
						break;
					case 16:
						player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() - value);
						break;
					case 17:
						player.setWindAttackB(player.getWindAttackB() - value);
						break;
					case 18:
						player.setWindDefenceB(player.getWindDefenceB() - value);
						break;
					case 19:
						player.setWindIgnoreDefenceB(player.getWindIgnoreDefenceB() - value);
						break;
					case 20:
						player.setThunderAttackB(player.getThunderAttackB() - value);
						break;
					case 21:
						player.setThunderDefenceB(player.getThunderDefenceB() - value);
						break;
					case 22:
						player.setThunderIgnoreDefenceB(player.getThunderIgnoreDefenceB() - value);
						break;
					}
				}
			}
		}
	}
	
	public static int[] stringToInt(String[] a) throws Exception{
		int [] fs = new int[a.length];
		for(int k=0;k<a.length;k++){
			if(a[k]!=null && a[k].trim().length()>0){
				String newValue = a[k];
				if(newValue.contains(".0")){
					newValue = newValue.replace(".0", "");
				}
				fs[k] = Integer.parseInt(newValue);
			}
		}
		return fs;
	}
	
	public static double[] stringToDouble(String[] a) throws Exception{
		double [] fs = new double[a.length];
		for(int k=0;k<a.length;k++){
			if(a[k]!=null && a[k].trim().length()>0){
				String newValue = a[k];
				if(newValue.contains(".0")){
					newValue = newValue.replace(".0", "");
				}
				fs[k] = Double.parseDouble(newValue);
			}
		}
		return fs;
	}
	
	public static boolean isSameDay(long time1,long time2){
		Calendar ca=Calendar.getInstance();
		ca.setTimeInMillis(time1);
		int year1=ca.get(Calendar.YEAR);
		int month1=ca.get(Calendar.MONTH);
		int day1=ca.get(Calendar.DAY_OF_MONTH);
//		int hour1=ca.get(Calendar.HOUR);

		ca.setTimeInMillis(time2);
		int year2=ca.get(Calendar.YEAR);
		int month2=ca.get(Calendar.MONTH);
		int day2=ca.get(Calendar.DAY_OF_MONTH);
//		int hour2=ca.get(Calendar.HOUR);
		return year1==year2&&month1==month2&&day1==day2;//&&hour1==hour2;//&&minute1==minute2;///
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public String[] getInterestedMessageTypes() {
		return new String[]{"TALENT_FIRST_PAGE_REQ","QUERY_TALENT_EXP_REQ","CONFIRM_TALENT_EXP_REQ","XIULIAN_TALENT_EXP_REQ","QUERY_TALENT_SKILLS_REQ","FLY_TALENT_ADD_POINTS_REQ",
				"CONFIRM_FLY_TALENT_ADD_POINTS_REQ","ONCLICK_TALENT_BUTTON_REQ"};
	}

	@Override
	public String getName() {
		return "FlyTalentManager";
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn,
			RequestMessage message) throws ConnectionException, Exception {
		try {
			Player player = check(conn, message, logger);
			Method cacheMethod = methodCache.get(new Integer(message.getType()));
			if (cacheMethod != null) {
				ResponseMessage res = (ResponseMessage) cacheMethod.invoke(this, message, player);
				return res;
			}
			Method m = this.getClass().getDeclaredMethod("handle_" + message.getTypeDescription(), RequestMessage.class, Player.class);
			if (m != null) {
				methodCache.put(new Integer(message.getType()), m);
			}
			ResponseMessage res = (ResponseMessage) m.invoke(this, message, player);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[FlyTalentManager] [handleReqeustMessage] [error] [message:{}] [{}]", new Object[] { message.getTypeDescription(), e });
			throw e;
		}
	}

	@Override
	public boolean handleResponseMessage(Connection conn,
			RequestMessage request, ResponseMessage response)
			throws ConnectionException, Exception {
		return false;
	}

	
} 
