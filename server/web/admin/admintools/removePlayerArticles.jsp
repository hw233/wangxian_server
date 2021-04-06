<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!
	String option_show = "show"; 
	String option_do = "do"; 
	String [] positionArr = {"背包","宠物栏","防爆包","仓库","装备栏"};
%>
<%
	String option = request.getParameter("option");
	if (null == option) {
		option = "";
	}

	String playerIdS = request.getParameter("playerId");
	String articleIds = request.getParameter("articleIds");
	
%>

<%@page import="java.util.Iterator"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>移除角色的非法所得</title>
</head>
<body>
<h1 style="color: red;">移除角色的非法所得,慎用</h1>
<% if ("".equals(option)) {%>
	<form action="./removePlayerArticles.jsp" method="post">
		输入角色ID:<input name="playerId" type="text" value="<%=playerIdS%>"><br/>
		输入要删除的物品ID:<textarea rows="20" cols="15" name="articleIds"><%=articleIds %></textarea><BR/>
		<input name="option" type="hidden" value="<%=option_show%>">
		<input type="submit" value="提交">
	</form>
<%} else if (option_show.equals(option)) {%>
	<form action="./removePlayerArticles.jsp" method="post">
		<input name="playerId" type="hidden" value="<%=playerIdS%>"><br/>
		<input name="articleIds" type="hidden" value="<%=articleIds%>"><br/>
		<table>
			<tr>
				<td>物品位置</td>
				<td>物品ID</td>
				<td>物品名</td>
				<td>物品个数(若是装备栏，该值代表序号index)</td>
			</tr>
		<%
			Player player = GamePlayerManager.getInstance().getPlayer(Long.valueOf(playerIdS));
			if(player != null){
				Long [] ids = StringTool.string2Array(articleIds,"\r\n",Long.class);
				List<PlayerArticleEntity> list = getPlayerArticleEntity(player,ids);
			
				for (PlayerArticleEntity pae : list) {
					%>
					<tr>
						<td><%=positionArr[pae.position] %></td>
						<td><%=pae.ae.getId() %></td>
						<td><%=pae.ae.getArticleName() %></td>
						<td><%=pae.count %></td>
					</tr>
					<%
				}
			}else{
				out.print("无此玩家");
			}
		%>
		</table>
		<hr/>
		<input type="hidden" name="option" value="<%=option_do%>">
		输入删除密码<input name="pwd" type="password"><BR/>
		删除<input style="color: red;" type="submit" value="删除">
		<hr/>
	</form>
<%} else if(option_do.equals(option)){
	if (!"pwd".equals(request.getParameter("pwd"))) {
		out.print("<H1>没密码别得瑟</H1>");
	} else{
		Player player = GamePlayerManager.getInstance().getPlayer(Long.valueOf(playerIdS));
		Long [] ids = StringTool.string2Array(articleIds,"\r\n",Long.class);
		List<PlayerArticleEntity> list = getPlayerArticleEntity(player,ids);
		out.print("<H1>"+ removePlayerArticles(player,list) +"</H1>");
	}
} %>
</body>
</html>
<%!
	class PlayerArticleEntity {
		ArticleEntity ae;
		int position;
		//0背包，1宠物栏，2防爆包，3仓库，4身上
		int count;//若是装备栏，该值代表序号index，其它情况代表物品个数
		public PlayerArticleEntity (ArticleEntity ae,int position,int count) {
			this.ae = ae;
			this.position = position;
			this.count = count;
		}
	}

	public List<PlayerArticleEntity> getPlayerArticleEntity(Player player,Long [] ids) {
		List<PlayerArticleEntity> list = new ArrayList<PlayerArticleEntity>();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		for (Long articleId : ids) {
			//检查背包
			for (int i = 0; i < player.getKnapsacks_common().length;i++) {
				Knapsack knapsack = player.getKnapsacks_common()[i];
				if (knapsack == null) {
					continue;
				}
				for (Cell cell : knapsack.getCells()) {
					if (cell.getEntityId() > 0) {
						if (cell.getEntityId() == articleId) {
							ArticleEntity ae = aem.getEntity(cell.getEntityId());
							if (ae != null) {
								PlayerArticleEntity pae = new PlayerArticleEntity(ae,i,cell.count);
								list.add(pae);
								continue;
							}
						}
					}
				}
			}
			//检查防爆包
			Knapsack fangbao = player.getKnapsack_fangbao();
			if(fangbao != null){
				for (Cell cell : fangbao.getCells()) {
					if (cell.getEntityId() > 0) {
						if (cell.getEntityId() == articleId) {
							ArticleEntity ae = aem.getEntity(cell.getEntityId());
							if (ae != null) {
								PlayerArticleEntity pae = new PlayerArticleEntity(ae,2,cell.count);
								list.add(pae);
								continue;
							}
						}
					}
				}
			}
			
			//检查仓库
			Knapsack cangku = player.getKnapsacks_cangku();
			if(cangku != null){
				for (Cell cell : cangku.getCells()) {
					if (cell.getEntityId() > 0) {
						if (cell.getEntityId() == articleId) {
							ArticleEntity ae = aem.getEntity(cell.getEntityId());
							if (ae != null) {
								PlayerArticleEntity pae = new PlayerArticleEntity(ae,3,cell.count);
								list.add(pae);
								continue;
							}
						}
					}
				}
			}
			
			//检查身上装备栏
			Soul currSoul = player.getCurrSoul();
			EquipmentColumn ec = currSoul.getEc();
			if(ec != null){
				for (int i=0; i<ec.getEquipmentArrayByCopy().length; i++) {
					if(ec.getEquipmentArrayByCopy()[i] != null && ec.getEquipmentArrayByCopy()[i] instanceof EquipmentEntity) {
						EquipmentEntity ee = (EquipmentEntity)ec.getEquipmentArrayByCopy()[i];
						if ( ee!=null && ee.getId()> 0) {
							if (ee.getId() == articleId) {
								ArticleEntity ae = aem.getEntity(ee.getId());
								if (ae != null) {
									PlayerArticleEntity pae = new PlayerArticleEntity(ae,4,i);
									list.add(pae);
									continue;
								}
							}
						}
					}
				}
			}
			
		}		
		return list;
	}
	
	String reason = "移除角色的非法所得";
	public String removePlayerArticles(Player player,List<PlayerArticleEntity> list){
		Iterator<PlayerArticleEntity> iter = list.iterator();
		while(iter.hasNext()){
			PlayerArticleEntity pae = iter.next();
			switch(pae.position){
			case 0:
				for(int i=0; i<pae.count; i++){
					player.getKnapsacks_common()[0].removeByArticleId(pae.ae.getId(),reason,true);
				}
				break;
			case 1:	
				for(int i=0; i<pae.count; i++){
					player.getKnapsacks_common()[1].removeByArticleId(pae.ae.getId(),reason,true);
				}
				break;
			case 2:	
				for(int i=0; i<pae.count; i++){
					player.getKnapsack_fangbao().removeByArticleId(pae.ae.getId(),reason,true);
				}
				break;
			case 3:	
				for(int i=0; i<pae.count; i++){
					player.getKnapsacks_cangku().removeByArticleId(pae.ae.getId(),reason,true);
				}
				break;
			case 4:	
				player.getCurrSoul().getEc().takeOff(pae.count,player.getCurrSoul().getSoulType());
				if (Knapsack.logger.isInfoEnabled()) Knapsack.logger.info("[remove] ["+ this.getClass().getSimpleName() +"] [身上装备] [succ] [-] [owner:"+ player.getId() +"] ["+ player.getUsername() +"] ["+ player.getName() +"] [id:"+ pae.ae.getId() +"] ["+ pae.ae.getArticleName() +"] [color:"+ pae.ae.getColorType() +"] ["+ reason +"] [绑:"+ pae.ae.isBinded() +"]");
				break;
				
			}
		}
		return "已成功移除角色的非法所得！";
	}
%>