
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%><%@ page contentType="text/html;charset=utf-8"%>
<%@page import="com.fy.engineserver.sprite.*,
		com.fy.engineserver.mail.*,
		com.fy.engineserver.mail.service.*,
		java.util.*"%>
<%!

		public boolean running = true;

public class ExpireMailChecker implements Runnable {
		public void run() {
			PlayerManager pmanager = PlayerManager.getInstance();
			MailManager mmanager = MailManager.getInstance();
			while(running) {
				try {
					Thread.sleep(60*1000);
					long now = System.currentTimeMillis();
					List<Mail> list = mmanager.getExpiredMails();
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DAY_OF_YEAR, 30);
					int total = list.size();
					int deled = 0;
					int backed = 0;
					for(Mail mail : list) {
						if( (mail.getPrice() == 0 && !(mail.hasArticleEntities()||mail.getCoins() > 0)) || mail.getTitle().startsWith("[退]")) {
							//
						} else {
							long posterId = mail.getPoster();
							long receiverId = mail.getReceiver();
							if(posterId < 0 || mail.getType() == Mail.TYPE_SYSTEM) {
								//
							} else {
								Player rplayer = null;
								try {
									rplayer = pmanager.getPlayer(posterId);
								} catch(Exception e) {
									continue;
								}
								Player player = null;
								String pstr = "未知";
								try {
									player = pmanager.getPlayer(receiverId);
									pstr = player.getName();
								} catch(Exception e) {
									e.printStackTrace();
								}
								mail.setReceiver(rplayer.getId());
								if( mail.getPrice()>0){//付费邮件被退信
									mail.setPoster(-1);//设成系统退信，否则收信后不小心删除邮件-》再次退信，对方就能免费取出附件。
								}else{
									mail.setPoster(receiverId);
								}
								mail.setPrice(0);
								mail.setTitle("[退]" + mail.getTitle());
								if(mail.hasArticleEntities() || mail.getCoins() > 0)
									mail.setStatus(Mail.APPENDIX_UNREAD);
								else
									mail.setStatus(Mail.NORMAL_UNREAD);
								mail.setContent("玩家["+pstr+"]退回了您的邮件。原邮件内容:\n" + mail.getContent());
								mail.setCreateDate(new java.util.Date());
								mail.setExpireDate(cal.getTime());
								mmanager.updateMail(mail);
								if(MailManager.logger.isInfoEnabled()) {
									Player receiver = null;
									Player poster = null;
									try {
										receiver = PlayerManager.getInstance().getPlayer(receiverId);
										if(posterId != -1) {
											poster = PlayerManager.getInstance().getPlayer(posterId);
										}
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									String attachment="";
									Cell[] cells=mail.getCells();
									ArticleEntityManager aem=ArticleEntityManager.getInstance();
									if(cells!=null){
										for(int i=0;i<cells.length;i++){
											if(cells[i]!=null && cells[i].getEntityId() > 0 && cells[i].getCount() > 0){
												ArticleEntity ae=aem.getEntity(cells[i].getEntityId());
												if(ae!=null){
													attachment+=ae.getId()+"/" + ae.getArticleName() + "/"+cells[i].getCount() + ",";
												}
											} else {
												attachment+=cells[i].getEntityId()+"/空/"+cells[i].getCount() + ",";
											}
										}
										if(attachment.length() > 0) {
											attachment = attachment.substring(0, attachment.length()-1);
										}
									}
									
									MailManager.logger.info("[ExpireMailChecker-2] [处理过期邮件] [邮件id:"+mail.getId()+"] [有附件:退回发件人] [目前收件人:"+(poster!=null?posterId+"/"+poster.getName()+"/"+poster.getUsername():"")+"] [目前发件人:"+(receiver!=null?receiverId+"/"+receiver.getName()+"/"+receiver.getUsername():"")+"] [标题:"+mail.getTitle()+"] [内容:"+mail.getContent()+"] [附件："+attachment+"] [附加游戏币："+mail.getCoins()+"] [付费价格："+mail.getPrice()+"] ["+(System.currentTimeMillis()-now)+"ms]");
								}
								backed++;
							}
						}
					}
					if(MailManager.logger.isInfoEnabled()) {
						MailManager.logger.info("[ExpireMailChecker-2] [检查处理过期邮件] [总数:"+total+"] [删除:"+deled+"] [退回:"+backed+"] [耗时:"+(System.currentTimeMillis()-now)+"ms]");
					}
				} catch(Exception e) {
					e.printStackTrace();
					MailManager.logger.error("[ExpireMailChecker-2] [检查处理过期邮件时发生异常]", e);
					MailManager.logger.error(com.xuanzhi.tools.text.StringUtil.getStackTrace(e));
				}
			}
		}
	}
%>

<%
Thread t = new Thread(new ExpireMailChecker(),"ExpireMailChecker-2");
t.start();
%>
