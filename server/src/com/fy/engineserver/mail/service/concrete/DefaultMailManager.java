package com.fy.engineserver.mail.service.concrete;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fy.engineserver.dajing.DajingStudioManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.exception.WrongFormatMailException;
import com.fy.engineserver.mail.Mail;
import com.fy.engineserver.mail.MailSendType;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.MAIL_LIST_NEW_RES;
import com.fy.engineserver.message.MAIL_LIST_RES;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.PlayerSimpleInfo;
import com.fy.engineserver.sprite.PlayerSimpleInfoManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.mail.JavaMailUtils;
import com.xuanzhi.tools.queue.DefaultQueue;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.WordFilter;

public class DefaultMailManager extends MailManager implements Runnable{

	public static LruMapCache mCache;

	public static int MAIL_MONEY = 50000;
	public static int FREE_MAIL_NUM = 10;
	public static long MAIL_SPACE_TIME = 1000L * 5;
	public int sendMailToday = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
	public Map<Long, Integer> playerSendMailNum = new ConcurrentHashMap<Long, Integer>();
	public Map<Long, Long> playerSendMailTime = new ConcurrentHashMap<Long, Long>();

	public SimpleEntityManager<Mail> getEm() {
		return em;
	}

	public void setEm(SimpleEntityManager<Mail> em) {
		this.em = em;
	}


	
	// 更新周期，s
	protected long updatePeriod = 5;

	protected boolean running = true;
	protected Thread thread;

	protected Thread threadForHandleMessage;
	
	protected DefaultQueue queue = new DefaultQueue(10240);
	
	public static class PlayerMailRequest{
		public PlayerMailRequest(String type,Player p){
			this.type = type;
			this.player = p;
		}
		String type ;
		Player player;
	}
	
	public static String createMsgData (Mail m) {
		try {
			String md5 = StringUtil.hash(m.getCoins()+"");
			int rv = (int)m.getId()%1000;
			for (int i = 0; i < md5.length(); i++) {
				if (i%2 == 0) {
					rv += md5.charAt(i);
				}else {
					rv -= md5.charAt(i);
				}
			}
			logger.warn("[createMsgData] [MID:{}] [发{}] [收{}] [银{}] [{}]", new Object[]{m.getId(), m.getPoster(), m.getReceiver(), m.getCoins(), rv});
			return rv + "";
		} catch(Exception e) {
			logger.error("createMsgData出错:", e);
		}
		return "";
	}
	
	public static void checkMailMsg(Mail m) {
		String nowMsg = createMsgData(m);
		if (!nowMsg.equals(m.getMailMsgDate())) {
			sendErrorMail(m, nowMsg);
		}
	}
	
	public static void sendMail(String title, String content) {
		GameConstants gc = GameConstants.getInstance();
		StringBuffer sb = new StringBuffer();
		sb.append(content);
		sb.append("<HR>" + DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		ArrayList<String> args = new ArrayList<String>();
		args.add("-username");
		args.add("wtx062@126.com");
		args.add("-password");
		args.add("wangtianxin1986");

		args.add("-smtp");
		args.add("smtp.126.com");
		args.add("-from");
		args.add("wtx062@126.com");
		args.add("-to");
		String address_to = "";

		String[] addresses = {"3472335707@qq.com","116004910@qq.com"};
		if (addresses != null) {
			for (String address : addresses) {
				address_to += address + ",";
			}
		}

		if (!"".equals(address_to)) {
			args.add(address_to);
			args.add("-subject");
			if(gc != null){
				args.add(title + "["+gc.getServerName()+"]");
			}else{
				args.add(title);
			}
			args.add("-message");
			args.add(sb.toString());
			args.add("-contenttype");
			args.add("text/html;charset=utf-8");
			//System.out.println(Arrays.toString(args.toArray(new String[0])));
			try {
				JavaMailUtils.sendMail(args.toArray(new String[0]));
				//System.out.println(Arrays.toString(args.toArray(new String[0])));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void sendErrorMail(Mail m, String nowMsg) {
		try {
//			if (EnterLimitManager.isCheckSilver) {
				String content = "邮件出现银子的加密与银子数目不符合";
				GameConstants gc = GameConstants.getInstance();
				content += "[飘渺寻仙曲:游戏服]";
				StringBuffer sb = new StringBuffer();
				sb.append(content);
				sb.append("<br>");
				sb.append("邮件ID:" + m.getId() + "; 收件人:" + m.getReceiver() + " 银子:" + m.getCoins() + " 加密:" + m.getMailMsgDate() + " 应该是:" + nowMsg);
				sb.append("<br>" + DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
				ArrayList<String> args = new ArrayList<String>();
				args.add("-username");
				args.add("wtx062@126.com");
				args.add("-password");
				args.add("wangtianxin1986");
	
				args.add("-smtp");
				args.add("smtp.126.com");
				args.add("-from");
				args.add("wtx062@126.com");
				args.add("-to");
				//String address_to = "";
				args.add("3472335707@qq.com,116004910@qq.com");
				args.add("-subject");
				args.add("[飘渺寻仙曲] [邮件异常] [平台 " + PlatformManager.getInstance().getPlatform().getPlatformName() + "] ["+gc.getServerName()+"]");
				args.add("-message");
				args.add(sb.toString());
				args.add("-contenttype");
				args.add("text/html;charset=gbk");
				try {
					JavaMailUtils.sendMail(args.toArray(new String[0]));
				} catch (Exception e) {
					e.printStackTrace();
				}
//			}
			logger.warn("[检查到问题邮件] [发{}] [收{}] [银{}] [{}]", new Object[]{m.getPoster(), m.getReceiver(), m.getCoins(), m.getMailMsgDate()});
		} catch(Exception e) {
			logger.error("sendError出错", e);
		}
	}
	
	public void initialize() {
		
		em = SimpleEntityManagerFactory.getSimpleEntityManager(Mail.class);
		
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		mCache = new LruMapCache(32 * 1024 * 1024, 4 * 60 * 60 * 1000);
		self = this;

		Thread mailChecker = new Thread(new ExpireMailChecker(), "ExpireMailChecker");
		mailChecker.start();

		threadForHandleMessage = new Thread(this,"Mail-Handle-Thread");
		threadForHandleMessage.start();
		
		ServiceStartRecord.startLog(this);
	}

	public void run(){
		
		while(running){
			try{
				PlayerMailRequest r = (PlayerMailRequest)queue.pop(1000);
				if(r != null){
					if(r.type.equals("Option_Mail_OpenMail")){
						handle_Option_Mail_OpenMail(r.player);
						handle_Option_Mail_OpenMail_New(r.player);
					}
				}
			}catch(Exception e){
				if(logger.isWarnEnabled())
					logger.warn("[处理邮件用户请求的线程] [出现异常]",e);
				
			}
		}
		if(logger.isWarnEnabled())
			logger.warn("[处理邮件用户请求的线程] [停止运行]");
		
	}
	
	public void addPlayerMailRequestToQueue(PlayerMailRequest req){
		queue.push(req);
	}
	
	public void handle_Option_Mail_OpenMail(Player player){
		MailManager mm = this;
		PlayerManager pm = PlayerManager.getInstance();
		int pageIndex = 0;
		int pageNum = 10;
		int count = mm.getCount(player.getId());
		if(pageNum < 0){
			pageNum = 20;
		}
		if(pageNum > 100){
			pageNum = 100;
		}
		int pages = count / pageNum;
		if (count % pageNum != 0) {
			pages++;
		}
		if(pageIndex < 0){
			pageIndex = 0;
		}
		List<Mail> mails = mm.getAllMails(player, pageIndex * pageNum,
				pageNum);
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
					Player pp = pm.getPlayer(posterId);
					senderNames[i] = pp.getName();
				} catch (Exception e) {
					e.printStackTrace();
					MailManager.logger.error("[获取邮件列表异常] [找不到发信人] [id:"+ posterId + "]", e);
					senderNames[i] = Translate.未知;
				}
			}
			types[i] = (byte) mail.getType();
			prices[i] = new Long(mail.getPrice()).intValue();
			status[i] = (byte) mail.getStatus();
			expiredate[i] = mail.getExpireDate().getTime();
		}
		if (ids.length >= 100) {
			HINT_REQ hintreq = new HINT_REQ(
					GameMessageFactory.nextSequnceNum(), (byte) 1,
					Translate.您的邮箱已满清理后才能收新邮件);
			player.addMessageToRightBag(hintreq);
		}
		MAIL_LIST_RES res = new MAIL_LIST_RES(GameMessageFactory.nextSequnceNum(), ids, types,
				status, titles, senderNames, expiredate, pages, pageIndex,
				prices);
		StringBuffer sb = new StringBuffer();
		if(ids != null){
			for(long id : ids){
				sb.append(id+",");
			}
			
		}
		player.addMessageToRightBag(res);
		if (MailManager.logger.isInfoEnabled()) {
			MailManager.logger.info("[邮件列表] [操作人:{}/{}/{}] [总数:{}] [总页数:{}] [当前页数:{}] [每页数量:{}] [{}]", new Object[]{player.getId(),player.getName(),player.getUsername(),count,pages,pageIndex,pageNum,sb});
		}
	}
	
	public void handle_Option_Mail_OpenMail_New(Player player){
		MailManager mm = this;
		PlayerManager pm = PlayerManager.getInstance();
		int pageIndex = 0;
		int pageNum = 10;
		int count = mm.getCount(player.getId());
		if(pageNum < 0){
			pageNum = 20;
		}
		if(pageNum > 100){
			pageNum = 100;
		}
		int pages = count / pageNum;
		if (count % pageNum != 0) {
			pages++;
		}
		if(pageIndex < 0){
			pageIndex = 0;
		}
		List<Mail> mails = mm.getAllMails(player, Mail.TYPE_PLAYER, pageIndex * pageNum,
				pageNum);
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
					Player pp = pm.getPlayer(posterId);
					senderNames[i] = pp.getName();
				} catch (Exception e) {
					e.printStackTrace();
					MailManager.logger.error("[获取邮件列表异常] [找不到发信人] [id:"+ posterId + "]", e);
					senderNames[i] = Translate.未知;
				}
			}
			types[i] = (byte) mail.getType();
			prices[i] = new Long(mail.getPrice()).intValue();
			status[i] = (byte) mail.getStatus();
			expiredate[i] = mail.getExpireDate().getTime();
		}
		if (ids.length >= 100) {
			HINT_REQ hintreq = new HINT_REQ(
					GameMessageFactory.nextSequnceNum(), (byte) 1,
					Translate.您的邮箱已满清理后才能收新邮件);
			player.addMessageToRightBag(hintreq);
		}
		MAIL_LIST_NEW_RES res = new MAIL_LIST_NEW_RES(GameMessageFactory.nextSequnceNum(), ids, types,
				status, titles, senderNames, expiredate, pages, pageIndex,
				prices);
		StringBuffer sb = new StringBuffer();
		if(ids != null){
			for(long id : ids){
				sb.append(id+",");
			}
			
		}
		player.addMessageToRightBag(res);
		if (MailManager.logger.isInfoEnabled()) {
			MailManager.logger.info("[邮件列表] [操作人:{}/{}/{}] [总数:{}] [总页数:{}] [当前页数:{}] [每页数量:{}] [{}]", new Object[]{player.getId(),player.getName(),player.getUsername(),count,pages,pageIndex,pageNum,sb});
		}
	}
	
	@Override
	public Mail createMail(Mail mail) throws WrongFormatMailException {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (mail.getReceiver() <= 0 || mail.getTitle() == null || mail.getTitle().trim().length() == 0) {
			throw new WrongFormatMailException(Translate.邮件格式错误缺少发件人收件人或者标题);
		}
		if (mail.getPoster() != -1) {
			// 邮件内容,只需要对0级词汇完全过滤
			WordFilter filter = WordFilter.getInstance();
			if (!filter.cvalid(mail.getTitle(), 0) || !filter.cvalid(mail.getContent() == null ? "" : mail.getContent(), 0)) {
				logger.warn("[邮件非法] [发{}] [收{}] [{}] [{}]", new Object[]{mail.getPoster(), mail.getReceiver(), mail.getTitle(), mail.getContent(), });
				throw new WrongFormatMailException(Translate.邮件标题或内容含有非法信息);
			}
		}
		if (mail.getPoster() != -1 && mail.getContent().indexOf("请警惕骗子行为") == -1) {
			mail.setContent(mail.getContent()+ Translate.NOT_SYS_MAIL);
		}
		try {
//			PlayerSimpleInfo receiver = PlayerSimpleInfoManager.getInstance().getInfoById(mail.getReceiver());
			if (PlayerManager.getInstance().isOnline(mail.getReceiver())) {
				PlayerManager.getInstance().getPlayer(mail.getReceiver()).setLastCheckMailTime(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		boolean hasAppendix = false;
		if (mail.getCoins() > 0) {
			hasAppendix = true;
		}
		String attachment = "";
		Cell[] cells = mail.getCells();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleEntity entity = null;
		int acount = 0;
		int tcount = 0;
		DajingStudioManager djm = DajingStudioManager.getInstance();
		long receiverId = mail.getReceiver();
		long posterId = mail.getPoster();
//		Player receiver = null;
//		Player poster = null;
		PlayerSimpleInfo simpleReceiver = null;
		PlayerSimpleInfo simplePoster = null;
//		try {
//			receiver = PlayerManager.getInstance().getPlayerInCache(receiverId);
//			if (posterId != -1) {
//				poster = PlayerManager.getInstance().getPlayerInCache(posterId);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		try{
			simpleReceiver = PlayerSimpleInfoManager.getInstance().getInfoById(mail.getReceiver());
			if(posterId != -1){
				simplePoster = PlayerSimpleInfoManager.getInstance().getInfoById(posterId);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		if (cells != null) {
			for (int i = 0; i < cells.length; i++) {
				if (cells[i] != null && cells[i].getEntityId() > 0 && cells[i].getCount() > 0) {
					ArticleEntity ae = aem.getEntity(cells[i].getEntityId());
					if (ae != null) {
						attachment += ae.getId() + "/" + ae.getArticleName() + "/" + cells[i].getCount() + ",";
						hasAppendix = true;
						if (entity == null) {
							entity = ae;
							tcount = cells[i].getCount();
						}
						acount++;
						//统计,当发送方不是系统时需要把银子的发送数据统计
						if(simplePoster != null && simpleReceiver != null){
							if(Translate.银块.equals(ae.getArticleName())){
								if(djm != null){
									try{
										djm.notify_邮件附件交易银块(PlayerManager.getInstance().getPlayer(posterId), PlayerManager.getInstance().getPlayer(receiverId), cells[i].getCount());
									}catch(Exception ex){
										ex.printStackTrace();
									}
								}
							}
						}
					} else {
						attachment += cells[i].getEntityId() + Translate.text_2727 + cells[i].getCount() + ",";
					}
				} else {
					attachment += Translate.text_4695;
				}
			}
			if (attachment.length() > 0) {
				attachment = attachment.substring(0, attachment.length() - 1);
			}
		}
		if (hasAppendix) {
			mail.setStatus(Mail.APPENDIX_UNREAD);
		}else{
			mail.setStatus(Mail.NORMAL_UNREAD);
		}

		
		mail.setId(this.getNextId());
		mail.setMailMsgDate(DefaultMailManager.createMsgData(mail));
		mail.setNewFlag(true);
		// mail = mailDAO.saveMail(mail);
		mCache.put(mail.getId(), mail);
		em.notifyNewObject(mail);
		if(logger.isInfoEnabled())
			logger.info("[创建邮件] [邮件id:{}] [发件人:{}] [收件人:{}] [标题:{}] [内容:{}] [附件：{}] [附加游戏币：{}] [付费价格：{}] [{}] [{}ms]", new Object[]{mail.getId(),(simplePoster != null ? posterId + "/" + simplePoster.getName() + "/" + simplePoster.getUsername() : posterId),(simpleReceiver != null ? receiverId + "/" + simpleReceiver.getName() + "/" + simpleReceiver.getUsername() : receiverId),mail.getTitle(),mail.getContent(),attachment,mail.getCoins(),mail.getPrice(), mail.getMailMsgDate(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now)});
		return mail;
	}

	public long 玩家发送邮件需要钱(Mail mail){
		long cost = 100;
		if(mail != null){
			if(mail.getPoster() <= 0){
				return cost = 0;
			}
			if(mail.getCoins() > 0){
				cost = cost + mail.getCoins() / 100 + 1;
			}
		}
		return cost;
	}
	private synchronized long getNextId() {
		try {
			return em.nextId();
		} catch (Exception e) {
			throw new RuntimeException("致命异常，无法获取邮件最大id");
		}
	}

	/**
	 * 合服用
	 * 
	 * @param mail
	 * @return
	 * @throws WrongFormatMailException
	 */
	public Mail importMail(Mail mail) {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		try {
			em.flush(mail);
			if(logger.isInfoEnabled())
				logger.info("[导入邮件] [邮件id:{}] [发件人:{}] [收件人:{}] [标题:{}] [内容:{}] [附加游戏币：{}] [付费价格：{}] [{}ms]", new Object[]{mail.getId(),mail.getPoster(),mail.getReceiver(),mail.getTitle(),mail.getContent(),mail.getCoins(),mail.getPrice(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now)});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mail;
	}

//	@Override
//	public TransactionFuture transactionCreateMail(Mail mail) {
//		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//		TransactionFutureFactory factory = TransactionFutureFactory.getInstance();
//		if (mail.getReceiver() <= 0 || mail.getTitle() == null) {
//			return factory.getTransactionFuture(null, false, null, this.getClass().getName() + "@createTransactionMail");
//		}
//		String attachment = "";
//		Cell[] cells = mail.getCells();
//		boolean hasAppendix = false;
//		ArticleEntityManager aem = ArticleEntityManager.getInstance();
//		if (cells != null) {
//			for (int i = 0; i < cells.length; i++) {
//				if (cells[i] != null && cells[i].getEntityId() > 0 && cells[i].getCount() > 0) {
//					ArticleEntity ae = aem.getEntity(cells[i].getEntityId());
//					if (ae != null) {
//						attachment += ae.getId() + "/" + ae.getArticleName() + "/" + cells[i].getCount() + ",";
//					} else {
//						attachment += cells[i].getEntityId() + Translate.text_2727 + cells[i].getCount() + ",";
//					}
//					hasAppendix = true;
//				} else {
//					attachment += Translate.text_4695;
//				}
//			}
//			if (attachment.length() > 0) {
//				attachment = attachment.substring(0, attachment.length() - 1);
//			}
//		}
//		if (mail.getCoins() > 0) {
//			hasAppendix = true;
//		}
//		if (hasAppendix) {
//			mail.setStatus(Mail.APPENDIX_UNREAD);
//		}
//		long receiverId = mail.getReceiver();
//		long posterId = mail.getPoster();
//		Player receiver = null;
//		Player poster = null;
//		try {
//			receiver = PlayerManager.getInstance().getPlayer(receiverId);
//			if (receiver != null) {
//				receiver.setLastCheckMailTime(0);
//				receiver.setMailMark(true);
//			}
//			if (posterId != -1) {
//				poster = PlayerManager.getInstance().getPlayer(posterId);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		mail.setId(this.getNextId());
//		em.save(mail);
//		if(logger.isInfoEnabled())
//			logger.info("[创建邮件(事务)] [邮件id:{}] [发件人:{}] [收件人:{}] [标题:{}] [内容:{}] [附件：{}] [附加游戏币：{}] [付费价格：{}] [{}ms]", new Object[]{mail.getId(),(poster != null ? posterId + "/" + poster.getName() + "/" + poster.getUsername() : posterId),(receiver != null ? receiverId + "/" + receiver.getName() + "/" + receiver.getUsername() : receiverId),mail.getTitle(),mail.getContent(),attachment,mail.getCoins(),mail.getPrice(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now)});
//		return future;
//	}

	@Override
	public void deleteMail(long id) {

		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		Mail mail = getMail(id);
		if (mail != null) {
			mail.setStatus(Mail.DELETED);
			try {
				em.remove(mail);
				mCache.remove(id);
			} catch (Exception e) {
				logger.error("[删除邮件] [异常] ["+id+"]",e);
			}
		}

		long receiverId = mail.getReceiver();
		long posterId = mail.getPoster();
//		Player receiver = null;
//		Player poster = null;
//		try {
//			receiver = PlayerManager.getInstance().getPlayer(receiverId);
//			if (posterId != -1) {
//				poster = PlayerManager.getInstance().getPlayer(posterId);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		PlayerSimpleInfo simpleReceiver = null;
		PlayerSimpleInfo simplePoster = null;
		try{
			simpleReceiver = PlayerSimpleInfoManager.getInstance().getInfoById(mail.getReceiver());
			if(posterId != -1){
				simplePoster = PlayerSimpleInfoManager.getInstance().getInfoById(posterId);
			}
		}catch(Exception ex){
			ex.printStackTrace();
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
					attachment += Translate.text_4695;
				}
			}
			if (attachment.length() > 0) {
				attachment = attachment.substring(0, attachment.length() - 1);
			}
		}

		if(logger.isInfoEnabled())
			logger.info("[删除邮件] [邮件id:{}] [发件人:{}] [收件人:{}] [标题:{}] [内容:{}] [附件：{}] [附加游戏币：{}] [付费价格：{}] [{}ms]", new Object[]{mail.getId(),(simplePoster != null ? posterId + "/" + simplePoster.getName() + "/" + simplePoster.getUsername() : posterId),(simpleReceiver != null ? receiverId + "/" + simpleReceiver.getName() + "/" + simpleReceiver.getUsername() : receiverId),mail.getTitle(),mail.getContent(),attachment,mail.getCoins(),mail.getPrice(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now)});
	}
	
	@Override
	public void deleteAllReadedNotHasAppendixsMail(Player player) {
		long count;
		long now = System.currentTimeMillis();
		try {
			//count = em.count(Mail.class, "status="+Mail.NORMAL_READED+" and receiver="+player.getId());
			count = em.count(Mail.class, "status=? and receiver=?",new Object[]{Mail.NORMAL_READED,player.getId()});
		
			//List<Mail> list = em.query(Mail.class, "status="+Mail.NORMAL_READED+" and receiver="+player.getId(), "", 1, count + 1);
			List<Mail> list = em.query(Mail.class, "status=? and receiver=?", new Object[]{Mail.NORMAL_READED,player.getId()},"", 1, count + 1);
			if(list != null){
				for(Mail mail : list){
					try {
						em.remove(mail);
						mCache.remove(mail.getId());
					} catch (Exception e) {
						logger.error("[批量删除邮件] [异常]",e);
					}
				}
			}
			if(logger.isInfoEnabled())
				logger.info("[批量删除邮件] [收件人:{"+player.getLogString()+"}] ["+(System.currentTimeMillis() - now)+"ms]");
		} catch (Exception ex) {
			logger.error("[批量删除邮件] [异常]",ex);
		}
	}
	
	@Override
	public int hasNewMail(Player player) {
		long count = 0;
		try {
			//count = em.count(Mail.class, "(status="+Mail.APPENDIX_UNREAD+" or status="+Mail.NORMAL_UNREAD+") and receiver="+player.getId());
			
			count = em.count(Mail.class, "(status=? or status=?) and receiver=?",new Object[]{Mail.APPENDIX_UNREAD,Mail.NORMAL_UNREAD,player.getId()});
		} catch (Exception e) {
			e.printStackTrace();
		}
			if(MailManager.logger.isInfoEnabled())
				MailManager.logger.info("[查询是否有新邮件] [{}] [{}]", new Object[]{count,player.getLogString()});
		return (int)count;
	}

	@Override
	public List<Mail> getAllMails(Player player) {
		long count = 0;
		try {
			//count = em.count(Mail.class, "status<>"+Mail.DELETED +" and receiver="+player.getId());
			count = em.count(Mail.class, "status<>? and receiver=?",new Object[]{Mail.DELETED,player.getId()});
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[邮件查询异常]",e);
		}
		if(count > 0){
			try{
				return em.query(Mail.class, "status<>? and receiver=?", new Object[]{Mail.DELETED,player.getId()} ,"", 1, count+1);
			}catch(Exception ex){
				ex.printStackTrace();
				logger.error("[邮件查询异常]",ex);
			}
		}
		return new ArrayList<Mail>();
	}

	public List<Mail> getBetweenMails(Player player, Date fromdate, Date todate) {
		long count = 0;
		
		//String querySql = "receiver="+player.getId()+" and createDate>=to_date('"+DateUtil.formatDate(fromdate,"yyyy-MM-dd HH:mm:ss")+"','yyyy-mm-dd hh24:mi:ss') and createDate<=to_date('"+DateUtil.formatDate(todate,"yyyy-MM-dd HH:mm:ss")+"','yyyy-mm-dd hh24:mi:ss')";
		
		String querySql = "receiver=? and createDate>=to_date(?,'yyyy-mm-dd hh24:mi:ss') and createDate<=to_date(?,'yyyy-mm-dd hh24:mi:ss')";
		
		if("mysql".equalsIgnoreCase(SimpleEntityManagerFactory.getDbType())){
			//querySql = "receiver="+player.getId()+" and createDate>=str_to_date('"+DateUtil.formatDate(fromdate,"yyyy-MM-dd HH:mm:ss")+"','%Y-%m-%d %H:%i:%S') and createDate<=str_to_date('"+DateUtil.formatDate(todate,"yyyy-MM-dd HH:mm:ss")+"','%Y-%m-%d %H:%i:%S')";
			querySql = "receiver=? and createDate>=str_to_date(?,'%Y-%m-%d %H:%i:%S') and createDate<=str_to_date(?,'%Y-%m-%d %H:%i:%S')";
		}
		
		try {
			count = em.count(Mail.class, querySql,new Object[]{player.getId(),DateUtil.formatDate(fromdate,"yyyy-MM-dd HH:mm:ss"),DateUtil.formatDate(todate,"yyyy-MM-dd HH:mm:ss")});
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[邮件查询异常]",e);
		}
		if(count > 0){
			try{
				return em.query(Mail.class, querySql, new Object[]{player.getId(),DateUtil.formatDate(fromdate,"yyyy-MM-dd HH:mm:ss"),DateUtil.formatDate(todate,"yyyy-MM-dd HH:mm:ss")},"", 1, count+1);
			}catch(Exception ex){
				ex.printStackTrace();
				logger.error("[邮件查询异常]",ex);
			}
		}
		return new ArrayList<Mail>();
	}

	public List<Mail> getAllMails(Player player, int start, int length) {
		try {
			//return em.query(Mail.class, "status<>"+Mail.DELETED + " and receiver="+player.getId(), "createDate desc", start+1, start+1+length);
			List<Mail> list = em.query(Mail.class, "status<>? and receiver=?", new Object[]{Mail.DELETED,player.getId()},"createDate desc", start+1, start+1+length);
			if(list != null){
				for(Mail m : list){
					if (m.getMailMsgDate() == null) {
						m.setMailMsgDate(createMsgData(m));
					}else {
						checkMailMsg(m);
					}
					if(mCache.get(m.getId()) == null){
						mCache.put(m.getId(), m);
					}
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[邮件查询异常]",e);
		}
		return new ArrayList<Mail>();
	}
	
	/**
	 * 获取某个玩家的所有邮件
	 * @param player
	 * @return
	 */
	public List<Mail> getAllMails(Player player, int mailType, int start, int length){
		if (mailType >= 0) {
			try {
				//return em.query(Mail.class, "status<>"+Mail.DELETED + " and receiver="+player.getId(), "createDate desc", start+1, start+1+length);
				List<Mail> list = em.query(Mail.class, "status<>? and receiver=? and type=?", new Object[]{Mail.DELETED,player.getId(), mailType},"createDate desc", start+1, start+1+length);
				if(list != null){
					for(Mail m : list){
						if (m.getMailMsgDate() == null) {
							m.setMailMsgDate(createMsgData(m));
						}else {
							checkMailMsg(m);
						}
						if(mCache.get(m.getId()) == null){
							mCache.put(m.getId(), m);
						}
					}
				}
				return list;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("[邮件查询异常]",e);
			}
		}else {
			return getAllMails(player, start, length);
		}
		return new ArrayList<Mail>();
	}

	public Mail getMail(long id) {
		Mail mail = (Mail) mCache.get(id);
		if (mail == null) {
			try {
				mail = em.find(id);
				if(mail==null){
					logger.warn("[通过id查询邮件] [报错:邮件不存在] [id:"+id+"]");
					return null;
				}
				if (mail.getMailMsgDate() == null) {
					mail.setMailMsgDate(createMsgData(mail));
				}else {
					checkMailMsg(mail);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (mail != null) {
				mCache.put(id, mail);
			}
		}
		return mail;
	}

	public Mail getMailInCache(long id) {
		return (Mail) mCache.get(id);
	}

	public List<Mail> getExpiredMails() {
		Date date = new Date();
		long count = 0;
		
		//String querySql = "status<>"+Mail.DELETED +" and expireDate<=to_date('"+DateUtil.formatDate(date,"yyyy-MM-dd HH:mm:ss")+"','yyyy-mm-dd hh24:mi:ss')";
		
		String querySql = "status<>? and expireDate<=to_date(?,'yyyy-mm-dd hh24:mi:ss')";
		
		if("mysql".equalsIgnoreCase(SimpleEntityManagerFactory.getDbType())){
			//querySql = "status<>"+Mail.DELETED +" and expireDate<=str_to_date('"+DateUtil.formatDate(date,"yyyy-MM-dd HH:mm:ss")+"','%Y-%m-%d %H:%i:%S')";
			querySql = "status<>? and expireDate<=str_to_date(?,'%Y-%m-%d %H:%i:%S')";
		}
		try {
			count = em.count(Mail.class, querySql,new Object[]{Mail.DELETED,DateUtil.formatDate(date,"yyyy-MM-dd HH:mm:ss")});
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(count > 0){
			try {
				if (count > 10000){
					count = 9000;
				}
				return em.query(Mail.class, querySql, new Object[]{Mail.DELETED,DateUtil.formatDate(date,"yyyy-MM-dd HH:mm:ss")},"", 1, count+1);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("[邮件查询异常]",e);
			}
		}
		return new ArrayList<Mail>();
	}

	@Override
	public List<Mail> getUnReadMails(Player player, int start, int length) {
		try{
			//return em.query(Mail.class, "(status="+Mail.APPENDIX_UNREAD+" or status="+Mail.NORMAL_UNREAD+") and receiver="+player.getId(), "createDate desc", start+1, start+1+length);
			List<Mail> ms = em.query(Mail.class, "(status=? or status=?) and receiver=?", new Object[]{Mail.APPENDIX_UNREAD,Mail.NORMAL_UNREAD,player.getId()},"createDate desc", start+1, start+1+length);
			for (Mail m : ms) {
				if (m.getMailMsgDate() == null) {
					m.setMailMsgDate(createMsgData(m));
				}else {
					checkMailMsg(m);
				}
			}
			return ms;
		}catch(Exception ex){
			logger.error("[邮件查询异常]",ex);
		}
		return new ArrayList<Mail>();
	}
	
	/**
	 * 获取某个玩家的未读邮件
	 * @param player
	 * @return
	 */
	public List<Mail> getUnReadMails(Player player, int mailType, int start, int length){
		try{
			//return em.query(Mail.class, "(status="+Mail.APPENDIX_UNREAD+" or status="+Mail.NORMAL_UNREAD+") and receiver="+player.getId(), "createDate desc", start+1, start+1+length);
			List<Mail> ms = em.query(Mail.class, "(status=? or status=?) and receiver=? and type=?", new Object[]{Mail.APPENDIX_UNREAD,Mail.NORMAL_UNREAD,player.getId(), mailType},"createDate desc", start+1, start+1+length);
			for (Mail m : ms) {
				if (m.getMailMsgDate() == null) {
					m.setMailMsgDate(createMsgData(m));
				}else {
					checkMailMsg(m);
				}
			}
			return ms;
		}catch(Exception ex){
			logger.error("[邮件查询异常]",ex);
		}
		return new ArrayList<Mail>();
	}

	@Override
	public List<Mail> getReadMails(Player player, int start, int length) {
		try{
			//return em.query(Mail.class, "(status="+Mail.APPENDIX_READED+" or status="+Mail.NORMAL_READED+") and receiver="+player.getId(), "createDate desc", start+1, start+1+length);
			List<Mail> ms = em.query(Mail.class, "(status=? or status=?) and receiver=?", new Object[]{Mail.APPENDIX_READED,Mail.NORMAL_READED,player.getId()},"createDate desc", start+1, start+1+length);
			for (Mail m : ms) {
				if (m.getMailMsgDate() == null) {
					m.setMailMsgDate(createMsgData(m));
				}else {
					checkMailMsg(m);
				}
			}
			return ms;
		}catch(Exception ex){
			logger.error("[邮件查询异常]",ex);
		}
		return new ArrayList<Mail>();
	}
	
	/**
	 * 获取某个玩家的已读邮件
	 * @param player
	 * @return
	 */
	public List<Mail> getReadMails(Player player, int mailType, int start, int length){
		try{
			//return em.query(Mail.class, "(status="+Mail.APPENDIX_READED+" or status="+Mail.NORMAL_READED+") and receiver="+player.getId(), "createDate desc", start+1, start+1+length);
			List<Mail> ms = em.query(Mail.class, "(status=? or status=?) and receiver=? and type=?", new Object[]{Mail.APPENDIX_READED,Mail.NORMAL_READED,player.getId(), mailType},"createDate desc", start+1, start+1+length);
			for (Mail m : ms) {
				if (m.getMailMsgDate() == null) {
					m.setMailMsgDate(createMsgData(m));
				}else {
					checkMailMsg(m);
				}
			}
			return ms;
		}catch(Exception ex){
			logger.error("[邮件查询异常]",ex);
		}
		return new ArrayList<Mail>();
	}

	@Override
	public List<Mail> getUnReadMailHasAppendixs(Player player, int start, int length) {
		try{
			//return em.query(Mail.class, "status="+Mail.APPENDIX_UNREAD +" and receiver="+player.getId(), "createDate desc", start+1, start+1+length);
			List<Mail> ms = em.query(Mail.class, "status=? and receiver=?", new Object[]{Mail.APPENDIX_UNREAD,player.getId()},"createDate desc", start+1, start+1+length);
			for (Mail m : ms) {
				if (m.getMailMsgDate() == null) {
					m.setMailMsgDate(createMsgData(m));
				}else {
					checkMailMsg(m);
				}
			}
			return ms;
		}catch(Exception ex){
			logger.error("[邮件查询异常]",ex);
		}
		return new ArrayList<Mail>();
	}
	
	public List<Mail> getUnReadMailHasAppendixs(Player player, int mailType, int start, int length){
		try{
			//return em.query(Mail.class, "status="+Mail.APPENDIX_UNREAD +" and receiver="+player.getId(), "createDate desc", start+1, start+1+length);
			List<Mail> ms = em.query(Mail.class, "status=? and receiver=? and type=?", new Object[]{Mail.APPENDIX_UNREAD,player.getId(), mailType},"createDate desc", start+1, start+1+length);
			for (Mail m : ms) {
				if (m.getMailMsgDate() == null) {
					m.setMailMsgDate(createMsgData(m));
				}else {
					checkMailMsg(m);
				}
			}
			return ms;
		}catch(Exception ex){
			logger.error("[邮件查询异常]",ex);
		}
		return new ArrayList<Mail>();
	}

	@Override
	public List<Mail> getReadMailHasAppendixs(Player player, int start, int length) {
		try{
			//return em.query(Mail.class, "status="+Mail.APPENDIX_READED +" and receiver="+player.getId(), "createDate desc", start+1, start+1+length);
			List<Mail> ms = em.query(Mail.class, "status=? and receiver=?", new Object[]{Mail.APPENDIX_READED,player.getId()},"createDate desc", start+1, start+1+length);
			for (Mail m : ms) {
				if (m.getMailMsgDate() == null) {
					m.setMailMsgDate(createMsgData(m));
				}else {
					checkMailMsg(m);
				}
			}
			return ms;
		}catch(Exception ex){
			logger.error("[邮件查询异常]",ex);
		}
		return new ArrayList<Mail>();
	}
	
	public List<Mail> getReadMailHasAppendixs(Player player, int mailType, int start, int length){
		try{
			//return em.query(Mail.class, "status="+Mail.APPENDIX_READED +" and receiver="+player.getId(), "createDate desc", start+1, start+1+length);
			return em.query(Mail.class, "status=? and receiver=? and type=?", new Object[]{Mail.APPENDIX_READED,player.getId(), mailType},"createDate desc", start+1, start+1+length);
		}catch(Exception ex){
			logger.error("[邮件查询异常]",ex);
		}
		return new ArrayList<Mail>();
	}

	@Override
	public List<Mail> getUnReadMails(Player player) {
		long count = 0;
		try {
			//count = em.count(Mail.class, "(status="+Mail.APPENDIX_UNREAD+" or status="+Mail.NORMAL_UNREAD+") and receiver="+player.getId());
			count = em.count(Mail.class, "(status=? or status=?) and receiver=?",new Object[]{Mail.APPENDIX_UNREAD,Mail.NORMAL_UNREAD,player.getId()});
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[邮件查询异常]",e);
		}
		if(count > 0){
			try{
				//return em.query(Mail.class, "(status="+Mail.APPENDIX_UNREAD+" or status="+Mail.NORMAL_UNREAD+") and receiver="+player.getId(), "", 1, count+1);
				return em.query(Mail.class, "(status=? or status=?) and receiver=?", new Object[]{Mail.APPENDIX_UNREAD,Mail.NORMAL_UNREAD,player.getId()}, "", 1, count+1);
			}catch(Exception ex){
				ex.printStackTrace();
				logger.error("[邮件查询异常]",ex);
			}
		}
		return new ArrayList<Mail>();
	}

	public void destroy() {
		long l = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

		em.destroy();

		System.out.println("[Destroy] 调用destroy方法保存所有邮件, cost " + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - l) + " ms");
		if(logger.isWarnEnabled())
			logger.warn("[Destroy] 调用destroy方法保存所有邮件, cost {} ms", new Object[]{(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - l)});

	}

	public long sendMail(long receiverId, ArticleEntity[] entities, String title, String content, long coins, long bindyuanbao, long rmbyuanbao, String createReason) throws Exception {
		return sendMail(-1, receiverId, entities, title, content, coins, bindyuanbao, rmbyuanbao, createReason);
	}
	
	@Override
	public long sendMail(long receiverId, ArticleEntity entities[], int[] counts, String title, String content, long coins, long bindyuanbao, long rmbyuanbao, String createReason) throws Exception{
//		Mail mail = new Mail();
//		mail.setCoins(coins);
////		mail.setBindyuanbao(bindyuanbao);
////		mail.setRmbyuanbao(rmbyuanbao);
//		mail.setContent(content);
//		mail.setPoster(-1);
//		mail.setReceiver(receiverId);
//		mail.setTitle(title);
//		if(createReason != null){
//			mail.setCreateReason(createReason);
//		}
//		if(entities!=null&&counts!=null){
//			for(int i = 0; i<entities.length;i++){
//				Article article = ArticleManager.getInstance().getArticle(entities[i].getArticleName());
//				if(article.isOverlap()){
//					mail.getCells()[i].setEntityId(entities[i].getId());
//					mail.getCells()[i].setCount(counts[i]);
//				}else{
//					mail.getCells()[i].setEntityId(entities[i].getId());
//					mail.getCells()[i].setCount(1);
//				}
//			}
//		}
//		Mail m = createMail(mail);
//		return m.getId();
		return sendMail(receiverId, entities, counts, title, content, coins, bindyuanbao, rmbyuanbao, createReason, MailSendType.系统发送,"","","");
	}
	
	//add mailSendType 1
	@Override
	public long sendMail(long posterId, long receiverId, ArticleEntity[] entities, String title, String content, long coins, long bindyuanbao, long rmbyuanbao, String createReason,MailSendType type,String playername,String ipaddress,String recorder) throws Exception {
		Mail mail = new Mail();
		mail.setCoins(coins);
		mail.setContent(content);
		mail.setPoster(posterId);
		mail.setReceiver(receiverId);
		mail.setTitle(title);
		if(createReason != null){
			mail.setCreateReason(createReason);
		}
		int colors[] = null;
		String anames [] = null;
		int index = 0;
		if (entities != null && entities.length > 0) {
			colors = new int[entities.length];
			anames = new String[entities.length];
			for (ArticleEntity entity : entities) {
				if (entity != null) {
					Article article = ArticleManager.getInstance().getArticle(entity.getArticleName());
					index++;
					if(index>=entities.length){
						index = entities.length - 1;
					}
					colors[index] = entity.getColorType();
					anames[index] = article.getName_stat();
					mail.put(entity);
				}
			}
		}
		Mail m = createMail(mail);
//		if(type==MailSendType.后台发送){
//			if(PlatformManager.getInstance().isPlatformOf(Platform.韩国) && type==MailSendType.后台发送){
//			try{
//				String [] titles =	{"类型","服务器","角色名","玩家id","IP地址","邮件标题","邮件内容","颜色集","数量集","绑银","操作人","操作时间"};
//				StringBuffer buffer = new StringBuffer();
//				buffer.append("<table style='font-size=12px;' border=1><tr bgcolor='greend'>");
//				for(String t:titles){
//					buffer.append("<th>");
//					buffer.append(t);
//					buffer.append("</th>");
//				}
//				buffer.append("</tr>");
//				
//				buffer.append("<tr><td>后台发邮件</td><td>"+GameConstants.getInstance().getServerName()+"</td><td>"+playername+"</td><td>"+receiverId+"</td><td>"+ipaddress+"</td>"+"<td>"+title+"</td>"+"<td>"+content+"</td>"+"<td>"+Arrays.toString(colors)+"</td>"+"<td>"+(entities==null?"0":entities.length)+"</td><td>"+coins+"</td><td>"+recorder+"</td>"+"<td>"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())+"</td></tr>");
//				buffer.append("</table>");
//				DataCheckManager.sendMail(title, buffer.toString());
//				if(ActivitySubSystem.logger.isInfoEnabled()){
//					ActivitySubSystem.logger.info("[GM后台发送邮件] [操作人:"+recorder+"] [角色名:"+playername+"] [角色id:"+receiverId+"] [ip:"+ipaddress+"] [物品:"+Arrays.toString(anames)+"] [颜色:"+Arrays.toString(colors)+"] [标题:"+title+"] [content:"+content+"]");
//				}
//			}catch(Exception e){
//				e.printStackTrace();
//				ActivitySubSystem.logger.warn("[GM后台发送邮件] [异常2] [操作人:"+recorder+"] [角色名:"+playername+"] [角色id:"+receiverId+"] [ip:"+ipaddress+"] [物品:"+Arrays.toString(anames)+"] [颜色:"+Arrays.toString(colors)+"] [标题:"+title+"] [content:"+content+"]",e);
//			}
//		}
			
//			{
//				try
//				{
//					if(PlayerManager.getInstance().getPlayer(posterId) != null )
//					{
//						ArticleTradeRecord articleTradeRecord =  new ArticleTradeRecord();
//						articleTradeRecord.setSellPlayerId(posterId);
//						articleTradeRecord.setBuyPlayerId(receiverId);
//						if (entities != null && entities.length > 0) {
//							long[] articleIds = new long[entities.length];
//							for(int i = 0; i < articleIds.length; i++)
//							{
//								articleIds[i] = entities[i].getId();
//							}
//
//							articleTradeRecord.setArticleIds(articleIds);
//
//							articleTradeRecord.setDesc("发邮件");
//							articleTradeRecord.setTradeTime(System.currentTimeMillis());
//
//							ArticleTradeRecordManager.getInstance().notifyNew(articleTradeRecord);
//							if(ArticleTradeRecordManager.logger.isInfoEnabled())
//								ArticleTradeRecordManager.logger.info("[通过defaultMailManager创建邮件交易记录] [成功] [sell:"+mail.getPoster()+"] [buyer:"+mail.getReceiver()+"]"+articleTradeRecord.getLogStr()+"");
//						}
//						else
//						{
//							ArticleTradeRecordManager.logger.warn("[通过defaultMailManager创建邮件交易记录] [不需要创建交易记录因为邮件无道具] [sell:"+mail.getPoster()+"] [buyer:"+mail.getReceiver()+"]");
//						}
//					}
//					else
//					{
//						ArticleTradeRecordManager.logger.warn("[通过defaultMailManager创建邮件交易记录] [失败] [没有获取到发邮件人角色] [sell:"+mail.getPoster()+"] [buyer:"+mail.getReceiver()+"]");
//					}
//				}
//				catch(Exception e)
//				{
//					ArticleTradeRecordManager.logger.error("[通过defaultMailManager创建邮件交易记录] [失败] [出现未知异常] [sell:"+mail.getPoster()+"] [buyer:"+mail.getReceiver()+"]",e);
//				}
//			}
			
			
		return m.getId();
	}
	
	//add mailSendType 2
	@Override
	public long sendMail(long receiverId, ArticleEntity entities[], int[] counts, String title, String content, long coins, long bindyuanbao, long rmbyuanbao, String createReason,MailSendType type,String playername,String ipaddress,String recorder) throws Exception{
		Mail mail = new Mail();
		mail.setCoins(coins);
		mail.setContent(content);
		mail.setPoster(-1);
		mail.setReceiver(receiverId);
		mail.setTitle(title);
		if(createReason != null){
			mail.setCreateReason(createReason);
		}
		int colors[] = null;
		String anames [] = null;
		if(entities!=null&&counts!=null){
			colors = new int[entities.length];
			anames = new String[entities.length];
			for(int i = 0; i<entities.length;i++){
				Article article = ArticleManager.getInstance().getArticle(entities[i].getArticleName());
				if(article.isOverlap()){
					mail.getCells()[i].setEntityId(entities[i].getId());
					mail.getCells()[i].setCount(counts[i]);
				}else{
					mail.getCells()[i].setEntityId(entities[i].getId());
					mail.getCells()[i].setCount(1);
				}
				colors[i] = article.getColorType();
				anames[i] = article.getName_stat();
			}
		}
		Mail m = createMail(mail);
//		if(type==MailSendType.后台发送){
//			if(PlatformManager.getInstance().isPlatformOf(Platform.韩国) && type==MailSendType.后台发送){
//			try{
//				String [] titles =	{"类型","服务器","角色名","玩家id","IP地址","邮件标题","邮件内容","颜色集","数量集","绑银","操作人","操作时间"};
//				StringBuffer buffer = new StringBuffer();
//				buffer.append("<table style='font-size=12px;' border=1><tr bgcolor='greend'>");
//				for(String t:titles){
//					buffer.append("<th>");
//					buffer.append(t);
//					buffer.append("</th>");
//				}
//				buffer.append("</tr>");
//				
//				buffer.append("<tr><td>发送邮件</td><td>"+GameConstants.getInstance().getServerName()+"</td><td>"+playername+"</td><td>"+receiverId+"</td><td>"+ipaddress+"</td>"+"<td>"+title+"</td>"+"<td>"+content+"</td>"+"<td>"+Arrays.toString(colors)+"</td>"+"<td>"+Arrays.toString(counts)+"</td><td>"+coins+"</td><td>"+recorder+"</td>"+"<td>"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())+"</td></tr>");
//				buffer.append("</table>");
//				DataCheckManager.sendMail(title, buffer.toString());
//				if(ActivitySubSystem.logger.isInfoEnabled()){
//					ActivitySubSystem.logger.info("[GM后台发送邮件] [操作人:"+recorder+"] [角色名:"+playername+"] [角色id:"+receiverId+"] [ip:"+ipaddress+"] [物品:"+Arrays.toString(anames)+"] [数量:"+Arrays.toString(counts)+"] [颜色:"+Arrays.toString(colors)+"] [标题:"+title+"] [content:"+content+"]");
//				}
//			}catch(Exception e){
//				e.printStackTrace();
//				ActivitySubSystem.logger.warn("[GM后台发送邮件] [异常] [操作人:"+recorder+"] [角色名:"+playername+"] [角色id:"+receiverId+"] [ip:"+ipaddress+"] [物品:"+Arrays.toString(anames)+"] [数量:"+Arrays.toString(counts)+"] [颜色:"+Arrays.toString(colors)+"] [标题:"+title+"] [content:"+content+"]",e);
//			}
//		}
			
		return m.getId();
	}

	@Override
	public long sendMail(long posterId, long receiverId, ArticleEntity[] entities, String title, String content, long coins, long bindyuanbao, long rmbyuanbao, String createReason) throws Exception {
//		Mail mail = new Mail();
//		mail.setCoins(coins);
////		mail.setBindyuanbao(bindyuanbao);
////		mail.setRmbyuanbao(rmbyuanbao);
//		mail.setContent(content);
//		mail.setPoster(posterId);
//		mail.setReceiver(receiverId);
//		mail.setTitle(title);
//		if(createReason != null){
//			mail.setCreateReason(createReason);
//		}
//		if (entities != null && entities.length > 0) {
//			for (ArticleEntity entity : entities) {
//				if (entity != null) {
//					mail.put(entity);
//				}
//			}
//		}
//		Mail m = createMail(mail);
//		return m.getId();
		
		return sendMail(posterId, receiverId, entities, title, content, coins, bindyuanbao, rmbyuanbao, createReason, MailSendType.系统发送, "", "", "");
	}

//	@Override
//	public TransactionFuture transactionSendMail(long receiverId, ArticleEntity[] entities, String title, String content, long coins, long bindyuanbao, long rmbyuanbao) {
//		return transactionSendMail(-1, receiverId, entities, title, content, coins, bindyuanbao, rmbyuanbao);
//	}
//
//	@Override
//	public TransactionFuture transactionSendMail(long posterId, long receiverId, ArticleEntity[] entities, String title, String content, long coins, long bindyuanbao, long rmbyuanbao) {
//		Mail mail = new Mail();
//		mail.setCoins(coins);
////		mail.setBindyuanbao(bindyuanbao);
////		mail.setRmbyuanbao(rmbyuanbao);
//		mail.setContent(content);
//		mail.setPoster(posterId);
//		mail.setReceiver(receiverId);
//		mail.setTitle(title);
//		mail.setNewFlag(true);
//		if (entities != null && entities.length > 0) {
//			for (ArticleEntity entity : entities) {
//				mail.put(entity);
//			}
//		}
//		TransactionFuture f = transactionCreateMail(mail);
//		return f;
//	}

	public class ExpireMailChecker implements Runnable {
		public void run() {
			while (true) {
				try {
					Thread.sleep(60 * 1000);
					if(!com.fy.engineserver.util.ContextLoadFinishedListener.isLoadFinished()){
						continue;
					}
					long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
					List<Mail> list = getExpiredMails();
					PlayerManager pmanager = PlayerManager.getInstance();
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DAY_OF_YEAR, 30);
					int total = list.size();
					int deled = 0;
					int backed = 0;
					for (Mail mail : list) {
						if ((mail.getPrice() == 0 && !(mail.hasArticleEntities() || mail.getCoins() > 0)) || mail.getTitle().startsWith(Translate.text_2762)) {
							// 普通邮件或者是退件，直接删除
							deleteMail(mail.getId());
							if (logger.isInfoEnabled()) {
								logger.info("[处理过期邮件] [邮件id:{}] [普通邮件,直接删除] [创建日期:{}] [过期日期:{}]", new Object[]{mail.getId(),(DateUtil.formatDate(mail.getCreateDate(), "yyyy-MM-dd HH:mm:ss")),(DateUtil.formatDate(mail.getExpireDate(), "yyyy-MM-dd HH:mm:ss"))});
							}
							deled++;
						} else {
							// 退回发件人
							long posterId = mail.getPoster();
							long receiverId = mail.getReceiver();
							if (posterId < 0 || mail.getType() == Mail.TYPE_SYSTEM) {
								// 如果是系统邮件， 直接删除
								deleteMail(mail.getId());
								if (logger.isInfoEnabled()) {
									if(logger.isInfoEnabled())
										logger.info("[处理过期邮件] [邮件id:{}] [系统邮件,直接删除] [创建日期:{}] [过期日期:{}]", new Object[]{mail.getId(),(DateUtil.formatDate(mail.getCreateDate(), "yyyy-MM-dd HH:mm:ss") ),(DateUtil.formatDate(mail.getExpireDate(), "yyyy-MM-dd HH:mm:ss") )});
								}
								deled++;
							} else {
								Player rplayer = null;
								try {
									rplayer = pmanager.getPlayer(posterId);
								} catch (Exception e) {
									// 退回的玩家已删除，直接删除邮件
									deleteMail(mail.getId());
									if (logger.isInfoEnabled()) {
										if(logger.isInfoEnabled())
											logger.info("[处理过期邮件] [邮件id:{}] [退回的玩家不存在,直接删除] [创建日期:{}] [过期日期:{}]", new Object[]{mail.getId(),(DateUtil.formatDate(mail.getCreateDate(), "yyyy-MM-dd HH:mm:ss")),(DateUtil.formatDate(mail.getExpireDate(), "yyyy-MM-dd HH:mm:ss"))});
									}
									deled++;
									continue;
								}
								Player player = null;
								String pstr = Translate.text_1211;
								try {
									player = pmanager.getPlayer(receiverId);
									pstr = player.getName();
								} catch (Exception e) {
									e.printStackTrace();
								}
								mail.setReceiver(rplayer.getId());
								if (mail.getPrice() > 0) {// 付费邮件被退信
									mail.setPoster(-1);// 设成系统退信，否则收信后不小心删除邮件-》再次退信，对方就能免费取出附件。
								} else {
									mail.setPoster(receiverId);
								}
								mail.setPrice(0);
								mail.setTitle(Translate.text_2762 + mail.getTitle());
								if (mail.hasArticleEntities() || mail.getCoins() > 0)
									mail.setStatus(Mail.APPENDIX_UNREAD);
								else
									mail.setStatus(Mail.NORMAL_UNREAD);
								mail.setContent(Translate.text_2585 + pstr + Translate.text_2763 + mail.getContent());
								mail.setCreateDate(new java.util.Date());
								mail.setExpireDate(cal.getTime());
								if (logger.isInfoEnabled()) {
									Player receiver = null;
									Player poster = null;
									try {
										receiver = PlayerManager.getInstance().getPlayer(receiverId);
										if (posterId != -1) {
											poster = PlayerManager.getInstance().getPlayer(posterId);
										}
									} catch (Exception e) {
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
												}
											} else {
												attachment += cells[i].getEntityId() + Translate.text_2727 + cells[i].getCount() + ",";
											}
										}
										if (attachment.length() > 0) {
											attachment = attachment.substring(0, attachment.length() - 1);
										}
									}

									if(logger.isInfoEnabled())
										logger.info("[处理过期邮件] [邮件id:{}] [有附件:退回发件人] [目前收件人:{}] [目前发件人:{}] [标题:{}] [内容:{}] [附件：{}] [附加游戏币：{}] [付费价格：{}] [{}ms]", new Object[]{mail.getId(),(poster != null ? posterId + "/" + poster.getName() + "/" + poster.getUsername() : ""),(receiver != null ? receiverId + "/" + receiver.getName() + "/" + receiver.getUsername() : ""),mail.getTitle(),mail.getContent(),attachment,mail.getCoins(),mail.getPrice(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now)});
								}
								backed++;
							}
						}
					}
					if (logger.isInfoEnabled()) {
						if(logger.isInfoEnabled())
							logger.info("[检查处理过期邮件] [总数:{}] [删除:{}] [退回:{}] [耗时:{}ms]", new Object[]{total,deled,backed,(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now)});
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("[检查处理过期邮件时发生异常]", e);
					logger.error(StringUtil.getStackTrace(e));
				}
			}
		}
	}

	@Override
	public List<Mail> getPlayerDeletedMails(Player player, int start, int length) {
		return null;
	}

	@Override
	public int getCount(long playerId) {
		long count = 0;
		try {
			//count = em.count(Mail.class, "status<>"+Mail.DELETED + " and receiver="+playerId);
			count = em.count(Mail.class, "status<>? and receiver= ?", new Object[]{Mail.DELETED,playerId});
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[邮件查询异常]",e);
		}
		return (int)count;
	}
	

	@Override
	public int getCount(long playerId, int type) {
		long count = 0;
		try {
			Object paramValues[] = null;
			//String queryStr = "status<>"+Mail.DELETED + " and receiver="+playerId;
			String queryStr = "status<>? and receiver=?";
			paramValues = new Object[]{Mail.DELETED,playerId};
			if(type == 1){
				//queryStr = "(status="+Mail.APPENDIX_UNREAD+" or status="+Mail.NORMAL_UNREAD+") and receiver="+playerId;
				queryStr = "(status=? or status=?) and receiver=?";
				paramValues = new Object[]{Mail.APPENDIX_UNREAD,Mail.NORMAL_UNREAD,playerId};
				
			}else if(type == 2){
				//queryStr = "(status="+Mail.APPENDIX_READED+" or status="+Mail.NORMAL_READED+") and receiver="+playerId;
				queryStr = "(status=? or status=?) and receiver=?";
				paramValues = new Object[]{Mail.APPENDIX_READED,Mail.NORMAL_READED,playerId};
				
			}else if(type == 3){
				
				//queryStr = "status="+Mail.APPENDIX_UNREAD +" and receiver="+playerId;
				
				queryStr = "status=? and receiver=?";
				paramValues = new Object[]{Mail.APPENDIX_UNREAD,playerId};
			}else if(type == 4){
				//queryStr = "status="+Mail.APPENDIX_READED +" and receiver="+playerId;
				queryStr = "status=? and receiver=?";
				paramValues = new Object[]{Mail.APPENDIX_READED,playerId};
			}
			count = em.count(Mail.class, queryStr,paramValues);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[邮件查询异常]",e);
		}
		return (int)count;
	}
	
	public int getCount(int mailType, long playerId){
		long count = 0;
		try {
			//count = em.count(Mail.class, "status<>"+Mail.DELETED + " and receiver="+playerId);
			count = em.count(Mail.class, "status<>? and receiver= ? and type=?", new Object[]{Mail.DELETED,playerId, mailType});
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[邮件查询异常]",e);
		}
		return (int)count;
	}
	public int getCount(long playerId, int mailType, int type){
		long count = 0;
		try {
			Object paramValues[] = null;
			//String queryStr = "status<>"+Mail.DELETED + " and receiver="+playerId;
			String queryStr = "status<>? and receiver=? and type=?";
			paramValues = new Object[]{Mail.DELETED,playerId, mailType};
			if(type == 1){
				//queryStr = "(status="+Mail.APPENDIX_UNREAD+" or status="+Mail.NORMAL_UNREAD+") and receiver="+playerId;
				queryStr = "(status=? or status=?) and receiver=? and type=?";
				paramValues = new Object[]{Mail.APPENDIX_UNREAD,Mail.NORMAL_UNREAD,playerId, mailType};
				
			}else if(type == 2){
				//queryStr = "(status="+Mail.APPENDIX_READED+" or status="+Mail.NORMAL_READED+") and receiver="+playerId;
				queryStr = "(status=? or status=?) and receiver=? and type=?";
				paramValues = new Object[]{Mail.APPENDIX_READED,Mail.NORMAL_READED,playerId, mailType};
				
			}else if(type == 3){
				
				//queryStr = "status="+Mail.APPENDIX_UNREAD +" and receiver="+playerId;
				
				queryStr = "status=? and receiver=? and type=?";
				paramValues = new Object[]{Mail.APPENDIX_UNREAD,playerId, mailType};
			}else if(type == 4){
				//queryStr = "status="+Mail.APPENDIX_READED +" and receiver="+playerId;
				queryStr = "status=? and receiver=? and type=?";
				paramValues = new Object[]{Mail.APPENDIX_READED,playerId, mailType};
			}
			count = em.count(Mail.class, queryStr,paramValues);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[邮件查询异常]",e);
		}
		return (int)count;
	}
}
