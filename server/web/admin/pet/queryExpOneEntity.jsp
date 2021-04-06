<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"><%!



%>

<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.sprite.pet.SingleForPetPropsEntity"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerMysqlImpl "%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.xuanzhi.tools.simplejpa.impl.MetaDataEntity"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.xuanzhi.tools.simplejpa.impl.MetaDataField"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.lang.reflect.Type"%>
<%@page import="com.xuanzhi.tools.text.JsonUtil"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询经验丹的经验</title>
</head>
<body>

	<%
	
	
	
		String exp = request.getParameter("exp");
		if(exp != null && !exp.equals("")){
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			SimpleEntityManagerMysqlImpl em = (SimpleEntityManagerMysqlImpl)aem.em;
			long id = Long.parseLong(exp);
			SingleForPetPropsEntity entity = (SingleForPetPropsEntity)em.select_object(id);
			
			long vv [] = entity.getValues();
			out.println(java.util.Arrays.toString(vv));
			
			out.println("=======================================<br>");
			
			String sql = "select T0.*,T1.*,T2.* from ARTICLEENTITY_S64 T0 left join ARTICLEENTITY_T1_S64 T1 on T0.ID=T1.ID_SEC_1 left join ARTICLEENTITY_T2_S64 T2 on T0.ID=T2.ID_SEC_2 where T0.ID=?";
			java.sql.Connection conn = em.getConnection();
			java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1,id);
			java.sql.ResultSet rs = pstmt.executeQuery();
			
			rs.next();
			SingleForPetPropsEntity entity2 = (SingleForPetPropsEntity)em.construct_object_from_resultset("",id,rs,1,new int[10]);
			 vv  = entity2.getValues();
			out.println(java.util.Arrays.toString(vv));
			
			out.println("=======================================<br>");
			
			out.println(rs.getString("VALUES2"));
			
			out.println("==================mde =====================<br>");
			
			
			Field field = SimpleEntityManagerMysqlImpl.class.getDeclaredField("mde");
			field.setAccessible(true);
			MetaDataEntity mde = (MetaDataEntity)field.get(em);
			MetaDataField mdf = mde.getMetaDataField(SingleForPetPropsEntity.class,"values");
			out.println("columnNameForMysql = "+mdf.columnNameForMysql+"<br>");
		
			field = SimpleEntityManagerMysqlImpl.class.getDeclaredField("column2IndexForSelectObject");
			field.setAccessible(true);
			HashMap<String,Integer> map2 = (HashMap<String,Integer>)field.get(em);
			Integer index = map2.get(mdf.columnNameForMysql);
			out.println("index = "+index+",and value = "+rs.getString(index)+"<br>");
			
			out.println("----------------column2IndexForSelectObject----------<br>");
			for(String s : map2.keySet()){
				int i = map2.get(s);
				out.println("map2: "+s+"="+i+"<br>");
			}
			out.println("----------------resultset----------<br>");
			
			for(int i = 1 ; i <= 53 ; i++){
				String s = rs.getString(i);
				out.println("result["+i+"]="+s+"<br>");
			}
		}
	
	
	%>


	<form action="">
		物品id:<input type="text" name="exp"/>
		<input type="submit" value="submit">
	</form>

</body>
</html>