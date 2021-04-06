<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.xuanzhi.tools.queue.*"%>
     <%!
     public static int color = 0;
     public static int bind = 0;
     ArticleManager.BindType bindType = null;
     String articleName = null;
     HashMap<String,Integer> articleCountMap = new HashMap<String,Integer>(); 
     %>
<%
        PlayerManager sm = PlayerManager.getInstance();

CoreSubSystem core = CoreSubSystem.getInstance();

Date date = new Date(org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getStartupDate());
String pid = request.getParameter("pid");
if(pid != null){
try{
out.println(pid);
Player p = sm.getPlayer(Integer.parseInt(pid));
p.getConn().close();
}catch(Exception ex){
ex.printStackTrace();
}
}
Player sprites[] = sm.getOnlinePlayers();
Arrays.sort(sprites,new Comparator<Player>(){
	public int compare(Player p1,Player p2){
		if(p1.getLevel() > p2.getLevel()) return -1;
		if(p1.getLevel() < p2.getLevel()) return 1;
		if(p1.getBindSilver() > p2.getBindSilver()) return -1;
		if(p1.getBindSilver() < p2.getBindSilver()) return 1;
		return 0;
	}
});
String selectIndex = request.getParameter("selectIndex");

if(selectIndex != null){
	if(selectIndex.equals("1")){//按照银子排序
		Arrays.sort(sprites,new Comparator<Player>(){
			public int compare(Player p1,Player p2){
				if(p1.getSilver() > p2.getSilver()) return -1;
				if(p1.getSilver() < p2.getSilver()) return 1;
				return 0;
			}
		});
	}else if(selectIndex.equals("2")){//按照绑银排序
		Arrays.sort(sprites,new Comparator<Player>(){
			public int compare(Player p1,Player p2){
				if(p1.getBindSilver() > p2.getBindSilver()) return -1;
				if(p1.getBindSilver() < p2.getBindSilver()) return 1;
				return 0;
			}
		});
	}else if(selectIndex.equals("5")){//按照血上限排序
		Arrays.sort(sprites,new Comparator<Player>(){
			public int compare(Player p1,Player p2){
				if(p1.getMaxHP() > p2.getMaxHP()) return -1;
				if(p1.getMaxHP() < p2.getMaxHP()) return 1;
				return 0;
			}
		});
	}else if(selectIndex.equals("6")){//按照攻强排序
		Arrays.sort(sprites,new Comparator<Player>(){
			public int compare(Player p1,Player p2){
				int attack1 = p1.getPhyAttack();
				if(attack1 < p1.getMagicAttack()){
					attack1 = p1.getMagicAttack();
				}
				int attack2 = p2.getPhyAttack();
				if(attack2 < p2.getMagicAttack()){
					attack2 = p2.getMagicAttack();
				}
				if(attack1 > attack2) return -1;
				if(attack1 < attack2) return 1;
				return 0;
			}
		});
	}else if(selectIndex.equals("7")){//按照攻强排序
		Arrays.sort(sprites,new Comparator<Player>(){
			public int compare(Player p1,Player p2){
				long attack1 = p1.getWage();
				if(attack1 < p1.getWage()){
					attack1 = p1.getWage();
				}
				long attack2 = p2.getWage();
				if(attack2 < p2.getWage()){
					attack2 = p2.getWage();
				}
				if(attack1 > attack2) return -1;
				if(attack1 < attack2) return 1;
				return 0;
			}
		});
	}else if(selectIndex.equals("3")){//按照身上的东西排序，东西名称为下面指定的名称
		articleName = request.getParameter("articleName");
	String colorStr = request.getParameter("color");
	String bindStr = request.getParameter("bind");
	color = 0;
	bind = 0;
	try{
		color = Integer.parseInt(colorStr);
	}catch(Exception ex){
		
	}
	try{
		bind = Integer.parseInt(bindStr);
	}catch(Exception ex){
		
	}
	bindType = null;
	if(bind == 2){
		bindType = ArticleManager.BindType.NOT_BIND;
	}else if(bind == 1){
		bindType = ArticleManager.BindType.BIND;
	}else{
		bindType = ArticleManager.BindType.BOTH;
	}
	if(articleName != null){
		ArticleManager am = ArticleManager.getInstance();
		Article a = am.getArticle(articleName.trim());
		if(a != null){
			if(sprites != null){
				for(int i = 0; i < sprites.length; i++){
					Player p1 = sprites[i];
					if(p1 != null){
						int count1 = p1.getArticleNum(articleName,color,bindType);
						if(p1.getKnapsacks_cangku() != null){
							if(bind == 2){
								count1 += p1.getKnapsacks_cangku().countArticle(articleName,color,false);
							}else if(bind == 1){
								count1 += p1.getKnapsacks_cangku().countArticle(articleName,color,true);
							}else{
								count1 += p1.getKnapsacks_cangku().countArticle(articleName,color);
							}
						}
						articleCountMap.put(p1.getName(),count1);
					}
				}

			}
			Arrays.sort(sprites,new Comparator<Player>(){
				public int compare(Player p1,Player p2){
					int count1 = articleCountMap.get(p1.getName()) != null?articleCountMap.get(p1.getName()):0;
					int count2 = articleCountMap.get(p2.getName()) != null?articleCountMap.get(p2.getName()):0;
					if(count1 > count2) return -1;
					if(count1 < count2) return 1;
					return 0;
				}
			});
		}else{
			out.println("你输入的要查找的"+articleName+"不存在");
		}
	}else{
		out.println("请输入的要查找的东西");
	}

	}
}


%>
<%@page import="com.fy.engineserver.stat.StatData"%>
<%@include file="IPManager.jsp" %>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager.BindType"%><html><head>
<script type="text/javascript">
function kickplayer(pid){
document.getElementById("pid").value = pid;
document.f1.submit();
}
</script>
</HEAD>
<BODY>
<h2>Game Server，在线人数：<%= sprites.length %>，正在等待的用户：<%= core.waitingEnterGameConnections.size() %>，各个在线玩家的情况</h2><br><a href="./playersonline.jsp">刷新此页面</a><br><br>
<form id="f1" name="f1" method="get">
<input type="hidden" name="pid" id="pid">
</form>
<form id="f2" name="f2" method="get">
<select name="selectIndex">
<option value="0">按照默认排序
<option value="1">按照银子排序
<option value="2">按照绑银排序
<option value="3">按照身上的东西排序，东西名称为下面指定的名称
<option value="5">按照血上限排序
<option value="6">按照攻强排序
<option value="7">按照工资排序
</select>
指定要排序的物品名<input name="articleName">&nbsp;颜色(0白，1绿，2蓝，3紫默认白色)<input name="color">&nbsp;绑定状态(0不管1绑定2不绑定默认不管)<input name="bind">
<input type="submit" value="排序">
</form>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center"><td>编号</td><td>ID</td><td>Online</td>
<td>Identity</td>
<td>队列情况(size/push/pop/register)</td>
<td>Name</td><td>账户名</td><td>地图</td>
<td>级别</td><td>总时长</td><td>银子</td><td>绑银</td><td>工资</td><td><%=(articleName+color+bind) %>物品个数</td>
<td>血</td>
<td>物理攻强</td>
<td>法术攻强</td>
<td>蓝</td>
<td>经验值</td>
<td>本尊级别</td>
<td>本尊血量</td>
<td>本尊物攻</td>
<td>本尊法攻</td>
<td>元神级别</td>
<td>元神血量</td>
<td>元神物攻</td>
<td>元神法攻</td>
<td>心法技能等级</td>
<td>最后一次接收数据的时间</td><td>X坐标</td><td>Y坐标</td>
<td>踢下线</td>
</tr>

<%
        //int n = sm.getAmountOfPlayers();

        for(int i = 0 ; i < sprites.length ; i++){
                Connection conn = sprites[i].getConn();
                
                StatData sdata =sprites[i].getStatData(StatData.STAT_ONLINE_TIME);
                String alltime = "";
                if(sdata != null){
                	alltime = "" + (sdata.getValue()/3600000) + "小时";
                }
                String queuePushNum = "0";
                String queuePopNum = "0";
                String size = "0";
                String register = "false"       ;
                String readyNum = "1";
                if(conn != null){
                        DefaultSelectableQueue dq = (DefaultSelectableQueue) conn.getAttachment2();
                        if(dq != null){
                                size = "" + dq.size();
                                queuePushNum = "" + dq.getPushNum();
                                queuePopNum  = "" + dq.getPopNum();
                                register = "" + dq.isRegistered();
                                readyNum = "" + dq.getReadyNum();
                        }
                }
                %><tr bgcolor="#FFFFFF" align="center">
                <td><%=i+1 %></td>
                <td><a href='player/mod_player.jsp?action=select_playerId&playerid=<%=sprites[i].getId() %>'><%=sprites[i].getId() %></a></td>
                <td><%= (sprites[i].getConn() != null && sprites[i].getConn().getState() != Connection.CONN_STATE_CLOSE) %></td>
                <td><%= (sprites[i].getConn()!=null?sprites[i].getConn().getIdentity():"无连接") %></td>
                <td><%= size+"/"+queuePushNum+"/"+queuePopNum+"/"+register +"," + readyNum%></td>
                <td><%= sprites[i].getName()%></td>
                <td><%= sprites[i].getUsername()%></td>
                <td><%= sprites[i].getGame() %></td>
                <td><%= sprites[i].getSoulLevel() %></td>
                <td><%= alltime %></td>
                <td><%= sprites[i].getSilver() %></td>
                <td><%= sprites[i].getBindSilver() %></td>
                <td><%= sprites[i].getWage() %></td>
                <td><%= (articleCountMap.get(sprites[i].getName()) != null?articleCountMap.get(sprites[i].getName()):0) %></td>
                <td><%= sprites[i].getHp()+"/" + sprites[i].getMaxHP() %></td>
                <td><%= sprites[i].getPhyAttack() %></td>
                <td><%= sprites[i].getMagicAttack() %></td>
                <td><%= sprites[i].getMp()+"/" + sprites[i].getMaxMP() %></td>
                <td><%= sprites[i].getExp()+"/" + sprites[i].getNextLevelExp() %></td>
                <td><%= (sprites[i].getMainSoul() != null? sprites[i].getMainSoul().getGrade():"--") %></td>
                <td><%= (sprites[i].getMainSoul() != null? sprites[i].getMainSoul().getHp():"--") %></td>
                <td><%= (sprites[i].getMainSoul() != null? sprites[i].getMainSoul().getPhyAttack():"--") %></td>
                <td><%= (sprites[i].getMainSoul() != null? sprites[i].getMainSoul().getMagicAttack():"--") %></td>
                <td><%= (sprites[i].getSoul(1) != null? sprites[i].getSoul(1).getGrade():"--") %></td>
                <td><%= (sprites[i].getSoul(1) != null? sprites[i].getSoul(1).getHp():"--") %></td>
                <td><%= (sprites[i].getSoul(1) != null? sprites[i].getSoul(1).getPhyAttack() + "/A" + sprites[i].getSoul(1).getPhyAttackA():"--") %></td>
                <td><%= (sprites[i].getSoul(1) != null? sprites[i].getSoul(1).getMagicAttack()+ "/A:"+sprites[i].getSoul(1).getMagicAttackA():"--") %></td>
                <td><%= Arrays.toString(sprites[i].getXinfaLevels()) %></td>
                <td><%= (System.currentTimeMillis() - sprites[i].getLastRequestTime())/1000 %>秒前</td>
                <td><%=sprites[i].getX() %></td>
                <td><%=sprites[i].getY() %></td>
                <td><input type="button" value="踢下线" onclick="javascript:kickplayer(<%=sprites[i].getId() %>);"></td>
                </tr><%
        }
%>
</table>
</BODY></html>
