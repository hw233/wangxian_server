package com.fy.engineserver.mail.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.exception.WrongFormatMailException;
import com.fy.engineserver.mail.Mail;
import com.fy.engineserver.mail.MailSendType;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;

public abstract class MailManager {
	
//	public static Logger logger = Logger.getLogger(MailManager.class);
public	static Logger logger = LoggerFactory.getLogger(MailManager.class);
	
	protected static MailManager self;
	public SimpleEntityManager<Mail> em;
	public static MailManager getInstance(){
		return self;
	}
	
	/**
	 * 创建一封邮件，此邮件必须包含发件人，收件人，标题，其他为可填写
	 * @param mail
	 * @return
	 */
	public abstract Mail createMail(Mail mail) throws WrongFormatMailException;
	
	public abstract Mail importMail(Mail mail);
//	
//	/**
//	 * 以事物的方式创建一封邮件，此邮件必须包含发件人，收件人，标题，其他为可填写
//	 * @param mail
//	 * @return
//	 */
//	public abstract TransactionFuture transactionCreateMail(Mail mail);
//	
	/**
	 * 通过id获取一个邮件
	 * @param id
	 * @return
	 */
	public abstract Mail getMail(long id);
	
	public abstract Mail getMailInCache(long id);
	
	/**
	 * 获得过期的邮件
	 * @return
	 */
	public abstract List<Mail> getExpiredMails();
	
	/**
	 * 获取某个玩家的所有未读邮件
	 * @param player
	 * @return
	 */
	public abstract List<Mail> getUnReadMails(Player player);
	
	/**
	 * 获取某个玩家的某个时间段邮件
	 * @param player
	 * @return
	 */
	public abstract List<Mail> getBetweenMails(Player player,Date fromdate,Date todate);
	/**
	 * 获取某个玩家的未读邮件
	 * @param player
	 * @return
	 */
	public abstract List<Mail> getUnReadMails(Player player, int start, int length);
	/**
	 * 获取某个玩家的未读邮件
	 * @param player
	 * @return
	 */
	public abstract List<Mail> getUnReadMails(Player player, int mailType, int start, int length);
	
	/**
	 * 获取某个玩家的已读邮件
	 * @param player
	 * @return
	 */
	public abstract List<Mail> getReadMails(Player player, int start, int length);
	/**
	 * 获取某个玩家的已读邮件
	 * @param player
	 * @return
	 */
	public abstract List<Mail> getReadMails(Player player, int mailType, int start, int length);
	/**
	 * 获取某个玩家的有附件未读邮件
	 * @param player
	 * @return
	 */
	public abstract List<Mail> getUnReadMailHasAppendixs(Player player, int start, int length);
	/**
	 * 获取某个玩家的有附件未读邮件
	 * @param player
	 * @return
	 */
	public abstract List<Mail> getUnReadMailHasAppendixs(Player player, int mailType, int start, int length);
	
	/**
	 * 获取某个玩家的有附件已读邮件
	 * @param player
	 * @return
	 */
	public abstract List<Mail> getReadMailHasAppendixs(Player player, int start, int length);
	/**
	 * 获取某个玩家的有附件已读邮件
	 * @param player
	 * @return
	 */
	public abstract List<Mail> getReadMailHasAppendixs(Player player, int mailType, int start, int length);
	
	/**
	 * 玩家是否有新邮件
	 * @param player
	 * @return
	 */
	public abstract int hasNewMail(Player player);
	
	/**
	 * 获取某个玩家的所有邮件
	 * @param player
	 * @return
	 */
	public abstract List<Mail> getAllMails(Player player);
	
	/**
	 * 获取某个玩家的所有邮件
	 * @param player
	 * @return
	 */
	public abstract List<Mail> getAllMails(Player player, int start, int length);
	
	/**
	 * 获取某个玩家的所有邮件
	 * @param player
	 * @return
	 */
	public abstract List<Mail> getAllMails(Player player, int mailType, int start, int length);
	
	/**
	 * 获取某个玩家的已删除邮件
	 * @param player
	 * @return
	 */
	public abstract List<Mail> getPlayerDeletedMails(Player player, int start, int length);

	/**
	 * 删除一份邮件
	 * @param id
	 */
	public abstract void deleteMail(long id);

	/**
	 * 删除所有已读且没有附件的邮件
	 * @param id
	 */
	public abstract void deleteAllReadedNotHasAppendixsMail(Player player);

	/**
	 * 发送一封邮件
	 * @param receiverId
	 * @param title
	 * @param content
	 * @param coins
	 */
	public abstract long sendMail(long receiverId, ArticleEntity entities[], String title, String content, long coins, long bindyuanbao, long rmbyuanbao, String createReason) throws Exception;
	
	/**
	 * 发送一封邮件,系统给玩家发用此方法
	 * @param receiverId
	 * @param title
	 * @param content
	 * @param coins
	 */
	public abstract long sendMail(long receiverId, ArticleEntity entities[], int[] counts, String title, String content, long coins, long bindyuanbao, long rmbyuanbao, String createReason) throws Exception;
	
	public abstract long sendMail(long posterId, long receiverId, ArticleEntity[] entities, String title, String content, long coins, long bindyuanbao, long rmbyuanbao, String createReason,MailSendType type,String playername,String ipaddress,String recorder) throws Exception;
	public abstract long sendMail(long receiverId, ArticleEntity entities[], int[] counts, String title, String content, long coins, long bindyuanbao, long rmbyuanbao, String createReason,MailSendType type,String playername,String ipaddress,String recorder) throws Exception;
	
	/**
	 * 玩家发送一封邮件
	 * @param receiverId
	 * @param title
	 * @param content
	 * @param coins
	 */
	public abstract long sendMail(long posterId, long receiverId, ArticleEntity entities[], String title, String content, long coins, long bindyuanbao, long rmbyuanbao, String createReason) throws Exception;
//	
//	/**
//	 * 以事物的方式发送一封邮件
//	 * @param receiverId
//	 * @param title
//	 * @param content
//	 * @param coins
//	 */
//	public abstract TransactionFuture transactionSendMail(long receiverId, ArticleEntity entities[], String title, String content, long coins, long bindyuanbao, long rmbyuanbao);
//	
//	/**
//	 * 玩家以事物的方式发送一封邮件
//	 * @param receiverId
//	 * @param title
//	 * @param content
//	 * @param coins
//	 */
//	public abstract TransactionFuture transactionSendMail(long posterId, long receiverId, ArticleEntity entities[], String title, String content, long coins, long bindyuanbao, long rmbyuanbao);

	public abstract int getCount(long playerId);
	public abstract int getCount(long playerId, int type);
	
	public abstract int getCount(int mailType, long playerId);
	public abstract int getCount(long playerId, int mailType, int type);
}
