<%@page import="com.fy.engineserver.mail.MailSendType"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.economic.SavingReasonType"%>
<%@page import="com.fy.engineserver.economic.CurrencyType"%>
<%@page import="com.fy.engineserver.economic.BillingCenter"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.jiazu.JiazuTitle"%>
<%@page import="com.fy.engineserver.jiazu.JiazuMember"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String ip = request.getRemoteAddr();
String recorder = "";
Object o = session.getAttribute("authorize.username");
if(o!=null){
	recorder = o.toString();
}
	String send = request.getParameter("send");
	boolean realSend = "send".equals(send);

	GameConstants constants = GameConstants.getInstance();
	String thisServerName = constants.getServerName();
	JiazuPrize thisJp = null;

	String 洪荒至尊录 = "洪荒至尊录";
	String 洪荒暗魁录 = "洪荒暗魁录";
	for (JiazuPrize jp : JiazuPrize.values()) {
		if (jp.serverName.equals(thisServerName)) {
			thisJp = jp;
			break;
		}
	}

	if (thisJp == null) {
		out.print("<H1>没有本服务器的相关配置[" + thisServerName + "]</H1>");
		return;
	}

	Long[] jiazuIds = thisJp.jiazuIds;
	List<Jiazu> jiazuList = new ArrayList<Jiazu>();
	Article article_洪荒暗魁录 = null;
	Article article_洪荒至尊录 = null;
	{
		//先检查所有的家族,物品,是否存在
		for (Long jiazuId : jiazuIds) {
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(jiazuId);
			if (jiazu == null) {
				out.print("<H1>没有找到家族,ID:[" + jiazuId + "]</H1>");
				return;
			}
			jiazuList.add(jiazu);
		}
		article_洪荒暗魁录 = ArticleManager.getInstance().getArticle(洪荒暗魁录);
		if (article_洪荒暗魁录 == null) {
			out.print("<H1>物品不存在:[" + 洪荒暗魁录 + "]</H1>");
			return;
		}
		article_洪荒至尊录 = ArticleManager.getInstance().getArticle(洪荒至尊录);
		if (article_洪荒至尊录 == null) {
			out.print("<H1>物品不存在:[" + 洪荒至尊录 + "]</H1>");
			return;
		}
	}
	{
		//判断通过,开始发奖励
		for (Jiazu jiazu : jiazuList) {
			Set<JiazuMember> jiazuMemeSet = JiazuManager.getInstance().getJiazuMember(jiazu.getJiazuID());
			if (jiazuMemeSet != null && jiazuMemeSet.size() > 0) {
				for (JiazuMember jm : jiazuMemeSet) {
					if (jm != null) {
						long playerId = jm.getPlayerID();
						JiazuTitle jiazuTitle = jm.getTitle();
						Player player = null;
						try {
							player = GamePlayerManager.getInstance().getPlayer(playerId);
						} catch (Exception e) {
						}
						if (player == null) {
							String notice1 = "[家族的成员(角色)不存在] [jiazu:" + jiazu.getJiazuID() + "] [jiazuName:" + jiazu.getName() + "] [成员ID:" + jm.getJiazuMemID() + "] [角色ID:" + jm.getPlayerID() + "]";
							out.print("<H1>" + notice1 + "</H1>");
							System.out.println(notice1);
						}
						Article article = article_洪荒暗魁录;
						long money = 2500000;
						if (jiazuTitle.equals(JiazuTitle.master) || jiazuTitle.equals(JiazuTitle.vice_master)) {
							article = article_洪荒至尊录;
							money = 5000000;
						}
						long mailId = -1;
						if (realSend) {
							ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.运营发放活动奖励, player, 4, 1, true);
							mailId = MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { 1 }, "家族实力排行奖励", "尊敬的玩家，恭喜您的家族在家族实力排行榜取得名次，飞行坐骑请查看附件，银锭已经打入您的工资商店账户，请注意查收，感谢您对飘渺寻仙曲的支持，祝您游戏愉快！！", 0L, 0L, 0L, "家族实力排行奖励",MailSendType.后台发送,player.getName(),ip,recorder);
							BillingCenter.getInstance().playerSaving(player, money, CurrencyType.GONGZI, SavingReasonType.JIAZU_GONGZI, "家族实力排行奖励");
						}
						String notice2 = "[发送奖励成功] [" + (realSend ? "真正发送" : "测试发送") + "] [职务:" + jiazuTitle.getName() + "] [家族:" + jiazu.getJiazuID() + "/" + jiazu.getName() + "] [角色:" + player.getLogString() + "] [得到工资:" + money + "] [增加后工资:" + player.getWage() + "] [得到坐骑:" + article.getName() + "] [邮件ID:" + mailId + "]";
						out.print("<font style='font-size=12px'>" + notice2 + "</font><BR/>");
						System.out.println(notice2);
					}
				}
			} else {
				String notice = "家族成员不存在,[jiazuId:" + jiazu.getJiazuID() + "] [jiazuName:" + jiazu.getName() + "]";
				out.print("<H1>" + notice + "</H1>");
				System.out.println(notice);
			}
		}
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>2012-国庆家族活动</title>
</head>
<body>

</body>
</html>
<%!enum JiazuPrize {
		pan2("pan2", 1101000000000000001L),
		巍巍昆仑("巍巍昆仑", 1100000000000389121L), 
		//傲视三界("傲视三界",1148000000000000007L,1148000000000000030L,1148000000000061479L,1148000000000000027L,1148000000000000012L,1148000000000000049L,1148000000000000008L,1148000000000000010L,1148000000000000001L),
		//惊天战神("惊天战神", 1150000000000000051L,1150000000000000001L,1150000000000000139L,1150000000000000163L,1150000000000000009L,1150000000000000012L,1150000000000000047L,1150000000000000033L,1150000000000000032L), 
		//王者之域("王者之域", 1149000000000000045L,1149000000000000004L,1149000000000000015L,1149000000000000180L,1149000000000000054L,1149000000000000037L,1149000000000000003L,1149000000000000001L,1149000000000000009L), 
		;
		JiazuPrize(String serverName, Long... jiazuIds) {
			this.serverName = serverName;
			this.jiazuIds = jiazuIds;
		}

		final String serverName;
		final Long[] jiazuIds;
	}%>