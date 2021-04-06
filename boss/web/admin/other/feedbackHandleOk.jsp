<%@page import="com.fy.boss.gm.gmuser.QuestionQuery"%>
<%@page import="com.fy.boss.gm.gmuser.server.TransferQuestionManager"%>
<%@page import="com.fy.boss.gm.XmlServer"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.boss.gm.XmlServerManager"%>
<%@page import="com.fy.boss.gm.gmuser.TransferQuestion"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<link type="text/css" rel="stylesheet" href="style.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>玩家反馈已处理列表</title>
<script type='text/javascript' src='jquery-1.6.2.js'></script>

<script type="text/javascript">

	function openDialogue(index,servername){
		var ss = document.getElementById('opendiv'+index).style.display;
		if (ss == "none"){
			document.getElementById('opendiv'+index).style.display = "block";
			$.ajax({
				type: 'POST',
				url: "getCrossServerContent.jsp",
				contentType : "application/x-www-form-urlencoded; charset=utf-8",
				data: {'fbId':index,"servername":servername},
				dataType: "text",
				error:function(XMLHttpRequest, textStatus, errorThrown){   alert("error:status:"+XMLHttpRequest.status);
                alert("error:readyState:"+XMLHttpRequest.readyState);
				},	
				success:function(result){
					if(result!=null){
						mess = result.split("#$&*^");	
						if(mess.length>0){
							for(var i=0;i<mess.length-1;i++){
									var obj = eval('('+mess[i]+')');
									var objid = obj.id;
									if(objid){
				 						 $("#record"+obj.fid).append(obj.dialogs);
									}
							}
						}	
					}
				}
			});
			document.getElementById('record'+index).scrollTop = document.getElementById('record'+index).scrollHeight;
		} else if (ss == "block"){
			document.getElementById('opendiv'+index).style.display = "none";
		}
	}
	
	function query(){
		var gmname = $("#gmname").val();
		var recorder = $("#recorder").html();
		var begintime = $("#begintime").val();
		var endtime = $("#endtime").val();
// 		alert("gmname:"+gmname+"--recorder:"+recorder+"--begintime:"+begintime+"--endtime:"+endtime);
		if(gmname!="-----"&&begintime&&endtime){
			servers = document.forms[0].server;
		    var tab = document.getElementById("msg");
		    //该表格的行数
		    var rowlen = tab.rows.length;
		    for(var i=rowlen-1;i>0;i--){
		    	tab.deleteRow(i);
		    }
		    var messNum = 0;
		    if(servers.length>0){
				for (i=0;i<servers.length;i++)
				{
					var adminurl = servers[i].value+"/getServer.jsp";
					var mess = new Array();
					$.ajax({
						type: 'POST',
						url: "crossServer.jsp",
						contentType : "application/x-www-form-urlencoded; charset=utf-8",
						data: {'gmname':gmname,'begintime':begintime,'endtime':endtime,'adminurl':adminurl},
						dataType: "text",
						error:function(XMLHttpRequest, textStatus, errorThrown){   alert("error:status:"+XMLHttpRequest.status);
		                alert("error:readyState:"+XMLHttpRequest.readyState);
						},
						success:function(result){
							
							//请求多个地址，多个地址的结果会一起到达
							messes = result.split("没有记录！");
							for(var j=0;j<messes.length;j++){
								if(messes[j].length>12){
									mess = messes[j].split("#$&*^");	
									if(mess.length>0){
										
										
										for(var i=0;i<mess.length-1;i++){
												var obj = eval('('+mess[i]+')');
												var objid = obj.id;
												if(objid){
													var emptyTr=document.getElementById('msg').insertRow(1);
													 emptyTr.id = "trid";
													 emptyTr.innerHTML = "<td colspan='10' id='"+obj.fid+"'><div style='display:none' id='isnewclose"+obj.fid+"'>0</div><div id='sendservernamediv"+obj.fid+"' style='display:none'>"+obj.servername+"</div><div id='senddiv"+obj.fid+"' style='display:none'>"+obj.fid+"</div><div id='opendiv"+obj.fid+"' style=\"display:none\"><table style='background-color:#f8f6ff;width:500px;height:300px; border:4px solid #3399cc;'><tr><td><table><tr><div style='width:330px;height:200px;overflow-x:hidden;overflow-y:scroll; border:2px solid #3399cc;' id='record"+obj.fid+"'>"+obj.dialogs+"</div></tr></table></td><td width='170px' valign='top'><br>服务器："+obj.servername+"<br>国家："+obj.countryname+"<br>账 号："+obj.userName+"<br>角色名："+obj.playerName+"<br>职业："+obj.career+"<br>等级："+obj.playerlevel+"</td></tr></table></div></td>";
													 var x=document.getElementById('msg').insertRow(1);
													 x.id = "trid";
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
													 
													 y.innerHTML=obj.servername;
													 r.innerHTML=obj.channel;
													 z.innerHTML="<div id='date"+obj.fid+"'>"+obj.sendTimestr+"</div>";
													 b.innerHTML=obj.userName;
													 c.innerHTML=obj.playerName;
													 if(obj.subject!=null){
														d.innerHTML = "<a href=\"javascript:openDialogue('"+obj.fid+"','"+obj.servername+"')\">"+obj.subject+"</a>";
													 }
													 if(obj.content!=null){
														 if(obj.content.length>10){
															 e.innerHTML=obj.content.substring(0,10)+"......";
														 }else{
															 e.innerHTML=obj.content;
														 }
													 }
													
													 if(obj.vipLevel==1){
														 f.innerHTML="<img src='images/v-01.png' width='30' height='30' title='1.体力最多可购买1次；2.增加1次喝酒次数；3.增加1次屠魔贴;4.开启随身修理'>";
													 }else if(obj.vipLevel==2){
														 f.innerHTML="<img src='images/v-02.png' width='30' height='30' title='1.体力最多可购买6次；2.开启随身邮件；3.增加每日绑银使用上限200两；4.增加1次额外经验种植机会；5.包含VIP1所有功能.'>";
													 }else if(obj.vipLevel==3){
														 f.innerHTML="<img src='images/v-03.png' width='30' height='30'  title='1.增加2次喝酒次数；2.增加2次屠魔贴;3.开启随身仓库;4.获得1次额外摇钱树种植机会;5.可以使用中级炼妖石;6.可书安心新境界6星以上杂念任务;7.包含VIP2所有功能.'>";
													 }else if(obj.vipLevel==4){
														 f.innerHTML="<img src='images/v-04.png' width='30' height='30' title='1.最多可购买14次体力；2.开启随身求购;3.可用银子书安心境界杂念任务;4.增加每日绑银使用上限500两；5.可使用仙府鬼斧神工令;6.押镖多一次加货机会；7.幽冥幻域每日最多可刷新3次;8.包含VIP3所有功能.'>";
													 }else if(obj.vipLevel==5){
														 f.innerHTML="<img src='images/v-05.png' width='30' height='30' title='1.增加3次喝酒次数；2.增加3次屠魔贴;3.可以用元宝捐献家族资金换取贡献度;4.幽冥幻域获得经验奖励提升50%；5.可以使用高级炼妖石;6获得2次额外摇钱树种植机会;6.包含VIP4所有功能.'>";
													 }else if(obj.vipLevel==6){
														 f.innerHTML="<img src='images/v-06.png' width='30' height='30' title='1.最多可购买24次体力；2.幽冥幻域获得经验奖励提升100%；3.增加每日绑银使用上限1锭；4.增加2次额外经验种植机会；5.幽冥幻域每日最多可刷新4次;6.包含VIP5所有功能.'>";
													 }else if(obj.vipLevel==7){
														 f.innerHTML="<img src='images/v-07.png' width='30' height='30' title='1.增加4次喝酒次数；2.增加4次屠魔贴；3.包含VIP6所有功能.'>";
													 }else if(obj.vipLevel==8){
														 f.innerHTML="<img src='images/v-08.png' width='30' height='30' title='1.体力最多可购买36次；2.包含VIP7所有功能.'>";
													 }else if(obj.vipLevel==9){
														 f.innerHTML="<img src='images/v-09.png' width='30' height='30' title='1.增加5次喝酒次数；2.增加5次屠魔贴；3.包含VIP8所有功能.'>";
													 }else if(obj.vipLevel==10){
														 f.innerHTML="<img src='images/v-10.png' width='30' height='30' title='1.体力最多可购买48次;2.包含VIP9所有功能。'>";
													 }else{
														 f.innerHTML="0";
													 }
													 g.innerHTML="<div id='recorder'>"+obj.lastGmId+"</div>";
													 h.innerHTML="<font color='black'>已关闭";
													 messNum++;
													 document.getElementById('title1').innerHTML = messNum;
												}
											
										}
									}
									
								}
							}	
							
								
						},
					});	
				}
			}
		}else{
			alert("请选择正确的编号，或时间不能为空！");
		}
		
	}
</script>
</head>
<body bgcolor="#c8edcc">
<h1>已处理问题列表</h1>
<%
	Object obj = session.getAttribute("authorize.username");
	String [] gmnums = {"GM00","GM01","GM02","GM04","GM05","GM06","GM07","GM08","GM09","GM10","GM11","GM12","GM15","GM16","GM17","GM18","GM19","GM20",
			"GM21","GM22","GM23","GM25","GM26","GM27","GM29","GM30","GM31","GM32","GM33","GM34","GM35","GM36","GM37","GM39","GM40",
			"GM41","GM42","GM43","GM44","GM45","GM46","GM47","GM48","GM49","GM50","GM51","GM52","GM53","GM54","GM55","GM56","GM57","GM58","GM59","GM60",
			"GM61","GM66","GM88","GM89","GM87","GM90","GM91","GM92","GM93","GM95","GM96","GM97","GM98","GM99","GM101","GM102","GM103","GM104","GM105"};
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String begin = sdf.format(new Date());
	String end = sdf.format(System.currentTimeMillis()+24*60*60*1000);
	
	
	
%>
<form>
<div>
<table style="display: none">
	<% 
	 			
	out.print("<th>91不可见</th>");
	out.print("<td ><div id='http://116.213.192.212:8002/gm' style='display:none;'>云波鬼岭</div><input type='checkbox' id='server' name='server' value='http://116.213.192.212:8002/gm'/>云波鬼岭</td>");
	out.print("<td ><div id='http://116.213.192.226:8001/gm' style='display:none;'>黄金海岸</div><input type='checkbox' id='server' name='server' value='http://116.213.192.226:8001/gm'/>黄金海岸</td>");
	out.print("</tr>");
	out.print("<tr>");
	out.print("<th>91、UC不可见</th>");
	out.print("<td ><div id='http://116.213.192.229:8001/gm' style='display:none;'>蓬莱仙阁</div><input type='checkbox' id='server' name='server' value='http://116.213.192.229:8001/gm'/>蓬莱仙阁</td>");
	out.print("<td ><div id='http://116.213.192.244:8001/gm' style='display:none;'>乾元金光</div><input type='checkbox' id='server' name='server' value='http://116.213.192.244:8001/gm'/>乾元金光</td>");
	out.print("<td ><div id='http://116.213.193.68:8001/gm' style='display:none;'>北冥佛光</div><input type='checkbox' id='server' name='server' value='http://116.213.193.68:8001/gm'/>北冥佛光</td>");
	out.print("<td ><div id='http://116.213.193.71:8001/gm' style='display:none;'>七宝莲台</div><input type='checkbox' id='server' name='server' value='http://116.213.193.71:8001/gm'/>七宝莲台</td>");
	out.print("<td ><div id='http://116.213.204.70:8001/gm' style='display:none;'>明空梵天</div><input type='checkbox' id='server' name='server' value='http://116.213.204.70:8001/gm'/>明空梵天</td>");
	out.print("<td ><div id='http://116.213.204.82:8001/gm' style='display:none;'>冲霄云楼</div><input type='checkbox' id='server' name='server' value='http://116.213.204.82:8001/gm'/>冲霄云楼</td>");
	out.print("<td ><div id='http://211.151.127.230:8001/gm' style='display:none;'>王者之域</div><input type='checkbox' id='server' name='server' value='http://211.151.127.230:8001/gm'/>王者之域</td>");
	out.print("<td ><div id='http://211.151.127.232:8001/gm' style='display:none;'>独战群神</div><input type='checkbox' id='server' name='server' value='http://211.151.127.232:8001/gm'/>独战群神</td>");
	out.print("<td ><div id='http://116.213.192.246:8001/gm' style='display:none;'>傲剑凌云</div><input type='checkbox' id='server' name='server' value='http://116.213.192.246:8001/gm'/>傲剑凌云</td>");
	out.print("<td ><div id='http://116.213.204.111:8001/gm' style='display:none;'>百花仙谷</div><input type='checkbox' id='server' name='server' value='http://116.213.204.111:8001/gm'/>百花仙谷</td>");
	out.print("<td><div id='http://116.213.204.80:8001/gm' style='display:none;'>金蛇圣殿</div><input type='checkbox' id='server' name='server' value='http://116.213.204.80:8001/gm'/>金蛇圣殿</td>");
	out.print("<td><div id='http://116.213.192.232:8001/gm' style='display:none;'>雄霸天下</div><input type='checkbox' id='server' name='server' value='http://116.213.192.232:8001/gm'/>雄霸天下</td>");
	out.print("<td><div id='http://116.213.204.69:8001/gm' style='display:none;'>仙界至尊</div><input type='checkbox' id='server' name='server' value='http://116.213.204.69:8001/gm'/>仙界至尊</td>");
	out.print("<td><div id='http://116.213.204.140:8001/gm' style='display:none;'>月满乾坤</div><input type='checkbox' id='server' name='server' value='http://116.213.204.140:8001/gm'/>月满乾坤</td>");
	out.print("<td><div id='http://116.213.204.132:8001/gm' style='display:none;'>勇者无敌</div><input type='checkbox' id='server' name='server' value='http://116.213.204.132:8001/gm'/>勇者无敌</td>");
	out.print("<td><div id='http://116.213.204.112:8002/gm' style='display:none;'>傲视群雄</div><input type='checkbox' id='server' name='server' value='http://116.213.204.112:8002/gm'/>傲视群雄</td>");
	out.print("<td ><div id='http://116.213.204.134:8001/gm' style='display:none;'>问鼎江湖</div><input type='checkbox' id='server' name='server' value='http://116.213.204.134:8001/gm'/>问鼎江湖</td>");
	out.print("<td ><div id='http://116.213.192.248:8002/gm' style='display:none;'>笑傲江湖</div><input type='checkbox' id='server' name='server' value='http://116.213.192.248:8002/gm'/>笑傲江湖</td>");
	out.print("<td ><div id='http://116.213.204.164:8001/gm' style='display:none;'>鱼跃龙门</div><input type='checkbox' id='server' name='server' value='http://116.213.204.164:8001/gm'/>鱼跃龙门</td>");
	out.print("<td ><div id='http://116.213.192.244:8002/gm' style='display:none;'>龙翔凤舞</div><input type='checkbox' id='server' name='server' value='http://116.213.192.244:8002/gm'/>龙翔凤舞</td>");
	out.print("</tr>");
	out.print("<tr>");
	out.print("<th>腾讯</th>");
	out.print("<td ><div id='http://117.135.149.27:20026/gm' style='display:none;'>太虚幻境</div><input type='checkbox' id='server' name='server' value='http://117.135.149.27:20026/gm'/>太虚幻境</td>");
	out.print("<td ><div id='http://117.135.149.27:20014/gm' style='display:none;'>幽冥山谷</div><input type='checkbox' id='server' name='server' value='http://117.135.149.27:20014/gm'/>幽冥山谷</td>");
	out.print("<td ><div id='http://117.135.149.27:20020/gm' style='display:none;'>昆仑圣殿</div><input type='checkbox' id='server' name='server' value='http://117.135.149.27:20020/gm'/>昆仑圣殿</td>");
	out.print("<td ><div id='http://117.135.149.27:20032/gm' style='display:none;'>凌霄宝殿</div><input type='checkbox' id='server' name='server' value='http://117.135.149.27:20032/gm'/>凌霄宝殿</td>");
	out.print("<td ><div id='http://117.135.149.27:20184/gm' style='display:none;'>霸气乾坤</div><input type='checkbox' id='server' name='server' value='http://117.135.149.27:20184/gm'/>霸气乾坤</td>");
	out.print("<td ><div id='http://117.135.149.27:20178/gm' style='display:none;'>烟雨青山</div><input type='checkbox' id='server' name='server' value='http://117.135.149.27:20178/gm'/>烟雨青山</td>");
	out.print("<td ><div id='http://117.135.130.45:8801/gm' style='display:none;'>仙山琼阁</div><input type='checkbox' id='server' name='server' value='http://117.135.130.45:8801/gm'/>仙山琼阁</td>");
	out.print("<td ><div id='http://117.135.130.25:8801/gm' style='display:none;'>霸气无双</div><input type='checkbox' id='server' name='server' value='http://117.135.130.25:8801/gm'/>霸气无双</td>");
	out.print("<td ><div id='http://117.135.149.97:8801/gm' style='display:none;'>华山之巅</div><input type='checkbox' id='server' name='server' value='http://117.135.149.97:8801/gm'/>华山之巅</td>");
	out.print("<td ><div id='http://117.135.149.99:8801/gm' style='display:none;'>神龙摆尾</div><input type='checkbox' id='server' name='server' value='http://117.135.149.99:8801/gm'/>神龙摆尾</td>");
	out.print("<td ><div id='http://117.135.130.112:8801/gm' style='display:none;'>柳暗花明</div><input type='checkbox' id='server' name='server' value='http://117.135.130.112:8801/gm'/>柳暗花明</td>");
	out.print("</tr>");
	out.print("<th>91专服</th>");
	out.print("<td ><div id='http://116.213.192.242:8001/gm' style='display:none;'>金霞玉鼎</div><input type='checkbox' id='server' name='server' value='http://116.213.192.242:8001/gm'/>金霞玉鼎</td>");
	out.print("<td ><div id='http://116.213.192.245:8002/gm' style='display:none;'>海天佛国</div><input type='checkbox' id='server' name='server' value='http://116.213.192.245:8002/gm'/>海天佛国</td>");
	out.print("<td ><div id='http://116.213.193.69:8001/gm' style='display:none;'>百花深谷</div><input type='checkbox' id='server' name='server' value='http://116.213.193.69:8001/gm'/>百花深谷</td>");
	out.print("<td ><div id='http://116.213.193.66:8001/gm' style='display:none;'>东极仙岛</div><input type='checkbox' id='server' name='server' value='http://116.213.193.66:8001/gm'/>东极仙岛</td>");
	out.print("<td ><div id='http://116.213.204.70:8002/gm' style='display:none;'>摘星楼台</div><input type='checkbox' id='server' name='server' value='http://116.213.204.70:8002/gm'/>摘星楼台</td>");
	out.print("<td ><div id='http://116.213.204.85:8001/gm' style='display:none;'>幽灵山庄</div><input type='checkbox' id='server' name='server' value='http://116.213.204.85:8001/gm'/>幽灵山庄</td>");
	out.print("<td ><div id='http://116.213.204.81:8003/gm' style='display:none;'>斩龙神台</div><input type='checkbox' id='server' name='server' value='http://116.213.204.81:8003/gm'/>斩龙神台</td>");
	out.print("<td ><div id='http://211.151.127.231:8001/gm' style='display:none;'>惊天战神</div><input type='checkbox' id='server' name='server' value='http://211.151.127.231:8001/gm'/>惊天战神</td>");
	out.print("<td ><div id='http://116.213.192.243:8001/gm' style='display:none;'>岚境仙音</div><input type='checkbox' id='server' name='server' value='http://116.213.192.243:8001/gm'/>岚境仙音</td>");
	out.print("<td ><div id='http://116.213.192.211:8001/gm' style='display:none;'>伏魔幻境</div><input type='checkbox' id='server' name='server' value='http://116.213.192.211:8001/gm'/>伏魔幻境</td>");
	out.print("<td ><div id='http://116.213.204.98:8001/gm' style='display:none;'>六道轮回</div><input type='checkbox' id='server' name='server' value='http://116.213.204.98:8001/gm'/>六道轮回</td>");
	out.print("<td ><div id='http://116.213.204.112:8001/gm' style='display:none;'>太极玄天</div><input type='checkbox' id='server' name='server' value='http://116.213.204.112:8001/gm'/>太极玄天</td>");
	out.print("<td ><div id='http://116.213.204.66:8002/gm' style='display:none;'>狂龙魔窟</div><input type='checkbox' id='server' name='server' value='http://116.213.204.66:8002/gm'/>狂龙魔窟</td>");
	out.print("<td ><div id='http://116.213.192.213:8001/gm' style='display:none;'>日光峡谷</div><input type='checkbox' id='server' name='server' value='http://116.213.192.213:8001/gm'/>日光峡谷</td>");
	out.print("<td ><div id='http://116.213.204.102:8001/gm' style='display:none;'>四海之巅</div><input type='checkbox' id='server' name='server' value='http://116.213.204.102:8001/gm'/>四海之巅</td>");
	out.print("<td ><div id='http://116.213.192.216:8001/gm' style='display:none;'>壮气凌云</div><input type='checkbox' id='server' name='server' value='http://116.213.192.216:8001/gm'/>壮气凌云</td>");
	out.print("<td ><div id='http://116.213.204.116:8001/gm' style='display:none;'>江山如画</div><input type='checkbox' id='server' name='server' value='http://116.213.204.116:8001/gm'/>江山如画</td>");
	out.print("<td ><div id='http://116.213.204.130:8001/gm' style='display:none;'>碧海青天</div><input type='checkbox' id='server' name='server' value='http://116.213.204.130:8001/gm'/>碧海青天</td>");
	out.print("<td ><div id='http://116.213.204.133:8001/gm' style='display:none;'>侠骨柔肠</div><input type='checkbox' id='server' name='server' value='http://116.213.204.133:8001/gm'/>侠骨柔肠</td>");
	out.print("<td ><div id='http://116.213.204.98:8002/gm' style='display:none;'>凌霜傲雪</div><input type='checkbox' id='server' name='server' value='http://116.213.204.98:8002/gm'/>凌霜傲雪</td>");
	out.print("<td ><div id='http://116.213.193.66:8002/gm' style='display:none;'>仙岛秘境</div><input type='checkbox' id='server' name='server' value='http://116.213.193.66:8002/gm'/>仙岛秘境</td>");
	out.print("<td ><div id='http://116.213.204.68:8002/gm' style='display:none;'>拨云揽月</div><input type='checkbox' id='server' name='server' value='http://116.213.204.68:8002/gm'/>拨云揽月</td>");
	out.print("<td ><div id='http://116.213.204.162:8001/gm' style='display:none;'>马踏千里</div><input type='checkbox' id='server' name='server' value='http://116.213.204.162:8001/gm'/>马踏千里</td>");
	out.print("<td ><div id='http://116.213.204.173:8001/gm' style='display:none;'>游云惊龙</div><input type='checkbox' id='server' name='server' value='http://116.213.204.173:8001/gm'/>游云惊龙</td>");
	out.print("</tr>");
	out.print("<tr>");
	out.print("<th>UC专服</th>");
	out.print("<td ><div id='http://116.213.192.247:8002/gm' style='display:none;'>普陀潮音</div><input type='checkbox' id='server' name='server' value='http://116.213.192.247:8002/gm'/>普陀潮音</td>");
	out.print("<td ><div id='http://116.213.192.228:8001/gm' style='display:none;'>鹊桥仙境</div><input type='checkbox' id='server' name='server' value='http://116.213.192.228:8001/gm'/>鹊桥仙境</td>");
	out.print("<td ><div id='http://116.213.192.231:8002/gm' style='display:none;'>瑶台仙宫</div><input type='checkbox' id='server' name='server' value='http://116.213.192.231:8002/gm'/>瑶台仙宫</td>");
	out.print("<td ><div id='http://116.213.192.242:8002/gm' style='display:none;'>紫阳青峰</div><input type='checkbox' id='server' name='server' value='http://116.213.192.242:8002/gm'/>紫阳青峰</td>");
	out.print("<td ><div id='http://116.213.192.247:8001/gm' style='display:none;'>崆峒灵宝</div><input type='checkbox' id='server' name='server' value='http://116.213.192.247:8001/gm'/>崆峒灵宝</td>");
	out.print("<td ><div id='http://116.213.193.72:8001/gm' style='display:none;'>万佛朝宗</div><input type='checkbox' id='server' name='server' value='http://116.213.193.72:8001/gm'/>万佛朝宗</td>");
	out.print("<td ><div id='http://116.213.204.66:8001/gm' style='display:none;'>瀛台仙山</div><input type='checkbox' id='server' name='server' value='http://116.213.204.66:8001/gm'/>瀛台仙山</td>");
	out.print("<td ><div id='http://116.213.204.72:8002/gm' style='display:none;'>无量净土</div><input type='checkbox' id='server' name='server' value='http://116.213.204.72:8002/gm'/>无量净土</td>");
	out.print("<td ><div id='http://116.213.204.84:8001/gm' style='display:none;'>金宫银坊</div><input type='checkbox' id='server' name='server' value='http://116.213.204.84:8001/gm'/>金宫银坊</td>");
	out.print("<td ><div id='http://211.151.127.228:8001/gm' style='display:none;'>遨游四海</div><input type='checkbox' id='server' name='server' value='http://211.151.127.228:8001/gm'/>遨游四海</td>");
	out.print("<td ><div id='http://211.151.127.229:8001/gm' style='display:none;'>傲视三界</div><input type='checkbox' id='server' name='server' value='http://211.151.127.229:8001/gm'/>傲视三界</td>");
	out.print("<td ><div id='http://211.151.127.233:8001/gm' style='display:none;'>霸气纵横</div><input type='checkbox' id='server' name='server' value='http://211.151.127.233:8001/gm'/>霸气纵横</td>");
	out.print("<td ><div id='http://116.213.192.248:8001/gm' style='display:none;'>龙脉之地</div><input type='checkbox' id='server' name='server' value='http://116.213.192.248:8001/gm'/>龙脉之地</td>");
	out.print("<td ><div id='http://116.213.192.214:8001/gm' style='display:none;'>九霄之巅</div><input type='checkbox' id='server' name='server' value='http://116.213.192.214:8001/gm'/>九霄之巅</td>");
	out.print("<td ><div id='http://116.213.192.227:8001/gm' style='display:none;'>游龙引凤</div><input type='checkbox' id='server' name='server' value='http://116.213.192.227:8001/gm'/>游龙引凤</td>");
	out.print("<td ><div id='http://116.213.204.100:8001/gm' style='display:none;'>百炼成仙</div><input type='checkbox' id='server' name='server' value='http://116.213.204.100:8001/gm'/>百炼成仙</td>");
	out.print("<td ><div id='http://116.213.204.113:8001/gm' style='display:none;'>龙飞凤舞</div><input type='checkbox' id='server' name='server' value='http://116.213.204.113:8001/gm'/>龙飞凤舞</td>");
	out.print("<td ><div id='http://116.213.204.101:8001/gm' style='display:none;'>通天浮屠</div><input type='checkbox' id='server' name='server' value='http://116.213.204.101:8001/gm'/>通天浮屠</td>");
	out.print("<td ><div id='http://116.213.204.114:8001/gm' style='display:none;'>逍遥神殿</div><input type='checkbox' id='server' name='server' value='http://116.213.204.114:8001/gm'/>逍遥神殿</td>");
	out.print("<td ><div id='http://116.213.204.115:8001/gm' style='display:none;'>纵横天下</div><input type='checkbox' id='server' name='server' value='http://116.213.204.115:8001/gm'/>纵横天下</td>");
	out.print("<td ><div id='http://116.213.204.71:8001/gm' style='display:none;'>叱咤风云</div><input type='checkbox' id='server' name='server' value='http://116.213.204.71:8001/gm'/>叱咤风云</td>");
	out.print("<td ><div id='http://211.151.127.234:8001/gm' style='display:none;'>鹏程万里</div><input type='checkbox' id='server' name='server' value='http://211.151.127.234:8001/gm'/>鹏程万里</td>");
	out.print("<td ><div id='http://116.213.204.131:8001/gm' style='display:none;'>仙山云海</div><input type='checkbox' id='server' name='server' value='http://116.213.204.131:8001/gm'/>仙山云海</td>");
	out.print("<td ><div id='http://116.213.204.142:8001/gm' style='display:none;'>唯我独尊</div><input type='checkbox' id='server' name='server' value='http://116.213.204.142:8001/gm'/>唯我独尊</td>");
	out.print("<td ><div id='http://116.213.204.113:8002/gm' style='display:none;'>谁与争锋</div><input type='checkbox' id='server' name='server' value='http://116.213.204.113:8002/gm'/>谁与争锋</td>");
	out.print("<td><div id='http://116.213.204.143:8001/gm' style='display:none;'>逆转乾坤</div><input type='checkbox' id='server' name='server' value='http://116.213.204.143:8001/gm'/>逆转乾坤</td>");
	out.print("<td><div id='http://116.213.204.67:8002/gm' style='display:none;'>独闯天涯</div><input type='checkbox' id='server' name='server' value='http://116.213.204.67:8002/gm'/>独闯天涯</td>");
	out.print("<td><div id='http://116.213.204.163:8001/gm' style='display:none;'>龙啸九州</div><input type='checkbox' id='server' name='server' value='http://116.213.204.163:8001/gm'/>龙啸九州</td>");
	out.print("<td><div id='http://116.213.204.174:8001/gm' style='display:none;'>龙腾虎跃</div><input type='checkbox' id='server' name='server' value='http://116.213.204.174:8001/gm'/>龙腾虎跃</td>");
	out.print("</tr>");
	out.print("<tr>");
	out.print("<th>苹果专服<font color='red'></th>");
	out.print("<td ><div id='http://116.213.193.67:8001/gm' style='display:none;'>西方灵山</div><input type='checkbox' id='server' name='server' value='http://116.213.193.67:8001/gm'/>西方灵山</td>");
	out.print("<td ><div id='http://116.213.192.245:8001/gm' style='display:none;'>飞瀑龙池</div><input type='checkbox' id='server' name='server' value='http://116.213.192.245:8001/gm'/>飞瀑龙池</td>");
	out.print("<td ><div id='http://116.213.192.228:8002/gm' style='display:none;'>玉幡宝刹</div><input type='checkbox' id='server' name='server' value='http://116.213.192.228:8002/gm'/>玉幡宝刹</td>");
	out.print("<td ><div id='http://116.213.192.215:8002/gm' style='display:none;'>问天灵台</div><input type='checkbox' id='server' name='server' value='http://116.213.192.215:8002/gm'/>问天灵台</td>");
	out.print("<td ><div id='http://116.213.193.70:8001/gm' style='display:none;'>雪域冰城</div><input type='checkbox' id='server' name='server' value='http://116.213.193.70:8001/gm'/>雪域冰城</td>");
	out.print("<td ><div id='http://116.213.204.67:8001/gm' style='display:none;'>白露横江</div><input type='checkbox' id='server' name='server' value='http://116.213.204.67:8001/gm'/>白露横江</td>");
	out.print("<td ><div id='http://116.213.204.72:8001/gm' style='display:none;'>左岸花海</div><input type='checkbox' id='server' name='server' value='http://116.213.204.72:8001/gm'/>左岸花海</td>");
	out.print("<td ><div id='http://116.213.204.68:8001/gm' style='display:none;'>裂风峡谷</div><input type='checkbox' id='server' name='server' value='http://116.213.204.68:8001/gm'/>裂风峡谷</td>");
	out.print("<td ><div id='http://116.213.204.83:8001/gm' style='display:none;'>右道长亭</div><input type='checkbox' id='server' name='server' value='http://116.213.204.83:8001/gm'/>右道长亭</td>");
	out.print("<td ><div id='http://116.213.204.99:8001/gm' style='display:none;'>永安仙城</div><input type='checkbox' id='server' name='server' value='http://116.213.204.99:8001/gm'/>永安仙城</td>");
	out.print("<td ><div id='http://116.213.192.230:8001/gm' style='display:none;'>霹雳霞光</div><input type='checkbox' id='server' name='server' value='http://116.213.192.230:8001/gm'/>霹雳霞光</td>");
	out.print("<td ><div id='http://116.213.204.103:8001/gm' style='display:none;'>对酒当歌</div><input type='checkbox' id='server' name='server' value='http://116.213.204.103:8001/gm'/>对酒当歌</td>");
	out.print("<td ><div id='http://116.213.204.141:8001/gm' style='display:none;'>独霸一方</div><input type='checkbox' id='server' name='server' value='http://116.213.204.141:8001/gm'/>独霸一方</td>");
	out.print("<td ><div id='http://116.213.204.99:8002/gm' style='display:none;'>独步天下</div><input type='checkbox' id='server' name='server' value='http://116.213.204.99:8002/gm'/>独步天下</td>");
	out.print("<td ><div id='http://116.213.193.72:8002/gm' style='display:none;'>飞龙在天</div><input type='checkbox' id='server' name='server' value='http://116.213.193.72:8002/gm'/>飞龙在天</td>");
	out.print("<td ><div id='http://116.213.204.175:8001/gm' style='display:none;'>九霄龙吟</div><input type='checkbox' id='server' name='server' value='http://116.213.204.175:8001/gm'/>九霄龙吟</td>");
	out.print("</tr>");
	out.print("<tr>");
	out.print("<th>当乐、UC不可见</th>");
	out.print("<td ><div id='http://116.213.192.212:8001/gm' style='display:none;'>云海冰岚</div><input type='checkbox' id='server' name='server' value='http://116.213.192.212:8001/gm'/>云海冰岚</td>");
	out.print("<td ><div id='http://116.213.192.215:8001/gm' style='display:none;'>无极冰原</div><input type='checkbox' id='server' name='server' value='http://116.213.192.215:8001/gm'/>无极冰原</td>");
	out.print("<td ><div id='http://116.213.192.226:8002/gm' style='display:none;'>福地洞天</div><input type='checkbox' id='server' name='server' value='http://116.213.192.226:8002/gm'/>福地洞天</td>");
	out.print("<td ><div id='http://116.213.192.231:8001/gm' style='display:none;'>峨嵋金顶</div><input type='checkbox' id='server' name='server' value='http://116.213.192.231:8001/gm'/>峨嵋金顶</td>");
	out.print("</tr>");
	out.print("<tr>");
	out.print("<th>全混</th>");
	out.print("<td><div id='http://116.213.192.194:8001/gm' style='display:none;'>风雪之巅</div><input type='checkbox' id='server' name='server' value='http://116.213.192.194:8001/gm'/>风雪之巅</td>");
	out.print("<td><div id='http://116.213.192.195:8001/gm' style='display:none;'>桃源仙境</div><input type='checkbox' id='server' name='server' value='http://116.213.192.195:8001/gm'/>桃源仙境</td>");
	out.print("<td><div id='http://116.213.192.196:8001/gm' style='display:none;'>飘渺王城</div><input type='checkbox' id='server' name='server' value='http://116.213.192.196:8001/gm'/>飘渺王城</td>");
	out.print("<td><div id='http://116.213.192.197:8001/gm' style='display:none;'>千年古城</div><input type='checkbox' id='server' name='server' value='http://116.213.192.197:8001/gm'/>千年古城</td>");
	out.print("<td><div id='http://116.213.142.181:8001/gm' style='display:none;'>燃烧圣殿</div><input type='checkbox' id='server' name='server' value='http://116.213.142.181:8001/gm'/>燃烧圣殿</td>");
	out.print("<td><div id='http://116.213.142.180:8001/gm' style='display:none;'>太华神山</div><input type='checkbox' id='server' name='server' value='http://116.213.142.180:8001/gm'/>太华神山</td>");
	out.print("<td><div id='http://116.213.192.210:8001/gm' style='display:none;'>东海龙宫</div><input type='checkbox' id='server' name='server' value='http://116.213.192.210:8001/gm'/>东海龙宫</td>");
	out.print("<td><div id='http://116.213.192.210:8002/gm' style='display:none;'>炼狱绝地</div><input type='checkbox' id='server' name='server' value='http://116.213.192.210:8002/gm'/>炼狱绝地</td>");
	out.print("</tr>");
	 		
	 %>	
	 </div>
	 </table>
	 </form>

<form>
	<table>
		<tr><th>GM编号：</th>
			<td><select id='gmname'>
			<option>-----</option>
			<%
				for(int i=0;i<gmnums.length;i++){
					out.print("<option>"+gmnums[i]+"</option>");
				}
			%>
			</select></td>
		</tr>
		<tr><th>时间段：</th><td><input type='text' name='begintime' id='begintime' value='<%=begin%>'>--<input type='text' name='endtime' id='endtime' value='<%=end%>'></td></tr><font color='red'></font>
		<tr><input type="button" value='查询' onclick="query()"/><input type="reset" value='清空'/></tr>
	</table>
</form>

<table id="msg">
	<caption id='title1'>0</caption>
		<tr>
			<th>服务器</th>
			<th>渠道</th>
			<th>玩家反馈时间</th>
			<th>账号</th>
			<th>角色名</th>
			<th>标题</th>
			<th>内容</th>
			<th>VIP等级</th>
			<th>操作人员</th>
			<th>反馈状态</th>
 		</tr>
</table>
</body>
</html>