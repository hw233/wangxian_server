package com.fy.engineserver.util.datacheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.activity.fairyBuddha.FairyBuddhaManager;
import com.fy.engineserver.activity.fairylandTreasure.FairylandTreasureManager;
import com.fy.engineserver.activity.xianling.XianLingManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.ComposeInterface;
import com.fy.engineserver.datasource.article.data.articles.QiLingArticle;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeapon;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.hunshi.HunshiManager;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.shop.ShopManager;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.datacheck.handler.ArticleCheckHandler;
import com.fy.engineserver.util.datacheck.handler.MonsterFlopCheckHandler;
import com.fy.engineserver.util.datacheck.handler.ShopCheckHandler;
import com.fy.engineserver.util.datacheck.handler.TaskCheckHandler;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 数据校验管理
 * 
 * 
 */
public class DataCheckManager {

	private List<ConfigServer> configServers = new ArrayList<ConfigServer>();

	private List<DataCheckHandler> checkHandlers = new ArrayList<DataCheckHandler>();

	private static DataCheckManager instance;

	public static String[] mailAddress = {};

	public static Map<Platform, String[]> platFormMailAddress = new HashMap<Platform, String[]>();

	private TaskManager taskManager;

	private MemoryMonsterManager monsterManager;

	private ShopManager shopManager;

	private ArticleManager article_manager;

	private FaeryManager faeryManager;

	private FairyBuddhaManager fairyBuddhaManager;

	private FairylandTreasureManager fairylandTreasureManager;
	
	private HunshiManager hunshiManager;
	
	private XianLingManager xianLingManager;
	
	public static StringBuffer errorMess = new StringBuffer();
	static {
		platFormMailAddress.put(Platform.官方, new String[] {  "wtx062@126.com"});
	}

	public static DataCheckManager getInstance() {
		return instance;
	}

	public void init() {
		
		instance = this;

		initConfigServers();

		initDataCheckHandlers();

		doCheck();
		ServiceStartRecord.startLog(this);
	}

	public void doCheck() {
		boolean needSendMail = false;
		for (ConfigServer configServer : configServers) {
			if (configServer.serverCheck().getBooleanValue()) {
				needSendMail = true;
				break;
			}
		}
		ArticleManager.logger.warn("[DataCheckManager] [doCheck] [needSendMail:"+needSendMail+"]");
		if (needSendMail) {
			List<SendHtmlToMail[]> totalError = new ArrayList<SendHtmlToMail[]>();
			List<DataCheckHandler> handlers = new ArrayList<DataCheckHandler>();
			for (DataCheckHandler handler : checkHandlers) {
				if (handler.getCheckResult().getBooleanValue()) { // 有数据录入错误
					SendHtmlToMail[] htmlToMails = (SendHtmlToMail[]) handler.getCheckResult().getObjValue();
					if (htmlToMails != null && htmlToMails.length > 0) {
						errorMess.append(handler.getHandlerName()).append("<br>");
//						/errorMess.append(htmlToMails);
					}
				}
			}
			ArticleManager.logger.warn("[DataCheckManager] [doCheck] [needSendMail:"+needSendMail+"] [errorMess:"+errorMess.toString()+"]");
			
//			boolean hasContentTosend = false;
//			// 做最上面的目录索引
//			sendMailBuffer.append("<a name='pagetop'></a>");
//			for (int i = 0; i < totalError.size(); i++) {
//				SendHtmlToMail[] htmlToMails = totalError.get(i);
//				DataCheckHandler handler = handlers.get(i);
//				StringBuffer sbf = new StringBuffer();
//				sbf.append("<a href='#").append(handler.getHandlerName()).append("'>").append(handler.getHandlerName()).append("有<font color=red>").append(htmlToMails.length).append("</font>个错误</a><br/>");
//				sendMailBuffer.append(sbf.toString());
//			}
//			sendMailBuffer.append("<hr>");
//			for (int i = 0; i < totalError.size(); i++) {
//				SendHtmlToMail[] htmlToMails = totalError.get(i);
//				DataCheckHandler handler = handlers.get(i);
//				hasContentTosend = true;
//				StringBuffer sbf = new StringBuffer();
//				sbf.append("<H1><a name='").append(handler.getHandlerName()).append("'>").append(handler.getHandlerName()).append("</a>");
//				sbf.append("录入出错:").append(htmlToMails.length).append("个");
//				sbf.append("<a href='#pagetop'>↑↑↑↑↑返回顶部↑↑↑↑↑</a>");
//				sbf.append("</H1>");
//				sbf.append(Arrays.toString(handler.involveConfigfiles()));
//				// 做表头
//				SendHtmlToMail temp = htmlToMails[0];
//
//				sbf.append("<table style='font-size=12px;' border=1>");
//				sbf.append("<tr bgcolor='greend'>");
//				sbf.append("<td>序号</td>");
//				for (String title : temp.getTitles()) {
//					sbf.append("<td>");
//					sbf.append(title);
//					sbf.append("</td>");
//				}
//				sbf.append("</tr>");
//
//				int index = 0;
//				for (SendHtmlToMail htmlToMail : htmlToMails) {
//					sbf.append("<tr>");
//					sbf.append("<td>");
//					sbf.append(++index);
//					sbf.append("</td>");
//					for (String value : htmlToMail.getValues()) {
//						sbf.append("<td>");
//						sbf.append(value);
//						sbf.append("</td>");
//					}
//					sbf.append("</tr>");
//				}
//				sbf.append("</table>");
//				sbf.append("<HR>");
//				sbf.append("<HR>");
//				sendMailBuffer.append(sbf.toString());
//			}
//			String titlePrefix = "[飘渺寻仙曲] [" + GameConstants.getInstance().getServerName() + "] [" + PlatformManager.getInstance().getPlatform().toString() + "]";
//			if (hasContentTosend) {
//				// TODO SENDMAIL
//				ArticleManager.logger.error("[数据检查] " + sendMailBuffer.toString());
//				sendMail(titlePrefix + " [启动有异常数据]", sendMailBuffer.toString());
//			} else {
//				sendMailBuffer.append("已检查的内容如下:<BR/>");
//				for (DataCheckHandler handler : checkHandlers) {
//					sendMailBuffer.append(handler.getHandlerName());
//					sendMailBuffer.append(":");
//					sendMailBuffer.append(Arrays.toString(handler.involveConfigfiles()));
//					sendMailBuffer.append("<BR/>");
//
//				}
//				//sendMail(titlePrefix + " [数据正常] ", sendMailBuffer.toString());
//			}
		}

	}

//	public static void sendMail(String title, String content) {
//		StringBuffer sb = new StringBuffer();
//		sb.append(content);
//		sb.append("<br>" + DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
//		ArrayList<String> args = new ArrayList<String>();
//		args.add("-username");
//		args.add("sqage_wx_restart");
//		args.add("-password");
//		args.add("2wsxcde3");
//
//		args.add("-smtp");
//		args.add("smtp.163.com");
//		args.add("-from");
//		args.add("wtx062@126.com");
//		args.add("-to");
//		// TODO mailAddress 修改邮件
//		String address_to = "";
//
//		String[] addresses = platFormMailAddress.get(PlatformManager.getInstance().getPlatform());
//		if (addresses != null) {
//			for (String address : addresses) {
//				address_to += address + ",";
//			}
//		}
//
//		if (!"".equals(address_to)) {
//			args.add(address_to);
//			args.add("-subject");
//			args.add(title);
//			args.add("-message");
//			args.add(sb.toString());
//			args.add("-contenttype");
//			args.add("text/html;charset=utf-8");
//			try {
//				JavaMailUtils.sendMail(args.toArray(new String[0]));
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}

	public void initDataCheckHandlers() {
		checkHandlers.add(new MonsterFlopCheckHandler());
		checkHandlers.add(new ArticleCheckHandler());
		checkHandlers.add(new ShopCheckHandler());
		checkHandlers.add(new TaskCheckHandler());
//		checkHandlers.add(new CaveCheakHandler());
//		checkHandlers.add(new ActivenessCheckHandler());
//		checkHandlers.add(new ChongzhiActivityCheckHandler());
//		checkHandlers.add(new NewServerPrizeCheckHandler());
//		checkHandlers.add(new FairyBuddhaCheckHandler());
//		checkHandlers.add(new SkillCheckHandler());
//		checkHandlers.add(new WingCheckHandler());
//		checkHandlers.add(new FairylandTreasureCheckHandler());
//		checkHandlers.add(new HunshiCheckHandler());
//		checkHandlers.add(new XianLingCheckHandler());
	}

	public void initConfigServers() {
		ConfigServer configServer = new ConfigServer() {
			@Override
			public CompoundReturn serverCheck() {
				CompoundReturn cr = CompoundReturn.createCompoundReturn();
				List<String> serverNames = new ArrayList<String>();
				serverNames.add("开发服");
				GameConstants constants = GameConstants.getInstance();
				if (serverNames.contains(constants.getServerName())) {
					return cr.setBooleanValue(true);
				}
				return cr.setBooleanValue(false);
			}
		};
		configServers.add(configServer);
	}

	/**
	 * BooleanValue:物品颜色是否正确
	 * StringValue: 问题原因
	 * @param a
	 * @return
	 */
	public CompoundReturn isRightColorOfArticle(String articlename, int colortype) {
		CompoundReturn cr = CompoundReturn.create();
		Article a = ArticleManager.getInstance().getArticle(articlename);

		if (a == null) {
			return cr.setBooleanValue(false).setStringValue("物品" + articlename + "不存在！");
		}

		if (a instanceof Equipment) {
			if (colortype < 0 || colortype > 6) {
				return cr.setBooleanValue(false).setStringValue("物品" + articlename + "颜色超出界限,物品颜色：" + colortype);
			}
		} else {
			if (a instanceof MagicWeapon) {
				if (colortype < 0 || colortype > 5) {
					return cr.setBooleanValue(false).setStringValue("物品" + articlename + "颜色超出界限,物品颜色：" + colortype);
				}
			} else {
				if (colortype < 0 || colortype > 4) {
					return cr.setBooleanValue(false).setStringValue("物品" + articlename + "颜色超出界限,物品颜色：" + colortype);
				}
			}
		}

		if (a instanceof ComposeInterface || a instanceof QiLingArticle) {
			return cr.setBooleanValue(true).setStringValue("是可合成改变颜色的物品,不做判断!");
		}

		if (a.getColorType() != colortype) {
			return cr.setBooleanValue(false).setStringValue("正确的颜色为" + a.getColorType() + ",出售的颜色为：" + colortype);
		}

		if (a.getColorType() == colortype) {
			return cr.setBooleanValue(true).setStringValue("正确.");
		}

		return cr.setBooleanValue(false).setStringValue("其他颜色不符合的：正确的颜色为" + a.getColorType() + ",出售的颜色为：" + colortype);
	}

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

	public List<ConfigServer> getConfigServers() {
		return configServers;
	}

	public void setConfigServers(List<ConfigServer> configServers) {
		this.configServers = configServers;
	}

	public List<DataCheckHandler> getCheckHandlers() {
		return checkHandlers;
	}

	public void setCheckHandlers(List<DataCheckHandler> checkHandlers) {
		this.checkHandlers = checkHandlers;
	}

	public MemoryMonsterManager getMonsterManager() {
		return monsterManager;
	}

	public void setMonsterManager(MemoryMonsterManager monsterManager) {
		this.monsterManager = monsterManager;
	}

	public static String[] getMailAddress() {
		return mailAddress;
	}

	public static void setMailAddress(String[] mailAddress) {
		DataCheckManager.mailAddress = mailAddress;
	}

	public ShopManager getShopManager() {
		return shopManager;
	}

	public void setShopManager(ShopManager shopManager) {
		this.shopManager = shopManager;
	}

	public FairyBuddhaManager getFairyBuddhaManager() {
		return fairyBuddhaManager;
	}

	public void setFairyBuddhaManager(FairyBuddhaManager fairyBuddhaManager) {
		this.fairyBuddhaManager = fairyBuddhaManager;
	}

	public static void setInstance(DataCheckManager instance) {
		DataCheckManager.instance = instance;
	}

	public ArticleManager getArticle_manager() {
		return article_manager;
	}

	public void setArticle_manager(ArticleManager article_manager) {
		this.article_manager = article_manager;
	}

	public static Map<Platform, String[]> getPlatFormMailAddress() {
		return platFormMailAddress;
	}

	public static void setPlatFormMailAddress(Map<Platform, String[]> platFormMailAddress) {
		DataCheckManager.platFormMailAddress = platFormMailAddress;
	}

	public FaeryManager getFaeryManager() {
		return faeryManager;
	}

	public void setFaeryManager(FaeryManager faeryManager) {
		this.faeryManager = faeryManager;
	}

	public FairylandTreasureManager getFairylandTreasureManager() {
		return fairylandTreasureManager;
	}

	public void setFairylandTreasureManager(FairylandTreasureManager fairylandTreasureManager) {
		this.fairylandTreasureManager = fairylandTreasureManager;
	}

	public HunshiManager getHunshiManager() {
		return hunshiManager;
	}

	public void setHunshiManager(HunshiManager hunshiManager) {
		this.hunshiManager = hunshiManager;
	}

	public XianLingManager getXianLingManager() {
		return xianLingManager;
	}

	public void setXianLingManager(XianLingManager xianLingManager) {
		this.xianLingManager = xianLingManager;
	}

}
