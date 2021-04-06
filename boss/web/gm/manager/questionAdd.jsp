<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.xuanzhi.tools.text.ChineseUtil"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.boss.gm.gmuser.QuestionQuery"%>
<%@page import="com.fy.boss.gm.gmuser.server.TransferQuestionManager"%>
<%@page import="com.fy.boss.gm.XmlServer"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.gm.XmlServerManager"%>
<%@page import="com.fy.boss.gm.gmuser.TransferQuestion"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="../../getpostdata.jsp" %>
<link type="text/css" rel="stylesheet" href="../css/style.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加新问题</title>
<script type="text/javascript">
	function changeType2(tname){
		var str = "";
		var questionmoule = ""
		if(tname=="运行问题"){
			 questionmoule = "手机型号：\n使用的设备标识符：\n客户端下载渠道：\n客户端版本号：\n具体提示：\n玩家姓名：\n联系方式：";
			str = "<select name='erjiselect'><option>无法登陆</option><option>运行报错</option><option>其他问题</option></select>";
		}else if(tname=="玩家建议"){
			str = "<select name='erjiselect'><option>游戏活动建议</option><option>规则建议</option><option>其他建议</option></select>";
			questionmoule = "建议内容：\n详细问题描述：\n玩家姓名：\n联系方式：";
		}else if(tname=="数据异常"){
			questionmoule = "出现异常的功能（宠物信息：装备信息：活动名称：任务名称：）：\n异常的具体表现：\n出现时间：\n人物经验减少（正常经验数：非正常经验数：丢失经验数：）\n交易类（交易对方角色名称：交易金额：交易物品信息：）\n详细问题描述：\n玩家姓名：\n联系方式：";
			str = "<select name='erjiselect'><option>功能异常</option><option>人物异常</option><option>宠物异常</option><option>坐骑异常</option><option>装备异常</option><option>活动异常</option><option>任务异常</option><option>交易类异常</option><option>其他异常</option></select>";
		}else if(tname=="充值问题"){
			str = "<select name='erjiselect'><option>充值未到账</option><option>渠道充值问题</option><option>充值异常</option><option>充值活动问题</option><option>其他问题</option></select>";
			questionmoule = "充值时间：\n充值渠道：\n订单号后6位数字：\n充值卡类型：\n充值卡号：\n充值卡面额：\n充值面额：\n充值次数：\n详细问题描述：\n玩家姓名：\n联系方式：";
		}else if(tname=="帐号问题"){
			questionmoule = "密保问题：\n密保答案：\n注册账号时的手机型号：\n手机设备号（仓库/家族/宗派无需提供此项）：\n玩家姓名：\n联系方式（申请解封必须与申请封停时拨打的电话号码一致）：";
			str = "<select name='erjiselect'><option>密码找回</option><option>帐号纠纷</option><option>帐号找回</option><option>修改密保</option><option>申请封停</option><option>申请解封</option><option>停封申述</option><option>账号禁言</option><option>帐号封停</option></select>";
		}else if(tname=="丢失问题"){
			questionmoule = "上一次看到物品（银两）的时间：\n上一次看到物品（银两）的数量：\n发现丢失的时间：\n丢失物品（银两数量）的名称、数量、品质（颜色）：\n邮件丢失（收件人角色名称：发送时间：发送物品名称：）\n详细问题描述：玩家姓名：联系方式：";
			str = "<select name='erjiselect'><option>丢弃问题</option><option>邮件丢失</option><option>银锭丢失</option><option>物品丢失</option><option>其他丢失</option></select>";
		}else if(tname=="游戏问题"){
			questionmoule = "请详细说明玩家询问的问题";
			str = "<select name='erjiselect'><option>游戏内容问题</option><option>游戏功能咨询</option><option>游戏道具咨询</option><option>游戏活动咨询</option><option>游戏任务咨询</option><option>其他咨询问题</option></select>";
		}else if(tname=="其他问题"){
			questionmoule = "出现问题的时间：\n是否可以重现：\n所在地图：\n具体操作：\n详细问题描述：\n玩家姓名：\n联系方式：";
			str = "<select name='erjiselect'><option>提交BUG</option><option>玩家举报</option><option>投诉</option><option>其他</option></select>";
		}else if(tname=="--"){
			questionmoule = "请描述清楚~";
			str = "<select name='erjiselect'><option>--</option></select>";
		}
		
// 		questionmoule = questionmoule.replace('<br/>','\n');
		
		document.getElementById('erjiwenti').innerHTML = str;
		document.getElementById('questionask').innerHTML = questionmoule;
	}
</script>
</head>
<body bgcolor="#c8edcc">
<!-- <h1>问题提交</h1> -->
<form method="post">
	<table>
	<%
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());	
		
		String realname = request.getParameter("realname");
		String telephone = request.getParameter("telephone");
		String recordtime = request.getParameter("recordtime");
		String email = request.getParameter("email");
		String telephone2 = request.getParameter("telephone2");
		String gamename = request.getParameter("gamename");
		String username = request.getParameter("username");
		String qudao = request.getParameter("qudao");
		String playername = request.getParameter("playername");
		String versionnum = request.getParameter("versionnum");
		String relnum = request.getParameter("relnum");
		String servername = request.getParameter("servername");
		String handler = request.getParameter("handler");
		String handledoor = request.getParameter("handledoor");
		String handletodoor = request.getParameter("handletodoor");
		String questionask = request.getParameter("questionask");
		String yijiselect = request.getParameter("yijiselect");
		String erjiselect = request.getParameter("erjiselect");
		String viplevel = request.getParameter("viplevel");
		if(username!=null&&!"".equals(username)&&!"--".equals(servername)&&!yijiselect.equals("--")&&questionask!=null&&!"".equals(questionask.trim())&&telephone!=null&&!"".equals(telephone.trim())&&handletodoor!=null&&!"".equals(handletodoor.trim())){
			TransferQuestion question = new TransferQuestion();
			question.setRealName(realname);
			question.setTelephone(telephone);
			//这个和页面显示的时间不一致，为了防止一直在处理问题页面，不退出的现象
			question.setRecordTime(time);
			question.setMail(email);
			question.setTelephone2(telephone2);
			question.setGameName(gamename);
			question.setUsername(username);
			question.setGameQuDao(qudao);
			question.setPlayerName(playername);
			question.setVarsionNum(versionnum);
			question.setResVersionNum(relnum);
			question.setServerName(servername);			
			question.setHandler(handler);
			question.setCurrHadler("");
			question.setHandlBuMeng(handledoor);
			question.setHandlOtherBuMeng(handletodoor);
			question.setQuestionMess(questionask);
			question.setQuestionType1(yijiselect);
			question.setQuestionType2(erjiselect);
			question.setViplevel(viplevel);
			question.setIsNewQuestion("new");
			//
			//
			TransferQuestionManager tqm = TransferQuestionManager.getInstance();
			if(tqm.addQuestion(question)){
				out.print("<font color='red' size='5'>添加成功!!</font>");
			}else{
				out.print("<font color='red' size='5'>添加失败!!</font>");
			}
		}else{
			out.print("<font color='red' size='5'>必填项不能为空!!</font>");
		}
		Object obj = session.getAttribute("authorize.username");
	%>
	
	<%!
		enum Type1 {
			运行问题("无法登陆","运行报错","其他问题"),
			玩家建议("游戏活动建议","规则建议","其他建议"),
			数据异常("功能异常","人物异常","宠物异常","坐骑异常","装备异常","活动异常","任务异常","交易类异常","其他异常"),
			充值问题 ("充值未到账","充值异常","渠道充值问题","充值活动问题","其他充值问题"),
			帐号问题("密码找回","帐号纠纷","帐号找回","修改密保","申请封停","申请解封","封停申诉","账号禁言","帐号封停"),
			丢失问题("丢弃问题","邮件丢失","银锭丢失","物品丢失"),
			游戏问题("游戏内容问题","游戏功能咨询","游戏道具咨询","游戏活动咨询","游戏任务咨询","其他咨询问题"),
			其他问题 ("提交BUG","玩家举报","投诉","其他");
			private String []typename;
			Type1(String...type2name){
				this.typename = type2name;
			}
		}
	
		enum Config{
			技术部门("wangtianxin","wangyongquan","lvfei","lihuiqin","chudaping","wangyugang","liuxun"),
			页面部门("wangsiyu","panju","wanyu","panzhichao",
	 				"gaopeng","wangwei","yuyang","lijinlong","yangjinlong","qichongyang","xukuiling","liuyaoqiang",
	 				"quyunjun","liuzhaodong","taoshen","yangxiaoliang","liyang","lihao","ximeng","hanqirui","caomengjie",
	 				"bianhaibo","niezhigang","chenhongyuan","sunmenglong","changhao",
	 				"yaotengfei","luotao","yangjie","goumeng","liwei","jiaohailiang","jiazhiyu","tanghaiping","gepeng"
	 				,"shangwei","yangboxiong","dengshaoyu","limengting","wanglong","dongjunhao","liukai","chenming","liuyu","zhangjunchen","yuanyonghong","chentao","wenguosi","chenxiong","fanlei","guojing","新人01","新人02","新人03","新人04","新人05","wangbo","chengxiao","zhangzhang","wangwenbo","wenshiliang","lisongtao","wangshuo","suli","fulianjie","caozixiang","新人06","新人07","新人08","新人09","新人10"),
			运营部门("huangqing","zhangjianqin","吉雅泰"),
			数据部门("liuqing","liyan01","zhangjian","wuyunpeng","liuchuan","fangqiang","wangyuxiang","wangzhilei","makang","yaoyuan","sufang","chenhao","liushaohua","xiaoyang"),
			米佳("mijia"),
			刘洋("liuyanga"),
			VIP部门("liuyanga","liutao","liuhe","yaoyuhui","wangshuai","zhouwenyu","zhanglei","xuchang","hujingjing","liujian","jiangnan","wuquancai","jingtailong"),
			呼叫部门("niuqi","nijia","yangjing","lifei","zhaohuijuan","liuhongxue","liyan","lipan","chenjian","lihui","qishuo","suncuiping","liaochangchun","wangyao","wangyao","lizhengzheng","wangchen","lixiaolong","liuyuqing","hanzhaojing","lizhaobo","mengyangqing","yangzhao","wangyanan","fuhongwei","dingyixin","qizhiwei","zhangjitong","qizhiwei","chenmingyu","peishengran","cuixiaomeng","nizhenning","zhaopengyu","zhuyina","liushengyue","zhouying","zhurui","zhenganqi","wangbin");
			private String [] name;
			Config(String...type2name){
				this.name = type2name;
			}
			
		}
		
	%>
		<% 
			if(obj!=null){
		%>
		<tr><td colspan="4" bgcolor="#CAE811"><B><font size="4">递交个人资料(电话客服)</font></B></td></tr>
		<tr><th>姓名：</th><td><input type="text" name="realname" value="" /></td><th>联系电话<font color="red">（必填）：</th><td><input type="text" name="telephone" value=""/></td></tr>
		<tr><th>记录时间：</th><td><%=time %></td><input type="hidden" name="recordtime" value="<%=time %>"/><th>联系邮箱:</th><td><input type="text" name="email" value=""/></td></tr>
		<tr><th>其他联系方式：</th><td colspan="3"><input type="text" value="" name="telephone2" size="50"/></td></tr>
		<tr><td colspan="4" bgcolor="#CAE811"><B><font size="4">游戏基本信息</font></B></td></tr>
		<tr><th>游戏名称</th><td><select name='gamename'><option>飘渺寻仙曲</option>
		<option>三国</option><option>潜龙</option><option>西游</option></select></td><th>游戏渠道:</th><td><input type='text' name='qudao' value=''/></td></tr>
		<tr><th>游戏账号：<font color="red">（必填）：</font></th><td><input type="text" name="username" value="" /></td><th>角色名：</th><td><input type="text" name="playername" value=""/></td></tr>
		<tr><th>版本号：</th><td><input type='text' value='' name='versionnum'/></td><th>资源版本号：</th><td><input type='text' value='' name='relnum'/></td></tr>
		<tr><th>一级问题分类<font color="red">（必选）：</font></th><td>
		<select onchange="changeType2(this.value)" name="yijiselect">
			<%
				out.print("<option>--</option>");
				for(Type1 type:Type1.values()){
					out.print("<option>"+type+"</option>");
				}
			%>		
		</select>
		</td><th>二级问题分类<font color="red">（必选）：</font></th><td id='erjiwenti'>
			<select name='erjiselect'><option>--</option></select>		
		</td></tr>	
		<tr><th>服务器：<font color="red">（必选）：</font></th><td><select name='servername'>
		<%
		XmlServerManager smanager = XmlServerManager.getInstance();
		String currbumen = "";
		List<XmlServer> totalservers = smanager.getServers();
			out.print("<option>--</option>");
			ChineseUtil util = new ChineseUtil();
			util.setDicFile("/home/boss/resin_boss_test/webapps/game_boss/gm/manager/chinese4utf8.txt");
			util.initialize();
			Map<String,String> map = new HashMap<String,String>();
			String [] quanpin = new String[totalservers.size()];
			for (int i=0; i<totalservers.size();i++) {
				quanpin[i] = util.getFullPin(totalservers.get(i).getDescription());
				map.put(util.getFullPin(totalservers.get(i).getDescription()),totalservers.get(i).getDescription());
			}
			
			Arrays.sort(quanpin);
			
			for(String ss:quanpin){
				if (ss!=null) {
					out.print("<option>" + map.get(ss) + "</option>");
				}
			}
			
		%>
		</select></td><th>VIP等级：</th><td><input type='text' name='viplevel'></td></tr>
		<tr><td colspan="4" bgcolor="#CAE81A"><B><font size="4">递交部门信息</font></B></td></tr>
	
		<tr><th>当前登录人：</th><td><%=obj.toString() %><input type="hidden" name='handler' value='<%=obj.toString() %>'></td><th>当前所在部门：</th><td>
		<%
			for(Config conf1:Config.values()){
				for(String name:conf1.name){
					if(obj.toString().equals(name)){
						out.print(conf1.name().toString());
						currbumen = conf1.name().toString();
		%>
						<input type="hidden" name='handledoor' value='<%=currbumen %>'>	
		<%				
					}
				}
			}
		%>
		
		</td></tr>
		<tr><th>递交至部门：</th><td>
		<%
			//一线客服和电话客服只能递交数据部门，数据部门可以递交技术部门和运营部门，技术部门能递交给运营部门和数据部门，
			//运营部门能递交给技术部门和数据部门，最终回复玩家是数据部门去做
		if(!"".equals(currbumen)){
// 			if(currbumen.equals("页面部门")){
// 				out.print("<select name='handletodoor'><option>数据部门</option></select>");
// 			}else if(currbumen.equals("数据部门")){
// 				out.print("<select name='handletodoor'><option>技术部门</option><option>运营部门</option><option>呼叫部门</option><option>页面部门</option></select>");
// 			}else if(currbumen.equals("技术部门")){
// 				out.print("<select name='handletodoor'><option>数据部门</option><option>运营部门</option><option>技术部门</option></select>");
// 			}else if(currbumen.equals("运营部门")){
// 				out.print("<select name='handletodoor'><option>数据部门</option><option>技术部门</option></select>");
// 			}else 
				if(currbumen.equals("呼叫部门")){
					out.print("<select name='handletodoor'><option>数据部门</option></select>");
				}else if(currbumen.equals("数据部门")){
					out.print("<select name='handletodoor'><option>技术部门</option></select>");
				}else if(currbumen.equals("VIP部门")){
					out.print("<select name='handletodoor'><option>数据部门</option></select>");
				}
		}
		
		%>
		</td></tr>
		<tr><th>问题描述：</th><td colspan="3"><textarea name="questionask" id='questionask' cols="70" rows="7">请描述清楚~</textarea></td></tr>
<!-- 		<tr><th>问题描述：</th><td colspan="3" rowspan="5"  name="questionask" id='questionask'>请描述清楚~</td></tr> -->
		<tr><td colspan="4"><input type="submit" value='递交'/><input type='reset' value='清空' /></td></tr>	
		<%}else{
			out.print("请重新登录！");
		} %>
	</table>
	
	
</form>	
</body>
</html>