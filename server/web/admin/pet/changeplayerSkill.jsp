<%@page import="com.fy.engineserver.datasource.career.CareerThread"%>
<%@page import="com.fy.engineserver.datasource.skill.Skill"%>
<%@page import="com.fy.engineserver.datasource.career.CareerManager"%>
<%@page import="com.fy.engineserver.datasource.career.Career"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.fy.engineserver.stat.StatData"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="org.apache.log4j.Logger"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>修改玩家技能</title>
		<style type="text/css">
</style>
		<script type="text/javascript">
	
</script>
	</head>
		<%
			String pname = request.getParameter("playername");
			String skillidd = request.getParameter("skillid");
			String skillleveld = request.getParameter("skilllevel");
			if(skillidd!=null && skillleveld!=null){
				String skillids [] = skillidd.split(",");
				String skilllevels [] = skillleveld.split(",");
				if(skillids!=null && skilllevels!=null){
					if(skillids.length!=skilllevels.length){
						out.print("要加等级的技能id必须和技能等级一一对应，请认真点填写！");
						return;
					}
					if(pname!=null && pname.trim().length()>0){
						Player p = null;
						try{
							p = PlayerManager.getInstance().getPlayer(Long.parseLong(pname.trim()));
						}catch(Exception e){
							out.print("<font color='red'>玩家   "+pname+" 不存在！</font>");
						}
						out.print("玩家："+p.getName()+"--玩家id："+p.getId()+"<br>");
						if(p!=null){
							CareerManager cm = CareerManager.getInstance();
							Career career = cm.getCareer(p.getCareer());
							Skill skill = null;
							if (career != null) {
								for(int k=0;k<skillids.length;k++){
									String skillid = skillids[k];
									byte skilllevel = Byte.parseByte(skilllevels[k]);
									if (career.basicSkills != null) {
										for (int i = 0; i < career.basicSkills.length; i++) {
											skill = career.basicSkills[i];
											if (skill != null && skill.getId()==Integer.parseInt(skillid.trim())) {
												out.print("<B>技能类型：基础技能</B>---设置前<br>技能名字："+skill.getName()+"<br>技能id："+skill.getId()+"<br>技能比例："+p.getCareerBasicSkillsLevels()[i]+"/"+skill.getMaxLevel()+"<br>");
												byte[] careerBasicSkillsLevels = p.getCareerBasicSkillsLevels();
												careerBasicSkillsLevels[i] = skilllevel;
												p.setCareerBasicSkillsLevels(careerBasicSkillsLevels);
												out.print("<B>技能类型：基础技能</B>---设置后<br>技能名字："+skill.getName()+"<br>技能id："+skill.getId()+"<br>技能比例："+p.getCareerBasicSkillsLevels()[i]+"/"+skill.getMaxLevel()+"<br><hr>");
											}
										}
									}
									if (career.nuqiSkills != null) {
										for (int i = 0; i < career.nuqiSkills.length; i++) {
											skill = career.nuqiSkills[i];
											if (skill != null && skill.getId()==Integer.parseInt(skillid.trim())) {
												out.print("<B>技能类型：怒气技能</B>---设置前<br>技能名字："+skill.getName()+"<br>技能id："+skill.getId()+"<br>技能比例："+p.getNuqiSkillsLevels()[i]+"/"+skill.getMaxLevel()+"<br>");
												byte[] nuqiSkillsLevels = p.getNuqiSkillsLevels();
												nuqiSkillsLevels[i] = skilllevel;
												p.setNuqiSkillsLevels(nuqiSkillsLevels);
												out.print("<B>技能类型：怒气技能</B>---设置前<br>技能名字："+skill.getName()+"<br>技能id："+skill.getId()+"<br>技能比例："+p.getNuqiSkillsLevels()[i]+"/"+skill.getMaxLevel()+"<br><hr>");
											}
										}
									}
									if (career.threads != null) {
										for (int ii = 0; ii < career.threads.length; ii++) {
											CareerThread ct = career.threads[ii];
											if (ct != null) {
												Skill[] skills = ct.skills;
												for (int i = 0; i < skills.length; i++) {
													skill = skills[i];
													if (skill != null && skill.getId()==Integer.parseInt(skillid.trim())) {
														out.print("<B>技能类型：进阶技能---设置前</B><br>技能名字："+skill.getName()+"<br>技能id："+skill.getId()+"<br>技能比例："+p.getSkillOneLevels()[i]+"/"+skill.getMaxLevel()+"<br>");
														byte[] skillOneLevels = p.getSkillOneLevels();
														skillOneLevels[i] = skilllevel;
														p.setSkillOneLevels(skillOneLevels);
														out.print("<B>技能类型：进阶技能---设置前</B><br>技能名字："+skill.getName()+"<br>技能id："+skill.getId()+"<br>技能比例："+p.getSkillOneLevels()[i]+"/"+skill.getMaxLevel()+"<br><hr>");
													}
												}
											}
										}
									}
									
									if (career.xinfaSkills != null) {
										for (int i = 0; i < career.xinfaSkills.length; i++) {
											skill = career.xinfaSkills[i];
											if (skill != null && skill.getId()==Integer.parseInt(skillid.trim())) {
												out.print("<B>技能类型：心法技能</B>---设置前<br>技能名字："+skill.getName()+"<br>技能id："+skill.getId()+"<br>技能比例："+p.getXinfaLevels()[i]+"/"+skill.getMaxLevel()+"<br>");
												byte[] xinfaLevels = p.getXinfaLevels();
												xinfaLevels[i] = skilllevel;
												p.setXinfaLevels(xinfaLevels);
												out.print("<B>技能类型：心法技能</B>---设置后<br>技能名字："+skill.getName()+"<br>技能id："+skill.getId()+"<br>技能比例："+p.getXinfaLevels()[i]+"/"+skill.getMaxLevel()+"<br><hr>");
											}
										}
									}
								}
							}
							
						}
					}else{
						out.print("请输出正确的角色名！");
					}
				}else{
					out.print("解析字符串出错！");
					return;
				}
			}else{
				out.print("不能为空！");
			}
			out.print("<hr>");
		%>
		<body>
			<h2>通过玩家角色名查询玩家的技能</h2>
			<h3>多个技能id和要修改的技能等级一一对应，之间用英文逗号分隔</h3>
			<form>
				<table>
					<tr>
						<th>
							角色id：
						</th>
						<td>
							<input type='text' name='playername' value="<%=pname %>">
						</td>
						<th>
							技能id：
						</th>
						<td>
							<input type='text' name='skillid' value="<%=skillidd %>">
						</td>
						<th>
							技能等级：
						</th>
						<td>
							<input type='text' name='skilllevel' value="<%=skillleveld %>">
						</td>
						<td>
							<input type="submit" value='修改'>
						</td>
					</tr>
				</table>
			</form>
		</body>
	
</html>
