<%@page import="com.fy.engineserver.sprite.NewUserEnterServerService"%>
<%@page import="com.fy.engineserver.enterlimit.EnterLimitManager"%>
<%@page import="com.fy.engineserver.validate.UserAskData"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.validate.ChooseAnswerAskData"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.validate.OtherValidateManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<%


	String userName = (String)session.getAttribute("username");
	if(userName == null){

		String action = request.getParameter("action");
		if(action != null && action.equals("login")){
			String u = request.getParameter("username");
			String p = request.getParameter("password");

			if(u != null && p != null && u.equals("wtx") && p.equals("147258369") ){
				userName = u;
				session.setAttribute("username",userName);
			}else{
				out.println("<h3>用户名密码不对</h3>");
			}
		}

		if(userName == null){
%>
	<center>
		<h1>请先登录</h1>
		<form>
		<input type="hidden" name="action" value="login">
		请输入用户名：<input type="text" name="username" value="" size=30><br/>
		请输入密码：<input type="text" name="password" value="" size=30><br/>
		<input type="submit" value="提  交">
		</form>
	</center>
<%
		return;
		}
	}	
%>

	<%
		String action = request.getParameter("action");
		int seeType = -1;
		UserAskData queryUserAskData = null;
		if (action != null) {
			if (action.equals("openClose")) {
				OtherValidateManager.isOpen = !OtherValidateManager.isOpen;
			}else if (action.equals("seeV")) {
				String seeTs = request.getParameter("seeType");
				if (seeTs != null && seeTs.length() > 0) {
					seeType = Integer.parseInt(seeTs);
				}
			}else if (action.equals("deleteV")) {
				String vType = request.getParameter("vType");
				seeType = Integer.parseInt(vType);
				int Index = Integer.parseInt(request.getParameter("vIndex"));
				if (seeType == OtherValidateManager.VALIDATE_TYPE_PICTURE) {
					OtherValidateManager.getInstance().pictureNames.remove(Index);
				}else if (seeType == OtherValidateManager.VALIDATE_TYPE_ANIMATION) {
					OtherValidateManager.getInstance().animationNames.remove(Index);
				}else if (seeType == OtherValidateManager.VALIDATE_TYPE_PARTICLE) {
					OtherValidateManager.getInstance().particleNames.remove(Index);
				}else if (seeType == OtherValidateManager.VALIDATE_TYPE_WORD) {
					OtherValidateManager.getInstance().wordNames.remove(Index);
				}else if (seeType == OtherValidateManager.VALIDATE_TYPE_CHOOSE_ANSWER) {
					OtherValidateManager.getInstance().chooseWordAsks.remove(Index);
				}
			}else if (action.equals("loadExcel")){
				OtherValidateManager.getInstance().loadFile();
			}else if (action.equals("queryOnePlayer")) {
				String pp = request.getParameter("pID");
				long id = Long.parseLong(pp);
				Player p = PlayerManager.getInstance().getPlayer(id);
				if (p != null) {
					queryUserAskData = OtherValidateManager.getInstance().getUserAskData(p);
				}
			}else if (action.equals("setValidateType")) {
				int type = Integer.parseInt(request.getParameter("type"));
				OtherValidateManager.setValidateType = type;
			}else if (action.equals("isAllRandom")) {
				OtherValidateManager.isAllRandom = !OtherValidateManager.isAllRandom;
			}else if (action.equals("xiugai")) {
				String pIDS = request.getParameter("pID");
				String uNameS = request.getParameter("uName");
				String pNameS = request.getParameter("pName");
				String levelS = request.getParameter("level");
				String aNumS = request.getParameter("aNum");
				String rNumS = request.getParameter("rNum");
				String wNumS = request.getParameter("wNum");
				String vStateS = request.getParameter("vState");
				String pNumS = request.getParameter("pNum");
				String tAnNumS = request.getParameter("tAnNum");
				Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(pIDS));
				if (p != null) {
					queryUserAskData = OtherValidateManager.getInstance().getUserAskData(p);
					String[] ss = aNumS.substring(1, aNumS.length() - 1).split(", ");
					int[] ii = new int[ss.length];
					for (int i = 0; i < ss.length; i++) {
						ii[i] = Integer.parseInt(ss[i]);
					}
					queryUserAskData.setAnswerTimes(ii);
					
					ss = rNumS.substring(1, rNumS.length() - 1).split(", ");
					ii = new int[ss.length];
					for (int i = 0; i < ss.length; i++) {
						ii[i] = Integer.parseInt(ss[i]);
					}
					queryUserAskData.setAnswerRightTimes(ii);
					
					ss = wNumS.substring(1, wNumS.length() - 1).split(", ");
					ii = new int[ss.length];
					for (int i = 0; i < ss.length; i++) {
						ii[i] = Integer.parseInt(ss[i]);
					}
					queryUserAskData.setAnswerWrongTimes(ii);
					
					queryUserAskData.setLastValidateState(Integer.parseInt(vStateS));
					
					queryUserAskData.setDirectPassCount(Integer.parseInt(pNumS));
					
					queryUserAskData.setTodayRihgtTimes(Integer.parseInt(tAnNumS));
				}
			}else if (action.equals("changeMINMAX")) {
				String picMin = request.getParameter("picMin");
				String picMax = request.getParameter("picMax");
				String aniMin = request.getParameter("aniMin");
				String aniMax = request.getParameter("aniMax");
				String parMin = request.getParameter("parMin");
				String parMax = request.getParameter("parMax");
				String wordMin = request.getParameter("wordMin");
				String wordMax = request.getParameter("wordMax");
				OtherValidateManager.getInstance().MIN_PICTURE_NUM = Integer.parseInt(picMin);
				OtherValidateManager.getInstance().MAX_PICTURE_NUM = Integer.parseInt(picMax);
				OtherValidateManager.getInstance().MIN_ANIMATION_NUM = Integer.parseInt(aniMin);
				OtherValidateManager.getInstance().MAX_ANIMATION_NUM = Integer.parseInt(aniMax);
				OtherValidateManager.getInstance().MIN_PARTICLE_NUM = Integer.parseInt(parMin);
				OtherValidateManager.getInstance().MAX_PARTICLE_NUM = Integer.parseInt(parMax);
				OtherValidateManager.getInstance().MIN_WORD_NUM = Integer.parseInt(wordMin);
				OtherValidateManager.getInstance().MAX_WORD_NUM = Integer.parseInt(wordMax);
			}else if (action.equals("setRandom")) {
				String ss = request.getParameter("ask_random");
				String[] ask_randomS = ss.substring(1, ss.length()-1).split(", ");
				int[] askRandom = new int[ask_randomS.length];
				for (int i = 0; i < ask_randomS.length; i++) {
					askRandom[i] = Integer.parseInt(ask_randomS[i]);
				}
				OtherValidateManager.ask_random = askRandom;
			}else if (action.equals("openOther")) {
				String deal = request.getParameter("deal");
				String mail = request.getParameter("mail");
				String pEnter = request.getParameter("pEnter");
				String requestBuy = request.getParameter("requestBuy");
				String task = request.getParameter("task");
				OtherValidateManager.isOpenDeal = Boolean.parseBoolean(deal);
				OtherValidateManager.isOpenMail = Boolean.parseBoolean(mail);
				OtherValidateManager.isOpenPlayerEnter = Boolean.parseBoolean(pEnter);
				OtherValidateManager.isOpenRequestBuy = Boolean.parseBoolean(requestBuy);
				OtherValidateManager.isOpenTask = Boolean.parseBoolean(task);
			}else if (action.equals("chanagerNoVC")) {
				String v = request.getParameter("noVaChannel");
				String[] noVC = v.substring(1, v.length()-1).split(", ");
				ArrayList<String> list = new ArrayList<String>();
				for (int i = 0; i < noVC.length; i++) {
					list.add(noVC[i]);
				}
				OtherValidateManager.noValidateChannel = list;
			}else if (action.equals("isUserOldHandle")) {
			}else if (action.equals("CLIENT_MOVE_SCALE")) {
				OtherValidateManager.CLIENT_MOVE_SCALE = Integer.parseInt(request.getParameter("cMs"));
			}else if (action.equals("isCheckIOS_Server")) {
				EnterLimitManager.isCheckIOS_Server = !EnterLimitManager.isCheckIOS_Server;
			}
		}
	%>
	
	<a href="./otherValidate.jsp">刷新次页面</a>
	
	<br>
	<form>
		答题开关:<%=OtherValidateManager.isOpen ? "开启中" : "关闭中" %>
		<input type="hidden" name="action" value="openClose" />
		<input type="submit" value="确定" />
	</form>
	<br>
	<form>
		握手开关:<%=NewUserEnterServerService.isSendHand1&&NewUserEnterServerService.isSendHand2 ? "旧握手可用" : "只用新握手" %>
		<input type="hidden" name="action" value="isUserOldHandle" />
		<input type="submit" value="确定" />
	</form>
	<br>
	<form>
		IOS服务器校验开关:<%=EnterLimitManager.isCheckIOS_Server ? "检查IOS服" : "未检查" %>
		<input type="hidden" name="action" value="isCheckIOS_Server" />
		<input type="submit" value="确定" />
	</form>
	<br>
	<form>
		答题分别开关开启的:<%=OtherValidateManager.isOpenDeal ? "交易" : "" %>
		<%=OtherValidateManager.isOpenMail ? "邮件" : "" %>
		<%=OtherValidateManager.isOpenPlayerEnter ? "用户登录" : "" %>
		<%=OtherValidateManager.isOpenRequestBuy ? "求购" : "" %>
		<%=OtherValidateManager.isOpenTask ? "任务" : "" %>
		<input type="hidden" name="action" value="openOther" />
		<input type="text" name="deal" value="<%=OtherValidateManager.isOpenDeal %>" />
		<input type="text" name="mail" value="<%=OtherValidateManager.isOpenMail %>" />
		<input type="text" name="pEnter" value="<%=OtherValidateManager.isOpenPlayerEnter %>" />
		<input type="text" name="requestBuy" value="<%=OtherValidateManager.isOpenRequestBuy %>" />
		<input type="text" name="task" value="<%=OtherValidateManager.isOpenTask %>" />
		<input type="submit" value="确定" />
	</form>
	<br>
	<form>
		答题难度，控制控件晃动:<%=OtherValidateManager.CLIENT_MOVE_SCALE %>
		<input type="hidden" name="action" value="CLIENT_MOVE_SCALE" />
		<input type="text" name="cMs" value="<%=OtherValidateManager.CLIENT_MOVE_SCALE %>">
		<input type="submit" value="确定" />
	</form>
	<br>
	<form>
		不用答题的渠道:<%=Arrays.toString(OtherValidateManager.noValidateChannel.toArray()) %>
		<br>
		<input type="hidden" name="action" value="chanagerNoVC" />
		<input size="200" type="text" name="noVaChannel" value="<%=Arrays.toString(OtherValidateManager.noValidateChannel.toArray()) %>">
		<br>
		<input type="submit" value="确定" />
	</form>
	<br>
	<form>
		答题选题模式(这个级别最高):<%=OtherValidateManager.setValidateType >= 0 ? "固定"+OtherValidateManager.setValidateType : "其他模式" %>
		<input type="hidden" name="action" value="setValidateType" />
		<input type="text" name="type">
		<input type="submit" value="确定" />
	</form>
	<br>
	<form>
		答题选题模式(随机5种模式):<%=OtherValidateManager.isAllRandom ? "随机" : "非随机" %>
		<input type="hidden" name="action" value=isAllRandom />
		<input type="submit" value="确定" />
	</form>
	<br>
	<form>
		重新载入excel
		<input type="hidden" name="action" value="loadExcel" />
		<input type="submit" value="确定" />
	</form>
	<br>
	<form>
		修改答题几率
		<input type="hidden" name="action" value="setRandom" />
		<input type="text" name="ask_random" value="<%=Arrays.toString(OtherValidateManager.ask_random) %>">
		<input type="submit" value="确定" />
	</form>
	<br>
	<form>
		查看题目(0图片,1动画,2粒子,3文字,4选择题)
		<input type="hidden" name="action" value="seeV" />
		<input type="text" name="seeType"/>
		<input type="submit" value="确定" />
	</form>
	<br>
	<form>
		一致性题目答案个数:
		<input type="hidden" name="action" value="changeMINMAX" />
		图片最小最大<input type="text" name="picMin" value="<%=OtherValidateManager.getInstance().MIN_PICTURE_NUM %>" />
					<input type="text" name="picMax" value="<%=OtherValidateManager.getInstance().MAX_PICTURE_NUM %>" />
		动画最小最大<input type="text" name="aniMin" value="<%=OtherValidateManager.getInstance().MIN_ANIMATION_NUM %>" />
					<input type="text" name="aniMax" value="<%=OtherValidateManager.getInstance().MAX_ANIMATION_NUM %>" />
		粒子最小最大<input type="text" name="parMin" value="<%=OtherValidateManager.getInstance().MIN_PARTICLE_NUM %>" />
					<input type="text" name="parMax" value="<%=OtherValidateManager.getInstance().MAX_PARTICLE_NUM %>" />
		文字最小最大<input type="text" name="wordMin" value="<%=OtherValidateManager.getInstance().MIN_WORD_NUM %>" />
					<input type="text" name="wordMax" value="<%=OtherValidateManager.getInstance().MAX_WORD_NUM %>" />
		<input type="submit" value="确定" />
	</form>
	<br>
	<%
		if (seeType >= 0) {
			if (seeType == OtherValidateManager.VALIDATE_TYPE_CHOOSE_ANSWER) {
				%>
				<form>
					删除某个题目
					<input type="hidden" name="action" value="deleteV" />
					<input type="hidden" name="vType" value="<%=seeType %>" />
					<input type="text" name="vIndex"/>
					<input type="submit" value="确定" />
				</form>
			<br>
				<%
				out.println("<table border=\"2\">");
				out.println("<tr>");
				out.println("<td>Index</td>");
				out.println("<td>题目</td>");
				out.println("<td>答案</td>");
				out.println("<td>正确答案</td>");
				out.println("</tr>");
				for (int i = 0; i < OtherValidateManager.getInstance().chooseWordAsks.size(); i++) {
					ChooseAnswerAskData data = OtherValidateManager.getInstance().chooseWordAsks.get(i);
					out.println("<tr>");
					out.println("<td>"+i+"</td>");
					out.println("<td>"+data.getAnswerMsg()+"</td>");
					out.println("<td>"+Arrays.toString(data.getAnswers())+"</td>");
					out.println("<td>"+data.getRightAnswerIndex()+"</td>");
					out.println("</tr>");
				}
				out.println("</table>");
			}else {
				ArrayList<String> names = null;
				if (seeType == OtherValidateManager.VALIDATE_TYPE_PICTURE) {
					names = OtherValidateManager.getInstance().pictureNames;
				}else if (seeType == OtherValidateManager.VALIDATE_TYPE_ANIMATION) {
					names = OtherValidateManager.getInstance().animationNames;
				}else if (seeType == OtherValidateManager.VALIDATE_TYPE_PARTICLE) {
					names = OtherValidateManager.getInstance().particleNames;
				}else if (seeType == OtherValidateManager.VALIDATE_TYPE_WORD) {
					names = OtherValidateManager.getInstance().wordNames;
				}
				if (names != null) {
					%>
				<form>
					删除某个题目
					<input type="hidden" name="action" value="deleteV" />
					<input type="hidden" name="vType" value="<%=seeType %>" />
					<input type="text" name="vIndex"/>
					<input type="submit" value="确定" />
				</form>
				<br>
					<%
					out.println("<table border=\"2\">");
					for (int i = 0; i < names.size(); i++) {
						if (i%6 == 0) {
							out.println("<tr>");
						}
						out.println("<td>"+i+"  "+names.get(i)+"</td>");
						if (i%6 == 5) {
							out.println("</tr>");
						}
					}
					out.println("</tr>");
					out.println("</table>");
				}
			}
		}
	%>
	
	<form>
		输入玩家ID查询某个玩家的答题情况
		<input type="hidden" name="action" value="queryOnePlayer" />
		<input type="text" name="pID"/>
		<input type="submit" value="确定" />
	</form>
	<br>
	<%
		if (queryUserAskData != null) {
			out.println("<form>");
			%>
			<form>
				<input type="hidden" name="action" value="xiugai" />
				ID<input type="text" name="pID" value="<%=queryUserAskData.getPid() %>">
				用户名<input type="text" name="uName" value="<%=queryUserAskData.getuName() %>">
				名字<input type="text" name="pName" value="<%=queryUserAskData.getpName() %>">
				等级<input type="text" name="level" value="<%=queryUserAskData.getLevel() %>">
				答题次数<input type="text" name="aNum" value="<%=Arrays.toString(queryUserAskData.getAnswerTimes()) %>">
				正确次数<input type="text" name="rNum" value="<%=Arrays.toString(queryUserAskData.getAnswerRightTimes()) %>">
				错误次数<input type="text" name="wNum" value="<%=Arrays.toString(queryUserAskData.getAnswerWrongTimes()) %>">
				答题状态<input type="text" name="vState" value="<%=queryUserAskData.getLastValidateState() %>">
				直接通过次数<input type="text" name="pNum" value="<%=queryUserAskData.getDirectPassCount() %>">
				今天答题数目<input type="text" name="tAnNum" value="<%=queryUserAskData.getTodayRihgtTimes() %>">
				<input type="submit" value="修改"/>
			</form>
			<%
			out.println("</form>");
		}
	%>
</body>
</html>