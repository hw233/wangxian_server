<%@page import="com.xuanzhi.confirmation.bean.ConfirmationCode"%>
<%@page import="com.xuanzhi.confirmation.bean.Prize"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.xuanzhi.confirmation.bean.GameActivity"%>
<%@page import="com.xuanzhi.confirmation.codestore.CodeStorer"%>
<%@page import="com.xuanzhi.confirmation.service.server.DataManager"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");%>
<%

	String all = request.getParameter("all");
	List<WeiXinArticle> list = new ArrayList<WeiXinArticle>();
	initList(list);
	List<WeiXinUser> userList = new ArrayList<WeiXinUser>();
	
	buildDatas(list, userList);
%>
<style type="text/css">
<!--

-->
td {
	  text-align:center;
}
</style>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<table style="font-size:12px" border="1"  width="80%">
	<tr style="color:red;">
		<th>物品名</th>
		<th>实际销售</th>
		<th>实际领取</th>
		<th>差值(前-后)</th>
		<th>系统作废</th>
		<th>库存</th>
		<th>录入总数</th>
	</tr>
	
	<% for (WeiXinArticle wa : list) {
		boolean notice = Math.abs(wa.totalSellNum -  wa.codeUseNum) > 10;
		%>
		<tr>
			<td><%=wa.articleName %></td>
			<td><%=wa.totalSellNum %></td>
			<td><%=wa.codeUseNum %></td>
			<td style="background-color:<%=notice?"red":""%>"><%=wa.totalSellNum -  wa.codeUseNum%></td>
			<td><%=wa.sysCancleNum %></td>
			<td><%=(wa.codeTotalNum - wa.sysCancleNum - wa.codeUseNum) %></td>
			<td><%=wa.codeTotalNum %></td>
		</tr>
	<% } %>
</table>
<hr>
<hr>
<hr>
<%if ("true".equals(all)) { %>
	<table style="font-size:12px" border="1" width="80%">
		<tr style="color:red;">
			<th>用户名</th>
			<th>角色名</th>
			<th>领取次数</th>
			<th>领取记录</th>
		</tr>
		<% for (WeiXinUser wxUser : userList) { %>
			<tr>
				<td><%=wxUser.userName %></td>
				<td><%=wxUser.playerName %></td>
				<td><%=wxUser.record.size() %></td>
				<td>
					<%for (ReceiveRecord record : wxUser.record) { 
						out.print(record.toString() + "<BR/>");
					} %>
				</td>
			</tr>
		<% } %>
	</table>
<%} %>
</body>
</html>
<%!class WeiXinArticle {
		/** 物品名称 */
		String articleName;
		/*出售数量*/
		int totalSellNum;
		/* 激活码领取量  */
		int codeUseNum;
		/* 激活码录入量 */
		int codeTotalNum;
		/* 系统取消数量 */
		int sysCancleNum;

		public WeiXinArticle(String articleName, int totalSellNum) {
			this.articleName = articleName;
			this.totalSellNum = totalSellNum;
			this.codeTotalNum = 0;
			this.codeUseNum = 0;
			this.sysCancleNum = 0;
		}
	}

	public WeiXinArticle getWeiXinArticle(GameActivity ga , List<WeiXinArticle> list) {
		for (WeiXinArticle wa : list) {
			if (wa.articleName.equals(ga.getName())) {
				return wa;
			}
		}
		return null;
	}
	/*领取礼包的用户*/
	class WeiXinUser{
		String playerName;
		String userName;
		List<ReceiveRecord> record = new ArrayList<ReceiveRecord>();
		
		public WeiXinUser(String playerName,String userName) {
			this.playerName = playerName;
			this.userName = userName;
		}
		
		public boolean equals(Object obj) {
			WeiXinUser other = (WeiXinUser) obj;
			if (!this.userName.equals(other.userName)) {
				return false;
			}
			if (!this.playerName.equals(other.playerName)) {
				return false;
			}
			return true;
		}
	}

	class ReceiveRecord {
		String articleName;
		long articleNum;
		long activityId;
		long exchangeTime;
		public ReceiveRecord (String articleName,long articleNum,long activityId,long exchangeTime) {
			this.articleName = articleName;
			this.articleNum = articleNum;
			this.activityId = activityId;
			this.exchangeTime = exchangeTime;
		}
		
		public String toString() {
			return "[" + articleName +",数量:" +articleNum + ",活动:" + activityId + "," + sdf.format(new Date(exchangeTime))+ "]";
		}
	}
	
	public void initList(List<WeiXinArticle> list) {
		list.add(new WeiXinArticle("宠物基础天赋包（超级捆）",135));
		list.add(new WeiXinArticle("地灵福瑞包",782));
		list.add(new WeiXinArticle("法宝经验宝匣",717));
		list.add(new WeiXinArticle("法宝经验神匣",14));
		list.add(new WeiXinArticle("福瑞橙酒锦囊",293));
		list.add(new WeiXinArticle("福瑞橙酒小锦囊",1367));
		list.add(new WeiXinArticle("福瑞橙帖锦囊",120));
		list.add(new WeiXinArticle("福瑞蓝酒礼包",673));
		list.add(new WeiXinArticle("福瑞蓝帖礼包",571));
		list.add(new WeiXinArticle("福瑞紫酒礼包",507));
		list.add(new WeiXinArticle("福瑞紫酒小锦囊",953));
		list.add(new WeiXinArticle("福瑞紫帖礼包",237));
		list.add(new WeiXinArticle("福瑞紫帖小锦囊",653));
		list.add(new WeiXinArticle("改名卡",1077));
		list.add(new WeiXinArticle("高级造化丹锦囊",3063));
		list.add(new WeiXinArticle("金法宝重置符",49));
		list.add(new WeiXinArticle("炼星无双锦囊",629));
		list.add(new WeiXinArticle("炼星无双小锦囊",1176));
		list.add(new WeiXinArticle("炼星小锦囊",7184));
		list.add(new WeiXinArticle("灵兽锦囊",5407));
		list.add(new WeiXinArticle("灵兽进阶锦囊",412));
		list.add(new WeiXinArticle("明玉祥瑞锦囊",665));
		list.add(new WeiXinArticle("明玉祥瑞小锦囊",4271));
		list.add(new WeiXinArticle("七彩流光锦囊",141));
		list.add(new WeiXinArticle("七彩流光小锦囊",3773));
		list.add(new WeiXinArticle("启灵丹",51));
		list.add(new WeiXinArticle("器灵经验神匣",35));
		list.add(new WeiXinArticle("千载后天锦囊",269));
		list.add(new WeiXinArticle("千载先天锦囊",226));
		list.add(new WeiXinArticle("千载先天小锦囊",877));
		list.add(new WeiXinArticle("神匣锦囊",216));
		list.add(new WeiXinArticle("神匣珠礼包",125));
		list.add(new WeiXinArticle("神婴石礼包",74));
		list.add(new WeiXinArticle("万年后天礼包",530));
		list.add(new WeiXinArticle("万年先天大礼包",132));
		list.add(new WeiXinArticle("羽化进阶包",510));
		list.add(new WeiXinArticle("羽化无双锦囊",147));
		list.add(new WeiXinArticle("羽炼大礼包",1361));
		list.add(new WeiXinArticle("羽炼礼包",6005));
		list.add(new WeiXinArticle("高级造化丹小锦囊",226));
		list.add(new WeiXinArticle("启智丹",81));
		list.add(new WeiXinArticle("万年先天礼包",524));
		list.add(new WeiXinArticle("深渊器灵礼包",32));
		list.add(new WeiXinArticle("神婴石礼包",74));
		list.add(new WeiXinArticle("千载后天小锦囊",513));
		list.add(new WeiXinArticle("元婴石礼包",203));
		list.add(new WeiXinArticle("器灵经验宝匣",22));
		list.add(new WeiXinArticle("福瑞橙帖小锦囊",352));
		list.add(new WeiXinArticle("逆天元气丹礼包",53));
		list.add(new WeiXinArticle("真神匣锦囊",30));
		list.add(new WeiXinArticle("九级主流宝石锦囊",8));
		list.add(new WeiXinArticle("精华羽化包",700));
	}
	
	
	public void buildDatas(List<WeiXinArticle> list,List<WeiXinUser> userList){
			
		DataManager dataManager = DataManager.getInstance();
		CodeStorer codeStorer = dataManager.getCodeStorer();
		List<GameActivity> activities = codeStorer.getAllActivity();
		for (int i = activities.size() - 1; i >= 0; i--) {
			GameActivity ga = activities.get(i);
			WeiXinArticle wa =  getWeiXinArticle(ga, list);
			if (wa != null) {
				List<String> codeList = ga.getAllCodes();
				wa.codeTotalNum += codeList.size();
				for (String codeStr : codeList) {
					ConfirmationCode code = dataManager.getCodeStorer().getConfirmationCode(codeStr);
					if (!code.isValid()) {
						String playerName = code.getPlayerName();							
						String userName = code.getUserName();
						WeiXinUser tempUser = new WeiXinUser(playerName,userName);
						//find User
						WeiXinUser user = null;
						for (WeiXinUser wxUser : userList) {
							if (wxUser.equals(tempUser)) {
								user = wxUser;
								break;
							}
						}
						if (user == null) {
							userList.add(tempUser);
							user = tempUser;
						}
						if ("后台".equals(code.getChannel())) {
							wa.sysCancleNum += 1;
						} else {
							wa.codeUseNum += 1;
						}
						
						user.record.add(new ReceiveRecord(ga.getName(),1L,ga.getId(),code.getExchangeTime()));
					}
				}
			}
		}
	}
%>