<%@page import="com.fy.engineserver.datasource.skill2.GenericSkill"%>
<%@page import="com.fy.engineserver.datasource.skill2.GenericSkillManager"%>
<%@page import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@page import="com.fy.engineserver.sprite.pet2.PetsOfPlayer"%>
<%@page import="com.fy.engineserver.sprite.pet2.Pet2Manager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="java.util.*,
	com.xuanzhi.tools.text.*,
	com.fy.engineserver.datasource.article.manager.*,
	com.fy.engineserver.datasource.article.data.props.*,
	com.fy.engineserver.datasource.article.data.entity.*,
	com.fy.engineserver.util.*,
	com.fy.engineserver.sprite.*,
	com.fy.engineserver.sprite.pet.*"

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.career.CareerManager"%>
<%@page import="com.fy.engineserver.datasource.skill.Skill"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<style type="text/css">

</style>
<script type="text/javascript">

</script>
</head>
<body>
<br><br>
<h2>查询技能修正</h2><br>
	<%
		String action = request.getParameter("action");
		String petId = request.getParameter("petId");
		String skillId1 = request.getParameter("skillId1");
		String skillId2 = request.getParameter("skillId2");
		String psd = request.getParameter("psd");
		action = action == null ? "" : action;
		petId = petId == null ? "" : petId;
		skillId1 = skillId1 == null ? "" : skillId1;
		skillId2 = skillId2 == null ? "" : skillId2;
		psd = psd == null ? "" : psd;
		if(!TestServerConfigManager.isTestServer() && !psd.equals("xiuzhengskillId")) {
			out.println("不是测试服，修改需要密码！");
			return;
		}
		if (action.equals("xiuzhengS")) {
			Pet pet = PetManager.getInstance().getPet(Long.parseLong(petId));
			if (pet == null) {
				out.println("输入宠物Id错误！");
				return ;
			}
			int s1 = Integer.parseInt(skillId1);
			int s2 = Integer.parseInt(skillId2);
			GenericSkill gs1 = GenericSkillManager.getInst().maps.get(s1);
			GenericSkill gs2 = GenericSkillManager.getInst().maps.get(s2);
			if (gs1 == null || gs2 == null) {
				out.println("输入的宠物技能id错误!");
				return ;
			}
			Pet2Manager.log.warn("[后台修正宠物技能] [原有宠物天赋技能:" + Arrays.toString(pet.getTianFuSkIds()) + "] [PetId : " + pet.getId() + "]");
			out.println("[后台修正宠物技能] [原有宠物天赋技能:" + Arrays.toString(pet.getTianFuSkIds()) + "] [PetId : " + pet.getId() + "]<br>");
			int[] temp = pet.getTianFuSkIds();
			boolean ll = false;
			for (int i=0; i<temp.length; i++) {
				if (temp[i] == s1) {
					temp[i] = s2;
					ll = true;
					break;
				}
			}
			if (!ll) {
				out.println("该宠物没有对应天赋技能!");
				return ;
			} 
			pet.setTianFuSkIds(temp);
			out.println("[后台修正宠物技能] [成功] [宠物天赋技能:" + Arrays.toString(pet.getTianFuSkIds()) + "] [PetId : " + pet.getId() + "]<br>");
			Pet2Manager.log.warn("[后台修正宠物技能] [成功] [宠物天赋技能:" + Arrays.toString(pet.getTianFuSkIds()) + "] [PetId : " + pet.getId() + "]");
			pet.init();
		}
	%>
	<form action="replacePetSkillId.jsp" method="post">
		<input type="hidden" name="action" value="xiuzhengS" />宠物Id:
		<input type="text" name="petId" value="<%=petId%>" />需要替换的技能id:
		<input type="text" name="skillId1" value="<%=skillId1%>" />替换后的技能id:
		<input type="text" name="skillId2" value="<%=skillId2%>" />非测试服密码:
		<input type="text" name="psd" value="<%=psd%>" />
		<input type="submit" value="修正宠物技能" />
	</form>
	<br />
</body>
</html>
