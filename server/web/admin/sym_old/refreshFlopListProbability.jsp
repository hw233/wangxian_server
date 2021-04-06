<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page
	import="com.fy.engineserver.sprite.monster.MemoryMonsterManager"%>
<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page
	import="com.fy.engineserver.sprite.monster.MemoryMonsterManager.MonsterTempalte"%>
<%@page import="com.fy.engineserver.sprite.monster.Monster"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>刷新怪物掉率</title>
</head>
<body>
<%
	String path = request.getRealPath("flopProp.txt");
	out.print(path + " 是否存在:" + new File(path).exists() + "<HR/>");
	FileInputStream fis = new FileInputStream(path);
	InputStreamReader isr = new InputStreamReader(fis, "GBK");
	BufferedReader bufferedReader = new BufferedReader(isr);
	String line = bufferedReader.readLine();
	Map<String, Integer[]> propMap = new LinkedHashMap<String, Integer[]>();
	String tempname = "多宝妖灵(宝)";
	List<Integer> propList = new ArrayList<Integer>();
	try {
		while (line != null && !"".equals(line.trim())) {
			String[] values = line.split("\t");
			String monsterName = values[0];
			String prop = values[1];
			if (monsterName.equals(tempname)) {
				propList.add(Integer.parseInt(prop));
			} else {
				propMap.put(tempname, propList.toArray(new Integer[0]));
				tempname = monsterName;
				propList.clear();
				propList.add(Integer.parseInt(prop));
			}
			line = bufferedReader.readLine();
		}
		propMap.put(tempname, propList.toArray(new Integer[0]));

	} catch (Exception e) {
		out.print("解析出异常了,去stderr看看!<BR/>");
		e.printStackTrace();
	} finally {
		if (bufferedReader != null) {
			try {
				bufferedReader.close();
			} catch (Exception ee) {
				out.print("关闭出异常了,去stderr看看!<BR/>");
				ee.printStackTrace();
			}
		}
	}

	MemoryMonsterManager mmm = (MemoryMonsterManager) MemoryMonsterManager.getMonsterManager();
	LinkedHashMap<Integer, MemoryMonsterManager.MonsterTempalte> templates = mmm.getTemplates();
	for (Integer categoryId : templates.keySet()) {
		Monster m = templates.get(categoryId).monster;
		if (propMap.containsKey(m.getName())) {
			out.print(m.getName());
			out.print(Arrays.toString(propMap.get(m.getName()))+"<hr>");
			m.setFsProbabilitis(propMap.get(m.getName()));
		}
	}
	mmm.setTemplates(templates);
	out.print("刷新完毕!");
%>
</body>
</html>