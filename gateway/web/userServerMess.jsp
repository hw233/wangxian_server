<%@page import="com.fy.gamegateway.mieshi.server.MieshiPlayerInfo"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiPlayerInfoManager"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfo"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <script type='text/javascript' src='jquery-1.6.2.js'></script>
  <link rel="stylesheet" href="xxk.css"/>
	<title>服务器列表</title>
</head>
<script language="javascript">
	$(document).ready(function(){
		$("a.tab").click(function () {
		// switch all tabs off
		$(".active").removeClass("active");
		// switch this tab on
		$(this).addClass("active");
		// slide all content up
		$(".content").slideUp();
		// slide this content up
		var content_show = $(this).attr("title");
		$("#"+content_show).slideDown();
		});
	}); 
</script>
<body>
<div id="tabbed_box_1" class="tabbed_box">
	<%
	String servername = request.getParameter("servername");
	String player = request.getParameter("playername");
	String viplevel = request.getParameter("viplevel");
	String username = request.getParameter("username");
	String qudao = request.getParameter("qudao");
	%>
	服务器：<%=servername %><br>
	帐号：<%=username %><br>
	角色名：<%=player %><br>
	VIP等级：<%=viplevel %><br>
	渠道：<%=qudao %>
    <div class="tabbed_area">
    	<ul class="tabs">
    	<%
    		
			MieshiServerInfoManager serverInfo = MieshiServerInfoManager.getInstance();
			MieshiPlayerInfoManager playerinfo = MieshiPlayerInfoManager.getInstance();
			String[] categories = serverInfo.getServerCategories();
			if(categories!=null&&categories.length>0){
				String []servernames = {"天下无双","洪荒古殿","江山美人","桃李春风","桃花仙岛","春风得意","鸟语花香","春暖花开","蜂飞蝶舞","金风玉露","九仙揽月",
						"上善若水","万象更新","启天之路","仗剑天涯","踞虎盘龙","华夏战神","柳暗花明","游云惊龙","龙翔凤舞","龙腾虎跃",
						"九霄龙吟","鱼跃龙门","龙啸九州","马踏千里","飞龙在天","笑傲江湖","独闯天涯","神龙摆尾","拨云揽月","逆转乾坤",
						"问鼎江湖","仙岛秘境","谁与争锋","傲视群雄","独步天下","凌霜傲雪","勇者无敌","唯我独尊","侠骨柔肠","独霸一方",
						"仙山云海","月满乾坤","碧海青天","江山如画","鹏程万里","霸气无双","对酒当歌","叱咤风云","雄霸天下","壮气凌云",
						"四海之巅","纵横天下","仙山琼阁","日光峡谷","霹雳霞光","逍遥神殿","仙界至尊","通天浮屠","金蛇圣殿","狂龙魔窟",
						"龙飞凤舞","太极玄天","百花仙谷","幽恋蝶谷","百炼成仙","永安仙城","六道轮回","烟雨青山","游龙引凤","伏魔幻境",
	        			"九霄之巅","龙脉之地","傲剑凌云","岚境仙音","霸气乾坤","独战群神","霸气纵横","右道长亭","惊天战神","王者之域",
	        			"傲视三界","遨游四海","冲霄云楼","裂风峡谷","凌霄宝殿","斩龙神台","金宫银坊","幽灵山庄","左岸花海","无量净土",
	        			"明空梵天","摘星楼台","瀛台仙山","白露横江","东极仙岛","万佛朝宗","七宝莲台","百花深谷","雪域冰城","北冥佛光",
	        			"崆峒灵宝","昆仑圣殿","西方灵山","普陀潮音","海天佛国","幽冥山谷","乾元金光","飞瀑龙池","紫阳青峰","金霞玉鼎",
	        			"太虚幻境","瑶台仙宫","峨嵋金顶","问天灵台","蓬莱仙阁","鹊桥仙境","福地洞天","黄金海岸","无极冰原","云波鬼岭",
	        			"云海冰岚","炼狱绝地","东海龙宫","燃烧圣殿","太华神山","千年古城","飘渺王城","风雪之巅","桃源仙境","玉幡宝刹"};
				String [] urlnames = {"http://116.213.192.230:8002","http://116.213.204.83:8002","http://116.213.204.101:8002","http://117.121.22.5:8001","http://117.121.22.4:8001","http://117.121.22.3:8001","http://116.213.192.214:8002","http://116.213.204.165:8001","http://116.213.204.146:8001","http://116.213.204.177:8001","http://116.213.204.111:8002","http://116.213.204.145:8001",
						"http://116.213.204.100:8002","http://116.213.204.176:8001","http://116.213.204.144:8001","http://116.213.193.71:8002","http://116.213.204.71:8002","http://117.135.130.112:8801","http://116.213.204.173:8001","http://116.213.192.244:8002","http://116.213.204.174:8001","http://116.213.204.175:8001",
						"http://116.213.204.163:8001","http://116.213.204.164:8001","http://116.213.204.162:8001","http://116.213.193.72:8002","http://116.213.192.248:8002",
						"http://116.213.204.67:8002","http://117.135.149.99:8801","http://116.213.204.68:8002","http://116.213.204.143:8001","http://116.213.204.134:8001",
						"http://116.213.193.66:8002","http://116.213.204.113:8002","http://116.213.204.112:8002","http://116.213.204.99:8002",
						"http://116.213.204.98:8002","http://116.213.204.132:8001","http://116.213.204.142:8001","http://116.213.204.133:8001","http://116.213.204.141:8001",
						"http://116.213.204.131:8001","http://116.213.204.140:8001","http://116.213.204.130:8001","http://116.213.204.116:8001","http://211.151.127.234:8001",
						"http://117.135.130.25:8801","http://116.213.204.103:8001","http://116.213.204.71:8001","http://116.213.192.232:8001","http://116.213.192.216:8001",
						"http://116.213.204.102:8001","http://116.213.204.115:8001","http://117.135.130.45:8801","http://116.213.192.213:8001","http://116.213.192.230:8001",
						"http://116.213.204.114:8001","http://116.213.204.69:8001","http://116.213.204.101:8001","http://116.213.204.80:8001","http://116.213.204.66:8002",
						"http://116.213.204.113:8001","http://116.213.204.112:8001","http://116.213.204.111:8001","http://116.213.204.81:8001","http://116.213.204.100:8001",
	        			"http://116.213.204.99:8001","http://116.213.204.98:8001","http://117.135.149.27:20178","http://116.213.192.227:8001","http://116.213.192.211:8001",
	        			"http://116.213.192.214:8001","http://116.213.192.248:8001","http://116.213.192.246:8001","http://116.213.192.243:8001","http://117.135.149.27:20184",
	        			"http://211.151.127.232:8001","http://211.151.127.233:8001","http://116.213.204.83:8001","http://211.151.127.231:8001","http://211.151.127.230:8001",
	        			"http://211.151.127.229:8001","http://211.151.127.228:8001","http://116.213.204.82:8001","http://116.213.204.68:8001","http://117.135.149.27:20032",
	        			"http://116.213.204.81:8003","http://116.213.204.84:8001","http://116.213.204.84:8002","http://116.213.204.72:8001","http://116.213.204.72:8002",
	        			"http://116.213.204.70:8001","http://116.213.204.70:8002","http://116.213.204.66:8001","http://116.213.204.67:8001","http://116.213.193.66:8001",
	        			"http://116.213.193.72:8001","http://116.213.193.71:8001","http://116.213.193.69:8001","http://116.213.193.70:8001","http://116.213.193.68:8001",
	        			"http://116.213.192.247:8001","http://117.135.149.27:20020","http://116.213.193.67:8001","http://116.213.192.247:8002","http://116.213.192.245:8002",
	        			"http://117.135.149.27:20014","http://116.213.192.244:8001","http://116.213.192.245:8001","http://116.213.192.242:8002","http://116.213.192.242:8001",
	        			"http://117.135.149.27:20026","http://116.213.192.231:8002","http://116.213.192.231:8001","http://116.213.192.215:8002","http://116.213.192.229:8001",
	        			"http://116.213.192.228:8001","http://116.213.192.226:8002","http://116.213.192.226:8001","http://116.213.192.215:8001","http://116.213.192.212:8002",
	        			"http://116.213.192.212:8001","http://116.213.192.210:8002","http://116.213.192.210:8001","http://116.213.142.181:8001","http://116.213.142.180:8001",
	        			"http://116.213.192.197:8001","http://116.213.192.196:8001","http://116.213.192.194:8001","http://116.213.192.195:8001","http://116.213.192.228:8002"};
				String url = "";
				if(servername!=null){
					for(int i=0;i<servernames.length;i++){
						if(servernames[i].equals(servername)){
							url = urlnames[i];
						}
					}
				}
				
				
				for(int i=0;i<categories.length;i++){
					MieshiServerInfo[] infos= serverInfo.getServerInfoListByCategory(categories[i]);	
					if(i==0){
						out.print("<li><a href='#' title='content_"+i+"' class='tab'>"+categories[i]+"</a></li>");
					}else{ 
						out.print("<li><a href='#' title='content_"+i+"' class='tab'>"+categories[i]+"</a></li>");
					}
					if(i+1==categories.length){
						out.print("</ul>");
					}
					
				}	
				for(int k=0;k<categories.length;k++){
					int index = k;
					MieshiServerInfo[] infos= serverInfo.getServerInfoListByCategory(categories[k]);
					for(int j=0; j<infos.length;j++){
						try{
							if(index==k){
								out.print("<div id='content_"+k+"' class='content'><ul>");
								index = -1;
							}
							MieshiPlayerInfo pi = playerinfo.getMieshiPlayerInfoByUsername(username, infos[j].getRealname());
							if(pi!=null){
								String playername = pi.getPlayerName();
								int level = pi.getLevel();
// 									out.println("<h4>[username:"+username+"] [servername:"+servername+"] [IP:"+infos[j].getIp()+"] [port:"+infos[j].getPort()+"] ["+infos[j].getName()+"] ["+infos[j].getRealname()+"] ["+infos[j].getServerType()+"] ["+infos[j].getCategory()+"]</h4>");
// 								out.print(url);
								out.print("<li><a href='"+url+"/gm/gmuser/user_action.jsp?userid="+username+"&authorize.username=wangtianxin&authorize.password=123321'>"+infos[j].getRealname()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<small><font color='red'>"+playername+"</font>  "+level+"级 </small></a></li>");
							}
						}catch(Exception e){
							e.printStackTrace();
							out.print(e);
							out.print("<li><a href=''>"+infos[j].getName()+"<small>"+"  "+"  "+"  "+"</small></a></li>");
						}
						if(j+1==infos.length){
							out.print("</ul></div>");
						}
					}	
				}
			}
		
		%>
    
    </div>

</div>


</body>
</html>