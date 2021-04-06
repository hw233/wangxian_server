<%@ page contentType="text/html;charset=utf8"%>
<%@page
	import="java.util.*,
				com.xuanzhi.tools.text.*,
				com.fy.engineserver.chat.*,
				com.fy.engineserver.mail.*,
				com.fy.engineserver.datasource.article.data.props.*,
				com.fy.engineserver.sprite.horse.*,
				com.fy.engineserver.datasource.article.data.equipments.*,
				com.fy.engineserver.mail.service.*,
				com.fy.engineserver.datasource.article.data.entity.*,
				com.fy.engineserver.datasource.article.data.articles.*,
				com.fy.engineserver.datasource.article.manager.*,
				com.fy.engineserver.economic.*,
				com.xuanzhi.boss.game.GameConstants,
				com.fy.engineserver.gateway.*,
				com.fy.gamegateway.gmaction.*,
				com.fy.engineserver.message.*,
				com.fy.engineserver.core.Game,
				com.fy.engineserver.sprite.*,
				com.fy.engineserver.country.manager.*,
				com.fy.engineserver.society.*,
				com.fy.engineserver.masterAndPrentice.*,
				com.fy.engineserver.homestead.faery.service.*,
				com.fy.engineserver.sprite.pet.PetManager,
				com.fy.engineserver.sprite.pet.Pet,
				com.fy.engineserver.datasource.article.data.entity.PetPropsEntity,
				com.fy.engineserver.homestead.cave.*"%>
<%!
String[] careerNames = new String[]{"通用","斗罗","鬼煞","灵尊","巫皇"};
public String getCareerName(int career) {
	return careerNames[career];
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf8" />
		<title>角色信息查询</title>
		<style type="text/css">
body {
	background-image: url(images/beijing.gif);
}
</style>
		<script src="pryAssets/SpryTabbedPanels.js" type="text/javascript"></script>
		<link href="SpryAssets/SpryTabbedPanels.css" rel="stylesheet"
			type="text/css" />
		<style type="text/css">
body,td,th {
	font-size: 16px;
}
</style>
		<script>
function showBanChat() {
	var banchat = document.getElementById("banchat");
	banchat.style.display = "block";
}
function showClearSilver() {
	var clearSilver = document.getElementById("clearSilver");
	clearSilver.style.display = "block";
}
function showAddSilver() {
	var addSilver = document.getElementById("addSilver");
	addSilver.style.display = "block";
}
function showAddOldArticle() {
	var addOldArticle = document.getElementById("addOldArticle");
	addOldArticle.style.display = "block";
}
function showAddNewArticle() {
	var addNewArticle = document.getElementById("addNewArticle");
	addNewArticle.style.display = "block";
}
function showFindPet() {
	var findPet = document.getElementById("findPet");
	findPet.style.display = "block";
}

function banchat() {
 	var playerId = document.getElementById("playerId").value;
 	var bantime = document.getElementById("bantime").value;
 	window.location.replace("playerinfo.jsp?action=banchat&playerId="+playerId+"&bantime=" + bantime);
}

function kickPlayer() {
 	var playerId = document.getElementById("playerId").value;
 	window.location.replace("playerinfo.jsp?action=kick&playerId="+playerId);
}

function clearSilver() {
	if(window.confirm("您确定要清除此人的银两？")) {
 		var playerId = document.getElementById("playerId").value;
	 	var reason = document.getElementById("clearSilverReason").value;
	 	window.location.replace("playerinfo.jsp?action=clearSilver&playerId="+playerId+"&reason=" + reason);
	 }
}

function addSilver() {
	var amount = document.getElementById("amount").value;
	if(amount >  50000000) {
		window.alert("您的补量超过了限制");
	} else { 
		if(window.confirm("您确定要给此人补银两" + amount + "？")) {
 			var playerId = document.getElementById("playerId").value;
		 	var reason = document.getElementById("addSilverReason").value;
		 	var title = document.getElementById("addSilverMailTitle").value;
		 	var content = document.getElementById("addSilverMailContent").value;
		 	window.location.replace("playerinfo.jsp?action=addSilver&playerId="+playerId+"&reason=" + reason + "&amount=" + amount + "&title=" + title + "&content=" + content);
		}
	}
}

function addOldArticle() {
	var entityId = document.getElementById("oldEntityId").value;
	var num = document.getElementById("oldNum").value;
	if(window.confirm("您确定要给此人补此老物品" + num + "个？")) {
 		var playerId = document.getElementById("playerId").value;
	 	var reason = document.getElementById("addOldArticleReason").value;
	 	var title = document.getElementById("addOldArticleMailTitle").value;
	 	var content = document.getElementById("addOldArticleMailContent").value;
	 	window.location.replace("playerinfo.jsp?action=addOldArticle&playerId="+playerId+ "&reason=" + reason + "&entityId=" + entityId + "&num=" + num + "&title=" + title + "&content=" + content);
	}
}

function addNewArticle() {
	var article = document.getElementById("article").value;
	var num = document.getElementById("newNum").value;
	if(window.confirm("您确定要给此人补此新物品" + num + "个？")) {
 		var playerId = document.getElementById("playerId").value;
	 	var reason = document.getElementById("addNewArticleReason").value;
	 	var title = document.getElementById("addNewArticleMailTitle").value;
	 	var color = document.getElementById("color").options[document.getElementById("color").selectedIndex].value;
	 	var binded = document.getElementById("newBinded").options[document.getElementById("newBinded").selectedIndex].value;
	 	var content = document.getElementById("addNewArticleMailContent").value;
	 	window.location.replace("playerinfo.jsp?action=addNewArticle&playerId="+playerId+ "&reason=" + reason + "&article=" + article + "&num=" + num + "&title=" + title + "&content=" + content + "&binded=" + binded + "&color=" + color);
	}
}

function findPet() {
	if(window.confirm("您确定要给此人找回宠物？")) {
 		var playerId = document.getElementById("playerId").value;
	 	var reason = document.getElementById("findPetReason").value;
	 	var petIds = document.getElementById("petIds").value;
	 	var title = document.getElementById("findPetTitle").value;
	 	var content = document.getElementById("findPetContent").value;
	 	window.location.replace("playerinfo.jsp?action=findPet&playerId="+playerId+"&reason=" + reason + "&petIds=" + petIds + "&title=" + title + "&content=" + content);
	 }
}

function clearCell(index, type) {
	if(window.confirm("您确定要清空此格？")) {
 		var playerId = document.getElementById("playerId").value;
	 	window.location.replace("playerinfo.jsp?action=clearCell&playerId="+playerId+ "&index=" + index + "&type=" + type);
	 }
}
</script>
	</head>
	<%
	String ip = request.getRemoteAddr();
	String recorder = "";
	Object o = session.getAttribute("authorize.username");
	if(o!=null){
		recorder = o.toString();
	}
	int permLevel = -1;
	com.xuanzhi.tools.authorize.AuthorizeManager am = com.xuanzhi.tools.authorize.AuthorizeManager.getInstance();
	com.xuanzhi.tools.authorize.Role role0 = am.getRoleManager().getRole("role_gm_common");
	com.xuanzhi.tools.authorize.Role role1 = am.getRoleManager().getRole("role_gm_captain");
	com.xuanzhi.tools.authorize.Role role2 = am.getRoleManager().getRole("role_gm_admin");
	String gmusername = (String)session.getAttribute("authorize.username");
	com.xuanzhi.tools.authorize.User gmuser = am.getUserManager().getUser(gmusername);
	if(gmuser != null) {
		if(gmuser.isRoleExists(role2)) {
			permLevel = 2;
		} else if(gmuser.isRoleExists(role1)) {
			permLevel = 1;
		} else if(gmuser.isRoleExists(role0)) {
			permLevel = 0;
		}
	}
	if(permLevel < 0) {
		System.out.println("[严重警告: 正在访问没有权限的页面] ["+gmusername+"] ["+request.getRequestURL()+"]");
		return;
	}
	
Player p = null;
String username = request.getParameter("username");
String playerId = request.getParameter("playerId");
if(username != null) {
	String pname = request.getParameter("playername");
	p = PlayerManager.getInstance().getPlayer(pname, username);
}
if(p == null && playerId != null) {
	long id = Long.valueOf(playerId);
	p = PlayerManager.getInstance().getPlayer(id);
	if(p != null) {
		username = p.getUsername();
	}
}
String action = request.getParameter("action");
if(p != null && action != null) {
	if(action.equals("kick")) {
		PlayerManager.getInstance().kickPlayer(p.getId());
		out.println("<script>window.alert('已经把玩家踢下线');</script>");
	} else if(action.equals("banchat")) {
		int bantime = Integer.valueOf(request.getParameter("bantime"));
		ChatMessageService.getInstance().banPlayer(p.getId(), bantime);
		out.println("<script>window.alert('已经禁言了此玩家');</script>");
	} else if(action.equals("clearSilver")) {
		if(permLevel > 0) {
			String reason = request.getParameter("reason");
			long amount = p.getSilver();
			p.setSilver(0L);
			//加入清除银两日志
			sendActionLogToGateway(session, p, GmAction.ACTION_CLEAR_SILVER, amount, "", "", new String[0], reason);
			out.println("<script>window.alert('已经清除了此玩家的银两');</script>");
		} else {
			out.println("<script>window.alert('无权限');</script>");
		}
	} else if(action.equals("addSilver")) {
		if(permLevel > 1) {
			String reason = request.getParameter("reason");
			long amount = Long.valueOf(request.getParameter("amount"));
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			MailManager.getInstance().sendMail(p.getId(), new ArticleEntity[0], new int[0], title, content, amount, 0, 0, "GM工具补发",MailSendType.后台发送,p.getName(),ip,recorder);
			sendActionLogToGateway(session, p, GmAction.ACTION_ADD_SILVER, amount, "", "", new String[0], reason);
			out.println("<script>window.alert('已经给玩家补发了银两');</script>");
		} else {
			out.println("<script>window.alert('无权限');</script>");
		}
	} else if(action.equals("addOldArticle")) {
		if(permLevel > 0) {
			String reason = request.getParameter("reason");
			long entityId = Long.valueOf(request.getParameter("entityId"));
			int num = Integer.valueOf(request.getParameter("num"));
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(entityId);
			if(entity == null) {
				out.println("<script>window.alert('无此老物品');</script>");
			} else {
				Article a = ArticleManager.getInstance().getArticle(entity.getArticleName());
				if(num > 1 && !a.isOverlap()) {
					out.println("<script>window.alert('您发的老物品不是可堆叠的，数量不能大于1');</script>");
				} else {
					MailManager.getInstance().sendMail(p.getId(), new ArticleEntity[]{entity}, new int[]{num}, title, content, 0, 0, 0, "GM工具补发",MailSendType.后台发送,p.getName(),ip,recorder);
					sendActionLogToGateway(session, p, GmAction.ACTION_ADD_OLD_ARTICLE, num, entity.getId()+"/"+entity.getArticleName()+"/绑定:"+(entity.isBinded()?"是":"否"), "", new String[0], reason);
					out.println("<script>window.alert('成功补发了老物品');</script>");
				}
			}
		} else {
			out.println("<script>window.alert('无权限');</script>");
		}
	} else if(action.equals("addNewArticle")) {
		if(permLevel > 1) {
			String reason = request.getParameter("reason");
			String articleName = request.getParameter("article");
			int num = Integer.valueOf(request.getParameter("num"));
			int color = Integer.valueOf(request.getParameter("color"));
			int binded = Integer.valueOf(request.getParameter("binded"));
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			Article a = ArticleManager.getInstance().getArticle(articleName);
			if(a == null) {
				out.println("<script>window.alert('无此物品');</script>");
			} else {
				if(a.isOverlap()) {
					if(num < 999) {
						//ArticleEntity e = ArticleEntityManager.getInstance().getEntity(a.getName(), binded==0?false:true, color==-1?a.getColorType():color);
						//if(e == null) {
						ArticleEntity	e = ArticleEntityManager.getInstance().createEntity(a, binded==0?false:true, ArticleEntityManager.CREATE_REASON_GMSEND, null, color==-1?a.getColorType():color, num, true);
						//}
						MailManager.getInstance().sendMail(p.getId(), new ArticleEntity[]{e}, new int[]{num}, title, content, 0, 0, 0, "GM工具补发",MailSendType.后台发送,p.getName(),ip,recorder);
						sendActionLogToGateway(session, p, GmAction.ACTION_ADD_NEW_ARTICLE, num, e.getId()+"/"+e.getArticleName()+"/颜色:"+(ArticleManager.getColorString(a, color==-1?a.getColorType():color))+"绑定:"+(e.isBinded()?"是":"否"), "", new String[0], reason);
						out.println("<script>window.alert('成功补发了新物品');</script>");
					} else {
						out.println("<script>window.alert('发送新物品超过数量限制');</script>");
					}
				} else {
					if(num <= 5) {
						List<ArticleEntity> elist = new ArrayList<ArticleEntity>();
						for(int i=0; i<num; i++) {
							ArticleEntity e = ArticleEntityManager.getInstance().createEntity(a, binded==0?false:true, ArticleEntityManager.CREATE_REASON_GMSEND, null, color==-1?a.getColorType():color, num, true);
							elist.add(e);
						}
						int nums[] = new int[num];
						for(int i=0; i<nums.length; i++) {
							nums[i] = 1;
						}
						MailManager.getInstance().sendMail(p.getId(), elist.toArray(new ArticleEntity[0]), nums, title, content, 0, 0, 0, "GM工具补发",MailSendType.后台发送,p.getName(),ip,recorder);
						sendActionLogToGateway(session, p, GmAction.ACTION_ADD_OLD_ARTICLE, num, a.getName()+"/颜色:"+(ArticleManager.getColorString(a, color==-1?a.getColorType():color))+"绑定:"+(binded==0?"否":"是"), "", new String[0], reason);
						out.println("<script>window.alert('成功补发了新物品');</script>");
					} else {
						out.println("<script>window.alert('发送新物品超过数量限制');</script>");
					}
				}
			}
		} else {
			out.println("<script>window.alert('无权限');</script>");
		}
	} else if(action.equals("findPet")) {
		if(permLevel > 0) {
			String reason = request.getParameter("reason");
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			String petIds = request.getParameter("petIds");
			long petId = Long.parseLong(petIds);
			Pet pet = PetManager.getInstance().getPet(petId);
			if(pet != null){
					pet.setDelete(false);
					long petEntityId = pet.getPetPropsId();
					ArticleEntity ae =  ArticleEntityManager.getInstance().getEntity(petEntityId);
					if(ae != null){
						if(ae instanceof PetPropsEntity){
							PetPropsEntity ppe = (PetPropsEntity)ae;
							String eggName =  ppe.getEggArticle();
							
							Article a = ArticleManager.getInstance().getArticle(eggName);
							PetEggPropsEntity pep = (PetEggPropsEntity) ArticleEntityManager.getInstance().createEntity(a, false, ArticleEntityManager.CREATE_REASON_PET_SEAL, null, 0, 1, true);
							pep.setPetId(pet.getId());
							if (pet.isIdentity()) {
								switch (pet.getQuality()) {
								case 0:
									pep.setColorType(0);
									break;
								case 1:
									pep.setColorType(1);
									break;
								case 2:
									pep.setColorType(2);
									break;
								case 3:
									pep.setColorType(3);
									break;
								case 4:
									pep.setColorType(4);
									break;
								default:
									break;
		
								}
							} else {
								pep.setColorType(0);
							}
							
							if(pep != null){
								MailManager.getInstance().sendMail(-1,p.getId(), new ArticleEntity[]{pep}, title, content, 0, 0, 0,"GM工具补删除宠物",MailSendType.后台发送,p.getName(),ip,recorder);
								PetManager.logger.warn("[找回删除宠物成功] ["+p.getLogString()+"] [petId:"+pet.getId()+"]");
								sendActionLogToGateway(session, p, GmAction.ACTION_ADD_PET, 1, "", pep.getId() + "/" + pep.getArticleName(), new String[0], reason);
								out.println("<script>window.alert('成功给玩家找回了宠物');</script>");
							}
						}else{
							out.println("<script>window.alert('找回宠物失败,不是宠物蛋道具');</script>");
						}
					}else{
						out.println("<script>window.alert('找回宠物失败,没有找到宠物实体"+petEntityId+"');</script>");
					}
			
			}else{
				out.println("<script>window.alert('找回宠物失败,没有这个宠物"+petId+"');</script>");
			}
		} else {
			out.println("<script>window.alert('无权限');</script>");
		}
	} else if(action.equals("clearCell")) {
		if(permLevel > 0) {
			int index = Integer.valueOf(request.getParameter("index"));
			String type = request.getParameter("type");
			if(type.equals("common")) {
				p.getKnapsack_common().clearCell(index, "GM后台操作", false);
				ArticleEntity ar = p.getKnapsack_common().getArticleEntityByCell(index);
				int num = p.getKnapsack_common().getCell(index).getCount();
				if(ar != null) {
					sendActionLogToGateway(session, p, GmAction.ACTION_CLEAR_KNAPSACKCELL, num, ar.getArticleName(), "", new String[0], "清空背包格子" + index);
				}
			} else if(type.equals("fangbao")) {
				p.getKnapsack_fangbao().clearCell(index, "GM后台操作", false);
				ArticleEntity ar = p.getKnapsack_fangbao().getArticleEntityByCell(index);
				int num = p.getKnapsack_fangbao().getCell(index).getCount();
				if(ar != null) {
					sendActionLogToGateway(session, p, GmAction.ACTION_CLEAR_KNAPSACKCELL, num, ar.getArticleName(), "", new String[0], "清空防爆包格子" + index);
				}
			} else if(type.equals("cangku")) {
				p.getKnapsacks_cangku().clearCell(index, "GM后台操作", false);
				ArticleEntity ar = p.getKnapsacks_cangku().getArticleEntityByCell(index);
				int num = p.getKnapsacks_cangku().getCell(index).getCount();
				if(ar != null) {
					sendActionLogToGateway(session, p, GmAction.ACTION_CLEAR_KNAPSACKCELL, num, ar.getArticleName(), "", new String[0], "清空仓库格子" + index);
				}
			}
		}
	}
} 
%>
	<body>
		<br>
		<br>
		<%if(p != null) { %>
		<input type=hidden name=playerId id='playerId' value="<%=p.getId() %>">
		<table width="768" border="1" align="center" cellpadding="1"
			cellspacing="1">
			<tr>
				<td colspan="3" align="center" valign="middle" bgcolor="#05BDC7">
					角色基本信息：
				</td>
			</tr>
			<tr>
				<td width="97">
					角色ID：：
				</td>
				<td width="248" align="left" valign="middle">
					&nbsp;<%=p.getId() %></td>
				<td width="300" rowspan="5" align="center" valign="middle">
				<%if(PlayerManager.getInstance().isOnline(p.getId())){ %>
					<input type="button" name="button7" id="button7" value="踢下线"
						onclick="kickPlayer();" />
				<%} %>
				</td>
			</tr>
			<tr>
				<td>
					角色名称：
				</td>
				<td>
					&nbsp;<%=p.getName() %></td>
			</tr>
			<tr>
				<td>
					账号：
				</td>
				<td>
					&nbsp;<%=p.getUsername() %></td>
			</tr>
			<tr>
				<td>
					VIP等级：
				</td>
				<td>
					&nbsp;<%=p.getVipLevel() %></td>
			</tr>
			<tr>
				<td>
					职业：
				</td>
				<td>
					&nbsp;<%=getCareerName(p.getCareer()) %></td>
			</tr>
			<tr>
				<td>
					国家：
				</td>
				<td>
					&nbsp;<%=CountryManager.得到国家名(p.getCountry()) %></td>
				<td rowspan="5" align="center" valign="middle">
					<input type="button" name="button9" id="button9" value="查询邮件"
						onclick="window.open('mails.jsp?stype=sub2&playerId=<%=p.getId() %>','_blank')" />
				</td>
			</tr>
			<tr>
				<td>
					仙府：
				</td>
				<td>
					&nbsp;<%Cave cave = FaeryManager.getInstance().getCave(p);if(cave != null) {out.print( cave.getFaery().getName() );}%>
				</td>
			</tr>
			<tr>
				<td>
					在线状态：
				</td>
				<td>
					&nbsp;<%=PlayerManager.getInstance().isOnline(p.getId())?"在线":"离线" %></td>
			</tr>
			<tr>
				<td>
					所在地图：
				</td>
				<td>
					&nbsp;<%=p.getLastGame() %></td>
			</tr>
			<tr>
				<td>
					配偶：
				</td>
				<td>
					&nbsp;<%=p.getSpouse() %></td>
			</tr>
			<tr>
				<td>
					性别：
				</td>
				<td>
					&nbsp;<%=p.getSex()==0?"男":"女" %></td>
				<td rowspan="4" align="center" valign="middle">
					<%if(!ChatMessageService.getInstance().isPlayerBaned(p.getId())) {%>
					<input type="button" name="button8" id="button8" value="禁言" onclick="showBanChat()"/>
					<div id="banchat" style="display:none;"><br>
						封禁时间:
						<input type=text name=bantime id="bantime" size=6 value="0">小时，0为永久<br>
						<input type=button name=sub1 value="确定" onclick="banchat();">
					</div>
					<%}  else {%>
					已禁言
					<%} %>
				</td>
			</tr>
			<tr>
				<td>
					级别：
				</td>
				<td>
					&nbsp;<%=p.getLevel() %></td>
			</tr>
			<tr>
				<td>
					宗族：
				</td>
				<td>
					&nbsp;<%=p.getZongPaiName() %></td>
			</tr>
			<tr>
				<td>
					家族：
				</td>
				<td>
					&nbsp;<%=p.getJiazuName() %></td>
			</tr>
			<tr>
				<td>
					银子：
				</td>
				<td>
					&nbsp;<%=p.getSilver() %></td>
				<td rowspan="5" align="center" valign="middle">
					<input type="button" name="button10" id="button10" value="发送GM邮件" onclick="window.open('/gm/gmuser/sendMail.jsp?username=<%=username %>','_blank')"/>
				</td>
			</tr>
			<tr>
				<td>
					绑银：
				</td>
				<td>
					&nbsp;<%=p.getBindSilver() %></td>
			</tr>
			<tr>
				<td>
					称号：
				</td>
				<td>
					&nbsp;<%=p.getTitle() %></td>
			</tr>
			<tr>
				<td>
					师徒列表：
				</td>
				<td>
					<%=getShituRelation(p)%>
				</td>
			</tr>
			<tr>
				<td>
					好友列表：
				</td>
				<td>
					<%=getFriends(p) %>
				</td>
			</tr>
		</table>
		<p>
			&nbsp;
		</p>
		<table width="1050" border="1" align="center" cellpadding="1"
			cellspacing="1">
			<tr>
				<td colspan="6" align="center" bgcolor="#F5620A">
					高级操作：
				</td>
			</tr>
			<tr align="center">
				<td valign="middle" bgcolor="#999999">&nbsp;
					<%if(permLevel > 0) {%>
					<input type="button" name="button" id="button" value="清空银两"
						onclick="showClearSilver()" />
					<div id="clearSilver" style="display: none; text-align:left;">
						原因:
						<input type=text name=clearSilverReason id="clearSilverReason"
							size=10>
						<input type=button name=bu1 value="确定" onclick="clearSilver();">
					</div>
					<%}%>
				</td>
				<td valign="middle" bgcolor="#999999">&nbsp;
					<%if(permLevel > 1) { %>
					<input type="button" name="button2" id="button2" value="补发银两"
						onclick="showAddSilver();" />
					<div id="addSilver" style="display: none;text-align:left;">
						额度:
						<input type="text" id="amount" size="6"><br>
						原因:
						<input type=text name=addSilverReason id="addSilverReason" size=10><br>
						邮件标题:
						<input type=text name=addSilverMailTitle id='addSilverMailTitle'
							value='系统补发银两'><br>
						邮件内容:
						<input type=text name=addSilverMailContent
							id='addSilverMailContent' value='这是系统给您补发的银两，请收取'><br>
						<input type=button name=bu1 value="确定" onclick="addSilver();">
					</div>
					<%} %>
				</td>
				<td valign="middle" bgcolor="#999999">&nbsp;
					<%if(permLevel > 0) { %>
					<input type="button" name="button3" id="button3" value="补发旧物品"
						onclick="showAddOldArticle();" />
					<div id="addOldArticle" style="display: none;text-align:left;">
						物品ID:
						<input type=text id="oldEntityId" size=5><br>
						数量:
						<input type=text id="oldNum" size=5 value="1"><br>
						原因:
						<input type=text id="addOldArticleReason" size=5><br>
						邮件标题:
						<input type=text name=addOldArticleMailTitle
							id='addOldArticleMailTitle' value='系统补发丢弃物品'><br>
						邮件内容:
						<input type=text name=addOldArticleMailContent
							id='addOldArticleMailContent' value='这是系统给您补发的丢弃物品，请收取'><br>
						<input type=button name=bu1 value="确定" onclick="addOldArticle();">
					</div>
					<%} %>
				</td>
				<td valign="middle" bgcolor="#999999">&nbsp;
					<%if(permLevel > 1) {%>
					<input type="button" name="button4" id="button4" value="补发新物品"
						onclick="showAddNewArticle();" />
					<div id="addNewArticle" style="display: none;text-align:left;">
						物品名:
						<input type=text id="article" size=5><br>
						数量:
						<input type=text id="newNum" size=5 value="1"><br>
						颜色:
						<select id="color">
							<option value="-1">默认</option>
							<option value="0">白色</option>
							<option value="1">绿色</option>
							<option value="2">蓝色</option>
							<option value="3">紫色</option>
						</select>
						<br>
						是否绑定:
						<select id="newBinded">
							<option value=0>
								否
							</option>
							<option value=1>
								是
							</option>
						</select><br>
						原因:
						<input type=text id="addNewArticleReason" size=5><br>
						邮件标题:
						<input type=text name=addNewArticleMailTitle
							id='addNewArticleMailTitle' value='系统补发物品'><br>
						邮件内容:
						<input type=text name=addNewArticleMailContent
							id='addNewArticleMailContent' value='这是系统给您补发的物品，请收取'><br>
						<input type=button name=bu1 value="确定" onclick="addNewArticle();">
					</div>
					<%} %>
				</td>
				<td valign="middle" bgcolor="#999999">&nbsp;
					<%if(permLevel > 0) {%>
					<input type="button" name="button5" id="button5" value="补发宠物"
						onclick="showFindPet();" />
					<div id="findPet" style="display: none;text-align:left;">
						宠物id:
						<input type="text" name="petIds" id="petIds"/><br>
						<br />
						原因:
						<input type=text id="findPetReason" id="findPetReason" size=5><br>
						<br>
						邮件标题:
						<input type="text" name="findPetTitle" id="findPetTitle" value="丢失宠物找回" /><br>
						<br />
						邮件内容:
						<input type="text" name="findPetContent" id="findPetContent"
							value="您好，由于误操作放生，导致宠物删除，经过核实给您补上。" /><br>
						<br />
						<input type="button" name=bu1 value="确定" onclick="findPet();" />
					</div>
					<%} %>
				</td>
			</tr>
		</table>
		<p>
			&nbsp;
		</p>
		<%
		//装备栏
		EquipmentColumn currentEC = p.getEquipmentColumns();
		EquipmentColumn soulEC = null;
		Soul soul = p.getSoul(Soul.SOUL_TYPE_SOUL);
		if(soul != null) {
			soulEC = soul.getEc();
		}
		if(soulEC == null) {
			soulEC = new EquipmentColumn();
		}
		%>
		<table width="1050" height="272" border="1" align="center"
			cellpadding="1" cellspacing="1">
			<tr>
				<td colspan="2" align="center" valign="middle" bgcolor="#05BDC7">
					当前装备栏：
				</td>
				<td colspan="2" align="center" valign="middle" bgcolor="#05BDC7">
					元神装备栏：
				</td>
			</tr>
			<tr>
				<td width="115" height="21">
					武器：
				</td>
				<td width="412">
				<%
				String name = "空";
				String color = "";
				String id = "";
				ArticleEntity ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_weapon);
				EquipmentEntity ee = null;
				if(ae instanceof EquipmentEntity){
					ee = (EquipmentEntity)ae;
				}
				if(ee != null) {
					id = String.valueOf(ee.getId());
					name = ee.getArticleName();
					Article a = ArticleManager.getInstance().getArticle(name);
					if(a != null) {
						color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
					}
				}
				name = name + color;
				%>
					<%=id %>&nbsp;<%=name %>
				</td>
				<td width="132">
					武器：
				</td>
				<td width="368">
					<%
					name = "空";
					color = "";
					id = "";
					 ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_weapon);
					 if(ae instanceof EquipmentEntity){
						 ee = (EquipmentEntity)ae;
					 }
					if(ee != null) {
						id = String.valueOf(ee.getId());
						name = ee.getArticleName();
						Article a = ArticleManager.getInstance().getArticle(name);
						if(a != null) {
							color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
						}
					}
					name = name + color;
					%>
					<%=id %>&nbsp;<%=name %>
				</td>
			</tr>
			<tr>
				<td height="21">
					头盔：
				</td>
				<td>
				<%
				name = "空";
				color = "";
				id = "";
				ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_head);
				 if(ae instanceof EquipmentEntity){
					 ee = (EquipmentEntity)ae;
				 }
				if(ee != null) {
					id = String.valueOf(ee.getId());
					name = ee.getArticleName();
					Article a = ArticleManager.getInstance().getArticle(name);
					if(a != null) {
						color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
					}
				}
				name = name + color;
				%>
					<%=id %>&nbsp;<%=name %>
				</td>
				<td>
					头盔：
				</td>
				<td>
				<%
				name = "空";
				color = "";
				id = "";
				ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_head);
				 if(ae instanceof EquipmentEntity){
					 ee = (EquipmentEntity)ae;
				 }
				if(ee != null) {
					id = String.valueOf(ee.getId());
					name = ee.getArticleName();
					Article a = ArticleManager.getInstance().getArticle(name);
					if(a != null) {
						color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
					}
				}
				name = name + color;
				%>
					<%=id %>&nbsp;<%=name %>
				</td>
			</tr>
			<tr>
				<td height="21">
					护肩：
				</td>
				<td>
				<%
				name = "空";
				color = "";
				id = "";
				ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_shoulder);
				 if(ae instanceof EquipmentEntity){
					 ee = (EquipmentEntity)ae;
				 }
				if(ee != null) {
					id = String.valueOf(ee.getId());
					name = ee.getArticleName();
					Article a = ArticleManager.getInstance().getArticle(name);
					if(a != null) {
						color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
					}
				}
				name = name + color;
				%>
					<%=id %>&nbsp;<%=name %>
				</td>
				<td>
					护肩：
				</td>
				<td>
				<%
				name = "空";
				color = "";
				id = "";
				ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_shoulder);
				 if(ae instanceof EquipmentEntity){
					 ee = (EquipmentEntity)ae;
				 }
				if(ee != null) {
					id = String.valueOf(ee.getId());
					name = ee.getArticleName();
					Article a = ArticleManager.getInstance().getArticle(name);
					if(a != null) {
						color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
					}
				}
				name = name + color;
				%>
					<%=id %>&nbsp;<%=name %>
				</td>
			</tr>
			<tr>
				<td height="21">
					胸：
				</td>
				<td>
				<%
				name = "空";
				color = "";
				id = "";
				ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_body);
				 if(ae instanceof EquipmentEntity){
					 ee = (EquipmentEntity)ae;
				 }
				if(ee != null) {
					id = String.valueOf(ee.getId());
					name = ee.getArticleName();
					Article a = ArticleManager.getInstance().getArticle(name);
					if(a != null) {
						color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
					}
				}
				name = name + color;
				%>
					<%=id %>&nbsp;<%=name %>
				</td>
				<td>
					胸：
				</td>
				<td>
				<%
				name = "空";
				color = "";
				id = "";
				ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_body);
				 if(ae instanceof EquipmentEntity){
					 ee = (EquipmentEntity)ae;
				 }
				if(ee != null) {
					id = String.valueOf(ee.getId());
					name = ee.getArticleName();
					Article a = ArticleManager.getInstance().getArticle(name);
					if(a != null) {
						color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
					}
				}
				name = name + color;
				%>
					<%=id %>&nbsp;<%=name %>
				</td>
			</tr>
			<tr>
				<td height="21">
					护腕：
				</td>
				<td>
				<%
				name = "空";
				color = "";
				id = "";
				ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_hand);
				 if(ae instanceof EquipmentEntity){
					 ee = (EquipmentEntity)ae;
				 }
				if(ee != null) {
					id = String.valueOf(ee.getId());
					name = ee.getArticleName();
					Article a = ArticleManager.getInstance().getArticle(name);
					if(a != null) {
						color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
					}
				}
				name = name + color;
				%>
					<%=id %>&nbsp;<%=name %>
				</td>
				<td>
					护腕：
				</td>
				<td>
				<%
				name = "空";
				color = "";
				id = "";
				ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_hand);
				 if(ae instanceof EquipmentEntity){
					 ee = (EquipmentEntity)ae;
				 }
				if(ee != null) {
					id = String.valueOf(ee.getId());
					name = ee.getArticleName();
					Article a = ArticleManager.getInstance().getArticle(name);
					if(a != null) {
						color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
					}
				}
				name = name + color;
				%>
					<%=id %>&nbsp;<%=name %>
				</td>
			</tr>
			<tr>
				<td height="21">
					腰带：
				</td>
				<td>
				<%
				name = "空";
				color = "";
				id = "";
				ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_belt);
				 if(ae instanceof EquipmentEntity){
					 ee = (EquipmentEntity)ae;
				 }
				if(ee != null) {
					id = String.valueOf(ee.getId());
					name = ee.getArticleName();
					Article a = ArticleManager.getInstance().getArticle(name);
					if(a != null) {
						color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
					}
				}
				name = name + color;
				%>
					<%=id %>&nbsp;<%=name %>
				</td>
				<td>
					腰带：
				</td>
				<td>
				<%
				name = "空";
				color = "";
				id = "";
				ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_belt);
				 if(ae instanceof EquipmentEntity){
					 ee = (EquipmentEntity)ae;
				 }
				if(ee != null) {
					id = String.valueOf(ee.getId());
					name = ee.getArticleName();
					Article a = ArticleManager.getInstance().getArticle(name);
					if(a != null) {
						color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
					}
				}
				name = name + color;
				%>
					<%=id %>&nbsp;<%=name %>
				</td>
			</tr>
			<tr>
				<td height="21">
					靴子：
				</td>
				<td>
				<%
				name = "空";
				color = "";
				id = "";
				ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_foot);
				 if(ae instanceof EquipmentEntity){
					 ee = (EquipmentEntity)ae;
				 }
				if(ee != null) {
					id = String.valueOf(ee.getId());
					name = ee.getArticleName();
					Article a = ArticleManager.getInstance().getArticle(name);
					if(a != null) {
						color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
					}
				}
				name = name + color;
				%>
					<%=id %>&nbsp;<%=name %>
				</td>
				<td>
					靴子：
				</td>
				<td>
				<%
				name = "空";
				color = "";
				id = "";
				ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_foot);
				 if(ae instanceof EquipmentEntity){
					 ee = (EquipmentEntity)ae;
				 }
				if(ee != null) {
					id = String.valueOf(ee.getId());
					name = ee.getArticleName();
					Article a = ArticleManager.getInstance().getArticle(name);
					if(a != null) {
						color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
					}
				}
				name = name + color;
				%>
					<%=id %>&nbsp;<%=name %>
				</td>
			</tr>
			<tr>
				<td height="21">
					首饰：
				</td>
				<td>
				<%
				name = "空";
				color = "";
				id = "";
				ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_jewelry);
				 if(ae instanceof EquipmentEntity){
					 ee = (EquipmentEntity)ae;
				 }
				if(ee != null) {
					id = String.valueOf(ee.getId());
					name = ee.getArticleName();
					Article a = ArticleManager.getInstance().getArticle(name);
					if(a != null) {
						color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
					}
				}
				name = name + color;
				%>
					<%=id %>&nbsp;<%=name %>
				</td>
				<td>
					首饰：
				</td>
				<td>
				<%
				name = "空";
				color = "";
				id = "";
				ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_jewelry);
				 if(ae instanceof EquipmentEntity){
					 ee = (EquipmentEntity)ae;
				 }
				if(ee != null) {
					id = String.valueOf(ee.getId());
					name = ee.getArticleName();
					Article a = ArticleManager.getInstance().getArticle(name);
					if(a != null) {
						color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
					}
				}
				name = name + color;
				%>
					<%=id %>&nbsp;<%=name %>
				</td>
			</tr>
			<tr>
				<td height="21">
					项链:
				</td>
				<td>
				<%
				name = "空";
				color = "";
				id = "";
				ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_necklace);
				 if(ae instanceof EquipmentEntity){
					 ee = (EquipmentEntity)ae;
				 }
				if(ee != null) {
					id = String.valueOf(ee.getId());
					name = ee.getArticleName();
					Article a = ArticleManager.getInstance().getArticle(name);
					if(a != null) {
						color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
					}
				}
				name = name + color;
				%>
					<%=id %>&nbsp;<%=name %>
				</td>
				<td>
					项链:
				</td>
				<td>
				<%
				name = "空";
				color = "";
				id = "";
				ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_necklace);
				 if(ae instanceof EquipmentEntity){
					 ee = (EquipmentEntity)ae;
				 }
				if(ee != null) {
					id = String.valueOf(ee.getId());
					name = ee.getArticleName();
					Article a = ArticleManager.getInstance().getArticle(name);
					if(a != null) {
						color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
					}
				}
				name = name + color;
				%>
					<%=id %>&nbsp;<%=name %>
				</td>
			</tr>
			<tr>
				<td height="21">
					戒指：
				</td>
				<td>
				<%
				name = "空";
				color = "";
				id = "";
				ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_fingerring);
				 if(ae instanceof EquipmentEntity){
					 ee = (EquipmentEntity)ae;
				 }
				if(ee != null) {
					id = String.valueOf(ee.getId());
					name = ee.getArticleName();
					Article a = ArticleManager.getInstance().getArticle(name);
					if(a != null) {
						color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
					}
				}
				name = name + color;
				%>
					<%=id %>&nbsp;<%=name %>
				</td>
				<td>
					戒指：
				</td>
				<td>
				<%
				name = "空";
				color = "";
				id = "";
				ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_fingerring);
				 if(ae instanceof EquipmentEntity){
					 ee = (EquipmentEntity)ae;
				 }
				if(ee != null) {
					id = String.valueOf(ee.getId());
					name = ee.getArticleName();
					Article a = ArticleManager.getInstance().getArticle(name);
					if(a != null) {
						color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
					}
				}
				name = name + color;
				%>
					<%=id %>&nbsp;<%=name %>
				</td>
			</tr>
			<tr>
				<td height="21">
					坐骑：
				</td>
				<td>
					&nbsp;<%
					long horseId = p.getRidingHorseId();
					Horse rideHorse = null;
					if(horseId > 0) {
						rideHorse = HorseManager.getInstance().getHorseById(horseId, p);
					}
					%><%=rideHorse!=null?rideHorse.getHorseName():"无" %>
				</td>
				<td>
					坐骑：
				</td>
				<td>
					&nbsp;
				<%
				if(soul != null) {
					horseId = soul.getRidingHorseId();
					if(horseId > 0) {
						rideHorse = HorseManager.getInstance().getHorseById(horseId, p);
					}
				}
				%><%=rideHorse!=null?rideHorse.getHorseName():"无" %>
				</td>
			</tr>
		</table>
		<p>
			&nbsp;
		</p>
		<%
		Knapsack knap = p.getKnapsack_common();
		Cell cells[] = knap.getCells();
		%>
		<table width="1050" border="1" align="center" cellpadding="1"
			cellspacing="1">
			<tr>
				<td colspan="5" align="center" valign="middle" bgcolor="#05BDC7">
					背包栏：
				</td>
			</tr>
			<tr>
			<%
			for(int i=0; i<cells.length; i++) {
				name = "空";
				color = "";
				id = "";
				long eId = cells[i].getEntityId();
				int eNum = cells[i].getCount();
				ArticleEntity e = null;
				if(eId > 0 && eNum > 0) {
					e = ArticleEntityManager.getInstance().getEntity(eId);
					if(e != null) {
						id = String.valueOf(e.getId());
						name = e.getArticleName();
						Article a = ArticleManager.getInstance().getArticle(name);
						if(a != null) {
							color = "("+ArticleManager.getColorString(a, e.getColorType())+")";
							name = name + color;
						}
					}
				}
			%>
				<td>
					<%=eId %>&nbsp;<%=name %>
					<%if((e!=null || eId > 0) && permLevel > 0) {%>
					<input type=button name=bt1 value="清空" onclick="clearCell('<%=i %>','common');">
					<%} %>
				</td>
			<%
			if((i+1)%5 == 0) {
				out.println("</tr><tr>");
			}
			}
			%>
		</table>
		<p>
			&nbsp;
		</p>
		<%
		knap = p.getPetKnapsack();
		cells = knap.getCells();
		%>
		<table width="1050" border="1" align="center" cellpadding="1"
			cellspacing="1">
			<tr>
				<td colspan="5" align="center" valign="middle" bgcolor="#05BDC7">
					宠物栏：
				</td>
			</tr>
			<tr>
			<%
			id = "";
			for(int i=0; i<cells.length; i++) {
				long pEggId = cells[i].getEntityId();
				Pet pet = null;
				if(pEggId > 0) {
					PetPropsEntity ppe = (PetPropsEntity) ArticleEntityManager.getInstance().getEntity(pEggId);
					if(ppe != null) {
						id = String.valueOf(ppe.getId());
						pet = PetManager.getInstance().getPet(ppe.getPetId());
					}
				}
				name = "空";
				if(pet != null) {
					name = pet.getName() + "("+PetManager.得到宠物稀有度名(pet.getRarity())+")";
				}
			%>
				<td>
					<%=id %>&nbsp;<%=name %>
				</td>
			<%
			if((i+1)%5 == 0) {
				out.println("</tr><tr>");
			}
			}
			%>
		</table>
		<p>
			&nbsp;
		</p>
		<%
		knap = p.getKnapsack_fangbao();
		if(knap != null) {
			cells = knap.getCells();
		} else {
			cells = new Cell[0];
		}
		%>
		<table width="1050" border="1" align="center" cellpadding="1"
			cellspacing="1">
			<tr>
				<td colspan="5" align="center" valign="middle" bgcolor="#05BDC7">
					防爆背包：
				</td>
			</tr>
			<tr>
			<%
			for(int i=0; i<cells.length; i++) {
				name = "空";
				color = "";
				id = "";
				long eId = cells[i].getEntityId();
				int eNum = cells[i].getCount();
				ArticleEntity e = null;
				if(eId > 0 && eNum > 0) {
					e = ArticleEntityManager.getInstance().getEntity(eId);
					if(e != null) {
						id = String.valueOf(e.getId());
						name = e.getArticleName();
						Article a = ArticleManager.getInstance().getArticle(name);
						if(a != null) {
							color = "("+ArticleManager.getColorString(a, e.getColorType())+")";
							name = name + color;
						}
					}
				}
			%>
				<td>
					<%=eId %>&nbsp;<%=name %>
					<%if((e!=null || eId > 0) && permLevel > 0) {%>
					<input type=button name=bt1 value="清空" onclick="clearCell('<%=i %>','fangbao');">
					<%} %>
				</td>
			<%
			if((i+1)%5 == 0) {
				out.println("</tr><tr>");
			}
			}
			%>
		</table>
		<p>
			&nbsp;
		</p>
		<%
		knap = p.getKnapsacks_cangku();
		cells = knap.getCells();
		%>
		<table width="1050" border="1" align="center" cellpadding="1"
			cellspacing="1">
			<tr>
				<td colspan="5" align="center" valign="middle" bgcolor="#05BDC7">
					仓库：
				</td>
			</tr>
			<tr>
			<%
			for(int i=0; i<cells.length; i++) {
				name = "空";
				color = "";
				id = "";
				long eId = cells[i].getEntityId();
				int eNum = cells[i].getCount();
				ArticleEntity e = null;
				if(eId > 0 && eNum > 0) {
					e = ArticleEntityManager.getInstance().getEntity(eId);
					if(e != null) {
						id = String.valueOf(e.getId());
						name = e.getArticleName();
						Article a = ArticleManager.getInstance().getArticle(name);
						if(a != null) {
							color = "("+ArticleManager.getColorString(a, e.getColorType())+")";
							name = name + color;
						}
					}
				}
			%>
				<td>
					<%=eId %>&nbsp;<%=name %>
					<%if((e!=null || eId > 0) && permLevel > 0) {%>
					<input type=button name=bt1 value="清空" onclick="clearCell('<%=i %>','cangku');">
					<%} %>
				</td>
			<%
			if((i+1)%5 == 0) {
				out.println("</tr><tr>");
			}
			}
			%>
		</table>
		<p>
			&nbsp;
		</p>
		<p>
			&nbsp;
		</p>
		<p>
			&nbsp;
		</p>
		<script type="text/javascript">
var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1");
</script>
		<% }%>
		
	</body>
</html>
<%!
public String getShituRelation(Player p) {
	Relation relation = SocialManager.getInstance().getRelationById(p.getId());
	MasterPrentice mp = relation.getMasterPrentice();
	PlayerSimpleInfo info = null;
	if(mp != null){
		long masterId = mp.getMasterId();
		if(masterId != -1){
			if(masterId != -1){
				try {
					info = PlayerSimpleInfoManager.getInstance().getInfoById(masterId);
					return "师傅 - " + masterId + " - " + info.getName() + " - " + getCareerName(info.getCareer()) + " - " + info.getLevel() + "<br>";
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else{
			List<Long> prenticeIds = mp.getPrentices();			
			StringBuffer sb = new StringBuffer();
			for(long id : prenticeIds){
				try {
					info = PlayerSimpleInfoManager.getInstance().getInfoById(id);
					sb.append("徒弟 - " + id + " - " + info.getName() + " - " + getCareerName(info.getCareer()) + " - " + info.getLevel() + "<br>");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return sb.toString();
		}
	}
	return "";
}

public String getFriends(Player p) {
	Relation relation = SocialManager.getInstance().getRelationById(p.getId());
	if(relation != null) {
		List<Long> flist = relation.getFriendlist();
		StringBuffer sb = new StringBuffer();
		for(long id : flist){
			try {
				PlayerSimpleInfo info = PlayerSimpleInfoManager.getInstance().getInfoById(id);
				sb.append("好友 - " + id + " - " + info.getName() + " - " + getCareerName(info.getCareer()) + " - " + info.getLevel() + "<br>");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	return "";
}

public void sendActionLogToGateway(HttpSession session, Player p, int actionType, long amount, String articleInfo, 
		String petInfo, String otherInfo[], String reason) {
	GmAction ga = new GmAction();
	ga.setActionTime(System.currentTimeMillis());
	ga.setActionType(actionType);
	ga.setAmount(amount);
	ga.setArticleInfo(articleInfo);
	String sessionUser = (String)session.getAttribute("authorize.username");
	if(sessionUser == null) {
		sessionUser = "";
	}
	ga.setOperator(sessionUser);
	ga.setOtherInfos(otherInfo);
	ga.setPetInfo(petInfo);
	ga.setPlayerId(p.getId());
	ga.setPlayerName(p.getName());
	ga.setReason(reason);
	ga.setServerName(GameConstants.getInstance().getServerName());
	ga.setUserName(p.getUsername());
	GM_ACTION_REQ req = new GM_ACTION_REQ(GameMessageFactory.nextSequnceNum(), ga);
	MieshiGatewayClientService.getInstance().sendMessageToGateway(req);
	Game.logger.error("[进行GM操作] [] ["+sessionUser+"] ["+p.getLogString()+"] [action:"+actionType+"] [amount:"+amount+"] [article:"+articleInfo+"] [pet:"+petInfo+"] [reason:"+reason+"]");
}
%>
