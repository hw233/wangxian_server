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
<title>添加新问题</title>
<script type='text/javascript' src='/game_boss/dwr/engine.js'></script>
<script type="text/javascript" src="/game_boss/dwr/util.js"></script>
<script type="text/javascript" src='/game_boss/dwr/interface/gmquestion.js'></script>
<script> 

    //取消当前处理人的状态
　　function checkLeave(){ 
		  var id = document.getElementById('updateid').value;
		  gmquestion.setQuestionMess(id);
    } 
    
    //回复玩家到对应的反馈
    function replayPlayer(idd,serve){
    	if(true){
    		alert("暂不开放，请联系相关人员！");
    	}
    	var mess = document.getElementById('replaymessage').value;
    	var gmname = document.getElementById('currloginer').value;
    	if(mess&&gmname){
    		var servername = new Array('谁与争锋','傲视群雄','独步天下','凌霜傲雪','勇者无敌','唯我独尊','侠骨柔肠','独霸一方','仙山云海','月满乾坤','碧海青天','江山如画','鹏程万里','霸气无双','对酒当歌','叱咤风云','雄霸天下','壮气凌云','四海之巅','纵横天下','仙山琼阁','日光峡谷','霹雳霞光','逍遥神殿','仙界至尊','通天浮屠','金蛇圣殿','狂龙魔窟','龙飞凤舞','太极玄天','百花仙谷','幽恋蝶谷','百炼成仙','永安仙城','六道轮回','烟雨青山','游龙引凤','伏魔幻境',
        			'九霄之巅','龙脉之地','傲剑凌云','岚境仙音','霸气乾坤','独战群神','霸气纵横','右道长亭','惊天战神','王者之域',
        			'傲视三界','遨游四海','冲霄云楼','裂风峡谷','凌霄宝殿','斩龙神台','金宫银坊','幽灵山庄','左岸花海','无量净土',
        			'明空梵天','摘星楼台','瀛台仙山','白露横江','东极仙岛','万佛朝宗','七宝莲台','百花深谷','雪域冰城','北冥佛光',
        			'崆峒灵宝','昆仑圣殿','西方灵山','普陀潮音','海天佛国','幽冥山谷','乾元金光','飞瀑龙池','紫阳青峰','金霞玉鼎',
        			'太虚幻境','瑶台仙宫','峨嵋金顶','问天灵台','蓬莱仙阁','鹊桥仙境','福地洞天','黄金海岸','无极冰原','云波鬼岭',
        			'云海冰岚','炼狱绝地','东海龙宫','燃烧圣殿','太华神山','千年古城','飘渺王城','风雪之巅','桃源仙境','玉幡宝刹');
        	
        	var urlname = new Array('http://116.213.204.113:8002','http://116.213.204.112:8002','http://116.213.204.99:8002','http://116.213.204.98:8002','http://116.213.204.132:8001','http://116.213.204.142:8001','http://116.213.204.133:8001','http://116.213.204.141:8001','http://116.213.204.131:8001','http://116.213.204.140:8001','http://116.213.204.130:8001','http://116.213.204.116:8001','http://211.151.127.234:8001','http://117.135.130.25:8801','http://116.213.204.103:8001','http://116.213.204.71:8001','http://116.213.192.232:8001','http://116.213.192.216:8001','http://116.213.204.102:8001','http://116.213.204.115:8001','http://117.135.130.45:8801','http://116.213.192.213:8001','http://116.213.192.230:8001','http://116.213.204.114:8001','http://116.213.204.69:8001','http://116.213.204.101:8001','http://116.213.204.80:8001','http://116.213.204.66:8002','http://116.213.204.113:8001','http://116.213.204.112:8001','http://116.213.204.111:8001','http://116.213.204.81:8001','http://116.213.204.100:8001',
        			'http://116.213.204.99:8001','http://116.213.204.98:8001','http://117.135.149.27:20178','http://116.213.192.227:8001','http://116.213.192.211:8001',
        			'http://116.213.192.214:8001','http://116.213.192.248:8001','http://116.213.192.246:8001','http://116.213.192.243:8001','http://117.135.149.27:20184',
        			'http://211.151.127.232:8001','http://211.151.127.233:8001','http://116.213.204.83:8001','http://211.151.127.231:8001','http://211.151.127.230:8001',
        			'http://211.151.127.229:8001','http://211.151.127.228:8001','http://116.213.204.82:8001','http://116.213.204.68:8001','http://117.135.149.27:20032',
        			'http://116.213.204.81:8003','http://116.213.204.84:8001','http://116.213.204.84:8002','http://116.213.204.72:8001','http://116.213.204.72:8002',
        			'http://116.213.204.70:8001','http://116.213.204.70:8002','http://116.213.204.66:8001','http://116.213.204.67:8001','http://116.213.193.66:8001',
        			'http://116.213.193.72:8001','http://116.213.193.71:8001','http://116.213.193.69:8001','http://116.213.193.70:8001','http://116.213.193.68:8001',
        			'http://116.213.192.247:8001','http://117.135.149.27:20020','http://116.213.193.67:8001','http://116.213.192.247:8002','http://116.213.192.245:8002',
        			'http://117.135.149.27:20014','http://116.213.192.244:8001','http://116.213.192.245:8001','http://116.213.192.242:8002','http://116.213.192.242:8001',
        			'http://117.135.149.27:20026','http://116.213.192.231:8002','http://116.213.192.231:8001','http://116.213.192.215:8002','http://116.213.192.229:8001',
        			'http://116.213.192.228:8001','http://116.213.192.226:8002','http://116.213.192.226:8001','http://116.213.192.215:8001','http://116.213.192.212:8002',
        			'http://116.213.192.212:8001','http://116.213.192.210:8002','http://116.213.192.210:8001','http://116.213.142.181:8001','http://116.213.142.180:8001',
        			'http://116.213.192.197:8001','http://116.213.192.196:8001','http://116.213.192.194:8001','http://116.213.192.195:8001','http://116.213.192.228:8002');
        	
//         	alert("mess:"+mess+"--gmname:"+gmname+"--servername:"+servername.length+"--urlname:"+urlname.length+"--serve:"+serve);
        	var gmnames = new Array("wangyugang","吉雅泰","wangsiyu","zhangjunchen","wenguosi","panju","chentao","wanyu","panzhichao",
    	 				"gaopeng","wangwei","yuyang","lijinlong","makang","yaoyuan","yangjinlong","qichongyang","xukuiling","liuyaoqiang",
    	 				"quyunjun","liuzhaodong","taoshen","yangxiaoliang","liyang","lihao","ximeng","wangjiangang","hanqirui","caomengjie",
    	 				"bianhaibo","niezhigang","chenhongyuan","xiaoyang","yanxiaoqin","nizhenning","mengting","wangtianxin","huangqing","sunmenglong",
    	 				"yaotengfei","lilinfeng","quhaotian","zhangqi","yangjie","goumeng","liwei","jiaohailiang","jiazhiyu","tanghaiping","gepeng"
    	 				,"shangwei","yangboxiong","luotao","dengshaoyu","limengting","wanglong","dongjunhao","liukai","liuyu","zhangjitong","chenmingyu","peishengran","cuixiaomeng","zhaopengyu","yangzhao","chenxiong","chenhao","sufang","zhangjian","liuchuan","wuyunpeng","wangzhilei","zhengtiancheng","geyunfei");
     		var gmnums = new Array("GM01","GM02","GM04","GM05","GM06","GM07","GM08","GM09","GM10","GM11","GM12","GM15","GM16","GM17","GM18",
     				"GM19","GM20","GM21","GM22","GM23","GM25","GM26","GM27","GM29","GM30","GM31","GM32","GM33","GM34","GM35",
     				"GM36","GM37","GM39","GM40","GM99","GM98","GM97","GM96","GM95","GM93","GM41","GM42","GM43","GM44","GM45","GM46","GM47","GM48","GM49","GM50"
     				,"GM52","GM53","GM54","GM55","GM00","GM66","GM61","GM56","GM57","GM58","GM59","GM88","GM93","GM92","GM91","GM89","GM87","GM90","GM62","GM63","GM64","GM65","GM45","GM47","GM73","GM74");
    	 		
        	var serverurl = 0;
        	for(var i=0;i<servername.length;i++){
        		if(serve==servername[i]){
        			serverurl = urlname[i];
        		}
        	}
        	
        	var url = serverurl+"/replayPlayer.jsp";
        	var aa = document.getElementById("replayid");
        	var gmNum = 0;
        	for(var jj=0;jj<gmnames.length;jj++){
        		if(gmname!=null&&gmname==gmnames[jj]){
        			gmNum = gmnums[jj];
        		}
        	}
//         	alert("idd:"+idd+"--url:"+url+"--gmnames:"+gmnames.length+"--gmnums:"+gmnums.length+"--gmNum:"+gmNum+"--gmname:"+gmname);
    		if(aa){
    			aa.parentNode.removeChild(aa);
    			for (var prop in aa) { 
    				delete aa[prop]; 
    			}
    		}
    		var tagHead = document.getElementsByTagName("head")[0];
    		var s = document.createElement("script");
    		s.type = "text/javascript";
    		s.src = url+"?replayid="+idd+'&gmname='+gmNum+'&mess='+mess+"";
    		s.id = "replayid";
    		tagHead.appendChild(s);
    	}else{
    		alert("信息不能为空！");
    	}
    	
		
    }
    
    function replaySucc(mess){
    	if(mess=="yes"){
    		gmquestion.delQuestionById();
    		alert("回复成功,跳转到问题列表?");
    		checkLeave();
    		document.gogo.submit();
    	}
    }
    
    function committ(){
    	var handletodoor = document.getElementById('handletodoor').value;
//     	alert(handletodoor);
		if(handletodoor!="--"){
			document.gogo.submit();
		}else{
			alert("请您选择正确的递交部门！");
		}
    	
    }
</script>
</head>
<body bgcolor="#c8edcc" onbeforeunload="checkLeave()">


		<form method="post" name='gogo' action="questionHandle.jsp">
	<table>
	<%!
		enum Config{
		技术部门("wangtianxin","wangyongquan","lvfei","lihuiqin","chudaping","wangyugang","liuxun"),
		页面部门("wangsiyu","panju","wanyu","panzhichao",
 				"gaopeng","wangwei","yuyang","lijinlong","yangjinlong","qichongyang","xukuiling","liuyaoqiang",
 				"quyunjun","liuzhaodong","taoshen","yangxiaoliang","liyang","lihao","ximeng","hanqirui","caomengjie",
 				"bianhaibo","niezhigang","sunmenglong","changhao",
 				"yaotengfei","luotao","yangjie","goumeng","liwei","jiaohailiang","jiazhiyu","gepeng"
 				,"shangwei","yangboxiong","dengshaoyu","zhengtiancheng","fangqiang","cuixuechen","limengting","wanglong","dongjunhao","liukai","chenming","liuyu","zhangjunchen","chentao","wenguosi","chenxiong","guojing","新人01","新人02","新人03","新人04","新人05","wangbo","chengxiao","zhangzhang","wangwenbo","wenshiliang","lisongtao","wangshuo","suli","fulianjie","新人06","新人07","新人08","新人09","新人10","nijia","lihui","gaoyi","wangzheng","zhoumengfan","zhengtiancheng","geyunfei"),
		运营部门("huangqing","zhangjianqin","吉雅泰"),
		数据部门("liyan01","liuqing","liyan01","zhangjian","wuyunpeng","liuchuan","wangyuxiang","wangzhilei","makang","yaoyuan","sufang","chenhao","liushaohua","xiaoyang"),
		米佳("mijia"),
		刘洋("liuyanga"),
		冰冰("bingbing"),
		张建("zhangjian1"),
		VIP部门("liuyanga","liutao","liuhe","yaoyuhui","fanlei","chenhongyuan","tanghaiping","wangshuai","zhouwenyu","zhanglei","xuchang","hujingjing","liujian","jiangnan","wuquancai","jingtailong"),
		呼叫部门("caozixiang","niuqi","qishuo","liyan","jiangnana","zhaohuijuan","yangjing","lifei","liuhongxue","dingyixin","nijia","zhangtingting","suncuiping","lipan","liaochangchun","wangyao","wangchen","lizhaobo","lixiaolong","lizhengzheng","liuyuqing","hanzhaojing","lizhaobo","mengyangqing","yangzhao","wangyanan","fuhongwei","zhangjitong","chenmingyu","peishengran","qizhiwei","cuixiaomeng","nizhenning","zhaopengyu","zhuyina","liushengyue","zhouying","zhurui","zhenganqi","wangbin");
			private String [] name;
			Config(String...type2name){
				this.name = type2name;
			}
		}
	%>
	<%
// 		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
// 		String time = sdf.format(new Date());	
		Object obj = session.getAttribute("authorize.username");
		String id = request.getParameter("id");
		String idd = request.getParameter("idd");
		String currType = "";
		TransferQuestion question = null;
		out.print("id:"+id+"idd:"+idd);
		if(id!=null&&id.trim().length()>0){
			question = TransferQuestionManager.getInstance().getQuestionById(Long.parseLong(id));
			if(idd==null||idd==""||idd.equals("null")){
				question.setCurrHadler(obj.toString());
			}
			
		}
		if(obj!=null&&question!=null){
	%>
	    
		<tr><td colspan="4" bgcolor="#CAE811"><input type="hidden" id="updateid" name="updateid" value="<%=id%>"/><B><font size="4">个人资料</font></B></td></tr>
		<tr><th>姓名：</th><td>
		<%
			if(question.getRealName()!=null){
				out.print(question.getRealName());
			}else{
				out.print("");
			}
		
		%>
		
		
		</td><th>联系电话：</th>
		<td>
		<%
			if(question.getTelephone()!=null){
				out.print(question.getTelephone());
			}else{
				out.print("");
			}
		
		%>
		
		</td></tr>
		<tr><th>记录时间：</th><td><%=question.getRecordTime() %></td><th>联系邮箱:</th><td>
		<%
			if(question.getMail()!=null){
				out.print(question.getMail());
			}else{
				out.print("");
			}
		%>
		</td></tr>
		<tr><th>其他联系方式：</th><td colspan="3">
		<%
			if(question.getMail()!=null){
				out.print(question.getTelephone2());
			}else{
				out.print("");
			}
		%>
		</td></tr>
		<tr><td colspan="4" bgcolor="#CAE811"><B><font size="4">游戏基本信息</font></B></td></tr>
		<tr><th>游戏名称</th><td><%=question.getGameName() %></td><th>游戏渠道:</th><td><%=question.getGameQuDao() %></td></tr>
		<tr><th>游戏账号：</th><td><%=question.getUsername() %></td><th>角色名：</th><td><%=question.getPlayerName() %></td></tr>
		<tr><th>版本号：</th><td><%=question.getVarsionNum() %></td><th>资源版本号：</th><td><%=question.getResVersionNum() %></td></tr>
		<tr><th>一级问题分类:</th><td><%=question.getQuestionType1() %></td><th>二级问题分类:</th><td><%=question.getQuestionType2() %></td></tr>	
		<tr><th>服务器：</th><td><%=question.getServerName() %></td><th>VIP等级：</th><td><%=question.getViplevel() %></td></tr>
		<tr><td colspan="4" bgcolor="#CAE81A"><B><font size="4">递交部门信息</font></B></td></tr>
	
		<tr><th>当前登录人：</th><td><%=obj.toString() %><input type="hidden" id='currloginer' name='currloginer' value='<%=obj.toString() %>'></td><th>当前所在部门：</th><td>
		<%
		String currbumen = "";
			for(Config conf1:Config.values()){
				for(String name:conf1.name){
					if(obj.toString().equals(name)){
						out.print(conf1.name().toString());
						currType = conf1.name().toString();
						currbumen = currType;
		%>
						<input type="hidden" name='handledoor' value='<%=conf1.name().toString() %>'>	
		<%				
					}
				}
			}
		%>
		
		</td></tr>
		<tr><th>递交人：</th><td><%=question.getHandler() %></td><th>递交至部门：</th><td>
		<%
			//一线客服和电话客服只能递交数据部门，数据部门可以递交技术部门和运营部门，技术部门能递交给运营部门和数据部门，
			//运营部门能递交给技术部门和数据部门，最终回复玩家是数据部门去做
			
			if(!"".equals(currbumen)){
				if(currbumen.equals("页面部门")){
					out.print("<select name='handletodoor' id='handletodoor'><option>--</option><option>数据部门</option><option>页面部门</option><option>已处理</option></select>");
				}else if(currbumen.equals("数据部门")){
					out.print("<select name='handletodoor' id='handletodoor'><option>--</option><option>技术部门</option><option>运营部门</option><option>呼叫部门</option><option>页面部门</option><option>张剑琴</option><option>打回呼叫</option><option>打回VIP</option><option>VIP部门</option><option>打回页面</option><option>张建</option><option>已处理</option></select>");
				}else if(currbumen.equals("技术部门")){
					out.print("<select name='handletodoor' id='handletodoor'><option>--</option><option>数据部门</option><option>运营部门</option><option>技术部门</option><option>李慧琴</option><option>刘洵</option><option>米佳</option><option>王勇全</option><option>吕飞</option><option>刘洋</option><option>王天鑫</option><option>张剑琴</option></select>");
				}else if(currbumen.equals("运营部门")){
					out.print("<select name='handletodoor' id='handletodoor'><option>--</option><option>数据部门</option><option>技术部门</option><option>张剑琴</option><option>冰冰</option><option>已处理</option></select>");
				}else if(currbumen.equals("呼叫部门")){
					out.print("<select name='handletodoor' id='handletodoor'><option>--</option><option>数据部门</option><option>呼叫部门</option><option>页面部门</option><option>已处理</option></select>");
				}else if(currbumen.equals("VIP部门")){
					out.print("<select name='handletodoor' id='handletodoor'><option>--</option><option>数据部门</option><option>已处理</option></select>");
				}else if(currbumen.equals("米佳")){
					out.print("<select name='handletodoor' id='handletodoor'><option>--</option><option>数据部门</option><option>技术部门</option><option>已处理</option></select>");
				}else if(currbumen.equals("刘洋")){
					out.print("<select name='handletodoor' id='handletodoor'><option>--</option><option>运营部门</option><option>李慧琴</option><option>刘洵</option><option>米佳</option><option>王勇全</option><option>吕飞</option><option>刘洋</option><option>王天鑫</option></select>");
				}else if(currbumen.equals("冰冰")){
					out.print("<select name='handletodoor' id='handletodoor'><option>数据部门</option><option>数据部门</option><option>技术部门</option><option>冰冰</option><option>已处理</option></select>");
				}else if(currbumen.equals("张建")){
					out.print("<select name='handletodoor' id='handletodoor'><option>--</option><option>数据部门</option><option>技术部门</option><option>已处理</option></select>");
				}
			}
		
		%>
		
		</td></tr>
		<tr><th>问题描述：</th><td colspan="3" width="20px">
		<%
			if(question.getQuestionMess()!=null){
				String mess = question.getQuestionMess().replaceAll("\n", "<br>");
		%>
				<%=question.getServerName() %>&nbsp;<font color='red'><%=question.getPlayerName()%></font>&nbsp;<%=question.getRecordTime() %><br><hr/><%=mess %>
		<%
			}else{
				out.print("--");
				
			}
		%>
		
		</td></tr>
		<%
			if(idd!=null){
				if(idd.equals("chakan")){
				}
			}else{
				if(currbumen.equals("技术部门")){
					out.print("<tr><th>递交原因：</th><td colspan='3' width='20px'><textarea id='questionreason' name='questionreason' cols='55' rows='4'></textarea></td></tr>");
				}else{
					out.print("<tr><th>递交原因：</th><td colspan='3' width='20px'><textarea id='questionreason' name='questionreason' cols='55' rows='4'></textarea></td></tr>");	
				}
				
			}
		%>
		<%
			if(!"".equals(currType)){
				if(currType.equals("页面部门")){
					out.print("<tr><th>回复玩家：</th><td colspan='3' width='20px'><textarea id='replaymessage' name='replaymessage' cols='50' rows='4'></textarea></td></tr>");
				}
			}
		%>
		
		
		<%
			if(idd!=null){
				if(idd.equals("chakan")){
					out.print("<tr><td colspan='4'><a href='questionHandle.jsp' title='返回问题列表'>返回</a></td></tr>");
				}
			}else{
				if(!"".equals(currType)){
					if(currType.equals("页面部门")){
						out.print("<tr><td colspan='3'><input type='button' onclick='committ()' value='递交'/><input type='reset' value='清空' /></td><td><input type='button' value='回复玩家' onclick=\"replayPlayer('"+question.getFeedbackid()+"','"+question.getServerName()+"');\"/></td></tr>");
					}else{
						out.print("<tr><td colspan='4'><input type='button' onclick='committ()' value='递交'/><input type='reset' value='清空' /></td></tr>");
					}
				}
				
			}
		%>
			
		<%}else{
			out.print("找不到问题，或请重新登录！");
		} %>
	</table>
</form>	

	<table>
		<th>递交人</th><th>递交部门</th><th>目标部门</th><th>递交时间</th><th>递交原因</th>
		<%
			List<QuestionQuery> list = TransferQuestionManager.getInstance().getQueriesByEventId(question.getEventid());
			for(QuestionQuery ss:list){
				%>
					<tr><td><%=ss.getHandleer() %></td><td><%=ss.getHandledoor() %></td><td><%=ss.getHandletodoor() %></td><td><%=ss.getHandletime() %></td><td><%=ss.getQuestionmess() %></td></tr>
				<%
			}
		%>
	</table>


</body>
</html>