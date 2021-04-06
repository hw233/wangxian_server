<%@page import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@page import="com.fy.engineserver.datasource.skill2.GenericSkillManager"%>
<%@page import="com.fy.engineserver.sprite.pet2.Pet2Manager"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate"%>
<%@page import="org.apache.commons.lang.ArrayUtils"%>
<%@page import="com.fy.engineserver.datasource.skill2.GenericBuff"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="java.util.*,
	com.xuanzhi.tools.text.*,
	com.fy.engineserver.datasource.article.manager.*,
	com.fy.engineserver.datasource.article.data.props.*,
	com.fy.engineserver.datasource.article.data.entity.*,
	com.fy.engineserver.util.*,
	com.fy.engineserver.sprite.*,
	com.fy.engineserver.sprite.pet.*,
	com.fy.engineserver.datasource.skill2.GenericBuff"

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%><html>
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
<h2>根据id查询宠物</h2>
<br>


		<%
		Map<Integer, String> id2name = new HashMap<Integer, String>();
		Iterator<String> it = GenericSkillManager.attName2id.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			id2name.put(GenericSkillManager.attName2id.get(key), key);
		}
		String stId = request.getParameter("petId");
		String pwd = request.getParameter("pwd");
		String forceT1skStr = request.getParameter("forceT1sk");
		String forceT2skStr = request.getParameter("forceT2sk");
		String forceGen2 = request.getParameter("forceGen2");
		String rm1skStr = request.getParameter("rm1sk");
		String rm2skStr = request.getParameter("rm2sk");
		boolean rm1sk = rm1skStr != null && !rm1skStr.isEmpty();
		boolean rm2sk = rm2skStr != null && !rm2skStr.isEmpty();
		boolean forceT1sk = false;
		boolean forceT2sk = false;
		if(forceT1skStr != null && !forceT1skStr.isEmpty()){
			forceT1sk = true;
		}
		if(forceT2skStr != null && !forceT2skStr.isEmpty()){
			forceT2sk = true;
		}
		if(stId == null || stId.equals("")){
			
			%>
			<form action="">
					输入宠物id:<input type="text" name="petId"></input>	<br/>
					密码:<input type="password" name="pwd">查询宠物信息不需输密码</input>	<br/>
					强制获得 一 代技能:<input type="checkbox" name="forceT1sk"></input>	<br/>
					强制获得 二 代技能:<input type="checkbox" name="forceT2sk"></input>	<br/>
					<br/>
					强制宠物为二代:<input type="checkbox" name="forceGen2"></input>	<br/>
					<br/>
					遗忘 一 代技能:<input type="checkbox" name="rm1sk"></input>	<br/>
					遗忘 二 代技能:<input type="checkbox" name="rm2sk"></input>	<br/>
					<input type="submit" value="sumit"></input>
			</form>
			
			<%
							}else{
							
							long id = Long.parseLong(stId.trim());
							PetManager pm = PetManager.getInstance();
							Pet pet = pm.getPet(id);
							if(pet != null){
								//pet.setGeneration((byte)1);
								if(TestServerConfigManager.isTestServer()|| ( pwd != null && "xgcwjnpwd".equals(pwd))){
									if(forceGen2 != null && !forceGen2.isEmpty() ){
										pet.setGeneration((byte)1);
										out.print("强制为二代宠物ok<br/>");
									}
									if(forceT1sk){
										pet.talent1Skill = pet.petProps.talent1skill;
										pet.setTalent1Skill(pet.petProps.talent1skill);
										out.print("强制技能 一 ok<br/>");
									}
									if(forceT2sk){
										pet.talent2Skill = pet.petProps.talent2skill;
										pet.setTalent2Skill(pet.petProps.talent2skill);
										out.print("强制技能 二 ok<br/>");
									}
									if(rm1sk){
										pet.talent1Skill = 0;
										pet.setTalent1Skill(0);
										out.print("遗忘技能 一 ok<br/>");
									}
									if(rm2sk){
										pet.talent2Skill = 0;
										pet.setTalent2Skill(0);
										out.print("遗忘技能 二 ok<br/>");
									}
									pet.init();
								}else{
									out.print("不是测试服或密码错误！<br/>");
								}
								Player p = pet.getMaster();
								if(p != null){
									out.print("玩家名称:"+p.getName()+"<br/>");
								}else{
									out.print("玩家名称:getMaster is null<br/>");
								}
									out.print("几代 : "+(pet.getGeneration()+1)+"<br/>");
									out.print("进阶 : "+pet.getGrade()+"<br/>");
								out.print("pet id:"+pet.getId()+"<br/><br/>");
								out.print("这个宠物所属的玩家id:"+pet.getOwnerId()+"<br/>");
								out.print("宠物名字:"+pet.getName()+"<br/>");
								out.print("getAvataRace:"+pet.getAvataRace()+"<br/>");
								out.print("getAvataSex:"+pet.getAvataSex()+"<br/>");
								out.print("道具id:"+pet.getPetPropsId()+"<br/>");
								out.print("性格"+PetManager.得到宠物性格名(pet.getCharacter())+"<br/>");
								out.print("天生技能一:"+pet.talent1Skill+"  "+(pet.petProps == null ? "null" : pet.petProps.talent1skill)+"<br/>");
								out.print("天生技能二:"+pet.talent2Skill+"  "+(pet.petProps == null ? "null" : pet.petProps.talent2skill)+"<br/>");
								out.print("天赋技能 id:"+ArrayUtils.toString(pet.tianFuSkIds)+"<br/>");
								out.print("天赋技能 lv:"+ArrayUtils.toString(pet.tianFuSkIvs)+"<br/>");
								out.print("getCategory:"+pet.getCategory()+"<br/>");
								long ll = ((com.fy.engineserver.datasource.skill2.CountTimesSkillAgent)pet.getSkillAgent()).atkTimes;
						                out.print("攻击次数:"+ll+"<br/>");
								out.print("buff:<br/>");
								GenericBuff b = pet.pet2buff;
								while(b != null){
									out.print(" 技能名称 ");	out.print(b.srcSkName);
									out.print(" attId ");	out.print(b.attId);
									out.print(" 属性名称 ");	out.print(b.attName+":"+id2name.get(b.attId));
									out.print(" 值 ");	out.print(b.v);
									out.print(" 是否百分比 ");	out.print(b.percent);
									if(b.attId == GenericBuff.ATT_ADD_SUB_BUFF){
										out.print(" buff template");
										BuffTemplate t = b.getTemplate();
										if(t == null){
											out.print(" null ");
										}else{
											out.print(t.getName());
										}
									}
									out.print(" paramInt ");	out.print(b.paramInt);
									out.print(" paramIntB ");	out.print(b.paramIntB);
									out.print(" <br/> ");
									b = b.next;
								}
								out.print(" $ ");
								Map<Integer, Integer> map = pet.specialTargetFactor;
								if(map == null){
									out.print("没有特定目标技能<br/>");
								}else{
									out.print("特定目标技能:<br/>");
									for(Integer key : map.keySet()){
										out.print(key+"->"+map.get(key)+"<br/>");
									}
								}
								out.print(" 给主人的经验加成 ");	out.print(pet.exp2playerRatio);	out.print(" <br/> ");;
							out.println("<br/>===============<br/>");	
							out.println("StrengthA:"+pet.getStrengthA()+"B:"+pet.getStrengthB()+":C"+pet.getStrengthC()+"<br/>");
							out.println("getDexterity:"+pet.getDexterityA()+"B:"+pet.getDexterityB()+":C"+pet.getDexterityC()+"<br/>");
							out.println("setSpellpower:"+pet.getSpellpowerA()+"B:"+pet.getSpellpowerB()+":C"+pet.getSpellpowerC()+"<br/>");
							out.println("getConstitutionA:"+pet.getConstitutionA()+"B:"+pet.getConstitutionB()+":C"+pet.getConstitutionC()+"<br/>");
							out.println("getDingliA:"+pet.getDingliA()+"B:"+pet.getDingliB()+":C"+pet.getDingliC()+"<br/>");
							out.println("暴击系数:"+pet.critFactor+"<br/>");
							out.println("几率:"+pet.dmgScaleRate+"增加伤害"+pet.dmgScale+"<br/>");
							out.println("物攻:"+pet.getPhyAttack()+"<br/>");
							out.println("法攻:"+pet.getMagicAttack()+"<br/>");
							out.println("暴击getCriticalHit:"+pet.getCriticalHit()+"<br/>");
							out.println("getSpeedA:"+pet.getSpeedA()+"<br/>");
							out.println("getSpeed:"+pet.getSpeed()+"<br/>");
							out.println("towerRate:"+pet.towerRate+"<br/>");
							} else {
									out.print("没有这个宠物" + id);
								}
							}
						%>
</body>
</html>
