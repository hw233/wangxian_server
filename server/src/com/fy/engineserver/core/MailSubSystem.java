package com.fy.engineserver.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.articleProtect.ArticleProtectDataValues;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.constants.InitialPlayerConstant;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.economic.thirdpart.migu.entity.ArticleTradeRecord;
import com.fy.engineserver.economic.thirdpart.migu.entity.ArticleTradeRecordManager;
import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.enterlimit.EnterLimitManager.PlayerRecordType;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.exception.WrongFormatMailException;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.green.GreenServerManager;
import com.fy.engineserver.mail.Mail;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.mail.service.concrete.DefaultMailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_GetOutPriceArticleFromMail;
import com.fy.engineserver.menu.Option_SendMailConfirm;
import com.fy.engineserver.menu.Option_UseCancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.mail.Option_Mail_MONEY;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.MAIL_CREATE_REQ;
import com.fy.engineserver.message.MAIL_DELETE_ALL_READED_RES;
import com.fy.engineserver.message.MAIL_DELETE_REQ;
import com.fy.engineserver.message.MAIL_GETOUT_ARTICLE_REQ;
import com.fy.engineserver.message.MAIL_GETOUT_ARTICLE_RES;
import com.fy.engineserver.message.MAIL_GET_REQ;
import com.fy.engineserver.message.MAIL_GET_RES;
import com.fy.engineserver.message.MAIL_LIST_BY_STATUS_NEW_REQ;
import com.fy.engineserver.message.MAIL_LIST_BY_STATUS_NEW_RES;
import com.fy.engineserver.message.MAIL_LIST_BY_STATUS_REQ;
import com.fy.engineserver.message.MAIL_LIST_BY_STATUS_RES;
import com.fy.engineserver.message.MAIL_LIST_CARRY_NEW_REQ;
import com.fy.engineserver.message.MAIL_LIST_CARRY_REQ;
import com.fy.engineserver.message.MAIL_LIST_NEW_REQ;
import com.fy.engineserver.message.MAIL_LIST_NEW_RES;
import com.fy.engineserver.message.MAIL_LIST_REQ;
import com.fy.engineserver.message.MAIL_LIST_RES;
import com.fy.engineserver.message.MAIL_MSG_RES;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.smith.ArticleRelationShipManager;
import com.fy.engineserver.smith.RelationShipHelper;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.PlayerSimpleInfo;
import com.fy.engineserver.sprite.PlayerSimpleInfoManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.validate.OtherValidateManager;
import com.fy.engineserver.validate.ValidateAsk;
import com.fy.engineserver.validate.ValidateManager;
import com.fy.engineserver.vip.VipManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.stat.client.StatClientService;
import com.xuanzhi.stat.model.PlayerActionFlow;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class MailSubSystem extends SubSystemAdapter {

	public static final int GAME_DATA_PACKET_SIZE = 1024;

	public static boolean FORCE_USE_DEFAULT_VIEW = false;

	public static int DEFAULT_PLAYER_VIEWWIDTH = 360;

	public static int DEFAULT_PLAYER_VIEWHEIGHT = 480;

	public String tagforbid[] = new String[] { "'", "\"", " or ", "μ", "Μ", "\n", "\t", " ", "　", "　" };

	/**
	 * 判断是否含有禁用的标签字符
	 * 
	 * @param name
	 * @return
	 */
	public boolean tagValid(String name) {
		String aname = name.toLowerCase();
		for (String s : tagforbid) {
			if (aname.indexOf(s) != -1) {
				return false;
			}
		}
		return true;
	}

	private static MailSubSystem self;

	public static MailSubSystem getInstance() {
		return self;
	}

	public static Logger logger = LoggerFactory.getLogger(MailSubSystem.class);

	private PlayerManager playerManager;
	private MailManager mailManager;

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public MailManager getMailManager() {
		return mailManager;
	}

	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}

	public String[] getInterestedMessageTypes() {
		return new String[] { "MAIL_LIST_REQ", "MAIL_LIST_BY_STATUS_REQ", "MAIL_GET_REQ", "MAIL_GETOUT_ARTICLE_REQ", "MAIL_DELETE_REQ", "MAIL_CREATE_REQ", "MAIL_DELETE_ALL_READED_REQ", "MAIL_LIST_CARRY_REQ", "MAIL_LIST_NEW_REQ", "MAIL_LIST_CARRY_NEW_REQ", "MAIL_LIST_BY_STATUS_NEW_REQ"

		};
	}

	public String getName() {
		return "MailSubSystem";
	}

	public void init() throws Exception {
		
		self = this;
		ServiceStartRecord.startLog(this);
	}

	public void destroy() {

	}

	/**
	 * TODO 如果此方法已经处理了请求消息，但是不需要返回响应，那么最后也会return null<br>
	 * 这样一来，调用者就无法区分出消息是否已经被处理了
	 */
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		Player player = check(conn, message, logger);
		if (useMethodCache) {
			return super.handleReqeustMessage(conn, message, player);
		}

		Class clazz = this.getClass();
		Method m1 = clazz.getDeclaredMethod("handle_" + message.getTypeDescription(), Connection.class, RequestMessage.class, Player.class);
		m1.setAccessible(true);
		try {
			return (ResponseMessage) m1.invoke(this, conn, message, player);
		} catch (InvocationTargetException e) {
			Throwable targetEx = ((InvocationTargetException) e).getTargetException();
			if (targetEx != null) {
				targetEx.printStackTrace();
			}
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public ResponseMessage handle_MAIL_LIST_REQ(Connection conn, RequestMessage message, Player player) {
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2724);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isInCrossServer()) {
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2725);
			player.addMessageToRightBag(hintreq);
			return null;
		}
		MAIL_LIST_REQ req = (MAIL_LIST_REQ) message;
		int pageIndex = req.getPageIndex();
		int pageNum = req.getPageNum();
		int count = mailManager.getCount(player.getId());
		if (pageNum < 0) {
			pageNum = 20;
		}
		if (pageNum > 100) {
			pageNum = 100;
		}
		int pages = count / pageNum;
		if (count % pageNum != 0) {
			pages++;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		List<Mail> mails = mailManager.getAllMails(player, pageIndex * pageNum, pageNum);
		long ids[] = new long[mails.size()];
		String titles[] = new String[mails.size()];
		String senderNames[] = new String[mails.size()];
		byte types[] = new byte[mails.size()];
		long prices[] = new long[mails.size()];
		byte status[] = new byte[mails.size()];
		long expiredate[] = new long[mails.size()];
		for (int i = 0; i < mails.size(); i++) {
			Mail mail = mails.get(i);
			ids[i] = mail.getId();
			titles[i] = mail.getTitle();
			senderNames[i] = Translate.系统;
			if (mail.getType() == Mail.TYPE_PLAYER) {
				long posterId = mail.getPoster();
				try {
					Player pp = playerManager.getPlayer(posterId);
					senderNames[i] = pp.getName();
				} catch (Exception e) {
					e.printStackTrace();
					MailManager.logger.error("[获取邮件列表异常] [找不到发信人] [id:" + posterId + "]", e);
					senderNames[i] = Translate.未知;
				}
			}
			types[i] = (byte) mail.getType();
			prices[i] = new Long(mail.getPrice()).intValue();
			status[i] = (byte) mail.getStatus();
			expiredate[i] = mail.getExpireDate().getTime();
		}
		if (ids.length >= 100) {
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.您的邮箱已满清理后才能收新邮件);
			player.addMessageToRightBag(hintreq);
		}
		MAIL_LIST_RES res = new MAIL_LIST_RES(req.getSequnceNum(), ids, types, status, titles, senderNames, expiredate, pages, pageIndex, prices);
		StringBuffer sb = new StringBuffer();
		if (ids != null) {
			for (long id : ids) {
				sb.append(id + ",");
			}

		}
		if (MailManager.logger.isInfoEnabled()) {
			MailManager.logger.info("[邮件列表] [操作人:{}/{}/{}] [总数:{}] [总页数:{}] [当前页数:{}] [每页数量:{}] [{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), count, pages, pageIndex, pageNum, sb });
		}

		MAIL_MSG_RES mailmsgres = new MAIL_MSG_RES(GameMessageFactory.nextSequnceNum(), DefaultMailManager.MAIL_MONEY,0);//DefaultMailManager.MAIL_MONEY, DefaultMailManager.FREE_MAIL_NUM);
		player.addMessageToRightBag(mailmsgres);

		return res;
	}

	public ResponseMessage handle_MAIL_LIST_NEW_REQ(Connection conn, RequestMessage message, Player player) {
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2724);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isInCrossServer()) {
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2725);
			player.addMessageToRightBag(hintreq);
			return null;
		}
		MAIL_LIST_NEW_REQ req = (MAIL_LIST_NEW_REQ) message;
		int pageIndex = req.getPageIndex();
		int pageNum = req.getPageNum();
		int mailType = req.getMailBaseType();
		int count = mailManager.getCount(mailType, player.getId());
		if (pageNum < 0) {
			pageNum = 20;
		}
		if (pageNum > 100) {
			pageNum = 100;
		}
		int pages = count / pageNum;
		if (count % pageNum != 0) {
			pages++;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		List<Mail> mails = mailManager.getAllMails(player, mailType, pageIndex * pageNum, pageNum);
		long ids[] = new long[mails.size()];
		String titles[] = new String[mails.size()];
		String senderNames[] = new String[mails.size()];
		byte types[] = new byte[mails.size()];
		long prices[] = new long[mails.size()];
		byte status[] = new byte[mails.size()];
		long expiredate[] = new long[mails.size()];
		for (int i = 0; i < mails.size(); i++) {
			Mail mail = mails.get(i);
			ids[i] = mail.getId();
			titles[i] = mail.getTitle();
			senderNames[i] = Translate.系统;
			if (mail.getType() == Mail.TYPE_PLAYER) {
				long posterId = mail.getPoster();
				try {
					Player pp = playerManager.getPlayer(posterId);
					senderNames[i] = pp.getName();
				} catch (Exception e) {
					e.printStackTrace();
					MailManager.logger.error("[获取邮件列表异常] [找不到发信人] [id:" + posterId + "]", e);
					senderNames[i] = Translate.未知;
				}
			}
			types[i] = (byte) mail.getType();
			prices[i] = new Long(mail.getPrice()).intValue();
			status[i] = (byte) mail.getStatus();
			expiredate[i] = mail.getExpireDate().getTime();
		}
		if (ids.length >= 100) {
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.您的邮箱已满清理后才能收新邮件);
			player.addMessageToRightBag(hintreq);
		}
		MAIL_LIST_NEW_RES res = new MAIL_LIST_NEW_RES(req.getSequnceNum(), ids, types, status, titles, senderNames, expiredate, pages, pageIndex, prices);
		StringBuffer sb = new StringBuffer();
		if (ids != null) {
			for (long id : ids) {
				sb.append(id + ",");
			}

		}
		if (MailManager.logger.isInfoEnabled()) {
			MailManager.logger.info("[邮件列表New] [type:{}] [操作人:{}/{}/{}] [总数:{}] [总页数:{}] [当前页数:{}] [每页数量:{}] [{}]", new Object[] { mailType, player.getId(), player.getName(), player.getUsername(), count, pages, pageIndex, pageNum, sb });
		}

		MAIL_MSG_RES mailmsgres = new MAIL_MSG_RES(GameMessageFactory.nextSequnceNum(),DefaultMailManager.MAIL_MONEY,0);//DefaultMailManager.MAIL_MONEY, DefaultMailManager.FREE_MAIL_NUM);
		player.addMessageToRightBag(mailmsgres);

		return res;
	}

	public ResponseMessage handle_MAIL_LIST_CARRY_REQ(Connection conn, RequestMessage message, Player player) {
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2724);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isInCrossServer()) {
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2725);
			player.addMessageToRightBag(hintreq);
			return null;
		}
		VipManager vm = VipManager.getInstance();
		if (vm != null && vm.vip是否开启随身邮件(player)) {
			MAIL_LIST_CARRY_REQ req = (MAIL_LIST_CARRY_REQ) message;
			int pageIndex = req.getPageIndex();
			int pageNum = req.getPageNum();
			int count = mailManager.getCount(player.getId());
			if (pageNum < 0) {
				pageNum = 20;
			}
			if (pageNum > 100) {
				pageNum = 100;
			}
			int pages = count / pageNum;
			if (count % pageNum != 0) {
				pages++;
			}
			if (pageIndex < 0) {
				pageIndex = 0;
			}
			List<Mail> mails = mailManager.getAllMails(player, pageIndex * pageNum, pageNum);
			long ids[] = new long[mails.size()];
			String titles[] = new String[mails.size()];
			String senderNames[] = new String[mails.size()];
			byte types[] = new byte[mails.size()];
			long prices[] = new long[mails.size()];
			byte status[] = new byte[mails.size()];
			long expiredate[] = new long[mails.size()];
			for (int i = 0; i < mails.size(); i++) {
				Mail mail = mails.get(i);
				ids[i] = mail.getId();
				titles[i] = mail.getTitle();
				senderNames[i] = Translate.系统;
				if (mail.getType() == Mail.TYPE_PLAYER) {
					long posterId = mail.getPoster();
					try {
						Player pp = playerManager.getPlayer(posterId);
						senderNames[i] = pp.getName();
					} catch (Exception e) {
						e.printStackTrace();
						MailManager.logger.error("[获取邮件列表异常] [找不到发信人] [id:" + posterId + "]", e);
						senderNames[i] = Translate.未知;
					}
				}
				types[i] = (byte) mail.getType();
				prices[i] = new Long(mail.getPrice()).intValue();
				status[i] = (byte) mail.getStatus();
				expiredate[i] = mail.getExpireDate().getTime();
			}
			if (ids.length >= 100) {
				HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.您的邮箱已满清理后才能收新邮件);
				player.addMessageToRightBag(hintreq);
			}
			MAIL_LIST_RES res = new MAIL_LIST_RES(req.getSequnceNum(), ids, types, status, titles, senderNames, expiredate, pages, pageIndex, prices);
			StringBuffer sb = new StringBuffer();
			if (ids != null) {
				for (long id : ids) {
					sb.append(id + ",");
				}

			}
			if (MailManager.logger.isInfoEnabled()) {
				MailManager.logger.info("[邮件列表] [操作人:{}/{}/{}] [总数:{}] [总页数:{}] [当前页数:{}] [每页数量:{}] [{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), count, pages, pageIndex, pageNum, sb });
			}
			return res;
		} else {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.您的vip级别不足);
			player.addMessageToRightBag(hreq);
			return null;
		}
	}

	public ResponseMessage handle_MAIL_LIST_CARRY_NEW_REQ(Connection conn, RequestMessage message, Player player) {
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2724);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isInCrossServer()) {
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2725);
			player.addMessageToRightBag(hintreq);
			return null;
		}
		VipManager vm = VipManager.getInstance();
		if (vm != null && vm.vip是否开启随身邮件(player)) {
			MAIL_LIST_CARRY_NEW_REQ req = (MAIL_LIST_CARRY_NEW_REQ) message;
			int pageIndex = req.getPageIndex();
			int pageNum = req.getPageNum();
			int mailType = req.getMailBaseType();
			int count = mailManager.getCount(mailType, player.getId());
			if (pageNum < 0) {
				pageNum = 20;
			}
			if (pageNum > 100) {
				pageNum = 100;
			}
			int pages = count / pageNum;
			if (count % pageNum != 0) {
				pages++;
			}
			if (pageIndex < 0) {
				pageIndex = 0;
			}
			List<Mail> mails = mailManager.getAllMails(player, mailType, pageIndex * pageNum, pageNum);
			long ids[] = new long[mails.size()];
			String titles[] = new String[mails.size()];
			String senderNames[] = new String[mails.size()];
			byte types[] = new byte[mails.size()];
			long prices[] = new long[mails.size()];
			byte status[] = new byte[mails.size()];
			long expiredate[] = new long[mails.size()];
			for (int i = 0; i < mails.size(); i++) {
				Mail mail = mails.get(i);
				ids[i] = mail.getId();
				titles[i] = mail.getTitle();
				senderNames[i] = Translate.系统;
				if (mail.getType() == Mail.TYPE_PLAYER) {
					long posterId = mail.getPoster();
					try {
						Player pp = playerManager.getPlayer(posterId);
						senderNames[i] = pp.getName();
					} catch (Exception e) {
						e.printStackTrace();
						MailManager.logger.error("[获取邮件列表异常] [找不到发信人] [id:" + posterId + "]", e);
						senderNames[i] = Translate.未知;
					}
				}
				types[i] = (byte) mail.getType();
				prices[i] = new Long(mail.getPrice()).intValue();
				status[i] = (byte) mail.getStatus();
				expiredate[i] = mail.getExpireDate().getTime();
			}
			if (ids.length >= 100) {
				HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.您的邮箱已满清理后才能收新邮件);
				player.addMessageToRightBag(hintreq);
			}
			MAIL_LIST_NEW_RES res = new MAIL_LIST_NEW_RES(req.getSequnceNum(), ids, types, status, titles, senderNames, expiredate, pages, pageIndex, prices);
			StringBuffer sb = new StringBuffer();
			if (ids != null) {
				for (long id : ids) {
					sb.append(id + ",");
				}

			}
			if (MailManager.logger.isInfoEnabled()) {
				MailManager.logger.info("[邮件列表New] [type:{}] [操作人:{}/{}/{}] [总数:{}] [总页数:{}] [当前页数:{}] [每页数量:{}] [{}]", new Object[] { mailType, player.getId(), player.getName(), player.getUsername(), count, pages, pageIndex, pageNum, sb });
			}
			return res;
		} else {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.您的vip级别不足);
			player.addMessageToRightBag(hreq);
			return null;
		}
	}

	public ResponseMessage handle_MAIL_LIST_BY_STATUS_REQ(Connection conn, RequestMessage message, Player player) {
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2724);
			player.addMessageToRightBag(nreq);
			return null;
		}
		MAIL_LIST_BY_STATUS_REQ req = (MAIL_LIST_BY_STATUS_REQ) message;
		byte mailType = req.getMailType();
		int pageIndex = req.getPageIndex();
		int pageNum = req.getPageNum();
		int count = mailManager.getCount(player.getId());
		if (pageNum < 0) {
			pageNum = 20;
		}
		if (pageNum > 100) {
			pageNum = 100;
		}

		if (pageIndex < 0) {
			pageIndex = 0;
		}
		List<Mail> mails = new ArrayList<Mail>();
		// 0全部邮件，1未读邮件，2已读邮件，3附件未读邮件，4附件已读邮件
		if (mailType == 0) {
			mails = mailManager.getAllMails(player, pageIndex * pageNum, pageNum);
		} else if (mailType == 1) {
			mails = mailManager.getUnReadMails(player, pageIndex * pageNum, pageNum);
			count = mailManager.getCount(player.getId(), 1);
		} else if (mailType == 2) {
			mails = mailManager.getReadMails(player, pageIndex * pageNum, pageNum);
			count = mailManager.getCount(player.getId(), 2);
		} else if (mailType == 3) {
			mails = mailManager.getUnReadMailHasAppendixs(player, pageIndex * pageNum, pageNum);
			count = mailManager.getCount(player.getId(), 3);
		} else if (mailType == 4) {
			mails = mailManager.getReadMailHasAppendixs(player, pageIndex * pageNum, pageNum);
			count = mailManager.getCount(player.getId(), 4);
		}
		int pages = count / pageNum;
		if (count % pageNum != 0) {
			pages++;
		}
		long ids[] = new long[mails.size()];
		String titles[] = new String[mails.size()];
		String senderNames[] = new String[mails.size()];
		byte types[] = new byte[mails.size()];
		long prices[] = new long[mails.size()];
		byte status[] = new byte[mails.size()];
		long expiredate[] = new long[mails.size()];
		for (int i = 0; i < mails.size(); i++) {
			Mail mail = mails.get(i);
			ids[i] = mail.getId();
			titles[i] = mail.getTitle();
			senderNames[i] = Translate.系统;
			if (mail.getType() == Mail.TYPE_PLAYER) {
				long posterId = mail.getPoster();
				try {
					Player pp = playerManager.getPlayer(posterId);
					senderNames[i] = pp.getName();
				} catch (Exception e) {
					e.printStackTrace();
					MailManager.logger.error("[获取邮件列表异常] [找不到发信人] [id:" + posterId + "]", e);
					senderNames[i] = Translate.未知;
				}
			}
			types[i] = (byte) mail.getType();
			prices[i] = new Long(mail.getPrice()).intValue();
			status[i] = (byte) mail.getStatus();
			expiredate[i] = mail.getExpireDate().getTime();
		}
		if (ids.length >= 100) {
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.您的邮箱已满清理后才能收新邮件);
			player.addMessageToRightBag(hintreq);
		}
		MAIL_LIST_BY_STATUS_RES res = new MAIL_LIST_BY_STATUS_RES(req.getSequnceNum(), ids, types, status, titles, senderNames, expiredate, pages, pageIndex, prices);
		StringBuffer sb = new StringBuffer();
		if (ids != null) {
			for (long id : ids) {
				sb.append(id + ",");
			}

		}
		if (MailManager.logger.isInfoEnabled()) {
			MailManager.logger.info("[邮件列表] [操作人:{}/{}/{}] [总数:{}] [总页数:{}] [当前页数:{}] [每页数量:{}] [{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), count, pages, pageIndex, pageNum, sb });
		}
		return res;
	}

	public ResponseMessage handle_MAIL_LIST_BY_STATUS_NEW_REQ(Connection conn, RequestMessage message, Player player) {
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2724);
			player.addMessageToRightBag(nreq);
			return null;
		}
		MAIL_LIST_BY_STATUS_NEW_REQ req = (MAIL_LIST_BY_STATUS_NEW_REQ) message;
		int mailBaseType = req.getMailBaseType();
		byte mailType = req.getMailType();
		int pageIndex = req.getPageIndex();
		int pageNum = req.getPageNum();
		int count = mailManager.getCount(mailBaseType, player.getId());
		if (pageNum < 0) {
			pageNum = 20;
		}
		if (pageNum > 100) {
			pageNum = 100;
		}

		if (pageIndex < 0) {
			pageIndex = 0;
		}
		List<Mail> mails = new ArrayList<Mail>();
		// 0全部邮件，1未读邮件，2已读邮件，3附件未读邮件，4附件已读邮件
		if (mailType == 0) {
			mails = mailManager.getAllMails(player, mailBaseType, pageIndex * pageNum, pageNum);
		} else if (mailType == 1) {
			mails = mailManager.getUnReadMails(player, mailBaseType, pageIndex * pageNum, pageNum);
			count = mailManager.getCount(player.getId(), mailBaseType, 1);
		} else if (mailType == 2) {
			mails = mailManager.getReadMails(player, mailBaseType, pageIndex * pageNum, pageNum);
			count = mailManager.getCount(player.getId(), mailBaseType, 2);
		} else if (mailType == 3) {
			mails = mailManager.getUnReadMailHasAppendixs(player, mailBaseType, pageIndex * pageNum, pageNum);
			count = mailManager.getCount(player.getId(), mailBaseType, 3);
		} else if (mailType == 4) {
			mails = mailManager.getReadMailHasAppendixs(player, mailBaseType, pageIndex * pageNum, pageNum);
			count = mailManager.getCount(player.getId(), mailBaseType, 4);
		}
		int pages = count / pageNum;
		if (count % pageNum != 0) {
			pages++;
		}
		long ids[] = new long[mails.size()];
		String titles[] = new String[mails.size()];
		String senderNames[] = new String[mails.size()];
		byte types[] = new byte[mails.size()];
		long prices[] = new long[mails.size()];
		byte status[] = new byte[mails.size()];
		long expiredate[] = new long[mails.size()];
		for (int i = 0; i < mails.size(); i++) {
			Mail mail = mails.get(i);
			ids[i] = mail.getId();
			titles[i] = mail.getTitle();
			senderNames[i] = Translate.系统;
			if (mail.getType() == Mail.TYPE_PLAYER) {
				long posterId = mail.getPoster();
				try {
					Player pp = playerManager.getPlayer(posterId);
					senderNames[i] = pp.getName();
				} catch (Exception e) {
					e.printStackTrace();
					MailManager.logger.error("[获取邮件列表异常] [找不到发信人] [id:" + posterId + "]", e);
					senderNames[i] = Translate.未知;
				}
			}
			types[i] = (byte) mail.getType();
			prices[i] = new Long(mail.getPrice()).intValue();
			status[i] = (byte) mail.getStatus();
			expiredate[i] = mail.getExpireDate().getTime();
		}
		if (ids.length >= 100) {
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.您的邮箱已满清理后才能收新邮件);
			player.addMessageToRightBag(hintreq);
		}
		MAIL_LIST_BY_STATUS_NEW_RES res = new MAIL_LIST_BY_STATUS_NEW_RES(req.getSequnceNum(), ids, types, status, titles, senderNames, expiredate, pages, pageIndex, prices);
		StringBuffer sb = new StringBuffer();
		if (ids != null) {
			for (long id : ids) {
				sb.append(id + ",");
			}

		}
		if (MailManager.logger.isInfoEnabled()) {
			MailManager.logger.info("[邮件列表New] [{}] [操作人:{}/{}/{}] [总数:{}] [总页数:{}] [当前页数:{}] [每页数量:{}] [{}]", new Object[] { mailBaseType, player.getId(), player.getName(), player.getUsername(), count, pages, pageIndex, pageNum, sb });
		}
		return res;
	}

	public ResponseMessage handle_MAIL_GET_REQ(Connection conn, RequestMessage message, Player player) {
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2724);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isInCrossServer()) {
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2725);
			player.addMessageToRightBag(hintreq);
			return null;
		}
		MAIL_GET_REQ req = (MAIL_GET_REQ) message;
		long id = req.getId();
		Mail mail = mailManager.getMail(id);
		if (mail == null) {
			MAIL_GET_RES res = new MAIL_GET_RES(req.getSequnceNum(), (byte) 0, "", "", 0, "", new long[8], new int[8], 0, 0L, 0L, 0);
			return res;
		}
		long receiverId = mail.getReceiver();
		if (receiverId != player.getId()) {
			if (MailManager.logger.isWarnEnabled()) MailManager.logger.warn("[发现外挂] [试图获取其他人的邮件] [邮件id:{}] [操作人:{}/{}/{}] [connection:{}]", new Object[] { mail.getId(), player.getId(), player.getName(), player.getUsername(), conn.getName() });
			MAIL_GET_RES res = new MAIL_GET_RES(req.getSequnceNum(), (byte) 0, "", "", 0, "", new long[8], new int[8], 0, 0L, 0L, 0);
			return res;
		}
		long posterId = mail.getPoster();
		String posterName = Translate.未知;
		if (posterId > 0) {
			Player poster = null;
			try {
				poster = playerManager.getPlayer(mail.getPoster());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(" ", e);
			}
			if (poster != null) {
				posterName = poster.getName();
			}
		} else if (posterId == -1) {
			posterName = Translate.系统;
		}
		long entityIds[] = new long[mail.getCells().length];
		int counts[] = new int[entityIds.length];
		for (int i = 0; i < counts.length; i++) {
			entityIds[i] = mail.getCells()[i].getEntityId();
			counts[i] = mail.getCells()[i].getCount();
		}
		MAIL_GET_RES res = new MAIL_GET_RES(req.getSequnceNum(), (byte) mail.getType(), mail.getTitle(), mail.getContent(), mail.getPoster(), posterName, entityIds, counts, new Long(mail.getCoins()).intValue(), mail.getCreateDate().getTime(), mail.getExpireDate().getTime(), new Long(mail.getPrice()).intValue());
		if (mail.getStatus() == Mail.NORMAL_UNREAD) {
			mail.setStatus(Mail.NORMAL_READED);
			player.removeNewMailCount();
		} else if (mail.getStatus() == Mail.APPENDIX_UNREAD) {
			mail.setStatus(Mail.APPENDIX_READED);
			player.removeNewMailCount();
		}
		player.setLastCheckMailTime(0);

		if (MailManager.logger.isInfoEnabled()) {
			Player receiver = null;
			Player poster = null;
			try {
				receiver = PlayerManager.getInstance().getPlayer(receiverId);
				if (posterId != -1) {
					poster = PlayerManager.getInstance().getPlayer(posterId);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String attachment = "";
			Cell[] cells = mail.getCells();
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			if (cells != null) {
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] != null && cells[i].getEntityId() > 0 && cells[i].getCount() > 0) {
						ArticleEntity ae = aem.getEntity(cells[i].getEntityId());
						if (ae != null) {
							attachment += ae.getId() + "/" + ae.getArticleName() + "/" + cells[i].getCount() + ",";
						} else {
							attachment += cells[i].getEntityId() + Translate.text_2727 + cells[i].getCount() + ",";
						}
					} else {
						if (cells[i] != null) {
							attachment += Translate.text_2728 + cells[i].getEntityId() + Translate.text_2729 + cells[i].getCount() + ",";
						} else {
							attachment += Translate.text_2730;
						}
					}
				}
				if (attachment.length() > 0) {
					attachment = attachment.substring(0, attachment.length() - 1);
				}
			}
			try {
				EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.收取邮件次数, 1L});
				EventRouter.getInst().addEvent(evt);
			} catch (Exception e) {
				PlayerAimManager.logger.error("[目标系统] [统计玩家查看邮件异常] [" + player.getLogString() + "]", e);
			}
			
			{
				try
				{
					if(posterId > 0 && PlayerManager.getInstance().getPlayer(posterId) != null )
					{
						ArticleTradeRecord articleTradeRecord =  new ArticleTradeRecord();
						articleTradeRecord.setSellPlayerId(posterId);
						articleTradeRecord.setBuyPlayerId(receiverId);
						
						List<Long> articleIdsList = new ArrayList<Long>();
						
						
						if (cells != null) {
							for (int i = 0; i < cells.length; i++) {
								articleIdsList.add(cells[i].getEntityId());
							}
							
							Long[] articleIds = articleIdsList.toArray(new Long[0]);
							long[] articlelongIds = new long[articleIds.length];
							if(articleIds.length > 0)
							{
								for(int i = 0; i < articleIds.length; i++)
								{
									articlelongIds[i] = articleIds[i].longValue();
								}
	
								
								articleTradeRecord.setArticleIds(articlelongIds);
								
								articleTradeRecord.setDesc("发邮件");
								articleTradeRecord.setTradeTime(System.currentTimeMillis());
							
								ArticleTradeRecordManager.getInstance().notifyNew(articleTradeRecord);
								if(ArticleTradeRecordManager.logger.isInfoEnabled())
									ArticleTradeRecordManager.logger.info("[创建邮件交易记录] [成功] [sell:"+mail.getPoster()+"] [buyer:"+mail.getReceiver()+"]"+articleTradeRecord.getLogStr()+"");
							}
						}
						else
						{
							ArticleTradeRecordManager.logger.warn("[创建邮件交易记录] [不需要创建交易记录因为邮件无道具] [sell:"+mail.getPoster()+"] [buyer:"+mail.getReceiver()+"]");
						}
					}
					else
					{
						ArticleTradeRecordManager.logger.warn("[创建邮件交易记录] [失败] [没有获取到发邮件人角色] [sell:"+mail.getPoster()+"] [buyer:"+mail.getReceiver()+"]");
					}
				}
				catch(Exception e)
				{
					ArticleTradeRecordManager.logger.error("[创建邮件交易记录] [失败] [出现未知异常] [sell:"+mail.getPoster()+"] [buyer:"+mail.getReceiver()+"]",e);
				}
			}
			
			
			
			
			if (MailManager.logger.isInfoEnabled()) MailManager.logger.info("[打开邮件] [邮件id:{}] [操作人:{}/{}/{}] [收件人:{}] [发件人:{}] [标题:{}] [内容:{}] [附件：{}] [附加游戏币：{}] [付费价格：{}] [{}ms]", new Object[] { mail.getId(), player.getId(), player.getName(), player.getUsername(), (receiver != null ? receiverId + "/" + receiver.getName() + "/" + receiver.getUsername() : ""), (poster != null ? posterId + "/" + poster.getName() + "/" + poster.getUsername() : ""), mail.getTitle(), mail.getContent(), attachment, mail.getCoins(), mail.getPrice(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
			
		}
		return res;
	}

	public ResponseMessage handle_MAIL_GETOUT_ARTICLE_REQ(Connection conn, RequestMessage message, Player player) {
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2724);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isInCrossServer()) {
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2725);
			player.addMessageToRightBag(hintreq);
			return null;
		}
		MAIL_GETOUT_ARTICLE_REQ req = (MAIL_GETOUT_ARTICLE_REQ) message;
		long id = req.getId();
		Mail mail = mailManager.getMail(id);

		if (mail == null) {
			MAIL_GETOUT_ARTICLE_RES res = new MAIL_GETOUT_ARTICLE_RES(req.getSequnceNum(), Translate.text_2731);
			return res;
		}
		if (mail.getStatus() == Mail.NORMAL_UNREAD) {
			mail.setStatus(Mail.NORMAL_READED);
			player.removeNewMailCount();
		} else if (mail.getStatus() == Mail.APPENDIX_UNREAD) {
			mail.setStatus(Mail.APPENDIX_READED);
			player.removeNewMailCount();
		}
		long receiverId = mail.getReceiver();
		long posterId = mail.getPoster();
		Player receiver = null;
		Player poster = null;
		if (receiverId != player.getId()) {
			if (MailManager.logger.isWarnEnabled()) MailManager.logger.warn("[发现外挂] [试图获取其他人的邮件附件] [邮件id:{}] [操作人:{}/{}/{}] [connection:{}]", new Object[] { mail.getId(), player.getId(), player.getName(), player.getUsername(), conn.getName() });
			MAIL_GETOUT_ARTICLE_RES res = new MAIL_GETOUT_ARTICLE_RES(req.getSequnceNum(), Translate.text_2732);
			return res;
		}
		try {
			receiver = PlayerManager.getInstance().getPlayer(receiverId);
			if (posterId != -1) {
				poster = PlayerManager.getInstance().getPlayer(posterId);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String attachment = "";
		Cell[] cells = mail.getCells();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if (cells != null) {
			for (int i = 0; i < cells.length; i++) {
				if (cells[i] != null && cells[i].getEntityId() > 0 && cells[i].getCount() > 0) {
					ArticleEntity ae = aem.getEntity(cells[i].getEntityId());
					if (ae != null) {
						attachment += ae.getId() + "/" + ae.getArticleName() + "/" + cells[i].getCount() + ",";
					} else {
						attachment += cells[i].getEntityId() + Translate.text_2727 + cells[i].getCount() + ",";
					}
				} else {
					if (cells[i] != null) {
						attachment += Translate.text_2728 + cells[i].getEntityId() + Translate.text_2729 + cells[i].getCount() + ",";
					} else {
						attachment += Translate.text_2730;
					}
				}
			}
			if (attachment.length() > 0) {
				attachment = attachment.substring(0, attachment.length() - 1);
			}
		}

		if (mail.getPrice() > 0) {
			WindowManager wm = WindowManager.getInstance();
			MenuWindow mw = wm.createTempMenuWindow(600);
			mw.setDescriptionInUUB(Translate.text_2733 + mail.getPrice() + Translate.text_2734);
			Option_GetOutPriceArticleFromMail option = new Option_GetOutPriceArticleFromMail();
			option.setId(mail.getId());
			option.setText(Translate.确定);
			Option cancelOption = new Option_UseCancel();
			cancelOption.setText(Translate.取消);
			Option[] options = new Option[] { option, cancelOption };
			mw.setOptions(options);

			CONFIRM_WINDOW_REQ re = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
			player.addMessageToRightBag(re);
			return null;
		}
		try {
			List<ArticleEntity> elist = new ArrayList<ArticleEntity>();
			for (Cell cell : cells) {
				long entityId = cell.getEntityId();
				int count = cell.getCount();
				if (entityId != -1 && count > 0) {
					ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(entityId);
					for (int i = 0; i < count; i++) {
						elist.add(entity);
					}
				}
			}

			boolean putOK = player.putAllOK(elist.toArray(new ArticleEntity[0]));
			long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			if (elist.size() > 0 && putOK) {
				String reason = "提取邮件，" + mail.getCreateReason();
				player.putAll(elist.toArray(new ArticleEntity[0]), reason);
				mail.clearAllCell();
				try {
					for (ArticleEntity ae : elist) {
						ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, reason, null);
					}
				} catch (Exception ex) {

				}

			} else if (elist.size() > 0) {
				MAIL_GETOUT_ARTICLE_RES res = new MAIL_GETOUT_ARTICLE_RES(req.getSequnceNum(), Translate.用户背包已满无法放置更多物品);
				return res;
			}
			BillingCenter billingCenter = BillingCenter.getInstance();
			if (mail.getCoins() > 0) {
				String des = mail.getCreateReason();
				if (des == null) {
					des = "";
				}
				billingCenter.playerSaving(player, mail.getCoins(), CurrencyType.YINZI, SavingReasonType.MAIL_GET, des);
			}
			mail.setCoins(0);
			mail.setPrice(0);
			mail.setStatus(Mail.NORMAL_READED);
			mail.setLastModifyDate(new java.util.Date());
			MAIL_GETOUT_ARTICLE_RES res = new MAIL_GETOUT_ARTICLE_RES(req.getSequnceNum(), "");
			if (MailManager.logger.isInfoEnabled()) {
				MailManager.logger.info("[取出邮件的附件] [邮件id:{}] [操作人:{}/{}/{}] [发件人:{}] [收件人:{}] [标题:{}] [内容:{}] [附件：{}] [附加游戏币：{}] [付费价格：{}] [{}ms]", new Object[] { mail.getId(), player.getId(), player.getName(), player.getUsername(), (poster != null ? posterId + "/" + poster.getName() + "/" + poster.getUsername() : posterId), (receiver != null ? receiverId + "/" + receiver.getName() + "/" + receiver.getUsername() : receiverId), mail.getTitle(), mail.getContent(), attachment, mail.getCoins(), mail.getPrice(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
			}

			if (req.getDeleteFlag()) {
				mailManager.deleteMail(id);
			}
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			MailManager.logger.error("[取出邮件附件异常] [邮件id:" + mail.getId() + "]  [操作人:" + player.getId() + "/" + player.getName() + "/" + player.getUsername() + "]", e);
		}
		return null;
	}

	public void getPriceMail(Player player, long id) {
		try {
			MailManager mailManager = MailManager.getInstance();
			PlayerManager playerManager = PlayerManager.getInstance();
			Mail mail = mailManager.getMail(id);
			if (mail != null && mail.getPrice() > 0) {
				long receiver = mail.getReceiver();
				if (receiver != player.getId()) {
					if (MailManager.logger.isWarnEnabled()) MailManager.logger.warn("[发现外挂] [试图获取其他人的付费邮件附件] [邮件id:{}] [{}] [{}] [{}]", new Object[] { mail.getId(), player.getUsername(), player.getId(), player.getName() });
					return;
				}
				long coins = mail.getCoins();
				long price = mail.getPrice();
				if (player.getSilver() < mail.getPrice()) {
					String description = Translate.金币不足;
					try {
						description = Translate.translateString(Translate.付费提示, new String[][] { new String[] { Translate.COUNT_1, mail.getPrice() + "" } });
					} catch (Exception ex) {

					}
					HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hint);
					return;
				} else {

					BillingCenter billingCenter = BillingCenter.getInstance();
					try {
						billingCenter.playerExpense(player, mail.getPrice(), CurrencyType.YINZI, ExpenseReasonType.MAIL_FEE_GET, "", -1);
						// 已经付费，设置为可提取状态
						mail.setPrice(0L);
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.DAY_OF_YEAR, 30);
						mail.setExpireDate(cal.getTime());
						if (MailManager.logger.isInfoEnabled()) {
							long senderId = mail.getPoster();
							Player sender = null;
							try {
								sender = playerManager.getPlayer(senderId);
							} catch (Exception e) {
								e.printStackTrace();
								MailManager.logger.error(" ", e);
							}
							if (MailManager.logger.isInfoEnabled()) MailManager.logger.info("[付费提取邮件] [邮件id:{}] [付费方] [user:{}] [{}] [接受人id:{}] [付费:{}] [发送方] [user:{}] [{}] [发送方id:{}]", new Object[] { mail.getId(), player.getUsername(), player.getName(), player.getId(), price, (sender != null ? sender.getUsername() : ""), (sender != null ? sender.getName() : ""), (sender != null ? sender.getId() : "") });
						}
					} catch (Exception e) {
						e.printStackTrace();
						MailManager.logger.error("[收取付费邮件附件时出错,玩家扣费失败] [邮件id:" + mail.getId() + "] [user:" + player.getUsername() + "] [" + player.getName() + "] [接受人id:" + player.getId() + "] [应付费:" + price + "]", e);
						HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.提取附件失败扣费失败);
						player.addMessageToRightBag(hint);
						return;
					}
					try {
						long senderId = mail.getPoster();
						Player sender = null;
						try {
							sender = playerManager.getPlayer(senderId);
						} catch (Exception e) {
							e.printStackTrace();
							MailManager.logger.error(" ", e);
						}
						if (sender != null) {
							Mail mm = new Mail();
							String title = Translate.邮件回复;
							try {
								title = Translate.translateString(Translate.邮件回复主题, new String[][] { new String[] { Translate.PLAYER_NAME_1, player.getName() } });
							} catch (Exception ex) {

							}
							mm.setTitle(title);
							try {
								title = Translate.translateString(Translate.邮件回复内容, new String[][] { new String[] { Translate.PLAYER_NAME_1, player.getName() }, new String[] { Translate.COUNT_1, price + "" } });
							} catch (Exception ex) {

							}
							mm.setContent(title);
							mm.setCoins(price);
							Calendar cal = Calendar.getInstance();
							mm.setCreateDate(cal.getTime());
							// 30天后过期
							cal.add(Calendar.DAY_OF_YEAR, 30);
							mm.setExpireDate(cal.getTime());
							mm.setPoster(-1);
							mm.setReceiver(senderId);
							mm.setType(Mail.TYPE_SYSTEM);
							mailManager.createMail(mm);
							sender.setLastCheckMailTime(0);
						}
					} catch (Exception e) {
						e.printStackTrace();
						MailManager.logger.error("[收取付费邮件附件时出错] [邮件id:" + mail.getId() + "] [user:" + player.getUsername() + "] [" + player.getName() + "] [接受人id:" + player.getId() + "]", e);
						HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.提取附件失败请联系管理员);
						player.addMessageToRightBag(hint);
						return;
					}
				}
				try {
					String attachment = "";
					Cell[] cells = mail.getCells();
					ArticleEntityManager aem = ArticleEntityManager.getInstance();
					// 附件中的内容
					if (cells != null) {
						for (int i = 0; i < cells.length; i++) {
							if (cells[i] != null && cells[i].getEntityId() > 0 && cells[i].getCount() > 0) {
								ArticleEntity ae = aem.getEntity(cells[i].getEntityId());
								if (ae != null) {
									attachment += ae.getId() + "/" + ae.getArticleName() + "/" + cells[i].getCount() + ",";
								} else {
									attachment += cells[i].getEntityId() + Translate.text_2727 + cells[i].getCount() + ",";
								}
							} else {
								if (cells[i] != null) {
									attachment += Translate.text_2728 + cells[i].getEntityId() + Translate.text_2729 + cells[i].getCount() + ",";
								} else {
									attachment += Translate.text_2730;
								}
							}
						}
						if (attachment.length() > 0) {
							attachment = attachment.substring(0, attachment.length() - 1);
						}
					}

					List<ArticleEntity> elist = new ArrayList<ArticleEntity>();
					for (Cell cell : cells) {
						long entityId = cell.getEntityId();
						int count = cell.getCount();
						if (entityId != -1 && count > 0) {
							ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(entityId);
							for (int i = 0; i < count; i++) {
								elist.add(entity);
							}
						}
					}

					boolean putOK = player.putAllOK(elist.toArray(new ArticleEntity[0]));
					long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
					if (elist.size() > 0 && putOK) {
						String reason = "提取邮件，" + mail.getCreateReason();
						player.putAll(elist.toArray(new ArticleEntity[0]), reason);
						mail.clearAllCell();
						try {
							for (ArticleEntity ae : elist) {
								ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, reason, null);
							}
						} catch (Exception ex) {

						}
					} else if (elist.size() > 0) {
						MAIL_GETOUT_ARTICLE_RES res = new MAIL_GETOUT_ARTICLE_RES(GameMessageFactory.nextSequnceNum(), Translate.text_2723);
						if (MailManager.logger.isInfoEnabled()) {
							long senderId = mail.getPoster();
							Player sender = null;
							try {
								sender = playerManager.getPlayer(senderId);
							} catch (Exception e) {
								e.printStackTrace();
								MailManager.logger.error(" ", e);
							}
							if (MailManager.logger.isInfoEnabled()) MailManager.logger.info("[取出付费邮件的附件失败，用户背包已满，无法放置更多物品] [邮件id:{}] [接受人:{}] [接受人:{}] [接受人ID:{}] [发送方] [user:{}] [{}] [发送方id:{}] [物品:{}] [{}] [是否能放置:{}] [金币:{}] [价格(付费):{}] [{}]", new Object[] { mail.getId(), player.getUsername(), player.getName(), player.getId(), (sender != null ? sender.getUsername() : ""), (sender != null ? sender.getName() : ""), (sender != null ? sender.getId() : ""), elist.size(), attachment, putOK, coins, price, (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
						}
						player.addMessageToRightBag(res);
						return;
					}
					BillingCenter billingCenter = BillingCenter.getInstance();
					if (mail.getCoins() > 0) {
						billingCenter.playerSaving(player, mail.getCoins(), CurrencyType.YINZI, SavingReasonType.MAIL_GET, "");
					}

					mail.setCoins(0);
					mail.setPrice(0);
					mail.setStatus(Mail.NORMAL_READED);
					mail.setLastModifyDate(new java.util.Date());
					MAIL_GETOUT_ARTICLE_RES res = new MAIL_GETOUT_ARTICLE_RES(GameMessageFactory.nextSequnceNum(), "");
					if (MailManager.logger.isInfoEnabled()) {
						long senderId = mail.getPoster();
						Player sender = null;
						try {
							sender = playerManager.getPlayer(senderId);
						} catch (Exception e) {
							e.printStackTrace();
							MailManager.logger.error(" ", e);
						}
						if (MailManager.logger.isInfoEnabled()) MailManager.logger.info("[取出付费邮件的附件] [邮件id:{}] [接受人:{}] [接受人:{}] [接受人ID:{}] [发送方] [user:{}] [{}] [发送方id:{}] [物品:{}] [{}] [是否能放置:{}] [金币:{}] [价格(付费):{}] [{}]", new Object[] { mail.getId(), player.getUsername(), player.getName(), player.getId(), (sender != null ? sender.getUsername() : ""), (sender != null ? sender.getName() : ""), (sender != null ? sender.getId() : ""), elist.size(), attachment, putOK, coins, price, (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
					}
					player.addMessageToRightBag(res);
					// 发送统计日志
					try {
						if (GamePlayerManager.isAllowActionStat()) {
							StatClientService.getInstance().sendPlayerAction(player.getUsername(), player.getName(), GameConstants.getInstance().getServerName(), player.getCurrentGame() == null ? player.getLastGame() : player.getCurrentGame().getGameInfo().getName(), CareerManager.careerNames[player.getCareer()], "", player.getLevel(), player.getPassport().getRegisterChannel(), player.getPassport().getRegisterChannel(), player.getPassport().getRegisterChannel(), player.getPassport().getRegisterChannel(), PlayerActionFlow.行为类型_提取付费邮件, player.getName(), new java.util.Date(), mail.getPrice());
						}
					} catch (Exception err) {
						err.printStackTrace();
						StatClientService.logger.error("[发送日志失败]", err);
					}
					return;
				} catch (Exception e) {
					e.printStackTrace();
					long senderId = mail.getPoster();
					Player sender = null;
					try {
						sender = playerManager.getPlayer(senderId);
					} catch (Exception ex) {
						ex.printStackTrace();
						MailManager.logger.error(" ", ex);
					}
					MailManager.logger.error("[取出付费邮件附件异常] [邮件id:" + mail.getId() + "] [接受人:" + player.getUsername() + "] [接受人:" + player.getName() + "] [接受人ID:" + player.getId() + "] [发送方] [user:" + (sender != null ? sender.getUsername() : "") + "] [" + (sender != null ? sender.getName() : "") + "] [发送方id:" + (sender != null ? sender.getId() : "") + "]", e);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ResponseMessage handle_MAIL_CREATE_REQ(Connection conn, RequestMessage message, Player player) {
		if (UnitServerFunctionManager.needCloseFunctuin(Function.邮件)) {
			player.sendError(Translate.合服功能关闭提示);
			return null;
		}
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您的角色处于锁定状态暂时不能使用邮箱);
			player.addMessageToRightBag(nreq);
			return null;
		}

		MAIL_CREATE_REQ req = (MAIL_CREATE_REQ) message;
		int nowTaday = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
		DefaultMailManager defaultMailManager = (DefaultMailManager) DefaultMailManager.getInstance();
		if (nowTaday != defaultMailManager.sendMailToday) {
			defaultMailManager.sendMailToday = nowTaday;
		}

		Long time = defaultMailManager.playerSendMailTime.get(player.getId());
		if (time != null && System.currentTimeMillis() - time < DefaultMailManager.MAIL_SPACE_TIME) {
			player.sendError(Translate.您发送邮件频率太过频繁请稍后再试);
			return null;
		}

		boolean hasArticle = false;
		int indexes[] = req.getIndexes();
		for(int i : indexes){
			if(i > 0){
				hasArticle = true;
			}
		}
		if (hasArticle) {
			defaultMailManager.playerSendMailTime.put(player.getId(),System.currentTimeMillis());
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(300);
			mw.setTitle(Translate.邮件收费提示);
			mw.setDescriptionInUUB(Translate.发送邮件绑银不足);
			Option_Mail_MONEY money = new Option_Mail_MONEY();
			money.setReq(req);
			money.setText(Translate.确定);
			Option_Cancel cancel = new Option_Cancel();
			cancel.setText(Translate.取消);
			Option options[] = new Option[] { money, cancel };
			mw.setOptions(options);
			CONFIRM_WINDOW_REQ cwr = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
			player.addMessageToRightBag(cwr);
			return null;
		}

		creatMail(req, player, false);

		return null;
	}

	public void creatMail(RequestMessage message, Player player, boolean isAskBefore) {
		if (!isAskBefore) {
			int state = OtherValidateManager.getInstance().getValidateState(player, OtherValidateManager.ASK_TYPE_MAIL);
			if (state == ValidateManager.VALIDATE_STATE_需要答题) {
				ValidateAsk ask = OtherValidateManager.getInstance().sendValidateAsk(player, OtherValidateManager.ASK_TYPE_MAIL);
				ask.setAskFormParameters(new Object[] { message });
				return;
			}
		}

		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		MAIL_CREATE_REQ req = (MAIL_CREATE_REQ) message;

		try {
			if (RelationShipHelper.checkForbidAndSendMessage(player)) {
				if (RelationShipHelper.logger.isInfoEnabled()) {
					RelationShipHelper.logger.info("[玩家因为打金行为被限制发送邮件] [" + player.getLogString() + "]");
				}
				return;
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		if (ChatMessageService.getInstance().isSilence(player.getId()) >= 2) {
			EnterLimitManager.logger.warn("[用户被限制邮件] ["+player.getUsername()+"] ["+player.getName()+"] ["+player.getId()+"] ["+player.getLevel()+"] 银子:["+player.getSilver()+"]");
			return;
		}

		DefaultMailManager defaultMailManager = (DefaultMailManager) DefaultMailManager.getInstance();
		
		long sendMoney = 0;
		boolean hasArticle = false;
		for(int i : req.getIndexes()){
			if(i > 0){
				hasArticle = true;
			}
		}
		if(hasArticle){
			if (player.getBindSilver() + player.getSilver() < 50000) {
				player.sendError("银子不足，发送失败");
				return;
			}
			sendMoney = 50000;
		}

		String content = req.getMcontent();
		if (player.isInCrossServer()) {
			if (req.getTitle().equals(Translate.BUG自动报告)) {
				MailManager.logger.error("[BUG自动报告] [GMBUG] [操作人:{}/{}/{}] [内容:{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), content });
				return;
			}
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2725);
			player.addMessageToRightBag(hintreq);
			return;
		}

		int indexes[] = req.getIndexes();
		int count[] = req.getCount();
		long coins = 0;
		boolean fangbao[] = req.getFangbao();
		String title = req.getTitle();
		if (indexes == null || count == null || indexes.length != count.length || fangbao.length != count.length) {
			HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.邮件中的物品错误);
			player.addMessageToRightBag(hint);
			return;
		}
		if (content.length() == 0) {
			content = Translate.无;
		}
		String receiver = req.getReceiverName();

		Player receiverO = null;
		try {
			if (receiver != null && receiver.length() > 0) {
				receiverO = playerManager.getPlayer(receiver);
			}
		} catch (Exception e) {
			MailManager.logger.error("[发送邮件] [获得接收人失败] [" + receiver + "]", e);
		}
		if (receiverO == null) {
			MailManager.logger.error("[发送邮件失败] [接收人不存在] [操作人:{}/{}/{}] [接收人:{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), receiver });
			if (!title.equals(Translate.BUG自动报告)) {
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.无此收信人);
				player.addMessageToRightBag(hint);
			} else {
				MailManager.logger.error("[BUG自动报告] [GMBUG] [操作人:{}/{}/{}] [内容:{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), content });
			}
			return;
		}

		if (receiverO.isBlackuser(player.getId())) {
			MailManager.logger.error("[发送邮件失败] [发送人在接受人的黑名单中] [操作人:" + player.getId() + "/" + player.getName() + "/" + player.getUsername() + "] [接收人:" + receiver + "]");
			HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2737);
			player.addMessageToRightBag(hint);
			return;
		}

		if (title.length() == 0) {
			title = Translate.无标题;
		}
		long price = 0;

		if (coins < 0 || price < 0) {
			HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.邮件金币或者付费价格错误);
			player.addMessageToRightBag(hint);
			return;
		}

		for (int i = 0; i < count.length; i++) {
			if (count[i] < 0) {
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.数量错误);
				player.addMessageToRightBag(hint);
				return;
			}
		}

		long sendMailCostYinzi = 0;
		// 这边还需要加上税款
		if (coins + sendMailCostYinzi > 0 && coins + sendMailCostYinzi > player.getSilver()) {
			MailManager.logger.error("[发送邮件失败] [金币不够] [操作人:{}/{}/{}] [发送人:{}/{}/{}] [接收人:{}/{}/{}] [附加金币:{}] [玩家拥有金币:{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), player.getId(), player.getName(), player.getUsername(), receiverO.getId(), receiverO.getName(), receiverO.getUsername(), coins, player.getSilver() });
			if (!title.equals(Translate.BUG自动报告)) {
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.translateString(Translate.发送邮件银子不足, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(coins + sendMailCostYinzi) } }));
				player.addMessageToRightBag(hint);
			}
			return;
		}

		if (price > 0) {
			boolean fujian = false;
			for (int i = 0; i < indexes.length; i++) {
				if (indexes[i] >= 0 && count[i] > 0) {
					fujian = true;
				}
			}
			if (!fujian) {
				MailManager.logger.error("[发送邮件失败] [此邮件为付费邮件，必须添加附件] [操作人:{}/{}/{}] [下标:{}] [数量:{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), (StringUtil.arrayToString(indexes, ",")), (StringUtil.arrayToString(count, ",")) });
				if (!title.equals(Translate.BUG自动报告)) {
					HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.此邮件为付费邮件需要添加附件);
					player.addMessageToRightBag(hint);
				}
				return;
			}
		}

		if (receiver != null && receiver.equals(player.getName())) {
			if (!title.equals(Translate.BUG自动报告)) {
				MailManager.logger.error("[发送邮件失败] [不能给自己发送邮件] [操作人:{}/{}/{}] [接收人:{}/{}/{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), receiverO.getId(), receiverO.getName(), receiverO.getUsername() });
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.不能给自己发送邮件);
				player.addMessageToRightBag(hint);
			}
			return;
		}
		if (coins > 0) {
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(300);
			mw.setTitle(Translate.发送确认);
			String gangName = "";
			if (receiverO.getGangName() != null) {
				gangName = receiverO.getGangName();
			}
			String description = Translate.发送确认;
			try {
				description = Translate.translateString(Translate.发送确认详细, new String[][] {});
			} catch (Exception ex) {

			}
			mw.setDescriptionInUUB(description);
			Option_SendMailConfirm osmc = new Option_SendMailConfirm(message);
			osmc.setColor(0xffffff);
			osmc.setText(Translate.确定);
			Option_UseCancel oc = new Option_UseCancel();
			oc.setColor(0xffffff);
			oc.setText(Translate.取消);
			Option[] options = new Option[] { oc, osmc };
			mw.setOptions(options);
			CONFIRM_WINDOW_REQ cwr = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
			player.addMessageToRightBag(cwr);
			return;
		}

		Mail mail = new Mail();
		mail.setCoins(coins);
		mail.setContent(content);
		mail.setPoster(player.getId());
		mail.setPrice(price);
		mail.setType(Mail.TYPE_PLAYER);
		if (!receiver.matches(InitialPlayerConstant.GM)) {
			int mailNum = mailManager.getCount(receiverO.getId());
			if (mailNum >= 100) {
				MailManager.logger.error("[发送邮件失败] [收件人邮箱已满] [操作人:{}/{}/{}] [接收人:{}/{}/{}] [{}ms]", new Object[] { player.getId(), player.getName(), player.getUsername(), receiverO.getId(), receiverO.getName(), receiverO.getUsername(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
				if (!title.equals(Translate.BUG自动报告)) {
					HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.发送失败收件人邮箱已满);
					player.addMessageToRightBag(hint);
					if (receiverO.isOnline()) {
						HINT_REQ hint2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.您的邮箱已满不能接收新邮件请及时清理);
						receiverO.addMessageToRightBag(hint2);
					}
				}
				return;
			}
		}
		mail.setReceiver(receiverO.getId());
		mail.setTitle(title);
		boolean hasAppendix = false;

		boolean existTwoSameIndex = false;
		for (int i = 0; i < indexes.length; i++) {
			for (int j = 0; j < indexes.length; j++) {
				if (i != j && indexes[i] == indexes[j] && fangbao[i] == fangbao[j] && indexes[i] != -1) {
					existTwoSameIndex = true;
					break;
				}
			}
			if (existTwoSameIndex) {
				break;
			}
		}

		if (existTwoSameIndex) {
			HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.发送失败邮件附件中有两个格子指向背包中的同一个格子);
			player.addMessageToRightBag(hint);

			MailManager.logger.error("[发送邮件失败] [可能是外挂行为:邮件中多个格子指向背包中同一格子] [操作人:{}/{}/{}] [接收人:{}/{}/{}] [格子下标：{}] [数量：{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), receiverO.getId(), receiverO.getName(), receiverO.getUsername(), StringUtil.arrayToString(indexes, ","), StringUtil.arrayToString(count, ",") });
			return;
		}
		try {
			Cell cells[] = mail.getCells();
			for (int i = 0; i < indexes.length; i++) {
				if (indexes[i] >= 0) {
					int knapsackIndex = 0;
					Knapsack knap = fangbao[i] ? player.getKnapsack_fangbao() : player.getKnapsack_common();
					ArticleEntity ae = knap.getArticleEntityByCell(indexes[i]);
					if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_Common, ae.getId())) {
						player.sendError(Translate.锁魂物品不能邮寄);
						return;
					}
					if (ae != null && ae.getColorType() >= 3) {
						MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(300);
						mw.setTitle(Translate.发送确认);
						String description = Translate.邮件发送贵重物品确认;
						mw.setDescriptionInUUB(description);
						Option_SendMailConfirm osmc = new Option_SendMailConfirm(message);
						osmc.setColor(0xffffff);
						osmc.setText(Translate.确定);
						Option_UseCancel oc = new Option_UseCancel();
						oc.setColor(0xffffff);
						oc.setText(Translate.取消);
						Option[] options = new Option[] { oc, osmc };
						mw.setOptions(options);
						CONFIRM_WINDOW_REQ cwr = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
						player.addMessageToRightBag(cwr);
						return;
					}
					if (!GreenServerManager.canToOtherPlayer(ae)) {
						player.sendError(Translate.text_trade_023);
						return;
					}
					if (ae != null && ae.isBinded() || (ae != null && ae.isRealBinded())) {
						MailManager.logger.error("[发送邮件失败] [可能是外挂行为:邮寄了绑定物品] [操作人:{}/{}/{}] [接收人:{}/{}/{}] [格子下标：{}] [数量：{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), receiverO.getId(), receiverO.getName(), receiverO.getUsername(), StringUtil.arrayToString(indexes, ","), StringUtil.arrayToString(count, ",") });
						HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.发送失败不能邮寄绑定的物品);
						player.addMessageToRightBag(hint);
						return;
					}
					if (ae != null) {
						int num = knap.getCell(indexes[i]).getCount();
						if (num < count[i]) {
							MailManager.logger.error("[发送邮件失败] [用户背包中没有那么多物品] [操作人:{}/{}/{}] [接收人:{}/{}/{}] [ 背包格子：{}] [个数{}] [物品：{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), receiverO.getId(), receiverO.getName(), receiverO.getUsername(), indexes[i], count[i], ae.getArticleName() });
							if (!title.equals(Translate.BUG自动报告)) {
								HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.你的背包格中没有那么多物品);
								player.addMessageToRightBag(hint);
							}
							return;
						} else {
							hasAppendix = true;
						}
					} else {
						MailManager.logger.error("[发送邮件失败] [用户背包中没有任何物品] [操作人:{}/{}/{}] [接收人:{}/{}/{}] [ 背包格子：{}] [个数{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), receiverO.getId(), receiverO.getName(), receiverO.getUsername(), indexes[i], count[i] });
						if (!title.equals(Translate.BUG自动报告)) {
							HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.你的背包格中没有任何物品);
							player.addMessageToRightBag(hint);
						}
						return;
					}
				}
			}

			BillingCenter billingCenter = BillingCenter.getInstance();
			if (coins > 0) {
				try {
					billingCenter.playerExpense(player, coins, CurrencyType.YINZI, ExpenseReasonType.MAIL_PUT_MONEY, "", -1);
					hasAppendix = true;
				} catch (Exception e) {
					e.printStackTrace();
					MailManager.logger.error("[创建邮件失败]  [操作人:" + player.getId() + "/" + player.getName() + "/" + player.getUsername() + "] [接收人:" + receiverO.getId() + "/" + receiverO.getName() + "/" + receiverO.getUsername() + "] [扣除金币时发生异常]", e);
					if (!title.equals(Translate.BUG自动报告)) {
						HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.扣除金币时出错);
						player.addMessageToRightBag(hint);
					}
					return;
				}
			}
			if (sendMailCostYinzi > 0) {
				try {
					billingCenter.playerExpense(player, sendMailCostYinzi, CurrencyType.SHOPYINZI, ExpenseReasonType.SEND_MAIL, "发送邮件扣费");
				} catch (Exception e) {

				}
			}

			// 检查后，再开始删除背包中的物品 -- 
			boolean deleteFail = false;
			StringBuffer sb = new StringBuffer();
			int sendCount = 0;
			int colors[] = new int[indexes.length];
			String names [] = new String[indexes.length];
			synchronized (player.tradeKey) {
				for (int i = 0; deleteFail == false && i < indexes.length; i++) {
					if (indexes[i] >= 0) {
						int knapsackIndex = 0;
						Knapsack knap = fangbao[i] ? player.getKnapsack_fangbao() : player.getKnapsack_common();
						ArticleEntity ae = knap.getArticleEntityByCell(indexes[i]);
						if (ae != null && !ae.isBinded()) {
							colors[i] = ae.getColorType();
							names[i] = ae.getArticleName();
							for (int j = 0; j < count[i]; j++) {
								if (knap.getCell(indexes[i]).getCount() < 1) {
									deleteFail = true;
									break;
								} else {
									ArticleEntity removeAe = knap.remove(indexes[i], "发送邮件删除", false);
									
									if (removeAe != null) {
										// 统计
										ArticleStatManager.addToArticleStat(player, null, removeAe, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "发送邮件删除", null);
										sendCount++;
									}
								}
							}
							cells[i].setCount(count[i]);
							cells[i].setEntityId(ae.getId());
							sb.append(ae.getId() + "/" + ae.getArticleName() + "/" + count[i] + ",");
						} else {
							sb.append(knap.getCell(indexes[i]).getEntityId() + "/" + count[i] + ",");
						}
					}
				}
			}
			String entityDesp = sb.toString();
			if (entityDesp.length() > 0) {
				entityDesp = entityDesp.substring(0, entityDesp.length() - 1);
			}

			if (deleteFail == true) {
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.发送失败删除背包物品失败);
				player.addMessageToRightBag(hint);

				MailManager.logger.error("[发送邮件失败] [删除背包物品失败] [操作人:{}/{}/{}] [接收人:{}/{}/{}] [格子下标：{}] [数量：{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), receiverO.getId(), receiverO.getName(), receiverO.getUsername(), StringUtil.arrayToString(indexes, ","), StringUtil.arrayToString(count, ",") });
				return;
			}

			Calendar cal = Calendar.getInstance();
			mail.setCreateDate(cal.getTime());
			if (price == 0) {
				cal.add(Calendar.DAY_OF_YEAR, 30);
			} else {
				cal.add(Calendar.HOUR, 1);
			}
			mail.setExpireDate(cal.getTime());
			if (hasAppendix) {
				mail.setStatus(Mail.APPENDIX_UNREAD);
			}
			mail.setCreateReason("玩家创建邮件");

			try {
				long start = System.currentTimeMillis();
				// 进行防骗子处理，规则，如果发现发送者不是接收者的好友，并且发现接收者好友列表中有玩家名称和发送者近似，那么认为有骗子嫌疑，对接收者进行提示
				if (!receiverO.isFriend(player.getId())) {
					List<Long> flist = SocialManager.getInstance().getFriendlist(receiverO);
					if (flist.size() == 0) {
						if (MailManager.logger.isDebugEnabled()) {
							MailManager.logger.debug("[不符合嫌疑要求] [接收方好友列表为空] [sname:" + player.getName() + "] [rname:" + receiverO.getName() + "]");
						}
					}
					PlayerSimpleInfoManager psm = PlayerSimpleInfoManager.getInstance();
					boolean suspect = false;
					String sname = null;
					for (Long id : flist) {
						PlayerSimpleInfo sp = psm.getInfoById(id);
						if (sp == null) {
							if (MailManager.logger.isDebugEnabled()) {
								MailManager.logger.debug("[不符合嫌疑要求] [无法找到简单信息] [sname:" + player.getName() + "] [rname:" + receiverO.getName() + "] [fid:" + id + "]");
							}
							continue;
						}
						int distance = StringUtils.getLevenshteinDistance(player.getName(), sp.getName());
						double approx = new Double(distance) * 100 / new Double(sp.getName().length());
						if (approx < 35 || (player.getName().length() > 2 && distance <= 2) || (approx <= 50 && player.getName().length() == 2)) {
							suspect = true;
							sname = sp.getName();
							if (MailManager.logger.isDebugEnabled()) {
								MailManager.logger.debug("[符合嫌疑要求] [sname:" + player.getName() + "] [fname:" + sp.getName() + "] [distance:" + distance + "] [approx:" + approx + "]");
							}
							break;
						} else {
							if (MailManager.logger.isDebugEnabled()) {
								MailManager.logger.debug("[不符合嫌疑要求] [sname:" + player.getName() + "] [fname:" + sp.getName() + "] [distance:" + distance + "] [approx:" + approx + "]");
							}
						}
					}
					if (suspect) {
						String msg = "</f><f color='0xff0000'>系统提示：对方不是您的好友，但是名字和您的好友“" + sname + "”相似，请警惕骗子行为！</f>";
						msg = msg + "\n<f color='#FFFFFF'>" + mail.getContent();
						mail.setContent(msg);
						MailManager.logger.warn("[疑似骗子邮件] [发送方:" + player.getLogString() + "] [接收方:" + receiverO.getLogString() + "] [相似好友:" + sname + "] [" + (System.currentTimeMillis() - start) + "ms]");
					}
				} else {
					if (MailManager.logger.isDebugEnabled()) {
						MailManager.logger.debug("[不符合嫌疑要求] [双方为好友关系] [sname:" + player.getName() + "] [rname:" + receiverO.getName() + "]");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				mail = mailManager.createMail(mail);

				if (GamePlayerManager.isAllowActionStat()) {
					StatClientService.getInstance().sendPlayerAction(player.getUsername(), player.getName(), GameConstants.getInstance().getServerName(), player.getCurrentGame() == null ? player.getLastGame() : player.getCurrentGame().getGameInfo().getName(), CareerManager.careerNames[player.getCareer()], "", player.getLevel(), player.getPassport().getRegisterChannel(), player.getPassport().getRegisterChannel(), player.getPassport().getRegisterChannel(), player.getPassport().getRegisterChannel(), PlayerActionFlow.行为类型_邮件, receiverO.getName(), new java.util.Date(), 0);
				}
			} catch (WrongFormatMailException e) {
				if (coins > 0) {
					try {
						billingCenter.playerSaving(player, coins, CurrencyType.YINZI, SavingReasonType.MAIL_SEND_FAIL_RETURN_MONEY, "");
					} catch (Exception ex) {
						ex.printStackTrace();
						MailManager.logger.error("[返还邮件金币失败] [操作人:" + player.getId() + "/" + player.getName() + "/" + player.getUsername() + "] [接收人:" + receiverO.getId() + "/" + receiverO.getName() + "/" + receiverO.getUsername() + "] [返还金币时发生异常]", ex);
						if (!title.equals(Translate.BUG自动报告)) {
							HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.返还金币时出错);
							player.addMessageToRightBag(hint);
						}
					}
				}

				Cell cellss[] = mail.getCells();
				List<ArticleEntity> elist = new ArrayList<ArticleEntity>();
				sb = new StringBuffer();
				for (Cell cell : cellss) {
					long entityId = cell.getEntityId();
					int counts = cell.getCount();
					if (entityId != -1 && counts > 0) {
						ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(entityId);
						for (int i = 0; i < counts; i++) {
							elist.add(entity);
						}
					}
				}
				boolean putOK = player.putAllOK(elist.toArray(new ArticleEntity[0]));
				long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				if (elist.size() > 0 && putOK) {
					String reason = "创建邮件失败返还邮件附件";
					player.putAll(elist.toArray(new ArticleEntity[0]), reason);
					mail.clearAllCell();
					try {
						for (ArticleEntity ae : elist) {
							ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, reason, null);
						}
					} catch (Exception ex) {

					}
				} else if (elist.size() > 0) {
					HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.用户背包已满无法放置更多物品);
					player.addMessageToRightBag(hint);
					if (MailManager.logger.isDebugEnabled()) MailManager.logger.debug("[返还邮件的附件] [失败] [用户背包已满，无法放置更多物品] [邮件id:{}] [操作人:{}/{}/{}] [接收人:{}/{}/{}] [返还物品:{}] [是否能放置:{}] [金币:{}] [价格(付费):{}] [{}ms]", new Object[] { mail.getId(), player.getId(), player.getName(), player.getUsername(), receiverO.getId(), receiverO.getName(), receiverO.getUsername(), entityDesp, putOK, coins, price, (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
				}

				mail.setCoins(0);
				mail.setLastModifyDate(new java.util.Date());
				MailManager.logger.error("[创建邮件] [异常] [邮件id:" + mail.getId() + "] [操作人:" + player.getId() + "/" + player.getName() + "/" + player.getUsername() + "] [接收人:" + receiverO.getId() + "/" + receiverO.getName() + "/" + receiverO.getUsername() + "] [返还物品:" + entityDesp + "] [是否能放置:" + putOK + "] [金币:" + coins + "] [价格(付费):" + price + "] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "ms]", e);

				String error = e.getMessage();
				if (error == null) {
					error = Translate.创建邮件失败;
				}
				if (!title.equals(Translate.BUG自动报告)) {
					HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, error);
					player.addMessageToRightBag(hint);
				}
				return;
			}

			if (sendMoney > 0) {
				try {
					BillingCenter.getInstance().playerExpense(player, sendMoney, CurrencyType.BIND_YINZI, ExpenseReasonType.SEND_MAIL, "发邮件收费");
				} catch (Exception e) {
					logger.error("[邮件收费扣钱失败]", e);
				}
			}

			if (!title.equals(Translate.BUG自动报告)) {
				HINT_REQ hint = null;
				if (sendMoney == 0) {
					hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.邮件发送成功);
				} else {
					hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.translateString(Translate.邮件发送成功收费, new String[][] { { Translate.COUNT_1, sendMoney/1000 + "" } }));
				}
				player.addMessageToRightBag(hint);
			} else {
				if (GamePlayerManager.isAllowActionStat()) {
					StatClientService.getInstance().sendPlayerAction(player.getUsername(), player.getName(), GameConstants.getInstance().getServerName(), player.getCurrentGame() == null ? player.getLastGame() : player.getCurrentGame().getGameInfo().getName(), CareerManager.careerNames[player.getCareer()], "", player.getLevel(), player.getPassport().getRegisterChannel(), player.getPassport().getRegisterChannel(), player.getPassport().getRegisterChannel(), player.getPassport().getRegisterChannel(), PlayerActionFlow.行为类型_邮件, Translate.BUG自动报告, new java.util.Date(), 0);
				}
			}

			// 统计
			boolean mailContantsArticle = false;
			Cell cellss[] = mail.getCells();
			for (Cell cell : cellss) {
				long entityId = cell.getEntityId();
				int counts = cell.getCount();
				if (entityId != -1 && counts > 0) {
					mailContantsArticle = true;
					ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(entityId);
					ArticleStatManager.addToArticleStat(player, receiverO, entity, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, counts, "发邮件", null);
				}
			}

			defaultMailManager.playerSendMailTime.put(player.getId(), System.currentTimeMillis());

			if (mailContantsArticle) {
				EnterLimitManager.setValues(player, PlayerRecordType.发送带附件的邮件数);
			}
			
//			try{
//				if(WhiteListManager.getInstance().isWhiteListPlayer(player)){
//					WhiteListManager.getInstance().addMailRowData(player, receiverO, com.fy.engineserver.util.whitelist.WhiteListManager.ActionType.邮件,mail.getCoins(), names, colors, count,mail.getContent());
//				}
//			}catch(Exception e){
//				e.printStackTrace();
//			}
			
			if (MailManager.logger.isInfoEnabled()) {
				MailManager.logger.info("[发送邮件] [成功] [邮件id:{}] [操作人:{}/{}/{}] [接受人:{}/{}/{}] [{}] [{}] [金币:{}] [价格:{}] [物品:{}] [格子下标：{}] [数量：{}] [sendMoney:{}]", new Object[] { mail.getId(), player.getId(), player.getName(), player.getUsername(), receiverO.getId(), receiverO.getName(), receiverO.getUsername(), mail.getTitle(), mail.getContent(), mail.getCoins(), mail.getPrice(), entityDesp, StringUtil.arrayToString(indexes, ","), StringUtil.arrayToString(count, ",") ,sendMoney});
			}
			try {
				AchievementManager.getInstance().record(player, RecordAction.发送邮件次数);
			} catch (Exception e) {
				PlayerAimManager.logger.error("[目标系统] [统计玩家发送邮件次数] [异常] [" + player.getLogString() + "]", e);
			}

			try {
				if (sendCount > 0) {
					Player down = player;
					Player up = receiverO;
					int amount = sendCount;
					ArticleRelationShipManager msm = ArticleRelationShipManager.getInstance();
					if (msm != null) {
						msm.addArticleMove(down, up, amount);
					}
				} else {
					logger.info("[不进行物品转移统计] [mailId:" + mail.getId() + "] [sendCount:" + sendCount + "]");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[创建邮件失败]", e);
			String error = e.getMessage();
			if (error == null) {
				error = Translate.创建邮件失败;
			}
			if (!title.equals(Translate.BUG自动报告)) {
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, error);
				player.addMessageToRightBag(hint);
			}
		}
	}

	public void confirmCreateMail(RequestMessage message, Player player) {
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

		try {
			if (RelationShipHelper.checkForbidAndSendMessage(player)) {
				if (RelationShipHelper.logger.isInfoEnabled()) {
					RelationShipHelper.logger.info("[玩家因为打金行为被限制发送邮件] [" + player.getLogString() + "]");
				}
				return;
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您的角色处于锁定状态暂时不能使用邮箱);
			player.addMessageToRightBag(nreq);
			return;
		}
		
		if (ChatMessageService.getInstance().isSilence(player.getId()) >= 2) {
			EnterLimitManager.logger.warn("[用户被限制邮件] ["+player.getUsername()+"] ["+player.getName()+"] ["+player.getId()+"] ["+player.getLevel()+"] 银子:["+player.getSilver()+"]");
			return;
		}
		
		MAIL_CREATE_REQ req = (MAIL_CREATE_REQ) message;
		String content = req.getMcontent();
		if (player.isInCrossServer()) {
			if (req.getTitle().equals(Translate.BUG自动报告)) {
				MailManager.logger.error("[BUG自动报告] [GMBUG] [操作人:{}/{}/{}] [内容:{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), content });
				return;
			}
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2725);
			player.addMessageToRightBag(hintreq);
			return;
		}

		int indexes[] = req.getIndexes();
		int count[] = req.getCount();
		boolean fangbao[] = req.getFangbao();
		long coins = req.getCoins();
		String title = req.getTitle();
		if (indexes == null || count == null || indexes.length != count.length || indexes.length != fangbao.length) {
			HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.邮件中的物品错误);
			player.addMessageToRightBag(hint);
			return;
		}
		if (content.length() == 0) {
			content = Translate.无;
		}
		
		String receiver = req.getReceiverName();

		Player receiverO = null;
		try {
			if (receiver != null && receiver.length() > 0) {
				receiverO = playerManager.getPlayer(receiver);
			}
		} catch (Exception e) {
			MailManager.logger.error("[发送邮件] [获得接收人失败] [" + receiver + "]", e);
		}
		if (receiverO == null) {
			MailManager.logger.error("[发送邮件失败] [接收人不存在] [操作人:{}/{}/{}] [接收人:{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), receiver });
			if (!title.equals(Translate.BUG自动报告)) {
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.无此收信人);
				player.addMessageToRightBag(hint);
			} else {
				MailManager.logger.error("[BUG自动报告] [GMBUG] [操作人:{}/{}/{}] [内容:{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), content });
			}
			return;
		}

		if (receiverO.isBlackuser(player.getId())) {
			MailManager.logger.error("[发送邮件失败] [发送人在接受人的黑名单中] [操作人:" + player.getId() + "/" + player.getName() + "/" + player.getUsername() + "] [接收人:" + receiver + "]");
			HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2737);
			player.addMessageToRightBag(hint);
			return;
		}

		if (title.length() == 0) {
			title = Translate.无标题;
		}
		long price = req.getPrice();

		if (coins < 0 || price < 0) {
			HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.邮件金币或者付费价格错误);
			player.addMessageToRightBag(hint);
			return;
		}

		for (int i = 0; i < count.length; i++) {
			if (count[i] < 0) {
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.数量错误);
				player.addMessageToRightBag(hint);
				return;
			}
		}

		// 这边还需要加上税款
		if (coins > 0 && coins > player.getSilver()) {
			MailManager.logger.error("[发送邮件失败] [金币不够] [操作人:{}/{}/{}] [发送人:{}/{}/{}] [接收人:{}/{}/{}] [附加金币:{}] [玩家拥有金币:{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), player.getId(), player.getName(), player.getUsername(), receiverO.getId(), receiverO.getName(), receiverO.getUsername(), coins, player.getSilver() });
			if (!title.equals(Translate.BUG自动报告)) {
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.金币不够);
				player.addMessageToRightBag(hint);
			}
			return;
		}

		if (price > 0) {
			boolean fujian = false;
			for (int i = 0; i < indexes.length; i++) {
				if (indexes[i] >= 0 && count[i] > 0) {
					fujian = true;
				}
			}
			if (!fujian) {
				MailManager.logger.error("[发送邮件失败] [此邮件为付费邮件，必须添加附件] [操作人:{}/{}/{}] [下标:{}] [数量:{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), (StringUtil.arrayToString(indexes, ",")), (StringUtil.arrayToString(count, ",")) });
				if (!title.equals(Translate.BUG自动报告)) {
					HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.此邮件为付费邮件需要添加附件);
					player.addMessageToRightBag(hint);
				}
				return;
			}
		}

		if (receiver != null && receiver.equals(player.getName())) {
			if (!title.equals(Translate.BUG自动报告)) {
				MailManager.logger.error("[发送邮件失败] [不能给自己发送邮件] [操作人:{}/{}/{}] [接收人:{}/{}/{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), receiverO.getId(), receiverO.getName(), receiverO.getUsername() });
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.不能给自己发送邮件);
				player.addMessageToRightBag(hint);
			}
			return;
		}

		Mail mail = new Mail();
		mail.setCoins(coins);
		mail.setContent(content);
		mail.setPoster(player.getId());
		mail.setPrice(price);
		mail.setType(Mail.TYPE_PLAYER);
		if (!receiver.matches(InitialPlayerConstant.GM)) {
			int mailNum = mailManager.getCount(receiverO.getId());
			if (mailNum >= 100) {
				MailManager.logger.error("[发送邮件失败] [收件人邮箱已满] [操作人:{}/{}/{}] [接收人:{}/{}/{}] [{}ms]", new Object[] { player.getId(), player.getName(), player.getUsername(), receiverO.getId(), receiverO.getName(), receiverO.getUsername(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
				if (!title.equals(Translate.BUG自动报告)) {
					HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.发送失败收件人邮箱已满);
					player.addMessageToRightBag(hint);
					if (receiverO.isOnline()) {
						HINT_REQ hint2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.您的邮箱已满不能接收新邮件请及时清理);
						receiverO.addMessageToRightBag(hint2);
					}
				}
				return;
			}
		}
		mail.setReceiver(receiverO.getId());
		mail.setTitle(title);
		boolean hasAppendix = false;

		boolean existTwoSameIndex = false;
		for (int i = 0; i < indexes.length; i++) {
			for (int j = 0; j < indexes.length; j++) {
				if (i != j && indexes[i] == indexes[j] && fangbao[i] == fangbao[j] && indexes[i] != -1) {
					existTwoSameIndex = true;
					break;
				}
			}
			if (existTwoSameIndex) {
				break;
			}
		}

		if (existTwoSameIndex) {
			HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.发送失败邮件附件中有两个格子指向背包中的同一个格子);
			player.addMessageToRightBag(hint);

			MailManager.logger.error("[发送邮件失败] [可能是外挂行为:邮件中多个格子指向背包中同一格子] [操作人:{}/{}/{}] [接收人:{}/{}/{}] [格子下标：{}] [数量：{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), receiverO.getId(), receiverO.getName(), receiverO.getUsername(), StringUtil.arrayToString(indexes, ","), StringUtil.arrayToString(count, ",") });
			return;
		}

		try {
			Cell cells[] = mail.getCells();
			for (int i = 0; i < indexes.length; i++) {
				if (indexes[i] >= 0) {
					int knapsackIndex = 0;
					Knapsack knap = fangbao[i] ? player.getKnapsack_fangbao() : player.getKnapsack_common();
					ArticleEntity ae = knap.getArticleEntityByCell(indexes[i]);
					if (ae != null && ae.isBinded() || (ae != null && ae.isRealBinded())) {
						MailManager.logger.error("[发送邮件失败] [可能是外挂行为:邮寄了绑定物品] [操作人:{}/{}/{}] [接收人:{}/{}/{}] [格子下标：{}] [数量：{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), receiverO.getId(), receiverO.getName(), receiverO.getUsername(), StringUtil.arrayToString(indexes, ","), StringUtil.arrayToString(count, ",") });
						HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.发送失败不能邮寄绑定的物品);
						player.addMessageToRightBag(hint);
						return;
					}
					if (ae != null) {
						int num = knap.getCell(indexes[i]).getCount();
						if (num < count[i]) {
							MailManager.logger.error("[发送邮件失败] [用户背包中没有那么多物品] [操作人:{}/{}/{}] [接收人:{}/{}/{}] [ 背包格子：{}] [个数{}] [物品：{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), receiverO.getId(), receiverO.getName(), receiverO.getUsername(), indexes[i], count[i], ae.getArticleName() });
							if (!title.equals(Translate.BUG自动报告)) {
								HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.你的背包格中没有那么多物品);
								player.addMessageToRightBag(hint);
							}
							return;
						} else {
							hasAppendix = true;
						}
					} else {
						MailManager.logger.error("[发送邮件失败] [用户背包中没有任何物品] [操作人:{}/{}/{}] [接收人:{}/{}/{}] [ 背包格子：{}] [个数{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), receiverO.getId(), receiverO.getName(), receiverO.getUsername(), indexes[i], count[i] });
						if (!title.equals(Translate.BUG自动报告)) {
							HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.你的背包格中没有任何物品);
							player.addMessageToRightBag(hint);
						}
						return;
					}
				}
			}

			BillingCenter billingCenter = BillingCenter.getInstance();
			if (coins > 0) {
				try {
					billingCenter.playerExpense(player, coins, CurrencyType.YINZI, ExpenseReasonType.MAIL_PUT_MONEY, "", -1);
					hasAppendix = true;
				} catch (Exception e) {
					e.printStackTrace();
					MailManager.logger.error("[创建邮件失败]  [操作人:" + player.getId() + "/" + player.getName() + "/" + player.getUsername() + "] [接收人:" + receiverO.getId() + "/" + receiverO.getName() + "/" + receiverO.getUsername() + "] [扣除金币时发生异常]", e);
					if (!title.equals(Translate.BUG自动报告)) {
						HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.扣除金币时出错);
						player.addMessageToRightBag(hint);
					}
					return;
				}
			}

			// 检查后，再开始删除背包中的物品 -- 
			boolean deleteFail = false;
			StringBuffer sb = new StringBuffer();
			int sendCount = 0;
			synchronized (player.tradeKey) {
				for (int i = 0; deleteFail == false && i < indexes.length; i++) {
					if (indexes[i] >= 0) {
						int knapsackIndex = 0;
						Knapsack knap = fangbao[i] ? player.getKnapsack_fangbao() : player.getKnapsack_common();
						ArticleEntity ae = knap.getArticleEntityByCell(indexes[i]);
						if (ae != null && !ae.isBinded()) {
							for (int j = 0; j < count[i]; j++) {
								if (knap.getCell(indexes[i]).getCount() < 1) {
									deleteFail = true;
									break;
								} else {
									ArticleEntity removeAe = knap.remove(indexes[i], "发送邮件删除", false);
									
									if (removeAe != null) {
										// 统计
										ArticleStatManager.addToArticleStat(player, null, removeAe, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "发送邮件删除", null);
										sendCount++;
									}
								}
							}
							cells[i].setCount(count[i]);
							cells[i].setEntityId(ae.getId());
							sb.append(ae.getId() + "/" + ae.getArticleName() + "/" + count[i] + ",");
						} else {
							sb.append(knap.getCell(indexes[i]).getEntityId() + "/" + count[i] + ",");
						}
					}
				}
			}
			String entityDesp = sb.toString();
			if (entityDesp.length() > 0) {
				entityDesp = entityDesp.substring(0, entityDesp.length() - 1);
			}

			if (deleteFail == true) {
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.发送失败删除背包物品失败);
				player.addMessageToRightBag(hint);

				MailManager.logger.error("[发送邮件失败] [删除背包物品失败] [操作人:{}/{}/{}] [接收人:{}/{}/{}] [格子下标：{}] [数量：{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), receiverO.getId(), receiverO.getName(), receiverO.getUsername(), StringUtil.arrayToString(indexes, ","), StringUtil.arrayToString(count, ",") });
				return;
			}

			Calendar cal = Calendar.getInstance();
			mail.setCreateDate(cal.getTime());
			if (price == 0) {
				cal.add(Calendar.DAY_OF_YEAR, 30);
			} else {
				cal.add(Calendar.HOUR, 1);
			}
			mail.setExpireDate(cal.getTime());
			if (hasAppendix) {
				mail.setStatus(Mail.APPENDIX_UNREAD);
			}
			try {
				boolean hasArticle = false;
				for(int i : indexes){
					if(i > 0){
						hasArticle = true;
					}
				}
				if (hasArticle) {
					try {
						BillingCenter.getInstance().playerExpense(player, 50000, CurrencyType.BIND_YINZI, ExpenseReasonType.SEND_MAIL, "发邮件收费");
					} catch (Exception e) {
						logger.error("[邮件收费扣钱失败]", e);
					}
				}
				mail = mailManager.createMail(mail);

				if (GamePlayerManager.isAllowActionStat()) {
					StatClientService.getInstance().sendPlayerAction(player.getUsername(), player.getName(), GameConstants.getInstance().getServerName(), player.getCurrentGame() == null ? player.getLastGame() : player.getCurrentGame().getGameInfo().getName(), CareerManager.careerNames[player.getCareer()], "", player.getLevel(), player.getPassport().getRegisterChannel(), player.getPassport().getRegisterChannel(), player.getPassport().getRegisterChannel(), player.getPassport().getRegisterChannel(), PlayerActionFlow.行为类型_邮件, receiverO.getName(), new java.util.Date(), 0);
				}
			} catch (WrongFormatMailException e) {
				if (coins > 0) {
					try {
						billingCenter.playerSaving(player, coins, CurrencyType.YINZI, SavingReasonType.MAIL_SEND_FAIL_RETURN_MONEY, "");
					} catch (Exception ex) {
						ex.printStackTrace();
						MailManager.logger.error("[返还邮件金币失败] [操作人:" + player.getId() + "/" + player.getName() + "/" + player.getUsername() + "] [接收人:" + receiverO.getId() + "/" + receiverO.getName() + "/" + receiverO.getUsername() + "] [返还金币时发生异常]", ex);
						if (!title.equals(Translate.BUG自动报告)) {
							HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.返还金币时出错);
							player.addMessageToRightBag(hint);
						}
					}
				}

				Cell cellss[] = mail.getCells();
				List<ArticleEntity> elist = new ArrayList<ArticleEntity>();
				sb = new StringBuffer();
				for (Cell cell : cellss) {
					long entityId = cell.getEntityId();
					int counts = cell.getCount();
					if (entityId != -1 && counts > 0) {
						ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(entityId);
						for (int i = 0; i < counts; i++) {
							elist.add(entity);
						}
					}
				}
				boolean putOK = player.putAllOK(elist.toArray(new ArticleEntity[0]));
				long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				if (elist.size() > 0 && putOK) {
					String reason = "创建邮件失败返还邮件附件";
					player.putAll(elist.toArray(new ArticleEntity[0]), reason);
					mail.clearAllCell();
					try {
						for (ArticleEntity ae : elist) {
							ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, reason, null);
						}
					} catch (Exception ex) {

					}
				} else if (elist.size() > 0) {
					HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.用户背包已满无法放置更多物品);
					player.addMessageToRightBag(hint);
					if (MailManager.logger.isDebugEnabled()) MailManager.logger.debug("[返还邮件的附件] [失败] [用户背包已满，无法放置更多物品] [邮件id:{}] [操作人:{}/{}/{}] [接收人:{}/{}/{}] [返还物品:{}] [是否能放置:{}] [金币:{}] [价格(付费):{}] [{}ms]", new Object[] { mail.getId(), player.getId(), player.getName(), player.getUsername(), receiverO.getId(), receiverO.getName(), receiverO.getUsername(), entityDesp, putOK, coins, price, (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
				}

				mail.setCoins(0);
				mail.setLastModifyDate(new java.util.Date());
				MailManager.logger.error("[创建邮件] [异常] [邮件id:" + mail.getId() + "] [操作人:" + player.getId() + "/" + player.getName() + "/" + player.getUsername() + "] [接收人:" + receiverO.getId() + "/" + receiverO.getName() + "/" + receiverO.getUsername() + "] [返还物品:" + entityDesp + "] [是否能放置:" + putOK + "] [金币:" + coins + "] [价格(付费):" + price + "] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "ms]", e);

				String error = e.getMessage();
				if (error == null) {
					error = Translate.创建邮件失败;
				}
				if (!title.equals(Translate.BUG自动报告)) {
					HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, error);
					player.addMessageToRightBag(hint);
				}
				return;
			}

			if (!title.equals(Translate.BUG自动报告)) {
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.邮件发送成功);
				player.addMessageToRightBag(hint);
			} else {
				if (GamePlayerManager.isAllowActionStat()) {
					StatClientService.getInstance().sendPlayerAction(player.getUsername(), player.getName(), GameConstants.getInstance().getServerName(), player.getCurrentGame() == null ? player.getLastGame() : player.getCurrentGame().getGameInfo().getName(), CareerManager.careerNames[player.getCareer()], "", player.getLevel(), player.getPassport().getRegisterChannel(), player.getPassport().getRegisterChannel(), player.getPassport().getRegisterChannel(), player.getPassport().getRegisterChannel(), PlayerActionFlow.行为类型_邮件, Translate.BUG自动报告, new java.util.Date(), 0);
				}
			}

			boolean mailContantsArticle = false;
			// 统计
			Cell cellss[] = mail.getCells();
			for (Cell cell : cellss) {
				long entityId = cell.getEntityId();
				int counts = cell.getCount();
				if (entityId != -1 && counts > 0) {
					mailContantsArticle = true;
					ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(entityId);
					ArticleStatManager.addToArticleStat(player, receiverO, entity, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, counts, "发邮件", null);
				}
			}
			if (mailContantsArticle) {
				EnterLimitManager.setValues(player, PlayerRecordType.发送带附件的邮件数);
			}

			if (MailManager.logger.isInfoEnabled()) {
				MailManager.logger.info("[发送邮件] [成功] [邮件id:{}] [操作人:{}/{}/{}] [接受人:{}/{}/{}] [{}] [{}] [金币:{}] [价格:{}] [物品:{}] [格子下标：{}] [数量：{}]", new Object[] { mail.getId(), player.getId(), player.getName(), player.getUsername(), receiverO.getId(), receiverO.getName(), receiverO.getUsername(), mail.getTitle(), mail.getContent(), mail.getCoins(), mail.getPrice(), entityDesp, StringUtil.arrayToString(indexes, ","), StringUtil.arrayToString(count, ",") });
			}
			
			try {
				AchievementManager.getInstance().record(player, RecordAction.发送邮件次数);
			} catch (Exception e) {
				PlayerAimManager.logger.error("[目标系统] [统计玩家发送邮件次数] [异常] [" + player.getLogString() + "]", e);
			}

			try {
				if (sendCount > 0) {
					Player down = player;
					Player up = receiverO;
					int amount = sendCount;
					ArticleRelationShipManager msm = ArticleRelationShipManager.getInstance();
					if (msm != null) {
						msm.addArticleMove(down, up, amount);
					}
				} else {
					logger.info("[不进行物品转移统计] [mailId:" + mail.getId() + "] [sendCount:" + sendCount + "]");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[创建邮件失败]", e);
			String error = e.getMessage();
			if (error == null) {
				error = Translate.创建邮件失败;
			}
			if (!title.equals(Translate.BUG自动报告)) {
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, error);
				player.addMessageToRightBag(hint);
			}
		}
		return;
	}

	public ResponseMessage handle_MAIL_DELETE_REQ(Connection conn, RequestMessage message, Player player) {
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2724);
			player.addMessageToRightBag(nreq);
			return null;
		}
		if (player.isInCrossServer()) {
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2725);
			player.addMessageToRightBag(hintreq);
			return null;
		}
		MAIL_DELETE_REQ req = (MAIL_DELETE_REQ) message;
		long id = req.getId();
		Mail mail = mailManager.getMail(id);

		long receiver = mail.getReceiver();
		if (receiver != player.getId()) {
			if (MailManager.logger.isWarnEnabled()) MailManager.logger.warn("[发现外挂] [试图删除其他人的邮件] [邮件id:{}] [操作人:{}/{}/{}] [{}]", new Object[] { mail.getId(), player.getId(), player.getName(), player.getUsername(), conn.getName() });
			return null;
		}

		Player receiverO = null;
		try {
			receiverO = playerManager.getPlayer(receiver);
		} catch (Exception e) {
			MailManager.logger.error("[删除邮件] [获得接收人失败] [" + receiver + "]", e);
		}

		if (mail != null) {
			if (mail.getStatus() == Mail.NORMAL_UNREAD) {
				mail.setStatus(Mail.NORMAL_READED);
				player.removeNewMailCount();
			} else if (mail.getStatus() == Mail.APPENDIX_UNREAD) {
				mail.setStatus(Mail.APPENDIX_READED);
				player.removeNewMailCount();
			}
			// 如果邮件中还有物品，那么直接退回邮件
			if (mail.hasArticleEntities() || mail.getCoins() > 0) {
				long poster = mail.getPoster();
				if (poster < 0 || mail.getType() == Mail.TYPE_SYSTEM) {
					// 如果是系统邮件， 直接删除
					mailManager.deleteMail(id);
					if (MailManager.logger.isWarnEnabled()) MailManager.logger.warn("[删除邮件] [系统邮件，直接删除] [邮件id:{}] [操作人:{}/{}/{}] [receiver:{}] [{}ms]", new Object[] { mail.getId(), player.getId(), player.getName(), player.getUsername(), (receiverO != null ? receiverO.getId() + "/" + receiverO.getName() + "/" + receiverO.getUsername() : receiver), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });

				} else {
					mail.setReceiver(poster);
					if (mail.getPrice() > 0) {// 付费邮件被退信
						mail.setPoster(-1);// 设成系统退信，否则收信后不小心删除邮件-》再次退信，对方就能免费取出附件。
					} else {
						mail.setPoster(receiver);
					}
					mail.setPrice(0);
					mail.setTitle(Translate.text_2762 + mail.getTitle());
					mail.setStatus(Mail.APPENDIX_UNREAD);
					mail.setContent(Translate.text_2585 + player.getName() + Translate.text_2763 + mail.getContent());
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DAY_OF_YEAR, 30);
					mail.setExpireDate(cal.getTime());
					if (MailManager.logger.isWarnEnabled()) MailManager.logger.warn("[删除邮件] [包含附件，回退给发件人] [邮件id:{}] [操作人:{}/{}/{}] [receiver:{}] [{}ms]", new Object[] { mail.getId(), player.getId(), player.getName(), player.getUsername(), (receiverO != null ? receiverO.getId() + "/" + receiverO.getName() + "/" + receiverO.getUsername() : receiver), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
				}
			} else {
				mailManager.deleteMail(id);
				if (MailManager.logger.isWarnEnabled()) MailManager.logger.warn("[删除邮件] [直接删除] [邮件id:{}] [操作人:{}/{}/{}] [receiver:{}] [{}ms]", new Object[] { mail.getId(), player.getId(), player.getName(), player.getUsername(), (receiverO != null ? receiverO.getId() + "/" + receiverO.getName() + "/" + receiverO.getUsername() : receiver), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
			}
			player.setLastCheckMailTime(0);
		}

		return null;
	}

	public ResponseMessage handle_MAIL_DELETE_ALL_READED_REQ(Connection conn, RequestMessage message, Player player) {
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player.isLocked()) {
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2724);
			player.addMessageToRightBag(nreq);
			return null;
		}

		mailManager.deleteAllReadedNotHasAppendixsMail(player);
		if (MailManager.logger.isWarnEnabled()) MailManager.logger.warn("[批量删除邮件] [直接删除所有普通已经读取且没有附件邮件] [操作人:{}] [{}ms]", new Object[] { player.getLogString(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });

		player.setLastCheckMailTime(0);

		return new MAIL_DELETE_ALL_READED_RES(message.getSequnceNum(), "");
	}

	public boolean handleResponseMessage(Connection conn, RequestMessage req, ResponseMessage message) throws ConnectionException, Exception {
		return false;
	}
}
