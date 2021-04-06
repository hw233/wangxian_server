package com.fy.engineserver.gm.custom;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.Mail;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;

public class GmMailReplay {
//	protected static Logger logger = Logger.getLogger(GmMailReplay.class.getName());
public	static Logger logger = LoggerFactory.getLogger(GmMailReplay.class.getName());
	private MailManager mmanager ;
	private PlayerManager pmanager;
	private GmItemManager gimanager ;
	private ArticleEntityManager aemanager ;
	private ArticleManager amanager ;
	private  List<Mail> questionmails = new ArrayList<Mail>();
	private  List<Mail> gmmails = new ArrayList<Mail>();
    private static GmMailReplay self;
	private GmMailReplay() {
	}  
    public void initialize(){
    	self = this;
    	if(logger.isInfoEnabled())
	    	logger.info("[GmMailReplay][init][success]");
    }
	public static GmMailReplay getInstance() {
		return self;
	}

	public long getGmMailSize(String gmname) {
		try {
			Player player = pmanager.getPlayer(gmname);
			return mmanager.getCount(player.getId());
		} catch (Exception e) {
			e.printStackTrace();
			return 0l;
		}

	}

	public List<Mail> getGmMail(String gmname, int start, int length) {
		try {
			List<Mail> mails = new ArrayList<Mail>();
			Player player = pmanager.getPlayer(gmname);
			mails = mmanager.getAllMails(player, start, length);
			return mails;
		} catch (Exception e) {
			return null;
		}
	}

	public List<Mail> getBetweenMail(String gmname, Date fromdate, Date todate) {
		try {
			List<Mail> mails = new ArrayList<Mail>();
			Player player = pmanager.getPlayer(gmname);
			mails = mmanager.getBetweenMails(player, fromdate, todate);
			return mails;
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * public List<Mail> getGmMail(String gmname,String uname){ try { List<Mail>
	 * mails1 = new ArrayList<Mail>(); List<Mail> mails = new ArrayList<Mail>();
	 * Player player = pmanager.getPlayer(gmname); mails1 =
	 * mmanager.getAllMails(player); for(Mail m:mails1){
	 * if(uname.equals(pmanager.getPlayer(m.getPoster()).getName()))
	 * mails.add(m); } return mails; } catch (Exception e) { return null; }
	 * 
	 * 
	 * }
	 */
	public void deleteMail(String[] delids) {
		for (String delid : delids) {
			try{
			  int id = Integer.parseInt(delid.trim());
			  Mail mail = mmanager.getMail(id); 
			  Player p = pmanager.getPlayer(mail.getReceiver());
			  if(mail.hasArticleEntities() || mail.getCoins() > 0) {
					long poster = mail.getPoster();
					if(poster < 0 || mail.getType() == Mail.TYPE_SYSTEM) {
						//如果是系统邮件， 直接删除
						mmanager.deleteMail(id);
//						logger.warn("[删除邮件] [系统邮件，GM删除] [邮件id:"+mail.getId()+"]");
						if(logger.isWarnEnabled())
							logger.warn("[删除邮件] [系统邮件，GM删除] [邮件id:{}]", new Object[]{mail.getId()});

					} else {
						mail.setReceiver(poster);
						if( mail.getPrice()>0){//付费邮件被退信
							mail.setPoster(-1);//设成系统退信，否则收信后不小心删除邮件-》再次退信，对方就能免费取出附件。
						}else{
							mail.setPoster(p.getId());
						}
						mail.setPrice(0);
						mail.setTitle(Translate.text_2762 + mail.getTitle());
						mail.setStatus(Mail.APPENDIX_UNREAD);
						mail.setContent(Translate.text_2585+p.getName()+Translate.text_2763 + mail.getContent());
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.DAY_OF_YEAR, 30);
						mail.setExpireDate(cal.getTime());
						//mmanager.updateMail(mail);
//						logger.warn("[删除邮件] [包含附件，回退给发件人] [邮件id:"+mail.getId()+"] [操作人:gm");
						if(logger.isWarnEnabled())
							logger.warn("[删除邮件] [包含附件，回退给发件人] [邮件id:{}] [操作人:gm", new Object[]{mail.getId()});
					}
				} else {
					mmanager.deleteMail(id);
//					logger.warn("[删除邮件] [普通邮件，直接删除] [邮件id:"+mail.getId()+"] [操作人:gm");
					if(logger.isWarnEnabled())
						logger.warn("[删除邮件] [普通邮件，直接删除] [邮件id:{}] [操作人:gm", new Object[]{mail.getId()});

				}
			}catch(Exception e){
//			  logger.warn("[mail]["+delid+"][delete][fail]");
			  if(logger.isWarnEnabled())
				  logger.warn("[mail][{}][delete][fail]", new Object[]{delid});
			  continue;
			}
		}
	}

//	public List<Mail> getAllGmMail(String gmname) {
//		try {
//			List<Mail> mails = new ArrayList<Mail>();
//			Player player = pmanager.getPlayer(gmname);
//			mails = mmanager.getAllMails(player);
//			logger
//					.info("gname" + gmname
//							+ " 's mail has been read success at ");
//			return mails;
//		} catch (Exception e) {
//			logger.info("gname" + gmname + " 's mail has been reed fail at ");
//			return null;
//		}
//	}

	public boolean sendMail(long pid, int mid, long receiverId, String items[],boolean isban,
			String title, String content) {
		
		return false;
	}
	
	
	public boolean sendMail(long pid, int mid, long receiverId, String item,int count,
			String title, String content) {
		
		return false;
	}
	
	public MailManager getMmanager() {
		return mmanager;
	}

	public void setMmanager(MailManager mmanager) {
		this.mmanager = mmanager;
	}

	public PlayerManager getPmanager() {
		return pmanager;
	}

	public void setPmanager(PlayerManager pmanager) {
		this.pmanager = pmanager;
	}

	public List<Mail> getQuestionmails() {
		return questionmails;
	}

	public List<Mail> getGmmails() {
		return gmmails;
	}

	public GmItemManager getGimanager() {
		return gimanager;
	}
	public void setGimanager(GmItemManager gimanager) {
		this.gimanager = gimanager;
	}
	public ArticleEntityManager getAemanager() {
		return aemanager;
	}
	public void setAemanager(ArticleEntityManager aemanager) {
		this.aemanager = aemanager;
	}
	public ArticleManager getAmanager() {
		return amanager;
	}
	public void setAmanager(ArticleManager amanager) {
		this.amanager = amanager;
	}
    
	
	
}
