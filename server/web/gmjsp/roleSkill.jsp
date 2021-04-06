<%@page import="com.fy.engineserver.datasource.skill.master.SkEnhanceManager"%>
<%@page import="com.fy.engineserver.datasource.skill.master.SkBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.datasource.skill.Skill"%>
<%@page import="com.fy.engineserver.datasource.career.Career"%>
<%@page import="com.fy.engineserver.datasource.career.CareerManager"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="java.net.URLDecoder"%><%@page import="com.fy.boss.gm.gmpagestat.GmEventManager"%><%@page import="net.sf.json.JSONArray"%><%@page import="java.util.HashMap"%><%@page import="java.util.Map"%><%@page import="com.fy.engineserver.mail.service.MailManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%><%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%><%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%><%@page import="com.fy.engineserver.sprite.Player"%><%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	long playerId = Long.valueOf(request.getParameter("playerId"));
	String type = request.getParameter("type");
	String gmUser = URLDecoder.decode(request.getParameter("gmUserName"),"utf-8");
	Map<String, Object> result = new HashMap<String, Object>();
	try {
		Player player = null;
		try {
			player = GamePlayerManager.getInstance().getPlayer(playerId);
		} catch (Exception e) {
			
		}
		if ("1".equals(type)) {			//显示角色技能心法信息
			Soul soul = player.getSoul(Soul.SOUL_TYPE_BASE);
			CareerManager cm = CareerManager.getInstance();
			Career c = cm.getCareer(player.getMainCareer());
			List<Object> basicSkills = new ArrayList<Object>();
			List<Object> rangSkills = new ArrayList<Object>();
			List<Object> advancedSkills = new ArrayList<Object>();
			List<Object> masterSkills = new ArrayList<Object>();
			List<Object> xinfaSkills = new ArrayList<Object>();
			for (int i=0; i<c.getBasicSkills().length; i++) {		//基础技能
				Map<String,Object> bb = new LinkedHashMap<String,Object>();
				Skill skill = c.getBasicSkills()[i];
				bb.put("name", skill.getName());
				bb.put("skillId", skill.getId());
				bb.put("level", soul.getCareerBasicSkillsLevels()[i]);
				bb.put("maxLevel", skill.getMaxLevel());
				basicSkills.add(bb);
			}
			for (int i=0; i<c.getNuqiSkills().length; i++) {		//怒气技能
				Map<String,Object> bb = new LinkedHashMap<String,Object>();
				Skill skill = c.getNuqiSkills()[i];
				bb.put("name", skill.getName());
				bb.put("skillId", skill.getId());
				bb.put("level", player.getNuqiSkillsLevels()[i]);
				bb.put("maxLevel", skill.getMaxLevel());
				rangSkills.add(bb);
			}
			for (int i=0; i<c.threads[0].getSkills().length; i++) {		//进阶技能
				Map<String,Object> bb = new LinkedHashMap<String,Object>();
				Skill skill = c.threads[0].getSkills()[i];
				bb.put("name", skill.getName());
				bb.put("skillId", skill.getId());
				bb.put("level", soul.getSkillOneLevels()[i]);
				bb.put("maxLevel", skill.getMaxLevel());
				advancedSkills.add(bb);
			}
			SkBean s = SkEnhanceManager.getInst().findSkBean(player);
			if (s != null) {
				byte[] lvs = s.getLevels();
				for (int i=0; i<lvs.length; i++) {
					Skill skill = c.threads[0].getSkillByIndex(i);
					if (skill != null) {
						Map<String,Object> bb = new LinkedHashMap<String,Object>();
						bb.put("name", "大师级能" + skill.getName());
						bb.put("skillId", skill.getId());
						bb.put("level", lvs[i]);
						bb.put("maxLevel", 30);
						masterSkills.add(bb);
					}
				}
			}
			for (int i=0; i<c.getXinfaSkills().length; i++) {
				Skill xinfask = c.getXinfaSkills()[i];
				if (s != null) {
					Map<String,Object> bb = new LinkedHashMap<String,Object>();
					int xinfaLv = player.getSkillLevel(xinfask.getId());
					bb.put("name", xinfask.getName());
					bb.put("skillId", xinfask.getId());
					bb.put("level", xinfaLv);
					bb.put("maxLevel", 100);
					xinfaSkills.add(bb);
				}
			}
			result.put("basicSkills", basicSkills);
			result.put("rangSkills", rangSkills);
			result.put("advancedSkills", advancedSkills);
			result.put("masterSkills", masterSkills);
			result.put("heartSkills", xinfaSkills);
		} else if ("2".equals(type)) {		//心法等级参数
			int skId = Integer.parseInt(request.getParameter("skillId"));
			byte level = Byte.parseByte(request.getParameter("level"));
			CareerManager cm = CareerManager.getInstance();
			Career c = cm.getCareer(player.getMainCareer());
			Skill[] skills = c.getXinfaSkills();
			boolean xinfa = false;
			if (skills != null) {
				for (int i = 0; i < skills.length; i++) {
					Skill s = skills[i];
					if (s != null && s.getId() == skId) {
						byte oldLv = player.getXinfaLevels()[i];
						player.getXinfaLevels()[i] = level;
						player.setXinfaLevels(player.getXinfaLevels());
						ArticleManager.logger.warn("[gm平台操作] [修改心法技能等级] [修改者:"+gmUser+"] [" + player.getLogString() + "] [skId:" + skId + "] [oldLv:" + oldLv + "] [newLv:" + player.getXinfaLevels()[i] + "]");
						result.put("success", "true");
						xinfa = true;
						break;
					}
				}
			}
			if (!xinfa) {
				result.put("success", "false");
				result.put("message", "没有对应id的心法技能");
			}
		} else if ("3".equals(type)) {		//修改大师技能等级
			int skId = Integer.parseInt(request.getParameter("skillId"));
			byte level = Byte.parseByte(request.getParameter("level"));
			CareerManager cm = CareerManager.getInstance();
			Career c = cm.getCareer(player.getMainCareer());
			SkBean s = SkEnhanceManager.getInst().findSkBean(player);
			boolean b = false;
			if (s != null) {
				byte[] lvs = s.getLevels();
				for (int i=0; i<lvs.length; i++) {
					Skill skill = c.threads[0].getSkillByIndex(i);
					if (skId == skill.getId()) {
						byte oldLv = lvs[i];
						lvs[i] = level;	
						s.setLevels(lvs);
						result.put("success", "true");
						ArticleManager.logger.warn("[gm平台操作] [修改大师技能等级] [修改者:"+gmUser+"] [" + player.getLogString() + "] [skId:" + skId + "] [oldLv:" + oldLv + "] [newLv:" + s.getLevels()[i] + "]");
						b = true;
						break;
					}
				}
			}
			if (!b) {
				result.put("success", "false");
				result.put("message", "没有对应id的 大师技能");
			}
		}
	} catch (Exception e) {
		result.put("result","页面出现异常" + e.getMessage());		
	}
	JSONArray json = JSONArray.fromObject(result);
	out.print(json.toString()); 
	out.flush();
	out.close();
%>