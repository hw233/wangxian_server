<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.career.*,java.util.*,com.fy.engineserver.datasource.skill.*,com.google.gson.*" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Weapon"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>各种职业数据浏览</title>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<style type="text/css">
<!--
body{
 margin:0px;
 padding: 0px;
 text-align: center;
 table-layout: fixed;
 word-wrap: break-word;
}
#main{
width:1120px;
margin:0px auto;
border-top:0px solid #69c;
border-right:1px solid #69c;
border-bottom:1px solid #69c;
border-left:0px solid #69c;
background-color:#DCE0EB;
}
.divWidth{
width:40px;
int:left;
border-top:1px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
border-left:1px solid #69c;
}
.divbigwidth{
width:303px;
int:left;
border-top:1px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
border-left:1px solid #69c;
}
.divbigrightwidth{
width:306px;
int:left;
border-top:1px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
border-left:1px solid #69c;
}
.titlecolor{
background-color:#C2CAF5;
}
.tableclass1{
width:303px;
border:0px solid #69c;
color:blue;
}
.tableclass1td{
border:1px solid blue;
overflow:auto;
margin-left: 0px;
margin-right: 0px;
padding-left: 0px;
padding-right: 0px;
}
.tableclass2{
width:306px;
border:0px solid #69c;
border-collapse: collapse;
}
td{
border:1px solid #69c;
word-wrap: break-word;
}
.td{
border-right:0px solid #69c;
border-bottom:0px solid #69c;
}
.toptd{border-top:0px solid #69c;}
.lefttd{border-left:0px solid #69c;}
.tdlittlewidth{
width:50px;
}
.tdwidth{
width:25%;
}
.tdForBlank{
height:20px;
border-top:1px dotted #69c;
border-right:1px dotted #69c;
border-bottom:1px dotted #69c;
border-left:1px dotted #69c;
}
.activeSkill a{text-decoration:none;color:#0080C0}
.activeSkill a:visited{color:#0080C0}
.activeSkill a:hover{color:red}
-->
</style>
<%
Gson gson = new Gson();
CareerManager careerManager = CareerManager.getInstance(); 
Career[] careers = careerManager.getCareers();
int careerCount = 0;
if(careers != null && careers.length != 0){
	careerCount = careers.length;
}
%>
</head>
<body onload="javascript:loopchange(<%=careerCount %>);">
<h2>各种职业数据浏览</h2>
<br>
<a href="./career.jsp">刷新此页面</a>
<br>
<br>
<div id="main">
	<div id="div01" class="divWidth titlecolor">
	职业
	</div>
	<div id="div02" class="divWidth titlecolor">
	编号
	</div>
	<div id="div03" class="divWidth titlecolor">
	性别限制
	</div>
	<div id="div04" class="divWidth titlecolor">
	可使用材质
	</div>
	<div id="div05" class="divWidth titlecolor">
	可使用武器
	</div>
	<div id="div060" class="divbigwidth titlecolor">
	职业路线图1
	</div>
	<div id="div061" class="divbigwidth titlecolor">
	职业路线图2
	</div>
	<div id="div07" class="divbigrightwidth titlecolor">
	职业技能及学习条件
	</div>
	<div style="clear: both"> </div>
	<%
	for(int i = 1; i <= careerCount; i++){
		Career career = careers[(i-1)];
		if(career != null){
		%>
	<div id="div<%=i %>1" class="divWidth">
	<%=career.getName() %>
	</div>
	<div id="div<%=i %>2" class="divWidth">
	<%=career.getId() %>
	</div>
	<div id="div<%=i %>3" class="divWidth">
	<%if(career.isEnableMale() && career.isEnableFemale()){ %>
			无
			<%}else if(career.isEnableMale()){%>
			男
			<%}else if(career.isEnableFemale()){ %>
			女
			<%}else{%>
			配置出错
	<%} %>
	</div>
	<div id="div<%=i %>4" class="divWidth">
	</div>
	<div id="div<%=i %>5" class="divWidth">
	<%StringBuffer sbb = new StringBuffer();
			int[] intss = career.getWeaponTypeLimit();
			if(intss != null){
				for(int index : intss){
					sbb.append(" "+Weapon.WEAPONTYPE_NAME[index]);
				}
			}
			%>
			<%=sbb.toString()%>
	</div>
	<div id="div<%=i %>60" class="divbigwidth">
	        <%
			CareerThread careerThreads[] = career.getCareerThreads();
            %>
	<table id="table<%=i %>10" class="tableclass1" >
		<%
		if(careerThreads != null){
			if(careerThreads.length > 0){
				CareerThread ct = careerThreads[0];
				//由于要分层次，需要计算行列首先分为12行
				if(ct != null){
					Skill skills[] = ct.getSkills();
					int layer1 = 0;
					int layer2 = 0;
					int layer3 = 0;
					int layer4 = 0;
					int layer5 = 0;
					int layer6 = 0;
					int layer7 = 0;
					int layer8 = 0;
					int layer9 = 0;
					int layer10 = 0;
					int layer11 = 0;
					int layer12 = 0;
					int layer13 = 0;
					if(skills != null){
						Skill[] skilllist1 = new Skill[5];
						Skill[] skilllist2 = new Skill[5];
						Skill[] skilllist3 = new Skill[5];
						Skill[] skilllist4 = new Skill[5];
						Skill[] skilllist5 = new Skill[5];
						Skill[] skilllist6 = new Skill[5];
						Skill[] skilllist7 = new Skill[5];
						Skill[] skilllist8 = new Skill[5];
						Skill[] skilllist9 = new Skill[5];
						Skill[] skilllist10 = new Skill[5];
						Skill[] skilllist11 = new Skill[5];
						Skill[] skilllist12 = new Skill[5];
						Skill[] skilllist13 = new Skill[5];
						for(Skill skill : skills){
							if(skill != null){
								if(skill.getNeedCareerThreadPoints() == 0){
									skilllist1[skill.getColIndex()] = skill;
									layer1++;
								}else if(skill.getNeedCareerThreadPoints() <= 5){
									skilllist2[skill.getColIndex()] = skill;
									layer2++;
								}else if(skill.getNeedCareerThreadPoints() <= 10){
									skilllist3[skill.getColIndex()] = skill;
									layer3++;
								}else if(skill.getNeedCareerThreadPoints() <= 15){
									skilllist4[skill.getColIndex()] = skill;
									layer4++;
								}else if(skill.getNeedCareerThreadPoints() <= 20){
									skilllist5[skill.getColIndex()] = skill;
									layer5++;
								}else if(skill.getNeedCareerThreadPoints() <= 25){
									skilllist6[skill.getColIndex()] = skill;
									layer6++;
								}else if(skill.getNeedCareerThreadPoints() <= 30){
									skilllist7[skill.getColIndex()] = skill;
									layer7++;
								}else if(skill.getNeedCareerThreadPoints() <= 35){
									skilllist8[skill.getColIndex()] = skill;
									layer8++;
								}else if(skill.getNeedCareerThreadPoints() <= 40){
									skilllist9[skill.getColIndex()] = skill;
									layer9++;
								}else if(skill.getNeedCareerThreadPoints() <= 45){
									skilllist10[skill.getColIndex()] = skill;
									layer10++;
								}else if(skill.getNeedCareerThreadPoints() <= 50){
									skilllist11[skill.getColIndex()] = skill;
									layer11++;
								}else if(skill.getNeedCareerThreadPoints() <= 55){
									skilllist12[skill.getColIndex()] = skill;
									layer12++;
								}else{
									skilllist13[skill.getColIndex()] = skill;
									layer13++;
								}
							}
						}
						int maxCount = layer1;
						if(maxCount < layer2){
							maxCount = layer2;
						}
						if(maxCount < layer3){
							maxCount = layer3;
						}
						if(maxCount < layer4){
							maxCount = layer4;
						}
						if(maxCount < layer5){
							maxCount = layer5;
						}
						if(maxCount < layer6){
							maxCount = layer6;
						}
						if(maxCount < layer7){
							maxCount = layer7;
						}
						if(maxCount < layer8){
							maxCount = layer8;
						}
						if(maxCount < layer9){
							maxCount = layer9;
						}
						if(maxCount < layer10){
							maxCount = layer10;
						}
						if(maxCount < layer11){
							maxCount = layer11;
						}
						if(maxCount < layer12){
							maxCount = layer12;
						}
						if(maxCount < layer13){
							maxCount = layer13;
						}
						maxCount = 5;
						int tdstylewidth = 0;
						if(maxCount != 0){
							tdstylewidth = 300/maxCount;
						}else{
							tdstylewidth = 300;
						}
						
						for(int m = 0; m < 13; m++){
							Skill[] skilllist = null;
							if(m == 0){
								skilllist = skilllist1;
							}else if(m == 1){
								skilllist = skilllist2;
							}else if(m == 2){
								skilllist = skilllist3;
							}else if(m == 3){
								skilllist = skilllist4;
							}else if(m == 4){
								skilllist = skilllist5;
							}else if(m == 5){
								skilllist = skilllist6;
							}else if(m == 6){
								skilllist = skilllist7;
							}else if(m == 7){
								skilllist = skilllist8;
							}else if(m == 8){
								skilllist = skilllist9;
							}else if(m == 9){
								skilllist = skilllist10;
							}else if(m == 10){
								skilllist = skilllist11;
							}else if(m == 11){
								skilllist = skilllist12;
							}else if(m == 12){
								skilllist = skilllist13;
							}
							%>
							<tr>
							<%for(int n = 0; n < maxCount; n++){ %>
							  <%
								if(skilllist.length > n){ 
								Skill skill = skilllist[n];
								
									String skillName = "错误编号";
									int skillId = -1;
									String skillClassName = "";
									int skillNeedCount = 0;
									if(skill != null){
										%>
										<td class="tableclass1td" style="width:<%=tdstylewidth%>px"><%
										skillName = skill.getName();
										skillId = skill.getId();
										skillClassName = skill.getClass().getName();
										skillNeedCount = skill.getNeedCareerThreadPoints();
									
									if(skillClassName.indexOf("skill.activeskills.")>=0){
									%><div class="activeSkill"><a href="skillbyid.jsp?id=<%=skillId%>&className=<%=skillClassName%>">
									<%= skillName+skillNeedCount  %></a></div>
									<%}else{ %>
									<a href="skillbyid.jsp?id=<%=skillId%>&className=<%=skillClassName%>">
									<%= skillName+skillNeedCount  %></a>
									<%}
									%>
									</td>
									<%
									}else{
										%>
										<td class="tableclass1td tdForBlank" style="width:<%=tdstylewidth%>px">&nbsp;</td>
										<%
									}
									}else{ %>
								<td class="tableclass1td tdForBlank" style="width:<%=tdstylewidth%>px">&nbsp;</td>
								<%} %>
							<%} %>
							</tr>
						<%
						}
					}
				}
			}
		}
		%>
	</table>
	</div>
	<div id="div<%=i %>61" class="divbigwidth">
	<table id="table<%=i %>11" class="tableclass1" >
		<%
		if(careerThreads != null){
			if(careerThreads.length > 1){
				CareerThread ct = careerThreads[1];
				//由于要分层次，需要计算行列首先分为6行
				if(ct != null){
					Skill skills[] = ct.getSkills();
					int layer1 = 0;
					int layer2 = 0;
					int layer3 = 0;
					int layer4 = 0;
					int layer5 = 0;
					int layer6 = 0;
					int layer7 = 0;
					int layer8 = 0;
					int layer9 = 0;
					int layer10 = 0;
					int layer11 = 0;
					int layer12 = 0;
					int layer13 = 0;
					if(skills != null){
						Skill[] skilllist1 = new Skill[5];
						Skill[] skilllist2 = new Skill[5];
						Skill[] skilllist3 = new Skill[5];
						Skill[] skilllist4 = new Skill[5];
						Skill[] skilllist5 = new Skill[5];
						Skill[] skilllist6 = new Skill[5];
						Skill[] skilllist7 = new Skill[5];
						Skill[] skilllist8 = new Skill[5];
						Skill[] skilllist9 = new Skill[5];
						Skill[] skilllist10 = new Skill[5];
						Skill[] skilllist11 = new Skill[5];
						Skill[] skilllist12 = new Skill[5];
						Skill[] skilllist13 = new Skill[5];
						for(Skill skill : skills){
							if(skill != null){
								if(skill.getNeedCareerThreadPoints() == 0){
									skilllist1[skill.getColIndex()] = skill;
									layer1++;
								}else if(skill.getNeedCareerThreadPoints() <= 5){
									skilllist2[skill.getColIndex()] = skill;
									layer2++;
								}else if(skill.getNeedCareerThreadPoints() <= 10){
									skilllist3[skill.getColIndex()] = skill;
									layer3++;
								}else if(skill.getNeedCareerThreadPoints() <= 15){
									skilllist4[skill.getColIndex()] = skill;
									layer4++;
								}else if(skill.getNeedCareerThreadPoints() <= 20){
									skilllist5[skill.getColIndex()] = skill;
									layer5++;
								}else if(skill.getNeedCareerThreadPoints() <= 25){
									skilllist6[skill.getColIndex()] = skill;
									layer6++;
								}else if(skill.getNeedCareerThreadPoints() <= 30){
									skilllist7[skill.getColIndex()] = skill;
									layer7++;
								}else if(skill.getNeedCareerThreadPoints() <= 35){
									skilllist8[skill.getColIndex()] = skill;
									layer8++;
								}else if(skill.getNeedCareerThreadPoints() <= 40){
									skilllist9[skill.getColIndex()] = skill;
									layer9++;
								}else if(skill.getNeedCareerThreadPoints() <= 45){
									skilllist10[skill.getColIndex()] = skill;
									layer10++;
								}else if(skill.getNeedCareerThreadPoints() <= 50){
									skilllist11[skill.getColIndex()] = skill;
									layer11++;
								}else if(skill.getNeedCareerThreadPoints() <= 55){
									skilllist12[skill.getColIndex()] = skill;
									layer12++;
								}else{
									skilllist13[skill.getColIndex()] = skill;
									layer13++;
								}
							}
						}
						int maxCount = layer1;
						if(maxCount < layer2){
							maxCount = layer2;
						}
						if(maxCount < layer3){
							maxCount = layer3;
						}
						if(maxCount < layer4){
							maxCount = layer4;
						}
						if(maxCount < layer5){
							maxCount = layer5;
						}
						if(maxCount < layer6){
							maxCount = layer6;
						}
						if(maxCount < layer7){
							maxCount = layer7;
						}
						if(maxCount < layer8){
							maxCount = layer8;
						}
						if(maxCount < layer9){
							maxCount = layer9;
						}
						if(maxCount < layer10){
							maxCount = layer10;
						}
						if(maxCount < layer11){
							maxCount = layer11;
						}
						if(maxCount < layer12){
							maxCount = layer12;
						}
						if(maxCount < layer13){
							maxCount = layer13;
						}
						maxCount = 5;
						int tdstylewidth = 0;
						if(maxCount != 0){
							tdstylewidth = 300/maxCount;
						}else{
							tdstylewidth = 300;
						}
						
						for(int m = 0; m < 13; m++){
							Skill[] skilllist = null;
							if(m == 0){
								skilllist = skilllist1;
							}else if(m == 1){
								skilllist = skilllist2;
							}else if(m == 2){
								skilllist = skilllist3;
							}else if(m == 3){
								skilllist = skilllist4;
							}else if(m == 4){
								skilllist = skilllist5;
							}else if(m == 5){
								skilllist = skilllist6;
							}else if(m == 6){
								skilllist = skilllist7;
							}else if(m == 7){
								skilllist = skilllist8;
							}else if(m == 8){
								skilllist = skilllist9;
							}else if(m == 9){
								skilllist = skilllist10;
							}else if(m == 10){
								skilllist = skilllist11;
							}else if(m == 11){
								skilllist = skilllist12;
							}else if(m == 12){
								skilllist = skilllist13;
							}
							%>
							<tr>
							<%for(int n = 0; n < maxCount; n++){ %>
							  <%
								if(skilllist.length > n){ 
								Skill skill = skilllist[n];
								
									String skillName = "错误编号";
									int skillId = -1;
									String skillClassName = "";
									int skillNeedCount = 0;
									if(skill != null){
										%>
										<td class="tableclass1td" style="width:<%=tdstylewidth%>px"><%
										skillName = skill.getName();
										skillId = skill.getId();
										skillClassName = skill.getClass().getName();
										skillNeedCount = skill.getNeedCareerThreadPoints();
									
									if(skillClassName.indexOf("skill.activeskills.")>=0){
									%><div class="activeSkill"><a href="skillbyid.jsp?id=<%=skillId%>&className=<%=skillClassName%>">
									<%= skillName+skillNeedCount  %></a></div>
									<%}else{ %>
									<a href="skillbyid.jsp?id=<%=skillId%>&className=<%=skillClassName%>">
									<%= skillName+skillNeedCount  %></a>
									<%}
									%>
									
									</td>
									<%
									}else{
										%>
										<td class="tableclass1td tdForBlank" style="width:<%=tdstylewidth%>px">&nbsp;</td>
										<%
									}
										}else{ %>
								<td class="tableclass1td tdForBlank" style="width:<%=tdstylewidth%>px">&nbsp;</td>
								<%} %>
							<%} %>
							</tr>
						<%
						}
					}
				}
			}
		}
		%>
	</table>
	</div>
	<div id="div<%=i %>7" class="divbigrightwidth" >
	<table id="table<%=i %>2" class="tableclass2">
		<tr>
			<td class="td toptd lefttd tdwidth titlecolor">职业技能</td>
			<td class="td toptd tdwidth titlecolor">需要线路点数</td>
			<td class="td toptd tdwidth titlecolor">前提技能</td>
			<td class="td toptd tdwidth titlecolor">技能等级</td>
		</tr>
		<%
		if(careerThreads != null){
			for(CareerThread ct : careerThreads){
				if(ct != null){
					Skill skills[] = ct.getSkills();
					if(skills != null){
						for(Skill skill : skills){
							if(skill != null){%>
								<tr>
									<td class="td lefttd"><a href="skillsbylinkid.jsp?id=<%=skill.getId()%>"><%=skill.getName()+"("+skill.getId()+")"%></a></td>
									<td class="td"><%=skill.getNeedCareerThreadPoints() %></td>
									<td class="td">无</td>
									<td class="td">无</td>
								</tr>
								<%
							}
						}
					}
				}
			}
		}%>
	</table>
	</div>
	<div style="clear: both"> </div>
	<%
	}
	}
%>
</div>
</body>
<script type="text/javascript">
function loopchange(index){
	for(var i = 0; i <= index; i++){
		heightautochange(i);
	}
}
function heightautochange(index) {
	var myobj1=document.getElementById("div"+index+"1");
	var myobj2=document.getElementById("div"+index+"2");
	var myobj3=document.getElementById("div"+index+"3");
	var myobj4=document.getElementById("div"+index+"4");
	var myobj5=document.getElementById("div"+index+"5");
	var myobj60=document.getElementById("div"+index+"60");
	var myobj61=document.getElementById("div"+index+"61");
	var myobj7=document.getElementById("div"+index+"7");
	var maxlength = myobj1.clientHeight;
	var length1 = myobj1.clientHeight;
	var length2 = myobj2.clientHeight;
	var length3 = myobj3.clientHeight;
	var length4 = myobj4.clientHeight;
	var length5 = myobj5.clientHeight;
	var length60 = myobj60.clientHeight;
	var length61 = myobj61.clientHeight;
	var length7 = myobj7.clientHeight;
	if(maxlength < length2){
		maxlength = length2;
	}
	if(maxlength < length3){
		maxlength = length3;
	}
	if(maxlength < length4){
		maxlength = length4;
	}
	if(maxlength < length5){
		maxlength = length5;
	}
	if(maxlength < length60){
		maxlength = length60;
	}
	if(maxlength < length61){
		maxlength = length61;
	}
	if(maxlength < length7){
		maxlength = length7;
	}
	myobj1.style.height=length1+"px";
	myobj1.style.paddingTop = (maxlength-length1)/2+"px";
	myobj1.style.paddingBottom = (maxlength-length1)/2+"px";
	myobj2.style.height=length2+"px";
	myobj2.style.paddingTop = (maxlength-length2)/2+"px";
	myobj2.style.paddingBottom = (maxlength-length2)/2+"px";
	myobj3.style.height=length3+"px";
	myobj3.style.paddingTop = (maxlength-length3)/2+"px";
	myobj3.style.paddingBottom = (maxlength-length3)/2+"px";
	myobj4.style.height=length4+"px";
	myobj4.style.paddingTop = (maxlength-length4)/2+"px";
	myobj4.style.paddingBottom = (maxlength-length4)/2+"px";
	myobj5.style.height=length5+"px";
	myobj5.style.paddingTop = (maxlength-length5)/2+"px";
	myobj5.style.paddingBottom = (maxlength-length5)/2+"px";
	myobj60.style.height=length60+"px";
	myobj60.style.paddingTop = (maxlength-length60)/2+"px";
	myobj60.style.paddingBottom = (maxlength-length60)/2+"px";
	myobj61.style.height=length61+"px";
	myobj61.style.paddingTop = (maxlength-length61)/2+"px";
	myobj61.style.paddingBottom = (maxlength-length61)/2+"px";
	myobj7.style.height=length7+"px";
	myobj7.style.paddingTop = (maxlength-length7)/2+"px";
	myobj7.style.paddingBottom = (maxlength-length7)/2+"px";

	}
</script>
</html>
