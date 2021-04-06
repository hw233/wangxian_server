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
<link type="text/css" rel="stylesheet" href="gm/css/style.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>问题列表</title>

<script type="text/javascript">
function opennewwindow(id){
	window.open("questionLastHandle.jsp?id="+id);		
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
				,"shangwei","lisongtao","yangboxiong","dengshaoyu","limengting","wanglong","dongjunhao","liukai","wangzhilei","liuyu","zhangjunchen","yuanyonghong","chentao","wenguosi","chenxiong","fanlei","guojing","新人01","新人02","新人03","新人04","新人05","wangbo","chengxiao","zhangzhang","wangwenbo","xuchang","wenshiliang","zhouwenyu","liyan","wangshuo"),
	运营部门("huangqing","zhangjianqin","吉雅泰","chihang","qichongyang"),
	数据部门("zhangjian","luotao","liuchuan","makang","wuyunpeng","yaoyuan","sufang","chenhao","liushaohua","xiaoyang"),
	米佳("mijia"),
	刘洋("liuyanga"),
	张剑琴("zhangjianqin"),
	王勇全(new String[]{"黄金海岸","无极冰原","云波鬼岭","云海冰岚","炼狱绝地","东海龙宫","燃烧圣殿","太华神山","玉幡宝刹","太虚幻境","幽冥山谷","凌霄宝殿","霸气乾坤","烟雨青山","昆仑圣殿","仙山琼阁","飘渺王城","通天浮屠","金蛇圣殿","伏魔幻境","叱咤风云"},"wangyongquan"),
	刘洵(new String[]{"马踏千里","风雪之巅","傲剑凌云","岚境仙音","独战群神","霸气纵横","右道长亭","惊天战神","王者之域","傲视三界","遨游四海","冲霄云楼","裂风峡谷","斩龙神台","金宫银坊","幽灵山庄","狂龙魔窟","龙飞凤舞","太极玄天","百花仙谷","龙脉之地","雄霸天下"},"liuxun"),
	王天鑫(new String[]{"龙啸九州","神龙摆尾","独步天下","华山之巅","勇者无敌","唯我独尊","侠骨柔肠","鹏程万里","桃源仙境","左岸花海","无量净土","明空梵天","摘星楼台","瀛台仙山","白露横江","东极仙岛","万佛朝宗","七宝莲台","百花深谷","雪域冰城","北冥佛光","崆峒灵宝","西方灵山","幽恋蝶谷","百炼成仙","永安仙城","六道轮回","九霄之巅","壮气凌云","四海之巅"},"wangtianxin"),
	李慧琴(new String[]{"天下无双","洪荒古殿","江山美人","桃李春风","桃花仙岛","春风得意","鸟语花香","春暖花开","蜂飞蝶舞","金风玉露","九仙揽月","上善若水","万象更新","启天之路","仗剑天涯","踞虎盘龙","华夏战神","九霄龙吟","龙腾虎跃","龙翔凤舞","游云惊龙","柳暗花明","鱼跃龙门","飞龙在天","笑傲江湖","独闯天涯","拨云揽月","谁与争锋","傲视群雄","凌霜傲雪","独霸一方","仙山云海","月满乾坤","碧海青天","江山如画","霸气无双","千年古城","普陀潮音","海天佛国","乾元金光","飞瀑龙池","紫阳青峰","金霞玉鼎","瑶台仙宫","峨嵋金顶","问天灵台","蓬莱仙阁","鹊桥仙境","福地洞天","日光峡谷","霹雳霞光","逍遥神殿","仙界至尊","纵横天下","游龙引凤","对酒当歌"  },"lihuiqin"),
	呼叫部门("wangyao","wangchen","lizhaobo","lixiaolong","lizhengzheng","liuyuqing","hanzhaojing","lizhaobo","mengyangqing","yangzhao","liutao","wangyanan","fuhongwei","zhangjitong","chenmingyu","peishengran","qizhiwei","cuixiaomeng","nizhenning","zhaopengyu","zhuyina","liushengyue","zhouying","zhurui","zhenganqi","wangbin");
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
	int count = 0;
	Object obj = session.getAttribute("authorize.username");
	List<TransferQuestion> list = new ArrayList<TransferQuestion>();
	if(obj!=null){
		List<TransferQuestion> list2 = TransferQuestionManager.getInstance().getQuestionsViplevel("6");
		for(TransferQuestion qq:list2){
			if(!qq.getHandlOtherBuMeng().equals("已处理")){
				list.add(qq);
			}
		}	
	}else{
		out.print("请找相关人添加Gm后台权限!");
	}
%>
<form method="post">
	<table id="addmess"><caption>数量：<div id="nums"><%=list.size() %></div></caption>
		<tr><input type="hidden" id='currhandler'/><th>事件编号</th><th>游戏名称</th><th>服务器名</th><th>账号</th><th>VIP用户</th><th>一级分类</th><th>二级分类</th><th>提交时间</th><th>问题所在部门</th><th>当前状态</th><th>操作</th></tr>
		<%
			
			if(list!=null&&list.size()>0){
				for(TransferQuestion qq:list){
					if(qq.getHandlOtherBuMeng().equals("已处理")){
						continue;
					}
		%>
					<tr id='<%=qq.getId() %>del'><td><%=qq.getEventid() %></td><td><%=qq.getGameName() %></td><td><%=qq.getServerName() %></td><td><%=qq.getUsername() %></td><td><%=qq.getViplevel() %></td><td><%=qq.getQuestionType1() %></td><td><%=qq.getQuestionType2() %></td>
					<td><%=qq.getRecordTime() %></td><td id='<%=qq.getId()%>other'><%=qq.getHandlOtherBuMeng() %></td><td id='<%=qq.getId() %>'><%=qq.getCurrHadler() %></td><td><a title="我来处理" onclick='opennewwindow("<%=qq.getId() %>")'>处理</a>||<a title="查看问题" href="questionLastHandle.jsp?idd=chakan&id=<%=qq.getId() %>">查看</a></td></tr>
		<%	
				}
			}else{
				out.print("没有问题处理~");
			}
		%>
	</table>
	
</form>	
</body>
</html>