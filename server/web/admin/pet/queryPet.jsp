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
<h2>查询宠物</h2><br>
<form id='f01' name='f01' action="">
		<input type='hidden' name='action' value='select_playerName'>
		请输入角色名：<input type='text' name='playerName' id='playerName'  size='20'/>
		<input type='submit' value='提  交' />
	</form>
	<%
	String pName = "";
	String action = request.getParameter("act");
	if("setXueMaiAct".equals(action)){
		Long pid = Long.valueOf(request.getParameter("pid"));
		Player p = null;
		try{
			p = PlayerManager.getInstance().getPlayerInCache(pid);
		}catch(Exception e){
			out.println("玩家不存在 "+pid);
		}
		if(p != null){
			PetsOfPlayer bean0 = Pet2Manager.getInst().findPetsOfPlayer(p);
			Long setXueMai = Long.valueOf(request.getParameter("setXueMai"));
			bean0.setXueMai(setXueMai);
			out.println("已设置血脉为:"+setXueMai);
			pName = p.getName();
		}
	}
	////////////
			List<Pet> list = new ArrayList<Pet>();
			String playerName = request.getParameter("playerName");
			if(playerName == null || playerName.equals("")) return ;
			String errorMessage = null;
			PlayerManager pm = PlayerManager.getInstance();
			Player player = null;
			ArticleManager am = ArticleManager.getInstance();
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			 long xueMai = 0;
			try { 
				player =  pm.getPlayer(playerName);
			}catch (Exception e){
				errorMessage = "玩家不存在";
			}
			
			if(errorMessage != null){
				out.print(errorMessage);
				return ;
			}else{
				pName = player.getName();
				Knapsack k = player.getPetKnapsack();
				long activePetId = player.getActivePetId();
				Cell[] cells = k.getCells();
				for(Cell c :cells){
					if(!c.isEmpty()){
						long id = c.getEntityId();
						PetPropsEntity ppe = (PetPropsEntity) aem.getEntity(id);
						
						PetManager petManager = PetManager.getInstance();
						if(ppe == null) continue;
						Pet pet = petManager.getPet(ppe.getPetId());
						if(pet != null){
							list.add(pet);
						}
					}
				}
				//
				PetsOfPlayer bean = Pet2Manager.getInst().findPetsOfPlayer(player);
				xueMai = bean.xueMai;
			}
			%>
	玩家名称：<%=player.getName() %>
			血脉值:<%=xueMai		%>  <%if (player != null){ %>
			<form action="" method='post'>
				<input type="hidden" name='act' value='setXueMaiAct'/>
				<input type="hidden" name='pid' value='<%=player.getId()%>'/>
				<input type="text" name='setXueMai' value='<%=xueMai%>'/>
				<input type="submit" value="设置血脉值"/>
			</form>
			<%} %>
		<table border="1" align="center" width="80%">
		<tr> 
			<td width="15%">宠物名</td>  
			<td width="15%">宠物id</td> 
			<td width="60%">宠物信息</td> 
			<td width="10%">对宠物的操作</td>
		 </tr>
			<%
	for(Pet p:list){
		%>
		<tr> 
		<% out.print("<td>"+p.getName()+"</td>") ;
			out.print("<td>"+p.getId()+"</td>") ;
		%>  
			<td>
				<table align="center"> 
					<tr> <td>主人</td><td><%= p.getOwnerId() %></td></tr>
					<tr> <td>种类</td><td><%= p.getCategory() %></td></tr>
					<tr> <td>职业倾向</td><td><%= p.getCareer() %></td></tr>
					<tr> <td>性格</td><td><%= p.getCharacter() %></td></tr>
					<tr> <td>性别</td><td><%= p.getSex()%></td></tr>
					<tr> <td>宠物可生育的次数</td><td><%= p.getBreedTimes()%></td></tr>
					<tr> <td>宠物已经生育的次数</td><td><%= p.getBreededTimes()  %></td></tr>
					<tr> <td>最大宠物的快乐值</td><td><%= p.getMaxHappyness() %></td></tr>
					<tr> <td>最大宠物的寿命</td><td><%= p.getMaxLifeTime() %></td></tr>
					<tr> <td>快乐值</td><td><%= p.getHappyness() %></td></tr>
					<tr> <td>寿命</td><td><%= p.getLifeTime() %></td></tr>
					<tr> <td>等级</td><td><%= p.getLevel() %></td></tr>
					<tr> <td>当前经验值</td><td><%= p.getExp() %></td></tr>
					<tr> <td>升到下级的经验值</td><td><%= p.getExpPercent() %></td></tr>
					<tr> <td>品质:普兽灵兽仙兽神兽圣兽</td><td><%= p.getCharacter() %></td></tr>
					<tr> <td>宠物是否变异</td><td><%= p.getVariation() %></td></tr>
					<tr> <td>力量资质</td><td><%= p.getShowStrengthQuality() %></td></tr>
					<tr> <td>身法资质</td><td><%= p.getShowDexterityQuality()%></td></tr>
					<tr> <td>灵力资质</td><td><%= p.getShowSpellpowerQuality() %></td></tr>
					<tr> <td>耐力资质</td><td><%= p.getShowConstitutionQuality() %></td></tr>
					<tr> <td>定力资质</td><td><%= p.getShowDingliQuality() %></td></tr>
					<tr> <td>携带等级</td><td><%= p.getTrainLevel() %></td></tr>
					<tr> <td>稀有度</td><td><%= p.getRarity() %></td></tr>
					<tr> <td>成长品质</td><td><%= p.getGrowthClass() %></td></tr>
					<tr> <td>成长值</td><td><%= p.getGrowth() %></td></tr>
					<tr> <td>力量</td><td><%= p.getStrength() %></td></tr>
					<tr> <td>身法</td><td><%= p.getDexterity() %></td></tr>
					<tr> <td>灵力</td><td><%= p.getSpellpower() %></td></tr>
					<tr> <td>耐力</td><td><%= p.getConstitution() %></td></tr>
					<tr> <td>定力</td><td><%= p.getDingli() %></td></tr>
					<tr> <td>外功强度</td><td><%= p.getPhyAttack() %></td></tr>
					<tr> <td>外功防御</td><td><%= p.getPhyDefence() %></td></tr>
					<tr> <td>内功强度</td><td><%= p.getMagicAttack() %></td></tr>
					<tr> <td>内功防御</td><td><%= p.getMagicDefence() %></td></tr>
					<tr> <td>最大血量</td><td><%= p.getMaxHP() %></td></tr>
					<tr> <td>当前血量</td><td><%= p.getHp() %></td></tr>
					<tr> <td>暴击</td><td><%= p.getCriticalHit() %></td></tr>
					<tr> <td>命中</td><td><%= p.getHit() %></td></tr>
					<tr> <td>闪避</td><td><%= p.getDodge() %></td></tr>
					<tr> <td>命中率</td><td><%= p.getHitRate() %></td></tr>
					<tr> <td>闪避率</td><td><%= p.getDodgeRate() %></td></tr>
					<tr> <td>暴击率</td><td><%= p.getCriticalHitRate()  %></td></tr>
					<tr> <td>外功防御率</td><td><%= p.getPhysicalDecrease() %></td></tr>
					<tr> <td>内功防御率</td><td><%= p.getSpellDecrease() %></td></tr>

					<tr> <td>还没分配的属性点</td><td><%= p.getUnAllocatedPoints() %></td></tr>
					<tr> <td>模式, 0-被动，1-主动，2-只跟随</td><td><%= p.getActivationType() %></td></tr>
					<tr> <td>物品id</td><td><%= p.getPetPropsId() %></td></tr>
					<tr >
						<td>技能</td> <% int[] ids = p.getSkillIds() ;
						
							StringBuffer sb = new StringBuffer();
							for(int i= 0;i<ids.length;i++){
								CareerManager cm  = CareerManager.getInstance();
								Skill skill = cm.getSkillById(ids[i]);
								sb.append("id:"+ids[i]+"name:"+skill.getName()+" 等级 "+p.getSkillLevelById(ids[i])+"描述: "+skill.getDescription()+"<br/>");
							}
						%>
						
						<td><%=sb.toString() %></td>
					</tr>
					<tr >
						<td>普通技能</td> 
						
						<td><%=p.getCommonSkillId() %></td>
					</tr>
					<tr >
						<td>挂机状态</td> 
						
						<td><%=p.getHookInfo() == null ? "--" :p.getHookInfo().toString()  %></td>
					</tr>
				</table>
			</td>
		
			<td>  
				 <table>
					<tr><td>鉴定</td></tr>
					<tr><td>释放 </td></tr>
					<tr><td>还童</td></tr>
					<tr><td>鉴定</td></tr>
					<tr><td>鉴定</td></tr>
					<tr><td>鉴定</td></tr>
				
				</table>  
			</td>
		
		 </tr>
		 <%
	}
	%>
	</table>
</body>
</html>
