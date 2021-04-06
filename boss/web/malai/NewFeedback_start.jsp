<%@page import="com.fy.boss.gm.newfeedback.service.NewFeedbackQueueManager"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="../gm/css/style.css" />
<script type='text/javascript' src='jquery-1.6.2.js'></script>
<title>反馈开始</title>
</head>

<script language="javascript">
	function handle(gmid,gmname){
		if(gmid&&gmname){
			var o = {'gmname':gmid,'isvip':'VIP'};
			$.ajax({
				  type: 'POST',
				  url: "gmcommitsure.jsp",
				  contentType : "application/x-www-form-urlencoded; charset=utf-8",
				  data: o,
				  dataType: "html",
					  error:function(result){alert("error:"+arguments[0]+":"+arguments[1]+":"+arguments[2])},
					  success: function(result)
					  {
						  if(result.indexOf("true")>0){
							  window.location.replace("http://119.81.29.67:9110/mieshi_game_boss/NewFeedback_handle.jsp?handle="+gmid+"&startwork=yes");
						  }else{
							  alert("请组长重新设置编号");
						  }
					  }
				});
		}
	}
	function handle2(gmid,gmname){
		if(gmid&&gmname){
			var o = {'gmname':gmid,'isvip':'--'};
			$.ajax({
				  type: 'POST',
				  url: "gmcommitsure.jsp",
				  contentType : "application/x-www-form-urlencoded; charset=utf-8",
				  data: o,
				  dataType: "html",
					  error:function(result){alert("error:"+arguments[0]+":"+arguments[1]+":"+arguments[2])},
					  success: function(result)
					  {
						  if(result.indexOf("true")>0){
							  window.location.replace("http://119.81.29.67:9110/mieshi_game_boss/NewFeedback_handle2.jsp?handle="+gmid+"&startwork=yes");
						  }else{
							  alert("请组长重新设置编号");
						  }
					  }
				});
		}
	}
</script>

<body bgcolor="#c8edcc">
	<table>
		<%
			Object obj = session.getAttribute("authorize.username");
			String gmid = "";
			Map<String, String> gmNameIds = new HashMap<String, String>();
			String gmname = obj.toString();
			if (obj != null) {
				
				
		 		String [] gmnames = {"klgm01","吉雅泰","chudaping","wangsiyu","zhangjunchen","wenguosi","yuanyonghong","panju","chentao","wanyu","panzhichao",
		 				"gaopeng","wangwei","yuyang","lijinlong","makang","yaoyuan","yangjinlong","qichongyang","xukuiling","liuyaoqiang",
		 				"quyunjun","liuzhaodong","taoshen","yangxiaoliang","liyang","lihao","ximeng","wangjiangang","hanqirui","caomengjie",
		 				"bianhaibo","niezhigang","chenhongyuan","xiaoyang","yanxiaoqin","liutao","nizhenning","mengting","wangtianxin","huangqing","sunmenglong",
		 				"yaotengfei","lilinfeng","quhaotian","zhangqi","yangjie","goumeng","liwei","jiaohailiang","jiazhiyu","tanghaiping","gepeng"
		 				,"shangwei","yangboxiong","luotao","dengshaoyu","limengting","wanglong","dongjunhao","liukai","liuyu","zhangjitong","chenmingyu","peishengran","cuixiaomeng","zhaopengyu","yangzhao","chenxiong","chenhao","sufang","zhangjian","liuchuan","liuyanga","fanlei","liushengyue","zhouying","zhurui","zhenganqi","lihuiqin","guojing","新人01","新人02","新人03","新人04","新人05","chenming",
		 				"wuchun","wuyunpeng","wangbo","chengxiao","lilanlan","wangyongquan","wangzhilei","wangwenbo","zhangzhang","xuchang","wangxin","wangyanan","liwei","wangshuai","wenshiliang","zhouwenyu","changhao","lisongtao"};
		 		String [] gmnums = {"GM01","GM02","GM03","GM04","GM05","GM06","GM07","GM08","GM09","GM10","GM11","GM12","GM15","GM16","GM17","GM18",
		 				"GM19","GM20","GM21","GM22","GM23","GM25","GM26","GM27","GM29","GM30","GM31","GM32","GM33","GM34","GM35",
		 				"GM36","GM37","GM39","GM40","GM99","GM98","GM97","GM96","GM95","GM93","GM41","GM42","GM43","GM44","GM111","GM46","GM47","GM48","GM49","GM50"
		 				,"GM52","GM53","GM54","GM55","GM00","GM66","GM61","GM56","GM57","GM58","GM59","GM88","GM93","GM92","GM91","GM89","GM87","GM90","GM62","GM63","GM64","GM65","GM10000","GM59","GM99","GM96","GM89","GM94","GM999","GM54",
		 				"GM101","GM102","GM103","GM104","GM105","GM48","GM60","GM45","GM57","GM58","GM51","GM991","GM47","GM57","GM55","GM43","GM58","GM91","GM36","GM37","GM32","GM33","GM56","GM57"};
				for (int i = 0; i < gmnames.length; i++) {
					if (gmnames[i] != null && gmnums[i] != null) {
						gmNameIds.put(gmnames[i],gmnums[i]);
					}
				}
				if (gmNameIds.keySet().contains(gmname)) {
					gmid = gmNameIds.get(gmname);
				}
			}else{
				out.print("请重新登录");
			}
			if(gmid.trim().length()>0){
				NewFeedbackQueueManager nq = NewFeedbackQueueManager.getInstance();
				int queussize = nq.getQueue().size();
				int vipquesusize = nq.getVipqueue().size();
				out.print("<h3>"+gmid+" 欢迎您的归来.</h3>");
				out.print("<h4>普通在线排队用户："+queussize);
				out.print("，VIP在线排队用户："+vipquesusize);
				out.print(",一共还有"+(queussize+vipquesusize)+"个问题等待处理.</h4><hr>");
			%>
				<tr><input type='button' onclick='handle2("<%=gmid %>","<%=gmname %>")' value='开始工作(普通通道)'></tr><br><br><br>
				<tr><input type='button' onclick='handle("<%=gmid %>","<%=gmname %>")' value='开始工作(VIP通道)'></tr>
			<%	
				
			}else{
				out.print("<h1>请找相关人员配置GM编号！</hr>");
			}
		%>
		
		
		
	</table>

</body>
</html>

