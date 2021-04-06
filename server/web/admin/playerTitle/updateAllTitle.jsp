<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Map.*"%>
<%@page import="com.fy.engineserver.playerTitles.PlayerTitlesManager"%>
<%@page import="java.io.*"%>
<%@page import="com.fy.engineserver.playerTitles.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>

<%@page import="com.fy.engineserver.playerTitles.PlayerTitlesManager.PlayerTitleTemplate"%><html>
<head>
<title>根据日志更新所有的玩家称号</title>

</head>
<body>

	<%
		//resin_stable/webapps/game_server/admin/playerTitle/
		// resin_stable/log/game_server/
		//String fileName = "/home/game/resin/webapps/game_server/WEB-INF/game_init_config/addTitle.log";
	
		String nums = request.getParameter("nums");
		if(nums != null && !nums.equals("")){
	
			int num = Integer.parseInt(nums);
			if(num > 4 || num < 0){
				out.print("num 错误");
				return ;
			}
			
			String fileName = "../../../../log/game_server/addTitle.log";
			if(num == 0){
				fileName = "/home/game/resin/log/game_server/addTitle.log";
			}else if(num < 4){
				fileName = "/home/game/resin_stable/log/game_server/addTitle.log";
			}else{
				fileName = "/home/game/resin_stable_qiannian/log/game_server/addTitle.log";
			}
			
			Map<String,List<Long>> map = new HashMap<String,List<Long>>();
			BufferedReader br = null;
			try{
				br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
				String ss = br.readLine();
	
				while(ss != null){
					int name1 = ss.indexOf("value:");
					int name2 = ss.indexOf("] [color");
					int value1 = ss.indexOf("id:");
					int value2 = ss.indexOf("}{name:");
					
					String name = ss.substring(name1+6, name2);
					String value = ss.substring(value1+3, value2);
					
					if(map.get(name) == null){
						List<Long> list = new ArrayList<Long>();
						list.add(Long.parseLong(value));
						map.put(name, list);
					}else{
						long id = Long.parseLong(value);
						if(map.get(name).contains(id)){
							out.println("重复:"+id);
						}else{
							map.get(name).add(id);
						}
					}
					ss = br.readLine();
				}
				
				PlayerTitlesManager ptm = PlayerTitlesManager.getInstance();
				for(String st : map.keySet()){
					List<Long> listId = map.get(st);
					out.println(st +"  "+listId.size()+"<br/>");
					
					//得到key
					List<PlayerTitlesManager.PlayerTitleTemplate> list = PlayerTitlesManager.getInstance().getList();
					String key = "";
					for(PlayerTitleTemplate ptt : list){
						if(ptt.getTitleName().equals(st)){
							key = ptt.getKey();
							break;
						}
					}
					
					for(long id : listId){
						
						try{
							Player player = PlayerManager.getInstance().getPlayer(id);
							
							int titleNum = ptm.getKeyByType(key);
							String titleName = ptm.getValueByType(key);
							int color = ptm.getColorByType(key);
							
							List<PlayerTitle> playerTitles = player.getPlayerTitles();
							
							if (playerTitles == null) {
								playerTitles = new ArrayList<PlayerTitle>();
							}
							if (playerTitles.size() == 0) {
								playerTitles.add(new PlayerTitle(-1, "", 0));
							}
							
							boolean bln = true;
							for (PlayerTitle pt : playerTitles) {
								if (pt.getTitleType() == titleNum) {
									//pt.setTitleName(titleName);
									bln = false;
									break;
								}
							}
							if (bln) {
								PlayerTitle pt = new PlayerTitle(titleNum, titleName, color);
								playerTitles.add(pt);
							}
							
							player.setPlayerTitles(playerTitles);
							
						}catch(Exception e){
							out.print("没找着玩家"+id);
						}
						
					}
				}
				out.print("over");
			}catch(Exception e){
				if(br != null){
					try {
						br.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			return ;
		}
	%>
	
	<form action="">
		第几个服务区:<input type="text" name="nums"/>
		<input type="submit" value="submit"/>
	</form>

</body>
</html>
