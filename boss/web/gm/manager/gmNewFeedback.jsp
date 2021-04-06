<%@page import="java.util.ArrayList"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="java.util.Date"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="com.fy.boss.gm.record.TelRecord"%>
<%@page import="com.fy.boss.gm.record.TelRecordManager"%>
<%@page import="com.fy.boss.gm.record.ActionManager"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.gm.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="../css/style.css"/>
<title>选择服务器</title>
</head>

<!-- 该页面只管显示 -->
 <%
 	int trnum1 = 0;
 %>
<script language="javascript"> 

	//记录是否点击'选择服务器'按钮
	var clicknum = 0;
	//总条数
	var trnum1 = 0;
	var trnum2 = 0;
	//消息创建时间，消息回复时间，第一次获得数据的时间
	var replydate = 0;
	//排序
	var newmess = new Array();
	//定义未处理显示数
	var nonum = 1;
	//编号
	var messnum = 0;
	//记录未处理数组长度，用来控制在上一次没有排序完，不能添加新的元素
	var nolength = 0;
	
	//定义状态数量
	huizongnum=0;
	huizongnum_=0;
	huizongnum__=0;
	huizongnum_sum=0;
	
	function feedback(channel,gmstat,sendtime,fid,servername,date,username,name,title,content,vip,gm,state,career,level,country,dialog){
		this.channel = channel;
		this.gmstat = gmstat;
		this.replytime = sendtime;
		this.fid = fid;
		this.servername = servername;	
		this.date = date;
		this.username = username;
		this.name = name;
		this.title = title;
		this.content = content;
		this.vip = vip;
		this.gm = gm;
		this.state = state;
		this.career = career;
		this.level = level;
		this.country = country;
		this.dialog = dialog;
	}
	var selectedservers = new Array();
	
	function ss(){
		//可优化 不必每次执行
		
		servers = document.forms[0].server;
		url = "";
		for (i=0;i<servers.length;i++)
		{
			if (servers[i].checked)
			{
				selectedservers.push(servers[i]);
			}
		}
		if(selectedservers.length>0){
			document.getElementById("mytable").style.display='none'; 
			document.getElementById("mybutton").style.display='none'; 
			document.getElementById("allche").style.display='none'; 
			self.setInterval("sure()",2000);
		}else{
			alert("请选择服务器！");
		}
		
	}
	
	function openDialogue(index){
		var ss = document.getElementById('opendiv'+index).style.display;
		if (ss == "none"){
			document.getElementById('opendiv'+index).style.display = "block";
			document.getElementById('record'+index).scrollTop = document.getElementById('record'+index).scrollHeight;
		} else if (ss == "block"){
			document.getElementById('opendiv'+index).style.display = "none";
		}
		
	}
	
	//根据时间排序
	function delMess(obj,obj2){
// 		alert("obj.replytime:"+obj+"--obj2.replytime:"+obj2);
		if(obj>obj2){
			return 1;
		}
		if(obj<obj2){
			return -1;
		}
	}
	
	function addquestion(id,servername,channel,userName,playerName,vipLevel){
		window.open('../../questionAdd.jsp?id='+id+'&servername='+servername+'&username='+userName+'&qudao='+channel+'&playername='+playerName+'&viplevel='+vipLevel,'newwindow','width=700,height=460,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no,z-look=no');
	}
	
	function queryquestion(){
		window.open('../record/telrecord_list.jsp','newwindow','height=500,width=700,top=160,left=180,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no,z-look=no,alwaysRaised=yse ');
	}
	
	function cb(data){
		if(data){
			 var obj = data;
			 //用于定位消息的位置
			 var messIndex = 0;
			 

			 
			if(obj.GmState==2&&obj.servername){
				 var myfeedback = new feedback(obj.channel,obj.GmState,obj.sendTimes,obj.fid,obj.servername,obj.sendTimestr,obj.userName,obj.playerName,obj.subject,obj.content,obj.vipLevel,obj.lastGmId,obj.GmState,obj.career,obj.playerlevel,obj.countryname,obj.dialogs);
				 var isshow = document.getElementById(myfeedback.fid);
				 var newnum = document.getElementById(myfeedback.servername+'_new').innerHTML;
				 var sumnum = document.getElementById(myfeedback.servername+'_sum').innerHTML;
				 var nonum = document.getElementById(myfeedback.servername+'_no').innerHTML;

				 replydate = myfeedback.replytime+"##"+myfeedback.servername;
					 if(isshow!=null){
// 						 alert("状态是新，已经有了");
						 document.getElementById('isnewclose'+myfeedback.fid).innerHTML = 1;
						 document.getElementById('record'+myfeedback.fid).innerHTML = myfeedback.dialog;	 
						 document.getElementById('date'+myfeedback.fid).innerHTML = "<div id='date"+myfeedback.fid+"'>"+myfeedback.date+"</div>";
						 document.getElementById('gmstat'+obj.fid).innerHTML = "<font color='green'>待处理";
						 document.getElementById('record'+myfeedback.fid).scrollTop = document.getElementById('record'+myfeedback.fid).scrollHeight;
					 	 document.getElementById(myfeedback.fid).style.bgcolor = "red";
					 }else{
						 //判断是否add数组
						 var isadd = 0;
						 //处理完毕的消息，不参与排序,没有玩家的回复时间
						 if(obj.GmState==2||obj.GmState==0){
							 if(obj.servername){
								 for(var a=0;a<newmess.length;a++){
									 if(obj.sendTimes==newmess[a]){
										 isadd = 1;
									 }
								 }
								 if(isadd==0){
									 newmess.push(obj.sendTimes);
//			 						 alert("isadd==0刷新后长度："+newmess.length+"-状态:"+obj.GmState+"--时间:"+obj.sendTimes+"--subject:"+obj.subject);
								 }
								 
//			 					 alert("刷新后长度："+newmess.length+"-状态:"+obj.GmState+"--时间:"+obj.sendTimes+"--subject:"+obj.subject);
							 }
//			 				 alert("刷新qian长度："+newmess.length+"-server:"+obj.servername+"--sendTimes:"+obj.sendTimes+"--userName:"+obj.userName+"--subject:"+obj.subject);
						 }
						 if(newmess.length>0){
							 newmess.sort(delMess);
							 var newindex = 0;
//		 					 alert("newmess.length:"+newmess.length);
							 for(var i=0;i<newmess.length;i++){
								 
								 if(newmess[i]!=null&&newmess[i]==myfeedback.replytime){
									 newindex= i;
//		 							 alert("GmState==2newindex:"+newindex);
									 break;
								 }
							 }
							 messIndex = parseInt(newindex)*2+1;
//		 					 alert("GmState==2messIndex:"+messIndex);
						 }else{
						  	messIndex = 1;
						 }
						 huizongnum_ = parseInt(huizongnum_)+1;
						 huizongnum_sum = parseInt(huizongnum_sum)+1;
						 document.getElementById('huizong_new').innerHTML = huizongnum_;
						 document.getElementById('huizong_sum').innerHTML = huizongnum_sum;
// 						 alert("1111111newnum:"+newnum+"--sumnum:"+sumnum+"--parseInt(newnum)+parseInt(1):"+(parseInt(newnum)+parseInt(1)));
						 document.getElementById(myfeedback.servername+'_new').innerHTML = parseInt(newnum)+parseInt(1);
						 document.getElementById(myfeedback.servername+'_sum').innerHTML = parseInt(sumnum)+parseInt(1);
						
						 scrollmess(myfeedback);
						 
						 if(newmess.length>0){
// 							 alert("新tr："+messIndex);
							 var emptyTr=document.getElementById('msg').insertRow(messIndex);
							 emptyTr.id = "trid"+myfeedback.fid;
							 var pUserName = myfeedback.username;
							 if(pUserName.indexOf("QQUSER")!=-1){
								 pUserName = pUserName.substring(0,6);
							 }
							 
							 emptyTr.innerHTML = "<td colspan='11' id='"+myfeedback.fid+"'><div style='display:none' id='isnewclose"+myfeedback.fid+"'>0</div><div id='arrindex1"+myfeedback.fid+"' style='display:none'>"+newindex+"</div><div id='valuenew"+myfeedback.fid+"' style='display:none'>"+messIndex+"</div><div id='sendservernamediv"+myfeedback.fid+"' style='display:none'>"+myfeedback.servername+"</div><div id='senddiv"+myfeedback.fid+"' style='display:none'>"+myfeedback.fid+"</div><div id='opendiv"+myfeedback.fid+"' style=\"display:none\"><table style='background-color:#f8f6ff;width:500px;height:300px; border:4px solid #3399cc;'><tr><td><table><tr><div style='width:330px;height:200px;overflow-x:hidden;overflow-y:scroll; border:2px solid #3399cc;' id='record"+myfeedback.fid+"'>"+myfeedback.dialog+"</div></tr><tr><textarea id='senddialogid"+myfeedback.fid+"' style='width:330px;height:60px;'></textarea></tr><tr><input type='button' id='click' name='click' onclick=\"send('"+myfeedback.fid+"')\" value='发送'/><input type='button' id='close' name='close' onclick=\"closes("+myfeedback.state+",'"+myfeedback.fid+"','"+obj.sendTimes+"')\" value='关闭会话'/></tr></table></td><td width='170px' valign='top'><input type='button' id='qiangzhi' onclick=\"closes('qzclose','"+myfeedback.fid+"','"+obj.sendTimes+"')\" value='手动关闭'/><br>服务器："+myfeedback.servername+"<br>国家："+myfeedback.country+"<br>账 号："+myfeedback.username+"<br>角色名："+myfeedback.name+"<br>职业："+myfeedback.career+"<br>等级："+myfeedback.level+"</td></tr></table></div></td>";
	 						 
							 document.getElementById('isnewclose'+myfeedback.fid).innerHTML = 0;
							 var x=document.getElementById('msg').insertRow(messIndex);
							 x.id = "trxid"+myfeedback.fid;
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
							 var j=x.insertCell(10);
							 
							 
							 y.innerHTML=myfeedback.servername;
							 r.innerHTML=myfeedback.channel;
							 z.innerHTML="<div id='date"+myfeedback.fid+"'>"+myfeedback.date+"</div>";
							 b.innerHTML=pUserName+".....";
							 c.innerHTML=myfeedback.name;
							 if(myfeedback.title!=null){
								d.innerHTML = "<a href=\"javascript:openDialogue('"+myfeedback.fid+"')\">"+myfeedback.title+"</a>";
							 }
							 if(myfeedback.content!=null){
								 if(myfeedback.content.length>10){
									 e.innerHTML=myfeedback.content.substring(0,10)+"......";
								 }else{
									 e.innerHTML=myfeedback.content;
								 }
							 }
							 
							 if(myfeedback.vip==1){
								 f.innerHTML="<img src='../../images/v-01.png' width='30' height='30' title='1.体力最多可购买1次；2.增加1次喝酒次数；3.增加1次屠魔贴;4.开启随身修理'>";
							 }else if(myfeedback.vip==2){
								 f.innerHTML="<img src='../../images/v-02.png' width='30' height='30' title='1.体力最多可购买6次；2.开启随身邮件；3.增加每日绑银使用上限200两；4.增加1次额外经验种植机会；5.包含VIP1所有功能.'>";
							 }else if(myfeedback.vip==3){
								 f.innerHTML="<img src='../../images/v-03.png' width='30' height='30'  title='1.增加2次喝酒次数；2.增加2次屠魔贴;3.开启随身仓库;4.获得1次额外摇钱树种植机会;5.可以使用中级炼妖石;6.可书安心新境界6星以上杂念任务;7.包含VIP2所有功能.'>";
							 }else if(myfeedback.vip==4){
								 f.innerHTML="<img src='../../images/v-04.png' width='30' height='30' title='1.最多可购买14次体力；2.开启随身求购;3.可用银子书安心境界杂念任务;4.增加每日绑银使用上限500两；5.可使用仙府鬼斧神工令;6.押镖多一次加货机会；7.幽冥幻域每日最多可刷新3次;8.包含VIP3所有功能.'>";
							 }else if(myfeedback.vip==5){
								 f.innerHTML="<img src='../../images/v-05.png' width='30' height='30' title='1.增加3次喝酒次数；2.增加3次屠魔贴;3.可以用元宝捐献家族资金换取贡献度;4.幽冥幻域获得经验奖励提升50%；5.可以使用高级炼妖石;6获得2次额外摇钱树种植机会;6.包含VIP4所有功能.'>";
							 }else if(myfeedback.vip==6){
								 f.innerHTML="<img src='../../images/v-06.png' width='30' height='30' title='1.最多可购买24次体力；2.幽冥幻域获得经验奖励提升100%；3.增加每日绑银使用上限1锭；4.增加2次额外经验种植机会；5.幽冥幻域每日最多可刷新4次;6.包含VIP5所有功能.'>";
							 }else if(myfeedback.vip==7){
								 f.innerHTML="<img src='../../images/v-07.png' width='30' height='30' title='1.增加4次喝酒次数；2.增加4次屠魔贴；3.包含VIP6所有功能.'>";
							 }else if(myfeedback.vip==8){
								 f.innerHTML="<img src='../../images/v-08.png' width='30' height='30' title='1.体力最多可购买36次；2.包含VIP7所有功能.'>";
							 }else if(myfeedback.vip==9){
								 f.innerHTML="<img src='../../images/v-09.png' width='30' height='30' title='1.增加5次喝酒次数；2.增加5次屠魔贴；3.包含VIP8所有功能.'>";
							 }else if(myfeedback.vip==10){
								 f.innerHTML="<img src='../../images/v-10.png' width='30' height='30' title='1.体力最多可购买48次;2.包含VIP9所有功能。'>";
							 }else{
								 f.innerHTML="0";
							 }
							 
							 g.innerHTML="<div id='recorder"+myfeedback.fid+"'>"+myfeedback.gm+"</div>";
							 if(myfeedback.state==0){
								 h.innerHTML="<div id='gmstat"+myfeedback.fid+"'><font color='red'>未处理</div>";
							 }else if(myfeedback.state==2){
								 h.innerHTML="<div id='gmstat"+myfeedback.fid+"'><font color='green'>待处理</div>";
							 }else if(myfeedback.state==1||myfeedback.state==3){
								 h.innerHTML="<font color='black'>其他";
							 }
							 j.innerHTML = "<input type='button' onclick=\"addquestion('"+obj.fid+"','"+obj.servername+"','"+obj.channel+"','"+obj.userName+"','"+obj.playerName+"','"+obj.vipLevel+"')\" value='添加'/><input type='button' onclick='queryquestion()' value='查找'/>";
							 document.getElementById('title1').innerHTML = obj.sendTimestr;
						 }
					 }
			 }
			 else if(obj.GmState==0&&obj.servername){
				 
				 var myfeedback = new feedback(obj.channel,obj.GmState,obj.sendTimes,obj.fid,obj.servername,obj.sendTimestr,obj.userName,obj.playerName,obj.subject,obj.content,obj.vipLevel,obj.lastGmId,obj.GmState,obj.career,obj.playerlevel,obj.countryname,obj.dialogs);
				 var newnum = document.getElementById(myfeedback.servername+'_new').innerHTML;
				 var sumnum = document.getElementById(myfeedback.servername+'_sum').innerHTML;
				 var nonum = document.getElementById(myfeedback.servername+'_no').innerHTML;		
				 var isshow = document.getElementById(myfeedback.fid);
				 if(isshow){
// 					 alert("出现已经有的未处理");
				 }else{
					 huizongnum = parseInt(huizongnum)+1;
					 huizongnum_sum = parseInt(huizongnum_sum) + 1;
					 document.getElementById('huizong').innerHTML = huizongnum;
					 document.getElementById('huizong_sum').innerHTML = huizongnum_sum;
					 document.getElementById(myfeedback.servername+'_no').innerHTML = parseInt(nonum)+parseInt(1);
					 document.getElementById(myfeedback.servername+'_sum').innerHTML = parseInt(sumnum)+parseInt(1);
					 scrollmess(myfeedback);
					 //判断是否add数组
					 var isadd = 0;
					 //处理完毕的消息，不参与排序,没有玩家的回复时间
					 if(obj.GmState==2||obj.GmState==0){
						 if(obj.servername){
							 for(var a=0;a<newmess.length;a++){
								 if(obj.sendTimes==newmess[a]){
									 isadd = 1;
								 }
							 }
							 if(isadd==0){
								 newmess.push(obj.sendTimes);
//		 						 alert("isadd==0刷新后长度："+newmess.length+"-状态:"+obj.GmState+"--时间:"+obj.sendTimes+"--subject:"+obj.subject);
							 }
							 
//		 					 alert("刷新后长度："+newmess.length+"-状态:"+obj.GmState+"--时间:"+obj.sendTimes+"--subject:"+obj.subject);
						 }
//		 				 alert("刷新qian长度："+newmess.length+"-server:"+obj.servername+"--sendTimes:"+obj.sendTimes+"--userName:"+obj.userName+"--subject:"+obj.subject);
					 }
					 if(newmess.length>0){
						 newmess.sort(delMess);
						 var newindex = 0;
						 for(var i=0;i<newmess.length;i++){
							 if(newmess[i]!=null&&newmess[i]==myfeedback.replytime){
								 newindex= i;
// 								 alert("GmState==2newindex:"+newindex);
								 break;
							 }
						 }
						 messIndex = parseInt(newindex)*2+1;
// 						 alert("GmState==0messIndex:"+messIndex);
					 }else{
					  	messIndex = 1;
					 }
					 
					 if(newmess.length>0){
// 						 alert("未处理tr："+messIndex);
						 replydate = myfeedback.replytime+"##"+myfeedback.servername;
// 						 alert("将要插入的行的位置是：" +nonum+",此时表的正确的总行数为:"+rowNum + ",实际的表的总行数为:"+acRownum);
						 var emptyTr=document.getElementById('msg').insertRow(messIndex);
						 
						 emptyTr.id = "trid"+myfeedback.fid;
						 var pUserName = myfeedback.username;
						 if(pUserName.indexOf("QQUSER")!=-1){
							 pUserName = pUserName.substring(0,6);
						 }
						 
						 emptyTr.innerHTML = "<td colspan='11' id='"+myfeedback.fid+"'><div style='display:none' id='isnewclose"+myfeedback.fid+"'>0</div><div id='arrindex1"+myfeedback.fid+"' style='display:none'>"+messIndex+"</div><div id='value"+myfeedback.fid+"' style='display:none'>"+nonum+"</div><div id='sendservernamediv"+myfeedback.fid+"' style='display:none'>"+myfeedback.servername+"</div><div id='senddiv"+myfeedback.fid+"' style='display:none'>"+myfeedback.fid+"</div><div id='opendiv"+myfeedback.fid+"' style=\"display:none\"><table style='background-color:#f8f6ff;width:500px;height:300px; border:4px solid #3399cc;'><tr><td><table><tr><div style='width:330px;height:200px;overflow-x:hidden;overflow-y:scroll; border:2px solid #3399cc;' id='record"+myfeedback.fid+"'>"+myfeedback.dialog+"</div></tr><tr><textarea id='senddialogid"+myfeedback.fid+"' style='width:330px;height:60px;'></textarea></tr><tr><input type='button' id='click' name='click' onclick=\"send('"+myfeedback.fid+"')\" value='发送'/><input type='button' id='close' name='close' onclick=\"closes("+myfeedback.state+",'"+myfeedback.fid+"','"+obj.sendTimes+"')\" value='关闭会话'/></tr></table></td><td width='170px' valign='top'><input type='button' id='qiangzhi'  name='qiangzhi"+myfeedback.fid+"'  onclick=\"closes('qzclose','"+myfeedback.fid+"','"+obj.sendTimes+"')\" value='手动关闭'/><br>服务器："+myfeedback.servername+"<br>国家："+myfeedback.country+"<br>账 号："+myfeedback.username+"<br>角色名："+myfeedback.name+"<br>职业："+myfeedback.career+"<br>等级："+myfeedback.level+"</td></tr></table></div></td>";
						 document.getElementById('isnewclose'+myfeedback.fid).innerHTML = 0;
						 var x=document.getElementById('msg').insertRow(messIndex);
						 
						 x.id = "trxid"+myfeedback.fid;
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
						 var j=x.insertCell(10);
						 
						 y.innerHTML=myfeedback.servername;
						 r.innerHTML=myfeedback.channel;
						 z.innerHTML="<div id='date"+myfeedback.fid+"'>"+myfeedback.date+"</div>";
						 b.innerHTML=pUserName;
						 c.innerHTML=myfeedback.name;
						 if(myfeedback.title!=null){
							d.innerHTML = "<a href=\"javascript:openDialogue('"+myfeedback.fid+"')\">"+myfeedback.title+"</a>";
						 }
						 if(myfeedback.content!=null){
							 if(myfeedback.content.length>10){
								 e.innerHTML=myfeedback.content.substring(0,10)+"......";
							 }else{
								 e.innerHTML=myfeedback.content;
							 }
						 }

						 if(myfeedback.vip==1){
							 f.innerHTML="<img src='../../images/v-01.png' width='30' height='30' title='1.体力最多可购买1次；2.增加1次喝酒次数；3.增加1次屠魔贴;4.开启随身修理'>";
						 }else if(myfeedback.vip==2){
							 f.innerHTML="<img src='../../images/v-02.png' width='30' height='30' title='1.体力最多可购买6次；2.开启随身邮件；3.增加每日绑银使用上限200两；4.增加1次额外经验种植机会；5.包含VIP1所有功能.'>";
						 }else if(myfeedback.vip==3){
							 f.innerHTML="<img src='../../images/v-03.png' width='30' height='30' title='1.增加2次喝酒次数；2.增加2次屠魔贴;3.开启随身仓库;4.获得1次额外摇钱树种植机会;5.可以使用中级炼妖石;6.可书安心新境界6星以上杂念任务;7.包含VIP2所有功能.'>";
						 }else if(myfeedback.vip==4){
							 f.innerHTML="<img src='../../images/v-04.png' width='30' height='30' title='1.最多可购买14次体力；2.开启随身求购;3.可用银子书安心境界杂念任务;4.增加每日绑银使用上限500两；5.可使用仙府鬼斧神工令;6.押镖多一次加货机会；7.幽冥幻域每日最多可刷新3次;8.包含VIP3所有功能.'>";
						 }else if(myfeedback.vip==5){
							 f.innerHTML="<img src='../../images/v-05.png' width='30' height='30' title='1.增加3次喝酒次数；2.增加3次屠魔贴;3.可以用元宝捐献家族资金换取贡献度;4.幽冥幻域获得经验奖励提升50%；5.可以使用高级炼妖石;6获得2次额外摇钱树种植机会;6.包含VIP4所有功能.'>";
						 }else if(myfeedback.vip==6){
							 f.innerHTML="<img src='../../images/v-06.png' width='30' height='30' title='1.最多可购买24次体力；2.幽冥幻域获得经验奖励提升100%；3.增加每日绑银使用上限1锭；4.增加2次额外经验种植机会；5.幽冥幻域每日最多可刷新4次;6.包含VIP5所有功能.'>";
						 }else if(myfeedback.vip==7){
							 f.innerHTML="<img src='../../images/v-07.png' width='30' height='30' title='1.增加4次喝酒次数；2.增加4次屠魔贴；3.包含VIP6所有功能.'>";
						 }else if(myfeedback.vip==8){
							 f.innerHTML="<img src='../../images/v-08.png' width='30' height='30' title='1.体力最多可购买36次；2.包含VIP7所有功能.'>";
						 }else if(myfeedback.vip==9){
							 f.innerHTML="<img src='../../images/v-09.png' width='30' height='30' title='1.增加5次喝酒次数；2.增加5次屠魔贴；3.包含VIP8所有功能.'>";
						 }else if(myfeedback.vip==10){
							 f.innerHTML="<img src='../../images/v-10.png' width='30' height='30' title='1.体力最多可购买48次;2.包含VIP9所有功能.'/>";
						 }else{
							 f.innerHTML="0";
						 }

						 if(myfeedback.gm){
							 g.innerHTML="<div id='recorder"+myfeedback.fid+"'>"+myfeedback.gm+"</div>";
						 }else{
							 g.innerHTML="<div id='recorder"+myfeedback.fid+"'>无</div>";
						 }	
						 
						 if(myfeedback.state==0){
							 h.innerHTML="<div id='gmstat"+myfeedback.fid+"'><font color='red'>新消息</div>";
						 }else if(myfeedback.state==2){
							 h.innerHTML="<div id='gmstat"+myfeedback.fid+"'><font color='green'>新</div>";
						 }else if(myfeedback.state==1||myfeedback.state==3){
							 h.innerHTML="<font color='black'>其他";
						 }
						 j.innerHTML = "<input type='button' onclick=\"addquestion('"+obj.fid+"','"+obj.servername+"','"+obj.channel+"','"+obj.userName+"','"+obj.playerName+"','"+obj.vipLevel+"')\" value='添加'/><input type='button' onclick='queryquestion()' value='查找'/>";		
						 document.getElementById('title1').innerHTML = obj.sendTimestr;
					 }
					 
					 
				 }
				 
			 }else if(obj.GmState==1&&obj.servername){
				 var myfeedback = new feedback(obj.channel,obj.GmState,obj.sendTimes,obj.fid,obj.servername,obj.sendTimestr,obj.userName,obj.playerName,obj.subject,obj.content,obj.vipLevel,obj.lastGmId,obj.GmState,obj.career,obj.playerlevel,obj.countryname,obj.dialogs);
				 var newnum = document.getElementById(myfeedback.servername+'_new').innerHTML;
				 var sumnum = document.getElementById(myfeedback.servername+'_sum').innerHTML;
				 var nonum = document.getElementById(myfeedback.servername+'_no').innerHTML;
				 var isshow = document.getElementById(myfeedback.fid);
				 if(isshow!=null){
					 //
					 var gmstat1 = document.getElementById('gmstat'+myfeedback.fid).innerHTML;
					 if(gmstat1.indexOf('待处理')!=-1){
						 var isnewclose = document.getElementById('isnewclose'+myfeedback.fid).innerHTML;
						 if(isnewclose==0){
							 huizongnum_ = parseInt(huizongnum_)-1;
							 document.getElementById('huizong_new').innerHTML = huizongnum_;
							 document.getElementById(myfeedback.servername+'_new').innerHTML = parseInt(newnum)-parseInt(1);
							 
							 document.getElementById('isnewclose'+myfeedback.fid).innerHTML = "1";
						 }
					 }else if(gmstat1.indexOf('新消息')!=-1){
						 huizongnum = parseInt(huizongnum)-1;
						 document.getElementById('huizong').innerHTML = huizongnum;
						 document.getElementById(myfeedback.servername+'_no').innerHTML = parseInt(nonum)-parseInt(1);
						 
					 }
					 
					 document.getElementById('gmstat'+myfeedback.fid).innerHTML = "已回复";
					 document.getElementById('recorder'+myfeedback.fid).innerHTML = myfeedback.gm;
					 document.getElementById('record'+myfeedback.fid).innerHTML = myfeedback.dialog;	
					 document.getElementById('date'+myfeedback.fid).innerHTML = myfeedback.date;
					 document.getElementById('record'+myfeedback.fid).scrollTop = document.getElementById('record'+myfeedback.fid).scrollHeight;
				 }
				 
			 }
		}
		
	}
	
	//当GM恢复成功，会回调这个函数
	function closes(mess,index,time){
		var gmstat1 = document.getElementById('gmstat'+index).innerHTML;
		//
		var gmid = document.getElementById('gmid').innerHTML;
		//反馈--id
		var sendFeedbackId = document.getElementById('senddiv'+index).innerHTML; 
		
		var fservername = document.getElementById('sendservernamediv'+index).innerHTML;	
		var newnum = document.getElementById(fservername+'_new').innerHTML;
		 var sumnum = document.getElementById(fservername+'_sum').innerHTML;
		 var nonum = document.getElementById(fservername+'_no').innerHTML;
		if(mess=="qzclose"){
		    var gmreply = "感谢您对《飘渺寻仙曲》的支持，祝您游戏愉快!!";
			//反馈--服务器
			if(gmstat1.indexOf('新消息')!=-1){
				alert("亲爱的GM，未处理状态的消息不能关闭，请用关闭会话！");
			}	
			else if(gmstat1.indexOf('待处理')!=-1||gmstat1.indexOf('已回复')!=-1)
			{
				if(gmreply){
					if(gmreply.length<300){
						
						 huizongnum_sum = parseInt(huizongnum_sum)-1;
						 document.getElementById('huizong_sum').innerHTML = huizongnum_sum;
						 if(gmstat1.indexOf('待处理')!=-1){
							 huizongnum_ = parseInt(huizongnum_)-1;
							 document.getElementById('huizong_new').innerHTML = huizongnum_;
							 document.getElementById(fservername+'_new').innerHTML = parseInt(newnum)-parseInt(1);
						 }
						 
						 document.getElementById(fservername+'_sum').innerHTML = parseInt(sumnum)-parseInt(1);
						url = "";
							if (selectedservers.length>0)
							{
								for(var i=0;i<selectedservers.length;i++){
									//发送
									var url = selectedservers[i].value+"/sendMessage.jsp";
									var aa = document.getElementById("senddid");
									if(aa){
										aa.parentNode.removeChild(aa);
										for (var prop in aa) { 
											delete aa[prop]; 
										}
									}
				
									var tagHead = document.getElementsByTagName("head")[0];
									var s = document.createElement("script");
									s.type = "text/javascript";
									s.src = url+"?gmreply="+gmreply+"&gmid="+gmid+"&sendFeedbackId="+sendFeedbackId+"&authorize.username=gmapp&authorize.password=gmapp321";
									s.id = "senddid";
									tagHead.appendChild(s);
									
									//关闭
									var url2 = selectedservers[i].value+"/closeMessage.jsp";
									var aa2 = document.getElementById("closeid");
									if(aa2){
										aa2.parentNode.removeChild(aa2);
										for (var prop in aa2) { 
											delete aa2[prop]; 
										}
									}
// 									alert("mess:"+mess+"url2:"+url2);
									var tagHead2 = document.getElementsByTagName("head")[0];
									var s2 = document.createElement("script");
									s2.type = "text/javascript";
									s2.src = url2+"?closemess=closemess&gmid="+gmid+"&sendFeedbackId="+sendFeedbackId+"&authorize.username=gmapp&authorize.password=gmapp321";
									s2.id = "closeid";
									tagHead.appendChild(s2);
								}
								
							}
					}else{
						alert("不能超过300字！！");
					}
					
					
				}else{
					alert("不能回复玩家空内容！！");
				}
				 for(var i=0;i<newmess.length;i++){
					 if(time==newmess[i]){
						 newmess.splice(i, 1);
					 }
				 }
				 
				var tr = document.getElementById('trid'+index);
				 var trx = document.getElementById('trxid'+index);
				 tr.parentNode.removeChild(tr);
				 trx.parentNode.removeChild(trx);
			}
			
			//
			
		}else if(mess==0 || mess==2){
			 var wait = document.getElementById('gmstat'+index).innerHTML;
			 if(wait=="已回复"){
// 				 var gmreply = document.getElementById('senddialogid'+index).value;
				 huizongnum_sum = parseInt(huizongnum_sum)-1;
				 document.getElementById('huizong_sum').innerHTML = huizongnum_sum;					 
				 document.getElementById(fservername+'_sum').innerHTML = parseInt(sumnum)-parseInt(1);
				    //删除浏览器的记录，删除server缓存记录
					
				 for(var i=0;i<newmess.length;i++){
					 if(time==newmess[i]){
						 newmess.splice(i, 1);
					 }
				 }
				 
				 var tr = document.getElementById('trid'+index);
				 var trx = document.getElementById('trxid'+index);
				 tr.parentNode.removeChild(tr);
				 trx.parentNode.removeChild(trx);
				 
				 
			 }else{
				 document.getElementById('opendiv'+index).style.display="none";
			 }
			
		}else if(mess==1){
			alert("状态是等待处理");
			
		}else if(mess=="closesucc"){
			//现在有这样的一个问题
		}
		
	}
	
	
	function send(index){
		//gmId
		var gmid = document.getElementById('gmid').innerHTML;
		//反馈--id
		var sendFeedbackId = document.getElementById('senddiv'+index).innerHTML; 
		//反馈--内容
	    var gmreply = document.getElementById('senddialogid'+index).value;
		//反馈--服务器
		var fservername = document.getElementById('sendservernamediv'+index).innerHTML;	
		
		var gmstat = document.getElementById('gmstat'+index).innerHTML;
		
		if(gmreply){
// 			alert("gmid:"+gmid+"sendFeedbackId:"+sendFeedbackId+"gmreply:"+gmreply+"fservername:"+fservername);
			if(gmreply.length<300){
				url = "";
					if (selectedservers.length>0)
					{
						for(var i=0;i<selectedservers.length;i++){
							var url = selectedservers[i].value+"/sendMessage.jsp";
							var aa = document.getElementById("sendid");
							if(aa){
								aa.parentNode.removeChild(aa);
								for (var prop in aa) { 
									delete aa[prop]; 
								}
							}
		
							var tagHead = document.getElementsByTagName("head")[0];
							var s = document.createElement("script");
							s.type = "text/javascript";
							s.src = url+"?gmreply="+gmreply+"&gmid="+gmid+"&sendFeedbackId="+sendFeedbackId+"&authorize.username=gmapp&authorize.password=gmapp321";
							s.id = "sendid";
							tagHead.appendChild(s);
						}
							
					}
			}else{
				alert("不能超过300字！！");
			}
			
			
		}else{
			alert("您已经回复过，请等待玩家回复,不建议您分批发送多条消息来答复玩家，建议玩家的每次提问我们都能有一条完整的解答信息去回复!");
		}

	}
	
// 	var is3name = new Array();
	function sure(){
		
		//返回给各个服务器的时间和服务器
		var backservertime = 0;
// 		var backservername = 0;先取消服务器判断2秒刷新一次不会丢太多数据，丢的数据下次页面刷新会有
		var strs= new Array();
		if(replydate!=0){
			strs = replydate.split("##");
			for (i=0;i<strs.length;i++){    
				backservertime = strs[0];
// 				backservername = strs[1];
		    } 
		}
		url="";
		
			if (selectedservers.length>0)
			{
				for(var i=0;i<selectedservers.length;i++){
					var url = selectedservers[i].value+"/getMessage.jsp";
					var id = selectedservers[i].value;
					var aa = document.getElementById(id);
					if(aa){
						aa.parentNode.removeChild(aa);
						for (var prop in aa) { 
							delete aa[prop]; 
						}
					}
					var tagHead = document.getElementsByTagName("head")[0];
					var s = document.createElement("script");
					s.type = "text/javascript";
					s.src = url+"?cb=cb&replydate="+backservertime+"&authorize.username=gmapp&authorize.password=gmapp321";
					s.id = id;
					tagHead.appendChild(s);
				}
			}
		
	}
	
	function allcheaked(){ 
		var a = document.getElementsByTagName("input"); 
		if(a[0].checked==true){ 
			for (var i=0; i<a.length; i++) {
				if (a[i].type == "checkbox") a[i].checked = false; 
			}
			document.getElementById('allche').value = "全选"; 
		}else { 
			for (var i=0; i<a.length; i++) {
				if (a[i].type == "checkbox") a[i].checked = true;
			}
			document.getElementById('allche').value = "取消全选"; 
		} 
	} 

	 function scrollmess(obj){
		 if(obj){
		     document.getElementById('d1').innerHTML = "<marquee  loop='1'  height='25' width='100%'  bgcolor='#ffaaaa'>"+obj.title+"&nbsp;,&nbsp;"+obj.content+"&nbsp;,&nbsp;("+obj.servername+")&nbsp;,&nbsp;("+obj.date+")</marquee>";	 
		 }
	 } 
	 

	 //
	
function $(tag){
    return document.getElementById(tag);
 }
 
	function tohide(){
		var ishide = document.getElementById('con').style.display;
		if(ishide=="none"){
			document.getElementById('con').style.display = "block";
			document.getElementById('tohideid').value = "隐藏";
		}else if(ishide=="block"){
			document.getElementById('con').style.display = "none";
			document.getElementById('tohideid').value = "显示";
		}
		
	}
	
   function displaynone(){
		document.getElementById('serverStat').style.display = "none";   
		document.getElementById('yinc').style.display = "none"; 
		document.getElementById('xianshi').style.display = "block";
   }
   
   function displayblock(){
	   document.getElementById('serverStat').style.display = "block";  
	   document.getElementById('xianshi').style.display = "none";
	   document.getElementById('yinc').style.display = "block"; 
   }
	   
</script>

<body bgcolor="#c8edcc">


<form>
	
	
	
	<% 
		
		out.print("<div id ='d1'></div>");
		Object obj = session.getAttribute("authorize.username");
		String gmid = "gm";
		//
		
		out.print("<b>你好:"+obj+",欢迎您来处理问题！</b>");
		out.print("<hr>");
		out.print("<img src='images/xianshi.jpg' id='xianshi' width='30' height='30' onclick='displayblock()' style='display:none' title='查看所选服务器状态数量'>");
		out.print("<img src='images/yinc.jpg' id='yinc' width='30' height='30' onclick='displaynone()' style='display:block' title='隐藏所选服务器状态数量'>");
	 	out.print("<div><table id='serverStat'>");
	 	out.print("<tr><th>状态-服务器</th><th>云波鬼岭</th><th>黄金海岸</th><th>蓬莱仙阁</th><th>乾元金光</th><th>太虚幻境</th><th>幽冥山谷</th><th>金霞玉鼎</th><th>海天佛国</th><th>普陀潮音</th><th>炼狱绝地</th><th>太华神山</th></tr>");
	 	out.print("<th>新消息</th><td id='云波鬼岭_no'>0</td><td id='黄金海岸_no'>0</td><td id='蓬莱仙阁_no'>0</td><td id='乾元金光_no'>0</td><td id='太虚幻境_no'>0</td><td id='幽冥山谷_no'>0</td><td id='金霞玉鼎_no'>0</td><td id='海天佛国_no'>0</td><td id='普陀潮音_no'>0</td><td id='炼狱绝地_no'>0</td><td id='太华神山_no'>0</td></tr>");
	 	out.print("<th>待处理</th><td id='云波鬼岭_new'>0</td><td id='黄金海岸_new'>0</td><td id='蓬莱仙阁_new'>0</td><td id='乾元金光_new'>0</td><td id='太虚幻境_new'>0</td><td id='幽冥山谷_new'>0</td><td id='金霞玉鼎_new'>0</td><td id='海天佛国_new'>0</td><td id='普陀潮音_new'>0</td><td id='炼狱绝地_new'>0</td><td id='太华神山_new'>0</td></tr>");
	 	out.print("<th>总数</th><td id='云波鬼岭_sum'>0</td><td id='黄金海岸_sum'>0</td><td id='蓬莱仙阁_sum'>0</td><td id='乾元金光_sum'>0</td><td id='太虚幻境_sum'>0</td><td id='幽冥山谷_sum'>0</td><td id='金霞玉鼎_sum'>0</td><td id='海天佛国_sum'>0</td><td id='普陀潮音_sum'>0</td><td id='炼狱绝地_sum'>0</td><td id='太华神山_sum'>0</td></tr>");
	 	out.print("<tr><th>状态-服务器</th><th>鹊桥仙境</th><th>瑶台仙宫</th><th>紫阳青峰</th><th>西方灵山</th><th>飞瀑龙池</th><th>玉幡宝刹</th><th>问天灵台</th><th>云海冰岚</th><th>无极冰原</th><th>东海龙宫</th><th>燃烧圣殿</th></tr>");
	 	out.print("<th>新消息</th><td id='鹊桥仙境_no'>0</td><td id='瑶台仙宫_no'>0</td><td id='紫阳青峰_no'>0</td><td id='西方灵山_no'>0</td><td id='飞瀑龙池_no'>0</td><td id='玉幡宝刹_no'>0</td><td id='问天灵台_no'>0</td><td id='云海冰岚_no'>0</td><td id='无极冰原_no'>0</td><td id='东海龙宫_no'>0</td><td id='燃烧圣殿_no'>0</td></tr>");
	 	out.print("<th>待处理</th><td id='鹊桥仙境_new'>0</td><td id='瑶台仙宫_new'>0</td><td id='紫阳青峰_new'>0</td><td id='西方灵山_new'>0</td><td id='飞瀑龙池_new'>0</td><td id='玉幡宝刹_new'>0</td><td id='问天灵台_new'>0</td><td id='云海冰岚_new'>0</td><td id='无极冰原_new'>0</td><td id='东海龙宫_new'>0</td><td id='燃烧圣殿_new'>0</td></tr>");
	 	out.print("<th>总数</th><td id='鹊桥仙境_sum'>0</td><td id='瑶台仙宫_sum'>0</td><td id='紫阳青峰_sum'>0</td><td id='西方灵山_sum'>0</td><td id='飞瀑龙池_sum'>0</td><td id='玉幡宝刹_sum'>0</td><td id='问天灵台_sum'>0</td><td id='云海冰岚_sum'>0</td><td id='无极冰原_sum'>0</td><td id='东海龙宫_sum'>0</td><td id='燃烧圣殿_sum'>0</td></tr>");
	 	out.print("<tr><th>状态-服务器</th><th>福地洞天</th><th>峨嵋金顶</th><th>风雪之巅</th><th>桃源仙境</th><th>飘渺王城</th><th>千年古城</th><th>昆仑圣殿</th><th>北冥佛光</th><th>崆峒灵宝</th><th>雪域冰城</th><th>百花深谷</th> </tr>");
	 	out.print("<th>新消息</th><td id='福地洞天_no'>0</td><td id='峨嵋金顶_no'>0</td><td id='风雪之巅_no'>0</td><td id='桃源仙境_no'>0</td><td id='飘渺王城_no'>0</td><td id='千年古城_no'>0</td><td id='昆仑圣殿_no'>0</td><td id='北冥佛光_no'>0</td><td id='崆峒灵宝_no'>0</td><td id='雪域冰城_no'>0</td><td id='百花深谷_no'>0</td></tr>");
	 	out.print("<th>待处理</th><td id='福地洞天_new'>0</td><td id='峨嵋金顶_new'>0</td><td id='风雪之巅_new'>0</td><td id='桃源仙境_new'>0</td><td id='飘渺王城_new'>0</td><td id='千年古城_new'>0</td><td id='昆仑圣殿_new'>0</td><td id='北冥佛光_new'>0</td><td id='崆峒灵宝_new'>0</td><td id='雪域冰城_new'>0</td><td id='百花深谷_new'>0</td></tr>");
	 	out.print("<th>总数</th><td id='福地洞天_sum'>0</td><td id='峨嵋金顶_sum'>0</td><td id='风雪之巅_sum'>0</td><td id='桃源仙境_sum'>0</td><td id='飘渺王城_sum'>0</td><td id='千年古城_sum'>0</td><td id='昆仑圣殿_sum'>0</td><td id='北冥佛光_sum'>0</td><td id='崆峒灵宝_sum'>0</td><td id='雪域冰城_sum'>0</td><td id='百花深谷_sum'>0</td></tr>");
	 	out.print("<tr><th>状态-服务器</th><th>七宝莲台</th><th>万佛朝宗</th><th>东极仙岛</th><th>白露横江</th><th>瀛台仙山</th><th>摘星楼台</th><th>明空梵天</th><th>无量净土</th><th>左岸花海</th><th>幽灵山庄</th><th>金宫银坊</th></tr>");
	 	out.print("<th>新消息</th><td id='七宝莲台_no'>0</td><td id='万佛朝宗_no'>0</td><td id='东极仙岛_no'>0</td><td id='白露横江_no'>0</td><td id='瀛台仙山_no'>0</td><td id='摘星楼台_no'>0</td><td id='明空梵天_no'>0</td><td id='无量净土_no'>0</td><td id='左岸花海_no'>0</td><td id='幽灵山庄_no'>0</td><td id='金宫银坊_no'>0</td></tr>");
	 	out.print("<th>待处理</th><td id='七宝莲台_new'>0</td><td id='万佛朝宗_new'>0</td><td id='东极仙岛_new'>0</td><td id='白露横江_new'>0</td><td id='瀛台仙山_new'>0</td><td id='摘星楼台_new'>0</td><td id='明空梵天_new'>0</td><td id='无量净土_new'>0</td><td id='左岸花海_new'>0</td><td id='幽灵山庄_new'>0</td><td id='金宫银坊_new'>0</td></tr>");
	 	out.print("<th>总数</th><td id='七宝莲台_sum'>0</td><td id='万佛朝宗_sum'>0</td><td id='东极仙岛_sum'>0</td><td id='白露横江_sum'>0</td><td id='瀛台仙山_sum'>0</td><td id='摘星楼台_sum'>0</td><td id='明空梵天_sum'>0</td><td id='无量净土_sum'>0</td><td id='左岸花海_sum'>0</td><td id='幽灵山庄_sum'>0</td><td id='金宫银坊_sum'>0</td></tr>");
		out.print("<tr><th>状态-服务器</th><th>斩龙神台</th><th>裂风峡谷</th><th>冲霄云楼</th><th>遨游四海</th><th>凌霄宝殿</th><th>傲视三界</th><th>王者之域</th><th>惊天战神</th><th>右道长亭</th><th>霸气纵横</th><th>独战群神</th></tr>");
	 	out.print("<th>新消息</th><td id='斩龙神台_no'>0</td><td id='裂风峡谷_no'>0</td><td id='冲霄云楼_no'>0</td><td id='遨游四海_no'>0</td><td id='凌霄宝殿_no'>0</td><td id='傲视三界_no'>0</td><td id='王者之域_no'>0</td><td id='惊天战神_no'>0</td><td id='右道长亭_no'>0</td><td id='霸气纵横_no'>0</td><td id='独战群神_no'>0</td> </tr>");
	 	out.print("<th>待处理</th><td id='斩龙神台_new'>0</td><td id='裂风峡谷_new'>0</td><td id='冲霄云楼_new'>0</td><td id='遨游四海_new'>0</td><td id='凌霄宝殿_new'>0</td><td id='傲视三界_new'>0</td><td id='王者之域_new'>0</td><td id='惊天战神_new'>0</td><td id='右道长亭_new'>0</td><td id='霸气纵横_new'>0</td><td id='独战群神_new'>0</td> </tr>");
	 	out.print("<th>总数</th><td id='斩龙神台_sum'>0</td><td id='裂风峡谷_sum'>0</td><td id='冲霄云楼_sum'>0</td><td id='遨游四海_sum'>0</td><td id='凌霄宝殿_sum'>0</td><td id='傲视三界_sum'>0</td><td id='王者之域_sum'>0</td><td id='惊天战神_sum'>0</td><td id='右道长亭_sum'>0</td><td id='霸气纵横_sum'>0</td><td id='独战群神_sum'>0</td> </tr>");
	 	out.print("<tr><th>状态-服务器</th><th>霸气乾坤</th><th>岚境仙音</th><th>傲剑凌云</th><th>九霄之巅</th><th>龙脉之地</th><th>伏魔幻境</th><th>游龙引凤</th><th>百炼成仙</th><th>烟雨青山</th><th>六道轮回</th><th>永安仙城</th> </tr>");
	 	out.print("<th>新消息</th><td id='霸气乾坤_no'>0</td><td id='岚境仙音_no'>0</td><td id='傲剑凌云_no'>0</td><td id='九霄之巅_no'>0</td><td id='龙脉之地_no'>0</td><td id='伏魔幻境_no'>0</td><td id='游龙引凤_no'>0</td><td id='百炼成仙_no'>0</td><td id='烟雨青山_no'>0</td><td id='六道轮回_no'>0</td><td id='永安仙城_no'>0</td></tr>");
	 	out.print("<th>待处理</th><td id='霸气乾坤_new'>0<td id='岚境仙音_new'>0</td><td id='傲剑凌云_new'>0</td><td id='九霄之巅_new'>0</td><td id='龙脉之地_new'>0</td><td id='伏魔幻境_new'>0</td></td><td id='游龙引凤_new'>0</td><td id='百炼成仙_new'>0</td><td id='烟雨青山_new'>0</td><td id='六道轮回_new'>0</td><td id='永安仙城_new'>0</td></tr>");
	 	out.print("<th>总数</th><td id='霸气乾坤_sum'>0<td id='岚境仙音_sum'>0</td><td id='傲剑凌云_sum'>0</td><td id='九霄之巅_sum'>0</td><td id='龙脉之地_sum'>0</td><td id='伏魔幻境_sum'>0</td></td><td id='游龙引凤_sum'>0</td><td id='百炼成仙_sum'>0</td><td id='烟雨青山_sum'>0</td><td id='六道轮回_sum'>0</td><td id='永安仙城_sum'>0</td></tr>");
	 	out.print("<tr><th>状态-服务器</th><th>幽恋蝶谷</th><th>百花仙谷</th><th>太极玄天</th><th>龙飞凤舞</th><th>狂龙魔窟</th><th>金蛇圣殿</th><th>通天浮屠</th><th>日光峡谷</th><th>霹雳霞光</th><th>逍遥神殿</th><th>仙界至尊</th></tr>");
	 	out.print("<th>新消息</th><td id='幽恋蝶谷_no'>0</td><td id='百花仙谷_no'>0</td><td id='太极玄天_no'>0</td><td id='龙飞凤舞_no'>0</td><td id='狂龙魔窟_no'>0</td><td id='金蛇圣殿_no'>0</td><td id='通天浮屠_no'>0</td><td id='日光峡谷_no'>0</td><td id='霹雳霞光_no'>0</td><td id='逍遥神殿_no'>0</td><td id='仙界至尊_no'>0</td></tr>");
	 	out.print("<th>待处理</th><td id='幽恋蝶谷_new'>0</td><td id='百花仙谷_new'>0</td><td id='太极玄天_new'>0</td><td id='龙飞凤舞_new'>0</td><td id='狂龙魔窟_new'>0</td><td id='金蛇圣殿_new'>0</td><td id='通天浮屠_new'>0</td><td id='日光峡谷_new'>0</td><td id='霹雳霞光_new'>0</td><td id='逍遥神殿_new'>0</td><td id='仙界至尊_new'>0</td></tr>");
	 	out.print("<th>总数</th><td id='幽恋蝶谷_sum'>0</td><td id='百花仙谷_sum'>0</td><td id='太极玄天_sum'>0</td><td id='龙飞凤舞_sum'>0</td><td id='狂龙魔窟_sum'>0</td><td id='金蛇圣殿_sum'>0</td><td id='通天浮屠_sum'>0</td><td id='日光峡谷_sum'>0</td><td id='霹雳霞光_sum'>0</td><td id='逍遥神殿_sum'>0</td><td id='仙界至尊_sum'>0</td></tr>");
	 
	 	out.print("<tr><th>状态-服务器</th><th>仙山琼阁</th><th>四海之巅</th><th>纵横天下</th><th>对酒当歌</th><th>叱咤风云</th><th>雄霸天下</th><th>壮气凌云</th><th>霸气无双</th><th>江山如画</th><th>鹏程万里</th><th>碧海青天</th></tr>");
	 	out.print("<th>新消息</th><td id='仙山琼阁_no'>0</td><td id='四海之巅_no'>0</td><td id='纵横天下_no'>0</td><td id='对酒当歌_no'>0</td><td id='叱咤风云_no'>0</td><td id='雄霸天下_no'>0</td><td id='壮气凌云_no'>0</td><td id='霸气无双_no'>0</td><td id='江山如画_no'>0</td><td id='鹏程万里_no'>0</td><td id='碧海青天_no'>0</td></tr>");
	 	out.print("<th>待处理</th><td id='仙山琼阁_new'>0</td><td id='四海之巅_new'>0</td><td id='纵横天下_new'>0</td><td id='对酒当歌_new'>0</td><td id='叱咤风云_new'>0</td><td id='雄霸天下_new'>0</td><td id='壮气凌云_new'>0</td><td id='霸气无双_new'>0</td><td id='江山如画_new'>0</td><td id='鹏程万里_new'>0</td><td id='碧海青天_new'>0</td></tr>");
	 	out.print("<th>总数</th><td id='仙山琼阁_sum'>0</td><td id='四海之巅_sum'>0</td><td id='纵横天下_sum'>0</td><td id='对酒当歌_sum'>0</td><td id='叱咤风云_sum'>0</td><td id='雄霸天下_sum'>0</td><td id='壮气凌云_sum'>0</td><td id='霸气无双_sum'>0</td><td id='江山如画_sum'>0</td><td id='鹏程万里_sum'>0</td><td id='碧海青天_sum'>0</td></tr>");
	 	out.print("<tr><th>状态-服务器</th><th>月满乾坤</th><th>仙山云海</th><th>独霸一方</th><th>侠骨柔肠</th><th>唯我独尊</th><th>勇者无敌</th><th>凌霜傲雪</th><th>华山之巅</th><th>谁与争锋</th><th>傲视群雄</th><th>独步天下</th> </tr>");
	 	out.print("<th>新消息</th><td id='月满乾坤_no'>0</td><td id='仙山云海_no'>0</td><td id='独霸一方_no'>0</td><td id='侠骨柔肠_no'>0</td><td id='唯我独尊_no'>0</td><td id='勇者无敌_no'>0</td><td id='凌霜傲雪_no'>0</td><td id='华山之巅_no'>0</td><td id='谁与争锋_no'>0</td><td id='傲视群雄_no'>0</td><td id='独步天下_no'>0</td> </tr>");
	 	out.print("<th>待处理</th><td id='月满乾坤_new'>0</td><td id='仙山云海_new'>0</td><td id='独霸一方_new'>0</td><td id='侠骨柔肠_new'>0</td><td id='唯我独尊_new'>0</td><td id='勇者无敌_new'>0</td><td id='凌霜傲雪_new'>0</td><td id='华山之巅_new'>0</td><td id='谁与争锋_new'>0</td><td id='傲视群雄_new'>0</td><td id='独步天下_new'>0</td>  </tr>");
	 	out.print("<th>总数</th><td id='月满乾坤_sum'>0</td><td id='仙山云海_sum'>0</td><td id='独霸一方_sum'>0</td><td id='侠骨柔肠_sum'>0</td><td id='唯我独尊_sum'>0</td><td id='勇者无敌_sum'>0</td><td id='凌霜傲雪_sum'>0</td><td id='华山之巅_sum'>0</td><td id='谁与争锋_sum'>0</td><td id='傲视群雄_sum'>0</td><td id='独步天下_sum'>0</td> </tr>");
	 	out.print("<tr><th>状态-服务器</th><th>逆转乾坤</th><th>问鼎江湖</th><th>仙岛秘境</th><th>拨云揽月</th><th>神龙摆尾</th><th>飞龙在天</th><th>笑傲江湖</th><th>独闯天涯</th><th>马踏千里</th><th>龙啸九州</th><th>鱼跃龙门</th> <td><B><font size='3px' color='red'>汇总</font></B></td></tr>");
	 	out.print("<th>新消息</th><td id='逆转乾坤_no'>0</td><td id='问鼎江湖_no'>0</td><td id='仙岛秘境_no'>0</td><td id='拨云揽月_no'>0</td><td id='神龙摆尾_no'>0</td><td id='飞龙在天_no'>0</td><td id='笑傲江湖_no'>0</td><td id='独闯天涯_no'>0</td><td id='马踏千里_no'>0</td><td id='龙啸九州_no'>0</td><td id='鱼跃龙门_no'>0</td> <td id='huizong'></td></tr>");
	 	out.print("<th>待处理</th><td id='逆转乾坤_new'>0</td><td id='问鼎江湖_new'>0</td><td id='仙岛秘境_new'>0</td><td id='拨云揽月_new'>0</td><td id='神龙摆尾_no'>0</td><td id='飞龙在天_new'>0</td><td id='笑傲江湖_new'>0</td><td id='独闯天涯_new'>0</td><td id='马踏千里_new'>0</td><td id='龙啸九州_new'>0</td><td id='鱼跃龙门_new'>0</td>  <td id='huizong_new'></td></tr>");
	 	out.print("<th>总数</th><td id='逆转乾坤_sum'>0</td><td id='问鼎江湖_sum'>0</td><td id='仙岛秘境_sum'>0</td><td id='拨云揽月_sum'>0</td><td id='神龙摆尾_no'>0</td><td id='飞龙在天_sum'>0</td><td id='笑傲江湖_sum'>0</td><td id='独闯天涯sum'>0</td><td id='马踏千里_sum'>0</td><td id='龙啸九州_sum'>0</td><td id='鱼跃龙门_sum'>0</td> <td id='huizong_sum'></td></tr>");
	 	out.print("</table></div>");
	 	out.print("<table width='90%' id='mytable'>");
	 	
	 	if(obj!=null){
	 		String gmname = obj.toString();
	 		Map<String,String> gmNameIds = new HashMap<String,String>();
	 		String [] gmnames = {"wangyugang","吉雅泰","wangsiyu","zhangjunchen","wenguosi","yuanyonghong","panju","chentao","wanyu","panzhichao",
	 				"gaopeng","wangwei","yuyang","lijinlong","makang","yaoyuan","yangjinlong","qichongyang","xukuiling","liuyaoqiang",
	 				"quyunjun","liuzhaodong","taoshen","yangxiaoliang","liyang","lihao","ximeng","wangjiangang","hanqirui","caomengjie",
	 				"bianhaibo","niezhigang","chenhongyuan","xiaoyang","yanxiaoqin","liutao","nizhenning","mengting","wangtianxin","huangqing","sunmenglong",
	 				"yaotengfei","lilinfeng","quhaotian","zhangqi","yangjie","goumeng","liwei","jiaohailiang","jiazhiyu","tanghaiping","gepeng"
	 				,"shangwei","yangboxiong","luotao","dengshaoyu","limengting","wanglong","dongjunhao","liukai","liuyu","zhangjitong","chenmingyu","peishengran","cuixiaomeng","zhaopengyu","yangzhao","chenxiong","chenhao","sufang","zhangjian","liuchuan","liuyanga","fanlei","liushengyue","zhouying","zhurui","zhenganqi","lihuiqin","guojing","新人01","新人02","新人03","新人04","新人05","chenming",
	 				"wuchun","wuyunpeng","wangbo","chengxiao","lilanlan","zhangzhang","wangwenbo","xuchang","wangzhilei","zhengtiancheng","geyunfei"};
	 		String [] gmnums = {"GM01","GM02","GM04","GM05","GM06","GM07","GM08","GM09","GM10","GM11","GM12","GM15","GM16","GM17","GM18",
	 				"GM19","GM20","GM21","GM22","GM23","GM25","GM26","GM27","GM29","GM30","GM31","GM32","GM33","GM34","GM35",
	 				"GM36","GM37","GM39","GM40","GM99","GM98","GM97","GM96","GM95","GM93","GM41","GM42","GM43","GM44","GM45","GM46","GM47","GM48","GM49","GM50"
	 				,"GM52","GM53","GM54","GM55","GM00","GM66","GM61","GM56","GM57","GM58","GM59","GM88","GM93","GM92","GM91","GM89","GM87","GM90","GM62","GM63","GM64","GM65","GM10000","GM59","GM99","GM96","GM89","GM94","GM999","GM54","GM101","GM102","GM103","GM104","GM105","GM48","GM60","GM45","GM57","GM58","GM51","GM55","GM57","GM43","GM47","GM73","GM74"};
	 		for(int i=0;i<gmnames.length;i++){
	 			if(gmnames[i]!=null&&gmnums[i]!=null){
	 				gmNameIds.put(gmnames[i],gmnums[i]);
	 			}
	 		}	
	 		
	 		if(gmNameIds.keySet().contains(gmname)){
	 			gmid = gmNameIds.get(gmname);
	 			out.print("<div id='gmid' style='display:none;'>"+gmid+"</div>");
	 			
	 			out.print("<tr>");
	 			out.print("<th>其它</th>");
	 			out.print("<td ><div id='http://116.213.204.81:8001/gm' style='display:none;'>幽恋蝶谷</div><input type='checkbox' id='server' name='server' value='http://116.213.204.81:8001/gm'/>幽恋蝶谷</td>");
	 			out.print("</tr>");
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
	</table>
	 </form>
	
   
	<input type="button" onclick="ss()" id="mybutton" value="确定"><input type="button" onclick="allcheaked()" id="allche" value="全选"><br><br>
		
		<table cellspacing="1" cellpadding="2" border="1" id="msg">
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
		  				<th>操作</th>
		  			</tr>
		 </table>
	 <%  		
	 		}else{
	 			out.print("<h1>请找GM管理人给您配置gm编号！</h1>");
	 			out.print("<hr>");
	 		}
	 		
	 	}else{
			out.print("请给该页面设置过滤器！");
		}
		
	%>

</body>
</html>

