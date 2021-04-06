<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.menu.WindowManager"%>
<%@page import="java.io.File"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="org.apache.poi.poifs.filesystem.POIFSFileSystem"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFSheet"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFRow"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFCell"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.InputStreamReader"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询玩家通过账号</title>
</head>
<body>

	<%
		String ss = request.getParameter("name");
		if(ss != null && !ss.equals("")){
			
			String fileName = request.getRealPath("/");
			File file = new File(fileName+ss);
			
			 Map<String,Player> map = new HashMap<String,Player>();
			 List<String> list = new ArrayList<String>();
			 BufferedReader br = null;
			 try{
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				
				String name = br.readLine();
				while(name != null){
					list.add(name);
					name = br.readLine();
				}
				
			 }catch(Exception e){
				 
			 }finally{
				 if(br != null){
					 br.close();
				 }
				               
			 }
			/* HSSFWorkbook workbook = null;
			
			 InputStream is = new FileInputStream(file);
			 POIFSFileSystem pss = new POIFSFileSystem(is);
			 workbook = new HSSFWorkbook(pss);
			 
			 int 账号 = 1;
			
			 
			 HSSFSheet sheet = null;
			 sheet = workbook.getSheetAt(0);
			 int rows = sheet.getPhysicalNumberOfRows();
			 HSSFRow row = null;
			 HSSFCell cell = null;

			 Map<String,Player> map = new HashMap<String,Player>();
			 List<String> list = new ArrayList<String>();
			 
			 for(int i= 1;i<rows;i++){
				 int x = 0;
				 int y = 0;
				 row = sheet.getRow(i);
				 if(row != null){
					 String account = null;
					 cell = row.getCell(账号);
					 if(cell != null){
						 account = cell.getStringCellValue().trim();
						 if(account != null){
							 list.add(account);
						 }
					 }
				 }
			 }
			 */
			 out.print("这个服玩家个数:"+list.size()+"<br/>");
		
			 PlayerManager pm = PlayerManager.getInstance();
			 for(String s:list){
				 Player[] pp = null;
				 try{
				 	pp = pm.getPlayerByUser(s);
				 }catch(Exception e){
					 out.print("没有找着玩家"+s+"<br/>");
				 }
				 if(pp != null &&pp.length > 0){
					 Player p = pp[0];
					 map.put(s,p);
				 }else{
					 out.print("账号错误"+s+"<br/>");
				 }
			 }
			 out.print("放入map成功<br/>");
			 int num = 0;
			 for(Map.Entry<String,Player> en: map.entrySet()){
				 out.print(++num+" 账号:"+en.getKey()+"    name:"+en.getValue().getName()+"<br/>");
			 }
			 
			out.print("成功<br/>");
			return;
		}
	
	%>
		<form action="">
			文件名:<input type="text" name="name" />
			<input type="submit" value="submit" />
		
		</form>


</body>
</html>