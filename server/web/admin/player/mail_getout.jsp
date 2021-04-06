<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.mail.service.*,
				com.fy.engineserver.mail.*,
				com.fy.engineserver.core.*,
				com.fy.engineserver.economic.*,
				com.fy.engineserver.sprite.*,
				java.util.*"%>
<%
MailManager mmanager = MailManager.getInstance();
PlayerManager pmanager = PlayerManager.getInstance();
String mailid = request.getParameter("mid");
String message = null;
if(mailid != null) {
	long mid = Long.parseLong(mailid);
	Mail mail = mmanager.getMail(mid);
	if(mail != null) {
		long coins = mail.getCoins();
		long price = mail.getPrice();
		if(price > 0) {
			message = "不能手工提取含有价格的邮件";
		} else {
			try {
				Player player = pmanager.getPlayer(mail.getReceiver());
				Cell cells[] = mail.getCells();
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
				long now = System.currentTimeMillis();
				if (elist.size() > 0 && putOK) {
					player.putAll(elist.toArray(new ArticleEntity[0]));
					mail.clearAllCell();
				} else if(elist.size() > 0) {
					throw new Exception("用户背包已满，无法提取");
				} 
				BillingCenter billingCenter = BillingCenter.getInstance();
				if(mail.getCoins() > 0) {
					billingCenter.playerSaving(player, mail.getCoins(), CurrencyType.GAME_MONEY, SavingReasonType.MAIL_GET, new Object[]{mail});
				}
				mail.setCoins(0);
				mail.setPrice(0);
				mail.setBindyuanbao(0);
				mail.setRmbyuanbao(0);
				mail.setStatus(Mail.NORMAL_READED);
				mail.setLastModifyDate(new java.util.Date());
				mmanager.updateMail(mail);
				if( CoreSubSystem.logger.isDebugEnabled())
					CoreSubSystem.logger.debug("[取出邮件的附件(后台人工提取)] [接受人:"+player.getUsername()+"] [接受人:"+player.getName()+"] [发送人:"+mail.getPoster()+"] ["+mail.getId()+"] [物品:"+elist.size()+"] [是否能放置:"+putOK+"] [金币:"+coins+"] [价格(付费):"+price+"] ["+(System.currentTimeMillis()-now)+"]");
				message = "提取附件成功!";
			} catch(Exception e) {
				e.printStackTrace();
				message = e.getMessage();
				CoreSubSystem.logger.error("取出邮件附件异常(后台人工提取) ["+mail.getId()+"]", e);
			}
		}
	}
}
%>
<%if(message != null) out.println("<font color=red>"+message+"</font>");%>
<%@include file="../IPManager.jsp" %>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%><form action="" name=f1>
	邮件id:<input type=text size=10 name="mid">
	<input type=submit name=sub1 value="接收">
</form>
