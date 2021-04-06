<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiPlayerInfoManager"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiPlayerInfo"%>

<%@page import="java.net.URLEncoder"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%>
<%@page import="com.fy.engineserver.activity.costBillbord.CostBillboardManager"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.activity.costBillbord.CostData"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.activity.costBillbord.CostArtice"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.activity.costBillbord.CostConfig"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
Class cl = CostBillboardManager.class;
Field f = cl.getDeclaredField("configs");
f.setAccessible(true);
List<CostConfig> configs = (List<CostConfig>)f.get(CostBillboardManager.getInstance());

CostConfig c = configs.get(0);


CostData[] datas = CostBillboardManager.getInstance().getSortData();
String serverName = null;
int [][] ranks = {new int[]{1,1},new int[]{2,5},new int[]{6,10},new int[]{11,50}};
for(int i=0;i<datas.length;i++){
	CostData data = datas[i];
	if(data == null){
		out.print("[消费排行榜] [奖励发放] [失败:data == null] [i:"+i+"] [datas:"+datas.length+"] ["+c+"]]");
		continue;
	}
	try{
		if(serverName == null){
			serverName = GameConstants.getInstance().getServerName();
		}
		if(data.getServerName().equals(serverName)){
			Player p = PlayerManager.getInstance().getPlayer(data.getId());
			List<ArticleEntity> aes = new ArrayList<ArticleEntity>();
			for(int j=0;j<ranks.length;j++){
				int rs[]=ranks[j];
				int rank = i + 1;
				if(rank >= rs[0] && rank <= rs[1]){
					List<CostArtice> list = c.rewards.get(j+1);
					int counts [] = new int[list.size()];
					int index = 0;
					StringBuffer names = new StringBuffer();
					for(CostArtice ca : list){
						if(ca == null){
							continue;
						}
						Article a = ArticleManager.getInstance().getArticle(ca.rewardArtice);
						if(a == null){
							out.print("[消费排行榜] [奖励发放] [失败:道具不存在] [name:"+ca.rewardArtice+"] [datas:"+datas.length+"] ["+c+"]");
							return;
						}
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a,true,  ArticleEntityManager.消费排行榜, p, ca.rewardColor, ca.rewardCount, true);
						if(ae == null){
							out.print("[消费排行榜] [奖励发放] [失败:道具ae不存在] [name:"+ca.rewardArtice+"] [datas:"+datas.length+"] ["+c+"]");
							return;
						}
						aes.add(ae);
						names.append(ae.getArticleName()).append(ca.rewardCount).append("=====");
						counts[index] = ca.rewardCount;
						index++;
					}
					if(aes.size() <= 0){
						out.print("[消费排行榜] [奖励发放] [失败:道具配置错误] [datas:datas.length] ["+c+"]");
						return;
					}
					String costTypeStr = "消费排行榜奖励";
					try{
						costTypeStr = CostBillboardManager.getInstance().activityHeadMess[c.costType-1];
					}catch(Exception e){
						e.printStackTrace();
					}
					
					MailManager.getInstance().sendMail(p.getId(), aes.toArray(new ArticleEntity[]{}), counts, costTypeStr, costTypeStr, 0, 0, 0, "消费排行榜奖励");
					out.print("[消费排行榜] [奖励发放] [成功] [names:"+names.toString()+"]  [aes:"+aes.size()+"] [counts:"+counts.length+"] [costTypeStr:"+costTypeStr+"] [rank:"+rank+"] [datas:"+datas.length+"] ["+p.getLogString()+"]<br>");
				}
			}
		}
	}catch(Exception e){
		e.printStackTrace();
		out.print("[消费排行榜] [奖励发放] [异常] ["+e+"]");
	}
}

%>

</body>
</html>