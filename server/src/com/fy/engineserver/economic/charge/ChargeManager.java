package com.fy.engineserver.economic.charge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.CheckAttribute;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.ArticleProperty;
import com.fy.engineserver.datasource.article.data.props.PackageProps;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.message.CHARGE_LIST_REQ;
import com.fy.engineserver.message.CHARGE_LIST_RES;
import com.fy.engineserver.message.DAY_PACKAGE_OF_RMB_REQ;
import com.fy.engineserver.message.DAY_PACKAGE_OF_RMB_RES;
import com.fy.engineserver.message.FIRST_CHARGE_REQ;
import com.fy.engineserver.message.FIRST_CHARGE_RES;
import com.fy.engineserver.message.FIRST_CHARGE_STATE_REQ;
import com.fy.engineserver.message.FIRST_CHARGE_STATE_RES;
import com.fy.engineserver.message.GET_LOGIN_REWARD_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.LOGIN_REWARD_REQ;
import com.fy.engineserver.message.LOGIN_REWARD_RES;
import com.fy.engineserver.message.MONTH_CARD_FIRST_PAGE_REQ;
import com.fy.engineserver.message.MONTH_CARD_FIRST_PAGE_RES;
import com.fy.engineserver.message.NOTICE_BUY_SUCCESS_RES;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.TimeTool.TimeDistance;
import com.fy.engineserver.util.console.ChangeAble;
import com.fy.engineserver.util.console.MConsole;
import com.fy.engineserver.util.console.MConsoleManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

/**
 * 充值方式配置
 * 
 * 
 */
@CheckAttribute("充值方式配置")
public class ChargeManager implements MConsole {
	/** 各种充值方式 */
	@CheckAttribute("各种充值方式")
	public static final String[] chargeModes = { "移动充值卡", "联通一卡付", "电信充值卡", "盛大卡", "征途卡", "骏网一卡通", "久游卡", "网易卡", "完美卡", "搜狐卡", "Q币", "腾讯神州付", "91豆", "应用豆", "UCWap支付", "乐逗", "APPSTORE", "UCSDK充值", "支付宝WAP充值", "财付通WAP充值", "PP易宝充值卡", "PP移动充值卡", "爱贝充值", "宝币", "同步推充值", "华为SDK充值","Wap支付宝" };

	@CheckAttribute(value = "各个渠道充值方式列表", des = "<渠道名字,充值列表>")
	private HashMap<String, List<ChargeMode>> channelChargeModes = new HashMap<String, List<ChargeMode>>();
	@CheckAttribute(value = "金额配置列表", des = "<ID,金额配置>")
	private HashMap<String, ChargeMoneyConfigure> chargeMoneyConfigures = new HashMap<String, ChargeMoneyConfigure>();
	@CheckAttribute(value = "渠道映射", des = "<渠道,映射成渠道>")
	private HashMap<String, String> channelMapped = new HashMap<String, String>();
	@CheckAttribute(value = "白名单路径")
	private String diskCachePath;
	@CheckAttribute(value = "屏蔽渠道")
	private String diskCachePathCloseChannel;
	private DiskCache diskCache = null;
	private DiskCache diskCacheCloseChannel = null;

	@ChangeAble("是否使用新的充值接口")
	public static boolean useNewChargeInterface = true;

	public static String chargeWhiteListKey = "chargeWhiteList";
	public static String closeChannelKey = "closeChannelList";

	private final static String DEFAULT_CHANNEL = "YOUAI_XUNXIAN";
	private final static String DEFAULT_WIN8_CHANNEL = "WIN8STORE_MIESHI";
	private final static String DEFAULT_WP8_CHANNEL = "WP8_MIESHI";

	public static List<String> allowFenList = new ArrayList<String>();

	/** 所有特殊渠道的特殊充值方式 */
	public static HashMap<String, List<ChargeMode>> specialChargeModeMap = new HashMap<String, List<ChargeMode>>();

	public static Logger logger = LoggerFactory.getLogger(ChargeManager.class);
	
	private Map<Integer,CardConfig> cards = new HashMap<Integer,CardConfig>();

	private static ChargeManager instance;

	public static ChargeManager getInstance() {
		return instance;
	}
	
	//网关boss迁移，充值屏蔽渠道
	public List<String> limitChannelChargeList = new ArrayList<String>();

	public List<SpecialConfig> specialConfigList = new ArrayList<SpecialConfig>();

	static {
		allowFenList.add("QQ充值");
		allowFenList.add("天猫直充");
		allowFenList.add("昆仑Android");
		allowFenList.add("移动短信");

		allowFenList.add("KunlunAppStore");
		allowFenList.add("MalaiAppstore");
		allowFenList.add("MalaiAndroid");

		allowFenList.add("KoreaT-STORE");
		allowFenList.add("KoreaKT");
		allowFenList.add("KoreaAppstore");
		allowFenList.add("KoreaGoogleplay");
		allowFenList.add("KoreaNA");
		
	}
	/** 配置文件 */
	private String filePath;
	private String fileActivity;

	public static String specialConIndex = "specialCon_";

	private void loadFile() throws Throwable {
		File file = new File(getFilePath());
		if (!file.exists()) {
			throw new FileNotFoundException(getFilePath());
		}
		
		InputStream is = new FileInputStream(file);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);

		HSSFSheet sheet = workbook.getSheetAt(0);// 所有充值通道
		int maxRow = sheet.getPhysicalNumberOfRows();

		for (int i = 1; i < maxRow; i++) {
			try {
				HSSFRow row = sheet.getRow(i);
				List<ChargeRatio> chargeRatios = new ArrayList<ChargeRatio>();
				int index = 0;
				HSSFCell cell = null;
				String userChannel = row.getCell(index++).getStringCellValue().trim();
				String modeName = row.getCell(index++).getStringCellValue().trim();
				String modeShowName = row.getCell(index++).getStringCellValue().trim();
				cell = row.getCell(index++);
				if (cell != null) {
					String value = cell.getStringCellValue();
					if (value != null && !"".equals(value.trim())) {
						value = value.trim();
						String[] subs = value.split("\\|");
						for (String sub : subs) {
							String[] temp = sub.split(",");
							String[] parms = temp[0].split("-");
							double ratio = Double.valueOf(temp[1]);
							int min = Integer.valueOf(parms[0]);
							int max = Integer.valueOf(parms[1]);
							ChargeRatio chargeRatio = new ChargeRatio(min, max, ratio);
							chargeRatios.add(chargeRatio);
						}
					}
				}
				String topDescription = row.getCell(index++).getStringCellValue();
				String footDescription = row.getCell(index++).getStringCellValue();
				long now = SystemTime.currentTimeMillis();
				// if (!sp_serverName.contains(GameConstants.getInstance().getServerName()) && now < sp_end) {
				// footDescription += ".3月29日—4月6日，累积充值达98、498、998、1998、4998、9998、19998元，拿七重大礼，重重逆天，一步登仙！";
				// }
				// for (SpecialConfig config : specialConfigList) {
				// CompoundReturn cr = config.getSpacialConfig();
				// if (cr != null && cr.getBooleanValue()) {
				// footDescription += cr.getStringValue();
				// break;
				// }
				// }
				String buttonText = row.getCell(index++).getStringCellValue();
				int showOrder = (int) row.getCell(index++).getNumericCellValue();
				String[] moneyConfigureIds = StringTool.string2Array(row.getCell(index++).getStringCellValue(), ",", String.class);
				Integer[] showConfiguresLevelLimit = null;
				cell = row.getCell(index++);
				if (cell != null && cell.getStringCellValue() != null && !"".equals(cell.getStringCellValue().trim())) {
					showConfiguresLevelLimit = StringTool.string2Array(cell.getStringCellValue(), ",", Integer.class);
					if (showConfiguresLevelLimit != null && showConfiguresLevelLimit.length != moneyConfigureIds.length) {
						throw new IllegalStateException("[充值金额个等级不匹配] [充值方式:" + modeName + "] [渠道:" + userChannel + "]");
					} else {
						logger.warn("充值方式:" + modeName + ",[充值金额:" + Arrays.toString(moneyConfigureIds) + "],金额等级限制:" + Arrays.toString(showConfiguresLevelLimit));
					}
				}
				ChargeMode createEmptyChargeMode = ChargeModeFactory.getInstance().createEmptyChargeMode(modeName);

				if (createEmptyChargeMode == null) {
					String notice = "[ERROR] [充值方式配置错误] [文件:" + getFilePath() + "] [行数:" + i + "] [充值方式不存在:" + modeName + "]";
					logger.error(notice);
					System.out.println(notice);
					continue;
				}

				String[] chargeTexts = null;
				cell = row.getCell(index++);
				if (cell != null) {
					chargeTexts = StringTool.string2Array(cell.getStringCellValue(), ",", String.class);
				} else {
					chargeTexts = new String[0];
				}
				cell = row.getCell(index++);
				String particularDes = "";
				if (cell != null) {
					particularDes = cell.getStringCellValue();
				}
				createEmptyChargeMode.setAllParm(userChannel, modeShowName, topDescription, footDescription, buttonText, showOrder, moneyConfigureIds, chargeTexts, particularDes, chargeRatios, showConfiguresLevelLimit);
				if (!channelChargeModes.containsKey(createEmptyChargeMode.getUserChannel())) {
					channelChargeModes.put(createEmptyChargeMode.getUserChannel(), new ArrayList<ChargeMode>());
				}
				channelChargeModes.get(createEmptyChargeMode.getUserChannel()).add(createEmptyChargeMode);
				logger.warn("[系统启动] [充值方式配置] [" + createEmptyChargeMode.toString() + "]");
			} catch (Exception e) {
				logger.error("行数" + i, e);
				throw e;
			}
		}
		logger.warn("----------------------------------------------------------------------------------------------------------------");
		logger.warn("----------------------------------------------------------------------------------------------------------------");
		sheet = workbook.getSheetAt(1);// 所有充值金额
		maxRow = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < maxRow; i++) {
			try {
				HSSFRow row = sheet.getRow(i);
				int index = 0;
				String id = row.getCell(index++).getStringCellValue();
				long denomination = (long) row.getCell(index++).getNumericCellValue();
				int type = (int) row.getCell(index++).getNumericCellValue();
				String showText = row.getCell(index++).getStringCellValue();
				String description = row.getCell(index++).getStringCellValue();
				HSSFCell cell = row.getCell(index++);
				String specialCon = "";
				if (cell != null) {
					try {
						specialCon = cell.getStringCellValue().trim();
						if (specialCon.indexOf(specialConIndex) == 0) {
							specialCon = specialCon.substring(specialConIndex.length(), specialCon.length());
						}
					} catch (Exception e) {
						specialCon = String.valueOf((int) cell.getNumericCellValue());
					}
				}
				String backageIcon = row.getCell(index++).getStringCellValue();
				String short_des = row.getCell(index++).getStringCellValue();
				ChargeMoneyConfigure moneyConfigure = new ChargeMoneyConfigure(id, denomination, type, showText, description, specialCon);
				moneyConfigure.setBackageIcon(backageIcon);
				moneyConfigure.setShort_des(short_des);
				chargeMoneyConfigures.put(moneyConfigure.getId(), moneyConfigure);
				logger.warn("[系统启动] [充值金额配置] [" + moneyConfigure.toString() + "]");
			} catch (Exception e) {
				logger.warn("[系统启动] [充值金额配置] [解析异常] [" + i + "行]", e);
			}
		}
		logger.warn("----------------------------------------------------------------------------------------------------------------");
		logger.warn("----------------------------------------------------------------------------------------------------------------");
		initAllmode();
		// 渠道映射
		sheet = workbook.getSheetAt(2);// 所有充值金额
		maxRow = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < maxRow; i++) {
			HSSFRow row = sheet.getRow(i);
			String channel = row.getCell(0).getStringCellValue();
			String asChannel = row.getCell(1).getStringCellValue();
			channelMapped.put(channel, asChannel);
			logger.warn("[系统启动] [渠道映射] [" + channel + "--> " + asChannel + "]");
		}
		
		//月卡配置
		sheet = workbook.getSheetAt(3);
		initMonthCardConfig(sheet);
		

		// 配置特殊渠道
		specialChargeModeMap.put("YOUAI_XUNXIAN", new ArrayList<ChargeMode>());
		specialChargeModeMap.put("APPSTOREGUOJI_MIESHI", new ArrayList<ChargeMode>());
		specialChargeModeMap.put("APPSTORE_XUNXIAN", new ArrayList<ChargeMode>());
		specialChargeModeMap.put("APPSTORE2014_MIESHI", new ArrayList<ChargeMode>());

		specialChargeModeMap.get("YOUAI_XUNXIAN").add(getChargeModeByChannelAndModeName("SPECIAL_MIESHI", "天猫直充"));
		specialChargeModeMap.get("APPSTOREGUOJI_MIESHI").add(getChargeModeByChannelAndModeName("SPECIAL_MIESHI", "天猫直充"));
		specialChargeModeMap.get("APPSTORE_XUNXIAN").add(getChargeModeByChannelAndModeName("SPECIAL_MIESHI", "天猫直充"));
		specialChargeModeMap.get("APPSTORE2014_MIESHI").add(getChargeModeByChannelAndModeName("SPECIAL_MIESHI", "天猫直充"));
	}
	
	
	public void initMonthCardConfig(HSSFSheet sheet){
		for (int i = 1; i < 4; i++) {
			HSSFRow row = sheet.getRow(i);
			int cardId = (int) row.getCell(0).getNumericCellValue();
			String cardName = row.getCell(1).getStringCellValue();
			int chargeMoney = (int) row.getCell(2).getNumericCellValue();
			String icon = row.getCell(3).getStringCellValue();
			int[] ids = ReadFileTool.getIntArrByString(row, 4, logger, ",");
			String cardInfo = ReadFileTool.getString(row, 5, logger);
			
			CardConfig config = new CardConfig(cardId, cardName, chargeMoney, icon, ids, cardInfo);
			cards.put(cardId, config);
			logger.warn("[系统启动] [月卡配置] [" + config + "]");
		}
	}
	
	public String getFileActivity() {
		return fileActivity;
	}


	public void setFileActivity(String fileActivity) {
		this.fileActivity = fileActivity;
	}


	private Map<String,ChargeCardConfig> chargeActivitys = new HashMap<String,ChargeCardConfig>();
	
	public Map<String,ChargeCardConfig> getActivityy(){
		return chargeActivitys;
	}
	
	public void clearData(){
		if(chargeActivitys != null && chargeActivitys.size() > 0){
			Iterator<ChargeCardConfig> it = chargeActivitys.values().iterator();
			while(it.hasNext()){
				ChargeCardConfig c = it.next();
				if(c.isEffectDate() == false){
					NewChongZhiActivityManager.instance.cleanFirstChargeData();
					it.remove();
				}
			}
		}
	}
	
	public ChargeCardConfig getEffectConfig(String channel,String chargeId,Player p){
		ChargeCardConfig config = chargeActivitys.get(chargeId);
		if(config != null && config.isEffect(channel)){
			return config;
		}
		return null;
	}
	
	public void initChargeActivity() throws Exception{
		chargeActivitys.clear();
		File file = new File(fileActivity);
		if (!file.exists()) {
			throw new FileNotFoundException(fileActivity);
		}
		
		InputStream is = new FileInputStream(file);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);

		HSSFSheet sheet = workbook.getSheetAt(0);
		int maxRow = sheet.getPhysicalNumberOfRows();

		for (int i = 1; i < maxRow; i++) {
			try {
				HSSFRow row = sheet.getRow(i);
				int index = 0;
				String chargeId = row.getCell(index++).getStringCellValue().trim();
				
				ChargeMoneyConfigure chargeMoneyConfigure = chargeMoneyConfigures.get(chargeId);
				if (chargeMoneyConfigure == null) {
					throw new Exception("[充值活动加载] [异常：不存在的充值金额ID:"+chargeId+"]");
				}
				
				int chargeType = (int) row.getCell(index++).getNumericCellValue();
				int activityId = (int) row.getCell(index++).getNumericCellValue();
				String activtyInfo = row.getCell(index++).getStringCellValue().trim();
				int giveType = (int) row.getCell(index++).getNumericCellValue();
				long giveValue = (long) row.getCell(index++).getNumericCellValue();
				String tableHead = row.getCell(index++).getStringCellValue().trim();
				String startTime = row.getCell(index++).getStringCellValue().trim();
				String endTime = row.getCell(index++).getStringCellValue().trim();
				String serverNames = row.getCell(index)!=null?row.getCell(index).getStringCellValue().trim():"";
				index ++;
				String channelNames = row.getCell(index)!=null? row.getCell(index).getStringCellValue().trim() : "";
				
				ChargeCardConfig config = new ChargeCardConfig();
				config.setId(chargeId);
				config.setChargeType(chargeType);
				config.setActivityType(activityId);
				config.setIconOrDesc(activtyInfo);
				config.setGiveType(giveType);
				config.setGiveValue(giveValue);
				config.setTableHead(tableHead);
				config.setStartTime(TimeTool.formatter.varChar19.parse(startTime));
				config.setEndTime(TimeTool.formatter.varChar19.parse(endTime));
				int yuan = (int)chargeMoneyConfigure.getDenomination()/100;
				if(yuan <= 0){
					throw new Exception("[充值活动加载] [异常：金额错误:"+chargeId+"]");
				}
				config.setSilverInfo("可获得："+yuan+"元");
				
				List<String> openS = new ArrayList<String>();
				List<String> limitC = new ArrayList<String>();
				
				if(serverNames != null && !serverNames.isEmpty()){
					String [] ss = serverNames.split(",");
					if(ss != null && ss.length > 0){
						for(String s : ss){
							if(s != null){
								openS.add(s);
							}
						}
					}
				}
				
				if(channelNames != null && !channelNames.isEmpty()){
					String [] ss = channelNames.split(",");
					if(ss != null && ss.length > 0){
						for(String s : ss){
							if(s != null){
								limitC.add(s);
							}
						}
					}
				}
				config.setOpenServers(openS);
				config.setOpenChannel(limitC);
				chargeActivitys.put(chargeId,config);
				logger.warn("[系统启动] [充值活动加载] [活动数:"+chargeActivitys.size()+"] [" + config + "]");
			} catch (Exception e) {
				logger.error("[系统启动] [充值活动加载] [异常] [行数" + i+ "]", e);
				throw e;
			}
		}
	}
	
	/**
	 * 为了减少配置
	 * 修改渠道名字,多个子渠道使用母渠道的充值方式
	 * @param userChannel
	 * @return
	 */
	public String modifyChannelName(String userChannel) {
		if (channelMapped != null) {
			for (Iterator<String> itor = channelMapped.keySet().iterator(); itor.hasNext();) {
				String channel = itor.next();
				if (channel != null) {
					if (channel.equalsIgnoreCase(userChannel)) {
						if (logger.isWarnEnabled()) {
							logger.warn("[修改用户渠道名字] [原渠道:" + userChannel + "] [在映射列表中] [修改为:" + channelMapped.get(channel) + "]");
						}
						return channelMapped.get(channel);
					}
				}
			}
		}
		return userChannel;
	}

	public List<ChargeMode> getChannelChargeModes(String channelName) {
		List<ChargeMode> list = null;
		channelName = modifyChannelName(channelName);

		for (Iterator<String> itor = channelChargeModes.keySet().iterator(); itor.hasNext();) {
			String channel = itor.next();
			if (channel.equalsIgnoreCase(channelName)) {
				list = channelChargeModes.get(channel);
				break;
			}
		}

		if (list == null) {
			String defaultChannel = DEFAULT_CHANNEL;
			if (channelName != null && channelName.toLowerCase().contains("win8")) {
				defaultChannel = DEFAULT_WIN8_CHANNEL;

			}
			if (channelName != null && channelName.toLowerCase().contains("wp8")) {
				defaultChannel = DEFAULT_WP8_CHANNEL;
				
			}

			if (logger.isWarnEnabled()) {
				logger.warn("[获取渠道的充值列表] [未配置的渠道:" + channelName + "] [使用默认的充值渠道:" + defaultChannel + "]");
			}
			for (Iterator<String> itor = channelChargeModes.keySet().iterator(); itor.hasNext();) {
				String channel = itor.next();
				if (channel.equalsIgnoreCase(defaultChannel)) {
					list = channelChargeModes.get(channel);
					break;
				}
			}
			if (logger.isWarnEnabled()) {
				logger.warn("[获取渠道的充值列表] [未配置的渠道:" + channelName + "] [使用默认的充值渠道:" + defaultChannel + "] [默认的充值方式个数:" + (list == null ? "--" : list.size()) + "]");
			}
		}
		return list;
	}

	// 是否支持最小单位是分的充值
	public final boolean allowFen(String chargeMode) {
		for (int i = 0; i < allowFenList.size(); i++) {
			String modeName = allowFenList.get(i);
			if (modeName != null) {
				if (modeName.equalsIgnoreCase(chargeMode)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获得特殊的充值方式,只在充值回调的时候用
	 * @param channel这个channel是modified的名字
	 * @return
	 */
	public List<ChargeMode> getSpecialChargeMode(String channel) {
		channel = modifyChannelName(channel);

		if (logger.isWarnEnabled()) {
			logger.warn("[要获取特殊的充值方式] [渠道:" + channel + "]");
		}
		List<ChargeMode> list = specialChargeModeMap.get(channel);
		// if (channel.equalsIgnoreCase("APPSTORE")) {
		// List<ChargeMode> list = new ArrayList<ChargeMode>();
		// list.add(getChargeModeByChannelAndModeName("SPECIAL_MIESHI", "天猫直充"));
		// if (logger.isWarnEnabled()) {
		// logger.warn("[要获取特殊的充值方式] [渠道:" + channel + "] [是APPSTORE] [数量:" + list.size() + "]");
		// }
		// return list;
		// }
		// if (channel.equalsIgnoreCase("YOUAI_XUNXIAN")) {
		// List<ChargeMode> list = new ArrayList<ChargeMode>();
		// list.add(getChargeModeByChannelAndModeName("SPECIAL_MIESHI", "天猫直充"));
		// if (logger.isWarnEnabled()) {
		// logger.warn("[要获取特殊的充值方式] [渠道:" + channel + "] [是YOUAI_XUNXIAN] [数量:" + list.size() + "]");
		// }
		// return list;
		// }
		if (logger.isWarnEnabled()) {
			logger.warn("[要获取特殊的充值方式] [渠道:" + channel + "] [列表:" + (list == null ? "没找到" : (list.size() + "个")) + "]");
		}
		return list;
	}

	private void initAllmode() {
		for (Iterator<String> itor = channelChargeModes.keySet().iterator(); itor.hasNext();) {
			String userChannel = itor.next();
			List<ChargeMode> chargeModes = channelChargeModes.get(userChannel);
			if (chargeModes == null || chargeModes.size() == 0) {
				logger.error("[加载充值] [错误] [渠道:" + userChannel + "] [没有任何充值方式:" + chargeModes + "]");
				continue;
			}
			for (ChargeMode chargeMode : chargeModes) {
				if (chargeMode.getMoneyConfigureIds() == null || chargeMode.getMoneyConfigureIds().length == 0) {
					logger.error("[加载充值] [错误] [渠道:" + userChannel + "] [充值方式:" + chargeMode.getModeName() + "] [没有任何充值金额:" + chargeMode.getMoneyConfigureIds() + "]");
					continue;
				}
				for (String id : chargeMode.getMoneyConfigureIds()) {
					ChargeMoneyConfigure chargeMoneyConfigure = chargeMoneyConfigures.get(id);
					if (chargeMoneyConfigure == null) {
						logger.error("[加载充值] [错误] [渠道:" + userChannel + "] [充值方式:" + chargeMode.getModeName() + "] [不存在的充值金额ID:" + id + "]");
						continue;
					}
					chargeMode.getMoneyConfigures().add(chargeMoneyConfigure);
					chargeMoneyConfigure.getChargeMode().add(chargeMode);
				}
			}
			Collections.sort(chargeModes, order);
			channelChargeModes.put(userChannel, chargeModes);
		}
	}

	public ChargeMode getChargeModeByChannelAndModeName(String channelName, String modeName) {
		List<ChargeMode> list = getChannelChargeModes(channelName);
		if (list == null) {
			return null;
		}
		for (ChargeMode temp : list) {
			if (temp.getModeName().equalsIgnoreCase(modeName)) {
				return temp;
			}
		}
		return null;
	}

	public Comparator<ChargeMode> order = new Comparator<ChargeMode>() {

		@Override
		public int compare(ChargeMode o1, ChargeMode o2) {
			return o1.getShowOrder() - o2.getShowOrder();
		}
	};

	private HashMap<String, List<ChargeMode>> getChannelChargeModes() {
		return channelChargeModes;
	}

	public void setChannelChargeModes(HashMap<String, List<ChargeMode>> channelChargeModes) {
		this.channelChargeModes = channelChargeModes;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public HashMap<String, ChargeMoneyConfigure> getChargeMoneyConfigures() {
		return chargeMoneyConfigures;
	}

	public void setChargeMoneyConfigures(HashMap<String, ChargeMoneyConfigure> chargeMoneyConfigures) {
		this.chargeMoneyConfigures = chargeMoneyConfigures;
	}

	public String getDiskCachePath() {
		return diskCachePath;
	}

	public void setDiskCachePath(String diskCachePath) {
		this.diskCachePath = diskCachePath;
	}

	public String getDiskCachePathCloseChannel() {
		return diskCachePathCloseChannel;
	}

	public void setDiskCachePathCloseChannel(String diskCachePathCloseChannel) {
		this.diskCachePathCloseChannel = diskCachePathCloseChannel;
	}

	/**
	 * 得到diskcache中所有的充值白名单
	 * @return
	 */
	public List<String> getChargeWhiteList() {
		return (List<String>) diskCache.get(chargeWhiteListKey);
	}

	/**
	 * 新增一个白名单
	 * @param userName
	 */
	public void addChargeWhiteList(String userName) {
		List<String> list = getChargeWhiteList();
		if (list == null) {
			list = new ArrayList<String>();
		}
		if (!list.contains(userName)) {
			list.add(userName);
			diskCache.put(chargeWhiteListKey, (Serializable) list);
			if (logger.isWarnEnabled()) {
				logger.warn("[新增一个白名单] [" + userName + "]");
			}
		}
	}
	
	public void addLimitChargeChannel(String channelName){
		limitChannelChargeList.add(channelName);
		if (logger.isWarnEnabled()) {
			logger.warn("[新增一个限制充值渠道] [增加的渠道:" + channelName + "] [所有限制渠道:"+limitChannelChargeList+"]");
		}
	}
	
	public void removeLimitChargeChannel(String channelName){
		if(limitChannelChargeList.contains(channelName)){
			limitChannelChargeList.remove(channelName);
		}
		if (logger.isWarnEnabled()) {
			logger.warn("[新增一个限制充值渠道] [删除的渠道:" + channelName + "] [所有限制渠道:"+limitChannelChargeList+"]");
		}
	}

	/**
	 * 移除一个白名单
	 * @param userName
	 */
	public void removeChargeWhiteList(String userName) {
		List<String> list = getChargeWhiteList();
		if (list == null) {
			return;
		}
		if (list.contains(userName)) {
			list.remove(userName);
			diskCache.put(chargeWhiteListKey, (Serializable) list);
			if (logger.isWarnEnabled()) {
				logger.warn("[移除一个白名单] [" + userName + "]");
			}
		}
	}

	public boolean inWhiteList(String userName) {
		List<String> list = getChargeWhiteList();
		if (list == null) {
			return false;
		}
		return list.contains(userName);
	}

	/**
	 * 得到diskcache中所有的充值屏蔽渠道
	 * @return
	 */
	public List<String> getCloseChannelList() {
		return (List<String>) diskCacheCloseChannel.get(closeChannelKey);
	}

	/**
	 * 新增一个白名单
	 * @param channelName
	 */
	public void addCloseChannelList(String channelName) {
		List<String> list = getCloseChannelList();
		if (list == null) {
			list = new ArrayList<String>();
		}
		if (!list.contains(channelName)) {
			list.add(channelName);
			diskCacheCloseChannel.put(closeChannelKey, (Serializable) list);
			if (logger.isWarnEnabled()) {
				logger.warn("[新增一个屏蔽渠道] [" + channelName + "]");
			}
		}
	}

	/**
	 * 移除一个屏蔽渠道
	 * @param channelName
	 */
	public void removeCloseChannelList(String channelName) {
		List<String> list = getCloseChannelList();
		if (list == null) {
			return;
		}
		if (list.contains(channelName)) {
			list.remove(channelName);
			diskCacheCloseChannel.put(closeChannelKey, (Serializable) list);
			if (logger.isWarnEnabled()) {
				logger.warn("[移除一个屏蔽渠道] [" + channelName + "]");
			}
		}
	}

	/**
	 * 是否在屏蔽渠道中
	 * @param channelName
	 * @return
	 */
	public boolean inCloseChanneList(String channelName) {
		List<String> list = getCloseChannelList();
		if (list == null) {
			return false;
		}
		for (String close : list) {
			if (close != null && close.equalsIgnoreCase(channelName)) {
				return true;
			}
		}
		return false;
	}

	public void init() throws Throwable {
		initSpecialConfig();
		loadFile();
		diskCache = new DefaultDiskCache(new File(diskCachePath), "chargeWhiteList", 1000L * 60 * 60 * 24 * 365 * 10);// 10年
		diskCacheCloseChannel = new DefaultDiskCache(new File(diskCachePathCloseChannel), "chargeCloseChannel", 1000L * 60 * 60 * 24 * 365 * 10);// 10年
		instance = this;
		initChargeActivity();
		MConsoleManager.register(this);
		ServiceStartRecord.startLog(this);
	}

	private void initSpecialConfig() {
		long startTime = 0;
		long endTime = 0;
		/**
		 * 测试
		 */
		startTime = TimeTool.formatter.varChar19.parse("2013-06-08 00:00:00");
		endTime = TimeTool.formatter.varChar19.parse("2013-06-13 23:59:59");
		specialConfigList.add(new SpecialConfig(startTime, endTime) {
			// 国服4月活动
			@Override
			public CompoundReturn getSpacialConfig() {
				if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
					long now = SystemTime.currentTimeMillis();
					if (startTime > now || endTime < now) {
						return CompoundReturn.createCompoundReturn().setBooleanValue(false);
					}
					if ("醉倚浮生".equals(GameConstants.getInstance().getServerName()) || "金戈铁马".equals(GameConstants.getInstance().getServerName()) || "落霞水榭".equals(GameConstants.getInstance().getServerName())) {
						return CompoundReturn.createCompoundReturn().setBooleanValue(false);
					}
					return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(1).setStringValue("6月8日0点至6月13日23:59期间，众仙友累积充值10元，就可获赠价值80元的“肉粽礼包”，累积充值98元可获双喜礼包，累积充值196元可再获双喜礼包1个。宠物宝宝、飞行坐骑随便兑换，这你忍的了吗？心动事不迟疑，《飘渺寻仙曲》让你所向披靡。");
				}
				return CompoundReturn.createCompoundReturn().setBooleanValue(false);
			}
		});

	}

	public static void main(String[] args) {
		String specialCon = "specialCon_1234567890123";
		if (specialCon.indexOf(specialConIndex) == 0) {
			specialCon = specialCon.substring(specialConIndex.length(), specialCon.length());
		}
		System.out.println("specialCon:" + specialCon);
	}

	/**
	 * 检查充值活动是否在有效期
	 * @return
	 */
	public List<CompoundReturn> chargeActivitySpecialConfig() {
		long now = System.currentTimeMillis();
		List<CompoundReturn> list = new ArrayList<CompoundReturn>();
		for (SpecialConfig config : specialConfigList) {
			CompoundReturn cr = config.getSpacialConfig();
			if (cr != null && cr.getBooleanValue()) {
				if (config.startTime > now || config.endTime < now) {
					continue;
				}
				// System.out.println("config.startTime:"+config.startTime+"--config.endTime:"+config.endTime+"--youxiao:"+config.getSpacialConfig().getStringValue()+"\n");
				list.add(CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(1).setStringValue(config.getSpacialConfig().getStringValue()));
			}
		}
		return list;
	}
	
	public long oneMonth = 30 * 24 * 60 * 60 * 1000L;
	public long oneDay = 24 * 60 * 60 * 1000L;
	
	public void updateCradReward(Player p){
		int cardIds [] = p.getCardIds();
		long cardEndDate [] = p.getCardEndDate();
		for(int i=0;i<cardIds.length;i++){
			if(cardIds[i] > 0){//充过值
				if(cardEndDate[i] > System.currentTimeMillis()){//有效期内
					handleCardReward(p, cardIds[i],"凌晨更新");
				}
			}
		}
	}
	
	public void handleLoginReward(Player p){
		long now = System.currentTimeMillis();
		int cardIds [] = p.getCardIds();
		long cardEndDate [] = p.getCardEndDate();
		for(int i=0;i<cardIds.length;i++){
			if(cardIds[i] > 0){//充过值
				if(cardEndDate[i] > System.currentTimeMillis()){//有效期内
					long lastTime = p.getLastRewardDate()[i];
					int rewardNums = 0;
					for(int j=0;j<30;j++){
						lastTime += oneDay * j;
						if(lastTime >= now){
							break;
						}
						if(!TimeTool.instance.isSame(lastTime, now, TimeDistance.DAY)){
							rewardNums++;
						}
					}
					if(rewardNums > 0){
						while(rewardNums-- > 0){
							handleCardReward(p, cardIds[i],"登陆补发-"+rewardNums);
						}
					}
				}
			}
		}
	}
	String nbArticle = "乾坤戒指";
	public void handleCardReward(Player player,int id,String reason){
		if(id <= 0 || id > 3){
			return;
		}
		List<ArticleEntity> list = new ArrayList<ArticleEntity>();
		List<String> articleNames = new ArrayList<String>();
		int [] nums = null;
		String title = "";
//		每日绑银500两,每日2张彩世仙符
		if(id == 1){
			nums = new int[]{5,2};
			articleNames.add("银票(10两)");
			articleNames.add("玫瑰花束");
			title = "修真卡每日奖励";
//		每日玫瑰花束,每日蓝色封魔录两张
		}else if(id == 2){
			nums = new int[]{10,1,2,1,5};
			articleNames.add("银票(10两)");
			articleNames.add("玫瑰花束");
			articleNames.add("蓝色封魔录礼包(绑)");
			articleNames.add("圣兽阁体验券(普)");
			articleNames.add("原地复活令");
			title = "渡劫卡每日奖励";
//		每日蓝酒两瓶,每日体力丹3颗,每日常青草2颗	
		}else if(id == 3){
			nums = new int[]{15,2,3,2};
			articleNames.add("银票(10两)");
			articleNames.add("蓝色玉液礼包(绑)");
			articleNames.add("活力丹");
			articleNames.add("坐骑培养膏");
			title = "飞仙卡每日奖励";
		}
		try {
			for(int i=0;i<articleNames.size();i++){
				String name = articleNames.get(i);
				Article a = ArticleManager.getInstance().getArticle(name);
				ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.月卡每日奖励, player, a.getColorType(), nums[i], true);
				list.add(ae);
			}
			MailManager.getInstance().sendMail(player.getId(), list.toArray(new ArticleEntity[]{}), nums, title, title, 0, 0, 0, "月卡每日奖励");
			long lastRewardDate [] = player.getLastRewardDate();
			lastRewardDate[id-1] = System.currentTimeMillis();
			player.setLastRewardDate(lastRewardDate);
			boolean hasNBReward = true;
			for(long d : player.getBuyRecord()){
				if(d <= 0){
					hasNBReward = false;
				}
			}
			if(hasNBReward){
				handleActivityGive(player,4);
				player.setBuyRecord(new long[]{0,0,0});
				String chenhao = "再世真仙";
				Article a = ArticleManager.getInstance().getArticle(nbArticle);
				ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.月卡每日奖励, player, a.getColorType(), 1, true);
				MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{ae}, nums, "开启三大月卡专属特权", "恭喜您获得开启三大月卡专属福利，防爆储物戒，物品放入储物戒内，100%不会掉落", 0, 0, 0, "3种月卡集齐奖励");
				int[] keys = player.getKeyByTitle(chenhao);
				player.addTitle(keys[0], chenhao, keys[1],"",0,chenhao,"","", -1L);
			}
			ActivitySubSystem.logger.warn("[月卡每日奖励] [{}] [成功] [hasNBReward:{}] [id:{}] [{}] [{}]",new Object[]{reason,hasNBReward,id,player.getLogString()});
		} catch (Exception e) {
			e.printStackTrace();
			ActivitySubSystem.logger.warn("[月卡每日奖励] [异常] [id:{}] [{}] [{}]",new Object[]{id,player.getLogString(),e});
		}
	}
	
	public void buyMonthCard(Player p,String chargeId){
		int id = 0;
		if(chargeId.equals("newxunxian_yueka12") || chargeId.equals("MQB12") || 
				chargeId.equals("yxx12") || chargeId.equals("xunxianqingyuan.012") 
				|| chargeId.equals("jiuzhou_yueka12") || chargeId.equals("huo012")||
				chargeId.equals("qileyueka12") || chargeId.equals("huo012_2")||
				chargeId.equals("piaomiaoqu012") || chargeId.equals("xunxian012") || chargeId.equals("jianxia012") || chargeId.equals("huo012_3") || chargeId.equals("huo012_4")){
			id = 1;
		}else if(chargeId.equals("newxunxian_yueka18") || chargeId.equals("MQB18") ||
				chargeId.equals("yxx18")|| chargeId.equals("jiuzhou_yueka18")|| 
				chargeId.equals("huo018") || chargeId.equals("qileyueka18")|| 
				chargeId.equals("huo018_2") || chargeId.equals("xunxian018") || chargeId.equals("jianxia018")|| chargeId.equals("huo018_3") || chargeId.equals("huo018_4")){
			id = 2;
		}else if(chargeId.equals("newxunxian_yueka30") || chargeId.equals("MQB30") || 
				chargeId.equals("yxx30")|| chargeId.equals("jiuzhou_yueka30")||
				chargeId.equals("huo030") || chargeId.equals("qileyueka30")|| 
				chargeId.equals("huo030_2") || chargeId.equals("xunxian030") || chargeId.equals("jianxia030")|| chargeId.equals("huo030_3") || chargeId.equals("huo030_4")){
			id = 3;
		}
		if(id <= 0 || id > 3){
			return;
		}
		
		//1:修真2:渡劫3:飞仙
		//支付成功 TODO
		long [] endDates = p.getCardEndDate();
		int [] ids = p.getCardIds();
		
		//处理购买奖励，凌晨12点自动发奖励
		boolean sendReward = false;
		if(endDates[id - 1] < System.currentTimeMillis()){
			endDates[id - 1] = System.currentTimeMillis();
			sendReward = true;
		}
		
		ids[id - 1] = id;
		endDates[id - 1] += oneMonth;
		p.setCardEndDate(endDates);
		p.setCardIds(ids);
		p.sendError("月卡购买成功");
		
		long [] days = new long[cards.size()];
		int index = 0;
		Iterator<Entry<Integer, CardConfig>> it = cards.entrySet().iterator();
		List<String> xz = new ArrayList<String>();
		List<String> dj = new ArrayList<String>();
		List<String> fx = new ArrayList<String>();
		while(it.hasNext()){
			Entry<Integer, CardConfig> entry = it.next();
			int key = entry.getKey();
			ids[index] = key;
			days[index] = p.getCardEndDate()[index] - System.currentTimeMillis() < 0 ? 0 : p.getCardEndDate()[index] - System.currentTimeMillis();
			CardConfig config = entry.getValue();
			for(int fid : config.getIds()){
				for(CardFunction f : CardFunction.values()){
					if(f.getId() == fid){
						if(index == 0){
							xz.add(f.getName());
						}else if(index == 1){
							dj.add(f.getName());
						}else if(index == 2){
							fx.add(f.getName());
						}
					}
				}
			}
			index++;
		}
		
		if(sendReward){
			String chenhao = "";
			if(id == 1){
				chenhao = "去伪存真";
			}else if(id == 2){
				chenhao = "渡厄了凡";
			}else if(id == 3){
				chenhao = "霞举飞升";
			}
			handleActivityGive(p,id);
			long rs [] = p.getBuyRecord();
			rs[id-1] = System.currentTimeMillis();
			p.setBuyRecord(rs);
			
			int[] keys = p.getKeyByTitle(chenhao);
			p.addTitle(keys[0], chenhao, keys[1],"",0,chenhao,"","", -1L);
			handleCardReward(p, id,"购买月卡");
		}
		p.addMessageToRightBag(new NOTICE_BUY_SUCCESS_RES(GameMessageFactory.nextSequnceNum(), 1, 1));
		logger.warn("[购买月卡] [sendReward:{}] [id:{}] [ids:{}] [dates:{}] [{}]",new Object[]{sendReward,id,Arrays.toString(p.getCardIds()),Arrays.toString(p.getCardEndDate()), p.getLogString()});
	}
	
	public boolean isOpen(){
		return true;
//		if(GameConstants.getInstance().getServerName().contains("混沌起源")){	
//			Calendar cl = Calendar.getInstance();
//			cl.set(Calendar.YEAR,2018);
//			cl.set(Calendar.MONTH,8);
//			cl.set(Calendar.DAY_OF_MONTH,28);
//			cl.set(Calendar.HOUR_OF_DAY,0);
//			cl.set(Calendar.MINUTE,0);
//			cl.set(Calendar.SECOND,0);
//			long startTime = cl.getTimeInMillis();
//			cl.set(Calendar.YEAR,2018);
//			cl.set(Calendar.MONTH,10);
//			cl.set(Calendar.DAY_OF_MONTH,29);
//			cl.set(Calendar.HOUR_OF_DAY,23);
//			cl.set(Calendar.MINUTE,59);
//			cl.set(Calendar.SECOND,59);
//			long endTime = cl.getTimeInMillis();
//			ActivityManagers.logger.warn("[额外送活动] ["+GameConstants.getInstance().getServerName()+"] [当前时间:"+TimeTool.formatter.varChar19.format(SystemTime.currentTimeMillis())+"] [活动开始时间:"+TimeTool.formatter.varChar19.format(startTime)+"] [活动结束时间:"+TimeTool.formatter.varChar19.format(endTime)+"]");
//			return SystemTime.currentTimeMillis() >= startTime && SystemTime.currentTimeMillis() < endTime;
//		}
//		return false;
//		if(GameConstants.getInstance().getServerName().contains("九州天路")){
//			Calendar cl = Calendar.getInstance();
//			cl.set(Calendar.YEAR,2018);
//			cl.set(Calendar.MONTH,9);
//			cl.set(Calendar.DAY_OF_MONTH,20);
//			cl.set(Calendar.HOUR_OF_DAY,23);
//			cl.set(Calendar.MINUTE,59);
//			cl.set(Calendar.SECOND,59);
//			ActivityManagers.logger.warn("[额外送活动] ["+GameConstants.getInstance().getServerName()+"] [当前时间:"+TimeTool.formatter.varChar19.format(SystemTime.currentTimeMillis())+"] [活动结束时间:"+TimeTool.formatter.varChar19.format(cl.getTimeInMillis())+"]");
//			return SystemTime.currentTimeMillis() < cl.getTimeInMillis();
//		}
//		if(GameConstants.getInstance().getServerName().contains("九州仙境")){
//			Calendar cl = Calendar.getInstance();
//			cl.set(Calendar.YEAR,2018);
//			cl.set(Calendar.MONTH,8);
//			cl.set(Calendar.DAY_OF_MONTH,3);
//			cl.set(Calendar.HOUR_OF_DAY,23);
//			cl.set(Calendar.MINUTE,59);
//			cl.set(Calendar.SECOND,59);
//			ActivityManagers.logger.warn("[额外送活动] ["+GameConstants.getInstance().getServerName()+"] [当前时间:"+TimeTool.formatter.varChar19.format(SystemTime.currentTimeMillis())+"] [活动结束时间:"+TimeTool.formatter.varChar19.format(cl.getTimeInMillis())+"]");
//			return SystemTime.currentTimeMillis() < cl.getTimeInMillis();
//		}
//		return false;
	}
	
	/*
	 * type:1:修真2:渡劫3:飞仙4:三卡起开
	 */
	public void handleActivityGive(Player p,int type){
		if(!isOpen()){
			return;
		}
		String name = "工资卡100两";
		String reason = "";
		int num = 0;
		if(type == 1){
			num = 12;
			reason = "修真";
		}else if(type == 2){
			num = 18;
			reason = "渡劫";
		}else if(type == 3){
			num = 30;
			reason = "飞仙";
		}else if(type == 4){
			name = "4级主流宝石袋";
			num = 1;
			reason = "修真,渡劫,飞仙";
		}
		if(num == 0){
			return;
		}
		try {
			Article a = ArticleManager.getInstance().getArticle(name);
			ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a,true,ArticleEntityManager.开服头7日额外送, p, a.getColorType(), num, true);
			MailManager.getInstance().sendMail(p.getId(), new ArticleEntity[]{ae},new int[]{num}, "月卡限时额外送好礼", "开启"+reason+"额外赠送好礼", 0, 0, 0, "月卡额外赠送");
			ActivityManagers.logger.warn("[月卡额外送活动] [成功] ["+reason+"] [赠送:"+name+"] [数量:"+num+"] ["+p.getLogString()+"]");
		} catch (Exception e) {
			e.printStackTrace();
			ActivityManagers.logger.warn("[月卡额外送活动] [异常] [type:"+type+"] ["+p.getLogString()+"]",e);
		}
		
	}
	
	public void handle_LOGIN_REWARD_REQ(LOGIN_REWARD_REQ req ,Player player){
		int [] states = player.getLoginState();
		String [] names = player.getRewardNames();
		String [] icons = new String[names.length];
		long [] aeIds1 = null;
		long [] aeIds2 = null;
		long [] aeIds3 = null;
		long [] aeIds4 = null;
		long [] aeIds5 = null;
		long [] aeIds6 = null;
		long [] aeIds7 = null;
		try {
			for(int i=0;i<names.length;i++){
				PackageProps a = (PackageProps)ArticleManager.getInstance().getArticle(names[i]);
				ArticleProperty [] articleNames = a.getArticleNames();
				icons[i] = a.getIconId();
				if(i==0){
					aeIds1 = new long[articleNames.length];
					for(int j=0;j<articleNames.length;j++){
						ArticleProperty ap = articleNames[j];
						Article article = ArticleManager.getInstance().getArticle(ap.getArticleName());
						ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(ap.getArticleName(), true, ap.getColor());
						if(ae == null){
							ae = ArticleEntityManager.getInstance().createTempEntity(article, true, ArticleEntityManager.七日登录奖励, null, ap.getColor());
						}
						aeIds1[j] = ae.getId();
 					}
				}else if(i==1){
					aeIds2 = new long[articleNames.length];
					for(int j=0;j<articleNames.length;j++){
						ArticleProperty ap = articleNames[j];
						Article article = ArticleManager.getInstance().getArticle(ap.getArticleName());
						ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(ap.getArticleName(), true, ap.getColor());
						if(ae == null){
							ae = ArticleEntityManager.getInstance().createTempEntity(article, true, ArticleEntityManager.七日登录奖励, null, ap.getColor());
						}
						aeIds2[j] = ae.getId();
 					}
				}else if(i==2){
					aeIds3 = new long[articleNames.length];
					for(int j=0;j<articleNames.length;j++){
						ArticleProperty ap = articleNames[j];
						Article article = ArticleManager.getInstance().getArticle(ap.getArticleName());
						ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(ap.getArticleName(), true, ap.getColor());
						if(ae == null){
							ae = ArticleEntityManager.getInstance().createTempEntity(article, true, ArticleEntityManager.七日登录奖励, null, ap.getColor());
						}
						aeIds3[j] = ae.getId();
 					}
				}else if(i==3){
					aeIds4 = new long[articleNames.length];
					for(int j=0;j<articleNames.length;j++){
						ArticleProperty ap = articleNames[j];
						Article article = ArticleManager.getInstance().getArticle(ap.getArticleName());
						ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(ap.getArticleName(), true, ap.getColor());
						if(ae == null){
							ae = ArticleEntityManager.getInstance().createTempEntity(article, true, ArticleEntityManager.七日登录奖励, null, ap.getColor());
						}
						aeIds4[j] = ae.getId();
 					}
				}else if(i==4){
					aeIds5 = new long[articleNames.length];
					for(int j=0;j<articleNames.length;j++){
						ArticleProperty ap = articleNames[j];
						Article article = ArticleManager.getInstance().getArticle(ap.getArticleName());
						ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(ap.getArticleName(), true, ap.getColor());
						if(ae == null){
							ae = ArticleEntityManager.getInstance().createTempEntity(article, true, ArticleEntityManager.七日登录奖励, null, ap.getColor());
						}
						aeIds5[j] = ae.getId();
 					}
				}else if(i==5){
					aeIds6 = new long[articleNames.length];
					for(int j=0;j<articleNames.length;j++){
						ArticleProperty ap = articleNames[j];
						Article article = ArticleManager.getInstance().getArticle(ap.getArticleName());
						ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(ap.getArticleName(), true, ap.getColor());
						if(ae == null){
							ae = ArticleEntityManager.getInstance().createTempEntity(article, true, ArticleEntityManager.七日登录奖励, null, ap.getColor());
						}
						aeIds6[j] = ae.getId();
 					}
				}else if(i==6){
					aeIds7 = new long[articleNames.length];
					for(int j=0;j<articleNames.length;j++){
						ArticleProperty ap = articleNames[j];
						Article article = ArticleManager.getInstance().getArticle(ap.getArticleName());
						ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(ap.getArticleName(), true, ap.getColor());
						if(ae == null){
							ae = ArticleEntityManager.getInstance().createTempEntity(article, true, ArticleEntityManager.七日登录奖励, null, ap.getColor());
						}
						aeIds7[j] = ae.getId();
 					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(aeIds1 == null || aeIds2 == null || aeIds3 == null || aeIds4 == null || aeIds5 == null || aeIds6 == null || aeIds7 == null){
			player.sendError(Translate.七日宝箱配置错误);
			ActivitySubSystem.logger.warn("[七日宝箱] [界面请求] [错误:配置错误] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}]",
					new Object[]{aeIds1,aeIds2,aeIds3, aeIds4,aeIds5,aeIds6,aeIds7,player.getLogString()});
			return;
		}
		LOGIN_REWARD_RES res = new LOGIN_REWARD_RES(req.getSequnceNum(), states, names, icons, aeIds1, aeIds2, aeIds3, aeIds4, aeIds5, aeIds6, aeIds7);
		player.addMessageToRightBag(res);
	}
	
	public void handle_GET_LOGIN_REWARD_REQ(GET_LOGIN_REWARD_REQ req ,Player player){
		player.getLoginReward(req.getDay());
	}
	
	public void handle_FIRST_CHARGE_STATE_REQ(FIRST_CHARGE_STATE_REQ req ,Player player){
//		if(ActivityManager.limitFunctionOfApple){
//			FIRST_CHARGE_STATE_RES res = new FIRST_CHARGE_STATE_RES(req.getSequnceNum(), 1);
//			player.addMessageToRightBag(res);
//			return;
//		}
		FIRST_CHARGE_STATE_RES res = new FIRST_CHARGE_STATE_RES(req.getSequnceNum(), player.isFirstCharge()?1:0);
		player.addMessageToRightBag(res);
	}
	
	public String [][] dayPackageNames = {{"蓝色玉液礼包(绑)","原地复活令","一品修法真丹(初级)","冰棒","冰棒","一元礼包抽奖券"},
										{"蓝色封魔录礼包(绑)","蓝色封魔录礼包(绑)","冰棒","冰棒","原地复活令","三元礼包抽奖券"},
										{"紫色玉液礼包(绑)","一品修法真丹(初级)","冰棒","冰棒","原地复活令","六元礼包抽奖券"}};
	public long [][] dayPackageNums = {{1,1,1,1,1,1},{1,1,1,1,1,1},{1,1,1,1,1,1}};

	public void buyPackageOfDay(Player p,String chargeId){
		String name = "";
		int index = -1;
		if(chargeId.equals("xunxian_day1") || chargeId.equals("MQB1") || chargeId.equals("yxx1") || chargeId.equals("huo01") 
				|| chargeId.equals("xunxianqingyuan.01") || chargeId.equals("jiuzhou_day1") || 
				chargeId.equals("qilemeiri1") || chargeId.equals("huo01_2")  || 
				chargeId.equals("piaomiaoqu01")  || chargeId.equals("xunxian01") || chargeId.equals("jianxia01") || chargeId.equals("huo01_3") || chargeId.equals("huo01_4")){
			name = "1元每日礼包";
			index = 0;
		}else if(chargeId.equals("xunxian_day3") || chargeId.equals("MQB3") || chargeId.equals("yxx3") || chargeId.equals("huo03") 
				|| chargeId.equals("xunxianqingyuan.01") || chargeId.equals("jiuzhou_day3")
				|| chargeId.equals("qilemeiri3") || chargeId.equals("huo03_2")||
				chargeId.equals("piaomiaoqu03") || chargeId.equals("xunxian03") || chargeId.equals("jianxia03") || chargeId.equals("huo03_3") || chargeId.equals("huo03_4")){
			index = 1;
			name = "3元每日礼包";
		}else if(chargeId.equals("xunxian_day6") || chargeId.equals("MQB6") || chargeId.equals("yxx6") || chargeId.equals("huo06") 
				|| chargeId.equals("xunxianqingyuan.01") || chargeId.equals("jiuzhou_day6")
				|| chargeId.equals("qilemeiri6") || chargeId.equals("huo06_2") || 
				chargeId.equals("piaomiaoqu06") || chargeId.equals("xunxian06") || chargeId.equals("jianxia06") || chargeId.equals("huo06_3")|| chargeId.equals("huo06_4")){
			index = 2;
			name = "6元每日礼包";
		}
		int buyNum = 1;
		try {
			long[] lastBuyTime = p.getLastBuyTime();
			int[] buyTimes = p.getBuyTimes();
			lastBuyTime[index] = System.currentTimeMillis();
			buyTimes[index] = buyTimes[index]+1;
			p.setLastBuyTime(lastBuyTime);
			p.setBuyTimes(buyTimes);
			
			if(isOpen()){
				buyNum = 2;
			}
			
			Article article = ArticleManager.getInstance().getArticle(name);
			ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.每日礼包购买, p, article.getColorType(), buyNum, true);
			MailManager.getInstance().sendMail(p.getId(), new ArticleEntity[]{ae},new int[]{buyNum} ,"每日礼包购买", "每日礼包购买", 0, 0, 0, "每日礼包");
			p.sendError("物品已经通过邮件发放，请注意查收");
			p.addMessageToRightBag(new NOTICE_BUY_SUCCESS_RES(GameMessageFactory.nextSequnceNum(), 2, 1));
			logger.warn("[购买每日礼包] [chargeId:"+chargeId+"] [buyNum:"+buyNum+"] [name:"+name+"] ["+p.getLogString()+"]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void handle_DAY_PACKAGE_OF_RMB_REQ(DAY_PACKAGE_OF_RMB_REQ req ,Player player){
		if(!ActivityManager.getInstance().openFunction){
			player.sendError(Translate.此功能暂未开放);
			return;
		}
		String channelName = req.getChannelName();
		if(channelName == null || channelName.isEmpty()){
			player.sendError(Translate.请选择正确的卡+".");
			return;
		}
		
		List<ChargeMode> list = ChargeManager.getInstance().getChannelChargeModes(channelName);
		if (list == null || list.size() == 0) {
			player.sendError(Translate.无匹的充值信息请联系GM);
			ChargeManager.logger.error(player.getLogString() + "[查询充值信息-每日礼包] [异常] [无匹配的充值列表] [渠道:" + channelName + "]");
			return;
		}
		ChargeMode mode = list.get(0);
		if(mode == null){
			player.sendError(Translate.无匹的充值信息请联系GM);
			ChargeManager.logger.error(player.getLogString() + "[查询充值列表-每日礼包] [异常] [无匹配的充值信息2] [渠道:" + channelName + "] [充值列表:"+list.size()+"]");
			return;
		}
		try {
			String modeName = mode.getModeName();
			int[] cardType = {1,2,3};
			long [] ids = {};
			long [] ids2= {};
			long [] ids3= {};
			long [] nums= {};
			long [] mums2= {};
			long [] nums3= {};
			long [] tempIds = {};
			for(int i=0;i<dayPackageNames.length;i++){
				String [] names = dayPackageNames[i];
				tempIds = new long[names.length];
				for(int j=0;j<names.length;j++){
					Article article = ArticleManager.getInstance().getArticle(names[j]);
					ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(names[j], true, article.getColorType());
					if(ae == null){
						try {
							ae = ArticleEntityManager.getInstance().createTempEntity(article, true, ArticleEntityManager.七日登录奖励, null, article.getColorType());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					tempIds[j] = ae.getId();
				}
				if(i==0){
					ids = tempIds;
					nums = dayPackageNums[0];
				}else if(i==1){
					ids2 = tempIds;
					mums2 = dayPackageNums[1];
				}else if(i==2){
					ids3 = tempIds;
					nums3 = dayPackageNums[2];
				}
			}
			String [] chargeIds;
			String [] specConfigs = {""};
			channelName = modifyChannelName(channelName);
			if(channelName.equals("APPSTORE_XUNXIAN")){
				chargeIds = new String[]{"xxy1","xxy3","xxy6"};
				specConfigs = new String[]{"xunxian_day1","xunxian_day3","xunxian_day6"};
			}else if(channelName.equals("JIUZHOUAPPSTORE_XUNXIAN")){
				chargeIds = new String[]{"meiri1","meiri3","meiri6"};
				specConfigs = new String[]{"jiuzhou_day1","jiuzhou_day3","jiuzhou_day6"};
			}else if(channelName.equals("QILEAPPSTORE_XUNXIAN")){
				chargeIds = new String[]{"qilemeiri1","qilemeiri3","qilemeiri6"};
				specConfigs = new String[]{"com.sg.jiuzhoucuhanqi.1","com.sg.jiuzhoucuhanqi.3","com.sg.jiuzhoucuhanqi.6.1"};
			}else if(channelName.equals("QQYSDK_XUNXIAN")){
				chargeIds = new String[]{"MQB1","MQB3","MQB6"};
				specConfigs = new String[]{"","",""};
			}else if(channelName.equals("HUOGAMEAPPSTORE_XUNXIAN")){
				chargeIds = new String[]{"huo01","huo03","huo06"};
				specConfigs = new String[]{"xunxianqingyuan.01","xunxianqingyuan.03","xunxianqingyuan.06"};
			}else if(channelName.equals("HUOGAME2APPSTORE_XUNXIAN")){
				chargeIds = new String[]{"huo01_2","huo03_2","huo06_2"};
				specConfigs = new String[]{"yijianlingyun.01","yijianlingyun.03","yijianlingyun.06"};
			}else if(channelName.equals("JIUZHOUPIAOMIAOQU_XUNXIAN")){
				chargeIds = new String[]{"piaomiaoqu01","piaomiaoqu03","piaomiaoqu06"};
				specConfigs = new String[]{"com.xxjz.pml.lb1","com.xxjz.pml.lb3","com.xxjz.pml.lb6"};
			}else if(channelName.equals("XUNXIANJUEAPPSTORE_XUNXIAN")){
				chargeIds = new String[]{"xunxian01","xunxian03","xunxian06"};
				specConfigs = new String[]{"com.ahh.xunxian_day1","com.ahh.xunxian_day3","com.ahh.xunxian_day6"};
			}else if(channelName.equals("AIWANAPPSTORE_XUNXIAN") || channelName.equals("AIWAN2APPSTORE_XUNXIAN")){
				chargeIds = new String[]{"jianxia01","jianxia03","jianxia06"};
				specConfigs = new String[]{"xunxian_day1","xunxian_day3","xunxian_day6"};
			}else if(channelName.equals("HUOGAME4APPSTORE_XUNXIAN")){
				chargeIds = new String[]{"huo01_3","huo03_3","huo06_3"};
				specConfigs = new String[]{"yijianzhuxian.01","yijianzhuxian.03","yijianzhuxian.06"};
			}else if(channelName.equals("HUOGAME5APPSTORE_XUNXIAN")){
				chargeIds = new String[]{"huo01_4","huo03_4","huo06_4"};
				specConfigs = new String[]{"sanjiepiaomiao.01","sanjiepiaomiao.03","sanjiepiaomiao.06"};
			}else{
				chargeIds = new String[]{"yxx1","yxx3","yxx6"};
				specConfigs = new String[]{"","",""};
			}
			
			int[] buyType = new int[player.getLastBuyTime().length];
			for(int i=0;i<buyType.length;i++){
				long[] lastBuyTime = player.getLastBuyTime();
				int[] buyTimes = player.getBuyTimes();
				if(!TimeTool.instance.isSame(lastBuyTime[i], System.currentTimeMillis(), TimeDistance.DAY)){
					lastBuyTime[i] = System.currentTimeMillis();
					buyTimes[i] = 0;
				}
				buyType[i] = buyTimes[i] > 0 ?1:0;
			}
			
			if(!player.ownMonthCard(CardFunction.每日可购买一个3元专属礼包)){
				buyType[1] = 2;
			}
			if(!player.ownMonthCard(CardFunction.每日可购买一个6元专属礼包)){
				buyType[2] = 3;
			}
			
			DAY_PACKAGE_OF_RMB_RES res = new DAY_PACKAGE_OF_RMB_RES(req.getSequnceNum(), cardType, ids, nums, ids2, mums2, ids3, nums3, modeName, "每日礼包", chargeIds, specConfigs, buyType);
			player.addMessageToRightBag(res);
			logger.warn("[查询每日礼包] [channel:{}] [modeName:{}] [chargeList:{}] [ids:{}] [specConfigs:{}] [buyType:{}] [{}]",
					new Object[]{channelName,modeName,list.size(),Arrays.toString(ids),Arrays.toString(specConfigs),Arrays.toString(buyType), player.getLogString()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String [] rewardName = {"飞剑坐骑(7天)","无双圣宝锦囊","6级攻击宝石袋","九阳戒指"};
	
	public void handle_FIRST_CHARGE_REQ(FIRST_CHARGE_REQ req ,Player player){
		long [] ids = new long[rewardName.length];
		try {
			if(!ActivityManager.getInstance().openFunction){
				player.sendError(Translate.此功能暂未开放);
				return;
			}
			for(int i=0;i<rewardName.length;i++){
				Article a = ArticleManager.getInstance().getArticle(rewardName[i]);
				ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(rewardName[i], true, a.getColorType());
				if(ae == null){
					ae = ArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.七日登录奖励, player, a.getColorType());
				}
				ids[i] = ae.getId();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		FIRST_CHARGE_RES res = new FIRST_CHARGE_RES(req.getSequnceNum(), ids, new int[]{1,1,1,1});
		player.addMessageToRightBag(res);
	}
	
	public void queryChargeList(CHARGE_LIST_REQ req,Player p){
		
		
		String channel = req.getChannel();
		if(channel == null || channel.isEmpty() || p == null){
			logger.warn("[获取充值列表] [失败:参数错误] [channel:"+channel+"] [player:"+(p!=null?p.getLogString():"null")+"]");
			return;
		}
		if ("HUASHENGSDK_XUNXIAN".equals(channel)) {
			p.sendError(Translate.暂不开放);
			ChargeManager.logger.error(p.getLogString() + " [HUASHENGSDK_XUNXIAN不能充值] [用户渠道:" + channel + "] ");
			return;
		}
		String modeNames [] = null;
		ChargeCardConfig [] chargeCardConfig = null;
		
		List<ChargeMode> list = getChannelChargeModes(channel);
		if (list == null || list.size() == 0) {
			p.sendError(Translate.无匹的充值信息请联系GM);
			logger.error("[查询充值信息] [异常] [无匹配的充值列表] [渠道:" + channel + "] ["+p.getLogString()+"]");
			return;
		}
		
		ChargeMode chargeMode = null;
		modeNames = new String[list.size()];
		for(int i=0;i<list.size();i++){
			ChargeMode temp = list.get(i);
			chargeMode = temp;
			modeNames[i] = temp.getModeName();
		}
		
		List<ChargeMoneyConfigure> configures = chargeMode.getMoneyConfigures(p);
		if (configures == null || configures.size() == 0) {
			p.sendError(Translate.无任何充值方式请联系GM);
			logger.error("[查询充值列表] [异常] [该充值列表下无任何信息] [渠道:" + channel + "] ["+p.getLogString()+"]");
			return;
		}
		
		chargeCardConfig = new ChargeCardConfig[configures.size()];
		for(int j=0;j<configures.size();j++){
			ChargeMoneyConfigure ccc = configures.get(j);
			ChargeCardConfig c = new ChargeCardConfig();
			c.setId(ccc.getId());
			int yuan = (int)ccc.getDenomination()/100;
			c.setNeedChargeRmb(yuan+"元");
			c.setSilverInfo("可获得："+BillingCenter.得到带单位的银两(ChargeRatio.DEFAULT_CHARGE_RATIO * yuan));
			ChargeCardConfig chargeActivity = getEffectConfig(channel,ccc.getId(), p);
			if(chargeActivity != null){
				c.setActivityType(chargeActivity.getActivityType());
				if(chargeActivity.getChargeType() == 1){
					if(!chargeActivity.canFirstCharge(p)){
						c.setActivityType(0);
					}
				}
				c.setIconOrDesc(chargeActivity.getIconOrDesc());
				c.setTableHead(chargeActivity.getTableHead());
				c.setTuiJianIcon(chargeActivity.getTuiJianIcon());
				c.setShowInIcon(chargeActivity.getShowInIcon());
				logger.error("[查询充值列表] [找到活动] [充值id:"+chargeActivity.getId()+"] [渠道:" + channel + "] ["+p.getLogString()+"]");
			}
			c.setBackageIcon(ccc.getBackageIcon());
			c.setSpecConfigs(ccc.getSpecialConf());
			chargeCardConfig[j] = c;
		}
		logger.warn("[查询充值列表] [成功] ["+chargeMode.getModeName()+"] [modeNames:"+Arrays.toString(modeNames)+"] [列表长度:" + configures.size() + "] ["+chargeCardConfig.length+"] [渠道:" + channel + "] ["+p.getLogString()+"]");
		CHARGE_LIST_RES res = new CHARGE_LIST_RES(req.getSequnceNum(), modeNames,"充值",chargeCardConfig);
		p.addMessageToRightBag(res);
	}
	
	public void handle_MONTH_CARD_FIRST_PAGE_REQ(MONTH_CARD_FIRST_PAGE_REQ req ,Player player){
		if(!ActivityManager.getInstance().openFunction){
			player.sendError(Translate.此功能暂未开放);
			return;
		}
		String channleName = req.getChannelName();
		if(channleName == null || channleName.isEmpty()){
			player.sendError(Translate.请选择正确的卡+".");
			return;
		}
		channleName = modifyChannelName(channleName);
		List<ChargeMode> list = ChargeManager.getInstance().getChannelChargeModes(channleName);
		if (list == null || list.size() == 0) {
			player.sendError(Translate.无匹的充值信息请联系GM);
			ChargeManager.logger.error("[查询充值列表] [月卡功能] [异常] [无匹配的充值列表] [渠道:{}] [玩家:{}]",new Object[]{channleName,player.getLogString()});
			return;
		}
		ChargeMode mode = list.get(0);
		if(mode == null){
			player.sendError(Translate.无匹的充值信息请联系GM);
			ChargeManager.logger.error("[查询充值列表] [月卡功能] [异常] [无匹配的充值列表2] [渠道:{}] [玩家:{}]",new Object[]{channleName,player.getLogString()});
			return;
		}
		String modeName = mode.getModeName();
		int [] ids = new int[cards.size()];
		long [] days = new long[cards.size()];
		int index = 0;
		Iterator<Entry<Integer, CardConfig>> it = cards.entrySet().iterator();
		List<String> xz = new ArrayList<String>();
		List<String> dj = new ArrayList<String>();
		List<String> fx = new ArrayList<String>();
		while(it.hasNext()){
			Entry<Integer, CardConfig> entry = it.next();
			int id = entry.getKey();
			ids[index] = id;
			days[index] = player.getCardEndDate()[index] - System.currentTimeMillis() < 0 ? 0 : player.getCardEndDate()[index] - System.currentTimeMillis();
			CardConfig config = entry.getValue();
			for(int fid : config.getIds()){
				for(CardFunction f : CardFunction.values()){
					if(f.getId() == fid){
						if(index == 0){
							xz.add(f.getName());
						}else if(index == 1){
							dj.add(f.getName());
						}else if(index == 2){
							fx.add(f.getName());
						}
					}
				}
			}
			index++;
		}
		
		String [] chargeIds;
		String [] specConfigs;
		if(channleName.equals("APPSTORE_XUNXIAN")){
			chargeIds = new String[]{"app12","app18","app30"};
			specConfigs = new String[]{"newxunxian_yueka12","newxunxian_yueka18","newxunxian_yueka30"};
		}else if(channleName.equals("JIUZHOUAPPSTORE_XUNXIAN")){
			chargeIds = new String[]{"jiuzhouyueka12","jiuzhouyueka18","jiuzhouyueka30"};
			specConfigs = new String[]{"jiuzhou_yueka12","jiuzhou_yueka18","jiuzhou_yueka30"};
		}else if(channleName.equals("QILEAPPSTORE_XUNXIAN")){
			chargeIds = new String[]{"qileyueka12","qileyueka18","qileyueka30"};
			specConfigs = new String[]{"com.sg.jiuzhoucuhanqi.xiuzhen","com.sg.jiuzhoucuhanqi.dujie","com.sg.jiuzhoucuhanqi.feixian"};
		}else if(channleName.equals("QQYSDK_XUNXIAN")){
			chargeIds = new String[]{"MQB12","MQB18","MQB30"};
			specConfigs = new String[]{"","",""};
		}else if(channleName.equals("HUOGAMEAPPSTORE_XUNXIAN")){
			chargeIds = new String[]{"huo012","huo018","huo030"};
			specConfigs = new String[]{"xunxianqingyuan.012","xunxianqingyuan.018","xunxianqingyuan.030"};
		}else if(channleName.equals("HUOGAME2APPSTORE_XUNXIAN")){
			chargeIds = new String[]{"huo012_2","huo018_2","huo030_2"};
			specConfigs = new String[]{"yijianlingyun.012","yijianlingyun.018","yijianlingyun.030"};
		}else if(channleName.equals("JIUZHOUPIAOMIAOQU_XUNXIAN")){
			chargeIds = new String[]{"piaomiaoqu012","piaomiaoqu018","piaomiaoqu030"};
			specConfigs = new String[]{"com.xxjz.pml.yk12","com.xxjz.pml.yk18","com.xxjz.pml.yk30"};
		}else if(channleName.equals("XUNXIANJUEAPPSTORE_XUNXIAN")){
			chargeIds = new String[]{"xunxian012","xunxian018","xunxian030"};
			specConfigs = new String[]{"com.ahh.newxunxian_yueka12","com.ahh.newxunxian_yueka18","com.ahh.newxunxian_yueka30"};
		}else if(channleName.equals("AIWANAPPSTORE_XUNXIAN") || channleName.equals("AIWAN2APPSTORE_XUNXIAN")){
			chargeIds = new String[]{"jianxia012","jianxia018","jianxia030"};
			specConfigs = new String[]{"newxunxian_yueka12","newxunxian_yueka18","newxunxian_yueka30"};
		}else if(channleName.equals("HUOGAME4APPSTORE_XUNXIAN")){
			chargeIds = new String[]{"huo012_3","huo018_3","huo030_3"};
			specConfigs = new String[]{"yijianzhuxian.012","yijianzhuxian.018","yijianzhuxian.030"};
		}else if(channleName.equals("HUOGAME5APPSTORE_XUNXIAN")){
			chargeIds = new String[]{"huo012_4","huo018_4","huo030_4"};
			specConfigs = new String[]{"sanjiepiaomiao.012","sanjiepiaomiao.018","sanjiepiaomiao.030"};
		}else{
			chargeIds = new String[]{"yxx12","yxx18","yxx30"};
			specConfigs = new String[]{"","",""};
		}
		//官方渠道，特殊处理，让玩家自己选择充值方式
//		if(channleName.equals("YOUAI_XUNXIAN")){
//			modeName = "官方充值";
//		}
		
		MONTH_CARD_FIRST_PAGE_RES res = new MONTH_CARD_FIRST_PAGE_RES(req.getSequnceNum(), ids, days, xz.toArray(new String[]{}), dj.toArray(new String[]{}), fx.toArray(new String[]{}), modeName, "购买月卡", chargeIds, specConfigs);
		player.addMessageToRightBag(res);
		logger.warn("[查询月卡信息] [channel:{}] [modeName:{}] [chargeList:{}] [ids:{}] [specConfigs:{}] [{}]",
				new Object[]{channleName,modeName,list.size(),Arrays.toString(ids),Arrays.toString(specConfigs),player.getLogString()});
	}

	public Map<Integer, CardConfig> getCards() {
		return cards;
	}

	public void setCards(Map<Integer, CardConfig> cards) {
		this.cards = cards;
	}

	@Override
	public String getMConsoleName() {
		return "充值管理器";
	}

	@Override
	public String getMConsoleDescription() {
		return "一些对充值的配置";
	}
}
