<%@page import="java.util.Comparator"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
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
<link type="text/css" rel="stylesheet" href="../css/style.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>问题列表</title>
<script type='text/javascript' src='/game_boss/dwr/engine.js'></script>
<script type="text/javascript" src="/game_boss/dwr/util.js"></script>
<script type="text/javascript" src='/game_boss/dwr/interface/gmquestion.js'></script>

<script type="text/javascript">
	window.onload=function(){
		self.setInterval("newmess()",2000);
		self.setInterval("instermess()",2000);
		self.setInterval("delmess()",2000);
	}
	
	function newmess(){
		gmquestion.getTransferQuestions(show);
	}
	
	function instermess(){
		gmquestion.getTransferQuestions2(insterm);
	}
	
	function delmess(){
		gmquestion.getTransferQuestionsdel(deletetr);
	}
	
	function deletetr(mess){
		if(mess.length>0){
			for(i=0;i<mess.length;i++){
				var ss = mess[i].split("@#$^&*");
				if(ss[0]!=""){
					var isid = document.getElementById(ss[0]+"del");
					if(isid!=null){
						var aaa = document.getElementById('currhandler').value;
						var bbb = aaa.split(",");
						//用来定义新递交目标部门是否是在自己当前部门范围内
						var isself = 0;
						for(var j=0;j<bbb.length;j++){
							if(bbb[j]!=null){
								if(bbb[j]==ss[9]){
									isself =1;
// 									alert("document.getElementById(ss[0]).innerHTML:"+document.getElementById(ss[0]+'other').innerHTML+"--ss[9]:"+ss[9]);
									var otherid = document.getElementById(ss[0]+'other');
									if(otherid){
										document.getElementById(ss[0]+'other').innerHTML = ss[9];
									}
									
								}
							}
							
						}
						if(isself==0){
								isid.parentNode.removeChild(isid);
								document.getElementById("nums").innerHTML = parseInt(document.getElementById("nums").innerHTML) - 1;
						}
					}else{
						var aaa = document.getElementById('currhandler').value;
						var bbb = aaa.split(",");
						for(var j=0;j<bbb.length;j++){
							if(bbb[j]!=null){
								if(bbb[j]==ss[9]){
									innertr(ss);
	 								document.getElementById("nums").innerHTML = parseInt(document.getElementById("nums").innerHTML) + 1;
								}
							}
							
						}
					}
// 						var bbb = document.getElementById('currhandler').value;
// 						if(bbb==ss[9]){
// 							if(isid==null){
// 								innertr(ss);
// 	 								document.getElementById("nums").innerHTML = parseInt(document.getElementById("nums").innerHTML) + 1;
// 							}
// 						}
					
					
				}
			}
		}		
	}
	
	function innertr(ss){
		 var x=document.getElementById('addmess').insertRow(1);
		 x.id = ss[0]+"del";
		 var y=x.insertCell(0);
		 var r=x.insertCell(1);
		 var z=x.insertCell(2);
		 var b=x.insertCell(3);
		 var c=x.insertCell(4);
		 var d=x.insertCell(5);
		 var e=x.insertCell(6);
		 var f=x.insertCell(7);
		 var g=x.insertCell(8);
		 var h=x.insertCell(9);
		 var i=x.insertCell(10);
		 y.innerHTML="<td>"+ss[10]+"</td>";
		 r.innerHTML="<td>"+ss[2]+"</td>";
		 z.innerHTML="<td>"+ss[3]+"</td>";
		 b.innerHTML="<td>"+ss[4]+"</td>";
		 c.innerHTML="<td>"+ss[5]+"</td>";
		 d.innerHTML="<td>"+ss[6]+"</td>";
		 e.innerHTML="<td>"+ss[7]+"</td>";
		 f.innerHTML="<td>"+ss[8]+"</td>";
		 g.innerHTML="<td>"+ss[9]+"</td>";
		 h.innerHTML="<td>"+ss[1]+"</td>";
		 h.id = ss[0];
		 i.innerHTML="<td><a title='我来处理' href='questionLastHandle.jsp?id="+ss[0]+"'>处理</a>||<a title='查看问题' href=questionLastHandle.jsp?idd=chakan&id="+ss[0]+">查看</a></td>";
	}
	
	function opennewwindow(id){
		window.open("questionLastHandle.jsp?id="+id);		
	}
	
	function insterm(messee){
		if(messee.length>0){
			for(i=0;i<messee.length;i++){
				var ss = messee[i].split("@#$^&*");
				if(ss[0]!=""){
					var isid = document.getElementById(ss[0]+"del");
					if(isid==null){
						var bb = document.getElementById('currhandler').value;
						if(bb==ss[9]||"默认所有"==ss[9]){
							innertr(ss);
							document.getElementById("nums").innerHTML = parseInt(document.getElementById("nums").innerHTML) + 1;
						}
					}
				}
			}
		}
	}
	
	function show(messes){
		if(messes.length>0){
			for(i=0;i<messes.length;i++){
				var ss = messes[i].split("@#$^&*");
				if(ss[1]!=""){
					var isid = document.getElementById(ss[0]+"");
					if(isid!=null){
						if(ss[1]=="--"){
							document.getElementById(ss[0]).innerHTML ="null";
						}else{
							document.getElementById(ss[0]).innerHTML ="<font color='red'>"+ss[1]+"处理中...</font>";
						}
					}
					
				}
			}
		}
	}
	
</script>
</head>
<body bgcolor="#c8edcc">
<h1>问题列表</h1>
<%!
	enum Config{
	技术部门("wangtianxin","wangyongquan","lvfei","lihuiqin","chudaping","wangyugang","liuxun","liuyanga"),
	页面部门("wangsiyu","panju","wanyu","panzhichao",
				"gaopeng","wangwei","yuyang","lijinlong","yangjinlong","xukuiling","liuyaoqiang",
				"quyunjun","liuzhaodong","taoshen","yangxiaoliang","liyang","lihao","ximeng","hanqirui","caomengjie",
				"bianhaibo","niezhigang","chenhongyuan","sunmenglong",
				"yaotengfei","yangjie","goumeng","liwei","jiaohailiang","jiazhiyu","tanghaiping","gepeng"
				,"shangwei","lisongtao","yangboxiong","dengshaoyu","limengting","wanglong","dongjunhao","liukai","wangzhilei","liuyu","zhangjunchen","yuanyonghong","chentao","wenguosi","chenxiong","fanlei","guojing","新人01","新人02","新人03","新人04","新人05","wangbo","chengxiao","zhangzhang","wangwenbo","xuchang","wenshiliang","zhouwenyu","liyan","wangshuo","jiangnan","liuhe","zhanglei","lizhaobo"),
	运营部门("huangqing","zhangjianqin","吉雅泰","chihang","qichongyang"),
	数据部门("zhangjian","luotao","liuchuan","makang","wuyunpeng","yaoyuan","sufang","chenhao","liushaohua","xiaoyang"),
	米佳("mijia"),
	刘洋("liuyanga"),
	张剑琴("zhangjianqin"),
	王勇全(new String[]{"黄金海岸","无极冰原","云波鬼岭","云海冰岚","炼狱绝地","东海龙宫","燃烧圣殿","太华神山","玉幡宝刹","太虚幻境","幽冥山谷","凌霄宝殿","霸气乾坤","烟雨青山","昆仑圣殿","仙山琼阁","飘渺王城","通天浮屠","金蛇圣殿","伏魔幻境","叱咤风云"},"wangyongquan"),
	刘洵(new String[]{"马踏千里","风雪之巅","傲剑凌云","岚境仙音","独战群神","霸气纵横","右道长亭","惊天战神","王者之域","傲视三界","遨游四海","冲霄云楼","裂风峡谷","斩龙神台","金宫银坊","幽灵山庄","狂龙魔窟","龙飞凤舞","太极玄天","百花仙谷","龙脉之地","雄霸天下"},"liuxun"),
	王天鑫(new String[]{"龙啸九州","神龙摆尾","独步天下","华山之巅","勇者无敌","唯我独尊","侠骨柔肠","鹏程万里","桃源仙境","左岸花海","无量净土","明空梵天","摘星楼台","瀛台仙山","白露横江","东极仙岛","万佛朝宗","七宝莲台","百花深谷","雪域冰城","北冥佛光","崆峒灵宝","西方灵山","幽恋蝶谷","百炼成仙","永安仙城","六道轮回","九霄之巅","壮气凌云","四海之巅"},"wangtianxin"),
	李慧琴(new String[]{"天下无双","洪荒古殿","江山美人","桃李春风","桃花仙岛","春风得意","鸟语花香","春暖花开","蜂飞蝶舞","金风玉露","九仙揽月","上善若水","万象更新","启天之路","仗剑天涯","踞虎盘龙","华夏战神","九霄龙吟","龙腾虎跃","龙翔凤舞","游云惊龙","柳暗花明","鱼跃龙门","飞龙在天","笑傲江湖","独闯天涯","拨云揽月","谁与争锋","傲视群雄","凌霜傲雪","独霸一方","仙山云海","月满乾坤","碧海青天","江山如画","霸气无双","千年古城","普陀潮音","海天佛国","乾元金光","飞瀑龙池","紫阳青峰","金霞玉鼎","瑶台仙宫","峨嵋金顶","问天灵台","蓬莱仙阁","鹊桥仙境","福地洞天","日光峡谷","霹雳霞光","逍遥神殿","仙界至尊","纵横天下","游龙引凤","对酒当歌"  },"lihuiqin"),
	呼叫部门("hujingjing","wuquancai","wangyao","wangchen","lizhaobo","lixiaolong","lizhengzheng","liuyuqing","hanzhaojing","lizhaobo","mengyangqing","yangzhao","liutao","wangyanan","fuhongwei","zhangjitong","chenmingyu","peishengran","qizhiwei","cuixiaomeng","nizhenning","zhaopengyu","zhuyina","liushengyue","zhouying","zhurui","zhenganqi","wangbin");
		private String [] name;
		private String[] serverNames;
		Config(String...name){
			this.name=name;
		}
		Config( String[] serverNames,String...name){
			this.serverNames = serverNames;
			this.name=name;
		}
	}	


%>
<%
	Object obj = session.getAttribute("authorize.username");
	List<TransferQuestion> list = new ArrayList<TransferQuestion>();
	//权限
	List<String> typeNames = new ArrayList<String>();
	String typeName = "";
	if(obj!=null){
		for(Config name1:Config.values()){
			for(String nn:name1.name){
				if(obj.toString().equals(nn)){
// 					typeName = name1.toString();
					typeNames.add(name1.toString());
				}
			}
		}
	}else{
		out.print("请找相关人添加Gm后台权限!");
	}
	String questionType1 = request.getParameter("questionType1");
	String questionreason = request.getParameter("questionreason");
	String handletodoor = request.getParameter("handletodoor");
	String handledoor = request.getParameter("handledoor");
	String hadler = request.getParameter("hadler");
	String id = request.getParameter("updateid");
	String currloginer = request.getParameter("currloginer");
	
// 	TransferQuestionManager.getInstance().handledate("王勇全");
	
	String replaymessage = request.getParameter("replaymessage");
	if(id!=null&&id.trim().length()>0){
		TransferQuestion question = TransferQuestionManager.getInstance().getQuestionById(Long.parseLong(id));	
		if(questionreason!=null&&questionreason.trim().length()>0){
			question.setQuestionMess(questionreason);
		}
		if(handletodoor!=null&&handletodoor.trim().length()>0){
			if(handletodoor.equals("打回页面")||handletodoor.equals("打回呼叫")){
				question.setBackNum(question.getBackNum()+1);
			}
			question.setHandlOtherBuMeng(handletodoor);
			if(handletodoor.equals("已处理")){
				TransferQuestionManager.getInstance().delQuestionById(question.getId());
			}
// 			out.print("handletodoor:"+handletodoor);
		}
// 		question.setHandlOtherBuMeng(handletodoor);
		question.setIsDelete("yes");
		question.setIsNewQuestion("no");
// 		question.setHandler(obj.toString());
		question.setCommitTime(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());	
		QuestionQuery questionquery =  new QuestionQuery();
		questionquery.setEventid(question.getEventid());
		questionquery.setHandleer(currloginer);
		questionquery.setHandledoor(handledoor);
		questionquery.setHandletime(time);
		questionquery.setHandletodoor(handletodoor);
		
		TransferQuestionManager.getInstance().addRecord(questionquery);
		if(replaymessage!=null&&replaymessage.trim().length()>0){
			questionquery.setQuestionmess(replaymessage);
		}else{
			questionquery.setQuestionmess(questionreason);
		}
		
// 		out.print("questionType1:"+questionType1+"--questionreason:"+questionreason+"--handletodoor:"+handletodoor+"--handledoor:"+handledoor+"--id"+id);
	}
	
	if(questionType1!=null&&questionType1.trim().length()>0){
		list = TransferQuestionManager.getInstance().getQuestionsByYiJiType(questionType1);
	}else{
		if(typeNames.size()>0){
			for(int i=0;i<typeNames.size();i++){
				typeName = typeName+","+typeNames.get(i);
				List<TransferQuestion> types = new ArrayList<TransferQuestion>();
				types = TransferQuestionManager.getInstance().getQuestionsByBuMen2(typeNames.get(i),0);
				list.addAll(types);
			}
		}
	}
// 	out.print("typename:"+typeName);
	
%>

<%!boolean needShowBgcolor(Object obj,String serverName){
	String userName = obj.toString();
	Config config = null;
	out :for (Config temp : Config.values()) {
		if (Config.技术部门 == temp) {
			continue;
		}
		for (String n : temp.name) {
			if (n.equals(userName)) {
				config = temp;
				break out;
			}
		}
	}
	if (config==null) {
		return false;
	} else {
		if (config.serverNames == null) {
			return false;
		}
		for (String sName:config.serverNames) {
			if (sName.equals(serverName)) {
				return true;
			}
		}
	}
	return false;
} %>

<%!
List<TransferQuestion> getCompareQuestions(TransferQuestion[] list){
// 	System.out.print("list.size:"+list.length);
		if(list!=null&&list.length>0){
			try{
				Arrays.sort(list,new Comparator<TransferQuestion>(){
					
					@Override
					public int compare(TransferQuestion o1, TransferQuestion o2) {
// 						int id1 = Integer.parseInt(o1.getEventid());
// 						int id2 = Integer.parseInt(o2.getEventid());
						long id1 = o1.getCommitTime();
						long id2 = o2.getCommitTime();
						if(id1< id2){
							return 1;
						}else if(id1 >id2){
							return -1;
						}
						return 0;
					}
				});
			}catch(Exception e){
				e.printStackTrace();
				System.out.print("异常:"+e);
			}
			
		}
		return Arrays.asList(list);
   }

%>

<form method="post">
	<table id="addmess"><caption>数量：<div id="nums"><%=list.size() %></div></caption>
		<tr><input type="hidden" id='currhandler' value='<%=typeName%>'/><th>事件编号</th><th>游戏名称</th><th>服务器名</th><th>账号</th><th>VIP用户</th><th>一级分类</th><th>二级分类</th><th>提交时间</th><th>问题所在部门</th><th>当前状态</th><th>操作</th></tr>
		<%
			if(list!=null&&list.size()>0){
// 				list = getCompareQuestions(list.toArray(new TransferQuestion[]{}));
				int selfNum = 0;
				for(TransferQuestion qq:list){
					if(qq.getHandlOtherBuMeng().equals("已处理")){
						continue;
					}
					boolean showColor = needShowBgcolor(obj,qq.getServerName());
					if (showColor) {selfNum++;}
					if (qq.getBackNum()>0){
						if(qq.getHandlOtherBuMeng().equals("呼叫部门")||qq.getHandlOtherBuMeng().equals("页面部门")||qq.getHandlOtherBuMeng().equals("数据部门")||qq.getHandlOtherBuMeng().equals("技术部门")){
		%>
							<tr id='<%=qq.getId() %>del' style="background-color: yellow"><td><%=qq.getEventid() %></td><td><%=qq.getGameName() %></td><td><%=qq.getServerName() %></td><td><%=qq.getUsername() %></td><td><%=qq.getViplevel() %></td><td><%=qq.getQuestionType1() %></td><td><%=qq.getQuestionType2() %></td>
							<td><%=qq.getRecordTime() %></td><td id='<%=qq.getId()%>other'><%=qq.getHandlOtherBuMeng() %></td><td id='<%=qq.getId() %>'><%=qq.getCurrHadler() %></td><td><a title="我来处理" href="questionLastHandle.jsp?id=<%=qq.getId() %>">处理</a>||<a title="查看问题" href="questionLastHandle.jsp?idd=chakan&id=<%=qq.getId() %>">查看</a></td></tr>
		<%
		}
					}else{
		%>
						<tr id='<%=qq.getId() %>del' style="background-color: <%=showColor?"#d297cc":""%>"><td><%=qq.getEventid() %></td><td><%=qq.getGameName() %></td><td><%=qq.getServerName() %></td><td><%=qq.getUsername() %></td><td><%=qq.getViplevel() %></td><td><%=qq.getQuestionType1() %></td><td><%=qq.getQuestionType2() %></td>
						<td><%=qq.getRecordTime() %></td><td id='<%=qq.getId()%>other'><%=qq.getHandlOtherBuMeng() %></td><td id='<%=qq.getId() %>'><%=qq.getCurrHadler() %></td><td><a title="我来处理" onclick='opennewwindow("<%=qq.getId() %>")'>处理</a>||<a title="查看问题" href="questionLastHandle.jsp?idd=chakan&id=<%=qq.getId() %>">查看</a></td></tr>
		<%
					}
							
					
				}
				if(selfNum > 0){
					out.print("<h3>待处理个数:"+selfNum+"</h3>");
				}
			}else{
				out.print("没有问题处理~");
			}
		%>
	  
		
	</table>
	
	
</form>	
</body>
</html>