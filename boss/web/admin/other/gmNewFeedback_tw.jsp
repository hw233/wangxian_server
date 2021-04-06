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
<link rel="stylesheet" href="gm/css/style.css"/>
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
		window.open('questionAdd.jsp?id='+id+'&servername='+servername+'&username='+userName+'&qudao='+channel+'&playername='+playerName+'&viplevel='+vipLevel,'newwindow','width=700,height=460,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no,z-look=no');
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
								 f.innerHTML="<img src='images/v-01.png' width='30' height='30' title='1.体力最多可购买1次；2.增加1次喝酒次数；3.增加1次屠魔贴;4.开启随身修理'>";
							 }else if(myfeedback.vip==2){
								 f.innerHTML="<img src='images/v-02.png' width='30' height='30' title='1.体力最多可购买6次；2.开启随身邮件；3.增加每日绑银使用上限200两；4.增加1次额外经验种植机会；5.包含VIP1所有功能.'>";
							 }else if(myfeedback.vip==3){
								 f.innerHTML="<img src='images/v-03.png' width='30' height='30'  title='1.增加2次喝酒次数；2.增加2次屠魔贴;3.开启随身仓库;4.获得1次额外摇钱树种植机会;5.可以使用中级炼妖石;6.可书安心新境界6星以上杂念任务;7.包含VIP2所有功能.'>";
							 }else if(myfeedback.vip==4){
								 f.innerHTML="<img src='images/v-04.png' width='30' height='30' title='1.最多可购买14次体力；2.开启随身求购;3.可用银子书安心境界杂念任务;4.增加每日绑银使用上限500两；5.可使用仙府鬼斧神工令;6.押镖多一次加货机会；7.幽冥幻域每日最多可刷新3次;8.包含VIP3所有功能.'>";
							 }else if(myfeedback.vip==5){
								 f.innerHTML="<img src='images/v-05.png' width='30' height='30' title='1.增加3次喝酒次数；2.增加3次屠魔贴;3.可以用元宝捐献家族资金换取贡献度;4.幽冥幻域获得经验奖励提升50%；5.可以使用高级炼妖石;6获得2次额外摇钱树种植机会;6.包含VIP4所有功能.'>";
							 }else if(myfeedback.vip==6){
								 f.innerHTML="<img src='images/v-06.png' width='30' height='30' title='1.最多可购买24次体力；2.幽冥幻域获得经验奖励提升100%；3.增加每日绑银使用上限1锭；4.增加2次额外经验种植机会；5.幽冥幻域每日最多可刷新4次;6.包含VIP5所有功能.'>";
							 }else if(myfeedback.vip==7){
								 f.innerHTML="<img src='images/v-07.png' width='30' height='30' title='1.增加4次喝酒次数；2.增加4次屠魔贴；3.包含VIP6所有功能.'>";
							 }else if(myfeedback.vip==8){
								 f.innerHTML="<img src='images/v-08.png' width='30' height='30' title='1.体力最多可购买36次；2.包含VIP7所有功能.'>";
							 }else if(myfeedback.vip==9){
								 f.innerHTML="<img src='images/v-09.png' width='30' height='30' title='1.增加5次喝酒次数；2.增加5次屠魔贴；3.包含VIP8所有功能.'>";
							 }else if(myfeedback.vip==10){
								 f.innerHTML="<img src='images/v-10.png' width='30' height='30' title='1.体力最多可购买48次;2.包含VIP9所有功能。'>";
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
							 f.innerHTML="<img src='images/v-01.png' width='30' height='30' title='1.体力最多可购买1次；2.增加1次喝酒次数；3.增加1次屠魔贴;4.开启随身修理'>";
						 }else if(myfeedback.vip==2){
							 f.innerHTML="<img src='images/v-02.png' width='30' height='30' title='1.体力最多可购买6次；2.开启随身邮件；3.增加每日绑银使用上限200两；4.增加1次额外经验种植机会；5.包含VIP1所有功能.'>";
						 }else if(myfeedback.vip==3){
							 f.innerHTML="<img src='images/v-03.png' width='30' height='30' title='1.增加2次喝酒次数；2.增加2次屠魔贴;3.开启随身仓库;4.获得1次额外摇钱树种植机会;5.可以使用中级炼妖石;6.可书安心新境界6星以上杂念任务;7.包含VIP2所有功能.'>";
						 }else if(myfeedback.vip==4){
							 f.innerHTML="<img src='images/v-04.png' width='30' height='30' title='1.最多可购买14次体力；2.开启随身求购;3.可用银子书安心境界杂念任务;4.增加每日绑银使用上限500两；5.可使用仙府鬼斧神工令;6.押镖多一次加货机会；7.幽冥幻域每日最多可刷新3次;8.包含VIP3所有功能.'>";
						 }else if(myfeedback.vip==5){
							 f.innerHTML="<img src='images/v-05.png' width='30' height='30' title='1.增加3次喝酒次数；2.增加3次屠魔贴;3.可以用元宝捐献家族资金换取贡献度;4.幽冥幻域获得经验奖励提升50%；5.可以使用高级炼妖石;6获得2次额外摇钱树种植机会;6.包含VIP4所有功能.'>";
						 }else if(myfeedback.vip==6){
							 f.innerHTML="<img src='images/v-06.png' width='30' height='30' title='1.最多可购买24次体力；2.幽冥幻域获得经验奖励提升100%；3.增加每日绑银使用上限1锭；4.增加2次额外经验种植机会；5.幽冥幻域每日最多可刷新4次;6.包含VIP5所有功能.'>";
						 }else if(myfeedback.vip==7){
							 f.innerHTML="<img src='images/v-07.png' width='30' height='30' title='1.增加4次喝酒次数；2.增加4次屠魔贴；3.包含VIP6所有功能.'>";
						 }else if(myfeedback.vip==8){
							 f.innerHTML="<img src='images/v-08.png' width='30' height='30' title='1.体力最多可购买36次；2.包含VIP7所有功能.'>";
						 }else if(myfeedback.vip==9){
							 f.innerHTML="<img src='images/v-09.png' width='30' height='30' title='1.增加5次喝酒次数；2.增加5次屠魔贴；3.包含VIP8所有功能.'>";
						 }else if(myfeedback.vip==10){
							 f.innerHTML="<img src='images/v-10.png' width='30' height='30' title='1.体力最多可购买48次;2.包含VIP9所有功能.'/>";
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
									s.src = url+"?gmreply="+gmreply+"&gmid="+gmid+"&sendFeedbackId="+sendFeedbackId+"&authorize.username=wangtianxin&authorize.password=123321";
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
									s2.src = url2+"?closemess=closemess&gmid="+gmid+"&sendFeedbackId="+sendFeedbackId+"&authorize.username=wangtianxin&authorize.password=123321";
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
							s.src = url+"?gmreply="+gmreply+"&gmid="+gmid+"&sendFeedbackId="+sendFeedbackId+"&authorize.username=wangtianxin&authorize.password=123321";
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
			if (strs.length>0){    
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
					s.src = url+"?cb=cb&replydate="+backservertime+"&authorize.username=wangtianxin&authorize.password=123321";
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
	 	
	 	out.print("<tr><th>状态-服务器</th><th>斬龍屠魔</th><th>天降神兵</th><th>雪域冰城</th><th>華山之巔</th><th>千年古城</th><th>飄渺王城</th><th>昆侖仙境</th><th>皇圖霸業</th><th>人間仙境</th><th>伏虎沖天</th><th>無相如來</th><th>仙侶奇緣</th><th>仙人指路</th></tr>");
	 	out.print("<th>新消息</th><td id='斬龍屠魔_no'>0</td><td id='天降神兵_no'>0</td><td id='雪域冰城_no'>0</td><td id='華山之巔_no'>0</td><td id='千年古城_no'>0</td><td id='飄渺王城_no'>0</td><td id='昆侖仙境_no'>0</td><td id='皇圖霸業_no'>0</td><td id='人間仙境_no'>0</td><td id='伏虎沖天_no'>0</td><td id='無相如來_no'>0</td><td id='仙侶奇緣_no'>0</td><td id='仙人指路_no'>0</td></tr>");
	 	out.print("<th>待处理</th><td id='斬龍屠魔_new'>0</td><td id='天降神兵_new'>0</td><td id='雪域冰城_new'>0</td><td id='華山之巔_new'>0</td><td id='千年古城_new'>0</td><td id='飄渺王城_new'>0</td><td id='昆侖仙境_new'>0</td><td id='皇圖霸業_new'>0</td><td id='人間仙境_new'>0</td><td id='伏虎沖天_new'>0</td><td id='無相如來_new'>0</td><td id='仙侶奇緣_new'>0</td><td id='仙人指路_new'>0</td></tr>");
	 	out.print("<th>总数</th><td id='斬龍屠魔_sum'>0</td><td id='天降神兵_sum'>0</td><td id='雪域冰城_sum'>0</td><td id='華山之巔_sum'>0</td><td id='千年古城_sum'>0</td><td id='飄渺王城_sum'>0</td><td id='昆侖仙境_sum'>0</td><td id='皇圖霸業_sum'>0</td><td id='人間仙境_sum'>0</td><td id='伏虎沖天_sum'>0</td><td id='無相如來_sum'>0</td><td id='仙侶奇緣_sum'>0</td><td id='仙人指路_sum'>0</td></tr>");
	 	out.print("<tr><th>状态-服务器</th><th>瓊漿玉露</th><th>步雲登仙</th><th>道骨仙風</th><th>蜀山之道</th><th>禦劍伏魔</th><th>傲視遮天</th><th>縱橫九州</th><th>仙姿玉貌</th><th>神魔仙界</th><th>氣衝霄漢</th> <td><B><font size='3px' color='red'>汇总</font></B></td></tr>");
	 	out.print("<th>新消息</th><td id='瓊漿玉露_no'>0</td><td id='步雲登仙_no'>0</td><td id='道骨仙風_no'>0</td><td id='蜀山之道_no'>0</td><td id='禦劍伏魔_no'>0</td><td id='傲視遮天_no'>0</td><td id='縱橫九州_no'>0</td><td id='仙姿玉貌_no'>0</td><td id='神魔仙界_no'>0</td><td id='氣衝霄漢_no'>0</td> <td id='huizong'></td></tr>");
	 	out.print("<th>待处理</th><td id='瓊漿玉露_new'>0</td><td id='步雲登仙_new'>0</td><td id='道骨仙風_new'>0</td><td id='蜀山之道_new'>0</td><td id='禦劍伏魔_new'>0</td><td id='傲視遮天_new'>0</td><td id='縱橫九州_new'>0</td><td id='仙姿玉貌_new'>0</td><td id='神魔仙界_new'>0</td><td id='氣衝霄漢_new'>0</td>  <td id='huizong_new'></td></tr>");
	 	out.print("<th>总数</th><td id='瓊漿玉露_sum'>0</td><td id='步雲登仙_sum'>0</td><td id='道骨仙風_sum'>0</td><td id='蜀山之道_sum'>0</td><td id='禦劍伏魔_sum'>0</td><td id='傲視遮天_sum'>0</td><td id='縱橫九州_sum'>0</td><td id='仙姿玉貌_sum'>0</td><td id='神魔仙界_sum'>0</td><td id='氣衝霄漢_sum'>0</td> <td id='huizong_sum'></td></tr>");
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
	 				,"shangwei","yangboxiong","luotao","dengshaoyu","limengting","wanglong","dongjunhao","liukai","liuyu","zhangjitong","chenmingyu","peishengran","cuixiaomeng","zhaopengyu","yangzhao","chenxiong","chenhao","sufang","zhangjian","liuchuan","liuyanga","fanlei","liushengyue","zhouying","zhurui","zhenganqi","lihuiqin","guojing","新人01","新人02","新人03","新人04","新人05","chenming","wuchun","wuyunpeng","wangbo","chengxiao","lilanlan","zhangzhang","wangwenbo","klgm01"};
	 		String [] gmnums = {"GM01","GM02","GM04","GM05","GM06","GM07","GM08","GM09","GM10","GM11","GM12","GM15","GM16","GM17","GM18",
	 				"GM19","GM20","GM21","GM22","GM23","GM25","GM26","GM27","GM29","GM30","GM31","GM32","GM33","GM34","GM35",
	 				"GM36","GM37","GM39","GM40","GM99","GM98","GM97","GM96","GM95","GM93","GM41","GM42","GM43","GM44","GM45","GM46","GM47","GM48","GM49","GM50"
	 				,"GM52","GM53","GM54","GM55","GM00","GM66","GM61","GM56","GM57","GM58","GM59","GM88","GM93","GM92","GM91","GM89","GM87","GM90","GM62","GM63","GM64","GM65","GM10000","GM59","GM99","GM96","GM89","GM94","GM999","GM54","GM101","GM102","GM103","GM104","GM105","GM48","GM60","GM45","GM57","GM58","GM51","GM55","GM57","GM01"};
	 		for(int i=0;i<gmnames.length;i++){
	 			if(gmnames[i]!=null&&gmnums[i]!=null){
	 				gmNameIds.put(gmnames[i],gmnums[i]);
	 			}
	 		}	
	 		
	 		if(gmNameIds.keySet().contains(gmname)){
	 			gmid = gmNameIds.get(gmname);
	 			out.print("<div id='gmid' style='display:none;'>"+gmid+"</div>");
	 			
	 			out.print("<tr>");
	 			out.print("<td ><div id='http://211.72.246.185:8002/gm' style='display:none;'>斬龍屠魔</div><input type='checkbox' id='server' name='server' value='http://211.72.246.185:8002/gm'/>斬龍屠魔</td>");
	 			out.print("<td ><div id='http://211.72.246.185:8001/gm' style='display:none;'>天降神兵</div><input type='checkbox' id='server' name='server' value='http://211.72.246.185:8001/gm'/>天降神兵</td>");
	 			out.print("<td ><div id='http://210.65.151.19:8002/gm' style='display:none;'>雪域冰城</div><input type='checkbox' id='server' name='server' value='http://210.65.151.19:8002/gm'/>雪域冰城</td>");
	 			out.print("<td ><div id='http://210.65.151.19:8001/gm' style='display:none;'>華山之巔</div><input type='checkbox' id='server' name='server' value='http://210.65.151.19:8001/gm'/>華山之巔</td>");
	 			out.print("<td ><div id='http://210.65.151.32:8002/gm' style='display:none;'>千年古城</div><input type='checkbox' id='server' name='server' value='http://210.65.151.32:8002/gm'/>千年古城</td>");
	 			out.print("</tr>");
	 			out.print("<tr>");
	 			out.print("<td ><div id='http://210.65.151.32:8001/gm' style='display:none;'>飄渺王城</div><input type='checkbox' id='server' name='server' value='http://210.65.151.32:8001/gm'/>飄渺王城</td>");
	 			out.print("<td ><div id='http://211.72.246.29:8001/gm' style='display:none;'>昆侖仙境</div><input type='checkbox' id='server' name='server' value='http://211.72.246.29:8001/gm'/>昆侖仙境</td>");
	 			out.print("<td ><div id='http://211.72.246.148:8001/gm' style='display:none;'>皇圖霸業</div><input type='checkbox' id='server' name='server' value='http://211.72.246.148:8001/gm'/>皇圖霸業</td>");
	 			out.print("<td ><div id='http://211.72.246.148:8002/gm' style='display:none;'>人間仙境</div><input type='checkbox' id='server' name='server' value='http://211.72.246.148:8002/gm'/>人間仙境</td>");
	 			out.print("<td ><div id='http://211.72.246.36:8002/gm' style='display:none;'>伏虎沖天</div><input type='checkbox' id='server' name='server' value='http://211.72.246.36:8002/gm'/>伏虎沖天</td>");
	 			out.print("</tr>");
	 			out.print("<tr>");
	 			out.print("<td ><div id='http://211.72.246.36:8001/gm' style='display:none;'>無相如來</div><input type='checkbox' id='server' name='server' value='http://211.72.246.36:8001/gm'/>無相如來</td>");
	 			out.print("<td ><div id='http://211.72.246.154:8001/gm' style='display:none;'>仙侶奇緣</div><input type='checkbox' id='server' name='server' value='http://211.72.246.154:8001/gm'/>仙侶奇緣</td>");
	 			out.print("<td ><div id='http://211.72.246.154:8002/gm' style='display:none;'>仙人指路</div><input type='checkbox' id='server' name='server' value='http://211.72.246.154:8002/gm'/>仙人指路</td>");
	 			out.print("<td ><div id='http://211.72.246.12:8001/gm' style='display:none;'>瓊漿玉露</div><input type='checkbox' id='server' name='server' value='http://211.72.246.12:8001/gm'/>瓊漿玉露</td>");
	 			out.print("<td ><div id='http://211.72.246.56:8001/gm' style='display:none;'>仙姿玉貌</div><input type='checkbox' id='server' name='server' value='http://211.72.246.56:8001/gm'/>仙姿玉貌</td>");
	 			out.print("</tr>");
	 			out.print("<tr>");
	 			out.print("<td ><div id='http://211.72.246.56:8002/gm' style='display:none;'>步雲登仙</div><input type='checkbox' id='server' name='server' value='http://211.72.246.56:8002/gm'/>步雲登仙</td>");
	 			out.print("<td ><div id='http://211.72.246.12:8002/gm' style='display:none;'>道骨仙風</div><input type='checkbox' id='server' name='server' value='http://211.72.246.12:8002/gm'/>道骨仙風</td>");
	 			out.print("<td ><div id='http://211.72.246.183:8001/gm' style='display:none;'>蜀山之道</div><input type='checkbox' id='server' name='server' value='http://211.72.246.183:8001/gm'/>蜀山之道</td>");
	 			out.print("<td ><div id='http://211.72.246.183:8002/gm' style='display:none;'>禦劍伏魔</div><input type='checkbox' id='server' name='server' value='http://211.72.246.183:8002/gm'/>禦劍伏魔</td>");
	 			out.print("</tr>");
	 			out.print("<tr>");
	 			out.print("<td ><div id='http://210.65.151.17:8002/gm' style='display:none;'>傲視遮天</div><input type='checkbox' id='server' name='server' value='http://210.65.151.17:8002/gm'/>傲視遮天</td>");
	 			out.print("<td ><div id='http://210.65.151.17:8001/gm' style='display:none;'>縱橫九州</div><input type='checkbox' id='server' name='server' value='http://210.65.151.17:8001/gm'/>縱橫九州</td>");
	 			out.print("<td ><div id='http://210.65.151.30:8001/gm' style='display:none;'>神魔仙界</div><input type='checkbox' id='server' name='server' value='http://210.65.151.30:8001/gm'/>神魔仙界</td>");
	 			out.print("<td ><div id='http://210.65.151.51:8001/gm' style='display:none;'>氣衝霄漢</div><input type='checkbox' id='server' name='server' value='http://210.65.151.51:8001/gm'/>氣衝霄漢</td>");
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

