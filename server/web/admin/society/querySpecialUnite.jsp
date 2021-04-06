<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>


<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@page import="com.fy.engineserver.society.Relation"%>
<%@page import="com.fy.engineserver.unite.Unite"%>
<%@page import="com.fy.engineserver.unite.UniteManager"%><html>
<head>
<title>查询指定结义</title>
</head>
<body>

<%
	String name = request.getParameter("name");
	String set = request.getParameter("set");

	
	if(name != null && !name.equals("")){
		long uId = Long.parseLong(name);
		if(set != null && !set.equals("")){
			//修改
			//只查询
			Unite u = UniteManager.getInstance().getUnite(uId);
			if(u != null){
				int i =0;
				List<Long> tempList = new ArrayList<Long>();
				for(long id :u.getMemberIds()){
					Relation r = SocialManager.getInstance().getRelationById(id);
					if(r.getUniteId() != uId){
						tempList.add(id);
					}
				}
				if(tempList != null && tempList.size() >0){
					for(Long tid:tempList){
						u.getMemberIds().remove(tid);
						UniteManager.logger.error("[后台修改结义成员] ["+u.getLogString()+"] ["+tid+"]");
					}
					u.setMemberIds(u.getMemberIds());
					out.print("修改成员完成");
				}else{
					out.print("没有发生修改");
				}
			}else{
				out.print("指定id unite null"+uId);
			}
		}else{
			//只查询
			Unite u = UniteManager.getInstance().getUnite(uId);
			if(u != null){
				int i =0;
				List<Long> tempList = new ArrayList<Long>();
				for(long id :u.getMemberIds()){
					if(!tempList.contains(id)){
						tempList.add(id);
					}else{				
						UniteManager.logger.error("[后台修改结义成员] [重复] ["+u.getLogString()+"] ["+id+"]");
					}
					Relation r = SocialManager.getInstance().getRelationById(id);
					out.print(i+"   id:"+id+"结义id:"+r.getUniteId()+"<br/>");
					++i;
				}
				if(tempList.size() != u.getMemberIds().size()){
					u.setMemberIds(tempList);
				}
			}else{
				out.print("指定id unite null"+uId);
			}
		}
		return;
	}
%>


<form action="">
	
	查询id:<input type="text" name="name"/>
	修改(空为不修改值查询)<input type="text" name="set"/>
	<input type="submit" value="submit"/>
</form>



</body>

</html>
