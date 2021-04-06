package com.fy.engineserver.economic.thirdpart.migu.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.InlayArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.data.props.LastingForPetProps;
import com.fy.engineserver.datasource.article.data.props.LastingProps;
import com.fy.engineserver.datasource.article.data.props.MoneyProps;
import com.fy.engineserver.datasource.article.data.props.SingleForPetProps;
import com.fy.engineserver.datasource.article.data.props.SingleProps;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.career.Career;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.datasource.skill.master.SkBean;
import com.fy.engineserver.datasource.skill.master.SkEnhanceManager;
import com.fy.engineserver.economic.thirdpart.migu.MiguSaleService;
import com.fy.engineserver.economic.thirdpart.migu.entity.model.SaleConstants;
import com.fy.engineserver.economic.thirdpart.migu.entity.model.TempSaleInfo;
import com.fy.engineserver.event.Event;
import com.fy.engineserver.event.EventProc;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.CaveField;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.jiazu2.instance.JiazuMember2;
import com.fy.engineserver.jiazu2.instance.JiazuXiulian;
import com.fy.engineserver.jiazu2.manager.JiazuEntityManager2;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.jiazu2.model.PracticeModel;
import com.fy.engineserver.newBillboard.BillboardStatDate;
import com.fy.engineserver.newBillboard.BillboardStatDateManager;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.playerAims.instance.PlayerAimsEntity;
import com.fy.engineserver.playerAims.manager.PlayerAimeEntityManager;
import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.qiancengta.QianCengTaManager;
import com.fy.engineserver.qiancengta.QianCengTa_Ta;
import com.fy.engineserver.society.Relation;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.sprite.horse.HorseOtherData;
import com.fy.engineserver.sprite.horse2.manager.Horse2Manager;
import com.fy.engineserver.sprite.horse2.model.HorseSkillModel;
import com.fy.engineserver.talent.FlyTalentManager;
import com.fy.engineserver.talent.TalentData;
import com.fy.engineserver.talent.TalentPageInfo;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.text.JsonUtil;

public class SaleRecordManager implements EventProc{

	protected static SaleRecordManager m_self = null;
	
	private String fileName;
    
public static org.slf4j.Logger	 log = MiguSaleService.logger;
	    
    protected SaleRecordDAO saleRecordDAO;
    /** 生成米谷交易订单时boss异常验证订单时使用  key=playerId_currentTime*/
    private Map<String, TempSaleInfo> tempSaleInfoMaps = new Hashtable<String, TempSaleInfo>();
    
    public static long cleanInteval = 60 * 60 * 1000;
    
    public Map<String, String> channelReflect = new HashMap<String, String>();
        
    public static SaleRecordManager getInstance() {
		return m_self;
	}
    
	public void initialize() throws Exception{
		
		m_self = this;
		this.doReg();
		loadFile();
		ServiceStartRecord.startLog(this);
	}
	
	public void loadFile() throws Exception {
		File f = new File(fileName);
		if(!f.exists()){
			throw new Exception(fileName + " 配表不存在! " + f.getAbsolutePath());
		}
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(0);	
		int rows = sheet.getPhysicalNumberOfRows();
		for (int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			String key = ReadFileTool.getString(row, 0, log);
			String value = ReadFileTool.getString(row, 1, log);
			channelReflect.put(key, value);
		}
	}
	
	public String getSaleInfoByKey(String key) {
		if (key == null) {
			return "";
		}
		TempSaleInfo tsi = tempSaleInfoMaps.get(key);
		if (tsi == null) {
			return "";
		}
		return tsi.getSaleInfo();
	}
	/**
	 * 角色是否出售中
	 * @param player
	 * @return
	 */
	public boolean isRoleInSale(Player player) {
		try {
			return saleRecordDAO.isRoleSaling(player.getId());
		} catch (Exception e) {
			log.warn("[验证角色是否正在出售] [异常] [" + player.getLogString() + "]", e);
		}
		return true;
	}
	
	/**
	 * 增加订单info缓存
	 * @param key
	 * @param info
	 */
	public void createTempSaleInfo(String key, String info) {
		long now = System.currentTimeMillis();
		try {
			TempSaleInfo tsi = new TempSaleInfo(now, info);
			synchronized (tempSaleInfoMaps) {
				if (!tempSaleInfoMaps.containsKey(key)) {
					tempSaleInfoMaps.put(key, tsi);
				}
			}
		} catch (Exception e) {
			log.warn("[创建订单临时缓存] [key:" + key + "] [" + info + "]", e);
		}
	}
	/**
	 * 定时清理订单info缓存
	 */
	public void cleanTempSaleInfo() {
		long now = System.currentTimeMillis();
		try {
			synchronized (tempSaleInfoMaps) {
				Iterator<String> ite = tempSaleInfoMaps.keySet().iterator();
				while (ite.hasNext()) {
					String key = ite.next();
					TempSaleInfo tsi = tempSaleInfoMaps.get(key);
					if ((tsi.getCreateTime() + cleanInteval) <= now) {
						ite.remove();
					}
				}
			}
		} catch (Exception e) {
			log.warn("[清理订单临时缓存]", e);
		}
	}
	
	public static int 出售角色最低等级 = 111;
	
	public boolean canPlayerSeal4Show(Player player) {
		if (player.getLevel() < 出售角色最低等级) {
			return false;
		}
		if (this.isRoleInSale(player)) {
			return false;
		}
		return true;
	}
	
	public static boolean useJson = true;
	
	public String getPlayerInfoShow(String contenttype, Player player) {
		try {
			int type = Integer.parseInt(contenttype);
			StringBuffer sb = new StringBuffer();
			switch (type) {
			case 1:				//人物属性
				appendAttrStr(player, sb);
				break;
			case 2:				//当前装备
				appendEquStr(player, sb);
				break;
			case 3:				//背包、仓库
				appendBagStr(player, sb);
				break;
			case 4:				//本尊技能。修炼
				appendBaseSoulStr(player, sb);
				break;
			case 5:				//元神技能、修炼
				appendSoulStr(player, sb);
				break;
			case 6:				//宠物
				appendPetStr(player, sb);
				break;
			case 7:				//坐骑
				appendHorseStr(player, sb, Soul.SOUL_TYPE_BASE);		//本尊坐骑
				break;
			case 8:				//元神坐骑
				appendHorseStr(player, sb, Soul.SOUL_TYPE_SOUL);
				break;
			}
			return URLEncoder.encode(sb.toString(),"utf-8");
		} catch (Exception e) {
			log.warn("[获取角色信息] [异常] [" + player.getLogString() + "] [type:" + contenttype + "]", e);
		}
		return "";
	}
	/**
	 * 人物属性
	 * @param player
	 * @param sb
	 */
	public void appendAttrStr(Player player, StringBuffer sb) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		
		CountryManager cm = CountryManager.getInstance();
		Relation re = SocialManager.getInstance().getRelationById(player.getId());
		String countryLevel = CountryManager.得到官职名(cm.官职(player.getCountry(), player.getId()));
		if (countryLevel == null) {
			countryLevel = "没有官职";
		}
		String jiazuName = "没有家族";
		String zongpaiName = "没有宗派";
		if (player.getJiazuName() != null && !player.getJiazuName().isEmpty()) {
			jiazuName = player.getJiazuName();
		}
		if (player.getZongPaiName() != null && !player.getZongPaiName().isEmpty()) {
			zongpaiName = player.getZongPaiName();
		}
		String coupleName = "没有配偶";
		if (re != null && re.getMarriageId() > 0) {
			coupleName = player.getSpouse();
		}
		String title = "";
		if (player.getDefaultTitleType() > 0) {
			title = PlayerTitlesManager.getInstance().getTitleName(player.getDefaultTitleType());
		}
		TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(player.getId());
		int robberyLv = 0;
		if (entity != null) {
			robberyLv = entity.getCurrentLevel();
		}
		int aimNum = 0;
		PlayerAimsEntity aimEntity = PlayerAimeEntityManager.instance.getEntity(player.getId());
		if (aimEntity != null) {
			aimNum = aimEntity.getTotalScore();
		}
		BillboardStatDate bbs = BillboardStatDateManager.getInstance().getBillboardStatDate(player.getId());
		String ss = "黄泉0层";
		if (bbs != null && bbs.getCeng() > 0) {
			ss = BillboardsManager.千层塔道[bbs.getJie()]+(bbs.getCeng()+1)+"层";
		} else {
			QianCengTa_Ta ta = QianCengTaManager.getInstance().getTa(player.getId());
			if (ta != null && ta.getMaxCengInDao() > 0) {
				ss = "黄泉1层";
			}
		}
		if (!useJson) {
			sb.append(String.format(SaleConstants.roleAttr1, player.getMaxHP() + "", player.getMaxMP()+"",player.getPhyAttack()+"",player.getPhyDefence()+SaleConstants.getAttrPercentDes(player,1)
					,player.getMagicAttack()+"",player.getMagicDefence()+SaleConstants.getAttrPercentDes(player,2), player.getBreakDefence()+SaleConstants.getAttrPercentDes(player,3),player.getHit()+SaleConstants.getAttrPercentDes(player,4),player.getDodge()+SaleConstants.getAttrPercentDes(player,5)
					,player.getAccurate()+SaleConstants.getAttrPercentDes(player,6)
					,player.getCriticalHit()+SaleConstants.getAttrPercentDes(player,7),player.getCriticalDefence()+SaleConstants.getAttrPercentDes(player,8),player.getThunderAttack()+"",player.getThunderDefence()+SaleConstants.getAttrPercentDes(player,9),player.getThunderIgnoreDefence()+SaleConstants.getAttrPercentDes(player,10)
					,player.getBlizzardAttack()+"",player.getBlizzardDefence()+SaleConstants.getAttrPercentDes(player,11),player.getBlizzardIgnoreDefence()+SaleConstants.getAttrPercentDes(player,12)
					,player.getWindAttack()+"",player.getWindDefence()+SaleConstants.getAttrPercentDes(player,13),player.getWindIgnoreDefence()+SaleConstants.getAttrPercentDes(player,14)
					,player.getFireAttack()+"",player.getFireDefence()+SaleConstants.getAttrPercentDes(player,15),player.getFireIgnoreDefence()+SaleConstants.getAttrPercentDes(player,16)));
			sb.append(String.format(SaleConstants.roleAttr2, Translate.classlevel[player.getClassLevel()],countryLevel,jiazuName,zongpaiName,coupleName
					,title, player.getLilian()+"",player.getGongxun()+"", player.getEnergy()+"",player.getSpeed()+"",player.getCulture()+"",player.getEvil()+""
					,Player.currUpgradePoints[player.getUpgradeNums()]+"%",aimNum+"/455",robberyLv+"重",ss));
		} else {
			try {
//				sb.append(json1);
				{		//buff信息
					Map<String, Object> buffMap = new LinkedHashMap<String, Object>();
					buffMap.put("title", "buff数据");
					List<String> bf = new ArrayList<String>();
					if (player.getBuffs() != null && player.getBuffs().size() > 0) {
						Buff[] bons = player.getBuffs().toArray(new Buff[player.getBuffs().size()]);
						for (int i=0; i<bons.length; i++) {
							if (bons[i] != null) {
								bf.add(bons[i].getTemplateName() + ":" + bons[i].getDescription());
							}
						}
					}
					buffMap.put("text", bf);
					resultList.add(buffMap);
				}
				Map<String, Object> js = SaleConstants.createPlayerAttrDes(player);
				resultList.add(js);
				Map<String, Object> mainMap = new LinkedHashMap<String, Object>();
				mainMap.put("title", "基本信息");
				List<String> map2 = new ArrayList<String>();
//				Map<String, String> map2 = new HashMap<String, String>();
				for (int i=0; i<SaleConstants.roleJson2.length; i++) {
					String value = "";
					int index = i+1;
					switch (index) {
					case 1:value = 得到带单位的银两(player.getSilver());break;
					case 2:value = 得到带单位的银两(player.getBindSilver());break;
					case 3:value = 得到带单位的银两(player.getWage());break;
					case 4:value = player.getChargePoints() + "";break;
					case 5:value = Translate.classlevel[player.getClassLevel()];break;
					case 6:value = countryLevel;break;
					case 7:value = jiazuName;break;
					case 8:value = zongpaiName;break;
					case 9:value = coupleName;break;
					case 10:value = title;break;
					case 11:value = player.getLilian()+"";break;
					case 12:value = player.getGongxun()+"";break;
					case 13:value = player.getEnergy()+"";break;
					case 14:value = player.getSpeed()+"";break;
					case 15:value = player.getCulture()+"";break;
					case 16:value = player.getEvil()+"";break;
					case 17:value = Player.currUpgradePoints[player.getUpgradeNums()]+"%";break;
					case 18:value = aimNum+"/455";break;
					case 19:value = robberyLv+"重"+"";break;
					case 20:value = ss+"";break;
					}
					map2.add(SaleConstants.roleJson2[i]+":"+value);
//					String json2 = JsonUtil.jsonFromObject(mainMap);
//					sb.append(json2);
				}
				mainMap.put("text", map2);
				resultList.add(mainMap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.warn("[异常]", e);
			}
		}
		
		try {			//仙婴信息
			Map<String, Object> mainMap = new LinkedHashMap<String, Object>();
			mainMap.put("title", "仙婴信息");
			if (player.getLevel() > 220) {
				TalentData data = FlyTalentManager.getInstance().getTalentData(player.getId());
				if (data != null) {
					List<String> map2 = new ArrayList<String>();
					map2.add("仙婴等级:" + data.getXylevel());
					int realLevel = data.getXylevel()+data.getXylevelA();
					long time = FlyTalentManager.TALENT_SKILL_CD_TIME-data.getMinusCDTimes();
					String showTime = TimeTool.instance.getShowTime(time);
					map2.add("附体冷却时间:" + showTime);
					TalentPageInfo info = FlyTalentManager.getInstance().infos.get(realLevel - 1);
					map2.add("外攻:" + info.getPhyAttact());
					map2.add("内攻:" + info.getMagicAttact());
					map2.add("外防:" + info.getPhyDefence());
					map2.add("内防:" + info.getMagicDefence());
					map2.add("气血:" + info.getHp());
					mainMap.put("text", map2);
				}
			}
			resultList.add(mainMap);
		} catch (Exception e) {
			log.warn("[获取玩家仙婴属性] [异常] [" + player.getLogString() + "]", e);
		}
		
		Cave cave = FaeryManager.getInstance().getCave(player);
		int xiaoyaojuLv = 0;
		int wanbaokuLv = 0;
		int yushouzhaiLv = 0;
		int famenLv = 0;
		int tiandi = 0;
		long liangshi = 0;
		long mucai = 0;
		long shiliao = 0;
		if (cave != null) {
			xiaoyaojuLv = cave.getMainBuilding().getGrade();
			wanbaokuLv = cave.getStorehouse().getGrade();
			yushouzhaiLv = cave.getPethouse().getGrade();
			famenLv = cave.getDoorplate().getGrade();
			for (CaveField caveField : cave.getFields()) {
				if (caveField.getAssartStatus() != FaeryConfig.FIELD_STATUS_DESOLATION) {
					tiandi++;
				}
			}
			liangshi = cave.getCurrRes().getFood();
			mucai = cave.getCurrRes().getWood();
			shiliao = cave.getCurrRes().getStone();
			if (!useJson) {
				sb.append(String.format(SaleConstants.roleAttr3, xiaoyaojuLv+"",wanbaokuLv+"",yushouzhaiLv+"",famenLv+"",tiandi+"/6",liangshi+"",mucai+"",shiliao+""));
			} else {
				Map<String, Object> mainMap = new LinkedHashMap<String, Object>();
				mainMap.put("title", "仙府信息");
				List<String> map2 = new ArrayList<String>();
//				Map<String, String> map2 = new HashMap<String, String>();
				map2.add("逍遥居"+":"+xiaoyaojuLv+"");
				map2.add("万宝库:"+wanbaokuLv+"");
				map2.add("驭兽斋:"+yushouzhaiLv+"");
				map2.add("法门:"+famenLv+"");
				map2.add("田地:"+ tiandi+"/6");
				map2.add("粮食:"+liangshi+"");
				map2.add("木材:"+ mucai+"");
				map2.add("石材:"+shiliao+"");
				mainMap.put("text", map2);
				resultList.add(mainMap);
//				try {
//					String json3 = JsonUtil.jsonFromObject(mainMap);
//					sb.append(json3);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
			}
		} else {
			if (!useJson) {
				sb.append(SaleConstants.roleAttr3_2);
			} else {
				Map<String, Object> mainMap = new LinkedHashMap<String, Object>();
				mainMap.put("title", "仙府信息");
				resultList.add(mainMap);
//				try {
//					String json3 = JsonUtil.jsonFromObject(mainMap);
//					sb.append(json3);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
			}
		}
		if (!useJson) {
			sb.append(String.format(SaleConstants.roleAttr4,"0","0","0"));
		} else {
//			try {
//				Map<String, Object> mainMap = new LinkedHashMap<String, Object>();
//				mainMap.put("title", "战队信息");
////				BattleTeam btm1 = JJCManager.getInstance().getTeam(player, 1, "米谷角色查询");
////				BattleTeamMember btm = JJCManager.getInstance().getPlayerInfo(player.getId(), 1);
//				List<String> map2 = new ArrayList<String>();
////				Map<String, String> map2 = new HashMap<String, String>();
//				if (btm1 != null) {
//					map2.add("战队名称:"+btm1.getTeamName());
//					map2.add("战队战勋:"+ btm1.getTeamLevel() + "");
//					if (btm != null) {
//						map2.add("个人战勋:"+ btm.getBattleLevel()+"");
//					} else {
//						map2.add("个人战勋:0");
//					}
//				} else {
//					map2.add("战队名称:没有战队");
//				}
//				mainMap.put("text", map2);
////				String json3 = JsonUtil.jsonFromObject(mainMap);
//				resultList.add(mainMap);
////				sb.append(json3);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}
		if (useJson) {
			try {
				String json3 = JsonUtil.jsonFromObject(resultList);
				sb.append(json3);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String 得到带单位的银两(long money) {
		StringBuffer sb = new StringBuffer();
		long 文 = money % 1000;
		long 两 = money / 1000;
		两 = 两 % 1000;
		long 锭 = money / 1000000;
		if (锭 > 0) {
			sb.append(锭 + Translate.锭);
		}
		if (两 > 0) {
			sb.append(两 + Translate.两);
		}
		if (文 > 0) {
			sb.append(文 + Translate.文);
		}
		if (sb.length() <= 0) {
			sb.append("0文");
		}
		return sb.toString();
	}
	/**
	 * 装备信息
	 * @param player
	 * @param sb
	 */
	public void appendEquStr(Player player, StringBuffer sb) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for (Soul soul : player.getSouls()) {
			if (soul == null) {
				continue;
			}
			Map<String, Object> mainMap = new LinkedHashMap<String, Object>();
			List<EquipInfoClass> equList = new ArrayList<EquipInfoClass>();
//			List<Map<String, String>> equList = new ArrayList<Map<String,String>>();
			if (!useJson) {
				sb.append(String.format(SaleConstants.divStart, SaleConstants.equInfos[soul.getSoulType()]));
			} else {
				mainMap.put("title", SaleConstants.equInfos[soul.getSoulType()]);
			}
			for (int i=0; i<soul.getEc().getEquipmentIds().length; i++) {
				if (soul.getEc().getEquipmentIds()[i] > 0) {
					ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(soul.getEc().getEquipmentIds()[i]);
					if (ae != null) {
						Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
						if (a != null) {
							if (!useJson) {
								sb.append(String.format(SaleConstants.currentEqu2, a.getIconId(),"1",ae.getId()+""));	
							} else {
								boolean isEu = false;
								if (ae instanceof EquipmentEntity) {
									isEu = true;
								}
								EquipInfoClass info = new EquipInfoClass(ae.getArticleName(),a.getIconId(),1,ae.getId(),i,ae.getColorType(),isEu,ae.isBinded());
								equList.add(info);
							}
						}
					}
				}
			}
			
			if (!useJson) {
				sb.append(SaleConstants.divStEnd);
			} else {
				try {
					mainMap.put("image", equList);
					resultList.add(mainMap);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		try {
			if (resultList.size() == 1) {
				Map<String, Object> mainMap = new LinkedHashMap<String, Object>();
				List<EquipInfoClass> equList = new ArrayList<EquipInfoClass>();
				mainMap.put("title", SaleConstants.equInfos[1]);
				mainMap.put("image", equList);
				resultList.add(mainMap);
			}
			String json3 = JsonUtil.jsonFromObject(resultList);
			sb.append(json3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 背包/仓库
	 * @param player
	 * @param sb
	 */
	public void appendBagStr(Player player, StringBuffer sb) {
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		
		for (int i=0; i<SaleConstants.bags.length; i++) {
			Knapsack bag = getKnapByIndex(player, i);
			if (bag != null) {
				Map<String, Object> mainMap = new LinkedHashMap<String, Object>();
				List<EquipInfoClass> equList = new ArrayList<EquipInfoClass>();
				if (!useJson) {
					sb.append(String.format(SaleConstants.divStart, SaleConstants.bags[i]));
				} else {
					mainMap.put("title", SaleConstants.bags[i]);
				}
				for (int j=0; j<bag.getCells().length; j++) {
					Cell c = bag.getCell(j);
					if (c != null && c.getEntityId() > 0) {
						ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(c.getEntityId());
						if (ae != null && checkArticle(ae)) {
							Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
							if (a != null) {
								if (!useJson) {
									sb.append(String.format(SaleConstants.currentEqu2, a.getIconId(),c.getCount()+"",ae.getId()+""));
								} else {
									boolean isEu = false;
									if (ae instanceof EquipmentEntity) {
										isEu = true;
									}
									EquipInfoClass info = new EquipInfoClass(ae.getArticleName(), a.getIconId(), c.getCount(), 
											ae.getId(), j, ae.getColorType(), isEu,ae.isBinded());
									equList.add(info);
								}
							}
						}
					}
				}
				if (!useJson) {
					sb.append(SaleConstants.divStEnd);
				} else {
					mainMap.put("image", equList);
					resultList.add(mainMap);
//					try {
//						mainMap.put("image", equList);
//						String json3 = JsonUtil.jsonFromObject(mainMap);
//						sb.append(json3);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
				}
			}
		}
		if (player.getShouhunKnap() != null) {
			Map<String, Object> mainMap = new LinkedHashMap<String, Object>();
			List<EquipInfoClass> list = new ArrayList<SaleRecordManager.EquipInfoClass>();
			if (!useJson) {
				sb.append(String.format(SaleConstants.divStart, "兽魂仓库"));
			} else {
				mainMap.put("title", "兽魂仓库");
			}
			int index = 0;
			for (long id : player.getShouhunKnap()) {
				if (id > 0) {
					ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(id);
					if (ae != null && checkArticle(ae)) {
						Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
						if (a != null) {
							if (!useJson) {
								sb.append(String.format(SaleConstants.currentEqu2, a.getIconId(),"1",ae.getId()+""));
							} else {
								boolean isEu = false;
								if (ae instanceof EquipmentEntity) {
									isEu = true;
								}
								EquipInfoClass info = new EquipInfoClass(ae.getArticleName(), a.getIconId(), 1, ae.getId(), index++, ae.getColorType(),isEu,ae.isBinded());
								list.add(info);
							}
						}
					}
				}
			}
			if (!useJson) {
				sb.append(SaleConstants.divStEnd);
			} else {
				try {
					mainMap.put("image", list);
					resultList.add(mainMap);
//					String json3 = JsonUtil.jsonFromObject(mainMap);
//					sb.append(json3);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (useJson) {
			try {
				String json3 = JsonUtil.jsonFromObject(resultList);
				sb.append(json3);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 本尊技能/修炼
	 * @param player
	 * @param sb
	 */
	public void appendBaseSoulStr(Player player, StringBuffer sb) {
		Soul soul = player.getSoul(Soul.SOUL_TYPE_BASE);
		if (soul == null) {
			return;
		}
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for (int i=0; i<SaleConstants.soulInfo.length; i++) {
			if (!useJson) {
				sb.append(String.format(SaleConstants.divStart, SaleConstants.soulInfo[i]));
				sb.append(getSkillInfoDes(player, soul, i, null));
				sb.append(SaleConstants.divStEnd);
			} else {
				try {
					Map<String, Object> mainMap = new LinkedHashMap<String, Object>();
					mainMap.put("title", SaleConstants.soulInfo[i]);
					List<String> map2 = new ArrayList<String>();
//					Map<String, String> map2 = new HashMap<String, String>();
					getSkillInfoDes(player, soul, i, map2);
					mainMap.put("text", map2);
					resultList.add(mainMap);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		try {
			String json3 = JsonUtil.jsonFromObject(resultList);
			sb.append(json3);
		} catch (Exception e) {
			e.printStackTrace();;
		}
	}
	/**
	 * 元神技能/修炼
	 * @param player 	
	 * @param sb
	 */
	public void appendSoulStr(Player player, StringBuffer sb) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		Soul soul = player.getSoul(Soul.SOUL_TYPE_SOUL);
		if (soul == null) {
			for (int i=0; i<SaleConstants.soulInfo.length; i++) {
				Map<String, Object> mainMap = new LinkedHashMap<String, Object>();
				mainMap.put("title", SaleConstants.soulInfo[i]);
				List<String> map2 = new ArrayList<String>();
				mainMap.put("text", map2);
				resultList.add(mainMap);
			}
			try {
				String json3 = JsonUtil.jsonFromObject(resultList);
				sb.append(json3);
			} catch (Exception e) {
				e.printStackTrace();;
			}
			return;
		}
		for (int i=0; i<SaleConstants.soulInfo.length; i++) {
			if (i >= 2) {			//元神没有之后两个属性
				break;
			}
			if (!useJson) {
				sb.append(String.format(SaleConstants.divStart, SaleConstants.soulInfo[i]));
				sb.append(getSkillInfoDes(player, soul, i, null));
				sb.append(SaleConstants.divStEnd);
			} else {
				try {
					Map<String, Object> mainMap = new LinkedHashMap<String, Object>();
					mainMap.put("title", SaleConstants.soulInfo[i]);
//					String json3 = JsonUtil.jsonFromObject(mainMap);
					List<String> map2 = new ArrayList<String>();
//					Map<String, String> map2 = new HashMap<String, String>();
					getSkillInfoDes(player, soul, i, map2);
					mainMap.put("text", map2);
					resultList.add(mainMap);
//					sb.append(json3);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		try {
			String json3 = JsonUtil.jsonFromObject(resultList);
			sb.append(json3);
		} catch (Exception e) {
			e.printStackTrace();;
		}
	}
	/***
	 * 宠物
	 * @param player
	 * @param sb
	 */
	public void appendPetStr(Player player, StringBuffer sb) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for (int i=0; i<SaleConstants.petInfos.length; i++) {
			Map<String, Object> mainMap = new LinkedHashMap<String, Object>();
			List<EquipInfoClass> equList = new ArrayList<SaleRecordManager.EquipInfoClass>();
			if (!useJson) {
				sb.append(String.format(SaleConstants.divStart, SaleConstants.petInfos[i]));
			} else {
				mainMap.put("title", SaleConstants.petInfos[i]);
			}
			if (i == 0) {			//宠物背包
				Knapsack bag = player.getPetKnapsack();
				if (bag != null) {
					for (int j=0; j<bag.getCells().length; j++) {
						Cell c = bag.getCell(j);
						if (c != null && c.getEntityId() > 0) {
							ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(c.getEntityId());
							if (ae != null) {
								Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
								if (a != null) {
									if (!useJson) {
										sb.append(String.format(SaleConstants.currentEqu2, a.getIconId(),c.getCount()+"",ae.getId()+""));
									} else {
										boolean isEu = false;
										if (ae instanceof EquipmentEntity) {
											isEu = true;
										}
										EquipInfoClass info = new EquipInfoClass(ae.getArticleName(), a.getIconId(), c.getCount(), ae.getId(), j, ae.getColorType(), isEu,ae.isBinded());
										equList.add(info);
									}
								}
							}
						}
					}
				}
			} /*else if(i ==1){		//仙府驭兽斋
				Cave cave = FaeryManager.getInstance().getCave(player);
				if (cave != null && cave.getPethouse() != null && cave.getPethouse().getHookInfos() != null) {
					for (PetHookInfo phi : cave.getPethouse().getHookInfos()) {
						if (phi != null && phi.getArticleId() > 0) {
							ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(phi.getArticleId());
							if (ae != null) {
								Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
								if (a != null) {
									if (!useJson) {
										sb.append(String.format(SaleConstants.currentEqu2, a.getIconId(),"1",ae.getId()+""));
									} else {
										Map<String, String> map2 = new HashMap<String, String>();
										map2.put("icon", a.getIconId());
										map2.put("num", "1");
										map2.put("equiponlyid", ae.getId()+"");
										map2.put("cell", "1");
										map2.put("quality", ae.getColorType()+"");
										mainMap.put("image", map2);
									}
								}
							}
						}
					}
				}
			}*/
			if (!useJson) {
				sb.append(SaleConstants.divStEnd);
			} else {
				try {
					mainMap.put("image", equList);
					resultList.add(mainMap);
					String json3 = JsonUtil.jsonFromObject(resultList);
					sb.append(json3);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static boolean 是否开启魂石属性显示 = true;
	/***
	 * 坐骑
	 * @param player
	 * @param sb
	 */
	public void appendHorseStr(Player player, StringBuffer sb, int soulType) {
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		
		Soul soul = player.getSoul(soulType);
		if (soul != null) {
			StringBuffer fly = new StringBuffer();
			StringBuffer horseEqu = new StringBuffer();
			List<EquipInfoClass> equDesList = new ArrayList<SaleRecordManager.EquipInfoClass>();
			List<EquipInfoClass> hunshi1 = new ArrayList<SaleRecordManager.EquipInfoClass>();
			List<EquipInfoClass> hunshi2 = new ArrayList<SaleRecordManager.EquipInfoClass>();
			List<String> flyDes = new ArrayList<String>();
			for (long id : soul.getHorseArr()) {
				Horse horse = HorseManager.getInstance().getHorseByHorseId(player, id, soul);
				if (horse != null ) {
					horse.setCareer(soul.getCareer());
					horse.owner = player;
					horse.init();
					horse.setHorseLevel(soul.getGrade());
					HorseManager.getInstance().mCache.put(horse.getHorseId(), horse);
					if (!horse.isFly()) {
						HorseOtherData otherData = horse.getOtherData();
						StringBuffer skillDes = new StringBuffer();
						for (int i=0; i<otherData.getSkillList().length; i++) {
							if (otherData.getSkillList()[i] > 0) {
								HorseSkillModel model = Horse2Manager.instance.horseSkillMap.get(otherData.getSkillList()[i]);
								if (model != null) {
									skillDes.append(model.getName()+"LV"+(otherData.getSkillLevel()[i]+1));
									if (i < (otherData.getSkillList().length-1)) {
										skillDes.append(",");
									}
								}
							}
						}
						if (!useJson) {
							sb.append(String.format(SaleConstants.horse_land, horse.getHorseShowName(),horse.getHorseLevel()+"",Horse2Manager.colorDes[horse.getColorType()],horse.getSpeed()+""
									,otherData.getBloodStar() + "星",Horse2Manager.getJieJiMess(otherData.getRankStar()),horse.getMaxHP(),horse.getMaxMP(),horse.getPhyAttack()
									,horse.getPhyDefence(),horse.getBreakDefence(),horse.getHit(),horse.getDodge(),horse.getAccurate(),horse.getCriticalHit(),horse.getCriticalDefence()
									,horse.getThunderAttack(),horse.getThunderDefence(),horse.getThunderIgnoreDefence()
									,horse.getBlizzardAttack(),horse.getBlizzardDefence(),horse.getBlizzardIgnoreDefence()
									,horse.getWindAttack(),horse.getWindDefence(),horse.getWindIgnoreDefence()
									,horse.getFireAttack(),horse.getFireDefence(),horse.getFireIgnoreDefence(),skillDes.toString()));
						} else {
							Map<String, Object> mainMap = new LinkedHashMap<String, Object>();
							List<String> map = new ArrayList<String>();
//							Map<String, String> map = new LinkedHashMap<String, String>();
							mainMap.put("title", "陆地坐骑");
							SaleConstants.putHorseDes(horse, map);
							mainMap.put("text", map);
							resultList.add(mainMap);
//							try {
//								String json = JsonUtil.jsonFromObject(mainMap);
//								sb.append(json);
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
						}
						for (int i=0; i<horse.getEquipmentColumn().getEquipmentIds().length; i++) {
							long equId = horse.getEquipmentColumn().getEquipmentIds()[i];
							if (equId > 0) {
								ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(equId);
								if (ae != null) {
									Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
									if (a != null) {
										if (!useJson) {
											horseEqu.append(String.format(SaleConstants.currentEqu2, a.getIconId(),"1",ae.getId()+""));
										} else {
											boolean isEu = false;
											if (ae instanceof EquipmentEntity) {
												isEu = true;
											}
											EquipInfoClass info = new EquipInfoClass(ae.getArticleName(), a.getIconId(), 1, ae.getId(), i, ae.getColorType(), isEu,ae.isBinded());
											equDesList.add(info);
										}
									}
								}
							}
						}
						for (int i=0; i<horse.getHunshiArray().length; i++) {
							long equId = horse.getHunshiArray()[i];
							if (equId > 0) {
								ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(equId);
								if (ae != null) {
									Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
									if (a != null) {
										EquipInfoClass info = new EquipInfoClass(ae.getArticleName(), a.getIconId(), 1, ae.getId(), i, ae.getColorType(), false, ae.isBinded());
										hunshi1.add(info);
									}
								}
							}
						}
						for (int i=0; i<horse.getHunshi2Array().length; i++) {
							long equId = horse.getHunshi2Array()[i];
							if (equId > 0) {
								ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(equId);
								if (ae != null) {
									Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
									if (a != null) {
										EquipInfoClass info = new EquipInfoClass(ae.getArticleName(), a.getIconId(), 1, ae.getId(), i, ae.getColorType(), false, ae.isBinded());
										hunshi2.add(info);
									}
								}
							}
						}
					} else {
						if (soulType ==Soul.SOUL_TYPE_BASE) {				//只有本尊显示飞行坐骑
							if (!useJson) {
								fly.append(String.format(SaleConstants.base_2, horse.getHorseName()+"(七阶)"));
							} else {
								flyDes.add(horse.getHorseName()+"(七阶)");
							}
						}
					}
				}
			}
			if (!useJson) {
				sb.append(String.format(SaleConstants.divStart, "坐骑装备")).append(horseEqu.toString()).append(SaleConstants.divStEnd);
				sb.append(String.format(SaleConstants.divStart, "飞行坐骑")).append(fly.toString()).append(SaleConstants.divStEnd);
			} else {
				try {
					if (resultList.size() <= 0) {
						Map<String, Object> mainMap = new LinkedHashMap<String, Object>();
						List<String> map = new ArrayList<String>();
						mainMap.put("title", "陆地坐骑");
						mainMap.put("text", map);
						resultList.add(mainMap);
					}
					
					Map<String, Object> mainMap1 = new LinkedHashMap<String, Object>();
					mainMap1.put("title", "坐骑装备");
					mainMap1.put("image", equDesList);
					resultList.add(mainMap1);
					
					if (是否开启魂石属性显示) {
						Map<String, Object> mainMap3 = new LinkedHashMap<String, Object>();
						mainMap3.put("title", "魂石");
						mainMap3.put("image", hunshi1);
						resultList.add(mainMap3);
						
						Map<String, Object> mainMap4 = new LinkedHashMap<String, Object>();
						mainMap4.put("title", "套装魂石");
						mainMap4.put("image", hunshi2);
						resultList.add(mainMap4);
					}
//					String json1 = JsonUtil.jsonFromObject(mainMap1);
//					sb.append(json1);
					if (soulType == Soul.SOUL_TYPE_BASE) {
						Map<String, Object> mainMap2 = new LinkedHashMap<String, Object>();
						mainMap2.put("title", "飞行坐骑");
						mainMap2.put("text", flyDes);
						resultList.add(mainMap2);
					}
					String json2 = JsonUtil.jsonFromObject(resultList);
					sb.append(json2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			Map<String, Object> mainMap = new LinkedHashMap<String, Object>();
			mainMap.put("title", "陆地坐骑");
			List<EquipInfoClass> equDesList = new ArrayList<SaleRecordManager.EquipInfoClass>();
			mainMap.put("image", equDesList);
			resultList.add(mainMap);
			try {
				String json2 = JsonUtil.jsonFromObject(resultList);
				sb.append(json2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getBaoshiAttrDes(InlayArticle baoshi) {
		StringBuffer sb = new StringBuffer();
		int[] values = baoshi.getPropertysValues();
		if(values != null){
			for(int i = 0; i < values.length; i++){
				int value = values[i];
				if(value != 0){
					switch(i){
					case 0 :
						sb.append(Translate.最大血量).append("+").append(value);
						break;
					case 1 :
						sb.append(Translate.物理攻击).append("+").append(value);
						break;
					case 2 :
						sb.append(Translate.法术攻击).append("+").append(value);
						break;
					case 3 :
						sb.append(Translate.物理防御).append("+").append(value);
						break;
					case 4 :
						sb.append(Translate.法术防御).append("+").append(value);
						break;
					case 5 :
						sb.append(Translate.闪躲).append("+").append(value);
						break;
					case 6 :
						sb.append(Translate.免暴).append("+").append(value);
						break;
					case 7 :
						sb.append(Translate.命中).append("+").append(value);
						break;
					case 8 :
						sb.append(Translate.暴击).append("+").append(value);
						break;
					case 9 :
						sb.append(Translate.精准).append("+").append(value);
						break;
					case 10 :
						sb.append(Translate.破甲).append("+").append(value);
						break;
					case 11 :
						sb.append(Translate.庚金攻击).append("+").append(value);
						break;
					case 23:
						sb.append(Translate.庚金攻击).append("+").append(value);
						break;
					case 26:
						sb.append(Translate.庚金攻击).append("+").append(value);
						break;
					case 12 :
						sb.append(Translate.庚金抗性).append("+").append(value);
						break;
					case 24 :
						sb.append(Translate.庚金抗性).append("+").append(value);
						break;
					case 27 :
						sb.append(Translate.庚金抗性).append("+").append(value);
						break;
					case 13 :
						sb.append(Translate.庚金减抗).append("+").append(value);
						break;
					case 25 :
						sb.append(Translate.庚金减抗).append("+").append(value);
						break;
					case 28 :
						sb.append(Translate.庚金减抗).append("+").append(value);
						break;
					case 14 :
						sb.append(Translate.葵水攻击).append("+").append(value);
						break;
					case 29 :
						sb.append(Translate.葵水攻击).append("+").append(value);
						break;
					case 32 :
						sb.append(Translate.葵水攻击).append("+").append(value);
						break;
					case 15 :
						sb.append(Translate.葵水抗性).append("+").append(value);
						break;
					case 30 :
						sb.append(Translate.葵水抗性).append("+").append(value);
						break;
					case 33 :
						sb.append(Translate.葵水抗性).append("+").append(value);
						break;
					case 16 :
						sb.append(Translate.葵水减抗).append("+").append(value);
						break;
					case 31 :
						sb.append(Translate.葵水减抗).append("+").append(value);
						break;
					case 34 :
						sb.append(Translate.葵水减抗).append("+").append(value);
						break;
					case 17 :
						sb.append(Translate.离火攻击).append("+").append(value);
						break;
					case 41 :
						sb.append(Translate.离火攻击).append("+").append(value);
						break;
					case 44 :
						sb.append(Translate.离火攻击).append("+").append(value);
						break;
					case 18 :
						sb.append(Translate.离火抗性).append("+").append(value);
						break;
					case 42 :
						sb.append(Translate.离火抗性).append("+").append(value);
						break;
					case 45 :
						sb.append(Translate.离火抗性).append("+").append(value);
						break;
					case 19 :
						sb.append(Translate.离火减抗).append("+").append(value);
						break;
					case 43 :
						sb.append(Translate.离火减抗).append("+").append(value);
						break;
					case 46 :
						sb.append(Translate.离火减抗).append("+").append(value);
						break;
					case 20 :
						sb.append(Translate.乙木攻击).append("+").append(value);
						break;
					case 35 :
						sb.append(Translate.乙木攻击).append("+").append(value);
						break;
					case 38 :
						sb.append(Translate.乙木攻击).append("+").append(value);
						break;
					case 21 :
						sb.append(Translate.乙木抗性).append("+").append(value);
						break;
					case 36 :
						sb.append(Translate.乙木抗性).append("+").append(value);
						break;
					case 39 :
						sb.append(Translate.乙木抗性).append("+").append(value);
						break;
					case 22 :
						sb.append(Translate.乙木减抗).append("+").append(value);
						break;
					case 37 :
						sb.append(Translate.乙木减抗).append("+").append(value);
						break;
					case 40 :
						sb.append(Translate.乙木减抗).append("+").append(value);
						break;
					}
				}
			}
		}
		return sb.toString();
	}
	public static String[] wingDes = new String[]{"闪躲+1%","命中+1%","暴击+1%","精准+1%","破甲+1%"};
	/**
	 * 
	 * @param soul		元神
	 * @param index		技能类型
	 * @return	技能信息
	 */
	public String getSkillInfoDes(Player player, Soul soul, int index, List<String> map) {
		StringBuffer sb = new StringBuffer();
		CareerManager cm = CareerManager.getInstance();
		Career c = cm.getCareer(soul.getCareer());
		if (index == 0) {			//元神基础/进阶技能
			for (int i=0; i<c.getBasicSkills().length; i++) {
				Skill skill = c.getBasicSkills()[i];
				if (map == null) {
					sb.append(String.format(SaleConstants.base, skill.getName(), soul.getCareerBasicSkillsLevels()[i]+"级"));
				}else {
					map.add(skill.getName()+":"+soul.getCareerBasicSkillsLevels()[i]+"级");
				}
			}
			for (int i=0; i<c.threads[0].getSkills().length; i++) {
				Skill skill = c.threads[0].getSkillByIndex(i);
				if (map == null) {
					sb.append(String.format(SaleConstants.base, skill.getName(), soul.getSkillOneLevels()[i]+"级"));
				} else {
					map.add(skill.getName()+":"+soul.getSkillOneLevels()[i]+"级");
				}
			}
		} else if (index == 1) {	//元神大师技能
			SkBean s = SkEnhanceManager.getInst().findSkBean(player);
			if (s != null) {
				byte[] lvs = null;
				int sum = 0;
				if (soul.getSoulType() == Soul.SOUL_TYPE_BASE) {
					lvs = s.getLevels();
				} else {
					lvs = s.getSoulLevels();
				}
				if (soul.getSoulType() == Soul.SOUL_TYPE_BASE && s.getLevels() != null) {
					for (byte b : s.getLevels()) {
						if (b > 0) {
							sum += b;
						}
					}
				}
				if (soul.getSoulType() == Soul.SOUL_TYPE_SOUL && s.getSoulLevels() != null) {
					for (byte b : s.getSoulLevels()) {
						if (b > 0) {
							sum += b;
						}
					}
				}
				if (map == null) {
					sb.append(String.format(SaleConstants.base, "总修炼",sum+"/360"));
					sb.append(String.format(SaleConstants.base, "剩余修炼值",s.getPoint()+""));
				} else {
					map.add("总修炼:" + sum+"/360");
					map.add("剩余修炼值:"+ s.getPoint()+"");
				}
				if (lvs != null) {
					for (int i=0; i<lvs.length; i++) {
						Skill skill = c.threads[0].getSkillByIndex(i);
						if (skill != null) {
							if (map == null) {
								sb.append(String.format(SaleConstants.base, "大师级能"+skill.getName(), SkEnhanceManager.getSkSkillDes(lvs[i])));
							} else {
								byte lv = lvs[i] >=0? lvs[i] : 0;
								map.add("大师技能" + skill.getName()+":"+SkEnhanceManager.getSkSkillDes(lv));
							}
						}
					}
				}
			} 
		} else if (index == 2) {	//元神怒气技能
			for (int i=0; i<c.getNuqiSkills().length; i++) {
				Skill skill = c.getNuqiSkills()[i];
				if (map == null) {
					sb.append(String.format(SaleConstants.base, skill.getName(), player.getNuqiSkillsLevels()[i]+"级"));
				} else {
					map.add(skill.getName()+":"+ player.getNuqiSkillsLevels()[i]+"级");
				}
			}
		} else if (index == 3) {	//角色心法、注魂
			for (int i=0; i<c.getXinfaSkills().length; i++) {
				Skill skill = c.getXinfaSkills()[i];
				int lv = player.getSkillLevel(skill.getId());
				if (lv > 0) {
					if (map == null) {
						sb.append(String.format(SaleConstants.base, skill.getName(), lv/10+"层"+lv%10+"重"));
					} else {
						map.add(skill.getName()+":"+lv/10+"层"+lv%10+"重");
					}
				}
			}
		} else if (index == 4) {	//家族修炼
			JiazuMember2 jm2 = JiazuEntityManager2.instance.getEntity(player.getId());
			if (jm2 != null) {
				for (JiazuXiulian xiulian : jm2.getXiulian()) {
					if (xiulian.getSkillId() > 0) {
						PracticeModel model = JiazuManager2.instance.praticeMaps.get(xiulian.getSkillId());
						if (model != null) {
							if (map == null) {
								sb.append(String.format(SaleConstants.base, model.getSkillName(), xiulian.getSkillLevel()+"级"));
							} else {
								map.add(model.getSkillName()+":"+xiulian.getSkillLevel()+"级");
							}
						}
					}
				}
			}
		}
		return sb.toString(); 
	}
	public static String[] 不显示道具统计名 = new String[]{"银票"};
	public static String[] 需要显示的宠物道具 = new String[]{"金髓丸", "补天丹", "天璇丸", "天权丹", "天命丹", "天启丹"};
	/**
	 * 检查是否需要显示
	 * @param ae
	 * @return
	 */
	public boolean checkArticle(ArticleEntity ae) {
		Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
		if (a instanceof Equipment && ae.getColorType() < 4) {
			return false;
		} else if (a instanceof SingleProps || a instanceof SingleForPetProps || a instanceof LastingProps || a instanceof LastingForPetProps
				|| a instanceof MoneyProps) {
			for (int i=0; i<需要显示的宠物道具.length; i++) {
				if (需要显示的宠物道具[i].equalsIgnoreCase(a.getName_stat())) {
					return true;
				}
			}
			return false;
		}
		for (int i=0; i<不显示道具统计名.length; i++) {
			if (不显示道具统计名[i].equalsIgnoreCase(a.getName_stat())) {
				return false;
			}
		}
		if (ae.getTimer() != null) {
			long day = (ae.getTimer().getEndTime()-com.fy.engineserver.gametime.SystemTime.currentTimeMillis())/1000/60/60/24;
			return day > 物品显示有效期设定;
		}
		return true;
	}
	
	public static int 物品显示有效期设定 = 20;
	
	public static int[] newXinfa = new int[]{7001,7002,7003,7004,7005,7006,7007,7008,7009,7010,7011,7012};
	
	public Knapsack getKnapByIndex(Player player, int index) {
		switch (index) {
		case 0:
			return player.getKnapsack_common();
		case 1:
			return player.getKnapsack_fangbao();
		case 2:
			return player.getKnapsacks_cangku();
		case 3:
			return player.getKnapsacks_QiLing();
		case 4:
			return player.getKnapsacks_warehouse();
		default:
			break;
		}
		return null;
	}
	
	public static class EquipInfoClass {
		public String name;
		public String icon;
		public int num;
		public String equiponlyid;
		public int cell;
		public int quality;
		public boolean bind;
		public EquipInfoClass(String name,String icon,int num,long equiponlyid,int cell,int quality, boolean isEqu,boolean bind) {
			this.name = name;
			this.icon = icon;
			this.num = num;
			this.equiponlyid = equiponlyid+"";
			this.cell = cell;
			this.bind = bind;
			if (isEqu) {
				if (quality <= 3) {				//紫色以下色值+1
					this.quality = quality+1;
				} else if (quality == 4) {		//完美紫改为7
					this.quality = 7;
				} else {
					this.quality = quality;
				}
			} else {
				this.quality = quality+1;
			}
			
		}
	}
	
	public int getColorType(int quality){
		int result = quality;
		if (quality <= 3) {				//紫色以下色值+1
			result = quality+1;
		} else if (quality == 4) {		//完美紫改为7
			result = 7;
		} else {
			result = quality;
		}
		return result;
	}
	
	/**
	 * 创建一个订单
	 * @param saleRecord
	 * @return
	 */
	public SaleRecord createSaleRecord(SaleRecord saleRecord) {
		saleRecordDAO.saveNew(saleRecord);
		log.warn("[创建订单] " + saleRecord.getLogStr());
		return saleRecord;
	}

	/**
	 * 通过id获得一个订单
	 * @param id
	 * @return
	 */
	public SaleRecord getSaleRecord(long id) {
		SaleRecord saleRecord = saleRecordDAO.getById(id);
		return saleRecord;
	}
	
	/**
	 * 通过订单号获得一个订单
	 * @param orderId
	 * @return
	 */
	public SaleRecord getSaleRecord(String orderId) {
		return saleRecordDAO.getByOrderid(orderId);
	}
	

//	 * 通过渠道订单号获得一个订单
//	 * @param orderId
//	 * @return
//	 */
	public SaleRecord getByChannelOrderid(String channelOrderId,String userChannel) {
		return saleRecordDAO.getByChannelOrderid(channelOrderId,userChannel);
	}
	
	/**
	 * 得到所有订单数量
	 * @return
	 */
	public long getCount() {
		return saleRecordDAO.getCount();
	}
	
	/**
	 * 分页获得玩家成功订单
	 * @param passportId
	 * @param start
	 * @param length
	 * @return
	 */
	public List getUserSuccessSaleRecords(long passportId, int start, int length) {
		return saleRecordDAO.getUserSuccPageRows(passportId, start, length);
	}
	
	/**
	 * 获得用户没有response的订单
	 * @param passportId
	 * @return
	 */
	public List getUserUnbackedSaleRecords(long passportId) {
		return saleRecordDAO.getUserUnbackedPageRows(passportId);
	}
	
	/**
	 * 分页获得用户订单
	 * @param passportId
	 * @param start
	 * @param length
	 * @return
	 */
	public List getUserSaleRecords(long passportId, int start, int length) {
		return saleRecordDAO.getUserPageRows(passportId, start, length);
	}
	
	/**
	 * 获得用户所有订单数量
	 * @param passportId
	 * @return
	 */
	public long getUserSaleRecordCount(long passportId) {
		return saleRecordDAO.getCount(passportId);
	}
	
	/**
	 * 分页获得订单
	 * @param start
	 * @param length
	 * @return
	 */
	public List getSaleRecords(int start, int length) {
		return saleRecordDAO.getPageRows(start, length);
	}
	
	/**
	 * 获得成功充值但是没有通知服务器的订单
	 * @return
	 */
	public List getSuccAndUnnotifiedSaleRecords() {
		return saleRecordDAO.getSuccUnnotifiedSaleRecords();
	}
	
	/**
	 * 获得所有失败的订单
	 * 失败的定义为：1-调用响应为失败的， 2-回调为失败的， 3-通知服务器失败的
	 * @return
	 */
	public List getFailedSaleRecords(int start, int len) {
		return saleRecordDAO.getFailedSaleRecords(start, len);
	}
	
	public long getFailedSaleRecordsCount() {
		return saleRecordDAO.getFailedSaleRecordsCount();
	}
	
	/*/**
	 * 更新一个订单
	 * @param d
	 */
/*	public void updateSaleRecord(SaleRecord d) {
		saleRecordDAO.update(d);
		log.warn("[更新订单] " + d.getLogStr());
	}*/
	
	
	public void updateSaleRecord(SaleRecord d,String fieldName) {
		saleRecordDAO.update(d,fieldName);
		log.warn("[更新订单单个字段] [字段名:"+fieldName+"] " + d.getLogStr());
	}
	
	public void batch_updateField(SaleRecord saleRecord,List<String> fieldNames)
	{
		try {
			saleRecordDAO.batch_updateField(saleRecord, fieldNames);
		} catch (Exception e) {
			log.error("[批量更新字段] [失败] ",e);
		}
	}
	

	public SaleRecordDAO getSaleRecordDAO() {
		return saleRecordDAO;
	}

	public void setSaleRecordDAO(SaleRecordDAO saleRecordDAO) {
		this.saleRecordDAO = saleRecordDAO;
	}
	
	public SimpleEntityManager<SaleRecord> getEm()
	{
		return this.saleRecordDAO.getEm();
	}
	
	//根据卡号和充值类型统计订单
	public long getCountForCardNoAndPayType(String cardNo,String payType) throws Exception
	{
		long count = this.saleRecordDAO.count(" mediumInfo='"+cardNo+"' and savingMedium='"+payType+"'" );
		return count;
	}
	
	public void update(SaleRecord order) throws Exception
	{
		this.saleRecordDAO.update(order);
	}
	
	//根据cardNo和充值类型查询订单
	public List<SaleRecord> getSaleRecordForCardNoAndPayType(String cardNo,String payType)
	{	
		long startTime = System.currentTimeMillis();
		try {
			List<SaleRecord> lst = this.saleRecordDAO.queryForWhere(" mediumInfo='"+cardNo+"' and savingMedium='"+payType+"'");
			if(lst != null)
			{
				if(log.isInfoEnabled())
					log.info("[根据充值卡号和充值类型查询订单] [成功] [充值卡号:"+cardNo+"] [充值类型:"+payType+"] [结果集大小:"+lst.size()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				log.error("[根据充值卡号和充值类型查询订单] [失败] [充值卡号:"+cardNo+"] [充值类型:"+payType+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return lst; 
		} catch (Exception e) {
			log.error("[根据充值卡号和充值类型查询订单] [失败] [充值卡号:"+cardNo+"] [充值类型:"+payType+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		}
	}
	//根据条件查询订单
	public List<SaleRecord> getSaleRecordsByCondition(String whereSql)
	{	
		long startTime = System.currentTimeMillis();
		try {
			List<SaleRecord> lst = this.saleRecordDAO.queryForWhere(whereSql);
			if(lst != null)
			{
				if(log.isInfoEnabled())
					log.info("[根据条件查询订单] [成功] [查询条件:"+whereSql+"] [结果集大小:"+lst.size()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				log.error("[根据条件查询订单] [失败] [查询条件:"+whereSql+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return lst; 
		} catch (Exception e) {
			log.error("[根据条件查询订单] [失败] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		}
	}
	
	/**
	 * 获得玩家挂银锭单但是未交易的数量
	 * @param deviceCode
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public int getNoSaledCount4Silver(long playerId) throws Exception {
		Object[] paramvalues = new Object[]{SaleRecord.RESPONSE_NOBACK,SaleRecord.RESPONSE_SHIFT,SaleRecord.SILVER_TRADE,playerId};
		int count = (int)this.saleRecordDAO.count("(responseResult = ? or responseResult = ?)   and tradeType = ? and sellPlayerId=?",paramvalues);
		return count;
	}
	
	/**
	 * 获得玩家挂物品单但是未交易的数量
	 * @param deviceCode
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public int getNoSaledCount4Article(long playerId) throws Exception {
		Object[] paramvalues = new Object[]{SaleRecord.RESPONSE_NOBACK,SaleRecord.RESPONSE_SHIFT,SaleRecord.ARTICLE_TRADE,playerId};
		
		int count = (int)this.saleRecordDAO.count("(responseResult = ? or responseResult = ?)   and tradeType = ? and sellPlayerId=?",paramvalues);
		return count;
	}
	
	/**
	 * 获得设备今日的充值总额
	 * @param deviceCode
	 * @return
	 */
	public long getDeviceTodaySavingAmount(String deviceCode) {
		Calendar cal = Calendar.getInstance();
		long end = cal.getTimeInMillis();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		long start = cal.getTimeInMillis();
		
		try {
			List<SaleRecord> list = saleRecordDAO.queryForWhere(" notifySucc='T' and deviceCode like '"+deviceCode+"%' and createTime>="+start+" and createTime<" + end);
			long amount = 0;
			if(list != null) {
				for(SaleRecord order : list) {
					amount  += order.getPayMoney();
				}
			}
			return amount;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("查询设备今日充值时发生异常：deviceCode=("+deviceCode+")", e);
		}
		return 0;
	}

	@Override
	public void proc(Event evt) {
		// TODO Auto-generated method stub
		try {
			switch (evt.id) {
			case Event.SERVER_HOUR_CHANGE:	
				this.cleanTempSaleInfo();
				break;
			}
		} catch (Exception e) {
			log.warn("[清理订单信息临时缓存] [proc]", e);
		}
	}

	@Override
	public void doReg() {
		// TODO Auto-generated method stub
		EventRouter.register(Event.SERVER_HOUR_CHANGE, this);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
