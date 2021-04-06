<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.sprite.monster.MemoryMonsterManager"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.fy.engineserver.sprite.monster.MonsterManager"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.sprite.monster.MemoryMonsterManager.MonsterTempalte"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>刷怪物掉落</title>
</head>
<body>
<%
	MonsterManager mm = MemoryMonsterManager.getMonsterManager();
	MemoryMonsterManager mmm = (MemoryMonsterManager) mm;
	LinkedHashMap<String, Integer[]> monster2FlopListProbabilityMap = mmm.getMonster2FlopListProbabilityMap();
	LinkedHashMap<Integer, MonsterTempalte> templates = mmm.getTemplates();
	for (String monsterName : monster2FlopListProbabilityMap.keySet()) {
		if (monsterName.equals("乾天域主(165)") || monsterName.equals("乾天域主(185)") || monsterName.equals("乾天域主(205)") || monsterName.equals("乾天域主(225)") || monsterName.equals("坤地域主(165)") || monsterName.equals("坤地域主(185)") || monsterName.equals("坤地域主(205)") || monsterName.equals("坤地域主(225)") || monsterName.equals("震雷域主(165)") || monsterName.equals("震雷域主(185)") || monsterName.equals("震雷域主(205)") || monsterName.equals("震雷域主(225)") || monsterName.equals("巽风域主(165)") || monsterName.equals("巽风域主(185)") || monsterName.equals("巽风域主(205)") || monsterName.equals("巽风域主(225)") || monsterName.equals("坎水域主(165)") || monsterName.equals("坎水域主(185)") || monsterName.equals("坎水域主(205)") || monsterName.equals("坎水域主(225)") || monsterName.equals("离火域主(165)") || monsterName.equals("离火域主(185)") || monsterName.equals("离火域主(205)") || monsterName.equals("离火域主(225)") || monsterName.equals("艮山域主(165)") || monsterName.equals("艮山域主(185)") || monsterName.equals("艮山域主(205)") || monsterName.equals("艮山域主(225)") || monsterName.equals("兑泽域主(165)") || monsterName.equals("兑泽域主(185)") || monsterName.equals("兑泽域主(205)") || monsterName.equals("兑泽域主(225)")) {
			Integer[] ints = monster2FlopListProbabilityMap.get(monsterName);
			out.print("修改前：" + Arrays.toString(ints) + "<br>");
			ints[0] = 3939;
			ints[1] = 3939;
			ints[3] = 3939;
			ints[5] = 143939;
			ints[7] = 143939;
			monster2FlopListProbabilityMap.put(monsterName, ints);
			for (Integer id : templates.keySet()) {
				if (templates.get(id).monster.getName().equals(monsterName)) {
					templates.get(id).monster.setFsProbabilitis(monster2FlopListProbabilityMap.get(monsterName));
					out.print("修改后：" + Arrays.toString(templates.get(id).monster.getFsProbabilitis()) + "<br>");
				}
			}
		}
	}
	mmm.setMonster2FlopListProbabilityMap(monster2FlopListProbabilityMap);
%>
</body>
</html>