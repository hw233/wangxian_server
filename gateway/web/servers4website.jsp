<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*,com.fy.gamegateway.thirdpartner.congzhi.* "%>
<%@page import="com.xuanzhi.tools.text.ParamUtils;"%><%



//String beanName = "outer_gateway_connection_selector";
	MieshiServerInfoManager mm = MieshiServerInfoManager.getInstance();
	
	String SERVERTYPE_NAMES[] = MieshiServerInfo.SERVERTYPE_NAMES;
	

	String categories[] = mm.getServerCategories();

	String category = "";
	int priority = 0;
	String name = "";
	String realName = "";
	String ip = "";
	int port = 0;
	int status = 0;
	int serverType = 0;
	String desp = "";
	int clientId = 0;
	int serverId = 0;
	
	
	categories = mm.getServerCategories();
	StringBuffer categorySB = new StringBuffer();
	for(int i = 0 ; i < categories.length ; i++){
		categorySB.append(categories[i]);
		if(i < categories.length -1){
			categorySB.append(",");
		}
	}
	String colors[] = new String[]{"#FFFFFF","#888888","#888888","#ff00ff","#FFFFFF","#FFFFFF",
			"#888888","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF"};
	
%>
<%
	String jsons = "[";
	for(int i = 0 ; i < categories.length ; i++){
		MieshiServerInfo si[] = mm.getServerInfoSortedListByCategory(categories[i]);
	/*		for(int j = 0 ; j < si.length ; j++){
			MieshiServerInfo s = si[j];
			 if(s.getServerType() < colors.length) color = colors[s.getServerType()];
			out.println("<tr bgcolor='"+color+"' align='center'>");
			out.println("<td><input type='checkbox' name='realName' value='"+s.getRealname()+"'></td>");
			out.println("<td>"+s.getCategory()+"</td>");
			out.println("<td>"+s.getName()+"</td>");
			out.println("<td>"+MieshiServerInfo.STATUS_DESP[s.getStatus()]+"</td>");
			out.println("<td>"+SERVERTYPE_NAMES[s.getServerType()]+"</td>");
			out.println("<td>"+s.getDescription()+"</td>");
			out.println("</tr>"); 
		
			
		} */
		
		List<MieshiServerInfo> lst = new ArrayList<MieshiServerInfo>();
		for(MieshiServerInfo ms : si)
		{
			if(ms.getServerType() == MieshiServerInfo.SERVERTYPE_苹果正式服务器)
			{
				lst.add(ms);
			}
		}
		
		jsons  += "{\"servers\":" + JsonUtil.jsonFromObject(lst.toArray());
		jsons += "},";
	}
	jsons = jsons.substring(0,jsons.length()-1);
	jsons += "]";
	out.println(jsons);
%>	


