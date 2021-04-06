<%@page import="com.fy.engineserver.mail.MailSendType"%>
<%@page import="com.sqage.stat.client.StatClientService"%>
<%@page import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@page import="com.sqage.stat.model.DaoJuFlow"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.mail.Mail"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.util.CompoundReturn"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="org.apache.poi.hssf.usermodel.*"%>
<%@page import="org.apache.poi.poifs.filesystem.POIFSFileSystem"%>
<%@page import="java.io.*"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!
	int maxProp = 5;
%>
<%
ip = request.getRemoteAddr();
Object o = session.getAttribute("authorize.username");
if(o!=null){
	recorder = o.toString();
}
	String fileName = request.getRealPath("wangxianjiangli.xls");
	out.print("fileName:"+fileName);
	out.print("<BR>");
	String queyrTypeStr = request.getParameter("QueyrType");//查看本服的还是所有服的
	String operatTypeStr = request.getParameter("OperateType");//查看本服的还是所有服的
	QueyrType queyrType = null;
	OperateType operateType = null;
	if (!fileExists(fileName)) {
		out.print("<H1>配置文件不存在 :" + fileName + "</H1>");
		return;
	}
	for (QueyrType type : QueyrType.values()) {
		if (type.name().equals(queyrTypeStr)) {
			queyrType = type;
			break;
		}
	}
	for (OperateType type : OperateType.values()) {
		if (type.name().equals(operatTypeStr)) {
			operateType = type;
			break;
		}
	}
	HashMap<String, List<PlayerPrize>> prizes = null;
	boolean hasSend = false;//是否发了邮件
	try{
		//out.print("queyrType:"+queyrType);
		//out.print("<BR>");
		//out.print("operateType:" +operateType);
		//out.print("<BR>");
		prizes = getPrize(queyrType,fileName); //查询所有数据
		//out.print(">>>>>>>>>>>>>>>>>>"+prizes);
		if (OperateType.send.equals(operateType)) {///是要发送邮件的
			if (QueyrType.singleServer.equals(queyrType)) {
				if (prizes != null && prizes.size() == 1) {
					{
						//真正发送邮件
						String pwd = request.getParameter("pwd");
						if (!"shenqiwangxian666".equals(pwd)) {
							out.print("<H1>找剑琴发奖励去,没事儿别瞎点,瞎点扣工资</H1>");
							return;
						}
						GameConstants constants = GameConstants.getInstance();
						String localServerName = constants.getServerName();
						List<PlayerPrize> list = prizes.get(localServerName);
						if (list != null && list.size() > 0) {
							for (PlayerPrize pp : list) {
								hasSend = true;
								CompoundReturn cr = pp.sendMail(true,true,request);
								String notice = "<font color=" + (cr.getBooleanValue() ? "green" : "red") + " >[发送邮件"+(cr.getBooleanValue() ? "成功" : "失败")+"]</font>" + cr.getStringValue();
								out.print(notice);
								out.print("<BR/>");
								System.out.println(notice);
							}
						} else {
							out.print("<h1>要发送的列表LIST为空!</h1>");
							out.print("<BR/>");
						}
					}
				} else {
					out.print("<h1>要发送的列表MAP为空!</h1>");
					out.print("<BR/>");
				}
			} else {
				out.print("<h1>非法访问!</h1>");
				out.print("<BR/>");
				return;
			}
		}
	} catch (Throwable t) {
		out.print("异常 :" + t.toString());
	}
%>

<%!/**
	 * 传空,表示查看所有服务器的奖励
	 */
	HashMap<String, List<PlayerPrize>> getPrize(QueyrType queyrType,String fileName) throws Throwable{
		HashMap<String, List<PlayerPrize>> map = new HashMap<String, List<PlayerPrize>>();
		GameConstants constants = GameConstants.getInstance();
		String localServerName = constants.getServerName();
		if (!fileExists(fileName)) {
			System.out.println("=====文件不存在=====");
			return null;
		}
		File file = new File(fileName);
		InputStream is = new FileInputStream(file);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);

		HSSFSheet sheet = workbook.getSheetAt(0);
		int maxRow = sheet.getPhysicalNumberOfRows();
		System.out.println("=====最大行数"+maxRow+"=====");
		for (int i = 1 ; i < maxRow ; i++) {
			HSSFRow row = sheet.getRow(i);
			
			String serverName = "";
			String playerName = "";
			long playerId = -1L;
			String[] propNames = new String[maxProp];
			int[] propColors = new int[maxProp];
			int[] propNums = new int[maxProp];
			boolean[] propBinds = new boolean[maxProp];

			String mailTitle = "";
			String mailContent = "";
			
			String des =  "";
			playerName = row.getCell(0).getStringCellValue();
			try { 
				playerId = Long.valueOf(row.getCell(1).getStringCellValue());
			} catch (Exception e) {
				playerId = (long) row.getCell(1).getNumericCellValue();
			}
			serverName = row.getCell(2).getStringCellValue();
			
			if (QueyrType.singleServer.equals(queyrType) && !serverName.equals(localServerName) ) {
				continue;
			}
			
			for (int k = 0; k < maxProp;k++) {
				HSSFCell cell = row.getCell(3+k);
				if (cell != null) {
					String cellValue = cell.getStringCellValue();
					if (cellValue != null && !"".equals(cellValue.trim())) {
					String []  values = StringTool.string2Array(cellValue,",",String.class);
					if (values == null || values.length != 4) {
						System.out.println("[" +fileName + "]物品录入错误(长度不符 )[cellValue:" + cellValue+"]");
						throw new IllegalStateException("录入错误,物品录入错误(长度不符 )[cellValue:" + cellValue+"]");
					}
					propNames[k] = values[0].trim();
					propNums[k] = Integer.valueOf(values[1].trim());
					propColors[k] = Integer.valueOf(values[2].trim());
					propBinds[k] = "true".equalsIgnoreCase(values[3].trim());
					}
				}
			}
			mailTitle = row.getCell(maxProp + 3).getStringCellValue();
			mailContent = row.getCell(maxProp + 4).getStringCellValue();
			HSSFCell cell = row.getCell(maxProp + 5);
			if (cell != null) {
				des = cell.getStringCellValue();
			}
			PlayerPrize playerPrize = new PlayerPrize(serverName, playerName, playerId, propNames, propColors, propNums, propBinds, mailTitle, mailContent, des );
			if (!map.containsKey(playerPrize.serverName)) {
				map.put(playerPrize.serverName,new ArrayList<PlayerPrize>());
			}
			map.get(playerPrize.serverName).add(playerPrize);

			System.out.println("给服务器["+playerPrize.serverName+"]增加["+playerPrize.playerName+"] [map.size="+map.size()+"]");
		}
		return map;
	}
	boolean fileExists(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}
%>
	
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>邮件奖励</title>
</head>
<body>
图例:<BR/>
绑定:<span style="font-family: webdings;font-weight: bolder; color: green;">@</span>
不绑定:<span style="font-family: webdings;font-weight: bolder; color: red;">`</span>
<BR/>
	<%
		if (queyrType == null) {
	%>
		<form name="f1" action="./sendPrizeMail.jsp" method="post">
			<input type="hidden" name="QueyrType" value="<%=QueyrType.singleServer.name()%>">
			<input type="hidden" name="OperateType" value="<%=OperateType.see.name()%>">
			<input type="submit" value="查看本服务数据">
		</form>
		<br/>
		<form name="f2" action="./sendPrizeMail.jsp" method="post">
			<input type="hidden" name="QueyrType" value="<%=QueyrType.allServer.name() %>">
			<input type="hidden" name="OperateType" value="<%=OperateType.see.name() %>">
			<input type="submit" value="查看 所有服务数据">
		</form>
	<%
		} else {
	%>
		<!-- 展现 -->
		<table style="font-size: 12px;">
		<%
			if (prizes != null) {
				boolean allCheckSuccess = true;
				for(Iterator<String> itor = prizes.keySet().iterator();itor.hasNext();) {
					String serverName = itor.next();
					List<PlayerPrize> list = prizes.get(serverName);
					if (list == null || list.size() == 0) {
						continue;
					}
			%>
				<tr style="background-color: #578CDD;">
					<td>状态</td>
					<td>服务器</td>
					<td>角色名</td>
					<td>角色ID</td>
					<%for (int k = 0; k < maxProp; k++) { %>
						<td>物品<%=k+1 %></td>
					<%} %>
					<td>邮件标题</td>
					<td>邮件内容</td>
					<td>备注 </td>
				</tr>
				<%
					for (int i = 0; i < list.size();i++ ) {
						PlayerPrize playerPrize = list.get(i);
						CompoundReturn cr = null; 
						try {
							cr = playerPrize.sendMail(false,QueyrType.singleServer.equals(queyrType),request);
						} catch (Throwable e) {
							e.printStackTrace();
							out.print(e.toString());
							out.print("<BR/>");
						}
						if (cr == null) {
							continue;
						}
						if (!cr.getBooleanValue()) {
							allCheckSuccess = false;
							out.print(cr.getStringValue());
							out.print("<BR/>");
							
							System.out.println(cr.getStringValue());
						}
				%>
					<tr style="color: <%=cr.getBooleanValue() ? "green" : "red" %>" title="<%=cr.getStringValue()%>">
						<td style="font-family: webdings"><%=cr.getBooleanValue()? "a":"r" %></td>
						<td><%=playerPrize.serverName %></td>
						<td><%=playerPrize.playerName %></td>
						<td><%=playerPrize.playerId %></td>
						<% for(int m = 0; m < maxProp; m++){
							String prop = playerPrize.propNames[m];
						%>
								<% if(prop == null || "".equals(prop)) {
								%>
							<td><%=m %></td>
								<%
								 } else {
									 //TODO 颜色和绑定图标
								 %>
									<td title="物品:<%=prop%>,数量:<%=playerPrize.propNums[m]%>,颜色:<%=playerPrize.propColors[m]%>,绑定:<%=playerPrize.propBinds[m]%>">
										<span style="font-family: webdings;color: <%=playerPrize.propBinds[m]?"green":"red"%>"><%=playerPrize.propBinds[m]? "@" : "`" %></span><%=prop + "*" + playerPrize.propNums[m] %> 
									</td>
								 <%} %>
						<%} %>
						<td><%=playerPrize.mailTitle %></td>
						<td><%=playerPrize.mailContent %></td>
						<td><%=playerPrize.des %></td>
					</tr>
				<%
					}
				%>
					<tr>
						<td colspan="12"></td>
					</tr>
		<% } %>
		</table>
		
		<form name="f4" action="./sendPrizeMail.jsp" method="post">
		<% if (queyrType.equals(QueyrType.singleServer)){ %>
			<input type="hidden" name="OperateType" value="<%=OperateType.send.name() %>">
			<input type="hidden" name="QueyrType" value="<%=QueyrType.singleServer.name()%>">	
			输入密码 :<input type="password" name="pwd" size="15">
			<input type="submit" value="发送邮件" <%=!hasSend && allCheckSuccess && prizes.size() > 0 ? "" : "disabled"%>>
		<% } %>		
		</form>
	<%
		} else {
			out.print("<H1>没有任何配置数据</H1>");
		}
	%>
<%} %>
</body>
</html>
<%!
String recorder = "";
String ip = "";
class PlayerPrize {

	String serverName;
	String playerName;
	long playerId;
	String[] propNames = new String[maxProp];
	int[] propColors = new int[maxProp];
	int[] propNums = new int[maxProp];
	boolean[] propBinds = new boolean[maxProp];

	String mailTitle;
	String mailContent;
	
	String des;

	public PlayerPrize(String serverName, String playerName, long playerId, String[] propNames, int[] propColors, int[] propNums, boolean[] propBinds, String mailTitle, String mailContent,String des ) {
		this.serverName = serverName;
		this.playerName = playerName;
		this.playerId = playerId;
		this.propNames = propNames;
		this.propColors = propColors;
		this.propNums = propNums;
		this.propBinds = propBinds;
		this.mailTitle = mailTitle;
		this.mailContent = mailContent;
		this.des = des;
	}
	CompoundReturn sendMail (boolean realSend,boolean checkPlayer,HttpServletRequest request) throws Throwable{
		CompoundReturn cr = CompoundReturn.createCompoundReturn();
		Player p = null;
		if (checkPlayer) {
			try {
				p = GamePlayerManager.getInstance().getPlayer(playerId);
			} catch (Exception e) {
				
			}
			if (p == null) {
				return cr.setBooleanValue(false).setStringValue("[角色不存在] [playerId:" + playerId + "] [名字:" + playerName + "]");
			}
			if (!p.getName().equals(playerName)) {
				return cr.setBooleanValue(false).setStringValue("[角色名称与录入名称不符] [playerId:" + playerId + "] [名字:" + playerName + "] [通过id获得名字:" + p.getName() + "]");
			}
		}
		boolean checkArticleSuccess = true;// 物品校验
		StringBuffer sbf = new StringBuffer("[含有不存在的物品,请检查配置]\n");
		Article [] articles = new Article[maxProp];
		
		for (int i = 0; i < propNames.length; i++) {
			String propName = propNames[i];
			if (propName != null && !"".equals(propName)) {
				Article article = ArticleManager.getInstance().getArticle(propName);
				if (article == null) {
					checkArticleSuccess = false;
					sbf.append("[").append(propName).append("]不存在\n");
				} else {
					articles [i] = article;
				}
			}
		}
		if (!checkArticleSuccess) {
			return cr.setBooleanValue(false).setStringValue(sbf.toString());
		}
		if (!realSend) {
			return cr.setBooleanValue(true).setStringValue("[校验通过]");
		} else {//真正发邮件
			StringBuffer ss = new StringBuffer();
			ArticleEntity [] aes = new ArticleEntity[maxProp];
			List<ArticleEntity> aeList = new ArrayList<ArticleEntity>();
			List<Integer> numList = new ArrayList<Integer>();
			for (int i= 0; i < articles.length; i++) {
				Article article = articles[i];
				if (article != null) {
					//articlenames[i] = article.getName();
					ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article,propBinds[i],ArticleEntityManager.运营发放活动奖励,p,propColors[i],propNums[i],true);
					aeList.add(ae);
					numList.add(propNums[i]);
					ss.append("<br/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[物品:" + ae.getArticleName() + "] [id:" + ae.getId() + "] [颜色:" + ae.getColorType() + "] [绑定:" + propBinds[i] + "] [数量:" + propNums[i] + "]");
				}
			}
			long mailId = -1;
			int [] numArr = new int [numList.size()];
			for (int kk = 0; kk < numList.size(); kk++) {
				numArr[kk] = numList.get(kk);
			}
			try {
				mailId = MailManager.getInstance().sendMail(playerId,aeList.toArray(new ArticleEntity[0]),numArr,mailTitle,mailContent,0L,0L,0L,"运营发送活动物品",MailSendType.后台发送,p.getName(),ip,recorder);
				{ 
					Player player = GamePlayerManager.getInstance().getPlayer(playerId);
					//发送到统计平台
					for (int i = 0; i < aeList.size(); i++) {
						ArticleEntity ae = aeList.get(i);
						int num = numArr[i];
						DaoJuFlow daoJuFlow = new DaoJuFlow();
						daoJuFlow.setJixing(request.getRemoteAddr());
						daoJuFlow.setCreateDate(System.currentTimeMillis());
						daoJuFlow.setDanJia(0L);// 购买道具的单价
						daoJuFlow.setDaoJuName(ae.getArticleName());
						daoJuFlow.setDaoJuNum((long)num);// 购买道具的数量
	
						daoJuFlow.setFenQu(GameConstants.getInstance().getServerName());
	
						daoJuFlow.setGetType("后台生成-sendPrizeMail");
						daoJuFlow.setHuoBiType("");
	
						daoJuFlow.setGameLevel(String.valueOf(request.getSession().getAttribute("authorize.username")));
						daoJuFlow.setUserName(player.getName() + "/" + player.getId());
	
						daoJuFlow.setBindType(String.valueOf(ae.isBinded()));
						daoJuFlow.setDaoJuColor(String.valueOf(ae.getColorType()));
						daoJuFlow.setDaoJuLevel("0");
	
						daoJuFlow.setPosition("后台发送");// 购买道具的位置，比如商店购买
						if (!TestServerConfigManager.isTestServer()) {
							StatClientService statClientService = StatClientService.getInstance();
							statClientService.sendDaoJuFlow("", daoJuFlow);
							ArticleManager.logger.error("发送统计数据:" + daoJuFlow.toString());
						}
					}
				}
				//数据统计
				/*String ip = request.getRemoteAddr();
				Object o = session.getAttribute("authorize.username");
				if(o!=null){
					String recorder = o.toString();
					RecordEvent mailevent =  new MailEvent(mailTitle,mailContent,articlenames,numArr,0,p.getName(),ip,recorder);
					EventManager.getInstance().eventAdd(mailevent);
				}*/
			} catch (Exception e){
				e.printStackTrace();
				return cr.setBooleanValue(false).setStringValue("[发送邮件失败] [异常:"+e.toString()+"]");
			}
			
			return cr.setBooleanValue(true).setStringValue("[playerId:" + playerId + "] [名字:" + playerName + "] [发送成功] [邮件ID:" + mailId + "] <BR/> ["+ss.toString()+"<br/>]");
		}
	}
}
enum OperateType {
	send, //发送
	see;//查看
}
enum QueyrType {
	singleServer, //单服(本服)
	allServer;//所有服务
}%>