<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page
	import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.datasource.article.manager.*,com.fy.engineserver.datasource.article.data.props.*,com.fy.engineserver.datasource.article.data.entity.*,com.fy.engineserver.util.*,com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.pet.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
</script>
</head>
<body>
<br>
<h2>确认取消临时加点</h2>
<br>
<form action="" name=f1>加点<br>
	
	petId:<input type=text size=20 name="petId" /><br>
	类型(0力量1灵力2身法3耐力4定力，默认为0):<input type=text size=20 name="oprateType"/><br>
	
	取消(0确定,1为取消):<input type=text size=20 name="cancel" /><br>
	
	<input type=submit name=sub1 value=提交>
</form>

<%
	
	String petIdSt = request.getParameter("petId");
	if(petIdSt != null){
		
		long petId = Long.parseLong(petIdSt);
		
		PetManager pm = PetManager.getInstance();
		Pet pet = pm.getPet(petId);
		if(pet == null){
			out.print("宠物为null");
			return ;
		}
		
		String cancelSt = request.getParameter("cancel");
		if(cancelSt != null && cancelSt.trim().equals("1")){
			//取消
			String result = pm.opratePetAllocatePoint(pet,(byte)1);
			if(result.equals("")){
				out.print("取消加点成功");
			}else{
				out.print(result);
			}
		}else if(cancelSt != null && cancelSt.trim().equals("0")){
			//取消
			String result = pm.opratePetAllocatePoint(pet,(byte)0);
			if(result.equals("")){
				out.print("确定加点成功");
			}else{
				out.print(result);
			}
		}else{
			//加点
			String typeSt = request.getParameter("oprateType");
			int type = 0;
			if(typeSt != null && !typeSt.equals("")){
				type = Integer.parseInt(typeSt.trim());
			}
			int[] points = {0,0,0,0,0};
			points[type] = 1;
			
			int total = 0;
			boolean valid = true;
			for (int i = 0; i < points.length; i++) {
				if (points[i] < 0) {
					valid = false;
					break;
				}
				total += points[i];
			}
			pet.setUnAllocatedPoints(pet.getUnAllocatedPoints() - total);
			int[] tempPoints = null;
			if(pet.getTempPoints() == null){
				tempPoints = new int[5];
			}else{
				tempPoints = pet.getTempPoints();
			}
			for (int i = 0; i < points.length; i++) {
				switch (i) {
				case PetPropertyUtility.力量:
					pet.setStrengthS(pet.getStrengthS() + points[i]);
					tempPoints[PetPropertyUtility.力量] += points[i];
					break;
				case PetPropertyUtility.灵力:
					pet.setSpellpowerS(pet.getSpellpowerS() + points[i]);
					tempPoints[PetPropertyUtility.灵力] += points[i];
					break;
				case PetPropertyUtility.身法:
					pet.setDexterityS(pet.getDexterityS() + points[i]);
					tempPoints[PetPropertyUtility.身法] += points[i];
					break;
				case PetPropertyUtility.耐力:
					pet.setConstitutionS(pet.getConstitutionS() + points[i]);
					tempPoints[PetPropertyUtility.耐力] += points[i];
					break;
				case PetPropertyUtility.定力:
					pet.setDingliS(pet.getDingliS() + points[i]);
					tempPoints[PetPropertyUtility.定力] += points[i];
					break;
				default:
					//
				}
			}
			pet.setTempPoints(tempPoints);
			pet.reinitFightingProperties();
			out.print("临时加点成功");
		}
	}


%>


</body>
</html>
