<%@page import="com.fy.boss.gm.faq.FaqGameModule"%>
<%@page import="com.fy.boss.gm.faq.FaqManager"%>
<%@ page contentType="text/html;charset=utf-8" import="java.util.*"%>
<%-- <%@ include file="header.jsp"%> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		
			<link href="dtree.css" type="text/css" rel="stylesheet" />
			<script src="dtree.js" type=text/javascript></script>

			<META content="MSHTML 6.00.2900.3157" name=GENERATOR>
				<style type="text/css">
				
<!--
body,td,th {
	font-size: 12px;
}

body {
	margin-left: 5px;
	margin-top: 5px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-color: #DDEDFF;
}
-->
</style>
	</head>
	<body>
		<div align="center" class="dtree">
			<a href="javascript:%20d.openAll();" class="node">全部展开</a> |
			<a href="javascript:%20d.closeAll();" class="node">全部折叠</a>
			<br />
			<br />
		</div>
		<div style="display: none;" id="sess"></div>
			<% 
// 			LinkedHashMap<String, ArrayList<String[]>> directoryMap = new LinkedHashMap<String, ArrayList<String[]>>();
// 			String dirs[] = directoryMap.keySet().toArray(new String[0]);
// 			List<FaqGameModule> games = FaqManager.getInstance().getGames();
// 			ArrayList<String[]> di = null;
// 			ArrayList<String[]> dii = null;
// 			List<String> names = new ArrayList<String>();
// 			List<String> modules = new ArrayList<String>();
// 			List<String> urls = new ArrayList<String>();
// 			List<String> mods = new ArrayList<String>();
			//初始化游戏和其对应的模块
			
			LinkedHashMap<String, ArrayList<String[]>> directoryMap = FaqManager.getInstance().getMap(); 
			String dirs[] = directoryMap.keySet().toArray(new String[0]);
// 			for (FaqGameModule mt : games) {
// 				if(dirs.length>0){
// 					for(int j=0;j<dirs.length;j++){
// 						if(dirs[j].equals(mt.getGamename())){
// 							di = directoryMap.get(mt.getGamename());
// 							di.add(new String[]{mt.getModule(),mt.getUrl()});
// 							directoryMap.put(mt.getGamename(), di);
// 						}else{
// 							dii = new ArrayList<String[]>();
// 							dii.add(new String[]{mt.getModule(),mt.getUrl()});
// 							directoryMap.put(mt.getGamename(), dii);
// 						}
// 					}	
// 				}else{
// 					di = new ArrayList<String[]>();
// 					di.add(new String[]{mt.getModule(),mt.getUrl()});
// 					directoryMap.put(mt.getGamename(), di);
// 				}
// 				names.add(mt.getGamename());
// 				modules.add(mt.getModule());	
// 				urls.add(mt.getUrl());
// 			}
// 			for(FaqGameModule mt : games){
// 				for(int i=0;i<names.size();i++){
// 					if(names.get(i).equals(mt.getGamename())){
// 						String ss = modules.get(i);
// 						String aa = urls.get(i);
// 						di.add(new String[]{ss,aa});
// 					}
// 				}
// 				directoryMap.put(mt.getGamename(), di);
// 			}
			
			
			
			
			
		%>
		<script type=text/javascript>
		
                var d = new dTree('d');
                d.add(0,-1,'游戏FAQ手册');
		<%

			int parentIndex = 0;
			int currentIndex = 1;
			for (int i = 0; i < dirs.length; i++) {
				List<String[]> pageList = directoryMap.get(dirs[i]);
				int count = 0;
				for (int j = 0; j < pageList.size(); j++) {
					String ps []= pageList.get(j);
					count++;
				}

				if (count > 0) {
					out.println("d.add(" + currentIndex + ",0,'" + dirs[i]
							+ "','');");
					parentIndex = currentIndex;
					currentIndex++;

					for (int j = 0; j < pageList.size(); j++) {
						String ps[] = pageList.get(j);
						out.println("d.add(" + currentIndex + "," + parentIndex
								+ ",'" + ps[0] + "','" + ps[1] + "');");
						currentIndex++;
					}

				}
			}
			%>
			document.write(d);
        
	</script>

		<div></div>
	</body>
</html>
