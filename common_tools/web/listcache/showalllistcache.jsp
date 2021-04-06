<%@ page language="java" pageEncoding="GBK" contentType="text/html;charset=GBK" import="java.util.*,com.xuanzhi.tools.listcache.*,com.xuanzhi.tools.listcache.event.*,com.xuanzhi.tools.listcache.concrete.*"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>Show All List Cache</title>
<style>
td{ font-size:12px; color:#FF33FF; font-family:Arial; font-weight:bold;}
th{ background-color:#EEEEEE; border:1px solid #0059CC; align:center;}
.l{ text-align:right; padding-right:0px; vertical-align:top;}
.r{ text-align:left; padding-left:0px; vertical-align:bottom;}
.m{ text-align:center; height:20px; vertical-align:middle; color:#0059CC; font-weight:bold; font-size:16px;}
</style>
</head>
<%

	boolean show_id = false;
	boolean vertical = false;
	String fk = "";
	int fc = 1;

        String tmpStr = request.getParameter("vertical");
        if(tmpStr != null && tmpStr.equals("true"))
        {
                vertical = true;
        }

        tmpStr = request.getParameter("show_id");
        if(tmpStr != null && tmpStr.equals("true"))
        {
                show_id = true;
                vertical = true;
        }

	fk = request.getParameter("fkeyword");
	if(fk == null) fk = "";

	tmpStr = request.getParameter("fcontain");
	if(tmpStr != null && tmpStr.equals("0"))
		fc = 0;
		
	HashSet set = new HashSet();
	ListCacheEventHandler.searchListener(set,fk);
    llc = (LruListCache[])set.toArray(new LruListCache[0]);

	int total = 0;
	int maxBlockSize = 0;
	
	for(int i = 0 ; i < llc.length ; i++){
		total +=llc[i].getCachedSize();
		if(llc[i].getMaxBlockSize() > maxBlockSize)
			maxBlockSize = llc[i].getMaxBlockSize();
	}
	int minBlockSize = maxBlockSize;
	for(int i = 0 ; i < llc.length ; i++){
		if(llc[i].getMinBlockSize() < minBlockSize)
			minBlockSize = llc[i].getMinBlockSize();
	}
	
	int left_width = 30;
	int right_width = 30;
	int middle_width = 60;
	int total_width = (left_width + right_width + middle_width) * llc.length;
	int max_height = 150;
	int min_height = 20;

	

%>
<body>
<br/><center><b>Total Cache: <%=llc.length%>, Total Cached Size: <%=total%></b></center><br/>
<br/><center><a href="showalllistcache.jsp?fkeyword=<%=fk%>&fcontain=<%=fc%>">横向显示</a> | <a href="showalllistcache.jsp?vertical=true&fkeyword=<%=fk%>&fcontain=<%=fc%>">纵向显示</a> | <a href="showalllistcache.jsp?show_id=true&fkeyword=<%=fk%>&fcontain=<%=fc%>">显示ID</a></center><br/>
<center><form action="showalllistcache.jsp"><input type="hidden" name="vertical" value="<%=vertical%>"><input type="hidden" name="show_id" value="<%=show_id%>">
请输入过虑关键字<input type="text" name="fkeyword" value="<%=fk%>"> | <input type="radio" name="fcontain" <%=(fc==1?"checked":"")%> value="1">包含    <input type="radio" name="fcontain" <%=(fc==0?"checked":"")%> value="0">不包含 | <input type="submit" value="提  交"></form><br/></center>
<% if(vertical) {%>
<table border="0" align="center" cellpadding="0" cellspacing="0" width="<%=total_width%>">	
<tr><%
        for(int i = 0 ; i < llc.length ; i++){
                %><td align="center" width="<%=(left_width + right_width + middle_width)%>"><b><font size="3" color="#333333"><%=llc[i].getName()%></font></b></td><%
        }
%></tr>
<tr><%
	for(int i = 0 ; i < llc.length ; i++){
		 String objectName = llc[i].getName() +"."+ llc[i].getProperty();
		%><td nowrap="nowrap" align="center" width="<%=(left_width + right_width + middle_width)%>"><%=(objectName)%><br/>CS/TS: <%=llc[i].getCachedSize()%>/<%=llc[i].getTotalSize()%></td><%
	}
%></tr>
<tr><%
	for(int i = 0 ; i < llc.length ; i++){
		%><td valign="top" align="center"><table border="0" align="center" cellpadding="0" cellspacing="0"><%
		AvlTree avlTree = llc[i].getAvlTree();
		BlockTreeNode node = null;
		if(!avlTree.isEmpty()){
			node = avlTree.minimum();
			int height = (int)(min_height + (max_height - min_height)*(nodes[0].getSizeNumber() - minBlockSize)*1.0/(maxBlockSize - minBlockSize));
			%><tr>
			  <td width="<%=left_width%>" class="l"><%=nodes[0].getStartNumber()%></td>
			  <th nowrap="nowrap" width="<%=middle_width%>" height="<%=height%>"><%
			  	if(!show_id){
			  		out.print("" + (node.getLeftLifeTime(llc[i].getMaxBlockLifeTime())/1000)+"s");
				}else{
			  		Object ids[] = node.getIds();
			  		int sz = node.getSizeNumber();
			  		StringBuffer sb = new StringBuffer();
			  		%><table border="0" align="center" bgcolor="#73A5E6" cellpadding="0" cellspacing="1" width="100%"><%
			  		for(int k = 0 ; k < sz ; k++){
						Object fieldValue = "-"; //llc[i].getFieldValue(ids[k]);
			  			sb.append("<tr><td bgcolor='#EEEEEE'>"+ids[k] + "</td><td bgcolor='#DDDDDD'>"+fieldValue+"</td></tr>");
					}
			  		out.print(sb.toString());
			  		%></table><%
			  	}
			  %></th>
			  <td width="<%=right_width%>" class="r"><%=node.getSizeNumber()%></td>
			</tr><%
		}
		BlockTreeNode previous = avlTree.minimum();
		node = avlTree.next(previous);
		while(node != null){
			if(node.getStartNumber() == previous.getStartNumber() + previous.getSizeNumber()){
				%><tr><td colspan="3" class="m">||</td><%
			}else{
				%><tr><td colspan="3" class="m">↓</td><%
			}
			int height = (int)(min_height + (max_height - min_height)*(node.getSizeNumber() - minBlockSize)*1.0/(maxBlockSize - minBlockSize));
			%><tr>
			  <td width="<%=left_width%>" class="l"><%=node.getStartNumber()%></td>
			  <th width="<%=middle_width%>" height="<%=height%>"><%
				if(!show_id)
			  		out.print("" + (node.getLeftLifeTime(llc[i].getMaxBlockLifeTime())/1000)+"s");
			  	else{
			  		Object ids[] = node.getIds();
			  		int sz = nodes[j].getSizeNumber();
			  		StringBuffer sb = new StringBuffer();
			  		%><table bgcolor="#73A5E6" border="0" align="center" cellpadding="0" cellspacing="1" width="100%"><%
			  		for(int k = 0 ; k < sz ; k++){
			  			Object fieldValue = "-";
			  			sb.append("<tr><td bgcolor='#EEEEEE'>"+ids[k] + "</td><td bgcolor='#DDDDDD'>"+fieldValue+"</td></tr>");
					}
			  		out.print(sb.toString());
			  		%></table><%
			 	}
				%></th><td width="<%=right_width%>" class="r"><%=node.getSizeNumber()%></td></tr><%	
				
			previous = node;
			node = avlTree.next(previous);	
		}
		%></table></td><%
	}
%></tr>
</table>
<%}else{
	int width1 = 50;
	int width2 = 50;

	int min_width = 30;
	int max_width = 400;
	total_width = 100;
	for(int i = 0 ; i < llc.length ; i++){
		int dw = 0;
		AvlTree avlTree = llc[i].getAvlTree();
		BlockTreeNode node = avlTree.minimum();
		
	 	while(node != null){
			dw += (int)(min_width+ (max_width- min_width)*(node.getSizeNumber() - minBlockSize)*1.0/(maxBlockSize - minBlockSize));
			node = avlTree.next(node);
		}
		if(total_width < dw)
			total_width = dw;
	}

	%><table border="0" align="left" cellpadding="0" cellspacing="0" width="<%=total_width%>"><%
        for(int i = 0 ; i < llc.length ; i++){
                String objectName = llc[i].getName() +"."+ llc[i].getProperty();

	%><tr height="60"><td class="m" width="<%=width1%>"><%=llc[i].getName()%></td><td class="m" width="<%=width2%>"><%=llc[i].getProperty() %></td><td class="m" width="width1">CS/TS: <br/><%=llc[i].getCachedSize()%>/<%=llc[i].getTotalSize()%></td><td><table border="0" align="left" cellpadding="0" cellspacing="0"><%

		AvlTree avlTree = llc[i].getAvlTree();
		BlockTreeNode node = avlTree.minimum();

		%><tr height="15"><%

		if(avlTree.size() > 0){
			%><td class="r"><%=node.getStartNumber()%></td><%	
		}
		node = avlTree.next(node);
		while(node != null){
			%><td class="m">&nbsp;</td><td class="r"><%=node.getStartNumber()%></td><%
			node = avlTree.next(node);	
		}
		if(avlTree.size() == 0){
			%><td class="m">&nbsp;</td><%
		}
	
		%></tr><tr height="30"><% 
		// int height = (int)(min_height + (max_height - min_height)*(nodes[0].getSizeNumber() - minBlockSize)*1.0/(maxBlockSize - minBlockSize));
		if(avlTree.size() > 0){
			node = avlTree.minimum();
		 	int bw = (int)(min_width+ (max_width- min_width)*(node.getSizeNumber() - minBlockSize)*1.0/(maxBlockSize - minBlockSize));
                        %><th width="<%=bw%>"><%=(node.getLeftLifeTime(llc[i].getMaxBlockLifeTime())/1000)%>s</th><%
            }
            BlockTreeNode previous = node;
            node = avlTree.next(previous);
            while(node != null){
		 		int bw = (int)(min_width+ (max_width- min_width)*(node.getSizeNumber() - minBlockSize)*1.0/(maxBlockSize - minBlockSize));
				if(node.getStartNumber() == previous.getStartNumber() + previous.getSizeNumber()){
	                        %><td class="m">≡</td><%
				}else{
				%><td class="m">→</td><%
				}
				%><th width="<%=bw%>"><%=(node.getLeftLifeTime(llc[i].getMaxBlockLifeTime())/1000)%>s</th><%
				previous = node;
				node = avlTree.next(previous);
            }
		if(avlTree.size() == 0){
                        %><td class="m" width="<%=(total_width - width1 - width2)%>">&nbsp;</td><%
                }
	
		%></tr><tr height="15"><%
				node = avlTree.minimum();
                if(avlTree.size()> 0){
                        %><td class="l"><%=node.getSizeNumber()%></td><%
                }
                
                node = avlTree.next(node);
                while(node != null){
                        %><td class="m">&nbsp;</td><td class="l"><%=node.getSizeNumber()%></td><%
                        node = avlTree.next(node);
                }
		if(avlTree.size() == 0){
                        %><td class="m">&nbsp;</td><%
                }
		%></tr></table></td></tr><%
        }
	%></table><%
}%>
</body>
</html>
